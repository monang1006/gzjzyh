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
			<input type="hidden" id="clumnId" name="model.toaTrainColumn.clumnId"
								value="${clumnId}">
													
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
												添加培训信息
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
					<input id="trainTopic" name="model.trainTopic" type="text" style="width:80%;" value="${model.trainTopic}" size="100">
					</td>
					<td colspan="1"  height="21" class="biao_bg1" align="right" width="20%">
						<span class="wz">培训类型(<font color="#FF6600">*</font>)：&nbsp;</span>
					</td>
					<td colspan="1"  class="td1" align="left" width="30%">
	<s:select list="trainTypeList" listKey="dictItemCode" listValue="dictItemName"  headerKey="" headerValue="请选择培训类型" 
							id="trainType" name="model.trainType" style="width:80%"/>
					</td>
					
				</tr>
            	<tr>
				    <td colspan="1" height="21" class="biao_bg1" align="right">
						<span class="wz">开始日期(<font color="#FF6600">*</font>)：&nbsp;</span>
					</td>
					<td colspan="1" class="td1" align="left">
						 <strong:newdate name="model.trainStartdate" id="trainStartdate"  width="80%"
                      skin="whyGreen" isicon="true" dateobj="${model.trainStartdate}" dateform="yyyy-MM-dd"></strong:newdate>
					</td>
					<td width="20%" colspan="1" height="21" class="biao_bg1" align="right">
						<span class="wz">结束日期(<font color="#FF6600">*</font>)：&nbsp;</span>
					</td>
					<td  width="30%" colspan="1" class="td1">
					 <strong:newdate name="model.trainEnddate" id="trainEnddate"  width="80%"
                      skin="whyGreen" isicon="true" dateobj="${model.trainEnddate}" dateform="yyyy-MM-dd"></strong:newdate>
					</td>
					
				</tr>
				<tr>
				   
				    <td colspan="1" height="21" class="biao_bg1" align="right" width="20%">
						<span class="wz">主办单位：&nbsp;</span>
					</td>
					<td colspan="1" class="td1" align="left" width="30%">
				<input id="trainCommpany" name="model.trainCommpany" type="text" style="width:80%;" value="${model.trainCommpany}">
					</td>	
					<td  height="21" class="biao_bg1" align="right" width="20%">
						<span class="wz">费用(元)：&nbsp;</span>
					</td>
					<td   class="td1" align="left" width="30%">
			<input id="trainMoney" name="model.trainMoney" type="text" style="width:80%;" value="${model.trainMoney}" class="required number">
					</td>
					
				</tr>
				
				<tr>
					<td height="21" class="biao_bg1" align="right">
						<span class="wz">培训人员(<font color="#FF6600">*</font>)：&nbsp;</span><br>
						
					</td>
					<td class="td1" colspan="3" align="left">
					
						<textarea  id="personsName" name="personsName"
							             style="width:90%;height:150px;" >${personsName}</textarea>
							   <input id="persons" name="persons" type="hidden">
						<input type="button" class="input_bg" value="添 加" onClick="adduser();">
				    </td>
				</tr>
				<tr>
					<td height="21" class="biao_bg1" align="right">
						<span class="wz">备注：&nbsp;</span><br>
						<font color="#FF6600">不可超过232个字符</font>
					</td>
					<td class="td1" colspan="3" align="left">
							<textarea  id="trainDemo" name="model.trainDemo" 
							             style="width:90%;height:150px;" >${model.trainDemo}</textarea>
				    </td>
				</tr>
				<tr>
						<td height="21" class="biao_bg1" align="right">
							<span class="wz">附    件&nbsp;&nbsp;&nbsp;：</span>
						</td>
						<td class="td1" colspan="3" align="left">
							&nbsp;<input type="file" onkeydown="return false;" name="upload" class="multi required" style="width: 63%;"/>
								${attachFiles}	
						</td>
					</tr>	
				
			</table><br>
			<table width="100%">
			<tr>
					<td class="td1" colspan="4" align="center" height="21">
					    <input name="Submit" type="button" class="input_bg" value="保  存" onclick="save();">
					    &nbsp; &nbsp; &nbsp; &nbsp; 
						<input name="Submit2" type="button" class="input_bg" value="关  闭"
							onclick="window.close();">
					</td>
				</tr>
			</table>
		
		</s:form>
		</DIV>
		<SCRIPT type="text/javascript">
			//删除附件
 function delAttach(id){
		 	var attach = document.getElementById(id);
		 	var delAttachIds = document.getElementById("delAttachIds").value;
		 	document.getElementById("delAttachIds").value += id + ",";
		 	attach.style.display="none";
		 }
		 //下载附件
 function download(id){
		 var attachDownLoad = document.getElementById("attachDownLoad");
		 	attachDownLoad.src = "<%=path%>/personnel/trainingmanage/training!down.action?attachId="+id;
		 }
		
			function save(){ 
			  var inputDocument=document;
			  if(inputDocument.getElementById("trainTopic").value==""){
    	       alert("培训主题不能为空，请输入。");
    	      inputDocument.getElementById("trainTopic").focus();
    	          return false;
             } 
               if(inputDocument.getElementById("trainType").value==""){
    	       alert("培训类型不能为空，请选择。");
    	      inputDocument.getElementById("trainType").focus();
    	          return false;
             } 
             
         
			  if(inputDocument.getElementById("trainStartdate").value==""){
    	       alert("开始日期不能为空，请选择。");
    	      inputDocument.getElementById("trainStartdate").focus();
    	          return false;
             } 
               if(inputDocument.getElementById("trainEnddate").value==""){
    	       alert("结束日期不能为空，请选择。");
    	      inputDocument.getElementById("trainEnddate").focus();
    	          return false;
             } 
             
             
             
               if(inputDocument.getElementById("trainCommpany").value.length>16){
    	           alert("主办单位长度过长，请重新输入!");
    	         inputDocument.getElementById("trainCommpany").focus();
    	          return false;
             } 
             if(inputDocument.getElementById("trainMoney").value.length>32){
    	       alert("培训费用长度过长，请重新输入!");
    	      inputDocument.getElementById("trainMoney").focus();
    	          return false;
             } 
              if(/[^\d]/.test(inputDocument.getElementById("trainMoney").value)){
 		        alert("培训费用必须是数字！");
 		          return false;
 		      }
             if(inputDocument.getElementById("personsName").value==""){
    	       alert("培训人员不能为空，请选择。");
    	      inputDocument.getElementById("personsName").focus();
    	          return false;
             } 
			//获取被删除的附件id
			var delAttachIds = document.getElementById("delAttachIds").value;
			if(delAttachIds.length>0){
            		delAttachIds = delAttachIds.substring(0,delAttachIds.length-1);
            	}
            document.getElementById("delAttachIds").value = delAttachIds; 
   				trainingform.submit();
			}
			
function adduser(){	
	 var result=window.showModalDialog("<%=path%>/personnel/personorg/personOrg!adduser.action",window,'help:no;status:no;scroll:no;dialogWidth:450px; dialogHeight:500px');
	if(result=='succss'){
    // location="<%=path%>/usergroup/baseGroup!userList.action?groupId="+id;
     }

}
		</SCRIPT>
	</body>
</html>
