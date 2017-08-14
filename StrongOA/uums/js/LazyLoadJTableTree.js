// Honghai's JavaScript Tree Menu

/******************************************************************************
* Define the MenuItem object.                                                 *
******************************************************************************/
//-------------Honghai's codes begin......."

//define the const index for img..
var JTTTableStyleClassDef = "JTT_STable"
var JTTTableDefineDef     = "border=0 cellpadding=0 cellspacing=0"
var JTTHeadCellStyleClassDef = "JTT_SHeadCell"
var JTTHeadStyleClassDef     = "JTT_SHead"
var JTTRowCellStyleClassDef  = "JTT_SRowCell"
var JTTIndexHeadStyleClass   = "JTT_SHeadCell"
var JTTIndexRowStyleClass    = "JTT_SIndexRowCell"
var JTTIndexHeadText         = "#"

var JTTIconStyleClassDef     = "JTT_SIcon"     //"border-width:0px;border-top-width:0px;border-top:0"
                                              //<IMG align="left" border="0" VSPACE="0" hspace="0" 
                                              // width="19px" height="19px"
var JTTPlusMinusStyleClassDef= "JTT_SPlusMinus"//Cursor:hand;
var JTTTreeTextStyleClassDef = "JTT_STreeText" //
var JTTTreeNodeStyleClassDef = "JTT_STreeNode" 


var JTTSelHilightBGColor = "#4977B4"
var JTTSelHilightColor = "#FFFFD5"
var JTTOddRowStyleClass   = "JTT_RowStyleOdd"
var JTTEvenRowStyleClass  = "JTT_RowStyleEven"

var JTTCookieKey  = "JTTCookieKey"

var JTTCntRootMinusImg 	= 0;   
var JTTCntRootPlusImg 	= 1;   
var JTTCntTeePlusImg   	= 2;   
var JTTCntTeeMinusImg   	= 3;   
var JTTCntTeeImg   	= 4;   
var JTTCntRootImg   	= 5;   
var JTTCntBlankImg	= 6;   
var JTTCntDefLeafImg	= 7;   
var JTTCntDefFoldOpenImg	= 8;   
var JTTCntDefFoldCloseImg= 9;   
var JTTCntBottomPlusImg	= 10;  
var JTTCntBottomMinusImg	= 11;  
var JTTCntBottomBarImg	= 12;  
var JTTCntBarImg		= 13;  
var JTTCntRootTeeImg	= 14;  
var JTTCntOnlyPlusImg	= 15; 
var JTTCntOnlyMinusImg	= 16; 
//end of define the const index for img..

////////////define JTTIconListClass for saving Icons.
function JTTIconListClass(aBaseIconDir) { //save icons.
  this.items       	= new Array();
  this.addIcon  	= JTTaddIcon;
  this.findIconIndex= JTTFindIconIndex;
  this.getIcon		= JTTGetIconByIndex;
  this.ImgBaseDir   = aBaseIconDir ? aBaseIconDir :"";  
  
  this.addIcon("menu_root_minus.gif");  // 		JTTCntRootMinusImg 	= 0;
  this.addIcon("menu_root_plus.gif");   // 		JTTCntRootPlusImg 	= 1;
  this.addIcon("menu_tee_plus.gif");    //|- and plus     JTTCntTeePlusImg   	= 2;
  this.addIcon("menu_tee_minus.gif");   //|- and minus 	JTTCntTeeMinusImg   	= 3;
  this.addIcon("menu_tee.gif");         //|-              JTTCntTeeImg   		= 4;
  this.addIcon("menu_root.gif");        //Root img        JTTCntRootImg   		= 5;
  this.addIcon("menu_pixel.gif");       //blank           JTTCntBlankImg		= 6;
  this.addIcon("menu_link_default.gif");//		JTTCntDefLeafImg		= 7;
  this.addIcon("menu_folder_open.gif"); //		JTTCntDefFoldOpenImg	= 8;
  this.addIcon("menu_folder_closed.gif");//		JTTCntDefFoldCloseImg	= 9;
  this.addIcon("menu_corner_plus.gif");  //|_ +plus	JTTCntBottomPlusImg	= 10;
  this.addIcon("menu_corner_minus.gif");//|_ +minus	JTTCntBottomMinusImg	= 11;
  this.addIcon("menu_corner.gif");      //|_              JTTCntBottomBarImg	= 12;
  this.addIcon("menu_bar.gif");         // |		JTTCntBarImg		= 13;
  this.addIcon("menu_root_tee.gif");    // |r		JTTCntRootTeeImg		= 14; 
  this.addIcon("menu_only_plus.gif");    // +		JTTCntOnlyPlusImg	= 15; 
  this.addIcon("menu_only_minus.gif");    // -		JTTCntOnlyMinusImg	= 16; 
  
}

//for JTTIconListClass
function JTTaddIcon(item) {
  var i = this.items.length;
  this.items[i] = item;
  return i;  
}

//for JTTIconListClass
function JTTFindIconIndex(item)
{
   var i;
   for (i=0; i< this.items.length; i++)
   {
   	if (this.items[i] == item ) return i;
   }
   return -1;
}

//for JTTIconListClass
function JTTGetIconByIndex(aIndex)
{
   if(aIndex >= 0 || aIndex < this.items.length ) return this.items[aIndex];
   else return "";

}

/////////define JTTreeNodeClass for save TreeNode
function JTTreeNodeClass(id,fid,textArr,aActionJS, expanded
                           , tooltip, icon,aCellStyleArr,others,box) 
{
  
  this.id 	    = id;	
  this.fid  	= fid
  this.TextArray= textArr
  this.ActionJS = aActionJS;

  this.expanded = (expanded != null) ? expanded : false;  
  this.tooltip 	= tooltip !=null ? tooltip :"";
  this.icon    	= icon != null ? icon : "";
  this.CellStyleArr = aCellStyleArr
  this.others   = others
  
  /*checkbox:*/
  this.box      = box;
  
  this.mFiltered = false;// the node has been filtered
  
  this.subtree     = null; //JTableTreeDataClass
  this.MakeSubTree = JTTMakeSubTree; //function..
  
  /**imgBars**/
  this.imgBars = "";
  
  /**hasLazyLoaded**/
  this.hasLazyLoaded = false;
  
  /**isLast**/
  this.isLast = false;
}

//for JTTreeNodeClass
function JTTMakeSubTree(tree, isExpanded) 
{
  this.subtree 		= tree;
  this.expanded 	= (isExpanded != null) ? isExpanded : this.expanded;
}

//////define JTableTreeDataClass
function JTableTreeDataClass(aBaseIconDir, aNeedCookie,cookieKey) 
{
  this.items = new Array();
  this.icons = new JTTIconListClass(aBaseIconDir)	 //JTIconListClass,add some icon to to...  
  
  this.TableStyleClass = JTTTableStyleClassDef
  this.NeedCookie      = aNeedCookie != null ? aNeedCookie : false
  this.CookieKey      = cookieKey != null ? cookieKey : "";
  this.TableDefine     = JTTTableDefineDef
  
  this.HeadCellStyleClassArr = null //default all is JTT_HeadCell
  this.HeadStyleClass = null
  this.HeadCellTextArr= null
  this.HeadCellWidthArr= null
  this.HtmlBeforeHead = null //default = null
  this.HtmlAfterHead  = null //default = null
  
  this.HtmlAtTail     = null      //default = null

  this.addTreeNode = JTTaddTreeNodeDetail;
  this.addNode = JTTAddTreeNode;
  this.addHead = JTTAddHeadHTML;
  this.addTail = JTTAddTailHTML;
}

