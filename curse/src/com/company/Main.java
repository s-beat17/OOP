package com.company;

import javax.swing.*;
import java.awt.*;

public class Main {

    public static void main(String[] args) {

        JFrame window = new JFrame("Главное окно");
        JButton prodWindow = new JButton("Список машин", new ImageIcon("./img/prodList.png"));
        JButton buyersWindow = new JButton("Список покупателей", new ImageIcon("./img/buyers.png"));

        prodWindow.addActionListener((e) -> new products());
        buyersWindow.addActionListener((e) -> new LabTest());

        JPanel mainp = new JPanel();
        JPanel panel = new JPanel();

        panel.setLayout(new GridLayout(4, 1));
        panel.setSize(350, 100);

        // adds to the GridLayout
        panel.add(prodWindow);
        panel.add(buyersWindow);
       /* panel.add(reportWindow);
        panel.add(refWindow);*/
        mainp.add(panel);
        window.add(BorderLayout.CENTER, mainp);
        window.setSize(400,380);
        window.setLocation(500,200);
        window.setVisible(true);
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
}