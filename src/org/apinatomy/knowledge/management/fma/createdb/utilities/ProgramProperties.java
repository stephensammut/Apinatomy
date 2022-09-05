package org.apinatomy.knowledge.management.fma.createdb.utilities;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

public class ProgramProperties {

	private static String version=null;
	private static String patchFolder=null;
	private static String microcirculations=null;
	private static String branchingOrder=null;
	private static String sqlSchema=null;
	private static String apinatomyIDs=null;
	private static int idCounter=0;
	private static String vascularNetworks=null;
	
	public static String getMicrocirculationFilePath(){
		return patchFolder+"/"+microcirculations;
	}

	public static String getBranchingOrderFilePath(){
		return patchFolder+"/"+branchingOrder;
	}

	public static String getStaticApinatomyIDsFilePath(){
		return patchFolder+"/"+apinatomyIDs;
	}
	
	public static String getSQLSchemaFilePath(){
		return sqlSchema;
	}
	
	public static String getVersion(){
		return version;
	}

	public static int getNextFMAidFromPropertiesFile(){
		return idCounter;
	}
	
	public static String getVascularNetworksFilePath(){
		return  patchFolder+"/"+vascularNetworks;
	}
	
	public static void loadPropertiesFile(String propertiesFileName) {

		// Read properties file.
		Properties properties = new Properties();
		try {
		    FileInputStream propertiesFileStream = new FileInputStream(propertiesFileName);
			properties.load(propertiesFileStream);
			version = properties.getProperty("property_file_version");
			sqlSchema = properties.getProperty("sql_schema_file");
			patchFolder = properties.getProperty("inclusion_folder");
			microcirculations = properties.getProperty("microcirculations_file");
			branchingOrder = properties.getProperty("branching_order_file");
			apinatomyIDs = properties.getProperty("static_ids_file");
			idCounter = Integer.parseInt(properties.getProperty("id_counter"));
			vascularNetworks = properties.getProperty("vascular_networks_file");
			propertiesFileStream.close();
		} 
		
		catch (IOException e) {
			String error = e.getMessage();
			if (error.contains("cannot find the file")){
				System.out.println("Unable to locate properties file ["+propertiesFileName+"] in directory. \nCreating and Aborting.");
				properties.setProperty("property_file_version", "1");
				properties.setProperty("sql_schema_file", "apinatomy.sql");
				properties.setProperty("inclusion_folder", "inclusions");
				properties.setProperty("microcirculations_file", "insert_data_microcirculations.csv");
				properties.setProperty("branching_order_file", "vascular_branch_order.txt");
				properties.setProperty("vascular_networks_file", "insert_data_vascularnetworks.csv");
				properties.setProperty("static_ids_file", "apinatomyIDs.csv");
				properties.setProperty("id_counter", "2000000");

				try {
				    FileOutputStream propertiesFileStream = new FileOutputStream(propertiesFileName);
					properties.store(propertiesFileStream, null);
					propertiesFileStream.close();
				}
				catch (IOException e2) {
					System.out.println(e2.getMessage());
				}
			}
			else {
				System.out.println(error);
			}
			System.exit(0);
		}
	
	}
}
