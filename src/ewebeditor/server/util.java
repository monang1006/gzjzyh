package ewebeditor.server;

import java.util.*;
import java.util.regex.*;
import java.text.*;
import java.io.*;
import java.security.*;


public class util
{
		
	public util(){


	}


	public String[] split(String source, String div) {
		int arynum = 0, intIdx = 0, intIdex = 0, div_length = div.length();
		if (source.compareTo("") != 0) {
		  if (source.indexOf(div) != -1) {
			intIdx = source.indexOf(div);
			for (int intCount = 1; ; intCount++) {
			  if (source.indexOf(div, intIdx + div_length) != -1) {
				intIdx = source.indexOf(div, intIdx + div_length);
				arynum = intCount;
			  }
			  else {
				arynum += 2;
				break;
			  }
			}
		  }
		  else {
			arynum = 1;
		  }
		}
		else {
		  arynum = 0;

		}
		intIdx = 0;
		intIdex = 0;
		String[] returnStr = new String[arynum];

		if (source.compareTo("") != 0) {
		  if (source.indexOf(div) != -1) {
			intIdx = (int) source.indexOf(div);
			returnStr[0] = (String) source.substring(0, intIdx);
			for (int intCount = 1; ; intCount++) {
			  if (source.indexOf(div, intIdx + div_length) != -1) {
				intIdex = (int) source.indexOf(div, intIdx + div_length);
				returnStr[intCount] = (String) source.substring(intIdx + div_length,
					intIdex);
				intIdx = (int) source.indexOf(div, intIdx + div_length);
			  }
			  else {
				returnStr[intCount] = (String) source.substring(intIdx + div_length,
					source.length());
				break;
			  }
			}
		  }
		  else {
			returnStr[0] = (String) source.substring(0, source.length());
			return returnStr;
		  }
		}
		else {
		  return returnStr;
		}
		return returnStr;
	}

	public String dealNull(String str) {
		String returnstr = null;
		if (str == null) {
			returnstr = "";
		} else {
			returnstr = str;
		}
		return returnstr;
	}

	public Object dealNull(Object obj) {
		Object returnstr = null;
		if (obj == null){
			returnstr = (Object) ("");
		}else{
			returnstr = obj;
		}
		return returnstr;
	}

	public String replace(String str, String substr, String restr) {
		String[] tmp = split(str, substr);
		String returnstr = null;
		if (tmp.length != 0) {
			returnstr = tmp[0];
			for (int i = 0; i < tmp.length - 1; i++) {
				returnstr = dealNull(returnstr) + restr + tmp[i + 1];
			}
		}
		return dealNull(returnstr);
	}

	public String getConfigString(String s_Key, String s_Config){
		String s_Result = "";
		Pattern p = Pattern.compile("//" + s_Key + " = \"(.*)\"");
		Matcher m = p.matcher(s_Config);
		while (m.find()) {
			s_Result = m.group(1);
		}
		return s_Result;
	}

	public ArrayList getConfigArray(String s_Key, String s_Config){
		ArrayList a_Result = new ArrayList();
		Pattern p = Pattern.compile("//" + s_Key + " = \"(.*)\"");
		Matcher m = p.matcher(s_Config);
		while (m.find()) {
			a_Result.add(m.group(1));
		}
		return a_Result;
	}

	public String getConfigFileRealPath(String s_ServletPath){
		return getRealPathFromRelative(s_ServletPath, "jsp/config.jsp");
	}

	public String getRealPathFromRelative(String s_ServletPath, String s_RelativePath){
		String s_FileSeparator = System.getProperty("file.separator");
		
		String s_Url = s_RelativePath;
		if (s_Url.substring(0, 1).equals("/")){
			s_Url = replace(s_Url, "/", s_FileSeparator);
			return getSiteRootRealPath()+s_Url.substring(1,s_Url.length());
		}

		String s_EditorRoot = getEditorRootRealPath(s_ServletPath);
		String s_PrePath = s_EditorRoot.substring(0, s_EditorRoot.length()-1);

		while(s_Url.startsWith("../")){
			s_Url = s_Url.substring(3);
			s_PrePath = s_PrePath.substring(0, s_PrePath.lastIndexOf(s_FileSeparator));
		}
		
		return s_PrePath + s_FileSeparator + s_Url;
	}

	public String getEditorRootRealPath(String s_ServletPath){
		String s_FileSeparator = System.getProperty("file.separator");
		
		String s_Path = s_ServletPath;
		s_Path = s_Path.substring(0, s_Path.lastIndexOf("/"));
		s_Path = s_Path.substring(0, s_Path.lastIndexOf("/"));
		
		if (!s_Path.equals("")){
			s_Path = replace(s_Path, "/", s_FileSeparator);
			s_Path += s_FileSeparator;
			s_Path = s_Path.substring(1, s_Path.length());
		}
		
		s_Path = getSiteRootRealPath()+s_Path;

		return s_Path;
	}

	public String getSiteRootRealPath(){
		String s_ClassPath = this.getClass().getResource("").getPath();
		String s_FileSeparator = System.getProperty("file.separator");
		int n_Pos = s_ClassPath.lastIndexOf("WEB-INF");
		s_ClassPath = s_ClassPath.substring(0,n_Pos);
		if (s_FileSeparator.equals("\\")){
			if (s_ClassPath.startsWith("/")){
				s_ClassPath=s_ClassPath.substring(1,s_ClassPath.length());
			}
			s_ClassPath = replace(s_ClassPath, "/", s_FileSeparator);
		}

		return s_ClassPath;
	}


