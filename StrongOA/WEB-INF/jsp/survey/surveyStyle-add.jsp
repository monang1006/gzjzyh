<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/common/include/rootPath.jsp"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib uri="/tags/security" prefix="security"%>
<HTML><HEAD><TITLE>调查样式</TITLE>
<LINK href="<%=path%>/oa/css/survey/style.css" type=text/css rel=stylesheet>
<LINK href="<%=frameroot%>/css/properties_windows.css" type=text/css rel=stylesheet>
<%@include file="/common/include/meta.jsp" %>

<script src="<%=path%>/oa/js/survey/prototype.js" type="text/javascript"></script>


<SCRIPT>
String.prototype.trim = function() {
                var strTrim = this.replace(/(^\s*)|(\s*$)/g, "");
                strTrim = strTrim.replace(/^[\s　\t]+|[\s　\t]+$/, ""); 
                return strTrim;
            }

function CheckForm()
{
	var styleName = $('styleName').value.trim();
    if(styleName!=''){
	     if(styleName.length>64)
	     {
	       alert('名称过长！');
	       $('styleName').focus();
	       return false;
	     }else{
		     var re1 = new RegExp("^([\u4E00-\uFA29]|[\uE7C7-\uE7F3]|[a-zA-Z0-9])*$");
			 if (!re1.test(styleName)){
			      alert("名称只能输入汉字，字母或数字！");
			      $('styleName').focus();
			      return false;
			 }
	     }
	     	      	
	}else
	  {
	     alert('名称不能为空！');
	     $('styleName').value = "";
	     $('styleName').focus();
		return false;
	  }
	$('styleName').value = styleName;
	form1.submit();
}


</SCRIPT>
</HEAD>
<base target="_self"/>
<BODY  class=contentbodymargin oncontextmenu="return false;"  >
<DIV id=contentborder align=center>

<TABLE cellSpacing=0 cellPadding=3 width="100%" border=0>
  <TBODY>
  <TR>
  <td width="5%" align="center"><img src="<%=frameroot%>/images/ico/ico.gif" width="9" height="9"></td>
    <TD class=pagehead1> 
    <script>
    	var  styleId = "${model.styleId}";
    	if(""==styleId|null==styleId|"undefined"==styleId|undefined==styleId){
    		document.write("添加样式");
    	}else{
    		document.write("编辑样式");
    	}
    </script>
    </TD></TR>
  </TBODY>
</TABLE>
  <BR>
<FORM name=form1 action="<%=root%>/survey/surveyStyle!save.action" method=post>
<TABLE class=pubtable cellSpacing=1 cellPadding=3 width="100%" align=center border=0>
  <TBODY>
  <TR>
    <TD class=tablecol2 noWrap align=right>名称(<font color="red">*</font>)：<input type="hidden" value=${model.styleId} name=model.styleId /></TD>
    <TD class=tablecol1 noWrap><INPUT class=text maxlength="128"  id="styleName" size="64" name=model.styleName value="${model.styleName}" /></TD></TR>
  <TR>
    <TD class=tablecol2 noWrap align=right>样式：</TD>
	<td colspan="2">
		<textarea id="styleContent" name="model.styleContent" style="display: none">${model.styleContent }</textarea>
		<IFRAME ID="eWebEditor1" src="<%=path%>/common/ewebeditor/ewebeditor.htm?id=styleContent&style=coolblue"
				frameborder="0" scrolling="no" width="750" height="500"></IFRAME>
	</td>
   </TR>
    
  
      
      
      </TBODY></TABLE>
      
      </form>
      


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

</DIV>
</BODY></HTML>
