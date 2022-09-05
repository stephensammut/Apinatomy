package org.apinatomy.knowledge.management.fma.createdb.vascularanatomy;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;

import org.apinatomy.knowledge.management.fma.createdb.sql.DBConnections;
import org.apinatomy.knowledge.management.fma.createdb.sql.InsertSQLQuery;
import org.apinatomy.knowledge.management.fma.createdb.utilities.ConvertCase;
import org.apinatomy.knowledge.management.fma.createdb.utilities.FMAIDManager;
import org.apinatomy.knowledge.management.fma.createdb.utilities.Vocabulary;

public class EstablishVascularConnectivity {

public static HashSet<String> arterialConnections = new HashSet<String>();
public static HashSet<String> venousConnections = new HashSet<String>();
private static ArrayList<String> fromSinus;
private static ArrayList<String> toSinus;

	public static void getArterialConnectionsFromFMA(HashSet<String> setOfArteries) {
		String fromArteryFMAID="";
		String toArteryFMAID="";

		ArrayList<String[]> newStructures = new ArrayList<String[]>();

		boolean found=false;
		
		try {		
			Iterator<String> arteryIterator = setOfArteries.iterator();
			
			while (arteryIterator.hasNext()){
				String arteryToAnalyse = arteryIterator.next();
				VascularCircuits.insertInArterialCircuit(Vocabulary.getFMAID(arteryToAnalyse));
				found=false;

				//Is the arteryToAnalyse a trunk? if so, retain its fma id. 
				//If not, create a trunk
								
				if (arteryToAnalyse.contains("Trunk ")|| arteryToAnalyse.contains(" trunk")){ 
					fromArteryFMAID = Vocabulary.getFMAID(arteryToAnalyse);	
					
					//the trunk form does exist, but does the nontrunkalised arterial counterpart exist?
					String arterialNonTrunked = arteryToAnalyse.replace("Trunk of ", "");
					arterialNonTrunked = ConvertCase.toUpperCase(arterialNonTrunked);
					if (!Vocabulary.containsFMATerm(arterialNonTrunked)) {
						String newFmaID =  Integer.toString(FMAIDManager.getNewFMAID(arterialNonTrunked)); 
						Vocabulary.insert(arterialNonTrunked, newFmaID);
						newStructures.add(new String[] {newFmaID, arterialNonTrunked,"",""});
					}
				}
				else {
					String arterialTrunk = "Trunk of "+ConvertCase.toLowerCase(arteryToAnalyse);
					if (Vocabulary.containsFMATerm(arterialTrunk)) {
						//arteryToAnalyse is not a trunk, however the trunkal form already exists within the fma
						fromArteryFMAID = Vocabulary.getFMAID(arterialTrunk);
						VascularCircuits.insertInArterialCircuit(fromArteryFMAID);
					}
					
					else {
						//arteryToAnalyse is not a trunk, and the trunkal form of the vessel does not exist
						fromArteryFMAID = Integer.toString(FMAIDManager.getNewFMAID(arterialTrunk));
						Vocabulary.insert(arterialTrunk, fromArteryFMAID);
						newStructures.add(new String[] {fromArteryFMAID, arterialTrunk,"",""});
						VascularCircuits.insertInArterialCircuit(fromArteryFMAID);
					}
				}
				
				String statement = "select slot, short_value from fma where frame = \""+arteryToAnalyse+"\" and (slot='continuous with proximally' or slot='continuous with distally' or slot='branch' or slot='branch of' or slot='regional part') and (short_value like '%arter%' or short_value like '%aorta%')";
				Statement st = DBConnections.getFMAConnection().createStatement();
				ResultSet rs = st.executeQuery(statement);
				while (rs.next()) {
					found=true;
					toArteryFMAID="";
					String slot = rs.getString("slot");
					String result = rs.getString("short_value");
					
					//however we only add trunks
					if (result.contains("Trunk ")|| result.contains(" trunk")|| result.contains(" plexus")){
						toArteryFMAID = Vocabulary.getFMAID(result);
						//is a trunk. does the parent artery exist?
						String toArteryWithoutTrunk = result.replace("Trunk of ", "");
						toArteryWithoutTrunk = ConvertCase.toUpperCase(toArteryWithoutTrunk);
						if (!Vocabulary.containsFMATerm(toArteryWithoutTrunk)) {
							String newArteryWithoutTrunkID = Integer.toString(FMAIDManager.getNewFMAID(toArteryWithoutTrunk));
							Vocabulary.insert(toArteryWithoutTrunk, newArteryWithoutTrunkID);
							newStructures.add(new String[] {newArteryWithoutTrunkID, toArteryWithoutTrunk,"",""});
							VascularCircuits.insertInArterialCircuit(newArteryWithoutTrunkID);
							toArteryWithoutTrunk = ConvertCase.toLowerCase(toArteryWithoutTrunk);
						}
					}
					else {
						//the result is not a trunk. Search in vocabulary - does the trunk exist?
						String newToTrunk = "Trunk of "+ConvertCase.toLowerCase(result);
						if (Vocabulary.containsFMATerm(newToTrunk)) {
							toArteryFMAID = Vocabulary.getFMAID(newToTrunk);
							VascularCircuits.insertInArterialCircuit(toArteryFMAID);
						}
						else {
							toArteryFMAID = Integer.toString(FMAIDManager.getNewFMAID(newToTrunk));
							Vocabulary.insert(newToTrunk, toArteryFMAID);
							newStructures.add(new String[] {toArteryFMAID, newToTrunk,"",""});
							VascularCircuits.insertInArterialCircuit(toArteryFMAID);
						}
					}

					//now insert connectivity data
					if (slot.equals("continuous with distally") || slot.equals("branch") || slot.equals("regional part")){
						if (!fromArteryFMAID.equals(toArteryFMAID)){
							arterialConnections.add(fromArteryFMAID+"_"+toArteryFMAID);
						}
					}
					else if (slot.equals("continuous with proximally") || slot.equals("branch of")) {
						if (!fromArteryFMAID.equals(toArteryFMAID)){
							arterialConnections.add(toArteryFMAID+"_"+fromArteryFMAID);
						}
					}
					
				}
				
				if (!found){
					//no proximal or distal connections found
					
					if (arteryToAnalyse.contains("Trunk")|| arteryToAnalyse.contains("trunk")){
						//do not trunkalise, do nothing
						String newWithoutTrunkInt=null;
						String newWithoutTrunk = arteryToAnalyse.replace("Trunk of ", "");
						newWithoutTrunk = ConvertCase.toUpperCase(newWithoutTrunk);
						
						if (!Vocabulary.containsFMATerm(newWithoutTrunk)){
							newWithoutTrunkInt = Integer.toString(FMAIDManager.getNewFMAID(newWithoutTrunk));
							Vocabulary.insert(newWithoutTrunk, newWithoutTrunkInt);
							newStructures.add(new String[] {newWithoutTrunkInt, newWithoutTrunk,"",""});
							VascularCircuits.insertInArterialCircuit(newWithoutTrunkInt);
						}
						
					}
					else { //insert unconnected trunk in fma
						String newTrunk = "Trunk of "+ConvertCase.toLowerCase(arteryToAnalyse);
						if (!Vocabulary.containsFMATerm(newTrunk)) {
							arteryToAnalyse = Integer.toString(FMAIDManager.getNewFMAID(newTrunk));
							Vocabulary.insert(newTrunk, arteryToAnalyse);
							newStructures.add(new String[] {arteryToAnalyse, newTrunk,"",""});
							VascularCircuits.insertInArterialCircuit(arteryToAnalyse);
						}
					}
				}
				rs.close();
				st.close();
						
		}
			
			String newStructureArray[][] = new String[newStructures.size()][4];
			for (int i=0;i<newStructures.size();i++){
				String values[] = newStructures.get(i);
				newStructureArray[i][0] = values[0];
				newStructureArray[i][1] = values[1];
				newStructureArray[i][2] = "";
				newStructureArray[i][3] = "";
			}
			InsertSQLQuery.insert2DArrayInTable("structures",newStructureArray);
			
			
			String newConnectionsArray[][] = new String[arterialConnections.size()][2];
			int i=0;
			Iterator<String> it2 = arterialConnections.iterator();
			while (it2.hasNext()){
				String value = it2.next();
				newConnectionsArray[i][0] = value.substring(0,value.indexOf("_"));
				newConnectionsArray[i][1] = value.substring(value.indexOf("_")+1);
				i++;
			}
			
			
			InsertSQLQuery.insert2DArrayInTable("arterial_network",newConnectionsArray);	
			
			newStructures.clear();
			
		} catch (SQLException ex){
			System.err.println("SQL Error: " + ex.getMessage());
		}
	}	
	
	
	public static void getVenousConnectionsFromFMA(HashSet<String> setofVeins) {
		String fromVeinFMAID="";
		String toVeinFMAID="";
		ArrayList<String[]> newStructures = new ArrayList<String[]>();

		boolean found=false;
		try {		
			Iterator<String> it = setofVeins.iterator();
			while (it.hasNext()){
				found=false;
				String veinToAnalyse = it.next();
				VascularCircuits.insertInVenousCircuit(Vocabulary.getFMAID(veinToAnalyse));
				//1. is the veinToAnalyse a trunk?
				if (veinToAnalyse.contains("Trunk ")|| veinToAnalyse.contains(" trunk")){ 
					fromVeinFMAID = Vocabulary.getFMAID(veinToAnalyse);	
		
					//the trunk form does exist, but does the nontrunkalised arterial counterpart exist?
					String veinNonTrunked = veinToAnalyse.replace("Trunk of ", "");
					veinNonTrunked = ConvertCase.toUpperCase(veinNonTrunked);
					if (!Vocabulary.containsFMATerm(veinNonTrunked)) {
						String newFmaID =  Integer.toString(FMAIDManager.getNewFMAID(veinNonTrunked)); 
						Vocabulary.insert(veinNonTrunked, newFmaID);
						newStructures.add(new String[] {newFmaID, veinNonTrunked,"",""});
					}
					
					
				}
				
				else {
					String newFromTrunk = "Trunk of "+ConvertCase.toLowerCase(veinToAnalyse);

					if (Vocabulary.containsFMATerm(newFromTrunk)) {
						fromVeinFMAID = Vocabulary.getFMAID(newFromTrunk);
					}
					

					else {

						fromVeinFMAID = Integer.toString(FMAIDManager.getNewFMAID(newFromTrunk));
						Vocabulary.insert(newFromTrunk, fromVeinFMAID);
						newStructures.add(new String[] {fromVeinFMAID, newFromTrunk,"",""});
						VascularCircuits.insertInVenousCircuit(fromVeinFMAID);
					}
				}
				
				
				String statement = "select slot, short_value from fma where frame = \""+veinToAnalyse+"\" and (slot='continuous with proximally' or slot='continuous with distally' or slot='tributary' or slot='tributary of' or slot='regional part') and (short_value like '%vein%' or short_value like '%vena%'or short_value like '%venous%') and short_value not like '%pericardium%'";
				Statement st = DBConnections.getFMAConnection().createStatement();
				ResultSet rs = st.executeQuery(statement);
				while (rs.next()) {
					found=true;
					toVeinFMAID="";
					String slot = rs.getString("slot");
					String result = rs.getString("short_value");
					
					//however we only add trunks - is the branch trunkalisable?
					if (result.contains("Trunk ")|| result.contains(" trunk")|| result.contains(" plexus")){
						toVeinFMAID = Vocabulary.getFMAID(result);
					}
					else {
						//can be trunkalised, search in hashmap - does the trunk exist?
						String newToTrunk = "Trunk of "+ConvertCase.toLowerCase(result);
						if (Vocabulary.containsFMATerm(newToTrunk)) {
							toVeinFMAID = Vocabulary.getFMAID(newToTrunk);
						}
						else {
							toVeinFMAID = Integer.toString(FMAIDManager.getNewFMAID(newToTrunk));
							Vocabulary.insert(newToTrunk, toVeinFMAID);
							newStructures.add(new String[] {toVeinFMAID, newToTrunk,"",""});
							VascularCircuits.insertInVenousCircuit(toVeinFMAID);
						}
					}
					
					//now insert connectivity data
					if (slot.equals("continuous with distally") || slot.equals("tributary") || slot.equals("regional part")){
						if (!fromVeinFMAID.equals(toVeinFMAID)){
							venousConnections.add(toVeinFMAID+"_"+fromVeinFMAID);
						}
					}
					else if (slot.equals("continuous with proximally") || slot.equals("tributary of ")) {
						if (!fromVeinFMAID.equals(toVeinFMAID)){
							venousConnections.add(fromVeinFMAID+"_"+toVeinFMAID);
						}
					}
				}
				
				if (!found){
					if (veinToAnalyse.contains("Trunk")|| veinToAnalyse.contains("trunk")){
						//do not trunkalise, do nothing
						
						String newWithoutTrunk = veinToAnalyse.replace("Trunk of ", "");
						String newWithoutTrunkInt=null;
						
						newWithoutTrunk = ConvertCase.toUpperCase(newWithoutTrunk);
						if (!Vocabulary.containsFMATerm(newWithoutTrunk)){
							newWithoutTrunkInt = Integer.toString(FMAIDManager.getNewFMAID(newWithoutTrunk));
							Vocabulary.insert(newWithoutTrunk, newWithoutTrunkInt);
							newStructures.add(new String[] {newWithoutTrunkInt, newWithoutTrunk,"",""});
							VascularCircuits.insertInVenousCircuit(newWithoutTrunkInt);
						}
					}
					
					else { //insert unconnected trunk in fma
						String newTrunk = "Trunk of "+ConvertCase.toLowerCase(veinToAnalyse);
						if (!Vocabulary.containsFMATerm(newTrunk)) {
							veinToAnalyse = Integer.toString(FMAIDManager.getNewFMAID(newTrunk));
							Vocabulary.insert(newTrunk, veinToAnalyse);
							newStructures.add(new String[] {veinToAnalyse, newTrunk,"",""});
							VascularCircuits.insertInVenousCircuit(veinToAnalyse);
						}
					}
				}
				rs.close();
				st.close();
			}
			
			String newStructureArray[][] = new String[newStructures.size()][4];
			for (int i=0;i<newStructures.size();i++){
				String values[] = newStructures.get(i);
				newStructureArray[i][0] = values[0];
				newStructureArray[i][1] = values[1];
				newStructureArray[i][2] = "";
				newStructureArray[i][3] = "";
			}
			InsertSQLQuery.insert2DArrayInTable("structures",newStructureArray);
			
			
			String newConnectionsArray[][] = new String[venousConnections.size()][2];
			int i=0;
			Iterator<String> it2 = venousConnections.iterator();
			while (it2.hasNext()){
				String value = it2.next();
				newConnectionsArray[i][0] = value.substring(0,value.indexOf("_"));
				newConnectionsArray[i][1] = value.substring(value.indexOf("_")+1);
				i++;
			}
			
			InsertSQLQuery.insert2DArrayInTable("venous_network",newConnectionsArray);	
			newStructures.clear();
		} 
		
		catch (SQLException ex){
			System.err.println("SQL Error: " + ex.getMessage());
			System.exit(1);
		}
	}	
	
	
	public static void connectMiscVessels(){
		try{
			Statement tableStmt = DBConnections.getApinatomyConnection().createStatement();
		
			tableStmt.executeUpdate(" insert into microcirculations values (50767,62001,\"VEN\",\"N\");"); //Superior saggital sinus drains diencephalon

			//remove Trunk of systemic arterial tree
			String trunkSysArtTree = Vocabulary.getFMAID("Trunk of systemic arterial tree");
			String trunkSysArtTreeFemale = Vocabulary.getFMAID("Trunk of systemic arterial tree of female human body");
			String trunkSysArtTreeMale = Vocabulary.getFMAID("Trunk of systemic arterial tree of male human body");
			String trunkAorta = Vocabulary.getFMAID("Trunk of aorta");
			String trunkArchAorta = Vocabulary.getFMAID("Trunk of arch of aorta");
			String trunkAbdAorta = Vocabulary.getFMAID("Trunk of abdominal aorta");
			String trunkAscAorta = Vocabulary.getFMAID("Trunk of ascending aorta");

			tableStmt.executeUpdate("delete from arterial_network where vessel_from = "+trunkAbdAorta+";");
			tableStmt.executeUpdate("delete from arterial_network where vessel_from = "+trunkAscAorta+";");
			
			tableStmt.executeUpdate("update arterial_network set vessel_from="+trunkAorta+" where vessel_from="+trunkSysArtTree+";");
			tableStmt.executeUpdate("update arterial_network set vessel_from="+trunkAorta+" where vessel_from="+trunkArchAorta+";");
			tableStmt.executeUpdate("update arterial_network set vessel_from="+trunkAorta+" where vessel_from="+trunkSysArtTreeFemale+";");
			tableStmt.executeUpdate("update arterial_network set vessel_from="+trunkAorta+" where vessel_from="+trunkSysArtTreeMale+";");

			tableStmt.executeUpdate("insert into arterial_network values("+Vocabulary.getFMAID("Left ventricle")+","+ Vocabulary.getFMAID("Trunk of aorta")+");");
			tableStmt.executeUpdate("insert into arterial_network values("+Vocabulary.getFMAID("Right ventricle")+","+ Vocabulary.getFMAID("Pulmonary trunk")+");");
			tableStmt.executeUpdate("insert into arterial_network values("+Vocabulary.getFMAID("Pulmonary trunk")+","+ Vocabulary.getFMAID("Trunk of right pulmonary artery")+");");
			tableStmt.executeUpdate("insert into arterial_network values("+Vocabulary.getFMAID("Pulmonary trunk")+","+ Vocabulary.getFMAID("Trunk of left pulmonary artery")+");");
			
			tableStmt.executeUpdate("insert into venous_network values("+Vocabulary.getFMAID("Trunk of superior vena cava")+","+ Vocabulary.getFMAID("Right atrium")+");");
			tableStmt.executeUpdate("insert into venous_network values("+Vocabulary.getFMAID("Trunk of inferior vena cava")+","+ Vocabulary.getFMAID("Right atrium")+");");
			tableStmt.executeUpdate("insert into venous_network values("+Vocabulary.getFMAID("Trunk of coronary sinus")+","+ Vocabulary.getFMAID("Right atrium")+");");
			tableStmt.executeUpdate("insert into venous_network values("+Vocabulary.getFMAID("Trunk of portal vein")+","+ Vocabulary.getFMAID("Liver")+");");
			tableStmt.executeUpdate("insert into venous_network values("+Vocabulary.getFMAID("Trunk of segmental tributary of splenic vein")+","+ Vocabulary.getFMAID("Trunk of splenic vein")+");");
			tableStmt.executeUpdate("insert into venous_network values("+Vocabulary.getFMAID("Trunk of left common pulmonary vein")+","+ Vocabulary.getFMAID("Left atrium")+");");
			tableStmt.executeUpdate("insert into venous_network values("+Vocabulary.getFMAID("Trunk of right common pulmonary vein")+","+ Vocabulary.getFMAID("Left atrium")+");");
			tableStmt.executeUpdate("insert into venous_network values("+Vocabulary.getFMAID("Trunk of azygos vein")+","+ Vocabulary.getFMAID("Trunk of superior vena cava")+");");
	
			//now modify subclavian arteries
			String a = Vocabulary.getFMAID("Trunk of left subclavian artery proper");
			String b = Vocabulary.getFMAID("Trunk of first part of left subclavian artery proper");
			String c = Vocabulary.getFMAID("Trunk of second part of left subclavian artery proper");
			String d = Vocabulary.getFMAID("Trunk of third part of left subclavian artery proper");
			String e = Vocabulary.getFMAID("Trunk of right subclavian artery proper");
			String f = Vocabulary.getFMAID("Trunk of first part of right subclavian artery proper");
			String g = Vocabulary.getFMAID("Trunk of second part of right subclavian artery proper");
			String h = Vocabulary.getFMAID("Trunk of third part of right subclavian artery proper");
			String i = Vocabulary.getFMAID("Trunk of first part of subclavian artery proper");
			String j = Vocabulary.getFMAID("Trunk of second part of subclavian artery proper");
			String k = Vocabulary.getFMAID("Trunk of third part of subclavian artery proper");
			tableStmt.executeUpdate("delete from arterial_network where vessel_from="+a+" or vessel_to="+a+";");
			tableStmt.executeUpdate("delete from arterial_network where vessel_from="+b+" or vessel_to="+b+";");
			tableStmt.executeUpdate("delete from arterial_network where vessel_from="+c+" or vessel_to="+c+";");
			tableStmt.executeUpdate("delete from arterial_network where vessel_from="+d+" or vessel_to="+d+";");
			tableStmt.executeUpdate("delete from arterial_network where vessel_from="+e+" or vessel_to="+e+";");
			tableStmt.executeUpdate("delete from arterial_network where vessel_from="+f+" or vessel_to="+f+";");
			tableStmt.executeUpdate("delete from arterial_network where vessel_from="+g+" or vessel_to="+g+";");
			tableStmt.executeUpdate("delete from arterial_network where vessel_from="+h+" or vessel_to="+h+";");
			tableStmt.executeUpdate("delete from arterial_network where vessel_from="+i+" or vessel_to="+i+";");
			tableStmt.executeUpdate("delete from arterial_network where vessel_from="+j+" or vessel_to="+j+";");
			tableStmt.executeUpdate("delete from arterial_network where vessel_from="+k+" or vessel_to="+k+";");		
			tableStmt.executeUpdate("delete from structures where structure_name like \"Trunk of %subclavian%proper\" and fma_id >500000;");
	
			
			tableStmt.executeUpdate("delete from arterial_network where vessel_from=269055 and vessel_to="+Vocabulary.getFMAID("Trunk of atrioventricular node branch of right coronary artery")+";");
			tableStmt.executeUpdate("delete from arterial_network where vessel_from=269055 and vessel_to="+Vocabulary.getFMAID("Trunk of atrioventricular node branch of right coronary artery")+";");
			
			//67619="Organ region"
			tableStmt.executeUpdate("delete from microcirculations where organ=67619");
			//lung
			tableStmt.executeUpdate("delete from regional_parts where main_structure_id=68216 and regional_part_id=68567;");
			//pre-hep port vein -> port-vein
			tableStmt.executeUpdate("update venous_network set vessel_to=14329 where vessel_from=78210 and vessel_to=71909;"); 
			String trunkAntPCDArt = Vocabulary.getFMAID("Trunk of anterior superior pancreaticoduodenal artery");
			String trunkPostPCDArt = Vocabulary.getFMAID("Trunk of posterior superior pancreaticoduodenal artery"); 
			String trunkSupPCDArt = Vocabulary.getFMAID("Trunk of superior pancreaticoduodenal artery");
			tableStmt.executeUpdate("insert into microcirculations values ("+trunkAntPCDArt+ ",7200,\"ART\",\"N\");");
			tableStmt.executeUpdate("insert into microcirculations values ("+trunkPostPCDArt+",7200,\"ART\",\"N\");");
			tableStmt.executeUpdate("delete from microcirculations where vessel="+trunkSupPCDArt+" and organ=7200;");
			//solve celiac trunk issue
			String trCelArt = Vocabulary.getFMAID("Trunk of celiac artery");
			tableStmt.executeUpdate("update arterial_network set vessel_from = 14812 where vessel_from = "+trCelArt+";");
			tableStmt.executeUpdate("update arterial_network set vessel_to = 14812 where vessel_to = "+trCelArt+";");
			tableStmt.executeUpdate("delete from structures where structure_name like \"Trunk of %groove%\" and fma_id>200000;");
			//splenic pulp issue
			String splenSinusoid = Vocabulary.getFMAID("Trunk of splenic venous sinusoid");
			String redPulp = Vocabulary.getFMAID("Trunk of venous sinus of red pulp of spleen"); 
			tableStmt.executeUpdate("delete from microcirculations where vessel = "+splenSinusoid+" or vessel="+redPulp+";");
			//delete trunk of artery connections
			tableStmt.executeUpdate("delete from microcirculations where vessel ="+Vocabulary.getFMAID("Trunk of artery")+";");
			//pulmonary connections: connect to trunk of left pulmonary atery
			tableStmt.executeUpdate("insert into arterial_network values(8614,"+Vocabulary.getFMAID("Trunk of basal portion of left pulmonary artery")+");");
			tableStmt.executeUpdate("delete from arterial_network where vessel_from="+Vocabulary.getFMAID("Trunk of pulmonary arterial tree")+";");
			//bonchial->pulm misconnection
			tableStmt.executeUpdate("delete from arterial_network where vessel_from=31939 and vessel_to=8616;");
			tableStmt.executeUpdate("insert into arterial_network values (8613, "+Vocabulary.getFMAID("Trunk of basal portion of right pulmonary artery")+");");
			tableStmt.executeUpdate("insert into arterial_network values (8613, "+Vocabulary.getFMAID("Trunk of interlobar portion of right pulmonary artery")+");");
			//connect aorta to Trunk of common carotid artery
			tableStmt.executeUpdate("insert into arterial_network values("+Vocabulary.getFMAID("Trunk of aorta")+",69323);");
			//connect mesencephalic artery to basilar artery
			tableStmt.executeUpdate("insert into arterial_network values(76647,+"+Vocabulary.getFMAID("Trunk of mesencephalic artery")+");");
			tableStmt.executeUpdate("delete from venous_network where vessel_to="+Vocabulary.getFMAID("Trunk of anterior segmental vein"));
			//remove multiple connections to Trunk of anterior choroidal artery(76253) - should only connect to ICA
			tableStmt.executeUpdate("delete from arterial_network where vessel_to=76253 and vessel_from!=69315;");
	
			
			String[][] coronaryZones = new String[][] {
					{"Trunk of anterior interventricular branch of left coronary artery","Trunk of basal zone of anterior interventricular branch of left coronary artery"},
					{"Trunk of anterior interventricular branch of left coronary artery","Trunk of zone 1 of anterior interventricular branch of left coronary artery"},
					{"Trunk of anterior interventricular branch of left coronary artery","Trunk of zone 14 of anterior interventricular branch of left coronary artery"},
					{"Trunk of anterior interventricular branch of left coronary artery","Trunk of zone 14a of anterior interventricular branch of left coronary artery"},
					{"Trunk of anterior interventricular branch of left coronary artery","Trunk of zone 15 of anterior interventricular branch of left coronary artery"},
					{"Trunk of anterior interventricular branch of left coronary artery","Trunk of zone 13 of anterior interventricular branch of left coronary artery"},
					{"Trunk of anterior interventricular branch of left coronary artery","Trunk of zone 16a of anterior interventricular branch of left coronary artery"},
					{"Trunk of anterior interventricular branch of left coronary artery","Trunk of zone 17 of anterior interventricular branch of left coronary artery"},
					{"Trunk of anterior interventricular branch of left coronary artery","Trunk of zone 8 of anterior interventricular branch of left coronary artery"},
					{"Trunk of anterior interventricular branch of left coronary artery","Trunk of zone 9 of anterior interventricular branch of left coronary artery"},
					{"Trunk of anterior interventricular branch of left coronary artery","Trunk of zone 15a of anterior interventricular branch of left coronary artery"},
					{"Trunk of anterior interventricular branch of left coronary artery","Trunk of zone 3 of anterior interventricular branch of left coronary artery"},
					{"Trunk of anterior interventricular branch of left coronary artery","Trunk of zone 7 of anterior interventricular branch of left coronary artery"},
					{"Trunk of anterior interventricular branch of left coronary artery","Trunk of zone 13a of anterior interventricular branch of left coronary artery"},
					{"Trunk of anterior interventricular branch of left coronary artery","Trunk of zone 2 of anterior interventricular branch of left coronary artery"},
	
					{"Trunk of circumflex branch of left coronary artery","Trunk of zone 10 of circumflex branch of left coronary artery"},
					{"Trunk of circumflex branch of left coronary artery","Trunk of zone 11 of circumflex branch of left coronary artery"},
					{"Trunk of circumflex branch of left coronary artery","Trunk of zone 15 of circumflex branch of left coronary artery"},
					{"Trunk of circumflex branch of left coronary artery","Trunk of zone 4 of circumflex branch of left coronary artery"},
					{"Trunk of circumflex branch of left coronary artery","Trunk of zone 5 of circumflex branch of left coronary artery"},
	
					{"Trunk of left marginal artery","Trunk of zone 12 of left marginal artery"},
					{"Trunk of left marginal artery","Trunk of zone 17 of left marginal artery"},
					{"Trunk of left marginal artery","Trunk of zone 18 of left marginal artery"},
					{"Trunk of left marginal artery","Trunk of zone 19 of left marginal artery"},
					{"Trunk of left marginal artery","Trunk of zone 16 of left marginal artery"},
					{"Trunk of left marginal artery","Trunk of zone 6 of left marginal artery"},
					{"Trunk of left marginal artery","Trunk of zone 20 of left marginal artery"},
	
					{"Trunk of left diagonal artery","Trunk of zone 13 of left diagonal artery"},
					{"Trunk of left diagonal artery","Trunk of zone 13a of left diagonal artery"},
					{"Trunk of left diagonal artery","Trunk of zone 7 of left diagonal artery"}
				};
			
			
			for (int r=0;r<coronaryZones.length;r++){
				String vessel1ID = Vocabulary.getFMAID(coronaryZones[r][0]);
				String vessel2ID = Vocabulary.getFMAID(coronaryZones[r][1]);
				if (vessel1ID == null){ 
					vessel1ID = Integer.toString(FMAIDManager.getNewFMAID(coronaryZones[r][0]));
					Vocabulary.insert(coronaryZones[r][0], vessel1ID);
					tableStmt.executeUpdate("insert into structures values ("+ vessel1ID+",\""+coronaryZones[r][0]+"\",\"\",\"\");");
				}
				if (vessel2ID == null){ 
					vessel2ID = Integer.toString(FMAIDManager.getNewFMAID(coronaryZones[r][1]));
					Vocabulary.insert(coronaryZones[r][1], vessel2ID);
					tableStmt.executeUpdate("insert into structures values ("+ vessel2ID+",\""+coronaryZones[r][1]+"\",\"\",\"\");");
				}
				tableStmt.executeUpdate("insert into arterial_network values ("+vessel1ID+ ", "+vessel2ID+");");
	
			}
			
			tableStmt.close();
			
			}
			catch (Exception e){
				e.printStackTrace();
			}
	}
	

