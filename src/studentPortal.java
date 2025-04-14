import javax.swing.*;

public class studentPortal extends JFrame{
    private JButton goToMyGradesButton;
    private JButton goToMyTeachersButton;
    private JTextArea textArea1;
    private JPanel studentPortalPanel;

    public studentPortal() {
        setSize(500, 500);
        setContentPane(studentPortalPanel);
        setVisible(true);
    }
}
