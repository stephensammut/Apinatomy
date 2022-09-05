package org.apinatomy.knowledge.management.fma.createdb.vascularanatomy.segmentalisation;

import java.io.IOException;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map;

import org.apinatomy.knowledge.management.fma.createdb.sql.DBConnections;
import org.apinatomy.knowledge.management.fma.createdb.sql.InsertSQLQuery;

public class SegmentGenerator {

	static int segment_ID = 0;
	
	//lists organs that have a microcirculation <FMAID, internal API 'segment' ID>
	static Hashtable <Integer,Integer> FMAidToMCSegmentid = new Hashtable <Integer,Integer>();

	static Hashtable <Integer,MyVascularSegment> segmentTable = new Hashtable <Integer,MyVascularSegment>();
	static Hashtable <Integer,MyFMAVesselServiceArrayList> servicingTable = new Hashtable <Integer,MyFMAVesselServiceArrayList>();
	static Hashtable rdnTrap = new Hashtable();
	
	
		public static void main(){
			
		
		// Start off by building microcirculation maps - which organs have a blood supply?
		// populates FMAidToMCSegmentid 
		GenerateMCs gmcs = new GenerateMCs();
		
		// Right atrium
		FollowSegment fs = new FollowSegment(DBConnections.getApinatomyConnection(),7096,3);
		// Right ventricle
		FollowSegment fs1 = new FollowSegment(DBConnections.getApinatomyConnection(),7098,1);
		// Left ventricle
		FollowSegment fs2 = new FollowSegment(DBConnections.getApinatomyConnection(),7101,1);
		// Left atrium
		FollowSegment fs3 = new FollowSegment(DBConnections.getApinatomyConnection(),7097,3);
		// Trunk of portal vein
		FollowSegment fs4 = new FollowSegment(DBConnections.getApinatomyConnection(),14329,3);
		
		LabelSegments ls = new LabelSegments(DBConnections.getApinatomyConnection());
	
		Enumeration<MyVascularSegment> e = SegmentGenerator.segmentTable.elements();
		ArrayList<String[]> list = new ArrayList<String[]>();
		
		
		
		while(e.hasMoreElements()){
			
			MyVascularSegment mvs = e.nextElement();
			String a1 = Integer.toString(mvs.id);
			String a2 = Integer.toString(mvs.vesselFMAid);
			String a3 = mvs.startNodeID;
			String a4 = mvs.endNodeID;
			String a5 = mvs.getFullLabel();
			String a6 = Integer.toString(mvs.whatBiologyType());
			String a7 = Integer.toString(mvs.startNodeFMAID);
			String a8 = Integer.toString(mvs.endNodeFMAID);
			String a9 = Integer.toString(mvs.startNodeType);
			String a10 = Integer.toString(mvs.endNodeType);
			
			list.add(new String[]{a1,a2,a3,a4,a5,a6,a7,a8,a9,a10});
		}

		String[][] segmentArray = new String[list.size()][10];
		for (int r=0;r<list.size();r++){
			String[] contents = list.get(r);
			for (int c=0;c<10;c++){
				segmentArray[r][c]=contents[c];
			}
		}
		
		InsertSQLQuery.insert2DArrayInTable("vascular_segments", segmentArray);
	}
	
}
