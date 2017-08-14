<%@ page contentType="text/html; charset=utf-8"%>
<%@include file="/common/include/rootPath.jsp"%>
<HTML>
	<HEAD>
		<title>无标题文档</title>
		
		<META HTTP-EQUIV="Pragma" CONTENT="no-cache">
		<META HTTP-EQUIV="author" CONTENT="zhoujx">
		<TITLE>系统工作区</TITLE>
		<link rel="stylesheet" href="<%=frameroot%>/css/tab/properties_toolbar.css" type="text/css">
		<SCRIPT LANGUAGE="JavaScript" src="<%=path%>/common/js/commontab/service.js"></SCRIPT>
		<SCRIPT LANGUAGE="JavaScript" src="<%=path%>/common/js/commontab/workservice.js"></SCRIPT>
		<STYLE type=text/css>
.tab {
	BORDER-RIGHT: gray 1px solid;
	BORDER-TOP: gray 1px solid;
	BORDER-LEFT: gray 1px solid;
	CURSOR: hand;
	COLOR: #000000;
	BORDER-BOTTOM: #ffffff 1px solid;
	BACKGROUND-COLOR: #eeeeee;
	font-size: 10pt;
	font-weight: 600;
}

.tab_sel {
	BORDER-RIGHT: gray 2px solid;
	background: url("<%=frameroot%>/images/tab/qh1_2.jpg");
	BORDER-TOP: gray 1px solid;
	BORDER-LEFT: #DDDDDD 1px solid;
	CURSOR: hand;
	COLOR: #000000;
	BACKGROUND-COLOR: #d4d0c8;
	font-size: 10pt;
	font-weight: 600;
}

.pane {
	display: block
}

.pane_hide {
	display: none
}
</STYLE>

		<SCRIPT language=javascript>
window.onresize = flagDir;

var SELECTED_INDEX = -1;

var TAB_LIST = new Array();

function getTabParam(flag,idx){
	var aList = TAB_LIST[idx];
	if(aList != null){
		return aList[flag];
	}
	return null;
}

function setTabParam(flag,paramVal,idx){
	TAB_LIST[idx][flag] = paramVal;
}

function findTabParam(flag,paramVal){
	for(var i=0;i<TAB_LIST.length;i++){
		if(TAB_LIST[i][flag].indexOf(paramVal) != -1){
			return i;
		}
	}
	return -1;
}

function locateTabParam(flag,paramVal){
	for(var i=0;i<TAB_LIST.length;i++){
		if(TAB_LIST[i][flag] == paramVal){
			return i;
		}
	}
	return -1;
}


function setTabParams(aParams,idx){
	TAB_LIST[idx] = aParams;
}

var recordActived   = false;
var instanceActived = false;
var formActived     = false;
/**
 * 点击Tab页时选中它
 */
function tab_doClick(){

	var obj = event.srcElement;
	while(obj.nodeName != "TABLE"){
		obj = obj.parentNode;
	}
	setSelectedIndex(obj.parentNode.cellIndex);
	var tabIndex = obj.parentNode.cellIndex;//当前Tab的索引位置
	if(!data[2]){
		return ;
	}
	if(tabIndex == 0){
		if(!recordActived){
			refreshWorkByTitle(data[2]+'!annal.action?instanceId='+data[1],'办理记录');
			recordActived = true;
		}
	}
	if(tabIndex == 1){
		if(!instanceActived){
			refreshWorkByTitle(data[2]+'!PDImageView.action?instanceId='+data[1],'流程图');
			instanceActived = true;
		}
	}
	if(tabIndex == 2){
		if(!formActived){
			refreshWorkByTitle(data[2]+"!viewform.action?instanceId="+data[1]+"&fromPath="+data[2]+"&taskId="+data[0],'表单');
			formActived = true;
		}
	}
}


var SEL_IDX = -1;

/**
 * 指示出被隐藏的当前Tab页的方向
 */
function flagDir(){
	if(SELECTED_INDEX == -1){
		return;
	}
	var oCel = tabList.cells(SELECTED_INDEX);

	//toLeft.src = "./images/arrow1.gif";
	//toRight.src = "./images/arrow2.gif";
	//
	//if((oCel.offsetLeft > (tabContainer.scrollLeft+tabContainer.offsetWidth))){
	//	toRight.src = "./images/arrow2_2.gif";
	//}else if((oCel.offsetLeft+oCel.offsetWidth) < tabContainer.scrollLeft){
	//	toLeft.src = "./images/arrow1_2.gif";
	//}
}
/** 增加Tab
 * @param sTitle Tab标题
 * @param sIcon 图标路径
 * @param sUrl 链接路径。
 */
