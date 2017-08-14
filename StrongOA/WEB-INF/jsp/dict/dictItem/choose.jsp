<%@ page contentType="text/html; charset=utf-8"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<%@include file="/common/include/rootPath.jsp"%>
<html>
	<head>
		<%@include file="/common/include/meta.jsp"%>
		<title>增加字典</title>
		<link href="<%=frameroot%>/css/properties_windows_list.css" type="text/css" rel=stylesheet>
		<LINK href="<%=frameroot%>/css/toolbar.css" type="text/css" rel=stylesheet>
		<script src="<%=root%>/common/scripts/validator.js" language="javascript"></script>
		<script src="<%=root%>/common/scripts/handleEnter.js" language="javascript"></script>
		<script src="<%=root%>/common/scripts/common.js" language="javascript"></script>
		<script language=javascript>
		function submitForm(action){                                  //validator
   			if(submitValidateForm()){
    			if(Validator.Validate(DictForm,2)){
    				DictForm.action.value = action;
      	    		DictForm.submit();
  			 	}
  			 }
		}


		String.prototype.trim = function() {
			var strTrim = this.replace(/(^\s*)|(\s*$)/g, "");
			strTrim = strTrim.replace(/^[\s　\t]+|[\s　\t]+$/, ""); 
			return strTrim;
		}
			
	    function submitValidateForm(){
				//新建模板字段长度控制,长度脚本控制
			return true;	
	    }
       
        function cancel(){
         	location = "<%=root%>/dic/Listtype.do";
        }
         
        function getValue(obj)
        {
           	var dictType=obj.value;
           	parent.project_work_tree.location="<%=root%>/dict/dictType/dictType!tree.action?type="+dictType;       
        }
        
        function selectDict()
        {
        	var dictType=document.getElementById("dictType").value;
        	var url= "<%=root%>/fileNameRedirectAction.action?toPage=dict/dictItem/temp.jsp?dictType="+dictType;
	    	showModalDialog(url,window,'dialogWidth:250pt ;dialogHeight:100pt;status:no;help:no;scroll:no;status:0;help:0;scroll:0;');
        }

</script>

	</head>
	<BODY class="contentbodymargin">
		<div id=contentborder style="overflow:hidden; background-color:#eeeff3;">
		<table width="100%" border="0" cellspacing="0" cellpadding="0" style="vertical-align: top;">
		  <tr>
		  <td vlign="top">
		  <table width="100%" border="0" cellpadding="0" cellspacing="0" class="table1_search">
			<tr>
			<td>
			 字典类型:&nbsp;<SELECT id="dictType" name="model.dictType" style="border:1px solid #b3bcc3;background-color:#ffffff;" onChange="getValue(this)" >
						<OPTION value=''>
							全部
						</OPTION>
						<OPTION value='A'>
							国标码
						</OPTION>
						<OPTION value='B'>
							地方码
						</OPTION>
						<OPTION value='C'>
							自定义码
						</OPTION>
						<OPTION value='D'>
							其他
						</OPTION>
					</SELECT>&nbsp;<input id="img_sousuo" type="button" onClick="selectDict()"/>
			</td>
			</tr>
		</table>
		</td>
		</tr>
		</table>
	</div>
	</body>
</html>
