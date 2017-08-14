<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="/tags/web-flex" prefix="webflex"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<%@ taglib uri="/tags/web-newdate" prefix="strong"%>
<%@include file="/common/include/rootPath.jsp"%>
<%@include file="/common/eformOCX/version.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" >
<html>
	<head>
		<title>销假单填写</title>
		<%@include file="/common/include/meta.jsp"%>
		<LINK href="<%=frameroot%>/css/properties_windows.css" type=text/css
			rel=stylesheet>
		<script src="<%=path%>/common/js/jquery/jquery-1.2.6.js"
			type="text/javascript"></script>
		<script src="<%=path%>/common/js/common/common.js"
			type="text/javascript"></script>
		<SCRIPT LANGUAGE="JavaScript"
			src="<%=path%>/common/js/commontab/service.js"></SCRIPT>
		<SCRIPT LANGUAGE="JavaScript"
			src="<%=path%>/common/js/commontab/workservice.js"></SCRIPT>
		<script src="<%=path%>/common/js/common/map.js" type="text/javascript"></script>	
		<script src="<%=path%>/oa/js/eform/eform.js" type="text/javascript"></script>
		<script type="text/javascript">
		
			//初始化设置
			function initial(){
				if($("#pkFieldValue").val()!=""){
					FormInputOCX.AddFilterParams($("#tableName").val(),$("#pkFieldName").val(),$("#pkFieldValue").val());
			     	FormInputOCX.LoadFormData();//将业务数据加载到电子表单容器中
				}
				if(FormInputOCX.OfficeActiveDocument){
					with (FormInputOCX.OfficeActiveDocument.Application) {//痕迹保留
					    UserName = '${userName}';
					}
				}
			}
			//由eform.js中定义的doNext()回调
			function goBack(){
				 alert("发送成功！");
		   		 if('${apptype}'=="apply"){
				 location="<%=root%>/attendance/apply/apply.action";
				 }else{
				 location="<%=root%>/attendance/apply/apply!canclelist.action";
				 }
			}
		
			//送审
			function submitNext() {
				var eform = document.getElementById("FormInputOCX");
				eform.SetFieldValue('T_OA_ATTEND_CANCLE','CANCLE_STATE',"1");
				doNext();
		    }
		    //保存表单成功以后的回调函数
		    function AfterSaveFormData(isReturn){
		        if(isReturn){
			    	alert("保存成功！");
			    	 if('${apptype}'=="apply"){
						location="<%=root%>/attendance/apply/apply.action";
					 }else{
						location="<%=root%>/attendance/apply/apply!canclelist.action";
					 }
		    	}else{
		    	    alert("保存成功！");
		    	    //window.returnValue = "OK" ;
		    	}
		    }
	

			//返回按钮
			function returnfirst(){
			    if('${apptype}'=="apply"){
				location="<%=root%>/attendance/apply/apply.action";
				}else{
				location="<%=root%>/attendance/apply/apply!canclelist.action";
				}
			}
	
	//保存表单数据,返回生成的表单ID,
	//@param:isReturn:保存后是否需要返回到草稿页。
	//true：返回；false：停留在当前页
    function cansaveFormData(isReturn){
		var formData=FormInputOCX.GetFormData();//表单数据
		if(formData == ""){
			return ;
		}
		var ret = FormInputOCX.GetLastReturnCode();
		if(ret == 1){
			$.post(contextPath + "!canFormDateSave.action",{formData:formData},
				   function(data){
						if(data == "-1"){
							alert("读取电子表单信息失败，请检查是否上传的文件过大！");
						}else if(data == "-2"){
							alert("保存表单数据失败！请检查表单域字段是否绑定到对应表字段！");
						}else{//返回业务数据：表名;主键名;主键值
							AfterSaveFormData(isReturn);//回调函数
						}
			});
		}else{
			alert("保存表单数据失败，请检查表单域字段是否绑定到对应表字段！");
		}
    }
	
	
   
    
 
