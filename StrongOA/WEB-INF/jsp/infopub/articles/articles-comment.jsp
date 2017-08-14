<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@include file="/common/include/rootPath.jsp"%>
<%@ taglib uri="/tags/web-newdate" prefix="strong"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib uri="/tags/c.tld" prefix="c"%>
<%@ taglib uri="/tags/security" prefix="security"%>
<html>
	<head>
		<title>查看评论</title>
		<%@include file="/common/include/meta.jsp"%>
		<LINK  type=text/css rel=stylesheet href="<%=frameroot%>/css/properties_windows.css" >
		<LINK href="<%=path%>/common/js/tabpane/css/luna/tab.css" type=text/css rel=stylesheet>
		<script language="javascript" src="<%=path%>/common/js/tabpane/js/tabpane.js"></script>
		<LINK href="<%=path %>/common/frame/css/properties_windows.css" type=text/css rel=stylesheet>
		<script type="text/javascript" language="javascript" src="<%=jsroot%>/jquery/jquery-1.2.6.js"></script>
		<script type="text/javascript" src="<%=jsroot%>/validate/jquery.validate.js"></script>
		<script type="text/javascript" src="<%=jsroot%>/validate/formvalidate.js"></script>
		<script type="text/javascript" src="<%=jsroot%>/common/common.js"></script>
		<script type="text/javascript" language="javascript" src="<%=jsroot%>/upload/jquery.MultiFile.js"></script>
		<script type="text/javascript" language="javascript" src="<%=jsroot%>/upload/jquery.blockUI.js"></script>
		<script language="JavaScript" src="<%=path%>/common/js/commontab/service.js"></script>
		<script language="JavaScript" src="<%=path%>/common/js/commontab/workservice.js"></script>
		<script type="text/javascript">
			function onsubmit(){
				if(document.getElementById("commentName").value == ''){
					alert("主标题不能为空！");
					document.getElementById("commentName").focus();
			  		return;
				}
				document.getElementById("artsave").submit();
				//var commentName = document.getElementById("commentName").value;
				//var commentText = document.getElementById("commentText").value;
				//alert(document.getElementById("articlesId").value)commentName
				//var articlesId = document.getElementById("articlesId").value;
				//var columnArticleId = document.getElementById("columnArticleId").value;
				//location = '<%=path%>/infopub/articles/articles!comments.action?commentName='+commentName+'&articlesId='+articlesId+'&commentText='+commentText+'&columnArticleId='+columnArticleId;
			}
			function delcomment(id,arid){
		
			 window.location.href="<%=root%>/infopub/articles/articles!deleteComment.action?commentid="+id+"&articlesId="+arid;
			
			}
		</script>
	</head>
	<base target="_self"/>
	<body class=contentbodymargin oncontextmenu="return false;" >
	<script type="text/javascript" language="javascript" src="<%=jsroot%>/newdate/WdatePicker.js"></script>
<DIV id=contentborder align=center>
    <table width="100%" border="0" cellspacing="0" cellpadding="0" style="vertical-align: top;">
  <tr>
    <td height="100%">
    <table width="100%" border="0" cellspacing="0" cellpadding="0">
      <tr>
        <td height="40" style="FILTER:progid:DXImageTransform.Microsoft.Gradient(gradientType=0,startColorStr=#ededed,endColorStr=#ffffff);">
        <table width="100%" border="0" cellspacing="0" cellpadding="00">
          <tr>
            <td >&nbsp;</td>
            <td width="30%">
            	<img src="<%=frameroot%>/images/ico/ico.gif" width="9" height="9">&nbsp;
            	查看评论
            	</td>
            
            <td width="70%">
            <!--  
            <a class="Operation" href="javascript:history.go(-1);">
			  返回上一页</a> -->
            </td>
          </tr>
        </table>
        </td>
      </tr>
    </table> 
    <table width="96%" border="0" cellpadding="0" cellspacing="0" style="border:1px solid #CCCCCC;"  align="center"  >
  <tr>
    <td height="50" align="center">
    <a href="<%=root%>/infopub/articles/articles!showColumnArticl.action?columnArticleId=${columnArticleId}">
    <font size="6" color="#000000">${model.articlesTitle}
    </font></a></td>
  </tr>
  <tr>
    <td height="13" valign="top"><hr width="90%" size="1" /></td>
  </tr>

  <c:forEach items="${commentsList}" begin="${(pagenow-1)*pagesize}" 
     end="${(pagenow-1)*pagesize+pagesize-1}" var="commentsList" varStatus="status">
  <tr>
  	<td height="32" valign="top">
  	<table width="90%" border="0" cellpadding="0" cellspacing="0" style="border:1px solid #CCCCCC;" align="center">
  <tr>
    <td bgcolor="#CCFFFF">&nbsp;&nbsp;评论人:${commentsList.commentUser} &nbsp;&nbsp;&nbsp;评论时间:${commentsList.commentAddtime}</td>
  </tr>
    <tr>
    <td >&nbsp;&nbsp;${commentsList.commentTitle}</td>
  </tr>
    <tr>
    <td >&nbsp;&nbsp;&nbsp;&nbsp;${commentsList.commentContent}</td>
  </tr>
  <c:if test="${isactive=='1'}"></c:if>
  <tr><td align="right">
  <a href="javascript:delcomment('${commentsList.commentId}','${model.articlesId}')">删除</a>
  </td></tr>
