<%@ page contentType="text/html; charset=utf-8"%>
<head>
<style type="text/css">

.ui-tabs-panel{
padding-top: 0px !important;
padding-bottom: 0px !important;
}
.ui-widget-content{

}
.ui-corner-bottom{

}
}
</style>
</head>
<!-- 新增tab页对应内容区 -->
<div id="tabs-8" height="100%" width="100%">
	<Fieldset style="border: 1px solid #C0C0C0;">
	<LEGEND align=left style="font-size:9pt;">
		&nbsp;
		<span id=tabpage1_1>办理模式</span>&nbsp;
	</LEGEND>
	<table align="left">
		<tr>
			<td align="right"><input type="radio" id="doNotSelect" name="radio" onclick="doNotSelectButton()" /></td>
			<td align="left"><label for="doNotSelect">正常办理</label></td>
			
			<td align="right"><input type="radio" id="chkSuggestionButton" name="radio" onclick="doCheckSuggestionButton(this)" /></td>
			<td align="left"><label for="chkSuggestionButton">快捷办理</label>
				<input type="hidden" name="plugins_isSuggestionButton" value="0">
			</td>
			
			<td align="right"><input type="radio" id="chkIsPop" name="radio" onclick="doCheckIsPop(this)" /></td>
			<td align="left"><label for="chkIsPop">弹出</label>
				<input type="hidden" name="plugins_isPop" value="0">	
			</td>
			<td align="right"><input type="radio" id="chkIsMenu" name="radio" onclick="doCheckIsMenu(this)" /></td>
			<td align="left"><label for="chkIsMenu">菜单</label>
				<input type="hidden" name="plugins_isMenu" value="0">	
			</td>
			<td align="right"><input type="radio" id="chkIsOpen" name="radio" onclick="doCheckIsOpen(this)" /></td>
			<td align="left"><label for="chkIsOpen">展开</label>
				<input type="hidden" name="plugins_isOpen" value="0">	
			</td>
			<td></td>
			<td></td>
		</tr>
	</table>
	</Fieldset>
	
	<Fieldset style="border: 1px solid #C0C0C0;">
	<LEGEND align=left style="font-size:9pt;">
		&nbsp;
		<span id=tabpage1_1>按钮名称</span>&nbsp;
	</LEGEND>
	<TABLE border=0 width="100%" height="100%"
		style="font-size:9pt;">
		<!-- 这里提供按钮名称设置,用于在办理流程时动态设置按钮名称 -->
		<TR valign=top>
			<TD></TD>
			<TD>
				<script type="text/javascript">
					function valdata(obj){
						var FF_Char=new Array("\;","\'","\"","\\","\,","\/","}","{","(",")","[","]");//非法字符
						var temp = obj.value;
						var flag= false;
						for(var i=0;i<FF_Char.length;i++){
							if(temp.indexOf(FF_Char[i])!=-1){
								flag = true;
								alert(FF_Char[i]+'为非法字符！');
								obj.value = "";
								break;
							}
						}
					}
				</script>
				<span id=tabpage1_3>提交下一处理人</span>&nbsp;&nbsp;
				<%--<INPUT TYPE="text" NAME="plugins_buttonname" value="" onkeyup="valdata(this)" maxlength="8" class=txtput>--%>
				<INPUT TYPE="text" NAME="plugins_buttonname" value=""  maxlength="8" class=txtput>
			</TD>
			<TD>
				<span id="tabpage2_7">处&nbsp;&nbsp;理&nbsp;&nbsp;状&nbsp;态</span>&nbsp;&nbsp;
				<INPUT TYPE="text" NAME="plugins_toViewState" value=""  maxlength="8" class=txtput>
			</TD>
		</TR>
		<TR valign=top>
			<TD></TD>
			<TD>
				<span id="tabpage2_7">保&nbsp;&nbsp;存&nbsp;&nbsp;并&nbsp;&nbsp;关&nbsp;&nbsp;闭</span>&nbsp;&nbsp;
				<INPUT TYPE="text" NAME="plugins_toSave" value=""  maxlength="8" class=txtput>
			</TD>
			<TD>
			<span id="tabpage2_7">退&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;回</span>&nbsp;&nbsp;
				<INPUT TYPE="text" NAME="plugins_toBack" value=""  maxlength="8" class=txtput>
			</TD>
		</TR>
		<TR valign=top>
			<TD></TD>
			<TD>
				<span id="tabpage2_7">驳&nbsp;&nbsp;
				&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
				&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;回</span>&nbsp;&nbsp;
				<INPUT TYPE="text" NAME="plugins_toReturnBack" value=""  maxlength="8" class=txtput>
			</TD>
			<TD>
			<span id="tabpage2_7">打印处理单&nbsp;</span>&nbsp;&nbsp;
				<INPUT TYPE="text" NAME="plugins_toPrint" value=""  maxlength="8" class=txtput>
			</TD>
		</TR>
		<TR valign=top>
			<TD></TD>
			<TD>
				<span id="tabpage2_7">退回上一处理人</span>&nbsp;&nbsp;
				<INPUT TYPE="text" NAME="plugins_toPrev" value=""  maxlength="8" class=txtput>
			</TD>
			<TD>
			<span id="tabpage2_7">主&nbsp;&nbsp;办&nbsp;&nbsp;变&nbsp;更</span>&nbsp;&nbsp;
				<INPUT TYPE="text" NAME="plugins_toChangeMainActor" value=""  maxlength="8" class=txtput>
			</TD>
		</TR>
		<TR valign=top>
			<TD></TD>
			<TD>
				<span id="tabpage2_7">保&nbsp;&nbsp;存&nbsp;&nbsp;或&nbsp;&nbsp;提&nbsp;&nbsp;交</span>&nbsp;&nbsp;
				<INPUT TYPE="text" NAME="plugins_toSaveOrToNext" value=""  maxlength="8" class=txtput>
			</TD>
			<TD>
			</TD>
		</TR>
		</TABLE>
	</Fieldset>
	
	<Fieldset style="border: 1px solid #C0C0C0;">
	<LEGEND align=left style="font-size:9pt;">
		&nbsp;
		<span id=tabpage1_1>流程办理设置</span>&nbsp;
	</LEGEND>
	<table align="left">
			<tr>
			<td >
			<input type="checkbox"
						id="chkshowrepeal" name="chkshowrepeal" onclick="doCheckShowRepeal(this)" value="0"> </span> &nbsp;
			<label for="chkshowrepeal">启用废除</label>
				<input type="hidden" name="plugins_showrepeal" id="plugins_showrepeal" value="0"/>	
			</td>
			<td>
			<input type="checkbox"
						id="chksuggestionrequired" name="chksuggestionrequired" onclick="doCheckSuggestionRequired(this)" value="0"> </span> &nbsp;
			<label for="chksuggestionrequired">审批意见必填</label>
				<input type="hidden" name="plugins_suggestionrequired" id="plugins_suggestionrequired" value="0"/>	
			</td>
			<td>
			<input type="checkbox"
						id="chknotoverrule" name="chknotoverrule" onclick="doCheckNotOverRule(this)" value="0"> </span> &nbsp;
			<label for="chknotoverrule">不可驳回到此节点</label>
				<!--不可驳回到此节点 "0":可驳回|"1":不可驳回 默认值为"0"-->
				<input type="hidden" name="plugins_notoverrule" id="plugins_notoverrule" value="0"/>	
			</td>
			</tr>
			<tr>
			<td>
			<input type="checkbox"
						id="chkmustFetchTask" name="chkmustFetchTask" onclick="doCheckMustFetchTask(this)" value="0"> </span> &nbsp;
			<label for="chkmustFetchTask">启用已办文强制取回</label>
				<input type="hidden" name="plugins_mustFetchTask" id="plugins_mustFetchTask" value="0"/>
			</td>
			<td>
			<input type="checkbox"
						id="chknotbackspace" name="chknotbackspace" onclick="doCheckNotBackspace(this)" value="0"> </span> &nbsp;
			<label for="chknotbackspace">不可退回到此节点</label>
				<input type="hidden" name="plugins_notbackspace" id="plugins_notbackspace" value="0"/>
			</td>
			<td>
			</td>
			<td>
			</td>
			</tr>
			<tr>
			<td>
			</td>
			<td>
			</td>
			<td>
			</td>
			</tr>
	</table>
	</Fieldset>

	<Fieldset style="border: 1px solid #C0C0C0;">
		<LEGEND align=left style="font-size:9pt;">
			&nbsp;
			<span id=tabpage2_1>其他</span>&nbsp;
		</LEGEND>
		<TABLE border=0 width="100%" height="100%"
			style="font-size:9pt;">

			<TR valign=top>
				<TD width=5></TD>
				<TD>
					<!-- 
					<label for="chkYjzxSuggestion">意见征询节点</label>
					</span>
					<input type="checkbox" id="chkYjzxSuggestion" onclick="doCheckYjzxSuggestion(this)" />
					<input type="hidden" name="plugins_chkYjzxSuggestion" value="0">
					&nbsp;&nbsp;&nbsp;
					 -->
					<input type="checkbox" id="chkShowBlankSuggestion" onclick="doCheckShowBlankSuggestion(this)"/>
					<span id=tabpage2_7><label for="chkShowBlankSuggestion">允许表单显示空白意见</label></span>&nbsp;&nbsp;
					<input type="hidden" name="plugins_chkShowBlankSuggestion" value="0">
					<input type="checkbox" id="chkShowBackSuggestion" onclick="doCheckShowBackSuggestion(this)"/>
					<span id=tabpage2_7><label for="chkShowBackSuggestion">允许表单显示退回意见</label></span>&nbsp;&nbsp;
					<input type="hidden" name="plugins_chkShowBackSuggestion" value="0">
					&nbsp;&nbsp;
					<input type="checkbox" id="chkNeedSign" onclick="doCheckSign(this)"/>
					<span id=tabpage2_7><label for="chkNeedSign">需签名认证</label></span>
					<input type="hidden" name="plugins_chkNeedSign" value="0">
				</TD>
				<TD></TD>
			</TR>
			<TR valign=top>
				<TD width=5></TD>
				<TD>
					<input type="checkbox" id="chkDoReturnTaskActor" onclick="doCheck(this)" />
					<span id=tabpage2_7><label for="chkDoReturnTaskActor">返回经办人</label></span>
					<input type="hidden" name="plugins_doReturnTaskActos" value="0">
					<span style="color:red;word-wrap:break-word;">设置【返回经办人】，则该节点提交下一步时不选择人员，直接指定下一步处理人为该节点上一步的处理人。</span>
					&nbsp;
					<br/>
					<input type="checkbox" id="chkModifySuggestion" onclick="doCheckModifySuggestion(this)"/>
					<span id=tabpage2_7>
					<!-- 允许修改审批意见 -->
					<label for="chkModifySuggestion">签收节点</label>
					</span>
					<input type="hidden" name="plugins_chkModifySuggestion" value="0">
					<span style="color:red;word-wrap:break-word;">设置【签收节点】，则【动态选择处理人】在业务里无效。</br> 必须满足：一、收文流程  二、节点名称和任务名称为"XX签收" 如："秘书处签收"</span>
					&nbsp;&nbsp;&nbsp;
					<br/>
					<input type="checkbox" id="chkModifySend" onclick="doCheckModifySend(this)" />
					<label for="chkModifySend">签发节点</label>
					<input type="hidden" name="plugins_chkModifySend" value="0">
				</TD>
				<TD></TD>
			</TR>
		</TABLE>
	</Fieldset>
	
	<Fieldset style="border: 1px solid #C0C0C0;">
		<LEGEND align=left style="font-size:9pt;">
			&nbsp;
			<span id=tabpage2_1>并发取消设置</span>&nbsp;
		</LEGEND>
		<TABLE border=0 width="100%" height="100%"
			style="font-size:9pt;">
			<TR valign=top>
				<TD width=5></TD>
				<TD>
					<input id="ConcurrencySet" type="checkbox" onclick="doCheckConcurrencySet(this)"/>
					<input type="hidden" name="plugins_ConcurrencySet" value="0">
					<label for="ConcurrencySet">
						取消并发不可选
					</label>
					<span style="color:red;word-wrap:break-word;">
						该选项适用于存在并发的流程且本节点是汇聚节点，实现在流程监控页面操作“取消并发”流程的控制，根据流程业务情况，控制本节点不能选作“取消并发”后跳转的节点
					</span>
					
				</TD>
				<TD></TD>
			</TR>
			<TR height="3">
				<TD></TD>
				<TD></TD>
				<TD></TD>
			</TR>
		</TABLE>
	</Fieldset>

	<Fieldset style="border: 1px solid #C0C0C0;">
		<LEGEND align=left style="font-size:9pt;">
			&nbsp;
			<span id=tabpage2_1>超期设置</span>&nbsp;
		</LEGEND>
		<TABLE border=0 width="100%" height="100%"
			style="font-size:9pt;">
			<TR valign=top>
				<TD width=5></TD>
				<td>
				<input type="checkbox"
							id="chklcqx" name="chklcqx" onclick="doCheckLcqx(this)" value="0"> </span> &nbsp;
				<label for="chklcqx">显示流程期限设置</label>
					<input type="hidden" name="plugins_lcqx" id="plugins_lcqx" value="0"/>
				</td>
				<TD width=5></TD>
				<td>
				<input type="checkbox"
						id="chkmustFetchTaskTimeOut" name="chkmustFetchTaskTimeOut" onclick="doCheckMustFetchTaskTimeOut(this)" value="0"> </span> &nbsp;
				<label for="chkmustFetchTaskTimeOut">流程超期强制取回至该节点</label>
				<input type="hidden" name="plugins_mustFetchTaskTimeOut" id="plugins_mustFetchTaskTimeOut" value="0"/>
				</td>
				<TD></TD>
			</TR>
			<TR valign=top>
				<TD width=5></TD>
				<td>
				<input type="checkbox" name="chkbacklast" onclick="doCheckBackLast(this)" value="0"> 
				<label for="chkmustFetchTaskTimeOut">任务超期自动退回上一环节处理人</label>
				<input type="hidden" name="plugins_backlast" id="plugins_backlast" value="0"/>	 &nbsp;	
				</td>
				<TD></TD>
			</TR>
			<TR height="3">
				<TD></TD>
				<TD></TD>
				<TD></TD>
				<TD></TD>
			</TR>
		</TABLE>
	</Fieldset>

