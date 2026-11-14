package com.oasis.reservation.ui;

import com.oasis.reservation.dao.ReservationDAO;
import com.oasis.reservation.dao.TrainDAO;
import com.oasis.reservation.model.Reservation;
import com.oasis.reservation.util.UiUtil;
import com.oasis.reservation.util.Validator;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.sql.SQLException;
import java.time.LocalDate;

/** Reservation form: validates input, saves the booking and shows a confirmation. */
public class BookingPanel extends JPanel {

    private final String username;
    private final TrainDAO trainDAO = new TrainDAO();
    private final ReservationDAO reservationDAO = new ReservationDAO();

    private final JTextField nameField = new JTextField(20);
    private final JTextField trainNoField = new JTextField(20);
    private final JTextField trainNameField = new JTextField(20);
    private final JComboBox<String> classBox = new JComboBox<>(new String[]{
            "Sleeper (SL)", "Third AC (3A)", "Second AC (2A)", "First AC (1A)", "Chair Car (CC)"});
    private final JTextField dateField = new JTextField(20);
    private final JTextField fromField = new JTextField(20);
    private final JTextField toField = new JTextField(20);

    public BookingPanel(String username) {
        this.username = username;
        setLayout(new GridBagLayout());
        setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));

        trainNameField.setEditable(false);

        addRow(0, "Passenger Name *", nameField);
        addRow(1, "Train Number *", trainNoField);
        addRow(2, "Train Name (auto)", trainNameField);
        addRow(3, "Class Type *", classBox);
        addRow(4, "Date of Journey * (dd-MM-yyyy)", dateField);
        addRow(5, "From (Source Station) *", fromField);
        addRow(6, "To (Destination Station) *", toField);

        JButton bookBtn = new JButton("Book Ticket");
        JButton clearBtn = new JButton("Clear");
        JPanel buttons = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        buttons.add(bookBtn);
        buttons.add(clearBtn);
        GridBagConstraints c = new GridBagConstraints();
        c.gridx = 0; c.gridy = 7; c.gridwidth = 2; c.insets = new Insets(18, 6, 6, 6);
        add(buttons, c);

        JLabel note = new JLabel("Available trains: 12627, 12301, 12951, 12009, 12723, 22691, 12839, 12615");
        note.setFont(note.getFont().deriveFont(11f));
        c.gridy = 8; c.insets = new Insets(6, 6, 6, 6);
        add(note, c);

        // Auto-fill the train name whenever the train number changes
        trainNoField.getDocument().addDocumentListener(new DocumentListener() {
            public void insertUpdate(DocumentEvent e) { updateTrainName(); }
            public void removeUpdate(DocumentEvent e) { updateTrainName(); }
            public void changedUpdate(DocumentEvent e) { updateTrainName(); }
        });

        bookBtn.addActionListener(e -> doBook());
        clearBtn.addActionListener(e -> clearForm());
    }

    private void addRow(int row, String label, Component field) {
        GridBagConstraints gl = new GridBagConstraints();
        gl.gridx = 0; gl.gridy = row; gl.anchor = GridBagConstraints.WEST;
        gl.insets = new Insets(6, 6, 6, 14);
        add(new JLabel(label), gl);

        GridBagConstraints gf = new GridBagConstraints();
        gf.gridx = 1; gf.gridy = row; gf.weightx = 1; gf.fill = GridBagConstraints.HORIZONTAL;
        gf.insets = new Insets(6, 0, 6, 6);
        add(field, gf);
    }

    private void updateTrainName() {
        String text = trainNoField.getText().trim();
        if (text.isEmpty()) {
            trainNameField.setText("");
        } else if (!Validator.isNumericTrainNo(text)) {
            trainNameField.setText("Invalid train number");
        } else {
            try {
                String name = trainDAO.findName(Integer.parseInt(text));
                trainNameField.setText(name != null ? name : "Train not found");
            } catch (SQLException ex) {
                trainNameField.setText("Database error");
            }
        }
    }

    private void doBook() {
        String name = nameField.getText().trim();
        String trainNoText = trainNoField.getText().trim();
        String dateText = dateField.getText().trim();
        String from = fromField.getText().trim();
        String to = toField.getText().trim();

        // ----- validation -----
        if (Validator.isBlank(name) || Validator.isBlank(trainNoText) || Validator.isBlank(dateText)
                || Validator.isBlank(from) || Validator.isBlank(to)) {
            warn("Please fill in all the required fields.");
            return;
        }
        if (!Validator.isNumericTrainNo(trainNoText)) {
            warn("Train number must contain digits only.");
            return;
        }
        LocalDate date = Validator.parseDate(dateText);
        if (date == null) {
            warn("Enter a valid date in dd-MM-yyyy format (for example 25-12-2026).");
            return;
        }
        if (date.isBefore(LocalDate.now())) {
            warn("Date of journey cannot be in the past.");
            return;
        }
        if (from.equalsIgnoreCase(to)) {
            warn("Source and destination stations must be different.");
            return;
        }

        // ----- save -----
        try {
            int trainNo = Integer.parseInt(trainNoText);
            String trainName = trainDAO.findName(trainNo);
            if (trainName == null) {
                warn("No train exists with number " + trainNo + ".");
                return;
            }

            Reservation r = new Reservation(username, name, trainNo, trainName,
                    (String) classBox.getSelectedItem(), date, from, to);
            reservationDAO.book(r);

            UiUtil.showText(this, "Booking Confirmed",
                    r.toDisplayString() + "\n\nPlease note your PNR. You need it to cancel the ticket.",
                    JOptionPane.INFORMATION_MESSAGE);
            clearForm();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Database error: " + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void warn(String message) {
        JOptionPane.showMessageDialog(this, message, "Validation Error", JOptionPane.WARNING_MESSAGE);
    }

    private void clearForm() {
        nameField.setText("");
        trainNoField.setText("");
        trainNameField.setText("");
        classBox.setSelectedIndex(0);
        dateField.setText("");
        fromField.setText("");
        toField.setText("");
    }
}
