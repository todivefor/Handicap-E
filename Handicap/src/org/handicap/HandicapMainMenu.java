package org.handicap;

import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import java.awt.BorderLayout;
import java.awt.Image;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;

import java.awt.event.ActionListener;
import java.util.Date;
import java.awt.event.ActionEvent;

public class HandicapMainMenu extends JPanel 
{
/*
 * 	Components referenced elsewhere in other classes	
 */
	public static JButton btnAddScores;
	public static JButton btnDisplayScores;
	public static JButton btnEditCourses;
	
	/**
	 * Create the panel.
	 */
	public HandicapMainMenu() 
	{
		setLayout(new BorderLayout(0, 0));
		
		JPanel panel = new JPanel();
		add(panel, BorderLayout.NORTH);
		
		btnAddScores = new JButton("Add Scores");
		btnAddScores.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) 
			{
//				if (HandicapMain.coursesDataChanged == true)	// Have courses changed
//				{
//					HandicapMain.fillComboBox(HandicapMain.connection,
//							HandicapMain.courseTableName); 		// Load course combo
////					HandicapMain.coursesDataChanged = false;	// Turn off course change			
//				}
				addScore();
				HandicapMain.cards.show(getParent(), HandicapMain.ADDSCORES);
			}
		});
		panel.add(btnAddScores);
		
		btnDisplayScores = new JButton("Display Scores");
		btnDisplayScores.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) 
			{
				DisplayScores.scoreEditingAllowed = true;	// score table display, can edit
				if (DisplayScores.scoreDataChanged)			// Has anything changed?
				{
					boolean tournament = false;
					if (DisplayScores.scoreDataChanged == true)	// Need to rebuild display?
					{
						HandicapMain.refreshScoreTable(sqliteConnection.connection,
								HandicapMain.scoreTableName, tournament);
						DisplayScores.scoreDataChanged = false;	// No need to display until next change
					}
				}
//				if (HandicapMain.coursesDataChanged == true)	// Have courses changed
//				{
//					HandicapMain.refreshCourseTable(HandicapMain.connection, HandicapMain.courseTableName);
////					HandicapMain.coursesDataChanged = false;	// Turn off course change	
//				}
				HandicapMain.cards.show(getParent(), HandicapMain.DISPLAYSCORES);
			}
		});
		panel.add(btnDisplayScores);
		
		btnEditCourses = new JButton("Edit Courses");
		btnEditCourses.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) 
			{
				/*
				 * 
				 * 				This makes course name, rating, and slope select all
				 * 			
				 */

				MaintainCourses.textFieldAddCourseName.addFocusListener(new java.awt.event.FocusAdapter() 
				{
					public void focusGained(java.awt.event.FocusEvent evt) 
					{
						SwingUtilities.invokeLater(new Runnable() 
						{
							@Override
							public void run() 
							{
								MaintainCourses.textFieldAddCourseName.selectAll();
								MaintainCourses.textFieldAddCourseCourseRating.selectAll();
								MaintainCourses.textFieldAddCourseCourseSlope.selectAll();
//								dateChooser.selectAll();
							}
						});
					}
				});

				//				MiscMethods.setTabFocus(tabbedPane, MENUINDEX, ADDCOURSESINDEX);
//				if (HandicapMain.coursesDataChanged == true)	// Have courses changed
//				{
//					HandicapMain.refreshCourseTable(HandicapMain.connection, HandicapMain.courseTableName);
//					HandicapMain.coursesDataChanged = false;	// Turn off course change	
//				}
				HandicapMain.cards.show(getParent(), HandicapMain.MAINTAINCOURSES);
			}
		});
		panel.add(btnEditCourses);
		
		JPanel panel_1 = new JPanel();
		add(panel_1, BorderLayout.SOUTH);
		
		JButton btnExit = new JButton("Exit");
		btnExit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) 
			{
				System.exit(0);
			}
		});
		panel_1.add(btnExit);
		
		JPanel panelPicture = new JPanel();
		add(panelPicture, BorderLayout.CENTER);
		
		JLabel lblOcc = new JLabel("OCC");
		
		/*
		 * 		Add image to main menu
		 * 
		 * 		https://www.youtube.com/watch?v=tFwp589MAFk
		 */
				
				Image imgOCC = new ImageIcon(this.getClass().getResource("/OCC-9.jpg")).getImage();
				lblOcc.setIcon(new ImageIcon(imgOCC));
		
//		lblOcc.setBounds(0, 0, 852, 478);
		panelPicture.add(lblOcc);
		
/*
 * 		End panel creation
 */

	}
	
	public static void addScore() 
	{
		/*
		 * 
		 * 				This makes score, rating, and slope select all
		 * 			
		 */
						AddScores.textFieldScore.addFocusListener(new java.awt.event.FocusAdapter() {
						    public void focusGained(java.awt.event.FocusEvent evt) {
						        SwingUtilities.invokeLater(new Runnable() {
						            @Override
						            public void run() 
						            {
						            	AddScores.textFieldScore.selectAll();
						            	AddScores.textFieldCourseRating.selectAll();
						            	AddScores.textFieldCourseSlope.selectAll();
						            }
						        });
						    }
						});
						
						AddScores.textFieldScore.setText(null);						// Clear out
						AddScores.getCourse((String)AddScores.comboBoxCourse.getItemAt(0)); 	// First entry in course combobox
		
						AddScores.btnAddScoreDelete.setVisible(false);		// Make delete button invisible
						Date now = new Date();
						AddScores.dateChooserAddScoresDate.setDate(now);
//						MiscMethods.setTabFocus(tabbedPane, MENUINDEX, ADDSCORESINDEX);
						AddScores.textFieldScore.requestFocusInWindow();		// set focus to score
//						textFieldScore.setNextFocusableComponent(textFieldCourseRating);
						
//						tabbedPane.removeTabAt(1);       // remove display scores tab
	}

}
