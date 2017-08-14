package com.strongit.xxbs.service.impl;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.jws.WebService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.strongit.utils.DateTimes;
import com.strongit.xxbs.entity.TInfoBaseInfoReport;
import com.strongit.xxbs.entity.TInfoBasePublish;
import com.strongit.xxbs.service.HelloWorld;
import com.strongit.xxbs.service.ISubmitService;
import com.strongmvc.orm.hibernate.GenericDAOHibernate;
import com.strongmvc.util.SpringContextUtil;

import org.dom4j.Document; 
import org.dom4j.DocumentHelper; 
import org.dom4j.Element; 
import org.hibernate.SessionFactory;

@Service
@Transactional(readOnly = true)
public class HelloWorldImpl implements HelloWorld {
	private GenericDAOHibernate<TInfoBasePublish, String> publishDao;
	
	@Autowired
	public void setSessionFactory(SessionFactory sessionFactory)
	{
		publishDao = new GenericDAOHibernate<TInfoBasePublish, String>(
				sessionFactory, TInfoBasePublish.class);
	}
	
	public String  getPublishs(String strDate ,String endDate)
	throws Exception{
		ApplicationContext ctx = SpringContextUtil.getApplicationContext();
		ISubmitService submitService = (ISubmitService) ctx.getBean("submitService");
		Document document = DocumentHelper.createDocument(); 
		if((strDate==null)||("".equals(strDate))||(endDate==null)||("".equals(endDate))){
			Element Info = document.addElement("Info");
			Element error =  Info.addElement("error");
			error.addText("参数不能为空!");
		}
		else{
				 if((!DateTimes.isDate(strDate))||(!DateTimes.isDate(endDate)))
				 {
					 Element Info = document.addElement("Info");
						Element error =  Info.addElement("error");
						error.addText("查询异常!");
				 }
				 else
				 {
				 SimpleDateFormat sim = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				 List<TInfoBasePublish> pub = submitService.publishlist(strDate, endDate);
					Element Info = document.addElement("Info");
					if(pub.size()>0){
					for(int i=0;i<pub.size();i++)
					{
						Element row =  Info.addElement("row");
						Element title =  row.addElement("title");
						title.addText(pub.get(i).getPubTitle()); 
						Element content =  row.addElement("content");
						if(pub.get(i).getPubEditContent()!=null)
						{
						content.addCDATA(pub.get(i).getPubEditContent());
						}
						Element pubDate =  row.addElement("pubDate");
						pubDate.addText(sim.format(pub.get(i).getPubDate()));
						Element submitDate =  row.addElement("submitDate");
						submitDate.addText(sim.format(pub.get(i).getPubSubmitDate()));
						Element orgs =  row.addElement("orgs");
						if((pub.get(i).getPubMergeName()!=null)&&(!"".equals(pub.get(i).getPubMergeName())))
						{
							String merName = pub.get(i).getPubMergeName();
							String m[] = merName.split(",");
							for(int j=0;j<m.length;j++)
							{
								Element org =  orgs.addElement("org");
								org.addText(m[j].toString());
							}
						}
						else
						{
							Element org =  orgs.addElement("org");
								org.addText(pub.get(i).getOrgName());
						}
						
					}
					}
					else
					{
						Element error =  Info.addElement("error");
						error.addText("无数据!");
					}
			} 
			
		}
		return document.asXML();
    }
    
	public String[] sayHi(){
		String a[] = new String[2];
		a[0]="111";
		a[1]="222";
		return a;
	}
}
