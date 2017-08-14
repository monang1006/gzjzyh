function showDialog(url, width, height) {

	var param = "dialogWidth=" + width + "px;dialogHeight=" + height + "px;";
	if ($.browser.msie != true) {
		var top = (window.screen.availHeight - 20 - height) / 2;
		var left = (window.screen.availWidth - 10 - width) / 2;
		param = param + "dialogTop:" + top + "px;dialogLeft:" + left + "px;";
	}
	var mask0 = null;
	var mask1 = null;
	/*if (window.top.frames['perspective_toolbar']) {
		mask0 = window.top.frames['perspective_toolbar'].document
				.getElementById("mask_top");
		mask1 = window.top.frames['perspective_content'].document
				.getElementById("mask_content");
		mask0.style.visibility = 'visible';
		mask1.style.visibility = 'visible';
	}*/

	var ret = showModalDialog(url, "", param);
	/*if (mask0) {
		mask0.style.visibility = 'hidden';
		mask1.style.visibility = 'hidden';
	}*/
	return ret;
}
//处理键盘事件 禁止后退键（Backspace）密码或单行、多行文本框除外  
function banBackSpace(e){     
    var ev = e || window.event;//获取event对象     
    var obj = ev.target || ev.srcElement;//获取事件源     
      
    var t = obj.type || obj.getAttribute('type');//获取事件源类型    
      
    //获取作为判断条件的事件类型  
    var vReadOnly = obj.getAttribute('readonly');  
    var vEnabled = obj.getAttribute('enabled');  
    //处理null值情况  
    vReadOnly = (vReadOnly == null) ? false : vReadOnly;  
    vEnabled = (vEnabled == null) ? true : vEnabled;  
      
    //当敲Backspace键时，事件源类型为密码或单行、多行文本的，  
    //并且readonly属性为true或enabled属性为false的，则退格键失效  
    var flag1=(ev.keyCode == 8 && (t=="password" || t=="text" || t=="textarea")   
                && (vReadOnly==true || vEnabled!=true))?true:false;  
     
    //当敲Backspace键时，事件源类型非密码或单行、多行文本的，则退格键失效  
    var flag2=(ev.keyCode == 8 && t != "password" && t != "text" && t != "textarea")  
                ?true:false;          
      
    //判断  
    if(flag2){  
        return false;  
    }  
    if(flag1){     
        return false;     
    }     
}  
  
//禁止后退键 作用于Firefox、Opera  
document.onkeypress=banBackSpace;  
//禁止后退键  作用于IE、Chrome  
document.onkeydown=banBackSpace; 

/**
 * 弹出窗口
 * 
 * @param {}
 *            Url
 * @param {}
 *            Width
 * @param {}
 *            Height
 * @param {}
 *            WindowObj
 * @author 邓志城
 * @date 2009年6月4日9:27:22
 * @return {}
 */
function OpenWindow(Url, Width, Height, WindowObj) {
	var ReturnStr = showModalDialog(Url, WindowObj, "dialogWidth:" + Width
			+ "pt;dialogHeight:" + Height + "pt;"
			+ "status:no;help:no;scroll:no;");
	return ReturnStr;
}
/**
 * 弹非模态窗口
 * 
 * @param {}
 *            Url
 * @param {}
 *            Width
 * @param {}
 *            Height
 * @param {}
 *            WindowObj
 * @author 严建
 * @date 2011年9月15日9:36
 * @return {}
 */
function WindowOpen(Url, Name, Width, Height, title) {
	var win = window
			.open(
					Url,
					Name,
					'height='
							+ Height
							+ ', width='
							+ Width
							+ ', top=0, left=0, toolbar=no, '
							+ 'menubar=no, scrollbars=no, resizable=yes,location=no, status=no');
	return win;
}
/**
 * 弹非模态窗口
 * 
 * @param {}
 *            Url
 * @param {}
 *            Width
 * @param {}
 *            Height
 * @param {}
 *            Top
 * @param {}
 *            Left
 * @param {}
 *            WindowObj
 * @author 严建
 * @date 2011年9月15日9:36
 * @return {}
 */
function WindowOpen(Url, Name, Width, Height, Top, Left, title) {
	var win = window
			.open(
					Url,
					Name,
					'height='
							+ Height
							+ ', width='
							+ Width
							+ ', top='
							+ Top
							+ ', left='
							+ Left
							+ ', toolbar=no, '
							+ 'menubar=no, scrollbars=no, resizable=yes,location=no, status=no');
	return win;
}
/**
 * 打开窗口
 * 
 * @param {}
 *            Url
 * @param {}
 *            Width
 * @param {}
 *            Height
 */
function OpenWin(Url, Width, Height) {
	var win = window
			.open(
					Url,
					'sendDoc',
					'height='
							+ Height
							+ ', width='
							+ Width
							+ ', top=0, left=0, toolbar=no, '
							+ 'menubar=no, scrollbars=no, resizable=yes,location=no, status=no');
	return win;
}

//设置打印份数为5
function setPnum(pnum){
	pnum = 5;
	return pnum;
}
/**
 * 在IE6中正常显示透明PNG
 * 
 * @author 严建
 * @date 2011年10月08日14:04 用法： < script language="JavaScript">
 *       window.attachEvent("onload", correctPNG); < /script>
 */
