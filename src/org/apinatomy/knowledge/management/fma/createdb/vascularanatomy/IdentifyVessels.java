package org.apinatomy.knowledge.management.fma.createdb.vascularanatomy;

import java.util.HashSet;
import java.util.Iterator;

import org.apinatomy.knowledge.management.fma.createdb.utilities.Vocabulary;

public class IdentifyVessels {

	
	 public static HashSet<String> getSetofArteriesFromFMA() {
		HashSet<String> setOfFMAArteries = new HashSet<String>(); 

		Iterator<String> iterator = Vocabulary.getAllVocabulary().keySet().iterator();
		while (iterator.hasNext()){
			String text = iterator.next();
			
			if ((text.contains("rtery") || text.contains("aorta") || text.startsWith("Artery of ") || text.contains("arterial")) &&
					!text.contains("ostium of") && !text.startsWith("Arch of") && !text.equals("Artery") && !text.contains("lamina of") &&
					!text.startsWith("Lumen of") && !text.startsWith("Set of") && !text.startsWith("Subdivision of") && !text.startsWith("Wall of") &&  
					!text.startsWith("arterial supply") && !text.startsWith("Atlantic part of") &&!text.startsWith("Zone of") &&
					!text.startsWith("Basal part of") && !text.contains("ramus of") && !text.startsWith("Basal portion of") &&
					!text.startsWith("Basement membrane") && !text.startsWith("Bifurcation of") &&
					!text.startsWith("Blood in") && !text.startsWith("Bulb of") && !text.contains("part of") && !text.contains("arterial network") &&
					!text.startsWith("Canal for") && !text.startsWith("Cephalic part of") && !text.contains("arterial circle") &&
					!text.contains("Zone") && !text.contains("zone") &&!text.startsWith("Connective tissue") && !text.contains("segment of") &&
					!text.startsWith("Endothelium of") && !text.startsWith("Fibroblast of") && !text.startsWith("Groove") && !text.startsWith("Ostium of") &&
					!text.startsWith("Origin of") && !text.startsWith("Orifice of") && !text.startsWith("Pars ") && !text.startsWith("Position of") &&
					!text.contains("arterial tree") && !text.startsWith("Region of") && !text.startsWith("Segment of") && !text.startsWith("Stem of") &&
					!text.startsWith("Summit of") && !text.startsWith("Tunica ") && !text.startsWith("Visceral serous pericardium of") &&
					!text.contains("l lymph node")) {
				
					
				setOfFMAArteries.add(text);
			}
		}
		return setOfFMAArteries;
	}
	
	 public static HashSet<String> getSetOfVeinsFromFMA() {
			HashSet<String> setOfFMAVeins = new HashSet<String>(); 
			
			Iterator<String> iterator = Vocabulary.getAllVocabulary().keySet().iterator();
			while (iterator.hasNext()){
				String text = iterator.next().toString();
				
				if ((text.contains("vein")  ||  text.contains("vena") || text.startsWith("Vein of ") || text.contains("venous")|| text.contains("oronary sinus")) &&
				!text.contains("venous tree") && !text.startsWith("Hepatovenous s") && !text.contains(" bulb of ") && !text.contains("systemic venous tree") &&
				!text.startsWith("Serous pericardium of" ) && !text.startsWith("Systemic venous system") && !text.startsWith("Tendon of") &&
				!text.startsWith("venous drainage") && !text.contains("ostium of") && !text.startsWith("Arch of") && !text.equals("Artery") && !text.contains("lamina of") &&
				!text.startsWith("Lumen of") && !text.startsWith("Set of") && !text.startsWith("Subdivision of") && !text.startsWith("Wall of") &&
				!text.startsWith("Zone ") && !text.startsWith("arterial supply") && !text.startsWith("Atlantic part of")&&
				!text.startsWith("Basal part of") && !text.contains("ramus of") && !text.startsWith("Basal portion of")&&
				!text.startsWith("Basement membrane") && !text.startsWith("Bifurcation of")&&
				!text.startsWith("Blood in") && !text.startsWith("Bulb of") && !text.contains("part of") && !text.contains("arterial network") &&
				!text.startsWith("Canal for") && !text.startsWith("Cephalic part of") && !text.contains("arterial circle")&&
				!text.contains("zone") && !text.startsWith("Connective tissue") && !text.contains("segment of")&&
				!text.startsWith("Endothelium of") && !text.startsWith("Fibroblast of") && !text.startsWith("Groove") && !text.startsWith("Ostium of") &&
				!text.startsWith("Origin of" ) && !text.startsWith("Orifice of") && !text.startsWith("Pars " ) && !text.startsWith("Position of")&&
				!text.contains("venous tree") && !text.startsWith("Region of") && !text.startsWith("Segment of") && !text.startsWith("Stem of") &&
				!text.startsWith("Summit of") && !text.startsWith("Tunica ") && !text.startsWith("Visceral serous pericardium of") &&
				!text.startsWith("Parietal serous pericardium") && !text.startsWith("Pericardial cavity") &&
				!text.startsWith("Valve of") && !text.startsWith("Ligament of") && !text.startsWith("Fold of") &&
				!text.startsWith("Mesothelium of")) {

					setOfFMAVeins.add(text);
				}
			}
			return setOfFMAVeins;
		}
	 
}
