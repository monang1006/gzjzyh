<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@	include file="/common/include/rootPath.jsp"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<html>
	<head>
		<title>查看文件</title>
		<%@include file="/common/include/meta.jsp"%>
		<LINK type=text/css rel=stylesheet href="<%=frameroot%>/css/properties_windows.css">
		<script type="text/javascript" language="javascript" src="<%=jsroot%>/jquery/jquery-1.2.6.js"></script>
		<script type="text/javascript" src="<%=jsroot%>/validate/jquery.validate.js"></script>
		<script type="text/javascript" src="<%=jsroot%>/validate/formvalidate.js"></script>
		<script type="text/javascript" src="<%=jsroot%>/common/common.js"></script>
		<script type="text/javascript" language="javascript" src="<%=jsroot%>/upload/jquery.MultiFile.js"></script>
		<script type="text/javascript" language="javascript" src="<%=jsroot%>/upload/jquery.blockUI.js"></script>
		
		 <style type="text/css">
        .tableRed
        {
            border-collapse: collapse;
            border: 0px;
            width: 88%;
            border-top: 2px solid #ff0000;
        }
        .tableRed td
        {
            border-bottom: 2px solid #ff0000;
            font-family: 宋体, Arial;
            font-size: 12px;
            vertical-align: top;
            padding: 8px 12px;
        }
        .tableRed td.tdLeftRed
        {
            border-right: 2px solid #ff0000;
            width: 50%;
            padding: 0px;
        }
        .tableRed td.tdRightRed
        {
            border-left: 2px solid #ff0000;
            width: 50%;
            padding: 0px;
        }
        .tableRed td td
        {
            border: 0px;
            padding: 0px;
        }
        .divInfo_Red
        {
            border-bottom: 2px solid #ff0000;
            padding: 8px 12px;
        }
        .divInfo_Red_bottom
        {
            padding: 8px 12px;
        }
        .trTitle
        {
            color: Red;
            font-size: 14px;
            font-family: 宋体, Arial;
            font-weight: bold;
        }
        .trSpan
        {
            color: Red;
            font-size: 14px;
            font-family: 宋体, Arial;
        }
        .tdSpan
        {
            font-size: 12px;
            font-family: 宋体, Arial;
        }
        input
        {
            border-top: 0px;
            border-left: 0px;
            border-right: 0px;
            line-height: 22px;
            font-size: 12px;
            overflow: hidden;
        }
        textarea
        {
            border-top: 0px;
            border-left: 0px;
            border-right: 0px;
            line-height: 22px;
            font-size: 12px;
            overflow: visible;
        }
        .divRight
        {
            text-align: right;
        }
    </style>
	</head>
	<body class=contentbodymargin oncontextmenu="return false;">
	  
    <form method="post" action="" id="form1" datasaved="" ActionMode="view" DataChanged="false">
    <p align="center">
        <font face="华文中宋" size="7" color="red">
            <label id="lblOrgName" readOnly="True" canChange="false" class="readonly">赣州市国土资源局发文稿纸</label>
        </font>
    </p>
    <table align="center" class="tableRed" style="width: 98%;table-layout: fixed;
            word-wrap: break-word;">
        <tr>
            <td class="tdLeftRed">
            <table  cellpadding="0px" cellspacing="0px" style=" border:0px none; width:100%"  >
            <tr>
                <td id="DZQF" style="width:50%;display:none;" readOnly="True" canChange="false" class="readonly">
                <div id="divSUB" class="divInfo_Red" style="height:80px;">
                    <span class="trSpan"><font color="red">书记签发：</font></span><br />
                    <textarea name="SUBISSUED_OPTION" id="SUBISSUED_OPTION" oldValue="" MaxLength="500" class="left readonly" ondblclick="if(this.readOnly){return false;}GetPhrase_1(0)" title="双击鼠标可进行选择输入！" DefaultValue="" style="width: 100%; height: 112px; overflow: visible;" ElementLabel="党组书记意见" ElementName="SUBISSUED_OPTION" canChange="false" readOnly="True"></textarea>
                    <div class="divRight">
                        <input name="SUBISSUED_Userid" type="hidden" id="SUBISSUED_Userid" oldValue="" MaxLength="20" class="readonly" DefaultValue="" FieldType="NUMBER" ElementLabel="书记id" ElementName="SUBISSUED_Userid" value="" canChange="false" readOnly="True" />
                        <span class="tdSpan">签名:</span>
                        <input name="SUBISSUED_PEOPLE" type="text" id="SUBISSUED_PEOPLE" oldValue="" MaxLength="10" class="left readonly" DefaultValue="" style="width: 80px; border: none" ElementLabel="党组书记签名" ElementName="SUBISSUED_PEOPLE" value="" canChange="false" readOnly="True" /><span id="spanSUBISSUED"></span>
                        <span class="tdSpan">日期:</span>
                        <input name="SUBISSUED_TIME" type="text" id="SUBISSUED_TIME" oldValue="" ondblclick="calendar()" class="left readonly" DefaultValue="" FieldType="DATE" style="width: 65px;
                            border: none" ElementLabel="党组书记签名日期" ElementName="SUBISSUED_TIME" value="" canChange="false" readOnly="True" />
                    </div>
                </div>
                </td>

                <td id="JZQF" style="width:100%;" readOnly="True" canChange="false" class="readonly"> 
            <div class="divInfo_Red" style="height: 80px">
                    <span class="trSpan"><font color="red"><span id="spanQf">局长签发：</span></font></span><br />
                    <textarea name="Leader_Issued" id="LEADER_ISSUED" oldValue="发。" MaxLength="500" class="left readonly" ondblclick="if(this.readOnly){return false;}GetPhrase_1(0)" title="双击鼠标可进行选择输入！" DefaultValue="" style="width: 100%; height: 82px; overflow: visible;" ElementLabel="意见" ElementName="Leader_Issued" canChange="false" readOnly="True">发。</textarea>
                    <div class="divRight">
                        <span class="tdSpan">签名:</span>
                        <input name="Issued_People" type="text" id="ISSUED_PEOPLE" oldValue="罗志欣" MaxLength="10" class="left readonly" DefaultValue="" style="width:80px;border:none;" ElementLabel="签发人" ElementName="Issued_People" value="罗志欣" canChange="false" readOnly="True" />
                        <span class="tdSpan">日期:</span>
                        <input name="Issued_Time" type="text" id="ISSUED_TIME" oldValue="2017-07-10" ondblclick="calendar()" class="left readonly" DefaultValue="" FieldType="DATE" style="width: 65px;
                            border: none" ElementLabel="签发时间" ElementName="Issued_Time" value="2017-07-10" canChange="false" readOnly="True" />
                    </div>
                </div>
                </td>

                </tr>
                </table>
               
                
                <div class="divInfo_Red_bottom">
                    <span class="trSpan"><font color="red"><span id="spanSht" readOnly="True" canChange="false" class="readonly">分管领导审签：</span></font></span>
                    <div id="tdCss" style="width: 100%;">
                        <table id="IssuedList" align="center" border="0" cellspacing="0" cellpadding="0" style="width: 100%;" tbname="IssuedList" pkValues="54097701" MaxRowNum="1">
	<tr rowstate="TableHead" style="">
		<td class="tdHide" colspan="2" style="text-align: left;">
                                    <input name="IssuedAddList" type="button" id="IssuedAddList" value="+" onclick="TableAddRow();" style="height: 15px; display: none;" />
                                </td>
	</tr>
	<tr style="display:;" RowNum="0">
		<td style="width: 100%; text-align: left">
                                    <textarea name="IssuedOption" id="IssuedOption" DefaultValue="" MaxLength="500" oldValue="拟同意，呈邓局长示。" readOnly="True" ondblclick="if(this.readOnly){return false;}GetPhrase_1(0)" class="left readonly" title="双击鼠标可进行选择输入！" style="width: 100%; overflow: visible; height: 40px;" ElementLabel="意见" ElementName="IssuedOption" canChange="false" tlwtag="Proofoption">拟同意，呈邓局长示。</textarea>
                                    <div class="divRight">
                                        <input name="IssuedDept" type="hidden" id="IssuedDept" oldValue="局领导" readOnly="True" MaxLength="30" class="left readonly" DefaultValue="" style="width: 80px; border: none;
                                            display: none" ElementLabel="会签单位" ElementName="IssuedDept" value="局领导" canChange="false" tlwtag="Proofdept" />
                                        <span class="tdSpan">签名：</span>
                                        <input name="IssuedPerson" type="text" id="IssuedPerson" oldValue="陈炜" readOnly="True" MaxLength="10" class="left readonly" DefaultValue="" style="width: 80px; border: none" ElementLabel="会签人" ElementName="IssuedPerson" value="陈炜" canChange="false" tlwtag="Proofperson" />
                                        <span id="spanIssuedPerson" value=""></span><span class="tdSpan">日期：</span>
                                        <input name="IssuedTime" type="text" id="IssuedTime" DefaultValue="" oldValue="2017-07-10" readOnly="True" class="left readonly" onclick="calendar()" title="双击鼠标可进行选择输入！" FieldType="DATE" style="width: 80px; border: none" ElementLabel="会签时间" ElementName="IssuedTime" value="2017-07-10" canChange="false" tlwtag="Prooftime" /></div>
                                    <input name="IssuedId" type="hidden" id="IssuedId" oldValue="219020701" class="readonly" MaxLength="12" tlwtag="Proofobjectid" DefaultValue="" FieldType="NUMBER" ElementLabel="业务序号" ElementName="IssuedId" value="219020701" canChange="false" readOnly="True" />
                                    <input name="HANDLENUM" type="hidden" id="HANDLENUM" oldValue="54097701" class="readonly" MaxLength="12" tlwtag="Proofhandlenum" DefaultValue="" FieldType="NUMBER" ElementLabel="处理序号" ElementName="HANDLENUM" value="54097701" canChange="false" readOnly="True" />
                                    <input name="IssuedAspect" type="hidden" id="IssuedAspect" oldValue="分管领导审核(签发)" class="readonly" MaxLength="20" tlwtag="Proofaspect" DefaultValue="" ElementLabel="会签环节" ElementName="IssuedAspect" value="分管领导审核(签发)" canChange="false" readOnly="True" />
                                    <input name="IssuedorgName" type="hidden" id="IssuedorgName" oldValue="赣州市局" class="readonly" MaxLength="100" tlwtag="Prooforgname" DefaultValue="" ElementLabel="会签人组织名称" ElementName="IssuedorgName" value="赣州市局" canChange="false" readOnly="True" />
                                    <input name="IssuedOrgid" type="hidden" id="IssuedOrgid" oldValue="7000" class="readonly" MaxLength="12" tlwtag="Prooforgid" DefaultValue="" FieldType="NUMBER" ElementLabel="会签人组织ID" ElementName="IssuedOrgid" value="7000" canChange="false" readOnly="True" />
                                    <input name="IssuedDeptID" type="hidden" id="IssuedDeptID" oldValue="700000" class="readonly" MaxLength="12" tlwtag="Proofdeptid" DefaultValue="" FieldType="NUMBER" ElementLabel="会签部门ID" ElementName="IssuedDeptID" value="700000" canChange="false" readOnly="True" />
                                    <input name="IssuedPersonID" type="hidden" id="IssuedPersonID" oldValue="100122599" class="readonly" MaxLength="12" tlwtag="Proofpersonid" DefaultValue="" FieldType="NUMBER" ElementLabel="会签人ID" ElementName="IssuedPersonID" value="100122599" canChange="false" readOnly="True" />
                                    <input name="IssuedRATIFYID" type="hidden" id="IssuedRATIFYID" oldValue="1331342701" class="readonly" MaxLength="12" tlwtag="ProofRatifyid" DefaultValue="" FieldType="NUMBER" ElementLabel="环节办理过程ID" ElementName="IssuedRATIFYID" value="1331342701" canChange="false" readOnly="True" />
                                    <input name="ISSUEDPERSONNUM" type="hidden" id="ISSUEDPERSONNUM" oldValue="" class="readonly" MaxLength="50" tlwtag="ProofpersonNum" DefaultValue="" ElementLabel="数字签名" ElementName="ISSUEDPERSONNUM" value="" canChange="false" readOnly="True" />
                                </td>
	</tr>
