import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class studentPortal extends JFrame{
    private JButton goToMyGradesButton;
    private JButton goToMyTeachersButton;
    private JTextArea textArea1;
    private JPanel studentPortalPanel;
    private JLabel courseCount;
    private JLabel gpaLabel;

    public studentPortal(String email) {
        setSize(500, 250);
        setContentPane(studentPortalPanel);
        setVisible(true);
        String query = "SELECT COUNT(*) FROM grades WHERE email = ?";
        String gpaQuery = "SELECT gpa FROM students WHERE email = ?";

        courseCount.setText(String.valueOf((int)connect.executeCoursesCount(query, email)));
        gpaLabel.setText(String.valueOf(connect.executeCoursesCount(gpaQuery, email)));

        goToMyGradesButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new students(1, email);
            }
        });
    }
}
