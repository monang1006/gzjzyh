<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/common/include/rootPath.jsp"%>
<HTML>
	<HEAD>
	<TITLE>内容</TITLE>
	<META http-equiv=Content-Type content="text/html; charset=UTF-8">
	<LINK href="<%=frameroot%>/css/status_toolbar.css" type=text/css rel=stylesheet>
	<script src="<%=path%>/common/js/jquery/jquery-1.2.6.js" type="text/javascript"></script>
	<script type="text/javascript">
		$(function(){
			$('body').bind('selectstart', function() { return false; });
		});
	</script>
	<script type="text/javascript">
		function changeFrame(){
			if(window.parent.parent.frames.container.rows=="50%,*"){
				window.parent.parent.frames.container.rows="0,*";
			}else{
				window.parent.parent.frames.container.rows="50%,*";
			}
		}
		
		function upFrame(){
			if(window.parent.parent.frames.container.rows=="50%,*"){
				window.parent.parent.frames.container.rows="0%,*";
			}else if(window.parent.parent.frames.container.rows=="95%,*"){
				window.parent.parent.frames.container.rows="50%,*";
			}
		}
		
		function downFrame(){
			if(window.parent.parent.frames.container.rows=="50%,*"){
				window.parent.parent.frames.container.rows="95%,*";
			}else if(window.parent.parent.frames.container.rows=="0%,*"){
				window.parent.parent.frames.container.rows="50%,*";
			}
		}
		
		 var state = 0 ;
		  function hidemenu(temp){
		  	if(state == 0){
		  		if(temp==1){
		  			window.parent.parent.frames.container.rows="0%,*";
					state = 1;
					document.picDown.style["display"]="none";
					document.picUp.src="<%=frameroot%>/images/jiantou3.jpg";
				//	document.picUp.title="点击打开图片";
		  		}else{
		  			window.parent.parent.frames.container.rows="97%,*";
					state = 2;
					document.picUp.style["display"]="none";
					document.picDown.src="<%=frameroot%>/images/jiantou1.jpg";
				//	document.picDown.title="点击隐藏图片";
		  		}
			}else if(state == 1){
				window.parent.parent.frames.container.rows="50%,*";
				state = 0;
				document.picDown.style["display"]="";
				document.picUp.src="<%=frameroot%>/images/jiantou1.jpg";
				document.picDown.src="<%=frameroot%>/images/jiantou3.jpg";
			//	document.pic.title="点击隐藏图片";
			}else{
				window.parent.parent.frames.container.rows="50%,*";
				state = 0;
				document.picUp.style["display"]="";
				document.picUp.src="<%=frameroot%>/images/jiantou1.jpg";
				document.picDown.src="<%=frameroot%>/images/jiantou3.jpg";
				//document.pic.title="点击打开图片";
			}
		  }
		  
	</script>
	</HEAD>
	<BODY class=toolbarbodymargin>
		<DIV id=toolbarborder>
			<DIV id=toolbar>
			  	<TABLE height=15 cellSpacing=0 cellPadding=0 width="100%" border=0 id=doubleclickcolumn vAlign=center ondblclick="" >
				  	<TBODY >
					  	<TR >
					    	<%--<TD class=text noWrap align=left width=10% height=20
					    		style="FILTER:progid:DXImageTransform.Microsoft.Gradient(gradientType=0,startColorStr=#ededed,endColorStr=#ffffff);">
					    	&nbsp;&nbsp;<img src="<%=frameroot%>/images/ico.gif" width="9" height="9">&nbsp;&nbsp;公文分发情况
							</TD>
							--%><TD class=text noWrap align=left width=90% height=15
					    		style="FILTER:progid:DXImageTransform.Microsoft.Gradient(gradientType=0,startColorStr=#ededed,endColorStr=#ffffff);">
					    		<TABLE height=20 cellSpacing=0 cellPadding=0 width="100%" border=0
					    			style="FILTER:progid:DXImageTransform.Microsoft.Gradient(gradientType=0,startColorStr=#ededed,endColorStr=#ffffff);">
								  	<TR >
								    	<TD class=text noWrap align=left width=90% height=12 valign="bottom" >
								    		 <div style="text-align:center; position:absolute; top:0; width:100%;line-height:1;">
		<a href="#" onClick="hidemenu(1);"><img style="vertical-align:top;" name=picUp src="<%=frameroot%>/images/jiantou1.jpg" border="0"  /></a>
		<a href="#" onClick="hidemenu(2);"><img style="vertical-align:top;" name=picDown src="<%=frameroot%>/images/jiantou3.jpg" border="0" /></a>
		</div>
										</TD>
									</TR><%--	
									<TR >
										<TD class=text noWrap align=left width=60% height=12 valign="top">
										<img src="<%=frameroot%>/images/jiantou_up.JPG" style="cursor: hand" width="56" height="6" onclick="upFrame()"/>
								    		<img src="<%=frameroot%>/images/jiantou_down.JPG" style="cursor: hand" width="56" height="6" onclick="downFrame()"/>
										</TD>
								  	</TR>
						  		--%></TABLE>
					    	</TD>
							
					  	</TR>
				  	</TBODY>
			  	</TABLE>
		  	</DIV>
	  	</DIV>
  	</BODY>
</HTML>