</div>

<div id="tabs-9" height="100%" width="100%">
							<TABLE border=0 width="100%" height="100%">
								<TR valign=top>
									<TD></TD>
									<TD width="100%" valign=top>
										<Fieldset style="border: 1px solid #C0C0C0;">
											<LEGEND align=left style="font-size:9pt;">
												&nbsp;
												<span id=tabpage2_1>表单设置</span>&nbsp;
											</LEGEND>
											<TABLE border=0 width="100%" height="100%"
												style="font-size:9pt;">
											<!-- 增加挂接的任务节点的特殊业务字段 -->	
												<TR valign=top>
													<TD width=5></TD>
													<TD>
														<span id=tabpage2_8>审批域&nbsp;&nbsp;&nbsp;&nbsp;</span>&nbsp;&nbsp;
														<INPUT TYPE="text" NAME="plugins_businessFlagName" value="" class=txtput disabled>
														&nbsp;&nbsp;&nbsp;
														<a id="setFlag" href="#" onclick="setFlags()">选择域</a>
														<INPUT TYPE="hidden" NAME="plugins_businessFlag" value="">
													</TD>
													<TD></TD>
												</TR>
												<TR height="3">
													<TD></TD>
													<TD></TD>
													<TD></TD>
												</TR>
											</TABLE>
										</Fieldset>
									</TD>
									<TD>
										&nbsp;
									</TD>
								</TR>
								<TR valign=top>
									<TD></TD>
									<TD width="100%" valign=top>
										<Fieldset style="border: 1px solid #C0C0C0;">
											<LEGEND align=left style="font-size:9pt;">
												&nbsp;
												<span id=tabpage2_1>PDF权限设置</span>&nbsp;
											</LEGEND>
											<TABLE border=0 width="100%" height="100%"
												style="font-size:9pt;">
												<TR valign=top style="display:none">
													<TD width=5></TD>
													<TD>
														<span id=tabpage2_7>表单权限</span>&nbsp;&nbsp;
														<INPUT TYPE="text" NAME="formpriv" value="" class=txtput disabled>
														&nbsp;&nbsp;&nbsp;
														<a id="setpriv" href="#" onclick="setPrivs()">设置权限</a>
													</TD>
													<TD></TD>
												</TR>
												<TR valign=top>
													<TD width=5></TD>
													<TD>
														<table>
															<tr>
																<td>
																	<input type="checkbox" id="chkPermitUploadPDF" onclick="doPermitUploadPDF(this)" />
																	<label for="chkPermitUploadPDF">上传PDF权限</label>
																	<input type="hidden" name="plugins_permitUploadPDF" value="0">
																</td>
																<td>
																	<input type="checkbox" id="chkPermitUploadSMJ" onclick="doPermitUploadSMJ(this)" />
																	<label for="chkPermitUploadSMJ">上传扫描件权限</label>
																	<input type="hidden" name="plugins_permitUploadSMJ" value="0">
																</td>
															</tr>
														</table>
													</TD>
													<TD valign="bottom" align="right" style="text-align: right">
														<a href="#" onclick="selectAll4();">全选</a>&nbsp;
													</TD>
												</TR>
												<TR height="3">
													<TD></TD>
													<TD></TD>
													<TD></TD>
												</TR>
											</TABLE>
										</Fieldset>
									</TD>
									<TD>
										&nbsp;
									</TD>
								</TR>
								<TR valign=top>
									<TD></TD>
									<TD width="100%" valign=top>
										<Fieldset style="border: 1px solid #C0C0C0;">
											<LEGEND align=left style="font-size:9pt;">
												&nbsp;
												<span id=tabpage2_1>文件操作权限设置</span>&nbsp;
											</LEGEND>
											<TABLE border=0 width="100%" height="100%"
												style="font-size:9pt;">
												<TR valign=top style="display:none">
													<TD width=5></TD>
													<TD>
														<span id=tabpage2_7>表单权限</span>&nbsp;&nbsp;
														<INPUT TYPE="text" NAME="formpriv" value="" class=txtput disabled>
														&nbsp;&nbsp;&nbsp;
														<a id="setpriv" href="#" onclick="setPrivs()">设置权限</a>
													</TD>
													<TD></TD>
												</TR>
												<TR valign=top>
													<TD width=5></TD>
													<TD>
														<table>
															<tr style="display:none">
																<td>
																	<input type="checkbox" id="wordPrivil1" name="wordPrivil">
																	<label for="wordPrivil1">导出正文</label>
																</td>
																<!--屏蔽该选项   隐藏该td即可   不要直接删除 -->
																<td>
																	<input type="checkbox" id="wordPrivil2" name="wordPrivil">
																	<label for="wordPrivil2">导入模板</label>
																</td>
																<td>
																	<input type="checkbox" id="wordPrivil3" name="wordPrivil">
																	<label for="wordPrivil3">打印</label>
																</td>
															</tr>
															<tr>	
																<td>
																	<input type="checkbox" id="wordPrivil4" name="wordPrivil">
																	<label for="wordPrivil4">显示【保存草稿】按钮</label>
																</td>
																<td style="display:none">
																	<input type="checkbox" id="wordPrivil5" name="wordPrivil">
																	<label for="wordPrivil5">保存并关闭</label>
																</td>
																<td>
																	<input type="checkbox" id="wordPrivil6" name="wordPrivil">
																	<label for="wordPrivil6">草稿必填</label>
																</td>
															</tr>
														</table>
														<span style="color:red;word-wrap:break-word;">
							在第一个任务节点里，【保存草稿】、【草稿必填】功能是无效的。
					</span>
														
													</TD>
													<TD valign="bottom" align="right" style="text-align: right">
														<a href="#" onclick="selectAll1();">全选</a>&nbsp;
													</TD>
												</TR>
												<TR height="3">
													<TD></TD>
													<TD></TD>
													<TD></TD>
												</TR>
											</TABLE>
										</Fieldset>
									</TD>
									<TD>
										&nbsp;
									</TD>
								</TR>
								<TR valign=top>
									<TD></TD>
									<TD width="100%" valign=top>
										<Fieldset style="border: 1px solid #C0C0C0;">
											<LEGEND align=left style="font-size:9pt;">
												&nbsp;
												<span id=tabpage2_1>文件编辑权限设置</span>&nbsp;
											</LEGEND>
											<TABLE border=0 width="100%" height="100%" style="font-size:9pt;">
												<TR valign=top>
													<TD width=5></TD>
													<TD>
														<table>
															<tr>
																<td>
																	<input type="checkbox" id="wordPrivil7" name="wordPrivil">
																	<label for="wordPrivil7">保留痕迹</label>
																</td>
																<td>
																	<input type="checkbox" id="wordPrivil8" name="wordPrivil">
																	<label for="wordPrivil8">不保留痕迹</label>
																</td>
																<td>
																	<input type="checkbox" id="wordPrivil9" name="wordPrivil">
																	<label for="wordPrivil9">显示痕迹</label>
																</td>
																<td>
																	<input type="checkbox" id="wordPrivil10" name="wordPrivil">
																	<label for="wordPrivil10">隐藏痕迹</label>
																</td>
															</tr>
															<tr>
																<td>
																	<input type="checkbox" id="wordPrivil11" name="wordPrivil">
																	<label for="wordPrivil11">文件套红</label>
																</td>
																<td>
																	<input type="checkbox" id="wordPrivil12" name="wordPrivil">
																	<label for="wordPrivil12">擦除痕迹</label>
																</td>
																<!-- 
																<td>
																	<input type="checkbox" id="wordPrivil13" name="wordPrivil">
																	<label for="wordPrivil13">只读</label>
																</td>
																 -->
																<td>
																	<input type="checkbox" id="chkDoMarkModify" name="wordPrivil" onclick="doCheckMarkModify(this)" />
																	<span style="color: red;"><label for="wordPrivil14">默认不留痕迹</label></span>
																	<input type="hidden" name="plugins_doMarkModify" value="0">
																</td>
																<td>
																	<input type="checkbox" id="chkDoShowRevisions" name="wordPrivil" onclick="doCheckShowRevisions(this)" />
																	<span style="color: red;"><label for="wordPrivil15">默认显示痕迹</label></span>
																	<input type="hidden" name="plugins_doShowRevisions" value="0">
																</td>
															</tr>
														</table>
													</TD>
													<TD valign="bottom" align="right" style="text-align: right">
														<a href="#" onclick="selectAll2();">全选</a>&nbsp;
													</TD>
												</TR>
												<TR height="3">
													<TD></TD>
													<TD></TD>
													<TD></TD>
												</TR>
											</TABLE>
										</Fieldset>
									</TD>
									<TD>
										&nbsp;
									</TD>
								</TR>
								<TR valign=top style="display:none">
									<TD></TD>
									<TD width="100%" valign=top>
										<Fieldset style="border: 1px solid #C0C0C0;">
											<LEGEND align=left style="font-size:9pt;">
												&nbsp;
												<span id=tabpage2_1>电子认证权限设置</span>&nbsp;
											</LEGEND>
											<TABLE border=0 width="100%" height="100%"
												style="font-size:9pt;">
												<TR valign=top>
													<TD width=5></TD>
													<TD>
														<table>
															<tr>
																<td>
																	<input type="checkbox" id="wordPrivil16" name="wordPrivil">
																	<label for="wordPrivil14">加盖电子印章</label>
																</td>
																<td>
																	<input type="checkbox" id="wordPrivil17" name="wordPrivil">
																	<label for="wordPrivil15">加盖电子印章(从服务器)</label>
																</td>
																<!--  
																<td>
																	<input type="checkbox" id="wordPrivil18" name="wordPrivil">
																	<label for="wordPrivil16">生成二维条码
																</td>
																-->
																<td>
																	<input type="checkbox" id="wordPrivil19" name="wordPrivil">
																	<label for="wordPrivil17">插入手写签名</label>
																</td>
															</tr>
															<tr>
																<td>
																	<input type="checkbox" id="wordPrivil20" name="wordPrivil">
																	<label for="wordPrivil18">全屏手工绘画</label>
																</td>
																<td>
																	<input type="checkbox" id="wordPrivil21" name="wordPrivil">
																	<label for="wordPrivil19">全屏手写签名</label>
																</td>
																<td>
																	&nbsp;
																</td>
																<td>
																	&nbsp;
																</td>
															</tr>
														</table>
													</TD>
													<TD valign="bottom" align="right" style="text-align: right">
														<a href="#" onclick="selectAll3();">全选</a>&nbsp;
													</TD>
												</TR>
												<TR height="3">
													<TD></TD>
													<TD></TD>
													<TD></TD>
												</TR>
											</TABLE>
										</Fieldset>
									</TD>
									<TD>
										&nbsp;
									</TD>
								</TR>
								<TR valign=top style="display:none">
									<TD></TD>
									<TD width="100%" valign=top>
										<Fieldset style="border: 1px solid #C0C0C0;">
											<LEGEND align=left style="font-size:9pt;">
												&nbsp;
												<span id=tabpage2_1>挂接动作</span>&nbsp;
											</LEGEND>
											<TABLE border=0 width="100%" height="100%"
												style="font-size:9pt;">
												<TR valign=top>
													<TD width=5></TD>
													<TD>
														<span id=tabpage2_7>挂接动作</span>&nbsp;&nbsp;
														<INPUT TYPE="checkbox" NAME="isSetAction" value=""
															onclick="isSetAction();">
														&nbsp;&nbsp;&nbsp;
													</TD>
													<TD></TD>
												</TR>
												<TR valign=top>
													<TD width=5></TD>
													<TD>
														<span id=tabpage2_7>设置动作</span>&nbsp;&nbsp;
														<INPUT TYPE="text" NAME="actionSet" value="" class=txtput
															disabled>
														&nbsp;&nbsp;&nbsp;
													</TD>
													<TD></TD>
												</TR>
												<TR height="3">
													<TD></TD>
													<TD></TD>
													<TD></TD>
												</TR>
											</TABLE>
										</Fieldset>
									</TD>
									<TD>
										&nbsp;
									</TD>
								</TR>
								<TR height="100%">
									<TD></TD>
									<TD></TD>
									<TD></TD>
								</TR>
							</TABLE>
