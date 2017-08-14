<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@taglib prefix="s" uri="/struts-tags"%>
<%@include file="/common/include/rootPath.jsp"%>
<html>
	<head>
		<title>界面快捷设置</title>
		<%@include file="/common/include/meta.jsp"%>
		<!--  引用公共样式文件,建议所有样式都以文件方式定义在jsp文件外部,通常定义在WebRoot目录下的CSS文件夹下-->
	     <LINK href="<%=frameroot%>/css/properties_windows_special.css" type=text/css rel=stylesheet>
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
			<style type="text/css">
		 .desktopmenu {
		   background-color: #ededed;
		   
		 }
		 .table1{
		 border-left:solid 1px #808080;
		 border-top:solid 1px #808080;
		 }
		 .table1 td{
		 border-bottom:solid 1px #808080;
		 border-right:solid 1px #808080;
		 }
		 .select1{
		 margin:-2px; width:250px;height:300px;align:center;border: none; background: none;
		 }
		  .select2{
		 margin:-2px; width:270px;height:300px;align:center;border: none; background: none;
		 }
		 .select1,.select2{background-color:#ffffff;}
		</style>
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
	var selected =  $('select[name = select1] option:selected');
	if(selected.length == 0)
		return;
	selected.each(function(){
		var $matchedOpts = $('select[name = select2] option[value^=' + $(this).val().substring(0,8) + ']');
		if($matchedOpts.length == 0){
			$(this).insertAfter($('select[name = select2] option[isParent=' + $(this).val().substring(0,8) + ']'));
			return;
		}
		var prev = $([]);
		var that = this;
		$matchedOpts.each(function(){
			if(parseInt($(this).val().substring(8)) > parseInt($(that).val().substring(8))){
				$(that).insertBefore(this);
				return false;
			}else
				if($(this).next().val() == 'MENU_SORT' || !$(this).next().is('option'))
				{
					$(that).insertAfter(this);
				}
		});
	});
}

function func_select_all1()
{
 for (i=select1.options.length-1; i>=0; i--)
   select1.options(i).selected=true;
}

function func_select_all2()
{  
	  
	 for (var i=0;i<select2.options.length;i++){      
		 var temOption=select2.options[i];       
		 temOption.selected=true;       
		 }   
 //for (i=select2.options.length-1; i>=0; i--)
  // select2.options(i).selected=true;
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
     alert("调整菜单快捷组的项目顺序时，请选择其中一项。");
     return;
  }
  else if(sel_count>1)
  {
     alert("调整菜单快捷组的项目顺序时，只能选择其中一项。");
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

  var container = $('select[name=select1]');
  var targetElmt = $('select[name=select1] option:selected');
  var index = $('select[name=select1] option').index(targetElmt);
  container .scrollTop(index * 20 - 1 - container.height()/2);
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
     alert("调整菜单快捷组的项目顺序时，请选择其中一项。");
     return;
  }
  else if(sel_count>1)
  {
     alert("调整菜单快捷组的项目顺序时，只能选择其中一项。");
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

  var container = $('select[name=select1]');
  var targetElmt = $('select[name=select1] option:selected');
  var index = $('select[name=select1] option').index(targetElmt);
  container .scrollTop(index * 20 - 1 - container.height()/2);
}
</script>
		<s:head />
	</head>
	<body class=contentbodymargin oncontextmenu="return false;">
		<DIV id=contentborder align=center>
			<table width="100%" border="0" cellspacing="0" cellpadding="00">
				<tr>
				<td colspan="3" class="table_headtd">
					<table width="100%" border="0" cellspacing="0" cellpadding="00">
						<tr>
							<td class="table_headtd_img" >
								<img src="<%=frameroot%>/images/ico/ico.gif" >&nbsp;
							</td>
							<td align="left">
								<strong>个人快捷菜单设置</strong>
							</td>
							<td align="right">
								<table border="0" align="right" cellpadding="00" cellspacing="0">
					                <tr>
					                 	
					                 	<td width="7"><img src="<%=frameroot%>/images/ch_h_l.gif"/></td>
					                 	<td class="Operation_input" onclick="mysubmit();">&nbsp;保&nbsp;存&nbsp;设&nbsp;置&nbsp;</td>
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
						<table width="720px" border="1" cellpadding="0" cellspacing="0"
							align="center" class="table1" bordercolorlight="#808080"
							bordercolordark="#FFFFFF">
							<s:hidden id="id" name="modle.fastmenuId"></s:hidden>
							<s:hidden id="param" name="param"></s:hidden>
							<tr class="desktopmenu">
								<td align="center" class="biao_bg1">
									排序
								</td>
								<td align="center" >
									<b>系统快捷菜单项</b>
								</td>
								<td align="center" class="biao_bg1">
									选择
								</td>
								<td align="center" >
									<b>备选菜单项</b>
								</td>
							</tr>
							<tr>
								<td align="center">
								<a id="input_bg"  href="#" class="button" onclick="func_up();">↑</a>
									<br>
									<br>
									<a id="input_bg"  href="#" class="button" onclick="func_down();">↓</a>
								</td>
								<td valign="top" align="center" >
									<select name="select1" ondblclick="func_delete();"
										multiple="multiple" class="select1">
										<s:iterator value="#request.userFMList">
											<s:if test="privilSyscode==\"0001\"||privilSyscode==\"0002\"||privilSyscode==\"0003\"||privilSyscode==\"0004\"||privilSyscode==\"0005\"||privilSyscode==\"0006\"">
												<option value="MENU_SORT" myAttr="asdfas">
													------------<s:property value="privilName" />-------------
												</option>
											</s:if>
											<s:else>
												<option value="<s:property value="privilSyscode" />">
													<s:property value="privilName" />
												</option>			
											</s:else>	
										</s:iterator>
									</select>
									<a id="input_bg"  href="#" class="button" onclick="func_select_all1();">全 选</a>
								</td>
								<td align="center" class="biao_bg1">
								<a id="input_bg"  href="#" class="button" onclick="func_insert();">←</a>
									<br>
									<br>
									<a id="input_bg"  href="#" class="button" onclick="func_delete();">→</a>
								</td>
								<td align="center" valign="top" >
