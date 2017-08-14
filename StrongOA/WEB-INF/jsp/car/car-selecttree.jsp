<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@taglib prefix="s" uri="/struts-tags" %>
<%@include file="/common/include/rootPath.jsp" %>
<% 
	response.setHeader("Cache-Control","no-store");
	response.setHeader("Pragrma","no-cache");
	response.setDateHeader("Expires",0);
%>
<html>
<head>
<title>分享车辆树</title>
<%@include file="/common/include/meta.jsp" %>
<LINK href="<%=path%>/common/css/treeview.css" type=text/css rel=stylesheet>
<style>
#contentborder {
	BORDER-RIGHT: #848284 1px solid;
	PADDING-RIGHT: 1px;
	BORDER-TOP: #848284 0px solid;
	PADDING-LEFT: 1px;
	BACKGROUND: white;
	PADDING-BOTTOM: 1px;
	OVERFLOW: auto;
	BORDER-LEFT: #848284 1px solid;
	WIDTH: 100%;
	PADDING-TOP: 1px;
	BORDER-BOTTOM: #848284 1px solid;
	POSITION: absolute;
	HEIGHT: 100%
}
body
{
	font: normal 12px arial, tahoma, helvetica, sans-serif;
	margin:0;
	padding:0px;
}
.simpleTree
{
	
	margin:0;
	padding:0;
	/*
	overflow:auto;
	width: 250px;
	height:350px;
	overflow:auto;
	border: 1px solid #444444;
	*/
}
.simpleTree li
{
	list-style: none;
	margin:0;
	padding:0 0 0 34px;
	line-height: 14px;
}
.simpleTree li span
{
	display:inline;
	clear: left;
	white-space: nowrap;
}
.simpleTree ul
{
	margin:0; 
	padding:0;
}
.simpleTree .root
{
	margin-left:-16px;
	font: normal 11px arial, tahoma, helvetica, sans-serif;
	background: url(<%=path %>/oa/image/mymail/root.gif) no-repeat 16px 0 #ffffff;
}
.simpleTree .line
{
	margin:0 0 0 -16px;
	padding:0;
	line-height: 3px;
	height:3px;
	font-size:3px;
	background: url(<%=path %>/oa/image/mymail/line_bg.gif) 0 0 no-repeat transparent;
}
.simpleTree .line-last
{
	margin:0 0 0 -16px;
	padding:0;
	line-height: 3px;
	height:3px;
	font-size:3px;
	background: url(<%=path %>/oa/image/mymail/spacer.gif) 0 0 no-repeat transparent;
}
.simpleTree .line-over
{
	margin:0 0 0 -16px;
	padding:0;
	line-height: 3px;
	height:3px;
	font-size:3px;
	background: url(<%=path %>/oa/image/mymail/line_bg_over.gif) 0 0 no-repeat transparent;
}
.simpleTree .line-over-last
{
	margin:0 0 0 -16px;
	padding:0;
	line-height: 3px;
	height:3px;
	font-size:3px;
	background: url(<%=path %>/oa/image/mymail/line_bg_over_last.gif) 0 0 no-repeat transparent;
}
.simpleTree .folder-open
{
	margin-left:-16px;
	background: url(<%=path %>/oa/image/mymail/collapsable.gif) 0 -2px no-repeat #fff;
}
.simpleTree .folder-open-last
{
	margin-left:-16px;
	background: url(<%=path %>/oa/image/mymail/collapsable-last.gif) 0 -2px no-repeat #fff;
}
.simpleTree .folder-close
{
	margin-left:-16px;
	background: url(<%=path %>/oa/image/mymail/expandable.gif) 0 -2px no-repeat #fff;
}
.simpleTree .folder-close-last
{
	margin-left:-16px;
	background: url(<%=path %>/oa/image/mymail/expandable-last.gif) 0 -2px no-repeat #fff;
}
.simpleTree .doc
{
	margin-left:-16px;
	background: url(<%=path %>/oa/image/mymail/leaf.gif) 0 -1px no-repeat #fff;
}
.simpleTree .doc-last
{
	margin-left:-16px;
	background: url(<%=path %>/oa/image/mymail/leaf-last.gif) 0 -1px no-repeat #fff;
}
.simpleTree .ajax
{
	background: url(<%=path %>/oa/image/mymail/spinner.gif) no-repeat 0 0 #ffffff;
	height: 16px;
	display:none;
}
.simpleTree .ajax li
{
	display:none;
	margin:0; 
	padding:0;
}
.simpleTree .trigger
{
	display:inline;
	margin-left:-32px;
	width: 28px;
	height: 11px;
	cursor:pointer;
}
.simpleTree .text
{
	font: normal 11px arial, tahoma, helvetica, sans-serif;
	cursor: hand;
}
.simpleTree .active
{
	font: bold;
	cursor:hand;
	background-color:#F7BE77;
}
#drag_container
{
	background:#ffffff;
	color:#000;
	font: normal 11px arial, tahoma, helvetica, sans-serif;
	border: 1px dashed #767676;
}
#drag_container ul
{
	list-style: none;
	padding:0;
	margin:0;
}

