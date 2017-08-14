// 流程模型类
// 为了方便扩展流程验证而创建，类内容为空
function processModel(){
	
}
/**
 * 流程模型整体验证Filter设置
 */
var processValidateFilters = [];
// 添加流程模型的默认验证Filter
// 1.如果验证正确，则接口返回true
// 2.如果验证失败，则接口直接alert错误信息，并返回false
processValidateFilters.push(function(){
	var nodeIds = this;
	var isValidated = true;
	
	// 判断是否有结束节点
  	var hasNoEndNodeFlag = true;
  	// 判断除开始和结束节点外的每个节点是否都具有进入迁移和离开迁移
  	var nodeHasNoTransFlag = false;
  	var noSource = 0;
	var noTarget = 0;
	var conditionFlag = "";
	for (var i=0; i<nodeIds.length; i++) {
		var objid = nodeIds[i].value;
		var obj = document.getElementById(objid);
		if(obj.ctlType == CNST_CTLTYPE_END){
			hasNoEndNodeFlag = false;
			var aLines = obj.line.split(";");
			if(obj.line == "" || obj.line.indexOf(":") == -1){
				//没有前置
				noTarget += 1;
			}
		}
		else if(obj.ctlType == CNST_CTLTYPE_INITIAL){
			if(obj.line == "" || obj.line.indexOf(":") == -1){
				noSource += 1;
			}
			
		}
		//验证普通节点
		else if(obj.ctlType != CNST_CTLTYPE_END && obj.ctlType != CNST_CTLTYPE_INITIAL && obj.ctlType != CNST_CTLTYPE_LINE){
			var sourceNodeFlag = false;
			var targetNodeFlag = false;
			var aLines = obj.line.split(";");
			for( var j = 0; j < aLines.length - 1; j ++ ){
				var sourceNodeId = (aLines[j].split("TO")[0]).split(":")[1];
				var targetNodeId = aLines[j].split("TO")[1];
				if(sourceNodeId == objid){
					sourceNodeFlag = true;
				}
				if(targetNodeId == objid){
					targetNodeFlag = true;
				}
			}
//			if(!sourceNodeFlag || !targetNodeFlag){
//				nodeHasNoTransFlag = true;
//				break;
//			}
			if (sourceNodeFlag == false) {
				//没有后置
				noSource += 1;
			}
			if (targetNodeFlag == false) {
				//没有前置
				noTarget += 1;
			}
			//任务节点需要验证的情况
			if(obj.ctlType == CNST_CTLTYPE_TASK){
				//判断会签不通过时选择的路由是否存在
				if(obj.isCounterSign == "1"){
					var noApprove = obj.noApprove;
					var id = obj.id;
					var line = obj.line.split(";");
					var toLine = new Array();
					var temp = false;
					for (var v = 0; v < line.length - 1; v++) {
						var flag = line[v].split(":");
						if (flag[1].split("TO")[0] == id) {
							//lineId=flag[0];
							//toLine.push(document.getElementById(flag[0]).ctlName);
							if (noApprove == document.getElementById(flag[0]).ctlName || noApprove == "无") {
								temp = true;
								conditionFlag = "";
								break;
							}
						}
					}
					if (!temp) {
						conditionFlag = "任务节点: " + obj.title + " 会签不通过时选择的路由不存在。";
					}
					
				}

			}
			
			//判断条件节点需要验证的情况
			if(obj.ctlType == CNST_CTLTYPE_CONDITION){
				//判断默认路由是否真实存在
				var logicSet = obj.logicSet;
				if (logicSet.indexOf("$") != -1) {
					logicSet = logicSet.split("$");
					logicSet = logicSet[logicSet.length - 1]; //得到默认路由
				}
				var id = obj.id;
				var line = obj.line.split(";");
				var toLine = new Array();
				var temp = false;
				for (var v = 0; v < line.length - 1; v++) {
					var flag = line[v].split(":");
					if (flag[1].split("TO")[0] == id) {
						//lineId=flag[0];
						//toLine.push(document.getElementById(flag[0]).ctlName);
						if (logicSet == document.getElementById(flag[0]).ctlName) {
							temp = true;
							conditionFlag = "";
							break;
						}
					}
				}
				if (!temp) {
					conditionFlag = "条件节点: " + obj.title + " 的默认路由无效。";
				}
				
			}		
		}
	}
	if(hasNoEndNodeFlag){
		alert("模型中最少要有一个结束节点。");
		isValidated = false;
	} 

	if (isValidated && noSource > 0 && noTarget == 0) {
		alert("模型中存在没有后置连接的节点。");
		isValidated = false;
	}
	if (isValidated && noTarget > 0 && noSource == 0) {
		alert("模型中存在没有前置连接的节点。");
		isValidated = false;
	}
	if (isValidated && noTarget > 0 && noSource > 0) {
		alert("模型中存在没有前置连接和后置连接的节点。");
		isValidated = false;
	}
	if(conditionFlag != ""){
		alert(conditionFlag);
		isValidated = false;
	}
	return isValidated;
});
processModel.prototype.validateFilters = processValidateFilters;

