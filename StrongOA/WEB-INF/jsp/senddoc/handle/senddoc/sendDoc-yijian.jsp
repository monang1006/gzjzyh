<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<jsp:directive.page import="java.text.SimpleDateFormat,com.strongit.oa.util.GlobalBaseData"/>

<%@ taglib uri="/struts-tags" prefix="s"%>
<%@taglib uri="/tags/web-remind" prefix="strong"%>
<%@include file="/common/eformOCX/version.jsp"%>
<%@include file="/common/include/rootPath.jsp"%>
<html>
	<head>
		<title>反馈意见</title>
		<%@include file="/common/include/meta.jsp" %>
		<LINK href="<%=frameroot%>/css/properties_windows.css" type=text/css rel=stylesheet>
		<link type="text/css" rel="stylesheet" href="<%=path%>/common/js/tabpane/css/luna/tab.css" />
		<script type="text/javascript" src="<%=path%>/common/js/tabpane/js/tabpane.js"></script>
		<script src="<%=path%>/common/js/jquery/jquery-1.2.6.js" type="text/javascript"></script>
		<script language="javascript" src="<%=path%>/common/js/upload/jquery.MultiFile.js"></script>
		<script src="<%=path%>/common/js/upload/jquery.blockUI.js" language="javascript"></script>
		<script src="<%=path%>/common/js/common/common.js" type="text/javascript"></script>
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
		
			if(!loaded){
				var TANGER_OCX_OBJ = document.getElementById("TANGER_OCX_OBJ");
				var width=screen.availWidth-55;
	   			var height=screen.availHeight-130;
			
			  		var type = TANGER_OCX_OBJ.DocType;//得到OFFICE类型.
				  	TANGER_OCX_OBJ.OpenFromURL(basePath + "sends/transDoc!openEmptyDocFromUrl.action?docType=1");
				  	TANGER_OCX_OBJ.WebFileName='newFile';
				
			  	TANGER_OCX_OBJ.width = width;
			  	TANGER_OCX_OBJ.height = height;
				
				loaded = true;
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
		

		
		//提交
		function doSubmit(){
			
					
						var TANGER_OCX_OBJ = document.getElementById("TANGER_OCX_OBJ");
						openDoc();
						if(TANGER_OCX_OBJ == ""){
							loaded = false;
							openDoc();
						}
						var docid = "${docid}";
				
			        	var ret = TANGER_OCX_OBJ.SaveToUrl("<%=root%>/senddoc/sendDoc!doyijian.action?docid=${docid}",
			                                           "wordDoc",
			                                           "",
			                                           "newFile.doc",
			                                           "form");
			             
			            if(ret.substring(0,1) == "0"){
			           		//alert("操作成功！");
			           		//window.returnValue = "0";
			           		returnValue = "1";
						
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
	   //选择机构
	   function chooseDept(deptName,deptId){
	   	  var info = new Array();
	   	  
	   	  info[0] = document.getElementById(deptId).value;
	   	  info[1] = document.getElementById(deptName).value;
	   	  var ret = OpenWindow('<%=root%>/sends/transDoc!chooseDept.action', '600', '400', info);
	   	  if(ret && ret != null){
	   		var id = ret[0];
	   		var name = ret[1];
	   		document.getElementById(deptId).value = id;
	   		document.getElementById(deptName).value = name;
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
	<body class=contentbodymargin oncontextmenu="return false;" scroll="auto" background="#ff919899" onload="openDoc();">
			<form id="form" name="form" action="<%=root %>/sends/transDoc!save.action" method="post" enctype="multipart/form-data">
				<input type="type" style="display: none ;" name="wordDoc"/><!-- WORD正文 -->
				
				
				<table width="100%" border="0" cellspacing="0" cellpadding="00">
					<tr>
						<td height="40"
							style="FILTER:progid:DXImageTransform.Microsoft.Gradient(gradientType=0,startColorStr=#ededed,endColorStr=#ffffff);">
							<table width="100%" border="0" cellspacing="0" cellpadding="00">
								<tr>
									<!--<td width="5%" align="center">
										&nbsp;
									</td>
									-->
									<td width="30%">
									<img src="<%=frameroot%>/images/ico.gif" width="9" height="9">
										反馈意见
									</td>
									<td width="70%">
										<table border="0" align="right" cellpadding="00"
											cellspacing="0">
											<tr>
												
											 <td>
													<a class="Operation" onclick="doSubmit();" href="#">
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
				<table width="100%"><!--class="table1"-->
					<tr>
					
					<!--  <td height="10" valign="top"><a href="#" onclick="hidemenu();"><img name=pic src="<%=frameroot%>/images/jiantou_2.jpg" width="6" height="56" border="0"  title="点击打开菜单栏"/></a></td>
			           //<td style="background:#dae6f2">
			           -->
			            <td> 
							<DIV style="100%" >
								
								
									<s:include value="/common/goldgridOCX/version2.jsp"></s:include>
									
								
							
							</DIV>
						</td>
					</tr>
				</table>
			</form>
	</body>
</html>
<iframe name="myIframe" style="display:none"></iframe>
