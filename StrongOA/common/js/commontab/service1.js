/**
 *	该脚本文件提供一些公用的函数
 * @copyright strongIT
 * @author zhoujx
 * @date 2002-5-20
 * 修改人  ：王春涛  
 * 修改日期：20050301
 * 修改内容：修改纳税人识别号不为15位是只显示提示信息
 */

//消息提示框
var URL_MESSAGE = scriptroot+"/fileNameRedirectAction.action?toPage=/frame/perspective_content/actions_container/dlg_message.jsp";
//内部协查管理的JSP绝对路径
var URL_NBXC = "/StrongOA/work/jcfz/nx/nx_check/index.jsp";
//Web打印预览页面
var URL_TO_PRINT = "/StrongOA/work/public/htm/toprinter.htm";
//Web输出Excel
var URL_TO_EXCEL = "/StrongOA/work/public/htm/toexcel.htm";
//alert
var URL_ALERT = scriptroot+"/fileNameRedirectAction.action?toPage=/frame/perspective_content/actions_container/dlg_alert.jsp";
//confirm
var URL_CONFIRM = scriptroot+"/fileNameRedirectAction.action?toPage=/frame/perspective_content/actions_container/dlg_confirm.jsp";
//prompt
var URL_PROMPT = scriptroot+"/fileNameRedirectAction.action?toPage=/frame/perspective_content/actions_container/dlg_prompt.jsp";
//申报业务模块要打印的违法违章通知单
var URL_TZD = "/StrongOA/work/sb/public/wfwztzd/wfwztzd.xsl";
var StrongOA$VER = "$PRODUCT";

//正式环境一定要去掉
//----开发时用。正式环境需去掉start
convertPaths();

/** 开发时用。转换所有绝对路径。方便在本地打开时能正常定位到指定路径。
 *  当在服务器上运行时，不做处理；当在本地打开时，作相应处理，定位到本地文件
 */
function convertPaths(){
	if (location.protocol == "http:") {
		return;
	}
	var url = location.href;
	var idx = url.lastIndexOf("/StrongOA2.0_DEV");
	if (idx == -1) {
		alert("转换异常");
		return;
	}
	var pre = url.substring(0,idx);

	URL_MESSAGE		= pre + URL_MESSAGE;
	URL_NBXC		= pre + URL_NBXC;
	URL_TO_PRINT	= pre + URL_TO_PRINT;
	URL_TO_EXCEL	= pre + URL_TO_EXCEL;
	URL_ALERT		= pre + URL_ALERT;
	URL_CONFIRM		= pre + URL_CONFIRM;
	URL_PROMPT		= pre + URL_PROMPT;
	URL_TZD			= pre + URL_TZD;
}

var LOGIN = false;
var IS_LOGIN = true;
var HOST = "http://zhanggx:7001";
function loginRemote(){
		if (LOGIN) {
			return;
		}
		if (StrongOA$VER != "$DEVELOP") {
			LOGIN = false;
			return false;
		}
		var service = new Service();
		LOGIN = true;
		service.debug = false;
		service.serviceUrl = HOST+"/StrongOA/entry/validatelogin";
		service.serviceParam = "type=ipc&purpose=LogInService&module=Entry&userId=&password=";
		service.serviceParse = false;
		var res = service.doService();
		IS_LOGIN = false;
		var idx = -1;
		if(res != null)
			idx = res.indexOf(":CONSOLE:");
		if(idx != -1){
			window.status = "登录服务器["+HOST+"]成功！";
			return true;
		}else{
			alert("系统提示：\n登录失败！");
			LOGIN = false;
			return false;
		}

}
//----开发时用。正式环境需去掉end


//是否进行页面改变的监听
var PAGE_LISTENER_ENABLED = false;
notifyChange(false);


/*
 * 屏蔽右键菜单
 
function window.document.oncontextmenu(){
	return false;
}*/
/** alert
 * @param str 字符串
 * @param flag 字符串消息级别：0,1,2
 */
/*function window.alert(str,flag){
	if(flag == null){
		flag = 0;
	}
	return window.showModalDialog(URL_ALERT,new Array(""+flag,str),"dialogWidth=500px;dialogHeight=200px;resizable=yes;status=no;help=no");
}

*//** confirm
 * @param str 字符串
 *//*
function window.confirm(str){
	return showModalDialog(URL_CONFIRM,str,"dialogWidth=500px;dialogHeight=200px;resizable=yes;status=no;help=no");
}
*//** prompt
 * @param str 字符串
 * @param val 默认值
 * @param isPwd 内容是否为密文
 *//*
function window.prompt(str,val,isPwd){
	return showModalDialog(URL_PROMPT,new Array(str,val,isPwd),"dialogWidth=500px;dialogHeight=220px;resizable=yes;status=no;help=no");
}*/

/** 设置是否进行页面的监控
 * @param bl true/false 是否进行页面监控，默认的状态为true
 */
function setPageListenerEnabled(bl){
	PAGE_LISTENER_ENABLED = bl;
}

/** 取得控制台
*/
function getSysConsole(){
    if(window.top.perspective_content=="undefined"||(typeof window.top.perspective_content)=="undefined"){
        return window.top.bottomFrame;//top.FRM_RIGHT;
    }else if(window.top.perspective_toolbar=="undefined"||(typeof window.top.perspective_toolbar)=="undefined"){
    	return window.top.perspective_content.document.mainIframe;
    	}else{
    	return window.top.perspective_content.actions_container.personal_properties_toolbar;//top.FRM_RIGHT;
    }
}

/** 取得控制台
*/
function getWinConsole(win){
	return win.top.FRM_RIGHT;
}

/** 取得控制台
*/
function getSysLeft(){
	return top.FRM_LEFT;
}

/** 发送数据
 * @param data 要发送的数据
 * @return 服务器的返回信息
 */
function send(data) {
	//---------用作本地调试，正式环境去掉start
	if (location.protocol == "file:" && !LOGIN) {
		var doc = loadXml(data);
		var name = doc.documentElement.selectSingleNode("Service/Name");
		var file = name.text+".xml";

		var doc = new ActiveXObject("MSXML.DOMDocument");
		doc.async = false;
		doc.load(file);
		var oErr = doc.parseError;
		if (StrongOA$VER != "$DEVELOP") {
			oErr.errorCode = -1;
		}
		if (oErr.errorCode != 0) {
			if (confirm("无法解析本地文件["+file+"]，您希望连接到["+HOST+"]吗")) {
				if (!loginRemote()) {
					return "<StrongOASession><Service><RtnCode>1</RtnCode><RtnMsg><Code>-1</Code><Message>登录["+HOST+"]失败</Message></RtnMsg><Argument></Argument></Service></StrongOASession>";
				}else{
					this.serviceUrl = HOST+"/StrongOA/entry/EntryServlet";//后台servlet路径
				}
			}else{
				return "<StrongOASession><Service><RtnCode>1</RtnCode><RtnMsg><Code>-1</Code><Message><![CDATA[在本地打开模块时，解析本地服务XML文件[" + file + "]错误：\n" + oErr.reason + "\nLine:" + oErr.line + "\nLinepos:" + oErr.linepos+"]]></Message></RtnMsg><Argument></Argument></Service></StrongOASession>";
			}
		}else{
			return doc.xml;
		}
	}
	if (location.protocol == "file:" && !IS_LOGIN) {
		this.serviceUrl = HOST+"/StrongOA/entry/EntryServlet";//后台servlet路径
	}
	//---------用作本地调试，正式环境去掉end

	var param = this.serviceParam;

	var search = location.search;
	//滤掉target
	var idx = search.indexOf("&target=");

	if (idx != -1) {
		var idx1 = search.indexOf("&", idx + 1);

		if (idx1 == -1)
			search = search.substring(0, idx);
		else
			search = search.substring(0, idx) + search.substring(idx1);
	}

	if (search == "") {
		search = "?" + param;
	} else if (search.indexOf(param) == -1) {
		search += "&" + param;
	}

	var action = this.serviceUrl + search;
	//alert("action:"+action);
	var res = null;
	data = "<?xml version=\"1.0\" encoding=\"GBK\"?>\n" + data;
	var xmlhttp = this.serviceHttp; 
	try {
		xmlhttp.Open("POST", action, this.serviceAsync);
		xmlhttp.setRequestHeader("Referer",location.href);
		if (this.serviceAsync) {
			if (this.serviceListener != null) {
				xmlhttp.onreadystatechange = this.serviceListener;
				window.status = "发送异步通讯请求...";
			}else{
				showMessage("Service类异常，无法得到异步处理的监听器");
				xmlhttp.onreadystatechange = new Function(alert("缺省监听器:\n您没有设置异步通讯的监听器！"));//new Function("缺省监听器");
			}
		}
		xmlhttp.Send(data);
		if (this.serviceAsync) {
			return null;
		}
		res = xmlhttp.responseText;
	} catch(e) {
		showMessage("不能连接到后台服务器：\n" + e.name + "\n" + e.message);
		window.status = "连接服务器异常！";
	}
	xmlhttp = null;
	return res;
}

