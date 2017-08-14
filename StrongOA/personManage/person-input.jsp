<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="/tags/web-flex" prefix="webflex"%>
<%@ taglib uri="/tags/web-newdate" prefix="strong"%>
<%@taglib prefix="s" uri="/struts-tags"%>
<%@include file="/common/include/rootPath.jsp"%>
<html>
	<head>
		<title>编辑人员基本信息</title>
		<META http-equiv=Content-Type content="text/html; charset=utf-8">
		<LINK href="<%=frameroot%>/css/properties_windows.css" type=text/css
			rel=stylesheet>
		<link href="<%=frameroot%>/strongitmenu.css" type="text/css"
			rel="stylesheet">
		<script language='javascript' src='<%=path%>/common/js/grid/ChangeWidthTable.js'></script>

		<!--右键菜单样式 -->
		<script language='javascript'
			src='<%=path%>/common/js/grid/ChangeWidthTable.js'></script>
		<SCRIPT language="javascript" src="<%=path%>/common/js/menu/menu.js"></SCRIPT>
		<!--右键菜单脚本 -->
		<script language='javascript'
			src='<%=path%>/common/js/jquery/jquery-1.2.6.js'></script>
		<script language="javascript"
			src="<%=path%>/common/js/common/common.js"></script>
		<!--右键菜单脚本 -->
		<script language="javascript"
			src="<%=path%>/common/js/common/search.js"></script>
			
		<script>
		