	public static void getSQLBrainSinusConnections(String fmaTerm){
		
		String proxVessel ="";
    	try {
			Statement st = DBConnections.getFMAConnection().createStatement();
			String statement="select short_value from fma where slot=\"tributary\" and frame=\""+fmaTerm+"\";";
			ResultSet rs = st.executeQuery(statement);
			while (rs.next()) {
			  proxVessel = rs.getString("short_value");
	        	String proxVessel2 = "Trunk of "+ ConvertCase.toLowerCase(proxVessel);

			  if (!venousConnections.contains(proxVessel+"_"+fmaTerm)&&
					  !venousConnections.contains(proxVessel2+"_"+fmaTerm)){

			  if (!proxVessel.contains("vein")){
				  fromSinus.add(Vocabulary.getFMAID(proxVessel));
			  }
			  else {
		        	fromSinus.add(Vocabulary.getFMAID(proxVessel2));
			  }

			  if (!fmaTerm.contains("vein")){			  
				  toSinus.add(Vocabulary.getFMAID(fmaTerm));
			  }
			  else {
		        	String fmaTerm2 = "Trunk of "+ ConvertCase.toLowerCase(fmaTerm);
		        	toSinus.add(Vocabulary.getFMAID(fmaTerm2));
			  }
			  }
			  getSQLBrainSinusConnections(proxVessel);
			}
			rs.close();
			st.close();
		} catch (SQLException ex){
			System.err.println("Error: " + ex.getMessage());
			ex.printStackTrace();
			System.exit(1);
		}
	}

