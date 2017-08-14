<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@include file="/common/include/rootPath.jsp"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@include file="/common/include/meta.jsp"%>
<%
	response.setHeader("Cache-Control", "no-store");
	response.setHeader("Pragrma", "no-cache");
	response.setDateHeader("Expires", 0);
%>
<html>
	<head>
		<%@include file="/common/include/meta.jsp"%>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<title>设置表格表头</title>
		<LINK href="<%=frameroot%>/css/properties_windows.css" type=text/css
			rel=stylesheet>
<SCRIPT LANGUAGE="JavaScript" src="<%=path%>/common/js/commontab/service.js"></SCRIPT>
<script type="text/javascript">

function trim(str)
{
 var tmpStr = new String(str);
 var startIndex = 0,endIndex = 0;
 for(var i=0;i<tmpStr.length;i++)
 {
  if(tmpStr.charAt(i)==" ")
  {
   continue;
  }
  else
  {
   startIndex = i;
   break;
  }
 }
 for(var i=tmpStr.length;i>=0;i--) //注意开始最大下标必须减1
 {
  if(tmpStr.charAt(i-1)==" ")
  {
   
   continue;
  }
  else
  {
   endIndex = i;
   break;
  }
 }

  tmpStr = tmpStr.substring(startIndex,endIndex);
  return tmpStr;
}
  var ff=0;
function init(){  
     _table=document.getElementById("table");
	 _table.border="0.5px";
	 _table.width="80px";
	
 	//for(var i=1;i<2;i++){
		 var row=document.createElement("tr");  
		// row.id=i;
		// for(var j=1;j<2;j++){
			 var cell=document.createElement("td"); 
	cell.innerHTML="<input type='text' id='worksname"+ff+"' value='' style='WIDTH:100px; font-size:9pt; color:#000000' />";  
			 row.appendChild(cell); 
			 ff=1;

		// }
		 document.getElementById("newbody").appendChild(row);  
	// }
 }  
 
  
 
 /*添加列，采用insertCell(列位置)方法*/
 function addCell(){
 /*document.getElementById("table").rows.item(0).cells.length
  用来获得表格的列数
 */
   // for(var i=0;i<document.getElementById("table").rows.length;i++){
     
		var cell=document.getElementById("table").rows[0].insertCell(1);
		//alert(ff);
		cell.innerHTML="<input type='text' id='worksname"+ff+"' value='' style='WIDTH:100px; font-size:9pt; color:#000000' />";
		ff++;
	//}
 }
 /*删除列，采用deleteCell(列位置)的方法*/
 function removeCell(){
	for(var i=0;i<document.getElementById("table").rows.length;i++){
		//alert(document.getElementById("table").rows.item(0).cells.length);
	        if(document.getElementById("table").rows.item(0).cells.length>1){
	        	document.getElementById("table").rows[i].deleteCell(1);	
	        	ff--;
	        }
	        if(document.getElementById("table").rows.item(0).cells.length==1){
	        	     ff=1;
	        	}
		  	
	}
}


	function setColsTitle(){
     var r=document.getElementById('table').rows;
		var k=document.getElementById("table").rows.item(0).cells.length	
		   var t;
		   t=k+',';
		for(var i=0;i<k;i++){
			var worksid='worksname'+i;
			
			var totitle=document.getElementById(worksid).value;
			t=t+totitle+',';
			
       }
          //  alert(t);
            window.returnValue=t;
            window.close();
 
 }
		  
	
   </script>
	</head>
	<body oncontextmenu="return false;" style="overflow: auto" onLoad="init();">
		<DIV id=contentborder align=center>
			<table width="100%"
				style="FILTER:progid:DXImageTransform.Microsoft.Gradient(gradientType=0,startColorStr=#ededed,endColorStr=#ffffff);">
				<tr>
					<td>&nbsp;</td>
					<td width="50%">
						<img src="<%=frameroot%>/images/ico/ico.gif" width="9" height="9">&nbsp;
						设置表格表头
					</td>
					<td width="*">
						&nbsp;
					</td>
				</tr>
			</table>
			<s:form action="" method="post"
							id="22" theme="simple">
			
			<table width="100%" height="10%" border="0" cellpadding="0"
				cellspacing="1" align="center" class="table1">
				<!-- 
				<tr>
					<td width="30%" height="21" class="biao_bg1" align="right">
						<span class="wz">设置行数(<font color="red">*</font>)：</span>
					</td>
					<td width="25%" class="td1" colspan="3" align="left">
	  <s:select name="rowsNum"  list="#{'':'选择行数','0':'0','1':'1','2':'2','3':'4','4':'4','5':'5'}" listKey="key" listValue="value" style="width:6em"/>
					</td>
				</tr>
				 -->
				<tr>
					<td colspan="2" height="21" class="biao_bg1" align="center">
						<span class="wz"><font-size:12pt>设置表头</font></span>
					</td>
					
				</tr>
				<tr>
				<td colspan="2" height="21" class="biao_bg1" align="left">
		<img src='<%=path%>/oa/image/survey/add_vote.gif' class='imglinkgray' onclick='addCell();' title='添加列' style='display:'>&nbsp;&nbsp;&nbsp;
		<img src='<%=path%>/oa/image/survey/delete_s.gif' class='imglinkgray' onclick='removeCell()' title='删除列' style='display:'>
				</td>
				</tr>
				<tr>
				<td colspan="2" height="21" class="biao_bg1" align="right">
				 <table  id="table" align="center">  
          <tbody id="newbody"></tbody>  
	 
          </table> 
				
				</td>
				
				</tr>
							
				<tr>
					<td class="td1" colspan="2" align="center">
						<input name="Submit" type="button" onclick="setColsTitle();" class="input_bg" value="确定">
						<input name="Submit2" type="button" class="input_bg" value="关 闭"
							onclick="javascript:window.close();">
					</td>
				</tr>
			</table>
	 </s:form>
	 <div id="myTable">
	  
	 </div>
		</DIV>
	</body>
</html>
