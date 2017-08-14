<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/common/include/rootPath.jsp"%>
<%@ taglib uri="/tags/web-newdate" prefix="strong"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib uri="/tags/security" prefix="security"%>
<HTML><HEAD><TITLE> 添加调查 </TITLE>
<LINK href="<%=path%>/oa/css/survey/style.css" type=text/css rel=stylesheet>
<%@include file="/common/include/meta.jsp" %>
<% 
	response.setHeader("Cache-Control","no-store");
	response.setHeader("Pragrma","no-cache");
	response.setDateHeader("Expires",0);
%>

<script src="<%=path%>/oa/js/survey/prototype.js" type="text/javascript"></script>


<SCRIPT>
function CheckForm()
{
	if($('model.surveyName').value!=''){
	     if($('model.surveyName').value.length>64)
	     {
	       alert('名称过长！');
	       return false;
	     }	      	
	}else
	  {
	     alert('名称不能为空！');
		return false;
	  }
	if($('model.explain').value!=''){
	     if($('model.explain').value.length>128)
	     {
	       alert('说明过长！');
	       return false;
	     }	      	
	}
	
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
function setAllDept(sid){
	var state = document.getElementById('DEPT_ALL').checked
	var show = document.getElementById('tr_select');
	if(state==true){
		show.style.display='none';
	}else{
		show.style.display='';
	}
}
</SCRIPT>
</HEAD>
<base target="_self"/>
<BODY  class=contentbodymargin oncontextmenu="return false;"  >
<TABLE cellSpacing=0 cellPadding=3 width="100%" border=0>
  <TBODY>
  <TR>
   <td>&nbsp;</td>
    <TD class=pagehead1>
   <img src="<%=frameroot%>/images/ico/ico.gif" width="9" height="9">&nbsp;
   	  添加调查 
     </TD></TR>
  </TBODY>
</TABLE>
  <BR>
<FORM name=form1 action="<%=root %>/survey/survey!save.action" method=post>
<script type="text/javascript" src="<%=path%>/common/js/newdate/WdatePicker.js"></script>
<INPUT type=hidden value=0 name=model.state>
<input type=hidden id="isPublic" name=model.isPublic value="${model.isPublic}" />
<TABLE class=pubtable cellSpacing=1 cellPadding=3 width="100%" align=center 
border=0>
  <TBODY>
  <TR>
    <TD class=tablecol2 noWrap align=right>名 称(<font color="red">*</font>)：</TD>
    <TD class=tablecol1  noWrap><INPUT class=text maxlength="128" size="20" name=model.surveyName  >&nbsp;&nbsp;</TD></TR>
  <TR>
    <TD class=tablecol2 noWrap align=right>显示样式：</TD>
    <TD class=tablecol1 noWrap>

	<s:select list="styleList" style="width:460" listKey="styleId" listValue="styleName" headerKey="" headerValue="请选择样式"  name="model.surveyStyleId" />
									
    </TD>
        </TR>
  <TR>
    <TD class=tablecol2 noWrap align=right>限制重复：</TD>
    <TD class=tablecol1 noWrap><INPUT class=checkbox type=checkbox CHECKED 
      value=1 name=model.surveyUnRepeat> 是否限制某个人重复提交调查。 </TD></TR>
  <TR>
    <TD class=tablecol2 noWrap align=right>限制查看结果：</TD>
    <TD class=tablecol1 noWrap>
    	<INPUT id="isPublic_yes" name="public" class=checkbox type=radio value=1 name=model.isPublic onclick="check_box('yes')"> 限制查看结果。 
    	<INPUT id="isPublic_no" name="public" class=checkbox type=radio value=0 name=model.isPublic onclick="check_box('no')"> 不限制查看结果。 
    </TD>
  </TR>
      <script>
      var isp="${model.isPublic}"+"";
      	if("1"==isp){
      		document.getElementById("isPublic_yes").checked=true;
      	}else{
      		document.getElementById("isPublic_no").checked=true;
      	}
      	function check_box(public){
      		if(public=="yes"){
      			document.getElementById("isPublic").value="1";
      		}else{
      			document.getElementById("isPublic").value="0";
      		}
      	}
      </script>
  <!--  <TR>
    <TD class=tablecol2 noWrap align=left>调查范围：</TD>
    <TD>
      <TABLE>
        <TBODY>
        <TR>
          <TD class=tablecol1 noWrap align=left>所有部门：&nbsp;<INPUT 
            class=checkbox onclick=setAllDept(); type=checkbox value=DEPT_ALL 
            name=DEPT_ALL> </TD></TR>
        <TR id=tr_select>
          <TD class=tablecol1 noWrap>
            <TABLE 
            style="BORDER-RIGHT: #e1e1e1 1px solid; BORDER-TOP: #e1e1e1 1px solid; BORDER-LEFT: #e1e1e1 1px solid; BORDER-BOTTOM: #e1e1e1 1px solid" 
            cellSpacing=1 cellPadding=3 width=450>
              <TBODY>
              <TR>
                <TD class=Tablecol2 noWrap align=left>部门：</TD>
                <TD class=Tablecol1><SPAN class=Tablecol1><TEXTAREA class=textarea name=SELECT_DEPT_NAME readOnly wrap=yes cols=35></TEXTAREA> 
                  <INPUT type=hidden name=SELECT_DEPT> <INPUT type=hidden 
                  name=SELECT> <INPUT class=button onclick="LoadWindowDept('SELECT_DEPT','SELECT_DEPT_NAME','form1');" type=button value=选择 name=""> 
<INPUT class=button onclick="clearConten('SELECT_DEPT','SELECT_DEPT_NAME')" type=button value=清空 name=""> 
                  </SPAN></TD></TR>
              <TR>
                <TD class=Tablecol2 noWrap align=left>角色：</TD>
                <TD class=Tablecol1><SPAN class=TableLine1><TEXTAREA class=textarea name=SELECT_PRIV_NAME readOnly wrap=yes cols=35></TEXTAREA> 
                  <INPUT type=hidden name=SELECT_PRIV> <INPUT type=hidden 
                  name=SELECT> <INPUT class=button onclick="LoadWindowPriv('SELECT_PRIV','SELECT_PRIV_NAME','form1');" type=button value=选择 name=""> 
<INPUT class=button onclick="clearConten('SELECT_PRIV','SELECT_PRIV_NAME')" type=button value=清空 name=""> 
                  </SPAN></TD></TR>
              <TR>
                <TD class=Tablecol2 noWrap align=left>用户：</TD>
                <TD class=Tablecol1><SPAN class=Tablecol1><TEXTAREA class=textarea name=SELECT_USER_NAME readOnly wrap=yes cols=35></TEXTAREA> 
                  <INPUT type=hidden name=SELECT_USER> <INPUT type=hidden 
                  name=SELECT> <INPUT class=button onclick="LoadWindowUser('SELECT_USER','SELECT_USER_NAME','form1');" type=button value=选择 name=""> 
<INPUT class=button onclick="clearConten('SELECT_USER','SELECT_USER_NAME')" type=button value=清空 name=""> 
                  </SPAN></TD></TR></TBODY></TABLE></TD></TR></TBODY></TABLE></TD></TR>-->
  <TR>
    <TD class=tablecol2 noWrap align=right>说明：</TD>
    <TD class=tablecol1 noWrap><TEXTAREA class=textarea name=model.explain rows=5 wrap=yes cols=58 value=""></TEXTAREA> 
    </TD></TR>
    
  <TR>
    <TD class=tablecol2 noWrap align=left>开始时间(<font color="red">*</font>)：</TD>
    <TD class=Tablecol1>
    <strong:newdate name="model.surveyStartTime" id="search1" width="30%" skin="whyGreen" isicon="true" dateform="yyyy-MM-dd"></strong:newdate>
    </TD></TR>
  <TR>
    <TD class=tablecol2 noWrap align=left>结束时间(<font color="red">*</font>)：</TD>
    <TD class=tablecol1 noWrap>
    <strong:newdate name="model.surveyEndTime" id="search2" width="30%" skin="whyGreen" isicon="true" dateform="yyyy-MM-dd"></strong:newdate>   
    </TD></TR>
      
      
      </TBODY></TABLE>
      
      </form>
      



<table width="90%" border="0" cellspacing="0" cellpadding="00">
				<tr>
					<td align="center" valign="middle">
						<table width="27%" border="0" cellspacing="0" cellpadding="00">
							<tr>
							
								<td width="30%">
									<input name="Submit" type="button" class="input_bg" onclick="CheckForm()" value="保  存">
								</td>
								<td width="30%">
									<input name="Submit2" type="button" class="input_bg"
										value="关  闭" onclick="window.close();">
								</td>
							</tr>
						</table>
					</td>
				</tr>
</table>

</BODY></HTML>
