import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.util.ArrayList;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class studentsForm extends JFrame{
    private JTextField searchBar;
    private JTable table;
    private JComboBox comboBox;
    private JPanel studentsFormPanel;

    public static DefaultTableModel model = new DefaultTableModel();
    private ArrayList<String[]> columns;

    public studentsForm() {
        setSize(700, 700);
        setContentPane(studentsFormPanel);
        setVisible(true);
        table.setModel(model);

        String query = "SELECT student_id AS ID, first_name AS Name, last_name AS Surname, email AS Email FROM students";
        columns = connect.executeQuery(query, 4);
        updateTable();

        searchBar.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                //super.keyTyped(e);
                updateTableSearch(searchBar.getText());
            }
        });

        comboBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String query1 = query + " ORDER BY ";
                if (comboBox.getSelectedIndex() == 0) {
                    query1 = query1 + "Email ASC";
                }
                else if (comboBox.getSelectedIndex() == 1) {
                    query1 = query1 + "Email DESC";
                }

                model.setColumnCount(0);
                model.setRowCount(0);
                columns = connect.executeQuery(query1, 4);

                updateTable();
            }
        });
    }
    private void updateTable() {
        model.setRowCount(0);
        for (String[] column : columns) {
            model.addRow(column);
        }
    }

    private void updateTableSearch(String search) {
        model.setRowCount(0); // Clear all existing rows in the table
        for (String[] row : columns) {
            for (String cell : row) {
                if (cell != null && cell.toLowerCase().contains(search.toLowerCase())) {
                    model.addRow(row);
                    break;
                }
            }
        }
    }
}
