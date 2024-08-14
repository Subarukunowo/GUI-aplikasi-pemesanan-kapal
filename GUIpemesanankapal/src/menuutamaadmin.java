import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class menuutamaadmin {
    private JPanel menuadmin;
    private JButton lihatJadwalButton;
    private JButton editJadwalButton;
    private JButton customerServiceButton;
    private JButton logOutButton;

    public menuutamaadmin() {
        // Initialize JPanel and components
        menuadmin = new JPanel(new GridLayout(4, 1));

        lihatJadwalButton = new JButton("Lihat Jadwal");
        editJadwalButton = new JButton("Edit Jadwal");
        customerServiceButton = new JButton("Customer Service");
        logOutButton = new JButton("Log Out");

        menuadmin.add(lihatJadwalButton);
        menuadmin.add(editJadwalButton);
        menuadmin.add(customerServiceButton);
        menuadmin.add(logOutButton);

        lihatJadwalButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                jadwalkapal.main(new String[0]);
                closeCurrentWindow(e);
            }
        });

        editJadwalButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                editjadwal.main(new String[0]);
                closeCurrentWindow(e);
            }
        });

        customerServiceButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                UserChat.main(new String[0]);
                closeCurrentWindow(e);
            }
        });

        logOutButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Implementasi logout, kembali ke halaman login
                JFrame loginFrame = new JFrame("Login Admin");
                loginadmin loginForm = new loginadmin();
                loginFrame.setContentPane(loginForm.getAdminPanel());
                loginFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                loginFrame.pack();
                loginFrame.setLocationRelativeTo(null);
                loginFrame.setVisible(true);

                closeCurrentWindow(e);
            }
        });
    }

    public JPanel getadmin() {
        return menuadmin;
    }

    private void closeCurrentWindow(ActionEvent e) {
        JComponent component = (JComponent) e.getSource();
        Window window = SwingUtilities.getWindowAncestor(component);
        window.dispose();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Menu Utama Admin");
            menuutamaadmin menuadmin = new menuutamaadmin();
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.getContentPane().add(menuadmin.getadmin());
            frame.pack();
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
    }
}
