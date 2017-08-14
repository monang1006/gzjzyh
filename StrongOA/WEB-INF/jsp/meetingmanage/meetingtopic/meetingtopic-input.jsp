<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@include file="/common/include/rootPath.jsp"%>
<%@taglib uri="/tags/web-newdate" prefix="strong"%>
<%@taglib prefix="s" uri="/struts-tags"%>
<html>
	<head>
		<%@include file="/common/include/meta.jsp"%>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<title>议题申报</title>
		<LINK href="<%=frameroot%>/css/properties_windows.css" type=text/css
			rel=stylesheet>
		<script type="text/javascript"
			src="<%=path%>/common/js/newdate/WdatePicker.js"></script>
		<script src="<%=path%>/common/js/jquery/jquery-1.2.6.js"
			type="text/javascript"></script>
		<script language="javascript"
			src="<%=path %>/common/js/upload/jquery.MultiFile.js"></script>
		<script src="<%=path%>/common/js/upload/jquery.blockUI.js"
			type="text/javascript"></script>
		<script src="<%=path%>/common/js/fckeditor2/fckeditor.js"
			type="text/javascript"></script>
		<script language='javascript'
			src='<%=path%>/common/js/jquery/jquery-1.2.6.js'></script>
		<SCRIPT language="javascript"
			src="<%=request.getContextPath()%>/common/js/menu/menu.js"></SCRIPT>

		<LINK href="<%=frameroot%>/css/properties_windows.css" type=text/css
			rel=stylesheet>
		<LINK href="<%=frameroot%>/css/search.css" type=text/css
			rel=stylesheet>
		<script language='javascript'
			src='<%=path%>/common/js/jquery/jquery-1.2.6.js'></script>

		<script language="javascript"
			src="<%=path%>/common/js/common/common.js"></script>

		<script type="text/javascript">
		
		 //转换时间格式(yyyy-MM-dd HH:mm)--->(yyyyMMddHHmm)
         function date2string(stime){
         	var arrsDate1=stime.split('-');
         	stime=arrsDate1[0]+""+arrsDate1[1]+""+arrsDate1[2];
         	var arrsDate2=stime.split(' ');
         	stime=arrsDate2[0]+""+arrsDate2[1];
         	var arrsDate3=stime.split(':');
         	stime=arrsDate3[0]+""+arrsDate3[1]+""+arrsDate3[2];
         	return stime;
         }
   	function closes(){
   		//alert("1111");
   		//window.dialogArguments.location="<%=path %>/meetingmanage/meetingtopic/meetingtopic.action";
		window.close();	
	}
	function onbeforeclose(){
	
		window.returnValue="reload";
	}      
	function formsubmit(){
		
		 var inputDocument=document;
    if(inputDocument.getElementById("topicCode").value==""){
    	alert("议题编号不能为空，请输入。");
    	inputDocument.getElementById("topicCode").focus();
    	return false;
    }
   
    if(inputDocument.getElementById("topicCode").value.length>16){
   	    alert("议题编号长度超过限制，请重新输入。");
    	inputDocument.getElementById("topicCode").focus();
    	return false;
    }
    
    if(inputDocument.getElementById("topicSubject").value==""){
    	alert("议题主题不能为空，请输入。");
    	inputDocument.getElementById("topicSubject").focus();
    	return false;
    }
   
    if(inputDocument.getElementById("topicsort").value=="123"){
    	alert("请选择议题分类。");
    	inputDocument.getElementById("topicsort").focus();
    	return false;
    }

    
	var oEditor = FCKeditorAPI.GetInstance('content');
				     var acontent = oEditor.GetXHTML();
		document.getElementById("topicContent").value=acontent;
		
		//获取被删除的附件id
			var delAttachIds = document.getElementById("delAttachIds").value;
			if(delAttachIds.length>0){
            		delAttachIds = delAttachIds.substring(0,delAttachIds.length-1);
            	}
            document.getElementById("delAttachIds").value = delAttachIds;           
       		document.forms(0).action="<%=path %>/meetingmanage/meetingtopic/meetingtopic!save.action";
         	document.forms(0).submit();
			 
		}
		
		//删除附件
 function delAttach(id){
		 	var attach = document.getElementById(id);
		 	var delAttachIds = document.getElementById("delAttachIds").value;
		 	document.getElementById("delAttachIds").value += id + ",";
		 	attach.style.display="none";
		 }
		 //下载附件
 function download(id){
		 var attachDownLoad = document.getElementById("attachDownLoad");
		 	attachDownLoad.src = "<%=path%>/meetingmanage/meetingtopic/meetingtopic!down.action?attachId="+id;
		 }		
