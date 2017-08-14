<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@include file="/common/include/rootPath.jsp"%>
<%@ taglib uri="/tags/security" prefix="security"%>
<html>
	<head>
		<%@include file="/common/include/meta.jsp"%>
		<title>类型管理</title>
		<LINK type=text/css rel=stylesheet href="<%=frameroot%>/css/properties_windows_add.css">
		<script src="<%=path%>/common/js/jquery/jquery-1.2.6.js" type="text/javascript" language="javascript"></script>
		<script src="<%=path%>/common/js/upload/jquery.MultiFile.js" type="text/javascript" language="javascript"></script>
		<script src="<%=path%>/common/js/upload/jquery.blockUI.js" type="text/javascript" language="javascript"></script>
		<script language="JavaScript" src="<%=path%>/common/js/commontab/service.js"></script>
		<script language="JavaScript" src="<%=path%>/common/js/commontab/workservice.js"></script>
		<script src="<%=path%>/common/js/common/common.js" type="text/javascript"></script>
		<SCRIPT>
		var num = '${pccmodel.pccNum}';
		var name = '${pccmodel.pccName}';
		var temp = '${addoredit}';
		if(temp!=1){
			num='';
		}
		function save(){
			//document.getElementById("myForm"),submit();
			var strTemp;
			var pccName = trim($("#pccName").val());
			var pccOther = trim($("#pccOther").val());
			var pccNum = trim($("#pccNum").val());
			var temp = "";
			if(pccName==null||pccName==""){
				temp = "类型名称不能为空。\n";
				strTemp="pccName";
			}else{
				if(pccName.length>20){
					temp = temp + "类型名称长度不能大于20。\n";
					strTemp="pccName";
				}
			}
			if(pccOther!=null&&pccOther!=""){
				if(pccOther.length>500){
					temp = temp + "类型备注输入长度不能大于500。\n";
					strTemp="pccOther";
				}
			}
			if(pccNum!=null&&pccNum!=""){
				if(pccNum.length>8){
					temp = temp + "类型排序号输入长度不能大于8。\n";
					strTemp="pccNum";
				}
			}
			if(temp!=null&&temp!=""){
				alert(temp);
				//定位光标
				document.getElementById(strTemp).focus();
				return;
			}
			//判断序号是否重复
			var url = "<%=root%>/publiccontact/publicContact!isRepeat.action";
			$.ajax({
				type:"post",
				dataType:"text",
				url:url,
				data:{pccNum:pccNum,pccName:pccName},
				success:function(data){
					if(data=="true"){
						$("#myForm").submit();
					}else if(data=="numFalse"){
						var mmm = $("#pccNum").val();
						if(num==mmm){
							$("#myForm").submit();
						}else{
							var id=$("#pccId").val();
							if(id!=""){
							if(confirm("类型排序号已存在，是否继续保存？")){
								$("#myForm").submit();
							}
							}else{
								$("#myForm").submit();
							}
						}
					}else if(data=="nameFalse"){
						var nnn = $("#pccName").val();
						if(name==nnn){
							$("#myForm").submit();
						}else{
							alert("类型名称已存在，请重新输入。");
							return;
						}
					}else{
						var mmm = $("#pccNum").val();
						var nnn = $("#pccName").val();
						if(name!=nnn){
							alert("类型名称已存在，请重新输入。");
							return;
						}
						if(num!=mmm){
							if(confirm("类型排序号已存在，是否继续保存？")){
								$("#myForm").submit();
							}
						}else{
							$("#myForm").submit();
						}
					}
					}
			});
		}
		function trim(str){ //删除左右两端的空格
	　　     return str.replace(/(^\s*)|(\s*$)/g, "");
	　　 }
	　　 function ltrim(str){ //删除左边的空格
	　　     return str.replace(/(^\s*)/g,"");
	　　 }
	　　 function rtrim(str){ //删除右边的空格
	　　     return str.replace(/(\s*$)/g,"");
	　　 }
