package org.handicap;

import javax.swing.JPanel;
import java.awt.BorderLayout;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

import org.iconutils.IconUtils;
import org.todivefor.string.utils.StringUtils;

import javax.swing.JButton;
import javax.swing.JTable;
import javax.swing.JScrollPane;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.awt.event.ActionEvent;

public class MaintainCourses extends JPanel 
{
	/*
	 * 	Components referenced else where
	 */
	
	public static JTable tableDisplayCourses;
	public static JTextField textFieldAddCourseName; 
	public static JTextField textFieldAddCourseCourseRating;
	public static JTextField textFieldAddCourseCourseSlope;
	
	final static int MAXCOURSENAMELENGTH = 50;				// max course name length

	/**
	 * Create the panel.
	 */
	public MaintainCourses() 
	{
		setLayout(new BorderLayout(0, 0));
		
		JPanel panelSouth = new JPanel();
		add(panelSouth, BorderLayout.SOUTH);
		
		JButton btnAddCourseExit = new JButton("Back",
				IconUtils.getNavigationIcon("Back", 24));
		btnAddCourseExit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) 
			{
				HandicapMain.cards.show(getParent(), HandicapMain.MAINMENU);
			}
		});
		panelSouth.add(btnAddCourseExit);

		JPanel panelCenter = new JPanel();
		add(panelCenter, BorderLayout.CENTER);
		
		JScrollPane scrollPaneCenter = new JScrollPane();
		panelCenter.add(scrollPaneCenter, BorderLayout.CENTER);
		
		tableDisplayCourses = new JTable();
		
		tableDisplayCourses.addMouseListener(new MouseAdapter() 
		{
			@Override
			public void mouseClicked(MouseEvent e) 
			{
/*
 * 			Mouse clicked on Course table				
 */
				try 
				{
					int row = tableDisplayCourses.getSelectedRow();
					String NAME_ = tableDisplayCourses.getModel().getValueAt(row, 0).toString();				
					String query = "Select * from " + HandicapMain.courseTableName + " where Name = '"+NAME_+"'";
					PreparedStatement pst = sqliteConnection.connection.prepareStatement(query);
					ResultSet rs = pst.executeQuery();

					
					while(rs.next())
					{
						textFieldAddCourseName.setText(rs.getString("Name"));
						HandicapMain.saveCourseName = textFieldAddCourseName.getText();
						textFieldAddCourseCourseRating.setText(rs.getString("Rating"));
						textFieldAddCourseCourseSlope.setText(rs.getString("Slope"));
					}
					textFieldAddCourseName.requestFocusInWindow();		// set focus to course
					rs.close();					
					pst.close();
				}
				catch (Exception e1) 
				{
					e1.printStackTrace();
				}
			}
		});
		
		scrollPaneCenter.setViewportView(tableDisplayCourses);
		
		JPanel panelWest = new JPanel();
		add(panelWest, BorderLayout.WEST);
