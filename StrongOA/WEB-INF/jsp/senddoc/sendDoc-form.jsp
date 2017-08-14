<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib uri="/tags/web-newdate" prefix="strong"%>
<%@taglib uri="/struts-tags" prefix="s"%>
<%@include file="/common/include/rootPath.jsp"%>
<%@include file="/common/eformOCX/version.jsp"%>
<html>
	<head>
		<%@include file="/common/include/meta.jsp"%>
		<title>发文处理单</title>
		<link href="<%=frameroot%>/css/properties_windows.css" type="text/css" rel="stylesheet">
		<script src="<%=path%>/common/js/jquery/jquery-1.2.6.js" type="text/javascript"></script>
		<script src="<%=path%>/common/js/upload/jquery.blockUI.js" language="javascript"></script>
		<script src="<%=path%>/common/js/common/common.js" type="text/javascript"></script>
		<script src="<%=path%>/common/js/common/map.js" type="text/javascript"></script>
		<script src="<%=path%>/oa/js/eform/eform.js" type="text/javascript"></script>
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
		var TANGER_OCX_Username = '${userName}';
        //初始化设置
		function initial(){
			if(FormInputOCX.OfficeActiveDocument){
				with (FormInputOCX.OfficeActiveDocument.Application) {//痕迹保留
				    UserName = '${userName}';
				}
			}
			if($("#taskId").val()!= ""){
				var author = FormInputOCX.GetFieldValue("T_OA_SENDDOC","SENDDOC_USER");
				if(!author || author == null || author == "" || author == "null"){
					FormInputOCX.SetFieldValue('T_OA_SENDDOC','SENDDOC_USER','${userLoginName}');
				}
				FormInputOCX.OfficeActiveDocument.TrackRevisions = true;
			}		
		}
	  	//由eform.js中定义的doNext()回调
		function goBack(){
			alert("发送成功！");
	   		window.returnValue = "OK" ;
	      	window.close();
		}

	    //保存表单成功以后的回调函数
	    function AfterSaveFormData(flag){
	    	alert("保存成功！");
	    	if(flag)
	    		window.close();
	    }			
	
	    function TANGER_OCX_ShowDialog(dType) {
	      var FormInputOCX = document.getElementById("FormInputOCX");
	      FormInputOCX.SetOfficePageActive();
	      try{
		      FormInputOCX.OfficeShowDialog(dType);      
	      }catch(e){}
	    }
		//打印控制
		function TANGER_OCX_PrintDoc(booValue) {
			var needprint;
			var printsign;
			var ret;
			$.getJSON("<%=root%>/senddoc/sendDoc!getNowDig.action",
		       {pkFieldValue:$("#pkFieldValue").val()},
		       function(msg){
		       		var total = msg[0].total;
		       		var printed = msg[0].printed;
		       		var reValue = OpenWindow("<%=root%>/senddoc/sendDoc!gotoPrintConfig.action?total="+total+"&printed="+printed,"400", "300", window);
		       		
		       		if(reValue == null || reValue == undefined){
		       			return ;
		       		}
		       		
		       		if(reValue=="false"){
		       			return;
		       		}else{
						var para=reValue.split(",");
						printsign=para[0];
						needprint=para[1];
		       		}
		       		var FormInputOCX = document.getElementById("FormInputOCX");
				    var TANGER_OCX_OBJ = FormInputOCX.OfficeControl;
				    TANGER_OCX_OBJ.FilePrint=true;
		       		if(printsign=="1"){//打印公章
			       		if((parseInt(needprint)+parseInt(printed))<=parseInt(total)){
			       			var sucprint="0";
			       			for(i=0;i<parseInt(needprint);i++){
				       			try{
				       				if(printsign=="0"){
					       				FormInputOCX.OfficeControl.SetSignsVisible('*',false,'',2);
								     	FormInputOCX.SetOfficePageActive();
								      	ret=FormInputOCX.OfficePrintOut(booValue);	
								      	FormInputOCX.OfficeControl.SetSignsVisible('*',true,'',2);
							      	}else{
								     	FormInputOCX.SetOfficePageActive();
								      	ret=FormInputOCX.OfficePrintOut(booValue);
							      	}
							      	
							      	if(ret==1){
							      		sucprint=i+1;
							      	}else{
							      		alert("您在打印第"+(i+1)+"份时失败！");
							      		break;
							      	}     	
				       			}catch(e){
				       				alert("您在打印第"+(i+1)+"份时出现异常！");
				       				break;
				       			}
			       			}
					       $.ajax({
						       type:"post",
						       url:"<%=root%>/senddoc/sendDoc!changePrintedNum.action",
						       data:{
									pkFieldValue:$("#pkFieldValue").val(),
									printed:(parseInt(printed)+parseInt(sucprint))
						       },
						       success:function(info){
						       		if(info=="true"){
									    /*if(confirm("是否提交下一处理人？")){
									      submitNext();
									    }*/
						       		}else{
						       			alert("对不起出现错误");
						       		}
						       }
					      });
			       		}else{
			       			alert("允许打印份数为"+total+",已打印份数为"+printed+",您已经不能打印"+needprint+"份了");
			       			return;
			       		}
		       		}else{//不打印公章,打印份数不做控制
		       				for(i=0;i<parseInt(needprint);i++){
				       			try{
					       				FormInputOCX.OfficeControl.SetSignsVisible('*',false,'',2);
								     	FormInputOCX.SetOfficePageActive();
								      	ret=FormInputOCX.OfficePrintOut(booValue);	
								      	FormInputOCX.OfficeControl.SetSignsVisible('*',true,'',2);
							      	if(ret==1){
							      		
							      	}else{
							      		alert("您在打印第"+(i+1)+"份时失败！");
							      		break;
							      	}     	
				       			}catch(e){
				       				alert("您在打印第"+(i+1)+"份时出现异常！");
				       				break;
				       			}
			       			}
		       		}
		       		TANGER_OCX_OBJ.FilePrint=false;
		       }
		      );
	    }
		
		//进入或退出痕迹保留状态，调用下面的两个函数
		function TANGER_OCX_SetMarkModify(boolvalue) {
			var FormInputOCX = document.getElementById("FormInputOCX");
			FormInputOCX.SetOfficePageActive();
			FormInputOCX.OfficeActiveDocument.TrackRevisions = boolvalue;
		}
		
		//显示/不显示痕迹
		function TANGER_OCX_ShowRevisions(boolvalue) {
			var FormInputOCX = document.getElementById("FormInputOCX");
			FormInputOCX.SetOfficePageActive();
			FormInputOCX.OfficeActiveDocument.ShowRevisions = boolvalue;
		}

		//清除痕迹
		function TANGER_OCX_AcceptAllRevisions(){
			var FormInputOCX = document.getElementById("FormInputOCX");
			var TANGER_OCX_OBJ = FormInputOCX.OfficeControl; 
			TANGER_OCX_OBJ.ActiveDocument.AcceptAllRevisions();//接受所有的修订
		}
		
		//从本地增加图片到文档指定位置
		//是否浮动文件,true为浮动，false为非浮动
		function TANGER_OCX_AddPicFromLocal(value){
			var FormInputOCX = document.getElementById("FormInputOCX");
			FormInputOCX.SetOfficePageActive();
		    var ret = FormInputOCX.OfficeAddPicFromLocal("", //路径
														true,//是否提示选择文件
														value,//是否浮动图片
														100,//如果是浮动图片，相对于左边的Left 单位磅
														100,
														1,
														100,
														1); //如果是浮动图片，相对于当前段落Top	
		}
		
		//插入模板
        function TANGER_OCX_AddTemplateFromURL(){
		   var FormInputOCX = document.getElementById("FormInputOCX");
		   FormInputOCX.SetOfficePageActive();
		   var ReturnStr=OpenWindow("<%=root%>/senddoc/sendDoc!templateTree.action","400", "350", window);
           if(ReturnStr){
           		var TANGER_OCX_OBJ = FormInputOCX.OfficeControl; 
				if(!TANGER_OCX_OBJ){//校验是否存在WORD
					return ;
				} 
				//保存WORD内容到本地
				TANGER_OCX_OBJ.SaveToLocal("c:\\tempword\\temp.doc",true);
				initWord();
				FormInputOCX.OfficeActiveDocument.TrackRevisions = false;
				//查找标签与电子表的映射关系.
				$.getJSON("<%=root%>/senddoc/sendDoc!readBookMarkInfo.action?timestamp="+new Date(),{formId:$("#formId").val()},function(ret){
					if(ret == "-1"){
						alert("读取标签与电子表单映射时异常!");
						return ;
					}
					TANGER_OCX_OBJ.AddTemplateFromURL("<%=basePath%>/doctemplate/doctempItem/docTempItem!opendoc.action?doctemplateId="+ReturnStr,true); 
	           		var doc =TANGER_OCX_OBJ.ActiveDocument;
					var bks = doc.Bookmarks;
					var bksCount = bks.Count;
					var contentBookMarkName = "" ;//正文的标签名称
					for(i=1;i<=bksCount ;i++){
						try{
							var name = bks(i).Name ;
							var value = TANGER_OCX_OBJ.GetBookmarkValue(name);
							TANGER_OCX_OBJ.SetBookmarkValue(name,"");//先清空原书签的值
							if(value.indexOf("正文")!=-1){
								contentBookMarkName = name ;
							}else{
								$.each(ret,function(i,json){
									if(name == json.bookMarkName){
										var controlValue = FormInputOCX.GetControlValue(json.componentName);
										if(!controlValue || controlValue == null){
											controlValue = FormInputOCX.GetFieldValue(json.tableName,json.fieldName);
										}
										if(controlValue != "普通"){
											TANGER_OCX_OBJ.SetBookmarkValue(name,controlValue);
										}
									}
								});
							}
						}catch(e){}
					} 
					if(contentBookMarkName != ""){//存在正文标签
	          	  		TANGER_OCX_OBJ.ActiveDocument.BookMarks(contentBookMarkName).Select();
	          	  		//TANGER_OCX_OBJ.AddTemplateFromLocal("",true);
	          	  		TANGER_OCX_OBJ.AddTemplateFromLocal("c:\\tempword\\temp.doc",false);
					}
				});
           }
        }
		
		//全屏手写签名
		function TANGER_OCX_DoHandSign2(){
			var FormInputOCX = document.getElementById("FormInputOCX");
			FormInputOCX.SetOfficePageActive();
			var ret = FormInputOCX.OfficeDoHandSign2 (TANGER_OCX_Username,//当前登录用户 必须
													  "", //SignKey
													  0,//left//可选参数
													  0,//top
													  0,//relative=0，表示按照屏幕位置批注
													  100); //缩放100%，表示原大小
		}
		
		//全屏手工绘图，与以上区别，不添加验证信息
		function TANGER_OCX_DoHandDraw2(){
		  var FormInputOCX = document.getElementById("FormInputOCX");
		  FormInputOCX.SetOfficePageActive();
		  var ret = FormInputOCX.OfficeDoHandDraw2(0,//left//可选参数
											       0,//top
											 	   0,//relative=0，表示按照屏幕位置批注
											       100); //缩放100%，表示原大小
		}
		
		//加盖本地电子印章
		function TANGER_OCX_AddSignFromLocal(){
			var FormInputOCX = document.getElementById("FormInputOCX");
			FormInputOCX.SetOfficePageActive();
			try{
				var ret = FormInputOCX.OfficeAddSignFromLocal(TANGER_OCX_Username,
															 "",
															 true,
															 0,
															 0,
															 "", 
															 1,
															 100,
															 1);
			}catch(e){}
			
		}
		
		//查看办理记录
	    function annal(){
		 var taskId = $("#taskId").val();
		 var audit = OpenWindow("<%=root%>/senddoc/sendDoc!annallist.action?taskId="+taskId, 
	                                   500, 450, window);
	    }
	    
	    var state = 1 ;
		//隐藏显示文件操作菜单栏
		function hidemenu(){
			if(state == 0){
				$("#fileOperation").hide();
				state = 1;
				document.pic.src="<%=root%>/images/ico/jiantou_2.jpg";
				document.pic.title="点击打开菜单栏";
			}else if(state == 1){
				$("#fileOperation").show();
				state = 0;
				document.pic.src="<%=root%>/images/ico/jiantou.jpg";
				document.pic.title="点击隐藏菜单栏";
			}
		}
    </script>
	</head>
	<base target="_self"/>
	<body class="contentbodymargin" oncontextmenu="return false;" onunload="resumeConSignTask();">
		<form id="form" name="form" action="<%=root %>/senddoc/sendDoc!save.action" method="post">
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
		<s:hidden id="userName" name="userName"></s:hidden>
		<!-- 流程名称 -->
		<s:hidden id="workflowName" name="workflowName"></s:hidden>
		<!-- 节点上挂接的工作流插件信息 -->
		<s:hidden id="pluginInfo" name="pluginInfo"></s:hidden>
		</form>
		<DIV id=contentborder align=center>
				<table width="100%" border="0" cellspacing="0" cellpadding="00">
					<tr>
						<td height="40">
							<table width="100%" border="0" cellspacing="0" cellpadding="00">
								<tr>
									<td>&nbsp;</td>
									<td >
						 		 	<img src="<%=frameroot%>/images/ico/ico.gif" width="9" height="9"alt="">&nbsp;
										发文处理单
									</td>
									<td>
										<table border="0" align="right" cellpadding="00"
											cellspacing="0">
											<tr>
												<td id="fullScreen" style="display: none;" >
													<a class="Operation" href="javascript:doFullScreen();">
														<img src="<%=root%>/images/ico/chakan.gif" width="15"
															height="15" class="img_s">全屏模式&nbsp;</a>
												</td>
												<td width="5">
													&nbsp;
												</td>
												<td id="td_startworkflow" style="display: none ;">
													<a class="Operation" href="javascript:showForm();"><img
															src="<%=root%>/images/ico/tianjia.gif" width="15"
															height="15" class="img_s">启动新流程&nbsp;</a>
												</td>
												<td width="5"></td>
												${returnFlag }
												<td id="toSave" >
													<a class="Operation" href="javascript:saveFormData(true);"><img
															src="<%=root%>/images/ico/baocun.gif" width="15"
															height="15" class="img_s">保存并关闭&nbsp;</a>
												</td>
												<td width="5"></td>
												<td id="toSave" >
													<a class="Operation" href="javascript:saveFormData(false);"><img
															src="<%=root%>/images/ico/baocun.gif" width="15"
															height="15" class="img_s">保存&nbsp;</a>
												</td>
												<td width="5"></td>
												<td >
													<a class="Operation" href="javascript:window.close();"><img
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
							</table>
						</td>
					</tr>
				</table>
				<table width="100%" border="0" cellpadding="0"
					cellspacing="1" class="table1">
					<tr>
						<td id="fileOperation" style="display: none ;" valign="top" width="10%" height="100%">
							${privilegeInfo }
						</td>
						<td height="10" width="1%" valign="top"><a href="#" onclick="hidemenu();"><img name=pic src="<%=root%>/images/ico/jiantou_2.jpg" width="6" height="56" border="0"  title="点击打开菜单栏"/></a></td>
						<td align="center">				  
			              <object height="850" width="100%" classid="clsid:750B2722-ADE6-446A-85EF-D9BAEAB8C423"
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
			              <td style="display: none ;">
			              <!-- 千航OFFICE控件 -->
				              <object id="TANGER_OCX_OBJ"
								classid="clsid:C9BC4DFF-4248-4a3c-8A49-63A7D317F404"
								codebase="<%=root%>/common/OfficeControl/OfficeControl.cab<%=OCXVersion%>"
								width="100%" height="100%">
								<param name="ProductCaption" value="思创数码科技股份有限公司">
								<param name="ProductKey" value="B339688E6F68EAC253B323D8016C169362B3E12C">
								<param name="BorderStyle" value="1">
								<param name="TitlebarColor" value="42768">
								<param name="TitlebarTextColor" value="0">
								<param name="TitleBar" value="false">
								<param name="MenuBar" value="false">
								<param name="Toolbars" value="true">
								<param name="IsResetToolbarsOnOpen" value="true">
								<param name="IsUseUTF8URL" value="true">
								<param name="IsUseUTF8Data" value="true">
								<span style="color: red">不能装载文档控件。请在检查浏览器的选项中检查浏览器的安全设置。</span>
								</object>
							</td>
					</tr>
				</table>
		</DIV>
	</body>
</html>