	public static void getBrainSinusConnections(){
		fromSinus=new ArrayList<String>();
		toSinus=new ArrayList<String>();
	    
	    String mainSinusList[]=new String[] {"Internal jugular vein", "Right internal jugular vein","Left internal jugular vein"};  
		for (int i=0;i<mainSinusList.length;i++){
			getSQLBrainSinusConnections(mainSinusList[i]);
			
			String sinusArray[][] = new String[fromSinus.size()][2];
			for (int j=0;j<fromSinus.size();j++){
				sinusArray[j][0]=fromSinus.get(j);
				sinusArray[j][1]=toSinus.get(j);
			}
			InsertSQLQuery.insert2DArrayInTable("venous_network",sinusArray);	
			
			fromSinus.clear();
			toSinus.clear();
			//now that we've added sinuses, lets populate the database with other venous connections that the FMA does not have
			
		

			/**
			1. connect the following: 
			Anterior internal vertebral venous plexus -> Basilar venous plexus
			Anterior intercavernous sinus->Right cavernous sinus
			Posterior intercavernous sinus->Left cavernous sinus
			Anterior intercavernous sinus->Left cavernous sinus
			Posterior intercavernous sinus->Right cavernous sinus
			Anterior intercavernous sinus<-Right cavernous sinus
			Posterior intercavernous sinus<-Left cavernous sinus
			Anterior intercavernous sinus<-Left cavernous sinus
			Posterior intercavernous sinus<-Right cavernous sinus
			Superior saggital sinus-> Left transverse sinus
			Posterior internal vertebral venous plexus->Occipital sinus
			Occipital sinus -> Right sigmoid sinus
			Occipital sinus -> Left sigmoid sinus
			Occipital sinus -> Right transverse sinus

			 */
			
		
			String additionalConnection[][]=new String[][] { {"50783","4869"},{"50774","51402"},{"50775","51403"},
					{"50774","51403"},{"50775","51402"},{"51402","50774"},{"51403","50775"},
					{"51403","50774"},{"51402","50775"},{"50767","50765"},{"4870","50781"},
					{"50781","51293"},{"50781","51294"},{ "50781","50764"}};
			InsertSQLQuery.insert2DArrayInTable("venous_network",additionalConnection);	
			
			
			String moreConnections[][]=new String[17][2];
			moreConnections[0][0] = Vocabulary.getFMAID("Trunk of right superior anastomotic vein");
			moreConnections[0][1] = Vocabulary.getFMAID("Superior sagittal sinus");
			moreConnections[1][0] = Vocabulary.getFMAID("Trunk of left superior anastomotic vein");
			moreConnections[1][1] = Vocabulary.getFMAID("Superior sagittal sinus");
			moreConnections[2][0] = Vocabulary.getFMAID("Trunk of right superior anastomotic vein");
			moreConnections[2][1] = "50767";
			moreConnections[3][0] = Vocabulary.getFMAID("Trunk of left superior anastomotic vein");
			moreConnections[3][1] = "50767";
			moreConnections[5][0] = Vocabulary.getFMAID("Trunk of deep cerebral vein");
			moreConnections[5][1] = Vocabulary.getFMAID("Trunk of basal vein");
			moreConnections[6][0] = Vocabulary.getFMAID("Trunk of inferior ventricular vein");
			moreConnections[6][1] = Vocabulary.getFMAID("Trunk of basal vein");
			moreConnections[7][0] = Vocabulary.getFMAID("Trunk of inferior thalamostriate vein");
			moreConnections[7][1] = Vocabulary.getFMAID("Trunk of basal vein");
			moreConnections[8][0] = Vocabulary.getFMAID("Trunk of inferior choroid vein");
			moreConnections[8][1] = Vocabulary.getFMAID("Trunk of basal vein");
			moreConnections[9][0] = Vocabulary.getFMAID("Trunk of tributary of basal vein");
			moreConnections[9][1] = Vocabulary.getFMAID("Trunk of basal vein");
			moreConnections[10][0] = Vocabulary.getFMAID("Trunk of deep middle cerebral vein");
			moreConnections[10][1] = Vocabulary.getFMAID("Trunk of basal vein");
			moreConnections[11][0] = Vocabulary.getFMAID("Trunk of vein of olfactory gyrus");
			moreConnections[11][1] = Vocabulary.getFMAID("Trunk of basal vein");
			moreConnections[12][0] = Vocabulary.getFMAID("Trunk of penduncular tributary of basal vein");
			moreConnections[12][1] = Vocabulary.getFMAID("Trunk of basal vein");
			moreConnections[13][0] = Vocabulary.getFMAID("Trunk of anterior cerebral vein");
			moreConnections[13][1] = Vocabulary.getFMAID("Trunk of basal vein");
			moreConnections[14][0] = Vocabulary.getFMAID("Trunk of right basal vein");
			moreConnections[14][1] = Vocabulary.getFMAID("Trunk of right internal cerebral vein");
			moreConnections[15][0] = Vocabulary.getFMAID("Trunk of right internal cerebral vein");
			moreConnections[15][1] = Vocabulary.getFMAID("Trunk of deep cerebral vein");
			moreConnections[16][0] = Vocabulary.getFMAID("Trunk of left basal vein");
			moreConnections[16][1] = Vocabulary.getFMAID("Trunk of left internal cerebral vein");
			moreConnections[4][0] = Vocabulary.getFMAID("Trunk of left internal cerebral vein");
			moreConnections[4][1] = Vocabulary.getFMAID("Trunk of deep cerebral vein");
			InsertSQLQuery.insert2DArrayInTable("venous_network",moreConnections);	

		}

	}
	
	
}
