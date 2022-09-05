package org.apinatomy.knowledge.management.fma.createdb.structuralanatomy;

import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;

import org.apinatomy.knowledge.management.fma.createdb.sql.DBConnections;
import org.apinatomy.knowledge.management.fma.createdb.sql.InsertSQLQuery;
import org.apinatomy.knowledge.management.fma.createdb.utilities.Vocabulary;


public class StructuralPartonomy {
 

  private static String[][] convertMapToArray( HashMap<String, HashSet<String>> map) {

    String[][] partsArray;
    ArrayList<String[]> arrayList = new ArrayList<String[]>();
    Iterator<String> it = map.keySet().iterator();

    while (it.hasNext()){
      String key = it.next();
      HashSet<String> values = map.get(key);
      Iterator<String> valueIterator = values.iterator();
      while (valueIterator.hasNext()){
        arrayList.add(new String[] {key, valueIterator.next()});
      }
    }

    partsArray = new String[arrayList.size()][2];

    for (int i=0;i<arrayList.size();i++){
      String values[] = arrayList.get(i);
      partsArray[i][0]=values[0];
      partsArray[i][1]=values[1];
    }     
    arrayList.clear();
    return partsArray;
  }


    public static void populatePartonomyTables(){
      HashMap<String,HashSet<String>> constitutionalParts = new HashMap<String,HashSet<String>>();
      HashMap<String,HashSet<String>> regionalParts = new HashMap<String,HashSet<String>>();
      HashMap<String,HashSet<String>> subclass = new HashMap<String,HashSet<String>>();
        
        try{
          String query = "select frame, slot, short_value from fma where slot='constitutional part' or slot = 'constitutional part of' or slot='regional part' or slot = 'regional part of' or slot = ':DIRECT-INSTANCES' or slot = ':DIRECT-SUBCLASSES' or slot = ':DIRECT-SUPERCLASSES'";
          Statement st = DBConnections.getFMAConnection().createStatement();
          ResultSet rs = st.executeQuery(query);

          while (rs.next()) {
            String frame = rs.getString("frame");
            String slot = rs.getString("slot");
            String short_value = rs.getString("short_value");

            if (Vocabulary.containsFMATerm(frame) && Vocabulary.containsFMATerm(short_value)) {
             
              String frameID = Vocabulary.getFMAID(frame);
              String shortValueID = Vocabulary.getFMAID(short_value);
              HashSet<String> parts;

              if (slot.equals("constitutional part")){
                if (constitutionalParts.containsKey(frameID)){
                  parts = constitutionalParts.get(frameID);
                  parts.add(shortValueID);
                }

                else {
                  parts = new HashSet<String>();
                  parts.add(shortValueID);
                }
                constitutionalParts.put(frameID, parts);
              }
              else if (slot.equals("constitutional part of")){
                if (constitutionalParts.containsKey(shortValueID)){
                  parts = constitutionalParts.get(shortValueID);
                  parts.add(frameID);
                }
                else {
                  parts = new HashSet<String>();
                  parts.add(frameID);
                }                
                constitutionalParts.put(shortValueID, parts);
              }

              else if (slot.equals("regional part")){
                if (regionalParts.containsKey(frameID)){
                  parts = regionalParts.get(frameID);
                  parts.add(shortValueID);
                }
                else {
                  parts = new HashSet<String>();
                  parts.add(shortValueID);
                }
                regionalParts.put(frameID, parts);
              }

              else if (slot.equals("regional part of")){
                if (regionalParts.containsKey(shortValueID)){
                  parts = regionalParts.get(shortValueID);
                  parts.add(frameID);
                }
                else {
                  parts = new HashSet<String>();
                  parts.add(frameID);
                }                
                regionalParts.put(shortValueID, parts);
              }
              
              
              else if (slot.equals(":DIRECT-INSTANCES")||slot.equals(":DIRECT-SUBCLASSES")){
                if (subclass.containsKey(frameID)){
                  parts = subclass.get(frameID);
                  parts.add(shortValueID);
                }
                else {
                  parts = new HashSet<String>();
                  parts.add(shortValueID);
                }
                subclass.put(frameID, parts);
              }        

              

              else if (slot.equals(":DIRECT-SUPERCLASSES")){
                if (subclass.containsKey(shortValueID)){
                  parts = subclass.get(shortValueID);
                  parts.add(frameID);
                }
                else {
                  parts = new HashSet<String>();
                  parts.add(frameID);
                }                
                subclass.put(shortValueID, parts);
              }
            }
          }
          rs.close();
          st.close();
          
          InsertSQLQuery.insert2DArrayInTable("constitutional_parts", convertMapToArray(constitutionalParts));
          InsertSQLQuery.insert2DArrayInTable("regional_parts", convertMapToArray(regionalParts));
          InsertSQLQuery.insert2DArrayInTable("structure_subclass", convertMapToArray(subclass));
          constitutionalParts.clear();
          regionalParts.clear();
          subclass.clear();
          
        }

        catch (Exception e){
          e.printStackTrace();
        }

      }
} 