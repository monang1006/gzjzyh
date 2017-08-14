
  //流程基本信息初始化
  	function initProcess(url){
		var dialog = window.showModalDialog(url, window, "dialogWidth:600px; dialogHeight:600px; center:yes; help:no; resizable:no; status:no") ;
		if(dialog != "true"){
			window.close();
		}		
	}
  
  //流程基本信息修改
  function editProcess(url){
	  url = url + "&rand=" + (new Date()).getTime();
	  var dialog = window.showModalDialog(url, window, "dialogWidth:600px; dialogHeight:600px; center:yes; help:no; resizable:no; status:no") ;		
  }
  
  //由数据库导入模型文件
  function loadProcess(str){
  		if(str!="null"){
  			
  			//zw
  			//str = correctModel(str);
  			
  		    var pragramdoc= createXmlDom();
			var workflow=str.split("</workflow>")[0];
			workflow=workflow.split("<workflow>")[1];
			var program=str.split("</workflow>")[1];
			program=program.split("</docroot>")[0];

			pragramdoc.loadXML(program);
			
			//流程文件xml节点标识
			nCtlNum = pragramdoc.getElementsByTagName("program")[0].getAttributeNode("nCtlNum").value;
			//流程表单设置
			//以当前流程为准
			processForm=pragramdoc.getElementsByTagName("program")[0].getAttributeNode("processForm").value;
			//流程类型设置
			//以当前流程为准
			processType=pragramdoc.getElementsByTagName("program")[0].getAttributeNode("processType").value;
			//流程宽度
			processRight=pragramdoc.getElementsByTagName("program")[0].getAttributeNode("processWidth").value;
			//流程高度
			processBottom=pragramdoc.getElementsByTagName("program")[0].getAttributeNode("processHeight").value;
			//是否启动流程定时器
			doc_isTimer = pragramdoc.getElementsByTagName("program")[0].getAttributeNode("processIsTimer").value;
			//流程定时器设置
			doc_timerSet = pragramdoc.getElementsByTagName("program")[0].getAttributeNode("processTimerSet").value;						
			
			noderoot=pragramdoc.getElementsByTagName("nodes")[0];
			lineroot=pragramdoc.getElementsByTagName("lines")[0];
			repaintElements(workflow,1);
			pragramdoc=null;
			noderoot=null;
			lineroot=null;
	  		CollectGarbage(); 
		}
  }
  
  
  //模型保存时遍历流程图，为每个节点生成
  var preNode = new Array();
  
  //模型遍历时暂时停止遍历的transition Id
  var stopTransition = new Array();
  
  //遍历流程图并生成preNode数组
  //added by yubin on 2008.09.02
  function generatePreNode(){
  	var start = document.getElementById("w1");
  	preNode[start.id] = "";
	var aLines = start.line.split(";");
	for( var i = 0; i < aLines.length - 1; i++ ){
		var lineObjStr = (aLines[i].split("TO")[0]).split(":")[0];
		var oLineStart = eval(lineObjStr).fromelement;
		if(oLineStart.id == start.id){
			var oLineEnd = eval(lineObjStr).toelement;
			preNode[oLineEnd.id] = preNode[start.id] + start.nodeclass.thisCaption  + ",";
			loopGeneratePreNode(oLineEnd);
		}
	}
	var point = 0; // stopTransition 数组指针
	while(point < stopTransition.length){
		var line = eval(stopTransition[point]);
		var nodeStart = line.fromelement;
		var nodeEnd = line.toelement;
//		alert(preNode[nodeStart.id]+","+nodeStart.id+","+preNode[nodeEnd.id]+","+nodeEnd.id);
//		alert(preNode["w20"]);
		if(preNode[nodeStart.id].indexOf(nodeEnd.nodeclass.thisCaption + ",") == -1){ // 判断是否流程图中的环形流程
//			alert("true");
			var nodes = preNode[nodeStart.id].split(",");
			for(var j = 0; j < nodes.length-1; j++){
				if(preNode[nodeEnd.id] == null){
					preNode[nodeEnd.id] = "";
				}
				if(preNode[nodeEnd.id].indexOf(nodes[j]+",") == -1 && nodeEnd.id != nodes[j]){
					preNode[nodeEnd.id] = preNode[nodeEnd.id] + nodes[j] + ",";
				}
			}
			if(preNode[nodeEnd.id].indexOf(nodeStart.nodeclass.thisCaption) == -1){
				preNode[nodeEnd.id] = preNode[nodeEnd.id] + nodeStart.nodeclass.thisCaption  + ",";
			}
			loopGeneratePreNode(nodeEnd); // 暂时停止的遍历继续执行
		}else{
			var nodes = preNode[nodeStart.id].split(",");
			for(var j = 0; j < nodes.length-1; j++){
				if(preNode[nodeEnd.id] == null){
					preNode[nodeEnd.id] = "";
				}
				if(preNode[nodeEnd.id].indexOf(nodes[j]+",") == -1 && nodeEnd.id != nodes[j]){
					preNode[nodeEnd.id] = preNode[nodeEnd.id] + nodes[j] + ",";
				}
			}
			if(preNode[nodeEnd.id].indexOf(nodeStart.nodeclass.thisCaption) == -1){
				preNode[nodeEnd.id] = preNode[nodeEnd.id] + nodeStart.nodeclass.thisCaption  + ",";
			}			
		}
		point = point + 1;
	}
//	for(var i in preNode){
//		alert(i + "      " + preNode[i]);
//	}
  }
  
  //递归遍历流程图
  //added by yubin on 2008.09.02
  function loopGeneratePreNode(node){
	var aLines = node.line.split(";");
	for( var i = 0; i < aLines.length - 1; i++ ){
		var lineObjStr = (aLines[i].split("TO")[0]).split(":")[0];
		var oLineStart = eval(lineObjStr).fromelement;
		if(oLineStart.id == node.id){
			var oLineEnd = eval(lineObjStr).toelement;
			if(preNode[node.id].indexOf(oLineEnd.nodeclass.thisCaption + ",") == -1){ // 判断是否流程图中的环形流程
				if(checkLoop(oLineEnd, node)){ // 如果两个节点间有回路，则暂时停止遍历
					stopTransition[stopTransition.length] = lineObjStr;
				}else{
					var nodes = preNode[node.id].split(",");
					for(var j = 0; j < nodes.length-1; j++){
						if(preNode[oLineEnd.id] == null){ // 为空时首先赋空值
							preNode[oLineEnd.id] = "";
						}
						// 前驱不是本节点或当前前驱内没有重复节点
						if(preNode[oLineEnd.id].indexOf(nodes[j]+",") == -1 && oLineEnd.id != nodes[j]){
							preNode[oLineEnd.id] = preNode[oLineEnd.id] + nodes[j] + ",";
						}
					}
					if(preNode[oLineEnd.id].indexOf(node.nodeclass.thisCaption + ",") == -1){
						preNode[oLineEnd.id] = preNode[oLineEnd.id] + node.nodeclass.thisCaption  + ",";
					}
					loopGeneratePreNode(oLineEnd);					
				}
			}else{
					var nodes = preNode[node.id].split(",");
					for(var j = 0; j < nodes.length-1; j++){
						if(preNode[oLineEnd.id] == null){ // 为空时首先赋空值
							preNode[oLineEnd.id] = "";
						}
						// 前驱不是本节点或当前前驱内没有重复节点
						if(preNode[oLineEnd.id].indexOf(nodes[j]+",") == -1 && oLineEnd.id != nodes[j]){
							preNode[oLineEnd.id] = preNode[oLineEnd.id] + nodes[j] + ",";
						}
					}
					if(preNode[oLineEnd.id].indexOf(node.nodeclass.thisCaption + ",") == -1){
						preNode[oLineEnd.id] = preNode[oLineEnd.id] + node.nodeclass.thisCaption  + ",";
					}		
			}
		}
	}
	
/*	for( var i = 0; i < aLines.length - 1; i++ ){
		var lineObjStr = (aLines[i].split("TO")[0]).split(":")[0];
		var oLineStart = eval(lineObjStr).fromelement;
		if(oLineStart.id == node.id){
			var oLineEnd = eval(lineObjStr).toelement;
			if(preNode[node.id].indexOf(oLineEnd.nodeclass.thisCaption) == -1){ // 判断是否流程图中的环形流程
				loopGeneratePreNode(oLineEnd);
			}
		}
	}*/	  	
  }
  
  //判断两个节点间是否有回路
  //added by yubin on 2008.09.02
  function checkLoop(nodeSource, nodeDestinate){
  	var hasVisited = new Array();
	return loopCheckLoop(hasVisited, nodeSource, nodeDestinate);  	
  }
  
  //递归判断两个节点间是否有回路
  //added by yubin on 2008.09.02
  function loopCheckLoop(hasVisited, nodeSource, nodeDestinate){
	var aLines = nodeSource.line.split(";");
	for( var i = 0; i < aLines.length - 1; i++ ){
		var lineObjStr = (aLines[i].split("TO")[0]).split(":")[0];
		var oLineStart = eval(lineObjStr).fromelement;
		if(oLineStart.id == nodeSource.id){
			var oLineEnd = eval(lineObjStr).toelement;
			if(hasVisited[oLineEnd.id] != "1"){
				if(oLineEnd.id == nodeDestinate.id){
					return true;
				}else{
					hasVisited[oLineEnd.id] = "1";
					if(loopCheckLoop(hasVisited, oLineEnd, nodeDestinate)){
						return true;
					}
				}
			}
		}
	}
	return false;  	
  }
  
  
  //模型文件导出到数据库中
  function saveProcess(){
	// 流程模型内容
	var processXml = "";
	
  	// 验证流程模型
	var isValidated = processValidate();
	// 如果验证通过，则继续执行
    if(isValidated){
    	prepareProcessModel();
    	processXml = buildProcessModelXml();
    }
    
    return processXml;
  }



  //导入模型文件  
  function loadDoc()   
  {		
 	try{
		var dialog = window.showModalDialog(scriptroot + "/workflowDesign/action/processDesign!initImport.action", window, "dialogWidth:500px; dialogHeight:180px; center:yes; help:no; resizable:no; status:no") ;
		if(dialog != null && dialog != ""){
			if(dialog == "false"){
				alert("导入的文件为子模板文件，请使用“导入子模板”进行导入！");
				return false;
			}	
			var str = dialog;   
  		    var pragramdoc= createXmlDom();
			var workflow=str.split("</workflow>")[0];
			workflow=workflow.split("<workflow>")[1];
			var program=str.split("</workflow>")[1];
			program=program.split("</docroot>")[0];

			pragramdoc.loadXML(program);

			//流程文件xml节点标识
			nCtlNum = pragramdoc.getElementsByTagName("program")[0].getAttributeNode("nCtlNum").value;	
			//流程表单设置
			//以当前流程为准
			//processForm=pragramdoc.getElementsByTagName("program")[0].getAttributeNode("processForm").value;
			pragramdoc.getElementsByTagName("program")[0].getAttributeNode("processForm").value = processForm;
			//流程类型设置
			//以当前流程为准
			//processType=pragramdoc.getElementsByTagName("program")[0].getAttributeNode("processType").value;
			pragramdoc.getElementsByTagName("program")[0].getAttributeNode("processType").value = processType;
			//流程宽度
			processRight=pragramdoc.getElementsByTagName("program")[0].getAttributeNode("processWidth").value;
			//流程高度
			processBottom=pragramdoc.getElementsByTagName("program")[0].getAttributeNode("processHeight").value;
			//是否启动流程定时器
			doc_isTimer = pragramdoc.getElementsByTagName("program")[0].getAttributeNode("processIsTimer").value;
			//流程定时器设置
			doc_timerSet = pragramdoc.getElementsByTagName("program")[0].getAttributeNode("processTimerSet").value;
			
			noderoot=pragramdoc.getElementsByTagName("nodes")[0];
			lineroot=pragramdoc.getElementsByTagName("lines")[0];
			repaintElements(workflow,1);
			pragramdoc=null;
			noderoot=null;
			lineroot=null;
	  		CollectGarbage(); 
		}  		
	}catch(e){
       alert("导入的模型文件有误，请确认模型文件正确！");
	}   
  }  

  //导出模型文件
  function   saveDoc(){
	var str = saveProcess();
	if(str == ""){
		/**
		 * 结束节点的提示统一在saveProcess()方法中提示
		 * 修改人：yubin
		 * 修改时间：2013-12-16
		 */
		//alert("模型中至少要有一个结束节点！");
		return false;
	}
	document.getElementById("processfile").value = str;
	var form = document.getElementById("processForm");
	form.submit();
  }