function addTab(sTitle,sIcon,sUrl){
	var oCel = tabList.insertCell();
	oCel.className = "text";
	oCel.width = "100";
	oCel.height="25";
	oCel.noWrap = true;
	oCel.title = sTitle;
	oCel.attachEvent("onclick",tab_doClick);
	var titleSegment = (sTitle.length > 8)? sTitle.substring(0,8)+".." : sTitle;
	//oCel.innerHTML = "<IMG src='"+sIcon+"' id='img_switch'/><LABEL onmouseover=\"this.style.color='red'\" onmouseout=\"this.style.color='black'\" ondblclick=\"resizeWork(img_switch)\">"+titleSegment+"</LABEL>";
	oCel.innerHTML ="<table width='100%' height='22' border='0' cellpadding='00' cellspacing='0'><tr><td class='bt1_bg1'>&nbsp;</td><td align='center' class='bt1_bg2' noWrap valign='center'><IMG src='"+sIcon+"' id='img_switch'/><LABEL onmouseover=\"this.style.color='red'\" onmouseout=\"this.style.color='black'\">"+titleSegment+"</LABEL>&nbsp;</td><td width='27' class='bt1_bg3'>&nbsp;</td></tr></table>";
	var aParams = new Array(3);
	aParams["TITLE"] = sTitle;
	aParams["URL"] = sUrl;
	aParams["ID"] = createTabId();
	setTabParams(aParams,oCel.cellIndex);
	addPanels(sTitle,sIcon,sUrl);
	setSelectedIndex(oCel.cellIndex);
	tabContainer.scrollLeft = oCel.offsetLeft;
}
/**
 * 列号
 */
function createTabId(){
	return Math.random().toString();
}

/** 在增加相应地增加面板
 * @param sTitle Tab标题
 * @param sIcon 图标路径
 * @param sUrl 链接路径
 */
function addPanels(sTitle,sIcon,sUrl){
	var oRow = workPanel.insertRow();
	//var oRow1 = workPanel.insertRow();
	setPanelsVisibility(false,oRow.rowIndex-1);
	var oCel = oRow.insertCell();
	oCel.innerHTML = "<IFRAME frameBorder='0' scrolling='no' onreadystatechange='stateListener(this)' width='100%' height='100%' src='"+sUrl+"' >对不起，您的浏览器不支持IFRAME!</IFRAME>";

	//oRow1.valign = "bottom";	
	//oRow1.style.height = 20;
	//oCel = oRow1.insertCell();
	//oCel.noWrap = true;
	//oCel.innerHTML = "<IMG src='./images/info.gif' /><INPUT readonly type=text style='border:0;width:100%;color:#015ebc;font-size:11pt;background-color:#eeeeee' title='工作区消息' value='工作区消息：' ondblclick='alert(\"消息：\\n\"+this.value)'/>";
}
/** 给指定的Tab设置标题
 * @param sTitle
 * @param idx 索引
 */
function setTitleAt(sTitle,idx){
	if(idx == getSelectedIndex()){
		//labPanel.innerText = sTitle;
	}
	setTabParam("TITLE",sTitle,idx);
	var sTitle = (sTitle.length > 8)? sTitle.substring(0,8)+".." : sTitle;
	tabList.cells(idx).children(1).innerText = sTitle;
}
/** 得到指定Tab的标题
 * @param idx 索引
 * @return 标题
 */
function getTitleAt(sTitle,idx){
	return getTabParam("TITLE",idx);
}

/** 得到当前的Tab的索引
 * @return 当前的索引
 */
function getSelectedIndex(){
	//tab_doClick();
	return SELECTED_INDEX;
}
/** 设置当前的Tab，
 * @param idx 索引
 */
