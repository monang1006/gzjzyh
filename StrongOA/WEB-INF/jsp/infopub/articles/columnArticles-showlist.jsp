
<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ taglib uri="/tags/web-flex" prefix="webflex"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<%@ taglib uri="/tags/web-newdate" prefix="strong"%>
<%@ taglib uri="/tags/security" prefix="security"%>
<%@include file="/common/include/rootPath.jsp"%>
<HTML>
	<HEAD>
		<TITLE>文章列表</TITLE>
		<%@include file="/common/include/meta.jsp"%>
		<!--  引用公共样式文件,建议所有样式都以文件方式定义在jsp文件外部,通常定义在WebRoot目录下的CSS文件夹下-->
	<link href="<%=frameroot%>/css/strongitmenu.css" type="text/css" rel="stylesheet"><!--右键菜单样式 -->
	<LINK href="<%=frameroot%>/css/properties_windows_list.css" type=text/css rel=stylesheet>
	<LINK href="<%=frameroot%>/css/search.css" type=text/css  rel=stylesheet>
	<!--  引用公共及自定义js文件,建议js都以文件方式定义在jsp文件外部,通常定义在WebRoot目录下的js文件夹下-->
	<SCRIPT language="javascript" src="<%=path%>/common/js/menu/menu.js"></SCRIPT><!--右键菜单脚本 -->
	<script language='javascript' src='<%=path%>/common/js/grid/ChangeWidthTable.js'></script>
	<script language='javascript' src='<%=path%>/common/js/jquery/jquery-1.2.6.js'></script>
	<script src="<%=path%>/common/js/common/common.js" type="text/javascript"></script>
	<script language="javascript" src="<%=path%>/common/js/common/windowsadaptive.js"></script>
		<script type="text/javascript">
			var flag="";
		function showimg(guidetype,hot,top,cancomment){
			var rv = '' ;
			/**
			if(guidetype == '1'){
				rv += "<font color='#CC0000'>导</font>&nbsp&nbsp";
			}			
			if(hot == '1'){
				rv += "<font color='#FF0000'>热</font>&nbsp&nbsp";
			}**/
			if(top == '1'){
				rv += "<font color='#0000FF'>固顶</font>&nbsp&nbsp";
			}
			if(cancomment == '1'){
				rv += "<font color='#FF00FF'>评论</font>&nbsp&nbsp";
			}
			return rv;
		}
		function showimg1(guidetype,hot,top,cancomment){
			var rv = '' ;
			/**
			if(guidetype == '1'){
				rv += "<font color='#CC0000'>导</font>&nbsp&nbsp";
			}			
			if(hot == '1'){
				rv += "<font color='#FF0000'>热</font>&nbsp&nbsp";
			}**/
			if(top == '1'){
				rv += "固顶 ";
			}
			if(cancomment == '1'){
				rv += "评论 ";
			}
			return rv;
		}
		 //连接OA内部		
    	 function viewFile(value,name){
    	 	getSysConsole().navigate('<%=path%>/infopub/articles/articles!showColumnArticl.action?columnArticleId='+value,name);	  
             //top.perspective_content.actions_container.personal_properties_toolbar.navigate('<%=path%>/infopub/articles/articles!showColumnArticl.action?columnArticleId='+value,name);	  
     	 } 
     	 //连接外部网络
         function gowai(value,name){
             getSysConsole().navigate(value,name);	
         }
         
         function onclickTitl(obj){
         	obj = $(obj);
         	var $parent = obj.parent();//tr
         	var chk = $parent.find("input:checkbox").eq(0);
            var id=chk.val();
            var isurl=obj.attr("value");
            var url=obj.next().attr("value");
            //var titlename = obj.next().next().attr("value");
            var titlename = obj.text();
            if(isurl=='1'){
            
           		viewFile(id,titlename);
               //gowai(url,titlename);
            }else{
            //alert(titlename);
               viewFile(id,titlename)
            }
         }
		function showTitle(titlename,articlesIshot,articlesIsstandtop,isurl,url,id){
			var rv='';
			var name=$.trim(titlename);
			if(articlesIsstandtop == '1'){
				titlename= "<font color='#CC0000'>[顶]</font>&nbsp&nbsp"+titlename;
			}
			rv=titlename;
			if(articlesIshot == '1'){
				rv=rv+ "&nbsp&nbsp<img src='<%=root%>/oa/image/desktop/hot.gif'>";
			}
			return  rv;
		}
		function showTitle1(titlename,articlesIshot,articlesIsstandtop,isurl,url,id){
			var rv='';
			var name=$.trim(titlename);
			if(articlesIsstandtop == '1'){
				titlename= "[顶] "+titlename;
			}
			rv=titlename;
			if(articlesIshot == '1'){
				rv=rv+ "&nbsp&nbsp<img src='<%=root%>/oa/image/desktop/hot.gif'>";
			}
			return  rv;
		}

		</script>
