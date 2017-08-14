<%@ page contentType="text/html; charset=utf-8"%>
<%@include file="/common/include/rootPath.jsp"%>
<%
String designRoot = path + "/workflow/designer";
%>
<html xmlns:v="urn:schemas-microsoft-com:vml">
	<head>
	
		<title>工作流模型设计器</title>
		<meta http-equiv="X-UA-Compatible" content="IE=6" />
		<link type="text/css" rel="StyleSheet"
			href="<%=designRoot%>/css/main.css" />
		<link type="text/css" rel="StyleSheet"
			href="<%=designRoot%>/css/contextmenu.css" />
		<%-- <LINK
			href="<%=request.getContextPath()%>/common/frame/css/properties_windows.css"
			type=text/css rel=stylesheet> --%>
<script type="text/javascript" src="<%=designRoot%>/js/jquery/jquery-1.4.2.js"></script>
		
		<script src="<%=designRoot%>/js/workflow/const.js"></script>
		<!--系统常量-->
		<script src="<%=designRoot%>/js/workflow/contextmenu.js"></script>
		<!--右键菜单-->
		<script src="<%=designRoot%>/dtree/dtree.js"></script>
		<!--树-->
		<script src="<%=designRoot%>/js/workflow/bar.js"></script>
		<!--工具栏类-->
		<script src="<%=designRoot%>/js/workflow/property.js"></script>
		<!--属性类-->
		<script src="<%=designRoot%>/js/workflow/changeproperty.js"></script>
		<!--更改属性类-->
		<script src="<%=designRoot%>/js/workflow/draw.js"></script>
		<!--图形绘画类-->
		<script src="<%=designRoot%>/js/workflow/elements.js"></script>
		<!--图形元素类-->
		<script src="<%=designRoot%>/js/workflow/observer.js"></script>
		<!--观察类-->
		<script src="<%=designRoot%>/js/workflow/createxml.js"></script>
		<!--导入导出xml文件-->
		<script src="<%=designRoot%>/js/workflow/repaint.js"></script>
		<!--更新画布-->
		<script src="<%=designRoot%>/js/workflow/interface.js"></script>
		<!--公共接口-->
		