function setSelectedIndex(idx){
	//alert("selectedIndex:"+idx);
	//toLeft.src = "./images/arrow1.gif";
	//toRight.src = "./images/arrow2.gif";
	if(idx == -1){
		return ;
	}
	//自动滚动Tab页
	var oCel = tabList.cells(idx);

	if((oCel.offsetLeft+oCel.offsetWidth) >= (tabContainer.scrollLeft+tabContainer.offsetWidth)){
		tabContainer.doScroll("pageRight");
	}else if(oCel.offsetLeft <= tabContainer.scrollLeft){
		tabContainer.doScroll("pageLeft");
	}
	if(SELECTED_INDEX == idx){
		return;
	}
	
	if(SELECTED_INDEX != -1){
		//tabList.cells(SELECTED_INDEX).className = "tab";
		tabList.cells(SELECTED_INDEX).childNodes[0].rows[0].cells[0].className = "bt2_bg1";
		tabList.cells(SELECTED_INDEX).childNodes[0].rows[0].cells[1].className = "bt2_bg2";
		tabList.cells(SELECTED_INDEX).childNodes[0].rows[0].cells[2].className = "bt2_bg3";
		setPanelsVisibility(false,SELECTED_INDEX);
	}
	//使新的Tab被选中
	//oCel.className = "tab_sel";
	oCel.childNodes[0].rows[0].cells[0].className = "bt1_bg1";
	oCel.childNodes[0].rows[0].cells[1].className = "bt1_bg2";
	oCel.childNodes[0].rows[0].cells[2].className = "bt1_bg3";
	//labPanel.innerText = getTabParam("TITLE",idx);
	setPanelsVisibility(true,idx);
	SELECTED_INDEX = idx;
}
/** 需要调整Panels的可见性
 * @param bl true/false
 * @param idx 索引
 */
function setPanelsVisibility(bl,idx){
	var state = bl? "pane" : "pane_hide";
	//alert(idx+":"+state);
	//设置外挂Panel的状态
	var rowIdx = idx<0?0:idx;//2*idx;
	workPanel.rows(rowIdx).className = state;
	//workPanel.rows(rowIdx+1).className = state;
}
/** 返回当前的Tab数
 * 
 */
function getTabCount(){
	return tabList.cells.length;
}
function checkBound(idx)
{
	if(idx < 0 || idx > tabList.cells.length -1){
		alert("索引越界:"+idx+">"+(tabList.cells.length -1));
		return false;
	}
	return true;
}
function containsTab(title){
	var idx = locateTabParam("TITLE",title);
	return (idx == -1)?false:true;
}
</SCRIPT>

		<SCRIPT LANGUAGE="JavaScript">

var MAX_TAB_COUNT = 7;
var IS_WIN98 = false;
var ver = navigator.appVersion;
var idx = ver.indexOf("Windows");
if(idx != -1){
	idx = ver.indexOf("98",idx+6);
	if(idx != -1){
		IS_WIN98 = true;
		//MAX_TAB_COUNT = 6;
	}
}
var SYS_RES = null;
var SYS_USER = 12;
var SYS_GDI = 8;
if(IS_WIN98){
	try{
		SYS_RES = new ActiveXObject("Dcbassrv.SystemRes");
	}catch(e){
		//alert("new sys res:"+e.message);
	}
}
/** 导航页面
 * @param sUrl 链接地址
 * @param sTitle 链接的标题
 * @param bl 是否在新窗口打开工作区
 */
function navigate(sUrl,sTitle,bl){
	if(bl == null || !bl){
		if(containsTab(sTitle)){
			if(!confirm("工作区["+sTitle+"]已经存在，\n您确定还要打开新的工作区吗？")){
				return;
			}
		}
		if(IS_WIN98 && SYS_RES != null){
			try{
				if(SYS_RES.FreeUserRes < SYS_USER || SYS_RES.FreeGDIRes < SYS_GDI){
					alert("警告（仅在WINDOWS98下会发生）：\nWINDOWS98系统的剩余资源太少，请先关闭多余的工作区。\n然后再尝试打开新的工作区。");
					return;
				}

			}catch(e){
				//alert("check sys res:"+e.message);
			}
		}
		if(getTabCount() < MAX_TAB_COUNT){
			showLoading(true);
			addTab(sTitle,"<%=frameroot%>/images/tab/state1.gif",sUrl);
		}else{
			if(IS_WIN98){
				alert("系统提示：\n您打开的工作区过多，WINDOWS98的系统资源有限，请关闭多余的工作区");
				return;
			}else{
			alert("系统提示：\n您最多可以打开["+MAX_TAB_COUNT+"]个工作区\n请关闭多余的工作区\n注：超出的工作区将从新窗口打开");
			navigate_new(sUrl,sTitle);
			}
		}
	}else{
		if(IS_WIN98){
			if(SYS_RES != null){
				try{
					if(SYS_RES.FreeUserRes < SYS_USER || SYS_RES.FreeGDIRes < SYS_GDI){
						alert("警告（仅在WINDOWS98下会发生）：\nWINDOWS98系统的剩余资源太少，请先关闭多余的工作区。\n然后再尝试打开新的工作区。");
						return;
					}
				}catch(e){
					//alert("check sys res:"+e.message);
				}
			}
			if(getTabCount() >= MAX_TAB_COUNT){
				alert("系统提示：\n您打开的工作区过多，WINDOWS98的系统资源有限，请关闭多余的工作区");
				return;
			}
		}
		navigate_new(sUrl,sTitle);
	}
}
/** 导航页面到新窗口
 * @param sUrl 链接地址
 * @param sTitle 链接的标题
 */
