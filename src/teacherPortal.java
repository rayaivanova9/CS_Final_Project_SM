import javax.swing.*;

public class teacherPortal extends JFrame{
    private JButton goToCoursesButton;
    private JButton goToGradesButton;
    private JButton goToTeachersButton;
    private JButton goToStudentsButton;
    private JTextArea textArea;
    private JPanel teacherPortalPanel;
    private JLabel courseCount;

    public teacherPortal(String email) {
        setSize(500, 250);
        setContentPane(teacherPortalPanel);
        setVisible(true);
        String query = "SELECT COUNT(*) FROM courses WHERE email = ?";

        courseCount.setText(String.valueOf(connect.executeCoursesCount(query, email)));
    }
}
