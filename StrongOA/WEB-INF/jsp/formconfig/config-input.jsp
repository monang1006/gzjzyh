<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@taglib prefix="s" uri="/struts-tags"%>
<%@include file="/common/include/rootPath.jsp"%>
<html>
	<head>
		<title>界面快捷设置</title>
		<%@include file="/common/include/meta.jsp"%>
		<LINK href="<%=frameroot%>/css/properties_windows.css" type=text/css
			rel=stylesheet>
		<link href="<%=frameroot%>/strongitmenu.css" type="text/css"
			rel="stylesheet">
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
function func_insert()
{
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
 for (i=select1.options.length-1; i>=0; i--)
   select1.options(i).selected=true;
}

function func_select_all2()
{
 for (i=select2.options.length-1; i>=0; i--)
   select2.options(i).selected=true;
}

function func_up()
{
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
</script>
		<s:head />
	</head>
	<body class=contentbodymargin oncontextmenu="return false;">
		<DIV id=contentborder align=center>
			<table width="100%" border="0" cellspacing="0" cellpadding="0"
				style="vertical-align: top;">
				<tr>
					<td height="100%">
						<table width="100%" border="0" cellspacing="0" cellpadding="0">
							<tr>
								<td height="40"
									style="FILTER:progid:DXImageTransform.Microsoft.Gradient(gradientType=0,startColorStr=#ededed,endColorStr=#ffffff);">
									<table width="100%" border="0" cellspacing="0" cellpadding="00">
										<tr>
											<td>&nbsp;</td>
											<td width="30%">
												<img src="<%=frameroot%>/images/ico/ico.gif" width="9"
													height="9">&nbsp;
												表单配置
											</td>
											<td width="70%"></td>
										</tr>
									</table>
								</td>
							</tr>
						</table>
						<table width="70%" border="1" cellpadding="0" cellspacing="1"
							align="center" class="table1" bordercolorlight="#808080"
							bordercolordark="#FFFFFF">
							<s:hidden id="id" name="modle.fastmenuId"></s:hidden>
							<s:hidden id="param" name="param"></s:hidden>
							<tr>
								<td align="center" class="biao_bg1">
									排序
								</td>
								<td align="center" class="biao_bg1">
									<b>电子表单</b>
								</td>
								
								
							</tr>
							<tr>
								<td align="center" class="biao_bg1">
									<input type="button" class="input_bg" value=" ↑ "
										onclick="func_up();">
									<br>
									<br>
									<input type="button" class="input_bg" value=" ↓ "
										onclick="func_down();">
								</td>
								<td valign="top" align="center" class="biao_bg1">
									<select name="select1" 
										multiple="multiple" style="width:180px;height:270px">
										<s:iterator value="formList">
											<option value="<s:property value="id" />">
												<s:property value="title" />
											</option>			
										</s:iterator>
									</select>
								</td>
							</tr>
							<tr>
								<td align="center" valign="top" colspan="2" class="biao_bg1">
									<input type="button" class="input_bg" value="保存设置"
										onclick="mysubmit();">
								
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
				var processTypeId="${processTypeId}";
				var str="";
			    for (i=0;i<select1.options.length;i++){
			    	str+=","+select1.options(i).value+"_"+i;
			    }
			    if(str==""){
					alert("没有待配置的表单！");
					return;			    
			    }else{
			    	str=str.substring(1);
			    }
			 	$.post("<%=path%>/formconfig/config!config.action",
           			{"processTypeId":processTypeId,"sequeceStr":str},
           			function(data){
	    				if(data=="1"){
	    					alert("配置成功！")
		      				window.close();
		    			}else{
							 alert("配置失败！");
						}
		  			}
		  		);
			}
		</SCRIPT>
	</body>
</html>
