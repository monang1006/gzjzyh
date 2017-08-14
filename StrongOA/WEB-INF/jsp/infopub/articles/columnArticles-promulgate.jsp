<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ taglib uri="/tags/web-flex" prefix="webflex"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<%@ taglib uri="/tags/web-newdate" prefix="strong"%>
<%@include file="/common/include/rootPath.jsp"%>
<%@ taglib uri="/tags/security" prefix="security"%>
<HTML>
	<HEAD>
		<TITLE>稿件列表</TITLE>
		
		<%@include file="/common/include/meta.jsp"%>
		<link href="<%=frameroot%>/css/strongitmenu.css" type="text/css" rel="stylesheet"><!--右键菜单样式 -->
		<LINK href="<%=frameroot%>/css/properties_windows.css" type=text/css rel=stylesheet>
		<script language='javascript' src='<%=path%>/common/js/grid/ChangeWidthTable.js'></script>
				<script language="javascript" src="<%=path%>/common/js/jquery/jquery-1.2.6.js"></script>
		<script language="javascript" src="<%=path%>/common/js/common/search.js"></script>
		<script src="<%=path%>/common/js/common/common.js" type="text/javascript"></script>
		<LINK type=text/css rel=stylesheet href="<%=frameroot%>/css/search.css">
		<SCRIPT language="javascript" src="<%=path%>/common/js/menu/menu.js"></SCRIPT><!--右键菜单脚本 -->
		<script language="JavaScript" src="<%=path%>/common/js/commontab/service.js"></script>
		<script language="JavaScript" src="<%=path%>/common/js/commontab/workservice.js"></script>
		<script type="text/javascript">
		//显示文章属性
		function showimg(articlesGuidetype,articlesIshot,articlesIsstandtop,articlesIscancomment){
			var rv = '' ;
			/**
			if(articlesGuidetype == '1'){
				rv += "<font color='#CC0000'>导</font>&nbsp&nbsp";
			}
			if(articlesIshot == '1'){
				rv += "<font color='#FF0000'>热</font>&nbsp&nbsp";
			}**/
			if(articlesIsstandtop == '1'){
				rv += "<font color='#0000FF'>固顶</font>&nbsp&nbsp";
			}
			if(articlesIscancomment == '1'){
				rv += "<font color='#FF00FF'>评论</font>&nbsp&nbsp";
			}
			return rv;
		}
		</script>
</HEAD>
<BODY class=contentbodymargin oncontextmenu="return false;" onload="initMenuT()">
	<script language="javascript" type="text/javascript" src="<%=path%>/common/js/newdate/WdatePicker.js"></script>
