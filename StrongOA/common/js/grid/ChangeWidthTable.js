//记录鼠标状态, 记录鼠标按下, 记录当前列
var curState, curDown, curCol,curRow;
var oldPlace, newPlace;
var sortBlankText = "&nbsp;&nbsp;&nbsp;";
var lastclick = -1;	
var reverse = false;
var sortUpText = "↑";
var sortDownText = "↓";
var hasCheck = "false";
var orColor,slColor="#B0CBEF";
var namecol = 0;
var clickColor = "#a9b2ca";
var idcols = new Array;
var tableStatus = null;
var selRow = -1;
var footnum=0;
var selRows=new Array();
function setFootNum(num){
	footnum=num;
}
function setHasCheck(check){
	hasCheck=check;
}
function setTableStatus(status){
	tableStatus=status;
}
function setOrColor(color){
	orColor=color;
}
function setNamecol(col){
	namecol=col;
}
function setClickColor(clickcolor){
    clickColor=clickcolor;
}
function getTable(item) {
 var obj = null;
 do {
  if (item.tagName=="TABLE") {
   obj = item;
   break;
  }
  item = item.parentElement;
 }
 while (item != null); 
 return obj;
}

function calculateOffset(item, offsetName) {
 var totalOffset = 0;
 do {
  totalOffset += eval('item.offset' + offsetName);
  item = item.parentElement;
 }
 while (item != null);
 return totalOffset;
}

function moveCol(objCol,obj) {
  var curTable = getTable(objCol);
 window.status = curTable.parentElement.scrollLeft;//window.document.body.scrollLeft;
 if (!curDown) {
  //curCol = objCol;
  //鼠标没有按下 
  curCol = objCol;
  //document.getElementById("myTable_td").innerHTML="window.event.x:"+window.event.x+"<br>objCol.offsetLeft:"+objCol.offsetLeft+"<br>curTable.parentElement.scrollLeft:"+curTable.parentElement.scrollLeft+"<br>curTable.scrollLeft:"+curTable.scrollLeft+"<br>calculateOffset(objCol, 'Left'):"+calculateOffset(objCol, "Left")+"<br>objCol.offsetWidth:"+objCol.offsetWidth+"<br><br>前面："+(window.event.x + curTable.parentElement.scrollLeft)+"<br>后面:"+(objCol.offsetLeft + objCol.offsetWidth-3);
   if (objCol.tagName=="TH"&&window.event.x + curTable.parentElement.scrollLeft >= objCol.offsetLeft + objCol.offsetWidth-3) {
   //移动到了相应的部位/改变鼠标
   curState=true;
   curTable.style.cursor = "col-resize";//"w-resize";
  }
  else {
   curState = false;
   curTable.style.cursor = "default";
  }
 	//document.getElementById("myTable_td").innerHTML=document.getElementById("myTable_td").innerHTML+"<br><br>ccurState："+curState+"<br>obj.style.cursor:"+obj.style.cursor;
  
  //document.getElementById("myTable_td").innerHTML=document.getElementById("myTable_td").innerHTML+"<br><br>curTable.offsetLeft："+curTable.offsetLeft;
  // document.getElementById("myTable_td").innerHTML=document.getElementById("myTable_td").innerHTML+"<br>curTable.offsetWidth："+curTable.offsetWidth; 
  // document.getElementById("myTable_td").innerHTML=document.getElementById("myTable_td").innerHTML+"<br>myTable_div.scrollLeft："+curTable.parentElement.scrollLeft;  
 }
} 
 
function  upBody(obj) {
 //鼠标抬起/一切恢复原状态  
 if (curState) {
   var curTable = getTable(curCol);
  //---------------------------调整表格--------------------------
  //调整条件:(层左侧+线左侧=线绝对左侧坐标)>目标的左侧坐标+20
  newPlace = window.event.x + curTable.parentElement.scrollLeft;//window.document.body.scrollLeft;
  var curcolwidth=curCol.offsetWidth;
  if (myDiv.offsetLeft + myLine.offsetLeft > calculateOffset(curCol, "Left") + 20) {
   if (curCol.offsetWidth > oldPlace - newPlace)
    curCol.width =curcolwidth - (oldPlace - newPlace);
   if (curTable!=null){
   if(newPlace>oldPlace&&curTable.rows[0].cells[curTable.rows[0].cells.length-1].offsetWidth<=5){
    	curTable.width = curTable.offsetWidth-(oldPlace - newPlace);
   }else if(newPlace<oldPlace){
   		if(curTable.offsetWidth-(oldPlace - newPlace)>=window.document.body.scrollWidth-20)
   			curTable.width = curTable.offsetWidth-(oldPlace - newPlace);
   		else 
   			curTable.width = curTable.parentElement.offsetWidth;
   	}
   }
   //curCol.innerText = curCol.width;
  }
  //-------------------------------------------------------------
  curState = false;
  curDown = false;
  myDiv.style.display = "none";
  obj.style.cursor = "default";
 }
}
 