/** [private]得到请求的数据，数据的名称，请求的ID
 * @param serviceName 服务名
 * @param reqData 请求的数据，即Argument节点的内容
 * @param serviceId service id,标识工作区
 * @return 请求的XML字符串
 */
function getRequest(serviceName, reqData, serviceId) {
	var reqStr = "<StrongOASession>\n"
				+ "  <Service id=\"" + serviceId + "\">\n"
				+ "    <Name>" + serviceName + "</Name>\n"
				+ "    <Argument>" + reqData + "</Argument>\n"
				+ "  </Service>\n"
				+ "</StrongOASession>";

	return reqStr;
}

/** [private]得到Argument节点的值，即输入参数
 *  参数格式：一一对应（键值对）
 *  例：getArgument('NSRSBH','777777777777715','PZXH','888888888888888')
 * @return Argument节点内容
 */
function getArgument() { return createArgument(getArgument.arguments); }

/** [private]生成Argument节点内容
 * @param arrKeyValue 键值对的数组
 * @return Argument节点内容
 */
function createArgument(arrKeyValue) {
	if (arrKeyValue.length == 1)
		return arrKeyValue[0];

	var strArgu = "";

	for (var i = 0; i < arrKeyValue.length - 1; i = i + 2) {
		//程序缺陷，虑掉NSRSBH里的非法字符
		if(arrKeyValue[i] == "NSRSBH"){
			var s = arrKeyValue[i + 1];
			s = s.replace(/&/g,"&amp;");
			s = s.replace(/</g,"&lt;");
			s = s.replace(/>/g,"&gt;");
			arrKeyValue[i + 1] = s;
		}
		strArgu += "<" + arrKeyValue[i] + ">" + arrKeyValue[i + 1] + "</" + arrKeyValue[i] + ">\n";
	}

	return strArgu;
}

/** [private]得到（封装）服务器的返回数据
 * @param doc 服务器返回的响应的doc
 * @return 需要的CDATA内的数据Str
 */
function processResponse(doc) {
	var result = new Array("1", "*", "请求数据时发生错误，可能由以下原因引起：\n1.浏览器不能连接到后台服务器\n（可能是由于长时间未与服务器连接而导致会话失效。）；\n2.服务器返回的数据格式错误。\n请稍候重新登录或与系统管理员联系。", "");

	//如果出现了致命的传输错误
	if (doc == null)
		return result;

	var root = doc.documentElement;

	if (root == null)
		return result;

	var service = root.selectSingleNode("Service");

	if (service == null)
		return result;

	var rtnCode = service.selectSingleNode("RtnCode").text;
	var rtnMsg = service.selectSingleNode("RtnMsg");

	var code = rtnMsg.selectSingleNode("Code").text;
	var message = rtnMsg.selectSingleNode("Message").text;

	var argument = service.selectSingleNode("Argument").text;

	result[0] = rtnCode;  //状态：0/1——成功/失败
	result[1] = code;     //状态代码
	result[2] = message;  //状态信息
	result[3] = argument; //返回数据

	return result;
}

/////////////////////////////以下为可供外部调用的函数方法/////////////////////

/** 解析XML字符串
 * @param str 要解析的XML字符串
 * @return document对象
 */
function loadXml(str) {
	if (str == null)
		return null;
	var doc = new ActiveXObject("MSXML.DOMDocument");
	doc.async = false;
	doc.loadXML(str);
	var oErr = doc.parseError;
	if (oErr.errorCode != 0) {
		if (str.length > 5 && str.substring(0,5) == "<!DOC") {
			return null;
		}else{
			alert("解析XML数据错误：\n" + oErr.reason + "\nLine:" + oErr.line + "\nLinepos:" + oErr.linepos + "\nsrcText:\n" + oErr.srcText);
		}
		return null;
	}
	return doc;
}

/** 解析XML文件
 * @param file 要解析的XML文件
 * @return document对象
 */
function loadFile(file) {
	var doc = new ActiveXObject("MSXML.DOMDocument");
	doc.async = false;
	doc.load(file);
	var oErr = doc.parseError;

	if (oErr.errorCode != 0) {
		alert("解析XML文件[" + file + "]错误：\n" + oErr.reason + "\nLine:" + oErr.line + "\nLinepos:" + oErr.linepos + "\nsrcText:\n" + oErr.srcText);

		return null;
	}

	return doc;
}

/** 得到页面所属工作区的SID，只能用于通过&lt:iframe&gt;节点继承的页面。<br>
 *  对于模式窗口无法通过top直接访问它的顶层窗口
 *  修改：对于模式窗口或其他不能访问的情况，以前返回null,现在自动创建一个新的ID，前缀："NEW-" DATE:20031107
 * @return 页面所属工作区的SID
 */
var LOCAL_WORKID = null;//单页面在本地打开时用于记录workid，正式运行时去掉
function getWorkId() {
	if (getSysConsole() != null) {
		return getSysConsole().getWorkId(window);
	} else {
		if (window.opener != null && getWinConsole(window.opener) != null){
			return getWinConsole(window.opener).getWorkId(window);//???
		}

		//------用于本地打开文件测试，正式运行时去掉start
		if (location.protocol == "file:") {
			if (LOCAL_WORKID == null) {
				LOCAL_WORKID = "LOCAL-"+createWorkId();
			}
			return LOCAL_WORKID;
		}
		//------用于本地打开文件测试，正式运行时去掉end

		return "NEW-"+createWorkId();
		//return null;
	}
}

/** 创建工作区ID，只是返回一个ID号，不过任何处理。目前，不要用这个函数
 * @return 工作区ID
 */
function createWorkId1() {
	alert("兼容性警告：\n不要调用[createWorkId1()]\n应该调用[createWorkId()]");

	if (getSysConsole() != null) {
		return getSysConsole().createWorkId();
	} else {
		alert("获取工作区SID错误：[createWorkId()]\n当前页面不在系统控制台框架内，\n或者您在模式窗口里调用了该函数。\n请注意：模式窗口无法通过top直接访问它的顶层窗口！\n这种情况下，请使用Service(serviceName,serviceId)");

		return null;
	}
}

/** 创建工作区ID，只是返回一个ID号，不过任何处理<br>
 *  因为该方法的调用一般在模式窗口内，如果调用top.FRM_RIGHT，则必须要求传给模式窗口top.FRM_RIGHT的引用，提交麻烦。
 * @return 工作区ID
 */
function createWorkId() {
	return Math.random().toString();
}

/** 关闭工作区
 *  关闭顺序：先处理window.onWorkClosing事件，如果事件函数返回为true则继续关闭工作区<br>
 *  事件注册方法：window.onWorkClosing = myFunctionName，如：window.onWorkClosing = go;
 * @param bl true/false是否强制退出
 */
function closeWork(bl) {
	if (getSysConsole() != null) {
		getSysConsole().closeWork(bl,window);
	}else if(top == self){
		window.close();
	}else {
		alert("关闭工作区错误：\n当前页面不在系统控制台框架内");
	}
}

/** 取消工作区，可以做一些公用的处理。每个模块在取消时需要调用，如果成功返回则可继续自己的业务
 *  如果允许取消则将修改标志置为false<br>
 *  <font color=red>同样当保存成功后，也必需将修改标志置为false</font>
 * @return true/false 执行成功/失败
 */
function cancelWork(){
	if (isChanged()) {
		if (confirm("当前工作区的内容已经发生变化，但还没有被保存。\n取消后没有被保存的数据将丢失。\n您确定要取消工作区吗？")) {
			notifyChange();
			return true;
		}else{
			return false;
		}
	}else{
		return true;
	}
}
/**
 * 提供简便方法，刷新工作区待办事宜，没有则打开一个新的
 */
function refreshTodo(){
	if (getSysConsole() != null) {
		getSysConsole().refreshTodo();
	}	
}
/** 通知当前工作区是否发生变化
 * @param bl true/false 通知当前工作区是否发生变化
 */
function notifyChange(bl) {
	if(!PAGE_LISTENER_ENABLED) return;
	//alert("当前工作区发生变化,请先保存！");
	if (getSysConsole() != null)
		getSysConsole().notifyChange(bl,window);
	else
		alert("通知工作区时发生错误：\n当前页面不在系统控制台框架内");
		//return false;
}

