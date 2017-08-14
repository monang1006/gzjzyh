/*
*######################################
* eWebEditor v7.3 - Advanced online web based WYSIWYG HTML editor.
* Copyright (c) 2003-2011 eWebSoft.com
*
* For further information go to http://www.ewebeditor.net/
* This copyright notice MUST stay intact for use.
*######################################
*/


var URLParams = new Object() ;
var aParams = document.location.search.substr(1).split('&') ;
for (i=0 ; i < aParams.length ; i++) {
	var aParam = aParams[i].split('=') ;
	URLParams[aParam[0]] = aParam[1] ;
}


var EWIN = parent.EWIN;
var EWEB = EWIN.EWEB;
var EWEBDialog = EWIN.EWEBDialog ;
var EWEBTools = EWIN.EWEBTools;
var EWEBBrowser = EWIN.EWEBBrowser;
var EWEBParam = EWIN.EWEBParam;
var lang = EWIN.lang;
var config = EWIN.config;
var EWEBSelection = EWIN.EWEBSelection;

var ParentDialog = parent.ParentDialog();
if (ParentDialog){
	ParentDialog = ParentDialog.contentWindow.InnerDialog;
}


EWEBTools.RegisterDollarFunction(window);


function GetParam(s_Name, s_Value){
	return (URLParams[s_Name]) ? URLParams[s_Name] : s_Value;
}

function LoadScript(url){
	document.write( '<scr' + 'ipt type="text/javascript" src="' + url + '"><\/scr' + 'ipt>' );
}

function BaseTrim(str){
	lIdx=0;
	rIdx=str.length;
	if (BaseTrim.arguments.length==2){
		act=BaseTrim.arguments[1].toLowerCase();
	}else{
		act="all";
	}

	for(var i=0;i<str.length;i++){
		thelStr=str.substring(lIdx,lIdx+1);
		therStr=str.substring(rIdx,rIdx-1);
		if ((act=="all" || act=="left") && thelStr==" "){
			lIdx++;
		}
		if ((act=="all" || act=="right") && therStr==" "){
			rIdx--;
		}
	}
	str=str.slice(lIdx,rIdx);
	return str;
}

function BaseAlert(theText,notice){
	alert(notice);
	theText.focus();
	theText.select();
	return false;
}