function downBody(obj) {
 //鼠标按下
 if (curState) {
  curDown = true;
  var curTable = getTable(window.event.srcElement);
  if (curTable!=null) {
   //---------定位竖线----------
   myDiv.style.display = ""; //层可见
   myLine.style.height = curTable.offsetHeight;
   myLine.style.width = 1;
   myDiv.style.left = window.event.x + curTable.parentElement.scrollLeft - myLine.offsetLeft;
   myDiv.style.top = calculateOffset(curTable, "Top") - myLine.offsetTop;
   //---------------------------
   oldPlace = window.event.x + window.document.body.scrollLeft;
  }
 }
} 
 
function moveBody(obj) {
 //鼠标移动
 if (curDown) {
  //鼠标按下状态
  var curTable = getTable(window.event.srcElement);
  myDiv.style.left = window.event.x + curTable.parentElement.scrollLeft- myLine.offsetLeft;
  obj.style.cursor = "col-resize";//"w-resize";
 }
}

function selectBody() {
 //鼠标选择文本[不支持动态调整？]
 if (curDown) //鼠标按下于调整状态
  window.event.returnValue = false;
}
function getHead(objTable){
	for(var i=0;i<objTable.rows;i++){
		if(objTable.rows(i).tagName=="TH"){
			return objTable.rows(i);
		}
	}
	return objTable.rows(0);
}
function getHeadLine(objTable,obj){
	for(var i=0;i<objTable.rows;i++){
		if(objTable.rows(i)==obj){
			return i+1;
		}
	}
	return 1;
}
function sort(objCol,num,type,dateformat) {
	
	return;//屏蔽点击列表表头排序功能 modify by luosy 2012-11-22
	
 var txt = objCol.innerHTML;
 var sortAsc = false;
 if (txt.charAt(txt.length - 1)==sortDownText)
  sortAsc = true;
 var objTable = getTable(objCol);
 for (var i = 0; i < getHead(objTable).cells.length; i++) { 
  txt = getHead(objTable).cells(i).innerHTML;
  getHead(objTable).cells(i).selectIndex=i;
  if ((txt.charAt(txt.length - 1)==sortDownText) || (txt.charAt(txt.length - 1)==sortUpText))
   getHead(objTable).cells(i).innerHTML = txt.substring(0, txt.length - 1);
 }
 objCol.innerHTML += (sortAsc?sortUpText:sortDownText)
 sortTable(objCol, sortAsc,num,type,dateformat);
}
function getSortFieldType(clickObject){
	var spanFT = getSpanByKey(clickObject,"FieldType");
	if(spanFT==null)
		return "string";
	return spanFT.innerHTML;
}
//根据Span的ID值得到Object中的对应span
function getSpanByKey(clickObject,key){
	var clds = clickObject.children;
	for(var i = 0;i<clds.length;i++){
		if(clds[i].id == key){
			return clds[i];
		}
	}
	return null;
}

function sortTable(objCol, sortAsc,num,type,dateformat) { 
 if (objCol.tagName == "TH") {
 var clickObject = event.srcElement;
 while (clickObject.tagName != "TH") 
	{
		clickObject = clickObject.parentElement;
	}
  var objTable = getTable(objCol);
  var t = objTable.tBodies(0);
  var i,j,k;
  var iRowInsertRow, iRowWalkRow, current, insert, textRowInsert;
  var iColumn = clickObject.selectIndex;
  var fldtype = getSortFieldType(clickObject);
  var boltmp,rowtmp1,rowtmp2,rowtmp3, tmp,tmp1,tmp2,tmp3,tmp4,tmp5;
  for (iRowInsert = 0+1; iRowInsert < t.rows.length; iRowInsert++){
  	 if (iColumn) {	
  	 	if( typeof(t.children[iRowInsert].children[iColumn]) != "undefined")
     		      textRowInsert = t.children[iRowInsert].children[iColumn].innerText;
		else
			textRowInsert = "";
        } else {
           textRowInsert = t.children[iRowInsert].innerText;
        }
        
        if(textRowInsert==""){
        	textRowInsert = t.children[iRowInsert].children[iColumn].value;
        }
   	for (iRowWalk = 0; iRowWalk <= iRowInsert ; iRowWalk++) {
   		if (iColumn) {
			if(typeof(t.children[iRowWalk].children[iColumn]) != "undefined")
				textRowCurrent = t.children[iRowWalk].children[iColumn].innerText;
			else
				textRowCurrent = "";
            } else {
			textRowCurrent = t.children[iRowWalk].innerText;
            }
            
         if(textRowCurrent==""){
        	textRowCurrent = t.children[iRowWalk].children[iColumn].value;
        }
            current = textRowCurrent;
		insert  = textRowInsert;
		//如果类型为number数值型， 那么需要去掉中间的，
		//如果类型为percent百分比， 那么需要去掉%，
		if(fldtype=="number"){
			current = removeDouHao(current);
			insert = removeDouHao(insert);
		}else if(fldtype=="percent"){
			current = removePercent(current);
			insert = removePercent(insert);
			//alert(insert+"[");
		}
		
		if ( !isNaN(current) && !isNaN(insert)) //fix a bug by VN 2002.6.18
		{
			current= eval(current);
			insert= eval(insert);
		}
		else
		{
			if(current==undefined){current="";}
			if(insert==undefined){insert="";}
			current	= current.toLowerCase();
			insert	= insert.toLowerCase();
		}
    	if (((!sortAsc && insert>current) || (sortAsc && insert<current))&&(iRowInsert != iRowWalk)){
    		
      		eRowInsert = t.children[iRowInsert];
      		eRowWalk = t.children[iRowWalk];
            t.insertBefore(eRowInsert, eRowWalk);
            iRowWalk = iRowInsert;
     }
   }
  }
 }
}

