/**该脚本提供了将查询数据导出为excel格式的文件的公共方法
 * @copyright digitalChina 2002
 * @author cheys
 * @date 2004-08-31
**/
//cheys add jxh420 2004-09-01 将查询结果导出为excel

/**接口，得到服务名
*/
var GCOLSCAPTION = new Array();
var GCOLSTAGNAME  = new Array();
var GCOLSDATATYPE = new Array();

var GWORKID = 0;	//工作区ID
var GITEMPATH = "ROOT/ITEM"; //返回结果到ITEM标签的全路径（含ITEM）
var GQUERYROWSPERPAGE =  2000; //查询时每页的记录数。
var GSAVEROWSPERFILE =  40000; //保存时每个文件的记录数。
var GQUERYTIMESPERFILE  =  0; //对于每个文件，需查询的次数。
var GPATH ="c:/ctais";
var GFILENAME =  new Array();
var GSSNAME ="";
var URL_EXCELEXPORT = "/ctais/work/public/js/toexport.xml";
var URL_SHOWEXCELFILENAME ="/ctais/work/public/htm/savefile.htm";
var GSERVICENAME = new Array();
var GSPARAMS = new Array();
var GXSLDSO =  new Array();

var GERROR = false;
var GDCERROR = false;
var GFILENAMESAVE = new Array();
function interface_toexcel_setServiceName(serivceName ){	
	GSERVICENAME = serivceName;
}
function interface_toexcel_setRowsPerFile(iRows){
	GSAVEROWSPERFILE = iRows;
}
/**接口，得到各服务名需要的参数
*/
function interface_toexcel_setParamaters(parameters){	
	GSPARAMS = parameters;
}
/**接口，得到查询时每页的记录数。
*/
function interface_toexcel_setQueryRowsPerPage(iRows){
	GQUERYROWSPERPAGE = iRows;
}

/**接口，得到列名及列的标签内容
**/
function interface_toexcel_setColsTagName(tagName){		
	GCOLSTAGNAME = tagName;
}
/**接口，得到列标题
*/
function interface_toexcel_setColsCaption(caption){	
	GCOLSCAPTION = caption;
}
/**接口，得到列的数据类型
*/
function interface_toexcel_setColsDataType(dataType ){
	GCOLSDATATYPE = dataType;
}
/**接口，得到行循环的标签层次
*/
function interface_toexcel_setTreePath(sPath){
	GITEMPATH = sPath;
}


/**接口，得到保存的几个参数。
*/
function interface_toexcel_getSaveParam(){
	showMessage("请实现interface_toexcel_getSaveParam()");
}
/**返回工作薄的名字。
*/
function interface_toexcel_setSheetName(sName){
	GSSNAME = sName;
}
/**得到主文件名及扩展文件名
*/
function interface_toexcel_setFileName(aFileName){
	GFILENAME = aFileName;
}

/**得到工作区ID
*/
function interface_toexcel_setWorkId(workId){
	GWORKID = workId;
}

/**检查三个接口方法是否正确
*/
function fn_checkInterface(){
	var iCaptionLen = GCOLSCAPTION.length;
	var iTagNameLen = GCOLSTAGNAME.length;
	var iDataTypeLen = GCOLSDATATYPE.length;
	if (iCaptionLen !=iTagNameLen  )
	{
		showMessage("列引用标签名个数"+ iTagNameLen +"与列标题名个数"+iCaptionLen+"不相等。\n请检查相关的接口函数的返回值是否正确。");
		return false;
	}
	if (iCaptionLen != iDataTypeLen)
	{
		showMessage("列单元格类型个数"+ iDataTypeLen +"与列标题名个数"+iCaptionLen+"不相等。\n请检查相关的接口函数的返回值是否正确。");
		return false;
	}
	if (iTagNameLen != iDataTypeLen)
	{
		showMessage("列引用标签名个数"+ iTagNameLen +"列单元格类型个数"+iDataTypeLen+"不相等。\n请检查相关的接口函数的返回值是否正确。");
		return false;
	}
	return true;
}
/**拼caption行
**/
function fn_getCaptionRow(){
	var sCol="<Row>";
	for (var i=0;i<GCOLSCAPTION.length ;i++ )
	{
		sCol =sCol+ "\n<Cell ss:StyleID=\"s29\"><Data ss:Type=\"String\">" + GCOLSCAPTION[i] + "</Data></Cell>";
	}
	sCol = sCol + "\n</Row>\n";
	return sCol;
}