function songshen(){
	var issave=document.getElementById("issave").value;
	 // alert(issave);
	if(issave==''|| issave==null){
	  alert('请先保存议题再提交！');
	  return;
	}
   var id=document.getElementById("topId").value;
  //  alert(id);
    $.post(
		"<%=path%>/meetingmanage/meetingtopic/meetingtopic!getProcessName.action",
		{topId:id},
		function(data){
			if(data!='flagfalse'){
				var str = data;
				var p = str.split(",");
				if(p[1] == '0'){
					var returnValue = OpenWindow("<%=root%>/meetingmanage/meetingtopic/meetingtopic!nextstep.action?topId="+p[0], 
			                                  550, 500, window);
			      if(returnValue=='OK'){
			        window.dialogArguments.location="<%=path %>/meetingmanage/meetingtopic/meetingtopic.action";
			        window.close();
			        }
		        }else if(p[1]=='1'){
		        	alert("议题已提交审核中...");
		        }else{
		        	alert("议题已送审...")
		        }
			}else{
	     // document.forms(0).action="<%=path%>/meetingmanage/meetingtopic/meetingtopic!pubTopic.action?topId="+id;
        // document.forms(0).submit();
        $.post(
	          "<%=path%>/meetingmanage/meetingtopic/meetingtopic!pubTopic.action",
		     {topId:id},
		     function(data){
		  
			 if(data=='success'){	
		      alert('成功通过！');
		 window.dialogArguments.location='<%=path %>/meetingmanage/meetingtopic/meetingtopic.action';
		 window.close();
			
		}
		
		}
	)
			}
		}
	)
}		 
		     	