<script type="text/javascript" src="<%=designRoot%>/js/jquery/jquery.timer.js"></script>
<script type="text/javascript" src="<%=designRoot%>/js/workflow/clonenode.js"></script>		
		
		<link href="<%=designRoot%>/dtree/dtree.css" type="text/css"
			rel="stylesheet">
		<script>
		 //var doc = new ActiveXObject("Msxml2.DOMDocument"); //ie5.5+,CreateObject("Microsoft.XMLDOM") 		 
		 var doc = createXmlDom();
		 doc.async = false ;
		 doc.validateOnParse = true;
		
		var pragramdoc;
		var noderoot;
		var lineroot;
		
		//添加根节点
		var root = doc.createNode(1,"process-definition","");
	    //创建属性 
	    var r = doc.createAttribute("xmlns:xsi"); 
	        r.value="urn:jbpm.org:jpdl-3.1"; 
	
	        //添加属性 
	        root.setAttributeNode(r); 
	    //创建第二个属性     
	    var r = doc.createAttribute("name"); 
	        r.value=processName; 
	         
	        //添加属性 
	        root.setAttributeNode(r);
	    doc.appendChild(root);   
	</script>
	</head>
	<base target="_self" />
	<body class="whole" oncontextmenu="return false;" >
		<!-- 流程自定义验证扩展页面 -->
		<jsp:include page="ext/flow_validate_ext.jsp" />
		
		<form id="processForm"
			action="<%=path%>/workflowDesign/action/processDesign!fileExport.action" target="tempframe"
			method="POST">
			<input type="hidden" name="processfile" id="processfile" value="" />
			<input type="hidden" name="processName" id="processName" value="" />
			<input type="hidden" name="processType" id="processType" value="" />
			<input type="hidden" id="pfId" value="${pfId}"/>
		</form>
		<form id="export-tpl-form" action="<%=path%>/workflowDesign/action/processDesign!exportTpl.action" target="tempframe"
			method="POST">
			<input type="hidden" name="processfile" id="process-tpl" value="" />
			<input type="hidden" name="processName" id="process-name" value="" />
		</form>
		<table width="100%" height="100%" id="main-table"
			style="position:absolute;left:0;top:0" border="0" cellpadding="0"
			cellspacing="0">
			<tr style="display:none">
				<td>
					<select name="sltObjArray" style="display:none">
					</select>
					<input name="iptCtlNum" value=1 style="display:none" />
				</td>
			</tr>
			<!-- bgcolor="#00659C" -->
			<tr height="25">
				<td class="toolTD" align="left" valign="middle" id=td_title>
					&nbsp;
					<font style="font-size:12pt">工作流模型设计器  ${wfversion }</font>
				</td>
			</tr>
			<tr height="25">
				<td align="left" valign="middle" class="toolTD" id=td_toolbar>
					<!-- <input type="submit" name="Submit2" value="查看流程" onclick="test()"> -->
					&nbsp;&nbsp;<a href='#' title='查看流程' onclick='test()'><img src='<%=designRoot%>/images/workflow/chakan.gif' border='0' align='absmiddle'/>&nbsp;查看流程</a>
					<script>
						if("${type}" != "edit"){
					  		var html = "<img src='<%=designRoot%>/images/workflow/split.gif' align='absmiddle'>"
					  			 + "<a href='#' title='修改流程' onclick='editProcess(\"<%=path%>/workflowDesign/action/processDesign.action?pfId=${pfId}\")'><img src='<%=designRoot%>/images/workflow/bianji.gif' border='0' align='absmiddle'/>&nbsp;修改流程</a>"
					  			 + "<img src='<%=designRoot%>/images/workflow/split.gif' align='absmiddle'>"
					  			 + "<a href='#' title='保存流程' onclick='saveFile()'><img src='<%=designRoot%>/images/workflow/baocun.gif' border='0' align='absmiddle'/>&nbsp;保存流程</a>"
					  			 + "<img src='<%=designRoot%>/images/workflow/split.gif' align='absmiddle'>"
					  			 + "<a href='#' title='高级设置' onclick='seniorSet()'><img src='<%=designRoot%>/images/workflow/set_config.gif' border='0' align='absmiddle'/>&nbsp;高级设置</a>"
					  			 + "<img src='<%=designRoot%>/images/workflow/split.gif' align='absmiddle'>"
					  			 + "<a href='#' title='导出模型' onclick='saveDoc()'><img src='<%=designRoot%>/images/workflow/daochu.gif' border='0' align='absmiddle'/>&nbsp;导出模型</a>"
					  			 + "<img src='<%=designRoot%>/images/workflow/split.gif' align='absmiddle'>"
					  			 + "<a href='#' title='导入模型' onclick='loadDoc()'><img src='<%=designRoot%>/images/workflow/daoru.gif' border='0' align='absmiddle'/>&nbsp;导入模型</a>"
					  			 + "<img src='<%=designRoot%>/images/workflow/split.gif' align='absmiddle'>"
					  			 + "<a href='#' title='导出子模板' onclick='exportTpl()'><img src='<%=designRoot%>/images/workflow/daochu.gif' border='0' align='absmiddle'/>&nbsp;导出子模板</a>"
					  			 + "<img src='<%=designRoot%>/images/workflow/split.gif' align='absmiddle'>"
					  			 + "<a href='#' title='导入子模板' onclick='importTpl()'><img src='<%=designRoot%>/images/workflow/daoru.gif' border='0' align='absmiddle'/>&nbsp;导入子模板</a>"
					  			 + "<img src='<%=designRoot%>/images/workflow/split.gif' align='absmiddle'>"
					  			 + "<a href='#' title='复制' id='all-copy'><img src='<%=designRoot%>/images/workflow/fuzhi.gif' border='0' align='absmiddle'/>&nbsp;复制</a>"
					  			 + "<img src='<%=designRoot%>/images/workflow/split.gif' align='absmiddle'>"
					  			 + "<a href='#' title='粘贴' id='all-paste'><img src='<%=designRoot%>/images/workflow/zhantie.gif' border='0' align='absmiddle'/>&nbsp;粘贴</a>"
					  			 + "<img src='<%=designRoot%>/images/workflow/split.gif' align='absmiddle'>"
					  			 + "<a href='#' title='删除' id='choose-delete'><img src='<%=designRoot%>/images/workflow/delete.gif' border='0' align='absmiddle'/>&nbsp;删除</a>"
							document.write(html);
						}else{
							currentStatus = "edit";
					  		var html = "<img src='<%=designRoot%>/images/workflow/split.gif' align='absmiddle'>"
					  			+ "<a href='#' title='保存流程' onclick='edit()'><img src='<%=designRoot%>/images/workflow/baocun.gif' border='0' align='absmiddle'/>&nbsp;保存流程</a>";
							document.write(html);		
						}
					   </script>
					<img src='<%=designRoot%>/images/workflow/split.gif' align='absmiddle'>
					<a href='#' title='上移' onclick="moveGraph('top')"><img src='<%=designRoot%>/images/workflow/shangyi.gif' border='0' align='absmiddle'/>&nbsp;上移</a>
					<img src='<%=designRoot%>/images/workflow/split.gif' align='absmiddle'>
					<a href='#' title='下移' onclick="moveGraph('bottom')"><img src='<%=designRoot%>/images/workflow/xiayi.gif' border='0' align='absmiddle'/>&nbsp;下移</a>
					<img src='<%=designRoot%>/images/workflow/split.gif' align='absmiddle'>
					<a href='#' title='左移' onclick="moveGraph('left')"><img src='<%=designRoot%>/images/workflow/ht.gif' border='0' align='absmiddle'/>&nbsp;左移</a>
					<img src='<%=designRoot%>/images/workflow/split.gif' align='absmiddle'>
					<a href='#' title='右移' onclick="moveGraph('right')"><img src='<%=designRoot%>/images/workflow/qj.gif' border='0' align='absmiddle'/>&nbsp;右移</a>
					<img src='<%=designRoot%>/images/workflow/split.gif' align='absmiddle'>
					<a href='#' title='模型验证' id='btn-validate'><img src='<%=designRoot%>/images/workflow/validate.png' border='0' align='absmiddle'/>&nbsp;模型验证</a>
					<img src='<%=designRoot%>/images/workflow/split.gif' align='absmiddle'>
				</td>
			</tr>

			<tr height="100%">
				<td class="toolTD">
					<table style="margin:0px;width:100%;height:100%;" border="0"
						cellpadding="2" cellspacing="2">
						<tr>
							<td width="25" class="toolTD">
								<div id="selectItem"
									style="width:100%;height:100%;padding-top:10px;margin-left:2px">
								</div>
							</td>
							<td width="170" align="left" valign="top" id="seniorSet"
								style="display:none" class="toolTD">
								<!--左边的属性显示菜单-->
								<table height=25 cellspacing=0 cellpadding=0 width="170"
									border=0>
									<tbody>
										<tr>
											<td valign=middle nowrap align=left height=25
												style="padding-left:10px" class="toolTD">
												属性-
												<label id=lbCtlName></label>
											</td>
										</tr>
									</tbody>
								</table>
								<table cellspacing=0 cellpadding=0 width="170" border=0
									height="100%">
									<tbody>
										<tr>
											<td width="100%" valign=middle align=left height="100%"
												class="toolTD">
												<div id="tbProperty"
													style="border:1 solid gray;overflow:auto;background-color:white;height:95%;width:168px;padding-top:10px;padding-left:10px;">
												</div>
											</td>
										</tr>
									</tbody>
								</table>
							</td>
							<td align="left" valign="top" class="toolTD">
								<!--流程图-->
								<div id=drawCanvas onselectstart="return false"
									style='overflow:auto;width:100%;height:100%;border:1 solid gray;color:black;background-image:url("<%=designRoot%>/images/workflow/bg.png")'>
									<script>
							fCtlNum = iptCtlNum.value;  //初始控件数量						
							var snode=new startnode();
							snode.addxml();
						</script>
								</div>
							</td>
						</tr>
					</table>
				</td>
			</tr>
		</table>
		<iframe id='tempframe' name='tempframe' height="0px" width="0px"></iframe>
	</body>
