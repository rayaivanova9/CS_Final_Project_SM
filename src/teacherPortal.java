import javax.swing.*;
import javax.swing.event.DocumentListener;
import javax.swing.event.DocumentEvent;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class teacherPortal extends JFrame{
    private JButton goToCoursesButton;
    private JButton goToGradesButton;
    private JButton goToTeachersButton;
    private JButton goToStudentsButton;
    private JTextArea textArea1;
    private JPanel teacherPortalPanel;
    private JLabel courseCount;
    private JLabel img;
    private JButton uploadImageButton;

    private File selectedFile;

    public teacherPortal(String email) {
        setSize(500, 700);
        setContentPane(teacherPortalPanel);
        setVisible(true);
        teacherPortalPanel.setBackground(new Color(190, 225, 225));

        String query = "SELECT COUNT(*) FROM courses WHERE email = ?";
        textArea1.setText(connect.loadText(email));
        loadImage(connect.loadTeacherImage(email));

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

        uploadImageButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Open a file chooser for selecting an image
                JFileChooser fileChooser = new JFileChooser();
                int result = fileChooser.showOpenDialog(teacherPortal.this);

                if (result == JFileChooser.APPROVE_OPTION) {
                    selectedFile = fileChooser.getSelectedFile();
                    ImageIcon icon = new ImageIcon(selectedFile.getPath());
                    img.setIcon(new ImageIcon(icon.getImage().getScaledInstance(100, 100, Image.SCALE_DEFAULT)));

                    // Proceed to upload and update the image in the database
                    try {
                        FileInputStream fis = new FileInputStream(selectedFile);
                        byte[] imageData = new byte[(int) selectedFile.length()];
                        fis.read(imageData);

                        // Get the teacher's email and update their image in the database
                        String teacherEmail = email; // Assuming email is unique for each teacher
                        connect.updateTeacherImageByEmail(teacherEmail, imageData);

                        JOptionPane.showMessageDialog(teacherPortal.this, "Teacher's image updated successfully!");

                    } catch (IOException ioException) {
                        JOptionPane.showMessageDialog(teacherPortal.this, "Error updating image.");
                        ioException.printStackTrace();
                    }
                } else {
                    JOptionPane.showMessageDialog(teacherPortal.this, "No file selected.");
                }
            }
        });
    }
    public void loadImage (ImageIcon icon) {
        img.setIcon(icon);
    }
}
