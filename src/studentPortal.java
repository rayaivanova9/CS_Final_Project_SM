import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.event.DocumentListener;
import javax.swing.event.DocumentEvent;

public class studentPortal extends JFrame{
    private JButton goToMyGradesButton;
    private JButton goToMyTeachersButton;
    public JTextArea textArea1;
    private JPanel studentPortalPanel;
    private JLabel courseCount;
    private JLabel gpaLabel;

    public studentPortal(String email) {
        setSize(500, 250);
        setContentPane(studentPortalPanel);
        setVisible(true);
        studentPortalPanel.setBackground(new Color(190, 225, 225));

        String query = "SELECT COUNT(*) FROM grades WHERE email = ?";
        String gpaQuery = "SELECT gpa FROM students WHERE email = ?";
        textArea1.setText(connect.loadText(email));

        courseCount.setText(String.valueOf((int)connect.executeCoursesCount(query, email)));
        gpaLabel.setText(String.valueOf(connect.executeCoursesCount(gpaQuery, email)));

        goToMyGradesButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                students.model.setColumnCount(0);
                new students(1, email);
            }
        });
        goToMyTeachersButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                students.model.setColumnCount(0);
                new students(2, email);
            }
        });
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
    }
}
