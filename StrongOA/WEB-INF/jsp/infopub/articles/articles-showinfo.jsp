<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="/common/include/rootPath.jsp"%>
<%@ taglib uri="/tags/web-newdate" prefix="strong"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib uri="/tags/c.tld" prefix="c"%>
<%@ taglib uri="/tags/security" prefix="security"%>
<html>
	<head>
		<title>预览稿件</title>
		<%@include file="/common/include/meta.jsp"%>
		<LINK type=text/css rel=stylesheet
			href="<%=frameroot%>/css/properties_windows.css">
		<LINK href="<%=path%>/common/js/tabpane/css/luna/tab.css"
			type=text/css rel=stylesheet>
		<script language="javascript"
			src="<%=path%>/common/js/tabpane/js/tabpane.js"></script>
		<LINK href="<%=path%>/common/frame/css/properties_windows.css"
			type=text/css rel=stylesheet>
		<script type="text/javascript" language="javascript"
			src="<%=jsroot%>/jquery/jquery-1.2.6.js"></script>
		<script type="text/javascript"
			src="<%=jsroot%>/validate/jquery.validate.js"></script>
		<script type="text/javascript"
			src="<%=jsroot%>/validate/formvalidate.js"></script>
		<script language="javascript"
			src="<%=path%>/common/js/common/search.js"></script>
		<script type="text/javascript" src="<%=jsroot%>/common/common.js"></script>
		<script type="text/javascript" language="javascript"
			src="<%=jsroot%>/upload/jquery.MultiFile.js"></script>
		<script type="text/javascript" language="javascript"
			src="<%=jsroot%>/upload/jquery.blockUI.js"></script>
		<script language="JavaScript"
			src="<%=path%>/common/js/commontab/service.js"></script>
		<script language="JavaScript"
			src="<%=path%>/common/js/commontab/workservice.js"></script>

		<style>
/* gold */
body {
	font-size: 14px;
	margin: 0 0;
	padding: 0;
	font-family: "Lucida Grande", "Lucida Sans Unicode", "Lucida Sans",
		Verdana, Arial, sans-serif;
	color: #333;
}

div,p,li,td,dd,a {
	word-wrap: break-word;
}

small {
	font-size: 12px;
	line-height: 1.5em;
	margin: 0 0 30px;
}

h1,h2,h3 {
	font-weight: bold;
}

h1 {
	font-size: 32px;
}

h2 {
	font-size: 24px;
}

h3 {
	font-size: 24px;
}

h4 {
	font-size: 16px;
}

h5 {
	font-size: 16px;
}

h6 {
	font-size: 14px;
}

ol li {
	list-style: decimal outside;
}

blockquote {
	margin: 20px 30px 5px 20px;
	height: 1%;
	padding: 2px 0 0 40px;
	font-style: italic;
	background: url(/wp-content/themes/rexsong/quotes.gif) no-repeat left
		top;
}

blockquote cite {
	margin: 5px 0 0;
	display: block;
}

a img {
	border: none;
}

h1 {
	line-height: 1.0;
	margin-top: 0;
	font-family: 'mingliu';
}

h1 {
	padding: 0;
	margin: 0;
}

h2 {
	margin: 30px 0 0;
}

h3 {
	padding: 0;
	margin: 30px 0 0;
}

p img {
	padding: 0;
	max-width: 100%;
}

a,h2 a:hover,h3 a:hover {
	color: #06c;
}

a:hover {
	color: #147;
	text-decoration: none;
}

p,ul,ol,h4,h5 {
	clear: both;
}

/* table { table-layout:fixed; word-wrap:break-word; word-break:break-all; } */
.entry {
	margin: 25px 0 0;
	text-indent: 2em;
	padding-top:20px;
	background:url(<%=root%>/frame/theme_red/images/xian.png) repeat-x;
}

.entry p {
	font-size: 14px;
	overflow: hidden;
	margin: 0 0 20px;
	line-height: 1.5;
	font-family: "Lucida Grande", "Lucida Sans Unicode", "Lucida Sans",
		Verdana, Arial, sans-serif;
}

.entry li {
	line-height: 1.6;
}

.entry h4 {
	padding-top: 0;
}

.entry ul,.entry ol {
	margin-top: 20px;
	margin-bottom: 20px;
	padding-right: 20px;
}

.widecolumn .entry {
	line-height: 1.4em;
}

.widecolumn {
	line-height: 1.6em;
}

