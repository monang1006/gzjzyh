<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/common/include/rootPath.jsp"%>
<%@ taglib uri="/tags/web-newdate" prefix="strong"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib uri="/tags/security" prefix="security"%>
<HTML><HEAD><TITLE> 添加调查 </TITLE>
<!-- <LINK href="<%=path%>/oa/css/survey/style.css" type=text/css rel=stylesheet> -->
<%@include file="/common/include/meta.jsp" %>
<% 
	response.setHeader("Cache-Control","no-store");
	response.setHeader("Pragrma","no-cache");
	response.setDateHeader("Expires",0);
%>
<!--<LINK href="<%=path%>/oa/css/survey/style.css" type=text/css rel=stylesheet>-->
<LINK href="<%=frameroot%>/css/properties_windows_add.css" type=text/css rel=stylesheet>
<script src="<%=path%>/oa/js/survey/prototype.js" type="text/javascript"></script>
<SCRIPT>
 function checkRC(ele){
      //radio字段选中检验
	  var isOk=false;
	  for(i=0;i<ele.length;i++) {
//	    alert(ele[i].value);
       	if(ele[i].checked) {
           isOk=true;
           break;
       	 }       	 
	   }
	   if(!isOk){
	     	return false;
	   }else{
			return true;
	   }
 }

function CheckForm()
{
	if($('vote.title').value!=''){
	     if($('vote.title').value.length>50)
	     {
	       alert('名称过长！');
	       return false;
	     }	      	
	}else
	  {
	     alert('名称不能为空！');
		return false;
	  }
	if($('vote.memo').value!=''){
	     if($('vote.memo').value.length>100)
	     {
	       alert('说明过长！');
	       return false;
	     }	      	
	}
	if($('vote.styleId').value.length<1){
	   alert("显示样式不能为空！");
	   return false ;
	}
	
	
	   //radio选中检验
	   if(!checkRC(document.getElementsByName("vote.type"))){
	      alert("问卷类型必须指定！");
	      return false ;
	   }
	   if(!checkRC(document.getElementsByName('vote.isRepeated'))){
	      alert("限制重复必须指定！");
	      return false ;
	   }
	   if(!checkRC(document.getElementsByName('vote.isPrivate'))){
	      alert("限制查看结果必须指定！");
	      return false ;
	   }

	var START_DATE = $('vote.startDate').value; 
	
	var END_DATE = $('vote.endDate').value; 
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
<DIV id=contentborder align=center>
<table width="100%" border="0" cellspacing="0" cellpadding="00">
				<tr>
				<td colspan="3" class="table_headtd">
					<table width="100%" border="0" cellspacing="0" cellpadding="00">
						<tr>
							<td class="table_headtd_img" >
								<img src="<%=frameroot%>/images/ico/ico.gif" >&nbsp;
							</td>
							<td align="left">
								
								<strong>添加调查 </strong>
							</td>
							<td align="right">
								<table border="0" align="right" cellpadding="00" cellspacing="0">
					                <tr>
					                 	<td width="7"><img src="<%=frameroot%>/images/ch_h_l.gif"/></td>
					                 	<td class="Operation_input" onclick="CheckForm();">&nbsp;保&nbsp;存&nbsp;</td>
					                 	<td width="7"><img src="<%=frameroot%>/images/ch_h_r.gif"/></td>
				                  		<td width="5"></td>
					                 	<td width="8"><img src="<%=frameroot%>/images/ch_z_l.gif"/></td>
					                 	<td class="Operation_input1" onclick="window.close();">&nbsp;关&nbsp;闭&nbsp;</td>
					                 	<td width="7"><img src="<%=frameroot%>/images/ch_z_r.gif"/></td>
				                  		<td width="6"></td>
					                </tr>
					            </table>
							</td>
						</tr>
					</table>
				</td>
				</tr>
				<tr>
					<td>


<FORM name=form1 action="<%=root %>/vote/vote!saveVote.action" method=post>
<script type="text/javascript" src="<%=path%>/common/js/newdate/WdatePicker.js"></script>
<INPUT type=hidden value=0 name="vote.state">
<INPUT type=hidden value='N' name="vote.isRealname">
<table  border="0" cellpadding="0" cellspacing="0" class="table1" width="100%" >
  <TBODY>
  <TR>
    <TD class="biao_bg1" align="right"><span class="wz"><font color="red">*</font>&nbsp;名 称：</span></TD>
    <TD class="td1" style="padding-left:5px;"><INPUT class=text maxlength="50" size="50" name="vote.title">&nbsp;&nbsp;</TD></TR>
  <TR>
    <TD class="biao_bg1" align="right"><span class="wz"><font color="red">*</font>&nbsp;显示样式：</span></TD>
    <TD class="td1" style="padding-left:5px;">

	<s:select list="styleList" style="width:460" listKey="styleId" listValue="styleName" headerKey="" headerValue="请选择样式"  name="vote.styleId" />
									
    </TD>
        </TR>
        <TR>
    <TD class="biao_bg1" align="right"><span class="wz"><font color="red">*</font>&nbsp;问卷类型：</span></TD>
    <TD class="td1" style="padding-left:5px;">
    			   <input type="radio" name="vote.type" value="1">网页参与
       &nbsp;&nbsp;<input type="radio" name="vote.type" value="2">短信和页面参与
   
   </TD></TR>
  <TR>
    <TD class="biao_bg1" align="right"><span class="wz"><font color="red">*</font>&nbsp;限制重复参与：</span></TD>
    <TD class="td1" style="padding-left:5px;">
   					  <input type="radio" name="vote.isRepeated" value="N">允许重复
     	  &nbsp;&nbsp;<input type="radio" name="vote.isRepeated" value="Y">不允许重复
    </TD></TR>
  <TR>
    <TD class="biao_bg1" align="right"><span class="wz"><font color="red">*</font>&nbsp;限制查看结果：</span></TD>
    <TD class="td1" style="padding-left:5px;">
     				  <input type="radio" name="vote.isPrivate" value="Y">限制查看
     	  &nbsp;&nbsp;<input type="radio" name="vote.isPrivate" value="N">不限制查看
    </TD>
  </TR>
      <script>
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
    <TD class="biao_bg1" align="right" valign="top"><span class="wz" >说明：</span></TD>
    <TD class="td1" style="padding-left:5px;"><TEXTAREA class=textarea name=vote.memo rows=5 wrap=yes cols=58 value=""></TEXTAREA> 
    </TD></TR>
    
  <TR>
    <TD class="biao_bg1" align="right"><span class="wz"><font color="red">*</font>&nbsp;开始时间：</span></TD>
    <TD class="td1" style="padding-left:5px;">
    <strong:newdate name="vote.startDate" id="search1" width="30%" skin="whyGreen" isicon="true" dateform="yyyy-MM-dd"></strong:newdate>
    </TD></TR>
  <TR>
    <TD class="biao_bg1" align="right"><span class="wz"><font color="red">*</font>&nbsp;结束时间：</span></TD>
    <TD class="td1" style="padding-left:5px;">
    <strong:newdate name="vote.endDate" id="search2" width="30%" skin="whyGreen" isicon="true" dateform="yyyy-MM-dd"></strong:newdate>   
    </TD></TR>
      <tr>
							<td class="table1_td"></td>
							<td></td>
						</tr>
      
      </TBODY></TABLE>
      
      </form>
      </table>
      </DIV>
 </BODY></HTML>
