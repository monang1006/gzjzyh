<%@page language="java" contentType="text/html; charset=UTF-8"  pageEncoding="UTF-8"%>
<%@taglib uri="/tags/web-flex" prefix="webflex"%>
<%@taglib uri="/tags/web-newdate" prefix="strong"%>
<%@ taglib uri="/tags/security" prefix="security"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<%@include file="/common/include/rootPath.jsp"%>

<html>
	<head>
		<%@include file="/common/include/meta.jsp"%>
		<title>公文列表</title>
		<link href="<%=frameroot%>/css/strongitmenu.css" type="text/css" rel="stylesheet">
		<link href="<%=frameroot%>/css/properties_windows_list.css" type="text/css" rel="stylesheet">
		<link href="<%=frameroot%>/css/search.css" type="text/css" rel="stylesheet">   
		<script language='javascript' src='<%=path%>/common/js/grid/ChangeWidthTable.js' type="text/javascript"></script>
		<script src="<%=path%>/common/js/jquery/jquery-1.2.6.js" type="text/javascript"></script>
		<script language="javascript" src="<%=path%>/common/js/menu/menu.js" type="text/javascript"></script>    
		<script src="<%=path%>/common/js/common/common.js" type="text/javascript"></script>
		<script src="<%=path%>/common/js/common/search.js" type="text/javascript"></script>
		<style media="screen" type="text/css">
		.tabletitle {
		  FILTER:progid:DXImageTransform.Microsoft.Gradient(
		                        gradientType = 0, 
		                        startColorStr = #ededed, 
		                        endColorStr = #ffffff);
		}
		
		.hand {
		  cursor:pointer;
		}
		</style>
		<script type="text/javascript">
			var subing = false;
			function chageone(id){
	    		if(id==null||id==""){
	    			alert("请您选择要查看的公文");
	    			return false;
	    		}else{
	    			if(id.indexOf(",")!=-1){
	    				alert("对不起,一次只能查看一份公文");
	    				return false;
	    			}else{
	    				return true;
	    			}
	    		}
	    	}
	    	function chooseFlow(){
				var id = getValue();
				if(id==null||id==""){
					alert("请您选择要操作的文档");
					return;
				}
	    	
				var rValue=OpenWindow('<%=root%>/docafterflow/docafterflow!chooseWorkFlow.action', '300', '400', window);
				
				if(rValue==null){
					
				}else{
				$("#info").html("<font color='red'>"+rValue+"</font>");
					//alert($("#info").attr("st"));
					$("#info").attr("st",rValue);
				}
				getDoc();
				//alert($("#info").attr("st"));
	    	}
			function getDocxx(){
				var id = getValue();
				//if(id==null||id==""){
				//	alert("请您选择要操作的文档");
				//}else if($("#info").attr("st")==""){
				//	alert("请您选择您要进行公文流转的流程");
				//	return;
				//}else{
					if(subing==false){
						subing=true;
 					  	$.ajax({
					  		type:"post",
					  		dataType:"text",
					  		url:"<%=root%>/docafterflow/docafterflow!sureGet.action?type=${type}",
					  		data:"mydocId="+id+"&workFlowName="+$("#info").attr("st"),
					  		success:function(msg){
								if(msg=="true"){
									window.close();
									window.returnValue="true";
								}else if(msg=="refresh"){
									window.location.href="<%=root%>/recvdoc/recvDoc.action";
								}else{
									subing=false;
									alert("确认分发失败，请您重新确认");
								}
								
					  		}
					  	});
					 }else{
					 	alert("正在签收请稍后...");
					 }
				//}
			}
			
			
			function getDoc(){
				var id = getValue();
				if(id==null||id==""){
					alert("请您选择要操作的文档");
					return;
			 	}
					if(subing==false){
						subing=true;
 					  	$.ajax({
					  		type:"post",
					  		dataType:"text",
					  		url:"<%=root%>/docafterflow/docafterflow!sureGetCheck.action?type=${type}",
					  		data:"mydocId="+id,
					  		success:function(msg){
								if(msg=="true"){
 									window.close();
									window.returnValue="true";
								}else if(msg=="refresh"){
 									//window.location.href="<%=root%>/recvdoc/recvDoc.action";
								    window.location.href="<%=root%>/docafterflow/docafterflow!showGetList.action";
								}else{
									subing=false;
									alert("确认分发失败，请您重新确认");
								}
								
					  		}
					  	});
					 }else{
					 	alert("正在签收请稍后...");
					 }
 			}
			function viewDoc(){
				var id = getValue();
				if(chageone(id)==true){
				   // OpenWindow('<%=root%>/docdis/docDis!view.action?docId='+id, '700', '400', window);
					var rValue = OpenWindow('<%=root%>/docafterflow/docafterflow!view.action?mydocId='+id, '650', '500', window);
				}
			}
	    	$(document).ready(function(){
		        $("#img_sousuo").click(function(){
		        	$("#myTableForm").submit();
		        }); 
	    	});
		</script>
		<base target="_self" />
	</head>
	<body class="contentbodymargin" oncontextmenu="return false;"  onload="initMenuT();">
		<script type="text/javascript" src="<%=path%>/common/js/newdate/WdatePicker.js"></script>
		<div id="contentborder" align="center">
			<table width="100%" border="0" cellspacing="0" cellpadding="0" style="vertical-align: top;">
		      <tr>
		         <td colspan="3" class="table_headtd">
		          <table width="100%" border="0" align="right" cellpadding="0" cellspacing="0">
		            <tr>
						<td class="table_headtd_img" >
						<img src="<%=frameroot%>/images/ico/ico.gif" >&nbsp;
						</td>
						<td align="left">
							<strong>待签收公文</strong>
						</td>
						<td align="right">
							<table border="0" align="right" cellpadding="00" cellspacing="0">
								<tr>
									<td width="4"><img src="<%=frameroot%>/images/bt_l.jpg"/></td>
				                 	<td class="Operation_list" onclick="viewDoc();"><img src="<%=root%>/images/operationbtn/view.png"/>&nbsp;查&nbsp;看&nbsp;公&nbsp;文&nbsp;</td>
				                 	<td width="4"><img src="<%=frameroot%>/images/bt_r.jpg"/></td>
			                  		<td width="5"></td>
			                  		<td width="4"><img src="<%=frameroot%>/images/bt_l.jpg"/></td>
				                 	<td class="Operation_list" onclick="getDoc();"><img src="<%=root%>/images/operationbtn/edit.png"/>&nbsp;确&nbsp;认&nbsp;签&nbsp;收&nbsp;</td>
				                 	<td width="4"><img src="<%=frameroot%>/images/bt_r.jpg"/></td>
			                  		<td width="5"></td>
		                  		</tr>
						</table>
						</td>
		            </tr>
		          </table>
		        </td>
		      </tr>
		      <tr>
		        <td height="100%">
		        <form id="myTableForm" action="<%=root%>/docafterflow/docafterflow.action" method="get" >
		          <webflex:flexTable name="myTable" width="100%" height="200px" wholeCss="table1" property="docId" isCanDrag="true"
		            isCanFixUpCol="true" clickColor="#A9B2CA" footShow="showCheck"
		            getValueType="getValueByProperty" collection="${page.result}"
		            page="${page}">
		            <table width="100%" border="0" cellpadding="0" cellspacing="0" class="table1_search">
		               <tr>
		              		 <td>
					       		&nbsp;&nbsp;公文标题：&nbsp;<input name="title" id="title" type="text" class="search" title="请您输入公文标题" value="${title}">
					       		&nbsp;&nbsp;开始日期：&nbsp;<strong:newdate  name="startDate" id="startDate" skin="whyGreen" isicon="true"  classtyle="search" title="请输入开始日期" dateform="yyyy-MM-dd" dateobj="${startDate}"/>
					       		&nbsp;&nbsp;结束日期：&nbsp;<strong:newdate  name="endDate" id="endDate" skin="whyGreen" isicon="true"  classtyle="search" title="请输入结束日期" dateform="yyyy-MM-dd" dateobj="${endDate}"/>
					       		&nbsp;&nbsp;&nbsp;&nbsp;<input id="img_sousuo" type="button" />
		                	</td>
		              </tr>
		            </table>
		            <webflex:flexCheckBoxCol caption="选择" property="docId"
		              showValue="toaDocDis.senddocTitle" width="5%" isCheckAll="true"
		              isCanDrag="false" isCanSort="false"></webflex:flexCheckBoxCol>
		            <webflex:flexTextCol caption="标题" property="toaDocDis.senddocTitle"
		              showValue="toaDocDis.senddocTitle" width="45%" isCanDrag="true"
		              isCanSort="true"></webflex:flexTextCol>
		            <webflex:flexTextCol caption="所属部门" property="deptname"
		              showValue="deptname" width="25%" isCanDrag="true"
		              isCanSort="true"></webflex:flexTextCol>
		            <webflex:flexDateCol caption="成文时间"
		              property="getDocDate" showValue="getDocDate"
		              width="25%" isCanDrag="true" dateFormat="yyyy-MM-dd HH:mm"  isCanSort="true"></webflex:flexDateCol>
		          </webflex:flexTable>
		          </form>
		        </td>
		      </tr>
			</table>
		</div>
		  <script language="javascript">
		    var sMenu = new Menu();
			function initMenuT(){
				sMenu.registerToDoc(sMenu);
				var item = null;
				item = new MenuItem("<%=root%>/images/operationbtn/view.png","查看公文","viewDoc",1,"ChangeWidthTable","checkMoreDis");
				sMenu.addItem(item);    
				item = new MenuItem("<%=root%>/images/operationbtn/edit.png","确认签收","getDoc",1,"ChangeWidthTable","checkOneDis");
				sMenu.addItem(item);    
				sMenu.addShowType("ChangeWidthTable");
				registerMenu(sMenu);
			}
		  </script>
	</body>
</html>
