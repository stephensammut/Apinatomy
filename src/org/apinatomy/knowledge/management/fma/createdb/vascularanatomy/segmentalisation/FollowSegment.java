package org.apinatomy.knowledge.management.fma.createdb.vascularanatomy.segmentalisation;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Iterator;

public class FollowSegment {

	/**
	 * @uml.property  name="fmaID"
	 */
	public int fmaID ;
	/**
	 * @uml.property  name="prox_node"
	 */
	public int prox_node;
	/**
	 * @uml.property  name="dist_node"
	 */
	public int dist_node;
	/**
	 * @uml.property  name="sBT"
	 */
	public int sBT;
	/**
	 * @uml.property  name="cn"
	 * @uml.associationEnd  multiplicity="(0 -1)" elementType="java.lang.Integer"
	 */
	private Connection cn;
	
	
	///gets structures attached to fmaIDIN
	//cnIN = database connection
	//fmaIDIN = FMA node ID to start from: the proximal node
	//sBTIN: 1= arterial circulation, 3=venous circulation
	
	public FollowSegment(Connection cnIN, int fmaIDIN, int sBTIN){

		cn = cnIN;
		fmaID = fmaIDIN;
		prox_node = fmaID;
		dist_node = 0;
		sBT = sBTIN;

		try {
			Statement stSEL1 = (Statement) cn.createStatement();
			ResultSet rsSEL_BR = stSEL1.executeQuery(""
					+ ""+fmaID);

			while (rsSEL_BR.next()) {
				if(rsSEL_BR.getInt("count(*)") == 0){
					// no connections found
					pursueVessel();
				}
			}

			rsSEL_BR.close();
			stSEL1.close();

		} catch (SQLException ex){
			System.err.println("Error: " + ex.getMessage());
			ex.printStackTrace();
			System.exit(1);
		}

	}

	// get vessel branches+connections
	public void pursueVessel(){	
		
		int terminal = 1;
		
		//get the order of all vascular branches for a start node
		GetVascularBranches gvbs = new GetVascularBranches(cn,fmaID);
		ArrayList <Integer> al = gvbs.getOrder();
		
		//iterate over these branches
		Iterator iter = al.iterator();

		while (iter.hasNext()) {

			dist_node = (Integer) iter.next();
			//System.out.println(fmaID + " : "+ prox_node + " " + dist_node);
			SegmentGenerator.segmentTable.put(SegmentGenerator.segment_ID,new MyVascularSegment(sBT,SegmentGenerator.segment_ID, prox_node, 0, dist_node, 0, fmaID));
			SegmentGenerator.segment_ID++;
			if(!(SegmentGenerator.rdnTrap.containsKey(dist_node))){
				SegmentGenerator.rdnTrap.put(dist_node,1);
				//recursively go through branch to identify other branching. sBT (1=ART, 3=VEIN)
				FollowSegment fs = new FollowSegment(cn, dist_node, sBT);
			}
			terminal = 0;
			prox_node=dist_node;
		}

		if(terminal == 1){
			SegmentGenerator.segmentTable.put(SegmentGenerator.segment_ID,new MyVascularSegment(sBT,SegmentGenerator.segment_ID, prox_node, 0,prox_node, 1, fmaID));
			SegmentGenerator.segment_ID++;
		}else{
			SegmentGenerator.segmentTable.put(SegmentGenerator.segment_ID,new MyVascularSegment(sBT,SegmentGenerator.segment_ID, dist_node, 0,fmaID, 1, fmaID));
			SegmentGenerator.segment_ID++;
		}

	}

}
