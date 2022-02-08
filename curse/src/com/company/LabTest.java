package com.company;

import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRXmlDataSource;
import net.sf.jasperreports.engine.export.JRHtmlExporter;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import org.w3c.dom.*;
import org.xml.sax.SAXException;

import javax.swing.*;
import javax.swing.table.*;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.*;
import java.util.HashMap;
import java.util.logging.Logger;

//Класс приложения, визуализирующий экранную форму
public class LabTest {
    LabTest() {
        show();
    }
/*

    //Окно приложения
    private JFrame buyerList;
    //Модель таблицы
    private DefaultTableModel model;
    //Добавить
    private JButton addInf;
    //Удалить
    private JButton remove;
    //Изменить
    private JButton edit;
    //Сохранить изменения
    private JButton save;
    //Открыть файл
    private JButton folder;
    //Печать
    private JButton print;
    //Панель инструментов
    private JToolBar toolBar;
    //Таблица
    protected JTable buyers;
    //Выпадающий список
    private JComboBox brand;
    //Поле поискового запроса
    private JTextField carName;
    //Скролл
    private JScrollPane scroll;
    // Поиск
    private JButton filter = new JButton("Поиск");
    //Диалоговое окно редактирования данных
    private EditDialogEmploy dialog;
    //Диалоговое окно добавления данных
    private AddDialogEmploy dialogAdd;
    //Поле поискового запроса
    private JTextField textSearch;

    //Поток 1 отвечает за загрузку данных из XML-файла в экранную форму
    Thread t1 = new Thread();
    //Поток 2 отвечает за редактирование данных и сохранение XML-файла
    Thread t2 = new Thread();
    //Поток 3 отвечает за формирование отчета
    Thread t3 = new Thread();

    final static public Object shared = new Object();

    //Логгер класса LabTest
    private static final Logger log = Logger.getLogger(String.valueOf(LabTest.class));

    //Метод генерации отчетов в форматах PDF и HTML.
    public static void print(String datasource, String xpath, String template, String resultpath) {
        try {
            // Указание источника XML-данных
            JRDataSource jr = new JRXmlDataSource(datasource, xpath);
            // Создание отчета на базе шаблона
            JasperReport report = JasperCompileManager.compileReport(template);
            // Заполнение отчета данными
            JasperPrint print = JasperFillManager.fillReport(report, null, jr);
            JasperExportManager.exportReportToHtmlFile(print,resultpath);
            if (resultpath.toLowerCase().endsWith("pdf")) {
                JRExporter exporter;
                exporter = new JRPdfExporter();
                exporter.setParameter(JRExporterParameter.OUTPUT_FILE_NAME, resultpath);
                exporter.setParameter(JRExporterParameter.JASPER_PRINT, print);
                exporter.exportReport();
            }
            else
            JasperExportManager.exportReportToHtmlFile(print, resultpath);
        }
        catch (JRException e) {
                e.printStackTrace();
        }
    }

    //Метод чтения данных из файла
    public void read(String filename) {
        try {
            BufferedReader reader = new BufferedReader(new FileReader("buyerList"));
            for (int i = 0; i < model.getRowCount(); i++)
                model.removeRow(0); // Очистка таблицы
                String brand;
                do {
                    brand = reader.readLine();
                    if (brand != null) {
                        String[] temp2 = brand.split(";");
                        model.addRow(temp2); // Запись строки в таблицу
                    }
                } while (brand != null);
                reader.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } // файл не найден
            catch (IOException e) {
                e.printStackTrace();
            }
        }

    //Метод записи данных в файл
    public void write(String filename) {
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter("buyerList"));
            for (int i = 0; i < model.getRowCount(); i++) {// Для всех строк
                for (int j = 0; j < model.getColumnCount(); j++) {// Для всех столбцов
                    writer.write((String) model.getValueAt(i, j)); // Записать значение из ячейки
                    if (j != model.getColumnCount() - 1)
                        writer.write(";"); // Записать символ перевода каретки
                }
                if (i != model.getRowCount() - 1)
                writer.write("\r\n");
            }
            writer.close();
        } catch (IOException e) {// Ошибка записи в файл
            e.printStackTrace();
        }
    }

    //Метод проверки списка на отсутсвие записей
    private static class MyException extends Exception {
        MyException() {
            super("Вы не ввели модель машины для поиска");
        }
    }

    private void checkName(JTextField bName) throws MyException, NullPointerException {
        String sName = bName.getText();
        if (sName.contains("Модель")) throw new MyException();
        if (sName.length() == 0) throw new NullPointerException();
    }

    //Метод загрузки данных в XML файл
    public void makeXml() {
        try {
            // Создание парсера документа
            DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            // Создание пустого документа
            Document doc = builder.newDocument();
            // Создание корневого элемента buyerList и добавление его в документ
            Node buyerList = doc.createElement("buyerList");
            doc.appendChild(buyerList);
            // Создание дочерних элементов buyers и присвоение значений атрибутам
            for (int i = 0; i < model.getRowCount(); i++) {
                Element buyers = doc.createElement("buyers");
                buyerList.appendChild(buyers);
                buyers.setAttribute("brand", (String) model.getValueAt(i, 0));
                buyers.setAttribute("title", (String) model.getValueAt(i, 1));
                buyers.setAttribute("have", (String) model.getValueAt(i, 2));
            }
            try {
                // Создание преобразователя документа
                Transformer trans = TransformerFactory.newInstance().newTransformer();
                // Создание файла с именем buyers.xml для записи документа
                java.io.FileWriter fw = new FileWriter("buyers.xml");
                // Запись документа в файл
                trans.transform(new DOMSource(doc), new StreamResult(fw));
            }
            catch (TransformerConfigurationException e) {
                e.printStackTrace();
            }
            catch (TransformerException e) {
                e.printStackTrace();
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }
        catch (ParserConfigurationException e) {
            e.printStackTrace();
        }
    }

    //Метод выгрузки данных из XML файла
    public void loadXML() {
        try {
            // Создание парсера документа
            DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            DocumentBuilder dBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            Document doc = builder.newDocument();
            // Чтение документа из файла
            JFileChooser fileChooser = new JFileChooser("D:\\LabTest");
            int ret = fileChooser.showDialog(null, "Открыть файл");
            if (ret == JFileChooser.APPROVE_OPTION) {
                doc = dBuilder.parse(fileChooser.getSelectedFile());
                // Нормализация документа
                doc.getDocumentElement().normalize();
                // Получение списка элементов с именем buyers
                NodeList nlbuyers = doc.getElementsByTagName("buyers");
                // Цикл просмотра списка элементов и запись данных в таблицу
                for (int temp = 0; temp < nlbuyers.getLength(); temp++) {
                    // Выбор очередного элемента списка
                    Node elem = nlbuyers.item(temp);
                    // Получение списка атрибутов элемента
                    NamedNodeMap attrs = elem.getAttributes();
                    // Чтение атрибутов элемента
                    String brand = attrs.getNamedItem("brand").getNodeValue();
                    String title = attrs.getNamedItem("title").getNodeValue();
                    String have = attrs.getNamedItem("have").getNodeValue();
                    // Запись данных в таблицу
                    model.addRow(new String[]{brand, title, have});
                }
            }
        }
        catch (ParserConfigurationException e) {
            e.printStackTrace();
        }
        // Обработка ошибки парсера при чтении данных из XML-файла
        catch (SAXException e) {
            e.printStackTrace();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    //Метод отображения окна
    public void show() {
        // Создание окна
        // Объявления графических компонентов
        JFrame buyerList = new JFrame("Список машин");
        buyerList.setSize(500, 300);
        buyerList.setLocation(100, 100);
        buyerList.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        // Создание кнопок и прикрепление иконок
        JButton save = new JButton(new ImageIcon("./img/save.png"));
        JButton open = new JButton(new ImageIcon("./img/open.png"));
        JButton addInf = new JButton(new ImageIcon("./img/add.png"));
        JButton edit = new JButton(new ImageIcon("./img/edit.png"));
        JButton remove = new JButton(new ImageIcon("./img/remove.png"));
        JButton print = new JButton(new ImageIcon("./img/print.png"));


        // Настройка подсказок для кнопок
        save.setToolTipText("Сохранить");
        open.setToolTipText("Открыть");
        addInf.setToolTipText("Добавить информацию о машине");
        edit.setToolTipText("Редактировать информацию о машине");
        remove.setToolTipText("Удалить информацию о машине");
        print.setToolTipText("Печать списка машин");

        // Добавление кнопок на панель инструментов
        JToolBar toolBar = new JToolBar("Панель инструментов");
        toolBar.add(save);
        toolBar.add(open);
        toolBar.add(addInf);
        toolBar.add(edit);
        toolBar.add(remove);
        toolBar.add(print);

        // Размещение панели инструментов
        buyerList.setLayout(new BorderLayout());
        buyerList.add(toolBar, BorderLayout.NORTH);

        // Создание таблицы с данными
        String[] columns = {"Марка", "Модель", "В наличии"};
        String[][] data = {{"Mercedes", "E 450", "Есть"}, {"BMW", "X6", "Нет"}};
        DefaultTableModel model = new DefaultTableModel(data, columns);
        this.buyers = new JTable(model);
        buyers.setFont(new Font(Font.SERIF, Font.BOLD, 14));
        buyers.setIntercellSpacing(new Dimension(0, 1));
        buyers.setRowHeight(buyers.getRowHeight() + 10);
        buyers.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        buyers.setDefaultRenderer(buyers.getColumnClass(1), new DefaultTableCellRenderer() {
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                super.setHorizontalAlignment(SwingConstants.CENTER);
                super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                return this;
            }
        });

        scroll = new JScrollPane(this.buyers);

        // Размещение таблицы с данными
        buyerList.add(scroll, BorderLayout.CENTER);
        // Подготовка компонентов поиска
        JComboBox<String> brand = new JComboBox<>(new String[]{"Марка", "Мерседес", "БМВ"});
        JTextField carName = new JTextField("Модель");
        // Добавление компонентов на панель
        JPanel filterPanel = new JPanel();
        filterPanel.add(brand);
        filterPanel.add(carName);
        filterPanel.add(filter);
        // Размещение панели поиска внизу окна
        buyerList.add(filterPanel, BorderLayout.SOUTH);
        // Визуализация экранной формы
        buyerList.setVisible(true);

        // Слушатели

        //Добавить элемент
        addInf.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String[] buyers = {"", "", ""};
                model.addRow(buyers);
            }
        });

        //Удаление элемента
        remove.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                model.removeRow(buyers.getSelectedRow());
            }
        });

        //Сохранение в файл txt
        save.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                FileDialog saveFile = new FileDialog(buyerList, "Сохранить...", FileDialog.SAVE);
                saveFile.setFile("*.txt");
                saveFile.setVisible(true); // Отобразить запрос пользователю
                // Определить имя выбранного каталога и файла
                String fileName = saveFile.getDirectory() + saveFile.getFile();
                if(fileName == null) return; // Если пользователь нажал «отмена»
                try {
                    BufferedWriter writer = new BufferedWriter(new FileWriter(fileName));
                    for (int i = 0; i < model.getRowCount(); i++) {// Для всех строк
                        for (int j = 0; j < model.getColumnCount(); j++) { // Для всех столбцов
                            writer.write((String) model.getValueAt(i, j)); // Записать значение из ячейки
                            writer.write("\n"); // Записать символ перевода каретки
                        }
                    }
                    writer.close();
                }
                catch(IOException e) { // Ошибка записи в файл
                    e.printStackTrace();
                }
                try {
                    // Создание парсера документа
                    DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
                    // Создание пустого документа
                    Document doc = builder.newDocument();
                    // Создание корневого элемента buyerList и добавление его в документ
                    Node buyerList = doc.createElement("buyerList");
                    doc.appendChild(buyerList);
                    // Создание дочерних элементов buyers и присвоение значений атрибутам
                    for (int i = 0; i < model.getRowCount(); i++) {
                        Element buyers = doc.createElement("buyers");
                        buyerList.appendChild(buyers);
                        buyers.setAttribute("brand", (String) model.getValueAt(i, 0));
                        buyers.setAttribute("title", (String) model.getValueAt(i, 1));
                        buyers.setAttribute("have", (String) model.getValueAt(i, 2));
                    }
                    try {
                        // Создание преобразователя документа
                        Transformer trans = TransformerFactory.newInstance().newTransformer();
                        // Создание файла с именем buyers.xml для записи документа
                        FileWriter fw = new FileWriter("buyers.xml");
                        // Запись документа в файл
                        trans.transform(new DOMSource(doc), new StreamResult(fw));
                    }
                    catch (TransformerConfigurationException e) {
                        e.printStackTrace();
                    }
                    catch (TransformerException e) {
                        e.printStackTrace();
                    }
                    catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                catch (ParserConfigurationException e) {
                    e.printStackTrace();
                }
            }
        });

        //Загрузка из txt файла
        open.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                FileDialog openFile = new FileDialog(buyerList, "Открыть...", FileDialog.LOAD);
                openFile.setFile("*.txt");
                openFile.setVisible(true); // Отобразить запрос пользователю
                // Определить имя выбранного каталога и файла
                String fileName = openFile.getDirectory() + openFile.getFile();
                if (fileName == null) return; // Если пользователь нажал «отмена»
                try {
                    BufferedReader reader = new BufferedReader(new FileReader(fileName));
                    int rows = model.getRowCount();
                    for (int i = 0; i < rows; i++) model.removeRow(0); // Очистка таблицы
                    String brand;
                    do {
                        brand = reader.readLine();
                        if(brand != null)
                        { String title = reader.readLine();
                            String have = reader.readLine();
                            model.addRow(new String[]{brand, title, have}); // Запись строки в таблицу
                        }
                    } while(brand != null);
                    reader.close();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } // файл не найден
                catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        filter.addActionListener((e) -> {
            try { checkName(carName);
            }
            catch(NullPointerException ex) {
                JOptionPane.showMessageDialog(buyerList, ex.toString());
            }
            catch(MyException myEx) {
                JOptionPane.showMessageDialog(null, myEx.getMessage());
            }
        });

        edit.addActionListener((e)-> {
            t2 = new Thread(() -> {
                if (t1.isAlive()) {
                    try {
                        log.info("Ожидание завершения 1 потока");
                        JOptionPane.showMessageDialog(buyerList, "Ждем, пока отработает 1 поток");
                        t1.join();
                        JOptionPane.showMessageDialog(buyerList, "1 поток отработал, пробуем запустить 2 поток");
                    } catch (InterruptedException ex) {
                        ex.printStackTrace();
                    }
                }
                if (model.getRowCount() != 0) {
                    if (buyers.getSelectedRow() != -1) {
                        JOptionPane.showMessageDialog(null,"2 поток запущен");
                        dialog = new EditDialogEmploy(buyerList, LabTest.this, "Редактирование");
                        dialog.setVisible(true);
                        try {
                            Thread.sleep(5000);

                        } catch (InterruptedException ex) {
                            ex.printStackTrace();
                        }
                    } else {
                        JOptionPane.showMessageDialog(null, "Не выбрана строка. Нечего редактировать");
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "В данном окне нет записей. Нечего редактировать");
                }
            });
            t2.start();

        });
        edit.setMnemonic(KeyEvent.VK_E);

        save.addActionListener((e) -> {
            t3 = new Thread(() -> {
                if (t2.isAlive()) {
                    try {
                        JOptionPane.showMessageDialog(buyerList, "Ждем, пока отработает 2 поток");
                        t2.join();
                        JOptionPane.showMessageDialog(buyerList, "2 поток отработал");
                    } catch (InterruptedException ex) {
                        ex.printStackTrace();
                    }
                }
                if (model.getRowCount() != 0) {
                    JOptionPane.showMessageDialog(null, "3 поток создает отчет");
                    try {
                        Thread.sleep(5000);
                    } catch (InterruptedException ex) {
                        ex.printStackTrace();
                    }
                    print("buyers.xml", "window/buyers", "Cherry.jrxml", "otchet.pdf");
                }
            });
            t3.start();
        });

        folder.addActionListener((e) -> {
            t1 = new Thread(() -> {
                JOptionPane.showMessageDialog(buyerList, "1 поток начал работу");
                loadXML();
                buyers.setRowSelectionInterval(0,0);
                try {
                    Thread.sleep(6000);
                } catch (InterruptedException ex) {
                    ex.printStackTrace();
                }
                SwingUtilities.invokeLater(() -> {
                    JOptionPane.showMessageDialog(buyerList, "1 поток закончил работу.");
                });
            });
            t1.start();
        });

        filter.addActionListener((e) -> {
            if (model.getRowCount() != 0) {
                */
/*if (!textSearch.getText().isEmpty())
                    log.debug("Запуск нового поиска по ключевому слову: " + textSearch.getText());
                else
                    log.debug("Сброс ключевого слова поиска");*//*

                TableRowSorter<TableModel> sorter = new TableRowSorter<TableModel>(((DefaultTableModel) model));
                sorter.setStringConverter(new TableStringConverter() {
                    @Override
                    public String toString(TableModel model, int row, int column) {
                        return model.getValueAt(row, column).toString().toLowerCase();
                    }
                });
                sorter.setRowFilter(RowFilter.regexFilter("(?i)" + textSearch.getText().toLowerCase()));
                buyers.setRowSorter(sorter);
            }
        });

        print.addActionListener((e)-> {
            class myThread extends Thread{
                private int type;
                public myThread(int i) {
                    type=i;
                }

                public void run() {

                    if (type==1) {
                        synchronized (shared) {
                            try {
                                shared.wait();
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }

                            try {

                                loadXML();
                                buyers.setRowSelectionInterval(0,0);

                            } catch (Exception e1) {
                                e1.printStackTrace();
                            }
                        }
                    }

                    if (type==2) {
                        synchronized (shared) {
                            shared.notifyAll();
                            try {
                                shared.wait();
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }

                            dialog = new EditDialogEmploy(buyerList, LabTest.this, "Редактирование");
                            dialog.setVisible(true);

                            shared.notifyAll();
                        }
                    }

                    if (type==3) {
                        synchronized (shared) {
                            shared.notifyAll();
                            try {
                                shared.wait();
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }

                            print("D:\\LabTest\\buyers.xml","buyerList/buyers","D:\\LabTest\\Simple1.jrxml","D:\\LabTest\\otchet.doc");

                        }
                    }
                }
            }
            new myThread(1).start();
            new myThread(2).start();
            new myThread(3).start();
        });

    }

    public void addR(String[] arr){
        model.addRow(arr);
    }

    */
/*public static void main(String[] args) {
        new LabTest().show();
        print("D:\\LabTest\\buyers.xml","buyerList/buyers","D:\\LabTest\\Simple1.jrxml","D:\\LabTest\\otchet.doc");
    }*//*

}*/