</div>

<div id="tabs-10" height="100%" width="100%">
										<Fieldset style="border: 1px solid #C0C0C0;">
		<LEGEND align=left style="font-size:9pt;">
			&nbsp;
			<span id=tabpage2_1>指定流程和表单</span>&nbsp;
		</LEGEND>
		<TABLE border=0 width="100%" height="100%"
			style="font-size:9pt;">
			<TR valign=top>
				<TD width=5></TD>
				<TD>
					<span id=tabpage2_2>流程指定</span>&nbsp;&nbsp;<INPUT TYPE="text" id="plugins_flowName1" NAME="plugins_flowName1" value="" class=txtput disabled>&nbsp;&nbsp;&nbsp;<a href="#" onclick="setProcess(this)">选择流程</a><INPUT id="plugins_flowId1" TYPE="hidden" NAME="plugins_flowId1" value="">&nbsp;&nbsp;&nbsp;<a href="#" onclick="clearData('plugins_flowName1','plugins_flowId1')">清空</a>
				</TD>
				<TD></TD>
			</TR>
			<TR valign=top>
				<TD width=5></TD>
				<TD>
					<span id=tabpage2_2>流程表单</span>&nbsp;&nbsp;<INPUT TYPE="text" id="plugins_formName1" NAME="plugins_formName1" value="" class=txtput disabled>&nbsp;&nbsp;&nbsp;<a href="#" onclick="setForms2(this)">选择表单</a><INPUT id="plugins_formId1" TYPE="hidden" NAME="plugins_formId1" value="">&nbsp;&nbsp;&nbsp;<a href="#" onclick="clearData('plugins_formName1','plugins_formId1')">清空</a>
				</TD>
				<TD></TD>
			</TR>
			<TR valign=top>
				<TD width=5></TD>
				<TD>
					<span id=tabpage2_2>流程指定</span>&nbsp;&nbsp;<INPUT TYPE="text" id="plugins_flowName2" NAME="plugins_flowName2" value="" class=txtput disabled>&nbsp;&nbsp;&nbsp;<a href="#" onclick="setProcess(this)">选择流程</a><INPUT id="plugins_flowId2" TYPE="hidden" NAME="plugins_flowId2" value="">&nbsp;&nbsp;&nbsp;<a href="#" onclick="clearData('plugins_flowName2','plugins_flowId2')">清空</a>
				</TD>
				<TD></TD>
			</TR>
			<TR valign=top>
				<TD width=5></TD>
				<TD>
					<span id=tabpage2_2>流程表单</span>&nbsp;&nbsp;<INPUT TYPE="text" id="plugins_formName2" NAME="plugins_formName2" value="" class=txtput disabled>&nbsp;&nbsp;&nbsp;<a href="#" onclick="setForms2(this)">选择表单</a><INPUT id="plugins_formId2" TYPE="hidden" NAME="plugins_formId2" value="">&nbsp;&nbsp;&nbsp;<a href="#" onclick="clearData('plugins_formName2','plugins_formId2')">清空</a>
				</TD>
				<TD></TD>
			</TR>
			<TR valign=top>
				<TD width=5></TD>
				<TD>
					<span id=tabpage2_2>流程指定</span>&nbsp;&nbsp;<INPUT TYPE="text" id="plugins_flowName3" NAME="plugins_flowName3" value="" class=txtput disabled>&nbsp;&nbsp;&nbsp;<a href="#" onclick="setProcess(this)">选择流程</a><INPUT id="plugins_flowId3" TYPE="hidden" NAME="plugins_flowId3" value="">&nbsp;&nbsp;&nbsp;<a href="#" onclick="clearData('plugins_flowName3','plugins_flowId3')">清空</a>
				</TD>
				<TD></TD>
			</TR>
			<TR valign=top>
				<TD width=5></TD>
				<TD>
					<span id=tabpage2_2>流程表单</span>&nbsp;&nbsp;<INPUT TYPE="text" id="plugins_formName3" NAME="plugins_formName3" value="" class=txtput disabled>&nbsp;&nbsp;&nbsp;<a href="#" onclick="setForms2(this)">选择表单</a><INPUT id="plugins_formId3" TYPE="hidden" NAME="plugins_formId3" value="">&nbsp;&nbsp;&nbsp;<a href="#" onclick="clearData('plugins_formName3','plugins_formId3')">清空</a>
				</TD>
				<TD></TD>
			</TR>
			<TR valign=top>
				<TD width=5></TD>
				<TD>
					<span id=tabpage2_2>流程指定</span>&nbsp;&nbsp;<INPUT TYPE="text" id="plugins_flowName4" NAME="plugins_flowName4" value="" class=txtput disabled>&nbsp;&nbsp;&nbsp;<a href="#" onclick="setProcess(this)">选择流程</a><INPUT id="plugins_flowId4" TYPE="hidden" NAME="plugins_flowId4" value="">&nbsp;&nbsp;&nbsp;<a href="#" onclick="clearData('plugins_flowName4','plugins_flowId4')">清空</a>
				</TD>
				<TD></TD>
			</TR>
			<TR valign=top>
				<TD width=5></TD>
				<TD>
					<span id=tabpage2_2>流程表单</span>&nbsp;&nbsp;<INPUT TYPE="text" id="plugins_formName4" NAME="plugins_formName4" value="" class=txtput disabled>&nbsp;&nbsp;&nbsp;<a href="#" onclick="setForms2(this)">选择表单</a><INPUT id="plugins_formId4" TYPE="hidden" NAME="plugins_formId4" value="">&nbsp;&nbsp;&nbsp;<a href="#" onclick="clearData('plugins_formName4','plugins_formId4')">清空</a>
				</TD>
				<TD></TD>
			</TR>
			<TR valign=top>
				<TD width=5></TD>
				<TD>
					<span id=tabpage2_2>流程指定</span>&nbsp;&nbsp;<INPUT TYPE="text" id="plugins_flowName5" NAME="plugins_flowName5" value="" class=txtput disabled>&nbsp;&nbsp;&nbsp;<a href="#" onclick="setProcess(this)">选择流程</a><INPUT id="plugins_flowId5" TYPE="hidden" NAME="plugins_flowId5" value="">&nbsp;&nbsp;&nbsp;<a href="#" onclick="clearData('plugins_flowName5','plugins_flowId5')">清空</a>
				</TD>
				<TD></TD>
			</TR>
			<TR valign=top>
				<TD width=5></TD>
				<TD>
					<span id=tabpage2_2>流程表单</span>&nbsp;&nbsp;<INPUT TYPE="text" id="plugins_formName5" NAME="plugins_formName5" value="" class=txtput disabled>&nbsp;&nbsp;&nbsp;<a href="#" onclick="setForms2(this)">选择表单</a><INPUT id="plugins_formId5" TYPE="hidden" NAME="plugins_formId5" value="">&nbsp;&nbsp;&nbsp;<a href="#" onclick="clearData('plugins_formName5','plugins_formId5')">清空</a>
				</TD>
				<TD></TD>
			</TR>
			<TR valign=top>
				<TD width=5></TD>
				<TD>
					<span id=tabpage2_2>流程指定</span>&nbsp;&nbsp;<INPUT TYPE="text" id="plugins_flowName6" NAME="plugins_flowName6" value="" class=txtput disabled>&nbsp;&nbsp;&nbsp;<a href="#" onclick="setProcess(this)">选择流程</a><INPUT id="plugins_flowId6" TYPE="hidden" NAME="plugins_flowId6" value="">&nbsp;&nbsp;&nbsp;<a href="#" onclick="clearData('plugins_flowName6','plugins_flowId6')">清空</a>
				</TD>
				<TD></TD>
			</TR>
			<TR valign=top>
				<TD width=5></TD>
				<TD>
					<span id=tabpage2_2>流程表单</span>&nbsp;&nbsp;<INPUT TYPE="text" id="plugins_formName6" NAME="plugins_formName6" value="" class=txtput disabled>&nbsp;&nbsp;&nbsp;<a href="#" onclick="setForms2(this)">选择表单</a><INPUT id="plugins_formId6" TYPE="hidden" NAME="plugins_formId6" value="">&nbsp;&nbsp;&nbsp;<a href="#" onclick="clearData('plugins_formName6','plugins_formId6')">清空</a>
				</TD>
				<TD></TD>
			</TR>

			<TR height="3">
				<TD></TD>
				<TD></TD>
				<TD></TD>
			</TR>
		</TABLE>
	</Fieldset>