/** 当前工作区是否发生变化
 * @return true/false 当前工作区是否发生了变化
 */
function isChanged() {
	if(!PAGE_LISTENER_ENABLED) return false;
	if (getSysConsole() != null) {
		return getSysConsole().isChanged(window);
	} else {
		//alert("查询工作区改变状态时发生错误：\n当前页面不在系统控制台框架内");

		return false;
	}
}

/////////////////////////////////////////////////////////////////
/////////////////////////类Service////////////////////////////////

/** 类Service构造函数<br>
 *  支持同步/异步两种调用方式<br>
 *  var service = new Service("serviceName","sid:12345");
 *  <ol>
 *  <li>var myRes = service.doService(myArgus);
 *  <li>service.serviceAsync = true;//异步<br>
 *  service.serviceListener = myListener;//注册监听器<br>
 *  service.doService(myArgus);<br>
 *  //以下代码写在myListener函数里<br>
 *  //doResponse(Service对象,Service对象的Http对象的返回);<br>
 *  if(service.serviceHttp.readyState == 4){<br>
 *	&nbsp;&nbsp;var myRes = doResponse(service,service.serviceHttp.responseText);//得到返回<br>
 *  }
 *  </ol>
 * @param serviceName 服务名
 * @param serviceId 工作区标识[可选参数]
 * @return Service对象
 */
function Service(serviceName, serviceId) {
	//开发期检查，判断是否加载了workservice.js脚本文件，开发结束后屏蔽该功能
	//相应地屏蔽掉workservice.js里WORK_SERVICE
	//if (typeof (WORK_SERVICE) == "undefined") {
	//	alert("系统提示：\n请在该页面内加载workservice.js脚本文件！");
	//}

	if (getSysConsole() != null && window != getSysConsole() && window != getSysLeft()) {
		getSysConsole().setMessage("工作区消息：", 0,window);
	}
	this.modify = true;
	this.isDebug = isDebug;
	this.debug = this.isDebug();
	this.serviceName = serviceName;
	this.serviceId = serviceId;
	this.serviceUrl = "/StrongOA/entry/EntryServlet";
	this.serviceParam = "type=ipc&purpose=EntryService&module=Entry";
	this.serviceParse = true; 
	this.serviceAsync = false;
	this.serviceHttp = new ActiveXObject("Msxml2.XMLHTTP.4.0");//4.0
	this.serviceListener = null;
	this.servicePack = true;//用<StrongOA >...包装请求，对于soap则不需要

	if (Service.arguments.length == 1) {
		this.serviceId = getWorkId();
	}
	this.result = new Array("1","","","");
	//this.rtnCode = "1";
	//this.code = "";
	//this.message = "";
	this.doService = doService;
	this.close = close$;
	this.send = send;
	this.getRtnCode = getRtnCode;
	this.getCode = getCode;
	this.getMessage = getMessage;
	this.getResponse = getResponse;
}
/** 关闭工作区，加$为防止有重名函数
 */
function close$(){
	this.serviceName = "common.StrongOAMasterBndService.exit";
	this.doService();
	this.serviceHttp = null;
}
/** 关闭工作区
 * @param sid sid
 */
function closeService(sid){
	var service = new Service("common.StrongOAMasterBndService.exit",sid);
	service.doService();
	service = null;
}
/** 向控制台的TOP页面查询是否打开调试
 * @return true/false
 */
function isDebug(){
	var debug = false;
	if (!debug) {
		this.modify = false;
		var idx = document.cookie.indexOf("debug=");
		if (idx == -1) {
			return debug;
		}else{
			return document.cookie.charAt(idx+6) == 't';
		}
	}
	return debug;
}

/** 处理交互，返回Argument节点值
 *  输入参数的方式同getArgument('NSRSBH','777777777777715','PZXH','888888888888888')
 *  即：service.doService('NSRSBH','777777777777715','PZXH','888888888888888')或service.doService(xml)
 * @return Argument节点值
 */
function doService() {
   // beginLog(this.serviceName);
	var argu = createArgument(doService.arguments);
	var req = "";
	if (this.servicePack) {
		req = getRequest(this.serviceName, argu, this.serviceId);
	}else{
		req = argu;
	}
	if (this.debug) {
		var rtn = showMessage("发送数据：\n" + req, -1);

		if (rtn == null){
			return;
		}else{
			if (this.modify) {
				req = rtn.substring(6);
			}
		}
	}

	window.status = "正在请求数据，请稍候...";
	var res = this.send(req);
	if (!this.serviceAsync) {
		return doResponse(this,res);
	}else{
		return null;
	}
}

/** 将处理返回的过程提出来（是函数而非对象方法，以便异步的监听器能够方便调用），以便同步/异步都可单独调用。
 * @param obj Service对象。必需这样给出。因为对于异步，只能调用函数，不能调用方法。
 * @param res xmlhttp.responseText数据。对于同步则是send()方法返回的数据
 */
function doResponse(obj,res){
	if (obj.debug) {
		var rtn = showMessage("返回数据：\n" + res, -1);
		if (rtn == null) {
			return;
		}
		if (obj.modify) {
			res = rtn.substring(6);
		}
	}
	if (!obj.serviceParse){
		return res;
	}

	var oRes = loadXml(res);
	window.status = "处理服务器返回数据...";
	var result = processResponse(oRes);
	obj.result = result;
	window.status = "数据传输完毕！";
	return result[3];
}

/** 得到RtnCode节点值
 * @return RtnCode节点值
 */
function getRtnCode() { return this.result[0]; }

/** 得到Code节点值
 * @return Code节点值
 */
function getCode() { return this.result[1]; }

/** 得到Message节点值
 * @return Message节点值
 */
function getMessage() { return this.result[2]; }

/** 得到doService返回结果，更确切的说，是doResponse()的返回（对于异步通讯）
 * @return doService返回结果
 */
function getResponse(){	return this.result[3];}

////////////////////类Service定义结束///////////////////////////////

/////////////////////测试用类////////////////////////
/** 测试前台的日志类，将日志写入文件
 * @param logFile 日志文件名
 */
function Logger(logFile){
	var logger = getSysLeft();
	if (logger != null) {
		logger.AppletMain.initLogger();
		logger.AppletMain.setLogFile(logFile);
	}
	this.initLog = initLog;
	this.beginLog = beginLog;
	this.endLog = endLog;
}

/** 注明当前测试的记录名称，在每个测试前需要调用，以声明进行的是什么测试
 * @param testName 当前测试的内容描述
 */
function initLog(testName){
	var logger = getSysLeft();
	if (logger != null) {
		logger.AppletMain.initLog(testName);
	}
}

/** 标记开始测试记录
 * @param testName 当前测试的内容描述
 */
function beginLog(testName){
	if (testName != null) {
		initLog(testName);
	}
	var logger = getSysLeft();
	if (logger != null) {
		logger.AppletMain.beginLog();
	}
}
/** 标记结束测试记录，并写入测试日志文件
 */
function endLog(){
	var logger = getSysLeft();
	if (logger != null) {
		logger.AppletMain.endLog();
	}
}


/////////////////////测试用类定义结束////////////////////////

/** 检查纳税人识别号是否合法。由于目前不能确切的知道到底需要虑掉哪些字符，
 * 所以该项检查被忽略！！
 * @param nsrsbh 纳税人识别号字符串
 * @return true/false 是/否合法
 */
function checkNsrsbh(nsrsbh) {
	var iLen = nsrsbh.length;

	if (iLen == 0) {
		showMessage("纳税人识别号不能为空!");

		return false;
	}

	//修改人  ：王春涛  20050301
	//修改内容：修改纳税人识别号不为15位是只显示提示信息
	//修改前代码
	/*
	if (iLen < 15 || iLen > 20) {
		showMessage("纳税人识别号应是15~20位!");

		return false;
	}
	*/
	//修改后代码
	if (iLen < 15) {
		if (confirm("纳税人识别号小于15位，是否继续！")){
			return true;
		}else{
			return false;
		}
	}
	//修改结束



	/*if (nsrsbh.indexOf('*') != -1 || nsrsbh.indexOf('$') != -1 || nsrsbh.indexOf('~') != -1
		|| nsrsbh.indexOf(',') != -1 || nsrsbh.indexOf(';') != -1 || nsrsbh.indexOf('!') != -1 || nsrsbh.indexOf('+') != -1 || nsrsbh.indexOf('@') != -1 || nsrsbh.indexOf('#') != -1 || nsrsbh.indexOf('%') != -1 || nsrsbh.indexOf('^') != -1 || nsrsbh.indexOf('&') != -1) {//允许"-"号
		showMessage("纳税人识别号包含非法字符!");

		return false;
	}*/

	return true;
}