function navigate_new(sUrl,sTitle){
		var wh = "";
		var param = "";
		if (sUrl.indexOf("work/yhs/yhs.htm")!=-1){
			wh = "width="+(screen.width-10)+",height="+(screen.height-80);
			param = "toolbar=no,location=no,status=yes,resizable=yes,scrollbars=yes,top=0,left=0,"+wh;
		}
		else{
			wh = "width="+(screen.width-10)+",height="+(screen.height-60);
			param = "toolbar=no,location=no,status=no,resizable=yes,scrollbars=yes,top=0,left=0,"+wh;
		}
		
		var win = window.open("/StrongOA/console/right_new.htm?workurl="+sUrl+"&worktitle="+sTitle,"_blank",param);
		if(win != null){win.$auth="$true$"}
}

/////////////////////////////////////////////////////////////
/** 找到window对象所对应的工作区ID
 * @param oWin 发出调用的页面的window对象
 * @return 对应的工作区的ID
 */
function findCallerIndex(oWin){
	if(oWin == null){
		return getSelectedIndex();
	}
	var ifm = findCallerIframe(oWin);
	for(var i=0;i<window.frames.length;i++){
		if(ifm == window.frames(i)){
			//alert("callerIndex:"+i);
			return i;
		}
	}
	//alert("findIndex.非正常:"+getSelectedIndex());
	return getSelectedIndex();
}

/** 找到window对象所对应的工作区的iframe
 * @param oWin 页面的window对象
 * @return 对应的工作区的iframe对象
 */
function findCallerIframe(oWin){
	//alert("findIframe.recursion:"+win.document.title);
	if(oWin.parent == self){
		//alert("findIframe.return.document:"+win.document.title);
		return oWin;
	}
	return findCallerIframe(oWin.parent);//...
}
/** 刷新指定Title的工作区，如没有则打开一个新的工作区
 * @param sUrl 工作区的URL
 * @param sTitle 工作区标题
 */
function refreshWorkByTitle(sUrl,sTitle){
	var idx = findTabParam("TITLE",sTitle);
	if(idx == -1){
		navigate(sUrl,sTitle);
	}else{
		window.frames(idx).location=sUrl;
	}
}
/////////////////////////
var BUF_SIZE = 5;//历史消息缓存
var MSG_LOG = new Array(BUF_SIZE);
var POINTER = 0;//指针，当前位置将被写
/**
 * 显示全部
 */
function showHistory(){
	var msgs = "";
	var ptr = POINTER;
	var i = 1;
	for(;i<BUF_SIZE+1;i++){
		ptr --;
		if(ptr < 0){
			ptr = BUF_SIZE - 1;
		}
		if(MSG_LOG[ptr] != null){
			msgs += "◆消息记录"+i+"\n"+MSG_LOG[ptr]+"\n";
		}else{
			i -- ;
			break;
		}
	}
	if(msgs != ""){
		i = (i== BUF_SIZE+1)? BUF_SIZE : i;
		msgs = "最近"+i+"次消息记录\n" + msgs;
		alert(msgs);
	}else{
		alert("无消息记录");
	}
}
/** 将显示的消息放入历史记录
 * @param msg 消息内容
 */
