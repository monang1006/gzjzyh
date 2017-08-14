package com.strongit.oa.webservice.iphone.client.iphoneWork;

import java.util.ArrayList;
import java.util.List;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.xml.namespace.QName;
import javax.xml.rpc.ServiceException;

import junit.framework.TestCase;

import org.apache.axis.client.Call;
import org.apache.axis.client.Service;
import org.apache.axis.encoding.ser.JAFDataHandlerDeserializerFactory;
import org.apache.axis.encoding.ser.JAFDataHandlerSerializerFactory;
import org.dom4j.Element;
import org.junit.Test;

import com.strongit.oa.util.Dom4jUtil;

public class iphoneWorkClient extends TestCase {

	private Service service = null;
	private Call call = null;
	private String url = null;
	private Dom4jUtil dom = new Dom4jUtil();
	
	public void setUp(){
		service = new Service();
		try {
			call = (Call)service.createCall();
			url = "http://localhost/oa/services/iphoneWorkService";
			call.setTargetEndpointAddress(url);
		} catch (ServiceException e) {
			e.printStackTrace();
		}
	}

	public void tearDown(){
		service = null;
		call = null;
	}

	@Test
	public void testSubmitWorkflow() throws Exception {
		call.setOperationName("submitWorkflow");
		Object[] params = new Object[]{"939800","这是任务办理意见","6006800BBDF034DFE040007F01001D9F"};
		String ret = (String)call.invoke(params);
		System.out.println(ret);
	}
	
	@Test
	public void testFetchTask() throws Exception {
		url = "http://192.168.2.122:8080/oa5.0/services/WorkflowService";
		call.setTargetEndpointAddress(url);
		call.setOperationName("fetchTask");
		Object[] params = new Object[]{"417045","6006800BBDF034DFE040007F01001D9F"};
		String ret = (String)call.invoke(params);
		System.out.println(ret);
	}
	
	@Test
	public void testLoadAttachment() throws Exception {
		url = "http://192.168.2.122:8080/StrongOA/services/WorkflowService";
		call.setTargetEndpointAddress(url);
		call.setOperationName("loadAttachment");
		Object[] params = new Object[]{"402882fa35dbfeb30135dc0527370003"};
		String ret = (String)call.invoke(params);
		System.out.println(ret);
	}
	
	
	
	
	@Test
	public void testLoadFormData() throws Exception  {
		url = "http://192.168.2.96/oa/services/iphoneWorkService";
		call.setTargetEndpointAddress(url);
		call.setOperationName("loadFormData");
		Object[] params = new Object[]{"ff8080812fddec2c012fde23f86a0050","2974067"};//938022,
		String ret = (String)call.invoke(params);
		System.out.println(ret);
	}
	
	
	@Test
	public void testGetUsmedSuggestion() throws Exception  {
		url = "http://192.168.2.122:8080/StrongOA/services/WorkflowService";
		call.setTargetEndpointAddress(url);
		call.setOperationName("getUsedSuggestion");
		Object[] params = new Object[]{"6006800BBDF034DFE040007F01001D9F"};//938022,
		String ret = (String)call.invoke(params);
		System.out.println(ret);
	}
	
	@Test
	public void testGetToDo() throws Exception {
		url = "http://localhost/oa/services/WorkflowService";
		call.setTargetEndpointAddress(url);
		call.setOperationName("getTodo");
		Object[] params = new Object[]{"ff808081244cdf2401244ce78204040f",null,null,"1","40",null,null};
		String ret = (String)call.invoke(params);
		System.out.println(ret);
	}
	
	@Test
	public void testLoadContent() throws Exception {
		url = "http://localhost/OA5.0/services/iphoneWorkService ";
		call.setTargetEndpointAddress(url);
		call.setOperationName("loadContent");
		Object[] params = new Object[]{"2580475"};
		String ret = (String)call.invoke(params);
		System.out.println(ret);
	}
	
	@Test
	public void testGetProcessed() throws Exception {
		url = "http://localhost/oa/services/WorkflowService";
		call.setTargetEndpointAddress(url);
		call.setOperationName("getProcessed");
		Object[] params = new Object[]{"ff808081244cdf2401244ce78204040f",null,null,"1","15",null,null};
		String ret = (String)call.invoke(params);
		System.out.println(ret);
	}
	
	/**
	 * 测试根据流程名称获取流程步骤
	 * @author:邓志城
	 * @date:2009-7-8 下午05:03:00
	 * @see com.strongit.oa.webservice.server.workflow.getNextTransitions
	 */
//	@Test
//	public void testGetNextTransitions()throws Exception{
//		url = "http://192.168.2.168:8088/StrongOA2.0_DEV/services/WorkflowService";
//		call.setTargetEndpointAddress(url);
//		call.setOperationName("getNextTransitions");
//		Object[] params = new Object[]{"发文管理1"};
//		String ret = (String)call.invoke(params);
//		//根据返回的XML字符生成一个document对象
//		dom.loadXML(ret);
//		List<Element> statusElements = dom.getElementsByTagNames("status");
//		Element statusElement = statusElements.get(0);
//		String status = statusElement.getTextTrim();
//		assertEquals("1", status);
//	}