</html>

<script language="JavaScript" for="document" event="onkeydown">
if(window.event.ctrlKey){
	isCtrlKeyDown = true;
	//fSelectedObjArray = [];
	
	if(window.event.keyCode == 67){
		copyNode();
	}
	else if(window.event.keyCode == 86){
		pasteNode();
	}
}

//删除功能
if(window.event.keyCode == 46){
	if("${type}" != "edit" && hasNeedDelObj(fSelectedObjArray) && confirm("是否要删除？")){
		deleteMenuFunc(fSelectedObjArray);
	}
}
</script>
<script language="JavaScript" for="document" event="onkeyup">
isCtrlKeyDown = false;
</script>


<!------------------------------------------------------------------------------
|                        绘图,拖动   			          					       |
|                            												   |
|						        											   |
------------------------------------------------------------------------------->
<script language="JavaScript">


var isCtrlKeyDown = false;

var hasCopyObj = false;

var pasteCount = 0;

function copyNode(e){
	hasCopyObj = true;
	//exeCopyNode(selectedNode);
}

$("#all-copy").click(function(){
	copyNode();
	pasteCount = 0;
});

$("#all-paste").click(function(){
	pasteNode();
	pasteCount++;
});

$("#choose-delete").click(function(){
	if("${type}" != "edit" && hasNeedDelObj(fSelectedObjArray) && confirm("是否要删除？")){
		deleteMenuFunc(fSelectedObjArray);
	}
});

