package org.apinatomy.knowledge.management.fma.createdb.utilities;

public class ConvertCase {
    
	public static String toUpperCase(String s) {
        return (s.length()>0)? Character.toUpperCase(s.charAt(0))+s.substring(1) : s;
    }

    public static String toLowerCase(String s) {
        return (s.length()>0)? Character.toLowerCase(s.charAt(0))+s.substring(1) : s;
    }
    	
}
