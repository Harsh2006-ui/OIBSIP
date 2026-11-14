package com.oasis.reservation.ui;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import java.awt.BorderLayout;
import java.awt.Font;

/** Main window shown after login: tabs for booking and cancellation. */
public class MainFrame extends JFrame {

    public MainFrame(String username) {
        super("Online Reservation System");
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        JLabel who = new JLabel("Logged in as: " + username);
        who.setFont(who.getFont().deriveFont(Font.BOLD));
        JButton logout = new JButton("Logout");
        logout.addActionListener(e -> {
            dispose();
            new LoginFrame();
        });

        JPanel header = new JPanel(new BorderLayout());
        header.setBorder(BorderFactory.createEmptyBorder(8, 12, 8, 12));
        header.add(who, BorderLayout.WEST);
        header.add(logout, BorderLayout.EAST);

        JTabbedPane tabs = new JTabbedPane();
        tabs.addTab("Book Ticket", new BookingPanel(username));
        tabs.addTab("Cancel Ticket", new CancelPanel());

        add(header, BorderLayout.NORTH);
        add(tabs, BorderLayout.CENTER);

        setSize(560, 520);
        setLocationRelativeTo(null);
        setVisible(true);
    }
}