</table>

                    </div>
                </div>
            </td>
            <td class="tdRightRed" colspan="2">
                <div class="divInfo_Red" style="height: 80px">
                    <span class="trSpan"><font color="red">承办科室拟稿人：</font></span><br />
                    <textarea name="CREATEOPTION" id="CREATEOPTION" oldValue="来文通知，请各部门主要负责同志务必增强“四个意识”，对照存在的问题，按照习近平总书记“四个亲自”和省委“六个进一步”的要求，认真开展自查，列出问题清单，并切实加以整改，于7月10日前,由主要领导签字后加盖公章，将自查及整改落实情况报市委改革办。" MaxLength="500" class="left readonly readonly" ondblclick="if(this.readOnly){return false;}GetPhrase_1(0)" title="双击鼠标可进行选择输入！" DefaultValue="" style="width: 100%; height: 40px; overflow: visible;" ElementLabel="拟稿意见" ElementName="CREATEOPTION" canChange="false" readOnly="True">来文通知，请各部门主要负责同志务必增强“四个意识”，对照存在的问题，按照习近平总书记“四个亲自”和省委“六个进一步”的要求，认真开展自查，列出问题清单，并切实加以整改，于7月10日前,由主要领导签字后加盖公章，将自查及整改落实情况报市委改革办。</textarea>
                    <div class="divRight" style="margin-top: 5px;">
			                        部门：<input name="DEPTNAME" type="text" id="DEPTNAME" oldValue="" MaxLength="100" class="left readonly readonly" DefaultValue="" style="width: 60px; border: none" ElementLabel="拟稿部门名称" ElementName="DEPTNAME" value="" canChange="false" readOnly="True" />
			                        签名：&nbsp;<input name="USERNAME" type="text" id="USERNAME" oldValue="" MaxLength="10" class="left readonly readonly" DefaultValue="" style="width: 60px; border: none" ElementLabel="拟稿用户名称" ElementName="USERNAME" value="" canChange="false" readOnly="True" />
                        <span id="spanUSERID" readOnly="True" canChange="false" class="readonly"></span>日期：&nbsp;<input name="DRAFTDATE" type="text" id="DRAFTDATE" oldValue="2017-07-07" class="left readonly readonly" DefaultValue="" FieldType="DATE" style="width: 60px; border: none" ElementLabel="拟稿时间" ElementName="DRAFTDATE" value="2017-07-07" canChange="false" readOnly="True" />
                    </div>
                </div>
                <div id="divhg" class="divInfo_Red readonly" style="height: 95px;" readOnly="True" canChange="false">
                    <span class="trSpan"><font color="red"><span id="spanhg">承办科室负责人核稿：</span></font> </span>
                    <div id="tdCheckCss" style="width: 100%;">
                        <table id="CheckList" align="center" border="0" cellspacing="0" cellpadding="0" style="width: 100%;" tbname="CheckList" pkValues="80412701,80396701" MaxRowNum="2">
							<!-- tr>
								<td style="width: 100%; text-align: left">
                                    <textarea name="checkoption" id="checkoption" MaxLength="500" oldValue="请利用科校对后印发。" readOnly="True"  class="left readonly" title="" style="width: 100%; overflow: visible; height: 40px;" ElementLabel="意见" ElementName="checkoption" canChange="false" tlwtag="Proofoption"></textarea>
                                    <div class="divRight">
                                        <span class="tdSpan">签名：</span>
                                        <input name="checkperson" type="text" id="checkperson" oldValue="刘金莉" readOnly="True" MaxLength="10" class="left readonly" style="width: 80px; border: none" ElementLabel="核对人" ElementName="checkperson" value="刘金莉" canChange="false" tlwtag="Proofperson" />
                                        <span id="spancheckperson" value=""></span><span class="tdSpan">日期：</span>
                                        <input name="checktime" type="text" id="checktime" oldValue="2017-07-10" readOnly="True" class="left readonly" DefaultValue="" FieldType="DATE" style="width: 80px;
                                            border: none" ElementLabel="核稿时间" ElementName="checktime" value="2017-07-10" canChange="false" tlwtag="Prooftime" />
                                    </div>
                                </td>
							</tr>
							 -->
						</table>

                    </div>
                </div>
                <div id="div1" class="divInfo_Red readonly" style="height: 95px;" readOnly="True" canChange="false">
                    <span class="trSpan"><font color="red"><span id="spanhq">部门会签：</span></font> </span>
                    <div id="tdCheckCssds" style="width: 100%;">
                        <table id="CheckListds" align="center" border="0" cellspacing="0" cellpadding="0" style="width: 100%;" tbname="CheckListds" pkValues="" MaxRowNum="0">
	<tr rowstate="TableHead" style="border: 0px;">
		<td class="tdHide" colspan="2" style="text-align: left;">
                                    <input name="CheckAddListds" type="button" id="CheckAddListds" value="+" onclick="TableAddRow();" style="height: 15px; display: none;" />
                                </td>
	</tr>
	<tr style="display:none;">
		<td style="width: 100%; text-align: left">
                                    <textarea name="checkoptionds" id="checkoptionds" DefaultValue="" MaxLength="500" readOnly="True" ondblclick="if(this.readOnly){return false;}GetPhrase_1(0)" class="left readonly" title="双击鼠标可进行选择输入！" style="width: 100%; overflow: visible; height: 40px;" ElementLabel="会签意见" ElementName="checkoptionds" canChange="false" tlwtag="Proofoption"></textarea>
                                    <div class="divRight">
                                        <input name="checkdeptds" type="hidden" id="checkdeptds" readOnly="True" MaxLength="30" class="left readonly" DefaultValue="" style="width: 80px; border: none;
                                            display: none" ElementLabel="会签单位" ElementName="checkdeptds" value="" canChange="false" tlwtag="Proofdept" />
                                        <span class="tdSpan">签名：</span>
                                        <input name="checkpersonds" type="text" id="checkpersonds" readOnly="True" MaxLength="10" class="left readonly" DefaultValue="" style="width: 80px; border: none" ElementLabel="会签人" ElementName="checkpersonds" value="" canChange="false" tlwtag="Proofperson" />
                                        <span id="span1" value=""></span><span class="tdSpan">日期：</span>
                                        <input name="checktimeds" type="text" id="checktimeds" readOnly="True" class="left readonly" DefaultValue="" FieldType="DATE" style="width: 80px;
                                            border: none" ElementLabel="会签时间" ElementName="checktimeds" value="" canChange="false" tlwtag="Prooftime" />
                                    </div>
                                    <input name="objectidds" type="hidden" id="objectidds" class="readonly" MaxLength="12" tlwtag="Proofobjectid" DefaultValue="" FieldType="NUMBER" ElementLabel="业务序号" ElementName="objectidds" value="" canChange="false" readOnly="True" />
                                    <input name="checkhandlenumds" type="hidden" id="checkhandlenumds" class="readonly" MaxLength="12" tlwtag="Proofhandlenum" DefaultValue="" FieldType="NUMBER" ElementLabel="处理序号" ElementName="checkhandlenumds" value="" canChange="false" readOnly="True" />
                                    <input name="checkaspectds" type="hidden" id="checkaspectds" class="readonly" MaxLength="20" tlwtag="Proofaspect" DefaultValue="" ElementLabel="会签环节" ElementName="checkaspectds" value="" canChange="false" readOnly="True" />
                                    <input name="checkorgnameds" type="hidden" id="checkorgnameds" class="readonly" MaxLength="100" tlwtag="Prooforgname" DefaultValue="" ElementLabel="会签人组织名称" ElementName="checkorgnameds" value="" canChange="false" readOnly="True" />
                                    <input name="checkorgidds" type="hidden" id="checkorgidds" class="readonly" MaxLength="12" tlwtag="Prooforgid" DefaultValue="" FieldType="NUMBER" ElementLabel="会签人组织ID" ElementName="checkorgidds" value="" canChange="false" readOnly="True" />
                                    <input name="checkdeptidds" type="hidden" id="checkdeptidds" class="readonly" MaxLength="12" tlwtag="Proofdeptid" DefaultValue="" FieldType="NUMBER" ElementLabel="会签部门ID" ElementName="checkdeptidds" value="" canChange="false" readOnly="True" />
                                    <input name="checkpersonidds" type="hidden" id="checkpersonidds" class="readonly" MaxLength="12" tlwtag="Proofpersonid" DefaultValue="" FieldType="NUMBER" ElementLabel="会签人ID" ElementName="checkpersonidds" value="" canChange="false" readOnly="True" />
                                    <input name="Ratifyidds" type="hidden" id="Ratifyidds" class="readonly" MaxLength="14" tlwtag="ProofRatifyid" DefaultValue="" FieldType="NUMBER" ElementLabel="RATIFYID" ElementName="Ratifyidds" value="" canChange="false" readOnly="True" />
                                    <input name="CHECKPERSONNUMds" type="hidden" id="CHECKPERSONNUMds" tlwtag="ProofpersonNum" value="" />
                                </td>
	</tr>
