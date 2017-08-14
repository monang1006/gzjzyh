<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@taglib prefix="s" uri="/struts-tags"%>
<%@include file="/common/include/rootPath.jsp"%>
<html>
	<head>
		<title>桌面菜单管理</title>
		<META http-equiv=Content-Type content="text/html; charset=utf-8">
		<!--  引用公共样式文件,建议所有样式都以文件方式定义在jsp文件外部,通常定义在WebRoot目录下的CSS文件夹下-->
	    <LINK href="<%=frameroot%>/css/properties_windows_special.css" type=text/css rel=stylesheet>
		<link href="<%=frameroot%>/strongitmenu.css" type="text/css" rel="stylesheet">
		<!--右键菜单样式 -->
		<script language='javascript' src='<%=path%>/common/js/grid/ChangeWidthTable.js'></script>
		<SCRIPT language="javascript" src="<%=path%>/common/js/menu/menu.js"></SCRIPT>
		<!--右键菜单脚本 -->
		<script language='javascript' src='<%=path%>/common/js/jquery/jquery-1.2.6.js'></script>
		<script language="javascript" src="<%=path%>/common/js/common/common.js"></script>
		<!--右键菜单脚本 -->
		<script language="javascript" src="<%=path%>/common/js/common/search.js"></script>
		<style type="text/css">
		 .desktopmenu {
		   background-color: #ededed;
		   
		 }
		 .table1{
		 border-collapse:collapse;
		 border-left:solid 1px #808080;
		 border-top:solid 1px #808080;
		 }
		 .table1 td{
		 border-bottom:solid 1px #808080;
		 border-right:solid 1px #808080;
		 }
		 .select1{
		 margin:-2px; width:180px;height:300px;align:center;border: none; background: #ffffff;
		
		 }
		 
		</style>
		<script>
		// $(document).ready(function(){
				//$("#save").click(function(){
				   // fld_str="";
				    //alert(select1.options.length);
				   // for (i=0; i< select1.options.length; i++)
				   // {
				     // options_value=select1.options(i).value;
				     // fld_str+=options_value+",";
				    //}
				    //fld_str=fld_str.substring(0, fld_str.length - 1);
				  	//$.ajax({
				  		//type:"post",
				  		//dataType:"text",
				  		
				  		//data:"idStr="+fld_str,
				  		//success:function(msg){
							//if(msg=="true"){
								//alert("操作成功！");
							//}
				  		//}
				  	//});
				//});
			//});
			function mysubmit1(){
				fld_str="";
			    //alert(select1.options.length);
			    for (i=0; i< select1.options.length; i++)
			    {
			      options_value=select1.options(i).value;
			      fld_str+=options_value+",";
			    }
			    fld_str=fld_str.substring(0, fld_str.length - 1);
			  	$.ajax({
			  		type:"post",
			  		dataType:"text",
			  		url:"<%=root%>/desktopmenu/desktopMenu!edit.action",
			  		data:"idStr="+fld_str,
			  		success:function(msg){
						if(msg=="true"){
							alert("操作成功！");
						}
			  		}
			  	});
			}
			function func_insert(){
				for (i=0; i<select2.options.length; i++){
					if(select2.options(i).selected && select2.options(i).value!="MENU_SORT"){
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
				}
			}

			function func_delete(){
				for (i=0;i<select1.options.length;i++){
					if(select1.options(i).selected){
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

			function func_select_all1(){
				for(i=select1.options.length-1; i>=0; i--){
					select1.options(i).selected=true;
				}
			}

			function func_select_all2(){
				for(i=select2.options.length-1; i>=0; i--){
				   select2.options(i).selected=true;
				}
			}

			function func_up(){
				sel_count=0;
				for (i=select1.options.length-1; i>=0; i--){
					if(select1.options(i).selected){
					   sel_count++;
					}
				}
				
				if(sel_count==0){
				   alert("调整菜单快捷组的项目顺序时，请选择其中一项！");
				   return;
				}else if(sel_count>1){
				   alert("调整菜单快捷组的项目顺序时，只能选择其中一项！");
				   return;
				}
				
				i=select1.selectedIndex;
				
				if(i!=0){
					var my_option = document.createElement("OPTION");
					my_option.text=select1.options(i).text;
					my_option.value=select1.options(i).value;
					
					select1.add(my_option,i-1);
					select1.remove(i+1);
					select1.options(i-1).selected=true;
				}
			}

			function func_down(){
				sel_count=0;
				for (i=select1.options.length-1; i>=0; i--){
				  if(select1.options(i).selected){
				     sel_count++;
				  }
				}
			
				if(sel_count==0){
				   alert("调整菜单快捷组的项目顺序时，请选择其中一项！");
				   return;
				}else if(sel_count>1){
				   alert("调整菜单快捷组的项目顺序时，只能选择其中一项！");
				   return;
				}
			
				i=select1.selectedIndex;
			
				if(i!=select1.options.length-1){
				  var my_option = document.createElement("OPTION");
				  my_option.text=select1.options(i).text;
				  my_option.value=select1.options(i).value;
				
				  select1.add(my_option,i+2);
				  select1.remove(i);
				  select1.options(i+1).selected=true;
				}
			}
		</script>
	</head>
	<body class=contentbodymargin oncontextmenu="return false;" style="background-color: #ffffff;">
		<DIV id=contentborder align=center>
			<table width="100%" border="0" cellspacing="0" cellpadding="0"
				style="vertical-align: top;">
				<tr>
					<td height="100%">
						<table width="100%" border="0" cellspacing="0" cellpadding="00">
				<tr>
				<td colspan="3" class="table_headtd">
					<table width="100%" border="0" cellspacing="0" cellpadding="00">
						<tr>
							<td class="table_headtd_img" >
								<img src="<%=frameroot%>/images/ico/ico.gif" >&nbsp;
							</td>
							<td align="left">
								<strong>桌面菜单管理</strong>
							</td>
							<td align="right">
								<table border="0" align="right" cellpadding="00" cellspacing="0">
					                <tr>
					                 	
					                 	<td width="7"><img src="<%=frameroot%>/images/ch_h_l.gif"/></td>
					                 	<td class="Operation_input" onclick="mysubmit1();">&nbsp;保存设置&nbsp;</td>
					                 	<td width="7"><img src="<%=frameroot%>/images/ch_h_r.gif"/></td>
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
						<table width="720px"  cellpadding="0" cellspacing="0"
							align="center" class="table1"
							bordercolordark="#FFFFFF"  >
							<s:hidden id="id" name="modle.fastmenuId"></s:hidden>
							<tr class="desktopmenu">
								<td  class="biao_bg1" align="center">
									排序
								</td>
								<td   align="center">
									<b>桌面菜单已有项</b>
								</td>
								<td  class="biao_bg1" align="center">
									选择
								</td>
								<td    align="center">
									<b>备选菜单项</b>
								</td>
							</tr>
							<tr>
								<td    align="center">
								<a   href="#" class="button" onclick="func_up();">↑</a>
									
									<br>
									<br>
									<a   href="#" class="button" onclick="func_down();">↓</a>
									
								</td>
								<td     align="center">
									<select  name="select1" ondblclick="func_delete();" multiple="multiple" class="select1">
										<s:iterator value="#request.selected"  id="privil">
											<option value="<s:property value="#privil.privilSyscode" />">
												<s:property value="#privil.privilName" />
											</option>			
										</s:iterator>
									</select>
									<a   href="#" class="button" onclick="func_select_all1();">全 选 </a>
									
								</td>
								<td   align="center">
								   <a   href="#" class="button" onclick="func_insert();">←</a>
									
									<br>
									<br>
									<a   href="#" class="button" onclick="func_delete();">→</a>
									
								</td>
								<td   align="center">
									<select name="select2" ondblclick="func_insert();" multiple="multiple" class="select1">
										<s:iterator value="#request.maySelect" id="privil">	
											<option value="<s:property value="#privil.privilSyscode" />">
												<s:property value="#privil.privilName" />
											</option>										
										</s:iterator>
									</select>
									<a   href="#" class="button" onclick="func_select_all2();">全 选 </a>
									
								</td>
							</tr>
							<tr class="desktopmenu">
								<td  align="center" height="40px" colspan="4" >
									点击条目时，可以组合CTRL或SHIFT键进行多选
									
									<!--  <input type="button" name="save" id="save" class="input_bg" value="保存设置">-->
								</td>
							</tr>
						</table>
					</td>
				</tr>
			</table>
		</DIV>
		<SCRIPT type="text/javascript">
			function mysubmit()
			{
			    fld_str="";
			    for (i=0; i< select1.options.length; i++)
			    {
			      options_value=select1.options(i).value;
			      fld_str+=options_value+",";
			    }
			    fld_str=fld_str.substring(0, fld_str.length - 1);
			    var id=document.getElementById("id").value;
				top.perspective_top.location="<%=path%>/shortcutmenu/fastMenu!save.action?modleIds="+fld_str+"&id="+id;
			}
		</SCRIPT>
	</body>
</html>
