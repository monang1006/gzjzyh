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
		<script language='javascript' src='<%=path%>/common/js/grid/ChangeWidthTable.js'></script>
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
		    //全选添加
			$("#btn_all_select").click(function(){
				allSelect(sel_accept);
			});
			//全选删除
			$("#btn_all_delete").click(function(){
				deleteAllSelect(sel_accept);
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
				
				window.returnValue=accept;
				
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
				var url = "<%=root %>/address/addressOrg!orguserlist2.action?orgId="+groupId;
				parent.document.getElementById('project_work_content').src=url;
				//parent.project_work_content.document.location="<%=root %>/address/addressOrg!orguserlist.action?orgId="+groupId";
			}else{
				var url = "<%=root %>/address/address!userlist.action?groupId="+groupId;
				parent.document.getElementById('project_work_content').src=url;
				//parent.project_work_content.document.location="<%=root %>/address/address.action?groupId="+groupId;
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
		//全选添加
		function allSelect(sel_accept){
		    document.frames.project_work_content.allSelect(sel_accept);
		}
		
		//全选deleteAllOpt
		function deleteAllSelect(sel_accept){
		    document.frames.project_work_content.deleteAllSelect(sel_accept);
		}
	</script>
	</head>
	<base target="_self"/>
	<body class="">
		<DIV id=contentborder style="overflow: auto;" cellpadding="0">
			<form action="<%=root %>/address/address!save.action" method="post">
				<table width="100%" height="391" border="0" cellpadding="0" cellspacing="0" bordercolor="#FFFFFF">
					<tr height="80%" >
						<td width="100%" height="307">
							<table width="100%" height="381" border="1" cellpadding="0" cellspacing="0" bordercolor="#FFFFFF">
								<tr>
									<td width="20%" height="298" style="border-top-color: gray;">
										<DIV id=contentborder style="background-color: white;" cellpadding="0">
											<tree:strongtree title="通讯录"  check="false" dealclass="com.strongit.oa.address.util.AddressDeal" data="${groupList}" target="project_work_content"  />	
										</DIV>
									</td>
									<td width="42%" bgcolor="#FFFFFF" style="border-top-color: #FFFFFF;">
										<DIV style="background-color: white;" id=contentborder cellpadding="0">
											<iframe frameborder=0 src="<%=root%>/address/addressOrg!orguserlist2.action" width=100% name="project_work_content" id="project_work_content" height=100% scrolling="auto"></iframe>
								  		</DIV>	
							  	    </td>
									<td width="9%" style="border-top-color: #FFFFFF;">
										<DIV id=contentborder style="background-color: white;" cellpadding="0">
										 <table width="100%" height="350" border="0" align="center" cellpadding="0" cellspacing="0" bordercolor="#FFFFFF">
										 	<tr><td height="110" align="center">
									        <input id="btn_accept_right" type="button" class="input_bg" value=" -> " title="添加左侧选中的收件人"/><br><br>
									        <input id="btn_accept_left" type="button"   class="input_bg" value=" <- " title="移除右侧选中的收件人"/><br><br>
									        <input id="btn_all_select" type="button"   class="input_bg" value="->>" title="全选添加左侧的收件人"/><br><br>
									        <input id="btn_all_delete" type="button"   class="input_bg" value="<<-" title="全选删除右侧的收件人"/>
							              </td></tr>
									      
									     </table>
								  </DIV>	
								  </td>
									<td width="20%" style="border-top-color: #FFFFFF;">
										<DIV id=contentborder style="background-color: white;" cellpadding="0">
										<table width="100%" height="254" border="0" cellpadding="0" cellspacing="0" bordercolor="#FFFFFF">
											<tr>
											  <td  >
												收件人：<br>
												  <select id="sel_accept" ondblclick="dblclickRemove(this);" multiple="multiple" size="7" style="width:165;height:360px">
											      </select>
											  </td>
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
