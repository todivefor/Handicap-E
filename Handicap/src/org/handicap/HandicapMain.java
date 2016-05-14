package org.handicap;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Component;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.prefs.BackingStoreException;
import java.util.prefs.Preferences;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.plaf.metal.DefaultMetalTheme;
import javax.swing.plaf.metal.MetalLookAndFeel;
import javax.swing.plaf.metal.OceanTheme;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;

import org.iconutils.IconUtils;
import org.todivefor.string.utils.StringUtils;

import net.proteanit.sql.DbUtils;

import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

public class HandicapMain extends JFrame
{
/*
 * 	Components referenced from other classes
 */
//	private JPanel contentPane;
	public static JMenu mnDebug;
	
	public static CardLayout cards = new CardLayout();
	
	public static boolean debug = true;					// Debug mode	<debug>
	final static boolean cTime = false;					// Compile time debug  <debug>
	private boolean pathSet = false;					// Created DB?
	private static String dbPath = null;				// Path to DB
	
	public static boolean fillingLookAndFeelComboBox = true;	// Am I filling the combobox?
	public static String saveCourseName;						// Save course
	
	private static String userName = null;				// Who this is
	
	public static String operatingSystem;				// OS
	
//	public static Connection connection;				// DB connection
	
	public static Preferences handicapPrefs;			// preferences object
	
//	Preference file constants
	
	final static String HANDICAPDB = "DBFile";									// DB field containing path to handicap.sqlite field ID
	final static String NODB = "NODB";												// DB has not been initialized
	final static String HANDICAPSCORETABLENAME = "SCORETABLE";					// Preference - SCORE table name field ID
	final static String NOST = "NOST";												// Score table not initialized 
	final static String HANDICAPCOURSETABLENAME = "COURSETABLE";				// Preference - COURSE table name field ID
	final static String NOCT = "NOCT";												// Course table not initialized 
	final static String HANDICAPUSERNAME = "USERNAME";							// Preference - USER name field ID
	final static String NOUN = "NOUN";												// Tournament table not initialized 
	final static String HANDICAPNINEHOLE = "NINEHOLE";							// 9 hole table position
	final static String NONH = "NONH";												// 9 hole not initialized
	final static String	HANDICAPDEBUG = "DEBUG";								// Debug
	final static boolean NODG = false;												// Debug not initialized
	final static String HANDICAPLOOKANDFEEL = "LOOKANDFEEL";					// Look and feel
	final static String NOLF = "NOLF";												// Look and feel not initialized
	final static String HANDICAPTHEME = "THEME";								// Theme if metal look and feel
	final static String NOTH = "NOTH";												// Theme not initialized
	
	public static String inicatorTournOrNineOnDB;						// T or 9 indicator on DB
	
//	CARDS
	
	final static String MAINMENU = "MainMenu";
	final static String MAINTAINCOURSES = "MaintainCourses";
	final static String DISPLAYSCORES = "DisplayScores";
	final static String ADDSCORES = "AddScores";
	final static String PREFERRENCES = "Preferrences";
	
// 	SCORE table column position constants
	
	final static int DATE_POS = 0;							// Date column
	final static int COURSE_POS = 1;						// Course column
	final static int T_POS = 2;								// T column
	final static int SCORE_POS = 3;							// Score column
	final static int U_POS = 4;								// U column
	final static int RATING_POS = 5;						// Rating column
	final static int SLOPE_POS = 6;							// Slope column
	final static int DIFFERENTIAL_POS = 7;					// Differential column
	
	// Miscellaneous constants
	
	final static int MAXCOURSENAMELENGTH = 50;				// max course name length
	final static String TOURNINDICATOR = "T";				// Tournament indicator
	final static String NINEINDICATOR = "9";				// Nine hole indicator
	
	public static String scoreTableName = null;
	public static String courseTableName = null;
	
	// Specify the look and feel to use by defining the LOOKANDFEEL constant
	// Valid values are: null (use the default), "Metal", "System", "Motif", Windows
	// and "GTK"
	final static String LOOKANDFEEL = "Metal";						//  Default look and feel

	// If you choose the Metal L&F, you can also choose a theme.
	// Specify the theme to use by defining the THEME constant
	// Valid values are: "DefaultMetal", "Ocean",  and "Test"

	//final static String THEME = "DefaultMetal";
	final static String THEME = "Ocean";							// Default theme for metal
	public static String lookAndFeel = null;						// Set to nothing
	
