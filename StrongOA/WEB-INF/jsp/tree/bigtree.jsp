<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<jsp:directive.page import="com.strongmvc.exception.SystemException"/>
<%@ taglib uri="/tags/web-bigtree" prefix="tree"%>
<%@	include file="/common/include/rootPath.jsp"%>
<%
	List data = (List)request.getAttribute("data");
	if(data == null){
		throw new SystemException("数据为空！");
	}
	String title = (String)request.getAttribute("title");
	if(title == null || "null".equals(title) || "".equals(title)){
		title = request.getParameter("title");
		if(title == null || "null".equals(title) || "".equals(title)){
			title = "组织机构";
		}
	}
	String check = (String)request.getAttribute("check");
	if(check == null || "null".equals(check) || "".equals(check)){
		check = request.getParameter("check");
		if(check == null || "null".equals(check) || "".equals(check)){
			check = "true";
		}
	}
	boolean chk = Boolean.parseBoolean(check);
	String hasSelected = (String)request.getAttribute("id");
	if(hasSelected == null || "null".equals(hasSelected) || "".equals(hasSelected)) {
		hasSelected = request.getParameter("id");
		if(hasSelected == null || "null".equals(hasSelected) || "".equals(hasSelected)) {
			hasSelected = "";
		}
	}
%>
<html>
		<head>
		<title><%=title %></title>
		<LINK type=text/css rel=stylesheet href="<%=frameroot%>/css/properties_windows_add.css">
		<LINK href="<%=path%>/common/css/tree.css" type=text/css rel=stylesheet>
		<script src="<%=path%>/common/js/jquery/jquery-1.2.6.js" type="text/javascript"></script>
		<SCRIPT src="<%=root%>/common/bigtree/js/tree.js"></SCRIPT>
		<script type="text/javascript">
			var treeObj = null;
			//初始化
			function init(o){
				var aHasSelected = '<%=hasSelected%>';
				if(aHasSelected != ""){
	                o.aHasSelected = ","+aHasSelected+",";   
				}	
				treeObj = o;
			}
			//得到选中的项
			function doSubmit() {
				var ret = new Array();
				var id = "";
				var name = "";
				if(treeObj == null) {
					alert("树节点未生成。");
					return ;
				}
				var items = $("#bigTreeDiv").getTSNs();
				$.each(items,function(i,item){
					//alert(item.id+"--"+item.text);
					if((item.id).substring(0,1) != 'o'){//机构节点
						id += item.id + ",";
						name += item.text + ",";
					}
				});
				if(id != ""){
					id = id.substring(0,id.length - 1);
					name = name.substring(0,name.length - 1);
				} else {
				/*
					alert("请选择人员。");
					return ;
					*/
				}
				ret[0] = id;
				var ids = id.split(",");
				if(ids.length>20){
					alert("最多可以设置20位领导人。");
					return ;
				}
				ret[1] = name;
				window.returnValue = ret;
				window.close();
			}
			//伪CHECKBOX的单击事件.
			function chkclick(item){
				
			}
		</script>
		</head>
		<body>
        <DIV cellpadding="0" style="overflow:hidden;">
          <div align="center">
         
            <table width="100%" border="0" cellspacing="0" cellpadding="00" height="44px;"  class="table_headtd" style="vertical-align: top;">
              <tr>
                <td class="table_headtd_img"><img src="<%=frameroot%>/images/ico/ico.gif"></td>
                <td align="left"><strong>请选择分管领导</strong></td>
                 <td align="right">
                
                <table border="0" align="right" cellpadding="00" cellspacing="0">
                    <tr>
                    <td width="7"><img src="<%=frameroot%>/images/ch_h_l.gif"/></td>
                    <td class="Operation_input" onClick="doSubmit()">&nbsp;确&nbsp;定&nbsp;</td>
                    <td width="7"><img src="<%=frameroot%>/images/ch_h_r.gif"/></td>
                    <td width="5"></td>
                    <td width="7"><img src="<%=frameroot%>/images/ch_z_l.gif"/></td>
                    <td class="Operation_input1" onClick="window.close()">&nbsp;取&nbsp;消&nbsp;</td>
                    <td width="7"><img src="<%=frameroot%>/images/ch_z_r.gif"/></td>
                    <td width="6"></td>
                  </tr>
                  </table>
               </td>
              </tr>
            </table>
            <div style="height:458px;  overflow-y:auto; overflow-x:hidden; widows:100%">
              <div style="padding:0 20px; text-align: left;">
                <tree:strongbigtree title="<%=title%>" data="<%=data%>" dealclass="com.strongit.oa.common.tree.TreeImpl" oncheckboxclick="chkclick" check="<%=chk%>"/>
              </div>
            </div>
          </div>
        </DIV>
</body>
</html>