//线段类	
function line(clickEvent, thisCaption, fromid, toid, isStraint, points, attributes) {
	var ob = document.getElementById("drawCanvas");
	var scrolltop, scrollleft;
	if (document.documentElement && document.documentElement.scrollTop) {
		scrolltop = document.documentElement.scrollTop;
		scrollleft = document.documentElement.scrollLeft;
	} else {
		if (document.body) {
			scrolltop = document.body.scrollTop;
			scrollleft = document.body.scrollLeft;
		}
	}

		//构造函数参数-----由图形建立
	if (arguments.length == 1) {
			//是否选择的是节点
		var nodeType = clickEvent.srcElement.ctlNodeType;
		var ctlType = clickEvent.srcElement.ctlType;
		if (!(nodeType == "node" || nodeType == "childNode" ) || ctlType == CNST_CTLTYPE_LINE) {
			return;
		}
			//获得节点
		var obj;
		if (nodeType == "node") {
			obj = clickEvent.srcElement;
		} else {
			obj = clickEvent.srcElement.parentElement;
		}
		if (fAddLineFlag == 1) {
			
				//计算鼠标在drawCanvas中的绝对位置
			var xx = clickEvent.clientX - getLeft(ob);//drawCanvas.offsetLeft+scrollleft;
			var yy = clickEvent.clientY - getTop(ob);//drawCanvas.offsetTop+scrolltop;
			xx = xx < 0 ? 0 : xx;
			yy = yy < 0 ? 0 : yy;
			var sl, st;
			with (obj.style) {
				sl = parseInt(pixelLeft);  //Left
				st = parseInt(pixelTop);   //Top
				sw = 100;//parseInt(width);      //width
				sh = 40;//parseInt(height);     //height
			}
				//计算路线
			sl = sl + sw / 2;
			st = st + sh / 2;
			this.thisIDName = "w" + fCtlNum;
			this.thisCaption = "\u8fde\u63a5" + fCtlNum;
			var newshapeobj = document.createElement("v:PolyLine");
			this.vmlobj = newshapeobj;
			newshapeobj.id = this.thisIDName;
			newshapeobj.style.position = "absolute";
			newshapeobj.status = "Queued";
			newshapeobj.oldStatus = "Finshed";
			newshapeobj.style.cursor = "default";
			newshapeobj.style.zindex = "-999";
			newshapeobj.filled = "false";

				//attribute
			newshapeobj.setAttribute("fromelement", obj);
			newshapeobj.setAttribute("toelement", null);
			newshapeobj.setAttribute("ctlType", CNST_CTLTYPE_LINE);
			newshapeobj.setAttribute("ctlName", this.thisCaption);
			newshapeobj.setAttribute("line", "");
			newshapeobj.setAttribute("ctlNodeType", "node");
			//newshapeobj.setAttribute("ctlNodeType", CNST_LINE_NAME);
			newshapeobj.setAttribute("nodeclass", this);
			newshapeobj.setAttribute("isStraint", "1");
			newshapeobj.setAttribute("movetype", "0");
			newshapeobj.setAttribute("plugins", []);//插件属性
			var newstrokeobj = document.createElement("v:stroke");
			this.strokeobj = newstrokeobj;
			newstrokeobj.endArrow = "Classic";
			newshapeobj.appendChild(newstrokeobj);
			var posArray = new Array(6);
			posArray[0] = sl;
			posArray[1] = st;
			posArray[2] = (sl + xx) / 2 + 10;
			posArray[3] = (st + yy) / 2 + 50;
			posArray[4] = xx + 50;
			posArray[5] = yy + 50;
			newshapeobj.setAttribute("posArray", posArray);
			newshapeobj.points = posArray[0] + "," + posArray[1] + " " + posArray[4] + "," + posArray[5];
			var newtextobj = document.createElement("v:TextBox");
			this.textobj = newtextobj;
			newtextobj.style.position = "absolute";
			newtextobj.style.fontsize = "10pt";
			newtextobj.style.overflow = "visible";
			newtextobj.style.color = "blue";
			newtextobj.innerText = this.thisCaption;
			newtextobj.style.top = posArray[3];
			newtextobj.style.left = posArray[2];
			newtextobj.style.zindex = "-999";
			newtextobj.style.height = lineTextHeight;
			newtextobj.style.width = lineTextWidth;
			newtextobj.setAttribute("ctlNodeType", CNST_LINE_NAME);
			ob.appendChild(newtextobj);
			ob.appendChild(newshapeobj);
			fAddLineFlag++;
			fSelectedObj = newshapeobj;
		}
	} else {
		this.thisIDName = clickEvent;
		this.thisCaption = thisCaption;
		var newshapeobj = document.createElement("v:PolyLine");
		this.vmlobj = newshapeobj;
		newshapeobj.id = this.thisIDName;
		newshapeobj.style.position = "absolute";
		newshapeobj.status = "Queued";
		newshapeobj.oldStatus = "Finshed";
		newshapeobj.style.cursor = "default";
		newshapeobj.style.zindex = "-999";
		newshapeobj.filled = "false";

				//起点与终点
		var fromelement = eval(fromid);
		var toelement = eval(toid);
				//attribute
		newshapeobj.setAttribute("fromelement", fromelement);
		newshapeobj.setAttribute("toelement", toelement);
		newshapeobj.setAttribute("ctlType", CNST_CTLTYPE_LINE);
		newshapeobj.setAttribute("ctlName", this.thisCaption);
		newshapeobj.setAttribute("line", "");
		newshapeobj.setAttribute("ctlNodeType", "node");
		//newshapeobj.setAttribute("ctlNodeType", CNST_LINE_NAME);
		newshapeobj.setAttribute("nodeclass", this);
		newshapeobj.setAttribute("isStraint", isStraint);
		newshapeobj.setAttribute("movetype", "0");
		newshapeobj.setAttribute("plugins", attributes['plugins']);//插件属性
		var newstrokeobj = document.createElement("v:stroke");
		this.strokeobj = newstrokeobj;
		newstrokeobj.endArrow = "Classic";
		newshapeobj.appendChild(newstrokeobj);
		var posArray = new Array();
		newshapeobj.setAttribute("posArray", posArray);
		if (points == "") {
			tempposArray = cf_CalculateLinePos(fromelement, toelement);
			this.vmlobj.posArray[0] = tempposArray["x1"];
			this.vmlobj.posArray[1] = tempposArray["y1"];
			this.vmlobj.posArray[2] = tempposArray["x2"];
			this.vmlobj.posArray[3] = tempposArray["y2"];
			this.vmlobj.posArray[4] = tempposArray["x4"];
			this.vmlobj.posArray[5] = tempposArray["y4"];
			this.vmlobj.posArray[6] = tempposArray["x5"];
			this.vmlobj.posArray[7] = tempposArray["y5"];
			this.vmlobj.posArray[8] = tempposArray["x6"];
			this.vmlobj.posArray[9] = tempposArray["y6"];
		} else {
			tempposArray = points.split(",");
			this.vmlobj.posArray[0] = tempposArray[0];
			this.vmlobj.posArray[1] = tempposArray[1];
			this.vmlobj.posArray[2] = tempposArray[2];
			this.vmlobj.posArray[3] = tempposArray[3];
			this.vmlobj.posArray[4] = tempposArray[4];
			this.vmlobj.posArray[5] = tempposArray[5];
			this.vmlobj.posArray[6] = tempposArray[6];
			this.vmlobj.posArray[7] = tempposArray[7];
			this.vmlobj.posArray[8] = tempposArray[8];
			this.vmlobj.posArray[9] = tempposArray[9];
		}
		if (isStraint == "1") {
			newshapeobj.points = this.vmlobj.posArray[0] + "," + this.vmlobj.posArray[1] + " " + this.vmlobj.posArray[4] + "," + this.vmlobj.posArray[5];
		} else {
			if (isStraint == "0") {
				newshapeobj.points = this.vmlobj.posArray[0] + "," + this.vmlobj.posArray[1] + " " + this.vmlobj.posArray[6] + "," + this.vmlobj.posArray[7] + " " + this.vmlobj.posArray[8] + "," + this.vmlobj.posArray[9] + " " + this.vmlobj.posArray[4] + "," + this.vmlobj.posArray[5];
			}
		}
		var thisLineID = this.vmlobj.fromelement.id + "TO" + this.vmlobj.toelement.id;
		this.vmlobj.setAttribute("name", thisLineID);
		thisLineID = this.thisIDName + ":" + thisLineID;
		//元素迁移线为：迁移线Id+”：“+源节点Id+”TO“+目的节点Id+”;“
		this.vmlobj.fromelement.line += thisLineID + ";";
		this.vmlobj.toelement.line += thisLineID + ";";
		ob.appendChild(newshapeobj);
		var newtextobj = document.createElement("v:TextBox");
		this.textobj = newtextobj;
		newtextobj.style.position = "absolute";
		newtextobj.style.fontsize = "10pt";
		newtextobj.style.overflow = "visible";
		newtextobj.style.color = "blue";
		newtextobj.style.zindex = "-999";
		newtextobj.innerText = this.thisCaption;
		newtextobj.style.top = this.vmlobj.posArray[3];
		newtextobj.style.left = this.vmlobj.posArray[2];
		newtextobj.style.width = lineTextWidth;
		newtextobj.style.height = lineTextHeight;
		newtextobj.setAttribute("ctlNodeType", CNST_LINE_NAME);
		ob.appendChild(newtextobj);
		var newcrossobj = document.createElement("v:oval");
		newcrossobj.style.position = "absolute";
		newcrossobj.style.display = "none";
		newcrossobj.StrokeColor = "red";
		newcrossobj.style.left = this.vmlobj.posArray[6] - linePointWidth / 2;
		newcrossobj.style.top = this.vmlobj.posArray[7] - linePointHeight / 2;
		newcrossobj.style.width = linePointWidth;
		newcrossobj.style.height = linePointHeight;
		newcrossobj.fillcolor = "red";
		newcrossobj.ctlNodeType = "node";
		newcrossobj.ctlType = CNST_CTLTYPE_MOVEPOINT;
		newcrossobj.movetype = "1";
		newcrossobj.nodeclass = this;
		this.movepoint1 = newcrossobj;
		this.movepoint1.ondblclick = parentclick;
		ob.appendChild(newcrossobj);
					//第二移动点
		var newcrossobj2 = document.createElement("v:oval");
		newcrossobj2.style.position = "absolute";
		newcrossobj2.style.display = "none";
		newcrossobj2.StrokeColor = "red";
		newcrossobj2.style.left = this.vmlobj.posArray[8] - linePointWidth / 2;
		newcrossobj2.style.top = this.vmlobj.posArray[9] - linePointHeight / 2;
		newcrossobj2.style.width = linePointWidth;
		newcrossobj2.style.height = linePointHeight;
		newcrossobj2.fillcolor = "red";
		newcrossobj2.ctlNodeType = "node";
		newcrossobj2.ctlType = CNST_CTLTYPE_MOVEPOINT;
		newcrossobj2.movetype = "2";
		newcrossobj2.nodeclass = this;
		this.movepoint2 = newcrossobj2;
		this.movepoint2.ondblclick = parentclick;
		ob.appendChild(newcrossobj2);
	}
	this.checkend = checkendnode;
	function checkendnode(endclickevent) {
		var ob = document.getElementById("drawCanvas");
		this.vmlobj.posArray[4] = endclickevent.clientX - getLeft(ob) - 10;//ob.offsetLeft-10+scrollleft;
		this.vmlobj.posArray[5] = endclickevent.clientY - getTop(ob) - 10;//ob.offsetTop-10+scrolltop;
		var str = this.vmlobj.id + ".Points.value=\"" + this.vmlobj.posArray[0] + "," + this.vmlobj.posArray[1] + " " + this.vmlobj.posArray[4] + "," + this.vmlobj.posArray[5] + "\"";
		eval(str);
		this.vmlobj.posArray[2] = (this.vmlobj.posArray[0] + this.vmlobj.posArray[4]) / 2;
		this.vmlobj.posArray[3] = (this.vmlobj.posArray[1] + this.vmlobj.posArray[5]) / 2;
		this.textobj.style.top = this.vmlobj.posArray[3];
		this.textobj.style.left = this.vmlobj.posArray[2];
	}
	this.delxml = dellinexml;
	function dellinexml() {
		var lines = doc.getElementsByTagName("transition");
		for (var i = 0; i < lines.length; i++) {
			if (lines[i].getAttributeNode("atnode").value == this.vmlobj.id) {
				lines[i].parentNode.removeChild(lines[i]);
				break;
			}
		}
	}
	this.drawend = drawendnode;
	function drawendnode(endupevent) {
		/*
		var nodeType = endupevent.srcElement.ctlNodeType;
		if (!(nodeType == "node" || nodeType == "childNode") || endupevent.srcElement == this.vmlobj) {
			fSelectedObj = this.vmlobj.fromelement;
			this.textobj.removeNode(true);
			this.vmlobj.removeNode(true);
			fAddLineFlag = 0;
			leftToolBar.clickBtn(0);
			return;
		}
			//获得节点
		var obj;
		if (nodeType == "node") {
			obj = endupevent.srcElement;
		} else {
			obj = endupevent.srcElement.parentElement;
		}*/
		
		//zw
		if(endupevent == null){
			fSelectedObj = this.vmlobj.fromelement;
			this.textobj.removeNode(true);
			this.vmlobj.removeNode(true);
			fAddLineFlag = 0;
			//leftToolBar.clickBtn(0);
			return;
		}
		var obj = endupevent;

			//如果是同一节点
		if (obj.id == this.vmlobj.fromelement.id) {
			alert("\u8bf7\u9009\u62e9\u4e0d\u540c\u8282\u70b9\u505a\u4e3a\u7ed3\u675f\u8282\u70b9!");
			fSelectedObj = this.vmlobj.fromelement;
			this.textobj.removeNode(true);
			this.vmlobj.removeNode(true);
			fAddLineFlag = 0;
			//leftToolBar.clickBtn(0);
			return;
		} else {
			var ob = this.vmlobj.fromelement;
			var atempArray = cf_CalculateLinePos(ob, obj);
			this.vmlobj.posArray[0] = atempArray["x1"];
			this.vmlobj.posArray[1] = atempArray["y1"];
			this.vmlobj.posArray[2] = atempArray["x2"];
			this.vmlobj.posArray[3] = atempArray["y2"];
			this.vmlobj.posArray[4] = atempArray["x4"];
			this.vmlobj.posArray[5] = atempArray["y4"];
			this.vmlobj.posArray[6] = atempArray["x5"];
			this.vmlobj.posArray[7] = atempArray["y5"];
			this.vmlobj.posArray[8] = atempArray["x6"];
			this.vmlobj.posArray[9] = atempArray["y6"];
			var str = this.vmlobj.id + ".Points.value=\"" + this.vmlobj.posArray[0] + "," + this.vmlobj.posArray[1] + " " + this.vmlobj.posArray[4] + "," + this.vmlobj.posArray[5] + "\"";
			eval(str);
			this.textobj.style.top = this.vmlobj.posArray[3];
			this.textobj.style.left = this.vmlobj.posArray[2];
			this.vmlobj.toelement = obj;
			var thisLineID = this.vmlobj.fromelement.id + "TO" + this.vmlobj.toelement.id;
			var totalObj = document.getElementById("drawCanvas").childNodes;
			for (var i = 0; i < totalObj.length; i++) {
				if (totalObj[i].name == thisLineID) {
					alert("\u8be5\u8fde\u63a5\u5df2\u7ecf\u5b58\u5728!");
					fSelectedObj = this.vmlobj.fromelement;
					this.textobj.removeNode(true);
					this.vmlobj.removeNode(true);
					fAddLineFlag = 0;
					//leftToolBar.clickBtn(0);
					return;
				}
			}
			
			if((this.vmlobj.fromelement.ctlType == CNST_CTLTYPE_INITIAL || 
				this.vmlobj.fromelement.ctlType == CNST_CTLTYPE_NODE || 
				this.vmlobj.fromelement.ctlType == CNST_CTLTYPE_PROSTATE) && 
				this.vmlobj.fromelement.line != ""){
					var aLines = this.vmlobj.fromelement.line.split(";");
					for( var i = 0; i < aLines.length - 1; i ++ ){
						var oLineStart = (aLines[i].split("TO")[0]).split(":")[1];
						if(oLineStart == this.vmlobj.fromelement.id){				
							alert(this.vmlobj.fromelement.title + "节点只能有一条后置连接！");
							fSelectedObj = this.vmlobj.fromelement;
							this.textobj.removeNode(true);
							this.vmlobj.removeNode(true);
							fAddLineFlag = 0;
							//leftToolBar.clickBtn(0);
							return;
						}
					}
			}
			//结束节点不允许有后置连接
			if(this.vmlobj.fromelement.ctlType == CNST_CTLTYPE_END){
				alert(this.vmlobj.fromelement.title + "节点不允许有后置连接！");
				fSelectedObj = this.vmlobj.fromelement;
				this.textobj.removeNode(true);
				this.vmlobj.removeNode(true);
				fAddLineFlag = 0;
				//leftToolBar.clickBtn(0);
				return;				
			}
			//zw
			if(this.vmlobj.toelement.ctlType == CNST_CTLTYPE_INITIAL){
				alert(this.vmlobj.toelement.title + "节点不允许有前置连接！");
				fSelectedObj = this.vmlobj.toelement;
				this.textobj.removeNode(true);
				this.vmlobj.removeNode(true);
				fAddLineFlag = 0;
				//leftToolBar.clickBtn(0);
				return;				
			}
			
			this.vmlobj.setAttribute("name", thisLineID);
			thisLineID = this.thisIDName + ":" + thisLineID;
			this.vmlobj.fromelement.line += thisLineID + ";";
			obj.line += thisLineID + ";";
			addToObjArray(this.thisIDName, true); //增加到控件列表
			var newcrossobj = document.createElement("v:oval");
			newcrossobj.style.position = "absolute";
			newcrossobj.style.display = "none";
			newcrossobj.StrokeColor = "red";
			newcrossobj.style.left = this.vmlobj.posArray[6] - linePointWidth / 2;
			newcrossobj.style.top = this.vmlobj.posArray[7] - linePointHeight / 2;
//				newcrossobj.style.width=4*Math.sqrt(2);
//				newcrossobj.style.height=4*Math.sqrt(2);
			newcrossobj.style.width = linePointWidth;
			newcrossobj.style.height = linePointHeight;
			newcrossobj.fillcolor = "red";
			newcrossobj.ctlNodeType = "node";
			newcrossobj.ctlType = CNST_CTLTYPE_MOVEPOINT;
			newcrossobj.movetype = "1";
			newcrossobj.nodeclass = this;
			this.movepoint1 = newcrossobj;
			this.movepoint1.ondblclick = parentclick;
			document.getElementById("drawCanvas").appendChild(newcrossobj);
			var newcrossobj2 = document.createElement("v:oval");
			newcrossobj2.style.position = "absolute";
			newcrossobj2.style.display = "none";
			newcrossobj2.StrokeColor = "red";
			newcrossobj2.style.left = this.vmlobj.posArray[8] - linePointWidth / 2;
			newcrossobj2.style.top = this.vmlobj.posArray[9] - linePointHeight / 2;
//				newcrossobj2.style.width=4*Math.sqrt(2);
//				newcrossobj2.style.height=4*Math.sqrt(2);
			newcrossobj2.style.width = linePointWidth;
			newcrossobj2.style.height = linePointHeight;
			newcrossobj2.fillcolor = "red";
			newcrossobj2.ctlNodeType = "node";
			newcrossobj2.ctlType = CNST_CTLTYPE_MOVEPOINT;
			newcrossobj2.movetype = "2";
			newcrossobj2.nodeclass = this;
			this.movepoint2 = newcrossobj2;
			this.movepoint2.ondblclick = parentclick;
			document.getElementById("drawCanvas").appendChild(newcrossobj2);
			//leftToolBar.clickBtn(0);
			fAddLineFlag = 0;
		}
	}
	function parentclick() {
		this.nodeclass.vmlobj.ondblclick();
	}
	this.vmlobj.oncontextmenu = contextmenuitemline;
	function contextmenuitemline() {
		if(currentStatus != "edit"){
			contextForThisLine();
		}
	}
	this.vmlobj.ondblclick = attributeSet;
}
function attributeSet() {
	if (this.ctlType == CNST_CTLTYPE_LINE) {
		var dialog = window.showModalDialog(systemroot + "/flow_role.jsp", window, "dialogWidth:600px; dialogHeight:600px; center:yes; help:no; resizable:no; status:no");
	} else if (this.ctlType == CNST_CTLTYPE_TASK) {
		var dialog = window.showModalDialog(systemroot + "/flow_next.jsp", window, "dialogWidth:600px; dialogHeight:600px; center:yes; help:no; resizable:no; status:no");
	} else if (this.ctlType == CNST_CTLTYPE_CONDITION) {
		var dialog = window.showModalDialog(systemroot + "/flow_decision.jsp", window, "dialogWidth:600px; dialogHeight:600px; center:yes; help:no; resizable:no; status:no; scroll:yes;");
	} else if (this.ctlType == CNST_CTLTYPE_PROSTATE) {
		var dialog = window.showModalDialog(systemroot + "/flow_subprocess.jsp", window, "dialogWidth:600px; dialogHeight:600px; center:yes; help:no; resizable:no; status:no");
	} else if (this.ctlType == CNST_CTLTYPE_SUPERSTATE) {
		var dialog = window.showModalDialog(systemroot + "/flow_activeSub.jsp", window, "dialogWidth:600px; dialogHeight:600px; center:yes; help:no; resizable:no; status:no");
	} else if (this.ctlType == CNST_CTLTYPE_NODE) {
		var dialog = window.showModalDialog(systemroot + "/flow_node.jsp", window, "dialogWidth:600px; dialogHeight:600px; center:yes; help:no; resizable:no; status:no");
	} else if (this.ctlType == CNST_CTLTYPE_INITIAL) {
		var dialog = window.showModalDialog(systemroot + "/flow_startnode.jsp", window, "dialogWidth:600px; dialogHeight:600px; center:yes; help:no; resizable:no; status:no");
	} else if (this.ctlType == CNST_CTLTYPE_END) {
		var dialog = window.showModalDialog(systemroot + "/flow_endnode.jsp", window, "dialogWidth:600px; dialogHeight:600px; center:yes; help:no; resizable:no; status:no");
	} else{
		var dialog = window.showModalDialog(systemroot + "/flow_othernode.jsp", window, "dialogWidth:600px; dialogHeight:600px; center:yes; help:no; resizable:no; status:no");
	}
}
function contextmenuitem() {
	contextForThis();
}