function ts_sort_date(a,b,dateformat) {
    // y2k notes: two digit years less than 50 are treated as 20XX, greater than 50 are treated as 19XX
    var yearstr=dateformat.indexOf("y");
    var yearend=dateformat.lastIndexOf("y");
    var monthstr=dateformat.indexOf("M");
    var monthend=dateformat.lastIndexOf("M");
    var daystr=dateformat.indexOf("d");
    var dayend=dateformat.lastIndexOf("d");
    
    var Hourstr=dateformat.indexOf("H");
    var Hourend=dateformat.lastIndexOf("H");
    var Minusstr=dateformat.indexOf("m");
    var Minusend=dateformat.lastIndexOf("M");
    var Secondstr=dateformat.indexOf("s");
    var Secondend=dateformat.lastIndexOf("s");
    aa = a.innerText;
    bb = b.innerText;
    if(aa.length!=dateformat.length||bb.length!=dateformat.length)
    	return 0;
    var dt1="",dt2="";
    if(yearstr!=-1){
    	dt1=dt1+aa.substr(yearstr,yearend);
    	dt2=dt2+bb.substr(yearstr,yearend);
    }
    if(monthstr!=-1){
    	dt1=dt1+aa.substr(monthstr,monthend);
    	dt2=dt2+bb.substr(monthstr,monthend);
    }
    if(daystr!=-1){
    	dt1=dt1+aa.substr(daystr,dayend);
    	dt2=dt2+bb.substr(daystr,dayend);
    }
    if(Hourstr!=-1){
    	dt1=dt1+aa.substr(Hourstr,Hourend);
    	dt2=dt2+bb.substr(Hourstr,Hourend);
    }
    if(Minusstr!=-1){
    	dt1=dt1+aa.substr(Minusstr,Minusend);
    	dt2=dt2+bb.substr(Minusstr,Minusend);
    }
    if(Secondstr!=-1){
    	dt1=dt1+aa.substr(Secondstr,Secondend);
    	dt2=dt2+bb.substr(Secondstr,Secondend);
    }
    if (dt1==dt2) return 0;
    if (dt1<dt2) return -1;
    return 1;
}

function ts_sort_currency(a,b) { 
	var aa = a.innerText.replace(",","");
	aa = aa.replace("%","");
    aa = aa.replace(/[^0-9.]/g,'');
    
    var bb = b.innerText.replace(",","");
	bb = bb.replace("%","");
    bb = b.innerText.replace(/[^0-9.]/g,'');
    
    return parseFloat(aa) - parseFloat(bb);
}

function ts_sort_numeric(a,b) { 
	var aa = a.innerText.replace(",","");
    aa = parseFloat(aa);
    if (isNaN(aa)) aa = 0;
    var bb = b.innerText.replace(",","");
    bb = parseFloat(bb); 
    if (isNaN(bb)) bb = 0;
    return aa-bb;
}

function ts_sort_caseinsensitive(a,b) {
    aa = a.innerText.toLowerCase();
    bb = b.innerText.toLowerCase();
    if (aa==bb) return 0;
    if (aa<bb) return -1;
    return 1;
}