	public HandicapMain() 
	{
//		JFrame mainFrame = new JFrame("Handicap");
		super("Handicap");
//		this.setTitle("Handicap");

/*
 * 		Initialization
 */
		
		/*
		 * 
		 * 	TODO	***** Load all Preferences *****
		 * 
		 * Preferences 
		 * MAC OS X
		 * 		~/library/Preferences/com.apple.java.util.prefs.plist
		 * Windows
		 * 		Registry - HKEY_CURRENT_USER\SOFTWARE\JavaSoft\Prefs
		 * 			It produces a warning
		 * 
		 * 
		 */
		
		handicapPrefs = Preferences.userRoot().node(this.getClass().getName());
		dbPath = handicapPrefs.get(HANDICAPDB, NODB);					// Path in preferences
		debug = handicapPrefs.getBoolean(HANDICAPDEBUG, NODG);			// Debug in preferences
		
		operatingSystem = System.getProperty("os.name");				// Get OS
		if (isDebug())
		{
			System.out.println(operatingSystem);						// <debug>
		}
		
		scoreTableName = handicapPrefs.get(HANDICAPSCORETABLENAME, NOST);	// SCORE table
		courseTableName = handicapPrefs.get(HANDICAPCOURSETABLENAME, NOCT);	// COURSE table
		userName = handicapPrefs.get(HANDICAPUSERNAME, NOUN); 
		// User Name
		
		if (dbPath.equals(NODB))					// Have we setup DB?
		{
			setPathSet(false);						//  Handicap DB has not been initialized
		}
		else
		{
			setPathSet(true);						// Handicap DB has been initialized
		}

		debug = handicapPrefs.getBoolean(HANDICAPDEBUG, NODG);	// Set debug from prefs

		/*		set look and feel.  Need to figure this out
		 * 		this can be set in first frame called
		 */

		lookAndFeel = handicapPrefs.get(HANDICAPLOOKANDFEEL, NOLF);	// Get look and feel
		String theme = handicapPrefs.get(HANDICAPTHEME, NOTH);		// Get theme if metal
		if (lookAndFeel.equals(NOLF))	
			initLookAndFeel(LOOKANDFEEL, THEME);					// Pass default Metal, Ocean
		else
			initLookAndFeel(lookAndFeel, theme);					// Pass from preferences
		
/*
 * 		End of initialization
 */

		/**
		 * Create the frame.
		 */
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//				   X   Y   W    H	
		setBounds(200, 0, 750, 700);
//		setBounds(150, 0, 800, 800);
		
		JMenuBar menuBar = new JMenuBar();
		setJMenuBar(menuBar);
		
		JMenu mnHandicap = new JMenu("Handicap");
		menuBar.add(mnHandicap);
		
		JMenuItem mntmAbout = new JMenuItem("About",
				IconUtils.getGeneralIcon( "About", 24 ));
		mnHandicap.add(mntmAbout);
		
		JMenuItem mntmPreferrences = new JMenuItem("Preferrences",
				IconUtils.getGeneralIcon( "Preferences", 24 ));
		mntmPreferrences.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) 
			{
				HandicapMain.cards.show(getContentPane(), PREFERRENCES);
			}
		});
		mnHandicap.add(mntmPreferrences);
		
		JMenuItem mntmExiHandicap = new JMenuItem("Exit Handicap");
		mntmExiHandicap.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) 
			{
				if (isPathSet())
				{
					closeHandicapDB(sqliteConnection.connection);
				}
				System.exit(0);;
			}

		});
		mnHandicap.add(mntmExiHandicap);
		
		JMenu mnFile = new JMenu("File");
		menuBar.add(mnFile);
		
		JMenuItem mntmNewDb = new JMenuItem("New DB",
				IconUtils.getGeneralIcon( "New", 24 ));
		
		mntmNewDb.addActionListener(new ActionListener() 
		{
			public void actionPerformed(ActionEvent e) 
			{
				Preferrences.fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);	// Only look at directories
				int returnVal = Preferrences.fc.showOpenDialog(null);
				if (returnVal == JFileChooser.APPROVE_OPTION) 
	    		{
//	    			File file = fc.getSelectedFile();
	    			//This is where a real application would open the file.
	    			
					String path = Preferrences.fc.getSelectedFile().getAbsolutePath();

					/*
					 * 	Set buttons on main menu
					 */
					
					String hDBName = JOptionPane.showInputDialog("Enter the name of the new handicap DB");
					path = path + "/" + hDBName + ".sqlite";
					
					if (isPathSet())								// First time thru?
					{
						sqliteConnection.connection = sqliteConnection.dbConnector(path);	// Open the new DB
						HandicapMainMenu.btnAddScores.setVisible(true);
						HandicapMainMenu.btnDisplayScores.setVisible(true);
						HandicapMainMenu.btnEditCourses.setVisible(true);
						setPathSet(true);										// Handicap DB has been initialized
						sqliteConnection.connection = sqliteConnection.dbConnector(dbPath);		// Open handicap DB
						
//																				// New DB
//						recalcHandicapIndex = true;								// Must force redisplay scores
//						needToReloadCourseComboBox = true;						// Must force reload course combo
						
						// TODO should this be a load or just a need to load switch
//						refreshCourseTable(connection, courseTableName);		// Fill initial course table
//						MiscMethods.fillComboBox(connection, courseTableName);				// Fill courses combo box
					}
					else													// New
					{
						closeHandicapDB(sqliteConnection.connection); 			// DB open so, close it
						handicapPrefs.remove(HANDICAPDB);	 				// Remove path from prefs
						sqliteConnection.connection = sqliteConnection.dbConnector(path);	// Open the new DB
					}
					userName = JOptionPane.showInputDialog("Your first name");
					handicapPrefs.put(HANDICAPUSERNAME, userName);
					createScoreTable(userName);								// Create SCORE table
					createCourseTable(userName);							// Create COURSE Table
																			// New DB
					DisplayScores.scoreDataChanged = true;								// Must force redisplay scores
//					coursesDataChanged = true;								// Must force reload course combo
					
					// TODO should this be a load or just a need to load switch
//					MiscMethods.refreshCourseTable(connection, courseTableName);
//					MiscMethods.fillComboBox(connection, courseTableName);
					handicapPrefs.put(HANDICAPDB, path);					// Save the new DB in preference
					setPathSet(true);
//					if (cTime)
//					txtrLog.append("New DB: " + path);
	    		} 
				else 
	    		{
//					if (cTime)
//						txtrLog.append("New command cancelled by user." + "\n");
	    		}
//				if (cTime)
//					txtrLog.setCaretPosition(txtrLog.getDocument().getLength());
			}
		});
		
		mnFile.add(mntmNewDb);
		
		JMenuItem mntmSwitchDb = new JMenuItem("Switch DB",
				IconUtils.getGeneralIcon( "Open", 24 ));
		mntmSwitchDb.addActionListener(new ActionListener() 
		{
			public void actionPerformed(ActionEvent e) 
			{
				Preferrences.fc.setFileSelectionMode(JFileChooser.FILES_ONLY);			// Only look at files
				int returnVal = Preferrences.fc.showOpenDialog(null);
				if (returnVal == JFileChooser.APPROVE_OPTION) 
				{	
					
					String path = Preferrences.fc.getSelectedFile().getAbsolutePath();

					if (isPathSet())									// path to DB set?
						closeHandicapDB(sqliteConnection.connection); 		// DB open so, close it
					else
					{
						HandicapMainMenu.btnAddScores.setVisible(true);
						HandicapMainMenu.btnDisplayScores.setVisible(true);
						HandicapMainMenu.btnEditCourses.setVisible(true);
						setPathSet(true);													// Handicap DB has been initialized
					}
					handicapPrefs.remove(HANDICAPDB);	 				// Remove path from prefs
					sqliteConnection.connection = sqliteConnection.dbConnector(path);	// Open the new DB
					String answer = JOptionPane.showInputDialog("Enter your first name for tables:");
					handicapPrefs.put(HANDICAPUSERNAME, answer);					// Save username
					scoreTableName = answer + "_SCORE_TBL";
					handicapPrefs.put(HANDICAPSCORETABLENAME, scoreTableName);		// Save the new in preference
					courseTableName = answer + "_COURSE_TBL";
					handicapPrefs.put(HANDICAPCOURSETABLENAME, courseTableName);	// Save the new in preference
					refreshCourseTable(sqliteConnection.connection, courseTableName);				// Fill initial course table
					DisplayScores.scoreDataChanged = true;										// Force re-display

					//    						tournamentTableName = answer + "_TOURNAMENT_TBL";
					//    						handicapPrefs.put(HANDICAPTOURNAMENTTABLENAME, tournamentTableName);
					// Save the new in preference
					
																			// Switch DB
					DisplayScores.scoreDataChanged = true;								// Must force redisplay scores
//					coursesDataChanged = true;								// Must force reload course combo
					
					// TODO should this be a load or just a need to load switch
//					MiscMethods.refreshCourseTable(connection, courseTableName);
//					MiscMethods.fillComboBox(connection, courseTableName);
					handicapPrefs.put(HANDICAPDB, path);					// Save the new in preference
					setPathSet(true);
//					if (cTime)
//						txtrLog.append("Switched to DB: " + path);
				} 
				else 
				{
//					if (cTime)
//						txtrLog.append("Switch command cancelled by user." + "\n");
				}
//				if (cTime)
//					txtrLog.setCaretPosition(txtrLog.getDocument().getLength());
			}
		});
		mnFile.add(mntmSwitchDb);
		
