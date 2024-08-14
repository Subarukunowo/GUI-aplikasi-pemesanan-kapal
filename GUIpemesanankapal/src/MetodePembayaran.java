import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MetodePembayaran {
    private JPanel panel1;
    private JPanel jpbayar;
    private JLabel harga;
    private JButton btkonfirmasi;
    private JButton btkembali;
    private JComboBox<String> comboBox1;
    private JTextField tfnorek;

    public MetodePembayaran(int hargaTiket) {

        harga.setText("RP " + hargaTiket + "");


        btkembali.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Kembali ke menu utama
                JFrame menuUtamaFrame = new JFrame("Menu Utama");
                pemesanan menuUtamaForm = new pemesanan();
                menuUtamaFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                menuUtamaFrame.getContentPane().add(menuUtamaForm.getpemesanan());
                menuUtamaFrame.pack();
                menuUtamaFrame.setLocationRelativeTo(null);
                menuUtamaFrame.setVisible(true);

                // Tutup frame pembayaran saat ini
                JComponent component = (JComponent) e.getSource();
                Window window = SwingUtilities.getWindowAncestor(component);
                window.dispose();
            }
        });

        // Add action listener for konfirmasi button
        btkonfirmasi.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String bank = (String) comboBox1.getSelectedItem();
                String noRek = tfnorek.getText();

                if (bank.isEmpty() || noRek.isEmpty()) {
                    JOptionPane.showMessageDialog(panel1, "Silakan lengkapi data pembayaran.");
                } else {
                    JOptionPane.showMessageDialog(panel1, "Pembayaran berhasil. Tiket telah dipesan.");
                    // Lanjut ke menu utama

                    JFrame menuUtamaFrame = new JFrame("Menu Utama");
                    pemesanan menuUtamaForm = new pemesanan();
                    menuUtamaFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                    menuUtamaFrame.getContentPane().add(menuUtamaForm.getpemesanan());
                    menuUtamaFrame.pack();
                    menuUtamaFrame.setLocationRelativeTo(null);
                    menuUtamaFrame.setVisible(true);

                    // Tutup frame pembayaran saat ini
                    JComponent component = (JComponent) e.getSource();
                    Window window = SwingUtilities.getWindowAncestor(component);
                    window.dispose();
                }
            }
        });
    }

    public JPanel getPanel1() {
        return panel1;
    }

    public static void main(String[] args) {
        int hargaTiket = Integer.parseInt(args[0]);
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Metode Pembayaran");
            MetodePembayaran pembayaranForm = new MetodePembayaran(hargaTiket);
            frame.setContentPane(pembayaranForm.getPanel1());
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.pack();
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
    }

}
