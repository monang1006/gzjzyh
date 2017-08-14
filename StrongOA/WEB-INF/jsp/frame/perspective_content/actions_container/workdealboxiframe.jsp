<%@ page contentType="text/html; charset=utf-8"%>
<%@include file="/common/include/rootPath.jsp"%>
<HTML>
	<HEAD>
		<title>无标题文档</title>

		<META HTTP-EQUIV="Pragma" CONTENT="no-cache">
		<META HTTP-EQUIV="author" CONTENT="zhoujx">
		<TITLE>系统工作区</TITLE>
		<link rel="stylesheet"
			href="<%=frameroot%>/css/tab/properties_toolbar.css" type="text/css">
		<link rel="stylesheet" href="<%=frameroot%>/css/tab/style.css"
			type="text/css">
		<script type="text/javascript"
			src="<%=root%>/oa/js/desktop/prototype.js"></script>
		<style id="popupmanager">
		a:link,a:visited,a:hover,a:active{
			text-decoration:none;
		}
.logo{
	font-family: "黑体";
	font-size: 32px;
	font-weight: bold;
	color: #FFFFFF;}
	
.popupMenu {
	width: 100px;
	border: 1px solid #666666;
	background-color: #F9F8F7;
	padding: 1px;
}

.popupMenuTable { /*
	background-image: url(/images/popup/bg_menu.gif);
	*/
	background-repeat: repeat-y;
}

.popupMenuTable TD {
	font-family: MS Shell Dlg;
	font-size: 12px;
	cursor: default;
}

.popupMenuRow {
	height: 21px;
	padding: 1px;
}

.popupMenuRowHover {
	height: 21px;
	border: 1px solid #0A246A;
	background-color: #B6BDD2;
}

.popupMenuSep {
	background-color: #A6A6A6;
	height: 1px;
	width: expression(parentElement . offsetWidth-27);
	position: relative;
	left: 28;
}
</style>
		<STYLE type=text/css>
.tab {
	BORDER-RIGHT: gray 1px solid;
	BORDER-TOP: gray 1px solid;
	BORDER-LEFT: gray 1px solid;
	CURSOR: hand;
	COLOR: #000000;
	BORDER-BOTTOM: #ffffff 1px solid;
	BACKGROUND-COLOR: #eeeeee;
	font-size: 10pt;
	font-weight: 600;
}

.tab_sel {
	BORDER-RIGHT: gray 2px solid;
	background: url("<%=frameroot%>/images/tab/images/perspective_leftside/qh1_2.jpg");
	BORDER-TOP: gray 1px solid;
	BORDER-LEFT: #DDDDDD 1px solid;
	CURSOR: hand;
	COLOR: #000000;
	BACKGROUND-COLOR: #d4d0c8;
	font-size: 10pt;
	font-weight: 600;
}

.pane {
	display: block
}

.pane_hide {
	display: none
}
</STYLE>
	</head>

	<body class=toolbarbodymargin>
		<table id="workdealTable" border="0" cellpadding="0" cellspacing="0">
		<tr><td>
		<div id="boxtitle">
				我的流程
				<span class="close"><img style="cursor: hand"
						src="<%=frameroot%>/images/perspective_right/small_ico_10.jpg"
						onclick="showWorkDeal()" /> </span>
			</div>
			<div class="s_box_title01">
				待办事宜
				<span class="close"><img
						src="<%=frameroot%>/images/perspective_right/small_ico_19.jpg"
						onclick="showContent(this,'zbgzcontent')" /> </span>
			</div>
			<div class="content" type="1" id="zbgzcontent">
				<script type="text/javascript">
								var contentdiv = document.getElementById("zbgzcontent");
								var myAjax = new Ajax.Request(
						             '<%=path%>/senddoc/sendDoc!showDesktop.action', // 请求的URL
						            {
						                //参数
						                parameters : 'showNum=25&subLength=15',
						                method:  'post', 
						                // 指定请求成功完成时需要执行的js方法
						                onComplete: function(response){
						                	contentdiv.innerHTML=response.responseText;
						                }
						            }
						        );
							</script>
			</div>
			<!-- <div class="s_box_title02">
				待办工作
				<span class="close"><img
						src="<%=frameroot%>/images/perspective_right/small_ico_26.jpg"
						onclick="showContent(this,'dbgzcontent')" /> </span>
			</div>
			<div id="dbgzcontent" type="1" class="content" style="display: none;"></div>
			<div class="s_box_title03">
				主办工作
				<span class="close"><img
						src="<%=frameroot%>/images/perspective_right/small_ico_26.jpg"
						onclick="showContent(this,'zubgzcontent')" /> </span>
			</div>
			<div id="zubgzcontent" type="2" class="content"
				style="display: none;"></div>
			<div class="s_box_title04">
				已办工作
				<span class="close"><img
						src="<%=frameroot%>/images/perspective_right/small_ico_26.jpg"
						onclick="showContent(this,'ybgzcontent')" /> </span>
			</div>-->
			<div id="ybgzcontent" type="3" class="content" style="display: none;"></div>
		</div>
		</td>
		</tr>
		</table>
		<SCRIPT>
	function showWorkDeal(){
		var objPCM = window.parent.document.getElementById('workdealbox');
			if(objPCM.style.display == "none"){
				//window.parent.Element.show('workdealbox');
				//window.parent.Element.hide('linkmanbox');
				window.parent.document.getElementById('workdealbox').style.display = "block";
				window.parent.document.getElementById('linkmanbox').style.display = "none";
			}
			else{
				//window.parent.Element.hide('workdealbox');	
				window.parent.document.getElementById('workdealbox').style.display = "none";
			}
	}
	function showLinkMan(){
		var objPCM = window.parent.document.getElementById('linkmanbox');
			if(objPCM.style.display == "none"){
				//window.parent.Element.show('linkmanbox');
				//window.parent.Element.hide('workdealbox');
				window.parent.document.getElementById('workdealbox').style.display = "none";
				window.parent.document.getElementById('linkmanbox').style.display = "block";
			}
			else{
				//window.parent.Element.hide('linkmanbox');	
				window.parent.document.getElementById('linkmanbox').style.display = "none";
			}
	}
	function showContent(obj,ids){
		var contentdiv = document.getElementById(ids);
		
		if(contentdiv.style.display == "none"){
			contentdiv.style.display="";
			obj.src=frameroot+"/images/perspective_right/small_ico_19.jpg";
			loadWorkInfo(contentdiv,contentdiv.type);
		}else{
			contentdiv.style.display="none";
			obj.src=frameroot+"/images/perspective_right/small_ico_26.jpg";
		}
		var x = Math.max(document.getElementById("workdealTable").offsetHeight,document.getElementById("workdealTable").scrollHeight);
		var workdealframeobj = window.parent.document.getElementById("workdealframe");
		if(x>=workdealframeobj.height-20)
			window.parent.changeWorkdealBoxWidth("220");
		else
			window.parent.changeWorkdealBoxWidth("205");
	}
	
	function loadWorkInfo(contentdiv,listMode){
		var myAjax = new Ajax.Request(
             '<%=path%>/senddoc/sendDoc!showDesktop.action', // 请求的URL
            {
                //参数
                parameters : 'showNum=25&subLength=15',
                method:  'post', 
                // 指定请求成功完成时需要执行的js方法
                onComplete: function(response){
                	contentdiv.innerHTML=response.responseText;
                }
            }
        );
	}
	//显示个人通讯录
	function showPerAddress(){
		peradd_left.className="bg1_left";
		peradd_mid.className="bg1_mid";
		peradd_right.className="bg1_right";
		sysadd_left.className="bg2_left";
		sysadd_mid.className="bg2_mid";
		sysadd_right.className="bg2_right";
		
		var contact = document.getElementById("contact");
		if(contact.src != "<%=root%>/address/addressGroup!systree.action"){
			contact.src = "<%=root%>/address/addressGroup!systree.action";	
		}
	}
	 //回车搜索
	   function startSearch(obj, evt) {
	   		//判断是否是回车
			var keyCode = event.keyCode ? event.keyCode : event.which ? event.which : event.charCode;
			//var userName = encodeURI(encodeURI(obj.value));
			if (keyCode == 13) {				
				doSearch(obj.value);
			}	   		
	   }
	   function doSearch(userName){
	   if(userName==""|userName==null|userName=="搜索联系人..."){
	   		return ;
	   }
	   userName = encodeURI(encodeURI(userName));
	    /*var contentdiv = document.getElementById("searchResult");
	   	var myAjax = new Ajax.Request(
             '<%=path%>/address/addressOrg!searchUser.action', // 请求的URL
            {
                //参数
                parameters : 'userName='+userName,
                method:  'post', 
                // 指定请求成功完成时需要执行的js方法
                onComplete: function(response){
                	contentdiv.innerHTML=response.responseText;
                }
            }
        );*/
        var contact = document.getElementById("contact");
        var searchType = "";
        if(peradd_left.className=="bg1_left"){//在个人通讯录中搜索
        	searchType = "personal";
        }else{//在系统通讯录中搜索
        	searchType = "public";
        }
        contact.src = "<%=path%>/address/addressOrg!searchUser.action?userName="+userName+"&searchType="+searchType;
	   }
	function showSysAddress(){
		peradd_left.className="bg2_left";
		peradd_mid.className="bg2_mid";
		peradd_right.className="bg2_right";
		sysadd_left.className="bg1_left";
		sysadd_mid.className="bg1_mid";
		sysadd_right.className="bg1_right";
		
		var contact = document.getElementById("contact");
		if(contact.src != "<%=root%>/address/addressOrg!systree.action"){
			contact.src = "<%=root%>/address/addressOrg!systree.action";	
		}
	}
	
	</SCRIPT>
		<DIV id=LOADING onClick="showLoading(false)"
			style="position: absolute; top: 30%; left: 38%; display: none"
			align=center>
			<font color=#16387C><strong>正在加载，请稍候...</strong> </font>
			<br>
			<IMG src="<%=frameroot%>/images/tab/loading.gif">
		</DIV>

	</BODY>
</html>
