package com.company;

import javax.swing.*;

public class EditDialogProd extends DialogProd {

    @Override
    public void progress(products parent) {
        setVisible(false);
        int row = parent.cars.getSelectedRow();
        parent.cars.setValueAt(name.getText(), row, 0);
        parent.cars.setValueAt(modelCar.getText(), row, 1);
        parent.cars.setValueAt(vin.getText(), row, 2);
        parent.cars.setValueAt(price.getText(), row, 3);
    }

    @Override
    public void init(products parent) {
        int row = parent.cars.getSelectedRow();
        name = new JTextField(parent.cars.getValueAt(row, 0).toString(), 20);
        modelCar = new JTextField(parent.cars.getValueAt(row, 1).toString(), 20);
        vin = new JTextField(parent.cars.getValueAt(row, 2).toString(), 20);
        price = new JTextField(parent.cars.getValueAt(row, 3).toString(), 20);
        checker(0,name);
        checker(1,modelCar);
        checkerInt(2,vin);
        checkerInt(3,price);
    }

    public EditDialogProd(JFrame owner, products parent, String title){
        super(owner,parent, title);
    };
}