/**
 * 迁移线验证Filter设置
 */
var lineValidateFilters = [];
// 添加迁移线的默认验证Filter
// 1.如果验证正确，则接口返回true
// 2.如果验证失败，则接口直接alert错误信息，并返回false
lineValidateFilters.push(function(){
	// TODO：工作流本身属性的验证逻辑
	//alert("工作流本身属性的验证逻辑。");
	return true;
});
line.prototype.validateFilters = lineValidateFilters;



// 开始节点类
//function startnode(thisIDName, thisCaption, leftpix, toppix, nodeEnter, nodeLeave, nodeForm, formPriv, issetaction, actionset) {
function startnode(attributes) {
	var ob = document.getElementById("drawCanvas");
	var newshapeobj = document.createElement("v:roundrect");
	newshapeobj.innerHTML = "<img src='" + systemroot + "/images/ico/start.gif' style='margin:6'>";
	this.vmlobj = newshapeobj;

		//shadowobj
	var newshadowobj = document.createElement("v:shadow");
	this.shadowobj = newshadowobj;

		//textboxobj
	var newtextobj = document.createElement("v:TextBox");
	this.textobj = newtextobj;

		//构造函数参数-----由图形建立
	if (arguments.length == 0) {
		this.thisIDName = "w" + fCtlNum;
		this.thisCaption = "\u5f00\u59cb";
		this.leftpix = 0;
		this.toppix = 0;
		this.vmlobj.setAttribute("nodeEnter", "");
		this.vmlobj.setAttribute("nodeLeave", "");
		this.vmlobj.setAttribute("nodeForm", "\u6d41\u7a0b\u542f\u52a8\u8868\u5355,0");
		this.vmlobj.setAttribute("formPriv", "superadmin");
		this.vmlobj.setAttribute("isSetAction", "0");
		this.vmlobj.setAttribute("actionSet", "");
		this.vmlobj.setAttribute("plugins", []);//插件属性

		//zw
		this.vmlobj.setAttribute("eaNodeEnterName", "");
		this.vmlobj.setAttribute("eaNodeLeaveName", "");
	} else { // 构造函数参数-----由文件导入
		this.thisIDName = attributes['thisIDName']//thisIDName;
		this.thisCaption = attributes['thisCaption']//thisCaption;
		this.leftpix = attributes['leftpix']//leftpix;
		this.toppix = attributes['toppix']//toppix;
		this.vmlobj.setAttribute("nodeEnter", attributes['nodeEnter'])//nodeEnter);
		this.vmlobj.setAttribute("nodeLeave", attributes['nodeLeave'])//nodeLeave);
		this.vmlobj.setAttribute("nodeForm", attributes['nodeForm'])//nodeForm);
		this.vmlobj.setAttribute("formPriv", attributes['formPriv'])//formPriv);
		this.vmlobj.setAttribute("isSetAction", attributes['isSetAction'])//issetaction);
		this.vmlobj.setAttribute("actionSet", attributes['actionSet'])//actionset);
		this.vmlobj.setAttribute("plugins", attributes['plugins']);//插件属性

		//zw
		this.vmlobj.setAttribute("eaNodeEnterName", attributes['eaNodeEnterName']);
		this.vmlobj.setAttribute("eaNodeLeaveName", attributes['eaNodeLeaveName']);
	}
	this.nodetype = "initial";
	initNodeElement(this);
	newshapeobj.ondblclick = attributeSet;
	ob.appendChild(newshapeobj);
	addToObjArray(this.thisIDName, true); //增加到控件列表
	this.addxml = addtoxml;
	function addtoxml() {
							//添加根节点
		var n = doc.createNode(1, "start-state", "");
		
		//添加xml节点标识属性
		var r = doc.createAttribute("xmlflag");
		r.value = nCtlNum ++; 

			//添加属性 
		n.setAttributeNode(r);			
		
					//创建属性 
		var r = doc.createAttribute("name");
		r.value = this.textobj.innerText; 

						//添加属性 
		n.setAttributeNode(r); 
					//创建属性 
		var r = doc.createAttribute("atnode");
		r.value = this.vmlobj.id; 

						//添加属性 
		n.setAttributeNode(r);
		root.appendChild(n);
	}
	this.addline = addlinetoxml;
	this.delxml = delstartxml;
	function delstartxml() {
	}
}