</table>

                    </div>
                </div>
                <div id="divjh" class="divInfo_Red_bottom readonly" style="height: 100px;" readOnly="True" canChange="false">
                    <span class="trSpan"><font color="red"><span id="spanJhDiv">办公室核稿：</span></font></span>
                    <div id="DISJHHANDLE" style="width: 100%;">
                        <table id="JhList" align="center" border="0" cellspacing="0" cellpadding="0" style="width: 100%;" tbname="JhList" pkValues="68788701,68783701" MaxRowNum="2">
							<tr rowstate="TableHead" style="border: 0px;">
								<td class="tdHide" colspan="2" style="text-align: left;">
                                    <input name="JhAddList" type="button" id="JhAddList" value="+" onclick="TableAddRow();" style="height: 15px; display: none;" />
                                </td>
							</tr>
							<tr style="display:;" RowNum="0">
								<td style="width: 100%; text-align: left">
                                    <textarea name="jhoption" id="jhoption" DefaultValue="" MaxLength="500" oldValue="已核，呈陈炜同志示。" readOnly="True" ondblclick="if(this.readOnly){return false;}GetPhrase_1(0)" class="left readonly" title="双击鼠标可进行选择输入！" style="width: 100%; overflow: visible; height: 40px;" ElementLabel="意见" ElementName="jhoption" canChange="false" tlwtag="Proofoption">已核，呈陈炜同志示。</textarea>
                                    <div class="divRight">
                                        <input name="jhdept" type="hidden" id="jhdept" oldValue="办公室" readOnly="True" MaxLength="30" class="left readonly" DefaultValue="" style="width: 80px; border: none;
                                            display: none" ElementLabel="校核单位" ElementName="jhdept" value="办公室" canChange="false" tlwtag="Proofdept" />
                                        <span class="tdSpan">签名：</span>
                                        <input name="jhperson" type="text" id="jhperson" oldValue="胡皆林" readOnly="True" MaxLength="20" class="left readonly" DefaultValue="" style="width: 80px; border: none" ElementLabel="校核人" ElementName="jhperson" value="胡皆林" canChange="false" tlwtag="Proofperson" />
                                        <span id="spanjhperson" value=""></span><span class="tdSpan">日期：</span>
                                        <input name="jhtime" type="text" id="jhtime" oldValue="2017-07-07" readOnly="True" class="left readonly" DefaultValue="" FieldType="DATE" style="width: 80px; border: none" ElementLabel="校核时间" ElementName="jhtime" value="2017-07-07" canChange="false" tlwtag="Prooftime" />
                                    </div>
                                </td>
							</tr>
						</table>

                    </div>
                </div>
            </td>
        </tr>
        <tr id="trLight" style="display:none;" readOnly="True" canChange="false" class="readonly">
	<td class="left" colspan="3">
                时效状态：
                <label for = "Green" style = "_filter:progid:DXImageTransform.Microsoft.AlphaImageLoader(src='../../../../App_Themes/Oxic_Sea/images/绿灯.png', sizingMethod='scale');_background:Transparent;width:12px;height:15px; display:inline-block;"></label>
                <input value="Green" name="light" type="radio" id="Green" style="border-style:none;" onclick="EffectLight(&#39;Light&#39;);" />
                <label for = "Yellow" style = "_filter:progid:DXImageTransform.Microsoft.AlphaImageLoader(src='../../../../App_Themes/Oxic_Sea/images/黄灯.png', sizingMethod='scale');_background:Transparent;width:12px;height:15px;display:inline-block;"></label>
                <input value="Yellow" name="light" type="radio" id="Yellow" style="border-style:none;" onclick="EffectLight(&#39;Light&#39;);" />
                <label for = "Red" style = "_filter:progid:DXImageTransform.Microsoft.AlphaImageLoader(src='../../../../App_Themes/Oxic_Sea/images/红灯.png', sizingMethod='scale');_background:Transparent;width:12px;height:15px;display:inline-block;"></label>
                <input value="Red" name="light" type="radio" id="Red" style="border-style:none;" onclick="EffectLight(&#39;Light&#39;);" />
            </td>
