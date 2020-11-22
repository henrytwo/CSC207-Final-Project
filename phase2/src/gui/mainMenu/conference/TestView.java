package gui.mainMenu.conference;

import gui.util.Panelable;

import javax.swing.*;

public class TestView implements Panelable {
    private JTabbedPane tabbedPane1;
    private JPanel panel;
    private JList list1;
    private JSpinner spinner1;
    private JPasswordField mainPassword;
    private JRadioButton someButton;
    private JCheckBox checkBox1;
    private JCheckBox checkBox2;
    private JTextArea textArea1;
    private JTable table1;
    private JEditorPane editorPane1;
    private JTextField textField1;
    private JCheckBox checkBox3;
    private JCheckBox checkBox4;

    @Override
    public Panelable getParent() {
        return null;
    }

    @Override
    public JPanel getPanel() {
        return panel;
    }
}
