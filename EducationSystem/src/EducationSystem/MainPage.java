package EducationSystem;

import javax.swing.*;
import javax.swing.ImageIcon;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

import java.awt.event.*;

public class MainPage extends JFrame {
    /*
	 * 
	 */
	private static final long serialVersionUID = 1L;
	//private JButton retrieveMarksButton;

    public MainPage() {
        // Set frame properties
        setTitle("Education system");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Create label
        JLabel welcomeLabel = new JLabel("Education Ecosystem for Specially Abled Students");
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 18));
        welcomeLabel.setHorizontalAlignment(SwingConstants.CENTER);
        welcomeLabel.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));
        add(welcomeLabel, BorderLayout.NORTH);

       

        // Create menu bar
        JMenuBar menuBar = new JMenuBar();

        // Create menus
        JMenu StudentMenu = new JMenu("Student Detials");
        JMenu FacultyMenu = new JMenu("Faculty Details");
        JMenu CourseMenu = new JMenu("Course Details");
        JMenu AccoMenu = new JMenu("Accmmodations");
        //JMenu bankMenu = new JMenu("bank Details");

        // Create menu item for student menu
        JMenuItem Viewstudentdetails = new JMenuItem("View student details");
         Viewstudentdetails.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                new StudentTableGUI();
            }
        });

        // Create menu item for course menu
        JMenuItem viewFacultydetails = new JMenuItem("View Faculty details");
        viewFacultydetails.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                new Faculty();
            }
        });

        // Create menu item for enrollment menu
        JMenuItem viewCoursedetails = new JMenuItem("View Course Details");
        viewCoursedetails.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                new Course();
            }
        });

        // Create menu item for semester menu
        JMenuItem viewAccommodationsdetails = new JMenuItem("View Accommodations Details");
        viewAccommodationsdetails.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                new Accommodations();
            }
        });
        

        // Create menu item for grade menu
       

        // Add menu items to respective menus
        StudentMenu.add(Viewstudentdetails);
        FacultyMenu.add(viewFacultydetails);
        CourseMenu.add(viewCoursedetails);
        AccoMenu.add(viewAccommodationsdetails);
        //bankMenu.add(viewbankDetails);

        // Add menus to the menu bar
        menuBar.add(StudentMenu);
        menuBar.add(FacultyMenu);
        menuBar.add(CourseMenu);
        menuBar.add(AccoMenu);
       // menuBar.add(bankMenu);

        // Set the menu bar
        setJMenuBar(menuBar);


        addWindowStateListener(new WindowStateListener() {
            public void windowStateChanged(WindowEvent e) {
                if ((e.getNewState() & Frame.MAXIMIZED_BOTH) == Frame.MAXIMIZED_BOTH) {
                    System.out.println("Window maximized");
                } else {
                    System.out.println("Window not maximized");
                }
            }
        });

        // Set frame size and visibility
        setSize(800, 600);
        setVisible(true);
    }
   

    public static void main(String[] args) {
    	
        new MainPage();
    }
}

