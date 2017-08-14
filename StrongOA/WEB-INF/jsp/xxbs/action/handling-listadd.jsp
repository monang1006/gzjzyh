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
			报送信息
		</div>
		<div class="information_out" id="information_out" style="background-color:#fefefe;">
			<div id="tabHead" class="top_nav"></div>
<s:form id="myform" name="myform" action="/xxbs/action/handling!savelist.action" theme="simple">
				
			
			<input type="hidden" name="toId" id="toId" value="${toId}"/>
			<div id="tabBody">
			
			<div id="_tab1">

             <table class="information_list" cellspacing="0" cellpadding="0">
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
</s:form>
			</div>
			
		
		<div class="information_list_choose_pagedown">
			<input type="button" class="information_list_choose_button9"
					value="保存" id="saveSubmit" />
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



$(function(){
	
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
			var flag = "${model.TInfoBaseIssue.issIsPublish}";
			if(flag==1){
				$("select").attr("disabled", true);
			}
			//$("select").attr("disabled", true);
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
	

	
	
	$("#saveSubmit").click(function(){
		if($("#jourId").val == "0" || $("#issId").val() == null || $("#column").val() == null){
			alert("请选择期刊期号和栏目。");
			return;
		}
		if(validator.form()){
			$("select").attr("disabled", false);
			$("#saveSubmit").attr("disabled","none");
			$("#myform").submit();
		}
	});
	
	$("#cancel").click(function(){
		window.close();
	});
	
	
	
	
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


</script>
