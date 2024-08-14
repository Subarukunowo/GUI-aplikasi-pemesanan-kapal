import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.font.TextAttribute;
import java.sql.*;
import java.util.HashMap;
import java.util.Map;

public class login {
    private JPanel kapal;
    private JTextField tfusername;
    private JPasswordField pfpass;
    private JButton btlogin;
    private JLabel regislabel;

    public login() {
        btlogin.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                prosesLogin();
            }
        });
        regislabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {

                Font originalFont = regislabel.getFont();
                Map<TextAttribute, Object> attributes = new HashMap<>(originalFont.getAttributes());
                attributes.put(TextAttribute.UNDERLINE, TextAttribute.UNDERLINE_ON);
                regislabel.setFont(originalFont.deriveFont(attributes));
                regislabel.setForeground(Color.BLUE);
            }

            @Override
            public void mouseExited(MouseEvent e) {

                Font originalFont = regislabel.getFont();
                Map<TextAttribute, Object> attributes = new HashMap<>(originalFont.getAttributes());
                attributes.put(TextAttribute.UNDERLINE, -1);
               regislabel.setFont(originalFont.deriveFont(attributes));
                regislabel.setForeground(UIManager.getColor("Label.foreground"));
            }

            @Override
            public void mouseClicked(MouseEvent e) {

                SwingUtilities.invokeLater(() -> {
                    JFrame loginFrame = new JFrame("Log in");

                    Registrasi loginForm = new Registrasi();
                    loginFrame.setContentPane(loginForm.jpregis());
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


    private Connection buatKoneksi() throws SQLException {
        String url = "jdbc:mysql://localhost:3306/pemesanankapaltiket";
        String username = "root";
        String password = "";
        return DriverManager.getConnection(url, username, password);
    }

    private void prosesLogin() {
        try (Connection connection = buatKoneksi()) {
            String username = tfusername.getText();
            String password = new String(pfpass.getPassword());
            String sql = "SELECT * FROM login WHERE username = ? AND password = ?";
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setString(1, username);
                statement.setString(2, password);
                ResultSet resultSet = statement.executeQuery();

                if (resultSet.next()) {
                    JOptionPane.showMessageDialog(kapal, "Login berhasil!");
                    bukaMenuUtama();
                    // Tutup frame login saat ini
                    closeCurrentWindow();
                } else {
                    JOptionPane.showMessageDialog(kapal, "Login gagal. Periksa kembali username dan password Anda.");
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    private void bukaMenuUtama() {
        SwingUtilities.invokeLater(() -> {
            JFrame menuUtamaFrame = new JFrame("Menu Utama");
            menuutama menuUtamaForm = new menuutama();
            menuUtamaFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            menuUtamaFrame.getContentPane().add(menuUtamaForm   .getmenuutama());
            menuUtamaFrame.pack();
            menuUtamaFrame.setLocationRelativeTo(null);
            menuUtamaFrame.setVisible(true);
        });
    }

    private void closeCurrentWindow() {
        Window window = SwingUtilities.getWindowAncestor(kapal);
        window.dispose();
    }

    public JPanel getKapal() {
        return kapal;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Login");
            login loginForm = new login();
            frame.setContentPane(loginForm.getKapal());
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.pack();
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
    }
}
