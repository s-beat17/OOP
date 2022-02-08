package com.company;

import org.apache.log4j.Logger;
import javax.swing.*;

/**
 * Класс окна Добавления данных
 */
public class AddDialogEmploy extends DialogEmploy {
    /**
     * Логгер класса AddDialogBuyer
     */
    private static final Logger log = Logger.getLogger(AddDialogEmploy.class);
    @Override
    public void progress(LabTest parent) {
        log.debug("Старт метода progress");
        setVisible(false);
        String[] arr = {familia.getText(),name.getText(),rang.getText()};
        log.warn("Попытка добавить запись "+ " " + arr[0] + " " + arr[1] + " " + arr[2]);
        parent.addR(arr);
        log.info("Добавлена запись "+ " " + arr[0] + " " + arr[1] + " " + arr[2]);
        JOptionPane.showMessageDialog(null, "Вы добавили покупателя \""+arr[0]+" "+arr[1]+"\"");
    }

    @Override
    public void init(LabTest parent) {
        log.debug("Старт метода init");
        familia = new JTextField(20);
        name= new JTextField(20);
        rang = new JTextField(20);
    }

    public AddDialogEmploy(JFrame owner, LabTest parent, String title){
        super(owner,parent,title);
    }
}