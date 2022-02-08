package com.company;

import org.apache.log4j.Logger;

import javax.swing.*;

public class EditDialogEmploy extends DialogEmploy {
    //Логгер класса EditDialogEmploy
    private static final Logger log = Logger.getLogger(EditDialogEmploy.class);

    @Override
    public void progress(LabTest parent) {
        log.debug("Старт метода progress");
        setVisible(false);
        int row = parent.buyers.getSelectedRow();
        log.warn("Попытка изменить запись "+ parent.buyers.getValueAt(row,0) +" "+parent.buyers.getValueAt(row,1) + " " + parent.buyers.getValueAt(row,2));
        parent.buyers.setValueAt(familia.getText(), row, 0);
        parent.buyers.setValueAt(name.getText(), row, 1);
        parent.buyers.setValueAt(rang.getText(), row, 2);
        log.info("Заменена на "+ parent.buyers.getValueAt(row,0) +" "+parent.buyers.getValueAt(row,1) + " " + parent.buyers.getValueAt(row,2));
        parent.makeXml();
        SwingUtilities.invokeLater(() -> JOptionPane.showMessageDialog(null, "2 поток закончил работу, данные сохранены"));
    }

    @Override
    public void init(LabTest parent) {
        log.debug("Старт метода Init");
        int row = parent.buyers.getSelectedRow();
        familia = new JTextField(parent.buyers.getValueAt(row, 0).toString(), 20);
        name = new JTextField(parent.buyers.getValueAt(row, 1).toString(), 20);
        rang = new JTextField(parent.buyers.getValueAt(row, 2).toString(), 20);
        checker(0,familia);
        checker(1,name);
        checker(2,rang);
    }

    public EditDialogEmploy(JFrame owner, LabTest parent, String title){
        super(owner,parent, title);
    };
}