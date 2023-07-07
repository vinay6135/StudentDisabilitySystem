package EducationSystem;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class Course extends JFrame {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JTextField txtcourse, txtname, txtdis,txtsid;
    private JTable tblcourse;
    private JButton btnAdd, btnModify, btnDelete, btnDisplay;

    private Connection connection;

    public Course() {
        initializeUI();
        connectToDatabase();
        displaycourse();
    }

    private void initializeUI() {
        txtcourse = new JTextField();
        txtname = new JTextField();
        txtdis = new JTextField();
        txtsid=new JTextField();

        tblcourse = new JTable();
        tblcourse.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tblcourse.getSelectionModel().addListSelectionListener(e -> selectAtm());

        JScrollPane scrollPane = new JScrollPane(tblcourse);

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

        panel.add(new JLabel("Course id:"), gbc);
        gbc.gridy++;
        panel.add(new JLabel("course name:"), gbc);
        gbc.gridy++;
        panel.add(new JLabel("Disability:"), gbc);
        gbc.gridy++;
        panel.add(new JLabel("student_id:"), gbc);
        

        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1;

        panel.add(txtcourse, gbc);
        gbc.gridy++;
        panel.add(txtname, gbc);
        gbc.gridy++;
        panel.add(txtdis, gbc);
        gbc.gridy++;
        panel.add(txtsid,gbc);
        

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

        btnAdd.addActionListener(e -> insertcourse());

        btnModify.addActionListener(e -> modifycourse());

        btnDelete.addActionListener(e -> deletecourse());

        btnDisplay.addActionListener(e -> displaycourse());

        setTitle("courses");
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

    private void insertcourse() {
        String course = txtcourse.getText();
        String name = txtname.getText();
        String dis = txtdis.getText();
        String sid=txtsid.getText();

        try {
            String query = "INSERT INTO course (course_id, course_name, Disability_type,student_id) VALUES (?, ?, ?,?)";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, course);
            statement.setString(2, name);
            statement.setString(3, dis);
            statement.setString(4,sid);
            statement.executeUpdate();

            clearFields();
            displaycourse();
            JOptionPane.showMessageDialog(this, "Successfully inserted");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void modifycourse() {
        int selectedRow = tblcourse.getSelectedRow();
        if (selectedRow >= 0) {
            String course = txtcourse.getText();
            String name = txtname.getText();
            String dis = txtdis.getText();
            String sid=txtsid.getText();

            try {
                String query = "UPDATE course SET course_name=?,Disability_type=?,student_id=? WHERE course_id=?";
                PreparedStatement statement = connection.prepareStatement(query);
                statement.setString(1, name);
                statement.setString(2, dis);
                statement.setString(3, sid);
                statement.setString(4,course);
                statement.executeUpdate();

                clearFields();
                displaycourse();
                JOptionPane.showMessageDialog(this, "Successfully modified");
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } else {
            JOptionPane.showMessageDialog(this, "Please select an course to modify.");
        }
    }

    private void deletecourse() {
        int selectedRow = tblcourse.getSelectedRow();
        if (selectedRow >= 0) {
            String course = tblcourse.getValueAt(selectedRow, 0).toString();

            int option = JOptionPane.showConfirmDialog(this, "Are you sure you want to delete this course?", "Confirmation", JOptionPane.YES_NO_OPTION);
            if (option == JOptionPane.YES_OPTION) {
                try {
                    String query = "DELETE FROM course WHERE course_id=?";
                    PreparedStatement statement = connection.prepareStatement(query);
                    statement.setString(1, course);
                    statement.executeUpdate();

                    clearFields();
                    displaycourse();
                    JOptionPane.showMessageDialog(this, "Deleted successfully.");
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        } else {
            JOptionPane.showMessageDialog(this, "Please select an course to delete.");
        }
    }

    private void displaycourse() {
        try {
            String query = "SELECT * FROM course";
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);

            List<course> courses = new ArrayList<>();
            while (resultSet.next()) {
                String course = resultSet.getString("course_id");
                String name = resultSet.getString("course_name");
                String dis = resultSet.getString("Disability_type");
                String sid = resultSet.getString("student_id");
                
                courses.add(new course(course, name, dis,sid));
            }

            DefaultTableModel model = new DefaultTableModel();
            model.setColumnIdentifiers(new String[]{"course id", "course name", "Disability","Student id"});

            for (course cou : courses) {
                model.addRow(new String[]{cou.getcourse(), cou.getname(), cou.getdis(),cou.getsid()});
            }

            tblcourse.setModel(model);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void selectAtm() {
        int selectedRow = tblcourse.getSelectedRow();
        if (selectedRow >= 0) {
            String course = tblcourse.getValueAt(selectedRow, 0).toString();
            String name = tblcourse.getValueAt(selectedRow, 1).toString();
            String dis = tblcourse.getValueAt(selectedRow, 2).toString();
            String sid = tblcourse.getValueAt(selectedRow, 3).toString();
            

            txtcourse.setText(course);
            txtname.setText(name);
            txtdis.setText(dis);
            txtsid.setText(sid);
        }
    }

    private void clearFields() {
        txtcourse.setText("");
        txtname.setText("");
        txtdis.setText("");
        txtsid.setText("");
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(Course::new);
    }

    private class course {
        private String course;
        private String name;
        private String dis;
        private String sid;

        public course(String course, String name, String dis,String sid) {
            this.course = course;
            this.name = name;
            this.dis = dis;
            this.sid=sid;
        }

        public String getcourse() {
            return course;
        }

        public String getname() {
            return name;
        }

        public String getdis() {
            return dis;
        }
        public String getsid() {
            return sid;
        }
    }
}