/**拼列引用行
*/
function fn_getColsTagNameRow(){
	var sCol = '<xsl:for-each select="'+GITEMPATH+'">\n<Row>';
	var style="";
	for (var i=0;i<GCOLSTAGNAME.length ;i++ )
	{
		if (GCOLSDATATYPE[i] =="String")
		{
			style ="s21" ;
		}else {
			style ="s22" ;
		}
		sCol = sCol + "\n<Cell ss:StyleID=\""+ style +"\"><Data ss:Type=\""+GCOLSDATATYPE[i]+"\"><xsl:value-of select=\""+GCOLSTAGNAME[i]+"\"/></Data></Cell>"
	}
	return sCol + "\n</Row>\n</xsl:for-each>\n";
}
/**拼column
*/
function fn_getColumns(){
	var sCol = "";
	var style="";
	for (var i=0;i<GCOLSDATATYPE.length ;i++ )
	{
		if (GCOLSDATATYPE[i] == "Number")
		{
			style = "s24";
		}else{
			style = "s23";
		}
		sCol = sCol + '<Column ss:StyleID="'+style+'" ss:Width="110"/>\n';
	}
	return sCol;
}
/**得到Xsl文件的头部信息
*/
function fn_getXslHead(){
	var sXsl = '<?xml version="1.0" encoding="GBK"?>\n'
					+' <xsl:stylesheet xmlns:xsl="http://www.w3.org/TR/WD-xsl">\n'
					+' <xsl:template match="/">\n'
					+' <xsl:script language="javascript">\n'
					 +' function setSheetName(){\n'
					  +' return this.selectsingleNode("//SSNAME").text;\n'
					 +' }\n'
					 +' function setExpandedRowCount(){\n'
					  +' return this.selectsingleNode("//DW_ATTRIBUTE/TOTALROWCOUNT").text;\n'
					+' }\n'
					+' </xsl:script>\n'
					+' <Workbook xmlns="urn:schemas-microsoft-com:office:spreadsheet"\n'
					 +' xmlns:o="urn:schemas-microsoft-com:office:office"\n'
					 +' xmlns:x="urn:schemas-microsoft-com:office:excel"\n'
					 +' xmlns:ss="urn:schemas-microsoft-com:office:spreadsheet"\n'
					 +' xmlns:html="http://www.w3.org/TR/REC-html40">\n'
					 +' <Worksheet>\n'
					  +' <xsl:attribute name="ss:Name">\n'
						+' <xsl:eval>setSheetName()</xsl:eval>\n'
					  +' </xsl:attribute>\n'
					 +' <Table ss:ExpandedColumnCount="'+GCOLSTAGNAME.length+'" x:FullColumns="1"\n'
					   +' x:FullRows="1" ss:DefaultColumnWidth="54" ss:DefaultRowHeight="14.25">\n'
					   +' <xsl:attribute name="ss:ExpandedRowCount">\n'
					   +' <xsl:eval>setExpandedRowCount()</xsl:eval>\n'
					  +' </xsl:attribute>\n'	;
		return sXsl;
}
/**得到Xsl文件的尾部信息。
*/
function fn_getXslBoot(){
	var sXsl = '</Table>\n'
				  +'</Worksheet>\n'
				+'</Workbook>\n'
				+'</xsl:template>\n'
				+'</xsl:stylesheet>';
	return sXsl;
}

/**
*生成样式表
*/
function fn_createXslDso(){
	if (!fn_checkInterface())
	{
		return false;
	}
	var sXsl = fn_getXslHead()
		+fn_getColumns()
		+fn_getCaptionRow()
		+fn_getColsTagNameRow()
		+fn_getXslBoot();
	GXSLDSO = loadXml(sXsl);
	return true;
}

