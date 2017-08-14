<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/common/include/rootPath.jsp"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<html>
	<head>
		<title>工作查询结果</title>
		<LINK href="<%=frameroot%>/css/properties_windows.css" type=text/css rel=stylesheet>
		<script src="<%=path%>/common/js/jquery/jquery-1.2.6.js" type="text/javascript"></script>
		<script src="<%=path%>/common/js/common/common.js" type="text/javascript"></script>
		<script type="text/javascript">
			//全选
			function checkAll(chkAll){//this,document.getElementById('myTable_td'),'#A9B2CA',true
				var checked = chkAll.checked;
				$("input:checkbox").attr("checked",checked);
				if(checked){
					$(".td1").css("background-color","#A9B2CA");
				}else{
					$(".td1").css("background-color","");
				}
			}
			//单击某行时修改其背景颜色
			function changeColor(currentTr){
				var chk = $("#"+currentTr+" td:first-child>input").attr("checked");
				var tagName = event.srcElement.tagName;//事件源,因为checkbox也在此tr中
				if(chk){
					if(tagName!="INPUT"){//如果单击的是tr中的某个TD，不是checkbox
						$("#"+currentTr+" td:first-child>input").attr("checked",false);
						$("#"+currentTr+">td").css("background-color","");
					}else{
						$("#"+currentTr+">td").css("background-color","#A9B2CA");
					}	
				}else{
					if(tagName!="INPUT"){
						$("#"+currentTr+" td:first-child>input").attr("checked",true);
						$("#"+currentTr+">td").css("background-color","#A9B2CA");
					}else{
						$("#"+currentTr+">td").css("background-color","");
					}	
					
				}
			}
			
			//复选框单击事件
			function chkClick(){
				
			}
			
			/**
			* 查看详情
			* @param instanceId 流程实例ID
			* @param businessId 业务数据{表名;主键字段名;主键值}
			*/
			function viewInfo(instanceId,businessId){
				var width=screen.availWidth-10;;
           		var height=screen.availHeight-30;
          		var ReturnStr=OpenWindow("<%=root%>/work/work!displayworkview.action?bussinessId="+businessId+
	                         			 "&instanceId="+instanceId, 
                                   		 width, height, window);
			}
			//查看流程图
		    function workflowView(instanceId){      
		        var width=screen.availWidth-10;;
		        var height=screen.availHeight-30;
		        var ReturnStr=OpenWindow("<%=root%>/work/work!PDImageView.action?instanceId="+instanceId, 
		                                   width, height, window);
		    }
		</script>
	</HEAD>
<BODY class=contentbodymargin oncontextmenu="return false;">
		<DIV id=contentborder align=center>
			<table id="myTable_div" width="100%"  cellpadding="0" cellspacing="0">
				<div id="myDiv" style="display:none; height:201px; left:12px; position:absolute; top:50px; width:28px; z-index:1">
				 	<hr id="myLine" width="1" size="200" noshade color="#F4F4F4">
				</div>
				<tr>
					<td valign=top>
						<img src="<%=root %>/oa/image/work/infofind.gif" /><font size="5"> 工作查询结果</font>
					</td>
				</tr>
				<tr>
					<td valign=top align="left">
						<table id="myTable" style="vertical-align: top;" align="left" class="table1"  cellpadding=0 cellspacing=1 width="100%" height="100%"  >
							<thead>
								<tr>
									<th width="3%" height="22" class="biao_bg2"><input id="checkall" type="checkbox" name="checkall" onclick="checkAll(this);"></th>
									<!-- <th width="8%" align="center" height="22" class='biao_bg2'>流水号</th>-->
									<th align="center" class='biao_bg2'>工作名称/文号</th>
									<th width="20%" align="center" height="22" class='biao_bg2'>开始时间</th>
									<th width="10%" align="center" height="22" class='biao_bg2'>状态</th>
									<th width="20%" colspan="1" align="center" height="22" class='biao_bg2'>操作</th>
									<th class="biao_bg2" style="text-indent: 0px;"></th>
								</tr>
							</thead>
							<tbody>
								<s:iterator value="workFlowLst" >
									 <tr id="tr<s:property value='flowNum'/>" onclick="changeColor(this.id);">
										<td width="3%" id="chkButtonTd" class="td1" style="text-indent: 0px;" align="center">
											<input type="checkbox" name="chkButton">
										</td>
										<!-- <td width="8%" class="td1">${flowNum }</td>-->
										<td class="td1">${bussinessName }</td>
										<td class="td1">${flowStartDate }</td>
										<td class="td1">${flowStatus }</td>
										<td class="td1" align="center">
											<a href="JavaScript:viewInfo('${flowNum }','${businessId }');">查看详情</a>&nbsp;&nbsp;
											<a href="JavaScript:workflowView('${flowNum }');">流程图</a>
										</td>
										<td class="td1" style="text-indent: 0px;"></td>
									</tr>
								</s:iterator>
								<s:if test="workFlowLst!=null && workFlowLst.size()>0">
									<tr style="background-color: #e3eef2;border: thick;">
										<td colspan="7">
											&nbsp;&nbsp;
											<input type="checkbox" onclick="checkAll(this);">全选&nbsp;&nbsp;
											<span>
												共<FONT color="red" size="4"><b><s:property value="workFlowLst.size()"/></b></FONT>条&nbsp;&nbsp;
												<input type="button" class="input_bg" onclick="history.back();" value="返 回">
											</span>
										</td>
									</tr>	
								</s:if>
								<s:if test="workFlowLst.size()==0">
									<tr style="background-color: #e3eef2;border: thick;">
										<td colspan="7" align="center">
											<span>
												<FONT color="red" size="4"><b>未找到记录</b></FONT>&nbsp;&nbsp;
												<input type="button" class="input_bg" onclick="history.back();" value="返 回">
											</span>
										</td>
									</tr>	
								</s:if>
							</tbody>
						</table>
					</td>
				</tr>
			</table>
		</DIV>
 </BODY>
</HTML>