//for JTableTreeDataClass
function JTTAddHeadHTML(aCellTxtArr, aCellStyleArr, aBeforeHeadHtml, 
                        aAfterHeadHtml, aHeadStyle)
{
  this.HeadCellTextArr = aCellTxtArr
  this.HeadCellStyleClassArr = aCellStyleArr //default all is JTT_HeadCell
  this.HtmlBeforeHead = aBeforeHeadHtml 
  this.HtmlAfterHead  = aAfterHeadHtml  
  this.HeadStyleClass = aHeadStyle      != null ? aHeadStyle      : JTTHeadStyleClassDef
}

//for JTableTreeDataClass
function JTTAddTailHTML(ahtml)
{
   this.HtmlAtTail = ahtml ;
}


//for JTableTreeDataClass
function JTTaddTreeNodeDetail(id,fid,textArr,aActionJS, expanded
                              , tooltip, icon, aCellStyleArr,others,box) 
{
  	 var expand = fJTTGetNodeCookieExpanded(this,id,expanded);
  	 this.items[this.items.length]
             = new JTTreeNodeClass(id,fid,textArr,aActionJS, expand, 
                                   tooltip, icon,aCellStyleArr,others,box);                               
}

function fJTTGetNodeCookieExpanded(aThis, id,defaultExpanded){
	if(aThis.NeedCookie){
		var cookieValue = getCookie(JTTCookieKey+aThis.CookieKey+id);
  		//alert(id+"="+cookieValue);
		if(cookieValue!=null){
			if(cookieValue=="true"){
				return true;
			}else{
				return false;
			}
		}
	}
	return defaultExpanded;
}
//for JTableTreeDataClass
function JTTAddTreeNode(item) {
  this.items[this.items.length] = item;
}

///////defien JTableTreeClass the main class..
function JTableTreeClass(aTreeName, aDivObj, aTreeNotes,aShowRoot,aHilight
                         ,aNeedIndex,aBaseIndex,aNeedCheckbox,aCheckBoxn,aSelectCheckn,aNeedLazy )
{
   this.mMenu		= null; 
   this.mObj 		= aDivObj;
   this.mNodes 		= aTreeNotes;	//JTableTreeDataClass 
   this.mNeedIndex      = aNeedIndex == null ? true : aNeedIndex ;
   this.mShowRoot       = aShowRoot == null ? false : aShowRoot;
   this.mSelectHilight  = aHilight == null ? true : aHilight ;
   this.mBaseIndex      = aBaseIndex == null ? 1 : aBaseIndex;
   
   this.mSelected	= null; //the id...
   this.mClickNode   = null  //the JTTreeNodeClass
   this.mTreeRoot	= new JTTreeNodeClass(0,0,null);

   this.OnClick         = JTTOnClick
   this.OnMenuShow         = JTTOnMenuShow
   this.ClickPlusMinus  = JTTClickPlusMinus;
   this.ClickPlusMinusByNode  = JTTClickPlusMinusByNode;
   this.SelectNode   = JTSelectNodeById //
   this.GetTreeNode    	= JTTGetTreeNode;      
   this.RebuildTree	= JTTRebuildTree; //rebuild all .....   
	
   this.FilterTree    	= JTTFilterTree;   // filter tree node
   this.ClearFilter    	= JTTClearFilterTree; // clear filter action
   this.mFilter    	= false;// is the tree filtered?  
   this.mFilterScript = null;// the script for fitlering the tree

   this.SetMenu         = JTTSetMenu;
   
   this.AddNode          = JTTAddNode
   this.DelNode          = JTTDelNodeById
///////   this.SetProperty      = JTTSetPropertyByIndex
//   this.GetNodeIndex     = JTTGetNodeIndexById

/*private:*/
   this.mTable		= null;        //using for build the table
   this.mBars		= new Array(); //using for build the table 
   this.mBarLevel	= 0;           //using for build the table
   this.mIndex          = 0;           //using for build the table, to set the bkground color.
   this.mOldBGColor     = null; //use for hilight a line..
   this.mOldColor       = null; //use for hilight a line..
   this.AddBarItem	= JTTAddBarItem;
   this.DelBarItem	= JTTDelBarItem;
   
   /*checkbox:*/
   this.aNeedCheckbox  = aNeedCheckbox == null? false:aNeedCheckbox;
   this.aCheckBoxn     = aCheckBoxn == null? "rightCheckBox" : aCheckBoxn;
   this.aSelectCheckn  = aSelectCheckn == null? "gotoSelectCheck" : aSelectCheckn;
   
   /*treeName:*/
   this.aTreeName = aTreeName;
   
   /*aNeedLazy*/
   this.aNeedLazy = aNeedLazy == null? false:aNeedLazy;
   
   /*lazy child nodes flag*/
   this.childNodes = new Array();
  
}

//for JTableTreeClass
//初始化根节点的子树节点信息
function fJTTInitTreeData(aThis)
{
	var i
	var fid;
	var lNode;
	
	var oNode;
	
	aThis.mSelected     = null
	aThis.mClickNode    = null
	aThis.mTable		= null;
	aThis.mBars.length  = 0; 
	aThis.mBarLevel		= 0; 
	aThis.mIndex         = 0; 
	aThis.mOldBGColor    = null;
	aThis.mOldColor      = null;
	aThis.mTreeRoot.subtree = null;

	//aThis.mTreeRoot.icon = JTTDoIcons(aThis.mNodes.icons, aThis.mTreeRoot.icon );

	/**lazy edit  
	for(i =0; i<aThis.mNodes.items.length; i++)
	{
		aThis.mNodes.items[i].subtree = null;
		aThis.mNodes.items[i].icon = JTTDoIcons(aThis.mNodes.icons, aThis.mNodes.items[i].icon);
	}
	**/
	
	oNode = aThis.mTreeRoot;
	
	oNode.MakeSubTree( new JTableTreeDataClass());
  
	for(i =0;i<aThis.mNodes.items.length;i++){
		fid = aThis.mNodes.items[i].fid;
		aThis.mNodes.items[i].icon = JTTDoIcons(aThis.mNodes.icons, aThis.mNodes.items[i].icon);
		aThis.mNodes.items[i].MakeSubTree(new JTableTreeDataClass());
		/**lazy edit**/
		if(aThis.aNeedLazy){
			/**reset the node expand status**/
			aThis.mNodes.items[i].expanded = false;		
			id = aThis.mNodes.items[i].id;
			//保存每个父级节点的最后一个子级节点
			aThis.childNodes[fid] = id;
		}
		
		if (fid == 0){
			//构造当前节点的子树节点
			oNode.subtree.addNode(aThis.mNodes.items[i]);
			continue;
		}
		
		lNode = fJTTFindTreeNode(fid, aThis.mNodes);
		//如果遍历节点的父节点存在，则将遍历节点加入父结点子树，否则加入根结点子树
		if(lNode !=null){
			if(lNode.subtree == null){
				lNode.MakeSubTree(new JTableTreeDataClass());
			}
			lNode.subtree.addNode(aThis.mNodes.items[i]);
			lNode.hasLazyLoaded = true;
		}else{
			aThis.mNodes.items[i].fid = 0;
			aThis.mTreeRoot.MakeSubTree(aThis.mNodes.items[i]);
		}
	}
}	

