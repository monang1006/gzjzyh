<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="/tags/web-tree" prefix="tree"%>
<%@include file="/common/include/rootPath.jsp"%>
<HTML>
	<HEAD>
		<TITLE>选择人员</TITLE>
		<%@include file="/common/include/meta.jsp" %>
		<LINK href="<%=frameroot%>/css/properties_windows.css" type=text/css rel=stylesheet>
		<LINK href="<%=path%>/common/css/treeview.css" type=text/css rel=stylesheet>
		<script type="text/javascript">
			var imageRootPath='<%=path%>/common/frame';
		</script>
		<SCRIPT src="<%=root%>/common/js/mztree_check/mztreeview_check.js"></SCRIPT>
		<script src="<%=path%>/common/js/jquery/jquery-1.2.6.js" type="text/javascript"></script>
		<script src="<%=path%>/common/js/common/btn.js" type="text/javascript"></script>
		<style type="text/css">
			#contentborder{
				BORDER-RIGHT: #848284 1px solid;
				PADDING-RIGHT: 3px;
				BORDER-TOP: #848284 1px solid;
				PADDING-LEFT: 3px;
				BACKGROUND: white;
				PADDING-BOTTOM: 10px;
				OVERFLOW: auto;
				BORDER-LEFT: #848284 1px solid;
				PADDING-TOP: 0px;
				BORDER-BOTTOM: #848284 1px solid;
			}
			
		</style>
		<script type="text/javascript">
			var chkTreeNode	;
			$(document).ready(function(){
				chkTreeNode = document.getElementsByName("chkTreeNode");
				init();//初始化
				$("input:checkbox").each(function(){
					$(this).click(function(){
						var chkId = $(this).val();
						var len = chkId.length;
						var index = chkId.indexOf("<-user->");
						if(index!=-1){//此节点为用户
							var id = chkId.substring(chkId.lastIndexOf(">")+1);
							var txt = $(this).next().text();
							if($(this).attr("checked") == true){//勾选
								parent.addOpt(txt,id);
							}else{//未选中状态
								parent.removeOpt(id);
							}
						}else{
							if($(this).attr("checked") == true){//勾选
								$("input:checkbox").each(function(){
									var _childId = $(this).val();
									var index = _childId.lastIndexOf("<-user->");
									if(index!=-1){//节点为用户节点
										if(_childId.substring(0,len) == chkId){
											var id = _childId.substring(_childId.lastIndexOf(">")+1);
											var txt = $(this).next().text();
											parent.addOpt(txt,id);
										}
									}
								});
							}else{
								$("input:checkbox").each(function(){
									var _childId = $(this).val();
									var index = _childId.lastIndexOf("<-user->");
									if(index!=-1){//节点为用户节点
										if(_childId.substring(0,len) == chkId){
											var id = _childId.substring(_childId.lastIndexOf(">")+1);
											parent.removeOpt(id);
										}
									}
								});
							}
						}
						
						/*if(id.length == 32 && id.substring(0,3) != "org"){
							if($(this).attr("checked") == true){//勾选
								parent.addOpt(txt,id);
							}else{//未选中状态
								parent.removeOpt(id);
							}
						}else{
							if($(this).attr("checked") == true){//勾选
								$(":checked").each(function(){
									var id = $(this).val();
									var txt = $(this).next().text();
									if(id.length == 32 && id.substring(0,3) != "org"){
										parent.addOpt(txt,id);
									}
								});
							}else{
								var pid=this.id.substring(4);
								
								var grandfather = this;
								if(tree.node[pid].valueId == "" || tree.node[pid].valueId.substring(0,3) != "org"){//包含2个或2个以上节点的根节点
									$("input:checkbox").each(function(){
										var valueid = $(this).val();
										if(valueid.length == 32 && valueid.substring(0,3) != "org"){
											parent.removeOpt(valueid);
										}
									});		
								}else{
									$("input:checkbox").each(function(){
										var valueid = $(this).val();
										if(valueid.length == 32 && valueid.substring(0,3) != "org"){
											var checkid=this.id.substring(4);
											var checknode=tree.node[checkid];
											var parentid=checknode.parentId;
											if(pid == parentid){
												parent.removeOpt(valueid);
											}
										}else{
											var checkid=this.id.substring(4);
											var checknode=tree.node[checkid];
											var parentid=checknode.parentId;
											if(pid == parentid){
												if(checknode.hasChild){
													for(var j=0;j<checknode.childNodes.length;j++){
														var childnode=checknode.childNodes[j];
														parent.removeOpt(childnode.valueId);
													}
												}
											}
										}
									});
								}
							}	
						}*/
					});
				});
				
			});
			//全部取消
			function cancelChk(){
				$("input:checkbox").each(function(){
					$(this).attr("checked",false);
				});
			}
			
			//初始化选择框值,根据主窗口的选择框来加载
			function init(){
				var objSelect = parent.document.getElementById("sel_person");
				$("input:checkbox").each(function(){
					var chkvalue = $(this).val();
					var index = chkvalue.indexOf("<-user->");
					if(index!=-1){
						var id = chkvalue.substring(chkvalue.lastIndexOf(">")+1);
						if (objSelect.length != 0) {
	                        for(var j=0; j<objSelect.options.length; j++){
	                             if (id == objSelect.options[j].value) {
	                                 $(this).attr("checked",true);
	                             }
	                        }
	                    }    
					}
				});
			}
			
		</script>
	</HEAD>
	<BODY>
		<tree:strongtree title="组织机构"  check="true" dealclass="com.strongit.oa.address.util.AddressSelectUserDeal" data="${orgList}" target="project_work_content"  />	
	</BODY>
</HTML>
