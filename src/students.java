import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;

public class students extends JFrame{
    private JTextField searchBar;
    private JTable table;
    private JComboBox comboBox;
    private JPanel studentsPanel;
    private JLabel tableName;

    public static DefaultTableModel model = new DefaultTableModel();
    private ArrayList<String[]> columns;

    public students(int index, String email) {
        setSize(700, 250);
        setContentPane(studentsPanel);
        setVisible(true);
        String queryGrades = "SELECT courses.course_name AS Course, grades.grade AS Grade " +
                "FROM grades " +
                "JOIN students ON grades.student_id = students.student_id " +
                "JOIN courses ON grades.course_id = courses.course_id " +
                "WHERE students.email = ?";
        String queryTeachers = "SELECT " +
                "courses.course_name AS Course, " +
                "CONCAT(teachers.first_name, ' ', teachers.last_name) AS Name, " +
                "teachers.email AS Email " +
                "FROM students " +
                "JOIN grades ON students.student_id = grades.student_id " +
                "JOIN courses ON grades.course_id = courses.course_id " +
                "JOIN teachers ON courses.teacher_id = teachers.teacher_id " +
                "WHERE students.email = ?";
        table.setModel(model);


        if (index == 1) {
            tableName.setText("My Grades");

            columns = connect.executeTable(queryGrades, email, 1);
            updateTable();

            comboBox.addItem("Course Name A-Z");
            comboBox.addItem("Course Name Z-A");
            comboBox.addItem("Grade Acs");
            comboBox.addItem("Grade Desc");
        }
        else if (index == 2) {
            tableName.setText("My Teachers");

            columns = connect.executeTable(queryTeachers, email, 1);
            updateTable();

            comboBox.addItem("Teacher Name A-Z");
            comboBox.addItem("Teacher Name Z-A");
            comboBox.addItem("Course Name A-Z");
            comboBox.addItem("Course Name Z-A");
        }
        else {
            JOptionPane.showMessageDialog(
                    null, null,
                    "Error", JOptionPane.ERROR_MESSAGE);
            students.this.dispose();
        }


        comboBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (index == 1) {
                    String queryGrades1 = queryGrades + " ORDER BY ";

                    switch (comboBox.getSelectedIndex()) {
                        case 0:
                            queryGrades1 = queryGrades1 + "Course ASC";
                            break;
                        case 1:
                            queryGrades1 = queryGrades1 + "Course DESC";
                            break;
                        case 2:
                            queryGrades1 = queryGrades1 + "Grade ASC";
                            break;
                        case 3:
                            queryGrades1 = queryGrades1 + "Grade DESC";
                            break;
                    }
                    model.setColumnCount(0);
                    model.setRowCount(0);
                    columns = connect.executeTable(queryGrades1, email, 1);
                }
                else if (index == 2) {
                    String queryTeachers1 = queryTeachers + " ORDER BY ";

                    switch (comboBox.getSelectedIndex()) {
                        case 0:
                            queryTeachers1 = queryTeachers1 + "Name ASC";
                            break;
                        case 1:
                            queryTeachers1 = queryTeachers1 + "Name DESC";
                            break;
                        case 2:
                            queryTeachers1 = queryTeachers1 + "Course ASC";
                            break;
                        case 3:
                            queryTeachers1 = queryTeachers1 + "Course DESC";
                            break;
                    }
                    model.setColumnCount(0);
                    model.setRowCount(0);
                    columns = connect.executeTable(queryTeachers1, email, 1);
                }
                updateTable();
            }
        });

        searchBar.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                //super.keyTyped(e);
                updateTableSearch(searchBar.getText());
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