//		scrollPaneCenter.setRowHeaderView(panelWest);
		GridBagLayout gbl_panelWest = new GridBagLayout();
		gbl_panelWest.columnWidths = new int[]{10, 10, 10, 10, 10, 10};
		gbl_panelWest.rowHeights = new int[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
		gbl_panelWest.columnWeights = new double[]{Double.MIN_VALUE, 0.0};
		gbl_panelWest.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
		panelWest.setLayout(gbl_panelWest);
		
//		JLabel lblBlankSpace = new JLabel("");
//		GridBagConstraints gbc_lblBlankSpace = new GridBagConstraints();
//		gbc_lblBlankSpace.insets = new Insets(0, 0, 5, 5);
//		gbc_lblBlankSpace.gridx = 1;
//		gbc_lblBlankSpace.gridy = 0;
//		panelWest.add(lblBlankSpace, gbc_lblBlankSpace);
//		
//		JLabel lblBlankSpace_1 = new JLabel("");
//		GridBagConstraints gbc_lblBlankSpace_1 = new GridBagConstraints();
//		gbc_lblBlankSpace_1.insets = new Insets(0, 0, 5, 5);
//		gbc_lblBlankSpace_1.gridx = 1;
//		gbc_lblBlankSpace_1.gridy = 3;
//		panelWest.add(lblBlankSpace_1, gbc_lblBlankSpace_1);
		
		JLabel lblCourse = new JLabel("Course");
		GridBagConstraints gbc_lblCourse = new GridBagConstraints();
		gbc_lblCourse.anchor = GridBagConstraints.WEST;
		gbc_lblCourse.insets = new Insets(100, 0, 5, 5);
		gbc_lblCourse.gridx = 1;
		gbc_lblCourse.gridy = 4;
		panelWest.add(lblCourse, gbc_lblCourse);
		
		textFieldAddCourseName = new JTextField(15);
		GridBagConstraints gbc_textfieldAddCourseName = new GridBagConstraints();
		gbc_textfieldAddCourseName.anchor = GridBagConstraints.WEST;
		gbc_textfieldAddCourseName.gridwidth = 4;
		gbc_textfieldAddCourseName.insets = new Insets(100, 0, 5, 0);
		gbc_textfieldAddCourseName.gridx = 2;
		gbc_textfieldAddCourseName.gridy = 4;
		panelWest.add(textFieldAddCourseName, gbc_textfieldAddCourseName);
		
		JLabel lblRating = new JLabel("Rating");
		GridBagConstraints gbc_lblRating = new GridBagConstraints();
		gbc_lblRating.anchor = GridBagConstraints.WEST;
		gbc_lblRating.insets = new Insets(0, 0, 5, 5);
		gbc_lblRating.gridx = 1;
		gbc_lblRating.gridy = 5;
		panelWest.add(lblRating, gbc_lblRating);
		
		textFieldAddCourseCourseRating = new JTextField(4);
		GridBagConstraints gbc_textfieldAddCourseRating = new GridBagConstraints();
		gbc_textfieldAddCourseRating.anchor = GridBagConstraints.WEST;
		gbc_textfieldAddCourseRating.gridwidth = 1;
		gbc_textfieldAddCourseRating.insets = new Insets(0, 0, 5, 5);
		gbc_textfieldAddCourseRating.gridx = 2;
		gbc_textfieldAddCourseRating.gridy = 5;
		panelWest.add(textFieldAddCourseCourseRating, gbc_textfieldAddCourseRating);
		
		JLabel lblSlope = new JLabel("Slope");
		GridBagConstraints gbc_lblSlope = new GridBagConstraints();
		gbc_lblSlope.anchor = GridBagConstraints.WEST;
		gbc_lblSlope.insets = new Insets(0, 0, 5, 5);
		gbc_lblSlope.gridx = 1;
		gbc_lblSlope.gridy = 6;
		panelWest.add(lblSlope, gbc_lblSlope);
		
		textFieldAddCourseCourseSlope = new JTextField(3);
		GridBagConstraints gbc_textfieldAddCourseSlope = new GridBagConstraints();
		gbc_textfieldAddCourseSlope.anchor = GridBagConstraints.WEST;
		gbc_textfieldAddCourseSlope.gridwidth = 1;
		gbc_textfieldAddCourseSlope.insets = new Insets(0, 0, 5, 5);
		gbc_textfieldAddCourseSlope.gridx = 2;
		gbc_textfieldAddCourseSlope.gridy = 6;
		panelWest.add(textFieldAddCourseCourseSlope, gbc_textfieldAddCourseSlope);
		
		JButton btnSave = new JButton("Save");
		btnSave.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) 
			{
				if (!checkCourseNameLength())				// Course name < 51
					return;									// No, get out of here

//				Test for valid rating
				
				String ratingStr = textFieldAddCourseCourseRating.getText();
				if (!(ratingStr.equals("")))
				{
					if (!StringUtils.isStringNumeric(ratingStr))
					{
						JOptionPane.showMessageDialog(null, "Rating \" " + ratingStr +
								"\"  is not a valid number, renenter.",
								"Rating", JOptionPane.ERROR_MESSAGE);
						return;
					}
				}
				else
				{
					JOptionPane.showMessageDialog(null, "Rating \" " + ratingStr +
							"\"  is not a valid number, renenter.",
							"Rating", JOptionPane.ERROR_MESSAGE);
					return;
				}
				
//				Test for valid slope
				
				String slopeStr = textFieldAddCourseCourseSlope.getText();
				if (!(slopeStr.equals("")))
				{
					if (!StringUtils.isInteger(slopeStr))
					{
						JOptionPane.showMessageDialog(null, "Slope \" " + slopeStr +
								"\"  is not a valid slope, renenter.",
								"Slope", JOptionPane.ERROR_MESSAGE);
						return;
					}
				}
				else
				{
					JOptionPane.showMessageDialog(null, "Slope \" " + slopeStr +
							"\"  is not a valid slope, renenter.",
							"Slope", JOptionPane.ERROR_MESSAGE);
					return;
				}
				
				try 
				{	
					String query = "insert into " + HandicapMain.courseTableName + " (Name, Rating, Slope) values(?, ?, ?)";
					PreparedStatement pst = sqliteConnection.connection.prepareStatement(query);
					pst.setString(1, textFieldAddCourseName.getText());
					pst.setString(2, textFieldAddCourseCourseRating.getText());
					pst.setString(3, textFieldAddCourseCourseSlope.getText());
					
					pst.execute();
					JOptionPane.showMessageDialog(null, textFieldAddCourseName.getText() + " course added");
					
					pst.close();
				}
				catch (Exception e1) 
				{
					JOptionPane.showMessageDialog(null, "You are trying to add a duplicate course");
					if (HandicapMain.isDebug())
						e1.printStackTrace();
				}
//				comboBoxCourse = Handicap.getComboBoxCourse();
//				MiscMethods.fillComboBox(connection, courseTableName);
				HandicapMain.refreshCourseTable(sqliteConnection.connection, HandicapMain.courseTableName);
//				HandicapMain.coursesDataChanged = true;		// Force reload courses
			}
		});
		GridBagConstraints gbc_btnSave = new GridBagConstraints();
		gbc_btnSave.anchor = GridBagConstraints.WEST;
		gbc_btnSave.insets = new Insets(50, 0, 5, 5);
		gbc_btnSave.gridx = 1;
		gbc_btnSave.gridy = 8;
		panelWest.add(btnSave, gbc_btnSave);
		
		JButton btnUpdate = new JButton("Update");
		btnUpdate.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) 
			{
				if (!checkCourseNameLength())				// Course name < 51
					return;	
				
				try 
				{				
					String query = "update " + HandicapMain.courseTableName + " set Name='" 
							+ textFieldAddCourseName.getText() + "' ,"
							+ "Rating='" + textFieldAddCourseCourseRating.getText() + "',"
							+ "Slope='" + textFieldAddCourseCourseSlope.getText() + "' "
							+ "where Name='" + HandicapMain.saveCourseName + "'";
							
					PreparedStatement pst = sqliteConnection.connection.prepareStatement(query);
					
					pst.execute();
					if (HandicapMain.isDebug())
						JOptionPane.showMessageDialog(null, textFieldAddCourseName.getText() +
							" course updated");
					
					pst.close();
				}
				catch (Exception e1) 
				{
					e1.printStackTrace();
				}
//				MiscMethods.fillComboBox(connection, courseTableName);
				HandicapMain.refreshCourseTable(sqliteConnection.connection, HandicapMain.courseTableName);
//				HandicapMain.coursesDataChanged = true;		// Force reload courses
			}
		});
		GridBagConstraints gbc_btnUpdate = new GridBagConstraints();
		gbc_btnUpdate.anchor = GridBagConstraints.WEST;
		gbc_btnUpdate.insets = new Insets(0, 0, 5, 5);
		gbc_btnUpdate.gridx = 1;
		gbc_btnUpdate.gridy = 9;
		panelWest.add(btnUpdate, gbc_btnUpdate);
		
		JButton btnDelete = new JButton("Delete");
		btnDelete.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) 
			{
				int action = JOptionPane.showConfirmDialog(null, "Do you really want to delete " +
						textFieldAddCourseName.getText() + "?", "Delete", JOptionPane.YES_NO_OPTION);
				if (action == 0)
				{
					try 
					{
						String query = "delete from " + HandicapMain.courseTableName +
								" where Name='"+textFieldAddCourseName.getText() + "' ";
								
						PreparedStatement pst = sqliteConnection.connection.prepareStatement(query);
						
						pst.execute();
						JOptionPane.showMessageDialog(null, textFieldAddCourseName.getText() + " course deleted");
						
						pst.close();
					}
					catch (Exception e1) 
					{
						e1.printStackTrace();
					}
//					HandicapMain.fillComboBox(HandicapMain.connection, HandicapMain.courseTableName);
					HandicapMain.refreshCourseTable(sqliteConnection.connection, HandicapMain.courseTableName);
				}
//				HandicapMain.coursesDataChanged = true;		// Force reload courses
			}
		});
		GridBagConstraints gbc_btnDelete = new GridBagConstraints();
		gbc_btnDelete.anchor = GridBagConstraints.WEST;
		gbc_btnDelete.insets = new Insets(0, 0, 0, 5);
		gbc_btnDelete.gridx = 1;
		gbc_btnDelete.gridy = 10;
		panelWest.add(btnDelete, gbc_btnDelete);
		
		JPanel panelNorth = new JPanel();
		add(panelNorth, BorderLayout.NORTH);

	}
	
	/**
	 * This method test length of Course name (< 51)
	 * @return
	 */
	private boolean checkCourseNameLength() 
	{
		if (textFieldAddCourseName.getText().length() > MAXCOURSENAMELENGTH)
		{
			JOptionPane.showMessageDialog(null, "Course name must be less than" 
					+ MAXCOURSENAMELENGTH + " characters.",
					"Course name", JOptionPane.WARNING_MESSAGE);
			return false;
		}
		return true;
	}

}
