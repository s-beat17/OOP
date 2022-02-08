package com.company;

import javax.swing.*;

public class AddDialogProd extends DialogProd {

    public static boolean init(String s) {
        return s == null || "".equals(s);
    }

    @Override
    public void progress(products parent) {
        setVisible(false);
        String[] arr = {name.getText(),modelCar.getText(),vin.getText(),price.getText()};
        parent.addR(arr);
        JOptionPane.showMessageDialog(null, "Вы добавили автомобиль "+ arr [0] + " " + arr [1]);
    }

    @Override
    public void init(products parent) {
        name = new JTextField(20);
        modelCar= new JTextField(20);
        vin = new JTextField(20);
        price = new JTextField(20);
    }

    public AddDialogProd(JFrame owner, products parent, String title){
        super(owner,parent,title);
    }
}