</script>
		<base target="_self">
	</head>
	<body class=contentbodymargin oncontextmenu="return false;" onbeforeunload="onbeforeclose()">
		<DIV id=contentborder align=center>

			<table width="100%"
				style="FILTER: progid : DXImageTransform.Microsoft.Gradient ( gradientType = 0, startColorStr = #ededed, endColorStr = #ffffff );">
				<tr>
					<td>&nbsp;</td>
					<td width="40%">
						<img src="<%=frameroot%>/images/ico/ico.gif" width="9" height="9">&nbsp;
						议题管理
					</td>
					<td width="60%"><table align="right"><tr>
						<td>
							<a class="Operation" href="#" onclick="songshen()">
								<img
									src="<%=root%>/images/ico/tijiao.gif"
									width="15" height="15" class="img_s"><span id="test"
								style="cursor:hand">提  交&nbsp;</span> </a>
						</td>
						<td width="5"></td>
						<td>
							<a class="Operation" href="#" onclick="closes()">
								<img
									src="<%=root%>/images/ico/guanbi.gif"
									width="15 " height="15" class="img_s"><span id="test"
								style="cursor:hand">关  闭&nbsp;</span> </a>
						</td>
						<td width="5"></td>
					</tr></table></td>
					</tr>
			</table>
			<iframe id="attachDownLoad" src=''
				style="display: none; border: 4px solid #CCCCCC;"></iframe>
			<s:form action="/meetingmanage/meetingtopic/meetingtopic!save.action"
				method="post" enctype="multipart/form-data">
				<input type="hidden" id="delAttachIds" name="delAttachIds" value="">
				<input type="hidden" id="topId" name="model.topicId"
					value="${model.topicId}">
				<input type="hidden" id="issave" name="issave"
								value="${issave}">
				<input type="hidden" id="topOrgcode" name="model.topOrgcode"
					value="${model.topOrgcode}">
				<input type="hidden" id="departmentId" name="model.departmentId"
					value="${model.departmentId}">
				<input type="hidden" id="topicContent" name="model.topicContent" />
				<div id="con" style="display: none">
					${model.topicContent}
				</div>
				<table width="100%" height="10%" border="0" cellpadding="0"
					cellspacing="1" align="center" class="table1">
					<tr>
						<td width="20%" height="21" class="biao_bg1" align="right">
							<span class="wz">议题编号(<font color="red">*</font>)：</span>
						</td>
						<td class="td1" colspan="3" align="left">
							<input id="topicCode" name="model.topicCode"
								value="${model.topicCode}" type="text" size="31" maxlength="16">
						</td>
					</tr>
					<tr>
						<td width="20%" height="21" class="biao_bg1" align="right">
							<span class="wz">议题主题(<font color="red">*</font>)：</span>
						</td>
						<td class="td1" colspan="3" align="left">
							<input id="topicSubject" name="model.topicSubject"
								value="${model.topicSubject}" type="text" size="31"
								maxlength="25">
						</td>
					</tr>
					<tr>
						<td width="20%" height="21" class="biao_bg1" align="right">
							<span class="wz">议题分类(<font color="red">*</font>)：</span>
						</td>
						<td class="td1" colspan="3" align="left">
							<s:select list="sortList" listKey="topicsortId"
								listValue="topicsortName" headerKey="123" headerValue="请选择分类"
								id="topicsort" name="model.topicsort.topicsortId"
								style="width:15.5em" />

						</td>
					</tr>

					<tr>
						<td colspan="1" height="21" class="biao_bg1" align="right">
							<span class="wz">创建时间&nbsp;&nbsp;&nbsp;：</span>
						</td>
						<td colspan="3" class="td1" align="left">
							<strong:newdate name="model.topicEstime" id="topicEstime"
								width="246" skin="whyGreen" isicon="true"
								dateobj="${model.topicEstime}" dateform="yyyy-MM-dd HH:mm:ss"></strong:newdate>
					</tr>

					<tr>
						<td width="15%" height="21" class="biao_bg1" align="right">
							<span class="wz">议题描述&nbsp;&nbsp;&nbsp;：</span>
						</td>
						<td class="td1" colspan="3" align="left">
							<input id="topicDemo" name="model.topicDemo"
								value="${model.topicDemo}" type="text" size="73" maxlength="50">
						</td>
					</tr>

					<tr>
						<td height="21" class="biao_bg1" align="right">
							<span class="wz">议题内容&nbsp;&nbsp;&nbsp;：</span>
						</td>
						<td class="td1" colspan="3" align="left">
							<script type="text/javascript"
								src="<%=request.getContextPath()%>/common/js/fckeditor2/fckeditor.js">
						</script>
							<script type="text/javascript">
													 
							var oFCKeditor = new FCKeditor( 'content' );
							oFCKeditor.BasePath	= '<%=request.getContextPath()%>/common/js/fckeditor2/'
							oFCKeditor.ToolbarSet = "FutureDefaultSet" ;
							oFCKeditor.Width = '96%' ;
							oFCKeditor.align = 'left';
                            oFCKeditor.Height = '300' ;								
							oFCKeditor.Value= document.getElementById("con").innerText;
							oFCKeditor.Create() ;
						 </script>
						</td>
					</tr>

					<tr>
						<td height="21" class="biao_bg1" align="right">
							<span class="wz">附 件&nbsp;&nbsp;&nbsp;：</span>
						</td>
						<td class="td1" colspan="3" align="left">
							&nbsp;&nbsp;
							<input type="file" onkeydown="return false;" name="upload"
								class="multi required" style="width: 63%;" />
							${attachFiles}
						</td>
					</tr>
				</table>
			</s:form>

			<table width="100%"
				style="FILTER: progid : DXImageTransform.Microsoft.Gradient ( gradientType = 0, startColorStr = #ededed, endColorStr = #ffffff );">
				<tr>
					<td>&nbsp;</td>
					<td width="40%">
					</td>
					<td width="60%"><table align="left"><tr>
						<td>
							<a class="Operation" href="#" onclick="formsubmit();">
								<img
									src="<%=root%>/images/ico/tijiao.gif"
									width="15" height="15" class="img_s"><span id="test"
								style="cursor:hand">保  存&nbsp;</span> </a>
						</td>
						<td width="5"></td>
						<td>
							<a class="Operation" href="#" onclick="closes()">
								<img
									src="<%=root%>/images/ico/guanbi.gif"
									width="15 " height="15" class="img_s"><span id="test"
								style="cursor:hand">关  闭&nbsp;</span> </a>
						</td>
						<td width="5"></td>
					</tr></table></td>
					</tr>
			</table>
		</DIV>
	</body>
</html>
