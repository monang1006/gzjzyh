<%@page language="java" contentType="text/html; charset=UTF-8"  pageEncoding="UTF-8"%>
<%@taglib uri="/tags/web-flex" prefix="webflex"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<%@ taglib uri="/tags/security" prefix="security"%>
<%@taglib uri="/tags/web-newdate" prefix="strong"%>
<%@include file="/common/include/rootPath.jsp"%>
<html>
  <head>
    <%@include file="/common/include/meta.jsp"%>
    <title>公文列表</title>
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
    	 function showState(state,id){
		    var str;
			if(state == "0"){
				str = "草稿";
			}else if(state == "4"){
				str = "<a href='#' title='查看退回原因' onclick=\"javascript:viewReason('"+id+"')\"><font color='red'>退回</font></a>";
				//str += "&nbsp;<input type='button' class='input_bg' value='退回' onclick=\"javascript:viewReason('"+id+"')\" />";
			}else if(state == "5"){
				str = "<font color='blue'>回收</font>";
			}
			return str;
		}
    
      //查看退回原因
      function viewReason(id){
      	OpenWindow("<%=root%>/sends/transDoc!viewBackReason.action?model.docId="+id+"&timestamp="+new Date(),"400","300",window);
      }
    
      //添加文档
      function newDoc(){
          	var width=screen.availWidth-10;
   			var height=screen.availHeight-30;
   			//var ret=OpenWindow("<%=root%>/sends/transDoc!input.action",width, height, window);
   			//这里要使用最小化按钮
   			var ret = window.open("<%=root%>/sends/transDoc!input.action","newWindow","height=" +height+",width="+width +",top=0,left=0,toolbar=no,menubar=no,scrollbars=no, resizable=no,location=no, status=no");
   			if(ret){
   				if(ret == "0"){
   					location = "<%=root%>/sends/transDoc.action";
   				}
   			}
      }
      
      //编辑文档
      function editDoc() {         
       		 var bussinessId = getValue();
       		 if(bussinessId == ""){
       		 	alert("请选择要修改的公文！");
       		 	return ;
       		 }else{
       		 	var docIds = bussinessId.split(",");
       		 	if(docIds.length>1){
       		 		alert("一次只能修改一份公文！");
       		 		return ;
       		 	}
       		 }
       		var width=screen.availWidth-10;
   			var height=screen.availHeight-30;
   			//var ret=OpenWindow("<%=root%>/sends/transDoc!input.action?model.docId="+bussinessId+"&timestamp="+new Date(),width, height, window); 
   			//window.open("<%=root%>/sends/transDoc!input.action?model.docId="+bussinessId+"&timestamp="+new Date(),width, height, window); 
   			window.open("<%=root%>/sends/transDoc!input.action?model.docId="+bussinessId+"&timestamp="+new Date(),"newWindow","height=" +height+",width="+width +",top=0,left=0,toolbar=no,menubar=no,scrollbars=no, resizable=no,location=no, status=no");
   			//if(ret){
   			//	if(ret == "0"){
   			//		location = "<%=root%>/sends/transDoc.action";
   			//	}
   			//} 
      }
      
      //删除文档
      function deleteDoc() {
          var bussinessId = getValue();
          if (bussinessId != "") {
          	  if(confirm("删除选定的公文，确定？")){
	              location = "<%=root%>/sends/transDoc!delete.action?model.docId="+bussinessId;
          	  }
          } else {
              alert("请选择要删除的公文！");
          }
      }
      
      //公文提交
      function sendDoc() {
		 var bussinessId = getValue();
		 if (bussinessId != "") {
			  var url = "<%=root%>/sends/transDoc!listValue.action?formId="+bussinessId;
          	  if(confirm("提交选定的公文，确定？")){
	              location = "<%=root%>/sends/transDoc!submit.action?model.docId="+bussinessId;
        	  }
         } else {
             alert("请选择要提交的公文！");
         }
     }
		//提交表单
	  function doSubmit(){
	  	$("form").submit();
	  }	

       $(document).ready(function(){
        $("#img_sousuo").click(function(){
        	$("form").submit();
        });     
      });
      
    </script>
  </head>
  <body class="contentbodymargin" oncontextmenu="return false;" onload=initMenuT()>
	  <script type="text/javascript"
	      src="<%=path%>/common/js/newdate/WdatePicker.js"></script>
    <div id="contentborder" align="center">
      <table width="100%" border="0" cellspacing="0" cellpadding="0" style="vertical-align: top;">
        <tr>
          <td height="40" class="tabletitle">
            <table width="100%" border="0" align="right" cellpadding="0" cellspacing="0">
              <tr>
                <td width="5%" align="center">
                  <img src="<%=frameroot%>/images/ico.gif" width="9" height="9"
                    alt="">
                </td>
                <td width="30%">
                  发文草稿列表
                </td>
                <td>
                  &nbsp;
                </td>
                <td width="70%"><table align="right"><tr>
	                <td>
	                  <a class="Operation" href="javascript:newDoc();"><img
	                                    src="<%=frameroot%>/images/tianjia.gif" width="15"
	                                    height="15" class="img_s">草拟公文</a>
	                </td>
	                <td width="5"></td>
	                <td>
	                  <a class="Operation" href="javascript:sendDoc();"><img
	                                    src="<%=frameroot%>/images/songshen.gif" width="15"
	                                    height="15" class="img_s">提交</a>
	                </td>
	                <td width="5">
                	</td>
	                <td>
										<a class="Operation" href="javascript:editDoc();"><img
												src="<%=frameroot%>/images/bianji.gif" width="15"
												height="15" class="img_s">修改</a>
									</td>
	                <td width="5"></td>
	                <td>
	                  <a class="Operation" href="javascript:deleteDoc();"><img
	                                    src="<%=frameroot%>/images/shanchu.gif" width="15"
	                                    height="15" class="img_s">删除</a>
	                </td>
	                <td width="5"></td>
	                </tr></table></td>
              </tr>
            </table>
          </td>
        </tr>
        <tr>
          <td height="100%">
          <s:form id="myTableForm" action="/sends/transDoc.action">
            <webflex:flexTable name="myTable" width="100%" height="200px"
              wholeCss="table1" property="0" isCanDrag="true"
              isCanFixUpCol="true" clickColor="#A9B2CA" footShow="showCheck"
              getValueType="getValueByArray" collection="${page.result}"
              page="${page}">
              <table width="100%" border="0" cellpadding="0" cellspacing="1"
                class="table1">
                <tr>
                  <td width="40px" align="center" class="biao_bg1" id="img_sousuo"  style="cursor: hand;" >
                    <img src="<%=frameroot%>/images/sousuo.gif"   width="17"
                      height="16">
                  </td>
                  <td width="41%" class="biao_bg1">
                    <s:textfield name="model.docTitle" cssClass="search" title="请输入公文标题"></s:textfield>  
                  </td>
                  <td width="20%" class="biao_bg1">
                    <s:textfield name="model.docCode" cssClass="search" title="请输入发文文号"></s:textfield>  
                  </td>
                  <td width="8%" class="biao_bg1">
                  	<s:select onchange="doSubmit()" cssStyle="width:100%" list="jjcdItems" headerKey="" headerValue="全部" listKey="dictItemName" listValue="dictItemName" name="model.docEmergency"></s:select>
                  </td>
                  <td width="10%" align="center" class="biao_bg1" >
                  <input name="startDate" title="创建开始日期" value = "${startStr}" class="Wdate search" id="searchStartDate" style="width: 98%; height: 22px;" onclick="WdatePicker({dateFmt:'yyyy-MM-dd',skin:'whyGreen'})" type="text" readOnly="readonly"/>
                  </td>
                  <td width="9%" align="center" class="biao_bg1" >
                   <input name="endDate" title="创建结束日期" value = "${endStr}"  class="Wdate search" id="searchEndtDate" style="width: 98%; height: 22px;" onclick="WdatePicker({dateFmt:'yyyy-MM-dd',skin:'whyGreen'})" type="text" readOnly="readonly"/>
                  </td>
                  <td width="8%" class="biao_bg1">
                   	<s:select onchange="doSubmit()" cssStyle="width:100%" name="model.docState" list="#{0:'草稿',4:'退回',5:'回收'}"   listKey="key" listValue="value"  headerKey="" headerValue="全部"></s:select>
                  </td>
                  <td class="biao_bg1">
                    &nbsp;
                  </td>
                </tr>
              </table>
              <webflex:flexCheckBoxCol caption="选择" valuepos="0" valueshowpos="1"
                width="3%" isCheckAll="true"
                isCanDrag="false" isCanSort="false"></webflex:flexCheckBoxCol>
              <webflex:flexTextCol caption="公文标题" showsize="30" valuepos="0" valueshowpos="1" isCanDrag="true"
                width="42%" isCanSort="true"></webflex:flexTextCol>
              <webflex:flexTextCol caption="发文文号" valuepos="2" valueshowpos="2" isCanDrag="true"
                width="21%" isCanSort="true"></webflex:flexTextCol>
              <webflex:flexTextCol caption="紧急程度" valuepos="3" valueshowpos="3" isCanDrag="true"
                width="8%" isCanSort="true"></webflex:flexTextCol>
              <webflex:flexDateCol caption="创建日期"
                valuepos="5" valueshowpos="5"
                width="20%" isCanDrag="true" dateFormat="yyyy-MM-dd hh:mm" isCanSort="true"></webflex:flexDateCol>  
              <webflex:flexTextCol caption="公文状态" valuepos="9" valueshowpos="javascript:showState(9,0)" isCanDrag="true"
                width="6%" isCanSort="true"></webflex:flexTextCol>   
            </webflex:flexTable>
            </s:form>
          </td>
        </tr>
      </table>
    </div>
    <script language="javascript">
      var sMenu = new Menu();
      function initMenuT(){
        sMenu.registerToDoc(sMenu);
        var item = null;
	        item = new MenuItem("<%=frameroot%>/images/tianjia.gif","草拟公文","newDoc",1,"ChangeWidthTable","checkMoreDis");
	        sMenu.addItem(item);
	        item = new MenuItem("<%=frameroot%>/images/songshen.gif","提交","sendDoc",1,"ChangeWidthTable","checkOneDis");
	        sMenu.addItem(item);
	        item = new MenuItem("<%=frameroot%>/images/bianji.gif","修改","editDoc",1,"ChangeWidthTable","checkMoreDis");
	        sMenu.addItem(item);
	        item = new MenuItem("<%=frameroot%>/images/shanchu.gif","删除","deleteDoc",1,"ChangeWidthTable","checkOneDis");
	        sMenu.addItem(item);
        sMenu.addShowType("ChangeWidthTable");
        registerMenu(sMenu);
      }
    </script>
  </body>
</html>