</tr>

        <tr>
            <td class="left" colspan="3">
                <span class="trSpan">发文号：</span><input name="OutDoc_Symbol" type="text" id="OUTDOC_SYMBOL" oldValue="" MaxLength="100" class="readonly readonly" DefaultValue="" style="width:40%; " ElementLabel="发文文号" ElementName="OutDoc_Symbol" value="" canChange="false" readonly="True" /><input name="txtDocNum" type="text" id="txtDocNum" style="width: 35%; display: none;" readonly="True" class="right readonly" canChange="false" />
            </td>
        </tr>
        <tr>
            <td colspan = "3" class = "left" style = "color:#ff0000;">
                <span class="trSpan">文件类型：</span>&nbsp;
                <span>
                    <select name="File_Type" id="FILE_TYPE" onbeforeactivate="return false;" oldValue="" ondblclick="self.focus();" MaxLength="50" class="readonly readonly" DefaultValue="" onchange="File_category_collection(this);" ElementLabel="文件类型" ElementName="File_Type" canChange="false">
	<option selected="selected" value=""></option>
	<option value="公文类">公文类</option>
	<option value="业务类">业务类</option>
</select>
                </span>
                <span>
                    <select name="File_category" id="FILE_CATEGORY" onbeforeactivate="return false;" oldValue="" ondblclick="self.focus();" MaxLength="80" class="readonly readonly" DefaultValue="" ElementLabel="文件类型" ElementName="File_category" canChange="false">
	<option selected="selected" value=""></option>
	<option value="市政府文件">市政府文件</option>
	<option value="建设用地审查">建设用地审查</option>
	<option value="用地预审">用地预审</option>
	<option value="国土资源部文件">国土资源部文件</option>
	<option value="建设用地区位调整">建设用地区位调整</option>
	<option value="国土资源厅文件">国土资源厅文件</option>
	<option value="地籍变更">地籍变更</option>
	<option value="市直文件">市直文件</option>
	<option value="其他">其他</option>
	<option value="分局业务">分局业务</option>
	<option value="其他业务">其他业务</option>