    /**
     * Окно приложения
     */
    private JFrame buyerList;
    /**
     * Модель таблицы
     */
    private DefaultTableModel model;
    /**
     * Добавить
     */
    private JButton add;
    /**
     * Удалить
     */
    private JButton delete;
    /**
     * Изменить
     */
    private JButton edit;
    /**
     * Сохранить изменения
     */
    private JButton save;
    /**
     * Открыть файл
     */
    private JButton folder;
    /**
     * Печать
     */
    private JButton print;
    /**
     * Панель инструментов
     */
    private JToolBar toolBar;
    /**
     * Таблица
     */
    protected JTable buyers;
    /**
     * Выпадающий список
     */
    private JComboBox comboBox;
    /**
     * Поле поискового запроса
     */
    private JTextField textSearch;
    /**
     * Поиск
     */
    private JButton search;
    /**
     * Диалоговое окно редактирования данных
     */
    private EditDialogEmploy dialog;
    /**
     * Диалоговое окно добавления данных
     */
    private AddDialogEmploy dialogAdd;

    /**
     * Поток 1 отвечает за загрузку данных из XML-файла в экранную форму
     */
    Thread t1 = new Thread();
    /**
     * Поток 2 отвечает за редактирование данных и сохранение XML-файла
     */
    Thread t2 = new Thread();
    /**
     * Поток 3 отвечает за формирование отчета
     */
    Thread t3 = new Thread();

