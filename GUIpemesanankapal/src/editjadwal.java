import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class editjadwal {
    private JPanel jadwal;
    private JTextField tfcari;
    private JButton kembaliButton;
    private JButton cariButton;
    private JButton editButton;
    private JButton saveButton;
    private JTable jadwaltable;
    private DefaultTableModel tableModel;
    private List<Object[]> originalData;

    public editjadwal() {
        jadwal = new JPanel(new BorderLayout());

        JPanel topPanel = new JPanel();
        topPanel.add(new JLabel("Edit Data Kapal :"));
        tfcari = new JTextField(20);
        topPanel.add(tfcari);
        cariButton = new JButton("Cari");
        topPanel.add(cariButton);
        editButton = new JButton("Edit");
        topPanel.add(editButton);
        kembaliButton = new JButton("Kembali");
        topPanel.add(kembaliButton);
        saveButton = new JButton("Save");
        topPanel.add(saveButton);

        jadwal.add(topPanel, BorderLayout.NORTH);

        jadwaltable = new JTable();
        JScrollPane tableScrollPane = new JScrollPane(jadwaltable);
        jadwal.add(tableScrollPane, BorderLayout.CENTER);

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
                JFrame menuUtamaFrame = new JFrame("Menu Utama");
                menuutama menuUtamaForm = new menuutama();
                menuUtamaFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                menuUtamaFrame.getContentPane().add(menuUtamaForm.getmenuutama());
                menuUtamaFrame.pack();
                menuUtamaFrame.setLocationRelativeTo(null);
                menuUtamaFrame.setVisible(true);

                JComponent component = (JComponent) kembaliButton;
                Window window = SwingUtilities.getWindowAncestor(component);
                window.dispose();
            }
        });

        editButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                editSelectedJadwal();
            }
        });

        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                saveEditedJadwal();
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

            tableModel = new DefaultTableModel(new String[]{"ID", "Nama Kapal", "Tujuan", "Tanggal Berangkat", "Jam Berangkat"}, 0);
            originalData = new ArrayList<>();
            while (rs.next()) {
                int id = rs.getInt("id");
                String namaKapal = rs.getString("nama_kapal");
                String tujuan = rs.getString("tujuan");
                Date tanggalBerangkat = rs.getDate("tanggal_berangkat");
                String jamBerangkat = rs.getString("jam_berangkat");
                tableModel.addRow(new Object[]{id, namaKapal, tujuan, tanggalBerangkat, jamBerangkat});
                originalData.add(new Object[]{id, namaKapal, tujuan, tanggalBerangkat, jamBerangkat});
            }
            jadwaltable.setModel(tableModel);
            jadwaltable.removeColumn(jadwaltable.getColumnModel().getColumn(0));
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error retrieving data: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void carijadwal() {
        String searchQuery = tfcari.getText().trim();
        String sql = "SELECT * FROM jadwal_keberangkatan WHERE nama_kapal LIKE ? OR tujuan LIKE ? OR tanggal_berangkat = ?";

        try (Connection conn = buatKoneksi();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, "%" + searchQuery + "%");
            pstmt.setString(2, "%" + searchQuery + "%");
            try {
                Date date = Date.valueOf(searchQuery);
                pstmt.setDate(3, date);
            } catch (IllegalArgumentException e) {
                pstmt.setDate(3, null);
            }

            ResultSet rs = pstmt.executeQuery();

            tableModel = new DefaultTableModel(new String[]{"ID", "Nama Kapal", "Tujuan", "Tanggal Berangkat", "Jam Berangkat"}, 0);
            originalData = new ArrayList<>();
            while (rs.next()) {
                int id = rs.getInt("id");
                String namaKapal = rs.getString("nama_kapal");
                String tujuan = rs.getString("tujuan");
                Date tanggalBerangkat = rs.getDate("tanggal_berangkat");
                String jamBerangkat = rs.getString("jam_berangkat");
                tableModel.addRow(new Object[]{id, namaKapal, tujuan, tanggalBerangkat, jamBerangkat});
                originalData.add(new Object[]{id, namaKapal, tujuan, tanggalBerangkat, jamBerangkat});
            }
            jadwaltable.setModel(tableModel);
            jadwaltable.removeColumn(jadwaltable.getColumnModel().getColumn(0)); // Hide ID column
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error retrieving data: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void editSelectedJadwal() {
        int selectedRow = jadwaltable.getSelectedRow();
        if (selectedRow != -1) {
            DefaultTableModel model = (DefaultTableModel) jadwaltable.getModel();
            int id = (int) model.getValueAt(selectedRow, 0);
            String namaKapal = (String) model.getValueAt(selectedRow, 1);
            String tujuan = (String) model.getValueAt(selectedRow, 2);
            Date tanggalBerangkat = (Date) model.getValueAt(selectedRow, 3);
            String jamBerangkat = (String) model.getValueAt(selectedRow, 4);

            JTextField tfNamaKapal = new JTextField(namaKapal);
            JTextField tfTujuan = new JTextField(tujuan);
            JTextField tfTanggalBerangkat = new JTextField(tanggalBerangkat.toString());
            JTextField tfJamBerangkat = new JTextField(jamBerangkat);

            JPanel panel = new JPanel(new GridLayout(4, 2));
            panel.add(new JLabel("Nama Kapal:"));
            panel.add(tfNamaKapal);
            panel.add(new JLabel("Tujuan:"));
            panel.add(tfTujuan);
            panel.add(new JLabel("Tanggal Berangkat:"));
            panel.add(tfTanggalBerangkat);
            panel.add(new JLabel("Jam Berangkat:"));
            panel.add(tfJamBerangkat);

            int result = JOptionPane.showConfirmDialog(null, panel, "Edit Jadwal", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
            if (result == JOptionPane.OK_OPTION) {
                model.setValueAt(tfNamaKapal.getText().trim(), selectedRow, 1);
                model.setValueAt(tfTujuan.getText().trim(), selectedRow, 2);
                model.setValueAt(Date.valueOf(tfTanggalBerangkat.getText().trim()), selectedRow, 3);
                model.setValueAt(tfJamBerangkat.getText().trim(), selectedRow, 4);
            }
        } else {
            JOptionPane.showMessageDialog(null, "Pilih jadwal yang ingin diedit terlebih dahulu.", "Warning", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void saveEditedJadwal() {
        List<Object[]> rowsToUpdate = new ArrayList<>();

        for (int i = 0; i < tableModel.getRowCount(); i++) {
            int id = (int) tableModel.getValueAt(i, 0);
            String updatedNamaKapal = (String) tableModel.getValueAt(i, 1);
            String updatedTujuan = (String) tableModel.getValueAt(i, 2);
            Date updatedTanggalBerangkat = (Date) tableModel.getValueAt(i, 3);
            String updatedJamBerangkat = (String) tableModel.getValueAt(i, 4);

            Object[] originalRow = originalData.get(i);
            if (!updatedNamaKapal.equals(originalRow[1]) || !updatedTujuan.equals(originalRow[2]) ||
                    !updatedTanggalBerangkat.equals(originalRow[3]) || !updatedJamBerangkat.equals(originalRow[4])) {
                rowsToUpdate.add(new Object[]{id, updatedNamaKapal, updatedTujuan, updatedTanggalBerangkat, updatedJamBerangkat});
            }
        }

        String sql = "UPDATE jadwal_keberangkatan SET nama_kapal = ?, tujuan = ?, tanggal_berangkat = ?, jam_berangkat = ? WHERE id = ?";

        try (Connection conn = buatKoneksi()) {
            for (Object[] row : rowsToUpdate) {
                try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                    pstmt.setString(1, (String) row[1]);
                    pstmt.setString(2, (String) row[2]);
                    pstmt.setDate(3, (Date) row[3]);
                    pstmt.setString(4, (String) row[4]);
                    pstmt.setInt(5, (int) row[0]);

                    int rowsUpdated = pstmt.executeUpdate();
                    if (rowsUpdated == 0) {
                        JOptionPane.showMessageDialog(null, "Jadwal gagal disimpan. Silakan coba lagi.", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
            JOptionPane.showMessageDialog(null, "Jadwal berhasil disimpan!");
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error updating data: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
        loadAllJadwal();
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("jadwalkapal");
        frame.setContentPane(new editjadwal().jadwal);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}