function fJTTBuildRootNode(aThis, aNode, aIsLast )
{
	//aNode.expanded = fJTTGetNodeCookieExpanded(aThis, aNode);
	//TR的id值为aThis.mObj.id_aNode.id
	var lRow = '<tr class="' 
   	    + ( aThis.mIndex % 2 == 0 ? JTTEvenRowStyleClass : JTTOddRowStyleClass )
   	    +  '" id="'
   	    + aThis.mObj.id + '_' + aNode.id + '" '
   	    + '>'
   	    + ( aThis.mNeedIndex 
	                 ? '<td nowrap class="' + JTTIndexRowStyleClass + '"><NOBR>' 
	                                 + (aThis.mBaseIndex + aThis.mIndex) 
	                                 + '</td></NOBR>'
	           
	                 :"")
   
	aThis.mIndex ++   
   	
   	aNode.isLast = aIsLast;
   	
	var i;
	var lhtml = ""
	for(i = 0; i < aNode.TextArray.length; i ++)
	{
		//构造表格第一个td内容，主要是要构造图片和选择框等
		if( i == 0)
		{	
			//第一格TD的id值为row + aThis.mObj.id_aNode.id
			lhtml +='<td nowrap class="' 
			      + (aNode.CellStyleArr == null ? JTTRowCellStyleClassDef 
			                                      : aNode.CellStyleArr[i])
			      + '" id="row' + aThis.mObj.id + "_" + aNode.id
			      + '"><NOBR>'		

			var lNodeImg = null
			//子树不为空，即已构造空子树
			if(aNode.subtree !=null)
			{
				//视节点的打开情况和是否是最后一个节点的情况来决定显示的图片
				if(aNode.expanded) 	
					limg = aIsLast ? JTTCntOnlyMinusImg : JTTCntRootMinusImg;
				else 			
					limg = aIsLast ? JTTCntOnlyPlusImg : JTTCntRootPlusImg;
				//构造树形开关图片，图片Id为img + aThis.mObj.id
				lhtml +=fJTTBuildImg(aThis, limg, true );
				//文件夹图标
				lNodeImg = (aNode.icon != null ? aNode.icon : JTTCntDefFoldCloseImg);
			//子树为空，即尚未构造空子树	
			}else{
				if(! aIsLast){   
					lhtml +=fJTTBuildImg(aThis, JTTCntRootTeeImg );
				}
				lNodeImg = (aNode.icon ? aNode.icon : JTTCntDefLeafImg);   	
			}
		    lhtml += fJTTBuildNodeLink(aThis,aNode,lNodeImg) + '</NOBR></td>';
			continue;	
		}
		//构造除第一格外的其他表格的信息
		lhtml +='<td nowrap class="' 
		      + (aNode.CellStyleArr == null ? JTTRowCellStyleClassDef 
		                                      : aNode.CellStyleArr[i])
		      + '" ><NOBR>'
		      + ( typeof(aNode.TextArray[i]) == "string" &&  aNode.TextArray[i] =="" ? 
		          "&nbsp;" : aNode.TextArray[i])
		      + '</NOBR></td>'
	}//	end for(i = 0; i < aNode.TextArray.length; i ++)

    aThis.mTable += lRow + lhtml + '</tr>'
    
	//保存该节点需要缩进的空间
	aNode.imgBars = aThis.mBars.join(",");
	
	/**lazy edit**/
	//如果不需要延迟加载
	//if(!aThis.aNeedLazy){
	if(aNode.subtree != null){
		//添加缩进空间，记录当前是在树的第几级节点
		aThis.AddBarItem(aIsLast ? JTTCntBlankImg : JTTCntBarImg);
		//构造aNode子树的节点
		for( i = 0; i< aNode.subtree.items.length; i++){
			fJTTBuildFromNode(aThis, aNode.subtree.items[i], 
			   	                 (i== aNode.subtree.items.length - 1 )? true : false,
			   	                 aNode.expanded);			
		}
		aThis.DelBarItem();
	}
	//}
}

function fJTTBuildFromNode(aThis, aNode, aIsLast, aDisplay)
{
	//aNode.expanded = fJTTGetNodeCookieExpanded(aThis, aNode);
	var lRow  = '<tr nowrap class="' 
	           + ( aThis.mIndex % 2 == 0 ? JTTEvenRowStyleClass : JTTOddRowStyleClass )
	           + '" id="' 
	           + aThis.mObj.id + '_' + aNode.id + '" '
	           + (aDisplay ?  ' style="display:block" ' : ' style="display:none" ')
	           + '>'
	           + ( aThis.mNeedIndex 
	                 ? '<td nowrap class="' + JTTIndexRowStyleClass + '"><NOBR>' 
	                                 + (aThis.mBaseIndex + aThis.mIndex) 
	                                 + '</NOBR></td>'
	           
	                 :"")
	
	aNode.isLast = aIsLast;
	
	aThis.mIndex ++
	var i;
	var lhtml = ""
	for(i = 0; i < aNode.TextArray.length; i ++)
	{
		if( i == 0)
		{
			lhtml +='<td nowrap class="' 
			      + (aNode.CellStyleArr == null ? JTTRowCellStyleClassDef 
			                                      : aNode.CellStyleArr[i])
			      + '" id="row' + aThis.mObj.id + "_" + aNode.id
			      + '"><NOBR>'		
			for(var j=0; j< aThis.mBarLevel; j++)
			{
				lhtml +=fJTTBuildImg(aThis, aThis.mBars[j]);
			}

			var lNodeImg = null
			if(aNode.subtree !=null)
			{
				if(aNode.expanded) 	
					limg = aIsLast ? JTTCntBottomMinusImg : JTTCntTeeMinusImg;
				else 			
					limg = aIsLast ? JTTCntBottomPlusImg : JTTCntTeePlusImg;
				//构造树形开关图片，图片Id为img + aThis.mObj.id
				lhtml +=fJTTBuildImg(aThis, limg, true );
				
				lNodeImg = (aNode.icon != null ? aNode.icon : JTTCntDefFoldCloseImg);
				
			}else{
				lhtml +=fJTTBuildImg(aThis, aIsLast ? JTTCntBottomBarImg : JTTCntTeeImg );
			   	
				lNodeImg = (aNode.icon != null ? aNode.icon : JTTCntDefLeafImg);   	
			}
   			
		   lhtml += fJTTBuildNodeLink(aThis,aNode,lNodeImg) + '</NOBR></td>';
			continue;	
		}
		
		lhtml +='<td nowrap class="' 
		      + (aNode.CellStyleArr == null ? JTTRowCellStyleClassDef 
		                                      : aNode.CellStyleArr[i])
		      + '" ><NOBR>'
		      + ( typeof(aNode.TextArray[i]) == "string" &&  aNode.TextArray[i] =="" ? 
		          "&nbsp;" : aNode.TextArray[i])
		      + '</NOBR></td>'
	}//	end for(i = 0; i < aNode.TextArray.length; i ++)

    aThis.mTable += lRow + lhtml + '</tr>'
    
	//保存该节点需要缩进的空间
	aNode.imgBars = aThis.mBars.join(",");
	
	if(aNode.subtree != null)
	{
		aThis.AddBarItem(aIsLast ? JTTCntBlankImg : JTTCntBarImg);
		   
		for( i = 0; i< aNode.subtree.items.length; i++)
		{
		   	fJTTBuildFromNode(aThis, aNode.subtree.items[i], 
		   	                 (i== aNode.subtree.items.length - 1 )? true : false,
		   	                 aNode.expanded&&aDisplay);		
		   	            	
		}
		aThis.DelBarItem();
	}
}