/**
 * 开始节点验证Filter设置
 */
var startnodeValidateFilters = [];
// 添加开始节点的默认验证Filter
// 1.如果验证正确，则接口返回true
// 2.如果验证失败，则接口直接alert错误信息，并返回false
startnodeValidateFilters.push(function(){
	// TODO：工作流本身属性的验证逻辑
	//alert("工作流本身属性的验证逻辑。");
	return true;
});
startnode.prototype.validateFilters = startnodeValidateFilters;


// 状态节点类
//function statnode(clickEvent, thisCaption, leftpix, toppix, nodeEnter, nodeLeave, nodeForm, formPriv, issetaction, actionset) {
function statnode(clickEvent, attributes) {
	var ob = document.getElementById("drawCanvas");
	var newshapeobj = document.createElement("v:roundrect");
	newshapeobj.innerHTML = "<img src='" + systemroot + "/images/ico/stat.gif' style='margin:6'>";
	this.vmlobj = newshapeobj;

		//shadowobj
	var newshadowobj = document.createElement("v:shadow");
	this.shadowobj = newshadowobj;

		//textboxobj
	var newtextobj = document.createElement("v:TextBox");
	this.textobj = newtextobj;
		//构造函数参数-----由图形建立
	if (arguments.length == 1) {
		this.thisIDName = "w" + fCtlNum;
		this.thisCaption = "\u72b6\u6001" + fCtlNum;
			//计算鼠标在drawCanvas中的绝对位置
		var xx = clickEvent.clientX - getLeft(ob), yy = clickEvent.clientY - getTop(ob);
		xx = xx < 0 ? 0 : xx;
		yy = yy < 0 ? 0 : yy;
		this.leftpix = xx;
		this.toppix = yy;
		this.vmlobj.setAttribute("nodeEnter", "");
		this.vmlobj.setAttribute("nodeLeave", "");
		this.vmlobj.setAttribute("nodeForm", "\u6d41\u7a0b\u542f\u52a8\u8868\u5355,0");
		this.vmlobj.setAttribute("formPriv", "superadmin");
		this.vmlobj.setAttribute("isSetAction", "0");
		this.vmlobj.setAttribute("actionSet", "");
		this.vmlobj.setAttribute("plugins", []);//插件属性
	} else { // 构造函数参数-----由文件导入
		this.thisIDName = clickEvent;
		this.thisCaption = attributes['thisCaption']//thisCaption;
		this.leftpix = attributes['leftpix']//leftpix;
		this.toppix = attributes['toppix']//toppix;
		this.vmlobj.setAttribute("nodeEnter", attributes['nodeEnter'])//nodeEnter);
		this.vmlobj.setAttribute("nodeLeave", attributes['nodeLeave'])//nodeLeave);
		this.vmlobj.setAttribute("nodeForm", attributes['nodeForm'])//nodeForm);
		this.vmlobj.setAttribute("formPriv", attributes['formPriv'])//formPriv);
		this.vmlobj.setAttribute("isSetAction", attributes['isSetAction'])//issetaction);
		this.vmlobj.setAttribute("actionSet", attributes['actionSet'])//actionset);
		this.vmlobj.setAttribute("plugins", attributes['plugins']);//插件属性
	}
	this.nodetype = "stat";
	initNodeElement(this);
	newshapeobj.ondblclick = attributeSet;
	newshapeobj.oncontextmenu = contextmenuitem;
	newshapeobj.setAttribute("beanof", new Array(2, 3));
	ob.appendChild(newshapeobj);
	addToObjArray(this.thisIDName, true); //增加到控件列表
	function contextmenuitem() {
		if(currentStatus != "edit"){
			contextForThis();
		}
	}
	this.addline = addlinetoxml;
	this.delxml = delnodexml;
}


// 任务节点类
//function tasknode(clickEvent, thisCaption, leftpix, toppix, taskStart, taskEnd, taskName, isCounterSign, handleActor, nodeEnter, nodeLeave, nodeForm, formPriv, issetaction, actionset, noApprove, taskstartmail, taskendmail, taskstartmes, taskendmes, preTranType, nextTranType, isActiveactor, counterSignType, realActors, canSelectOther, maxActors) {
function tasknode(clickEvent, attributes) {
	var ob = document.getElementById("drawCanvas");
	var newshapeobj = document.createElement("v:roundrect");
	newshapeobj.innerHTML = "<img src='" + systemroot + "/images/ico/task.gif' style='margin:6'>";
	this.vmlobj = newshapeobj;

		//shadowobj
	var newshadowobj = document.createElement("v:shadow");
	this.shadowobj = newshadowobj;

		//textboxobj
	var newtextobj = document.createElement("v:TextBox");
	this.textobj = newtextobj;		

		//构造函数参数-----由图形建立
	if (arguments.length == 1) {
		this.thisIDName = "w" + fCtlNum;
		this.thisCaption = "\u4efb\u52a1" + fCtlNum;
			//计算鼠标在drawCanvas中的绝对位置
		var xx = clickEvent.clientX - getLeft(ob), yy = clickEvent.clientY - getTop(ob);
		xx = xx < 0 ? 0 : xx;
		yy = yy < 0 ? 0 : yy;
		this.leftpix = xx;
		this.toppix = yy;
			//新增任务属性
		this.vmlobj.setAttribute("taskStart", "");
		this.vmlobj.setAttribute("taskEnd", "");
		this.vmlobj.setAttribute("taskName", this.thisCaption);
		this.vmlobj.setAttribute("isCounterSign", "0");
		this.vmlobj.setAttribute("handleActor", "s0$1,发起人");
		this.vmlobj.setAttribute("realActors", "\u53d1\u8d77\u4eba");
		this.vmlobj.setAttribute("nodeEnter", "");
		this.vmlobj.setAttribute("nodeLeave", "");
		this.vmlobj.setAttribute("nodeForm", "\u6d41\u7a0b\u542f\u52a8\u8868\u5355,0");
		this.vmlobj.setAttribute("formPriv", "superadmin");
		this.vmlobj.setAttribute("isSetAction", "0");
		this.vmlobj.setAttribute("actionSet", "");
		this.vmlobj.setAttribute("noApprove", "无");
		this.vmlobj.setAttribute("taskStartMail", "0");
		this.vmlobj.setAttribute("taskEndMail", "0");
		this.vmlobj.setAttribute("taskStartMes", "0");
		this.vmlobj.setAttribute("taskEndMes", "0");
		this.vmlobj.setAttribute("preTranType", "0");
		this.vmlobj.setAttribute("nextTranType", "0");
		this.vmlobj.setAttribute("isActiveactor", "0");
		this.vmlobj.setAttribute("counterSignType", "0");
		this.vmlobj.setAttribute("canSelectOther", "0");
		this.vmlobj.setAttribute("maxActors", "1");
		this.vmlobj.setAttribute("reAssign", "0");
		this.vmlobj.setAttribute("reAssignmore", "0");
		this.vmlobj.setAttribute("reassignActor", "");
		this.vmlobj.setAttribute("doc_isTimer", "0");
		this.vmlobj.setAttribute("doc_timerSet", "0,day,0,day,0,day,0,0,0,0,0,0");
		this.vmlobj.setAttribute("isDocEdit", "0");
		this.vmlobj.setAttribute("wordPrivil", "0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0");
		this.vmlobj.setAttribute("plugins", []);//插件属性
		
		//zw
		this.vmlobj.setAttribute("eaNodeEnterName", "");
		this.vmlobj.setAttribute("eaNodeLeaveName", "");
		this.vmlobj.setAttribute("eaTaskStartName", "");
		this.vmlobj.setAttribute("eaTaskEndName", "");
		
	} else { // 构造函数参数-----由文件导入
		//zw
		//当clickEvent是字符类型时，值为id
		this.thisIDName = clickEvent;
		this.thisCaption = attributes['thisCaption']//thisCaption;
		this.leftpix = attributes['leftpix']//leftpix;
		this.toppix = attributes['toppix']//toppix;
			//新增任务属性
		this.vmlobj.setAttribute("taskStart", attributes['taskStart'])//taskStart);
		this.vmlobj.setAttribute("taskEnd", attributes['taskEnd'])//taskEnd);
		this.vmlobj.setAttribute("taskName", attributes['taskName'])//taskName);
		this.vmlobj.setAttribute("isCounterSign", attributes['isCounterSign'])//isCounterSign);
		this.vmlobj.setAttribute("handleActor", attributes['handleActor'])//handleActor);
		this.vmlobj.setAttribute("realActors", attributes['realActors'])//realActors);
		this.vmlobj.setAttribute("nodeEnter", attributes['nodeEnter'])//nodeEnter);
		this.vmlobj.setAttribute("nodeLeave", attributes['nodeLeave'])//nodeLeave);
		this.vmlobj.setAttribute("nodeForm", attributes['nodeForm'])//nodeForm);
		this.vmlobj.setAttribute("formPriv", attributes['formPriv'])//formPriv);
		this.vmlobj.setAttribute("isSetAction", attributes['issetaction'])//issetaction);
		this.vmlobj.setAttribute("actionSet", attributes['actionset'])//actionset);
		this.vmlobj.setAttribute("noApprove", attributes['noApprove'])//noApprove);
		this.vmlobj.setAttribute("taskStartMail", attributes['taskStartMail'])//taskstartmail);
		this.vmlobj.setAttribute("taskEndMail", attributes['taskEndMail'])//taskendmail);
		this.vmlobj.setAttribute("taskStartMes", attributes['taskStartMes'])//taskstartmes);
		this.vmlobj.setAttribute("taskEndMes", attributes['taskEndMes'])//taskendmes);
		this.vmlobj.setAttribute("preTranType", attributes['preTranType'])//preTranType);
		this.vmlobj.setAttribute("nextTranType", attributes['nextTranType'])//nextTranType);
		this.vmlobj.setAttribute("isActiveactor", attributes['isActiveactor'])//isActiveactor);
		this.vmlobj.setAttribute("counterSignType", attributes['counterSignType'])//counterSignType);
		this.vmlobj.setAttribute("canSelectOther", attributes['canSelectOther'])//canSelectOther);
		this.vmlobj.setAttribute("maxActors", attributes['maxActors'])//maxActors);
		this.vmlobj.setAttribute("reAssign", attributes['reAssign']);
		this.vmlobj.setAttribute("reAssignmore", attributes['reAssignmore']);
		this.vmlobj.setAttribute("reassignActor", attributes['reassignActor']);
		this.vmlobj.setAttribute("doc_isTimer", attributes['doc_isTimer']);
		this.vmlobj.setAttribute("doc_timerSet", attributes['doc_timerSet']);
		this.vmlobj.setAttribute("isDocEdit", attributes['isDocEdit']);
		this.vmlobj.setAttribute("wordPrivil", attributes['wordPrivil']);
		this.vmlobj.setAttribute("plugins", attributes['plugins']);//插件属性
		
		//zw
		this.vmlobj.setAttribute("eaNodeEnterName", attributes['eaNodeEnterName']);
		this.vmlobj.setAttribute("eaNodeLeaveName", attributes['eaNodeLeaveName']);
		this.vmlobj.setAttribute("eaTaskStartName", attributes['eaTaskStartName']);
		this.vmlobj.setAttribute("eaTaskEndName", attributes['eaTaskEndName']);
	}
	this.nodetype = "task";
	initNodeElement(this);
	newshapeobj.ondblclick = attributeSet;
	newshapeobj.oncontextmenu = contextmenuitem;
	ob.appendChild(newshapeobj);
	addToObjArray(this.thisIDName, true); //增加到控件列表
	function contextmenuitem() {
		if(currentStatus != "edit"){
			contextForThis();
		}
	}
	this.addline = addlinetoxml;
	this.delxml = delnodexml;
}

