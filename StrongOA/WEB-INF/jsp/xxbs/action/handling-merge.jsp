<%@ page contentType="text/html; charset=utf-8"%>
<%@include file="/common/include/rootPath_cb.jsp"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="web" uri="/tags-web/component"%>
<html>
	<head>
		<title>报送信息</title>

		<%@include file="/common/include/meta.jsp"%>
		<link rel="stylesheet" type="text/css" href="<%=themePath%>/css/global.css" />
		<link rel="stylesheet" type="text/css" href="<%=themePath%>/css/component.css" />
		<script type="text/javascript" src="<%=scriptPath%>/common.js"></script>
		<script type="text/javascript" src="<%=scriptPath%>/global1.js"></script>
		<script type="text/javascript" src="<%=scriptPath%>/Message_${locale}.js"></script>
		<script type="text/javascript" src="<%=scriptPath%>/component-min.js"></script>
		<script type="text/javascript" src="<%=scriptPath%>/jqgrid/jqgrid.js"></script>
		<script type="text/javascript" src="<%=scriptPath%>/ckeditor/ckeditor.js"></script>
		<style type="text/css">
			html { -webkit-box-sizing:border-box; -moz-box-sizing:border-box; box-sizing:border-box; padding:40px 0px 40px 0px; overflow:hidden;}
			html,body { height:100%;}
			.nobr br{
				display:none;
			}
			.nobr select{ font-size:16px; }
		</style>
	</head>
	<base target="_self" />
	<body>
		<script type="text/javascript" src="<%=scriptPath%>/wait.js"></script>

		<div class="windows_title">
			补录信息
		</div>
		<div class="information_out" id="information_out" style="background-color:#fefefe;">
			<div id="tabHead" class="top_nav"></div>