/** 导航函数，可用于控制页面工作区的Tab页
 * @param url 链接地址
 * @param title 显示在Tab标签的内容
 * @param bl 是否在新窗口打开工作区
 */
function navigate(url, title, bl) {
	if (getSysConsole() != null) {
        if((getSysConsole()!="undefined")&&(getSysConsole().location!="undefined")&&((getSysConsole().location+"").indexOf("nav_right.jsp")>0)){
            var id = new Date();
            addTab(id.getTime(), title, url);
        }else{
			getSysConsole().navigate(url, title, bl);
        }
	}else{
		alert("top.FRM_RIGHT为空，不能调用navigate()",1);
	}
}

/** 弹出提示窗口,所有页面内的alert()需替换成showMessage();
 *  功能：弹出提示，视消息级别flag，在工作区状态栏给出提示！
 * @param str 要显示的字符串
 * @param flag 消息级别:-1——调试信息；0——一般消息提示；1——警告信息[默认]；2——错误信息；3——致命错误
 */
function showMessage(str, flag) {
	if (flag == null) {
		flag = 1;
	}

	switch (flag) {
		case -1:
			//var url = location.href;

			//var idx = url.indexOf("/work/");
			//var file = url.substring(0, idx + 6) + "/public/htm/dlg_message.htm";
			var rtn = showModalDialog(URL_MESSAGE, new String(str), 'dialogWidth=480px;dialogheight=530px;status:no;help:no');
			//return (rtn == null) ? false : true;
			return rtn;
		/*
		case 0:
			alert(str,0);

			if (top.FRM_RIGHT != null)
				top.FRM_RIGHT.setMessage(str, 0,window);

			break;

		case 1:
			alert(str,1);

			if (top.FRM_RIGHT != null)
				top.FRM_RIGHT.setMessage(str, 1,window);

			break;
		case 2:
			alert(str,2);

			if (top.FRM_RIGHT != null)
				top.FRM_RIGHT.setMessage(str, 2,window);

			break;
		*/
		default:
			alert(str,flag);

			if (getSysConsole() != null)
				getSysConsole().setMessage(str, flag,window);

	}
}
/** 设置工作区状态栏信息
 * @param str 要显示的字符串
 * @param flag 消息级别:-1——调试信息；0——一般消息提示；1——警告信息[默认]；2——错误信息；3——致命错误
 */
function setMessage(str, flag){
	if (getSysConsole() != null)
		getSysConsole().setMessage(str, flag,window);
}
/**节点之间的数据复制。一般用于把doservice()返回的数据，映射到页面的相应的数据岛中。
*  @param oSrc 获得原数据的节点。
*  @param oTarget 一般为页面上的数据岛，也可以是个节点。
*  @param targetNodeName 目标节点名称。必须写出该节点的完整路径，如:ROOT/ITEMS
*/
function xmlNodeCopy(oSrc, oTarget, targetNodeName) {
	var oSrcNode = oSrc.getElementsByTagName(targetNodeName)(0);
	var oTargetNode = oTarget.getElementsByTagName(targetNodeName)(0);

	//如果没有子节点则视为单独的节点，直接对其赋值
	if (oSrcNode.childNodes.length == 0) {
		oTargetNode.text = oSrcNode.text;

		return;
	}

	//如果有子节点，为处理可增行的节点问题，将先删除目标节点的所有自节点，
	//然后将源节点的所有子节点拷贝到目标节点
	while (oTargetNode.childNodes.length > 0) {
		oTargetNode.removeChild(oTargetNode.childNodes(0));
	}

	for (var i = 0; i < oSrcNode.childNodes.length; i++) {
		oTargetNode.appendChild(oSrcNode.childNodes(i).cloneNode(true));
	}
}

/**此函数用于把输入的日期变成输入月的第一天的日期。
*  用于DataWindow内,例如：用户输入"2002-02-03",界面显示为"2002-02-01";
*  @param oDataWindow 数据窗口
*  @param obj 格式化日期的输入框
*/
function formatMonthFirstD(oDataWindow, obj) {
	var iRowNum = oDataWindow.getObjPoint(obj)[0];

	var iColNum = oDataWindow.getObjPoint(obj)[1]
	var tempDate = oDataWindow.getCellValue(iRowNum, iColNum);
	oDataWindow.setCellValue(iRowNum, iColNum, formatMonthFirst(tempDate));
}

/**此函数用于把输入的日期变成输入月的第一天的日期。
*  例如：用户输入"2002-02-03",界面显示为"2002-02-01";
*  @param strDate 要格式化日期值，字符型
*  @return 格式化后的月初值
*/
function formatMonthFirst(strDate) {
	var tempDate = strDate;

	var index1 = tempDate.lastIndexOf(".");
	var index2 = tempDate.lastIndexOf("-");

	if ((index1 != -1) || (index2 != -1)) {
		var index3 = index1;

		if (index2 > index1) {
			index3 = index2;
		}

		return tempDate.substring(0, index3 + 1) + "01";
	} else if ((index1 == -1) && (index2 == -1) && tempDate.length == 8) {
		return tempDate.substring(0, tempDate.length - 2) + "01";
	}
}

/**此函数用于把输入的日期变成输入月的最后一天的日期。
*  用于DataWindow内,例如：用户输入"2002-03-03",界面显示为"2002-03-31";
*  @param oDataWindow 数据窗口
*  @param obj 格式化日期的输入框
*/
function formatMonthLastD(oDataWindow, obj) {
	var iRowNum = oDataWindow.getObjPoint(obj)[0];

	var iColNum = oDataWindow.getObjPoint(obj)[1]
	var tempDate = oDataWindow.getCellValue(iRowNum, iColNum);
	oDataWindow.setCellValue(iRowNum, iColNum, formatMonthLast(tempDate));
}

/**此函数用于把输入的日期变成输入月的最后一天的日期。
*  例如：用户输入"2002-03-03",界面显示为"2002-03-31";
*  @param strDate 要格式化日期值，字符型
*  @return 格式化后的月末值
*/
function formatMonthLast(strDate) {
	var tempDate = strDate;

	var index1 = tempDate.lastIndexOf(".");
	var index2 = tempDate.lastIndexOf("-");
	var index11 = tempDate.indexOf(".");
	var index22 = tempDate.indexOf("-");

	//形式如2002-2-2,2002.2.3的解析
	if ((index1 != -1) || (index2 != -1)) {
		var index3 = index1;

		if (index2 > index1) {
			index3 = index2;
		}

		index33 = index11;

		if ((index22 < index11 && index22 != -1) || (index11 == -1 && index22 != -1)) {
			index33 = index22;
		}

		var dTempDate = new Date();
        dTempDate.setDate(1);
		dTempDate.setYear(parseInt(tempDate.substring(0, index33), 10));
		var iMonth=parseInt(tempDate.substring(index33 + 1, index3), 10);
		dTempDate.setMonth(iMonth);
		//设置当月的最后一天
		dTempDate.setDate(0);
		if (iMonth==1 || iMonth==3 || iMonth==5  || iMonth==7  || iMonth==8 || iMonth==10 || iMonth==12 ) //月份1有问题，直接拼串
		{
			return tempDate.substring(0, index3 + 1) + "31";
		}
		return tempDate.substring(0, index3 + 1) + dTempDate.getDate();
	//形式如20020202的解析
	} else if ((index1 == -1) && (index2 == -1) && tempDate.length == 8) {
		var dTempDate = new Date();
        dTempDate.setDate(1);
		dTempDate.setYear(parseInt(tempDate.substring(0, 4), 10));
		var iMonth=parseInt(tempDate.substring(4, 6), 10);
		dTempDate.setMonth(iMonth);
		//设置当月的最后一天
		dTempDate.setDate(0);
		if (iMonth==1 || iMonth==3 || iMonth==5  || iMonth==7  || iMonth==8 || iMonth==10 || iMonth==12)
		{
			return tempDate.substring(0, tempDate.length - 2) + "31";
		}
		return tempDate.substring(0, tempDate.length - 2) + dTempDate.getDate();
	}
}

/** 在xml里按关键字查找一个节点的值。
 *   @param parentNode 要查找节点的上级节点 
 *   @param keyNodeName 关键字节点的名称
 *   @param keyNodeValue 关键字节点的值
 *   @param resultNodeName 返回节点的名称 
 *   @return resultNodeValue 返回节点的值
 */
