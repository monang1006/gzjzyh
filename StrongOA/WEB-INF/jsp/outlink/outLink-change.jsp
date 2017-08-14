<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib prefix="s" uri="/struts-tags"%>
<%@include file="/common/include/rootPath.jsp"%>
<%@ taglib uri="/tags/security" prefix="security"%>
<html>
	<head>
		<%@include file="/common/include/meta.jsp"%>
		<LINK href="<%=frameroot%>/css/windows.css" type=text/css rel=stylesheet>
		<!--  引用公共样式文件,建议所有样式都以文件方式定义在jsp文件外部,通常定义在WebRoot目录下的CSS文件夹下-->
	    <LINK href="<%=frameroot%>/css/properties_windows_add.css" type=text/css rel=stylesheet>
		<script language="javascript" src="<%=path%>/common/js/jquery/jquery-1.2.6.js"></script>
		<title>保存外部链接</title>
		<script type="text/javascript">
			function save(){
				if($.trim($("#addrDes").val())==''){
					alert("链接描述为必填字段，不为空。");
				}else if($.trim($("#address").val())==''){
					alert("链接地址为必填字段，不为空。");
				}else if(!chargeUrl($("#address").val())){
					
				}else{
					$.ajax({
						type:"post",
						dataType:"text",
						url:"<%=root%>/outlink/outLink!save.action",
						data:"id="+$("#id").val()+"&des="+$("#addrDes").val()+"&address="+$("#address").val()+"&type="+$('input[@name=type][@checked]').val(),
						success:function(msg){
							if(msg=="true"){
								window.returnValue="true";
								window.close();
							}else{
								alert("操作失败，请您重新添加。");
							}
						}
					});
				}
			}
			function preView(){
				if($("#address").val()==''){
					alert("链接地址为空，不能够进行预览。");
					return;
				}else{
					if(chargeUrl($("#address").val())){
						var iWidth=650; //弹出窗口的宽度;
						var iHeight=500; //弹出窗口的高度;
						var iTop = (window.screen.availHeight-30-500)/2; //获得窗口的垂直位置;
						var iLeft = (window.screen.availWidth-10-650)/2; //获得窗口的水平位置;
						window.open ($("#address").val(), '预览链接地址', 'height='+iHeight+',,innerHeight='+iHeight+',width='+iWidth+',innerWidth='+iWidth+',top='+iTop+',left='+iLeft+',toolbar=yes, menubar=yes, scrollbars=yes, resizable=yes,location=no, status=yes');
					}
				}
				
			}
			
			function chargeUrl(url){
				var reg = "^http[s]?:\\/\\/([\\w-]+\\.)+[\\w-]+([\\w-./?:%&=]*)?$";
				var regExp = new RegExp(reg);
				if(regExp.test(url)){
					return true;
				}else{
					alert('您输入的链接地址有误，请重新输入!链接地址格式如:”http://www.baidu.com/“。');
					return false;
				}
			}
			
			function cancle(){
				window.close();
			}
		</script>
	</head>
	<body class=contentbodymargin>
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
								<strong>编辑外部链接</strong>
							</td>
							<td align="right">
								<table border="0" align="right" cellpadding="00" cellspacing="0">
					                <tr>
					                 	
					                 	<td width="7"><img src="<%=frameroot%>/images/ch_h_l.gif"/></td>
					                 	<td class="Operation_input" onclick="save();">&nbsp;保&nbsp;存&nbsp;</td>
					                 	<td width="7"><img src="<%=frameroot%>/images/ch_h_r.gif"/></td>
				                  		<td width="5"></td>
				                  		<td width="7"><img src="<%=frameroot%>/images/ch_z_l.gif"/></td>
					                 	<td class="Operation_input1" onclick="preView();">&nbsp;预&nbsp;览&nbsp;</td>
					                 	<td width="7"><img src="<%=frameroot%>/images/ch_z_r.gif"/></td>
				                  		<td width="5"></td>
					                 	<td width="8"><img src="<%=frameroot%>/images/ch_z_l.gif"/></td>
					                 	<td class="Operation_input1" onclick="cancle();">&nbsp;取&nbsp;消&nbsp;</td>
					                 	<td width="7"><img src="<%=frameroot%>/images/ch_z_r.gif"/></td>
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
					
						<input type="hidden" id="id" name="id" value="${model.outlinkId }">
						<table width="100%" border="0" cellpadding="0" cellspacing="0"
							class="table1">
							<tr>
								<td  height="21" class="biao_bg1" align="right">
									<span class="wz"><font color=red>*</font>&nbsp;链接描述：</span>
								</td>
								<td class="td1">
									&nbsp;
									<input type="text" id="addrDes" name="addrDes"  maxlength=50 size="80" value="${model.outlinkDes }">
								</td>
							</tr>

							<tr>
								<td  height="21" class="biao_bg1" align="right">
									<span class="wz"><font color=red>*</font>&nbsp;链接地址：</span>
								</td>
								<td class="td1">
									&nbsp;
									<input type="text" id="address" name="address"  maxlength=200 size="80" value="${model.outlinkAddress }">
								</td>
							</tr>
							<tr>
								<td  height="21" class="biao_bg1" align="right">
									<span class="wz"><font color=red>*</font>&nbsp;链接类型：</span>
								</td>
								<td class="td1">
									&nbsp;
									<s:radio id="type" name="type" list="#{'0':'桌面链接' , '1':'首页链接','2':'处理类链接' }" listKey="key" listValue="value" value="model.outlinkType" />
								</td>
							</tr>
							<td class="table1_td"></td>
					<td></td>
				</tr>
						</table>
				
					</td>
				</tr>
			</table>
		</DIV>
	</body>
</html>