</div>

<script>
/**
 * 动态添加业务扩展Tab页
 */
$(function() {
	$("#tabs").tabs('add', '#tabs-8', '扩展属性');
	$("#tabs").tabs('add', '#tabs-9', '扩展表单');
	$("#tabs").tabs('add', '#tabs-10', '扩展流程');
	
	$("input[name=canSelectOther]").hide();
	$("input[name=canSelectOther]").prev().hide();
	
	
	doCheckMustFetchTaskTimeOut(document.getElementById("chkmustFetchTaskTimeOut"));
	doCheckNotOverRule(document.getElementById("chknotoverrule"));
	doCheckNotBackspace(document.getElementById("chknotbackspace"));
	doCheckConcurrencySet(document.getElementById("ConcurrencySet"));
});

/**
 * 自定义验证接口，在保存时对扩展页面中扩展的业务属性进行正确性验证
 * 1.如果验证正确，则接口返回true
 * 2.如果验证失败，则接口直接alert错误信息，并返回false
 */
function customValidate(){
	// TODO：扩展属性验证逻辑
	
	var isOk = true;
	
	var preTranType = $("input[name=preTranType]:checked").val();
	if(preTranType != "1" && $("#ConcurrencySet").attr("checked")){
		$("#ConcurrencySet").attr("checked", false);
		$("#chkmustFetchTaskTimeOut").attr("disabled", false);
		alert("设置[取消并发时，选择要跳转的汇聚节点不包括此节点]，此节点必须是汇聚节点。");
		isOk = false;
	}
	
	if($("#chkModifySuggestion").attr("checked") && $("#chkDoReturnTaskActor").attr("checked")){
		alert("[返回经办人]和[签收节点]不能同时勾选。");
		isOk = false;
	}

	if($("#chkModifySuggestion").attr("checked") && parseInt($("input[name=maxActors]").val()) < 2){
		alert("设置[签收节点]时，[最大参与人数]必须大于1。");
		isOk = false;
	}
	
	//此处仅提示信息
	if($("input[name=taskstartaction]").val() != "com.strongit.oa.common.remind.WorkFlowRemindAction"){
		alert("[任务开始]需要设置默认值为 'com.strongit.oa.common.remind.WorkFlowRemindAction'(工作流流转过程中的消息提醒类)。");
		//isOk = false;
	}
	
	var flow1 = $("#plugins_flowName1").val();
	var form1 = $("#plugins_formName1").val();
	var flow2 = $("#plugins_flowName2").val();
	var form2 = $("#plugins_formName2").val();
	var flow3 = $("#plugins_flowName3").val();
	var form3 = $("#plugins_formName3").val();
	var flow4 = $("#plugins_flowName4").val();
	var form4 = $("#plugins_formName4").val();
	var flow5 = $("#plugins_flowName5").val();
	var form5 = $("#plugins_formName5").val();
	var flow6 = $("#plugins_flowName6").val();
	var form6 = $("#plugins_formName6").val();
	
	var fError = "流程指定和流程表单要同时填。";
	if(flow1 || form1){
		if(!(flow1 && form1)){
			alert(fError);
			isOk = false;
		}
	}
	if(flow2 || form2){
		if(!(flow2 && form2)){
			alert(fError);
			isOk = false;
		}
	}
	if(flow3 || form3){
		if(!(flow3 && form3)){
			alert(fError);
			isOk = false;
		}
	}
	if(flow4 || form4){
		if(!(flow4 && form4)){
			alert(fError);
			isOk = false;
		}
	}
	if(flow5 || form5){
		if(!(flow5 && form5)){
			alert(fError);
			isOk = false;
		}
	}
	if(flow6 || form6){
		if(!(flow6 && form6)){
			alert(fError);
			isOk = false;
		}
	}

	return isOk;
}