</HEAD>
<BODY class=contentbodymargin oncontextmenu="return false;" onload="initMenuT();">
	<script language="javascript" type="text/javascript" src="<%=path%>/common/js/newdate/WdatePicker.js"></script>
<DIV id=contentborder align=center>
  <table width="100%" border="0" cellspacing="0" cellpadding="0" style="vertical-align: top;">
  <tr>
    <td height="100%">
    <table width="100%" border="0" cellspacing="0" cellpadding="0">
      <tr>
        <td height="40" style="FILTER:progid:DXImageTransform.Microsoft.Gradient(gradientType=0,startColorStr=#ededed,endColorStr=#ffffff);">
        <table width="100%" border="0" cellspacing="0" cellpadding="00">
          <tr>
            	<td colspan="3" class="table_headtd">
					<table width="100%" border="0" cellspacing="0" cellpadding="00">
						<tr>
							<td class="table_headtd_img" >
								<img src="<%=frameroot%>/images/ico/ico.gif" >&nbsp;
							</td>
							<td align="left">
							<script>
									var name = "${column.clumnName }";
											if(name==null|name==""){
												window.document.write("<strong>文章列表</strong>");
											}else{
												window.document.write("文章列表：${column.clumnName }</strong>");
											}
							</script>
							</td>
							<td align="right">
								<table border="0" align="right" cellpadding="00" cellspacing="0">
						            <tr>
						            	<td width="4"><img src="<%=frameroot%>/images/bt_l.jpg"/></td>
					                 	<td class="Operation_list" onclick="show();"><img src="<%=root%>/images/operationbtn/view.png"/>&nbsp;查&nbsp;看&nbsp;文&nbsp;章</td>
					                 	<td width="4"><img src="<%=frameroot%>/images/bt_r.jpg"/></td>
				                  		<td width="5"></td>
				                  		<td width="2%"></td>
									</tr>
					            </table>
							</td>
						</tr>
					</table>
				</td>
            </tr>
            <tr>
            	<td>
            		<s:form id="myTableForm" action="/infopub/articles/columnArticles.action" method="post">
					<input type="hidden" name="columnId" id="columnId" value="${columnId}">
					<input type="hidden" name="columnArticleId" id="columnArticleId" value="${columnArticleId}">
					<input type="hidden" name="showtype" id="showtype" value="${showtype}">
						<webflex:flexTable name="myTable" width="100%" height="370px" wholeCss="table1" property="articlesId" isCanDrag="true" isCanFixUpCol="true" clickColor="#A9B2CA" footShow="showCheck" getValueType="getValueByProperty" 
     						 collection="${page.result}" page="${page}">
					        <table width="100%" border="0" cellpadding="0" cellspacing="0" class="table1_search">
							     <tr>
							       <td>
							       		&nbsp;&nbsp;文章标题：&nbsp;<input style="width:130px;"  maxlength="25" name="articlesTitle" id="articlesTitle" type="text"  class="search" title="请您输入文章标题" value="${articlesTitle }">
							       		&nbsp;&nbsp;作者：&nbsp;<input style="width:100px;" maxlength="25" name="articlesAuthor" id="articlesAuthor" type="text" class="search"  title="请您输入作者" value="${articlesAuthor }">
							       		&nbsp;&nbsp;点击次数：&nbsp;<input maxlength="10"  style="width:100px;" name="Hits" id="Hits" type="text" class="search" title="请您输入点击次数" value="${Hits}">
							       		&nbsp;&nbsp;属性：&nbsp;<s:select name="disLogo"   list="#{'请选择属性':'0','固顶':'3','评论':'4'}" listKey="value" listValue="key" onchange='$("#img_sousuo").click();'/>
							       		&nbsp;&nbsp;&nbsp;&nbsp;<input id="img_sousuo" type="button" />
							       	</td>
							     </tr>
							</table> 
							<webflex:flexCheckBoxCol caption="选择" property="columnArticleId" showValue="toaInfopublishArticle.articlesTitle" width="5%" isCheckAll="true" isCanDrag="false" isCanSort="false"></webflex:flexCheckBoxCol>
								<webflex:flexTextCol caption="文章标题" property="toaInfopublishArticle.articlesIsredirect"  onclick="onclickTitl(this)" 
								showValue="javascript:showTitle(toaInfopublishArticle.articlesTitle,toaInfopublishArticle.articlesIshot,columnArticleIsstandtop,toaInfopublishArticle.articlesIsredirect,toaInfopublishArticle.articlesRedirecturl,columnArticleId)"
								  width="25%" isCanDrag="true" isCanSort="false" showsize="12"></webflex:flexTextCol>
								<webflex:flexTextCol caption="作者"  align="center" property="toaInfopublishArticle.articlesRedirecturl" showValue="toaInfopublishArticle.articlesAuthor" width="10%" isCanDrag="true" isCanSort="false"></webflex:flexTextCol>
								<webflex:flexTextCol caption="点击次数"   align="center" property="toaInfopublishArticle.articlesTitle" showValue="toaInfopublishArticle.articlesHits" width="15%" isCanDrag="true" isCanSort="false"></webflex:flexTextCol>
					    		<webflex:flexTextCol caption="属性"  align="center" 
														property="articlesId"
														showValue="javascript:showimg(columnArticleGuidetype,toaInfopublishArticle.articlesIshot,columnArticleIsstandtop,toaInfopublishArticle.articlesIscancomment)"
														width="10%" isCanDrag="true" isCanSort="false"></webflex:flexTextCol>
					    		<webflex:flexTextCol caption="评论次数"   align="center" property="countComment" showValue="countComment" width="15%" isCanDrag="true" isCanSort="false"></webflex:flexTextCol>
					    	
					    		<webflex:flexDateCol caption="发布时间"  property="toaInfopublishArticle.articlesAutopublishtime" showValue="toaInfopublishArticle.articlesAutopublishtime" showsize="20" dateFormat="yyyy-MM-dd HH:mm:ss" width="20%" isCanDrag="true" isCanSort="false"></webflex:flexDateCol>
					   		</webflex:flexTable>
					</s:form>
            	</tr>
			</table>
