package org.apinatomy.knowledge.management.fma.createdb.vascularanatomy.segmentalisation;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Hashtable;

public class GetVascularBranches {

	/**
	 * @uml.property  name="cn"
	 */
	private Connection cn;
	/**
	 * @uml.property  name="fmaID"
	 */
	private int fmaID = 0;
	/**
	 * @uml.property  name="rTrap"
	 * @uml.associationEnd  qualifier="valueOf:java.lang.Integer java.lang.Integer"
	 */
	// contains a list of vascular branches that have been added
	private Hashtable rTrap = new Hashtable();
	
	public GetVascularBranches(Connection cnIN, int fmaIDIN){
		cn=cnIN;
		fmaID=fmaIDIN;
	}
	
	//gets vessel branching order from database
	
	public ArrayList <Integer> getOrder(){
		
		//contains an ordered list of vessels and branches.
		ArrayList<Integer> order = new ArrayList<Integer>();
		
		try {
			//get branches from main vessel: not all will have an entry (eg no branch)
			Statement stSEL1 = (Statement) cn.createStatement();
			System.out.println("select distinct BRANCH from branching_order where MAIN_VESSEL= "+fmaID+" order by SEQUENCE");
			ResultSet rsSEL_BR = stSEL1.executeQuery("select distinct BRANCH from branching_order where MAIN_VESSEL= "+fmaID+" order by SEQUENCE");
			
			while (rsSEL_BR.next()) {
				int branch = rsSEL_BR.getInt("BRANCH");
				rTrap.put(branch,1);
				order.add(branch); 
			}

			rsSEL_BR.close();
			stSEL1.close();
			
			//get venous network
			Statement stSEL2 = (Statement) cn.createStatement();
			ResultSet rsSEL_VC = stSEL2.executeQuery("select VESSEL_FROM from venous_network where VESSEL_TO="+fmaID+" order by VESSEL_FROM");
			
			while (rsSEL_VC.next()) {
				int vessel = rsSEL_VC.getInt("VESSEL_FROM");
				if(!(rTrap.containsKey(vessel))){
					order.add(vessel);
				}	 
			}

			rsSEL_VC.close();
			stSEL2.close();

			//get arterial network
			Statement stSEL3 = (Statement) cn.createStatement();
			ResultSet rsSEL_AC = stSEL3.executeQuery("select VESSEL_TO from arterial_network where VESSEL_FROM="+fmaID+" order by VESSEL_TO");
			
			while (rsSEL_AC.next()) {
				int vessel = rsSEL_AC.getInt("VESSEL_TO");

				if(!(rTrap.containsKey(vessel))){
					order.add(vessel);
				}	 
			}

			rsSEL_AC.close();
			stSEL3.close();		
			
		} catch (SQLException ex){
			System.err.println("Error: " + ex.getMessage());
			ex.printStackTrace();
		}
	
		return order;
	}
}
