package org.apinatomy.knowledge.management.fma.createdb.vascularanatomy;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;

import org.apinatomy.knowledge.management.fma.createdb.sql.InsertSQLQuery;
import org.apinatomy.knowledge.management.fma.createdb.utilities.ConvertCase;
import org.apinatomy.knowledge.management.fma.createdb.utilities.FMAIDManager;
import org.apinatomy.knowledge.management.fma.createdb.utilities.Vocabulary;

public class VesselStructure {

	 public static void createWallAndLumen (HashSet<String> vesselSet){
	    	
	    	//query the fma database, and create walls and luminas for all vessels
		 
	    	ArrayList<String[]> constitutionalParts = new ArrayList<String[]>();
	    	Map<String, String[]> vesselLumenAndWallMap = new HashMap<String, String[]>();
	    	
	    	try {		
	    		Iterator<String> it = vesselSet.iterator();
	    		while (it.hasNext()){
	    			String vessel = it.next().toString();
	    			String wall = "Wall of "+ConvertCase.toLowerCase(vessel);
	    			String lumen = "Lumen of "+ConvertCase.toLowerCase(vessel);

	    			if (!Vocabulary.containsFMATerm(wall)){
	    				String newFMAID = Integer.toString(FMAIDManager.getNewFMAID(wall));
	    				Vocabulary.insert(wall,newFMAID);
	    				vesselLumenAndWallMap.put(wall, new String[] {newFMAID, "", ""});
	    				constitutionalParts.add(new String[] {Vocabulary.getFMAID(vessel), newFMAID});    			
	    			}
	    		
	    			if (!Vocabulary.containsFMATerm(lumen)) {
	    				String newFMAID = Integer.toString(FMAIDManager.getNewFMAID(lumen));
	    				Vocabulary.insert(lumen,newFMAID);
	    				vesselLumenAndWallMap.put(lumen, new String[]{newFMAID, "", ""});
	    				constitutionalParts.add(new String[] {Vocabulary.getFMAID(vessel), newFMAID});
	    			}
	    		}
	    		
	    		InsertSQLQuery.populateStructuresTable(vesselLumenAndWallMap);
	    		String constitutionalPartsArray[][] = new String[constitutionalParts.size()][2];
	    		for (int i=0;i<constitutionalParts.size();i++){
	    			String values[] = constitutionalParts.get(i);
	    			constitutionalPartsArray[i][0] = values[0];
	    			constitutionalPartsArray[i][1] = values[1];
	    		}

	    		InsertSQLQuery.insert2DArrayInTable("constitutional_parts",constitutionalPartsArray);
	    		constitutionalParts.clear();
	    		vesselLumenAndWallMap.clear();
	    	}
	    	catch (Exception e){
	    		e.printStackTrace();
	    	}
	    }
	 	 
	 
}
