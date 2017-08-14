<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@include file="/common/include/rootPath.jsp"%>
<%@taglib uri="/tags/web-newdate" prefix="strong"%>
<%@taglib prefix="s" uri="/struts-tags"%>
<html>
	<head>
	<%@include file="/common/include/meta.jsp"%>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<title>培训信息管理</title>
		<LINK href="<%=frameroot%>/css/properties_windows.css" type=text/css
			rel=stylesheet>
			   <script type="text/javascript"
      src="<%=path%>/common/js/newdate/WdatePicker.js"></script>
      	<script src="<%=path%>/common/js/jquery/jquery-1.2.6.js" type="text/javascript"></script>
      <script language="javascript" src="<%=path %>/common/js/upload/jquery.MultiFile.js"></script>
      	<script src="<%=path%>/common/js/upload/jquery.blockUI.js" type="text/javascript"></script>
      	<script src="<%=path%>/common/js/fckeditor2/fckeditor.js" type="text/javascript" ></script>
      	<script language='javascript'
			src='<%=path%>/common/js/jquery/jquery-1.2.6.js'></script>
			<SCRIPT language="javascript"
			src="<%=request.getContextPath()%>/common/js/menu/menu.js"></SCRIPT>
	
		<LINK href="<%=frameroot%>/css/properties_windows.css" type=text/css
			rel=stylesheet>
		<LINK href="<%=frameroot%>/css/search.css" type=text/css
			rel=stylesheet>
		<script language='javascript'
			src='<%=path%>/common/js/jquery/jquery-1.2.6.js'></script>
	
		<script language="javascript"
			src="<%=path%>/common/js/common/common.js"></script>
		<script>
		

</script>
		<s:head />
		<base target="_self">
	</head>
   <body class=contentbodymargin oncontextmenu="return false;">
				<script type="text/javascript"
			src="<%=path%>/common/js/newdate/WdatePicker.js"></script>
		<!-- 增加form    -->
		<DIV id=contentborder >
		<iframe id="attachDownLoad" src='' style="display:none;border:4px solid #CCCCCC;"></iframe>
		<s:form action="/personnel/trainingmanage/training!save.action" id="trainingform" name="trainingform" method="post" enctype="multipart/form-data">
			<input type="hidden" id="delAttachIds" name="delAttachIds" value="">
			<input type="hidden" id="trainingId" name="model.trainId"
								value="${model.trainId}">
		<table width="100%" border="0" cellspacing="0" cellpadding="00"
									style="FILTER:progid:DXImageTransform.Microsoft.Gradient(gradientType=0,startColorStr=#ededed,endColorStr=#ffffff);">
										<br>
									      <tr>
											<td>&nbsp;</td>
											<td width="12%">
												<img src="<%=frameroot%>/images/ico/ico.gif" width="9"
													height="9">&nbsp;
												查看培训信息
											</td>
											<td width="1%">
												&nbsp;
											</td>
											<td width="87%">				
								
								</td>
							</tr>
						</table>
            <table width="100%">
				<tr>
					<td height="10">	
					  
					</td>
				</tr>
			</table>
			
				<table width="100%" border="0" cellpadding="0" 
				cellspacing="1" class="table1">
				
				<tr>
					<td colspan="1" width="20%" height="21" class="biao_bg1" align="right">
						<span class="wz">培训主题(<font color="#FF6600">*</font>)：&nbsp;</span>
					</td>
					<td colspan="1" width="30%" class="td1" align="left">
				${model.trainTopic}
					</td>
					<td colspan="1"  height="21" class="biao_bg1" align="right" width="20%">
						<span class="wz">培训类型(<font color="#FF6600">*</font>)：&nbsp;</span>
					</td>
					<td colspan="1"  class="td1" align="left" width="30%">
	<s:select list="trainTypeList" listKey="dictItemCode" listValue="dictItemName"  headerKey="" headerValue="请选择培训类型" 
							id="trainType" name="model.trainType" style="width:80%" disabled="true"/>
					</td>
					
				</tr>
            	<tr>
				    <td colspan="1" height="21" class="biao_bg1" align="right">
						<span class="wz">开始日期(<font color="#FF6600">*</font>)：&nbsp;</span>
					</td>
					<td colspan="1" class="td1" align="left">
					<!-- 	${model.trainStartdate}-->
				
						 <strong:newdate name="model.trainStartdate" id="trainStartdate"  width="80%"
                      skin="whyGreen" isicon="true" dateobj="${model.trainStartdate}" dateform="yyyy-MM-dd"></strong:newdate>
                      
					</td>
					<td width="20%" colspan="1" height="21" class="biao_bg1" align="right">
						<span class="wz">结束日期(<font color="#FF6600">*</font>)：&nbsp;</span>
					</td>
					<td  width="30%" colspan="1" class="td1">
					<!-- ${model.trainEnddate}-->
					
					 <strong:newdate name="model.trainEnddate" id="trainEnddate"  width="80%"
                      skin="whyGreen" isicon="true" dateobj="${model.trainEnddate}" dateform="yyyy-MM-dd"></strong:newdate>
                       
					</td>
					
				</tr>
				<tr>
				   
				    <td colspan="1" height="21" class="biao_bg1" align="right" width="20%">
						<span class="wz">主办单位：&nbsp;</span>
					</td>
					<td colspan="1" class="td1" align="left" width="30%">
			${model.trainCommpany}
					</td>	
					<td  height="21" class="biao_bg1" align="right" width="20%">
						<span class="wz">费用(元)：&nbsp;</span>
					</td>
					<td   class="td1" align="left" width="30%">
			${model.trainMoney}
					</td>
					
				</tr>
				
				<tr>
					<td height="21" class="biao_bg1" align="right">
						<span class="wz">培训人员(<font color="#FF6600">*</font>)：&nbsp;</span><br>
					</td>
					<td class="td1" colspan="3" align="left">
						<textarea  id="persons" name="persons" readonly
							             style="width:92.5%;height:150px;" >${personsName}</textarea>
				    </td>
				</tr>
				<tr>
					<td height="21" class="biao_bg1" align="right">
						<span class="wz">备注：&nbsp;</span><br>
						<font color="#FF6600">不可超过232个字符</font>
					</td>
					<td class="td1" colspan="3" align="left">
							${model.trainDemo}
				    </td>
				</tr>
				<tr>
						<td height="21" class="biao_bg1" align="right">
							<span class="wz">附    件&nbsp;&nbsp;&nbsp;：</span>
						</td>
						<td class="td1" colspan="3" align="left">
							
								${attachFiles}	
						</td>
					</tr>	
				
			</table><br>
			<table width="100%">
			<tr>
					<td class="td1" colspan="4" align="center" height="21">
					   
					    &nbsp; &nbsp; &nbsp; 
						<input name="Submit2" type="button" class="input_bg" value="关  闭"
							onclick="window.close();">
					</td>
				</tr>
			</table>
		
		</s:form>
		</DIV>
		<SCRIPT type="text/javascript">

		 //下载附件
 function download(id){
		 var attachDownLoad = document.getElementById("attachDownLoad");
		 	attachDownLoad.src = "<%=path%>/personnel/trainingmanage/training!down.action?attachId="+id;
		 }
		
		</SCRIPT>
	</body>
</html>
