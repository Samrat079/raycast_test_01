import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;

public class GameWindow extends JPanel implements KeyListener, MouseMotionListener {
    Level map;

    int tileSize = 64;
    double playerX = tileSize * 1.5;
    double playerY = tileSize * 1.5;
    double playerAngle = 0;

    double speed = 150;
    double mouseSensitivity = 0.002;

    long lastTime = System.nanoTime();

    boolean forward, backward, left, right;

    Robot robot;
    boolean recenter = false;

    public GameWindow() {
        setPreferredSize(new Dimension(640, 420));
        setFocusable(true);

        addKeyListener(this);
        addMouseMotionListener(this);

        Timer timer = new Timer(16, e -> update());
        timer.start();
        map = new Level(40, 36, 64);

        try {
            robot = new Robot();
        } catch (AWTException e) {
            e.printStackTrace();
        }

        hideCursor();
    }

    private void hideCursor() {
        BufferedImage img = new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB);
        Cursor blank = Toolkit.getDefaultToolkit()
                .createCustomCursor(img, new Point(0, 0), "blank");
        setCursor(blank);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        int screenWidth = getWidth();
        int screenHeight = getHeight();

//        Sky Box
        g.setColor(Color.CYAN);
        g.fillRect(0, 0, screenWidth, screenHeight / 2);

//        Floor
        g.setColor(Color.GRAY);
        g.fillRect(0, screenHeight / 2, screenWidth, screenHeight / 2);

        double fov = Math.PI / 3;

        for (int x = 0; x < screenWidth; x++) {

            RayResult ray = castRay(fov, x, screenWidth);

            double correctedDistance = ray.distance * Math.cos(ray.angle - playerAngle);

            int wallHeight = (int)(screenHeight * tileSize / correctedDistance);

            int start = screenHeight / 2 - wallHeight / 2;

            switch (ray.wallType) {
                case 1 -> g.setColor(Color.BLUE);
                case 2 -> g.setColor(Color.RED);
                case 3 -> g.setColor(Color.GREEN);
                case 4 -> g.setColor(Color.ORANGE);
                case 5 -> g.setColor(Color.YELLOW);
                default -> g.setColor(Color.WHITE);
            }

            g.drawLine(x, start, x, start + wallHeight);
        }
    }

    private RayResult castRay(double fov, int column, int screenWidth) {

        double rayAngle = playerAngle - fov / 2 + fov * column / screenWidth;

        double rayX = Math.cos(rayAngle);
        double rayY = Math.sin(rayAngle);

        double distance = 0;

        while (distance < 800) {
            distance += 1;

            double worldX = playerX + rayX * distance;
            double worldY = playerY + rayY * distance;

            int wall = map.getWallType(worldX, worldY);

            if (wall > 0) {
                return new RayResult(distance, wall, rayAngle);
            }
        }

        return new RayResult(800, 1, rayAngle);
    }

    private void update() {

        long now = System.nanoTime();
        double deltaTime = (now - lastTime) / 1_000_000_000.0;
        lastTime = now;

        double moveStep = speed * deltaTime;

        double moveX = 0;
        double moveY = 0;

        if (forward) {
            moveX += Math.cos(playerAngle);
            moveY += Math.sin(playerAngle);
        }
        if (backward) {
            moveX -= Math.cos(playerAngle);
            moveY -= Math.sin(playerAngle);
        }
        if (left) {
            moveX += Math.cos(playerAngle - Math.PI/2);
            moveY += Math.sin(playerAngle - Math.PI/2);
        }
        if (right) {
            moveX -= Math.cos(playerAngle - Math.PI/2);
            moveY -= Math.sin(playerAngle - Math.PI/2);
        }

        double length = Math.hypot(moveX, moveY);

        if (length > 0) {
            moveX = moveX / length * moveStep;
            moveY = moveY / length * moveStep;
        }

        if (map.getWallType(playerX + moveX, playerY) == 0)
            playerX += moveX;

        if (map.getWallType(playerX, playerY + moveY) == 0)
            playerY += moveY;

        repaint();
    }

    private static class RayResult {
        double distance;
        int wallType;
        double angle;

        RayResult(double d, int w, double a) {
            distance = d;
            wallType = w;
            angle = a;
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_W -> forward = true;
            case KeyEvent.VK_S -> backward = true;
            case KeyEvent.VK_A -> left = true;
            case KeyEvent.VK_D -> right = true;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_W -> forward = false;
            case KeyEvent.VK_S -> backward = false;
            case KeyEvent.VK_A -> left = false;
            case KeyEvent.VK_D -> right = false;
        }
    }

    @Override public void keyTyped(KeyEvent e) {}

    @Override
    public void mouseMoved(MouseEvent e) {
        if (recenter) {
            recenter = false;
            return;
        }

        int centerX = getWidth()/2;
        int dx = e.getX() - centerX;

        playerAngle += dx * mouseSensitivity;
        playerAngle %= Math.PI * 2;

        recenter = true;
        Point p = getLocationOnScreen();
        robot.mouseMove(p.x + centerX,p.y + getHeight()/2);
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        mouseMoved(e);
    }
}