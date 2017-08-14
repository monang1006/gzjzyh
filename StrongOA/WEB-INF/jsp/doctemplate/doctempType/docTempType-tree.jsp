<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<jsp:directive.page import="com.strongit.oa.bo.ToaDoctemplateGroup"/>
<%@ taglib uri="/tags/web-flex" prefix="webflex"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<%@include file="/common/include/rootPath.jsp"%>
<html>
<head>
<title>公文模板类别</title>
<LINK href="<%=frameroot%>/css/windows.css" type=text/css rel=stylesheet>
<LINK href="<%=frameroot%>/css/treeview.css" type=text/css rel=stylesheet>
<SCRIPT type="text/javascript" src="<%=path%>/common/js/commontab/service.js"></SCRIPT>
<SCRIPT type="text/javascript" src="<%=path%>/common/js/commontab/workservice.js"></SCRIPT>
<script type="text/javascript" src="<%=root%>/oa/js/mymail/jquery.js"></script>
<script type="text/javascript" src="<%=root %>/oa/js/mymail/jquery.contextmenu.r2.js"></script>
<script type="text/javascript" src="<%=root %>/oa/js/mymail/jquery.simple.tree.js"></script>
<script src="<%=path%>/common/js/common/common.js" type="text/javascript"></script>

<script type="text/javascript">
var simpleTreeCollection;
$(document).ready(function(){
	simpleTreeCollection = $('.simpleTree').simpleTree({
		drag:false,
		autoclose: false,
		afterClick:function(node){
			var folderId = node.get(0).id;
			parent.project_work_content.document.location="<%=root%>/doctemplate/doctempItem/docTempItem.action?docgroupId="+folderId;
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
			    var ReturnStr=OpenWindow("<%=root%>/doctemplate/doctempType/docTempType!init.action?docgroupId="+folderId,  "400px", "250px", window);
				if(ReturnStr=="OK"){
					location.reload() ;
				}
			},

			'edit': function(t) {

			  var folderId = node.get(0).id;
							
				$.post("<%=path%>/doctemplate/doctempItem/docTempItem!IsAddItem.action",
	           {"docgroupId":folderId},
	           function(data){	         
			    	if(data=="1"){
						    var ReturnStr=OpenWindow("<%=root%>/doctemplate/doctempType/docTempType!input.action?docgroupId="+folderId,"400px", "250px", window);
							if(ReturnStr=="OK"){
								parent.location.reload() ;
							}
			    		}
			    	else{
			    			var message="当前模板类型【"+data+"】已经添加了模板或子节点，不可以编辑。";
			    			alert(message);
			    		}			    		
		    		}
		    	);			    		
			  

			},

			'delete': function(t) {
				var folderName = $('span:first',node).text();	
				var id = node.get(0).id;
			 	if(confirm("确定要删除模板类别“"+folderName+"”吗？")){
			 		$.post(
			 		"<%=path%>/doctemplate/doctempType/docTempType!delete.action",
			 		{docgroupId:id},
			 		function(data){
			 			if(data == "delfalse"){//此模板类别下已存在模板			 								 			
			 				alert("该模板类别下已存在模板项或子节点，不允许删除。");
			 			}else{
				 			alert("删除成功。");
				 			parent.location.reload();
			 			}
			 		 }
			 		);
			 	}

			}

		  }

		});


		},
		animate:false
	});


		$("#rootSpan").contextMenu("myMenu2",{
		bindings:{
			'add':function(){
				addFolder();
			}
		}
	});
	//在BODY上绑定右键功能
	$("body").contextMenu("myMenu2",{
		bindings:{
			'add':function(){
				addFolder();
			}
		}
	});
	
	function addFolder(){
		var ReturnStr=OpenWindow("<%=root%>/doctemplate/doctempType/docTempType!init.action", 
                                   "400px", "250px", window);
	    if(ReturnStr=="OK"){
	    	parent.project_work_tree.document.location.reload() 
	    }                          
	}	


});



</script>
<style type="text/css">
.folder-close,.simpleTree,.folder-open{background-color:#eeeff3 !important;}
</style>
</head>

<body  oncontextmenu="return false;" style="background-color: #eeeff3;"><!-- oncontextmenu="return false;" -->

<div class="contextMenu" oncontextmenu="return false;" id="myMenu1" style="width:100%">
	<ul>
		<li id="add"><img src="<%=path %>/oa/image/mymail/folder_add.png" /> &nbsp;新建</li>
		<li id="edit"><img src="<%=path %>/oa/image/mymail/folder_edit.png" /> 编辑</li>
		<li id="delete"><img src="<%=path %>/oa/image/mymail/folder_delete.png" /> 删除</li>
	</ul>
</div>

<div class="contextMenu" oncontextmenu="return false;" id="myMenu2" style="width:100%">
	<ul>
		<li id="add"><img src="<%=path %>/oa/image/mymail/folder_add.png" /> 新建类别</li>
	</ul>
</div>

<ul class="simpleTree">
	<li class="root" id='1'><span style="cursor: hand;" id="rootSpan">模板类别</span>
		<ul>
			<%
				List<ToaDoctemplateGroup> list=(List<ToaDoctemplateGroup>)request.getAttribute("docTempTypeList");
				String[] child=(String[])request.getAttribute("hasChild");
				for(int i=0;i<list.size();i++){
					ToaDoctemplateGroup folder=list.get(i);
					String docgroupParentId=folder.getDocgroupParentId();
					String typeName="";
					if(folder.getDocgroupType()!=null&&folder.getDocgroupType().equals("1")){
						//typeName="编辑器";
						typeName = "<img width=\"16px\" height=\"16px\" src=\""+path +"/images/ico/edit.gif\" title='编辑器' />";
					}else{
						//typeName="word控件";
						typeName = "<img width=\"16px\" height=\"16px\" src=\""+path +"/images/ico/word.gif\" title='word控件'/>";
					}
					
					String charge=child[i];
					if("1".equals(charge)){//存在子节点
						String id=folder.getDocgroupId()+i;
						out.println("<li id="+folder.getDocgroupId() +">");
						out.println("<span>"+folder.getDocgroupName()+typeName+"</span>");	
						out.println("<ul class=ajax>");
						out.println("<li id="+id +">{url:"+root+"/doctemplate/doctempType/docTempType!syntree.action?docgroupId="+folder.getDocgroupId()+"}</li>");	
						out.println("</ul>");	
						out.println("</li>");	
					}else{
						if("0".equals(docgroupParentId)){
							out.println("<li id="+folder.getDocgroupId() +">");
							out.println("<span>"+folder.getDocgroupName()+typeName+"</span>");	
							out.println("</li>");
						}else{
							out.println("<li id="+folder.getDocgroupId() +">");
							out.println("<span>"+folder.getDocgroupName());
							out.println("</span>");
							out.println("</li>");
						}
					}
				}	
			%>		
		</ul>
	</li>		
</ul>
</body>

</html>