	/**
	 * 测试保存表单数据
	 * @author:邓志城
	 * @date:2009-7-8 下午05:36:41
	 * @see com.strongit.oa.webservice.server.workflow.saveFormData
	 * @throws Excepiton
	 */
	@Test
	public void testSaveFormData()throws Exception{
		String fileName1 = "F:\\反编译工具\\FormServiceImpl.java";
	//	String fileName2 = "F:\\Java相关\\Axis\\axis-bin-1_4.zip";
		//String fileName3 = "F:\\Java相关\\Axis\\axis2-1.4.1-bin.zip";
		List<DataHandler> lst = new ArrayList<DataHandler>();
		List<String> fileNames = new ArrayList<String>();
		DataHandler dh1 = new DataHandler(new FileDataSource(fileName1));
	//	DataHandler dh2 = new DataHandler(new FileDataSource(fileName2));
		//DataHandler dh3 = new DataHandler(new FileDataSource(fileName3));
		lst.add(dh1);
	//	lst.add(dh2);
	//	lst.add(dh3);
		fileNames.add(dh1.getName());
	//	fileNames.add(dh2.getName());
	//	fileNames.add(dh3.getName());
		url = "http://192.168.2.88:8001/StrongOA/services/WorkflowService";
		call.setTargetEndpointAddress(url);
		call.setOperationName("saveFormData");
		QName qnameAttachMent = new QName("http://xml.apache.org/axis/wsdd/providers/java","ns1:DataHandler");
		call.registerTypeMapping(DataHandler.class, 
								 qnameAttachMent, 
								 JAFDataHandlerSerializerFactory.class,
								 JAFDataHandlerDeserializerFactory.class);
		//Object[] params = new Object[]{"13607041072","思创数码科技股份有限公司","A-0907","以技术为先导","活期账户","交通银行","6002222223232323232323","我开户。ipp",null,null};//,lst.toArray(new DataHandler[lst.size()]),fileNames.toArray(new String[fileNames.size()])};
		Object[] params = new Object[]{"13607041072","思创数码科技股份有限公司","A-0907","以技术为先导","活期账户","交通银行","6002222223232323232323","我开户。ipp","oa200971311917",lst.toArray(new DataHandler[lst.size()]),fileNames.toArray(new String[fileNames.size()])};
		String ret = (String)call.invoke(params);
		//根据返回的XML字符生成一个document对象
		dom.loadXML(ret);
		List<Element> statusElements = dom.getElementsByTagNames("status");
		Element statusElement = statusElements.get(0);
		String status = statusElement.getTextTrim();
		assertEquals("1", status);
	}

	/**
	 * 提交工作流时需要业务数据ID
	 * @author:邓志城
	 * @date:2009-7-8 下午05:43:42
	 * @return
	 * @throws Exception
	 */
	private String getBusinessId()throws Exception{
		url = "http://192.168.2.168:8088/StrongOA2.0_DEV/services/WorkflowService";
		call.setTargetEndpointAddress(url);
		call.setOperationName("saveFormData");
		Object[] params = new Object[]{"13607041072","思创数码科技股份有限公司","思创-0907","以技术为先导","活期账户","交通银行","6002222223232323232323","oa200971311917",null,null};
		String ret = (String)call.invoke(params);
		//根据返回的XML字符生成一个document对象
		dom.loadXML(ret);
		List<Element> itemElements = dom.getElementsByTagNames("item");
		Element itemElement = itemElements.get(0);
		String businessId = itemElement.attributeValue("value");
		System.out.println("businessId:"+businessId);
		return businessId;
	}
	
	/**
	 * 测试提交工作流
	 * @author:邓志城
	 * @date:2009-7-8 下午05:39:46
	 * @see com.strongit.oa.webservice.server.workflow.handleWorkFlow
	 * @throws Exception
	 */
	@Test
	public void testHandleWorkFlow()throws Exception{
		String businessId = getBusinessId();
		url = "http://192.168.2.168:8088/StrongOA2.0_DEV/services/WorkflowService";
		call.setTargetEndpointAddress(url);
		call.setOperationName("handleWorkFlow");
		Object[] params = new Object[]{"IPPOA",businessId,"以技术为先导","连接6","IPP请求OA审批"};
		String ret = (String)call.invoke(params);
		//根据返回的XML字符生成一个document对象
		dom.loadXML(ret);
		List<Element> statusElements = dom.getElementsByTagNames("status");
		Element statusElement = statusElements.get(0);
		String status = statusElement.getTextTrim();
		assertEquals("1", status);
	}

