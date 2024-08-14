import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.sql.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashSet;
import java.util.Set;

public class KelolaProfil {
    private JPanel panel1;
    private JPanel jpkelola;
    private JTextField tfnama;
    private JTextField tfemail;
    private JTextField tfnohp;
    private JButton btsimpan;
    private JButton btkembali;
    private JButton pilihGambarButton;
    private JLabel labelGambar;

    private Connection connection;
    private BufferedImage bufferedImage;
    private final Set<String> allowedFormats;

    public KelolaProfil() {
        allowedFormats = new HashSet<>();
        initializeAllowedFormats();
        initializeComponents();
        connectToDatabase();

        btsimpan.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                saveProfile();
            }
        });

        btkembali.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFrame menuUtamaFrame = new JFrame("Menu Utama");
                menuutama menuUtamaForm = new menuutama();
                menuUtamaFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                menuUtamaFrame.getContentPane().add(menuUtamaForm.getmenuutama());
                menuUtamaFrame.pack();
                menuUtamaFrame.setLocationRelativeTo(null);
                menuUtamaFrame.setVisible(true);

                JComponent component = (JComponent) btkembali;
                Window window = SwingUtilities.getWindowAncestor(component);
                window.dispose();
            }
        });

        pilihGambarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                chooseImage();
            }
        });
    }

    private void initializeAllowedFormats() {
        String[] formats = ImageIO.getReaderFormatNames();
        for (String format : formats) {
            allowedFormats.add(format.toLowerCase());
        }
    }

    private void initializeComponents() {
        panel1 = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);

        jpkelola = new JPanel(new GridBagLayout());
        GridBagConstraints jpkGbc = new GridBagConstraints();
        jpkGbc.fill = GridBagConstraints.HORIZONTAL;
        jpkGbc.insets = new Insets(5, 5, 5, 5);

        JLabel lblKelolaProfil = new JLabel("Kelola Profil", SwingConstants.CENTER);
        lblKelolaProfil.setFont(new Font("Arial", Font.BOLD, 18));

        tfnama = new JTextField(20);
        tfemail = new JTextField(20);
        tfnohp = new JTextField(15);
        btsimpan = new JButton("Simpan");
        btkembali = new JButton("Kembali");
        pilihGambarButton = new JButton("Pilih Gambar");
        labelGambar = new JLabel();

        // Adding components to jpkelola
        jpkGbc.gridx = 0;
        jpkGbc.gridy = 0;
        jpkGbc.gridwidth = 2;
        jpkelola.add(lblKelolaProfil, jpkGbc);

        jpkGbc.gridy++;
        jpkelola.add(pilihGambarButton, jpkGbc);

        jpkGbc.gridy++;
        jpkelola.add(labelGambar, jpkGbc);

        jpkGbc.gridwidth = 1;
        jpkGbc.gridy++;
        jpkGbc.gridx = 0;
        jpkelola.add(new JLabel("Nama"), jpkGbc);
        jpkGbc.gridx = 1;
        jpkelola.add(tfnama, jpkGbc);

        jpkGbc.gridy++;
        jpkGbc.gridx = 0;
        jpkelola.add(new JLabel("Email"), jpkGbc);
        jpkGbc.gridx = 1;
        jpkelola.add(tfemail, jpkGbc);



        jpkGbc.gridy++;
        jpkGbc.gridx = 0;
        jpkelola.add(new JLabel("No Handphone"), jpkGbc);
        jpkGbc.gridx = 1;
        jpkelola.add(tfnohp, jpkGbc);

        jpkGbc.gridy++;
        jpkGbc.gridx = 0;
        jpkGbc.gridwidth = 2;
        jpkelola.add(btsimpan, jpkGbc);

        gbc.gridx = 0;
        gbc.gridy = 0;
        panel1.add(btkembali, gbc);

        gbc.gridx = 1;
        panel1.add(jpkelola, gbc);
    }

    private void connectToDatabase() {
        try {
            // Replace these with your actual database credentials
            String url = "jdbc:mysql://localhost:3306/pemesanankapaltiket";
            String user = "root";
            String password = "";

            connection = DriverManager.getConnection(url, user, password);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void saveProfile() {
        try {
            String nama = tfnama.getText();
            String email = tfemail.getText();
            String no_hp = tfnohp.getText();
            byte[] foto_profil = bufferedImage != null ? imageToByteArray(bufferedImage) : null;

            String query = "INSERT INTO profil (nama, email, no_hp, foto_profil) VALUES (?, ?, ?, ?)";
            PreparedStatement pstmt = connection.prepareStatement(query);
            pstmt.setString(1, nama);
            pstmt.setString(2, email);
            pstmt.setString(3, no_hp);
            if (foto_profil != null) {
                pstmt.setBytes(4, foto_profil);
            } else {
                pstmt.setNull(4, Types.BLOB);
            }

            pstmt.executeUpdate();
            JOptionPane.showMessageDialog(null, "Profile added successfully!");
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Failed to add profile!");
        }
    }

    private void chooseImage() {
        JFileChooser fileChooser = new JFileChooser();
        int result = fileChooser.showOpenDialog(null);

        if (result == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            String fileExtension = getFileExtension(file);
            if (allowedFormats.contains(fileExtension.toLowerCase())) {
                try {
                    bufferedImage = ImageIO.read(file);
                    labelGambar.setIcon(new ImageIcon(bufferedImage));
                } catch (IOException e) {
                    e.printStackTrace();
                    JOptionPane.showMessageDialog(null, "Failed to load image!");
                }
            } else {
                JOptionPane.showMessageDialog(null, "Unsupported image format!");
            }
        }
    }

    private byte[] imageToByteArray(BufferedImage image) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            ImageIO.write(image, "png", baos);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return baos.toByteArray();
    }

    private BufferedImage byteArrayToImage(byte[] byteArray) {
        ByteArrayInputStream bais = new ByteArrayInputStream(byteArray);
        try {
            return ImageIO.read(bais);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private String getFileExtension(File file) {
        String fileName = file.getName();
        int lastIndexOfDot = fileName.lastIndexOf('.');
        if (lastIndexOfDot != -1 && lastIndexOfDot < fileName.length() - 1) {
            return fileName.substring(lastIndexOfDot + 1);
        }
        return "";
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("Kelola Profil");
        frame.setContentPane(new KelolaProfil().panel1);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setLocationRelativeTo(null); // Center the frame on the screen
        frame.setVisible(true);
    }
}
