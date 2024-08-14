import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.font.TextAttribute;
import java.sql.*;
import java.util.HashMap;
import java.util.Map;

public class Registrasi {
    private JPanel jpregis;
    private JTextField tfuser;
    private JTextField tfemail;
    private JTextField tfpass;
    private JTextField tfcpass;
    private JButton registrasiButton;
    private JLabel loginLabel;

    public Registrasi() {

        registrasiButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                registerUser();
            }
        });


        loginLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {

                Font originalFont = loginLabel.getFont();
                Map<TextAttribute, Object> attributes = new HashMap<>(originalFont.getAttributes());
                attributes.put(TextAttribute.UNDERLINE, TextAttribute.UNDERLINE_ON);
                loginLabel.setFont(originalFont.deriveFont(attributes));
                loginLabel.setForeground(Color.BLUE);
            }

            @Override
            public void mouseExited(MouseEvent e) {

                Font originalFont = loginLabel.getFont();
                Map<TextAttribute, Object> attributes = new HashMap<>(originalFont.getAttributes());
                attributes.put(TextAttribute.UNDERLINE, -1);
                loginLabel.setFont(originalFont.deriveFont(attributes));
                loginLabel.setForeground(UIManager.getColor("Label.foreground"));
            }

            @Override
            public void mouseClicked(MouseEvent e) {

                SwingUtilities.invokeLater(() -> {
                    JFrame loginFrame = new JFrame("Log in");

                    login loginForm = new login();
                    loginFrame.setContentPane(loginForm.getKapal());
                    loginFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                    loginFrame.pack();
                    loginFrame.setVisible(true);
                });

                // Menutup form registrasi
                JComponent component = (JComponent) e.getSource();
                Window window = SwingUtilities.getWindowAncestor(component);
                window.dispose();
            }
        });
    }

    private void registerUser() {
        String username = tfuser.getText();
        String email = tfemail.getText();
        String password = tfpass.getText();
        String confirmPassword = tfcpass.getText();

        if (username.isEmpty() || email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Silahkan lengkapi data nya", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (!password.equals(confirmPassword)) {
            JOptionPane.showMessageDialog(null, "Password nya tidak sama!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String url = "jdbc:mysql://localhost:3306/pemesanankapaltiket";
        String user = "root";
        String dbPassword = "";

        String query = "INSERT INTO login (username, email, password) VALUES (?, ?, ?)";

        try (Connection connection = DriverManager.getConnection(url, user, dbPassword);
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setString(1, username);
            statement.setString(2, email);
            statement.setString(3, password);

            int rowsInserted = statement.executeUpdate();
            if (rowsInserted > 0) {
                JOptionPane.showMessageDialog(null, "User registered successfully!");
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "Database error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Registrasi");
            frame.setContentPane(new Registrasi().jpregis);
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.pack();
            frame.setVisible(true);
        });
    }



    public Container jpregis() {
        return jpregis;
    }
}
