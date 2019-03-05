package com.company;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Menu extends JPanel{
    public JPanel panel1;
    private JButton quitGameButton;
    private JButton mapNr1Button;
    private JButton mapNr2Button;

    public Menu() {
        mapNr1Button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                EventQueue.invokeLater(() -> {
                    Main main = new Main(1);
                    main.setVisible(true);
                });
            }
        });
        mapNr2Button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                EventQueue.invokeLater(() -> {
                    Main main = new Main(2);
                    main.setVisible(true);
                });
            }
        });
        quitGameButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });
    }}
