function findFatherCode(code, ruleCode, topCodeLength, defaultParent){
	var length1 = 0;
	var length2 = 0;
	var codeLength = code.length;
	var fatherCode = "0";
	if(defaultParent != null && defaultParent != ""){
		fatherCode = defaultParent;
	}
	if(topCodeLength != null && topCodeLength != "" && code.length == topCodeLength){//如果当前节点是规定的top节点
		return fatherCode;
	}
	for (var i = 0; i < ruleCode.length; i++) {
		length1 = length1 + parseInt(ruleCode.substring(i, i+1));
		if (i > 0) {
			length2 = length2 + parseInt(ruleCode.substring(i-1, i));
		}
		if (codeLength == length1) {
			if (length2 != 0) {
				fatherCode = code.substring(0, length2);
				break;
			}
		}
	}
	return fatherCode;	
}

// select function
 function gotoSelectCheck(checkFlag,nodeValue,aThis){
    var pattern = new RegExp("\\," + nodeValue + "\\S*\\,");
	if(hasSelectCode != null && hasSelectCode != ""){
		hasSelectCode = hasSelectCode.replace(pattern, ",");
	}
   var node = aThis.GetTreeNode(nodeValue);
   if(checkFlag){
  	 selectFatherNode(checkFlag,node,aThis);
  	 } else{
	   selectFatherNode1(checkFlag,node,aThis); 
   }
   selectSubNode(checkFlag,node,aThis);
 }
 
 function selectSubNode(checkFlag,node,aThis){
 	var obj = document.getElementById("mTree_" + aThis.mObj.id + "_" + node.id);
 	if(obj != null){
 		obj.checked = checkFlag;
 		
	 	if(node.subtree != null){
	 	for(var i=0; i<node.subtree.items.length; i++){
	   		selectSubNode(checkFlag,node.subtree.items[i],aThis);
	   	} 
	   }
   }
 }
 
 function selectFatherNode(checkFlag,node,aThis){
 	var obj = document.getElementById("mTree_" + aThis.mObj.id + "_" + node.id);
 	var check="";
 	if(obj != null){
 		obj.checked = checkFlag;
	 	if(node.fid != null && node.fid != ""){
		 	var fatherNode = aThis.GetTreeNode(node.fid);
		 	for(var i=0; i<fatherNode.subtree.items.length; i++){
		   		check=document.getElementById("mTree_" + aThis.mObj.id + "_" + fatherNode.subtree.items[i].id).checked;
		   		if(!check){
		   			return;
		   		}
		   	} 
		 	//alert(check);
		 	if(check){
		 	selectFatherNode(checkFlag,fatherNode,aThis);}
	 	}
 	}
 }
 function selectFatherNode1(checkFlag,node,aThis){
	 var obj = document.getElementById("mTree_" + aThis.mObj.id + "_" + node.id);
	 	if(obj != null){
	 		obj.checked = checkFlag;
		 	if(node.fid != null && node.fid != ""){
			 	var fatherNode = aThis.GetTreeNode(node.fid);
			 	selectFatherNode1(checkFlag,fatherNode,aThis);
			 	
		 	}
	 	}
	 }
  function getAllCheckedOthers(aThis){
 	var value = "";
 	var obj = document.getElementsByName(aThis.aCheckBoxn);
 	for(var i = 0; i < obj.length; i++){
 		if(obj[i].checked == true){
 			var node = aThis.GetTreeNode(obj[i].value);
 			value = value + "," + node.others;
 		}
    }
    if(value != ""){
    	return value.substring(1);
    }else{
    	return value;
    } 	
 }
 
 
 function getAllCheckedValue(aThis){
 	var value = "";
 	var obj = document.getElementsByName(aThis.aCheckBoxn);
 	for(var i = 0; i < obj.length; i++){
 		if(obj[i].checked == true){
 			value = value + "," + obj[i].value;
 		}
    }
    if(value != ""){
    	return value.substring(1);
    }else{
    	return value;
    } 	
 }
 
 function getAllCheckedObj(aThis){
 	var obj = document.getElementsByName(aThis.aCheckBoxn);
    return obj;
 }
 
 /**
  * select all function
  */
  function selectAll(checkFlag, aThis) {
    var obj;
  	for(var i = 0; i < aThis.mNodes.items.length; i++) {
  		obj = document.getElementById("mTree_" + aThis.mObj.id + "_" + aThis.mNodes.items[i].id);
  		if(obj != null) {
  			obj.checked = checkFlag;
  		}
  	}
  }
