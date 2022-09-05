package org.apinatomy.knowledge.management.fma.createdb.compartments;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.apinatomy.knowledge.management.fma.createdb.sql.DBConnections;
import org.apinatomy.knowledge.management.fma.createdb.sql.InsertSQLQuery;
import org.apinatomy.knowledge.management.fma.createdb.utilities.Vocabulary;

public class CompartmentaliseBody {

	static Set<String> initialFMAIDsToIncludeInTile = new HashSet<String>();
	static Set<String> finalListFMAIDsToIncludeInTile = new HashSet<String>();
	static Set<String> exludeFMAIDsFromTile = new HashSet<String>();
	static Set<String> processedIDs = new HashSet<String>();

	static HashMap<String, String[]> compartmentKeyIDsInclude = new HashMap<String, String[]>();
	static HashMap<String, String[]> compartmentKeyIDsExclude = new HashMap<String, String[]>();

	final private static String compartmentAnnotations[][] = new String[][] {
			{ "1", "Large intestine" }, { "2", "Jejuno-ileum" },
			{ "3", "Liver Pancreas Duodenum" }, { "4", "Stomach" },
			{ "5", "Oesophagus" }, { "6", "ENT" }, { "7", "Genitals/Gonads" },
			{ "8", "Lower vessels" }, { "9", "Spleen and abdominal vessels" }, 
			{ "10", "Heart" }, { "11", "Upper vessels" }, { "12", "Lungs" },
			{ "13", "Urinary tract" }, 
			
			{ "14", "Cauda equina" },{ "15", "Lower spinal" },{ "16", "Upper spinal" },
			
			{ "17", "Brain" }, { "18", "Cranial nerves" }, { "19", "Lower limb" },
			{ "20", "Pelvis" }, { "21", "Abdomen" }, { "22", "Thorax" },
			{ "23", "Neck_UpperLimb" }, { "24", "Head" } };

	public static void insertcompartmentDefinitionsInDB() {
		InsertSQLQuery.insert2DArrayInTable("body_compartment_description",
				compartmentAnnotations);
	}
	
