import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class about extends JFrame {
    private JTable table;
    private DefaultTableModel tableModel;

    public about() {
        setTitle("Tampilkan Data Diri");
        setLayout(new BorderLayout());


        tableModel = new DefaultTableModel();
        table = new JTable(tableModel);

        tableModel.addColumn("NIM");
        tableModel.addColumn("Nama Lengkap");
        tableModel.addColumn("No HP");
        tableModel.addColumn("Email");
        tableModel.addColumn("Alamat");

        JScrollPane scrollPane = new JScrollPane(table);
        add(scrollPane, BorderLayout.CENTER);


        loadData();


        JButton kembaliButton = new JButton("Kembali");
        kembaliButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                menuutama.main(new String[0]);
                JComponent component = (JComponent) e.getSource();
                Window window = SwingUtilities.getWindowAncestor(component);
                window.dispose();
            }
        });

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(kembaliButton);
        add(buttonPanel, BorderLayout.SOUTH);

        setSize(600, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }

    private void loadData() {
        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/pemesanankapaltiket", "root", "")) {
            String query = "SELECT nim, nama_lengkap, no_hp, email, alamat FROM datadiri";
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(query);

            while (rs.next()) {
                String nim = rs.getString("nim");
                String namaLengkap = rs.getString("nama_lengkap");
                String noHp = rs.getString("no_hp");
                String email = rs.getString("email");
                String alamat = rs.getString("alamat");

                tableModel.addRow(new Object[]{nim, namaLengkap, noHp, email, alamat});
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error saat memuat data: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new about();
            }
        });
    }
}
