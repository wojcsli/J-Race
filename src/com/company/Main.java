package com.company;

import javax.swing.*;
import java.awt.*;

public class Main extends JFrame {

    public Main() {
        initUI();
    }
    public Main(int mapNr){
        initBoard(mapNr);
    }

    private void initUI() {
        add(new Menu());
        setTitle("J-Race");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(300, 400);
        setLocationRelativeTo(null);
        setContentPane(new Menu().panel1);
    }

    private void initBoard(int mapNr) {
        add(new Board(mapNr));
        setTitle("J-Race");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(660, 830);
        setLocationRelativeTo(null);
    }

    public static void main(String[] args) {

        EventQueue.invokeLater(() -> {
            Main main = new Main();
            main.setVisible(true);
        });
    }
}