function correctPNG() {
	// var arVersion = navigator.appVersion.split("MSIE");
	// var version = parseFloat(arVersion[1]);
	// if ((version >= 5.5) && (document.body.filters))
	// {
	// for(var j=0; j<document.images.length; j++)
	// {
	// var img = document.images[j];
	// var imgName = img.src.toUpperCase();
	// if (imgName.substring(imgName.length-3, imgName.length) == "PNG")
	// {
	// var imgID = (img.id) ? "id='" + img.id + "' " : "";
	// var imgClass = (img.className) ? "class='" + img.className + "' " : "";
	// var imgTitle = (img.title) ? "title='" + img.title + "' " : "title='" +
	// img.alt + "' ";
	// var imgStyle = "display:inline-block;" + img.style.cssText ;
	// if (img.align == "left") imgStyle = "float:left;" + imgStyle;
	// if (img.align == "right") imgStyle = "float:right;" + imgStyle;
	// if (img.parentElement.href) imgStyle = "cursor:hand;" + imgStyle;
	// var strNewHTML = "<span " + imgID + imgClass + imgTitle
	// + " style=\"" + "width:" + img.width + "px; height:" + img.height + "px;"
	// + imgStyle + ";"
	// + "filter:progid:DXImageTransform.Microsoft.AlphaImageLoader"
	// + "(src=\'" + img.src + "\', sizingMethod='scale');\"></span>" ;
	// img.outerHTML = strNewHTML;
	// j = j-1;
	// }
	// }
	// }
}
/**
 * 弹出提示
 * 
 * @author 邓志城
 * @date 2009年6月4日9:27:22
 * @param {}
 *            content
 */
function showTip(content) {
	$(content).appendTo("body");
	var obj = "#" + $(content).attr("id");
	var myleft = (jQuery("body").width() - jQuery(obj).width()) / 2
			+ jQuery("body").attr("scrollLeft");
	var mytop = (jQuery("body").height() - jQuery(obj).height()) / 3
			+ jQuery("body").attr("scrollTop");
	jQuery(obj).css("left", myleft).css("top", mytop).fadeIn("slow");
	window.setTimeout(function() {
		jQuery(obj).fadeOut(0);
	}, 0);
	window.setTimeout(function() {
		jQuery(obj).remove();
	}, 6500);
};

/**
 * 字符编码，指定encodeURI()不能编码的的字符用escape()编码,否则用encodeURI()编码
 * 
 * @author 严建
 * @date 2011年9月24日 14:57
 * @param {}
 *            stringObj:被编码的字符串,chatObj：不能用encodeURI()编码的字符
 * @return temp 编码之后的字符串
 */
function YJ_EncodeCode(stringObj, chatObj) {
	var temp = "";
	var arr = stringObj.split(chatObj);
	for ( var i = 0; i < arr.length; i++) {
		temp += encodeURI(encodeURI(arr[i]));
		if (i < arr.length - 1) {
			temp += escape(escape(chatObj));
		}
	}
	return temp;
}

/**
 * 字符编码，所有encodeURI()不能编码的字符用escape(),否则用encodeURI()编码
 * 
 * @author 严建
 * @date 2011年9月24日 15:12
 * @param {}
 *            stringObj:被编码的字符串
 * @return temp 编码之后的字符串
 */
function YJ_SpecialEncodeCode(stringObj) {
	var result = "";
	var charTemp = "";
	if (typeof (stringObj) != "string") {
		alert("YJ_SpecialEncodeCode()方法的参数必须是字符串!");
		return;
	}
	for ( var i = 0; i < stringObj.length; i++) {
		charTemp = stringObj.charAt(i);
		if (charTemp == encodeURI(encodeURI(charTemp))) {
			if (charTemp == escape(escape(charTemp))) {
				result += encodeURIComponent(encodeURIComponent(charTemp));
			} else {
				result += escape(escape(charTemp));
			}
		} else {
			result += encodeURI(encodeURI(charTemp));
		}
	}
	return result;
}

if ((typeof ($)) == "function") {
	$(document).ready(function() { 
		$("body").bind("keyup", function(event){ //enter默认查询 niwy
			 if (event.keyCode=="13"){
				var length = $("#img_sousuo").length;
				var le= $("#img_search").length;
				if(length > 0 ){
					$("#img_sousuo").click();
				}
				if(le>0){
					$("#img_search").click();
				}
		    }
		});
		// 需要过滤的特殊字符,含^,|,',",<,>./^[^\|"'<>]*$/.
		var url = $("form").attr("action"); // 屏蔽对邮件模块的过滤
		if (url && url.indexOf("/mymail") == -1) {
			/**
			 * 过滤特殊字符,针对文本输入域过滤.
			 */
			$("input:text[check!='false']").keyup(function() {
				/*
				 * var regEx = "<>" if(regEx.indexOf(this.value)!=-1){
				 * //alert("文本框中不能含有\n\n 1 单引号: ' \n 2 双引号: \" \n 3 竖 杠: | \n 4
				 * 尖角号: < > \n\n请检查输入！");
				 * $(this).val(this.value.replace(regEx,"")); }
				 */
				if (this.value.indexOf("&") != -1) {
					while (this.value.indexOf("&") != -1) {
						$(this).val(this.value.replace("&", "＆"));
					}
				}
				if (this.value.indexOf("<") != -1) {
					alert("文本框中不能含有尖角号: < > \n\n请检查输入。");
					while (this.value.indexOf("<") != -1) {
						$(this).val(this.value.replace("<", ""));
					}
				}
				if (this.value.indexOf(">") != -1) {
					alert("文本框中不能含有尖角号: < > \n\n请检查输入。");
					while (this.value.indexOf(">") != -1) {
						$(this).val(this.value.replace(">", ""));
					}
				}
			});
		}
	});
}
