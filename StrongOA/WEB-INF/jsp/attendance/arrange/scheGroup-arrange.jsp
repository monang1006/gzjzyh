<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="/tags/web-newdate" prefix="strong"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@include file="/common/include/rootPath.jsp"%>
<html>
	<head>
		<title>人员排班</title><%--
		该页面已取消
		--%><%@include file="/common/include/meta.jsp"%>
		<LINK type=text/css rel=stylesheet
			href="<%=frameroot%>/css/properties_windows.css">
		<script type="text/javascript" language="javascript"
			src="<%=path%>/common/js/jquery/jquery-1.2.6.js"></script>
		<script type="text/javascript" language="javascript"
			src="<%=path%>/common/js/validate/jquery.validate.js"></script>
		<script type="text/javascript" language="javascript"
			src="<%=path%>/common/js/validate/formvalidate.js"></script>
		<script language="javascript" type="text/javascript"
			src="<%=path%>/common/js/upload/jquery.MultiFile.js"></script>
		<script language="javascript" type="text/javascript"
			src="<%=path%>/common/js/upload/jquery.blockUI.js"></script>
		<script type="text/javascript" language="javascript"
			src="<%=path%>/common/js/newdate/WdatePicker.js"></script>
		<script src="<%=path%>/common/js/common/common.js"
			type="text/javascript"></script>
		<base target="_self">
		<script>
			window.name="MyModalDialog"; 
			function func_insert()
			{
				var select1=document.getElementById("select1");
				var select2=document.getElementById("select2");
			 for (i=0; i<select2.options.length; i++)
			 {
			   if(select2.options(i).selected && select2.options(i).value!="MENU_SORT")
			   {
			     option_text=select2.options(i).text;
			     option_value=select2.options(i).value;
			     option_style_color=select2.options(i).style.color;
			
			     var my_option = document.createElement("OPTION");
			     my_option.text=option_text;
			     my_option.value=option_value;
			     my_option.style.color=option_style_color;
			
			     pos=select1.options.length;
			     select1.add(my_option,pos);
			     select2.remove(i);
			     i--;
			  }
			 }//for
			}
			
			function func_delete()
			{
				var select1=document.getElementById("select1");
				var select2=document.getElementById("select2");
			 for (i=0;i<select1.options.length;i++)
			 {
			   if(select1.options(i).selected)
			   {
			     option_text=select1.options(i).text;
			     option_value=select1.options(i).value;
			
			     var my_option = document.createElement("OPTION");
			     my_option.text=option_text;
			     my_option.value=option_value;
			
			     pos=select2.options.length;
			     select2.add(my_option,pos);
			     select1.remove(i);
			     i--;
			  }
			 }
			}
			
			function func_select_all1()
			{
			var select1=document.getElementById("select1");
			 for (i=select1.options.length-1; i>=0; i--)
			   select1.options(i).selected=true;
			}
			
			function func_select_all2()
			{
			 var select2=document.getElementById("select2");
			 for (i=select2.options.length-1; i>=0; i--)
			   select2.options(i).selected=true;
			}
			
			function func_up()
			{
				var select1=document.getElementById("select1");
				var select2=document.getElementById("select2");
			  	sel_count=0;
			  	for (i=select1.options.length-1; i>=0; i--)
			  	{
			    	if(select1.options(i).selected)
			       	sel_count++;
			 	}
			
			  	if(sel_count==0)
			  	{
			    	alert("调整菜单快捷组的项目顺序时，请选择其中一项！");
			    	 return;
			  	}
			 	 else if(sel_count>1)
			  	{
			     	alert("调整菜单快捷组的项目顺序时，只能选择其中一项！");
			     	return;
			  	}
			
			  	i=select1.selectedIndex;
			
			  	if(i!=0)
			  	{
			    	var my_option = document.createElement("OPTION");
			    	my_option.text=select1.options(i).text;
			    	my_option.value=select1.options(i).value;
			
			    	select1.add(my_option,i-1);
			    	select1.remove(i+1);
			    	select1.options(i-1).selected=true;
			  	}
			}
			
			function func_down()
			{
			  var select1=document.getElementById("select1");
			  sel_count=0;
			  for (i=select1.options.length-1; i>=0; i--)
			  {
			    if(select1.options(i).selected)
			       sel_count++;
			  }
			
			  if(sel_count==0)
			  {
			     alert("调整菜单快捷组的项目顺序时，请选择其中一项！");
			     return;
			  }
			  else if(sel_count>1)
			  {
			     alert("调整菜单快捷组的项目顺序时，只能选择其中一项！");
			     return;
			  }
			
			  i=select1.selectedIndex;
			
			  if(i!=select1.options.length-1)
			  {
			    var my_option = document.createElement("OPTION");
			    my_option.text=select1.options(i).text;
			    my_option.value=select1.options(i).value;
			
			    select1.add(my_option,i+2);
			    select1.remove(i);
			    select1.options(i+1).selected=true;
			  }
			}
			    
        function radioOrg(){
		    var audit=OpenWindow("<%=root%>/personnel/personorg/personOrg!radiotree.action",270,320,window);
		    if(audit==undefined||audit==null){
		      	return false;
		    }else{  
             	$("#orgName").val(audit[1]);	
             	$("#orgId").val(audit[0]);    
				document.all.myTable.action="<%=root%>/attendance/arrange/scheGroup!arrange.action";
				document.all.myTable.submit(); 
		    }
		}
		
		function submitForm(){
		   	fld_str="";
		   	fld_str_pre="";
		   	var groupId=document.getElementById("groupId").value;
		   	var orgId=document.getElementById("orgId").value;
		   	//已选人员
		   	var select2=document.getElementById("select2");
		   	for (i=0; i< select2.options.length; i++){
		      	options_value=select2.options(i).value;
		      	fld_str+=options_value+",";
		   	}
		    fld_str=fld_str.substring(0, fld_str.length - 1);
		    
		    //可选人员
		    var select1=document.getElementById("select1");
		   	for (i=0; i< select1.options.length; i++){	
		      	options_value=select1.options(i).value;
		      	fld_str_pre+=options_value+",";
		   	}
		    fld_str_pre=fld_str_pre.substring(0, fld_str_pre.length - 1);
		    if(fld_str_pre==""&&fld_str==""){
		    	alert("该单位没有人员，排班无效！");
		    	return;
		    }
		   	$.ajax({
				type:"post",
				url:"<%=path%>/attendance/arrange/scheGroup!saveArrange.action",
				data:{
					groupId:groupId,
					fld_str:fld_str,
					fld_str_pre:fld_str_pre
				},
				success:function(data){
					alert("人员排班成功！");
					
				},
				error:function(data){
					alert("对不起，操作异常"+data);
				}
			});
		}
	</script>
	</head>
	<body class=contentbodymargin oncontextmenu="return false;">
		<DIV id=contentborder align=center>
			<s:form id="myTable" theme="simple"
				action="/attendance/arrange/scheGroup!save.action"  target="MyModalDialog">
				<table width="100%" border="0" cellspacing="0" cellpadding="0"
					style="vertical-align: top;">
					<tr>
						<td height="100%">
							<table width="100%" border="0" cellspacing="0" cellpadding="0">
								<tr>
									<td height="40"
										style="FILTER:progid:DXImageTransform.Microsoft.Gradient(gradientType=0,startColorStr=#ededed,endColorStr=#ffffff);">
										<table width="100%" border="0" cellspacing="0"
											cellpadding="00">
											<tr>
												<td>
												&nbsp;
												</td>
												<td width="30%">
													<img src="<%=frameroot%>/images/ico/ico.gif" width="9"
														height="9">&nbsp;
													人员排班
												</td>
												<td width="*">
													&nbsp;
												</td>
											</tr>
										</table>
									</td>
								</tr>
							</table>
							<input id="groupId" name="groupId" type="hidden" size="32"
								value="${model.groupId}">
							<input id="orgId" name="orgId" maxlength="25" type="hidden" size="39" value="${orgId}">
							<table width="100%" height="10%" border="0" cellpadding="0"
								cellspacing="1" align="center" class="table1">
								<tr>
									<td width="20%" height="21" class="biao_bg1" align="right">
										<span class="wz">部门名称：(<FONT color="red">*</FONT>)：</span>
									</td>						
									<s:if test="model.logo!=null&&model.logo==\"1\"">
										<td class="td1" height="21"  align="left">
											<input id="orgName" name="orgName" maxlength="25" type="text"
												size="39" value="${orgName}">
										</td>
										<td class="td1" height="21"  align="left">
											<input type="button" class="input_bg" value="选择" onclick="radioOrg()">
										</td>
										<td height="21" class="biao_bg1" align="right">
											起始班次：
										</td>
										<td class="td1" height="21" colspan="2" align="left">
											<s:select cssStyle="width: 150px" 
												list="#{'行政办':'0','倒班':'1'}"
												id="logo" name="model.logo"
												listKey="value" listValue="key" >
											</s:select>
										</td>
									</s:if>	
									<s:else>
										<td class="td1" height="21" colspan="4" align="left">
											<input id="orgName" name="orgName" maxlength="25" type="text"
												size="39" value="${orgName}"><input type="button" class="input_bg" value="选择"
															onclick="radioOrg()">
										</td>
									</s:else>				
								</tr>
								<tr>
									<td class="biao_bg1" align="right"
										rowspan="2">
									</td>
									<td align="center" class="biao_bg1">
										<b>可选人员</b>
									</td>
									<td align="center" class="biao_bg1">
										选择
									</td>
									<td align="center" colspan="2" valign="top" class="biao_bg1">
										<b>已选人员</b>
									</td>
								</tr>
								<tr>
									<td valign="top"  align="center" class="biao_bg1">
										<select id="select1" name="select1" ondblclick="func_delete();"
											multiple="multiple" style="width:130px;height:260px">
											<s:iterator value="#request.notArrangePerList">
												<option value="<s:property value="personid" />">
													<s:property value="personName" />
												</option>
											</s:iterator>
										</select>
										<input type="button" value=" 全 选 "
											onclick="func_select_all1();" class="input_bg">
									</td>
									<td align="center" class="biao_bg1">
										<input type="button" class="input_bg" value=" ← "
											onclick="func_insert();">
										<br>
										<br>
										<input type="button" class="input_bg" value=" → "
											onclick="func_delete();">
									</td>
									<td valign="top" colspan="2" align="center" class="biao_bg1">
										<select id="select2" name="select2" ondblclick="func_insert();"
											multiple="multiple" style="width:130px;height:260px">
											<s:iterator value="#request.arrangePerList">
												<option value="<s:property value="personid" />">
													<s:property value="personName" />
												</option>
											</s:iterator>
										</select>
										<input  type="button" value=" 全 选 "
											onclick="func_select_all2();" class="input_bg">
									</td>
								</tr>
							</table>
							<br>
							<table width="100%" border="0" cellspacing="0" cellpadding="00">
								<tr>
									<td align="center" valign="middle">
										<table width="27%" border="0" cellspacing="0" cellpadding="00">
											<tr>
												<td width="29%">
													<input id="save" type="button" class="input_bg" value="保 存"
														onclick="submitForm()">
												</td>
												<td width="37%">
													<input id="close" type="button" class="input_bg"
														value="关 闭" onclick="window.close();">
												</td>
											</tr>
										</table>
									</td>
								</tr>
							</table>
						</td>
					</tr>
				</table>
			</s:form>
		</DIV>
	</body>
</html>
