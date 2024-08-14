import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class menuutama {
    private JPanel menuutama;
    private JButton jadwalKapalButton;
    private JButton pemesananButton;
    private JButton profilButton;
    private JButton logOutButton;
    private JButton riwayatButton;
    private JButton customerServiceButton;
    private JButton aboutMeButton;

    public menuutama() {
        jadwalKapalButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                jadwalkapal.main(new String[0]);
                closeCurrentWindow(e);
            }
        });
        pemesananButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                pemesanan.main(new String[0]);
                closeCurrentWindow(e);
            }
        });
        profilButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                KelolaProfil.main(new String[0]);
                closeCurrentWindow(e);
            }
        });
        logOutButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                login.main(new String[0]);
                closeCurrentWindow(e);
            }
        });
        riwayatButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Riwayat.main(new String[0]);
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
        aboutMeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                about.main(new String[0]);
                closeCurrentWindow(e);
            }
        });
    }

    public JPanel getmenuutama() {
        return menuutama;
    }

    private void closeCurrentWindow(ActionEvent e) {
        JComponent component = (JComponent) e.getSource();
        Window window = SwingUtilities.getWindowAncestor(component);
        window.dispose();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Menu Utama");
            menuutama menuutama = new menuutama();
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.getContentPane().add(menuutama.getmenuutama());
            frame.pack();
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
    }
}
