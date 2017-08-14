<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<jsp:directive.page import="com.strongit.oa.common.user.IUserService"/>
<jsp:directive.page import="com.strongmvc.service.ServiceLocator"/>
<%@ taglib uri="/struts-tags" prefix="s"%>
<%@include file="/common/include/rootPath.jsp"%>
<%@include file="/common/OfficeControl/version.jsp"%>
<%
	IUserService userService = (IUserService)ServiceLocator.getService("userService");
	String userName = userService.getCurrentUser().getUserName();
%>
<html>
	<head>
		<%@include file="/common/include/meta.jsp"%>
		<!--<title>在线编辑附件，非只读文件可以点击工具栏上的保存按钮更新附件内容</title>-->
		<%
         String name = "文件查看";
         if(request.getParameter("name") != null){
         name = request.getParameter("name");
        }
          %>
        <title><%=name%></title>
		<LINK href="<%=frameroot%>/css/properties_windows.css"
			type=text/css rel=stylesheet>
		<script src="<%=path%>/common/js/jquery/jquery-1.2.6.js"
			type="text/javascript"></script>
		<script src="<%=path%>/common/OfficeControl/officecontrol.js" type="text/javascript"></script>
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
 
      function saveDoc() {
	        var TANGER_OCX_OBJ = document.getElementById("TANGER_OCX_OBJ");
	        var ret = TANGER_OCX_OBJ.SaveToURL($("#doctempForm").attr("action"),
	                                           "wordDoc",
	                                           "bussinessId=<%=request.getParameter("bussinessId")%>",
	                                           "新建文档",
	                                           "doctempForm");
	        if (ret == "0") {
	        	//alert("保存文档成功！");
	        	TANGER_OCX_OBJ.ShowTipMessage("信息提示","附件内容更新成功。",false);
                window.returnValue = "OK";
                window.close();
	        } else {
	           TANGER_OCX_OBJ.ShowTipMessage("信息提示","附件内容更新失败。",false);
	        }
      }
      
      //打开文档
      function openFromURL(bussinessId,tableNames,idName) {
          var TANGER_OCX_OBJ = document.getElementById("TANGER_OCX_OBJ");
          var readOnly = "<%=request.getParameter("readOnly")%>";    
          TANGER_OCX_OBJ.OpenFromURL("<%=root%>/senddoc/sendDoc!openWordDocFromUrls.action?bussinessId="+bussinessId+"&tableNames="+tableNames+"&idName="+idName);  
          if(readOnly == "true"){
	          TANGER_OCX_OBJ.SetReadOnly(true, "");// 是否只读
	          TANGER_OCX_OBJ.FileSave = false;
          
          }        
          
          try{
			  if(TANGER_OCX_OBJ.ActiveDocument.TrackRevisions!=undefined){
		          TANGER_OCX_OBJ.ActiveDocument.TrackRevisions = true;
			  }
			  
			  //初始化菜单协同办公
			  initCustomMenus();
			  //默认保留不显示痕迹
			  TANGER_OCX_SetMarkModify(true);
			  TANGER_OCX_ShowRevisions(false);
		  }catch(e){}
          
          with(TANGER_OCX_OBJ.ActiveDocument.Application){
				UserName = "<%=userName%>";
		  }

      }            	
      
      $(document).ready(function(){
      		openFromURL("<%=request.getParameter("bussinessId")%>","<%=request.getParameter("tableNames")%>","<%=request.getParameter("idName")%>");	  
	  }); 
	  
	  
	  
    	//进入或退出痕迹保留状态
		function TANGER_OCX_SetMarkModify(boolvalue) {
        	var TANGER_OCX_OBJ = document.getElementById("TANGER_OCX_OBJ");
			if(TANGER_OCX_OBJ != null && TANGER_OCX_OBJ!="" && TANGER_OCX_OBJ.ActiveDocument != null){//办理时,痕迹保留
		    	try{
					if($("#userName").val() != undefined){
						with(TANGER_OCX_OBJ.ActiveDocument.Application){					
							UserName = $("#userName").val();				
						}
					}		
			    	//TANGER_OCX_OBJ.ActiveDocument.TrackRevisions = true;
			    	TANGER_OCX_OBJ.ActiveDocument.TrackRevisions = boolvalue;
		    	}catch(e){}
			}
		
			//TANGER_OCX_OBJ.ActiveDocument.TrackRevisions = boolvalue;
		}
		
		//初始化自定义菜单
		function initCustomMenus(){
			try{
		        var TANGER_OCX_OBJ = document.getElementById("TANGER_OCX_OBJ");
				var myobj = TANGER_OCX_OBJ;	
				myobj.AddCustomMenu2(0,"协同办公(X)");
				myobj.AddCustomMenuItem2(0,0,-1,false,"保留痕迹",false,1000);
				//禁用"保留痕迹"菜单项
				if($("td#showMark").length == 0){
					myobj.EnableCustomMenuItem2(0,0,-1,true);
				}
				myobj.AddCustomMenuItem2(0,1,-1,false,"不保留痕迹",false,1001);
				if($("td#hideMark").length == 0){
					myobj.EnableCustomMenuItem2(0,1,-1,true);
				}
				myobj.AddCustomMenuItem2(0,2,-1,false,"-",true);
				myobj.AddCustomMenuItem2(0,3,-1,false,"显示痕迹",false,1003);
				if($("td#showRevisions").length == 0){
					myobj.EnableCustomMenuItem2(0,3,-1,true);
				}
				myobj.AddCustomMenuItem2(0,4,-1,false,"隐藏痕迹",false,1004);
				if($("td#hideRevisions").length == 0){
					myobj.EnableCustomMenuItem2(0,4,-1,true);
				}
				myobj.AddCustomMenuItem2(0,5,-1,false,"-",true);
				myobj.AddCustomMenuItem2(0,6,-1,false,"擦除痕迹",false,1006);
				if($("td#acceptRevisions").length == 0){
					myobj.EnableCustomMenuItem2(0,6,-1,true);
				}
			
			}catch(e){
				alert(e);
			}
		}
		//清除痕迹
		function TANGER_OCX_AcceptAllRevisions(){
	        var TANGER_OCX_OBJ = document.getElementById("TANGER_OCX_OBJ");
			if(TANGER_OCX_OBJ != null && TANGER_OCX_OBJ!="" && TANGER_OCX_OBJ.ActiveDocument != null){//办理时,痕迹保留
		    	try{
					if($("#userName").val() != undefined){
						with(TANGER_OCX_OBJ.ActiveDocument.Application){					
							UserName = $("#userName").val();				
						}
					}			
				TANGER_OCX_OBJ.ActiveDocument.AcceptAllRevisions();//接受所有的修订
		    	}catch(e){}
			}
		}
	  //显示/不显示痕迹
		function TANGER_OCX_ShowRevisions(boolvalue) {
        	var TANGER_OCX_OBJ = document.getElementById("TANGER_OCX_OBJ");
			if(TANGER_OCX_OBJ != null && TANGER_OCX_OBJ!="" && TANGER_OCX_OBJ.ActiveDocument != null){//办理时,痕迹保留
				var flag = TANGER_OCX_OBJ.IsReadOnly;
				if(flag){
					TANGER_OCX_OBJ.setReadOnly(false,"");
				}
		    	try{
					if($("#userName").val() != undefined){
						with(TANGER_OCX_OBJ.ActiveDocument.Application){					
							UserName = $("#userName").val();				
						}
					}
				TANGER_OCX_OBJ.ActiveDocument.ShowRevisions = boolvalue;
				if(flag){
					TANGER_OCX_OBJ.setReadOnly(flag,"");
				}			
		    	}catch(e){}
			}
		}
    </script>
    
    <script language="JScript" for="TANGER_OCX_OBJ" event="OnFileCommand(cmd,canceled)">
		if (cmd == 3) //user has clicked on file save menu or button
		{
			saveDoc();
			document.all("TANGER_OCX_OBJ").CancelLastCommand = true;
		}
	</script>
    
	</head>
	<base target="_self" />
	<body class="contentbodymargin" >
		<form id="doctempForm" name="doctempForm"
			action="<%=request.getParameter("contextPath") %>!saveAttachsT.action" enctype="multipart/form-data"
			method="post">
			<s:file id="wordDoc" name="wordDoc" cssStyle="display:none;"></s:file>
			<div id="contentborder" align="center">
				<table width="100%" height="100%" border="0" cellspacing="0"
					cellpadding="0" style="vertical-align: top;">
					<%--<tr>
									<td width="36%">
										<img src="<%=frameroot%>/images/ico/ico.gif" width="9"
											height="9" alt="">&nbsp;&nbsp;附件信息
									</td>
									<td align="right" >
									<a class="Operation" href="javascript:saveDoc();">
										<img src="<%=root%>/images/ico/baocun.gif" width="14"
											height="14" alt="" class="img_s">
									保存至服务器&nbsp;</a>
									</td>
									<td width="5"></td>
									<td align="left">
									<a class="Operation" href="javascript:window.close();">
										<img src="<%=root%>/images/ico/guanbi.gif" width="14"
											height="14" alt="" class="img_s">
									关闭&nbsp;</a>
									</td>
									<td width="41%"></td>
					</tr>
					--%><tr>
									<td colspan="5" valign="top" height="100%">
										<script type="text/javascript">
											document.write(OfficeTabContent);
										</script>
									</td>
					</tr>
				</table>
			</div>
		<form>
	</body>
</html>
			