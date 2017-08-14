/*--------------------------------------------------|
| dTree 2.05 | www.destroydrop.com/javascript/tree/ |
|---------------------------------------------------|
| Copyright (c) 2002-2003 Geir Landrö               |
|                                                   |
| This script can be used freely as long as all     |
| copyright messages are intact.                    |
|                                                   |
| Updated: 17.04.2003                               |
|--------------------------------------------------*/

//edited by yubin on 2008.11.03
// Node object
function Node(id, pid, name, url, menu, title, target, icon, iconOpen, open) {
	this.id = id;
	this.pid = pid;
	this.name = name;
	this.url = url;
	this.menu = menu;
	this.title = title;
	this.target = target;
	this.icon = icon;
	this.iconOpen = iconOpen;
	this._io = open || false;
	this._is = false;
	this._ls = false;
	this._hc = false;
	this._ai = 0;
	this._p;
};

// Tree object
function dTree(objName) {
	this.config = {
		target					: null,
		folderLinks			: true,
		useSelection		: true,
		useCookies			: true,
		useLines				: true,
		useIcons				: true,
		useStatusText		: false,
		closeSameLevel	: false,
		inOrder					: false
	}
	this.icon = {
		root				: systemroot + '/dtree/img/flow.gif',
		folder			: systemroot + '/dtree/img/imgfolder.gif',
		folderOpen	: systemroot + '/dtree/img/imgfolder.gif',
		node				: systemroot + '/dtree/img/base.gif',
		empty				: systemroot + '/dtree/img/empty.gif',
		line				: systemroot + '/dtree/img/line.gif',
		join				: systemroot + '/dtree/img/join.gif',
		joinBottom	: systemroot + '/dtree/img/joinbottom.gif',
		plus				: systemroot + '/dtree/img/plus.gif',
		plusBottom	: systemroot + '/dtree/img/plusbottom.gif',
		minus				: systemroot + '/dtree/img/minus.gif',
		minusBottom	: systemroot + '/dtree/img/minusbottom.gif',
		nlPlus			: systemroot + '/dtree/img/nolines_plus.gif',
		nlMinus			: systemroot + '/dtree/img/nolines_minus.gif'
	};
	this.obj = objName;
	this.aNodes = [];
	this.aIndent = [];
	this.nodeAIndent = [];
	this.root = new Node(-1);
	this.selectedNode = null;
	this.selectedFound = false;
	this.completed = false;
};

dTree.prototype.removeAll=function(){
	this.aNodes = [];
	this.aIndent = [];
	this.selectedNode = null;
	this.selectedFound = false;
	this.completed = false;	
}

// Adds a new node to the node array
dTree.prototype.add = function(id, pid, name, url, menu, title, target, icon, iconOpen, open) {
	this.aNodes[this.aNodes.length] = new Node(id, pid, name, url, menu, title, target, icon, iconOpen, open);
};

// edited by yubin on 2008.11.04
dTree.prototype.myadd = function(id, pid, name, url, menu, title, target, icon, iconOpen, open) {
	//alert(this.aIndent.length);
	this.aNodes[this.aNodes.length] = new Node(id, pid, name, url, menu, title, target, icon, iconOpen, open);
	var str = this.myaddNode(this.aNodes[this.aNodes.length-1]);
	return str;
};

// edited by yubin on 2008.11.04
dTree.prototype.myaddNode = function(pNode) {
	var str = '';
	var n=0;
	for (n; n<this.aNodes.length; n++) {
		if (this.aNodes[n].id == pNode.pid) {
			var cn = pNode;
			cn._p = this.aNodes[n];
			cn._ai = this.aNodes.length-1;
			this.setCS(cn);
			if (!cn.target && this.config.target) cn.target = this.config.target;
			if (cn._hc && !cn._io && this.config.useCookies) cn._io = this.isOpen(cn.id);
			if (!this.config.folderLinks && cn._hc) cn.url = null;
			if (this.config.useSelection && cn.id == this.selectedNode && !this.selectedFound) {
					cn._is = true;
					this.selectedNode = n;
					this.selectedFound = true;
			}
			str += this.mynode(cn, this.aNodes.length-1);
			if (cn._ls) break;
		}
	}
	return str;
};

