package com.company;

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
import java.io.FileWriter;
import java.io.IOException;

public class products {

    protected void makeXml() {
        try {
            // Создание парсера документа
            DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            // Создание пустого документа
            Document doc = builder.newDocument();
            // Создание корневого элемента carList и добавление его в документ
            Node carList = doc.createElement("carList");
            doc.appendChild(carList);
            // Создание дочерних элементов cars и присвоение значений атрибутам
            for (int i = 0; i < model.getRowCount(); i++) {
                Element cars = doc.createElement("cars");
                carList.appendChild(cars);
                cars.setAttribute("name", (String) model.getValueAt(i, 0));
                cars.setAttribute("modelCar", (String) model.getValueAt(i, 1));
                cars.setAttribute("vin", (String) model.getValueAt(i, 2));
                cars.setAttribute("price", (String) model.getValueAt(i, 3));
            }
            try {
                // Создание преобразователя документа
                Transformer trans = TransformerFactory.newInstance().newTransformer();
                // Создание файла с именем cars.xml для записи документа
                java.io.FileWriter fw = new FileWriter("cars.xml");
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

    protected void loadXML(){
        try{
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
                // Получение списка элементов с именем cars
                NodeList nlcars = doc.getElementsByTagName("cars");
                // Цикл просмотра списка элемента и запись данных в таблицу
                for (int temp = 0; temp < nlcars.getLength(); temp++) {
                    // Выбор очередного элемента списка
                    Node elem = nlcars.item(temp);
                    // Получение списка атрибутов документа
                    NamedNodeMap attrs = elem.getAttributes();
                    // Чтение атрибутов элемента
                    String name = attrs.getNamedItem("name").getNodeValue();
                    String modelCar = attrs.getNamedItem("modelCar").getNodeValue();
                    String vin = attrs.getNamedItem("vin").getNodeValue();
                    String price = attrs.getNamedItem("price").getNodeValue();
                    // Запись данных в таблицу
                    model.addRow(new String[]{name, modelCar, vin, price});
                }
            }
            else
                JOptionPane.showMessageDialog(null,"Вы не выбрали файл");
        }
        catch (ParserConfigurationException e){e.printStackTrace();}
        // Обработка ошибки парсера при чтении данных из XML-файла
        catch (SAXException e){e.printStackTrace();}
        catch (IOException e){e.printStackTrace();}
    }

    products(){
        show();
    }

    /**
     * Окно приложения
     */
    private JFrame carList;
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
    protected JTable cars;
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
     * Скролл
     */
    private JScrollPane scroll;

    private AddDialogProd addDialogProd;
    private EditDialogProd editDialogProd;

    Thread t1 = new Thread();
    Thread t2 = new Thread();
    Thread t3 = new Thread();

    public void show(){
        carList = new JFrame("Список машин");
        carList.setSize(1000,500);
        carList.setLocation(310,130);
        carList.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        // Создание кнопок и прикрепление иконок
        add = new JButton("Добавить", new ImageIcon("./img/add.png"));
        delete = new JButton("Удалить", new ImageIcon("./img/delete.png"));
        edit = new JButton("Редактировать", new ImageIcon("./img/edit.png"));
        save = new JButton("Сохранить", new ImageIcon("./img/save.png"));
        folder = new JButton("Загрузить", new ImageIcon("./img/folder.png"));
        print = new JButton("Печать",new ImageIcon("./img/print.png"));

        // Настройка подсказок
        add.setToolTipText("Добавить информацию о машине");
        delete.setToolTipText("Удалить информацию о машине");
        edit.setToolTipText("Изменить информацию о машине");
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
        carList.setLayout(new BorderLayout());
        carList.add(toolBar,BorderLayout.NORTH);
        // Создание таблицы с данными
        String[] columns = {"Марка", "Модель", "VIN", "Цена"};

        // Настройка таблицы
        model = new DefaultTableModel(columns,0){
            public boolean isCellEditable(int rowIndex, int columnIndex)
            {
                return false;
            }
        };
        this.cars = new JTable(model);
        cars.setFont(new Font(Font.SERIF,Font.BOLD,14));
        cars.setIntercellSpacing(new Dimension(0,1));
        cars.setRowHeight(cars.getRowHeight()+10);
        cars.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);

        cars.setDefaultRenderer(cars.getColumnClass(1), new DefaultTableCellRenderer(){
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                super.setHorizontalAlignment(SwingConstants.CENTER);
                super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                return this;
            }

        });

        scroll = new JScrollPane(this.cars);

        // Размещение таблицы с данными
        carList.add(scroll,BorderLayout.CENTER);
        // Подготовка компонентов поиска
        comboBox = new JComboBox(new String[]{"Фамилия", "Имя", "VIN"});
        textSearch = new JTextField();
        textSearch.setColumns(20);
        search = new JButton("Поиск");
        carList.getRootPane().setDefaultButton(search);
// remove the binding for pressed
        carList.getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW)
                .put(KeyStroke.getKeyStroke("ENTER"), "none");
// retarget the binding for released
        carList.getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW)
                .put(KeyStroke.getKeyStroke("released ENTER"), "press");
        // Добавление компонентов на панель
        JPanel searchPanel = new JPanel();
        searchPanel.add(comboBox);
        searchPanel.add(textSearch);
        searchPanel.add(search);

        // Размещение панели поиска внизу окна
        carList.add(searchPanel,BorderLayout.SOUTH);

        add.addActionListener((e) -> {
            addDialogProd = new AddDialogProd(carList, products.this, "Добавление записи");
            addDialogProd.setVisible(true);
        });

        add.setMnemonic(KeyEvent.VK_A);
        delete.addActionListener((e) -> {
            if (cars.getRowCount() > 0) {
                if (cars.getSelectedRow() != -1) {
                    try {
                        model.removeRow(cars.convertRowIndexToModel(cars.getSelectedRow()));
                        JOptionPane.showMessageDialog(carList, "Вы удалили строку");
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

        edit.addActionListener((e)-> {
            if (t1.isAlive()) {
                try {
                    JOptionPane.showMessageDialog(carList, "Ждем, пока отработает 1 поток");
                    t1.join();
                    JOptionPane.showMessageDialog(carList, "1 поток отработал, пробуем запустить 2 поток");
                } catch (InterruptedException ex) {
                    ex.printStackTrace();
                }
            }
            if (model.getRowCount() != 0) {
                if (cars.getSelectedRow() != -1) {
                    t2 = new Thread(() -> {
                        JOptionPane.showMessageDialog(null,"2 поток запущен");
                        editDialogProd = new EditDialogProd(carList, products.this, "Редактирование");
                        editDialogProd.setVisible(true);
                    });
                    t2.start();
                } else {
                    JOptionPane.showMessageDialog(null, "Не выбрана строка. Нечего редактировать");
                }
            } else {
                JOptionPane.showMessageDialog(null, "В данном окне нет записей. Нечего редактировать");
            }
        });
        edit.setMnemonic(KeyEvent.VK_E);

        save.addActionListener((e) -> {
            if (t2.isAlive()) {
                try {
                    t2.join();
                } catch (InterruptedException ex) {
                    ex.printStackTrace();
                }
            }
            if (model.getRowCount() != 0) {
                t3 = new Thread(() -> {
                    makeXml();
                });
                t3.start();
            }
        });

        folder.addActionListener((e) -> {
            t1 = new Thread(() -> {
                loadXML();
                cars.setRowSelectionInterval(0,0);
                SwingUtilities.invokeLater(() -> {
                    JOptionPane.showMessageDialog(carList, "1 поток закончил работу.");
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
                cars.setRowSorter(sorter);
            }
        });

        print.addActionListener((e)->{
            if (model.getRowCount() != 0) {
                LabTest.print("D:\\LabTest\\cars.xml", "carList/cars", "D:\\LabTest\\cars.jrxml", "D:\\LabTest\\cars.html");
            }
        });

        // Если не выделена строка, то прячем кнопки
        cars.getSelectionModel().addListSelectionListener((e) -> {
            Boolean check = true;
            if (cars.getSelectionModel().isSelectionEmpty()) {
                check = false;
            }
            edit.setVisible(check);
            delete.setVisible(check);
        });

        carList.setVisible(true);
    }
    public void addR(String[] arr){
        model.addRow(arr);
    }

}