.header h1,.header h1 a,.header h1 a:hover,.header h1 a:visited {
	
	text-align:center;
	font-size:16px;
	font-weight:bolder;
	color:#b00400;
}

h2,h2 a,h2 a:hover,h2 a:visited,h3,h3 a,h3 a:hover,h3 a:visited,#wp-calendar caption
	{
	font-weight: bold;
}

.entry p a:visited {
	color: #b85b5a;
}

.commentlist li,#commentform input,#commentform textarea {
	font-size: 12px;
}

.commentlist cite,.commentlist cite a {
	font-weight: bold;
	font-style: normal;
	font-size: 14px;
	text-transform: capitalize;
}

.commentlist p {
	font-weight: normal;
	line-height: 1.5em;
	text-transform: none;
}

.commentmetadata {
	font-weight: normal;
}

small,.nocomments,.postmetadata,blockquote,strike {
	color: #666;
}

.header {
	padding: 0;
	margin: 20px 0 0 0;
}

.widecolumn {
	float: left;
	padding: 0 20px 20px 20px;
	margin: 0;
}

.post {
	margin: 0 0 40px;
	text-align: justify;
	clear: both;
	padding:0 15px;
	background:#fdf9f3;
	border-top:4px solid #eb5644;
	border-left:1px solid #eb5644;
	border-bottom:1px solid #eb5644;
	border-right:1px solid #eb5644;
	width:99%;
}

.post h3 {
	margin: 0 0 0;
	font-family: PMingLiU, Verdana;
}

.post hr {
	display: block;
}

.post h4 {
	margin-top: 30px;
	line-height: 1.1;
}

.post h5 {
	margin-top: 30px;
	line-height: 1.1;
}

.widecolumn .post {
	margin: 0;
}

.widecolumn .postmetadata {
	margin: 0 0 30px 0;
	
}

.widecolumn .smallattachment {
	text-align: center;
	float: left;
	margin: 5px 5px 5px 0px;
}

.postmetadata {
	font-size: 12px;
	font-family: Tahoma;
}

html>body .entry ul {
	margin-left: 40px;
	padding: 0 0 0 0;
	text-indent: -10px;
}

html>body .entry li {
	margin: 0 0 0 10px;
}

.entry
 
ul
 
li
:before
,
{
content
:
 
"
\00BB
 
\0020
";

	
}
.entry ol {
	padding: 0 0 0 40px;
	margin: 20px 0;
}

.entry ol li {
	margin: 0;
	padding: 0;
	list-style-position: outside;
}

* html .entry table {
	margin: 20px 0;
}

#commentform p {
	margin: 10px 0;
}

#commentform label {
	height: 16px;
	overflow: hidden;
	background: #;
	display: block;
}

#commentform label * {
	line-height: 1.231;
	zoom: 1;
}

#commentform #comment {
	height: 106px;
	width: 492px;
	padding: 3px 4px;
	margin: 5px 0 0;
	overflow: hidden;
	font-size: 13px;
	line-height: 17px;
}

#commentform #submit {
	margin: 5px 0 0;
	padding: 2px 0;
	font-size: 12px;
	height: 25px;
	overflow: hidden;
	font-weight: bold;
	font-family: 微软雅黑;
	line-height: 1.231;
	width: 100px;
}

#commentform #submitit {
	margin: 5px 0 0;
	padding: 2px 0;
	font-size: 12px;
	height: 25px;
	overflow: hidden;
	font-weight: bold;
	font-family: 微软雅黑;
	line-height: 1.231;
	width: 100px;
}

.alt {
	margin: 0;
	padding: 5px;
}

.commentlist {
	padding: 0;
	margin: 30px 0 0;
	text-align: justify;
}

.commentlist li {
	margin: 0 0 20px;
	padding: 0;
	list-style-type: none;
}

.commentlist p {
	margin: 10px 5px 10px 0;
}

.commentmetadata {
	margin: 0 0;
	display: block;
}

.center {
	text-align: center;
}

.contentshow {
	font-size: 16px;
}

.contentshow1 {
	font-size: 14px;
}

.contentshow2 {
	font-size: 12px;
}

.header h1 {
	line-height: 1.0;
	margin: 40px 0 2px;
	font-family: PMingLiU, Verdana;
}

.commentlist .editAuthor,.commentlist .editAuthor a {
	text-transform: capitalize;
}

#comments {
	
}

#comments h3 {
	margin-bottom: 20px;
	font-size:16px;
	
}

#comments #commentlist {
	
}

