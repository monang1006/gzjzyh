<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib prefix="s" uri="/struts-tags"%>
<%@ taglib uri="/tags/web-newdate" prefix="strong"%>
<%@include file="/common/include/rootPath.jsp"%>
<%@include file="/common/eformOCX/version.jsp"%>
<html>
	<head>
		<title>工作流高级查询</title>
		<%@include file="/common/include/meta.jsp"%>
		<link href="<%=frameroot%>/css/properties_windows.css" type="text/css" rel="stylesheet">
		<script src="<%=path%>/common/js/jquery/jquery-1.2.6.js" type="text/javascript"></script>
		<script src="<%=path%>/common/js/common/common.js" type="text/javascript"></script>
		<script type="text/javascript">
			$(document).ready(function(){
				var formId = '${formId}';
				if(formId!="0"){
					var eform = document.getElementById("FormInputOCX");
			        eform.InitialData('<%=basePath%>/services/EFormService?wsdl');
			        eform.SetFormTemplateID(formId);
			        //.width和.height必须是小写
			  	    eform.width=eform.MaxWidth;//设置电子表单OCX的宽度为内表单最度的宽度
	  		        eform.height=eform.MaxHeight;//设置电子表单OCX的高度为内表单最高的高度
	  		        $("#tdforminfo").hide();
				}else{
					$("#tdforminfo").show();
				}
				//验证选择的人员是否超过一个
				document.getElementById("orguserid").attachEvent("onpropertychange",function(obj){
					if(obj.propertyName!="value"){
						return ;
					}
					var userId = $("#orguserid").val();
					if(userId.split(",").length>1){
						alert("发起人只能指定一个！");
						$("#orguserid").val("")
						$("#orgusername").val("");
					}
				});
				//根据流程名称获取流程所挂接的查询表单
				$("#selectWorkflow").change(function(){
					$.post("<%=root%>/work/work!getFormByWorkflow.action",
							{workflowName:encodeURI($(this).val())},
							function(data){
								var formId = data;
								if(formId!="0"){
									$("#tdobj").show();
					  		        $("#tdforminfo").hide();
									var FormInputOCX = document.getElementById("FormInputOCX");
							        FormInputOCX.InitialData('<%=basePath%>/services/EFormService?wsdl');
							        FormInputOCX.SetFormTemplateID(formId);
							        //.width和.height必须是小写
							  	    FormInputOCX.width=FormInputOCX.MaxWidth;//设置电子表单OCX的宽度为内表单最度的宽度
					  		        FormInputOCX.height=FormInputOCX.MaxHeight;//设置电子表单OCX的高度为内表单最高的高度
					  		        $("#formId").val(formId);
								}else{
									$("#tdobj").hide();
									$("#tdforminfo").show();
								}
							});
				});
			});
			//选择范围
			function selectScope(){
				var scope = $("#scope").val();
				if(scope == "3"){
					$("#spanUser").show();
				}else{
					$("#spanUser").hide();
				}
			}
			//选择人员
			function selectUser(){
				var ret=OpenWindow("<%=root%>/address/addressOrg!tree.action","600","400",window);
			}
			//显示隐藏表单
			function showOrhide(){
				var formDisplay = $("#trforminfo").css("display");
				if(formDisplay == 'none'){
					$("#trforminfo").show();
				}else{
					$("#trforminfo").hide();
				}
			}
			//返回
			function goback(){
				var workflowId = "${workflowId}";
				var workflowType = "${workflowType}";
				if(workflowId != ""){//从work-advancedsearch.jsp链接过来
					location = "<%=root%>/work/work!advancedSearch.action?workflowType=null";
				}else{//从工作查询直接链接进来
					location = "<%=root%>/work/work!query.action?workflowType="+workflowType;
				}
			}
			//获取组装的SQL语句,并执行查询
			function getSqlAndSubmit(){
				var FormInputOCX = document.getElementById("FormInputOCX");
				var formId = $("#formId").val();
				var sql = "";
				var startDate = $("#startDate").val();
				var endDate = $("#endDate").val();
				if(endDate != ""){
					if(date2Number(startDate) > date2Number(endDate)){
						alert("开始日期不能大于结束日期！");
						return ;
					}
				}
				
				//第一次进入此页面
				if(formId == ""){
					<s:if test="eformFieldLst!=null&&eformFieldLst.size()>0">
						sql = "select * from ";
						<s:iterator value='eformFieldLst' status="index">
							<s:if test="#index.index==0">
								sql += '${tablename }'+" where 1=1 ";
							</s:if>
							var fieldValue = FormInputOCX.GetControlValue("val_"+"${fieldname }");
							var controlValue = FormInputOCX.GetControlValue("if_"+"${fieldname }");
							if(fieldValue!="" && controlValue!=""){
								sql += " and "+'${fieldname}'+ " {" + controlValue +"|"+ fieldValue + "|"+'${type}'+"}";
							}
						</s:iterator>
					</s:if>
					$("#sql").val(sql);
					$("form").submit();
				}else{
					//还需要根据FormId获取表单中的字段域并组装SQL语句,这里通过JQUERY回传JSON格式字符流
					$.getJSON("<%=root%>/work/work!getFieldByFormId.action",
					  		        		  {formId:formId},
					  		        		  function(jsons){
					  		        		  	sql = "select * from ";
					  		        		  	$.each(jsons,function(i,json){
					  		        		  		if(i == 0){
					  		        		  			sql += json['tablename'] + " where 1=1 ";
					  		        		  		}
					  		        		  		var fieldValue = FormInputOCX.GetControlValue("val_"+json['fieldname']);
													var controlValue = FormInputOCX.GetControlValue("if_"+json['fieldname']);
													if(fieldValue!="" && controlValue!=""){
														sql += " and "+json['fieldname']+ " {" + controlValue +"|"+ fieldValue + "|"+json['type']+"}";
													}
					  		        		  	});
					  		        		  	$("#sql").val(sql);
												$("form").submit();
					  		        		  });
				}
			}
			
		 //转换时间格式(yyyy-MM-dd HH:mm)--->(yyyyMMddHHmm)
         function date2Number(stime){
         	var arrsDate1 = stime.split('-');
         	stime = arrsDate1[0] + "" + arrsDate1[1] + "" + arrsDate1[2];
         	try{
         		stime = parseInt(stime);
         	}catch(e){}
         	return stime;
         }
		</script>	
	</head>

	<body style="margin-top:5px;background-color: white;" >
	  <script type="text/javascript" src="<%=path%>/common/js/newdate/WdatePicker.js"></script>
		<div id="contentborder" >
			<s:form action="/work/work!doAdvancedSearch.action">
				<s:hidden name="sql" id="sql"></s:hidden>
				<s:hidden id="formId"></s:hidden><!-- 根据切换表单,来组装SQL语句 -->
				<table align="center" width="100%" border="0" cellpadding="0" cellspacing="1" class="table1">
					<tr style="background-color: #e3eef2;border: thick;">
				        <td colspan="2"><img src="<%=frameroot%>/images/ico/ico.gif" width="9" height="9">&nbsp;&nbsp;&nbsp;&nbsp;工作流程基本属性</td>
					</tr>
					<tr>
						<td width="15%" height="21" class="biao_bg1" align="right"><span class="wz">流程名称：</span></td>
						<td  class="td1">
							<select id="selectWorkflow" name="workflowName" style="width:115px;">
								<s:iterator value="typeList" var="workflowName">
						    	 	<option value="<s:property value='workflowName' />"><s:property value="workflowName"/></option>
								</s:iterator>  
							</select>
						</td>
					</tr>
					<tr>
						<td width="15%" height="21" class="biao_bg1" align="right"><span class="wz">工作流状态：</span></td>
						<td  class="td1">
							<select name="flow_status" style="width:115px;">
								<option value="0">所有</option>
								<option value="1">执行中</option>
								<option value="2">已结束</option>
							</select>
						</td>
					</tr>
					<tr>
						<td width="15%" height="21" class="biao_bg1" align="right"><span class="wz">查找范围：</span></td>
						<td  class="td1">
							<select id="scope" name="flow_query_type" style="width:115px;" onchange="JavaScript:selectScope();">
								<option value="0">所有范围</option>
								<option value="1">我经办的</option>
								<option value="2">我发起的</option>
								<option value="3">指定发起人</option>
							</select>
							&nbsp;
							<span id="spanUser" style="display:none;">
								<s:textfield id="orgusername" name="userName" disabled="true"></s:textfield>
								<s:hidden id="orguserid" name="userId"></s:hidden>
								&nbsp;
								<input type="button" class="input_bg" onclick="JavaScript:selectUser();" value="选 择"/>
							</span>
						</td>
					</tr>
					<tr>
						<td width="15%" height="21" class="biao_bg1" align="right"><span class="wz">流程开始日期范围：</span></td>
						<td  class="td1">
							从 <strong:newdate id="startDate" name="startDate" skin="whyGreen" isicon="true" dateform="yyyy-MM-dd"/> 至 <strong:newdate id="endDate" name="endDate" skin="whyGreen" isicon="true" dateform="yyyy-MM-dd"/>
						</td>
					</tr>
					<tr style="background-color: #e3eef2;border: thick;">
				        <td colspan="2"><img src="<%=frameroot%>/images/ico/ico.gif" width="9" height="9">&nbsp;&nbsp;&nbsp;&nbsp;表单数据条件&nbsp;&nbsp;
				        	<a href="#" onclick="JavaScript:showOrhide();"><font color="blue"><b>显示/隐藏条件</b></font></a>
				        </td>
					</tr>
					<tr id="trforminfo" style="display: ;background-color: white;">
						<td id="tdobj" align="center" colspan="2">
							<%--<script type="text/javascript">
								var formId = '${formId}';
								if(formId!="0"){
									document.write('<object classid="clsid:750B2722-ADE6-446A-85EF-D9BAEAB8C423"');
									document.write('codebase="<%=root%>/common/eformOCX/FormInputOCX.CAB<%=OCXVersion%>" id="FormInputOCX">');
									document.write('<param name="Visible" value="0" /><param name="Font" value="MS Sans Serif" /><param name="PixelsPerInch" value="96" />');
									document.write('<param name="PrintScale" value="1" /><param name="Scaled" value="-1" /><param name="DropTarget" value="0" />');
									document.write('<param name="HelpFile" value="" /><param name="ScreenSnap" value="0" /><param name="SnapBuffer" value="10" />');
									document.write('<param name="DoubleBuffered" value="0" /><param name="Enabled" value="-1" /><param name="AutoScroll" value="1" />');
									document.write('<param name="AutoSize" value="0" /><param name="AxBorderStyle" value="0" /><param name="Color" value="4278190095" /></object>');
								}
							</script>--%>
							<object classid="clsid:750B2722-ADE6-446A-85EF-D9BAEAB8C423"
								codebase="<%=root%>/common/eformOCX/FormInputOCX.CAB<%=OCXVersion%>"
								id="FormInputOCX">
								<param name="Visible" value="0" />
								<param name="Font" value="MS Sans Serif" />
								<param name="KeyPreview" value="0" />
								<param name="PixelsPerInch" value="96" />
								<param name="PrintScale" value="1" />
								<param name="Scaled" value="-1" />
								<param name="DropTarget" value="0" />
								<param name="HelpFile" value="" />
								<param name="ScreenSnap" value="0" />
								<param name="SnapBuffer" value="10" />
								<param name="DoubleBuffered" value="0" />
								<param name="Enabled" value="-1" />
								<param name="AutoScroll" value="1" />
								<param name="AutoSize" value="0" />
								<param name="AxBorderStyle" value="0" />
								<param name="Color" value="4278190095" />
							</object>
						</td>
					</tr>
					<tr>
						<td colspan="2" align="center" class="td1">
							<input type="button" class="input_bg" onclick="JavaScript:getSqlAndSubmit();" value="查询列表" />
							<input type="button" class="input_bg" onclick="JavaScript:goback();" value="返 回" />
						</td>
					</tr>
					<tr>
						<td id="tdforminfo" colspan="2" align="center" class="td1" style="display: none;">
							<font color='red' size='4'><b>流程未挂接查询表单！</b></font>
						</td>
					</tr>
				</table>
			</s:form>
		</div>	
	</body>
</html>
