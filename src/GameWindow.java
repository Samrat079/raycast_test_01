import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;

public class GameWindow extends JPanel implements KeyListener, MouseMotionListener {
    int[][] map = {
            {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1},
            {1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1},
            {1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1},
            {1, 0, 0, 0, 0, 1, 1, 1, 0, 0, 1, 1, 1, 0, 0, 0, 0, 0, 0, 1},
            {1, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 1, 0, 0, 1, 0, 1, 0, 1},
            {1, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 1},
            {1, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 1, 0, 0, 1, 0, 1, 0, 1},
            {1, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 1},
            {1, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 1, 0, 0, 1, 0, 1, 0, 1},
            {1, 0, 0, 0, 0, 1, 1, 1, 0, 0, 1, 1, 1, 0, 0, 0, 0, 0, 0, 1},
            {1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1},
            {1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1},
            {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1},
    };

    Robot robot;
    boolean recenter = false;

    int tileSize = 64;
    double playerX = 160;
    double playerY = 160;
    double playerAngle = 0;
    double mouseSensitivity = 0.002;
    double speed = 200;

    boolean forward, backward, left, right;

    public GameWindow() {
        setPreferredSize(new Dimension(640, 420));
        setFocusable(true);

        addKeyListener(this);
        addMouseMotionListener(this);

        // Game timer
        Timer timer = new Timer(16, e -> update());
        timer.start();

        // Cursor hider
        try  {
                robot = new Robot();
        } catch (AWTException e) {
            e.printStackTrace();
        }
        hideCursor();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        int screenWidth = getWidth(); // Also is the number of rays
        int screenHeight = getHeight();

        double fov = Math.PI / 3;

        for (int i = 0; i < screenWidth; i++) {
            double rayAngle = playerAngle - fov / 2 + fov * i / screenWidth;
            double rayX = Math.cos(rayAngle);
            double rayY = Math.sin(rayAngle);

            double distance = 0;
            boolean hitWall = false;

            while (!hitWall && distance < 800) {
                distance += 1;

                int testX = (int) ((playerX + rayX * distance) / tileSize);
                int testY = (int) ((playerY + rayY * distance) / tileSize);

                if (map[testY][testX] == 1) hitWall = true;
            }

            double correctedDistance = distance * Math.cos(rayAngle - playerAngle);
            int wallHeight = (int) (screenHeight * tileSize / correctedDistance);
            int start = screenHeight / 2 - wallHeight / 2;

            g.setColor(Color.BLUE);
            g.drawLine(i, start, i, start + wallHeight);
        }
    }

    public void update() {
        if (forward) {
            playerX += Math.cos(playerAngle) * speed;
            playerY += Math.sin(playerAngle) * speed;
        }

        if (backward) {
            playerX -= Math.cos(playerAngle) * speed;
            playerY -= Math.sin(playerAngle) * speed;
        }

        if (left) {
            playerX += Math.cos(playerAngle - Math.PI / 2) * speed;
            playerY += Math.sin(playerAngle - Math.PI / 2) * speed;
        }

        if (right) {
            playerX -= Math.cos(playerAngle - Math.PI / 2) * speed;
            playerY -= Math.sin(playerAngle - Math.PI / 2) * speed;
        }

        repaint();
    }

    public void hideCursor() {
        BufferedImage img = new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB);
        Cursor blankCursor = Toolkit.getDefaultToolkit().createCustomCursor(img, new Point(0, 0), "blank");
        setCursor(blankCursor);
    }


    @Override
    public void keyPressed(KeyEvent keyEvent) {
        switch (keyEvent.getKeyCode()) {
            case KeyEvent.VK_A -> left = true;
            case KeyEvent.VK_D -> right = true;
            case KeyEvent.VK_W -> forward = true;
            case KeyEvent.VK_S -> backward = true;
        }
    }

    @Override
    public void keyReleased(KeyEvent keyEvent) {
        switch (keyEvent.getKeyCode()) {
            case KeyEvent.VK_A -> left = false;
            case KeyEvent.VK_D -> right = false;
            case KeyEvent.VK_W -> forward = false;
            case KeyEvent.VK_S -> backward = false;
        }
    }

    @Override
    public void mouseMoved(MouseEvent mouseEvent) {
        if (recenter) {
            recenter = false;
            return;
        }

        int centerX = getWidth() / 2;
        int centerY = getHeight() / 2;

        int dx = mouseEvent.getX() - centerX;

        playerAngle += dx * mouseSensitivity;
        playerAngle %= Math.PI * 2;

        recenter = true;
        Point windowPos = getLocationOnScreen();
        robot.mouseMove(windowPos.x + centerX, windowPos.y + centerY);
    }

    @Override
    public void mouseDragged(MouseEvent mouseEvent) {
        mouseMoved(mouseEvent);
    }

    @Override
    public void keyTyped(KeyEvent keyEvent) {
    }
}
