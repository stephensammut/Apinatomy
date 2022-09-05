package org.apinatomy.knowledge.management.fma.createdb.utilities;

import java.util.HashMap;
import java.util.Map;

public class Vocabulary {

	// Description, FMAID
	private static Map<String, String> vocab = new HashMap<String, String>();
	
	//FMAID, Description
	private static Map<String, String> invertedVocab = new HashMap<String, String>();

	public static String getFMAID(String description){
		return vocab.get(description);
	}
	
	public static String getDescription(String fmaid){
		return invertedVocab.get(fmaid);
	}	
	
	//insert FMAID+Description in HashMaps	
	public static void insert(String description, String fmaid){
		vocab.put(description, fmaid);
		invertedVocab.put(fmaid, description);
	}
		
	public static boolean containsFMATerm(String key){
		if (vocab.containsKey(key)) return true;
		else return false;
	}
	
	public static boolean containsFMAID(String key){
		if (invertedVocab.containsKey(key)) return true;
		else return false;
	}
	
	public static Map<String, String> getAllVocabulary(){
		return vocab;
	}
	
	public static Map<String, String> getAllInvertedVocabulary(){
		return invertedVocab;
	}
	
}
