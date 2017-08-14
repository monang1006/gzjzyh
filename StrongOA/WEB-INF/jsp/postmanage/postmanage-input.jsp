<%@ page language="java" contentType="text/html; charset=UTF-8"	pageEncoding="UTF-8"%>
<%@	include file="/common/include/rootPath.jsp"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<html>
<head>
<%@include file="/common/include/meta.jsp"%>
<script type="text/javascript" src="<%=root %>/common/js/jquery/jquery-1.2.6.js"></script>
<script src="<%=path%>/common/js/common/common.js" type="text/javascript"></script>	


<base target="_self">
	<title>
		<s:if test="boWholepost.postId != null && boWholepost.postId !=''">
			编辑机构岗位
		</s:if>
		<s:else>
			新建机构岗位
		</s:else>
	</title>
	<LINK href="<%=frameroot%>/css/properties_windows_add.css" type=text/css rel=stylesheet>
		<script language="javascript">
		var numtest = /^\d+$/; 
		String.prototype.trim = function() {
				var strTrim = this.replace(/(^\s*)|(\s*$)/g, "");
				strTrim = strTrim.replace(/^[\s　\t]+|[\s　\t]+$/, ""); 
			    return strTrim;
			}		
		
		function formsubmit(){
		var id = document.getElementById("wpName").value;
		if($.trim(id)==null||$.trim(id)=="" ){
			alert("请输入岗位名称!");
			document.getElementById("wpName").focus();
			return;
		}else if($.trim(id)=="null"){
		   alert("岗位名称不能为null。");
			document.getElementById("wpName").focus();
			return;
		}
		
		 var sequence=document.getElementById("postSequence").value;
    if(!numtest.test(sequence)){
		 alert('排序号必须为整数');
		 document.getElementById("postSequence").focus();
		 return;
	}
	 if(sequence.length>10){
	   	   alert('排序序号不能超过10位数！！！');
		      document.getElementById("postSequence").focus();
		        return;
	   }
		
		var descript = document.getElementById("wpDescription").value;
		if(descript.length > 200){
			alert("岗位描述字数不能大于200!");
			return;
		}
		document.getElementById("wpDescription").value = descript.trim();
		document.forms[0].submit();
		}
	</script>
</head>
<body class=contentbodymargin oncontextmenu="return false;">
<DIV id=contentborder align=center>
<s:form id="mytable" action="/postmanage/postContent!save.action" method="POST" theme="simple">
    <input type="hidden" id="wpId" name="boWholepost.postId" value="${boWholepost.postId}">
    <table width="100%" border="0" cellspacing="0" cellpadding="0" style="vertical-align: top;">
  <tr>
    <td height="100%">
    <table width="100%" border="0" cellspacing="0" cellpadding="0">
      <tr>
        <td colspan="3" class="table_headtd">
        <table width="100%" border="0" cellspacing="0" cellpadding="00">
          <tr>
            <td class="table_headtd_img" >
				<img src="<%=frameroot%>/images/ico/ico.gif" >&nbsp;
			</td>
            <td align="left">
            	<script>
				var id = "${boWholepost.postId}";
				if(id==null|id==""){
					window.document.write("<strong>新建机构岗位</strong>");
				}else{
					window.document.write("<strong>编辑机构岗位</strong>");
				}
				</script>
            </td>
            <td align="right">
            	<table border="0" align="right" cellpadding="00" cellspacing="0">
	                <tr>
	                 	<td width="7"><img src="<%=frameroot%>/images/ch_h_l.gif"/></td>
	                 	<td class="Operation_input" onclick="formsubmit();">&nbsp;保&nbsp;存&nbsp;</td>
	                 	<td width="7"><img src="<%=frameroot%>/images/ch_h_r.gif"/></td>
                  		<td width="5"></td>
	                 	<td width="8"><img src="<%=frameroot%>/images/ch_z_l.gif"/></td>
	                 	<td class="Operation_input1" onclick="window.close()">&nbsp;关&nbsp;闭&nbsp;</td>
	                 	<td width="7"><img src="<%=frameroot%>/images/ch_z_r.gif"/></td>
                  		<td width="6"></td>
	                </tr>
	            </table>
            </td>
          </tr>
        </table>
        </td>
      </tr>
    </table>
			<table width="100%" height="10%" border="0" cellpadding="0"
				cellspacing="1" align="center" class="table1">
				<tr>
					<td width="40%" height="21" class="biao_bg1" align="right">
						<span class="wz"><font color="red">*</font>&nbsp;岗位名称：</span>
					</td>
					<td class="td1" colspan="3" align="left">
						<input id="wpName" name="boWholepost.postName" type="text" maxLength="50" size="22" value="${boWholepost.postName}" >
					</td>
				</tr>
				<tr>
					<td width="40%" height="21" class="biao_bg1" align="right">
						<span class="wz"><font color="red">*</font>&nbsp;排序序号：</span>
					</td>
					<td class="td1" colspan="3" align="left">
						<input id="postSequence" name="boWholepost.postSequence" type="text"
							maxlength="10" size="22" value="${boWholepost.postSequence}">
					</td>
				</tr>
				<tr>
				    <td width="40%" height="21" class="biao_bg1" align="right">
				        <span class="wz">关联所有组织机构：</span>
				    </td>
				    <td class="td1" colspan="3" align="left">
				        <%--设置是否可编辑全局岗位--%>
				        <s:set name="postIsoverall" value="%{boWholepost.postIsoverall}"></s:set>
				        <s:set name="overall" value="1"></s:set>
				        <s:if test="#postIsoverall == #overall" >
				              <span class="wz">自动关联</span>&nbsp;
							  <font color="#999999">设置为自动关联后不能再更改</font>
    							<input type="hidden" id="isOrg" name="boWholepost.postIsoverall" value="1">
				        </s:if><s:else>
				            <s:select id="isOrg" name="boWholepost.postIsoverall"
				                      list="#{'0':'手动关联','1':'自动关联'}"
				                      listKey="key" listValue="value" />&nbsp;
							  <font color="#999999">设置为自动关联后不能再更改</font>
				        </s:else>
				
				    </td>
				</tr>
								
				<tr>
					<td width="40%" height="21" class="biao_bg1" align="right" valign="top">
						<span class="wz">岗位说明：</span>
					</td>
					<td class="td1" colspan="3" align="left">
						<textarea id="wpDescription" name="boWholepost.postDescription" value="${boWholepost.postDescription}" rows="6" cols="30">${boWholepost.postDescription}</textarea>
					</td>
				</tr>
				<tr>
					<td class="table1_td"></td>
					<td></td>
				</tr>
			</table>
			<table id="annex" width="90%" height="10%" border="0" cellpadding="0" cellspacing="1" align="center" class="table1">
			</table>			
			<table width="90%" border="0" cellspacing="0" cellpadding="00">
				<tr>
					<td align="center" valign="middle">
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