// edited by yubin on 2008.11.04
dTree.prototype.mynode = function(node, nodeId) { 
	var str = '<div class="dTreeNode">' + this.myindent(node, nodeId);
//	var str = this.myindent(node, nodeId);
	if (this.config.useIcons) {
		if (!node.icon) node.icon = (this.root.id == node.pid) ? this.icon.root : ((node._hc) ? this.icon.folder : this.icon.node);
		if (!node.iconOpen) node.iconOpen = (node._hc) ? this.icon.folderOpen : this.icon.node;
		if (this.root.id == node.pid) {
			node.icon = this.icon.root;
			node.iconOpen = this.icon.root;
		}
		str += '<img id="i' + this.obj + nodeId + '" src="' + ((node._io) ? node.iconOpen : node.icon) + '" alt="" />';
	}
	if (node.url) {
		str += '<span nodeIndex = "' + node.id + '" id="s' + this.obj + nodeId + '" class="' + ((this.config.useSelection) ? ((node._is ? 'nodeSel' : 'node')) : 'node') + '" onfocus="blur()" ';
		if (node.title) str += ' title="' + node.title + '"';
		 str += ' onmouseover="window.status=\'' + node.name + '\';this.style.backgroundColor=\'yellow\';return true;" onmouseout="window.status=\'\';this.style.backgroundColor=\'\';return true;" ';
		if (this.config.useSelection && ((node._hc && this.config.folderLinks) || !node._hc))
			str += '';
		// add custmized contextmenu
		if(node.url != null){
			str += ' oncontextmenu = \'' + node.url + '\'';
		}
		str += '>'
	}
	else if ((!this.config.folderLinks || !node.url) && node._hc && node.pid != this.root.id)
		str += '<span href="javascript: ' + this.obj + '.o(' + nodeId + ');" class="node" oncontextmenu = \'contextmenuitem()\'>';
	str += node.name;
	if (node.url || ((!this.config.folderLinks || !node.url) && node._hc)) str += '</span>';
	str += '</div>';
//	this.aIndent.pop();
	return str;
};

// edited by yubin on 2008.11.04
dTree.prototype.myindent = function(node, nodeId) {
	var str = '';
	this.nodeAIndent[node.id] = this.nodeAIndent[node.pid];
	var indents = this.nodeAIndent[node.pid].split(",");	
	if (this.root.id != node.pid) {
		for (var n=0; n<indents.length; n++)
			str += '<img src="' + ( (indents[n] == 1 && this.config.useLines) ? this.icon.line : this.icon.empty ) + '" alt="" />';
		(node._ls) ? this.nodeAIndent[node.id] += ",0" : this.nodeAIndent[node.id] += ",1";		
		
		if (node._hc) {
			str += '<img onclick = "javascript: ' + this.obj + '.o(' + nodeId + ');" id="j' + this.obj + nodeId + '" src="';
			if (!this.config.useLines) str += (node._io) ? this.icon.nlMinus : this.icon.nlPlus;
			else str += ( (node._io) ? ((node._ls && this.config.useLines) ? this.icon.minusBottom : this.icon.minus) : ((node._ls && this.config.useLines) ? this.icon.plusBottom : this.icon.plus ) );
			str += '" alt="" />';		
		} else str += '<img id="j' + this.obj + nodeId + '" src="' + ( (this.config.useLines) ? ((node._ls) ? this.icon.joinBottom : this.icon.join ) : this.icon.empty) + '" alt="" />';
	}
	return str;
};



// Open/close all nodes
dTree.prototype.openAll = function() {
	this.oAll(true);
};
dTree.prototype.closeAll = function() {
	this.oAll(false);
};

// Outputs the tree to the page
dTree.prototype.toString = function() {
	var str = '<div class="dtree">\n';
	if (document.getElementById) {
		if (this.config.useCookies) this.selectedNode = this.getSelected();
		str += this.addNode(this.root);
	} else str += 'Browser not supported.';
	str += '</div>';
	if (!this.selectedFound) this.selectedNode = null;
	this.completed = true;
	return str;
};


// Creates the tree structure
dTree.prototype.addNode = function(pNode) {
	var str = '';
	var n=0;
	if (this.config.inOrder) n = pNode._ai;
	for (n; n<this.aNodes.length; n++) {
		if (this.aNodes[n].pid == pNode.id) {
			var cn = this.aNodes[n];
			cn._p = pNode;
			cn._ai = n;
			this.setCS(cn);
			if (!cn.target && this.config.target) cn.target = this.config.target;
			if (cn._hc && !cn._io && this.config.useCookies) cn._io = this.isOpen(cn.id);
			if (!this.config.folderLinks && cn._hc) cn.url = null;
			if (this.config.useSelection && cn.id == this.selectedNode && !this.selectedFound) {
					cn._is = true;
					this.selectedNode = n;
					this.selectedFound = true;
			}
			str += this.node(cn, n);
			if (cn._ls) break;
		}
	}
	return str;
};

