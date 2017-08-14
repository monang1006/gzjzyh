<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="/tags/web-tree" prefix="tree"%>
<%@include file="/common/include/rootPath.jsp" %>
<html>
	<head>
		<title>发送邮件-选择联系人</title>
		<%@include file="/common/include/meta.jsp" %>
		<%--<LINK href="<%=frameroot%>/css/properties_windows.css" type=text/css rel=stylesheet>
		--%>
		<LINK href="<%=frameroot%>/css/windows.css" type=text/css rel=stylesheet>
		<LINK href="<%=frameroot%>/css/treeview.css" type=text/css rel=stylesheet>
		<link type="text/css" rel="stylesheet" href="<%=path%>/common/js/tabpane/css/luna/tab.css" />
		<script type="text/javascript" src="<%=path%>/common/js/tabpane/js/tabpane.js"></script>
		<script src="<%=path%>/common/js/jquery/jquery-1.2.6.js" type="text/javascript"></script>
		<script src="<%=path%>/common/js/common/common.js" type="text/javascript"></script>
		<script src="<%=path%>/common/js/common/btn.js" type="text/javascript"></script>
		<SCRIPT src="<%=root%>/common/js/mztree_check/mztreeview_check.js"></SCRIPT>
	<style type="text/css">
		.input_bg {
			font-family: "宋体";
			background-image: url(<%=frameroot%>/images/perspective_leftside/input_bg.jpg);
			border: 1px solid #b8b8b8;
		}
	</style>
	<script type="text/javascript">
		$(document).ready(function(){
			var sel_hidden = $("#sel_hidden");
			var sel_copy = $("#sel_copy");
			var sel_accept = $("#sel_accept");
			
			sel_accept.append("${accepter}");
			sel_copy.append("${csAccepter}");
			sel_hidden.append("${msAaccepter}");
			
			//选择人员到收件人
			$("#btn_accept_right").click(function(){
				addOpt(sel_accept);
			});
			//取消收件人中的人员
			$("#btn_accept_left").click(function(){
				removeOpt(sel_accept);
			});
			//选择抄送人员
			$("#btn_copy_right").click(function(){
				addOpt(sel_copy);
			});
			//取消抄送人员
			$("#btn_copy_left").click(function(){
				removeOpt(sel_copy);
			});
			//选择密送人员
			$("#btn_hidden_right").click(function(){
				addOpt(sel_hidden);
			});
			//取消密送人员
			$("#btn_hidden_left").click(function(){
				removeOpt(sel_hidden);
			});
			
			//确定
			$("#btnOK").click(function(){
				var accept = getSelect(sel_accept);
				var copy   = getSelect(sel_copy);
				var hidden = getSelect(sel_hidden);
				var parentWindow = window.dialogArguments;
				var mailaddress = parentWindow.document.getElementById("mailaddress");
				var csinput = parentWindow.document.getElementById("csinput");
				var msinput = parentWindow.document.getElementById("msinput");
				mailaddress.value=accept;
				csinput.value=copy;
				msinput.value=hidden;
				window.close();
			//	alert(accept);
			//	alert(copy);
			//	alert(hidden);
			});
			//取消
			$("#btnNO").click(function(){
				window.returnValue = "none";
				window.close();
			});
			//选择人员到列表
			function addOpt(sel){
				$(".tr1").each(function(){
					var tr_css = $(this).css("background");
					if('#a9b2ca' == tr_css){
						var td_val,td_text ;
						$(this).find("td").each(function(i){
							if(i == 0){
								td_val = $(this).next().text();
								td_text=$(this).text();
							}
						});
						if(!hasSelect(sel,td_text,td_val)){
							sel.append($("<option value="+td_val+">"+td_text+"</option>"));
						}	
					}
				});
			}
			//取消选中人员
			function removeOpt(sel){
				 sel.find("option").each(function(){
				 	if($(this).attr("selected") == true){
				 		sel.get(0).removeChild(this);
				 	}
				 });
			}
			//获取选择的人员列表
			function getSelect(sel){
				var ret_accept = "";
				sel.find("option").each(function(){
					ret_accept += this.text+"<"+this.value+">"+",";
				});
				if(ret_accept.length>0){
					ret_accept = ret_accept.substring(0,ret_accept.length-1);
				}
				return ret_accept;
			}
			
			//数据加载前给出提示
			$("#content").ajaxStart(function(){
				$(this).html("正在加载数据,请稍候...");
			});
			//数据加载完成后提供行双击事件
			$("#content").ajaxComplete(function(){
				$(this).find("tr").each(function(){
					$(this).dblclick(function(){
						addOpt(sel_accept);
					});
				});
			});
			//加载数据
			$.ajax({
				type:"post",
				url:"<%=root %>/address/addressGroup!choosePersonByGroupId.action",
				success:function(data){
					$("#content").html(data);
				},
				error:function(){
					alert("对不起，数据加载失败！");
				}
			});
			
		});
		//树列表单击事件(flag标示是个人还是系统通讯录)
		function select(groupId,flag){
			if(flag!=undefined && "public" == flag){
				var url = "<%=root %>/address/addressOrg!choosePersonByOrgId.action";
				$.post(
					url,
					{orgId:groupId},
					function(data){
						$("#content").html(data)
					}
				);
			}else{
				var url = "<%=root %>/address/addressGroup!choosePersonByGroupId.action";
				$.post(
					url,
					{groupId:groupId},
					function(data){
						$("#content").html(data)
					}
				);
			}
			
		}
		//判断是否已经选择
		function hasSelect(sel,txt,val){
			var isExist = false;
			sel.find("option").each(function(){
				if(this.text == txt && this.value == val){
					isExist = true;
				}
			});
			return isExist;
		}
		//双击已经选中的人员,删除人员 
		function dblclickRemove(sel){
			if(sel.selectedIndex!=-1){
				var opt = sel.options[sel.selectedIndex];
				sel.removeChild(opt);
			}
		}
		
		
		function trOnMouse(e){
			var tb=this,tr,ee
		    ee=e==null?event.srcElement:e
		    if(ee.tagName!="TD")
		        return false;
		    tr=ee.parentNode;
		    
			var tr_css = $(tr).css("background");
			if('#a9b2ca' == tr_css){
				$(tr).css("background","");		
			}else{
				$(tr).css("background","#a9b2ca");
			}
		}
	</script>
	</head>
	<base target="_self"/>
	<body class="">
		<DIV id=contentborder style="overflow: auto;" cellpadding="0">
			<form action="<%=root %>/address/address!save.action" method="post">
				<table width="100%" height="391" border="0" cellpadding="0" cellspacing="0" bordercolor="#FFFFFF">
					<tr>
						<td height="21" colspan="2" style="FILTER:progid:DXImageTransform.Microsoft.Gradient(gradientType=0,startColorStr=#ededed,endColorStr=#ffffff);"><img src="<%=frameroot%>/images/ico/ico.gif" width="9" height="9">&nbsp;&nbsp;&nbsp;&nbsp;选择联系人</td>

					</tr>
					<tr height="80%" >
						<td width="100%" height="307">
							<table width="100%" height="381" border="1" cellpadding="0" cellspacing="0" bordercolor="#FFFFFF">
								<tr>
									<td width="22%" height="298" style="border-top-color: gray;">
										<DIV id=contentborder style="background-color: white;" cellpadding="0">
											<tree:strongtree title="通讯录"  check="false" dealclass="com.strongit.oa.address.util.AddressDeal" data="${groupList}" target="_self"  />	
										</DIV>
									</td>
									<td width="42%" bgcolor="#FFFFFF" style="border-top-color: #FFFFFF;">
										<DIV style="background-color: white;" id=contentborder cellpadding="0">
											<!-- 
											<table style="vertical-align: top;" onMouseDown="TableMouseDown('#A9B2CA')" width="100%" border="0" cellspacing="0" cellpadding="00">
											 -->
											<table style="vertical-align: top;"  rules="cols" onMouseDown="trOnMouse()" width="100%" border="0" cellspacing="1" cellpadding="00">
											
												<thead>
													<tr style="position:relative;top:expression(this.parentElement.offsetParent.parentElement.scrollTop);z-index:1;">
													<th width="50%" height="22" class='biao_bg2'>姓名</th>
													<th width="50%" height="22" class='biao_bg2'>电子邮件地址</th>
													<th class="biao_bg2" style="text-indent: 0px;"></th>
													</tr>
													<tr id="content">
													</tr>
												</thead>
											</table>
								  		</DIV>	
							  	    </td>
									<td width="9%" style="border-top-color: #FFFFFF;">
										<DIV id=contentborder style="background-color: white;" cellpadding="0">
										 <table width="100%" height="350" border="0" align="center" cellpadding="0" cellspacing="0" bordercolor="#FFFFFF">
										 	<tr><td height="110" align="center">
									        <input id="btn_accept_right" type="button" class="input_bg" value=" -> " title="添加左侧选中的收件人"/><br><br>
									        <input id="btn_accept_left" type="button"   class="input_bg" value=" <- " title="移除右侧选中的收件人"/>
							              </td></tr>
									      
							              <tr><td height="148" align="center">
									        <input id="btn_copy_right" type="button"  class="input_bg" value=" -> " title="添加左侧选中的抄送人"/><br><br>
									        <input id="btn_copy_left" type="button"   class="input_bg" value=" <- " title="移除右侧选中的抄送人"/>
							               </td></tr>
									       
							               <tr><td align="center">
									        <input id="btn_hidden_right" type="button"  class="input_bg" value=" -> " title="添加左侧选中的密送人"/><br><br>
									        <input id="btn_hidden_left" type="button"   class="input_bg" value=" <- " title="移除右侧选中的密送人"/></td></tr>
									     </table>
								  </DIV>	
								  </td>
									<td width="27%" style="border-top-color: #FFFFFF;">
										<DIV id=contentborder style="background-color: white;" cellpadding="0">
										<table width="100%" height="276" border="0" cellpadding="0" cellspacing="0" bordercolor="#FFFFFF">
											<tr>
											  <td >
												收件人：<br>
												  <select id="sel_accept" ondblclick="dblclickRemove(this);" multiple="multiple" size="7" style="width:254px;">
											      </select>
											  </td>
											</tr>
											<tr><td >
												<p>抄送：<br>
												  <select id="sel_copy" ondblclick="dblclickRemove(this);" multiple="multiple" size="7" style="width:254px;">
											      </select>
										      </p></td>
											</tr>
											<tr><td >
												<p>密送：<br>
												  <select id="sel_hidden" ondblclick="dblclickRemove(this);" multiple="multiple" size="7" style="width:254px;">
											      </select>
										      </p></td>
											</tr>
										</table>
										</DIV>	
								  </td>
								</tr>
						  </table>
					  </td>
					</tr>
					
					<tr>
						<td align="center"><br><br>
							<input type="button" id="btnOK"  class="input_bg" icoPath="<%=root%>/images/ico/queding.gif" value="  确定"/>&nbsp;&nbsp;&nbsp;&nbsp;
							<input type="button" id="btnNO"  class="input_bg" icoPath="<%=root%>/images/ico/quxiao.gif" value="  取消"/>
						</td>
					</tr>
			  </table>
			</form>
		</DIV>
	</body>
</html>
