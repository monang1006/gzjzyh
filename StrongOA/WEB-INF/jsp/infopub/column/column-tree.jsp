<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<jsp:directive.page import="com.strongit.oa.bo.ToaInfopublishColumn"/>
<%@ taglib uri="/tags/web-flex" prefix="webflex"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<%@include file="/common/include/rootPath.jsp"%>
<html>
<head>
<%@include file="/common/include/meta.jsp"%>
<title>公文模板类别</title>
<style type="text/css">
#contentborder,.folder-close,.folder-open,.folder-open-last,.folder-close-last{background-color:#eeeff3 !important;}
</style>
<LINK href="<%=frameroot%>/css/windows.css" type=text/css rel=stylesheet>
<LINK href="<%=frameroot%>/css/treeview.css" type=text/css rel=stylesheet>
<SCRIPT type="text/javascript" src="<%=path%>/common/js/commontab/service.js"></SCRIPT>
<SCRIPT type="text/javascript" src="<%=path%>/common/js/commontab/workservice.js"></SCRIPT>
<script type="text/javascript" src="<%=root%>/oa/js/mymail/jquery.js"></script>
<script type="text/javascript" src="<%=root %>/oa/js/mymail/jquery.contextmenu.r2.js"></script>
<script type="text/javascript" src="<%=root %>/oa/js/mymail/jquery.simple.tree.js"></script>
<script src="<%=path%>/common/js/common/common.js" type="text/javascript"></script>
<script language="JavaScript" src="<%=path%>/common/js/commontab/service.js"></script>
		<script language="JavaScript" src="<%=path%>/common/js/commontab/workservice.js"></script>

<script type="text/javascript">
var simpleTreeCollection;
function showname(id){
     $.post("<%=path%>/infopub/column/column!show.action",
            {clumnId:id},
            function(date){
            return date;
            });
}
$(document).ready(function(){
	simpleTreeCollection = $('.simpleTree').simpleTree({
		drag:false,
		autoclose: false,
		afterClick:function(node){
			var folderId = node.get(0).id;
			parent.project_work_content.document.location="<%=root%>/infopub/column/column!input.action?clumnId="+folderId;
		},
		afterDblClick:function(node){
		},
		afterMove:function(destination, source, pos){
		},
		afterAjax:function()
		{
		},
		afterContextMenu:function(node){

			$($('span:first',node)).contextMenu('myMenu1', {

			  menuStyle: {

				border: '2px solid #000'

			  },

			  itemStyle: {

				fontFamily : 'verdana',

				backgroundColor : '#666',

				color: 'white',

				border: 'none',

				padding: '1px'

			  },

			  itemHoverStyle: {

				color: '#fff',

				backgroundColor: '#0f0',

				border: 'none'

			  }

			});

		$($('span:first',node)).contextMenu('myMenu1', {

		  bindings: {

			'add': function(t) {
				var folderId = node.get(0).id;
			   	parent.project_work_content.document.location="<%=root%>/infopub/column/column!newSubColumn.action?clumnId="+folderId;
				//parent.location.reload() ;
			},

            'newcolumn':function(t){
                parent.project_work_content.document.location="<%=root%>/infopub/column/column!newSubColumn.action";
	
            },
            
			'edit': function(t) {
			  var folderId = node.get(0).id;
			  parent.project_work_content.document.location="<%=root%>/infopub/column/column!input.action?clumnId="+folderId;
			  //parent.location.reload() ;
			},

			'delete': function(t) {
				var folderName = $('span:first',node).text();	
				var id = node.get(0).id;
			 	if(confirm("确定要删除栏目“"+folderName+"”吗？")){
			 		$.post(
			 		"<%=path%>/infopub/column/column!delete.action",
			 		{clumnId:id},
			 		function(data){
			 			if(data == "delfalse"){	 								 			
			 				alert("该栏目下存在文章或子栏目，请先删除文章和子栏目。");
			 			}else{
				 			
				 			parent.location.reload();
			 			}
			 		 }
			 		);
			 	}

			},
			
			'marge':function(t){ //合并栏目
				var folderName = $('span:first',node).text();
				var id = node.get(0).id;
				var ReturnStr=OpenWindow("<%=root%>/infopub/column/column!chooseTree.action", 
                                   "300px", "350px", window);
               if(ReturnStr!=undefined){                    
                var goValue=ReturnStr.split(",");
                if(ReturnStr!="" && ReturnStr!=null){
                if(id==goValue[0]){
                   alert("不可以和自己合并！");
                   return;
                }
                	if(confirm("确定要将栏目“"+folderName+"”合并到“"+goValue[1]+"”的栏目下吗?")){
                		if(id!=ReturnStr){
                			$.post(
	                		"<%=path%>/infopub/column/column!margeColumn.action",
	                		{clumnId:id,pclumnId:goValue[0]},
	                		function(data){
	                			if(data == "margetrue"){
	                				parent.location.reload();
	                			}else{
	                				alert(data);
	                			}
	                			
	                		   }
	                		);
                		}else{
                			alert("不允许合并栏目至栏目本身");
                		}
					}
				}
				}                   
			},
			
			'move':function(t){//移动栏目
				var folderName = $('span:first',node).text();
				
				var id = node.get(0).id;
				var ReturnStr=OpenWindow("<%=root%>/infopub/column/column!chooseTree.action", 
                                   "300px", "350px", window);
             if(ReturnStr!=undefined){  
                var goValue=ReturnStr.split(",");
                if(ReturnStr!="" && ReturnStr!=null){
                  if(id==goValue[0]){
                     alert("不可以将“"+folderName+"”移动到自己下面。");
                     return;
                  }
                  if(confirm("确定要将栏目“"+folderName+"”移到“"+goValue[1]+"”栏目下吗？")){
					if(id!=ReturnStr){
	                	$.post(
	                		"<%=path%>/infopub/column/column!moveColumn.action",
	                		{clumnId:id,pclumnId:goValue[0]},
	                		function(data){
	                			if(data == "movetrue"){
	                				parent.location.reload();
	                			}else{
	                				alert(data);
	                			}
	                			
	                		}
	                	);
                	}else{
                		alert("不允许移动栏目至栏目本身。");
                	}
					}
				}   
				} 
			},
			
			'setpcol':function(t){
				var folderName = $('span:first',node).text();	
				var id = node.get(0).id;
				var parent=node.get(0).name;
				
				//confirm(folderName+"已经是顶级栏目！");
				//return false;
			 if(confirm("确定要将栏目“"+folderName+"”置为顶级栏目吗？")){
			 		$.post(
			 		"<%=path%>/infopub/column/column!setParentColumn.action",
			 		{clumnId:id},
			 		function(data){
			 			if(data == "settrue"){	 								 			
			 			window.parent.location.reload();
			 			}else{
				 			alert("该栏目已经是顶级栏目，不需要再设置。");
			 			}
			 		 }
			 		);
			 	}
				
			}
			

		  }

		});


		},
		animate:true
	});


});

   
		

