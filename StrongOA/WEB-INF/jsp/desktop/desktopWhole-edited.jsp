<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib prefix="s" uri="/struts-tags"%>
<%@include file="/common/include/rootPath.jsp"%>
<html>
	<head>
		<title>门户设置</title>
		<%@include file="/common/include/meta.jsp"%>
		<LINK href="<%=frameroot%>/css/properties_windows_add.css" type=text/css rel=stylesheet>
		<link rel="stylesheet" type="text/css" href="<%=root%>/oa/css/desktop/style.css">
		<link href="<%=frameroot%>/css/properties_windows.css" type=text/css rel=stylesheet>
		<script type="text/javascript" src="<%=root%>/oa/js/desktop/prototype.js"></script>
		<script type="text/javascript" src="<%=root%>/oa/js/desktop/common.js"></script>
		<script type="text/javascript" src="<%=root%>/oa/js/desktop/follow.js"></script>
		<script type="text/javascript" src="<%=root%>/oa/js/desktop/index/drag.js"></script>
		<style type="text/css">
		div.UserCard {
			position: absolute;
			z-index: 999;
			width: 270px;
			height: 143px;
		}
			BODY{margin-top: 2px;margin-right:0px;margin-left:0px;}
			
			#title{ border-bottom:1px solid #bbb; font-size:12px; padding:0 0 10px;}
			#title h1{ font-size:16px;}
			#title #intro{}
			.col_div{float:left;margin:0px 0px 0px 0px;}
			.drag_div , .modbox{ width:100%; margin:0px 5px 5px 5px auto; border:1px solid #999; padding:0px;}
			.drag_header{ height:22px }
			.drag_content{ height:40px; padding:5px;}
			.drag_editor{
				background:#EBEBEB;
				padding:10px;
				color:#333;
			}
			.no_drag{ height:0px; overflow:hidden; padding:0; border:0;}
			
			.btitle{
				display:inline;
			}
			.imglink{
				cursor:hand;margin-right:2px;
			}
			
			.imglinkgray{
				cursor:hand;margin-right:2px;
				filter:alpha(opacity=50);
				filter:gray();
			}
			
			#popupImgMenuID{
				border: 1px solid #A2C7D9;
				background-color: #F1F7F9;
				padding:5px;
				width: 200px;
				
			}
			</style>

		<style id="popupstyle">
			.popupImgMenu {
			
			}
			.popupImgMenu DIV,SPAN{
				font-size:12px
			}
		</style>

		<style type="text/css">
			.block_editor_a{
				display:inline;
				width:30%;
				padding:2px;
			
			}
			.block_editor_b{
				display:inline;
				width:70%;
				padding:2px;
				text-align:left;
			}
			
			.colorblock{
				display:inline;
				border:1px solid #666;
				width:15px;
				height:15px;
				margin-right:5px;
			
			}
			
			.block_button{
				border:1px solid #333;
				width:50px;
				height:18px;
				margin-left:10px;
			}
			.block_input{
				border:1px solid #666;
				height:18px;
			}
			ul li{
				padding:2px;
				list-style-image:url("<%=path%>/oa/image/desktop/index_i/dot.gif");
			}
		
		</style>

		<style>
			.layoutnum{
				width:30px;
				background:#EEE;
				border:1px solid #AAA;
				margin-left:10px;
				text-align:center;
				display:inline;
				font-weight:bold;
				color:#666;
				cursor:hand;
			}
			.layoutnumselect{
				width:30px;
				background:#D8E0F5;
				border:1px solid #AAA;
				margin-left:10px;
				text-align:center;
				display:inline;
				font-weight:bold;
				color:#666;
				cursor:hand;
			}
			.panelcon{
				padding:5px;
				border-bottom:1px solid #eee;
				background:#fff;
				width:100%;
				color:#333;
			
			}
			.panelicon{
				margin-right:10px;
			}
			.paneladdimg{
				margin-left:10px;
				cursor:hand;
			}
		</style>
		<style>
			.linkDiv{
				border-bottom:1px #DDDDEE dotted;
				padding:3px;
			}
			.linkDiv a:visited{
			color:#999;
			}
			
			.linktextOp{
				border-bottom: 1px double #0033CC;
				color:#0033CC;
				cursor:hand;
			}
			.linkgray{
				color:#999;
			}
			.linkgray10{
				color:#999;
				font-size:10px;
			}
			.linktext:link{ color: #0033CC; TEXT-DECORATION: underline;}
			.linktext:visited{ COLOR: #0033CC; TEXT-DECORATION: underline}
			.linktext:active{ COLOR: #0033CC; TEXT-DECORATION: underline}
			.linktext:hover   { color: #ff0000; TEXT-DECORATION: underline}
			
			A:link { color: #344456; TEXT-DECORATION: none;}
			A:visited { color: #344456; TEXT-DECORATION:none }
			A:active  { color: #3333ff; TEXT-DECORATION: underline}
			A:hover   { color: #ff0000; TEXT-DECORATION: underline}
		</style>
		
		<script>
			function OpenWindow(Url, Width, Height, WindowObj) {
				var ReturnStr = showModalDialog(Url, 
				                                WindowObj, 
				                                "dialogWidth:" + Width + "pt;dialogHeight:" + Height + "pt;"+
				                                "status:no;help:no;scroll:no;");
				return ReturnStr;
			}
			function toViewMail(mailid){
				var msg=OpenWindow('<%=root%>/mymail/mail!desktopview.action?sendid='+mailid, '700', '400', window);
			}
			//增加元素
			function addBlock(blocktype,blocktitle){
				var layouttype = $('layouttype').value;
				url = "<%=root%>/desktop/desktopSection!save.action";
				//addDragDiv('1',0,0,0,'gray','',blocktitle,blocktype);
				//queryString = "actionType=add&blocktype="+blocktype+"&blocklayout="+layouttype;
				queryString = "wholeId=${whole.desktopId}&blocktype="+blocktype+"&blocklayout="+layouttype;
				
				
				
				new Ajax.Request
				(
					url,
					{
						method: "post",	
						onSuccess : function(resp)
									{
										var returnvalue = trim(resp.responseText);
										if(returnvalue=="error"){
											alert("首页内容中已添加，请重新选择。");
											return;
										}
										var pos = returnvalue.indexOf(",");
										var blockid = null;
										var blockurl = null;
										if(pos!=-1){
											blockid = returnvalue.substring(0,pos);
											blockurl = returnvalue.substring(pos+1);
										}else{
											blockid = returnvalue;
											blockurl = null;
										}
										addDragDiv(blockid,0,0,0,'gray','',blocktitle,blocktype,blockurl);
									},
						onFailure : function()
									{
										//alert(url);
										
									},
						parameters : queryString
					}
				);
			
			}
		
		
		function addDragDiv(blockid,col,pos,isnotinit,blocktpl,imgurl,blocktitle,blocktype,blockurl){
			var col,blocktpl,imgurl,blocktitle;
			if(typeof(blocktpl) == "undefined" || blocktpl == ""){
				var blocktpl = "gray";
			}
			if(typeof(imgurl) == "undefined" || imgurl == ""){
				var imgurl = "box.gif";
			}
			if(typeof(blocktitle) == "undefined" || blocktitle == ""){
				var blocktitle = "新的模块";
			}
		
			var layouttype = $('layouttype').value;
			var mvAry = [];
			
			mvAry[mvAry.length]  =' <div id="drag_'+blockid+'" class="drag_div" style="background:#FFF;" url="'+scriptroot+blockurl+'">';
			
		
			mvAry[mvAry.length]  ='		<div style="width:100%;background:url(<%=path%>/oa/image/desktop/index_i/tpl/'+blocktpl+'/title_bg.png);height:25px;" id="drag_title_'+blockid+'">';
			mvAry[mvAry.length]  ='		<table width="100%" cellpadding="0" cellspacing="0" border="0">';
			mvAry[mvAry.length]  ='			<input type="hidden" name="blocktypevalue_'+blockid+'" id="blocktypevalue_'+blockid+'" value="'+blocktype+'">';
			mvAry[mvAry.length]  ='			<tr>';
			mvAry[mvAry.length]  ='				<td style="width:142px;font-weight:bold;padding:2px">';
			mvAry[mvAry.length]  ='					<img src="<%=path%>/oa/image/desktop/attachment/index/'+imgurl+'" id="drag_img_'+blockid+'" class="imglink" align="absmiddle" onclick="showPopup(\''+blockid+'\')"> <span id="drag_text_'+blockid+'">'+blocktitle+'</span>';
			mvAry[mvAry.length]  ='				</td>';
			mvAry[mvAry.length]  ='				<td>';
			mvAry[mvAry.length]  ='					<div id="drag_'+blockid+'_h" class="drag_header" style="width:100%;height:25px">&nbsp;</div>';
			mvAry[mvAry.length]  ='				</td>';
			mvAry[mvAry.length]  ='				<td align="right" style="width:60px;" onmousemove="switchOptionImg(\''+blockid+'\',1)" onmouseout="switchOptionImg(\''+blockid+'\',0)">';
			mvAry[mvAry.length]  ='					<img src="<%=path%>/oa/image/desktop/index_i/close.gif" class="imglinkgray" id="drag_switch_img_'+blockid+'" onclick="switchDrag(\'drag_switch_'+blockid+'\',this)" title="展开/隐藏" style="display:"> <img src="<%=path%>/oa/image/desktop/index_i/refresh.gif" class="imglinkgray" id="drag_refresh_img_'+blockid+'" onclick="resetDragContent(\''+blockid+'\');loadDragContent(\''+blockid+'\');" title="刷新" style="display:"> <img src="<%=path%>/oa/image/desktop/index_i/edit.gif" class="imglinkgray" title="编辑" onclick="modifyBlock(\''+blockid+'\')" id="drag_edit_img_'+blockid+'" style="display:"> <img src="<%=path%>/oa/image/desktop/index_i/closetab.gif" class="imglinkgray" onclick="delDragDiv(\''+blockid+'\')" title="删除" id="drag_delete_img_'+blockid+'" style="display:">';
			mvAry[mvAry.length]  ='				</td>';
			mvAry[mvAry.length]  ='			</tr>';
			mvAry[mvAry.length]  ='		</table>';
			mvAry[mvAry.length]  ='		</div>';
		
		
			mvAry[mvAry.length]  =' 	<div id="drag_switch_'+blockid+'">';
			mvAry[mvAry.length]  =' 		<div class="drag_editor" id="drag_editor_'+blockid+'" style="display:none">';
			mvAry[mvAry.length]  ='			<div id="loadeditorid_'+blockid+'" style="width:100px"><img src="<%=path%>/oa/image/desktop/loading.gif"><span id="loadeditortext_'+blockid+'" style="color:#333"></span></div>';
					
			mvAry[mvAry.length]  =' 		</div>';
			mvAry[mvAry.length]  =' 		<div class="drag_content" id="drag_content_'+blockid+'"><div id="loadcontentid_'+blockid+'" style="width:100px"><img src="<%=path%>/oa/image/desktop/loading.gif"><span id="loadcontenttext_'+blockid+'" style="color:#333"></span></div>';
			mvAry[mvAry.length]  =' 		</div>';
			mvAry[mvAry.length]  ='		</div>';
			mvAry[mvAry.length]  =' </div>';
			//alert(mvAry);
			var colnum;
			if(col > 0){
				colnum = col;
			}
			else{
				colnum = layouttype;
			}
			
			var objCol = document.getElementById("col_"+colnum);
			var objColChilds = objCol.getElementsByTagName("div");
			
			var objPos = objColChilds[0];
			if(pos > 0){
				objPos = document.getElementById("col_"+colnum+"_hidden_div");
			}
			
			var newdiv = document.createElement("div");
				newdiv.innerHTML = mvAry.join("");
			objCol.insertBefore(newdiv,objPos);
			
			if(typeof(isnotinit) == "undefined" || isnotinit == 0){
				initDrag();
			}
			
			//alert(document.getElementById("test").innerHTML);
			loadDragContent(blockid);
			saveorder();
			//alert(document.getElementById("test").innerHTML);
			//alert("addDragDiv("+blockid+"):"+DragUtil.getSortIndex());
		}
		
		//删除元素
		function delDragDiv(blockid){
		//	alert(blockid);
			Element.hide('popupImgMenuID');
			var logicv = window.confirm("确定删除这个模块吗？");
			if(logicv){
				var rid = document.getElementById("drag_"+blockid);
				rid.parentNode.removeChild(rid); 
				url = "desktopSection!delete.action";
				queryString = "blockid="+blockid+"&wholeId=${whole.desktopId}";
				new Ajax.Request(url,{method: "post",parameters : queryString});
				saveorder();
			}
		}
		
		//展开隐藏元素
		function switchDrag(tid,imgid){
			var openurl = "<%=root%>/oa/image/desktop/index_i/close.gif";
			var closeurl = "<%=root%>/oa/image/desktop/index_i/open.gif";
			showHiddenInfo(tid,imgid,openurl,closeurl);
		
		}
		
		//展开隐藏所有元素
		function switchDragAll(imgid){
			var openurl = "<%=root%>/oa/image/desktop/index_i/close.gif";
			var closeurl = "<%=root%>/oa/image/desktop/index_i/open.gif";
		
			var objSS = document.getElementById('showstatus');
			var n = objSS.value;
		
			var aryBlockId = [];
			aryBlockId = getAllDragDiv();
			var blockid = 0;
			for(var i=0;i<aryBlockId.length;i++){
				blockid = aryBlockId[i];
				var objDragSwitch = document.getElementById('drag_switch_'+blockid);
				
				if(n==1){
					objSS.value = 0;
					imgid.url = openurl;
					hiddenInfo(objDragSwitch,imgid,openurl);
				}
				else{
					objSS.value = 1;
					imgid.url = closeurl;
					showInfo(objDragSwitch,imgid,closeurl);
				}
			}
			
		}
		
		//取所有拖动元素的blockid
		function getAllDragDiv(){
			var odjLT = document.getElementById('layouttype');
			var m = odjLT.value;
			var aryBlockId = [];
			for(var j=1;j<=m;j++){
				var col = document.getElementById("col_"+j);
				var colChilds = col.getElementsByTagName("div");
				for(var i=0;i<colChilds.length;i++){
					if(colChilds[i].className == 'drag_div'){
						var blockid = colChilds[i].id.replace("drag_","");
						aryBlockId[aryBlockId.length] = blockid;
					}
				}
			}
			return aryBlockId;
		}
		
		//修改元素样式
		function switchTpl(id,tpl){
			var objDragTitle = document.getElementById("drag_title_"+id);
			var objDrag      = document.getElementById("drag_"+id);
			var objBlockTpl  = document.getElementById("blocktpl_"+id);
			objBlockTpl.value = tpl;
			//objDragTitle.style.backgroundImage = "url('<%=path%>/oa/image/desktop/index_i/tpl/"+tpl+"/title_bg.png')";
			var bordercolor = getTplBolderColor(tpl)
			objDrag.style.borderColor = bordercolor;
		
			url = "<%=root%>/desktop/desktopSection!change.action";
			queryString = "blockid="+id+"&tpl="+tpl;
			new Ajax.Request(url,{method: "post",parameters : queryString});
		}
		
		//修改元素标题
		function changeDragText(id){
			var objDragText = document.getElementById("drag_text_"+id);
			var objBlocktitle = document.getElementById("blocktitle_"+id);
			objDragText.innerHTML = objBlocktitle.value;
		}
		
		//展开元素编辑器
		function modifyBlock(id){
			Element.show('drag_switch_'+id);
			Element.hide('popupImgMenuID');
			var objDivID = document.getElementById('drag_editor_'+id);
		
			if(objDivID.style.display == ""){
				objDivID.style.display = "none";
				objDivID.innerHTML = '<div id="loadeditorid_'+id+'" style="width:100px"><img src="<%=path%>/oa/image/desktop/loading.gif"><span id="loadeditortext_'+id+'" style="color:#333"></span></div>'
				return ;
			}
		
			var objOtext = document.getElementById('loadeditortext_'+id);
		
			objDivID.style.display = "";
			objOtext.innerHTML = " 加载编辑器...";
			
			var saveGimgEditor = {
				onCreate: function(){
					Element.show('loadeditorid_'+id);
				},
				onComplete: function() {
					if(Ajax.activeRequestCount == 0){
						Element.hide('loadeditorid_'+id);
					}
				}
			};
			Ajax.Responders.register(saveGimgEditor);	
		    url="<%=root%>/desktop/desktopSection!showEdit.action";
			queryString = "blockid="+id+"&isEdit=edit";
			
			new Ajax.Request
			(
				url,
				{
					method: "post",	
					onSuccess : function(resp)
								{
									
									objDivID.innerHTML = resp.responseText;
									
								},
					onFailure : function()
								{
									//alert(url);
									
								},
					parameters : queryString
				}
			);
		
		
		
		}
		
		//保存编辑器内容
		function saveDragEditor(blockid){

			Element.hide('popupImgMenuID');
			
			var blocktitle = "";
			var blockrow = "5";
			var subjectlength = "20";
			var sectionFontSize = "12";
			var isshowcreator = 0;
			var isshowdate = 0;
			var isClose = 0;
			var blockeffect = 0;
			var commonid = 0;
			var blocktpl = "gray";	
			var commonstr = "";
			
			
			if(document.getElementById('blocktitle_'+blockid) != null){
				var objBlockTitle = document.getElementById('blocktitle_'+blockid);
				blocktitle = objBlockTitle.value;
			}
			if(document.getElementById('blockrow_'+blockid) != null){
				var objBlockRow = document.getElementById('blockrow_'+blockid);
				if((!isInteger(objBlockRow.value)) || objBlockRow.value == 0){
					objBlockRow.select();
					alert("请输入大于0的数字！");
					return;
				
				}
				blockrow = objBlockRow.value;
			}
		
			if(document.getElementById('subjectlength_'+blockid) != null){
				var objSubjectLength = document.getElementById('subjectlength_'+blockid);
				if((!isInteger(objSubjectLength.value)) || objSubjectLength.value == 0){
					objSubjectLength.select();
					alert("请输入大于0的数字！");
					return;
				
				}
				subjectlength = objSubjectLength.value;
			}

			if(document.getElementById('sectionFontSize_'+blockid).value != ""){
				if(document.getElementById('sectionFontSize_'+blockid).value == 0){
					sectionFontSize = "12";
				}else{
					sectionFontSize = document.getElementById('sectionFontSize_'+blockid).value;
				}
			}
		
			if(document.getElementById('isshowcreator_'+blockid) != null){
				var objIsshowCreator = document.getElementById('isshowcreator_'+blockid);
				if(objIsshowCreator.checked == true){
					isshowcreator=1;
				}
				else{
					isshowcreator=0;
				}
			}
			if(document.getElementById('isshowdate_'+blockid) != null){
				var objIshowDate = document.getElementById('isshowdate_'+blockid);
				if(objIshowDate.checked == true){
					isshowdate=1;
				}
				else{
					isshowdate=0;
				}
			}
			
			if(document.getElementById('isClose_'+blockid) != null){
				var objIsClose = document.getElementById('isClose_'+blockid);
				if(objIsClose.checked == true){
					isClose=1;
				}
				else{
					isClose=0;
				}
			}
			
			if(document.getElementById('blockeffect_'+blockid) != null){
				var objBlockEffect = document.getElementById('blockeffect_'+blockid);
				blockeffect = objBlockEffect.value;
			}
			if(document.getElementById('commonid_'+blockid) != null){
				var objCommonid = document.getElementById('commonid_'+blockid);
				commonid = objCommonid.value;
			}
			if(document.getElementById('commonstr_'+blockid) != null){
				var objCommonstr = document.getElementById('commonstr_'+blockid);
				commonstr = objCommonstr.value;
			}
			if(document.getElementById('blocktpl_'+blockid) != null){
				var objBlockTpl = document.getElementById('blocktpl_'+blockid);
				blocktpl = objBlockTpl.value;
			}
			
			var par = "";
			par += '&blocktitle='+blocktitle;
			par += '&blockrow='+blockrow;
			par += '&subjectlength='+subjectlength;
			par += '&sectionFontSize='+sectionFontSize;
			par += '&isshowcreator='+isshowcreator;
			par += '&isshowdate='+isshowdate;
			par += '&isClose='+isClose;
			par += '&blockeffect='+blockeffect;
			par += '&commonid='+commonid;
			par += '&commonstr='+commonstr;
			par += '&blocktpl='+blocktpl;
			par += '&blockid='+blockid;
			
			if(document.getElementById('showType_'+blockid) != null){
				var objShowType = document.getElementById('showType_'+blockid);//4028822f1fea0ed4011fea171fc10002
				showType=objShowType.value;
			}else{
				showType=null;
			}
			
			if(typeof(showType)=='undefined'||showType==null){
				//alert(typeof(showType));
			}else{
				par += '&showType='+showType;
				//alert(showType.value);
			}
			url = "<%=root%>/desktop/desktopSection!updateSection.action";
			queryString = par+"&isEdit=edit";
			new Ajax.Request
			(
				url,
				{
					method: "post",	
					onSuccess : function(resp)
								{
									modifyBlock(blockid);
									resetDragContent(blockid)
									loadDragContent(blockid);
								},
					onFailure : function()
								{
									//alert(url);
									
								},
					parameters : queryString
				}
			);
			window.location.reload();
		
		
		}
		
		//
		function resetDragContent(id){
			var objDivID = document.getElementById('drag_content_'+id);
			if(document.getElementById('loadcontentid_'+id) == null){
				objDivID.innerHTML = '<div id="loadcontentid_'+id+'" style="width:100px"><img src="<%=path%>/oa/image/desktop/loading.gif"><span id="loadcontenttext_'+id+'" style="color:#333"></span>'
			
			}
		
		}
		
		
		//加载元素内容
		function loadDragContent(id){
			var objDivID = document.getElementById('drag_content_'+id);
			var objOtext = document.getElementById('loadcontenttext_'+id);
		
			var objLoadcontentid = document.getElementById('loadcontentid_'+id);
			
			//在完成移动含有iframe的元素后,objOtex对象为null
			if(objOtext!=null){
				objOtext.innerHTML = "加载内容...";
			}
			
			var saveGimgContent = {
				onCreate: function(){
					Element.show('loadcontentid_'+id);
				},
				onComplete: function() {
					if(Ajax.activeRequestCount == 0){
						Element.hide('loadcontentid_'+id);
					}
				}
			};
			Ajax.Responders.register(saveGimgContent);
			var url = document.getElementById("drag_"+id).url;
			if(url==null||url=="null"||url=="")	
				url=scriptroot+"/archive/archiveborrow/archiveBorrow!showDesktop1.action";
			var sortType=document.getElementById("sortTypeHidden").value;
			if(sortType!=""){
				url=url+"&sortType="+sortType;
			}
			queryString = "blockid="+id;
			new Ajax.Request
			(
				url,
				{
					method: "post",	
					onSuccess : function(resp)
								{
									objDivID.innerHTML = trim(resp.responseText);				
									correctPNG();		//在IE6中正常显示透明PNG 				
								},
					onFailure : function()
								{
									objDivID.innerHTML="对不起您可能没有权限访问该模块，请与管理员联系！";
									//alert(url);
								},
					parameters : queryString
				}
			);
		
		
		
		}
		
		
		
		//展开图标库
		function showPopup(id){
			closeAllItemEditor();
		
			var objBlockID = document.getElementById('tmpblockid');
			objBlockID.value = id;
		
			var objDivID = document.getElementById('popupImgItem');
			var objDivPopup = document.getElementById('popupImgMenuID');
			var objOtext = document.getElementById('loadtext');
		
		    var popupX = 0;
		    var popupY = 0; 
		    contentBox = document.getElementById("popupImgMenuID");
		    var o = event.srcElement;
		    while(o.tagName!="BODY"){
		       popupX += o.offsetLeft;
			   popupY += o.offsetTop;
			   o = o.offsetParent;
			}
		
			objDivPopup.style.left=popupX+20;
			objDivPopup.style.top=popupY;
		
			objDivPopup.style.display="";
		
		
			objOtext.innerHTML = " 加载图标...";
			
			var saveGimgIcon = {
				onCreate: function(){
					Element.show('loadimgid');
				},
				onComplete: function() {
					if(Ajax.activeRequestCount == 0){
						Element.hide('loadimgid');
					}
				}
			};
			Ajax.Responders.register(saveGimgIcon);	
		
			
			url = "<%=root%>/fileNameRedirectAction.action?toPage=/desktop/block_icon.jsp";
			queryString = "";
		
			new Ajax.Request
			(
				url,
				{
					method: "post",	
					onSuccess : function(resp)
								{
									
									objDivID.innerHTML = resp.responseText;
									
								},
					onFailure : function()
								{
									//alert(url);
									
								},
					parameters : queryString
				}
			);
		
			   
		}
		
		//更换图标
		function changeDragIcon(src,blockimg){
			var objBlockID = document.getElementById('tmpblockid');
			var blockid = objBlockID.value;	
			
			var dragImgId = "drag_img_"+blockid;
			var objImgId = document.getElementById(dragImgId);
			objImgId.src = src;
			
			url = "<%=root%>/desktop/desktopSection!changeTpl.action";
			queryString = "blockid="+blockid+"&blockimg="+blockimg;
			new Ajax.Request(url,{method: "post",parameters : queryString});
			
		
			Element.hide('popupImgMenuID');
		
		}
		
		//删除图标
		function deleteDragIcon(){
			var objBlockID = document.getElementById('tmpblockid');
			var blockid = objBlockID.value;
			
			var dragImgId = "drag_img_"+blockid;
			var objImgId = document.getElementById(dragImgId);
			objImgId.src = "<%=path%>/oa/image/desktop/index_i/tmp.gif";
		
			url = "<%=root%>/desktop/desktopSection!changeTpl.action";
			queryString = "blockid="+blockid+"&blockimg=";
			new Ajax.Request(url,{method: "post",parameters : queryString});
		
			Element.hide('popupImgMenuID');
		
		}
		
		//获取模版边框值
		function getTplBolderColor(tpl){
			var bcol = "#999";
			switch(tpl){
				case "navarat":
					bcol = "#FFB0B0";
					break;
				case "orange":
					bcol = "#FFC177";
					break;
				case "yellow":
					bcol = "#FFED77";
					break;
				case "green":
					bcol = "#CBE084";
					break;
				case "blue":
					bcol = "#A1D9ED";
					break;
				case "gray":
					bcol = "#BBBBBB";
					break;
				case "o_navarat":
					bcol = "#B78AA9";
					break;
				case "o_orange":
					bcol = "#D68C6F";
					break;
				case "o_yellow":
					bcol = "#A9B98C";
					break;
				case "o_green":
					bcol = "#96C38A";
					break;
				case "o_blue":
					bcol = "#579AE9";
					break;
				case "o_gray":
					bcol = "#8AA2B7";
					break;
			}
			return bcol;
		
		}
		
		//元素编辑按钮开关
		function switchOptionImg(blockid,n){
			
			if(n==1){
				document.getElementById('drag_switch_img_'+blockid).style.display='';
				document.getElementById('drag_refresh_img_'+blockid).style.display='';
				document.getElementById('drag_edit_img_'+blockid).style.display='';
				document.getElementById('drag_delete_img_'+blockid).style.display='';
		//		Element.show('drag_switch_img_'+blockid);
		//		Element.show('drag_refresh_img_'+blockid);
		//		Element.show('drag_edit_img_'+blockid);
		//		Element.show('drag_delete_img_'+blockid);	
			}
			else{
				document.getElementById('drag_switch_img_'+blockid).style.display='none';
				document.getElementById('drag_refresh_img_'+blockid).style.display='none';
				document.getElementById('drag_edit_img_'+blockid).style.display='none';
				document.getElementById('drag_delete_img_'+blockid).style.display='none';	
			
			}
		
		
		}
		
		//锁定界面
		function lockWindowPage(){
			
			var widthHeight = getScreenWH();
			var screenDiv = document.createElement("div");
			screenDiv.id = "locksrceen";
			screenDiv.style.zIndex = "100";
			screenDiv.style.width = widthHeight.width;
			screenDiv.style.height = widthHeight.height;
			screenDiv.style.background = "#000";
			screenDiv.style.filter = "alpha(Opacity=20)";
			screenDiv.style.position = "absolute";
			screenDiv.style.left = "0px";
			screenDiv.style.top = "0px";
			document.body.appendChild(screenDiv);
		}
		
		//解除锁定界面
		function unlockWindowPage(){
			var screenDiv = document.getElementById("locksrceen");
			screenDiv.parentNode.removeChild(screenDiv);
		}
		
		//全屏高宽度
		function getScreenWH(){
			var objData = new Object();
			var cwidth =  document.body.clientWidth;
			var swidth =  document.body.scrollWidth;
			var cheight =  document.body.clientHeight;
			var sheight =  document.body.scrollHeight;
			objData.width = cwidth>swidth?cwidth:swidth;
			objData.height = cheight>sheight?cheight:sheight;
			return objData;
		}
		
		//设置列数
		function setLayoutType(n){
		//	alert(n);
			var objLayouttype = document.getElementById('layouttype');
			var m = objLayouttype.value;
			m = parseInt(m,10);
		//	alert(m);
			
			objLayouttype.value = n;
			if(n<m){
				var objInitText = document.getElementById('inittext');
				objInitText.innerHTML = "正在处理...";
				lockWindowPage();
		
				var logIsSure = window.confirm("修改后的列数比现有的列数少，被删除列里的模块将转到第一列！确定吗？");
				if(!logIsSure){
					objInitText.innerHTML = "";
					unlockWindowPage();
					return;
				}
			}
		
			
		
			for(i=1;i<=3;i++){
				var objLayoutnum = document.getElementById("layoutnum_"+i);
				if(i==n){
					objLayoutnum.className="layoutnumselect";
				}
				else{
					objLayoutnum.className="layoutnum";
				}
			}
		
			var objLayoutdisplay1 = document.getElementById('layoutdisplay1');
			var objLayoutdisplay2 = document.getElementById('layoutdisplay2');
			var objLayoutdisplay3 = document.getElementById('layoutdisplay3');
		//	alert("testpre:"+DragUtil.getSortIndex());
			if(n == 1){
				objLayoutdisplay1.style.display = "";
				objLayoutdisplay2.style.display = "none";
				objLayoutdisplay3.style.display = "none";
				switch(m){
					case 1:
						break;
					case 2:
						var changenum = moveColToCol1(2,2);
						var col1 = document.getElementById('col_1');
						var col2 = document.getElementById('col_2');
						var objLayout1 = document.getElementById("layout1");
						col2.parentNode.removeChild(col2);
						objLayout1.value = 99;
						col1.style.width = "99%";
						if(changenum==0)
							saveorder();
						break;
					case 3:
						var changenum = moveColToCol1(2,3);
						var col1 = document.getElementById('col_1');
						var col2 = document.getElementById('col_2');
						var col3 = document.getElementById('col_3');
						var objLayout1 = document.getElementById("layout1");
						objLayout1.value = 99;
						col2.parentNode.removeChild(col2);
						col3.parentNode.removeChild(col3);
						
						col1.style.width = "99%";
						if(changenum==0)
							saveorder();
						break;
				}
			}
		
			else if(n == 2){
				objLayoutdisplay1.style.display = "";
				objLayoutdisplay2.style.display = "";
				objLayoutdisplay3.style.display = "none";
				switch(m){
					case 1:
						var col1 = document.getElementById('col_1');
						var objLayout1 = document.getElementById("layout1");
						objLayout1.value = 59;
						col1.style.width = "59%";
		
						addCol2();
			
						var col2 = document.getElementById('col_2');
						var objLayout2 = document.getElementById("layout2");
						objLayout2.value = 40;
						col2.style.width = "40%";
		
						saveorder();
		
						break;
					case 2:
						break;
					case 3:
						var changenum = moveColToCol1(3,3);
						var col1 = document.getElementById('col_1');
						var col2 = document.getElementById('col_2');
						var col3 = document.getElementById('col_3');
						var objLayout1 = document.getElementById("layout1");
						var objLayout2 = document.getElementById("layout2");
		
						col3.parentNode.removeChild(col3);
		
						objLayout1.value = 59;
						col1.style.width = "59%";
		
						var widthcol2 = 40;
						col2.style.width = widthcol2+"%";;
						objLayout2.value = widthcol2;
						
						if(changenum==0)
							saveorder();
						break;
				}
			}
		
			else if(n == 3){
				objLayoutdisplay1.style.display = "";
				objLayoutdisplay2.style.display = "";
				objLayoutdisplay3.style.display = "";
				switch(m){
					case 1:
						var col1 = document.getElementById('col_1');
						var objLayout1 = document.getElementById("layout1");
						objLayout1.value = 30;
						col1.style.width = "30%";
						addCol2();
		
						var col2 = document.getElementById('col_2');
						var objLayout2 = document.getElementById("layout2");
						objLayout2.value = 39;
						col2.style.width = "39%";
						addCol3();
						
						saveorder();
						break;
					case 2:
						var col1 = document.getElementById('col_1');
						var objLayout1 = document.getElementById("layout1");
						objLayout1.value = 30;
						col1.style.width = "30%";
		
						var col2 = document.getElementById('col_2');
						var objLayout2 = document.getElementById("layout2");
						objLayout2.value = 39;
						col2.style.width = "39%";
						addCol3();
						var col3 = document.getElementById('col_3');
						var objLayout3 = document.getElementById("layout3");
						objLayout3.value = 30;
						col3.style.width = "30%";
						
						saveorder();
						break;
					case 3:
						break;
				}
			}
			
			var col1width = document.getElementById('layout1').value;
			var col2width = document.getElementById('layout2').value;
			var col3width = document.getElementById('layout3').value;
			//url = "operCookie.jsp";
			
			//queryString = "actionType=changelayout&layouttype="+n+"&layout1="+col1width+"&layout2="+col2width+"&layout3="+col3width;
			//changeColWidth();
			//initNewsPic();
			//new Ajax.Request(url,{method: "post",parameters : queryString});
			
			
			
		
		
		//	var col1width = document.getElementById('layout1').value;
		//	var col2width = document.getElementById('layout2').value;
		//	var col3width = document.getElementById('layout3').value;
		//	url="operCookie.jsp";
			
		///	alert ("col1:"+col1width+"col2:"+col2width+"col3"+col3width);
			
		//	queryString = "actionType=changewidth&layout1="+col1width+"&layout2="+col2width+"&layout3="+col3width;
			
		//	alert(queryString);
			
		//	new Ajax.Request(url,{method: "post",parameters : queryString});
		
			Element.hide('popupConMenuID');
			initNewsPic();
		//	alert(document.getElementById("test").innerHTML);
			//alert("test:"+DragUtil.getSortIndex());
		}
		
		//增加第二列
		function addCol2(){
			var colAry = [];
			colAry[colAry.length] =' 	<div id="col_2_hidden_div" class="drag_div no_drag"><div id="col_2_hidden_div_h"></div></div>';
		
			var col1 = document.getElementById("col_1");
		
			var col1Width = document.getElementById("layout1").value;
			var col2Width = 100-col1Width;
			document.getElementById("layout2").value = col2Width;
		
			var newColDiv = document.createElement("div");
				newColDiv.className = "col_div";
				newColDiv.id = "col_2";
				newColDiv.style.width = col2Width+"%";
				newColDiv.innerHTML = colAry.join("");
			col1.parentNode.insertBefore(newColDiv,null)
			initDrag();
		}
		
		//增加第三列
		function addCol3(){
			var colAry = [];
			colAry[colAry.length] =' 	<div id="col_3_hidden_div" class="drag_div no_drag"><div id="col_3_hidden_div_h"></div></div>';
		
			var col1 = document.getElementById("col_1");
		
			var col1Width = document.getElementById("layout1").value;
			var col2Width = document.getElementById("layout2").value;
			var col3Width = 100-col1Width-col2Width;
		
			document.getElementById("layout3").value = col3Width;
			var newColDiv = document.createElement("div");
				newColDiv.className = "col_div";
				newColDiv.id = "col_3";
				newColDiv.style.width = col3Width+"%";
				newColDiv.innerHTML = colAry.join("");
			col1.parentNode.insertBefore(newColDiv,null);
			initDrag();
		}
		
		//解析xml 
		function getXmlAndTrans(rpxml){
			var objBlock = new Object();
			blockItem = rpxml.getElementsByTagName("channel");
			mitem = blockItem.item(0).getElementsByTagName("item").item(0);
			objBlock.id = mitem.getElementsByTagName('blockid').item(0).text;
			objBlock.title = mitem.getElementsByTagName('blocktitle').item(0).text;
			objBlock.tpl = mitem.getElementsByTagName('blocktpl').item(0).text;
			objBlock.img = mitem.getElementsByTagName('blockimg').item(0).text;
			objBlock.url = mitem.getElementsByTagName('blockurl').item(0).text;
			return objBlock;
		}
		
		//内容转到第一列
		function moveColToCol1(m,n){
			//alert("m:"+m+";n:"+n);
			for(var j=m;j<=n;j++){
				var col = document.getElementById("col_"+j);
				var colChilds = col.getElementsByTagName("div");
				var changenum = 0;
				for(var i=0;i<colChilds.length;i++){
					if(colChilds[i].className == 'drag_div'){
						//alert(i+":"+colChilds[i].id+":"+colChilds[i].innerHTML);
						var blockid = colChilds[i].id.replace("drag_","");
						var url = scriptroot+"/desktop/desktopSection!showModel.action";
						queryString = "actionType=readblock&blockid="+blockid;
						changenum++;
						new Ajax.Request
						(
							url,
							{
								method: "post",	
								onSuccess : function(resp)
											{
												var objBlock = new Object();
												objBlock = getXmlAndTrans(resp.responseXML);
												var id = objBlock.id; 
												var title = objBlock.title;
												var tpl = objBlock.tpl;
												var img = objBlock.img;
												var url = objBlock.url;
												addDragDiv(id,1,1,0,tpl,img,title,m-1,url);
		//										alert("test:"+DragUtil.getSortIndex());
											},
								onFailure : function()
											{
												alert(url);
												
											},
								parameters : queryString
							}
						);	
						
					}
				}
			}
			var objInitText = document.getElementById('inittext');
			objInitText.innerHTML = "";
			unlockWindowPage();
			return changenum;
		}
		
		//数值太小
		function isThinNum(n){
			var logicv = true;
			if(n<10){
				logicv = window.confirm("输入的值偏小可能引起该模块变形！确定吗？");
			}
			return logicv;
		}
		
		//改变宽度
		function changeColWidth(){
		
			var objCol1 = document.getElementById('col_1');
			var col1width = document.getElementById('layout1').value;
			var col2width = document.getElementById('layout2').value;
			var col3width = document.getElementById('layout3').value;
		
			if(!isInteger(col1width)){
		
				document.getElementById('layout1').focus();
				document.getElementById('layout1').select();
				return;
		
			}
			if(!isThinNum(col1width)){
				document.getElementById('layout1').select();
				return;
			};
			objCol1.style.width = col1width+"%";
		
			if(document.getElementById('col_2') != null){
				var objCol2 = document.getElementById('col_2');
		
				if(!isInteger(col2width)){
		
					document.getElementById('layout2').select();
					return;
		
				}
				if(!isThinNum(col2width)){
					document.getElementById('layout2').select();
					return;
				};
				objCol2.style.width = col2width+"%";
		
			}
		
			if(document.getElementById('col_3') != null){
		
				var objCol3 = document.getElementById('col_3');
		
				if(!isInteger(col3width)){
		
					document.getElementById('layout3').select();
					return;
		
				}
		
				if(!isThinNum(col3width)){
					document.getElementById('layout3').select();
					return;
				};
				objCol3.style.width = col3width+"%";
			}
		
			//url = "operCookie.jsp";
		
			//queryString = "actionType=cw&layout1="+col1width+"&layout2="+col2width+"&layout3="+col3width;
		//	alert(queryString);
			//new Ajax.Request(url,{method: "post",parameters : queryString});
			saveorder();
			Element.hide('popupConMenuID');
			initNewsPic();
		
		}
		
		//加载列内容的面板
		function loadColEdit(){
			if(document.getElementById('coleditcon') == null){
				return;
			}
			var objDivID = document.getElementById('paneladdcontent');
			var objOtext = document.getElementById('coleditcontext');
			
			objOtext.innerHTML = "加载内容...";
			
			var saveGimgColCon = {
				onCreate: function(){
					Element.show('coleditcon');
				},
				onComplete: function() {
					if(Ajax.activeRequestCount == 0){
						Element.hide('coleditcon');
					}
				}
			};
			Ajax.Responders.register(saveGimgColCon);	
			
			url = "<%=root %>/desktop/desktopSectionsel.action";	
			queryString = "";
			new Ajax.Request
			(
				url,
				{
					method: "post",	
					onSuccess : function(resp)
								{
									
									objDivID.innerHTML = resp.responseText;
									
								},
					onFailure : function()
								{
									//alert(url);
									
								},
					parameters : queryString
				}
			);
		
		
		
		}
		
		//展开首页编辑器
		function showPanelCon(){
			var objPCM = document.getElementById('popupConMenuID');
			if(objPCM.style.display == "none"){
				closeAllItemEditor();
				Element.show('popupConMenuID');
			}
			else{
				Element.hide('popupConMenuID')	
			}
			loadColEdit();
		
		}
		
		//关闭所有元素编辑器
		function closeAllItemEditor(){
			var aryBlockId = getAllDragDiv();
			for(var i=0;i<aryBlockId.length;i++){
				var blockid = aryBlockId[i];
				var objDE = document.getElementById('drag_editor_'+blockid);
				if(objDE.style.display != "none"){
					objDE.style.display = "none";
					objDE.innerHTML = '<div id="loadeditorid_'+blockid+'" style="width:100px"><img src="<%=path%>/oa/image/desktop/loading.gif"><span id="loadeditortext_'+blockid+'" style="color:#333"></span></div>'
				}
				
			}
		}
		
		//初始化功能图标 元素内
		function initItemImg(){
			var aryBlockId = getAllDragDiv();
			for(var i=0;i<aryBlockId.length;i++){
				var blockid = aryBlockId[i];
				var objDSI = document.getElementById('drag_switch_img_'+blockid);
				var objDRI = document.getElementById('drag_refresh_img_'+blockid);
				var objDEI = document.getElementById('drag_edit_img_'+blockid);
				var objDDI = document.getElementById('drag_delete_img_'+blockid);
				objDSI.onmouseover = function (){this.style.filter=''};
				objDSI.onmouseout = function (){this.style.filter='gray()'};
				objDRI.onmouseover = function (){this.style.filter=''};
				objDRI.onmouseout = function (){this.style.filter='gray()'};
				objDEI.onmouseover = function (){this.style.filter=''};
				objDEI.onmouseout = function (){this.style.filter='gray()'};
				objDDI.onmouseover = function (){this.style.filter=''};
				objDDI.onmouseout = function (){this.style.filter='gray()'};
			}
		}
		
		//初始化功能图标 列
		function initColImg(){
				var objCPCM = document.getElementById('controlPopupConMenu');
				objCPCM.onclick = function (){
					showPanelCon();
				};
				var objDSIA = document.getElementById('drag_switch_img_all');
				objDSIA.onclick = function (){
					switchDragAll(objDSIA);
				};
		}
		
		//初始化元素内容
		function initLoadingCon(){
			var aryBlockId = getAllDragDiv();
			for(var i=0;i<aryBlockId.length;i++){
				var blockid = aryBlockId[i];
				loadDragContent(blockid);
			}
		}
		
		
		function LoadWindow(sortname,sortid,filename)
		{
			//alert(filename);
		  if(filename=='document_file_select_7'){ 			 URL="/module/document_file_select/index.php?sortid="+sortid+"&sortname="+sortname+"&filesort=2&getwhat=''";
		  }
		  if(filename=='document_file_select_8'){
			URL="/module/document_file_select/index.php?sortid="+sortid+"&sortname="+sortname+"&filesort=1&getwhat=readfile";
		  }
		  if(filename!='document_file_select_7'&&filename!='document_file_select_8'){
		  URL="/module/"+filename+"/index.php?sortid="+sortid+"&sortname="+sortname;
		  }
		  loc_x=document.body.scrollLeft+event.clientX-event.offsetX-100;
		  loc_y=document.body.scrollTop+event.clientY-event.offsetY+170;
		  window.showModalDialog(URL,self,"edge:raised;scroll:0;status:0;help:0;resizable:1;dialogWidth:320px;dialogHeight:395px;dialogTop:"+loc_y+"px;dialogLeft:"+loc_x+"px");
		}
		
		function ClearInput(sortname,sortid){
			document.getElementById(sortname).value = "";
			document.getElementById(sortid).value = "";
		}
		
		//初始化图片新闻的图片
		function initNewsPic(){
			var aryBlockId = getAllDragDiv();
			for(var i=0;i<aryBlockId.length;i++){
				var blockid = aryBlockId[i];
				var blocktype = document.getElementById('blocktypevalue_'+blockid).value;
				if(blocktype == -1){
					loadingNewsPic(-1,blockid);
				} 
			}
		}
		
		//新闻图片2
		function loadingNewsPic(newsid,blockid){
			
			var objLargeDivPic = document.getElementById('largeDivPic_'+blockid);
			var objLargeDivCon = document.getElementById('largeDivCon_'+blockid);
			var objDragTitle   = document.getElementById('drag_title_'+blockid);
			
			var width = objDragTitle.offsetWidth-16;
			url = "http://222.66.55.181:8011/general/new_mytable/news_pic.php";
			queryString = "f=pic&blockid="+blockid+"&newsid="+newsid+"&width="+width+"&height="+0;
		
			new Ajax.Request
			(
				url,
				{
					method: "post",	
					onSuccess : function(resp)
								{
									objLargeDivPic.innerHTML = resp.responseText;
									
								},
					onFailure : function()
								{
									//alert(url);
									
								},
					parameters : queryString
				}
			);
		
			url = "http://222.66.55.181:8011/general/new_mytable/news_pic.php";
			queryString = "f=txt&blockid="+blockid+"&newsid="+newsid+"&width="+width+"&height="+0;
			new Ajax.Request
			(
				url,
				{
					method: "post",	
					onSuccess : function(resp)
								{
									objLargeDivCon.innerHTML = resp.responseText;
									
								},
					onFailure : function()
								{
									//alert(url);
									
								},
					parameters : queryString
				}
			);
		
		}
		
		function saveorder(){
			var orderStr=DragUtil.getSortIndex();
			var layouttype = document.getElementById("layouttype").value;
			var layout1 = document.getElementById("layout1").value;
			var layout2 = document.getElementById("layout2").value;
			var layout3 = document.getElementById("layout3").value;
			url=scriptroot+"/desktop/desktopWhole!save.action";
			queryString="actionType=order&orderStr="+orderStr+"&wholeId=${wholeId}&layouttype="+layouttype;
			queryString+="&layout1="+layout1+"&layout2="+layout2+"&layout3="+layout3;
			new Ajax.Request
			(
				url,
				{
					method: "post",	
					onSuccess : function(resp)
								{
								},
					onFailure : function()
								{
									//alert(url);
									
								},
					parameters : queryString
				}
			);
		}
		
		
	//日程模块-万年历调用的方法
		function mouseoverToShow(date,element,evt){
			var calID = "drag_"+document.getElementById("calDeskTopID").value;
			var calbody = document.getElementById(calID);
			var contentborder = document.getElementById("contentborder");
			
			//获取日程元素在页面中的坐标
			  var x=calbody.offsetLeft;   
			  var y=calbody.offsetTop;   
			  var objParent=calbody.parentNode; 
			  while(objParent.tagName.toUpperCase()!=   "BODY"){   
				  x+=objParent.offsetLeft;   
				  y+=objParent.offsetTop;   
				  objParent = objParent.parentNode;   
			  }   
			var mosP = mouseCoords(evt);
			//alert("鼠标在模块中的坐标：\n mosP.x="+mosP.x+"\n mosP.y="+mosP.y);
	
			if((parseInt(x)+parseInt(mosP.x)+270)>parseInt(document.body.clientWidth)){
				$('detail').style.left=parseInt(x)+parseInt(mosP.x)-270;
				$('detail').style.top=parseInt(y)+parseInt(mosP.y)-parseInt(contentborder.scrollTop);
				$('detail').style.background = "url('<%=path%>/oa/image/calendar/calendar_destop_bgL.GIF') no-repeat";
				$('detailFrame').src="<%=path%>/calendar/calendar!showDesktopDetail.action?model.calStartTime="+date ;
			}else{
			    $('detail').style.left=parseInt(x)+parseInt(mosP.x);
			    $('detail').style.top=parseInt(y)+parseInt(mosP.y)-parseInt(contentborder.scrollTop);
			    $('detail').style.background = "url('<%=path%>/oa/image/calendar/calendar_destop_bgR.GIF') no-repeat";
			    $('detailFrame').src="<%=path%>/calendar/calendar!showDesktopDetail.action?model.calStartTime="+date ;
			}
			 $('detail').style.display = "";
		}
		
		function mouseCoords(ev){
			ev  = ev || window.event;
			if(ev.pageX || ev.pageY){
				return {x:ev.pageX, y:ev.pageY};
			}
			return {
				x:ev.clientX + document.body.scrollLeft - document.body.clientLeft,
				y:ev.clientY + document.body.scrollTop  - document.body.clientTop
			};
		}
		
		function resdesk(){
			var url=scriptroot+"/desktop/desktopWhole!deleteWhole.action";
			var queryString="wholeId=${wholeId}&portalId=${portalId}";
			if(confirm("您是要恢复默认桌面么？")==true){
				new Ajax.Request
				(
					url,
					{
						method: "post",	
						onSuccess : function(resp)
									{
										if(resp.responseText=="yes"){
											window.location.reload();
										}else{
											if(resp.responseText=="default"){
												alert("当前桌面为默认桌面，不能进行此操作！");
											}else{
												alert("还原操作失败！");
											}
										}
									},
						onFailure : function()
									{
										//alert(url);
										
									},
						parameters : queryString
					}
				);
			}
		}
		
		</script>

	</head>
	<body style="margin-bottom: 0;margin-top: 0;">
	<input type="hidden" id="sortTypeHidden" value="">
	<div id="detail" class="UserCard" style="display:none" >
		<table width="100%" height="100%" style="margin-left: 3px;margin-right: 3px;margin-top: 3px;">
			<tr>
				<td align="left" width="18px">
				</td>
				<td align="center" valign="bottom">
					<iframe id="detailFrame" src="" 
						width="88%" height="110px" border="0" > 
					</iframe>
				</td>
				<td width="22px"></td>
			</tr>
			<tr>
				<td></td>
				<td valign="top" align="center" >
					<a href="#" onclick="detail.style.display='none'">关闭</a>
				</td>
				<td width="22px"></td>
			</tr>
		</table>
	</div>
		<div id="contentborder" style="margin-left: 0px;" onscroll="detail.style.display='none';">
			<div style="width:200px;">
				<div style="float:left;width:100px;">
					<img style="margin-left:10px;" class="imglink" src="<%=path%>/oa/image/desktop/index_i/add.gif" title="添加修改首页内容" id="controlPopupConMenu">
					<img style="margin-left:5px;" src="<%=path%>/oa/image/desktop/index_i/open.gif" class="imglink" id="drag_switch_img_all" title="展开/隐藏所有模块">
				<!-- <img style="margin-left:5px;" src="<%=path%>/oa/image/desktop/index_i/restore.gif" class="imglink" id="restore" onclick="resdesk()" title="恢复默认桌面"> -->
				</div>
				<div style="float:left;width:100px;color:#999;height:20px;" id="inittext">
				</div>
			</div>

			<div id=test>
				<div id="col_1" class="col_div" style="width:${layout1}%">
					<s:iterator value="col1">
						<div id="drag_<s:property value="sectionId"/>" class="drag_div" style="background:#FFF;" url="<%=path%><s:property value="sectionurl"/>">
							<div style="width:100%;FILTER:progid:DXImageTransform.Microsoft.Gradient(gradientType=0,startColorStr=#<s:property value='sectionColor'/>,endColorStr=#ffffff);height:25px;" id="drag_title_<s:property value="sectionId"/>">
								<input type="hidden" name="blocktypevalue_<s:property value="sectionId"/>" id="blocktypevalue_<s:property value="sectionId"/>" value="<s:property value="sectionType"/>">
								<table width="100%" cellpadding="0" cellspacing="0" border="0">
									<tr>
										<td style="width:142px;font-weight:bold;padding:2px">
											<img src="<%=path%>/oa/image/desktop/attachment/index/<s:property value="sectionImg"/>" id="drag_img_<s:property value="sectionId"/>" class="imglink" align="absmiddle" onclick="showPopup('<s:property value="sectionId"/>')">
											<span id="drag_text_<s:property value="sectionId"/>"><s:property value="sectionName" /></span>
										</td>
										<td>
											<div id="drag_<s:property value="sectionId"/>_h" class="drag_header" style="width:100%;height:25px">
												&nbsp;
											</div>
										</td>
										<td align="right" style="width:60px;" onmousemove="switchOptionImg('<s:property value="sectionId"/>',1)" onmouseout="switchOptionImg('<s:property value="sectionId"/>',0)">
											<img src="<%=path%>/oa/image/desktop/index_i/close.gif" class="imglinkgray" id="drag_switch_img_<s:property value="sectionId"/>" onclick="switchDrag('drag_switch_<s:property value="sectionId"/>',this)" title="展开/隐藏" style="display:none">
											<img src="<%=path%>/oa/image/desktop/index_i/refresh.gif" class="imglinkgray" id="drag_refresh_img_<s:property value="sectionId"/>" onclick="resetDragContent('<s:property value="sectionId"/>');loadDragContent('<s:property value="sectionId"/>');"
												title="刷新" style="display:none">
											<img src="<%=path%>/oa/image/desktop/index_i/edit.gif" class="imglinkgray" title="编辑" onclick="modifyBlock('<s:property value="sectionId"/>')" id="drag_edit_img_<s:property value="sectionId"/>" style="display:none">
											<img src="<%=path%>/oa/image/desktop/index_i/closetab.gif" class="imglinkgray" onclick="delDragDiv('<s:property value="sectionId"/>')" title="删除" id="drag_delete_img_<s:property value="sectionId"/>" style="display:none">
										</td>
									</tr>
								</table>
							</div>
							<div id="drag_switch_<s:property value="sectionId"/>">
								<div class="drag_editor" id="drag_editor_<s:property value="sectionId"/>" style="display:none">
									<div id="loadeditorid_<s:property value="sectionId"/>" style="width:100px">
										<img src="<%=path%>/oa/image/desktop/loading.gif">
										<span id="loadeditortext_<s:property value="sectionId"/>" style="color:#333"></span>
									</div>
								</div>
								<div class="drag_content" id="drag_content_<s:property value="sectionId"/>">
									<div id="loadcontentid_<s:property value="sectionId"/>" style="width:100px">
										<img src="<%=path%>/oa/image/desktop/loading.gif">
										<span id="loadcontenttext_<s:property value="sectionId"/>" style="color:#333"></span>
									</div>
								</div>
							</div>
						</div>
						<script>loadDragContent('<s:property value="sectionId"/>');</script>
					</s:iterator>
					<div id="col_1_hidden_div" class="drag_div no_drag">
						<div id="col_1_hidden_div_h"></div>
					</div>
				</div>
				<s:if test="col2!=null&&col2.size>0">
					<div id="col_2" class="col_div" style="width:${layout2}%">
						<s:iterator value="col2">
							<div id="drag_<s:property value="sectionId"/>" class="drag_div" style="background:#FFF;" url="<%=path%><s:property value="sectionurl"/>">
								<div style="width:100%;FILTER:progid:DXImageTransform.Microsoft.Gradient(gradientType=0,startColorStr=#<s:property value='sectionColor'/>,endColorStr=#ffffff);height:25px;" id="drag_title_<s:property value="sectionId"/>">
									<input type="hidden" name="blocktypevalue_<s:property value="sectionId"/>" id="blocktypevalue_<s:property value="sectionId"/>" value="<s:property value="sectionType"/>">
									<table width="100%" cellpadding="0" cellspacing="0" border="0">
										<tr>
											<td style="width:142px;font-weight:bold;padding:2px">
												<img src="<%=path%>/oa/image/desktop/attachment/index/<s:property value="sectionImg"/>" id="drag_img_<s:property value="sectionId"/>" class="imglink" align="absmiddle" onclick="showPopup('<s:property value="sectionId"/>')">
												<span id="drag_text_<s:property value="sectionId"/>"><s:property value="sectionName" /></span>
											</td>
											<td>
												<div id="drag_<s:property value="sectionId"/>_h" class="drag_header" style="width:100%;height:25px">
													&nbsp;
												</div>
											</td>
											<td align="right" style="width:60px;" onmousemove="switchOptionImg('<s:property value="sectionId"/>',1)" onmouseout="switchOptionImg('<s:property value="sectionId"/>',0)">
												<img src="<%=path%>/oa/image/desktop/index_i/close.gif" class="imglinkgray" id="drag_switch_img_<s:property value="sectionId"/>" onclick="switchDrag('drag_switch_<s:property value="sectionId"/>',this)" title="展开/隐藏" style="display:none">
												<img src="<%=path%>/oa/image/desktop/index_i/refresh.gif" class="imglinkgray" id="drag_refresh_img_<s:property value="sectionId"/>"
													onclick="resetDragContent('<s:property value="sectionId"/>');loadDragContent('<s:property value="sectionId"/>');" title="刷新" style="display:none">
												<img src="<%=path%>/oa/image/desktop/index_i/edit.gif" class="imglinkgray" title="编辑" onclick="modifyBlock('<s:property value="sectionId"/>')" id="drag_edit_img_<s:property value="sectionId"/>" style="display:none">
												<img src="<%=path%>/oa/image/desktop/index_i/closetab.gif" class="imglinkgray" onclick="delDragDiv('<s:property value="sectionId"/>')" title="删除" id="drag_delete_img_<s:property value="sectionId"/>" style="display:none">
											</td>
										</tr>
									</table>
								</div>
								<div id="drag_switch_<s:property value="sectionId"/>">
									<div class="drag_editor" id="drag_editor_<s:property value="sectionId"/>" style="display:none">
										<div id="loadeditorid_<s:property value="sectionId"/>" style="width:100px">
											<img src="<%=path%>/oa/image/desktop/loading.gif">
											<span id="loadeditortext_<s:property value="sectionId"/>" style="color:#333"></span>
										</div>
									</div>
									<div class="drag_content" id="drag_content_<s:property value="sectionId"/>">
										<div id="loadcontentid_<s:property value="sectionId"/>" style="width:100px">
											<img src="<%=path%>/oa/image/desktop/loading.gif">
											<span id="loadcontenttext_<s:property value="sectionId"/>" style="color:#333"></span>
										</div>
									</div>
								</div>
							</div>
							<script>loadDragContent('<s:property value="sectionId"/>');</script>
						</s:iterator>
						<div id="col_2_hidden_div" class="drag_div no_drag">
							<div id="col_2_hidden_div_h"></div>
						</div>
					</div>
				</s:if>
				<s:elseif test="layouttype>=2">
					<div id="col_2" class="col_div" style="width:${layout2}%">
						<div id="col_2_hidden_div" class="drag_div no_drag">
							<div id="col_2_hidden_div_h">
							</div>
						</div>
					</div>
				</s:elseif>
				<s:if test="col3!=null&&col3.size>0">
					<div id="col_3" class="col_div" style="width:${layout3}%">
						<s:iterator value="col3">
							<div id="drag_<s:property value="sectionId"/>" class="drag_div" style="background:#FFF;" url="<%=path%><s:property value="sectionurl"/>">
								<div style="width:100%;FILTER:progid:DXImageTransform.Microsoft.Gradient(gradientType=0,startColorStr=#<s:property value='sectionColor'/>,endColorStr=#ffffff);height:25px;" id="drag_title_<s:property value="sectionId"/>">
									<input type="hidden" name="blocktypevalue_<s:property value="sectionId"/>" id="blocktypevalue_<s:property value="sectionId"/>" value="<s:property value="sectionType"/>">
									<table width="100%" cellpadding="0" cellspacing="0" border="0">
										<tr>
											<td style="width:142px;font-weight:bold;padding:2px">
												<img src="<%=path%>/oa/image/desktop/attachment/index/<s:property value="sectionImg"/>" id="drag_img_<s:property value="sectionId"/>" class="imglink" align="absmiddle" onclick="showPopup('<s:property value="sectionId"/>')">
												<span id="drag_text_<s:property value="sectionId"/>"><s:property value="sectionName" /></span>
											</td>
											<td>
												<div id="drag_<s:property value="sectionId"/>_h" class="drag_header" style="width:100%;height:25px">
													&nbsp;
												</div>
											</td>
											<td align="right" style="width:60px;" onmousemove="switchOptionImg('<s:property value="sectionId"/>',1)" onmouseout="switchOptionImg('<s:property value="sectionId"/>',0)">
												<img src="<%=path%>/oa/image/desktop/index_i/close.gif" class="imglinkgray" id="drag_switch_img_<s:property value="sectionId"/>" onclick="switchDrag('drag_switch_<s:property value="sectionId"/>',this)" title="展开/隐藏" style="display:none">
												<img src="<%=path%>/oa/image/desktop/index_i/refresh.gif" class="imglinkgray" id="drag_refresh_img_<s:property value="sectionId"/>"
													onclick="resetDragContent('<s:property value="sectionId"/>');loadDragContent('<s:property value="sectionId"/>');" title="刷新" style="display:none">
												<img src="<%=path%>/oa/image/desktop/index_i/edit.gif" class="imglinkgray" title="编辑" onclick="modifyBlock('<s:property value="sectionId"/>')" id="drag_edit_img_<s:property value="sectionId"/>" style="display:none">
												<img src="<%=path%>/oa/image/desktop/index_i/closetab.gif" class="imglinkgray" onclick="delDragDiv('<s:property value="sectionId"/>')" title="删除" id="drag_delete_img_<s:property value="sectionId"/>" style="display:none">
											</td>
										</tr>
									</table>
								</div>
								<div id="drag_switch_<s:property value="sectionId"/>">
									<div class="drag_editor" id="drag_editor_<s:property value="sectionId"/>" style="display:none">
										<div id="loadeditorid_<s:property value="sectionId"/>" style="width:100px">
											<img src="<%=path%>/oa/image/desktop/loading.gif">
											<span id="loadeditortext_<s:property value="sectionId"/>" style="color:#333"></span>
										</div>
									</div>
									<div class="drag_content" id="drag_content_<s:property value="sectionId"/>">
										<div id="loadcontentid_<s:property value="sectionId"/>" style="width:100px">
											<img src="<%=path%>/oa/image/desktop/loading.gif">
											<span id="loadcontenttext_<s:property value="sectionId"/>" style="color:#333"></span>
										</div>
									</div>
								</div>
							</div>
							<script>loadDragContent('<s:property value="sectionId"/>');</script>
						</s:iterator>
						<div id="col_3_hidden_div" class="drag_div no_drag">
							<div id="col_3_hidden_div_h"></div>
						</div>
					</div>
				</s:if>
				<s:elseif test="layouttype>=3">
					<div id="col_3" class="col_div" style="width:${layout3}%">
						<div id="col_3_hidden_div" class="drag_div no_drag">
							<div id="col_3_hidden_div_h"></div>
						</div>
					</div>
				</s:elseif>
			</div>

			<div id="popupImgMenuID" style="display:none;position:absolute">
				<div style="width:100%;padding:2px">
					<img src="<%=path%>/oa/image/desktop/index_i/delete.gif" class="imglink" onClick="Element.hide('popupImgMenuID')" title="关闭" align="right">
				</div>

				<div class="popupImgMenu" id="popupImgItem">
				</div>

				<div id="loadimgid" style="width:100px">
					<img src="<%=path%>/oa/image/desktop/loading.gif" align="absmiddle">
					<span id="loadtext" style="color:#333"></span>
				</div>

			</div>
			<input type="hidden" name="tmpblockid">
			<input type="hidden" name="layouttype" value="${layouttype}" id="layouttype">
			<input type="hidden" name="showstatus" value="1" id="showstatus">

			<div id="popupConMenuID" style="display:none;position:absolute;z-index:10;width:200px;top:20px;left:15px;border: 1px solid #A2C7D9;background-color: #F1F7F9;padding:5px">
				<div style="padding:5px;width:100%">
					<img class="imglink" align="right" src="<%=path%>/oa/image/desktop/index_i/delete.gif" onClick="Element.hide('popupConMenuID')">
				</div>
				<div style="display:inline;width:180;padding:5px;color:#222;">
					<img src="<%=path%>/oa/image/desktop/index_i/open.gif" align="absmiddle" style="cursor:hand" onClick="switchDrag('paneladdcontent',this)">
					添加首页内容
				</div>

				<div style="overflow :auto;height:300;width:100%" id="paneladdcontent" align="center">
					<div id="coleditcon" style="width:100px">
						<img src="<%=path%>/oa/image/desktop/loading.gif">
						<span id="coleditcontext" style="color:#333"></span>
					</div>
				</div>

				<div style="display:inline;width:180;padding:5px;color:#222;">
					<img src="<%=path%>/oa/image/desktop/index_i/open.gif" style="cursor:hand" align="absmiddle" onClick="switchDrag('panelmodifycol',this)">
					修改首页排版
				</div>
				<div id="panelmodifycol">
					<div style="display:inline;padding:2px;">
						<div style="width:40px;display:inline"></div>
						<s:if test="layouttype==1">
							<div id="layoutnum_1" class="layoutnumselect" onClick="setLayoutType(1)">
								1列
							</div>
							<div id="layoutnum_2" class="layoutnum" onClick="setLayoutType(2)">
								2列
							</div>
							<div id="layoutnum_3" class="layoutnum" onClick="setLayoutType(3)">
								3列
							</div>
						</s:if>
						<s:elseif test="layouttype==2">
							<div id="layoutnum_1" class="layoutnum" onClick="setLayoutType(1)">
								1列
							</div>
							<div id="layoutnum_2" class="layoutnumselect" onClick="setLayoutType(2)">
								2列
							</div>
							<div id="layoutnum_3" class="layoutnum" onClick="setLayoutType(3)">
								3列
							</div>
						</s:elseif>
						<s:else>
							<div id="layoutnum_1" class="layoutnum" onClick="setLayoutType(1)">
								1列
							</div>
							<div id="layoutnum_2" class="layoutnum" onClick="setLayoutType(2)">
								2列
							</div>
							<div id="layoutnum_3" class="layoutnumselect" onClick="setLayoutType(3)">
								3列
							</div>
						</s:else>
					</div>
					<div id="layoutdisplay0" style="width:100%;padding:5px;color:#333333;display:">
					 边框宽
						<input type="text" maxlength="3" name="layout0" style="width:30px" class="block_input" id="layout0" value="1" disabled>
						%
					</div>
					<div id="layoutdisplay1" style="width:100%;padding:5px;color:#333333;display:">
					   
						
						左列宽
						<input type="text" maxlength="3" name="layout1" style="width:30px" class="block_input" id="layout1" value="${layout1}">
						%
						<input type="button" value="确定" class="block_button" onClick="changeColWidth();">
					</div>
					<s:if test="layouttype>=2">
						<div id="layoutdisplay2" style="width:100%;padding:5px;color:#333333;display:">
							中列宽
							<input type="text" maxlength="3" name="layout2" style="width:30px" class="block_input" id="layout2" value="${layout2}">
							%
						</div>
					</s:if>
					<s:else>
						<div id="layoutdisplay2" style="width:100%;padding:5px;color:#333333;display:none;">
							中列宽
							<input type="text" maxlength="3" name="layout2" style="width:30px" class="block_input" id="layout2" value="${layout2}">
							%
						</div>
					</s:else>
					<s:if test="layouttype>=3">
						<div id="layoutdisplay3" style="width:100%;padding:5px;color:#333333;display:">
							右列宽
							<input type="text" maxlength="3" name="layout2" style="width:30px" class="block_input" id="layout3" value="${layout3}">
							%
						</div>
					</s:if>
					<s:else>
						<div id="layoutdisplay3" style="width:100%;padding:5px;color:#333333;display:none;">
							右列宽
							<input type="text" maxlength="3" name="layout2" style="width:30px" class="block_input" id="layout3" value="${layout3}">
							%
						</div>
					</s:else>
				</div>
			</div>
		</div>
		<script type="text/javascript">
			window.onload = function (){
				initDrag();
				initItemImg();
				initColImg();
				
				//initLoadingCon();
				//initNewsPic();
			}
		</script>
		
		<%--调色板 --%>
		<script>
		var ColorHex = new Array('00', '33', '66', '99', 'CC', 'FF')
		var SpColorHex = new Array('FF0000', '00FF00', '0000FF', 'FFFF00', '00FFFF',
				'FF00FF')
		var current = null
		var idColor;

		function intocolor(ssid) {
			idColor = ssid;
			var colorTable = ''
			for (i = 0; i < 2; i++) {
				for (j = 0; j < 6; j++) {
					colorTable = colorTable + '<tr height=12>'
					colorTable = colorTable + '<td width=11 style="background-color:#000000">'

					if (i == 0) {
						colorTable = colorTable
								+ '<td width=11 style="background-color:#'
								+ ColorHex[j] + ColorHex[j] + ColorHex[j] + '">'
					} else {
						colorTable = colorTable
								+ '<td width=11 style="background-color:#'
								+ SpColorHex[j] + '">'
					}

					colorTable = colorTable + '<td width=11 style="background-color:#000000">'
					for (k = 0; k < 3; k++) {
						for (l = 0; l < 6; l++) {
							colorTable = colorTable
									+ '<td width=11 style="background-color:#'
									+ ColorHex[k + i * 3] + ColorHex[l] + ColorHex[j]
									+ '">'
						}
					}
				}
			}
			colorTable = '<table width=253 border="0" id="coll1" cellspacing="0" cellpadding="0" style="border:1px #000000 solid;border-bottom:none;border-collapse: collapse" bordercolor="000000">'
					+ '<tr height=30><td colspan=21 bgcolor=#cccccc>'
					+ '<table cellpadding="0" cellspacing="1" border="0" style="border-collapse: collapse">'
					+ '<tr><td width="3"><td><input type="text" name="DisColor" size="6" disabled style="border:solid 1px #000000;background-color:#ffff00"></td>'
					+ '<td width="3"><td><input type="text" name="HexColor" size="7" style="border:inset 1px;font-family:Arial;" value="#000000"></td></tr></table></td></table>'
					+ '<table border="1"  id="coll2" cellspacing="0" cellpadding="0" style="border-collapse: collapse" bordercolor="000000" onmouseover="doOver()" onmouseout="doOut()" onclick="doclick()" style="cursor:hand;">'
					+ colorTable + '</table>';
			colorpanel.innerHTML = colorTable
		}

		function doOver() {
			if ((event.srcElement.tagName == "TD") && (current != event.srcElement)) {
				if (current != null) {
					current.style.backgroundColor = current._background
				}
				event.srcElement._background = event.srcElement.style.backgroundColor
				DisColor.style.backgroundColor = event.srcElement.style.backgroundColor
				HexColor.value = event.srcElement.style.backgroundColor
				event.srcElement.style.backgroundColor = "white"
				current = event.srcElement
			}
		}

		function doOut() {

			if (current != null)
				current.style.backgroundColor = current._background
		}

		function doclick() {
			if (event.srcElement.tagName == "TD") {
				//alert("选取颜色: " + event.srcElement._background)
				//var objDrag = document.getElementById("drag_"+idColor);
				//objDrag.style.background = event.srcElement._background;
				var tpl=event.srcElement._background;
				document.getElementById("coll1").style.display="none";
				document.getElementById("coll2").style.display="none";
				document.getElementById("showColors").style.background=tpl;
				//document.getElementById("drag_title_"+idColor).style = "width:100%;FILTER:progid:DXImageTransform.Microsoft.Gradient(gradientType=0,startColorStr=#FFFFFF,endColorStr=#ffffff);height:25px;";
				tpl = tpl.replace("#", "");
				switchTpl(idColor,tpl)
				return event.srcElement._background
			}
		}
		</script>
	</body>
</html>