<s:form id="myform" name="myform" action="/xxbs/action/handling!save.action" theme="simple">
				
			<web:htabpanel var="t" tabHeadRenderer="tabHead" tabBodyRenderer="tabBody" showMenu="true" tabHeadWidth="105" autoAdaptFunc="autoAdaptWindow">
			<web:tab id="tab3" text="点评" closeAble="false"  />
			<web:tab id="tab2" text="原始信息" closeAble="false" />
			<web:tab id="tab1" text="编辑信息" closeAble="false" />
				
				
			</web:htabpanel>

			<div id="tabBody">
			
			<div id="_tab1">
			<input type="hidden" id="orgId" name="orgId" value="${model.orgId}"/>
			<input type="hidden" name="pubId" id="pubId" value="${model.pubId}"/>
			<input type="hidden" id="mergeOrgId" name="mergeOrgId" value="${mergeOrgId}"/>
			<input type="hidden" name="toId" id="toId" value="${toId}"/>
          <div class="dntta">
            <table width="100%">
              <tr>
                <th><font color="#FF0000">*</font> 标题：</th>
              <td><textarea style="width:800px;height:50px" rows="2" class="information_out_input_words" 
					id="pubTitle" name="pubTitle">${model.pubTitle}</textarea></td>
              </tr>
              <tr>
                <th valign="top">正文：</th>
                <td><textarea style="width:800px;height:350px" rows="5" class="information_out_input_words"
					id="pubEditContent" name="pubEditContent"><s:iterator value="pubList"><s:property value='pubEditContent'/></s:iterator></textarea></td>
              </tr>
              <tr>
                <th valign="top">点评：</th>
                <td><textarea style="width:548px;height:40px" rows="5" class="information_out_input_words"
					id="pubComment" name="pubComment">${model.pubComment}</textarea></td>
              </tr>
              </table>
              <table width="100%">
              <tr>
                <th>报送单位：</th>
                <td><input type="text" id="orgName" class="information_out_input"
						readonly="readonly" value="${mergeOrgName}" name="orgName" style="width:169px;"/></td>
                <th>上报时间：</th>
                <td><s:date var="pdate" name="%{model.pubDate}" format="yyyy-MM-dd" />
						<input type="text" id="" class="information_out_input" style="width:145px;"
						readonly="readonly" value="${pdate}"/></td>
              </tr>
              <tr>
                <th><font color="#FF0000">*</font> 期刊期号：</th>
                <td class="nobr"><s:doubleselect id="jourId" list="journals" listKey="jourId" listValue="jourName"
						 value="%{jourId}" headerKey="0" headerValue=""
						 doubleName="TInfoBaseIssue.issId" doubleId="issId"
						 doubleList="top.TInfoBaseIssues" doubleListKey="issId" doubleListValue="issNumber" 
						 formName="myform" doubleValue="%{model.TInfoBaseIssue.issId}"/></td>
                <th><font color="#FF0000">*</font> 期刊栏目：</th>
                <td class="nobr"><s:select name="TInfoBaseColumn.colId" list="columns" id="column"
							listKey="colId" listValue="colName" value="%{model.TInfoBaseColumn.colId}"
							 /></td>
              </tr>
              </table>
              <table width="100%">
              <tr>
                <s:if test="%{model.TInfoBaseIssue.issIsPublish==\"1\"}">
                <th>批示：</th>
                <td><s:radio name="pubIsInstruction" id="pubIsInstruction" list="#{\"1\":'是',\"0\":'否'}" value="%{model.pubIsInstruction}"/></td>
                </s:if>
				<s:else>
                <th style="display: none">是否批示：</th>
                <td style="display: none"><s:radio name="pubIsInstruction" id="pubIsInstruction" list="#{\"1\":'是',\"0\":'否'}" value="%{model.pubIsInstruction}"/></td>
                </s:else>
              </tr>
              <s:if test="%{model.TInfoBaseIssue.issIsPublish==\"1\"}">
			  <tr class="pishi">
                <th>批示人：</th>
                <td><input class="information_out_input" type="text"  style="width:169px;"
						id="pubInstructor" name="pubInstructor" value="${model.pubInstructor}"/></td>
              </tr>
              <tr class="pishi">
                <th>批示内容：</th>
                <td><textarea class="information_out_input_words" style="width:548px; height:100px"
							id="pubInstructionContent" name="pubInstructionContent">${model.pubInstructionContent}</textarea></td>
              </tr>
              </s:if>
			  <s:else>
              <tr class="pishi" style="display: none">
                <th>批示人：</th>
                <td><input class="information_out_input" type="text"
						id="pubInstructor" name="pubInstructor" value="${model.pubInstructor}" style="width:169px;"/></td>
              </tr>
              <tr class="pishi" style="display: none">
                <th>批示内容：</th>
                <td><textarea class="information_out_input_words" style="width:548px;height:100px"
							id="pubInstructionContent" name="pubInstructionContent">${model.pubInstructionContent}</textarea></td>
              </tr>
              </s:else>
            </table>
          </div>
			<input type="hidden" name="pubUseStatus" id="pubUseStatus" value="${model.pubUseStatus}"/>
			</div>
						
			<div id="_tab2">
            <table>
            <s:iterator value="pubList" var="status">
              <tr>
                <th valign="top">原始标题<s:property value='#status.count'/>：</th>
                <td><input class="information_out_input" id="pubRawTitle" readonly="readonly"
							name="pubRawTitle" type="text" style="width:548px"
							value="<s:property value='pubRawTitle'/>"/></td>
              </tr>
              <tr>
                <th valign="top">原始内容<s:property value='#status.count'/>：</th>
                <td><textarea style="width:548px;height:210px" class="information_out_input_words" id="text" readonly="readonly"><s:property value='pubRawContent'/></textarea></td>
              </tr>
             </s:iterator>
            </table>
            <table>
              <tr>
                <th>约稿信息：</th>
                <td><input type="hidden" id="pubAppointId" name="TInfoBaseAppoint.aptId"
							value="${model.TInfoBaseAppoint.aptId}"/>
						<input type="text" id="appointTitle" class="information_out_input"
							value="${model.TInfoBaseAppoint.aptTitle}" style="width:188px;" readonly="readonly"/></td>
                <th>上报时间：</th>
                <td><s:date name="%{model.pubDate}" format="yyyy-MM-dd HH:mm" /></td>
              </tr>
              <tr>
                <th>报送单位：</th>
                <td><input type="hidden" id="orgId" name="orgId"
							value="${model.orgId}"/>
						<input type="text" id="" class="information_out_input"
						readonly="readonly" value="${orgName}" style="width:188px;"/>
						<input type="hidden" value="${model.pubInfoType}" name="model.pubInfoType"></td>
                <th>报送员：</th>
                <td><input type="hidden" id="pubPublisherId" name="pubPublisherId"
							value="${model.pubPublisherId}"/>
						<input class="information_out_input" id=""
							type="text" readonly="readonly"
							value="${userName}" style="width:188px;"/></td>
              </tr>
              <tr>
                <th>签发领导：</th>
                <td><input class="information_out_input" id="pubSigner"
							name="pubSigner" type="text"
							value="${model.pubSigner}" style="width:188px;"/></td>
                <th>责任编辑：</th>
                <td><input class="information_out_input" id="pubEditor"
							name="pubEditor" type="text"
							value="${model.pubEditor}" style="width:188px;"/></td>
              </tr>
            </table>
            <table>
              <tr>
                <th>邮件信息：</th>
                <td><s:if test="%{model.pubIsMailInfo==\"1\"}">
					是
					</s:if>
					<s:else>
					否
					</s:else></td>
              </tr>
            </table>
			</div>
			<div id="_tab3">
			<s:if test="%{model.pubIsComment==\"0\"}">
			暂无点评信息！
			</s:if>
				<div style="height: 230px; overflow:auto;">
				<div id="comt"></div>
				</div>
				<s:if test="%{model.pubIsComment==\"1\"}">
				<table style="float: left;">
				
				<tr>
					<td style="padding-top: 10px">
						<textarea style="width:570px;height:50px" rows="5" class="information_out_input_words" id="comment" name="comment"></textarea>
					</td>
					<td style="padding-top: 20px">
					<input type="button" value="提交" id="tijiao" onClick="add()">
					</td>
				</tr>
			</table>
			</s:if>
			</div>
			</div>
