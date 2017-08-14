<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib uri="/tags/web-flex" prefix="webflex"%>
<%@ taglib uri="/tags/web-psw" prefix="webpsw"%>
<%@include file="/common/include/rootPath.jsp"%>
<%@ page import="com.strongit.bo.ListTest" %>
<%@ page import="java.util.*"%>
<%@ page import="java.text.SimpleDateFormat"%>
<html>
<head>
<title>可调整宽度表格</title>
<link rel="stylesheet" type="text/css" href="<%=frameroot%>/css/strongitmenu.css">
<LINK href="<%=path%>/common/frame/css/properties_windows.css"
			type=text/css rel=stylesheet>
<script language='javascript' src='<%=path%>/common/js/grid/ChangeWidthTable.js'></script>
<SCRIPT language="javascript" src="<%=path%>/common/js/menu/menu.js"></SCRIPT>
<SCRIPT language="javascript" src="<%=path%>/common/js/checkpswstrong/checkpswstrong.js"></SCRIPT>
<SCRIPT>
	function clickline(id){
		alert("行值："+id);
	}
	function clickcol5(value){
		alert("该列值："+value);
	}
	function showName(name,value,test){
	if(value=="否")
		return name+":<font color='red'>"+value+"</font>"+test;
	else
		return name+":"+value+test;
	}
</SCRIPT>
</head>  
<body>
<%
	List testList = new ArrayList();
	for(int i=1;i<=10;i++){
		Object[] test = new Object[6];
		test[0]=i;
		test[1]="大连("+i+")";
		test[2]="￥"+(i*7);
		test[3]=new Date();
		test[4]=i*50;
		test[5]="女";
		testList.add(test);
	}
	request.setAttribute("testList",testList);
%>
一个调整表格宽度的代码
<form action=""> 
<TABLE width="100%">
	<TR><td><webpsw:pswstrong name="psw"/></td></TR>
</TABLE>
</form>
	<webflex:flexTable name="myTable" wholeCss="divAllcss" property="ListTest" isCanDrag="true" isCanFixUpCol="true" 
	clickColor="#A9B2CA" footShow="showCheck" getValueType="getValueByArray" collection="${testList}" ondblclick="clickline(this.value)">
		<webflex:flexCheckBoxCol caption="选择" valuepos="0" valueshowpos="1" width="10" isCheckAll="true" isCanDrag="false" isCanSort="false"></webflex:flexCheckBoxCol>
		<webflex:flexTextCol caption="数组一" valuepos="1" valueshowpos="1" width="100" isCanDrag="true" isCanSort="true"></webflex:flexTextCol>
		<webflex:flexTextCol caption="数组二" valuepos="2" valueshowpos="javascript:showName(1,2,中国)" width="100" isCanDrag="true" isCanSort="true"></webflex:flexTextCol>
		<webflex:flexDateCol caption="数组三" valuepos="3" valueshowpos="3" width="100" isCanDrag="true" isCanSort="true"></webflex:flexDateCol>
		<webflex:flexTextCol caption="数组四" valuepos="4" valueshowpos="4" width="100" isCanDrag="true" isCanSort="true"></webflex:flexTextCol>
		<webflex:flexTextCol caption="数组五" valuepos="5" valueshowpos="5" width="100" isCanDrag="true" isCanSort="true" onclick="clickcol5(this.value)"></webflex:flexTextCol>
	</webflex:flexTable>
<script language="javascript">
var sMenu = new Menu();
function initMenuT(){
	sMenu.registerToDoc(sMenu);
	var item = null;
	item = new MenuItem("<%=request.getContextPath()%>/common/images/tb-add.gif","增加","gotoAdd",1,"ChangeWidthTable","checkMoreDis");
	sMenu.addItem(item);
	item = new MenuItem("<%=request.getContextPath()%>/common/images/tb-change.gif","编辑","gotoAdd",1,"ChangeWidthTable","checkOneDis");
	sMenu.addItem(item);
	item = new MenuItem("<%=request.getContextPath()%>/common/images/tb-delete3.gif","删除","gotoAdd",1,"ChangeWidthTable","checkOneDis");
	sMenu.addItem(item);
	//sMenu.addLine();
	//item = new MenuItem("<%=request.getContextPath()%>/common/images/tb-change.gif","冻结列","frezeColum",1,"ChangeWidthTable","checkOneDis");
	//sMenu.addItem(item);
	sMenu.addShowType("ChangeWidthTable");
    registerMenu(sMenu);
}
function gotoAdd(){
	alert(getValue());
}
function clicknode(obj){
	alert(obj.value);
}
</script>
</body>
</html>

