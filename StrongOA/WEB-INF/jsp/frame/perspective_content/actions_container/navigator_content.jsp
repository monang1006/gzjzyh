<%@ page contentType="text/html; charset=utf-8"%>
<%@include file="/common/include/rootPath.jsp"%>
<HTML>
	<HEAD>
		<title>无标题文档</title>

		<META HTTP-EQUIV="Pragma" CONTENT="no-cache">
		<META HTTP-EQUIV="author" CONTENT="zhoujx">
		<TITLE>系统工作区</TITLE>
		<link rel="stylesheet"
			href="<%=frameroot%>/css/tab/properties_toolbar.css" type="text/css">
		<link rel="stylesheet" href="<%=frameroot%>/css/tab/style.css" type="text/css">
		<script src="<%=path%>/common/js/jquery/jquery-1.2.6.js" type="text/javascript"></script>
		<SCRIPT LANGUAGE="JavaScript" src="<%=jsroot%>/commontab/service.js"></SCRIPT>
		<SCRIPT LANGUAGE="JavaScript"
			src="<%=jsroot%>/commontab/workservice.js"></SCRIPT>
		<SCRIPT LANGUAGE="JavaScript" src="<%=jsroot%>/commontab/menu.js"></SCRIPT>
		<style id="popupmanager">
		a:link,a:visited,a:hover,a:active{
			text-decoration:none;
		}