</s:form>
			</div>
			
			
		<div class="information_list_choose_pagedown">
		<!--  <input type="button" class="information_list_choose_button9"
				value="点评" id="said" />-->
		<s:if test="%{model.TInfoBaseIssue.issIsPublish==\"1\"}">
			<input type="button" class="information_list_choose_button9"
					value="保存" id="saveSubmit" />
		</s:if>
		<s:if test="%{canCancelAdopt==true}">
			<input type="button" class="information_list_choose_button9"
				value="撤销采用" id="cancelAdopt" />
		</s:if>
		<s:elseif test="%{model.pubUseStatus!=\"1\"}">
			<!-- <input name="preUse" id="preUse" style="margin:0;" type="checkbox"
				<s:if test="%{preUse}">checked="checked"</s:if>
			/>
			<label for="preUse" class="checkboxLabel">预采用</label> -->
			<!--<input type="button" class="information_list_choose_button9"
				value="编辑" id="save" />-->
			<!--  <input type="button" class="information_list_choose_button9"
				value="预采用" id="preAdopt" />-->
			<input type="button" class="information_list_choose_button9"
				value="采用" id="adopt" />
			
		</s:elseif>
			<!-- <input type="button" class="information_list_choose_button9"
				value="共享" id="share" /> -->
				<input type="button" class="information_list_choose_button9"
				value="打印预览" id="print1" />
			<input type="button" class="information_list_choose_button9"
				value="关闭" id="cancel" />
		</div>
		<div id="mask"></div>
		<web:validator errorDisplayContainer="information_out"
			errorElement="div" submitTip="true" name="validator" formId="myform"></web:validator>
	</body>
</html>
<script type="text/javascript" src="<%=scriptPath%>/stopwait.js"></script>
<script type="text/javascript">

$("#print1").click(function(){
	var pubTitle = $("#pubTitle").val();
	var pubEditContent = $("#pubEditContent").val();
	var obj = new Object();
	obj.pubTitle=pubTitle;
	obj.submitDate="${model.pubDate}";
	obj.orgName = "${mergeOrgName}";
	obj.pubEditContent = pubEditContent;
	var url= "<%=root%>/xxbs/action/handling!print.action";
	//gl.showSubDialog(url,obj,"dialogWidth=600px;dialogHeight=500px");
	var ret = window.showModalDialog(url,obj,"dialogWidth=600px;dialogHeight=500px");
});

$("#said").click(function(){
	
	$("#myform").submit();
});


