<%@page language="java" contentType="text/html; charset=UTF-8"  pageEncoding="UTF-8"%>
<%@taglib uri="/tags/web-flex" prefix="webflex"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<%@ taglib uri="/tags/security" prefix="security"%>
<%@taglib uri="/tags/web-newdate" prefix="strong"%>
<%@include file="/common/include/rootPath.jsp"%>
<html>
  <head>
    <%@include file="/common/include/meta.jsp"%>
    <title>未签章公文列表</title>
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
    
    	function showState(state){
		    var str;
			if(state != "null" && state != null){
				str = "已签章";
			}else{
				str = "<font color='red'>未签章</font>";
			}
			return str;
		}
    
      //签章
      function signDoc() {         
         var bussinessId = getValue();
   		 if(bussinessId == ""){
   		 	alert("请选择要签章的公文！");
   		 	return ;
   		 }else{
   		 	var docIds = bussinessId.split(",");
   		 	if(docIds.length>1){
   		 		alert("一次只能签章一份公文！");
   		 		return ;
   		 	}
   		 }
   		var width=screen.availWidth-10;
		var height=screen.availHeight-30;
		OpenWin("<%=root%>/sends/transDoc!initSign.action?model.docId="+bussinessId+"&timestamp="+new Date(),width, height, window); 
      }
      
      //提交
      function sendDoc() {
       	 var bussinessId = getValue();
  		 if (bussinessId != "") {
          	  //提交确认
			  if(confirm("您确认已经完成对公文签章并提交发文吗？\n单击“确定”继续，单击“取消”停止。")){
			  	  var ids = bussinessId.split(",");
			  	  for(var i=0;i<ids.length ;i++){
			       	  var info = getInfo(ids[i]);
			  	  	  if(info[0] == ""){
			  	  	  	alert("公文[" + info[1] + "]未签章，不能提交。");
			  	  	  	return ;
			  	  	  }else{
				  	  	  if(info[2] != "true"){
				  	  	  	alert("公文[" + info[1] + "]未使用签章锁定公文，不能提交。");
				  	  	  	return ;
				  	  	  }
			  	  	  }
			  	  }
	              location = "<%=root%>/sends/transDoc!submit.action?model.docId="+bussinessId+"&actionName=signReload";
			  }
          } else {
              alert("请选择要提交的公文！");
          }
      }
      
      //通过循环列表返回数据
      function getInfo(id){
      	var info = new Array();//存储需要返回的数据
      	<s:iterator value="page.result" var="sign">
      		if(id == '${sign[0]}'){
				info[0] = '${sign[7]}';	//签章人
				info[1] = '${sign[1]}'; //公文标题
				info[2] = '${sign[8]}'; //是否锁定印章
			}
      	</s:iterator>
      	return info ;
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
      function downLoad(){
		window.open ('<%=root%>/doc/sends/dianziqianzhang.rar','window');
	}
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
   			var ret = OpenWindow("<%=root%>/fileNameRedirectAction.action?toPage=sends/transDoc-initback.jsp&id="+bussinessId,"400","300",window);
			if(ret){
				var url = "<%=root%>/sends/docSend!tuiwen.action?docModel.docId="+bussinessId;
				
		   		$.post(url, function(data) {
					if(data == "0"){
						alert("退回成功！");
						location = "<%=root%>/sends/transDoc!sign.action";
					}else{
						alert("退回失败，请重新操作！");
					}
				});
			}else{
			//	alert("操作错误，请关闭后重新操作！");
			}
   		}
      }
    </script>
  </head>
  <body class="contentbodymargin" oncontextmenu="return false;" onload="initMenuT();">
	  <script type="text/javascript"
	      src="<%=path%>/common/js/newdate/WdatePicker.js"></script>
    <div id="contentborder" align="center">
      <table width="100%" border="0" cellspacing="0" cellpadding="0" style="vertical-align: top;">
        <tr>
          <td height="40" class="tabletitle">
            <table width="100%" border="0" align="right" cellpadding="0" cellspacing="0">
              <tr>
                <td >
                  &nbsp;
                </td>
                <td width="30%">
                <img src="<%=frameroot%>/images/ico.gif" width="9" height="9"
                    alt="">&nbsp;
                  发文列表
                </td>
                <td>
                  &nbsp;
                </td>
                <td width="70%"><table align="right"><tr>
                	<td>
	                 <a class="Operation" style="color: #FF3030;" width="15" height="15" href="javascript:downLoad()">下载电子签章</a>
	                </td>
	                <td width="200"></td>
	                <td>
	                  <a class="Operation" href="javascript:tuiwen();"><img
	                                    src="<%=frameroot%>/images/cexiao.gif" width="15"
	                                    height="15" class="img_s">退回</a>
	                </td>
	                <td width="5"></td>
	                <td>
	                  <a class="Operation" href="javascript:signDoc();"><img
	                                    src="<%=frameroot%>/images/03.gif" width="15"
	                                    height="15" class="img_s">签章</a>
	                </td>
	                <td width="5"></td>
	               </tr></table></td>
              </tr>
            </table>
          </td>
        </tr>
        <tr>
          <td height="100%">
          <s:form id="myTableForm" action="/sends/transDoc!sign.action">
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
                  <td width="43%" class="biao_bg1">
                    <s:textfield name="model.docTitle" cssClass="search" title="请输入公文标题"></s:textfield>  
                  </td>
                  <td width="20%" class="biao_bg1">
                    <s:textfield name="model.docCode" cssClass="search" title="请输入发文文号"></s:textfield>  
                  </td>
                  <td width="6%" class="biao_bg1">
                  	<s:select onchange="doSubmit()" cssStyle="width:100%" list="jjcdItems" headerKey="" headerValue="全部" listKey="dictItemName" listValue="dictItemName" name="model.docEmergency"></s:select>
                  </td>
                  <td width="6%" class="biao_bg1">
                   	<s:select onchange="doSubmit()" cssStyle="width:100%" name="model.docSealPeople" list="#{'0':'未签章','1':'已签章'}"  headerKey="" headerValue="全部" listKey="key" listValue="value"></s:select>
                  </td>
                  <td width="11%" align="center" class="biao_bg1">
                    <strong:newdate name="startDate" id="startDate"  width="98%"
                      skin="whyGreen" isicon="true"  dateform="yyyy-MM-dd" title='开始时间'></strong:newdate>
                  </td>
                  <td width="10%" align="center" class="biao_bg1">
                    <strong:newdate name="endDate" id="endDate" width="98%"
                      skin="whyGreen" isicon="true" dateform="yyyy-MM-dd" title='结束时间'></strong:newdate>
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
                width="44%" isCanSort="true"></webflex:flexTextCol>
              <webflex:flexTextCol caption="发文文号" valuepos="2" valueshowpos="2" isCanDrag="true"
                width="21%" isCanSort="true"></webflex:flexTextCol>
              <webflex:flexTextCol caption="紧急程度" valuepos="3" valueshowpos="3" isCanDrag="true"
                width="6%" isCanSort="true"></webflex:flexTextCol>
              <webflex:flexTextCol caption="状态" valuepos="7" valueshowpos="javascript:showState(7)" isCanDrag="true"
                width="6%" isCanSort="true"></webflex:flexTextCol>
              <webflex:flexDateCol caption="创建时间"
                valuepos="5" valueshowpos="5"
                width="20%" isCanDrag="true" dateFormat="yyyy-MM-dd hh:mm" isCanSort="true"></webflex:flexDateCol>  
            </webflex:flexTable>
            </s:form>
          </td>
        </tr>
      </table>
      <%--<iframe id='sign' style="display:" name='sign'
				src='<%=root%>/fileNameRedirectAction.action?toPage=sends/docSign.jsp'
				frameborder=0 scrolling=auto width='100%' height='10%'></iframe>--%>
    </div>
    <script language="javascript">
      var sMenu = new Menu();
      function initMenuT(){
        sMenu.registerToDoc(sMenu);
        var item = null;
        	item = new MenuItem("<%=frameroot%>/images/cexiao.gif","退回","tuiwen",1,"ChangeWidthTable","checkOneDis");
	        sMenu.addItem(item);
	        item = new MenuItem("<%=frameroot%>/images/03.gif","签章","signDoc",1,"ChangeWidthTable","checkOneDis");
	        sMenu.addItem(item);
	       // item = new MenuItem("<%=frameroot%>/images/songshen.gif","提交","sendDoc",1,"ChangeWidthTable","checkOneDis");
	       // sMenu.addItem(item);
        sMenu.addShowType("ChangeWidthTable");
        registerMenu(sMenu);
      }
    </script>
  </body>
</html>
