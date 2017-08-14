var isIE = navigator.userAgent.toLowerCase().indexOf("msie") != -1;
//var isIE6 = navigator.userAgent.toLowerCase().indexOf("msie 6.0") != -1;
var isIE6 = $.browser.msie && ($.browser.version == "6.0") && !$.support.style;
var isIE7 = navigator.userAgent.toLowerCase().indexOf("msie 7.0") != -1 && !window.XDomainRequest;
var isIE8 = !!window.XDomainRequest&&!!document.documentMode;
var isGecko = navigator.userAgent.toLowerCase().indexOf("gecko") != -1;
var isQuirks = document.compatMode == "BackCompat";

function encodeURL(str){
	return encodeURI(str).replace(/=/g,"%3D").replace(/\+/g,"%2B").replace(/\?/g,"%3F").replace(/\&/g,"%26");
}

function htmlEncode(str) {
	return str.replace(/&/g,"&amp;").replace(/\"/g,"&quot;").replace(/</g,"&lt;").replace(/>/g,"&gt;").replace(/ /g,"&nbsp;");
}

function htmlDecode(str) {
	return str.replace(/\&quot;/g,"\"").replace(/\&lt;/g,"<").replace(/\&gt;/g,">").replace(/\&nbsp;/g," ").replace(/\&amp;/g,"&");
}

function isInt(str){
	return /^\-?\d+$/.test(""+str);
}

function isNumbe(str){
	var t = ""+str;
	for(var i=0;i<str.length;i++){
		var chr = str.charAt(i);
		if(chr!="."&&chr!="E"&&isNaN(parseInt(chr))){
			return false;
		}
	}
	return true;
}

function isTime(str){
	var arr = str.split(":");
	if(arr.length!=3){
		return false;
	}
	var date = new Date();
	date.setHours(arr[0]);
	date.setMinutes(arr[1]);
	date.setSeconds(arr[2]);
	return date.toString().indexOf("Invalid")<0;
}

function isDate(str){
	var arr = str.split("-");
	if(arr.length!=3){
		return false;
	}
	var date = new Date();
	date.setFullYear(arr[0]);
	date.setMonth(arr[1]);
	date.setDay(arr[2]);
	return date.toString().indexOf("Invalid")<0;
}

function isArray(obj) {
	 if(!obj){
	 	 return false;
	 }
   if (obj.constructor.toString().indexOf("Array") == -1){
      return false;
   } else{
      return true;
   }
}

String.prototype.startsWith = function(str) {
  return this.indexOf(str) == 0;
}

String.prototype.endsWith = function(str) {
  return this.charAt(this.length-1) == str;
}

String.prototype.trim = function(){
	return this.replace(/(^\s*)|(\s*$)/g,"");
}

String.prototype.leftPad = function(c,count){
	if(!isNaN(count)){
		var a = "";
		for(var i=this.length;i<count;i++){
			a = a.concat(c);
		}
		a = a.concat(this);
		return a;
	}
	return null;
}

String.prototype.rightPad = function(c,count){
	if(!isNaN(count)){
		var a = this;
		for(var i=this.length;i<count;i++){
			a = a.concat(c);
		}
		return a;
	}
	return null;
}

Array.prototype.clone = function(){
	var len = this.length;
	var r = [];
	for(var i=0;i<len;i++){
		if(typeof(this[i])=="undefined"||this[i]==null){
			r[i] = this[i];
			continue;
		}
		if(this[i].constructor==Array){
			r[i] = this[i].clone();
		}else{
			r[i] = this[i];
		}
	}
	return r;
}	

Array.prototype.insert = function(index,data){
	if(isNaN(index) || index<0 || index>this.length) {
		this.push(data);
	}else{
		var temp = this.slice(index);
		this[index]=data;
		for (var i=0; i<temp.length; i++){
			this[index+1+i]=temp[i];
		}
	}
	return this;
}

Array.prototype.remove = function(s,dust){//如果dust为ture，则返回被删除的元素
	if(dust){
	var dustArr=[];
	  for(var i=0;i<this.length;i++){
		if(s == this[i]){
			dustArr.push(this.splice(i, 1)[0]);
		}
	  }
	  return dustArr;
	}

  for(var i=0;i<this.length;i++){
    if(s == this[i]){
		this.splice(i, 1);
    }
  }
  return this;
}

Array.prototype.indexOf = function(func){
	var len = this.length;
	for(var i=0;i<len;i++){
		if (this[i]==arguments[0])
			return i;
	}
	return -1;
}

Array.prototype.each = function(func){
	var len = this.length;
	for(var i=0;i<len;i++){
		try{
			func(this[i],i);
		}catch(ex){alert("Array.prototype.each:"+ex.message);}
	}
}

function DateTimeUtil(){};

/**
  * 计算日期
  * 
  * @param  dat 	所要计算的日期
  *	@param	num 	所要计算的数量
  *	@param	type	所要计算的单位类型　year/month/day 或YEAR/MONTH/DAY
  * @return Date 计算后的日期
  * 如:DateTimeUtil.dateAfter(2006-01-28,2,"day") 计算后为2006-11-30
  * 如:DateTimeUtil.dateAfter(2006-11-28,-1,"month") 计算后为2006-10-28
  * 注释：公用方法
  */
DateTimeUtil.dateAfter = function(dat,num,type){
	return DateTimeUtil.getDateTime(dat,num,type);
}

DateTimeUtil.getDateTime = function(dat,num,type){
    if( dat=="" ) return "";
	var mydate;
	mydate = this.getDateFormat(dat);
	
	if(type=="year" || type=="YEAR")	mydate.setYear(mydate.getYear()+parseInt(num));
	if(type=="month" || type=="MONTH")	return DateTimeUtil.addMonthDate(dat,num);
	if(type=="day" || type=="DAY")		mydate.setDate(mydate.getDate()+parseInt(num));
	
	var yearstr = mydate.getYear().toString();
	var monstr = (mydate.getMonth()+parseInt(1)).toString();
	var daystr = mydate.getDate().toString();
	
	if(monstr.length == 1) monstr = "0" + monstr;
	if(daystr.length == 1) daystr = "0" + daystr;

	return yearstr + "-" + monstr + "-" + daystr;
}
/**
  * 计算日期两个日期相差多少天
  * 
  * @param  datestr1 	所要计算的日期
  * @param  datestr2 	所要计算的日期
  * @return 计算后的天数
  * 注释：公用方法
  */
DateTimeUtil.DateDiff = function(datestr1, datestr2){
    if( datestr1 =="" || datestr2 =="" ) return "";
	var mydate,mydate1;
	mydate = this.getDateFormat(datestr1);
	mydate1 = this.getDateFormat(datestr2);

    var date =  Math.round((Date.UTC(mydate.getYear(),mydate.getMonth(),mydate.getDate())-Date.UTC(mydate1.getYear(),mydate1.getMonth(),mydate1.getDate()))/(1000*60*60*24));
	return date ;
}
/**
 * 格式化日期
 * @param  datestr 	所要格式化的日期(字符串)
 */
DateTimeUtil.getDateFormat = function(datestr){
	if(datestr == "")return "";
	var ss,mydate;
	ss = datestr.split("-");

   return new Date(ss[0],ss[1]-1,ss[2]);

/*
	mydate = new Date();
	alert(ss[0]+"%"+ss[1]+"%"+ss[2])
	mydate.setYear(ss[0]);
	mydate.setMonth(parseInt(ss[1])-1);
	mydate.setDate(ss[2]);
	return mydate;
	*/
}

/**
 * 格式化日期
 * @param  date 	所要格式化的日期(Date)
 */
DateTimeUtil.formatDate = function(date){

	if(date == null || typeof(date) != "object" )return "";
	
	return date.getYear() + "-" + (date.getMonth()+parseInt(1)).toString() + "-" + date.getDate();
}

/**
 * 格式化日期
 * @param  date 	所要格式化的日期(Date)
 * @param  format   格式化的格式 
 * 例子：date ＝ new Date() , format = YYYYMMDDhhmmss  
 * return  res= 2007421105021
 */
DateTimeUtil.formatDateTime = function(date ,format){

	if(date == null || typeof(date) != "object" )return "";
	var res = new String ;
	res = format;
	res = res.replace("YYYY" , date.getYear());
	res = res.replace("MM" , (date.getMonth()+parseInt(1)).toString().length==1?("0"+(date.getMonth()+parseInt(1)).toString()):date.getMonth().toString());
	res = res.replace("DD" , date.getDate().toString().length==1?("0"+date.getDate().toString()):date.getDate().toString());
	res = res.replace("hh" , date.getHours().toString().length==1?("0"+date.getHours().toString()):date.getHours().toString());
	res = res.replace("mm" , date.getMinutes().toString().length==1?("0"+date.getMinutes().toString()):date.getMinutes().toString());
	res = res.replace("ss" , date.getSeconds().toString().length==1?("0"+date.getSeconds().toString()):date.getSeconds().toString());
	return res;
}

/**
 * 计算两个日期之间相差多少月 精确到日期小数位
 * 
 * （截止日期－开始日期　的最小月数) + (剩余天数/开始日期所属月的总天数)；
 * 
 * @param date1
 * @param date2
 * @return
 */
DateTimeUtil.getDiscrepantMonth = function(date1, date2){
 	if( date1 =="" || date2 =="" ) return "";
	var mydate,mydate1,month;
	mydate = this.getDateFormat(date1);
	mydate1 = this.getDateFormat(date2);
	month = ((mydate.getYear() - mydate1.getYear()) * 12) + ( mydate.getMonth() -  mydate1.getMonth())
			+ (mydate.getDate() - mydate1.getDate())/ this.getActualMaximum(mydate1.getMonth(),mydate1.getYear());
	return month;
}

/**
 * 计算日期月份内有多少天
 * @param  month 	计算的日期所属月份
 * @param  year 	计算的日期所属年份
 */
DateTimeUtil.getActualMaximum = function(month, year) {
	if(month == "" || year =="")return "";
	 var daysInMonth = new Array(31, 28, 31, 30, 31, 30, 31, 31,30, 31, 30, 31);
	 if (2 == month)
	    return ((0 == year % 4) && (0 != (year % 100))) ||(0 == year % 400) ? 29 : 28;
	 else
	    return daysInMonth[month-1];
}
/**
 *比较2个日期 date1等于date2 返回0 date1大于date2 返回1 date1小于date2返回-1
 * @param  date1 	
 * @param  date2 	
 */
DateTimeUtil.DateTimeDiff = function(date1,date2){
	if(date1 == date2){
		return 0;
	}
	if(date1 > date2){
		return 1;
	}
	if(date1 < date2){
		return -1;
	}
}
/**
* 返回当前日期:格式YYYY-MM-DD HH:MM:SS,By wangshunji
* modify by fyy for 取服务器时间
*/
DateTimeUtil.getCurrentDay = function(){
	try{
		if(getnowDateTime())  return getnowDateTime();
	}catch(e){}
	var date = new Date();
	var datestr = date.getYear() + '-';
	var month = date.getMonth() + 1 ;
	datestr +=  month > 9 ? month + '-' : ('0' + month) + '-';
	var day = date.getDate();
	datestr += day > 9 ? day : ('0' + day); 
	datestr = datestr + " ";
	var hour = date.getHours();
	datestr += hour > 9 ? hour : ('0'+ hour);
	datestr += ':';
	var minute = date.getMinutes();
	datestr += minute > 9 ? minute : ('0' + minute);
	datestr += ':';
	var second = date.getSeconds();
	datestr += second > 9 ? second : ('0' + second);
	return datestr;
}


DateTimeUtil.getCurrentDay_NotTime = function(){
	try{
		if(getnowDate())  return getnowDate();
	}catch(e){}
	var date = new Date();
	var datestr = date.getYear() + '-';
	var month = date.getMonth() + 1 ;
	datestr +=  month > 9 ? month + '-' : ('0' + month) + '-';
	var day = date.getDate();
	datestr += day > 9 ? day : ('0' + day); 
	return datestr;
}

/**
*  获取本月月底的日期格式:XXXX-XX-XX
* var strTime = d.getFullYear()+"-"+(d.getMonth()+1)+"-"+d.getDate()+" "+d.getHours()+":"+d.getMinutes()+":"+d.getSeconds();
*/
DateTimeUtil.getNextMonthFirstDay = function(){
	var d = new Date();
	var year = d.getFullYear();
	var month = d.getMonth()+1;
	
	var date =  year+"-";
	if(month<10) {
		date = date + "0" + month + "-";
	}
	else {
		date = date + month + "-";
	}
	date = date + this.getActualMaximum(month,year);
	return date;
}

/**
 * 日期相加(对月),减一天
 * da : 2007-01-01
 * num:整数
 * modify by fuyy
 */
DateTimeUtil.addMonthDate = function(da,num){
		var arr = new Array();
	arr = da.split("-");
	if(arr == null || arr == ""){
		alert("日期格式不对!");
		return false;
	}
	if(isNaN(num) || num.toString().indexOf(".")!= -1){
		alert("请输入整数!");
		return false;
	}	
	var year = arr[0];
	var month = arr[1];
	var date = arr[2];
	//使用parseInt注意不能以0开头，为了减少改动，再这里特殊处理，应该使用new Number
	//modify by shenx,5。15，
	if(month.substring(0,1) == 0){
        month = month.substring(1,2) ;
	}
	else{
		month = month.substring(0,2) ;
	}
	if(date.substring(0,1) == 0){
        date = date.substring(1,2) ;
	}
	else{
		date = date.substring(0,2) ;
	}
	var add = parseInt(num);
	if(add >= 12){
		year = parseInt(year) + parseInt((add - add%12)/12);
		month = parseInt(month) + add%12;
	}
	else{
		month = parseInt(month) + add;
		//add month > 12
		if(parseInt(month) > 12)
		{
			month = parseInt(month) - 12 ;
			year = parseInt(year) + 1 ; 
		}
	}

	//01号
	if(parseInt(date) == 1){
		month = parseInt(month) -1;
		//month = 0
		if(parseInt(month) == 0)
		{
			month = 12;
			//这里没有控制year<0的时候，也就是公元前
			year = parseInt(year) - 1;
		}
		date = this.getActualMaximum(month,year);	}
	else{
		date = parseInt(date,10) - 1;
	}
	//date > month.date时
	if(parseInt(date) > this.getActualMaximum(month,year)){
		date = this.getActualMaximum(month,year);
	}
	
	if(parseInt(month) < 10){
		month = "0"+month.toString(); 
	}
	if(parseInt(date) < 10){
		date = "0"+date.toString(); 
	}
	return year + "-" + month + "-" +date;

}

/*
 * 日期相减(对月),加一天
 * da : 2007-01-01
 * num:整数
 * author:fuyy
 */
DateTimeUtil.decMonthDate = function(da,num){
	var arr = new Array();
	arr = da.split("-");
	if(arr == null || arr == ""){
		alert("日期格式不对!");
		return false;
	}
	if(isNaN(num) || num.toString().indexOf(".")!= -1){
		alert("请输入整数!");
		return false;
	}	
	var year = arr[0];
	var month = arr[1];
	var date = arr[2];
	//使用parseInt注意不能以0开头，为了减少改动，再这里特殊处理，应该使用new Number
	//modify by shenx,5。15，
	if(month.substring(0,1) == 0){
        month = month.substring(1,2) ;
	}
	else{
		month = month.substring(0,2) ;
	}
	if(date.substring(0,1) == 0){
        date = date.substring(1,2) ;
	}
	else{
		date = date.substring(0,2) ;
	}

	var dec= parseInt(num);
	if(dec>= 12){
		year = parseInt(year) - parseInt((dec- dec%12)/12);
		month = parseInt(month) - dec%12;
		while(parseInt(month) <= 0)
		{
			month = parseInt(month) + 12 ;
			year = parseInt(year) - 1 ; 
		}

	}
	else{
		month = parseInt(month) - dec;

		while(parseInt(month) <= 0)
		{
			month = parseInt(month) + 12 ;
			year = parseInt(year) - 1 ; 
		}
	}
	//如果是当月的最后一天
	if(parseInt(date) == this.getActualMaximum(arr[1],arr[0])){
		month = parseInt(month) + 1;
		//month = 0
		if(parseInt(month) > 12)
		{
			month = 1;
			year = parseInt(year) + 1;
		}
		date = 1;
	}
	else{
		date = parseInt(date) + 1;
	}
	//date > month.date时
	if(parseInt(date) > this.getActualMaximum(month,year)){
		date = this.getActualMaximum(month,year);
	}
		if(parseInt(month) < 10){
		month = "0"+month.toString(); 
	}
	if(parseInt(date) < 10){
		date= "0"+date.toString(); 
	}
	return year + "-" + month + "-" +date;
}

/**
 * dtDate:like 2007-02-28
 * 取得给定日期天数后的日期
 */
DateTimeUtil.addDate = function(dtDate,num){
   var arr = new Array();
   arr = dtDate.split("-");
   var frmdate = arr[0]+"/"+arr[1]+"/"+arr[2]+" 00:00:00";
   var date = new Date(frmdate);	
   var lIntval = parseInt(num);//间隔
   date.setDate(date.getDate() + lIntval);
   var year = date.getYear();
   var month = date.getMonth()+1;
   if(month < 10){
   	 month = "0" + month;
   }
   var date = date.getDate();
   if(date < 10){
   	 date = "0" + date;
   }
   return  year +'-' + month + '-' +date;
}

/**
* s:String 日期格式，2007-1-1 0:0:0
* String 类型转 Date 类型
**/
DateTimeUtil.parseDate=function(s){
	s=s.replace(/-/g,"/");
	return new Date(s);
}

/**
 * 弹出窗口
 * @param {} Url
 * @param {} Width
 * @param {} Height
 * @param {} WindowObj
 * @author 邓志城
 * @date 2009年6月4日9:27:22
 * @return {}
 */
function OpenWindow(Url,Width,Height,WindowObj)
{
	var ReturnStr = showModalDialog(Url,WindowObj,'dialogWidth:'+Width+'pt;dialogHeight:'+Height+'pt;status:no;help:no;scroll:no;status:0;help:0;scroll:0;');
	return ReturnStr;
}

function OpenModalWindow(Url,Width,Height,WindowObj)
{
	var mask0=window.top.document.getElementById("mask_top");
	var mask1=window.top.document.getElementById("mainIframe").contentWindow.document.getElementById("mask_content");
	mask0.style.visibility='visible';
	mask1.style.visibility='visible';
	var ReturnStr = showModalDialog(Url,WindowObj,'dialogWidth:'+Width+'pt;dialogHeight:'+Height+'pt;status:no;help:no;scroll:no;status:0;help:0;scroll:0;');
	mask0.style.visibility='hidden';
	mask1.style.visibility='hidden';
	
	return ReturnStr;
}

function OpenModalWindow2(Url,Width,Height,WindowObj)
{
	var mask=document.getElementById("mask");
	mask.style.visibility='visible';
	var ReturnStr = showModalDialog(Url,WindowObj,'dialogWidth:'+Width+'pt;dialogHeight:'+Height+'pt;status:no;help:no;scroll:no;status:0;help:0;scroll:0;');
	mask.style.visibility='hidden';
	
	return ReturnStr;
}

function OpenModalWindow3(Url,Width,Height,WindowObj)
{
	var mask=window.parent.parent.parent.document.getElementById("mask");
	mask.style.visibility='visible';
	var ReturnStr = showModalDialog(Url,WindowObj,'dialogWidth:'+Width+'pt;dialogHeight:'+Height+'pt;status:no;help:no;scroll:no;status:0;help:0;scroll:0;');
	mask.style.visibility='hidden';
}

function OpenEditorWindow(Url,WindowName,Width,Height)
{
	window.open(Url,WindowName,'toolbar=0,location=0,maximize=1,directories=0,status=1,menubar=0,scrollbars=0,resizable=1,top=50,left=50,width='+Width+',height='+Height);
}

var Cookie = {};//Cookie操作类，支持大于4K的Cookie

Cookie.Spliter = "_ZVING_SPLITER_";

Cookie.get = function(name){
  var cs = document.cookie.split("; ");
  for(i=0; i<cs.length; i++){
	  var arr = cs[i].split("=");
	  var n = arr[0].trim();
	  var v = arr[1]?arr[1].trim():"";
	  if(n==name){
	  	return unescape(v);
	  }
	}
	return null;
}

Cookie.getAll = function(){
  var cs = document.cookie.split("; ");
  var r = [];
  for(i=0; i<cs.length; i++){
	  var arr = cs[i].split("=");
	  var n = arr[0].trim();
	  var v = arr[1]?arr[1].trim():"";
	  if(n.indexOf(Cookie.Spliter)>=0){
	  	continue;
	  }
	  if(v.indexOf("^"+Cookie.Spliter)==0){
	      var max = v.substring(Cookie.Spliter.length+1,v.indexOf("$"));
	      var vs = [v];
	      for(var j=1;j<max;j++){
	      	vs.push(Cookie.get(n+Cookie.Spliter+j));
	      }
	      v = vs.join('');
	      v = v.substring(v.indexOf("$")+1);
	   }
	   r.push([n,unescape(v)]);
	}
	return r;
}
 
Cookie.set = function(name, value, expires, path, domain, secure, isPart){
	if(!isPart){
		var value = escape(value);
	}  
	if(!name || !value){
		return false;
	}
	if(!path){
		path = Server.ContextPath;//特别注意，此处是为了实现不管当前页面在哪个路径下，Cookie中同名名值对只有一份
	}
	if(expires!=null){
	  if(/^[0-9]+$/.test(expires)){
	    expires = new Date(new Date().getTime()+expires*1000).toGMTString();
		}else{
			var date = DateTime.parseDate(expires);
			if(date){
				expires = date.toGMTString();
			}else{
		  	expires = undefined;
		  }
		}
	}
	if(!isPart){
	  Cookie.remove(name, path, domain);
	}
	var cv = name+"="+value+";"
		+ ((expires) ? " expires="+expires+";" : "")
		+ ((path) ? "path="+path+";" : "")
		+ ((domain) ? "domain="+domain+";" : "")
		+ ((secure && secure != 0) ? "secure" : "");
  if(cv.length < 4096){
		document.cookie = cv;
	}else{
		var max = Math.ceil(value.length*1.0/3800);
		for(var i=0; i<max; i++){
			if(i==0){
				Cookie.set(name, '^'+Cookie.Spliter+'|'+max+'$'+value.substr(0,3800), expires, path, domain, secure, true);
			}else{
				Cookie.set(name+Cookie.Spliter+i, value.substr(i*3800,3800), expires, path, domain, secure, true);
			}
		}
	}
  return true;
}

Cookie.remove = function(name, path, domain){
	var v = Cookie.get(name);
  if(!name||v==null){
  	return false;
  }
  if(escape(v).length > 3800){
		var max = Math.ceil(escape(v).length*1.0/3800);
		for(i=1; i<max; i++){
			document.cookie = name+Cookie.Spliter+i+"=;"
				+ ((path)?"path="+path+";":"")
				+ ((domain)?"domain="+domain+";":"")
				+ "expires=Thu, 01-Jan-1970 00:00:01 GMT;";
		}
	}
	document.cookie = name+"=;"  
		+ ((path)?"path="+path+";":"")   
		+ ((domain)?"domain="+domain+";":"")   
		+ "expires=Thu, 01-Jan-1970 00:00:01 GMT;";   
	return true;   
}

/**
 * 用传入的字符数组替换source字符串中的{}内容
 * @param {} source 替换前的字符串
 * @param {} params 需要替换的字符数组
 * @return 替换后的字符串
 */
function formatString(source, params){
	var reg = new RegExp("\\{\\d+\\}")
	$.each(params, function(i, n) {
		source = source.replace(reg, n);
	});
	return source;
}

/**
 * 从国际化资源文件中获取对应的文本值，若没有则返回“”
 * @param {} text -国际化资源内容
 * @param {} params -占位符要替换的文本内容数组
 * @return 国际化后的字符串
 */
function getText(text, params){
	if(text && text == null){
		return "";
	}
	var message = formatString(text, params);
	return message;
}

var URL_ALERT = "/common/jsp/alert.jsp";//警告页面地址
var URL_CONFIRM = "/common/jsp/confirm.jsp";//确认页面地址
var URL_ERROR = "/common/jsp/error.jsp";//错误页面地址
var URL_INFO = "/common/jsp/info.jsp";//提示页面地址

/**
 * 警告对话框，替代默认的alert()方法
 * @param {} str -alert要显示的信息
 * @return {}
 */
window.Alert = function(str){
	//return window.showModalDialog(root + URL_ALERT,[window, str],"dialogWidth=336px;dialogHeight=169px;resizable=no;status=no;help=no;location=no");
	return alert(str);
}

/**
 * 确认对话框，替代默认的confirm()方法
 * @param {} str -confirm要显示的信息
 * @return {} true或者false
 */
window.Confirm = function(str){
	//var returnvalue = window.showModalDialog(root + URL_CONFIRM,[window, str],"dialogWidth=336px;dialogHeight=169px;resizable=no;status=no;help=no;location=no");
	var returnvalue = confirm(str);
	return returnvalue;
}

/**
 * 错误对话框
 * @param {} str -要显示的错误信息
 * @return {}
 */
window.Error = function(str){
	//return window.showModalDialog(root + URL_ERROR,[window, str],"dialogWidth=336px;dialogHeight=169px;resizable=yes;status=yes;help=yes;location=yes;scroll=no;");
	return alert(str);
}

/**
 * 提示对话框
 * @param {} str -要显示的提示信息
 * @return {}
 */
window.Info = function(str){
	//return window.showModalDialog(root + URL_INFO,[window, str],"dialogWidth=336px;dialogHeight=169px;resizable=no;status=no;help=no;location=no");
	return alert(str);
}

//window.confirm = window.Confirm;

//window.alert = window.Error;

/**
 * 将表单中input和textarea等页面元素的值的前后空格去掉
 * @param form -form为jquery对象，而非DOM对象
 */
function trimForm(form) {
	var value = "";
	$("input,textarea", form).not(":button").each(function(i, element) {
		value = $(element).val();
		if (value != null) {
			$(element).val($.trim(value));
		}
	});
}

try { document.execCommand("BackgroundImageCache", false, true); } catch (e) { }

/**
 * 页面加载时展现加载提示信息,依赖于jquery.blockUI.js
 * @param {} message -加载提示信息
 */
function showLoadingTip(message){
	$.blockUI({
	    message: message,
	    centerY: false,
	    css: {
	    	fontWeight:'bold',
			filter: 'alpha(opacity=80)',
			opacity: '0.8',
			margin:		0,
			textAlign:	'center',
			color: '#FF0000',
			border:		'1px solid #ff8080',
			backgroundColor:'#fff2f2',
	        position: 'absolute',
			top:  0, 
	        left: 0, 
	        width: '99%',
			padding: '6px'
	    }
	});
}

function hideLoadingTip(){
	$(window).unblock();
}

/**
 * 操作完成时展示操作反馈信息
 * @param {} message -操作反馈信息
 */
function showActionTip(message){
	if($("#_actionTip").length == 0){
		$('<div style="display:none;" id="_actionTip"></div>').css({
			fontWeight:'bold',
			color: '#FF0000',
			filter: 'alpha(opacity=80)',
			opacity: '0.8',
			margin:		0,
			textAlign:	'center',
			border:		'1px solid #ff8080',
			backgroundColor:'#fff2f2',
	        position: 'absolute',
			top:  0, 
	        left: 0, 
	        width: '99%',
			padding: '6px'
	    }).appendTo($(document.body)).click(function(){$(this).hide();});
	}
	$("#_actionTip").html(message).show();
}

/**
 * 隐藏操作反馈信息
 */
function hideActionTip(){
	$("#_actionTip").hide();
}

/**
 * 取字符串长度
 * @param {Object} str
 * @return {TypeName} 
 */
function getStrLen(str) {
	var len = 0;
 	var v_char = str.split("");
 	for (var i=0;i<v_char.length;i++) {
  		if (v_char[i].charCodeAt(0)<299) {
   			len++;
  		} else {
   			len+=2;
  		}
 	}
 	return len;
}

function preview() 
{ 
window.print(); 
} 