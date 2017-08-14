var n=new Array();
var l=new Array();
var lto=new Array();
var delelement=new Array();

//重画画布
function repaintElements(wfxmlStr,isLoadXml){

	doc.loadXML(wfxmlStr);
	
	//zw
	//clearRepetitionLine(doc);
	
	root=doc.getElementsByTagName("process-definition")[0];
	if(isLoadXml==1){
		clearAllNodes(isLoadXml);
		compareChangeLoad();
	}else{
		compareChangeEdit();
		clearAllNodes(isLoadXml);
	}
	//流程名称设置，当导入流程名称与当前流程名称不一致时使用
	root.getAttributeNode("name").value = processName;
	
	for(var i=0;i<root.childNodes.length;i++){
		if(root.childNodes[i].getAttributeNode("atnode")!=null){
			var tempid=root.childNodes[i].getAttributeNode("atnode").value;
			tempid=parseInt(tempid.substring(1,tempid.length));
			if(maxfCtlNum<tempid)
				maxfCtlNum=tempid;
		}
	}
	var lines = doc.getElementsByTagName("transition");
	for(var i=0;i<lines.length;i++){
		if(lines[i].getAttributeNode("atnode")!=null){
			var tempid=lines[i].getAttributeNode("atnode").value;
			tempid=parseInt(tempid.substring(1,tempid.length));
			if(maxfCtlNum<tempid)
				maxfCtlNum=tempid;
		}
	}
	maxfCtlNum++;
	for(var i=0;i<root.childNodes.length;i++){
		if(isLoadXml==1){
			repaintElementLoad(root.childNodes[i],"node",i);
		}else{
			repaintElementEdit(root.childNodes[i],"node",i);
		}
	}
	var templines=root.getElementsByTagName("transition");
	for(var i=0;i<templines.length;i++){
		if(isLoadXml==1){
			repaintElementLoad(templines[i],"line",i);
		}else{
			repaintElementEdit(templines[i],"line",i);
		}
	}
	//恢复计数值
	fCtlNum=maxfCtlNum;
	iptCtlNum.value=fCtlNum; //控件中计数同步

	//最大值清空
	maxfCtlNum=-1;

	//数组清空
	n=new Array();
	l=new Array();
	lto=new Array();
    delelement=new Array();
	propertyListSubject.notifyObservers();    	
}


//清空画布
function clearAllNodes(isLoadXml){

	if(isLoadXml==1){
	//从控件列表删除
		for (var i=0;i<sltObjArray.options.length;) {
			var objid=sltObjArray.options[i].value
			sltObjArray.options[i] = null; //删除
			var obj=document.getElementById(objid);
			//obj.parentNode.removeChild(obj);
			if(obj.ctlType==CNST_CTLTYPE_LINE){
				obj.nodeclass.movepoint1.removeNode(true);
				obj.nodeclass.movepoint2.removeNode(true);
			}
			obj.nodeclass.textobj.removeNode(true);
			obj=obj.removeNode(true);
			obj=null;   
			CollectGarbage(); 
		}

	}else{
		for(var i=0;i<delelement.length;i++){
			var node=document.getElementById(delelement[i]);
			if(node!=null){
				if(node.ctlType!=CNST_CTLTYPE_LINE){
					changedis2(node);
				}else{
					changedisline2(node);
				}
			}
		}
	}
}



	function changedis2(aobj) {
		var lineinfo=aobj.line;
		var aLines = lineinfo.split(";");
		for(var i=0;i<aLines.length-1;i++){
			var lineObjStr = (aLines[i].split("TO")[0]).split(":")[0];
			changedisline2(eval(lineObjStr));
		}
		removeFromObjArray(aobj);
		aobj.removeNode(true);
		aobj=null;   
		CollectGarbage(); 
	}
	function changedisline2(aobj){
		deleteLine(aobj);
		removeFromObjArray(aobj);
		aobj.nodeclass.movepoint1.removeNode(true);
		aobj.nodeclass.movepoint2.removeNode(true);
		aobj.nodeclass.textobj.removeNode(true);
		aobj.removeNode(true);
		aobj=null;   
		CollectGarbage(); 
	}


