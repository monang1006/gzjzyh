<%@page language="java" contentType="text/html; charset=UTF-8"  pageEncoding="UTF-8"%>
<%@taglib uri="/tags/web-flex" prefix="webflex"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<%@ taglib uri="/tags/security" prefix="security"%>
<%@taglib uri="/tags/web-newdate" prefix="strong"%>
<%@include file="/common/include/rootPath.jsp"%>
<html>
  <head>
    <%@include file="/common/include/meta.jsp"%>
    <title>每日要情列表</title>
    <link href="<%=frameroot%>/css/strongitmenu.css" type="text/css"
      rel="stylesheet">
    <link href="<%=frameroot%>/css/properties_windows.css"
      type="text/css" rel="stylesheet">
    <link href="<%=frameroot%>/css/search.css" type="text/css"
      rel="stylesheet">   
    <script language='javascript'
      src='<%=path%>/common/js/grid/ChangeWidthTable.js'
      type="text/javascript"></script>
    <script src="<%=path%>/common/js/jquery/jquery-1.2.6.js"
      type="text/javascript"></script>
    <script language="javascript" src="<%=path%>/common/js/menu/menu.js"
      type="text/javascript"></script>    
    <script src="<%=path%>/common/js/common/common.js"
      type="text/javascript"></script>
    <script src="<%=path%>/common/js/common/search.js"
      type="text/javascript"></script>
    <style media="screen" type="text/css">
    .tabletitle {
      FILTER:progid:DXImageTransform.Microsoft.Gradient(
                            gradientType = 0, 
                            startColorStr = #ededed, 
                            endColorStr = #ffffff);
    }
    
    .hand {
      cursor:pointer;
    }
    </style>
    <security:authorize ifAllGranted="001-000100050006">
    <script type="text/javascript">
     
    function open(){
    	var issId = getValue();
		var ret = OpenWindow("<%=path%>/senddoc/sendDoc!getYQ.action?issId="+issId,1000, 800, window);
	}
    </script>
    </security:authorize>
    
    <security:authorize ifAllGranted="001-000100050007">
    <script type="text/javascript">
     
    function open1(){
    	var issId = getValue();
    	var ret = OpenWindow("<%=path%>/senddoc/sendDoc!getYQ.action?issId="+issId+"&flag=success",1000, 800, window);
	}
    </script>
    </security:authorize>
  </head>
  <body class="contentbodymargin" oncontextmenu="return false;" onload=initMenuT() >
	<script type="text/javascript" src="<%=path%>/common/js/newdate/WdatePicker.js"></script>
    <div id="contentborder" align="center">
      <table width="100%" border="0" cellspacing="0" cellpadding="0" style="vertical-align: top;">
        <tr>
          <td height="40" class="tabletitle">
            <table width="100%" border="0" align="right" cellpadding="0" cellspacing="0">
              <tr>
                <td>
                </td>
                <td width="30%">
                 <img src="<%=frameroot%>/images/ico.gif" width="9" height="9"
                    alt="">&nbsp;
                  每日要情列表
                </td>
                <td>
                  &nbsp;
                </td>
                 <td width="70%"><table align="right"><tr>
	                <td>
	                <security:authorize ifAllGranted="001-000100050006">
	                  <a class="Operation" href="javascript:open();"><img
	                                    src="<%=frameroot%>/images/chakan.gif" width="15"
	                                    height="15" class="img_s">批示要情</a>
	                </security:authorize>
	                <security:authorize ifAllGranted="001-000100050007">
	                <a class="Operation" href="javascript:open1();"><img
	                                    src="<%=frameroot%>/images/chakan.gif" width="15"
	                                    height="15" class="img_s">查看要情</a>
	                </security:authorize>
	                </td>
	                <td width="5">
                	</td>
                	</tr></table></td>
              </tr>
            </table>
          </td>
        </tr>
        <tr>
          <td height="100%">
          <s:form id="myTableForm" action="/senddoc/sendDoc!showListYQ.action?flag1=success">
            <webflex:flexTable name="myTable" width="100%" height="200px"
              wholeCss="table1" property="0" isCanDrag="true"
              isCanFixUpCol="true" clickColor="#A9B2CA" footShow="showCheck"
              getValueType="getValueByArray" collection="${articles.result}"
              page="${articles}" pagename="articles">
              <webflex:flexCheckBoxCol caption="选择" valuepos="0" valueshowpos="1"
                width="3%" isCheckAll="true"
                isCanDrag="false" isCanSort="false"></webflex:flexCheckBoxCol>
              <webflex:flexTextCol caption="标题" showsize="30" valuepos="0" valueshowpos="1" isCanDrag="true"
                width="47%" isCanSort="true"></webflex:flexTextCol>
              <webflex:flexTextCol caption="创建时间" valuepos="2" valueshowpos="2" isCanDrag="true"
                width="50%" isCanSort="true"></webflex:flexTextCol>
            </webflex:flexTable>
            
            <script language="javascript">
			var sMenu = new Menu();
			function initMenuT(){
				sMenu.registerToDoc(sMenu);
				var item = null;
				<security:authorize ifAllGranted="001-000100050006">
				item = new MenuItem("<%=root%>/images/ico/tianjia.gif","批示要情","open",3,"ChangeWidthTable","checkOneDis");
				sMenu.addItem(item);
				</security:authorize>
				<security:authorize ifAllGranted="001-000100050007">
				item = new MenuItem("<%=root%>/images/ico/tianjia.gif","查看要情","open1",3,"ChangeWidthTable","checkOneDis");
				sMenu.addItem(item);
				</security:authorize>
				sMenu.addShowType("ChangeWidthTable");
			    registerMenu(sMenu);
			}
		</script>
            </s:form>
          </td>
        </tr>
      </table>
    </div>
  </body>
</html>
