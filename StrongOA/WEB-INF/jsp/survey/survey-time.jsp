<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/common/include/rootPath.jsp"%>
<%@ taglib uri="/tags/web-newdate" prefix="strong"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<HTML><HEAD><TITLE>调查表管理</TITLE>
<LINK href="<%=path%>/oa/css/survey/style.css" type=text/css rel=stylesheet>




<script src="<%=path%>/oa/js/survey/prototype.js" type="text/javascript"></script>





<SCRIPT>

function CheckForm()
{

	var START_DATE = $('model.surveyStartTime').value; 
	
	var END_DATE = $('model.surveyEndTime').value; 
	var NOW_DATE = getdate();

	if(START_DATE>END_DATE)
	{
	   alert('开始时间不能大于结束时间！');
		return false;
	}
	if(NOW_DATE>END_DATE)
	{
	   alert('结束时间不能小于当前时间！');
		return false;
	}

	form1.submit();
}
function getdate()
{   
  var now=new Date();
  y=now.getFullYear();
  m=now.getMonth()+1;
  d=now.getDate();
  m=m<10?"0"+m:m;
  d=d<10?"0"+d:d;
  return y+"-"+m+"-"+d;
}

</SCRIPT>
</HEAD>
<base target="_self"/>
<BODY  class=contentbodymargin oncontextmenu="return false;"  >

<FORM name=form1 action="<%=root %>/survey/survey!setTime.action" method=post>
<script type="text/javascript" src="<%=path%>/common/js/newdate/WdatePicker.js"></script>
<INPUT type=hidden  name=model.surveyId value=${model.surveyId } >


<TABLE class=pubtable cellSpacing=1 cellPadding=3 width="100%" align=center border=0>
  <TBODY>

  <TR>
    <TD class=tablecol2 noWrap align=left width="35%">开始时间：</TD>
    <TD class=Tablecol1>
    <strong:newdate name="model.surveyStartTime" id="surveyStartTime" width="74%" skin="whyGreen" isicon="true" nowvalue="${startTime}" dateform="yyyy-MM-dd"></strong:newdate>
    </TD></TR>
  <TR>
    <TD class=tablecol2 noWrap align=left>结束时间：</TD>
    <TD class=tablecol1 noWrap>
    <strong:newdate name="model.surveyEndTime" id="surveyEndTime" width="74%" skin="whyGreen" isicon="true" nowvalue="${endTime}" dateform="yyyy-MM-dd"></strong:newdate>   
    </TD></TR>
      
      
    </TBODY>
</TABLE>
      
</form>
      


<TBODY>
<br><br>
<table width="90%" border="0" cellspacing="0" cellpadding="00">
				<tr>
					<td align="center" valign="middle">
						<table width="27%" border="0" cellspacing="0" cellpadding="00">
							<tr>
								<td width="30%">
									<input name="Submit2" type="submit" class="input_bg" value="保 存" onclick="CheckForm()">
								</td>
								<td width="30%">
									<input name="button" type="button" class="input_bg" value="关 闭" onclick="window.close()">
								</td>
							</tr>
						</table>
					</td>
				</tr>
</table>




  </TBODY>
</BODY></HTML>