.logo{
	font-family: "黑体";
	font-size: 32px;
	font-weight: bold;
	color: #FFFFFF;}
	
.popupMenu {
	width: 100px;
	border: 1px solid #666666;
	background-color: #F9F8F7;
	padding: 1px;
}

.popupMenuTable { /*
	background-image: url(/images/popup/bg_menu.gif);
	*/
	background-repeat: repeat-y;
}

.popupMenuTable TD {
	font-family: MS Shell Dlg;
	font-size: 12px;
	cursor: default;
}

.popupMenuRow {
	height: 21px;
	padding: 1px;
}

.popupMenuRowHover {
	height: 21px;
	border: 1px solid #0A246A;
	background-color: #B6BDD2;
}

.popupMenuSep {
	background-color: #A6A6A6;
	height: 1px;
	width: expression(parentElement . offsetWidth-27);
	position: relative;
	left: 28;
}
</style>
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
	background: url("<%=frameroot%>/images/tab/images/perspective_leftside/qh1_2.jpg");
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

var SELECTED_INDEX = -1;//记录当前选中tab页索引

var TAB_LIST = new Array();//记录所有打开的tab页

/**
 * 在TAB_LIST中根据索引获取对应tab页对象对应的值
 * @param:flag 要获取的值类型，TITLE：获取标题，URL：获取内容链接，ID：获取Tab页编号
 * @param:idx tab页在TAB_LIST中的索引位置
 */
function getTabParam(flag,idx){
	var aList = TAB_LIST[idx];
	if(aList != null){
		return aList[flag];
	}
	return null;
}

/**
 * 在TAB_LIST中根据索引设置对应tab页对象对应的值
 * @param:flag 要设置的值类型，TITLE：设置标题，URL：设置内容链接，ID：设置Tab页编号
 * @param:paramVal 要设置的值
 * @param:idx tab页在TAB_LIST中的索引位置
 */
function setTabParam(flag,paramVal,idx){
	TAB_LIST[idx][flag] = paramVal;
}

/**
 * 在TAB_LIST中根据tab页对象对应的值获取对应索引
 * @param:flag 要获取索引的值类型，TITLE：标题，URL：内容链接，ID：Tab页编号
 * @param:paramVal tab页对象对应的值
 */
function findTabParam(flag,paramVal){
	for(var i=0;i<TAB_LIST.length;i++){
		if(TAB_LIST[i][flag].indexOf(paramVal) != -1){
			return i;
		}
	}
	return -1;
}

/**
 * 在TAB_LIST中根据tab页对象对应的值获取对应索引
 * @param:flag 要获取索引的值类型，TITLE：标题，URL：内容链接，ID：Tab页编号
 * @param:paramVal tab页对象对应的值
 */
function locateTabParam(flag,paramVal){
	for(var i=0;i<TAB_LIST.length;i++){
		if(TAB_LIST[i][flag] == paramVal){
			return i;
		}
	}
	return -1;
}

/**
 * 在TAB_LIST中根据索引设置对应tab页对象
 * @param:aParams 要设置的tab页对象
 * @param:idx tab页在TAB_LIST中的索引位置
 */
function setTabParams(aParams,idx){
	TAB_LIST[idx] = aParams;
}

/**
 * 在TAB_LIST中移除对应索引的tab页对象
 * @param:idx tab页在TAB_LIST中的索引位置
 */
function removeTabParams(idx){

	TAB_LIST = TAB_LIST.slice(0,idx).concat(TAB_LIST.slice(idx+1));
}
/**
 * 点击Tab页时选中它
 */
function tab_doClick(){

	var obj = event.srcElement;//获取当前点击对象
	while(obj.nodeName != "TABLE"){
		obj = obj.parentNode;//循环往上找到对应的点击的table对象
	}
	setSelectedIndex(obj.parentNode.cellIndex);////设置当前选中的tab页为当前点击的tab页
	if(obj.parentNode.cellIndex=="0"){
		window.frames(obj.parentNode.cellIndex).location.reload();
	}
}

var SEL_IDX = -1;
//设置右键菜单
var tabrightmenu = new RightMenu();
tabrightmenu.addItem("close","x","<font class=w2kfont>关 闭</font>","ROOT","closeworkByRightMenu(false,SEL_IDX);");
//menu.addItem("sperator","","","ROOT",null);
//menu.addItem("resize","a","<font class=w2kfont>缩 放</font>","ROOT","resizeWork(img_switch);");
//document.writeln(menu.getMenu());
document.onclick=OnTabClick;//设置页面点击事件

//document.onDblClick = closeWorkByIdx(false,SEL_IDX);

/**
 * 右键菜单事件
 */
function tab_doMenu(){
	var obj = event.srcElement;//获取当前点击的对象
	while(obj.nodeName != "TABLE"){
		obj = obj.parentNode;//循环往上找到对应的点击的table对象
	}
	SEL_IDX = obj.parentNode.cellIndex;//获取对应table对象父对象在标题区的索引位置
	showTabMenu();//显示右键菜单
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
	if(SELECTED_INDEX == -1){//没有当前选中tab页，即没有打开的tab页时，设置向左向右滚动按钮为不可点击的图标状态
		toRight.src = "<%=frameroot%>/images/tab/arrow2_2.gif";//向右滚动按钮
		toLeft.src = "<%=frameroot%>/images/tab/arrow1_2.gif";//向左滚动按钮
		return;
	}
	
	var oCel = tabList.cells(SELECTED_INDEX);//获取当前选中tab页标题对应的单元格

	toLeft.src = "<%=frameroot%>/images/tab/arrow1.gif";//设置向左滚动按钮为可点击的图标状态
	toRight.src ="<%=frameroot%>/images/tab/arrow2.gif";//设置向右滚动按钮为可点击的图标状态
	//
	var oCel1 = tabList.cells(tabList.cells.length-1);//获取最后一个tab页标题对应的单元格
	var oCel2 = tabList.cells(0);//获取第一个tab页标题对应的单元格
	
	//如果最后一个tab页标题对应的单元格的左位移加位移宽度小于等于tab容器的左位移加位移宽度，即没有向右溢出的情况，则向右滚动按钮为不可点击的图标状态
	if((oCel1.offsetLeft +oCel1.offsetWidth) <= (tabContainer.scrollLeft+tabContainer.offsetWidth)){
		toRight.src = "<%=frameroot%>/images/tab/arrow2_2.gif";
	}
	//如果第一个tab页标题对应的单元格的左位移大于等于tab容器的左位移，即没有向左溢出的情况，则向左滚动按钮为不可点击的图标状态
	if(oCel2.offsetLeft>= tabContainer.scrollLeft){
		toLeft.src = "<%=frameroot%>/images/tab/arrow1_2.gif";
	}
}
/**
 * 滚动的方向标鼠标事件处理
 * @param obj 对象
 */
function scroll_doMouseOver(obj){
	var flag = "1";//向左滚动
	if(obj == toRight){
		flag = "2";//向右滚动
	}
	
	//如果向左向右滚动按钮为不可点击的图标状态，则直接返回
	if(obj.src.lastIndexOf("arrow"+flag+"_2.gif") != -1){
		return;
	}
	
	//将图标设置为选中状态
	obj.src = "<%=frameroot%>/images/tab/arrow"+flag+"_1.gif";
}
/**
 * 滚动的方向标鼠标事件处理
 * @param obj 对象
 */
function scroll_doMouseOut(obj){
	var flag = "1";//向左滚动
	if(obj == toRight){
		flag = "2";//向右滚动
	}
	//如果向左向右滚动按钮为不可点击的图标状态，则直接返回
	if(obj.src.lastIndexOf("arrow"+flag+"_2.gif") != -1){
		return;
	}
	
	//将图标设置为非选中可点击状态
	obj.src = "<%=frameroot%>/images/tab/arrow"+flag+".gif";
}
/**
 * 指定的索引Tab滚动到当前位置
 *
 */
function scrollToView(){
	toLeft.src = "<%=frameroot%>/images/tab/arrow1.gif";//设置向左滚动按钮为可点击的图标状态
	toRight.src = "<%=frameroot%>/images/tab/arrow2.gif";//设置向右滚动按钮为可点击的图标状态
	var idx = getSelectedIndex();//获取当前选择tab页索引
	
	//如果当前选择tab页索引不为-1，即存在打开的tab页，则重新设置tab容器的左位移
	if(idx != -1){
		tabContainer.scrollLeft = tabList.cells(idx).offsetLeft;
	}
}
/** 增加Tab
 * @param sTitle Tab标题
 * @param sIcon 图标路径
 * @param sUrl 链接路径。
 * 修改时间：2012-02-09 onDblClick='closeWork()'
 */
function addTab(sTitle,sIcon,sUrl){
	var oCel = tabList.insertCell();//在tab页标题区插入一个单元格
	oCel.className = "text";//设置单元格的样式
	oCel.width = "100";//设置单元格的宽度
	oCel.height="25";//设置单元格的高度
	oCel.noWrap = true;//设置单元格不可换行属性
	oCel.title = sTitle;//设置单元格标题
	oCel.attachEvent("onclick",tab_doClick);//添加单元格的点击事件
	oCel.attachEvent("oncontextmenu",tab_doMenu);//添加单元格的右击事件
	var titleSegment = (sTitle.length > 8)? sTitle.substring(0,8)+".." : sTitle;//如果单元格显示字符个数大于8，则8位后的字符用..代替
	//oCel.innerHTML = "<IMG src='"+sIcon+"' id='img_switch'/><LABEL onmouseover=\"this.style.color='red'\" onmouseout=\"this.style.color='black'\" ondblclick=\"resizeWork(img_switch)\">"+titleSegment+"</LABEL>";
	//设置单元格内容
	oCel.innerHTML ="<table width='100%' height='22' border='0' cellpadding='00' cellspacing='0' onDblClick='closeWork()'>"+
						"<tr><td class='bt1_bg1'>&nbsp;</td>"+
						"<td align='center' class='bt1_bg2' noWrap valign='center'>"+
						"<IMG src='"+sIcon+"' id='img_switch'/>"+
						"<LABEL >"+titleSegment+"</LABEL>"+
						"</td>"+
						"<td width='14' class='bt1_bg2'>"+
						"<IMG src='<%=frameroot%>/images/tab/close_03.jpg' id='img_close' align='bottom' onclick='closeWork()'/>"+
						"</td>"+
						"<td width='27' class='bt1_bg3'>&nbsp;</td>"+
						"</tr></table>";
	var aParams = new Array(3);
	aParams["TITLE"] = sTitle;//记录Tab页标题
	aParams["URL"] = sUrl;//记录Tab页内容链接
	aParams["ID"] = createTabId();//创建Tab页编号
	setTabParams(aParams,oCel.cellIndex);//将该Tab页信息加入TAB_LIST中
	
	addPanels(sTitle,sIcon,sUrl);//添加Tab页内容
	setSelectedIndex(oCel.cellIndex);//设置当前选中的tab页为新添加的tab页
	tabContainer.scrollLeft = oCel.offsetLeft;//将tab容器的横向滚动位置设置为新添加的tab页的相对左位移
	flagDir();//指示出被隐藏的当前Tab页的方向
}



/**
 * 随即创建列号
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
	var oRow = workPanel.insertRow();//在tab页内容区插入一行
	//var oRow1 = workPanel.insertRow();
	setPanelsVisibility(false,oRow.rowIndex-1);//调整Panels的可见性，设置上一个Panel不可见，即设置原当前Panel不可见
	var oCel = oRow.insertCell();//在插入的行中插入单元格
	//设置单元格的内容，此处是嵌入一个iframe
	oCel.innerHTML = "<IFRAME frameBorder=\"0\" scrolling=\"no\" onreadystatechange=\"stateListener(this)\" width=\"100%\" height=\"100%\" src=\""+sUrl+"\" >对不起，您的浏览器不支持IFRAME!</IFRAME>";
	//oRow1.valign = "bottom";	
	//oRow1.style.height = 20;
	//oCel = oRow1.insertCell();
	//oCel.noWrap = true;
	//oCel.innerHTML = "<IMG src='./images/info.gif' /><INPUT readonly type=text style='border:0;width:100%;color:#015ebc;font-size:11pt;background-color:#eeeeee' title='工作区消息' value='工作区消息：' ondblclick='alert(\"消息：\\n\"+this.value)'/>";
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
    //如果当前没有打开的tab页，则直接返回
	if(tabList.cells.length < 1){
		return ;
	}
	
	//如果idx索引超过了tab页最大的索引位置，即发生了索引越界，则直接返回
	if(!checkBound(idx)){
		return;
	}
	
	//删除对应索引在标题区的的单元格
	tabList.deleteCell(idx);
	//labPanel.innerText = "";
	removeTabParams(idx);//在TAB_LIST中移除对应索引的tab页对象
	removePanelsAt(idx);//删除对应索引在内容区的的行
	
	//如果删除的索引位置小于当前选中的索引位置，则当前选中的索引位置减一
	if(idx < SELECTED_INDEX){
		SELECTED_INDEX --;
	}
	//如果删除的索引位置等于当前选中的索引位置，则当前选中的索引位置设为-1，并重新设置最后一个tab页为当前选中页
	else if(idx == SELECTED_INDEX){
		SELECTED_INDEX = -1;
		setSelectedIndex(tabList.cells.length-1);////设置当前选中的tab页为最后一个tab页
	}
}
/** 删除指定的Tab后，需要删除相应的Panel
 * @param idx 索引
 */
function removePanelsAt(idx){
	var rowIdx = idx;//2*idx;
	//alert(rowIdx);
	//如果要删除的索引位置不为空并且小于tab页总数，则删除工作区对应的行
	if(idx!=null&&idx>=0&&idx<workPanel.rows.length)
		workPanel.deleteRow(idx);
	//workPanel.deleteRow(rowIdx);
}
/** 给指定的Tab设置图标
 * @param sImg 图标
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
	setTabParam("TITLE",sTitle,idx);//在TAB_LIST中根据索引设置对应tab页对象对应的值
	
	//如果设置的标题内容大小大于8，则将8位后的字符设为..，重新设置对应单元格标题
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
	toLeft.src = "<%=frameroot%>/images/tab/arrow1.gif";//向左滚动图片
	toRight.src = "<%=frameroot%>/images/tab/arrow2.gif";//向右滚动图片
	if(idx == -1){
		return ;
	}
	//自动滚动Tab页
	var oCel = tabList.cells(idx);//获取当前选择的单元格

	//如果单元格的左位移加上其宽度大于tab容器的左位移和其宽度之和，即发生了内容溢出，则设置容器自动向右滚动
	if((oCel.offsetLeft+oCel.offsetWidth) >= (tabContainer.scrollLeft+tabContainer.offsetWidth)){
		tabContainer.doScroll("pageRight");
	}
	//如果单元格的左位移小于等于于tab容器的左位移，则设置容器自动向左滚动
	else if(oCel.offsetLeft <= tabContainer.scrollLeft){
		tabContainer.doScroll("pageLeft");
	}
	
	//如果原纪录的当前tab页索引等于idx指示的索引大小，则返回不做处理
	if(SELECTED_INDEX == idx){
		return;
	}
	
	//如果原纪录的当前tab页索引不为-1，即原有已选择的当前tab页，则需要将原选中的tab页修改为未选中状态
	if(SELECTED_INDEX != -1){
		//tabList.cells(SELECTED_INDEX).className = "tab";
		tabList.cells(SELECTED_INDEX).childNodes[0].rows[0].cells[0].className = "bt2_bg1";
		tabList.cells(SELECTED_INDEX).childNodes[0].rows[0].cells[1].className = "bt2_bg2";
		tabList.cells(SELECTED_INDEX).childNodes[0].rows[0].cells[2].className = "bt2_bg2";
		tabList.cells(SELECTED_INDEX).childNodes[0].rows[0].cells[2].innerHTML = "&nbsp;";
		tabList.cells(SELECTED_INDEX).childNodes[0].rows[0].cells[3].className = "bt2_bg3";
		setPanelsVisibility(false,SELECTED_INDEX);//调整Panels的可见性,设置该Panel不可见
	}
	//使新的Tab被选中
	//oCel.className = "tab_sel";
	oCel.childNodes[0].rows[0].cells[0].className = "bt1_bg1";
	oCel.childNodes[0].rows[0].cells[1].className = "bt1_bg2";
	oCel.childNodes[0].rows[0].cells[2].className = "bt1_bg2";
	oCel.childNodes[0].rows[0].cells[2].innerHTML = "<IMG src='<%=frameroot%>/images/tab/close_03.jpg' id='img_close' align='bottom' onclick='closeWork()'/>";
	oCel.childNodes[0].rows[0].cells[3].className = "bt1_bg3";
	//labPanel.innerText = getTabParam("TITLE",idx);
	setPanelsVisibility(true,idx);//调整Panels的可见性,设置该Panel可见
	SELECTED_INDEX = idx;//重新记录当前tab页索引
	oCel.focus();
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

/** 判断对应索引位置是否越界
 *  @return true：未越界，false：索引越界
 */
function checkBound(idx)
{
	if(idx < 0 || idx > tabList.cells.length -1){
		alert("索引越界:"+idx+">"+(tabList.cells.length -1));
		return false;
	}
	return true;
}

/** 判断是否已存在同标题的工作区
 * @param title 链接的标题
 * @return true:存在相同标题工作区，false:不存在相同标题工作区
 */
function containsTab(title){
	var idx = locateTabParam("TITLE",title);
	return idx;
}
</SCRIPT>

		<SCRIPT LANGUAGE="JavaScript">

var MAX_TAB_COUNT = 20;//最大tab页个数
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
		SYS_RES = new ActiveXObject("Dcbassrv.SystemRes");//获取系统资源占用情况
	}catch(e){
		//alert("new sys res:"+e.message);
	}
}
//top.setSplash("初始化系统工作区...",20);


