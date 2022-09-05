package org.apinatomy.knowledge.management.fma.createdb.vascularanatomy.segmentalisation;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Hashtable;

import org.apinatomy.knowledge.management.fma.createdb.sql.DBConnections;
import org.apinatomy.knowledge.management.fma.createdb.utilities.Vocabulary;
import org.apinatomy.knowledge.management.fma.createdb.vascularanatomy.segmentalisation.MyVascularSegment.SegmentBiologyTypeList;

public class GenerateMCs {

	
	
	public  GenerateMCs(){
		
		try {
			
			// get list of organs that have a vascular supply (FMAID)
			Statement stSEL1 = DBConnections.getApinatomyConnection().createStatement();			
			ResultSet rsSEL_MC = stSEL1.executeQuery("select distinct ORGAN from microcirculations");
			
			while (rsSEL_MC.next()) {
				Integer organ = rsSEL_MC.getInt("ORGAN");
				
				//lists organs that have a microcirculation <organ FMAID, internal API 'segment' ID>
				SegmentGenerator.FMAidToMCSegmentid.put(organ,SegmentGenerator.segment_ID);
				
				//2=Microcirculation
				//Organ internal ID
				//startNodeFMAID (organ FMAID)
				//startNodeType (microcirculation=2)
				//endNodeFMAID (organ FMAID)
				//endNodeType (vein=3)
				//vesselFMAid (organ FMAID)
				MyVascularSegment mvs = new MyVascularSegment(2,SegmentGenerator.segment_ID, organ, 2, organ, 3, organ);
				mvs.addLabel(Vocabulary.getDescription(Integer.toString(organ)));
				System.out.println(mvs.label);
				SegmentGenerator.segmentTable.put(SegmentGenerator.segment_ID,mvs);
				SegmentGenerator.segment_ID++;
			}

			rsSEL_MC.close();
			stSEL1.close();
			
			//===
			// now get list of vessels in the microcirculation table
			Statement stSEL2 = DBConnections.getApinatomyConnection().createStatement();			
			ResultSet rsSEL_VS = stSEL2.executeQuery("select distinct VESSEL from microcirculations");
			
			while (rsSEL_VS.next()) {
				
				int vessel = rsSEL_VS.getInt("VESSEL");
				//for each vessel, identify which organ it supplies and what type of vessel it is (ART vs VEN)
				Statement stSEL3 = DBConnections.getApinatomyConnection().createStatement();
				ResultSet rsSEL_OT = stSEL3.executeQuery("select ORGAN, TYPE from microcirculations where VESSEL="+ vessel +" order by ORGAN");

				MyFMAVesselServiceArrayList mfvsal = new MyFMAVesselServiceArrayList(vessel);
				
				while (rsSEL_OT.next()) {
					int organ =rsSEL_OT.getInt("ORGAN"); 
					String type = rsSEL_OT.getString("TYPE");
					
					// vessel FMAID,
					// vessel description/label
					// organ FMAID
					// organ description/label
					// ART or VEN
					// internal organ ID from SegmentGenerator.FMAidToMCSegmentid
					mfvsal.addMC(new MyFMAVesselServicingMC(vessel,Vocabulary.getDescription(Integer.toString(vessel)),organ,Vocabulary.getDescription(Integer.toString(organ)),type,SegmentGenerator.FMAidToMCSegmentid.get(organ)));
				}
				rsSEL_OT.close();
				stSEL3.close();
				SegmentGenerator.servicingTable.put(vessel,mfvsal);
			}
			rsSEL_VS.close();
			stSEL2.close();
			
		} catch (SQLException ex){
			System.err.println("Error: " + ex.getMessage());
			ex.printStackTrace();
			System.exit(1);
		}
	}
	
}