<DIV id=contentborder align=center>
<s:form theme="simple" id="myTableForm" action="/infopub/articles/columnArticles.action">
<input type="hidden" name="columnId" id="columnId" value="${columnId}">
<input type="hidden" name="promulgate" id="promulgate" value="promulgate">
<input type="hidden" name="columnArticleId" id="columnArticleId" value="${columnArticleId}">

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
            	稿件列表
            	</td>
            <td width="70%">
            <table border="0" align="right" cellpadding="0" cellspacing="0">
                <tr>
                  <td>
                  <td width="5">&nbsp;</td>
                  <td width="*"></td>

                   <security:authorize ifAllGranted="001-0004000200010008">
                  <td width="5"></td>
                  <td >
                 
                  <a class="Operation" href="javascript:pubArticle();">
                  	<img src="<%=root%>/images/ico/fasong.gif" width="15" height="15" class="img_s">发布&nbsp;</a>
                
                  </td>
                   </security:authorize>
                     <security:authorize ifAllGranted="001-0004000200010009">
                  <td width="5"></td>
                  <td >
              
                  <a class="Operation" href="javascript:delpubArticle();">
                  	<img src="<%=root%>/images/ico/quxiao.gif" width="15" height="15" class="img_s">取消发布&nbsp;</a>
              
                  </td>
                   </security:authorize>
                
                  <td width="5"></td>
                  <td >
                  
                  <a class="Operation" href="javascript:show();">
                  	<img src="<%=root%>/images/ico/chakan.gif" width="15" height="15" class="img_s">查看&nbsp;</a>
               
                  </td>  
                 <security:authorize ifAllGranted="001-0004000200010001">
                 
                  <td width="5"></td>
                  <td >
                 
                  <a class="Operation" href="javascript:editUser();">
                  	<img src="<%=root%>/images/ico/bianji.gif" width="15" height="15" class="img_s">编辑&nbsp;</a>
               
                  </td>
                    </security:authorize>
                  <td width="5">&nbsp;</td>
                </tr>
            </table>
            </td>
          </tr>
        </table>
        </td>
      </tr>
    </table>
     <webflex:flexTable name="myTable" width="100%" height="370px" wholeCss="table1" property="articlesId" isCanDrag="true" isCanFixUpCol="true" clickColor="#A9B2CA" footShow="showCheck" getValueType="getValueByProperty" 
      collection="${page.result}" page="${page}">
		 <table width="100%" border="0" cellpadding="0" cellspacing="1" class="table1">
        <tr>
          <td width="5%" align="center"  class="biao_bg1">

          <img src="<%=root%>/images/ico/sousuo.gif" width="15" height="15" style="cursor: hand;" title="单击搜索" onclick="getListBySta();">

          </td>
          <td width="25%" align="center"  class="biao_bg1">
          	<input id="articlesTitle" name="articlesTitle"
			type="text" style="width:100%" value="${articlesTitle}" class="search" title="请输入标题">
          </td>
          <td width="10%" align="center"  class="biao_bg1">
          	<input id="articlesAuthor" name="articlesAuthor"
			type="text" style="width:100%" value="${articlesAuthor}" class="search" title="作者">
          </td>
          <td width="15%" align="center"  class="biao_bg1">
          	<input id="clumnName" name="clumnName"
			type="text" style="width:100%" value="${clumnName}" class="search" title="栏目名称">
          </td>
          <td width="15%" align="center"  class="biao_bg1">
          <s:select id="ArcticlesType" name="ArcticlesType"
												list="#{'全部':'0','已上栏':'1','办理中':'2','已发布':'3'}" listKey="value"
												listValue="key" cssStyle="width:100%" 
												onchange="getListBySta()"></s:select>
          </td>
          <td width="15%" align="center"  class="biao_bg1">
          <s:select id="disLogo" name="disLogo"
												list="#{'全部':'0','导读':'1','热点':'2','固点':'3','评论':'4'}" listKey="value"
												listValue="key" cssStyle="width:100%" 
												onchange="getListBySta()"></s:select>
          </td>
          <!-- 
          <td width="10%" align="center"  class="biao_bg1">
          	<input id="columnArticleLatestuser" name="model.columnArticleLatestuser"
			type="text" style="width:100%" value="${modle.columnArticleLatestuser}" class="search" title="编辑">
          </td>
           -->
           <td width="20%" align="center"  class="biao_bg1">
           <strong:newdate id="columnArticleLatestchangtime" name="columnArticleLatestchangtime"
												dateform="yyyy-MM-dd HH:mm:ss" isicon="true" width="100%"
												dateobj="${columnArticleLatestchangtime}" classtyle="search" title="编辑日期"/>
           </td>
          <td width="*%" align="center" class="biao_bg1"><input name="textfield4" type="text" style="width:100%"></td>
      </table> 
		<webflex:flexCheckBoxCol caption="选择" property="columnArticleId" showValue="toaInfopublishArticle.articlesTitle" width="5%" isCheckAll="true" isCanDrag="false" isCanSort="false"></webflex:flexCheckBoxCol>
		<webflex:flexTextCol caption="稿件标题" property="toaInfopublishArticle.articlesTitle" showValue="toaInfopublishArticle.articlesTitle" width="25%" isCanDrag="true" isCanSort="true" showsize="20"></webflex:flexTextCol>
		<webflex:flexTextCol caption="作者" property="toaInfopublishArticle.articlesAuthor" showValue="toaInfopublishArticle.articlesAuthor" width="10%" isCanDrag="true" isCanSort="true"></webflex:flexTextCol>
		<webflex:flexTextCol caption="所属栏目" property="toaInfopublishColumn.clumnName" showValue="toaInfopublishColumn.clumnName" width="15%" isCanDrag="true" isCanSort="true"></webflex:flexTextCol>
		<webflex:flexEnumCol caption="稿件状态" mapobj="${ArcticlesTypeMap}" property="columnArticleState" showValue="columnArticleState" width="15%" isCanDrag="true" isCanSort="true"></webflex:flexEnumCol>
		<webflex:flexTextCol caption="属性"
									property="articlesId"
									showValue="javascript:showimg(columnArticleGuidetype,toaInfopublishArticle.articlesIshot,toaInfopublishArticle.articlesIsstandtop,toaInfopublishArticle.articlesIscancomment)"
									width="15%" isCanDrag="true" isCanSort="true"></webflex:flexTextCol>
		<webflex:flexDateCol caption="编辑时间" property="columnArticleLatestchangtime" showValue="columnArticleLatestchangtime" showsize="20" dateFormat="yyyy-MM-dd HH:mm:ss" width="20%" isCanDrag="true" isCanSort="true"></webflex:flexDateCol>
	</webflex:flexTable>
      </td>
  </tr>
</table>
  </s:form>
</DIV>
<script language="javascript">
var sMenu = new Menu();
function initMenuT(){
	sMenu.registerToDoc(sMenu);
	var item = null;
	<security:authorize ifAllGranted="001-0004000200010008">
	item = new MenuItem("<%=root%>/images/ico/tianjia.gif","发布","pubArticle",1,"ChangeWidthTable","checkMoreDis");
	sMenu.addItem(item);
	</security:authorize>
	<security:authorize ifAllGranted="001-0004000200010009">
	item = new MenuItem("<%=root%>/images/ico/chuli.gif","取消发布","delpubArticle",1,"ChangeWidthTable","checkOneDis");
	sMenu.addItem(item);
	</security:authorize>
	<security:authorize ifAllGranted="001-0004000200010005">
	item = new MenuItem("<%=root%>/images/ico/yidong.gif","查看","show",1,"ChangeWidthTable","checkOneDis");
	sMenu.addItem(item);
	</security:authorize>
	 <security:authorize ifAllGranted="001-0004000200010001">
	item = new MenuItem("<%=root%>/images/ico/chuli.gif","编辑","editUser",1,"ChangeWidthTable","checkOneDis");
	sMenu.addItem(item);
	</security:authorize>
	sMenu.addShowType("ChangeWidthTable");
    registerMenu(sMenu);
}
//查看评论
function commentShow(){
var id=getValue();
	if(id == null||id == ''){
		alert('请选择稿件！');
		return;
	}
window.location.href="<%=root%>/infopub/articles/articles!getComments.action?articlesId="+id;
}

