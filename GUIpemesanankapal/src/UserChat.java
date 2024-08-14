import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class UserChat extends JFrame {

    private JPanel userChatPanel;
    private JPanel adminChatPanel;
    private JTextField userInputField;
    private JTextField adminInputField;
    private JButton userSendButton;
    private JButton adminSendButton;
    private JButton userImageButton;
    private JButton adminImageButton;
    private ImageIcon userProfileImage;
    private ImageIcon adminProfileImage;


    private static final String DB_URL = "jdbc:mysql://localhost:3306/pemesanankapaltiket";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "";

    public UserChat() {
        setTitle("Admin Chat");

        setSize(700, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        setLayout(new BorderLayout());

        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);


        JPanel userPanel = new JPanel();
        userPanel.setLayout(new BorderLayout());
        userChatPanel = new JPanel();
        userChatPanel.setLayout(new BoxLayout(userChatPanel, BoxLayout.Y_AXIS));
        JScrollPane userChatScrollPane = new JScrollPane(userChatPanel);

        JPanel userInputPanel = new JPanel();
        userInputPanel.setLayout(new BorderLayout());

        userInputField = new JTextField();
        userSendButton = new JButton("Send");
        userImageButton = new JButton("Image");

        userInputPanel.add(userInputField, BorderLayout.CENTER);
        userInputPanel.add(userSendButton, BorderLayout.EAST);
        userInputPanel.add(userImageButton, BorderLayout.WEST);

        userPanel.add(userChatScrollPane, BorderLayout.CENTER);
        userPanel.add(userInputPanel, BorderLayout.SOUTH);

        // Panel admin
        JPanel adminPanel = new JPanel();
        adminPanel.setLayout(new BorderLayout());
        adminChatPanel = new JPanel();
        adminChatPanel.setLayout(new BoxLayout(adminChatPanel, BoxLayout.Y_AXIS));
        JScrollPane adminChatScrollPane = new JScrollPane(adminChatPanel);

        JPanel adminInputPanel = new JPanel();
        adminInputPanel.setLayout(new BorderLayout());

        adminInputField = new JTextField();
        adminSendButton = new JButton("Send");
        adminImageButton = new JButton("Image");

        adminInputPanel.add(adminInputField, BorderLayout.CENTER);
        adminInputPanel.add(adminSendButton, BorderLayout.EAST);
        adminInputPanel.add(adminImageButton, BorderLayout.WEST);

        adminPanel.add(adminChatScrollPane, BorderLayout.CENTER);
        adminPanel.add(adminInputPanel, BorderLayout.SOUTH);

        splitPane.setLeftComponent(userPanel);
        splitPane.setRightComponent(adminPanel);

        add(splitPane, BorderLayout.CENTER);

        // Mengatur gambar profil pengguna
        userProfileImage = new ImageIcon("path/to/user_profile.jpg");
        Image userImage = userProfileImage.getImage().getScaledInstance(50, 50, Image.SCALE_SMOOTH);
        userProfileImage = new ImageIcon(userImage);

        // Mengatur gambar profil admin
        adminProfileImage = new ImageIcon("path/to/admin_profile.jpg");
        Image adminImage = adminProfileImage.getImage().getScaledInstance(50, 50, Image.SCALE_SMOOTH);
        adminProfileImage = new ImageIcon(adminImage);

        // Aksi tombol kirim pengguna
        userSendButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                sendUserMessage();
            }
        });

        // Aksi tombol kirim admin
        adminSendButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                sendAdminMessage();
            }
        });

        // Aksi tombol gambar pengguna
        userImageButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                sendUserImage();
            }
        });

        // Aksi tombol gambar admin
        adminImageButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                sendAdminImage();
            }
        });

        // Aksi input field pengguna (Enter key)
        userInputField.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                sendUserMessage();
            }
        });

        // Aksi input field admin (Enter key)
        adminInputField.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                sendAdminMessage();
            }
        });
    }

    private void sendUserMessage() {
        String message = userInputField.getText().trim();
        if (!message.isEmpty()) {
            addBubble(userChatPanel, message, BubbleType.SENT, userProfileImage);
            addBubble(adminChatPanel, message, BubbleType.RECEIVED, userProfileImage);
            saveMessageToDatabase("user", message, null); // Simpan pesan ke database
            userInputField.setText("");
        }
    }

    private void sendAdminMessage() {
        String message = adminInputField.getText().trim();
        if (!message.isEmpty()) {
            addBubble(adminChatPanel, message, BubbleType.SENT, adminProfileImage);
            addBubble(userChatPanel, message, BubbleType.RECEIVED, adminProfileImage);
            saveMessageToDatabase("admin", message, null); // Simpan pesan ke database
            adminInputField.setText("");
        }
    }

    private byte[] resizeImage(byte[] originalImage, int targetWidth, int targetHeight) throws IOException {
        ByteArrayInputStream bis = new ByteArrayInputStream(originalImage);
        BufferedImage image = ImageIO.read(bis);
        Image scaledImage = image.getScaledInstance(targetWidth, targetHeight, Image.SCALE_SMOOTH);
        BufferedImage bufferedScaledImage = new BufferedImage(targetWidth, targetHeight, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = bufferedScaledImage.createGraphics();
        g2d.drawImage(scaledImage, 0, 0, null);
        g2d.dispose();
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ImageIO.write(bufferedScaledImage, "png", bos);
        return bos.toByteArray();
    }

    private void sendUserImage() {
        JFileChooser fileChooser = new JFileChooser();
        int returnValue = fileChooser.showOpenDialog(null);
        if (returnValue == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            try {
                Path path = Paths.get(selectedFile.getAbsolutePath());
                byte[] imageData = Files.readAllBytes(path); // Read image file into byte array
                imageData = resizeImage(imageData, 100, 100); // Resize image
                ImageIcon imageIcon = new ImageIcon(imageData);
                Image image = imageIcon.getImage().getScaledInstance(100, 100, Image.SCALE_SMOOTH);
                imageIcon = new ImageIcon(image);
                addBubble(userChatPanel, imageIcon, BubbleType.SENT, userProfileImage);
                addBubble(adminChatPanel, imageIcon, BubbleType.RECEIVED, userProfileImage);
                saveMessageToDatabase("user", null, imageData); // Save image to database
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void sendAdminImage() {
        JFileChooser fileChooser = new JFileChooser();
        int returnValue = fileChooser.showOpenDialog(null);
        if (returnValue == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            try {
                Path path = Paths.get(selectedFile.getAbsolutePath());
                byte[] imageData = Files.readAllBytes(path);
                ImageIcon imageIcon = new ImageIcon(imageData);
                Image image = imageIcon.getImage().getScaledInstance(100, 100, Image.SCALE_SMOOTH);
                imageIcon = new ImageIcon(image);
                addBubble(adminChatPanel, imageIcon, BubbleType.SENT, adminProfileImage);
                addBubble(userChatPanel, imageIcon, BubbleType.RECEIVED, adminProfileImage);
                saveMessageToDatabase("admin", null, imageData); // Simpan gambar ke database
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void addBubble(JPanel panel, String message, BubbleType type, ImageIcon profileImage) {
        JPanel bubble = new JPanel();
        bubble.setLayout(new BoxLayout(bubble, BoxLayout.X_AXIS));

        JLabel profileLabel = new JLabel(profileImage);
        JLabel messageLabel = new JLabel(message);
        messageLabel.setOpaque(true);
        messageLabel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        if (type == BubbleType.SENT) {
            messageLabel.setBackground(new Color(143, 206, 250));
            bubble.add(Box.createHorizontalGlue());
            bubble.add(messageLabel);
            bubble.add(profileLabel);
        } else {
            messageLabel.setBackground(new Color(113, 253, 113));
            bubble.add(profileLabel);
            bubble.add(messageLabel);
            bubble.add(Box.createHorizontalGlue());
        }

        panel.add(bubble);
        panel.revalidate();
        panel.repaint();
    }

    private void addBubble(JPanel panel, ImageIcon imageIcon, BubbleType type, ImageIcon profileImage) {
        JPanel bubble = new JPanel();
        bubble.setLayout(new BoxLayout(bubble, BoxLayout.X_AXIS));

        JLabel profileLabel = new JLabel(profileImage);
        JLabel imageLabel = new JLabel(imageIcon);
        imageLabel.setOpaque(true);
        imageLabel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        if (type == BubbleType.SENT) {
            bubble.add(Box.createHorizontalGlue());
            bubble.add(imageLabel);
            bubble.add(profileLabel);
        } else {
            bubble.add(profileLabel);
            bubble.add(imageLabel);
            bubble.add(Box.createHorizontalGlue());
        }

        panel.add(bubble);
        panel.revalidate();
        panel.repaint();
    }

    private void saveMessageToDatabase(String sender, String message, byte[] imageBytes) {
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
            String sql = "INSERT INTO messages (sender, message, image) VALUES (?, ?, ?)";
            PreparedStatement statement = conn.prepareStatement(sql);

            statement.setString(1, sender);
            statement.setString(2, message);

            if (imageBytes != null) {
                statement.setBytes(3, imageBytes);
            } else {
                statement.setNull(3, java.sql.Types.BLOB);
            }

            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error saving message to database: " + e.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new UserChat().setVisible(true);
            }
        });
    }

    private enum BubbleType {
        SENT, RECEIVED
    }
}
