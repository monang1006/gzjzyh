//当前被选择的Tab的索引，默认为0，
//这个值要与下边表格中显示的Tab页相对应，
//不要直接调用该变量，通过调用getSelectedIndex()来实现
var SELECTED_INDEX = 0;

///////////////////以下为公用函数，可直接调用//////////////

/** 这个为用户接口（由用户来控制Tab页的onchange事件以及原page页的onfocus、onblur），
 *  可以通过设置返回值true/false来继续执行或取消Tab页切换事件
 * 当切换Tab页前被触发
 * @param selIdx 当前的Tab页索引
 * @param newIdx 即将切换的Tab页的索引
 * return true/false 继续执行或取消Tab页切换事件
 */
function interface_onchange(selIdx,newIdx){
	//alert("Tab页切换："+selIdx+"->"+newIdx);
	return true;
}

/** 使某个Tab页为disabled状态，相当于原Tab页的每个page的disabled = true/false
 * @param bl true/false
 * @param idx Tab页索引
 */
function disablePage(bl,idx){
	if(!checkBound(idx)){
		return;
	}
	htcTabList.cells(idx).disabled = bl;
	htcPanel.rows(idx).disabled = bl;
}

/** 某个索引页是否被disabled
 * @param idx 页索引
 * @return true/false 是否被disabled
 */
function isDisabled(idx){
	if(!checkBound(idx)){
		return false;
	}
	return htcTabList.cells(idx).disabled;
}

/** 返回当前的索引
 * @return 当前的索引
 */
function getSelectedIndex(){
	return SELECTED_INDEX;
}

/** 设置当前被选中的Tab页
 *  相当于原tab.htc的selectedIndex = idx;
 * @param idx 索引，从0开始
 */
function setSelectedIndex(idx){
	if(idx == SELECTED_INDEX || !checkBound(idx)){
		return;
	}
	var rtn = interface_onchange(SELECTED_INDEX,idx);
	if(rtn == null || !rtn){
		return;
	}
	if(SELECTED_INDEX != -1){
		htcTabList.cells(SELECTED_INDEX).className = "tab";
		htcPanel.rows(SELECTED_INDEX).className = "pane_hide";
	}
	htcTabList.cells(idx).className = "tab_sel";
	htcPanel.rows(idx).className = "pane";
	SELECTED_INDEX = idx;
}

///////////////////以上为公用函数，可直接调用//////////////

/////////////////////以下函数不要调用/////////////////////

/**
 * 点击Tab页时选中它
 */
function tab_doClick(){
	//给TD注册事件时，srcElement肯定是TD对象
	var obj = event.srcElement;
	while (obj.nodeName != "TD") {//防止td里有其它标签
		obj = obj.parentNode;
	}
	setSelectedIndex(obj.cellIndex);
}

/** idx越界检查
 * @param idx 要检查的索引
 * @return true/false是否通过边界检查
 */
function checkBound(idx)
{
	if(idx < 0 || idx > htcTabList.cells.length -1){
		alert("索引越界:"+idx+">"+(htcTabList.cells.length -1));
		return false;
	}
	return true;
}
/////////////////////以上函数不要调用////////////////////
