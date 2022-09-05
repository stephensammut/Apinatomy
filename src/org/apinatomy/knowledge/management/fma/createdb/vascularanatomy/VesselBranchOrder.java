package org.apinatomy.knowledge.management.fma.createdb.vascularanatomy;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import org.apinatomy.knowledge.management.fma.createdb.sql.DBConnections;
import org.apinatomy.knowledge.management.fma.createdb.sql.InsertSQLQuery;
import org.apinatomy.knowledge.management.fma.createdb.utilities.ConvertCase;
import org.apinatomy.knowledge.management.fma.createdb.utilities.FMAIDManager;
import org.apinatomy.knowledge.management.fma.createdb.utilities.Vocabulary;

public class VesselBranchOrder {

	static HashMap<String, String[]> newStructures = new HashMap<String, String[]>();
	
	static ArrayList<String[]> tempBranchingOrderTable = new ArrayList<String[]>();
	static ArrayList<String[]> finalBranchingOrderTable = new ArrayList<String[]>();



	private static void insertBranchOrderInDB(){
		for (int i=0;i<finalBranchingOrderTable.size();i++){
			String structure[] = finalBranchingOrderTable.get(i);
			String query="INSERT INTO branching_order VALUES (\""+structure[0]+"\", \""+structure[1]+"\", \""+structure[2]+"\");";
			try {
				Statement insertStmt = DBConnections.getApinatomyConnection().createStatement();
				insertStmt.executeUpdate (query);
				insertStmt.close();
			} 
			catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	private static void decipherAnnotationAndAmendDB(String text, String mainArteryFMAID, String newBranchFMAID){
		if (text.contains("WAS_BRANCH_OF")){
			//ie was incorrectly labelled as a branch of another artery
			String wasBranchOf = text.substring(text.indexOf("WAS_BRANCH_OF")+14, text.indexOf("}"));
			
			String wasBranchOfFMAID = null;
			if (Vocabulary.containsFMATerm(wasBranchOf)) wasBranchOfFMAID = Vocabulary.getFMAID(wasBranchOf);
			if (wasBranchOfFMAID==null){System.out.println("NO FMAID for: "+wasBranchOf);System.exit(1);}
			//1. delete old connection (error)
			InsertSQLQuery.runSQLQuery("DELETE FROM arterial_network where vessel_from = "+wasBranchOfFMAID+" and vessel_to = "+newBranchFMAID);
		}

		// ie this branch does not feature within the fma 
		if (!EstablishVascularConnectivity.arterialConnections.contains(mainArteryFMAID+"_"+newBranchFMAID)){
			InsertSQLQuery.runSQLQuery("INSERT INTO arterial_network values ("+mainArteryFMAID+", "+newBranchFMAID+")");
			EstablishVascularConnectivity.arterialConnections.add(mainArteryFMAID+"_"+newBranchFMAID);
		}
	}
	
	private static void processFile(String fileName){
		int currentBranchOrder=0;
		String branchArray[] = new String[20]; //20 branches maximum
		tempBranchingOrderTable.clear();
		finalBranchingOrderTable.clear();

		
		File file = new File(fileName);
		BufferedReader reader = null;
		try {
			reader = new BufferedReader(new FileReader(file));
			String text = null;
			while ((text = reader.readLine()) != null){
				
				if (text.contains("[branch")){
					String artery = text.substring(text.indexOf("]")+3);
					artery = artery.replace("Trunk_of", "Trunk of");

					// check whether artery exists in fma
					String fmaID="";
					if (!Vocabulary.containsFMATerm(artery)){
						// FMA term does not exist, create new entity
						//check whether nontrunked version exists
						String nonTrunk = artery.replaceAll("Trunk of ", "");
						nonTrunk = ConvertCase.toUpperCase(nonTrunk);
						if (!Vocabulary.containsFMATerm(nonTrunk)){
							fmaID = Integer.toString(FMAIDManager.getNewFMAID(nonTrunk));
							Vocabulary.insert(nonTrunk, fmaID);
							newStructures.put(nonTrunk, new String[] {fmaID,"","",""});					
						}
						
						fmaID = Integer.toString(FMAIDManager.getNewFMAID(artery));
						newStructures.put(artery, new String[] {fmaID,"","",""});
						VascularCircuits.insertInArterialCircuit(fmaID);
						Vocabulary.insert(artery, fmaID);
					}
					else {
						fmaID = Vocabulary.getFMAID(artery);
					}
					currentBranchOrder = Integer.parseInt(text.substring(text.indexOf("[branch")+7, text.indexOf("]")));
					
					if (text.contains("{")){
						//the { is considered as a special symbol in this class.
						//indicative of further manual annotations that have been done
						decipherAnnotationAndAmendDB(text, branchArray[currentBranchOrder-1],fmaID);
						//ie full line of text, FMAID of main artery, FMAID of branch
					}

					
					if (branchArray[currentBranchOrder]==null) {
						//never filed before
						//branchArray[currentBranchOrder]=artery;
						branchArray[currentBranchOrder]=fmaID;
						//if (currentBranchOrder>0) tempBranchingOrderTable.add(new String[] {branchArray[currentBranchOrder-1], artery});
						if (currentBranchOrder>0) tempBranchingOrderTable.add(new String[] {branchArray[currentBranchOrder-1], fmaID});

					}
					
					else if (branchArray[currentBranchOrder]!=null) {
						//filled before
						// delete all distal nodes
						for (int o=currentBranchOrder;o<20;o++){
							branchArray[o]=null;
						}
						//branchArray[currentBranchOrder]=artery;
						branchArray[currentBranchOrder]=fmaID;
						//if (currentBranchOrder>0) tempBranchingOrderTable.add(new String[] {branchArray[currentBranchOrder-1], artery});
						if (currentBranchOrder>0) tempBranchingOrderTable.add(new String[] {branchArray[currentBranchOrder-1], fmaID});

					}
			
				}
				else {
					//ignore that line since doesn't contain word '[branch'
				}			
							
			}
			reader.close();
			
			//all branches are now tabulated
			//get ordering
			
			int length = tempBranchingOrderTable.size();
			
			while (length>0){
			String mainArtery = tempBranchingOrderTable.get(0)[0];
			int orderNo=0;
			ArrayList<Integer> retainPositions = new ArrayList<Integer>();
			
			for (int l=0;l<length;l++){
				String[] row = tempBranchingOrderTable.get(l);
				String mainA = row[0];
				String branchA = row[1];
				if (mainA.equals(mainArtery)){
					orderNo++;
					finalBranchingOrderTable.add(new String[] {mainA, branchA, Integer.toString(orderNo)});
					tempBranchingOrderTable.remove(l);
					tempBranchingOrderTable.add(l, new String[] {"null", "null"});
					}
				else 	retainPositions.add(l);

				}
				
			ArrayList<String[]> tempList = new ArrayList<String[]>();
			tempList.addAll(tempBranchingOrderTable);
			tempBranchingOrderTable.clear();
			
			Iterator<Integer> im = retainPositions.iterator();
			while (im.hasNext()){
				int counter = Integer.parseInt(im.next().toString());
				tempBranchingOrderTable.add(tempList.get(counter));
			}			
			
			tempList.clear();
			length = tempBranchingOrderTable.size();
		
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		} 
	}
	
		
	public static void startBranchOrderInsertion(String branchOrderFileName){
		processFile(branchOrderFileName);
		insertBranchOrderInDB();     
        InsertSQLQuery.populateStructuresTable(newStructures);		
	}
	

}
