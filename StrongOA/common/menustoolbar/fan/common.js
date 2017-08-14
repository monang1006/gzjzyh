function ispiritWin(url,width_s,height_s){
 	var width = width_s;
 	var height = height_s;
 	if(width==''||width=='null'){
 		width = screen.availWidth;
 	}
 	if(height==''||height=='null'){
 		height = screen.availHeight;
 	}
    window.open(url, '', 'left=0, top=0, Width='+width+', height='+height+', toolbar=no, menubar=no,scrollbars=yes, resizable=yes,location=no, status=1') ;
}
var prefixes = ["MSXML2.DomDocument", "Microsoft.XMLDOM", "MSXML.DomDocument", "MSXML3.DomDocument"];
var dom;

var FolderCount = 19;
var thumbCount = 4;
var FolderLeavings = 4;

var arrayFolder;
var tbl;
var fileTD;
var rightArrow;
var handleOffsetHeight;

var currentThumbCount,currentMenuId,currentTD;

function getDomObject(){
	for (var i = 0; i < prefixes.length; i++) {
		try{dom = new ActiveXObject(prefixes[i]);}catch(ex){};
	}
}

window.onload = function(){
	/*initialize*/
	getDomObject();
	getXML();
	getCookie();
	//currentMenuId = 0;
	createMenu();
	//document.getElementById("ifrm").contentWindow.attachEvent("onresize",loadHTML2);

	arrayFolder = new Array();
	tbl = document.getElementById("tbl");
	fileTD = document.getElementById("fileTD");
	rightArrow = document.getElementById("rightArrow");
	
	for(var i=0;i<currentThumbCount;i++){
		delRow();
	}

	memorizeThumb();

	getHandleOffsetHeight();
};

window.onbeforeunload = function(){
	setCookie(currentThumbCount,currentMenuId);
};

window.onresize = function(){
	if(window.document.body.offsetWidth<120) parent.document.getElementById("frame2").cols = "120,*";

	memorizeThumb();
	getHandleOffsetHeight();
};

function getXML(){
	dom.async = false;
	dom.loadXML(menus);	
}

function getHandleOffsetHeight(){
	var tblTop = 0;
	obj = tbl.rows[3];
	while(obj.tagName!="BODY"){
		tblTop += obj.offsetTop;
		obj = obj.offsetParent;
	}
	handleOffsetHeight = tblTop;
}

function memorizeThumb(){
	var remainRows = Math.ceil((document.body.clientHeight-menuMargin-102)/23)-1;
	if(tbl.rows.length-4>remainRows){
		for(var i=1;i<=tbl.rows.length-4-remainRows;i++){
			if(tbl.rows.length>4){
				delRow();
			}
		}
	}else if(tbl.rows.length-4<remainRows){
		for(var i=1;i<=remainRows-(tbl.rows.length-4);i++){
			if(arrayFolder.length>currentThumbCount){
				addRow();
			}
		}
	}
}

function loadHTML(){
	with(document.getElementById("ifrm")){
		try{
			contentWindow.document.body.style.margin = "0";
			contentWindow.document.body.style.padding = "0";
			
			var nodeMenubar = dom.selectSingleNode("//menubar[@id='"+currentMenuId+"']");

			if(nodeMenubar.getAttribute("extra")==null){
				//contentWindow.document.body.innerHTML = "<div style='padding:10px;font-size:12px'>"+nodeMenubar.firstChild.text+"</div>";
				contentWindow.document.body.innerHTML = "";
				detachEvent("onload",loadHTML);
				//alert(nodeMenubar.getAttribute("extra"));
				//alert(dom.selectSingleNode("//menubar[@id='"+currentMenuId+"']").getAttribute("levelid"));
				//alert(nodeMenubar);
				src = "/StringOA/theme/theme!RefreshPrivilist.action?priviparent="+dom.selectSingleNode("//menubar[@id='"+currentMenuId+"']").getAttribute("levelid");
				//src="/StringOA/help.jsp";
			}else if(nodeMenubar.getAttribute("extra")=="systemSetting"){
				detachEvent("onload",loadHTML);
				src = "";
			}
		}catch(e){}
	}
}

function loadHTML2(){
	var nodeMenubar = dom.selectSingleNode("//menubar[@id='"+currentMenuId+"']");
	if(nodeMenubar.getAttribute("extra")==null){
		with(document.getElementById("ifrm").contentWindow.document.body){
			style.fontFamily = "MS Shell Dlg";
			style.fontSize = "12px";
			innerHTML = nodeMenubar.firstChild.text;
		}
	}
}

