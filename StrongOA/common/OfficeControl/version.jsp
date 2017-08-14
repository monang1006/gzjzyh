
<%@ page language="java" contentType="text/html; charset=UTF-8" %>
<%@ page import="com.strongit.oa.systemset.SystemsetAction" %>


<%
  SystemsetAction sys = new SystemsetAction();
  String officeNew = sys.getOfficeNew();
  //根据系统全局设置里面的OFFICE控件设置来判断使用哪个版本的OFFICE控件
  if(officeNew!=null && officeNew!="" && officeNew=="1"){
	  //等于1使用新版本的OFFICE
	  String OCXVersion = "#Version=5,0,1,0"; 
  }else{
	 //使用原来的OFFICE
  	String OCXVersion = "#Version=5,0,1,8"; 
  }
  request.setAttribute("officeNew",officeNew);
%>
<script type="text/javascript">	
  var officeNew = '${officeNew}';
   if(officeNew!= null && officeNew!= "" && officeNew=="1"){
	   	//新的OFFICE
	   var ProductCaption = "综合协同办公平台";
	   var productKey = "6E306A06154DC6066435DB5F17A3C138C467DD8F";
	   var OfficeTabContent = "";
	   OfficeTabContent += "<html><meta http-equiv='Content-Type' content='text/html; charset=GBK' />"
	   OfficeTabContent += "<title></title>";
	   OfficeTabContent +="<head>";
	   OfficeTabContent +="<script language='JScript' for='TANGER_OCX_OBJ' event='OnCustomMenuCmd2(menuPos,submenuPos,subsubmenuPos,menuCaption,menuID)'>";
	   OfficeTabContent +="if(menuID == '1000'){parent.TANGER_OCX_SetMarkModify(true);}if(menuID == '1001'){parent.TANGER_OCX_SetMarkModify(false);}";
	   OfficeTabContent +="if(menuID == '1003'){parent.TANGER_OCX_ShowRevisions(true);}if(menuID == '1004'){parent.TANGER_OCX_ShowRevisions(false);}";
	   OfficeTabContent +="if(menuID == '1006'){parent.TANGER_OCX_AcceptAllRevisions();}if(menuID == '1008'){parent.TANGER_OCX_AddTemplateFromURL();}";
	   OfficeTabContent +="if(menuID == '1009'){parent.TANGER_OCX_AddSignFromLocal();}";
	   OfficeTabContent += "</s";
	   OfficeTabContent += "cript>";
       OfficeTabContent +="</head>";
	   OfficeTabContent +=   "<body style='margin-left: 0px;margin-top: 0px;margin-right: 0px;margin-bottom: 0px;'>";
	   OfficeTabContent += "<object id=\"TANGER_OCX_OBJ\" width=\"100%\" height=\"100%\" classid=\"clsid:C39F1330-3322-4a1d-9BF0-0BA2BB90E970\" ";
	   OfficeTabContent += "codebase='" + scriptroot + "/common/OfficeControl_new/ofctnewclsid.cab#version=5,0,2,7'>";
	   OfficeTabContent += "<param name='MakerCaption' value=\"思创数码科技股份有限公司\">";
	   OfficeTabContent += "<param name='MakerKey' value='5C1FF1F1177246B272DB34DD8ADA318222D19F65'>"
	   OfficeTabContent += "<param name='ProductCaption' value='"+ProductCaption+"'>";
	   OfficeTabContent += "<param name='ProductKey' value='"+productKey+"'>";
	   OfficeTabContent += "<param name='TitleBar' value='false'>";
	   OfficeTabContent += "<param name='BorderStyle' value='1'>";
	   OfficeTabContent += "<param name='TitlebarColor' value='42768'>";
	   OfficeTabContent += "<param name='TitlebarTextColor' value='0'>";
	   OfficeTabContent += "<param name='IsResetToolbarsOnOpen' value='true'>";
	   OfficeTabContent += "<param name='IsUseUTF8URL' value='true'>";
	   OfficeTabContent += "<param name='IsUseUTF8Data' value='true'>";
	   OfficeTabContent += "<span style=\"color: red\">不能装载文档控件。请在检查浏览器的选项中检查浏览器的安全设置。</span></object>";
	   OfficeTabContent += "</body></html>";
   }else{
	   //原来的OFFICE
		  var ProductCaption = "综合协同办公平台";
		  var productKey = "6E306A06154DC6066435DB5F17A3C138C467DD8F";
		  var OfficeTabContent = "";
		  OfficeTabContent +="<html><meta http-equiv='Content-Type' content='text/html; charset=GBK' />"
		  OfficeTabContent +="<title></title>";
		  OfficeTabContent +="<head>";
		  OfficeTabContent +="<script language='JScript' for='TANGER_OCX_OBJ' event='OnCustomMenuCmd2(menuPos,submenuPos,subsubmenuPos,menuCaption,menuID)'>";
		  //OfficeTabContent +="alert('第' + menuPos +','+ submenuPos +','+ subsubmenuPos +'个菜单项,menuID='+menuID+',菜单标题为'+menuCaption+'的命令被执行.');";
		  OfficeTabContent +="if(menuID == '1000'){parent.TANGER_OCX_SetMarkModify(true);}if(menuID == '1001'){parent.TANGER_OCX_SetMarkModify(false);}";
		  OfficeTabContent +="if(menuID == '1003'){parent.TANGER_OCX_ShowRevisions(true);}if(menuID == '1004'){parent.TANGER_OCX_ShowRevisions(false);}";
		  OfficeTabContent +="if(menuID == '1006'){parent.TANGER_OCX_AcceptAllRevisions();}if(menuID == '1008'){parent.TANGER_OCX_AddTemplateFromURL();}";
		  OfficeTabContent +="if(menuID == '1009'){parent.TANGER_OCX_AddSignFromLocal();}";
		  OfficeTabContent += "</s";
		  OfficeTabContent += "cript>";
		  
		  //OfficeTabContent +="<script language='JScript' for='TANGER_OCX_OBJ' event='AfterOpenFromURL(doc)'>"; 
		  //OfficeTabContent +="if(typeof(parent.initWordAfter) != 'undefined'){parent.initWordAfter()};";
		  //OfficeTabContent += "</s";
		  //OfficeTabContent += "cript>";
		  
		  OfficeTabContent +="</head>";
		  OfficeTabContent +="<body style='margin-left: 0px;margin-top: 0px;margin-right: 0px;margin-bottom: 0px;'>";
		  OfficeTabContent += "<object id=\"TANGER_OCX_OBJ\" width=\"100%\" height=\"100%\" classid=\"clsid:01DFB4B4-0E07-4e3f-8B7A-98FD6BFF153F\" ";
		  OfficeTabContent += "codebase='" + scriptroot + "/common/OfficeControl/ofctnewclsid.cab#Version=5,0,2,4'>";
		  OfficeTabContent += "<param name='MakerCaption' value=\"思创数码科技股份有限公司\">";
		  OfficeTabContent += "<param name='MakerKey' value='5C1FF1F1177246B272DB34DD8ADA318222D19F65'>"
		  OfficeTabContent += "<param name='ProductCaption' value='"+ProductCaption+"'>";
		  OfficeTabContent += "<param name='ProductKey' value='"+productKey+"'>";
		  OfficeTabContent += "<param name='TitleBar' value='false'>";
		  OfficeTabContent += "<param name='BorderStyle' value='1'>";
		  OfficeTabContent += "<param name='TitlebarColor' value='42768'>";
		  OfficeTabContent += "<param name='TitlebarTextColor' value='0'>";
		  OfficeTabContent += "<param name='IsResetToolbarsOnOpen' value='true'>";
		  OfficeTabContent += "<param name='IsUseUTF8URL' value='true'>";
		  OfficeTabContent += "<param name='IsUseUTF8Data' value='true'>";
		  OfficeTabContent += "<span style=\"color: red\">不能装载文档控件。请在检查浏览器的选项中检查浏览器的安全设置。</span></object>";
		  OfficeTabContent += "</body></html>";
   }
</script>