/**检查各接口方法是否被正确调用

*/
function checkInterfaceFunction(){
	return true;
}
/**发第一次查询请求
*/
function interface_executeExport(){
	if (!checkInterfaceFunction())
	{
		return;
	}
	if (!confirm("请确认是否导出查询结果为EXCEL文件？\n按[确定]按钮开始导出，按[取消]按钮则取消导出操作。\n\n由于导出的数据量可能会比较大，请等待几分钟..."))
	{
		return;
	}
	if (!fn_createXslDso())
	{
		showMessage("生成样式表出错");
		return ;
	}

	if (GSERVICENAME[0]=="")
	{
		showMessage("请设置服务名！");
		return ;
	}
	var service = new Service(GSERVICENAME[0],GWORKID);
    var sArgu = service.doService(GSPARAMS[0]);
    var rtnCode = service.getRtnCode();
    var code = service.getCode();
    var msg = service.getMessage();

    if (rtnCode != "0")
    {
        showMessage(msg);

        return ;
    }

    if (code != "2000")
    {
        showMessage(msg);

        return ;
    }
	var sXmlAll = getItemString(sArgu);
	
	//生成样式表
	var oDoc = loadXml(sArgu);	

	var sItemParent = GITEMPATH.substr(0,GITEMPATH.lastIndexOf("/"));

	var oNode = oDoc.selectSingleNode(sItemParent);

	var sXml="";
	var iTotalRec = parseInt(oNode.selectSingleNode("DW_ATTRIBUTE/TOTALROWCOUNT").text,10);
	var iPageCount = parseInt(oNode.selectSingleNode("DW_ATTRIBUTE/PAGECOUNT").text,10);
	var iPageIndex = parseInt(oNode.selectSingleNode("DW_ATTRIBUTE/PAGEINDEX").text,10);
	
	///由于excel每一个sheet的最大行数是65536行，另外，如果数据太大，transformNode会报错。
	//所以计划每个文件的记录数在50000左右，如果还有更多的记录。则存成另一个文件。文件名在原名的基础上加"_01"..."_10"
	fn_getSaveParam();
	var iQueryTimes = GQUERYTIMESPERFILE;
	//每页2500条。每20页存成一个文件！
	var iPage = round((iPageCount /iQueryTimes +0.5),0);
//	alert(iPage)
	var rtnStr = ""; 
	var iRow =0;
	var oXsl = GXSLDSO;
	
	for (var j=0;j<iPage ;j++ )
	{	
		for (var i=1;i<=iQueryTimes ; i++)
		{
			if (i==1 && j==0)
			{
				continue;
			}
			if (iPageCount == 1)
			{
				break;
			}
			sXml = reQueryExp(i+j*iQueryTimes);
			
			if (!GERROR)
			{
				sXmlAll = sXmlAll + getItemString(sXml);
				if ((i+j*iQueryTimes) == iPageCount)
				{
					break;
				}
			}else{
				showMessage("导出EXCEL出错！");
				return;
			}
		}
		
		var iRowCount = iQueryTimes * GQUERYROWSPERPAGE;
		if (j==(iPage-1))
		{
			iRow = iTotalRec % iRowCount
		}
		else{
			iRow = iRowCount;
		}
		

		var xDocAll= "<ROOT>\n<DW_ATTRIBUTE>\n<TOTALROWCOUNT>"
						+(iRow+1)+"</TOTALROWCOUNT>\n</DW_ATTRIBUTE>\n<SSNAME>"
						+GSSNAME+"</SSNAME>\n"
						+sXmlAll+"\n</ROOT>";
		var oDocAll = loadXml(xDocAll);
		var sXmlExcel = oDocAll.transformNode(GXSLDSO);
		sXmlAll = "";
		var isSome = false;
		if (iPage >1)
		{
			isSome = true;
		}else{
			isSome = false;
		}
		var sRtn  = exp_one_yjkm(sXmlExcel,j,isSome);
		if (GDCERROR)
		{
			showMessage("文件导出失败！");
			return ;
		}	
		GFILENAMESAVE[j] = sRtn;
	}
	return true;
}
/**返回保存的文件名数组
*/
function interface_toexcel_getSaveFileName(){
	return GFILENAMESAVE;
}
/**导出时重复查询
*@param pageIndex 页数
*/
function reQueryExp(pageIndex){	
	if (GSERVICENAME[1]=="")
	{
		showMessage("请设置服务名！");
		GERROR = true;
		return;
	}
	var sParam ="";
	if (GSPARAMS[1] == "")
	{
		sParam = '<PAGEINDEX>'+pageIndex+'</PAGEINDEX>';
	}else{
		sParam = GSPARAMS[1]+'<PAGEINDEX>'+pageIndex+'</PAGEINDEX>';
	}
	//此处需处理参数问题 
	
	var service = new Service(GSERVICENAME[1],GWORKID);
    var sArgu = service.doService(sParam);
    var rtnCode = service.getRtnCode();
    var code = service.getCode();
    var msg = service.getMessage();

    if (rtnCode != "0")
    {
        showMessage(msg);
		GERROR = true;
        return "";
    }

    if (code != "2000")
    {
        showMessage(msg);
		GERROR = true;
        return "";
    }
	GERROR=false;
	return sArgu;
}