//		JSeparator separator = new JSeparator();
//		mnFile.add(separator);
//		
//		JMenuItem mntmBack = new JMenuItem("Back",
//				IconUtils.getNavigationIcon("Back", 24));
//		mnFile.add(mntmBack);
		
		JMenu mnEdit = new JMenu("Edit");
		menuBar.add(mnEdit);
		
		JMenuItem mntmAddScore = new JMenuItem("Add Score");
		mntmAddScore.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) 
			{
				HandicapMainMenu.addScore();
				HandicapMain.cards.show(getContentPane(), HandicapMain.ADDSCORES);
			}
		});
		mnEdit.add(mntmAddScore);
		
		JMenuItem mntmEditCourse = new JMenuItem("Edit Course");
		mntmEditCourse.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) 
			{
				/*
				 * 
				 * 				This makes course name, rating, and slope select all
				 * 			
				 */

				MaintainCourses.textFieldAddCourseName.addFocusListener(new java.awt.event.FocusAdapter() {
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
//								HandicapMain.cards.show(getContentPane(), MAINTAINCOURSES);
							}
						});
					}
				});
				HandicapMain.cards.show(getContentPane(), MAINTAINCOURSES);
			}
		});
		mnEdit.add(mntmEditCourse);
		
		JMenu mnView = new JMenu("View");
		menuBar.add(mnView);
		
		JMenuItem mntmDisplayScores = new JMenuItem("Display Scores");
		
		mntmDisplayScores.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) 
			{
				DisplayScores.scoreEditingAllowed = true;				// Allow editing of scores
				if (DisplayScores.scoreDataChanged)					// Has anything changed?
				{
					boolean tournament = false;
					refreshScoreTable(sqliteConnection.connection, scoreTableName, tournament);
				}
				HandicapMain.cards.show(getContentPane(), DISPLAYSCORES);	// Just display with no changes
			}
		});
		
		mnView.add(mntmDisplayScores);
		
		JMenuItem mntmDisplayArchiveScores = new JMenuItem("Display Archive Scores");
		
		mntmDisplayArchiveScores.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) 
			{
				String archiveYear = JOptionPane.showInputDialog(null, "Enter year of Archive (YYYY) to display", "Archive Year", JOptionPane.DEFAULT_OPTION);
				if (archiveYear == null)
					return;
				if (!StringUtils.isInteger(archiveYear))
				{
					JOptionPane.showMessageDialog(null, "Archive year is invalid.", "Archive Year", JOptionPane.ERROR_MESSAGE);
					return;
				}
				boolean tournament = false;
//				MiscMethods.refreshScoreTable(connection, "YrEnd2015Peter_SCORE_TBL", tournament);
				refreshScoreTable(sqliteConnection.connection, "YrEnd" + archiveYear + scoreTableName, tournament);
				DisplayScores.scoreEditingAllowed = false;
				DisplayScores.scoreDataChanged = true; 
				HandicapMain.cards.show(getContentPane(), DISPLAYSCORES);		// Just display with no changes
			}
		});
		
		mnView.add(mntmDisplayArchiveScores);
		
		JMenuItem mntmDisplayTournamentScores = new JMenuItem("Display Tournament Scores");
		
		mntmDisplayTournamentScores.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) 
			{
				boolean tournament = true;					// Display tournament data
				refreshScoreTable(sqliteConnection.connection, scoreTableName, tournament);
				DisplayScores.scoreEditingAllowed = false;
				DisplayScores.scoreDataChanged = true;					// Force rdeisplay score data
				HandicapMain.cards.show(getContentPane(),
						DISPLAYSCORES);						// Just display with no changes
			}
		});
		
		mnView.add(mntmDisplayTournamentScores);
		
		JMenu mnTools = new JMenu("Tools");
		menuBar.add(mnTools);
		
		JMenuItem mntmArchiveScores = new JMenuItem("Archive Scores");
		
		mntmArchiveScores.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) 
			{
/*
 * 			This will archive a years worth of data to a new table userName_YrEndnnn_TBL
 * 			If recalcindex is true redisplay scores file
 * 			Ask for year to archive
 * 			archive year data
 * 			Count scores > year to archive
 * 					delete scores from current
 * 				else
 * 					delete scores from current leaving some from archive year to make 20 total			
 */
				String archiveYear = JOptionPane.showInputDialog(null, "Enter year to Archive (YYYY)", "Archive Year", JOptionPane.DEFAULT_OPTION);
				if (archiveYear == null)
					return;
				if (!StringUtils.isInteger(archiveYear))
				{
					JOptionPane.showMessageDialog(null, "Archive year is invalid.", "Archive Year", JOptionPane.ERROR_MESSAGE);
					return;
				}
				String startDate, endDate;
				startDate = archiveYear + "-01-01";				// Start 01/01/YY
				endDate   = archiveYear + "-12-31";				// End   12/31/YY
				String tempScoreTableName = "YrEnd" + archiveYear + userName;
				createScoreTable(tempScoreTableName);					// Create a temporary score table
				tempScoreTableName = tempScoreTableName + "_Score_TBL";	// Finish off name 
//  INSERT INTO "main"."Peter_COURSE_TBL" SELECT "Name","Rating","Slope" FROM "main"."oXHFcGcd04oXHFcGcd04_Peter_COURSE_TBL"
				try 
				{	
					String query = "insert into " + tempScoreTableName + " SELECT  " + 
							"DateField," +
							"Course," +
							"T," +
							"Score," +
							"U," +
							"Rating," +
							"Slope," +
							"Differential" +
							" FROM " + scoreTableName +
							" WHERE DateField BETWEEN '" + startDate + "' AND '" + endDate + "'" +
							" Order by DateField DESC";
					if (isDebug())
						System.out.println("Query = " + query);
					
					PreparedStatement pst = sqliteConnection.connection.prepareStatement(query);
					
					pst.execute();
					JOptionPane.showMessageDialog(null, scoreTableName + " copied to " + tempScoreTableName);
					
					pst.close();
				}
				catch (Exception e1) 
				{
					JOptionPane.showMessageDialog(null, "Something happened here!");
					if (isDebug())
						e1.printStackTrace();
				}
				
			}
		});
		
		mnTools.add(mntmArchiveScores);
		
		mnDebug = new JMenu("Debug");
		menuBar.add(mnDebug);
		
//		JMenuItem mntmCreateNewTables = new JMenuItem("Create New Tables");
//		mntmCreateNewTables.addActionListener(new ActionListener() {
//			public void actionPerformed(ActionEvent e) 
//			{
//				userName = JOptionPane.showInputDialog("Your first name");
//				createScoreTable(userName);								// Create the SCORE table
//				createCourseTable(userName);							// Create the COURSE table
//				handicapPrefs.put(HANDICAPUSERNAME, userName);			// Save User name
//			}
//		});
//		mnDebug.add(mntmCreateNewTables);
		
//		JMenuItem mntmSwitchTables = new JMenuItem("Switch Tables");
//		mnDebug.add(mntmSwitchTables);
//		
//		JSeparator separator_1 = new JSeparator();
//		mnDebug.add(separator_1);
		
		JMenuItem mntmRemoveNineHole = new JMenuItem("Remove 9 Hole");
		mntmRemoveNineHole.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) 
			{
				handicapPrefs.remove(HANDICAPNINEHOLE);  							// <debug>
			}
		});
		mnDebug.add(mntmRemoveNineHole);
		
		JMenuItem mntmRemoveDbPath = new JMenuItem("Remove DB Path");
		mntmRemoveDbPath.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) 
			{
//				setPathSet(false);							// set to no path
				handicapPrefs.remove(HANDICAPDB);			// Remove path from prefs
			}
		});
		mnDebug.add(mntmRemoveDbPath);
		
		JMenuItem mntmRemoveNode = new JMenuItem("Remove Node");
		mntmRemoveNode.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) 
			{
				/*
				 * 
				 * 	Remove preferences  mac  ~/library/com.apple.java.util.prefs.plist
				 * 	In the PLIST stored as packageName + class name making call
				 * 	ie "org.handicap.Handicap"
				 * 
				 */
				int answer = JOptionPane.showConfirmDialog(null, "This will remove all preferrences.  Confirm:",
						"Delete Preferrences",	 JOptionPane.YES_NO_OPTION);
				if (answer == JOptionPane.YES_OPTION)
				{
				    try 
				    {
				    	handicapPrefs.removeNode();
					}
				    catch (BackingStoreException e1) 
				    {
						e1.printStackTrace();
					}
				}
			}
		});
		mnDebug.add(mntmRemoveNode);
		
		JPanel contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);
		
		HandicapMainMenu mm = new HandicapMainMenu();
