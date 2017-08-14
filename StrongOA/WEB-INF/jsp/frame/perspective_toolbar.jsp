<%@ page contentType="text/html; charset=utf-8"%>
<%@include file="/common/include/rootPath.jsp"%>
<%@ page import="com.strongit.bo.Channel" %>
<%@ page import="java.util.*"%>
<html xmlns="http://www.w3.org/1999/xhtml">
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
		<title>公共top区</title>
		<link href="<%=path%>/common/frame/css/toolbar.css" rel="stylesheet"
			type="text/css" />
<SCRIPT language="javascript" src="<%=path%>/common/frame/js/personMenu.js"></SCRIPT>
		<style id="popupmanager">
.logo{
	font-family: "黑体";
	font-size: 32px;
	font-weight: bold;
	color: #FFFFFF;}
	
.popupMenu {
	width: 100px;
	border: 1px solid #666666;
	background-color: #F9F8F7;
	padding: 1px;
}

.popupMenuTable { /*
	background-image: url(/images/popup/bg_menu.gif);
	*/
	background-repeat: repeat-y;
}

.popupMenuTable TD {
	font-family: MS Shell Dlg;
	font-size: 12px;
	cursor: default;
}

.popupMenuRow {
	height: 21px;
	padding: 1px;
}

.popupMenuRowHover {
	height: 21px;
	border: 1px solid #0A246A;
	background-color: #B6BDD2;
}

.popupMenuSep {
	background-color: #A6A6A6;
	height: 1px;
	width: expression(parentElement . offsetWidth-27);
	position: relative;
	left: 28;
}
</style>
<script type="text/javascript">
function openLink(url){
	alert(url);
}
function mydesk(){	
	top.perspective_content.actions_container.personal_properties_content.location="<%=basePath%>/desktop/desktopWhole.action";
}
function myemail(){	
	top.perspective_content.actions_container.personal_properties_content.location="<%=basePath%>/fileNameRedirectAction.action?toPage=mymail/mail_container.jsp";
}
function mymsg(){	
	top.perspective_content.actions_container.personal_properties_content.location="<%=basePath%>";
}
function myinfo(){	
	top.perspective_content.actions_container.personal_properties_content.location="<%=basePath%>personal_office/myinfo/myinfo_index.jsp";
}
function notify(){
	top.perspective_content.actions_container.personal_properties_content.location="<%=basePath%>integrated_Office/notify/notify_recvlist.jsp";
}

function selectCommpass(){//全文检索
	var value=document.getElementById("textfield").value;
	if(value=="" || value=="全文检索"){
		alert("请输入检索内容");
		return false;
	}
	top.perspective_content.actions_container.personal_properties_toolbar.navigate("<%=basePath%>/search/search!searchContent.action?searchContent="+value,"全文检索");
}




</script>
</head>
<%
	List testList = new ArrayList();
	Channel test1 = new Channel();
	test1.setBlocktitle("个人桌面");
	test1.setBlockimg("desk.gif");
	test1.setBlocktpl("url");
	testList.add(test1);
	Channel test2 = new Channel();
	test2.setBlocktitle("我的邮件");
	test2.setBlockimg("mail.gif");
	test2.setBlocktpl("url");
	testList.add(test2);
	Channel test3 = new Channel();
	test3.setBlocktitle("我的信息");
	test3.setBlockimg("xx.gif");
	test3.setBlocktpl("url");
	testList.add(test3);
	Channel test4 = new Channel();
	test4.setBlocktitle("个人设置");
	test4.setBlockimg("set.gif");
	test4.setBlocktpl("url");
	testList.add(test4);
	Channel test5 = new Channel();
	test5.setBlocktitle("我的收藏");
	test5.setBlockimg("sc.gif");
	test5.setBlocktpl("url");
	testList.add(test5);
	for(int i=0;i<5;i++){
	Channel test = new Channel();
	test.setBlocktitle("个人设置");
	test.setBlockimg("sc.gif");
	test.setBlocktpl("url");
	testList.add(test5);
	}
	request.setAttribute("testList",testList);
%>
<body class=gtoolbarbodymargin>
<table width="100%" border="0" cellspacing="0" cellpadding="0">
  <tr>
    <td  valign="top" class="btbg">
    	<table width="100%" height="53" border="0" cellpadding="00" cellspacing="0" class="logo_bg">
      	<tr>
        	<td width="40"></td>
        	<td id="title" class="logo">协同办公软件</td>
        	<td width="500" valign="middle">
        		<table width="100%" border="0" cellspacing="0" cellpadding="00">
          			<tr>
            			<td width="40" height="37" class="help">用户：</td>
            			<td width="42" class="help">admin</td>
            			<td width="29" align="center" class="help"><img src="images/perspective_toolbar/help.gif" width="14" height="14" /></td>
            			<td width="42" class="help"> 帮助 </td>
            			<td width="28" align="center" class="help"><img src="images/perspective_toolbar/about.gif" width="14" height="14" /></td>
            			<td width="39" class="help">关于</td>
            			<td width="28" align="center" class="help"><img src="images/perspective_toolbar/exit.gif" width="14" height="14" /></td>
            			<td width="50" class="help">退出</td>
            			<td width="80">
							<select name="menu1" onchange="MM_jumpMenu('parent',this,0)">
                				<option>请选择</option>
              				</select>
						</td>
						
						
          			</tr>
        		</table>
        	</td>
     	</tr>
 		</table>
    </td>
  </tr>
</table>  
<table width="100%" border="0" cellspacing="0" cellpadding="0">
	<tr>
		<td bgcolor="#aca99a" height="1"></td>
	</tr>
	<tr>
		<td bgcolor="#FFFFFF" height="1"></td>
	</tr>
</table>
<table width="100%" border="0" cellpadding="0" cellspacing="0" bgcolor="#d4d0c8" >
	<tr>
		<td>
			<table width="100%" border="0" cellpadding="00" cellspacing="0" bgcolor="#d4d0c8">
            	<tr>
              		<td width="238">
              			<table width="100%" border="0" cellspacing="0" cellpadding="00">
                			<tr>
                  				<td width="28" align="center"><img src="images/perspective_toolbar/tz.gif" width="16" height="16" /></td>
                  				<td width="60">通知通告：</td>
                  				<td width="125">&nbsp;</td>              			
                			</tr> 
                		</table>           		
             		</td>
              		<td>
              			<table width="190" height="20" border="0" cellpadding="00" cellspacing="0">
                			<tr>
                  				<td width="154"><input name="textfield" type="text" value="全文检索" onfocus="if (value =='全文检索'){value =''}" onblur="if (value ==''){value='全文检索'}"  onkeypress="if(event.keyCode==13) selectCommpass();" class="keyword" size="22"></td>
                  				<img src="<%=frameroot%>/images/perspective_toolbar/sousuo.gif" width="15" height="18" onclick="selectCommpass()" style="cursor: hand;"  title="单击搜索">
                			</tr>
              			</table>
              		</td>
              		<td width="500" valign="bottom">
              			<table width="100%" border="0" cellpadding="0" cellspacing="0">
                			<tr>
                				<%
           						for(int i=0;i<5&&i<testList.size();i++){   	
                				Channel test=(Channel)testList.get(i);
                				%>
                  				<td width="20"><img src="images/perspective_toolbar/<%=test.getBlockimg()%>" width="16" height="16" onclick="openLink('<%=test.getBlocktpl()%>')" /></td>
                 				<td width="50" height="28"><%=test.getBlocktitle() %></td>
                  				<% 
                  				}
                 			 	%>
                  				<td width="90"  align="left">
                  				&nbsp;&nbsp;
                  				<span id="goright" style="cursor:hand"  onclick="showFav();">
                  				<img src="images/perspective_toolbar/jiantou1.gif"  width="13" height="9"/>
                  				</span>
                  				</td>
                  				<td></td>
               			 	</tr>
               			 </table>
             		</td>
            	</tr>
				<tr>
             		<td height="1" colspan="3" bgcolor="#aca99a"></td>
            	</tr>
				<tr>
                	<td height="1" colspan="3" bgcolor="#FFFFFF"></td>
            	</tr>
          	</table>
		 </td>
      </tr>
</table>
<div id="divFavContent" style="display: none">
		<div class="popupMenu">
			<%
				if(testList.size()>5){
				for(int i=6;i<testList.size();i++){
					Channel tests=(Channel)testList.get(i);
			 %>
		
				<table cellspacing="0" cellpadding="0" border="0" width="100%"
					height="100%" class="popupMenuTable">
					<tr height="22">
						<td class="popupMenuRow"
							onmouseover="this.className='popupMenuRowHover';"
							onmouseout="this.className='popupMenuRow';"
							id="popupWin_Menu_Setting">
							<table cellspacing="0" cellpadding="0" border="0" width="100%"
								height="100%">
								<tr>
									<td width="28">
										&nbsp;
									</td>
									<td onclick="alert('请在onclik事件中添加链接');" style="cursor: hand">
										<%=tests.getBlocktitle() %>
									</td>
								</tr>
							</table>
						</td>
					</tr>
				</table>
			
			<%
			}
			}
		%>
		</div>
	</div>
</body>
</html>