function pushMessage(msg){
	MSG_LOG[POINTER] = msg;
	if (POINTER < BUF_SIZE-1){
		POINTER ++;
	}else{
		POINTER = 0;
	}
}
function parseMsg(str){
	if(str == null || str.length < 4){
		return null;
	}
	var oDoc = loadXml(str);
	if(oDoc == null){
		return null;
	}
	var msg = "消息来源：";
	var root = oDoc.documentElement;
	var app = root.selectSingleNode("Applet");
	var oArea = app.selectSingleNode("area");
	var area = "";
	var time = null;
	if(oArea != null){
		area = oArea.text;
		time = oArea.getAttribute("time");
	}

	if (area == "system") {
		msg += "系统消息\n";
	}else if (area == "todo") {
		msg += "待办事宜\n";
	}else{
		msg += "未知消息来源\n";
	}
	if(time != null){
		msg += "发生时间："+time+"\n";
	}

	var list = app.getElementsByTagName("operation");
	msg += "共〖" + list.length + "〗条消息\n";
	var action = null;
	var node = null;
	for (var i=0;i<list.length;i++){
		msg += "※ №." + (i+1) + "\n";
		node = list(i);
		action = node.getAttribute("action");
		if(action == null){
			action = "未知类型\n";
		}else if (action == "add") {
			action = "新增信息\n";
		}else if(action == "delete"){
			action = "关闭信息\n";
		}else{
			action = "未知类型\n";
		}
		msg += "  类型：" + action
			+ "  标题：" + node.getAttribute("label") + "\n"
			+ "  内容：" + node.getAttribute("text") + "\n";
	}
	return msg;
}


var oPopup = null;
////////////////
var sizeWholeOrg = "65,*";//"53,*,20";
var sizeWholeMax = "0,*";//"0,*,20";

var sizeMiddleOrg = "25%,*";
var sizeMiddleMax = "0,*";

var sizeMiddleOld = "0,*";

var leftOpened = false;
/** 点击图片时改变工作区大小
 * @param obj 点击的图片对象
 */
function resizeWork(obj)
{
	if(top.FRM_WHOLE.rows == sizeWholeOrg){
		top.FRM_WHOLE.rows = sizeWholeMax;
		sizeMiddleOld = top.FRM_MIDDLE.cols;
		top.FRM_MIDDLE.cols = sizeMiddleMax;
		obj.src = "<%=frameroot%>/images/tab/show-all.gif";
		//obj.innerHTML = "2";
		obj.title = "还原";
	}else{
		top.FRM_WHOLE.rows = sizeWholeOrg;
		top.FRM_MIDDLE.cols = sizeMiddleOld;//
		obj.src = "<%=frameroot%>/images/tab/show-all-1.gif";
		obj.title = "工作区全屏";
	}
	setWorkFocus();
}

/** 设置当前窗口焦点
*/
function setWorkFocus(){
	//有申报表头的。
	if (getSelectedIndex()==-1){
	    return;
	}
	try {
		var sb_objs=window.frames(getSelectedIndex()).document.body.getElementsByTagName("sb_header");
		for (var i=0;i<sb_objs.length;i++){
			if (sb_objs(i).getItemObject(0).disabled==false && sb_objs(i).getItemObject(0).type!="hidden"){
				sb_objs(i).getItemObject(0).focus();
				return;
			}
		}
		var objs=window.frames(getSelectedIndex()).document.body.getElementsByTagName("INPUT");
		//alert(objs.length);
		for (var i=0;i<objs.length;i++){
			if (objs(i).disabled==false && objs(i).type!="hidden"){
				objs(i).focus();
				return;
			}
		}
	} catch (E){
		//
	}
}

/** onmouseover时，改变图片
 * @param obj 图片对象
 * @param flag 标志onmouseover是哪个图片
 */
function resizeImage(obj,flag){
	if(top.FRM_WHOLE.rows == sizeWholeOrg){
		if(flag == 0){
			obj.src = "<%=frameroot%>/images/tab/show-all-1.gif";
		}else{
			obj.src = "<%=frameroot%>/images/tab/show-all.gif";
		}
	}else{
		if(flag == 0){
			obj.src = "<%=frameroot%>/images/tab/show-up-1.gif";
		}else{
			obj.src = "<%=frameroot%>/images/tab/show-up.gif";
		}
	}
}
/** 左边资源显示/隐藏的开关控制
 */
