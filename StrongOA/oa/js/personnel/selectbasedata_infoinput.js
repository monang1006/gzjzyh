var deptbudget_basedata_popUpWin=0;
var deptbudget_basedata_width = 360;
var deptbudget_basedata_height = 380;
var deptbudget_basedata_left = 100;
var deptbudget_basedata_top = 100;

var deptbudget_isOpenSelectWindow = '0';

function selectBaseData(objId,objName,dictValue){
	deptbudget_isOpenSelectWindow='1';
	var orgId=document.getElementById("orgId").value;
	if(document.getElementById("ORG_ID")!=undefined&&objId=="STRUC_ID"){
		if(document.getElementById("ORG_ID").value!=""&&document.getElementById("ORG_ID").value!=null)
			orgId=document.getElementById("ORG_ID").value;
	}
    var URLStr = contextPath+"/personnel/baseperson/person!infoItemTree.action?orgId="+orgId+"&objId="+objId+"&objName="+objName+"&dictCode="+dictValue;
	var sty="dialogWidth:"+deptbudget_basedata_width+"px;dialogHeight:"+deptbudget_basedata_height+"px;status:no";
    if(deptbudget_basedata_popUpWin)
	{
    	if(!deptbudget_basedata_popUpWin.closed) deptbudget_basedata_popUpWin.close();
	}
	deptbudget_basedata_popUpWin = showModalDialog(URLStr, window, sty);

}

function selectSalaryBaseArea(objId,objName,dictValue,flag){
	deptbudget_isOpenSelectWindow='1';
    var URLStr = contextPath+"/ipproot/personal/common/tree/action/InfoItemTreeAction.do?objId="+objId+"&objName="+objName+"&dictValue="+dictValue+"&flag="+flag;
	 var sty="dialogWidth:"+deptbudget_basedata_width+"px;dialogHeight:"+deptbudget_basedata_height+"px;status:no";
    if(deptbudget_basedata_popUpWin)
	{
              if(!deptbudget_basedata_popUpWin.closed) deptbudget_basedata_popUpWin.close();
	}
	deptbudget_basedata_popUpWin = showModalDialog(URLStr, window, sty);
}
function selectJTLXBaseArea(objId,objName,dictValue,value){
	deptbudget_isOpenSelectWindow='1';
    var URLStr = contextPath+"/ipproot/personal/common/tree/action/InfoItemTreeAction.do?objId="+objId+"&objName="+objName+"&dictValue="+dictValue+"&flagvalue="+value;
	 var sty="dialogWidth:"+deptbudget_basedata_width+"px;dialogHeight:"+deptbudget_basedata_height+"px;status:no";
    if(deptbudget_basedata_popUpWin)
	{
              if(!deptbudget_basedata_popUpWin.closed) deptbudget_basedata_popUpWin.close();
	}
	deptbudget_basedata_popUpWin = showModalDialog(URLStr, window, sty);
}
function selectBaseArea(objId,objName){
	deptbudget_isOpenSelectWindow='1';
    var URLStr = contextPath+"/ipproot/personal/common/tree/action/InfoAreaTreeAction.do?objId="+objId+"&objName="+objName;
	 var sty="dialogWidth:"+deptbudget_basedata_width+"px;dialogHeight:"+deptbudget_basedata_height+"px;status:no";
    if(deptbudget_basedata_popUpWin)
	{
              if(!deptbudget_basedata_popUpWin.closed) deptbudget_basedata_popUpWin.close();
	}
	deptbudget_basedata_popUpWin = showModalDialog(URLStr, window, sty);
}
function selectBaseCom(objId,objName){
	deptbudget_isOpenSelectWindow='1';
    var URLStr = contextPath+"/personnel/baseperson/person!infoItemTree.action?objId="+objId+"&objName="+objName;
	 var sty="dialogWidth:"+deptbudget_basedata_width+"px;dialogHeight:"+deptbudget_basedata_height+"px;status:no";
    if(deptbudget_basedata_popUpWin)
	{
              if(!deptbudget_basedata_popUpWin.closed) deptbudget_basedata_popUpWin.close();
	}
	deptbudget_basedata_popUpWin = showModalDialog(URLStr, window, sty);
}
// append by Yuangh 2007/06/13
function selectBaseData_Budget_CheckBox(objId,objName,tableName){
	deptbudget_isOpenSelectWindow='1';
	var baseType="";
	if(tableName.indexOf("T_BASE_GOVER")!=-1){
		//baseType=getBaseTypeByTableName_Other(tableName);
		baseType="1016";
	}else if(tableName.indexOf("T_BASE_")!=-1){
		baseType=getBaseTypeByTableName(tableName);
	}
	else if(tableName.indexOf("V_BASE_BUDGET_SECTIONS")!=-1){
		baseType="3";//????
	}
	else if(tableName.indexOf("V_BASE_ENTERPRISES")!=-1){
		baseType="2";//????
	}
	else if(tableName.indexOf("V_BASE_BUDGET_PURPOSES")!=-1){
		baseType="5";//????
	}
//alert(baseType);
selectBaseDataCheckBox(objId,objName,baseType);
	


}


function getBaseTypeByTableName(tableName){
	var baseType="";
	if(tableName=="T_BASE_BRANCHS"){
		baseType="1";
	}else if(tableName=="T_BASE_ENTERPRISES"){
		baseType="2";
	}else if(tableName=="T_BASE_BUDGET_SECTIONS"){
		baseType="3";
	}else if(tableName=="T_BASE_BUDGET_ITEMS"){
		baseType="4";
	}else if(tableName=="T_BASE_BUDGET_PURPOSES"){
		baseType="5";
	}else if(tableName=="T_BASE_BUDGET_PURPOSES"){
		baseType="5";
	}
	return baseType;

}

//??????????????????????????
function getBaseTypeByTableName_Other(tableName){
	var baseType="";
	if(tableName=="T_GOVSTOCK_CONTENTS"){
		baseType="1";
	}else if(tableName=="T_BASE_ENTERPRISES"){
		baseType="2";
	}
	return baseType;

}