//for JTableTreeClass
function JTTRebuildTree( )
{
   //edit by yubin
//   fJTTInitTreeData(this,this.mNodes,this.mTreeRoot);
   //构造以根节点开始的第一级树
   fJTTInitTreeData(this);
   var ltable = this.mNodes;   
   var i;
   var lhtml = "";

	//build the head...      
   this.mTable = '<TABLE class="' + ltable.TableStyleClass + '" ' 
                 + ltable.TableDefine
                 + ' id="table' + this.mObj.id +'">'
				 + (ltable.HtmlBeforeHead == null ? "" : ltable.HtmlBeforeHead )
	//alert("table" + this.mObj.id );
   //build the real header...
   //构造表格标题				 
	if(ltable.HeadCellTextArr != null)
	{
		lhtml = '<tr nowrap class="' + ltable.HeadStyleClass + '" id="JTT_Header"> '
		if(this.mNeedIndex)
		{
			lhtml += '<td nowrap class="' + JTTIndexHeadStyleClass + '">' 
			      + JTTIndexHeadText + '</td>'
			          
		}
		
		for(i = 0; i < ltable.HeadCellTextArr.length; i ++)
		{
			lhtml +='<td';
			if(ltable.HeadCellWidthArr!=null && i< ltable.HeadCellWidthArr.length){
				lhtml +=' width="'+ltable.HeadCellWidthArr[i]+'"';
			}
			lhtml +=' nowrap  class="' 
			      + (ltable.HeadCellStyleClassArr == null ? 
			                     JTTHeadCellStyleClassDef : 
			                     ltable.HeadCellStyleClassArr[i])
			      + '">' + ltable.HeadCellTextArr[i] + '</td>'
		}
		this.mTable += lhtml + '</tr>';
	}				 
	this.mTable += (ltable.HtmlAfterHead == null ? "" : ltable.HtmlAfterHead);

	//build the rows...
	//构造根节点的第一个子节点，跟其他子节点相比主要是图片不同
    if(this.mTreeRoot.subtree.items.length > 1){
    	
       fJTTBuildRootNode(this, this.mTreeRoot.subtree.items[0],false);

    }
    else
    {   if (this.mTreeRoot.subtree.items.length ==1 ){

            fJTTBuildRootNode(this, this.mTreeRoot.subtree.items[0],true);
        }
        else{
        //	Error ...
        	return;
        }
    }
    //构造根节点的其他子节点
    for(i =1 ;i < this.mTreeRoot.subtree.items.length ; i++)
    {
        fJTTBuildFromNode(this,this.mTreeRoot.subtree.items[i]
                         , (i ==this.mTreeRoot.subtree.items.length -1) ? true:false, true);
    }
   
   if(this.mNodes.HtmlAtTail != null) this.mTable += this.mNodes.HtmlAtTail;
 
   this.mObj.innerHTML = this.mTable + '</TABLE>';
}

//for JTableTreeClass
function JTTGetTreeNode(aNodeID)
{
   var i;
   if(aNodeID == 0) return this.mTreeRoot;
   for (i=0; i< this.mNodes.items.length; i++)
   {
   	if( this.mNodes.items[i].id == aNodeID) return this.mNodes.items[i];
   }
   return null;
}

//for JTableTreeClass
function JTTAddBarItem(img)
{
   this.mBars[this.mBarLevel] = img;
   this.mBarLevel ++;
}

//for JTableTreeClass
function JTTDelBarItem()
{
   this.mBarLevel --;
   if(this.mBarLevel <0) this.mBarLevel =0;
   this.mBars[this.mBarLevel] = null;
}

//////public function.......
function fJTTFindTreeNode(aId, aTree)
{
   var j;
   for(j=0;j<aTree.items.length; j++)
   {
      if(aId == aTree.items[j].id)
      {
      	return aTree.items[j];
      }
   }
   return null;
}

function fJTTBuildImg(aThis, aImgUrl, aPlusMinus, aAlt)
{
   var lsrc = aThis.mNodes.icons.getIcon(aImgUrl);
   if (lsrc == "" ) return " ";
   //属性开关图片的id为img + aThis.mObj.id
   //文件夹图片则无此属性
   var lt = aPlusMinus ? "id= 'img" + aThis.mObj.id +"'": "";
   var lAlt = aAlt != null ? "TITLE='" + aAlt +"' " : "  ";//
   
   var limg 	= '<IMG class="' 
                + (aPlusMinus ? JTTPlusMinusStyleClassDef : JTTIconStyleClassDef ) 
                + '" '
//                +'  '
				+ 'src="' 
				+ aThis.mNodes.icons.ImgBaseDir + lsrc
				+ '" ' +  lt + ' '+ lAlt + ' style="CURSOR:hand">';
                  
   return limg;
}

function fJTTGetCounterpartImgId(aThis, aLastImg)
{//
  var licons = aThis.mNodes.icons.items;
  var limg;

  if(aLastImg.lastIndexOf(licons[JTTCntTeePlusImg]) >=0) limg = licons[JTTCntTeeMinusImg];  
  else if(aLastImg.lastIndexOf(licons[JTTCntTeeMinusImg]) >=0) limg = licons[JTTCntTeePlusImg];  

  else if(aLastImg.lastIndexOf(licons[JTTCntRootMinusImg]) >=0) limg = licons[JTTCntRootPlusImg];
  else if(aLastImg.lastIndexOf(licons[JTTCntRootPlusImg]) >=0) limg = licons[JTTCntRootMinusImg];
 
  else if(aLastImg.lastIndexOf(licons[JTTCntOnlyPlusImg]) >=0) limg = licons[JTTCntOnlyMinusImg];  
  else if(aLastImg.lastIndexOf(licons[JTTCntOnlyMinusImg]) >=0) limg = licons[JTTCntOnlyPlusImg];  
  
  else if(aLastImg.lastIndexOf(licons[JTTCntBottomMinusImg]) >=0) limg = licons[JTTCntBottomPlusImg];  
  else if(aLastImg.lastIndexOf(licons[JTTCntBottomPlusImg])  >=0) limg = licons[JTTCntBottomMinusImg];  
  else return aLastImg;

  if(aThis.mNodes.icons.ImgBaseDir )
  	return aThis.mNodes.icons.ImgBaseDir  + limg;
  else return limg;
}


