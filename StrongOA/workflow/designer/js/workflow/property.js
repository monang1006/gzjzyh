/*------------------------------------------------------------------------------
+                        属性的显示 					                           +
+                            						                           +
+						          		       							       +
-------------------------------------------------------------------------------*/
	var pp_RightInputCss   =    "font:12;width:100%;"+
    							"height:100%;"+
                                "border:none;"+
                                "background-color: Transparent;";

	var d;


//add yubin start

	//工作流JBPM_XML属性状态接口
	function WorkflowPropertyState (aTableObj) {
		//property
		this.tableObj = aTableObj; //属性table对象
		//interface
		this.setProperty = setProperty; 	//设置属性
		//method
		this.clearRows = clearRows;     	//清空所有的行
		this.getRow2 = getRow2; 				//获得一行

		this.getEditRow2 = getEditRow2;			//编辑的行
		this.getComboBoxRow = getComboBoxRow;	//选择的行
		this.getMoreRow = getMoreRow;			//弹出的行

		function setProperty () {
		}
		function clearRows () {
		}
		function getRow2 (aType,tdId,aLable,aFieldName,aFieldValue,aCanEdit) {
		}
		//编辑的行
        function getEditRow2 (type,id,aLable,aFieldName,aFieldValue,aCanEdit) {
			return getRow2 (type,id,aLable,aFieldName,aFieldValue,aCanEdit);
        }
		//选择的行
        function getComboBoxRow (aLable,aFieldName,aFieldValue,aSelectList,aCanEdit) {
			return this.getRow (2,aLable,aFieldName,aFieldValue+":"+aSelectList,aCanEdit);
        }
		//弹出的行
        function getMoreRow (aLable,aFieldName,aUrl) {
			return this.getRow (3,aLable,aFieldName,aUrl,true);
        }


        function getFlowRow (aCaption,aUrl) {
			return getRow(1,aCaption,aUrl,true,true);
        }

	}

	var dtreeindex=1;
	
	
	function WorkflowXmlPropertyState (aTableObj) {
		//inhert
    	this.base=WorkflowPropertyState;
       	this.base(aTableObj);
		//override
		this.setProperty = setProperty;
		function setProperty () {
			d=new dTree("d", false);
			d.add(0,-1,' '+ root.getAttributeNode("name").value+'','','',root.getAttributeNode("name").value,'');
//			tree = new MzTreeView("tree");
//			tree.setIconPath(scriptroot);
//			tree.nodes["0_c0"] = 'text:' + root.getAttributeNode("name").value + ';url:javascript:';
			var node=root;
			if(fSelectedObj.ctlType!=CNST_CTLTYPE_LINE){
				for(var i=0;i<root.childNodes.length;i++){
					if(root.childNodes[i].getAttributeNode("atnode").value==fSelectedObj.id){
						node=root.childNodes[i];
						break;
					}
				}
			}else{
				var lines=doc.getElementsByTagName("transition");
									
				for(var i=0;i<lines.length;i++){

					if(lines[i].getAttributeNode("atnode").value==fSelectedObj.id){
						node=lines[i];
//						d.add(dtreeindex++,0,' '+node.parentNode.getAttributeNode("name").value+'','javascript:','',node.parentNode.getAttributeNode("name").value,'','dtree/img/obj1.gif','dtree/img/obj2.gif',1,'node');
//						tree.nodes["0_c" + dtreeindex++] = 'text:' + node.parentNode.getAttributeNode("name").value + ';url:javascript:';
						break;
					}
				}
			}
			loopXmlNode(node, 0);
			this.tableObj.innerHTML=d.toString();
			dtreeindex=1;
		}

		function loopXmlNode(rootNode, parentIndex){
//			var nodeIndex=dtreeindex;
			
//			var parentId = rootNode.parentNode.getAttributeNode('xmlflag').value;
			if(rootNode.getAttributeNode('xmlflag') != null){
				var nodeId = rootNode.getAttributeNode('xmlflag').value;
				if(rootNode.getAttributeNode("atnode") != null){
					d.add(nodeId, parentIndex, ' ' + rootNode.nodeName + '','contextForXmlatnode()','','','',systemroot + '/dtree/img/obj1.gif',systemroot + '/dtree/img/obj2.gif',1);			
				}else{
	//			d.add(dtreeindex++,rootNode.parentNode.getAttributeNode(),' '+rootNode.nodeName+'','javascript:','','','','dtree/img/obj1.gif','dtree/img/obj2.gif',1);
					d.add(nodeId, parentIndex, ' ' + rootNode.nodeName + '','contextForXmlnode()','','','',systemroot + '/dtree/img/obj1.gif',systemroot + '/dtree/img/obj2.gif',1);
				}
				
				for(var k =0; k<rootNode.attributes.length; k++){
					if(rootNode.attributes[k].name != 'atnode' && rootNode.attributes[k].name != 'xmlflag'){
						d.add(nodeId + '_' + rootNode.attributes[k].name, nodeId,' '+rootNode.attributes[k].name + ':' + rootNode.attributes[k].value+'','contextForXmlattribute()','','','',systemroot + '/dtree/img/obj1.gif',systemroot + '/dtree/img/obj2.gif',1)
					}
				}
	//			var attributes = rootNode.attributes;
	//			tree.nodes["c" + parentIndex + "_c" + dtreeindex++] = 'text:' + rootNode.nodeName + ';url:javascript:';
				for(var j=0;j<rootNode.childNodes.length;j++){
	//				loopXmlNode(rootNode.childNodes[j],nodeIndex);
					loopXmlNode(rootNode.childNodes[j], nodeId);
				}
			}
		}		
	}	

//add yubin end	


/*-----------属性环境角色--------*/
	function PropertyContext () {
		//property
		this.propertyState = new WorkflowXmlPropertyState(); //默认为开始状态
		//method
		this.setPropertyState = setPropertyState;
		this.setProperty = setProperty;
		function setPropertyState () {
			var obj = document.all.tbProperty;
			//合并
			this.propertyState = new WorkflowXmlPropertyState(obj);
		}
		function setProperty () {
  			this.propertyState.setProperty();
		}
	}
/*---end--属性环境角色--------*/
