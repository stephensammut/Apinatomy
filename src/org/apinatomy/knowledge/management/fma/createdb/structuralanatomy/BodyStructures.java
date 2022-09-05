package org.apinatomy.knowledge.management.fma.createdb.structuralanatomy;

import java.sql.*;
import java.util.HashMap;
import java.util.Map;

import org.apinatomy.knowledge.management.fma.createdb.sql.DBConnections;
import org.apinatomy.knowledge.management.fma.createdb.sql.InsertSQLQuery;
import org.apinatomy.knowledge.management.fma.createdb.utilities.Vocabulary;

public class BodyStructures {
	// Queries the main FMA database table (fma_local) and extracts the contents
	// of columns "frame", "slot" and "short_value" if slot=FMAID/definition/DIRECT-TYPE 
	// which are then used to construct the ApiNATOMY structures table
	
	
    private static Map<String, String[]> identifyAllBodyStructuresInFMA() {
		//gets a list of all entries within the fma database that have an associated fma id 

		Map<String, String[]> structureDetailsMap = new HashMap<String, String[]>();
		
		try {
			Statement st = DBConnections.getFMAConnection().createStatement();
			String statement = 
				"SELECT frame, slot, short_value from fma WHERE (frame NOT LIKE \"FM_%\" AND frame NOT LIKE \"N_fma%\" AND frame NOT LIKE \"test_live%\" AND frame NOT LIKE \"KB_%\" AND frame NOT LIKE \"ONARD%\" AND frame NOT LIKE \"Nance%\" AND frame NOT LIKE \"fm%\" AND frame NOT LIKE \"su_%\" AND frame NOT LIKE \"August%\" AND frame NOT LIKE \"live_incus_fm%\" AND frame NOT LIKE \"N_fma%\" AND frame NOT LIKE \"Copy of ONARD_%\") AND (slot=\"FMAID\" or slot=\"definition\" or slot=\":DIRECT-TYPE\");";
			ResultSet rs = st.executeQuery(statement);
			while (rs.next()) {
				String frame = rs.getString("frame");
				String slot = rs.getString("slot");
				String short_value = rs.getString("short_value");
				String structureDetails[];
				//check whether the structure is already in the Map. If so, extract all info in structureDetails array [value]
				//array_structure [fmaid] [direct_type] [definition]
				
				if (structureDetailsMap.containsKey(frame)) structureDetails = structureDetailsMap.get(frame);
				else structureDetails = new String[3];
				
				if (slot.equals("FMAID")){
					structureDetails[0]=short_value; 
					Vocabulary.insert(frame, short_value);
				}
				
				else if (slot.equals(":DIRECT-TYPE"))
					structureDetails[1]=short_value;
				else if (slot.equals("definition"))
					structureDetails[2]=short_value;
				
				structureDetailsMap.put(frame, structureDetails);
			}
			rs.close();
			st.close();
		} 
		catch (SQLException ex){
			ex.printStackTrace();
			System.exit(1);
		}
		return structureDetailsMap;
	}	
    
	public static void createStructuresTable(){
		Map<String,String[]> structuresMap = identifyAllBodyStructuresInFMA();
		InsertSQLQuery.populateStructuresTable(structuresMap);
        structuresMap.clear();
	}
	
}
