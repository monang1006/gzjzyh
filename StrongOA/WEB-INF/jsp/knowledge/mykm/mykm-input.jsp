<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="/tags/web-newdate" prefix="strong"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@include file="/common/include/rootPath.jsp"%>
<%@ taglib uri="/tags/security" prefix="security"%>
<html>
	<head>
		<%@include file="/common/include/meta.jsp"%>
		<title>知识收藏</title>
		<LINK href="<%=frameroot%>/css/properties_windows_add.css" type=text/css rel=stylesheet>
		<script src="<%=path%>/common/js/jquery/jquery-1.2.6.js" type="text/javascript" language="javascript"></script>
		<script src="<%=path%>/common/js/upload/jquery.MultiFile.js" type="text/javascript" language="javascript"></script>
		<script src="<%=path%>/common/js/upload/jquery.blockUI.js" type="text/javascript" language="javascript"></script>
		<script src="<%=path%>/common/js/validate/jquery.validate.js" type="text/javascript" language="javascript"></script>
		<script src="<%=path%>/common/js/validate/formvalidate.js" type="text/javascript" language="javascript"></script>
		<script language="JavaScript" src="<%=path%>/common/js/commontab/service.js"></script>
		<script language="JavaScript" src="<%=path%>/common/js/commontab/workservice.js"></script>
		<SCRIPT>
			function addsort(){//添加知识分类
				var audit= window.showModalDialog("<%=root%>/knowledge/mykmsort/mykmSort!input.action",window,'help:no;status:no;scroll:no;dialogWidth:450px; dialogHeight:200px');
				if(audit!=undefined && audit!=null && audit!=""){
					var result = audit.split(",");
					var id = result[0];
					var name = result[1];
					var option = "<option value='"+id+"' selected='selected'>"+name+"</option>";
					$("#mykmIssortId").append(option);
				}
			}
		
		function onSub(){//表单提交
		  if($("#mykmName").val()==null|| $.trim($("#mykmName").val())=="")
		  {
		    alert("知识标题不可以为空！");
		    return  false;
		  }
		  if($("#mykmDesc").val().length>=200){
		    return;
		  }
		 var url= $("#mykmUrl").val();
		 var url1=$("#mykmUrl1").val();
		 var id=$("#mykmId").val();
		 if((id==null||id=="")&&(url1==null||url1=="")){
		    if(url.substring(0,7)!="http://")
		    {
		    alert("连接路径必须以http://开头");
		    return false;
		    }
		    if(url.substring(8)==null||url.substring(8)=="")
		    {
		    alert("http://后面请写入正确的路径地址");
		    return false;
		    }
         }
		  $("#mykmUrl1").val(url);
		  mykmForm.submit();
		  returnValue="reload";
		 
		}
		//关闭窗体
		function col(){
		window.close();
		}
		
		
			function show(value){
			  $("#"+value).attr("selected",true);
			}
 		</SCRIPT>
	</head>
	<base target="_self"/>
	<body class=contentbodymargin onload="show('${model.mykmIssortId }')" oncontextmenu="return false;">
		<script src="<%=path%>/common/js/newdate/WdatePicker.js" type="text/javascript"></script>
		<DIV id=contentborder align=center>
		<s:form id="mykmForm" theme="simple"  action="/knowledge/mykm/mykm!save.action"  method="post">
				<input id="mykmId"  name="model.mykmId" type="hidden"
								size="32" value="${model.mykmId}">	
							<input id="mykmUser" name="model.mykmUser" type="hidden"
								size="32" value="${model.mykmUser}">	
                            <input id="mykmUrl1" name="mykmUrl" type="hidden" value="${model.mykmUrl}">
                            <input type="hidden" name="type" value="${type }">
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
								<strong>保存收藏</strong>
							</td>
							<td align="right">
								<table border="0" align="right" cellpadding="00" cellspacing="0">
					                <tr>
					                 	<td width="7"><img src="<%=frameroot%>/images/ch_h_l.gif"/></td>
					                 	<td class="Operation_input" onclick="onSub();">&nbsp;保&nbsp;存&nbsp;</td>
					                 	<td width="7"><img src="<%=frameroot%>/images/ch_h_r.gif"/></td>
				                  		<td width="5"></td>
					                 	<td width="8"><img src="<%=frameroot%>/images/ch_z_l.gif"/></td>
					                 	<td class="Operation_input1" onclick="col();">&nbsp;取&nbsp;消&nbsp;</td>
					                 	<td width="7"><img src="<%=frameroot%>/images/ch_z_r.gif"/></td>
				                  		<td width="6"></td>
					                </tr>
					            </table>
							</td>
						</tr>
					</table>
				</td>
				</tr>
				<tr>
					<td>
							<table width="100%" height="10%" border="0" cellpadding="0" cellspacing="1" align="center" class="table1">
								<tr>
									<td height="21" class="biao_bg1" align="right">
										<span class="wz"><font color="red">*</font>&nbsp;知识标题：&nbsp;</span>								</td>
									<td  colspan="3" align="left" class="td1">
									<input id="mykmName" maxlength="25" name="model.mykmName" type="text" size="48" value="${model.mykmName}" >&nbsp;
								  </td>
								</tr>
								
								<tr>
									<td  height="21" class="biao_bg1" align="right">
										<span class="wz">知识来源：&nbsp;</span>										</td>
									<td class="td1" colspan="3" align="left">
										<input id="mykmSource" name="model.mykmSource" maxlength="30" type="text" size="48" value="${model.mykmSource}" > 

									</td>
								</tr>
								<tr>
									<td  height="21" class="biao_bg1" align="right">
										<span class="wz">作&nbsp;&nbsp;&nbsp;&nbsp;者：&nbsp;</span>							</td>
									<td class="td1" colspan="3" align="left">
									
							<input id="mykmAuthor" name="model.mykmAuthor" maxlength="20" size="48" value="${model.mykmAuthor}" >
									</td>
								</tr>
								<tr>
									<td  height="21" class="biao_bg1" align="right">
										<span class="wz"><font color="red">*</font>&nbsp;连接路径：&nbsp;</span>								</td>
									<td class="td1" colspan="3" align="left">
									<s:if test="model.mykmUrl!=null&&model.mykmUrl!=''">
										<input id="mykmUrl" disabled="disabled" name="model.mykmUrl"  size="48" value="${model.mykmUrl}" >
										</s:if>
										<s:else>
										<input id="mykmUrl"  name="model.mykmUrl" maxlength="100"  size="48" value="http://" >
										</s:else>
									</td>
								</tr>
							<tr>
									<td  height="21" class="biao_bg1" align="right">
										<span class="wz"><font color="red">*</font>&nbsp;知识分类：&nbsp;</span>								</td>
									<td class="td1" colspan="3" align="left">
					                   <select id="mykmIssortId" name="model.mykmIssortId" lang="50" style="width:200px;">
					                   <s:iterator id="vo" value="sortList">
										<option id="${vo.mykmSortId }" value="${vo.mykmSortId }">${vo.mykmSortName }</option>
										</s:iterator>
										</select>
										<a  href="#" class="button" onclick="addsort()">添加分类</a>
									</td>
								</tr>
								<tr>
									<td height="21" class="biao_bg1" valign="top" align="right">
										<span class="wz">描述：&nbsp;</span>
									</td>
									<td class="td1" colspan="3" align="left">
										
									<textarea  id="mykmDesc" rows="5" cols="35"   name="model.mykmDesc" maxlength="200"  class="required">${model.mykmDesc}</textarea>
									</td>
								</tr>
							</table>
							</td>
						</tr>
						<tr>
							<td class="table1_td"></td>
							<td></td>
						</tr>
			</table>
	  </td>
	</tr>
	</table>	
		
	</s:form>
	</DIV>
	</body>
</html>