	/**
	 * 测试提交工作流【只要求客户端提供业务数据】
	 * @author:邓志城
	 * @date:2009-7-10 上午11:56:59
	 * @throws Exception
	 */
	@Test
	public void testSubmitFormData()throws Exception{
		String fileName1 = "F:\\反编译工具\\FormServiceImpl.java";
		//String fileName2 = "F:\\Java相关\\Axis\\axis-bin-1_4.zip";
		List<DataHandler> lst = new ArrayList<DataHandler>();
		List<String> fileNames = new ArrayList<String>();
		DataHandler dh1 = new DataHandler(new FileDataSource(fileName1));
		//DataHandler dh2 = new DataHandler(new FileDataSource(fileName2));
		lst.add(dh1);
		//lst.add(dh2);
		fileNames.add(dh1.getName());
		//fileNames.add(dh2.getName());
		//url = "http://192.168.2.168:8088/StrongOA2.0_DEV/services/WorkflowService";
		url = "http://192.168.2.88:8001/StrongOA/services/WorkflowService";
		call.setTargetEndpointAddress(url);
		call.setOperationName("submitFormData");
		QName qnameAttachMent = new QName("http://xml.apache.org/axis/wsdd/providers/java","ns1:DataHandler");
		call.registerTypeMapping(DataHandler.class, 
								 qnameAttachMent, 
								 JAFDataHandlerSerializerFactory.class,
								 JAFDataHandlerDeserializerFactory.class);
		//Object[] params = new Object[]{"13607041072","思创数码科技股份有限公司","思创-0907","以技术为先导","活期账户","交通银行","6002222223232323232323","我开户。ipp",null,null};//,lst.toArray(new DataHandler[lst.size()]),fileNames.toArray(new String[fileNames.size()])};
		Object[] params = new Object[]{"13607041072",
									   "思创数码科技股份有限公司",
									   "思创-0907",
									   "以技术为先导",
									   "活期账户",
									   "交通银行",
									   "6002222223232323232323",
									   "我开户。ipp",
									   "oa200971311917",//咨询反馈号
									   null,
									   null};
		String ret = (String)call.invoke(params);
		//根据返回的XML字符生成一个document对象
		dom.loadXML(ret);
		List<Element> statusElements = dom.getElementsByTagNames("status");
		Element statusElement = statusElements.get(0);
		String status = statusElement.getTextTrim();
		assertEquals("1", status);
	}

	/**
	 * 测试安全的WEBSERVICE提交工作流数据
	 * @author:邓志城
	 * @date:2009-7-10 下午02:57:53
	 * @throws Exception
	 */
	@Test
	public void testSafeSubmitFormData()throws Exception{
		String fileName1 = "F:\\反编译工具\\FormServiceImpl.java";
		//String fileName2 = "F:\\Java相关\\Axis\\axis-bin-1_4.zip";
		List<DataHandler> lst = new ArrayList<DataHandler>();
		List<String> fileNames = new ArrayList<String>();
		DataHandler dh1 = new DataHandler(new FileDataSource(fileName1));
		//DataHandler dh2 = new DataHandler(new FileDataSource(fileName2));
		lst.add(dh1);
		//lst.add(dh2);
		fileNames.add(dh1.getName());
		//fileNames.add(dh2.getName());
		url = "https://192.168.2.168:8443/StrongOA2.0_DEV/services/WorkflowService";
		//设置客户端证书路径以及密码【SSL】
		System.setProperty("javax.net.ssl.keyStore", "F:/keystore/client.keystore");  
		System.setProperty("javax.net.ssl.keyStorePassword", "123456");  
		System.setProperty("javax.net.ssl.trustStore", "F:/keystore/client.truststore");
		
		call.setTargetEndpointAddress(url);
		call.setOperationName("submitFormData");
		QName qnameAttachMent = new QName("http://xml.apache.org/axis/wsdd/providers/java","ns1:DataHandler");
		call.registerTypeMapping(DataHandler.class, 
								 qnameAttachMent, 
								 JAFDataHandlerSerializerFactory.class,
								 JAFDataHandlerDeserializerFactory.class);
		//Object[] params = new Object[]{"13607041072","思创数码科技股份有限公司","思创-0907","以技术为先导","活期账户","交通银行","6002222223232323232323","我开户。ipp",null,null};//,lst.toArray(new DataHandler[lst.size()]),fileNames.toArray(new String[fileNames.size()])};
		Object[] params = new Object[]{"13607041072",
									   "思创数码科技股份有限公司",
									   "思创-0907",
									   "以技术为先导",
									   "活期账户",
									   "交通银行",
									   "6002222223232323232323",
									   "我开户。ipp",
									   "oa200971311917",//咨询反馈号
									   lst.toArray(new DataHandler[lst.size()]),
									   fileNames.toArray(new String[fileNames.size()])};
		String ret = (String)call.invoke(params);
		//根据返回的XML字符生成一个document对象
		dom.loadXML(ret);
		List<Element> statusElements = dom.getElementsByTagNames("status");
		Element statusElement = statusElements.get(0);
		String status = statusElement.getTextTrim();
		assertEquals("1", status);
	}
	
	
	@Test
	public void testGetNextTransitions() throws Exception  {
		url = "http://localhost:8080/OA5.0/services/WorkflowService";
		call.setTargetEndpointAddress(url);
		call.setOperationName("getNextTransitions");
		Object[] params = new Object[]{"150329"};//938022,
		String ret = (String)call.invoke(params);
		System.out.println(ret);
	}
	
