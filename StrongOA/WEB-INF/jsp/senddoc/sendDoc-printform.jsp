<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@include file="/common/include/rootPath.jsp"%>
<%@include file="/common/OfficeControl/version.jsp"%>
<html>
	<head>
		<title>打印处理</title>
		<link rel="stylesheet" type="text/css"
			href="<%=path%>/common/js/loadmask/jquery.loadmask.css" />
		<LINK href="<%=frameroot%>/css/properties_windows.css" type=text/css
			rel=stylesheet>
		<script src="<%=path%>/common/js/jquery/jquery-1.2.6.js"
			type="text/javascript"></script>
		<script language="javascript"
			src="<%=path%>/common/js/loadmask/jquery.loadmask.js"></script>
		<script type="text/javascript">
	
		//var xmlDoc = new ActiveXObject("Microsoft.XMLDOM");
		$(document).ready(function(){
			var params = window.dialogArguments;//得到父窗口传来的参数（表单数据）
			var formReader = params[0];
			var formId = params[1];
			var TANGER_OCX_OBJ = document.getElementById("TANGER_OCX_OBJ");
			//TANGER_OCX_OBJ.Menubar = false;//隐藏菜单栏
			TANGER_OCX_OBJ.FileNew = false;
			TANGER_OCX_OBJ.FileOpen = false;
			TANGER_OCX_OBJ.FilePrint = true;
			
			TANGER_OCX_OBJ.OpenFromURL("<%=root%>/doctemplate/doctempItem/docTempItem!opendoc.action?doctemplateId=<%=request.getParameter("template")%>");
			//查找标签与电子表的映射关系.
			//$("body").mask("正在加载,请稍后...");
			$.getJSON("<%=root%>/senddoc/sendDoc!readBookMarkInfo.action?timestamp="+new Date(),{formId:formId},function(ret){
					if(ret == "-1"){
						alert("读取标签与电子表单映射时异常!");
						return ;
					}
	           		var doc =TANGER_OCX_OBJ.ActiveDocument;
					var bks = doc.Bookmarks;
					var bksCount = bks.Count;
					for(i=1;i<=bksCount ;i++){
						try{
							var name = bks(i).Name ;
							var value = TANGER_OCX_OBJ.GetBookmarkValue(name);
							TANGER_OCX_OBJ.SetBookmarkValue(name,"");//先清空原书签的值							
								$.each(ret,function(i,json){
									if(name == json.bookMarkName){
										var control = formReader.GetFormControl(json.componentName);
										if(control != null){
											if(control != "Strong.Form.WorkControls.AuditOpinion" ){	
												var controlValue = control.Value;
												if(control.GetProperty("SelectedName")){//下拉列表
													controlValue = control.SelectedName;
												}
												TANGER_OCX_OBJ.SetBookmarkValue(name,controlValue);
											}else{											
												var controlValue = control.Value;
												//alert(controlValue);
	      										 var obj = $(controlValue).find("row");
	      										 var value = "";
												 $.each(obj,function(i,row){
													var type = row.type;
													var opinion = row.opinion;
													var sign = row.sign;
													var date = row.date.split(" ");
													value += opinion +"\r\n" + sign + date[0] + "\r\n";
													//alert(value);
													
												});
												if(value != null){
														TANGER_OCX_OBJ.SetBookmarkValue(name,value);
													}
											
											}
										}
									}
								});
							
						}catch(e){}
					}
			});
		});
		function download(){
			$("body").append("<iframe src=\"<%=path%>/attachPage.doc\" height=\"0\" width=\"0\"></iframe>");
		}

	</script>
	</head>

	<body>	
									
		<div style="width: 100%;text-align: center;">							
			<a class="Operation" href="#" style="width: 120px;" onclick="download();">下载续页模板</a>
			
		</div>
	
		<script type="text/javascript">
			document.write(OfficeTabContent);
		</script>
	</body>
</html>
