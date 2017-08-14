<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ page import="com.strongit.oa.util.TempPo"%>
<%@ page import="com.strongit.oa.bo.ToaSendDocRegist"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<%@ taglib uri="/tags/web-newdate" prefix="strong"%>
<%@include file="/common/include/rootPath.jsp"%> 
<%@include file="/common/include/meta.jsp" %>

<html>
  <head>   
    <title>公文登记</title>
   	<link rel="stylesheet" type="text/css" href="<%=frameroot%>/css/properties_windows.css">
	
	<script src="<%=path%>/oa/js/calendar/spinelz_lib/prototype.js" type="text/javascript"></script>
	<script src="<%=path%>/common/js/jquery/jquery-1.2.6.js" type="text/javascript"></script>
	<script src="<%=path%>/oa/js/recvdoc/multiFile.js" type="text/javascript"></script>
	<script src="<%=path%>/common/js/common/common.js" type="text/javascript"></script>
	<script type="text/javascript">
		$().ready(
			function(){
				if("${requestScope.model.registId}"!=""){
					document.getElementById("saveAndAdd").style.display = "none";
				}
				if("${requestScope.model.docCode}"!=null&&"${requestScope.model.docCode}"!=""&&"${requestScope.model.registId}"==""){
					alert("保存成功!");
				}
		});
		
		function save(param){
			if(check("receiveTime")&&check("docTitle")&&check("send")&&check("toRoom")&&check("sendTime")){
				if(param == '1'){			//保存
					form.submit();
				}else{						//保存并添加
					document.getElementById("actionType").value = "2";
					form.submit();
				}
			}									
		}
		
		//提交前验证
		function check(param){
			var paramValue = document.getElementById(param).value;
			paramValue = paramValue.replace(/\s+/g,"");
			if(paramValue == null || paramValue == ""){
				alert("必填项未填写完整并且不能全为空格!");
				return false;
			}
			return true;
		}
		
		function windowclose(){
			window.close();
		}
		
		/**
		 * 表单上选择关联的字典项
		 * 
		 * @param param
		 *            字典名
		 */
		function selectOrgFromDict(param,id) {
			// var ret =
			// OpenWindow("<%=root%>/address/addressOrg!showDictOrgTreeWithCheckbox.action?type="+param,420,
			// 370, window);
			var ret = window
					.showModalDialog(
							scriptroot
									+ "/address/addressOrg!showDictOrgTreeWithCheckbox.action?type="
									+ param,
							window,
							'dialogWidth:420pt ;dialogHeight:370pt;status:no;help:no;scroll:no;status:0;help:0;scroll:0;');
			var date = new Date();
			if (ret)
				document.getElementById(id).value = ret + "〔" + date.getFullYear() + "〕号";  
		}
		
