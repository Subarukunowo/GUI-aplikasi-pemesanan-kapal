import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Riwayat extends JFrame {
    private JPanel Form;
    private JTable table1;
    private JButton kembaliButton;
    private DefaultTableModel tableModel;

    public Riwayat() {
        setTitle("Riwayat Pemesanan");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        Form = new JPanel(new BorderLayout());
        kembaliButton = new JButton("Kembali");

        tableModel = new DefaultTableModel();
        tableModel.setColumnIdentifiers(new String[]{"No Resi", "Nama", "Tanggal Pembayaran"});
        table1 = new JTable(tableModel);

        loadDataFromDatabase();

        JScrollPane scrollPane = new JScrollPane(table1);
        Form.add(scrollPane, BorderLayout.CENTER);
        Form.add(kembaliButton, BorderLayout.SOUTH);

        setContentPane(Form);

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

        setVisible(true);
    }

    private void loadDataFromDatabase() {
        String url = "jdbc:mysql://localhost:3306/pemesanankapaltiket";
        String user = "root";
        String password = "";

        String query = "SELECT No_resi, nama, tanggal FROM riwayat";

        try (Connection connection = DriverManager.getConnection(url, user, password);
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {

            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

            while (resultSet.next()) {
                String noResi = resultSet.getString("No_resi");
                String nama = resultSet.getString("nama");
                Date tanggal = resultSet.getDate("tanggal");
                String formattedTanggal = dateFormat.format(tanggal);

                tableModel.addRow(new Object[]{noResi, nama, formattedTanggal});
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new Riwayat());
    }
}