function fJTTBuildText(aThis, aNode, aText , aAlt)
{
	//alert(aText);
   var lAlt = aAlt != null ? "TITLE='" + aAlt +"'" : ""; 
   //<span>元素增加id属性，为了在延迟加载时改变checkbox的延迟加载标识方便 
   var ltext 	= '<span nowrap id="span2' + aThis.mObj.id + '_' + aNode.id +  '" class="'
                  + JTTTreeTextStyleClassDef + '" '
                  + lAlt + '>';
                  
   /*checkbox:*/
   //需要展现复选框
   if(aThis.aNeedCheckbox){
   	//复选框(默认选中)，名称为设置名称，id为mTree_aThis.mObj.id_aNode.id，单击事件为设置函数(当前选中状态,当前节点值,树)，复选框value为aNode.id
    if(aNode.box=='1'){
		ltext += '<input type="checkbox" style="width:14;height:14;padding:0;margin:0;clip: rect(auto auto auto auto)" name="'+aThis.aCheckBoxn+'" id="mTree_' + aThis.mObj.id + '_' + aNode.id+'" checked onClick="'+aThis.aSelectCheckn+'(this.checked,this.value,' + aThis.aTreeName + ')" value="'+aNode.id+'"';
	//不需要任何选择框
	}else if(aNode.box=='2'){
	//复选框，但不能选择，禁用	
	}else if(aNode.box=='-1'){
		ltext += '<input type="checkbox" style="width:14;height:14;padding:0;margin:0;clip: rect(auto auto auto auto)" name="'+aThis.aCheckBoxn+'" id="mTree_' + aThis.mObj.id + '_' + aNode.id+'" checked value="'+aNode.id+'" disabled="disabled"';
	//单选框
	}else if(aNode.box=='3'){
		ltext += '<input type="radio" style="width:14;height:14;padding:0;margin:0;clip: rect(auto auto auto auto)" name="'+aThis.aCheckBoxn+'" id="mTree_' + aThis.mObj.id + '_' + aNode.id+'" onClick="'+aThis.aSelectCheckn+'(this.checked,this.value,' + aThis.aTreeName + ')" value="'+aNode.id+'"';
	//复选框，默认不选中
	}else if(aNode.box=='0'){
		ltext += '<input type="checkbox" style="width:14;height:14;padding:0;margin:0;clip: rect(auto auto auto auto)" name="'+aThis.aCheckBoxn+'" id="mTree_' + aThis.mObj.id + '_' + aNode.id+'" onClick="'+aThis.aSelectCheckn+'(this.checked,this.value,' + aThis.aTreeName + ')" value="'+aNode.id+'"';
	//默认方案，同上
	}else{
		ltext += '<input type="checkbox" style="width:14;height:14;padding:0;margin:0;clip: rect(auto auto auto auto)" name="'+aThis.aCheckBoxn+'" id="mTree_' + aThis.mObj.id + '_' + aNode.id+'" onClick="'+aThis.aSelectCheckn+'(this.checked,this.value,' + aThis.aTreeName + ')" value="'+aNode.id+'"';
	}
	//如果该节点已被延迟加载过了，则设置checkbox的属性为已延迟加载状态
	if(aNode.hasLazyLoaded && aNode.box != '2'){
		ltext += ' flag="hasLazyLoaded">';
	}else if(aNode.box != '2'){
		ltext += ' flag="notLazyLoaded">';
	}
   }   
   //节点文本信息
   ltext = ltext + aText +'&nbsp;</span>';
   return ltext;
}

function fJTTBuildNodeLink(aThis, aNode, aImgUrl)
{
   //span id = span_aThis.mObj.id_aNode.id
   var lhtml ='<SPAN nowrap id="span' + aThis.mObj.id + '_' + aNode.id +  '" '
             + ' class="' + JTTTreeNodeStyleClassDef + '">';
             
   var ltxt = aNode.TextArray != null ? aNode.TextArray[0] : "   ";
   
   
   
   if(aImgUrl !=""){
	//构造文件夹图片
   	lhtml += fJTTBuildImg(aThis, aImgUrl, false, aNode.tooltip ? aNode.tooltip : ltxt);
   }
   //构造节点名称
   lhtml += fJTTBuildText(aThis, aNode, ltxt , aNode.tooltip ? aNode.tooltip : ltxt ) +"</SPAN>";
   
   return lhtml;
}

function JTTDoIcons(aIcons,aNameOrIndex)
{
   var i;

   if(("" + aNameOrIndex).lastIndexOf('.') >=0)
   {
   	i = aIcons.findIconIndex(aNameOrIndex);
   	if(i >=0) return i;
   	else{
   	   return aIcons.addIcon(aNameOrIndex);
   	}
   }else{
   	i =parseInt(aNameOrIndex);
   	if( isNaN(i)) return "";
   	else{ 
   	   if(i>=0 && i < aIcons.items.length ) 
   	      return i;
   	   else return "";
   	}
   }
}




function JTTSetMenu(smenu){
	if(smenu!=null){
		this.mMenu = smenu
	}
}
function JTTOnMenuShow(){

   var e;
   var lID, i;
   var clickPlace = 0; //not init.
   var lImgDir = this.mNodes.icons.ImgBaseDir;
   var lIcons = this.mNodes.icons;
   var lSrc;
   var lFather = null , lGrandFather = null;
   e = window.event.srcElement;  
   //alert(e.tagName);
   while(true)
   {
   	switch(e.tagName)
   	{
   	case "img":
   	case "IMG":
   	   lFather = e.parentElement;
   	   	if(lFather.tagName == 'NOBR'){
   	   		lFather = lFather.parentElement;
   	   	}   	   
   	   lGrandFather = lFather ? lFather.parentElement : null;
   	   if (lFather != null && lGrandFather != null)
   	   {
   	   	if(lFather.tagName =="TD" && lGrandFather.tagName == "TR")
   	   	{
   	   	   if( e.id == "img"+ this.mObj.id ) 
   	   	   {
   	   	   	i = (lGrandFather.id).lastIndexOf("_");
   	   	   	if ( i>=0 )
   	   	   	{
   	   	   	   lID = (lGrandFather.id).substr(i+1);
   	   	   	   this.ClickPlusMinus( lID)
   	   	   	}else
   	   	   	{
   	   	   	   return ;
   	   	   	}
		   }
   	   	}else{
   	   	  if(lFather.tagName == "SPAN")
   	   	  { 	e = lFather;
   	   	  	break;
   	   	  }
   	   	}
   	   	
   	   }
   	   return;
   	case "SPAN":
   	   	lFather = e.parentElement;
   	   	if(lFather.tagName == 'NOBR'){
   	   		lFather = lFather.parentElement;
   	   	}   	   	
   	   	if(lFather == null ) return;
   	   	
   	   	if(lFather.tagName =='SPAN'){
   	   	   e = lFather;
   	   	   break;
   	   	}
   	   	
        lGrandFather = lFather.parentElement ;
        if( lGrandFather == null) return;
   	   	
   	   	if(lFather.tagName == "TD" && lGrandFather.tagName == "TR")
   	   	{

   	   	   	i = (lGrandFather.id).lastIndexOf("_");
   	   	   	if ( i>=0 )
   	   	   	{
   	   	   	   lID = (lGrandFather.id).substr(i+1);
   	   	   	   /**
   	   	   	  	* contextmenu select on node
   	   	   	  	*/
			   var lNode = this.SelectNode(lID)
			   if( lNode == null ) return null;
			   
			   this.mClickNode = lNode;   	   	   	   
//   	   	   	   JTTClickOnNode(this, lID);
   	   	   	}else
   	   	   	{
   	   	   	   return ;
   	   	   	}
   	   	   
   	   	}
		if(this.mMenu!=null){
			this.mMenu.showMenu("JTreeNode");
		}
   		return;
   		//break;
   	default:
   		return;
   	}
   }
   //lID = 
}