</select>
                </span>
            </td>
        </tr>
        <tr>
            <td class="left">
                <span class="trSpan"><font color="red">密&nbsp;&nbsp;级：&nbsp;</font></span><select name="File_Security" id="FILE_SECURITY" onbeforeactivate="return false;" oldValue="" ondblclick="self.focus();" class="readonly readonly" ElementLabel="文件密级" MaxLength="20" DefaultValue="" onchange="File_Security_OnChange();" style="width: 20%;" ElementName="File_Security" canChange="false">
	<option selected="selected" value=""></option>
	<option value="秘密">秘密</option>
	<option value="机密">机密</option>
	<option value="绝密">绝密</option>
</select>
            </td>
            <td class="left" colspan="2">
                <div style="width: 50%; float: left;">
                    <span class="trSpan"><font color="red">是否公开：</font></span>
                    <select name="OPEN_ATTRIBUTE" id="OPEN_ATTRIBUTE" style="width: 80px;">
                        <option value=""></option>
                        <option value="是">是</option>
                        <option value="否">否</option>
                    </select></div>
                <div id="divHjcd" style="display:none; width: 50%; float: left; text-align: right;" readOnly="True" canChange="false" class="readonly">
                    <span class="trSpan"><font color="red">缓急程度：</font></span><select name="EMERGENCY" id="EMERGENCY" onbeforeactivate="return false;" oldValue="" ondblclick="self.focus();" MaxLength="20" class="readonly" DefaultValue="" style="width: 80px;" ElementLabel="缓急程度" ElementName="EMERGENCY" canChange="false">
	<option selected="selected" value=""></option>
	<option value="特急">特急</option>
	<option value="急件">急件</option>