	public static void populateCompartmentSeedTable(){
		Iterator it = compartmentKeyIDsInclude.keySet().iterator();
		while (it.hasNext()){
			String key = it.next().toString();
			String values[] = compartmentKeyIDsInclude.get(key);
			for (int i=0;i<values.length;i++){
				System.out.println("insert into body_compartment_contents_seeds values ('"+key+"','"+values[i]+"', 'Y');");
			}
		}
		
		
		 it = compartmentKeyIDsExclude.keySet().iterator();
		while (it.hasNext()){
			String key = it.next().toString();
			String values[] = compartmentKeyIDsExclude.get(key);
			for (int i=0;i<values.length;i++){
				System.out.println("insert into body_compartment_contents_seeds values ('"+key+"','"+values[i]+"', 'N');");
			}
		}
		
		
	}

	
	private static void populateCompartmentMap() {
		compartmentKeyIDsInclude.put("1", new String[] { "7201","14543","15711"});
		compartmentKeyIDsInclude.put("2", new String[] { "7208", "7207" });
		compartmentKeyIDsInclude.put("3", new String[] { "7206", "7197",
				"265228", "9706" ,"70586","79646","16017"});
		compartmentKeyIDsInclude.put("4", new String[] { "7148" });
		compartmentKeyIDsInclude.put("5", new String[] { "7131" });
		compartmentKeyIDsInclude.put("6", new String[] { "54396","59519","54397", "79771", "46472",
				"52780", "46688", "9597", "9703", "12516","55097","52750",
				"64854","59684","49184","55108","57417","84115","59101","59679",
				"61063","59365","56513","52746","59502","46726","52745","52826","49026","52897","73758"});
		compartmentKeyIDsInclude.put("7", new String[] { "7160", "7209",
				"17558", "18250", "19650","19624",
				"45652","7210","9909"});
		
		compartmentKeyIDsInclude.put("8", new String[] { "14750","87217" });

		
		compartmentKeyIDsInclude.put("9", new String[] { "49893","14751","50737","50735","66645","71904","14749","7196", "9604" });
		compartmentKeyIDsInclude.put("10", new String[] { "7088", "7099",
				"7100", "7110", "7180", "7233"});
		compartmentKeyIDsInclude.put("11", new String[] { "3768","66326","3939","77670","50981","3947","9603", "13890" });

		compartmentKeyIDsInclude.put("12", new String[] { "9690", "9583","45662", "7195",
				"7409" });
		compartmentKeyIDsInclude.put("13", new String[] { "7159" });
		compartmentKeyIDsInclude.put("16", new String[] { "86883", "6629","44836",
				"7647" });
		compartmentKeyIDsInclude.put("17", new String[] { "61833","61835","62514","242188","77626","61934","62007","72260","83967","50801", 
				"13889", "79876", "61817","62046","84881"});
		
		
		compartmentKeyIDsInclude.put("18", new String[] { "60909","12514", "12515",
				"12513", "50863", "50865", "50866", "50867", "50868", "50869",
				"50871", "12513","5865","20291" ,"58067","80284","58898","77624", "7193"});
		
		compartmentKeyIDsInclude.put("19", new String[] { "46897","22540","22532","22538","22539","22428","22441","7184", 
				 "9665","24222","24974","51608","32838","32875","75030",
				"35175","51537","33814","20234","33726","24491","25046","51607","33135","33112","51061","25245","32853"});
		compartmentKeyIDsInclude.put("20", new String[] { "9578", "9579","19086","16588", "17269","16585","72075","50226",
				"16902","42840","42847","42832","24964","12526","12527"
				,"45643", "20367", "20374","75130","18252","20462","42855"});
		compartmentKeyIDsInclude.put("21", new String[] { "9628","9577", "7144",
				"9620", "9921","19930","15080" ,"83805","16081"});
		compartmentKeyIDsInclude.put("22", new String[] { "9576",
				"7543", "7574", "7591", "7595", "7956", "9139", "9140", "9149",
				"9601","77169","38946","39178","39169" });
		compartmentKeyIDsInclude.put("23", new String[] { "7155", "7183",
				"9617", "9666", "9915", "54253" ,"71296","23889","33748","33770","37372","35287",
				"281371","32830","32825","37019","25524","38863","9621",
				"33792","33793","23400","240707","33789","83804","65024","65022","13668","34354","35246","63543","35285"});

		compartmentKeyIDsInclude.put("24", new String[] { "61844","61874","52801", "7154",
				"9613", "71223", "53669", "54237", "54238", "54239", "54241",
				"54360", "46565", "24218","75306","75438","264717","9616","59110","54964","59370",
				"52989","54368","49149","53128","264779","9711","52856","52872","75770","54450","54449","54448","52966","59351","59350","46479","52737"
				,"52781","59816","60027","59815"});

		
		
		compartmentKeyIDsExclude.put("1", new String[] { "255907", "256035",
				"11338", "63207", "63379", "255891", "256023", "67609",
				"256019", "62944", "255895" });
		compartmentKeyIDsExclude.put("2", new String[] { "255907", "256045",
				"255905", "63207", "255903", "63379", "256041", "256023",
				"255897", "255895", "62934", "263094", "263108", "67609",
				"256019", "255966", "256035", "256033", "256031", "62942",
				"255913", "62941", "255917", "62944", "269063", "261319",
				"263050", "62936", "63243", "62937", "255891", "63418",
				"256025" });
		compartmentKeyIDsExclude.put("3",
				new String[] { "256043", "255907", "256045", "255905", "62836",
						"255903", "63379", "255897", "263094", "62934",
						"263108", "67609", "256019", "255966", "256035",
						"256033", "256031", "62942", "62941", "255915",
						"255917", "62944", "269063", "62938", "261319",
						"263050", "62936", "63243", "62937", "21542", "255891",
						"62837", "256025" });
		compartmentKeyIDsExclude.put("4", new String[] { "256043", "62836",
				"63207", "256023", "256041", "255913", "255897", "255915",
				"255895", "62934", "62938", "255891", "67609", "256019",
				"63418", "62837", "256025" });
		compartmentKeyIDsExclude.put("5", new String[] { "62836", "21542",
				"62837" });
		compartmentKeyIDsExclude.put("6", new String[] {"74747", "61020", "61021","53643","53644","61734","52781","55605","54448","52781", "60909",
				"59815","60027","24779","24778","24765","24767","53658","59354","59396","59397","46818","46817","46827","46826","57804","225806","225808"});
		
		compartmentKeyIDsExclude.put("7", new String[] { "19673", "20218",
				"16563","20367", "20223","9598","18252","20462","20369","20372",
				"20380","20381","20374","20019","20171","20417","20011","20012","20168","20170","45649","27886"});
		
		
		compartmentKeyIDsExclude.put("12", new String[] { "6657", "6658",
				"6656", "6659", "68423", "68424", "68427", "68426", "68429","6654",
				"68435", "68434", "68431", "68432","67993","67993" });
		compartmentKeyIDsExclude.put("13", new String[] { "19681", "19675" });
		compartmentKeyIDsExclude.put("18", new String[] { "260119","54448","59027","59025","53082", "260127",
				"53083", "58737", "58736", "260124","61063", "52781","56513","59026"});
		
		
		
		compartmentKeyIDsExclude.put("19", new String[] { "16581", "72062",
				"16582", "57861", "16583", "30636", "61404", "20645","42855" ,"35178","45282","45284","45283"});
		compartmentKeyIDsExclude.put("20", new String[] { "68013","9600", "14548","29335",
				"14544", "19387", "19823", "19388", "19824", "19327", "9577",
				"15900", "17558", "55424", "18484", "55423", "18483", "15887",
				"15886", "19326", "27974", "7213", "19236", "7214", "19235","9707","45654","9909",
				"7210","7211","7212","33185","33186","33187","19645","18087"
				,"19970","45642","27648","19643","19642","19638","74318","74317","20005","19669","19674"});
		compartmentKeyIDsExclude.put("21", new String[] { "16580", "13295",
				"16563", "16564", "9578", "9738", "9397", "58287", "57861",
				"58286", "74754", "259284", "259282", "10430", "259278","9739",
				"61404", "20645","7144" ,"19820","19819","19818","19817","19810","19809","77061","77064","259288","16516"});
		compartmentKeyIDsExclude.put("22", new String[] { "9583", "9690", "14074","6657", "6658",
				"6656", "6659", "26572", "13376", "13373", "68423", "13399",
				"13375", "85055", "13374", "13398", "85056", "52529", "68424",
				"68427", "52527", "7088", "68426", "68429", "33644", "33645", "13282","13082","13341",
				"7396", "7395", "9396", "6629", "255862", "13323", "45662",
				"13322", "7463", "7464", "52530", "52532", "52531", "61768",
				"13411", "52537", "13412", "9582", "68435", "52534", "52533",
				"68434", "52536", "68431", "68432","9734","31303","31302","50180","50179","87544","87545","87546","87547","87548","87549"});
		
		
		
		
		compartmentKeyIDsExclude.put("23", new String[] { "68013","9395", "86885",
				"62494", "86884", "86883", "27888", "27887", "74245", "84583",
				"26728", "14067", "83112","46619","46562" });
		compartmentKeyIDsExclude.put("24", new String[] { "58734","12513", "59015","20291","50801", "56093",
				"56092", "59658", "24777", "24776", "74746", "54450", "24775",
				"59654", "54665", "59992", "52853", "56002", "55718", "52891",
				"55719", "72969", "9710", "12514", "72973", "72974", "9609",
				"72970", "270001", "12515", "53647", "52780", "53648", "53642",
				"54738", "54737", "53641", "57691", "46472", "57690", "54645",
				"57466", "61713", "57467", "61714", "54449", "53655", "53656",
				"61715", "86883", "54448", "269999", "46688", "24763", "60055",
				"60054", "79876", "67944", "59857", "67325","49184",
				"60909",  "56513","46490","49184","56513", "60909","61063","54397","54398","56513",
				"56514","56515","260392","52750","59790","61064","61065","59101", "59102","59103",
				"59656","59836","59502","46489","59515","46726","59655","52745","52748","54396","49026",
				"54833","54834","52897","57716","57717","57418","57419","54708","54707","59012","59014","73758"
				,"59837","59114","59115","59213","59211","74747"});
		
		 
	}