function setMessage(str,flag,oWin)
{
	var src = "<%=frameroot%>/images/tab/info.gif";
	switch(flag)
	{
		case 0:
			src = "<%=frameroot%>/images/tab/info.gif";
			break;
		case 1:
			src = "<%=frameroot%>/images/tab/warning.gif";
			break;
		case 2:
			src = "<%=frameroot%>/images/tab/error.gif";
			break;
		case 3:
			src = "<%=frameroot%>/images/tab/error.gif";
			break;
		case -1:
			src = "<%=frameroot%>/images/tab/info.gif";
			break;
		default:
			src = "<%=frameroot%>/images/tab/info.gif";
	}
	var rowIdx = findCallerIndex(oWin) + 1;
	var cel = workPanel.rows(rowIdx).cells(0);//.innerHTML = sFlag+str;
	if(cel != null){
		cel.children(0).src = src;
		cel.children(1).value = str;
		cel.children(1).title = str;
	}else{
		alert("设置工作区消息出错",1);
	}
}
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
 
 /*根据关闭标题名为sTitle的工作区*/
 function closeWorkByNames(sTitle){
 	closeWorkByIdx("",containsTab(sTitle));
 }

/** 导航页面
 * @param sUrl 链接地址
 * @param sTitle 链接的标题
 * @param bl 是否在新窗口打开工作区
 */
