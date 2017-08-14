<%@page language="java" contentType="text/html; charset=UTF-8"  pageEncoding="UTF-8"%>
<%@taglib uri="/tags/web-flex" prefix="webflex"%>
<%@taglib uri="/tags/web-newdate" prefix="strong"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<%@ taglib uri="/tags/security" prefix="security"%>
<%@ taglib uri="/tags/c.tld" prefix="c" %>
<%@include file="/common/include/rootPath.jsp"%>
<html>
  <head>
    <%@include file="/common/include/meta.jsp" %>
    <title>工作列表</title>
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
    <script type="text/javascript">
    
     function showimg(status){
			var rv = '' ;
			if(status == 'null'){
				rv = "<font color='#90036'>执行中</font>&nbsp&nbsp";
			}
			
			else {
				rv = "<font color='#63ad00'>已结束</font>&nbsp&nbsp";
			}
			
			return rv;
		}
	 
    	function openDoc() {
	        if(checkSelectedOneDis()){
	              var bd = getValue();
	              var info = getInfo(bd); 
	              var instanceId = info[0];
	              var bussinessId = info[1];
	              var formId = info[2];
	            //  parent.location = "<%=root%>/work/work!viewHostedBy.action?bussinessId="+bussinessId+
	              //           "&instanceId="+instanceId+"&formId="+formId;
		       		var width=screen.availWidth-10;;
	           		var height=screen.availHeight-30;
	          		var ReturnStr=OpenWindow("<%=root%>/work/work!displayworkview.action?bussinessId="+bussinessId+
		                         "&instanceId="+instanceId+"&formId="+formId, 
	                                   width, height, window);
	          }
	      }
	      
           function getInfo(id){
           	var info = new Array();
         	 <c:forEach items="${flowPage.result}" var="obj" varStatus="status">
         	 	var bid = '<c:out value="${obj[0]}"/>';
         	 	if(bid == id){
         	 		info[0] = '<c:out value="${obj[0]}"/>';//流程实例ID
         	 		info[1] = '<c:out value="${obj[5]}"/>';//业务数据ID
         	 		info[2] = '<c:out value="${obj[4]}"/>';//表单模板ID
         	 	}
         	 </c:forEach>
           	return info;
           }    
  
    //查看流程图
		    function workflowView(instanceId){  
		   // alert(instanceId);    
		        var width=screen.availWidth-10;;
		        var height=screen.availHeight-30;
		        var ReturnStr=OpenWindow("<%=root%>/work/work!PDImageView.action?instanceId="+instanceId, 
		                                   width, height, window);
		    }
    </script>
  </head>
  <body class="contentbodymargin" oncontextmenu="return false;" onload="initMenuT();">
  <script type="text/javascript"
      src="<%=path%>/common/js/newdate/WdatePicker.js"></script>
      <DIV id=contentborder align=center>
      <table width="100%" border="0" cellspacing="0" cellpadding="0" style="vertical-align: top;">
        <tr>
          <td height="100%">
          <s:form name="myTableForm" action="/work/work!querycontent.action" target="_self" >
          <input id="flow_id" name="flow_id" type="hidden" value="${flow_id }"/>
	       <input id="flow_status" name="flow_status" type="hidden" value="${flow_status }"/>
	       <input id="flow_query_type" name="flow_query_type" type="hidden" value="${flow_query_type }"/>
	       <input id="userId" name="userId" type="hidden" value="${userId }"/>
	      <strong:newdate name="prcs_date1" id="prcs_date1" 
                      skin="whyGreen" isicon="true" dateobj="${prcs_date1}" dateform="yyyy-MM-dd" width="0" ></strong:newdate> 
	     <strong:newdate name="prcs_date2" id="prcs_date2" 
                      skin="whyGreen" isicon="true" dateobj="${prcs_date2}" dateform="yyyy-MM-dd" width="0" ></strong:newdate> 
	       <input id="run_name" name="run_name" type="hidden" value="${run_name }"/>
	       
            <webflex:flexTable name="myTable" width="100%" height="200px"
              wholeCss="table1" property="aiPiId" isCanDrag="true"
              isCanFixUpCol="true" clickColor="#A9B2CA" footShow="showCheck"
              getValueType="getValueByArray" collection="${flowPage.result}"
              page="${flowPage}" pagename="flowPage">
             
              <webflex:flexCheckBoxCol caption="选择" valuepos="0" valueshowpos="1" width="5%" isCheckAll="true"
                isCanDrag="false" isCanSort="false"></webflex:flexCheckBoxCol>
               <webflex:flexTextCol caption="工作名称/文号" valuepos="1" valueshowpos="1" isCanDrag="true"
                width="40%" showsize="40" isCanSort="true"></webflex:flexTextCol>
                 <webflex:flexDateCol caption="结束时间"
                 valuepos="3" valueshowpos="3" width="0"  isCanDrag="true" isCanSort="true"></webflex:flexDateCol>
           
             
              <webflex:flexDateCol caption="开始时间"
                 valuepos="2" valueshowpos="2"
                width="25%" showsize="25" isCanDrag="true" isCanSort="true"></webflex:flexDateCol>
                 <webflex:flexTextCol caption="状态" showsize="25" valuepos="0" valueshowpos="javascript:showimg(3)" isCanDrag="true"
                width="15%" isCanSort="true"></webflex:flexTextCol>
                 <webflex:flexTextCol caption="操作" showsize="15" valuepos="0" valueshowpos="流程图" isCanDrag="true"
                width="15%" isCanSort="true" onclick="javascript:workflowView(this.value)"></webflex:flexTextCol> 
            </webflex:flexTable>
            </s:form>
          </td>
        </tr>
      </table>
   </DIV>
    <script language="javascript">
      var sMenu = new Menu();
      function initMenuT(){
       $("input:checkbox").parent().next().next().hide(); //隐藏第三列
        sMenu.registerToDoc(sMenu);
        var item = null;
       
	    item = new MenuItem("<%=root%>/images/ico/chakan.gif","查看","openDoc",1,"ChangeWidthTable","checkMoreDis");
	    sMenu.addItem(item);
	 
        sMenu.addShowType("ChangeWidthTable");
        registerMenu(sMenu);
      }    
    </script>
  
  </body>
</html>

              <!--<webflex:flexTextCol caption="状态" property="biState"
                showValue="biState" width="10%" isCanDrag="true"
                isCanSort="true"></webflex:flexTextCol>-->