var oPopup = window.createPopup();
function Menu(clss11) {
	this.clsName = clss11;
	//system variable
	var menuOpened = false;
	//user variable
	var oWhere = document.body;
	var menu = document.createElement("div");

	oWhere.appendChild(menu);
	menu.oncontextmenu = "javascript:return false;";
	menu.id = "menu";
	if (this.clsName) {
		menu.className = clss11;
	} else {
		menu.className = "menu";
	}

	
	menu.onmouseup = function () {
		event.cancelBubble = true;
	};
	var sIds = new Array();
	this.setIDS = function (idss) {
		sIds = idss;
	};
	var sNames = new Array();
	this.setNames = function (idss) {
		sNames = idss;
	};
	this.getNamesStr = function () {
		var sfNamesStr = "";
		for (igns = 0; igns < sNames.length; igns++) {
			if (igns == 0) {
				sfNamesStr += sNames[igns];
			} else {
				sfNamesStr += "," + sNames[igns];
			}
		}
		return sfNamesStr;
	};
	//?????????ShowType??????????????????????????????Menu
	var sShowTypes = new Array();
	this.addShowType = function (showType) {
		sShowTypes[sShowTypes.length] = showType;
	};
	this.registerToDoc = function (menu1) {
		document.oncontextmenu = function () {
			menu1.showMenu("body");
			return false;
		};
		document.onmouseup = function () {
			menu1.hiddenMenu();
		};
		document.onselectstart = function () {
			return true;
		};
	};
	var items = new Array();
	this.addItem = function (sItem) {
		var itemIndex = items.length;
		sItem.id = "item_id_" + itemIndex;
		sItem.imgId = "img_id_" + itemIndex;
		items[itemIndex] = sItem;
		var item = document.createElement("div");
		item.id = sItem.id;
		if (sItem.img) {
			item.innerHTML = "<img src=" + sItem.img + " id='" + sItem.imgId + "'  align='absmiddle' />";
		} else {
			item.innerHTML = "<span></span>";
		}
		if (sItem.text) {
			item.innerHTML += sItem.text;
		}
		if (sItem.href) {
			item.onmouseup = function () {
				clickGo(sItem.id, sItem.href, sItem.hrefType);
			};
		}
		item.onmouseover = function () {
			mouseoverState(this);
		};
		item.onmouseout = function () {
			mouseoutState(this);
		};
		item.className = "item";
		menu.appendChild(item);
		/*		if(sItem.img){
			var divImg = document.getElementById(sItem.imgId);
			divImg.onmouseup =function() {return false;}
			divImg.onmousedown =function() {return false;}
			divImg.onclick =function() {return false;}
		}
*/
	};
	function checkShowType(showType) {
		for (var ctI = 0; ctI < sShowTypes.length; ctI++) {
			if (sShowTypes[ctI] == showType) {
				return true;
			}
		}
		return false;
	}
	function mouseoverState(obj) {
	//	alert("mouseoverState");
		with (obj.style) {
			backgroundColor = "#c2cef8";
		}
	}
	function mouseoutState(obj) {
	//	alert("mouseoutState");
		with (obj.style) {
			backgroundColor = "";
		}
	}
	function clickGo(itemID, href, hrefType) {
		//if(srcElem.tagName.toLowerCase()=="img"){
		//	return false;
		//}
		var divItem = document.getElementById(itemID);
		if (!divItem) {
			return;
		}
		if (divItem.disabled) {
			return;
		}
		sHiddenMenu();
		//alert(idss);
		//alert(href+"('"+this.ids+"')");
		switch (hrefType) {
		  case 0:
			document.location.href = href;
			break;
		  case 1:
		javascript:
			eval(href + "()");
			break;
			//case 2:document.location.href=href+"&ids="+sIds;break;
		  case 3:
		javascript:
			eval(href + "(sIds,sNames)");
			break;
		}
		oPopup.hide();
	}
	//addLine
	this.addLine = function () {
		var itemIndex = items.length;
		var ml = new MenuLine();
		ml.id = "line_id_" + itemIndex;
		items[itemIndex] = ml;
		var line = document.createElement("div");
		line.id = ml.id;
		line.className = "line";
		menu.appendChild(line);
	};
	this.showMenu = function (showType) {
		if (!checkShowType(showType)) {
			return false;
		}
		if (menuOpened) {
			return false;
		}
	//disabled=true
		var haveLine = false;
		//????????????????????????,????????????????????????????????? ???????????????0??????????????????
		var menuCount = 0;
		var oPopBody = oPopup.document.body;
		var s = oPopup.document.createStyleSheet();
		s.cssText = GetPopupCssText();
		oPopBody.innerHTML = document.getElementById("menu").outerHTML;
		var opopmenu=oPopup.document.getElementById("menu");
		opopmenu.style.visibility="visible";
		for (iSm = 0; iSm < items.length; iSm++) {
			var sItem = items[iSm];
			var divItem = oPopup.document.getElementById(sItem.id);
			if (!divItem) {
				continue;
			}
			divItem.onmouseover = function() { mouseoverState(this)}
			divItem.onmouseout = function() { mouseoutState(this)}
			if(sItem.href){
				divItem.href=sItem.href;
				divItem.hrefType=sItem.hrefType;
			 	divItem.onmouseup = function() {clickGo(this.id,this.href,this.hrefType)}
			}
			if (divItem.className == "line") {
				if (haveLine || menuCount == 0 || iSm == (items.length - 1)) {
					divItem.style.display = "none";
					continue;
				}
				divItem.style.display = "";
				haveLine = true;
				continue;
			}
			var enabled = true;
			var display = true;
			if (sItem.showType == showType || sItem.showType == "") {
				if (sItem.checkDisFunction) {
					display = eval(sItem.checkDisFunction + "('display',sIds,sNames)");
					if (display) {
						haveLine = false;
						menuCount++;
						enabled = eval(sItem.checkDisFunction + "('enabled',sIds,sNames)");
					}
				} else {
					haveLine = false;
					menuCount++;
				}
			} else {
				display = false;
				enabled = false;
			}
			var divImg = null;
			if (display) {
				divItem.style.display = "";
				if (enabled) {
					divItem.disabled = false;
				} else {
					divItem.disabled = true;
				}
			} else {
				divItem.style.display = "none";
			}
		}
		if (menuCount < 1) {
			return false;
		}
		menuOpened = true;
		var menu = document.all.menu;
		with (menu.style) {
			//visibility = "visible";
			//if (event.clientX + menu.offsetWidth > document.body.offsetWidth) {
			//	pixelLeft = event.clientX - menu.offsetWidth;
			//} else {
				pixelLeft = 0;//event.clientX;
			//}
			//if (event.clientY + menu.offsetHeight > (document.body.offsetHeight + 2)) {
			//	if (event.clientY + (menu.offsetHeight / 2) > document.body.offsetHeight) {
			//		pixelTop = document.body.offsetHeight - menu.offsetHeight - 1;
			//	} else {
			//		pixelTop = event.clientY - (menu.offsetHeight / 2);
			//	}
			//} else {
				pixelTop =0;// event.clientY;
			//}
			//pixelTop = event.clientY + document.body.offsetTop;
			oPopup.show(0, 0, 100, 0);
			var realHeight = oPopBody.scrollHeight;
			oPopup.hide();
			oPopup.show(event.clientX, event.clientY, menu.offsetWidth, realHeight+2, document.body);
			//alert("document.body.scrollTop:"+window.parent.document.body.scrollHeight+"nevent.clientY:"+event.clientY+"nmenu.offsetHeight:"+menu.offsetHeight+"ndocument.body.offsetHeight:"+document.body.offsetHeight+"nmenu.offsetTop:"+menu.offsetTop);
			//alert("event.clientX:"+event.clientX+"nmenu.offsetWidth:"+menu.offsetWidth+"ndocument.body.offsetWidth:"+document.body.offsetWidth+"npixelLeft:"+pixelLeft+";pixelTop"+pixelTop);
		}
		
	};
	this.hiddenMenu = function () {
		sHiddenMenu();
	};
	function sHiddenMenu() {
		if (!menuOpened) {
			return false;
		}
		menuOpened = false;
		var menu = document.all.menu;
		with (menu.style) {
			visibility = "hidden";
		}
	}
}
/*
		hrefType = 0??????href???????????????
		hrefType = 1??????href?????????javascript
		???????????? hrefType = 2??????href???????????????,?????????????????????ID
			????????????????????????????????????&ids=? ?????????
		hrefType = 3??????href?????????javascript,?????????????????????ID
			?????????javascript??????????????????, ???????????????ID?????????
	*/
