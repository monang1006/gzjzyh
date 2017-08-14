<%@ page contentType="text/html; charset=utf-8"%>
<%@taglib prefix="s" uri="/struts-tags"%>
<%@ taglib uri="/WEB-INF/tlds/c.tld" prefix="c"%>
<%@include file="/common/include/rootPath.jsp"%>
<html>
	<head>
		<title>人员关联信息</title>
		<%@include file="/common/include/meta.jsp"%>
		<link type="text/css" rel="stylesheet" href="<%=path%>/oa/css/personnel/windows.css">
		<LINK href="<%=frameroot%>/css/properties_windows.css" type=text/css rel=stylesheet>
		<link rel="stylesheet" type="text/css" href="<%=path%>/oa/css/personnel/skin.css">
		<SCRIPT language="javascript" src="<%=path%>/oa/js/personnel/tab.js"></SCRIPT>
		<SCRIPT language="javascript" src="<%=path%>/oa/js/personnel/inching.js"></SCRIPT>
		<script language="JavaScript">     
			//用javascript去字符串左右空格,包括全角和半角
		   	String.prototype.trim = function() {
					var strTrim = this.replace(/(^\s*)|(\s*$)/g, "");
					strTrim = strTrim.replace(/^[\s　\t]+|[\s　\t]+$/, ""); 
				    return strTrim;
				}
		</script>
		<style>
			TBODY {
				HEIGHT: 0px
			}	
			
			#contentborder {
			    PADDING-RIGHT: 0px;
				PADDING-LEFT: 0px;
				PADDING-BOTTOM: 0px;
			}
			.contentbodymargin {
				  BACKGROUND:#FFFFFF; 
				 MARGIN: 0px 0px 0px
			}
			
		</style>
	</head>
	<base target="_self"> 
	<body class="contentbodymargin" oncontextmenu="return false;">
		<form  id="newForm" action="" method="post"></form>
		<input type="hidden" name="orgId" id="orgId" value="${orgId}">
		<INPUT type="hidden" name="personId">
		<center style="margin-top:0px;width: 100%;height: 100%">
		<s:if test="#request.tableList!=null&&#request.tableList.size()>0">
			<INPUT type="button" class="input_bg" value="新增人员" onclick="addnewPerson()">
			<span id="newInfoAdd"></span>
			<INPUT type="button" class="input_bg" value="关  闭" onclick="window.close();">
				<table id="companytab" width="100%" height="100%" >
					<tr>
						<td width="100%" height="100%">	
							<div id="linediv" style="overflow-x:hidden;display:none;width:100%;height:22">
								<table cellpadding="0" cellspacing="0" onselectstart="return false" class="tabcss" width="100%" height="100%">
									<tr>
										<s:iterator id="ite" value="#request.tableList" status="status"> 
											<td valign="bottom" style="position:relative;left:0;display:inline;" nowrap>
												<table cellspacing="0" cellpadding="0">
													<tr>
														<td width="1" height="1"></td>
														<td width="1" height="1"></td>
														<td class="tabbgcolor" nowrap></td>
														<td></td>
														<td></td>
													</tr>
													<tr>
														<td width="1" height="1"></td>
														<td width="1" height="1" class="tabbgcolor" nowrap></td>
														<td></td>
														<td class="tabbgcolor" nowrap></td>
														<td></td>
													</tr>
													<tr>
														<td width="1" class="tabbgcolor" nowrap></td>
														<td width="1" height="1"></td>
														<td height="22" class="titletop" nowrap>
															<span><s:property value="infoSetName"/></span>
														</td>
														<td width="1" bgcolor="#FFFFFF" nowrap></td>
														<td class="tabbgcolor" width="1" nowrap></td>
													</tr>
													<tr>
														<td bgcolor="#FFFFFF" nowrap></td>		
														<s:if test="#status.index ==\"0\"">
															<td colspan="4" height="1" bgcolor="white" nowrap></td>
													    </s:if>
													    <s:else>
															<td colspan="4" height="1" class="tabbgcolor" nowrap></td>
														</s:else>
													</tr>
												</table>									
											</td>
										</s:iterator>
										<td id="linetd" nowrap>
											&nbsp;
										</td>
										<td id="linetd2" valign="top" width="32" nowrap style="display:none;position:absolute;top:1;">
											<table cellspacing="0" cellpadding="0">
												<tr>
													<td height=19 width=32 nowrap>
														<button id="leftbutton" hideFocus="true" UNSELECTABLE="on" style="font-family:webdings;height:16;width:16;font-size:10;cursor:hand;" onclick="companytab.moveRight();">
															<span style="position:relative;top:-4;left:0;">3</span>
														</button>
														<button id="rightbutton" hideFocus="true" UNSELECTABLE="on" style="font-family:webdings;height:16;width:16;font-size:10;cursor:hand;" onclick="companytab.moveLeft();">
															<span style="position:relative;top:-4;left:-1;">4</span>
														</button>
													</td>
												</tr>
											</table>
										</td>
									</tr>
								</table>
							</div>
							<div id="cardcsstop" class="cardcssbottom" style="overflow:auto;width:100%;height:95%;margin: 0px;padding: 1px">
									<s:iterator id="ite2" value="#request.tableList" status="status">
											<s:if test="#status.index ==\"0\"">
												<div id="<s:property value="infoSetValue"/>" style="width:100%;height:100%;padding: 0;margin: 0px;" readonly="0"  changeLogo="changed"  cardnum="<s:property value='#status.index+1'/>">
													<IFRAME id="<s:property value="infoSetCode"/>" src="" scrolling="no" frameborder="0" width="100%" height="95%" marginwidth="0" marginheight="0"></IFRAME>
													<script language="JavaScript"> 	
														var readonly="0";
														var orgId=document.getElementById("orgId").value;	
														document.getElementById("<s:property value='infoSetCode'/>").src="<%=path%>/personnel/baseperson/person!initViewAddPerson.action?infoSetCode=<s:property value='infoSetCode'/>&orgId="+orgId+"&readonly="+readonly;										
													</script>
												</div>
									      	</s:if>
									      	<s:else>
												<div id="<s:property value='infoSetValue'/>" style="display:none" readonly="0" changeLogo="changed" cardnum="<s:property value='#status.index+1'/>">
													<IFRAME id="<s:property value='infoSetCode'/>" src="" scrolling="no" frameborder="0" width="100%" height="95%" marginwidth="0" marginheight="0"></IFRAME>
												</div>
											</s:else>
									</s:iterator>
							</div>
						</td>
					</tr>
				</table>	
				<script language="JavaScript"> 
					var companytab=new Tab(); 
					document.attachEvent("onreadystatechange",companytab_init); 
					function companytab_init(){ 
						if(document.readyState=="complete") 
							companytab.initTab("companytab",-1); 
							
					} 
					function nextCard(){
						companytab.nextCards();
					}
					
					function setVisible(cardId,visible){
						companytab.setVisible(cardId,visible);
					}
					
					function doCardClick1(card){
						var tableName=card.id;	
						id=card.children[0].id;			
						var orgId=document.all.orgId.value;
						var readonly=card.readonly;
		    			var personId=document.all.personId.value;
						if((personId==null||personId=='null'||personId=='')&&tableName!='T_OA_BASE_PERSON'){
							alert("请先添加人员！");
						}else{
							card.children[0].src="<%=path%>/personnel/baseperson/person!initViewAddPerson.action?infoSetValue="+tableName+"&orgId="+orgId+"&personId="+personId+"&readonly="+readonly;						  	
						}
					}
					function addnewInfo(){
						doCardClick1(companytab.getCurrentCard());
					}
					function addnewPerson(){
						var orgId=document.all.orgId.value;
						document.getElementById("newForm").action="<%=path%>/personnel/baseperson/person!initViewAddTool.action?orgId="+orgId;	
						document.getElementById("newForm").submit();
					}	
					
					function setPersonId(value){
						document.all.personId.value=value;
					}
					
					function getPersonId(){
						return document.all.personId.value;
					}
					
					function getOrgId(){
						return document.all.orgId.value;
					}
				</script>
			</s:if>
			<s:else>
				<script language="JavaScript"> 
						document.write("<br><br>人员暂时没有其他关联信息");
				</script>
			</s:else>
			</center>	
	</body>
</html>
