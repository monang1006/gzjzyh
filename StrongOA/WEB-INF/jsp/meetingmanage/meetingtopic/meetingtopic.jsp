<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="/tags/web-flex" prefix="webflex"%>
<%@taglib uri="/tags/web-newdate" prefix="strong"%>
<%@include file="/common/include/rootPath.jsp"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<title>议题列表</title>
		<META http-equiv=Content-Type content="text/html; charset=utf-8">
		<link href="<%=frameroot%>/css/strongitmenu.css" type="text/css"
			rel="stylesheet">
		<LINK type=text/css rel=stylesheet
			href="<%=frameroot%>/css/search.css">
	
		<script language='javascript'
			src='<%=request.getContextPath()%>/common/js/grid/ChangeWidthTable.js'></script>
		<SCRIPT language="javascript"
			src="<%=request.getContextPath()%>/common/js/menu/menu.js"></SCRIPT>
	
		<LINK href="<%=frameroot%>/css/properties_windows_list.css" type=text/css
			rel=stylesheet>
		<LINK href="<%=frameroot%>/css/search.css" type=text/css
			rel=stylesheet>
		<script language='javascript'
			src='<%=path%>/common/js/jquery/jquery-1.2.6.js'></script>
		<!--<script language="javascript"
			src="<%=path%>/common/js/common/search.js"></script>-->
		<script language="javascript"
			src="<%=path%>/common/js/common/common.js"></script>
			<SCRIPT LANGUAGE="JavaScript"
			src="<%=path%>/common/js/commontab/service.js"></SCRIPT>
		<SCRIPT LANGUAGE="JavaScript"
			src="<%=path%>/common/js/commontab/workservice.js"></SCRIPT>
		 <script type="text/javascript">
		 function showimg(topicStatus){
  //alert(noticeIs);
      
			var rv = '' ;
			if(topicStatus == '0'){
				rv = "<font color='#90036'>未送审</font>&nbsp&nbsp";
			}
			if(topicStatus == '1'){
				rv = "<font color='#ff1119'>审核中</font>&nbsp&nbsp";
			}
			if(topicStatus == '2'){
				rv = "<font color='#63ad00'>已审核</font>&nbsp&nbsp";
			}
			if(topicStatus == '3'){
				rv = "<font color='red'>占用中</font>&nbsp&nbsp";
			}
			if(topicStatus == '4'){
				rv = "<font color='blue'>占用完毕</font>&nbsp&nbsp";
			}
			return rv;
		}
		 
		 
		 </script> 
	</HEAD>

	<BODY class=contentbodymargin oncontextmenu="return false;"
		onload="initMenuT();">
	 <script type="text/javascript"
      src="<%=path%>/common/js/newdate/WdatePicker.js"></script>
		<DIV id=contentborder align=center>
		<s:form theme="simple" id="myTableForm" action="/meetingmanage/meetingtopic/meetingtopic.action">
			<table width="100%" border="0" cellspacing="0" cellpadding="00">
				<tr>
					<td colspan="3" class="table_headtd">
						<table width="100%" border="0" cellspacing="0" cellpadding="00">
							<tr>
								<td class="table_headtd_img" >
								<img src="<%=frameroot%>/images/ico/ico.gif" >&nbsp;
							</td>
							<td align="left">
								<strong>议题列表</strong>
							</td>
							<td align="right">
								<table border="0" align="right" cellpadding="00" cellspacing="0">
						            <tr>
						            	<td width="4"><img src="<%=frameroot%>/images/bt_l.jpg"/></td>
					                 	<td class="Operation_list" onclick="addTopic();"><img src="<%=root%>/images/operationbtn/add.png"/>&nbsp;添&nbsp;加&nbsp;</td>
					                 	<td width="4"><img src="<%=frameroot%>/images/bt_r.jpg"/></td>
				                  		<td width="5"></td>
				                  		<td width="4"><img src="<%=frameroot%>/images/bt_l.jpg"/></td>
					                 	<td class="Operation_list" onclick="editTopic();"><img src="<%=root%>/images/operationbtn/edit.png"/>&nbsp;编&nbsp;辑&nbsp;</td>
					                 	<td width="4"><img src="<%=frameroot%>/images/bt_r.jpg"/></td>
				                  		<td width="5"></td>
				                  		<td width="4"><img src="<%=frameroot%>/images/bt_l.jpg"/></td>
					                 	<td class="Operation_list" onclick="deleteTopic();"><img src="<%=root%>/images/operationbtn/del.png"/>&nbsp;删&nbsp;除&nbsp;</td>
					                 	<td width="4"><img src="<%=frameroot%>/images/bt_r.jpg"/></td>
				                  		<td width="5"></td>
				                  		<td width="4"><img src="<%=frameroot%>/images/bt_l.jpg"/></td>
					                 	<td class="Operation_list" onclick="viewTopic();"><img src="<%=root%>/images/operationbtn/view.png"/>&nbsp;查&nbsp;看&nbsp;</td>
					                 	<td width="4"><img src="<%=frameroot%>/images/bt_r.jpg"/></td>
				                  		<td width="5"></td>
				                  		<td width="4"><img src="<%=frameroot%>/images/bt_l.jpg"/></td>
					                 	<td class="Operation_list" onclick="songshen();"><img src="<%=root%>/images/operationbtn/Submit.png"/>&nbsp;提&nbsp;交&nbsp;</td>
					                 	<td width="4"><img src="<%=frameroot%>/images/bt_r.jpg"/></td>
				                  		<td width="5"></td>
											</tr></table></td>
										</tr>
									</table>
								</td>
							</tr>
						</table>
						<webflex:flexTable name="myTable" width="100%" height="364px"
							wholeCss="table1" property="id" isCanDrag="true"
							isCanFixUpCol="true" clickColor="#A9B2CA" footShow="showCheck"
							getValueType="getValueByProperty" collection="${page.result}" page="${page}">
							<table width="100%" border="0" cellpadding="0" cellspacing="0"
								class="table1_search">
								<tr>
									<td>
							       		&nbsp;&nbsp;议题主题：&nbsp;<input name="tSubject" id="tSubject" type="text" class="search" title="请您输入议题主题" value="${tSubject}">
							       		&nbsp;&nbsp;议题分类：&nbsp;<input name="tSorts" id="tSorts" type="text" class="search" title="请您输入议题分类" value="${tSorts }">
							       		&nbsp;&nbsp;创建时间：&nbsp;<strong:newdate  name="tEstime" id="tEstime" skin="whyGreen" isicon="true"  classtyle="search" title="请输入创建时间" dateform="yyyy-MM-dd" dateobj="${tEstime}"/>
							       		&nbsp;&nbsp;议题状态：&nbsp;<s:select name="tStatus" list="#{'':'选状态','0':'未送审','1':'审核中','2':'已审核','3':'占用中','4':'占用完毕'}" listKey="key" listValue="value" onchange='$("#img_sousuo").click();'/>
							       		&nbsp;&nbsp;&nbsp;&nbsp;<input id="img_sousuo" type="button" />
							       	</td>
								<!-- 	<td width="5%"  align="center" class="biao_bg1">
										          <img src="<%=root%>/images/ico/sousuo.gif" id="img_sousuo" width="17" height="16" style="cursor: hand;" title="单击搜索">
									</td>
									<td width="35%" class="biao_bg1">	
								<s:textfield name="tSubject" cssClass="search" title="请输入议题主题"></s:textfield> 
									</td>
									<td width="20%" class="biao_bg1">		
									<s:textfield name="tSorts" cssClass="search" title="请输入分类名称"></s:textfield>		
									</td>
									
									<td class="biao_bg1" width="20%">
									 <strong:newdate name="tEstime" id="tEstime" 
                      skin="whyGreen" isicon="true" dateobj="${tEstime}" dateform="yyyy-MM-dd" width="100%" classtyle="search"  title="日期"></strong:newdate>
									</td>
									
									<td width="25%" class="biao_bg1">
								    &nbsp;&nbsp;&nbsp;&nbsp; <s:select name="tStatus"  list="#{'':'选状态','0':'未送审','1':'审核中','2':'已审核','3':'占用中','4':'占用完毕'}" listKey="key" listValue="value" style="width:80%" />
									</td>
								<td width="5%" class="biao_bg1">
								 &nbsp;
									</td> -->
								</tr>
							</table>
							<webflex:flexCheckBoxCol caption="选择" property="topicId"
								showValue="topicCode" width="5%" isCheckAll="true" isCanDrag="false"
								isCanSort="false"></webflex:flexCheckBoxCol>
						<webflex:flexEnumCol caption="议题状态" mapobj="${meetingMap}" property="topicStatus" showValue="topicCode" width="0" isCanDrag="true" isCanSort="true"></webflex:flexEnumCol>	
							
							<webflex:flexTextCol caption="议题主题" property="topicSubject"
								showValue="topicSubject" width="35%" isCanDrag="true" isCanSort="true"showsize="30"></webflex:flexTextCol>
							<webflex:flexTextCol caption="议题分类" property="topicsort.topicsortId"
								showValue="topicsort.topicsortName" width="20%" isCanDrag="true" isCanSort="true" showsize="15"></webflex:flexTextCol>
							<webflex:flexTextCol caption="创建时间" property="topicEstime"
								showValue="topicEstime" width="20%" isCanDrag="true" isCanSort="true"showsize="30"></webflex:flexTextCol>
							<webflex:flexTextCol caption="议题状态" property="topicId" showValue="javascript:showimg(topicStatus)" width="25%" isCanDrag="true" isCanSort="true"></webflex:flexTextCol>

						</webflex:flexTable>
					</td>
				</tr>
			</table>
			</s:form>
						<iframe id="annexFrame" style="display:none"></iframe>
		</DIV>
		<script language="javascript">