	@Test
	
//	* @param nodeId 节点ID
	// * @param taskId 任务ID
	// * @param transitionId 迁移线ID
	public void testGetWorkflowTaskActors() throws Exception  {
		url = "http://192.168.2.122:8080/StrongOA/services/WorkflowService";
		call.setTargetEndpointAddress(url);
		call.setOperationName("getWorkflowTaskActors");
		Object[] params = new Object[]{"113464","135679","113448"};//938022,
		String ret = (String)call.invoke(params);
		System.out.println(ret);
	}
	@Test
	
	/*@param taskId
	 *            任务ID
	 * @param transitionName
	 *            流向名称
	 * @param returnNodeId
	 *            驳回节点ID
	 * @param businessId
	 *            表单业务数据ID
	 * @param suggestion
	 *            提交意见
	 * @param userId
	 *            人员ID
	 * @return String
	 * */
	public void testSubmitWorkflowNext() throws Exception  {
		
 		url = "http://192.168.2.122:8080/StrongOA/services/WorkflowService";
		call.setTargetEndpointAddress(url);
		call.setOperationName("submitWorkflowNext");
		Object[] params = new Object[]{"135679","6006800BBDF034DFE040007F01001D9F","我是是世俗","送办公室","ff80808125963f0a0125964897540006","113464"};//938022,
		
		//String taskId,String userId,String suggestion,String transitionName ,String taskActors
		String ret = (String)call.invoke(params);
		System.out.println(ret);
	}
	
	public void testGetIpadBgImage() throws Exception  {
			
	 		url = "http://localhost:8080/OA5.0/services/ipadLoginService";
			call.setTargetEndpointAddress(url);
			call.setOperationName("getIpadBgImage");
			Object[] params = new Object[]{};//938022,
			
			//String taskId,String userId,String suggestion,String transitionName ,String taskActors
			String ret = (String)call.invoke(params);
			System.out.println(ret);
		}
	
	public void testGetIpadBgUpdate() throws Exception  {
		
		url = "http://192.168.2.122:8080/StrongOA/services/ipadLoginService";
		call.setTargetEndpointAddress(url);
		call.setOperationName("getIpadBgUpdate");
		Object[] params = new Object[]{};//938022,
		
		//String taskId,String userId,String suggestion,String transitionName ,String taskActors
		String ret = (String)call.invoke(params);
		System.out.println(ret);
	}
	
	public void testBackLast() throws Exception  {
		
		url = "http://localhost/oa/services/iphoneWorkService";
		call.setTargetEndpointAddress(url);
		call.setOperationName("backLast");
		Object[] params = new Object[]{"2609581","ff8080812fddec2c012fde23f86a0050","申仪玲Iphone端退文"};
		//String taskId,String userId,String suggestion,String transitionName ,String taskActors
		String ret = (String)call.invoke(params);
		System.out.println(ret);
    }
	
	public void testGetOtherTodo() throws Exception  {
		
		url = "http://localhost/oa/services/iphoneWorkService";
		call.setTargetEndpointAddress(url);
		call.setOperationName("getOtherTodo");
		Object[] params = new Object[]{"ff808081244cdf2401244ce78204040f",null,null,"1","20",null, null};
		//String taskId,String userId,String suggestion,String transitionName ,String taskActors
		String ret = (String)call.invoke(params);
		System.out.println(ret);
    }
	
	public void testGetOtherProcessed() throws Exception  {
		
		url = "http://localhost/oa/services/iphoneWorkService";
		call.setTargetEndpointAddress(url);
		call.setOperationName("getOtherProcessed");
		Object[] params = new Object[]{"ff808081244cdf2401244ce78204040f",null,null,"1","20",null, null};
		//String taskId,String userId,String suggestion,String transitionName ,String taskActors
		String ret = (String)call.invoke(params);
		System.out.println(ret);
    }
	
}