<!-- 添加培训人员-->
function func_insert()
{
 for (i=0; i<select2.options.length; i++)
 {
   if(select2.options(i).selected)
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
<!-- 去掉培训人员 -->
function func_delete()
{
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
<!-- 全选培训人员列表中名单-->
function func_select_all1()
{
 for (i=select1.options.length-1; i>=0; i--)
   select1.options(i).selected=true;
}
<!-- 全选供选人员列表中名单-->
function func_select_all2()
{
 for (i=select2.options.length-1; i>=0; i--)
   select2.options(i).selected=true;
}
</script>
		<s:head />
	</head>
   <body class=contentbodymargin oncontextmenu="return false;">
				<script type="text/javascript"
			src="<%=path%>/common/js/newdate/WdatePicker.js"></script>
		<!-- 增加form    -->
		<DIV id=contentborder >
		<s:form action="/personnel/veteran!save.action" id="veteranform" name="veteranform" method="post" enctype="multipart/form-data">
		<table width="100%" border="0" cellspacing="0" cellpadding="00"
									style="FILTER:progid:DXImageTransform.Microsoft.Gradient(gradientType=0,startColorStr=#ededed,endColorStr=#ffffff);">
										<br>
									      <tr>
											<td width="5%" align="center">
												<img src="<%=frameroot%>/images/ico.gif" width="9"
													height="9">
											</td>
											<td width="12%">
												人员信息编辑
											</td>
											<td width="1%">
												&nbsp;
											</td>
											<td width="87%">
												
								
								</td>
							</tr>
						</table>
            <table width="100%">
				<tr>
					<td height="10">	
					  
					</td>
				</tr>
			</table>
			<table width="100%" border="0" cellpadding="0" 
				cellspacing="1" class="table1">
				
				<tr colspan="1" height="30" class="biao_bg1" align="right">
				<td height="30" class="biao_bg1" align="right"><span class="wz">工号(<font color="#FF6600">*</font>)：&nbsp;</span>
				</td><td colspan="" class="td1" align="left" ><input id=""
											
											name="" type="text" size="22"
											value="">
										<input type="button" class="input_bg" value="生成工号"
											onclick="">
										
				</td><td rowspan="6" width="15%" class="td1" align="left">
				<img src="" style="width: 125px; height: 155px;"><br>
				<input type="file" onkeydown="return false;" size="10" name="file" class="multi" />
						
				</td></tr>
				<tr colspan="1" height="30" class="biao_bg1" align="right">
				<td height="30" class="biao_bg1" align="right">		<span class="wz">姓名(<font color="#FF6600">*</font>)：&nbsp;</span>
				</td><td colspan=""  class="td1" align="left">
				<input id="name"  name="name" value="" type="text" style="">&nbsp;&nbsp;&nbsp;&nbsp;<span class="wz">曾用名：&nbsp;</span>
				<input id="name"  name="name" value="" type="text" style="">
				</td></tr>
				
				<tr colspan="1" height="30" class="biao_bg1" align="right">
				<td height="21" class="biao_bg1" align="right">		<span class="wz">性别(<font color="#FF6600">*</font>)：&nbsp;</span>
				</td><td colspan=""  class="td1" align="left">
				<input type="radio" name="aa" value="0" >男<input type="radio" name="aa" value="1" >女&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
				<span class="wz">出生日期(<font color="#FF6600">*</font>)：&nbsp;</span>
				<strong:newdate id="birthDate" name="birthDate"  isicon="true" dateform="yyyy-MM-dd"    />
				</td></tr>
			
				
				<tr colspan="1" height="30" class="biao_bg1" align="right">
				 <td colspan="1" height="30" class="biao_bg1" align="right">
						<span class="wz">身份证号(<font color="#FF6600">*</font>)：&nbsp;</span>
					</td>
					<td colspan="1" class="td1" align="left">
						<input id="cardId"  name="cardId" value="" type="text" style="width:80%;" class="required number">
					</td></tr>
				<tr colspan="1" height="30" class="biao_bg1" align="right">
				<td height="30" class="biao_bg1" align="right"> 		<span class="wz">籍贯(<font color="#FF6600">*</font>)：&nbsp;</span>
				</td><td colspan=""  class="td1" align="left">
				<input id="nativePlace"  name="nativePlace" value="" type="text" style="width:80%;" class="required number">
				</td></tr>
				
				<tr><td colspan="1" height="30" width="21%" class="biao_bg1" align="right">
				<span class="wz">出生地：&nbsp;</span>
				</td><td colspan="" class="td1" align="left">
				<input id="nativePlace"  name="nativePlace" value="" type="text" style="width:80%;" class="required number">
				
				</td></tr>
				</table>
				<table width="100%" border="0" cellpadding="0" 
				cellspacing="1" class="table1">
				
				<tr>
				
					<td height="21" class="biao_bg1" align="right">		
					<span class="wz">学历(<font color="#FF6600">*</font>)：&nbsp;</span>
				</td><td colspan=""  class="td1" align="left">
			  <s:select name="orgId" list="#{'0':'高中','1':'大专','2':'本科'}" listKey="key" listValue="value" onchange='' style="width:80%"/>
					</td>
				   <td width="25%" colspan="1" height="30" class="biao_bg1" align="right">
						<span class="wz">民族：&nbsp;</span>
					</td>
					<td  width="25%" colspan="1" class="td1">
						<input id="cardId"  name="cardId" value="" type="text" style="width:80%;" class="required number">
					</td>
					
			    	
				</tr>
            	<tr>
            	<td width="20%" colspan="1" height="30" class="biao_bg1" align="right">
						<span class="wz">毕业院校：&nbsp;</span>
					</td>
					<td  width="30%" colspan="1" class="td1">
						<input id="cardId"  name="cardId" value="" type="text" style="width:80%;" class="required number">
					</td>
					<td  height="30" class="biao_bg1" align="right">
						<span class="wz">政治面貌：&nbsp;</span>
					</td>
					<td   class="td1" align="left">
					<s:select name="health" list="#{'':'群众','0':'团员','1':'党员'}" listKey="key" listValue="value" onchange='$("#img_sousuo").click();' style="width:80%"/>
				
					</td>
				   
				</tr>
				
				
				<tr>
				 <td colspan="1" height="30" class="biao_bg1" align="right">
						<span class="wz">编制(<font color="#FF6600">*</font>)：&nbsp;</span>
					</td>
					<td colspan="1" class="td1" align="left">
						<s:select name="strucId" list="#{'':'编制','0':'编制一','1':'编制二','2':'编制三'}" listKey="key" listValue="value" onchange='$("#img_sousuo").click();' style="width:80%"/>
					</td>
			    	
				<td  height="30" class="biao_bg1" align="right">
						<span class="wz">部门(<font color="#FF6600">*</font>)：&nbsp;</span>
					</td>
					<td   class="td1" align="left">
					<s:select name="orgId" list="#{'':'部门','0':'部门一','1':'部门二'}" listKey="key" listValue="value" onchange='$("#img_sousuo").click();' style="width:80%"/>
					</td>
				</tr>
				<tr>
				      
				
					  
					<td width="20%" colspan="1" height="30" class="biao_bg1" align="right">
						<span class="wz">个人身份：&nbsp;</span>
					</td>
					<td  width="30%" colspan="1" class="td1">
						<s:select name="orgId" list="#{'0':'领导','1':'非领导'}" listKey="key" listValue="value" onchange='$("#img_sousuo").click();' style="width:80%"/>
					
					</td>
					 <td colspan="1" height="30" class="biao_bg1" align="right">
						<span class="wz">职务：&nbsp;</span>
					</td>
					<td colspan="1" class="td1" align="left">
						<s:select name="post" list="#{'':'职位','0':'职位一','1':'职位二'}" listKey="key" listValue="value" onchange='$("#img_sousuo").click();' style="width:80%"/>
					</td>
				</tr>
				<tr>
				 <td  height="30" class="biao_bg1" align="right">
						<span class="wz">待遇级别：&nbsp;</span>
					</td>
					<td   class="td1" align="left">
					<s:select name="type" list="#{'':'级别','0':'级别一','1':'级别二'}" listKey="key" listValue="value" onchange='$("#img_sousuo").click();' style="width:80%"/>
					</td>
						<td width="20%" colspan="1" height="30" class="biao_bg1" align="right">
						<span class="wz">入职日期：&nbsp;</span>
					</td>
					<td  width="30%" colspan="1" class="td1">
						<strong:newdate id="beginWork" name="beginWork"  isicon="true" dateform="yyyy-MM-dd" 
										    width="80%" />
					</td>
				    
			  </tr>
				
				
				
			</table><br>
			<table width="100%">
			<tr>
					<td class="td1" colspan="4" align="center" height="21">
					    <input name="Submit" type="button" class="input_bg" value="保  存" onclick="save();">
					    &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp;
						<input name="Submit2" type="button" class="input_bg" value="返  回"
							onclick="go();">
					</td>
				</tr>
			</table>
		
		</s:form>
		</DIV>
		<SCRIPT type="text/javascript">
			function getReason(){
			}
			function bullt(){
			var v=OpenWindow("<%=path%>/personnel/training!add.action");
			if(v!=undefined){
				document.getElementById("persons").value=v;
				}
			else{
				document.getElementById("persons").value="";
				}
			
			//OpenWindow("<%=path%>/mattendance/leave!newtest.action","300","300",window);
			//OpenWindow("<%=root%>/fileNameRedirectAction.action?toPage=mattendance/leave-newtest.jsp","300","170",window);
			//window.open("<%=root%>/fileNameRedirectAction.action?toPage=mattendance/leave-newtest.jsp",'width=300,   height=300,   scrollbars=no ');
			
			}
			//返回上一页
			var go=function(){
			  window.history.go(-1);
			}
			function mysubmit() 
			{
			   fld_str="";
			   for (i=0; i< select1.options.length; i++)
			   {
			      options_value=select1.options(i).value;
			      fld_str+=options_value+",";
			    }
			    fld_str=fld_str.substring(0, fld_str.length - 1);
			    document.getElementById("test").value=fld_str;
			    var id=document.getElementById("test").value;
			   alert("values_test： "+id);
				//top.perspective_top.location="<%=path%>/shortcutmenu/fastMenu!save.action?modleIds="+fld_str+"&id="+id+"&param="+param;
			}
			function save(){   
   				veteranform.submit();
			}
		</SCRIPT>
	</body>
</html>