function navigate(sUrl,sTitle,bl){
	if(bl == null || !bl){
		if(containsTab(sTitle)!=-1){//判断是否已存在同标题的工作区
			//if(!confirm("工作区["+sTitle+"]已经存在，\n您确定还要打开新的工作区吗？")){
			//	return;
			//}
			closeWorkByIdx("",containsTab(sTitle));
		}
		if(IS_WIN98 && SYS_RES != null){//如果操作系统是WIN98，并且系统资源占用情况不为空
			try{
				if(SYS_RES.FreeUserRes < SYS_USER || SYS_RES.FreeGDIRes < SYS_GDI){//如果系统剩余资源少于需要的资源，则进行相应提示
					alert("警告（仅在WINDOWS98下会发生）：\nWINDOWS98系统的剩余资源太少，请先关闭多余的工作区。\n然后再尝试打开新的工作区。");
					return;
				}

			}catch(e){
				//alert("check sys res:"+e.message);
			}
		}
		if(getTabCount() < MAX_TAB_COUNT){//如果已打开的TAB页总数小于设置的最大TAB页总数，则可添加新的TAB页
			showLoading(true);//显示“正在加载”信息
			addTab(sTitle,"<%=frameroot%>/images/tab/state1.gif",sUrl);//添加TAB页
		}else{//如果已打开的TAB页总数超过设置的最大TAB页总数，则进行相应提示
			if(IS_WIN98){
				alert("系统提示：\n您打开的工作区过多，WINDOWS98的系统资源有限，请关闭多余的工作区");
				return;
			}else{
				alert("系统提示：\n您最多可以打开["+MAX_TAB_COUNT+"]个工作区\n请关闭多余的工作区");
				//navigate_new(sUrl,sTitle);
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
		//navigate_new(sUrl,sTitle);
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

/**通过右键菜单的方式根据索引关闭对应tab页
 * @param bl true/false是否强制退出，null==false
 */
function closeworkByRightMenu(bl,callerIdx){
	if(confirm ("您确认关闭吗?", "ok", "cancel")){	
		closeWorkByIdx(bl,callerIdx);
	}
}

/**根据索引关闭对应tab页
 * @param bl true/false是否强制退出，null==false
 */
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
	
	//logoutPage(callerIdx);
	//if(confirm ("您确认关闭吗?", "ok", "cancel")){	
		removeTabAt(callerIdx);//删除指定的Tab，idx不一定为当前的Tab
		showLoading(false);
		setTimeout("CollectGarbage()",500);//延迟500ms进行垃圾收集
		flagDir();//指示出被隐藏的当前Tab页的方向
	//}
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
	var sImg = bl? "<%=frameroot%>/images/tab/state2.gif" : "<%=frameroot%>/images/tab/state1.gif";
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
/** 刷新指定TITLE的工作区，如没有则打开一个新的工作区
 * @param sUrl 工作区的URL
 * @param sTitle 工作区标题
 */
function refreshWorkByTitle(sUrl,sTitle){
	var idx = findTabParam("TITLE",sTitle);
	if(idx == -1){
		navigate(sUrl,sTitle);
	}else{
		//add by dengzc 2009年6月15日17:29:52
		//start
		setSelectedIndex(idx);
		// end
		window.frames(idx).location=sUrl;
	}
}

//刷新当前工作区域
function refreshCurrentWork(oWin){
	var callerIdx = findCallerIndex(oWin);
	if(document.frames.length > 0&&callerIdx>=0&&callerIdx<document.frames.length){
		
		//帐号注册页面单独刷新
		if($("form",window.frames(callerIdx).document)[0].id=="usermanagesave"
			&&$("form",window.frames(callerIdx).document)[0].target=="hiddenFrame"){
				window.frames(callerIdx).location="<%=path%>/policeregister/policeRegister!input.action";
				return;
			}
		
		if($("form",window.frames(callerIdx).document).length > 0 ){
			$("form",window.frames(callerIdx).document)[0].submit();
		}else{
			window.frames(callerIdx).location.reload();
		}
	}
}
//setTimeout('refreshCurrentWork(oWin)',10000); 
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
/** 消息通知，提示用户
 * @param bl 激活true/false
 */
function setActive(bl,str)
{
	if(bl == "true")
	{
		MESSENGER.src = "<%=frameroot%>/images/tab/ring-2.gif";
		MESSENGER.title = "您收到了新消息！[点击查看历史记录]";
		try {
			str = parseMsg(str);
		}catch(e){
			str = "您收到了新消息！\n无法得到消息内容\n"+e.message;
		}
		showPopupWin();
		var oMsg = oPopup.document.all("MESSAGE");
		if(oMsg != null){
			if(str == null || str == ""){
				str = "您收到了新消息！\n无具体消息内容";
			}
			oMsg.innerText = str;
			pushMessage(str);
		}
	}
	else
	{
		MESSENGER.src = "<%=frameroot%>/images/tab/ring-1.gif";
		MESSENGER.title = "正在监听消息...[点击查看历史记录]";
		//SND_MSG.src = "";
		MessageBox.style.display = "none";
		hidePopupWin();
	}
}


var oPopup = null;
function showPopupWin(){
	oPopup = window.createPopup();
	oPopupBody = oPopup.document.body;
	//oPopupBody.onload = myInit;
	oPopupBody.style.borderRight = "solid 4 #666666";
	oPopupBody.style.borderBottom = "solid 4 #666666";
	oPopupBody.style.borderLeft = "solid 3 #b6b6b6";
	oPopupBody.style.borderTop = "solid 3 #b6b6b6";
	oPopupBody.bgColor='#F8F8F8';
	oPopupBody.innerHTML = MessageBox.innerHTML;
    var w=screen.width;
    var h=screen.height;
	oPopup.show(w/2-220,h/2-150,480,280);
	oPopup.document.all("MESSAGE").innerText = "您收到了新的系统消息！";
}
function hidePopupWin(){
	if (oPopup != null && oPopup.isOpen){
		oPopup.hide();
	}
}
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
/** 左边权限显示/隐藏的开关控制
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

function initDoc(){
	$.post("<%=root%>/desktop/desktopWhole!getDesktopPortalList.action",
    function(data){
    	var arr=data.split("|");
    	if(data.indexOf("HTTP 错误")==-1){
	    	for(var i=0;i<arr.length;i++){
	    		var info=arr[i].split("#");
	    		if(info[1]!=null&&info[1]!=""){	
	    			navigate('<%=path%>/desktop/desktopWhole.action?defaultType=2&portalId='+info[1],info[0]);
	    		}else{
	    			//navigate('<%=path%>/desktop/desktopWhole.action',info[0]);
	    		}
	    	}
    	}else{
    		navigate('<%=path%>/common/error/403.jsp',"无权访问");
    	}
		try{
			msg2.StartListen();
		}catch(e){
		}
		if(window.top.opener != null && window.top.document.URL.indexOf("ptl=yes")!=-1)eval(unescape(tmp_.text))
	});
}


//setInterval("initDoc()",10000);

</SCRIPT>


<script for="window" event="onload">
	initDoc();
	state = true;//初始化完毕
	
	//navigate('<%=path%>/desktop/desktopWhole!gotoBgtDesktop.action','办公门户');
	
</script>
	</head>
	<body class=toolbarbodymargin>
		<table border=0 cellSpacing=0 cellPadding=0 width=100% height="100%"
			style="margin: 0;padding: 0">
			<!-- 横向TAB页 开始 -->
			<tr>
				<td height="28" width="*">
					<DIV id=toolbarborder>
						<DIV id=toolbar style="height: 28;">
							<TABLE onselectstart="return false" border=0
								style="position: absolute; top: 5; width: 100%; height: 28; z-index: 0"
								cellSpacing=0 cellPadding=1>
								<TR>
									<TD style="padding-left: 0">
										<DIV id="tabContainer"
											style="position: absolute; top: 2; width: 100%; height: 22; font-size: 15; overflow-y: no; overflow-x: hidden;">
											<TABLE style=" height: 22;" border=0 cellSpacing=2
												cellPadding=0>
												<TR id=tabList><!-- TAB页标题区 -->
												</TR>
											</TABLE>
										</DIV>
									</TD>
									<TD style="width: 105; cursor: hand;" nowrap align="right" valign="bottom">
										<img src="<%=frameroot%>/images/tab/arrow1.gif" id="toLeft" style=" display:none"
											onMouseOver="scroll_doMouseOver(this)"
											onMouseOut="scroll_doMouseOut(this)" title="向左滚动"  
											onClick="scrollTab('PageLeft')" />
										<img src="<%=frameroot%>/images/tab/arrow2.gif" id="toRight" style=" display:none"
											onMouseOver="scroll_doMouseOver(this)"
											onMouseOut="scroll_doMouseOut(this)" title="向右滚动"
											onClick="scrollTab('PageRight')" />
										<img src="<%=frameroot%>/images/tab/Back.png" title="当前工作区后退" width="17px" height="17px"
											onClick="history.back();"
											onMouseOver="this.src='<%=frameroot%>/images/tab/Back_o.jpg'"
											onMouseOut="this.src='<%=frameroot%>/images/tab/Back.png'" />
										<img src="<%=frameroot%>/images/tab/refresh_o_03.png" width="17px" height="17px"
											title="刷新当前工作区" onClick="refreshCurrentWork()"
											onMouseOver="this.src='<%=frameroot%>/images/tab/refresh_z_03.gif'"
											onMouseOut="this.src='<%=frameroot%>/images/tab/refresh_o_03.png'" />
										<img src="<%=frameroot%>/images/tab/close.gif" title="关闭当前工作区" width="17px" height="17px"
											onClick="closeWork()"
											onMouseOver="this.src='<%=frameroot%>/images/tab/close1.gif'"
											onMouseOut="this.src='<%=frameroot%>/images/tab/close.gif'" />
									</TD>
								</TR>
							</TABLE>
						</DIV>
					</DIV>
				</td>
			</tr>
			<!-- 横向TAB页 截止 -->
			<tr>
				<td height="95%">
					<TABLE
						style="padding-left: 0; padding-right: 0; padding-top: 0; padding-bottom: 0;"
						border=0 width="100%" height="100%" cellpadding="0"
						cellspacing="0">
						<TBODY id="workPanel"><!-- TAB页内容区 -->
						</TBODY>
					</TABLE>
				</td>
				<!-- 纵向TAB页 开始
				<td width="22" rowspan="2" height="95%">
					<table border=0 cellSpacing=0 cellPadding=0 width="22"
						height="100%">
						<tr>
							<%
								String theme=frameroot.substring(frameroot.lastIndexOf("/")+1);
								if(theme.equals("theme_red")){
							 %>
								<td width="22" height="89" onclick="showWorkDeal()"  style="cursor: hand;color:#ffffff; padding-left:2px;"
									background="<%=frameroot%>/images/perspective_right/work_03.jpg">
							<%
								}else{
							 %>
							 	<td width="22" height="89" onclick="showWorkDeal()"  style="cursor: hand; padding-left:2px;"
								background="<%=frameroot%>/images/perspective_right/work_03.jpg">
							<%
								} 
							%>
								我的流程
							</td>

						</tr>
						<tr>
							<%
									if(theme.equals("theme_red")){
							 %>
								<td width="22" height="89" onclick="showLinkMan()" style="cursor: hand;color:#ffffff; padding-left:2px;"
									background="<%=frameroot%>/images/perspective_right/work_03.jpg">
							<%
								}else{
							 %>
							 	<td width="22" height="89" onclick="showLinkMan()" style="cursor: hand; padding-left:2px;"
									background="<%=frameroot%>/images/perspective_right/work_03.jpg">
							 <%
								} 
							%>
								联系人
							</td>
						</tr>
						<tr>
							<td width="22" height="*">
								&nbsp;
							</td>
						</tr>
					</table>
					<div id="workdealbox" align="center" style="display: none;position:absolute;z-index:10;width:205px;height:100%;top:30px;right:24px;border: 1px solid #A2C7D9;" class=toolbarbodymargin>
						<iframe id="workdealframe" src="<%=path%>/fileNameRedirectAction.action?toPage=frame/perspective_content/actions_container/workdealboxiframe.jsp" height="560px" width="205px" frameborder="0" scrolling="auto"></iframe>
					</div>
					<div id="linkmanbox" align="center" style="display: none;position:absolute;z-index:10;width:205px;height:100%;top:30px;right:24px;border: 1px solid #A2C7D9;" class=toolbarbodymargin>
						<iframe id="" src="<%=path%>/fileNameRedirectAction.action?toPage=frame/perspective_content/actions_container/linkmanboxiframe.jsp" height="560px" width="205px" frameborder="0" scrolling="no"></iframe>
					</div>
				</td>
				纵向TAB页 截止 -->
			</tr>
		</table>
		<SCRIPT>
	document.writeln(tabrightmenu.getMenu());
	function changeWorkdealBoxWidth(wth){
		var objPCM = document.getElementById('workdealbox');
		var objFRM = document.getElementById('workdealframe');
		objPCM.style.width=wth;
		objFRM.width=wth;
	}
	function showWorkDeal(){
		var objPCM = document.getElementById('workdealbox');
			if(objPCM.style.display == "none"){
				//Element.show('workdealbox');
				//Element.hide('linkmanbox');
				$("#workdealbox").show();
				$("#linkmanbox").hide();
			}
			else{
				//Element.hide('workdealbox');	
				$("#workdealbox").hide();
			}
	}
	function showLinkMan(){
		var objPCM = document.getElementById('linkmanbox');
			if(objPCM.style.display == "none"){
				//Element.show('linkmanbox');
				//Element.hide('workdealbox');
				$("#linkmanbox").show();
				$("#workdealbox").hide();
			}
			else{
				//Element.hide('linkmanbox');	
				$("#linkmanbox").hide();
			}
	}
	function showContent(obj,ids){
		var contentdiv = document.getElementById(ids);
		
		if(contentdiv.style.display == "none"){
			contentdiv.style.display="";
			obj.src=frameroot+"/images/perspective_right/small_ico_19.jpg";
			loadWorkInfo(contentdiv,contentdiv.type);
		}else{
			contentdiv.style.display="none";
			obj.src=frameroot+"/images/perspective_right/small_ico_26.jpg";
		}
	}
	
	function loadWorkInfo(contentdiv,listMode){
		var myAjax = new Ajax.Request(
             '<%=path%>/work/work!ajaxWorkList.action', // 请求的URL
            {
                //参数
                parameters : 'listMode='+listMode,
                method:  'post', 
                // 指定请求成功完成时需要执行的js方法
                onComplete: function(response){
                	contentdiv.innerHTML=response.responseText;
                }
            }
        );
	}
	//显示个人通讯录
	function showPerAddress(){
		peradd_left.className="bg1_left";
		peradd_mid.className="bg1_mid";
		peradd_right.className="bg1_right";
		sysadd_left.className="bg2_left";
		sysadd_mid.className="bg2_mid";
		sysadd_right.className="bg2_right";
		
		var contact = document.getElementById("contact");
		if(contact.src != "<%=root %>/address/addressGroup!systree.action"){
			contact.src = "<%=root %>/address/addressGroup!systree.action";	
		}
	}
	 //回车搜索
	   function startSearch(obj, evt) {
	   		//判断是否是回车
			var keyCode = event.keyCode ? event.keyCode : event.which ? event.which : event.charCode;
			//var userName = encodeURI(encodeURI(obj.value));
			if (keyCode == 13) {				
				doSearch(obj.value);
			}	   		
	   }
	   function doSearch(userName){
	   if(userName==""|userName==null|userName=="搜索联系人..."){
	   		return ;
	   }
	   userName = encodeURI(encodeURI(userName));
	    /*var contentdiv = document.getElementById("searchResult");
	   	var myAjax = new Ajax.Request(
             '<%=path%>/address/addressOrg!searchUser.action', // 请求的URL
            {
                //参数
                parameters : 'userName='+userName,
                method:  'post', 
                // 指定请求成功完成时需要执行的js方法
                onComplete: function(response){
                	contentdiv.innerHTML=response.responseText;
                }
            }
        );*/
        var contact = document.getElementById("contact");
        var searchType = "";
        if(peradd_left.className=="bg1_left"){//在个人通讯录中搜索
        	searchType = "personal";
        }else{//在系统通讯录中搜索
        	searchType = "public";
        }
        contact.src = "<%=path%>/address/addressOrg!searchUser.action?userName="+userName+"&searchType="+searchType;
	   }
	function showSysAddress(){
		peradd_left.className="bg2_left";
		peradd_mid.className="bg2_mid";
		peradd_right.className="bg2_right";
		sysadd_left.className="bg1_left";
		sysadd_mid.className="bg1_mid";
		sysadd_right.className="bg1_right";
		
		var contact = document.getElementById("contact");
		if(contact.src != "<%=root %>/address/addressOrg!systree.action"){
			contact.src = "<%=root %>/address/addressOrg!systree.action";	
		}
	}
		
//setInterval("refreshCurrentWork()",10000);
	</SCRIPT>
		<DIV id=LOADING onClick="showLoading(false)"
			style="position: absolute; top: 30%; left: 38%; display: none"
			align=center>
			<font color=#16387C><strong>正在加载，请稍候...</strong> </font>
			<br>
			<IMG src="<%=frameroot%>/images/tab/loading.gif">
		</DIV>

	</BODY>
</html>
