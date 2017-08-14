<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="/tags/web-bigtree" prefix="tree"%>
<%@include file="/common/include/rootPath.jsp"%>
<HTML>
	<HEAD>
		<TITLE>选择人员</TITLE>
		<%@include file="/common/include/meta.jsp" %>
		<LINK type=text/css rel=stylesheet href="<%=frameroot%>/css/properties_windows.css">
		<LINK href="<%=path%>/common/css/tree.css" type=text/css rel=stylesheet>
		<script src="<%=path%>/common/js/jquery/jquery-1.2.6.js" type="text/javascript"></script>
		<SCRIPT src="<%=root%>/common/bigtree/js/tree.js"></SCRIPT>
		<script type="text/javascript">
			var treeObj = null;
			function init(o){
				/*var aHasSelected = '';
				if(aHasSelected != ""){
	                o.aHasSelected = ","+aHasSelected+",";   
				}*/	
				treeObj = o;
			}
			function doSubmit(){
				var id = "";
				if(treeObj == null) {
					alert("树节点未生成。");
					return ;
				}
				var ids = $("#bigTreeDiv").getTSIds();
				$.each(ids,function(i,item){
					if(item != ''){
						id += item;
					}
				});
				var ret = new Array();
				if(id != ""){
					var ids = id.split(",");
					for(var i=0;i<ids.length;i++){
						if(ids[i].substring(0,1) == 'u'){
							var uname = ids[i].substring(ids[i].indexOf("@")+1,ids[i].lastIndexOf("@"));
							ret.push(uname);
						}
					}
				}
				if(ret.length == 0){
					window.returnValue = "";
					window.close();
				}else{
					var param = window.dialogArguments;
					window.returnValue = ret.join(param);
					window.close();
				}
			}
			
		</script>
	</HEAD>
	<BODY>
		<DIV id=contentborder cellpadding="0">
			<div align="center">
				<input type="button" class="input_bg" onclick="doSubmit()" value="确 定"/>
				&nbsp;&nbsp;
				<input type="button" class="input_bg" onclick="window.close()" value="取 消"/>
			</div>
			<tree:strongbigtree data="${orgList}"  dealclass="com.strongit.oa.address.util.AddressSelectUserDeal"  check="true"/>
			<div align="center">
					<input type="button" class="input_bg" onclick="doSubmit()" value="确 定"/>
					&nbsp;&nbsp;
					<input type="button" class="input_bg" onclick="window.close()" value="取 消"/>
			</div>
		</DIV>	
	</BODY>
</HTML>
