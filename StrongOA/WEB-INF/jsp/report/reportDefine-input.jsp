<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<%@include file="/common/include/rootPath.jsp"%> 
<% 
	response.setHeader("Cache-Control","no-store");
	response.setHeader("Pragrma","no-cache");
	response.setDateHeader("Expires",0);
%>
<html>
	<head>
	<%@include file="/common/include/meta.jsp" %>
		<title>报表定义</title>
		<link rel="stylesheet" type="text/css" href="<%=frameroot%>/css/properties_windows_add.css">
		<script language='javascript' src='<%=path%>/common/js/grid/ChangeWidthTable.js' type="text/javascript"></script>
    <script src="<%=path%>/common/js/jquery/jquery-1.2.6.js" type="text/javascript"></script>
    <script language="javascript" src="<%=path%>/common/js/menu/menu.js" type="text/javascript"></script>
    <script src="<%=path%>/common/js/validate/checkboxvalidate.js" type="text/javascript"></script>
    <script src="<%=path%>/common/js/common/common.js" type="text/javascript"></script>
    <!--<script src="<%=path%>/common/js/common/search.js" type="text/javascript"></script>--> 
	<SCRIPT LANGUAGE="JavaScript" src="<%=path%>/common/js/commontab/service.js"></SCRIPT>
	<SCRIPT LANGUAGE="JavaScript" src="<%=path%>/common/js/commontab/workservice.js"></SCRIPT>
	<style>

		.table tr{ background-color:expression('#FFFFFF,#E7F3FF'.split(',')[rowIndex>=0&&rowIndex%2])}
		
	</style>

	
		<style type="text/css">
			 body, table, tr, td,div{
			    margin:0px;
			}
		</style>
		<script type="text/javascript">
		String.prototype.trim = function() {
                var strTrim = this.replace(/(^\s*)|(\s*$)/g, "");
                strTrim = strTrim.replace(/^[\s　\t]+|[\s　\t]+$/, ""); 
                return strTrim;
            }
            
		$(document).ready(function() {
			var definitionFormid="${model.definitionFormid}";
			if(definitionFormid!=null&&definitionFormid!=""&&definitionFormid!="undefined"){
				document.getElementById("workflowDiv").style.display="block";
				
			}else{
				document.getElementById("showItem").style.display="none";
			}
			//添加报表类型
		$("#addSort").click(function(){
			var ret=OpenWindow(this.url,"550","400",window);
		});
		
		//添加表单
		$("#addEform").click(function(){
			var ret=OpenWindow(this.url,"550","400",window);
			if(ret!=null&&ret!=""&&ret.length>1){	
				var definitionFormid=$("#definitionFormid").val();
		
				$.getJSON("<%=root%>/report/reportDefine!getShowitemList.action",
						  {definitionFormid:definitionFormid},
						  function(showitemInfo){
						  	if(showitemInfo == "-1"){
						  		show("对不起，加载数据异常！");
						  	}else{
						  		var htmlStr="";
						  		htmlStr+='<tr bgColor="#EBEBEB" onselectstart="return false">';
						           htmlStr+='<td align="center" width="30%"><div class="title" style="width:100%;height:25px;background-color: #EBEBEB;">字段显示名称</div></td>';
						           htmlStr+='<td align="center" width="10%"><div class="title" style="width:100%;height:25px;background-color: #EBEBEB;">是否统计</div></td>';
						           htmlStr+='<td align="center" width="10%"><div class="title" style="width:100%;height:25px;background-color: #EBEBEB;">排序方式</div></td>';
						           htmlStr+='<td align="center" width="10%"><div class="title" style="width:100%;height:25px;background-color: #EBEBEB;">显示顺序</div></td>';
						           htmlStr+='<td align="center" width="10%"><div class="title" style="width:100%;height:25px;background-color: #EBEBEB;">删除</div></td>	';
						           htmlStr+='<td align="center" width="30%"><div class="title" style="width:100%;height:25px;background-color: #EBEBEB;"></div></td>';
						           htmlStr+=' </tr>  ';
						  		var isNum="onkeyup=\"this.value=this.value.replace(/\D/g,'')\" onafterpaste=\"this.value=this.value.replace(/\D/g,'')\""
							  	$.each(showitemInfo,function(i,showitem){
							  		 
							           
							  			htmlStr=htmlStr+"<tr>";
										
										//htmlStr=htmlStr+"<td align='center' width='25%' ><div class='cdata' style='width:100%;height:25px'>"+showitem.showitemName+"</div></td>";
										
										
										
										htmlStr=htmlStr+"<td align='center' width='30%' ><div class='cdata' style='width:100%;height:25px'><input type='text' id='text1' name='text1' maxlength='45' size='50' onblur='showitemTextIsHas(this);' value='"+showitem.showitemText+"' class='tableinput'/>"
										
										htmlStr=htmlStr+"<input type='hidden' id='showitemId' name='showitemId' value=''>";
										
										htmlStr=htmlStr+"<input type='hidden' id='showitemName' name=''showitemName' value='"+showitem.showitemName+"'>";
										
										htmlStr=htmlStr+"</div></td>";
										
										htmlStr=htmlStr+"<td align='center' width='10%' ><div class='cdata' style='width:100%;height:25px'>"+
												"<select id='text2' class='tableinput' name='text2'><option value='1'  >是</option><option value='0' selected='selected' >否</option></select></div></td>";
												
										htmlStr=htmlStr+"<td align='center' width='10%' ><div class='cdata' style='width:100%;height:25px'>"+
												"<select id='text3' class='tableinput' name='text3'><option value='0' selected='selected' >空</option><option value='-1' >降序</option><option value='1' >升序</option></select></div></td>";
										htmlStr=htmlStr+"<td align='center' width='10%' ><div class='cdata' ><input type='text' id='text4' name='text4' onkeyup='sequenceIsNum(this)' onafterpaste='sequenceIsNum(this)'   maxlength='5' size='6' value='"+(i+1)+"' class='tableinput'  /></div></td>";
										
										htmlStr=htmlStr+" <td align='center' width='10%' ><div class='cdata' style='width:100%;height:25px'><a onclick='delShowItem(this.parentElement.parentElement.parentElement.rowIndex)'  href='#'><img  src='<%=root%>/images/ico/shanchu.gif' border='0' /></a> </div></td>";
										
										htmlStr=htmlStr+"	<td align='center' width='30%' ></td>";
										
										htmlStr=htmlStr+"</tr>";
							  	});
	
								$("#showItemInfo_Table").html(htmlStr);
								//$("#Id_TableofOut").show();
								document.getElementById("showItem").style.display="block";
						  	}
				});
			}
		});
		
		//添加工作流
		
		$("#addWorkflow").click(function(){
			var definitionFormid=$("#definitionFormid").val();
			var url="<%=root%>/report/reportDefine!checkWorkflow.action?definitionFormid="+definitionFormid+"&time="+(new Date());
			var ret=OpenWindow(url,"300","400",window);
		});
      });      
		 
		 //保存
		 function save(){
		 	var definitionName = $("#definitionName").val();
		 	definitionName = definitionName.trim();
		 	
		 	var sortId=$("#sortId").val();					//报表类型ID
		 	var definitionFormid=$("#definitionFormid").val();		//表单ID
		 	var definitionWorkflowid=$("#definitionWorkflowid").val();	//工作流ID
		 	var definitionId= $("#definitionId").val(); 
		 	
		 	if(definitionFormid==null||definitionFormid==""||definitionFormid=="null"){
		 		alert("请选择表单!");
		 		return ;
		 	}
		 	if(sortId==null||sortId==""||sortId=="null"){
		 		alert("请选择报表类型!");
		 		return ;
		 	}
		 	if(definitionWorkflowid==null||definitionWorkflowid==""||definitionWorkflowid=="null"){
		 		alert("请选择表单相关工作流!");
		 		return ;
		 	}
		 	
		 	if(""==definitionName){
		 		alert("请输入定义报表名称!");
		 		$("#definitionName").val("");
		 		$("#definitionName").focus();
		 		return ;
		 	}else{
		 		
			 	var boo=showitemData(); 
			 	if(boo){
			 	 $.post('<%=path%>/report/reportDefine!isHasDefinitionName.action',
				             { 'definitionName':definitionName,'definitionId':definitionId},
				              function(data){
				              if(data=='1'){
				              	alert("定义报表名已存在，请重新输入!");
						 		$("#definitionName").val("");
						 		$("#definitionName").focus();
						 		return ;
				              }else{
				              	
				              	var showitemData=$("#showitemData").val();
				              	showitemData=showitemData.trim();    
				              	if(showitemData!=null&&showitemData!=""){ 
									form.submit();              	
				              	}else{
				              		alert("表单显示项有问题，请重新确认！");
				              		return ;
				              	} 
				              	    
				              }
				       });		 	
			 	
			 	}
		 		
		 	}
		 }
		 
		 //组装显示项数据
		 function showitemData(){
			
			 var boo=false;
		 	var rownum = document.getElementById("showItemInfo_Table").rows;		//获取
		 	 if(rownum.length == 0){
			     alert("报表显示项不能为空！");
			     return;
			 }else{
			 	 var colnum;
			 	 var showitemArr = new Array();
				 if(!(rownum.length==""))
				 {
				     colnum= document.getElementById("showItemInfo_Table").rows[0].cells.length;
				 }
				
				 var ret = "[";
				
				 
				 for(var i = 0; i < rownum.length-1; i++){				 	
				 	//ret =ret+ "{showitemId:'"+rownum[i].all("showitemId").value+"'";
				 	
				 	ret =ret+ "{showitemId:''";
				 	ret=ret+ ",";
				 	ret = ret +"showitemName:'"+$('[id=showitemName]')[i].value+"'";
				 	ret = ret +",";
				 	
				 	var showitemText=$('[id=text1]')[i].value;
				 	//alert(showitemText);
				 	showitemText=showitemText.trim();
				 	
				 	
				 	if(showitemText!=null&&showitemText!=""){
					 	ret =ret + "showitemText:'"+showitemText+"'";
					 	ret =ret + ",";
					 	boo=true;
				 	}else{
				 		alert("报表显示项不能为空，请重新确认!");
				 		break;
				 	}
				 	ret =ret + "showitemIstotal:'"+$('[id=text2]')[i].value+"'";
					ret =ret + ",";
					
					ret =ret + "showitemOrderby:'"+$('[id=text3]')[i].value+"'";
					ret =ret + ",";
					ret =ret + "showitemSequence:'"+$('[id=text4]')[i].value+"'";
					ret =ret + "},";	

				 }
				ret = ret.substring(0,ret.length-1);
				ret = ret + "]";
				if(boo){
					$("#showitemData").val(ret);
				}
				 
			 }
			 return boo;
		 }
		 
		//取消
		function goBack(){
			window.location="<%=path%>/report/reportDefine.action";
		} 
		//排序顺序是否为数字
		function sequenceIsNum(obj){
			var showitemSequence=obj.value;
			var IsNum= /^[0-9]*[1-9][0-9]*$/;  
			if(showitemSequence!=null&&showitemSequence!=""){
				if(!IsNum.test(showitemSequence)){
			 		alert("排序号只能输入正整数!");
			 		obj.value="";
			 		obj.focus();
			 		return ;
			 	}		 	
			}else{
				//alert("请输入排序号!");
				//	obj.focus;
			 	//	return ;
			}
			
		}
	//定义报表名，不能为空，相同
	function isHasDefinitionName(value){
		var definitionId= $("#definitionId").val(); 		
		 if(value!=null&&value!=""){
			 $.post('<%=path%>/report/workflowReportSort!isHasDefinitionName.action',
			             { 'definitionName':value,'definitionId':definitionId},
			              function(data){
			              if(data=='1'){
			              	alert("定义报表名已存在，请重新输入!");
					 		$("#definitionName").val("");
					 		$("#definitionName").focus();
					 		return ;
			              }
			       });		 	
		 }else{
		 	alert("定义报表名不能为空!");
		 	return ;
		 }
		 
	}
	
	//判断显示项是否为空
	function showitemTextIsHas(obj){
		var showitemText=obj.value;
		if(showitemText!=null&&showitemText!=""){
		}else{
			alert("当前显示项不能为空!");
			return ;
		}
	}
	
	//判断定义报表排序号是否为整数
	function isHasNum(value){
		var definitionSequence=value.trim();
		var r = /^[0-9]*[1-9][0-9]*$/
		if(definitionSequence!=null&&definitionSequence!=""){
				if(!r.test(definitionSequence)){
			 		alert("排序号只能输入正整数!");
			 		$("#definitionSequence").val("");
					$("#definitionSequence").focus();
			 		return ;
			 	}		 	
			}else{
					//alert("请输入排序号!");
					//$("#definitionSequence").val("");
					//$("#definitionSequence").focus();
			}
	}
	
	//删除报表显示项
	function delShowItem(value){
		var rownum = document.getElementById("showItemInfo_Table").rows;		//获取
		if(confirm("确定删除报表显示项【"+rownum[value].all("text1").value+"】?")) {
			var showitemId=rownum[value].all("showitemId").value;
			
			if(showitemId!=null&&showitemId!=""){
				$.post('<%=path%>/report/reportDefine!deleteByShowitem.action',
			             { 'showitemId':showitemId},
			              function(data){
			              if(data=='1'){
							document.all("showItemInfo_Table").deleteRow(value);		
					 		return ;
			              }else{
			              	alert("删除【"+rownum[value].all("text1").value+"】出错，请重新删除！")
			              }
			       });	
			}else{
				document.all("showItemInfo_Table").deleteRow(value);		
				return ;
			}
		}
	}
	
	
		 
		</script>
	</head>
	<base target="_self"/>
	<body scroll="auto" class=contentbodymargin>
	<DIV id=contentborder align="center" >
	<s:form action="/report/reportDefine!save.action" name="form" method="post" enctype="multipart/form-data">
	<label id="l_actionMessage" style="display:none;"><s:actionmessage/></label>
	<input type="hidden" id="definitionId" name="model.definitionId" value="${model.definitionId}">
	<input type="hidden" id="definitionId1" name="definitionId" value="${definitionId}">
	<input type="hidden" id="showitemData" name="showitemData" value="${showitemData}">
	<table width="100%" border="0" cellspacing="0" cellpadding="0" style="vertical-align: top;">
				<tr>
					<td colspan="3" class="table_headtd">
					<table width="100%" border="0" cellspacing="0" cellpadding="00">
						<tr>
							<td class="table_headtd_img" >
								<img src="<%=frameroot%>/images/ico/ico.gif" >&nbsp;
							</td>
							<td align="left">
							<script>
							  var definitionId=$("#definitionId").val();
							  if(definitionId==null||''==definitionId){
								  window.document.write("<strong>增加报表定义</strong>");
							  }else{
								  window.document.write("<strong>编辑报表定义</strong>");
							  }
							 </script>
							</td>
							<td align="right">
								<table border="0" align="right" cellpadding="00" cellspacing="0">
					                <tr>
					                
					                 	<td width="7"><img src="<%=frameroot%>/images/ch_h_l.gif"/></td>
					                 	<td class="Operation_input" onclick="save();">&nbsp;保&nbsp;存&nbsp;</td>
					                 	<td width="7"><img src="<%=frameroot%>/images/ch_h_r.gif"/></td>
				                  		<td width="5"></td>
				                  	 
				                  		<td width="8"><img src="<%=frameroot%>/images/ch_z_l.gif"/></td>
					                 	<td class="Operation_input1" onclick="goBack();">&nbsp;取&nbsp;消&nbsp;</td>
					                 	<td width="7"><img src="<%=frameroot%>/images/ch_z_r.gif"/></td>
				                  		<td width="6"></td>
					                </tr>
					            </table>
							</td>
							
						</tr>
					</table>
				</td>
				</tr>
				<tr>
					<td>
					
	
		
	<table  border="0" cellpadding="0" cellspacing="0" class="table1" width="100%" >
		<tr>
		<td valign="top" align="left" >
			<div>			
				<table  border="0" cellpadding="0" cellspacing="0" class="table1" width="100%" >
					<tr>
						<td class="biao_bg1"  align="right">
							<span class="wz">报表名称：</span>
						</td>
						<td class="td1" style="margin-left: 5px;">
							&nbsp;<input id="definitionName" name="model.definitionName" onchange="isHasDefinitionName(this.value);"   type="text" value="${model.definitionName}"  onkeydown="if(event.keyCode==13){save(); return false;}"
								 size="45" maxlength="20">
						</td>
					</tr>
					<tr>
						<td class="biao_bg1"  align="right">
							<span class="wz">排序顺序：</span>
						</td>
						<td class="td1" style="margin-left: 5px;">
							&nbsp;<input id="definitionSequence" name="model.definitionSequence" onchange="isHasNum(this.value);"   type="text" value="${model.definitionSequence}"  onkeydown="if(event.keyCode==13){save(); return false;}"
								 size="45" maxlength="20">
						</td>
					</tr>	
					<tr>
						<td nowrap class="biao_bg1" align="right">
							<span class="wz">报表类型：</span>
						</td>
						<td class="td1" style="margin-left: 5px;">
							&nbsp;<input id="sortName" name="sortName"value="${model.toaReportSort.sortName}" type="text" size="45"  ondblclick="addSort.click();"   readonly="true" > &nbsp;
							<a id="addSort" url="<%=root%>/report/workflowReportSort.action?checkSort=checkSort" href="#" class="button"> 
							添加</a>&nbsp;
							<input type="hidden" id="sortId" name="sortId" value="${model.toaReportSort.sortId}" >
						</td>
					</tr>
					<tr>
						<td nowrap class="biao_bg1" align="right">
							<span class="wz">表单名称：</span>
						</td>
						<td class="td1" style="margin-left: 5px;">
							&nbsp;<input id="definitionFormname" name="model.definitionFormname" value="${model.definitionFormname}" type="text"  size="45"  ondblclick="addEform.click();"   readonly="true"  > &nbsp;
						    <a id="addEform" url="<%=root%>/eformManager/eformManager.action?reportEform=reportEform" href="#" class="button"> 
							添加</a>&nbsp;
							<input type="hidden" id="definitionFormid" name="model.definitionFormid" value="${model.definitionFormid}">
						</td>
					</tr>	
					
					<tr>
							<td class="table1_td"></td>
							<td></td>
						</tr>
						
					
				</table>
			</div>
			<div id="workflowDiv" style="display: none">
			
				<table  border="0" cellpadding="0" cellspacing="0" class="table1" width="100%" >
					<tr>
						<td nowrap class="biao_bg1"  align="right">
							<span class="wz">相关工作流：</span>
						</td>
						<td class="td1" style="margin-left: 5px;">
							&nbsp;<input id="definitionWorkflowname" name="model.definitionWorkflowname" value="${definitionWorkflowname}" type="text"  size="45"  ondblclick="addWorkflow.click();" readonly="true"  > &nbsp;
						    <a id="addWorkflow" url="" href="#" class="button"> 
							添加</a>&nbsp;
							<input type="hidden" id="definitionWorkflowid" name="model.definitionWorkflowid" value="${model.definitionWorkflowid}" >
						</td>
					</tr>			
				
				</table>
				
			</div>	
			
			<div id="showItem"  valign="top"  style="display: block ;overflow-y:scroll;">
			<table width="100%" border="0" cellspacing="0" cellpadding="0" style="vertical-align: top;">
				<tr>
					<td colspan="3" class="table_headtd">
					<table width="100%" border="0" cellspacing="0" cellpadding="00">
						<tr>
							<td class="table_headtd_img" >
								<img src="<%=frameroot%>/images/ico/ico.gif" >&nbsp;
							</td>
							<td align="left">
							   
										<strong>报表显示项</strong>
									
							</td>
							
							
						</tr>
					</table>
				
				<div>
					 <table id="Id_TableofOut"  border="0" cellspacing="0" cellpadding="0" width="100%" style="display: none;" >
					   <tr bgColor="#EBEBEB" onselectstart="return false">
						    <td align="center" width="30%"><div class="title" style="width:100%;height:25px">字段显示名称</div></td>
						      <td align="center" width="10%"><div class="title" style="width:100%;height:25px">是否统计</div></td>
						    <td align="center" width="10%"><div class="title" style="width:100%;height:25px">排序方式</div></td>
						    <td align="center" width="10%"><div class="title" style="width:100%;height:25px">显示顺序</div></td>
						      <td align="center" width="10%"><div class="title" style="width:100%;height:25px">删除</div></td>	
						     <td align="center" width="30%"><div class="title" style="width:100%;height:25px"></div></td>					
					    </tr>
				   	</table>
				</div>
				<div  style="overflow-y:scroll;">
					<table id="showItemInfo_Table" class="table"  border="0" cellspacing="0" cellpadding="0" width="100%" >
						<tr bgColor="#EBEBEB" onselectstart="return false">
						    <td align="center" width="30%"><div class="title" style="width:100%;height:25px;background-color: #EBEBEB;">字段显示名称</div></td>
						      <td align="center" width="10%"><div class="title" style="width:100%;height:25px;background-color: #EBEBEB;">是否统计</div></td>
						    <td align="center" width="10%"><div class="title" style="width:100%;height:25px;background-color: #EBEBEB;">排序方式</div></td>
						    <td align="center" width="10%"><div class="title" style="width:100%;height:25px;background-color: #EBEBEB;">显示顺序</div></td>
						      <td align="center" width="10%"><div class="title" style="width:100%;height:25px;background-color: #EBEBEB;">删除</div></td>	
						     <td align="center" width="30%"><div class="title" style="width:100%;height:25px;background-color: #EBEBEB;"></div></td>					
					    </tr>
						  <s:iterator value="showItemList" var="showitem" status="status">
							     <tr>
								     <td align='center' width='30%' ><div class='cdata' style='width:100%;height:25px'>
								     	<input type="hidden" id="showitemId" name="showitemId" value="${showitem.showitemId}">
								     	<input type="hidden" id="showitemName" name="showitemName" value="${showitem.showitemName}">
								     	<input type="text"  id='text1' name="text1" onblur="showitemTextIsHas(this);"  style='height:25px'  maxlength='45' size='50' value="${showitem.showitemText}"  class="tableinput"/>
								     	</div>
								     	
								     </td>
								     <td align='center' width='10%' ><div class='cdata' style='width:100%;height:25px'>
								     	<select id='text2' class='tableinput' name='text2'>
								     		<s:if test="showitemIstotal==\"1\"">
								     			<option value="0" >否</option>
								     		
								     			<option value="1"  selected="selected">是</option>
								     		
								     		</s:if>
								     		<s:else>
								     			<option value="0"  selected="selected">否</option>
								     		
								     			<option value="1" >是</option>
								     		</s:else>
								     	</select>	
								     	</div>
								     </td>
								     <td align="center" width="10%"><div class="cdata" style="width:100%;height:25px" class="biao_bg1">
								     	<select id='text3' class='tableinput' name='text3' >
								     		<s:if test="showitemOrderby==\"1\"">
								     			<option value="1"  selected="selected">升序</option>
								     			<option value="-1" >降序</option>
								     			<option value="0" >空</option>
								     		</s:if>
								     		<s:elseif test="showitemOrderby==\"-1\"">
								     			<option value="1"  >升序</option>
								     			<option value="-1" selected="selected" >降序</option>
								     			<option value="0" >空</option>
								     		</s:elseif>
								     		<s:else>
								     			<option value="1"  >升序</option>
								     			<option value="-1"  >降序</option>
								     			<option value="0" selected="selected">空</option>
								     		</s:else>
								     		
								     	</select>
								     
								     	</div>
								     </td>
								      <td align='center' width='10%' ><div class='cdata' ><input type='text' id='text4' name='text4' maxlength="5" size="5"    value="${showitem.showitemSequence}"   class="tableinput"  onkeyup="sequenceIsNum(this)" onafterpaste="sequenceIsNum(this)" />
								     	
								     	</div>
								     </td>
								     <td align="center" width="10%" ><div class="cdata" style="width:100%;height:25px" class="biao_bg1"><a onclick="delShowItem(this.parentElement.parentElement.parentElement.rowIndex)"  href="#"><img  src="<%=root%>/images/ico/shanchu.gif" border="0" /></a> </div></td>
								   	<td align="center" width="30%" ></td>
							     </tr>
						     </s:iterator>
				</table>
					
				<div id="saveDiv" style="display: block">
				 <table border="0" align="center" cellpadding="00" cellspacing="0">
					                <tr>
					                
					                 	<td width="7"><img src="<%=frameroot%>/images/ch_h_l.gif"/></td>
					                 	<td class="Operation_input" onclick="save();">&nbsp;保&nbsp;存&nbsp;</td>
					                 	<td width="7"><img src="<%=frameroot%>/images/ch_h_r.gif"/></td>
				                  		<td width="5"></td>
				                  	 
				                  		<td width="8"><img src="<%=frameroot%>/images/ch_z_l.gif"/></td>
					                 	<td class="Operation_input1" onclick="goBack();">&nbsp;取&nbsp;消&nbsp;</td>
					                 	<td width="7"><img src="<%=frameroot%>/images/ch_z_r.gif"/></td>
				                  		<td width="6"></td>
					                </tr>
					            </table>
				
			</div>
				</div>
			</div>
		</td>
		</tr>
		
	</table>
	</s:form>
	</DIV>
	</body>
</html>
