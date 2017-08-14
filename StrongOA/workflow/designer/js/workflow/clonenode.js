//zw

/*
 * @author 钟伟
 * @date 2013-10-29
 */


var currentPos = {};
currentPos.left = 0;
currentPos.top = 0;

var rects = null;

var isSelRect = false;

/**
 * 选择框开始
 * @param e window.event事件
 */
function startSelRect(e){
	var ele = e.srcElement;
	clearSelRect();
	
	if(e.button == 1 && ele.id == "drawCanvas"){
		var dc = $("#drawCanvas");
		var isShowPro =  $("#seniorSet").css('display');
		var offset = 40;
		if(isShowPro != 'none'){
			offset+=170;
		}
		var selRect = document.createElement("v:rect");
		selRect.id = "sel-rect";
		//selRect.stroked = false;
		selRect.strokecolor = "blue";
		selRect.fillcolor = "#5599FF";
		//selRect.style.border = "2px dotted red";
		selRect.style.position = "relative";
		selRect.style.width = "1px";
		selRect.style.height = "1px";
		selRect.style.opacity = "0.7";
		selRect.style.filter = "alpha(opacity=70)";
		
		var mLeft = e.clientX + ele.scrollLeft - offset;
		var mTop = e.clientY + ele.scrollTop - 60;
		selRect.style.left = mLeft;
		selRect.style.top = mTop;
		currentPos.left = mLeft;
		currentPos.top = mTop;
		dc.append(selRect);
		
		rects = $("roundrect,polyline");
		
		isSelRect = true;
	}
}

/**
 * 选择框移动
 * @param e window.event事件
 */
function moveSelRect(e){
	if(e.button == 1 && isSelRect){
		var selRect = $('#sel-rect');
		if(selRect[0] != undefined){
			var dc = $("#drawCanvas");
			var isShowPro =  $("#seniorSet").css('display');
			var offset = 40;
			if(isShowPro != 'none'){
				offset+=170;
			}
			var ele = dc[0];
			
			var mLeft = e.clientX + ele.scrollLeft - offset;
			var mTop = e.clientY + ele.scrollTop - 60;
			var width = mLeft - currentPos.left;
			var height = mTop - currentPos.top;
			if(width < 0){
				selRect[0].style.left = mLeft + "px";
			}
			if(height < 0){
				selRect[0].style.Top = mTop + "px";
			}
			selRect.width(Math.abs(width));
			selRect.height(Math.abs(height));
		}
	}
}

function clearSelRect(){
	
	$("#drawCanvas > #sel-rect").remove();
	
}

/**
 * 选择框结束
 * @param e window.event事件
 */
function endSelRect(e){
	//if(e.button == 1 && (e.srcElement.id == "drawCanvas" || e.srcElement.id == "sel-rect")){
	if(e.button == 1 && isSelRect){
		var selRect = $('#sel-rect');
		if(selRect.length < 1)
			return;
		
		var selXy = [];
		/*
		selXy.push(selRect.style.left.replace("px", ""));
		selXy.push(selRect.style.top.replace("px", ""));
		selXy.push(selRect.style.left.replace("px", "") + selRect.style.width.replace("px", ""));
		selXy.push(selRect.style.top.replace("px", "") + selRect.style.height.replace("px", ""));
		*/
		
		selXy.push(selRect.offset().left);
		selXy.push(selRect.offset().top);			
		selXy.push(selRect.offset().left + selRect.width());
		selXy.push(selRect.offset().top + selRect.height());
		
		var insideCount = 0;
		rects.each(function(){
			var objXy = [];
			objXy.push($(this).offset().left);
			objXy.push($(this).offset().top);
			objXy.push($(this).offset().left + $(this).width());
			objXy.push($(this).offset().top + $(this).height());
			
			if(hasInSelRect(selXy, objXy)){
				
				insideCount++;
				if(insideCount === 1){
					fSelectedObjArray = [];
					fSelectedObjCopyArray = [];
				}
				
				var selectDrawState = new SelectDrawState();
				selectDrawState.clickEvent = event;
				drawContext.setDrawState(selectDrawState);
				
				var tag=drawContext.draw(drawCanvas, this);
				if (tag == true) { //如果返回为真
		        	drawCanvas.style.cursor='default';
					leftToolBar.clickBtn(0);  //按下左边工具栏第一个按钮
					controlSubject.notifyObservers(); //通知所有的观察者
				}
			}
		});
		
	}
	isSelRect = false;
}