$("#btn-validate").click(function(){
	var isValidated = processValidate();
	if(isValidated){
		alert("流程模型正确。");
	}
});

function copyLine(e){
	exeCopyLine(selectedNode);
}

function hasNeedDelObj(fSelectedObjArray){
	var hasObj = false;
	
	var len = fSelectedObjArray.length;
	if(len == 1){
		if(fSelectedObjArray[0].ctlType != CNST_CTLTYPE_INITIAL){
			hasObj = true;
		}
	}
	else if(len > 1){
		hasObj = true;
	}
	return hasObj;
}


	/**
	* onmousedown 绘图,选择
	*/
	function drawCanvas.onmousedown(){
		//zw
		if(window.event.button == 2){
			leftToolBar.clickBtn(0);
		}
		
		if(currentStatus == "edit"){
			var selectDrawState = new SelectDrawState();
			selectDrawState.clickEvent = event;
			drawContext.setDrawState(selectDrawState);
        	drawCanvas.style.cursor='default';
			leftToolBar.clickBtn(0);  //按下左边工具栏第一个按钮						
		}
		else{
			//add by caidw	防止画线时鼠标移出画布再回到画布是点击产生悬浮的迁移线
			if (fDragApproved==true&&fSelectedObj!=null) {
				moveObjToline(fSelectedObj); //跟表格对齐
				controlSubject.notifyObservers(); //通知所有的观察者
			}else if(window.event.button==1 && fAddLineFlag==2){//确定连接尾节点
				var mX = window.event.clientX;
				var mY = window.event.clientY;
				var sNode = getNodeByPoint(mX, mY);
				fSelectedObj.nodeclass.drawend(sNode);
				if(fSelectedObj.ctlType==CNST_CTLTYPE_LINE){
					fSelectedObj.fromelement.nodeclass.addline(fSelectedObj);
					propertyListSubject.notifyObservers();
				}							
			}else if(fDragApproved==3){//移动连接
			
			}
			fDragApproved=false;
			
			endSelRect(event);
			clearSelRect();
			//end by caidw--------------------------
			
	        if ( fLiftButton=="sel" ) {						//选择
				var selectDrawState = new SelectDrawState();
				selectDrawState.clickEvent = event;
				drawContext.setDrawState(selectDrawState);
				//tag=drawContext.draw(drawCanvas,event);
				
				//zw
				startSelRect(window.event);
				
				//$("#display-debug").html(event.x+"--"+event.y);
				
	        } else if ( fLiftButton=="stat" ) {					//状态
				drawContext.setDrawState(new StatDrawState());
	        } else if ( fLiftButton=="task" ) {					//任务
				drawContext.setDrawState(new TaskDrawState());
	        } else if ( fLiftButton=="condition" ) {					//条件
				drawContext.setDrawState(new ConditionDrawState());
	        } else if ( fLiftButton=="end" ) {					//结束
				drawContext.setDrawState(new EndDrawState());
	        } else if ( fLiftButton=="joins" ) {					//合并
				drawContext.setDrawState(new JoinDrawState());
	        } else if ( fLiftButton=="splits" ) {					//分支
				drawContext.setDrawState(new SplitDrawState());
	        } else if ( fLiftButton=="superstate" ) {					//超状态
				drawContext.setDrawState(new SuperStateDrawState());
	        } else if ( fLiftButton=="node" ) {					//node
				drawContext.setDrawState(new NodeDrawState());
	        } else if ( fLiftButton=="processstate" ) {					//子流程
				drawContext.setDrawState(new ProcessStateDrawState());
	        } else if ( fLiftButton=="line" ) {					//连接
	        	if(event.button == 1){
					var lineDrawState = new LineDrawState();
					lineDrawState.clickEvent = event;
					drawContext.setDrawState(lineDrawState);
					fAddLineFlag = 1;
	        	}
	        } else {
				alert("请选择左边工具栏进行绘图");
				return;
			}
		}
	

		
		var tag=drawContext.draw(drawCanvas,event);
		
		
		if (tag == true) { //如果返回为真
        	drawCanvas.style.cursor='default';
			//leftToolBar.clickBtn(0);  //按下左边工具栏第一个按钮
            controlSubject.notifyObservers(); //通知所有的观察者
            
            //zw
            if(fLiftButton=="sel"){
            	selectedNode = event.srcElement;
            	selectedObj = fSelectedObj;
            	
				hasCopyObj = false;           	
            }
		}
	}


	/**
	* onmousemove 改变被拖动元素在页面上的位置
	*/
	function drawCanvas.onmousemove() {
		
		//zw
		moveSelRect(window.event);
		
		if(!isSelRect){
	  		if (event.button==1&&fDragApproved==true&&fSelectedObj!=null) {
				//移动到当前坐标
	    		fSelectedObj.style.pixelLeft=event.clientX+fx;
	    		fSelectedObj.style.pixelTop=event.clientY+fy;
				//是否超出范围
				var rx = fSelectedObj.style.pixelLeft;
				var ry = fSelectedObj.style.pixelTop;
				if(	rx<0 ) fSelectedObj.style.left = 0;
				if( ry<0 ) fSelectedObj.style.top = 0;
				//通知拖动观察者
				movecontrolSubject.notifyObservers();
	    		return false;
	   		}
	   		// add yubin start
			else if(fAddLineFlag==2){
				//连接到当前坐标
				fSelectedObj.nodeclass.checkend(event);
			}
	   		else if(event.button==1&&fDragApproved==3&&fSelectedObj!=null){

				var scrolltop, scrollleft, w, h; 
	     
				if (document.documentElement && document.documentElement.scrollTop) { 
					scrolltop = document.documentElement.scrollTop; 
					scrollleft = document.documentElement.scrollLeft; 
					w = document.documentElement.scrollWidth; 
					h = document.documentElement.scrollHeight; 
				} else if (document.body) { 
					scrolltop = document.body.scrollTop; 
					scrollleft = document.body.scrollLeft;
					w = document.body.scrollWidth; 
					h = document.body.scrollHeight; 
				}
				var xx=event.clientX-getLeft(drawCanvas);
				var yy=event.clientY-getTop(drawCanvas);			
				xx=xx<0?0:xx;
				yy=yy<0?0:yy;
				if(fSelectedObj.movetype=="1"){
					fSelectedObj.posArray[6]=xx;
					fSelectedObj.posArray[7]=yy;
				}else if(fSelectedObj.movetype=="2"){
					fSelectedObj.posArray[8]=xx;
					fSelectedObj.posArray[9]=yy;
				}
				movecontrolSubject.notifyObservers();
				
	    		return false;  			
	   		}
	   		// add yubin end
		}
	}

	/**
	* onmouseup 鼠标左键放开时,拖动停止
	*/
	function drawCanvas.onmouseup() {
					
		if (fDragApproved==true&&fSelectedObj!=null) {
			moveObjToline(fSelectedObj); //跟表格对齐
			controlSubject.notifyObservers(); //通知所有的观察者
		}else if(window.event.button==1 && fAddLineFlag==2){//确定连接尾节点
			//zw
			var mX = window.event.clientX;
			var mY = window.event.clientY;
			var sNode = getNodeByPoint(mX, mY);
			fSelectedObj.nodeclass.drawend(sNode);
			if(fSelectedObj.ctlType==CNST_CTLTYPE_LINE){
				fSelectedObj.fromelement.nodeclass.addline(fSelectedObj);
				propertyListSubject.notifyObservers();
			}							
		}else if(fDragApproved==3){//移动连接
		
		}
		fDragApproved=false;
		
		//zw
		endSelRect(event);
		clearSelRect();
	}
	
	//zw
	document.getElementById("drawCanvas").oncontextmenu = function(){
		if("${type}" != "edit" && hasCopyObj){
			drawCanvasMenu();
		}
	}
	
	//zw
	function getNodeByPoint(mouseX, mouseY){
		var selXy = [mouseX, mouseY, mouseX, mouseY];
		var node = null;
		
		$("roundrect").each(function(){
			var objXy = [];
			objXy.push($(this).offset().left);
			objXy.push($(this).offset().top);
			objXy.push($(this).offset().left + $(this).width());
			objXy.push($(this).offset().top + $(this).height());
			
			if(hasInSelRect(selXy, objXy)){
				node = this;
			}
		});

		return node;
	}
