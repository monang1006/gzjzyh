<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ taglib uri="/tags/web-flex" prefix="webflex"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<%@ taglib uri="/tags/c.tld" prefix="c" %>
<%@include file="/common/include/rootPath.jsp"%>
<HTML>
	<HEAD>
		<TITLE>全文检索结果</TITLE>
		<%@include file="/common/include/meta.jsp"%>
		<LINK href="<%=frameroot%>/css/properties_windows.css" type=text/css rel=stylesheet>
			<script src="<%=path%>/common/js/jquery/jquery-1.2.6.js"
			type="text/javascript"></script>
		<script src="<%=path%>/common/js/validate/jquery.validate.js"
			type="text/javascript"></script>
		<script type="text/javascript" src="<%=jsroot%>/common/common.js"></script>
		<script type="text/javascript">
			function links(id,type){
				if(type=="1"){//跳转到信息发布中
					getSysConsole().navigate("<%=basePath%>/infopub/articles/articles!showColumnArticl.action?columnArticleId="+id,"信息查看");
				}
				if(type=="2"){//跳转到个人文件柜中
					location = "<%=root%>/prsnfldr/privateprsnfldr/prsnfldrFile!download.action?id="+id;
				}
				
				if(type=="3"){//跳转到档案管理中
					var Width=screen.availWidth-10;
	           	 	var Height=screen.availHeight-30;
		           	$.ajax({
             	 		type:"post",
             	 		url:"<%=path%>/archive/tempfile/tempFile!readAnnexindex.action",
             	 		data:{
						tfileAppedId:id			
					},
		          success:function(data){
		          var arr=data.split(",");
			          if(arr.length!=1){
								if(arr[1]=="doc"||arr[1]=="docx"){
									var ReturnStr=OpenWindow("<%=root%>/archive/tempfile/tempFile!readAnnex.action?tempfileId="+arr[0]+"&tfileAppedId="+id, 
                                   		Width, Height, window);
								}else{
								}			
						}else{
							alert("对不起，该附件格式被破环！");
						}
					},
					error:function(data){
									alert("对不起，操作异常"+data);
								}
		              	 	});
				 }
				if(type=="4"){//跳转到档案管理中,档案文件没有附件
					$.post("<%=path%>/archive/tempfile/tempFile!searchViewFile.action",
		           {"tempfileId":id},
		           function(data){
		           		var arr=data.split(",");
		           		if(arr.length==1){
		           			$.post("<%=path%>/archive/archivefolder/archiveFolder!searchViewFile.action",
						    		 {"fileIds":id},
						    		  function(data1){
						    		  	var arrFile=data1.split(",");
						    		 	if(arrFile==1){ return; }
						    		 	else{
						           			getSysConsole().navigate("<%=path%>/archive/archivefolder/archiveFolder!viewFile.action?fileIds="+arrFile[0]+"&searchType=file",arrFile[1]);
						    		 		
						    		 	}
						    		  });
		           		}else{
		           			getSysConsole().navigate("<%=path%>/archive/tempfile/tempFile!input.action?tempfileId="+arr[0]+"&forwardStr=view&searchType=tempfile",arr[1]);
		           		}
		           		
		           });
				}
			}
			
			function searchView(){
<%--	window.location="<%=path%>/archive/tempfile/tempFile!searchViewAppend.action?tfileAppedId=402882e0288182180128818544980003";--%>
	var tfileAppedId='402882e0288182180128818544980003';
	$.post("<%=path%>/archive/tempfile/tempFile!searchViewAppend.action",
           {"tfileAppedId":tfileAppedId},
           function(data){
           var arr=data.split(",");
         
	    if(arr.length==1){
	      alert(data);
	      return;
	    }else{
           getSysConsole().navigate("<%=path%>/archive/tempfile/tempFile!input.action?tempfileId="+arr[0]+"&forwardStr=view&searchType=tempfile",arr[1]);
	    }	
	    });
}
			
		//	function links(id,type,url){
			//	if(type=="1"){//跳转到信息发布中
			//		top.perspective_content.actions_container.personal_properties_toolbar.navigate("<%=basePath%>"+url,"信息查看");
			//	}
				
		//	}
			
			function selectCommpass(){//全文检索
	var value=document.getElementById("searchContent").value;
	var value=ltrim(value);
	if(value=="" || value=="全文检索"){
		alert("请输入检索内容");
		return false;
	}
	location = "<%=basePath%>/search/search!searchContent.action?searchContent="+encodeURIComponent(value);
}