//参数结构：[left,top,right,bottom]
function hasInSelRect(selXY, objXY){
	var rXY = [];
	rXY.push(Math.max(selXY[0], objXY[0]));
	rXY.push(Math.max(selXY[1], objXY[1]));
	rXY.push(Math.min(selXY[2], objXY[2]));
	rXY.push(Math.min(selXY[3], objXY[3]));
	if(rXY[0] > rXY[2] || rXY[1] > rXY[3])
		return false;
	else
		return true;
}

function selectAllNodes(){
	$("roundrect,polyline").each(function(){
		var selectDrawState = new SelectDrawState();
		selectDrawState.clickEvent = event;
		drawContext.setDrawState(selectDrawState);
		
		var tag=drawContext.draw(drawCanvas, this);
		if (tag == true) { //如果返回为真
        	drawCanvas.style.cursor='default';
			leftToolBar.clickBtn(0);  //按下左边工具栏第一个按钮
			controlSubject.notifyObservers(); //通知所有的观察者
		}
	});
}




//已选择的节点
var selectedNode;
//已选择的节点对象值
var selectedObj;

function exeCopyNode(_this, pXy){
	var id = _this.id;
	if(id == ""){
		_this = _this.parentElement;
		id = _this.id;
	}

	/*
	var sObj = $(_this);
	
	var suffix = "c";
	var node = $(_this.outerHTML);

	node.attr("id", id+suffix+fCtlNum);
	node.attr("title", _this.title+suffix+fCtlNum);
	node.attr("taskName", _this.taskName+suffix+fCtlNum);
	
	node.children("textbox").text(sObj.children("textbox").text()+suffix+fCtlNum);
	
	node[0].ondblclick = attributeSet;
	node[0].oncontextmenu = contextmenuitem;
	*/

	
	var attrs = {};
	var nodeAttrs = _this.attributes;
	var len = nodeAttrs.length;
	for(var i=0;i<len;i++){
		if(nodeAttrs[i].specified){
			attrs[nodeAttrs[i].nodeName] = nodeAttrs[i].nodeValue;
		}
	}
	
	var oldId = attrs.id;
	
	var suffix = "c";
	attrs.oldId = oldId;
	attrs.id = attrs.id + suffix + fCtlNum;
	attrs.title = attrs.title + suffix + fCtlNum;
	attrs.thisCaption = attrs.nodeclass.thisCaption + suffix + fCtlNum;
	//attrs.nodeclass.thisCaption = attrs.thisCaption;

	if(pXy == null){
		attrs.leftpix = _this.offsetLeft + 50*(pasteCount+1);
		attrs.toppix = _this.offsetTop + 50*(pasteCount+1);
	}
	else{
		attrs.leftpix = pXy.x + _this.offsetLeft;
		attrs.toppix = pXy.y + _this.offsetTop;
	}
	
	var newNode = null;
	var nodeType = _this.ctlType;
	if(nodeType == CNST_CTLTYPE_TASK){
		attrs.taskName = attrs.taskName + suffix + fCtlNum;
		newNode = new tasknode(attrs['id'], attrs);
	}
	else if(nodeType == CNST_CTLTYPE_NODE){
		newNode = new nodenode(attrs['id'], attrs);
	}
	else if(nodeType == CNST_CTLTYPE_CONDITION){
		newNode = new conditionnode(attrs['id'], attrs);
	}
	else if(nodeType == CNST_CTLTYPE_PROSTATE){
		newNode = new processstatenode(attrs['id'], attrs);
	}
	else if(nodeType == CNST_CTLTYPE_END){
		newNode = new endnode(attrs['id'], attrs);
	}
	
	if(newNode != null){
		//fSelectedObj = newNode.vmlobj;
		newNode.vmlobj.oldId = oldId;
		fSelectedObjCopyArray.push(newNode.vmlobj);		
	}
	
	pdCloneNode(attrs, oldId);

	//$("#drawCanvas").append(node);
}

