
/**
* author:lanlc
* description:office控件常用功能按钮方法调用
* modifyer:
* Create Date: 2008-12-30
* description:
*/
var TANGER_OCX_Username="匿名用户";

//初始化WORD控件
function initWordOCX() {
   var TANGER_OCX_OBJ = document.getElementById("TANGER_OCX_OBJ");
   TANGER_OCX_OBJ.CreateNew("Word.Document");                
   //禁用新建功能
   TANGER_OCX_OBJ.FileNew=false;
   //禁用保存功能
   TANGER_OCX_OBJ.FileSave=false;
   //禁用打印功能
   TANGER_OCX_OBJ.FilePrint=false;
   //禁用关闭功能
   TANGER_OCX_OBJ.FileClose=false;
   //禁用另存为功能
   TANGER_OCX_OBJ.FileSaveAs=false;
}

//关闭office控件界面
function closeDoc() {
  window.returnValue = "NO";
  window.close();
}

//0：新建对象	1：打开		2：保存		3：另存为
//4：打印		5：打印设置	6：文件属性
function TANGER_OCX_ShowDialog(value) {
	try {
		var TANGER_OCX_OBJ = document.getElementById("TANGER_OCX_OBJ");
		TANGER_OCX_OBJ.ShowDialog(value);
	}
	catch (err) {
	}
	finally {
	}
}

 //文档打印   前台:true还是后台:false打印
function TANGER_OCX_PrintDoc(boolvalue) {
	try {
		var TANGER_OCX_OBJ = document.getElementById("TANGER_OCX_OBJ");
		TANGER_OCX_OBJ.FilePrint = true;
		TANGER_OCX_OBJ.printout(boolvalue);
	}
	catch (err) {
	}
	finally {
	}
}

//设置用户名
function TANGER_OCX_SetDocUser(cuser) {
	var TANGER_OCX_OBJ = document.getElementById("TANGER_OCX_OBJ");
	with (TANGER_OCX_OBJ.ActiveDocument.Application) {
		UserName = cuser;
		TANGER_OCX_Username = cuser;
	}
}

//进入或退出痕迹保留状态，调用下面的两个函数
function TANGER_OCX_SetMarkModify(boolvalue) {
	try{
		TANGER_OCX_SetReviewMode(boolvalue);
		TANGER_OCX_EnableReviewBar(!boolvalue);
	}catch(e){
	}
}

 //允许或禁止显示修订工具栏和工具菜单（保护修订）
function TANGER_OCX_EnableReviewBar(boolvalue) {
	var TANGER_OCX_OBJ = document.getElementById("TANGER_OCX_OBJ");
	TANGER_OCX_OBJ.ActiveDocument.CommandBars("Reviewing").Enabled = boolvalue;
	TANGER_OCX_OBJ.ActiveDocument.CommandBars("Track Changes").Enabled = boolvalue;
	TANGER_OCX_OBJ.IsShowToolMenu = boolvalue;	//关闭或打开工具菜单
}
	
//打开或者关闭修订模式
function TANGER_OCX_SetReviewMode(boolvalue) {
	var TANGER_OCX_OBJ = document.getElementById("TANGER_OCX_OBJ");
	TANGER_OCX_OBJ.ActiveDocument.TrackRevisions = boolvalue;
}

//显示/不显示痕迹
function TANGER_OCX_ShowRevisions(boolvalue) {
	var TANGER_OCX_OBJ = document.getElementById("TANGER_OCX_OBJ");
	TANGER_OCX_OBJ.ActiveDocument.ShowRevisions = boolvalue;
}

//从本地增加图片到文档指定位置
//是否浮动文件,true为浮动，false为非浮动
function TANGER_OCX_AddPicFromLocal(value){
	var TANGER_OCX_OBJ = document.getElementById("TANGER_OCX_OBJ");
    TANGER_OCX_OBJ.AddPicFromLocal(
	"", //路径
	true,//是否提示选择文件
	value,//是否浮动图片
	100,//如果是浮动图片，相对于左边的Left 单位磅
	100); //如果是浮动图片，相对于当前段落Top	
}

//模态窗口打开页面
function OpenWindow(Url, Width, Height, WindowObj) {
	var ReturnStr = showModalDialog(Url, WindowObj, "dialogWidth:" + Width + "pt;dialogHeight:" + Height + "pt;"+"status:no;help:no;scroll:no;");
	return ReturnStr;
}

//全屏手写签名
function TANGER_OCX_DoHandSign2(){
  var TANGER_OCX_OBJ = document.getElementById("TANGER_OCX_OBJ");
  TANGER_OCX_OBJ.DoHandSign2 (
	TANGER_OCX_Username,//当前登陆用户 必须
	"", //SignKey
	0,//left//可选参数
	0,//top
	0,//relative=0，表示按照屏幕位置批注
	100 //缩放100%，表示原大小
);   
}

//全屏手工绘图，与以上区别，不添加验证信息
function TANGER_OCX_DoHandDraw2(){
  var TANGER_OCX_OBJ = document.getElementById("TANGER_OCX_OBJ");
  TANGER_OCX_OBJ.DoHandDraw2(
	0,//left//可选参数
	0,//top
	0,//relative=0，表示按照屏幕位置批注
	100 //缩放100%，表示原大小
);   
}

//签名验证信息
function TANGER_OCX_DoCheckSign(){
	var TANGER_OCX_OBJ = document.getElementById("TANGER_OCX_OBJ");
	//返回值，验证结果字符串
	var retStr = TANGER_OCX_OBJ.DoCheckSign
	(
	false,""
	);
}

//加盖本地电子印章
function TANGER_OCX_AddSignFromLocal(){
	var TANGER_OCX_OBJ = document.getElementById("TANGER_OCX_OBJ");
	try{
		TANGER_OCX_OBJ.AddSignFromLocal(
									TANGER_OCX_Username,//当前登陆用户
									"",//缺省文件
									true,//提示选择
									0,//left
									0, //top
									"",
									1,
									100,
									1
		)
	}catch(e){alert("您已经3次输入错误口令，操作失败！");}
	
}