	public String ReadFile(String s_FileName){
		String s_Result = "";
		try {
			File objFile = new File(s_FileName);
			char[] chrBuffer = new char[10];
			int intLength;
			if(objFile.exists()){
				FileReader objFileReader = new FileReader(objFile);
				while((intLength=objFileReader.read(chrBuffer))!=-1){
					s_Result += String.valueOf(chrBuffer,0,intLength);
				}
				objFileReader.close();
			}
		} catch(IOException e) {
			System.out.println(e.getMessage());
		}
		return s_Result;
	}


	public String formatDate(Date myDate, int nFlag) {
		String strFormat = "";
		switch(nFlag){
		case 1:
			strFormat = "yyyy";
			break;
		case 2:
			strFormat = "yyyyMM";
			break;
		case 3:
			strFormat = "yyyyMMdd";
			break;
		case 4:
			strFormat = "yyyyMMddHHmmss";
			break;
		case 5:
			strFormat = "yyyy-MM-dd";
			break;
		}
		SimpleDateFormat formatter = new SimpleDateFormat(strFormat);
		String strDate = formatter.format(myDate);
		return strDate;
	}

	public String ReplaceTime(Date s_Time, String s_Patt){
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(s_Time);
		String y2 = String.valueOf(calendar.get(Calendar.YEAR));
		String y1 = y2.substring(2);
		String m1 = String.valueOf(calendar.get(Calendar.MONTH)+1);
		String m2 = m1;
		if (m2.length()==1){
			m2="0"+m2;
		}
		String d1 = String.valueOf(calendar.get(Calendar.DATE));
		String d2 = d1;
		if (d2.length()==1){
			d2="0"+d2;
		}
		String h1 = String.valueOf(calendar.get(Calendar.HOUR));
		String h2 = h1;
		if (h2.length()==1){
			h2="0"+h2;
		}
		String i1 = String.valueOf(calendar.get(Calendar.MINUTE));
		String i2 = i1;
		if (i2.length()==1){
			i2="0"+i2;
		}
		String s1 = String.valueOf(calendar.get(Calendar.SECOND));
		String s2 = s1;
		if (s2.length()==1){
			s2="0"+s2;
		}

		String ret = s_Patt;
		ret = replace(ret, "{yyyy}", y2);
		ret = replace(ret, "{yy}", y1);
		ret = replace(ret, "{mm}", m2);
		ret = replace(ret, "{m}", m1);
		ret = replace(ret, "{dd}", d2);
		ret = replace(ret, "{d}", d1);
		ret = replace(ret, "{hh}", h2);
		ret = replace(ret, "{h}", h1);
		ret = replace(ret, "{ii}", i2);
		ret = replace(ret, "{i}", i1);
		ret = replace(ret, "{ss}", s2);
		ret = replace(ret, "{s}", s1);

		return ret;
	}

	public String MD5(String plainText, int len) { 
		try { 
			MessageDigest md = MessageDigest.getInstance("MD5"); 
			md.update(plainText.getBytes()); 
			byte b[] = md.digest(); 

			int i; 

			StringBuffer buf = new StringBuffer(""); 
			for (int offset = 0; offset < b.length; offset++) { 
				i = b[offset]; 
				if(i<0) i+= 256; 
				if(i<16) buf.append("0"); 
				buf.append(Integer.toHexString(i)); 
			} 
			
			if (len==16){
				return buf.toString().substring(8,24);
			}else{
				return buf.toString();
			}

		} catch (NoSuchAlgorithmException e) { 
			// TODO Auto-generated catch block 
			e.printStackTrace(); 
		}
		return "";
	} 

	public boolean CheckLicense(String s_Domain, String s_License){
		return true;
		/*s_Domain=s_Domain.toLowerCase();
		if (s_Domain.equals("127.0.0.1") || s_Domain.equals("localhost")) {
			return true;
		}
		
		if (s_License.equals("")){
			return true;
		}

		String ret="";
		String[] aa = split(s_License, ";");
		for (int i=0; i<aa.length; i++) {
			String[] a = split(aa[i], ":");
			if (a.length==8){
				if (a[7].length()==32){				
					
					if (a[0].equals("3")){
						if (s_Domain.equals(a[6])){
							return true;
						}else if (s_Domain.length()>a[6].length()){
							if (s_Domain.substring(s_Domain.length()-a[6].length()-1).equals("."+a[6])){
								return true;
							}
						}
					}else{
						if ((s_Domain.equals(a[6])) || (s_Domain.equals("www."+a[6]))){
							return true;
						}
					}

				}
			}
		}
		return true;*/
	}

	public boolean IsInt(String str){
		if (str.equals("")){
			return false;
		}

		Pattern p = Pattern.compile("[^0-9]+");
		Matcher m = p.matcher(str);
		if (m.matches()){
			return false;
		}

		return true;
	}

	public boolean IsOkSParams(String s_SParams, String s_EncryptKey){
		if (s_SParams.equals("")) {return false;}

		int n = s_SParams.indexOf("|");
		if (n < 0){return false;}
		
		String s1 = s_SParams.substring(0, n);
		String s2 = s_SParams.substring(n + 1);

		if (!MD5(s_EncryptKey + s2, 16).equals(s1)) {return false;}

		return true;
	}

}
