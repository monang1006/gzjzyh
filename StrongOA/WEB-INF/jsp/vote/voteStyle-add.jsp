<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/common/include/rootPath.jsp"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib uri="/tags/security" prefix="security"%>
<HTML><HEAD><TITLE>调查样式</TITLE>
<LINK href="<%=path%>/oa/css/vote/style.css" type=text/css rel=stylesheet>
	<!--  引用公共样式文件,建议所有样式都以文件方式定义在jsp文件外部,通常定义在WebRoot目录下的CSS文件夹下-->
	<LINK href="<%=frameroot%>/css/properties_windows_add.css" type=text/css rel=stylesheet>
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
<table width="100%" border="0" cellspacing="0" cellpadding="00">
				<tr>
				<td colspan="3" class="table_headtd">
					<table width="100%" border="0" cellspacing="0" cellpadding="00">
						<tr>
							<td class="table_headtd_img" >
								<img src="<%=frameroot%>/images/ico/ico.gif" >&nbsp;
							</td>
							<td align="left">
								<script>
								var  styleId = "${model.styleId}";
						    	if(""==styleId|null==styleId|"undefined"==styleId|undefined==styleId){
									window.document.write("<strong>添加样式</strong>");
								}else{
									window.document.write("<strong>编辑样式</strong>");
								}
								</script>
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

<FORM name=form1 action="<%=root%>/vote/voteStyle!save.action" method=post>
<table  border="0" cellpadding="0" cellspacing="0" class="table1" width="100%" >
  <TBODY>
  <TR>
    <td class="biao_bg1" align="right"><font color="red">*</font>&nbsp;名称：<input type="hidden" value=${model.styleId} name=model.styleId /></TD>
    <td class="td1" style="padding-left:5px;"><INPUT class=text maxlength="100"  id="styleName" size="64" name=model.styleName value="${model.styleName}" /></TD></TR>
  <TR>
    <td class="biao_bg1" align="right">样式：</TD>
	<td class="td1" style="padding-left:5px;" colspan="2">
		<textarea id="styleContent" name="model.styleContent" style="display: none">${model.styleContent }</textarea>
		<IFRAME ID="eWebEditor1" src="<%=path%>/common/ewebeditor/ewebeditor.htm?id=styleContent&style=coolblue"
				frameborder="0" scrolling="no" width="750" height="500"></IFRAME>
	</td>
   </TR>
    
  <tr>
							<td class="table1_td"></td>
							<td></td>
						</tr>
      
      
      </TBODY></TABLE>
      
      </form>
      




</DIV>
</BODY></HTML>
