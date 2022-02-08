package com.company;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public abstract class DialogProd extends JDialog {
    protected static JTextField name;
    protected JTextField modelCar;
    protected JTextField vin;
    protected JTextField price;
    protected Boolean[] check = {false,false,false,false};
    private JButton ok = new JButton("Принять");
    private JButton cancel = new JButton("Закрыть");
    private JLabel nameLab = new JLabel("Название");
    private JLabel modelCarLab = new JLabel("Марка");
    private JLabel vinLab = new JLabel("VIN");
    private JLabel priceLab = new JLabel("Цена");


    public abstract void progress(products parent);


    public abstract void init(products parent);

    public DialogProd(JFrame owner, products parent, String title) {
        super(owner, title, true);
        ok.setEnabled(false);
        // Инит кнопок
        init(parent);

        // Создание валидатора полей
        name.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                checker(0, name);
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                checker(0, name);
            }

            @Override
            public void changedUpdate(DocumentEvent e) {

            }
        });

        modelCar.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                checker(1,modelCar);
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                checker(1,modelCar);
            }

            @Override
            public void changedUpdate(DocumentEvent e) {

            }
        });

        vin.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                checkerInt(2,vin);
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                checkerInt(2,vin);
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
            }
        });

        price.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                checkerInt(3,price);
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                checkerInt(3,price);
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
            }
        });

        ok.addActionListener((e) -> progress(parent));
        cancel.addActionListener((e) -> dispose());
        JPanel mainp = new JPanel();

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(4, 2, 2, 2));

        panel.setSize(300, 150);

        // adds to the GridLayout
        panel.add(nameLab);
        panel.add(name);
        panel.add(modelCarLab);
        panel.add(modelCar);
        panel.add(vinLab);
        panel.add(vin);
        panel.add(priceLab);
        panel.add(price);
        mainp.add(panel);
        add(BorderLayout.CENTER, mainp);
        JPanel but = new JPanel();
        but.add(ok);
        but.add(cancel);
        add(BorderLayout.SOUTH, but);
        setLocation(500, 250);
        setSize(500, 165);
        this.getRootPane().setDefaultButton(ok);
// remove the binding for pressed
        this.getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW)
                .put(KeyStroke.getKeyStroke("ENTER"), "none");
// retarget the binding for released
        this.getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW)
                .put(KeyStroke.getKeyStroke("released ENTER"), "press");
    }

    protected void checker(int i, JTextField field){
        Pattern b = Pattern.compile("^[a-zA-Z0-9]{1,20}$");
        Matcher rr = b.matcher(field.getText());
        Color color = Color.GREEN;
        if (rr.matches()) {
            check[i] = true;
        } else {
            color = Color.RED;
            check[i] = false;
        }
        field.setBorder(BorderFactory.createLineBorder(color));

        if (check[0] && check[1] && check[2] && check[3])
            ok.setEnabled(true);
        else
            ok.setEnabled(false);
    }

    protected void checkerInt(int i, JTextField field){
        Pattern c = Pattern.compile("^[a-zA-Z0-9]{1,20}$");
        Matcher rm = c.matcher(field.getText());
        Color color = Color.GREEN;
        if (rm.matches()) {
            check[i] = true;
        } else {
            color = Color.RED;
            check[i] = false;
        }
        field.setBorder(BorderFactory.createLineBorder(color));

        if (check[0] && check[1] && check[2] && check[3])
            ok.setEnabled(true);
        else
            ok.setEnabled(false);
    }
}