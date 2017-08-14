<%@ page contentType="text/html; charset=utf-8"%>
<%@include file="/common/include/rootPath_cb.jsp"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="web" uri="/tags-web/component"%>
<html>
	<head>
		<title>查看合并信息</title>

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
			查看合并信息
		</div>
		<div class="information_out" id="information_out" style="background-color:#fefefe;">
			<div id="tabHead" class="top_nav"></div>
<s:form id="myform" name="myform" action="/xxbs/action/handling!save.action" theme="simple">
				
			
			<web:htabpanel var="t" tabHeadRenderer="tabHead" tabBodyRenderer="tabBody" showMenu="true" tabHeadWidth="105" autoAdaptFunc="autoAdaptWindow">
				<web:tab id="tab3" text="点评" closeAble="false"  />
				<web:tab id="tab2" text="原始信息" closeAble="false" />
			</web:htabpanel>

			<div id="tabBody">
			
						
			<div id="_tab2">
            <table>
              <tr>
                <th valign="top">原始标题：</th>
                <td><textarea style="width:800px;height:50px" rows="2" class="information_out_input_words" readonly="readonly"
					id="pubRawTitle" name="pubRawTitle">${model.pubRawTitle}</textarea></td>
              </tr>
              <tr>
                <th valign="top">原始内容：</th>
                <td><textarea style="width:800px;height:400px" class="information_out_input_words" id="text" readonly="readonly">${model.pubRawContent}</textarea></td>
              </tr>
            </table>
            <table>
              <tr>
                <th>约稿信息：</th>
                <td><input type="hidden" id="pubAppointId" name="TInfoBaseAppoint.aptId"
							value="${model.TInfoBaseAppoint.aptId}"/>
						<input type="text" id="appointTitle" class="information_out_input"
							value="${model.TInfoBaseAppoint.aptTitle}" style="width:188px;" readonly="readonly"/></td>
                <th>上报时间：</th>
                <td><s:date name="%{model.pubDate}" format="yyyy-MM-dd HH:mm" />
                <input type="hidden" value="${model.pubDate}" id = "submitDate"> 
                </td>
              </tr>
              <tr>
                <th>报送单位：</th>
                <td><input type="hidden" id="orgId" name="orgId"
							value="${model.orgId}"/>
						<input type="text" id="orgName" class="information_out_input"
						readonly="readonly" value="${model.orgName}" style="width:188px;"/>
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
                <td><input class="information_out_input" id="pubSigner" readonly="readonly"
							name="pubSigner" type="text"
							value="${model.pubSigner}" style="width:188px;"/></td>
                <th>责任编辑：</th>
                <td><input class="information_out_input" id="pubEditor" readonly="readonly"
							name="pubEditor" type="text"
							value="${model.pubEditor}" style="width:188px;"/></td>
              </tr>
              <tr>
                <th>邮件信息：</th>
                <td><s:if test="%{model.pubIsMailInfo==\"1\"}">
					是
					</s:if>
					<s:else>
					否
					</s:else></td>
					 <s:if test="%{model.pubFile1!=null||model.pubFile2!=null}">
					<th>附件：</th>
					<td>
						<s:if test="%{model.pubFile1!=null}">
						<a href="#" onclick='down("${model.pubFile1}","${model.pubFile1Name}")'><font color="blue">${model.pubFile1Name}</font></a>,&nbsp;&nbsp;&nbsp;
						</s:if>
						<s:if test="%{model.pubFile2!=null}">
						<a href="#" onclick='down("${model.pubFile2}","${model.pubFile2Name}")'><font color="blue">${model.pubFile2Name}</font></a>
						</s:if>
					</td>
					</s:if>
              </tr>
            </table>
			</div>
			<div id="_tab3">
			<s:if test="%{model.pubIsComment==\"0\"}">
			暂无点评信息！
			</s:if>
			<s:else>
				<div style="height: 500px; overflow:auto;">
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
			</s:else>
			</div>
			</div>
</s:form>
			</div>
			
		
		<div class="information_list_choose_pagedown">
		 <s:if test="%{op!=\"flag\"}">
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
			<s:if test="%{model.pubUseStatus!=\"2\"}">
			<input type="button" class="information_list_choose_button9"
				value="预采用" id="preAdopt" />
			</s:if>
		</s:elseif>
			<input type="button" class="information_list_choose_button9"
					value="保存" id="saveSubmit" />
		<input type="button" class="information_list_choose_button9"
				value="关闭" id="cancel" />
		<input type="button" class="information_list_choose_button9"
				value="打印预览" id="print1" />
		<input type="button" class="information_list_choose_button9" 
				value="点评" id="said" />
		
			<!-- <input type="button" class="information_list_choose_button9"
				value="共享" id="share" /> -->
				
		</s:if>
		<s:else>
		<input type="button" class="information_list_choose_button9"
				value="关闭" id="cancel" />
		</s:else>
		 <s:if test="%{op!=\"flag\"}">
		  <div style=" float:right; padding:3px 86px 0 0;">
		    <a id="pre" onclick="checkPre()" href="<%=root%>/xxbs/action/handling!preView.action?toId=${model.pubId}" title="上一条" ><img src="<%=themePath%>/images/dz.gif" /></a>
		    &nbsp;&nbsp;<a id="next" onclick="checkNext()" href="<%=root%>/xxbs/action/handling!nextView.action?toId=${model.pubId}" title="下一条" ><img src="<%=themePath%>/images/dy.gif" /></a>
		  </div>
		 </s:if>
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
	var submitDate = $("#submitDate").val();
	var orgName = $("#orgName").val();
	var obj = new Object();
	obj.pubTitle=pubTitle;
	obj.pubEditContent = pubEditContent;
	obj.submitDate = submitDate;
	obj.orgName = orgName;
	var url= "<%=root%>/xxbs/action/handling!print.action";
	//gl.showSubDialog(url,obj,"dialogWidth=600px;dialogHeight=500px");
	var ret = window.showModalDialog(url,obj,"dialogWidth=1000px;dialogHeight=700px");
});

$("#said").click	(function(){
	var pubComment = $("#pubComment").val().trim();
	if(pubComment==""){
		alert("点评信息不能为空!");
		return;
	}
	/*if($("#jourId").val == "0" || $("#issId").val() == null || $("#column").val() == null){
		alert("请选择期刊期号和栏目。");
		return;
	}*/
	
	$("#myform").submit();
});


$(function(){
	t.activeId('tab2');
	$("#cancel").click(function(){
		window.close();
	});
	
	
});



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

function down(file,fileName){
	fileName = encodeURIComponent(fileName);
	fileName = encodeURIComponent(fileName);
	window.location = "<%=root%>/xxbs/action/submit!officeStream2.action?file="+file+"&fileName="+fileName;
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

function checkNext(){
	$.get("<%=root%>/xxbs/action/handling!isNext.action?toId=${model.pubId}", function(response){
		if(response == "success"){
			alert("已经是最后一篇了!");
			$("#next").attr("href","#");
		}
	});	
}

function checkPre(){
	var toId = "${model.pubId}";
	$.get("<%=root%>/xxbs/action/handling!isPre.action?id="+toId, function(response){
		if(response == "success"){
			alert("已经是第一篇了!");
			$("#pre").attr("href","#");
		}
	});	
}

</script>