    final static public Object shared = new Object();

    /**
     * Скролл
     */
    private JScrollPane scroll;

    /**
     * Логгер класса employs
     */
    private static final Logger log = Logger.getLogger(String.valueOf(LabTest.class));


    /**
     * Метод генерации отчетов в форматах DOCX и HTML.
     * @param datasource Имя файла XML с данными
     * @param xpath      Директория до полей с данными. Ex.: "BookList/Books" - Fields
     * @param template   Имя файла шаблона .jrxml
     * @param resultpath Имя файла, в который будет помещен отчет
     */

    public static void print(String datasource, String xpath, String template, String resultpath) {
        try {
            // Указание источника XML-данных
            JRDataSource ds = new JRXmlDataSource(datasource, xpath);
// Создание отчета на базе шаблона
            JasperReport jasperReport = JasperCompileManager.compileReport(template);
// Заполнение отчета данными
            JasperPrint print = JasperFillManager.fillReport(jasperReport, new HashMap(), ds);
            JRExporter exporter = null;
            if(resultpath.toLowerCase().endsWith("pdf"))
                exporter = new JRPdfExporter(); // Генерация отчета в формате PDF
            else
                exporter = new JRHtmlExporter(); // Генерация отчета в формате HTML
// Задание имени файла для выгрузки отчета
            exporter.setParameter(JRExporterParameter.OUTPUT_FILE_NAME, resultpath);
// Подключение данных к отчету
            exporter.setParameter(JRExporterParameter.JASPER_PRINT, print);
// Выгрузка отчета в заданном формате
            exporter.exportReport();
            JasperExportManager.exportReportToHtmlFile(print, resultpath);
            JOptionPane.showMessageDialog(null, "3 поток закончил работу. Отчет создан");
        } catch (JRException e) {
            e.printStackTrace();
        }
    }

