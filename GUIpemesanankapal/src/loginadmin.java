import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class loginadmin {

    private JPanel admin;
    private JButton btlogin;
    private JTextField tfusername;
    private JPasswordField pfpass;

    public loginadmin() {


        btlogin.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                prosesLogin();
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
            String sql = "SELECT * FROM login_admin WHERE nama_Admin = ? AND password = ?";
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setString(1, username);
                statement.setString(2, password);
                ResultSet resultSet = statement.executeQuery();

                if (resultSet.next()) {
                    JOptionPane.showMessageDialog(admin, "Login berhasil!");
                    bukaMenuUtama();
                    // Tutup frame login saat ini
                    closeCurrentWindow();
                } else {
                    JOptionPane.showMessageDialog(admin, "Login gagal. Periksa kembali username dan password Anda.");
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    private void bukaMenuUtama() {
        SwingUtilities.invokeLater(() -> {
            JFrame menuUtamaFrame = new JFrame("Menu Utama Admin");
            menuutamaadmin menuUtamaForm = new menuutamaadmin();
            menuUtamaFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            menuUtamaFrame.getContentPane().add(menuUtamaForm.getadmin());
            menuUtamaFrame.pack();
            menuUtamaFrame.setLocationRelativeTo(null);
            menuUtamaFrame.setVisible(true);
        });
    }

    private void closeCurrentWindow() {
        JFrame loginFrame = (JFrame) SwingUtilities.getWindowAncestor(admin);
        loginFrame.dispose();
    }

    public JPanel getAdminPanel() {
        return admin;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Login Admin");
            loginadmin loginForm = new loginadmin();
            frame.setContentPane(loginForm.getAdminPanel());
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.pack();
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
    }
}