/**
 * 任务节点验证Filter设置
 */
var tasknodeValidateFilters = [];
// 添加任务节点的默认验证Filter
// 1.如果验证正确，则接口返回true
// 2.如果验证失败，则接口直接alert错误信息，并返回false
tasknodeValidateFilters.push(function(){
	// TODO：工作流本身属性的验证逻辑
	//alert("工作流本身属性的验证逻辑。");
	return true;
});
tasknode.prototype.validateFilters = tasknodeValidateFilters;


// split节点类
//function splitnode(clickEvent, thisCaption, leftpix, toppix, nodeEnter, nodeLeave, nodeForm, formPriv, issetaction, actionset) {
function splitnode(clickEvent, attributes) {
	var ob = document.getElementById("drawCanvas");
	var newshapeobj = document.createElement("v:roundrect");
	newshapeobj.innerHTML = "<img src='" + systemroot + "/images/ico/splits.gif' style='margin:6'>";
	this.vmlobj = newshapeobj;

		//shadowobj
	var newshadowobj = document.createElement("v:shadow");
	this.shadowobj = newshadowobj;

		//textboxobj
	var newtextobj = document.createElement("v:TextBox");
	this.textobj = newtextobj;
		//构造函数参数-----由图形建立
	if (arguments.length == 1) {
		this.thisIDName = "w" + fCtlNum;
		this.thisCaption = "\u5206\u652f" + fCtlNum;
			//计算鼠标在drawCanvas中的绝对位置
		var xx = clickEvent.clientX - getLeft(ob), yy = clickEvent.clientY - getTop(ob);
		xx = xx < 0 ? 0 : xx;
		yy = yy < 0 ? 0 : yy;
		this.leftpix = xx;
		this.toppix = yy;
		this.vmlobj.setAttribute("nodeEnter", "");
		this.vmlobj.setAttribute("nodeLeave", "");
		this.vmlobj.setAttribute("nodeForm", "\u6d41\u7a0b\u542f\u52a8\u8868\u5355,0");
		this.vmlobj.setAttribute("formPriv", "superadmin");
		this.vmlobj.setAttribute("isSetAction", "0");
		this.vmlobj.setAttribute("actionSet", "");
		this.vmlobj.setAttribute("plugins", []);//插件属性
	} else { // 构造函数参数-----由文件导入
		this.thisIDName = clickEvent;
		this.thisCaption = attributes['thisCaption']//thisCaption;
		this.leftpix = attributes['leftpix']//leftpix;
		this.toppix = attributes['toppix']//toppix;
		this.vmlobj.setAttribute("nodeEnter", attributes['nodeEnter'])//nodeEnter);
		this.vmlobj.setAttribute("nodeLeave", attributes['nodeLeave'])//nodeLeave);
		this.vmlobj.setAttribute("nodeForm", attributes['nodeForm'])//nodeForm);
		this.vmlobj.setAttribute("formPriv", attributes['formPriv'])//formPriv);
		this.vmlobj.setAttribute("isSetAction", attributes['isSetAction'])//issetaction);
		this.vmlobj.setAttribute("actionSet", attributes['actionSet'])//actionset);
		this.vmlobj.setAttribute("plugins", attributes['plugins']);//插件属性
	}
	this.nodetype = "split";
	initNodeElement(this);
	newshapeobj.ondblclick = attributeSet;
	newshapeobj.oncontextmenu = contextmenuitem;
	newshapeobj.setAttribute("beanof", new Array(2, 3));
	ob.appendChild(newshapeobj);
	addToObjArray(this.thisIDName, true); //增加到控件列表
	function contextmenuitem() {
		if(currentStatus != "edit"){
			contextForThis();
		}
	}
	this.addline = addlinetoxml;
	this.delxml = delnodexml;
}



// join节点类
//function joinnode(clickEvent, thisCaption, leftpix, toppix, nodeEnter, nodeLeave, nodeForm, formPriv, issetaction, actionset) {
function joinnode(clickEvent, attributes) {
	var ob = document.getElementById("drawCanvas");
	var newshapeobj = document.createElement("v:roundrect");
	newshapeobj.innerHTML = "<img src='" + systemroot + "/images/ico/joins.gif' style='margin:6'>";
	this.vmlobj = newshapeobj;

		//shadowobj
	var newshadowobj = document.createElement("v:shadow");
	this.shadowobj = newshadowobj;

		//textboxobj
	var newtextobj = document.createElement("v:TextBox");
	this.textobj = newtextobj;
		//构造函数参数-----由图形建立
	if (arguments.length == 1) {
		this.thisIDName = "w" + fCtlNum;
		this.thisCaption = "\u805a\u5408" + fCtlNum;
			//计算鼠标在drawCanvas中的绝对位置
		var xx = clickEvent.clientX - getLeft(ob), yy = clickEvent.clientY - getTop(ob);
		xx = xx < 0 ? 0 : xx;
		yy = yy < 0 ? 0 : yy;
		this.leftpix = xx;
		this.toppix = yy;
		this.vmlobj.setAttribute("nodeEnter", "");
		this.vmlobj.setAttribute("nodeLeave", "");
		this.vmlobj.setAttribute("nodeForm", "\u6d41\u7a0b\u542f\u52a8\u8868\u5355,0");
		this.vmlobj.setAttribute("formPriv", "superadmin");
		this.vmlobj.setAttribute("isSetAction", "0");
		this.vmlobj.setAttribute("actionSet", "");
		this.vmlobj.setAttribute("plugins", []);//插件属性
	} else { // 构造函数参数-----由文件导入
		this.thisIDName = clickEvent;
		this.thisCaption = attributes['thisCaption']//thisCaption;
		this.leftpix = attributes['leftpix']//leftpix;
		this.toppix = attributes['toppix']//toppix;
		this.vmlobj.setAttribute("nodeEnter", attributes['nodeEnter'])//nodeEnter);
		this.vmlobj.setAttribute("nodeLeave", attributes['nodeLeave'])//nodeLeave);
		this.vmlobj.setAttribute("nodeForm", attributes['nodeForm'])//nodeForm);
		this.vmlobj.setAttribute("formPriv", attributes['formPriv'])//formPriv);
		this.vmlobj.setAttribute("isSetAction", attributes['isSetAction'])//issetaction);
		this.vmlobj.setAttribute("actionSet", attributes['actionSet'])//actionset);
		this.vmlobj.setAttribute("plugins", attributes['plugins']);//插件属性
	}
	this.nodetype = "join";
	initNodeElement(this);
	newshapeobj.ondblclick = attributeSet;
	newshapeobj.oncontextmenu = contextmenuitem;
	newshapeobj.setAttribute("beanof", new Array(2, 3));
	ob.appendChild(newshapeobj);
	addToObjArray(this.thisIDName, true); //增加到控件列表
	function contextmenuitem() {
		if(currentStatus != "edit"){
			contextForThis();
		}
	}
	this.addline = addlinetoxml;
	this.delxml = delnodexml;
}