</DIV>
<script language="javascript">
var sMenu = new Menu();
function initMenuT(){
	sMenu.registerToDoc(sMenu);
	var item = null;
    item = new MenuItem("<%=root%>/images/operationbtn/view.png","查看文章","show",1,"ChangeWidthTable","checkOneDis");
	sMenu.addItem(item);
	sMenu.addShowType("ChangeWidthTable");
    registerMenu(sMenu);
}

var columnId = '${columnId}';

function addArtcles(){//增加稿件
	var audit= window.showModalDialog("<%=root%>/infopub/articles/articles!input.action?columnId="+columnId,window,'help:no;status:no;scroll:no;dialogWidth:800px; dialogHeight:700px');
}

function editUser(){
	var id=getValue();
	if(id == null||id == ''){
		alert('请选择要编辑的记录。');
		return;
	}
	if(id.length >32){
		alert('只可以编辑一条记录。');
		return;
	}
	var audit = window.showModalDialog("<%=path%>/infopub/articles/articles!input.action?columnArticleId="+id+"&columnId="+columnId,window,'help:no;status:no;scroll:no;dialogWidth:800px; dialogHeight:700px');
	//if(audit=='ok'){
	//	location = '<%=path%>/usermanage/usermanage.action?orgId'+orgid;
	//}
}
function del(){//废除稿件
	var id=getValue();
	if(id == null||id == ''){
		alert('请选择要废除的记录。');
		return;
	}
	if(confirm("确定要废除吗？")) 
	{ 
	location = '<%=path%>/infopub/articles/articles!colAtrdelete.action?columnArticleId='+id+'&columnId='+columnId;
	} 
	
}
function notColumn(){
	location = '<%=path%>/infopub/articles/articles.action';
}