    /**
     * Метод чтения данных из файла
     *
     * @param filename Имя файла
     */
    public void read(String filename) {
        try {
            BufferedReader reader = new BufferedReader(new FileReader(filename));
            for (int i = 0; i < model.getRowCount(); i++)
                model.removeRow(0);
            String temp;
            do {
                temp = reader.readLine();
                if (temp != null) {
                    String[] temp2 = temp.split(";");
                    model.addRow(temp2);
                }
            } while (temp != null);
            reader.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Метод записи данных в файл
     *
     * @param filename Имя файла
     */
    public void write(String filename) {
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(filename));
            for (int i = 0; i < model.getRowCount(); i++) {
                for (int j = 0; j < model.getColumnCount(); j++) {
                    writer.write((String) model.getValueAt(i, j));
                    if (j != model.getColumnCount() - 1)
                        writer.write(";");
                }
                if (i != model.getRowCount() - 1)
                    writer.write("\r\n");
            }
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Метод загрузки данных в XML файл
     */
    public void makeXml() {
        try {
            // Создание парсера документа
            DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            // Создание пустого документа
            Document doc = builder.newDocument();
            // Создание корневого элемента buyerList и добавление его в документ
            Node buyerList = doc.createElement("buyerList");
            doc.appendChild(buyerList);
            // Создание дочерних элементов buyers и присвоение значений атрибутам
            for (int i = 0; i < model.getRowCount(); i++) {
                Element buyers = doc.createElement("buyers");
                buyerList.appendChild(buyers);
                buyers.setAttribute("familia", (String) model.getValueAt(i, 0));
                buyers.setAttribute("name", (String) model.getValueAt(i, 1));
                buyers.setAttribute("vin", (String) model.getValueAt(i, 2));
            }
            try {
                // Создание преобразователя документа
                Transformer trans = TransformerFactory.newInstance().newTransformer();
                // Создание файла с именем buyers.xml для записи документа
                java.io.FileWriter fw = new FileWriter("buyers.xml");
                // Запись документа в файл
                trans.transform(new DOMSource(doc), new StreamResult(fw));
            } catch (TransformerConfigurationException e) {
                e.printStackTrace();
            } catch (TransformerException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }


        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        }
    }

    /**
     * Метод выгрузки данных из XML файла
     */
    public void loadXML() {
        try {
            // Создание парсера документа
            DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            DocumentBuilder dBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            Document doc = builder.newDocument();
            // Чтение документа из файла
            JFileChooser fileChooser = new JFileChooser("D:\\LabTest");
            int ret = fileChooser.showDialog(null, "Открыть файл");
            if (ret == JFileChooser.APPROVE_OPTION) {
                doc = dBuilder.parse(fileChooser.getSelectedFile());
                // Нормализация документа
                doc.getDocumentElement().normalize();
                // Получение списка элементов с именем buyers
                NodeList nlbuyers = doc.getElementsByTagName("buyers");
                // Цикл просмотра списка элемента и запись данных в таблицу
                for (int temp = 0; temp < nlbuyers.getLength(); temp++) {
                    // Выбор очередного элемента списка
                    Node elem = nlbuyers.item(temp);
                    // Получение списка атрибутов документа
                    NamedNodeMap attrs = elem.getAttributes();
                    // Чтение атрибутов элемента
                    String familia = attrs.getNamedItem("familia").getNodeValue();
                    String name = attrs.getNamedItem("name").getNodeValue();
                    String vin = attrs.getNamedItem("vin").getNodeValue();
                    // Запись данных в таблицу
                    model.addRow(new String[]{familia, name, vin});
                }
            }
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        }
        // Обработка ошибки парсера при чтении данных из XML-файла
        catch (SAXException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Метод отображения окна
     */
    public void show() {
        /**
         * Запись в лог warn о старте метода show() buyers
         */

        // Создание окна
        buyerList = new JFrame("Список покупателей");
        buyerList.setSize(800, 500);
        buyerList.setLocation(310, 130);
        buyerList.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        // Создание кнопок и прикрепление иконок
        add = new JButton("Добавить", new ImageIcon("./img/add.png"));
        delete = new JButton("Удалить", new ImageIcon("./img/delete.png"));
        edit = new JButton("Редактировать", new ImageIcon("./img/edit.png"));
        save = new JButton("Сохранить", new ImageIcon("./img/save.png"));
        folder = new JButton("Загрузить", new ImageIcon("./img/folder.png"));
        print = new JButton("Печать", new ImageIcon("./img/print.png"));

        // Настройка подсказок
        add.setToolTipText("Добавить информацию о машинах");
        delete.setToolTipText("Удалить информацию о машинах");
        edit.setToolTipText("Изменить информацию о машинах");
        save.setToolTipText("Сохранить информацию о машинах");
        folder.setToolTipText("Загрузить информацию о машинах");
        print.setToolTipText("Распечатать информацию о машинах");

        // Добавление кнопок на панель инструментов
        toolBar = new JToolBar("Панель инструментов");
        toolBar.add(add);
        toolBar.add(delete);
        toolBar.add(edit);
        toolBar.add(save);
        toolBar.add(folder);
        toolBar.add(print);

        // Размещение панели инструментов
        buyerList.setLayout(new BorderLayout());
        buyerList.add(toolBar, BorderLayout.NORTH);

        // Создание таблицы с данными
        String[] columns = {"Фамилия", "Имя", "VIN"};

        // Настройка таблицы
        model = new DefaultTableModel(columns, 0) {
            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return false;
            }
        };
        this.buyers = new JTable(model);
        buyers.setFont(new Font(Font.SERIF, Font.BOLD, 14));
        buyers.setIntercellSpacing(new Dimension(0, 1));
        buyers.setRowHeight(buyers.getRowHeight() + 10);
        buyers.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);

        buyers.setDefaultRenderer(buyers.getColumnClass(1), new DefaultTableCellRenderer() {
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                super.setHorizontalAlignment(SwingConstants.CENTER);
                super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                return this;
            }

        });

        scroll = new JScrollPane(this.buyers);

        // Размещение таблицы с данными
        buyerList.add(scroll, BorderLayout.CENTER);

        // Подготовка компонентов поиска
        comboBox = new JComboBox(new String[]{"Фамилия", "Имя", "VIN"});
        textSearch = new JTextField();
        textSearch.setColumns(20);
        search = new JButton("Поиск");
        buyerList.getRootPane().setDefaultButton(search);
// remove the binding for pressed
        buyerList.getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW)
                .put(KeyStroke.getKeyStroke("ENTER"), "none");
// retarget the binding for released
        buyerList.getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW)
                .put(KeyStroke.getKeyStroke("released ENTER"), "press");
        // Добавление компонентов на панель
        JPanel searchPanel = new JPanel();
        searchPanel.add(comboBox);
        searchPanel.add(textSearch);
        searchPanel.add(search);

