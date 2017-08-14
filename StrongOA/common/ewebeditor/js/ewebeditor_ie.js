/*
*######################################
* eWebEditor v7.3 - Advanced online web based WYSIWYG HTML editor.
* Copyright (c) 2003-2011 eWebSoft.com
*
* For further information go to http://www.ewebeditor.net/
* This copyright notice MUST stay intact for use.
*######################################
*/








String.prototype.Contains = function(s){
	return (this.indexOf(s)>-1);
};

String.prototype.StartsWith = function(s){
	return (this.substr(0,s.length)==s);
};

String.prototype.EndsWith = function(s, ignoreCase){
	var L1 = this.length;
	var L2 = s.length;

	if (L2>L1){
		return false;
	}

	if (ignoreCase){
		var oRegex = new RegExp(s+'$','i');
		return oRegex.test(this);
	}else{
		return (L2==0 || this.substr(L1-L2, L2)==s);
	}
};

String.prototype.Trim = function(){
	return this.replace( /(^[ \t\n\r]*)|([ \t\n\r]*$)/g, '' ) ;
};

Array.prototype.IndexOf = function(s){
	for (var i=0; i<this.length; i++){
		if (this[i]==s){
			return i;
		}
	}
	return -1;
};


(function(){
	var compliantExecNpcg = /()??/.exec("")[1] === undefined;
	var compliantLastIndexIncrement = function () {
		var x = /^/g;
		x.test("");
		return !x.lastIndex;
	}();

	var real = {
		exec:	RegExp.prototype.exec,
		match:   String.prototype.match,
		replace: String.prototype.replace,
		split:   String.prototype.split,
		test:	RegExp.prototype.test
	};

	var _indexOf = function (array, item, from) {
		for (var i = from || 0; i < array.length; i++){
			if (array[i] === item){
				return i;
			}
		}
		return -1;
	};

	var _getNativeFlags = function (regex) {
		return (regex.global	 ? "g" : "") +
			   (regex.ignoreCase ? "i" : "") +
			   (regex.multiline  ? "m" : "") +
			   (regex.extended   ? "x" : "") +
			   (regex.sticky	 ? "y" : "");
	};

	var _isRegExp = function (o) {
		return Object.prototype.toString.call(o) === "[object RegExp]";
	};

	RegExp.prototype.exec = function (str) {
		var match = real.exec.apply(this, arguments),
		name, r2;

		if (match) {
			if (!compliantExecNpcg && match.length > 1 && _indexOf(match, "") > -1) {
				r2 = RegExp("^" + this.source + "$(?!\\s)", _getNativeFlags(this));
				real.replace.call(match[0], r2, function () {
					for (var i = 1; i < arguments.length - 2; i++) {
						if (arguments[i] === undefined){
							match[i] = undefined;
						}
					}
				});
			}

			if (!compliantLastIndexIncrement && this.global && this.lastIndex > (match.index + match[0].length)){
				this.lastIndex--;
			}
		}

		return match;
	};

	if (!compliantLastIndexIncrement) {
		RegExp.prototype.test = function (str) {
			var match = real.exec.call(this, str);
			if (match && this.global && this.lastIndex > (match.index + match[0].length)){
				this.lastIndex--;
			}
			return !!match;
		};
	}

	String.prototype.match = function (regex) {
		if (!_isRegExp(regex)){
			regex = RegExp(regex);
		}
		if (regex.global) {
			var result = real.match.apply(this, arguments);
			regex.lastIndex = 0;
			return result;
		}
		return regex.exec(this);
	};

	String.prototype.split = function (s, limit) {
		if (!_isRegExp(s)){
			return real.split.apply(this, arguments);
		}

		var str = this + "",
		output = [],
		lastLastIndex = 0,
		match, lastLength;

		if (limit === undefined || +limit < 0) {
			limit = Infinity;
		} else {
			limit = Math.floor(+limit);
			if (!limit){
				return [];
			}
		}

		while (match = s.exec(str)) {
			if (s.lastIndex > lastLastIndex) {
				output.push(str.slice(lastLastIndex, match.index));

				if (match.length > 1 && match.index < str.length){
					Array.prototype.push.apply(output, match.slice(1));
				}

				lastLength = match[0].length;
				lastLastIndex = s.lastIndex;

				if (output.length >= limit){
					break;
				}
			}

			if (!match[0].length){
				s.lastIndex++;
			}
		}

		if (lastLastIndex === str.length) {
			if (!real.test.call(s, "") || lastLength){
				output.push("");
			}
		} else {
			output.push(str.slice(lastLastIndex));
		}

		return output.length > limit ? output.slice(0, limit) : output;
	};

})();






var EWEBParam = (function(){
	var URLParams = new Object();
	var aParams = document.location.search.substr(1).split("&");
	for (i=0; i<aParams.length; i++){
		var aParam = aParams[i].split("=");
		URLParams[aParam[0]] = aParam[1];
	}

	var _GetPValue = function(s_Key, s_Default){
		return (URLParams[s_Key]) ? URLParams[s_Key] : s_Default;
	};

	return {
		LinkField : _GetPValue("id", ""),
		LinkOriginalFileName : _GetPValue("originalfilename", ""),
		LinkSaveFileName : _GetPValue("savefilename", ""),
		LinkSavePathFileName : _GetPValue("savepathfilename", ""),
		ExtCSS : _GetPValue("extcss", ""),
		FullScreen : _GetPValue("fullscreen", ""),
		StyleName : _GetPValue("style", "coolblue"),
		CusDir : _GetPValue("cusdir", ""),
		Skin : _GetPValue("skin", ""),
		FixWidth : _GetPValue("fixwidth", ""),
		Lang : _GetPValue("lang", ""),
		AreaCssMode : _GetPValue("areacssmode", ""),
		ReadOnly : _GetPValue("readonly", ""),
		SKey : _GetPValue("skey", "")
	};

})();





var EWEBBrowser = (function(){
	var s = navigator.userAgent.toLowerCase();
	var b_IsIE = (/*@cc_on!@*/false);
	var n_IEVer = 0;
	if (b_IsIE){
		n_IEVer = parseInt( s.match( /msie (\d+)/ )[1], 10 );
	}

	return {
		IsIE		: b_IsIE,
		IsIE6		: (n_IEVer==6),
		IsIE6P		: (n_IEVer>=6),
		IsIE7P		: (n_IEVer>=7),
		IsIE8P		: (n_IEVer>=8),
		IsGecko		: s.Contains('gecko/'),
		IsSafari	: s.Contains('safari'),
		IsOpera		: !!window.opera,
		IsAIR		: s.Contains(' adobeair/'),
		IsMac		: s.Contains('macintosh'),
		IsChrome	: s.Contains('chrome/'),
		IsCompatible	: true
	};
})();








var lang = new Object();

lang.TranslatePage = function( targetDocument ){
	var aInputs = targetDocument.getElementsByTagName("INPUT");
	for ( i = 0 ; i < aInputs.length ; i++ ){
		if ( aInputs[i].getAttribute("lang") ){
			aInputs[i].value = lang[ aInputs[i].getAttribute("lang") ];
		}
	}

	var aSpans = targetDocument.getElementsByTagName("SPAN");
	for ( i = 0 ; i < aSpans.length ; i++ ){
		if ( aSpans[i].getAttribute("lang") ){
			aSpans[i].innerHTML = lang[ aSpans[i].getAttribute("lang") ];
		}
	}

	var aOptions = targetDocument.getElementsByTagName("OPTION");
	for ( i = 0 ; i < aOptions.length ; i++ ){
		if ( aOptions[i].getAttribute("lang") ){
			aOptions[i].innerHTML = lang[ aOptions[i].getAttribute("lang") ];
		}
	}
};






var EWEBMenu = ( function(){
	var _BaseZindex = 9999;
	var _Document, _Popup, _IFrame;
	var _MenuHeader, _MenuHr, _Menu1, _Menu2;
	var _Pos = {x:0, y:0, ew:0, rel:null};
	var _IsStrict;
	var _MainNode;
	var _IsOpened = false;
	var _TopWindow;


	var _InitMenu = function(){
		if (_Document){
			return;
		}

		_Menu1 = "<table border=0 cellpadding=0 cellspacing=0 class='Menu_Box' id=Menu_Box><tr><td class='Menu_Box'><table border=0 cellpadding=0 cellspacing=0 class='Menu_Table'>";
		_MenuHr = "<tr><td class='Menu_Sep'><table border=0 cellpadding=0 cellspacing=0 class='Menu_Sep'><tr><td></td></tr></table></td></tr>";
		_Menu2 = "</table></td></tr></table>";
		_MenuHeader = "<html><head>"
			+"<link href='" + EWEB.RootPath + "/skin/" + config.Skin + "/menuarea.css' type='text/css' rel='stylesheet'>"
			+"</head>"
			+"<body scroll='no'>";
		
		if (EWEBBrowser.IsIE){
			_Popup = window.createPopup();

			_Document = _Popup.document;
			_Document.open();
			_Document.write(_MenuHeader);
			_Document.close();
		}else{
			_TopWindow = EWEBTools.GetTopWindow() ;
			var o_TopDocument = _TopWindow.document ;

			_IFrame = o_TopDocument.createElement('iframe') ;
			EWEBTools.ResetStyles( _IFrame );
			_IFrame.src					= 'javascript:void(0)' ;
			_IFrame.allowTransparency	= true ;
			_IFrame.frameBorder			= '0' ;
			_IFrame.scrolling			= 'no' ;
			_IFrame.style.width = _IFrame.style.height = '0px' ;
			EWEBTools.SetElementStyles( _IFrame,
				{
					position	: 'absolute',
					zIndex		: _BaseZindex
				} ) ;

			_IFrame._DialogArguments = window;

			o_TopDocument.body.appendChild( _IFrame ) ;

			var o_IFrameWindow = _IFrame.contentWindow ;
			_Document = o_IFrameWindow.document ;

			_Document.open() ;
			_Document.write(_MenuHeader) ;
			_Document.close() ;

			_IsStrict = EWEBTools.IsStrictMode( _Document );
			EWEBTools.DisableSelection(_Document.body);

			EWEBTools.AddEventListener( o_IFrameWindow, 'focus', _EWEBMenu_Win_OnFocus) ;
			EWEBTools.AddEventListener( o_IFrameWindow, 'blur', _EWEBMenu_Win_OnBlur) ;

		}

		_MainNode = _Document.body.appendChild( _Document.createElement('DIV') ) ;
		_MainNode.style.cssFloat = 'left' ;

		EWEBTools.AddEventListener( _Document, 'contextmenu', EWEBTools.CancelEvent ) ;
		EWEBTools.AddEventListener( _Document, 'dragstart', EWEBTools.CancelEvent ) ;
		EWEBTools.AddEventListener( _Document, 'selectstart', EWEBTools.CancelEvent ) ;
		EWEBTools.AddEventListener( _Document, 'select', EWEBTools.CancelEvent ) ;

	};




	var _GetMenuRow = function(s_Disabled, s_Event, s_Image, s_Html){
		var s_MenuRow = "";

		var s_Click;
		if (EWEBBrowser.IsIE){
			s_Click = "var w=parent;w."+s_Event+";w.EWEBMenu.Hide();";
		}else{
			s_Click = "var w=frameElement._DialogArguments;w."+s_Event+";w.EWEBMenu.Hide();"
		}

		if (s_Disabled==""){
			s_MenuRow += "<tr><td class='Menu_Item'><table border=0 cellpadding=0 cellspacing=0 width='100%'><tr><td valign=middle class=MouseOut onMouseOver=\"this.className='MouseOver'\" onMouseOut=\"this.className='MouseOut'\" onclick=\""+s_Click+"\">";
		}else{
			s_MenuRow += "<tr><td class='Menu_Item'><table border=0 cellpadding=0 cellspacing=0 width='100%'><tr><td valign=middle class=MouseDisabled>";

		}

		s_Disabled = (s_Disabled) ? "_Disabled" : "";

		s_MenuRow += "<table border=0 cellpadding=0 cellspacing=0><tr><td class=Menu_Image_TD>";

		if (typeof(s_Image)=="number"){
			var s_Img = "skin/" + config.Skin + "/buttons.gif";
			var n_Top = 16-s_Image*16;
			s_MenuRow += "<div class='Menu_Image"+s_Disabled+"'><img src='"+s_Img+"' style='top:"+n_Top+"px'></div>";
		}else if (s_Image!=""){
			var s_Img = "skin/" + config.Skin + "/" + s_Image;
			s_MenuRow += "<img class='Menu_Image"+s_Disabled+"' src='"+s_Img+"'>";
		}
		s_MenuRow += "</td><td class='Menu_Label"+s_Disabled+"'>" + s_Html + "</td></tr></table>";
		s_MenuRow += "</td></tr></table></td><\/tr>";
		return s_MenuRow;
	};

	var _GetStandardMenuRow = function(s_Disabled, s_Code, s_Lang){
		var a_Button = Buttons[s_Code];
		if (!s_Lang){
			s_Lang = lang[s_Code];
		}else{
			s_Lang = lang[s_Lang];
		}
		return _GetMenuRow(s_Disabled, a_Button[1], a_Button[0], s_Lang);
	};

	var _GetFormatMenuRow = function(s_Code, s_Cmd){
		var s_Disabled = "";
		if (!s_Cmd){
			s_Cmd = s_Code;
		}
		try{
			if (!EWEB.EditorDocument.queryCommandEnabled(s_Cmd)){
				s_Disabled = "disabled";
			}
		}catch(e){}

		return _GetStandardMenuRow(s_Disabled, s_Code);
	};


	var _GetTableMenuRow = function(what){
		var s_Menu = "";
		var s_Disabled = "disabled";
		switch(what){
		case "TableInsert":
			if (!EWEBTable.IsTableSelected()){s_Disabled="";}
			s_Menu += _GetStandardMenuRow(s_Disabled, "TableInsert");
			break;
		case "TableProp":
			if (EWEBTable.IsTableSelected()||EWEBTable.IsCursorInCell()){s_Disabled="";}
			s_Menu += _GetStandardMenuRow(s_Disabled, "TableProp");
			break;
		case "TableCell":
			if (EWEBTable.IsCursorInCell()){s_Disabled="";}
			s_Menu += _GetStandardMenuRow(s_Disabled, "TableCellProp");
			s_Menu += _GetStandardMenuRow(s_Disabled, "TableCellSplit");
			s_Menu += _MenuHr;
			s_Menu += _GetStandardMenuRow(s_Disabled, "TableRowProp");
			s_Menu += _GetStandardMenuRow(s_Disabled, "TableRowInsertAbove");
			s_Menu += _GetStandardMenuRow(s_Disabled, "TableRowInsertBelow");
			s_Menu += _GetStandardMenuRow(s_Disabled, "TableRowMerge");
			s_Menu += _GetStandardMenuRow(s_Disabled, "TableRowSplit");
			s_Menu += _GetStandardMenuRow(s_Disabled, "TableRowDelete");
			s_Menu += _MenuHr;
			s_Menu += _GetStandardMenuRow(s_Disabled, "TableColInsertLeft");
			s_Menu += _GetStandardMenuRow(s_Disabled, "TableColInsertRight");
			s_Menu += _GetStandardMenuRow(s_Disabled, "TableColMerge");
			s_Menu += _GetStandardMenuRow(s_Disabled, "TableColSplit");
			s_Menu += _GetStandardMenuRow(s_Disabled, "TableColDelete");
			break;
		}
		return s_Menu;
	};


	var _QuerySelFontSize = function(){
		if (EWEBSelection.GetType()=="Control"){return "";}
		
		var v = "";
		if (EWEBBrowser.IsIE){
			var sel = EWEB.EditorDocument.selection.createRange();
			var oRngTemp = EWEB.EditorDocument.body.createTextRange();

			var el = sel.parentElement();
			v = el.currentStyle.fontSize;

			var els = el.childNodes;
			for (var i=0; i<els.length; i++){
				if (els[i].nodeType==1){
					oRngTemp.moveToElementText(els[i]);
					if ( ((sel.compareEndPoints("StartToEnd",oRngTemp)<0)&&(sel.compareEndPoints("StartToStart",oRngTemp)>0)) || ((sel.compareEndPoints("EndToStart",oRngTemp)>0)&&(sel.compareEndPoints("EndToEnd",oRngTemp)<0)) ){
						if (els[i].currentStyle.fontSize!=v){
							v = "";
							break;
						}
					}
				}
			}

		}else{
			var rngParent = EWEBSelection.GetParentElement();
			if (!rngParent){
				return "";
			}
			
			var sourceRange=EWEBSelection.GetSelection().getRangeAt(0);
			var rngTemp = EWEB.EditorDocument.createRange();
			r = EWEBTools.GetCurrentElementStyle(rngParent,"font-size");
			var els = rngParent.getElementsByTagName("*");
			for (var i=0; i<els.length; i++){
				var el = els[i];
				rngTemp.selectNodeContents(el);
				if ((rngTemp.compareBoundaryPoints(Range.START_TO_END, sourceRange)>=0) && (rngTemp.compareBoundaryPoints(Range.END_TO_START, sourceRange)<=0)){
					var v=EWEBTools.GetCurrentElementStyle(el,"font-size");
					if (r!=v){
						r="";
						break;
					}
				}
			}

		}
		
		return v;
	};


	var _IsControlSelected = function(s_Tag, s_AttrName, s_AttrValue){
		if (EWEBSelection.GetType() == "Control"){
			if (s_Tag){
				var el = EWEBSelection.GetSelectedElement();
				if (el.tagName.toUpperCase() == s_Tag){
					if ((s_AttrName)&&(s_AttrValue)){
						if (el.getAttribute(s_AttrName, 2).toLowerCase()==s_AttrValue.toLowerCase()){
							return true;
						}
					}else{
						return true;
					}
				}
			}else{
				return true;
			}
		}
		return false;
	};


	_LoadComplete = function(){

		if (_Document.readyState!="complete"){
			return false;
		}

		if (_Document.images){
			for (var i=0; i<_Document.images.length; i++){
				var img = _Document.images[i];

				if (!img.complete){
					return false;
				}
			}
		}

		return true;
	};






	return {

		Show : function(){
			if (EWEBBrowser.IsIE){			
				if(! _LoadComplete()){
					window.setTimeout("EWEBMenu.Show()", 50);
					return;
				}

				var w = _Document.body.scrollWidth;
				var h = _Document.body.scrollHeight;

				if (_Pos.x+w>document.body.clientWidth){
					_Pos.x = _Pos.x - w + _Pos.ew;
				}

				_Popup.show(_Pos.x, _Pos.y, w, h, _Pos.rel);
			}else{
				var w = _MainNode.offsetWidth;
				var h = _MainNode.offsetHeight;
				var x = _Pos.x;
				var y = _Pos.y;
				
				if (_Document.readyState!="complete"){

					window.setTimeout("EWEBMenu.Show()", 50);
					return;
				}

				EWEBTools.SetElementStyles( _IFrame,
					{
						width	: w + 'px',
						height	: h + 'px',
						left	: x + 'px',
						top		: y + 'px'
					} ) ;

				_IsOpened = true;
			}
		},
		
		
		ShowToolMenu : function(e, s_Flag){
			if (EWEB.CurrMode!="EDIT"){return EWEBTools.CancelEvent(e);}

			_InitMenu();
			if (_IsOpened){this.Hide();}
			

			var s_Menu = "";

			switch(s_Flag){
			case "font":
				s_Menu += _GetFormatMenuRow("Bold");
				s_Menu += _GetFormatMenuRow("Italic");
				s_Menu += _GetFormatMenuRow("UnderLine");
				s_Menu += _GetFormatMenuRow("StrikeThrough");
				s_Menu += _MenuHr;
				s_Menu += _GetFormatMenuRow("SuperScript");
				s_Menu += _GetFormatMenuRow("SubScript");
				s_Menu += _MenuHr;
				s_Menu += _GetStandardMenuRow("", "UpperCase");
				s_Menu += _GetStandardMenuRow("", "LowerCase");
				s_Menu += _MenuHr;
				s_Menu += _GetStandardMenuRow("", "ForeColor");
				s_Menu += _GetStandardMenuRow("", "BackColor");
				s_Menu += _MenuHr;
				s_Menu += _GetStandardMenuRow("", "Big");
				s_Menu += _GetStandardMenuRow("", "Small");
				break;
			case "paragraph":
				s_Menu += _GetFormatMenuRow("JustifyLeft");
				s_Menu += _GetFormatMenuRow("JustifyCenter");
				s_Menu += _GetFormatMenuRow("JustifyRight");
				s_Menu += _GetFormatMenuRow("JustifyFull");
				s_Menu += _MenuHr;
				s_Menu += _GetFormatMenuRow("OrderedList", "insertorderedlist");
				s_Menu += _GetFormatMenuRow("UnOrderedList", "insertunorderedlist");
				s_Menu += _GetFormatMenuRow("Indent");
				s_Menu += _GetFormatMenuRow("Outdent");
				s_Menu += _MenuHr;
				s_Menu += _GetFormatMenuRow("Paragraph", "insertparagraph");
				s_Menu += _GetStandardMenuRow("", "BR");
				s_Menu += _MenuHr;
				s_Menu += _GetStandardMenuRow((_IsParagraphRelativeSelection()) ? "" : "disabled", "ParagraphAttr", "CMenuParagraph");
				break;
			case "edit":
				var s_Disabled = "";
				if (!EWEBHistory.QueryUndoState()){s_Disabled = "disabled";}
				s_Menu += _GetStandardMenuRow(s_Disabled, "UnDo");
				if (!EWEBHistory.QueryRedoState()){s_Disabled = "disabled";}
				s_Menu += _GetStandardMenuRow(s_Disabled, "ReDo");
				s_Menu += _MenuHr;
				s_Menu += _GetFormatMenuRow("Cut");
				s_Menu += _GetFormatMenuRow("Copy");

				s_Menu += _GetStandardMenuRow("", "Paste");
				s_Menu += _GetStandardMenuRow("", "PasteText");
				s_Menu += _GetStandardMenuRow("", "PasteWord");
				s_Menu += _MenuHr;
				s_Menu += _GetFormatMenuRow("Delete");
				s_Menu += _GetFormatMenuRow("RemoveFormat");
				s_Menu += _MenuHr;
				s_Menu += _GetFormatMenuRow("SelectAll");
				s_Menu += _GetFormatMenuRow("UnSelect");
				s_Menu += _MenuHr;
				s_Menu += _GetStandardMenuRow("", "FindReplace");
				s_Menu += _GetStandardMenuRow("", "QuickFormat");
				break;
			case "object":
				s_Menu += _GetStandardMenuRow("", "BgColor");
				s_Menu += _GetStandardMenuRow("", "BackImage");
				s_Menu += _MenuHr;
				s_Menu += _GetStandardMenuRow("", "absolutePosition");
				s_Menu += _GetStandardMenuRow("", "zIndexForward");
				s_Menu += _GetStandardMenuRow("", "zIndexBackward");
				s_Menu += _MenuHr;
				s_Menu += _GetStandardMenuRow("", "ShowBorders");
				s_Menu += _MenuHr;
				s_Menu += _GetStandardMenuRow("", "Quote");
				s_Menu += _GetStandardMenuRow("", "Code");
				break;
			case "component":
				s_Menu += _GetStandardMenuRow("", "Image");
				s_Menu += _GetStandardMenuRow("", "Flash");
				s_Menu += _GetStandardMenuRow("", "Media");
				s_Menu += _GetStandardMenuRow("", "File");
				s_Menu += _MenuHr;
				s_Menu += _GetStandardMenuRow("", "RemoteUpload");
				s_Menu += _GetStandardMenuRow("", "LocalUpload");
				s_Menu += _MenuHr;
				s_Menu += _GetStandardMenuRow("", "Fieldset");
				s_Menu += _GetStandardMenuRow("", "Iframe");
				s_Menu += _GetFormatMenuRow("HorizontalRule", "InsertHorizontalRule");
				s_Menu += _GetStandardMenuRow("", "Marquee");
				s_Menu += _MenuHr;
				s_Menu += _GetStandardMenuRow("", "CreateLink");
				s_Menu += _GetStandardMenuRow("", "Anchor");
				s_Menu += _GetStandardMenuRow("", "Map");
				s_Menu += _GetFormatMenuRow("Unlink");
				break;
			case "tool":
				s_Menu += _GetStandardMenuRow("", "Template");
				s_Menu += _GetStandardMenuRow("", "Symbol");
				s_Menu += _GetStandardMenuRow("", "Excel");
				s_Menu += _GetStandardMenuRow("", "Emot");
				s_Menu += _MenuHr;
				s_Menu += _GetStandardMenuRow("", "EQ");
				s_Menu += _GetStandardMenuRow("", "Art");
				s_Menu += _MenuHr;
				s_Menu += _GetStandardMenuRow("", "NowDate");
				s_Menu += _GetStandardMenuRow("", "NowTime");
				s_Menu += _MenuHr;
				s_Menu += _GetStandardMenuRow("", "ImportWord");
				s_Menu += _GetStandardMenuRow("", "ImportExcel");
				s_Menu += _GetStandardMenuRow("", "Capture");
				s_Menu += _MenuHr;
				s_Menu += _GetStandardMenuRow("", "Pagination");
				s_Menu += _GetStandardMenuRow("", "PaginationInsert");
				break;
			case "file":
				s_Menu += _GetStandardMenuRow("", "Refresh");
				s_Menu += _MenuHr;
				s_Menu += _GetStandardMenuRow("", "ModeCode");
				s_Menu += _GetStandardMenuRow("", "ModeEdit");
				s_Menu += _GetStandardMenuRow("", "ModeText");
				s_Menu += _GetStandardMenuRow("", "ModeView");
				s_Menu += _MenuHr;
				s_Menu += _GetStandardMenuRow("", "SizePlus");
				s_Menu += _GetStandardMenuRow("", "SizeMinus");
				s_Menu += _MenuHr;
				s_Menu += _GetStandardMenuRow("", "Print");
				s_Menu += _MenuHr;
				s_Menu += _GetStandardMenuRow("", "About");
				s_Menu += _GetStandardMenuRow("", "Site");
				height = 208;
				break;
			case "table":
				s_Menu += _GetTableMenuRow("TableInsert");
				s_Menu += _GetTableMenuRow("TableProp");
				s_Menu += _MenuHr;
				s_Menu += _GetTableMenuRow("TableCell");
				break;
			case "form":
				s_Menu += _GetFormatMenuRow("FormText", "InsertInputText");
				s_Menu += _GetFormatMenuRow("FormTextArea", "InsertTextArea");
				s_Menu += _GetFormatMenuRow("FormRadio", "InsertInputRadio");
				s_Menu += _GetFormatMenuRow("FormCheckbox", "InsertInputCheckbox");
				s_Menu += _GetFormatMenuRow("FormDropdown", "InsertSelectDropdown");
				s_Menu += _GetFormatMenuRow("FormButton", "InsertButton");
				break;
			case "gallery":
				s_Menu += _GetStandardMenuRow("", "GalleryImage");
				s_Menu += _GetStandardMenuRow("", "GalleryFlash");
				s_Menu += _GetStandardMenuRow("", "GalleryMedia");
				s_Menu += _GetStandardMenuRow("", "GalleryFile");
				break;
			case "zoom":
				for (var i=0; i<EWEBCommandZoom.Options.length; i++){
					if (EWEBCommandZoom.Options[i]==EWEBCommandZoom.CurrScale){
						s_Menu += _GetMenuRow("", "EWEBCommandZoom.Execute("+EWEBCommandZoom.Options[i]+")", 120, EWEBCommandZoom.Options[i]+"%");
					}else{
						s_Menu += _GetMenuRow("", "EWEBCommandZoom.Execute("+EWEBCommandZoom.Options[i]+")", 119, EWEBCommandZoom.Options[i]+"%");
					}
				}
				break;
			case "fontsize":
				var v = _QuerySelFontSize();
				for (var i=0; i<lang["FontSizeItem"].length; i++){
					if (lang["FontSizeItem"][i][0]==v){
						s_Menu += _GetMenuRow("", "formatFont('size','"+lang["FontSizeItem"][i][0]+"')", 120, lang["FontSizeItem"][i][1]);
					}else{
						s_Menu += _GetMenuRow("", "formatFont('size','"+lang["FontSizeItem"][i][0]+"')", 119, lang["FontSizeItem"][i][1]);
					}
				}
				break;
			case "fontname":
				var v = EWEB.EditorDocument.queryCommandValue("FontName");
				for (var i=0; i<lang["FontNameItem"].length; i++){
					if (lang["FontNameItem"][i]==v){
						s_Menu += _GetMenuRow("", "formatFont('face','"+lang["FontNameItem"][i]+"')", 120, lang["FontNameItem"][i]);
					}else{
						s_Menu += _GetMenuRow("", "formatFont('face','"+lang["FontNameItem"][i]+"')", 119, lang["FontNameItem"][i]);
					}
				}
				break;
			case "formatblock":
				var v = EWEB.EditorDocument.queryCommandValue("FormatBlock");
				if (v){
					v = v.toLowerCase();
				}else{
					v = "";
				}
				for (var i=0; i<lang["FormatBlockItem"].length; i++){
					if (lang["FormatBlockItem"][i][0].toLowerCase()==v){
						s_Menu += _GetMenuRow("", "format('FormatBlock','"+lang["FormatBlockItem"][i][0]+"')", 120, lang["FormatBlockItem"][i][1]);
					}else{
						s_Menu += _GetMenuRow("", "format('FormatBlock','"+lang["FormatBlockItem"][i][0]+"')", 119, lang["FormatBlockItem"][i][1]);
					}
				}
				break;
			}



			_MainNode.innerHTML = _Menu1 + s_Menu + _Menu2;
			
			if (_Popup){

				_Popup.show(0, 0, 0, 0, document.body);

				e = window.event ;
				e.returnValue=false;

				var el = e.srcElement;
				var x = e.clientX - e.offsetX;
				var y = e.clientY - e.offsetY;
				if (el.style.top){
					y = y - parseInt(el.style.top);
				}


				if (el.tagName.toLowerCase()=="img"){
					el = el.parentNode;
					x = x - el.offsetLeft - el.clientLeft;
					y = y - el.offsetTop - el.clientTop;
				}
				if (el.className=="TB_Btn_Image"){
					el = el.parentNode;
					x = x - el.offsetLeft - el.clientLeft;
					y = y - el.offsetTop - el.clientTop;
				}

				y = y + el.offsetHeight;

				var ew = parseInt(el.offsetWidth);

				_Pos.x = x;
				_Pos.y = y;
				_Pos.ew = ew;
				_Pos.rel = document.body;
				EWEBMenu.Show();

			}else{
				var el = e.target;

				if (el.tagName.toLowerCase()=="img" || el.className=="TB_Btn_Image"){
					el = el.parentNode;
				}

				var x = 0;
				var y = 0;
				var ew = el.offsetWidth;
				var relElement  = el;

				if ( EWEBBrowser.IsSafari ){
					x = e.clientX ;
					y = e.clientY ;
				}else{
					x = e.pageX ;
					y = e.pageY ;
				}


				var oPos = EWEBTools.GetDocumentPosition(_TopWindow, el);
				x = oPos.x;
				y = oPos.y + el.offsetHeight;

				_Pos.x = x;
				_Pos.y = y;
				_Pos.ew = ew;

				_IFrame.contentWindow.focus();

				window.setTimeout("EWEBMenu.Show()", 1);



			}


		},

		ShowContextMenu : function(e){
			if (EWEB.CurrMode!="EDIT"){return EWEBTools.CancelEvent(e);}

			_InitMenu();
			if (_IsOpened){EWEBMenu.Hide();}

			var s_Menu="";

			s_Menu += _GetFormatMenuRow("Cut");
			s_Menu += _GetFormatMenuRow("Copy");

			s_Menu += _GetStandardMenuRow("", "Paste");
			s_Menu += _GetFormatMenuRow("Delete");
			s_Menu += _GetFormatMenuRow("SelectAll");
			s_Menu += _MenuHr;

			if (EWEBTable.IsCursorInCell()){
				s_Menu += _GetTableMenuRow("TableProp");
				s_Menu += _GetTableMenuRow("TableCell");
				s_Menu += _MenuHr;
			}

			if (_IsControlSelected("TABLE")){
				s_Menu += _GetTableMenuRow("TableProp");
				s_Menu += _MenuHr;
			}

			if (_IsControlSelected("IMG")){
				var s_FakeTag = EWEBFake.GetTag();
				if (!s_FakeTag){
					s_Menu += _GetStandardMenuRow("", "Image", "CMenuImg");
					s_Menu += _MenuHr;
					s_Menu += _GetStandardMenuRow("", "zIndexForward");
					s_Menu += _GetStandardMenuRow("", "zIndexBackward");
					s_Menu += _MenuHr;
				}
				if (s_FakeTag=="flash"){
					s_Menu += _GetStandardMenuRow("", "Flash", "CMenuFlash");
					s_Menu += _MenuHr;
				}

				if (s_FakeTag=="mediaplayer6" || s_FakeTag=="mediaplayer7" || s_FakeTag=="realplayer" || s_FakeTag=="quicktime" || s_FakeTag=="flv"){
					s_Menu += _GetStandardMenuRow("", "Media", "CMenuMedia");
					s_Menu += _MenuHr;
				}

			}


			if (_IsParagraphRelativeSelection()){
				s_Menu += _GetStandardMenuRow("", "ParagraphAttr", "CMenuParagraph");
				s_Menu += _MenuHr;
			}

			s_Menu += _GetStandardMenuRow("", "FindReplace");


			_MainNode.innerHTML = _Menu1 + s_Menu + _Menu2;

			if (_Popup){
				_Popup.show(0, 0, 0, 0, document.body);

				e = eWebEditor.event ;

				_Pos.x = e.clientX;
				_Pos.y = e.clientY;
				_Pos.ew = 0;
				_Pos.rel = EWEB.EditorDocument.body;
				EWEBMenu.Show();

			}else{

				EWEBTools.CancelEvent(e);

				_Pos.x = e.pageX ;
				_Pos.y = e.pageY ;

				var el = EWEB.EditorDocument;
				var oPos = EWEBTools.GetDocumentPosition(_TopWindow, ( EWEBTools.IsStrictMode( el ) ? el.documentElement : el.body ));
				_Pos.x += oPos.x;
				_Pos.y += oPos.y;


				_Pos.ew = 0;

				_IFrame.contentWindow.focus();

				window.setTimeout("EWEBMenu.Show()", 1);


			}


			return false;
		},

		Hide : function(){
			if ( _Popup ){
				_Popup.hide() ;
			}else{
				if (!_IsOpened){
					return;
				}
				_IFrame.style.width = _IFrame.style.height = '0px' ;
				_IsOpened = false;
			}
		}


	};

})();