$(function(){
	t.activeId('tab1'); 
	if("${isJournalUnselected}" == "true"){
		var jour = $("#jourId")[0];
		jour.options[0].selected = true;
		$("#issId").empty();
		$("#column").empty();
	}
	

	if($("#pubUseStatus").val() == "1"){
			//$("input[type=text]").attr("readOnly", "readOnly");
			//$("input[type=checkbox]").attr("disabled", true);
			//$("input[type=radio]").attr("disabled", true);
			$("select").attr("disabled", true);
			//$("textarea").attr("readOnly", "readOnly");
		}
	
	$("#adopt").click(function(){
		if($("#jourId").val == "0" || $("#issId").val() == null || $("#column").val() == null){
			alert("请选择期刊期号和栏目。");
			return;
		}
		$("#pubUseStatus").attr("value", "1");
		if(validator.form()){
			$("#myform").submit();
		}
	});
	
	$("#cancelAdopt").click(function(){
		$("input[type=checkbox]").attr("disabled", false);
		$("input[type=radio]").attr("disabled", false);
		$("select").attr("disabled", false);
		$("#pubUseStatus").attr("value", "0");
		if(validator.form()){
			$("#cancelAdopt").attr("disabled","none");
			$("#myform").submit();
		}
	});

	$("#share").click(function(){
		var url = "<%=root%>/xxbs/action/handling!viewShare.action?toId=${toId}";
		var ret = gl.showSubDialog(url,420,180);
		if(ret == "success"){
			location.reload();
		}
	});
	
	$("#save").click(function(){
		if(validator.form()){
			$("#save").attr("disabled","none");
			$("#myform").submit();
		}
	});
	
	$("#saveSubmit").click(function(){
		if(validator.form()){
			$("select").attr("disabled", false);
			$("#saveSubmit").attr("disabled","none");
			$("#myform").submit();
		}
	});
	
	$("#cancel").click(function(){
		window.close();
	});
	
	$("#preUse").click(function(){
		var preUse = false;
		if($("#preUse").attr("checked")){
			preUse = true;
		}
		$.get("<%=root%>/xxbs/action/handling!preUse.action?toId=${model.pubId}&preUse="+preUse);
	});
	
	$("#preAdopt").click(function(){
		//$.get("<%=root%>/xxbs/action/handling!preUse.action?toId=${model.pubId}&preUse=true", function(){
		//	window.close();
		//});
		$("#pubUseStatus").attr("value", "2");
		if(validator.form()){
			$("#preAdopt").attr("disabled","none");
			$("#myform").submit();
		}
	});
	
	if("${op}" == "adopt"){
		//t.activeId('tab2');
	}
	
	$("#jourId").change(function(){
		var jourId = $(this).val();
		$("#column").empty();
		if(jourId != "0"){
			$.get("<%=root%>/xxbs/action/handling!columns.action?toId="+jourId, function(res){
				var obj = res;
				if(typeof(res) == "string"){
					obj = $.parseJSON(res);
				}
				var selectObj = $("#column")[0];
				for(var i=0;i<obj.length;i++){
					selectObj.options[i] = new Option(obj[i].colName, obj[i].colId);
				}
			});
		}
	});
	
	var isInstruction = function(){
		var ps = $(".pishi");
		var val = $("input[name='pubIsInstruction']:checked").val();
		if(val == "0"){
			ps.hide();
		}
		else if(val == "1"){
			ps.show();
		}
	};
	$("input[name='pubIsInstruction']").bind("click", isInstruction);
	$(window).bind("load", isInstruction);

	$.ajax({
			url: "<%=root%>/xxbs/action/handling!comment.action?toId=${model.pubId}",
			success: function(data){
				$("#comt").html(data);
			},
			cache: false,
			dataType: 'html'
		});
	
});

function add(){
	var pubId = $("#pubId").val();
	var comment = $("#comment").val().trim();
	var comments = $("#comment").val().trim();
	comment = encodeURIComponent(comment);
	comment = encodeURIComponent(comment);
	if(comments.length>200){
		alert("评论过长！");
		return false;
	}
	if(comment!=""){
	$.ajax({
		   dataType: 'text',
		   url: "<%=root%>/xxbs/action/handling!saveComment1.action",
		   data:"pubId="+pubId+"&comment="+comment,
		   success: function(data){
				if(data == "success"){
					$.ajax({
						url: "<%=root%>/xxbs/action/handling!comment.action?toId=${model.pubId}",
						success: function(data){
							$("#comt").html(data);
						},
						cache: false,
						dataType: 'html'
					});
					alert("评论成功！");
				}
			  }
		}); 
	}
	else{
		alert("评论内容不能为空！");
		return false;
	}
}


function viewComment(){
	var url = "<%=root%>/xxbs/action/submit!viewComment.action?toId=${toId}";
	gl.showSubDialog(url,500,280);
}

function viewInstruction(){
	var url = "<%=root%>/xxbs/action/adoption!viewInstruction.action?toId=${toId}";
	gl.showSubDialog(url,500,280);
}

function autoAdaptWindow() {
	var w1 = $("#tabHead").outerWidth();
	//return w1 - 5;
	return w1;
}

//下载附件
function download(){
	var attachDownLoad = document.getElementById("attachDownLoad");
	attachDownLoad.src = "<%=root%>/xxbs/action/submit!officeStream.action?toId=${toId}";
}

function del(id){
	if(confirm('确定要将此记录删除?')){
		var toId = '${toId}';
		$.get("<%=root%>/xxbs/action/handling!deleteComment.action?toId="+id, function(response){
			if(response == "success"){
				
				alert("删除成功");
				$.ajax({
					url: "<%=root%>/xxbs/action/handling!comment.action?toId="+toId,
					success: function(data){
						$("#comt").html(data);
					},
					cache: false,
					dataType: 'html'
				});
			}
			
		});	
	}
	
}

</script>
