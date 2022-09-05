package org.apinatomy.knowledge.management.fma.createdb.vascularanatomy.segmentalisation;

public class MyFMAVesselServicingMC {

	/**
	 * @uml.property  name="servicingVesselFMAid"
	 */
	public int servicingVesselFMAid;
	/**
	 * @uml.property  name="mCFMAid"
	 */
	public int MCFMAid;
	/**
	 * @uml.property  name="serviceType"
	 * @uml.associationEnd  qualifier="key:java.lang.Integer java.lang.Integer"
	 */
	public String serviceType;
	/**
	 * @uml.property  name="servicingVesselFMALabel"
	 * @uml.associationEnd  qualifier="key:java.lang.Integer java.lang.Integer"
	 */
	public String servicingVesselFMALabel;
	/**
	 * @uml.property  name="mCFMALabel"
	 * @uml.associationEnd  qualifier="key:java.lang.Integer java.lang.Integer"
	 */
	public String MCFMALabel;
	/**
	 * @uml.property  name="mCSegmentID"
	 */
	public int MCSegmentID;
		
	public MyFMAVesselServicingMC (Integer servicingVesselFMAidIN, String servicingVesselFMALabelIN, Integer MCFMAidIN, String MCFMALabelIN, String serviceTypeIN, Integer MCSegmentIDIN) {
		servicingVesselFMAid = servicingVesselFMAidIN;
		MCFMAid = MCFMAidIN;
		serviceType = serviceTypeIN;
		servicingVesselFMALabel = servicingVesselFMALabelIN;
		MCFMALabel = MCFMALabelIN;
		MCSegmentID = MCSegmentIDIN;
		
		//System.out.println(toString());
	}
	
	public String toString() { // Always good for debugging
		return "FMA Vessel " + servicingVesselFMAid +  " (" + servicingVesselFMALabel + ") services as " + serviceType + " the microcirculation of " + MCFMAid + " (" + MCFMALabel + ")";
	}
	
}