function createMenu(){
	var oTbl,oTR,oTD;

	oTbl = document.createElement("table");
	oTbl.id = "tbl";
	oTbl.cellSpacing = "1";
	oTbl.className = "OTTable";

	oTR = oTbl.insertRow();
	oTD = oTR.insertCell();
	if(dom.selectSingleNode("//menubar[@id='"+currentMenuId+"']")==null){
		currentMenuId=0;
	}
	oTD.setAttribute("menuid",dom.selectSingleNode("//menubar[@id='"+currentMenuId+"']").getAttribute("id"));
	oTD.className = "folder";
	oTD.innerHTML = "<img class='topicon' src='"+dom.selectSingleNode("//menubar[@id='"+currentMenuId+"']").getAttribute("icon")+"'/>" +"<span class='topword'>"+ dom.selectSingleNode("//menubar[@id='"+currentMenuId+"']").getAttribute("name")+"</span><img class='topimg' src='"+scriptroot+"/common/menustoolbar/fan/images/top_bg.png' class='topimg'>";
	oTR = oTbl.insertRow();
	oTD = oTR.insertCell();
	oTD.id = "fileTD";
	oTD.className = "file";
	var oIframe = document.createElement("iframe");
	oIframe.id = "ifrm";
	oIframe.scrolling="no";
	oIframe.style.width = "100%";
	oIframe.style.height = "100%";
	oIframe.frameBorder = "0";
	oIframe.attachEvent("onload",loadHTML);
	oTD.appendChild(oIframe);

	oTR = oTbl.insertRow();
	oTD = oTR.insertCell();
	oTD.className = "handle";
	oTD.onmousedown = function(){mousedown();};
	oTD.onmouseup = function(){mouseup();};
	oTD.onmousemove = function(){mousemove();};

	var oNodes = dom.selectNodes("//menubar");
	for(i=0;i<oNodes.length;i++){
		oTR = oTbl.insertRow();
		oTD = oTR.insertCell();
		oTD.setAttribute("menuid",dom.selectSingleNode("//menubar["+i+"]").getAttribute("id"));
		oTD.className = "folder";
		oTD.attachEvent("onmouseover",folderMouseOver);
		oTD.attachEvent("onmouseout",folderMouseOut);
		oTD.onclick = function(){slideFolder(this);};
		oTD.innerHTML = "<img class='topicon' src='"+dom.selectSingleNode("//menubar["+i+"]").getAttribute("icon")+"'/>" +"<span class='topword'>"+dom.selectSingleNode("//menubar["+i+"]").getAttribute("name")+"</span><img class='topimg' src='"+scriptroot+"/common/menustoolbar/fan/images/top_bg.png' class='topimg'>";
	}

	oTR = oTbl.insertRow();
	oTD = oTR.insertCell();
	oTD.id = "thumbBox";
	oTD.innerHTML = "<img id='rightArrow' src='"+scriptroot+"/common/menustoolbar/fan/images/open_win_arrow.gif' onclick='showFav()'/>";
	document.getElementById("divMenuBox").rows[0].cells[0].appendChild(oTbl);
}

function folderMouseOver(){
	var o = window.event.srcElement;
	var this_width = o.width;
	if(o.tagName!="TD"){
		o.parentNode.className="folderMouseOver";
	}
	//if(o.tagName=="IMG") o.className = "topimgover";
}

function folderMouseOut(){
	var o = window.event.srcElement;
	if(o.tagName!="TD"){
		if(o.parentNode.menuid!=currentMenuId){
			o.parentNode.className="folder";
		}
	}
}

function hideLoading(){
	document.getElementById("loading").style.display = "none";
	document.getElementById("ifrm").detachEvent("onload",hideLoading);
}


