package com.oasis.reservation.ui;

import com.oasis.reservation.dao.UserDAO;
import com.oasis.reservation.util.Validator;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.sql.SQLException;

/** Login window: username + password, access denied for invalid credentials. */
public class LoginFrame extends JFrame {

    private final JTextField usernameField = new JTextField(18);
    private final JPasswordField passwordField = new JPasswordField(18);
    private final UserDAO userDAO = new UserDAO();

    public LoginFrame() {
        super("Online Reservation System - Login");
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(20, 25, 20, 25));
        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(6, 6, 6, 6);

        JLabel title = new JLabel("Train Reservation Login", JLabel.CENTER);
        title.setFont(title.getFont().deriveFont(Font.BOLD, 18f));
        c.gridx = 0; c.gridy = 0; c.gridwidth = 2; c.fill = GridBagConstraints.HORIZONTAL;
        panel.add(title, c);

        c.gridwidth = 1; c.fill = GridBagConstraints.NONE; c.anchor = GridBagConstraints.WEST;
        c.gridx = 0; c.gridy = 1; panel.add(new JLabel("Username:"), c);
        c.gridx = 1; panel.add(usernameField, c);
        c.gridx = 0; c.gridy = 2; panel.add(new JLabel("Password:"), c);
        c.gridx = 1; panel.add(passwordField, c);

        JButton loginBtn = new JButton("Login");
        c.gridx = 0; c.gridy = 3; c.gridwidth = 2; c.anchor = GridBagConstraints.CENTER;
        panel.add(loginBtn, c);

        JLabel hint = new JLabel("Demo account: student / pass123", JLabel.CENTER);
        hint.setFont(hint.getFont().deriveFont(Font.ITALIC, 11f));
        c.gridy = 4;
        panel.add(hint, c);

        loginBtn.addActionListener(e -> doLogin());
        getRootPane().setDefaultButton(loginBtn); // Enter key = Login

        setContentPane(panel);
        pack();
        setResizable(false);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void doLogin() {
        String username = usernameField.getText().trim();
        String password = new String(passwordField.getPassword());

        if (Validator.isBlank(username) || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter both username and password.",
                    "Validation Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            if (userDAO.authenticate(username, password)) {
                dispose();
                new MainFrame(username);
            } else {
                JOptionPane.showMessageDialog(this, "Access Denied: invalid username or password.",
                        "Login Failed", JOptionPane.ERROR_MESSAGE);
                passwordField.setText("");
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Database error: " + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