</script>





<!------------------------------------------------------------------------------
|                        系统对象的初始化			          				   |
|                            												   |
|						        											   |
------------------------------------------------------------------------------->
<script language="JavaScript">

	/**
	 * 初始化流程对象
	 */
	 var model = new processModel();

	/**
	* 创建左边的工具条
 	*/
	var leftToolBar = new ToolBar("sel,|,task,|,condition,|,node,|,processstate,|,end,|,line,|",0,0,0,0);
	leftToolBar.clickBtn(0);  //按下左边工具栏第一个按钮
	function leftToolBarClick(aBtnName) {
		fLiftButton = aBtnName;
        if ( fLiftButton=="line" ) {	//画线
        	fAddLineFlag=1;
        } else {
			fAddLineFlag=0;
            drawCanvas.style.cursor='default';
		}
	}

	/**
	* 创建控件主题对象
 	*/
	var controlSubject = new ControlSubject();
	var propertyLabelObserver = new PropertyLabelObserver();//加入 右边的属性标签
	propertyLabelObserver.setObject(document.all.lbCtlName);
	controlSubject.attach(propertyLabelObserver);
	controlSubject.attach(new PropertyListObserver());//加入 右边的属性列表
	controlSubject.attach(new focusCtlObserver());//加入 获得焦点
	controlSubject.attach(new LinesObserver());//加入 线条重绘


	/**
	* 创建控件主题对象 用于拖动中  轻量级的主题
 	*/
	var movecontrolSubject = new ControlSubject();
	movecontrolSubject.attach(new LinesObserver());//加入 线条重绘

	/**
	* 创建画图环境对象
 	*/
	var drawContext = new DrawContext();

	/**
	* 创建属性显示环境对象
 	*/
	var	propertyContext = new PropertyContext();
	
	/**
	* 动态修改xml时使用	
	*/
	var propertyListSubject=new ControlSubject();
	propertyListSubject.attach(new PropertyListObserver());//加入 右边的属性列表


