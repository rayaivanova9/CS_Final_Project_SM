import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class courses extends JFrame{
    private JTextField searchBar;
    private JTable table;
    private JComboBox comboBox;
    private JButton addRowButton;
    private JButton deleteRowButton;
    private JPanel coursesPanel;
    private JLabel message;

    public static DefaultTableModel model = new DefaultTableModel();
    private ArrayList<String[]> columns;

    public courses(String email) {
        setSize(700, 300);
        setContentPane(coursesPanel);
        setVisible(true);
        table.setModel(model);

        coursesPanel.setBackground(new Color(202, 196, 245));

        String query = "SELECT courses.course_id AS ID, courses.course_name AS Course " +
                "FROM courses " +
                "WHERE courses.email = ?";

        columns = connect.executeTable(query, email, 2, 1);
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
                    query1 = query1 + "Course ASC";
                }
                else if (comboBox.getSelectedIndex() == 1) {
                    query1 = query1 + "Course DESC";
                }

                model.setColumnCount(0);
                model.setRowCount(0);
                columns = connect.executeTable(query1, email, 2, 1);

                updateTable();
            }
        });

        deleteRowButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedRow = table.getSelectedRow();
                if (selectedRow != -1) {
                    int selectedId = Integer.parseInt(model.getValueAt(selectedRow, 0).toString());
                    connect.deleteRow(selectedId, "courses", "course_id");
                    model.removeRow(selectedRow);
                    message.setText("Row deleted.");
                }
                else {
                    message.setText("No row selected.");
                }
                //updateTable();
            }
        });

        table.setDefaultEditor(Object.class, new DefaultCellEditor(new JTextField()) {
            private String oldValue; // Store the old value before editing


            @Override
            public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
                oldValue = (value != null) ? value.toString() : ""; // Capture the old value
                return super.getTableCellEditorComponent(table, value, isSelected, row, column);
            }


            @Override
            public boolean stopCellEditing() {
                // Capture the new value entered by the user
                String newValue = getCellEditorValue().toString();


                // Get the row and column indices
                int row = table.getEditingRow();
                int column = table.getEditingColumn();


                // Get the name of the column being edited
                String columnName = table.getColumnName(column);

                int id = Integer.parseInt(table.getValueAt(row, 0).toString());


                // Only update if the value has changed
                if (!newValue.equals(oldValue)) {
                    // Call the updated updateDatabase method with a single column update
                    String[] columns = {columnName};
                    String[] newValues = {newValue};
                    connect.updateDatabase(email, columns, newValues, "courses");
                }


                return super.stopCellEditing();
            }
        });

        addRowButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String newCourseName = JOptionPane.showInputDialog("Enter the name of the course");
                String tableName = "courses";
                String queryAddRow = "INSERT INTO courses (course_name, teacher_id, email) VALUES (?, ?, ?)";
                int id = connect.getId(email);
                connect.addRowCourses(newCourseName, tableName, email, id, queryAddRow);

                model.setColumnCount(0);
                model.setRowCount(0);
                columns = connect.executeTable(query, email, 2, 1);

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
