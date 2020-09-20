package editor;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class TextEditor extends JFrame {

    int[][] indexAndLength;
    int ii = 1;

    public TextEditor() {
        super("Notepad--");
        initComponents();
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 300);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void initComponents() {
        JTextArea textArea = new JTextArea();
        JPanel topPanel = new JPanel();
        JScrollPane scrollPane = new JScrollPane(textArea);
        JButton saveButton = new JButton("Save");
        JButton loadButton = new JButton("Load");
        JButton searchButton = new JButton("Search");
        JButton nextMatch = new JButton(">");
        JButton prevMatch = new JButton("<");
        JTextField searchField = new JTextField();
        JMenuBar menuBar = new JMenuBar();
        JMenu fileMenu = new JMenu("File");
        JMenuItem menuLoad = new JMenuItem("Load");
        JMenuItem menuSave = new JMenuItem("Save");
        JMenuItem menuExit = new JMenuItem("Exit");
        JMenu searchMenu = new JMenu("Search");
        JMenuItem menuStartSearch = new JMenuItem("Start Search");
        JMenuItem menuNextMatch = new JMenuItem("Next Match");
        JMenuItem menuPrevMatch = new JMenuItem("Previous Match");
        JMenuItem menuRegex = new JMenuItem("Use Regex?");
        JFileChooser fileChooser = new JFileChooser("./");
        JCheckBox useRegex = new JCheckBox("Use Regex");
        topPanel.setLayout(new GridLayout(1, 3, 10, 10));

        textArea.setName("TextArea");
        searchField.setName("SearchField");
        saveButton.setName("SaveButton");
        loadButton.setName("OpenButton");
        searchButton.setName("StartSearchButton");
        nextMatch.setName("NextMatchButton");
        prevMatch.setName("PreviousMatchButton");
        useRegex.setName("UseRegExCheckbox");
        fileChooser.setName("FileChooser");
        scrollPane.setName("ScrollPane");
        fileMenu.setName("MenuFile");
        menuLoad.setName("MenuOpen");
        menuSave.setName("MenuSave");
        menuExit.setName("MenuExit");
        searchMenu.setName("MenuSearch");
        menuStartSearch.setName("MenuStartSearch");
        menuPrevMatch.setName("MenuPreviousMatch");
        menuNextMatch.setName("MenuNextMatch");
        menuRegex.setName("MenuUseRegExp");

        nextMatch.addActionListener(e -> selectNext(textArea));
        menuNextMatch.addActionListener(e -> selectNext(textArea));
        prevMatch.addActionListener(e -> selectPrevious(textArea));
        menuPrevMatch.addActionListener(e -> selectPrevious(textArea));
        searchButton.addActionListener(e -> search(textArea, searchField, useRegex));
        menuStartSearch.addActionListener(e -> search(textArea, searchField, useRegex));
        saveButton.addActionListener(e -> saveFile(textArea, fileChooser));
        loadButton.addActionListener(e -> loadFile(textArea, fileChooser));
        menuSave.addActionListener(e -> saveFile(textArea, fileChooser));
        menuLoad.addActionListener(e -> loadFile(textArea, fileChooser));
        menuRegex.addActionListener(e -> useRegex.setSelected(!useRegex.isSelected()));
        menuExit.addActionListener(e -> this.dispose());

        add(fileChooser);

        setJMenuBar(menuBar);
        menuBar.add(fileMenu);
        fileMenu.add(menuLoad);
        fileMenu.add(menuSave);
        fileMenu.add(menuExit);
        menuBar.add(searchMenu);
        searchMenu.add(menuStartSearch);
        searchMenu.add(menuPrevMatch);
        searchMenu.add(menuNextMatch);
        searchMenu.add(menuRegex);

        topPanel.add(saveButton);
        topPanel.add(loadButton);
        topPanel.add(searchField);
        topPanel.add(searchButton);
        topPanel.add(useRegex);
        topPanel.add(prevMatch);
        topPanel.add(nextMatch);

        add(scrollPane, BorderLayout.CENTER);
        add(topPanel, BorderLayout.NORTH);

    }

    private void search(JTextArea text, JTextField bar, JCheckBox box) {
        Search search = new Search(text.getText(), bar.getText(), box.isSelected());
        search.start();
        try {
            search.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        indexAndLength = search.indexAndLength;

        ii = 0;

        int index = indexAndLength[ii][0];
        int foundText = indexAndLength[ii][1];

        text.setCaretPosition(index + foundText);
        text.select(index, index + foundText);
        text.grabFocus();
    }

    private void selectNext(JTextArea textArea) {
        ii++;
        if (ii == indexAndLength.length) {
            ii = 0;
        }
        int index = indexAndLength[ii][0];
        int foundText = indexAndLength[ii][1];

        textArea.setCaretPosition(index + foundText);
        textArea.select(index, index + foundText);
        textArea.grabFocus();
        System.out.println("ii = " + ii);
        System.out.print(indexAndLength[ii][0] + ", "); System.out.println(indexAndLength[ii][1]);
    }
    private void selectPrevious(JTextArea textArea) {
        ii--;
        if (ii < 0) {
            ii = indexAndLength.length - 1;
        }
        int index = indexAndLength[ii][0];
        int foundText = indexAndLength[ii][1];

        textArea.setCaretPosition(index + foundText);
        textArea.select(index, index + foundText);
        textArea.grabFocus();
        System.out.println("ii = " + ii);
        System.out.print(indexAndLength[ii][0] + ", "); System.out.println(indexAndLength[ii][1]);
    }

    public static String readFileAsString(String fileName) throws IOException {
        return new String(Files.readAllBytes(Paths.get(fileName)));
    }

    private void saveFile(JTextArea textArea, JFileChooser fileChooser) {

        fileChooser.setVisible(true);
        int returnVal = fileChooser.showSaveDialog(null);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            try (FileWriter fileIO = new FileWriter(fileChooser.getSelectedFile(), false)) {

                fileIO.write(textArea.getText());
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        }
        fileChooser.setVisible(false);
    }

    private void loadFile(JTextArea textArea, JFileChooser fileChooser) {

        fileChooser.setVisible(true);
        int returnVal = fileChooser.showOpenDialog(null);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            try {
                File file = fileChooser.getSelectedFile();
                textArea.setText(readFileAsString(file.getPath()));

            } catch (Exception exception) {
                exception.printStackTrace();
                textArea.setText("");
            }
        }
        fileChooser.setVisible(false);
    }
}

