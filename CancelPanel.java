package com.oasis.reservation.ui;

import com.oasis.reservation.dao.ReservationDAO;
import com.oasis.reservation.model.Reservation;
import com.oasis.reservation.util.Validator;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Font;
import java.sql.SQLException;

/** Cancellation form: fetch a booking by PNR, then confirm cancellation. */
public class CancelPanel extends JPanel {

    private final ReservationDAO dao = new ReservationDAO();
    private final JTextField pnrField = new JTextField(14);
    private final JTextArea detailsArea = new JTextArea(10, 40);
    private final JButton cancelBtn = new JButton("Confirm Cancellation");
    private Reservation current;

    public CancelPanel() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));

        JButton fetchBtn = new JButton("Fetch");
        JPanel top = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        top.add(new JLabel("PNR Number:"));
        top.add(pnrField);
        top.add(fetchBtn);

        detailsArea.setEditable(false);
        detailsArea.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 13));
        detailsArea.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));

        cancelBtn.setEnabled(false);
        JPanel bottom = new JPanel(new FlowLayout(FlowLayout.CENTER));
        bottom.add(cancelBtn);

        add(top, BorderLayout.NORTH);
        add(new JScrollPane(detailsArea), BorderLayout.CENTER);
        add(bottom, BorderLayout.SOUTH);

        fetchBtn.addActionListener(e -> doFetch());
        cancelBtn.addActionListener(e -> doCancel());

        // If the PNR text is edited after a fetch, clear the old details
        pnrField.getDocument().addDocumentListener(new DocumentListener() {
            public void insertUpdate(DocumentEvent e) { reset(); }
            public void removeUpdate(DocumentEvent e) { reset(); }
            public void changedUpdate(DocumentEvent e) { reset(); }
        });
    }

    private void reset() {
        current = null;
        detailsArea.setText("");
        cancelBtn.setEnabled(false);
    }

    private void doFetch() {
        String pnrText = pnrField.getText().trim();
        if (Validator.isBlank(pnrText)) {
            warn("Please enter a PNR number.");
            return;
        }
        if (!Validator.isValidPnr(pnrText)) {
            warn("A PNR is a 10-digit number.");
            return;
        }
        try {
            Reservation r = dao.findByPnr(Long.parseLong(pnrText));
            if (r == null) {
                reset();
                warn("No booking found for PNR " + pnrText + ".");
                return;
            }
            current = r;
            detailsArea.setText(r.toDisplayString());
            cancelBtn.setEnabled(true);
        } catch (SQLException ex) {
            dbError(ex);
        }
    }

    private void doCancel() {
        if (current == null) {
            return;
        }
        int choice = JOptionPane.showConfirmDialog(this,
                "Are you sure you want to cancel the booking with PNR " + current.getPnr() + "?",
                "Confirm Cancellation", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
        if (choice != JOptionPane.YES_OPTION) {
            return;
        }
        try {
            long pnr = current.getPnr();
            if (dao.cancel(pnr)) {
                pnrField.setText(""); // also triggers reset()
                JOptionPane.showMessageDialog(this, "Booking " + pnr + " has been cancelled.",
                        "Cancelled", JOptionPane.INFORMATION_MESSAGE);
            } else {
                warn("This booking no longer exists.");
                reset();
            }
        } catch (SQLException ex) {
            dbError(ex);
        }
    }

    private void warn(String message) {
        JOptionPane.showMessageDialog(this, message, "Validation Error", JOptionPane.WARNING_MESSAGE);
    }

    private void dbError(SQLException ex) {
        JOptionPane.showMessageDialog(this, "Database error: " + ex.getMessage(),
                "Error", JOptionPane.ERROR_MESSAGE);
    }
}