</select></div>
                <div style="display: none;">
                    <span class="trSpan"><font color="red">办理时限：</font></span><input name="OverTime" type="text" id="OverTime" oldValue="" class="readonly readonly" onclick="calendar()" DefaultValue="" FieldType="DATE" style="width: 80px;" ElementLabel="办理时限" ElementName="OverTime" value="" canChange="false" readOnly="True" /></div>
            </td>
        </tr>
        <tr>
            <td class="left" colspan="3">
                <span id="spanBti" class="trTitle readonly" readOnly="True" canChange="false">标题：</span>
                <textarea name="txtSubject" id="TXTSUBJECT"  MaxLength="200" class="right readonly readonly" DefaultValue="" style="width: 99%; height: 40px;" ElementLabel="文件标题" ElementName="txtSubject" canChange="false" readOnly="True"></textarea>
            </td>
        </tr>
        <tr>
            <td colspan="3">
                <span class="trSpan">主送：</span>
                <input name="Main_Delivery" type="text" id="MAIN_DELIVERY" DefaultValue="" oldValue="" ondblclick="GetLocution2(this,&#39;主送&#39;)" class="right readonly readonly" title="双击鼠标可进行选择输入！" style="width: 99%; height: 30px" ElementLabel="主送" ElementName="Main_Delivery" value="" canChange="false" MaxLength="600" readOnly="True" />
            </td>
        </tr>
        
        <tr>
            <td colspan="3">
                <span class="trSpan">抄送：</span>
                <input name="Copy_Delivery" type="text" id="COPY_DELIVERY" DefaultValue="" oldValue="" ondblclick="GetLocution2(this,&#39;抄送&#39;)" class="right readonly readonly" title="双击鼠标可进行选择输入！" style="width: 99%; height: 30px" ElementLabel="抄送" ElementName="Copy_Delivery" value="" canChange="false" MaxLength="600" readOnly="True" />
            </td>
        </tr>
        <tr id="trfz" style="display: none;" readOnly="True" canChange="false" class="readonly">
	<td colspan="3">
                <span class="trSpan">发至：</span>
                <input name="SEND_TO" type="text" id="SEND_TO" DefaultValue="" oldValue="" ondblclick="GetLocution2(this,&#39;发至&#39;)" class="right readonly" title="双击鼠标可进行选择输入！" style="width: 99%; height: 30px" ElementLabel="发至" ElementName="SEND_TO" value="" canChange="false" MaxLength="600" readOnly="True" />
            </td>
