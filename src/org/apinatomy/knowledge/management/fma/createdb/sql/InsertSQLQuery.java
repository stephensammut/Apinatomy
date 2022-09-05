package org.apinatomy.knowledge.management.fma.createdb.sql;

import java.sql.SQLException;
import java.sql.Statement;
import java.util.Iterator;
import java.util.Map;


public class InsertSQLQuery {

	
	public static void runSQLQuery(String sqlQuery){
		try {
			Statement insertStmt = DBConnections.getApinatomyConnection().createStatement();
		//	insertStmt.executeUpdate (sqlQuery);
			insertStmt.close();
		} 
		catch (Exception e) {
			System.err.println("This SQL query caused an error!: "+sqlQuery);
			e.printStackTrace();
		}
	}
	
	
	public static void populateStructuresTable(Map<String,String[]> structuresMap) {
		//inserts structure data within structures table
		String query = "INSERT INTO structures VALUES ";

		try {
			int i=0;
			String structureName="";
			String[] data;
			Statement insertStmt = DBConnections.getApinatomyConnection().createStatement();
			
			Iterator<String> it = structuresMap.keySet().iterator();
			while (it.hasNext()){
				structureName = it.next().toString();
	            data = structuresMap.get(structureName);
	            if (data[2] != null)
	            	data[2] = data[2].replaceAll("\"", "'");
	            query+="(\""+data[0]+"\", \""+structureName+"\", \""+data[1]+"\",\""+data[2]+"\"), ";
	            i++;
	            
	            if (i==1000){
	    			query = query.substring(0,query.length()-2);
	                insertStmt.executeUpdate (query);
	                query="INSERT INTO structures VALUES ";
	                i=0;
	            }
			}
			query = query.substring(0,query.length()-2);
         //   insertStmt.executeUpdate (query);
            insertStmt.close();	 
            structuresMap.clear();
		} 
		
		catch (SQLException sqlException) {
	            System.err.println("[SQL ERROR]: "+sqlException.getMessage());
	            System.err.println("This query has caused an error: "+query);
	            sqlException.printStackTrace();
	            System.exit(1);
		}
	}
	
	public static void insertSingleQueryInTable(String query){
		try{
			Statement tableStmt = DBConnections.getApinatomyConnection().createStatement();
			//tableStmt.executeUpdate(query);
			tableStmt.close();
		}
		catch (Exception e){
			System.err.println("This SQL query generated an error: "+query);
			e.printStackTrace();
		}

	}
	
	public static void insert2DArrayInTable(String tableName, String[][] data){
		int noColsTable = data[0].length; //get y in data[x][y]
    	String sqlQuery="";

        try {
        	String tempString="";
        	String queryString="";
        	Statement insertStmt = DBConnections.getApinatomyConnection().createStatement();
        	
        	int counter=0;
        	for (int j=0;j<data.length;j++){
        		tempString="(";
        		for (int cols=0;cols<noColsTable;cols++){ 
        			tempString += "\""+data[j][cols]+"\", ";
        		}
        		tempString=tempString.substring(0,tempString.length()-2)+"), ";
        		queryString+=tempString;
        		counter++;
        		
        		if (counter==700) {
        			counter=0;
                	queryString=queryString.substring(0,queryString.length()-2);
                	sqlQuery="INSERT INTO "+tableName+ " VALUES "+queryString;
                	System.out.println(sqlQuery);
                  //  insertStmt.executeUpdate (sqlQuery);
                    queryString = "";
        		}
        	}
        	queryString=queryString.substring(0,queryString.length()-2);
        //	sqlQuery="INSERT INTO "+tableName+ " VALUES "+queryString;
        	System.out.println(sqlQuery);

            insertStmt.executeUpdate (sqlQuery);
            insertStmt.close();
        } 
        catch (SQLException sqlException) {
            System.err.println("SQL error: "+sqlException.getMessage());
            System.err.println("This query caused an error! : "+sqlQuery);
            sqlException.printStackTrace();
        }
	}
}