//特殊字符过滤
//匹配中文 数字 字母 下划线       
function checkInput(vvv) {
	var s = vvv.value;
	if(s==""||s==null) return;
	//var pattern = new RegExp("[`~!@#$^&*()=|{}':;',\\[\\].<>/?~！@#￥……&*（）&mdash;—|{}【】‘；：”“'。，、？]")
    var pattern = new RegExp("[`~!@#$^&*()=|{}':;'\\[\\].<>/?~！@#￥……&*（）&mdash;—|{}【】‘；：”“'。？]")
    var rs = "";
	var ttt =/^[A-Za-z]+$/;//英文字母
	var k;
    for (var i = 0; i < s.length; i++) {
        //rs = rs + s.substr(i, 1).replace(pattern, '');
        //alert(pattern.test(s.substr(i,i+1)))
        var mmm = s.substr(i,1);
        if(ttt.test(mmm)){
        	continue;
        }
        if(pattern.test(mmm)){
        	//alert();
        	k=i;
        	rs="……";
        	break;
        }
    }
    if(rs!=""){
    	alert("不允许输入特殊字符。");
    	vvv.value = vvv.value.substring(0,k);
    }else{
    	return  true;
    }
}
 		</SCRIPT>
	</head>
	<base target="_self"/>
	<body class=contentbodymargin  oncontextmenu="return false;">
		<DIV id=contentborder align=center>
		<s:form id="myForm" theme="simple"  action="/publiccontact/publicContact!saveType.action" method="post">
			<input type="hidden" id="pccmodel.pccId" name="pccmodel.pccId" value="${pccmodel.pccId}">
			<table width="100%" border="0" cellspacing="0" cellpadding="0" style="vertical-align: top;">
				<tr>
					<td height="100%">
							<table width="100%" border="0" cellspacing="0" cellpadding="0">
								<tr>
									<td colspan="3" class="table_headtd">
										<table width="100%" border="0" cellspacing="0" cellpadding="00">
											<tr>
												<td class="table_headtd_img" >
													<img src="<%=frameroot%>/images/ico/ico.gif" >&nbsp;
												</td>
												<td align="left">
													<s:if test="#request.addoredit!=1"><strong>新建类型</strong></s:if>
													<s:else><strong>编辑类型</strong></s:else>
												</td>
												<td align="right">
													<table border="0" align="right" cellpadding="00" cellspacing="0">
														<tr>
								               		        <td width="7"><img src="<%=frameroot%>/images/ch_h_l.gif"/></td>
										                 	<td class="Operation_input" onclick="save();">&nbsp;保&nbsp;存&nbsp;</td>
										                 	<td width="7"><img src="<%=frameroot%>/images/ch_h_r.gif"/></td>
									                  		<td width="5"></td>
										                 	<td width="8"><img src="<%=frameroot%>/images/ch_z_l.gif"/></td>
										                 	<td class="Operation_input1" onclick="window.close()">&nbsp;取&nbsp;消&nbsp;</td>
										                 	<td width="7"><img src="<%=frameroot%>/images/ch_z_r.gif"/></td>
									                  		<td width="6"></td>
														</tr>
													</table>
													
												</td>
											</tr>
										</table>
									</td>
								</tr>
							</table>
							<table  border="0" cellpadding="0" cellspacing="1" class="table1" width="100%" >
								<tr>
									<td  class="biao_bg1" align="right">
										<span class="wz" ><font color="red">*</font>&nbsp;名称：</span>
                                    </td>
                                    <td class="td1">
										<input type="text" id="pccName" name="pccmodel.pccName" value="${pccmodel.pccName}"
										maxlength="20" onkeyup="checkInput(this);">
								  	</td>
								</tr>
								<tr>
									<td  class="biao_bg1" align="right">
										<span class="wz">序号：</span>
									</td>
									<td class="td1">
										<input type="text" id="pccNum" name="pccmodel.pccNum" value="${pccmodel.pccNum}"
										onkeyup="this.value=this.value.replace(/\D/g,'')"  onafterpaste="this.value=this.value.replace(/\D/g,'')"
										maxlength="8">
								  	</td>
								</tr>
								<tr>
									<td class="biao_bg1" align="right">
										<span class="wz">备注：</span>	
									</td>
									<td class="td1">
										<textarea rows="5" cols="16" id="pccOther" name="pccmodel.pccOther" maxlength="500" onkeyup="checkInput(this);">${pccmodel.pccOther}</textarea> 
								  	</td>
								</tr>
							</table>
							<div style="height: 20px;"></div>
					</td>
				</tr>
			</table>
						</s:form>
		</DIV>
	</body>
</html>
