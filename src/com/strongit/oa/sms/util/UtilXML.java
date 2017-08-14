package com.strongit.oa.sms.util;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

/*
 * Copyright : Strong Digital Technological Co., LTD.
 * All right reserved.
 * JDK 1.5.0_14;?Struts：2.1.2;Spring 2.5.6;?Hibernate： 3.3.1.GA
 * Create Date: 2012-12-18
 * Autour: luosy
 * Version: V1.0
 * Description： 
 */
public class UtilXML {

//	String path = PathUtil.getRootPath();
//	File f = new File(path+"WEB-INF/classes/phoneconfig.xml");
	File f = null;
	
	public static void main(String[] orgs){
		System.out.println("sdf");
	}
	
	public UtilXML()
	{
		Resource re = new ClassPathResource("phoneconfig.xml");
		try
		{
			f = re.getFile();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}

    /**
     * author:luosy
     * description:
     * modifyer:
     * description:
     * @return
     * @throws DocumentException
     */
    public List findAllConfig() throws DocumentException{
    	  List li=new ArrayList();
    
    	    SAXReader reader = new SAXReader();
    	    Document doc = reader.read(f);
    	    Element foo = doc.getRootElement();
    	    PhoneConfig con=new PhoneConfig();
    	    con.setChinamobile_dbip(foo.element("chinamobile_dbip").getText());
    	    con.setChinamobile_dbusername(foo.element("chinamobile_dbusername").getText());
    	    con.setChinamobile_dbpword(foo.element("chinamobile_dbpword").getText());
    	    con.setChinamobile_masusername(foo.element("chinamobile_masusername").getText());
    	    con.setChinamobile_maspword(foo.element("chinamobile_maspword").getText());
    	    con.setChinamobile_type(foo.element("chinamobile_type").getText());
    	    con.setChinamobile_isopen(foo.element("chinamobile_isopen").getText());
    	    con.setChinamobile_desc(foo.element("chinamobile_desc").getText());
    	    
    	    con.setChinamobile2_dbip(foo.element("chinamobile2_dbip").getText());
    	    con.setChinamobile2_dbusername(foo.element("chinamobile2_dbusername").getText());
    	    con.setChinamobile2_dbpword(foo.element("chinamobile2_dbpword").getText());
    	    con.setChinamobile2_masusername(foo.element("chinamobile2_masusername").getText());
    	    con.setChinamobile2_maspword(foo.element("chinamobile2_maspword").getText());
    	    con.setChinamobile2_type(foo.element("chinamobile2_type").getText());
    	    con.setChinamobile2_isopen(foo.element("chinamobile2_isopen").getText());
    	    con.setChinamobile2_desc(foo.element("chinamobile2_desc").getText());
    	    
    	    con.setGsmmodem_smscomPort(foo.element("gsmmodem_smscomPort").getText());
    	    con.setGsmmodem_smscomBps(foo.element("gsmmodem_smscomBps").getText());
    	    con.setGsmmodem_smsSystemRate(foo.element("gsmmodem_smsSystemRate").getText());
    	    con.setGsmmodem_smscomName(foo.element("gsmmodem_smscomName").getText());
    	    con.setGsmmodem_smscomSimnum(foo.element("gsmmodem_smscomSimnum").getText());
    	    con.setGsmmodem_desc(foo.element("gsmmodem_desc").getText());
    	    con.setGsmmodem_isopen(foo.element("gsmmodem_isopen").getText());
    	    con.setGsmmodem_type(foo.element("gsmmodem_type").getText());
    	    li.add(con);
    	
    	
			return li;
    }
    
    /**
     * author:luosy
     * description: 只要启用了就连接在String
     * modifyer:
     * description:
     * @return
     */
    public String isOpen(){
    	  String s="";
  	    try {  	
  	    	
  	   // 	String path2 =System.getProperty("user.dir")+"\\src\\serverconfig.xml";
  	    SAXReader reader = new SAXReader();
  	    Document doc = reader.read(f);
  	    Element foo = doc.getRootElement();

  	    String[] str=new String[]{"chinamobile","chinamobile2","chinaunicom","chinatelecommunications","gsmmodem"};
  	    for(int i=0;i<str.length;i++){
  	    	String see=foo.element(str[i]+"_isopen").getText();
  	    	if(foo.element(str[i]+"_isopen").getText().equals("true")){
  	    		s+=foo.element(str[i]+"_type").getText()+",";
  	    	}
  	    }
  	    } catch (Exception e) {
  	    e.printStackTrace();
  	    return null;
  	    }
  	    String subs=s.substring(0,s.length()-1);
			return subs;
  }

    /**
     * author:luosy
     * description:启动，停止反复
     * modifyer:
     * description:
     * @param type
     * @param meth
     * @return
     * @throws Exception
     */
    public boolean isOpen(String type,String meth)throws Exception{
    	SAXReader reader = new SAXReader();
    	Document document = reader.read(f);
    	String s=document.getRootElement().element(type+"_isopen").getText();
    	if(document.getRootElement().element(type+"_isopen").getText().equals("false")&&meth.equals("start")){
    	document.getRootElement().element(type+"_isopen").setText("true");
    	writeXML(document,f.getPath());
    	}
    	if(document.getRootElement().element(type+"_isopen").getText().equals("true")&&meth.equals("close")){
    		document.getRootElement().element(type+"_isopen").setText("false");
    		writeXML(document,f.getPath());
    	}
    	//System.out.println(document.getRootElement().element(type+"_isopen").getText());

        
    	return true;
    }
    
    /**
     * author:luosy
     * description:
     * modifyer:
     * description:
     * @param mo
     * @param type
     * @return
     * @throws Exception
     */
    public boolean updateSMSConfig(PhoneConfig mo,String type)throws Exception{
    	SAXReader saxReader = new SAXReader();
    	SAXReader reader = new SAXReader();
    	Document document = reader.read(f);
    	Element foo;
    	List list = document.selectNodes("//sms" );
        Iterator iter=list.iterator();
          while(iter.hasNext()){
        	  foo=(Element)iter.next();
        	  if(foo.element(type+"_type").getText().equals("chinamobile")){
        		  foo.element("chinamobile_dbip").setText(mo.getChinamobile_dbip());
        		  foo.element("chinamobile_dbusername").setText(mo.getChinamobile_dbusername());
        		  foo.element("chinamobile_dbpword").setText(mo.getChinamobile_dbpword());
        		  foo.element("chinamobile_masusername").setText(mo.getChinamobile_masusername());
        		  foo.element("chinamobile_maspword").setText(mo.getChinamobile_maspword());
        	  }else if(foo.element(type+"_type").getText().equals("chinamobile2")){
        		  foo.element("chinamobile2_dbip").setText(mo.getChinamobile2_dbip());
        		  foo.element("chinamobile2_dbusername").setText(mo.getChinamobile2_dbusername());
        		  foo.element("chinamobile2_dbpword").setText(mo.getChinamobile2_dbpword());
        		  foo.element("chinamobile2_masusername").setText(mo.getChinamobile2_masusername());
        		  foo.element("chinamobile2_maspword").setText(mo.getChinamobile2_maspword());
        	  }else if(foo.element(type+"_type").getText().equals("gsmmodem")){
        		  foo.element("gsmmodem_smsSystemRate").setText(mo.getGsmmodem_smsSystemRate());
        		  foo.element("gsmmodem_smscomPort").setText(mo.getGsmmodem_smscomPort());
        		  foo.element("gsmmodem_smscomBps").setText(mo.getGsmmodem_smscomBps());
        		  foo.element("gsmmodem_smscomName").setText(mo.getGsmmodem_smscomName());
        		  foo.element("gsmmodem_smscomSimnum").setText(mo.getGsmmodem_smscomSimnum());
        	  }
         }

        writeXML(document,f.getPath());
    	return true;
    }
    
    /**
     * author:luosy
     * description:
     * modifyer:
     * description:
     * @param document
     * @param path
     * @throws IOException
     */
    public void writeXML(Document document,String path) throws IOException {    
    	  org.dom4j.io.XMLWriter xmlWriter = new org.dom4j.io.XMLWriter(
    	            new FileOutputStream(path));
    	        xmlWriter.write(document);
    	        xmlWriter.close();

    }   
    
}