	private static void getTileContents(String tileID) {

		String[] includeIDs = compartmentKeyIDsInclude.get(tileID);
		for (int i = 0; i < includeIDs.length; i++) {
			initialFMAIDsToIncludeInTile.add(includeIDs[i]);
		}

		String[] excludeIDs = compartmentKeyIDsExclude.get(tileID);
		if (excludeIDs != null) {
			for (int i = 0; i < excludeIDs.length; i++) {
				exludeFMAIDsFromTile.add(excludeIDs[i]);
			}
		}

	}

	public static void getDistalMicrocirculationConnections(String fmaTerm) {

		try {
			HashSet<String> requestElements = new HashSet<String>();
			Statement st = DBConnections.getApinatomyConnection()
					.createStatement();
			String statement = "select regional_part_id, constitutional_part_id, child_structure_id "
					+ "from ((structures left join constitutional_parts on structures.fma_id = constitutional_parts.main_structure_id) "
					+ "left join regional_parts on structures.fma_id = regional_parts.main_structure_id) "
					+ "left join structure_subclass on structures.fma_id = structure_subclass.main_structure_id "
					+ "where fma_id=\"" + fmaTerm + "\"";

			ResultSet rs1 = st.executeQuery(statement);

			while (rs1.next()) {

				String regional = rs1.getString("regional_part_id");
				String constitutional = rs1.getString("constitutional_part_id");
				String direct = rs1.getString("child_structure_id");

				if (!exludeFMAIDsFromTile.contains(regional)
						&& regional != null
						&& !Vocabulary.getDescription(regional).toString()
								.contains("artery")
						&& !Vocabulary.getDescription(regional).toString()
								.contains("vein")) {
					if (!processedIDs.contains(regional))
						requestElements.add(regional);
					finalListFMAIDsToIncludeInTile.add(regional);
				}

				if (!exludeFMAIDsFromTile.contains(constitutional)
						&& constitutional != null
						&& !Vocabulary.getDescription(constitutional)
								.toString().contains("artery")
						&& !Vocabulary.getDescription(constitutional)
								.toString().contains("vein")) {
					if (!processedIDs.contains(constitutional))
						requestElements.add(constitutional);
					finalListFMAIDsToIncludeInTile.add(constitutional);
				}

				if (!exludeFMAIDsFromTile.contains(direct)
						&& direct != null
						&& !Vocabulary.getDescription(direct).toString()
								.contains("artery")
						&& !Vocabulary.getDescription(direct).toString()
								.contains("vein")) {
					if (!processedIDs.contains(direct))
						requestElements.add(direct);
					finalListFMAIDsToIncludeInTile.add(direct);
				}
			}
			rs1.close();
			st.close();

			Iterator<String> it = requestElements.iterator();
			while (it.hasNext()) {
				String id = it.next();
				processedIDs.add(id);
				getDistalMicrocirculationConnections(id);
			}
		} catch (SQLException ex) {
			ex.printStackTrace();
			System.exit(1);
		}
	}