// 条件节点类
//function conditionnode(clickEvent, thisCaption, leftpix, toppix, nodeEnter, nodeLeave, nodeForm, formPriv, issetaction, actionset, issetlogic, logicvar, logicset) {
function conditionnode(clickEvent, attributes) {
	var ob = document.getElementById("drawCanvas");
	var newshapeobj = document.createElement("v:roundrect");
	newshapeobj.innerHTML = "<img src='" + systemroot + "/images/ico/condition.gif' style='margin:6'>";
	this.vmlobj = newshapeobj;

		//shadowobj
	var newshadowobj = document.createElement("v:shadow");
	this.shadowobj = newshadowobj;

		//textboxobj
	var newtextobj = document.createElement("v:TextBox");
	this.textobj = newtextobj;
		//构造函数参数-----由图形建立
	if (arguments.length == 1) {
		this.thisIDName = "w" + fCtlNum;
		this.thisCaption = "\u6761\u4ef6" + fCtlNum;
			//计算鼠标在drawCanvas中的绝对位置
		var xx = clickEvent.clientX - getLeft(ob), yy = clickEvent.clientY - getTop(ob);
		xx = xx < 0 ? 0 : xx;
		yy = yy < 0 ? 0 : yy;
		this.leftpix = xx;
		this.toppix = yy;
		this.vmlobj.setAttribute("nodeEnter", "");
		this.vmlobj.setAttribute("nodeLeave", "");
		this.vmlobj.setAttribute("nodeForm", "\u6d41\u7a0b\u542f\u52a8\u8868\u5355,0");
		this.vmlobj.setAttribute("formPriv", "superadmin");
		this.vmlobj.setAttribute("isSetAction", "0");
		this.vmlobj.setAttribute("actionSet", "");
//		this.vmlobj.setAttribute("isSetLogic", "0");
//		this.vmlobj.setAttribute("logicVar", "");
		this.vmlobj.setAttribute("logicSet", "");
		this.vmlobj.setAttribute("plugins", []);//插件属性
		
		//add by caidw
		this.vmlobj.setAttribute("decideHandleName", "");
		this.vmlobj.setAttribute("decideHandleClass", "");
	
	} else { // 构造函数参数-----由文件导入
		this.thisIDName = clickEvent;
		this.thisCaption = attributes['thisCaption']//thisCaption;
		this.leftpix = attributes['leftpix']//leftpix;
		this.toppix = attributes['toppix']//toppix;
		this.vmlobj.setAttribute("nodeEnter", attributes['nodeEnter'])//nodeEnter);
		this.vmlobj.setAttribute("nodeLeave", attributes['nodeLeave'])//nodeLeave);
		this.vmlobj.setAttribute("nodeForm", attributes['nodeForm'])//nodeForm);
		this.vmlobj.setAttribute("formPriv", attributes['formPriv'])//formPriv);
		this.vmlobj.setAttribute("isSetAction", attributes['isSetAction'])//issetaction);
		this.vmlobj.setAttribute("actionSet", attributes['actionSet'])//actionset);
//		this.vmlobj.setAttribute("isSetLogic", attributes['issetlogic'])//issetlogic);
//		this.vmlobj.setAttribute("logicVar", attributes['logicvar'])//logicvar);
		this.vmlobj.setAttribute("logicSet", attributes['logicSet'])//logicset);
		this.vmlobj.setAttribute("plugins", attributes['plugins']);//插件属性
	
		//add by caidw
		this.vmlobj.setAttribute("decideHandleName", attributes['decideHandleName']);
		this.vmlobj.setAttribute("decideHandleClass", attributes['decideHandleClass']);
		}
	this.nodetype = "condition";
	initNodeElement(this);
	newshapeobj.ondblclick = attributeSet;
	newshapeobj.oncontextmenu = contextmenuitem;
	newshapeobj.setAttribute("beanof", new Array(2, 3));
	ob.appendChild(newshapeobj);
	addToObjArray(this.thisIDName, true); //增加到控件列表
	function contextmenuitem() {
		if(currentStatus != "edit"){
			contextForThis();
		}
	}
	this.addline = addlinetoxml;
	this.delxml = delnodexml;
}

/**
 * 条件节点验证Filter设置
 */
var conditionnodeValidateFilters = [];
// 添加条件节点的默认验证Filter
// 1.如果验证正确，则接口返回true
// 2.如果验证失败，则接口直接alert错误信息，并返回false
conditionnodeValidateFilters.push(function(){
	// TODO：工作流本身属性的验证逻辑
	//alert("工作流本身属性的验证逻辑。");
	return true;
});
conditionnode.prototype.validateFilters = conditionnodeValidateFilters;


// node节点类
//function nodenode(clickEvent, thisCaption, leftpix, toppix, nodeEnter, nodeLeave, nodeDelegation, nodeForm, formPriv, issetaction, actionset) {
function nodenode(clickEvent, attributes) {
	var ob = document.getElementById("drawCanvas");
	var newshapeobj = document.createElement("v:roundrect");
	newshapeobj.innerHTML = "<img src='" + systemroot + "/images/ico/node.gif' style='margin:6'>";
	this.vmlobj = newshapeobj;

		//shadowobj
	var newshadowobj = document.createElement("v:shadow");
	this.shadowobj = newshadowobj;

		//textboxobj
	var newtextobj = document.createElement("v:TextBox");
	this.textobj = newtextobj;
		//构造函数参数-----由图形建立
	if (arguments.length == 1) {
		this.thisIDName = "w" + fCtlNum;
		this.thisCaption = "\u81ea\u52a8\u8282\u70b9" + fCtlNum;
			//计算鼠标在drawCanvas中的绝对位置
		var xx = clickEvent.clientX - getLeft(ob), yy = clickEvent.clientY - getTop(ob);
		xx = xx < 0 ? 0 : xx;
		yy = yy < 0 ? 0 : yy;
		this.leftpix = xx;
		this.toppix = yy;
		this.vmlobj.setAttribute("nodeEnter", "");
		this.vmlobj.setAttribute("nodeLeave", "");
		this.vmlobj.setAttribute("nodeDelegation", "");
		this.vmlobj.setAttribute("nodeForm", "\u6d41\u7a0b\u542f\u52a8\u8868\u5355,0");
		this.vmlobj.setAttribute("formPriv", "superadmin");
		this.vmlobj.setAttribute("isSetAction", "0");
		this.vmlobj.setAttribute("actionSet", "");
		this.vmlobj.setAttribute("preTranType", "0");
		this.vmlobj.setAttribute("plugins", []);//插件属性
		
		//zw
		this.vmlobj.setAttribute("eaNodeEnterName", "");
		this.vmlobj.setAttribute("eaNodeLeaveName", "");
		this.vmlobj.setAttribute("eaNodeDelegateName", "");

	} else { // 构造函数参数-----由文件导入
		this.thisIDName = clickEvent;
		this.thisCaption = attributes['thisCaption']//thisCaption;
		this.leftpix = attributes['leftpix']//leftpix;
		this.toppix = attributes['toppix']//toppix;
		this.vmlobj.setAttribute("nodeEnter", attributes['nodeEnter'])//nodeEnter);
		this.vmlobj.setAttribute("nodeLeave", attributes['nodeLeave'])//nodeLeave);
		this.vmlobj.setAttribute("nodeDelegation", attributes['nodeDelegation'])//nodeDelegation);		
		this.vmlobj.setAttribute("nodeForm", attributes['nodeForm'])//nodeForm);
		this.vmlobj.setAttribute("formPriv", attributes['formPriv'])//formPriv);
		this.vmlobj.setAttribute("isSetAction", attributes['isSetAction'])//issetaction);
		this.vmlobj.setAttribute("actionSet", attributes['actionSet'])//actionset);
		this.vmlobj.setAttribute("preTranType", attributes['preTranType']);
		this.vmlobj.setAttribute("plugins", attributes['plugins']);//插件属性
		
		//zw
		this.vmlobj.setAttribute("eaNodeEnterName", attributes['eaNodeEnterName']);
		this.vmlobj.setAttribute("eaNodeLeaveName", attributes['eaNodeLeaveName']);
		this.vmlobj.setAttribute("eaNodeDelegateName", attributes['eaNodeDelegateName']);
	}
	this.nodetype = CNST_CTLTYPE_NODE;
	initNodeElement(this);
	newshapeobj.ondblclick = attributeSet;
	newshapeobj.oncontextmenu = contextmenuitem;
	ob.appendChild(newshapeobj);
	addToObjArray(this.thisIDName, true); //增加到控件列表
	function contextmenuitem() {
		if(currentStatus != "edit"){
			contextForThis();
		}
	}
	this.addline = addlinetoxml;
	this.delxml = delnodexml;
}

/**
 * 自定义节点验证Filter设置
 */
