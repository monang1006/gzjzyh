<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="/tags/web-flex" prefix="webflex"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib uri="/tags/web-newdate" prefix="strong"%>
<%@ include file="/common/include/rootPath.jsp"%>
<html>
	<head>
	  <%@include file="/common/include/meta.jsp"%>
		<title>流程模型</title>		
		<link href="<%=frameroot%>/css/strongitmenu.css" type="text/css" rel="stylesheet">
		<link href="<%=frameroot%>/css/properties_windows_list.css" type="text/css" rel="stylesheet">
      	<LINK href="<%=frameroot%>/css/search.css" type=text/css rel=stylesheet>
		<script language='javascript' src='<%=root%>/common/js/grid/ChangeWidthTable.js' type="text/javascript"></script>
		<script language="javascript" src="<%=root%>/common/js/menu/menu.js" type="text/javascript"></script>
		<script src="<%=path%>/common/js/jquery/jquery-1.2.6.js" type="text/javascript"></script>
		<script src="<%=path%>/common/js/upload/jquery.blockUI.js" language="javascript"></script>	
		<script src="<%=path%>/common/js/common/common.js" type="text/javascript"></script>	
		<script src="<%=path%>/common/js/common/search.js" type="text/javascript"></script>	
	<script type="text/javascript">
	$(document).ready(function(){
			$("input").focus();
		}); 
	function judgeDeploy(pfIsDeploy,pfIsEdit){
	  var str;
		if(pfIsDeploy == "0"){
			str = "未部署";
		}else if(pfIsDeploy == "1" && pfIsEdit == "1"){
			str = "最新版本未部署";
		}else{
			str = "<font color='red'>已部署</font>";
		}
		return str;
	}
	
	function displayProcessTypeName(processTypeName){
		if(processTypeName == "twfInfoProcessType.ptName"){
			return "未指定";
		}else{
			return processTypeName;
		}
	}
	
	function doSubmit(){
		$("#pfCreator").val(encodeURI($("#searchPfCreator").val()));
		$("#pfName").val(encodeURI($("#searchPfName").val()));
		//  搜索状态标示
		$("#issearch").val("1");
		document.getElementById("myTableForm").submit();
	}
