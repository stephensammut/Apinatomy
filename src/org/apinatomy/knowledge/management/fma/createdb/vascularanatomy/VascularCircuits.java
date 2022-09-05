package org.apinatomy.knowledge.management.fma.createdb.vascularanatomy;

import java.sql.Statement;
import java.util.HashSet;
import java.util.Iterator;

import org.apinatomy.knowledge.management.fma.createdb.sql.DBConnections;
import org.apinatomy.knowledge.management.fma.createdb.sql.InsertSQLQuery;
import org.apinatomy.knowledge.management.fma.createdb.utilities.Vocabulary;



public class VascularCircuits {

	
	private static HashSet<String> arterialCircuitSet = new HashSet<String>();
	private static HashSet<String> venousCircuitSet = new HashSet<String>();
	
	
	public static void insertInArterialCircuit(String id){
		arterialCircuitSet.add(id);
	}
	
	public static void insertInVenousCircuit(String id){
		venousCircuitSet.add(id);
	}
	
	public static void populateVascularCircuitsTable(){
		try{
		String[] portal_circulation= {"Trunk of pancreaticoduodenal vein",
				"Trunk of pancreatic tributary of splenic vein",
				"Trunk of splenic vein",
				"Trunk of segmental tributary of splenic vein",
				"Trunk of splenic venous sinusoid",
				"Trunk of venous sinus of red pulp of spleen",
				"Trunk of left gastric vein",
				"Trunk of right gastric vein",
				"Trunk of right gastroepiploic vein",
				"Trunk of short gastric vein",
				"Trunk of left gastroepiploic vein",
				"Trunk of right gastroepiploic vein",
				"Trunk of left gastroepiploic vein",
				"Trunk of right gastroepiploic vein",
				"Trunk of left gastroepiploic vein",
				"Trunk of pancreaticoduodenal vein",
				"Trunk of pancreatic tributary of splenic vein",
				"Trunk of pancreatic tributary of splenic vein",
				"Trunk of short gastric vein",
				"Trunk of superior mesenteric vein",
				"Trunk of inferior mesenteric vein",
				"Trunk of superior rectal vein",
				"Trunk of portal vein",
				"Trunk of pre-hepatic portal vein",
				"Trunk of jejunal vein",
				"Trunk of ileal vein",
				"Trunk of ileal tributary of ileocolic vein",
				"Trunk of ileocolic vein",
				"Trunk of middle colic vein",
				"Trunk of right colic vein",
				"Trunk of left colic vein",
				"Trunk of sigmoid vein",
				"Trunk of cecal tributary of ileocolic vein"
				};
		
		String[] pulmonary_arterial_circulation={ "Pulmonary trunk",  "Trunk of right pulmonary artery", 
		"Trunk of right upper lobar artery", "Trunk of apicoposterior division of right upper lobar artery", 
		"Trunk of right apical segmental artery", "Trunk of apical part of right apical segmental artery", 
		"Trunk of anterior part of right apical segmental artery", "Trunk of right posterior segmental artery", 
		"Trunk of apical part of right posterior segmental artery", "Trunk of posterior part of right posterior segmental artery", 
		"Trunk of right anterior segmental artery", "Trunk of posterior branch of right anterior segmental artery", 
		"Trunk of superior division of posterior branch of right anterior segmental artery", "Trunk of inferior division of posterior branch of right anterior segmental artery", 
		"Trunk of anterior branch of right anterior segmental artery", "Trunk of inferomedial branch of right pulmonary artery", 
		"Trunk of middle lobar artery", "Trunk of lateral segmental artery", 
		"Trunk of posterior branch of lateral segmental artery", "Trunk of anterior branch of lateral segmental artery", 
		"Trunk of medial segmental artery", "Trunk of superior branch of medial segmental artery", 
		"Trunk of inferior branch of medial segmental artery", "Trunk of right lower lobar artery", 
		"Trunk of right subsuperior segmental artery", "Trunk of right medial basal segmental artery", 
		"Trunk of anterior branch of right medial basal segmental artery", "Trunk of medial branch of right medial basal segmental artery", 
		"Trunk of right anterior basal segmental artery", "Trunk of lateral branch of right anterior basal segmental artery", 
		"Trunk of basal branch of right anterior basal segmental artery", "Trunk of right lateral basal segmental artery", 
		"Trunk of lateral branch of right lateral basal segmental artery", "Trunk of basal branch of right lateral basal segmental artery", 
		"Trunk of right posterior basal segmental artery", "Trunk of accessory subsuperior branch of right posterior basal segmental artery", 
		"Trunk of laterobasal branch of right posterior basal segmental artery", "Trunk of mediobasal branch of right posterior basal segmental artery", 
		"Trunk of right superior segmental artery", "Trunk of medial branch of right superior segmental artery", 
		"Trunk of superior branch of right superior segmental artery", "Trunk of lateral branch of right superior segmental artery", 
		"Trunk of left pulmonary artery", "Trunk of left upper lobar artery", 
		"Trunk of left apical segmental artery", "Trunk of apical part of left apical segmental artery", 
		"Trunk of anterior part of left apical segmental artery", "Trunk of left posterior segmental artery", 
		"Trunk of apical part of left posterior segmental artery","Trunk of posterior part of left posterior segmental artery", 
		"Trunk of left anterior segmental artery", "Trunk of posterior branch of left anterior segmental artery", 
		"Trunk of superior division of posterior branch of left anterior segmental artery", "Trunk of inferior division of posterior branch of left anterior segmental artery", 
		"Trunk of anterior branch of left anterior segmental artery", "Trunk of left lower lobar artery", 
		"Trunk of left subsuperior segmental artery", "Trunk of left medial basal segmental artery", 
		"Trunk of latero-anterior branch of left medial basal segmental artery", "Trunk of medio-anterior branch of left medial basal segmental artery", 
		"Trunk of left anterior basal segmental artery", "Trunk of lateral branch of left anterior basal segmental artery", 
		"Trunk of basal branch of left anterior basal segmental artery", "Trunk of left lateral basal segmental artery", 
		"Trunk of accessory subsuperior branch of left lateral basal segmental artery", 
		"Trunk of basal branch of left lateral basal segmental artery", "Trunk of left posterior basal segmental artery", 
		"Trunk of accessory subsuperior branch of left posterior basal segmental artery", "Trunk of laterobasal branch of left posterior basal segmental artery", 
		"Trunk of mediobasal branch of left posterior basal segmental artery", "Trunk of left superior segmental artery", 
		"Trunk of medial branch of left superior segmental artery", "Trunk of superior branch of left superior segmental artery", 
		"Trunk of lateral branch of left superior segmental artery","Trunk of lingular artery", "Trunk of superior lingular artery", 
		"Trunk of inferior lingular artery", "Trunk of basal portion of left pulmonary artery", "Trunk of left basal segmental artery",
		"Trunk of posterolateral basal branch of left pulmonary artery", "Trunk of antero-medial basal segmental artery" ,
				"Trunk of anteromedial basal branch of left pulmonary artery", "Trunk of right posterolateral basal pulmonary artery",
				"Trunk of right anteromedial basal pulmonary artery", "Trunk of basal portion of right pulmonary artery",
				"Trunk of right basal segmental artery", "8"};

		
		String[] pulmonary_venous_circulation ={
				"Trunk of left common pulmonary vein",
				"Trunk of left inferior pulmonary vein", "Trunk of left superior segmental vein", 
				"Trunk of medial part of left superior segmental vein", "Trunk of lateral part of left superior segmental vein", 
				"Trunk of left common basal vein", "Trunk of left superior basal vein", 
				"Trunk of left anterior basal segmental vein", "Trunk of intrasegmental tributary of left anterior basal segmental vein", 
				"Trunk of lateral part of left anterior basal segmental vein", "Trunk of basal part of left anterior basal segmental vein", 
				"Trunk of left medial basal segmental vein", "Trunk of intrasegmental tributary of left medial basal segmental vein", 
				"Trunk of intersegmental tributary of left medial basal segmental vein", "Trunk of left inferior basal vein", 
				"Trunk of left lateral basal segmental vein", "Trunk of intrasegmental tributary of left lateral basal segmental vein", 
				"Trunk of left posterior basal segmental vein", "Trunk of intrasegmental tributary of left posterior basal segmental vein", 
				"Trunk of mediobasal part of posterior basal segmental vein", "Trunk of left superior pulmonary vein", 
				"Trunk of left upper lobar vein", "Trunk of apicoposterior segmental vein", 
				"Trunk of left apical segmental vein", "Trunk of intrasegmental tributary of left apical segmental vein", 
				"Trunk of apical part of left apical segmental vein", "Trunk of left posterior segmental vein", 
				"Trunk of intrasegmental tributary of left posterior segmental vein", "Trunk of interlobar division of right posterior segmental vein", 
				"Trunk of left anterior segmental vein", "Trunk of intrasegmental tributary of left anterior segmental vein", 
				"Trunk of inferior part of anterior segmental vein", "Trunk of lingular vein", 
				"Trunk of superior lingular vein", "Trunk of intrasegmental tributary of superior lingular vein", 
				"Trunk of subsegmental part of superior lingular vein", "Trunk of inferior lingular vein", 
				"Trunk of intrasegmental tributary of inferior lingular vein", "Trunk of right inferior pulmonary vein", 
				"Trunk of right superior segmental vein", "Trunk of intrasegmental tributary of right superior segmental vein", 
				"Trunk of medial part of superior segmental vein", "Trunk of superior part of right superior segmental vein", 
				"Trunk of lateral part of right superior segmental vein", "Trunk of right common basal vein", 
				"Trunk of right superior basal vein", "Trunk of right anterior basal segmental vein", 
				"Trunk of intrasegmental tributary of right anterior basal segmental vein", "Trunk of lateral part of right anterior basal segmental vein", 
				"Trunk of basal part of right anterior basal segmental vein", "Trunk of right medial basal segmental vein", 
				"Trunk of intrasegmental tributary of right medial basal segmental vein", "Trunk of intersegmental tributary of right medial basal segmental vein", 
				"Trunk of right inferior basal vein", "Trunk of right lateral basal segmental vein", 
				"Trunk of intrasegmental tributary of right lateral basal segmental vein", "Trunk of lateral part of lateral basal segmental vein", 
				"Trunk of right posterior basal segmental vein", "Trunk of intrasegmental tributary of right posterior basal segmental vein", 
				"Trunk of laterobasal part of posterior basal segmental vein", "Trunk of right superior pulmonary vein", 
				"Trunk of right upper lobar vein", "Trunk of right apical segmental vein", 
				"Trunk of intrasegmental tributary of right apical segmental vein", "Trunk of apical part of right apical segmental vein", 
				"Trunk of right posterior segmental vein", "Trunk of intrasegmental tributary of right posterior segmental vein", 
				"Trunk of central division of right posterior segmental vein", "Trunk of right anterior segmental vein", 
				"Trunk of intrasegmental tributary of right anterior segmental vein", "Trunk of superior part of anterior segmental vein", 
				"Trunk of middle lobar vein", "Trunk of lateral segmental vein", 
				"Trunk of intrasegmental tributary of right lateral segmental vein", "Trunk of subsegmental part of lateral segmental vein", 
				"Trunk of medial segmental vein", "Trunk of intrasegmental tributary of right medial segmental vein", 
				"Trunk of subsegmental part of medial segmental vein"};
		
		Statement tableStmt = DBConnections.getApinatomyConnection().createStatement();

		for (int i=0;i<portal_circulation.length;i++){
			tableStmt.executeUpdate("INSERT INTO VASCULAR_CIRCUITS VALUES ("+Vocabulary.getFMAID(portal_circulation[i])+", \"GI_PORTAL\");");
			if (venousCircuitSet.contains(Vocabulary.getFMAID(portal_circulation[i]))) venousCircuitSet.remove(Vocabulary.getFMAID(portal_circulation[i]));
		}
		
		for (int i=0;i<pulmonary_arterial_circulation.length;i++){
			tableStmt.executeUpdate("INSERT INTO VASCULAR_CIRCUITS VALUES ("+Vocabulary.getFMAID(pulmonary_arterial_circulation[i])+", \"PULMONARY_ARTERIAL\");");
			if (arterialCircuitSet.contains(Vocabulary.getFMAID(pulmonary_arterial_circulation[i]))) arterialCircuitSet.remove(Vocabulary.getFMAID(pulmonary_arterial_circulation[i]));
		}

		for (int i=0;i<pulmonary_venous_circulation.length;i++){
			tableStmt.executeUpdate("INSERT INTO VASCULAR_CIRCUITS VALUES ("+Vocabulary.getFMAID(pulmonary_venous_circulation[i])+", \"PULMONARY_VENOUS\");");
			if (venousCircuitSet.contains(Vocabulary.getFMAID(pulmonary_venous_circulation[i]))) venousCircuitSet.remove(Vocabulary.getFMAID(pulmonary_venous_circulation[i]));
		}
		tableStmt.close();

		
		//now populate arterial and venous circuits
		arterialCircuitSet.add(Vocabulary.getFMAID("Celiac trunk"));

		Iterator<String> it = arterialCircuitSet.iterator();
		int size=arterialCircuitSet.size();
		int i=0;
		String[][] vascularCircuit = new String[size][2];
		while (it.hasNext()){
			vascularCircuit[i][0] = it.next();
			vascularCircuit[i][1] = "SYSTEMIC_ARTERIAL";
			i++;
		}
		InsertSQLQuery.insert2DArrayInTable("vascular_circuits", vascularCircuit);
		
		i=0;
		venousCircuitSet.add(Vocabulary.getFMAID("Trunk of coronary sinus"));

		size=venousCircuitSet.size();
		vascularCircuit = new String[size][2];
		it = venousCircuitSet.iterator();
		while (it.hasNext()){
			vascularCircuit[i][0] = it.next();
			vascularCircuit[i][1] = "SYSTEMIC_VENOUS";
			i++;		
		}
		InsertSQLQuery.insert2DArrayInTable("vascular_circuits", vascularCircuit);

		
		} catch (Exception e){
			e.printStackTrace();
		}

	}
	
	
	
}
