package org.handicap;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import javax.swing.JButton;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

import org.iconutils.IconUtils;
import org.todivefor.string.utils.StringUtils;

import net.proteanit.sql.DbUtils;

import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.awt.event.ActionEvent;
import javax.swing.JTable;
import javax.swing.JScrollPane;

public class DisplayScores extends JPanel 
{
	GridBagConstraints constraints = new GridBagConstraints();
	
	/*
	 * 	Components referenced elsewhere in other classes	
	 */
	public static JTable tableDisplayScores;
	public static JTextField textDisplayScoresIndex;
	public static JTextField textDisplayScoresIndexAdj;
	public static JTextField textDisplayScoresNumberScores;
	public static JTextField textDisplayScoresAverageScore;
	
	public static boolean scoreEditingAllowed = false;			// Don't allow edit on tournament display
	public static boolean scoreDataChanged = true;				// Need to recalc index?
	
	public static String saveDate;								// Save score date

	/**
	 * Create the panel.
	 */
	public DisplayScores() 
	{
		setLayout(new BorderLayout(0, 0));
		
//		JPanel panelNorth = new JPanel();
//		add(panelNorth, BorderLayout.NORTH);
		
		JScrollPane scrollPane = new JScrollPane();
		add(scrollPane, BorderLayout.NORTH);
		
		tableDisplayScores = new JTable();
		
		tableDisplayScores.setShowGrid(false);
		tableDisplayScores.addMouseListener(new MouseAdapter() {
		
			@Override
			public void mouseClicked(MouseEvent e) 
			{
				try 
				{					//  <date change>
					if (!scoreEditingAllowed)								// Are we in an archive / tournament display
					{
						JOptionPane.showMessageDialog(null, "Editing is not allowed");
						return;
					}
					
					int row = tableDisplayScores.getSelectedRow();
					String DATE_ = tableDisplayScores.getModel().getValueAt(row,
							HandicapMain.DATE_POS).toString();
					String ymdDate = HandicapMain.convertTableDate(DATE_);		// Convert display date to table date
																				// MM/dd/yy -> yyyy-MM-dd
					
//					
					String query = "Select * from " + HandicapMain.scoreTableName +
							" where DateField = '"+ymdDate+"'";
//					String query = "Select * from " + scoreTableName + " where Date = '"+DATE_+"'";
					PreparedStatement pst = sqliteConnection.connection.prepareStatement(query);
					ResultSet rs = pst.executeQuery();
					
					while(rs.next())
					{
//						dateChooser = getDateChooser();
						
						saveDate = rs.getString("DateField");				// Save off date in case we are changing it
						// convert to date
						AddScores.dateChooserAddScoresDate.setDate(convertDate(saveDate));	// <date change>
//						dateChooser.setDateFormatString(getSaveDate());		// Date in field
						
						AddScores.comboBoxCourse.setSelectedItem(rs.getString("Course"));
						
						AddScores.textFieldScore.setText(rs.getString("Score"));
						AddScores.textFieldCourseRating.setText(rs.getString("Rating"));
						AddScores.textFieldCourseSlope.setText(rs.getString("Slope"));
						AddScores.btnAddScoresAdd.setText("Update");			// change button to update in Add Score
						AddScores.btnAddScoreDelete.setVisible(true);			// Make delete button visible
						
		/*
		* 
		* 						inicatorTournOrNineOnDB indicates T or 9 set in DB record
		* 							We use this to set respective chckbox indicator here
		* 							We will also use it in "delete" or "update" routines to 
		* 								determine if changed (ie on to off or off to on)
		* 						
		*/
						
						HandicapMain.inicatorTournOrNineOnDB = rs.getString(HandicapMain.TOURNINDICATOR);					// Tournament or 9 hole
						if (HandicapMain.inicatorTournOrNineOnDB != null)							// null causes probs with switch
						{
							switch (HandicapMain.inicatorTournOrNineOnDB)
							{
							case HandicapMain.TOURNINDICATOR:
								AddScores.chckbxAddScoresTournamentScore.setSelected(true);	// Set tournament
								break;
								
							case HandicapMain.NINEINDICATOR:
								AddScores.chckbxAddScoresNineHoleScore.setSelected(true);		// Set 9 hole
								break;
							}
						}
						
						
						AddScores.textFieldScore.addFocusListener(new java.awt.event.FocusAdapter() {
						    public void focusGained(java.awt.event.FocusEvent evt) {
						        SwingUtilities.invokeLater(new Runnable() {
						            @Override
						            public void run() {
						            	AddScores.textFieldScore.selectAll();
						            	AddScores.textFieldCourseRating.selectAll();
						            	AddScores.textFieldCourseSlope.selectAll();
//						            	dateChooser.selectAll();
						            }
						        });
						    }
						});
						
//						MiscMethods.setTabFocus(tabbedPane, DISPLAYSCORESINDEX, ADDSCORESINDEX);
					}
					rs.close();					
					pst.close();
				}
				catch (Exception e1) 
				{
					e1.printStackTrace();
				}
//				AddScores.textFieldScore.requestFocusInWindow();		// set focus to score
				HandicapMain.cards.show(getParent(), "AddScores");
			}
		});
		
		scrollPane.setViewportView(tableDisplayScores);
		
		JPanel panelCenter = new JPanel();
		add(panelCenter, BorderLayout.CENTER);
		
		@SuppressWarnings("unused")
		int x, y;  // for clarity
		
		GridBagLayout gbl_panel = new GridBagLayout();
//		gbl_panel.rowWeights = new double[]{1.0, 0.0, 0.0, 0.0};
//		gbl_panel.columnWeights = new double[]{0.0, 0.0, 0.0, 1.0};
		panelCenter.setLayout(gbl_panel);
		
		constraints.insets = new Insets(0, 20, 0, 0);
		constraints.anchor = GridBagConstraints.LINE_START;
/*
 * 		Row 0 - Number Scores		
 */
		JLabel lblNumberScores = new JLabel("Number Scores");
		addGB(panelCenter, lblNumberScores, x = 0, y = 0);
		
//		constraints.insets = new Insets(0, 0, 0, 0);
		textDisplayScoresNumberScores = new JTextField(3);
		textDisplayScoresNumberScores.setEditable(false);
		addGB(panelCenter, textDisplayScoresNumberScores, x = 1, y = 0);
/*
 * 		Row 1 - Index 
 */
//		constraints.insets = new Insets(0, 0, 0, 0);
		JLabel lblIndex = new JLabel("Index");
		constraints.anchor = GridBagConstraints.WEST;
		addGB(panelCenter, lblIndex, x = 0, y = 1);
		
//		constraints.insets = new Insets(0, 0, 0, 0);
		textDisplayScoresIndex = new JTextField(4);
		textDisplayScoresIndex.setEditable(false);
		addGB(panelCenter, textDisplayScoresIndex, x = 1, y = 1);
		
//		constraints.insets = new Insets(0, 0, 0, 0);
		textDisplayScoresIndexAdj = new JTextField(4);
		textDisplayScoresIndexAdj.setEditable(false);
		addGB(panelCenter, textDisplayScoresIndexAdj, x = 2, y = 1);
		
//		constraints.insets = new Insets(0, 0, 0, 0);
		JButton btnCalculateCourseHandicap = new JButton("Calculate Course Handicaps");
		
		btnCalculateCourseHandicap.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) 
			{
				scoreEditingAllowed = false;		// Don't allow editing of scores
				String hIndexStr = textDisplayScoresIndex.getText();			// Get index from text
				if (hIndexStr.equals("NH"))
				{
					JOptionPane.showMessageDialog(null, "Not enough scores to calculate course handicaps");
					scoreDataChanged = true;									// Force redisplay of scores
					return;														// Get out of here
				}
				try 
				{
					String query = "select Name, Rating, Slope, Rating as 'Course Handicap' from " +
							HandicapMain.courseTableName + " Order by Name";

					PreparedStatement pst = sqliteConnection.connection.prepareStatement(query);
					ResultSet rs = pst.executeQuery();
					tableDisplayScores.setModel(DbUtils.resultSetToTableModel(rs));

					rs.close();
					pst.close();

				}
				catch (Exception e1) 
				{
					e1.printStackTrace();
				}
				/*
				 * 				Loop through tableDisplayScores (actually courses)
				 * 				calculate course handicap and put in third column
				 * 
				 * 				0 - Course, 1 - Rating, 2 - Slope, 3 - rating where 
				 * 				we will display course handicap
				 */

				double hIndex = Double.parseDouble(textDisplayScoresIndex.getText());		// Index
				String hIndexAdjStr = textDisplayScoresIndexAdj.getText();					// Adjusted index
				if (hIndexAdjStr.length() != 0)
				{
					hIndexAdjStr = hIndexAdjStr.substring(0, hIndexAdjStr.length() - 1);	// Strip off R
					if (StringUtils.isStringNumeric(hIndexAdjStr))
					{
						hIndex = Double.parseDouble(hIndexAdjStr);			// Use adjusted	
					}
				}
				
				int lastRow = tableDisplayScores.getRowCount();				// Rows in table
				for (int row = 0; row < lastRow; row++)						// Loop thru all courses
				{
					int courseSlope = 0;
					courseSlope = (int) tableDisplayScores.getModel().
							getValueAt(row, 2);								// Slope
					
					double courseSlopeD = courseSlope;
					double courseIndex = (hIndex * courseSlopeD) / 113F;
					int courseIndexI = (int)Math.floor(courseIndex + .5F);		// Round to whole number
					tableDisplayScores.setValueAt(String.valueOf(courseIndexI), row, 3);
				}
				HandicapMain.renderColumns(tableDisplayScores);				// Set column sizes for table tableDisplayCourses
				scoreDataChanged = true;						// Force redisplay of scores
			}
		});
		
		addGB(panelCenter, btnCalculateCourseHandicap, x = 3, y = 1);