//		mm.btnAddScores.addActionListener(new ActionListener() {
//			public void actionPerformed(ActionEvent e) 
//			{
//				
//			}
//		});

		getContentPane().setLayout(cards);
		getContentPane().add(mm, MAINMENU);
		MaintainCourses mc = new MaintainCourses();
		getContentPane().add(mc, MAINTAINCOURSES);
		DisplayScores ds = new DisplayScores();
		getContentPane().add(ds, DISPLAYSCORES);
		AddScores as = new AddScores();
		getContentPane().add(as, ADDSCORES);
		Preferrences pr = new Preferrences();
		getContentPane().add(pr, PREFERRENCES);
		
/*
 * 		End of GUI creation
 */
		
		/*
		 * 
		 * 	Check to see if Handicap DB has been initialized.
		 * 	If not, don't allow anything until it has.
		 * 
		 */
				

				if (!(isPathSet()))							// path to DB set?
				{
					/*
					 * 
					 * Handicap DB has not been setup, do not allow 
					 * anything until it is
					 * 
					 */
					HandicapMainMenu.btnAddScores.setVisible(false);
					HandicapMainMenu.btnDisplayScores.setVisible(false);
					HandicapMainMenu.btnEditCourses.setVisible(false);

					JOptionPane.showMessageDialog(null, "DB has not been initialized,\n"
							+ "go to File menu and select New DB to create a new DB,\n"
							+ "or Switch DB to switch to an existing DB.");
				}
				else
				{
					HandicapMainMenu.btnAddScores.setVisible(true);
					HandicapMainMenu.btnDisplayScores.setVisible(true);
					HandicapMainMenu.btnEditCourses.setVisible(true);
					setPathSet(true);										// Handicap DB has been initialized
					sqliteConnection.connection = sqliteConnection.dbConnector(dbPath);		// Open handicap DB
					refreshCourseTable(sqliteConnection.connection, courseTableName);		// Fill initial course table

					// TODO should this be a load or just a need to load switch
//					MiscMethods.refreshCourseTable(connection, courseTableName);		// Fill initial course table
//					MiscMethods.fillComboBox(connection, courseTableName);				// Fill courses combo box
				}
		
	}
	
//	private static void setTitleInFrame(String title)
//	{
//		HandicapMain.setTitle(title);
//	}
/**
 * This method will close the Scores and Courses Database	
 * @param connection
 */
	public static void closeHandicapDB(Connection connection) 
	{
		try 
		{
			connection.close();
		} 
		catch (SQLException e) 
		{
			e.printStackTrace();
		}
	}
	
	/**
	 * 	This method creates the score table from scoreTableName
	 */
	
	public void createScoreTable(String userName) 
	{
		/*
		 * 	 Create new SCORE table
		 */ 
		
//		scoreTableName = JOptionPane.showInputDialog("Enter Your first name");
		scoreTableName = userName + "_SCORE_TBL";
		
		try 
		{
			String query = "CREATE TABLE " + scoreTableName + 
				"(DateField DATETIME NOT NULL, " +
				"Course TEXT(45) NULL, " +
				"T CHAR(1) NULL, " +
				"Score INTEGER(3) NULL, " +
				"U CHAR(1) NULL, " +
				"Rating DOUBLE(3) NULL, " +
				"Slope INTEGER(3) NULL, " +
				"Differential DOUBLE(3) NULL, " +
//				"DateSort DATETIME NOT NULL, " +
				"PRIMARY KEY(DateField))";				
			PreparedStatement pst = sqliteConnection.connection.prepareStatement(query);
//					pst.setString(1, "NewTable");
			pst.execute();
			pst.close();
			/*
			 * Set score table in in preferences
			 * Setup a new variable to be used in all score DB queries
			 */
			if (!userName.startsWith("YrEnd"))										// Archive ScoreTable?
				handicapPrefs.put(HANDICAPSCORETABLENAME, scoreTableName);			//  No, save the new in preference
			else
			{
				 String yrEndName = scoreTableName.substring(0, 9);				// YrEnd-YYYY_
				scoreTableName = scoreTableName.replace(yrEndName, "");				// Strip off "YrEnd-YYYY_"
			}
				
		}
		catch (SQLException e1) 
		{
			JOptionPane.showMessageDialog(null, "Duplicate SCORE table, using previous: " + scoreTableName,
					"SCORE table change",JOptionPane.ERROR_MESSAGE);
//					e1.printStackTrace();
		}
	}