#comments #commentlist dl {
	margin: 0 0 25px;
	padding: 0;
}

#comments #commentlist dl.alt {
	
}

#comments #commentlist dt {
	float: left;
	position: relative;
	width: 65px;
	margin: 0;
	padding: 0;
}

#comments #author_image {
	position: absolute;
	z-index: 2;
	width: 48px;
	height: 48px;
}

#comments #commentlist dd {
	font-size: 12px;
	margin: 0 0 0 70px;
	padding: 0;
	height: 1%;
}

#comments #commentlist cite {
	font-size: 14px;
	font-weight: bold;
	font-style: inherit;
	text-transform: capitalize;
}
</style>
		<script type="text/javascript">
		function cancel(){
			location="articles-showcontent.jsp";
		}
	     function addmykm(id){
               var url="/infopub/articles/articles!showColumnArticl.action?columnArticleId="+id;

      //var audit= window.showModalDialog("<%=root%>/knowledge/mykm/mykm!input.action?mykmUrl="+url+"&articleId="+id,window,'help:no;status:no;scroll:no;dialogWidth:450px; dialogHeight:400px');
   //  window.location.href="<%=root%>/knowledge/mykm/mykm!input.action?mykmUrl="+url+"&articleId="+id
               window.open ("<%=root%>/knowledge/mykm/mykm!input.action?mykmUrl="+url+"&columnArticleId="+id, '', 'height=400, width=450, top=200, left=400, toolbar=no,menubar=no, scrollbars=no, resizable=no,location=no, status=no');
          }

          function commentShow(id){
               window.location.href="<%=root%>/infopub/articles/articles!getComments.action?articlesId="+id;
          }
          function addshore(id){
               var url="/infopub/articles/articles!showColumnArticl.action?columnArticleId="+id;
               var audit= window.showModalDialog("<%=root%>/knowledge/mykm/mykm!shore.action?mykmUrl="+url+"&columnArticleId="+id,window,'help:no;status:no;scroll:no;dialogWidth:450px; dialogHeight:400px');
           }

           function delComment(id,arid){
				if(confirm("确定要删除吗？")){
                	window.location.href="<%=root%>/infopub/articles/articles!deleteComment.action?commentid="+id+"&columnArticleId="+arid;
				}
           }
           function onbmit(){
				if($.trim(document.getElementById("commentText").value) == ''){
					alert("内容不能为空！");
					document.getElementById("commentText").focus();
			  		return;
				}
				document.getElementById("artsave").submit();window.history
	      }


          function fontshow(value){
               if(value=="da"){
            //   $("#contents").addClass('cotentshow');
                  //document.getElementById('contents').style.fontSize='18px';

                  try{
                  	 document.getElementById("contents").style.fontSize= "18px";
                  }catch(e){

                  }
            	 
            	  
            	  
               }else if(value=="zh"){
             //  $("#contents").addClass('cotentshow1');
                  //document.getElementById('contents').style.fontSize='16px';
                  try{
                  	 document.getElementById("contents").style.fontSize= "16px";
                  }catch(e){

                  }
               }else {
             //  $("#contents").addClass('cotentshow2');
                //document.getElementById('contents').style.fontSize='14px';
                  try{
                  	 document.getElementById("contents").style.fontSize= "14px";
                  }catch(e){

                  }
               }
          }
               
               $(document).ready(function(){
                   
               });
     /*    function del(value,id){
             $.post(
              "<%=root%>/infopub/articles/articles!deleteComment.action",
                {commentid:value,articlesId:id},
                function(date){
             //   alert(json);
              //  document.getElementById(value).style.display="none";
                  $("#"+value).css("display","none");
                  
                   var sum=$("#num").text();
                   sum=eval(Number(sum.substring(2,sum.indexOf("条")))-1);
             
                   $("#num").text("共有"+sum+"条评论");
                   $("#articlescomment").append(date); 
               
                }
         );
          }
          
        function add(id){
        var text=$("#commentText").text();
         
            $.post(
               "<%=root%>/infopub/articles/articles!comments.action",
               {commentText:text,articlesId:id},
               function(date){
               //alert("评论成功！");
               
              //  var oNewNode = document.createElement("DIV");
              //  var nowNode = document.getElementById("commentlist");              
              //   oNewNode.setAttribute("id",id);
              //   nowNode.insertBefore(oNewNode);
                
                
              // $("#"+id).append(date);
              //   $(".comment").css("display","none");
               $("#articlescomment").prepend(date); 
                var sum=$("#num").text();
               sum=eval(Number(sum.substring(2,sum.indexOf("条")))+1);
             
               $("#num").text("共有"+sum+"条评论");
               
               //控制页面显示内容
               var nu=$(".comment").size();
               if(nu>6){
                $(".comment:last").css("display","none");
                $(".comment:last").removeAttr("class"); 
               }
               
               }
               
            );
        
        }*/
           var commentLength = function()
			{
			  document.getElementById('commentText').onkeydown = function()
			  {    
			    if(this.value.length >= 200)
			      event.returnValue = false;
			  }
			}
	</script>

	</head>
	<body class="contentbodymargin">
		<div class="widecolumn" id=contentborder>
	</table>
			<div class="post">
				<div class="header">
				<table width="100%" border="0"  cellpadding="0" cellspacing="0">
				<tr><td>
					<h1 style="text-align: center">
						<s:if test="ca.toaInfopublishArticle.articlesTitlefont=='1'">
							<B> <font
								color="${ca.toaInfopublishArticle.articlesTitlecolor }">${ca.toaInfopublishArticle.articlesTitle}</font>
							</B>
						</s:if>
						<s:elseif test="ca.toaInfopublishArticle.articlesTitlefont=='2'">
							<I><font
								color="${ca.toaInfopublishArticle.articlesTitlecolor }">${ca.toaInfopublishArticle.articlesTitle}</font>
							</I>
						</s:elseif>
						<s:elseif test="ca.toaInfopublishArticle.articlesTitlefont=='3'">
							<B><I><font
									color="${ca.toaInfopublishArticle.articlesTitlecolor }">${ca.toaInfopublishArticle.articlesTitle}</font>
							</I> </B>
						</s:elseif>
						<s:else>
							<font color="${ca.toaInfopublishArticle.articlesTitlecolor }">${ca.toaInfopublishArticle.articlesTitle}</font>
						</s:else>
					</h1></td></tr><tr><td>
					<p class="postmetadata alt center" style="margin: 10px 0;">
						<small class="commentmetadata"> 时间：${latestchangtime
							}&nbsp;&nbsp;&nbsp;&nbsp;
							共点击${ca.toaInfopublishArticle.articlesHits}次&nbsp;&nbsp;&nbsp;&nbsp;