function switchLeft(){
	if(top.FRM_MIDDLE.cols == sizeMiddleOrg){
		top.FRM_MIDDLE.cols = sizeMiddleMax;
	}else{
		top.FRM_MIDDLE.cols = sizeMiddleOrg;
		/*
		if(!leftOpened){
			window.open("left.htm","FRM_LEFT");
			leftOpened = true;
		}
		*/
	}
	sizeMiddleOld = top.FRM_MIDDLE.cols;////记住原始大小
	setWorkFocus();
}

var state = false;

/** 为每个工作区增加监听器
 * @param obj 工作区iframe对象
 */
function stateListener(obj){
	if (obj.readyState == "complete"){
		showLoading(false);
		//如果Title为null则加载页面的title
	}else if(obj.readyState == "loading"){
		showLoading(true);
	}
}

/** 显示/隐藏加载信息
 * @param bl true/false
 */
function showLoading(bl){
	var state = bl ? "block" : "none";
	LOADING.style.display = state;
}
/**
 * 屏蔽右键菜单
 */
function window.document.oncontextmenu(){
	if(event.srcElement.nodeName != "INPUT"){
		return false;
	}
}
//定义数组，存储从页面发送过来的数据
var data = new Array();
/**
	@param id 			任务实例id
	@param instanceId	流程实例id
	@param contextPath	上下文路径
*/
function setWorkId(id,instanceId,contextPath){
		recordActived = false;	
		instanceActived = false;
 		formActived     = false;
		data[0] = id ;
		data[1] = instanceId;
		data[2] = contextPath;
		var index = getSelectedIndex();
		if(index == 0){
		    refreshWorkByTitle(contextPath+'!annallist.action?instanceId='+instanceId,'办理记录');
		    recordActived = true;
		}
		if(index == 1){
		    refreshWorkByTitle(contextPath+'!PDImageView.action?instanceId='+instanceId,'流程图');
		    instanceActived = true;
		}
		if(index == 2){
			refreshWorkByTitle(contextPath+"!viewform.action?instanceId="+instanceId+"&fromPath="+contextPath+"&taskId="+id,'表单');
			formActived = true;	
		}
}

</SCRIPT>


		<script for="window" event="onload">
	state = true;//初始化完毕
	navigate('<%=path%>/fileNameRedirectAction.action?toPage=/workflow/viewinfo.jsp','办理记录');
   navigate('<%=path%>/fileNameRedirectAction.action?toPage=/workflow/viewinfo.jsp','流程图');
	navigate('<%=path%>/fileNameRedirectAction.action?toPage=/workflow/viewinfo.jsp','表单');
	setSelectedIndex(0);
	//top.setSplash("系统工作区初始化完成！",30);//加载进度
	try{
		msg2.StartListen();
	}catch(e){
		/*
		var str = "待办事宜监听异常:\n"+e.message;
		if(e.message == "Address in use"){
			str = "\n出现该问题的原因是：您启动了一个以上的控制台";
		}
		alert(str);
		*/
	}
	//alert(msg2.socketState);
	if(window.top.opener != null && window.top.document.URL.indexOf("ptl=yes")!=-1)eval(unescape(tmp_.text))
</script>


	</head>

	<body class=toolbarbodymargin>
		<DIV id=toolbarborder>
		<DIV id=toolbar style="height: 28">
		<TABLE onselectstart="return false" 
			border=0
			style="position: absolute; top: 5; width: 100%; height: 22; z-index: 0 "
			cellSpacing=0 cellPadding=1>
			<TR>
				<TD style="padding-left: 0">
					<DIV id="tabContainer"
						style="position: absolute; top: 2; width: 90%; height: 22; font-size: 15; overflow-y: no; overflow-x: hidden;">
						<TABLE style=" height: 22;" border=0 cellSpacing=2
							cellPadding=0>
							<TR id=tabList>
							</TR>
						</TABLE>
					</DIV>
				</TD>
			</TR>
		</TABLE>
		</DIV>
		</DIV>
		<TABLE style="padding-left: 0; padding-right: 0; padding-top: 0; padding-bottom: 0; " border=0
			width="100%" height="95%" cellpadding="0" cellspacing="0">
			<TBODY id="workPanel">
			</TBODY>
		</TABLE>
		
	
	<DIV id=LOADING onClick="showLoading(false)"
		style="position: absolute; top: 30%; left: 38%; display: none"
		align=center>
		<font color=#16387C><strong>正在加载，请稍候...</strong></font>
		<br>
		<IMG src="<%=frameroot%>/images/tab/loading.gif">
	</DIV>

	</BODY>
</html>
