package com.company;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.TimerTask;

public class Board extends JPanel implements ActionListener {

    private final int LEVEL_HEIGHT = 31;
    private final int LEVEL_WIDTH = 28;
    private final int BLOCK_SIZE = 23;
    private final Dimension WINDOW_SIZE = new Dimension(660, 830);
    private final String ALL_DIRS = "wasd";
    private final int SCREEN_HEIGHT = LEVEL_HEIGHT * BLOCK_SIZE;
    private final int SCREEN_WIDTH = LEVEL_WIDTH * BLOCK_SIZE;


    private int py, px;
    private int pyOld, pxOld;
    private char pDir;
    private char pDirOld;

    private Timer moveTimer;
    private java.util.Timer mainTimer = new java.util.Timer();
    TimerTask task = new TimerTask() {
        @Override
        public void run() {
            timeLeft--;
        }
    };
    long timeLeft;
    private int left;
    private int score;
    private int mapNr;

    private char[][] levelData = new char[LEVEL_HEIGHT][LEVEL_WIDTH];
    private double multiplier = 1;
    private boolean initialized = false;

    private boolean dead = false;

    public Board(int mapNr) {
        this.mapNr = mapNr;
        initVar();
        initBoard();

    }

    private void initBoard() {
        addKeyListener(new TAdapter());
        setFocusable(true);
        setSize(600, 1000);
        setBackground(Color.black);
        loadMap(mapNr);
    }


    public void initVar() {
        timeLeft = 40;
        moveTimer = new Timer(70, this);
        score = 0;
        left = 0;
    }


    private void drawMaze(Graphics2D g2d) {

        int x, y;

        for (y = 0; y < LEVEL_HEIGHT; y++) {
            for (x = 0; x < LEVEL_WIDTH; x++) {

                g2d.setColor(Color.white);
                g2d.setStroke(new BasicStroke(1));

                if ((levelData[y][x] == '6') || (levelData[y][x] == '^')) {
                    g2d.drawLine((x * BLOCK_SIZE) + 12, y * BLOCK_SIZE, (x * BLOCK_SIZE) + 12, y * BLOCK_SIZE + BLOCK_SIZE);
                }


                if ((levelData[y][x] == '%') || (levelData[y][x] == '5')) {
                    g2d.drawLine(x * BLOCK_SIZE, y * BLOCK_SIZE + 12, x * BLOCK_SIZE + BLOCK_SIZE, y * BLOCK_SIZE + 12);
                }

                if ((levelData[y][x] == '1') || (levelData[y][x] == '!')) {
                    g2d.drawLine(x * BLOCK_SIZE + 12, y * BLOCK_SIZE + 12, x * BLOCK_SIZE + 12, y * BLOCK_SIZE + BLOCK_SIZE);
                    g2d.drawLine(x * BLOCK_SIZE + 12, y * BLOCK_SIZE + 12, x * BLOCK_SIZE + BLOCK_SIZE, y * BLOCK_SIZE + 12);
                }
                if ((levelData[y][x] == '2') || (levelData[y][x] == '@')) {
                    g2d.drawLine(x * BLOCK_SIZE + 12, y * BLOCK_SIZE + 12, x * BLOCK_SIZE + 12, y * BLOCK_SIZE + BLOCK_SIZE);
                    g2d.drawLine(x * BLOCK_SIZE, y * BLOCK_SIZE + 12, x * BLOCK_SIZE + BLOCK_SIZE - 12, y * BLOCK_SIZE + 12);
                }
                if ((levelData[y][x] == '4') || (levelData[y][x] == '$')) {
                    g2d.drawLine(x * BLOCK_SIZE + 12, y * BLOCK_SIZE + 12, x * BLOCK_SIZE + 12, y * BLOCK_SIZE);
                    g2d.drawLine(x * BLOCK_SIZE, y * BLOCK_SIZE + 12, x * BLOCK_SIZE + 11, y * BLOCK_SIZE + 12);
                }
                if ((levelData[y][x] == '3') || (levelData[y][x] == '#')) {
                    g2d.drawLine(x * BLOCK_SIZE + 12, y * BLOCK_SIZE + 12, x * BLOCK_SIZE + 12, y * BLOCK_SIZE);
                    g2d.drawLine(x * BLOCK_SIZE + 12, y * BLOCK_SIZE + 12, x * BLOCK_SIZE + BLOCK_SIZE, y * BLOCK_SIZE + 12);
                }

                if (levelData[y][x] == '.') {
                    g2d.setColor(Color.yellow);
                    g2d.fillRect(x * BLOCK_SIZE + 12, y * BLOCK_SIZE + 12, 2, 2);
                }

                if (levelData[y][x] == 'x') {
                    g2d.setColor(Color.GREEN);
                    g2d.drawLine(x * BLOCK_SIZE, y * BLOCK_SIZE, x * BLOCK_SIZE + 23, y * BLOCK_SIZE + 23);
                    g2d.drawLine(x * BLOCK_SIZE, y * BLOCK_SIZE + 23, x * BLOCK_SIZE + 23, y * BLOCK_SIZE);
                }
            }
        }
    }