#drag_container li
{
	list-style: none;
	background-color:#ffffff;
	line-height:18px;
	white-space: nowrap;
	padding:1px 1px 0px 16px;
	margin:0;
}
#drag_container li span
{
	padding:0;
}
#drag_container li.doc, #drag_container li.doc-last
{
	background: url(<%=path %>/oa/image/mymail/leaf.gif) no-repeat -17px 0 #ffffff;
}
#drag_container .folder-close, #drag_container .folder-close-last
{
	background: url(<%=path %>/oa/image/mymail/expandable.gif) no-repeat -17px 0 #ffffff;
}

#drag_container .folder-open, #drag_container .folder-open-last
{
	background: url(<%=path %>/oa/image/mymail/collapsable.gif) no-repeat -17px 0 #ffffff;
}
.contextMenu
{
	display:none;
}
.node{
	cursor: hand;
}
div.detailCard {
			position: absolute;
			z-index: 999;
			width: 200px;
			height: 140px;
			FILTER:progid:DXImageTransform.Microsoft.Gradient(gradientType=1,startColorStr=#99cccc,endColorStr=#ffffff);
		}
	
div.detailCon {
	margin: 5px;
	}
</style>
<script type="text/javascript" src="<%=path %>/oa/js/mymail/jquery.js"></script>
<script type="text/javascript" src="<%=path %>/oa/js/mymail/jquery.contextmenu.r2.js"></script>
<script type="text/javascript" src="<%=path %>/oa/js/meetingroom/jquerysimpletree_addmouseover.js"></script>
<script src="<%=path%>/common/js/common/common.js" type="text/javascript"></script>
<script type="text/javascript">
var simpleTreeCollection;
$(document).ready(function(){
	simpleTreeCollection = $('.simpleTree').simpleTree({
		drag:false,
		autoclose: false,
		afterClick:function(node){
			var carId = node.get(0).id;
			$('#detail').css("display","none"); 
			$('#daycontent').attr("src","<%=path%>/car/carApply!selectCalender.action?carId="+carId);
			
		},
		afterDblClick:function(node){
		},
		afterMove:function(destination, source, pos){
		},
		afterAjax:function()
		{
		},
		afterContextMenu:function(node){
			//alert("右键："+node.get(0).id);
			var obj = node.get(0);
			var x=obj.offsetLeft+node.width()/2;   
			var y=obj.offsetTop+node.height();  

         $.ajax({ url: "<%=root%>/car/car!judgeImg.action",
             type: "post",
             dataType: "text",
             data: "carId="+node.get(0).id,
             success: function(message){			     
             	   var str = message.split("&");
				   eval("var imgisnull = " + str[0]);
				   eval("var statemap = " + str[1]);
				   var carState = $('span:eq(2)',node).html();
				   carState = statemap[carState]
				   
		        if (imgisnull=="0"){
						$('#detail').html("<div class=\"detailCon\" onclick=\"hideDetail()\">"
			    	   + "<TABLE CELLPADDING=\"0\" CELLSPACING=\"0\" width=\"190\" height=\"5\" BORDER=\"0\"><TR><TD STYLE=\"padding-top:2px;padding-bottom:2px;font:8pt Arial,Verdana,Tahoma;color:#FFF8F0\"></TD><td align=\"right\" STYLE=\"padding-top:2px;padding-bottom:2px;font:9pt Arial,Verdana,Tahoma;color:#FFF8F0\" title=\"关闭\"><img onclick=\"\" src=\"<%=root%>/images/ico/shanchu.gif\"/></td></TR></TABLE>"
			  	   //   +"<img src=\"<%=root%>/images/ico/shanchu.gif\">"
			   	     +$('span:eq(0)',node).html()
			   	      +"（"+carState+"）"
			         +"<br>"
			    	     +$('span:eq(1)',node).html()
						//+"<br>图片：<img src=\"<%=frameroot%>/images/ico/ico.gif\"></div>");
						+"<br>图片：<img src=\"<%=path%>/oa/image/car/nophoto.jpg\" width=\"190\" height=\"120\">"
						+"</div>");
					$('#detail').css("left",parseInt(x)); 
			 	   $('#detail').css("top",parseInt(y)); 
			 	   $('#detail').css("display",""); 
			 	   //document.getElementsByName()
			}else{
			     $('#detail').html("<div class=\"detailCon\" onclick=\"hideDetail()\">"
			       + "<TABLE CELLPADDING=\"0\" CELLSPACING=\"0\" width=\"190\" height=\"5\" BORDER=\"0\"><TR><TD STYLE=\"padding-top:2px;padding-bottom:2px;font:8pt Arial,Verdana,Tahoma;color:#FFF8F0\"></TD><td align=\"right\" STYLE=\"padding-top:2px;padding-bottom:2px;font:9pt Arial,Verdana,Tahoma;color:#FFF8F0\" title=\"关闭\"><img onclick=\"\" src=\"<%=root%>/images/ico/shanchu.gif\"/></td></TR></TABLE>"
			     //   +"<img src=\"<%=root%>/images/ico/shanchu.gif\">"
			        +$('span:eq(0)',node).html()
			         +"（"+carState+"）"
			         +"<br>"
			         +$('span:eq(1)',node).html()
					//+"<br>图片：<img src=\"<%=frameroot%>/images/ico/ico.gif\"></div>");
					+"<br>图片：<img src=\"<%=path %>/car/car!viewImg.action?carId="+node.get(0).id+"\" width=\"190\" height=\"150\">"
					+"</div>");
			
					$('#detail').css("left",parseInt(x)); 
					$('#detail').css("top",parseInt(y)); 
					$('#detail').css("display",""); 
					//document.getElementsByName()
		  	   }
			}
          });
		},
		mouseOver:function(node){
		},
		animate:false
	});
});

	//隐藏DIV提示
	function hideDetail(){
		$('#detail').css("display","none"); 
		$('.active').attr('class','text');
	}
</script>
</head>
<body oncontextmenu="return false;" bgcolor="black" onclick="hideDetail();" >
<DIV id=contentborder align="left">
	<div id="detail" class="detailCard" style="display:none ;" >
	</div>

	<table align="center" width="100%" border="0" cellpadding="0" cellspacing="1" class="table1">
		<tr>
			<td width="15%" valign="top" > 
				<ul class="simpleTree">
					<li class="root" id='1'><label style="cursor: hand;" id="rootSpan">车辆</label>
						<ul id="groupTree">
					
							<s:iterator value="#request.carlist" status="statu" id="carlist">
								<li id='<s:property value="#carlist.carId"/>'>
									<span  title="点击左键查看视图，点击右键查看车辆信息">车牌号：<s:property value="#carlist.carno"/></span>
									<span style="display:none">可乘人数：<s:property value="#carlist.takenumber"/></span>
									<span style="display:none"><s:property value="#carlist.status"/></span>
								</li>
							</s:iterator>
						</ul>
						
					</li>
				</ul>
			</td>
			<td width="80%" valign="top">
					<iframe id="daycontent" src='<%=path%>/car/carApply!selectCalender.action'
						frameborder="0" scrolling="no" height="780px" width="100%" align="top" style="border-left:4px solid #CCCCCC;">				
					</iframe>
			</td>
		</tr>
	</table>
</DIV>


</body>
</html>