//for JTableTreeClass
//树节点的鼠标点击事件
function JTTOnClick()
{
   var e;
   var lID, i;
   var clickPlace = 0; //not init.
   var lImgDir = this.mNodes.icons.ImgBaseDir;
   var lIcons = this.mNodes.icons;
   var lSrc;
   var lFather = null , lGrandFather = null;
   
   e = window.event.srcElement;  
   while(true)
   {
   	switch(e.tagName)
   	{
   	case "img":
   	case "IMG":
   	   lFather = e.parentElement;
   	    //如果是<NOBR>，则继续往上级查找
   	   	if(lFather.tagName == 'NOBR'){
   	   		lFather = lFather.parentElement;
   	   	}
   	   //取祖父节点   	   
   	   lGrandFather = lFather ? lFather.parentElement : null;
   	   //如果父级和祖父节点都不为空
   	   if (lFather != null && lGrandFather != null)
   	   {
   	    //表示该图片是树形开关图片
   	   	if(lFather.tagName =="TD" && lGrandFather.tagName == "TR")
   	   	{
   	   	   if( e.id == "img"+ this.mObj.id ) 
   	   	   {
   	   	   	i = (lGrandFather.id).lastIndexOf("_");
   	   	   	if ( i>=0 )
   	   	   	{
   	   	   		//取得对应节点id
   	   	   	   lID = (lGrandFather.id).substr(i+1);
   	   	   	   //触发树形开关事件
   	   	   	   this.ClickPlusMinus( lID)
   	   	   	}else
   	   	   	{
   	   	   	   return ;
   	   	   	}
		   }
   	   	}else{
   	   	  //如果是文件夹图片，则将事件对象指向span，触发span事件
   	   	  if(lFather.tagName == "SPAN")
   	   	  { 	e = lFather;
   	   	  	break;
   	   	  }
   	   	}
   	   	
   	   }
   	   return;
   	   //break;
   	case "TD":
   	case "td":
   		return;
   		//break;
   	case "TR":
   		return;
   		//break;
   	case "SPAN":
   	   	lFather = e.parentElement;
   	   	//跳过<NOBR>元素
   	   	if(lFather.tagName == 'NOBR'){
   	   		lFather = lFather.parentElement;
   	   	}
   	   	if(lFather == null ) return;
   	   	//如果是节点文本的<SPAN>元素，则替换到包含文件夹图标的<SPAN>
   	   	if(lFather.tagName =='SPAN'){
   	   	   e = lFather;
   	   	   break;
   	   	}
        lGrandFather = lFather.parentElement ;
        if( lGrandFather == null) return;
   	   	//最终取到最外层<SPAN>
   	   	if(lFather.tagName == "TD" && lGrandFather.tagName == "TR")
   	   	{

   	   	   	i = (lGrandFather.id).lastIndexOf("_");
   	   	   	if ( i>=0 )
   	   	   	{
   	   	   	   //取到事件触发节点id
   	   	   	   lID = (lGrandFather.id).substr(i+1);
   	   	   	   //触发节点点击事件
   	   	   	   JTTClickOnNode(this, lID);
   	   	   	}else
   	   	   	{
   	   	   	   return ;
   	   	   	}
   	   	   
   	   	}
   		return;
   		//break;
   	default:
   		return;
   	}
   }
   //lID = 
   
}
//for JTableTreeClass
function JTTFilterTree(filterScript){
	//alert(filterScript);
	this.mFilter = true;
	this.mFilterScript = filterScript;
	if(this.mNodes.items.length>0){
    	this.ClickPlusMinusByNode(this.mNodes.items[0],true);
	}
}
//for JTableTreeClass
function JTTClearFilterTree(){
	this.mFilter = false;
	this.mFilterScript = null;
	if(this.mNodes.items.length>0){
    	this.ClickPlusMinusByNode(this.mNodes.items[0],true);
	}
	
	
}

//for JTableTreeClass
//点击开关树形结构事件
function JTTClickPlusMinus(aNodeID)
{
   var lNode = this.GetTreeNode(aNodeID);
   this.ClickPlusMinusByNode(lNode,false);
   /*
   if(lNode)
   {
   	lNode.expanded = lNode.expanded ? false : true;
   	//this.RebuildTree();
   	this.mTable.rows(this.mObj.id+"_"+lNode.id).style.display = lNode.expanded ? block : none;
   }*/

}

//点击开关树形结构事件
function JTTClickPlusMinusByNode(aNode,isFilter)
{
   var limg;
   if(aNode != null)
   {
	    if(!isFilter){
	    	//节点开关状态变换
	   		aNode.expanded = aNode.expanded ? false : true;
	   		//取得节点开关状态图片
		   	limg = this.mObj.children(0)
		   	                .rows(this.mObj.id +"_" + aNode.id)
		   	                .cells("row"+this.mObj.id+"_"+aNode.id)
		   	                .children(0)
		   	                .children("img"+this.mObj.id);
		
		   	limg.src = fJTTGetCounterpartImgId(this, limg.src);
		   	//set the expand status to cookie
		   	fJTTSetExpandCookie(this,aNode);
	   	}
	   	//edit by yubin
	   	//如果树需要延迟加载且该节点尚未加载则加载该节点的子树
	   	if(this.aNeedLazy && !aNode.hasLazyLoaded){
	   		JTTLazyExpandOrClose(this,aNode,aNode.expanded,isFilter);
	   	//执行正常的树性开关操作
	   	}else{
	   		JTTExpandOrClose(this,aNode,aNode.expanded,isFilter);
	   	}
	   	//for( i =0; i< aNode.subtree.items.length ; i++){
	   	       //this.mObj.children(0).rows(this.mObj.id+"_"
	   	         //                         +aNode.subtree.items[i].id).style.display 
	   	           //            = aNode.expanded ? "block" : "none";
	   	//}
   }
   /* remove by Jerry Tang
   if(this.mNeedIndex)
   {
   		var i;
   		var lIndex = 0;
   		var lNode  = null;
   		for(i = 0; i < this. mObj.children(0).rows.length; i ++)
   		{
			lNode = this. mObj.children(0).rows(i);
   			if( lNode.style.display !="none" && ("" + lNode.id).indexOf(this.mObj.id +"_") >= 0)
   			{
 				lNode.className =  (lIndex % 2 == 0) ? 
   				                    JTTEvenRowStyleClass : 
  				                    JTTOddRowStyleClass
   				lNode.cells(0).innerText = "" + (lIndex + this.mBaseIndex)
   				lIndex ++;
   			}
   		}
   }
   */
   //设置当前节点为选中节点
   this.mClickNode = aNode;
// due to .htc file   
//   if(this.mObj.onclickplusminus != null)
//  {
//		this.mObj.onclickplusminus();
//   }

}
function fJTTSetExpandCookie(aThis,aNode){
   		if(aThis.mNodes.NeedCookie){
       		var dd = new Date();
       		dd.setHours(23,59,59);
       		//alert(JTTCookieKey+aThis.mNodes.CookieKey+aNode.id+"="+aNode.expanded);
   			setCookie(JTTCookieKey+aThis.mNodes.CookieKey+aNode.id,aNode.expanded,dd)
   		}
}

function JTTExpandOrClose(aThis, aBegin, aExpandOrClose,isFilter)
{
   var i ;
   if(aBegin.subtree){
	   for(i =0; i<aBegin.subtree.items.length; i++)
	   {
	   		var nowNode = aBegin.subtree.items[i];
		    var nowDisplay = aExpandOrClose;
		    if(isFilter){
		    	if(!nowNode.subtree){
		    		//alert(aThis.mFilter);
		    		if(aThis.mFilter){
		    			nowNode.mFiltered = eval(aThis.mFilterScript+"(nowNode)");
		    			nowDisplay = nowDisplay && !nowNode.mFiltered;
		    		}else{
		    			nowNode.mFiltered = false;
		    		}
		    	}
		    }else{
		    	if(!nowNode.subtree && aThis.mFilter){
		    		nowDisplay = nowDisplay && !nowNode.mFiltered;
		    	}
		    }
		   	aThis.mObj.children(0)
		   	     .rows(aThis.mObj.id + "_" + nowNode.id)
		   	     .style.display = nowDisplay ? "block" : "none";
	
			aThis.mClickNode = nowNode;
			if(!isFilter){
// due to .htc file			
//				if(aExpandOrClose && aThis.mObj.onshownode != null)
//		   		{
//					aThis.mObj.onshownode();
//		   		}else if(aThis.mObj.onhidenode != null){
//					aThis.mObj.onhidenode();
//		   		}
	   		}
	   		
		   if(isFilter){
		   		JTTExpandOrClose(aThis,nowNode,aExpandOrClose&&nowNode.expanded,isFilter);
		   }else if(nowNode.expanded)
		   		JTTExpandOrClose(aThis,nowNode,aExpandOrClose,isFilter);
	   }
   }
}