function modiftySamePositionNode(attrs){
	var hasSame = false;
	$("roundrect").each(function(){
		if(Math.abs($(this).offset().left - attrs.leftpix) <= 10){
			hasSame = true;
		}
	});
	return hasSame;
}

function exeCopyLine(_this, fromId, toId){
	var attrs = {};
	var nodeAttrs = _this.attributes;
	var len = nodeAttrs.length;
	for(var i=0;i<len;i++){
		if(nodeAttrs[i].specified){
			attrs[nodeAttrs[i].nodeName] = nodeAttrs[i].nodeValue;
		}
	}
	
	var oldId = attrs.id;
	
	var suffix = "c";
	attrs.oldId = oldId;
	attrs.id = attrs.id + suffix + fCtlNum;
	attrs.name = attrs.name + suffix + fCtlNum;
	attrs.thisCaption = attrs.nodeclass.thisCaption + suffix + fCtlNum;

	attrs.leftpix = _this.offsetLeft + 50;
	attrs.toppix = _this.offsetTop + 50;

	
	var nodeType = _this.ctlType;
	var newLine = null;
	if(nodeType == CNST_CTLTYPE_LINE){
		newLine = new line(attrs["id"],attrs["thisCaption"], fromId, toId, attrs.isStraint, "",attrs);
		newLine.vmlobj.oldId = oldId;
	}
	
	var newLineElem = {
			fromId: fromId,
			fromName: newLine.vmlobj.fromelement.title,
			toId: toId,
			toName: newLine.vmlobj.toelement.title,
			lineId: newLine.vmlobj.id,
			lineName: newLine.thisCaption
		};
	var oldLineElem = {
			fromId: _this.fromelement.nodeclass.thisIDName,
			fromName: _this.fromelement.nodeclass.thisCaption,
			toId: _this.toelement.nodeclass.thisIDName,
			toName: _this.toelement.nodeclass.thisCaption,
			lineId: _this.nodeclass.thisIDName,
			lineName: _this.nodeclass.thisCaption
		};
	modifyTransition(newLine.vmlobj.fromelement.ctlType, newLineElem, oldLineElem);
	addToObjArray(newLine.thisIDName,false);
	//fSelectedObjCopyArray.push(newLine.vmlobj);
	//fSelectedObj = newLine.vmlobj;
}

var isSingleNode = true;

function pdCloneNode(attrs, oldId){
	root = doc.getElementsByTagName("process-definition")[0];
	
	var nodes = null;
	var nodeType = attrs.ctlType;
	
	if(nodeType == CNST_CTLTYPE_TASK){
		nodes = root.getElementsByTagName("task-node");
	}
	else if(nodeType == CNST_CTLTYPE_NODE){
		nodes = root.getElementsByTagName("node");
	}
	else if(nodeType == CNST_CTLTYPE_CONDITION){
		nodes = root.getElementsByTagName("decision");
	}
	else if(nodeType == CNST_CTLTYPE_PROSTATE){
		nodes = root.getElementsByTagName("task-node");
	}
	else if(nodeType == CNST_CTLTYPE_END){
		nodes = root.getElementsByTagName("end-state");
	}
	
	if(nodes == null)
		return;
	
	var len = nodes.length;
	for(var i=0;i<len;i++){
		var oneNode = nodes[i];
		if(oneNode.getAttribute("atnode") == oldId){
			var newNode = oneNode.cloneNode(true);
			newNode = modifyAttr(newNode, attrs);

			if(isSingleNode){
				var trans = newNode.getElementsByTagName("transition");
				var lenTrans = trans.length;
				for(var j=0;j<lenTrans;j++){
					newNode.removeChild(trans[j]);
				}
			}

			root.appendChild(newNode);
		}
	}
}

