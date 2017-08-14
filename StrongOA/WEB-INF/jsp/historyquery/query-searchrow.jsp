<%@ page contentType="text/html; charset=utf-8"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<%@include file="/common/include/rootPath.jsp"%>

<html>
	<head>
		<%@include file="/common/include/meta.jsp"%>
		<title>选择列</title>
		<link href="<%=frameroot%>/css/properties_windows.css" type=text/css rel=stylesheet>
		<script src="<%=path%>/common/js/jquery/jquery-1.2.6.js"
			type="text/javascript"></script>
		<SCRIPT>
		String.prototype.trim = function() {
				var strTrim = this.replace(/(^\s*)|(\s*$)/g, "");
				strTrim = strTrim.replace(/^[\s　\t]+|[\s　\t]+$/, ""); 
			    return strTrim;
		}
		
function saveButton(){		
	var checkvalues=document.getElementsByName("infoItem");
	var infoSetValue="${infoSetValue}";
	var checkedvalue="";
	for(var i=0;i<checkvalues.length;i++){
		if(checkvalues[i].checked==true)
		    checkedvalue=checkedvalue+","+checkvalues[i].value;
	}
	if(checkedvalue==""){
		alert("请选择列");
		return false;
	}
	else
		checkedvalue=checkedvalue.substring(1,checkedvalue.length);		        
		$.ajax({
			type:"post",
			data:{
				checkedvalue:checkedvalue,
				infoSetValue:infoSetValue		
			},
			url:"<%=path%>/historyquery/query!remberCloumn.action",
			success:function(data){	
				window.close();
			},
			error:function(data){
				alert("对不起，操作异常"+data);
			}
		});  	
}
		
		
function cancelRecomm(){
	window.close();
}
	
function CheckAll()
{        
    var checkvalues=document.getElementsByName("infoItem");         
	for(var i=0;i<checkvalues.length;i++)
		checkvalues[i].checked=true;		    
}
      
function ContraryAll()
    {
          var checkvalues=document.getElementsByName("infoItem");
            
		      for(var i=0;i<checkvalues.length;i++)
		      {
		      	if(checkvalues[i].checked==true && checkvalues[i].disabled==false)
		      		checkvalues[i].checked=false;
		      	else
		      		checkvalues[i].checked=true;
		      }
		      		 
      }

		</SCRIPT>
	</head>
<!--   控制关闭弹出窗口-->
 <base target="_self"> 
	<BODY class="contentbodymargin" oncontextmenu="return false;">
		<DIV id=contentborder cellpadding="0">
			<center>
				<TABLE>
					<TR>
						<td></td>
					</TR>
				</TABLE>
					<table  border="1"  id="conditionTable" cellspacing="0" width="95%" bordercolordark="#FFFFFF" ordercolorlight="#000000" bordercolor="#333300" cellpadding="2" >
						<tr>

							<td width="20%" colspan="2">							 
						    <LABEL>${objName}</LABEL>
							</td>
						</tr>
						<tr>	
						  <td align="left" valign="top">
							<DIV style="width:99%;height:300;overflow:scroll">
							<TABLE id="addTable" border="0" cellspacing="0" width="200" bordercolordark="#FFFFFF" ordercolorlight="#000000" bordercolor="#333300" cellpadding="2">	 
							  <s:iterator value="columnList">
							    <tr valign="top">
							    <td valign="top" align="left">
							    <s:if test="infoItemDatatype!=15 && infoItemFlag==1">	
									<input type='CheckBox' name='infoItem' value="<s:property value="infoItemCode" />"  checked='checked' disabled="disabled" style='width:100'>	
									<s:property value="infoItemSeconddisplay" />
								</s:if>
								<s:if test="infoItemDatatype!=15 && infoItemFlag==0">
									<s:if test="checkedvalue.indexOf(infoItemCode)>=0">
										<input type='CheckBox' name='infoItem' value="<s:property value="infoItemCode" />"  checked='checked' style='width:100'>	
										<s:property value="infoItemSeconddisplay" />
									</s:if>
									<s:else>
										<input type='CheckBox' name='infoItem' value="<s:property value="infoItemCode" />" style='width:100'>	
										<s:property value="infoItemSeconddisplay" />
									</s:else>
								</s:if>
								</td>
								</tr>		
							</s:iterator>
							</TABLE>
							</DIV>
							</td>
				  		</tr>						
					</TABLE>
					<br>
					<input type="button" class="anniu" value="确定" onclick="saveButton();"/>
					<input type="button" name="chkAll" id="chkAll" class="anniu" value="全选" onclick="CheckAll()"/>
					<input type="button" class="anniu" value="反选" onclick="ContraryAll()"/>
					<input type="button" class="anniu" value="取消" onclick="cancelRecomm();" />
		</DIV>
	</body>
</html>