function fn_getSaveParam(){
	if (GSAVEROWSPERFILE > 50000 )
	{
		showMessage("每个保存的文件行数不能大于50000条！");
		return false;
	}
	GQUERYTIMESPERFILE = parseInt(GSAVEROWSPERFILE/GQUERYROWSPERPAGE,10);
	return true;
}


/**保存一个文件
*@param sXml 内容
*@param iPage 页号
*@param isSome 是否是多个文件
*/
function exp_one_yjkm(sXml,iPage,isSome){
	try{		
		var sXmlAll = "<?xml version=\"1.0\" encoding=\"GBK\"?>\n<?mso-application progid=\"Excel.Sheet\"?>\n";
		var fm="";
		var sXh="";
		var fullname="";
	//	var allFileName="由于导出的记录数太多，所以分"+aXml.length+"个文件保存:\n";
		
		var oDoc = loadXml(sXml);
		var oDocAll = loadFile(URL_EXCELEXPORT);
		oDocAll.selectSingleNode("//Workbook").appendChild(oDoc.selectSingleNode("//Worksheet"));	
		var aFileName = GFILENAME;
		if (isSome)
		{
			if (iPage+1<10)
			{
				sXh ="0"+(iPage+1);
			}
			else{
				sXh = iPage+1;
			}
			 fm = aFileName[0]+"_"+sXh+aFileName[1];
		}
		else{
			fm = aFileName[0]+aFileName[1];
		}
		fullname = HTMLToExcel(sXmlAll+oDocAll.documentElement.xml,fm);	
		GDCERROR = false;
		return fullname;
	}catch(e)
	{
		GDCERROR = true;
		return "";
	}	
	
}


/**转HTML成Word文档
*@param sHTML 待保存的内容
*@param dm 文件名
*/
function HTMLToExcel(sHTML,dm){
	
	try{
	var sPath=GPATH+"/"+dm;
	var xml = sHTML;	
	//初始化文件操作控件
	var oFile = new ActiveXObject("Scripting.FileSystemObject");
	var oTs = oFile.createTextFile(sPath);
	oTs.writeLine(xml);
	oTs.close();
	oTs = null;
	oFile = null;
	}catch(e){
		showMessage("创建文件出错：\n"+e.message);
		return "";
	}
	//showMessage("导出的文件保存在"+sPath);
	return dm;
}


/**得到字符串
*@param str 字串
*/
function getItemString(str){
	var iLen = str.indexOf("<ITEM>");
	return str.substring(iLen,str.length-7);
}


/**显示成功信息
*@path aPath 文件保存路径
*/
function interface_toexcel_showFileSavePath(aPath){
	var oRtnValue = window.showModalDialog(URL_SHOWEXCELFILENAME,aPath , "dialogHeight:300px;dialogWidth:600px;status:no;help:no;scroll:no");
	if (oRtnValue  == null)
	{		
		return;
	}
}

/**文件何存的路径
*/
function getSavePath(){
	return GPATH;
}