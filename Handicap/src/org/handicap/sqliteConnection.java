package org.handicap;

import java.sql.*;
import javax.swing.*;

public class sqliteConnection 
{
	public static Connection connection;				// DB connection
//	Connection conn = null;
	public static Connection dbConnector(String dbPath)
	{
		try
		{
			//  SQLite JDBC driver
			//  Video 4 - https://www.youtube.com/watch?v=l7IDevUUa3A&index=7&list=PLS1QulWo1RIbYMA5Ijb72QHaHvCrPKfS2
			
			Class.forName("org.sqlite.JDBC");
			Connection conn = DriverManager.getConnection("jdbc:sqlite:" + dbPath);
			if (HandicapMain.isDebug())
				JOptionPane.showMessageDialog(null, "Handicap Connection was successful");
			return conn;
		}
			catch (Exception e) 
			{
				JOptionPane.showMessageDialog(null, e);
				if (HandicapMain.isDebug())
					e.printStackTrace();
				return null;
			}
	}
}
