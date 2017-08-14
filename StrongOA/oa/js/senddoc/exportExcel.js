var oXL = null;

/**
 * Intranet选项——安全——自定义级别——对没有标记为安全的ActiveX控件进行初始化和脚本运行”这一项设置为“启用”就可以了
 */
function CellAreaExcel(filename) {
	
	if(!confirm("确定要将当前页内容导出为EXCEL？")){
		return;
	}
	
	if(typeof(filename)== "undefined"){
		filename = "";
		if(typeof($("#workflowName")) != "undefined"){
			filename = $("#workflowName").val();
		}
		if(filename == null){
			filename = "";
		}
	}
	filename = filename.replace(/(^\s*)|(\s*$)/g,"");
	var myDate = new Date();
	filename = filename + myDate.getFullYear()+""
				+((myDate.getMonth()+1)<10?("0"+(myDate.getMonth()+1)):(myDate.getMonth()+1))
				+""+(myDate.getDate()<10?("0"+myDate.getDate()):myDate.getDate())
				+""+(myDate.getHours()<10?("0"+myDate.getHours()):myDate.getHours())
				+""+(myDate.getMinutes()<10?("0"+myDate.getMinutes()):myDate.getMinutes())
				+""+(myDate.getSeconds()<10?("0"+myDate.getSeconds()):myDate.getSeconds()); 
	if ($("#handleKind").val() == "2") {// 收文
		recvdocExcel(filename);
	} else if ($("#handleKind").val() == "1") {// 发文
		senddocExcel(filename);
	} else if ($("#handleKind").val() == "3") {//用于发文登记编号(sendDocRegist-list.jsp中)
		sendDocRegistExcel(filename);
	} else if ($("#handleKind").val() == "0") {//专用于重新分办
		returnExcel(filename);
	} else {// 其他
		otherExcel(filename);
	}
}

//专用于重新分办
function returnExcel(filename) {
	if(oXL == null){
		oXL = initoXL();
	}
	if (oXL != null) {
		var oWB = oXL.Workbooks.Add();
		var oSheet = oWB.ActiveSheet;
		var Lenr = myTable.rows.length;
		for (i = 0; i < Lenr; i++) {
			var Lenc = myTable.rows(i).cells.length;
			for (j = 3; j < Lenc; j++) {//不显示图片列和复选列，故j = 3；表单控件存在一列预留列，故j < Lenc-1
				var cj = j - 3;
				if (i == 0) {
					oSheet.Cells(i + 1, cj + 1).HorizontalAlignment = 3;// 水平对齐
					oSheet.Cells(i + 1, cj + 1).Font.Bold = true; // 粗体
					oSheet.Cells(i + 1, cj + 1).value = myTable.rows(i)
							.cells(j).innerText;
				} else {
					oSheet.Cells(i + 1, cj + 1).HorizontalAlignment = 2;// 左对齐
					oSheet.Cells(i + 1, cj + 1).value = myTable.rows(i)
							.cells(j).title;
				}
				if (cj == 0) {// 标题
					oSheet.Cells(i + 1, cj + 1).ColumnWidth = 60;
				} else {
					oSheet.Cells(i + 1, cj + 1).ColumnWidth = 30;
					oSheet.Cells(i + 1, cj + 1).HorizontalAlignment=3;//居中
					
				}
				oSheet.Cells(i + 1, cj + 1).WrapText = true;	//设置自动换行
				oSheet.Cells(i + 1, cj + 1).Borders.Weight = 2;	//设置单元格边框
			}
		}
		oXL.Visible = true;
		saveAsFile(oSheet,filename);
	}
}

