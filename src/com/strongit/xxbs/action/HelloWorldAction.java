package com.strongit.xxbs.action;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.cxf.jaxws.endpoint.dynamic.JaxWsDynamicClientFactory;
import org.apache.cxf.endpoint.Client;
import org.springframework.beans.factory.annotation.Autowired;

import com.strongit.xxbs.entity.TInfoBaseBulletin;
import com.strongit.xxbs.service.HelloWorld;
import com.strongmvc.webapp.action.BaseActionSupport;
public class HelloWorldAction extends BaseActionSupport<TInfoBaseBulletin> {

	/**
	 * @param args
	 * @throws Exception 
	 */
	/*public static void main(String[] args) throws Exception {
		System.out.println(System.getProperty("java.endorsed.dirs")); 
		JaxWsDynamicClientFactory clientFactory = JaxWsDynamicClientFactory
		.newInstance();		

		Client client = clientFactory
				.createClient("http://192.168.2.84/xxcb/ws/oaPublish?wsdl");
		
		Object[] result = client.invoke("getPublishs",new Object[]{"1","2013-3-21 0:0:0"});
		
		System.out.println(result[0]);

	}*/
	
	private HelloWorld helloWorld;
	private String tXML;
	
	
	@Autowired
	public void setHelloWorld(HelloWorld helloWorld) {
		this.helloWorld = helloWorld;
	}

	public TInfoBaseBulletin getModel() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String delete() throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String input() throws Exception {
		HttpClient client = new HttpClient();
		String dominoUrl = "http://192.168.2.84";
		int protolIndex = dominoUrl.indexOf("://");
		int ipIndex = dominoUrl.lastIndexOf(":");
		String protol = dominoUrl.substring(0, protolIndex).trim();
		String strDate = getRequest().getParameter("strDate");
		String endDate = getRequest().getParameter("endDate");
		String ip;
		int port;
		if (protolIndex >= ipIndex) {
			ip = dominoUrl.substring(protolIndex + 3)
					.trim();
			port = 80;
		} else {
			ip = dominoUrl.substring(protolIndex + 3,
					ipIndex).trim();
			port = Integer.parseInt(dominoUrl.substring(
					ipIndex + 1).trim());
		}
		client.getHostConfiguration().setHost(ip, port,
				protol);
		GetMethod post = new GetMethod("/xxcb/xxbs/action/helloWorld.action?");
		//post.setQueryString("")
		client.executeMethod(post);
		
		byte[] inputstr = post.getResponseBody();
		ByteArrayInputStream ByteinputStream = new ByteArrayInputStream(
				inputstr);
		ByteArrayOutputStream outStream = new ByteArrayOutputStream();
		int ch = 0;
		try {
			while ((ch = ByteinputStream.read()) != -1) {
				int upperCh = (char) ch;
				outStream.write(upperCh);
			}
		} catch (Exception e) {
		}
		tXML=new String(outStream.toByteArray(),"utf-8");
		getResponse().getWriter().write(tXML);
		return null;
	}

	@Override
	public String list() throws Exception {
		String strDate = getRequest().getParameter("strDate");
		String endDate = getRequest().getParameter("endDate");
		tXML = helloWorld.getPublishs(strDate,endDate);
		getResponse().getWriter().write(tXML);
		return null;
	}

	@Override
	protected void prepareModel() throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String save() throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	public String gettXML() {
		return tXML;
	}

	public void settXML(String tXML) {
		this.tXML = tXML;
	}

	
}
