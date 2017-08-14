/**
 *	???????????????
 * @copyright Strongit
 * @author zhoujx
 * @date 2008
*/
  
  //document.onclick=OnClick;
  //document.oncontextmenu = new Function("showMenu();window.event.returnValue=false;");
  /** ???????????????,?????????
   */
  function RightMenu()
  {
	createStyleSheet();
    this.addExtendMenu=addExtendMenu;
    this.addItem=addItem;
    this.getMenu=getMenu;
    this.HideAll=HideAll;
    this.I_OnMouseOver=I_OnMouseOver;
    this.I_OnMouseOut=I_OnMouseOut;
    this.I_OnMouseUp=I_OnMouseUp;
    this.P_OnMouseOver=P_OnMouseOver;
    this.P_OnMouseOut=P_OnMouseOut;
    A_ROOT = new Array();
    HTMLstr  = "";
    HTMLstr += "<!-- RightButton PopMenu -->\n";
    HTMLstr += "\n";
    HTMLstr += "<!-- PopMenu Starts -->\n";
    HTMLstr += "<div style='z-index:10' onmouseleave='OnTabClick()'><div id='E_ROOT' class='rm_div'>\n";
                        // ROOT = right button pop menu
    HTMLstr += "<table width='100%' border='0' cellspacing='0'>\n";
    //HTMLstr += "<tr><td width='20' valign='bottom' onclick='window.event.cancelBubble=true;' class='info'>???<BR>???<BR>???<BR>???\n";
	HTMLstr += "<tr><td style='width:20' valign='bottom' onclick='window.event.cancelBubble=true;' class='info'>&nbsp;&nbsp;";
    HTMLstr += "</td><td width='120' style='padding: 1' valign='top'>\n";
    HTMLstr += "<table width='100%' border='0' cellspacing='0'>\n";
    HTMLstr += "<!-- Insert A Extend Menu or Item On Here For E_ROOT -->\n";
    HTMLstr += "</table></td></tr></table>\n";
    HTMLstr += "</div>\n";
    HTMLstr += "<!-- Insert A Extend_Menu Area on Here For E_ROOT -->";
    HTMLstr += "\n";
    HTMLstr += "</div><!-- PopMenu Ends -->\n";
  }

  /** ???????????????
   */
  function createStyleSheet(){
	styleInfo = document.createStyleSheet();
	styleInfo.addRule(".info","FONT-SIZE: 14px; WIDTH: 20px; COLOR: #015ebc; FONT-FAMILY: @Tahoma,@??????;filter:progid:DXImageTransform.Microsoft.Gradient(endColorstr='#0D73CE', startColorstr='#C6E1FB',gradientType='0'");
	styleInfo.addRule("TR.over","FONT-SIZE: 12px; CURSOR: default; COLOR: #ffffff; BACKGROUND-COLOR: #000080");
	styleInfo.addRule("TR.out","FONT-SIZE: 12px; COLOR: #000000; BACKGROUND-COLOR: #efefef");
	styleInfo.addRule("DIV.rm_div","BORDER-RIGHT: #ffffff 1px outset; PADDING-RIGHT: 1px; BORDER-TOP: #ffffff 1px outset; DISPLAY: none; PADDING-LEFT: 1px; FILTER: Alpha(Opacity='90'); PADDING-BOTTOM: 1px; BORDER-LEFT: #ffffff 1px outset; WIDTH: 0px; PADDING-TOP: 1px; BORDER-BOTTOM: #ffffff 1px outset; POSITION: absolute; HEIGHT: 0px; BACKGROUND-COLOR: #efefef");
	styleInfo.addRule("HR.sperator","BORDER-RIGHT: #ffffff 1px inset; BORDER-TOP: #ffffff 1px inset; BORDER-LEFT: #ffffff 1px inset; WIDTH: 95%; BORDER-BOTTOM: #ffffff 1px inset");
	styleInfo.addRule(".w2kfont","FONT-SIZE: 8pt; FONT-FAMILY: Tahoma");
  }

  /** ??????????????????
   * @param id ????????????id?????????????????????
   * @param wh ??????????????????????????????img??????????????????Wingdings?????????????????????????????????
   * @param name ?????????????????????????????????HTML??????
   * @param parent ?????????id??????
   */
  function addExtendMenu(id,wh,name,parent)
  {
    var TempStr = "";

    eval("A_"+parent+".length++");
    eval("A_"+parent+"[A_"+parent+".length-1] = id");  // ?????????????????????????????????ID????????????
    TempStr += "<div id='E_"+id+"' class='rm_div'>\n";
    TempStr += "<table width='100%' border='0' cellspacing='0'>\n";
    TempStr += "<!-- Insert A Extend Menu or Item On Here For E_"+id+" -->";
    TempStr += "</table>\n";
    TempStr += "</div>\n";
    TempStr += "<!-- Insert A Extend_Menu Area on Here For E_"+id+" -->";
    TempStr += "<!-- Insert A Extend_Menu Area on Here For E_"+parent+" -->";
    HTMLstr = HTMLstr.replace("<!-- Insert A Extend_Menu Area on Here For E_"+parent+" -->",TempStr);
    
    eval("A_"+id+" = new Array()");
    TempStr  = "";
    TempStr += "<!-- Extend Item : P_"+id+" -->\n";
    TempStr += "<tr id='P_"+id+"' class='out'";
    TempStr += " onmouseover='P_OnMouseOver(\""+id+"\",\""+parent+"\")'";
    TempStr += " onmouseout='P_OnMouseOut(\""+id+"\",\""+parent+"\")'";
    TempStr += " onmouseup=window.event.cancelBubble=true;";
    TempStr += " onclick=window.event.cancelBubble=true;";
    TempStr += "><td nowrap>";
    TempStr += "<font face='Wingdings' style='font-size:18px'>0</font>&nbsp;"+name+"&nbsp;&nbsp</td><td style='font-family: webdings; text-align: ;'>4";
    TempStr += "</td></tr>\n";
    TempStr += "<!-- Insert A Extend Menu or Item On Here For E_"+parent+" -->";
    HTMLstr = HTMLstr.replace("<!-- Insert A Extend Menu or Item On Here For E_"+parent+" -->",TempStr);
  }
  /** ???????????????
   * @param id ????????????id?????????????????????
   * @param wh ??????????????????????????????img??????????????????Wingdings?????????????????????????????????
   * @param name ?????????????????????????????????HTML??????
   * @param parent ?????????id??????
   * @param actioin ??????????????????
   */
  function addItem(id,wh,name,parent,action)
  {
    var TempStr = "";
    var ItemStr = "<!-- ITEM : I_"+id+" -->";
    if(id == "sperator")
    {
      TempStr += ItemStr+"\n";
      TempStr += "<tr class='out' onclick='window.event.cancelBubble=true;' onmouseup='window.event.cancelBubble=true;'><td colspan='2' height='1'><hr class='sperator'></td></tr>";
      TempStr += "<!-- Insert A Extend Menu or Item On Here For E_"+parent+" -->";
      HTMLstr = HTMLstr.replace("<!-- Insert A Extend Menu or Item On Here For E_"+parent+" -->",TempStr);
      return;
    }
    if(HTMLstr.indexOf(ItemStr) != -1)
    {
      alert("I_"+id+"already exist!");
      return;
    }
    TempStr += ItemStr+"\n";
    TempStr += "<tr id='I_"+id+"' class='out'";
    TempStr += " onmouseover='I_OnMouseOver(\""+id+"\",\""+parent+"\")'";
    TempStr += " onmouseout='I_OnMouseOut(\""+id+"\")'";
    TempStr += " onclick='window.event.cancelBubble=true;'";
    TempStr += " onmouseup='I_OnMouseUp(\""+id+"\",\""+parent+"\",\""+action+"\")'";
    TempStr += "><td nowrap>";
    TempStr +="<font face='Wingdings' style='font-size:18px'>"+wh+"</font>&nbsp;"+ name+"&nbsp;";
    TempStr += "</td><td></td></tr>\n";
    TempStr += "<!-- Insert A Extend Menu or Item On Here For E_"+parent+" -->";
    HTMLstr = HTMLstr.replace("<!-- Insert A Extend Menu or Item On Here For E_"+parent+" -->",TempStr);
  }

  /** ???????????????HTML??????
   * @return ?????????HTML??????
   */
 function getMenu()
  {
    return HTMLstr;
  }

  /** ????????????????????????
   * @param id ??????
   * @param parent ?????????
   */
  function I_OnMouseOver(id,parent)
  {
    var Item;
    if(parent != "ROOT")
    {
      var ParentItem;
      ParentItem = eval("P_"+parent);
      ParentItem.className="over";
    }
    Item = eval("I_"+id);
    Item.className="over";
    HideAll(parent,1);
  }

  /** ????????????????????????
   * @param id ??????
   */
  function I_OnMouseOut(id)
  {
    var Item;
    Item = eval("I_"+id);
    Item.className="out";
  }

  /** ??????????????????????????????
   * @param id ??????
   * @param parent ?????????
   * @param action ???????????????
   */
  function I_OnMouseUp(id,parent,action)
  {
    var ParentMenu;
    window.event.cancelBubble=true;
    OnTabClick();
    ParentMenu = eval("E_"+parent);
    ParentMenu.display="none";
	eval(action);
  }

  /** ??????????????????
   * @param id ??????
   * @param parent ?????????
   */
  function P_OnMouseOver(id,parent)
  {
    var Item;
    var Extend;
    var Parent;
    if(parent != "ROOT")
    {
      var ParentItem;
      ParentItem = eval("P_"+parent);
      ParentItem.className="over";
    }
    HideAll(parent,1);
    Item = eval("P_"+id);
    Extend = eval("E_"+id);
    Parent = eval("E_"+parent);
    Item.className="over";
    Extend.style.display="block";
    Extend.style.posLeft=document.body.scrollLeft+Parent.offsetLeft+Parent.offsetWidth-4;
    if(Extend.style.posLeft+Extend.offsetWidth > document.body.scrollLeft+document.body.clientWidth)
        Extend.style.posLeft=Extend.style.posLeft-Parent.offsetWidth-Extend.offsetWidth+8;
    if(Extend.style.posLeft < 0) Extend.style.posLeft=document.body.scrollLeft+Parent.offsetLeft+Parent.offsetWidth;
    Extend.style.posTop=Parent.offsetTop+Item.offsetTop+1;
    if(Extend.style.posTop+Extend.offsetHeight > document.body.scrollTop+document.body.clientHeight)
      Extend.style.posTop=document.body.scrollTop+document.body.clientHeight-Extend.offsetHeight;
    if(Extend.style.posTop < 0) Extend.style.posTop=0;
  }

  /** ????????????????????????
   * @param id ??????
   * @param parent ?????????
   */
  function P_OnMouseOut(id,parent)
  {
  }

  /** ??????
   * @param id ??????
   * @param flag ??????
   */
  function HideAll(id,flag)
  {
    var Area;
    var Temp;
    var i;
    if(!flag)
    {
      Temp = eval("E_"+id);
      Temp.style.display="none";
    }
    Area = eval("A_"+id);
    if(Area.length)
    {
      for(i=0; i < Area.length; i++)
      {
        HideAll(Area[i],0);
        Temp = eval("E_"+Area[i]);
        Temp.style.display="none";
        Temp = eval("P_"+Area[i]);
        Temp.className="out";
      }
    }
  }

  /** ????????????
   */
  function showTabMenu(){
      var PopMenu;
      PopMenu = eval("E_ROOT");
      HideAll("ROOT",0);
      PopMenu.style.display="block";
      PopMenu.style.posLeft=document.body.scrollLeft+window.event.clientX-2;
      PopMenu.style.posTop=document.body.scrollTop+window.event.clientY-2;
      if(PopMenu.style.posLeft+PopMenu.offsetWidth > document.body.scrollLeft+document.body.clientWidth)
        PopMenu.style.posLeft=document.body.scrollLeft+document.body.clientWidth-PopMenu.offsetWidth;
      if(PopMenu.style.posLeft < 0) PopMenu.style.posLeft=0;
      if(PopMenu.style.posTop+PopMenu.offsetHeight > document.body.scrollTop+document.body.clientHeight)
        PopMenu.style.posTop=document.body.scrollTop+document.body.clientHeight-PopMenu.offsetHeight;
      if(PopMenu.style.posTop < 0) PopMenu.style.posTop=0;
  }

  /** ????????????
   */
  function OnTabClick()
  {
    HideAll("ROOT",0);
  }