function findNodeValue(parentNode, keyNodeName, keyNodeValue, resultNodeName) {
	var resultNodeValue = "";

	for (var i = 0; i < parentNode.length; i++) {
		var snv = parentNode(i).getElementsByTagName(keyNodeName).item(0).text;

		if (snv == keyNodeValue) {
			resultNodeValue = parentNode(i).getElementsByTagName(resultNodeName).item(0).text;

			break;
		}
	}

	return resultNodeValue;
}

/** 在xml里按关键字查找一条节点记录。
*   @param parentNode 要查找节点的上级节点 
*   @param keyNodeName 关键字节点的名称
*   @param keyNodeValue 关键字节点的值
*   @return resultNode 返回符合条件的节点（与keyNode同级）记录，包括多个字段。
*/
function findNode(parentNode, keyNodeName, keyNodeValue) {
	var resultNode = null;

	for (var i = 0; i < parentNode.length; i++) {
		var snv = parentNode(i).getElementsByTagName(keyNodeName).item(0).text;

		if (snv == keyNodeValue) {
			resultNode = parentNode(i);

			break;
		}
	}

	return resultNode;
}

/**用于把一个日期string 转换成一个 Date 类型的值
*  @param strDate 要转换的字符串 ‘20020303’ 或 ‘2002-3-3’ 或 ‘2002.3.3’
*  @return 日期类型值
*/
function strToDate(strDate) {
	var tempDate = strDate;

	var index1 = tempDate.lastIndexOf(".");
	var index2 = tempDate.lastIndexOf("-");
	var index11 = tempDate.indexOf(".");
	var index22 = tempDate.indexOf("-");

	//形式如2002-2-2,2002.2.3的解析
	if ((index1 != -1) || (index2 != -1)) {
		var index3 = index1;

		if (index2 > index1) {
			index3 = index2;
		}

		index33 = index11;

		if ((index22 < index11 && index22 != -1) || (index11 == -1 && index22 != -1)) {
			index33 = index22;
		}
		var year = parseInt(tempDate.substring(0, index33), 10);
		var month = parseInt(tempDate.substring(index33 + 1, index3), 10)-1;
		var date = parseInt(tempDate.substring(index3 + 1, tempDate.length), 10);
		var dTempDate = new Date(year,month,date);
		return dTempDate;
	//形式如20020202的解析
	} else if ((index1 == -1) && (index2 == -1) && tempDate.length == 8) {

		var year = parseInt(tempDate.substring(0, 4), 10);
		var month = parseInt(tempDate.substring(4, 6), 10) - 1;
		var date = parseInt(tempDate.substring(6, 8), 10);
		var dTempDate = new Date(year,month,date);
		return dTempDate;
	}
}

/** 把一个日期类型的值转换成，按一定格式格式化的字符串。（目前只做‘yyyy-mm-dd’）
*   @param dDate 要格式化的日期值,为Date类型 
*   @return 格式化后的日期字符串
*/
function formatDate(dDate) {
	var iMonth = (dDate.getMonth() + 1);

	var sMonth = iMonth.toString();

	if (sMonth.length == 1) {
		sMonth = "0" + sMonth;
	}
	//add by zhanggx 041020
	var sDate = dDate.getDate().toString();
	if (sDate.length == 1) {
		sDate = "0" + sDate;
	}

	return dDate.getFullYear() + "-" + sMonth + "-" + sDate;
}

/** 用于格式化xml文件的节点值
*   只针对<>33,333,333.00<>数值节点，转换为<>33333333.00<>
*   <>200.200,000%<> 转换为<>2.00200000<>
*   @param xmlId 页面数据岛的ID
*   @param nodeName 要格式化节点的上级节点
*   @return 格式化后的xml数据
*/
function formatXML(xmlId, nodeName) {
	formatNode(xmlId.getElementsByTagName(nodeName));

	return xmlId.xml;
}

/** 只针对<>33,333,333.00<>数值节点，转换为<>33333333.00<>
*   <>200.200,000%<> 转换为<>2.00200000<>
*   不能用在<>sss,ssss,ss<>的节点。  
*   @param obj 要格式化的xml节点的上级节点。 
*    
*/
function formatNode(obj) {
	for (var j = 0; j < obj.length; j++) {
		for (var i = 0; i < obj(j).childNodes.length; i++) {
			var sValue = obj(j).childNodes(i).text;

			if (sValue.indexOf(",") == -1 && sValue.indexOf("%") == -1) {
				continue;
			}

			var sResult = "";

			if (sValue.indexOf(",") != -1) {
				var aValue = sValue.split(",");

				for (var j1 = 0; j1 < aValue.length; j1++) {
					sResult = sResult + aValue[j1];
				}
			}

			if (sResult == "") {
				sResult = sValue;
			}

			if (sValue.indexOf("%") != -1) {
				var sResult1 = parseFloat(sResult, 10) / 100;

				sResult = sResult1.toString();
			}

			obj(j).childNodes(i).text = sResult;
		}
	}
}

/** 格式化一个节点的值
*   @param obj 要格式化的节点
*/
function formatNodeValue(obj) {
	var sValue = obj(0).text;

	obj(0).text = formatValue(sValue);
}

/** 格式化一个33,333,333.00或200.200,000%的值
*   为33333333.00，2。00200000
*   @param srcStr 要格式化的字符串
*   @return 格式化后的值
*/
function formatValue(srcStr) {
	var sValue = srcStr;

	if (sValue.indexOf(",") == -1 && sValue.indexOf("%") == -1) {
		return sValue;
	}

	var sResult = "";

	if (sValue.indexOf(",") != -1) {
		var aValue = sValue.split(",");

		for (var j = 0; j < aValue.length; j++) {
			sResult = sResult + aValue[j];
		}
	}

	if (sResult == "") {
		sResult = sValue;
	}

	if (sValue.indexOf("%") != -1) {
		var sResult1 = parseFloat(sResult, 10) / 100;

		sResult = sResult1.toString();
	}

	return sResult;
}

/** 把一个浮点数，以小数点后几位四舍五入  
*   @param srcValue 要舍位的值
*   @param iCount  要舍位到小数点后几位
*   return 四舍五入后的数
*/
function round(srcValuef, iCount) {
	var srcValue=srcValuef;
	var zs=true;
	//判断是否是负数
	if (srcValue<0)
	{
        srcValue=Math.abs(srcValue);
		zs=false;
	}
	var iB = Math.pow(10, iCount);
	//有时乘100结果也不精确
    var value1=srcValue * iB;

    var anumber=new Array();
    var anumber1=new Array();

    var fvalue=value1;
	var value2=value1.toString();
    var idot=value2.indexOf(".");
    //如果是小数
	if (idot!=-1)
	{
        anumber=srcValue.toString().split(".");
		//如果是科学计数法结果
		if (anumber[1].indexOf("e")!=-1)
		{
            return Math.round(value1)/iB;
		}
	    anumber1=value2.split(".");
        if (anumber[1].length<=iCount)
        {
			return parseFloat(srcValuef,10);
        } 
		var fvalue3=parseInt(anumber[1].substring(iCount,iCount+1),10);
		if (fvalue3>=5)
		{
			fvalue=parseInt(anumber1[0],10)+1;
		} else {
			//对于传入的形如111.834999999998 的处理（传入的计算结果就是错误的，应为111.835）
			if (fvalue3==4 && anumber[1].length>10 && parseInt(anumber[1].substring(iCount+1,iCount+2),10)==9 )
			{
                fvalue=parseInt(anumber1[0],10)+1;
			} else {
				fvalue=parseInt(anumber1[0],10);
			}
		}
	}
    //如果是负数就用0减四舍五入的绝对值
	if (zs)
	{
		return fvalue/iB;
	} else {
		return 0-fvalue/iB;
	}
}