//		function departscelet(){			
//				var objId = "tempfileDepartment";
// 				var objName = "tempfileDepartmentName";
// 				var URLStr = "<%=path %>"+"/senddocRegist/sendDocRegist!orgtree.action?objId="+objId+"&objName="+objName+"&moduletype=pige";
//	 			var sty="dialogWidth:550px;dialogHeight:280px;help:no;status:no;scroll:no";
//				showModalDialog(URLStr, window, sty);
// 			}
 			
		function getOrgInfo(orgid,orgName){
			document.getElementById("toRoom").value = orgid; 
			document.getElementById("toRoomName").value = orgName; 	
		}
		
		function clearOrgInfo(){
			document.getElementById("toRoom").value = ""; 
			document.getElementById("toRoomName").value = "";
		}
		
		function setToRoomName(){
			var idAndName = document.getElementById("room").value;
			idAndName = idAndName.split(";");
			document.getElementById("toRoom").value = idAndName[0];
			document.getElementById("toRoomName").value = idAndName[1];
		}
	</script>
  </head>  
	<base target="_self"/>
  <body onload=setToRoomName()>
  <script type="text/javascript" src="<%=path %>/common/js/newdate/WdatePicker.js"></script>
  <DIV id=contentborder align=center>
  <s:form action="/senddocRegist/sendDocRegist!save.action" name="form" method="post" enctype="multipart/form-data" >
  <input type="hidden" name="model.registId" id="registId" value="${model.registId}">
  <input type="hidden" name="actionType" id="actionType" value="">
    <table width="100%" border="0" cellspacing="0" cellpadding="00">
		<tr>
			<td height="40"
				style="FILTER:progid:DXImageTransform.Microsoft.Gradient(gradientType=0,startColorStr=#ededed,endColorStr=#ffffff);">
				<table width="100%" border="0" cellspacing="0" cellpadding="00">
					<tr>
						<td>
						&nbsp;
						</td>
						<td width="30%">
							<img src="<%=frameroot%>/images/ico/ico.gif" width="9" height="9">&nbsp;
							公文登记
						</td>
						<td>
							&nbsp;
						</td>
						<td width="70%">
						<table border="0" align="right" cellpadding="00" cellspacing="0">
			                <tr>
			                  	 <td><a class="Operation" href="javascript:save('1');"><img src="<%=root%>/images/ico/baocun.gif" width="15" height="15" class="img_s">保存&nbsp;</a></td>
				           		 <td width="5"></td>
				           		 <td id="saveAndAdd" name="saveAndAdd"><a class="Operation" href="javascript:save('2');"><img src="<%=root%>/images/ico/baocun.gif" width="15" height="15" class="img_s">保存并添加&nbsp;</a></td>
				           		 <td width="5"></td>
			                   	<td><a class="Operation" href="javascript:windowclose();"><img src="<%=root%>/images/ico/quxiao.gif" width="15" height="15" class="img_s">取消&nbsp;</a></td>
				            	<td width="5"></td>
			                </tr>
			            </table>
						</td>
					</tr>
				</table>
			</td>
		</tr>
	</table>
	<table border="0" cellpadding="0" cellspacing="1" width="100%">
		<tr>
			<td>
				<table>
					<tr>
						<td class="biao_bg1" width="20%" align="right">
							<span class="wz">收办时间(<font color=red>*</font>)：</span>
						</td>
						<td class="td1" width="70%">
						<%
							ToaSendDocRegist sdr = (ToaSendDocRegist)request.getAttribute("model");
							Date receiveTime = sdr.getReceiveTime();
							if(receiveTime==null){
						%>
						&nbsp;<strong:newdate name="model.receiveTime" id="receiveTime"  dateform="yyyy-MM-dd HH:mm:ss" dateobj="<%=new Date() %>"
							width="200px" skin="whyGreen" isicon="true"></strong:newdate>
						<%
							}else{
						%>
						&nbsp;<strong:newdate name="model.receiveTime" id="receiveTime"  dateform="yyyy-MM-dd HH:mm:ss" dateobj="${model.receiveTime}"
							width="200px" skin="whyGreen" isicon="true"></strong:newdate>
						<%	
							} 
						%>
						</td>
					</tr>
					<tr>
						<td class="biao_bg1" width="20%" align="right">
							<span class="wz">公文标题(<font color=red>*</font>)：</span>
						</td>
						<td class="td1" width="70%">
						<%
							String docT = sdr.getDocTitle();
							if(docT==null){
								docT = "";
							}else if(!"".equals(docT) && docT.indexOf("\"")>-1){
								docT = docT.replaceAll("\"", "&quot;");
							}
						 %>
						&nbsp;<input id="docTitle" name="model.docTitle" type="text"  size="60" maxlength="60" value="<%=docT%>">
						</td>
					</tr>
					<tr>
						<td class="biao_bg1" width="20%" align="right">
							<span class="wz">签发(<font color=red>*</font>)：</span>
						</td>
						<td class="td1" width="70%">
						&nbsp;<select id="send" name="model.send" style="width:40%">
						<%						
							List<TempPo> qfList = (List)request.getAttribute("QF");
							for(TempPo qf : qfList){
								if(sdr.getSend()!=null && sdr.getSend().equals(qf.getName())){
								%>
								<option value="<%=qf.getName() %>" selected="selected"><%=qf.getName() %></option>
								<%
								}else{
								%>
								<option value="<%=qf.getName() %>"><%=qf.getName() %></option>
								<%
								}
							}
						%>
						</select>
						</td>
					</tr>
					<tr>
						<td class="biao_bg1" width="20%" align="right">
							<span class="wz">签发日期(<font color=red>*</font>)：</span>
						</td>
						<td class="td1" width="70%">
						&nbsp;<strong:newdate name="model.sendTime" id="sendTime"  dateform="yyyy-MM-dd HH:mm:ss" dateobj="${model.sendTime}"
							width="200px" skin="whyGreen" isicon="true"></strong:newdate>
						</td>
					</tr>
					<tr>
						<td class="biao_bg1" width="20%" align="right">
							<span class="wz">主办处室(<font color=red>*</font>)：</span>
						</td>
						<td class="td1" width="70%">
						&nbsp;<select id="room" name="room" onchange="setToRoomName();" style="width:40%">
							<%
								List<TempPo> orgList = (List)request.getAttribute("orgList");
								for(TempPo tempPo : orgList){
									String idAndName = tempPo.getId() + ";" + tempPo.getName();
									if(sdr.getToRoom()!=null && sdr.getToRoom().equals(tempPo.getId())){
									%>
									<option value="<%=idAndName%>" selected="selected"><%=tempPo.getName()%></option>
									<%
									}else{
									%>
									<option value="<%=idAndName%>"><%=tempPo.getName()%></option>
									<%
									}													
								}
							%>
						</select>
						<input id="toRoomName" name="model.toRoomName" type="hidden" value="${model.toRoomName }" size="45" maxlength="45" readonly="readonly">
						<input id="toRoom" name="model.toRoom" type="hidden" value="${model.toRoom }">
						</td>
					</tr>
					<tr>
						<td class="biao_bg1" width="20%" align="right">
							<span class="wz">公文编号：</span>
						</td>
						<td class="td1" width="70%">
						&nbsp;<input id="docCode" name="model.docCode" type="text" value="${model.docCode }" size="24" maxlength="24">
						&nbsp;<input type='button' class='input_bg' onclick="selectOrgFromDict('FWDJH','docCode');" value="选 择"/>
						</td>
					</tr>
					<tr>
						<td class="biao_bg1" width="20%" align="right">
							<span class="wz">密级：</span>
						</td>
						<td class="td1" width="70%">
						&nbsp;<select id="secret" name="model.secret" style="width:40%">
								<option></option>
						<%
							List<TempPo> mmdjList = (List)request.getAttribute("MMDJ");
							for(TempPo mmdj : mmdjList){
								if(sdr.getSecret()!=null && sdr.getSecret().equals(mmdj.getName())){
								%>
								<option value="<%=mmdj.getName() %>" selected="selected"><%=mmdj.getName() %></option>
								<%
								}else{
								%>
								<option value="<%=mmdj.getName() %>"><%=mmdj.getName() %></option>
								<%
								}
							}
						%>
						</select>
						</td>
					</tr>					
				</table>
			</td>
		</tr>
	</table>
  </s:form>
  </DIV>
  </body>
</html>