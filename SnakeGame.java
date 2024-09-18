import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Random;

public class SnakeGame1 extends JFrame implements ActionListener, KeyListener {

    private static final long serialVersionUID = 1L;

    private static final int GRID_SIZE = 20;
    private static final int CELL_SIZE = 25;

    private int[][] grid;
    private int snakeLength;
    private int[] snakeX, snakeY;
    private int direction; // 0: up, 1: right, 2: down, 3: left
    private boolean running;
    private Timer timer;
    private int foodX, foodY;

    public SnakeGame() {
        setTitle("Snake Game");
        setSize(GRID_SIZE * CELL_SIZE, GRID_SIZE * CELL_SIZE);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        grid = new int[GRID_SIZE][GRID_SIZE];
        snakeX = new int[GRID_SIZE * GRID_SIZE];
        snakeY = new int[GRID_SIZE * GRID_SIZE];

        direction = 1; // initially moving right
        running = true;
        snakeLength = 3;

        spawnSnake();
        spawnFood();

        timer = new Timer(100, this);
        timer.start();

        addKeyListener(this);
        setFocusable(true);
    }

    private void spawnSnake() {
        for (int i = 0; i < snakeLength; i++) {
            snakeX[i] = GRID_SIZE / 2 - i;
            snakeY[i] = GRID_SIZE / 2;
            grid[snakeY[i]][snakeX[i]] = 1; // mark snake body on the grid
        }
    }

    private void spawnFood() {
        Random random = new Random();
        foodX = random.nextInt(GRID_SIZE);
        foodY = random.nextInt(GRID_SIZE);

        // Make sure the food doesn't spawn on the snake
        while (grid[foodY][foodX] == 1) {
            foodX = random.nextInt(GRID_SIZE);
            foodY = random.nextInt(GRID_SIZE);
        }

        grid[foodY][foodX] = 2; // mark food on the grid
    }

    private void move() {
        int newHeadX = snakeX[0];
        int newHeadY = snakeY[0];

        switch (direction) {
            case 0:
                newHeadY--;
                break;
            case 1:
                newHeadX++;
                break;
            case 2:
                newHeadY++;
                break;
            case 3:
                newHeadX--;
                break;
        }

        // Check if the new head collides with the snake body or the game borders
        if (newHeadX < 0 || newHeadX >= GRID_SIZE || newHeadY < 0 || newHeadY >= GRID_SIZE
                || grid[newHeadY][newHeadX] == 1) {
            running = false;
            timer.stop();
            JOptionPane.showMessageDialog(this, "Game Over! Your score: " + (snakeLength - 3));
            System.exit(0);
        }

        // Check if the new head reaches the food
        if (newHeadX == foodX && newHeadY == foodY) {
            snakeLength++;
            spawnFood();
        } else {
            // If no food is eaten, remove the tail of the snake
            grid[snakeY[snakeLength - 1]][snakeX[snakeLength - 1]] = 0;
        }

        // Update the position of the snake's head
        for (int i = snakeLength - 1; i > 0; i--) {
            snakeX[i] = snakeX[i - 1];
            snakeY[i] = snakeY[i - 1];
        }

        snakeX[0] = newHeadX;
        snakeY[0] = newHeadY;

        // Mark the new position of the snake on the grid
        for (int i = 0; i < snakeLength; i++) {
            grid[snakeY[i]][snakeX[i]] = 1;
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (running) {
            move();
            repaint();
        }
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);

        // Draw the grid
        for (int i = 0; i < GRID_SIZE; i++) {
            for (int j = 0; j < GRID_SIZE; j++) {
                if (grid[i][j] == 1) {
                    g.setColor(Color.GREEN); // Snake body
                } else if (grid[i][j] == 2) {
                    g.setColor(Color.RED); // Food
                } else {
                    g.setColor(Color.WHITE); // Empty cell
                }

                g.fillRect(j * CELL_SIZE, i * CELL_SIZE, CELL_SIZE, CELL_SIZE);
                g.setColor(Color.BLACK);
                g.drawRect(j * CELL_SIZE, i * CELL_SIZE, CELL_SIZE, CELL_SIZE);
            }
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {
        int key = e.getKeyCode();

        if ((key == KeyEvent.VK_UP) && (direction != 2)) {
            direction = 0;
        } else if ((key == KeyEvent.VK_RIGHT) && (direction != 3)) {
            direction = 1;
        } else if ((key == KeyEvent.VK_DOWN) && (direction != 0)) {
            direction = 2;
        } else if ((key == KeyEvent.VK_LEFT) && (direction != 1)) {
            direction = 3;
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyReleased(KeyEvent e) {
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            SnakeGame game = new SnakeGame();
            game.setVisible(true);
        });
    }
}