//edit by yubin
//延迟加载生成树形节点
function fJTTLazyBuildFromNode(aThis, fNode, aNode, aIsLast, aDisplay, rowIndex)
{
	aNode.isLast = aIsLast;
	
	aThis.mIndex ++;
	var i;
	var lhtml = "";
	//新建<TR>
	var newNode = aThis.mObj.children(0).insertRow(rowIndex + 1);
	/**
	 * set tr attribute
	 */
	newNode.className = '' + (aThis.mIndex % 2 == 0 ? JTTEvenRowStyleClass : JTTOddRowStyleClass);
	newNode.id = aThis.mObj.id + '_' + aNode.id;
	newNode.style.display = aDisplay ?  'block' : 'none';
	
	if(aThis.mNeedIndex){
		var indexTd = newNode.insertCell(-1);
		indexTd.className = JTTIndexRowStyleClass;
		indexTd.innerHTML = "<NOBR>" + aThis.mBaseIndex + aThis.mIndex + "</NOBR>";
	}
	
	for(i = 0; i < aNode.TextArray.length; i ++){
		if( i == 0){
			//新建第一个<TD>
			var indexTd = newNode.insertCell(-1);
			indexTd.className = (aNode.CellStyleArr == null ? JTTRowCellStyleClassDef 
			                                      : aNode.CellStyleArr[i]);
			indexTd.id = 'row' + aThis.mObj.id + "_" + aNode.id;
			//根据树形结构构造前面的图片
			for(var j=0; fNode.imgBars != "" && j< fNode.imgBars.split(",").length; j++)
			{
				lhtml +=fJTTBuildImg(aThis, fNode.imgBars.split(",")[j]);
			}
			//构造开关图片前的最后一个图片
			//若父节点不是最后一个节点，则图片为|
			if(!fNode.isLast){
				lhtml += fJTTBuildImg(aThis, JTTCntBarImg);
				//aNode.imgBars = fNode.imgBars == "" ? fNode.imgBars + JTTCntBarImg + "," + JTTCntBlankImg : fNode.imgBars + "," + JTTCntBarImg + "," + JTTCntBlankImg;
				aNode.imgBars = fNode.imgBars == "" ? fNode.imgBars + JTTCntBarImg : fNode.imgBars + "," + JTTCntBarImg;
			//若父节点是最后一个节点，则图片为空
			}else{
				lhtml += fJTTBuildImg(aThis, JTTCntBlankImg);
				//aNode.imgBars = fNode.imgBars == "" ? fNode.imgBars + JTTCntBlankImg + "," + JTTCntBlankImg : fNode.imgBars + "," + JTTCntBlankImg + "," + JTTCntBlankImg;
				aNode.imgBars = fNode.imgBars == "" ? fNode.imgBars + JTTCntBlankImg : fNode.imgBars + "," + JTTCntBlankImg;
			}

			var lNodeImg = null;
			if(aNode.subtree !=null)
			{
				if(aNode.expanded) 	
					limg = aIsLast ? JTTCntBottomMinusImg : JTTCntTeeMinusImg;
				else 			
					limg = aIsLast ? JTTCntBottomPlusImg : JTTCntTeePlusImg;
				
				lhtml +=fJTTBuildImg(aThis, limg, true );
				
				lNodeImg = (aNode.icon != null ? aNode.icon : JTTCntDefFoldCloseImg);
				
			}else{
				lhtml +=fJTTBuildImg(aThis, aIsLast ? JTTCntBottomBarImg : JTTCntTeeImg );
			   	
				lNodeImg = (aNode.icon != null ? aNode.icon : JTTCntDefLeafImg);   	
			}
   			
		   lhtml += fJTTBuildNodeLink(aThis,aNode,lNodeImg);
		   
		   			
		   indexTd.innerHTML = "<NOBR>" + lhtml + "</NOBR>";
		   continue;	
		}
		
		var indexTd = newNode.insertCell(-1);
		indexTd.className = (aNode.CellStyleArr == null ? JTTRowCellStyleClassDef 
		                                      : aNode.CellStyleArr[i]);
		indexTd.innerHTML = "<NOBR>" + ( typeof(aNode.TextArray[i]) == "string" &&  aNode.TextArray[i] =="" ? 
		          "&nbsp;" : aNode.TextArray[i]) + "</NOBR>";
		
	}
}

//延迟加载树形开关
function JTTLazyExpandOrClose(aThis, aBegin, aExpandOrClose, isFilter){
	//标注已经延迟加载过
	aBegin.hasLazyLoaded = true;
	//需要展现复选框
    if(aThis.aNeedCheckbox){
		//改变该节点的checkbox的延迟加载状态
		var checkbox = aThis.mObj.children(0)
		   	                .rows(aThis.mObj.id +"_" + aBegin.id)
		   	                .cells("row"+aThis.mObj.id+"_"+aBegin.id)
		   	                .children(0)
		   	                .children("span" + aThis.mObj.id + "_" + aBegin.id)
		   	                .children(1)
		   	                .children("mTree_" + aThis.mObj.id + "_" + aBegin.id);
		if(checkbox != null){
			checkbox.flag = "hasLazyLoaded";
		}
	}
	
	lazyLoadSubTree(aBegin, aExpandOrClose, isFilter);
}

//edit by yubin
//延迟加载树形开关后由页面ajax调用的方法，否则该方法会先于页面ajax方法调用
function AfterJTTLazyExpandOrClose(aThis, aBegin, aExpandOrClose,isFilter)
{
   var i ;
   //lazy initial data
   fJTTLazyLoadInitTreeData(aThis, aBegin);

   if(aBegin.subtree != null && aBegin.subtree.items.length > 0){
	   var rowIndex =  aThis.mObj.children(0).rows(aThis.mObj.id + "_" + aBegin.id).rowIndex;   
	   for(i =0; i<aBegin.subtree.items.length; i++)
	   {
	   		var nowNode = aBegin.subtree.items[i];
		    var nowDisplay = aExpandOrClose;
		    if(isFilter){
		    	if(!nowNode.subtree){
		    		//alert(aThis.mFilter);
		    		if(aThis.mFilter){
		    			nowNode.mFiltered = eval(aThis.mFilterScript + "(nowNode)");
		    			nowDisplay = nowDisplay && !nowNode.mFiltered;
		    		}else{
		    			nowNode.mFiltered = false;
		    		}
		    	}
		    }else{
		    	if(!nowNode.subtree && aThis.mFilter){
		    		nowDisplay = nowDisplay && !nowNode.mFiltered;
		    	}
		    }
		    fJTTLazyBuildFromNode(aThis, aBegin, nowNode, i == (aBegin.subtree.items.length-1)? true : false, nowDisplay, rowIndex++);
	   }
   }else{
   		//若该节点延迟加载子树为空，则改变其开关图片样式
   		limg = aThis.mObj.children(0)
		   	                .rows(aThis.mObj.id +"_" + aBegin.id)
		   	                .cells("row"+aThis.mObj.id+"_"+aBegin.id)
		   	                .children(0)
		   	                .children("img"+aThis.mObj.id);
		limg.src = fJTTChangeNullChildrenImg(aThis, limg.src);
		limg.id = "";
   }

}