function ts_sort_default(a,b) {
    var aa = a.innerText.toLowerCase();
    var bb = b.innerText.toLowerCase();
    //var len=aa.length>bb.length?bb.length:aa.length;
    //for(var i=0;i<len;i++){
    //	var achar = aa.charCodeAt(i);
    //	var bchar = bb.charCodeAt(i);
    //	if(achar<bchar) return -1;
    //	if(achar>bchar) return 1;   	
    //}
    if (aa==bb) return 0;
    if (aa<bb) return -1;
    return 1;
    //return 0;
}
function showDetail(){
	var obj = event.srcElement;
	obj.title = obj.innerValue;
}
function setTableBorder(objTable) {
 var i,j;
 for (i = 0; i < objTable.rows.length; i++)
  for (j = 0; j < objTable.rows(i).cells.length; j++) {
  	var obj = objTable.rows(i).cells(j);
 	var showsize = objTable.rows(0).cells(j).showsize;
 	if(showsize==undefined||showsize==0||showsize==""||showsize=="null")
 		showsize=10;
  	var objhtml = obj.innerHTML;
  	obj.nowrap="nowrap";
  	if(objhtml.indexOf("<INPUT")!=-1||objhtml.indexOf("<SCRIPT")!=-1||objhtml.indexOf("<IMG")!=-1) ;
    else if (objhtml == "")
    	obj.innerHTML = "&nbsp;";
    else if(objhtml.length > showsize){
   		obj.innerValue = objhtml;
		//obj.innerHTML = objhtml.substring(0,showsize)+"***";  	
   		obj.innerHTML = subStr(objhtml, showsize*2);	
   		obj.onmouseover = showDetail;
   }
   //objTable.rows(i).cells(j).className = (i==0?"biao_bg2":"td1");
  }
}

/** @author 张磊 @createdate 2012-03-23 @description 处理字符串过长
var subStr=function(str,length){
var a=str.match(/[^\x00-\xff]|[A-Z]|[\%\@\#\$]|[,!.?<>\/\'\"\{\}\[\]\:\;\(\)\&\^\*\~\s]{1,2}|[a-z0-9]{1,2}/g);
	return a.length<length?str:a.slice(0,length).join("")+"..";
}*/

function subStr(str, len){
	if( ! str || ! len){
			return '';
	}
	var a = 0;// 预期计数：中文2字节，英文1字节
	var i = 0;// 循环计数
	var temp = '';// 临时字串
	for (i = 0; i < str.length; i ++ ){
		if (str.charCodeAt(i) > 255){
			a += 2;// 按照预期计数增加2
		}
		else{
			a ++ ;
		}
		if((a-(i*ssxs))> len){// 如果增加计数后长度大于限定长度，就直接返回临时字符串
			return temp+"..";
		}
		temp += str.charAt(i);// 将当前内容加到临时字符串
	}
	return str;	// 如果全部是单字节字符，就直接返回源字符串
}

function TableKeyDown(color,e){
	keycode1=event.keyCode
	if(keycode1==38){  //键盘上键
		if(curRow!=undefined&&curRow.previousSibling!=undefined&&curRow.previousSibling.tagName=="TD"){
			curRow.style.cssText+=";background:white";
			curRow.previousSibling.style.cssText+=";background:"+color;
			curRow=curRow.previousSibling;
		}
	}
	else if(keycode1==40){  //键盘下键
		if(curRow!=undefined&&curRow.nextSibling!=undefined&&curRow.nextSibling.tagName=="TD"){
			curRow.style.cssText+=";background:white";
			curRow.nextSibling.style.cssText+=";background:"+color;
			curRow=curRow.nextSibling;
		}
	}
}
var select_rows = new Array();
function TableMouseDown(color,e){
    var tb=this,tr,ee
    ee=e==null?event.srcElement:e
    if(ee.tagName!="TD")
        return false;
    tr=ee.parentNode
    //if(tb.selRow!=null)
    //    setTrReveal(tb.selRow,"background:white",1)
//    if(tr.cells[0].style.cssText.toLowerCase().indexOf("#a9b2ca")>-1){
//        setTrReveal(tr,"background:white",false)
//    }else{
//	    setTrReveal(tr,"background:"+color,false)
//    }
    setTrReveal(tr,"background:"+color,true)
    tb.selRow=tr
    return true;
}

function setTrReveal(tr,css,isclear,noDelay){
    var i
    if(!document.all)
        return tr.style.cssText+=";"+css
    if(curRow!=null&&curRow!=undefined&&isclear){//如果没有按下ctrl键，则原点击行取消选中状态
        curRow.style.cssText+=";background:white";
        var k,trRows;
        for(k=0;k<curRow.parentNode.rows.length;k++){
            trRows = curRow.parentNode.rows[k];
            for(i=0;i< trRows.cells.length;i++){
	            trRows.cells[i].style.cssText = trRows.cells[i].style.cssText.toLowerCase().replace("background: "+clickColor,"");
	            //curRow.cells[i].style.cssText+=";background:white";
	        }
        }
//        for(i=0;i< curRow.cells.length;i++){
//            curRow.cells[i].style.cssText = curRow.cells[i].style.cssText.toLowerCase().replace("background: #a9b2ca","");
//            //curRow.cells[i].style.cssText+=";background:white";
//        }
        select_rows.length--;
    }
    tr.style.cssText+=";"+css;
    select_rows[select_rows.length++]=tr;
    curRow=tr;
    if(css.indexOf('white')>-1){
        for(i=0;i< curRow.cells.length;i++){
            curRow.cells[i].style.cssText = curRow.cells[i].style.cssText.toLowerCase().replace("background: "+clickColor,"");
        }
    }else{
	    for(i=0;i< curRow.cells.length;i++){
	        curRow.cells[i].style.cssText+=";"+css;
	    }
    }
}