var sMenu = new Menu();
function initMenuT(){
  $("input:checkbox").parent().next().hide(); //隐藏第二列
	sMenu.registerToDoc(sMenu);
	var item = null;
	item = new MenuItem("<%=root%>/images/ico/tb-add.gif","添 加","addTopic",1,"ChangeWidthTable","checkMoreDis");
	sMenu.addItem(item);
	item = new MenuItem("<%=root%>/images/ico/bianji.gif","编 辑","editTopic",1,"ChangeWidthTable","checkOneDis");
	sMenu.addItem(item);
	item = new MenuItem("<%=root%>/images/ico/shanchu.gif","删 除","deleteTopic",1,"ChangeWidthTable","checkOneDis");
	sMenu.addItem(item);
	item = new MenuItem("<%=root%>/images/ico/chakan.gif","查 看","viewTopic",1,"ChangeWidthTable","checkOneDis");
	sMenu.addItem(item);
	item = new MenuItem("<%=root%>/images/ico/songshen.gif","提 交","songshen",1,"ChangeWidthTable","checkOneDis");
	sMenu.addItem(item);
	sMenu.addShowType("ChangeWidthTable");
    registerMenu(sMenu);
}

function addTopic(){
   	var width=screen.availWidth/2;
    var height=screen.availHeight*2/3;
	var result=window.showModalDialog("<%=path%>/meetingmanage/meetingtopic/meetingtopic!input.action",window,"dialogWidth:" + width + "pt;dialogHeight:" + height + "pt;"+
		                                "status:no;help:no;scroll:no;");
	//alert(result);
	window.location.href="<%=path %>/meetingmanage/meetingtopic/meetingtopic.action";
}
function editTopic(){
	var id=getValue();
	
		if(id==null || id==""){
		alert("请选择需要编辑的记录！");
			return;
		}
		if(id.length >32){
		alert('一次只能编辑一条记录!');
		return;
	}	
	var ss=$(":checked").parent().next().attr("value");
  //  alert(ss);
   if(ss!='0'){
   		alert('该议题已经提交不允许编辑了！');
   		return ;
   }
   else{
   	var width=screen.availWidth/2;
    var height=screen.availHeight*2/3;
	var result=window.showModalDialog("<%=path%>/meetingmanage/meetingtopic/meetingtopic!input.action?topId="+id,window,"dialogWidth:" + width + "pt;dialogHeight:" + height + "pt;"+
		                                "status:no;help:no;scroll:no;");

	}	
	
	window.location.href="<%=path %>/meetingmanage/meetingtopic/meetingtopic.action";
}
function deleteTopic(){
var id=getValue();
	if(id==null || id==""){
		alert("请选择需要删除的记录！");
			return;
		}
	var ss=$(":checked").parent().next().attr("value");
   
     if(ss=='1'){
   		alert('该议题已经送入流程不允许删除！');
   		return ;
	   }
	 if(ss!='0'&&ss!='1'){
   		alert('该议题已经审核不允许删除！');
   		return ;
	   }
	if(confirm("您确定要删除吗？")){
	$.post(
		"<%=path%>/meetingmanage/meetingtopic/meetingtopic!delete.action",
		{topId:id},
		function(data){
	
			if(data=='deletefalsedeletetrue'){	
		      alert('删除失败，请先删除与之关联的会议')    
		}
			else if(data=='deletetrue'){
		   		location="<%=path%>/meetingmanage/meetingtopic/meetingtopic.action"; 
			}
		}
	);
	}
		//if(confirm("您确定要删除吗？")){
   //location="<%=path%>/meetingmanage/meetingtopic/meetingtopic!delete.action?topId="+id;	
 //}	
}
function viewTopic(){
	var id=getValue();
	
		if(id==null || id==""){
		alert("请选择需要查看的记录！");
			return;
		}
		if(id.length >32){
		alert('一次只能查看一条记录!');
		return;
	}	
	var ss=$(":checked").parent().next().attr("value");
   // alert(ss);
	if(ss=='0'|| ss=='1'|| ss=='2'||ss=='3'|| ss=='4'){
		var width=screen.availWidth/2;
    	var height=screen.availHeight*2/3;
		var result=window.showModalDialog("<%=path%>/meetingmanage/meetingtopic/meetingtopic!displayview.action?topId="+id,window,"dialogWidth:" + width + "pt;dialogHeight:" + height + "pt;"+
		                                "status:no;help:no;scroll:no;");
   }
  
}
/*function quhui(){
	var id=getValue();
	if(id==null || id==""){
		alert("请选择需要取回的记录！");
			return;
		}	
	var ss=$(":checked").parent().next().attr("value");
   
     if(ss =='0'){
   		alert('该议题未送入送入工作流审核，不需要取回');
   		return ;
   }
	if(confirm("您确定要取回吗？")){
	$.post(
			"<%=path%>/meetingmanage/meetingtopic/meetingtopic!quhui.action?thestatus=1",
			{topId:id},
			function(data){
				//alert(data);
				if(data=='OK'){	
				  location="<%=path%>/meetingmanage/meetingtopic/meetingtopic.action";	
			     alert('取回成功！')    
			}
			}
		);
		}
}*/
function songshen(){
    var id=getValue();
		if(id==null || id==""){
		alert("请选择需要送审的记录！");
			return;
		}	
	if(id.length >32){
		alert('一次只能送审一条记录!!!');
		return;
	}	
	var ss=$(":checked").parent().next().attr("value");
  //  alert(ss);
   if(ss!='0'){
   	alert('该会议议题已经送审！');
   		return ;
   }
   $.post(
		"<%=path%>/meetingmanage/meetingtopic/meetingtopic!getProcessName.action",
		{topId:id},
		function(data){
			if(data!='flagfalse'){
				var str = data;
				var p = str.split(",");
				if(p[1] == '0'){
					var returnValue = OpenWindow("<%=root%>/meetingmanage/meetingtopic/meetingtopic!nextstep.action?topId="+p[0], 
			                                  550, 500, window);
			        if(returnValue=='OK'){
			        	location.reload();
			        }
		        }else if(p[1]=='1'){
		        	alert("议题已提交审核中...");
		        }else{
		        	alert("议题已送审...")
		        }
			}else{
			
			$.post(
	          "<%=path%>/meetingmanage/meetingtopic/meetingtopic!pubTopic.action",
		     {topId:id},
		     function(data){
		   
			 if(data=='success'){	
		      alert('成功通过！');
		    location="<%=path%>//meetingmanage/meetingtopic/meetingtopic.action"; 
		}
		
		}
	)
				//location = '<%=path%>/meetingmanage/meetingtopic/meetingtopic!pubTopic.action?topId='+id;
			}
		}
	)
}


$(document).ready(function(){
        $("#img_sousuo").click(function(){
        	$("form").submit();
        });     
      });


</script>
	</BODY>
</HTML>
		