//比较变化
function compareChangeEdit(){
	var k=0;
	var elementList=sltObjArray.options;
	var nodeChilds=root.childNodes;
	for(var i=0;i<elementList.length;i++){
		var node=eval(elementList[i].value);
		if(node.ctlType!=CNST_CTLTYPE_LINE){
			for(var j=0;j<nodeChilds.length;j++){
				if(nodeChilds[j].getAttributeNode("atnode")!=null && node.id==nodeChilds[j].getAttributeNode("atnode").value){
					n[node.id]=true;
					//保持名称一致
					if(nodeChilds[j].getAttributeNode("name")!=null){
						node.nodeclass.thisCaption=nodeChilds[j].getAttributeNode("name").value;
						node.nodeclass.textobj.innerText=node.nodeclass.thisCaption;
					}else{
						node.nodeclass.thisCaption="";
						node.nodeclass.textobj.innerText="";
					}
					k=1;
					break;
				}
			}
			if(k==0){
				delelement[delelement.length]=node.id;
			}
			k=0;
		}
	}
	k=0;
	locateToNode();
	var transitionChilds=doc.getElementsByTagName("transition");
	for(var i=0;i<elementList.length;i++){
		var line=eval(elementList[i].value);
		if(line.ctlType==CNST_CTLTYPE_LINE){
			for(var j=0;j<transitionChilds.length;j++){
				var tonodeid=root.childNodes[lto[j]].getAttributeNode("atnode");
				if(transitionChilds[j].getAttributeNode("atnode")!=null && tonodeid!=null && transitionChilds[j].parentNode.getAttributeNode("atnode")!=null && line.id==transitionChilds[j].getAttributeNode("atnode").value && line.fromelement.id==transitionChilds[j].parentNode.getAttributeNode("atnode").value && line.toelement.id==tonodeid.value){
					l[line.id]=true;
					//保持名称一致
					if(transitionChilds[j].getAttributeNode("name")!=null){
						line.nodeclass.thisCaption=transitionChilds[j].getAttributeNode("name").value;
						line.nodeclass.textobj.innerText=line.nodeclass.thisCaption;
					}else{
						var r=doc.createAttribute("name");
						r.value="";
						transitionChilds[j].setAttributeNode(r);
						line.nodeclass.thisCaption="";
						line.nodeclass.textobj.innerText="";
					}
					k=1;
					break;
				}
			}
			if(k==0){
				delelement[delelement.length]=line.id;
			}
			k=0;
		}
	}
}


//比较变化
function compareChangeLoad(){
	var k=0;
	var nodeChilds = noderoot.childNodes;
	var elementChilds = root.childNodes;
	for(var i=0;i<nodeChilds.length;i++){
		for(var j=0;j<elementChilds.length;j++){
			if(elementChilds[j].getAttributeNode("atnode")!=null && nodeChilds[i].getAttributeNode("thisIDName").value==elementChilds[j].getAttributeNode("atnode").value){
				n[nodeChilds[i].getAttributeNode("thisIDName").value]=i;
				//保持名称一致
				if(elementChilds[j].getAttributeNode("name")!=null){
					nodeChilds[i].getAttributeNode("thisCaption").value=elementChilds[j].getAttributeNode("name").value;
				}else{
					nodeChilds[i].getAttributeNode("thisCaption").value="";
				}
				k=1;
				break;
			}
		}
		if(k==0){
			for(var index in n){
				if(parseInt(n[index])>i)
					n[index]=n[index]-1;
			}
			nodeChilds[i].parentNode.removeChild(nodeChilds[i]);
			i--;
		}
		k=0;
	}
	k=0;
	locateToNode();
	var lineChilds=lineroot.childNodes;
	var transitionChilds=doc.getElementsByTagName("transition");
	for(var i=0;i<lineChilds.length;i++){
		for(var j=0;j<transitionChilds.length;j++){
			var tonodeid=root.childNodes[lto[j]].getAttributeNode("atnode");
			if(transitionChilds[j].getAttributeNode("atnode")!=null && tonodeid!=null && transitionChilds[j].parentNode.getAttributeNode("atnode")!=null && lineChilds[i].getAttributeNode("thisIDName").value==transitionChilds[j].getAttributeNode("atnode").value && lineChilds[i].getAttributeNode("fromelement").value==transitionChilds[j].parentNode.getAttributeNode("atnode").value && lineChilds[i].getAttributeNode("toelement").value==tonodeid.value){
				l[transitionChilds[j].getAttributeNode("atnode").value]=i;
				//保持名称一致
				if(transitionChilds[j].getAttributeNode("name")!=null){
					lineChilds[i].getAttributeNode("thisCaption").value=transitionChilds[j].getAttributeNode("name").value;
				}else{
					var r=doc.createAttribute("name");
					r.value="";
					transitionChilds[j].setAttributeNode(r);
					lineChilds[i].getAttributeNode("thisCaption").value="";
				}
				k=1;
				break;
			}
		}
		if(k==0){
			for(var index in l){
				if(parseInt(l[index])>i)
					l[index]=l[index]-1;
			}
			lineChilds[i].parentNode.removeChild(lineChilds[i]);
			i--;
		}
		k=0;
	}
}

