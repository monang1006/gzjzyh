/************************************************
** 创建人:	耿洪亮
** 日 期:	2004.2.20
** 描 述:	数字微调
**
** 版 本:	1.0
************************************************/
var _interv = null;
var _interv_ob = null;
var _currob = null;
function _inchingswitch(obj,flag)
{
	var o,ob,ar;
	if(_currob != null&&obj.parentElement.parentElement.contains(_currob))
	{
		o = _currob;
		ob = _currob.value;
		ar = _currob.conf.split(",");
	}
	else
	{
		o = obj.parentElement.nextSibling;//输入筐
		ob = obj.parentElement.nextSibling.value;//输入筐值
		ar = obj.parentElement.nextSibling.conf.split(",");//参数用逗号分割
	}

	var step = parseInt(ar[0]);
	var min = parseInt(ar[1]);
	var max = parseInt(ar[2]);
	var alph = ar[3];

	if(isNaN(min))
		min = 1;
	if(ob=="")
	ob=min;//如果输入域为空则默认是最小值
	if(typeof(alph)!="undefined"&&alph!="")
	{
		if(alph==ob.substr(ob.length-2,ob.length-1))
			ob = ob.substr(0,ob.length-1);
	}
	var obvalue = parseInt(ob);
	if(isNaN(obvalue))
	{
//		alert("微调初始值是非数字！");
		obvalue = min;
	}
	if(isNaN(step))
		step = 1;
	if(flag=="+")
	{
		obvalue = obvalue + step;
		if(obvalue>max)
			return;
	}
	if(flag=="-")
	{
		obvalue = obvalue - step;
		if(obvalue<min)
			return;
	}
	o.value = obvalue+alph;
	o.select();
}
function _uper(ob)
{
	//alert(event.fromElement);
	if(_interv_ob!=null)
		_inchingswitch(_interv_ob,"+");
	else
		_inchingswitch(ob,"+");
}

function _conti_uper(obb)
{
	if(_interv==null)
	{
		_interv_ob = obb;
		_interv=window.setInterval("_uper()", 200);
	}
}

function _stop_inch()
{
	if(_interv!=null)
	{
		clearInterval(_interv);
		_interv=null;
		_interv_ob = null;
	}
}

function _downer(ob)
{
	if(_interv_ob!=null)
		_inchingswitch(_interv_ob,"-");
	else
		_inchingswitch(ob,"-");
}

function _conti_downer(obb)
{
	if(_interv==null)
	{
		_interv_ob = obb;
		_interv=window.setInterval("_downer()", 200);
	}
}
function _selthis(ob)
{
	ob.select();//alert(ob.tagName);
	_currob = ob;
}