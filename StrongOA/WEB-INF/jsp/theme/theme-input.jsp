<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="/tags/web-newdate" prefix="strong"%>
<%@taglib prefix="s" uri="/struts-tags"%>
<%@include file="/common/include/rootPath.jsp"%>
<%@ taglib uri="/tags/security" prefix="security"%>
<%
response.setHeader("Pragma","No-cache");
response.setHeader("Cache-Control","No-cache");
response.setDateHeader("Expires",-10);
%>
<html>
	<head>
		<title>界面设置</title>
		<%@include file="/common/include/meta.jsp"%>
		<!--  引用公共样式文件,建议所有样式都以文件方式定义在jsp文件外部,通常定义在WebRoot目录下的CSS文件夹下-->
	    <LINK href="<%=frameroot%>/css/properties_windows_special.css" type=text/css rel=stylesheet>
		<script type="text/javascript" language="javascript"
			src="<%=jsroot%>/jquery/jquery-1.2.6.js"></script>
		<script type="text/javascript" language="javascript"
			src="<%=jsroot%>/common/common.js"></script>
		<script type="text/javascript" language="javascript"
			src="<%=jsroot%>/common/search.js"></script>
		<script type="text/javascript" language="javascript"
			src="<%=jsroot%>/upload/jquery.MultiFile.js"></script>
		<script type="text/javascript" language="javascript"
			src="<%=jsroot%>/upload/jquery.blockUI.js"></script>
		<!-- <script type="text/javascript" language="javascript" src="<%=path %>/oa/js/theme/weather.js"></script>-->
		
		<script type="text/javascript">
		
		//主题重置
		function resett(){
		    location = "<%=root%>/theme/theme!reset.action";
		    location = "<%=root%>/theme/theme!input.action?privilSyscode=000500040001";
		}
		//表单重置
		function resettAll(){
			document.getElementById('myForm').reset();
		}
		
		$(document).ready(function(){
			//初始化快捷菜单图标
  			var value=document.getElementById("baseMenuIcon").value;
  			$("input[name='baseMenuIcon']").each(function(){	
  				if($(this).val()==value){			
					$(this).attr("checked","checked");
				}
  			});
			//初始化浏览器窗口图标
			if(document.getElementById("baseWindowsTitle").value==null||document.getElementById("baseWindowsTitle").value==""){
			 	document.getElementById("baseWindowsTitle").value="协同办公软件";
			 }
			 /**
			//初始化默认个人文件柜大小 
			if(document.getElementById("baseFolderSize").value==null||document.getElementById("baseFolderSize").value==""){
				document.getElementById("baseFolderSize").value="20";
			}
			//初始化默认个人消息空间大小
			if(document.getElementById("baseMsgSize").value==null||document.getElementById("baseMsgSize").value==""){
				document.getElementById("baseMsgSize").value="20";
			}
			*/   
		});
		
		function submitForm(){			
			var k=0;
			var objs = document.getElementsByName("baseMenuIcon");	//菜单图标
			for(var i=0;i<objs.length;i++){
				if(objs[i].checked==true){
					document.getElementById("baseMenuIcon").value = objs[i].value;
					k++;
					break;
				}
			}
			if(k==0){
				alert("请选择菜单图标！");
				return false;
			}else{
				k=0;
			}	
								
			//var interfaceThemes = document.getElementById("baseInterfaceThemes").value;
			/**
			var gray = document.getElementById("topPicGray").value;
			var blue = document.getElementById("topPicBlue").value;
			var green = document.getElementById("topPicGreen").value;
			var red = document.getElementById("topPicRed").value;
			
			if(gray =="" || gray == null || gray == ""){
				alert("请上传主界面顶部灰色风格图片!");
			}				
			if(blue =="" || blue == null || blue == ""){				
				alert("请上传主界面顶部蓝色风格图片!");
			}			
			if(green =="" || green == null || green == ""){				
				alert("请上传主界面顶部绿色风格图片!");
			}			
			if(red == "" || red == null || red == ""){				
				alert("请上传主界面顶部红色风格图片!");
			}
			*/			
			/**
			//added by dengzc 2009年10月27日16:06:07
			//增加对文件柜数据输入验证
			var folderSize = $("#baseFolderSize").val();
			folderSize = $.trim(folderSize);
			if(folderSize != ""){
				if(isNaN(folderSize)){
					alert("请输入正确的文件柜大小！");
					return ;
				}else{
					folderSize = parseInt(folderSize);
					if(folderSize < 0){
						alert("请输入正确的文件柜大小！");
						return ;
					}
				}
			}
			$("#baseFolderSize").val(folderSize);
			//增加对个人消息空间大小输入验证
			var baseMsgSize = $("#baseMsgSize").val();
			baseMsgSize = $.trim(baseMsgSize);
			if(baseMsgSize != ""){
				if(isNaN(baseMsgSize)){
					alert("请输入正确的个人消息空间大小！");
					$("#baseMsgSize").focus();
					return ;
				}else{
					if(!isNumber(baseMsgSize)){
						alert("输入的大小<"+baseMsgSize+">格式不对\n请输入正整数！");
						$("#baseMsgSize").focus();
						return ;
					}
					baseMsgSize = parseInt(baseMsgSize);
					if(baseMsgSize < 0){
						alert("输入的大小<"+baseMsgSize+">格式不对\n请输入正整数！");
						$("#baseMsgSize").focus();
						return ;
					}
				}
			}
			$("#baseMsgSize").val(baseMsgSize);
			
			*/
			
			document.getElementById("myForm").submit();
		}
		
		function isNumber(oNum) {
			if(!oNum) return false;
			var strP=/^(([1-9][0-9]*)|([1-9]*))$/;
			if(!strP.test(oNum)) return false;
			try{
				if(parseFloat(oNum)!=oNum) return false;
			}catch(ex){
				return false;
			}
			return true;
		}
  		/** 
		$("#baseCity").change(function(){
			eval("this.location='/tqyb/weatherframe/"+$(this).value+"/1.html'");
	  		var value=document.getElementById("baseCity").value
			window.location="http://www.cma.gov.cn/tqyb/weatherframe/"+value+"/1.html";
			alert(window.frames[0].document.getElementById("Image11").value);
			alert(document.getElementById("myIframe").src)
	  		var text=window.frames[0].$(".city").text();
	    	alert(text)
			$("#city").text();
		 });		 
		 */			
		</script>
	</head>
		
	<body class=contentbodymargin oncontextmenu="return false;">
		<script type="text/javascript" language="javascript"
			src="<%=jsroot%>/newdate/WdatePicker.js"></script>
		<DIV id=contentborder align=center>
			<s:form action="/theme/theme!save.action" id="myForm" theme="simple" enctype="multipart/form-data" >			
				<input id="baseId" name="baseId" type="hidden" size="32" value="${modle.baseId}">
				
				<table width="100%" border="0" cellspacing="0" cellpadding="00">
				<tr>
				<td colspan="3" class="table_headtd">
					<table width="100%" border="0" cellspacing="0" cellpadding="00">
						<tr>
							<td class="table_headtd_img" >
								<img src="<%=frameroot%>/images/ico/ico.gif" >&nbsp;
							</td>
							<td align="left">
								<strong>界面设置</strong>
							</td>
							<td align="right">
								<table border="0" align="right" cellpadding="00" cellspacing="0">
					                <tr>
					                    <td width="7"><img src="<%=frameroot%>/images/ch_h_l.gif"/></td>
					                 	<td class="Operation_input" onclick="submitForm()">&nbsp;保&nbsp;存&nbsp;</td>
					                 	<td width="7"><img src="<%=frameroot%>/images/ch_h_r.gif"/></td>
				                  		<td width="5"></td>
					                 	<%--<td width="7"><img src="<%=frameroot%>/images/ch_z_l.gif"/></td>
					                 	<td class="Operation_input1" onclick="resett()">&nbsp;主题重置&nbsp;</td>
					                 	<td width="7"><img src="<%=frameroot%>/images/ch_z_r.gif"/></td>
				                  		<td width="5"></td> --%>
					                 	<td width="8"><img src="<%=frameroot%>/images/ch_z_l.gif"/></td>
					                 	<td class="Operation_input1"  onclick="resettAll()">&nbsp;重&nbsp;置&nbsp;</td>
					                 	<td width="7"><img src="<%=frameroot%>/images/ch_z_r.gif"/></td>
				                  		<td width="6"></td>
					                </tr>
					            </table>
							</td>
						</tr>
					</table>
				</td>
				</tr>
				<tr height="10px">
				  <td>&nbsp;</td>
				</tr>
				<tr>
					<td>
					
				<fieldset style="width: 95%" align=center>
					<legend>
						<span class="wz"  style='height:25px'>界面主题与菜单 </span>
					</legend>
					<table width="100%" border="0" cellpadding="0" cellspacing="0"
						class="table1" style="border-top:0px;">												
						<tr>
							<td class="biao_bg1" align="right">
								<span class="wz"><font color=red>*</font>&nbsp;界面主题：</span>
							</td>
							<td class="td1">
									<s:select cssStyle="width: 170px;"
										list="#{'灰色-14号字体':'/frame/theme_gray',
												'蓝色-14号字体':'/frame/theme_blue',
												'绿色-14号字体':'/frame/theme_green',
												'红色-14号字体':'/frame/theme_red'}"
										id="baseInterfaceThemes" name="baseInterfaceThemes"
										listKey="value" listValue="key">
									</s:select>
								</td>
						</tr>						
						<!--<tr>
							<td  height="21" class="biao_bg1" align="right">
								<span class="wz">登录界面风格：</span>
							</td>
							<td class="td1" >
								<s:select cssStyle="width: 70px"
									list="#{
											'默认':'default',
											'门户':'portal',
											'简洁':'simple',
											'新闻':'news'}"
									id="baseLoginThemes" name="baseLoginThemes"
									listKey="value" listValue="key">
								</s:select>
							</td>
						</tr>-->						
						<tr>
							<td  height="21" class="biao_bg1" align="right">
								<span class="wz">默认展开菜单：</span>
							</td>
							<td class="td1" >
								<s:select cssStyle="width: 170px;" list="%{defaultMenuMap}"
									id="baseDefaultStartMenu" name="baseDefaultStartMenu"
									listKey="key" listValue="value" theme="simple">
								</s:select>
							</td>
						</tr>						
						<tr>
							<td  height="21" class="biao_bg1" align="right">
								<span class="wz"><FONT color="red">*</FONT>&nbsp;快捷菜单图标：</span>
							</td>
							<s:hidden id="baseMenuIcon" name="modle.baseMenuIcon"></s:hidden>
							<td class="td1"  align="left">
								<%-- <input type="radio" name="baseMenuIcon" value="0">
								<label for="MENU_IMAGE0" style="cursor: hand">
									每个菜单使用不同图标
								</label>--%>
								<input type="radio" name="baseMenuIcon" value="1">
								<label for="MENU_IMAGE1" style="cursor: hand">
									不显示菜单图标
								</label>
								<input type="radio" name="baseMenuIcon" value="01.gif">
								<img src="<%=root%>/images/ico/01.gif" align=absmiddle>

								<input type="radio" name="baseMenuIcon" value="02.gif">
								<img src="<%=root%>/images/ico/02.gif" align=absmiddle>
								
								<input type="radio" name="baseMenuIcon" value="03.gif">
								<img src="<%=root%>/images/ico/03.gif" align=absmiddle>

							</td>
						</tr>						
					</table>
				</fieldset>	
				</td>
				</tr>
					
				<tr height="10px">
				  <td>&nbsp;</td>
				</tr>		
				<tr>
					<td>
				<fieldset style="width: 95%" align=center>
					<legend>
						<span class="wz"  style='height:25px'>登录选项</span>
					</legend>
					<table width="100%" border="0" cellpadding="0" cellspacing="0"
						class="table1" style="border-top:0px;">						
						<tr>
							<td  height="21" class="biao_bg1" align="right">
								<span class="wz">登录界面图片：</span>
							</td>
							<td class="td1"  align="left">
								<input class="upFileBtn"  type="file" id="files" name="files"/>
							</td>
						</tr>						
					</table>
				</fieldset>
				</td>
				</tr>
				<tr height="10px">
				  <td>&nbsp;</td>
				</tr>
				<tr>
					<td>
				<fieldset style="width: 95%" align=center>
					<legend>
						<span class="wz"  style='height:25px'>主界面设置</span>
					</legend>
					<table width="100%" border="0" cellpadding="0" cellspacing="0"
						class="table1" style="border-top:0px;">						
						<tr>
							<td  height="21" class="biao_bg1" align="right">
								<span class="wz">浏览器窗口标题：</span>
							</td>
							<td class="td1"  align="left">
								<input id="baseWindowsTitle" name="baseWindowsTitle" "
									type="text" maxlength="30" size="30"
									value="${modle.baseWindowsTitle}">
								&nbsp;&nbsp;
								<font color="#999999">(默认为'协同办公软件')</font>
							</td>
						</tr>
						
						<tr>
							<td  height="21" class="biao_bg1" align="right" title="上传图片规格为(1366×90)">
								<span class="wz">顶部灰色主题：</span>
							</td>
							<td class="td1"  align="left" title="上传图片规格为(1366×90)">
								<input class="upFileBtn" type="file" name="topPicGray" id="topPicGray">
								图片预览：<img src="<%=root%>/oa/image/topPic/testGray.jpg?tempid=<%=System.currentTimeMillis() %>" width="150" height="30"/ >
							</td>							
						</tr>
						
						<tr>
							<td  height="21" class="biao_bg1" align="right" title="上传图片规格为(1366×90)">
								<span class="wz">顶部蓝色主题：</span>
							</td>
							<td class="td1"  align="left" title="上传图片规格为(1366×90)">
								<input class="upFileBtn" type="file" name="topPicBlue" id="topPicBlue">
								图片预览：<img src="<%=root%>/oa/image/topPic/testBlue.jpg?tempid=<%=System.currentTimeMillis() %>" width="150" height="30"/ >
							</td>
						</tr>
						
						<tr>
							<td  height="21" class="biao_bg1" align="right" title="上传图片规格为(1366×90)">
								<span class="wz">顶部绿色主题：</span>
							</td>
							<td class="td1"  align="left" title="上传图片规格为(1366×90)">
								<input class="upFileBtn" type="file" name="topPicGreen" id="topPicGreen">
								图片预览：<img src="<%=root%>/oa/image/topPic/testGreen.jpg?tempid=<%=System.currentTimeMillis() %>" width="150" height="30"/ >
							</td>
						</tr>

						<tr>
							<td height="21" class="biao_bg1" align="right" title="上传图片规格为(1366×90)">
								<span class="wz">顶部红色主题：</span>
							</td>
							<td class="td1"  align="left" title="上传图片规格为(1366×90)">
								<input class="upFileBtn" type="file" name="topPicRed" id="topPicRed">
								图片预览：<img src="<%=root%>/oa/image/topPic/testRed.jpg?tempid=<%=System.currentTimeMillis() %>" width="150" height="30"/ >
							</td>
						</tr>
												
					</table>
				</fieldset>
				
				
				</td>
				</tr>
				<tr height="10px">
				  <td>&nbsp;</td>
				</tr>
				<tr>
					<td>
				<fieldset style="width: 95%" align=center>
					<legend>
						<span class="wz"  style='height:25px'>状态栏选项</span>
					</legend>
					<table width="100%" border="0" cellpadding="0" cellspacing="0"
						class="table1" style="border-top:0px;">
						
						<tr>
							<td  height="21" class="biao_bg1" align="right">
								<span class="wz">底部状态栏文字：</span>
							</td>
							<td class="td1"  align="left">
								<input id="baseStatusbar" name="baseStatusbar" type="text" "
									size="30" maxlength="30" value="${modle.baseStatusbar }">
							</td>
						</tr>
						<tr>
							<td  height="21" class="biao_bg1" align="right">
								<span class="wz">状态栏切换时间：</span>
							</td>
							<td class="td1"  align="left">
								<input id="baseStatusbarTime" name="baseStatusbarTime" "
									type="text" size="30" value="${modle.baseStatusbarTime }">
								秒
							</td>
						</tr>
				         
					</table>
				</fieldset>
				
			  <!--
				<br>
				<fieldset style="width: 95%">
					<legend>
						<span class="wz">天气预报</span>
					</legend>
					<table width="100%" border="0" cellpadding="0" cellspacing="1"
						class="table1">				
	
						<tr>
							<td width="30%" height="21" class="biao_bg1" align="right">
							<span class="wz">是否显示：</span>
							</td>
							<td class="td1" width="70%" align="left">
								<input type="checkbox" name="baseIsShow" id="baseIsShow" checked>
							</td>
						</tr>	
						<tr>
							<td width="30%" height="21" class="biao_bg1" align="right">
								<span class="wz">默认城市：</span>
							</td>
		    				<td class="td1" width="70%" align="left">
		 						 <select id="province" name="PROVINCE" onChange="Province_onchange(this.options.selectedIndex);">
		   						<option value="选择省">选择省</option>
		 						 </select>
		 						<s:hidden id="baseCityValue" name="modle.baseCity"/>
		  						<select id="baseCity" name="baseCity">
		    						<option value="选择城市">选择城市</option>
		  						</select>
		    				</td>
		   				</tr>
						<tr>
							<td height="21" width="30%" class="biao_bg1" align="right">
								<span class="wz">用户上传头像：</span>
							</td>
							<td class="td1" width="70%" align="left">
								<input id="fileno" name="content" type="text" size="22">
							</td>
						</tr>
					</table>
				</fieldset>
				<br>
				<fieldset style="width: 95%">
					<legend>
						<span class="wz">默认设置</span>
					</legend>
					<table width="100%" border="0" cellpadding="0" cellspacing="1"
						class="table1">	
						
						<tr>
							<td width="30%" height="21" class="biao_bg1" align="right">
								<span class="wz">默认个人文件柜大小：</span>
							</td>
							<td class="td1" width="70%" align="left">
								<input id="baseFolderSize" name="baseFolderSize"
									type="text" size="30" maxlength="3" value="${modle.baseFolderSize }">
								MB
							</td>
						</tr>
						
						<tr>
							<td width="30%" height="21" class="biao_bg1" align="right">
								<span class="wz">默认个人消息空间大小：</span>
							</td>
							<td class="td1" width="70%" align="left">
								<input id="baseMsgSize" name="model.baseMsgSize"
									type="text" size="30" maxlength="4" value="${modle.baseMsgSize }">
								MB
							</td>
						</tr>						
					</table>
				</fieldset>								
			  -->
				
						</td>
					</tr>
					<tr>
							<td class="table1_td"></td>
							<td></td>
						</tr>
				</table>
			</s:form>
		</DIV>
		<!--  
		<div id="weather" title="点击城市名称可以更改城市">
			<span> <span id="city"></span> <span id="picture"></span> <span
				id="desc"></span> <span id="descs"></span> </span>
		</div>
		-->
	</body>
</html>