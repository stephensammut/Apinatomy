package org.apinatomy.knowledge.management.fma.createdb.sql;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnections {
	
	private static Connection fmaDatabaseConnection=null;
	private static Connection apinatomyDatabaseConnection=null;

    private static Connection createConnection(String host, String db, String username, String password) {
	    Connection connection = null;
	    try {
	        Class.forName("com.mysql.jdbc.Driver").newInstance();
	        connection = DriverManager.getConnection("jdbc:mysql://"+host+"/"+db+"?" +
	                                           "user="+username+"&password="+password);
	    } catch (SQLException ex) {
	        System.err.println("Database error: " + ex.getMessage());
	        System.exit(0);
	    } catch (Exception e) {
	        System.err.println("Database error: " + e.getMessage());
	        System.exit(0);
	    }
	    return connection;
	}
    
    public static Connection getFMAConnection() {
    	return fmaDatabaseConnection;
    }

    public static Connection getApinatomyConnection() {
    	return apinatomyDatabaseConnection;
    }
    
    public static void initialiseConnections(String host, String fmadb, String apinatomydb, String user, String passwd){
		fmaDatabaseConnection = createConnection(host,fmadb,user,passwd);
		apinatomyDatabaseConnection = createConnection(host,apinatomydb,user,passwd);
    }	
    
}
