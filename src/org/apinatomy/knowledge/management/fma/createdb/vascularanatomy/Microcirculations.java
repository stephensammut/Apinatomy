package org.apinatomy.knowledge.management.fma.createdb.vascularanatomy;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashSet;

import org.apinatomy.knowledge.management.fma.createdb.sql.DBConnections;
import org.apinatomy.knowledge.management.fma.createdb.sql.InsertSQLQuery;
import org.apinatomy.knowledge.management.fma.createdb.utilities.ConvertCase;
import org.apinatomy.knowledge.management.fma.createdb.utilities.FMAIDManager;
import org.apinatomy.knowledge.management.fma.createdb.utilities.Vocabulary;

public class Microcirculations {

	
	static HashSet<String> vascularFieldConnections = new HashSet<String>();
	
	public static void importMicrocirculationsFromFMA(String type){
		ArrayList<String[]> newStructures = new ArrayList<String[]>();
		ArrayList<String[]> microcirculations = new ArrayList<String[]>();
		
		String slot=null;
		
	    try {
	        Statement stmt_c = DBConnections.getFMAConnection().createStatement();
	        if (type.equals("ART")) slot="arterial supply";
	        else if (type.equals("VEN")) slot="venous drainage";
	        
	        String query = "select frame,short_value from fma where slot =  \""+slot+"\" and short_value != \"Class\" and short_value != \"Portion of tissue\""; 
	        
	        ResultSet res_c = stmt_c.executeQuery(query);
	        while (res_c.next()){
	        	String microcirculation = res_c.getString("frame");
	        	String vessel = res_c.getString("short_value");
	        	
	        	//make sure that vessel is a trunk 
	        	String vessel_trunk = "Trunk of "+ ConvertCase.toLowerCase(vessel);
	        	String vessel_id="";
	        	if (Vocabulary.containsFMATerm(vessel_trunk)) {
	        		vessel_id = Vocabulary.getFMAID(vessel_trunk);
					if (type.equals("ART")) VascularCircuits.insertInArterialCircuit(vessel_id);
					if (type.equals("VEN")) VascularCircuits.insertInVenousCircuit(vessel_id);
	        	}
	        	else {
	        		vessel_id=Integer.toString(FMAIDManager.getNewFMAID(vessel_trunk));
	        		Vocabulary.insert(vessel_trunk, vessel_id);
					newStructures.add(new String[] {vessel_id, vessel_trunk,"",""});
					if (type.equals("ART")) VascularCircuits.insertInArterialCircuit(vessel_id);
					if (type.equals("VEN")) VascularCircuits.insertInVenousCircuit(vessel_id);
	        	}
	        	
	        	String organ_id = Vocabulary.getFMAID(microcirculation);
      	    	microcirculations.add(new String[] {vessel_id, organ_id,type,"Y"});
      	    	vascularFieldConnections.add(vessel_id+"_"+organ_id);
	        }
	    
	        res_c.close();
	        stmt_c.close();
	        
			String structureArray[][] = new String[newStructures.size()][4];
			for (int i=0;i<newStructures.size();i++){
				String values[] = newStructures.get(i);
				structureArray[i][0] = values[0];
				structureArray[i][1] = values[1];
				structureArray[i][2] = "";
				structureArray[i][3] = "";
			}
			InsertSQLQuery.insert2DArrayInTable("structures",structureArray);
			
			String organVasculatureArray[][] = new String[microcirculations.size()][4];
			for (int i=0;i<microcirculations.size();i++){
				String values[] = microcirculations.get(i);
				organVasculatureArray[i][0] = values[0];
				organVasculatureArray[i][1] = values[1];
				organVasculatureArray[i][2] = values[2];
				organVasculatureArray[i][3] = values[3];
			}
			InsertSQLQuery.insert2DArrayInTable("microcirculations",organVasculatureArray);	
	        
	        
	    }  catch (SQLException ex){
	        System.err.println("SQL Error: " + ex.getMessage());
	        ex.printStackTrace();
	    }
	}
	
	
	
	
	
}
