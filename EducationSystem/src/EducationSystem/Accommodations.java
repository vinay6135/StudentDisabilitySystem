package EducationSystem;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class Accommodations extends JFrame {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JTextField txtsid, txtacc;
    private JTable tblAcc;
    private JButton btnAdd, btnModify, btnDelete, btnDisplay;

    private Connection connection;

    public Accommodations() {
        initializeUI();
        connectToDatabase();
        displayAcc();
    }

    private void initializeUI() {
        txtsid = new JTextField();
        txtacc = new JTextField();
        

        tblAcc = new JTable();
        tblAcc.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tblAcc.getSelectionModel().addListSelectionListener(e -> selectAtm());

        JScrollPane scrollPane = new JScrollPane(tblAcc);

        btnAdd = new JButton("Add");
        btnModify = new JButton("Modify");
        btnDelete = new JButton("Delete");
        btnDisplay = new JButton("Display");

        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(5, 5, 5, 5);

        panel.add(new JLabel("Student id:"), gbc);
        gbc.gridy++;
        panel.add(new JLabel("Accommodation:"), gbc);
        

        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1;

        panel.add(txtsid, gbc);
        gbc.gridy++;
        panel.add(txtacc, gbc);
       

        gbc.gridx = 0;
        gbc.gridy++;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.weightx = 0;

        panel.add(btnAdd, gbc);
        gbc.gridy++;
        panel.add(btnModify, gbc);
        gbc.gridy++;
        panel.add(btnDelete, gbc);
        gbc.gridy++;
        panel.add(btnDisplay, gbc);

        setLayout(new BorderLayout());
        add(panel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);

        btnAdd.addActionListener(e -> insertAcc());

        btnModify.addActionListener(e -> modifyAcc());

        btnDelete.addActionListener(e -> deleteAcc());

        btnDisplay.addActionListener(e -> displayAcc());

        setTitle("Accommodations");
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void connectToDatabase() {
        String url = "jdbc:oracle:thin:@localhost:1521:xe";
        String username = "vinay";
        String password = "vinay";

        try {
            connection = DriverManager.getConnection(url, username, password);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void insertAcc() {
        String student = txtsid.getText();
        String accommo = txtacc.getText();
       

        try {
            String query = "INSERT INTO Accommodations (student_id, Accommodation_type) VALUES (?, ?)";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1,student );
            statement.setString(2, accommo);
            
            statement.executeUpdate();

            clearFields();
            displayAcc();
            JOptionPane.showMessageDialog(this, "inserted successfully.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void modifyAcc() {
        int selectedRow = tblAcc.getSelectedRow();
        if (selectedRow >= 0) {
            String stuid = txtsid.getText();
            String acc = txtacc.getText();
           

            try {
                String query = "UPDATE Accommodations SET Accommodation_type=? WHERE student_id=?";
                PreparedStatement statement = connection.prepareStatement(query);
                statement.setString(1, acc);
                statement.setString(2, stuid);
                
                statement.executeUpdate();

                clearFields();
                displayAcc();
                JOptionPane.showMessageDialog(this, "Successfully modified.");
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } else {
            JOptionPane.showMessageDialog(this, "Please select an student to modify.");
        }
    }

    private void deleteAcc() {
        int selectedRow = tblAcc.getSelectedRow();
        if (selectedRow >= 0) {
            String acc = tblAcc.getValueAt(selectedRow, 0).toString();

            int option = JOptionPane.showConfirmDialog(this, "Are you sure you want to delete this ?", "Confirmation", JOptionPane.YES_NO_OPTION);
            if (option == JOptionPane.YES_OPTION) {
                try {
                    String query = "DELETE FROM Accommodations WHERE student_id=?";
                    PreparedStatement statement = connection.prepareStatement(query);
                    statement.setString(1, acc);
                    statement.executeUpdate();

                    clearFields();
                    displayAcc();
                    JOptionPane.showMessageDialog(this, "Successfully deleted.");
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        } else {
            JOptionPane.showMessageDialog(this, "Please select an student to delete.");
        }
    }

    private void displayAcc() {
        try {
            String query = "SELECT * FROM Accommodations";
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);

            List<Accommodations1> accs = new ArrayList<>();
            while (resultSet.next()) {
                String stuId = resultSet.getString("student_id");
                String acc = resultSet.getString("Accommodation_type");
                
                accs.add(new Accommodations1(stuId, acc));
            }

            DefaultTableModel model = new DefaultTableModel();
            model.setColumnIdentifiers(new String[]{"Student id", "Accommodation"});

            for (Accommodations1 acc : accs) {
                model.addRow(new String[]{acc.getstdId(), acc.getacc()});
            }

            tblAcc.setModel(model);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

   

	private void selectAtm() {
        int selectedRow = tblAcc.getSelectedRow();
        if (selectedRow >= 0) {
            String stuId = tblAcc.getValueAt(selectedRow, 0).toString();
            String acc = tblAcc.getValueAt(selectedRow, 1).toString();
            

            txtsid.setText(stuId);
            txtacc.setText(acc);
            
        }
    }

    private void clearFields() {
        txtsid.setText("");
        txtacc.setText("");
        
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(Accommodations::new);
    }

    private class Accommodations1 {
        private String stdId;
        private String acc;
      

        public Accommodations1(String stdId, String acc) {
            this.stdId = stdId;
            this.acc = acc;
           
        }

        public String getstdId() {
            return stdId;
        }

        public String getacc() {
            return acc;
        }

        
    }
}