function getPDNodes(nodeType){
	root = doc.getElementsByTagName("process-definition")[0];
	
	var nodes = null;
	
	if(nodeType == CNST_CTLTYPE_TASK){
		nodes = root.getElementsByTagName("task-node");
	}
	else if(nodeType == CNST_CTLTYPE_NODE){
		nodes = root.getElementsByTagName("node");
	}
	else if(nodeType == CNST_CTLTYPE_CONDITION){
		nodes = root.getElementsByTagName("decision");
	}
	else if(nodeType == CNST_CTLTYPE_PROSTATE){
		nodes = root.getElementsByTagName("task-node");
	}
	else if(nodeType == CNST_CTLTYPE_END){
		nodes = root.getElementsByTagName("end-state");
	}
	return nodes;
}

function modifyAttr(node, attrs){
	node.setAttribute("atnode", attrs.id);
	node.setAttribute("name", attrs.title);
	node.setAttribute("xmlflag", nCtlNum ++);
	
	if(attrs.ctlType == CNST_CTLTYPE_TASK){
		var task = node.getElementsByTagName("task")[0];
		task.setAttribute("name", attrs.title);
	}
	return node;
}

//LineElem={},fromId,toId,lineId,fromName,toName,lineName
function modifyTransition(nodeType, newLineElem, oldLineElem){
	var nodes = getPDNodes(nodeType);
	if(nodes == null)
		return;
	
	var len = nodes.length;
	var newNode = null;
	var oldNode = null;
	for(var i=0;i<len;i++){
		var oneNode = nodes[i];
		if(oneNode.getAttribute("atnode") == oldLineElem.fromId){
			oldNode = oneNode;
		}
		if(oneNode.getAttribute("atnode") == newLineElem.fromId){
			newNode = oneNode;
		}
	}
	
	if(newNode != null && oldNode != null){
		var oldTrans = oldNode.getElementsByTagName("transition");
		var lenOldTrans = oldTrans.length;
		for(var j=0;j<lenOldTrans;j++){
			var tran = oldTrans[j];
			if(tran.getAttribute("atnode") == oldLineElem.lineId){
				var copyTran = tran.cloneNode(true);
				copyTran.setAttribute("name", newLineElem.lineName);
				copyTran.setAttribute("atnode", newLineElem.lineId);
				copyTran.setAttribute("to", newLineElem.toName);
				newNode.appendChild(copyTran);
			}
		}	
	}
	
}

function minOffset(){
	var offset = {};
	offset.left = 0;
	offset.top = 0;
	for(var i=0;i<fSelectedObjArray.length;i++){
		if(fSelectedObjArray[i].ctlType != "line"){
			var left = fSelectedObjArray[i].offsetLeft;
			var top = fSelectedObjArray[i].offsetTop;
			if(offset.left == 0 || left < offset.left){
				offset.left = left;
				offset.top = top;
			}
		}
	}
	return offset;
}

