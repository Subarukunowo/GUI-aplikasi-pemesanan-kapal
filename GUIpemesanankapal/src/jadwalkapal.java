import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class jadwalkapal {
    private JPanel jadwal;
    private JTextField tfcari;
    private JButton kembaliButton;
    private JButton cariButton;
    private JTable jadwaltable;

    public jadwalkapal() {
        // Create the panel with a BorderLayout
        jadwal = new JPanel(new BorderLayout());

        // Initialize other components
        tfcari = new JTextField(20);
        kembaliButton = new JButton("Kembali");
        cariButton = new JButton("Cari");

        // Create table with column headers
        String[] columnNames = {"Nama Kapal", "Tujuan", "Tanggal Berangkat", "Jam Berangkat"};
        DefaultTableModel model = new DefaultTableModel(columnNames, 0);
        jadwaltable = new JTable(model);

        // Add table to scroll pane
        JScrollPane scrollPane = new JScrollPane(jadwaltable);
        jadwal.add(scrollPane, BorderLayout.CENTER);

        // Create a panel for search field and buttons
        JPanel searchPanel = new JPanel();
        searchPanel.add(new JLabel("Cari:"));
        searchPanel.add(tfcari);
        searchPanel.add(cariButton);
        searchPanel.add(kembaliButton);

        jadwal.add(searchPanel, BorderLayout.NORTH);

        // Load all schedules initially
        loadAllJadwal();

        cariButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                carijadwal();
            }
        });

        kembaliButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                menuutama.main(new String[0]);
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

    private void loadAllJadwal() {
        String sql = "SELECT * FROM jadwal_keberangkatan";

        try (Connection conn = buatKoneksi();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            DefaultTableModel model = (DefaultTableModel) jadwaltable.getModel();
            while (rs.next()) {
                String namaKapal = rs.getString("nama_kapal");
                String tujuan = rs.getString("tujuan");
                Date tanggalBerangkat = rs.getDate("tanggal_berangkat");
                String jamBerangkat = rs.getString("jam_berangkat");
                model.addRow(new Object[]{namaKapal, tujuan, tanggalBerangkat, jamBerangkat});
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error retrieving data: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void carijadwal() {
        String searchQuery = tfcari.getText().trim();
        String sql = "SELECT * FROM jadwal_keberangkatan WHERE nama_kapal LIKE ?";

        try (Connection conn = buatKoneksi();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, "%" + searchQuery + "%");

            ResultSet rs = pstmt.executeQuery();

            DefaultTableModel model = (DefaultTableModel) jadwaltable.getModel();
            model.setRowCount(0); // Clear existing data
            while (rs.next()) {
                String namaKapal = rs.getString("nama_kapal");
                String tujuan = rs.getString("tujuan");
                Date tanggalBerangkat = rs.getDate("tanggal_berangkat");
                String jamBerangkat = rs.getString("jam_berangkat");
                model.addRow(new Object[]{namaKapal, tujuan, tanggalBerangkat, jamBerangkat});
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error retrieving data: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("jadwalkapal");
        frame.setContentPane(new jadwalkapal().jadwal);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }
}