</table>

<br>

	
     </td>
    </tr>
    
  </c:forEach>
    
 <tr><td align="center">
 &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
		   <a href="<%=root%>/infopub/articles/articles!getComments.action?pagenow=1&articlesId=${model.articlesId}">首页</a>&nbsp;&nbsp;&nbsp;&nbsp;
		<c:choose>
			<c:when test="${pagenow>1}">
				<a href="<%=root%>/infopub/articles/articles!getComments.action?pagenow=${pagenow-1 }&articlesId=${model.articlesId}">上一页</a>
			</c:when>
			<c:otherwise>上一页</c:otherwise>
		</c:choose>&nbsp;&nbsp;&nbsp;&nbsp;
		<c:choose>
			<c:when test="${pagenow<psize}">
				<a href="<%=root%>/infopub/articles/articles!getComments.action?pagenow=${pagenow+1 }&articlesId=${model.articlesId}">下一页</a>
			</c:when>
			<c:otherwise>下一页</c:otherwise>
		</c:choose>
	&nbsp;&nbsp;&nbsp;&nbsp;
		<a href="<%=root%>/infopub/articles/articles!getComments.action?pagenow=${psize}&articlesId=${model.articlesId}">尾页</a>
 </td></tr>
 
 
    <tr>
    <td height="13" valign="top" align="center">
    <table width="90%" border="0" cellpadding="0" cellspacing="0" style="border:1px solid #CCCCCC;" align="center" >
    <s:form id="artsave" action="/infopub/articles/articles!comments.action" theme="simple"  >
    <input type="hidden" name="columnArticleId" id="columnArticleId" value="${columnArticleId}">
    <tr>
		<td width="15%" class="biao_bg1" align="right">
			<span id="addressspan" class="wz" >评论主题(<font color="red">*</font>)：</span>
		</td>
		<td width="85%" class="td1">
			<input id="commentName" name="commentName" type="text" size="108">
		</td>
	</tr>
     <tr>
		<td width="15%" class="biao_bg1" align="right">
			<span id="addressspan" class="wz" >评论内容：</span>
		</td>
		<td width="85%" class="td1">
			<textarea id="commentText" name="commentText" rows="4" cols="68"></textarea>
    	
    	<input id="articlesId" name="articlesId" type="hidden" value="${model.articlesId}">
		</td>
	</tr>
	</s:form>
	<tr>
		<td width="15%" class="biao_bg1" align="right">
			<span id="addressspan" class="wz" ></span>
		</td>
	<td width="85%" class="td1">
	<input name="textbutton" type="button" class="input_bg" value="发表评论" onclick="onsubmit()">
	</td></tr>
    </table>
    </td>
  </tr>
  
  <tr>
    <td height="23" valign="top"><table width="90%" border="0" align="center">
      <tr>
        <td>
<!-- 
	        	<c:if test="${ca.toaInfopublishArticle.articlesIscancomment=='1' }">
	        		<div align="right"><a href="<%=root%>/infopub/articles/articles!getComments.action?articlesId=${ca.toaInfopublishArticle.articlesId}">共有${commentNum}条评论 我要发表评论</a></div>
	        		
	        	</c:if>
-->
        </td>
      </tr>
    </table></td>
  </tr>
</table>





   </td></tr></table>
		</DIV>
	</body>
</html>
