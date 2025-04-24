import javax.swing.*;

public class students extends JFrame{
    private JTextField searchBar;
    private JTable table;
    private JComboBox comboBox;
    private JPanel studentsPanel;
    private JLabel tableName;

    public students(int index, String email) {
        setSize(500, 250);
        setContentPane(studentsPanel);
        setVisible(true);
        String query;

        if (index == 1) {
            tableName.setText("My Grades");
            query = "SELECT courses.course_name, grades.grade" +
                    "FROM grades" +
                    "         JOIN students ON grades.student_id = students.student_id" +
                    "         JOIN courses ON grades.course_id = courses.course_id" +
                    "WHERE students.email = ?";
            connect.executeTable(query, email);
        }
        else if (index == 2) {
            tableName.setText("My Teachers");

        }
        else {

        }
    }
}
