
package com.strongit.oa.util;

public class FiltrateContent {
	
	public static String getNewContent(String value){
		if(value!=null){
	        value = value.replaceAll("\\Q--\\E","");
	        value = value.replaceAll("\\Q\"\\E","“");
	        value = value.replaceAll("\\Q%\\E","");
	        value = value.replaceAll("\\Q'\\E","‘");
	        value = value.replaceAll("\\Q<\\E","");
	        value = value.replaceAll("\\Q>\\E","");
	        value = value.replaceAll("\\Q&lt\\E","");
	        value = value.replaceAll("\\Q&gt\\E","");
	        value = value.replaceAll("\\Q;\\E","；");
	        value = value.replaceAll("\\Q{\\E","｛");
	        value = value.replaceAll("\\Q}\\E","｝");
	        value = value.replaceAll("\\Q=\\E","＝");
	        value = value.replaceAll("\\Q/\\E","");
	        value = value.replaceAll("\\Q(\\E","（");
	        value = value.replaceAll("\\Q)\\E","）");
	        value = value.replaceAll("\\Qbegin@begin\\E","");
	        value = value.replaceAll("\\Qend@end\\E","");
	        String values = value.toUpperCase(); 
	        
	        while(values.indexOf("OR")>0 ){
	        	value = (value.substring(0,values.indexOf("OR"))+value.substring(values.indexOf("OR")+2));
	        	values = (values.substring(0,values.indexOf("OR"))+values.substring(values.indexOf("OR")+2));	
	        }
	        while(values.indexOf("BY")>0 ){
	        	value = (value.substring(0,values.indexOf("BY"))+value.substring(values.indexOf("BY")+2));
	        	values = (values.substring(0,values.indexOf("BY"))+values.substring(values.indexOf("BY")+2));	
	        }
	        while(values.indexOf("AND")>0 ){
	        	value = (value.substring(0,values.indexOf("AND"))+value.substring(values.indexOf("AND")+3));
	        	values = (values.substring(0,values.indexOf("AND"))+values.substring(values.indexOf("AND")+3));
	        }
	        while(values.indexOf("SCRIPT")>0 ){
	        	value = (value.substring(0,values.indexOf("SCRIPT"))+value.substring(values.indexOf("SCRIPT")+6));
	        	values = (values.substring(0,values.indexOf("SCRIPT"))+values.substring(values.indexOf("SCRIPT")+6));
	        }
	        while(values.indexOf("INSERT")>0 ){
	        	value = (value.substring(0,values.indexOf("INSERT"))+value.substring(values.indexOf("INSERT")+6));
	        	values = (values.substring(0,values.indexOf("INSERT"))+values.substring(values.indexOf("INSERT")+6));
	        }
	        while(values.indexOf("SELECT")>0 ){
	        	value = (value.substring(0,values.indexOf("SELECT"))+value.substring(values.indexOf("SELECT")+6));
	        	values = (values.substring(0,values.indexOf("SELECT"))+values.substring(values.indexOf("SELECT")+6));
	        }
	        while(values.indexOf("UPDATE")>0 ){
	        	value = (value.substring(0,values.indexOf("UPDATE"))+value.substring(values.indexOf("UPDATE")+6));
	        	values = (values.substring(0,values.indexOf("UPDATE"))+values.substring(values.indexOf("UPDATE")+6));
	        }
	        while(values.indexOf("DELETE")>0 ){
	        	value = (value.substring(0,values.indexOf("DELETE"))+value.substring(values.indexOf("DELETE")+6));
	        	values = (values.substring(0,values.indexOf("DELETE"))+values.substring(values.indexOf("DELETE")+6));
	        }
	        while(values.indexOf("UNTIL")>0 ){
	        	value = (value.substring(0,values.indexOf("UNTIL"))+value.substring(values.indexOf("UNTIL")+5));
	        	values = (values.substring(0,values.indexOf("UNTIL"))+values.substring(values.indexOf("UNTIL")+5));
	        }
	        
	        }else{
	        	value = "";
	        }
		return value;
	}
}
