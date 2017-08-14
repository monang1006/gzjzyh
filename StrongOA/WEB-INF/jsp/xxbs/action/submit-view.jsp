<%@ page contentType="text/html; charset=utf-8"%>
<%@include file="/common/include/rootPath_cb.jsp"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="web" uri="/tags-web/component"%>
<html>
	<head>
		<title>查看报送信息</title>

		<%@include file="/common/include/meta.jsp"%>
		<link rel="stylesheet" type="text/css" href="<%=themePath%>/css/global.css" />
		<link rel="stylesheet" type="text/css" href="<%=themePath%>/css/component.css" />
		<script type="text/javascript" src="<%=scriptPath%>/common.js"></script>
		<script type="text/javascript" src="<%=scriptPath%>/global1.js"></script>
		<script type="text/javascript" src="<%=scriptPath%>/Message_${locale}.js"></script>
		<script type="text/javascript" src="<%=scriptPath%>/component-min.js"></script>
		<script type="text/javascript" src="<%=scriptPath%>/jqgrid/jqgrid.js"></script>
		<style type="text/css">
			html { -webkit-box-sizing:border-box; -moz-box-sizing:border-box; box-sizing:border-box; padding:40px 0px 40px 0px; overflow:hidden;}
			html,body { height:100%;}
		</style>
	</head>
	<base target="_self" />
	<body>
		<script type="text/javascript" src="<%=scriptPath%>/wait.js"></script>

		<div class="windows_title">
			查看报送信息
		</div>
		<div class="information_out" id="information_out">
			<div id="tabHead" class="top_nav"></div>
				
			<web:htabpanel var="t" tabHeadRenderer="tabHead" tabBodyRenderer="tabBody" showMenu="true" tabHeadWidth="105" autoAdaptFunc="autoAdaptWindow">
				<web:tab id="tab3" text="点评" closeAble="false" />
				<web:tab id="tab2" text="原始信息" closeAble="false" />
				<web:tab id="tab1" text="采用信息" closeAble="false" />
				
			</web:htabpanel>

			<div id="tabBody">
			
			<div id="_tab1">
			<table class="information_list" style="width:97%" cellspacing="0" cellpadding="0">
				<tr>
					<td class="labelTd">
						<font color="#FF0000">*</font> 标题：
					</td>
					<td class="contentTd" colspan="3">
						<textarea style="width:800px;height:50px" rows="2" class="information_out_input_words" readonly="readonly"
					id="pubTitle" name="pubTitle">${model.pubTitle}</textarea>
					</td>
				</tr>
				<tr>
					<td class="labelTd" valign="top">
						<font color="#FF0000"></font> 正文：
					</td>
					<td class="contentTd" colspan="3">
						<textarea style="width:800px;height:400px" rows="5" class="information_out_input_words" readonly="readonly"
					id="pubEditContent" name="pubEditContent">${model.pubEditContent}</textarea>
					</td>
				</tr>
				<iframe id="attachDownLoad" src=''
				style="display: none; border: 4px solid #CCCCCC;"></iframe>
				
				<tr>
					<td class="labelTd">
						<font color="#FF0000"></font> 报送单位：
					</td>
					<td class="contentTd"><input type="text" id="orgName" class="information_out_input"
						readonly="readonly" value="${orgName}" style="width:188px;"/></td>
					<!--  <td class="labelTd">
						<font color="#FF0000"></font> 信息类型
					</td>
					<td class="contentTd">
						<s:if test="%{model.pubInfoType==\"0\"}">普通信息</s:if>
						<s:elseif test="%{model.pubInfoType==\"1\"}">涉密信息</s:elseif>					
					</td>-->
					<td class="labelTd">
						<font color="#FF0000"></font> 上报时间：
					</td>
					<td class="contentTd"><input class="information_out_input" id="pubDate" readonly="readonly" type="text"
							value="<s:date name="%{model.pubDate}" format="yyyy-MM-dd HH:mm" />" style="width:188px;"/></td>
				</tr>
				<tr>
						<td class="labelTd">
						<font color="#FF0000"></font> 签发领导：
					</td>
					<td class="contentTd"><input class="information_out_input" id="pubSigner" readonly="readonly"
							name="pubSigner" type="text"
							value="${model.pubSigner}" style="width:188px;"/></td>
					<td class="labelTd">
						 报送员：
					</td>
					<td class="contentTd"><input class="information_out_input" id=""
							type="text" readonly="readonly"
							value="${userName}" style="width:188px;"/></td>
				</tr>
				<tr>
					<td class="labelTd">
						<font color="#FF0000"></font> 责任编辑：
					</td>
					<td class="contentTd"><input class="information_out_input" id="pubEditor" readonly="readonly"
							name="pubEditor" type="text"
							value="${model.pubEditor}" style="width:188px;"/></td>
					<td class="labelTd">
						 邮件信息：
					</td>
					<td class="contentTd">
					<s:if test="%{model.pubIsMailInfo==\"1\"}">
					<input class="information_out_input" id="pubIsMailInfo" readonly="readonly" type="text"
							value="是" style="width:188px;"/>
					</s:if>
					<s:else>
					<input class="information_out_input" id="pubIsMailInfo" readonly="readonly" type="text"
							value="否" style="width:188px;"/>
					</s:else>
					</td>
				</tr>
				<tr>
					<td class="labelTd">
						 约稿信息：
					</td>
					<td class="contentTd"><input type="text" id="appointTitle" class="information_out_input"
							value="${model.TInfoBaseAppoint.aptTitle}" style="width:188px;" readonly="readonly"/></td>
					<td class="labelTd">
						 共享：
					</td>
					<td class="contentTd">
						<s:if test="%{model.pubIsShare==\"1\"}">
						<input class="information_out_input" id="pubIsShare" readonly="readonly" type="text"
							value="是" style="width:188px;"/>
						</s:if>
						<s:else>
						<input class="information_out_input" id="pubIsShare" readonly="readonly" type="text"
							value="否" style="width:188px;"/>
						</s:else>
					</td>
				</tr>
				<tr>
					<td class="labelTd">
						 采用：
					</td>
					<td class="contentTd">
						<s:if test="%{model.pubUseStatus==\"1\"}">
						<input class="information_out_input" id="pubUseStatus" readonly="readonly" type="text"
							value="是" style="width:188px;"/>
						</s:if>
						<s:else>
						<input class="information_out_input" id="pubUseStatus" readonly="readonly" type="text"
							value="否" style="width:188px;"/>
						</s:else>
					</td>
					<td class="labelTd">
						 点评：
					</td>
					<td class="contentTd">
						<s:if test="%{model.pubIsComment==\"1\"}">
						<button class="input_button_5" onclick="viewComment();">查看点评</button>
						</s:if>
						<s:else>
						<input class="information_out_input" id="pubIsComment" readonly="readonly" type="text"
							value="否" style="width:188px;"/>
						</s:else>
					</td>
				</tr>
				<tr>
					<td class="labelTd">
						 批示：
					</td>
					<td class="contentTd">
						<s:if test="%{model.pubIsInstruction==\"1\"}">
						<button class="input_button_5" onclick="viewInstruction();">查看批示</button>
						</s:if>
						<s:else>
						<input class="information_out_input" id="pubIsInstruction" readonly="readonly" type="text"
							value="否" style="width:188px;"/>
						</s:else>
					</td>
					
				</tr>
				<s:if test="%{model.pubFile1!=null||model.pubFile2!=null}">
				<tr>
					<td class="labelTd">
						附件：
					</td>
					<td class="contentTd" width="300px">
						<s:if test="%{model.pubFile1!=null}">
						<img src="<%=path%>/oa/image/mymail/yes.gif">
						<a href="#" onclick='down("${model.pubFile1}","${model.pubFile1Name}")'><font color="blue">${model.pubFile1Name}</font></a>,
						</s:if>
						<s:if test="%{model.pubFile2!=null}">
						<img src="<%=path%>/oa/image/mymail/yes.gif">
						<a href="#" onclick='down("${model.pubFile2}","${model.pubFile2Name}")'><font color="blue">${model.pubFile2Name}</font></a>
						</s:if>
					</td>
					
				</tr>
				</s:if>
			</table>

			</div>
			
			<div id="_tab2">
			<table class="information_list" style="width:97%" cellspacing="0" cellpadding="0">
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
			</div>
			<input type="hidden" name="pubId" id="pubId" value="${model.pubId}"/>
			<div id="_tab3">
			<s:if test="%{model.pubIsComment==\"0\"}">
			暂无点评信息！
			</s:if>
			<s:else>
				<div style="height: 500px; overflow:auto;">
				<div id="comt"></div>
				</div>
				<s:if test="%{model.pubIsComment==\"1\"}">
				<table style="float: left; height: 180px">
				<tr>
					<td style="padding-top: 10px">
						<textarea style="width:570px;height:50px" rows="5" class="information_out_input_words" id="comment" name="comment"></textarea>
					</td>
					<td style="padding-top: 20px">
					<input type="button" value="提交" id="tijiao" onclick="add()">
					</td>
				</tr>
				
			</table>
			</s:if>
			</s:else>
			</div>
			</div>
			
		</div>
		<div class="information_list_choose_pagedown">
			<input type="button" class="information_list_choose_button9"
				value="关闭" name="cancel" id="cancel" />
		</div>
		<div id="mask"></div>
		<web:validator errorDisplayContainer="information_out"
			errorElement="div" submitTip="true" name="validator" formId="myform"></web:validator>
	</body>