        // Размещение панели поиска внизу окна
        buyerList.add(searchPanel, BorderLayout.SOUTH);

        // Слушатели


        add.addActionListener((e) -> {
            dialogAdd = new AddDialogEmploy(buyerList, LabTest.this, "Добавление записи");
            dialogAdd.setVisible(true);
        });

        add.setMnemonic(KeyEvent.VK_A);
        delete.addActionListener((e) -> {
            if (buyers.getRowCount() > 0) {
                if (buyers.getSelectedRow() != -1) {
                    try {
                        model.removeRow(buyers.convertRowIndexToModel(buyers.getSelectedRow()));
                        JOptionPane.showMessageDialog(buyerList, "Вы удалили строку");
                    } catch (Exception ex) {
                        JOptionPane.showMessageDialog(null, "Ошибка");
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "Вы не выбрали строку для удаления");
                }
            } else {
                JOptionPane.showMessageDialog(null, "В данном окне нет записей. Нечего удалять");
            }
        });

        delete.setMnemonic(KeyEvent.VK_D);

        buyerList.addWindowListener(new WindowListener() {
            @Override
            public void windowOpened(WindowEvent e) {

            }

            @Override
            public void windowClosing(WindowEvent e) {
                JOptionPane.showMessageDialog(buyerList, "Вы закрываете окно)");
            }

            @Override
            public void windowClosed(WindowEvent e) {

            }

            @Override
            public void windowIconified(WindowEvent e) {

            }

            @Override
            public void windowDeiconified(WindowEvent e) {

            }

            @Override
            public void windowActivated(WindowEvent e) {

            }

            @Override
            public void windowDeactivated(WindowEvent e) {

            }
        });

