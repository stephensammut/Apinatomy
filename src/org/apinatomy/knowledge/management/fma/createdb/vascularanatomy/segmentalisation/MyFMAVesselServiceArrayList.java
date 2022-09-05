package org.apinatomy.knowledge.management.fma.createdb.vascularanatomy.segmentalisation;

import java.util.ArrayList;

public class MyFMAVesselServiceArrayList {

	/**
	 * @uml.property  name="al"
	 * @uml.associationEnd  multiplicity="(0 -1)" elementType="org.apinatomy.createdatabase.vascularanatomy.segmentalisation.MyFMAVesselServicingMC"
	 */
	ArrayList <MyFMAVesselServicingMC> al = new ArrayList<MyFMAVesselServicingMC>();
	/**
	 * @uml.property  name="servicingVesselFMAid"
	 */
	public int servicingVesselFMAid;
	
	public MyFMAVesselServiceArrayList(Integer servicingVesselFMAidIN){
		
		servicingVesselFMAid = servicingVesselFMAidIN;
		
	}
	
	public void addMC(MyFMAVesselServicingMC mc){
		al.add(mc);
	}
	
}