/**
 * 用指定颜色标识选中框，并在选择文本页中展现已选择的值
 * “已选择：”
 */
function checkValue(obj,tdobj,color,flag){
	if(!tdobj){return;}
	var showValue=tdobj.innerText;
	var objshowvalue = obj.showValue;
//    alert(objshowvalue+"\n"+tdobj.innerText+"\n"+color+"\n"+flag+"\nnamecol:"+namecol)
	if(namecol>0&&sMenu){
		if(obj.offsetParent.parentNode.cells[namecol].innerValue!=undefined)
			objshowvalue = obj.offsetParent.parentNode.cells[namecol].innerValue;
		else
			objshowvalue = obj.offsetParent.parentNode.cells[namecol].innerText;
	}
//    alert(objshowvalue+"\n"+tdobj.innerText+"\n"+color+"\n"+flag+"\nnamecol:"+namecol)
    if(objshowvalue.length>15){
        objshowvalue = objshowvalue.substring(0,15)+"...";
    }
	if(obj.checked==true){
		setTrReveal(obj.offsetParent.parentNode,"background:"+color,false);  
		//obj.offsetParent.parentNode.style.cssText+=";background:"+color;
		if(flag&&tdobj!=undefined){//&&showValue.indexOf(objshowvalue)==-1){
			if(showValue.indexOf("：")==showValue.length-1){
				tdobj.innerText=showValue+objshowvalue;
			}
			else{
				tdobj.innerText=showValue+"，"+objshowvalue;
			}
			/*处理列表全选问题，当下面全部选中时，将全选框选中 ---start-----*/
	    	var flag2 = true; //设置标志
			$("input:checkbox").each(function(){
				if(!$(this).attr("checked")){
					if($(this).attr("name")=="checkall"){//排除全选框
					}else{
						flag2 = false;
					}
				}
			});
			if(flag2){
				  var chkAll = document.getElementsByName("checkall"); 
			            chkAll[0].click();
			}
			/*----------------------------end------------------------------------------*/
		}
		return obj.value;
	}else{
		//obj.offsetParent.parentNode.style.cssText+=";background:white";
        var chkAll = document.getElementsByName("checkall"); 
        if(chkAll[0].checked){
            chkAll[0].checked = false;
        }
		setTrReveal(obj.offsetParent.parentNode,"background:white",false);  
		if(flag&&tdobj!=undefined){
            if(showValue.indexOf(objshowvalue)=="4"){
                if(showValue.length-objshowvalue.length==showValue.indexOf(objshowvalue)){
                    showValue = showValue.replace(objshowvalue,"");
                }else{
	                showValue = showValue.replace(objshowvalue+"，","");
                }
                tdobj.innerText = showValue;
                return;
            }
            if(showValue.indexOf("，"+objshowvalue+"，")>0){
                showValue = showValue.replace(objshowvalue+"，","");
                tdobj.innerText = showValue;
                return;
            }
            
            if(showValue.length-objshowvalue.length==showValue.indexOf(objshowvalue)){
                showValue = showValue.replace("，"+objshowvalue,"");
                tdobj.innerText = showValue;
                return;
            }
            /*
	    	var pos=showValue.indexOf(objshowvalue);
	    	if(pos==-1)
	    		return ;
			var newpos=pos+objshowvalue.length;
			if(pos>4)
				pos=pos-1;
			else if(pos==4&&newpos<showValue.length)
				newpos=newpos+1;
			tdobj.innerText=showValue.substring(0,pos)+showValue.substring(newpos);
            */
            
		}
	}
}
/**
 * 复选框全选功能 	
 */
function checkAll(obj,tdobj,color,flag){
	var checkobjs=document.getElementsByName("chkButton");
    tdobj.innerText = "已选择：";
	for(var i=0;i<checkobjs.length;i++){
		checkobjs[i].checked=obj.checked;
		checkValue(checkobjs[i],tdobj,color,flag);
	}
}
/**
 * 获取复选框选择值的方法
 */
function getCheckValue(){
	var checkobjs=document.getElementsByName("chkButton");
	var chkvalue="";
	for(var i=0;i<checkobjs.length;i++){
		if(chkvalue.checked==true)
			chkvalue=chkvalue+","+chkvalue.value;
	}
	if(chkvalue.length>1)
		chkvalue=chkvalue.substring(1);
	return chkvalue;
}

/**
 * 用指定颜色标识选中框，并在选择文本页中展现已选择的值
 */
function checkSingleValue(obj,tdobj,color,flag){
	var showValue=tdobj.innerText;
	//obj.offsetParent.parentNode.style.cssText+=";background:"+color;
	TableMouseDown(color,obj.offsetParent);
	if(flag&&tdobj!=undefined){
//alert("flag:"+flag+"\ntdobj.innerText:"+tdobj.innerText+"\nshowValue:"+showValue+"\nobj.showValue:"+obj.showValue);
		tdobj.innerText=showValue.substring(0,showValue.indexOf("：")+1)+obj.showValue;
  //      alert(tdobj.innerText);
	}
}

