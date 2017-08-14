<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="/tags/web-flex" prefix="webflex"%>
<%@ taglib uri="/tags/web-newdate" prefix="strong"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<%@include file="/common/include/rootPath.jsp"%>
<%
String msg = (String) request.getAttribute("msg");
%>
<HTML>
	<HEAD>
		<TITLE>班组管理</TITLE>
		<%@include file="/common/include/meta.jsp"%>
		<link href="<%=path%>/common/css/strongitmenu.css" type="text/css"
			rel="stylesheet">
		<!--右键菜单样式 -->
		<LINK href="<%=frameroot%>/css/properties_windows.css" type=text/css
			rel=stylesheet>
		<LINK href="<%=frameroot%>/css/search.css" type=text/css
			rel=stylesheet>
		<script language='javascript'
			src='<%=path%>/common/js/grid/ChangeWidthTable.js'></script>
		<SCRIPT language="javascript" src="<%=path%>/common/js/menu/menu.js"></SCRIPT>
		<!--右键菜单脚本 -->
		<script src="<%=path%>/common/js/jquery/jquery-1.2.6.js"
			type="text/javascript"></script>
		<script src="<%=path%>/common/js/validate/checkboxvalidate.js"
			type="text/javascript"></script>
		<script src="<%=path%>/common/js/common/common.js"
			type="text/javascript"></script>
		<script src="<%=path%>/common/js/common/search.js"
			type="text/javascript"></script>
		<script src="<%=path%>/oa/js/address/address.js"
			type="text/javascript"></script>
	</HEAD>
	<BODY class=contentbodymargin oncontextmenu="return false;"
		onload="initMenuT();">
		<script type="text/javascript"
			src="<%=path%>/common/js/newdate/WdatePicker.js"></script>
		<DIV id=contentborder align=center>
			<s:if test="(#request.registelist==null||#request.registelist.size()==0)&&#request.msg!=null&&#request.msg!=\"\"">
				<div align="center">
					<table>
						<tr>
							<td>
							</td>
						</tr>
					</table>
					<table width="500" height="280" border="0" align="center" 
						cellpadding="0" cellspacing="1" bgcolor="#B0DA8B">
						<tr>
							<td height="280" bgcolor="#EFFEF0">
								<table width="490" border="0" cellpadding="0" cellspacing="0">
									<tr>
										<td height="166" align="center" valign="top">
											<table width="356" border="0" align="center" cellpadding="0"
												cellspacing="0">
												<tr>
													<td width="128">
														<img src="http://statics1.fepss.cn/images/chucuo.jpg"
															height="103" width="128"></img>
													</td>
													<td width="228">
														<span
															style="font-size: 14px;font-weight: bold;color: #CC0000;"><%=msg%>
															<input type="button" class="input_bg" value="查看上下班记"
																	onclick="viewRegisterRecord()"></span>
													</td>	
												</tr>
											</table>
										</td>
									</tr>
								</table>
							</td>
						</tr>
					</table>
				</div>	
			</s:if>
			<s:else>
				<table width="100%" border="0" cellspacing="0" cellpadding="00">
					<tr>
						<td height="100%">
							<table width="100%" border="0" cellspacing="0" cellpadding="00">
								<tr>
									<td height="40"
										style="FILTER:progid:DXImageTransform.Microsoft.Gradient(gradientType=0,startColorStr=#ededed,endColorStr=#ffffff);">
										<table width="100%" border="0" cellspacing="0"
											cellpadding="00">
											<tr>
												<td>
												&nbsp;
												</td>
												<td width="30%">
													<img src="<%=frameroot%>/images/ico/ico.gif" width="9"
														height="9">&nbsp;
													考勤登记列表
												</td>
												<td width="70%">
													<table width="100%" border="0" align="right"
														cellpadding="0" cellspacing="0">
														<tr>
															<td width="100%" align="right">
																<table width="100%" border="0" align="right"
																	cellpadding="0" cellspacing="0">
																	<tr>
																		<td width="*">
																			&nbsp;
																		</td>
																		<td width="58" align="right">
																			<a class="Operation" href="javascript:register();">
																				<img src="<%=root%>/images/ico/tianjia.gif"
																					width="15" height="15" class="img_s">登记</a>
																		</td>
																		<td width="5"></td>
																		<td width="85" align="right">
																			<a class="Operation" href="javascript:describe();">
																				<img src="<%=root%>/images/ico/bianji.gif"
																					width="15" height="15" class="img_s">情况说明</a>
																		</td>
																		<td width="5"></td>
																		<td width="100" align="right">
																			<a class="Operation" href="javascript:viewRegisterRecord();">
																				<img src="<%=root%>/images/ico/chakan.gif"
																					width="15" height="15" class="img_s">查询登记考勤</a>
																		</td>
																		<td width="5"></td>
																	</tr>
																</table>
															</td>
														</tr>
														<tr higth="20"></tr>
														<tr>
														</tr>
													</table>
												</td>
											</tr>
										</table>
									</td>
								</tr>
								<webflex:flexTable name="myTable" width="100%" height="370px"
									wholeCss="table1" property="scheduleId" isCanDrag="true"
									showSearch="false" isCanFixUpCol="true" clickColor="#A9B2CA"
									footShow="showCheck" isShowMenu="true" 
									getValueType="getValueByProperty" collection="${registelist}">
									<webflex:flexCheckBoxCol caption="选择" property="scheduleId"
										showValue="sequencecode" width="5%" isCheckAll="true"
										isCanDrag="false" isCanSort="false"></webflex:flexCheckBoxCol>
									<webflex:flexTextCol caption="登记序号" property="sequencecode"
										showValue="sequencecode" width="15%" isCanDrag="true"
										isCanSort="true"></webflex:flexTextCol>
									<webflex:flexDateCol caption="出勤日期" property="attendDate"
										showValue="attendDate" width="15%" dateFormat="yyyy-MM-dd"
										isCanDrag="true" isCanSort="true"></webflex:flexDateCol>
									<webflex:flexTextCol caption="登记类型" property="attendType"
										showValue="attendType" width="15%" isCanDrag="true"
										isCanSort="true"></webflex:flexTextCol>
									<webflex:flexTextCol caption="开始登记时间" property="registerStime"
										showValue="registerStime" width="10%" showsize="20"
										isCanDrag="true" isCanSort="true"></webflex:flexTextCol>
									<webflex:flexTextCol caption="规定时间" property="regulationTime"
										showValue="regulationTime" width="10%" showsize="20"
										isCanDrag="true" isCanSort="true"></webflex:flexTextCol>
									<webflex:flexTextCol caption="结束登记时间" property="registerEtime"
										showValue="registerEtime" width="10%" showsize="20"
										isCanDrag="true" isCanSort="true"></webflex:flexTextCol>
									<webflex:flexDateCol caption="登记时间" property="registerTime"
										showValue="registerTime" width="20%" showsize="20"
										dateFormat="yyyy-MM-dd HH:mm:ss" isCanDrag="true"
										isCanSort="true"></webflex:flexDateCol>
								</webflex:flexTable>
							</table>
						</td>
					</tr>
				</table>
			</s:else>
		</DIV>
		<script language="javascript">
	
		var sMenu = new Menu();
		function initMenuT(){
			sMenu.registerToDoc(sMenu);
			var item = null;
				item = new MenuItem("<%=root%>/images/ico/tianjia.gif","登记","register",1,"ChangeWidthTable","checkMoreDis");
				sMenu.addItem(item);
				item = new MenuItem("<%=root%>/images/ico/bianji.gif","情况说明","describe",1,"ChangeWidthTable","checkOneDis");
				sMenu.addItem(item);
				item = new MenuItem("<%=root%>/images/ico/chakan.gif","查询登记考勤","viewRegisterRecord",1,"ChangeWidthTable","checkOneDis");
				sMenu.addItem(item);
			sMenu.addShowType("ChangeWidthTable");
		    registerMenu(sMenu);
		}
        //情况说明
		function describe(){
			var id=getValue();
			if(id==""){
					alert("请选择一条考勤登记记录！");
					return;
				}else if(id.indexOf(",")!=-1){
					alert("只能选择一条考勤登记记录！");
					return;
					}
					var ids=id.split("|");
		    var audit= window.showModalDialog("<%=root%>/attendance/register/register!descRegister.action?attendId="+ids[0],window,'help:no;status:no;scroll:no;dialogWidth:450px; dialogHeight:300px');
		
		}
		
	    function register(){
	    	var id=getValue();
			if(id==""){
				alert("请选择要登记的记录！");
			}else if(id.indexOf(",")!=-1){
				alert("有且只能选择一条登记记录！");
			}else{
				var arr=id.split("|");
				var myDate = new Date();
				var mytime=myDate.toLocaleTimeString();    		//获取当前时间
				mytime=mytime.substring(0,mytime.lastIndexOf(":"));
				if(mytime.length==4){
					mytime="0"+mytime;
				}
				var startTime=arr[1];
				var endTime=arr[2];
				var workTime=arr[4];
				if(startTime=="null"||startTime==""||startTime==null){
					startTime="00:00";
				}
				if(endTime=="null"||endTime==""||endTime==null){
					endTime="23:59";
				}
				if((startTime>workTime||workTime>endTime)&&startTime>endTime){	//处理特殊情况，如23:00-01:00-2:00,或者22:00-23:00-01:00
		 	   		if((mytime>=startTime&&mytime<="23:59")||(mytime>="00:00"&&mytime<=endTime)){
		 	   		}else{
		 	   			alert("不在登记时间范围内！");
		 	   			return;
		 	   		}		 	   		
		 	   	}else{	//处理一般情况，如16:00-17:00-18:00,或者00:00-01:00-02:00
			 		if(mytime<startTime){		//还没到登记时间
						alert("不在登记时间范围内！");
						return;
					}else if(mytime>endTime){	//已过登记时间
						alert("不在登记时间范围内！");
						return;
					}
			 	}
			    var str=myDate.format("yyyy-MM-dd hh:mm:ss");
				window.location="<%=path%>/attendance/register/register!attendRegister.action?attendId="+arr[0]+"&attendType="+arr[3]+"&registerTime="+str;		
			}
	    }
	    
	    Date.prototype.format = function(format){   
   			var o = {   
     		"M+" : this.getMonth()+1, //month   
     		"d+" : this.getDate(),    //day   
     		"h+" : this.getHours(),   //hour   
    		 "m+" : this.getMinutes(), //minute   
     		"s+" : this.getSeconds(), //second   
     		"q+" : Math.floor((this.getMonth()+3)/3), //quarter   
    		"S" : this.getMilliseconds() //millisecond   
  			}   
	   		if(/(y+)/.test(format))
	   		 	format=format.replace(RegExp.$1,(this.getFullYear()+"").substr(4 - RegExp.$1.length));   
			for(var k in o)if(new RegExp("("+ k +")").test(format))   
	  			format = format.replace(RegExp.$1,RegExp.$1.length==1 ?o[k]:("00"+ o[k]).substr((""+ o[k]).length));   
			return format;   
		}   
		
		function viewRegisterRecord(){
			window.location="<%=path%>/attendance/register/register!viewRegisterRecord.action";	
		}
		
		function submitForm(){
		}
	</script>
	</BODY>
</HTML>