        edit.addActionListener((e) -> {
//            t2 = new Thread(() -> {
                if (t1.isAlive()) {
                    try {
                        log.info("Ожидание завершения 1 потока");
                        JOptionPane.showMessageDialog(buyerList, "Ждем, пока отработает 1 поток");
                        t1.join();
                        JOptionPane.showMessageDialog(buyerList, "1 поток отработал, пробуем запустить 2 поток");
                    } catch (InterruptedException ex) {

                        ex.printStackTrace();
                    }
                }
                if (model.getRowCount() != 0) {
                    if (buyers.getSelectedRow() != -1) {
                        t2 = new Thread(() -> {
                            JOptionPane.showMessageDialog(null, "2 поток запущен");
                            dialog = new EditDialogEmploy(buyerList, LabTest.this, "Редактирование");
                            dialog.setVisible(true);
                            try {
                                Thread.sleep(5000);

                            } catch (InterruptedException ex) {
                                ex.printStackTrace();
                            }
                        });
                        t2.start();
                    } else {

                        JOptionPane.showMessageDialog(null, "Не выбрана строка. Нечего редактировать");
                    }
                } else {

                    JOptionPane.showMessageDialog(null, "В данном окне нет записей. Нечего редактировать");
                }
//            });
//            t2.start();
        });
        edit.setMnemonic(KeyEvent.VK_E);

