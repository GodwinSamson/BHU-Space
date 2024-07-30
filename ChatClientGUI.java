import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ChatClientGUI extends JFrame {
    private JTextArea messageArea;
    private JTextField textField;
    private JButton exitButton; // Declare the exit button
    private JButton saveButton; // Declare the save button
    private ChatClient client;
    private String name;

    public ChatClientGUI() {
        super("Chat Application");
        setSize(400, 500);
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE); // Set default close operation to DO_NOTHING_ON_CLOSE

        // Styling variables
        Color backgroundColor = new Color(240, 240, 240); // Light gray background
        Color buttonColor = new Color(75, 75, 75); // Darker gray for buttons
        Color textColor = new Color(50, 50, 50); // Almost black for text
        Font textFont = new Font("Arial", Font.PLAIN, 14);
        Font buttonFont = new Font("Arial", Font.BOLD, 12);

        // Apply styles to the message area
        messageArea = new JTextArea();
        messageArea.setEditable(false);
        messageArea.setBackground(backgroundColor);
        messageArea.setForeground(textColor);
        messageArea.setFont(textFont);
        JScrollPane scrollPane = new JScrollPane(messageArea);
        add(scrollPane, BorderLayout.CENTER);

        // Prompt for user name
        name = JOptionPane.showInputDialog(this, "Enter your name:", "Name Entry", JOptionPane.PLAIN_MESSAGE);
        // Update the window title to include user's name
        this.setTitle("Chat Application - " + name);

        // Apply styles to the text field
        textField = new JTextField();
        textField.setFont(textFont);
        textField.setForeground(textColor);
        textField.setBackground(backgroundColor);
        textField.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String message = "[" + new SimpleDateFormat("HH:mm:ss").format(new Date()) + "] " + name + ": "
                        + textField.getText();
                client.sendMessage(message);
                textField.setText("");
            }
        });

        // Apply styles to the save button and initialize it
        saveButton = new JButton("Save");
        saveButton.setFont(buttonFont);
        saveButton.setBackground(buttonColor);
        saveButton.setForeground(Color.WHITE);
        saveButton.addActionListener(e -> saveChatHistoryToFile());

        // Apply styles to the exit button and initialize it
        exitButton = new JButton("Exit");
        exitButton.setFont(buttonFont);
        exitButton.setBackground(buttonColor);
        exitButton.setForeground(Color.WHITE);
        exitButton.addActionListener(e -> {
            // Send a departure message to the server
            String departureMessage = name + " has left the chat.";
            client.sendMessage(departureMessage);

            // Delay to ensure the message is sent before exiting
            try {
                Thread.sleep(1000); // Wait for 1 second to ensure message is sent
            } catch (InterruptedException ie) {
                Thread.currentThread().interrupt();
            }

            // Exit the application
            System.exit(0);
        });

        // Creating a bottom panel to hold the text field, save button, and exit button
        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.setBackground(backgroundColor); // Apply background color to the panel
        JPanel buttonPanel = new JPanel(); // Panel for buttons
        buttonPanel.setBackground(backgroundColor);
        buttonPanel.add(saveButton);
        buttonPanel.add(exitButton);
        bottomPanel.add(textField, BorderLayout.CENTER);
        bottomPanel.add(buttonPanel, BorderLayout.EAST); // Add the button panel to the bottom panel
        add(bottomPanel, BorderLayout.SOUTH); // Add the bottom panel to the frame

        // Initialize and start the ChatClient
        try {
            this.client = new ChatClient("127.0.0.1", 5000, this::onMessageReceived);
            client.startClient();
        } catch (IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error connecting to the server", "Connection error",
                    JOptionPane.ERROR_MESSAGE);
            System.exit(1);
        }

        // Add window listener to handle window closing event
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                saveChatHistoryToFile();
                // Send a departure message to the server
                String departureMessage = name + " has left the chat.";
                client.sendMessage(departureMessage);

                // Delay to ensure the message is sent before exiting
                try {
                    Thread.sleep(1000); // Wait for 1 second to ensure message is sent
                } catch (InterruptedException ie) {
                    Thread.currentThread().interrupt();
                }

                // Exit the application
                System.exit(0);
            }
        });
    }

    private void saveChatHistoryToFile() {
        try (FileWriter fileWriter = new FileWriter("chat_history.txt");
             PrintWriter printWriter = new PrintWriter(fileWriter)) {
            printWriter.print(messageArea.getText());
            JOptionPane.showMessageDialog(this, "Chat history saved to chat_history.txt", "Save Successful",
                    JOptionPane.INFORMATION_MESSAGE);
        } catch (IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error saving chat history", "Save Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void onMessageReceived(String message) {
        // Use SwingUtilities.invokeLater to ensure thread safety when updating the GUI
        SwingUtilities.invokeLater(() -> messageArea.append(message + "\n"));
    }

    public static void main(String[] args) {
        // Ensure the GUI is created and updated on the Event Dispatch Thread
        SwingUtilities.invokeLater(() -> {
            new ChatClientGUI().setVisible(true);
        });
    }
}