function JTSelectNodeById(aId)
{
	
	var lRows = this.mObj.children(0).rows;
	var lTemp ;


	var lNode = this.GetTreeNode(aId);
	
	if( lNode == null ) return null;

	if(this.mSelectHilight)
	{
		if( this.mSelected != null){
			lTemp =lRows(this.mObj.id + "_" + this.mSelected)
			            .cells("row"+this.mObj.id+"_"+ this.mSelected)
			            .children(0)
			            .children("span"+this.mObj.id+"_"+this.mSelected)
			            .children(1);
			            
			lTemp.style.backgroundColor = this.mOldBGColor;
			lTemp.style.color = this.mOldColor;
		}
		
		lTemp =lRows(this.mObj.id + "_" + aId)
		                               .cells("row"+this.mObj.id+"_"+aId)
		                               .children(0)
			                       .children("span"+this.mObj.id+"_"+aId)
			                       .children(1);
		this.mOldBGColor = lTemp.style.backgroundColor
		this.mOldColor   = lTemp.style.color                       
		lTemp.style.backgroundColor = JTTSelHilightBGColor;
		lTemp.style.color = JTTSelHilightColor;
    }
	this.mSelected = aId;

/**
 * due to the ActionJS executed by JTTClickOnNode(aThis,aId)
 */
//	if(lNode.ActionJS != null ){
//		eval(lNode.ActionJS);
//	}
	return lNode;

}



function JTTClickOnNode(aThis,aId)
{
	//alert(aId);
	var lNode = aThis.SelectNode(aId)
	if( lNode == null ) return null;
	//alert(lNode.id);
	var lRows = aThis.mObj.children(0).rows;
	var lTemp ;
	
	//var objEvent = createEventObject();

	//objEvent.ConnectedURL = m_szCurrentPrivURL;
	//id_OnClickNode.fire(objEvent);
	
	if(lNode.ActionJS != null ){
		eval(lNode.ActionJS);
	}
	
	aThis.mClickNode = lNode;
//  due to .htc file
//	if(aThis.mObj.onclicknode)
//	{
//		aThis.mObj.onclicknode(); 
//	}
}

function JTTAddNode(id,fid,textArr,aActionJS, expanded
                              , tooltip, icon, aCellStyleArr,others)
{
	this.mNodes.addTreeNode(id,fid,textArr,aActionJS, expanded
	                              , tooltip, icon, aCellStyleArr,others);
	this.RebuildTree();	                              
}

function JTTDelNodeById(aId)
{
   var i;
   if(aId == null || aId == 0) return false;
   
   for (i=0; i< this.mNodes.items.length; i++)
   {
   		if( this.mNodes.items[i].id == aId)
   		{ 
   			var lNode = this.mNodes.items[i];
   			if(lNode.subtree == null || lNode.subtree.items.length <= 0)
   			{
   				for(var j = i; j < this.mNodes.items.length - 1; j++)
   				{
   					this.mNodes.items[j] = this.mNodes.items[j + 1]
   				}
   				this.mNodes.items.length --;
   				this.RebuildTree();	
   				return true;
   			}
   			return false;
   		}
   }
   return false;
}
///////////////////////////////////////////

/*
   name - name of the cookie
   value - value of the cookie
   [expires] - expiration date of the cookie
     (defaults to end of current session)
   [path] - path for which the cookie is valid
     (defaults to path of calling document)
   [domain] - domain for which the cookie is valid
     (defaults to domain of calling document)
   [secure] - Boolean value indicating if the cookie transmission requires
     a secure transmission
   * an argument defaults when it is assigned null as a placeholder
   * a null placeholder is not required for trailing omitted arguments
*/

function setCookie(name, value, expires, path, domain, secure) {
  var curCookie = name + "=" + escape(value) +
      ((expires) ? "; expires=" + expires.toGMTString() : "") +
      ((path) ? "; path=" + path : "") +
      ((domain) ? "; domain=" + domain : "") +
      ((secure) ? "; secure" : "");
  document.cookie = curCookie;
}


/*
  name - name of the desired cookie
  return string containing value of specified cookie or null
  if cookie does not exist
*/

function getCookie(name) {
  var dc = document.cookie;
  var prefix = name + "=";
  var begin = dc.indexOf("; " + prefix);
  if (begin == -1) {
    begin = dc.indexOf(prefix);
    if (begin != 0) return null;
  } else
    begin += 2;
  var end = document.cookie.indexOf(";", begin);
  if (end == -1)
    end = dc.length;
  return unescape(dc.substring(begin + prefix.length, end));
}


/*
   name - name of the cookie
   [path] - path of the cookie (must be same as path used to create cookie)
   [domain] - domain of the cookie (must be same as domain used to
     create cookie)
   path and domain default if assigned null or omitted if no explicit
     argument proceeds
*/

function deleteCookie(name, path, domain) {
  if (getCookie(name)) {
    document.cookie = name + "=" +
    ((path) ? "; path=" + path : "") +
    ((domain) ? "; domain=" + domain : "") +
    "; expires=Thu, 01-Jan-70 00:00:01 GMT";
  }
}

// date - any instance of the Date object
// * hand all instances of the Date object to this function for "repairs"

function fixDate(date) {
  var base = new Date(0);
  var skew = base.getTime();
  if (skew > 0)
    date.setTime(date.getTime() - skew);
}

//延迟加载初始化oNode节点的子树节点信息
function fJTTLazyLoadInitTreeData(aThis, oNode)
{
	var i
	var fid;
	var lNode;
  
	for(i =0;i<oNode.subtree.items.length;i++)
	{
		oNode.subtree.items[i].icon = JTTDoIcons(aThis.mNodes.icons, oNode.subtree.items[i].icon);
		aThis.mNodes.addNode(oNode.subtree.items[i]);

		oNode.subtree.items[i].expanded = false;
		id = oNode.subtree.items[i].id;
		//保存每个父级节点的最后一个子级节点
		aThis.childNodes[oNode.id] = id;
	}
	
	/**lazy edit**/
	//如果是延迟加载，则为第一级子树的每个节点构造空的子树
	for(i=0; i<oNode.subtree.items.length; i++){
		oNode.subtree.items[i].MakeSubTree(new JTableTreeDataClass());
	}
}

//延迟加载时若某节点加载的子树为空，则改变该节点的开关图片样式
function fJTTChangeNullChildrenImg(aThis, aLastImg){
  var licons = aThis.mNodes.icons.items;
  var limg;

  if(aLastImg.lastIndexOf(licons[JTTCntTeePlusImg]) >=0) limg = licons[JTTCntTeeImg];  
  else if(aLastImg.lastIndexOf(licons[JTTCntTeeMinusImg]) >=0) limg = licons[JTTCntTeeImg];  

  else if(aLastImg.lastIndexOf(licons[JTTCntRootMinusImg]) >=0) limg = licons[JTTCntRootTeeImg];
  else if(aLastImg.lastIndexOf(licons[JTTCntRootPlusImg]) >=0) limg = licons[JTTCntRootTeeImg];
 
  else if(aLastImg.lastIndexOf(licons[JTTCntOnlyPlusImg]) >=0) limg = licons[JTTCntOnlyMinusImg];  
  else if(aLastImg.lastIndexOf(licons[JTTCntOnlyMinusImg]) >=0) limg = licons[JTTCntOnlyPlusImg];  
  
  else if(aLastImg.lastIndexOf(licons[JTTCntBottomMinusImg]) >=0) limg = licons[JTTCntBottomBarImg];  
  else if(aLastImg.lastIndexOf(licons[JTTCntBottomPlusImg])  >=0) limg = licons[JTTCntBottomBarImg];  
  else return aLastImg;

  if(aThis.mNodes.icons.ImgBaseDir )
  	return aThis.mNodes.icons.ImgBaseDir  + limg;
  else return limg;
}



