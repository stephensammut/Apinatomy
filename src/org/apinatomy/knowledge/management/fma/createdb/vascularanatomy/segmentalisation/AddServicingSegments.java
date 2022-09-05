package org.apinatomy.knowledge.management.fma.createdb.vascularanatomy.segmentalisation;

import java.sql.Connection;
import java.util.Enumeration;
import java.util.Iterator;

import org.apinatomy.knowledge.management.fma.createdb.utilities.Vocabulary;

public class AddServicingSegments {

	public  AddServicingSegments(Connection cn, MyVascularSegment mvs, String filler, String vesselName){
		String label = "egment " + mvs.id + " of " + vesselName;	
		String workLabel="";
		MyFMAVesselServiceArrayList mfvsal = SegmentGenerator.servicingTable.get(mvs.vesselFMAid);
		Iterator iter = mfvsal.al.iterator();
		
		int size = mfvsal.al.size();
		int count = 0;
		MyVascularSegment current_MC_mvs = mvs;
		MyVascularSegment previous_MC_mvs = null;
		int type = mvs.whatBiologyType();
		
		while (iter.hasNext()) {
			MyFMAVesselServicingMC mfvsmc = (MyFMAVesselServicingMC) iter.next();
			count++;
			if(count==size){ //we're at the final MC serviced by this vessel
				if(count==1){//this vessel services only one MC
					if(mfvsmc.serviceType.equals("ART")){//this vessel is an artery
						workLabel = "Terminal s" + label + " from " +filler + " to arteriolar side of the "+ mfvsmc.MCFMALabel +" microcirculation";
						current_MC_mvs.endNodeFMAID=mfvsmc.MCFMAid;
						current_MC_mvs.endNodeID=mfvsmc.MCFMAid+"_2";
						current_MC_mvs.addLabel(workLabel);
					}else{//this vessel is a vein
						current_MC_mvs.endNodeFMAID=mfvsmc.MCFMAid;
						current_MC_mvs.endNodeID=mfvsmc.MCFMAid+"_3";						
						workLabel = "Terminal s" + label + " from " + filler + " to venular side of the "+ mfvsmc.MCFMALabel +" microcirculation";
						current_MC_mvs.addLabel(workLabel);				
					}
				}else{//this vessel services many MCs
					SegmentGenerator.segment_ID++;
					if(mfvsmc.serviceType.equals("ART")){//this vessel is an artery
						String gfl = Vocabulary.getDescription(Integer.toString(previous_MC_mvs.endNodeFMAID));
						
						workLabel = "Terminal segment " + SegmentGenerator.segment_ID + " of " + vesselName + " from origin of terminal arteriolar segment to "+ gfl +" to arteriolar side of the "+ mfvsmc.MCFMALabel +" microcirculation";
						current_MC_mvs = new MyVascularSegment(type, SegmentGenerator.segment_ID, previous_MC_mvs.startNodeFMAID, 0, mfvsmc.MCFMAid, 2, mvs.vesselFMAid);
						current_MC_mvs.addLabel(workLabel);
						SegmentGenerator.segmentTable.put(SegmentGenerator.segment_ID,current_MC_mvs);
					}else{//this vessel is a vein
						String gfl = Vocabulary.getDescription(Integer.toString(previous_MC_mvs.endNodeFMAID));

						workLabel = "Terminal segment " + SegmentGenerator.segment_ID + " of " + vesselName + " from origin of terminal venular segment to "+ gfl +" to venular side of the "+ mfvsmc.MCFMALabel +" microcirculation";
						current_MC_mvs = new MyVascularSegment(type, SegmentGenerator.segment_ID,  previous_MC_mvs.startNodeFMAID, 0, mfvsmc.MCFMAid, 3, mvs.vesselFMAid);
						current_MC_mvs.addLabel(workLabel);
						SegmentGenerator.segmentTable.put(SegmentGenerator.segment_ID,current_MC_mvs);
					}				
				}
			}
			else{ //have not yet reached the end of the list of MCs
				SegmentGenerator.segment_ID++;
				if(count==1){ //we're still at the very start of a non-single-member list
					if(mfvsmc.serviceType.equals("ART")){//this vessel is an artery
						//first, the extension
						workLabel = "Segment " + SegmentGenerator.segment_ID + " of " + vesselName + " from " + filler +" to origin of terminal arteriolar segment to " + mfvsmc.MCFMALabel +" microcirculation";
						current_MC_mvs.endNodeFMAID=SegmentGenerator.segment_ID;
						int linker = current_MC_mvs.endNodeFMAID;
						current_MC_mvs.endNodeID=SegmentGenerator.segment_ID+"_0";
						current_MC_mvs.addLabel(workLabel);
						//then bring out the connector to the MC
						SegmentGenerator.segment_ID++;
						workLabel = "Segment " + SegmentGenerator.segment_ID + " of " + vesselName + " from origin of supplying terminal segment to the arteriolar side of the " + mfvsmc.MCFMALabel +" microcirculation";
						current_MC_mvs = new MyVascularSegment(type, SegmentGenerator.segment_ID, linker, 0, mfvsmc.MCFMAid, 2, mvs.vesselFMAid);
						current_MC_mvs.addLabel(workLabel);						
						SegmentGenerator.segmentTable.put(SegmentGenerator.segment_ID,current_MC_mvs);
					}else{//this vessel is a vein
						//first, the extension
						workLabel = "Segment " + SegmentGenerator.segment_ID + " of " + vesselName + " from " + filler +" to origin of terminal venular segment to " + mfvsmc.MCFMALabel +" microcirculation";
						current_MC_mvs.endNodeFMAID=SegmentGenerator.segment_ID;
						int linker = current_MC_mvs.endNodeFMAID;
						current_MC_mvs.endNodeID=SegmentGenerator.segment_ID+"_0";
						current_MC_mvs.addLabel(workLabel);
						//then bring out the connector to the MC
						SegmentGenerator.segment_ID++;
						workLabel = "Segment " + SegmentGenerator.segment_ID + " of " + vesselName + " from origin of supplying terminal segment to the venular side of the " + mfvsmc.MCFMALabel +" microcirculation";
						current_MC_mvs = new MyVascularSegment(type, SegmentGenerator.segment_ID, linker, 0, mfvsmc.MCFMAid, 3, mvs.vesselFMAid);
						current_MC_mvs.addLabel(workLabel);						
						SegmentGenerator.segmentTable.put(SegmentGenerator.segment_ID,current_MC_mvs);						
					}				
				}else{ // we're past the fist item on the list but have not reach the end yet
					if(mfvsmc.serviceType.equals("ART")){//this vessel is an artery
						//first, the extension
						String gfl = Vocabulary.getDescription(Integer.toString(previous_MC_mvs.endNodeFMAID));
						workLabel = "Segment " + SegmentGenerator.segment_ID + " of " + vesselName + " from origin of terminal arteriolar segment to "+ gfl +" to origin of terminal arteriolar segment to "+ mfvsmc.MCFMALabel +" microcirculation";
						current_MC_mvs = new MyVascularSegment(type, SegmentGenerator.segment_ID, previous_MC_mvs.startNodeFMAID, 0, SegmentGenerator.segment_ID, 0, mvs.vesselFMAid);
						current_MC_mvs.addLabel(workLabel);
						SegmentGenerator.segmentTable.put(SegmentGenerator.segment_ID,current_MC_mvs);
						int linker = SegmentGenerator.segment_ID;
						//then bring out the connector to the MC
						SegmentGenerator.segment_ID++;
						workLabel = "Segment " + SegmentGenerator.segment_ID + " of " + vesselName + " from origin of supplying terminal segment to the arteriolar side of the " + mfvsmc.MCFMALabel +" microcirculation";
						current_MC_mvs = new MyVascularSegment(type, SegmentGenerator.segment_ID, linker, 0, mfvsmc.MCFMAid, 2, mvs.vesselFMAid);
						current_MC_mvs.addLabel(workLabel);						
						SegmentGenerator.segmentTable.put(SegmentGenerator.segment_ID,current_MC_mvs);
					}else{//this vessel is a vein
						//first, the extension
						String gfl = Vocabulary.getDescription(Integer.toString(previous_MC_mvs.endNodeFMAID));
						workLabel = "Segment " + SegmentGenerator.segment_ID + " of " + vesselName + " from origin of terminal venular segment to "+ gfl +" to origin of terminal venular segment to "+ mfvsmc.MCFMALabel +" microcirculation";
						current_MC_mvs = new MyVascularSegment(type, SegmentGenerator.segment_ID, previous_MC_mvs.startNodeFMAID, 0, SegmentGenerator.segment_ID, 0, mvs.vesselFMAid);
						current_MC_mvs.addLabel(workLabel);
						SegmentGenerator.segmentTable.put(SegmentGenerator.segment_ID,current_MC_mvs);
						int linker = SegmentGenerator.segment_ID;
						//then bring out the connector to the MC
						SegmentGenerator.segment_ID++;
						workLabel = "Segment " + SegmentGenerator.segment_ID + " of " + vesselName + " from origin of supplying terminal segment to the venular side of the " + mfvsmc.MCFMALabel +" microcirculation";
						current_MC_mvs = new MyVascularSegment(type, SegmentGenerator.segment_ID, linker, 0, mfvsmc.MCFMAid, 3, mvs.vesselFMAid);
						current_MC_mvs.addLabel(workLabel);						
						SegmentGenerator.segmentTable.put(SegmentGenerator.segment_ID,current_MC_mvs);					
					}				
				}
				//System.out.println(label + " hekk " + current_MC_mvs.label);
				previous_MC_mvs = current_MC_mvs;
			}
		}
		
	}
	
}