//专用于发文登记编号(sendDocRegist-list.jsp中)
function sendDocRegistExcel(filename) {
	if(oXL == null){
		oXL = initoXL();
	}
	if (oXL != null) {
		var oWB = oXL.Workbooks.Add();
		var oSheet = oWB.ActiveSheet;
		var Lenr = myTable.rows.length;
		for (i = 0; i < Lenr; i++) {
			var Lenc = myTable.rows(i).cells.length;
			for (j = 2; j < Lenc; j++) {//不显示图片列和复选列，故j = 3；表单控件存在一列预留列，故j < Lenc-1
				var cj = j - 2;
				if (i == 0) {
					oSheet.Cells(i + 1, cj + 1).HorizontalAlignment = 3;// 水平对齐
					oSheet.Cells(i + 1, cj + 1).Font.Bold = true; // 粗体
					oSheet.Cells(i + 1, cj + 1).value = myTable.rows(i)
							.cells(j).innerText;
				} else {
					oSheet.Cells(i + 1, cj + 1).HorizontalAlignment = 2;// 左对齐
					oSheet.Cells(i + 1, cj + 1).value = myTable.rows(i)
							.cells(j).title;
				}
				if (cj == 0) {// 标题
					oSheet.Cells(i + 1, cj + 1).ColumnWidth = 50;
				} else if(cj == 1){
					oSheet.Cells(i + 1, cj + 1).ColumnWidth = 10;
				} else if(cj == 2){
					oSheet.Cells(i + 1, cj + 1).ColumnWidth = 25;
				} else if(cj == 3){
					oSheet.Cells(i + 1, cj + 1).ColumnWidth = 20;
				} else if(cj == 4){
					oSheet.Cells(i + 1, cj + 1).ColumnWidth = 10;
				}else{
					oSheet.Cells(i + 1, cj + 1).ColumnWidth = 15;
				}
				oSheet.Cells(i + 1, cj + 1).WrapText = true;	//设置自动换行
				oSheet.Cells(i + 1, cj + 1).Borders.Weight = 2;	//设置单元格边框
			}
		}
		oXL.Visible = true;
		saveAsFile(oSheet,filename);
	}
}



/**
 * 其他类型列表数据导出
 */
function otherExcel(filename) {
	if(oXL == null){
		oXL = initoXL();
	}
	if (oXL != null) {
		var oWB = oXL.Workbooks.Add();
		var oSheet = oWB.ActiveSheet;
		var Lenr = myTable.rows.length;
		for (i = 0; i < Lenr; i++) {
			var Lenc = myTable.rows(i).cells.length;
			//for (j = 3; j < Lenc; j++) {//不显示图片列和复选列，故j = 3；表单控件存在一列预留列，故j < Lenc-1
			//	var cj = j - 3;
			for (j = 2; j < Lenc; j++) {//显示图片列和复选列，故j = 2；表单控件存在一列预留列，故j < Lenc-1
				var cj = j - 2;			
				if (i == 0) {
					oSheet.Cells(i + 1, cj + 1).HorizontalAlignment = 3;// 水平对齐
					oSheet.Cells(i + 1, cj + 1).Font.Bold = true; // 粗体
					oSheet.Cells(i + 1, cj + 1).value = myTable.rows(i)
							.cells(j).innerText;
				} else {
					oSheet.Cells(i + 1, cj + 1).HorizontalAlignment = 2;// 左对齐
					oSheet.Cells(i + 1, cj + 1).value = myTable.rows(i)
							.cells(j).title;
				}
				if (cj == 0) {// 标题
					oSheet.Cells(i + 1, cj + 1).ColumnWidth = 40;
				} else {
					oSheet.Cells(i + 1, cj + 1).ColumnWidth = 20;
				}
				oSheet.Cells(i + 1, cj + 1).WrapText = true;	//设置自动换行
				oSheet.Cells(i + 1, cj + 1).Borders.Weight = 2;	//设置单元格边框
			}
		}
		oXL.Visible = true;
		saveAsFile(oSheet,filename);
	}
}
/**
 * 发文类型列表数据导出
 */