var nodenodeValidateFilters = [];
// 添加自定义节点的默认验证Filter
// 1.如果验证正确，则接口返回true
// 2.如果验证失败，则接口直接alert错误信息，并返回false
nodenodeValidateFilters.push(function(){
	// TODO：工作流本身属性的验证逻辑
	//alert("工作流本身属性的验证逻辑。");
	return true;
});
nodenode.prototype.validateFilters = nodenodeValidateFilters;

	
// 子流程节点类
//function processstatenode(clickEvent, thisCaption, leftpix, toppix, nodeEnter, nodeLeave, nodeForm, formPriv, issetaction, actionset, subprocesstype, subprocessname, subprocessform, synchronize, fromparent, toparent) {
function processstatenode(clickEvent, attributes) {
	var ob = document.getElementById("drawCanvas");
	var newshapeobj = document.createElement("v:roundrect");
	newshapeobj.innerHTML = "<img src='" + systemroot + "/images/ico/processstate.gif' style='margin:6'>";
	this.vmlobj = newshapeobj;

		//shadowobj
	var newshadowobj = document.createElement("v:shadow");
	this.shadowobj = newshadowobj;

		//textboxobj
	var newtextobj = document.createElement("v:TextBox");
	this.textobj = newtextobj;
		//构造函数参数-----由图形建立
	if (arguments.length == 1) {
		this.thisIDName = "w" + fCtlNum;
		this.thisCaption = "\u5b50\u6d41\u7a0b" + fCtlNum;
			//计算鼠标在drawCanvas中的绝对位置
		var xx = clickEvent.clientX - getLeft(ob), yy = clickEvent.clientY - getTop(ob);
		xx = xx < 0 ? 0 : xx;
		yy = yy < 0 ? 0 : yy;
		this.leftpix = xx;
		this.toppix = yy;
		this.vmlobj.setAttribute("nodeEnter", "");
		this.vmlobj.setAttribute("nodeLeave", "");
		this.vmlobj.setAttribute("nodeForm", "\u6d41\u7a0b\u542f\u52a8\u8868\u5355,0");
		this.vmlobj.setAttribute("formPriv", "superadmin");
		this.vmlobj.setAttribute("isSetAction", "0");
		this.vmlobj.setAttribute("actionSet", "");
		this.vmlobj.setAttribute("subProcessType", "1");
		this.vmlobj.setAttribute("subProcessName", "");
		this.vmlobj.setAttribute("subProcessForm", "");
		this.vmlobj.setAttribute("synchronize", "1");
		this.vmlobj.setAttribute("fromParent", "1");
		this.vmlobj.setAttribute("toParent", "1");
		this.vmlobj.setAttribute("plugins", []);//插件属性
		//zw
		this.vmlobj.setAttribute("eaNodeEnterName", "");
		this.vmlobj.setAttribute("eaNodeLeaveName", "");

	} else { // 构造函数参数-----由文件导入
		this.thisIDName = clickEvent;
		this.thisCaption = attributes['thisCaption']//thisCaption;
		this.leftpix = attributes['leftpix']//leftpix;
		this.toppix = attributes['toppix']//toppix;
		this.vmlobj.setAttribute("nodeEnter", attributes['nodeEnter'])//nodeEnter);
		this.vmlobj.setAttribute("nodeLeave", attributes['nodeLeave'])//nodeLeave);
		this.vmlobj.setAttribute("nodeForm", attributes['nodeForm'])//nodeForm);
		this.vmlobj.setAttribute("formPriv", attributes['formPriv'])//formPriv);
		this.vmlobj.setAttribute("isSetAction", attributes['isSetAction'])//issetaction);
		this.vmlobj.setAttribute("actionSet", attributes['actionSet'])//actionset);
		this.vmlobj.setAttribute("subProcessType", attributes['subProcessType'])//subprocesstype);
		this.vmlobj.setAttribute("subProcessName", attributes['subProcessName'])//subprocessname);
		this.vmlobj.setAttribute("subProcessForm", attributes['subProcessForm'])//subprocessform);
		this.vmlobj.setAttribute("synchronize", attributes['synchronize'])//synchronize);
		this.vmlobj.setAttribute("fromParent", attributes['fromParent'])//fromparent);
		this.vmlobj.setAttribute("toParent", attributes['toParent'])//toparent);
		this.vmlobj.setAttribute("plugins", attributes['plugins']);//插件属性
		//zw
		this.vmlobj.setAttribute("eaNodeEnterName", attributes['eaNodeEnterName']);
		this.vmlobj.setAttribute("eaNodeLeaveName", attributes['eaNodeLeaveName']);
	
	}
	this.nodetype = CNST_CTLTYPE_PROSTATE;
	initNodeElement(this);
	newshapeobj.ondblclick = attributeSet;
	newshapeobj.oncontextmenu = contextmenuitem;
	ob.appendChild(newshapeobj);
	addToObjArray(this.thisIDName, true); //增加到控件列表
	function contextmenuitem() {
		if(currentStatus != "edit"){
			contextForThis();
		}
	}
	this.addline = addlinetoxml;
	this.delxml = delnodexml;
}

/**
 * 子流程节点验证Filter设置
 */
var processstatenodeValidateFilters = [];
// 添加子流程节点的默认验证Filter
// 1.如果验证正确，则接口返回true
// 2.如果验证失败，则接口直接alert错误信息，并返回false
processstatenodeValidateFilters.push(function(){
	var nodeId = this;
	var nodeElement = document.getElementById(nodeId);
	var node = nodeElement.nodeclass;
	var isValidated = true;
	
	// 判断子流程类型是否已设置
	var subProcessType = node.vmlobj.subProcessType;
	if(subProcessType == undefined || subProcessType == null || subProcessType == ""){
		alert("节点" + node.textobj.innerText + "未设置子流程类型。");
		isValidated = false;
	}
	// 判断子流程是否已设置
	var subProcessName = node.vmlobj.subProcessName;
	if(isValidated ){
		if(subProcessName == undefined || subProcessName == null || subProcessName == ""){
			alert("节点" + node.textobj.innerText + "未设置子流程。");
			isValidated = false;
		}else{
			//alert($("#processName").val());
			$.ajax({
				url : scriptroot
						+ "/workflowDesign/action/processDesign!checkSubProcess.action",
				type : "post",
				dataType : "text",
				async : false,
				data : "processName=" + subProcessName,
				success : function(data) {
					if (data != null) {
						var msg = data;
						if(msg == "0"){
							alert("节点" + node.textobj.innerText + "设置的子流程无效。");
							isValidated = false;
						}
					}
				}
			});		
		}
	}
	// 判断子流程表单是否已设置
	var subProcessForm = node.vmlobj.subProcessForm;
	if(isValidated && (subProcessForm == undefined || subProcessForm == null || subProcessForm == "")){
		alert("节点" + node.textobj.innerText + "未设置子流程表单。");
		isValidated = false;
	}
	
	return isValidated;
});
processstatenode.prototype.validateFilters = processstatenodeValidateFilters;


// 动态子流程节点类
//function superstatenode(clickEvent, thisCaption, leftpix, toppix, taskStart, taskEnd, taskName, handleActor, nodeEnter, nodeLeave, nodeForm, formPriv, issetaction, actionset, taskstartmail, taskendmail, taskstartmes, taskendmes, isActiveactor, realActors, synchronize, subProcessType, canSelectOther, maxActors, fromparent, toparent) {
function superstatenode(clickEvent, attributes) {
	var ob = document.getElementById("drawCanvas");
	var newshapeobj = document.createElement("v:roundrect");
	newshapeobj.innerHTML = "<img src='" + systemroot + "/images/ico/superstate.gif' style='margin:6'>";
	this.vmlobj = newshapeobj;

		//shadowobj
	var newshadowobj = document.createElement("v:shadow");
	this.shadowobj = newshadowobj;

		//textboxobj
	var newtextobj = document.createElement("v:TextBox");
	this.textobj = newtextobj;
		//构造函数参数-----由图形建立
	if (arguments.length == 1) {
		this.thisIDName = "w" + fCtlNum;
		this.thisCaption = "\u52a8\u6001\u5b50\u6d41\u7a0b" + fCtlNum;
			//计算鼠标在drawCanvas中的绝对位置
		var xx = clickEvent.clientX - getLeft(ob), yy = clickEvent.clientY - getTop(ob);
		xx = xx < 0 ? 0 : xx;
		yy = yy < 0 ? 0 : yy;
		this.leftpix = xx;
		this.toppix = yy;
		this.vmlobj.setAttribute("taskStart", "");
		this.vmlobj.setAttribute("taskEnd", "");
		this.vmlobj.setAttribute("taskName", this.thisCaption);
		this.vmlobj.setAttribute("handleActor", "s0$1,发起人");
		this.vmlobj.setAttribute("realActors", "\u53d1\u8d77\u4eba");
		this.vmlobj.setAttribute("nodeEnter", "");
		this.vmlobj.setAttribute("nodeLeave", "");
		this.vmlobj.setAttribute("nodeForm", "\u6d41\u7a0b\u542f\u52a8\u8868\u5355,0");
		this.vmlobj.setAttribute("formPriv", "superadmin");
		this.vmlobj.setAttribute("isSetAction", "0");
		this.vmlobj.setAttribute("actionSet", "");
		this.vmlobj.setAttribute("taskStartMail", "0");
		this.vmlobj.setAttribute("taskEndMail", "0");
		this.vmlobj.setAttribute("taskStartMes", "0");
		this.vmlobj.setAttribute("taskEndMes", "0");
		this.vmlobj.setAttribute("isActiveactor", "0");
		this.vmlobj.setAttribute("subProcessType", "2");
		this.vmlobj.setAttribute("synchronize", "1");
		this.vmlobj.setAttribute("canSelectOther", "0");
		this.vmlobj.setAttribute("maxActors", "1");
		this.vmlobj.setAttribute("fromParent", "1");
		this.vmlobj.setAttribute("toParent", "1");
		this.vmlobj.setAttribute("doc_isTimer", "0");
		this.vmlobj.setAttribute("doc_timerSet", "0,day,0,day,0,day,0,0,0,0,0,0");
		this.vmlobj.setAttribute("plugins", []);//插件属性
	} else { // 构造函数参数-----由文件导入
		this.thisIDName = clickEvent;
		this.thisCaption = attributes['thisCaption']//thisCaption;
		this.leftpix = attributes['leftpix']//leftpix;
		this.toppix = attributes['toppix']//toppix;
		this.vmlobj.setAttribute("taskStart", attributes['taskStart'])//taskStart);
		this.vmlobj.setAttribute("taskEnd", attributes['taskEnd'])//taskEnd);
		this.vmlobj.setAttribute("taskName", attributes['taskName'])//taskName);
		this.vmlobj.setAttribute("handleActor", attributes['handleActor'])//handleActor);
		this.vmlobj.setAttribute("realActors", attributes['realActors'])//realActors);
		this.vmlobj.setAttribute("nodeEnter", attributes['nodeEnter'])//nodeEnter);
		this.vmlobj.setAttribute("nodeLeave", attributes['nodeLeave'])//nodeLeave);
		this.vmlobj.setAttribute("nodeForm", attributes['nodeForm'])//nodeForm);
		this.vmlobj.setAttribute("formPriv", attributes['formPriv'])//formPriv);
		this.vmlobj.setAttribute("isSetAction", attributes['isSetAction'])//issetaction);
		this.vmlobj.setAttribute("actionSet", attributes['actionSet'])//actionset);
		this.vmlobj.setAttribute("taskStartMail", attributes['taskStartMail'])//taskstartmail);
		this.vmlobj.setAttribute("taskEndMail", attributes['taskEndMail'])//taskendmail);
		this.vmlobj.setAttribute("taskStartMes", attributes['taskStartMes'])//taskstartmes);
		this.vmlobj.setAttribute("taskEndMes", attributes['taskEndMes'])//taskendmes);
		this.vmlobj.setAttribute("isActiveactor", attributes['isActiveactor'])//isActiveactor);
		this.vmlobj.setAttribute("subProcessType", attributes['subProcessType'])//subProcessType);
		this.vmlobj.setAttribute("synchronize", attributes['synchronize'])//synchronize);
		this.vmlobj.setAttribute("canSelectOther", attributes['canSelectOther'])//canSelectOther);
		this.vmlobj.setAttribute("maxActors", attributes['maxActors'])//maxActors);
		this.vmlobj.setAttribute("fromParent", attributes['fromParent'])//fromparent);
		this.vmlobj.setAttribute("toParent", attributes['toParent'])//toparent);
		this.vmlobj.setAttribute("doc_isTimer", attributes['doc_isTimer']);
		this.vmlobj.setAttribute("doc_timerSet", attributes['doc_timerSet']);
		this.vmlobj.setAttribute("plugins", attributes['plugins']);//插件属性
	}
	this.nodetype = CNST_CTLTYPE_SUPERSTATE;
	initNodeElement(this);
	newshapeobj.ondblclick = attributeSet;
	newshapeobj.oncontextmenu = contextmenuitem;
	ob.appendChild(newshapeobj);
	addToObjArray(this.thisIDName, true); //增加到控件列表
	function contextmenuitem() {
		if(currentStatus != "edit"){
			contextForThis();
		}
	}
	this.addline = addlinetoxml;
	this.delxml = delnodexml;
}	


