package org.apinatomy.knowledge.management.fma.createdb.sql;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Statement;
import java.util.ArrayList;



public class SQLSchema {

	
	public static ArrayList<String> loadSchemaFromFile(String schemaFileName){

		//load SQL schema from file. Allow for comments and commands spanned on multiple lines
		
		ArrayList<String> sqlSchema = new ArrayList<String>();
		String SQLStatement=""; 
		try {
		    BufferedReader in = new BufferedReader(new FileReader(schemaFileName));
		    String str;
		    while ((str = in.readLine()) != null) {
		    	
		    	if (str.startsWith("--")){
		    		//this is a comment, add as is
		    		sqlSchema.add(str);
		    	}
		    	else {
		    		if (!str.endsWith(";")) {
		    			SQLStatement = SQLStatement+str;
		    		}
		    		else if (str.endsWith(";")) {
		    			SQLStatement += str;
		    			sqlSchema.add(SQLStatement);
		    			SQLStatement="";
		    		}
		    	}
		    }
		    in.close();
		    return sqlSchema;

		} catch (IOException e) {
			e.printStackTrace();
			System.exit(0);
		}
		return null;
	}

    public static void implementSchema(String schemaFileName) {
    	// loads the SQL schema from SQL file and dumps within ApiNATOMY database
    	ArrayList<String> schema = SQLSchema.loadSchemaFromFile(schemaFileName);
    	
    	try{
    		Statement schemaStatement = DBConnections.getApinatomyConnection().createStatement();
    		for (int i=0;i<schema.size();i++){
    			schemaStatement.executeUpdate(schema.get(i).toString());
    		}
    		schemaStatement.close();
    		schema.clear();
    	}
    	catch (Exception e){
    		e.printStackTrace();
    	}
    }
    
}
