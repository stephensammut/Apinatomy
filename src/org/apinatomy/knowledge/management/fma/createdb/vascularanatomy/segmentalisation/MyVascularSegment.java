package org.apinatomy.knowledge.management.fma.createdb.vascularanatomy.segmentalisation;

public class MyVascularSegment {
	/**
	 * @uml.property  name="id"
	 */
	public int id;
	/**
	 * @uml.property  name="startNodeID"
	 */
	public String startNodeID;
	/**
	 * @uml.property  name="endNodeID"
	 */
	public String endNodeID;
	/**
	 * @uml.property  name="startNodeFMAID"
	 */
	public int startNodeFMAID;
	/**
	 * @uml.property  name="endNodeFMAID"
	 */
	public int endNodeFMAID;
	/**
	 * @uml.property  name="startNodeType"
	 */
	public int startNodeType;
	/**
	 * @uml.property  name="endNodeType"
	 */
	public int endNodeType;
	/**
	 * @uml.property  name="vesselFMAid"
	 */
	public int vesselFMAid;
	/**
	 * @uml.property  name="label"
	 */
	public String label;
	/**
	 * @author   Stephen
	 */
	public enum SegmentBiologyTypeList {/**
	 * @uml.property  name="aRTERY"
	 * @uml.associationEnd  
	 */
	ARTERY,/**
	 * @uml.property  name="vEIN"
	 * @uml.associationEnd  
	 */
	VEIN,/**
	 * @uml.property  name="mICROCIRULATION"
	 * @uml.associationEnd  
	 */
	MICROCIRULATION};
	/**
	 * @uml.property  name="segmentBiologyType"
	 * @uml.associationEnd  multiplicity="(1 1)"
	 */
	public SegmentBiologyTypeList segmentBiologyType;
	
	
	public MyVascularSegment (Integer sBTIN, Integer idIN, Integer startNodeFMAIDIN, Integer startNodeTypeIN, Integer endNodeFMAIDIN, Integer endNodeTypeIN, Integer vesselFMAidIN) {
		
		switch (sBTIN) {
	    case 1:
	    	segmentBiologyType = SegmentBiologyTypeList.ARTERY;
	      break;
	    case 2:
	    	segmentBiologyType = SegmentBiologyTypeList.MICROCIRULATION;
	      break;
	    case 3:
	    	segmentBiologyType = SegmentBiologyTypeList.VEIN;
	      break;
	    }
		
		id = idIN; 

		startNodeFMAID = startNodeFMAIDIN;
		endNodeFMAID = endNodeFMAIDIN;
		startNodeType = startNodeTypeIN;
		endNodeType = endNodeTypeIN;
		
		startNodeID = startNodeFMAIDIN+"_"+startNodeTypeIN;
		endNodeID = endNodeFMAIDIN+"_"+endNodeTypeIN;
		
		vesselFMAid = vesselFMAidIN;
	}
	
	public String toString() { 
		return id + " of " + vesselFMAid + " from: " + startNodeID + " to: " + endNodeID;
	}
	
	public void addLabel(String labelIN){
		label = labelIN;
	}
	
	public String getFullLabel(){
		String fullLabel = "";
		switch (segmentBiologyType) {
	    case ARTERY:
	    	fullLabel = "Arterial " + label;
	      break;
	    case MICROCIRULATION:
	    	fullLabel = "Microcirculation segment of " + label;
	      break;
	    case VEIN:
	    	fullLabel = "Venous " + label;
	      break;
	    }

	return fullLabel;
	}
	
	public Integer whatBiologyType(){
		
		int type = 0;
		switch (segmentBiologyType) {
	    case ARTERY:
	    	type=1;
	      break;
	    case MICROCIRULATION:
	    	type=2;
	      break;
	    case VEIN:
	    	type=3;
	      break;
	    }

	return type;
	}
	
}