// Creates the node icon, url and text
dTree.prototype.node = function(node, nodeId) { 
	var str = '<div class="dTreeNode">' + this.indent(node, nodeId);
	if (this.config.useIcons) {
		if (!node.icon) node.icon = (this.root.id == node.pid) ? this.icon.root : ((node._hc) ? this.icon.folder : this.icon.node);
		if (!node.iconOpen) node.iconOpen = (node._hc) ? this.icon.folderOpen : this.icon.node;
		if (this.root.id == node.pid) {
			node.icon = this.icon.root;
			node.iconOpen = this.icon.root;
		}
		str += '<img id="i' + this.obj + nodeId + '" src="' + ((node._io) ? node.iconOpen : node.icon) + '" alt="" />';
	}
	if (node.url) {
		str += '<span nodeIndex = "' + node.id + '" id="s' + this.obj + nodeId + '" class="' + ((this.config.useSelection) ? ((node._is ? 'nodeSel' : 'node')) : 'node') + '" onfocus="blur()" ';
		if (node.title) str += ' title="' + node.title + '"';
		 str += ' onmouseover="window.status=\'' + node.name + '\';this.style.backgroundColor=\'yellow\';return true;" onmouseout="window.status=\'\';this.style.backgroundColor=\'\';return true;" ';
		if (this.config.useSelection && ((node._hc && this.config.folderLinks) || !node._hc))
			str += '';
		// add custmized contextmenu
		if(node.url != null){
			str += ' oncontextmenu = \'' + node.url + '\'';
		}
		str += '>'
	}
	else if ((!this.config.folderLinks || !node.url) && node._hc && node.pid != this.root.id)
		str += '<span href="javascript: ' + this.obj + '.o(' + nodeId + ');" class="node" oncontextmenu = \'contextmenuitem()\'>';
	str += node.name;
	if (node.url || ((!this.config.folderLinks || !node.url) && node._hc)) str += '</span>';

	if (node._hc) {
		str += '<div id="d' + this.obj + nodeId + '" class="clip" style="display:' + ((this.root.id == node.pid || node._io) ? 'block' : 'none') + ';">';
		str += this.addNode(node);
		str += '</div>';
	}
	str += '</div>';	
	this.aIndent.pop();
	return str;
};

// Adds the empty and line icons
dTree.prototype.indent = function(node, nodeId) {
	var str = '';
	this.nodeAIndent[node.id] = "";	
	if (this.root.id != node.pid) {
		for (var n=0; n<this.aIndent.length; n++)
			str += '<img src="' + ( (this.aIndent[n] == 1 && this.config.useLines) ? this.icon.line : this.icon.empty ) + '" alt="" />';
		(node._ls) ? this.aIndent.push(0) : this.aIndent.push(1);
		
		for (var n=0; n<this.aIndent.length; n++)
			this.nodeAIndent[node.id] = this.nodeAIndent[node.id] + this.aIndent[n] + ",";		
		this.nodeAIndent[node.id] = this.nodeAIndent[node.id].substring(0, this.nodeAIndent[node.id].length-1);		
		
		if (node._hc) {
/**			str += '<a href="javascript: ' + this.obj + '.o(' + nodeId + ');" onfocus="blur()"><img id="j' + this.obj + nodeId + '" src="';
			if (!this.config.useLines) str += (node._io) ? this.icon.nlMinus : this.icon.nlPlus;
			else str += ( (node._io) ? ((node._ls && this.config.useLines) ? this.icon.minusBottom : this.icon.minus) : ((node._ls && this.config.useLines) ? this.icon.plusBottom : this.icon.plus ) );
			str += '" alt="" /></a>';**/
			str += '<img onclick = "javascript: ' + this.obj + '.o(' + nodeId + ');" id="j' + this.obj + nodeId + '" src="';
			if (!this.config.useLines) str += (node._io) ? this.icon.nlMinus : this.icon.nlPlus;
			else str += ( (node._io) ? ((node._ls && this.config.useLines) ? this.icon.minusBottom : this.icon.minus) : ((node._ls && this.config.useLines) ? this.icon.plusBottom : this.icon.plus ) );
			str += '" alt="" />';		
		} else str += '<img id="j' + this.obj + nodeId + '" src="' + ( (this.config.useLines) ? ((node._ls) ? this.icon.joinBottom : this.icon.join ) : this.icon.empty) + '" alt="" />';
	}
	return str;
};

