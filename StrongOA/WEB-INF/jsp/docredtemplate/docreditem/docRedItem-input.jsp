<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<%@include file="/common/include/rootPath.jsp"%>
<%@include file="/common/OfficeControl/version.jsp"%>
<html>
	<head>
		<%@include file="/common/include/meta.jsp"%>
		<title>保存公文套红</title>
		<LINK href="<%=frameroot%>/css/properties_windows.css"
			type=text/css rel=stylesheet>
		<script src="<%=path%>/common/js/jquery/jquery-1.2.6.js"
			type="text/javascript"></script>
		<script src="<%=path%>/common/js/validate/jquery.validate.js"
			type="text/javascript"></script>
		<script src="<%=path%>/common/js/validate/formvalidate.js"
			type="text/javascript"></script>
		<SCRIPT LANGUAGE="JavaScript" src="<%=path%>/common/js/commontab/service.js"></SCRIPT>
		<SCRIPT LANGUAGE="JavaScript" src="<%=path%>/common/js/commontab/workservice.js"></SCRIPT>
		<style media="screen" type="text/css">
		    .tabletitle {
		      FILTER:progid:DXImageTransform.Microsoft.Gradient(
		                            gradientType = 0, 
		                            startColorStr = #ededed, 
		                            endColorStr = #ffffff);
		    }
		    
		    .hand {
		      cursor:pointer;
		    }
	    </style>
		<script type="text/javascript">
		 //初始化WORD控件
            function initWordOCX() {
              var TANGER_OCX_OBJ = document.getElementById("TANGER_OCX_OBJ");
                TANGER_OCX_OBJ.CreateNew("Word.Document");                
                //禁用新建功能
                TANGER_OCX_OBJ.FileNew=false;
                //禁用保存功能
                TANGER_OCX_OBJ.FileSave=false;
                //禁用打印功能
                TANGER_OCX_OBJ.FilePrint=false;
                //禁用关闭功能
                TANGER_OCX_OBJ.FileClose=false;
                //禁用另存为功能
                TANGER_OCX_OBJ.FileSaveAs=false;
            }
      
      function saveDoc() {
      	var RTtitle = $.trim($("#redstempTitle").val());
        if(RTtitle == "") {
          alert("请填写模板名称！");
          $("#redstempTitle").val("");
          $("#redstempTitle").focus();
        } else {
        
        	if(RTtitle.length>50){
        		alert("请将模板名称的字数控制在50个字以内！");
        		$("#redstempTitle").focus();
        		return ;
        	}
	        var TANGER_OCX_OBJ = document.getElementById("TANGER_OCX_OBJ");
	        var ret = TANGER_OCX_OBJ.SaveToURL(docredForm.action,
	                                           "wordDoc",
	                                           "",
	                                           $("#redstempTitle").val(),
	                                           "docredForm");
	        eval("var doc = "+ret);
	        if (doc.success == "yes") {
	            $("#redstempTitle").val(doc.title);
	            $("#redstempId").val(doc.id);
	            var r=confirm("保存文档成功，是否继续编辑？");
	            if (r == false) {
	                window.returnValue = "OK";
	                window.close();
	            }
	        } else {
	            alert("保存文档失败!");
	        }
        }
      }
      
      function openFromURL(redstempId) {
          var TANGER_OCX_OBJ = document.getElementById("TANGER_OCX_OBJ");
          TANGER_OCX_OBJ.OpenFromURL("<%=root%>/docredtemplate/docreditem/docRedItem!opendoc.action?redstempId="+redstempId);
      }
      
      	//0：新建对象	1：打开		2：保存		3：另存为
		//4：打印		5：打印设置	6：文件属性
      function showDialog(value){
      try
		{
	      	var TANGER_OCX_OBJ = document.getElementById("TANGER_OCX_OBJ");
	      	TANGER_OCX_OBJ.ShowDialog(value);
		}catch(err){}
		finally{
		}
      }
      
      //文档打印
      function printdoc(){
      	try
		{
	      	var TANGER_OCX_OBJ = document.getElementById("TANGER_OCX_OBJ");
	      	TANGER_OCX_OBJ.FilePrint=true;
			TANGER_OCX_OBJ.printout(true);
		}catch(err){}
		finally{
		}
		
      }      
      
      function closeDoc() {
        window.returnValue = "NO";
        window.close();
      }
      
      $(document).ready(function(){			  
			  initWordOCX();
			  var id = $("#redstempId").val()
			  if ((id != "") && (id != null)) {
			     openFromURL($("#redstempId").val());
			  }
			}); 
    </script>
	</head>
	<base target="_self" />
	<body class="contentbodymargin" >
		<s:form id="docredForm" name="docredForm"
			action="/docredtemplate/docreditem/docRedItem!save.action" enctype="multipart/form-data"
			method="post">
			<s:hidden id="redstempId" name="model.redstempId"></s:hidden>
<%--			<s:hidden id="docgroupId" name="model.toaDoctemplateGroup.docgroupId"></s:hidden>--%>
			<input type="hidden" name="model.toaDocredGroup.redtempGroupId" id="redtempGroupId" value="${redtempGroupId}"/>
			
			<s:file id="wordDoc" name="wordDoc" cssStyle="display:none;"></s:file>
			<div id="contentborder" align="center">
				<table width="100%" height="100%" border="0" cellspacing="0"
					cellpadding="0" style="vertical-align: top;">
					<tr>
						<td width="100%" colspan="2" height="40" class="tabletitle">
							<table width="100%" border="0" align="right" cellpadding="0"
								cellspacing="0">
								<tr>
									<td>&nbsp;</td>
									<td width="30%">
										<img src="<%=frameroot%>/images/ico/ico.gif" width="9"
											height="9" alt="">&nbsp;
										公文模块
									</td>
									<td>
										&nbsp;
									</td>
									<td width="70%"><table align="right"><tr>
									<td >
									<a class="Operation" href="javascript:saveDoc();">
										<img src="<%=root%>/images/ico/baocun.gif" width="14"
											height="14" alt="" class="img_s">
										保存至服务器&nbsp;</a>
									</td>
									<td width="5"></td>
									<td >
									<a class="Operation" href="javascript:closeDoc();">
										<img src="<%=root%>/images/ico/guanbi.gif" width="14"
											height="14" alt="" class="img_s">
									关闭&nbsp;</a>
									</td>
									<td width="23">
										&nbsp;
									</td>
									</tr></table></td>
								</tr>
							</table>
						</td>
					</tr>
					<tr>
						<td valign="top" width="10%" height="100%">
							<table width="100%" align="center" border="0" cellpadding="0"
								cellspacing="1" class="table1">
								
								
								<tr>
									<td nowrap align="center" class="biao_bg1">
										文件操作
									</td>
								</tr>
								<tr onclick="showDialog(1)" style="cursor: pointer; line-height: 20px;">
									<td nowrap class="td1">
										打开本地文件
									</td>
								</tr>
								<tr onclick="showDialog(5);" style="cursor: pointer; line-height: 20px;">
									<td nowrap class="td1">
										设置页面布局
									</td>
								</tr>
								<tr onclick="showDialog(6);" style="cursor: pointer; line-height: 20px;">
									<td nowrap class="td1">
										设置文件属性
									</td>
								</tr>
								<tr onclick="printdoc();" style="cursor: pointer; line-height: 20px;">
									<td nowrap class="td1">
										打印控制
									</td>
								</tr>
								<tr onclick="saveDoc();"
									style="cursor: pointer; line-height: 20px;" title="保存为草稿">
									<td nowrap class="td1">
										<img src="<%=root%>/images/ico/baocun.gif" width="14"
											height="14" alt="">
										保存至服务器
									</td>
								</tr>
								<tr onclick="showDialog(2);" style="cursor: pointer; line-height: 20px;">
									<td nowrap class="td1">
										保存到本地
									</td>
								</tr>
								<tr onclick="closeDoc();"
									style="cursor: pointer; line-height: 20px;">
									<td nowrap class="td1">
										<img src="<%=root%>/images/ico/guanbi.gif" width="14"
											height="14" alt="">
										关闭
									</td>
								</tr>
								<tr>
									<td nowrap align="center" class="biao_bg1">
										文件编辑
									</td>
								</tr>
								
							</table>
						</td>
						<td width="90%">
							<table width="100%" height="100%" align="center" border="0"
								cellpadding="0" cellspacing="1" class="table1">
								<tr>
									<td width="10%" nowrap class="biao_bg1" align="right">
										<span class="wz">所属模板类别：</span>
									</td>
									<td width="20%" class="td1" align="left" title="${redtempGroupName}">
										<script>
											var grpName = "${redtempGroupName}";
											if(grpName.length>15){
												document.write(grpName.substring(0,15)+"...");
											}else{
												document.write(grpName);
											}
										</script>
									</td>
									<td width="10%" nowrap class="biao_bg1" align="right">
										<span class="wz">模板标题(<span style="color: red;">*</span>)：</span>
									</td>
									<td nowrap class="td1">
										<s:textfield id="redstempTitle" name="model.redstempTitle"  maxlength="100" title="请将字数控制在50个汉字以内"
											  cssStyle="width: 80%;" ></s:textfield>
										&nbsp;
									</td>
								</tr>
								<tr>
									<td colspan="4" valign="top" height="100%">
										<%--<object id="TANGER_OCX_OBJ"
											classid="clsid:C9BC4DFF-4248-4a3c-8A49-63A7D317F404"
											codebase="<%=root%>/common/OfficeControl/OfficeControl.cab<%=OCXVersion%>"
											width="100%" height="850">
											<param name="ProductCaption" value="思创数码科技股份有限公司">
											<param name="ProductKey" value="B339688E6F68EAC253B323D8016C169362B3E12C">
											<param name="BorderStyle" value="1">
											<param name="TitlebarColor" value="42768">
											<param name="TitlebarTextColor" value="0">
											<param name="TitleBar" value="false">
											<param name="MenuBar" value="true">
											<param name="Toolbars" value="true">
											<param name="IsResetToolbarsOnOpen" value="true">
											<param name="IsUseUTF8URL" value="true">
											<param name="IsUseUTF8Data" value="true">
											<span style="color: red">不能装载文档控件。请在检查浏览器的选项中检查浏览器的安全设置。</span>
										</object>
										--%>
										<script type="text/javascript">
											document.write(OfficeTabContent);
										</script>
									</td>
								</tr>
							</table>
						</td>
					</tr>
				</table>
			</div>
		</s:form>
	</body>
</html>
