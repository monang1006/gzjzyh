<%@ page contentType="text/html; charset=utf-8"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<%@include file="/common/include/rootPath.jsp"%>

<html>
	<head>
		<%@include file="/common/include/meta.jsp"%>
		<title>选择列</title>
		<link href="<%=frameroot%>/css/properties_windows.css" type=text/css rel=stylesheet>
		<SCRIPT>
		String.prototype.trim = function() {
				var strTrim = this.replace(/(^\s*)|(\s*$)/g, "");
				strTrim = strTrim.replace(/^[\s　\t]+|[\s　\t]+$/, ""); 
			    return strTrim;
		}
		
function saveButton(){		
	var checkvalues=document.getElementsByName("infoItem");
	var checkedvalue="";
	var checkedname="";
	for(var i=0;i<checkvalues.length;i++){
		if(checkvalues[i].checked==true){
		    checkedvalue=checkedvalue+","+checkvalues[i].value.split('-')[0];
		    checkedname=checkedname+","+checkvalues[i].value.split('-')[1];
		   
	}
	}
	if(checkedvalue==""){
		alert("请选择列");
		return false;
	}
	else
	{
		checkedvalue=checkedvalue.substring(1,checkedvalue.length);	 
		checkedname=checkedname.substring(1,checkedname.length);	
}          
 	
 	
	
	 window.dialogArguments.document.getElementById("pdepEditcode").value=checkedvalue;
	  window.dialogArguments.document.getElementById("pdepEditname").value=checkedname;
		window.close();
	
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
      
     function doallcheck(){   
        var allche = document.getElementById("allid");   
        var che = document.getElementsByName("infoItem");   
        if(allche.checked == true){   
            for(var i=0;i<che.length;i++){   
                che[i].checked="checked";   
            }   
        }else{   
            for(var i=0;i<che.length;i++){   
                che[i].checked=false;   
            }   
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
						    <LABEL>人员基本字段信息</LABEL>
							</td>
						</tr>
						<tr>	
						  <td align="left" valign="top">
							<DIV style="width:99%;height:200;overflow:scroll">
							<TABLE id="addTable" border="0" cellspacing="0" width="300" bordercolordark="#FFFFFF" ordercolorlight="#000000" bordercolor="#333300" cellpadding="2">
							<tr><td align="right"><input type="checkBox" name="allid" id="allid" onclick="doallcheck();"/>全选</td></tr>
							  <s:iterator value="dataRowTitle">
							    <s:if test="infoItemField==\"ORG_ID\" || infoItemField==\"PERSON_PSET\" || infoItemField==\"STRUC_ID\" || infoItemField==\"PERSON_TREATMENT_LEVEL\" || infoItemField==\"PERSON_STATUS\" || infoItemField==\"PERSON_PERSON_KIND\"">	
							    <tr valign="top">
							    <td valign="top" align="left">
							  
									<input type='CheckBox' name='infoItem' value="<s:property value="infoItemField" />-<s:property value="infoItemSeconddisplay" />" style='width:150'>	
									<s:property value="infoItemSeconddisplay" />
							
							
								</td>
								</tr>
							</s:if>		
							</s:iterator>
							</TABLE>
							</DIV>
							</td>
				  		</tr>						
					</TABLE>
					<br>
					<input type="button" class="anniu" value="确定" onclick="saveButton();"/>
					
				
					<input type="button" class="anniu" value="取消" onclick="cancelRecomm();" />
		</DIV>
	</body>
</html>