function _EWEBMenu_Win_OnFocus(e){


};

function _EWEBMenu_Win_OnBlur(e){
	EWEBMenu.Hide();	
};







var EWEBDialog = ( function(){
	var _TopDialog ;
	var _BaseZIndex = 9999;
	var _Cover;
	var _Blocker;

	var _TopWindow;
	var _TopDocument;

	var _GetZIndex = function(){
		return ++_BaseZIndex ;
	};

	var _ResizeHandler = function(){
		if ( !_Cover ){
			return ;
		}

		var relElement = EWEBTools.IsStrictMode( _TopDocument ) ? _TopDocument.documentElement : _TopDocument.body ;

		EWEBTools.SetElementStyles( _Cover,
			{
				'width' : Math.max( relElement.scrollWidth,
					relElement.clientWidth,
					_TopDocument.scrollWidth || 0 ) - 1 + 'px',
				'height' : Math.max( relElement.scrollHeight,
					relElement.clientHeight,
					_TopDocument.scrollHeight || 0 ) - 1 + 'px'
			} ) ;
	};

	var _ToTop = function(el){
		EWEBTools.SetElementStyles( el,
			{
				'zIndex'	: _GetZIndex()
			} ) ;
	};

	var _DisplayMainCover = function(){
		if (!_TopWindow){
			_TopWindow = EWEBTools.GetTopWindow() ;
			_TopDocument = _TopWindow.document ;
		}

		_Cover = _TopDocument.createElement( "div" ) ;
		EWEBTools.ResetStyles( _Cover ) ;
		EWEBTools.SetElementStyles( _Cover,
			{
				"position" : "absolute",
				"zIndex" : _GetZIndex(),
				"top" : "0px",
				"left" : "0px",
				"backgroundColor" : "#ffffff"
			} ) ;
		EWEBTools.SetOpacity( _Cover, 0.50 ) ;

		if ( EWEBBrowser.IsIE && !EWEBBrowser.IsIE7P ){
			_Blocker = _TopDocument.createElement( "iframe" ) ;
			EWEBTools.ResetStyles( _Blocker ) ;
			_Blocker.hideFocus = true ;
			_Blocker.frameBorder = 0 ;
			_Blocker.src = EWEBTools.GetVoidUrl() ;
			EWEBTools.SetElementStyles( _Blocker,
				{
					"width" : "100%",
					"height" : "100%",
					"position" : "absolute",
					"left" : "0px",
					"top" : "0px",
					"filter" : "progid:DXImageTransform.Microsoft.Alpha(opacity=0)"
				} ) ;
			_Cover.appendChild( _Blocker ) ;
		}

		EWEBTools.AddEventListener( _TopWindow, "resize", _ResizeHandler ) ;
		_ResizeHandler() ;

		_TopDocument.body.appendChild( _Cover ) ;
	};


	var _HideMainCover = function(){
		EWEBTools.RemoveNode( _Cover ) ;
	};


	return {

		OpenDialog : function( dialogPage){
			if ( !_TopDialog ){
				_DisplayMainCover() ;
			}else{
				_ToTop(_Cover);
			}

			var dialogInfo = {
				TopWindow : _TopWindow,
				EditorWindow : window,
				Page : dialogPage
			}

			EWEBSelection.Save(true);


			var n_Width = 160;
			var n_Height = 100;

			var dialog = _TopDocument.createElement( 'iframe' ) ;
			EWEBTools.ResetStyles( dialog ) ;
			
			dialog.frameBorder = 0 ;
			dialog.allowTransparency = true ;
			var useAbsolutePosition = EWEBBrowser.IsIE && ( !EWEBBrowser.IsIE7P || !EWEBTools.IsStrictMode( _TopWindow.document ) ) ;

			EWEBTools.SetElementStyles( dialog,
					{
						'position'	: ( useAbsolutePosition ) ? 'absolute' : 'fixed',
						'width'		: n_Width + 'px',
						'height'	: n_Height + 'px',
						'zIndex'	: _GetZIndex()
					} ) ;
			
			this.CenterDialog(dialog, n_Width, n_Height);
			dialog.src = EWEB.RootPath+"/dialog/dialog.htm" ;

			dialog._DialogArguments = dialogInfo ;
			_TopDocument.body.appendChild( dialog ) ;
			dialog._ParentDialog = _TopDialog ;

			_TopDialog = dialog ;
		},

		OnDialogClose : function( dialogWindow ){
			var dialog = dialogWindow.frameElement ;
			EWEBTools.RemoveNode( dialog ) ;

			if ( dialog._ParentDialog ){
				_TopDialog = dialog._ParentDialog ;
				_ToTop(_TopDialog);
			}else{
				_HideMainCover() ;
				setTimeout( function(){ _TopDialog = null ; }, 0 ) ;
				EWEBSelection.Release();
			}
		},


		CenterDialog : function(dialog, n_Width, n_Height){
			if (!n_Width){
				n_Width = parseInt(dialog.style.width, 10);
			}
			if (!n_Height){
				n_Height = parseInt(dialog.style.height, 10);
			}

			var viewSize = EWEBTools.GetViewPaneSize( _TopWindow ) ;
			var scrollPosition = { 'X' : 0, 'Y' : 0 } ;
			var b_UseAbsolutePosition = EWEBBrowser.IsIE && ( !EWEBBrowser.IsIE7P || !EWEBTools.IsStrictMode( _TopWindow.document ) ) ;
			if ( b_UseAbsolutePosition ){
				scrollPosition = EWEBTools.GetScrollPosition( _TopWindow ) ;
			}
			var n_Top  = Math.max( scrollPosition.Y + ( viewSize.Height - n_Height - 20 ) / 2, 0 ) ;
			var n_Left = Math.max( scrollPosition.X + ( viewSize.Width - n_Width - 20 )  / 2, 0 ) ;
			
			EWEBTools.SetElementStyles( dialog,
				{
					'top'		: n_Top + 'px',
					'left'		: n_Left + 'px'
				} ) ;

		},

		GetCover : function()
		{
			return _Cover ;
		},

		GetTopDialog : function(){
			return _TopDialog;
		}


	};


})();