</script>


<!------------------------------------------------------------------------------
|                        右键菜单			          					           |
|                            												   |
|						        											   |
------------------------------------------------------------------------------->

<script language="JavaScript">
	//初始化右键菜单
	ContextMenu.intializeContextMenu();


</script>

<script>
	function test(){
		var dialog = window.showModalDialog(systemroot+"/flow_condition.jsp", window, "dialogWidth:673px; dialogHeight:560px; center:yes; help:no; resizable:no; status:no") ;
	}
	
	function seniorSet(){
		if(document.getElementById("seniorSet").style.display == "block"){
			document.getElementById("seniorSet").style.display = "none";
		}else{
			document.getElementById("seniorSet").style.display = "block";
		}
	}

	function saveFile() {
		var str = saveProcess();
		if(str == ""){
			//alert("模型中至少要有一个结束节点！");
			return false;
		}
		document.getElementById("processfile").value = str;
		document.getElementById("processName").value = processName;
		document.getElementById("processType").value = processType.split(",")[1];
		var form = document.getElementById("processForm");
		window.event.returnValue = true;
		form.action = "<%=path%>/workflowDesign/action/processDesign!save.action?saveType=save";
		form.submit();
		
	}
	
	function edit() {
		var str = saveProcess();
		if(str == ""){
			//alert("模型中至少要有一个结束节点！");
			return false;
		}
		document.getElementById("processfile").value = str;
		document.getElementById("processName").value = processName;
		document.getElementById("processType").value = processType.split(",")[1];
		var form = document.getElementById("processForm");
		if("${wfId}" != ""){
			form.action = "<%=path%>/workflowDesign/action/processHistoryVersion!save.action?wfId=${wfId}";
		}
		else{
			form.action = "<%=path%>/workflowDesign/action/processDesign!save.action?saveType=edit";
		}
		window.event.returnValue = true;
		form.submit();
		
	}
	