function pasteNode(pXy){
	if(!hasCopyObj)
		return;
	
	var offset = minOffset();
	if(pXy != null){
		pXy.x = pXy.x - offset.left;
		pXy.y = pXy.y - offset.top;
	}

	var lines = [];
	for(var i=0;i<fSelectedObjArray.length;i++){
		var obj = fSelectedObjArray[i];
		if(obj.ctlType == "line"){
			lines.push(obj);
		}
		else if(obj.ctlNodeType == "node"){
			exeCopyNode(obj, pXy);
		}
	}
	
	for(var j=0;j<lines.length;j++){
		var fromElem = {};
		var toElem = {};
		fromElem.id = lines[j].fromelement.nodeclass.thisIDName;
		fromElem.name = lines[j].fromelement.nodeclass.thisCaption;
		fromElem.type = lines[j].fromelement.nodeclass.nodetype;
		toElem.id = lines[j].toelement.nodeclass.thisIDName;
		toElem.name = lines[j].toelement.nodeclass.thisCaption;
		toElem.type = lines[j].toelement.nodeclass.nodetype;
		
		var newFromId = null;
		var newToId = null;
		for(var ii=0;ii<fSelectedObjCopyArray.length;ii++){
			if(fSelectedObjCopyArray[ii].oldId == fromElem.id){
				newFromId = fSelectedObjCopyArray[ii].nodeclass.thisIDName;
			}
			if(fSelectedObjCopyArray[ii].oldId == toElem.id){
				newToId = fSelectedObjCopyArray[ii].nodeclass.thisIDName;
			}
		}
		if(newFromId == null && newToId != null && (fromElem.type == "task" || fromElem.type == "condition")){
			newFromId = fromElem.id;
		}
		if(newToId ==null && newFromId != null){
			newToId = toElem.id;
		}
		if(newFromId != null && newToId != null){
			exeCopyLine(lines[j], newFromId, newToId);
		}
	}
	
	//hasCopyObj = false;
}




function modifyXmlflag(){
	var pd = doc.getElementsByTagName("process-definition")[0];
	var nodes = pd.getElementsByTagName("*");
	for(var i=0,len=nodes.length;i<len;i++){
		var node = nodes[i];
		node.setAttribute("xmlflag", i);
	}
}

function isExistId(id){
	var str = buildProcessModelXml();
	var pos = str.indexOf("thisIDName=\""+id+"\"");
	if(pos == -1)
		return false;
	else
		return true;
}

function renameAllId(tplXml){
    var pragramdoc= new createXmlDom();
    try{
    	var workflow=tplXml.split("</workflow>")[0];
    	workflow=workflow.split("<workflow>")[1];
    	var program=tplXml.split("</workflow>")[1];
    	program=program.split("</doc-fragment>")[0];
    	//program=program.split("</docroot>")[0];

    	pragramdoc.loadXML(program);   	
    }
    catch(e){
    	alert("子模板文件错误。请检查导入的子模板文件。");
    	/**
    	 * 如果检测到子模板文件错误，则直接返回false，不再继续执行，否则会报脚本错误。
    	 * 修改人：yubin
    	 * 修改时间：2013-12-16
    	 */
    	return false;
    }
	
	var nodes = pragramdoc.getElementsByTagName("nodeelement");
	var lines = pragramdoc.getElementsByTagName("lineelement");
	
	var idNames = [];
	
	for(var n=0;n<nodes.length;n++){
		var idName = {};
		idName.id = nodes[n].getAttribute("thisIDName");
		idName.name = nodes[n].getAttribute("thisCaption");
		idNames.push(idName);
	}
	
	for(var n=0;n<lines.length;n++){
		var idName = {};
		idName.id = lines[n].getAttribute("thisIDName");
		idName.name = lines[n].getAttribute("thisCaption");
		idNames.push(idName);
	}
	
	var suffix = "i";
	for(var n=0;n<idNames.length;n++){
		tplXml = tplXml.replace(new RegExp("\""+idNames[n].id+"\"", "g"), "\""+idNames[n].id+suffix+fCtlNum+"\"");
		tplXml = tplXml.replace(new RegExp("\""+idNames[n].name+"\"", "g"), "\""+idNames[n].name+suffix+fCtlNum+"\"");
		tplXml = tplXml.replace(new RegExp("CDATA\\["+idNames[n].name+"\\]", "g"), "CDATA\["+idNames[n].name+suffix+fCtlNum+"\]");
		fCtlNum++;
	}
	
	var regex = /\"([0-9]*)px\"/g;
	tplXml = tplXml.replace(regex, function($0, $1){
		var px = parseInt($1) + 50;
		return "\"" + px + "px\"";
	});
	
	return tplXml;
}