//去掉左边空格
function ltrim(s){ 
    return s.replace( /^\s*/,""); 
} 

		</script>
</HEAD>
<BODY class=contentbodymargin oncontextmenu="return false;" >
<DIV id=contentborder align=center>
  <table width="100%" border="0" cellspacing="0" cellpadding="0" style="vertical-align: top;">
  <tr>
    <td height="100%">
    <table width="100%" border="0" cellspacing="0" cellpadding="0">
    
    	
      <tr>
        <td height="50" style="FILTER:progid:DXImageTransform.Microsoft.Gradient(gradientType=0,startColorStr=#ededed,endColorStr=#ffffff);">
        <table width="100%" border="0" cellspacing="0" cellpadding="00" >
          <tr valign="bottom">
          	<td width="50" align="right">&nbsp;
          		<img src="<%=root%>/images/ico/logo_Y.png" width="36" height="39">	
            <td width="*" align="left">
            	<font size="6" face="微软雅黑">全文检索</font>
            	<input name="searchContent" id="searchContent" type="text" size="30" style="height: 23px;font-size:16" value="${searchContent}"  onkeypress="if(event.keyCode==13) selectCommpass();">
             	<input name="su" type="button" value="全文检索" onclick="selectCommpass()" >
            </td>
          </tr>
        </table>
        </td>
      </tr>
          <tr><td>
    	<hr width="98%" size="1" />
    </td>
    </tr>
    </table>

    <table width="90%" border="0" background="0">
  <tr>
    <td>
     <c:if test="${compassPage.totalElements==0}">     <br><br>

     	<font size="2">&nbsp;&nbsp;&nbsp;&nbsp;抱歉，没有找到与“<font color="red">${searchContent}</font>”相关的信息。</font><br><br><br>
&nbsp;&nbsp;&nbsp;&nbsp;<font size="2"><B>建议： </B></font><br>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;看看输入的文字是否有误 <br>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;去掉可能不必要的字词，如“的”、“什么”等 <br>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;请换用另外的查询字词 <br>
     	
    </c:if>
    <table width="90%" border="0" background="0">
    <c:forEach items="${compassPage.elements}" var="compassList" varStatus="status">
     <tr>
		<td width="20%">
			<a href="javascript:links('${compassList.data.id}','${compassList.data.field10}')">
			<c:if test="${compassList.highlightedText['title']==null}">
				<font size="3" color="#0000FF"><U><c:out value="${compassList.data.title}" /></U></font>
			</c:if> 
			<c:if test="${compassList.highlightedText['title']!=null}">
				<font size="3" color="#0000FF"><U><c:out value="${compassList.highlightedText['title']}" escapeXml="false" /></U></font>
			</c:if>
			</a>
		</td>
	</tr>
	     <tr>
		<td width="20%">
			<c:if test="${compassList.highlightedText['articlecontent']==null}">
				<font size="2" ><c:out value="${dataList.data.articlecontent}" /></font>
			</c:if> 
			<c:if test="${compassList.highlightedText['articlecontent']!=null}">
				<font size="2" ><c:out value="${compassList.highlightedText['articlecontent']}" escapeXml="false" /></font>
			</c:if>
		</td>
	</tr>
	<tr><td>
		<font color="#488074">来源:</font>
		<c:if test="${compassList.data.field10=='1'}">
			<font color="#488074">信息发布</font>
		</c:if>
		<c:if test="${compassList.data.field10=='2'}">
			<font color="#488074">公共文件柜</font>
		</c:if>
		
		<c:if test="${compassList.data.field10=='3'}">
			<font color="#488074">档案管理</font>
		</c:if>
		<c:if test="${compassList.data.field10=='4'}">
			<font color="#488074">档案管理</font>
		</c:if>
		<c:if test="${compassList.data.createdate!=null}">
			&nbsp;&nbsp;&nbsp;&nbsp;<font color="#488074"><c:out value="${dataList.data.createdate}" /></font>
		</c:if>
	</td>
	</tr>
	<tr><td>&nbsp;</td></tr>
	</c:forEach>
	</table>
	</td>
  </tr>