</script>
<script type="text/javascript">
		window.onload = function(){
			processName = "${processName}";
			document.getElementById("process-name").value = processName;
			document.getElementById("processName").value = processName;
			if("${type}" == "add"){
				alert("请先为流程设置流程类型和流程启动表单！");
				initProcess('<%=path%>/workflowDesign/action/processDesign.action');
			}else{
				loadProcess("${processfile}");			
			}
		}
		
	  function onbeforeunload_handler(){
  		  var n = window.event.screenX - window.screenLeft;   
             var b = n > document.documentElement.scrollWidth-50;   
             if(b && window.event.clientY < 0 || window.event.altKey)   
             {    
                 window.event.returnValue = "是否放弃对当前流程的修改？";     
             } 
	  } 
			window.onbeforeunload = onbeforeunload_handler;

	</script>
<!--右键菜单函数-->
<script src="<%=designRoot%>/js/workflow/contextmenuFunc.js"></script>
<script>
/**
 * 页面每隔5分钟向后台发送一次Request请求，以保持与服务器的连接，防止在设计流程的过程中发生连接过期
 */
var timer = $.timer(300000, function(){ 
	$.ajax({url:"<%=path%>/workflowDesign/action/processDesign!connect.action", async:false})
});

/**
 * 在页面卸载时停止定时器
 */
$(window).bind("unload", function(){
	timer.stop();
});
</script>