var EWEBToolbar = (function(){


	var _InitBtn = function(btn){
		btn.onmouseover = _BtnMouseOver;
		btn.onmouseout = _BtnMouseOut;
		btn.onmousedown = _BtnMouseDown;
		btn.onmouseup = _BtnMouseUp;
		btn.ondragstart = EWEBTools.CancelEvent;
		btn.onselectstart = EWEBTools.CancelEvent;
		btn.onselect = EWEBTools.CancelEvent;
		btn.YINITIALIZED = true;
		return true;
	};

	var _GetBtnEventElement = function(e){
		if (!e){
			e = window.event;
		}
		var el = e.srcElement || e.target;
		if (el.tagName=="IMG"){
			el=el.parentNode;
		}
		if (el.className=="TB_Btn_Image"){
			el=el.parentNode;
		}
		return el;
	};


	var _BtnMouseOver = function(e){
		var el = _GetBtnEventElement(e);
		el.className = "TB_Btn_Over";
	};

	var _BtnMouseOut = function(e){
		var el = _GetBtnEventElement(e);
		if (el.QCV){
			el.className = "TB_Btn_Down";
		}else{
			el.className = "TB_Btn";
		}
	};

	var _BtnMouseDown = function(e){
		var el = _GetBtnEventElement(e);
		el.className = "TB_Btn_Down";
	};

	var _BtnMouseUp = function(e){
		var el = _GetBtnEventElement(e);
		if (el.className = "TB_Btn_Down"){
			el.className = "TB_Btn_Over";
		}else{
			if (el.QCV){
				el.className = "TB_Btn_Down";
			}else{
				el.className = "TB_Btn";
			}
		}
	};


	var _CacheBtnGroup;
	var _InitCacheBtnGroup = function(){
		if (_CacheBtnGroup){return;}
		
		var a_CmdGroup = {
			normal : ["Bold","Italic","UnderLine","StrikeThrough","SuperScript","SubScript","JustifyLeft","JustifyCenter","JustifyRight","JustifyFull"],
			mode :["ModeCode","ModeEdit","ModeView","ModeText"],
			other : ["ShowBlocks","ShowBorders","Maximize"]
		};

		var els=$("eWebEditor_Toolbar").getElementsByTagName("DIV");
		_CacheBtnGroup = new Object;
		for (s_GroupName in a_CmdGroup){
			var a_Cmd = a_CmdGroup[s_GroupName];
			var o_CacheBtn = new Object;
			for (var i=0; i<a_Cmd.length; i++){
				var s_Cmd=a_Cmd[i];
				o_CacheBtn[s_Cmd] = new Array();
				for (var j=0; j<els.length; j++){
					var el=els[j];
					if (el.getAttribute("name")=="TB_Name_"+s_Cmd){
						o_CacheBtn[s_Cmd][o_CacheBtn[s_Cmd].length]=el;
					}
				}
			}
			_CacheBtnGroup[s_GroupName] = o_CacheBtn;
		}
	};


	var _CheckTBStatusBtns = function(b_IsControl){
		_InitCacheBtnGroup();

		var o_BtnGroup = _CacheBtnGroup["normal"];
		for (s_Cmd in o_BtnGroup){
			var v=EWEB.EditorDocument.queryCommandState(s_Cmd);
			var els=o_BtnGroup[s_Cmd];
			for (var j=0; j<els.length; j++){
				var el=els[j];
				el.QCV=v;
				if (b_IsControl){
					el.className="TB_Btn";
				}else{
					if (v){
						el.className="TB_Btn_Down";
					}else{
						el.className="TB_Btn";
					}
				}
			}
		}
	};


	var _RefreshModeBtnStatus = function(){
		_InitCacheBtnGroup();

		var o_BtnGroup = _CacheBtnGroup["mode"];
		for (s_Cmd in o_BtnGroup){		
			var els=o_BtnGroup[s_Cmd];
			for (var j=0; j<els.length; j++){
				var el=els[j];
				var s_Name = el.getAttribute("name");
				s_Name=s_Name.substr(s_Name.length-4).toUpperCase();
				if (s_Name==EWEB.CurrMode){
					el.QCV="on";
					el.className="TB_Btn_Down";
				}else{
					el.QCV="";
					el.className="TB_Btn";
				}
			}
		}
	};


	var _CheckTBStatusDrops = function(b_IsControl){
		var cmd,v;

		
		cmd="FontName";
		if (b_IsControl){
			v = "";
		}else{

			v = EWEB.EditorDocument.queryCommandValue(cmd);
		}
		_CheckTBStatusDrop(cmd,v);

		cmd="FontSize";
		if (b_IsControl){
			v="";
		}else{

			v = EWEB.EditorDocument.queryCommandValue(cmd);
			if (v){
				v=_Fontsize2Css(v);
			}else{
				v=_GetSeletionFontSizeCss();
			}
		}
		_CheckTBStatusDrop(cmd,v);
	};

	var _CheckTBStatusDrop = function(cmd,v){
		var els=document.getElementsByName("TB_Name_"+cmd);
		for (var i=0; i<els.length; i++){
			var el=els[i];
			if (v){
				v=v.toLowerCase();
				v=v.replace(/[\'\"]/gi,'');
				var b=false;
				for (var j=0; j<el.options.length; j++){
					var s_OptValue = el.options[j].value.toLowerCase();
					if (s_OptValue==v || ( cmd=="FontSize" && _CompareFontSize(s_OptValue, v) )){
						el.selectedIndex=j;
						b=true;
						break;
					}
				}
				if (!b){
					el.options[el.options.length]=new Option(v, v);
					el.selectedIndex=el.options.length-1;
				}			
			}else{
				el.selectedIndex=0;
			}
		}
	};

	var _CompareFontSize = function(s1, s2){
		s1 = s1.toLowerCase();
		s2 = s2.toLowerCase();
		if (s1==s2){return true;}
		
		if (s1.EndsWith("pt") && s2.EndsWith("px")){
			var n_DotLen = 0;
			s1 = parseFloat(s1) +"";
			var n = s1.indexOf(".");
			if (n>=0){
				var s = s1.substr(n+1);
				n_DotLen = s.length;
			}
			if (n_DotLen>0){
				if (s1==Math.round(parseFloat(s2)*3/4*Math.pow(10, n_DotLen)+0.01)/Math.pow(10, n_DotLen)){
					return true;
				}
			}else{
				if (s1==parseInt(parseFloat(s2)*3/4+0.01)){
					return true;
				}
			}
		}
		return false;
	};


	var _GetSeletionFontSizeCss = function(){		
		var r="";
		if (EWEBBrowser.IsIE){
			var rng=EWEBSelection.GetSelection().createRange();
			if (rng.text.length<=1){
				return rng.parentElement().currentStyle.fontSize;
			}
			
			var html=rng.htmlText;
			html=html.replace(/<[^>]+>/gi," ");
			html=html.replace(/(&nbsp\;|\s)+/gi, " ");
			html=html.replace(/(^\s*)|(\s*$)/gi, "");
			var a_Txt=html.split(" ");

			var s_BookMark = rng.getBookmark();
			rng.collapse();
			for (var i=0; i<a_Txt.length; i++){
				if (!a_Txt[i]){
					continue;
				}
				var b=rng.findText(a_Txt[i]);
				if (b){
					var v=rng.parentElement().currentStyle.fontSize;
					if ((r!="")&&(r!=v)){
						r="";
						break;
					}
					r=v;
					rng.collapse(false);
				}else{
					break;
				}
			}

			rng.moveToBookmark(s_BookMark);
		}else{

			var rngParent = EWEBSelection.GetParentElement();
			if (!rngParent){
				return "";
			}
			
			var sourceRange=EWEBSelection.GetSelection().getRangeAt(0);
			var rngTemp = EWEB.EditorDocument.createRange();
			r = EWEBTools.GetCurrentElementStyle(rngParent,"font-size");
			var els = rngParent.getElementsByTagName("*");
			for (var i=0; i<els.length; i++){
				var el = els[i];
				rngTemp.selectNodeContents(el);
				if ((rngTemp.compareBoundaryPoints(Range.START_TO_END, sourceRange)>=0) && (rngTemp.compareBoundaryPoints(Range.END_TO_START, sourceRange)<=0)){
					var v=EWEBTools.GetCurrentElementStyle(el,"font-size");
					if (r!=v){
						r="";
						break;
					}
				}
			}

			if (r!=""){



			}
		}

		return r;
	};





	return {

		InitTB : function(){
			var i, els, el, p;

			p = $("eWebEditor_Toolbar");
			els = p.getElementsByTagName("div");

			for (i=0; i<els.length; i++){
				el=els[i];
				if(el.className=="TB_Btn"){
					if (el.YINITIALIZED == null){
						if (! _InitBtn(el)){
							alert("Problem initializing:" + el.id);
							return false;
						}
					}
				}
			}
			return true;
		},


		CheckTBStatus : function(){
			if (EWEB.CurrMode!="EDIT"){return;}

			var b=(EWEBSelection.GetType()=="Control") ? true : false;
			_CheckTBStatusBtns(b);
			_CheckTBStatusDrops(b);
		},

		RefreshModeBtnStatus : function(){
			_RefreshModeBtnStatus();
		},

		GetBtns : function(s_GroupName, s_BtnName){
			_InitCacheBtnGroup();
			return _CacheBtnGroup[s_GroupName][s_BtnName];
		}



	};

})();








var EWEBTable = (function(){
	var _SelectedTD;
	var _SelectedTR;
	var _SelectedTBODY;
	var _SelectedTable;


	return {

		IsCursorInCell : function(){
			if (EWEBSelection.GetType() != "Control"){
				var el = EWEBSelection.GetParentElement();
				while(el){
					if (!el.tagName){
						el=null;
						break;
					}
					if (el.tagName.toUpperCase()=="TD" || el.tagName.toUpperCase()=="TH"){
						break;
					}else if (el.tagName.toUpperCase()=="BODY" || el.tagName.toUpperCase()=="HTML"){
						el=null;
						break;
					}
					el = el.parentNode;
				}
				if (el){
					_SelectedTD = el;
					_SelectedTR = _SelectedTD.parentNode;
					_SelectedTBODY =  _SelectedTR.parentNode;
					_SelectedTable = _SelectedTBODY.parentNode;
					return true;
				}
			}
			return false;
		},

		IsTableSelected : function(){
			if (EWEBSelection.GetType() == "Control"){
				var el = EWEBSelection.GetSelectedElement();
				if (el.tagName.toUpperCase() == "TABLE"){
					_SelectedTable = el;
					return true;
				}
			}
			return false;
		},


		TableInsert : function(){
			if (!EWEBTable.IsTableSelected()){
				showDialog('table.htm', true);
			}
		},

		TableProp : function(){
			if (EWEBTable.IsTableSelected()||EWEBTable.IsCursorInCell()){
				showDialog('table.htm?action=modify', true);
			}
		},

		CellProp : function(){
			if (EWEBTable.IsCursorInCell()){
				showDialog('tablecell.htm', true);
			}
		},

		CellSplit : function(){
			if (EWEBTable.IsCursorInCell()){
				showDialog('tablecellsplit.htm',true);
			}
		},

		RowProp : function(){
			if (EWEBTable.IsCursorInCell()){
				showDialog('tablecell.htm?action=row', true);
			}
		},

		RowInsertAbove : function(){
			if (!EWEBTable.IsCursorInCell()){return;}
			EWEBHistory.Save();

			var numCols = 0;

			allCells = _SelectedTR.cells;
			for (var i=0;i<allCells.length;i++){
				numCols = numCols + allCells[i].getAttribute('colSpan');
			}

			var newTR = _SelectedTable.insertRow(_SelectedTR.rowIndex);

			for (i = 0; i < numCols; i++){
				newTD = newTR.insertCell(-1);
				newTD.innerHTML = "&nbsp;";
			}

			EWEBHistory.Save();
		},

		RowInsertBelow : function(){
			if (!EWEBTable.IsCursorInCell()){return;}
			EWEBHistory.Save();

			var numCols = 0;

			allCells = _SelectedTR.cells;
			for (var i=0;i<allCells.length;i++){
				numCols = numCols + allCells[i].getAttribute('colSpan');
			}

			var newTR = _SelectedTable.insertRow(_SelectedTR.rowIndex+1);

			for (i = 0; i < numCols; i++){
				newTD = newTR.insertCell(-1);
				newTD.innerHTML = "&nbsp;";
			}

			EWEBHistory.Save();
		},

		RowMerge : function(){
			if (!EWEBTable.IsCursorInCell()){return;}
			EWEBHistory.Save();

			var rowSpanTD = _SelectedTD.getAttribute('rowSpan');
			allRows = _SelectedTable.rows;
			if (_SelectedTR.rowIndex +1 != allRows.length){
				var allCellsInNextRow = allRows[_SelectedTR.rowIndex+_SelectedTD.rowSpan].cells;
				var addRowSpan = allCellsInNextRow[_SelectedTD.cellIndex].getAttribute('rowSpan');
				var moveTo = _SelectedTD.rowSpan;

				if (!addRowSpan) addRowSpan = 1;

				_SelectedTD.rowSpan = _SelectedTD.rowSpan + addRowSpan;
				allRows[_SelectedTR.rowIndex + moveTo].deleteCell(_SelectedTD.cellIndex);
			}

			EWEBHistory.Save();
		},

		RowSplit : function(nRows){
			if (!EWEBTable.IsCursorInCell()){return;}
			if (nRows<2){return;}
			EWEBHistory.Save();

			var addRows = nRows - 1;
			var addRowsNoSpan = addRows;

			var nsLeftColSpan = 0;
			for (var i=0; i<_SelectedTD.cellIndex; i++){
				nsLeftColSpan += _SelectedTR.cells[i].colSpan;
			}

			var allRows = _SelectedTable.rows;


			while (_SelectedTD.rowSpan > 1 && addRowsNoSpan > 0){
				var nextRow = allRows[_SelectedTR.rowIndex+_SelectedTD.rowSpan-1];
				_SelectedTD.rowSpan -= 1;

				var ncLeftColSpan = 0;
				var position = -1;
				for (var n=0; n<nextRow.cells.length; n++){
					ncLeftColSpan += nextRow.cells[n].getAttribute('colSpan');
					if (ncLeftColSpan>nsLeftColSpan){
						position = n;
						break;
					}
				}

				var newTD=nextRow.insertCell(position);
				newTD.innerHTML = "&nbsp;";

				addRowsNoSpan -= 1;
			}


			for (var n=0; n<addRowsNoSpan; n++){
				var numCols = 0;

				allCells = _SelectedTR.cells;
				for (var i=0;i<allCells.length;i++){
					numCols = numCols + allCells[i].getAttribute('colSpan');
				}

				var newTR = _SelectedTable.insertRow(_SelectedTR.rowIndex+1);

				for (var j=0; j<_SelectedTR.rowIndex; j++){
					for (var k=0; k<allRows[j].cells.length; k++){
						if ((allRows[j].cells[k].rowSpan>1)&&(allRows[j].cells[k].rowSpan>=_SelectedTR.rowIndex-allRows[j].rowIndex+1)){
							allRows[j].cells[k].rowSpan += 1;
						}
					}
				}

				for (i = 0; i < allCells.length; i++){
					if (i!=_SelectedTD.cellIndex){
						_SelectedTR.cells[i].rowSpan += 1;
					}else{
						newTD = newTR.insertCell(-1);
						newTD.colSpan = _SelectedTD.colSpan;
						newTD.innerHTML = "&nbsp;";
					}
				}
			}

			EWEBHistory.Save();
		},

		RowDelete : function(){
			if (!EWEBTable.IsCursorInCell()){return;}
			EWEBHistory.Save();

			_SelectedTable.deleteRow(_SelectedTR.rowIndex);
			EWEBHistory.Save();
		},

		ColInsertLeft : function(){
			if (!EWEBTable.IsCursorInCell()){return;}
			EWEBHistory.Save();

			moveFromEnd = (_SelectedTR.cells.length-1) - (_SelectedTD.cellIndex);
			allRows = _SelectedTable.rows;
			for (i=0;i<allRows.length;i++){
				rowCount = allRows[i].cells.length - 1;
				position = rowCount - moveFromEnd;
				if (position < 0){
					position = 0;
				}
				newCell = allRows[i].insertCell(position);
				newCell.innerHTML = "&nbsp;";
			}

			EWEBHistory.Save();
		},

		ColInsertRight : function(){
			if (!EWEBTable.IsCursorInCell()){return;}
			EWEBHistory.Save();

			moveFromEnd = (_SelectedTR.cells.length-1) - (_SelectedTD.cellIndex);
			allRows = _SelectedTable.rows;
			for (i=0;i<allRows.length;i++){
				rowCount = allRows[i].cells.length - 1;
				position = rowCount - moveFromEnd;
				if (position < 0){
					position = 0;
				}
				newCell = allRows[i].insertCell(position+1);
				newCell.innerHTML = "&nbsp;";
			}

			EWEBHistory.Save();
		},

		ColMerge : function(){
			if (!EWEBTable.IsCursorInCell()){return;}
			EWEBHistory.Save();

			var colSpanTD = _SelectedTD.getAttribute('colSpan');
			allCells = _SelectedTR.cells;

			if (_SelectedTD.cellIndex + 1 != _SelectedTR.cells.length){
				var addColspan = allCells[_SelectedTD.cellIndex+1].getAttribute('colSpan');
				_SelectedTD.colSpan = colSpanTD + addColspan;
				_SelectedTR.deleteCell(_SelectedTD.cellIndex+1);
			}

			EWEBHistory.Save();
		},

		ColDelete : function(){
			if (!EWEBTable.IsCursorInCell()){return;}
			EWEBHistory.Save();

			moveFromEnd = (_SelectedTR.cells.length-1) - (_SelectedTD.cellIndex);
			allRows = _SelectedTable.rows;
			for (var i=0;i<allRows.length;i++){
				endOfRow = allRows[i].cells.length - 1;
				position = endOfRow - moveFromEnd;
				if (position < 0){
					position = 0;
				}

				allCellsInRow = allRows[i].cells;

				if (allCellsInRow[position].colSpan > 1){
					allCellsInRow[position].colSpan = allCellsInRow[position].colSpan - 1;
				} else {
					allRows[i].deleteCell(position);
				}
			}

			EWEBHistory.Save();
		},

		ColSplit : function(nCols){
			if (!EWEBTable.IsCursorInCell()){return;}
			if (nCols<2){return;}
			EWEBHistory.Save();

			var addCols = nCols - 1;
			var addColsNoSpan = addCols;
			var newCell;

			var nsLeftColSpan = 0;
			var nsLeftRowSpanMoreOne = 0;
			for (var i=0; i<_SelectedTD.cellIndex; i++){
				nsLeftColSpan += _SelectedTR.cells[i].colSpan;
				if (_SelectedTR.cells[i].rowSpan > 1){
					nsLeftRowSpanMoreOne += 1;
				}
			}

			var allRows = _SelectedTable.rows;

			while (_SelectedTD.colSpan > 1 && addColsNoSpan > 0){
				newCell = _SelectedTR.insertCell(_SelectedTD.cellIndex+1);
				newCell.innerHTML = "&nbsp;";
				_SelectedTD.colSpan -= 1;
				addColsNoSpan -= 1;
			}

			for (i=0;i<allRows.length;i++){
				var ncLeftColSpan = 0;
				var position = -1;
				for (var n=0; n<allRows[i].cells.length; n++){
					ncLeftColSpan += allRows[i].cells[n].getAttribute('colSpan');
					if (ncLeftColSpan+nsLeftRowSpanMoreOne>nsLeftColSpan){
						position = n;
						break;
					}
				}

				if (_SelectedTR.rowIndex!=i){
					if (position!=-1){
						allRows[i].cells[position+nsLeftRowSpanMoreOne].colSpan += addColsNoSpan;
					}
				}else{
					for (var n=0; n<addColsNoSpan; n++){
						newCell = allRows[i].insertCell(_SelectedTD.cellIndex+1);
						newCell.innerHTML = "&nbsp;";
						newCell.rowSpan = _SelectedTD.rowSpan;
					}
				}
			}

			EWEBHistory.Save();
		}


	};

})();



var EWEBTableResize = (function(){
	var _nTriggerAreaWidth = 2;
	var _nSepWidth = 5;
	
	var _bOnWidth = false;
	var _bOnHeight = false;
	var _bOnResizing = false;

	var _oSepDivV = null;
	var _nSepDivVLeft = 0;
	var _nSepDivVTop = 0;

	var _oSepDivH = null;
	var _nSepDivHLeft = 0;
	var _nSepDivHTop = 0;

	var _nStartX;
	var _nStartY;

	var _nScreenClientX;
	var _nScreenClientY;
	var _nCellMinX;
	var _nCellMinY;

	var _oOnCell = null;
	var _oOnTable = null;
	var _oOnRow = null;
	var _nOnCellWidth;
	var _nOnTableWidth;
	var _nOnTableHeight;
	var _nOnRowHeight;

	var _aDoWCellObj = new Array();
	var _aDoWCellWidth = new Array();

	var _aDoHCellObj = new Array();
	var _aDoHCellHeight = new Array();

	var _bInit = false;
	var _Init = function(){
		_oSepDivV = $("div_TableResizeSepV");
		_oSepDivV.className = "TableResizeSepV";
		_oSepDivH = $("div_TableResizeSepH");
		_oSepDivH.className = "TableResizeSepH";

		EWEBTools.AddEventListener( document, 'mousemove', EWEBTableResize.PMM ) ;
	};

	var _ResetVar = function(){
		_bOnWidth = false;
		_bOnHeight = false;
		_bOnResizing = false;

		_oOnCell = null;
		_oOnTable = null;
		_oOnRow = null;

		_aDoWCellObj.length=0;
		_aDoWCellWidth.length=0;
		_aDoHCellObj.length=0;
		_aDoHCellHeight.length=0;

		_oSepDivV.style.display="none";
		_oSepDivH.style.display="none";
	};

	var _GetCellWidth = function(o_Cell){
		var n = o_Cell.offsetWidth;
		n = n - _ConvertToPx(EWEBTools.GetCurrentElementStyle(o_Cell, "padding-left"));
		n = n - _ConvertToPx(EWEBTools.GetCurrentElementStyle(o_Cell, "padding-right"));
		return n;
	};

	var _GetCellHeight = function(o_Cell){
		var n = o_Cell.offsetHeight;
		n = n - _ConvertToPx(EWEBTools.GetCurrentElementStyle(o_Cell, "padding-top"));
		n = n - _ConvertToPx(EWEBTools.GetCurrentElementStyle(o_Cell, "padding-bottom"));
		return n;
	};

	var _GetRowSpan = function(o_Cell){
		if (isNaN(parseInt(o_Cell.rowSpan))){
			return 1;
		}else{
			return parseInt(o_Cell.rowSpan);
		}
	}

	var _ConvertToPx = function(s_Value){
		if (!s_Value){return 0;}
		if (isNaN(parseInt(s_Value))){return 0;}

		s_Value = s_Value.toLowerCase();
		if (parseFloat(s_Value)==0){
			return 0;
		}else if (s_Value.EndsWith("px")){
			return parseInt(s_Value);		
		}else if (s_Value.EndsWith("pt")){
			return parseInt(parseFloat(s_Value)*4/3);
		}else if (s_Value.EndsWith("cm")){

			return parseInt(parseFloat(s_Value)*28.35*4/3);
		}else if (s_Value.EndsWith("mm")){
			return parseInt(parseFloat(s_Value)*0.001*28.35*4/3);
		}else{
			return 0;
		}
	};

	var _ShowSep = function(){
		_oSepDivV.style.display="none";
		_oSepDivH.style.display="none";

		if(_bOnWidth){
			_oSepDivV.style.cursor="e-resize";
			_oSepDivV.style.display="";
		}

		if(_bOnHeight){
			_oSepDivH.style.cursor = "s-resize";
			_oSepDivH.style.display="";
		}

		if (_bOnWidth && _bOnHeight){
			_oSepDivV.style.cursor="se-resize";
			_oSepDivH.style.cursor = "se-resize";
		}

	};

	return{
		
		OnResizing : function(){
			return _bOnResizing;
		},

		MM : function(e){
			if (_bOnResizing){
				this.MM2(e);
				return;
			}

			if (!_bInit){
				_Init();
			}
			
			_ResetVar();


			var el = e.srcElement || e.target;
			while (el.tagName.toUpperCase() != "TD" && el.tagName.toUpperCase() != "TH"){
				if (el.tagName.toUpperCase()=="BODY" || el.tagName.toUpperCase()=="HTML"){
					_ResetVar();
					return;
				}
				el = el.parentNode;
				if (!el){
					_ResetVar();
					return;
				}
			}
			_oOnCell = el;


			var n_Left = 0;
			var n_Top = 0;
			while (el){
				n_Left += el.offsetLeft;
				n_Top += el.offsetTop;
				el = el.offsetParent;
			}

			var scrollPos = EWEBTools.GetScrollPosition(EWEB.EditorWindow);
			

			var n_Right = n_Left + _oOnCell.offsetWidth - scrollPos.X;
			_nScreenClientX = e.screenX - e.clientX;
			_nCellMinX = n_Right - _nTriggerAreaWidth;
			if( e.clientX > _nCellMinX){
				_bOnWidth = true;
			}


			var n_Bottom = n_Top + _oOnCell.offsetHeight - scrollPos.Y;
			_nScreenClientY = e.screenY - e.clientY;
			_nCellMinY = n_Bottom - _nTriggerAreaWidth;
			if( e.clientY > _nCellMinY){
				_bOnHeight = true;
			}
			
			if (_bOnWidth || _bOnHeight){
				_nSepDivVLeft = n_Right+$("eWebEditor").offsetLeft+$("eWebEditor_Layout").offsetLeft;
				_nSepDivVTop = $("eWebEditor").offsetTop+$("eWebEditor_Layout").offsetTop+$("eWebEditor_ToolarPTR").offsetHeight;
				_oSepDivV.style.left=_nSepDivVLeft+"px";
				_oSepDivV.style.top=_nSepDivVTop+"px";
				_oSepDivV.style.width=_nSepWidth+"px";
				_oSepDivV.style.height=$("eWebEditor").offsetHeight+"px";

				_nSepDivHLeft = $("eWebEditor").offsetLeft+$("eWebEditor_Layout").offsetLeft;
				_nSepDivHTop = n_Bottom+$("eWebEditor").offsetTop+$("eWebEditor_Layout").offsetTop+$("eWebEditor_ToolarPTR").offsetHeight;
				_oSepDivH.style.left=_nSepDivHLeft+"px";
				_oSepDivH.style.top=_nSepDivHTop+"px";
				_oSepDivH.style.width=$("eWebEditor").offsetWidth+"px";
				_oSepDivH.style.height=_nSepWidth+"px";
			}

			_ShowSep();
			

		},

		MD2 : function(e){
			if (!e){
				e = eWebEditor.event;
			}

			if (EWEBBrowser.IsIE && e.button!=1){
				return;
			}

			_bOnResizing = true;

			_nStartX=e.screenX;
			_nStartY=e.screenY;

			_oOnTable = EWEBTools.GetParentNodeByTag(_oOnCell, "TABLE");

			if (_bOnWidth){
				_nOnTableWidth = _oOnTable.offsetWidth;
				_oOnTable.style.width = _oOnTable.offsetWidth;
				_nOnCellWidth = _oOnCell.offsetWidth;

				for(var i=0;i<_oOnTable.rows.length;i++){
					for(var j=0;j<_oOnTable.rows[i].cells.length;j++ ){
						var o_Cell = _oOnTable.rows[i].cells[j];
						o_Cell.style.width = _GetCellWidth(o_Cell);
						o_Cell.removeAttribute("width");
					}
				}

				_aDoWCellObj.length=0;
				_aDoWCellWidth.length=0;
				var n = 0;
				for(var i=0;i<_oOnTable.rows.length;i++){
					for(var j=0;j<_oOnTable.rows[i].cells.length;j++ ){
						var o_Cell = _oOnTable.rows[i].cells[j];
						if ((o_Cell.offsetLeft+o_Cell.offsetWidth)>=(_oOnCell.offsetLeft+_oOnCell.offsetWidth) && (o_Cell.offsetLeft<(_oOnCell.offsetLeft+_oOnCell.offsetWidth))){
							_aDoWCellObj[n] = o_Cell;
							_aDoWCellWidth[n] = parseInt(o_Cell.style.width);
							n++;
							break;
						}
					}
				}
			}



			if (_bOnHeight){
				_oOnRow=_oOnCell.parentNode;
				_nOnTableHeight = _oOnTable.offsetHeight;
				_oOnTable.style.height = _oOnTable.offsetHeight;
				_nOnRowHeight = _oOnCell.offsetHeight;
				for(var i=0;i<_oOnTable.rows.length;i++){
					for(var j=0;j<_oOnTable.rows[i].cells.length;j++ ){
						var o_Cell = _oOnTable.rows[i].cells[j];
						o_Cell.style.height = _GetCellHeight(o_Cell);
						o_Cell.removeAttribute("height");
					}
				}

				_aDoHCellObj.length=0;
				_aDoHCellHeight.length=0;
				var n = 0;
				var n_Row = _oOnRow.rowIndex + _GetRowSpan(_oOnRow);
				for(var i=0;i<n_Row;i++){
					for(var j=0;j<_oOnTable.rows[i].cells.length;j++ ){
						var o_Cell = _oOnTable.rows[i].cells[j];
						if ((_GetRowSpan(o_Cell)+i)>=n_Row && i<n_Row){
							_aDoHCellObj[n] = o_Cell;
							_aDoHCellHeight[n] = parseInt(o_Cell.style.height);
							n++;
						}
					}
				}
			}



		},

		MM2 : function(e){
			if(!e){
				e = eWebEditor.event;
			}

			if (EWEBBrowser.IsIE && e.button!=1 && _bOnResizing){
				_ResetVar();
			}
			
			if (!_bOnResizing){
				var b_Changed = false;
				if (e.screenX-_nScreenClientX>_nCellMinX){
					if (!_bOnWidth){
						b_Changed = true;
						_bOnWidth = true;
					}
				}else{
					if (_bOnWidth){
						b_Changed = true;
						_bOnWidth = false;
					}
				}
				if (e.screenY-_nScreenClientY>_nCellMinY){
					if (!_bOnHeight){
						b_Changed = true;
						_bOnHeight = true;
					}
				}else{
					if (_bOnHeight){
						b_Changed = true;
						_bOnHeight = false;
					}
				}
				if (b_Changed){
					_ShowSep();
				}
				
				return;
			}


			if (_bOnWidth){
				var n_SepResize = e.screenX - _nStartX;
				var n_CellResize = n_SepResize;
				if (_oOnTable.align.toLowerCase()=="center"){
					n_CellResize = 2*n_CellResize;
				}
				var n_NewCellWidth = n_CellResize + _nOnCellWidth;
				var n_NewTableWidth = n_CellResize + _nOnTableWidth;
				if(n_NewCellWidth>=2){
					_oOnTable.style.width = n_NewTableWidth+"px";
					_oSepDivV.style.left=(_nSepDivVLeft+n_SepResize)+"px";

					for (var i=0; i<_aDoWCellObj.length; i++){
						try{
							_aDoWCellObj[i].style.width = (n_CellResize + _aDoWCellWidth[i]) + "px";
						}catch(er){}
					}
				}
			}
			
			if(_bOnHeight){
				var n_SepResize = e.screenY - _nStartY;
				var n_NewRowHeight=n_SepResize + _nOnRowHeight;
				var n_NewTableHeight = n_SepResize + _nOnTableHeight;

				if(n_NewRowHeight>=2 ){
					_oOnTable.style.height = n_NewTableHeight+"px";
					_oSepDivH.style.top=(_nSepDivHTop+n_SepResize)+"px";

					for (var i=0; i<_aDoHCellObj.length; i++){
						try{
							_aDoHCellObj[i].style.height = (n_SepResize + _aDoHCellHeight[i]) + "px";
						}catch(er){}
					}
				}
			}

		},


		MU2 : function(e){
			if (!_bOnResizing){
				return;
			}
			_ResetVar();
		},

		PMM : function(e){
			if(!e){
				e = window.event;
			}

			if (EWEBBrowser.IsIE && e.button!=1 && _bOnResizing){
				_ResetVar();
			}

		}

		



	};

})();








var EWEBHistory = (function(){
	var _data = [];
	var _position = 0;
	var _bookmark = [];
	var _saved = false;


	var _SetHistoryCursor = function(){
		var s_Bookmark = _bookmark[_position];
		if (s_Bookmark){

			
			if (EWEBBrowser.IsIE){
				eWebEditor_Layout.focus();
				if (s_Bookmark.substring(0,8) != "[object]"){
					var r = EWEB.EditorDocument.body.createTextRange();
					if (r.moveToBookmark(_bookmark[_position])){


						r.select();
					}
				}else{
					if (EWEB.CurrMode=="EDIT"){
						var r = EWEB.EditorDocument.body.createControlRange();
						var a = s_Bookmark.split("|");
						var els = EWEB.EditorDocument.body.getElementsByTagName(a[1]);
						var el = els[a[2]];
						r.addElement(el);
						r.select();
					}
				}
			}else{
				

                //EWEB.EditorWindow.getSelection().addRange(_bookmark[_position]);

			}
		}
	};


	var _GetElementTagIndex = function(el){
		var els = EWEB.EditorDocument.body.getElementsByTagName(el.tagName);
		for (var i=0; i<els.length; i++){
			if (els[i]==el){
				return i;
			}
		}
		return null;
	};



	return {

		QueryUndoState : function(){
			if (_data.length <= 1 || _position <= 0){
				return false;
			}
			return true;
		},

		QueryRedoState : function(){
			if (_position >= _data.length-1 || _data.length == 0){
				return false;
			}
			return true;
		},


		Change : function(){
			_saved = false;
		},

		Save: function(){
			if (_saved){
				return;
			}
			_saved = true;
			var s_Html = getHTML();
			if (_data[_position] == s_Html){return;}

			var nBeginLen = _data.length;
			var nPopLen = _data.length - _position;
			for (var i=1; i<nPopLen; i++){
				_data.pop();
				_bookmark.pop();
			}

			_data[_data.length] = s_Html;
			
			if (EWEBBrowser.IsIE){
			
				if (EWEBSelection.GetType() != "Control"){
					try{
						_bookmark[_bookmark.length] = EWEB.EditorDocument.selection.createRange().getBookmark();
					}catch(e){
						_bookmark[_bookmark.length] = "";
					}
				} else {
					var el = EWEBSelection.GetSelectedElement();
					_bookmark[_bookmark.length] = "[object]|" + el.tagName + "|" + _GetElementTagIndex(el);
				}

			}else{
				try{
					_bookmark[_bookmark.length] = EWEB.EditorWindow.getSelection().getRangeAt(0).endContainer;
				}catch(e){
					_bookmark[_bookmark.length] = "";
				}

			}

			if (nBeginLen!=0){
				_position++;
			}
		},


		Go : function(v){
			if (!_saved){this.Save();}



			if (v == -1){
				if (_position > 0){
					_position = _position - 1;
					setHTML(_data[_position], true);
					_SetHistoryCursor();

				}

			} else {
				if (_position < _data.length -1){
					_position = _position + 1;
					setHTML(_data[_position], true);
					_SetHistoryCursor();
				}
			}
			EWEB.Focus();
			EWEBToolbar.CheckTBStatus();
		}



	};

})();






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
			eWebEditorActiveX.Lang = "zh-cn";
			eWebEditorActiveX.SendUrl = EWEB.SendUrl;
			eWebEditorActiveX.LocalSize = config.AllowLocalSize;
			eWebEditorActiveX.LocalExt = config.AllowLocalExt;
			b = true;
		}
	}catch(e){}

	if (!b && b_AutoInstall){
		showDialog("installactivex.htm", true);
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






var EWEB = {
	EditorDocument : null,
	EditorWindow : null,
	CurrMode : null,
	LinkField : null,
	
	BaseHref : "",
	RootPath : "",
	SitePath : "",
	SendUrl : "",
	
	ReadyState : "",


	Focus : function(){
		if (EWEB.CurrMode=="CODE" || EWEB.CurrMode=="TEXT"){
			EWEB.EditorTextarea.focus();
			return;
		}
		if (EWEBBrowser.IsIE){
			if (config.FixWidth){
				if(document.activeElement.id != "eWebEditor"){
					eWebEditor.focus();
				}
				try{
					var rng = EWEB.EditorDocument.selection.createRange();
					if (rng.parentElement().tagName=="BODY"){
						rng.moveToElementText(EWEB.EditorDocument.getElementById("eWebEditor_FixWidth_DIV"));
						rng.collapse(true);
						rng.select();
					}else{
						rng.select();
					}
				}catch(e){}

			}else{
				eWebEditor.focus();
			}

		}else{
			this.EditorWindow.focus();
		}
	},



	Init : function(){
//		if (!config.L){return;}
		if (!EWEBBrowser.IsCompatible){return;}

		this.SitePath = document.location.protocol.toLowerCase() + "//" + document.location.host;

		var s = document.location.pathname;
		this.RootPath = s.substr(0, s.length-15);

		this.BaseHref = "";
		if(config.BaseHref!=""){
			this.BaseHref = "<base href='" + this.SitePath + config.BaseHref + "'></base>";
		}

		if (EWEBParam.ExtCSS){
			this.ExtCSS = "<link href='" + _Relative2fullpath(EWEBParam.ExtCSS) + "' type='text/css' rel='stylesheet'>";
		}else{
			this.ExtCSS = "";
		}

		if (EWEBParam.Skin){
			config.Skin = EWEBParam.Skin;
		}
		if (EWEBParam.FixWidth){
			config.FixWidth = EWEBParam.FixWidth;
		}
		if (EWEBParam.AreaCssMode){
			config.AreaCssMode = EWEBParam.AreaCssMode;
		}
		if (EWEBParam.ReadOnly){
			config.InitMode="VIEW";
			if (EWEBParam.ReadOnly=="2"){
				config.StateFlag = "";
			}else{
				config.SBCode = "";
				config.SBEdit = "";
				config.SBText = "";
				config.SBView = "";
			}
		}

		this.SendUrl = this.SitePath + this.RootPath + "/" + config.ServerExt + "/upload." + config.ServerExt + "?style=" + EWEBParam.StyleName + "&cusdir=" + EWEBParam.CusDir + "&sparams=" + config.SParams;

		EWEBTools.AddEventListener( document, 'contextmenu', _Doc_OnCancel ) ;
		EWEBTools.AddEventListener( document, 'dragstart', _Doc_OnCancel ) ;
		EWEBTools.AddEventListener( document, 'selectstart', _Doc_OnCancel ) ;
		EWEBTools.AddEventListener( document, 'select', _Doc_OnCancel ) ;


		if ( EWEBBrowser.IsGecko && !EWEBBrowser.IsOpera ){
			window.onresize = function( e ){
				if ( e && e.originalTarget !== document && e.originalTarget !== window && (!e.originalTarget.ownerDocument || e.originalTarget.ownerDocument != document )){
					return ;
				}

				_GeckoResize();
			};
		}


	}

};




EWEB.MakeEditable = function(){
	var oDoc = this.EditorDocument ;

	if ( EWEBBrowser.IsIE ){
		if (config.FixWidth){

			oDoc.body.contentEditable = false;
			o_Div = oDoc.getElementById("eWebEditor_FixWidth_DIV");

			o_Div.contentEditable = true;

		}else{


			oDoc.body.contentEditable = true;

		}

		oDoc.execCommand("2D-Position",true,true);



	}else{
		try{


			oDoc.designMode = 'on' ;

			oDoc.execCommand("styleWithCSS",false,"true");
			oDoc.execCommand("enableInlineTableEditing",false,"false");
			if (config.EnterMode=="2"){
				oDoc.execCommand("insertBrOnReturn",false,"true");
			}
			if (config.FixWidth){
				oDoc.designMode = 'off' ;
				o_Div = oDoc.getElementById("eWebEditor_FixWidth_DIV");
				o_Div.contentEditable = "true";
			}
		}catch (e){
			EWEBTools.AddEventListener( this.EditorWindow.frameElement, 'DOMAttrModified', _EWEBFrame_DomAttrModified ) ;
		}

	}
};

function _EWEBFrame_DomAttrModified( evt ){
	if ( EWEB._timer ){
		window.clearTimeout( EWEB._timer ) ;
	}

	EWEB._timer = EWEBTools.SetTimeout( _EWEBFrame_MakeEditableByMutation, 1000, EWEB ) ;
}

function _EWEBFrame_MakeEditableByMutation(){
	delete this._timer ;
	EWEBTools.RemoveEventListener( this.EditorWindow.frameElement, 'DOMAttrModified', _EWEBFrame_DomAttrModified ) ;

	this.MakeEditable() ;
}





function _Doc_OnCancel(e){
	if (EWEB.CurrMode=="EDIT" || EWEB.CurrMode=="VIEW"){
		return EWEBTools.CancelEvent(e);
	}
}





function _GeckoResize(){
	var eInnerElement = $( 'eWebEditor' ) ;
	if ( eInnerElement ){
		var oCell = eInnerElement.parentNode;

		eInnerElement.style.width=(oCell.scrollWidth-4 )+'px';
		eInnerElement.style.height=(oCell.scrollHeight-4 )+'px';
		eInnerElement.style.height="100%";
	}else{
		window.setTimeout(_GeckoResize, 10);
	}

}











var config = new Object();








window.onload = _Win_Onload;

function _Win_Onload(){
//	if (!config.L){return;}
	if (EWEB.ReadyState){return;}
	EWEB.ReadyState = "loading";

	EWEBTools.RegisterDollarFunction(window);

	if (!EWEBBrowser.IsCompatible){return;}
	EWEB.LinkField = parent.document.getElementsByName(EWEBParam.LinkField)[0];

	if ( EWEBBrowser.IsGecko && !EWEBBrowser.IsOpera ){
		_GeckoResize();
	}

	EWEBToolbar.InitTB();

	EWEBTools.DisableSelection(document.body);
	if (EWEBBrowser.IsIE){


	}else{
		$("eWebEditorTextarea").style.MozUserSelect = "text";
	}

	EWEB.EditorWindow = $("eWebEditor").contentWindow;
	EWEB.EditorDocument = EWEB.EditorWindow.document;
	EWEB.EditorTextarea = $("eWebEditorTextarea");

	if (!EWEBBrowser.IsCompatible){
		config.InitMode = "TEXT";
	}

	if ($("D_ContentFlag").value=="0"){
		$("D_ContentEdit").value = EWEB.LinkField.value;
		$("D_ContentLoad").value = EWEB.LinkField.value;
		$("D_CurrMode").value = config.InitMode;
		$("D_ContentFlag").value = "1";
	}

	setMode($("D_CurrMode").value, true);

	_SetLinkedField();
	EWEB.ReadyState = "complete";
	try{parent.EWEBEDITOR_OnLoadComplete(EWEBParam.LinkField);}catch(e){}
}


function _SetLinkedField(){
	if (! EWEB.LinkField){return ;}
	var oForm = EWEB.LinkField.form;
	if (!oForm){return ;}

	EWEBTools.AddEventListener( oForm, 'submit', _AttachSubmit ) ;

	if (!oForm.submitEditor){oForm.submitEditor = new Array();}
	oForm.submitEditor[oForm.submitEditor.length] = _AttachSubmit;
	if (! oForm.originalSubmit){
		oForm.originalSubmit = oForm.submit;
		oForm.submit = function(){
			if (this.submitEditor){
				for (var i = 0 ; i < this.submitEditor.length ; i++){
					this.submitEditor[i]();
				}
			}
			this.originalSubmit();
		};
	}

	EWEBTools.AddEventListener( oForm, 'reset', _AttachReset ) ;

	if (! oForm.resetEditor) oForm.resetEditor = new Array();
	oForm.resetEditor[oForm.resetEditor.length] = _AttachReset;
	if (! oForm.originalReset){
		oForm.originalReset = oForm.reset;
		oForm.reset = function(){
			if (this.resetEditor){
				for (var i = 0 ; i < this.resetEditor.length ; i++){
					this.resetEditor[i]();
				}
			}
			this.originalReset();
		};
	}
}

function _AttachSubmit(){
	var oForm = EWEB.LinkField.form;
	if (!oForm){return;}

	var s_Html = getHTML();

	if (config.PaginationMode!="0" && config.PaginationAutoFlag!="0"){
		if (EWEB.CurrMode!="EDIT"){
			setMode("EDIT");
		}

		var b=true;
		if (config.PaginationAutoFlag=="1"){
			var els=EWEB.EditorDocument.getElementsByTagName("IMG");
			for (var i=0; i<els.length; i++){
				var s_Attr=els[i].getAttribute("eWebEditorFake",2);
				if (s_Attr){
					if (s_Attr.toLowerCase()=="pagination"){
						b=false;
						break;
					}
				}
			}
		}
		if (b){
			EWEBPagination.Auto(config.PaginationAutoNum);
			s_Html = getHTML();
		}
	}

	$("D_ContentEdit").value = s_Html;
	_SplitTextField(EWEB.LinkField, s_Html);
}

function _AttachReset(){
	setHTML($("D_ContentLoad").value);
}


function _SubmitLinkForm(){
	var o_Form = EWEB.LinkField.form;
	if (!o_Form){return ;}
	if (o_Form.onsubmit){
		if (o_Form.onsubmit()){
			o_Form.submit();
		}
	}else{
		o_Form.submit();
	}
}


function  _Iframe_Doc_OnHelp(e){
	showDialog('about.htm');
	return EWEBTools.CancelEvent(e);
}


function  _Iframe_Doc_OnPaste(e){
	if (!e){e = eWebEditor.event;}

	if (EWEBBrowser.IsIE){
		return EWEBTools.CancelEvent(e);
	}else{
		if (config.AutoDetectPaste=="2"){
			window.setTimeout("pasteText()", 10);
			return EWEBTools.CancelEvent(e);
		}
		EWEBHistory.Save();
		return true;
	}
}


function  _Iframe_Doc_OnKeyUp(e){
	if (!e){
		e = eWebEditor.event;
	}

	var n_KeyCode = e.keyCode || e.which;
	var s_Key = String.fromCharCode(n_KeyCode).toUpperCase();




	if (e.ctrlKey || ((n_KeyCode>=33)&&(n_KeyCode<=40)) || (n_KeyCode==13) || (n_KeyCode==8) || (n_KeyCode==46) ){
		EWEBToolbar.CheckTBStatus();
	}

	return true;
}


function _Iframe_Doc_OnKeyDown(e){
	if (!e){
		e = eWebEditor.event;
	}
	
	var n_KeyCode = e.keyCode || e.which;
	var s_Key = String.fromCharCode(n_KeyCode).toUpperCase();

	var b_Cancel = false;


	if (n_KeyCode==112 && !EWEBBrowser.IsIE){
		showDialog("about.htm");
		b_Cancel = true;
	}


	if (n_KeyCode==113){
		EWEBCommandShowBorders.Execute();
		b_Cancel = true;
	}


	if (n_KeyCode==114){
		EWEBCommandShowBlocks.Execute();
		b_Cancel = true;
	}


	if (e.ctrlKey){

		if (n_KeyCode==13){
			_SubmitLinkForm();
			b_Cancel = true;
		}

		if (n_KeyCode==187 || n_KeyCode==107){
			sizeChange(300);
			b_Cancel = true;
		}

		if (n_KeyCode==189 || n_KeyCode==109){
			sizeChange(-300);
			b_Cancel = true;
		}

		if (s_Key=="1"){
			setMode("CODE");
			b_Cancel = true;
		}

		if (s_Key=="2"){
			setMode("EDIT");
			b_Cancel = true;
		}

		if (s_Key=="3"){
			setMode("TEXT");
			b_Cancel = true;
		}

		if (s_Key=="4"){
			setMode("VIEW");
			b_Cancel = true;
		}


		if (s_Key=="A"){
			if ((EWEB.CurrMode!="CODE")&&(config.FixWidth)){
				_SelectAll_FixWidth();
			}else{
				EWEB.Focus();
				EWEBHistory.Save();
				EWEB.EditorDocument.execCommand("SelectAll",false,null);
				EWEBHistory.Save();
				EWEB.Focus();
			}
			b_Cancel = true;
		}


		if (s_Key == "D"){
			pasteWord();
			b_Cancel = true;
		}

		if (s_Key == "R"){
			findReplace();
			b_Cancel = true;
		}

		if (s_Key == "Z"){
			EWEBHistory.Go(-1);
			b_Cancel = true;
		}

		if (s_Key == "Y"){
			EWEBHistory.Go(1);
			b_Cancel = true;
		}

		if (s_Key == "V" && EWEBBrowser.IsIE){
			window.setTimeout("doPaste()", 10);
			b_Cancel = true;
		}
	}


	if ((config.EnterMode=="2")&&(n_KeyCode==13)&& EWEBBrowser.IsIE){
		EWEBHistory.Save();
		EWEBHistory.Change();

		var rng = EWEB.EditorDocument.selection.createRange();
		if (e.shiftKey){
			var p = rng.parentElement();
			if (p.tagName!="P" || p.innerHTML==""){
				rng.pasteHTML("&nbsp;");
				rng.select();
				rng.collapse(false);
			}

			try{
				rng.pasteHTML("</P><P id=eWebEditor_Temp_P>");
			}catch(err){
				return false;
			}

			e.cancelBubble = true;
			e.returnValue = false;
			
			var el=EWEB.EditorDocument.getElementById("eWebEditor_Temp_P");
			if (el.innerHTML==""){
				el.innerHTML="&nbsp;";
			}
			rng.moveToElementText(el);
			rng.select();
			rng.collapse(false);
			rng.select();
			el.removeAttribute("id");
		}else{
			try{
				rng.pasteHTML("<br>");
			}catch(err){
				return false;
			}
			e.cancelBubble = true;
			e.returnValue = false;
			rng.select();
			rng.moveEnd("character", 1);
			rng.moveStart("character", 1);
			rng.collapse(false);
		}
		b_Cancel = true;
	}




	if (b_Cancel){
		return EWEBTools.CancelEvent(e);
	}else{

		if ((n_KeyCode==13)||(n_KeyCode==8)||(n_KeyCode==46)){
			EWEBHistory.Save();
			EWEBHistory.Change();

		}else if ((n_KeyCode>=33)&&(n_KeyCode<=40)){
			EWEBHistory.Save();

		}else if (!((e.ctrlKey && s_Key=="A") || (e.ctrlKey && s_Key=="F"))){
			EWEBHistory.Change();
		}
		
		return true;
	}
}





function  _Iframe_Doc_OnMouseDown(e){

}

function  _Iframe_Doc_OnMouseUp(e){
	if (!e){
		e = eWebEditor.event;
	}

	EWEBTableResize.MU2(e);

	EWEBHistory.Save();
	EWEBToolbar.CheckTBStatus();
}

function  _Iframe_Doc_OnMouseMove(e){
	if (!e){
		e = eWebEditor.event;
	}

	EWEBTableResize.MM(e);


}


function  _Iframe_Doc_OnDragEnd(){
	EWEBHistory.Save();
	return true;
}





function _Relative2fullpath(s_Url){
	if(s_Url.indexOf("://")>=0){return s_Url;}
	if(s_Url.substr(0,1)=="/"){return s_Url;}

	var s_Path = EWEB.RootPath;
	while(s_Url.substr(0,3)=="../"){
		s_Url = s_Url.substr(3);
		s_Path = s_Path.substring(0, s_Path.lastIndexOf("/"));
	}
	return s_Path + "/" + s_Url;
}





function insertHTML(s_Html){
	if (_IsModeView()){return;}

	switch (EWEB.CurrMode){
	case "EDIT":
		EWEBSelection.Restore() ;
		EWEB.Focus()

		s_Html = EWEBFake.Normal2Fake(s_Html);

		if (EWEBBrowser.IsIE){
			if (EWEB.EditorDocument.selection.type.toLowerCase() == "control"){
				EWEB.EditorDocument.selection.clear();
			}

			s_Html = '<span id="__ewebeditor_temp_remove__" style="display:none;">eWebEditor</span>' + s_Html ;
			EWEB.EditorDocument.selection.createRange().pasteHTML(s_Html) ;
			EWEB.EditorDocument.getElementById('__ewebeditor_temp_remove__').removeNode( true ) ;

		}else{
			var o_DocFrag = EWEB.EditorDocument.createDocumentFragment();
			var o_TmpDiv = EWEB.EditorDocument.createElement( 'div' ) ;
			o_TmpDiv.innerHTML = s_Html ;
			
			var o_Child ;
			while ( (o_Child = o_TmpDiv.firstChild) ){
				o_DocFrag.appendChild( o_TmpDiv.removeChild( o_Child ) ) ;
			}

			var o_Range = EWEB.EditorWindow.getSelection().getRangeAt(0);
			o_Range.deleteContents()
			o_Range.insertNode( o_DocFrag ) ;


		}
		EWEBToolbar.CheckTBStatus();
		break;
	case "TEXT":
	case "CODE":
		EWEB.EditorTextarea.focus();
		if (EWEBBrowser.IsIE){
			EWEB.EditorDocument.selection.createRange().text = s_Html ;
		}else{
			EWEB.EditorTextarea.execCommand("insertHTML", false, s_Html);
		}
		break;
	}
};


function setHTML(s_Html, b_NotSaveHistory){
	$("D_ContentEdit").value = s_Html;
	switch (EWEB.CurrMode){
	case "CODE":
		EWEB.EditorTextarea.value = EWEBCodeFormat.Format(s_Html);
		break;
	case "TEXT":
		s_Html = s_Html.replace(/<script[^>]*?>(?:[^a]|a)*?<\/script>/gi,"");
		s_Html = s_Html.replace(/<style[^>]*?>(?:[^a]|a)*?<\/style>/gi,"");
		s_Html = s_Html.replace(/<object[^>]*?>(?:[^a]|a)*?<\/object>/gi,"");
		s_Html = s_Html.replace(/<embed[^>]*?>(?:[^a]|a)*?<\/embed>/gi,"");
		s_Html = s_Html.replace(/<table[^>]*?>(?:[^a]|a)*?<\/table>/gi,"");

		$("eWebEditor_Temp_HTML").innerHTML = s_Html;
		if (EWEBBrowser.IsIE){
			s_Html = $("eWebEditor_Temp_HTML").innerText;
		}else{
			s_Html = _GetInnerTextGecko($("eWebEditor_Temp_HTML"));
		}
	
		EWEB.EditorTextarea.value = s_Html;
		break;
	case "EDIT":
		EWEB.EditorDocument.designMode="on";
		EWEB.EditorDocument.open();
		EWEB.EditorDocument.write(_GetHtmlWithHeader(EWEBFake.Normal2Fake(s_Html)));
		EWEB.EditorDocument.close();

		EWEB.MakeEditable();

		EWEBTools.AddEventListener( EWEB.EditorDocument.body, 'paste',  _Iframe_Doc_OnPaste );
		EWEBTools.AddEventListener( EWEB.EditorDocument, 'help',  _Iframe_Doc_OnHelp );
		EWEBTools.AddEventListener( EWEB.EditorDocument.body, 'dragend',  _Iframe_Doc_OnDragEnd );
		EWEBTools.AddEventListener( EWEB.EditorDocument, 'keydown', _Iframe_Doc_OnKeyDown );
		EWEBTools.AddEventListener( EWEB.EditorDocument, 'keyup',  _Iframe_Doc_OnKeyUp );
		EWEBTools.AddEventListener( EWEB.EditorDocument, 'contextmenu', EWEBMenu.ShowContextMenu ) ;
		EWEBTools.AddEventListener( EWEB.EditorDocument, 'mousedown',  _Iframe_Doc_OnMouseDown ) ;
		EWEBTools.AddEventListener( EWEB.EditorDocument, 'mouseup',  _Iframe_Doc_OnMouseUp ) ;
		EWEBTools.AddEventListener( EWEB.EditorDocument, 'mousemove',  _Iframe_Doc_OnMouseMove ) ;

		if (EWEBBrowser.IsIE){
			if (config.FixWidth){
				EWEBTools.AddEventListener( EWEB.EditorDocument.getElementById("eWebEditor_FixWidth_DIV"), 'beforedeactivate',  function(){ EWEBSelection.Save();} ) ;
			}else{
				EWEBTools.AddEventListener( EWEB.EditorDocument, 'beforedeactivate',  function(){ EWEBSelection.Save(); } ) ;
			}
		}


		break;
	case "VIEW":
		EWEB.EditorDocument.designMode="off";
		EWEB.EditorDocument.open();
		EWEB.EditorDocument.write(_GetHtmlWithHeader(s_Html));
		EWEB.EditorDocument.close();

		EWEBTools.AddEventListener( EWEB.EditorDocument, 'help',  _Iframe_Doc_OnHelp );
		EWEBTools.AddEventListener( EWEB.EditorDocument, 'contextmenu', EWEBTools.CancelEvent ) ;
		break;
	}


	if (!b_NotSaveHistory){
		EWEBHistory.Save();
	}
}



function getHTML(b_NotCheckEmpty){
	var s_Html;
	switch(EWEB.CurrMode){
	case "CODE":
		s_Html = EWEB.EditorTextarea.value;
		break;
	case "EDIT":
		EWEBPagination.Fix();
		if (config.FixWidth){
			s_Html = EWEB.EditorDocument.getElementById("eWebEditor_FixWidth_DIV").innerHTML;
		}else{
			s_Html = EWEB.EditorDocument.body.innerHTML;
		}
		s_Html = EWEBFake.Fake2Normal(s_Html);
		break;
	case "VIEW":
		s_Html = $("D_ContentEdit").value;
		break;
	case "TEXT":
		s_Html = EWEB.EditorTextarea.value;
		s_Html = HTMLEncode(s_Html);
		break;
	default:
		s_Html = $("D_ContentEdit").value;
		break;
	}

	if (!b_NotCheckEmpty){
		if ((s_Html.toLowerCase()=="<p>&nbsp;</p>")||(s_Html.toLowerCase()=="<p></p>")||(s_Html.toLowerCase()=="<br>")){
			s_Html = "";
		}
	}
	return s_Html;
}

function appendHTML(s_Html){
	if(_IsModeView()){return;}

	switch(EWEB.CurrMode){
	case "EDIT":
		s_Html = EWEBFake.Normal2Fake(s_Html);
		EWEB.EditorDocument.body.innerHTML += s_Html;
		break;
	case "CODE":
	case "TEXT":
		EWEB.EditorTextarea.value += s_Html;
		break;
	}
}


function openUploadDialog(s_Type, s_Mode, s_LinkID_SavePathFileName, s_LinkID_SaveFileName, s_LinkID_OriginalFileName){
	EWEBDialog.OpenDialog('i_upload.htm?type='+s_Type+'&mode='+s_Mode+'&savepathfilename='+s_LinkID_SavePathFileName+'&savefilename='+s_LinkID_SaveFileName+'&originalfilename='+s_LinkID_OriginalFileName);
}

function _IsModeEdit(){
	if(EWEB.CurrMode=="EDIT"){return true;}
	alert(lang["MsgOnlyInEditMode"]);
	return false;
}

function _IsModeView(){
	if (EWEB.CurrMode=="VIEW"){
		alert(lang["MsgCanotSetInViewMode"]);
		return true;
	}
	return false;
}

function _IsInIE(){
	if (EWEBBrowser.IsIE){return true;}

	alert(lang["MsgOnlyForIE"]);
	return false;
}


function format(s_CmdName, s_CmdValue){
	if(!_IsModeEdit()){return;}
	EWEBSelection.Restore() ;
	EWEB.EditorWindow.focus() ;
	EWEB.Focus();
	EWEBHistory.Save();

	if ((s_CmdName=="unselect")&&(!EWEBBrowser.IsIE)){
		EWEBSelection.Collapse(true);
	}else if((s_CmdName=="selectall")&&(config.FixWidth)){
		_SelectAll_FixWidth();

	}else{
		EWEB.EditorDocument.execCommand(s_CmdName,false,s_CmdValue);
	}
	EWEBHistory.Save();
	EWEB.Focus();
	EWEBToolbar.CheckTBStatus();
}


function _SelectAll_FixWidth(){
	if (EWEBBrowser.IsIE){
		var r = EWEB.EditorDocument.body.createTextRange();
		r.moveToElementText(EWEB.EditorDocument.getElementById("eWebEditor_FixWidth_DIV"));
		r.select();
	}else{
		var r = EWEBSelection.GetSelection().getRangeAt(0);
		r.selectNodeContents(EWEB.EditorDocument.getElementById("eWebEditor_FixWidth_DIV"));
	}
}


function _Fontsize2Css(s){
	var r;
	switch(s+""){
	case "1":
		r = "8pt";
		break;
	case "2":
		r = "10pt";
		break;
	case "3":
		r = "12pt";
		break;
	case "4":
		r = "14pt";
		break;
	case "5":
		r = "18pt";
		break;
	case "6":
		r = "24pt";
		break;
	case "7":
		r = "36pt";
		break;
	default:
		r = "";
		break;
	}
	return r;
}

function setMode(s_NewMode, b_NotFocus){
	if (s_NewMode==EWEB.CurrMode){return;}

	if (s_NewMode=="TEXT"){
		if (EWEB.CurrMode==$("D_CurrMode").value){
			if (!confirm(lang["MsgHtmlToText"])){
				return;
			}
		}
	}

	try{$("eWebEditor_CODE").className = "SB_Mode_BtnOff";}catch(e){}
	try{$("eWebEditor_EDIT").className = "SB_Mode_BtnOff";}catch(e){}
	try{$("eWebEditor_TEXT").className = "SB_Mode_BtnOff";}catch(e){}
	try{$("eWebEditor_VIEW").className = "SB_Mode_BtnOff";}catch(e){}
	try{$("eWebEditor_"+s_NewMode).className = "SB_Mode_BtnOn";}catch(e){}

	if (EWEBParam.ReadOnly){
		$("eWebEditor_ToolarTREdit").style.display = "none";
		$("eWebEditor_ToolarTRText").style.display = "none";
	}else{
		if (s_NewMode=="EDIT"){
			$("eWebEditor_ToolarTREdit").style.display = "";
			$("eWebEditor_ToolarTRText").style.display = "none";
		}else{
			$("eWebEditor_ToolarTREdit").style.display = "none";
			if (config.TB2Flag=="1"){
				$("eWebEditor_ToolarTRText").style.display = "";
			}else{
				$("eWebEditor_ToolarTRText").style.display = "none";
			}
		}
	}

	if (s_NewMode=="EDIT" || s_NewMode=="VIEW"){
		$("eWebEditor").style.display = "";
		$("eWebEditorTextarea").style.display = "none";		
	}else{
		$("eWebEditor").style.display = "none";
		$("eWebEditorTextarea").style.display = "";
		if (s_NewMode=="CODE"){
			$("eWebEditorTextarea").className = "codemode";
		}else{
			$("eWebEditorTextarea").className = "textmode";
		}
	}


	var s_Html = getHTML(true);

	EWEB.CurrMode = s_NewMode;
	$("D_CurrMode").value = s_NewMode;
	
	setHTML(s_Html);

	if (s_NewMode=="EDIT"){
		EWEBCommandShowBlocks.RestoreState();
		EWEBCommandShowBorders.RestoreState();
	}
	EWEBToolbar.RefreshModeBtnStatus();

	if (!b_NotFocus){
		EWEB.Focus();
	}
}



function _GetInnerTextGecko(el){
	var a_BreakTag = ["p","div","h1","h2","h3","h4","h5","h6","pre","ol","ul","pre","fieldset","form","table","blockquote","dl","li","br"];
	var a_EmptyTag = ["script","style","table","object","embed"];
	var s_Txt = ""; 
	var o_Nodes = el.childNodes; 
	for(var i=0; i <o_Nodes.length; i++) { 
		if(o_Nodes[i].nodeType==1){
			var s_Tag = o_Nodes[i].tagName.toLowerCase();
			if (a_EmptyTag.IndexOf(s_Tag)<0){
				if (s_Tag=="pre"){
					var s_Pre = o_Nodes[i].innerHTML;
					s_Pre = s_Pre.replace(/<[^>]*?>/gi,"");
					s_Txt += s_Pre;
				}else{
					s_Txt += _GetInnerTextGecko(o_Nodes[i]);
				}
			}
			if (a_BreakTag.IndexOf(s_Tag)>=0){
				s_Txt += "\n";
			}
		}else if(o_Nodes[i].nodeType==3){
			s_Txt += o_Nodes[i].nodeValue; 
		} 
	}
	return s_Txt;
}



function showDialog(s_Url, b_MustInModeEdit, b_MustInIE){
	if (b_MustInModeEdit && !_IsModeEdit()){return;}
	if (b_MustInIE && !_IsInIE()){return;}

	if (s_Url.indexOf(".")<0){
		s_Url = s_Url + ".htm";
	}

	EWEBHistory.Save();
	EWEBDialog.OpenDialog(s_Url);
}






function HTMLEncode(s_Html){
	if (s_Html==null){return "";}
	s_Html = s_Html.replace(/&/gi, "&amp;");
	s_Html = s_Html.replace(/\"/gi, "&quot;");
	s_Html = s_Html.replace(/</gi, "&lt;");
	s_Html = s_Html.replace(/>/gi, "&gt;");
	s_Html = s_Html.replace(/ (?= )/gi,"&nbsp;");
	s_Html = s_Html.replace(/\n/gi,"<br>");
	return s_Html;
}

function HTMLDecode(s_Html){
	if (s_Html==null){return "";}
	s_Html = s_Html.replace(/<br(?=[ \/>]).*?>/gi, "\n");
	s_Html = s_Html.replace(/&nbsp;;/gi, " ");
	s_Html = s_Html.replace(/&quot;/gi, "\"");
	s_Html = s_Html.replace(/&lt;/gi, "<");
	s_Html = s_Html.replace(/&gt;/gi, ">");
	s_Html = s_Html.replace(/&amp;/gi, "&");
	return s_Html;
}





function addUploadFile(s_OriginalFileName, s_SavePathFileName){
	var s_SaveFileName = s_SavePathFileName.substr(s_SavePathFileName.lastIndexOf("/")+1);
	doInterfaceUpload(EWEBParam.LinkOriginalFileName, s_OriginalFileName);
	doInterfaceUpload(EWEBParam.LinkSaveFileName, s_SaveFileName);
	doInterfaceUpload(EWEBParam.LinkSavePathFileName, s_SavePathFileName);
}

function doInterfaceUpload(s_LinkName, s_Value){
	if (s_Value==""){return;}

	if (s_LinkName){
		var o_LinkUpload = parent.document.getElementById(s_LinkName);
		if (!o_LinkUpload){
			o_LinkUpload = parent.document.getElementsByName(s_LinkName)[0];
		}
		if (o_LinkUpload){
			if (o_LinkUpload.value!=""){
				o_LinkUpload.value = o_LinkUpload.value + "|";
			}
			o_LinkUpload.value = o_LinkUpload.value + s_Value;
			
			try{
				o_LinkUpload.onchange();
			}catch(e){

			}
		}
	}
}

function _SplitTextField(o_LinkField, s_Html){
	o_LinkField.value = s_Html;
}

var _gsEventUploadAfter;
function remoteUpload(s_EventUploadAfter){
	if (config.AutoRemote!="1"){return;}
	if (EWEB.CurrMode=="TEXT"){return;}

	_gsEventUploadAfter = s_EventUploadAfter;
	var objField = document.getElementsByName("eWebEditor_UploadText")[0];
	_SplitTextField(objField, getHTML());

	showProcessingMsg(lang["MsgRemoteUploading"]);
	$("eWebEditor_UploadForm").submit();
}

function remoteUploadOK(){
	$("divProcessing").style.display = "none";
	if (EWEB.LinkField){
		if (_gsEventUploadAfter){
			eval("parent."+_gsEventUploadAfter);
		}
	}
}

function localUpload(){
	if (!_IsInIE()){return;}
	if (EWEB.CurrMode=="TEXT"){return;}
	if (!CheckActiveXInstall(true)){return;}

	showProcessingMsg(lang["MsgLocalUploading"]);

	var s_HTML = getHTML();
	eWebEditorActiveX.LocalUpload(s_HTML);
	window.setTimeout("LocalUploadStatus()", 100);
}

function LocalUploadStatus(){
	if (eWebEditorActiveX.Status!="ok"){
		window.setTimeout("LocalUploadStatus()", 100);
		return;
	}

	if (CheckActiveXError()){
		$("divProcessing").style.display = "none";
		return;
	}

	var s_OriginalFiles = eWebEditorActiveX.OriginalFiles;
	var s_SavedFiles = eWebEditorActiveX.SavedFiles;
	if (s_OriginalFiles){
		var a_Original = s_OriginalFiles.split("|");
		var a_Saved = s_SavedFiles.split("|");
		for (var i=0; i<a_Original.length; i++){
			if (a_Saved[i]){
				var s_OriginalFileName = a_Original[i];
				var s_SavePathFileName = a_Saved[i];
				addUploadFile(s_OriginalFileName, s_SavePathFileName);
			}
		}
	}

	var s_HTML = eWebEditorActiveX.Body;
	setHTML(s_HTML, true);

	eWebEditorActiveX = null;
	$("divProcessing").style.display = "none";
}

function showProcessingMsg(msg){
	$("msgProcessing").innerHTML = msg;
	$("divProcessing").style.top = (document.body.clientHeight-parseFloat($("divProcessing").style.height))/2;
	$("divProcessing").style.left = (document.body.clientWidth-parseFloat($("divProcessing").style.width))/2;
	$("divProcessing").style.display = "";
}



function _GetHtmlWithHeader(s_Html){
	var s_Header = '<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN"><html><head>';
	s_Header += '<link href="' + EWEB.RootPath + '/skin/' + config.Skin + '/editarea.css" type="text/css" rel="stylesheet">';

	switch(EWEB.CurrMode){
	case 'CODE':

		break;
	case 'TEXT':

		break;
	case 'EDIT':
	case 'VIEW':	
		if (config.AreaCssMode!='1'){
			s_Header += '<link href="' + EWEB.RootPath + '/skin/' + config.Skin + '/editarea.zh-cn.css" type="text/css" rel="stylesheet">';
		}
		s_Header += EWEB.ExtCSS;
		break;
	}

	s_Header += EWEB.BaseHref + '</head>';

	var s_Ret = s_Header;
	if (config.FixWidth){
		s_Ret += '<body class="ewebeditor__fixwidth">'
			+ '<div id="eWebEditor_FixWidth_DIV" style="width:'+config.FixWidth+';" >';
		if (!EWEBBrowser.IsIE && s_Html==''){
			s_Ret += '<br>';
		}
		s_Ret += s_Html + '</div></body>';
	}else{
		s_Ret += '<body>' + s_Html + '</body>';
	}

	return s_Ret;
}


function getCount(n_Type){
	var str=getText();
	str = str.replace(/\n/g,"");
	str = str.replace(/\r/g,"");

	var l=str.length;
	var n=0;
    for (var i=0;i<l;i++){
        if (str.charCodeAt(i)<0||str.charCodeAt(i)>255){
			if (n_Type!=0){
				n++;
				if (n_Type==3){
					n++;
				}
			}
		}else{
			if (n_Type!=1){
				n++;
			}
		}
    }
    return n;
}

function getText(){
	var s_Txt;
	if (EWEB.CurrMode=="TEXT"){
		s_Txt = EWEB.EditorTextarea.value;
	}else{
		$("eWebEditor_Temp_HTML").innerHTML = getHTML();
		if (EWEBBrowser.IsIE){
			s_Txt = $("eWebEditor_Temp_HTML").innerText;
		}else{
			s_Txt = _GetInnerTextGecko($("eWebEditor_Temp_HTML"));
		}
	}

	return s_Txt;
}





function LoadScript(url){
	document.write( '<scr' + 'ipt type="text/javascript" src="' + url + '" onerror="alert(\'Error loading \' + this.src);"><\/scr' + 'ipt>' );
}






function ShowEWEBEditorBody(){
//	if (!config.L){
//		document.write("<table style='width:100%;height:100%;border-collapse:collapse' borderColor='#999999' bgColor='#ffffff' border='1px'><tr><td align='center' style='font-size:9pt'>eWebEditor!<br><br>"+lang["ErrLicense"]+"</td></tr></table>");
//		return;
//	}
	

	var s_Body = "";

	s_Body += "<table id='eWebEditor_Layout' border='0' cellpadding='0' cellspacing='0' width='100%' height='100%' style='table-layout:fixed'>";

	s_Body += "<tr id='eWebEditor_ToolarPTR'><td>";
		s_Body += "<table id='eWebEditor_Toolbar' border=0 cellpadding=0 cellspacing=0 width='100%'>";
		s_Body += "<tr id='eWebEditor_ToolarTRText' style='display:none'><td>";
		s_Body += _GetToolbar( _GetTextToolbarArr(), "Text");
		s_Body += "</td></tr>";
		s_Body += "<tr id='eWebEditor_ToolarTREdit' style='display:'><td>";
		s_Body += _GetToolbar(config.Toolbars, "Edit");
		s_Body += "</td></tr>";
		s_Body += "</table>";
	s_Body += "</td></tr>";

	s_Body += "<tr><td id='eWebEditor_EditareaTD' height='100%'>";
	s_Body += "<input type='hidden' id='D_ContentEdit' value=''>";
	s_Body += "<input type='hidden' id='D_CurrMode' value=''>";
	s_Body += "<input type='hidden' id='D_ContentLoad' value=''>";
	s_Body += "<input type='hidden' id='D_ContentFlag' value='0'>";
	s_Body += "<input type='hidden' id='D_PaginationTitle' value=''>";
	s_Body += "<textarea id='eWebEditorTextarea' style='display:none;width:100%;height:100%;'></textarea>";
	s_Body += '<iframe id="eWebEditor" width="100%" height="100%" scrolling="yes" frameborder="0" src="'+EWEBTools.GetVoidUrl()+'"></iframe>';
	s_Body += "</td></tr>";

	if (config.StateFlag){
		s_Body += "<tr><td class='SB'>";
		s_Body += "	<table border='0' cellpadding='0' cellspacing='0' width='100%' class='SB'>";
		s_Body += "	<tr valign='middle'>";
		s_Body += "	<td>";
		s_Body += "		<table border='0' cellpadding='0' cellspacing='0' class='SB_Mode'>";
		s_Body += "		<tr>";
		s_Body += "		<td class='SB_Mode_Left'></td>";
		if (config.SBCode){
			s_Body += "		<td class=SB_Mode_BtnOff id=eWebEditor_CODE onclick=\"setMode('CODE')\" unselectable=on><table border=0 cellpadding=0 cellspacing=0><tr><td class=SB_Mode_Btn_Img>" + _GetBtnImgHTML("ModeCode") + "</td><td class=SB_Mode_Btn_Text>" + lang["StatusModeCode"] + "</td></tr></table></td>";
			s_Body += "		<td class=SB_Mode_Sep></td>";
		}
		if (config.SBEdit){
			s_Body += "		<td class=SB_Mode_BtnOff id=eWebEditor_EDIT onclick=\"setMode('EDIT')\" unselectable=on><table border=0 cellpadding=0 cellspacing=0><tr><td class=SB_Mode_Btn_Img>" + _GetBtnImgHTML("ModeEdit") + "</td><td class=SB_Mode_Btn_Text>" + lang["StatusModeEdit"] + "</td></tr></table></td>";
			s_Body += "		<td class=SB_Mode_Sep></td>";
		}
		if (config.SBText){
			s_Body += "		<td class=SB_Mode_BtnOff id=eWebEditor_TEXT onclick=\"setMode('TEXT')\" unselectable=on><table border=0 cellpadding=0 cellspacing=0><tr><td class=SB_Mode_Btn_Img>" + _GetBtnImgHTML("ModeText") + "</td><td class=SB_Mode_Btn_Text>" + lang["StatusModeText"] + "</td></tr></table></td>";
			s_Body += "		<td class=SB_Mode_Sep></td>";
		}
		if (config.SBView){
			s_Body += "		<td class=SB_Mode_BtnOff id=eWebEditor_VIEW onclick=\"setMode('VIEW')\" unselectable=on><table border=0 cellpadding=0 cellspacing=0><tr><td class=SB_Mode_Btn_Img>" + _GetBtnImgHTML("ModeView") + "</td><td class=SB_Mode_Btn_Text>" + lang["StatusModeView"] + "</td></tr></table></td>";
		}
		s_Body += "		</tr>";
		s_Body += "		</table>";
		s_Body += "	</td>";
		if (EWEBParam.FullScreen!="1" && config.SBSize){
			s_Body += "	<td align=right>";
			s_Body += "		<table border=0 cellpadding=0 cellspacing=0 class=SB_Size>";
			s_Body += "		<tr>";
			s_Body += "		<td class=SB_Size_Btn onclick='sizeChange(300)' title='"+lang["SizePlus"]+"'>"+_GetBtnImgHTML("SizePlus")+"</td>";
			s_Body += "		<td class=SB_Size_Sep></td>";
			s_Body += "		<td class=SB_Size_Btn onclick='sizeChange(-300)' title='"+lang["SizeMinus"]+"'>"+_GetBtnImgHTML("SizeMinus")+"</td>";
			s_Body += "		<td class=SB_Size_Right></td>";
			s_Body += "		</tr>";
			s_Body += "		</table>";
			s_Body += "	</td>";
		}
		s_Body += "	</tr>";
		s_Body += "	</Table>";
		s_Body += "</td></tr>";
	}
	s_Body += "</table>";

	s_Body += "<div id='eWebEditor_Temp_HTML' style='visibility:hidden;overflow:hidden;position:absolute;width:1px;height:1px'></div>";

	s_Body += "<div style='position:absolute;display:none'>";
	s_Body += "<form id='eWebEditor_UploadForm' action='" + config.ServerExt + "/upload." + config.ServerExt + "?action=remote&type=remote&style=" + EWEBParam.StyleName + "&language=zh-cn&cusdir=" + EWEBParam.CusDir + "&skey=" + EWEBParam.SKey + "' method='post' target='eWebEditor_UploadTarget'>";
	s_Body += "<input type='hidden' name='eWebEditor_UploadText'>";
	s_Body += "</form>";
	s_Body += '<iframe name="eWebEditor_UploadTarget" width=0 height=0 src="'+EWEBTools.GetVoidUrl()+'"></iframe>';
	s_Body += "</div>";

	s_Body += "<div id=divProcessing style='width:200px;height:30px;position:absolute;display:none'>";
	s_Body += "<table border=0 cellpadding=0 cellspacing=1 bgcolor='#000000' width='100%' height='100%'><tr><td bgcolor=#3A6EA5><marquee id='msgProcessing' align='middle' behavior='alternate' scrollamount='5' style='font-size:9pt;color:#ffffff'></marquee></td></tr></table>";
	s_Body += "</div>";
	
	s_Body += "<div id='div_TableResizeSepV' style='position:absolute;display:none;background-color:transparent;overflow:hidden;' onmousedown='EWEBTableResize.MD2(event)' onmousemove='EWEBTableResize.MM2(event)' onmouseup='EWEBTableResize.MU2(event)'></div>";
	s_Body += "<div id='div_TableResizeSepH' style='position:absolute;display:none;background-color:transparent;overflow:hidden;' onmousedown='EWEBTableResize.MD2(event)' onmousemove='EWEBTableResize.MM2(event)' onmouseup='EWEBTableResize.MU2(event)'></div>";
	document.write(s_Body);
}


function _GetToolbar(a_Toolbars, s_ID){
	var s_Ret = "<table border='0' cellpadding='0' cellspacing='0' width='100%' id='eWebEditor_Toolbar_"+s_ID+"' unselectable>";
	for (var i=0; i<a_Toolbars.length; i++){
		s_Ret += "<tr><td class='TB_Left'></td><td class='TB_Center'><table border='0' cellpadding='0' cellspacing='0'><tr>";
		var tb = a_Toolbars[i];
		for (var j=0; j<tb.length; j++){
			var s_Code = tb[j];



			var a_Button = Buttons[s_Code];

			if (s_Code=="TBSep"){
				s_Ret += "<td class='TB_Btn_Padding'><div class='TB_Sep'></div></td>";
			}else if (a_Button[3]==0){
				s_Ret += "<td class='TB_Btn_Padding'><div class='TB_Btn' name='TB_Name_"+s_Code+"' title=\"" + lang[s_Code] + "\" onclick=\"" + a_Button[1] + "\">";
				if (typeof(a_Button[0])=="number"){
					var s_Img = "skin/" + config.Skin + "/buttons.gif";
					var n_Top = 16-a_Button[0]*16;

					if (EWEBBrowser.IsIE){

						s_Ret += "<div class='TB_Btn_Image'><img src='"+s_Img+"' style='top:"+n_Top+"px' /></div>";
					
					}else{

						s_Ret += "<img class='TB_Btn_Image' src='sysimage/space.gif' style='background-position: 0px "+n_Top+"px;background-image: url("+s_Img+");' />";

					}
					
				}else{
					var s_Img = "skin/" + config.Skin + "/" + a_Button[0];
					s_Ret += "<img class='TB_Btn_Image' src='"+s_Img+"'>";
				}
				s_Ret += "</div></td>";
			}else if (a_Button[3]==1){
				var s_FixedWidth = "";
				var s_Options = "";
				switch(s_Code){
				case "FontName":
					s_FixedWidth=" style='width:115px'";
					for (var k=0; k<lang[s_Code+"Item"].length; k++){
						s_Options += "<option value='"+lang[s_Code+"Item"][k]+"'>"+lang[s_Code+"Item"][k]+"</option>";
					}
					break;
				case "FontSize":
					s_FixedWidth=" style='width:55px'";
					for (var k=0; k<lang[s_Code+"Item"].length; k++){
						s_Options += "<option value='"+lang[s_Code+"Item"][k][0]+"'>"+lang[s_Code+"Item"][k][1]+"</option>";
					}
					break;
				case "FormatBlock":
					s_FixedWidth=" style='width:90px'";
					for (var k=0; k<lang[s_Code+"Item"].length; k++){
						s_Options += "<option value='"+lang[s_Code+"Item"][k][0]+"'>"+lang[s_Code+"Item"][k][1]+"</option>";
					}
					break;
				case "ZoomSelect":
					s_FixedWidth=" style='width:55px'";
					for (var k=0; k<EWEBCommandZoom.Options.length; k++){
						s_Options += "<option value='"+EWEBCommandZoom.Options[k]+"'>"+EWEBCommandZoom.Options[k]+"%</option>";
					}

					break;
				}
				s_Ret += "<td class='TB_Btn_Padding'><select name='TB_Name_"+s_Code+"' onchange=\"" + a_Button[1] + "\" size=1 " + s_FixedWidth + "><option selected>" + lang[s_Code] + "</option>" + s_Options + "</select></td>";
			}

		}
		s_Ret += "</tr></table></td><td class='TB_Right'></td></tr>";
	}
	s_Ret += "</table>";
	return s_Ret;
}


function _GetTextToolbarArr(){
	var a = new Array();
	var b = false;
	a.push("TBHandle");
	if (config.TB2Mode=="1"){
		a.push("ModeCode");
		a.push("ModeEdit");
		a.push("ModeText");
		a.push("ModeView");
		b = true;
	}
	if (config.TB2Max=="1"){
		if (b){
			a.push("TBSep");
		}
		a.push("Maximize");
	}
	return [a];
}



function _GetBtnImgHTML(s_Code, s_Class){
	var a_Btn = Buttons[s_Code];
	var n_Top = 16-a_Btn[0]*16;
	var s_Img = "skin/" + config.Skin + "/buttons.gif";
	if (EWEBBrowser.IsIE){
		return "<div><img src='"+s_Img+"' style='top:"+n_Top+"px'></div>";
	}else{
		return "<img class='SB_Btn_Image' src='sysimage/space.gif' style='background-position: 0px "+n_Top+"px;background-image: url("+s_Img+");' />";
	}
}






var EWEBPagination = new Object();

EWEBPagination.Insert = function(){
	if (config.PaginationMode=="0"){
		return false;
	}

	EWEB.Focus();
	var el;
	if (EWEBSelection.GetType()=="Control"){
		el = EWEBSelection.GetSelectedElement();
	}else{
		el = EWEBSelection.GetParentElement();
	}
	
	el = this._FindTopElementByElement(el);

	if (!el){
		insertHTML("</P><P id=eWebEditor_Temp_P>");
		var p=EWEB.EditorDocument.getElementById("eWebEditor_Temp_P");
		p.removeAttribute("id");
		this._InsertPaginationElByEl(p, "beforeBegin");
	}else{
		this._InsertPaginationElByEl(el, "afterEnd");
	}
};

EWEBPagination._InsertPaginationElByEl = function(el, s_Pos){
	var b_CreateP=false;
	if (s_Pos=="afterEnd" && (!el.nextSibling)){
		b_CreateP=true;
	}
	var o_NewNode=EWEB.EditorDocument.createElement("img");
	o_NewNode.className="ewebeditor__pagination";
	o_NewNode.setAttribute("_ewebeditor_fake_tag", "pagination");
	o_NewNode.setAttribute("src", EWEB.RootPath+"/sysimage/space.gif");
	if (EWEBBrowser.IsIE){
		el.insertAdjacentElement(s_Pos, o_NewNode);
	}else{
		if (s_Pos=="beforeBegin"){
			el.parentNode.insertBefore(o_NewNode, el);
		}else{
			el.parentNode.insertBefore(o_NewNode, el.nextSibling);
		}
	}
	
	if (b_CreateP){
		var p=EWEB.EditorDocument.createElement("p");
		el.parentElement.appendChild(p);
	}
};


EWEBPagination._FindTopElementByElement = function(el){
	if (el.tagName=="HTML"){
		return null;
	}
	var te = null;
	
	if (config.FixWidth){
		while (!((el.tagName.toUpperCase()=="DIV") && (el.getAttribute("id")=="eWebEditor_FixWidth_DIV"))){
			te = el;
			el = el.parentNode;
			if (!el || !el.tagName){
				break;
			}
		}
	}else{
		while (el.tagName.toUpperCase() != "BODY"){
			te = el;
			el = el.parentNode;
			if (!el || !el.tagName){
				break;
			}
		}
	}

	return te;
};




EWEBPagination.Auto = function(s_Num){
	if (config.PaginationMode=="0"){
		return false;
	}

	this.Empty();

	var n_Num=parseInt(s_Num);
	if (n_Num<1){
		return false;
	}
	if (getCount(2)<=n_Num){
		return false;
	}

	if (EWEB.CurrMode!="EDIT"){
		setMode("EDIT");
	}
	
	var o_Body;
	if (config.FixWidth){
		o_Body = EWEB.EditorDocument.getElementById("eWebEditor_FixWidth_DIV");
	}else{
		o_Body = EWEB.EditorDocument.body;
	}

	var o_Nodes = o_Body.childNodes;
	var l=0;
	for (var i=0; i<o_Nodes.length; i++){
		var o_Node=o_Nodes[i];
		if (o_Node.nodeType==1){

			var s=o_Node.innerText || o_Node.textContent;
			if (s){
				l+=s.length;
			}
		}else if (o_Node.nodeType==3){

			l+=o_Node.length;
		}

		if (l>=n_Num){
			if (o_Node.nextSibling){
				if (o_Node.nodeType==1){
					this._InsertPaginationElByEl(o_Node, "afterEnd");
				}else{
					this._InsertPaginationElByEl(o_Node.nextSibling, "beforeBegin");
				}
				l=0;
			}
		}	
	}
};

EWEBPagination.Empty = function(){
	if (config.PaginationMode=="0"){
		return;
	}

	var els=EWEB.EditorDocument.getElementsByTagName("IMG");
	for (var i=els.length-1; i>=0; i--){
		var el=els[i];
		var s_Attr = el.getAttribute("_ewebeditor_fake_tag",2);
		if (s_Attr){
			if (s_Attr.toLowerCase()=="pagination"){
				EWEBTools.RemoveNode(el);
			}
		}
	}
};

EWEBPagination.Fix = function(){
	if (config.PaginationMode=="0"){
		return false;
	}

	var els=EWEB.EditorDocument.getElementsByTagName("IMG");
	for (var i=els.length-1; i>=0; i--){
		var el=els[i];
		var s_Attr = el.getAttribute("_ewebeditor_fake_tag",2);
		if (s_Attr){		
			if (s_Attr.toLowerCase()=="pagination"){
				var te=this._FindTopElementByElement(el);
				if (te){
					var b=false;
					if (te.tagName=="DIV" || te.tagName=="P"){
						var s = te.innerText || te.textContent;
						s = s.Trim();
						if (!s){
							b=true;
							this._InsertPaginationElByEl(te, "beforeBegin");
							EWEBTools.RemoveNode(te);
						}
					}
					if (!b){
						this._InsertPaginationElByEl(te, "afterEnd");
						EWEBTools.RemoveNode(el);
					}
				}
			}
		}
	}
};










var EWEBFake = new Object();

EWEBFake.Normal2Fake = function(s_Html){	
	s_Html = this._PaginationCode2Img(s_Html);

	s_Html = this._Code2Img( s_Html, 'script', /<script[\s\S]*?<\/script>/gi );
	s_Html = this._Code2Img( s_Html, 'noscript', /<noscript[\s\S]*?<\/noscript>/gi );
	s_Html = this._Code2Img( s_Html, 'comment', /<!--[\s\S]*?-->/g );
	s_Html = this._ObjectCode2Img(s_Html);
	s_Html = this._EmbedCode2Img(s_Html);
	s_Html = this._ProtectEvents(s_Html);
	s_Html = this._ProtectUrl(s_Html, "img", "src");
	s_Html = this._ProtectUrl(s_Html, "a", "href");

	return s_Html;
};

EWEBFake.Fake2Normal = function(s_Html){
	s_Html = this._PaginationImg2Code(s_Html);
	s_Html = this._RestoreEvents(s_Html);
	s_Html = this._RestoreUrl(s_Html);
	s_Html = this._Img2Code(s_Html);
	s_Html = this._RestoreTempClass(s_Html);

	return s_Html;
};


EWEBFake.GetTag = function(){
	var el = EWEBSelection.GetSelectedElement();
	return el.getAttribute("_ewebeditor_fake_tag", 2);
};


EWEBFake._Code2Img = function(s_Html, s_Tag, o_Reg){
	function _Replace(m){
		return EWEBFake._GetImgHtml(s_Tag, m);
	};

	return s_Html.replace(o_Reg, _Replace);
};

EWEBFake._Img2Code = function(s_Html){

	function _Replace(m, s_Tag){
		
		function _Replace1(m, s_Value){
			if (['flash','flv','mediaplayer6','mediaplayer7','realplayer','quicktime','unknownobject'].IndexOf(s_Tag)>=0){
				var s_Style = EWEBFake._GetFullStyleFromHtml(m, 'img');
				if (s_Style != ''){
					s_Style = ' style='+s_Style;
				}
				
				var s_Width = (EWEBFake._GetStyleValueFromHtml(m, 'img', 'width')=='') ? EWEBFake._GetAttValueFromHtml(m, 'img', 'width') : '';
				var s_Height = (EWEBFake._GetStyleValueFromHtml(m, 'img', 'height')=='') ? EWEBFake._GetAttValueFromHtml(m, 'img', 'height') : '';

				var s_Align = EWEBFake._GetAttValueFromHtml(m, 'img', 'align');
				var s_Vspace = EWEBFake._GetAttValueFromHtml(m, 'img', 'vspace');
				var s_Hspace = EWEBFake._GetAttValueFromHtml(m, 'img', 'hspace');
				var s_FakeHtml = decodeURIComponent(s_Value);

				s_FakeHtml = EWEBFake._SetFullStyleToHtml(s_FakeHtml, "object", s_Style);
				s_FakeHtml = EWEBFake._SetFullStyleToHtml(s_FakeHtml, "embed", s_Style);

				s_FakeHtml = EWEBFake._SetAttValueToHtml(s_FakeHtml, 
					["object","embed"], 
					[ ["width",s_Width], ["height", s_Height], ["align",s_Align], ["vspace",s_Vspace], ["hspace",s_Hspace] ]
					);
				return s_FakeHtml;

			}else{
				return decodeURIComponent(s_Value);
			}
		};
	
		return m.replace(/<img [^>]*?_ewebeditor_fake_value=\"([^\">]+?)\"[^>]*?>/gi, _Replace1);
	};

	return s_Html.replace(/<img [^>]*?_ewebeditor_fake_tag=\"(\w+?)\"[^>]*?>/gi, _Replace);

};


EWEBFake._PaginationCode2Img = function(s_Html){
	if (config.PaginationMode=="0"){
		return s_Html;
	}

	var s_Ret = '';
	var s_Title = '';
	var s_Img = EWEBFake._GetImgHtml('pagination', "");
	
	if (config.PaginationMode=="1"){
		s_Ret = "";
		var re = /<!--ewebeditor:page title=\"([^\">]*)\"-->((?:[^a]|a)+?)<!--\/ewebeditor:page-->/gi;
		var m;
		var n_PageCount = 0;
		while ((m = re.exec(s_Html)) != null) {
			n_PageCount++;
			s_Title += HTMLDecode(m[1]) + "\r\n";
			
			if (s_Ret!=""){
				s_Ret+=s_Img;
			}
			s_Ret += m[2];
		}

		if (n_PageCount==0){
			s_Ret = s_Html;
		}

	}else{
		var re = new RegExp(config.PaginationKey.replace(/([\[\]\{\}\.\(\)\*\+\?])/gi, "\\$1"),'gi');
		s_Ret=s_Html.replace(re, s_Img);
	}

	$("D_PaginationTitle").value=s_Title;

	return s_Ret;
};

EWEBFake._PaginationImg2Code = function(s_Html){
	if (config.PaginationMode=="0"){
		return s_Html;
	}
	var s_Ret=s_Html;


	var a = s_Html.split(/<img [^>]*?_ewebeditor_fake_tag=\"pagination\"[^>]*?>/gi);
	if (a.length>1){
		if (config.PaginationMode=="1"){
			s_Ret = "";
			var a_Title = $("D_PaginationTitle").value.split("\r\n");
			for (var i=0; i<a.length; i++){
				var s_Title = "";
				if (a_Title[i]){
					s_Title=HTMLEncode(a_Title[i]);
				}
				s_Ret += "<!--ewebeditor:page title=\""+s_Title+"\"-->\r\n";
				s_Ret += a[i] + "\r\n";
				s_Ret += "<!--/ewebeditor:page-->\r\n\r\n";
			}
		}else{
			s_Ret = a[0];
			for (var i=1; i<a.length; i++){
				s_Ret+="\r\n"+config.PaginationKey+"\r\n"+a[i];
			}
		}
	}
	return s_Ret;
};

EWEBFake._ObjectCode2Img = function(s_Html){

	function _Replace(m){
		var s_ClsID = m.replace(/<object [^>]*?classid\s*=\s*[\'\"]?clsid\s*:\s*([a-z0-9\-]+)[\'\"]?[^>]*?>[\s\S]*/gi, '$1');
		s_ClsID = s_ClsID.toUpperCase();

		if (s_ClsID=='D27CDB6E-AE6D-11CF-96B8-444553540000'){



			if (/plugin\/flvplayer\.swf/.test(m) && /flashvars[^>]*?\.flv/.test(m)){
				return EWEBFake._GetImgHtml('flv', m, 'object');
			}else{

				return EWEBFake._GetImgHtml('flash', m, 'object');
			}


		}else if (s_ClsID=='22D6F312-B0F6-11D0-94AB-0080C74C7E95'){

			return EWEBFake._GetImgHtml('mediaplayer6', m, 'object');
		}else if (s_ClsID=='6BF52A52-394A-11D3-B153-00C04F79FAA6'){

			return EWEBFake._GetImgHtml('mediaplayer7', m, 'object');
		}else if (s_ClsID=='CFCDAA03-8BE4-11CF-B84B-0020AFBBCCFA'){

			return EWEBFake._GetImgHtml('realplayer', m, 'object');
		}else if (s_ClsID=='02BF25D5-8C17-4B23-BC80-D3488ABDDC6B'){

			return EWEBFake._GetImgHtml('quicktime', m, 'object');
		}else{
			return EWEBFake._GetImgHtml('unknownobject', m, 'object');
		}
	};


	return s_Html.replace(/<object[\s\S]*?<\/object>/gi, _Replace);
};


EWEBFake._EmbedCode2Img = function(s_Html){
	
	function _Replace(m){
		var s_Type = m.replace(/<embed [^>]*?type\s*=\s*[\'\"]?([^\'\"\s]+)[\'\"]?[^>]*?>[\s\S]*/gi, '$1');
		s_Type = s_Type.toLowerCase();

		if (s_Type=='application/x-shockwave-flash'){

			return EWEBFake._GetImgHtml('flash', m, 'embed');
		}else if (['application/x-mplayer2', 'video/x-ms-asf', 'video/x-msvideo', 'video/mpeg', 'audio/mid', 'audio/mpeg', 'audio/wav', 'video/x-ms-wm', 'audio/x-ms-wma', 'video/x-ms-wmv', 'video/x-ms-wmp', 'video/x-ms-wmx'].IndexOf(s_Type)>=0){


			return EWEBFake._GetImgHtml('mediaplayer6', m, 'embed');
		}else if (s_Type=='video/quicktime'){


			return EWEBFake._GetImgHtml('quicktime', m, 'embed');
		}else if (['audio/x-pn-realaudio', 'audio/x-pn-realaudio-plugin', 'application/vnd.rn-realmedia'].IndexOf(s_Type)>=0){

			return EWEBFake._GetImgHtml('realplayer', m, 'embed');
		}else{
			return EWEBFake._GetImgHtml('unknownobject', m, 'embed');
		}
	};

	return s_Html.replace(/<embed[\s\S]*?<\/embed>/gi, _Replace);
};


EWEBFake._GetImgHtml = function(s_Tag, s_Value, s_AttTag){
	if (s_AttTag){
		var s_Style = EWEBFake._GetFullStyleFromHtml(s_Value, s_AttTag);
		var s_Width = EWEBFake._GetAttValueFromHtml(s_Value, s_AttTag, 'width');
		var s_Height = EWEBFake._GetAttValueFromHtml(s_Value, s_AttTag, 'height');
		var s_Align = EWEBFake._GetAttValueFromHtml(s_Value, s_AttTag, 'align');
		var s_Vspace = EWEBFake._GetAttValueFromHtml(s_Value, s_AttTag, 'vspace');
		var s_Hspace = EWEBFake._GetAttValueFromHtml(s_Value, s_AttTag, 'hspace');

		if (s_Style != ''){
			s_Style = ' style='+s_Style;
		}

		var s_Html = '<img src="'+EWEB.RootPath+'/sysimage/space.gif" class="ewebeditor__'+s_Tag+'" _ewebeditor_fake_tag="'+s_Tag+'" _ewebeditor_fake_value="'+encodeURIComponent(s_Value)+'"'+s_Style;
		if (s_Width!=''){
			s_Html += ' width="'+s_Width+'"';
		}
		if (s_Height!=''){
			s_Html += ' height="'+s_Height+'"';
		}
		if (s_Align!=''){
			s_Html += ' align="'+s_Align+'"';
		}
		if (s_Vspace!=''){
			s_Html += ' vspace="'+s_Vspace+'"';
		}
		if (s_Hspace!=''){
			s_Html += ' hspace="'+s_Hspace+'"';
		}
		s_Html += '>';

		return s_Html;
	}else{
		return '<img src="'+EWEB.RootPath+'/sysimage/space.gif" class="ewebeditor__'+s_Tag+'" _ewebeditor_fake_tag="'+s_Tag+'" _ewebeditor_fake_value="'+encodeURIComponent(s_Value)+'">';
	}
};

EWEBFake._GetFullStyleFromHtml = function(s_Html, s_Tag){
	var re = new RegExp('^[\\s\\S]*?<'+s_Tag+'(?=[\\s>])[^>]*?\\sstyle\\s*?=\\s*?(\'[^\'>]+?\'|\"[^\">]+?\")[^>]*?>[\\s\\S]*$','gi');
	if (re.test(s_Html)){
		return s_Html.replace(re, '$1');
	}else{
		return '';
	}
};

EWEBFake._GetStyleValueFromHtml = function(s_Html, s_Tag, s_AttName){
	var re = new RegExp('^[\\s\\S]*?<'+s_Tag+'(?=[\\s>])[^>]*?\\sstyle\\s*?=\\s*?([\'\"])[^>]*?\\b'+s_AttName+'\\s*?:\\s*?(\\w+)(?=[\\s\;\'\"])[^>]*?\\1[^>]*?>[\\s\\S]*$','gi');
	if (re.test(s_Html)){
		return s_Html.replace(re, '$2');
	}else{
		return '';
	}
};

EWEBFake._GetAttValueFromHtml = function(s_Html, s_Tag, s_AttName){
	var re = new RegExp('^[\\s\\S]*?<'+s_Tag+'(?=[\\s>])[^>]*?\\s'+s_AttName+'\\s*?=\\s*?([\'\"]?)(\\w+)\\1[^>]*?>[\\s\\S]*$','gi');
	if (re.test(s_Html)){
		return s_Html.replace(re, '$2');
	}else{
		return '';
	}
};


EWEBFake._SetFullStyleToHtml = function(s_Html, s_Tag, s_Style){

	function _Replace(m){
		var r = /\sstyle\s*?=\s*?([\'\"])[^>]*?\1/gi;
		if (r.test(m)){
			s_Style = s_Style.replace('$', '\\$');
			return m.replace(r, s_Style);
		}else{
			return m.substring(0,m.length-1)+s_Style+'>';
		}
	};

	var re = new RegExp('<'+s_Tag+'(?=[\\s>])[^>]*?>','gi');
	return s_Html.replace(re, _Replace);
};




EWEBFake._SetAttValueToHtml = function(s_Html, a_Tag, a_Att){
	for (var i=0; i<a_Tag.length; i++){
		s_Html = this._SetAttValueToHtmlByTag(s_Html, a_Tag[i], a_Att);
	}
	return s_Html;
};


EWEBFake._SetAttValueToHtmlByTag = function(s_Html, s_Tag, a_Att){
	
	function _Replace(m){
		var s_AttName, s_AttValue;
		
		for (var i=0; i<a_Att.length; i++){
			s_AttName = a_Att[i][0];
			s_AttValue = a_Att[i][1];
			var s = '';
			if (s_AttValue!=''){
				s = ' '+s_AttName+'="'+s_AttValue+'"';
			}
			var r = new RegExp('\\s'+s_AttName+'\\s*?=\\s*?([\'\"]?)\\w+\\1', 'gi');
			if (r.test(m)){
				m=m.replace(r, s);
			}else{
				if (s_AttValue!=''){
					m = m.substring(0,m.length-1)+s+'>';
				}
			}
		}

		return m;
	};

	
	var re = new RegExp('<'+s_Tag+'[^>]*?>','gi');
	return s_Html.replace(re, _Replace);
};


EWEBFake._ProtectEvents = function(s_Html){

	function _Replace(m){
		function _Replace2( m, s_AttName ){
			return ' _ewebeditor_pa_' + s_AttName + '="' + encodeURIComponent( m ) + '"' ;
		};

		return m.replace( /\s(on\w+)[\s\r\n]*=[\s\r\n]*?(\'|\")([\s\S]*?)\2/gi, _Replace2 ) ;
	};

	return s_Html.replace(/<[^\>]+ on\w+[\s\r\n]*=[\s\r\n]*?(\'|\")[\s\S]+?\>/gi, _Replace);
};

EWEBFake._RestoreEvents = function(s_Html){

	function _Replace(m, m1){
		return decodeURIComponent( m1 ) ;
	};

	return s_Html.replace(/\s_ewebeditor_pa_\w+=\"([^\"]+)\"/gi, _Replace);
};



EWEBFake._ProtectUrl = function(s_Html, s_Tag, s_AttName){
	
	function _Replace(m, m1, m2, m3, m4){
		var r = new RegExp('<'+s_Tag+'[\\s\\r\\n]','gi');
		if (!r.test(m) || /_ewebeditor_ta_/.test(m) || /_ewebeditor_fake_/.test(m)){
			return m;
		}else{
			return m1 + m2 + ' _ewebeditor_ta_'+s_AttName+'="' + encodeURIComponent(m3) + '"' + m4;
		}
	};

	var re = new RegExp('(<'+s_Tag+'[^>]*?)([\\s\\r\\n]'+s_AttName+'[\\s\\r\\n]*?=[\\s\\r\\n]*?[\'\"]([^>\'\"]*?)[\'\"])([\\s\\S]*?>)','gi');
	return s_Html.replace(re, _Replace );
};

EWEBFake._RestoreUrl = function(s_Html){

	function _Replace(m, s_AttName, s_Value){
		var r = new RegExp('[\\s\\r\\n]'+s_AttName+'[\\s\\r\\n]*?=[\\s\\r\\n]*?[\'\"][^>\'\"]*?[\'\"]', 'gi');
		var s = m.replace(r, '');
		r = new RegExp('\\s_ewebeditor_ta_'+s_AttName+'+\\s*?=\\s*?\"([^\"]*?)\"', 'gi');
		return s.replace(r, ' '+s_AttName+'="'+decodeURIComponent(s_Value)+'"');
	};

	return s_Html.replace(/<\w+[^>]*?_ewebeditor_ta_(\w+)\s*?=\s*?\"([^\">]*?)\"[^>]*>/gi, _Replace);
};

EWEBFake._RestoreTempClass = function(s_Html){
	s_Html = s_Html.replace(/(<\w+(?=\s)[^>]*?\sclass\s*?=[^>]*?)(ewebeditor__\w+)([^>]*?>)/gi, '$1$3');
	s_Html = s_Html.replace(/(<\w+(?=\s)[^>]*?)(\sclass\s*?=\s*?\"\s*\")([^>]*>)/gi, '$1$3');
	return s_Html;
};






var EWEBCodeFormat = {

	Format : function(s_Html){
		var s_FormatIndentator = this._GetIndent();
		if (s_FormatIndentator==''){
			return s_Html;
		}
		var s_SepStr = this._GetSepStr(s_Html)
		var a_ProtectedData = new Array();
		
		var r_DecreaseIndent = /^\<\/(HTML|HEAD|BODY|FORM|TABLE|TBODY|THEAD|TR|UL|OL|DL)[ \>]/i;
		var r_IncreaseIndent = /^\<(HTML|HEAD|BODY|FORM|TABLE|TBODY|THEAD|TR|UL|OL|DL)[ \/\>]/i;
		var r_FormatIndentatorRemove = new RegExp( '^'+s_FormatIndentator );

		var _Replace1 = function(m, m1, m2, m3, m4){
			a_ProtectedData.push(m3);
			return m1 + s_SepStr + m4 ;
		};


		var re = /(<(style|script|pre)(?=[\s>])[^>]*?>)([\s\S]*?)(<\/\2>)/gi;
		s_Html = s_Html.replace(re, _Replace1);


		s_Html = s_Html.replace( /\<(P|DIV|H1|H2|H3|H4|H5|H6|ADDRESS|PRE|OL|UL|LI|DL|DT|DD|TITLE|META|LINK|BASE|SCRIPT|LINK|TD|TH|AREA|OPTION)[^\>]*\>/gi, '\n$&' ) ;
		s_Html = s_Html.replace( /\<\/(P|DIV|H1|H2|H3|H4|H5|H6|ADDRESS|PRE|OL|UL|LI|DL|DT|DD|TITLE|META|LINK|BASE|SCRIPT|LINK|TD|TH|AREA|OPTION)[^\>]*\>/gi, '$&\n' ) ;
		s_Html = s_Html.replace( /\<(BR|HR)[^\>]*\>/gi, '$&\n' ) ;
		s_Html = s_Html.replace( /\<\/?(HTML|HEAD|BODY|FORM|TABLE|TBODY|THEAD|TR)[^\>]*\>/gi, '\n$&\n' ) ;

		var a_Lines = s_Html.split( /\s*\n+\s*/g ) ;

		var s_Indentation = '' ;
		s_Html = '' ;

		for ( var i = 0 ; i < a_Lines.length ; i++ ){
			var s_Line = a_Lines[i] ;

			if ( s_Line.length == 0 ){
				continue ;
			}
			
			if ( r_DecreaseIndent.test( s_Line ) ){
				s_Indentation = s_Indentation.replace( r_FormatIndentatorRemove , '' ) ;
			}

			s_Html += s_Indentation + s_Line + '\n' ;

			if ( r_IncreaseIndent.test( s_Line ) ){
				s_Indentation += s_FormatIndentator ;
			}
		}

		
		var _Replace2 = function(m, m1, m2){
			return m1.toLowerCase()+m2;
		};


		s_Html = s_Html.replace(/(<\/?\w+(?=[\s>]))([^>]*?>)/gi, _Replace2);



		var a_Html = s_Html.split(s_SepStr);
		s_Html = a_Html[0];
		for (var i=0; i<a_ProtectedData.length; i++){
			s_Html += a_ProtectedData[i] + a_Html[i+1];
		}

		return s_Html.Trim() ;
	},

	_GetSepStr : function(s_Html){
		var s_SepStr = '__ewebeditor__sepstr__';
		var i = 0;
		while(true){
			i = i + 1;
			s_SepStr = s_SepStr + i;
			if (s_Html.indexOf(s_SepStr)<0){
				break;
			}
		}
		return s_SepStr;
	},

	_GetIndent : function(){
		var n = parseInt(config.CodeFormat);
		var s_Indent = '';
		for (var i=0; i<n; i++){
			s_Indent += ' ';
		}
		return s_Indent;
	}


};






function sizeChange(n_Size){
	for (var i=0; i<parent.frames.length; i++){
		if (parent.frames[i].document==self.document){
			var o_Frame=parent.frames[i].frameElement;
			var n_Height = parseInt(o_Frame.offsetHeight);
			if (n_Height+n_Size>=300){
				o_Frame.height=n_Height+n_Size;
			}
			break;
		}
	}
}


function spellCheck(){
	if (!_IsInIE()){return;}

	try {
		var tmpis = new ActiveXObject("ieSpell.ieSpellExtension");
		tmpis.CheckAllLinkedDocuments(EWEB.EditorDocument);
	} catch(exception){
		if (confirm(lang["MsgIeSpellDownload"])){
			window.open("http://www.iespell.com/download.php","IeSpellDownload");
		}
	}
}


function findReplace(){
	showDialog('findreplace.htm', true);
}



function paragraphAttr(){
	if (!_IsModeEdit()){return;}
	EWEB.Focus();

	if (!_IsParagraphRelativeSelection()){
		alert(lang["MsgNotParagraph"]);
		return;
	}

	showDialog('paragraph.htm', true);
}


function _IsParagraphRelativeSelection(){
	if (EWEBSelection.GetType()=="Control"){return false;}

	if (EWEBBrowser.IsIE){
		var o_Selection = EWEB.EditorDocument.selection.createRange();
		var o_Body = EWEB.EditorDocument.body;
		var a_AllEl = o_Body.getElementsByTagName("P");
		var o_RngTemp = o_Body.createTextRange();

		for(var i=0;i<a_AllEl.length;i++){
			o_RngTemp.moveToElementText(a_AllEl[i]);
			if (o_Selection.inRange(o_RngTemp)){
				return true;
			}else{
				if ( ((o_Selection.compareEndPoints("StartToEnd",o_RngTemp)<0)&&(o_Selection.compareEndPoints("StartToStart",o_RngTemp)>0)) || ((o_Selection.compareEndPoints("EndToStart",o_RngTemp)>0)&&(o_Selection.compareEndPoints("EndToEnd",o_RngTemp)<0)) ){
					return true;
				}
			}
		}

	}else{
		var o_Selection = EWEB.EditorWindow.getSelection();
		var o_Body = EWEB.EditorDocument.body;
		var a_AllEl = o_Body.getElementsByTagName("P");

		for(var i=0;i<a_AllEl.length;i++){
			if (o_Selection.containsNode(a_AllEl[i], true)){
				return true;
			}
		}

	}

	return false;
}




function mapEdit(){
	if (!_IsModeEdit()){return;}

	EWEBHistory.Save();
	var b = false;
	if (EWEBSelection.GetType() == "Control"){
		var o_Control = EWEBSelection.GetSelectedElement();
		if (o_Control.tagName.toUpperCase() == "IMG"){
			b = true;
		}
	}
	if (!b){
		alert(lang["MsgMapLimit"]);
		return;
	}

	showDialog("map.htm", true);
}


function createLink(){
	if (!_IsModeEdit()){return;}

	if (EWEBSelection.GetType() == "Control"){
		var o_Control = EWEBSelection.GetSelectedElement();
		if (o_Control.tagName.toUpperCase() != "IMG"){
			alert(lang["MsgHylnkLimit"]);
			return;
		}
	}

	showDialog("hyperlink.htm", true);
}



function insert(s_CmdName){
	if (!_IsModeEdit()){return;}
	EWEB.Focus();
	EWEBHistory.Save();

	var s_Txt;
	if (EWEBBrowser.IsIE){
		var sel = EWEB.EditorDocument.selection.createRange();
		s_Txt = sel.text;
	}else{
		s_Txt = EWEB.EditorWindow.getSelection().toString();
	}
	

	switch(s_CmdName){
	case "nowdate":
		var d = new Date();
		insertHTML(d.toLocaleDateString());
		break;
	case "nowtime":
		var d = new Date();
		insertHTML(d.toLocaleTimeString());
		break;
	case "br":
		insertHTML("<br>");
		break;
	case "code":
		insertHTML('<table width=95% border="0" align="Center" cellpadding="6" cellspacing="0" style="border: 1px Dotted #CCCCCC; TABLE-LAYOUT: fixed"><tr><td bgcolor=#FDFDDF style="WORD-WRAP: break-word"><font style="color: #990000;font-weight:bold">'+lang["HtmlCode"]+'</font><br>'+HTMLEncode(s_Txt)+'</td></tr></table>');
		break;
	case "quote":
		insertHTML('<table width=95% border="0" align="Center" cellpadding="6" cellspacing="0" style="border: 1px Dotted #CCCCCC; TABLE-LAYOUT: fixed"><tr><td bgcolor=#F3F3F3 style="WORD-WRAP: break-word"><font style="color: #990000;font-weight:bold">'+lang["HtmlQuote"]+'</font><br>'+HTMLEncode(s_Txt)+'</td></tr></table>');
		break;
	case "big":
		insertHTML("<big>" + s_Txt + "</big>");
		break;
	case "small":
		insertHTML("<small>" + s_Txt + "</small>");
		break;
	case "printbreak":
		insertHTML("<div style=\"FONT-SIZE: 1px; PAGE-BREAK-BEFORE: always; VERTICAL-ALIGN: middle; HEIGHT: 1px; BACKGROUND-COLOR: #c0c0c0\">&nbsp; </div>");
		break;
	default:
		alert(lang["ErrParam"]);
		break;
	}
}



var EWEBCommandZoom = {
	Options : [10, 25, 50, 75, 100, 150, 200, 500],
	CurrScale : 100,
	
	Execute : function(n_Scale){
		if (EWEBBrowser.IsIE){
			EWEB.EditorDocument.body.runtimeStyle.zoom = n_Scale + "%";
		}else{



			EWEB.EditorDocument.body.style.MozTransform= 'scale('+(n_Scale/100)+')';
		}
		
		this.CurrScale = n_Scale;
	}
};



function absPosition(){
	if (EWEBSelection.GetType() != "Control"){return;}

	if (EWEBBrowser.IsIE){
		var o_Range = EWEB.EditorDocument.selection.createRange();
		for (var i=0; i<o_Range.length; i++){
			var o_Control = o_Range.item(i);
			if (o_Control.style.position != 'relative'){
				o_Control.style.position='relative';
			}else{
				o_Control.style.position='static';
			}
		}
	}else{
		var o_Control = EWEBSelection.GetSelectedElement();
		if (o_Control.style.position != 'relative'){
			o_Control.style.position='relative';
		}else{
			o_Control.style.position='static';
		}
	}
}



function setZIndex(s_Flag){
	if (EWEBSelection.GetType() != "Control"){return;}

	if (EWEBBrowser.IsIE){
		var o_Range = EWEB.EditorDocument.selection.createRange();
		for (var i=0; i<o_Range.length; i++){
			var o_Control = o_Range.item(i);
			if (s_Flag=='forward'){
				o_Control.style.zIndex  +=1;
			}else{
				o_Control.style.zIndex  -=1;
			}
			o_Control.style.position='relative';
		}
	}else{
		var o_Control = EWEBSelection.GetSelectedElement();
		if (s_Flag=='forward'){
			o_Control.style.zIndex  +=1;
		}else{
			o_Control.style.zIndex  -=1;
		}
		o_Control.style.position='relative';
	}
}



function formatText(what){
	EWEBSelection.Restore() ;
	EWEB.EditorWindow.focus() ;


	if (EWEBSelection.GetType()!="Text"){return;}
	EWEBHistory.Save();

	if (EWEBBrowser.IsIE){
		var sel = EWEB.EditorDocument.selection;
		var rng = sel.createRange();

		var r =  EWEB.EditorDocument.body.createTextRange();
		var n_Start = 0;
		while (r.compareEndPoints("StartToStart", rng)<0){
			r.moveStart("character",1);
			n_Start++;
		}
		var n_End = 0;
		while (r.compareEndPoints("EndToEnd", rng)>0){
			r.moveEnd("character",-1);
			n_End--;
		}

		var a = ["a","b","c","d","e","f","g","h","i","j","k","l","m","n","o","p","q","r","s","t","u","v","w","x","y","z"];
		var l, u, s_Search, s_Replace;
		for (var i=0; i<26; i++){
			l = a[i];
			u = a[i].toUpperCase();
			switch(what){
			case "uppercase":
				s_Search = l;
				s_Replace = u;
				break;
			case "lowercase":
				s_Search = u;
				s_Replace = l;
				break;
			}

			r = rng.duplicate();
			while(r.findText(s_Search, 0, 4)){
				r.text = s_Replace;
				r = rng.duplicate();
			}
		}

		r =  EWEB.EditorDocument.body.createTextRange();
		r.moveStart("character",n_Start);
		r.moveEnd("character",n_End);
		r.select();

	}else{

		_Gecko_FormatText._FormatText(what);

	}

	EWEBHistory.Save();
}




function formatFont(s_CmdName, s_CmdValue){
	EWEBSelection.Restore() ;
	EWEB.EditorWindow.focus() ;
	if (EWEBSelection.GetType()!="Text"){return;}
	EWEBHistory.Save();

	var s_TempKey = "eWebEditor_Temp_FontName";
	if (EWEBBrowser.IsIE){
		var r = EWEB.EditorDocument.selection.createRange();
		var bk = r.getBookmark();

		EWEB.EditorDocument.execCommand("fontname","",s_TempKey);
		var a_Font = EWEB.EditorDocument.body.getElementsByTagName("FONT");
		var arr = new Array();
		for (var i=0; i<a_Font.length; i++){
			var o_Font = a_Font[i];
			if (o_Font.getAttribute("face") == s_TempKey){
				arr[arr.length] = a_Font[i];
			}
		}

		for (var i=0; i<arr.length; i++){
			var o_Font = arr[i];
			_DelStyleInFont(o_Font, s_CmdName);
			_DelEmptyNodeInFont(o_Font);
			_SetStyleInFont(o_Font, s_CmdName, s_CmdValue);
			o_Font.removeAttribute("face");

			var o_Parent = o_Font.parentElement;
			if (o_Parent.tagName=="FONT"){
				_FontAttribute2Style(o_Parent);
			}
			if ((o_Parent.tagName=="FONT")||(o_Parent.tagName=="SPAN")){
				if (o_Parent.innerText==o_Font.innerText){
					o_Parent.style.cssText = o_Parent.style.cssText + ";" + o_Font.style.cssText;
					o_Parent.innerHTML = o_Font.innerHTML;
					continue;
				}
			}
		}

		r.moveToBookmark(bk);
		r.select();
	}else{
		if (["uppercase","lowercase","size"].IndexOf(s_CmdName)>=0){
			_Gecko_FormatText._FormatText(s_CmdName,s_CmdValue);
		}else{
			if (s_CmdName=="face"){s_CmdName = "fontName";}
			EWEB.EditorDocument.execCommand(s_CmdName,false,s_CmdValue);
		}

	}


}


function _DelStyleInFont(obj, s_CmdName){
	_SetFontStyleValue(obj, s_CmdName, "");
	var o_Children = obj.children;
	for (var j=0; j<o_Children.length; j++){
		_DelStyleInFont(o_Children[j], s_CmdName);
		if (o_Children[j].tagName=="FONT"){
			_FontAttribute2Style(o_Children[j]);
		}
	}
}

function _DelEmptyNodeInFont(obj){
	var o_Children = obj.children;
	for (var j=0; j<o_Children.length; j++){
		_DelEmptyNodeInFont(o_Children[j]);
		if ((o_Children[j].tagName=="FONT") || (o_Children[j].tagName=="SPAN")){
			if ((o_Children[j].style.cssText=="" && o_Children[j].className=="")||(o_Children[j].innerHTML=="")){

				o_Children[j].removeNode(false);
				_DelEmptyNodeInFont(obj);
				return;
			}
		}
	}
}

function _SetStyleInFont(obj, s_CmdName, v){
	_SetFontStyleValue(obj, s_CmdName, v);
	var o_Children = obj.children;
	for (var j=0; j<o_Children.length; j++){
		if ((o_Children[j].tagName=="SPAN")||(o_Children[j].tagName=="FONT")){
			_SetStyleInFont(o_Children[j], s_CmdName, v);
		}
	}
}

function _SetFontStyleValue(obj, s_CmdName, v){
	try{
		switch(s_CmdName){
		case "face":
			obj.style.fontFamily = v;
			break;
		case "size":
			obj.style.fontSize = v;
			break;
		case "color":
			obj.style.color = v;
			break;
		default:
			break;
		}
	}catch(e){}
}


function _FontAttribute2Style(el){
	if (el.style.fontFamily==""){
		var s = el.getAttribute("face");
		if (s){
			el.style.fontFamily = s;
		}
	}
	el.removeAttribute("face");

	if (el.style.fontSize==""){
		var s = el.getAttribute("size");
		s=_Fontsize2Css(s);
		if (s){
			el.style.fontSize = s;
		}
	}
	el.removeAttribute("size");

	if (el.style.color==""){
		var s = el.getAttribute("color");
		if (s){
			el.style.color = s;
		}
	}
	el.removeAttribute("color");
}




var _Gecko_FormatText = (function(){
	var _Stop, _StartFound, _Cmd, _CmdValue;
	var _Nodes = [];
	var _StartContainer, _EndContainer, _StartOffset, _EndOffset, _IsOnlyTextChange, _SelectAll, _SameNode;

	return {

		_FormatText : function(s_Cmd, s_CmdValue){
			if (EWEBSelection.GetType()!="Text"){return;}

			_StartFound = false;
			_Stop = false;
			_Nodes.length = 0;
			_Cmd = s_Cmd;
			_CmdValue = s_CmdValue;
			_IsOnlyTextChange = (_Cmd=="uppercase" || _Cmd=="lowercase") ? true : false;
			_SelectAll = false;
			_SameNode = false;
			
			var sel = EWEB.EditorWindow.getSelection(); 
			var rng = sel.getRangeAt(0); 
			_StartContainer = rng.startContainer;
			_EndContainer = rng.endContainer;
			_StartOffset = rng.startOffset;
			_EndOffset = rng.endOffset;



			if(_StartContainer.isSameNode(_EndContainer)){ 
				if (_StartContainer.nodeType==1 && _StartContainer.tagName=="BODY"){
					_SelectAll = true;
					_EndContainer = this._GetLastTextNode(_StartContainer);
					_StartContainer = this._GetFirstTextNode(_StartContainer);
					if (!_StartContainer || !_EndContainer){
						return;
					}
					_StartOffset = 0;
					_EndOffset = _EndContainer.nodeValue.length;

					this._GoThroughElements(rng.startContainer);
				}else{
					_SameNode = true;
					var s_NodeValue = _StartContainer.nodeValue; 
					if (_IsOnlyTextChange){
						_StartContainer.nodeValue = s_NodeValue.substring(0, _StartOffset) + this._GetFormatValue(s_NodeValue.substring(_StartOffset, _EndOffset)) + s_NodeValue.substring(_EndOffset); 
					}else{
						EWEB.EditorDocument.execCommand("inserthtml", false, this._GetFormatValue(s_NodeValue.substring(_StartOffset, _EndOffset)) );
					}
				}
			}else{
				this._GoThroughElements(rng.commonAncestorContainer);
			} 

			if (_SelectAll){
				EWEB.EditorDocument.execCommand("SelectAll",false,null);
			}else{
				if (_IsOnlyTextChange){
					rng.setStart(_StartContainer, _StartOffset);
					rng.setEnd(_EndContainer, _EndOffset);
				}else{
					if (_SameNode){
						var o_SpanNode = EWEB.EditorDocument.getElementById("eWebEditor_Temp_Span_FontSize");
						if (o_SpanNode){
							o_SpanNode.removeAttribute("id");
							rng.selectNodeContents(o_SpanNode.firstChild);
						}
					}else{
						rng.setStart(_StartContainer, _StartContainer.nodeValue.length);
						rng.setEnd(_EndContainer, 0);
					}
				}
			}
			
			sel.removeAllRanges() ;
			sel.addRange( rng ) ;

			EWEB.Focus();

		},

		_GoThroughElements : function(el){
			if(el.isSameNode(_StartContainer)) {_StartFound = true; }
		 
			if(_StartFound){
				if(el.isSameNode(_StartContainer)){ 
					if (_IsOnlyTextChange){
						el.nodeValue = el.nodeValue.substring(0, _StartOffset) + this._GetFormatValue(el.nodeValue.substring(_StartOffset)); 
					}else{
						_Nodes[_Nodes.length] = el;
					}
				}else if(el.isSameNode(_EndContainer)){ 
					if (_IsOnlyTextChange){
						el.nodeValue = this._GetFormatValue(el.nodeValue.substring(0,_EndOffset)) + el.nodeValue.substring(_EndOffset); 
					}else{
						_Nodes[_Nodes.length] = el;

						for (var i=0; i<_Nodes.length; i++){
							if (_Nodes[i].nodeValue==""){
								continue;
							}
							if (i==0){
								var o_ParentNode = _Nodes[i].parentNode;
								if ((o_ParentNode.tagName.toUpperCase()=="SPAN") && (_Nodes[i].nodeValue.substring(_StartOffset)==o_ParentNode.innerHTML)){
									this._SetFormatValue(o_ParentNode);
								}else{
									var o_NewNode = this._CreateNewSpanNode(_Nodes[i].nodeValue.substring(_StartOffset));
									o_ParentNode.insertBefore(o_NewNode, _Nodes[i].nextSibling);
									_Nodes[i].nodeValue = _Nodes[i].nodeValue.substring(0, _StartOffset);
								}

							}else if (i==_Nodes.length-1){
								var o_ParentNode = _Nodes[i].parentNode;
								if ((o_ParentNode.tagName.toUpperCase()=="SPAN") && (_Nodes[i].nodeValue.substring(0,_EndOffset)==o_ParentNode.innerHTML)){
									this._SetFormatValue(o_ParentNode);
								}else{
									var o_NewNode = this._CreateNewSpanNode(_Nodes[i].nodeValue.substring(0,_EndOffset));
									o_ParentNode.insertBefore(o_NewNode, _Nodes[i]);
									_Nodes[i].nodeValue = _Nodes[i].nodeValue.substring(_EndOffset);
								}

							}else{
								var o_ParentNode = _Nodes[i].parentNode;
								if ((o_ParentNode.tagName.toUpperCase()=="SPAN") && (_Nodes[i].nodeValue==o_ParentNode.innerHTML)){
									this._SetFormatValue(o_ParentNode);
								}else{
									var o_NewNode = this._CreateNewSpanNode(_Nodes[i].nodeValue);
									o_ParentNode.replaceChild(o_NewNode, _Nodes[i]);
								}
							}
						}
					}



					_Stop = true; 

				}else if(el.nodeType == 3){ 
					if (_IsOnlyTextChange){
						el.nodeValue = this._GetFormatValue(el.nodeValue);
					}else{
						_Nodes[_Nodes.length] = el;
					}
				} 
			} 

			if (el.hasChildNodes()){
				for (var i=0; i<el.childNodes.length; i++){
					if (_Stop){return;}

					this._GoThroughElements(el.childNodes[i]); 
				}
			}
		},

		_GetFormatValue : function(str){
			switch(_Cmd){
			case "uppercase":
				return str.toUpperCase();
				break;
			case "lowercase":
				return str.toLowerCase();
				break;
			case "size":
				return "<span id=\"eWebEditor_Temp_Span_FontSize\" style=\"font-size:"+_CmdValue+"\">"+str+"</span>";
				break;
			}
		},

		_CreateNewSpanNode : function(s_Txt){
			var o_NewSpanNode = EWEB.EditorDocument.createElement("span");
			var o_NewTextNode = EWEB.EditorDocument.createTextNode(s_Txt);
			o_NewSpanNode.appendChild(o_NewTextNode);

			this._SetFormatValue(o_NewSpanNode);
			return o_NewSpanNode;
		},

		_SetFormatValue : function(el){
			switch(_Cmd){
			case "size":
				el.style.fontSize = _CmdValue;
				break;
			case "face":
				el.style.fontName = _CmdValue;
				break;
			}
		},

		_GetFirstTextNode : function(el){
			if (el.nodeType==3){
				return el;
			}
			if (el.hasChildNodes()){
				for (var i=0; i<el.childNodes.length; i++){
					var o_Node = this._GetFirstTextNode(el.childNodes[i]);
					if (o_Node){
						return o_Node;
					} 
				}
			}
			return null;
		},

		_GetLastTextNode : function(el){
			var o_Node;
			if (el.nodeType==3){
				o_Node=el;
			}
			if (el.hasChildNodes()){
				for (var i=0; i<el.childNodes.length; i++){
					var o_Node2 = this._GetLastTextNode(el.childNodes[i]);
					if (o_Node2){
						o_Node = o_Node2;
					} 
				}
			}
			
			if (o_Node){
				return o_Node;
			}else{
				return null;
			}
		}



	};

})();






var EWEBCommandJustify = {

	Execute : function(s_CmdName){
		var b = false;
		if (s_CmdName=="justifyleft" || s_CmdName=="justifycenter" || s_CmdName=="justifyright"){
			if (EWEBSelection.GetType()=="Control"){
				var o_Control = EWEBSelection.GetSelectedElement();
				try{
					if (s_CmdName=="justifycenter"){
						o_Control.align = "";
						o_Control.style.display="block";
						o_Control.style.margin="0px auto";
						o_Control.style.textAlign=s;
					}else{
						o_Control.align = s_CmdName.substr(7);
						o_Control.style.display="";
						o_Control.style.margin="";
						o_Control.style.textAlign="";
					}
					b = true;
				}catch(e){
				}
			}
		}

		if (!b){
			var p = this._GetSelectionParentElementByTags(["P", "DIV", "TD", "TH"]);
			if (p){
				if (s_CmdName=="justifyfull"){
					p.style.textAlign = "";
					p.style.textJustify = "inter-ideograph";
				}else{
					p.style.textAlign = s_CmdName.substr(7);
					p.style.textJustify = "";
				}
			}else{
				EWEB.EditorDocument.execCommand(s_CmdName,false,null);
			}
		
		}
	},


	_GetSelectionParentElementByTags : function(a_Tag){
		var el = null;
		if (EWEBSelection.GetType() != "Control"){
			el = EWEBSelection.GetParentElement();
			if (el.tagName=="HTML"){
				return null;
			}
			while (a_Tag.IndexOf(el.tagName.toUpperCase())<0){
				if (el.tagName.toUpperCase()=="BODY"){
					el=null;
					break;
				}
				el = el.parentNode;
			}
		}
		return el;
	}

};



var EWEBCommandCopyCut = new Object();

EWEBCommandCopyCut.Execute = function(s_CmdName){
	var b_Enabled = false ;

	if ( EWEBBrowser.IsIE ){

		var onEvent = function(){
			b_Enabled = true ;
		} ;

		var s_EventName = 'on' + s_CmdName.toLowerCase() ;

		EWEB.EditorDocument.body.attachEvent( s_EventName, onEvent ) ;
		EWEB.EditorDocument.execCommand(s_CmdName, false, null);
		EWEB.EditorDocument.body.detachEvent( s_EventName, onEvent ) ;
	}else{
		try{
			EWEB.EditorDocument.execCommand(s_CmdName, false, null);
			b_Enabled = true ;
		}catch(e){}
	}

	if ( !b_Enabled ){
		alert( lang[ 'MsgSafe' + s_CmdName ] ) ;
	}
};






var EWEBCommandMaximize = new Object();

EWEBCommandMaximize.Execute = function(){

	var o_Frame		= window.frameElement ;

	var o_ParentDocEl		= parent.document.documentElement ;
	var o_ParentBodyStyle	= parent.document.body.style ;
	var o_Parent ;


	if ( ! this.IsMaximized ){
		if( EWEBBrowser.IsIE ){
			parent.attachEvent( 'onresize', _EWEBCommandMaximize_Resize ) ;
		}else{
			parent.addEventListener( 'resize', _EWEBCommandMaximize_Resize, true ) ;
		}

		this._ScrollPos = EWEBTools.GetScrollPosition( parent ) ;

		o_Parent = o_Frame ;
		while( (o_Parent = o_Parent.parentNode) ){
			if ( o_Parent.nodeType == 1 ){
				o_Parent._ewebSavedStyles = EWEBTools.SaveStyles( o_Parent ) ;
				o_Parent.style.zIndex = 9998 ;
			}
		}

		if ( EWEBBrowser.IsIE ){
			this._Overflow = o_ParentDocEl.style.overflow ;
			o_ParentDocEl.style.overflow	= 'hidden' ;
			o_ParentBodyStyle.overflow		= 'hidden' ;
		}else{
			o_ParentBodyStyle.overflow = 'hidden' ;
			o_ParentBodyStyle.width = '0px' ;
			o_ParentBodyStyle.height = '0px' ;
		}

		this._EditorFrameStyles = EWEBTools.SaveStyles( o_Frame ) ;
		var o_ViewPaneSize = EWEBTools.GetViewPaneSize( parent ) ;

		o_Frame.style.position	= "absolute";
		o_Frame.offsetLeft ;
		o_Frame.style.zIndex	= 9998;
		o_Frame.style.left		= "0px";
		o_Frame.style.top		= "0px";
		o_Frame.style.width		= o_ViewPaneSize.Width + "px";
		o_Frame.style.height	= o_ViewPaneSize.Height + "px";

		if ( !EWEBBrowser.IsIE ){
			o_Frame.style.borderRight = o_Frame.style.borderBottom = "9999px solid white" ;
			o_Frame.style.backgroundColor		= "white";
		}

		parent.scrollTo(0, 0);

		var editorPos = EWEBTools.GetWindowPosition( parent, o_Frame ) ;
		if ( editorPos.x != 0 ){
			o_Frame.style.left = ( -1 * editorPos.x ) + "px" ;
		}
		if ( editorPos.y != 0 ){
			o_Frame.style.top = ( -1 * editorPos.y ) + "px" ;
		}

		this.IsMaximized = true ;

	}else{

		if( EWEBBrowser.IsIE ){
			parent.detachEvent( "onresize", _EWEBCommandMaximize_Resize ) ;
		}else{
			parent.removeEventListener( "resize", _EWEBCommandMaximize_Resize, true ) ;
		}

		o_Parent = o_Frame ;
		while( (o_Parent = o_Parent.parentNode) ){
			if ( o_Parent._ewebSavedStyles ){
				EWEBTools.RestoreStyles( o_Parent, o_Parent._ewebSavedStyles ) ;
				o_Parent._ewebSavedStyles = null ;
			}
		}

		if ( EWEBBrowser.IsIE ){
			o_ParentDocEl.style.overflow = this._Overflow ;
		}

		EWEBTools.RestoreStyles( o_Frame, this._EditorFrameStyles ) ;
		parent.scrollTo( this._ScrollPos.X, this._ScrollPos.Y ) ;

		this.IsMaximized = false ;
	}

	this._RefreshState();

	if ( EWEB.CurrMode == "EDIT" && !EWEBBrowser.IsIE){
		EWEB.MakeEditable() ;
	}

	EWEB.Focus() ;

};


EWEBCommandMaximize._RefreshState = function(){
	var a_Btn = EWEBToolbar.GetBtns("other", "Maximize");
	for (var j=0; j<a_Btn.length; j++){
		var el=a_Btn[j];
		if (this.IsMaximized){
			el.QCV="on";
			el.className="TB_Btn_Down";
		}else{
			el.QCV=null;
			el.className="TB_Btn";
		}
	}
};

function _EWEBCommandMaximize_Resize(){
	var o_ViewPaneSize = EWEBTools.GetViewPaneSize( parent ) ;

	var o_Frame = window.frameElement ;
	o_Frame.style.width		= o_ViewPaneSize.Width + 'px' ;
	o_Frame.style.height	= o_ViewPaneSize.Height + 'px' ;
}




function doCapture(){
	if (!_IsInIE()){return;}
	if (!CheckActiveXInstall(true)){return;}
	eWebEditorActiveX.Capture("");
	window.setTimeout("CaptureStatus()", 100);
}

function CaptureStatus(){
	try{
		if (eWebEditorActiveX.Status!="ok"){
			window.setTimeout("CaptureStatus()", 100);
			return;
		}
	}catch(e){return}

	if (eWebEditorActiveX.Error=="cancel"){
		return;
	}
	if (CheckActiveXError()){
		return;
	}

	var s_HTML = eWebEditorActiveX.Body;
	insertHTML(s_HTML);

	eWebEditorActiveX = null;
}




function doPaste(){
	if (config.AutoDetectPaste=="2"){
		window.setTimeout("pasteText()", 10);
		return;
	}

	if (EWEBBrowser.IsIE){	
		
		if (config.AutoDetectPaste=="1"){
			if (!CheckActiveXInstall(true)){return false;}
			var s_Flag = eWebEditorActiveX.GetClipboardFlag();
			eWebEditorActiveX = null;
			var a_Flag = s_Flag.split("|") ;
			if (a_Flag[5]=="1"){
				window.setTimeout("pasteWord()", 10);
				return false;
			}else if (a_Flag[5]=="2"){
				window.setTimeout("pasteExcel()", 10);
				return false;
			}else if (a_Flag[0]=="1" || a_Flag[3]=="1"){
				window.setTimeout("PasteOption('"+s_Flag+"')", 10);
				return false;
			}
		}

		IEPasteHTML();

	}else{
		try{
			EWEB.EditorDocument.execCommand("paste", false, null);
		}catch(e){
			showDialog("pastegecko.htm");
		}
	}
}

function IEPasteHTML(){
	var s_Html = _IEGetClipboardHTML();
	insertHTML(s_Html);
}

function PasteOption(s_Flag){
	showDialog("pasteoption.htm?flag="+s_Flag, true);
}

function pasteWord(){
	showDialog("importword.htm?action=paste", true);
}

function pasteExcel(){
	showDialog("importexcel.htm?action=paste", true);
}

function pasteText(){
	EWEB.Focus();
	EWEBHistory.Save();

	if (EWEBBrowser.IsIE){
		var s_Html = HTMLEncode( clipboardData.getData("Text") );
		insertHTML(s_Html);
	}else{
		showDialog("pastegecko.htm?action=text");
	}
	EWEBHistory.Save();
	EWEB.Focus();
}


function _IEGetClipboardHTML(){
	var o_Div = $("eWebEditor_Temp_HTML");
	o_Div.innerHTML = "";

	var o_TextRange = document.body.createTextRange();
	o_TextRange.moveToElementText(o_Div);
	o_TextRange.execCommand("Paste");

	var s_Html = o_Div.innerHTML;
	o_Div.innerHTML = "";

	return s_Html;
}



var EWEBCommandShowBorders = {

	Execute : function(){
		this._InitStateFromConfig();
		this._IsOnState = !this._IsOnState;
		this._RefreshState();
	},

	RestoreState : function(){
		this._InitStateFromConfig();
		if (this._IsOnState){
			this._RefreshState();
		}
	},

	_InitStateFromConfig : function(){
		if (typeof(this._IsOnState)=="undefined"){
			this._IsOnState = (config.ShowBorder == "0") ? false : true;
		}
	},

	_RefreshState : function(){
		var els = EWEB.EditorDocument.getElementsByTagName("TABLE");
		for (var i=0; i<els.length; i++){
			var el = els[i];
			if (this._IsOnState){
				el.className += ' ewebeditor__showtableborders' ;
			}else{
				el.className = el.className.replace( /(^| )ewebeditor__showtableborders/gi, '' ) ;
			}
		}

		var a_Btn = EWEBToolbar.GetBtns("other", "ShowBorders");
		for (var j=0; j<a_Btn.length; j++){
			var el=a_Btn[j];
			if (this._IsOnState){
				el.QCV="on";
				el.className="TB_Btn_Down";
			}else{
				el.QCV=null;
				el.className="TB_Btn";
			}
		}
	}

};





var EWEBCommandShowBlocks = {

	Execute : function(){
		this._InitStateFromConfig();
		this._IsOnState = !this._IsOnState;
		this._RefreshState();
	},

	RestoreState : function(){
		this._InitStateFromConfig();
		if (this._IsOnState){
			this._RefreshState();
		}	
	},

	_InitStateFromConfig : function(){
		if (typeof(this._IsOnState)=="undefined"){
			this._IsOnState = (config.ShowBlock == "1") ? true : false;
		}
	},


	_RefreshState : function(){
		var o_Body = EWEB.EditorDocument.body ;
		if ( this._IsOnState ){
			o_Body.className += ' ewebeditor__showblocks' ;
		}else{
			o_Body.className = o_Body.className.replace( /(^| )ewebeditor__showblocks/gi, '' ) ;
		}

		var a_Btn = EWEBToolbar.GetBtns("other", "ShowBlocks");
		for (var j=0; j<a_Btn.length; j++){
			var el=a_Btn[j];
			if (this._IsOnState){
				el.QCV="on";
				el.className="TB_Btn_Down";
			}else{
				el.QCV=null;
				el.className="TB_Btn";
			}
		}
	}

};



function DoImport(s_Flag){
	if (!_IsInIE()){return;}
	switch(s_Flag){
	case "word":
		showDialog('importword.htm', true);
		break;
	case "excel":
		showDialog('importexcel.htm', true);
		break;
	}
}



var EWEBCommandForm = {

	Insert : function(s_Flag){
		switch(s_Flag){
		case "inputtext":
			insertHTML('<input type="text">');
			break;
		case "textarea":
			insertHTML('<textarea></textarea>');
			break;
		case "radio":
			insertHTML('<input type="radio">');
			break;
		case "checkbox":
			insertHTML('<input type="checkbox">');
			break;
		case "select":
			insertHTML('<select></select>');
			break;
		case "button":
			insertHTML('<input type="button">');
			break;
		}
	}

};




function DoPrint(){
	EWEB.EditorWindow.print();
}





var EWEBTools = new Object();

EWEBTools.IsStrictMode = function( document ){


	return ( "CSS1Compat" == ( document.compatMode || ( EWEBBrowser.IsSafari ? "CSS1Compat" : null ) ) ) ;
};


EWEBTools.ResetStyles = function( element ){
	element.style.cssText = "margin:0;" +
		"padding:0;" +
		"border:0;" +
		"background-color:transparent;" +
		"background-image:none;" ;
};



EWEBTools.GetElementDocument = function ( element ){
	return element.ownerDocument || element.document ;
};


EWEBTools.GetElementWindow = function( element ){
	return this.GetDocumentWindow( this.GetElementDocument( element ) ) ;
};

EWEBTools.GetDocumentWindow = function( document ){
	if ( EWEBBrowser.IsSafari && !document.parentWindow ){
		this.FixDocumentParentWindow( window.top ) ;
	}

	return document.parentWindow || document.defaultView ;
};

EWEBTools.FixDocumentParentWindow = function( targetWindow ){
	if ( targetWindow.document ){
		targetWindow.document.parentWindow = targetWindow ;
	}

	for ( var i = 0 ; i < targetWindow.frames.length ; i++ ){
		EWEBTools.FixDocumentParentWindow( targetWindow.frames[i] ) ;
	}
};

EWEBTools.GetTopWindow = function(){
	var topWindow = window.parent ;

	while ( topWindow.parent && topWindow.parent != topWindow ){
		try{
			if ( topWindow.parent.document.domain != document.domain ){
				break ;
			}
			if ( topWindow.parent.document.getElementsByTagName( "frameset" ).length > 0 ){
				break ;
			}
		}catch ( e ){
			break ;
		}
		topWindow = topWindow.parent ;
	}

	return topWindow;
};


EWEBTools.GetDocumentPosition = function( w, node ){
	var x = 0 ;
	var y = 0 ;
	var curNode = node ;
	var prevNode = null ;
	var curWindow = EWEBTools.GetElementWindow( curNode ) ;
	while ( curNode && !( curWindow == w && ( curNode == w.document.body || curNode == w.document.documentElement ) ) ){
		x += curNode.offsetLeft - curNode.scrollLeft ;
		y += curNode.offsetTop - curNode.scrollTop ;

		if ( ! EWEBBrowser.IsOpera ){
			var scrollNode = prevNode ;
			while ( scrollNode && scrollNode != curNode ){
				x -= scrollNode.scrollLeft ;
				y -= scrollNode.scrollTop ;
				scrollNode = scrollNode.parentNode ;
			}
		}

		prevNode = curNode ;
		if ( curNode.offsetParent ){
			curNode = curNode.offsetParent ;
		}else{
			if ( curWindow != w ){
				curNode = curWindow.frameElement ;
				prevNode = null ;
				if ( curNode ){
					curWindow = curNode.contentWindow.parent ;
				}
			}else{
				curNode = null ;
			}
		}
	}

	if ( EWEBTools.GetCurrentElementStyle( w.document.body, 'position') != 'static' || ( EWEBBrowser.IsIE && EWEBTools.GetPositionedAncestor( node ) == null ) ){
		x += w.document.body.offsetLeft ;
		y += w.document.body.offsetTop ;
	}

	return { "x" : x, "y" : y } ;
};

EWEBTools.GetWindowPosition = function( w, node ){
	var pos = this.GetDocumentPosition( w, node ) ;
	var scroll = EWEBTools.GetScrollPosition( w ) ;
	pos.x -= scroll.X ;
	pos.y -= scroll.Y ;
	return pos ;
};


EWEBTools.ScrollIntoView = function( element, alignTop ){
	var window = this.GetElementWindow( element ) ;
	var windowHeight = this.GetViewPaneSize( window ).Height ;

	var offset = windowHeight * -1 ;

	if ( alignTop === false ){
		offset += element.offsetHeight || 0 ;
		offset += parseInt( this.GetCurrentElementStyle( element, 'marginBottom' ) || 0, 10 ) || 0 ;
	}

	var elementPosition = this.GetDocumentPosition( window, element ) ;
	offset += elementPosition.y ;

	var currentScroll = this.GetScrollPosition( window ).Y ;
	if ( offset > 0 && ( offset > currentScroll || offset < currentScroll - windowHeight ) ){
		window.scrollTo( 0, offset ) ;
	}
};


EWEBTools.ProtectFormStyles = function( formNode ){
	if ( !formNode || formNode.nodeType != 1 || formNode.tagName.toLowerCase() != 'form' ){
		return [] ;
	}
	var hijackRecord = [] ;
	var hijackNames = [ 'style', 'className' ] ;
	for ( var i = 0 ; i < hijackNames.length ; i++ ){
		var name = hijackNames[i] ;
		if ( formNode.elements.namedItem( name ) ){
			var hijackNode = formNode.elements.namedItem( name ) ;
			hijackRecord.push( [ hijackNode, hijackNode.nextSibling ] ) ;
			formNode.removeChild( hijackNode ) ;
		}
	}
	return hijackRecord ;
};

EWEBTools.RestoreFormStyles = function( formNode, hijackRecord ){
	if ( !formNode || formNode.nodeType != 1 || formNode.tagName.toLowerCase() != 'form' ){
		return ;
	}
	if ( hijackRecord.length > 0 ){
		for ( var i = hijackRecord.length - 1 ; i >= 0 ; i-- ){
			var node = hijackRecord[i][0] ;
			var sibling = hijackRecord[i][1] ;
			if ( sibling ){
				formNode.insertBefore( node, sibling ) ;
			}else{
				formNode.appendChild( node ) ;
			}
		}
	}
};

EWEBTools.GetPositionedAncestor = function( element ){
	var currentElement = element ;

	while ( currentElement != EWEBTools.GetElementDocument( currentElement ).documentElement ){
		if ( this.GetCurrentElementStyle( currentElement, 'position' ) != 'static' ){
			return currentElement ;
		}

		if ( currentElement == EWEBTools.GetElementDocument( currentElement ).documentElement && currentWindow != w ){
			currentElement = currentWindow.frameElement ;
		}else{
			currentElement = currentElement.parentNode ;
		}
	}

	return null ;
};



EWEBTools.SetTimeout = function( func, milliseconds, thisObject, paramsArray, timerWindow ){
	return ( timerWindow || window ).setTimeout(
		function(){
			if ( paramsArray ){
				func.apply( thisObject, [].concat( paramsArray ) ) ;
			}else{
				func.apply( thisObject ) ;
			}
		},
		milliseconds ) ;
};

EWEBTools.RunFunction = function( func, thisObject, paramsArray, timerWindow ){
	if ( func ){
		this.SetTimeout( func, 0, thisObject, paramsArray, timerWindow ) ;
	}
};

EWEBTools.SetElementStyles = function( element, styleDict ){
	var style = element.style ;
	for ( var styleName in styleDict ){
		style[ styleName ] = styleDict[ styleName ] ;
	}
};

EWEBTools.RemoveNode = function( node, excludeChildren ){
	if ( excludeChildren ){
		var eChild ;
		while ( (eChild = node.firstChild) ){
			node.parentNode.insertBefore( node.removeChild( eChild ), node ) ;
		}
	}

	return node.parentNode.removeChild( node ) ;
};

EWEBTools.GetParentNodeByTag = function(o_Node, s_TagName){
	while (o_Node && o_Node.tagName && o_Node.tagName!="BODY"){
		if (o_Node.tagName.toUpperCase() == s_TagName ){
			return o_Node;
		}
		o_Node = o_Node.parentNode;
	}
	return null; 
};






var EWEBSelection = new Object();



EWEBTools.RegisterDollarFunction = function(targetWindow){
	targetWindow.$ = targetWindow.document.getElementById ;
};


EWEBTools.GetVoidUrl = function(){
	if ( EWEBBrowser.IsIE6 ){
		return "javascript: '';" ;
	}else{
		return "" ;
	}
};


EWEBTools.CancelEvent = function( e ){
	try{
		e.returnValue = false;
		e.cancelBubble = true;
		e.keyCode=0;
	}catch(er){}
	return false ;
};


EWEBTools.AddEventListener = function( sourceObject, eventName, listener ){
	sourceObject.attachEvent( "on" + eventName, listener ) ;
};


EWEBTools.RemoveEventListener = function( sourceObject, eventName, listener ){
	sourceObject.detachEvent( "on" + eventName, listener ) ;
};



EWEBTools.GetViewPaneSize = function( win ){
	var oSizeSource ;

	var oDoc = win.document.documentElement ;
	if ( oDoc && oDoc.clientWidth ){				// IE6 Strict Mode
		oSizeSource = oDoc ;
	}else{
		oSizeSource = win.document.body ;		// Other IEs
	}

	if ( oSizeSource ){
		return { Width : oSizeSource.clientWidth, Height : oSizeSource.clientHeight } ;
	}else{
		return { Width : 0, Height : 0 } ;
	}
};


EWEBTools.GetScrollPosition = function( relativeWindow ){
	var oDoc = relativeWindow.document ;

	var oPos = { X : oDoc.documentElement.scrollLeft, Y : oDoc.documentElement.scrollTop } ;

	if ( oPos.X > 0 || oPos.Y > 0 ){
		return oPos ;
	}

	return { X : oDoc.body.scrollLeft, Y : oDoc.body.scrollTop } ;
};


EWEBTools.SetOpacity = function( element, opacity ){
	opacity = Math.round( opacity * 100 ) ;
	element.style.filter = ( opacity > 100 ? "" : "progid:DXImageTransform.Microsoft.Alpha(opacity=" + opacity + ")" ) ;
};


EWEBTools.GetCurrentElementStyle = function( element, propertyName ){
	propertyName = propertyName.replace(/\-(\w)/g, function (strMatch, p1){
			return p1.toUpperCase();
		});

	return element.currentStyle[ propertyName ] ;
};

EWEBTools.DisableSelection = function( element ){
	element.unselectable = 'on' ;

	var e, i = 0 ;
	while ( (e = element.all[ i++ ]) ){
		if (e.getAttribute('eweb_donotdisableselect',2)=='true'){
			i += e.all.length;
			continue;
		}
		switch ( e.tagName ){
			case 'IFRAME' :
			case 'TEXTAREA' :
			case 'INPUT' :
			case 'SELECT' :
			
				break ;
			default :
				e.unselectable = 'on' ;
		}
	}
};


EWEBTools.SaveStyles = function( element ){
	var data = EWEBTools.ProtectFormStyles( element ) ;

	var oSavedStyles = new Object() ;

	if ( element.className.length > 0 ){
		oSavedStyles.Class = element.className ;
		element.className = '' ;
	}

	var sInlineStyle = element.style.cssText ;

	if ( sInlineStyle.length > 0 ){
		oSavedStyles.Inline = sInlineStyle ;
		element.style.cssText = '' ;
	}

	EWEBTools.RestoreFormStyles( element, data ) ;
	return oSavedStyles ;
};

EWEBTools.RestoreStyles = function( element, savedStyles ){
	var data = EWEBTools.ProtectFormStyles( element ) ;
	element.className		= savedStyles.Class || '' ;
	element.style.cssText	= savedStyles.Inline || '' ;
	EWEBTools.RestoreFormStyles( element, data ) ;
};











EWEBSelection.GetType = function(){
	try{
		var ieType = EWEBSelection.GetSelection().type ;
		if ( ieType == 'Control' || ieType == 'Text' ){
			return ieType ;
		}

		if ( this.GetSelection().createRange().parentElement ){
			return 'Text' ;
		}
	}catch(e){

	}

	return 'None' ;
};

EWEBSelection.GetSelectedElement = function(){
	if ( this.GetType() == 'Control' ){
		var oRange = this.GetSelection().createRange() ;

		if ( oRange && oRange.item ){
			return this.GetSelection().createRange().item(0) ;
		}
	}
	return null ;
};

EWEBSelection.GetParentElement = function(){
	switch ( this.GetType() ){
		case 'Control' :
			var el = EWEBSelection.GetSelectedElement() ;
			return el ? el.parentElement : null ;

		case 'None' :
			return null ;

		default :
			return this.GetSelection().createRange().parentElement() ;
	}
};

EWEBSelection.GetBoundaryParentElement = function( startBoundary ){
	switch ( this.GetType() ){
		case 'Control' :
			var el = EWEBSelection.GetSelectedElement() ;
			return el ? el.parentElement : null ;

		case 'None' :
			return null ;

		default :
			var doc = EWEB.EditorDocument ;

			var range = doc.selection.createRange() ;
			range.collapse( startBoundary !== false ) ;

			var el = range.parentElement() ;
			return EWEBTools.GetElementDocument( el ) == doc ? el : null ;
	}
};

EWEBSelection.SelectNode = function( node ){
	EWEB.Focus() ;
	this.GetSelection().empty() ;
	var oRange ;
	try{
		oRange = EWEB.EditorDocument.body.createControlRange() ;
		oRange.addElement( node ) ;
	}catch(e){
		oRange = EWEB.EditorDocument.body.createTextRange() ;
		oRange.moveToElementText( node ) ;
	}

	oRange.select() ;
};

EWEBSelection.Collapse = function( toStart ){
	EWEB.Focus() ;
	if ( this.GetType() == 'Text' ){
		var oRange = this.GetSelection().createRange() ;
		oRange.collapse( toStart == null || toStart === true ) ;
		oRange.select() ;
	}
};


EWEBSelection.HasAncestorNode = function( nodeTagName ){
	var oContainer ;

	if ( this.GetSelection().type == "Control" ){
		oContainer = this.GetSelectedElement() ;
	}else{
		var oRange  = this.GetSelection().createRange() ;
		oContainer = oRange.parentElement() ;
	}

	while ( oContainer ){
		if ( oContainer.nodeName.IEquals( nodeTagName ) ) return true ;
		oContainer = oContainer.parentNode ;
	}

	return false ;
};



EWEBSelection.MoveToAncestorNode = function( nodeTagName ){
	var oNode, oRange ;

	if ( ! EWEB.EditorDocument )
		return null ;

	if ( this.GetSelection().type == "Control" ){
		oRange = this.GetSelection().createRange() ;
		for ( i = 0 ; i < oRange.length ; i++ ){
			if (oRange(i).parentNode){
				oNode = oRange(i).parentNode ;
				break ;
			}
		}
	}else{
		oRange  = this.GetSelection().createRange() ;
		oNode = oRange.parentElement() ;
	}

	while ( oNode && !oNode.nodeName.Equals( nodeTagName ) ){
		oNode = oNode.parentNode ;
	}

	return oNode ;
};

EWEBSelection.Delete = function(){
	var oSel = this.GetSelection() ;

	if ( oSel.type.toLowerCase() != "none" ){
		oSel.clear() ;
	}

	return oSel ;
};

EWEBSelection.GetSelection = function(){
	this.Restore() ;
	return EWEB.EditorDocument.selection ;
};

EWEBSelection.Save = function( lock ){
	var editorDocument = EWEB.EditorDocument ;

	if ( !editorDocument ){
		return ;
	}

	if ( this.locked ){
		return ;
	}
	this.locked = !!lock ;

	var selection = editorDocument.selection ;
	var range ;

	if ( selection ){
		try {
			range = selection.createRange() ;
		}
		catch(e) {}


		if ( range ){
			if ( range.parentElement && EWEBTools.GetElementDocument( range.parentElement() ) != editorDocument ){
				range = null ;
			}else if ( range.item && EWEBTools.GetElementDocument( range.item(0) )!= editorDocument ){
				range = null ;
			}
		}
	}
	this.SelectionData = range ;
};

EWEBSelection._GetSelectionDocument = function( selection ){
	var range = selection.createRange() ;
	if ( !range ){
		return null;
	}else if ( range.item ){
		return EWEBTools.GetElementDocument( range.item( 0 ) ) ;
	}else{
		return EWEBTools.GetElementDocument( range.parentElement() ) ;
	}
};

EWEBSelection.Restore = function(){
	if ( this.SelectionData ){
		EWEB.IsSelectionChangeLocked = true ;

		try{

			if ( this._GetSelectionDocument( EWEB.EditorDocument.selection ) == EWEB.EditorDocument ){
				EWEB.IsSelectionChangeLocked = false ;
				return ;
			}

			this.SelectionData.select() ;

		}
		catch ( e ) {}

		EWEB.IsSelectionChangeLocked = false ;
	}
};

EWEBSelection.Release = function(){
	this.locked = false ;
	delete this.SelectionData ;
};
