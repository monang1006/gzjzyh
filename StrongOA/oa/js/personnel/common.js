/**
 * 弹出窗口
 * @author 彭小青
 * @date 2009年9月25日9:08:22
 * @return {}
 */

function OpenWindow(Url,Width,Height,WindowObj)
{
	var ReturnStr=showModalDialog(Url,WindowObj,'dialogWidth:'+Width+'pt;dialogHeight:'+Height+'pt;status:no;help:no;scroll:no;status:0;help:0;scroll:0;');
	return ReturnStr;
}
function WinPop(url, width, height)
{
  window.showModelessDialog(url,"",'dialogWidth=' + width + 'px; dialogHeight=' + height + 'px; resizable=no; help=no; scroll=no; status=no;resizable=0; help=0; scroll=0; status=0;');
}
function OpenThenSetValue(Url,Width,Height,WindowObj,SetObj)
{
	var ReturnStr=showModalDialog(Url,WindowObj,'dialogWidth:'+Width+'pt;dialogHeight:'+Height+'pt;status:no;help:no;scroll:no;status:0;help:0;scroll:0;');
	if (ReturnStr!='') SetObj.value=ReturnStr;
	return ReturnStr;
}
function OpeneditorWindow(Url,WindowName,Width,Height)
{
	window.open(Url,WindowName,'toolbar=0,location=0,maximize=1,directories=0,status=1,menubar=0,scrollbars=0,resizable=1,top=50,left=50,width='+Width+',height='+Height);
}