</table>
<c:if test="${compassPage.totalElements!=0}">
<input type="hidden" id="searchContent" name="searchContent" value='<c:out value="${compassPage.query}" />' />
<script language="javascript">
function gotoCurrentPage(pageNo) {
var searchContent=document.getElementById("searchContent").value;
//searchContent=encodeURIComponent(searchContent).replace(/%/g,"@");
//document.strongPageNext.searchContent.value=searchContent;
if(pageNo=='1')
window.location="<%=path%>/search/search!searchContent.action?searchContent="+searchContent;
else
window.location="<%=path%>/search/search!searchContent.action?pageNo="+pageNo+"&searchContent="+searchContent;

}
</script>
					&nbsp;
					共
					<c:out value="${compassPage.lastPageNumber}" />
					页
					<c:out value="${compassPage.totalElements}" />
					条记录&nbsp;&nbsp;&nbsp;当前页:
					<c:out value="${compassPage.pageNumber}" />

					<c:if test="${compassPage.pageNumber==1}">
					首页&nbsp;&nbsp;&nbsp;
					</c:if>

					<c:if test="${compassPage.pageNumber>1}">
						<a href="#" onclick="gotoCurrentPage(1)">首页</a>&nbsp;&nbsp;&nbsp;
					</c:if>

					<c:if test="${compassPage.pageNumber==1}">
					上一页&nbsp;&nbsp;&nbsp;
					</c:if>

					<c:if test="${compassPage.pageNumber>1}">
						<a href="#"
							onclick="gotoCurrentPage(<c:out value="${compassPage.pageNumber}" />-1)">上一页</a>&nbsp;&nbsp;&nbsp;
					</c:if>

					第
					<select class="conditionSelectBox" name='pageNo'
						onChange='javascript:gotoCurrentPage(this.value)'>
						<c:if test="${compassPage.lastPageNumber>0}">
							<c:forEach var="opt" begin="1" end="${compassPage.lastPageNumber}"
								step="1">

								<c:if test="${compassPage.pageNumber==opt}">
									<option selected value='<c:out value="${opt}" />'>
										<c:out value="${opt}" />
									</option>
								</c:if>
								<c:if test="${compassPage.pageNumber!=opt}">
									<option value='<c:out value="${opt}" />'>
										<c:out value="${opt}" />
									</option>
								</c:if>

							</c:forEach>
						</c:if>


					</select>
					页

					<c:if test="${compassPage.pageNumber==compassPage.lastPageNumber}">
					下一页&nbsp;&nbsp;&nbsp;
					</c:if>
					<c:if test="${compassPage.pageNumber<compassPage.lastPageNumber}">
						<a href="#"
							onclick="gotoCurrentPage(<c:out value="${compassPage.pageNumber}" />+1)">下一页</a>&nbsp;&nbsp;&nbsp;
					</c:if>

					<c:if test="${compassPage.pageNumber==compassPage.lastPageNumber}">
					尾页&nbsp;&nbsp;&nbsp;
					</c:if>
					<c:if test="${compassPage.pageNumber<compassPage.lastPageNumber}">
						<a href="#"
							onclick="gotoCurrentPage(<c:out value="${compassPage.lastPageNumber}" />)">尾页</a>&nbsp;&nbsp;&nbsp;
					</c:if>
					</c:if>

      </td>
  </tr>
</table>
      
</DIV>
</BODY></HTML>
