package gui;

import worktofiles.FileReader;
import worktofiles.TextFinder;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Window extends JFrame {

    private List<String> listOfPath = new ArrayList<>();
    private JTextField extensionField;
    private JTextField pathField;
    private JTextField tosearchField;
    private JList<String> pathList;
    private JEditorPane  textFromFile;

    private FileReader fileReader;

    public Window() {
        super("Task1");
        ActionListener action = new Action();
        ListSelectionListener listOfAction = new ListOfAction();

        setDefaultCloseOperation(EXIT_ON_CLOSE);
        extensionField = new JTextField("log",5);
        extensionField.setHorizontalAlignment(JTextField.LEFT);
        pathField = new JTextField("D:\\test",25);
        tosearchField = new JTextField(25);

        extensionField.addActionListener(action);
        extensionField.setActionCommand("find");
        pathField.addActionListener(action);
        pathField.setActionCommand("find");
        tosearchField.addActionListener(action);
        tosearchField.setActionCommand("find");

        JLabel pathLabel = new JLabel("Path :");
        JLabel extensionLabel = new JLabel("Extension :");
        JLabel tosearchLabel = new JLabel("Text :");

        JButton findButton = new JButton("Find");
        findButton.addActionListener(action);
        findButton.setActionCommand("find");

        JButton nextButton = new JButton("Next");
        nextButton.addActionListener(action);
        nextButton.setActionCommand("next");

        JButton prevButton = new JButton("Prev");
        prevButton.addActionListener(action);
        prevButton.setActionCommand("prev");

        SpringLayout layout = new SpringLayout();

        Container contentPane = getContentPane();
        contentPane.setLayout(layout);

        contentPane.add(tosearchLabel);
        contentPane.add(tosearchField);
        contentPane.add(pathLabel);
        contentPane.add(pathField);
        contentPane.add(extensionLabel);
        contentPane.add(extensionField);
        contentPane.add(findButton);
        contentPane.add(nextButton);
        contentPane.add(prevButton);

        layout.putConstraint(SpringLayout.WEST , tosearchLabel, 10,
                SpringLayout.WEST , contentPane);
        layout.putConstraint(SpringLayout.NORTH , tosearchLabel, 10,
                SpringLayout.WEST , contentPane);
        layout.putConstraint(SpringLayout.WEST , tosearchField, 10,
                SpringLayout.EAST , tosearchLabel);
        layout.putConstraint(SpringLayout.NORTH , tosearchField, 10,
                SpringLayout.WEST , contentPane);
        layout.putConstraint(SpringLayout.WEST , pathLabel, 10,
                SpringLayout.EAST , tosearchField);
        layout.putConstraint(SpringLayout.NORTH , pathLabel, 10,
                SpringLayout.WEST , contentPane);
        layout.putConstraint(SpringLayout.WEST , pathField, 10,
                SpringLayout.EAST , pathLabel);
        layout.putConstraint(SpringLayout.NORTH , pathField, 10,
                SpringLayout.WEST , contentPane);
        layout.putConstraint(SpringLayout.WEST , extensionLabel, 10,
                SpringLayout.EAST , pathField);
        layout.putConstraint(SpringLayout.NORTH , extensionLabel, 10,
                SpringLayout.WEST , contentPane);
        layout.putConstraint(SpringLayout.WEST , extensionField, 10,
                SpringLayout.EAST , extensionLabel);
        layout.putConstraint(SpringLayout.NORTH , extensionField, 10,
                SpringLayout.WEST , contentPane);
        layout.putConstraint(SpringLayout.WEST , findButton, 10,
                SpringLayout.EAST , extensionField);
        layout.putConstraint(SpringLayout.NORTH , findButton, 5,
                SpringLayout.WEST , contentPane);

        pathList = new JList<>(listOfPath.toArray(new String[listOfPath.size()]));
        pathList.addListSelectionListener(listOfAction);

        contentPane.add(pathList);

        layout.putConstraint(SpringLayout.WEST , pathList, 10,
                SpringLayout.WEST , contentPane);
        layout.putConstraint(SpringLayout.NORTH , pathList, 10,
                SpringLayout.SOUTH , pathLabel);

        textFromFile = new JEditorPane();
        textFromFile.setEditable(false);
        textFromFile.setSize(500, 400);

        int vsb = ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS;
        int hsb = ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS;
        JScrollPane scrollPane = new JScrollPane(textFromFile, vsb, hsb);
        scrollPane.setPreferredSize(new Dimension(500, 600));
        contentPane.add(scrollPane);

        layout.putConstraint(SpringLayout.EAST , scrollPane, -10,
                SpringLayout.EAST , contentPane);
        layout.putConstraint(SpringLayout.NORTH , scrollPane, 10,
                SpringLayout.SOUTH , pathLabel);
        layout.putConstraint(SpringLayout.EAST , nextButton, -10,
                SpringLayout.EAST , extensionField);
        layout.putConstraint(SpringLayout.NORTH , nextButton, 5,
                SpringLayout.SOUTH , scrollPane);
        layout.putConstraint(SpringLayout.EAST , prevButton, -10,
                SpringLayout.WEST , nextButton);
        layout.putConstraint(SpringLayout.NORTH , prevButton, 5,
                SpringLayout.SOUTH , scrollPane);

        setSize(1000, 810);
        setVisible(true);
    }

    public class Action implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            String text = "";
            switch (e.getActionCommand()) {
                case "find":
                    TextFinder textFinder = new TextFinder();
                    try {
                        textFinder.fileFinde(new File(pathField.getText()), extensionField.getText(), tosearchField.getText());
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }
                    listOfPath = textFinder.getResult();
                    pathList.setListData(listOfPath.toArray(new String[listOfPath.size()]));
                    break;
                case "next":
                    try {
                        text = fileReader.readText(false);
                    }
                    catch (IOException ex) {
                        ex.printStackTrace();
                    }
                    if (text.equals("")) break;
                    textFromFile.setText(text);
                    break;
                case "prev":
                    try {
                        text = fileReader.readText(true);
                    }
                    catch (IOException ex) {
                        ex.printStackTrace();
                    }
                    if (text.equals("")) break;
                    textFromFile.setText(text);
                    break;
            }
        }
    }

    public class ListOfAction implements ListSelectionListener {
        public void valueChanged(ListSelectionEvent e) {
            if (!e.getValueIsAdjusting()) {
                int selected = ((JList<?>) e.getSource()).getSelectedIndex();
                fileReader = new FileReader(listOfPath.get(selected));
                String text = "";
                try {
                    text = fileReader.readText(false);
                }
                catch (IOException ex) {
                    ex.printStackTrace();
                }
                textFromFile.setText(text);
            }
        }
    }

    public String getPathFromTextField() {
        return pathField.getText();
    }

    public String getExtensionFromExtensionField() {
        return extensionField.getText();
    }

}