/*
 * 		Row 2 - Average Scores
 */
//		constraints.insets = new Insets(0, 0, 0, 0);
		JLabel textDisplayScorbtnAverage = new JLabel("Average Score");
		addGB(panelCenter, textDisplayScorbtnAverage, x = 0, y = 2);
		
//		constraints.insets = new Insets(20, 20, 0, 0);
		textDisplayScoresAverageScore = new JTextField(3);
		textDisplayScoresAverageScore.setEditable(false);
		addGB(panelCenter, textDisplayScoresAverageScore, x = 1, y = 2);
		
		JPanel panelSouth = new JPanel();
		add(panelSouth, BorderLayout.SOUTH);
		
		JButton btnDisplayScoresExit = new JButton("Back",
				IconUtils.getNavigationIcon("Back", 24));
		btnDisplayScoresExit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) 
			{
				HandicapMain.cards.show(getParent(), "MainMenu");
			}
		});
		panelSouth.add(btnDisplayScoresExit);
	}
	
	  void addGB(JPanel addToPanel, Component component, int x, int y) 
	  {
	    constraints.gridx = x;
	    constraints.gridy = y;
	    addToPanel.add(component, constraints);
	  }
	  
	  /**
	   * This method converts String date to Date format
	   * 
	   * @param String (yyyy-MM-dd)
	   * @return Date date
	   */
	  	
	  	private static java.util.Date convertDate (String dateInString)
	  	{
	  		java.util.Date date = null;
	  		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
//	  		SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yy");				// <date change>
	  		try 
	  		{
	  			date = formatter.parse(dateInString);
	  			if (HandicapMain.isDebug())
	  			{
	  				System.out.println("String Date in: " + dateInString);
	  				System.out.println("Date Date after parsing: " + date);
	  				System.out.println("String Date as returned: " + formatter.format(date));
	  			}
	  		} 
	  		catch (ParseException e) 
	  		{
	  			e.printStackTrace();
	  		}
	  		return date;
	  	}

}



