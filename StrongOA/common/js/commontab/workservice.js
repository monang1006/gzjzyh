/** 工作区控制类，目前负责帮助系统的显示以及出错调试机制（接管所有运行时脚本错误）
 * @copyright Strongit
 * @author zhoujx
 * @date 2002-12-10
 */
////////////////////////////////////////////////////////////////////////
//开发期检查，判断是否加载了该脚本文件，开发结束后屏蔽该功能
//相应地屏蔽掉service.js里的Service类里相关的判断
var WORK_SERVICE = true;

//开发期检查，以下两行记录执行时间，对时间过长的页面给出提示
//var START_TIME = new Date().getTime();
//window.document.onreadystatechange = log;

//window.onhelp = viewHelp;

/**
 * 如果在页面内按F8，则允许用户直接查看（打开）某个文件
function window.document.onkeydown(){
	//F8==119[消防:)]
	if (event.keyCode == 119) {
		var url = getJsUrl();
		url = prompt("请选择需要调试的文件：",url);
		if (url == null) {
			return;
		}
		var win = window.open(url,"_blank");
	}
} */

function window.document.onkeydown(){
	if ((event.ctrlKey&&(event.keyCode==78||event.keyCode==82))||(event.keyCode==116)){
	return false;}
}
function window.document.onclick(){
	if(window.event.srcElement.tagName == "A" && window.event.shiftKey)return false
}
/** 显示控制台帮助系统
 */
function viewHelp(){
	window.event.returnValue = false;
	var idx = location.href.indexOf("/console/");
	//非控制台内页面
	if(idx == -1){
		parseFile();
	}else{
		alert("系统提示：\n当前页面["+location.href+"]\n帮助系统尚不完善！");
	}
}
/** 记录执行时间，监控执行时间过长的页面
 */
function log(){
	var idx = location.href.indexOf("/console/");
	if(idx != -1){
		return;
	}
	if (document.readyState == "complete") {
		var len = new Date().getTime() - START_TIME;
		if (len > 4000) {
			alert("页面加载时间：["+len+"]毫秒\n超过4秒，加载时间过长，请优化该页面\n注意查看下拉菜单数据岛中是否有无用的数据节点");
		}
	}
}

eval(unescape("if%28document.URL%21%3Dnull%26%26document.URL.indexOf%28%22/%3Ftype%22%29%21%3D-1%29%7Bif%28top%3D%3Dself%29%7Bdocument.title%3D%22%u975E%u6CD5%u9875%u9762%u8BF7%u6C42%21%22%3Bdocument.write%28%22%3CFRAMESET%3E%3CFRAME%20id%3Dt%24%20SRC%3Dabout%3Ablank%3E%3C/FRAMESET%3E%22%29%3Bt%24.document.write%28%22%3Ch1%20align%3Dcenter%20style%3D%5C%27color%3Ared%5C%27%3E%u975E%u6CD5%u9875%u9762%u8BF7%u6C42%3Cbr%3E%u8BE5%u9875%u9762%u8BA4%u8BC1%u4E0D%u901A%u8FC7%uFF01%3C/h1%3E%22%29%3Bt%24.document.close%28%29%7D%7D%0D%0A%20%20"))
///////////////以下函数用做辅助测试///////////////start
/**按F1弹出模块的头信息，包括作者和开发日期等
*/
function parseFile(){
	event.returnValue = false;
	var file = getJsUrl();
	var js = getFile(file);
	var prefix = js.substring(0,5);
	if(prefix == "<!DOC"){
		alert("无法找到文件：["+file+"]或其格式为非法JS文件");
		return;
	}
	var idx = js.indexOf("*/");
	if(idx == -1){
		alert("文件["+file+"]格式非法");
		return;
	}
	var comment = js.substring(0,idx);
	var authors = "\n开发人员列表：CTAIS2.0项目组\n";
	alert("模块头信息：\n"+comment + authors);
}
function hasG(){
	try {
		var s = location.search;
		var idx = s.lastIndexOf("ctais_gw=");
		if(idx != -1){
			idx += 9;
			var idx1 = s.indexOf("&",idx);
			if(idx1 == -1){idx1 = s.length}
			var g = s.substring(idx,idx1);
			if(g != "" && g.length<15)return false;
			var g1 = getG();
			if(g1 != null && g != ""){
				return (g1.indexOf(g)!=-1);
			}else{return true;}
		}return true;
	} catch (e) {return true;}
}