/**
 * 获取单选框选择值的方法 	
 */
function getSingleValue(){
	var checkobjs=document.getElementsByName("chkRadio");
	var chkvalue="";
	for(var i=0;i<checkobjs.length;i++){
		if(checkobjs[i].checked==true)
			return checkobjs[i].value;
	}
}
function getSelectTD(){
	var crrentTdindex=0;
	for(var i=0;i<curRow.cells.length;i++){
    	if(curRow.cells[i]==curCol){
    		crrentTdindex=i;
    		break;
    	}
    }
    return crrentTdindex;
}
/** 
    * 冻结列
*/
function frezeColum(){
	//var crrentTd = testTable.getSelectTD();
	var table=getTable(curCol);
	var crrentTd = getSelectTD();
	var i,j;
	 for(i=0;i<table.rows.length-footnum;i++){
	 	for(j=0;j<table.rows[i].cells.length;j++){
	 		table.rows[i].cells[j].style.cssText="";
	 	}
	 }
	 //alert(table.rows[0].cells[0].parentElement.parentElement.parentElement.parentElement.tagName);
	//table.rows[0].cells[0].style.cssText="position:relative;top:expression(this.parentElement.parentElement.parentElement.parentElement.scrollTop);left:expression(this.parentElement.parentElement.parentElement.parentElement.scrollLeft);z-index:10";
	//for(j=1;j<=crrentTd;j++){
	//	table.rows[0].cells[j].style.cssText="position:relative;left:expression(this.parentElement.parentElement.parentElement.parentElement.scrollLeft);top:expression(this.parentElement.parentElement.parentElement.parentElement.scrollTop);z-index:10;";
	// }
	 for(i=1;i<table.rows.length-footnum;i++){
	 	//table.rows[i].style.position = "relative";
	 	for(j=0;j<=crrentTd;j++){
	 		table.rows[i].cells[j].style.cssText="position:relative;left:expression(this.parentElement.offsetParent.scrollLeft);z-index:1;";
	 	}
	 }
	 
}
function FixTable() {
    //获取table外div
    var element=getTable(curCol);
    var out_div = element.parentElement;
    //附加拖动toolbar事件，在拖动toolbar后调整用于拖拉div的位子
    out_div.attachEvent("onscroll", DivLocalizer);
    //如果指定COL_NUM，则锁定相应的列
    var crrentTd = getSelectTD();
    var tbRows = element.rows;
    var rowCell = null;
    for (var i = 0; i < element.rows.length-footnum; i++) {
        tbRows[i].style.position = "relative";
        for (var j = 0; j < crrentTd; j++) {
            rowCell = tbRows[i].cells[j];
            with (rowCell.style) {
                if (i != 0) {
                    //background = "99ffff";
                    zIndex = 1;
                }
                position = "relative";
                setExpression("left", "this.parentElement.offsetParent.scrollLeft");
            }
        }
    }
    //锁定表头
    var tbFirstRow = element.rows[0];
    with (tbFirstRow.style) {
        //background = "9999ff";
        position = "relative";
        setExpression("top", "this.offsetParent.scrollTop");
        zIndex = 10;
    }
}

var func_menu=null;
function registerMenu(menu1){
    func_menu = menu1;
}
function TableOnContextMenu(color,nameColNum) {
    var crrentTr = event.srcElement.parentElement;
    if(!isNaN(parseInt(nameColNum))){
		namecol=parseInt(nameColNum);
	}
    while (crrentTr.tagName != "TR") {
        if (crrentTr == null || crrentTr == "undefined" || crrentTr.tagName == "BODY") {
            return;
        }
        crrentTr = crrentTr.parentElement;
    }
    var crrentTrIndex = crrentTr.rowIndex;
	//过滤表头	
    if (0 == crrentTrIndex) {
        return;
    }
    //if (!TableMouseDown(color)) {
		//弹出
        if (func_menu) {
			func_menu.setIDS(GetIds(nameColNum));
			func_menu.setNames(GetNames(nameColNum));
            func_menu.showMenu("ChangeWidthTable");
        }
    //} else {
		//清颜色

   //     ChangeColour(true);
		//改变选中值

	//select_rows.length = 0;
    //    AddSelectRow(crrentTrIndex);
		//弹出
    //    if (func_menu) {
	//		func_menu.setIDS(GetIds());
	//		func_menu.setNames(GetNames());
    //        func_menu.showMenu("ChangeWidthTable");
    //    }
    //}
    //first_row = crrentTrIndex;
}

