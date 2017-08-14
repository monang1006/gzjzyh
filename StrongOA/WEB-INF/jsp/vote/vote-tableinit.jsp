<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@include file="/common/include/rootPath.jsp"%>
<html>
<head><title>表格型问题的列数设置</title></head>
<LINK href="<%=path%>/oa/css/survey/style.css" type=text/css rel=stylesheet>
<LINK href="<%=frameroot%>/css/properties_windows.css" type=text/css rel=stylesheet>
<body>
<script type="text/javascript">
  	function doAction(){
    var tablecol=document.getElementById("tablecol").value;
    if(tablecol==null||tablecol.length<1){
        alert("表格列数必须指定！");
    	return false;
    }
    if(/[^\d]/.test(tablecol)){
    	   alert("必须输入数字");
    	   return false;
    }else{
    	  if(tablecol>6)
    	   {
    	     alert("列数必须小于7");
    	     return false ;
    	   }
    }
    	
    var tableisonly=document.getElementById("tableisonly").value;
    if(tableisonly==null||tableisonly.length<1){
      alert("表格问题类型必须指定！");
      return ;
    }
    
    window.returnValue=tablecol+"-"+tableisonly;//返回
    window.close();
  }
  
 function myback(){
  	window.returnValue="";//返回
    window.close();
 } 
</script>
<DIV id=contentborder align=center>
  <table width="100%" border="0" cellspacing="0" cellpadding="0" style="vertical-align: top;">
  <tr>
    <td height="100%">
		<table width="100%" border="0" cellspacing="0" cellpadding="0"  > 
		 <tr>
		   <td height="40" style="FILTER:progid:DXImageTransform.Microsoft.Gradient(gradientType=0,startColorStr=#ededed,endColorStr=#ffffff);">
			   <table width="100%" border="0" cellspacing="0" cellpadding="00">
				 <tr>
					<td>
					&nbsp;
					</td>
					<td width="40%">
					<img src="<%=frameroot%>/images/ico/ico.gif" width="9" height="9">&nbsp;
					表格型问题的列数设置
					</td>
					<td width="*">&nbsp;</td>
					<td width="5"></td>	
				
						</tr>
					</table>
				</td>
			</tr>
			<tr>
			  <td>
			  <table><tr><td align="right">
			  表格列数：</td><td align="left"><input type=text title="列数1至6之间" maxlenght=50 style="width:70" name="tablecol"></td>
              <tr><td align="right">表格问题类型：</td><td align="left">
               <select name="tableisonly">
               <option value=''>--请选择--</option>
               <option value='N'>多选</option>
               <option value='Y'>单选</option>
               </select>
               </td></tr></table>
              &nbsp;&nbsp; <input type=button value="确定" class="input_bg" onclick="doAction()">&nbsp;&nbsp;&nbsp;
               <input type=button value="关闭" class="input_bg" onclick="myback()">
			  <td></td>
			</tr>
		</table>
</DIV>

</body>
</html>