function slideFolder(o){
	//o.detachEvent("onmouseover",folderMouseOver);
	//o.detachEvent("onmouseout",folderMouseOut);
	//alert(currentTD);
	if(currentTD!=null && currentTD!=o.getAttribute("menuid")){
		if(tbl.rows[3+parseInt(currentTD)]!=null && (tbl.rows.length-1)!=(3+parseInt(currentTD))){
			tbl.rows[3+parseInt(currentTD)].cells[0].className = "folder";
			tbl.rows[3+parseInt(currentTD)].cells[0].attachEvent("onmouseover",folderMouseOver);
			tbl.rows[3+parseInt(currentTD)].cells[0].attachEvent("onmouseout",folderMouseOut);
		}
	}
	currentTD = o.getAttribute("menuid");
	
	//document.getElementById(currentMenuId).attachEvent("onmouseover",folderMouseOver);
	//document.getElementById(currentMenuId).attachEvent("onmouseout",folderMouseOut);

	clickRow = o.parentElement.rowIndex;
	currentMenuId = o.getAttribute("menuid");

	var nodeMenubar = dom.selectSingleNode("//menubar[@id='"+currentMenuId+"']");
	tbl.rows[0].firstChild.innerHTML = "<img class='topicon' src='"+dom.selectSingleNode("//menubar[@id='"+currentMenuId+"']").getAttribute("icon")+"'/>" +"<span class='topword'>"+ dom.selectSingleNode("//menubar[@id='"+currentMenuId+"']").getAttribute("name")+"</span><img class='topimg' src='"+scriptroot+"/common/menustoolbar/fan/images/top_bg.png' class='topimg'>";

	var oIframe = document.getElementById("ifrm");

	if(nodeMenubar.getAttribute("extra")==null){
		//oIframe.contentWindow.attachEvent("onresize",loadHTML2);
		//oIframe.contentWindow.document.body.innerHTML = nodeMenubar.firstChild.text;
		loadHTML();
	}else if(nodeMenubar.getAttribute("extra")=="systemSetting"){
		//oIframe.contentWindow.document.body.innerHTML = "Loading...";
		//fileTD.insertAdjacentHTML("afterBegin","<div id='loading'' style='font-family:MS Shell Dlg;font-size:12px;color:#999;font-weight:bold;margin:5px'><img src='/images/loading2.gif' style='vertical-align:middle;margin-right:0'/> Loading...</div>");
		oIframe.detachEvent("onload",loadHTML);
		//oIframe.attachEvent("onload",hideLoading);
		oIframe.src = "/MainMenuTree.jsp";
	}
	//setCookie(currentThumbCount,currentMenuId);
}

function mousedown(){
	el = window.event.srcElement;
	while(el.tagName!="TD"){
		el = el.parentElement;
	}
	el.setCapture();
}

function mouseup(){
	el.releaseCapture();
}


function mousemove(){
	getHandleOffsetHeight();

	window.event.cancelBubble = false;
	cliX = window.event.clientX;
	cliY = window.event.clientY;
	//alert(cliY);
	if(menuMargin<6){
		if(cliY<327) return false;
	}else{
		
	if(cliY<305) return false;
	}

	
	if(cliY>handleOffsetHeight+22 && tbl.rows.length<=FolderCount && tbl.rows.length>FolderLeavings){
		delRow();
		currentThumbCount++;
		handleOffsetHeight+=22;
	}
	if(cliY<handleOffsetHeight-22 && tbl.rows.length<FolderCount && tbl.rows.length>=FolderLeavings){
		addRow();
		currentThumbCount--;
		handleOffsetHeight-=22;
	}
	//setCookie(currentThumbCount,currentMenuId);
}

function delRow(){

	if(tbl.rows[tbl.rows.length-2].firstChild.className=="handle") return false;
	arrayFolder.push(tbl.rows[tbl.rows.length-2].firstChild.getAttribute("menuid")+"|"+tbl.rows[tbl.rows.length-2].firstChild.innerHTML);
	
	var oImg = document.createElement("img");
	oImg.setAttribute("menuid",tbl.rows[tbl.rows.length-2].firstChild.getAttribute("menuid"));

//alert(tbl.rows[tbl.rows.length-2].firstChild.children[0].nextSibling.innerHTML);
	oImg.setAttribute("menuname",tbl.rows[tbl.rows.length-2].firstChild.children[0].nextSibling.innerHTML);
	oImg.src = tbl.rows[tbl.rows.length-2].firstChild.firstChild.src;
	oImg.onclick = function(){slideFolder(this)};

	if(arrayFolder.length<=thumbCount){
		tbl.rows[tbl.rows.length-1].firstChild.insertBefore(oImg,rightArrow);
	}else{
		insertToPopupMenu(oImg);
	}

	tbl.deleteRow(tbl.rows.length-2);
}