</tr>

        <tr>
            <td colspan="3">
                <span id="ifrFileUpload"  class="trSpan">正文：</span><br />
                <!--  
                iframe id="ifrFileUpload" width="100%" height="150" frameborder="0" style="border:1px solid #878f97" readOnly="True" canChange="false" class="readonly" src="../../FileUpLoad/office/office.html?type=2&amp;catalog=fwzwfj&amp;moduleId=46258999&amp;flowsn=219020701&amp;eformId=8790590&amp;action=9a&amp;UserName=管理员"></iframe>
                -->
            </td>
        </tr>
        <tr>
            <td class="left" colspan="1">
                <span class="trSpan">是否增加附件： </span>
                <select name="ifaddattach" id="ifaddattach" onbeforeactivate="return false;" oldValue="否" ondblclick="self.focus();" MaxLength="30" class="readonly readonly" DefaultValue="" onchange="ifaddattach_OnChange()" ElementLabel="是否增加附件" ElementName="ifaddattach" canChange="false">
	<option value=""></option>
	<option value="是">是</option>
	<option selected="selected" value="否">否</option>
</select>
            </td>
            <td class="left" colspan="2" >
                    <span class="trSpan"><font color="red">是否关联收文：</font></span>
                    <span><select name="selReativeRec" id="selReativeRec" onbeforeactivate="return false;" oldValue="否" ondblclick="self.focus();" MaxLength="4" class="readonly readonly" DefaultValue="" onchange="selReativeRec_OnChange(&#39;recDocID&#39;,&#39;hdnReflash&#39;);" ElementLabel="是否关联收文" ElementName="selReativeRec" canChange="false">
	<option value=""></option>
	<option value="是">是</option>
	<option selected="selected" value="否">否</option>
</select></span>
                </td>
        </tr>
        <tr id="trIfAFileUpload" readOnly="True" canChange="false" class="readonly">
	<td colspan="3">
                <span class="trSpan">附件：</span><br />
                 <iframe id="ifaFileUpload" width="100%" height="150" frameborder="0" style="border:1px solid #878f97" src="../../FileUpLoad/office/office.html?type=1&amp;catalog=fwfj&amp;moduleId=46258999&amp;flowsn=219020701&amp;eformId=8790590&amp;action=9a&amp;UserName=管理员"></iframe>
            </td>
