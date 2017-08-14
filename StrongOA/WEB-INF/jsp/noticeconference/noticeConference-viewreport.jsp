
<%@page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@taglib uri="/tags/web-flex" prefix="webflex"%>
<%@taglib uri="/tags/web-newdate" prefix="strong"%>

<%@include file="/common/include/rootPath2.jsp"%>
<%@taglib prefix="s" uri="/struts-tags"%>
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<title>参会成员列表</title>
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
		     var conAll = $("conAll").val();
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
			
			function getValue(id){
				var sid = $("#conAll").val();
				if($("#"+id).attr("checked")=="checked"){
					sid = sid + id +",";
				}
				else{
					sid = sid.replace(id + "," ,"");
				}
				$("#conAll").val(sid);
			}
			
			
			
			//批量办理确认
		    function sure(){
		     var conAll = $("#conAll").val();
		     if(conAll==""){
		    	 alert("最少选择一项进行办理!");
		    	 return false;
		     }
		     var con = conAll.split(",");
		     
		      $('#formid').form('submit',{
						   url: "<%=path%>/noticeconference/noticeConference!sureToConsend.action?conAll="+conAll,
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
							  for(var i=0;i<con.length;i++){
							  $("#"+con[i]).parent().parent().find("td:eq(2)").html("<b>"+data+"</b>"); 
							   $("#"+con[i]).parent().parent().find("td:eq(3)").html("");     
							  }
						}	
					});
		   
		   }
			
			function check(){
				var conAll = "";
				if($("#chkall").attr("checked")=="checked"){
				 $("[name = chkItem]:checkbox").attr("checked", true);
				 $("[name = chkItem]:checkbox").each(function () {
					 conAll = conAll + $(this).attr("value") +",";
				 });
				}
				else{
					$("[name = chkItem]:checkbox").attr("checked", false);
					conAll="";
				}
				$("#conAll").val(conAll);
			}

		</script>


	</HEAD>

	<BODY class=contentbodymargin oncontextmenu="return false;"
		style="overflow: scroll">

		<a id="reload"></a>
		<table width="100%" border="0" cellspacing="0"
											cellpadding="00">
			<tr>
				<td colspan=3>
					&nbsp;&nbsp;<b>各单位报名情况</b>
				</td>
				<td colspan=3 align="right">
					已报名单位：${reportSend} &nbsp;未签收单位：${waitSend}  &nbsp;已上报人数：${total}&nbsp;<input type="button" value="办理确认" onclick="sure()">
				</td>
				<td>
				&nbsp;&nbsp;&nbsp;&nbsp;
				</td>
			</tr>
		</table>
		<div style="WIDTH: 100%; HEIGHT: 95%;OVERFLOW: scroll;">
           ${contentHtml}
        </div>
		

		<form id="formid" method="post">
			<input type="hidden" name="consendId" id="consendId" />
			<input type="hidden" name="conAll" id="conAll" />
		</form>
	</BODY>
	<script type="text/javascript">
	
	
	//删除成员分类
	function deleteCategory(categoryId){
	    $("#categoryId").val(categoryId);
		if(confirm("该操作将删除所选成员分类及下属所有参会成员，确定吗？")){
		     $('#formid').form('submit',{
						   url: "<%=path%>/provinconference/seat/seatManager!delete.action",
						   onSubmit: function(){
							return true;
						},
						success: function(data){						      
							  if(data=='true'){
							        var prewin=window.dialogArguments;
                                    //reflush(); 
                                     window.close();
                                    prewin.viewMeeting();  
                                   
							  } else{
							     alert("成员类型删除错误");
							  }                      
						}	
					});
				 
		    
		    
			//location="<%=path%>/seatset/seatsetCategory!deleteCategory.action?categoryId="+categoryId;
			//reflush();
		}
	}	
	
	//编辑成员分类
	function editCategory(categoryId){
		var url = "<%=path%>/seatset/seatsetCategory!edit.action?categoryId="+categoryId;
     	var ret =window.showModalDialog(url,window,"dialogWidth=450px;dialogHeight=300px"); 
     	if(null!=ret && ""!=ret){
     		reflush();
     		//window.location.reload();
     	}
	}		
	</script>
</HTML>