/**
 * 配置页面保存时执行的业务接口
 * 1.如果执行正确，则接口返回true
 * 2.如果执行失败，则接口直接alert错误信息，并返回false
 */
function onSave(){
	// TODO：配置页面保存时执行的业务逻辑
	return true;
}

/**
 * 配置页面取消时执行的业务接口
 * 1.如果执行正确，则接口返回true
 * 2.如果执行失败，则接口直接alert错误信息，并返回false
 */
function onCancel(){
	// TODO：配置页面取消时执行的业务逻辑
	return true;
}
</script>

<script type="text/javascript">
$(document).ready(function(){
	if($("#chkDoMarkModify").attr("checked")){
		$("#chkDoShowRevisions").attr("disabled",true);
	}else{
		if($("#chkDoShowRevisions").attr("checked")){
			$("#chkDoMarkModify").attr("disabled",true);
		}
	}
});
//指定表单
function setForms() {
	if (document.getElementsByName("setform")[0].disabled != "") {
		return false;
	}
	var vPageLink = scriptroot
			+ "/workflowDesign/action/processDesign!getAllForms.action?formId="
			+ document.getElementsByName("formid")[0].value;
	// var returnValue = OpenWindow(vPageLink,190,260,window);
	var returnValue = window
			.showModalDialog(
					vPageLink,
					window,
					"dialogWidth:270pt;dialogHeight:290pt;status:no;help:no;scroll:no;status:0;help:0;scroll:0;");
	if (returnValue != null) {
		var returnits = returnValue.split(",");
		var obj = document.getElementsByName("togetherform")[0];
		obj.value = returnits[1];
		var obj = document.getElementsByName("formid")[0];
		obj.value = returnits[0];
		var obj = document.getElementsByName("formpriv")[0];
		obj.value = "superadmin";
	}
}
//指定特殊的业务字段
function setFlags(){
	if(document.getElementsByName("setFlag")[0].disabled){
		return false;
	}
	var vPageLink = scriptroot + "/workflow/designer/flag-set.jsp";
	var formId = document.getElementsByName("formid")[0].value;
	var permitReassigned="";
	var topermitReassigned = document.getElementsByName("reAssign")[0].checked;//允许指派
	 if(topermitReassigned){
	   permitReassigned="1";
	 }
	formId = formId != null && formId != "" && formId != "0" ? formId : opener.processForm.split(",")[1];
	if(formId == 0){
		alert("流程未指定表单！");
		return;
	}
	var flagId = document.getElementsByName("plugins_businessFlag")[0].value;
	flagId = flagId != null && flagId != "" ? flagId : "";
	var params = [formId, flagId,permitReassigned, window];
	var returnValue = window.showModalDialog(vPageLink,params,"dialogWidth:430pt;dialogHeight:360pt;status:no;help:no;scroll:no;status:0;help:0;scroll:0;");
	if(returnValue != null){
        var returnits= returnValue.split(",");
		var obj=document.getElementsByName("plugins_businessFlagName")[0];
		obj.value=returnits[1];	
		var obj=document.getElementsByName("plugins_businessFlag")[0];
		obj.value=returnits[0];
	}					
}

