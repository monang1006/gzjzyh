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
<s:form id="myform" name="myform" action="/xxbs/action/handling!saveEntry.action" theme="simple">
				
			<web:htabpanel var="t" tabHeadRenderer="tabHead" tabBodyRenderer="tabBody" showMenu="true" tabHeadWidth="105" autoAdaptFunc="autoAdaptWindow">
				<web:tab id="tab1" text="编辑信息" closeAble="false" />
			</web:htabpanel>

			<div id="tabBody">
			
			<div id="_tab1">
			<input type="hidden" id="orgId" name="orgId" value="${model.orgId}"/>
			<input type="hidden" name="pubId" id="pubId" value="${model.pubId}"/>
			<input type="hidden" id="mergeOrgId" name="mergeOrgId" value="${mergeOrgId}"/>
			<input type="hidden" name="toId" id="toId" value="${toId}"/>
			<input type="hidden" value="1" name="model.pubInfoType">
          <div class="dntta">
            <table width="100%">
              <tr>
                <th><font color="#FF0000">*</font> 标题：</th>
                <td><textarea style="width:800px;height:50px" rows="2" class="information_out_input_words" 
					id="pubTitle" name="pubTitle"></textarea></td>
              </tr>
              <tr>
                <th valign="top"><font color="red">*</font>正文：</th>
                <td><textarea style="width:800px;height:400px" rows="5" class="information_out_input_words"
					id="pubEditContent" name="pubEditContent"></textarea></td>
              </tr>
              <tr>
                <th valign="top">点评：</th>
                <td><textarea style="width:800px;height:40px" rows="5" class="information_out_input_words"
					id="pubComment" name="pubComment"></textarea></td>
              </tr>
              </table>
              <table width="100%">
              <tr>
               <th>
						<font color="#FF0000">*</font> 报送单位：
				</th>
                <td  class="contentTd"><input class="information_out_input" readonly="readonly" type="text" id="orgCode" name="orgName" /></td>
                 <th>
						 联系电话：
				</th>
					<td class="contentTd">
						<input class="information_out_input readOnly" type="text" readOnly="readOnly"	value="${userTelephone}"/>
					</td>
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
					<tr>
                <th><font color="#FF0000">*</font> 签发领导：</th>
                <td><input class="information_out_input" id="pubSigner"
							name="pubSigner" type="text"
							value="${IssuePeople}" style="width:188px;"/></td>
                <th><font color="#FF0000">*</font> 责任编辑：</th>
                <td><input class="information_out_input" id="pubEditor"
							name="pubEditor" type="text"
							value="${editor}" style="width:188px;"/></td>
              </tr>
            </table>
          </div>
			<input type="hidden" name="pubUseStatus" id="pubUseStatus" value="${model.pubUseStatus}"/>
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
			  <input type="button" class="information_list_choose_button9"
				value="采用" id="adopt" />			  
			  <input type="button" class="information_list_choose_button9"
				value="预采用" id="preAdopt" />
			
		</s:elseif>
			<!-- <input type="button" class="information_list_choose_button9"
				value="共享" id="share" /> -->
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
	obj.pubEditContent = pubEditContent;
	var url= "<%=root%>/xxbs/action/handling!print.action";
	//gl.showSubDialog(url,obj,"dialogWidth=600px;dialogHeight=500px");
	var ret = window.showModalDialog(url,obj,"dialogWidth=600px;dialogHeight=500px");
});

$("#said").click(function(){
	
	$("#myform").submit();
});


$(function(){
	if("${isJournalUnselected}" == "true"){
		var jour = $("#jourId")[0];
		jour.options[0].selected = true;
		$("#issId").empty();
		$("#column").empty();
	}
	
	$("#orgCode").click(function(){
		var url = "<%=root%>/xxbs/action/submit!tree.action";
		var ret = window.showModalDialog(url,"","dialogWidth=600px;dialogHeight=500px");
		var org = new Array();
		if(ret!=undefined){
			
		$("#mergeOrgId").val(ret.id);
		$("#orgCode").val(ret.title);
		}
	});
	

	if($("#pubUseStatus").val() == "1"){
			//$("input[type=text]").attr("readOnly", "readOnly");
			//$("input[type=checkbox]").attr("disabled", true);
			//$("input[type=radio]").attr("disabled", true);
			$("select").attr("disabled", true);
			//$("textarea").attr("readOnly", "readOnly");
		}
	
	$("#adopt").click(function(){
		if($("#pubTitle").val().trim()==""){
			alert("标题不能为空！");
			return;
		}
		 
		 if($("#pubEditContent").val().trim()==""){
		      alert("正文内容不能为空！");
		      return;
		 }
		if($("#orgCode").val().trim()==""){
			alert("报送单位不能为空！");
			return;
		}
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
		if($("#pubTitle").val().trim()==""){
			alert("标题不能为空!");
			return;
		}
		 if($("#pubEditContent").val().trim()==""){
		      alert("正文内容不能为空！");
		      return;
		 }
		if($("#orgCode").val().trim()==""){
			alert("报送单位不能为空!");
			return;
		}
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
