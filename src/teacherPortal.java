import javax.swing.*;
import javax.swing.event.DocumentListener;
import javax.swing.event.DocumentEvent;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class teacherPortal extends JFrame{
    private JButton goToCoursesButton;
    private JButton goToGradesButton;
    private JButton goToTeachersButton;
    private JButton goToStudentsButton;
    private JTextArea textArea1;
    private JPanel teacherPortalPanel;
    private JLabel courseCount;
    private JLabel img;

    public teacherPortal(String email) {
        setSize(500, 250);
        setContentPane(teacherPortalPanel);
        setVisible(true);
        String query = "SELECT COUNT(*) FROM courses WHERE email = ?";
        textArea1.setText(connect.loadText(email));

        courseCount.setText(String.valueOf((int)connect.executeCoursesCount(query, email)));

        textArea1.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                connect.saveText(textArea1.getText(), email);
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                connect.saveText(textArea1.getText(), email);
            }

            @Override
            public void changedUpdate(DocumentEvent e) {

            }
        });

        goToCoursesButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                courses.model.setColumnCount(0);
                new courses(email);
            }
        });


        goToGradesButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                grades.model.setColumnCount(0);
                new grades(email);
            }
        });
        goToTeachersButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                teachers.model.setColumnCount(0);
                new teachers();
            }
        });
        goToStudentsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                studentsForm.model.setColumnCount(0);
                new studentsForm();
            }
        });
    }
}