// Checks if a node has any children and if it is the last sibling
dTree.prototype.setCS = function(node) {
	var lastId;
	for (var n=0; n<this.aNodes.length; n++) {
		if (this.aNodes[n].pid == node.id) node._hc = true;
		if (this.aNodes[n].pid == node.pid) lastId = this.aNodes[n].id;
	}
	if (lastId==node.id) node._ls = true;
};

// Returns the selected node
dTree.prototype.getSelected = function() {
	var sn = this.getCookie('cs' + this.obj);
	return (sn) ? sn : null;
};

// Highlights the selected node
dTree.prototype.s = function(id) {
	if (!this.config.useSelection) return;
	var cn = this.aNodes[id];
	if (cn._hc && !this.config.folderLinks) return;
	if (this.selectedNode != id) {
		if (this.selectedNode || this.selectedNode==0) {
			eOld = document.getElementById("s" + this.obj + this.selectedNode);
			eOld.className = "node";
		}
		eNew = document.getElementById("s" + this.obj + id);
		eNew.className = "nodeSel";
		this.selectedNode = id;
		if (this.config.useCookies) this.setCookie('cs' + this.obj, cn.id);
	}
};

// Toggle Open or close
dTree.prototype.o = function(id) {
	var cn = this.aNodes[id];
	this.nodeStatus(!cn._io, id, cn._ls);
	cn._io = !cn._io;
	if (this.config.closeSameLevel) this.closeLevel(cn);
	if (this.config.useCookies) this.updateCookie();
};

// Open or close all nodes
dTree.prototype.oAll = function(status) {
	for (var n=0; n<this.aNodes.length; n++) {
		if (this.aNodes[n]._hc && this.aNodes[n].pid != this.root.id) {
			this.nodeStatus(status, n, this.aNodes[n]._ls)
			this.aNodes[n]._io = status;
		}
	}
	if (this.config.useCookies) this.updateCookie();
};

// Opens the tree to a specific node
dTree.prototype.openTo = function(nId, bSelect, bFirst) {
	if (!bFirst) {
		for (var n=0; n<this.aNodes.length; n++) {
			if (this.aNodes[n].id == nId) {
				nId=n;
				break;
			}
		}
	}
	var cn=this.aNodes[nId];
	if (cn.pid==this.root.id || !cn._p) return;
	cn._io = true;
	cn._is = bSelect;
	if (this.completed && cn._hc) this.nodeStatus(true, cn._ai, cn._ls);
	if (this.completed && bSelect) this.s(cn._ai);
	else if (bSelect) this._sn=cn._ai;
	this.openTo(cn._p._ai, false, true);
};

// Closes all nodes on the same level as certain node
dTree.prototype.closeLevel = function(node) {
	for (var n=0; n<this.aNodes.length; n++) {
		if (this.aNodes[n].pid == node.pid && this.aNodes[n].id != node.id && this.aNodes[n]._hc) {
			this.nodeStatus(false, n, this.aNodes[n]._ls);
			this.aNodes[n]._io = false;
			this.closeAllChildren(this.aNodes[n]);
		}
	}
}

// Closes all children of a node
dTree.prototype.closeAllChildren = function(node) {
	for (var n=0; n<this.aNodes.length; n++) {
		if (this.aNodes[n].pid == node.id && this.aNodes[n]._hc) {
			if (this.aNodes[n]._io) this.nodeStatus(false, n, this.aNodes[n]._ls);
			this.aNodes[n]._io = false;
			this.closeAllChildren(this.aNodes[n]);		
		}
	}
}

// Change the status of a node(open or closed)
dTree.prototype.nodeStatus = function(status, id, bottom) {
	eDiv	= document.getElementById('d' + this.obj + id);
	eJoin	= document.getElementById('j' + this.obj + id);
	if (this.config.useIcons) {
		eIcon	= document.getElementById('i' + this.obj + id);
		eIcon.src = (status) ? this.aNodes[id].iconOpen : this.aNodes[id].icon;
	}
	eJoin.src = (this.config.useLines)?
	((status)?((bottom)?this.icon.minusBottom:this.icon.minus):((bottom)?this.icon.plusBottom:this.icon.plus)):
	((status)?this.icon.nlMinus:this.icon.nlPlus);
	eDiv.style.display = (status) ? 'block': 'none';
};


