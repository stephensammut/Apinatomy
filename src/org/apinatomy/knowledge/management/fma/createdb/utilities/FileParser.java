package org.apinatomy.knowledge.management.fma.createdb.utilities;

import java.io.BufferedReader;
import java.io.FileReader;

import org.apinatomy.knowledge.management.fma.createdb.sql.InsertSQLQuery;

public class FileParser {


    public static boolean containsOnlyNumbers(String str) {
        if (str == null || str.length() == 0)
            return false;
        
        for (int i = 0; i < str.length(); i++) {
            if (!Character.isDigit(str.charAt(i)))
                return false;
        }
        return true;
    }
    
	public static void parseCSVFile(String csvFilePath){
	
		   try{
			   FileReader reader = new FileReader(csvFilePath);
		        BufferedReader inFile = new BufferedReader(reader);
		        String str=null;
		        String tableName=null;
		        String action=null;
		        String values=null;
		        
		        while ((str = inFile.readLine()) != null) {
		        	String params[] = str.split(",");
		        	
		      	    for (int i = 0; i < params.length; i++) {
		      	    	tableName = params[0];
		      	    	action = params[1];
		      	    	values = params[2];
		      	    }
		      	    values = values.replaceAll("\\{", "");
		      	    values = values.replaceAll("\\}", "");
		      	    String data[] = values.split(";");
		      	    if (action.toUpperCase().equals("INSERT")){
		 //     	    	System.out.print("Inserting: ");
		      	    	String query = "INSERT INTO "+tableName+" values (";
		      	    	
		      	    	for (int j=0;j<data.length;j++){
		      	    		String fmaid = Vocabulary.getFMAID(data[j]);
		      	    		if (fmaid!=null){ 
		      	    			query+="\""+fmaid+"\"";
		   //   	    			System.out.print(data[j]+" ["+fmaid+"] already exists in fma");
		      	    		}
		      	    		
		      	    		else {
		      	    			if (data[j].equals("Y") || data[j].equals("N") ||
		      	    					data[j].equals("ART") || data[j].equals("VEN") ||
		      	    					containsOnlyNumbers(data[j])) {
		      	    					query +="\""+data[j]+"\"";
			      	    			//		if (containsOnlyNumbers(data[j])) System.out.print (Vocabulary.getDescription(data[j])+" ["+ data[j]+"] id entered in file");
		      	    			}
		      	    			else {
		      	    				//insert as a new structure within the FMA
		      	    				String id = Integer.toString(FMAIDManager.getNewFMAID(data[j]));
		      	    				Vocabulary.insert(data[j], id);
		      	    				query+="\""+id+"\"";
		     // 	    				System.out.print(" "+data[j]+" ["+id+"] ");
		      	    				String query2="insert into structures values ("+id+",\""+data[j]+"\",\"\",\"\")";
		      	    				InsertSQLQuery.insertSingleQueryInTable(query2);
		      	    				//if this is a trunk, check that the artery exists
		      	    				if (data[j].contains("Trunk of ")){
		      	    					String nonTrunk = data[j];
		      	    					nonTrunk = nonTrunk.replace("Trunk of ", "");
		      	    					nonTrunk = ConvertCase.toUpperCase(nonTrunk);
		      	    					if (!Vocabulary.containsFMATerm(nonTrunk)){
		      	    						id = Integer.toString(FMAIDManager.getNewFMAID(nonTrunk));
				      	    				Vocabulary.insert(nonTrunk, id);
				      	    				query2="insert into structures values ("+id+",\""+nonTrunk+"\",\"\",\"\")";
				      	    				InsertSQLQuery.insertSingleQueryInTable(query2);
		      	    					}
		      	    				}
		      	    			}
		      	    		}
		      	    		if (data.length != ((j)+1))
		      	    			query+=",";		      	    		
		      	    	}
		      	    	query += ");";
	      	    	//	System.out.println();

		      	  	InsertSQLQuery.insertSingleQueryInTable(query);
		      	    }
		      	    
		      	    
		      	    }
		        reader.close();
		   }
		   catch (Exception e){
			   e.printStackTrace();
		   }
		      	    
	}

}