//zw

function createXmlDom(){
	if(typeof arguments.callee.activeXString != "string"){
		var vers = ["MSXML2.DOMDocument.6.0", "MSXML2.DOMDocument.3.0", "MSXML2.DOMDocument"];
		for(var i=0,len=vers.length;i<len;i++){
			var xmlDom = new ActiveXObject(vers[i]);
			arguments.callee.activeXString = vers[i];
			return xmlDom;
		}
	}
	return new ActiveXObject(arguments.callee.activeXString);
}

/**
 * 该方法主要是在保存的时候去除重复的迁移线和错误的迁移线，并未真正解决产生错误迁移线的问题<br>
 * 经过和曹钦确认，先注释该方法，待测试人员测试发现相同问题后找到产生问题的真正原因并解决。
 */
/**
function clearRepetitionLine(doc){
	var nodes = doc.getElementsByTagName("process-definition")[0].childNodes;
	for(var i=0,len=nodes.length;i<len;i++){
		var trans = nodes[i].getElementsByTagName("transition");
		var arr = [];
		for(var j=0,jLen=trans.length;j<jLen;j++){
			var val = trans[j].getAttribute("atnode");
			if(inArray(arr,val)){
				nodes[i].removeChild(trans[j]);
			}
			else{
				arr.push(val);
			}
		}
	}
}
**/

/**
 * 流程模型验证，包括流程整体验证和各个节点/迁移线的单独验证两部分
 * 1. 如果验证成功，返回true
 * 2. 如果验证失败，alert错误信息，并返回false
 */
function processValidate(){
	var isValidated = true;
	
	// 遍历流程模型各个节点/迁移线
	for (var i=0; i<sltObjArray.options.length; i++) {
		var objid = sltObjArray.options[i].value;
		var obj = document.getElementById(objid);
		var node = obj.nodeclass;
		// 执行节点/迁移线上的每个验证器
		if(node.validateFilters.length > 0){
			for(var j=0; j<node.validateFilters.length; j++){
				isValidated = node.validateFilters[j].call(objid);
				// 如果验证失败，则返回
				if(!isValidated){
					break;
				}
			}
		}
		// 如果验证失败，则返回
		if(!isValidated){
			break;
		}
	}
	
	// 流程整体验证
	if(isValidated && model.validateFilters.length > 0){
		for(var j=0; j<model.validateFilters.length; j++){
			isValidated = model.validateFilters[j].call(sltObjArray.options);
			// 如果验证失败，则返回
			if(!isValidated){
				break;
			}
		}
	}
	
	return isValidated;
}

/**
 * 自动计算当前流程模型的一些相关信息，如模型图大小、节点的前置节点信息等
 */
function prepareProcessModel(){
	generatePreNode();
	countMaxpoint();
}

/**
 * 构造流程模型XML
 * @returns 流程模型XML字符串
 */
