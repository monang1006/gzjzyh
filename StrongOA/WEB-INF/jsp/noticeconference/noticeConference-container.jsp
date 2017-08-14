<%@ page contentType="text/html; charset=utf-8"%>
<%@include file="/common/include/rootPath.jsp"%>
<% 
	String meetingType=request.getParameter("meetingType");
%>
<HTML>
	<HEAD>
		<title>会议通知</title>
		
		<META HTTP-EQUIV="Pragma" CONTENT="no-cache">
		<META HTTP-EQUIV="author" CONTENT="zhoujx">
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

function removeTabParams(idx){

	TAB_LIST = TAB_LIST.slice(0,idx).concat(TAB_LIST.slice(idx+1));
}
/**
 * 点击Tab页时选中它
 */
function tab_doClick(){

	var obj = event.srcElement;
	while(obj.nodeName != "TABLE"){
		obj = obj.parentNode;
	}
	setSelectedIndex(obj.parentNode.cellIndex);
}


var SEL_IDX = -1;

function tab_doMenu(){
	var obj = event.srcElement;
	while(obj.nodeName != "TABLE"){
		obj = obj.parentNode;
	}
	SEL_IDX = obj.parentNode.cellIndex;
	showMenu();
	window.event.returnValue=false;
}
/**
 * 点击左右滚动时执行
 * @param dir 滚动方向
 */
function scrollTab(dir){
	tabContainer.doScroll(dir);
	flagDir();
}
/**
 * 指示出被隐藏的当前Tab页的方向
 */
function flagDir(){
	if(SELECTED_INDEX == -1){
		return;
	}
	var oCel = tabList.cells(SELECTED_INDEX);
}

/**
 * 滚动的方向标鼠标事件处理
 * @param obj 对象
 */
function scroll_doMouseOver(obj){
	var flag = "1";
	if(obj == toRight){
		flag = "2";
	}
	if(obj.src.lastIndexOf("arrow"+flag+"_2.gif") != -1){
		return;
	}
	obj.src = "<%=frameroot%>/images/tab/arrow"+flag+"_1.gif";
}

/**
 * 滚动的方向标鼠标事件处理
 * @param obj 对象
 */
function scroll_doMouseOut(obj){
	var flag = "1";
	if(obj == toRight){
		flag = "2";
	}
	if(obj.src.lastIndexOf("arrow"+flag+"_2.gif") != -1){
		return;
	}
	obj.src = "<%=frameroot%>/images/tab/arrow"+flag+".gif";
}

/**
 * 指定的索引Tab滚动到当前位置
 *
 */
function scrollToView(){
	//toLeft.src = "./images/arrow1.gif";
	//toRight.src = "./images/arrow2.gif";
	var idx = getSelectedIndex();
	if(idx != -1){
		tabContainer.scrollLeft = tabList.cells(idx).offsetLeft;
	}
}

/** 增加Tab
 * @param sTitle Tab标题
 * @param sIcon 图标路径
 * @param sUrl 链接路径。
 */
function addTab(sTitle,sIcon,sUrl){
	var oCel = tabList.insertCell();
	oCel.className = "text";
	oCel.width = "128";
	oCel.height="25";
	oCel.noWrap = true;
	oCel.title = sTitle;
	oCel.attachEvent("onclick",tab_doClick);
	//oCel.attachEvent("oncontextmenu",tab_doMenu);
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
}

/** [deprecated]插入指定的Tab，另：insertPanels()
 * @param idx 索引
 */
function insertTab(idx){
	//addTab(title,sIcon,sUrl);
	alert("异常：目前不需要实现");
}

/** 删除指定的Tab，idx不一定为当前的Tab
 * @param idx 索引
 */
function removeTabAt(idx){
	if(tabList.cells.length < 1){
		return ;
	}
	
	if(!checkBound(idx)){
		return;
	}
	tabList.deleteCell(idx);
	removeTabParams(idx);
	removePanelsAt(idx);
	if(idx < SELECTED_INDEX){
		SELECTED_INDEX --;
	}else if(idx == SELECTED_INDEX){
		SELECTED_INDEX = -1;
		setSelectedIndex(tabList.cells.length-1);
	}
}

/** 删除指定的Tab后，需要删除相应的Panel
 * @param idx 索引
 */
function removePanelsAt(idx){
	var rowIdx = idx;//2*idx;
	//alert(rowIdx);
	if(idx!=null&&idx>=0&&idx<workPanel.rows.length)
		workPanel.deleteRow(idx);
	//workPanel.deleteRow(rowIdx);
}

/** 给指定的Tab设置图标
 * @param sImg
 * @param idx 索引
 */
function setIconAt(sImg,idx){
	tabList.cells(idx).children(0).src = sImg;
}

/** 得到Tab标签的图标
 * @param idx 索引
 * @return 返回图标的src
 */
function getIconAt(idx){
	//return tabList.cells(idx).children(0).src;
	return tabList.cells(idx).children(0).cells[1].children(0).src;
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
		tabList.cells(SELECTED_INDEX).childNodes[0].rows[0].cells[0].className = "bt2_bg1";
		tabList.cells(SELECTED_INDEX).childNodes[0].rows[0].cells[1].className = "bt2_bg2";
		tabList.cells(SELECTED_INDEX).childNodes[0].rows[0].cells[2].className = "bt2_bg3";
		setPanelsVisibility(false,SELECTED_INDEX);
	}
	//使新的Tab被选中
	oCel.childNodes[0].rows[0].cells[0].className = "bt1_bg1";
	oCel.childNodes[0].rows[0].cells[1].className = "bt1_bg2";
	oCel.childNodes[0].rows[0].cells[2].className = "bt1_bg3";
	setPanelsVisibility(true,idx);
	SELECTED_INDEX = idx;
}

