package EducationSystem;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class StudentTableGUI extends JFrame {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JTextField txtId, txtName, txtgender, txtDisability, txtcontact;
    private JTable tblStudent;
    private JButton btnAdd, btnModify, btnDelete, btnDisplay;

    private Connection connection;
	private String gender;
	private String Disability;
	

    public void Student(String id, String name, String gender2, String disability2, String aadharNo) {
		// TODO Auto-generated constructor stub
	}

	public StudentTableGUI() {
	    //this.MainPage = MainPage;
        initializeUI();
        connectToDatabase();
        displayStudents();
    }

    private void initializeUI() {
        txtId = new JTextField();
        txtName = new JTextField();
        txtgender = new JTextField();
        txtDisability = new JTextField();
        txtcontact = new JTextField();
        

        tblStudent = new JTable();
        tblStudent.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tblStudent.getSelectionModel().addListSelectionListener(e -> selectStudent());

        JScrollPane scrollPane = new JScrollPane(tblStudent);

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

        panel.add(new JLabel("Student ID:"), gbc);
        gbc.gridy++;
        panel.add(new JLabel("Name:"), gbc);
        gbc.gridy++;
        panel.add(new JLabel("Gender:"), gbc);
        gbc.gridy++;
        panel.add(new JLabel("Disability:"), gbc);
        gbc.gridy++;
        panel.add(new JLabel("conatct no:"), gbc);
        gbc.gridy++;
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1;

        panel.add(txtId, gbc);
        gbc.gridy++;
        panel.add(txtName, gbc);
        gbc.gridy++;
        panel.add(txtgender, gbc);
        gbc.gridy++;
        panel.add(txtDisability, gbc);
        gbc.gridy++;
        panel.add(txtcontact, gbc);
        gbc.gridy++;
        

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

        btnAdd.addActionListener(e -> insertStudents());

        btnModify.addActionListener(e -> modifyStudents());

        btnDelete.addActionListener(e -> deleteStudents());

        btnDisplay.addActionListener(e -> displayStudents());

        setTitle("Student Table");
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

    private void insertStudents() {
        String id = txtId.getText();
        String name = txtName.getText();
        String gender = txtgender.getText();
        String Disability = txtDisability.getText();
        String contact = txtcontact.getText();
        

        try {
            String query = "INSERT INTO student (student_id, Name, gender, Disability, contact_info) VALUES (?, ?, ?, ?, ?)";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, id);
            statement.setString(2, name);
            
			statement.setString(3, gender);
            statement.setString(4, Disability);
            statement.setString(5, contact);
            
            statement.executeUpdate();

            clearFields();
            displayStudents();
            JOptionPane.showMessageDialog(this, "Successfully inserted.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void modifyStudents() {
        int selectedRow = tblStudent.getSelectedRow();
        if (selectedRow >= 0) {
            String id = txtId.getText();
            String name = txtName.getText();
            String gender = txtgender.getText();
            String Disability = txtDisability.getText();
            String contact = txtcontact.getText();
         
            try {
                String query = "UPDATE student SET Name=?, gender=?, Disability=?, contact_info=? WHERE student_id=?";
                PreparedStatement statement = connection.prepareStatement(query);
                statement.setString(1, name);
                statement.setString(2, gender);
                statement.setString(3, Disability);
                statement.setString(4, contact);
                
                statement.setString(5, id);
                statement.executeUpdate();

                clearFields();
                displayStudents();
                JOptionPane.showMessageDialog(this, "successfully modified");
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } else {
            JOptionPane.showMessageDialog(this, "Please select a student details to modify.");
        }
    }

    private void deleteStudents() {
        int selectedRow = tblStudent.getSelectedRow();
        if (selectedRow >= 0) {
            String id = tblStudent.getValueAt(selectedRow, 0).toString();

            int option = JOptionPane.showConfirmDialog(this, "Are you sure you want to delete this customer?", "Confirmation", JOptionPane.YES_NO_OPTION);
            if (option == JOptionPane.YES_OPTION) {
                try {
                    String query = "DELETE FROM student WHERE student_id=?";
                    PreparedStatement statement = connection.prepareStatement(query);
                    statement.setString(1, id);
                    statement.executeUpdate();

                    clearFields();
                    displayStudents();
                    JOptionPane.showMessageDialog(this, "Successfully deleted");
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        } else {
            JOptionPane.showMessageDialog(this, "Please select a student to delete.");
        }
    }

    private void displayStudents() {
        try {
            String query = "SELECT * FROM student";
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);

            List<Student> student = new ArrayList<>();
            while (resultSet.next()) {
                String id = resultSet.getString("student_id");
                String name = resultSet.getString("Name");
                String gender = resultSet.getString("gender");
                String Disability = resultSet.getString("Disability");
                String contact = resultSet.getString("contact_info");
                
               
				student.add(new Student(id, name, gender, Disability, contact));
            }

            DefaultTableModel model = new DefaultTableModel();
            model.setColumnIdentifiers(new String[]{"ID", "Name", "Gender", "Disability", "contact"});

            for (Student students : student) {
                model.addRow(new String[]{students.getId(), students.getName(), students.getgender(),
                        students.getDisability(), students.getcontact()});
            }

            tblStudent.setModel(model);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void selectStudent() {
        int selectedRow = tblStudent.getSelectedRow();
        if (selectedRow >= 0) {
            String id = tblStudent.getValueAt(selectedRow, 0).toString();
            String name = tblStudent.getValueAt(selectedRow, 1).toString();
            String address = tblStudent.getValueAt(selectedRow, 2).toString();
            String contactNo = tblStudent.getValueAt(selectedRow, 3).toString();
            String aadharNo = tblStudent.getValueAt(selectedRow, 4).toString();
           

            txtId.setText(id);
            txtName.setText(name);
            txtgender.setText(address);
            txtDisability.setText(contactNo);
            txtcontact.setText(aadharNo);
           
        }
    }

    private void clearFields() {
        txtId.setText("");
        txtName.setText("");
        txtgender.setText("");
        txtDisability.setText("");
        txtcontact.setText("");
        
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(StudentTableGUI::new);
    }

    private class Student {
        private String id;
        private String name;
        private String gender;
        private String Disability;
        private String contact;
        

        public Student(String id, String name, String gender, String Disability, String contact) {
            this.id = id;
            this.name = name;
            this.gender = gender;
            this.Disability = Disability;
            this.contact = contact;
            
        }

        public String getId() {
            return id;
        }

        public String getName() {
            return name;
        }

        public String getgender() {
            return gender;
        }

        public String getDisability() {
            return Disability;
        }

        public String getcontact() {
            return contact;
        }

       
        }
    }