function buildProcessModelXml(){
	pragramdoc= createXmlDom();
	var programroot=pragramdoc.createNode(1,"program","");
	pragramdoc.appendChild(programroot);
	
	//创建属性 
	var r = pragramdoc.createAttribute("nCtlNum"); 
	r.value = nCtlNum;
	//添加属性 
	programroot.setAttributeNode(r);	
	
	//创建属性 
	var r = pragramdoc.createAttribute("processType"); 
	r.value=processType;
	//添加属性 
	programroot.setAttributeNode(r);		
	
	//创建属性 
	var r = pragramdoc.createAttribute("processForm"); 
	r.value=processForm;
	//添加属性 
	programroot.setAttributeNode(r);
	
	//创建属性 
	var r = pragramdoc.createAttribute("processWidth"); 
	r.value = processRight + 20;
	//添加属性 
	programroot.setAttributeNode(r);
	
	//创建属性 
	var r = pragramdoc.createAttribute("processHeight"); 
	r.value = processBottom + 20;
	//添加属性 
	programroot.setAttributeNode(r);
	
	//是否设置定时器
	var r = pragramdoc.createAttribute("processIsTimer"); 
	r.value = doc_isTimer;
	//添加属性 
	programroot.setAttributeNode(r);
	
	//定时器信息
	var r = pragramdoc.createAttribute("processTimerSet"); 
	r.value = doc_timerSet;
	//添加属性 
	programroot.setAttributeNode(r);
	
	//判断迁移线是否加了轨迹action，若没有则加上，作为工具使用，不放入正式功能中
	/**
	var transitions = doc.getElementsByTagName("transition");
	for(var i=0; i<transitions.length; i++){
		var transition = transitions[i];
		var actions = transition.getElementsByTagName("action");
		if(actions == null || actions.length == 0){
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
			transition.appendChild(n1);
		}else{
			var bool = true;
			for(var j=0; j<actions.length; j++){
				var action = actions[j];
				if(action.name == flowTrackName){
					bool = false;
				}
			}
			if(bool){
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
				transition.appendChild(n1);
			}
		}
	}
	**/
	
	//如果设置了定时器信息则在 JBPM 模型文件中加入定时器信息
	var processLeaveEvent = null;
	var timerCreateEvent = null;
	var startNode = doc.getElementsByTagName("start-state")[0];
	var events = startNode.getElementsByTagName("event");
	for(var i=0; i<events.length; i++){
		if(events[i].getAttributeNode("type")!=null && events[i].getAttributeNode("type").value=="node-leave"){
			processLeaveEvent = events[i];
		}else if(events[i].getAttributeNode("type")!=null && events[i].getAttributeNode("type").value=="timer-create"){
			timerCreateEvent = events[i];
		}
	}	
	//设置流程进入触发定时器操作
	if(doc_isTimer == "1"){
		var timerSet = doc_timerSet.split(",");
		var wholeLast;
		var preLast;
		//流程持续时间换算成分钟
		if(timerSet[1] == "day"){
			wholeLast = parseInt(timerSet[0])*24*60;
		}else if(timerSet[1] == "hour"){
			wholeLast = parseInt(timerSet[0])*60;
		}else if(timerSet[1] == "minute"){
			wholeLast = parseInt(timerSet[0]);
		}
		//提前催办时间换算成分钟
		if(timerSet[3] == "day"){
			preLast = parseInt(timerSet[2])*24*60;
		}else if(timerSet[3] == "hour"){
			preLast = parseInt(timerSet[2])*60;
		}else if(timerSet[3] == "minute"){
			preLast = parseInt(timerSet[2]);
		}
		var duration = wholeLast - preLast;		
		// 起始点进入事件已存在
		if(processLeaveEvent != null){
			var k=1;
			var createActions = processLeaveEvent.getElementsByTagName("create-timer");
			for(var i=0; i<createActions.length; i++){
				if(createActions[i].getAttributeNode("name")!=null && createActions[i].getAttributeNode("name").value=="com.strongit.processCreateTimer"){
					createActions[i].getAttributeNode("duedate").value = duration + " minute";
					//判断是否需要重复催办
					if(timerSet[4] != "0"){
						//判断repeat节点是否存在，若不存在，则需要新建repeat节点
						var r = createActions[i].getAttributeNode("repeat");
						if(r == null){
							r = doc.createAttribute("repeat");
							createActions[i].setAttributeNode(r);
						}
						createActions[i].getAttributeNode("repeat").value = timerSet[4] + " " + timerSet[5];
					}else{
						createActions[i].removeAttribute("repeat");
					}
					var action = createActions[i].getElementsByTagName("action")[0];
					action.getAttributeNode("class").value = timerAction;
					k=0;
					break;
				}
			}
			if(k==1){
				var n = doc.createNode(1, "create-timer", "");
				
				//添加xml节点标识属性
				var r = doc.createAttribute("xmlflag");
				r.value = nCtlNum ++; 
		
					//添加属性 
				n.setAttributeNode(r);					
				
				var r = doc.createAttribute("name");
				r.value = "com.strongit.processCreateTimer";
				n.setAttributeNode(r);
				r = doc.createAttribute("duedate");
				r.value = duration + " minute";;
				n.setAttributeNode(r);
				
				//判断是否需要重复催办
				if(timerSet[4] != "0"){
					r = doc.createAttribute("repeat");
					r.value = timerSet[4] + " " + timerSet[5];;
					n.setAttributeNode(r);
				}				

				var n2 = doc.createNode(1, "action", "");
				
				//添加xml节点标识属性
				var r = doc.createAttribute("xmlflag");
				r.value = nCtlNum ++; 
		
					//添加属性 
				n2.setAttributeNode(r);				
															 
				r = doc.createAttribute("class"); 
				r.value = timerAction;
				n2.setAttributeNode(r);					
				n.appendChild(n2);
				processLeaveEvent.appendChild(n);				
			}	
		}else{
			processLeaveEvent = doc.createNode(1,"event","");
			
			//添加xml节点标识属性
			var r = doc.createAttribute("xmlflag");
			r.value = nCtlNum ++; 
			//添加属性 
			processLeaveEvent.setAttributeNode(r);			
			
			var r = doc.createAttribute("type"); 
			r.value="node-leave";
			processLeaveEvent.setAttributeNode(r);
			var n = doc.createNode(1, "create-timer", "");
			
			//添加xml节点标识属性
			var r = doc.createAttribute("xmlflag");
			r.value = nCtlNum ++; 
			//添加属性 
			n.setAttributeNode(r);			
			
			r = doc.createAttribute("name");
			r.value = "com.strongit.processCreateTimer";
			n.setAttributeNode(r);
			r = doc.createAttribute("duedate");
			r.value = duration + " minute";;
			n.setAttributeNode(r);
			
			//判断是否需要重复催办
			if(timerSet[4] != "0"){
				r = doc.createAttribute("repeat");
				r.value = timerSet[4] + " " + timerSet[5];
				n.setAttributeNode(r);
			}
			
			var n2 = doc.createNode(1, "action", "");
			
			//添加xml节点标识属性
			var r = doc.createAttribute("xmlflag");
			r.value = nCtlNum ++; 
			//添加属性 
			n2.setAttributeNode(r);			
														 
			r = doc.createAttribute("class"); 
			r.value = timerAction;
			n2.setAttributeNode(r);					
			n.appendChild(n2);
			processLeaveEvent.appendChild(n);
			startNode.appendChild(processLeaveEvent);				
		}
		// 起始点Timer-Create事件已存在
		if(timerCreateEvent != null){
			var k=1;
			var createActions = timerCreateEvent.getElementsByTagName("action");
			for(var i=0; i<createActions.length; i++){
				if(createActions[i].getAttributeNode("name")!=null && createActions[i].getAttributeNode("name").value == createTimerName){
					createActions[i].getAttributeNode("class").value = createTimerClass;
					k=0;
					break;
				}
			}
			if(k==1){
				var n = doc.createNode(1, "action", "");
				
				//添加xml节点标识属性
				var r = doc.createAttribute("xmlflag");
				r.value = nCtlNum ++; 
		
					//添加属性 
				n.setAttributeNode(r);					
				
				var r = doc.createAttribute("name");
				r.value = createTimerName;
				n.setAttributeNode(r);
				r = doc.createAttribute("class");
				r.value = createTimerClass;
				n.setAttributeNode(r);				
				timerCreateEvent.appendChild(n);				
			}	
		}else{
			timerCreateEvent = doc.createNode(1,"event","");
			
			//添加xml节点标识属性
			var r = doc.createAttribute("xmlflag");
			r.value = nCtlNum ++; 
			//添加属性 
			timerCreateEvent.setAttributeNode(r);			
			
			var r = doc.createAttribute("type"); 
			r.value="timer-create";
			timerCreateEvent.setAttributeNode(r);
			var n2 = doc.createNode(1, "action", "");
			
			//添加xml节点标识属性
			var r = doc.createAttribute("xmlflag");
			r.value = nCtlNum ++; 
			//添加属性 
			n2.setAttributeNode(r);
			
			var r = doc.createAttribute("name");
			r.value = createTimerName;
			n2.setAttributeNode(r);			
														 
			r = doc.createAttribute("class"); 
			r.value = createTimerClass;
			n2.setAttributeNode(r);					
			timerCreateEvent.appendChild(n2);
			startNode.appendChild(timerCreateEvent);				
		}		
	}else{
		if(processLeaveEvent!=null){
			var actions=processLeaveEvent.getElementsByTagName("create-timer");
			for(var i=0;i<actions.length;i++){
				if(actions[i].getAttributeNode("name")!=null && actions[i].getAttributeNode("name").value=="com.strongit.processCreateTimer"){
					actions[i].parentNode.removeChild(actions[i]);
					break;
				}
			}
		}
		if(processLeaveEvent!=null && processLeaveEvent.childNodes.length==0){
			processLeaveEvent.parentNode.removeChild(processLeaveEvent);
		}
		if(timerCreateEvent!=null){
			var actions=timerCreateEvent.getElementsByTagName("action");
			for(var i=0;i<actions.length;i++){
				if(actions[i].getAttributeNode("name")!=null && actions[i].getAttributeNode("name").value == createTimerName){
					actions[i].parentNode.removeChild(actions[i]);
					break;
				}
			}
		}
		if(timerCreateEvent!=null && timerCreateEvent.childNodes.length==0){
			timerCreateEvent.parentNode.removeChild(timerCreateEvent);
		}		
	}
	//流程定时器添加结束	
	noderoot=pragramdoc.createNode(1,"nodes","");
	programroot.appendChild(noderoot);
	lineroot=pragramdoc.createNode(1,"lines","");
	programroot.appendChild(lineroot);
	
	for (var i=0;i<sltObjArray.options.length;i++) {
		var objid=sltObjArray.options[i].value;
		var element=eval(objid);
		if(element.ctlType==CNST_CTLTYPE_LINE){
				var n = pragramdoc.createNode(1,"lineelement","");
				//创建属性 
				var r = pragramdoc.createAttribute("thisIDName"); 
					r.value=element.id;
					//添加属性 
					n.setAttributeNode(r); 
				//创建属性 
				var r = pragramdoc.createAttribute("thisCaption"); 
					r.value=element.nodeclass.thisCaption;

					//添加属性 
					n.setAttributeNode(r);
				//创建属性 
				var r = pragramdoc.createAttribute("isStraint"); 
					r.value=element.isStraint;

					//添加属性 
					n.setAttributeNode(r);
				var r = pragramdoc.createAttribute("fromelement"); 
					r.value=element.fromelement.id;

					//添加属性 
					n.setAttributeNode(r);
				//为了增加迁移线插件属性功能，保存原节点名称作为迁移线唯一标识部分
				var r = pragramdoc.createAttribute("fromelementName"); 
					r.value=element.fromelement.nodeclass.thisCaption;

					//添加属性 
					n.setAttributeNode(r);				
				var r = pragramdoc.createAttribute("toelement"); 
					r.value=element.toelement.id;

					//添加属性 
					n.setAttributeNode(r);
				//为了增加迁移线插件属性功能，保存目标节点名称作为迁移线唯一标识部分
				var r = pragramdoc.createAttribute("toelementName");
					r.value=element.toelement.nodeclass.thisCaption;

					//添加属性 
					n.setAttributeNode(r);
					
				// 改成Property形式
				var nn = pragramdoc.createNode(1,"property","");
				var r = pragramdoc.createAttribute("key"); 
					r.value = "points";
					//添加属性 
					nn.setAttributeNode(r);	
				var linestr=''+element.posArray[0]+','+element.posArray[1]+','+element.posArray[2]+','+element.posArray[3]+','+element.posArray[4]+','+element.posArray[5]+','+element.posArray[6]+','+element.posArray[7]+','+element.posArray[8]+','+element.posArray[9]+'';
				var cdata = pragramdoc.createCDATASection(linestr);
				nn.appendChild(cdata);
				//nn.text = linestr;
				n.appendChild(nn);
				
				var plugins = element.nodeclass.vmlobj.plugins;//迁移线设置插件属性信息
				for(var flag in plugins){
					// 改成Property形式
					var nn = pragramdoc.createNode(1,"property","");
					var r = pragramdoc.createAttribute("key"); 
					r.value = flag;
					//添加属性 
					nn.setAttributeNode(r);	
					var cdata = pragramdoc.createCDATASection(plugins[flag]);
					nn.appendChild(cdata);
					//nn.text = plugins[flag];
					n.appendChild(nn);
				}
				
				lineroot.appendChild(n);
		}else{
			// 用户需要在页面重载的方法，主要用于在生成节点Xml之前执行的自定义动作，传入参数为节点Dom对象
			if (typeof(createNodeXml) != "undefined") {
				createNodeXml(element);
			}
			
			var nodeele = pragramdoc.createNode(1,"nodeelement","");
			var r1 = pragramdoc.createAttribute("thisIDName"); 
			r1.value=element.nodeclass.thisIDName;    
			//添加属性 
			nodeele.setAttributeNode(r1); 
			var r1 = pragramdoc.createAttribute("thisCaption"); 
			r1.value=element.nodeclass.thisCaption;    
			//添加属性 
			nodeele.setAttributeNode(r1); 
			var r1 = pragramdoc.createAttribute("leftpix"); 
			r1.value=element.style.left;    
			//添加属性 
			nodeele.setAttributeNode(r1); 
			var r1 = pragramdoc.createAttribute("toppix"); 
			r1.value=element.style.top;    
			//添加属性 
			nodeele.setAttributeNode(r1); 
			var r1 = pragramdoc.createAttribute("ctlType"); 
			r1.value=element.ctlType;    
			//添加属性 
			nodeele.setAttributeNode(r1); 
			var r1 = pragramdoc.createAttribute("ctlName"); 
			r1.value=element.ctlName;    
			//添加属性 
			nodeele.setAttributeNode(r1);
			var r1 = pragramdoc.createAttribute("nodename"); 
			r1.value=element.nodeclass.textobj.innerText;    
			//添加属性 
			nodeele.setAttributeNode(r1);
			
			// 改成Property形式
			var nn = pragramdoc.createNode(1,"property","");
			var r = pragramdoc.createAttribute("key"); 
			r.value = "nodeEnter";
			//添加属性 
			nn.setAttributeNode(r);	
			var cdata = pragramdoc.createCDATASection(element.nodeclass.vmlobj.nodeEnter);
			nn.appendChild(cdata);
			//nn.text = element.nodeclass.vmlobj.nodeEnter;
			nodeele.appendChild(nn);
			

			
			
			//zw
			// 改成Property形式
			var nn = pragramdoc.createNode(1,"property","");
			var r = pragramdoc.createAttribute("key"); 
			r.value = "eaNodeEnterName";
			//添加属性 
			nn.setAttributeNode(r);	
			var cdata = pragramdoc.createCDATASection(element.nodeclass.vmlobj.eaNodeEnterName);
			nn.appendChild(cdata);
			nodeele.appendChild(nn);

			// 改成Property形式
			var nn = pragramdoc.createNode(1,"property","");
			var r = pragramdoc.createAttribute("key"); 
			r.value = "eaNodeLeaveName";
			//添加属性 
			nn.setAttributeNode(r);	
			var cdata = pragramdoc.createCDATASection(element.nodeclass.vmlobj.eaNodeLeaveName);
			nn.appendChild(cdata);
			nodeele.appendChild(nn);

			
			
			
			
			// 改成Property形式
			var nn = pragramdoc.createNode(1,"property","");
			var r = pragramdoc.createAttribute("key"); 
			r.value = "nodeLeave";
			//添加属性 
			nn.setAttributeNode(r);	
			var cdata = pragramdoc.createCDATASection(element.nodeclass.vmlobj.nodeLeave);
			nn.appendChild(cdata);
			//nn.text = element.nodeclass.vmlobj.nodeLeave;
			nodeele.appendChild(nn);
			
			// 改成Property形式
			var nn = pragramdoc.createNode(1,"property","");
			var r = pragramdoc.createAttribute("key"); 
			r.value = "nodeForm";
			//添加属性 
			nn.setAttributeNode(r);	
			var cdata = pragramdoc.createCDATASection(element.nodeclass.vmlobj.nodeForm);
			nn.appendChild(cdata);
			//nn.text = element.nodeclass.vmlobj.nodeForm;
			nodeele.appendChild(nn);
			
			// 改成Property形式
			var nn = pragramdoc.createNode(1,"property","");
			var r = pragramdoc.createAttribute("key"); 
			r.value = "formPriv";
			//添加属性 
			nn.setAttributeNode(r);	
			var cdata = pragramdoc.createCDATASection(element.nodeclass.vmlobj.formPriv);
			nn.appendChild(cdata);
			//nn.text = element.nodeclass.vmlobj.formPriv;
			nodeele.appendChild(nn);
			
			// 改成Property形式
			var nn = pragramdoc.createNode(1,"property","");
			var r = pragramdoc.createAttribute("key"); 
			r.value = "isSetAction";
			//添加属性 
			nn.setAttributeNode(r);	
			var cdata = pragramdoc.createCDATASection(element.nodeclass.vmlobj.isSetAction);
			nn.appendChild(cdata);
			//nn.text = element.nodeclass.vmlobj.isSetAction;
			nodeele.appendChild(nn);
			
			// 改成Property形式
			var nn = pragramdoc.createNode(1,"property","");
			var r = pragramdoc.createAttribute("key"); 
			r.value = "actionSet";
			//添加属性 
			nn.setAttributeNode(r);	
			var cdata = pragramdoc.createCDATASection(element.nodeclass.vmlobj.actionSet);
			nn.appendChild(cdata);
			//nn.text = element.nodeclass.vmlobj.actionSet;
			nodeele.appendChild(nn);
			
			var plugins = element.nodeclass.vmlobj.plugins;//节点设置插件属性信息
			for(var flag in plugins){
				// 改成Property形式
				var nn = pragramdoc.createNode(1,"property","");
				var r = pragramdoc.createAttribute("key"); 
				r.value = flag;
				//添加属性 
				nn.setAttributeNode(r);	
				var cdata = pragramdoc.createCDATASection(plugins[flag]);
				nn.appendChild(cdata);
				//nn.text = plugins[flag];
				nodeele.appendChild(nn);
			}

			if(element.ctlType == CNST_CTLTYPE_INITIAL){
				// 添加定时器催办信息
				// 改成Property形式
				var nn = pragramdoc.createNode(1,"property","");
				var r = pragramdoc.createAttribute("key"); 
				r.value = "timerSet";
				//添加属性 
				nn.setAttributeNode(r);	
				var cdata = pragramdoc.createCDATASection(doc_timerSet);
				nn.appendChild(cdata);
				//nn.text = doc_timerSet;
				nodeele.appendChild(nn);								
			}else if(element.ctlType == CNST_CTLTYPE_TASK){
				// 添加前驱节点集合
				// 改成Property形式
				var nn = pragramdoc.createNode(1,"property","");
				var r = pragramdoc.createAttribute("key"); 
				r.value = "preNodes";
				//添加属性 
				nn.setAttributeNode(r);	
				if(preNode[element.id]){
					var cdata = pragramdoc.createCDATASection(preNode[element.id].substring(0, preNode[element.id].length-1));
					nn.appendChild(cdata);
				}
				//nn.text = preNode[element.id].substring(0, preNode[element.id].length-1);
				nodeele.appendChild(nn);
				
				// 改成Property形式
				var nn = pragramdoc.createNode(1,"property","");
				var r = pragramdoc.createAttribute("key"); 
				r.value = "isCounterSign";
				//添加属性 
				nn.setAttributeNode(r);	
				var cdata = pragramdoc.createCDATASection(element.nodeclass.vmlobj.isCounterSign);
				nn.appendChild(cdata);
				//nn.text = element.nodeclass.vmlobj.isCounterSign;
				nodeele.appendChild(nn);
				
				// 改成Property形式
				var nn = pragramdoc.createNode(1,"property","");
				var r = pragramdoc.createAttribute("key"); 
				r.value = "taskName";
				//添加属性 
				nn.setAttributeNode(r);	
				var cdata = pragramdoc.createCDATASection(element.nodeclass.vmlobj.taskName);
				nn.appendChild(cdata);
				//nn.text = element.nodeclass.vmlobj.taskName;
				nodeele.appendChild(nn);
				
				
				//zw
				// 改成Property形式
				var nn = pragramdoc.createNode(1,"property","");
				var r = pragramdoc.createAttribute("key"); 
				r.value = "eaTaskStartName";
				//添加属性 
				nn.setAttributeNode(r);	
				var cdata = pragramdoc.createCDATASection(element.nodeclass.vmlobj.eaTaskStartName);
				nn.appendChild(cdata);
				nodeele.appendChild(nn);

				// 改成Property形式
				var nn = pragramdoc.createNode(1,"property","");
				var r = pragramdoc.createAttribute("key"); 
				r.value = "eaTaskEndName";
				//添加属性 
				nn.setAttributeNode(r);	
				var cdata = pragramdoc.createCDATASection(element.nodeclass.vmlobj.eaTaskEndName);
				nn.appendChild(cdata);
				nodeele.appendChild(nn);

				
				
				// 改成Property形式
				var nn = pragramdoc.createNode(1,"property","");
				var r = pragramdoc.createAttribute("key"); 
				r.value = "taskStart";
				//添加属性 
				nn.setAttributeNode(r);	
				var cdata = pragramdoc.createCDATASection(element.nodeclass.vmlobj.taskStart);
				nn.appendChild(cdata);
				//nn.text = element.nodeclass.vmlobj.taskStart;
				nodeele.appendChild(nn);												

				// 改成Property形式
				var nn = pragramdoc.createNode(1,"property","");
				var r = pragramdoc.createAttribute("key"); 
				r.value = "taskEnd";
				//添加属性 
				nn.setAttributeNode(r);	
				var cdata = pragramdoc.createCDATASection(element.nodeclass.vmlobj.taskEnd);
				nn.appendChild(cdata);
				//nn.text = element.nodeclass.vmlobj.taskEnd;
				nodeele.appendChild(nn);

				// 改成Property形式
				var nn = pragramdoc.createNode(1,"property","");
				var r = pragramdoc.createAttribute("key"); 
				r.value = "handleActor";
				//添加属性 
				nn.setAttributeNode(r);	
				var cdata = pragramdoc.createCDATASection(element.nodeclass.vmlobj.handleActor);
				nn.appendChild(cdata);
				//nn.text = element.nodeclass.vmlobj.handleActor;
				nodeele.appendChild(nn);
				
				// 改成Property形式
				var nn = pragramdoc.createNode(1,"property","");
				var r = pragramdoc.createAttribute("key"); 
				r.value = "realActors";
				//添加属性 
				nn.setAttributeNode(r);	
				var cdata = pragramdoc.createCDATASection(element.nodeclass.vmlobj.realActors);
				nn.appendChild(cdata);
				//nn.text = element.nodeclass.vmlobj.realActors;
				nodeele.appendChild(nn);								

				// 改成Property形式
				var nn = pragramdoc.createNode(1,"property","");
				var r = pragramdoc.createAttribute("key"); 
				r.value = "noApprove";
				//添加属性 
				nn.setAttributeNode(r);	
				var cdata = pragramdoc.createCDATASection(element.nodeclass.vmlobj.noApprove);
				nn.appendChild(cdata);
				//nn.text = element.nodeclass.vmlobj.noApprove;
				nodeele.appendChild(nn);	

				// 改成Property形式
				var nn = pragramdoc.createNode(1,"property","");
				var r = pragramdoc.createAttribute("key"); 
				r.value = "taskStartMail";
				//添加属性 
				nn.setAttributeNode(r);	
				var cdata = pragramdoc.createCDATASection(element.nodeclass.vmlobj.taskStartMail);
				nn.appendChild(cdata);
				//nn.text = element.nodeclass.vmlobj.taskStartMail;
				nodeele.appendChild(nn);
				
				// 改成Property形式
				var nn = pragramdoc.createNode(1,"property","");
				var r = pragramdoc.createAttribute("key"); 
				r.value = "taskEndMail";
				//添加属性 
				nn.setAttributeNode(r);	
				var cdata = pragramdoc.createCDATASection(element.nodeclass.vmlobj.taskEndMail);
				nn.appendChild(cdata);
				//nn.text = element.nodeclass.vmlobj.taskEndMail;
				nodeele.appendChild(nn);				

				// 改成Property形式
				var nn = pragramdoc.createNode(1,"property","");
				var r = pragramdoc.createAttribute("key"); 
				r.value = "taskStartMes";
				//添加属性 
				nn.setAttributeNode(r);	
				var cdata = pragramdoc.createCDATASection(element.nodeclass.vmlobj.taskStartMes);
				nn.appendChild(cdata);
				//nn.text = element.nodeclass.vmlobj.taskStartMes;
				nodeele.appendChild(nn);

				// 改成Property形式
				var nn = pragramdoc.createNode(1,"property","");
				var r = pragramdoc.createAttribute("key"); 
				r.value = "taskEndMes";
				//添加属性 
				nn.setAttributeNode(r);	
				var cdata = pragramdoc.createCDATASection(element.nodeclass.vmlobj.taskEndMes);
				nn.appendChild(cdata);
				//nn.text = element.nodeclass.vmlobj.taskEndMes;
				nodeele.appendChild(nn);

				// 改成Property形式
				var nn = pragramdoc.createNode(1,"property","");
				var r = pragramdoc.createAttribute("key"); 
				r.value = "preTranType";
				//添加属性 
				nn.setAttributeNode(r);	
				var cdata = pragramdoc.createCDATASection(element.nodeclass.vmlobj.preTranType);
				nn.appendChild(cdata);
				//nn.text = element.nodeclass.vmlobj.preTranType;
				nodeele.appendChild(nn);
			
				// 改成Property形式
				var nn = pragramdoc.createNode(1,"property","");
				var r = pragramdoc.createAttribute("key"); 
				r.value = "nextTranType";
				//添加属性 
				nn.setAttributeNode(r);	
				var cdata = pragramdoc.createCDATASection(element.nodeclass.vmlobj.nextTranType);
				nn.appendChild(cdata);
				//nn.text = element.nodeclass.vmlobj.nextTranType;
				nodeele.appendChild(nn);

				// 改成Property形式
				var nn = pragramdoc.createNode(1,"property","");
				var r = pragramdoc.createAttribute("key"); 
				r.value = "isActiveactor";
				//添加属性 
				nn.setAttributeNode(r);	
				var cdata = pragramdoc.createCDATASection(element.nodeclass.vmlobj.isActiveactor);
				nn.appendChild(cdata);
				//nn.text = element.nodeclass.vmlobj.isActiveactor;
				nodeele.appendChild(nn);

				// 改成Property形式
				var nn = pragramdoc.createNode(1,"property","");
				var r = pragramdoc.createAttribute("key"); 
				r.value = "counterSignType";
				//添加属性 
				nn.setAttributeNode(r);	
				var cdata = pragramdoc.createCDATASection(element.nodeclass.vmlobj.counterSignType);
				nn.appendChild(cdata);
				//nn.text = element.nodeclass.vmlobj.counterSignType;
				nodeele.appendChild(nn);

				// 改成Property形式
				var nn = pragramdoc.createNode(1,"property","");
				var r = pragramdoc.createAttribute("key"); 
				r.value = "canSelectOther";
				//添加属性 
				nn.setAttributeNode(r);	
				var cdata = pragramdoc.createCDATASection(element.nodeclass.vmlobj.canSelectOther);
				nn.appendChild(cdata);
				//nn.text = element.nodeclass.vmlobj.canSelectOther;
				nodeele.appendChild(nn);

				// 改成Property形式
				var nn = pragramdoc.createNode(1,"property","");
				var r = pragramdoc.createAttribute("key"); 
				r.value = "maxActors";
				//添加属性 
				nn.setAttributeNode(r);	
				var cdata = pragramdoc.createCDATASection(element.nodeclass.vmlobj.maxActors);
				nn.appendChild(cdata);
				//nn.text = element.nodeclass.vmlobj.maxActors;
				nodeele.appendChild(nn);

				// 改成Property形式
				var nn = pragramdoc.createNode(1,"property","");
				var r = pragramdoc.createAttribute("key"); 
				r.value = "reAssign";
				//添加属性 
				nn.setAttributeNode(r);	
				var cdata = pragramdoc.createCDATASection(element.nodeclass.vmlobj.reAssign);
				nn.appendChild(cdata);
				//nn.text = element.nodeclass.vmlobj.reAssign;
				nodeele.appendChild(nn);
				
				// 改成Property形式
				var nn = pragramdoc.createNode(1,"property","");
				var r = pragramdoc.createAttribute("key"); 
				r.value = "reAssignmore";
				//添加属性 
				nn.setAttributeNode(r);	
				var cdata = pragramdoc.createCDATASection(element.nodeclass.vmlobj.reAssignmore);
				nn.appendChild(cdata);
				//nn.text = element.nodeclass.vmlobj.reAssignmore;
				nodeele.appendChild(nn);				

				// 改成Property形式
				var nn = pragramdoc.createNode(1,"property","");
				var r = pragramdoc.createAttribute("key"); 
				r.value = "reassignActor";
				//添加属性 
				nn.setAttributeNode(r);	
				var cdata = pragramdoc.createCDATASection(element.nodeclass.vmlobj.reassignActor);
				nn.appendChild(cdata);
				//nn.text = element.nodeclass.vmlobj.reassignActor;
				nodeele.appendChild(nn);

				// 增加任务定时器设置信息
				// 改成Property形式
				var nn = pragramdoc.createNode(1,"property","");
				var r = pragramdoc.createAttribute("key"); 
				r.value = "doc_isTimer";
				//添加属性 
				nn.setAttributeNode(r);	
				var cdata = pragramdoc.createCDATASection(element.nodeclass.vmlobj.doc_isTimer);
				nn.appendChild(cdata);
				//nn.text = element.nodeclass.vmlobj.doc_isTimer;
				nodeele.appendChild(nn);

				// 增加任务定时器设置信息
				// 改成Property形式
				var nn = pragramdoc.createNode(1,"property","");
				var r = pragramdoc.createAttribute("key"); 
				r.value = "doc_timerSet";
				//添加属性 
				nn.setAttributeNode(r);	
				var cdata = pragramdoc.createCDATASection(element.nodeclass.vmlobj.doc_timerSet);
				nn.appendChild(cdata);
				//nn.text = element.nodeclass.vmlobj.doc_timerSet;
				nodeele.appendChild(nn);

				// 增加会签修改文档属性
				// 改成Property形式
				var nn = pragramdoc.createNode(1,"property","");
				var r = pragramdoc.createAttribute("key"); 
				r.value = "isDocEdit";
				//添加属性 
				nn.setAttributeNode(r);	
				var cdata = pragramdoc.createCDATASection(element.nodeclass.vmlobj.isDocEdit);
				nn.appendChild(cdata);
				//nn.text = element.nodeclass.vmlobj.isDocEdit;
				nodeele.appendChild(nn);
				
				// 改成Property形式
				var nn = pragramdoc.createNode(1,"property","");
				var r = pragramdoc.createAttribute("key"); 
				r.value = "wordPrivil";
				//添加属性 
				nn.setAttributeNode(r);	
				var cdata = pragramdoc.createCDATASection(element.nodeclass.vmlobj.wordPrivil);
				nn.appendChild(cdata);
				//nn.text = element.nodeclass.vmlobj.wordPrivil;
				nodeele.appendChild(nn);				
			}else if(element.ctlType==CNST_CTLTYPE_CONDITION){
				// 改成Property形式
				var nn = pragramdoc.createNode(1,"property","");
				var r = pragramdoc.createAttribute("key"); 
				r.value = "logicSet";
				//添加属性 
				nn.setAttributeNode(r);	
				var cdata = pragramdoc.createCDATASection(element.nodeclass.vmlobj.logicSet);
				nn.appendChild(cdata);
				//nn.text = element.nodeclass.vmlobj.logicSet;
				nodeele.appendChild(nn);	
				
				
				//add by caidw
				// 增加条件节点条件处理类名称设置
				var nn = pragramdoc.createNode(1,"property","");
				var r = pragramdoc.createAttribute("key"); 
				r.value = "decideHandleName";
				//添加属性 
				nn.setAttributeNode(r);	
				var cdata = pragramdoc.createCDATASection(element.nodeclass.vmlobj.decideHandleName);
				nn.appendChild(cdata);
				nodeele.appendChild(nn);
				
				// 增加条件节点条件处理类路径设置
				var nn = pragramdoc.createNode(1,"property","");
				var r = pragramdoc.createAttribute("key"); 
				r.value = "decideHandleClass";
				//添加属性 
				nn.setAttributeNode(r);	
				var cdata = pragramdoc.createCDATASection(element.nodeclass.vmlobj.decideHandleClass);
				nn.appendChild(cdata);
				nodeele.appendChild(nn);
				
				
			}else if(element.ctlType==CNST_CTLTYPE_PROSTATE){
				// 添加前驱节点集合
				// 改成Property形式
				var nn = pragramdoc.createNode(1,"property","");
				var r = pragramdoc.createAttribute("key"); 
				r.value = "preNodes";
				//添加属性 
				nn.setAttributeNode(r);	
				if(preNode[element.id]){
					var cdata = pragramdoc.createCDATASection(preNode[element.id].substring(0, preNode[element.id].length-1));
					nn.appendChild(cdata);
				}
				//nn.text = preNode[element.id].substring(0, preNode[element.id].length-1); 
				nodeele.appendChild(nn);				
				
				// 改成Property形式
				var nn = pragramdoc.createNode(1,"property","");
				var r = pragramdoc.createAttribute("key"); 
				r.value = "subProcessType";
				//添加属性 
				nn.setAttributeNode(r);	
				var cdata = pragramdoc.createCDATASection(element.nodeclass.vmlobj.subProcessType);
				nn.appendChild(cdata);
				//nn.text = element.nodeclass.vmlobj.subProcessType; 
				nodeele.appendChild(nn);

				// 改成Property形式
				var nn = pragramdoc.createNode(1,"property","");
				var r = pragramdoc.createAttribute("key"); 
				r.value = "subProcessName";
				//添加属性 
				nn.setAttributeNode(r);	
				var cdata = pragramdoc.createCDATASection(element.nodeclass.vmlobj.subProcessName);
				nn.appendChild(cdata);
				//nn.text = "<![CDATA[" + element.nodeclass.vmlobj.subProcessName + "]]>"; 
				nodeele.appendChild(nn);

				// 改成Property形式
				var nn = pragramdoc.createNode(1,"property","");
				var r = pragramdoc.createAttribute("key"); 
				r.value = "subProcessForm";
				//添加属性 
				nn.setAttributeNode(r);	
				var cdata = pragramdoc.createCDATASection(element.nodeclass.vmlobj.subProcessForm);
				nn.appendChild(cdata);
				//nn.text = element.nodeclass.vmlobj.subProcessForm; 
				nodeele.appendChild(nn);

				// 改成Property形式
				var nn = pragramdoc.createNode(1,"property","");
				var r = pragramdoc.createAttribute("key"); 
				r.value = "synchronize";
				//添加属性 
				nn.setAttributeNode(r);	
				var cdata = pragramdoc.createCDATASection(element.nodeclass.vmlobj.synchronize);
				nn.appendChild(cdata);
				//nn.text = element.nodeclass.vmlobj.synchronize; 
				nodeele.appendChild(nn);

				// 改成Property形式
				var nn = pragramdoc.createNode(1,"property","");
				var r = pragramdoc.createAttribute("key"); 
				r.value = "fromParent";
				//添加属性 
				nn.setAttributeNode(r);	
				var cdata = pragramdoc.createCDATASection(element.nodeclass.vmlobj.fromParent);
				nn.appendChild(cdata);
				//nn.text = element.nodeclass.vmlobj.fromParent; 
				nodeele.appendChild(nn);

				// 改成Property形式
				var nn = pragramdoc.createNode(1,"property","");
				var r = pragramdoc.createAttribute("key"); 
				r.value = "toParent";
				//添加属性 
				nn.setAttributeNode(r);	
				var cdata = pragramdoc.createCDATASection(element.nodeclass.vmlobj.toParent);
				nn.appendChild(cdata);
				//nn.text = element.nodeclass.vmlobj.toParent; 
				nodeele.appendChild(nn);																
			}else if(element.ctlType==CNST_CTLTYPE_SUPERSTATE){
				// 添加前驱节点集合
				// 改成Property形式
				var nn = pragramdoc.createNode(1,"property","");
				var r = pragramdoc.createAttribute("key"); 
				r.value = "preNodes";
				//添加属性 
				nn.setAttributeNode(r);	
				if(preNode[element.id]){
					var cdata = pragramdoc.createCDATASection(preNode[element.id].substring(0, preNode[element.id].length-1));
					nn.appendChild(cdata);
				}
				//nn.text = preNode[element.id].substring(0, preNode[element.id].length-1); 
				nodeele.appendChild(nn);				
		
				// 改成Property形式
				var nn = pragramdoc.createNode(1,"property","");
				var r = pragramdoc.createAttribute("key"); 
				r.value = "subProcessType";
				//添加属性 
				nn.setAttributeNode(r);	
				var cdata = pragramdoc.createCDATASection(element.nodeclass.vmlobj.subProcessType);
				nn.appendChild(cdata);
				//nn.text = element.nodeclass.vmlobj.subProcessType; 
				nodeele.appendChild(nn);
				
				// 改成Property形式
				var nn = pragramdoc.createNode(1,"property","");
				var r = pragramdoc.createAttribute("key"); 
				r.value = "taskName";
				//添加属性 
				nn.setAttributeNode(r);	
				var cdata = pragramdoc.createCDATASection(element.nodeclass.vmlobj.taskName);
				nn.appendChild(cdata);
				//nn.text = element.nodeclass.vmlobj.taskName; 
				nodeele.appendChild(nn);						

				// 改成Property形式
				var nn = pragramdoc.createNode(1,"property","");
				var r = pragramdoc.createAttribute("key"); 
				r.value = "taskStart";
				//添加属性 
				nn.setAttributeNode(r);	
				var cdata = pragramdoc.createCDATASection(element.nodeclass.vmlobj.taskStart);
				nn.appendChild(cdata);
				//nn.text = element.nodeclass.vmlobj.taskStart; 
				nodeele.appendChild(nn);

				// 改成Property形式
				var nn = pragramdoc.createNode(1,"property","");
				var r = pragramdoc.createAttribute("key"); 
				r.value = "taskEnd";
				//添加属性 
				nn.setAttributeNode(r);	
				var cdata = pragramdoc.createCDATASection(element.nodeclass.vmlobj.taskEnd);
				nn.appendChild(cdata);
				//nn.text = element.nodeclass.vmlobj.taskEnd; 
				nodeele.appendChild(nn);
				
				// 改成Property形式
				var nn = pragramdoc.createNode(1,"property","");
				var r = pragramdoc.createAttribute("key"); 
				r.value = "taskStartMail";
				//添加属性 
				nn.setAttributeNode(r);	
				var cdata = pragramdoc.createCDATASection(element.nodeclass.vmlobj.taskStartMail);
				nn.appendChild(cdata);
				//nn.text = element.nodeclass.vmlobj.taskStartMail; 
				nodeele.appendChild(nn);				

				// 改成Property形式
				var nn = pragramdoc.createNode(1,"property","");
				var r = pragramdoc.createAttribute("key"); 
				r.value = "taskEndMail";
				//添加属性 
				nn.setAttributeNode(r);	
				var cdata = pragramdoc.createCDATASection(element.nodeclass.vmlobj.taskEndMail);
				nn.appendChild(cdata);
				//nn.text = element.nodeclass.vmlobj.taskEndMail; 
				nodeele.appendChild(nn);

				// 改成Property形式
				var nn = pragramdoc.createNode(1,"property","");
				var r = pragramdoc.createAttribute("key"); 
				r.value = "realActors";
				//添加属性 
				nn.setAttributeNode(r);	
				var cdata = pragramdoc.createCDATASection(element.nodeclass.vmlobj.realActors);
				nn.appendChild(cdata);
				//nn.text = element.nodeclass.vmlobj.realActors; 
				nodeele.appendChild(nn);
				
				// 改成Property形式
				var nn = pragramdoc.createNode(1,"property","");
				var r = pragramdoc.createAttribute("key"); 
				r.value = "handleActor";
				//添加属性 
				nn.setAttributeNode(r);	
				var cdata = pragramdoc.createCDATASection(element.nodeclass.vmlobj.handleActor);
				nn.appendChild(cdata);
				//nn.text = element.nodeclass.vmlobj.handleActor; 
				nodeele.appendChild(nn);
				
				// 改成Property形式
				var nn = pragramdoc.createNode(1,"property","");
				var r = pragramdoc.createAttribute("key"); 
				r.value = "taskStartMes";
				//添加属性 
				nn.setAttributeNode(r);	
				var cdata = pragramdoc.createCDATASection(element.nodeclass.vmlobj.taskStartMes);
				nn.appendChild(cdata);
				//nn.text = element.nodeclass.vmlobj.taskStartMes; 
				nodeele.appendChild(nn);
				
				// 改成Property形式
				var nn = pragramdoc.createNode(1,"property","");
				var r = pragramdoc.createAttribute("key"); 
				r.value = "taskEndMes";
				//添加属性 
				nn.setAttributeNode(r);	
				var cdata = pragramdoc.createCDATASection(element.nodeclass.vmlobj.taskEndMes);
				nn.appendChild(cdata);
				//nn.text = element.nodeclass.vmlobj.taskEndMes; 
				nodeele.appendChild(nn);

				// 改成Property形式
				var nn = pragramdoc.createNode(1,"property","");
				var r = pragramdoc.createAttribute("key"); 
				r.value = "isActiveactor";
				//添加属性 
				nn.setAttributeNode(r);	
				var cdata = pragramdoc.createCDATASection(element.nodeclass.vmlobj.isActiveactor);
				nn.appendChild(cdata);
				//nn.text = element.nodeclass.vmlobj.isActiveactor; 
				nodeele.appendChild(nn);

				// 改成Property形式
				var nn = pragramdoc.createNode(1,"property","");
				var r = pragramdoc.createAttribute("key"); 
				r.value = "synchronize";
				//添加属性 
				nn.setAttributeNode(r);	
				var cdata = pragramdoc.createCDATASection(element.nodeclass.vmlobj.synchronize);
				nn.appendChild(cdata);
				//nn.text = element.nodeclass.vmlobj.synchronize; 
				nodeele.appendChild(nn);			
				
				// 改成Property形式
				var nn = pragramdoc.createNode(1,"property","");
				var r = pragramdoc.createAttribute("key"); 
				r.value = "canSelectOther";
				//添加属性 
				nn.setAttributeNode(r);	
				var cdata = pragramdoc.createCDATASection(element.nodeclass.vmlobj.canSelectOther);
				nn.appendChild(cdata);
				//nn.text = element.nodeclass.vmlobj.canSelectOther; 
				nodeele.appendChild(nn);

				// 改成Property形式
				var nn = pragramdoc.createNode(1,"property","");
				var r = pragramdoc.createAttribute("key"); 
				r.value = "maxActors";
				//添加属性 
				nn.setAttributeNode(r);	
				var cdata = pragramdoc.createCDATASection(element.nodeclass.vmlobj.maxActors);
				nn.appendChild(cdata);
				//nn.text = element.nodeclass.vmlobj.maxActors; 
				nodeele.appendChild(nn);

				// 改成Property形式
				var nn = pragramdoc.createNode(1,"property","");
				var r = pragramdoc.createAttribute("key"); 
				r.value = "fromParent";
				//添加属性 
				nn.setAttributeNode(r);	
				var cdata = pragramdoc.createCDATASection(element.nodeclass.vmlobj.fromParent);
				nn.appendChild(cdata);
				//nn.text = element.nodeclass.vmlobj.fromParent; 
				nodeele.appendChild(nn);

				// 改成Property形式
				var nn = pragramdoc.createNode(1,"property","");
				var r = pragramdoc.createAttribute("key"); 
				r.value = "toParent";
				//添加属性 
				nn.setAttributeNode(r);	
				var cdata = pragramdoc.createCDATASection(element.nodeclass.vmlobj.toParent);
				nn.appendChild(cdata);
				//nn.text = element.nodeclass.vmlobj.toParent; 
				nodeele.appendChild(nn);

				// 增加任务定时器设置信息
				// 改成Property形式
				var nn = pragramdoc.createNode(1,"property","");
				var r = pragramdoc.createAttribute("key"); 
				r.value = "doc_isTimer";
				//添加属性 
				nn.setAttributeNode(r);	
				var cdata = pragramdoc.createCDATASection(element.nodeclass.vmlobj.doc_isTimer);
				nn.appendChild(cdata);
				//nn.text = element.nodeclass.vmlobj.doc_isTimer; 
				nodeele.appendChild(nn);				
				
				// 增加任务定时器设置信息
				// 改成Property形式
				var nn = pragramdoc.createNode(1,"property","");
				var r = pragramdoc.createAttribute("key"); 
				r.value = "doc_timerSet";
				//添加属性 
				nn.setAttributeNode(r);	
				var cdata = pragramdoc.createCDATASection(element.nodeclass.vmlobj.doc_timerSet);
				nn.appendChild(cdata);
				//nn.text = element.nodeclass.vmlobj.doc_timerSet; 
				nodeele.appendChild(nn);																							
			}else if(element.ctlType==CNST_CTLTYPE_NODE){
				// 改成Property形式
				var nn = pragramdoc.createNode(1,"property","");
				var r = pragramdoc.createAttribute("key"); 
				r.value = "preTranType";
				//添加属性 
				nn.setAttributeNode(r);	
				var cdata = pragramdoc.createCDATASection(element.nodeclass.vmlobj.preTranType);
				nn.appendChild(cdata);
				//nn.text = element.nodeclass.vmlobj.preTranType;
				nodeele.appendChild(nn);
				
				// 改成Property形式
				var nn = pragramdoc.createNode(1,"property","");
				var r = pragramdoc.createAttribute("key"); 
				r.value = "nodeDelegation";
				//添加属性 
				nn.setAttributeNode(r);	
				var cdata = pragramdoc.createCDATASection(element.nodeclass.vmlobj.nodeDelegation);
				nn.appendChild(cdata);
				//nn.text = element.nodeclass.vmlobj.nodeDelegation; 
				nodeele.appendChild(nn);
				
				//zw
				// 改成Property形式
				var nn = pragramdoc.createNode(1,"property","");
				var r = pragramdoc.createAttribute("key"); 
				r.value = "eaNodeDelegateName";
				//添加属性 
				nn.setAttributeNode(r);	
				var cdata = pragramdoc.createCDATASection(element.nodeclass.vmlobj.eaNodeDelegateName);
				nn.appendChild(cdata);
				nodeele.appendChild(nn);
			}		
			noderoot.appendChild(nodeele);
		}
	}  
	
	
      var str="<docroot><workflow>"+doc.xml+"</workflow>"+pragramdoc.xml+"</docroot>";
      
	  pragramdoc=null;
	  noderoot=null;
	  lineroot=null;
	  CollectGarbage();
	  return str;   
}

