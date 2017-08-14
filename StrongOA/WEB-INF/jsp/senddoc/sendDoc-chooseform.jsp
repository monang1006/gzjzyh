<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib uri="/struts-tags" prefix="s" %>
<%@include file="/common/include/rootPath.jsp"%>
<html>
  <head>
    <%@include file="/common/include/meta.jsp" %>
    <title>选择表单</title>
    <link
      href="<%=frameroot%>/css/properties_windows.css" type="text/css" rel="stylesheet">
    <script src="<%=path%>/common/js/jquery/jquery-1.2.6.js"
      type="text/javascript"></script>
    <script type="text/javascript">      
      		/*
             * 获得催办类型
             */
            function getRemindValue() {
            	var id = $("input[@type=radio][@checked]").val();
            	if(id!=null && id!=''){
            		 window.returnValue=id;
                	 window.close();
            	}else{
            		alert("请选择发文处理单！");
            	}
               
            }     
      
         $(document).ready(function(){
         	var size = $("input:radio").size();
         	if(size == 1){
         		var id = $("input:radio").val();
            	if(id!=null && id!=''){
            		 window.returnValue=id;
                	 window.close();
            	}
         	}
         });
      
	      function cancel() {
	          window.returnValue="";
	          window.close();
	      }
      
        //增加列单击事件,方便勾选操作.
        function doSelect(id){
        	document.getElementById(id).checked = true;
        }
     
    </script>
  </head>
  <base target="_self">
  <body class="contentbodymargin">
  	<DIV id=contentborder align=center>
  		<table width="100%" border="0" cellspacing="0" cellpadding="0"
				style="vertical-align: top;">
				<tr>
					<td height="100%">
						<form id="rendForm" theme="simple"
							action="" method="POST">
							<table height="30" width="100%" border="0" cellspacing="0"
								cellpadding="0">
								<tr>
									<td
										style="FILTER:progid:DXImageTransform.Microsoft.Gradient(gradientType=0,startColorStr=#ededed,endColorStr=#ffffff);">
										<table width="100%" border="0" cellspacing="0"
											cellpadding="00">
											<tr>
												<td>&nbsp;</td>
												<td width="50%">
						  							<img src="<%=frameroot%>/images/ico/ico.gif" width="9" height="9"alt="">&nbsp;
													请选择表单
												</td>
												<td width="10%">
													&nbsp;
												</td>
												<td width="35%">
												</td>
											</tr>
										</table>
									</td>
								</tr>
							</table>
							<table width="100%" height="10%" border="0" cellpadding="0"
								cellspacing="1" align="center" class="table1">
								
									<s:if test="formListbyWFType!=null && formListbyWFType.size()>0">
									<s:iterator value="formListbyWFType">
									<tr>	
										<td width="30%" height="30" class="biao_bg1" align="left" onclick="doSelect('<s:property value='id'/>');">
												<input id="<s:property value='id'/>" type="radio" name="form" value="<s:property value='id'/>"/>
												<span style="cursor: hand;">
												<s:property value="title"/></span>
										</td>
									</tr>
									</s:iterator>
									</s:if>
									<s:else>
										<tr>
										<td width="30%" height="51" class="biao_bg1" align="left">
										 <font color="red">没有可选表单</font>
										</td>
										</tr>
									</s:else>
								

							</table>
							<br>
							<table id="annex" width="90%" height="10%" border="0"
								cellpadding="0" cellspacing="1" align="center" class="table1">
							</table>

							<table width="90%" border="0" cellspacing="0" cellpadding="00">
								<tr>
									<td align="center" valign="middle">
										<table width="50%" border="0" cellspacing="0" cellpadding="00">
											<tr>
												<td width="33%">
													<input id="sb" type="button" class="input_bg" value="确 定" onclick="getRemindValue();">
												</td>
												<td width="33%">
													<input name="Submit2" type="button" class="input_bg"
														value="关 闭" onclick="cancel();">
												</td>
											</tr>
										</table>
									</td>
								</tr>
							</table>
						</form>
					</td>
				</tr>
			</table>
  	</DIV>
  </body>
</html>