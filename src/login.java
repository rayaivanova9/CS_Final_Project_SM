import javax.swing.*;
import org.mindrot.jbcrypt.BCrypt;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class login extends JFrame {
    private JTextField emailField;
    private JPasswordField passwordField;
    private JComboBox comboBox;
    private JButton logInButton;
    private JLabel message;
    private JPanel loginPanel;

    public login() {
        setSize(500, 500);
        setContentPane(loginPanel);
        setVisible(true);
        loginPanel.setBackground(new Color(153, 153, 204));

        logInButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int role = comboBox.getSelectedIndex();
                String email = emailField.getText();
                String password = String.valueOf(passwordField.getPassword());

                if (role != 0) {
                    if (connect.executeValidation(email, password, role)) {
                        message.setText("You were successfully logged in!");
                        if (role == 1) {
                            new studentPortal(email);
                            login.this.dispose();
                        }
                        else if (role == 2) {
                            new teacherPortal(email);
                            login.this.dispose();
                        }
                        else {
                            JOptionPane.showMessageDialog(
                                    null, null,
                                    "Error", JOptionPane.ERROR_MESSAGE);
                        }
                    }
                    else {
                        message.setText("Wrong email or password.");
                    }
                }
                else {
                    message.setText("Please choose an option.");
                }
            }
        });
    }
}