// [Cookie] Clears a cookie
dTree.prototype.clearCookie = function() {
	var now = new Date();
	var yesterday = new Date(now.getTime() - 1000 * 60 * 60 * 24);
	this.setCookie('co'+this.obj, 'cookieValue', yesterday);
	this.setCookie('cs'+this.obj, 'cookieValue', yesterday);
};

// [Cookie] Sets value in a cookie
dTree.prototype.setCookie = function(cookieName, cookieValue, expires, path, domain, secure) {
	document.cookie =
		escape(cookieName) + '=' + escape(cookieValue)
		+ (expires ? '; expires=' + expires.toGMTString() : '')
		+ (path ? '; path=' + path : '')
		+ (domain ? '; domain=' + domain : '')
		+ (secure ? '; secure' : '');
};

// [Cookie] Gets a value from a cookie
dTree.prototype.getCookie = function(cookieName) {
	var cookieValue = '';
	var posName = document.cookie.indexOf(escape(cookieName) + '=');
	if (posName != -1) {
		var posValue = posName + (escape(cookieName) + '=').length;
		var endPos = document.cookie.indexOf(';', posValue);
		if (endPos != -1) cookieValue = unescape(document.cookie.substring(posValue, endPos));
		else cookieValue = unescape(document.cookie.substring(posValue));
	}
	return (cookieValue);
};

// [Cookie] Returns ids of open nodes as a string
dTree.prototype.updateCookie = function() {
	var str = '';
	for (var n=0; n<this.aNodes.length; n++) {
		if (this.aNodes[n]._io && this.aNodes[n].pid != this.root.id) {
			if (str) str += '.';
			str += this.aNodes[n].id;
		}
	}
	this.setCookie('co' + this.obj, str);
};

// [Cookie] Checks if a node id is in a cookie
dTree.prototype.isOpen = function(id) {
	var aOpen = this.getCookie('co' + this.obj).split('.');
	for (var n=0; n<aOpen.length; n++)
		if (aOpen[n] == id) return true;
	return false;
};

// If Push and pop is not implemented by the browser
if (!Array.prototype.push) {
	Array.prototype.push = function array_push() {
		for(var i=0;i<arguments.length;i++)
			this[this.length]=arguments[i];
		return this.length;
	}
};
if (!Array.prototype.pop) {
	Array.prototype.pop = function array_pop() {
		lastElement = this[this.length-1];
		this.length = Math.max(this.length-1,0);
		return lastElement;
	}
};

// change the image of the parent Node
dTree.prototype.changeParentImg = function(srcElement, str){
//	var div = document.createElement("div");
//	div.setAttribute("class", "dTreeNode");
//	div.innerHTML = str;
	var element = document.getElementById('d' + srcElement.id.substring(1));
	if(element){
//		var lastdiv = element.childNodes[element.childNodes.length-1];
//		var imgs = lastdiv.getElementsByTagName("img");
//		var img = imgs[imgs.length-2];
//		img.src = this.icon.join;
		var html = element.innerHTML;
		element.innerHTML = str + html;
//		element.appendChild(div);
	}else{
		var childDiv = document.createElement("div");
		childDiv.setAttribute("id", 'd' + srcElement.id.substring(1));
		childDiv.setAttribute("class", "clip");
		childDiv.setAttribute("style", "display:block");
//		childDiv.appendChild(div); 
		childDiv.innerHTML = str;
		srcElement.parentNode.appendChild(childDiv);
		var img = document.getElementById('j' + srcElement.id.substring(1));
		img.src = this.icon.minusBottom;
		img.onclick = function(){ this.o(srcElement.id.substring(1).replace(this.obj, ""));};
	}
}


// change the image of the pre Node
dTree.prototype.changePreImg = function(srcElement){
	var div = srcElement.parentNode.parentNode;
	srcElement.parentNode.parentNode.removeChild(srcElement.parentNode);
	if(div.getElementsByTagName("div").length == 0){
		var imgs = div.parentNode.getElementsByTagName("img");
		var img = imgs[imgs.length-2];
		if(img.src == this.icon.minus){
			img.src = this.icon.minusBottom;
		}else{
			img.src = this.icon.joinBottom;
		}
		img.onclick = function(){}					
	}else{
		if(this.aNodes[parseInt(srcElement.id.substring(1).replace(this.obj, ""))]._ls){
			var lastdiv = div.childNodes[div.childNodes.length-1];
			var imgs = lastdiv.getElementsByTagName("img");
			var img = imgs[imgs.length-2];
			img.src = this.icon.joinBottom;
		}
	}
}