function IsColor(color){
	var temp=color;
	if (temp=="") return true;
	if (temp.length!=7) return false;
	return (temp.search(/\#[a-fA-F0-9]{6}/) != -1);
}

function IsEmptyOrInt(str){
	return (str.search(/[^0-9]+/)==-1);
}

function IsEmptyOr1Plus(str){
	return (IsEmptyOrInt(str) && parseInt(str)>0);
}


function OnDigitFieldKeyDown(e){
	if (!e){
		e = window.event;
	}
	if ((e.keyCode>=48 && e.keyCode<=57) || (e.keyCode>=35 && e.keyCode<=40) || e.keyCode==46 || e.keyCode==8 ){
		return true;
	}
	return EWEBTools.CancelEvent(e);
}

function SelectColor(s_FieldFlag){
	var s_Url = "selcolor.htm?returnfieldflag="+s_FieldFlag;
	EWEBDialog.OpenDialog(s_Url);
}

function SelectImage(){
	EWEBDialog.OpenDialog("backimage.htm?action=other");
}

function SelectBrowse(type, s_FieldFlag){
	EWEBDialog.OpenDialog('browse.htm?returnfieldflag='+s_FieldFlag+'&type='+type);
}

function SearchSelectValue(o_Select, s_Value){
	for (var i=0;i<o_Select.length;i++){
		if (o_Select.options[i].value == s_Value){
			o_Select.selectedIndex = i;
			return true;
		}
	}
	return false;
}

function ToInt(str){
	str=BaseTrim(str);
	if (str!=""){
		var sTemp=parseFloat(str);
		if (isNaN(sTemp)){
			str="";
		}else{
			str=sTemp;
		}
	}
	return str;
}

function IsURL(url){
	var sTemp;
	var b=true;
	sTemp=url.substring(0,7);
	sTemp=sTemp.toUpperCase();
	if ((sTemp!="HTTP://")||(url.length<10)){
		b=false;
	}
	return b;
}

function IsExt(url, opt){
	var sTemp;
	var b=false;
	var s=opt.toUpperCase().split("|");
	for (var i=0;i<s.length ;i++ ){
		sTemp=url.substr(url.length-s[i].length-1);
		sTemp=sTemp.toUpperCase();
		s[i]="."+s[i];
		if (s[i]==sTemp){
			b=true;
			break;
		}
	}
	return b;
}

function relativePath2rootPath(s_Url){
	if(s_Url.substring(0,1)=="/") {return s_Url;}
	if(s_Url.indexOf("://")>=0) {return s_Url;}

	var s_RootPath = EWEB.RootPath;
	while(s_Url.substr(0,3)=="../"){
		s_Url = s_Url.substr(3);
		s_RootPath = s_RootPath.substring(0,s_RootPath.lastIndexOf("/"));
	}
	return s_RootPath + "/" + s_Url;
}

function Rel2RootByBase(s_Url){
	if(config.BaseHref==""){return s_Url;}
	if(s_Url.substring(0,1)=="/") {return s_Url;}
	if(s_Url.indexOf("://")>=0) {return s_Url;}

	var s_Base = config.BaseHref;
	s_Base = s_Base.substring(0, s_Base.length-1);
	while(s_Url.substring(0,3)=="../"){
		s_Url = s_Url.substr(3);
		s_Base = s_Base.substring(0,s_Base.lastIndexOf("/"));
	}
	return s_Base + "/" + s_Url
}

function Root2PlugRel(s_Url){
	var s_PlugPath = EWEB.RootPath + "/plugin/";
	while(true){
		var n1=s_Url.indexOf("/");
		var n2=s_PlugPath.indexOf("/");
		if (n1>=0 && n1==n2){
			if (s_Url.substring(0,n1+1)==s_PlugPath.substring(0,n2+1)){
				s_Url = s_Url.substr(n1+1);
				s_PlugPath = s_PlugPath.substr(n2+1);
			}else{
				break;
			}
		}else{
			break;
		}
	}
	
	var s = s_PlugPath.replace(/[^\/]+/gi,'');
	var n = s.length;
	for (var i=0; i<n; i++){
		s_Url="../"+s_Url;
	}
	return s_Url;
}

function relativePath2setPath(url){
	switch(config.BaseUrl){
	case "0":
		url = relativePath2rootPath(url);
		return EraseBaseHref(url);
		break;
	case "1":
		return relativePath2rootPath(url);
		break;
	case "2":
	case "3":
		return EWEB.SitePath + relativePath2rootPath(url);
		break;
	}
}

function EraseBaseHref(url){
	var baseHref = config.BaseHref;

	var b=true;
	while(b){
		var n1=url.indexOf("/");
		var n2=baseHref.indexOf("/");
		if ((n1>=0) && (n2>=0)){
			var u1=url.substring(0,n1+1);
			var u2=baseHref.substring(0,n2+1);
			if (u1==u2){
				url=url.substr(n1+1);
				baseHref=baseHref.substr(n2+1);
			}else{
				b=false;
			}
		}else{
			b=false;
		}
	}

	if (baseHref!=""){
		var a=baseHref.split("/");
		for (var i=1; i<a.length; i++){
			url="../"+url;
		}
	}

	return url;
}


function adjustDialog(){
	var w = tabDialogSize.offsetWidth + 6;
	var h = tabDialogSize.offsetHeight + 25;
	if(EWEBBrowser.IsSP2){
		h += 20;
	}
	window.dialogWidth = w + "px";
	window.dialogHeight = h + "px";
	window.dialogLeft = (screen.availWidth - w) / 2;
	window.dialogTop = (screen.availHeight - h) / 2;
}

function imgButtonOver(el){
	if(!el["imageinitliazed"]){
		el["oncontextmenu"]= new Function("event.returnValue=false") ;
		el["onmouseout"]= new Function("imgButtonOut(this)") ;
		el["onmousedown"]= new Function("imgButtonDown(this)") ;
		el["unselectable"]="on" ;
		el["imageinitliazed"]=true ;
	}
	el.className = "imgButtonOver";
}

function imgButtonOut(el){
	el.className = "imgButtonOut";
}

function imgButtonDown(el){
	el.className = "imgButtonDown";
}

function getUploadForm(s_Type){
	var s_MaxSize;
	switch(s_Type){
	case "image":
		s_MaxSize = config.AllowImageSize;
		break;
	case "flash":
		s_MaxSize = config.AllowFlashSize;
		break;
	case "media":
		s_MaxSize = config.AllowMediaSize;
		break;
	case "file":
		s_MaxSize = config.AllowFileSize;
		break;
	default:
		return "";
	}

	var n_MaxSize = parseFloat(s_MaxSize)*1024;
	var html = "<iframe name='myuploadformtarget' style='display:none;position:absolute;width:0px;height:0px' src='blank.htm'></iframe>"
		+"<form action='../" + config.ServerExt + "/upload." + config.ServerExt + "?action=save&type="+s_Type+"&style="+EWEBParam.StyleName+"&cusdir="+EWEBParam.CusDir+"&skey="+EWEBParam.SKey+"' method=post name=myuploadform enctype='multipart/form-data' style='margin:0px;padding:0px;width:100%;border:0px;' target='myuploadformtarget'>"
		+"<input type=hidden name='MAX_FILE_SIZE' value='"+n_MaxSize+"'>"
		+"<input type=file name='uploadfile' id='uploadfile' size=28  onchange=\"this.form.originalfile.value=this.value;try{doPreview();} catch(e){}\">"
		+"<input type=hidden name='originalfile' value=''>"
		+"</form>";

	return html;
}

function getUploadErrDesc(s_Flag, s_Ext, s_Size){
	var s_ErrDesc = "";
	switch(s_Flag){
	case "ext":
		s_ErrDesc = lang["ErrUploadInvalidExt"] + ":" + s_Ext;
		break;
	case "size":
		s_ErrDesc = lang["ErrUploadSizeLimit"] + ":" + s_Size + "KB";
		break;
	case "file":
		s_ErrDesc = lang["ErrUploadInvalidFile"];
		break;
	case "style":
		s_ErrDesc = lang["ErrInvalidStyle"];
		break;
	case "space":
		s_ErrDesc = lang["ErrUploadSpaceLimit"] + ":" + config.SpaceSize + "MB";
		break;
	default:
		s_ErrDesc = s_Flag;
		break;
	}

	return s_ErrDesc;
}

function readCookie(s_Name){   
	var s_CookieValue = "";
	var s_Search = s_Name + "=";
	if(document.cookie.length>0){     
		n_Offset = document.cookie.indexOf(s_Search);
		if (n_Offset!=-1){     
			n_Offset += s_Search.length;
			var n_End = document.cookie.indexOf(";", n_Offset);
			if (n_End==-1){
				n_End = document.cookie.length;
			}
			s_CookieValue = unescape(document.cookie.substring(n_Offset,n_End));
		}
	}
	return s_CookieValue;
}

function writeCookie(s_Name, s_Value){   
	var s_Expire = "";
	s_Expire = new Date((new Date()).getTime() + 24*365*3600000);
	s_Expire = ";expires="+s_Expire.toGMTString();
	document.cookie = s_Name + "=" + escape(s_Value) + s_Expire;
}

var eWebEditorActiveX;
function CheckActiveXInstall(b_AutoInstall){
	if (eWebEditorActiveX){
		eWebEditorActiveX = null;
	}

	var b = false;
	try{
		eWebEditorActiveX = new ActiveXObject("eWebSoft.eWebEditor3");
		var s_Version = eWebEditorActiveX.Version;
		if (parseFloat(s_Version.replace(/[^0123456789]+/gi, ""))>=3200){
			eWebEditorActiveX.Lang ="zh-cn";
			eWebEditorActiveX.SendUrl = EWEB.SendUrl;
			eWebEditorActiveX.LocalSize = config.AllowLocalSize;
			eWebEditorActiveX.LocalExt = config.AllowLocalExt;
			b = true;
		}
	}catch(e){}

	if (!b && b_AutoInstall){
		EWEBDialog.OpenDialog("installactivex.htm", true);
	}

	return b;
}

function CheckActiveXError(){
	var s_Error = eWebEditorActiveX.Error;
	if (s_Error!=""){
		var s_ErrorCode, s_ErrorDesc;
		if (s_Error.indexOf(":")>=0){
			var a=s_Error.split(":");
			s_ErrorCode = a[0];
			s_ErrorDesc = a[1];
		}else{
			s_ErrorCode = s_Error;
			s_ErrorDesc = "";
		}

		switch(s_ErrorCode){
		case "L":
			alert(lang["ErrLicense"]);
			break;
		case "InvalidFile":
			alert(lang["ErrInvalidFile"]+":"+s_ErrorDesc);
			break;
		default:
			alert(s_Error);
		}
		return true;
	}
	return false;
}


function SetAttribute( el, s_AttrName, s_AttrValue ){
	if ( s_AttrValue == null || s_AttrValue.length == 0 ){
		el.removeAttribute( s_AttrName, 0 ) ;			// 0 : Case Insensitive
	}else{
		el.setAttribute( s_AttrName, s_AttrValue, 0 ) ;	// 0 : Case Insensitive
	}
}

function SetProtectAttribute(el, s_AttName, s_AttValue){
	SetAttribute(el, "_ewebeditor_ta_"+s_AttName, encodeURIComponent(s_AttValue));
}

function GetAttribute( el, s_AttrName ){
	var oAtt = el.attributes[s_AttrName] ;

	if ( oAtt == null || !oAtt.specified ){
		return "" ;
	}

	var oValue = el.getAttribute( s_AttrName, 2 ) ;

	if ( oValue == null ){
		oValue = oAtt.nodeValue ;
	}

	return ( oValue == null ? "" : oValue ) ;
}

function GetStyleAttribute( el, s_AttrName ){
	s_AttrName = s_AttrName.replace(/\-(\w)/g, function (s_Match, p1){
			return p1.toUpperCase();
		});
	
	var s_Value = el.style[s_AttrName];
	if (s_Value && (!EWEBBrowser.IsIE) && s_AttrName.indexOf("Color")>=0){
		s_Value = RGBtoHEX(s_Value);
	}
	if (!s_Value){
		switch(s_AttrName){
		case "backgroundColor":
			s_AttrName = "bgColor";
			break;
		case "textAlign":
			s_AttrName = "align";
			break;
		case "verticalAlign":
			s_AttrName = "valign";
			break;
		default:
		}
		s_Value = GetAttribute(el, s_AttrName);
	}

	return s_Value;
}

function RemovePX(s_Value){
	var n = parseInt(s_Value);
	if (isNaN(n)){
		return '';
	}
	if (s_Value.substr(s_Value.length-1)!="%"){
		s_Value = n+"";
	}
	return s_Value;
}

function GetProtectAttribute(el, s_AttName){
	var s_ProtectAttName = "_ewebeditor_ta_"+s_AttName;
	var o_Att = el.attributes[s_ProtectAttName] ;

	if ( o_Att == null || !o_Att.specified ){
		return GetAttribute(el, s_AttName) ;
	}else{
		return decodeURIComponent(el.getAttribute( s_ProtectAttName, 2 )) ;
	}
}

function GetFakeTag(el){
	return GetAttribute(el, "_ewebeditor_fake_tag");
}

function GetFakeValue(el){
	return decodeURIComponent(GetAttribute(el, "_ewebeditor_fake_value"));
}

function SetFakeValue(el, s_Html){
	SetAttribute(el, '_ewebeditor_fake_value', encodeURIComponent(s_Html));
}

function toHex(N) {
	if (N==null) return "00";
	N=parseInt(N);
	if (N==0 || isNaN(N)) return "00";
	N=Math.max(0,N);
	N=Math.min(N,255);
	N=Math.round(N);
	return "0123456789ABCDEF".charAt((N-N%16)/16) + "0123456789ABCDEF".charAt(N%16);
}

function RGBtoHEX(str){
	if (str.substring(0, 3) == 'rgb') {
		var arr = str.split(",");
		var r = arr[0].replace('rgb(','').trim()
		var g = arr[1].trim()
		var b = arr[2].replace(')','').trim();
		var hex = [
			toHex(r),
			toHex(g),
			toHex(b)
		];
		return "#" + hex.join('');
	}else{
		return str;
	}
}

var HtmlParamParser = {

	Init : function(s_Html){
		this.Params = new Object();
		this.Html = s_Html;
		var re = new RegExp("<object(?=[\\s>])","gi");
		if (re.test(s_Html)){
			this.Tag = "object";
			re = /<param\s+name\s*?=\s*?([\'\"]?)(\w+)\1[\s]+value\s*?=\s*?(\w+|\'[^\'>]+\'|\"[^\">]+\")[^>]*?>/gi;
			while((arr = re.exec(s_Html))!=null){
				var s_V = RegExp.$3;
				if (s_V.substring(0,1)=='\'' || s_V.substring(0,1)=='"'){
					s_V = s_V.substring(1,s_V.length-1);
				}
				this.Params[RegExp.$2.toLowerCase()] = s_V;
			}
		}else{
			this.Tag = "common";
			re = /\s(\w+)\s*?=\s*?(\w+|\'[^\'>]+\'|\"[^\">]+\")/gi;
			while((arr = re.exec(s_Html))!=null){
				var s_V = RegExp.$2;
				if (s_V.substring(0,1)=='\'' || s_V.substring(0,1)=='"'){
					s_V = s_V.substring(1,s_V.length-1);
				}
				this.Params[RegExp.$1.toLowerCase()] = s_V;
			}
		}
	},

	GetValue : function(s_Key){
		return (this.Params[s_Key]) ? this.Params[s_Key] : "";
	},


	GetHtml : function(){
		return this.Html;
	},

	SetValue : function(s_Key, s_Value){
		var s_Html = this.Html;
		
		function _Replace1(m, m1){
			if (s_Value==''){
				return '';
			}else{
				return '<param name="'+s_Key+'" value="'+s_Value+'">';
			}
		}
		
		function _Replace2(m){
			return m+'<param name="'+s_Key+'" value="'+s_Value+'">';
		}

		if (this.Tag=='object'){
			var re = new RegExp('<param(?=\\s)[^>]*?\\sname\\s*?=\\s*?(\w+|\'[^\'>]+\'|\"[^\">]+\")[^>]*?>', 'gi');
			if (re.test(s_Html)){
				s_Html = s_Html.replace(re, _Replace1);
			}else{
				if (s_Value!=''){
					s_Html = s_Html.replace(/<object[^>]*?>/, _Replace2);
				}
			}

			s_Html = this._GetSetValueByTag(s_Html, 'embed', s_Key, s_Value);
		}else{
			s_Html = this._GetSetValueByTag(s_Html, '\\w+', s_Key, s_Value);
		}


		this.Html = s_Html;
	},

	_GetSetValueByTag : function(s_Html, s_Tag, s_Key, s_Value){
		
		function _Replace1(m, m1, m2, m3){
			if (s_Value==''){
				return m1+m3;
			}else{
				return m1+' '+s_Key+'="'+s_Value+'"'+m3;
			}
		}

		function _Replace2(m){
			return m.substring(0,m.length-1)+' '+s_Key+'="'+s_Value+'">';
		}

		var re = new RegExp('(<'+s_Tag+'(?=[\\s>])[^>]*?)\\s'+s_Key+'\\s*?=\\s*?(\w+|\'[^\'>]+\'|\"[^\">]+\")([^>]*>)', 'gi');
		if (re.test(s_Html)){
			s_Html = s_Html.replace(re, _Replace1);
		}else{
			if (s_Value!=''){
				re = new RegExp('<'+s_Tag+'(?=[\\s>])[^>]*>', 'gi');
				s_Html = s_Html.replace(re, _Replace2);
			}
		}
		return s_Html;
	}

};


var DLGTab = {

	Click : function(n_Index, n_Count){
		if ($("tab_nav_"+n_Index).className=="tab_on"){
			return;
		}
		var s_NavID, s_TabID, s_CurrTabID;
		for (var i=1; i<=n_Count; i++){
			s_NavID = "tab_nav_"+i;
			s_TabID = $(s_NavID).getAttribute("_content_id", 2);
			if ($(s_NavID).className=="tab_on"){
				if (!DLGTab.FrameSize){
					DLGTab.FrameSize = new Array();
				}
				if (!DLGTab.FrameSize[i]){
					DLGTab.FrameSize[i] = {Width:$("tabDialogSize").offsetWidth, Height:$("tabDialogSize").offsetHeight};
				}

				if (!DLGTab.TabSize){
					DLGTab.TabSize = new Array();
				}
				if (!DLGTab.TabSize[i]){
					DLGTab.TabSize[i] = {Width:$(s_TabID).offsetWidth, Height:$(s_TabID).offsetHeight};
				}

				$(s_NavID).className="tab_off";
				$(s_TabID).style.display="none";
			}

			if (n_Index==i){
				s_CurrTabID = s_TabID;
			}
		}
		$("tab_nav_"+n_Index).className="tab_on";
		$(s_CurrTabID).style.display="";
		try{
			TabOnClick(n_Index, n_Count, s_CurrTabID);
		}catch(e){}
	},

	Create : function(a_Tab){
		var s_Html = '<table class=tab_layout1 border=0 cellpadding=0 cellspacing=0 width="100%"><tr><td>'
			+'<table class=tab_layout2 border=0 cellpadding=0 cellspacing=0><tr>'
			+'<td class=tab_begin></td>';
		for (var i=1; i<=a_Tab.length; i++){
			var s_Class = 'tab_on';
			if (i>1){
				s_Html += '<td class=tab_sep></td>';
				s_Class = 'tab_off';
			}
			s_Html += '<td><table id="tab_nav_'+i+'" class="'+s_Class+'" _content_id="'+a_Tab[i-1][1]+'" border=0 cellpadding=0 cellspacing=0><tr>'
				+'<td class=tab_left></td>'
				+'<td class=tab_center onclick="DLGTab.Click('+i+','+a_Tab.length+')">'+a_Tab[i-1][0]+'</td>'
				+'<td class=tab_right></td>'
				+'</tr></table></td>';
		}

		s_Html += '</tr></table></td></tr></table>';
		document.write(s_Html);
	}

};


var DLGMFU = {

	Load : function(s_Type, o_Container, s_Width, s_Height, s_MultiFile){
		if (this._Loaded){return;}

		this._Type = s_Type;
		this._Container = o_Container;
		this._MultiFile = s_MultiFile || '1';

		this._Container.style.width = s_Width;
		this._Container.style.height = s_Height;

		if (EWEBBrowser.IsIE){
			if (!CheckActiveXInstall(true)){
				this.ShowMsg(lang["DlgComNotice"] +"<br>" + lang["DlgComMFUMsgNotInstall"]);
				this.CheckInstalled();
				return;
			}

			this.ShowControl();
		}else{
			this.ShowMsg(lang["DlgComNotice"] +"<br>" + lang["MsgOnlyForIE"]);
			return;
		}
	},

	CheckInstalled : function(){
		if (!CheckActiveXInstall(false)){
			window.setTimeout("DLGMFU.CheckInstalled()", 1000);
		}else{
			var s_Html = "<span class=red><b>" + lang["DlgComMFUMsgInstallOk"] + "</b></span><br><br><input type=button class='dlgBtn' value='"+lang["DlgComMFUMsgBtnOk"]+"' onclick='DLGMFU.ShowControl()'>";
			this.ShowMsg(s_Html);
		}
	},

	ShowMsg : function(s_Html){
		this._Container.innerHTML = '<table border=0 cellpadding=0 cellspacing=5 width="100%" height="100%"><tr><td align=center valign=middle>'
			+'<table border=0 cellpadding=0 cellspacing=5>'
			+'<tr valign=top>'
			+'<td><img border=0 src="images/info.gif" align=absmiddle></td>'
			+'<td>'+s_Html+'</td>'
			+'</tr>'
			+'</table>'
			+'</td></tr></table>';
	},

	ShowControl : function(){
		var s_AllowSize, s_AllowExt;
		switch(this._Type){
		case 'image':
			s_AllowSize = config.AllowImageSize;
			s_AllowExt = config.AllowImageExt;
			break;
		case 'media':
			s_AllowSize = config.AllowMediaSize;
			s_AllowExt = config.AllowMediaExt;
			break;
		case 'flash':
			s_AllowSize = config.AllowFlashSize;
			s_AllowExt = config.AllowFlashExt;
			break;
		case 'file':
			s_AllowSize = config.AllowFileSize;
			s_AllowExt = config.AllowFileExt;
			break;
		}

		var s_Html = '<table border=0 cellpadding=0 cellspacing=5 width="100%" height="100%" style="background-color:#d4d0c8">'
			+'<tr>'
				+'<td style="background-color:#808080;color:#ffffff;padding-left:5px;">'
				+lang['DlgComMFUMsgAllow'].replace('<ext>',s_AllowExt).replace('<size>',s_AllowSize)
				+'</td>'
			+'</tr>'
			+'<tr>'
				+'<td height="100%">'
				+'<object id="eWebEditorMFU" classid="CLSID:D8653DCD-2DD5-4648-8498-09868D7AE9A0" codebase="eWebEditor3.CAB#version=3,2,0,0" width="100%" height="100%"></object>'
				+'</td>'
			+'</tr>'
			+'</table>';

		this._Container.innerHTML = s_Html;
		
		eWebEditorMFU.Lang = "zh-cn";
		eWebEditorMFU.Charset = config.Charset;
		eWebEditorMFU.SendUrl = EWEB.SendUrl;
		eWebEditorMFU.BlockSize = config.MFUBlockSize;
		eWebEditorMFU.FileType = this._Type;	//image,media,flash,file
		eWebEditorMFU.AllowSize = s_AllowSize;
		eWebEditorMFU.AllowExt = s_AllowExt;
		if (this._MultiFile=='0'){
			eWebEditorMFU.MultiFile = this._MultiFile;
		}
		
		this._Loaded = true;
	}

};

