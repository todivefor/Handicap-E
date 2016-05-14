package org.handicap;

import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.filechooser.FileNameExtensionFilter;

import org.iconutils.IconUtils;

import java.awt.GridBagLayout;
import javax.swing.JLabel;

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class Preferrences extends JPanel 
{

	JComboBox<String> comboBoxPreferencesTheme;
	private JLabel lblPreferencesTheme;
	public static JFileChooser fc;

	/**
	 * Create the panel.
	 */
	public Preferrences() 
	{
		setLayout(new BorderLayout(0, 0));
		
		JPanel panelCenter = new JPanel();
		add(panelCenter, BorderLayout.CENTER);
		
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[]{0, 0, 0, 0, 0};
		gridBagLayout.rowHeights = new int[]{0, 0, 0, 0};
		gridBagLayout.columnWeights = new double[]{0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
		gridBagLayout.rowWeights = new double[]{0.0, 0.0, 0.0, Double.MIN_VALUE};
		panelCenter.setLayout(gridBagLayout);
		
		JLabel lblLookAndFeel = new JLabel("Look and Feel");
		GridBagConstraints gbc_lblLookAndFeel = new GridBagConstraints();
		gbc_lblLookAndFeel.insets = new Insets(0, 5, 5, 5);
		gbc_lblLookAndFeel.gridx = 1;
		gbc_lblLookAndFeel.gridy = 1;
		panelCenter.add(lblLookAndFeel, gbc_lblLookAndFeel);
		
		JComboBox<String> comboBoxPreferencesLookAndFeel = new JComboBox<String>();
		comboBoxPreferencesLookAndFeel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) 
			{
				if (!HandicapMain.fillingLookAndFeelComboBox)
				{
					HandicapMain.lookAndFeel = (String)(comboBoxPreferencesLookAndFeel.getSelectedItem());
					if (HandicapMain.lookAndFeel.equals("Metal"))
					{
						lblPreferencesTheme.setVisible(true);
						comboBoxPreferencesTheme.setVisible(true);
					}
					else
					{
						lblPreferencesTheme.setVisible(false);
						comboBoxPreferencesTheme.setVisible(false);
						HandicapMain.initLookAndFeel(HandicapMain.lookAndFeel, HandicapMain.THEME);			// No theme, so go
					}
					HandicapMain.handicapPrefs.put(HandicapMain.HANDICAPLOOKANDFEEL, HandicapMain.lookAndFeel);
					SwingUtilities.updateComponentTreeUI(getParent());					// Update all components of frame
//					frame.pack();
					DisplayScores.scoreDataChanged = true;							// Re-due display scores
//					HandicapMain.coursesDataChanged = true;							// Re-due display courses
//					HandicapMain.cards.show(getParent(), HandicapMain.MAINMENU);
				}
			}
		});
		
		GridBagConstraints gbc_comboBoxPreferencesLookAndFeel = new GridBagConstraints();
		gbc_comboBoxPreferencesLookAndFeel.insets = new Insets(0, 0, 0, 0);
//		gbc_comboBox.fill = GridBagConstraints.HORIZONTAL;
//		gbc_comboBox.gridwidth = 3;
		gbc_comboBoxPreferencesLookAndFeel.anchor = GridBagConstraints.WEST;
		gbc_comboBoxPreferencesLookAndFeel.gridx = 3;
		gbc_comboBoxPreferencesLookAndFeel.gridy = 1;
		panelCenter.add(comboBoxPreferencesLookAndFeel, gbc_comboBoxPreferencesLookAndFeel);
		
		lblPreferencesTheme = new JLabel("Theme");
		GridBagConstraints gbc_lblPreferencesTheme = new GridBagConstraints();
		gbc_lblPreferencesTheme.insets = new Insets(10, 0, 0, 0);
		gbc_lblPreferencesTheme.gridx = 1;
		gbc_lblPreferencesTheme.gridy = 2;
		panelCenter.add(lblPreferencesTheme, gbc_lblPreferencesTheme);
		
		comboBoxPreferencesTheme = new JComboBox<String>();	
		
		comboBoxPreferencesTheme.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) 
			{
				if (!HandicapMain.fillingLookAndFeelComboBox)
				{
					String theme = (String)(comboBoxPreferencesTheme.getSelectedItem());
					HandicapMain.handicapPrefs.put(HandicapMain.HANDICAPTHEME, theme);
					HandicapMain.initLookAndFeel(HandicapMain.lookAndFeel, theme);
					SwingUtilities.updateComponentTreeUI(getParent());			// Update all components of frame
//					HandicapMain.cards.show(getParent(), HandicapMain.MAINMENU);
				}
			}
		});
		
		GridBagConstraints gbc_comboBoxPreferencesTheme = new GridBagConstraints();
		gbc_comboBoxPreferencesTheme.insets = new Insets(10, 0, 0, 0);
		gbc_comboBoxPreferencesTheme.fill = GridBagConstraints.NONE;
		gbc_comboBoxPreferencesTheme.anchor = GridBagConstraints.WEST;
		gbc_comboBoxPreferencesTheme.gridx = 3;
		gbc_comboBoxPreferencesTheme.gridy = 2;
		gbc_comboBoxPreferencesTheme.gridwidth = 5;
		panelCenter.add(comboBoxPreferencesTheme, gbc_comboBoxPreferencesTheme);
		
        //Create a file chooser
        fc = new JFileChooser();
        fc.setAcceptAllFileFilterUsed(false);
        FileNameExtensionFilter filter1 = new FileNameExtensionFilter("sqlite DB", "sqlite");
        fc.addChoosableFileFilter(filter1);
        
        //Uncomment one of the following lines to try a different
        //file selection mode.  The first allows just directories
        //to be selected (and, at least in the Java look and feel,
        //shown).  The second allows both files and directories
        //to be selected.  If you leave these lines commented out,
        //then the default mode (FILES_ONLY) will be used.
        //