//清空
function clearData(name,id){
	$("#"+name).val("");
	$("#"+id).val("");
}
//指定流程
function setProcess(hrefProcess){
	var id = $(hrefProcess).next();
	var vPageLink = scriptroot +"/workflowDesign/action/processDefinition!subProcess.action?subProcessId=" + id.val();
	var returnValue = window.showModalDialog(vPageLink,window,"dialogWidth:260pt;dialogHeight:360pt;status:no;help:no;scroll:no;status:0;help:0;scroll:0;");
	if(returnValue!=null){
		var returnits= returnValue.split(",");
		$(hrefProcess).prev().val(returnits[1]);
		id.val(returnits[0]);
	}	
}

//指定表单
function setForms2(hrefForm){
	var id = $(hrefForm).next();
	var vPageLink = scriptroot + "/workflowDesign/action/processDefinition!allForms.action?formId=" + id.val();
	var returnValue = window.showModalDialog(vPageLink,window,"dialogWidth:240pt;dialogHeight:360pt;status:no;help:no;scroll:no;status:0;help:0;scroll:0;");
	if(returnValue != null){
	        var returnits= returnValue.split(",");
			$(hrefForm).prev().val(returnits[1]);
			id.val(returnits[0]);
	}					
}

function initPlugins(){
  var returnTaskActor = opener.fSelectedObj.plugins["plugins_doReturnTaskActos"];
  if(returnTaskActor){
  	if(returnTaskActor == "1"){
	  	document.getElementById("chkDoReturnTaskActor").checked = true;
  	}
  }
  var chkDoMarkModify = opener.fSelectedObj.plugins["plugins_doMarkModify"];
  if(chkDoMarkModify){
  	if(chkDoMarkModify == "1"){
	  	document.getElementById("chkDoMarkModify").checked = true;
  	}
  }
   //是否可以打开新的PDF
  var chkPermitUploadPDF = opener.fSelectedObj.plugins["plugins_permitUploadPDF"];
  if(chkPermitUploadPDF){
  	if(chkPermitUploadPDF == "1"){
	  	document.getElementById("chkPermitUploadPDF").checked = true;
  	}
  }
  var chkPermitUploadSMJ = opener.fSelectedObj.plugins["plugins_permitUploadSMJ"];
  if(chkPermitUploadSMJ){
  	if(chkPermitUploadSMJ == "1"){
	  	document.getElementById("chkPermitUploadSMJ").checked = true;
  	}
  }
  var chkDoShowRevisions = opener.fSelectedObj.plugins["plugins_doShowRevisions"];
  if(chkDoShowRevisions){
  	if(chkDoShowRevisions == "1"){
	  	document.getElementById("chkDoShowRevisions").checked = true;
  	}
  }
  
  var chkNeedSign = opener.fSelectedObj.plugins["plugins_chkNeedSign"];
  if(chkNeedSign){
  	if(chkNeedSign == "1"){
	  	document.getElementById("chkNeedSign").checked = true;
  	}
  }
  
  var chkModifySuggestion = opener.fSelectedObj.plugins["plugins_chkModifySuggestion"];
  if(chkModifySuggestion){
  	if(chkModifySuggestion == "1"){
	  	document.getElementById("chkModifySuggestion").checked = true;
  	}
  }

  //是否显示签发按钮
  var chkModifySend = opener.fSelectedObj.plugins["plugins_chkModifySend"];
  if(chkModifySend){
  	if(chkModifySend == "1"){
	  	document.getElementById("chkModifySend").checked = true;
  	}
  }
  
  var chkYjzxSuggestion = opener.fSelectedObj.plugins["plugins_chkYjzxSuggestion"];
  if(chkYjzxSuggestion){
  	if(chkYjzxSuggestion == "1"){
	  	document.getElementById("chkYjzxSuggestion").checked = true;
  	}
  }
  //是否显示回退意见
  var chkShowBackSuggestion=opener.fSelectedObj.plugins["plugins_chkShowBackSuggestion"];
  if(chkShowBackSuggestion){
  	if(chkShowBackSuggestion=="1"){
  		document.getElementById("chkShowBackSuggestion").checked=true;
  	}
  }
  //是否显示空白意见
  var chkShowBlankSuggestion=opener.fSelectedObj.plugins["plugins_chkShowBlankSuggestion"];
  if(chkShowBlankSuggestion){
  	if(chkShowBlankSuggestion=="1"){
  		document.getElementById("chkShowBlankSuggestion").checked=true;
  	}
  }
  
  //是否自动退回上一办理人
  var chkModifySuggestion = document.getElementById("plugins_backlast").value;
  if(chkModifySuggestion){
  	if(chkModifySuggestion == "1"){
	  	document.getElementById("chkbacklast").checked = true;
  	}
  }
  //是否是快捷办理按钮
  var isSuggestionButton = document.getElementsByName("plugins_isSuggestionButton")[0].value;
  if(isSuggestionButton){
  	if(isSuggestionButton == "1"){
	  	document.getElementById("chkSuggestionButton").checked = true;
  	}
  }
  //是否弹出
  var chkIsPop = document.getElementsByName("plugins_isPop")[0].value;
  if(chkIsPop){
  	if(chkIsPop == "1"){
	  	document.getElementById("chkIsPop").checked = true;
  	}
  }
  //是否菜单
  var chkIsMenu = document.getElementsByName("plugins_isMenu")[0].value;
  if(chkIsMenu){
  	if(chkIsMenu == "1"){
	  	document.getElementById("chkIsMenu").checked = true;
  	}
  }
  
   //是否展开模式
  var chkIsOpen = document.getElementsByName("plugins_isOpen")[0].value;
  if(chkIsOpen){
  	if(chkIsOpen == "1"){
	  	document.getElementById("chkIsOpen").checked = true;
  	}
  }
  
  //是否正常办理
  if(document.getElementById("chkSuggestionButton").checked != true 
  		&& document.getElementById("chkIsPop").checked != true
  		&& document.getElementById("chkIsOpen").checked != true
  		&& document.getElementById("chkIsMenu").checked != true){
  	 document.getElementById("doNotSelect").checked = true;
  }
   //并发取消设置
  var chkConcurrencySet = document.getElementById("plugins_ConcurrencySet").value;
  if(chkConcurrencySet){
  	if(chkConcurrencySet == "1"){
	  	document.getElementById("ConcurrencySet").checked = true;
  	}
  }
  //是否启用废除
  var chkshowrepeal = document.getElementsByName("plugins_showrepeal")[0].value;
  if(chkshowrepeal){
  	if(chkshowrepeal == "1"){
	  	document.getElementById("chkshowrepeal").checked = true;
  	}
  }
  var chksuggestionrequired = document.getElementsByName("plugins_suggestionrequired")[0].value;
  if(chksuggestionrequired){
  	if(chksuggestionrequired == "1"){
	  	document.getElementById("chksuggestionrequired").checked = true;
  	}
  }
  var chknotoverrule = document.getElementsByName("plugins_notoverrule")[0].value;
  if(chknotoverrule){
  	if(chknotoverrule == "1"){
	  	document.getElementById("chknotoverrule").checked = true;
  	}
  }
  var chknotbackspace = document.getElementsByName("plugins_notbackspace")[0].value;
  if(chknotbackspace){
  	if(chknotbackspace == "1"){
	  	document.getElementById("chknotbackspace").checked = true;
  	}
  }
  /* yanjian 去除该设计省办公厅 意见征询的的处理代码 2012-06-19 20:19
  var chkyjzx = document.getElementById("plugins_yjzx").value;
  if(chkyjzx == "1"){
  	document.getElementById("chkyjzx").checked = true;
  	document.getElementById("plugins_yjzxname").style.display = "block";
  }
  */
  var chklcqx = document.getElementById("plugins_lcqx").value;
  if(chklcqx == "1"){
  	document.getElementById("chklcqx").checked = true;
  }
  
  var chkmustFetchTask = document.getElementById("plugins_mustFetchTask").value;
  if(chkmustFetchTask == "1"){
  	document.getElementById("chkmustFetchTask").checked = true;
  }
  
  var chkmustFetchTaskTimeOut = document.getElementById("plugins_mustFetchTaskTimeOut").value;
  if(chkmustFetchTaskTimeOut == "1"){
  	document.getElementById("chkmustFetchTaskTimeOut").checked = true;
	$("#chknotoverrule").attr("disabled", true);
	$("#chknotbackspace").attr("disabled", true);
	$("#ConcurrencySet").attr("disabled", true);
  }
  else{
	  document.getElementById("chkmustFetchTaskTimeOut").checked = false;
  }
}