/** 把数值转换为大写金额
*   @param num 数值，为数值或字符型
*   @return 大写金额字符
*/
function convertCN(num) {
	var arr1 = new Array("仟", "佰", "拾", "亿", "仟", "佰", "拾", "万", "仟", "佰", "拾", "圆", "点", "角", "分");

	var arr3 = new Array("零", "壹", "贰", "叁", "肆", "伍", "陆", "柒", "捌", "玖");
	num = num.toString();
	//判断是否为负
	var bfs=false;
	if (num.indexOf("-")!=-1)
	{
		num=num.substring(1,num.length);
		bfs=true;
	}
	var point = num.indexOf(".");

	if (point < 0)
		point = num.length;

	var len = arr1.length - point - 3;
	var strRet = "", lastChar = "";

	for (i = 0; i < num.length; i++) {
		if (i != point) {
			val = arr1[i + len];
			if (num.charAt(i) != "0" || (i==point-1 && point==1) || (i==point+1 && num.charAt(i) == "0" && num.charAt(i+1) != "0") )
				strRet += arr3[num.charAt(i)] + val;
			else {
				lastChar = strRet.substr(strRet.length - 1);
				if (val == "亿" || val == "万" || val == "圆" || val == "分") {
					if (lastChar == "零")
						strRet = strRet.substr(0, strRet.length - 1);

					lastChar = strRet.substr(strRet.length - 1);

					if (!((val == "万" && lastChar == "亿") || (val == "分" && lastChar == "角") || (val == "角" && lastChar == "圆")))
						strRet += val;
				} else {
					if (lastChar != "零")
						strRet += "零";
				}
			}
		}
	}

	//去分
	var ifen = strRet.indexOf("分");

	if (ifen >= 0) {
		var sffen = strRet.substring(ifen - 1, ifen);

		if (sffen == "圆") {
			strRet = strRet.substring(0, ifen);
		}
	}

	//加整
	var sfyuan = strRet.substring(strRet.length - 1, strRet.length);

	if (sfyuan == "圆" || sfyuan == "角") {
		strRet = strRet + "整";
	}
    
	//加负
    if (bfs)
    {
		strRet="（负数）"+strRet;
    }
	return strRet;
}

/** 使页面的层&lt;DIV&gt;显示/隐藏<br>
 *  要求页面的层&lt;DIV&gt;用如下方法引用：style="display:none"或style="display:"<br>
 *  调用方法：showHideLayers("Layer1","show","Layer2","hide")，即：方法成对出现，参数1为层ID，参数2为是否隐藏
 */
function showHideLayers() {
	var args = showHideLayers.arguments;

	for (var i = 0; i < args.length - 1; i += 2) {
		var isShow = (args[i + 1] == "show") ? "''" : "'none'";

		var script = args[i] + ".style.display=" + isShow;
		eval(script);
	}
}
/** 显示/隐藏某一区域（层）
 * @param oLayer 区域（层）名，为对象，并非字符串
 * @param oImg 图片区域对象名&lt;img id="img_switch" src="img_open"&gt;
 * @param sImg1 区域显示时的图片
 * @param sImg2 区域隐藏时的图片
 */
function switchLayer(oLayer, oImg, sImg1, sImg2) {
	var flag = oLayer.style.display;

	if (flag == "none") {
		oLayer.style.display = "block";

		oImg.src = sImg1;
	} else {
		oLayer.style.display = "none";

		oImg.src = sImg2;
	}
}

/** 按xml中的某一节点进行对xml排序 类似这种结构（ROOT/ITEM/NODENAME...）
*   @param dsoName 数据岛名称
*   @param colNodeName 列对应数据结点名称
*   @param orderType ture/false 升序/降序
*/
function xmlOrderBy(dsoName, colNodeName, orderType) {
	var root = dsoName.documentElement.cloneNode(true);

	var rootTemp = dsoName.createNode("element", root.nodeName, "");
	var ilen = root.childNodes.length;

	//排序
	for (var i = 0; i < ilen; i++) {
		var node1 = root.childNodes(0);

		var value1 = root.childNodes(0).getElementsByTagName(colNodeName)(0).text;
		var ilen1 = root.childNodes.length;

		for (var j = 0; j < ilen1; j++) {
			if (orderType) { //升序		
				if (root.childNodes(j).getElementsByTagName(colNodeName)(0).text < value1) {
					node1 = root.childNodes(j);
				}
			} else {
				//降序
				if (root.childNodes(j).getElementsByTagName(colNodeName)(0).text > value1) {
					node1 = root.childNodes(j);
				}
			}
		}

		rootTemp.appendChild(node1);
	}

	//替换成排序后的xml
	dsoName.removeChild(dsoName.documentElement);
	dsoName.appendChild(rootTemp);
}

/** 删除数据窗口中关键数据为空的行
*   @param dwid 数据窗口
*/
function deleteNullRow(dwid) {
	var iRowNum = dwid.getRowCount();

	var iColNum = dwid.getColCount();

	for (var i = iRowNum - 1; i >= 0; i--) {
		for (var j = 0; j < iColNum; j++) {
			if (dwid.col(j).fill == true && dwid.row(i).cell(j).value.toString() == "") {
				dwid.deleteRow(i);
				break;
			}
		}
	}
}

/** 检查日期时间的合法性 必须格式为“2003-09-09 或 2003-9-09 ”才返回ture,否则返回false
*   @param str 要检查的日期字符串。
*   @return true/false 
*/
function checkDate(str){		 
    var reg = /^(\d+)-(\d{1,2})-(\d{1,2})$/; 
    var r = str.match(reg); 
    if(r==null)return false; 
    r[2]=r[2]-1; 
    var d= new Date(r[1], r[2],r[3]); 
    if(d.getFullYear()!=r[1])return false; 
    if(d.getMonth()!=r[2])return false; 
    if(d.getDate()!=r[3])return false; 
    return true;
}

/** 检查日期时间的合法性 必须格式为“2003-09-09 08:08:08”才返回ture,否则返回false
*   @param str 要检查的日期时间字符串。
*   @return true/false 
*/
function checkDateTime(str){		 
    var reg = /^(\d+)-(\d{1,2})-(\d{1,2}) (\d{1,2}):(\d{1,2}):(\d{1,2})$/; 
    var r = str.match(reg); 
    if(r==null)return false; 
    r[2]=r[2]-1; 
    var d= new Date(r[1], r[2],r[3], r[4],r[5], r[6]); 
    if(d.getFullYear()!=r[1])return false; 
    if(d.getMonth()!=r[2])return false; 
    if(d.getDate()!=r[3])return false; 
    if(d.getHours()!=r[4])return false; 
    if(d.getMinutes()!=r[5])return false; 
    if(d.getSeconds()!=r[6])return false; 
    return true;
}

////////////////内部协查管理//////////////////
///////////////////////////////////////////////
/**通过本页面的nbxc_check(NSRSBH,YWHJ_DM,MKMC,HDNR)方法调用弹出窗口。
* @param NSRSBH 纳税人识别号
* @param YWHJ_DM 业务环节代码
* @param MKMC 模块名称
* @param HDNR 内容
* @param serviceName 服务名，如："common.StrongOAMasterBndService.commonNsrEntered"
* @param GZBZ 更正标志 //cheys 2004/02/17 added
* @return  true/false 成功/失败
*/

function defaultNsrEntry(NSRSBH,YWHJ_DM,MKMC,HDNR,serviceName,GZBZ,workId)
{

	var service = null;
	if (workId == null) {
		service = new Service(serviceName);
	}else{
		service = new Service(serviceName,workId);
	}
//	var sArgu=service.doService("NSRSBH",NSRSBH,"HDNR",HDNR,"MKMC",MKMC,"YWHJ",YWHJ_DM);
	if (GZBZ != null)
	{
		var sArgu=service.doService("NSRSBH",NSRSBH,"HDNR",HDNR,"MKMC",MKMC,"YWHJ",YWHJ_DM,"GZBZ",GZBZ);
	}
	else{
		var sArgu=service.doService("NSRSBH",NSRSBH,"HDNR",HDNR,"MKMC",MKMC,"YWHJ",YWHJ_DM);
	}
	var rtnCode=service.getRtnCode();
	if (rtnCode=="0")
	{
		  var oDoc=loadXml(sArgu);//解析xml
		  var tbtjts = oDoc.selectSingleNode("//TBTJTS").text;
		  if(tbtjts != "null"){
			  showMessage(tbtjts);
		  }
		  var isXcExisted = oDoc.selectSingleNode("//DO_XC").text;
		  var oNode = oDoc.selectSingleNode("//NBXCXX");
		  if(isXcExisted=="Y"){
			 return	nbxc_check(NSRSBH,YWHJ_DM,MKMC,HDNR,oNode);		
		  }
	}else{
		showMessage(service.getMessage());
		return false;
	}
 }
 