function exeImportNode(attrs){
	var newNode = null;
	var nodeType = attrs.ctlType;
	attrs.id = attrs.thisIDName;
	if(nodeType == CNST_CTLTYPE_TASK){
		newNode = new tasknode(attrs['id'], attrs);
	}
	else if(nodeType == CNST_CTLTYPE_NODE){
		newNode = new nodenode(attrs['id'], attrs);
	}
	else if(nodeType == CNST_CTLTYPE_CONDITION){
		newNode = new conditionnode(attrs['id'], attrs);
	}
	else if(nodeType == CNST_CTLTYPE_PROSTATE){
		newNode = new processstatenode(attrs['id'], attrs);
	}
	else if(nodeType == CNST_CTLTYPE_END){
		newNode = new endnode(attrs['id'], attrs);
	}
	else if(nodeType == CNST_CTLTYPE_LINE){
		newNode = new line(attrs["id"], attrs["thisCaption"], attrs.fromelement, attrs.toelement, attrs.isStraint, "", attrs);
	}
	
	return newNode;
}

var emptyTplXml = "<doc-fragment><workflow><process-definition></process-definition></workflow><program><nodes></nodes><lines></lines></program></doc-fragment>";

function getExportTpl(){
	var ids = [];
	var fsoNodes = [];
	var fsoLines = [];
	for(var one=0;one<fSelectedObjArray.length;one++){
		var obj = fSelectedObjArray[one];
		if(obj.ctlType == "line"){
			fsoLines.push(obj);
		}
		else{
			if(obj.ctlType != CNST_CTLTYPE_INITIAL){
				fsoNodes.push(obj);
				ids.push(obj.nodeclass.thisIDName);
			}
		}
	}
	
	var vLines = [];
	for(var a=0;a<fsoLines.length;a++){
		var line = fsoLines[a];
		var fromNodeExist = false;
		var toNodeExist = false;
		for(var b=0;b<fsoNodes.length;b++){
			if(line.fromelement.nodeclass.thisIDName == fsoNodes[b].nodeclass.thisIDName){
				fromNodeExist = true;
			}
			if(line.toelement.nodeclass.thisIDName == fsoNodes[b].nodeclass.thisIDName){
				toNodeExist = true;
			}
		}
		if(fromNodeExist && toNodeExist){
			vLines.push(line);
			ids.push(line.nodeclass.thisIDName);
		}
	}
	

	//var str = saveProcess();
	var str = buildProcessModelXml();

	var allXml = createXmlDom();
	allXml.loadXML(str);
	
	var tplXml = createXmlDom();
	tplXml.loadXML(emptyTplXml);
	
	var pdNodes = allXml.getElementsByTagName("process-definition")[0].childNodes;
	var nodes = allXml.getElementsByTagName("nodes")[0].childNodes;
	var lines = allXml.getElementsByTagName("lines")[0].childNodes;

	var tplPd = tplXml.getElementsByTagName("process-definition")[0];
	for(var c=0;c<pdNodes.length;c++){
		for(var ci=0;ci<ids.length;ci++){
			if(pdNodes[c].getAttribute("atnode") == ids[ci]){
				var newPdNode = pdNodes[c].cloneNode(true);
				var trans = newPdNode.getElementsByTagName("transition");
				for(var t=0;t<trans.length;t++){
					var lineExist = false;
					for(var vl=0;vl<vLines.length;vl++){
						if(trans[t].getAttribute("atnode") == vLines[vl].nodeclass.thisIDName){
							lineExist = true;
						}
					}
					if(!lineExist){
						newPdNode.removeChild(trans[t]);
					}
				}
				tplPd.appendChild(newPdNode);
			}
		}
	}

	var tplNodes = tplXml.getElementsByTagName("nodes")[0];
	for(var c=0;c<nodes.length;c++){
		for(var ci=0;ci<ids.length;ci++){
			if(nodes[c].getAttribute("thisIDName") == ids[ci]){
				tplNodes.appendChild(nodes[c].cloneNode(true));
			}
		}
	}

	var tplLines = tplXml.getElementsByTagName("lines")[0];
	for(var c=0;c<lines.length;c++){
		var a = lines[i];
		for(var ci=0;ci<ids.length;ci++){
			if(lines[c].getAttribute("thisIDName") == ids[ci]){
				tplLines.appendChild(lines[c].cloneNode(true));
			}
		}
	}
	
	
	allXml = null;
	CollectGarbage();

	return tplXml;
}

