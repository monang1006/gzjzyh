package com.strongit.uums.license;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MacUtil {
	/**
	 * Return Opertaion System Name;
	 * 
	 * @return os name.
	 */
	public static String getOsName() {
		String os = "";
		os = System.getProperty("os.name");
		return os;
	}

	/**
	 * Returns the MAC address of the computer.
	 * 
	 * @return the MAC address
	 */
	public static String getMACAddress() {
		String address = "";
		String os = getOsName();
		StringBuffer sb = new StringBuffer();
		if (os.startsWith("Windows")) {
			try {
				String command = "C:/WINDOWS/system32/cmd.exe /c ipconfig /all";
				//Process p = Runtime.getRuntime().exec(command);
				Process p = null;
				try{
					p = new ProcessBuilder("ipconfig","/all").start();
				}catch(Exception e){
					
				}
				BufferedReader br = new BufferedReader(new InputStreamReader(p.getInputStream()));
				String line;
				
				while ((line = br.readLine()) != null) {
					if (!"".equals(line)) {
						sb.append(line);
					}

					// if (line.indexOf("Physical Address") > 0) {
					// int index = line.indexOf(":");
					// index += 2;
					// address = line.substring(index);
					// break;
					// }
				}
				System.out.println(sb.toString());
				Collection<String> adds= findMacAddress(sb.toString());
				address=collection2str(adds,',');
				br.close();
				return address.trim();
			} catch (IOException e) {
			}
		} else if (os.startsWith("Linux")) {
			String command = "/bin/sh -c ifconfig -a";
			Process p;
			try {
//				p = Runtime.getRuntime().exec(command);
//				BufferedReader br = new BufferedReader(new InputStreamReader(p.getInputStream()));
//				String line;
//				while ((line = br.readLine()) != null) {
//					if (!"".equals(line)) {
//						sb.append(line);
//					}
////					if (line.indexOf("HWaddr") > 0) {
////						int index = line.indexOf("HWaddr") + "HWaddr".length();
////						address = line.substring(index);
////						break;
////					}
//				}
//				Collection<String> adds= findMacAddress(sb.toString());
//				address=collection2str(adds,',');
//				br.close();
		        BufferedReader bufferedReader = null;   
		        Process process = null;   
		        try {   
		            process = Runtime.getRuntime().exec("ifconfig eth0");// linux下的命令，一般取eth0作为本地主网卡 显示信息中包含有mac地址信息   
		            bufferedReader = new BufferedReader(new InputStreamReader(process.getInputStream()));   
		            String line = null;   
		            int index = -1;   
		            while ((line = bufferedReader.readLine()) != null) {   
		                index = line.toLowerCase().indexOf("hwaddr");// 寻找标示字符串[hwaddr]   
		                if (index >= 0) {// 找到了   
		                    address = line.substring(index +"hwaddr".length()+ 1).trim();//  取出mac地址并去除2边空格   
		                    break;   
		                }   
		            }   
		        } catch (IOException e) {   
		            e.printStackTrace();   
		        } finally {   
		            try {   
		                if (bufferedReader != null) {   
		                    bufferedReader.close();   
		                }   
		            } catch (IOException e1) {   
		                e1.printStackTrace();   
		            }   
		            bufferedReader = null;   
		            process = null;   
		        }   

			} catch (Exception e) {
			}
		}
		address = address.trim();
		return address;
	}

	private static String collection2str(Collection<String> array, char c) {
		StringBuffer sb=new StringBuffer();
		for(String s:array){
			if(!"".equals(s)){
				sb.append(s);
				sb.append(c);
			}
		}
		sb.deleteCharAt(sb.length()-1);
		return sb.toString();
	}

	private static Collection<String> findMacAddress(String str) {
		Pattern macPattern = Pattern.compile("([0-9A-Fa-f]{2})(-[0-9A-Fa-f]{2}){5}");
		Matcher macMatcher;
		macMatcher = macPattern.matcher(str.toString());
		Collection<String> li = new ArrayList<String>();
		while (macMatcher.find()) {
			li.add(macMatcher.group());
		}
		return li;
	}

	/**
	 * Main Class.
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		System.out.println("Operation System=" + getOsName());
		System.out.println("Mac Address=" + getMACAddress());
	}
}