/** 需要调整Panels的可见性
 * @param bl true/false
 * @param idx 索引
 */
function setPanelsVisibility(bl,idx){
	var state = bl? "pane" : "pane_hide";
	//设置外挂Panel的状态
	var rowIdx = idx<0?0:idx;//2*idx;
	workPanel.rows(rowIdx).className = state;
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
//top.setSplash("初始化系统工作区...",20);


/** 得到工作台状态栏消息
 */
function getMessage(oWin)
{
	//iframe所在row
	var rowIdx = findCallerIndex(oWin) + 1;
	var cel = workPanel.rows(rowIdx).cells(0);//.innerHTML = sFlag+str;
	if(cel != null){
		return cel.children(1).title;
	}else{
		return null;
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

/**关闭当前工作区
 * @param bl true/false是否强制退出，null==false
 */
function closeWork(bl,oWin){
	var callerIdx = findCallerIndex(oWin);
	closeWorkByIdx(bl,callerIdx);
}

function closeWorkByIdx(bl,callerIdx)
{
	//首先处理自定义window.onWorkClosing事件
	if(!bl && document.frames.length > 0&&callerIdx>=0&&callerIdx<document.frames.length){
		//alert(bl+":"+callerIdx);
		var iframeObj = document.frames(callerIdx);
		var sIframeObj = "document.frames("+callerIdx+")";
		var func = null;
		if(iframeObj.onWorkClosing != null && iframeObj.onWorkClosing != ""){
			func = iframeObj.onWorkClosing.toString();
		}else if(iframeObj.onworkclosing != null && iframeObj.onworkclosing != ""){
			func = iframeObj.onworkclosing.toString();
			alert("关闭事件调用警告：\n请使用window.onWorkClosing\n注意区分大小写");
		}else if(isChangedByIdx(callerIdx)){
			//如果被标记成已改变，则提示用户
			if(!confirm("当前工作区内容已经改变，但尚未保存\n您确定要关闭当前工作区吗？")){
				return;
			}
		}
		if(func != null){
			var rtn = eval(sIframeObj+"."+parseFunc(func)+"()");
			if(rtn == false){
				return;
			}
		}
	}
	
	//logoutPage(callerIdx);
	if(confirm ("您确认关闭吗?", "ok", "cancel")){	
	removeTabAt(callerIdx);
	showLoading(false);
	setTimeout("CollectGarbage()",500);//延迟500ms进行垃圾收集
	}
}

/** closeWork()时，注销Tab工作区页面
 * @param idx 工作区索引
 */
function logoutPage(idx){
	if(idx < 0){
		return;
	}
	var service = new Service("common.CtaisMasterBndService.exit",getTabParam("ID",idx));
	service.doService();
}

/** 对于一个函数，解析出函数名
 * @param str 为：function go(s){...}或go()或go
 */
function parseFunc(str){
	var idx1 = str.indexOf("function");
	if(idx1 == -1){
		idx1 = -8;
	}
	idx1 += 8;
	var idx2 = str.indexOf("(",idx1);
	if(idx2 == -1){
		idx2 = str.length;
	}
	var funcName = str.substring(idx1,idx2);
	return funcName;
}
/** 通知当前工作区是否发生变化
 * @param bl 是否发生了变化
 */
function notifyChange(bl,oWin){
	var sImg = bl? "./images/state2.gif" : "./images/state1.gif";
	//只能更新当前页面的
	//alert("change");
	setIconAt(sImg,findCallerIndex(oWin));
}
/** 当前工作区是否发生变化:true/false
 */
function isChanged(oWin){
	//只能查当前页面的，
	return getIconAt(findCallerIndex(oWin)).indexOf("state2.gif") != -1;
}
function isChangedByIdx(idx){
	//只能查当前页面的，
	return getIconAt(idx).indexOf("state2.gif") != -1;
}
/** 得到工作区id
 * @param oWin 发出调用的页面的window对象
 */
function getWorkId(oWin)
{
	var callerIndex = findCallerIndex(oWin);
	return getTabParam("ID",callerIndex);
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
///////////////////////////////////////////////

/** 刷新指定URL的工作区，如没有则打开一个新的工作区
 * @param sUrl 工作区的URL
 * @param sTitle 工作区标题
 */
function refreshWorkByUrl(sUrl,sTitle){
	var idx = findTabParam("URL",sUrl);
	if(idx == -1){
		navigate(sUrl,sTitle);
	}else{
		window.frames(idx).location.reload();
	}
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
function refreshCurrentWork(oWin){
	var callerIdx = findCallerIndex(oWin);
	window.frames(callerIdx).location.reload();
}
/**
 * 没别的，就图个方便:)
 */
function refreshTodo(){
	refreshWorkByUrl("../work/todo/index.jsp","待办事宜");
}
/////////////////////////
var BUF_SIZE = 5;//历史消息缓存
var MSG_LOG = new Array(BUF_SIZE);
var POINTER = 0;//指针，当前位置将被写

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

</SCRIPT>
<%
	    String conferId =(String) request.getParameter("conferId");// 会议ID
	   
	  %>
<script for="window" event="onload">
	state = true;//初始化完毕
	 alert(<%=conferId %>);
	navigate('<%=root%>/noticeconference/noticeConference.action?state=0&conferId=<%=conferId %>','会议通知草稿');
	navigate('<%=root%>/noticeconference/noticeConference.action?state=1&conferId=<%=conferId %>','已发会议通知');
	setSelectedIndex(0);
	try{
		msg2.StartListen();
	}catch(e){

	}
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
			width="100%" height="93%" cellpadding="0" cellspacing="0">
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
