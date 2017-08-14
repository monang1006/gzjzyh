<%@page language="java" contentType="text/html; charset=UTF-8"  pageEncoding="UTF-8"%>
<%@taglib uri="/tags/web-flex" prefix="webflex"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<%@ taglib uri="/tags/security" prefix="security"%>
<%@taglib uri="/tags/web-newdate" prefix="strong"%>
<%@include file="/common/include/rootPath.jsp"%>
<html>
  <head>
    <%@include file="/common/include/meta.jsp"%>
    <title>公文分发列表</title>
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
      //退回到草稿
      function tuiwen() {         
         var bussinessId = getValue();
   		 if(bussinessId == ""){
   		 	alert("请选择要退文的公文！");
   		 	return ;
   		 }else{
   		 	var docIds = bussinessId.split(",");
   		 	if(docIds.length>1){
   		 		alert("一次只能退文一份公文！");
   		 		return ;
   		 	}
   		 }
   		if(confirm("确定退回公文？")){
   			var url = "<%=root%>/sends/docSend!tuiwen.action?docModel.docId="+bussinessId;
	   		$.post(url, function(data) {
				if(data == "0"){
					alert("退回成功！");
					window.location ="<%=root%>/sends/docSend!doclist.action";
				}
			});
   		}
      }
      //添加文档
      function newDoc(){
          	var width=screen.availWidth-10;
   			var height=screen.availHeight-30;
   			var ret=OpenWindow("<%=root%>/sends/transDoc!input.action",width, height, window);
   		//location = "<%=root%>/sends/transDoc!input.action" ;	
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
       		var info = getInfo();
         	var formId = info[0];
         	var businessName = info[1];
         	var width=screen.availWidth-10;
   			var height=screen.availHeight-30;
   			var ret=OpenWindow("<%=root%>/senddoc/sendDoc!input.action?bussinessId="+bussinessId+"&formId="+formId+"&businessName="+encodeURI(encodeURI(businessName)),width, height, window);
            if(ret){
            	if(ret == "OK"){
            		window.location = "<%=root%>/senddoc/sendDoc!draft.action";          	
            	}
            }                
            
      }
      
      //删除文档
      function deleteDoc() {
          var bussinessId = getValue();
          if (bussinessId != "") {
          	  if(confirm("删除选定的公文，确定？")){
	              location = "<%=root%>/senddoc/sendDoc!delete.action?bussinessId="+bussinessId+"&listMode=0";
          	  }
          } else {
              alert("请选择要删除的公文！");
          }
      }
      
      //查看公文
		function viewDoc(){
			var id = getValue();
			if(""==id|null==id){
				alert("请选择一个公文!");
				return;
			}
			if(id.indexOf(",")>0){
				alert("一次只能查看一个公文！");
				return;
			}

			//var url = "<%=path%>/sends/docSend!viewDoc.action?docId="+id+"&showType=view";
			var url = "<%=path%>/receives/recvDoc!showDoc.action?docId="+id+"&showType=view&tuiwen=true";
			var width=screen.availWidth-10;
			var height=screen.availHeight-30;
			var a=OpenWindow(url,width, height, window);
			if(a=="reload"){
				window.location ="<%=root%>/sends/docSend!doclist.action";
			}
		}
		
		function getinfo(id){
           	var url = "<%=path%>/receives/recvDoc!showDoc.action?docId="+id+"&showType=view&tuiwen=true";
			var width=screen.availWidth-10;
			var height=screen.availHeight-30;
			var a=OpenWindow(url,width, height, window);
			if(a=="reload"){
				window.location ="<%=root%>/sends/docSend!doclist.action";
			}
      }
      
      //公文分发
      function sendDoc() {
			var id = getValue();
			if(""==id|null==id){
				alert("请选择一个公文!");
				return;
			}
			if(id.indexOf(",")>0){
				alert("一次只能发送一个公文！");
				return;
			}
			//window.location = '<%=root%>/sends/docSend!orgTree.action?docId='+id;
			
			//下面是之前的需求
			//var boo=OpenWindow('<%=root%>/sends/docSend!orgTree.action?docId='+id, '600', '400', window);
			//if(boo=="reload"){
			//	window.location ="<%=root%>/sends/docSend!doclist.action";
			//}
			
			//2013-1-29   xiaolj   新需求修改之后的
			var url = "<%=path%>/sends/docSend!viewSendOrg.action?docId=" + id;
			var a = OpenWindow(url,'550', '400', window);
			if(a=="reload"){
				window.location ="<%=root%>/sends/docSend!doclist.action";
				//window.close();
			}
      }
      
      //得到列表信息
      function getInfo(){
      	var info = new Array();
      	var id = getValue();
      	<s:iterator value="page.result">
      		if(id == '${senddocId}'){
      			info[0] = '${senddocFormid}';
      			info[1] = '${senddocTitle}';
      		}
      	</s:iterator>
      	return info;
      }
      
      $(document).ready(function(){
        $("#img_sousuo").click(function(){
        	$("form").submit();
        });     
      });
      //提交表单
	  function doSubmit(){
	  	$("form").submit();
	  }	
    </script>
  </head>
  <body class="contentbodymargin" oncontextmenu="return false;" onload="initMenuT();">
	<script type="text/javascript" src="<%=path%>/common/js/newdate/WdatePicker.js"></script>
    <div id="contentborder" align="center">
      <table width="100%" border="0" cellspacing="0" cellpadding="0" style="vertical-align: top;">
        <tr>
          <td height="30" class="tabletitle">
            <table width="100%" border="0" align="right" cellpadding="0" cellspacing="0">
              <tr>
                <td>
                </td>
                <td width="30%">
                 <img src="<%=frameroot%>/images/ico.gif" width="9" height="9"
                    alt="">&nbsp;
                  发文分发列表
                </td>
                <td>
                  &nbsp;
                </td>
                 <td width="70%"><table align="right"><tr>
                 	<td>
	                  <a class="Operation" href="javascript:tuiwen();"><img
	                                    src="<%=frameroot%>/images/cexiao.gif" width="15"
	                                    height="15" class="img_s">退文</a>
	                </td>
	                <td width="5"></td>
	                <td>
	                  <a class="Operation" href="javascript:viewDoc();"><img
	                                    src="<%=frameroot%>/images/chakan.gif" width="15"
	                                    height="15" class="img_s">查看公文</a>
	                </td>
	                <td width="5">
                	</td>
	                <td>
	                  <a class="Operation" href="javascript:sendDoc();"><img
	                                    src="<%=frameroot%>/images/songshen.gif" width="15"
	                                    height="15" class="img_s">分发</a>
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
          <s:form id="myTableForm" action="/sends/docSend!doclist.action">
            <webflex:flexTable name="myTable" width="100%" height="200px"
              wholeCss="table1" property="0" isCanDrag="true"
              isCanFixUpCol="true" clickColor="#A9B2CA" footShow="showCheck"
              getValueType="getValueByArray" collection="${page.result}"
              page="${page}">
              <table width="100%" border="0" cellpadding="0" cellspacing="1"
                class="table1">
                <tr>
                  <td width="40px" align="center" class="biao_bg1" style="cursor: hand;"  id="img_sousuo" >
                    <img src="<%=frameroot%>/images/sousuo.gif"    width="17"
                      height="16">
                  </td>
                  <td width="43%" class="biao_bg1">
                    <s:textfield name="docModel.docTitle" cssClass="search" title="请输入公文标题"></s:textfield>  
                  </td>
                  <td width="22%" class="biao_bg1">
                    <s:textfield name="docModel.docCode" cssClass="search" title="请输入发文文号"></s:textfield>  
                  </td>
                  <td width="8%" class="biao_bg1">
                  	<s:select onchange="doSubmit()" cssStyle="width:100%" list="jjcdItems" headerKey="" headerValue="全部" listKey="dictItemName" listValue="dictItemName" name="docModel.docEmergency"></s:select>
                  </td>
                  <td width="12%" align="center" class="biao_bg1">
                   <input name="startDate" title="开始日期" class="Wdate search" id="searchDraftDate" style="width: 98%; height: 22px;" onclick="WdatePicker({dateFmt:'yyyy-MM-dd',skin:'whyGreen'})" type="text" readOnly="readonly"/>
                  </td>
                  <td width="11%" align="center" class="biao_bg1">
                   <input name="startDate" title="结束日期" class="Wdate search" id="searchDraftDate" style="width: 98%; height: 22px;" onclick="WdatePicker({dateFmt:'yyyy-MM-dd',skin:'whyGreen'})" type="text" readOnly="readonly"/>
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
                width="44%" isCanSort="true" onclick="getinfo(this.value)"></webflex:flexTextCol>
              <webflex:flexTextCol caption="发文文号" valuepos="2" valueshowpos="2" isCanDrag="true"
                width="23%" isCanSort="true"></webflex:flexTextCol>
              <webflex:flexTextCol caption="紧急程度" valuepos="3" valueshowpos="3" isCanDrag="true"
                width="8%" isCanSort="true"></webflex:flexTextCol>
              <webflex:flexDateCol caption="创建时间"
                valuepos="5" valueshowpos="5"
                width="22%" isCanDrag="true" dateFormat="yyyy-MM-dd hh:mm" isCanSort="true"></webflex:flexDateCol>  
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
        	item = new MenuItem("<%=frameroot%>/images/cexiao.gif","退回","tuiwen",1,"ChangeWidthTable","checkOneDis");
	        sMenu.addItem(item);
	        item = new MenuItem("<%=frameroot%>/images/chakan.gif","查看公文","viewDoc",1,"ChangeWidthTable","checkOneDis");
	        sMenu.addItem(item);
	        item = new MenuItem("<%=frameroot%>/images/songshen.gif","分发","sendDoc",1,"ChangeWidthTable","checkOneDis");
	        sMenu.addItem(item);
        sMenu.addShowType("ChangeWidthTable");
        registerMenu(sMenu);
      }
    </script>
  </body>
</html>
