<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="/tags/web-statictree" prefix="s"%>
<%@include file="/common/include/rootPath.jsp"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<!-- saved from url=(0058)http://111.111.111.111:0000/chinaspis/perspective_toolbar.jsp -->

<HTML>
	<HEAD>
		<TITLE>导航器内容</TITLE>
		<script type="text/javascript">
			var imageRootPath='<%=frameroot%>';
		</script>
		<LINK href="<%=frameroot%>/css/navigator_windows.css" type=text/css rel=stylesheet>
		<LINK href="<%=frameroot%>/css/treeview.css" type=text/css rel=stylesheet>
		<SCRIPT src="<%=jsroot%>/mztree_check/mztreeview_check.js"></SCRIPT>
		
<SCRIPT type="text/javascript" language="java">
	function navigates(url,title){
		window.parent.parent.actions_container.personal_properties_toolbar.navigate("<%=path%>/"+url,title);
	}
</SCRIPT>
	</HEAD>
	<BODY class=contentbodymargin>
		<DIV id=treecontentborder>
			<table border="0" cellpadding="0" cellspacing="0" width="95%"
				height="100%">
				<tr>
					<td valign=top>
						<%
	String parentid = (String)request.getParameter("parentid") ;
%>
						<%
	if(parentid==null||"111".equals(parentid)){
%>
						<s:tree iconpath="frame/theme_gray/images/" title="领导办公"
							target="personal_properties_content">
							<s:node name="工作处理" id="gzcl2">
								<s:node name="在办工作"
									onclick='navigates("work/work!workinglist.action","在办工作")' id="zbgg2"></s:node>
								<s:node name="新建工作"
									onclick='navigates("work/work.action","新建工作")' id="xjgz2"></s:node>
								<s:node name="待办工作"
									onclick='navigates("work/work.action?listMode=1","待办工作")' id="dbgz2"></s:node>
								<s:node name="主办工作"
									onclick='navigates("work/work.action?listMode=2","主办工作")' id="yjgz2"></s:node>
								<s:node name="已办工作"
									onclick='navigates("work/work.action?listMode=3","已办工作")' id="ybgz2"></s:node>
								<s:node name="工作提示" 
									onclick='navigates("personal_office/work/work_workClew.jsp","工作提示")' id="gzts2"></s:node>
							</s:node>
							<s:node name="工作代办" id="gzdb">
								<s:node name="工作委托"
									onclick='navigates("personal_office/work/work_workConsign.jsp","工作委托")' id="gzwt"></s:node>
								<s:node name="秘书代办"
									onclick='navigates("personal_office/work/work_SECcommission.jsp","秘书代办")' id="msdb"></s:node>
								<s:node name="秘书办理"
									onclick='navigates("personal_office/work/work_SECtransact.jsp","秘书办理")' id="msbl"></s:node>
							</s:node>
							<s:node name="日程管理" id="rcgl">
								<s:node name="个人日程" onclick='navigates("personal_office/calendar/cal_dayview.jsp","个人日程")' id="grrc" />
								<s:node name="共享日程" onclick='navigates("personal_office/calendar/cal_shareview.jsp","共享日程")' id="gxrc" />
								<s:node name="领导日程" onclick='navigates("personal_office/calendar/cal_leaderview.jsp","领导日程")' id="ldrc" />
							</s:node>
							<s:node name="个人邮箱" id="mymail2">
								<s:node name="个人邮箱" id="mymailinfo2" onclick='navigates("fileNameRedirectAction.action?toPage=mymail/mail_container.jsp","个人邮箱")'></s:node>
							</s:node>
							<s:node name="流程监控" id="lcjk">
								<s:node name="流程监控"
									onclick='navigates("personal_office/work/work_flowManage.jsp","流程监控")' id="lcjk1"></s:node>
							</s:node>
							<s:node name="通讯录" id="txl2">
								<s:node name="个人通讯录"
									onclick='navigates("fileNameRedirectAction.action?toPage=address/address-personal.jsp","个人通讯录")' id="grtxl2" />
								<s:node name="系统通讯录" 
									onclick='navigates("fileNameRedirectAction.action?toPage=address/addressOrg.jsp","系统通讯录")' id="xttxl2" />
							</s:node>
							<s:node name="个人信息" id="personinfo2">
								<s:node name="个人信息" id="personinforeal2"
									onclick='navigates("personal_office/myinfo/myinfo_index.jsp","个人信息")'></s:node>
								<s:node name="数字证书" id="databook2"></s:node>
							</s:node>
						</s:tree>
						<%
 	}else if("112".equals(parentid)){
%>
						<s:tree iconpath="frame/theme_gray/images/" title="个人办公"
							target="personal_properties_content">
							<s:node name="工作处理" id="gzcl">
								<s:node name="在办工作"
									onclick='navigates("personal_office/work/work_workinglist.jsp","在办工作")' id="zbgg"></s:node>
								<s:node name="新建工作"
									onclick='navigates("personal_office/work/work_workDraft.jsp","新建工作")' id="xjgz"></s:node>
								<s:node name="待办工作"
									onclick='navigates("personal_office/work/work_pendingWork.jsp","待办工作")' id="dbgz"></s:node>
								<s:node name="主办工作"
									onclick='navigates("personal_office/work/work_alreadyWorkDraft.jsp","主办工作")' id="yjgz"></s:node>
								<s:node name="已办工作"
									onclick='navigates("personal_office/work/work_alreadyworklist.jsp","已办工作")' id="ybgz"></s:node>
								<s:node name="工作提示" 
								    onclick='navigates("personal_office/work/work_workClew.jsp","工作提示")' id="gzts"></s:node>
							</s:node>
							<s:node name="日程管理" id="rcgl2">
								<s:node name="个人日程" onclick='navigates("calendar/calendar.action","个人日程")' id="grrc2" />
								<s:node name="共享日程" onclick='navigates("calendar/calendar.action","共享日程")'  id="gxrc2" />
								<s:node name="领导日程" onclick='navigates("personal_office/calendar/cal_leaderview.jsp","领导日程")' id="ldrc2" />
							</s:node>
							<s:node name="个人邮箱" id="mymail">
								<s:node name="个人邮箱" id="mymailinfo" 
									onclick='navigates("personal_office/mymail/mail_container.html","个人邮箱")'></s:node>
							</s:node>
							<s:node name="流程监控" id="lcjk2">
								<s:node name="流程监控"
									onclick='navigates("personal_office/work/work_flowManage.jsp","流程监控")' id="lcjk21"></s:node>
							</s:node>
							<s:node name="通讯录" id="txl">
								<s:node name="个人通讯录"
									onclick='navigates("fileNameRedirectAction.action?toPage=address/address-personal.jsp","个人通讯录")' id="grtxl" />
								<s:node name="系统通讯录" onclick='navigates("fileNameRedirectAction.action?toPage=address/addressOrg.jsp","系统通讯录")'
									id="xttxl" />
							</s:node>
							<s:node name="个人信息" id="personinfo">
								<s:node name="个人信息" id="personinforeal"
									onclick='navigates("personal_office/myinfo/myinfo_index.jsp","个人信息")'></s:node>
								<s:node name="数字证书" id="databook"></s:node>
							</s:node>
							<s:node name="手机短信" id="sjdx">
								<s:node name="发送历史"
									onclick='navigates("sms/sms.action","手机短信")' id="fsls"></s:node>
							</s:node>
							<s:node name="文件柜" id="wjg">
								<s:node name="个人文件柜"
									 onclick='navigates("prsnfldr/privateprsnfldr/prsnfldrFolder!content.action","个人文件柜")' id="grwjg"></s:node>
								<s:node name="共享文件柜"
									 onclick='navigates("fileNameRedirectAction.action?toPage=prsnfldr/privateprsnfldr/prsnfldrFolder-sharecontent.jsp","共享文件柜")' id="gxwjg"></s:node>	 
								<s:node name="公共文件柜"
									 onclick='navigates("fileNameRedirectAction.action?toPage=prsnfldr/publicprsnfldr/publicPrsnfldrFolder-content.jsp","公共文件柜")' id="ggwjg"></s:node>	 
							</s:node>
						</s:tree>
						<%
  	}else if("113".equals(parentid)){
%>
						<s:tree iconpath="frame/theme_gray/images/" title="综合办公"
							target="personal_properties_content">
							<s:node name="发文管理" id="gwfwgl">
								<s:node name="公文草拟"
									onclick='navigates("senddoc/sendDoc!draft.action","公文草拟")'
									id="gwcn" />
								<s:node name="待办公文"
									onclick='navigates("senddoc/sendDoc!todo.action","待办公文")'
									id="dbgw" />
								<s:node name="主办公文"
									onclick='navigates("senddoc/sendDoc!hostedby.action","主办公文")'
									id="zbgw" />
								<s:node name="已办公文"
									onclick='navigates("senddoc/sendDoc!processed.action","已办公文")'
									id="ybgw" />
								<s:node name="公文统计"
									onclick='navigates("senddoc/sendDoc!statistics.action","公文统计")'
									id="gwtj" />
							</s:node>
							<s:node name="收文管理" id="gwswgl">
								<s:node name="来文登记"
									onclick='navigates("recvdoc/recvDoc.action?tableName=T_OARECVDOC","来文登记")'
									id="lwdj" />
								<s:node name="待办来文"
									onclick='navigates("recvdoc/recvDoc!todo.action?workflowType=1741","待办来文")'
									id="pbgw" />
								<s:node name="主办来文"
									onclick='navigates("recvdoc/recvDoc!hostedby.action?workflowType=1741","主办来文")'
									id="yngw" />
								<s:node name="已办来文"
									onclick='navigates("recvdoc/recvDoc!processed.action?workflowType=1741","已办来文")'
									id="ybgw" />
								<s:node name="来文统计"
									onclick='navigates("integrated_Office/recvdoc/recvdoc_status.jsp","来文统计")' id="lwtj" />
							</s:node>
							<s:node name="档案管理" id="dagl">
								<s:node name="年内文件管理"
									onclick='navigates("/archive/tempfile/tempFile.action","年内文件管理")'
									id="nnwjgl" />
								<s:node name="案卷归档"
									onclick='navigates("/fileNameRedirectAction.action?toPage=archive/archivefolder/folderContent.jsp?moduletype=pige","案卷归档")'
									id="ajgd" />
								<s:node name="案卷管理"
									onclick='navigates("/fileNameRedirectAction.action?toPage=archive/archivefolder/folderContent.jsp?moduletype=manage","案卷管理")'
									id="ajgl" />
								<s:node name="类目管理" onclick='navigates("/fileNameRedirectAction.action?toPage=archive/sort/sortContent.jsp","类目管理")' id="lmgl" />
								<s:node name="我的借阅文件" onclick='navigates("archive/archiveborrow/archiveBorrow.action","我的借阅文件")' id="wdjywj" />
								<s:node name="浏览借阅" onclick='navigates("/fileNameRedirectAction.action?toPage=archive/archivefolder/folderContent.jsp?moduletype=searchborrowFile","浏览借阅")' id="lljy" />
								<s:node name="借阅审核" onclick='navigates("archive/archiveborrow/archiveBorrow!auditing.action","借阅审核")' id="jysh" />
								<s:node name="案卷销毁" onclick='navigates("/archive/archiveDestr/archiveDestr.action","案卷销毁")' id="ajxh" />
							</s:node>
							<s:node name="督察督办" id="dcdb">
								<s:node name="草拟通知单"
									onclick='navigates("fileNameRedirectAction.action?toPage=inspect/inspect-newaddlist.jsp","草拟通知单")'
									id="dbdc" />
								<s:node name="督察处理"
									onclick='navigates("integrated_Office/inspect/inspect_axaminelist.jsp","督察处理")'
									id="dcsp" />
								<s:node name="已办督察"
									onclick='navigates("integrated_Office/inspect/inspect_proposedlist.jsp","已办督察")'
									id="ybdc" />
								<s:node name="已批督察"
									onclick='navigates("integrated_Office/inspect/inspect_approvelist.jsp","已批督察")'
									id="ypdc" />
							</s:node>
							<s:node name="会议管理" id="hygl">
								<s:node name="会议议题分类"
									onclick='navigates("integrated_Office/meeting/titleClassific/meeting_classficTitle.jsp","会议议题分类")'
									id="hyytfl" />
								<s:node name="会议议题"
									onclick='navigates("integrated_Office/meeting/title/meeting_titleList.jsp","会议议题")'
									id="hyyt" />
								<s:node name="制定会议计划"
									onclick='navigates("integrated_Office/meeting/meetingPlan/planList.jsp","制定会议计划")'
									id="zdhyjh" />
								<s:node name="会议记录"
									onclick='navigates("integrated_Office/meeting/meetingRecord/recordList.jsp","会议记录")'
									id="hyjl" />
							</s:node>
							<s:node name="新闻公告" id="tzgg">
								<s:node name="公告栏" onclick='navigates("notify/notify.action","公告栏")' id="tzggggl" />
								<s:node name="我发布的公告" onclick='navigates("notify/notify!mylist.action","我发布的公告")' id="wfbdgg" />
							</s:node>
						</s:tree>
						<%
  	}else if("114".equals(parentid)){
%>
						<s:tree iconpath="frame/theme_gray/images/" title="行政办公"
							target="personal_properties_content">
							
							<s:node name="会议室管理" id="hysgl">
								<s:node name="会议室管理"
									onclick='navigates("logistics_management/meetingroom/meetingRoom/meetingRoomList.jsp","会议室管理")'
									id="hysgl1" />
								<s:node name="会议室申请"
									onclick='navigates("logistics_management/meetingroom/application/applicationList.jsp","会议室申请")'
									id="hyssq" />
								<s:node name="会议室安排"
									onclick='navigates("logistics_management/meetingroom/arrangements/arrangementsList.jsp","会议室安排")'
									id="hysap" />
								<s:node name="会议室使用记录"
									onclick='navigates("logistics_management/meetingroom/roomRecord/roomRecordList.jsp","会议室使用记录")'
									id="hyssyjl" />
							</s:node>
							<s:node name="人事管理" id="personalmanagement">
								<s:node name="值班管理" id="zbgl">
									<s:node name="来访类型管理"
										onclick='navigates("personnel_management/watch/watchTypeList.jsp","来访类型管理")' id="lflxgl" />
									<s:node name="值班记录管理"
										onclick='navigates("personnel_management/watch/watchList.jsp","值班记录管理")' id="zbjlgl" />
								</s:node>
							</s:node>
						</s:tree>
						<%
  	}else if("115".equals(parentid)){
%>
						<s:tree iconpath="frame/theme_gray/images/" title="系统管理"
							target="personal_properties_content">
							<s:node name="用户管理" id="yhglmk">
								<s:node name="机构管理" onclick='navigates("/fileNameRedirectAction.action?toPage=/organisemanage/orgmanage-content.jsp","机构管理")' id="zzgl"></s:node>
								<s:node name="用户管理" onclick='navigates("/fileNameRedirectAction.action?toPage=/usermanage/usermanage-content.jsp","用户管理")' id="yhgl"></s:node>
								<s:node name="用户组管理" onclick='navigates("/fileNameRedirectAction.action?toPage=/usergroup/userContent.jsp","用户组管理")' id="yhzgl"></s:node>
								<s:node name="岗位管理" onclick='navigates("/postmanage/postContent!list.action","岗位管理")' id="mkgl"></s:node>
								<s:node name="角色管理" onclick='navigates("/rolemanage/baseRole.action","角色管理")' id="jsgl"></s:node>
								<s:node name="系统管理" onclick='navigates("/basesystem/baseSystem.action","系统管理")' id="yhxtgl"></s:node>	
								<s:node name="权限管理" onclick='navigates("/fileNameRedirectAction.action?toPage=/privilmanage/privilcontent.jsp","权限管理")' id="qxgl"></s:node>					
							</s:node>
							<s:node name="词语过滤" id="chgl">
								<s:node name="词语管理" onclick='navigates("/updatebadwords/phrasemanage/phrase.action","词语管理")' id="cygl"></s:node>
								<s:node name="过滤模块" onclick='navigates("/updatebadwords/phrasefilter/phraseFilter.action","过滤模块")' id="glmk"></s:node>
							</s:node>
							<s:node name="手机短信设置" id="sjdxsz">
								<s:node name="发送历史查看"
									onclick='navigates("sms/sms!allList.action","发送历史")' id="fslsck"></s:node>
								<s:node name="手机短信猫配置"
									onclick='navigates("sms/smsConf!input.action","短信猫配置")' id="sjdxmpz"></s:node>
								<s:node name="用户权限设置"
									onclick='navigates("collaborative_tools/sms/sms_sendconfine_list.jsp","用户权限设置")' id="yhcxsz"></s:node>
							</s:node>
							<s:node name="公文处理设置" id="gwclsz">
								<s:node name="公文文号管理"
									onclick='navigates("docnumber/docNumberHistory.action","公文文号管理")'
									id="gwwhgl" />
								<s:node name="公文模板管理" 
									onclick='navigates("/fileNameRedirectAction.action?toPage=doctemplate/doctempItem/doctempItemContent.jsp","公文模板管理")' 
									id="gwmbgl"/>
								<s:node name="公文套红管理" 
									onclick='navigates("/fileNameRedirectAction.action?toPage=docredtemplate/docreditem/docredItemContent.jsp","公文套红管理")' 
									id="gwthgl"/>
							</s:node>
							<s:node name="工作流管理" id="gzlsz">
							 	<s:node name="流程设计" onclick='navigates("workflowDesign/action/processFile.action", "流程设计")' id="lcsj" />
							  	<s:node name="流程类型" onclick='navigates("workflowDesign/action/processType.action", "流程类型")' id="lclx" />
                			  	<s:node name="流程委派" onclick='navigates("workflowDelegation/action/processDelegation.action","流程委派")' id="lcwp" />
                             	<s:node name="流程监控" onclick='navigates("workflowDesign/action/processMonitor!mainFrame.action","流程监控")' id="lcjk" />
              				</s:node>
							<s:node name="管理系统资源" onclick='navigates("system_management/resource/resourceInfo.jsp","管理系统资源")' id="glxtzy">
								<s:node name="系统资源查询" onclick='navigates("system_management/resource/resourceContent.jsp","系统资源查询")' id="xtzycx"></s:node>
								<s:node name="系统资源回收" onclick='navigates("system_management/resource/resourceManagement.jsp","系统资源回收")' id="xtzyhs"></s:node>
								<s:node name="系统资源设置" onclick='navigates("system_management/resource/resourceSet.jsp","系统资源设置")' id="xtzysz"></s:node>
							</s:node>
							<s:node name="字典管理" id="zdgl">
								<s:node name="字典类管理" onclick='navigates("/dict/dictType/dictType.action","字典类管理")' id="zdngl"></s:node>
								<s:node name="字典项管理" onclick='navigates("/fileNameRedirectAction.action?toPage=dict/dictItem/dictitemContent.jsp","字典项管理")' id="zdxgl"></s:node>
							</s:node>
							<s:node name="信息表管理" id="STRUCTURE">
								<s:node name="信息集管理" onclick='navigates("/fileNameRedirectAction.action?toPage=infotable/infoset/structureContent.jsp","信息集管理")' id="xxjgl"></s:node>
								<s:node name="信息项管理" onclick='navigates("/fileNameRedirectAction.action?toPage=infotable/infoitem/propertyContent.jsp","信息项管理")' id="xxxgl"></s:node>
								<s:node name="信息项分类" onclick='navigates("/fileNameRedirectAction.action?toPage=infotable/infotype/propertyTypeContent.jsp","信息项分类")' id="xxxfl"></s:node>
								<s:node name="信息表接口试用" onclick='navigates("/infotable/infoTable.action?tableName=T_OA_PERSONNEL_COMPANY","信息表接口试用")' id="xxbsy1"></s:node>
								<s:node name="信息表接口试用2" onclick='navigates("/infotable/infoTable!content.action?tableName=T_OA_PERSONNEL_COMPANY&fpro=COMPANY_PARENT_CODE&otherPro=COMPANY_NAME&functable=T_OA_PERSONNEL_PERSON","信息表接口试用2")' id="xxbsy2"></s:node>
							</s:node>
							<s:node name="控制面板" id="kzmb">
								<s:node name="界面设置" onclick='navigates("/theme/theme!input.action","界面设置")' id="jmsz"></s:node>
								<s:node name="菜单快捷组" onclick='navigates("/shortcutmenu/fastMenu.action","菜单快捷组")' id="cdkjz"></s:node>
								<s:node name="自定义桌面" onclick='navigates("/system_management/gloableStyle/setDesk.jsp","自定义桌面")' id="zdyzm"></s:node>
							</s:node>
							<s:node name="访问控制" onclick='navigates("/ipaccess/setipscope/ipScope.action","访问控制")' id="ysgl"></s:node>
						</s:tree>
						<%
	}
%>
					</td>
				</tr>
			</table>
		</DIV>
	</BODY>
</HTML>