</tr>

        <tr style="display:none">
            <td colspan="3">
                <span class="trTitle">主题词：</span>
                <input name="Keywords" type="text" id="Keywords" DefaultValue="" oldValue="" value="" ondblclick="GetLocutionKey(this,&#39;主题词&#39;)" class="right readonly readonly" title="双击鼠标可进行选择输入！" style="width: 99%;" ElementLabel="主题词" ElementName="Keywords" canChange="false" MaxLength="100" readOnly="True" />
            </td>
        </tr>
        <tr>
            <td class="left">
                <span class="trSpan">
                    <span id="LabelSign" class="readonly" readOnly="True" canChange="false"></span><label id="lblOrgName2" readOnly="True" canChange="false" class="readonly">赣州市国土资源局</label>办公室</span>
                <input name="OutDoc_Date" type="text" id="OutDoc_Date" oldValue="2017-07-10" class="readonly readonly" onclick="if(this.readOnly){return false;}calendar()" DefaultValue="" FieldType="DATE" style="width: 120px; text-align: center" ElementLabel="发文日期" ElementName="OutDoc_Date" value="2017-07-10" canChange="false" readOnly="True" /><span class="trSpan">印发</span>
            </td>
            <td class="left">
                <span class="trSpan">共印</span>
                <input name="OutDoc_Shares" type="text" id="OutDoc_Shares" oldValue="4" onkeyup="value=value.replace(/[^\d]/g, &#39;&#39;) " readOnly="True" MaxLength="5" class="readonly readonly" DefaultValue="" FieldType="NUMBER" style="width: 80px; text-align: center" ElementLabel="发文份数" ElementName="OutDoc_Shares" value="4" canChange="false" /><span class="trSpan">份</span>
            </td>
            <td class="left">
                <span class="trSpan">打印：</span>
                <input name="PrintPersonId" type="hidden" id="PrintPersonId" oldValue="1007701" MaxLength="12" class="readonly readonly" DefaultValue="" FieldType="NUMBER" ElementLabel="打字人ID" ElementName="PrintPersonId" value="1007701" canChange="false" readOnly="True" />
                <input name="PrintPerson" type="text" id="PrintPerson" oldValue="蔡雪飞" MaxLength="20" class="readonly readonly" DefaultValue="" style="display:none;" ElementLabel="打字人" ElementName="PrintPerson" value="蔡雪飞" canChange="false" readOnly="True" />
                <span id="spanPrintPersonId" readOnly="True" canChange="false" class="readonly" style="background:url(&#39;/gzweb/Business_OA/OA/SignImg/1007701.jpg&#39;) no-repeat; width:61px; height:31px;display:inline;_filter:progid:DXImageTransform.Microsoft.AlphaImageLoader(src=&#39;/gzweb/Business_OA/OA/SignImg/1007701.jpg&#39;, sizingMethod=&#39;scale&#39;);_background:Transparent;"></span>
            </td>
        </tr>
    </table>
    <div style="width: 100%; height: 30px;">
    </div>
	<script type="text/javascript">
	
   $(document).ready(function(){			  
	
		//初始化所有事务
		      var schedules = ${jsonArr};
		    //  	alert("schedules:"+schedules);
		      
		var filedata = eval(schedules)[0];//业务数据
		var CHECKHANDLE = eval(schedules)[1];//处室核稿意见 CHECKHANDLE
		var DISJHHANDLE = eval(schedules)[2];//ban公室拟稿意见 DISJHHANDLE
		var ISSUED_HANDLE_DS = eval(schedules)[3];//会文意见
		var ISSUED_HANDLE = eval(schedules)[4];//分管局领导拟稿意见
		var attchdata = eval(schedules)[5];//附件
		var flowdata = eval(schedules)[6];//流程数据
		
		
		//alert("filedata.TXTSUBJECT:"+filedata.TXTSUBJECT)
		    $.each(filedata, function(key, val) {
		       if("ISSUED_PEOPLE"==key){
		       }
		       	$('#'+key).val(val);
		    });
		    
		    
			
			
			 $.each(ISSUED_HANDLE, function(i, item){ 
				       	alert("attchdata__>"+i+"=="+item);
			 	var atthtml="<a href='hrefurl'>hrefName</a>"
			      　　$.each(item,function(key,val){
			    	
				       	alert("attchdata__>"+key+"=="+val);
			    	if("attachfile"==key||"ATTACHFILE"==key){
				       	atthtml = atthtml.replace("hrefurl", val);
			    	}
			    	if("fullname"==key||"FULLNAME"==key){
				       	atthtml = atthtml.replace("hrefName", val);
			    	}
			    　});
			   $('#ifrFileUpload').append(atthtml);
			   atthtml="";
			 }); 
			 
			 
		    $.each(CHECKHANDLE, function(i, item){      
			      　　$.each(item,function(key,val){
			    	var hgHtml = "";
			    	hgHtml = "<p>"+val+"</p>";
			    	if("CHECKOPTION"==key||"CHECKPERSON"==key||"CHECKTIME"==key){
				       	$('#CheckList').append(val);
			    	}
			    　});
			   $('#CheckList').append("<br>");
			 }); 
	}); 
	      
			function viewAnnex(value){	//查看附件
	           var frame=document.getElementById("annexFrame");
	           frame.src="<%=path%>/archive/tempfile/tempFile!download.action?tfileAppedId="+value+"&tempfileId=${model.tempfileId}";
            }
            
            
		</script>
		
	</body>
</html>
