
var oPopup = window.createPopup();
function showFav() {
	var popupX = -120;
	var popupY = 25;
	contentBox = document.getElementById("divFavContent");
	var o = event.srcElement;
	while (o.tagName != "BODY") {
		popupX += o.offsetLeft;
		popupY += o.offsetTop;
		o = o.offsetParent;
	}
	var oPopBody = oPopup.document.body;
	var s = oPopup.document.createStyleSheet();
	s.cssText = GetPopupCssText();
	oPopBody.innerHTML = contentBox.innerHTML;
	oPopBody.attachEvent("onmouseout", mouseout);

	//
	if(oPopup.document.getElementsByTagName("TD").length>0){
		for (var i = 0; i < oPopup.document.getElementsByTagName("TD").length; i++) {
			if (oPopup.document.getElementsByTagName("TD")[i].getAttribute("menuid") != null) {
				oPopup.document.getElementsByTagName("TD")[i].onclick = function () {
					slideFolder(this);
				};
				oPopup.document.getElementsByTagName("TD")[i].onmouseover = function () {
					this.className = "popupMenuRowHover";
				};
				oPopup.document.getElementsByTagName("TD")[i].onmouseout = function () {
					this.className = "popupMenuRow";
				};
			}
			var clickhref = oPopup.document.getElementsByTagName("TD")[i].getAttribute("menuhref");
			if(clickhref != null){
	            //oPopup.document.getElementsByTagName("TD")[i].onmouseup = function(){clickGo(clickhref)};
				oPopup.document.getElementsByTagName("TD")[i].onmouseup = function(){clickGo(this)};
			}
		}
		oPopup.show(0, 0, 150, 0);
		var realHeight = oPopBody.scrollHeight;
		oPopup.hide();
		oPopup.show(popupX + 20, popupY, 150, realHeight, document.body);
	}
}
function clickGo(href){
	javascript:eval("showMenu('nav_"+href.id+"')");
	oPopup.hide();
}
function mouseout() {
	var x = oPopup.document.parentWindow.event.clientX;
	var y = oPopup.document.parentWindow.event.clientY;
	if (x < 0 || y < 0) {
		oPopup.hide();
	}
}
function GetPopupCssText() {
	var styles = document.styleSheets;
	var csstext = "";
	for (var i = 0; i < styles.length; i++) {
		if (styles[i].id == "popupmanager") {
			csstext += styles[i].cssText;
		}
	}
	return csstext;
}