</script>
	</head>
	<body class=contentbodymargin oncontextmenu="return false;">
	<!-- 业务数据名称 -->
		<s:hidden id="businessName" name="businessName"></s:hidden>
		<!-- 业务表名称 -->
		<s:hidden id="tableName" name="tableName"></s:hidden>
		<!-- 业务表主键名称 -->
		<s:hidden id="pkFieldName" name="pkFieldName"></s:hidden>
		<!-- 业务表主键值 -->
		<s:hidden id="pkFieldValue" name="pkFieldValue"></s:hidden>
		<!-- 电子表单模板id -->
		<s:hidden id="formId" name="formId"></s:hidden>
		<!-- 任务id -->
		<s:hidden id="taskId" name="taskId"></s:hidden>
		<!-- 电子表单数据 -->
		<s:hidden id="formData" name="formData"></s:hidden>
		<!-- 拟稿单位 -->
		<s:hidden id="orgName" name="orgName"></s:hidden>
		<!-- 拟稿人 -->
	<input type="hidden" id="applyId" value="${model.applyId}"/>
	<input type="hidden" id="applyStime" value="${model.applyStime}"/>
	<input type="hidden" id="applyEtime" value="${model.applyEtime}"/>
	<s:hidden id="formId" name="formId"></s:hidden>
  <s:hidden id="workId" name="workId"></s:hidden>
	<s:hidden id="cancleid" name="cancleid"></s:hidden>
		<script type="text/javascript"
			src="<%=path%>/common/js/newdate/WdatePicker.js"></script>
		<DIV id=contentborder align=center>
		
			<form id="recvdocForm" action="<%=root%>/attendance/apply/apply!canFormDateSave.action"
				method="post" enctype="multipart/form-data">
				
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
										销假单填写
									</td>
									<td width="70%">
										<table border="0" align="right" cellpadding="00"
											cellspacing="0">
											<tr>

												<td width="*">
													&nbsp;
												</td>
												
												<td width="5"></td>
												<td >
													<a class="Operation" href="javascript:submitNext();"><img
				                                    src="<%=root%>/images/ico/songshen.gif" width="15"
				                                    height="15" class="img_s">送审&nbsp;</a>
												</td>
												<td width="5"></td>
												<td >
													<a class="Operation" href="javascript:cansaveFormData(true);">
														<img src="<%=root%>/images/ico/baocun.gif" width="15"
															height="15" class="img_s">保存返回&nbsp;</a>
												</td>
												<td width="5"></td>
											<!-- <td width="50">
													<a class="Operation" href="javascript:saveFormData(false);">
														<img src="<%=root%>/images/ico/baocun.gif" width="15"
															height="15" class="img_s">保存</a>
												</td>
												<td width="5"></td>-->
												<td >
													<a class="Operation" href="javascript:returnfirst();">
														<img src="<%=root%>/images/ico/ht.gif" width="15"
															height="15" class="img_s"> 返回&nbsp;</a>
												</td>
												<td width="5">
													&nbsp;
												</td>
											</tr>
										</table>
									</td>
								</tr>
							</table>
						</td>
					</tr>
				</table>
				<table width="100%" height="177" border="0" cellpadding="0"
					cellspacing="1" class="table1">
					<tr>
						
						<td>				  
              <object classid="clsid:750B2722-ADE6-446A-85EF-D9BAEAB8C423"
                codebase="<%=root%>/common/eformOCX/FormInputOCX.CAB<%=OCXVersion%>"
                id="FormInputOCX">
                <param name="Visible" value="0" />
                <param name="Font" value="MS Sans Serif" />
                <param name="KeyPreview" value="0" />
                <param name="PixelsPerInch" value="96" />
                <param name="PrintScale" value="1" />
                <param name="Scaled" value="-1" />
                <param name="DropTarget" value="0" />
                <param name="HelpFile" value="" />
                <param name="ScreenSnap" value="0" />
                <param name="SnapBuffer" value="10" />
                <param name="DoubleBuffered" value="0" />
                <param name="Enabled" value="-1" />
                <param name="AutoScroll" value="1" />
                <param name="AutoSize" value="0" />
                <param name="AxBorderStyle" value="0" />
                <param name="Color" value="4278190095" />
              </object>
            </td>
					</tr>
				</table>
				<br/>
			
			</form>
		</DIV>
	</body>
</html>