function insertToPopupMenu(o){
	var tbl,tbl2,tr,td;
	tbl = document.createElement("table");
	tbl.cellspacing = 0;
	tbl.cellpadding = 0;
	tbl.width = "100%";
	tbl.height = "100%";
	tr = tbl.insertRow();
	td = tr.insertCell();
	td.width = 28;
	td.innerHTML = "<img src='"+o.src+"'/>";
	td = tr.insertCell();
	td.innerHTML = o.getAttribute("menuname");

	tbl2 = document.getElementById("divFavContent").firstChild.firstChild;
	tr = tbl2.insertRow();
	td = tr.insertCell();
	tr.height = 22;
	td.className = "popupMenuRow";
	td.setAttribute("menuid",o.getAttribute("menuid"));
	td.appendChild(tbl);
}

function addRow(){
	var oTR = tbl.insertRow(tbl.rows.length-1);
	var oTD = document.createElement("td");
	var arrayTmp = arrayFolder.pop().split("|");
	oTD.setAttribute("menuid",arrayTmp[0]);
	oTD.innerHTML = arrayTmp[1];
	if(arrayTmp[0]==currentMenuId){
		oTD.className = "folderMouseOver";
	}else{
		oTD.className = "folder";
		oTD.attachEvent("onmouseover",folderMouseOver);
		oTD.attachEvent("onmouseout",folderMouseOut);
	}
	oTD.onclick = function(){slideFolder(this)};
	oTR.appendChild(oTD);

	if(document.getElementById("divFavContent").firstChild.firstChild.rows.length>2){
		document.getElementById("divFavContent").firstChild.firstChild.deleteRow();
	}else{
		var tmp = tbl.rows[tbl.rows.length-1].firstChild;
		tmp.removeChild(tmp.children[tmp.children.length-2]);
	}
}

function setCookie(cThumbCount,cMenuId){ 
	var cookieDate = new Date();
	cookieDate.setTime(cookieDate.getTime() + 10*365*24*60*60*1000);
	document.cookie = "cookieLeftMenuzjg="+cThumbCount+","+cMenuId+";expires="+cookieDate.toGMTString();
}

function getCookie(){ 
	var cookieData = new String(document.cookie); 
	var cookieHeader = "cookieLeftMenuzjg=" 
	var cookieStart = cookieData.indexOf(cookieHeader) + cookieHeader.length; 
	var cookieEnd = cookieData.indexOf(";", cookieStart); 
	if(cookieEnd==-1){ 
		cookieEnd = cookieData.length;
	}
	if(cookieData.indexOf(cookieHeader)!=-1){ 
		currentThumbCount = cookieData.substring(cookieStart, cookieEnd).split(",")[0];
		currentMenuId = cookieData.substring(cookieStart, cookieEnd).split(",")[1];
	}else{
		currentThumbCount = 0;
		currentMenuId = dom.selectSingleNode("//menubar[0]").getAttribute("id");
	}
}



/*
==============================================
PopupMenu
==============================================
*/
var oPopup = window.createPopup();
function GetPopupCssText(){
	var styles = document.styleSheets;
	var csstext = "";
	for(var i=0; i<styles.length; i++){
		if (styles[i].id == "popupmanager")
			csstext += styles[i].cssText;
	}
	return csstext;
}

function showFav(){
	var popupX = 0;
	var popupY = 0;
	contentBox = document.getElementById("divFavContent");
	var o = event.srcElement;
	while(o.tagName!="BODY"){
		popupX += o.offsetLeft;
		popupY += o.offsetTop;
		o = o.offsetParent;
	}
	var oPopBody = oPopup.document.body;
	var s = oPopup.document.createStyleSheet();
	s.cssText = GetPopupCssText();
    oPopBody.innerHTML = contentBox.innerHTML;
	oPopBody.attachEvent("onmouseout",mouseout);

	//
	for(var i=0;i<oPopup.document.getElementsByTagName("TD").length;i++){
		if(oPopup.document.getElementsByTagName("TD")[i].getAttribute("menuid")!=null){
			oPopup.document.getElementsByTagName("TD")[i].onclick = function(){slideFolder(this);};
			oPopup.document.getElementsByTagName("TD")[i].onmouseover = function(){this.className='popupMenuRowHover';};
			oPopup.document.getElementsByTagName("TD")[i].onmouseout = function(){this.className='popupMenuRow';};
		}
	}

	oPopup.show(0, 0, 100, 0);
	var realHeight = oPopBody.scrollHeight;
	oPopup.hide();

	oPopup.show(popupX+20, popupY, 100, realHeight, document.body);
}

function mouseout(){
	var x = oPopup.document.parentWindow.event.clientX;
	var y = oPopup.document.parentWindow.event.clientY
	if(x<0 || y<0) oPopup.hide();
}