function doNotSelectButton(){
	document.getElementsByName("plugins_isSuggestionButton")[0].value = "0";
	document.getElementsByName("plugins_isPop")[0].value = "0";
	document.getElementsByName("plugins_isMenu")[0].value = "0";
	document.getElementsByName("plugins_isOpen")[0].value = "0";
}

function doCheckSuggestionButton(chkObj){
	if(chkObj.checked){
		document.getElementsByName("plugins_isSuggestionButton")[0].value = "1";
		document.getElementsByName("plugins_isOpen")[0].value = "0";
		document.getElementsByName("plugins_isMenu")[0].value = "0";
		document.getElementsByName("plugins_isPop")[0].value = "0";
	}else{
		document.getElementsByName("plugins_isSuggestionButton")[0].value = "0";
	}
}

function doCheckIsPop(chkObj){
	if(chkObj.checked){
		document.getElementsByName("plugins_isPop")[0].value = "1";
		document.getElementsByName("plugins_isOpen")[0].value = "0";
		document.getElementsByName("plugins_isMenu")[0].value = "0";
		document.getElementsByName("plugins_isSuggestionButton")[0].value = "0";
	}else{
		document.getElementsByName("plugins_isPop")[0].value = "0";
	}
}

function doCheckIsMenu(chkObj){
	if(chkObj.checked){
		document.getElementsByName("plugins_isMenu")[0].value = "1";
		document.getElementsByName("plugins_isOpen")[0].value = "0";
		document.getElementsByName("plugins_isPop")[0].value = "0";
		document.getElementsByName("plugins_isSuggestionButton")[0].value = "0";
	}else{
		document.getElementsByName("plugins_isMenu")[0].value = "0";
	}
}

function doCheckIsOpen(chkObj){
	if(chkObj.checked){
		document.getElementsByName("plugins_isOpen")[0].value = "1";
		document.getElementsByName("plugins_isMenu")[0].value = "0";
		document.getElementsByName("plugins_isPop")[0].value = "0";
		document.getElementsByName("plugins_isSuggestionButton")[0].value = "0";
	}else{
		document.getElementsByName("plugins_isOpen")[0].value = "0";
	}
}

function doCheck(chkObj){
	if(chkObj.checked){
		document.getElementsByName("plugins_doReturnTaskActos")[0].value = "1";
	}else{
		document.getElementsByName("plugins_doReturnTaskActos")[0].value = "0";
	}
}

function doCheckMarkModify(chkObj){
	if(chkObj.checked){
		document.getElementsByName("plugins_doMarkModify")[0].value = "1";
		$("#chkDoShowRevisions").attr("disabled", true);
	}else{
		document.getElementsByName("plugins_doMarkModify")[0].value = "0";
		$("#chkDoShowRevisions").attr("disabled", false);;
	}
}

/**
 * 流程节点中上传PDF权限控制
 * @author 刘皙
 */
function doPermitUploadPDF(chkObj){
	if(chkObj.checked){
		document.getElementsByName("plugins_permitUploadPDF")[0].value = "1";
	}else{
		document.getElementsByName("plugins_permitUploadPDF")[0].value = "0";
	}
}

/**
 * 流程节点中上传扫描件权限控制
 * @author 刘皙
 */
function doPermitUploadSMJ(chkObj){
	if(chkObj.checked){
		document.getElementsByName("plugins_permitUploadSMJ")[0].value = "1";
	}else{
		document.getElementsByName("plugins_permitUploadSMJ")[0].value = "0";
	}
}

