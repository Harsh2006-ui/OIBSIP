package com.oasis.reservation;

import com.oasis.reservation.db.DBHelper;
import com.oasis.reservation.ui.LoginFrame;

import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import java.sql.SQLException;

/**
 * Entry point of the Online Reservation System.
 * Creates the database (first run only) and opens the login window.
 */
public class Main {
    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ignored) {
            // fall back to the default Swing look and feel
        }

        try {
            DBHelper.init();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null,
                    "Could not initialise the database:\n" + e.getMessage(),
                    "Database Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        SwingUtilities.invokeLater(LoginFrame::new);
    }
}
