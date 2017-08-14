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
	<LINK href="<%=frameroot%>/css/properties_windows.css" type=text/css rel=stylesheet>
	<LINK href="<%=frameroot%>/css/search.css" type=text/css  rel=stylesheet>
	<!--  引用公共及自定义js文件,建议js都以文件方式定义在jsp文件外部,通常定义在WebRoot目录下的js文件夹下-->
	<script language='javascript' src='<%=path%>/common/js/grid/ChangeWidthTable.js'></script>
	<script language='javascript' src='<%=path%>/common/js/jquery/jquery-1.2.6.js'></script>
	<script language='javascript' src="<%=path%>/common/js/common/common.js" ></script>
	<script language='javascript' src='<%=path%>/common/js/common/search.js'></script>
		
		<TITLE>手机短信发送权限列表</TITLE>
		
					
		<script type="text/javascript">
		
		function sendsms(){
			var url = "<%=request.getContextPath()%>/collaborative_tools/sms/sms_send.jsp";
			var a = window.showModalDialog(url,window,'dialogWidth:450px;dialogHeight:360px;help:no;status:no');
		}
		
		function configsms(){
//			var url
			location = "<%=request.getContextPath()%>/collaborative_tools/sms/sms_config.jsp";
//			var a = window.showModalDialog(url,window,'dialogWidth:450px;dialogHeight:360px;help:no;status:no');
		}
		
		</script>
	</HEAD>
	<%
		List testList = new ArrayList();
		for (int i = 1; i <= 20; i++) {
			ListTest test = new ListTest();
			test.setId(new Long(i));
			test.setMoney("￥" + (i * 7));
			test.setDate(new Date());
			test.setNum(i * 7+1370791000);
			if (i % 2 == 0) {
				if(i%3==0){
					test.setName("打开");
				}else{
					test.setName("关闭");
				}
			} else {
				test.setName("打开");
			}
			test.setItem("用户"+i);
			testList.add(test);
		}
		request.setAttribute("testList", testList);
	%>
	<BODY class=contentbodymargin oncontextmenu="return false;"
		onload=initMenuT()>

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
											<td>
											&nbsp;
											</td>
											<td width="227">
												<img src="<%=frameroot%>/images/ico/ico.gif" width="9"
													height="9">&nbsp;
												手机短信权限列表
											</td>
											<td>
												&nbsp;
											</td>
											<td width="290">
											<table width="283" border="0" align="right" cellpadding="00" cellspacing="0">
								                <tr>
								                								                  <td width="*">&nbsp;</td>
								                  <td width="19"><img src="<%=root%>/images/ico/kaiqi.gif" width="15" height="15"></td>
								                  <td width="33">开启</td>
								                  <td width="20"><img src="<%=root%>/images/ico/guanbi.gif" width="15" height="15" ></td>
								                  <td width="34">关闭</td>

								                </tr>
								            </table>
											</td>
										</tr>
									</table>
								</td>
							</tr>
						</table>
						
						<webflex:flexTable name="myTable" width="100%" height="365px" wholeCss="table1" property="id" isCanDrag="true" isCanFixUpCol="true" clickColor="#A9B2CA" footShow="showCheck" getValueType="getValueByProperty" collection="<%=testList %>">
							<table width="100%" border="0" cellpadding="0" cellspacing="1" class="table1">
					        <tr>
					          <td width="5%" align="center"  class="biao_bg1"><img src="<%=root%>/images/ico/sousuo.gif" width="15" height="15" ></td>
					          <td width="45%"  class="biao_bg1"><input name="userName" id="userName" type="text" style="width=100%" class="search" title="输入用户名"></td>
					          <td width="50%"  class="biao_bg1">&nbsp;
					            <select style="width: 98%" name="menu1" onChange="MM_jumpMenu('parent',this,0)">
					              <option>打开</option>
					              <option>关闭</option>
					            </select></td>
					          <td class="biao_bg1">&nbsp;</td>
					          </tr>
					        </table>
							<webflex:flexCheckBoxCol caption="选择" property="id"
								showValue="name" width="5%" isCheckAll="true" isCanDrag="false"
								isCanSort="false"></webflex:flexCheckBoxCol>
							<webflex:flexTextCol caption="用户" property="item"
								showValue="item" width="45%" isCanDrag="true" isCanSort="true"></webflex:flexTextCol>
							<webflex:flexTextCol caption="发送权限" property="name"
								showValue="name" width="50%" isCanDrag="true" isCanSort="true"></webflex:flexTextCol>
						  </webflex:flexTable>
				</tr>
			</table>
		</DIV>
		<script language="javascript">
			var sMenu = new Menu();
			function initMenuT(){
				sMenu.registerToDoc(sMenu);
				var item = null;
				item = new MenuItem("<%=request.getContextPath()%>/common/frame/perspective_leftside/ico.gif","编辑","gotoEdit",3,"ChangeWidthTable","checkOneDis");
				sMenu.addItem(item);
				item = new MenuItem("<%=request.getContextPath()%>/common/frame/perspective_leftside/ico.gif","删除","gotoRemove",3,"ChangeWidthTable","checkOneDis");
				sMenu.addItem(item);
				sMenu.addShowType("ChangeWidthTable");
			    registerMenu(sMenu);
			}
		</script>	
	</BODY>
</HTML>
