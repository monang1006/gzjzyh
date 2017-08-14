<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="/tags/web-flex" prefix="webflex"%>
<%@taglib uri="/tags/web-newdate" prefix="strong"%>
<%@include file="/common/include/rootPath.jsp"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%--<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">--%>
<html>
	<head>
		<%@include file="/common/include/meta.jsp"%>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<title>会议通知列表</title>
		<META http-equiv=Content-Type content="text/html; charset=utf-8">
		<link href="<%=frameroot%>/css/strongitmenu.css" type="text/css"
			rel="stylesheet">
		<LINK type=text/css rel=stylesheet
			href="<%=frameroot%>/css/search.css">
		<LINK href="<%=frameroot%>/css/properties_windows_list.css" type="text/css"
			rel=stylesheet>
		<script language='javascript'
			src='<%=path%>/common/js/jquery/jquery-1.2.6.js'></script>
		<!--<script language="javascript"
			src="<%=path%>/common/js/common/search.js"></script>-->
		<script language='javascript'
			src='<%=path%>/common/js/grid/ChangeWidthTable.js'></script>
		<SCRIPT language="javascript" src="<%=path%>/common/js/menu/menu.js"></SCRIPT>
		<script language="javascript"
			src="<%=path%>/common/js/common/common.js"></script>
		<SCRIPT LANGUAGE="JavaScript"
			src="<%=path%>/common/js/commontab/service.js"></SCRIPT>
		<SCRIPT LANGUAGE="JavaScript"
			src="<%=path%>/common/js/commontab/workservice.js"></SCRIPT>


		<script type="text/javascript">
		
		 function showimg(noticeIs){

			var rv = '' ;
			if(noticeIs == '0'){
				rv = "<font color='#ff090d'>未发送</font>&nbsp&nbsp";
			}
			if(noticeIs == '1'){
				rv = "<font color='#63ad00'>已发送</font>&nbsp&nbsp";
			}
					
			if(noticeIs == '2'){
				rv = "<font color='#63ad00'>会议结束</font>&nbsp&nbsp";
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
			<s:form theme="simple" id="myTableForm"
				action="/meetingmanage/meetingnotice/meetingnotice.action">
				<table width="100%" border="0" cellspacing="0" cellpadding="00">
					<tr>
						<td colspan="3" class="table_headtd">
							<table width="100%" border="0" cellspacing="0" cellpadding="00">
								<tr>
									<td class="table_headtd_img" >
								<img src="<%=frameroot%>/images/ico/ico.gif" >&nbsp;
							</td>
							<td align="left">
								<strong>通知列表</strong>
							</td>
							<td align="right">
								<table border="0" align="right" cellpadding="00" cellspacing="0">
						            <tr>
						            	<td width="4"><img src="<%=frameroot%>/images/bt_l.jpg"/></td>
					                 	<td class="Operation_list" onclick="addTitle()"><img src="<%=root%>/images/operationbtn/add.png"/>&nbsp;草拟通知&nbsp;</td>
					                 	<td width="4"><img src="<%=frameroot%>/images/bt_r.jpg"/></td>
				                  		<td width="5"></td>
				                  		<td width="4"><img src="<%=frameroot%>/images/bt_l.jpg"/></td>
					                 	<td class="Operation_list" onclick="editTitle()"><img src="<%=root%>/images/operationbtn/edit.png"/>&nbsp;编辑通知&nbsp;</td>
					                 	<td width="4"><img src="<%=frameroot%>/images/bt_r.jpg"/></td>
				                  		<td width="5"></td>
				                  		<td width="4"><img src="<%=frameroot%>/images/bt_l.jpg"/></td>
					                 	<td class="Operation_list" onclick="viewTitle()"><img src="<%=root%>/images/operationbtn/view.png"/>&nbsp;查看通知&nbsp;</td>
					                 	<td width="4"><img src="<%=frameroot%>/images/bt_r.jpg"/></td>
				                  		<td width="5"></td>
				                  		<td width="4"><img src="<%=frameroot%>/images/bt_l.jpg"/></td>
					                 	<td class="Operation_list" onclick="sendTitle()"><img src="<%=root%>/images/operationbtn/public.png"/>&nbsp;发送通知&nbsp;</td>
					                 	<td width="4"><img src="<%=frameroot%>/images/bt_r.jpg"/></td>
				                  		<td width="5"></td>
				                  		<td width="4"><img src="<%=frameroot%>/images/bt_l.jpg"/></td>
					                 	<td class="Operation_list" onclick="deleteTitle()"><img src="<%=root%>/images/operationbtn/del.png"/>&nbsp;删除通知&nbsp;</td>
					                 	<td width="4"><img src="<%=frameroot%>/images/bt_r.jpg"/></td>
				                  		<td width="5"></td>
				                  		<td width="4"><img src="<%=frameroot%>/images/bt_l.jpg"/></td>
					                 	<td class="Operation_list" onclick="shenqinroom()"><img src="<%=root%>/images/operationbtn/del.png"/>&nbsp;申请会议室&nbsp;</td>
					                 	<td width="4"><img src="<%=frameroot%>/images/bt_r.jpg"/></td>
				                  		<td width="5"></td>
												</tr>
											</table>
										</td>
									</tr>
										</table>
									</td>
								</tr>
							</table>
							<webflex:flexTable name="myTable" width="100%" height="364px"
								wholeCss="table1" property="id" isCanDrag="true"
								isCanFixUpCol="true" clickColor="#A9B2CA" footShow="showCheck"
								getValueType="getValueByProperty" collection="${nopage.result}"
								page="${nopage}" pagename="nopage">
								<table width="100%" border="0" cellpadding="0" cellspacing="0"
									class="table1_search">
									<tr>
										<td>
							       		&nbsp;&nbsp;通知标题：&nbsp;<input name="noticeTitle" id="noticeTitle" type="text" class="search" title="请您输入通知标题" value="${model.noticeTitle }">
							       		&nbsp;&nbsp;通知地址：&nbsp;<input name="noticeAddr" id="noticeAddr" type="text" class="search" title="请您输入通知地址" value="${model.noticeAddr }">
							       		&nbsp;&nbsp;开始时间：&nbsp;<strong:newdate  name="noticeStime" id="noticeStime" skin="whyGreen" isicon="true"  classtyle="search" title="请输入开始时间" dateform="yyyy-MM-dd" />
							       		&nbsp;&nbsp;结束时间：&nbsp;<strong:newdate  name="noticeEndTime" id="noticeEndTime" skin="whyGreen" isicon="true"  classtyle="search" title="请输入结束时间" dateform="yyyy-MM-dd" />
							       		&nbsp;&nbsp;发送状态：&nbsp;<s:select name="noticeIs" list="#{'':'选择状态','0':'未发送','1':'已发送','2':'会议结束'}"  listKey="key" listValue="value" onchange='$("#img_sousuo").click();'/>
							       		&nbsp;&nbsp;&nbsp;&nbsp;<input id="img_sousuo" type="button" />
							       	</td>
										<!--  <td width="5%" align="center" class="biao_bg1">
											<img src="<%=root%>/images/ico/sousuo.gif" id="img_sousuo"
												width="17" height="16" style="cursor: hand;" title="单击搜索">
										</td>
										<td width="25%" class="biao_bg1">
											<s:textfield name="noticeTitle" cssClass="search"
												title="请输入通知标题"></s:textfield>
										</td>

										<td width="25%" class="biao_bg1">

											<s:textfield name="noticeAddr" cssClass="search"
												title="请输入通知地址"></s:textfield>
										</td>

										<td class="biao_bg1" width="15%" align="center">
										
											<strong:newdate name="noticeStime" id="noticeStime" 
												skin="whyGreen" isicon="true" dateobj="${noticeStime}"
												dateform="yyyy-MM-dd" width="100%" classtyle="search" 
		                    					title="开始日期"></strong:newdate>
											
										</td>
										<td class="biao_bg1" width="15%" align="center">
										
											<strong:newdate name="noticeEndTime" id="noticeEndTime"
												skin="whyGreen" isicon="true" dateobj="${noticeEndTime}"
												dateform="yyyy-MM-dd" width="100%" classtyle="search" 
		                    					title="结束日期"></strong:newdate>
										</td>
										<td width="20%" class="td1" align="center">
											<s:select name="noticeIs"
												list="#{'':'选择状态','0':'未发送','1':'已发送','2':'会议结束'}" listKey="key"
												listValue="value" style="width:100%"/>
										</td>-->
										
									</tr>
								</table>
								<webflex:flexCheckBoxCol caption="选择" property="noticeId"
									showValue="noticeTitle" width="5%" isCheckAll="true"
									isCanDrag="false" isCanSort="false"></webflex:flexCheckBoxCol>
								<webflex:flexEnumCol caption="发送状态" mapobj="${noticeMap}"
									property="noticeIs" showValue="noticeIs" width="0%"
									isCanDrag="true" isCanSort="true"></webflex:flexEnumCol>
								<webflex:flexTextCol caption="通知标题" property="noticeTitle"
									showValue="noticeTitle" width="25%" isCanDrag="true"
									isCanSort="true"></webflex:flexTextCol>
								<webflex:flexTextCol caption="通知地址" property="noticeAddr"
									showValue="noticeAddr" width="25%" isCanDrag="true"
									isCanSort="true"></webflex:flexTextCol>
								<webflex:flexDateCol caption="开始时间" property="noticeStime"
									showValue="noticeStime" dateFormat="yyyy-MM-dd" width="15%"
									isCanDrag="true" isCanSort="true"></webflex:flexDateCol>
								<webflex:flexDateCol caption="结束时间" property="noticeEndTime"
									showValue="noticeEndTime" dateFormat="yyyy-MM-dd" width="15%"
									isCanDrag="true" isCanSort="true"></webflex:flexDateCol>
								<webflex:flexTextCol caption="发送状态" property="noticeId"
									showValue="javascript:showimg(noticeIs)" width="15%"
									isCanDrag="true" isCanSort="true"></webflex:flexTextCol>

							</webflex:flexTable>
						</td>
					</tr>
				</table>
			</s:form>
		</DIV>
		<script language="javascript">
var sMenu = new Menu();
function initMenuT(){
   $("input:checkbox").parent().next().hide(); //隐藏第二列
	sMenu.registerToDoc(sMenu);
	var item = null;
	item = new MenuItem("<%=root%>/images/ico/tb-add.gif","草拟通知","addTitle",1,"ChangeWidthTable","checkMoreDis");
	sMenu.addItem(item);
	item = new MenuItem("<%=root%>/images/ico/bianji.gif","编辑通知","editTitle",1,"ChangeWidthTable","checkMoreDis");
	sMenu.addItem(item);
	item = new MenuItem("<%=root%>/images/ico/bianji.gif","查看通知","viewTitle",1,"ChangeWidthTable","checkMoreDis");
	sMenu.addItem(item);
	item = new MenuItem("<%=root%>/images/ico/fasong.gif","发送通知","sendTitle",1,"ChangeWidthTable","checkOneDis");
	sMenu.addItem(item);
	item = new MenuItem("<%=root%>/images/ico/shanchu.gif","删除通知","deleteTitle",1,"ChangeWidthTable","checkOneDis");
	sMenu.addItem(item);
	item = new MenuItem("<%=root%>/images/ico/shengqing.gif","申请会议室","shenqinroom",1,"ChangeWidthTable","checkOneDis");
	sMenu.addItem(item);
	sMenu.addShowType("ChangeWidthTable");
    registerMenu(sMenu);
}

function addTitle(){
	var width=screen.availWidth/2;
    var height=screen.availHeight*2/3;
	var result=showModalDialog("<%=path%>/meetingmanage/meetingnotice/meetingnotice!input.action",window,"dialogWidth:" + width + "pt;dialogHeight:" + height + "pt;"+
		                                "status:no;help:no;scroll:no;");
  
}
function editTitle(){
	var id = getValue();
		var width=screen.availWidth/2;
    var height=screen.availHeight*2/3;
				if(id==null||id==""){
					alert("请选择要编辑的通知！");
				}else{
					if(id.indexOf(",")!=-1){
						alert("一次只能编辑一条通知！");
					}else{
					   var ss=$(":checked").parent().next().attr("value");	
					   if(ss!= 0){
					    alert("请选择未发送的通知进行编辑");
					   }else{
						var a = showModalDialog("<%=path%>/meetingmanage/meetingnotice/meetingnotice!input.action?notId="+id,window,"dialogWidth:" + width + "pt;dialogHeight:" + height + "pt;"+
		                                "status:no;help:no;scroll:no;");
							if(a=="reload"){
								location.reload();
							}
						}
					}
				}
}


function viewTitle(){
	var id = getValue();
		var width=screen.availWidth/2;
    var height=screen.availHeight*2/3;
				if(id==null||id==""){
					alert("请选择要查看的通知！");
				}else{
					if(id.indexOf(",")!=-1){
						alert("一次只能查看一条通知！");
					}else{
					     var a = showModalDialog("<%=path%>/meetingmanage/meetingnotice/meetingnotice!view.action?notId="+id,window,"dialogWidth:" + width + "pt;dialogHeight:" + height + "pt;"+
			                                "status:no;help:no;scroll:no;");
								if(a=="reload"){
									location.reload();
								}
							}
					}
}


function sendTitle(){
	var id=getValue();
		if(id==null || id==""){
		alert("请选择需要发送的通知!");
			return;
		}
		if(id.length >32){
		alert('一次只能发送一条记录!');
		return;
	}	
    var ss=$(":checked").parent().next().attr("value");
    //alert(ss);
	if(ss == 0){
		var width=screen.availWidth/2;
    	var height=screen.availHeight*2/3;
		var result=showModalDialog("<%=path%>/meetingmanage/meetingnotice/meetingnotice!input.action?notId="+id,window,"dialogWidth:" + width + "pt;dialogHeight:" + height + "pt;"+
		                                "status:no;help:no;scroll:no;");
	}else{
	
		alert('该会议通知已发送!')
	}
}
function deleteTitle(){
var id=getValue();
	if(id==null || id==""){
		alert("请选择需要删除的记录！");
			return;
		}
	var ss=$(":checked").parent().next().attr("value");	
	if(ss != 0){
		alert("该通知已发送，不允许删除!")
		return;
	}else{
		if(confirm("您确定要删除吗？")){
		
   			location="<%=path%>/meetingmanage/meetingnotice/meetingnotice!delete.action?notId="+id;	
   		}
	}	
}

function shenqinroom(){
  var id=getValue();
		if(id==null || id==""){
		alert("请选择需要申请会议室的通知！");
			return;
		}
		if(id.length >32){
		alert('一次只能申请一条记录!');
		return;
	}  
	var info = getInfo(id);
	
	var maMeetingdec=info[0];
	var calStartTime=info[1];
	var calEndTime=info[2];
	var nowdate=new Date();
	alert(nowdate);
	if(nowdate>calEndTime){
	 alert("会议时间已过，不能再申请会议室！");
			return;
	}
	var width=screen.availWidth*3/6;
    var height = screen.availHeight*2/4;
   	var url = "<%=path%>/meetingroom/meetingApply!applyRoom.action?model.maMeetingdec="+encodeURI(encodeURI(maMeetingdec))+"&model.maAppstarttime="+calStartTime+"&model.maAppendtime="+calEndTime;
	var a =showModalDialog(url,window,"dialogWidth:" + width + "pt;dialogHeight:" + height + "pt;"+
		                                "status:no;help:no;scroll:no;");
		if("reload"==a){
		///	parent.location="<%=path%>/meetingmanage/meetingnotice/meetingnotice.action";
		alert("申请会议室成功！");
		}		
}
//获取信息
function getInfo(id){
	var info = new Array();
	<s:iterator value="nopage.result">
		var pk = "${noticeId}";
		if(id == pk){
			info[0] = '${noticeTitle}';
			info[1] = '${noticeStime}';
			info[2] = '${noticeEndTime}';
		}
	</s:iterator>
	return info;
}
$(document).ready(function(){
        $("#img_sousuo").click(function(){
        	$("form").submit();
        });     
      });
  

</script>
	</BODY>
</HTML>
