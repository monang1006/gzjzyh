<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<%@include file="/common/include/rootPath.jsp"%>
<% 
	response.setHeader("Cache-Control","no-store");
	response.setHeader("Pragrma","no-cache");
	response.setDateHeader("Expires",0);
%>
<html>
	<head>
		<title>添加短信模块</title>
		<LINK href="<%=path%>/common/js/tabpane/css/luna/tab.css" type=text/css rel=stylesheet>
		<LINK href="<%=frameroot%>/css/properties_windows_add.css" type=text/css rel=stylesheet>
		<script language="javascript" src="<%=path%>/common/js/tabpane/js/tabpane.js"></script>
		<script language="javascript" src="<%=path%>/common/js/jquery/jquery-1.2.6.js"></script>
		<style type="text/css">
			body {
				margin-left: 0px;
				margin-top: 0px;
				margin-right: 0px;
				margin-bottom: 0px;
				height: 100%
			}
		</style>
		<script type="text/javascript">
			$(document).ready(function(){
				$("input[name='replyOrNot']").click(function(){
				     var item = $("input[name='replyOrNot']:checked").val();
					 if(item==0){
					 	$("#selectNum").hide();
					 	$("#dinput").hide();
					 }else{
					 	$("#selectNum").show();
					 	$("#dinput").show(); 
					 }
				});
				$("input[name='model.thankOrNot']").click(function(){
					var item=$("input[name='model.thankOrNot']:checked").val();
					if(item==0){
						$("#thankContent").hide();
					}else{
						$("#thankContent").show();
					}
				});
				$("#answerNum").change(function(){
					$("#dinput").html("");
					for(i=0;i<$("#answerNum").val();i++){
						$("#dinput").append("<tr><td width=\"30%\" class=\"biao_bg1\"  align=right><span class=\"wz\">状态位"+i+"含义：</span></td><td width=\"70%\" class=\"td1\"><input type=\"text\" name=\"means\" style=\"width:100%\" ><input type=\"hidden\" name=\"state\" value=\""+i+"\"></td></tr>");
					}
				});
				$("#canBtn").click(function(){
					window.close();
				});
				
				
				$("#subBtn").click(function(){
					var item = $("input[name='replyOrNot']:checked").val();
					var atem = $("input[name='model.thankOrNot']:checked").val();
					if($.trim($("#modelName").val())==""){
						$("#modelName").focus();
					}else if($.trim($("#letterNum").val())==""){
						alert("短信最大字数不能为空");
						$("#letterNum").focus();
					}else if(!isNotInt($("#letterNum").val())){
						alert("短信最大字数只能为整数");
						$("#letterNum").focus();
					}else if(item=="1"&&$("#answerNum").val()=="0"){
						alert("设置短信需要回复，但是您的短信答案个数为0,请您设置短信答案");
					}else if(atem=="1"&&$.trim($("#content").val())==""){
						alert("您选择需要填写感谢内容，请您填写感谢内容");
					}else if($("#content").val().length>70){
						alert("对不起，字数最大只能为70");
					}else{
						$("#myform").submit();
					}
				});
			});
			
			function isNotInt(num){
				var re=/^[0-9]*[1-9][0-9]*$/;
				return re.test(num);
			}
		</script>
	</head>
	<body class="contentbodymargin">
		<div id="contentborder">
			<form id="myform" action="<%=root %>/smsplatform/SmsPlatform!save.action" method="post" target="myIframe">
				<input type="hidden" id="modelId" value="">
		   <%--<table border="0" width="98%" bordercolor="#FFFFFF" cellspacing="0"
					cellpadding="0">
					<tr>
						<td >
							&nbsp;
								</td>
						<td>
						<img src="<%=frameroot%>/images/ico/ico.gif" width="9" height="9">&nbsp;
						添加短信模块
						</td>
					</tr>
					--%>
					
			<table width="100%" border="0" cellspacing="0" cellpadding="00">
				<tr>
				<td colspan="3" class="table_headtd">
					<table width="100%" border="0" cellspacing="0" cellpadding="00">
						<tr>
							<td class="table_headtd_img" >
								<img src="<%=frameroot%>/images/ico/ico.gif" >&nbsp;
							</td>
							<td align="left">
							<strong>添加短信模块</strong>
							</td>
					<td align="right">
								<table border="0" align="right" cellpadding="00" cellspacing="0">
					                <tr>
					                 	<td width="7"><img src="<%=frameroot%>/images/ch_h_l.gif"/></td>
					                 	<td class="Operation_input" onclick="$('#subBtn').click()">&nbsp;确&nbsp;定&nbsp;</td>
					                 	<td width="7"><img src="<%=frameroot%>/images/ch_h_r.gif"/></td>
				                  		<td width="5"></td>
					                 	
					                 	<td width="8"><img src="<%=frameroot%>/images/ch_z_l.gif"/></td>
					                 	<td class="Operation_input1" onclick="$('#canBtn').click()">&nbsp;取&nbsp;消&nbsp;</td>
					                 	<td width="7"><img src="<%=frameroot%>/images/ch_z_r.gif"/></td>
				                  		<td width="6"></td>
					                </tr>
					            </table>
							</td>
							</tr>
							</table>
							</td>
							</tr>
					
					
					
					
					
					
					<%--<tr height="80%">
						<td width="100%">
							<DIV class=tab-pane id=tabPane1>
								<SCRIPT type="text/javascript">
								tp1 = new WebFXTabPane( document.getElementById( "tabPane1") ,false );
								</SCRIPT>
								<DIV class=tab-page id=tabPage1>
									<H2 class=tab>
										编码规则
									</H2>
									--%>
					
			<table  border="0" cellpadding="0" cellspacing="0" class="table1" width="100%" >
				<fieldset style="width: 85%">
					<legend>
						<span class="wz">编码规则：</span>
					</legend>
					<table  border="0" cellpadding="0" cellspacing="1"
						class="table1">
										<tr>
											<td width="30%" class="biao_bg1" align=right>
												<span id="modelNamePan" class="wz">模块名称(<font color=red>*</font>)：</span>
											</td>
											<td width="70%" class="td1">
												<input type="text" title="请您输入模块名称(此信息为必填项)" name="model.bussinessModuleName" style="width:100% " id="modelName" value="" />
											</td>
										</tr>
										<tr>
											<td width="30%" class="biao_bg1" align=right>
												<span id="letterNumPan" class="wz">短信最大字数(<font color=red>*</font>)：</span>
											</td>
											<td width="70%" class="td1">
												<input type="text" name="model.wordNumber" style="width:100%" id="letterNum" title="请您输入该短信模块支持的短信最大字数(此信息为必填项)" value="70"/>
											</td>
										</tr>
										<tr>
											<td width="30%" class="biao_bg1"  align=right>
												<span id="openOrClosePan" class="wz">是否开启短信：</span>
											</td>
											<td width="70%" class="td1">
												<input type="radio" name="model.isEnable" id="openOrNot" value="1" checked />开启<input type="radio" name="model.isEnable" id="openOrNot" value="0" />关闭
											</td>
										</tr>
										<tr>
											<td width="100%" class="td1" colspan="2">
												<span class="wz" style="color:#999999">（说明：短信编码规则：用户接收短信编码=支持模块编码+自增位编码+状态位编码；用户根据接收到的短信编码进行回复，请您认真设计其长度）</span>
											</td>
										</tr>
										<tr>
											<td width="30%" class="biao_bg1"  align=right>
												<span class="wz">自增位长度：</span>
											</td>
											<td width="70%" class="td1">
												<select name="model.increaseLength" id="zzwcd" style="width:15%">
													<option value=3 selected>3</option>
													<option value=4>4</option>
													<option value=5>5</option>
													<option value=6>6</option>
													<option value=7>7</option>
													<option value=8>8</option>
												</select>
											</td>
										</tr>
									</table>	
									
							<table  border="0" cellpadding="0" cellspacing="1"
						class="table1">	
							<div align="left">
						       <span class="wz">配置系统邮箱：</span>
					          </div>
									
										<tr>
											<td width="100%" class="td1" colspan="2"><span class="wz" style="color:#999999">（说明：请您设置该模块所发送短信是否需要生成回复答案，以及对应答案的内容）</span></td>
										</tr>
										<tr>
											<td width="30%" class="biao_bg1"  align=right>
												<span class="wz">是否感谢回复：</span>
											</td>
											<td width="70%" class="td1">
												<input type="radio" name="model.thankOrNot" id="noThank" value="0" checked />不需要<input type="radio" name="model.thankOrNot" id="thank" value="1" />需要
											</td>
										</tr>
										<tr id="thankContent" style="display:none">
											<td width="30%" class="biao_bg1"  align=right>
												<span class="wz">感谢回复内容：</span>
											</td>
											<td width="70%" class="td1">
												<input type="text" name="model.thankContent" id="content"  style="width:100% "> 
											</td>
										</tr>
										<tr>
											<td width="30%" class="biao_bg1"  align=right>
												<span class="wz">是否需要回复：</span>
											</td>
											<td width="70%" class="td1">
												<input type="radio" name="replyOrNot" id="noReply" value="0" checked />不需要回复<input type="radio" name="replyOrNot" id="reply" value="1" />需要回复
											</td>
										</tr>
										<tr id="selectNum" style="display:none">
											<td width="30%" class="biao_bg1"  align=right>
												<span class="wz">短信答案个数：</span>
											</td>
											<td width="70%" class="td1">
												<select name="answerNum" id="answerNum" style="width:15%">
													<option value=0 selected>0</option>
													<option value=1>1</option>
													<option value=2>2</option>
													<option value=3>3</option>
													<option value=4>4</option>
													<option value=5>5</option>
													<option value=6>6</option>
													<option value=7>7</option>
													<option value=8>8</option>
													<option value=9>9</option>
													<option value=10>10</option>
												</select>
											</td>
										</tr>
										<tbody id="dinput">
										</tbody>
								   
								    
								    </table>
								    </fieldset>
								    </table>
								    
						</table>
					<tr align="center">
						<td>
							<input type="hidden" id="subBtn"  class="input_bg" value="确定" />
							<input type="hidden" id="canBtn"  class="input_bg" value="取消" />
						</td>
					</tr>
				</table>
				</form>
				</div>
				</body>
				
			
			
			<iframe name="myIframe" style="display:none"></iframe>
		
</html>
