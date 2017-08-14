<%@ page contentType="text/html; charset=utf-8"%>
<%@include file="/common/include/rootPath_cb.jsp"%>
<%@ taglib prefix="web" uri="/tags-web/component"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<html>
	<head>
		<META http-equiv=Content-Type content="text/html; charset=UTF-8">
		<title>组织机构树</title>
		<link href="<%=themePath%>/css/global.css" rel="stylesheet" type="text/css" />
		<link rel="stylesheet" type="text/css"
			href="<%=themePath%>/css/component.css" />
		<script type="text/javascript" src="<%=scriptPath%>/jquery.tooltip.js"></script>
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
<script type="text/javascript">
<!--
    /**
     * 单击树节点时右边框架展现该组织机构下的人员信息列表
     * @param item -点击的树节点
     */
  	function selectOrg(item){
		var id = item.value;
		$.get("<%=root%>/xxbs/action/address!isNature.action?toId="+id, function(res){
			if(res!="nosuccess"){
				parent.mainFrame.topFrame.location = "<%=root%>/xxbs/action/address!ogrlist.action?extOrgId="+id;
			}
		});
	}
//-->
</script>
<script type="text/javascript"
			src="<%=path%>/common/script/stopwait.js"></script>