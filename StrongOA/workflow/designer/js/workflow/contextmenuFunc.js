   
   // 右键菜单函数
    var dm ;

	// 预加载右键菜单，使其大小固定
	ContextMenu.populatePopup([],window);
	ContextMenu.fixSize();
	
	//zw
	function contextForThis() {
   		var eobj,popupoptions;
   		var e = window.event;
   		// 删除节点
		dm = new ContextItem("删除",function(){
			if(confirm("是否要删除？")){
				changedis(fSelectedObj);
			}
		},false);
		//复制按钮
		var btnCopy = new ContextItem("复制",function(){copyNode(e);}, false);
   		popupoptions = [dm, btnCopy];
   		ContextMenu.display(popupoptions);
	}
	
	//zw
	function drawCanvasMenu(){
		var e = window.event;
		var pXy = {};
		pXy.x = e.clientX;
		pXy.y = e.clientY;
		var btnPaste = new ContextItem("粘贴", function (){
			pasteNode(pXy);
		}, false);
		ContextMenu.display([btnPaste]);
	}
	
	//zw
	function deleteMenuFunc(fSelectedObjArray){
		for(var i=0,len=fSelectedObjArray.length;i<len;i++){
			var obj = fSelectedObjArray[i];
			if(obj.ctlType != CNST_CTLTYPE_INITIAL){
				if(obj.ctlType == CNST_CTLTYPE_LINE){
					changedisline(obj);
				}
				else{
					changedis(obj);
				}
			}
		}
	}

	
	// 删除连接
	function contextForThisLine(){
   		var eobj,popupoptions;
		dm = new ContextItem("删除",function(){
			if(confirm("是否要删除？")){
				changedisline(fSelectedObj);
			}
		},false);
    	popupoptions = [dm];
   		ContextMenu.display(popupoptions);		
	}
	function changedis(aobj) {
		var lineinfo=aobj.line;
		var aLines = lineinfo.split(";");
		for(var i=0;i<aLines.length-1;i++){
			var lineObjStr = (aLines[i].split("TO")[0]).split(":")[0];
			changedisline(eval(lineObjStr));
		}
		aobj.nodeclass.delxml();
		removeFromObjArray(aobj);
		aobj.removeNode(true);
		aobj=null;   
		CollectGarbage(); 
		propertyListSubject.notifyObservers();		
	}
	function changedisline(aobj){
		aobj.nodeclass.delxml();
		deleteLine(aobj);
		removeFromObjArray(aobj);
		aobj.nodeclass.movepoint1.removeNode(true);
		aobj.nodeclass.movepoint2.removeNode(true);
		aobj.nodeclass.textobj.removeNode(true);
		aobj.removeNode(true);
		aobj=null;   
		CollectGarbage();
		propertyListSubject.notifyObservers();		 
	}
	function deleteLine(aobj){
		var fromel=aobj.fromelement;
		var toel=aobj.toelement;
		fromel.line=clearLineInfo(aobj.id,fromel.line);
		toel.line=clearLineInfo(aobj.id,toel.line);
	}
	function clearLineInfo(lineid,objline){
		var reLine="";
		var aLines = objline.split(";");
		for(var i=0;i<aLines.length-1;i++){
			var lineObjStr = (aLines[i].split("TO")[0]).split(":")[0];
			if(lineObjStr!=lineid){
				reLine=reLine+aLines[i]+";";
			}
		}
		return reLine;
	}
	
	// xml模型子节点右键菜单
	function contextForXmlnode() {
		var srcElement = window.event.srcElement;
   		var popupoptions;
   		popupoptions = [
   				new ContextItem("添加节点",function(){addXmlnode(srcElement)},false),
   				new ContextItem("添加属性",function(){addXmlattribute(srcElement)},false),
   				new ContextItem("删除节点",function(){delXmlnode(srcElement)},false)
   				       ];
   		ContextMenu.display(popupoptions);
	}
	
	// xml模型节点右键菜单
	function contextForXmlatnode() {
		var srcElement = window.event.srcElement;
   		var popupoptions;
   		popupoptions = [
   				new ContextItem("添加节点",function(){addXmlnode(srcElement)},false),
   				new ContextItem("添加属性",function(){addXmlattribute(srcElement)},false)
   				       ];
   		ContextMenu.display(popupoptions);
	}	
	
	// xml模型属性右键菜单
	function contextForXmlattribute() {
		var srcElement = window.event.srcElement;
   		var popupoptions;
   		popupoptions = [
   				new ContextItem("修改属性",function(){editXmlattribute(srcElement)},false),
   				new ContextItem("删除属性",function(){delXmlattribute(srcElement)},false)
   				       ];
   		ContextMenu.display(popupoptions);
	}
	
	//添加子节点
	function addXmlnode(srcElement){
		var nodeIndex = srcElement.nodeIndex;
		var spanId = srcElement.id;
		var returnValue = window.showModalDialog(systemroot 
						+ "/flow_addNode.jsp", window, "dialogWidth:250px; dialogHeight:120px; center:yes; help:no; resizable:yes; status:no");		
		if(returnValue != null && returnValue != ''){
			try{
				var node = getNodeByAttribute(root, "xmlflag", nodeIndex);
				if(node != null){
					var n = doc.createNode("1", returnValue, "");
					//添加xml节点标识属性
					var r = doc.createAttribute("xmlflag");
					r.value = nCtlNum ++; 
					//添加属性 
					n.setAttributeNode(r);
					node.appendChild(n);
					var str = d.myadd(nCtlNum-1, nodeIndex, ' ' + returnValue, 'contextForXmlnode()','','','',systemroot + '/dtree/img/obj1.gif',systemroot + '/dtree/img/obj2.gif',1);
//					d.changeParentImg(srcElement, str);					
/**					var div = document.createElement("div");
					div.setAttribute("class", "dTreeNode");
					div.innerHTML = str;
//					alert(str);
					var element = document.getElementById('d' + spanId.substring(1));
					if(element){
						var lastdiv = element.childNodes[element.childNodes.length-1];
						var imgs = lastdiv.getElementsByTagName("img");
						var img = imgs[imgs.length-2];
						img.src = d.icon.join;
						element.appendChild(div);
					}else{
						var childDiv = document.createElement("div");
						childDiv.setAttribute("id", 'd' + spanId.substring(1));
						childDiv.setAttribute("class", "clip");
						childDiv.setAttribute("style", "display:block");
						childDiv.appendChild(div); 
						srcElement.parentNode.appendChild(childDiv);
						var img = document.getElementById('j' + spanId.substring(1));
						img.src = d.icon.minusBottom;
						img.onclick = function(){ d.o(spanId.substring(2));};
					}**/
//					var tempStr = srcElement.innerHTML;
//					alert(tempStr);
//					srcElement.innerHTML = tempStr + str;
//					alert(srcElement.parentNode.innerHTML);
//					srcElement.appendChild(div);
					propertyListSubject.notifyObservers();
				}
			}catch(e){
				alert("节点类型不能以数字开头，请重新输入！");
				return false;
			}		
		}
	}
	
	// 根据属性找到相关节点
	function getNodeByAttribute(doc, attribute, value){
		var xmlflag = doc.getAttributeNode(attribute);
		if(xmlflag != null && xmlflag.value == value){
			return doc;
		}else{
			var children = doc.childNodes;
			for(var i=0; i<children.length; i++){
				xmlflag = getNodeByAttribute(children[i], attribute, value);
				if(xmlflag != null){
					return xmlflag;
				}
			}
			return null;
		}
	}	
	
	//添加属性
	function addXmlattribute(srcElement){
		var nodeIndex = srcElement.nodeIndex;
		var spanId = srcElement.id;
		var returnValue = window.showModalDialog(systemroot 
						+ "/flow_addAttribute.jsp", window, "dialogWidth:250px; dialogHeight:200px; center:yes; help:no; resizable:yes; status:no");		
		if(returnValue != null && returnValue != ''){
			try{		
				var node = getNodeByAttribute(root, "xmlflag", nodeIndex);
				if(node != null){
					var r = doc.createAttribute(returnValue.split("|")[0]);
					r.value = returnValue.split("|")[1]; 
					//添加属性 
					node.setAttributeNode(r);
					
					var str = d.myadd(nodeIndex + '_' + returnValue.split("|")[0], nodeIndex, ' ' + returnValue.split("|")[0] + ':' + returnValue.split("|")[1], 'contextForXmlattribute()','','','',systemroot + '/dtree/img/obj1.gif',systemroot + '/dtree/img/obj2.gif',1);
//					d.changeParentImg(srcElement, str);
/**					var div = document.createElement("div");
					div.setAttribute("class", "dTreeNode");
					div.innerHTML = str;
//					alert(str);
					var element = document.getElementById('d' + spanId.substring(1));
					if(element){
						var lastdiv = element.childNodes[element.childNodes.length-1];
						var imgs = lastdiv.getElementsByTagName("img");
						var img = imgs[imgs.length-2];
						img.src = d.icon.join;
						element.appendChild(div);
					}else{
						var childDiv = document.createElement("div");
						childDiv.setAttribute("id", 'd' + spanId.substring(1));
						childDiv.setAttribute("class", "clip");
						childDiv.setAttribute("style", "display:block");
						childDiv.appendChild(div); 
						srcElement.parentNode.appendChild(childDiv);
						var img = document.getElementById('j' + spanId.substring(1));
						img.src = d.icon.minusBottom;
						img.onclick = function(){ d.o(spanId.substring(2));};
					}	**/				
					
					propertyListSubject.notifyObservers();
				}
			}catch(e){
				alert("属性名称不能以数字开头，请重新输入！");
				return false;
			}					
		}
	}	
	
	//修改属性
	function editXmlattribute(srcElement){
		var nodeIndex = srcElement.nodeIndex;
		var spanId = srcElement.id;	
		var returnValue = window.showModalDialog(systemroot 
						+ "/flow_editAttribute.jsp", window, "dialogWidth:250px; dialogHeight:120px; center:yes; help:no; resizable:yes; status:no");		
		if(returnValue != null && returnValue != ''){
			try{		
				var node = getNodeByAttribute(root, "xmlflag", nodeIndex.split("_")[0]);
				if(node != null){
					var r = node.getAttributeNode(nodeIndex.split("_")[1]);
					r.value = returnValue;
					srcElement.innerHTML = nodeIndex.split("_")[1] + ":" + returnValue;
//					propertyListSubject.notifyObservers();
				}
			}catch(e){
				alert("属性名称不能以数字开头，请重新输入！");
				return false;
			}					
		}						
	}
	
	//删除属性
	function delXmlattribute(srcElement){
		var nodeIndex = srcElement.nodeIndex;
		var spanId = srcElement.id;	
		var node = getNodeByAttribute(root, "xmlflag", nodeIndex.split("_")[0]);
		if(node != null){
			node.removeAttribute(nodeIndex.split("_")[1]);
//			d.changePreImg(srcElement);
/**			var div = srcElement.parentNode.parentNode;
			srcElement.parentNode.parentNode.removeChild(srcElement.parentNode);
			if(div.getElementsByTagName("div").length == 0){
				var imgs = div.parentNode.getElementsByTagName("img");
				var img = imgs[imgs.length-2];
				img.src = d.icon.joinBottom;
				img.onclick = function(){}					
			}else{
				if(d.aNodes[parseInt(spanId.substring(2))]._ls){
					var lastdiv = div.childNodes[div.childNodes.length-1];
					var imgs = lastdiv.getElementsByTagName("img");
					var img = imgs[imgs.length-2];
					img.src = d.icon.joinBottom;
				}
			}			**/
			propertyListSubject.notifyObservers();
		}	
	}
	
	//删除子节点
	function delXmlnode(srcElement){
		var nodeIndex = srcElement.nodeIndex;
		var spanId = srcElement.id;	
		var node = getNodeByAttribute(root, "xmlflag", nodeIndex);
		if(node != null){
			node.parentNode.removeChild(node);
//			d.changePreImg(srcElement);
/**			var div = srcElement.parentNode.parentNode;
			srcElement.parentNode.parentNode.removeChild(srcElement.parentNode);
			if(div.getElementsByTagName("div").length == 0){
				var imgs = div.parentNode.getElementsByTagName("img");
				var img = imgs[imgs.length-2];
				img.src = d.icon.joinBottom;
				img.onclick = function(){}				
			}else{
				if(d.aNodes[parseInt(spanId.substring(2))]._ls){
					var lastdiv = div.childNodes[div.childNodes.length-1];
					var imgs = lastdiv.getElementsByTagName("img");
					var img = imgs[imgs.length-2];
					img.src = d.icon.joinBottom;
				}
			}**/
			propertyListSubject.notifyObservers();	
		}
	}		