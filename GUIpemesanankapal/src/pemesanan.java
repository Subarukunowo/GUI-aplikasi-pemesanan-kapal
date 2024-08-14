import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import com.toedter.calendar.JDateChooser;

public class pemesanan {
    private JPanel pemesanan;
    private JTextField tfnama;
    private JComboBox<String> cbtujuan;
    private JComboBox<String> cbkelas;
    private JComboBox<String> cbjenis;
    private JComboBox<String> cbbayar;
    private JButton btnpesan;
    private JButton btnkembali;
    private JDateChooser dateChooser;
    private int selectedHarga = 0;

    public pemesanan() {
        pemesanan = new JPanel();
        pemesanan.setLayout(new GridLayout(7, 2));

        pemesanan.add(new JLabel("Nama Penumpang:"));
        tfnama = new JTextField();
        pemesanan.add(tfnama);

        pemesanan.add(new JLabel("Tujuan Keberangkatan:"));
        cbtujuan = new JComboBox<>(new String[]{"", "Sei Selari-Batam", "Batam-Sei Selari"});
        pemesanan.add(cbtujuan);

        pemesanan.add(new JLabel("Tanggal Keberangkatan:"));
        dateChooser = new JDateChooser();
        pemesanan.add(dateChooser);

        pemesanan.add(new JLabel("Kelas Penumpang:"));
        cbkelas = new JComboBox<>(new String[]{"", "VIP", "Reguler"});
        pemesanan.add(cbkelas);

        pemesanan.add(new JLabel("Jenis Kendaraan:"));
        cbjenis = new JComboBox<>(new String[]{"", "Golongan 1", "Golongan 2", "Golongan 3", "Golongan 4"});
        pemesanan.add(cbjenis);

        pemesanan.add(new JLabel("Metode Pembayaran:"));
        cbbayar = new JComboBox<>(new String[]{"", "Bank", "Dana", "OVO", "Gopay"});
        pemesanan.add(cbbayar);

        btnpesan = new JButton("Pesan");
        btnkembali = new JButton("Kembali");


        cbjenis.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    String selectedGolongan = (String) cbjenis.getSelectedItem();
                    if (selectedGolongan != null && !selectedGolongan.isEmpty()) {
                        updateHarga(selectedGolongan);
                    } else {
                        selectedHarga = 0;
                    }
                }
            }
        });

        btnpesan.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String nama = tfnama.getText();
                String tujuan = (String) cbtujuan.getSelectedItem();
                java.util.Date tanggal = dateChooser.getDate();
                String kelas = (String) cbkelas.getSelectedItem();
                String jenis = (String) cbjenis.getSelectedItem();
                String bayar = (String) cbbayar.getSelectedItem();

                if (nama.isEmpty() || tujuan.isEmpty() || tanggal == null || kelas.isEmpty() || jenis.isEmpty() || bayar.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Silakan lengkapi semua data.");
                } else {
                    // Konversi tanggal ke format SQL
                    java.sql.Date sqlDate = new java.sql.Date(tanggal.getTime());
                    // Masukkan data ke database
                    try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/pemesanankapaltiket", "root", "")) {
                        String query = "INSERT INTO pemesanan (nama_penumpang, tujuan, tanggal, kelas, jenis_kendaraan, metode_pembayaran) VALUES (?, ?, ?, ?, ?, ?)";
                        try (PreparedStatement stmt = conn.prepareStatement(query)) {
                            stmt.setString(1, nama);
                            stmt.setString(2, tujuan);
                            stmt.setDate(3, sqlDate);
                            stmt.setString(4, kelas);
                            stmt.setString(5, jenis);
                            stmt.setString(6, bayar);
                            stmt.executeUpdate();
                        }

                        int response = JOptionPane.showConfirmDialog(null, "Apakah Anda ingin membeli tiket kapal?", "Konfirmasi", JOptionPane.YES_NO_OPTION);
                        if (response == JOptionPane.YES_OPTION) {
                            JOptionPane.showMessageDialog(null, "Pemesanan tiket berhasil!");


                            MetodePembayaran.main(new String[]{String.valueOf(selectedHarga)});
                            JComponent component = (JComponent) e.getSource();
                            Window window = SwingUtilities.getWindowAncestor(component);
                            window.dispose();
                        } else {
                            // Kembali ke homepage
                            System.exit(0);
                        }
                    } catch (Exception ex) {
                        ex.printStackTrace();
                        JOptionPane.showMessageDialog(null, "Terjadi kesalahan: " + ex.getMessage());
                    }
                }
            }
        });

        pemesanan.add(btnpesan);
        pemesanan.add(btnkembali);
        btnkembali.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFrame menuUtamaFrame = new JFrame("Menu Utama");
                menuutama menuUtamaForm = new menuutama();
                menuUtamaFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                menuUtamaFrame.getContentPane().add(menuUtamaForm.getmenuutama());
                menuUtamaFrame.pack();
                menuUtamaFrame.setLocationRelativeTo(null);
                menuUtamaFrame.setVisible(true);


                JComponent component = (JComponent) btnkembali;
                Window window = SwingUtilities.getWindowAncestor(component);
                window.dispose();
            }
        });
    }

    private void updateHarga(String golongan) {
        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/pemesanankapaltiket", "root", "")) {
            String query = "SELECT harga FROM hargatiket WHERE golongan = ?";
            try (PreparedStatement stmt = conn.prepareStatement(query)) {
                stmt.setString(1, golongan);
                try (ResultSet rs = stmt.executeQuery()) {
                    if (rs.next()) {
                        selectedHarga = rs.getInt("harga");
                    } else {
                        selectedHarga = 0;
                    }
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "Terjadi kesalahan: " + ex.getMessage());
        }
    }

    public JPanel getpemesanan() {
        return pemesanan;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Menu Utama");
            pemesanan menuUtamaForm = new pemesanan();
            frame.setContentPane(menuUtamaForm.getpemesanan());
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.pack();
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
    }
}
