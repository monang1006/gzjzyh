<%@ page contentType="text/html; charset=utf-8"%>
<%@include file="/common/include/rootPath_cb.jsp"%>
<%@ taglib prefix="web" uri="/tags-web/component"%>
<html>
	<head>
		<META http-equiv=Content-Type content="text/html; charset=UTF-8">
		<title>组织机构树</title>
		<link href="<%=themePath%>/css/global.css" rel="stylesheet" type="text/css" />
		<link rel="stylesheet" type="text/css"
			href="<%=themePath%>/css/component.css" />
		<script language="javascript" src="<%=scriptPath%>/jquery-1.4.1.min.js"></script>
		<script type="text/javascript" src="<%=path%>/common/script/common.js"></script>			
		<script language="javascript" src="<%=scriptPath%>/component.js"></script>
	</head>

	<body>
		<script type="text/javascript" src="<%=path%>/common/script/wait.js"></script>	
		<div class="tree_box" style="position:relative;">
			<div id="moduleTree"></div>
			<web:tree name="moduleTree" lazyUrl="${root}/usermanage/usermanage!getOrgTree1.action" iconDir="${themePath}/image/tree/" title="组织机构" nodeClick="selectOrg"></web:tree>
		</div>
	</body>
</html>
<%
	String flag = request.getParameter("flag");
%>
<script type="text/javascript">
<!--
    /**
     * 单击树节点时右边框架展现该组织机构下的人员信息列表
     * @param item -点击的树节点
     */
  	function selectOrg(item){
		var id = item.value;
		var flag= <%=flag%>
		if(flag=="guoban"){
			$.get("<%=root%>/xxbs/action/address!isNature.action?toId="+id, function(res){
			if(res!="nosuccess"){
			parent.mainFrame.location = "<%=root%>/xxbs/action/piece!content.action?orgId="+id+"&flag=guoban";
			}
			});
		}
		else if(flag=="shengji"){
			$.get("<%=root%>/xxbs/action/address!isNature.action?toId="+id, function(res){
				if(res!="nosuccess"){
			parent.mainFrame.location = "<%=root%>/xxbs/action/piece!content.action?orgId="+id+"&flag=shengji";
			}
			});
		}
		else if(flag=="jiafen"){
			$.get("<%=root%>/xxbs/action/address!isNature.action?toId="+id, function(res){
				if(res!="nosuccess"){
			parent.mainFrame.location = "<%=root%>/xxbs/action/piece!content.action?orgId="+id+"&flag=jiafen";
			}
			});
		}
		else{
			$.get("<%=root%>/xxbs/action/address!isNature.action?toId="+id, function(res){
				if(res!="nosuccess"){
			parent.mainFrame.location = "<%=root%>/xxbs/action/piece!content.action?orgId="+id;
			}
			});
		}
		
	}
//-->
</script>
<script type="text/javascript"
			src="<%=path%>/common/script/stopwait.js"></script>