/**
 * 导出子模板
 */
function exportTpl(){
	var tplXml = getExportTpl();
	
	/**
	 * 如果返回false，则说明检测到子模板错误，不允许再继续执行
	 * 如果返回不为false，则继续执行导出子模板操作
	 * 修改人：yubin
	 * 修改时间：2013-12-16
	 */
	if(tplXml){
		var tpl = tplXml.xml;
		if(tpl.replace("\r\n", "") == emptyTplXml){
			if(confirm("没有选择要导出的内容，是否导出全部（开始节点除外）？")){
				isSelRect = true;
				selectAllNodes();
				isSelRect = false;
				tplXml = getExportTpl();
			}
			else{
				//alert("请选择要导出的内容。");
				return;
			}
		}
	
		document.getElementById("process-tpl").value = tplXml.xml;
		var form = document.getElementById("export-tpl-form");
		form.submit();
	}

}

/**
 * 导入子模板
 */
function importTpl(){
	try{
	var tplXml = window.showModalDialog(scriptroot + "/workflowDesign/action/processDesign!initTplImport.action", window, "dialogWidth:500px; dialogHeight:180px; center:yes; help:no; resizable:no; status:no") ;
	if(tplXml != null && tplXml != ""){
		if(tplXml == "false"){
			alert("导入的文件为模型文件，请使用“导入模型”进行导入！");
			return false;
		}
		//var curXml = saveProcess();
		tplXml = renameAllId(tplXml);
		
		/**
    	 * 如果返回false，则说明检测到子模板文件错误，不允许再继续执行
    	 * 如果返回不为false，则继续执行导入子模板操作
    	 * 修改人：yubin
    	 * 修改时间：2013-12-16
    	 */
		if(tplXml){
		    var pragramdoc= createXmlDom();
			pragramdoc.loadXML(tplXml);
			
			var nodes = pragramdoc.getElementsByTagName("nodeelement");
			var lines = pragramdoc.getElementsByTagName("lineelement");
			
			var nodesAttrs = [];
			for(var n=0;n<nodes.length;n++){
				var props = nodes[n].childNodes;
				var attrs = nodes[n].attributes;
				var nAttrs = {};
				nAttrs.plugins = {};
				for(var na=0;na<attrs.length;na++){
					nAttrs[attrs[na].nodeName] = attrs[na].nodeValue;
				}
				for(var pa=0;pa<props.length;pa++){
					var childnode = props[pa].childNodes[0];
					
					var val = "";
					if(childnode != null){
						val = childnode.nodeValue;
					}
					
					var key = props[pa].getAttribute("key");
					nAttrs[key] = val;
					
					if(key.indexOf(pluginNamePrefix) == 0){
						nAttrs.plugins[key] = val;
					}
				}


				nodesAttrs.push(nAttrs);
				exeImportNode(nAttrs);
			}
			
			var linesAttrs = [];
			for(var l=0;l<lines.length;l++){
				var props = lines[l].childNodes;
				var attrs = lines[l].attributes;
				var lAttrs = {};
				lAttrs.plugins = {};
				for(var la=0;la<attrs.length;la++){
					lAttrs[attrs[la].nodeName] = attrs[la].nodeValue;
				}
				for(var lpa=0;lpa<props.length;lpa++){
					lAttrs[props[lpa].getAttribute("key")] = props[lpa].childNodes[0].nodeValue;
					
					var key = props[lpa].getAttribute("key");
					var val = props[lpa].childNodes[0].nodeValue;
					if(key.indexOf(pluginNamePrefix) == 0){
						lAttrs.plugins[key] = val;
					}
	
				}
				lAttrs.ctlType = CNST_CTLTYPE_LINE;
				linesAttrs.push(lAttrs);
				var newLine = exeImportNode(lAttrs);
				addToObjArray(newLine.thisIDName,false);
			}
			
			root = doc.getElementsByTagName("process-definition")[0];
			var pdNodes = pragramdoc.getElementsByTagName("process-definition")[0].childNodes;
			for(var i=0;i<pdNodes.length;i++){
				root.appendChild(pdNodes[i].cloneNode(true));
			}
			
	
			/*
			var suffix = "c";
			for(var ni=0;ni<nodesAttrs.length;ni++){
				var attrs = nodesAttrs[ni];
				var id = attrs.thisIDName;
				if(isExistId(id)){
					attrs.thisIDName = attrs.thisIDName + suffix + fCtlNum;
					attrs.id = attrs.thisIDName;
					attrs.thisCaption = attrs.thisCaption + suffix + fCtlNum;
					attrs.nodename = attrs.nodename + suffix + fCtlNum;
					attrs.leftpix = (parseInt(attrs.leftpix) + 50) + "px";
					attrs.toppix = (parseInt(attrs.toppix) + 50) + "px";
				}
				exeImportNode(attrs);
			}
			*/
	
			//root.appendChild(newNode);
	
	  		CollectGarbage(); 
	
		}
	}
	}catch (e) {
		alert(e.name + ": " + e.message);
		//alert("导入的子模板文件有误，请确认文件正确！");
	}
}