</script>
	</head>
	<body class="contentbodymargin" onload="initMenuT();">
		<%--<script type="text/javascript" src="<%=path%>/common/js/newdate/WdatePicker.js"></script>--%>
		<script type="text/javascript" src="<%=path%>/common/script/My97DatePicker/WdatePicker.js"></script>
		<div id="contentborder" align="center">
			<table width="100%" border="0" cellspacing="0" cellpadding="0" style="vertical-align: top;">
				<tr>
					<td height="100%">
						<table width="100%" border="0" cellspacing="0" cellpadding="0">
							<tr>
								<td height="40"
									style="FILTER:progid:DXImageTransform.Microsoft.Gradient(gradientType=0,startColorStr=#ededed,endColorStr=#ffffff);">
									<table width="100%" border="0" cellspacing="0" cellpadding="00">
										<tr>
											<td class="table_headtd_img" >
												<img src="<%=frameroot%>/images/ico/ico.gif" >&nbsp;
											</td>
											<td align="left">
												<strong>流程模型列表</strong>
											</td>
											<td align="right">
												<table border="0" align="right" cellpadding="00" cellspacing="0">
													<tr>
														<td width="4"><img src="<%=frameroot%>/images/bt_l.jpg"/></td>
									                 	<td class="Operation_list" onclick="processFileAdd();"><img src="<%=root%>/images/operationbtn/add.png"/>&nbsp;新&nbsp;建&nbsp;</td>
									                 	<td width="4"><img src="<%=frameroot%>/images/bt_r.jpg"/></td>
								                  		<td width="5"></td>
								                  		<td width="4"><img src="<%=frameroot%>/images/bt_l.jpg"/></td>
									                 	<td class="Operation_list" onclick="editProcessFile();"><img src="<%=root%>/images/operationbtn/edit.png"/>&nbsp;编&nbsp;辑&nbsp;</td>
									                 	<td width="4"><img src="<%=frameroot%>/images/bt_r.jpg"/></td>
								                  		<td width="5"></td>
								                  		<td width="4"><img src="<%=frameroot%>/images/bt_l.jpg"/></td>
									                 	<td class="Operation_list" onclick="editProcess();"><img src="<%=root%>/images/operationbtn/Design.png"/>&nbsp;设&nbsp;计&nbsp;</td>
									                 	<td width="4"><img src="<%=frameroot%>/images/bt_r.jpg"/></td>
								                  		<td width="5"></td>
								                  		<td width="4"><img src="<%=frameroot%>/images/bt_l.jpg"/></td>
									                 	<td class="Operation_list" onclick="reDesignProcess();"><img src="<%=root%>/images/operationbtn/Re_design.png"/>&nbsp;重设计&nbsp;</td>
									                 	<td width="4"><img src="<%=frameroot%>/images/bt_r.jpg"/></td>
								                  		<td width="5"></td>
								                  		<td width="4"><img src="<%=frameroot%>/images/bt_l.jpg"/></td>
									                 	<td class="Operation_list" onclick="deployProcess();"><img src="<%=root%>/images/operationbtn/Deployment.png"/>&nbsp;部&nbsp;署&nbsp;</td>
									                 	<td width="4"><img src="<%=frameroot%>/images/bt_r.jpg"/></td>
								                  		<td width="5"></td>
								                  		<td width="4"><img src="<%=frameroot%>/images/bt_l.jpg"/></td>
									                 	<td class="Operation_list" onclick="delProcess();"><img src="<%=root%>/images/operationbtn/del.png"/>&nbsp;删&nbsp;除&nbsp;</td>
									                 	<td width="4"><img src="<%=frameroot%>/images/bt_r.jpg"/></td>
								                  		<td width="5"></td>
								                  		<td width="4"><img src="<%=frameroot%>/images/bt_l.jpg"/></td>
									                 	<td class="Operation_list" onclick="manageVersion();"><img src="<%=frameroot%>/images/tb-destroy.png"/>&nbsp;历史版本&nbsp;</td>
									                 	<td width="4"><img src="<%=frameroot%>/images/bt_r.jpg"/></td>
								                  		<td width="5"></td>
														</td>
														<%--<td width="110">
															<a class="Operation" href="javascript:mountQueryForm();"><img class="img_s" src="<%=root%>/images/ico/bianji.gif" width="12"
																height="12" alt="">挂接查询表单 </a>
														</td>
														<td width="5">
														<td width="110">
															<a class="Operation" href="javascript:mountViewForm();"><img class="img_s" src="<%=root%>/images/ico/bianji.gif" width="12"
																height="12" alt="">挂接展现表单 </a>
														</td>
														<td width="5">
														</td>
													--%>
													</tr>
												</table>
											</td>
										</tr>
									</table>
								</td>
							</tr>
						<tr><td>
						<form id="myTableForm" action="<%=root%>/workflowDesign/action/processFile.action" method="get">
							<s:hidden id="pfName" name="model.pfName" ></s:hidden>	
							<s:hidden id="pfCreator" name="model.pfCreator"></s:hidden>	
							<s:hidden id="issearch" name="issearch" value=""></s:hidden>	
							<webflex:flexTable name="myTable" width="100%" height="370px"
								wholeCss="table1" property="pfId" isCanDrag="true"
								isCanFixUpCol="true" clickColor="#A9B2CA" footShow="showCheck"
								getValueType="getValueByProperty" collection="${page.result}"
								page="${page}">
							<table width="100%" border="0" cellpadding="0" cellspacing="1" class="table1_search">
					                <tr>
					                  <td>
					                    <div style="float: left;width:260px;">
							       		&nbsp;&nbsp;流程文件名称：&nbsp;<input name="searchPfName"  id="searchPfName" type="text" class="search" title="请输入流程文件名称" value="${model.pfName}">
							       		</div>
							       		<div style="float: left;width:240px;">
							       		&nbsp;&nbsp;创建人：&nbsp;<input name="searchPfCreator" id="searchPfCreator"  type="text" class="search" title="请输入创建人" value="${model.pfCreator}">
							       		</div>
							       		<div style="float: left;width:190px;">
							       		&nbsp;&nbsp;起始日期：&nbsp;<strong:newdate name="startDate" id="searchstart" classtyle="search" title="请输入起始日期" skin="whyGreen" isicon="true" dateform="yyyy-MM-dd" dateobj="${startDate}"/>
							       		</div>
							       		<div style="float: left;width:190px;">
							       		&nbsp;&nbsp;结束日期：&nbsp;<strong:newdate name="endDate" id="searchend" classtyle="search" title="请输入结束日期" skin="whyGreen" isicon="true" dateform="yyyy-MM-dd" dateobj="${endDate}"/>
							       		</div>
							       		<div style="float: left;width:240px;padding-top: 5px;">
					                  	<%
					                  		List workflowTypeList = (List)request.getAttribute("workflowTypeList");
					                  		String workflowType = (String)request.getAttribute("workflowType");
					                  		if(workflowTypeList != null) {
					                  			out.println("&nbsp;&nbsp;流程类型：&nbsp;<select onchange='doSubmit();'  name=\"workflowType\">");
					                  			out.println("<option value=\"\">全部</option>");
					                  			for(int i=0;i<workflowTypeList.size();i++){
					                  				Object[] objs = (Object[])workflowTypeList.get(i);
					                  				if(workflowType != null && workflowType.equals(String.valueOf(objs[0]))){
					                  					out.println("<option selected=\"selected\" value=\""+objs[0]+"\">");
					                  					out.println(objs[1]);
					                  					out.println("</option>");
					                  				}else{
					                  					out.println("<option value=\""+objs[0]+"\">");
					                  					out.println(objs[1]);
					                  					out.println("</option>");
					                  				}
					                  			}
					                  			out.println("</select>");
					                  		}
					                  	%>
					                  	</div>
					                  	<div style="float: left;width:240px;padding-top: 3px;">
					                  	&nbsp;&nbsp;状态：&nbsp;<s:select name="model.pfIsDeploy"  list="#{'':'全部','0':'未部署','1':'已部署'}" listKey="key" listValue="value" onchange='doSubmit();'/>			       		
							       		&nbsp;&nbsp;&nbsp;&nbsp;<input id="img_sousuo" type="button" />
							       		</div>
							       		</td>
					                </tr>
					              </table>
								<webflex:flexCheckBoxCol caption="选择" property="pfId"
									showValue="pfName" width="3%" isCheckAll="true"
									isCanDrag="false" isCanSort="false"></webflex:flexCheckBoxCol>
								<webflex:flexTextCol caption="流程文件名称" property="pfName"
									showValue="pfName" width="27%" isCanDrag="true"
									isCanSort="true"></webflex:flexTextCol>
								<webflex:flexDateCol caption="创建时间" width="20%" isCanDrag="true"
									isCanSort="true" showsize="20" property="pfCreatedate"
									showValue="pfCreatedate" dateFormat="yyyy-MM-dd HH:mm:ss"></webflex:flexDateCol>
								<webflex:flexTextCol caption="创建人" property="pfCreator"
									width="10%" showValue="pfCreator" isCanDrag="true"
									isCanSort="true"></webflex:flexTextCol>
								<webflex:flexTextCol caption="表单" showsize="25" property="formName"
									width="15%" showValue="formName" isCanDrag="true"
									isCanSort="true"></webflex:flexTextCol>	
								<webflex:flexTextCol caption="流程类型" property="ptName"
									width="15%" showValue="ptName" isCanDrag="true"
									isCanSort="true"></webflex:flexTextCol>
								<webflex:flexTextCol caption="是否部署" showsize="25" property="pfIsDeploy"
									width="10%" showValue="pfIsDeploy"
									isCanDrag="true" isCanSort="true"></webflex:flexTextCol>
							</webflex:flexTable>
						</form>
						</td>
						</tr>
						</table>
					</td>
				</tr>
			</table>
		</div>
		<script language="javascript" type="text/javascript">
			var sMenu = new Menu();
			var ow ;
			function initMenuT(){
				$("#pfCreator").val(encodeURI($("#searchPfCreator").val()));
				$("#pfName").val(encodeURI($("#searchPfName").val()));
				$("#img_sousuo").click(function(){
					var st = $("#searchstart").val()
					var se = $("#searchend").val()
					st = st.replace("-","").replace("-","");
					se = se.replace("-","").replace("-","");
					if((st!=""&&se!="")&&st>se){
						alert("结束时间不能早于开始时间。");
						return ;
					}else{
						doSubmit();
					}
				});
				sMenu.registerToDoc(sMenu);
				var item = null;
				item = new MenuItem("<%=root%>/images/operationbtn/add.png","新建","processFileAdd",1,"ChangeWidthTable","checkMoreDis");
				sMenu.addItem(item);
				item = new MenuItem("<%=root%>/images/operationbtn/edit.png","编辑","editProcessFile",1,"ChangeWidthTable","checkOneDis");
				sMenu.addItem(item);
				item = new MenuItem("<%=root%>/images/operationbtn/Design.png","设计","editProcess",1,"ChangeWidthTable","checkOneDis");
				sMenu.addItem(item);
				item = new MenuItem("<%=root%>/images/operationbtn/Re_design.png","重设计","reDesignProcess",1,"ChangeWidthTable","checkOneDis");
				sMenu.addItem(item);
				item = new MenuItem("<%=root%>/images/operationbtn/Deployment.png","部署","deployProcess",1,"ChangeWidthTable","checkMoreDis");
				sMenu.addItem(item);			
				item = new MenuItem("<%=root%>/images/operationbtn/del.png","删除","delProcess",1,"ChangeWidthTable","checkMoreDis");
				sMenu.addItem(item);
				/*item = new MenuItem("<%=root%>/images/ico/tb-change.gif","挂接查询表单","mountQueryForm",1,"ChangeWidthTable","checkMoreDis");
				sMenu.addItem(item);
				item = new MenuItem("<%=root%>/images/ico/tb-change.gif","挂接展现表单","mountViewForm",1,"ChangeWidthTable","checkMoreDis");
				sMenu.addItem(item);*/
				
				item = new MenuItem("<%=frameroot%>/images/tb-destroy.png","历史版本","manageVersion",1,"ChangeWidthTable","checkOneDis");
				sMenu.addItem(item);

				sMenu.addShowType("ChangeWidthTable");
			    registerMenu(sMenu);
			}

			//页面变量
			var returnvalue = "";
			
			function processFileAdd(){
				var url="<%=root%>/workflowDesign/action/processFile!input.action";
				var returnvalue = window.showModalDialog(url,"processFileAddWindow","dialogWidth:350pt;dialogHeight:550pt;status:no;help:no;scroll:no;status:0;help:0;scroll:0;");
				if(returnvalue == true){
					location = "<%=path%>/workflowDesign/action/processFile.action";
				}
			}
	
			//挂接查询表单
			function mountQueryForm(){
				mountForm("query");
			}
			//挂接展现表单
			function mountViewForm(){
				mountForm("view");
			}
			//挂接表单{type:query|查询表单；view|展现表单}
			function mountForm(type){
				var id = getValue();
				if(id == ""){
					alert("请选择要挂接表单的流程！");
					return ;
				}else{
					var ids = id.split(",");
					if(ids.length>1){
						alert("一次只能为一个流程挂接表单！");
						return ;
					}
				}
				var workflowName = $(":checked").parent().next().text();
				var ReturnStr=OpenWindow("<%=root%>/work/work!queryFormLst.action?workflowId="+id+"&workflowName="+encodeURI(encodeURI(workflowName))+"&remindType="+type, 
			                             "500", "420", window);
			}	
	
			//修改流程定义记录
			function editProcessFile(){
				var id = getValue();
				if(id == ""){
					alert("请选择要编辑的记录。");
					return ;
				}else{
					var ids = id.split(",");
					if(ids.length>1){
						alert("一次只能操作一条记录。");
						return ;
					}
				}
				var url = scriptroot + "/workflowDesign/action/processFile!input.action?pfId=" + id+"&timestamp="+new Date();
				var returnvalue = window.showModalDialog(url,"processFileEditWindow","dialogWidth:350pt;dialogHeight:550pt;status:no;help:no;scroll:no;status:0;help:0;scroll:0;")
				if(returnvalue == true){
					location = "<%=path%>/workflowDesign/action/processFile.action?pageNo=" + ${page.pageNo};
				}	
			}
			
			/**
				校验对列表的操作。非空和选择多条记录的情况。
				@param:id 选择记录的主键ID
				@param:onlyEmpty 是否只是校验非空情况。true:只校验非空;false:同时校验选择多条记录的情况。
			*/
			function checkOpt(id,onlyEmpty){
				if(id == ""){
					alert("请选择记录。");
					return false;
				}else{
					if(!onlyEmpty){
						if(id.length>32){
							alert("一次只能操作一条记录。");
							return false;
						}
					}
				}
			}
			function show(i){
				$.blockUI({ overlayCSS: { backgroundColor: '#B3B3B3'}});
				$.blockUI({ message: '<span class="wz">'+i+'</span>',css:{width:'160px',height:'25px'}});
				
			}
			function hidden(){
				$.unblockUI();
			}
			
			//重设计
		function reDesignProcess(){
		  var id = getValue();
		  if(id == ""){
				alert("请选择要重设计的记录。");
				return ;
			}else{
				var ids = id.split(",");
				if(ids.length>1){
					alert("一次只能操作一条记录。");
					return ;
				}
			}
		  var url = scriptroot + "/workflowDesign/action/processDefinition!input.action?pfId=" + id + "&type=redesign";
		 // window.open(url,'processDefinitionRedesign','height=600, width=1000, top=84, left=183, toolbar=no, ' + 
		  //							'menubar=no, scrollbars=yes, resizable=yes,location=no, status=no');
		  var returnvalue = window.showModalDialog(url,"processDefinitionRedesign","dialogWidth:9999pt;dialogHeight:9999pt;status:no;help:no;scroll:no;status:0;help:0;scroll:0;");
		  if(returnvalue == true){
		  		$("#pfName").val(encodeURI($("#searchPfName").val()));
			  location = "<%=path%>/workflowDesign/action/processFile.action?pageNo=" + ${page.pageNo} + "&pfName="+$('#pfName').val();
			 // returnvalue = "";
		  }		  
		}
			
			//设计
		function editProcess(){
          var id = getValue();
          if(id == ""){
				alert("请选择要设计的记录。");
				return ;
			}else{
				var ids = id.split(",");
				if(ids.length>1){
					alert("一次只能操作一条记录。");
					return ;
				}
			}
          
          var type = "edit";
          var isDeploy = getColValue(id, 6);          
          if(isDeploy == "0"){
        	  type = "redesign";
          }
          
		  var url = scriptroot + "/workflowDesign/action/processDefinition!input.action?pfId=" + id + "&type="+type;		  		
		 // window.open(url,'processDefinitionEdit','height=600, width=1000, top=84, left=183, toolbar=no, ' + 
		  //							'menubar=no, scrollbars=yes, resizable=yes,location=no, status=no');		  							
		 var returnvalue = window.showModalDialog(url,"processDefinitionEdit","dialogWidth:9999pt;dialogHeight:9999pt;status:no;help:no;scroll:no;status:0;help:0;scroll:0;")
		 if(returnvalue == true){
			$("#pfName").val(encodeURI($("#searchPfName").val()));
		  	location = "<%=path%>/workflowDesign/action/processFile.action?pageNo=" + ${page.pageNo} + "&pfName="+$('#pfName').val();
			//  returnvalue = "";
		  }		  
		}		
			
			function delProcess(){
			  var ids = getValue();
			  if(ids == ""){
					alert("请选择要删除的记录。");
					return ;
				}
			 $.post("<%=path%>/workflowDesign/action/processFile!checkWorkflowIsDel.action",
	           {"workflowId":ids},
	           function(data){         
			    if(data=="0"){																			//当前流程没有被引用过
			     	  var ret = confirm("确认要删除吗？");
			          if (ret) {
			            location = "<%=path%>/workflowDesign/action/processFile!delete.action?ids=" + ids;	
			          }		  
			    }else if(data=="-1"){
			    	alert('删除流程异常。');
		      		return;
			    }else{																					//当前流程已被引用，如果删除将导致该流程定义下的流程信息全部丢失
			    	 var ret = confirm("流程已被引用，如做删除操作将导致该流程定义下的流程信息全部丢失，是否继续操作？");
			          if (ret) {
			            location = "<%=path%>/workflowDesign/action/processFile!delete.action?ids=" + ids;	
			          }		  
	<%--		    	alert('当前流程【'+data+"】已引用还没有办毕，不能做删除操作！");--%>
	<%--	      		return;--%>
			    }	
		  	 });	         
			}
			
			function deployProcess(){
			  var ids = getValue();
			  //var editName = $("input:checked").parent().parent().children().eq(2).html();
			  if(ids == ""){
					alert("请选择要部署的记录。");
					return ;
				}
				/**if (confirm("确定要部署吗？")) {
					show("请稍等，部署中...");
					//部署后依然显示为当前页
					location = "<%=path%>/workflowDesign/action/processFile!deploy.action?ids=" + ids + "&pageNo=" + ${page.pageNo};
					//hidden();
				  }	**/		
			   $.post("<%=path%>/workflowDesign/action/processFile!ischeck.action",
	           		{"ids":ids}, function(data){  
	           			if(data!=""){
	           				alert("请先挂接流程表单再部署。");
	           			}else{
	           				if (confirm("确定要部署吗？")) {
	           				    show("请稍等，部署中...");
								//部署后依然显示为当前页
					            location = "<%=path%>/workflowDesign/action/processFile!deploy.action?ids=" + ids + "&pageNo=" + ${page.pageNo};		
							  }
	           			}
	           });
			}	
			
			/*
			*历史版本
			*/
			function manageVersion(){
			    var id = getValue();
			    if(id == ""){
						alert("请选择要查看历史版本的记录。");
						return ;
					}else{
						var ids = id.split(",");
						if(ids.length>1){
							alert("一次只能操作一条记录。");
							return ;
						}
					}
				location = "<%=path%>/workflowDesign/action/processDefinitionHistoryVersion.action?pfId="+id;
			}
			
			function getColValue(pfId, index){
				return $("tr[value="+pfId+"] > td:eq("+index+")").attr("value");
			}

			
				$(window).resize(function(){
				 var id=$("#contentborder table:first");
				  var width=$(window).width();
				  if(width<=784){
					  id.css("width","784px");
				  }else{
					  id.css("width","100%");
				  }
			});		
	</script>
</body>
</html>
<script type="text/javascript">
<!--
	if('${errorMessage}' != ''){
		alert('${errorMessage}');
	}
//-->
</script>