<!--									<select name="select2" ondblclick="func_insert();"-->
<!--										multiple="multiple" style="width:180px;height:300px">-->
<!--										<s:iterator value="#request.FMList">	-->
<!--											<%--					-->
<!--											<s:if test="privilSyscode==\"0001\"||privilSyscode==\"0002\"||privilSyscode==\"0003\"||privilSyscode==\"0004\"||privilSyscode==\"0005\"||privilSyscode==\"0006\"">-->
<!--											--%>-->
<!--											<s:if test="privilSyscode.length()==8">-->
<!--												<option value="MENU_SORT">-->
<!--													------------<s:property value="privilName" />--------------->
<!--												</option>-->
<!--											</s:if>-->
<!--											<s:else>-->
<!--												<option value="<s:property value="privilSyscode" />">-->
<!--													<s:property value="privilName" />-->
<!--												</option>			-->
<!--											</s:else>								-->
<!--										</s:iterator>-->
<!--									</select>-->
									${select2}
									<%--					<select name="select2" ondblclick="func_insert();"--%>
									<%--						multiple="multiple" style="width:200px;;height:300px">							--%>
									<%--						<option value="MENU_SORT">--%>
									<%--							-------[领导办公]---------%>
									<%--						</option>--%>
									<%--						<option value="130">--%>
									<%--							在办工作--%>
									<%--						</option>--%>
									<%--						<option value="182">--%>
									<%--							新建工作--%>
									<%--						</option>--%>
									<%--						<option value="MENU_SORT">--%>
									<%--							-------[个人办公]---------%>
									<%--						</option>--%>
									<%--						<option value="MENU_SORT">--%>
									<%--							-------[综合办公]---------%>
									<%--						</option>--%>
									<%--							<option value="MENU_SORT">--%>
									<%--							-------[行政办公]---------%>
									<%--						</option>--%>
									<%--						<option value="MENU_SORT">--%>
									<%--							-------[系统管理]---------%>
									<%--						</option>--%>
									<%--					</select>--%>
									<a id="input_bg"  href="#" class="button" onclick="func_select_all2();">全 选</a>
								</td>
							</tr>
							<tr class="desktopmenu" >
								<td align="center" height="40px"  colspan="4" >
									点击条目时，可以组合CTRL或SHIFT键进行多选
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
			   var param=document.getElementById("param").value;
			   for (i=0; i< select1.options.length; i++)
			   {
			      options_value=select1.options(i).value;
			      fld_str+=options_value+",";
			    }
			    fld_str=fld_str.substring(0, fld_str.length - 1);
			    var id=document.getElementById("id").value;
			    var sarray=new Array();   
  				sarray=fld_str.split(',');   
			    if(sarray.length>6){
			    	alert("对不起,最多只能设置六个模块。");
			    	return false;
			    }
			    
			 	$.post("<%=path%>/shortcutmenu/fastMenu!save.action",
			           {"modleIds":fld_str,'id':id,"param":param},
			           function(msg){
						    if(msg!=""){
						      alert(msg);
						      return;
						    }
			    	});

				//top.perspective_top.location="<%=path%>/shortcutmenu/fastMenu!save.action?modleIds="+fld_str+"&id="+id+"&param="+param;
			}
		</SCRIPT>
	</body>
</html>
