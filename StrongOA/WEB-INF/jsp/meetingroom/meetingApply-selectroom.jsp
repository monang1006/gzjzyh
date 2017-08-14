<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="/tags/web-flex" prefix="webflex"%>
<%@ taglib uri="/tags/web-newdate" prefix="strong"%>
<%@include file="/common/include/rootPath.jsp"%>
<% 
	response.setHeader("Cache-Control","no-store");
	response.setHeader("Pragrma","no-cache");
	response.setDateHeader("Expires",0);
%>
<html>
	<HEAD>
		<title>选择会议室</title>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<LINK href="<%=frameroot%>/css/properties_windows.css" type=text/css  rel=stylesheet>
		<link href="<%=frameroot%>/css/strongitmenu.css" type="text/css" rel="stylesheet">
		<!--右键菜单样式 -->
		<LINK href="<%=frameroot%>/css/search.css" type=text/css rel=stylesheet>
		<script type="text/javascript" src="<%=path%>/common/js/newdate/WdatePicker.js"></script>
		<script language='javascript' src='<%=path%>/common/js/grid/ChangeWidthTable.js'></script>
		<script language='javascript' src='<%=path%>/common/js/jquery/jquery-1.2.6.js'></script>
		<script language="javascript" src="<%=path%>/common/js/common/search.js"></script>
		<script language="javascript" src="<%=path%>/common/js/common/common.js"></script>
		<!--右键菜单脚本 -->
		<script language="javascript" src="<%=path%>/common/js/menu/menu.js"></script>
		<!--右键菜单脚本 -->
		<script language='javascript' src='<%=path%>/common/js/upload/jquery.MultiFile.js'></script>
		<script language='javascript' src='<%=path%>/common/js/upload/jquery.blockUI.js'></script>
		<base target="_self">
		<style>
		div.detailCard {
			position: absolute;
			z-index: 999;
			width: 270px;
			height: 143px;
			FILTER:progid:DXImageTransform.Microsoft.Gradient(gradientType=1,startColorStr=#99cccc,endColorStr=#ffffff);
		}
	
		div.detailCon {
			margin: 5px;
		}
		</style>
	</HEAD>

	<BODY class=contentbodymargin oncontextmenu="return false;" onclick="hideDetail();" onload="initMenuT()">
		<DIV id=contentborder align=center>
			<table width="100%" border="0" cellspacing="0" cellpadding="00">
				<tr>
					<td height="100%" >
						<table width="100%" border="0" cellspacing="0" cellpadding="00">
							<tr>
								<td height="40"
									style="FILTER:progid:DXImageTransform.Microsoft.Gradient(gradientType=0,startColorStr=#ededed,endColorStr=#ffffff);">
									<table width="100%" border="0" cellspacing="0" cellpadding="00">
										<tr>
											<td >
											&nbsp;
											</td>
											<td width="40%">
												<img
													src="<%=frameroot%>/images/perspective_leftside/ico.gif"
													width="9" height="9">&nbsp;
												选择会议室
											</td>
											<td width="60%">
												&nbsp;
											</td>
											<table border="0" align="right" cellpadding="00" cellspacing="0">
											<td >
												<a class="Operation" href="#" onclick="subRoom();">
													<img src="<%=root%>/images/ico/queding.gif" width="15"
														height="15" class="img_s"><span id="test"
													style="cursor:hand">确&nbsp;定&nbsp;</span> </a>
											</td>
											<td width="5"> &nbsp; 
											</td>
											<td >
												<a class="Operation" href="#" onclick="window.close();">
													<img src="<%=root%>/images/ico/quxiao.gif" width="15"
														height="15" class="img_s"><span id="test"
													style="cursor:hand">取&nbsp;消&nbsp;</span> </a>
											</td>
											</table>
											<td width="5"></td>
										</tr>
									</table>
								</td>
							</tr>
						</table>
						<%--
							<webflex:flexTable name="titleTable" width="100%" height="364px"
							wholeCss="table1" property="mrId" isCanDrag="true" onclick="alert(this.innerHTML);"
							isCanFixUpCol="true" clickColor="#A9B2CA" footShow="showCheck"
							getValueType="getValueByProperty" collection="${roomList}">
						--%>
					</td>
				</tr>
				<tr>
					<td>
						<webflex:flexTable  name="myTable" width="100%" height="100%" wholeCss="table1" property="0" isCanDrag="true" isCanFixUpCol="true" clickColor="#A9B2CA" getValueType="getValueByArray" collection="${roomList}">
							<div id="detail" class="detailCard" style="display:none ;" onclick="hideDetail()" title="点击关闭提示窗口">
							</div>
						<%--
							<table width="100%" border="0" cellpadding="0" cellspacing="1" 
								class="table1">
								<tr>
									<td width="5%" align="center" class="biao_bg1">
										<img
											src="<%=root%>/images/ico/perspective_leftside/sousuo.gif"
											width="17" height="16">
									</td>
									<td width="15%" class="biao_bg1">
										<input id="id" type="text" class="search" title="会议室编号">
									</td>
									<td width="35%" class="biao_bg1">
										<input id="name" type="text" title="请输入会议室名称" class="search">
									</td>
									<td width="25%" class="biao_bg1">
										<input id="type" type="text" title="请输入会议室类型" class="search">
									</td>
									<td width="15%" class="biao_bg1">
										<select id="status" class="search">
											<option value="0">
												--请选择--
											</option>
											<option value="1">
												空
											</option>
											<option value="2">
												预订
											</option>
											<option value="3">
												使用
											</option>
										</select>
									</td>
									<td width="5%" class="biao_bg1">
										&nbsp;
									</td>
								</tr>
							</table>
							<webflex:flexRadioCol caption="选择" property="mrId"
								showValue="name" width="5%" isCanDrag="false"
								isCanSort="false"></webflex:flexRadioCol>
							<webflex:flexTextCol caption="会议室名称" property="name"
								showValue="name" isCanDrag="true" width="35%" isCanSort="true"></webflex:flexTextCol>
							<webflex:flexTextCol caption="会议室类型" property="mrType"
								showValue="mrType" isCanDrag="true" width="25%" isCanSort="true"></webflex:flexTextCol>
							<webflex:flexTextCol caption="会议室状态" property="mrState"
								showValue="mrState" isCanDrag="true" width="15%" isCanSort="true"></webflex:flexTextCol>
						--%>
						<table width="100%" border="0" cellpadding="0" cellspacing="1" class="table1">
								<tr>
									<td width="5%" align="left" class="biao_bg1">
									<font class="wz" >*操作提示：请尽量选择状态为空闲的会议室。如与别的申请形成冲突，则有可能导致您的申请被驳回！</font>
									</td>
								</tr>
							</table>
							
							<webflex:flexRadioCol caption="选择" valuepos="0" valueshowpos="1" width="5%" isCanDrag="false" isCanSort="false"></webflex:flexRadioCol>
							<webflex:flexTextCol caption="会议室名称" valuepos="0" valueshowpos="1"  onclick="viewDetail(this)" isCanDrag="true" width="50%" isCanSort="true"></webflex:flexTextCol>
							<webflex:flexTextCol caption="会议室类型" valuepos="2" valueshowpos="2" isCanDrag="true" width="30%" isCanSort="true"></webflex:flexTextCol>
							<webflex:flexTextCol caption="会议室状态" valuepos="4" valueshowpos="4" isCanDrag="true" width="15%" isCanSort="true"></webflex:flexTextCol>
						</webflex:flexTable>
					</td>
				</tr>
				<tr>
					<td class="td1" colspan="4" align="center">
						<input name="Submit" type="button" class="input_bg" value="确&nbsp;定" onclick="subRoom();">
						<input name="Submit2" type="button" class="input_bg" value="取 消" onclick="window.close();">
					</td>
				</tr>
			</table>
		</DIV>
		
		<script language="javascript">
var sMenu = new Menu();
function initMenuT(){
	sMenu.registerToDoc(sMenu);
	var item = null;
	item = new MenuItem("<%=root%>/images/ico/shengqing.gif","申请使用","addApplication",1,"ChangeWidthTable","checkMoreDis");
	sMenu.addItem(item);
    registerMenu(sMenu);
}

		function viewDetail(obj){
			/*alert(obj.value);
			var trobj = $("tr[value='"+obj.value+"']");
			alert(trobj.innerHTML);
			alert("td2:"+$('td:eq(2)',trobj).html());
			alert("td3:"+$('td:eq(3)',trobj).html());
			alert("td4:"+$('td:eq(4)',trobj).html());
			alert("td5:"+$('td:eq(5)',trobj).html());
			*/
			$.ajax({
		  		type:"post",
		  		dataType:"text",
		  		url:"<%=root%>/meetingroom/meetingroom!getRoomInfo.action",
		  		data:"mrId="+obj.value,
		  		success:function(msg){
		  			var room = msg.split(',');
		  			
		  			if(room.length==6){
						var x=obj.offsetLeft+250;   
						var y=obj.offsetTop+(143/2)+10;  
						var srtHtml = "<div class=\"detailCon\">"
								+"<div>名称：<a href='#' id='"+obj.value+"' style= 'text-decoration:underline;color:blue' title='选中该会议室' onclick='selectThis(this.id);'>"+room[0]+"</a></div>"
								+"<div>地点：" +room[1]+"</div>"
								+"<div>可容纳人数：" +room[2]+"&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;类型:"+room[4]+"</div>"
								+"<div>说明：" +room[3];+"</div>"
						if("yes"==room[5]){
								srtHtml = srtHtml+"<div>图片：<img src=\"<%=path%>/meetingroom/meetingroom!viewImg.action?mrId="+obj.value+"\" width=\"350\" height=\"250\"></div>";
						}else{
							srtHtml = srtHtml+"<div>图片：<br><img src=\"<%=path%>/oa/image/meetingroom/nophoto.jpg\" width=\"350\" height=\"250\"></div>";
						}
						
		  				$('#detail').html(srtHtml+"</div>");
						$('#detail').css("left",parseInt(x)); 
						$('#detail').css("top",parseInt(y)); 
						$('#detail').css("display","");
					}
		  		}
		  	});
		}
		
		//隐藏DIV提示
		function hideDetail(){
			$('#detail').css("display","none"); 
			$('.active').attr('class','text');
		}
		
		//在DIV提示框中选中会议室
		function selectThis(id){
			var chkR = $("input[name='chkRadio']");
			for(var i=0;i<chkR.length;i++){
				if(id==chkR[i].value){
					chkR[i].checked = "checked";
				}
			}
		}
		
		function subRoom(){
			var subId = "";
			var chkRadio = document.getElementsByName("chkRadio");
			for(var i=0;i<chkRadio.length;i++){
				if(chkRadio[i].checked){
					subId = chkRadio[i].value+","+chkRadio[i].showValue;
				}
			}
			if(""!=subId&&null!=subId){
				returnValue = subId;
				window.close();
			}else{
				alert("请选择申请的会议室！");
				return ;
			}
		}
		
		</script>
	</BODY>
</HTML>