</script>
</head>

<body class=contentbodymargin oncontextmenu="return false;">

<div class="contextMenu" oncontextmenu="return false;" id="myMenu1" style="width:100%">
	<ul>
	    <li id="newcolumn"><img src="<%=path %>/oa/image/mymail/folder_add.png" /> &nbsp;新建顶级栏目</li>
		<li id="add"><img src="<%=path %>/oa/image/mymail/folder_add.png" /> 新建子栏目</li>
		<li id="edit"><img src="<%=path %>/oa/image/mymail/folder_edit.png" /> 编辑栏目</li>
		<li id="delete"><img src="<%=path %>/oa/image/mymail/folder_delete.png" /> 删除栏目</li>
		<li id="marge"><img src="<%=path %>/oa/image/mymail/folder_edit.png" /> 合并栏目</li>
		<li id="move"><img src="<%=path %>/oa/image/mymail/folder_edit.png" /> 移动栏目</li>
		<li id="setpcol"><img src="<%=path %>/oa/image/mymail/folder_edit.png" /> 置顶级栏目</li>
	</ul>
</div>
<div id=contentborder>
<ul class="simpleTree">
	<div>
		</br>
	</div>
	<li class="root" id='1'>
	<span>栏目</span>
		<ul>
			<%
				List<ToaInfopublishColumn> list=(List<ToaInfopublishColumn>)request.getAttribute("columnList");
			     
				String[] child=(String[])request.getAttribute("hasChild");
				for(int i=0;i<list.size();i++){
					ToaInfopublishColumn folder=list.get(i);
					String charge=child[i];
					String clumnparent=folder.getClumnParent();//判断是否是顶级栏目
					if("0".equals(folder.getClumnParent())){
						if("1".equals(charge)){//存在子节点
							String id=folder.getClumnId()+i;
							out.println("<li name="+clumnparent+" id="+folder.getClumnId() +">");
							out.println("<span>"+folder.getClumnName()+"</span>");	
							out.println("<ul class=ajax>");
							out.println("<li name="+clumnparent+"  id="+id +">{url:"+root+"/infopub/column/column!syntree.action?clumnId="+folder.getClumnId()+"}</li>");	
							out.println("</ul>");	
							out.println("</li>");	
						//}else{
						}else {
							out.println("<li name="+clumnparent+"  id="+folder.getClumnId() +">");
							out.println("<span>"+folder.getClumnName());
							out.println("</span>");
							out.println("</li>");
						}
					}
				}	
			%>		
		</ul>
	</li>		
</ul>
</div>
</body>

</html>