/**
 * 	This method creates the course table from courseTableName
 */

	public void createCourseTable(String userName) 
	{
		/*
		 * 	 Create new COURSE table
		 */ 
		
//		courseTableName = JOptionPane.showInputDialog("Enter your first name");
		courseTableName = userName + "_COURSE_TBL";
		
		try 
		{
			String query = "CREATE TABLE " + courseTableName + 
				"(Name TEXT(45) NULL, " +	
				"Rating DOUBLE(3) NULL, " +
				"Slope INTEGER(3) NULL, " +
				"PRIMARY KEY(Name))";
									
			PreparedStatement pst = sqliteConnection.connection.prepareStatement(query);
//					pst.setString(1, "NewTable");
			pst.execute();
			pst.close();
			/*
			 * Set score table in in preferences
			 * Setup a new variable to be used in all COURSE DB queries
			 */
			handicapPrefs.put(HANDICAPCOURSETABLENAME, courseTableName);			// Save the new in preference
		}
		catch (SQLException e1) 
		{
			JOptionPane.showMessageDialog(null, "Duplicate COURSE table, using previous: " + courseTableName,
					"COURSE table change", JOptionPane.INFORMATION_MESSAGE);
					e1.printStackTrace();
		}
	}
	
	public static void refreshScoreTable(Connection connection, String scoreTableName, boolean tournament)
	{
//		if (recalcHandicapIndex == false)		// Have an scores changed?
//			return;								// No - just leave as is
//		recalcHandicapIndex = false;			// Go ahead and rebuild and turn off switch
/*
 * 		Read the scores TABLE
 */
//		JTable tableDisplayScores;
//		tableDisplayScores = Handicap.getTableDisplayScores();			// Where display area resides

		try 
		{
/*
 * 			Display date as on TABLE (yyyy-mm-dd)		<date change>
 */
			
//			String query = "select Date, Course as 'Course Name', T, Score, U,"
//					+ "Rating, Slope, Differential as 'Index' from " + scoreTableName + " Order by Date DESC";
			
			/*
			 * 			Display date mm/dd/yy
			 */		
			
			String query = "select strftime('%m/%d/', DateField) || substr(strftime('%Y', DateField),3, 2) as Date,"
					+ " Course as 'Course Name',"
					+ " T, Score, U,"
					+ "Rating, Slope,"
					+ " Differential as 'Index'"
					+ " from " + scoreTableName + " Order by DateField DESC";
			
/*
 * 			Change query if we are only displaying tournament scores
 */
			if (tournament)
			{
				query = "select strftime('%m/%d/', DateField) || substr(strftime('%Y', DateField),3, 2) as Date,"
						+ "Course as 'Course Name',"
						+ " T, Score, U,"
						+ "Rating, Slope,"
						+ " Differential as 'Index'"
						+ " from " + scoreTableName + " where T = '"+"T"+"' Order by DateField DESC";
			}
			PreparedStatement pst = connection.prepareStatement(query);

			ResultSet rs = pst.executeQuery();
			/**
			 * 			RS2XML - ResultSet
			 * 
			 * 			Load table using rs2xml.jar  video 12
			 * 	@see	https://www.youtube.com/watch?v=6cNYUc2PIag&list=PLS1QulWo1RIbYMA5Ijb72QHaHvCrPKfS2&index=12
			 * 			https://www.google.com/url?sa=t&rct=j&q=&esrc=s&source=web&cd=1&cad=rja&uact=8&ved=0ahUKEwip2K3KnuHKAhULlh4KHYa3AOUQFggcMAA&url=http%3A%2F%2Fsourceforge.net%2Fprojects%2Ffinalangelsanddemons%2F&usg=AFQjCNG_PLve63VxZe5Mg0niMGgRtZolPQ&sig2=V4xTJprXP6xgkFmTMf06LA&bvm=bv.113370389,d.dmo
			 * 		
			 */

			DisplayScores.tableDisplayScores.setModel(DbUtils.resultSetToTableModel(rs));


			rs.close();
			pst.close();
			
		}
		catch (Exception e1) 
		{
			if (HandicapMain.isDebug())
			{
				e1.printStackTrace();
			}
			return;										// If error, just get out of here
		}

			/*
			 * 
			 * 			Calculate how wide to make each column
			 * 		
			 */
//			int preferredWidth = 0;
		
//			Force some widths (Date, T, Score, U)
			
			int width = 90;			//		Date column
			DisplayScores.tableDisplayScores.getColumnModel().getColumn(HandicapMain.DATE_POS).setMinWidth(width);
			DisplayScores.tableDisplayScores.getColumnModel().getColumn(HandicapMain.DATE_POS).setMaxWidth(width);
			DisplayScores.tableDisplayScores.getColumnModel().getColumn(HandicapMain.DATE_POS).setPreferredWidth(width);
			width = 40;				//		T column
			DisplayScores.tableDisplayScores.getColumnModel().getColumn(HandicapMain.T_POS).setMinWidth(width);
			DisplayScores.tableDisplayScores.getColumnModel().getColumn(HandicapMain.T_POS).setMaxWidth(width);
			DisplayScores.tableDisplayScores.getColumnModel().getColumn(HandicapMain.T_POS).setPreferredWidth(width);
									//		U column
			DisplayScores.tableDisplayScores.getColumnModel().getColumn(HandicapMain.U_POS).setMinWidth(width);
			DisplayScores.tableDisplayScores.getColumnModel().getColumn(HandicapMain.U_POS).setMaxWidth(width);
			DisplayScores.tableDisplayScores.getColumnModel().getColumn(HandicapMain.U_POS).setPreferredWidth(width);
			width = 60;				//		Score column
			DisplayScores.tableDisplayScores.getColumnModel().getColumn(HandicapMain.SCORE_POS).setMinWidth(width);
			DisplayScores.tableDisplayScores.getColumnModel().getColumn(HandicapMain.SCORE_POS).setMaxWidth(width);
			DisplayScores.tableDisplayScores.getColumnModel().getColumn(HandicapMain.SCORE_POS).setPreferredWidth(width);
			
			renderColumns(DisplayScores.tableDisplayScores);				// Set column sizes for table tableDisplayScores
			
//			recalcHandicapIndex = false;									// Don't recalculate until needed
/*
 * 			Calculate Handicap index
 */
			String hi = "NH";												// Assume not enough scores
			DisplayScores.textDisplayScoresIndex.setText(hi);				// Put in display								
			
			double [] handicapIndex = calculateHandicapIndex(DisplayScores.tableDisplayScores);	// Calculate handicap index and mark used scores
			
			if (handicapIndex [0] != -99)										// Calculate index?
			{
				hi = String.valueOf(Math.floor(handicapIndex[0] * 10) / 10);	// Calculated index
				DisplayScores.textDisplayScoresIndex.setText(hi);							// Set handicap Index without adjustment
			}
			if (handicapIndex[1] != -99)										// Tournament adjustment?
			{
				hi = String.valueOf(Math.floor((handicapIndex[0] - handicapIndex[1]) * 10) / 10);	// Calculated index
				DisplayScores.textDisplayScoresIndexAdj.setText(hi + "R");			// Set handicap Index with adjustment
			}		
	}

