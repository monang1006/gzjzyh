<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@include file="/common/include/rootPath.jsp"%>
<%@ taglib prefix="c" uri="/tags/c.tld"%>
<html>
<head><title>设置详情链接</title></head>
<LINK href="<%=path%>/oa/css/survey/style.css" type=text/css rel=stylesheet>
<LINK href="<%=frameroot%>/css/properties_windows.css" type=text/css rel=stylesheet>
<body>
<% 
	response.setHeader("Cache-Control","no-store");
	response.setHeader("Pragrma","no-cache");
	response.setDateHeader("Expires",0);
%>
<script type="text/javascript">
  var oldvalue="${answer.url}" ;
 
  function hideEle(){
    var url=document.getElementById("answer.url").value;

    if(url==oldvalue){
    //没有改变，不保存
      return false;
    }
    
    if(url.length>100){
      alert("超过长度限制！最多100个字符！");
      return false;
    }
       
    if(url.length>0&&!isURL(url)){
       alert("合法链接必须以[http://]开头");
       return false;
    }   
        
    var myele=document.getElementById("submitupload");
    if(myele){
      myele.disabled=true;
      document.getElementById("img_loading").style.display="block";
    }
    oldvalue=url ;//保存值
    return true ;
   }
   
   function showEle(){
    var myele=document.getElementById("submitupload");
    if(myele){
      myele.disabled=false;
      document.getElementById("img_loading").style.display="none";
      alert("详情链接设置成功！");
      window.close();
    }
   }
   
   function isURL(str_url){ 
        var strRegex = "^((https|http)?://)"  ;
        var reg=new RegExp(strRegex);  
       //re.test() 
        if (reg.test(str_url)){ 
            return (true);  
         }else{  
            return (false);  
         } 
     } 
</script>

<DIV id=contentborder align=center>
  <table width="100%" border="0" cellspacing="0" cellpadding="0" style="vertical-align: top;">
  <tr>
    <td height="100%">
		<table width="100%" border="0" cellspacing="0" cellpadding="0"  style="FILTER:progid:DXImageTransform.Microsoft.Gradient(gradientType=0,startColorStr=#ededed,endColorStr=#ffffff);"> 
		 <tr>
		   <td height="40">
			   <table width="100%" border="0" cellspacing="0" cellpadding="00">
				 <tr>
					<td>
					&nbsp;
					</td>
					<td width="20%">
					<img src="<%=frameroot%>/images/ico/ico.gif" width="9" height="9">&nbsp;
					详情链接设置
					</td>
					<td width="*">&nbsp;</td>
					<td width="5"></td>	
				
						</tr>
					</table>
				</td>
			</tr>
			<tr>
			  <td>
			  <form action="<%=root%>/vote/answer!updateUrl.action" method="post" onsubmit="return hideEle()" target="iframe_upload">
  <input type="hidden" name="answer.aid" value="${answer.aid}">
  &nbsp;&nbsp;详情链接地址:<br>&nbsp;&nbsp;&nbsp;<input type="text" value="${answer.url}" style="width:340px" maxlength="100" name="answer.url" >
  <br><br>
  &nbsp;&nbsp;<input type="submit" id="submitupload" class="input_bg" value="提交">&nbsp;&nbsp;
  <input type="reset" id="submitupload" class="input_bg" value="重置">&nbsp;&nbsp;
  <input type="button" class="input_bg" value="关闭" onclick="window.close()">
</form>
			  </td>
			</tr>
		</table>
</DIV>

<div style="display:none" id="img_loading">处理中...<img src="<%=path%>/oa/image/survey/loading.gif"></div>
<iframe name="iframe_upload" style="display:none"></iframe>
</body>
</html>

