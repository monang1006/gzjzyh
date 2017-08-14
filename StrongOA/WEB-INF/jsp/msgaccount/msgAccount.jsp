<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<!--  引用标签处-->
<%@ taglib uri="/tags/web-flex" prefix="webflex"%>
<%@ taglib uri="/tags/web-newdate" prefix="strong"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<!--  引用公共文件,以下两个文件是建议包含-->
<%@include file="/common/include/rootPath.jsp" %>
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
	<%--<script language='javascript' src='<%=path%>/common/js/common/search.js'></script>
	--%><SCRIPT language="javascript" src="<%=path%>/common/js/menu/menu.js"></SCRIPT>
	<TITLE>短信平台模块列表</TITLE>
	<script type="text/javascript">
		$(document).ready(function(){
			$("#img_sousuo").click(function(){
				$("#simNumber").val(encodeURI($("#simNum").val()));
				$("#operNumber").val(encodeURI($("#operNum").val()));
				$("#simContent").val(encodeURI($("#content").val()));
				$("#beginDate").val($("#starttime").val());
				$("#endDate").val($("#endtime").val());
				$("form").submit();
			});
		});
	</script>
	</HEAD>
	<BODY class=contentbodymargin oncontextmenu="return false;" onload=initMenuT()>
		<script type="text/javascript" src="<%=path%>/common/js/newdate/WdatePicker.js"></script>
		<DIV id=contentborder align=center>
		<s:form id="myTableForm" action="/msgaccount/msgAccount.action" method="get">
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
												&nbsp;<img src="<%=frameroot%>/images/ico/ico.gif" width="9" height="9">&nbsp;
												短信猫话费查询
											</td>
											<td>
												&nbsp;
											</td>
											<td>
											<table border="0" align="right" cellpadding="00" cellspacing="0">
								                <tr>
								                    <td width="*">&nbsp;</td>
													<td>
														<a class="Operation" href="javascript:toSerchAccount();">
															<img src="<%=root%>/images/ico/message.gif" width="15" height="15" class="img_s">
															查询费用&nbsp;</a>
													</td>
													<td width="5"></td>
													<td>
														<a class="Operation" href="javascript:toSeeInfo();">
															<img src="<%=root%>/images/ico/chakan.gif" width="15" height="15" class="img_s">
															查看&nbsp;</a>
													</td>
													<td width="5"></td>
													<td>
														<a class="Operation" href="javascript:toDelete();">
															<img src="<%=root%>/images/ico/shanchu.gif" width="15" height="15" class="img_s">
															删除&nbsp;</a>
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
						--%>
						<table width="100%" border="0" cellspacing="0" cellpadding="00">
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
								                    <strong>短信猫话费查询</strong>
							                    </td>
											
											<td width="70%">
											<table border="0" align="right" cellpadding="00" cellspacing="0">
								                <tr>
								                <td width="4"><img src="<%=frameroot%>/images/bt_l.jpg"/></td>
					                 	        <td class="Operation_list" onclick="toSerchAccount();"><img src="<%=root%>/images/operationbtn/Query_cost.png"/>&nbsp;查&nbsp;询&nbsp;费&nbsp;用&nbsp;</td>
					                 	        <td width="4"><img src="<%=frameroot%>/images/bt_r.jpg"/></td>
				                  		        <td width="5"></td>
								                 	
								                	
								                	<td width="4"><img src="<%=frameroot%>/images/bt_l.jpg"/></td>
					                 	            <td class="Operation_list" onclick="toSeeInfo();"><img src="<%=root%>/images/operationbtn/view.png"/>&nbsp;查&nbsp;看&nbsp;</td>
					                 	            <td width="4"><img src="<%=frameroot%>/images/bt_r.jpg"/></td>
				                  		            <td width="5"></td>
								                	
							                  		
							                  		<td width="4"><img src="<%=frameroot%>/images/bt_l.jpg"/></td>
					                 	            <td class="Operation_list" onclick="toDelete();"><img src="<%=root%>/images/operationbtn/del.png"/>&nbsp;删&nbsp;除&nbsp;</td>
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
						<input type="hidden" name="simNumber" id="simNumber" value="${simNumber }" />
						<input type="hidden" name="operNumber" id="operNumber" value="${operNumber }" />
						<input type="hidden" name="simContent" id="simContent" value="${simContent }" />
						<input type="hidden" name="beginDate" id="beginDate" value="${beginDate }" />
						<input type="hidden" name="endDate" id="endDate" value="${endDate }" />
						<webflex:flexTable name="myTable" width="100%" height="365px" wholeCss="table1" property="id" isCanDrag="true" isCanFixUpCol="true" clickColor="#A9B2CA" footShow="showCheck" getValueType="getValueByProperty"  collection="${page.result}" page="${page}">
							<table width="100%" border="0" cellpadding="0" cellspacing="0" class="table1_search">
                                 <%--<tr>
						          <td width="5%" align="center"  class="biao_bg1"><img id="img_sousuo" src="<%=root%>/images/ico/sousuo.gif" width="15" height="15"></td>
						          <td width="20%" class="biao_bg1"><input name="operNum" id="operNum" type="text"  style="width=100%" class="search" title="请您输入运营商号码"></td>
						          <td width="15%" class="biao_bg1"><input name="simNum" id="simNum" type="text" style="width=100%" class="search" title="请您输入SIM卡号码"></td>
						          <td width="20%" class="biao_bg1"><input name="content" id="content" type="text" style="width=100%" class="search" title="请您输入短信内容"></td>
						          <td width="20%" align="center" class="biao_bg1"><strong:newdate  name="starttime" id="starttime" width="100%" skin="whyGreen" isicon="true"  classtyle="search" title="请输入起始日期"/></td>
						          <td width="20%" align="center" class="biao_bg1"><strong:newdate  name="endtime" id="endtime" width="100%" skin="whyGreen" isicon="true"  classtyle="search" title="请输入截止日期"/></td>
						        </tr>
						       --%>
						       
						        <tr>
							       <td>
							       		<div style="float: left;width:250px;">&nbsp;&nbsp;运营商号码：&nbsp;<input name="operNum" id="operNum" type="text"   class="search" title="请您输入运营商号码" value="${operNumber }" >
							       		</div>
							       		<div style="float: left;">&nbsp;&nbsp;SIM卡号码：&nbsp;<input name="simNum" id="simNum" type="text"   class="search" title="请您输入SIM卡号码"  value="${simNumber }" >
							       		</div>
							       		<div style="float: left;">&nbsp;&nbsp;短信内容：&nbsp;<input name="content" id="content" type="text"   class="search" title="请您输入短信内容"   value="${simContent }" >
							       		</div>
							       		<div style="float: left;width:185px;">&nbsp;&nbsp;起始日期：&nbsp;<strong:newdate  name="starttime" id="starttime" dateform="yyyy-MM-dd" dateobj="${beginDate}" skin="whyGreen" isicon="true"  classtyle="search" title="请输入起始日期"/>
							       		</div>
							       		<div style="float:left;width:300px;">&nbsp;&nbsp;截止日期：&nbsp;<strong:newdate  name="endtime" id="endtime" skin="whyGreen" isicon="true" dateform="yyyy-MM-dd" dateobj="${endDate}"  classtyle="search" title="请输入截止日期"/>
							       		&nbsp;&nbsp;&nbsp;&nbsp;<input id="img_sousuo" type="button" />
							       		</div>
							       	</td>
							     </tr>
						       
						       
						       </table>
							<webflex:flexCheckBoxCol caption="选择" property="replyMessageId" showValue="replyMessageId" width="5%" isCheckAll="true" isCanDrag="false" isCanSort="false"></webflex:flexCheckBoxCol>
							<webflex:flexTextCol caption="运营商号码" property="replyNumber" showValue="replyNumber" width="10%"  isCanDrag="true" isCanSort="true" showsize="20"></webflex:flexTextCol>
							<webflex:flexTextCol caption="SIM卡号码" property="senderNumber" showValue="senderNumber" width="30%" isCanDrag="true" isCanSort="true"  showsize="20"></webflex:flexTextCol>
							<webflex:flexTextCol caption="短信内容" property="replyMessageId" showValue="replyContent" width="35%" isCanDrag="true" isCanSort="true"  showsize="20" onclick="toView(this.value)"></webflex:flexTextCol>
							<webflex:flexTextCol caption="接收日期" property="replyTime" showValue="replyTime" width="20%" isCanDrag="true" isCanSort="true"  showsize="20"></webflex:flexTextCol>
						  </webflex:flexTable>
				</tr>
			</table>
			</s:form>
		</DIV>
		<script type="text/javascript">
			var sMenu = new Menu();
			function initMenuT(){
				sMenu.registerToDoc(sMenu);
				var item = null;
				item = new MenuItem("<%=root%>/images/operationbtn/Query_cost.png","查询费用","toSerchAccount",1,"ChangeWidthTable","checkMoreDis");
				sMenu.addItem(item);
				item = new MenuItem("<%=root%>/images/operationbtn/view.png","查看","toSeeInfo",1,"ChangeWidthTable","checkMoreDis");
				sMenu.addItem(item);
				item = new MenuItem("<%=root%>/images/operationbtn/del.png","删除","toDelete",1,"ChangeWidthTable","checkMoreDis");
				sMenu.addItem(item);
				sMenu.addShowType("ChangeWidthTable");
			    registerMenu(sMenu);
			}
			
			function toSerchAccount(){
				var boo=OpenWindow('<%=root%>/msgaccount/msgAccount!send.action', '400', '260', window);
			}
			
			function toView(id){
				var boo=OpenWindow('<%=root%>/msgaccount/msgAccount!info.action?ids='+id, '400', '180', window);
				if(boo=="true"){
					window.location.reload();
				}
			}
			
			function toSeeInfo(){
				var reId=getValue();
				if(reId==null||reId==""){
					alert("请您选择要进行查看的信息");
				}else{
					if(reId.indexOf(",")!=-1){
						alert("您一次只能读取一条信息");
					}else{
						var boo=OpenWindow('<%=root%>/msgaccount/msgAccount!info.action?ids='+reId, '400', '170', window);
						if(boo=="true"){
							window.location.reload();
						}
					}
				}
			}
			
			function toDelete(){
				var delId=getValue();
				if(delId==null||delId==""){
					alert("请您选择要进行删除的信息");
				}else{
				 if(confirm("确定要删除吗？")){
				  	$.ajax({
				  		type:"post",
				  		dataType:"text",
				  		url:"<%=root%>/msgaccount/msgAccount!delete.action",
				  		data:"ids="+delId,
				  		success:function(msg){
				  			if(msg=="true"){
				  				window.location.reload();
							}else{
								alert("删除失败,请您重新进行删除");
								window.location.reload();
				  			}
				  		}
				  	});
				  }
				 }
			}
		</script>
	</BODY>
</HTML>