function MenuItem(img1, text1, href1, hrefType1, showType1, checkDisFunc) {
	this.id = "";
	this.imgId = "";
	this.img = img1;
	this.text = text1;
	this.href = href1;
	this.hrefType = hrefType1;
	this.showType = showType1;
	this.checkDisFunction = checkDisFunc;
}
/**
???????????????

*/
function MenuLine() {
	this.id = "";
}
function onMenuMouseOut() {
	srcElem = event.srcElement;
	if (srcElem.tagName == "BODY") {
		sMenu.hiddenMenu();
	}
}
function getValue() {
	var allvalue = "";
	var chkButtons = document.all.chkButton;
	if (typeof (chkButtons) == "undefined") {
		return "";
	}
	if (typeof (chkButtons.length) == "undefined") {
		if (chkButtons.checked) {
			return chkButtons.value;
		} else {
			return "";
		}
	}
	for (var i = 0; i < chkButtons.length; i++) {
		if (chkButtons[i].checked) {
			allvalue += chkButtons[i].value + ",";
		}
	}
	if (allvalue.length > 0) {
		allvalue = allvalue.substring(0, allvalue.length - 1);
	}
	return allvalue;
}
function chekValue() {
	var checkButtonsNum = 0;
	var chkButtons = document.all.chkButton;
	if (typeof (chkButtons) == "undefined") {
		return checkButtonsNum;
	}
	if (typeof (chkButtons.length) == "undefined") {
		if (chkButtons.checked) {
			return 1;
		} else {
			return checkButtonsNum;
		}
	}
	for (var i = 0; i < chkButtons.length; i++) {
		if (chkButtons[i].checked) {
			checkButtonsNum++;
		}
	}
	return checkButtonsNum;
}
function checkMoreDis(type, ids, names) {
	return true;
}
function checkOneDis(type, ids, names) {
	if (names.length != 1) {
		return false;
	}
	return true;
}
function checkSelectedOneDis() {
	var checkValueNum = chekValue();
	if (checkValueNum == 0) {
		alert("\u8bf7\u9009\u62e9\u8bb0\u5f55。");
		return false;
	}
	if (checkValueNum != 1) {
		alert("\u53ea\u80fd\u9009\u62e9\u4e00\u6761\u6570\u636e。");
		return false;
	}
	return true;
}
function checkSelectedMoreDis() {
	if (chekValue() == 0) {
		alert("\u8bf7\u9009\u62e9\u8bb0\u5f55。");
		return false;
	}
	return true;
}

function GetPopupCssText() {
	var styles = document.styleSheets;
	var csstext = "";
	for (var i = 0; i < styles.length; i++) {
		//if (styles[i].id == "popupmanager") {
			csstext += styles[i].cssText;
		//}
	}
	return csstext;
}