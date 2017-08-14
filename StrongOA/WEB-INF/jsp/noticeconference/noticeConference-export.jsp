
<%@page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@taglib uri="/tags/web-flex" prefix="webflex"%>
<%@taglib uri="/tags/web-newdate" prefix="strong"%>

<%@include file="/common/include/rootPath2.jsp"%>
<%@taglib prefix="s" uri="/struts-tags"%>
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<title>导出参会成员</title>
		<base target="_self">
		<META http-equiv=Content-Type content="text/html; charset=utf-8">

		<link href="<%=frameroot%>/css/strongitmenu.css" type="text/css"
			rel="stylesheet">
		<LINK type=text/css rel=stylesheet
			href="<%=frameroot%>/css/search.css">

		<LINK href="<%=frameroot%>/css/properties_windows.css" type=text/css
			rel=stylesheet>
		<LINK href="<%=frameroot%>/css/search.css" type=text/css
			rel=stylesheet>
		<style type="text/css">
html {
	-webkit-box-sizing: border-box;
	-moz-box-sizing: border-box;
	box-sizing: border-box;
	padding: 40px 0px 40px 0px;
	overflow: hidden;
}

html,body {
	height: 100%;
}

#nobr br {
	display: none;
}

.information_list_choose_pagedownnew {
	position: absolute;
	width: 100%;
	height: 38px;
	text-align: right;
	left: 0;
	right: 0;
	bottom: 0;
	overflow: hidden;
	background: url(../image/wzfbbbg.gif) repeat-x center bottom;
}

.information_list span {
	display: inline;
	float: none;
	text-align: left;
	padding-right: 0px;
}

.information_list {
	border-collapse: collapse;
	border-spacing: 0;
}
</style>

		<LINK href="<%=frameroot%>/css/properties_windows.css" type=text/css
			rel=stylesheet>
		<LINK href="<%=frameroot%>/css/search.css" type=text/css
			rel=stylesheet>
		<script type="text/javascript"
			src="<%=request.getContextPath()%>/scripts/easyui-1.3/jquery.js"></script>
		<script type="text/javascript"
			src="<%=request.getContextPath()%>/scripts/easyui-1.3/jquery.easyui.min.js"></script>
		<script type="text/javascript">
		 
			/*  $(function(){
		   
		       $.ajax({
				  type: "post",
				  url: "<%=path%>/seatset/seatsetAttendMember!datagrid.action",
				  data: {
    			  		meetingId:'${meetingId}'
   			  		},
				  success: function(data){
   					 
   						    $("#table").append(data);
   						 
   			  		}
				});
		   
		   
		   
		   });*/
			function view(obj,num){
				var tr=document.getElementById("dv"+num);
				if(tr.style.display=="none"){
					tr.style.display="";
					obj.innerHTML="<b>－</b>";
				}else{
					tr.style.display="none";
					obj.innerHTML="<b>＋</b>";
				}
			}
			//返回重办
		   function reback(obj,id){
		      $("#consendId").val(id);
		     
		     $('#formid').form('submit',{
						   url: "<%=path%>/noticeconference/noticeConference!reBack.action",
						   onSubmit: function(){
							return true;
						},
						success: function(data){						      
							  if(data=='-1'){
							       alert("由于异步操作，该下发单位已被删除！！！");
							       return;
                                   
							  } else if(data=='ex'){
							     alert("操作失败！！！");
							     return;
							  }   
							  $(obj).parent().parent().find("td:eq(2)").html("<b>"+data+"</b>"); 
							   $(obj).parent().parent().find("td:eq(3)").html("");                 
						}	
					});
				 
		   
		   }
		   //办理确认
		    function sureTonext(obj,id){
		       $("#consendId").val(id);
		     
		      $('#formid').form('submit',{
						   url: "<%=path%>/noticeconference/noticeConference!sureToConsend.action",
						   onSubmit: function(){
							return true;
						},
						success: function(data){						      
							  if(data=='-1'){
							       alert("由于异步操作，该下发单位已被删除！！！");
							       return;
                                   
							  } else if(data=='ex'){
							     alert("操作失败！！！");
							     return;
							  }   
							  $(obj).parent().parent().find("td:eq(2)").html("<b>"+data+"</b>"); 
							   $(obj).parent().parent().find("td:eq(3)").html("");               
						}	
					});
		   
		   }
		 
			//自动刷新页面
			function reflush(){
				var obj=document.getElementById("reload");
				obj.href=window.location.href+"&random="+Math.random()+"&data="+new Date();
				obj.click();
			}	
			
			//导出参会人员 
		function exportmen(){
		 	 $('#formid').form('submit',{
						   url: '<%=root%>/noticeconference/noticeConference!exportMember.action',
						   onSubmit: function(){
							return true;
						},
						success: function(data){		
						    if(data=='1'){		      
							 alert("导出参会名单成功！！");
							 }else{
							 
							   alert("导出文件错误，原因："+data);
							 }
						}						
			 }); 
		}	
			
		//签到表
		function exportcheck(){
		 	 $('#formid').form('submit',{
						   url: '<%=root%>/noticeconference/noticeConference!exportCheck.action',
						   onSubmit: function(){
							return true;
						},
						success: function(data){		
						    if(data=='1'){		      
							 alert("导出参会名单成功！！");
							 }else{
							 
							   alert("导出文件错误，原因："+data);
							 }
						}						
			 }); 
		}

		</script>


	</HEAD>

	<BODY class=contentbodymargin oncontextmenu="return false;"
		style="overflow: scroll">

		<a id="reload"></a>
		<table width="100%" border="0" cellspacing="0"
											cellpadding="00">
			<tr>
				<td>
					&nbsp;&nbsp;<b>各单位报名情况</b>
				</td>
				<td>
					<a class="Operation" href="#" onclick="exportmen()">
						<img src="<%=root%>/images/ico/daochu.gif"  
								height="15" class="img_s"><span id="test" style="cursor:hand">导出参会人员</span> </a>
					<a class="Operation" href="#" onclick="exportcheck()">
						<img src="<%=root%>/images/ico/daochu.gif"  
								height="15" class="img_s"><span id="test1" style="cursor:hand">签到表</span> </a>
				</td>
				<td colspan=4 align="right">
					已报名单位：${reportSend} &nbsp;未签收单位：${waitSend}  &nbsp;已上报人数：${total}&nbsp;
				</td>
				<td>
				&nbsp;&nbsp;&nbsp;&nbsp;
				</td>
			</tr>
		</table>
		<div style="WIDTH: 100%; HEIGHT: 80%;OVERFLOW: scroll;">
           ${contentHtml}
        </div>
	

		<form id="formid" method="post">
			<input type="hidden" name="conNoticeId" id="conNoticeId"  value="${conNoticeId }"/>
		</form>
	</BODY>
 
</HTML>
