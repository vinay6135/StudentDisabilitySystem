package EducationSystem;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class Faculty extends JFrame {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JTextField txtTeacher, txtName, txtcontact, txtspecialization;
    private JTable tblFaculty;
    private JButton btnAdd, btnModify, btnDelete, btnDisplay;

    private Connection connection;

    public Faculty() {
        initializeUI();
        connectToDatabase();
        displayFaculty();
    }

    private void initializeUI() {
        txtTeacher = new JTextField();
        txtName = new JTextField();
        txtcontact = new JTextField();
        txtspecialization = new JTextField();

        tblFaculty = new JTable();
        tblFaculty.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tblFaculty.getSelectionModel().addListSelectionListener(e -> selectAccount());

        JScrollPane scrollPane = new JScrollPane(tblFaculty);

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

        panel.add(new JLabel("Teacher id:"), gbc);
        gbc.gridy++;
        panel.add(new JLabel("Name:"), gbc);
        gbc.gridy++;
        panel.add(new JLabel("contact no:"), gbc);
        gbc.gridy++;
        panel.add(new JLabel("Specialization:"), gbc);

        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1;

        panel.add(txtTeacher, gbc);
        gbc.gridy++;
        panel.add(txtName, gbc);
        gbc.gridy++;
        panel.add(txtcontact, gbc);
        gbc.gridy++;
        panel.add(txtspecialization, gbc);

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

        btnAdd.addActionListener(e -> insertFaculty());

        btnModify.addActionListener(e -> modifyFaculty());

        btnDelete.addActionListener(e -> deleteFaculty());

        btnDisplay.addActionListener(e -> displayFaculty());

        setTitle("Faculty Table");
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

    private void insertFaculty() {
        String Teacherid = txtTeacher.getText();
        String Name = txtName.getText();
        String contact = txtcontact.getText();
        String specialization = txtspecialization.getText();

        try {
            String query = "INSERT INTO Faculty (Teacher_id, Name, contact_info, specialization) VALUES (?, ?, ?, ?)";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, Teacherid);
            statement.setString(2, Name);
            statement.setString(3, contact);
            statement.setString(4, specialization);
            statement.executeUpdate();

            clearFields();
            displayFaculty();
        } catch (SQLException e) {
        	JOptionPane.showMessageDialog(this, "please enter the values");
            
        }
    }

    private void modifyFaculty() {
        int selectedRow = tblFaculty.getSelectedRow();
        if (selectedRow >= 0) {
            String teacher = txtTeacher.getText();
            String name = txtName.getText();
            String contact = txtcontact.getText();
            String specialization = txtspecialization.getText();

            try {
                String query = "UPDATE Faculty SET Name=?, contact_info=?, specialization=? WHERE Teacher_id=?";
                PreparedStatement statement = connection.prepareStatement(query);
                statement.setString(1, name);
                statement.setString(2, contact);
                statement.setString(3, specialization);
                statement.setString(4, teacher);
                statement.executeUpdate();

                clearFields();
                displayFaculty();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } else {
            JOptionPane.showMessageDialog(this, "Please select an Teacher to modify.");
        }
    }

    private void deleteFaculty() {
        int selectedRow = tblFaculty.getSelectedRow();
        if (selectedRow >= 0) {
            String Teacher = tblFaculty.getValueAt(selectedRow, 0).toString();

            int option = JOptionPane.showConfirmDialog(this, "Are you sure you want to delete this Information?", "Confirmation", JOptionPane.YES_NO_OPTION);
            if (option == JOptionPane.YES_OPTION) {
                try {
                    String query = "DELETE FROM Faculty WHERE Teacher_id=?";
                    PreparedStatement statement = connection.prepareStatement(query);
                    statement.setString(1, Teacher);
                    statement.executeUpdate();

                    clearFields();
                    displayFaculty();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        } else {
            JOptionPane.showMessageDialog(this, "Please select an Teacher id to delete.");
        }
    }

    private void displayFaculty() {
        try {
            String query = "SELECT * FROM Faculty";
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);

            List<Faculty1> Facul = new ArrayList<>();
            while (resultSet.next()) {
                String Teacher = resultSet.getString("Teacher_id");
                String Name = resultSet.getString("Name");
                String contact = resultSet.getString("contact_info");
                String specialization = resultSet.getString("specialization");
                Facul.add(new Faculty1(Teacher, Name, contact, specialization));
            }

            DefaultTableModel model = new DefaultTableModel();
            model.setColumnIdentifiers(new String[]{"Teacher id", "Name", "contact info", "specialization"});

            for (Faculty1 facul : Facul) {
                model.addRow(new String[]{  facul.getTeacher(), facul.getName(), facul.getcontact(), facul.getspecialization()});
            }

            tblFaculty.setModel(model);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void selectAccount() {
        int selectedRow = tblFaculty.getSelectedRow();
        if (selectedRow >= 0) {
            String Teacher = tblFaculty.getValueAt(selectedRow, 0).toString();
            String Name = tblFaculty.getValueAt(selectedRow, 1).toString();
            String contact = tblFaculty.getValueAt(selectedRow, 2).toString();
            String specialization = tblFaculty.getValueAt(selectedRow, 3).toString();

            txtTeacher.setText(Teacher);
            txtName.setText(Name);
            txtcontact.setText(contact);
            txtspecialization.setText(specialization);
        }
    }

    private void clearFields() {
        txtTeacher.setText("");
        txtName.setText("");
        txtcontact.setText("");
        txtspecialization.setText("");
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(Faculty::new);
    }

    private class Faculty1{
        private String Teacher;
        private String Name;
        private String contact;
       private String specialization;

        public Faculty1(String Teacher, String Name, String contact, String specialization) {
            this.Teacher = Teacher;
            this.Name= Name;
            this.contact = contact;
            this.specialization = specialization;
        }

        public String getTeacher() {
            return Teacher;
        }

        public String getName() {
            return Name;
        }

        public String getcontact() {
            return contact;
        }

        public String getspecialization() {
            return specialization;
        }
    }
}