	private static void clearSets() {
		initialFMAIDsToIncludeInTile.clear();
		finalListFMAIDsToIncludeInTile.clear();
		exludeFMAIDsFromTile.clear();
		processedIDs.clear();
	}

	private static void insertCompleteTileValues() {
		try {
			String tileArray[][];

			Iterator<String> compartmentIterator = compartmentKeyIDsInclude
					.keySet().iterator();

			while (compartmentIterator.hasNext()) {
				clearSets();
				String compartmentIdentifier = compartmentIterator.next();
				getTileContents(compartmentIdentifier);

				Iterator<String> it = initialFMAIDsToIncludeInTile.iterator();
				while (it.hasNext()) {
					getDistalMicrocirculationConnections(it.next());
				}

				finalListFMAIDsToIncludeInTile
						.addAll(initialFMAIDsToIncludeInTile);
				tileArray = new String[finalListFMAIDsToIncludeInTile.size()][2];
				int j = 0;
				it = finalListFMAIDsToIncludeInTile.iterator();
				while (it.hasNext()) {
					tileArray[j][0] = compartmentIdentifier;
					tileArray[j][1] = it.next();
					j++;
				}
				InsertSQLQuery.insert2DArrayInTable(
						"body_compartment_contents", tileArray);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void main() {
		insertcompartmentDefinitionsInDB();
		populateCompartmentMap();
		insertCompleteTileValues();
	}
}
