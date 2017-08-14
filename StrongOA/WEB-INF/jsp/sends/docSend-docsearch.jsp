<%@page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@taglib uri="/tags/web-flex" prefix="webflex"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<%@ taglib uri="/tags/security" prefix="security"%>
<%@taglib uri="/tags/web-newdate" prefix="strong"%>
<%@include file="/common/include/rootPath.jsp"%>
<html>
	<head>
		<%@include file="/common/include/meta.jsp"%>
		<title>公文查询列表</title>
		<link href="<%=frameroot%>/css/strongitmenu.css" type="text/css"
			rel="stylesheet">
		<link href="<%=frameroot%>/css/properties_windows.css" type="text/css"
			rel="stylesheet">
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
	FILTER: progid : DXImageTransform.Microsoft.Gradient ( 
                             gradientType =   0, startColorStr =  
		#ededed, endColorStr =   #ffffff );
}

.hand {
	cursor: pointer;
}
</style>
		<script type="text/javascript">
      
        //查看文档
        function viewDoc() {         
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
      
		//刷新公文分发情况列表
		function viewDocSend(){
			var id = getValue();
			if(""==id|null==id){
				alert("请选择一个公文!");
				return;
			}
			if(id.indexOf(",")>0){
				alert("一次只能查看一个公文的分发情况！");
				return;
			}
			window.parent.main_content.status_content.location = "<%=root%>/sends/docSend!sendslist.action?docId="+id;
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
			var boo=OpenWindow('<%=root%>/sends/docSend!orgTree.action?docId='+id, '550', '400', window);
			if(boo=="reload"){
				window.location ="<%=root%>/sends/docSend!docsearch.action";
				window.parent.main_content.status_content.location = "<%=root%>/sends/docSend!sendslist.action?docId="+id;
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

			var url = "<%=path%>/sends/docSend!viewDoc.action?docId="+id+"&showType=view";
			var width=screen.availWidth-10;
			var height=screen.availHeight-30;
			var a=OpenWindow(url,width, height, window);
			if(a=="reload"){
				window.location ="<%=root%>/sends/docSend!docsearch.action"
				window.parent.main_content.status_content.location = "<%=root%>/sends/docSend!sendslist.action?docId="+id;
			}
		}
      function getinfo(id){
           	var url = "<%=path%>/sends/docSend!viewDoc.action?docId="+id+"&showType=view";
			var width=screen.availWidth-10;
			var height=screen.availHeight-30;
			var a=OpenWindow(url,width, height, window);
			if(a=="reload"){
				window.location ="<%=root%>/sends/docSend!docsearch.action"
				window.parent.main_content.status_content.location = "<%=root%>/sends/docSend!sendslist.action?docId="+id;
			}
      }
    
    //回收公文  
		function recycleDoc(){
			var id = getValue();
			if(""==id|null==id){
				alert("请选择一个公文!");
				return;
			}
			if(id.indexOf(",")>0){
				alert("一次只能回收一个公文！");
				return;
			}
			$.post(scriptroot + "/sends/docSend!isHasRecycleDoc.action",
				{"docId":id},
				function(ret){
				if(ret == "1"){
					alert("公文已回收！");
					
				}else{	
					$.post(scriptroot + "/sends/docSend!isHasRecycleDocByDocIds.action",
	          		{"docId":id},
	          		function(myvalue){
          				if(myvalue=="0"){
          					alert("公文已被签收或拒收，不允许回收操作！");
          					return;
          				}else{
          					if(!confirm("您确定要回收该公文?")){
								return;
							}
							$.post(scriptroot + "/sends/docSend!recycleDoc.action",
								{"docId":id},
								function(ret){
								if(ret == "0"){
									alert("公文回收成功！");
									window.parent.main_content.status_content.location = "<%=root%>/sends/docSend!sendslist.action";
									window.location ="<%=root%>/sends/docSend!docsearch.action"
								}else if(ret == "-1"){
				   					alert("对不起，操作失败，请与管理员联系。");
				   					return ;
				   				}else{
				   					alert(ret);
				   					return ;
				   				}
							});
          				}
          			});
   				}
			});
			
		}
      
      
    //快速查看分发信息
      function getInfo(id){
    	//去除选中
    	var checkobjs=document.getElementsByName("chkButton");
		for(var i=0;i<checkobjs.length;i++){
			if(checkobjs[i].checked==true){
				checkobjs[i].checked=obj.checked;
			}
			//checkValue(checkobjs[i],tdobj,color,flag);
		}
    	var taskId = getValue();
    	var t = taskId.indexOf(",");
    	if(t!=-1){
    		taskId = "";
    	}
    	//单击选中行
    	clickChecked(id);
      	window.parent.main_content.status_content.location = "<%=root%>/sends/docSend!sendslist.action?docId="+id.value;
      }
      
       function showState(state,id){
		    var str;
			if(state == "2"){
				str = "待分发";
			}else if(state == "3"){
				str = "<font color='gray'>已分发</font>";
			}else if(state == "1"){
				str = "<font color='red'>待签章提交</font>";
			}else if(state == "4"){
				str = "<font color='red'>退回</font>";
			}else if(state == "5"){
				str = "<font color='blue'>已回收</font>";
			}
			return str;
		}
       
       
      //归档
       function gd(){
       	var taskId = getValue();
       	if(taskId == ""){
          	alert("请选择要归档的公文！");
          	return ;
          }else{
          	var taskIds = taskId.split(",");
          	if(taskIds.length>1){
          		alert("一次只能归档一份公文！");
          		return ;
          	}else{
          	//alert(scriptroot + "/sends/docSend!isHasRecycleDocByDocId.action");
          	$.post(scriptroot + "/sends/docSend!isHasRecycleDocByDocId.action",
          		{"docId":taskId},
          		function(myvalue){
          			if(myvalue=="0"){
          				alert("还有机构未接收公文，不能归档！");
          			}else{
          		//不用再填入文档编号，直接以当前时间代替	
          	    var now = new Date();
				var result = now.getFullYear() + "/" + now.getMonth() + "/" +  now.getDate() + " " + now.toLocaleTimeString();
				
          		$.post("<%=path%>/receives/archive/archiveDoc!isHasSendDoc.action",
                 	{"docId":taskId},
                   function(data){                	
                      if(data.indexOf("archiveId")!=-1){
                      	if(confirm("档案中已存在当前公文【文号】，确认是否归档？\n提示：确定归档将覆盖档案中相同文号的公文.")){	
                      		var archiveIdArr=data.split(":");
                      		var archiveId=archiveIdArr[1];
                      		
                      		//url = "<%=path%>/fileNameRedirectAction.action?toPage=sends/docSend-fileNo.jsp";
			          		//var fileNo = OpenWindow(url,window,'help:no;status:no;scroll:auto;dialogWidth:250px; dialogHeight:180px'); 
			          		//不用再填入文档编号，直接以当前时间代替	
			          		var fileNo = result;
			          		
			          		if(fileNo!=null&&fileNo!=""){
								location = "<%=path%>/receives/archive/archiveDoc!auditSend.action?docId="+taskId+"&fileNo="+encodeURI(encodeURI(fileNo))+"&archiveId="+archiveId;
			          		}
			          		
			          	}    			
                      }else if(data=="1"){
                      		alert("所选公文已归档！");  
                      		return;
                      }
                      else if(data=="2"){
                      		alert("所选公文不存在！"); 
                      		return; 
                      }else{									
		          		//url = "<%=path%>/fileNameRedirectAction.action?toPage=sends/docSend-fileNo.jsp";
		          		//var fileNo = OpenWindow(url,window,'help:no;status:no;scroll:auto;dialogWidth:250px; dialogHeight:180px'); 
			            //不用再填入文档编号，直接以当前时间代替	
			            var fileNo = result;
		          		if(fileNo!=null&&fileNo!=""){
							location = "<%=path%>/receives/archive/archiveDoc!auditSend.action?docId="+taskId+"&fileNo=" + encodeURI(encodeURI(fileNo));
		          		}	
                      }
                   });
                   }
          	});
          }  
       }  
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
	<body class="contentbodymargin" oncontextmenu="return false;"
		onload="initMenuT();">
		<script type="text/javascript"
			src="<%=path%>/common/js/newdate/WdatePicker.js"></script>
		<div id="contentborder" align="center">
			<table width="100%" border="0" cellspacing="0" cellpadding="0"
				style="vertical-align: top;">
				<tr>
					<td height="40" class="tabletitle">
						<table width="100%" border="0" align="right" cellpadding="0"
							cellspacing="0">
							<tr>
								<td>
									&nbsp;
								</td>
								<td width="30%">
									<img src="<%=frameroot%>/images/ico.gif" width="9" height="9"
										alt="">
									&nbsp; 公文查询列表
								</td>
								<td>
									&nbsp;
								</td>
								<td width="70%">
									<table align="right">
										<tr>
											<td>
												<a class="Operation" href="javascript:viewDoc();"><img
														src="<%=frameroot%>/images/chakan.gif" width="15"
														height="15" class="img_s">查看公文</a>
											</td>
											<%--<td width="5"></td>
	                 <td>
	                  <a class="Operation" href="javascript:sendDoc();"><img
	                                    src="<%=frameroot%>/images/songshen.gif" width="15"
	                                    height="15" class="img_s">分发</a>
	                </td>--%>
											<td width="5">
											</td>
											<td>
												<a class="Operation" href="javascript:recycleDoc();"><img
														src="<%=frameroot%>/images/shangbian.gif" width="15"
														height="15" class="img_s">回收</a>
											</td>
											<td width="5">
											</td>
											<td>
												<a class="Operation" href="javascript:gd();"><img
														src="<%=frameroot%>/images/weituo.gif" width="15"
														height="15" class="img_s">归档</a>
											</td>
											<td width="5">
											</td>
										</tr>
									</table>
								</td>
							</tr>
						</table>
					</td>
				</tr>
				<tr>
					<td height="100%">
						<s:form id="myTableForm" action="/sends/docSend!docsearch.action">
							<webflex:flexTable name="myTable" width="100%" height="200px"
								wholeCss="table1" property="0" isCanDrag="true" pageSize="10" 
								isCanFixUpCol="true" clickColor="#A9B2CA" footShow="showCheck" 
								onclick="getInfo(this)" getValueType="getValueByArray"
								collection="${page.result}" page="${page}">
								<table width="100%" border="0" cellpadding="0" cellspacing="1"
									class="table1">
									<tr>
										<td width="40px" align="center" class="biao_bg1" id="img_sousuo" style="cursor: hand;">
											<img src="<%=frameroot%>/images/sousuo.gif"  width="17" height="16">
										</td>
										<td width="40%" class="biao_bg1">
											<s:textfield name="docModel.docTitle" cssClass="search"
												title="请输入公文标题"></s:textfield>
										</td>
										<td width="23%" class="biao_bg1">
											<s:textfield name="docModel.docCode" cssClass="search"
												title="请输入发文文号"></s:textfield>
										</td>
										<td width="6%" class="biao_bg1">
											<s:select onchange="doSubmit()" cssStyle="width:100%"
												list="jjcdItems" headerKey="" headerValue="全部"
												listKey="dictItemName" listValue="dictItemName"
												name="docModel.docEmergency"></s:select>
										</td>
										<td width="10%" align="center" class="biao_bg1">
											<strong:newdate name="startDate" id="startDate" width="98%"
												skin="whyGreen" isicon="true" dateform="yyyy-MM-dd"></strong:newdate>
										</td>
										<td width="9%" align="center" class="biao_bg1">
											<strong:newdate name="endDate" id="endDate" width="98%"
												skin="whyGreen" isicon="true" dateform="yyyy-MM-dd"></strong:newdate>
										</td>
										<%--
                  <td width="10%" class="biao_bg1">
                   	<s:select onchange="doSubmit()" cssStyle="width:100%" name="docModel.docState" list="#{'1':'待签章提交','2':'待分发','3':'已分发'}" headerKey="" headerValue="全部" listKey="key" listValue="value"></s:select>
                  </td>
                  --%>
										<td class="biao_bg1">
											&nbsp;
										</td>
									</tr>
								</table>
								<webflex:flexCheckBoxCol caption="选择" valuepos="0"
									valueshowpos="1" width="3%" isCheckAll="true" isCanDrag="false"
									isCanSort="false"></webflex:flexCheckBoxCol>
								<webflex:flexTextCol caption="公文标题" showsize="30" valuepos="0"
									valueshowpos="1" isCanDrag="true" width="41%" isCanSort="true"
									 ></webflex:flexTextCol>
								<webflex:flexTextCol caption="发文文号" valuepos="2"
									valueshowpos="2" isCanDrag="true" width="24%" isCanSort="true"></webflex:flexTextCol>
								<webflex:flexTextCol caption="紧急程度" valuepos="3"
									valueshowpos="3" isCanDrag="true" width="6%" isCanSort="true"
									align="center"></webflex:flexTextCol>
								<webflex:flexDateCol caption="创建时间" valuepos="5"
									valueshowpos="5" width="20%" isCanDrag="true"
									dateFormat="yyyy-MM-dd hh:mm" isCanSort="true"></webflex:flexDateCol>
								<webflex:flexTextCol caption="公文状态" valuepos="9"
									valueshowpos="javascript:showState(9,0)" isCanDrag="true"
									width="6%" isCanSort="true" align="center"></webflex:flexTextCol>
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
	        item = new MenuItem("<%=frameroot%>/images/chakan.gif","查看公文","viewDoc",1,"ChangeWidthTable","checkMoreDis");
	        //sMenu.addItem(item);
	        //item = new MenuItem("<%=frameroot%>/images/songshen.gif","分发","sendDoc",1,"ChangeWidthTable","checkOneDis");
	        sMenu.addItem(item);
	        item = new MenuItem("<%=frameroot%>/images/shangbian.gif","回收","recycleDoc",1,"ChangeWidthTable","checkOneDis");
	        sMenu.addItem(item);
	         item = new MenuItem(" <%=frameroot%>/images/weituo.gif","归档","gd",1,"ChangeWidthTable","checkOneDis");
	        sMenu.addItem(item);
        sMenu.addShowType("ChangeWidthTable");
        registerMenu(sMenu);
      }
    </script>
	</body>
</html>
