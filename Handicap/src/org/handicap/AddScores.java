package org.handicap;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.JPanel;
import javax.swing.JTextField;

import org.iconutils.IconUtils;
import org.todivefor.string.utils.StringUtils;

import com.toedter.calendar.JDateChooser;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

public class AddScores extends JPanel 
{
	GridBagConstraints constraints = new GridBagConstraints();
	
/*
 * 	Components referenced elsewhere
 */
	public static JTextField textFieldScore;
	public static JTextField textFieldCourseRating;
	public static JTextField textFieldCourseSlope;
	public static JComboBox<String> comboBoxCourse;
	public static JCheckBox chckbxAddScoresNineHoleScore;
	public static JCheckBox chckbxAddScoresTournamentScore;
	public static JButton btnAddScoresAdd;
	public static JButton btnAddScoreDelete;
	public static JDateChooser dateChooserAddScoresDate;						// <date change>
	
	public static boolean fillingCourseCombobox = true;							// Filling initial combobox
	int x, y;  // GridBag for clarity

	/**
	 * Create the panel.
	 */
	public AddScores() 
	{
		
		setLayout(new BorderLayout(0, 0));
		
		JPanel panelWest = new JPanel();
		add(panelWest, BorderLayout.CENTER);
		GridBagLayout gbl_panel = new GridBagLayout();
		panelWest.setLayout(gbl_panel);
		constraints.insets = new Insets(20, 20, 0, 0);
		constraints.anchor = GridBagConstraints.LINE_START;
/*
 * 		Row 0 - Score
 */
		JLabel lblNumberScores = new JLabel("Score");
		constraints.weightx = 0.0;
		addGB(panelWest, lblNumberScores, x = 0, y = 0);

		textFieldScore = new JTextField(3);
		constraints.weightx = 0.0;
		addGB(panelWest, textFieldScore, x = 1, y = 0);
		
		chckbxAddScoresNineHoleScore = new JCheckBox("Nine Hole Score");
		constraints.weightx = 0.0;
		addGB(panelWest, chckbxAddScoresNineHoleScore, x = 2, y = 0);
		
		chckbxAddScoresTournamentScore = new JCheckBox("Tournament Score");
		constraints.weightx = 0.0;
		addGB(panelWest, chckbxAddScoresTournamentScore, x = 3, y = 0);
/*
 * 		Row 1 - Date
 */
		JLabel lblDate = new JLabel("Date");
		constraints.weightx = 0.0;
		addGB(panelWest, lblDate, x = 0, y = 1);
		
		dateChooserAddScoresDate = new JDateChooser();
		dateChooserAddScoresDate.setDateFormatString("MM/dd/yy");
		constraints.weightx = 0.0;
		addGB(panelWest, dateChooserAddScoresDate, x = 1, y = 1);
/*
 * 		Row 2 - Course
 */		
		JLabel lblCourse = new JLabel("Course");
		constraints.weightx = 0.0;
		addGB(panelWest, lblCourse, x = 0, y = 2);
		
		comboBoxCourse = new JComboBox<String>();
		
		comboBoxCourse.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) 
			{
				/*
				 * 
				 * 
				 * 	If this event was activated due to filling combobox, do nothing
				 * 	Only do anything if combobox has been clicked
				 * 
				 */
				
				if (fillingCourseCombobox)
				{
//					System.out.println("Reached it filling combo");
				}
				else
				{
//					System.out.println("Reached it action performed");
					getCourse((String)comboBoxCourse.getSelectedItem());	// Lookup course, then rating and slope
				}
			}
		});
		
		constraints.weightx = 0.0;
		addGB(panelWest, comboBoxCourse, x = 1, y = 2);
/*
 * 		Row 3 - Rating		
 */
		JLabel lblCourseRating = new JLabel("Course Rating");
		constraints.weightx = 0.0;
		addGB(panelWest, lblCourseRating, x = 0, y = 3);
		
		textFieldCourseRating = new JTextField(3);
		constraints.weightx = 0.0;
		addGB(panelWest, textFieldCourseRating, x = 1, y = 3);
/*
 * 		Row 4 - Slope	
 */
		JLabel lblCourseSlope = new JLabel("Course Slope");
		constraints.weightx = 0.0;
		addGB(panelWest, lblCourseSlope, x = 0, y = 4);
		
		textFieldCourseSlope = new JTextField(3);
		constraints.weightx = 0.0;
		addGB(panelWest, textFieldCourseSlope, x = 1, y = 4);
