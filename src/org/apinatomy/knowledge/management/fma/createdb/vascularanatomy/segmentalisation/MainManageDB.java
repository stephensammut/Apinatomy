package org.apinatomy.knowledge.management.fma.createdb.vascularanatomy.segmentalisation;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;


import org.apinatomy.knowledge.management.fma.createdb.sql.DBConnections;
import org.apinatomy.knowledge.management.fma.createdb.utilities.Vocabulary;

public class MainManageDB{

	// get a list of all fmaids and their corresponding annotation in the structures table
	private static void getVocabulary() {
		try {
			Statement st = DBConnections.getApinatomyConnection().createStatement();
			String statement="select fma_id, structure_name from structures;";

			ResultSet rs = st.executeQuery(statement);
			while (rs.next()) {
				Vocabulary.insert(rs.getString("structure_name"),rs.getString("fma_id"));
			}
			rs.close();
			st.close();
		} catch (SQLException ex){
			System.err.println("Error: " + ex.getMessage());
			ex.printStackTrace();
			System.exit(1);
		}
	}

	//main class
	public static void main(String args[])  {
		//connect to SQL datatabase
		DBConnections.initialiseConnections("localhost","fma_db","apinatomy_db","username","password");
		
		//load vocabulary in hashmap
		getVocabulary();
		
		try{
			// go to segment generation classes
			SegmentGenerator.main();

		}
		catch (Exception e){
			e.printStackTrace();
		}
	}
}
