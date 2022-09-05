package org.apinatomy.knowledge.management.fma.createdb.sql;

import java.sql.Statement;


public class SQLTableUniqueIndex {

	public static void makeUnique(){
		try{
			Statement tableStmt = DBConnections.getApinatomyConnection().createStatement();
			tableStmt.executeUpdate("ALTER IGNORE TABLE arterial_network ADD UNIQUE INDEX(vessel_from,vessel_to);");
			tableStmt.executeUpdate("ALTER IGNORE TABLE venous_network ADD UNIQUE INDEX(vessel_from,vessel_to);");
			tableStmt.executeUpdate("ALTER IGNORE TABLE microcirculations ADD UNIQUE INDEX(vessel, organ,type);");
			tableStmt.executeUpdate("ALTER IGNORE TABLE body_compartment_contents ADD UNIQUE INDEX(compartment_id, fma_id);");

			tableStmt.close();
		}
		catch (Exception e){
			e.printStackTrace();
		}
	}
}