/*
 * 		Row 5 - Add button		
 */
		btnAddScoresAdd = new JButton("Add");
		
		btnAddScoresAdd.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) 
			{
				boolean success = false;
				String nineHoleHang = HandicapMain.handicapPrefs.get(HandicapMain.HANDICAPNINEHOLE,
						HandicapMain.NONH);
				
/*
 * 				*******	
 * 				This code does not allow an update to the 9 hole indicator
 * 				that will cause 2 nine hole scores to be combined (ie change
 * 				indicator to on with a 9 hole score hanging).
 * 
 * 				This code can be reviewed to allow a 9 hole combination to occur when
 * 				maintenance is applied to change the indicator (ie turn indicator on
 * 				with a 9 hole score hanging.  You would have to combine scores and
 * 				then delete the remaining score.
 */
				if ((btnAddScoresAdd.getText().equals("Update")) &&
						((chckbxAddScoresNineHoleScore.isSelected()) &&
								(!HandicapMain.inicatorTournOrNineOnDB.equals(HandicapMain.NINEINDICATOR)) &&
								(!nineHoleHang.equals(HandicapMain.NONH))))
					JOptionPane.showMessageDialog(null, "You are trying to make a change to nine hole score indicator, "
							+ "that will caus a combination, delete and then add to accomplish");
					else
					{

						/*
						 * 				Assume this is a straight "Add"
						 */
						String query = "insert into " + HandicapMain.scoreTableName + " (DateField, Course, T, Score,"
								+ " Rating, Slope, Differential)"
								+ " values(?, ?, ?, ?, ?, ?, ?)";

						/*
						 * 				If an update or 9 hole selected and a 9 hole score hanging,
						 * 					do an update
						 */

						if ((btnAddScoresAdd.getText().equals("Update")) || 
								(chckbxAddScoresNineHoleScore.isSelected()) && (!nineHoleHang.equals(HandicapMain.NONH)))
						{
							query = "update " + HandicapMain.scoreTableName + " SET DateField = ?, Course = ?,"
									+ " T = ?, Score = ?, Rating = ?,"
									+ " Slope = ?, Differential = ? WHERE DateField = ?";
						}

						try 
						{
							success = addOrUpdateDBRow(query);
						}
						catch (SQLException e1) 
						{
							e1.printStackTrace();
						}

						if (success)
						{
							if (HandicapMain.isDebug())
								JOptionPane.showMessageDialog(null, "Data updated");
						}
						else
						{
							textFieldScore.requestFocusInWindow();		// set focus to score
							return;										// Invalid score entered
						}
					}

				DisplayScores.scoreDataChanged = true;								// Force score redisplay
				btnAddScoresAdd.setText("Add");										// change button back to Add in Add Score
				btnAddScoreDelete.setVisible(false);								// Make delete button not visible
				chckbxAddScoresTournamentScore.setSelected(false);					// Set tournament off
				chckbxAddScoresNineHoleScore.setSelected(false);					// Set nine hole off
//				MiscMethods.setTabFocus(tabbedPane, ADDSCORESINDEX, DISPLAYSCORESINDEX);
			}

		});
		
		constraints.anchor = GridBagConstraints.SOUTHWEST;
		constraints.insets = new Insets(60, 0, 0, 0);
		constraints.weightx = 0.0;
		addGB(panelWest, btnAddScoresAdd, x = 0, y = 5);
		
		/*
		 * 		Row 6 - Delete button		
		 */
				btnAddScoreDelete = new JButton("Delete");
				
				
				
				constraints.anchor = GridBagConstraints.SOUTHWEST;
				constraints.insets = new Insets(20, 0, 0, 0);
				constraints.weightx = 0.0;
				addGB(panelWest, btnAddScoreDelete, x = 0, y = 6);		
		
		JPanel panelSouth = new JPanel();
		add(panelSouth, BorderLayout.SOUTH);
		
		JButton btnAddScoresExit = new JButton("Back",
				IconUtils.getNavigationIcon("Back", 24));
		
		btnAddScoresExit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) 
			{
				HandicapMain.cards.show(getParent(), HandicapMain.MAINMENU);
			}
		});
		panelSouth.add(btnAddScoresExit);
	}
	
	  void addGB(JPanel addToPanel, Component component, int x, int y) 
	  {
	    constraints.gridx = x;
	    constraints.gridy = y;
	    addToPanel.add(component, constraints);
	  }
	  
		boolean addOrUpdateDBRow(String query) throws SQLException 
		{
			PreparedStatement pst = sqliteConnection.connection.prepareStatement(query);
			String dateIt = ((JTextField)dateChooserAddScoresDate.getDateEditor().getUiComponent()).getText();
			
//			int day = dateChooser.getCalendar().get(Calendar.DAY_OF_MONTH);
//			int month = dateChooser.getCalendar().get(Calendar.MONTH) + 1;			// Jan - 0, Feb - 1, ... Dec - 11
//			int year = dateChooser.getCalendar().get(Calendar.YEAR);
			
//			String dateIt = MiscMethods.dateToString(day, month, year);
			pst.setString(1, HandicapMain.convertTableDate(dateIt));
			
			pst.setString(2, (String)comboBoxCourse.getSelectedItem());
				
	/*
	* 
	* 		Test first to see if tournament or 9 hole score is set
	* 		If neither, set indicator in row off.
	* 			Test for 9 hole
	* 				else test for tournament
	* 
	*/
			if (!(chckbxAddScoresNineHoleScore.isSelected() || chckbxAddScoresTournamentScore.isSelected()))
			{
				pst.setString(3, "");										// Turn off, nothing special
				if ((HandicapMain.inicatorTournOrNineOnDB != null) &&
						(HandicapMain.inicatorTournOrNineOnDB.equals(HandicapMain.NINEINDICATOR)))				// Was 9 set in DB recored?
				{
					HandicapMain.handicapPrefs.remove(HandicapMain.HANDICAPNINEHOLE); 				// Yes so we must be turning it off
					HandicapMain.inicatorTournOrNineOnDB = null;							// Turn off to be safe
				}
			}
			else			// 9 hole score?
			{
				if (chckbxAddScoresNineHoleScore.isSelected())				// 9 hole score
				{			
					String nineHoleHang = HandicapMain.handicapPrefs.get(HandicapMain.HANDICAPNINEHOLE,
							HandicapMain.NONH);	
					if (nineHoleHang.equals(HandicapMain.NONH))								// 9 hole score hanging?
					{
						pst.setString(3, HandicapMain.NINEINDICATOR);									// No - set in table
						HandicapMain.handicapPrefs.put(HandicapMain.HANDICAPNINEHOLE, "yes");	// Set hanging 9 hole score
					}
					else														// 2nd nine hole score, must combine
					{
						int newNineHole = Integer.parseInt(textFieldScore.getText());			// Score just added
						
						combineNineHoleScores();									// Combine 2 9 hole scores
						
	/*
	 * 					Create combined course name (Combined, first nine date, first score, second score)
	 */
						String combinedName = "Combined " + DisplayScores.saveDate + " " +
									(Integer.parseInt(textFieldScore.getText()) - newNineHole) + " " +
									newNineHole; 													// Combined course name
						pst.setString(2, combinedName);								// Set course to combined
						pst.setString(3, "");										// Turn off 9 hole score indicator
						HandicapMain.handicapPrefs.remove(HandicapMain.HANDICAPNINEHOLE);			// Set 9 hole score combined
					}

				}
				else		// must be tournament
				{
//						chckbxAddScoresTournamentScore.setSelected(false); 		// Deselect tournament score
						pst.setString(3, HandicapMain.TOURNINDICATOR);
				}
			}
			

			
			String strScore, strCourseRating, strCourseSlope;
			
			pst.setString(4, strScore=textFieldScore.getText());		// Get score
			
			//	Test to be sure score entered is valid
			
			if (!StringUtils.isInteger(strScore))
			{
				JOptionPane.showMessageDialog(null, "Score \" " + strScore +
						"\"  is not a valid number, renenter.",
						"Score", JOptionPane.ERROR_MESSAGE);
				return false;
			}
			
			pst.setString(5, strCourseRating = textFieldCourseRating.getText());
			pst.setString(6, strCourseSlope = textFieldCourseSlope.getText());
			
			pst.setString(7, calcDiffernential(strScore, strCourseRating, strCourseSlope));		// Calculate differential
			
//			pst.setString(8, dateIt);
			if (query.contains("update"))						// Update
			{
				pst.setString(8, DisplayScores.saveDate);				// Get date from display score for update
			}

			try 
			{
				pst.execute();
			}
			catch (Exception e) 
			{
				JOptionPane.showMessageDialog(null, "Duplicate score entry", "Duplicate", JOptionPane.ERROR_MESSAGE);
				if (HandicapMain.isDebug())
					e.printStackTrace();						// If debug, show trace
				return false;
			}
			pst.close();
			return true;
		}

		/**
		 * 	This method calculates an individual score differential as follows
		 * 	differential = (score - course rating) * (113 / course slope)
		 * @param String strScore
		 * @param String strCourseRating
		 * @param String strCourseSlope
		 * @return String strDifferential
		 */

			private String calcDiffernential(String strScore, String strCourseRating, String strCourseSlope) 
			{
				final int BASESLOPE = 113;
				String strDifferential = null;
				int intScore, intCourseSlope;
				double dblCourseRating, dblDifferential;
				
				intScore = Integer.parseInt(strScore);
				dblCourseRating = Double.parseDouble(strCourseRating);
				intCourseSlope = Integer.parseInt(strCourseSlope);
				
				dblDifferential = ((intScore - dblCourseRating) * BASESLOPE) / intCourseSlope;			// Calculate differential
				
				strDifferential = String.format("%04.1f", dblDifferential);
				
				return strDifferential;
			}

			/**
			 * 	This method combines 2 nine hole scores as follows
			 * 		Save the date, score, rating, and slope
			 * 		Select from score table based on "9" in T column
			 * 			Add 2 scores
			 * 			Add 2 ratings
			 * 			Add 2 slopes / 2
			 * 			Put in textfileds and return
			 * 			Let process as an update to 1st 9 hole score
			 */

		private void combineNineHoleScores() 
		{
			boolean haveNineHoleScore = false;				// Assume no 9 hole scores hanging
			int saveNineHoleScore = Integer.parseInt(textFieldScore.getText());							// 9 hole score just added
			double saveNineHoleRating = Double.parseDouble(textFieldCourseRating.getText());			// 9 hole rating just added
			int saveNineHoleSlope = Integer.parseInt(textFieldCourseSlope.getText());					// 9 hole slope just added
			try 
			{
				String T_ = HandicapMain.NINEINDICATOR;					// Look for 9 hole score	
				String query = "Select * from " + HandicapMain.scoreTableName + " where T = '"+T_+"'";
				PreparedStatement pst = sqliteConnection.connection.prepareStatement(query);
				ResultSet rs = pst.executeQuery();
				while(rs.next())
				{
					haveNineHoleScore = true;									// We have a 9 hole score
					DisplayScores.saveDate = rs.getString("Date");				// Save date of orig score, will set new date
//					dateChooser.setDate(MiscMethods.convertDate(getSaveDate()));
					textFieldScore.setText(rs.getString("Score"));	
					textFieldCourseRating.setText(rs.getString("Rating"));
					textFieldCourseSlope.setText(rs.getString("Slope"));

				}
				rs.close();					
				pst.close();
			}
			catch (Exception e1) 
			{
				e1.printStackTrace();
			}
			if (!haveNineHoleScore)
			{
				JOptionPane.showMessageDialog(null, "Houston, we have a problem");		// Should not happen
				System.exit(99);														// Hanging 9 hole, but none in table
			}
			textFieldScore.setText(Integer.toString(saveNineHoleScore + Integer.parseInt(textFieldScore.getText())));
			textFieldCourseRating.setText(Double.toString(saveNineHoleRating + Double.parseDouble(textFieldCourseRating.getText())));	
			textFieldCourseSlope.setText(Integer.toString((saveNineHoleSlope + Integer.parseInt(textFieldCourseSlope.getText()))/2));
		}
		
		/**
		 * 	getCourse method fills the rating and slope from the course table
		 * 	on the add scores screen
		 * @param course
		 */

			public static void getCourse(String course) 
			{
				try 
				{
					
					String query = "Select * from " + HandicapMain.courseTableName + " where Name = ?";
					PreparedStatement pst = sqliteConnection.connection.prepareStatement(query);
					pst.setString(1, course);  									//returns object that needs converted to string
					ResultSet rs = pst.executeQuery();
					
					while(rs.next())
					{
						textFieldCourseRating.setText(rs.getString("Rating"));
						textFieldCourseSlope.setText(rs.getString("Slope"));
					}
					rs.close();					
					pst.close();
				}
				catch (Exception e1) 
				{
					e1.printStackTrace();
				}
			}
			
}