//        Default is file only
//        fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
//        fc.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
		
/*
 * 		BorderLayout SOUTH
 */
		
		JPanel panelSouth = new JPanel();
		add(panelSouth, BorderLayout.SOUTH);
/*
 * 		This button only to force "Back" button to center	
 */
		JButton btnPreferencesBlank = new JButton(" ");					// <debug>  mark debug button
//		btnPreferencesBlank.setHorizontalAlignment(SwingConstants.RIGHT);
		
		btnPreferencesBlank.setOpaque(false);
		btnPreferencesBlank.setContentAreaFilled(false);
		btnPreferencesBlank.setBorderPainted(false);
		panelSouth.add(btnPreferencesBlank);

		JButton btnPreferrencesExit = new JButton("Back",
				IconUtils.getNavigationIcon("Back", 24));
		btnPreferrencesExit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) 
			{
				HandicapMain.cards.show(getParent(), HandicapMain.MAINMENU);
			}
		});
		panelSouth.add(btnPreferrencesExit);
		
		JButton btnPreferencesDebug = new JButton(" ");					// <debug>  mark debug button
//		btnPreferencesDebug.setHorizontalAlignment(SwingConstants.LEFT);
		
		btnPreferencesDebug.setOpaque(false);
		btnPreferencesDebug.setContentAreaFilled(false);
		btnPreferencesDebug.setBorderPainted(false);
		
		btnPreferencesDebug.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) 
			{
/*
* 					Toggle debug						
*/
				if (HandicapMain.debug)
				{
					HandicapMain.debug = false;
					HandicapMain.mnDebug.setVisible(false); 						// Turn off debug menu
					HandicapMain.handicapPrefs.remove(HandicapMain.HANDICAPDEBUG); 			// Set debug off
				}
				else
				{
					HandicapMain.debug = true;
					HandicapMain.mnDebug.setVisible(true);						// Turn debug menu
					HandicapMain.handicapPrefs.putBoolean(HandicapMain.HANDICAPDEBUG, true);	// Set debug on
				}
				
			}
		});
//		btnPreferencesDebug.setBounds(453, 573, 117, 29);
		panelSouth.add(btnPreferencesDebug);
		
/*
 * 		End of GUI setup
 */

/*
 * 		Load themes for "metal"
 */
		comboBoxPreferencesTheme.addItem("DefaultMetal");						// Ocean or DefaultMetal if Metal
		comboBoxPreferencesTheme.addItem("Ocean");
		
		String lAndF = HandicapMain.handicapPrefs.get(HandicapMain.HANDICAPTHEME, HandicapMain.NOTH);					// Them from prefs
		if (!lAndF.equals(HandicapMain.NOLF))										// Any theme there
			comboBoxPreferencesTheme.setSelectedItem(lAndF);			// Yes, display in combobox
		
/*
 * 		Load L&F 	
 */
		comboBoxPreferencesLookAndFeel.addItem("Metal");					// "Metal", "System", "Motif", Windows
		comboBoxPreferencesLookAndFeel.addItem("System");
		comboBoxPreferencesLookAndFeel.addItem("Motif");
		if (!(HandicapMain.operatingSystem.startsWith("Win") || HandicapMain.operatingSystem.startsWith("Mac")))
			comboBoxPreferencesLookAndFeel.addItem("GTK");
		if (HandicapMain.operatingSystem.startsWith("Win"))		
				comboBoxPreferencesLookAndFeel.addItem("Windows");
		comboBoxPreferencesLookAndFeel.addItem("Nimbus");	
		
		lAndF = HandicapMain.handicapPrefs.get(HandicapMain.HANDICAPLOOKANDFEEL, HandicapMain.NOLF);	// Look and feel from prefs
		if (!lAndF.equals(HandicapMain.NOLF))										// Anything set?
		{
			comboBoxPreferencesLookAndFeel.setSelectedItem(lAndF);		// Yes, show it in combobox
			if (!lAndF.equals("Metal"))									// Meta?
			{
				comboBoxPreferencesTheme.setVisible(false);				// No, make theme box invisible
				lblPreferencesTheme.setVisible(false);
				HandicapMain.handicapPrefs.remove(HandicapMain.HANDICAPTHEME);					// Delete any them from prefs
			}
		}
		HandicapMain.fillingLookAndFeelComboBox = false;				// Done filling
		


	}

}