/**
 * 	Method will calculate handicap index from differentials in JTABLE tableDisplayScores
 * 
 * handicap index = AVG (differentials) * .96 (truncated to tenths)
 * 
 * @param tableDisplayScores
 * @return 
 */
	public static double[] calculateHandicapIndex(JTable tableDisplayScores) 
	{
		class Indices
		{

			public double differential;
			public int tableRowNumber;

		}

		double[] handicapIndexR = new double [2];
		handicapIndexR[0] = -99;							// Set to no handicap index
		handicapIndexR[1] = -99;							// Set to no handicap index adjustment
		double handicapIndex = -99;							// Handicap index, -99 not enough scores
		int numberTournamentScores = 0;						// Number of tournament scores
		double saveDifferential = 0;						// Save differential for ease
		double tournLowDiffOne = 99;						// Tournament low 1
		double tournLowDiffTwo = 99;						// Tournament low 2

		int lastRow = tableDisplayScores.getRowCount();		// Rows in table
		int numberNonNineHoleScores = lastRow;				// Number non-9 hole scores

		if (lastRow < 1)									// Empty?
		{
			handicapIndexR[0] = handicapIndex;				// yes - return -99
			return handicapIndexR;
		}
		int scoresInCurrentRecord = 20;						// # scores in current record - assume 20
		if (lastRow < 20)									// More than 20 scores?
		{
			scoresInCurrentRecord = lastRow;				// Get scores we have
		}

		Indices[] indexArray = new Indices [scoresInCurrentRecord];		// Array of indices # up to 20
		/*
		 * 
		 * 			Build array of Indices object
		 * 				double differential
		 * 				int table row number
		 */
		int numberNonNineHoleScoresInCurrentRecord = 0;
		int totalOfAllScores = 0;
		for (int row = 0; row < lastRow; row++)				// Loop thru all scores
		{
			String t = (String) tableDisplayScores.getModel().getValueAt(row, HandicapMain.T_POS);		// Index row
			if (row < scoresInCurrentRecord)				// Process current scoring record
			{												// Last 20 or less
				
//				Process scores in current scoring record
				
				if ((t == null) || (!(t.equals(HandicapMain.NINEINDICATOR))))		// Nine hole score?
				{
					indexArray[numberNonNineHoleScoresInCurrentRecord] = new Indices();		// No - process
					indexArray[numberNonNineHoleScoresInCurrentRecord].
						differential = saveDifferential = (double) tableDisplayScores.getModel().
							getValueAt(row, HandicapMain.DIFFERENTIAL_POS);							// differential
					indexArray[numberNonNineHoleScoresInCurrentRecord].
						tableRowNumber = row;												// row #
					numberNonNineHoleScoresInCurrentRecord++;	// Count non 9 hole scores in record
//					System.out.println("Row: " + indexArray[row].tableRowNumber + " Index: " + indexArray[row].differential);
				}
				else										// Yes 9 hole - skip
				{
					totalOfAllScores = totalOfAllScores - (int) tableDisplayScores.
							getModel().getValueAt(row, HandicapMain.SCORE_POS);	// Subtract 9 hole score
																	// we are going to add in later
					numberNonNineHoleScores = lastRow - 1;			// # scores that are not 9 holes
					if (scoresInCurrentRecord < lastRow)			// Do we have 20?
					{
						scoresInCurrentRecord++;					// Process 1 more for current record
					}
					
				}

			}
			saveDifferential = (double) tableDisplayScores.getModel().
					getValueAt(row, HandicapMain.DIFFERENTIAL_POS);				// Save differential for tournament processing
			totalOfAllScores = totalOfAllScores + (int) tableDisplayScores.
					getModel().getValueAt(row, HandicapMain.SCORE_POS);
			
			if ((t == null) || (!(t.equals(HandicapMain.TOURNINDICATOR))))		// Tournament score?
			{
				//  ignore
			}
			
/*			Process tournament score
 * 			Save off two lowest tournament scores
 */
			
			else															// Yes
			{
				numberTournamentScores = numberTournamentScores + 1;		// Count it

				if (saveDifferential < tournLowDiffOne)						// Differential smaller than first lower?	
				{
					if (tournLowDiffTwo == 99 && tournLowDiffOne == 99)		// First of 2?
						tournLowDiffOne = saveDifferential;					// Yes, save first lower
					else													// No
						if (tournLowDiffTwo != 99)							// Do we have a 2nd?
							tournLowDiffTwo = saveDifferential;				// No, save 2nd
						else
						{
							if (saveDifferential < tournLowDiffTwo)				// Is it smaller than second?
								tournLowDiffTwo = saveDifferential;				// Yes, replace second	
						}
				}
				else
				{
					if (tournLowDiffTwo == 99)							// Do we have a 2nd?
						tournLowDiffTwo = saveDifferential;				// No, save 2nd
					else
					{
						if (saveDifferential < tournLowDiffTwo)				// Is it smaller than second?
							tournLowDiffTwo = saveDifferential;				// Yes, replace second	
					}
				}
			}
		}
		/*
		 * 
		 * 		Calculate average score and put in textfield on Display Scores
		 * 
		 */
				double total = totalOfAllScores;					// Total non 9 hole scores
				double number = numberNonNineHoleScores;			// Number of non 9 hole scores
				double averageScore = Math.floor((total / number) * 10) / 10;	// Truncate to .1
				DisplayScores.textDisplayScoresAverageScore.setText(String.valueOf(averageScore));	// Set *** textfield ***
				DisplayScores.textDisplayScoresNumberScores.setText(String.valueOf					// Set *** textfield ***
						(numberNonNineHoleScores));											// Display number of scores
				/*
				 * 
				 * 			Calculate number of scores in record that will be used
				 * 			
				 */
				int nScores;								// number of scores to use in latest record
				switch (numberNonNineHoleScoresInCurrentRecord)
				{
				case 20:
					nScores = 10;							// if 20 -> 10
					break;

				case 19:
					nScores = 9;							// If 19 -> 9
					break;

				case 18:
					nScores = 8;							// If 18 -> 8
					break;

				case 17:									// If 17 -> 7
					nScores = 7;
					break;

				case 16:									// If 15 or 16 -> 6
				case 15:
					nScores = 6;
					break;

				case 14:									// If 13 or 14 -> 5	
				case 13:
					nScores = 5;
					break;

				case 12:									// If 11 or 12 -> 4
				case 11:
					nScores = 4;
					break;

				case 10:									// if 9 or 10 -> 3
				case 9:
					nScores = 3;
					break;

				case 8:										// If 7 or 8 -> 2
				case 7:
					nScores = 2;
					break;

				case 6:										// If 5 or 6 -> 1
				case 5:
					nScores = 1;
					break;

				default:									// Less than NH
					nScores = 0;

				}

				if (nScores == 0)							// Enough scores to calculate index?
				{
					handicapIndexR[0] = handicapIndex;		// No - return -99
					return handicapIndexR;
				}

		/*
		 * 
		 * 		Sort last 20 or less differentials
		 * 
		 */
				int temptableRowNumber;
				double tempDifferential;
				boolean finish = false;
				while (!finish)
				{
					for (int s = 1; s < numberNonNineHoleScoresInCurrentRecord; s++)
					{
						finish = true;
						for (int n = 0; n < (numberNonNineHoleScoresInCurrentRecord) - s; n++)
						{
							if (indexArray[n].differential > indexArray[n + 1].differential)
							{
								tempDifferential = indexArray[n + 1].differential;
								temptableRowNumber = indexArray[n + 1].tableRowNumber;
								indexArray[n + 1].differential = indexArray[n].differential;
								indexArray[n + 1].tableRowNumber = indexArray[n].tableRowNumber;
								indexArray[n].differential = tempDifferential;
								indexArray[n].tableRowNumber = temptableRowNumber;
								finish = false;				
							}
						}
					}
					
				}
		/*
		 * 		Set score used
		 */
				double totalDifferential = 0;
				for (int row = 0; row < nScores; row++)
				{
					tableDisplayScores.setValueAt("*", indexArray[row].tableRowNumber, HandicapMain.U_POS);	// Set used
					totalDifferential = totalDifferential + indexArray[row].differential;
				
//					System.out.println("Row: " + indexArray[row].tableRowNumber + " Index: " + indexArray[row].differential);
					
				}
		/*
		 * 		Calculate handicap index
		 */
				double scores;
				scores = nScores;																// Convert to double
				handicapIndex = Math.floor(((totalDifferential / scores) * .96) * 10) / 10;		// Truncate to .1

//				Calculate tournament adjustment

				double tournamentAdjustment = -99;									// default no adjustment
				double two = 2F;
				if (numberTournamentScores > 1)										// 2 or more tournament scores?
				{
					// Make tournament adjustment if handicap index - 2nd lowest tournament diff > 3
					
					if (handicapIndex - Math.max(tournLowDiffOne, tournLowDiffTwo) > 3)
					{
						double tournDiffAverage = (tournLowDiffOne + tournLowDiffTwo) / two;
						double tournDiffAverageRnd = Math.floor((tournDiffAverage) * 10) / 10;
						tournamentAdjustment = calcTournamentAdjustment(handicapIndex - tournDiffAverageRnd,	
							numberTournamentScores);								// Yes, calculate adjustment
					}
				}
				handicapIndexR[0] = handicapIndex;									// Set HI
				handicapIndexR[1] = tournamentAdjustment;							// Set HI adjustment
//				Handicap.setTextDisplayScoresIndex(String.valueOf(handicapIndex));	// Set handicap Index without adjustment
				
				return handicapIndexR;												// Handicap index and T adjustment
			}

	private static double calcTournamentAdjustment(double handicapIndex, int numberTournamentScores) {
		
		//  Tournament adjustment table
		
	double[] [] tournTable = new double [] []
	//	idx	2 3 4 5-9 10-19 20-29 30-39 =>40
		{
		{3.5, 0, 0, 0, 0, 0, 0, 0, 0},							// 3.0 - 3.4
		{4, 0, 0, 0, 0, 0, 0, 0, 0},							// 3.5 - 3.9
		{4.5, 1, 0, 0, 0, 0, 0, 0, 0},							// 4.0 - 4.4 
		{5, 1.8, 1, 0, 0, 0, 0, 0, 0},							// 4.5 - 4.9
		{5.5, 2.6, 1.9, 1,0, 0, 0, 0, 0},						// 5.0 - 5.4
		{6, 3.4, 2.7, 1.9, 1, 0, 0, 0, 0},						// 5.5 - 5.9
		{6.5, 4.1, 3.5, 2.8, 1.9, 1, 0, 0, 0},					// 6.0 - 6.4
		{7, 4.8, 4.3, 3.7, 2.9, 2, 1, 0, 0},					// 6.5 - 6.9
		{7.5, 5.5, 5, 4.5, 3.8, 3, 2.1, 1,0},					// 7.0 - 7.4
		{8, 6.2, 5.7, 5.3, 4.7, 3.9, 3.1, 2.2, 1},				// 7.5 - 7.9
		{8.5, 6.8, 6.4, 6, 5.5, 4.8, 4.1, 3.2, 2.2},			// 8.0 - 8.4
		{9, 7.4, 7.1, 6.7, 6.2, 5.7, 5, 4.2, 3.3},				// 8.5 - 8.9
		{9.5, 8.1, 7.8, 7.4, 7, 6.5, 5.9, 5.2, 4.4},			// 9.0 - 9.4
		{10, 8.7, 8.4, 8.1, 7.7, 7.3, 6.7, 6.1, 5.4},			// 9.5 - 9.9
		{10.5, 9.2, 9, 8.8, 8.4, 8, 7.6, 7, 6.4},				// 10.0 - 10.4
		{11, 9.8, 9.5, 9.4, 9.1, 8.7, 8.3, 7.8, 7.2},			// 10.5 - 10.9
		{11.5, 10.4, 10.2, 10, 9.7, 9.4, 9.1, 8.6, 8.1},		// 11.0 - 11.4
		{12, 11, 10.8, 10.6, 10.4, 10.1, 9.8, 9.4, 8.9},		// 11.5 - 11.9
		{12.5, 11.5, 11.4, 11.2, 11, 10.7, 10.5, 10.1, 9.7},	// 12.0 - 12.4
		{13, 12.1, 11.9, 11.8, 11.6, 11.4, 11.1, 10.8, 10.5},	// 12.5 - 12.9
		{13.5, 12.6, 12.5, 12.4, 12.2, 12, 11.8, 11.5, 11.2},	// 13.0 - 13.4
		{14, 13.2, 13.1, 12.9, 12.8, 12.6, 12.4, 12.2, 11.9},	// 13.5 - 13.9
		{99 ,13.7 ,13.6, 13.5, 13.4, 13.2, 13, 12.8, 12.6}		// 14.0 or more
		};
		
		int column = 0;
		int row = 0;
		
// 		Number of tournament scores determine column in table
		
		if (numberTournamentScores < 5)
			column = numberTournamentScores -1;
		else
			if (numberTournamentScores < 10)
				column = 4;
			else
				if (numberTournamentScores < 20)
					column = 5;
				else
					if (numberTournamentScores < 30)
						column = 6;
					else
						if (numberTournamentScores < 40)
							column = 7;
						else
							column = 8;
		
//		Unadjusted handicap index - average of low 2 tournament diffs determines row in table 
		
		for (row = 0; row < tournTable.length; row++)
		{
			if (handicapIndex < tournTable[row][0])		// Search for next highest HI in table column 0
				break;
		}
		return tournTable[row][column];					// Amount of adjustment
}
	
	/**
	 * This method will take a table (score table or course table) as input
	 * It will check the data of each column and set the width based on the data
	 * 
	 * @param tableToRender
	 */
		
	public static void renderColumns(JTable tableToRender) 
	{
		int width;
		for (int column = 0; column < tableToRender.getColumnCount(); column++)				// Trip through columns
		{	
			TableColumn tableColumn = tableToRender.getColumnModel().getColumn(column);
			
			int preferredWidth = tableColumn.getMinWidth();
			int maxWidth = tableColumn.getMaxWidth();
			
	/*
	 * 		This processes the data of the table, excluding the header row
	 */
			for (int row = 0; row < tableToRender.getRowCount(); row++)						// Trip through rows
			{
					TableCellRenderer cellRenderer = tableToRender.getCellRenderer(row, column);
					Component c = tableToRender.prepareRenderer(cellRenderer, row, column);
					width = c.getPreferredSize().width + tableToRender.getIntercellSpacing().width;
					preferredWidth = Math.max(preferredWidth, width);

					//  We've exceeded the maximum width, no need to check other rows

					if (preferredWidth >= maxWidth)
					{
						preferredWidth = maxWidth;
						break;
					}
			}
			tableColumn.setPreferredWidth( preferredWidth );	// Set column width

			// Centers SCORE data in table

//			String OS = HandicapMain.getOperatingSystem();			// Get OS
			
			DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
			centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);				// Assuming to center
	/*
	 * 		If L & F is "System", columns are left justified
	 * 			therefore make data left justified (at least for Mac and Windows)		
	 */
//			String lAndF = getLookAndFeel();									// Get look & feel
			if (lookAndFeel.equals("System") || lookAndFeel.equals("Nimbus"))
					centerRenderer.setHorizontalAlignment(SwingConstants.LEFT);			// Make left
	/*
	 * 		L & F "Windows", columns are left justified
	 * 			therefore make data left justified (at least for Mac and Windows)
	 */
			if (operatingSystem.startsWith("Win"))
			{
				if (lookAndFeel.equals("Windows"))
				{
					centerRenderer.setHorizontalAlignment(SwingConstants.LEFT);		// Make left
				}
			}

			tableToRender.getColumnModel().getColumn(column).setCellRenderer(centerRenderer);
			}

	}
	
	/**
	 * This method refreshes the course table
	 * 
	 * @param Connection connection
	 */

	public static void refreshCourseTable(Connection connection, String courseTableName) 
	{
//		JTable tableDisplayCourses;
//		
//		tableDisplayCourses = Handicap.getTableDisplayCourses();
		
//		connection = Handicap.getConnection();
//		connection = sqliteConnection.dbConnector();
		try 
		{
			String query = "select * from " + courseTableName + " Order by Name";
//			String query = "select Name, Rating, Slope"
//			String query = "select * from Peter1Score Group by Date";
//			String query = "select EID, Name, Surname, Age from EmployeeInfo";
			PreparedStatement pst = connection.prepareStatement(query);
			ResultSet rs = pst.executeQuery();
		
			//  Load table using rs2xml.jar  video 12
			//  https://www.youtube.com/watch?v=6cNYUc2PIag&list=PLS1QulWo1RIbYMA5Ijb72QHaHvCrPKfS2&index=12
			//  https://www.google.com/url?sa=t&rct=j&q=&esrc=s&source=web&cd=1&cad=rja&uact=8&ved=0ahUKEwip2K3KnuHKAhULlh4KHYa3AOUQFggcMAA&url=http%3A%2F%2Fsourceforge.net%2Fprojects%2Ffinalangelsanddemons%2F&usg=AFQjCNG_PLve63VxZe5Mg0niMGgRtZolPQ&sig2=V4xTJprXP6xgkFmTMf06LA&bvm=bv.113370389,d.dmo
			
			MaintainCourses.tableDisplayCourses.setModel(DbUtils.resultSetToTableModel(rs));
			
			rs.close();
			pst.close();
			
		}
		catch (Exception e1) 
		{
			e1.printStackTrace();
		}
		renderColumns(MaintainCourses.tableDisplayCourses);				// Set column sizes for table tableDisplayCourses
		
		fillComboBox(connection, courseTableName); 						// Load course comboBox
	}

	/**
	 * This method fills the Add Scores course combobox
	 * It sets boolean fillingCourseCombobox Handicap field while running
	 * to immediately exit coombobox listener
	 * @param connection
	 */

	public static void fillComboBox(Connection connection, String courseTableName)
	{
		AddScores.fillingCourseCombobox = true;				// Filling course combobox
		AddScores.comboBoxCourse.removeAllItems();				// clear combobox
		try 
		{
			String query = "select * from "+ courseTableName + "";
			PreparedStatement pst = connection.prepareStatement(query);
			ResultSet rs = pst.executeQuery();
			
			while(rs.next())
			{
				AddScores.comboBoxCourse.addItem(rs.getString("Name"));
			}
			rs.close();
		}
		catch (Exception e1) 
		{
			e1.printStackTrace();
		}
		AddScores.fillingCourseCombobox = false;				// Done filling course combobox
	}
	
	/**
	 * This method converts a display date mm/dd/yy to table date yyyy-mm-dd
	 * Century 80 before and 20 after current
	 * @param displayDate
	 * @throws ParseException 
	 */

	public static String convertTableDate(String displayDate)
	{

		String month, day, year, ymdDate = null;
		month = displayDate.substring(0, 2);				// Get month
		day = displayDate.substring(3, 5);					// Get day
		year = displayDate.substring(6, 8);					// Get year
		ymdDate = "20" + year + "-" + month + "-" + day;	// yyyy-MM-dd

		
		return ymdDate;
	}
	
