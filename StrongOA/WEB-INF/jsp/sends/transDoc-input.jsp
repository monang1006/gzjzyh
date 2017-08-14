<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<jsp:directive.page import="java.text.SimpleDateFormat,com.strongit.oa.util.GlobalBaseData"/>

<%@ taglib uri="/struts-tags" prefix="s"%>
<%@taglib uri="/tags/web-remind" prefix="strong"%>
<%@taglib uri="/tags/web-newdate" prefix="strongit"%>
<%@include file="/common/eformOCX/version.jsp"%>
<%@include file="/common/include/rootPath.jsp"%>
<html>
	<head>
		<title>草拟发文公文</title>
		<%@include file="/common/include/meta.jsp" %>
		<LINK href="<%=frameroot%>/css/properties_windows.css" type=text/css rel=stylesheet>
		<link type="text/css" rel="stylesheet" href="<%=path%>/common/js/tabpane/css/luna/tab.css" />
		<script type="text/javascript" src="<%=path%>/common/js/tabpane/js/tabpane.js"></script>
		<script src="<%=path%>/common/js/jquery/jquery-1.2.6.js" type="text/javascript"></script>
		<script language="javascript" src="<%=path%>/common/js/upload/jquery.MultiFile.js"></script>
		<script src="<%=path%>/common/js/upload/jquery.blockUI.js" language="javascript"></script>
		<script src="<%=path%>/common/js/common/common.js" type="text/javascript"></script>
	<script language="javascript">  
	 var checkSubmitFlg = false;   
	 function checkSubmit() {   
	 if (checkSubmitFlg == true) {   
	 return false;   
	 }   
	 checkSubmitFlg = true;   
	 return true;   
	 }   
	 document.ondblclick = function docondblclick() {   
	 window.event.returnValue = false;   
	 }   
	 document.onclick = function doconclick() {   
	 if (checkSubmitFlg) {   
	 window.event.returnValue = false;   
    }   
	 }   
	</script>   
		
		<script type="text/javascript">
		var isDoSave = false;//是否已经点击保存按钮
		var TANGER_OCX_Username = '${userName}';
		var tp1 ;
		var loaded = false;
		function custom(tabpage){
			if(tabpage.index != 1){//处理正文Tab
				return ;
			}
			var TANGER_OCX_OBJ = document.getElementById("TANGER_OCX_OBJ");
			openDoc();
			if(TANGER_OCX_OBJ == ""){
				loaded = false;
				openDoc();
			}
		}
		
	    function TANGER_OCX_ShowDialog(dType) {
	      var FormInputOCX = document.getElementById("TANGER_OCX_OBJ");
	      try{
		      FormInputOCX.ShowDialog(dType);      
	      }catch(e){}
	    }
	    
	    function TANGER_OCX_PrintDoc(booValue) {
	      var FormInputOCX = document.getElementById("TANGER_OCX_OBJ");
	      try{
		      FormInputOCX.PrintOut(booValue);      
	      }catch(e){}
	    }
    	    	
		//初始化自定义菜单
		function initCustomMenus(){
			
			try{
				var myobj = document.getElementById("TANGER_OCX_OBJ");;	
				myobj.AddCustomMenu2(0,"协同办公(X)");
				
				myobj.AddCustomMenuItem2(0,0,-1,false,"文件套红",false,1008);
				myobj.EnableCustomMenuItem2(0,8,-1,false);
			
			}catch(e){
				
			}
				
		}
    	//进入或退出痕迹保留状态
		function TANGER_OCX_SetMarkModify(boolvalue) {
			var FormInputOCX = document.getElementById("TANGER_OCX_OBJ");
			FormInputOCX.ActiveDocument.TrackRevisions = boolvalue;
		}
		
		//显示/不显示痕迹
		function TANGER_OCX_ShowRevisions(boolvalue) {
			var FormInputOCX = document.getElementById("TANGER_OCX_OBJ");
			FormInputOCX.ActiveDocument.ShowRevisions = boolvalue;
		}
		
		
		//插入模板
        function TANGER_OCX_AddTemplateFromURL(){
			var docIssueDepartSigned = $("#docIssueDepartSigned").val();

		   var TANGER_OCX_OBJ = document.getElementById("TANGER_OCX_OBJ");
		   var ReturnStr=OpenWindow("<%=root%>/sends/transDoc!templateTree.action","400", "350", window);
           if(ReturnStr){
				tp1.setSelectedIndex(1);
				if(!TANGER_OCX_OBJ){//校验是否存在WORD
					return ;
				} 
				TANGER_OCX_SetMarkModify(false);
				//查找标签与电子表的映射关系.
				$.getJSON("<%=root%>/sends/transDoc!readBookMarkInfo.action?timestamp="+new Date(),{formId:"/fileNameRedirectAction.action?toPage=sends/transDoc-input.jsp"},function(ret){
					if(ret == "-1"){
						alert("读取标签与电子表单映射时异常!");
						return ;
					}
					TANGER_OCX_OBJ.AddTemplateFromURL("<%=basePath%>/doctemplate/doctempItem/docTempItem!opendoc.action?doctemplateId="+ReturnStr,true); 
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
									var $Obj = $("#"+json.componentName);
									var type = $Obj.attr("type");
									var controlValue = "";
									if(type == "text" || type == "textarea"){//单行文本框,多行文本框
										controlValue = $Obj.val();
									}else if(type == "select-one"){//下拉列表
										controlValue = $Obj.get(0).options[$Obj.get(0).selectedIndex].text;
									}
									TANGER_OCX_OBJ.SetBookmarkValue(name,controlValue);
								}
							});
						}catch(e){}
					} 
					TANGER_OCX_SetMarkModify(true);
				});
           }
        }
		
		
		function openDoc(){
			var id = $("#id").val();
			if(!loaded){
				var TANGER_OCX_OBJ = document.getElementById("TANGER_OCX_OBJ");
				var width=screen.availWidth-55;
	   			var height=screen.availHeight-130;
				if(id == ""){
			  		var type = TANGER_OCX_OBJ.DocType;//得到OFFICE类型.
				  	TANGER_OCX_OBJ.OpenFromURL(basePath + "sends/transDoc!openEmptyDocFromUrl.action?docType=1");
				  	TANGER_OCX_OBJ.WebFileName='newFile';
				}else{
				  	TANGER_OCX_OBJ.OpenFromURL(basePath + "sends/transDoc!openWordDocFromUrl.action?model.docId="+id);
				}
			  	TANGER_OCX_OBJ.width = width;
			  	TANGER_OCX_OBJ.height = height;
				if(TANGER_OCX_OBJ.ActiveDocument){
					with (TANGER_OCX_OBJ.ActiveDocument.Application) {
					    UserName = '${userName}';
					}
				}
				loaded = true;
				tp1.setSelectedIndex(1);
				TANGER_OCX_OBJ.Activate(false); 
			}
		}
		
		var state = 1 ;
		//隐藏显示文件操作菜单栏
		function hidemenu(){
			if(state == 0){
				$("#fileOperation").hide();
				state = 1;
				document.pic.src="<%=frameroot%>/images/jiantou_2.jpg";
				document.pic.title="点击打开菜单栏";
			}else if(state == 1){
				openDoc();
				tp1.setSelectedIndex(1);
				$("#fileOperation").show();
				state = 0;
				document.pic.src="<%=frameroot%>/images/jiantou.jpg";
				document.pic.title="点击隐藏菜单栏";
			}
		}
		
		
		
  
  
		//保存表单数据
		function doSave(){
			//if(document.getElementById("duanxin").checked){
			 //   if(document.getElementById("rest10").value == "")
			 //   {
			///	    alert("短信内容不能为 空，请填写");
			//		tp1.setSelectedIndex(0);
			//		document.getElementById("rest10").focus();
			//		return ;
			  //  }
			//}else if(document.getElementById("rest10").value != "")
			//		    {
			//			    alert("如要发送短信，请勾选短信选项！");
			//				
			//				return ;
			//		    }
			
			
			
			//console.log(11);
		    var flag = true;//验证通过标记
			var alertMsg = "";//提示语
			var docIssueDepartSigned = $("#docIssueDepartSigned").val();
			if($.trim(docIssueDepartSigned) == ""){
				alertMsg += "发文单位不能为空，请填写\n"; //拼接提示语
				flag = false; //验证不通过
			}
			var docOfficialTime = $("#docOfficialTime").val();
			if($.trim(docOfficialTime) == "") {
				alertMsg +="成文日期不能为空，请填写\n";
				flag = false;
			}
		    var docTitle = $("#docTitle").val();
			if($.trim(docTitle) == ""){
				alertMsg +="公文标题不能为空，请填写\n";
			    flag = false;
			}
			var docCode = $("#docCode").val();
			if($.trim(docCode) == ""){
				alertMsg +="公文文号不能为空，请填写\n";
				flag = false;
			}
			var docSubmittoDepart = $("#docSubmittoDepart").val();
			if($.trim(docSubmittoDepart) == ""){
				alertMsg +="主送单位不能为空，请填写\n" ;
				flag = false;
			}
			
			if(!flag){
				alert(alertMsg);
				return;
			}
			
			
			//var docCcDepart = $("#docCcDepart").val();
			//if($.trim(docCcDepart) == ""){
			//	alert("抄送单位不能为空，请填写。");
			//	tp1.setSelectedIndex(0);
			//	$("#docCcDepart").get(0).focus();
			//	return ;
			//}
			
			var TANGER_OCX_OBJ = document.getElementById("TANGER_OCX_OBJ");
			openDoc();
			if(TANGER_OCX_OBJ == ""){
				loaded = false;
				openDoc();
			}
        	var ret = TANGER_OCX_OBJ.SaveToUrl("<%=root %>/sends/transDoc!save.action",
                                           "wordDoc",
                                           "model.docState=0",
                                           "newFile.doc",
                                           "form");
            if(ret.substring(0,1) == "0"){
            	/*if(!confirm("保存成功，是否继续编辑？")){
            		window.returnValue = "0";
            		window.close();
            	}else{
            		$("#id").val(ret.substring(2));
            	}*/
            //	alert("操作成功！");
            	//window.returnValue = "0";
            	opener.location.reload();
            	window.close();   //点击保存后关闭窗口
            }  
            if(ret == "-1"){
            	alert("正文数据读取失败。");
            	return ;
            }           
            if(ret == "-2"){
            	//alert("对不起，发生未知异常，请与管理员联系。");
            	alert("对不起，不能上传空附件。");
            	return ;
            }  
            if(ret == "-3"){
            	alert("公文状态已经改变，您无法对其操作,页面即将关闭！");
				window.close();
				return;
            }  
		}
		
		//提交
		function doSubmit(){
			var docIssueDepartSigned = $("#docIssueDepartSigned").val();
			//if(document.getElementById("duanxin").checked){
			 //   if(document.getElementById("rest10").value == "")
			 //   {
			///	    alert("短信内容不能为 空，请填写");
			//		tp1.setSelectedIndex(0);
			//		document.getElementById("rest10").focus();
			//		return ;
			  //  }
			//}else if(document.getElementById("rest10").value != "")
			//		    {
			//			    alert("如要发送短信，请勾选短信选项！");
			//				
			//				return ;
			//		    }
			if($.trim(docIssueDepartSigned) == ""){
				alert("发文单位不能为空，请填写");
				tp1.setSelectedIndex(0);
				$("#docIssueDepartSigned").get(0).focus();
				return ;
			}
			var docOfficialTime = $("#docOfficialTime").val();
			if($.trim(docOfficialTime) == ""){
				alert("成文日期不能为空，请填写");
				tp1.setSelectedIndex(0);
				$("#docOfficialTime").get(0).focus();
				return ;
			}
			var docTitle = $("#docTitle").val();
			if($.trim(docTitle) == ""){
				alert("公文标题不能为空，请填写。");
				tp1.setSelectedIndex(0);
				$("#docTitle").get(0).focus();
				return ;
			}
			var docCode = $("#docCode").val();
			if($.trim(docCode) == ""){
				alert("公文文号不能为空，请填写。");
				tp1.setSelectedIndex(0);
				$("#docCode").get(0).focus();
				return ;
			}
			var docSubmittoDepart = $("#docSubmittoDepart").val();
			if($.trim(docSubmittoDepart) == ""){
				alert("主送单位不能为空，请填写。");
				tp1.setSelectedIndex(0);
				$("#docSubmittoDepart").get(0).focus();
				return ;
			}
			var TANGER_OCX_OBJ = document.getElementById("TANGER_OCX_OBJ");
			openDoc();
			if(TANGER_OCX_OBJ == ""){
				loaded = false;
				openDoc();
			}
			var docSealIs = $("#docSealIs").attr("checked");
			var state ;
			if(docSealIs){
				state = "1";
			}else{
				state = "2";
			}
			var docOfficialTime = $("#docOfficialTime").val();
        	var ret = TANGER_OCX_OBJ.SaveToUrl("<%=root %>/sends/transDoc!save.action",
                                           "wordDoc",
                                           "model.docState="+state+"&type=1"+"&model.docOfficialTime="+docOfficialTime,
                                           "newFile.doc",
                                           "form");
            if(ret.substring(0,1) == "0"){
           		//alert("操作成功！");
           		//window.returnValue = "0";
           		opener.location.reload();
           		window.close();
            }  
            if(ret == "-1"){
            	alert("正文数据读取失败。");
            	return ;
            }           
            if(ret == "-2"){
            	//alert("对不起，发生未知异常，请与管理员联系。");
            	alert("对不起，不能上传空附件。");
            	return ;
            }
            if(ret == "-3"){
            	alert("公文状态已经改变，您无法对其操作,页面即将关闭！");
				window.close();
				return;
            }  
		}
		
		//删除附件,记录要删除的附件ID，多个以逗号隔开
		function deldbobj(id){
			var delIds = $("#deledAttachId").val();
			delIds = delIds + "," + id;
			$("#deledAttachId").val(delIds)
			$("#div"+id).hide();
		}
		//下载附件,改为链接实现
		function download(id){
			
		}
		
		//关闭
		function closeWindow(){
			window.close();
		}
		//获取文号
	   function getDocNumber(){
		 var ret = OpenWindow("<%=root%>/serialnumber/number/number!show.action?regulationSort=/senddoc/sendDoc",400,300,window);
		 if(ret){
			$("#docCode").val(ret);
		 }
	   }
	   //选择机构 zdeptId 主送单位code ;  cdeptId 抄送单位code  ;  flag 为判断是主送还是抄送按钮
	   function chooseDept(deptName,zdeptId,cdeptId,flag){
	   	  var info = new Array();
	   	  if(flag == 1){
		   	  info[0] = document.getElementById(zdeptId).value;
		   	  info[1] = document.getElementById(deptName).value;
		   	  info[2] = document.getElementById(cdeptId).value;
		   	  info[2] = info[2] + "," + $("#rest3").val();
		   	  //alert(info[0]);
		   	  //alert(info[1]);
		   	  var ret = OpenWindow('<%=root%>/sends/transDoc!chooseDept.action', '600', '400', info);
		   	  if(ret && ret != null){
		   	  	var id = ret[0];
		   		var name = ret[1];
		   		document.getElementById(zdeptId).value = id;
		   		document.getElementById(deptName).value = name;
		   		if($('#' + zdeptId) == ""){
					alert("必须选择一个主送单位！");
		   		}
		   	  }
	   	  }else{
	   	  	  info[0] = document.getElementById(cdeptId).value;
		   	  info[1] = document.getElementById(deptName).value;
		   	  info[2] = document.getElementById(zdeptId).value;
		   	  info[2] = info[2] + "," + $("#rest3").val();
		   	  //alert(info[0]);
		   	  //alert(info[1]);
		   	  var ret = OpenWindow('<%=root%>/sends/transDoc!chooseDept.action', '600', '400', info);
		   	  if(ret && ret != null){
		   	  	var id = ret[0];
		   		var name = ret[1];
		   		document.getElementById(cdeptId).value = id;
		   		document.getElementById(deptName).value = name;
		   	  }
	   	  }
	   }
	   
	   	//从本地增加图片到文档指定位置
		//是否浮动文件,true为浮动，false为非浮动
		function TANGER_OCX_AddPicFromLocal(value){
			var TANGER_OCX_OBJ = document.getElementById("TANGER_OCX_OBJ");
		    var ret = TANGER_OCX_OBJ.AddPicFromLocal("", //路径
														true,//是否提示选择文件
														value,//是否浮动图片
														100,//如果是浮动图片，相对于左边的Left 单位磅
														100,
														1,
														100,
														1); //如果是浮动图片，相对于当前段落Top	
		}
	</script>
	</head>
	<base target="_self"/>
	<body class=contentbodymargin oncontextmenu="return false;" scroll="auto" background="#ff919899">
			<script type="text/javascript"
			src="<%=path%>/common/js/newdate/WdatePicker.js"></script>
			<form id="form" name="form" action="<%=root %>/sends/transDoc!save.action" method="post" enctype="multipart/form-data">
				<input type="type" style="display: none ;" name="wordDoc"/><!-- WORD正文 -->
				<s:hidden id="id" name="model.docId"></s:hidden>
				<s:hidden id="deledAttachId" name="model.deledAttachId"></s:hidden>
				<s:hidden name="model.ddocEntryPeople"></s:hidden>
				<s:hidden id="rest3" name="model.rest3"></s:hidden>
				<s:hidden name="model.isdelete"></s:hidden>
				<table width="100%" border="0" cellspacing="0" cellpadding="00">
					<tr>
						<td height="40"
							style="FILTER:progid:DXImageTransform.Microsoft.Gradient(gradientType=0,startColorStr=#ededed,endColorStr=#ffffff);">
							<table width="100%" border="0" cellspacing="0" cellpadding="00">
								<tr>
									
									<td width="30%" align="left" >
									<img src="<%=frameroot%>/images/ico.gif" width="9" height="9">
										公文草拟
									</td>
									<td width="70%">
										<table border="0" align="right" cellpadding="00"
											cellspacing="0">
											<tr>
												<td>
													<a  class="Operation" href="javascript:doSave()" >
														<img src="<%=frameroot%>/images/baocun.gif" width="15"
															height="15" class="img_s"  >保存</a>
												</td>
											  <td width="5"></td>
											 <td>
													<a class="Operation"  href="javascript:doSubmit()">
														<img src="<%=frameroot%>/images/songshen.gif" width="15"
															height="15" class="img_s">提交</a>
												</td>
												<td width="5"></td>
												<td>
													<a class="Operation" onclick="closeWindow();" href="#">
														<img src="<%=frameroot%>/images/guanbi.gif" width="15"
															height="15" class="img_s">关闭</a>
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
				<table width="100%" border="0" cellpadding="0" 
					cellspacing="1" class=""><!--class="table1"-->
					<tr>
							<td id="fileOperation" style="display: none ;" valign="top" width="10%" height="100%">
										<table width="100%" align="center" border="0" cellpadding="0" cellspacing="1" class="table1">
											<tr>
												<td nowrap align="center" class="biao_bg1">文件操作</td>
											</tr>
											<tr onclick="TANGER_OCX_ShowDialog(1)" style="cursor: pointer; line-height: 20px;">
												<td nowrap class="td1">打开本地文件</td>
											</tr>
											<tr onclick="TANGER_OCX_ShowDialog(5);" style="cursor: pointer; line-height: 20px;">
												<td nowrap class="td1">设置页面布局</td>
											</tr>
											<tr onclick="TANGER_OCX_PrintDoc(true);" style="cursor: pointer; line-height: 20px;">
												<td nowrap class="td1">打印控制</td>
											</tr>
											<tr>
												<td nowrap align="center" class="biao_bg1">
													文件编辑
												</td>
											</tr>
											<tr onclick="TANGER_OCX_SetMarkModify(true);" style="cursor: pointer; line-height: 20px;">
												<td nowrap class="td1">
													保留痕迹
												</td>
											</tr>
											<tr onclick="TANGER_OCX_SetMarkModify(false);" style="cursor: pointer; line-height: 20px;">
												<td nowrap class="td1">
													不留痕迹
												</td>
											</tr>
											<tr onclick="TANGER_OCX_ShowRevisions(true);" style="cursor: pointer; line-height: 20px;">
												<td nowrap class="td1">
													显示痕迹
												</td>
											</tr>
											<tr onclick="TANGER_OCX_ShowRevisions(false);" style="cursor: pointer; line-height: 20px;">
												<td nowrap class="td1">
													隐藏痕迹
												</td>
											</tr>
											<tr onclick="TANGER_OCX_AddPicFromLocal(false);" style="cursor: pointer; line-height: 20px;">
												<td nowrap class="td1">
													插入图片
												</td>
											</tr>
											<tr onclick="TANGER_OCX_AddTemplateFromURL();" style="cursor: pointer; line-height: 20px;">
												<td nowrap class="td1">
													插入模板
												</td>
											</tr>
										</table>
									
						</td>
					<!--  <td height="10" valign="top"><a href="#" onclick="hidemenu();"><img name=pic src="<%=frameroot%>/images/jiantou_2.jpg" width="6" height="56" border="0"  title="点击打开菜单栏"/></a></td>
			           //<td style="background:#dae6f2">
			           -->
			            <td> 
							<DIV style="100%" class=tab-pane id=tabPane1>
								<SCRIPT type="text/javascript">
									tp1 = new WebFXTabPane( document.getElementById( "tabPane1") ,false );
								</SCRIPT>
								<DIV class=tab-page id=tabPage2>
									<H2 class=tab style="font-size:20px;">
										<font size=3>公文摘要</font>
									</H2>
									<table width="100%" border="0" cellpadding="0" cellspacing="1" class="table1">
										<tr>
											<td width="10%" height="28" class="biao_bg1" align="right">
												<span class="wz">发文单位：</span>
											</td>
											<td class="td1" colspan="3">
												<s:textfield title="发文单位" disabled="true" maxlength="25" id="docIssueDepartSigned" name="model.docIssueDepartSigned" cssStyle="width:83%"></s:textfield>
												<b><font color="red">*</font></b>
											</td>
										</tr>
										<tr>
											<td width="10%" height="28" class="biao_bg1" align="right">
												<span class="wz">发文文号：</span>
											</td>
											<td class="td1" colspan="3">
												<s:textfield title="发文文号" id="docCode" maxlength="25" name="model.docCode" cssStyle="width:83%"></s:textfield>
												<b><font color="red">*</font></b>
												<!-- <input type="button" onclick="getDocNumber()" class="input_bg" value="选择发文文号"/>-->
											</td>
										</tr>
										<tr>
											<td width="10%" height="28" class="biao_bg1" align="right">
												<span class="wz">公文标题：</span>
											</td>
											<td class="td1" colspan="3">
												<s:textfield title="公文标题" maxlength="70" id="docTitle" name="model.docTitle" cssStyle="width:83%"></s:textfield>
												<b><font color="red">*</font></b>
											</td>
										</tr>
										
										<!--
										<tr>
											<td width="10%" height="28" class="biao_bg1" align="right">
												<span class="wz">公文种类：</span>
											</td>
											<td class="td1">
												<s:select title="公文种类" id="docClass" name="model.docClass" cssStyle="width:85%" list="#{'0':'上行文','1':'平行文','2':'下行文'}" listKey="key" listValue="value"></s:select>
											</td>
											<td width="8%" height="28" class="biao_bg1" align="right">
												<span class="wz">秘密等级：</span>
											</td>
											<td class="td1">
												<s:if test="jjcdItems!=null&&jjcdItems.size()>0">
													<s:select title="秘密等级" id="docSecretLvl" name="model.docSecretLvl" cssStyle="width:63%" list="mmdjItems" listKey="dictItemName" listValue="dictItemName"></s:select>
												</s:if>
												<s:else>
													<select title="秘密等级" id="docSecretLvl" name="model.docSecretLvl"></select>
												</s:else>
											</td>
										</tr>
										-->
										<tr>
											<td width="10%" height="28" class="biao_bg1" align="right">
												<span class="wz">紧急程度：</span>
											</td>
											<!--  
											<td class="td1">
												<s:textfield title="紧急程度" maxlength="10" id="docEmergency" name="model.docEmergency" cssStyle="width:85%"></s:textfield>
											</td>
											-->
											<td class="td1">
												<s:if test="jjcdItems!=null&&jjcdItems.size()>0">
													<s:select title="紧急程度" id="docEmergency" name="model.docEmergency" cssStyle="width:85%" list="#{'':'','平急':'平急','加急':'加急','特急':'特急','特提':'特提'}"></s:select>
												</s:if>
												<s:else>
													<select title="紧急程度" id="docEmergency" name="model.docEmergency"></select>
												</s:else>
											</td>

											<td width="10%" height="28" class="biao_bg1" align="right">
												<span class="wz">成文日期：</span>
											</td>
											<td class="td1">
												<!--<input title="成文日期" disabled="disabled" value="<s:date name="model.docOfficialTime" format="yyyy-MM-dd"/>" id="docOfficialTime" name="model.docOfficialTime" style="width:63%"/>-->
												<strongit:newdate maxdate="${model.docEntryTime}" name="model.docOfficialTime" id="docOfficialTime" dateobj="${model.docOfficialTime}" width="63.6%" skin="whyGreen" isicon="true" dateform="yyyy-MM-dd"></strongit:newdate>
												<b><font color="red">*</font></b>
											</td>
										</tr>
										<%-- <s:if test="model.rest3!='001'"> --%>
										<tr>
											<td width="10%" height="28" class="biao_bg1" align="right">
												<span class="wz">主送单位：</span>
											</td>
											<td class="td1" colspan="3">
												<s:textarea disabled="true" maxlength="70" title="主送单位" id="docSubmittoDepart" name="model.docSubmittoDepart" cssStyle="width:83%;font-size:15px"></s:textarea>
												<b><font color="red">*</font></b>
												<input type="hidden" id="docSubmittoDepart_id" name="model.docSubmittoDepart_id" value="${model.docSubmittoDepart_id }"/>
												&nbsp;
												<input type="button" onclick="chooseDept('docSubmittoDepart','docSubmittoDepart_id','docCcDepart_id','1')" class="input_bg" value="选择主送单位"/>
											</td>
										</tr>
										<%-- </s:if>  --%>
										<tr>
											<td width="10%" height="28" class="biao_bg1" align="right">
												<span class="wz">抄送单位：</span>
											</td>
											<td class="td1" colspan="3">
												<s:textarea disabled="true" maxlength="70" title="抄送单位" id="docCcDepart" name="model.docCcDepart" cssStyle="width:83%;font-size:15px"></s:textarea>
												<b><font color="red">&nbsp;&nbsp;</font></b>
												&nbsp;
												<input type="hidden" id="docCcDepart_id" name="model.docCcDepart_id" value="${model.docCcDepart_id }"/>
												<input type="button" onclick="chooseDept('docCcDepart','docSubmittoDepart_id','docCcDepart_id','0')" class="input_bg" value="选择抄送单位"/>
											</td>
										</tr>
										<!--
										<tr>
											<td width="10%" height="28" class="biao_bg1" align="right">
												<span class="wz">会签人：</span>
											</td>
											<td class="td1">
												<s:textfield title="会签人" maxlength="32" id="docCountersigner" name="model.docCountersigner" cssStyle="width:85%"></s:textfield>
											</td>
											<td width="8%" height="28" class="biao_bg1" align="right">
												<span class="wz">签发人：</span>
											</td>
											<td class="td1">
												<s:textfield title="签发人" maxlength="24" id="docIssuer" title="签发人" name="model.docIssuer" cssStyle="width:63%"></s:textfield>
											</td>
										</tr>
										-->
										<tr>
											<td width="10%" height="28" class="biao_bg1" align="right">
												<span class="wz">创建时间：</span>
											</td>
											<td width="38%" class="td1">
												<input title="公文创建日期" disabled="disabled" value="<s:date name="model.docEntryTime" format="yyyy-MM-dd HH:MM:ss"/>" id="docEntryTime" name="model.docEntryTime" style="width:85%"/>
											</td>
											
											
											
											
											<td width="8%" height="28" class="biao_bg1" align="right">
												<span class="wz">公文状态：</span>
											</td>
											<td width="43%" class="td1">
												<s:i18n name="ApplicationMessage">
													<input title="公文状态" disabled="disabled" value="<s:text name="transdoc.state.draft"/>" id="docState" style="width:63%" />
												</s:i18n>
												<%--<!-- 草稿 -->
													<s:if test="model.docState==null||model.docState.equals(\"0\")">
														<s:i18n name="ApplicationMessage">
															<input title="公文状态" disabled="disabled" value="<s:text name="transdoc.state.draft"/>" id="docState" style="width:63%" />
														</s:i18n>
													</s:if>
													<s:if test="model.docState!=null&&model.docState.equals(\"1\")">
														<input title="公文状态" disabled="disabled" value="待签章" id="docState" style="width:63%" />
													</s:if>
													<s:if test="model.docState!=null&&model.docState.equals(\"2\")">
														<input title="公文状态" disabled="disabled" value="待分发" id="docState" style="width:63%" />
													</s:if>
													<s:if test="model.docState!=null&&model.docState.equals(\"3\")">
														<input title="公文状态" disabled="disabled" value="已发送" id="docState" style="width:63%" />
													</s:if>
												--%>
											</td>
										</tr>
										
										<tr>
											<td width="10%" height="28" class="biao_bg1" align="right">
												<span class="wz">签章处理：</span>
											</td>
											<td class="td1">
												<s:if test="model.docId == null">
													<s:if test="model.rest3 == 001">
														<input id="docSealIs" value="1" name="model.docSealIs" type="checkbox" />
													</s:if>
													<s:else>
														<input id="docSealIs" checked="checked" value="1" name="model.docSealIs" type="checkbox" />
													</s:else>
												</s:if>
												<s:else>
													<s:if test="model.docSealIs!=null&&model.docSealIs.equals(\"1\")">
														<input id="docSealIs" checked="checked" value="1" name="model.docSealIs" type="checkbox" />
													</s:if>
													<s:else>
														<input id="docSealIs" value="1" name="model.docSealIs" type="checkbox" />
													</s:else>
												</s:else>
											</td>
											<td width="8%" height="28" class="biao_bg1" align="right">
												<span class="wz">印发日期：</span>
											</td>
											<td class="td1">
												<strongit:newdate name="model.docprintSend" id="docprintSend" mindate="${model.docEntryTime}"  dateobj="${model.docprintSend}" width="63%" skin="whyGreen" isicon="true" dateform="yyyy-MM-dd"></strongit:newdate>
											</td>
										</tr>
										
										<%--<tr>
											<td width="11%" height="28" class="biao_bg1" align="right">
												短信通知<input id="duanxin" value="1" name="model.duanxin" type="checkbox" />：
											</td>
											<td class="td1" colspan="3">
													<s:textfield title="短信通知" maxlength="70" id="rest10" name="model.rest10" cssStyle="width:83%"></s:textfield>
											</td>
										</tr>--%>
									</table>
									<SCRIPT type="text/javascript">tp1.addTabPage( document.getElementById( "tabPage2" ) );</SCRIPT>

								</DIV>
								<DIV class=tab-page id=tabPage1>
									<H2 class=tab>
										<font size=3>公文正文</font>
									</H2>
								
									<s:include value="/common/goldgridOCX/version2.jsp"></s:include>
									<SCRIPT type="text/javascript">tp1.addTabPage( document.getElementById( "tabPage1" ) );</SCRIPT>
								</DIV>
								<DIV class=tab-page id=tabPage3>
									<H2 class=tab>
										<font size=3>附件</font>
									</H2>
									<table width="100%" border="0" cellpadding="0" cellspacing="1" class="table1">
										<tr>
											<td width="10%" height="28" class="biao_bg1" align="right">
												<span class="wz">公文附件：</span>
											</td>
											<td class="td1" colspan="3">
												<input type="file" onkeydown="return false;" id="attachMent" name="attachMent" class="multi required" style="width: 0px;" 
												 />
												<s:iterator value="model.ttransDocAttaches" status="statu" id="att">
													<div id=div<s:property value="docAttachId"/>>
														[<a onclick="deldbobj('<s:property value="docAttachId"/>');"
															href="#">删除</a>]
														<a href="<%=path %>/sends/transDoc!downLoad.action?model.deledAttachId=<s:property value="docAttachId"/>"
															target="myIframe"><s:property value="attachFileName" />
														</a>
													</div>
												</s:iterator>
											</td>
										</tr>
									</table>	
									<SCRIPT type="text/javascript">tp1.addTabPage( document.getElementById( "tabPage3" ) );</SCRIPT>
								</DIV>
							</DIV>
						</td>
					</tr>
				</table>
			</form>
	</body>
</html>
<iframe name="myIframe" style="display:none"></iframe>
