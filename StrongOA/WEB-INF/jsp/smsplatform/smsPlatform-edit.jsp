<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<%@ taglib uri="/tags/web-newdate" prefix="strong"%>
<%@include file="/common/include/rootPath.jsp"%>
<% 
	response.setHeader("Cache-Control","no-store");
	response.setHeader("Pragrma","no-cache");
	response.setDateHeader("Expires",0);
%>
<html>
	<head>
		<title>保存短信模块</title>
		<LINK href="<%=path%>/common/js/tabpane/css/luna/tab.css" type=text/css rel=stylesheet>
		<LINK href="<%=frameroot%>/css/properties_windows_add.css" type=text/css rel=stylesheet>
		<script language="javascript" src="<%=path%>/common/js/tabpane/js/tabpane.js"></script>
		<script language="javascript" src="<%=path%>/common/js/jquery/jquery-1.2.6.js"></script>
		<script language="javascript" src="<%=path%>/common/js/validate/jquery.validate.js"></script>
		<script language="javascript" src="<%=path%>/common/js/validate/formvalidate.js"></script>
		<style type="text/css">
			body {
				margin-left: 0px;
				margin-top: 0px;
				margin-right: 0px;
				margin-bottom: 0px;
				height: 100%
			}
			
			.displaynone{
				display: none;
			}
			select{
			border:1px solid #b3bcc3;background-color:#ffffff;
			}
		</style>
		<script type="text/javascript">
			$(document).ready(function(){
				//设置发送时间
				$("#noTiming").attr("checked","checked");
				$("#setSendTime").hide();
				$("#noTiming").click(function(){
					$("#setSendTime").hide();
					$("#timingtype").val("");
					myform.timingtype.value="";
					
				});
				$("#timing").click(function(){
					$("#setSendTime").show();
				});
				
				if(""=="${model.timingType }"){
					$("#noTiming").click();
				}else if("0"=="${model.timingType }"){
					$("#timing").click();
					changeTime("0");
					var timingbyDay = $("#timingDelay").val();
					timingbyDay = timingbyDay.substring(11,19);
					$("#DelayByday").val(timingbyDay);
				}else if("1"=="${model.timingType }"){
					$("#timing").click();
					changeTime("1");
					var timingbyMonth = $("#timingDelay").val();
					timingbyMonth = timingbyMonth.substring(8,19);
					$("#DelayByMonth").val(timingbyMonth);
				}
				
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
						$("#dinput").append("<tr><td width=\"20%\" class=\"biao_bg1\"  align=right><span class=\"wz\">状态位"+i+"含义：</span></td><td width=\"70%\" class=\"td1\"><input type=\"text\" name=\"means\" style=\"width:100%\" ><input type=\"hidden\" name=\"state\" value=\""+i+"\"></td></tr>");
					}
				});
				$("#canBtn").click(function(){
					window.returnValue="true";
					window.close();
				});
				$("#letterNum").blur(function(){
					
				});
				$("#subBtn").click(function(){
					var item = $("input[name='replyOrNot']:checked").val();
					var atem = $("input[name='model.thankOrNot']:checked").val();
					var timingOrNot = $("input[name='timingOrNot']:checked").val();
					if($.trim($("#modelName").val())==""){
						alert("模块名不为空。");
						$("#modelName").focus();
					}else if($.trim($("#letterNum").val())==""){
						alert("短信最大字数不能为空。");
						$("#letterNum").focus();
					}else if(!isNotInt($("#letterNum").val())){
						alert("短信最大字数只能为整数。");
						$("#letterNum").focus();
					}else if(timingOrNot=="1"&&""==myform.timingtype.value){
						alert("请选择定时发送时间类型。");
					}else if("0"==myform.timingtype.value&&$("#DelayByday").val()==""){
						alert("请设置定时发送时间。");
					}else if("1"==myform.timingtype.value&&$("#DelayByMonth").val()==""){
						alert("请设置定时发送时间。");
					}else if(item=="1"&&$("#answerNum").val()=="0"){
						alert("设置短信需要回复，但是您的短信答案个数为0,请您设置短信答案。");
					}else if(atem=="1"&&$.trim($("#content").val())==""){
						alert("您选择需要填写感谢内容，请您填写感谢内容。");
					}else if($("#content").val().length>70){
						alert("对不起，字数最大只能为70。");
					}else{
						setDelayTiming();
						$("#myform").submit();
					}
				});
			});
			function isNotInt(num){
				var re=/^[0-9]*[1-9][0-9]*$/;
				return re.test(num);
			}
			
			function changeTime(val){
				if("0"==val){//按每天  dateform="HH:mm:ss"
					$("#DelayByday").show();
					$("#DelayByMonth").hide();
				}else if("1"==val){//按每月 dateform="dd HH:mm:ss"
					$("#DelayByday").hide();
					$("#DelayByMonth").show();
				}
			}
			
			//设置定时时间
			function setDelayTiming(){
				var timingType = myform.timingtype.value;
				var today = new Date();
				if(""==timingType){
					$("#timingDelay").val("");
				}else if("0"==timingType){
					var DelayByday = $("#DelayByday").val();
					DelayByday = today.getYear()+"-"+(today.getMonth()+1)+"-"+today.getDate()+" "+DelayByday;
					$("#timingDelay").val(DelayByday);
				}else if("1"==timingType){
					var DelayByMonth = $("#DelayByMonth").val();
					DelayByMonth = today.getYear()+"-"+(today.getMonth()+1)+"-"+DelayByMonth;
					$("#timingDelay").val(DelayByMonth);
				}
			}
		</script>
	</head>
	<body class="contentbodymargin">
	<script type="text/javascript" src="<%=path%>/common/js/newdate/WdatePicker.js"></script>
		<div id="contentborder">
			<form id="myform" action="<%=root %>/smsplatform/smsPlatform!update.action" method="post" target="myIframe">
				<input type="hidden" name="model.bussinessModuleId" id="model.bussinessModuleId" value="${model.bussinessModuleId }">
				<input type="hidden" name="model.bussinessModuleCode" id="model.bussinessModuleCode" value="${model.bussinessModuleCode }">
				<input type="hidden" name="model.increaseCode" id="model.increaseCode" value="${model.increaseCode }">
				<input type="hidden" name="model.timingDelay" id="timingDelay" value="${model.timingDelay }">
				<%--<table border="0" width="98%" bordercolor="#FFFFFF" cellspacing="0" cellpadding="0">
					<tr>
						<td>
						&nbsp;
						<img src="<%=frameroot%>/images/ico/ico.gif" width="9" height="9">&nbsp;
						模块设置</td>
					</tr>
					--%>
	<div align=left style="width: 100%;padding:5px;">
			<tr>
			<td colspan="3" class="table_headtd">
					<table border="0" width="100%" cellpadding="0" cellspacing="0">
						<tr>
							<td class="table_headtd_img" >
								<img src="<%=frameroot%>/images/ico/ico.gif" >&nbsp;
							</td>
							<td align="left">
								<strong>保存短信模块</strong>
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
		</div>
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
									
				<fieldset style="width: 100%">
					<legend>
						<span class="wz">编码规则：</span>
					</legend>
					<table width="100%" border="0" cellpadding="0" cellspacing="0">
					<tr>
						<td width="100%" class="td1" colspan="2"><span class="wz" style="color:#999999">在您打开编辑页面的时候本模块将自动进入关闭状态以防其他用户操作，请您在完成操作后再进行打开操作。</span></td>
					</tr>         
					<tr>
						<td width="22%" class="biao_bg1"  align=right><span class="wz"><font color=red>*</font> 模块名称：</span>
						</td>
						<td width="78%" class="td1">
							<input type="text" title="请您输入模块名称(此信息为必填项)" name="model.bussinessModuleName" style="width:80%;border:1px solid #b3bcc3;background-color:#ffffff;" id="modelName" value="${model.bussinessModuleName }" readonly="readonly" />
						</td>
					</tr>
					<tr>
						<td  class="biao_bg1"  align=right><span class="wz"><font color=red>*</font> 短信最大字数：</span>
						</td>
						<td  class="td1">
							<input type="text" name="model.wordNumber" style="width:80%;border:1px solid #b3bcc3;background-color:#ffffff;" id="letterNum"  title="请您输入该短信模块支持的短信最大字数(此信息为必填项)" value="${model.wordNumber }"/>
						</td>
					</tr>
					<tr>
						<td  class="biao_bg1"  align=right><span  class="wz">是否开启短信：</span>
						</td>
						<td  class="td1">
							<!--  <input type="radio" name="model.isEnable" id="openOrNot" value="1" checked />开启<input type="radio" name="model.isEnable" id="openOrNot" value="0" />关闭-->
							<s:radio name="model.isEnable" id="model.isEnable" list="#{'1':'开启','0':'关闭'}"></s:radio>
						</td>
					</tr>
										
					<tr>
						<td  class="biao_bg1"  align=right><span class="wz">自增位长度：</span>
						</td>
						<td class="td1">
							<!--<select name="zzwcd" id="zzwcd" style="width:15%">
								<option value=3 selected>3</option>
								<option value=4>4</option>
								<option value=5>5</option>
								<option value=6>6</option>
								<option value=7>7</option>
								<option value=8>8</option>
							</select>-->
							<s:select list="#{'3':'3','4':'4','5':'5','6':'6','7':'7','8':'8'}" name="model.increaseLength" id="model.increaseLength" cssStyle="width:15%"></s:select>
						</td>
					</tr>
					<tr>
						<td width="100%" class="td1" colspan="2">
							<span class="wz" style="color:#999999">短信编码规则：用户接收短信编码=支持模块编码+自增位编码+状态位编码<br>用户根据接收到的短信编码进行回复，请您认真设计其长度</span>
						</td>
					</tr>
				</table>
				</fieldset>
								<%--<SCRIPT type="text/javascript">tp1.addTabPage( document.getElementById( "tabPage1" ) );</SCRIPT>

								</DIV>--%><%--
								
								
								
								<DIV class=tab-page id=tabPage2>
									<H2 class=tab>
										默认回复
									</H2>
									--%>
              <fieldset style="width: 100%">
	               <legend>
		         		 <span class="wz">默认回复：</span>
                   </legend>
                   		<table width="100%" border="0" cellpadding="0" cellspacing="0">
                    	<tr>
	                    	<td width="100%" class="td1" colspan="2"><span class="wz" style="color:#999999">请您设置该模块所发送短信是否需要生成回复答案，以及对应答案的内容</span></td>
	                	</tr>	
					
							<tr>
								<td width="21%" class="biao_bg1"  align=right>
									<span class="wz">是否设置发送时间：</span>
								</td>
								<td width="79%" class="td1">
									<input type="radio" name="timingOrNot" id="noTiming" value="0" />不需要<input type="radio" name="timingOrNot" id="timing" value="1" />需要
								</td>
							</tr>
							<tr id="setSendTime">
								<td  class="biao_bg1"  align=right>
									<span class="wz">设置发送时间：</span>
								</td>
								<td  class="td1">
									&nbsp;
									<s:select id="timingtype" name="model.timingType" list="#{'0':'每天','1':'每月'}" listKey="key" listValue="value" onchange="changeTime(this.value)"/>
									<strong:newdate name="DelayByday" id="DelayByday" width="50%" skin="whyGreen" isicon="true" dateform="HH:mm:ss" classtyle="displaynone"></strong:newdate>
									<strong:newdate name="DelayByMonth" id="DelayByMonth" width="50%" skin="whyGreen" isicon="true" dateform="dd HH:mm:ss" classtyle="displaynone"></strong:newdate>
								</td>
								</tr>
								<tr>
									<td  class="biao_bg1"  align=right>
										<span class="wz">是否感谢回复：</span>
									</td>
									<td  class="td1">
										<s:radio name="model.thankOrNot" id="model.thankOrNot" list="#{'0':'不需要','1':'需要'}"></s:radio>
										<!-- <input type="radio" name="model.thankOrNot" id="noThank" value="0" checked />不需要<input type="radio" name="model.thankOrNot" id="thank" value="1" />需要 -->
									</td>
								</tr>
								<s:if test="model.thankOrNot==0">
									<tr id="thankContent" style="display:none">
								</s:if>
								<s:else>
									<tr id="thankContent">
								</s:else>
									<td  class="biao_bg1"  align=right>
										<span class="wz">感谢回复内容：</span>
									</td>
									<td  class="td1">
										<input type="text" name="model.thankContent" id="content"  style="width:100% ;border:1px solid #b3bcc3;background-color:#ffffff;" value="${model.thankContent }"> 
									</td>
								<tr>
									<td  class="biao_bg1"  align=right>
										<span class="wz">是否需要回复：</span>
									</td>
									<td  class="td1">
										<s:radio name="replyOrNot" id="replyOrNot" list="#{'0':'不需要回复','1':'需要回复'}"></s:radio>
										<!--<input type="radio" name="replyOrNot" id="noReply" value="0" checked />不需要回复<input type="radio" name="replyOrNot" id="reply" value="1" />需要回复  -->
									</td>
								</tr>
								<s:if test="answerNum==0">
									<tr id="selectNum" style="display:none">
								</s:if>
								<s:else>
									<tr id="selectNum">
								</s:else>
									<td  class="biao_bg1"  align=right>
										<span class="wz">短信答案个数：</span>
									</td>
									<td  class="td1">
										<!--<select name="answerNum" id="answerNum" style="width:15%">
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
										</select>-->
										<s:select list="#{'0':'0','1':'1','2':'2','3':'3','4':'4','5':'5','6':'6','7':'7','8':'8','9':'9','10':'10'}" name="answerNum" id="answerNum" cssStyle="width:15%"></s:select>
									</td>
										<tbody id="dinput">
											<s:iterator value="#request.model.toaModuleStateMeans" id="mean">
												<tr><td width="30%" class="biao_bg1"  align=right><span class="wz">状态位<s:property value="#mean.moduleStateFlag"/>含义：</span></td><td width="70%" class="td1"><input type="text" name="means" style="width:100%" value="<s:property value="#mean.moduleStateMean"/>"><input type="hidden" name="state" value="<s:property value="#mean.moduleStateFlag"/>"></td></tr>
											</s:iterator>	
										</tbody>
								   </table>
								<%--<SCRIPT type="text/javascript">tp1.addTabPage( document.getElementById( "tabPage2" ) );</SCRIPT>
								--%>
					
					<tr align="center">
						<td>
							<input type="hidden" id="subBtn"  class="input_bg" value="确定" />
							<input type="hidden" id="canBtn"  class="input_bg" value="取消" />
						</td>
					</tr>
				</fieldset>
			</form>
			
			<iframe name="myIframe" style="display:none"></iframe>
			</div>
			</body>
			
</html>