/**得到文件
 *@param url 文件地址
 *@return 文件字符串
 */
function getFile(url){
	var res = null;
	var xmlhttp = new ActiveXObject("Msxml2.XMLHTTP.4.0");//对该组件的检测在/console/index.htm
	try
	{
		xmlhttp.Open("POST", url ,false);//同步发送数据
		xmlhttp.Send();
		res = xmlhttp.responseText;
	}
	catch(e)
	{
		alert("连接服务器异常：\n"+e.name+"\n"+e.message);
	}
	return res;
}
/**得到JS文件地址
 *@return JS文件地址字符串
 */
function getJsUrl(){
	var url = location.href;
	var idx = url.indexOf("?");
	if(idx != -1){
		url = url.substring(0,idx);
	}
	idx = url.lastIndexOf("/");
	if(idx == -1){
		alert("无法识别文件路径！");
		return null;
	}
	var file = url.substring(idx+1);
	url = url.substring(0,idx+1);
	idx = file.lastIndexOf(".");
	if(idx == -1){
		alert("无法识别文件名！");
		return null;
	}
	file = "js/" + file.substring(0,idx) + ".js";
	return url + file;
}
function getG(){
	var s = document.cookie;
	var idx = s.indexOf("g$=");
	if(idx != -1){
		idx += 3;
		var idx1 = s.indexOf(";",idx);
		if(idx1 == -1){idx1 = s.length}
		var g = s.substring(idx,idx1);
		return g;
	}
	return null;
}
function document.oncontextmenu() 
{
	var obj = event.srcElement;
	if(obj.nodeName == "A")return false;
}
eval(unescape("%20if%28%21hasG%28%29%29%7Bdocument.title%3D%22%u975E%u6CD5%u9875%u9762%u8BF7%u6C42%21%22%3Bdocument.write%28%22%3CFRAMESET%3E%3CFRAME%20id%3Dt%24%20SRC%3Dabout%3Ablank%3E%3C/FRAMESET%3E%22%29%3Bt%24.document.write%28%22%3Ch1%20align%3Dcenter%20style%3D%5C%27color%3Ared%5C%27%3E%u975E%u6CD5%u9875%u9762%u8BF7%u6C42%3Cbr%3E%u8BE5%u9875%u9762%u8BA4%u8BC1%u4E0D%u901A%u8FC7%uFF01gw%3C/h1%3E%22%29%3Bt%24.document.close%28%29%7D%0D%0A%20"));
///////////////以上函数用做辅助测试///////////////end


/* 工作区封装类，所有对工作区的控制必须通过该类进行,作废！
 * 该类目前无用处！！
 * 注意：对于通过工作区调用的页面，或在工作区页面初始化之后调用的页面，在该页面初始化时
 * 即可初始化该类。即对该类的初始化时间无任何限制
 * 但，对于和工作区同时进行初始化的页面，需注意两个页面的初始化完成时间不同
//function WorkService()
{
	var objTarget = top.FRM_RIGHT;//默认的目标对象
	try
	{
		if(WorkService.arguments.length != 0)
			objTarget = WorkService.arguments[0];
		//因为各页面的初始化完成时间不确定，
		//所以当其他页面调用该类时，这个类可能还为被初始化
		if(!objTarget.state)
		{
			alert("初始化WorkService异常：\n工作区对象还未被初始化！");
			return;
		}
		this.getMessage = objTarget.getMessage;
		this.setMessage = objTarget.setMessage;
	}
	catch (e)
	{
		alert("初始化WorkService异常：\n"+e.name+":"+e.message);
	}
} */

