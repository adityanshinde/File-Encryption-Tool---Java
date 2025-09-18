package ui;


import encryption.EncryptionHandler;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.nio.file.Files;

public class EncryptionToolUI extends JFrame {
    private JTextField passphraseField;
    private JLabel statusLabel;
    private JProgressBar progressBar;
    private File selectedFile;

    public EncryptionToolUI() {
        setTitle("File Encryption Tool");
        setSize(480, 380);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        getContentPane().setBackground(Color.WHITE);
        setLayout(new BorderLayout());

        // Header
        JLabel headerLabel = new JLabel("File Encryption Tool", SwingConstants.CENTER);
        headerLabel.setFont(new Font("Segoe UI", Font.BOLD, 26));
        headerLabel.setForeground(new Color(33, 37, 41));
        headerLabel.setBorder(BorderFactory.createEmptyBorder(20, 0, 10, 0));
        add(headerLabel, BorderLayout.NORTH);

        // Main panel with BoxLayout
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBackground(Color.WHITE);
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 40, 10, 40));

        // Upload button
        JButton uploadButton = new JButton("Upload File");
        styleButton(uploadButton);
        uploadButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        uploadButton.addActionListener(evt -> chooseFile());
        mainPanel.add(uploadButton);

        mainPanel.add(Box.createRigidArea(new Dimension(0, 15)));

        // Passphrase field
        JLabel passLabel = new JLabel("Passphrase (min 16 chars):");
        passLabel.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        passLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        mainPanel.add(passLabel);

        passphraseField = new JTextField();
        passphraseField.setToolTipText("Enter a secure passphrase");
        passphraseField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 32));
        passphraseField.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        mainPanel.add(passphraseField);

        mainPanel.add(Box.createRigidArea(new Dimension(0, 15)));

        // Button panel
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.X_AXIS));
        buttonPanel.setBackground(Color.WHITE);
        buttonPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JButton encryptButton = new JButton("Encrypt");
        styleButton(encryptButton);
        encryptButton.addActionListener(evt -> processFile(true));
        buttonPanel.add(encryptButton);

        buttonPanel.add(Box.createRigidArea(new Dimension(20, 0)));

        JButton decryptButton = new JButton("Decrypt");
        styleButton(decryptButton);
        decryptButton.addActionListener(evt -> processFile(false));
        buttonPanel.add(decryptButton);

        mainPanel.add(buttonPanel);

        mainPanel.add(Box.createRigidArea(new Dimension(0, 20)));

        // Status label
        statusLabel = new JLabel("Status: Waiting for action", SwingConstants.CENTER);
        statusLabel.setFont(new Font("Segoe UI", Font.ITALIC, 14));
        statusLabel.setForeground(new Color(80, 80, 80));
        statusLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        mainPanel.add(statusLabel);

        // Progress bar
        progressBar = new JProgressBar();
        progressBar.setIndeterminate(false);
        progressBar.setStringPainted(true);
        progressBar.setVisible(false);
        progressBar.setAlignmentX(Component.CENTER_ALIGNMENT);
        progressBar.setPreferredSize(new Dimension(200, 22));
        mainPanel.add(progressBar);

        add(mainPanel, BorderLayout.CENTER);

        setVisible(true);
    }

    private void styleButton(JButton button) {
        button.setBackground(new Color(66, 133, 244));
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setFont(new Font("Segoe UI", Font.BOLD, 15));
        button.setBorder(BorderFactory.createEmptyBorder(8, 18, 8, 18));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
    }

    private void chooseFile() {
        JFileChooser fileChooser = new JFileChooser();
        if (fileChooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            selectedFile = fileChooser.getSelectedFile();
            statusLabel.setText("Selected file: " + selectedFile.getName());
        }
    }

    private void processFile(boolean encrypt) {
        if (selectedFile == null) {
            statusLabel.setText("Error: No file selected");
            return;
        }
        if (!selectedFile.exists() || !selectedFile.isFile() || !selectedFile.canRead()) {
            statusLabel.setText("Error: Selected file is invalid or unreadable");
            return;
        }
        String passphrase = passphraseField.getText();
        if (passphrase == null || passphrase.trim().isEmpty()) {
            statusLabel.setText("Error: Passphrase is required");
            return;
        }
        if (passphrase.length() < 16) {
            statusLabel.setText("Error: Passphrase must be at least 16 characters");
            return;
        }

        progressBar.setVisible(true);
        progressBar.setIndeterminate(true);
        progressBar.setString("Processing...");
        statusLabel.setText("Processing file, please wait...");

        SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {
            @Override
            protected Void doInBackground() {
                try {
                    byte[] fileData = Files.readAllBytes(selectedFile.toPath());
                    byte[] processedData;

                    if (encrypt) {
                        processedData = EncryptionHandler.encrypt(fileData, passphrase);
                        if (processedData != null) {
                            saveFile(processedData, "encrypted_" + selectedFile.getName());
                            statusLabel.setText("File encrypted successfully!");
                        } else {
                            statusLabel.setText("Encryption failed. Check logs.");
                        }
                    } else {
                        processedData = EncryptionHandler.decrypt(fileData, passphrase);
                        if (processedData != null) {
                            saveFile(processedData, "decrypted_" + selectedFile.getName());
                            statusLabel.setText("File decrypted successfully!");
                        } else {
                            statusLabel.setText("Decryption failed. Check logs.");
                        }
                    }
                } catch (EncryptionHandler.EncryptionException ex) {
                    statusLabel.setText("Encryption error: " + ex.getMessage());
                } catch (Exception ex) {
                    statusLabel.setText("Error: " + ex.getMessage());
                }
                return null;
            }

            @Override
            protected void done() {
                progressBar.setIndeterminate(false);
                progressBar.setVisible(false);
            }
        };
        worker.execute();
    }

    private void saveFile(byte[] data, String fileName) throws IOException {
        File outputFile = new File(selectedFile.getParent(), fileName);
        if (outputFile.exists()) {
            int result = JOptionPane.showConfirmDialog(this,
                    "The file '" + fileName + "' already exists. Overwrite?",
                    "File Exists",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.WARNING_MESSAGE);
            if (result != JOptionPane.YES_OPTION) {
                statusLabel.setText("Save cancelled: File not overwritten.");
                return;
            }
        }
        try (FileOutputStream fos = new FileOutputStream(outputFile)) {
            fos.write(data);
        }
    }

    public static void main(String[] args) {
        new EncryptionToolUI();
    }
}
