<%@page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@taglib uri="/tags/web-flex" prefix="webflex"%>
<%@taglib uri="/tags/web-newdate" prefix="strong"%>
<%@taglib uri="/struts-tags" prefix="s" %>
<%@include file="/common/include/rootPath.jsp"%>
<%@include file="/common/eformOCX/version.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" >
<html>
	<head>
	  <%@include file="/common/include/meta.jsp" %>
		<title>查看工作</title>
		<link href="<%=frameroot%>/css/properties_windows.css" type="text/css" rel="stylesheet">
		<script src="<%=path%>/common/js/jquery/jquery-1.2.6.js" type="text/javascript"></script>
	    <script src="<%=path%>/common/js/common/common.js" type="text/javascript"></script>
		<script type="text/javascript">
		  //关闭
	      function goBack(){
	          window.close();
	      }
	      //查看流程图
	      function workflowView(){      
	          var width=screen.availWidth-10;;
	          var height=screen.availHeight-30;
	          var ReturnStr=OpenWindow("<%=root%>/work/work!PDImageView.action?instanceId="+'${instanceId}', 
	                                   width, height, window);
	      }
	      //初始化
	      $(document).ready(function(){
	      	  var formId = '${formId}';
	      	  if(formId == "0"){
	      	  	alert("流程未挂接展现表单！");
	      	  	window.close();
	      	  }else{
		          var eform = document.getElementById("FormInputOCX");
		          eform.InitialData('<%=basePath%>/services/EFormService?wsdl');
		          eform.SetFormTemplateID(formId);
		           eform.width=eform.MaxWidth;//设置电子表单OCX的宽度为内表单最度的宽度
     				eform.height=eform.MaxHeight;//设置电子表单OCX的高度为内表单最高的高度
		          var ret=eform.AddFilterParams('${tableName}','${pkFieldName}','${pkFieldValue}');
		          ret=eform.LoadFormData();
		          eform.SetFieldsReadOnly(true);
		          initFieldSet();
	      	  }
	      });
	      
	       //初始化意见设置
 function initFieldSet(){
 	 var FormInputOCX = document.getElementById("FormInputOCX");
 	$.getJSON("<%=root%>/work/work!getInstro.action?timeStamp="+new Date(),
 				{instanceId:'${instanceId}'},function(result){
 		$.each(result,function(i,json){
 			if(json.fieldName){//设置输入意见到表单容器域中
 				if(json.infomation){
   					FormInputOCX.SetControlValue(json.fieldName,json.infomation);
 				}
 			}
 			//设置在任务节点设置的属性应用到表单中
 			var otherField = json.otherField;
 			if(otherField){
  			$.each(otherField,function(i,field){
  				var control = FormInputOCX.GetControl(field.fieldName);
  				if(control){
  					if(field.ReadOnly){//控件是否只读
   					control.SetProperty("ReadOnly",field.ReadOnly);
  					}
  					if(field.Visible){//控件是否可显示
  						control.SetProperty("Visible",field.Visible);
  					}
  					if(field.Enabled){//控件是否可用
   					control.SetProperty("Enabled",field.Enabled);
  					}
  					if(field.SetReadOnly){//有这个属性,说明是OFFICE控件
  						control.FileNew = field.FileNew;//是否允许新建
  						control.FileSave = field.FileSave;//是否允许保存
  						control.FilePrint = field.FilePrint;//是否允许打印
  						control.FileOpen = field.FileOpen;//是否允许打开
  						control.FileClose = field.FileClose;//是否允许关闭
  						control.FileSaveAs = field.FileSaveAs;//是否允许另存为
  						control.SetReadOnly(field.SetReadOnly,"");//是否只读
  					}
  				}else{//找不到指定控件,可能是HTML控件,这里用JQUERY来捕捉对象
  					if(document.getElementById(field.fieldName)){
  						$("#"+field.fieldName).css("display",field.visible);
  					}
  				}
  			});
 			}
 		});
 	});
 }
	      
	     //打印处理
			function goToPrint(){
			  	var formId = '${formId}';
			    var FormInputOCX = document.getElementById("FormInputOCX");
			    $.getJSON("<%=root%>/work/work!getFormComponent.action?timeStamp="+new Date(),
	   				{formId:formId},function(result){
		      		$.each(result,function(i,json){
		      			if(json.fieldName){//设置输入意见到表单容器域中
				      		var control = FormInputOCX.GetControl(json.fieldName);
				      		if(json.fieldType == "TEFEdit" || json.fieldType == "TEFMemo"){
					      		control.SetProperty("FrameStyle","fsNone");
				      		}else if(json.fieldType == "TEFButton"){
				      			control.SetProperty("Visible","false");
				      		}
		      			}
		      			if(i == result.length - 1){
		      				var myPrint = FormInputOCX.PrintPage(0);
		      				
							if(myPrint==-1){
								alert("打印失败，请您重新打印");
							}
							$.each(result,function(i,json){
				      			if(json.fieldName){//设置输入意见到表单容器域中
						      		var control = FormInputOCX.GetControl(json.fieldName);
						      		if(json.fieldType == "TEFEdit" || json.fieldType == "TEFMemo"){
							      		control.SetProperty("FrameStyle","fsFlat");
						      		}else if(json.fieldType == "TEFButton"){
						      			control.SetProperty("Visible","true");
						      		}
				      			}
				      		});
		      			}
		      		});
		      	});
			}
	    </script>
	</head>
	<body class="contentbodymargin" oncontextmenu="return false;">	
		<div id="contentborder" align="center">
			<table width="100%" height="100%" border="0" cellspacing="0" cellpadding="0"
				style="vertical-align: top;">				
				<tr>
					<td height="40" class="tabletitle">
						<table width="100%" border="0" align="right" cellpadding="0"
							cellspacing="0">
							<tr>
								<td>&nbsp;</td>
								<td width="30%">
									<img src="<%=frameroot%>/images/ico/ico.gif" width="9" height="9"
										alt="">&nbsp;
									工作处理单
								</td>
								<td>
									&nbsp;
								</td>
				                <td >
				                  <a class="Operation" href="#" onclick="goToPrint();"><img
				                                    src="<%=root%>/images/ico/tb-print16.gif" width="15"
				                                    height="15" class="img_s">打印&nbsp;</a>
				                </td>
				                <td width="5"></td>
				                  <td >
				                  <a class="Operation" href="#" onclick="workflowView();"><img
				                                    src="<%=root%>/images/ico/chakan.gif" width="15"
				                                    height="15" class="img_s">查看流程图&nbsp;</a>
				                </td>
				                <td width="5"></td>
								<td >
									<a class="Operation" href="#"  onclick="goBack();"><img
                                    src="<%=root%>/images/ico/guanbi.gif" width="15"
                                    height="15" class="img_s">关闭&nbsp;</a>
								</td>
								<td width="5">
									&nbsp;
								</td>
							</tr>
						</table>
					</td>
				</tr>
				<tr>
			          <td align="center">
				            <object height="850" width="100%"
				              classid="clsid:750B2722-ADE6-446A-85EF-D9BAEAB8C423"
				              codebase="<%=root%>/common/eformOCX/FormInputOCX.CAB<%=OCXVersion%>"
				              id="FormInputOCX"><param name="Visible" value="0" />
				              <param name="AutoScroll" value="0" />
				              <param name="AutoSize" value="0" />
				              <param name="AxBorderStyle" value="1" />
				              <param name="Caption" value="FormInputOCX" />
				              <param name="Color" value="4278190095" />
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
				            </object>
			          </td>
        		</tr>
				<tr>
					<td height="40" align="center">
					<input name="Submit3" type="button" class="input_bg" value="打印" onclick="goToPrint();">
					<input name="Submit" type="button" class="input_bg" value="查看流程图" onclick="workflowView();">	
					<input name="Submit2" type="button" class="input_bg" value="关 闭" onclick="goBack();">     
					</td>
				</tr>
			</table>
		</div>
	</body>
</html>