import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class grades extends JFrame{
    private JTextField searchBar;
    private JTable table;
    private JComboBox comboBox;
    private JButton addRowButton;
    private JButton deleteRowButton;
    private JPanel gradesPanel;
    private JLabel message;
    private JButton updateGradeButton;

    public static DefaultTableModel model = new DefaultTableModel();
    private ArrayList<String[]> columns;

    public grades(String email) {
        setSize(700, 300);
        setContentPane(gradesPanel);
        setVisible(true);
        table.setModel(model);
        gradesPanel.setBackground(new Color(202, 196, 245));

        String query = "SELECT students.student_id, CONCAT(students.first_name, ' ', students.last_name) AS Student, courses.course_name AS Course, grades.grade AS Grade " +
        "FROM grades " +
        "JOIN courses ON grades.course_id = courses.course_id " +
        "JOIN students ON grades.student_id = students.student_id " +
        "WHERE courses.email = ?";

        columns = connect.executeTable(query, email, 2, 2);
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
                switch(comboBox.getSelectedIndex()){
                    case 0:
                        query1 = query1 + "Student ASC";
                        break;
                    case 1:
                        query1 = query1 + "Student DESC";
                        break;
                    case 2:
                        query1 = query1 + "Course ASC";
                        break;
                    case 3:
                        query1 = query1 + "Course DESC";
                        break;
                    case 4:
                        query1 = query1 + "Grade ASC";
                        break;
                    case 5:
                        query1 = query1 + "Grade DESC";
                        break;
                }

                model.setColumnCount(0);
                model.setRowCount(0);
                columns = connect.executeTable(query1, email, 2, 2);

                updateTable();
            }
        });

        deleteRowButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedRow = table.getSelectedRow();
                if (selectedRow != -1) {
                    int selectedId = Integer.parseInt(model.getValueAt(selectedRow, 0).toString());
                    connect.deleteRow(selectedId, "grades", "student_id");
                    model.removeRow(selectedRow);
                    message.setText("Row deleted.");
                }
                else {
                    message.setText("No row selected.");
                }
                //updateTable();
            }
        });

        addRowButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int newStudentId = Integer.parseInt(JOptionPane.showInputDialog("Enter the ID of the student"));
                int newCourseId = Integer.parseInt(JOptionPane.showInputDialog("Enter course ID"));
                double newGrade = Double.parseDouble(JOptionPane.showInputDialog("Enter grade"));
                String tableName = "grades";
                String queryAddRow = "INSERT INTO grades (student_id, course_id, grade, email) VALUES (?, ?, ?, ?)";
                String newEmail = connect.getEmail(newStudentId);
                connect.addRowGrades(newStudentId, tableName, newEmail, newCourseId, newGrade, queryAddRow);

                model.setColumnCount(0);
                model.setRowCount(0);
                columns = connect.executeTable(query, email, 2, 2);

                updateTable();
            }
        });

        updateGradeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int newStudentId = Integer.parseInt(JOptionPane.showInputDialog("Enter the ID of the student"));
                int newCourseId = Integer.parseInt(JOptionPane.showInputDialog("Enter course ID"));
                double newGrade = Double.parseDouble(JOptionPane.showInputDialog("Enter grade"));
                String queryUpdate = "UPDATE grades SET grade = ? WHERE student_id = ? AND course_id = ?;";

                connect.updateGrade(newStudentId, newCourseId, newGrade, queryUpdate);

                model.setColumnCount(0);
                model.setRowCount(0);
                columns = connect.executeTable(query, email, 2, 2);

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