function getInfo(id){alert(id);
	var info = new Array();
	<s:iterator value="page.result">
		if(id == '${columnArticleId}'){
			info[0] = '${toaInfopublishArticle.articlesTitle}';
			info[1] = '${toaInfopublishArticle.articlesIshot}';
			info[2] = '${columnArticleIsstandtop}';
			info[3] = '${toaInfopublishArticle.articlesIsredirect}';
			info[4] = '${toaInfopublishArticle.articlesRedirecturl}';
			info[5] = id;
		}
	</s:iterator>
	return info;
}

function show(){
	var id=getValue();
	if(id == null||id == ''){
		alert('请选择要查看文章的记录。');
		return;
	}
	if(id.length >32){
		alert('只可以查看一条记录。');
		return;
	}
	var titlename;
	 $("input:checkbox").each(function(){
		   if($(this).attr("checked")){
			   titlename=$(this).parent().next().text();		    
		      }
	 });
		getSysConsole().navigate('<%=path%>/infopub/articles/articles!showColumnArticl.action?columnArticleId='+id,titlename);	
	//location = '<%=path%>/infopub/articles/articles!showColumnArticl.action?columnArticleId='+id;
	//top.perspective_content.actions_container.personal_properties_toolbar.navigate('<%=path%>/infopub/articles/articles!showColumnArticl.action?columnArticleId='+id,'查看信息');
}

function pubArticle(){ 
	var id=getValue();
	if(id == null||id == ''){
		alert('请选择要发布的记录。');
		return;
	}
	if(id.length >32){
		alert('只能发布一条记录。');
		return;
	}
	$.post(
		"<%=path%>/infopub/articles/articles!getProcessName.action",
		{columnId:columnId,columnArticleId:id},
		function(data){
			if(data!='flagfalse'){
				var str = data;
				var p = str.split(",");
				if(p[2] != '3' && p[2]!='9'){
					var returnValue = OpenWindow("<%=root%>/infopub/articles/articles!wizard.action?columnArticleId="+p[1]+"&columnId="+p[0], 
			                                  400, 250, window);
			        if(returnValue=='OK'){
			        	location.reload();
			        }
		        }else if(p[2]=='3'){
		        	alert("文章已提交审核中...");
		        }else{
		        	alert("文章已发布...")
		        }
			}else{
				location = '<%=path%>/infopub/articles/articles!pubArticl.action?columnArticleId='+id+'&columnId='+columnId;
			}
		}
	)
	
}


function getListBySta(){	//根据属性查询
	//document.getElementById("disLogo").value="search";
	var reg = /^\d+$/;
	var hits = document.getElementById("Hits").value; 
	if(hits != null && hits != ""){
		if(!reg.test(hits)){
			alert("点击次数请输入非负整数!")
			return;
		}
	}
	document.getElementById("myTableForm").submit();
	
}
$(document).ready(function(){
	
  	//搜索
       $("#img_sousuo").click(function(){
    	   var reg = /^\d+$/;
    		var hits = document.getElementById("Hits").value; 
    		if(hits != null && hits != ""){
    			if(!reg.test(hits)){
    				alert("点击次数请输入非负整数。")
    				return;
    			}
    		}
       	$("form").submit();
       });     
  })
</script>
</BODY></HTML>
