import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class teachers extends JFrame{
    private JTextField searchBar;
    private JTable table;
    private JComboBox comboBox;
    private JPanel teachersPanel;

    public static DefaultTableModel model = new DefaultTableModel();
    private ArrayList<String[]> columns;

    public teachers() {
        setSize(700, 300);
        setContentPane(teachersPanel);
        setVisible(true);
        table.setModel(model);
        teachersPanel.setBackground(new Color(202, 196, 245));

        String query = "SELECT teacher_id AS ID, first_name AS Name, last_name AS Surname, email AS Email FROM teachers";
        columns = connect.executeQuery(query, 3);
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
                columns = connect.executeQuery(query1, 3);

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