function GetIds(nameColNum) {
	return GetNames(nameColNum);
}
function GetNames(nameColNum) {
	var names = new Array();
	for (var i = 0; i < select_rows.length; i++) {
		names[names.length] = select_rows[i].cells[nameColNum].innerHTML;
	}
	return names;
}
function chickRightMouse(e){
	curCol = event.srcElement
	doAnyClick();	
	if(sMenu){	
		sMenu.showMenu("ChangeWidthTable");		
	}
	return false;
	
}
function doAnyClick(){
	srcElem = event.srcElement;	
	//crawl up the tree to find the table row	
	if(hasCheck=="true"){
		if(srcElem.tagName=="INPUT"&&srcElem.id=="checkall"){		
			chekAll(srcElem);
			return;
		}	
		if(srcElem.tagName=="INPUT"&&srcElem.id=="chkButton"){
			checkButton(srcElem);
			return;
		}
        document.getElementsByName("checkall")[0].checked = false;
	}
	while (srcElem.tagName != "TR" && srcElem.tagName != "TABLE")
		srcElem = srcElem.parentElement;
	if(srcElem.tagName != "TR") return;
	if(srcElem.parentElement.tagName=="THEAD") return;
	if(srcElem.parentElement.tagName=="TFOOT") return;
	if(selRow!=-1){
	  if(event.ctrlKey){
                if(srcElem.innerHTML.indexOf("chkRadio")>-1&&srcElem.innerHTML.indexOf("chkRadioTd")>-1){//单选框
                    //chkRadioCheck(srcElem,true);
                    clickChecked(srcElem);
                }else{//复选框
				//	clearSelItems(srcElem);
					addSelItem(srcElem);
                    showSelStatus();
                    var flag2 = true; //设置标志
                    //判断是否全部选中
                    $("input:checkbox").each(function(){
                    	if(!$(this).attr("checked")){
                    		if($(this).attr("name")=="checkall"){//排除全选框
                    		}else{
                    			flag2 = false;
                    		}
                    	}
                    });
                    if(flag2){
                    	var chkAll = document.getElementsByName("checkall"); 
                    	chkAll[0].click();
                    }
                }
			
	    }
	    else{
	    	  if(event.shiftKey) selectBetween(srcElem)
			  else{
	                if(srcElem.innerHTML.indexOf("chkRadio")>-1&&srcElem.innerHTML.indexOf("chkRadioTd")>-1){//单选框
                        //chkRadioCheck(srcElem,true);
                        clickChecked(srcElem);
	                }else{//复选框
						clearSelItems(srcElem);
						addSelItem(srcElem);
                        
                        showSelStatus();
	                }
				}
		}
	}
	else{
	  addSelItem(srcElem);
	}

	//showSelStatus();

	 var oEvent 	= document.createEventObject();
	 oEvent.selected = srcElem;
	 //rowSelect.fire(oEvent);
	 getTable(curCol).fireEvent('onclick', oEvent);
}
function addSelItem(oItem){
	for(si=0;si<selRow+1;si++){
		if(selRows[si]==oItem) {
			return false;
		};
	}	
	
	oItem.style.backgroundColor = slColor;		
	selRow++;
	selRows[selRow]=oItem;
	if(hasCheck=="true"){	
		selectChkButton(oItem);
	}	
}
function selectBetween(){
	var cRows=srcElem.parentElement.rows;
	var trMin=10000;
	var trMax=0;

	for(i=0;i<selRow+1;i++)	if (selRows[i].rowIndex<trMin) trMin=selRows[i].rowIndex;
	if (srcElem.rowIndex<trMin) {trMax=trMin;trMin=srcElem.rowIndex;}
		else{trMax=srcElem.rowIndex}
	clearSelItems(srcElem);
	for(i=trMin-1;i<trMax;i++){
		addSelItem(cRows[i]);
	}
}
function clearSelItems(otr){
	var oRows=otr.parentElement.rows;//彻底清除	
	for (var i=0;i<oRows.length;i++){		
		oRows[i].style.backgroundColor=orColor;		
		if(hasCheck=="true"){
			delSelectChkButton(oRows[i]);
		}		
	}
	selRow=-1;
	selRows=new Array();
	
}
function delSelItem(oItem){
	var obj;
	oItem.style.backgroundColor = orColor;		
	for(var i=0;i<selRow+1;i++){		
		if(selRows[i]==oItem) selRows[i]=selRows[selRow];
	}	
	selRow--;	
	if(hasCheck=="true"){
		delSelectChkButton(oItem);
	}
}
function selectChkButton(oItem){	
	chkButtonCheck(oItem,true);
}
function delSelectChkButton(oItem){	
	chkButtonCheck(oItem,false);
}
function showSelStatus(){
	var sInfo="已选择：";
	_sIDs= new Array();
	_sNames= new Array();
	for(i=0;i<selRow+1;i++){
		//sInfo+=selRows[i].rowIndex+",";
		var objshowvalue="";
        
		if(selRows[i].cells[namecol].innerValue!=undefined)
			objshowvalue = selRows[i].cells[namecol].innerValue;
		else
			objshowvalue = selRows[i].cells[namecol].innerText;
            
        if(namecol==0){
            var ooo = selRows[i].cells[1].getElementsByTagName("input")[0];
            
            if(ooo != null){
            	objshowvalue = ooo.showValue;
            }
        }
    if(undefined != objshowvalue&&objshowvalue!=null){
        if(objshowvalue.length>15){
	        objshowvalue = objshowvalue.substring(0,15)+"...";
	    }
	 }
		if(_sNames.length==0){
			sInfo+=objshowvalue;
		}else{
			sInfo+="，"+objshowvalue;
		}
        
		sIDArr = new Array();
		for(sIt = 0;sIt<idcols.length;sIt++){
			sIDArr[sIDArr.length]= selRows[i].cells[idcols[sIt]].innerHTML;
		}
		if(sIDArr.length>0){
			_sIDs[_sIDs.length]=sIDArr;
		}
		_sNames[_sNames.length]=selRows[i].cells[namecol].innerHTML;
	}
	if(undefined != sMenu && sMenu){
		sMenu.setIDS(_sIDs);
		sMenu.setNames(_sNames);
	}
	tableStatus.innerText=ignoreSpaces(sInfo);
}
//过滤空格
function ignoreSpaces(string) {
		var temp = "";
		string = '' + string;
		splitstring = string.split("&nbsp;");
		for(i = 0; i < splitstring.length; i++)
			temp += splitstring[i];
		
		return temp;
}
function chkButtonCheck(oItem,value){
	for(var chkTdindex=0;chkTdindex<oItem.children.length;chkTdindex++){		
		if(oItem.children[chkTdindex].id=='chkButtonTd'){
			for(var chkindex=0;chkindex<oItem.children[chkTdindex].children.length;chkindex++){				
				if(oItem.children[chkTdindex].children[chkindex].name=='chkButton'){	
					oItem.children[chkTdindex].children[chkindex].checked=value;
				}				
			}	
		}	
	}		
}

