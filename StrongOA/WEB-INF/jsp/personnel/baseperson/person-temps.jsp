<%@ page contentType="text/html; charset=utf-8"%>
<%@taglib prefix="s" uri="/struts-tags"%>
<%@include file="/common/include/rootPath.jsp"%>
<html>
  <head>
    <!--公用脚本，因为在这里用了OpenWindow方法，如果没有用到这个脚本里面的方法可以不引用 -->
		<SCRIPT language="javascript" src="<%=root%>/oa/js/personnel/common.js"></SCRIPT>
		<SCRIPT language="javascript" src="<%=root%>/oa/js/personnel/prototype.js"></SCRIPT>
		<script language="javascript">
			//如果是人员信息集，orgId和fkeyid值相同，keyid和personId相同，如果是人员子集，fkeyid和personId相同
			var msg="<s:property value="msg"/>";
			var keyid="<s:property value="keyid"/>";
			var fkeyid="<s:property value="fkeyid"/>";
			var orgId="<s:property value="orgId"/>";  
			var personId="<s:property value="personId"/>";
			var infoSetCode="<s:property value="infoSetCode"/>";
			var forward="<s:property value="forward"/>";
			if(msg!=null&&msg!=""){
				alert(msg);
			}
			window.returnValue=true;
			if(msg==""||msg=="null"||msg==null)
			{
				if(forward!=null&&forward=="saveAndClose"){
					window.parent.close();
					window.parent.dialogArguments.submitForm();
				}else if(forward!=null&&forward=="saveAndAdd"){
				    if(infoSetCode=="40288239230c361b01230c7a60f10015"){
				    	 window.parent.addnewPerson();
				    }else{
				    	 window.parent.addnewInfo();
				    }
				    window.parent.dialogArguments.submitForm();
				}else{
					window.location=contextPath+"/personnel/baseperson/person!initEditPerson.action?personId="+personId+"&orgId="+orgId+"&infoSetCode="+infoSetCode+"&keyid="+keyid;  
				    window.parent.setPersonId(personId);
				    window.parent.nextCard();
				    window.parent.dialogArguments.submitForm();
			    }
			}
			else
				window.location=contextPath+"/personnel/baseperson/person!initViewAddPerson.action?personId="+personId+"&orgId="+orgId+"&infoSetCode="+infoSetCode;
		</script>
  </head>
</html>