/**通过本页面的nbxc_check(NSRSBH,YWHJ_DM,MKMC,HDNR)方法调用弹出窗口。
* @param NSRSBH 纳税人识别号
* @param YWHJ_DM 业务环节代码
* @param MKMC 模块名称
* @param HDNR 内容
* @param oDoc XML内容
* @return  true/false 成功/失败
*/
  function nbxc_check(NSRSBH,YWHJ_DM,MKMC,HDNR,oDoc)
 {
	var sid = createWorkId(); 
	var service = new Service("jcfz.nx.nx_check.NxCheckBndService.init",sid);
	var sArgu=service.doService("NSRSBH",NSRSBH,"YWHJ_DM",YWHJ_DM,"MKMC",MKMC,"HDNR",HDNR,"NBXC","N");
	var rtnCode=service.getRtnCode();
	if (rtnCode=="1")
	{
	   showMessage("检查该纳税人内部监控失败!");
	   return false;
	}

	  var count=oDoc.childNodes.length;
	  if (count!=0)
	  {

		  for (var i=0;i<count ;i++ )
		  {
			  var NBXCXH=oDoc.getElementsByTagName("NBXCXH")(i).text;
			  var scontinue=oDoc.getElementsByTagName("CONTINUE")(i).text;
			  var write_jkrz=oDoc.getElementsByTagName("WRITEJKRZ")(i).text;
			  var paraArray = new Array();
			  paraArray[0]=NBXCXH;
			  paraArray[1]=scontinue;
			  //var serviceId = getWorkId();
			  paraArray[2]=sid;
			  paraArray[3]=write_jkrz;
			  
			  var isContinue=showModalDialog(URL_NBXC,paraArray,'dialogWidth=600px;dialogheight=300px;status:no;help:no');
			  //如果点"关闭"按钮,那么视同终止操作
			  if(isContinue=="STOP"){
				  return false;
			  }
			  if (isContinue==null)
			  {
				  if (write_jkrz=="FALSE")
				  {
					 return false;
				  }
				  var service = new Service("jcfz.nx.nx_check.NxCheckBndService.saveJkrz",sid);
				  var sArgu=service.doService("NBXCXH",NBXCXH,"CZFS","STOP");
				  var rtnCode=service.getRtnCode();
				  if (rtnCode=="0")
				  {
					  showMessage(service.getMessage());
					  return false;
				  }else{
					  showMessage(service.getMessage());
					  return false;
				  }
			  }
		  }
	  }

	  return true;
}



/**不走界面服务流程，直接通过本页面的nbxc_check(NSRSBH,YWHJ_DM,MKMC,HDNR)方法调用弹出窗口。
* @param NSRSBH 纳税人识别号
* @param YWHJ_DM 业务环节代码
* @param MKMC 模块名称
* @param HDNR 内容
* @return  true/false 成功/失败
*/
 function interface_nbxc_check(NSRSBH,YWHJ_DM,MKMC,HDNR)
 {
	if(interface_nbxc_check.arguments.length!=4){
		showMessage("调用方法参数个数不等于４!")
		return false;
	}
	if (NSRSBH=="")
	{
		showMessage("纳税人识别号不能为空！");
		return false;
	}
	if (YWHJ_DM=="")
	{
		showMessage("业务环节代码不能为空！");
		return false;
	}
	if (MKMC=="")
	{
		showMessage("模块名称不能为空！");
		return false;
	}
	var service = new Service("jcfz.nx.nx_check.NxCheckBndService.init");
	var sArgu=service.doService("NSRSBH",NSRSBH,"YWHJ_DM",YWHJ_DM,"MKMC",MKMC,"HDNR",HDNR,"NBXC","Y");
	var rtnCode=service.getRtnCode();
	if (rtnCode=="0")
	{
		  var oDoc=loadXml(sArgu);//解析xml
		  var count=oDoc.documentElement.childNodes.length;
		  if (count!=0)
		  {
			  for (var i=0;i<count ;i++ )
			  {
				  var NBXCXH=oDoc.getElementsByTagName("NBXCXH")(i).text;
				  var scontinue=oDoc.getElementsByTagName("CONTINUE")(i).text;
				  var write_jkrz=oDoc.getElementsByTagName("WRITEJKRZ")(i).text;
				  var paraArray = new Array();
				  paraArray[0]=NBXCXH;
				  paraArray[1]=scontinue;
				  var serviceId = getWorkId();
				  paraArray[2]=serviceId;
				  paraArray[3]=write_jkrz;
				  
				  var isContinue=showModalDialog(URL_NBXC,paraArray,'dialogWidth=600px;dialogheight=300px;status:no;help:no');
				  //如果点"关闭"按钮,那么视同终止操作
				  if(isContinue=="STOP"){
					  return false;
				  }
				  if (isContinue==null)
				  {
					  if (write_jkrz=="FALSE")
					  {
						 return false;
					  }
					  var service = new Service("jcfz.nx.nx_check.NxCheckBndService.saveJkrz");
					  var sArgu=service.doService("NBXCXH",NBXCXH,"CZFS","STOP");
					  var rtnCode=service.getRtnCode();
					  if (rtnCode=="0")
					  {
						  showMessage(service.getMessage());
						  return false;
					  }else{
						  showMessage(service.getMessage());
						  return false;
					  }
				  }
				  
			  }
		  }
		  return true;
	}else if (rtnCode=="1")
	{
	   showMessage("检查该纳税人内部监控失败!");
	   return false;
	}
}
/////////////内部协查管理////////////////

/**
*自动更正输入错误
*此函数用于 会计期间或页码输入框 的 onchange事件或onblur 事件中,当用户输入错误的值后,自动更正错误
*@param obj 输入框
*@param minValue 最小值
*@param maxValue 最大值
*/
function gzInput(obj,minValue,maxValue){
	var sValue = parseInt(obj.getValue(),10);
	if (sValue > maxValue )
	{
		obj.setValue(maxValue);
	}
	if (sValue < minValue)
	{
		obj.setValue(minValue);
	}
}

/**
*检查控件的输入值是否在指定的期间内，用于提交请求之前的合法性检查
*@param obj 输入框
*@param minValue 最小值
*@param maxValue 最大值
*@return true|false 正确|错误
*/
function checkInput(obj,minValue,maxValue){
	var sValue = parseInt(obj.getValue(),10);
	if (sValue > maxValue || sValue <minValue)
	{		
		if (!obj.disabled)// 如果没有被disable,则移焦点过来。
		{
			obj.select();
			obj.focus();
		}
		return false;
	}
	return true;
}


/** 调入Web页打印预览页面
 * @param pagesize 纸张：目前为：A2,A3,A4,A5，如果纸张超过A2横向将不予调整，如果为null则自动调整
 * @param pageori 打印方向：0/1,纵/横，如果为null则自动调整
 * @param header 页眉（参考IE的打印设置）：&b&b&d
 * @param footer 页脚（参考IE的打印设置）：&b&p/&P&b
 * @param margin 页边距（毫米，参考IE的打印设置）：用“|”线分开的4个值，分别代表上右下左四个边距（与CSS的margin等属性规则相同）。
 *	如果只有2个值，则上下边取第一个值；左右边取第二个值
 * @param fontsize 字号（pt），如果不需要该参数，可将其设为null或者干脆不写该参数
 * @param caller 将回调的函数，默认为interface_getHTML()，因为同一页面内可能需要不同的打印接口函数，所以需要能够指定
 * @param toword 有这个参数时，打印页面将显示toword按钮，点该按钮将回掉toword对应的函数，默认将event传给该函数
 * @return window对象
 */
function toPrinter(pagesize,pageori,header,footer,margin,fontsize,caller,toword){
	var argus = caller == null? "?" : "?caller="+caller+"&";
	if (toword != null) {
		argus += "toword="+toword+"&";
	}
	argus += "pagesize="+pagesize+"&pageori="+pageori
		+"&pageheader="+header+"&pagefooter="+footer+"&pagemargin="+margin;
	if (fontsize != null && fontsize != "") {
		argus += "&fontsize="+fontsize;
	}
	var wh = "width="+(screen.width-11)+",height="+(screen.height-60);
	var param = "toolbar=no,location=no,status=yes,resizable=no,scrollbars=yes,top=0,left=0,"+wh;
	return window.open(URL_TO_PRINT+argus,"_blank",param);
}

/** 调入Web页Excel输出
 * @return window对象
*/
function toExcel(){
	var wh = "width="+(screen.width-11)+",height="+(screen.height-55);
	var param = "toolbar=no,location=no,status=yes,resizable=no,scrollbars=yes,top=0,left=0,"+wh;
	return window.open(URL_TO_EXCEL,"_blank",param);
}

/** 校验并格式化日期 （用户录入 2003－8－1 ，先进行日期校验 checkData()  ,再进行格式化 formatData() 2003-08-01 ）
 * @param obj 需要校验的日期对象。
 */
