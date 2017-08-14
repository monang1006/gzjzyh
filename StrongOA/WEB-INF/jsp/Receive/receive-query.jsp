<%@page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@taglib uri="/tags/web-flex" prefix="webflex"%>
<%@taglib uri="/tags/web-newdate" prefix="strong"%>
<%@ taglib uri="/tags/security" prefix="security"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<%@include file="/common/include/rootPath.jsp"%>

<html>
	<head>
		<%@include file="/common/include/meta.jsp"%>
		<title>公文获取列表</title>
		<link href="<%=frameroot%>/css/strongitmenu.css" type="text/css"
			rel="stylesheet">
		<link href="<%=frameroot%>/css/properties_windows.css" type="text/css"
			rel="stylesheet">
		<link href="<%=frameroot%>/css/search.css" type="text/css"
			rel="stylesheet">
		<script language='javascript'
			src='<%=path%>/common/js/grid/ChangeWidthTable.js'
			type="text/javascript"></script>
		<script src="<%=path%>/common/js/jquery/jquery-1.2.6.js"
			type="text/javascript"></script>
		<script language="javascript" src="<%=path%>/common/js/menu/menu.js"
			type="text/javascript"></script>
		<script src="<%=path%>/common/js/common/common.js"
			type="text/javascript"></script>
		<script src="<%=path%>/common/js/common/search.js"
			type="text/javascript"></script>
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
	    			alert("请您选择要操作的文档");
	    			return false;
	    		}else{
	    			if(id.indexOf(",")!=-1){
	    				alert("一次仅能操作一个文档，请您重新选择");
	    				return false;
	    			}else{
	    				return true;
	    			}
	    		}
	    	}
	    	function chooseFlow(){
				var rValue=OpenWindow('<%=root%>/docafterflow/docafterflow!chooseWorkFlow.action', '300', '400', window);
				$("#info").html("<font color='red'>"+rValue+"</font>");
				//alert($("#info").attr("st"));
				$("#info").attr("st",rValue);
				//alert($("#info").attr("st"));
	    	}
			function getDoc(){
				var id = getValue();
				if(id==null||id==""){
					alert("请您选择要操作的文档");
				}else if($("#info").attr("st")==""){
					alert("请您选择您要进行公文流转的流程");
				}else{
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
				}
			}
			
			function viewDoc(){
				var id = getValue();
				if(chageone(id)==true){
					var rValue = OpenWindow('<%=root%>/docafterflow/docafterflow!view.action?mydocId='+id, '700', '400', window);
				}
			}
	    	$(document).ready(function(){
		        $("#img_sousuo").click(function(){
		        	$("#myTableForm").submit();
		        }); 
	    	});
	    	
	    	
	    	function chakan(){
	    		var id = getValue();
				if(id==null||id==""){
					alert("请您选择数据");
					return false;
				}
				if(id.indexOf(",") != -1){
					alert("一次只能查看一个");
					return false;
				}
				location="<%=root%>/Receive/receive!attachlist.action?model.recvDocId="+id;	
	    	}
	    	function save(){
				var id = getValue();
				if(id==null||id==""){
					alert("请您选择数据");
					return false;
				}
				$.ajax({
					  		type:"post",
					  		dataType:"text",
					  		url:"<%=root%>/Receive/receive!save.action?id="+id,
					  		data:"1=1",
					  		success:function(msg){
					  			if(msg == "1"){
						  			window.returnValue="1";
						  			if(window.opener != null ){//存在父页面
							  			window.opener.reloadPage();//刷新父页面
							  			window.close();
						  			}else{
						  				window.location.reload();
						  			}
					  			}else{
					  				if(window.opener != null ){//存在父页面
						  				alert('导入失败,系统将关闭当前界面！');
						  				window.close();
					  				}else{
					  					alert('导入失败,系统将刷新当前界面！');
						  				window.location.reload();
					  				}
					  			}
					  		}
			  	});
				//location="<%=root%>/Receive/receive!save.action?id="+id;	
			}
		</script>
		<base target="_self" />
	</head>
	<body class="contentbodymargin" oncontextmenu="return false;"
		onload="initMenuT();window.focus();">
		<script type="text/javascript"
			src="<%=path%>/common/js/newdate/WdatePicker.js"></script>
		<div id="contentborder" align="center">
			<table width="100%" border="0" cellspacing="0" cellpadding="0"
				style="vertical-align: top;">
				<tr>
					<td height="40" class="tabletitle">
						<table width="100%" border="0" align="right" cellpadding="0"
							cellspacing="0">
							<tr>
								<td>
									&nbsp;&nbsp;
									<img src="<%=frameroot%>/images/ico/ico.gif" width="9" height="9"
										alt="">
									&nbsp; 公文获取选择
								</td>
								<td width="30%" align="left">

								</td>

								<td width="120">
									<a class="Operation" href="#" onclick="chakan()"><img
											src="<%=root%>/images/ico/chakan.gif" width="15" height="20"
											class="img_s">查看附件&nbsp;</a>
								</td>
								<td width="5"></td>
								<td width="120">
									<a class="Operation" href="#" onclick="save()"><img
											src="<%=root%>/images/ico/daoru.gif" width="15" height="20"
											class="img_s">确认签收&nbsp;</a>
								</td>
								<td width="5"></td>
							</tr>
						</table>
					</td>
				</tr>
				<tr>
					<td height="100%">
						<form id="myTableForm"
							action="<%=root%>/Receive/receive!query.action" method="get">
							<webflex:flexTable name="myTable" width="100%" height="200px"
								wholeCss="table1" property="recvDocId" isCanDrag="true"
								isCanFixUpCol="true" clickColor="#A9B2CA" footShow="showCheck"
								getValueType="getValueByProperty" collection="${page.result}"
								page="${page}">
								<table width="30%" border="0" cellpadding="0" cellspacing="1"
									class="table1">
									<tr>
										<td width="7%" align="center" class="biao_bg1">
											<img id='img_sousuo' src="<%=root%>/images/ico/sousuo.gif"
												width="15" height="15" alt="">
										</td>
										<td width="93%" align="center" class="biao_bg1">
											<strong:newdate name="searchDate" id="searchDate"
												dateobj="${searchDate}" width="100%" skin="whyGreen"
												isicon="true" classtyle="search" title="输入创建日期"
												dateform="yyyy-MM-dd" />
										</td>
									</tr>
								</table>
								<webflex:flexCheckBoxCol caption="选择" property="recvDocId"
									showValue="recvDocId" width="5%" isCheckAll="true"
									isCanDrag="false" isCanSort="false"></webflex:flexCheckBoxCol>
								<webflex:flexTextCol caption="标题" property="recvdocTitle"
									showValue="recvdocTitle" isCanDrag="true" isCanSort="true"
									width="37%"></webflex:flexTextCol>
								<webflex:flexTextCol caption="文号" property="recvdocCode"
									showValue="recvdocCode" isCanDrag="true" isCanSort="true"
									width="10%"></webflex:flexTextCol>
								<webflex:flexTextCol caption="缓急" property="recvdocEmergency"
									showValue="recvdocEmergency" isCanDrag="true" isCanSort="true"
									width="8%"></webflex:flexTextCol>
								<webflex:flexTextCol caption="发文单位"
									property="recvdocSubmittoDepart"
									showValue="recvdocSubmittoDepart" isCanDrag="true"
									isCanSort="true" width="20%"></webflex:flexTextCol>

								<webflex:flexDateCol caption="创建时间"
									property="recvdocOfficialTime" showValue="recvdocOfficialTime"
									isCanDrag="true" dateFormat="yyyy-MM-dd" isCanSort="true"
									width="20%">
								</webflex:flexDateCol>
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
				item = new MenuItem("<%=root%>/images/ico/chakan.gif","查看附件","chakan",1,"ChangeWidthTable","checkMoreDis");
				sMenu.addItem(item);    
				item = new MenuItem("<%=root%>/images/ico/daoru.gif","确认签收","save",1,"ChangeWidthTable","checkOneDis");
				sMenu.addItem(item);    
				sMenu.addShowType("ChangeWidthTable");
				registerMenu(sMenu);
			}
		  </script>
	</body>
</html>
