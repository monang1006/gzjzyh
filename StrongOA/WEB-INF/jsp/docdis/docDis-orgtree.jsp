<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="/tags/web-tree" prefix="tree"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<%@include file="/common/include/rootPath.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" >
<html>
	<head>
		<%@include file="/common/include/meta.jsp"%>
		<LINK href="<%=frameroot%>/css/properties_windows.css" type=text/css
			rel=stylesheet>
		<LINK href="<%=path%>/common/css/treeview.css" type=text/css
			rel=stylesheet>
		<script src="<%=path%>/common/js/jquery/jquery-1.2.6.js"
			type="text/javascript"></script>
		<script src="<%=path%>/common/js/common/common.js"
			type="text/javascript"></script>
		<SCRIPT src="<%=root%>/common/js/mztree_check/mztreeview_check.js"></SCRIPT>
		<title>机构树形结构</title>
		<script type="text/javascript">
			window.name="MyModalDialog";
			function getCheckId(){
		        var chkTreeNode=document.getElementsByName("chkTreeNode");	//得到选择框对象
		        var checkid;
		        checkid='';
		        for(var i=0;i<chkTreeNode.length;i++){						//获取被选中的值
					if(chkTreeNode[i].checked==true){
						checkid=checkid+chkTreeNode[i].value+",";			//得到被选中的行的编号
					}
				}
				if(checkid!=''){
					checkid = checkid.substring(0,checkid.length-1);
				}
				return checkid;
			}
			
			function getCheckName(){
		        var chkTreeNode=document.getElementsByName("chkTreeNode");	//得到选择框对象
		        var checkvalue,checkid;
		        checkvalue='';
		        for(var i=0;i<chkTreeNode.length;i++){						//获取被选中的值
					if(chkTreeNode[i].checked==true){
						checkvalue=checkvalue+chkTreeNode[i].nextSibling.nextSibling.innerHTML+",";
					}
				}
				if(checkvalue!=''){
					checkvalue = checkvalue.substring(0,checkvalue.length-1);
				}
				return checkvalue;
			}
			
			function doSelect(){
				var ids = getCheckId();
				var names = getCheckName();
				var distributeType="${distributeType}";
				if(ids!=""&&names!=""){
					$.ajax({
					  		type:"post",
					  		dataType:"text",
					  		url:"<%=root%>/docdis/docDis!beginDocDis.action",
					  		data:"docId=${docId}&ids="+ids+"&names="+names+"&distributeType="+distributeType,
					  		success:function(msg){
								if(msg=="true"){
 									//window.parent.location.href="<%=root%>/docDis/docDis.action";
							        window.parent.location.reload();
									window.returnValue="true";
									window.close();
								}else{
									alert("文档分发失败，请您再次操作重新分发！");
								}
					  		}
					 });
					}else{
						alert("分发失败,请您重新进行分发！");
					}
			}
			
			function doCancel(){
				window.close();
			}
			
			function showTree(value){
				document.all.distributeType.value=value;
				document.all.comform.submit();
			}
			
			function showTreeByContion(value1,value2){
				document.all.distributeType.value=value1;
				document.all.showType.value=value2;
				document.all.comform.submit();
			}
			
			function setCondition(value1,value2){
				var url= scriptroot+'/historyquery/query!initialQuery.action?module=setCon';
				var returnValue=OpenWindow(url,480,350,window);	
				if(returnValue==1){
					document.all.distributeType.value=value1;
					document.all.showType.value=value2;
					document.all.comform.submit();
				}
			}
		</script>
	</head>
	<base target="_self" />
	<body oncontextmenu="return false;" id="contentbodymargin">
		<div id="contentborder" align="center">
			<form action="<%=root%>/docdis/docDis!orgTree.action" method="post" name="comform" target="MyModalDialog">
				<input type="hidden" id="type" name="type" value="${type}"> 
				<input type="hidden" id="docId" name="docId" value="${docId}"> 
				<input type="hidden" id="distributeType" name="distributeType"> 
				<input type="hidden" id="showType" name="showType"> 
				<table width="85%" border="0" cellspacing="0" cellpadding="00">
					<tr>
						<td align="center" valign="middle">
							<table width="27%" border="0" cellspacing="0" cellpadding="00">
								<tr>
									<td align="center">
										<input type="button" class="input_bg" onclick="doSelect();"
											value="确定" />
									</td><td>	
										<input type="button" class="input_bg" onclick="doCancel();"
											value="取消" />
									</td>
								</tr>
							</table>
						</td>
					</tr>
				</table>
				<br>
				<!-- 
				<table width="100%" border="0" cellpadding="0" cellspacing="1"
					class="table1">
					<tr>
						<td width="25%" height="21" class="biao_bg1" align="right">
							分发方式：
						</td>
						<td class="td1">
							<input type="radio" id="org" name="tree" value="companyTree"
								onclick="showTree(this.value)">
							外部分发
							<input type="radio" id="user" name="tree" value="orgAndUserTree"
								onclick="showTree(this.value)">
							内部分发
						</td>
					</tr>
					<table>
				 -->
				</form>
				<table width="100%" border="0" cellpadding="0" cellspacing="1"
					class="table1">
					<tr>
						<script>
							var distributeType="${distributeType}";
							var showType="${showType}";
							if(distributeType=="orgAndUserTree"){
								document.write("<td class='td1'>");
								document.write("<input type='radio' id='orgTree' name='showTypes' value='orgs' onclick='showTree(\"orgAndUserTree\",this.value)'> 按机构");
								document.write("&nbsp;&nbsp;&nbsp;&nbsp;<input type='radio' id='userTree' name='showTypes' value='users' onclick='showTreeByContion(\"orgAndUserTree\",this.value)'> 按条件");	
								if(showType=="users"){
									document.write("&nbsp;&nbsp;&nbsp;&nbsp; <input type='button' id='setCon' name='setCon' onclick='setCondition(\"orgAndUserTree\",\""+showType+"\")' value='设置条件'>");
								}
								document.write("</td>");
							}
						</script>
					</tr>
					<tr>
						<td class="td1">
							<s:if test="distributeType!=null&&distributeType==\"orgAndUserTree\"">
								<tree:strongtree title="机构人员" check="true"
								chooseType="chooseOne"
								dealclass="com.strongit.oa.docdis.common.DocDisUserTree"
								data="${userList}" />
							</s:if>
							<s:else>
								<tree:strongtree title="组织机构" check="true"
									chooseType="chooseOne"
									dealclass="com.strongit.oa.docdis.common.DocDisOrgTree"
									data="${list}" />
							</s:else>
						</td>
					</tr>
				</table>
				<br>
				<table width="85%" border="0" cellspacing="0" cellpadding="00">
					<tr>
						<td align="center" valign="middle">
							<table width="27%" border="0" cellspacing="0" cellpadding="00">
								<tr>
									<td align="center">
										<input type="button" class="input_bg" onclick="doSelect();"
											value="确定" />
											</td><td>	
										<input type="button" class="input_bg" onclick="doCancel();"
											value="取消" />
									</td>
								</tr>
							</table>
						</td>
					</tr>
				</table>
	</body>
	<SCRIPT type="text/javascript">
	</SCRIPT>
</html>
