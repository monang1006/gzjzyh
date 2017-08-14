/*------------------------------------------------------------------------------
+                        工具条   					                           +
+                            						                           +
+						          		       							       +
-------------------------------------------------------------------------------*/

function mu(k){
	this.sel_head_index=-1
	this.css=new Array()
	this.css["head_df"]="margin:1;padding:3;padding-left:7;padding-right:7"
	this.css["head_fc"]="padding:2;padding-left:6;padding-right:6;border:1px solid white;border-right:1px solid #808080;border-bottom:1px solid #808080"
	this.css["head_hit"]="padding:2;padding-left:6;padding-right:6;border:1px solid #808080;border-right:1px solid white;border-bottom:1px solid white"
	this.ini=function(k){
		var l,i,a=k.split(";"),s_caption
		l="<span style='position:absolute;z-index:100;background:buttonface;width:100%;height:23;border:1px solid white;border-right:1px solid #808080;border-bottom:1px solid #808080'>\
		<table id=o_menu onmouseover='mu1.m_over(event.srcElement)' onmouseout='mu1.m_out(event.srcElement)' onmousedown='mu1.m_down(event.srcElement)' cellpadding=0 cellspacing=0><tr>"
		for(i in a){
			a[i]=a[i].split(":")
			s_caption=a[i][0]
			if(s_caption.indexOf("&")>-1)
				s_caption=s_caption.replace("&","(<u>")+"</u>)"
			l+="<td val='"+a[i][0].slice(0,(a[i][0]+"&").indexOf("&"))+"' list='"+a[i][1]+"' style='"+this.css["head_df"]+"'>"+s_caption+"</td>"
		}
		l+="</tr></table></span>"
		l+="<span id=o_menu_op style='display:none;z-index:200;padding:1;position:absolute;top:21;width:140;height:20;background:buttonface;border:2px outset;filter:progid:DXImageTransform.Microsoft.Shadow(color=#404040,direction=135,Strength=3)'>\
		</span>"
		document.write(l)
	}
	this.ini(k)
	this.m_over=function(ee){
		if(ee.tagName=="U")
			ee=ee.parentElement
		if(ee.tagName=="TD"){
			if(this.sel_head_index==-1)
				ee.style.cssText=this.css["head_fc"]
			else if(this.sel_head_index!=ee.cellIndex){
				o_menu.rows(0).cells(this.sel_head_index).style.cssText=this.css["head_df"]
				this.sel_head_index=ee.cellIndex
				this.m_down(o_menu.rows(0).cells(this.sel_head_index))
			}
		}
	}
	this.m_out=function(ee){
		if(ee.tagName=="U")
			ee=ee.parentElement
		if(ee.tagName=="TD"&&this.sel_head_index==-1){
			ee.style.cssText=this.css["head_df"]
		}
	}
	this.m_down=function(ee){
		if(ee.tagName=="U")
			ee=ee.parentElement
		if(ee.tagName=="TD"){
			this.sel_head_index=ee.cellIndex
			ee.style.cssText=this.css["head_hit"]
			var l="",a=ee.list.split(","),i,s_caption
			for(i in a){
				if(a[i]=="-")
					l+="<hr style='border-top:1px solid #808080;border-bottom:1px solid white'>"
				else{
					s_caption=a[i]
					if(s_caption.indexOf("&")>-1)
						s_caption=s_caption.replace("&","(<u>")+"</u>)"
					l+="<div onmouseup=\"mu1.sel('menu_"+ee.val+"_"+a[i].slice(0,(a[i]+"&").indexOf("&"))+"')\" onmouseover=\"this.style.color='white';this.style.background='#0A246A'\" onmouseout=\"this.style.color='';this.style.background='buttonface'\" style=background:buttonface;padding:2;padding-left:16>"+s_caption+"</div>"
				}
			}
			o_menu_op.parentElement.style.left=ee.offsetLeft
			o_menu_op.innerHTML=l
			o_menu_op.style.display=""
			this.status="hit"
		}
	}
	this.sel=function(k){
		o_menu_op.style.display="none"
		o_menu.rows(0).cells(this.sel_head_index).style.cssText=this.css["head_df"]
		this.sel_head_index=-1
		sysMenu_click(k)
	}
	this.blur=function(){
		if(this.sel_head_index==-1)
			return
		o_menu_op.style.display="none"
		o_menu.rows(0).cells(this.sel_head_index).style.cssText=this.css["head_df"]
		this.sel_head_index=-1
	}
}