var columnId = '${columnId}';//获取栏目ID

function addArtcles(){//增加稿件
	var audit= window.showModalDialog("<%=root%>/infopub/articles/articles!input.action?columnId="+columnId,window,'help:no;status:no;scroll:no;dialogWidth:800px; dialogHeight:700px');
}
function editUser(){
	var id=getValue();
	if(id == null||id == ''){//判断是否选中
		alert('请选择要编辑的稿件！');
		return;
	}
	if(id.length >32){//判断是否选择多个文章
		alert('只能编辑一个稿件！');
		return;
	}
	var audit = window.showModalDialog("<%=path%>/infopub/articles/articles!input.action?columnArticleId="+id+"&columnId="+columnId+"&isPromulgate=promulgate",window,'help:no;status:no;scroll:no;dialogWidth:800px; dialogHeight:700px');
	//if(audit=='ok'){
	//	location = '<%=path%>/usermanage/usermanage.action?orgId'+orgid;
	//}
}

function del(){//废除稿件
	var id=getValue();
	if(id == null||id == ''){//判断是否选中文章
		alert('请选择稿件！');
		return;
	}
	if(confirm("确定废除稿件吗?")) 
	{ 
	location = '<%=path%>/infopub/articles/articles!colAtrdelete.action?columnArticleId='+id+'&columnId='+columnId;
	} 
	
}
function notColumn(){
	location = '<%=path%>/infopub/articles/articles.action';
}

function show(){
	var id=getValue();
	if(id == null||id == ''){
		alert('请选择要查看的稿件！');
		return;
	}
	if(id.length >32){
		alert('只能查看一个稿件！');
		return;
	}
	//location = '<%=path%>/infopub/articles/articles!showColumnArticl.action?columnArticleId='+id;
	var audit = window.showModalDialog("<%=path%>/infopub/articles/articles!show.action?columnArticleId="+id,window,'help:no;status:no;scroll:no;dialogWidth:860px; dialogHeight:680px');
}

function pubArticle(){
	var id=getValue();
	if(id == null||id == ''){
		alert('请选择要发布的稿件！');
		return;
	}
	if(id.length >32){
		alert('只能发布一个稿件！');
		return;
	}
	$.post(
		"<%=path%>/infopub/articles/articles!getProcessName.action",
		{columnId:columnId,columnArticleId:id},
		function(data){
			if(data!='flagfalse'){
				var str = data;
				
				var p = str.split(",");
				
				if(p[2]=='3'){
		        	alert("文章已提交审核中...");
		        }else if(p[2]=='0'){
		            alert("文章已被删除！");
		        }else if(p[2]=='9'){
		        	alert("文章已发布...")
		        }else if(p[2]=='5'){
		        location = '<%=path%>/infopub/articles/articles!pubArticl.action?columnArticleId='+id+'&columnId='+columnId+'&isPromulgate=1';
		        }
			}else{
				location = '<%=path%>/infopub/articles/articles!pubArticl.action?columnArticleId='+id+'&columnId='+columnId+'&isPromulgate=1';
			}
		}
	)
	
}

function delpubArticle(){
    var id=getValue();
   
	if(id == null||id == ''){
		alert('请选择要取消的稿件！');
		return;
	}
	if(id.length >32){
		alert('只能取消一个稿件！');
		return;
	}
	$.post(
		"<%=path%>/infopub/articles/articles!getProcessName.action",
		{columnId:columnId,columnArticleId:id},
		function(data){
			if(data!='flagfalse'){
					var str = data;
					
					var p = str.split(",");
					
					if(p[2]=='3'){
			        	alert("文章已提交审核中...");
			        }else if(p[2]=='0'){
			            alert("文章已被删除！");
			        }else if(p[2]=='9'){
			        	alert("文章已发布...")
			        }else if(p[2]=='5'){
			    location = '<%=path%>/infopub/articles/articles!delPubArticl.action?columnArticleId='+id+'&columnId='+columnId+'&isPromulgate=1';
				 }
				}else{
					 location = '<%=path%>/infopub/articles/articles!delPubArticl.action?columnArticleId='+id+'&columnId='+columnId+'&isPromulgate=1';
				}
				}
	)
	
}
function getListBySta(){	//根据属性查询
	//document.getElementById("disLogo").value="search";
	document.getElementById("myTableForm").submit();
}

</script>
</BODY></HTML>