/**
 * 该方法主要是在保存的时候去除重复的迁移线和错误的迁移线，并未真正解决产生错误迁移线的问题<br>
 * 经过和曹钦确认，先注释该方法，待测试人员测试发现相同问题后找到产生问题的真正原因并解决。
 */
/**

function correctModel(str){
    var docroot = createXmlDom();
	docroot.loadXML(str);
	
	clearRepetitionLine(docroot);
	
	var nodes = docroot.getElementsByTagName("nodeelement");
	var lines = docroot.getElementsByTagName("lineelement");
	
	var nodeIds = {};
	var lineIds = {};
	
	for(var i=0,iLen=nodes.length;i<iLen;i++){
		var id = nodes[i].getAttribute("thisIDName");
		var title = nodes[i].getAttribute("thisCaption");
		nodeIds[id] = title;
	}
	
	for(var j=0,jLen=lines.length;j<jLen;j++){
		var id = lines[j].getAttribute("thisIDName");
		var title = lines[j].getAttribute("thisCaption");
		lineIds[id] = title;
	}
	
	
	var transitions = docroot.getElementsByTagName("transition");
	for(var t=0,tLen=transitions.length;t<tLen;t++){
		var id = transitions[t].getAttribute("atnode");
		if(lineIds[id] == null || lineIds[id] == undefined){
			var tran = transitions[t];
			tran.parentNode.removeChild(tran);
		}
	}
	return docroot.xml;
}
**/








