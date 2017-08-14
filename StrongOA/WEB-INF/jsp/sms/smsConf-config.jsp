<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<!--  引用标签处-->
<%@ taglib uri="/tags/web-flex" prefix="webflex"%>
<!--  引用公共文件,以下两个文件是建议包含-->
<%@ include file="/common/include/rootPath.jsp"%>
<%@ page import="com.strongit.bo.ListTest"%>
<HTML>
	<HEAD>
		<%@include file="/common/include/meta.jsp"%>
		<!--  引用公共样式文件,建议所有样式都以文件方式定义在jsp文件外部,通常定义在WebRoot目录下的CSS文件夹下-->
		<link href="<%=frameroot%>/css/properties_windows.css" type="text/css" rel="stylesheet">
		<LINK href="<%=frameroot%>/css/search.css" type=text/css rel=stylesheet>
		<!--  引用公共及自定义js文件,建议js都以文件方式定义在jsp文件外部,通常定义在WebRoot目录下的js文件夹下-->
		<script language='javascript' src='<%=path%>/common/js/grid/ChangeWidthTable.js'></script>
		<script language='javascript' src='<%=path%>/common/js/jquery/jquery-1.2.6.js'></script>
		<script language='javascript' src="<%=path%>/common/js/common/common.js"></script>
		<script language='javascript' src='<%=path%>/common/js/common/search.js'></script>
		<SCRIPT language="javascript" src="<%=path%>/common/js/menu/menu.js"></SCRIPT>
		<TITLE>短信通道列表</TITLE>
		<script type="text/javascript">
		function eToStr(type){
			if(type=="true"){
				return "开启";
			}else{
				return "关闭"
			}
		}
	</script>
	<BODY class=contentbodymargin oncontextmenu="return false;" onload=initMenuT()>
		<DIV id=contentborder align=center>
			<table width="100%" border="0" cellspacing="0" cellpadding="00">
				<tr>
					<td height="100%">
						<table width="100%" border="0" cellspacing="0" cellpadding="00">
							<tr>
								<td height="40"
									style="FILTER:progid:DXImageTransform.Microsoft.Gradient(gradientType=0,startColorStr=#ededed,endColorStr=#ffffff);">
									<table width="100%" border="0" cellspacing="0" cellpadding="00">
										<tr>
											<td width="30%">
												&nbsp;<img src="<%=frameroot%>/images/ico/ico.gif" width="9" height="9">&nbsp;
												短信通道列表
											</td>
											<td width="70%">
												<table border="0" align="right" cellpadding="00" cellspacing="0">
													<tr>
														<td width="*">
															&nbsp;
														</td>
														<%--
															<td width="19"><img src="<%=root%>/images/ico/kaiqi.gif" width="15" height="15"></td>
											                <td width="33"><a href="#" onclick="gotoAdd()">添加</a></td>
										                --%>
														<td>
															<a class="Operation" href="javascript:gotoEdit();">
																<img src="<%=root%>/images/ico/write.gif" width="15" height="15" class="img_s">
																配置&nbsp;</a>
														</td>
														<td width="19">
															<a class="Operation" href="javascript:startUse();">
																<img src="<%=root%>/images/ico/queding.gif" width="15" height="15" class="img_s">
																开启&nbsp;</a>
														</td>
														<td>
															<a class="Operation" href="javascript:noUse();">
																<img src="<%=root%>/images/ico/quxiao.gif" width="15" height="15" class="img_s">
																关闭&nbsp;</a>
														</td>
													</tr>
												</table>
											</td>
										</tr>
									</table>
								</td>
							</tr>
						</table>

						<webflex:flexTable name="myTable" width="100%" height="365px"
							wholeCss="table1" property="id" isCanDrag="false"
							isCanFixUpCol="true" clickColor="#A9B2CA" footShow="showCheck"
							getValueType="getValueByProperty" collection="${listphone}">
							<webflex:flexCheckBoxCol caption="选择" property="chinamobile_type"
								showValue="chinamobile_desc" width="5%" isCheckAll="false" isCanDrag="false"
								isCanSort="false"></webflex:flexCheckBoxCol>
							<webflex:flexTextCol caption="通道名称" property="chinamobile_desc"
								showValue="chinamobile_desc" width="15%" isCanDrag="true" isCanSort="true"></webflex:flexTextCol>
							<webflex:flexTextCol caption="数据库IP" property="chinamobile_dbip"
								showValue="chinamobile_dbip" width="15%" isCanDrag="true" isCanSort="true"></webflex:flexTextCol>
							<webflex:flexTextCol caption="数据库名称" property="chinamobile_dbusername"
								showValue="chinamobile_dbusername" width="15%" isCanDrag="true"
								isCanSort="true"></webflex:flexTextCol>
							<webflex:flexTextCol caption="数据库密码" property="chinamobile_dbpword"
								showValue="chinamobile_dbpword" width="15%" isCanDrag="true"
								isCanSort="true"></webflex:flexTextCol>
							<webflex:flexTextCol caption="MAS用户名" property="chinamobile_masusername"
								showValue="chinamobile_masusername" width="15%" isCanDrag="true"
								isCanSort="true"></webflex:flexTextCol>
							<webflex:flexTextCol caption="MAS密 码" property="chinamobile_maspword"
								showValue="chinamobile_maspword" width="10%" isCanDrag="true"
								isCanSort="true"></webflex:flexTextCol>
							<webflex:flexTextCol caption="是否启用" property="chinamobile_isopen"
								showValue="javascript:eToStr(chinamobile_isopen)" width="10%"
								isCanDrag="true" isCanSort="true"></webflex:flexTextCol>
						</webflex:flexTable>
						
						<webflex:flexTable name="myTable2" width="100%" height="365px"
							wholeCss="table1" property="id" isCanDrag="false"
							isCanFixUpCol="true" clickColor="#A9B2CA" footShow="showCheck"
							getValueType="getValueByProperty" collection="${listphone}">
							<%--<webflex:flexCheckBoxCol caption="选择"  property="gsmmodem_type"
								showValue="联通通道" width="5%" isCheckAll="false" isCanDrag="true"
								isCanSort="false"></webflex:flexCheckBoxCol>
							--%>
							<webflex:flexTextCol caption="" property=""
								showValue="" width="5%" isCanDrag="true" isCanSort="true"></webflex:flexTextCol>
							<webflex:flexTextCol caption="通道名称" property="gsmmodem_desc"
								showValue="联通通道" width="15%" isCanDrag="true" isCanSort="true"></webflex:flexTextCol>
							<webflex:flexTextCol caption="数据库IP" property="chinamobile_dbip"
								showValue="192.168.1.2" width="15%" isCanDrag="true" isCanSort="true"></webflex:flexTextCol>
							<webflex:flexTextCol caption="数据库名称" property="chinamobile_dbusername"
								showValue="oracle" width="15%" isCanDrag="true"
								isCanSort="true"></webflex:flexTextCol>
							<webflex:flexTextCol caption="数据库密码" property="chinamobile_dbpword"
								showValue="112" width="15%" isCanDrag="true"
								isCanSort="true"></webflex:flexTextCol>
							<webflex:flexTextCol caption="MAS用户名" property="chinamobile_masusername"
								showValue="mas11" width="15%" isCanDrag="true"
								isCanSort="true"></webflex:flexTextCol>
							<webflex:flexTextCol caption="MAS密 码" property="chinamobile_maspword"
								showValue="33" width="10%" isCanDrag="true"
								isCanSort="true"></webflex:flexTextCol>
							<webflex:flexTextCol caption="是否启用" property="isopen"
								showValue="javascript:eToStr(false)" width="10%"
								isCanDrag="true" isCanSort="true"></webflex:flexTextCol>
						</webflex:flexTable>
						
							<webflex:flexTable name="myTable3" width="100%" height="365px"
							wholeCss="table1" property="id" isCanDrag="false"
							isCanFixUpCol="true" clickColor="#A9B2CA" footShow="showCheck"
							getValueType="getValueByProperty" collection="${listphone}">
							<%--<webflex:flexCheckBoxCol caption="选择" property="gsmmodem_type"
								showValue="电信通道" width="5%" isCheckAll="true" isCanDrag="false"
								isCanSort="false"></webflex:flexCheckBoxCol>
							--%>
							<webflex:flexTextCol caption="" property=""
								showValue="" width="5%" isCanDrag="true" isCanSort="true"></webflex:flexTextCol>
							<webflex:flexTextCol caption="通道名称" property="gsmmodem_desc"
								showValue="电信通道" width="15%" isCanDrag="true" isCanSort="true"></webflex:flexTextCol>
							<webflex:flexTextCol caption="数据库IP" property="chinamobile_dbip"
								showValue="192.168.44.2" width="15%" isCanDrag="true" isCanSort="true"></webflex:flexTextCol>
							<webflex:flexTextCol caption="数据库名称" property="chinamobile_dbusername"
								showValue="oracle" width="15%" isCanDrag="true"
								isCanSort="true"></webflex:flexTextCol>
							<webflex:flexTextCol caption="数据库密码" property="chinamobile_dbpword"
								showValue="11233" width="15%" isCanDrag="true"
								isCanSort="true"></webflex:flexTextCol>
							<webflex:flexTextCol caption="MAS用户名" property="chinamobile_masusername"
								showValue="mas311" width="15%" isCanDrag="true"
								isCanSort="true"></webflex:flexTextCol>
							<webflex:flexTextCol caption="MAS密 码" property="chinamobile_maspword"
								showValue="333" width="10%" isCanDrag="true"
								isCanSort="true"></webflex:flexTextCol>
							<webflex:flexTextCol caption="是否启用" property="isopen"
								showValue="javascript:eToStr(false)" width="10%"
								isCanDrag="true" isCanSort="true"></webflex:flexTextCol>
						</webflex:flexTable>
						
						<webflex:flexTable name="myTable4" width="100%" height="365px"
							wholeCss="table1" property="id" isCanDrag="false"
							isCanFixUpCol="true" clickColor="#A9B2CA" footShow="showCheck"
							getValueType="getValueByProperty" collection="${listphone}">
							<webflex:flexCheckBoxCol caption="选择" property="gsmmodem_type"
								showValue="gsmmodem_desc" width="5%" isCheckAll="false" isCanDrag="false"
								isCanSort="false"></webflex:flexCheckBoxCol>
							<webflex:flexTextCol caption="通道名称" property="gsmmodem_desc"
								showValue="gsmmodem_desc" width="15%" isCanDrag="true" isCanSort="true"></webflex:flexTextCol>
							<webflex:flexTextCol caption="轮循间隔" property="gsmmodem_smsSystemRate"
								showValue="gsmmodem_smsSystemRate" width="15%" isCanDrag="true" isCanSort="true"></webflex:flexTextCol>
							<webflex:flexTextCol caption="通信端口" property="gsmmodem_smscomPort"
								showValue="gsmmodem_smscomPort" width="15%" isCanDrag="true" isCanSort="true"></webflex:flexTextCol>
							<webflex:flexTextCol caption="传输比率" property="gsmmodem_smscomBps"
								showValue="gsmmodem_smscomBps" width="15%" isCanDrag="true"
								isCanSort="true"></webflex:flexTextCol>
							<webflex:flexTextCol caption="SIM卡号码" property="gsmmodem_smscomSimnum"
								showValue="gsmmodem_smscomSimnum" width="15%" isCanDrag="true"
								isCanSort="true"></webflex:flexTextCol>
							<webflex:flexTextCol caption="设备名称" property="gsmmodem_smscomName"
								showValue="gsmmodem_smscomName" width="10%" isCanDrag="true"
								isCanSort="true"></webflex:flexTextCol>
							<webflex:flexTextCol caption="是否启用" property="gsmmodem_isopen"
								showValue="javascript:eToStr(gsmmodem_isopen)" width="10%"
								isCanDrag="true" isCanSort="true"></webflex:flexTextCol>
						</webflex:flexTable>
				</tr>
			</table>
		</DIV>
		<script language="javascript">
			var sMenu = new Menu();
			function initMenuT(){
				sMenu.registerToDoc(sMenu);
				var item = null;
				item = new MenuItem("<%=root%>/images/ico/write.gif","配置","gotoEdit",3,"ChangeWidthTable","checkOneDis");
				sMenu.addItem(item);
			//	item = new MenuItem("<%=root%>/images/ico/xiaohui.gif","删除","gotoRemove",3,"ChangeWidthTable","checkOneDis");
			//	sMenu.addItem(item);
				item = new MenuItem("<%=root%>/images/ico/queding.gif","开启","startUse",3,"ChangeWidthTable","checkOneDis");
				sMenu.addItem(item);
				item = new MenuItem("<%=root%>/images/ico/quxiao.gif","关闭","noUse",3,"ChangeWidthTable","checkOneDis");
				sMenu.addItem(item);
				sMenu.addShowType("ChangeWidthTable");
			    registerMenu(sMenu);
			}
			function gotoAdd(){
				var boo=OpenWindow('<%=root%>/fileNameRedirectAction.action?toPage=smsplatform/smsplatform-add.jsp', '400', '260', window);
				if(boo=="true"){
					window.location.reload();
				}
			}
			function gotoEdit(){
				var redId=getValue();
				if(redId==null||redId==""){
					alert("请您选中操作模块");
				}else{
					if(redId.indexOf(",")!=-1){
						alert("您一次只能操作一个模块");
					}else{
						var boo=OpenWindow('<%=root%>/sms/smsConf!editConfig.action?sendid='+redId, '400', '260', window);
						if(boo=="true"){
							window.location.reload();
						}
					}
				}
			}
			function gotoRemove(){
				if(confirm("删除后可能导致短信无法正常发送，您确定要删除该模块么?")==true){
					var redId=getValue();
					if(redId==null||redId==""){
						alert("请您选中要操作的模块");
					}else{
					  	$.ajax({
					  		type:"post",
					  		dataType:"text",
					  		url:"<%=root%>/sms/smsConf!delete.action",
					  		data:"sendid="+redId,
					  		success:function(msg){
					  			if(msg=="true"){
					  				alert("删除成功！");
					  				window.location.reload();
								}else{
									alert("删除失败");
					  			}
					  		}
					  	});
					}
				}
			}
			function startUse(){
				var redId=getValue();
				if(redId==null||redId==""||redId.indexOf(",")!=-1){
					alert("请您选中一个要操作的短信通道");
				}else if(curRow.cells[8].value=="true"){
					alert(curRow.cells[2].value+"已经开启");
				}else{
				  	$.ajax({
				  		type:"post",
				  		dataType:"text",
				  		url:"<%=root%>/sms/smsConf!isOpen.action",
				  		data:"phonetype="+redId,
				  		success:function(msg){
				  			if(msg=="true"){
				  				alert("开启成功！");
				  				window.location.reload();
							}else{
								alert("开启失败");
				  			}
				  		}
				  	});
				}
			}
			function noUse(){
				var redId=getValue();
				if(redId==null||redId=="" || redId.indexOf(",")!=-1){
					alert("请您选中一个要操作的短信通道");
				}else if(curRow.cells[8].value=="false"){
					alert(curRow.cells[2].value+"已经关闭");
				}else{
				  	$.ajax({
				  		type:"post",
				  		dataType:"text",
				  		url:"<%=root%>/sms/smsConf!isOpen.action",
				  		data:"phonetype="+redId,
				  		success:function(msg){
				  			if(msg=="true"){
				  				alert("关闭成功！");
				  				window.location.reload();
							}else{
								alert("关闭失败");
				  			}
				  		}
				  	});
				}
			}
		</script>
	</BODY>
</HTML>
