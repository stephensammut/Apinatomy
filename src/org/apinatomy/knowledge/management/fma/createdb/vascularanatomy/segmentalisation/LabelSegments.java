package org.apinatomy.knowledge.management.fma.createdb.vascularanatomy.segmentalisation;

import java.sql.Connection;
import java.util.Enumeration;

import org.apinatomy.knowledge.management.fma.createdb.utilities.Vocabulary;

public class LabelSegments {

	/**
	 * @uml.property  name="e"
	 */
	private Enumeration<Integer> e = SegmentGenerator.segmentTable.keys();
	/**
	 * @uml.property  name="cn"
	 */
	private Connection cn;

	public LabelSegments(Connection cnIN){

		cn=cnIN;
		while(e.hasMoreElements()){
			
			MyVascularSegment mvsNext = SegmentGenerator.segmentTable.get(e.nextElement());
			if(mvsNext.whatBiologyType() != 2){
				generateSegmentLabel(mvsNext);
			}
		}
		
	}
	
	private void generateSegmentLabel(MyVascularSegment mvs){

		//System.out.println(mvs.vesselFMAid + " " + mvs.startNodeFMAID +" String: "+ mvs.toString());
		String fmal_start = Vocabulary.getDescription(Integer.toString(mvs.startNodeFMAID));
		String fmal_vessel =Vocabulary.getDescription(Integer.toString(mvs.vesselFMAid));
		String label = "egment " + mvs.id + " of " + fmal_vessel;
		
		if(mvs.endNodeType == 1){ // so here we have a terminal segment
			if(mvs.startNodeFMAID == mvs.vesselFMAid){ // we have a single-segment vessel on our hands as segment starts at point of FMAvessel origin
				if(SegmentGenerator.servicingTable.containsKey(mvs.vesselFMAid)){ // this single-segment vessel is servicing an MC
					AddServicingSegments addss = new AddServicingSegments(cn, mvs, "its origin", fmal_vessel);
				}else{
					// this one-segment vessel does not service anything
					mvs.addLabel("Terminal s" + label + " from its origin to its terminus");
					SegmentGenerator.segmentTable.put(mvs.id,mvs);
				}
			}else{ // this segment is not from a single-segment vessel
				if(SegmentGenerator.servicingTable.containsKey(mvs.vesselFMAid)){ // this multi-segment vessel is servicing an MC
					AddServicingSegments addss = new AddServicingSegments(cn, mvs, " the origin of "+ fmal_start, fmal_vessel);
				}else{
					// this multi-segment vessel does not service anything
					mvs.addLabel("Terminal s" + label + " from the origin of "+ fmal_start +" to its terminus");
					SegmentGenerator.segmentTable.put(mvs.id,mvs);
				}
			}
		}else{ // this segment is not a terminal one
			String fmal_end = Vocabulary.getDescription(Integer.toString(mvs.endNodeFMAID));
			if(mvs.startNodeFMAID == mvs.vesselFMAid){ // the segment is at the origin of the vessel
				mvs.addLabel("S" + label + " from its origin to the origin of " + fmal_end);
			}else{ // the segment is not at the origin of the vessel
				mvs.addLabel("S" + label + " from the origin of " + fmal_start + " to the origin of " + fmal_end);
			}
			SegmentGenerator.segmentTable.put(mvs.id,mvs);
		}
		
	}
	
}
