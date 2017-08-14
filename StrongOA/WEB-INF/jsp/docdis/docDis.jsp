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
		<title>公文分发列表</title>
		<link href="<%=frameroot%>/css/strongitmenu.css" type="text/css"
			rel="stylesheet">
		<link href="<%=frameroot%>/css/properties_windows_list.css" type="text/css"
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
			function chageRecordState(id){
				var notDis = '${notDis}';
				var notReadOver = '${notReadOver}';
				if(notDis.indexOf(id+",")>=0){//还没有进行分发
					return "";
				}
				if(notReadOver.indexOf(id+",")>=0){
					return "<font color=red>未签收完</font>";
				}
				return "签收完毕";
			}
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
	    	
	    	function distribution(){
	    		var id = getValue();
	    		if(chageone(id)==true){
				  	$.ajax({
				  		type:"post",
				  		dataType:"text",
				  		url:"<%=root%>/docdis/docDis!charge.action",
				  		data:"docId="+id,
				  		success:function(msg){
							if(msg=="1"){	//内部和外部都已分发
								alert("已经被分发，不能够进行再次分发操作")
							}else{
								//if(msg!=null&&msg=="1"){		//外部已分发
								//	distributeType="orgAndUserTree"		//展现部门人员树
								//}else{	
									distributeType="companyTree"		//展现机构树树
								//}
								// window.open ('<%=root%>/docdis/docDis!orgTree.action?docId='+id+'&type='+msg+'&distributeType='+distributeType, 'newwindow', "height=300, width=300,toolbar=no, menubar=no, scrollbars=no, resizable=no,  status=no")
								var rValue=OpenWindow('<%=root%>/docdis/docDis!orgTree.action?docId='+id+'&type='+msg+'&distributeType='+distributeType, '300', '300', window);
								//var rValue=OpenWindow('<%=root%>/fileNameRedirectAction.action?toPage=docdis/docDis!selectType.jsp?docId='+id+'&type='+msg, '250', '300', window);
								if(rValue=="true"){
									window.location.reload();
								}
							}
				  		},
						error:function(data){
							alert("对不起，操作异常"+data);
						}
				  	});
	    		}
	    	}
	    	
	    	function statistics(){
	    		var id = getValue();
	    		//OpenWindow('<%=root%>/docdis/docDis!gotoCount.action?docId='+id, '700', '400', window);
	    		if(chageone(id)==true){
				  	$.ajax({
				  		type:"post",
				  		dataType:"text",
				  		url:"<%=root%>/docdis/docDis!charge.action",
				  		data:"docId="+id,
				  		success:function(msg){
							if(msg=="1"||msg=="3"){
								//top.perspective_content.actions_container.personal_properties_toolbar.navigate('<%=root%>/docdis/docDis!gotoCount.action?docId='+id,'统计');
								window.open('<%=root%>/docdis/docDis!gotoCount.action?docId='+id, '700', '400', window);
							}else{
								alert("未进行外部分发，不可以进行统计");
							}
				  		}
				  	});
	    		}
	    	}
	    	//彭小青添加
	    	function innerstatistics(){
	    		var id = getValue();
	    		if(chageone(id)==true){
				  	$.ajax({
				  		type:"post",
				  		dataType:"text",
				  		url:"<%=root%>/docdis/docDis!charge.action",
				  		data:"docId="+id,
				  		success:function(msg){
							if(msg=="2"||msg=="3"){
 								window.open('<%=root%>/docdis/docDis!gotoCountOfInner.action?docId='+id, '700', '400', window);
							}else{
								alert("未进行内部分发，不可以进行统计");
							}
				  		}
				  	});
	    		}
	    	}
	    	
	    	function edit(){
	    		var id = getValue();
	    		if(chageone(id)==true){
					$.ajax({
				  		type:"post",
				  		dataType:"text",
				  		url:"<%=root%>/docdis/docDis!charge.action",
				  		data:"docId="+id,
				  		success:function(msg){
							if(msg=="1"){
								alert("已经被分发，不能够进行编辑操作")
							}else{	
								location="<%=root%>/docdis/docDis!input.action?docId="+id;
							}
				  		},
						error:function(data){
							alert("对不起，操作异常"+data);
						}
				  	});
			  	}
			}
	    	
	    	function readdoc(){
	    		var id = getValue();
	    		if(chageone(id)==true){
	    			OpenWindow('<%=root%>/docdis/docDis!view.action?docId='+id, '650', '500', window);
	    		}
	    	}
	    	
	    	function changeContent(info){
	    		if(info==null||info==""||info=="null"||info=="0"){
	    			return "未分发";
	    		}else if(info=="1"){
	    			return "已分发";
	    		}else{
	    			return "";
	    		}
	    	}
	    	
	    	$(document).ready(function(){
		        $("#img_sousuo").click(function(){
		        	$("#myTableForm").submit();
		        }); 
	    	});
	    	
	    	function add(){
	    		location="<%=root%>/docdis/docDis!input.action";
	    	}
	    	
	    	//删除文档
	      function del() {
	          var id = getValue();
	          if(id==null||id==""){
	    			alert("请您选择要操作的文档");
	    			return;
	    	   }
	    	   if(confirm("您是否要真的删除文件呢？")==true){
	    	   $.ajax({
				  		type:"post",
				  		dataType:"text",
				  		url:"<%=root%>/docdis/docDis!charge.action",
				  		data:"docId="+id,
				  		success:function(msg){
							if(msg=="1"){
								alert("已经被分发，不能够进行删除操作")
							}else{	
								$.ajax({
							  		type:"post",
							  		dataType:"text",
							  		url:"<%=root%>/docdis/docDis!notRealDelete.action",
							  		data:"docId="+id,
							  		success:function(msg){
										if(msg=="true"){
											window.location.reload();
										}else{	
											alert("删除操作异常，请您与技术支持人员联系");
										}
							  		},
									error:function(data){
										alert("对不起，操作异常"+data);
									}
					  			});
							
 							}
				  		},
						error:function(data){
							alert("对不起，操作异常"+data);
						}
				  	});
	    	   
	    	   
	    	   
	    	   
	    	  		
			   }
	      }
	    </script>
	</head>
	<body class="contentbodymargin" oncontextmenu="return false;"
		onload="initMenuT();">
		<script type="text/javascript"
			src="<%=path%>/common/js/newdate/WdatePicker.js"></script>
		<div id="contentborder" align="center">
			<table width="100%" border="0" cellspacing="0" cellpadding="0"
				style="vertical-align: top;">
				<tr>
					<td colspan="3" class="table_headtd">
						<table width="100%" border="0" align="right" cellpadding="0"
							cellspacing="0">
							<tr>
								 <td class="table_headtd_img" >
									<img src="<%=frameroot%>/images/ico/ico.gif" >&nbsp;
								</td>
								<td align="left">
									<strong>公文分发列表</strong>
								</td>
								<td align="right">
									<table border="0" align="right" cellpadding="00" cellspacing="0">
										<tr>
											<td width="4"><img src="<%=frameroot%>/images/bt_l.jpg"/></td>
						                 	<td class="Operation_list" onclick="distribution();"><img src="<%=root%>/images/operationbtn/Distribution.png"/>&nbsp;分&nbsp;发&nbsp;</td>
						                 	<td width="4"><img src="<%=frameroot%>/images/bt_r.jpg"/></td>
					                  		<td width="5"></td>
					                  		<td width="4"><img src="<%=frameroot%>/images/bt_l.jpg"/></td>
						                 	<td class="Operation_list" onclick="statistics();"><img src="<%=root%>/images/operationbtn/fenfatongji.gif"/>&nbsp;外&nbsp;部&nbsp;分&nbsp;发&nbsp;统&nbsp;计&nbsp;</td>
						                 	<td width="4"><img src="<%=frameroot%>/images/bt_r.jpg"/></td>
					                  		<td width="5"></td>
					                  		<td width="4"><img src="<%=frameroot%>/images/bt_l.jpg"/></td>
						                 	<td class="Operation_list" onclick="add();"><img src="<%=root%>/images/operationbtn/add.png"/>&nbsp;新&nbsp;建&nbsp;</td>
						                 	<td width="4"><img src="<%=frameroot%>/images/bt_r.jpg"/></td>
					                  		<td width="5"></td>
					                  		<td width="4"><img src="<%=frameroot%>/images/bt_l.jpg"/></td>
						                 	<td class="Operation_list" onclick="edit();"><img src="<%=root%>/images/operationbtn/edit.png"/>&nbsp;编&nbsp;辑&nbsp;</td>
						                 	<td width="4"><img src="<%=frameroot%>/images/bt_r.jpg"/></td>
					                  		<td width="5"></td>
					                  		<td width="4"><img src="<%=frameroot%>/images/bt_l.jpg"/></td>
						                 	<td class="Operation_list" onclick="del();"><img src="<%=root%>/images/operationbtn/del.png"/>&nbsp;删&nbsp;除&nbsp;</td>
						                 	<td width="4"><img src="<%=frameroot%>/images/bt_r.jpg"/></td>
					                  		<td width="5"></td>
					                  		<td width="4"><img src="<%=frameroot%>/images/bt_l.jpg"/></td>
						                 	<td class="Operation_list" onclick="readdoc();"><img src="<%=root%>/images/operationbtn/view.png"/>&nbsp;查&nbsp;看&nbsp;</td>
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
						<s:form id="myTableForm" action="/docdis/docDis.action">
							<webflex:flexTable name="myTable" width="100%" height="200px"
								wholeCss="table1" property="senddocId" isCanDrag="true"
								isCanFixUpCol="true" clickColor="#A9B2CA" footShow="showCheck"
								getValueType="getValueByProperty" collection="${page.result}"
								page="${page}">
								 <table width="100%" border="0" cellpadding="0" cellspacing="0" class="table1_search">
									<tr>
										<td>
											&nbsp;&nbsp;标题：&nbsp;<input name="title" id="title" type="text" class="search" title="请您输入标题" value="${title}">
								       		&nbsp;&nbsp;分发状态：&nbsp;<s:select name="state" 
													list="#{'-1':'全部','0':'未分发','1':'已分发'}" listKey="key"
													listValue="value" />
								       		&nbsp;&nbsp;成文开始日期：&nbsp;<strong:newdate  name="startDate" id="startDate" skin="whyGreen" isicon="true"  classtyle="search" title="请输入成文开始日期" dateform="yyyy-MM-dd" dateobj="${startDate}"/>
								       		&nbsp;&nbsp;成文结束日期：&nbsp;<strong:newdate  name="endDate" id="endDate" skin="whyGreen" isicon="true"  classtyle="search" title="请输入成文结束日期" dateform="yyyy-MM-dd" dateobj="${endDate}"/>
								       		&nbsp;&nbsp;签收状态：&nbsp;<s:select name="signState" 
													list="#{'-1':'全部','0':'未签收完','1':'签收完毕'}"  listKey="key"
													listValue="value" />
								       		&nbsp;&nbsp;&nbsp;&nbsp;<input id="img_sousuo" type="button" />
								       		</td>
									</tr>
								</table>
								<webflex:flexCheckBoxCol caption="选择" property="senddocId"
									showValue="senddocTitle" width="3%" isCheckAll="true"
									isCanDrag="false" isCanSort="false"></webflex:flexCheckBoxCol>
								<webflex:flexTextCol caption="标题" property="senddocTitle"
									showValue="senddocTitle" width="39%" isCanDrag="true"
									isCanSort="true" showsize="30"></webflex:flexTextCol>
								<webflex:flexTextCol caption="是否外部分发" property="senddocDic"
									showValue="javascript:changeContent(senddocDic)" width="18%"
									isCanDrag="true" isCanSort="true"></webflex:flexTextCol>
								<webflex:flexDateCol caption="成文时间"
									property="senddocOfficialTime" showValue="senddocOfficialTime"
									width="22%" isCanDrag="true" dateFormat="yyyy-MM-dd HH:mm"
									isCanSort="true"></webflex:flexDateCol>
								<webflex:flexTextCol caption="外部分发状态" property="isDistribute"
									showValue="javascript:chageRecordState(senddocId)" width="18%"
									isCanDrag="true" isCanSort="true"></webflex:flexTextCol>
							</webflex:flexTable>
						</s:form>
					</td>
				</tr>
			</table>
		</div>
		<script language="javascript">
	    var sMenu = new Menu();
		function initMenuT(){
			sMenu.registerToDoc(sMenu);
			var item = null;
			item = new MenuItem("<%=root%>/images/operationbtn/Distribution.png","分发","distribution",1,"ChangeWidthTable","checkMoreDis");
			sMenu.addItem(item);    
			item = new MenuItem("<%=root%>/images/operationbtn/fenfatongji.gif","外部分发统计","statistics",1,"ChangeWidthTable","checkOneDis");
			sMenu.addItem(item);
			item = new MenuItem("<%=root%>/images/operationbtn/add.png","新增","add",1,"ChangeWidthTable","checkOneDis");
			sMenu.addItem(item);    
			item = new MenuItem("<%=root%>/images/operationbtn/edit.png","编辑","edit",1,"ChangeWidthTable","checkOneDis");
			sMenu.addItem(item); 
			item = new MenuItem("<%=root%>/images/operationbtn/del.png","删除","del",1,"ChangeWidthTable","checkOneDis");
			sMenu.addItem(item);   
			item = new MenuItem("<%=root%>/images/operationbtn/view.png","查看","readdoc",1,"ChangeWidthTable","checkMoreDis");
			sMenu.addItem(item);   
			sMenu.addShowType("ChangeWidthTable");
			registerMenu(sMenu);
		}
		
	
	  </script>
	</body>
</html>