        save.addActionListener((e) -> {
//            t3 = new Thread(() -> {
                if (t2.isAlive()) {
                    try {
                        JOptionPane.showMessageDialog(buyerList, "Ждем, пока отработает 2 поток");
                        t2.join();
                        JOptionPane.showMessageDialog(buyerList, "2 поток отработал");
                    } catch (InterruptedException ex) {
                        ex.printStackTrace();
                    }
                }
                if (model.getRowCount() != 0) {
                    JOptionPane.showMessageDialog(null, "3 поток создает отчет");
                    t3 = new Thread(() -> {
                        makeXml();
                    });
                    t3.start();
                }
//            });
//            t3.start();
        });
        folder.addActionListener((e) -> {
            t1 = new Thread(() -> {
                JOptionPane.showMessageDialog(buyerList, "1 поток начал работу");
                loadXML();
                buyers.setRowSelectionInterval(0, 0);
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException ex) {
                    ex.printStackTrace();
                }
                SwingUtilities.invokeLater(() -> {
                    JOptionPane.showMessageDialog(buyerList, "1 поток закончил работу.");
                });
            });
            t1.start();
        });

        search.addActionListener((e) -> {
            if (model.getRowCount() != 0) {
                TableRowSorter<TableModel> sorter = new TableRowSorter<TableModel>(((DefaultTableModel) model));
                sorter.setStringConverter(new TableStringConverter() {
                    @Override
                    public String toString(TableModel model, int row, int column) {
                        return model.getValueAt(row, column).toString().toLowerCase();
                    }
                });
                sorter.setRowFilter(RowFilter.regexFilter("(?i)" + textSearch.getText().toLowerCase()));
                buyers.setRowSorter(sorter);
            }
        });

        print.addActionListener((e) -> {
            class myThread extends Thread {
                private int type;

                public myThread(int i) {
                    type = i;
                }

                public void run() {

                   /* if (type == 1) {
                        synchronized (shared) {
                            try {
                                shared.wait();
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }

                            try {

                                loadXML();
                                buyers.setRowSelectionInterval(0, 0);

                            } catch (Exception e1) {
                                e1.printStackTrace();
                            }
                        }
                    }

                    if (type == 2) {
                        synchronized (shared) {
                            shared.notifyAll();
                            try {
                                shared.wait();
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }

                            dialog = new EditDialogEmploy(buyerList, LabTest.this, "Редактирование");
                            dialog.setVisible(true);

                            shared.notifyAll();
                        }
                    }*/

                    if (type == 3) {
                        synchronized (shared) {
                            shared.notifyAll();
                            try {
                                shared.wait();
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }

                            print("D:\\LabTest\\buyers.xml", "buyerList/buyers", "D:\\LabTest\\buyers.jrxml", "D:\\LabTest\\buyers.pdf");

                        }
                    }
                }
            }
            new myThread(1).start();
            new myThread(2).start();
            new myThread(3).start();
        });


        // Если не выделена строка, то прячем кнопки
        buyers.getSelectionModel().addListSelectionListener((e) -> {
            Boolean check = true;
            if (buyers.getSelectionModel().isSelectionEmpty()) {
                check = false;
            }
            edit.setVisible(check);
            delete.setVisible(check);
        });

        // Визуализация экранной формы
        buyerList.setVisible(true);
    }

    // Вспомогательная функция для добавления данных из формы
    // @param arr Массив данных: фамилия, имя, VIN
    public void addR(String[] arr) {
        model.addRow(arr);
    }

}