function chkRadioCheck(oItem,value){
    for(var chkTdindex=0;chkTdindex<oItem.children.length;chkTdindex++){        
        if(oItem.children[chkTdindex].id=='chkRadioTd'){
            for(var chkindex=0;chkindex<oItem.children[chkTdindex].children.length;chkindex++){             
                if(oItem.children[chkTdindex].children[chkindex].name=='chkRadio'){    
                    oItem.children[chkTdindex].children[chkindex].checked=value;
                }               
            }   
        }   
    }       
}

function doFromSubmit(fromName){
    try{
	    var img_sousuo = ""+$("#img_sousuo").attr("src");
	    if("undefined"!=img_sousuo&&null!=img_sousuo&&"null"!=img_sousuo&&""!=img_sousuo&&undefined!=img_sousuo){
	        $("#img_sousuo").click();
	    }else{
	        document.getElementById(fromName).submit();
	    }
    }catch(e){
        document.getElementById(fromName).submit();
    }
}


function isNumber(oNum) {
          if(!oNum) return false;
          var strP=/^\+?[1-9][0-9]*$/;
          if(!strP.test(oNum)) return false;
          try{
            if(parseFloat(oNum)!=oNum) return false;
          }catch(ex){
            return false;
          }
          return true;
       }

       
       function writeFlag(imgStr,showvalue){
            if(imgStr!=""){
	            var chkBox = document.getElementById("chkButton"+imgStr);
	            var radBox = document.getElementById("radio"+imgStr);
                if(chkBox!="null"&&chkBox!=null){
                    chkBox.parentElement.innerHTML += showvalue;
                }
                if(radBox!="null"&&radBox!=null){
                    radBox.parentElement.innerHTML += showvalue;
                }
            }
       }
       
       //单击选中行
		function clickChecked(trObj){
//		    alert("clickChecked()\n"+trObj.innerHTML); 
            var checkobjs=trObj.children;
		    for(var i=0;i<checkobjs.length;i++){
		        if(checkobjs[i].id=="chkButtonTd"){
                    checkobjs[i].children[0].checked=(!checkobjs[i].children[0].checked);
//                    checkobjs[i].children[0].onclick();     //  467 >> checkValue(obj,tdobj,color,flag)
                    checkValue(checkobjs[i].children[0],document.getElementById('myTable_td'),clickColor,true);
                }
                if(checkobjs[i].id=="chkRadioTd"){
                    
                    if(checkobjs[i].children[0].checked!=true){
	                    checkobjs[i].children[0].checked=(!checkobjs[i].children[0].checked);
                    }
//                    checkobjs[i].children[0].onclick();     //  564 >> checkSingleValue(obj,tdobj,color,flag)
                  //  alert("chkRadioTd");
                  //  alert("checkobjs[i].children[0]:"+checkobjs[i].children[0]+"\ndocument.getElementById('myTable_td'):"+document.getElementById('myTable_td')+"\nclickColor:"+clickColor)
                    checkSingleValue(checkobjs[i].children[0],document.getElementById('myTable_td'),clickColor,true);
                }
		    }
		}

        
        