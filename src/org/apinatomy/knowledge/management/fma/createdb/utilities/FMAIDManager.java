package org.apinatomy.knowledge.management.fma.createdb.utilities;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.HashMap;


public class FMAIDManager {

	private static int apinatomyID=0;
	
	private static HashMap<String,Integer> staticApinatomyIDVocab = new HashMap<String,Integer>();
	
	public static void loadStaticIDsFromFile(){
		
		//load next available fma from properties file
		apinatomyID = ProgramProperties.getNextFMAidFromPropertiesFile();
		if (apinatomyID==0){
			System.out.println("[WARNING] Incorrect ApiNATOMY ID counter specified in properties file. Aborting.");
			System.exit(0);
		}
		
		//load static list of fma ids that had been generated from previous releases
		try {
			File file = new File(ProgramProperties.getStaticApinatomyIDsFilePath());
			BufferedReader reader = new BufferedReader(new FileReader(file));
			String text = null;
			while ((text = reader.readLine()) != null){
				String[] str = text.split(",");
				str[0] = str[0].replaceAll("\"", "");
				staticApinatomyIDVocab.put(str[0], Integer.parseInt(str[1]));
			}
			reader.close();
		}
		catch (Exception e){
			e.printStackTrace();
		}
	}
	
	
	public static int getNewFMAID (String description){
		//1. check whether a 'static' apinatomyID is available within the patch file
		if (staticApinatomyIDVocab.containsKey(description)){
			return staticApinatomyIDVocab.get(description);
		}
		
		//2. if not, returns the next available id for inclusion within database
		else {
			apinatomyID++;
			return apinatomyID;
		}
	}
	
	
}