function deepCloneProperty(newAttrs, oldAttrs){
	var toStr = Object.prototype.toString;
	var arrayType = "[object Array]";
	
	newAttrs = newAttrs || {};
	
	for(var i in oldAttrs){
		if(oldAttrs.hasOwnProperty(i)){
			if(typeof oldAttrs[i] === "object" && oldAttrs[i] != "nodeclass"){
				newAttrs[i] = (toStr.call(oldAttrs[i]) === arrayType) ? [] : {};
				deepCloneProperty(newAttrs[i], oldAttrs[i]);
			}
			else{
				newAttrs[i] = oldAttrs[i];
			}
		}
	}
	return newAttrs;
}

function ownProperties(ownAttrs, domElement){

	ownAttrs = ownAttrs || {};
	
	var domAttrs = domElement.attributes;
	
	var len = domAttrs.length;
	
	for(var i=0;i<len;i++){
		if(domAttrs[i].specified){
			var name = domAttrs[i].nodeName;
			var ee;
			if(name == "nodeclass")
				ee = domElement[name];
			
			var a =  typeof domAttrs[i];
			var b = domElement[name].attributes;
			
			if(typeof domAttrs[i] === "object" && domElement[name].attributes != undefined){
				ownAttrs[name] = {};
				ownProperties(ownAttrs[name], domElement[name]);
			}
			else{
				var a = domAttrs[i].nodeValue;
				ownAttrs[name] = domAttrs[i].nodeValue;
				//ownAttrs[name] = deepCloneProperty(ownAttrs[name], domAttrs[i].nodeValue);
			}
			
		}
	}
	return ownAttrs;
}

function inArray(arr, val){
	var isIn = false;
	var arr = arr || [];
	for(var i=0;i<arr.length;i++){
		if(arr[i] == val){
			isIn = true;
		}
	}
	return isIn;
}