</html>
<script type="text/javascript" src="<%=scriptPath%>/stopwait.js"></script>
<script type="text/javascript">

$(function(){
	t.activeId('tab1'); 
	$("#cancel").click(function(){
		window.close();
	});
	
	$.ajax({
		url: "<%=root%>/xxbs/action/handling!comment.action?toId=${model.pubId}",
		success: function(data){
			$("#comt").html(data);
		},
		cache: false,
		dataType: 'html'
	});
	
});


function viewComment(){
	var url = "<%=root%>/xxbs/action/submit!viewComment.action?toId=${toId}";
	gl.showSubDialog(url,700,500);
}

function viewInstruction(){
	var url = "<%=root%>/xxbs/action/handling!viewInstruction.action?toId=${toId}";
	gl.showSubDialog(url,700,500);
}


function autoAdaptWindow() {
	var w1 = $("#tabHead").outerWidth();
	//return w1 - 5;
	return w1;
}

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
					$("#comment").val("");
				}
			  }
		}); 
	}
	else{
		alert("评论内容不能为空！");
		return false;
	}
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

function down(file,fileName){
	fileName = encodeURIComponent(fileName);
	fileName = encodeURIComponent(fileName);
	window.location = "<%=root%>/xxbs/action/submit!officeStream2.action?file="+file+"&fileName="+fileName;
}
//下载附件
function download(){
	var attachDownLoad = document.getElementById("attachDownLoad");
	attachDownLoad.src = "<%=root%>/xxbs/action/submit!officeStream.action?toId=${toId}";
}

</script>