function doCheckShowRevisions(chkObj){
	if(chkObj.checked){
		document.getElementsByName("plugins_doShowRevisions")[0].value = "1";
		$("#chkDoMarkModify").attr("disabled", true);
	}else{
		document.getElementsByName("plugins_doShowRevisions")[0].value = "0";
		$("#chkDoMarkModify").attr("disabled", false);
	}
}

//plugins_chkNeedSign
function doCheckSign(chkObj){
	if(chkObj.checked){
		document.getElementsByName("plugins_chkNeedSign")[0].value = "1";
	}else{
		document.getElementsByName("plugins_chkNeedSign")[0].value = "0";
	}
}

function doCheckBackLast(chkObj){
	if(chkObj.checked){
		document.getElementsByName("plugins_backlast")[0].value = "1";
	}else{
		document.getElementsByName("plugins_backlast")[0].value = "0";
	}
}

function doCheckModifySuggestion(chkObj){
	if(chkObj.checked){
		document.getElementsByName("plugins_chkModifySuggestion")[0].value = "1";
	}else{
		document.getElementsByName("plugins_chkModifySuggestion")[0].value = "0";
	}
}

//签发
function doCheckModifySend(chkObj){
	if(chkObj.checked){
		document.getElementsByName("plugins_chkModifySend")[0].value = "1";
	}else{
		document.getElementsByName("plugins_chkModifySend")[0].value = "0";
	}
}

function doCheckYjzxSuggestion(chkObj){
	if(chkObj.checked){
		document.getElementsByName("plugins_chkYjzxSuggestion")[0].value = "1";
	}else{
		document.getElementsByName("plugins_chkYjzxSuggestion")[0].value = "0";
	}
}

function doCheckShowBackSuggestion(chkObj){
	if(chkObj.checked){
		document.getElementsByName("plugins_chkShowBackSuggestion")[0].value = "1";
	}else{
		document.getElementsByName("plugins_chkShowBackSuggestion")[0].value = "0";
	}
}

function doCheckShowBlankSuggestion(chkObj){
	if(chkObj.checked){
		document.getElementsByName("plugins_chkShowBlankSuggestion")[0].value = "1";
	}else{
		document.getElementsByName("plugins_chkShowBlankSuggestion")[0].value = "0";
	}
}

function doCheckConcurrencySet(chkObj){
	if(chkObj.checked){
		document.getElementsByName("plugins_ConcurrencySet")[0].value = "1";
		$("#chkmustFetchTaskTimeOut").attr("disabled", true);
	}else{
		document.getElementsByName("plugins_ConcurrencySet")[0].value = "0";
		if(!document.getElementById("chknotbackspace").checked
				&& !document.getElementById("chknotoverrule").checked){
			$("#chkmustFetchTaskTimeOut").attr("disabled", false);
		}
	}
}

function doCheckShowRepeal(chkObj){
	if(chkObj.checked){
		document.getElementsByName("plugins_showrepeal")[0].value = "1";
	}else{
		document.getElementsByName("plugins_showrepeal")[0].value = "0";
	}
}

function doCheckSuggestionRequired(chkObj){
	if(chkObj.checked){
		document.getElementsByName("plugins_suggestionrequired")[0].value = "1";
	}else{
		document.getElementsByName("plugins_suggestionrequired")[0].value = "0";
	}
}

function doCheckNotOverRule(chkObj){
	if(chkObj.checked){
		document.getElementsByName("plugins_notoverrule")[0].value = "1";
		$("#chkmustFetchTaskTimeOut").attr("disabled", true);
	}else{
		document.getElementsByName("plugins_notoverrule")[0].value = "0";
		if(!document.getElementById("chknotbackspace").checked
				&& !document.getElementById("ConcurrencySet").checked){
			$("#chkmustFetchTaskTimeOut").attr("disabled", false);
		}
	}
}

function doCheckNotBackspace(chkObj){
	if(chkObj.checked){
		document.getElementsByName("plugins_notbackspace")[0].value = "1";
		$("#chkmustFetchTaskTimeOut").attr("disabled", true);
	}else{
		document.getElementsByName("plugins_notbackspace")[0].value = "0";
		if(!document.getElementById("chknotoverrule").checked
				&& !document.getElementById("ConcurrencySet").checked){
			$("#chkmustFetchTaskTimeOut").attr("disabled", false);
		}
	}
}

/* yanjian 去除该设计省办公厅 意见征询的的处理代码 2012-06-19 20:19
function doCheckYjzx(chkObj){
	if(chkObj.checked){
		document.getElementsByName("plugins_yjzx")[0].value = "1";
		document.getElementById("plugins_yjzxname").style.display = "block";
	}else{
		document.getElementsByName("plugins_yjzx")[0].value = "0";
		document.getElementById("plugins_yjzxname").style.display = "none";
	}
}
*/
function doCheckLcqx(chkObj){
	if(chkObj.checked){
		document.getElementsByName("plugins_lcqx")[0].value = "1";
	}else{
		document.getElementsByName("plugins_lcqx")[0].value = "0";
	}
}

function doCheckMustFetchTask(chkObj){
	if(chkObj.checked){
		document.getElementsByName("plugins_mustFetchTask")[0].value = "1";
	}else{
		document.getElementsByName("plugins_mustFetchTask")[0].value = "0";
	}
}

function doCheckMustFetchTaskTimeOut(chkObj){
	if(chkObj.checked){
		document.getElementsByName("plugins_mustFetchTaskTimeOut")[0].value = "1";
		$("#chknotoverrule").attr("disabled", true);
		$("#chknotbackspace").attr("disabled", true);
		$("#ConcurrencySet").attr("disabled", true);
	}else{
		document.getElementsByName("plugins_mustFetchTaskTimeOut")[0].value = "0";
		$("#chknotoverrule").attr("disabled", false);
		$("#chknotbackspace").attr("disabled", false);
		$("#ConcurrencySet").attr("disabled", false);
	}
}

//不允许输入非阿拉伯数字 yanjian 2011-09-15 15:27
function checkNum(objTR){
     		var txtval=objTR.value; 
          var key = event.keyCode;
          if(key < 48||key > 57){ 
           	event.keyCode = 0;
          }
     	}
     	
     	
     	
var all1Select = false;
var all2Select = false;
var all3Select = false;
var all4Select = false;
function selectAll1() {
	var wordPrivil = document.getElementsByName("wordPrivil");
	all1Select = !all1Select;
	//保证部分已屏蔽功能【全选】时不被选中
	for (var i = 0; i < 6; i++) {
		if(i == 3 || i == 5){
			wordPrivil[i].checked = all1Select;
		}else{
			wordPrivil[i].checked = false;
		}
	}
}
function selectAll2() {
	var wordPrivil = document.getElementsByName("wordPrivil");
	all2Select = !all2Select;
	for (var i = 6; i < 15; i++) {
		wordPrivil[i].checked = all2Select;
	}
}
function selectAll3() {
	var wordPrivil = document.getElementsByName("wordPrivil");
	all3Select = !all3Select;
	for (var i = 15; i < wordPrivil.length; i++) {
		wordPrivil[i].checked = all3Select;
	}
}
function selectAll4() {
	var wordPrivil = document.getElementsByName("chkPermitUploadPDF");
	document.getElementsByName("plugins_permitUploadPDF")[0].value = "1";
	all4Select = !all4Select;
	for (var i = 0; i < wordPrivil.length; i++) {
		wordPrivil[i].checked = all4Select;

	}
	var wordPrivil2 = document.getElementsByName("chkPermitUploadSMJ");
	document.getElementsByName("plugins_permitUploadSMJ")[0].value = "1";
	for (var j = 0; j < wordPrivil2.length; j++) {
		wordPrivil2[j].checked = all4Select;
	}
}

</script>