<%--							<a href="javascript:addmykm('${ca.columnArticleId}')"> [收藏]</a>--%>
<%----%>
<%--							&nbsp;-&nbsp; <a--%>
<%--							href="javascript:addshore('${ca.columnArticleId}');">[分享]</a>&nbsp;-&nbsp;--%>
							<a href="javascript:try{window.print()}catch(e){}"><font color="#666666">打印</font>
							<a href="javascript:window.parent.refreshWorkByTitle('<%=path%>/fileNameRedirectAction.action?toPage=infopub/articles/articles-showcontent.jsp','办公门户');"><font color="#666666">返回</font>
							<!-- 
						</a>&nbsp;-&nbsp; [<a href="javascript:fontshow('da')">大</a>-<a
							href="javascript:fontshow('zh')">中</a>-<a
							href="javascript:fontshow('xi')">小</a>]
						 -->	
							 </small>
					</p></td></tr></table>
				</div>
				<div class="entry">
					<table>
						<tr>
							<td>
								<div id="contents" style="font-size: 14px;margin-left:5%;margin-right:5%">
									<c:if test="${empty showContent}">
									    无内容显示
								    </c:if>
									${showContent}
								</div>
							</td>
						</tr>
						<tr>
							<td align="center">
							</td>
						</tr>
					</table>
				</div>
				<div align="center">
		<c:if test="${psize1>1}">
      &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;第${pagecontent }/${psize1 }页
  
		   <a href="<%=root%>/infopub/articles/articles!showColumnArticl.action?pagecontent=1&columnArticleId=${columnArticleId}">首页</a>&nbsp;&nbsp;&nbsp;&nbsp;
		<c:choose>
			<c:when test="${pagecontent>1}">
				<a href="<%=root%>/infopub/articles/articles!showColumnArticl.action?pagecontent=${pagecontent-1 }&columnArticleId=${columnArticleId}">上一页</a>
			</c:when>
        </c:choose>&nbsp;&nbsp;&nbsp;&nbsp;
		<c:choose>
			<c:when test="${pagecontent<psize1}">
				<a href="<%=root%>/infopub/articles/articles!showColumnArticl.action?pagecontent=${pagecontent+1 }&columnArticleId=${columnArticleId}">下一页</a>
			</c:when>
        </c:choose>
	&nbsp;&nbsp;&nbsp;&nbsp;
		<a href="<%=root%>/infopub/articles/articles!showColumnArticl.action?pagecontent=${psize1}&columnArticleId=${columnArticleId}">尾页</a>
		</c:if>

				</div>
			</div>
			<c:if test="${ca.toaInfopublishArticle.articlesIscancomment=='1' }">

				<div id="comments" align="left">
					<h3 id="num">
						共有${commentNum}条评论
					</h3>
					<div id="commentlist">
						<div id="articlescomment">
							<c:forEach items="${commentsList}"
								begin="${(pagenow-1)*pagesize}"
								end="${(pagenow-1)*pagesize+pagesize-1}" var="commentsList"
								varStatus="status">
								<div class="comment" id="${commentsList.commentId}">
									<dl class="alt">
										<dt>
											<div id="author_image">
												<img src="<%=root%>/images/ico/default.gif" width="48"
													height="48" />
											</div>
										</dt>
										<dd>
											<cite>${commentsList.commentUser}</cite>&nbsp;说:
											<p>
												${commentsList.commentContent}
											</p>
											<small class="commentmetadata">${commentsList.showTime}&nbsp;&nbsp;&nbsp;&nbsp;
												<c:if test="${isactive=='1'}">
													<security:authorize ifAllGranted="001-0004000200010014">
														<a
															href="javascript:delComment('${commentsList.commentId }','${columnArticleId}');">删除</a>&nbsp;
        
          <!--  <a href="javascript:delComment('${commentsList.commentId }','${columnArticleId}');">删除</a>&nbsp;
          -->
													</security:authorize>
												</c:if>
											</small>
										</dd>

									</dl>
								</div>
							</c:forEach>
						</div>
						<br>

						<c:if test="${psize>1}">
	       &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;第${pagenow }/${psize }页
  
		   <a
								href="<%=root%>/infopub/articles/articles!showColumnArticl.action?pagenow=1&columnArticleId=${columnArticleId}">首页</a>&nbsp;&nbsp;&nbsp;&nbsp;
		<c:choose>
								<c:when test="${pagenow>1}">
									<a
										href="<%=root%>/infopub/articles/articles!showColumnArticl.action?pagenow=${pagenow-1 }&columnArticleId=${columnArticleId}">上一页</a>
								</c:when>

							</c:choose>&nbsp;&nbsp;&nbsp;&nbsp;
		<c:choose>
								<c:when test="${pagenow<psize}">
									<a
										href="<%=root%>/infopub/articles/articles!showColumnArticl.action?pagenow=${pagenow+1 }&columnArticleId=${columnArticleId}">下一页</a>
								</c:when>
							</c:choose>
	&nbsp;&nbsp;&nbsp;&nbsp;
		<a
								href="<%=root%>/infopub/articles/articles!showColumnArticl.action?pagenow=${psize}&columnArticleId=${columnArticleId}">尾页</a>

						</c:if>
					</div>
				</div>
			</c:if>
			<c:if test="${ca.toaInfopublishArticle.articlesIscancomment=='1' }">
			<security:authorize ifAllGranted="001-0004000200010013">
				<div align="left">
					<h3 id="respond" style="font-size:16px;">
						发表评论
					</h3>
					<s:form id="artsave" action="/infopub/articles/articles!comments.action" theme="simple">
						<input type="hidden" name="columnArticleId" id="columnArticleId" value="${columnArticleId}">
						<p>
							<!--
							<textarea tabindex="1" name="comment" id="comment" cols="100%" rows="10" title="ctrl+enter提交评论"></textarea>
							-->
							<textarea id="commentText" maxlength="4000" onKeyUp="commentLength()" 
								tabindex="1" name="commentText" cols="70%" rows="10" style="overflow:hidden;"></textarea>
							<input id="articlesId" name="articlesId" type="hidden" value="${ca.toaInfopublishArticle.articlesId}">
						</p>
						<p>
						<a id="more" href="#" class="button"  onClick="onbmit()"  style="width:73px; height:24px; line-height:24px; border:none; background:url(<%=root %>/frame/theme_red/images/add_bt.jpg) no-repeat center;FONT-SIZE: 14px; FONT-FAMILY: Tahoma, Verdana, Arial, Helvetica;text-align: center;" >发表评论</a>
						</p>
					</s:form>
				</div>
			</security:authorize>
			</c:if>
	</div>
	</body>
</html>
