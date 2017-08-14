package com.strongit.uums.license;   

import java.io.BufferedReader;   
import java.io.InputStream;   
import java.io.InputStreamReader;   
  
public class MacReader {   
  
    public static String physicalAddress = "read MAC error!";   
  
    public MacReader() {   
    }   
       
    public static boolean isValid() {   
        try {   
            return getSpecialMacFromFile().equals(checkPhysicalAddress());   
        } catch(Exception ex) {   
            ex.printStackTrace();   
            return false;   
        }   
    }   
       
    public static String getSpecialMacFromFile() throws Exception {   
        String resource = "com/strongit/uums/license";   
        InputStream in = MacReader.class.getClassLoader().getResourceAsStream(resource);   
        if(in == null) {   
            in = ClassLoader.getSystemResourceAsStream(resource);   
        }   
        InputStreamReader fr = new InputStreamReader(in);   
        BufferedReader br = new BufferedReader(fr);   
        return br.readLine();   
    }   
  
    public static String checkPhysicalAddress() {   
        try {   
            String line;   
            Process process = Runtime.getRuntime().exec("c:/windows/system32/cmd.exe ipconfig all");   
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(process.getInputStream()));   
            while ((line = bufferedReader.readLine()) != null) {   
                if (line.indexOf("Physical Address. . . . . . . . . :") != -1) {   
                    if (line.indexOf(":") != -1) {   
                        physicalAddress = line.substring(line.indexOf(":") + 2);   
                        break; // 找到MAC,推出循环   
                    }   
                }   
            }   
            process.waitFor();   
        } catch (Exception e) {   
            e.printStackTrace();   
        }   
        System.out.println(physicalAddress);
        return physicalAddress;   
    }   
  
    public static void main(String[] args) throws Exception {   
        System.out.println(isValid());   
    }   
}  