function bar(k){
	this.css=new Array()
	this.css["df"]="margin:1"
	this.css["up"]="border:1px solid white;border-right:1px solid #808080;border-bottom:1px solid #808080"
	this.css["down"]="border:1px solid #808080;border-right:1px solid white;border-bottom:1px solid white"
	this.ini=function(k){
		var l,a=k.split(","),i
		l="<span  style='z-index:100;background:buttonface;position:absolute;left:0;top:0;width:995;height:27;border-bottom:1px solid #404040'>\
		<span style='width:100%;height:100%;border:1px solid white;border-right:1px solid #808080;border-bottom:1px solid #808080'>\
		<img align=absmiddle src=image/split2.gif>"
		for(i in a){
			if(a[i]=="|")
				l+="<img align=absmiddle src=images/workflow/split.gif>"
			else
				l+="<img val='"+a[i]+"' align=absmiddle src=images/workflow/"+a[i]+".gif onmouseover=bar1.m_over(event.srcElement) onmouseout=bar1.m_out(event.srcElement) onmousedown=bar1.m_down(event.srcElement) onmouseup=bar1.m_up(event.srcElement) style='"+this.css["df"]+"'>"
		}
		l+="</span></span>"
		document.write(l)
	}
	this.ini(k)
	this.m_over=function(ee){
		ee.style.cssText=this.css["up"]
	}
	this.m_out=function(ee){
		ee.style.cssText=this.css["df"]
	}
	this.m_down=function(ee){
		ee.style.cssText=this.css["down"]
	}
	this.m_up=function(ee){
		ee.style.cssText=this.css["up"]
		try{
			sysMenu_click("bar_"+ee.val)
		}catch(e){}
	}
}





	var toolBarButtonCss=new Array();
	toolBarButtonCss["df"]  =  "margin:2";
	toolBarButtonCss["up"]  =  "border:2px solid white;"+
        					   "border-right:2px solid #808080;"+
                               "border-bottom:2px solid #808080";
	toolBarButtonCss["down"] = "background:#e0e0e0;"+
        					   "border:2px solid #808080;"+
                               "border-right:2px solid white;"+
                               "border-bottom:2px solid white";

	//speed工具栏按钮的点击
	function speedToolBarButtonClick(aObj) {
		if ( !(aObj.isDown == true) ) {
			//重置所有的按钮
			resetAllToolBarButton(aObj.parentElement);
			//设置当前按钮
        	aObj.style.cssText = toolBarButtonCss["down"];
			aObj.isDown = true;
			try{
				leftToolBarClick( aObj.id.replace("btn_","") );
			}catch(e){}
		}

	}
	//speed工具栏按钮的onmouseover
	function speedToolBarButtonOver(aObj) {
        if ( !(aObj.isDown == true) ) {
			aObj.style.cssText = toolBarButtonCss["up"]
        }
	}
	//speed工具栏按钮的onmouseout
	function speedToolBarButtonOut(aObj) {
        if ( !(aObj.isDown == true) ) {
			aObj.style.cssText = toolBarButtonCss["df"]
        }
	}


	//重置所有的按钮
	function resetAllToolBarButton(aObj) {
		for (var i=0;i<aObj.children.length;i++) {
			aObj.children[i].style.cssText = toolBarButtonCss["df"];
			aObj.children[i].isDown = false;
		}
	}


	//工具条类
	function ToolBar(aStrList,left,top,width,height) {
		//property
		this.toolBarName;
		//constructor
		this.create = create;
		this.create();
		//method
		this.clickBtn = clickBtn;
		function create() {
			var str,a=aStrList.split(",");
			//工具条样式
			var spnStyle =  "position:relative;"+
							"left:"+left+";"+
							"top:"+top+";"+
							"width:100%;"+
							"height:100%;"+
							"padding:6;"+
							"z-index:100;"+
							"border-left:2px outset;"+
							"border-top:2px outset;"+
							"border-right:2px outset;"+
							"border-bottom:2px outset;"+
							"background:buttonface";
			var spnchildStyle= "width:"+(parseInt(width)-6)+";"+
							"height:"+(parseInt(height)-50)+";"+
							"border:2px inset;overflow:auto;"+
							"background:buttonface";
			this.toolBarName = "selectItem";//( ("tbar_"+Math.random()).replace(".","") ).substr(0,10);

			//输出
			str="";
//			str="<span style='"+spnStyle+"'>";
			//str=str+"<span id='"+this.toolBarName+"' onselectstart=\"return false\"  style='"+spnStyle+"' align='center'>";
			for(var i=0;i<a.length;i++){
				if (a[i]=="|") {
				//	str+="<img align=absmiddle src=" + systemroot + "/images/workflow/split.gif>";
					str += "<br/>";
				} else {
					var btnName = "btn_"+a[i];
					str  += " <img align=absmiddle"+
					   		" isDown='false' "+
                       		" onmouseover ='speedToolBarButtonOver(this)' "+
					   		" onmouseout ='speedToolBarButtonOut(this)' "+
					   		" onclick = 'speedToolBarButtonClick(this)' "+
					   		" name='"+btnName+"' "+
					   		" id='"+btnName+"' "+
			           		" style='"+toolBarButtonCss["df"]+
					   		"' src='" + systemroot + "/images/ico/"+a[i]+".gif' alt='"+buttonname[a[i]]+"'> ";
				}
			}
			//str+="</span>"
			//document.write(str);
			eval(this.toolBarName).innerHTML = str;
		}

		function clickBtn(i) {
			eval(this.toolBarName).children[i].click();
		}
	}