function check_format_date(obj) {
	vdate=obj.value;
	if (!obj.checkData())
	{
		if (obj.value.length==10)
		{
			showMessage("录入日期 '"+vdate+"' 非法，请重新录入。");
			obj.value="";
			return false;
		}
		obj.value=obj.formatData(obj.value);
	}
	if (!obj.checkData())//
	{
		showMessage("录入日期 '"+vdate+"' 非法，请重新录入。");
		obj.value="";
		return false;
	}
	return true;
}
//--------------------------------------------------------------------------------------
/** 设置审批意见的值（2003-9-4）//返回带同意和不同意的字符串
*@param obj 审批意见的值 （str）
*@param flag 点击同意或不同意按钮 （同意:Y  、不同意:N）
*/
function setSpyjValue(obj,flag)
{
	if (flag=="Y"){
		if (obj=="")//如果为空则直接填写“ 同意”
		{
			obj="同意";
		}else{
			if (obj.indexOf("不同意")==0)  //如果是“ 不同意”则删除不字。
			{
				var iYjLen=obj.length;
				obj = obj.substring(1,iYjLen);
			}else if (obj.indexOf("同意")==0)
			{
				
			}else{
				obj ="同意，"+obj.substring(0,iYjLen);//如果没有“同意”则添加。
			}
		}
	}else if (flag=="N"){
		if (obj=="")
		{
			obj="不同意";
		}else{
			if (obj.indexOf("同意")==0)
			{
				var iYjLen=obj.length;
				obj = "不"+obj.substring(0,iYjLen);
			}else if (obj.indexOf("不同意")==0)
			{
			
			}else{
				obj ="不同意，"+obj.substring(0,iYjLen);
			}
		}
	}
	return obj;
}
//--------------------------------------------------------------------------------------
/**此函数用于把输入的日期变成输入上一个月的最后一天的日期。
*  例如：用户输入"2002-03-03",界面显示为"2002-02-31"; 2003-01-16  界面显示为 2002-12-31
*  @param strDate 要格式化日期值，字符型 格式必须为 yyyy-mm-dd
*  @return 格式化后的月末值
*/
function formatPeriorMonthLast(strDate) {
	var dStr=strDate;
	mm=dStr.split('-');
	var tempStr="";
	if (mm[1]=='01'||mm[1]=='1')
	{
		dStr=parseInt(dStr.substring(0,4),10)-1+"-12-31";
		return dStr;
	}else{
		if (parseInt(mm[1],10)-1>9)
		{
			tempStr=parseInt(mm[1],10)-1;
		}else{
			tempStr=parseInt(mm[1],10)-1;
			tempStr="0"+tempStr;
		}
		tempdate=dStr.substring(0,5)+tempStr+'-01';
		dStr=formatMonthLast(tempdate);
		return dStr;
	}
}
////////////////注册表编辑类start//////////////////////
/**
 * 注册表编辑器，封装对注册表的操作
 */
function RegEdit(){
	this.shell = new ActiveXObject("WScript.Shell");
	this.regRead = regRead;
	this.regWrite = regWrite;
	this.regDelete = regDelete;
}

/** 返回名为 strName 的注册键或值。
 * @param strName 要读取的键或值。如果 strName 以反斜线 (\) 结束，本方法将返回键，而不是值
 * @return 名为 strName 的注册键或值
 */
function regRead(strName){
	var val = null;
	try {
		val = this.shell.regRead(strName);
	} catch (e) {
		//alert(e.message);
	}
	return val;
}

/** 设置 strName 指定的注册键或值
 * @param strName 要写的键或值的名称.如果 strName 以反斜线 (\) 结束，本方法将返回键，而不是值
 * @param anyValue 要写入键或注册表值中的值
 * @param strType 可选项。要保存到注册表中的值的数据类型REG_SZ、REG_EXPAND_SZ、REG_DWORD、REG_BINARY
 */
function regWrite(strName,anyValue,strType){
	if(strType == null)
		strType = "REG_SZ";
	this.shell.regWrite(strName,anyValue,strType);
}

/** 从注册表中删除 strName 指定的键或值。
 * @param strName 要删除的键或值的名字。如果 strName 以反斜线 (\) 结束，本方法将删除键，而不是值
 */
function regDelete(strName){
	this.shell.regDelete(strName);
}

////////////////注册表编辑类end//////////////////////



///印章管理，返回当前操作人员使用的印章/////////////////////
/**  公用函数
*/

var SIGNATUREPATH="c:\\StrongOA\\signature\\";
function getPicture(zsr_dm){
	var sid = createWorkId();
	var svc = new Service("util.yzgl.YzInterfaceBndService.init",sid);
	var oArg=svc.doService("ZSR_DM",zsr_dm);
	if (svc.getCode() != "2000")
	{
		showMessage(svc.getMessage());
		return;
	}else{		
		var oDoc = loadXml(oArg);		
		if (oDoc.selectSingleNode("ROOT/SIGNATUREID").text == "")
		{
			return "";
		}else{
			return SIGNATUREPATH + oDoc.selectSingleNode("ROOT/SIGNATUREID").text+".jpg";
		}
	}
}

function getWszsr_dm(swws_dm,wspzxh)
{
	var sid = createWorkId();
	var service = new Service("jcfz.common.wszsr.WszsrBndService.init",sid);
	var srAuge = service.doService("SWWS_DM",swws_dm,"WSPZXH",wspzxh);
	var msg = service.getMessage();
	var code = service.getCode();
	if (code=="2000")
	{
		//解析xml
		var oDoc = loadXml(srAuge);
		return oDoc.selectSingleNode("ROOT").text;
	}
	else
	{
		showMessage(msg);
		return ""; 
	}
}

/** 将XML转换到DOT文档模版内
 * @param doc XML Document
 * @param swws_dm 税务文书代码
 * @param bz 稽查还是违章
 */
function toWord(doc,swws_dm,bz,zsr_dm){
	var ole = null;
	try {
		ole = new ActiveXObject("Word.Application");
		try{
			if (typeof(bz)=="undefined"||bz=="")
			{
				ole.documents.add("c:/StrongOA/dot/" + swws_dm + ".doc");
			}else if (bz=="WZ")
			{
				ole.documents.add("c:/StrongOA/dot/" + swws_dm + "_wz.doc");
			}else if (bz=="JC")
			{
				ole.documents.add("c:/StrongOA/dot/" + swws_dm + "_jc.doc");
			}else if (bz=="WS")
			{
				ole.documents.add("c:/StrongOA/dot/" + swws_dm + ".doc");
			}else 
			{
				ole.documents.add("c:/StrongOA/dot/" + swws_dm + ".doc");
			}
		} catch(e2) {
			try{
				ole.documents.add("c:/StrongOA/dot/" + swws_dm + ".doc");
			}catch(e1){
				if (ole != null) {
					try {
						ole.quit();
					} catch (e) {
					}
				}
				showMessage("新建Word异常:"+e1.message,0);
			}
		}
		ole.AddIns.add("c:/StrongOA/dot/StrongOAWs.dot");
		ole.Visible = true;
		if(doc==null)
		{
			ole.run("toWord",null);	
		}else{
			ole.run("toWord",doc.documentElement);
			if (typeof(zsr_dm)=="undefined"||zsr_dm=="")
			{
				ole.run("RemoveSignatureLabel");
			}else{
				ole.run("RemoveSignatureLabel");
			/** 王珂锋 删除此块 总局版本不需要电子签章  d
				var picName = getPicture(zsr_dm);
				if(picName != ""){
				  ole.run("AddSignature",picName);
				}else{
				  showMessage("没有定义机关签章!",0);
				  //return;
				}
			*/
			}
		}
		try{
		//设置文书的模板为只读
		if(bz=="WS")
           	ole.run("setReadOnly","eo5w3960ie");
		}catch(ee){}
	} catch (e) {
		if (ole != null) {
			try {
				ole.quit();
			} catch (e) {
			}
		}
		showMessage("新建Word异常:"+e.message,0);
	}
}

var TZDNODE =null;
/**申报模块加打印通知单功能
*/
function sb_print_wfwztzd(oDoc){
	TZDNODE = oDoc;
	toPrinter("A4","0",null,null,"20|20|20|20",null,"interface_printTzd");

}

/**toPrinter函数的接口函数
*/
function interface_printTzd(){
	var oDoc = loadFile(URL_TZD);	//样式表
	var oNode =TZDNODE;

	if (oNode == null)
	{
		return null;
	}
	return oNode.transformNode(oDoc);
}

/** 得到UNICODE字符串的实际长度，单个中文字符长度为2
 * @param str 字符串
 * @return 长度
 */
function getStrByteLength(str){
    var len = str.length;
    for(var i=0;i < str.length;i++){
		if(str.charCodeAt(i) > 255){
			len ++;
		}
	}
	return len;
}

/**移动日期
 * @param sDate 日期 yyyy-mm-dd
 * @param iDays 正向后，负向前
 * @return 长度
* andm 2005-08-29 
*/
function moveDay(sDate,iDays){
	var date = strToDate(sDate);
	date.setDate(date.getDate()+iDays);
	return formatDate(date);
}