/*
??????????????????
======================================
.info {
	FONT-SIZE: 14px; WIDTH: 20px; COLOR: #ffffff; FONT-FAMILY: @Tahoma,@??????;
	 
}
TR.over {
	FONT-SIZE: 12px; CURSOR: default; COLOR: #ffffff; BACKGROUND-COLOR: #000080
}
TR.out {
	FONT-SIZE: 12px; COLOR: #000000; BACKGROUND-COLOR: #efefef
}
DIV.rm_div {
	BORDER-RIGHT: #ffffff 1px outset; PADDING-RIGHT: 1px; BORDER-TOP: #ffffff 1px outset; DISPLAY: none; PADDING-LEFT: 1px; FILTER: Alpha(Opacity='95'); PADDING-BOTTOM: 1px; BORDER-LEFT: #ffffff 1px outset; WIDTH: 0px; PADDING-TOP: 1px; BORDER-BOTTOM: #ffffff 1px outset; POSITION: absolute; HEIGHT: 0px; BACKGROUND-COLOR: #efefef
}
HR.sperator {
	BORDER-RIGHT: #ffffff 1px inset; BORDER-TOP: #ffffff 1px inset; BORDER-LEFT: #ffffff 1px inset; WIDTH: 95%; BORDER-BOTTOM: #ffffff 1px inset
}
.w2kfont {
	FONT-SIZE: 8pt; FONT-FAMILY: Tahoma
}

======================================
?????????
 var menu = new RightMenu();
menu.addItem("update","4","<font class=w2kfont>Web Update</font>","ROOT","alert(this.id);");
menu.addItem("suan","a","????????????","ROOT","alert(this.id);");
menu.addItem("sperator","","","ROOT",null);
menu.addExtendMenu("program","24","??????<font class=w2kfont>(<u>P</u>)</font>","ROOT",null);
menu.addExtendMenu("p_acc","+","??????","program",null);
menu.addItem("p_acc1","+","<font class=w2kfont>Microsoft FrontPage 2000</font>","program","alert(this.id);");
document.writeln(menu.getMenu());
**/