//	public static void editCourse() 
//	{
//		/*
//		 * 
//		 * 				This makes course name, rating, and slope select all
//		 * 			
//		 */
//						
//						MaintainCourses.textfieldAddCourseName.addFocusListener(new java.awt.event.FocusAdapter() {
//						    public void focusGained(java.awt.event.FocusEvent evt) {
//						        SwingUtilities.invokeLater(new Runnable() {
//						            @Override
//						            public void run() 
//						            {
//						            	MaintainCourses.textfieldAddCourseName.selectAll();
//						            	MaintainCourses.textFieldAddCourseCourseRating.selectAll();
//						            	MaintainCourses.textFieldAddCourseCourseSlope.selectAll();
//		//				            	dateChooser.selectAll();
//						            }
//						        });
//						    }
//						});
//						
//						HandicapMain.cards.show(getContentPane(), PREFERRENCES);
//	}
	
	/**
	 * This method sets the look and feel
	 * 
	 * metal is cross platform
	 * 		Theme can be either "DefaultMetal" or "Ocean" 
	 * System
	 * Motif
	 * GTK
	 * Windows
	 * 
	 * @param String LOOKANDFEEL
	 * @param String THEME
	 */
	public static void initLookAndFeel(String LOOKANDFEEL, String THEME) 
	{
		String lookAndFeel = null;
		   
	    if (LOOKANDFEEL != null) 
	    {
	        if (LOOKANDFEEL.equals("Metal")) 
	        {
	            lookAndFeel = UIManager.getCrossPlatformLookAndFeelClassName();
//	            System.out.println(lookAndFeel);
	          //  an alternative way to set the Metal L&F is to replace the 
	          // previous line with:
	          // lookAndFeel = "javax.swing.plaf.metal.MetalLookAndFeel";
	            
	        }
	        
	        else if (LOOKANDFEEL.equals("System")) 
	        {
	            lookAndFeel = UIManager.getSystemLookAndFeelClassName();
	        } 
	        
	        else if (LOOKANDFEEL.equals("Motif")) 
	        {
	            lookAndFeel = "com.sun.java.swing.plaf.motif.MotifLookAndFeel";
	        } 
	        
	        else if (LOOKANDFEEL.equals("GTK")) 
	        { 
	            lookAndFeel = "com.sun.java.swing.plaf.gtk.GTKLookAndFeel";
	        } 
	        
	        else if (LOOKANDFEEL.equals("Windows")) 
	        { 
	            lookAndFeel = "com.sun.java.swing.plaf.windows.WindowsLookAndFeel";
	        } 
	        else if (LOOKANDFEEL.equals("Nimbus")) 
	        { 
	            lookAndFeel = "com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel";
	        } 
	        
	        else {
	            System.err.println("Unexpected value of LOOKANDFEEL specified: "
	                               + LOOKANDFEEL);
	            lookAndFeel = UIManager.getCrossPlatformLookAndFeelClassName();
	        }

	        try {
	        	
	        	
	            UIManager.setLookAndFeel(lookAndFeel);
	            
	            // If L&F = "Metal", set the theme
	            
	            if (LOOKANDFEEL.equals("Metal")) 
	            {
	            	if (THEME.equals("DefaultMetal"))
	            		MetalLookAndFeel.setCurrentTheme(new DefaultMetalTheme());
	            	else if (THEME.equals("Ocean"))
	            		MetalLookAndFeel.setCurrentTheme(new OceanTheme());
	            	//              else
	            	//                 MetalLookAndFeel.setCurrentTheme(new TestTheme());

	            	UIManager.setLookAndFeel(new MetalLookAndFeel()); 
	            }	
	            	   
	        } 
	        
	        catch (ClassNotFoundException e)
	        {
	        	JOptionPane.showMessageDialog(null, "Couldn't find class for specified look and feel: "  +
	        						lookAndFeel + "\n" +
	        						"Did you include the L&F library in the class path?" + "\n" +
	        						"Using the default look and feel.");
	        } 
	        
	        catch (UnsupportedLookAndFeelException e) 
	        {
	        	JOptionPane.showMessageDialog(null, "Can't use the specified look and feel (" + lookAndFeel +
	        										") on this platform.\n" +
	        										"Using the default look and feel.\n\n" +
	        										"Select another in preferences.");
	        } 
	        
	        catch (Exception e) 
	        {
	        	JOptionPane.showMessageDialog(null, "Couldn't get specified look and feel (" + lookAndFeel+ "), for some reason. \n" +
	        										"Using the default look and feel.");
	            System.err.println("Couldn't get specified look and feel ("
	                               + lookAndFeel
	                               + "), for some reason.");
	            System.err.println("Using the default look and feel.");
	            e.printStackTrace();
	        }
	        
	        
	    }
		
	}

	public static boolean isDebug() 
	{
		return debug;
	}

	public static void setDebug(boolean debug) 
	{
		HandicapMain.debug = debug;
	}
	
/**
 * This method returns status of DB path
 * @return
 */
	private boolean isPathSet() 
	{
		return pathSet;
	}
	
	private void setPathSet(boolean pathSet)
	{
		this.pathSet = pathSet;
	}

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) 
	{
		EventQueue.invokeLater(new Runnable() 
		{
			public void run() 
			{
				try 
				{
					HandicapMain frame = new HandicapMain();
					frame.setVisible(true);
				} 
				catch (Exception e) 
				{
					e.printStackTrace();
				}
			}
		});
	}


}