function senddocExcel(filename) {
	if(oXL == null){
		oXL = initoXL();
	}
	if (oXL != null) {
		var oWB = oXL.Workbooks.Add();
		var oSheet = oWB.ActiveSheet;
		var Lenr = myTable.rows.length;
		for (i = 0; i < Lenr; i++) {
			var Lenc = myTable.rows(i).cells.length;
			for (j = 3; j < Lenc; j++) {//不显示图片列和复选列，故j = 3；表单控件存在一列预留列，故j < Lenc-1
				var cj = j - 3;
				if (i == 0) {
					oSheet.Cells(i + 1, cj + 1).HorizontalAlignment = 3;// 水平对齐
					oSheet.Cells(i + 1, cj + 1).Font.Bold = true; // 粗体
					oSheet.Cells(i + 1, cj + 1).value = myTable.rows(i)
							.cells(j).innerText;
				} else {
					oSheet.Cells(i + 1, cj + 1).HorizontalAlignment = 2;// 左对齐
					oSheet.Cells(i + 1, cj + 1).value = myTable.rows(i)
							.cells(j).title;
				}
				if (cj == 0) {// 标题
					oSheet.Cells(i + 1, cj + 1).ColumnWidth = 60;
				} else {
					oSheet.Cells(i + 1, cj + 1).ColumnWidth = 30;
					oSheet.Cells(i + 1, cj + 1).HorizontalAlignment=3;//居中
				}
				oSheet.Cells(i + 1, cj + 1).WrapText = true;	//设置自动换行
				oSheet.Cells(i + 1, cj + 1).Borders.Weight = 2;	//设置单元格边框
			}
		}
		oXL.Visible = true;
		saveAsFile(oSheet,filename);
	}
}

/**
 * 收文类型列表数据导出
 */
function recvdocExcel(filename) {
	if(oXL == null){
		oXL = initoXL();
	}
	if (oXL != null) {
		var oWB = oXL.Workbooks.Add();
		var oSheet = oWB.ActiveSheet;
		var Lenr = myTable.rows.length;
		for (i = 0; i < Lenr; i++) {
			var Lenc = myTable.rows(i).cells.length;
			for (j = 3; j < Lenc; j++) {//不显示图片列和复选列，故j = 3；表单控件存在一列预留列，故j < Lenc-1
				var cj = j - 3;
				if (i == 0) {
					oSheet.Cells(i + 1, cj + 1).HorizontalAlignment = 3;// 水平对齐
					oSheet.Cells(i + 1, cj + 1).Font.Bold = true; // 粗体
					oSheet.Cells(i + 1, cj + 1).value = myTable.rows(i)
							.cells(j).innerText;
				} else {
					oSheet.Cells(i + 1, cj + 1).HorizontalAlignment = 3;// 居中
					oSheet.Cells(i + 1, cj + 1).value = myTable.rows(i)
							.cells(j).title;
				}
				if (cj == 0) {// 标题
					oSheet.Cells(i + 1, cj + 1).ColumnWidth = 60;
					oSheet.Cells(i + 1, cj + 1).HorizontalAlignment = 2;// 居左
				} else {
					oSheet.Cells(i + 1, cj + 1).ColumnWidth = 30;
					oSheet.Cells(i + 1, cj + 1).HorizontalAlignment=3;//居中
				}
				oSheet.Cells(i + 1, cj + 1).WrapText = true;	//设置自动换行
				oSheet.Cells(i + 1, cj + 1).Borders.Weight = 2;	//设置单元格边框
			}
		}
		oXL.Visible = true;
		saveAsFile(oSheet,filename);
	}
}
/**
 * 保存文件
 */
function saveAsFile(oSheet, filename) {
	try {
		oSheet.SaveAs("\\" + filename + ".XLS");	//保存文件
		//oSheet.Application.Quit();	//关闭excel程序
	} catch (err) {
	}
}

/**
 * 初始化oXL
 */
function initoXL() {
	try {
		return new ActiveXObject("Excel.Application");
	} catch (err) {
		alert("目前无法进行此操作！\r\n" +
				"请先确定您的电脑上已经安装了Excel程序\r\n" 
				+ "如果没安装Excel程序请先安装；\r\n" 
				+"如果您已经安装了Excel程序，需要做一下设置：\r\n" 
				+ "1、选中IE菜单项【工具】并单击左键；\r\n"
				+ "2、选中Internet选项并单击左键；\r\n" + "3、选中【安全】选项卡；\r\n"
				+ "4、选中可信任站点，并点击按钮【站点】，将本站点添加为可信任站点，点击确定；\r\n"
				+ "5、点击按钮【自定义级别】，找到【ActiveX控件和插件】所属类别下的选项，\r\n"
				+ "并将为【对没有标记为安全的ActiveX控件进行初始化和脚本运行】这一项设置为【启用】；" +
				"经过上述设置之后仍无法正常使用，请联系相关人员！");
		return null;
	} finally {

	}
}