// 结束节点类
//function endnode(clickEvent, thisCaption, leftpix, toppix, nodeEnter, nodeLeave, nodeForm, formPriv, issetaction, actionset) {
function endnode(clickEvent, attributes) {
	var ob = document.getElementById("drawCanvas");
	var newshapeobj = document.createElement("v:roundrect");
	newshapeobj.innerHTML = "<img src='" + systemroot + "/images/ico/end.gif' style='margin:6'>";
	this.vmlobj = newshapeobj;

		//shadowobj
	var newshadowobj = document.createElement("v:shadow");
	this.shadowobj = newshadowobj;

		//textboxobj
	var newtextobj = document.createElement("v:TextBox");
	this.textobj = newtextobj;
		//构造函数参数-----由图形建立
	if (arguments.length == 1) {
		this.thisIDName = "w" + fCtlNum;
		this.thisCaption = "\u7ed3\u675f" + fCtlNum;
			//计算鼠标在drawCanvas中的绝对位置
		var xx = clickEvent.clientX - getLeft(ob), yy = clickEvent.clientY - getTop(ob);
		xx = xx < 0 ? 0 : xx;
		yy = yy < 0 ? 0 : yy;
		this.leftpix = xx;
		this.toppix = yy;
		this.vmlobj.setAttribute("nodeEnter", "");
		this.vmlobj.setAttribute("nodeLeave", "");
		this.vmlobj.setAttribute("nodeForm", "\u6d41\u7a0b\u542f\u52a8\u8868\u5355,0");
		this.vmlobj.setAttribute("formPriv", "superadmin");
		this.vmlobj.setAttribute("isSetAction", "0");
		this.vmlobj.setAttribute("actionSet", "");
		this.vmlobj.setAttribute("plugins", []);//插件属性

		//zw
		this.vmlobj.setAttribute("eaNodeEnterName", "");
		this.vmlobj.setAttribute("eaNodeLeaveName", "");
	} else { // 构造函数参数-----由文件导入
		this.thisIDName = clickEvent;
		this.thisCaption = attributes['thisCaption']//thisCaption;
		this.leftpix = attributes['leftpix']//leftpix;
		this.toppix = attributes['toppix']//toppix;
		this.vmlobj.setAttribute("nodeEnter", attributes['nodeEnter'])//nodeEnter);
		this.vmlobj.setAttribute("nodeLeave", attributes['nodeLeave'])//nodeLeave);
		this.vmlobj.setAttribute("nodeForm", attributes['nodeForm'])//nodeForm);
		this.vmlobj.setAttribute("formPriv", attributes['formPriv'])//formPriv);
		this.vmlobj.setAttribute("isSetAction", attributes['isSetAction'])//issetaction);
		this.vmlobj.setAttribute("actionSet", attributes['actionSet'])//actionset);
		this.vmlobj.setAttribute("plugins", attributes['plugins']);//插件属性

		//zw
		this.vmlobj.setAttribute("eaNodeEnterName", attributes['eaNodeEnterName']);
		this.vmlobj.setAttribute("eaNodeLeaveName", attributes['eaNodeLeaveName']);
	}
	this.nodetype = "end";
	initNodeElement(this);
	newshapeobj.ondblclick = attributeSet;
	newshapeobj.oncontextmenu = contextmenuitem;
	ob.appendChild(newshapeobj);
	addToObjArray(this.thisIDName, true); //增加到控件列表
	function contextmenuitem() {
		if(currentStatus != "edit"){
			contextForThis();
		}
	}
	this.addline = addlinetoxml;
	this.delxml = delnodexml;
}

/**
 * 结束节点验证Filter设置
 */
var endnodeValidateFilters = [];
// 添加结束节点的默认验证Filter
// 1.如果验证正确，则接口返回true
// 2.如果验证失败，则接口直接alert错误信息，并返回false
endnodeValidateFilters.push(function(){
	// TODO：工作流本身属性的验证逻辑
	//alert("工作流本身属性的验证逻辑。");
	return true;
});
endnode.prototype.validateFilters = endnodeValidateFilters;


function addlinetoxml(line) {
	if (fAddLineFlag == 0) {
		var node = root;
		for (var i = 0; i < root.childNodes.length; i++) {
			var tempnode = root.childNodes[i];
			if (root.childNodes[i].getAttributeNode("atnode").value == this.vmlobj.id) {
				node = root.childNodes[i];
			}
		}
		var n = doc.createNode(1, "transition", "");
		
		//添加xml节点标识属性
		var r = doc.createAttribute("xmlflag");
		r.value = nCtlNum ++; 

			//添加属性 
		n.setAttributeNode(r);			
		
					//创建属性 
		var r = doc.createAttribute("name");
		r.value = line.nodeclass.textobj.innerText;
						//添加属性 
		n.setAttributeNode(r); 
					//创建属性 
		var r = doc.createAttribute("to");
		r.value = line.toelement.nodeclass.thisCaption;

						//添加属性 
		n.setAttributeNode(r);
					//创建属性 
		var r = doc.createAttribute("atnode");
		var tempvalue = line.id;
		r.value = tempvalue;
						//添加属性 
		n.setAttributeNode(r);
		
		/*
		 * 在流程迁移线上保存流程轨迹信息
		 */
		var n1 = doc.createNode(1, "action", "");
		//添加xml节点标识属性
		var r = doc.createAttribute("xmlflag");
		r.value = nCtlNum ++; 
		//添加属性 
		n1.setAttributeNode(r);			
		
		var r = doc.createAttribute("name");
		r.value = flowTrackName;
		n1.setAttributeNode(r);
		var r = doc.createAttribute("class");
		r.value = flowTrackClass;
		n1.setAttributeNode(r);
		n.appendChild(n1);
		
		//增加任务节点前置连接类型判断
		//增加自动节点的前置连接类型判断
		if(line.toelement.ctlType == CNST_CTLTYPE_TASK || line.toelement.ctlType == CNST_CTLTYPE_NODE){
			var n1 = doc.createNode(1, "action", "");
			//添加xml节点标识属性
			var r = doc.createAttribute("xmlflag");
			r.value = nCtlNum ++; 
			//添加属性 
			n1.setAttributeNode(r);			
			
			var r = doc.createAttribute("name");
			r.value = preTranTypeName;
			n1.setAttributeNode(r);
			var r = doc.createAttribute("class");
			r.value = preTranTypeClass;
			n1.setAttributeNode(r);
			n.appendChild(n1);		
		}
		node.appendChild(n);
	}
}
function delnodexml() {
	var childs = root.childNodes;
	for (var i = 0; i < childs.length; i++) {
		if (childs[i].getAttributeNode("atnode").value == this.vmlobj.id) {
			childs[i].parentNode.removeChild(childs[i]);
			break;
		}
	}
}

	//初始化节点公共属性
function initNodeElement(obj) {
	obj.vmlobj.id = obj.thisIDName;
	obj.vmlobj.style.position = "absolute";
	
	//zw
	if(obj.leftpix == undefined)
		obj.vmlobj.style.left = "50px";
	else
		obj.vmlobj.style.left = obj.leftpix;
	
	if(obj.toppix == undefined)	
		obj.vmlobj.style.top = "50px";
	else
		obj.vmlobj.style.top = obj.toppix;
	
		//obj.vmlobj.style.auto="no";
	obj.vmlobj.style.overflow = "visible";
	obj.vmlobj.style.width = "100";
	obj.vmlobj.style.height = "40";
	obj.vmlobj.style.cursor = "default";
	obj.vmlobj.style.zindex = "999";
		//attribute
	obj.vmlobj.setAttribute("ctlType", obj.nodetype);
	obj.vmlobj.setAttribute("ctlName", obj.nodetype);
	obj.vmlobj.setAttribute("line", "");
	obj.vmlobj.setAttribute("ctlNodeType", "node");
	obj.vmlobj.setAttribute("nodeclass", obj);
	obj.shadowobj.color = "#b3b3b3";
	obj.shadowobj.offset = "2.25pt,2.25pt";
	obj.shadowobj.style.cursor = "default";
	obj.shadowobj.on = "t";
	obj.shadowobj.type = "single";
		//attribute
	obj.shadowobj.setAttribute("ctlNodeType", "childNode");
	obj.vmlobj.appendChild(obj.shadowobj);

		//textboxobj
	obj.textobj.style.fontsize = "10pt";
	obj.textobj.style.overflow = "visible";
	obj.textobj.style.color = "blue";
	obj.textobj.innerText = obj.thisCaption;
	obj.textobj.style.top = "auto";
	obj.textobj.style.height = "23.25pt";
	obj.textobj.style.margintop = "7.875pt";
	obj.textobj.style.marginleft = "0.375pt";
	obj.textobj.style.textAlign = "center";
	obj.vmlobj.title = obj.textobj.innerText;
		//attribute
	obj.textobj.setAttribute("ctlNodeType", "childNode");
	obj.vmlobj.appendChild(obj.textobj);
}

