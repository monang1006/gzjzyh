<%@ page language="java" pageEncoding="utf-8"%>
<%@include file="/common/include/rootPath.jsp"%>
<%@taglib prefix="s" uri="/struts-tags"%>
<html>
	<head>
		<title>导入CSV、vCard文件</title>
		<%@include file="/common/include/meta.jsp"%>
		<LINK href="<%=frameroot%>/css/windows.css" type=text/css
			rel=stylesheet>
		<link href="<%=frameroot%>/css/properties_windows_add.css" type="text/css"
			rel=stylesheet>
		<link rel="stylesheet" type="text/css" href="<%=frameroot%>/css/global.css">
        <link rel="stylesheet" type="text/css" href="<%=frameroot%>/css/base.css">
		<script language="javascript"
			src="<%=path%>/common/js/jquery/jquery-1.2.6.js"></script>
		<script src="<%=path%>/common/js/common/common.js" type="text/javascript"></script>
<style>
.table_top_bg {
	background: url(<%=frameroot%>/images//table_top_bg.png) repeat-x;
	
}
.table_sjys {
	border: 1px solid #4096ee;
}

.titq1{
	height:50px;
	color:#000;
	font-size:14px;
	font-weight:bolder;
	line-height:55px;
	padding-left:15px;
	}
.list_yan{
	width:98%;
	
	
	}
.list_yan1{
	width:100%;
	}		
.list_yan li{
	height:30px;
	line-height:30px;
	margin-bottom:1px;
	background-color:#f4f4f4;
	padding-left:5px;
	}
.list_yan1 li{
	height:30px;
	line-height:30px;
	margin-bottom:1px;
	background-color:#f4f4f4;
	padding-left:5px;
	}	
.list_yan li.xuanzhong{
	background-color:#d7ebff;}	
.btn_b{
	width:73px;
	height:24px;
	border:none;
	background:url(<%=frameroot%>/images/but_hs.png);
	margin:10px;}		
.xianba{
	border-right:1px solid #4096ee;
	
	padding-right:18px;
 }
.xian2{
	padding-left:15px;
	} 

</style>
		<SCRIPT type="text/javascript">
		
			function changeForm(value){
				document.getElementById("myTableForm").submit();
			}
			
			function removeData(){
				var count = $(":radio:checked").size();
				if(count==0){
					alert("请至少选择一项。");
				}else{
					var addresstext = $(":radio:checked").parent().next();
					var ad_span = addresstext.find("span");	
					var ad_value=addresstext.find("input:hidden")
					var ad_value2=addresstext.find("input:checkbox");
					ad_span.text("");
					ad_value.eq(0).val("");
					ad_value2.eq(0).val("")
					ad_value2.eq(1).val("")
				}
			}
			
			function mapData(){
				var count = $(":radio:checked").size();
				if(count==0){
					alert("请至少选择一项。");
				}else{
					var subFormId=document.getElementById("subFormId").value;
					var plaintext = $(":checked").next().text();	
					$("#plaintext").val(plaintext);	
					var datatype=$(":checked").next().next().val();
					var addresstext = $(":radio:checked").parent().next();
					var ad_span = addresstext.find("span");	
					var ad_value=addresstext.find("input:hidden")
					var ad_value2=addresstext.find("input:checkbox");
					var ret=OpenWindow("<%=path%>/systemset/systemset!changeMap.action?dataType="+datatype+"&subFormId="+subFormId,"300","150",window);
					if(null!=ret && ""!=ret){
						if(ad_span.html() == null){
							addresstext.append("<span class=\"wz\">"+ret+"</span>");
						}else{		
							var retArr=ret.split("|");
							ad_span.text(retArr[0]);
							ad_value.eq(0).val(retArr[1]);
							ad_value2.eq(0).val(retArr[2])
							ad_value2.eq(1).val(retArr[3])
						}
					}
				}
			}
	
			
			function save(){
				var params="";
				var parentFormId=document.getElementById("parentFormId").value;
				var subFormId=document.getElementById("subFormId").value;
				$("td>input:hidden").each(function(){
					var ad_content = $(this).next().text();		//子表单字段描述	
					if($.trim(ad_content) != ""){
						var ad_type= $(this).next().next().val();			//子表单字段数据类型		
						var ad_size= $(this).next().next().next().val();	//子表单字段数据大小
						var obj= $(this).parent().prev();					
						var prevFieldValue = obj.find("input:radio").val();	//父表单字段名
						var prevFieldText = obj.find("span").text();		//父表单字段描述
						var objs=obj.find("input:checkbox");
						var prevFieldDataType = objs.eq(0).val();		//父表单字段数据类型
						var prevFieldLength = objs.eq(1).val();			//父表单字段数据大小
						params+=prevFieldValue+"&"+prevFieldText+"&"+prevFieldDataType+"&"+prevFieldLength+"="+$(this).attr("value")+"&"+ad_content+"&"+ad_type+"&"+ad_size+"Γ";
					}
				});
				if(params.length>0){
					params = params.substring(0,params.length-1);
				}//else{
				//	alert("没有映射内容！");
				//	return;
				//}
				$.ajax({
					type:"post",
					url:"<%=root%>/systemset/systemset!saveMapping.action",
					data:{
						params:params,
						parentFormId:parentFormId,
						subFormId:subFormId
					},
					success:function(data){
						alert("映射成功！");
					},
					error:function(){
						alert("对不起，映射失败。");
					}
				});
			}
		</SCRIPT>
	</head>
	<body class=contentbodymargin>
		<DIV id=contentborder align=center>
			<s:form id="myTableForm" name="myTableForm"
				action="/systemset/systemset!mapdata.action">
				<input id="plaintext" type="hidden" />
				<div align=left style="width: 100%">
					<table width="100%" border="0" cellspacing="0" cellpadding="00"
						height="40"
						style="FILTER:progid:DXImageTransform.Microsoft.Gradient(gradientType=0,startColorStr=#ededed,endColorStr=#ffffff);">
						<tr>
							<td class="table_headtd_img" >
								<img src="<%=frameroot%>/images/ico/ico.gif" >&nbsp;
							</td>
							<td align="left">
								数据映射
							</td>
							<td align="right">
								<table border="0" align="right" cellpadding="00" cellspacing="0">
					                <tr>
					                 	<td width="7"><img src="<%=frameroot%>/images/ch_h_l.gif"/></td>
					                 	<td class="Operation_input" onclick="save();">&nbsp;确&nbsp;定&nbsp;</td>
					                 	<td width="7"><img src="<%=frameroot%>/images/ch_h_r.gif"/></td>
				                  		<td width="5"></td>
					                 	
					                </tr>
					            </table>
							</td>
						</tr>
					</table>
				</div>
				
 <table width="100%" border="0" cellpadding="0" cellspacing="0">
  <tr >
    <td align="center">
      <table width="95%" border="0" cellpadding="0" cellspacing="0" class="table_sjys">
        <tr height="50" class="table_top_bg">
          <td colspan="2" align="center">
            <table width="95%" height="50" border="0" cellpadding="0" cellspacing="0" style="border-bottom:1px dashed #9d9d9d; background:url(<%=frameroot%>/images//jiantou.png) 50% no-repeat;">
            <tr>
             <td align="left" width="50%"><div style="padding-left:15px;">父流程启动表单：<s:select cssStyle="width: 250px" list="list"
								onchange="changeForm();" listKey="id" listValue="title"
								id="parentFormId" name="parentFormId"></s:select></div></td>
             <td align="left" width="50%"><div style="padding-left:30px;">子流程启动表单：<s:select cssStyle="width: 250px" list="list"
								onchange="changeForm();" listKey="id" listValue="title"
								id="subFormId" name="subFormId"></s:select></div></td>
            </tr>
            </table>
             
          </td>
        </tr>
        <tr >
       <td align="center" valign="top">
          <table width="95%" border="0" cellpadding="0" cellspacing="0" style="margin-top:10px;">
		        <tr>
		          <td align="left" width="50%" valign="top">
		           <div class="titq1">父流程启动表单字段</div>
		          </td>
		          <td width="50%" valign="top" align="left">
		         <div class="titq1">子流程启动表单字段</div>
		         
		          </td>
		        </tr> 
		 <s:iterator value="itemList" var="item">      
		 <tr>
          <td align="left" width="50%" valign="top" height="30">
          <%-- <ul class="list_yan">
            <li class="">--%> <input type="radio" name="items" value="<s:property value="infoItemField"/>">
			<span class="wz"><s:property value="infoItemSeconddisplay"/></span>
			<input type="checkbox" style="visibility:hidden" name="datatype" value="<s:property value="infoItemDatatype"/>">
			<input type="checkbox" style="visibility:hidden" name="datatype" value="<s:property value="infoItemLength"/>"><%-- </li>
          </ul>--%>
           
          </td>
          <td width="50%" valign="top" align="left" height="30" style="padding-left:10px;">
           <%-- <ul class="list_yan1">
            <li class="">--%><input type="hidden" name="mapFiled" value="<s:property value="mapFiled"/>">
			<span class="wz" style="padding-left:16px;"><s:property value="mapFildDesc"/></span>
			<input type="checkbox" style="visibility:hidden" name="datatype" value="<s:property value="mapFildType"/>">
			<input type="checkbox" style="visibility:hidden" name="datatype" value="<s:property value="mapFildSize"/>">
			<%-- </li>
            
          </ul>--%>
           
           
          </td>
       </tr>   
       
       </s:iterator>
		 <tr>
		 <td align="center">
	  <input class="btn_b" type="button" value="映&nbsp;&nbsp;射" onclick="mapData()"><input class="btn_b" type="button" value="清&nbsp;&nbsp;空" onclick="removeData()"></div>
         </td>
         <td></td>
		</tr>      
          </table>
            </td>
        </tr>
     
      </table></td>
  </tr>
</table>
			
			
			
			
			
			
			</s:form>
		</DIV>
	</body>
</html>
