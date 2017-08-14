<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<!--  引用标签处-->
<%@ taglib uri="/tags/web-flex" prefix="webflex"%>
<!--  引用公共文件,以下两个文件是建议包含-->
<%@include file="/common/include/rootPath.jsp" %>

<%@ page import="com.strongit.bo.ListTest"%>
<HTML>
	<HEAD>
	<%@include file="/common/include/meta.jsp" %>
	<!--  引用公共样式文件,建议所有样式都以文件方式定义在jsp文件外部,通常定义在WebRoot目录下的CSS文件夹下-->
	<link href="<%=frameroot%>/css/strongitmenu.css" type="text/css" rel="stylesheet"><!--右键菜单样式 -->
	<LINK href="<%=frameroot%>/css/properties_windows_list.css" type=text/css rel=stylesheet>
	<LINK href="<%=frameroot%>/css/search.css" type=text/css  rel=stylesheet>
	<!--  引用公共及自定义js文件,建议js都以文件方式定义在jsp文件外部,通常定义在WebRoot目录下的js文件夹下-->
	<script language='javascript' src='<%=path%>/common/js/grid/ChangeWidthTable.js'></script>
	<script language='javascript' src='<%=path%>/common/js/jquery/jquery-1.2.6.js'></script>
	<script language='javascript' src="<%=path%>/common/js/common/common.js" ></script>
	<script language='javascript' src='<%=path%>/common/js/common/search.js'></script>
	<SCRIPT language="javascript" src="<%=path%>/common/js/menu/menu.js"></SCRIPT>
	<TITLE>短信平台模块列表</TITLE>
	<script type="text/javascript">
		function eToStr(type){
			if(type=="1"){
				return "开启";
			}else{
				return "关闭"
			}
		}
	</script>
	<BODY class=contentbodymargin oncontextmenu="return false;" onload=initMenuT()>
		<DIV id=contentborder align=center>
			<%--<table width="100%" border="0" cellspacing="0" cellpadding="00">
				<tr>
					<td height="100%">
						<table width="100%" border="0" cellspacing="0" cellpadding="00">
							<tr>
								<td height="40"
									style="FILTER:progid:DXImageTransform.Microsoft.Gradient(gradientType=0,startColorStr=#ededed,endColorStr=#ffffff);">
									<table width="100%" border="0" cellspacing="0" cellpadding="00">
										<tr>
											<td width="30%">
												&nbsp;<img src="<%=frameroot%>/images/ico/ico.gif" width="9"
													height="9">&nbsp;
												短信平台模块列表
											</td>
											<td>
												&nbsp;
											</td>
											<td>
											<table border="0" align="right" cellpadding="00" cellspacing="0">
								                <tr>
								                  <td width="*">&nbsp;</td>
								                  <td width="19"><img src="<%=root%>/images/ico/kaiqi.gif" width="15" height="15"></td>
								                  <td width="33"><a href="#" onclick="gotoAdd()">添加</a></td>
								                  
													<td>
														<a class="Operation" href="javascript:gotoEdit();">
															<img src="<%=root%>/images/ico/bianji.gif" width="15" height="15" class="img_s">
															编辑&nbsp;</a>
													</td>
													<td width="5"></td>
													<td>
														<a class="Operation" href="javascript:startUse();">
															<img src="<%=root%>/images/ico/queding.gif" width="15" height="15" class="img_s">
															开启&nbsp;</a>
													</td>
													<td width="5"></td>
													<td>
														<a class="Operation" href="javascript:noUse();">
															<img src="<%=root%>/images/ico/quxiao.gif" width="15" height="15" class="img_s">
															关闭&nbsp;</a>
													</td>
													<td width="5"></td>
								                </tr>
								            </table>
											</td>
										</tr>
									</table>
								</td>
							</tr>
						</table>
						
						--%><table width="100%" border="0" cellspacing="0" cellpadding="00">
				<tr>
					<td height="100%">
						<table width="100%" border="0" cellspacing="0" cellpadding="00">
							<tr>
								<td height="40"
									style="FILTER:progid:DXImageTransform.Microsoft.Gradient(gradientType=0,startColorStr=#ededed,endColorStr=#ffffff);">
									
											<table width="100%" border="0" cellspacing="0" cellpadding="00">
						                       <tr>
							                    <td class="table_headtd_img" >
								                  <img src="<%=frameroot%>/images/ico/ico.gif" >&nbsp;
							                    </td>
							                    <td align="left">
								                    <strong>短信平台模块列表</strong>
							                    </td>
											
											<td width="70%">
											<table border="0" align="right" cellpadding="00" cellspacing="0">
								                  <tr>
								                   <td width="4"><img src="<%=frameroot%>/images/bt_l.jpg"/></td>
					                 	           <td class="Operation_list" onclick="gotoEdit();"><img src="<%=root%>/images/operationbtn/edit.png"/>&nbsp;编&nbsp;辑&nbsp;</td>
					                 	           <td width="4"><img src="<%=frameroot%>/images/bt_r.jpg"/></td>
				                  		           <td width="5"></td>
								                 	
								                	
								                	<td width="4"><img src="<%=frameroot%>/images/bt_l.jpg"/></td>
					                 	            <td class="Operation_list" onclick="startUse();"><img src="<%=root%>/images/operationbtn/open.png"/>&nbsp;开&nbsp;启&nbsp;</td>
					                 	            <td width="4"><img src="<%=frameroot%>/images/bt_r.jpg"/></td>
				                  		            <td width="5"></td>
								                	
							                  		
							                  		<td width="4"><img src="<%=frameroot%>/images/bt_l.jpg"/></td>
					                 	            <td class="Operation_list" onclick="noUse();"><img src="<%=root%>/images/operationbtn/close.png"/>&nbsp;关&nbsp;闭&nbsp;</td>
					                 	            <td width="4"><img src="<%=frameroot%>/images/bt_r.jpg"/></td>
				                  		            <td width="5"></td>
							                  		
								                 	
								                </tr>
								            </table>
											</td>
										</tr>
									</table>
								</td>
							</tr>
						</table>
					</td>
				 </tr>
						
						
						
						<tr>
						<webflex:flexTable name="myTable" width="100%" height="365px" wholeCss="table1" property="id" isCanDrag="true" isCanFixUpCol="true" clickColor="#A9B2CA" footShow="showCheck" getValueType="getValueByProperty" collection="${list}">
							<table width="100%" border="0" cellpadding="0" cellspacing="0" class="table1">
					        <%--<tr>
					          <td width="5%" align="center"  class="biao_bg1"><img src="<%=root%>/images/ico/sousuo.gif" width="17" height="16"></td>
					          <td width="45%"  class="biao_bg1"><input name="modelName" id="modelName" type="text" style="width=100%" class="search" title="请您输入模块名称"></td>
					          <td width="50%"  class="biao_bg1">&nbsp;
					            <select style="width: 98%" name="menu1" onChange="MM_jumpMenu('parent',this,0)">
					              <option>打开</option>
					              <option>关闭</option>
					            </select></td>
					          <td class="biao_bg1">&nbsp;</td>
					          </tr>
					        --%>
					        
					        
					        
					        
					        </table>
							<webflex:flexCheckBoxCol caption="选择" property="bussinessModuleId" showValue="bussinessModuleName" width="5%" isCheckAll="true" isCanDrag="false" isCanSort="false"></webflex:flexCheckBoxCol>
							<webflex:flexTextCol caption="模块编码" property="bussinessModuleCode" showValue="bussinessModuleCode" width="10%"  isCanDrag="true" isCanSort="true"></webflex:flexTextCol>
							<webflex:flexTextCol caption="模块名称" property="bussinessModuleName" showValue="bussinessModuleName" width="30%" isCanDrag="true" isCanSort="true"></webflex:flexTextCol>
							<webflex:flexTextCol caption="短信字数" property="wordNumber" showValue="wordNumber" width="25%" isCanDrag="true" isCanSort="true"></webflex:flexTextCol>
							<webflex:flexTextCol caption="是否启用" property="isEnable" showValue="javascript:eToStr(isEnable)" width="20%" isCanDrag="true" isCanSort="true"></webflex:flexTextCol>
							<webflex:flexTextCol caption="自增位长度" property="increaseLength" showValue="increaseLength" width="10%" isCanDrag="true" isCanSort="true"></webflex:flexTextCol>
						  </webflex:flexTable>
				</tr>
			</table>
		</DIV>
		<script language="javascript">
			var sMenu = new Menu();
			function initMenuT(){
				sMenu.registerToDoc(sMenu);
				var item = null;
				item = new MenuItem("<%=root%>/images/operationbtn/edit.png","编辑","gotoEdit",3,"ChangeWidthTable","checkOneDis");
				sMenu.addItem(item);
			//	item = new MenuItem("<%=root%>/images/ico/xiaohui.gif","删除","gotoRemove",3,"ChangeWidthTable","checkOneDis");
			//	sMenu.addItem(item);
				item = new MenuItem("<%=root%>/images/operationbtn/open.png","开启","startUse",3,"ChangeWidthTable","checkOneDis");
				sMenu.addItem(item);
				item = new MenuItem("<%=root%>/images/operationbtn/close.png","关闭","noUse",3,"ChangeWidthTable","checkOneDis");
				sMenu.addItem(item);
				sMenu.addShowType("ChangeWidthTable");
			    registerMenu(sMenu);
			}
			function gotoAdd(){
				var boo=OpenWindow('<%=root%>/fileNameRedirectAction.action?toPage=smsplatform/smsplatform-add.jsp', '500', '360', window);
				if(boo=="true"){
					window.location.reload();
				}
			}
			function gotoEdit(){
				var redId=getValue();
				if(redId==null||redId==""){
					alert("请您选中操作模块。");
				}else{
					if(redId.indexOf(",")!=-1){
						alert("您一次只能操作一个模块。");
					}else{
						var boo=OpenWindow('<%=root%>/smsplatform/smsPlatform!edit.action?sendid='+redId, '530', '360', window);
						if(boo=="true"){
							window.location.reload();
						}
					}
				}
			}
			function gotoRemove(){
				if(confirm("删除后可能导致短信无法正常发送，您确定要删除该模块么？")==true){
					var redId=getValue();
					if(redId==null||redId==""){
						alert("请您选中要操作的模块。");
					}else{
					  	$.ajax({
					  		type:"post",
					  		dataType:"text",
					  		url:"<%=root %>/smsplatform/smsPlatform!delete.action",
					  		data:"sendid="+redId,
					  		success:function(msg){
					  			if(msg=="true"){
					  				alert("删除成功。");
					  				window.location.reload();
								}else{
									alert("删除失败。");
					  			}
					  		}
					  	});
					}
				}
			}
			function startUse(){
				var redId=getValue();
				if(redId==null||redId==""){
					alert("请您选中要操作的模块。");
				}else{
				  	$.ajax({
				  		type:"post",
				  		dataType:"text",
				  		url:"<%=root %>/smsplatform/smsPlatform!open.action",
				  		data:"sendid="+redId,
				  		success:function(msg){
				  			if(msg=="true"){
				  				alert("开启成功。");
				  				window.location.reload();
							}else{
								alert("开启失败。");
				  			}
				  		}
				  	});
				}
			}
			function noUse(){
				var redId=getValue();
				if(redId==null||redId==""){
					alert("请您选中要操作的模块。");
				}else{
				  	$.ajax({
				  		type:"post",
				  		dataType:"text",
				  		url:"<%=root %>/smsplatform/smsPlatform!close.action",
				  		data:"sendid="+redId,
				  		success:function(msg){
				  			if(msg=="true"){
				  				alert("关闭成功。");
				  				window.location.reload();
							}else{
								alert("关闭失败。");
				  			}
				  		}
				  	});
				}
			}
		</script>	
	</BODY>
</HTML>
