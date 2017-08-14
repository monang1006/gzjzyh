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
<title>会议室树</title>
<%@include file="/common/include/meta.jsp" %>
<LINK href="<%=frameroot%>/css/treeview.css" type=text/css rel=stylesheet>
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
			var id = node.get(0).id;
			$('#detail').css("display","none"); 
			changeRoom(id);
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
			var srtHtml = "<div class=\"detailCon\" onclick=\"hideDetail()\"><table><tr><td class=\"wz\">名称："+"<a href='#' id='"+node.get(0).id+"' onclick='changeRoom(this.id);' >"+$('span:eq(0)',node).html()+"</a></td>"
					+"&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<td class=\"wz\">地点：" +$('span:eq(1)',node).html()
					+"</td></tr><tr><td class=\"wz\">可容纳人数："+$('span:eq(2)',node).html()+"&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td><td class=\"wz\">类型："+$('span:eq(5)',node).html()
					+"</td></tr><tr><td  colspan=\"2\" class=\"wz\">说明：" +$('span:eq(3)',node).html();+"<td><tr><tr><td colspan=\"2\" class=\"wz\">"
			if(""!=$('span:eq(4)',node).html()&&null!=$('span:eq(4)',node).html()){
					srtHtml = srtHtml+"图片：<img src=\"<%=path%>/meetingroom/meetingroom!viewImg.action?mrId="+obj.id+"\" width=\"350\" height=\"250\"></td></tr></table></div>";
			}else{
				srtHtml = srtHtml+"<br>图片：<br><img src=\"<%=path%>/oa/image/meetingroom/nophoto.jpg\" width=\"350\" height=\"250\"></td></tr></table></div>";
			}
			$('#detail').html(srtHtml);
			$('#detail').css("left",parseInt(x)); 
			$('#detail').css("top",parseInt(y)); 
			$('#detail').css("display",""); 
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
	
	//查看所有会议室的使用情况
	function viewAll(){
		$('#detail').css("display","none"); 
		//$('#daycontent').attr("src","<%=path%>/meetingroom/meetingroom!selectCalender.action?canEdit=${canEdit}");
		changeRoom("");
	}
	
	
	function changeRoom(mrId){
		var frame = document.frames["daycontent"];
		if("undefined"==frame.calendar|frame.calendar==undefined){
			$('#daycontent').attr("src","<%=path%>/meetingroom/meetingroom!selectCalender.action?canEdit=${canEdit}&mrId="+mrId);
		}else{
			var displayType = frame.calendar.options.displayType;
			var initDate = frame.getCalendarDate();
			$('#daycontent').attr("src","<%=path%>/meetingroom/meetingroom!selectCalender.action?canEdit=${canEdit}&mrId="+mrId+"&displayType="+displayType+"&setDate="+initDate);
		}
	}
</script>
</head>
<body oncontextmenu="return false;" bgcolor="black" onclick="hideDetail();" scroll="no">
<DIV id=contentborder align="left">
	<div id="detail" class="detailCard" style="display:none ;" >
	</div>
	<table align="center" width="100%" border="0" cellpadding="0" cellspacing="1" class="table1">
		<tr>
			<td width="15%" valign="top" > 
				<ul class="simpleTree">
					<li class="root" id='1'><label style="cursor: hand;" id="rootSpan" onclick="viewAll();">所有会议室</label>
						<ul id="groupTree">
							<s:iterator value="#request.roomlist" status="statu" id="roomlist">
								<li id='<s:property value="#roomlist.mrId"/>'>
									<span title="点击左键查看视图，点击右键查看会议室信息"><s:property value="#roomlist.mrName"/></span>
									<span style="display:none"><s:property value="#roomlist.mrLocation"/></span>
									<span style="display:none"><s:property value="#roomlist.mrPeople"/></span>
									<span style="display:none"><s:property value="#roomlist.mrRemark"/></span>
									<span style="display:none"><s:property value="#roomlist.mrImg"/></span>
									<span style="display:none"><s:property value="#roomlist.mrType"/></span>
								</li>
							</s:iterator>
						</ul>
					</li>
				</ul>
			</td>
			<td width="80%" valign="top">
					<iframe id="daycontent" src="<%=path%>/meetingroom/meetingroom!selectCalender.action?canEdit=${canEdit }"  
						frameborder="0" scrolling="no" height="780px" width="100%" align="top" style="border-left:4px solid #CCCCCC;">				
					</iframe>
			</td>
		</tr>
	</table>
</DIV>
</body>
</html>