    public void loadMap(int mapNr) {

        File file;
        if (mapNr == 1) {
            file = new File("map1.txt");
        } else {
            file = new File("map2.txt");
        }
        Scanner scanner = null;

        try {
            scanner = new Scanner(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        for (int i = 0; i < LEVEL_HEIGHT; i++) {
            for (int j = 0; j < LEVEL_WIDTH; j++) {

                scanner.useDelimiter("");
                levelData[i][j] = scanner.next().charAt(0);
                if (levelData[i][j] == 'x') {
                    py = i;
                    px = j;
                }
                if (levelData[i][j] == '.') {
                    left++;
                }
            }
        }
    }

    private void playerMove() {
        {
            if (!testForCollision()) {
                levelData[pyOld][pxOld] = ' ';
                if (levelData[py][px] != ' ') {
                    score += 10 * multiplier;
                    multiplier += 0.25;
                    --left;
                } else {
                    multiplier = 1;
                }
                levelData[py][px] = 'x';
                pDirOld = pDir;
            }
        }

    }

    boolean testForCollision() {
        pxOld = px;
        pyOld = py;
        switch (pDir) {
            case 'a':
                if (px == 0) {
                    px = LEVEL_WIDTH - 1;
                } else if ((levelData[py][px - 1] == ' ') || (levelData[py][px - 1] == '.')) {
                    px--;
                }
                break;
            case 'd':
                if (px == LEVEL_WIDTH - 1) {
                    px = 0;
                } else if ((levelData[py][px + 1] == ' ') || (levelData[py][px + 1] == '.')) {
                    px++;
                }
                break;
            case 'w':
                if ((levelData[py - 1][px] == ' ') || (levelData[py - 1][px] == '.')) {
                    --py;
                }
                break;
            case 's':
                if ((levelData[py + 1][px] == ' ') || (levelData[py + 1][px] == '.')) {
                    ++py;
                }
        }
        if (px == pxOld && py == pyOld) {
            return true;
        }
        return false;
    }

    @Override
    public void paintComponent(Graphics g) {
        if (timeLeft >= 0) {
            if (left != 0) {
                do {
                    playerMove();
                    super.paintComponent(g);
                    drawGame(g);
                    moveTimer.start();
                } while (!moveTimer.isRunning());
            } else {
                gameWon(g);
            }
        } else {
            gameLost(g);
        }
    }

    private void gameLost(Graphics g) {
        dead = true;
        Graphics2D g2d = (Graphics2D) g;
        g2d.setColor(new Color(0, 32, 72));
        g2d.fillRect(50, SCREEN_HEIGHT / 2 - 30, SCREEN_WIDTH - 100, 50);
        g2d.setColor(Color.white);
        g2d.drawRect(50, SCREEN_HEIGHT / 2 - 30, SCREEN_WIDTH - 100, 50);

        String s = "YOU LOST," + " press enter to return";
        Font small = new Font("Helvetica", Font.BOLD, 14);
        FontMetrics metr = this.getFontMetrics(small);

        g2d.setColor(Color.red);
        g2d.setFont(small);
        g2d.drawString(s, (SCREEN_WIDTH - metr.stringWidth(s)) / 2, SCREEN_HEIGHT / 2);
    }

    public void gameWon(Graphics g) {
        dead = true;
        mainTimer.cancel();
        Graphics2D g2d = (Graphics2D) g;
        g2d.setColor(new Color(0, 32, 72));
        g2d.fillRect(50, SCREEN_HEIGHT / 2 - 30, SCREEN_WIDTH - 100, 50);
        g2d.setColor(Color.white);
        g2d.drawRect(50, SCREEN_HEIGHT / 2 - 30, SCREEN_WIDTH - 100, 50);

        String s = "YOU WON," + " press enter to return";
        Font small = new Font("Helvetica", Font.BOLD, 14);
        FontMetrics metr = this.getFontMetrics(small);

        g2d.setColor(Color.green);
        g2d.setFont(small);
        g2d.drawString(s, (SCREEN_WIDTH - metr.stringWidth(s)) / 2, SCREEN_HEIGHT / 2);
    }

    private void showIntroScreen(Graphics2D g2d) {

        g2d.setColor(new Color(0, 32, 72));
        g2d.fillRect(50, SCREEN_HEIGHT / 2 - 30, SCREEN_WIDTH - 100, 50);
        g2d.setColor(Color.white);
        g2d.drawRect(50, SCREEN_HEIGHT / 2 - 30, SCREEN_WIDTH - 100, 50);

        String s = "Press any key to start.";
        Font small = new Font("Helvetica", Font.BOLD, 14);
        FontMetrics metr = this.getFontMetrics(small);

        g2d.setColor(Color.white);
        g2d.setFont(small);
        g2d.drawString(s, (SCREEN_WIDTH - metr.stringWidth(s)) / 2, SCREEN_HEIGHT / 2);
    }

    private void drawScore(Graphics2D g) {

        g.setFont(new Font("Font", Font.BOLD, 26));
        g.setColor(Color.white);
        String points = "POINTS: " + score;
        String mp = "X: " + multiplier;
        String time = "TIME LEFT: " + timeLeft;


        g.drawString(points, 440, 758);
        g.drawString(time, 30, 758);
        g.drawString(mp, 273, 758);

    }

    private void drawGame(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;

        g2d.setColor(Color.black);
        g2d.fillRect(0, 0, WINDOW_SIZE.width, WINDOW_SIZE.height);

        if (!initialized) {
            showIntroScreen(g2d);
        }

        drawMaze(g2d);
        drawScore(g2d);
    }

    class TAdapter extends KeyAdapter {

        @Override
        public void keyPressed(KeyEvent e) {
            char key = Character.toLowerCase(e.getKeyChar());
            if (initialized) {
                if (ALL_DIRS.contains(Character.toString(key))) {
                    pDir = key;
                } else {
                    pDir = pDirOld;
                }
            } else {
                if (Character.toLowerCase(key) != '\'') {
                    initialized = true;
                    mainTimer.scheduleAtFixedRate(task, 500, 1000);
                }
            }
            if (dead) {
                if ((Character.toLowerCase(key) == KeyEvent.VK_ENTER)) {
                    JComponent comp = (JComponent) e.getSource();
                    Window win = SwingUtilities.getWindowAncestor(comp);
                    win.dispose();

                }

            }
        }

    }

    @Override
    public void actionPerformed(ActionEvent e) {
        repaint();
    }
}