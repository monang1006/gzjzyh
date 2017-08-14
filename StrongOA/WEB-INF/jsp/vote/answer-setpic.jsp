<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@include file="/common/include/rootPath.jsp"%>
<%@ taglib prefix="c" uri="/tags/c.tld"%>
<html>
<head><title>设置图片</title></head>
<LINK href="<%=path%>/oa/css/survey/style.css" type=text/css rel=stylesheet>
<LINK href="<%=frameroot%>/css/properties_windows.css" type=text/css rel=stylesheet>
<script src="<%=path%>/oa/js/survey/prototype.js" type="text/javascript"></script>
<% 
	response.setHeader("Cache-Control","no-store");
	response.setHeader("Pragrma","no-cache");
	response.setDateHeader("Expires",0);
%>
<body style="overflow:scroll" onload="init_pic('${answer.picPath}')">
 <script language="javascript">
   var oldvalue="${answer.picPath}";

   var pic_size="${param.pic_size}";//图片尺寸
   var img_width=200;//默认值
   var img_height=300;//默认值
   if(pic_size.length>0&&pic_size.indexOf("*")>-1){
   	img_width=pic_size.substring(0,pic_size.indexOf("*"));
   	img_height=pic_size.substring(pic_size.indexOf("*")+1);
   }
   
   
   function hideEle(){
    var myele=document.getElementById("submitupload");
    var file=document.getElementById("pic").value;
    
    if(oldvalue==file){
    	//未改变，不保存
    	return false ;
    }
    
    if(file.length<1){
   	   alert("请设置图片！");
   	   return false ;
   }
//	var file_name=file.value; 
//  if(file_name!=null&&file_name.length>0) 
//  {   //图片后缀过滤
//      var file_ext =file_name.substring(file_name.lastIndexOf( ". ")+1); 
//      if(file_ext=="jpg"||file_ext=="gif") 
//      { 
//        //do nothing
//      } 
//      else 
//      { 
//          alert( "只允许上传.jpg和.gif类型图片文件! "); 
//          return; 
//      } 
//  }
    
    if(myele){
      myele.disabled=true;
      document.getElementById("img_loading").style.display="block";
    }
    return true ;
   }
   
   function showEle(picPath){
    var myele=document.getElementById("submitupload");
    if(myele){
      myele.disabled=false;
      document.getElementById("img_loading").style.display="none";
      oldvalue=picPath;//保存目前的图片值
      alert("设置成功！");
    }
   
    if(picPath!=null&&picPath.length>1){
    	document.getElementById("myimg").innerHTML=getImg(picPath);
    }else{
        document.getElementById("myimg").innerHTML="";
    }
   }
   
  function getImg(picPath){
   	//拼装img的html
     	var img_html=new Array();
        img_html.push("<img width='");
        img_html.push(img_width);
        img_html.push("px' height='");
        img_html.push(img_height);
        img_html.push("px' src='<%=root%>/images/vote/");
        img_html.push(picPath);
        img_html.push("'>");
        return img_html.join("") ;
  }
  
  function rmPic(){
  	//删除服务器的图片
   var url = "<%=root%>/vote/answer!uploadPic.action";
   var queryString = "answer.aid=${answer.aid}";
   document.getElementById("img_loading").style.display="block";
	new Ajax.Request
	(
		url,
		{
			method: "post",	
			onSuccess : function(resp)
						{
							document.getElementById("myimg").innerHTML="";
							document.getElementById("img_loading").style.display="none";
						},
				onFailure : function()
							{
								alert("操作出错，请联系管理员！");
							},
				parameters : queryString
			}
		);
  }
  
  function init_pic(picPath){
     //初始加载图片
     if(picPath.length>0){
  	 	document.getElementById("myimg").innerHTML=getImg(picPath);
  	 }
  }
 </script>
 
 <DIV id=contentborder align=center>
  <table width="100%" border="0" cellspacing="0" cellpadding="0" style="vertical-align: top;">
  <tr>
    <td height="100%">

		<table width="100%" border="0" cellspacing="0" cellpadding="0" style="FILTER:progid:DXImageTransform.Microsoft.Gradient(gradientType=0,startColorStr=#ededed,endColorStr=#ffffff);"> 
		 <tr>
		   <td height="40" >
			   <table width="100%" border="0" cellspacing="0" cellpadding="00">
				 <tr>
					<td>
					&nbsp;
					</td>
					<td width="20%">
					<img src="<%=frameroot%>/images/ico/ico.gif" width="9" height="9">&nbsp;
					设置图片
					</td>
					<td width="*">&nbsp;</td>
					<td width="5"><br><br></td>	
						</tr>
					</table>
				</td></tr>
				<tr>
				<td>
<form action="<%=root%>/vote/answer!uploadPic.action" method="post" enctype="multipart/form-data" onsubmit="return hideEle()" target="iframe_upload">
 <input type="hidden" name="answer.aid" value="${answer.aid}">
   &nbsp;&nbsp;图片：<input type="file" class="upFileBtn" onkeydown="return false"  name="pic" >
  <br><br>
  &nbsp;&nbsp;<input type="submit"  class="input_bg" id="submitupload" value="提交">&nbsp;&nbsp;&nbsp;
  <input type="button"  class="input_bg" style="width:60" onclick="rmPic()" value="撤销图片">&nbsp;&nbsp;&nbsp;
  <input type="button"  class="input_bg"  value="关闭" onclick="window.close()">
</form>
</td></tr></table>
			</tr><tr>
			<td>
			<div style="display:none" id="img_loading">处理中...<img src="<%=path%>/oa/image/survey/loading.gif"></div>
			<div id="myimg"></div>
			</td>
			</tr>
		</table>
	

</DIV>

</br>
<iframe name="iframe_upload" style="display:none"></iframe>
</body>
</html>