function locateToNode(){
	var transitionChilds=doc.getElementsByTagName("transition");
	for(var j=0;j<transitionChilds.length;j++){
		var temparr=findToElement(transitionChilds[j].getAttributeNode("to").value);
		lto[j]=parseInt(temparr);
	}
}


//重载元素
function repaintElementLoad(elementnode,flag){
	if(flag=="node"){
		if(elementnode.getAttributeNode("atnode")!=null && n[elementnode.getAttributeNode("atnode").value]!=undefined){
			var programnode = noderoot.childNodes[n[elementnode.getAttributeNode("atnode").value]];
			//addToObjArray(programnode.getAttributeNode("thisIDName").value,false);
			var attributes = [];
			var plugins = [];
		    attributes['thisCaption'] = programnode.getAttributeNode("thisCaption").value;
		    attributes['leftpix'] = programnode.getAttributeNode("leftpix").value;
		    attributes['toppix'] = programnode.getAttributeNode("toppix").value;
		    var childAttributes = programnode.childNodes;
		    var childAttribute;
		    var subProcessType = null;
		    var childAttributeKey;//子属性key值
		    for(var k = 0; k < childAttributes.length; k++){
		   		childAttribute = childAttributes[k];
		   		childAttributeKey = childAttribute.getAttributeNode("key").value;//获取子属性key值
		   		if(childAttributeKey.indexOf("plugins_") == 0){//属于插件属性
		   			plugins[childAttributeKey] = childAttribute.text;
		   		}else{//属于非插件属性
		   			attributes[childAttributeKey] = childAttribute.text;
		   			if(childAttributeKey == "subProcessType"){
			   			subProcessType = childAttribute.text;
			   		}
		   		}
		    }
		    attributes['plugins'] = plugins;//设置插件属性
		    
			switch (elementnode.nodeName){
			   case "start-state":
			   	   //var attributes = new Array();
			   	   attributes['thisIDName'] = programnode.getAttributeNode("thisIDName").value;
				   //attributes['thisCaption'] = programnode.getAttributeNode("thisCaption").value;
				   //attributes['leftpix'] = programnode.getAttributeNode("leftpix").value;
				   //attributes['toppix'] = programnode.getAttributeNode("toppix").value;
				   //var childAttributes = programnode.childNodes;
				   //var childAttribute;
				   //for(var k = 0; k < childAttributes.length; k++){
				   //		childAttribute = childAttributes[k];
				   //		attributes[childAttribute.getAttributeNode("key").value] = childAttribute.text;
				   //}
				   var snode = new startnode(attributes);
				   fSelectedObj = snode.vmlobj;
				   break;
			   case "state":
			   	   //var attributes = new Array();
				   //attributes['thisCaption'] = programnode.getAttributeNode("thisCaption").value;
				   //attributes['leftpix'] = programnode.getAttributeNode("leftpix").value;
				   //attributes['toppix'] = programnode.getAttributeNode("toppix").value;
				   //var childAttributes = programnode.childNodes;
				   //var childAttribute;
				   //for(var k = 0; k < childAttributes.length; k++){
				   //		childAttribute = childAttributes[k];
				   //		attributes[childAttribute.getAttributeNode("key").value] = childAttribute.text;
				   //}			   
			       var stats=new statnode(programnode.getAttributeNode("thisIDName").value, attributes);
				   fSelectedObj=stats.vmlobj;
				   break;
			   case "task-node":
			   
			   	   //var attributes = [];
			   	   //var plugins = [];
				   //attributes['thisCaption'] = programnode.getAttributeNode("thisCaption").value;
				   //attributes['leftpix'] = programnode.getAttributeNode("leftpix").value;
				   //attributes['toppix'] = programnode.getAttributeNode("toppix").value;
				   //var childAttributes = programnode.childNodes;
				   //var childAttribute;
				   //var subProcessType = null;
				   //var childAttributeKey;//子属性key值
				   //for(var k = 0; k < childAttributes.length; k++){
				   //		childAttribute = childAttributes[k];
				   //		childAttributeKey = childAttribute.getAttributeNode("key").value;//获取子属性key值
				   //		if(childAttributeKey.indexOf("plugins_") == 0){//属于插件属性
				   //			plugins[childAttributeKey] = childAttribute.text;
				   //		}else{//属于非插件属性
				   //			attributes[childAttributeKey] = childAttribute.text;
				   //			if(childAttributeKey == "subProcessType"){
				   //			subProcessType = childAttribute.text;
				   //  		}
				   //		}
				   //}
				   //attributes['plugins'] = plugins;//设置插件属性
			   
				   if(subProcessType!=null && subProcessType=="1"){
			   	       /**var attributes = new Array();
				   	   attributes['thisCaption'] = programnode.getAttributeNode("thisCaption").value;
				   	   attributes['leftpix'] = programnode.getAttributeNode("leftpix").value;
				       attributes['toppix'] = programnode.getAttributeNode("toppix").value;
					   var childAttributes = programnode.childNodes;
					   var childAttribute;
					   for(var k = 0; k < childAttributes.length; k++){
					   		childAttribute = childAttributes[k];
					   		attributes[childAttribute.getAttributeNode("key").value] = childAttribute.text;
					   }**/			       					       					   	   
					   var tasks=new processstatenode(programnode.getAttributeNode("thisIDName").value, attributes);
					   fSelectedObj=tasks.vmlobj;			   	   
			   	   }else if(subProcessType!=null && subProcessType=="2"){
			   	   	   /**var attributes = new Array();
				   	   attributes['thisCaption'] = programnode.getAttributeNode("thisCaption").value;
				   	   attributes['leftpix'] = programnode.getAttributeNode("leftpix").value;
				       attributes['toppix'] = programnode.getAttributeNode("toppix").value;
					   var childAttributes = programnode.childNodes;
					   var childAttribute;
					   for(var k = 0; k < childAttributes.length; k++){
					   		childAttribute = childAttributes[k];
					   		attributes[childAttribute.getAttributeNode("key").value] = childAttribute.text;
					   }**/				       
					   var tasks=new superstatenode(programnode.getAttributeNode("thisIDName").value, attributes);
					   fSelectedObj=tasks.vmlobj;
			   	   }else{
			   	       /**var attributes = new Array();
				   	   attributes['thisCaption'] = programnode.getAttributeNode("thisCaption").value;
				   	   attributes['leftpix'] = programnode.getAttributeNode("leftpix").value;
				       attributes['toppix'] = programnode.getAttributeNode("toppix").value;
					   var childAttributes = programnode.childNodes;
					   var childAttribute;
					   for(var k = 0; k < childAttributes.length; k++){
					   		childAttribute = childAttributes[k];
					   		attributes[childAttribute.getAttributeNode("key").value] = childAttribute.text;
					   }**/					       				       				   	   
					   var tasks=new tasknode(programnode.getAttributeNode("thisIDName").value, attributes);
					   fSelectedObj=tasks.vmlobj;
				   }				   
				   break;
			   case "fork":
				   var splits=new splitnode(programnode.getAttributeNode("thisIDName").value,programnode.getAttributeNode("thisCaption").value,programnode.getAttributeNode("leftpix").value,programnode.getAttributeNode("toppix").value,programnode.getAttributeNode("nodeEnter").value,programnode.getAttributeNode("nodeLeave").value,programnode.getAttributeNode("nodeForm").value,programnode.getAttributeNode("formPriv").value,programnode.getAttributeNode("isSetAction").value,programnode.getAttributeNode("actionSet").value);
				   fSelectedObj=splits.vmlobj;
				   break;
			   case "join":
				   var joins=new joinnode(programnode.getAttributeNode("thisIDName").value,programnode.getAttributeNode("thisCaption").value,programnode.getAttributeNode("leftpix").value,programnode.getAttributeNode("toppix").value,programnode.getAttributeNode("nodeEnter").value,programnode.getAttributeNode("nodeLeave").value,programnode.getAttributeNode("nodeForm").value,programnode.getAttributeNode("formPriv").value,programnode.getAttributeNode("isSetAction").value,programnode.getAttributeNode("actionSet").value);
				   fSelectedObj=joins.vmlobj;
				   break;
			   case "decision":
			   	   //var attributes = new Array();
				   //attributes['thisCaption'] = programnode.getAttributeNode("thisCaption").value;
				   //attributes['leftpix'] = programnode.getAttributeNode("leftpix").value;
				   //attributes['toppix'] = programnode.getAttributeNode("toppix").value;
				   //var childAttributes = programnode.childNodes;
				   //var childAttribute;
				   //for(var k = 0; k < childAttributes.length; k++){
				   //		childAttribute = childAttributes[k];
				   //		attributes[childAttribute.getAttributeNode("key").value] = childAttribute.text;
				   //}
				   var conditions=new conditionnode(programnode.getAttributeNode("thisIDName").value, attributes);
				   fSelectedObj=conditions.vmlobj;
				   break;
			   case "end-state":
			   	   //var attributes = new Array();
				   //attributes['thisCaption'] = programnode.getAttributeNode("thisCaption").value;
				   //attributes['leftpix'] = programnode.getAttributeNode("leftpix").value;
				   //attributes['toppix'] = programnode.getAttributeNode("toppix").value;
				   //var childAttributes = programnode.childNodes;
				   //var childAttribute;
				   //for(var k = 0; k < childAttributes.length; k++){
				   //		childAttribute = childAttributes[k];
				   //		attributes[childAttribute.getAttributeNode("key").value] = childAttribute.text;
				   //}
				   var ends=new endnode(programnode.getAttributeNode("thisIDName").value, attributes);			   
				   fSelectedObj=ends.vmlobj;					
				   break;
			   case "node":
			   	   //var attributes = new Array();
				   //attributes['thisCaption'] = programnode.getAttributeNode("thisCaption").value;
				   //attributes['leftpix'] = programnode.getAttributeNode("leftpix").value;
				   //attributes['toppix'] = programnode.getAttributeNode("toppix").value;
				   //var childAttributes = programnode.childNodes;
				   //var childAttribute;
				   //for(var k = 0; k < childAttributes.length; k++){
				   //		childAttribute = childAttributes[k];
				   //		attributes[childAttribute.getAttributeNode("key").value] = childAttribute.text;
				   //}			   
				   var ends=new nodenode(programnode.getAttributeNode("thisIDName").value, attributes);			   
				   fSelectedObj=ends.vmlobj;					
				   break;
			   case "super-state":
				   var ends=new superstatenode(programnode.getAttributeNode("thisIDName").value,programnode.getAttributeNode("thisCaption").value,programnode.getAttributeNode("leftpix").value,programnode.getAttributeNode("toppix").value,programnode.getAttributeNode("nodeEnter").value,programnode.getAttributeNode("nodeLeave").value,programnode.getAttributeNode("nodeForm").value,programnode.getAttributeNode("formPriv").value,programnode.getAttributeNode("isSetAction").value,programnode.getAttributeNode("actionSet").value);
				   fSelectedObj=ends.vmlobj;					
				   break;
			   case "process-state":
				   var ends=new processstatenode(programnode.getAttributeNode("thisIDName").value,programnode.getAttributeNode("thisCaption").value,programnode.getAttributeNode("leftpix").value,programnode.getAttributeNode("toppix").value,programnode.getAttributeNode("isSetAction").value,programnode.getAttributeNode("actionSet").value);
				   fSelectedObj=ends.vmlobj;					
				   break;				   				   				   
			   default:
				   return;
			}			
		}else{
			var thisIDName=elementnode.getAttributeNode("atnode")!=null?elementnode.getAttributeNode("atnode").value:"w"+maxfCtlNum;
			//addToObjArray(thisIDName,false);
			if(elementnode.getAttributeNode("atnode")==null){
				var r=doc.createAttribute("atnode");
				r.value=thisIDName;
				elementnode.setAttributeNode(r);
			}
			switch (elementnode.nodeName){
			   case "start-state":
				   var thisCaption=elementnode.getAttributeNode("name")!=null?elementnode.getAttributeNode("name").value:"开始";
				   var snode=new startnode(thisIDName,thisCaption,0,0);
				   fSelectedObj=snode.vmlobj;
				   break;
			   case "state":
				   var thisCaption=elementnode.getAttributeNode("name")!=null?elementnode.getAttributeNode("name").value:"状态"+maxfCtlNum;
				   var stats=new statnode(thisIDName,thisCaption,0,0);
				   fSelectedObj=stats.vmlobj;
				   break;
			   case "task-node":
				   var thisCaption=elementnode.getAttributeNode("name")!=null?elementnode.getAttributeNode("name").value:"任务"+maxfCtlNum;
				   var tasks=new tasknode(thisIDName,thisCaption,0,0);
				   fSelectedObj=tasks.vmlobj;
				   break;
			   case "fork":
				   var thisCaption=elementnode.getAttributeNode("name")!=null?elementnode.getAttributeNode("name").value:"分支"+maxfCtlNum;
				   var splits=new splitnode(thisIDName,thisCaption,0,0);
				   fSelectedObj=splits.vmlobj;
				   break;
			   case "join":
				   var thisCaption=elementnode.getAttributeNode("name")!=null?elementnode.getAttributeNode("name").value:"聚合"+maxfCtlNum;
				   var joins=new joinnode(thisIDName,thisCaption,0,0);
				   fSelectedObj=joins.vmlobj;
				   break;
			   case "decision":
				   var thisCaption=elementnode.getAttributeNode("name")!=null?elementnode.getAttributeNode("name").value:"条件"+maxfCtlNum;
				   var conditions=new conditionnode(thisIDName,thisCaption,0,0);
				   fSelectedObj=conditions.vmlobj;
				   break;
			   case "end-state":
				   var thisCaption=elementnode.getAttributeNode("name")!=null?elementnode.getAttributeNode("name").value:"结束"+maxfCtlNum;
				   var ends=new endnode(thisIDName,thisCaption,0,0);
				   fSelectedObj=ends.vmlobj;
				   break;
			   case "node":
				   var thisCaption=elementnode.getAttributeNode("name")!=null?elementnode.getAttributeNode("name").value:"节点"+maxfCtlNum;
				   var ends=new nodenode(thisIDName,thisCaption,0,0);
				   fSelectedObj=ends.vmlobj;
				   break;
			   case "super-state":
				   var thisCaption=elementnode.getAttributeNode("name")!=null?elementnode.getAttributeNode("name").value:"超状态"+maxfCtlNum;
				   var ends=new superstatenode(thisIDName,thisCaption,0,0);
				   fSelectedObj=ends.vmlobj;
				   break;
			   case "process-state":
				   var thisCaption=elementnode.getAttributeNode("name")!=null?elementnode.getAttributeNode("name").value:"子流程"+maxfCtlNum;
				   var ends=new processstatenode(thisIDName,thisCaption,0,0);
				   fSelectedObj=ends.vmlobj;
				   break;				   
			   default:
				   return;
			}
			maxfCtlNum++;
		}
	}else if(flag=="line"){
		if(elementnode.getAttributeNode("atnode")!=null && l[elementnode.getAttributeNode("atnode").value]!=undefined){
			var programline=lineroot.childNodes[l[elementnode.getAttributeNode("atnode").value]];
			
			var attributes = [];
			var plugins = [];
		    var childAttributes = programline.childNodes;
		    var childAttribute;
		    var childAttributeKey;//子属性key值
		    for(var k = 0; k < childAttributes.length; k++){
		   		childAttribute = childAttributes[k];
		   		childAttributeKey = childAttribute.getAttributeNode("key").value;//获取子属性key值
		   		if(childAttributeKey.indexOf("plugins_") == 0){//属于插件属性
		   			plugins[childAttributeKey] = childAttribute.text;
		   		}else{//属于非插件属性
		   			attributes[childAttributeKey] = childAttribute.text;
		   		}
		    }
		    attributes['plugins'] = plugins;//设置插件属性
			
			var lineelement=new line(programline.getAttributeNode("thisIDName").value,programline.getAttributeNode("thisCaption").value,programline.getAttributeNode("fromelement").value,programline.getAttributeNode("toelement").value,programline.getAttributeNode("isStraint").value,programline.childNodes[0].text, attributes);
			addToObjArray(lineelement.thisIDName,false);
			fSelectedObj=lineelement.vmlobj;
		}else{
			var thisIDName=elementnode.getAttributeNode("atnode")!=null?elementnode.getAttributeNode("atnode").value:"w"+maxfCtlNum;
			var thisCaption=elementnode.getAttributeNode("name")!=null?elementnode.getAttributeNode("name").value:"连接"+maxfCtlNum;
			//addToObjArray(thisIDName,false);
			maxfCtlNum++;
			if(elementnode.getAttributeNode("atnode")==null){
				var r=doc.createAttribute("atnode");
				r.value=thisIDName;
				elementnode.setAttributeNode(r);
			}
			if(elementnode.getAttributeNode("name")==null){
				var r=doc.createAttribute("name");
				r.value=thisCaption;
				elementnode.setAttributeNode(r);
			}
			var fromid=elementnode.parentNode.getAttributeNode("atnode").value;
			var toid=root.childNodes[lto[index]].getAttributeNode("atnode").value;

			var lineelement=new line(thisIDName,thisCaption,fromid,toid,1,"");			
			addToObjArray(lineelement.thisIDName,false);
			fSelectedObj=lineelement.vmlobj;
		}
	}
}

