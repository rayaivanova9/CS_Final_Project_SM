import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class login extends JFrame{
    private JTextField emailField;
    private JPasswordField passwordField;
    private JComboBox comboBox;
    private JButton logInButton;
    private JLabel img;
    private JLabel message;
    private JPanel loginPanel;

    public login() {
        setSize(500, 500);
        setContentPane(loginPanel);
        setVisible(true);


        logInButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String email = emailField.getText();
                String password = String.valueOf(passwordField.getPassword());


            }
        });
    }
}
