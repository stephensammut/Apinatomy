package org.apinatomy.knowledge.management.fma.createdb;

import java.util.HashSet;

import org.apinatomy.knowledge.management.fma.createdb.compartments.CompartmentaliseBody;
import org.apinatomy.knowledge.management.fma.createdb.sql.DBConnections;
import org.apinatomy.knowledge.management.fma.createdb.sql.SQLSchema;
import org.apinatomy.knowledge.management.fma.createdb.sql.SQLTableUniqueIndex;
import org.apinatomy.knowledge.management.fma.createdb.structuralanatomy.BodyStructures;
import org.apinatomy.knowledge.management.fma.createdb.structuralanatomy.StructuralPartonomy;
import org.apinatomy.knowledge.management.fma.createdb.utilities.FMAIDManager;
import org.apinatomy.knowledge.management.fma.createdb.utilities.FileParser;
import org.apinatomy.knowledge.management.fma.createdb.utilities.ProgramProperties;
import org.apinatomy.knowledge.management.fma.createdb.vascularanatomy.EstablishVascularConnectivity;
import org.apinatomy.knowledge.management.fma.createdb.vascularanatomy.IdentifyVessels;
import org.apinatomy.knowledge.management.fma.createdb.vascularanatomy.Microcirculations;
import org.apinatomy.knowledge.management.fma.createdb.vascularanatomy.VascularCircuits;
import org.apinatomy.knowledge.management.fma.createdb.vascularanatomy.VesselBranchOrder;
import org.apinatomy.knowledge.management.fma.createdb.vascularanatomy.VesselStructure;
import org.apinatomy.knowledge.management.fma.createdb.vascularanatomy.segmentalisation.SegmentGenerator;


public class DatabaseCreation {

   
	public static void main(String[] args) {
		
        //load apinatomy properties file
        ProgramProperties.loadPropertiesFile("properties.txt");
        
		
		//Get database parameters from command line
		String host=null,fmadb=null,apidb=null,user=null,passwd=null;
       // for (int i = 0; i < args.length; i++) {
        //	if (args[i].startsWith("-h")) host = args[i].substring(2);
        //	else if (args[i].startsWith("-fmadb")) fmadb = args[i].substring(6);
        //	else if (args[i].startsWith("-newdb")) apidb=args[i].substring(6);
        //	else if (args[i].startsWith("-u")) user = args[i].substring(2);
        //	else if (args[i].startsWith("-p")) passwd = args[i].substring(2);
       // }
       
        host="localhost";
        fmadb="fma_db";
        apidb="apinatomy_db_test";
        user="username";
        passwd="password";
        
        if (host==null || fmadb==null || apidb==null ||user==null || passwd==null){  
        	System.out.println("> Command syntax: -hHOST -fmadbFMA_DATABASE -newdbAPINATOMY_DATABASE -uUSER -pPASSWORD");
        	System.exit(1);
        }
        
		
        System.out.println("ApiNATOMY database generation classes initiated\n");

        
        //connect to the FMA and ApiNATOMY databases
        System.out.println("Connecting to FMA ["+fmadb+"] and ApiNATOMY ["+ apidb+"] databases on '"+host+"'\n");
        DBConnections.initialiseConnections(host, fmadb, apidb, user, passwd);
    

        //load and implement SQL Schema
		System.out.println("> Loading schema and generating ApiNATOMY sql tables");
		SQLSchema.implementSchema(ProgramProperties.getSQLSchemaFilePath());
		
		
        //import static apinatomy ids
        FMAIDManager.loadStaticIDsFromFile();
        
        
        //populate structures table
		System.out.println("> Extracting core body structures and populating structures table");
		BodyStructures.createStructuresTable();
				
		
		//get regional/constitutional/direct-subclass parts from fma
		System.out.println("> Extracting and defining organism partonomies");
		StructuralPartonomy.populatePartonomyTables();
		

		System.out.println("> Analysing vascular tree");

		
		//identify arteries and veins
		System.out.println("  > Identifying vessels");
		HashSet<String> arterySet = IdentifyVessels.getSetofArteriesFromFMA();
		HashSet<String> veinSet = IdentifyVessels.getSetOfVeinsFromFMA();
		
		//run java garbage collector
		System.gc();
		
		
		//populate venous and arterial network tables
		System.out.println("  > Characterising vascular connectivity");
		EstablishVascularConnectivity.getArterialConnectionsFromFMA(arterySet);	
		EstablishVascularConnectivity.getVenousConnectionsFromFMA(veinSet);
		EstablishVascularConnectivity.getBrainSinusConnections();
		EstablishVascularConnectivity.connectMiscVessels();
		
		
		// insert custom networks
		FileParser.parseCSVFile(ProgramProperties.getVascularNetworksFilePath());
		
		
		//import vessel branch ordering
		System.out.println("  > Importing vascular tree branch order");
		VesselBranchOrder.startBranchOrderInsertion(ProgramProperties.getBranchingOrderFilePath());
		
		
		//import organ vasculature from FMA and file
		System.out.println("> Extracting microcirculations and additional vessels");
		Microcirculations.importMicrocirculationsFromFMA("ART");
		Microcirculations.importMicrocirculationsFromFMA("VEN");	
		FileParser.parseCSVFile(ProgramProperties.getMicrocirculationFilePath());
		

		//having updated the vocabulary with new vessels, re-update set
		arterySet = IdentifyVessels.getSetofArteriesFromFMA();
		veinSet = IdentifyVessels.getSetOfVeinsFromFMA();
		VesselStructure.createWallAndLumen(arterySet);
		VesselStructure.createWallAndLumen(veinSet);
	

		//generate vascular circuits
		System.out.println("> Generating circuits");
		VascularCircuits.populateVascularCircuitsTable();
		
		
		//generate vascular segments
		System.out.println("> Generating vascular segments");
		SegmentGenerator.main();
		
		
		//create body compartments
		System.out.println("> Generating body compartments");
		CompartmentaliseBody.main();
				
		SQLTableUniqueIndex.makeUnique();
		
		System.out.println("\n> ApiNATOMY database generated successfully");
		System.exit(1);
	}

}