//重画元素
function repaintElementEdit(elementnode,flag,index){
	if(flag=="node"){
		if(elementnode.getAttributeNode("atnode")!=null && n[elementnode.getAttributeNode("atnode").value]==true){
			fSelectedObj=eval(elementnode.getAttributeNode("atnode").value);
		}else{
			var thisIDName=elementnode.getAttributeNode("atnode")!=null?elementnode.getAttributeNode("atnode").value:"w"+maxfCtlNum;
			if(elementnode.getAttributeNode("atnode")==null){
				var r=doc.createAttribute("atnode");
				r.value=thisIDName;
				elementnode.setAttributeNode(r);
			}
			switch (elementnode.nodeName){
			   case "start-state":
				   var thisCaption=elementnode.getAttributeNode("name")!=null?elementnode.getAttributeNode("name").value:"开始";
				   var snode=new startnode(thisIDName,thisCaption,0,0);
				   fSelectedObj=snode.vmlobj;
				   break;
			   case "state":
				   var thisCaption=elementnode.getAttributeNode("name")!=null?elementnode.getAttributeNode("name").value:"状态"+maxfCtlNum;
				   var stats=new statnode(thisIDName,thisCaption,0,0);
				   fSelectedObj=stats.vmlobj;
				   break;
			   case "task-node":
				   var thisCaption=elementnode.getAttributeNode("name")!=null?elementnode.getAttributeNode("name").value:"任务"+maxfCtlNum;
				   var tasks=new tasknode(thisIDName,thisCaption,0,0);
				   fSelectedObj=tasks.vmlobj;
				   break;
			   case "fork":
				   var thisCaption=elementnode.getAttributeNode("name")!=null?elementnode.getAttributeNode("name").value:"分支"+maxfCtlNum;
				   var splits=new splitnode(thisIDName,thisCaption,0,0);
				   fSelectedObj=splits.vmlobj;
				   break;
			   case "join":
				   var thisCaption=elementnode.getAttributeNode("name")!=null?elementnode.getAttributeNode("name").value:"聚合"+maxfCtlNum;
				   var joins=new joinnode(thisIDName,thisCaption,0,0);
				   fSelectedObj=joins.vmlobj;
				   break;
			   case "decision":
				   var thisCaption=elementnode.getAttributeNode("name")!=null?elementnode.getAttributeNode("name").value:"条件"+maxfCtlNum;
				   var conditions=new conditionnode(thisIDName,thisCaption,0,0);
				   fSelectedObj=conditions.vmlobj;
				   break;
			   case "end-state":
				   var thisCaption=elementnode.getAttributeNode("name")!=null?elementnode.getAttributeNode("name").value:"结束"+maxfCtlNum;
				   var ends=new endnode(thisIDName,thisCaption,0,0);
				   fSelectedObj=ends.vmlobj;
				   break;
			   case "node":
				   var thisCaption=elementnode.getAttributeNode("name")!=null?elementnode.getAttributeNode("name").value:"节点"+maxfCtlNum;
				   var ends=new nodenode(thisIDName,thisCaption,0,0);
				   fSelectedObj=ends.vmlobj;
				   break;
			   case "super-state":
				   var thisCaption=elementnode.getAttributeNode("name")!=null?elementnode.getAttributeNode("name").value:"超状态"+maxfCtlNum;
				   var ends=new superstatenode(thisIDName,thisCaption,0,0);
				   fSelectedObj=ends.vmlobj;
				   break;
			   case "process-state":
				   var thisCaption=elementnode.getAttributeNode("name")!=null?elementnode.getAttributeNode("name").value:"子流程"+maxfCtlNum;
				   var ends=new processstatenode(thisIDName,thisCaption,0,0);
				   fSelectedObj=ends.vmlobj;
				   break;				   				   				   
			   default:
				   return;
			}
			maxfCtlNum++;
		}
	}else if(flag=="line"){
		if(elementnode.getAttributeNode("atnode")!=null && l[elementnode.getAttributeNode("atnode").value]==true){
			fSelectedObj=eval(elementnode.getAttributeNode("atnode").value);
		}else{
			var thisIDName=elementnode.getAttributeNode("atnode")!=null?elementnode.getAttributeNode("atnode").value:"w"+maxfCtlNum;
			var thisCaption=elementnode.getAttributeNode("name")!=null?elementnode.getAttributeNode("name").value:"连接"+maxfCtlNum;
			//addToObjArray(thisIDName,false);
			maxfCtlNum++;
			if(elementnode.getAttributeNode("atnode")==null){
				var r=doc.createAttribute("atnode");
				r.value=thisIDName;
				elementnode.setAttributeNode(r);
			}
			if(elementnode.getAttributeNode("name")==null){
				var r=doc.createAttribute("name");
				r.value=thisCaption;
				elementnode.setAttributeNode(r);
			}
			var fromid=elementnode.parentNode.getAttributeNode("atnode").value;
			var toid=root.childNodes[lto[index]].getAttributeNode("atnode").value;

			var lineelement=new line(thisIDName,thisCaption,fromid,toid,1,"");
			addToObjArray(lineelement.thisIDName,false);
			fSelectedObj=lineelement.vmlobj;
		}

	}
}

function findToElement(toname){
	for(var i=0;i<root.childNodes.length;i++){
		if(root.childNodes[i].getAttributeNode("name").value==toname){
			return i;
		}
	}
}