<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<jsp:directive.page import="com.strongit.oa.util.TempPo" />
<%@taglib uri="/struts-tags" prefix="s"%>
<%@include file="/common/include/rootPath.jsp"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
	<head>
		<title>办理记录查看</title>
		<%@include file="/common/include/meta.jsp"%>
		<LINK href="<%=frameroot%>/css/properties_windows.css" type=text/css rel=stylesheet>
		<link type="text/css" rel="stylesheet"
			href="<%=path%>/common/js/tabpane/css/luna/tab.css" />
		<script src="<%=path%>/common/js/jquery/jquery-1.2.6.js" type="text/javascript"></script>
		<style  media="screen" type="text/css">
		.text-overflow {  
			    display:block;/*内联对象需加*/  
			    width:31em;  
			    word-break:keep-all;/* 不换行 */  
			    white-space:nowrap;/* 不换行 */  
			    overflow:hidden;/* 内容超出宽度时隐藏超出部分的内容 */  
			    text-overflow:ellipsis;/* 当对象内文本溢出时显示省略标记(...) ；需与overflow:hidden;一起使用。*/  
			} 
		</style>
		<script type="text/javascript">	
			//还存在的问题，修改时，approveId没对应上，所以修改不成功
			//可能存在修改之后，title属性对应的值还未变，注意检查
			
				
			var flag = false;
			//修改
			function edit(id){
				$("#lable_"+id).hide();
				$("#input_"+id).show();
				$("#btn_save_"+id).attr("disabled",false);
			}
			
				//保存
			function save(id) {
				var oldContent = $("#lable_"+id).text();
				var newConent  = $("#input_"+id ).val();
				if(oldContent == newConent){
					alert("内容未发生变化，操作结束。");
					return ;
				}
				if(newConent.length > 500){
					alert("处理意见字数不能超过500个。");
					return;
				}
				$("#btn_save_"+id).attr("disabled",true);
				$.post("<%=path%>/workflowDesign/action/processMonitor!saveApproveInfo.action",{approveId:id,oldContent:oldContent,content:newConent},
					function(ret){
						if(ret == "0"){//修改成功
							$("#lable_"+id).show();
							$("#input_"+id).hide();
							$("#td_"+id).attr("title",newConent);
							if(newConent!=""&&newConent.length>30){
								newConent = newConent.substring(0,30)+"...";
							}
							$("#lable_"+id).text(newConent);
							flag = true;//修改成功
						} else if(ret == "-1"){//发生异常
							alert("对不起，操作失败，请与管理员联系。");
						} else if(ret == "-2"){//记录不存在
							alert("记录不存在或已删除，操作失败。");
						}
					});
			}
			
			//关闭
			function onReturn(){
				window.returnValue = flag;
			}
			
			
	        $(document).ready(function(){

	        //页面加载完毕后， 判断是否能修改处理意见
			/*var taskId = "${taskId}";
			$.post("<%=path%>/senddoc/sendDoc!chkModifySuggestion.action",{taskId:taskId},
					function(ret){
						if(ret == "1"){//修改成功
						//	$(".td_edit").show();
						}
					});*/
	        });
		</script>
	</head>

	<body onload="window.focus();">
		<div id="contentborder" align="center">
			<div>
				<table cellSpacing=1 cellPadding=1 width="100%" border="0"
					class="table1">
					<tr class="biao_bg2">
						<td align="center" width="100" class="text-overflow">
							<strong>名 称</strong>
						</td>
						<td align="center" width="180" class="text-overflow">
							<strong>处理人</strong>
						</td>
						<td align="center" width="52%" class="text-overflow">
							<strong>处理意见</strong>
						</td>
						<td align="center" width="120" class="text-overflow">
							<strong>处理时间</strong>
						</td>
						<td align="center" width="80" class="text-overflow">
							<strong>CA签名</strong>
						</td>
						<td class="td_edit" align="center" width="10%" style="display:none" class="text-overflow">
							<strong>操作</strong>
						</td>
					</tr>
					<s:iterator value="listAnnal" status="status">
						<tr class="biao_bg1">
							<td align="center">
								<s:property value="listAnnal[#status.index][1]" />
							</td>
							<td align="center">
								<s:property value="listAnnal[#status.index][4]" />
							</td>
							<td id="td_<s:property value="listAnnal[#status.index][0]" />" align="left" title='<s:property value="listAnnal[#status.index][8]" />'>
								<span id="lable_<s:property value="listAnnal[#status.index][0]" />">
								<s:property value="listAnnal[#status.index][5]" />
								</span>
								<input style="display:none;width:200px;" id='input_<s:property value="listAnnal[#status.index][0]" />' value="<s:property value="listAnnal[#status.index][8]" />" />
							</td>
							<td align="center">
								<s:date name="listAnnal[#status.index][6]"
									format="yyyy-MM-dd HH:mm:ss" />
							</td>
							<td align="left">
								<s:property value="listAnnal[#status.index][7]" />
							</td>
							<td class="td_edit" align="left" style="display:none">
								<input id="btn_edit_<s:property value="listAnnal[#status.index][0]" />" type="button" onclick="edit(<s:property value="listAnnal[#status.index][0]" />);" class="input_bg" value="修改" />
								<input id="btn_save_<s:property value="listAnnal[#status.index][0]" />" type="button" onclick="save(<s:property value="listAnnal[#status.index][0]" />);" disabled class="input_bg" value="确定" />
							</td>
						</tr>
					</s:iterator>
				</table>
			</div>

			<div align="center">
				<table>

				</table>
			</div>
	</body>
</html>
