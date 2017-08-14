;var parsers = [{
	id : "text",
	is : function(s) {
		return true;
	},
	format : function(s) {
		return $.trim(s.toLowerCase());
	},
	type : "text"
}, {
	id : "integer",
	is : function(s) {
		return s.match(new RegExp(/^\d+$/));
	},
	format : function(s) {
		var i = parseInt(s);
		return (isNaN(i)) ? 0 : i;
	},
	type : "numeric"
}, {
	id : "currency",
	is : function(s) {
		return /^[£$€?.]/.test(s);
	},
	format : function(s) {
		s = s.replace(new RegExp(/[^0-9.]/g), "");
		var i = parseFloat(s);
		return (isNaN(i)) ? 0 : i;
	},
	type : "numeric"
}, {
	id : "integer",
	is : function(s) {
		return /^\d+$/.test(s);
	},
	format : function(s) {
		var i = parseFloat(s);
		return (isNaN(i)) ? 0 : i;
	},
	type : "numeric"
}, {
	id : "floating",
	is : function(s) {
		return s
				.match(new RegExp(/^(\+|-)?[0-9]+\.[0-9]+((E|e)(\+|-)?[0-9]+)?$/));
	},
	format : function(s) {
		s = s.replace(new RegExp(/,/), "");
		var i = parseFloat(s);
		return (isNaN(i)) ? 0 : i;
	},
	type : "numeric"
}, {
	id : "ipAddress",
	is : function(s) {
		return /^\d{2,3}[\.]\d{2,3}[\.]\d{2,3}[\.]\d{2,3}$/.test(s);
	},
	format : function(s) {
		var a = s.split(".");
		var r = "";
		for (var i = 0, item; item = a[i]; i++) {
			if (item.length == 2) {
				r += "0" + item;
			} else {
				r += item;
			}
		}
		var i = parseFloat(s);
		return (isNaN(i)) ? 0 : i;
	},
	type : "numeric"
}, {
	id : "url",
	is : function(s) {
		return /^(https?|ftp|file):\/\/$/.test(s);
	},
	format : function(s) {
		return jQuery.trim(s.replace(new RegExp(/(https?|ftp|file):\/\//), ''));
	},
	type : "text"
}, {
	id : "isoDate",
	is : function(s) {
		return /^\d{4}[\/-]\d{1,2}[\/-]\d{1,2}$/.test(s);
	},
	format : function(s) {
		s = (s != "") ? new Date(s.replace(
				new RegExp(/-/g), "/")).getTime() : "0";
		var i = parseFloat(s);
		return (isNaN(i)) ? 0 : i;
	},
	type : "numeric"
}, {
	id : "percent",
	is : function(s) {
		return /^\d{1,3}%$/.test(s);
	},
	format : function(s) {
		s = s.replace(new RegExp(/%/g), "");
		var i = parseFloat(s);
		return (isNaN(i)) ? 0 : i;
	},
	type : "numeric"
}, {
	id : "usLongDate",
	is : function(s) {
		return /^[A-Za-z]{3,10}\.? [0-9]{1,2}, ([0-9]{4}|\'?[0-9]{2}) (([0-2]?[0-9]:[0-5][0-9])|([0-1]?[0-9]:[0-5][0-9]\s(AM|PM)))$/
				.test(s);
	},
	format : function(s) {
		s = new Date(s).getTime();
		var i = parseFloat(s);
		return (isNaN(i)) ? 0 : i;
	},
	type : "numeric"
}, {
	id : "shortDate",
	is : function(s) {
		return /\d{1,2}[\/-]\d{1,2}[\/-]\d{2,4}/.test(s);
	},
	format : function(s, table) {
		var c = table.config;
		s = s.replace(new RegExp(/-/g), "/");
		if (c.dateFormat == "us") {
			/** reformat the string in ISO format */
			s = s.replace(new RegExp(/(\d{1,2})[\/-](\d{1,2})[\/-](\d{4})/),
					"$3/$1/$2");
		} else if (c.dateFormat == "uk") {
			/** reformat the string in ISO format */
			s = s.replace(new RegExp(/(\d{1,2})[\/-](\d{1,2})[\/-](\d{4})/),
					"$3/$2/$1");
		} else if (c.dateFormat == "dd/mm/yy" || c.dateFormat == "dd-mm-yy") {
			s = s.replace(new RegExp(/(\d{1,2})[\/-](\d{1,2})[\/-](\d{2})/),
					"$1/$2/$3");
		}
		s = new Date(s).getTime();
		var i = parseFloat(s);
		return (isNaN(i)) ? 0 : i;
	},
	type : "numeric"
}, {
	id : "time",
	is : function(s) {
		return /^(([0-2]?[0-9]:[0-5][0-9])|([0-1]?[0-9]:[0-5][0-9]\s(am|pm)))$/
				.test(s);
	},
	format : function(s) {
		s = new Date("2000/01/01 " + s).getTime();
		var i = parseFloat(s);
		return (isNaN(i)) ? 0 : i;
	},
	type : "numeric"
}]

;(function ($) {
/*
 * jqGrid  3.6 - jQuery Grid
 * Copyright (c) 2008, Tony Tomov, tony@trirand.com
 * Dual licensed under the MIT and GPL licenses
 * http://www.opensource.org/licenses/mit-license.php
 * http://www.gnu.org/licenses/gpl.html
 * Date: 2009-11-08
 */
$.jgrid = $.jgrid || {};
$.extend($.jgrid,{
	htmlDecode : function(value){
		if(value=='&nbsp;' || value=='&#160;' || (value.length==1 && value.charCodeAt(0)==160)) { return "";}
		return !value ? value : String(value).replace(/&amp;/g, "&").replace(/&gt;/g, ">").replace(/&lt;/g, "<").replace(/&quot;/g, '"');
	},
	htmlEncode : function (value){
		return !value ? value : String(value).replace(/&/g, "&amp;").replace(/>/g, "&gt;").replace(/</g, "&lt;").replace(/\"/g, "&quot;");
	},
	format : function(format){ //jqgformat
		var args = $.makeArray(arguments).slice(1);
		if(format===undefined) format = "";
		return format.replace(/\{(\d+)\}/g, function(m, i){
			return args[i];
		});
	},
	getCellIndex : function (cell) {
		cell = $(cell);
		cell = (!cell.is('td') && !cell.is('th') ? cell.closest("td,th") : cell)[0];
		if ($.browser.msie) return $.inArray(cell, cell.parentNode.cells);
		return cell.cellIndex;
	},
	stripHtml : function(v) {
		v = v+"";
		var regexp = /<("[^"]*"|'[^']*'|[^'">])*>/gi;
		if(v) {	return v.replace(regexp,"");}
		else {return v;}
	},
	stringToDoc : function (xmlString) {
		var xmlDoc;
		if(typeof xmlString !== 'string') return xmlString;
		try	{
			var parser = new DOMParser();
			xmlDoc = parser.parseFromString(xmlString,"text/xml");
		}
		catch(e) {
			xmlDoc = new ActiveXObject("Microsoft.XMLDOM");
			xmlDoc.async=false;
			xmlDoc["loadXM"+"L"](xmlString);
		}
		return (xmlDoc && xmlDoc.documentElement && xmlDoc.documentElement.tagName != 'parsererror') ? xmlDoc : null;
	},
	parse : function(jsonString) {
		var js = jsonString, msg;
		if (js.substr(0,9) == "while(1);") { js = js.substr(9); }
		if (js.substr(0,2) == "/*") { js = js.substr(2,js.length-4); }
		if(!js) { js = "{}"; }
		($.jgrid.useJSON===true && typeof (JSON) === 'object' && typeof (JSON.parse) === 'function')
		    ? msg = JSON.parse(js)
		    : msg = eval('(' + js + ')');
		return  msg.hasOwnProperty('d') ? msg.d : msg;
	},
	empty : function () {
		while ( this.firstChild ) this.removeChild( this.firstChild );
	},
	jqID : function(sid){
		sid = sid + "";
		return sid.replace(/([\.\:\[\]])/g,"\\$1");
	},
	ajaxOptions: {},
	extend : function(methods) {
		$.extend($.fn.jqGrid,methods);
		if (!this.no_legacy_api) {
			$.fn.extend(methods);
		}
	}
});

$.fn.jqGrid = function( pin ) {
	if (typeof pin == 'string') {
		var fn = $.fn.jqGrid[pin];
		if (!fn) {
			throw ("jqGrid - No such method: " + pin);
		}
		var args = $.makeArray(arguments).slice(1);
		return fn.apply(this,args);
	}
	return this.each( function() {
		if(this.grid) {return;}

		var p = $.extend(true,{
			url: "",
			height: 150,
			page: 1,
			rowNum: null,
			records: 0,
			pager: "",
			pgbuttons: true,
			pginput: true,
			colModel: [],
			rowList: [],
			colNames: [],
			sortorder: "asc",
			sortname: "",
			totleCol:[],//添加合计列属性
			radioselect:false, //添加单选框属性
			selectInfo:null,//添加已选择属性 
			orderType:"server",//添加页内排序属性
			isTips:true, //添加是否显示提示框属性
			isFixRum:false,//添加拖动时是否固定行号属性
			isFixMilt:false,//添加拖动时是否固定多选框
			isFixRadio:false,//添加拖动时是否固定单选框
			tipsClass:"pretty", //提示框的样式
			datatype: "xml",
			mtype: "GET",
			altRows: false,
			selarrrow: [],
			savedRow: [],
			shrinkToFit: true,
			xmlReader: {},
			jsonReader: {},
			subGrid: false,
			subGridModel :[],
			reccount: 0,
			lastpage: 0,
			lastsort: 0,
			selrow: null,
			beforeSelectRow: null,
			onSelectRow: null,
			onSortCol: null,
			ondblClickRow: null,
			onRightClickRow: null,
			onPaging: null,
			onSelectAll: null,
			loadComplete: null,
			gridComplete: null,
			loadError: null,
			loadBeforeSend: null,
			afterInsertRow: null,
			beforeRequest: null,
			onHeaderClick: null,
			viewrecords: false,
			loadonce: false,
			multiselect: false,
			multikey: false,
			editurl: null,
			search: false,
			caption: "",
			hidegrid: true,
			hiddengrid: false,
			postData: {},
			userData: {},
			treeGrid : false,
			treeGridModel : 'nested',
			treeReader : {},
			treeANode : -1,
			ExpandColumn: null,
			tree_root_level : 0,
			prmNames: {page:"page",rows:"rows", sort: "sidx",order: "sord", search:"_search", nd:"nd"},
			forceFit : false,
			gridstate : "visible",
			cellEdit: false,
			cellsubmit: "remote",
			nv:0,
			loadui: "enable",
			toolbar: [false,""],
			scroll: false,
			multiboxonly : false,
			deselectAfterSort : true,
			scrollrows : false,
			autowidth: false,
			scrollOffset :18,
			cellLayout: 5,
			subGridWidth: 20,
			multiselectWidth: 20,
			gridview: false,
			rownumWidth: 25,
			rownumbers : false,
			pagerpos: 'right',
			recordpos: 'center',
			footerrow : false,
			userDataOnFooter : false,
			hoverrows : true,
			altclass : 'ui-priority-secondary',
			viewsortcols : [false,'vertical',true],
			resizeclass : '',
			autoencode : false,
			remapColumns : [],
			ajaxGridOptions :{},
			direction : "ltr"
		}, $.jgrid.defaults, pin || {});
		var grid={         
			headers:[],
			cols:[],
			footers: [],
			dragStart: function(i,x,y) {
				this.resizing = { idx: i, startX: x.clientX, sOL : y[0]};
				this.hDiv.style.cursor = "col-resize";
				this.curGbox = $("#rs_m"+p.id,"#gbox_"+p.id);
				this.curGbox.css({display:"block",left:y[0],top:y[1],height:y[2]});
				if($.isFunction(p.resizeStart)) p.resizeStart.call(this,x,i);
				document.onselectstart=new Function ("return false");
			},
			dragMove: function(x) {
				if(this.resizing) {
					var diff = x.clientX-this.resizing.startX,
					h = this.headers[this.resizing.idx],
					newWidth = p.direction === "ltr" ? h.width + diff : h.width - diff, hn, nWn;
					if(newWidth > 33) {
						this.curGbox.css({left:this.resizing.sOL+diff});
						if(p.forceFit===true ){
							hn = this.headers[this.resizing.idx+p.nv];
							nWn = p.direction === "ltr" ? hn.width - diff : hn.width + diff;
							if(nWn >33) {
								h.newWidth = newWidth;
								hn.newWidth = nWn;
							}
						} else {
							this.newWidth = p.direction === "ltr" ? p.tblwidth+diff : p.tblwidth-diff;
							h.newWidth = newWidth;
						}
					}
				}
			},
			dragEnd: function() {
				this.hDiv.style.cursor = "default";
				if(this.resizing) {
					var idx = this.resizing.idx,
					nw = this.headers[idx].newWidth || this.headers[idx].width;
					nw = parseInt(nw);
					this.resizing = false;
					$("#rs_m"+p.id).css("display","none");
					p.colModel[idx].width = nw;
					this.headers[idx].width = nw;
					this.headers[idx].el.style.width = nw + "px";
					if(this.cols.length>0) {this.cols[idx].style.width = nw+"px";}
					if(this.footers.length>0) {this.footers[idx].style.width = nw+"px";}
					if(p.forceFit===true){
						nw = this.headers[idx+p.nv].newWidth || this.headers[idx+p.nv].width;
						this.headers[idx+p.nv].width = nw;
						this.headers[idx+p.nv].el.style.width = nw + "px";
						if(this.cols.length>0) this.cols[idx+p.nv].style.width = nw+"px";
						if(this.footers.length>0) {this.footers[idx+p.nv].style.width = nw+"px";}
						p.colModel[idx+p.nv].width = nw;
					} else {
						p.tblwidth = this.newWidth || p.tblwidth;
						$('table:first',this.bDiv).css("width",p.tblwidth+"px");
						$('table:first',this.hDiv).css("width",p.tblwidth+"px");
						this.hDiv.scrollLeft = this.bDiv.scrollLeft;
						if(p.footerrow) {
							$('table:first',this.sDiv).css("width",p.tblwidth+"px");
							this.sDiv.scrollLeft = this.bDiv.scrollLeft;
						}
					}
					if($.isFunction(p.resizeStop)) p.resizeStop.call(this,nw,idx);
				}
   				this.curGbox=null;
				document.onselectstart=new Function ("return true");
			},
			populateVisible: function() {
				if (grid.timer) clearTimeout(grid.timer);
				grid.timer = null;

				var dh = $(grid.bDiv).height();
				if (!dh) return;
				var table = $("table:first", grid.bDiv);
				var rows = $("> tbody > tr:visible:first", table);
				var rh = rows.outerHeight() || grid.prevRowHeight;
				if (!rh) return;
				grid.prevRowHeight = rh;
				var rn = p.rowNum;
				if (rn < 10) {
					rn = parseInt(dh / rh) + 1 << 1;
					if (rn < 10) rn = 10;
					p.rowNum = rn;
				}
				var scrollTop = grid.scrollTop = grid.bDiv.scrollTop;
				var ttop = Math.round(table.position().top) - scrollTop;
				var tbot = ttop + table.height();
				var div = rh * rn;
				var page, npage, empty;
			    if (ttop <= 0 && tbot < dh && 
                    (p.lastpage==null||parseInt((tbot + scrollTop + div - 1) / div) < p.lastpage))
                {
					npage = parseInt((dh - tbot + div - 1) / div);
					if (tbot >= 0 || npage < 2 || p.scroll === true) {
						page = parseInt((tbot + scrollTop) / div) + 1;
						ttop = -1;
					} else {
						ttop = 1;
					}
				}
				if (ttop > 0) {
					page = parseInt(scrollTop / div) + 1;
					npage = parseInt((scrollTop + dh) / div) + 2 - page;
					empty = true;
				}

				if (npage) {
					if (p.lastpage && page > p.lastpage) {
						return;
					}
					if (grid.hDiv.loading) {
						grid.timer = setTimeout(grid.populateVisible, 200);
					} else {
						p.page = page;
						if (empty) {
							grid.selectionPreserver(table[0]);
							grid.emptyRows(grid.bDiv);
						}
						grid.populate(npage);
					}
				}
			},
			scrollGrid: function() {
				if(p.scroll) {
					var scrollTop = grid.bDiv.scrollTop;
					if (scrollTop != grid.scrollTop) {
						grid.scrollTop = scrollTop;
						if (grid.timer) clearTimeout(grid.timer);
						grid.timer = setTimeout(grid.populateVisible, 200);
					}
				}
				grid.hDiv.scrollLeft = grid.bDiv.scrollLeft;
				if(p.footerrow) {
					grid.sDiv.scrollLeft = grid.bDiv.scrollLeft;
				}
				
				//固定列
			  var colMs = p.colModel;
			  var trs = $("tr",this);
			  for(var colMsIndex = 0 ;colMsIndex<colMs.length;colMsIndex++)
			  {
			      if(typeof colMs[colMsIndex].fixecol == "undefined")//fixecol列属性设置，判断这列是否设置为固定
			      {
			           colMs[colMsIndex].fixecol = false;			           
			      }
			      var h = colMs[colMsIndex].fixecol;
			      if(colMs[colMsIndex].fixecol)
			      {
			         
			         trs.each(function(i){
			         $("td:eq("+colMsIndex+")",this).addClass("FixedCol");//.css("left",grid.bDiv.scrollLeft);
			         $("th:eq("+colMsIndex+")",grid.hDiv).addClass("FixedCol");//.css("left",grid.bDiv.scrollLeft);
			         $("td:eq("+colMsIndex+")",grid.sDiv).addClass("FixedCol");//.css("left",grid.bDiv.scrollLeft);
			         });
			      }
			  }
			    if(ts.p.isFixRum)//判断是否固定编号
			    {
			        trs.each(function(i){
			         $(".jqgrid-rownum",this).addClass("FixedCol");//.css("left",grid.bDiv.scrollLeft);
			         
			         });
			    }
			    if(ts.p.isFixMilt)//判断是否固定复选框
			    {
			        trs.each(function(i){
			         $(".cbox",this).parent().addClass("FixedCol");//.css("left",grid.bDiv.scrollLeft);
			         $(".cbox",grid.hDiv).parent().addClass("FixedCol");//.css("left",grid.bDiv.scrollLeft);
			         });
			    }
			    if(ts.p.isFixRadio)//判断是否固定单选框
			    {
			       trs.each(function(i){
			         $(".rbox",this).parent().addClass("FixedCol");//.css("left",grid.bDiv.scrollLeft);
			         
			         });
			    }
			
			},
			selectionPreserver : function(ts) {
				var p = ts.p;
				var sr = p.selrow, sra = p.selarrrow ? $.makeArray(p.selarrrow) : null;
				var left = ts.grid.bDiv.scrollLeft;
				var complete = p.gridComplete;
				p.gridComplete = function() {
					p.selrow = null;
					p.selarrrow = [];
					if(p.multiselect && sra && sra.length>0) {
						for(var i=0;i<sra.length;i++){
							if (sra[i] != sr)
								$(ts).jqGrid("setSelection",sra[i],false);
						}
					}
					if (sr) {
						$(ts).jqGrid("setSelection",sr,false);
					}
					ts.grid.bDiv.scrollLeft = left;
					if (p.gridComplete = complete) {
						complete();
					}
				}
			}
		};
		this.p = p ;
		var i, dir,ts;
		if(this.p.colNames.length === 0) {
			for (i=0;i<this.p.colModel.length;i++){
				this.p.colNames[i] = this.p.colModel[i].label || this.p.colModel[i].name;
			}
		}
		if( this.p.colNames.length !== this.p.colModel.length ) {
			alert($.jgrid.errors.model);
			return;
		}
		var gv = $("<div class='ui-jqgrid-view'></div>"), ii,
		isMSIE = $.browser.msie ? true:false,
		isSafari = $.browser.safari ? true : false;
		ts = this;
		ts.p.direction = $.trim(ts.p.direction.toLowerCase());
		if($.inArray(ts.p.direction,["ltr","rtl"]) == -1) ts.p.direction = "ltr";
		dir = ts.p.direction;

		$(gv).insertBefore(this);
		$(this).appendTo(gv).removeClass("scroll");
		var eg = $("<div class='ui-jqgrid ui-widget ui-widget-content ui-corner-all'></div>");
		$(eg).insertBefore(gv).attr({"id" : "gbox_"+this.id,"dir":dir});
		$(gv).appendTo(eg).attr("id","gview_"+this.id);
		if (isMSIE && $.browser.version <= 6) {
			ii = '<iframe style="display:block;position:absolute;z-index:-1;filter:Alpha(Opacity=\'0\');" src="javascript:false;"></iframe>';
		} else { ii="";}
		$("<div class='ui-widget-overlay jqgrid-overlay' id='lui_"+this.id+"'></div>").append(ii).insertBefore(gv);
		//$("<div class='loading ui-state-default ui-state-active' id='load_"+this.id+"'>"+this.p.loadtext+"</div>").insertBefore(gv);
		//提示文字改为提示图标
		$("<div class='loading' id='load_" + this.id + "'>" + this.p.loadtext + "</div>").insertBefore(gv);
		$(this).attr({cellSpacing:"0",cellPadding:"0",border:"0","role":"grid","aria-multiselectable":!!this.p.multiselect,"aria-labelledby":"gbox_"+this.id});
		var sortkeys = ["shiftKey","altKey","ctrlKey"],
		IntNum = function(val,defval) {
			val = parseInt(val,10);
			if (isNaN(val)) { return defval ? defval : 0;}
			else {return val;}
		},
		formatCol = function (pos, rowInd){
			var ral = ts.p.colModel[pos].align, result="style=\"", clas = ts.p.colModel[pos].classes;
			if(ral) result += "text-align:"+ral+";";
			if(ts.p.colModel[pos].hidden===true) result += "display:none;";
			if(rowInd===0) {
				result += "width: "+grid.headers[pos].width+"px;"
			}
			return result+"\"" + (clas !== undefined ? (" class=\""+clas+"\"") :"");
		},
		addCell = function(rowId,cell,pos,irow, srvr) {
			var v,prp;
			var cm = ts.p.colModel[pos];//修改value
			v = formatter(rowId,cell,pos,srvr,'add');
			prp = formatCol( pos,irow);
			if(cm.formatter == "date")
				{
				    cell = v;
				}
			return "<td value = '"+cell+"' role=\"gridcell\" "+prp+" tips=\""+$.jgrid.stripHtml(v)+"\">"+v+"</td>";
		},
		formatter = function (rowId, cellval , colpos, rwdat, _act){
			var cm = ts.p.colModel[colpos],v;
			if(typeof cm.formatter !== 'undefined') {
				var opts= {rowId: rowId, colModel:cm };
				if($.isFunction( cm.formatter ) ) {
					v = cm.formatter(cellval,opts,rwdat,_act);
				} else if($.fmatter){
					v = $.fn.fmatter(cm.formatter, cellval,opts, rwdat, _act);
				} else {
					v = cellVal(cellval);
				}
			} else {
				v = cellVal(cellval);
			}
			return v;
		},
		cellVal =  function (val) {
			return val === undefined || val === null || val === "" ? "&#160;" : ts.p.autoencode ? $.jgrid.htmlEncode(val+"") : val+"";
		},
		addMulti = function(rowid,pos,irow){
			var	v = "<input type=\"checkbox\""+" id=\"jqg_"+rowid+"\" class=\"cbox\" name=\"jqg_"+rowid+"\"/>",
			prp = formatCol(pos,irow);
			return "<td role='gridcell' "+prp+">"+v+"</td>";
		},
		addRadio = function (rowid, pos, irow) {//添加单选框
				var v = "<input type=\"radio\" id=\"jqg_" + rowid + "\" class=\"rbox\" name='jqg_' />", prp = formatCol(pos, irow);
				return "<td role='gridcell' " + prp + ">" + v + "</td>";
			},
		addRowNum = function (pos,irow,pG,rN) {
			var v =  (parseInt(pG)-1)*parseInt(rN)+1+irow,
			prp = formatCol(pos,irow);
			return "<td role=\"gridcell\" class=\"ui-state-default jqgrid-rownum\" "+prp+">"+v+"</td>";
		},
		reader = function (datatype) {
			var field, f=[], j=0, i;
			for(i =0; i<ts.p.colModel.length; i++){
				field = ts.p.colModel[i];
				if (field.name !== 'cb' && field.name !=='subgrid' && field.name !=='rn' && field.name !=='rb') {//添加单选框判断
					f[j] = (datatype=="xml") ? field.xmlmap || field.name : field.jsonmap || field.name;
					j++;
				}
			}
			return f;
		},
		orderedCols = function (offset) {
			var order = ts.p.remapColumns;
			if (!order || !order.length)
				order = $.map(ts.p.colModel, function(v,i) { return i; });
			if (offset)
				order = $.map(order, function(v) { return v<offset?null:v-offset });
			return order;
		},
		emptyRows = function (parent, scroll) {
			var tBody = $("tbody:first", parent);
			if(!ts.p.gridview || ts.p.jqgdnd) $("*",tBody).children().unbind();
			if(isMSIE) $.jgrid.empty.apply(tBody[0]);
			else tBody[0].innerHTML="";
			if (scroll && ts.p.scroll) {
				$(">div:first", parent).css({height:"auto"}).children("div:first").css({height:0,display:"none"});
				parent.scrollTop = 0;
			}
			tBody = null;
		},
		addXmlData = function (xml,t, rcnt, more, adjust) {
			var startReq = new Date();
			ts.p.reccount = 0;
			if($.isXMLDoc(xml)) {
				if(ts.p.treeANode===-1 && !ts.p.scroll) {
					emptyRows(t);
					rcnt=0;
				} else { rcnt = rcnt > 0 ? rcnt :0; }
			} else { return; }
			var i,fpos,ir=0,v,row,gi=0,si=0,ni=0,idn, getId,f=[],F,rd ={}, rl= ts.rows.length, xmlr,rid, rowData=[],ari=0, cn=(ts.p.altRows === true) ? " "+ts.p.altclass:"",cn1;
			if(!ts.p.xmlReader.repeatitems) {f = reader("xml");}
			if( ts.p.keyIndex===false) {
				idn = ts.p.xmlReader.id;
			} else {
				idn = ts.p.keyIndex;
			}
			if(f.length>0 && !isNaN(idn)) {
				if (ts.p.remapColumns && ts.p.remapColumns.length) {
					idn = $.inArray(idn, ts.p.remapColumns);
				}
				idn=f[idn];
			}
			if( (idn+"").indexOf("[") === -1 ) {
				if (f.length) {
					getId = function( trow, k) {return $(idn,trow).text() || k;};
				} else {
					getId = function( trow, k) {return $(ts.p.xmlReader.cell,trow).eq(idn).text() || k;};
				}
			}
			else {
				getId = function( trow, k) {return trow.getAttribute(idn.replace(/[\[\]]/g,"")) || k;};
			}
			$(ts.p.xmlReader.page,xml).each(function() {ts.p.page = this.textContent  || this.text || 1; });
			$(ts.p.xmlReader.total,xml).each(function() {ts.p.lastpage = this.textContent  || this.text || 1; }  );
			$(ts.p.xmlReader.records,xml).each(function() {ts.p.records = this.textContent  || this.text  || 0; }  );
			$(ts.p.xmlReader.userdata,xml).each(function() {ts.p.userData[this.getAttribute("name")]=this.textContent || this.text;});
			var gxml = $(ts.p.xmlReader.root+" "+ts.p.xmlReader.row,xml),gl = gxml.length, j=0;
			if(gxml && gl){
			var rn = parseInt(ts.p.rowNum),br=ts.p.scroll?(parseInt(ts.p.page)-1)*rn+1:1;
			if (adjust) rn *= adjust+1;
			var afterInsRow = $.isFunction(ts.p.afterInsertRow);
			while (j<gl) {
				xmlr = gxml[j];
				rid = getId(xmlr,br+j);
				cn1 = j%2 == 1 ? cn : '';
				rowData[ari++] = "<tr id=\""+rid+"\" role=\"row\" saveType = 'edit' class =\"ui-widget-content jqgrow ui-row-"+ts.p.direction+""+cn1+"\">";
				if(ts.p.rownumbers===true) {
					rowData[ari++] = addRowNum(0,j,ts.p.page,ts.p.rowNum);
					ni=1;
				}
				if(ts.p.multiselect===true) {
					rowData[ari++] = addMulti(rid,ni,j);
					gi=1;
				}
					if(ts.p.radioselect === true)//添加单选框
						{
						    rowData[ari++] = addRadio(rid,ni,j); 
						    gi = 1;
						}
				if (ts.p.subGrid===true) {
					rowData[ari++]= $(ts).jqGrid("addSubGridCell",gi+ni,j+rcnt);
					si= 1;
				}
				if(ts.p.xmlReader.repeatitems){
					if (!F) F=orderedCols(gi+si+ni);
					var cells = $(ts.p.xmlReader.cell,xmlr);
					$.each(F, function (k) {
						var cell = cells[this];
						if (!cell) return false;
						v = cell.textContent || cell.text;
						rd[ts.p.colModel[k+gi+si+ni].name] = v;
						rowData[ari++] = addCell(rid,v,k+gi+si+ni,j+rcnt,xmlr);
					});
				} else {
					for(i = 0; i < f.length;i++) {
						v = $(f[i],xmlr).text();
						rd[ts.p.colModel[i+gi+si+ni].name] = v;
						rowData[ari++] = addCell(rid, v, i+gi+si+ni, j+rcnt, xmlr);
					}
				}
				rowData[ari++] = "</tr>";
				if(ts.p.gridview === false ) {
					if( ts.p.treeGrid === true) {
						fpos = ts.p.treeANode >= -1 ? ts.p.treeANode: 0;
						row = $(rowData.join(''))[0]; // speed overhead
						try {$(ts).jqGrid("setTreeNode",rd,row);} catch (e) {}
						rl ===  0 ? $("tbody:first",t).append(row) : $(ts.rows[j+fpos+rcnt]).after(row);
					} else {
						$("tbody:first",t).append(rowData.join(''));
					}
					if (ts.p.subGrid===true) {
						try {$(ts).jqGrid("addSubGrid",ts.rows[ts.rows.length-1],gi+ni);} catch (e){}
					}
					if(afterInsRow) {ts.p.afterInsertRow.call(ts,rid,rd,xmlr);}
					rowData=[];ari=0;
				}
				rd={};
				ir++;
				j++;
				if(ir==rn) {break;}
			}
			}
			if(ts.p.gridview === true) {
				$("tbody:first",t).append(rowData.join(''));
			}
			ts.p.totaltime = new Date() - startReq;
			if(ir>0) {ts.grid.cols = ts.rows[0].cells;if(ts.p.records===0)ts.p.records=gl;}
			rowData =null;
			if(!ts.p.treeGrid && !ts.p.scroll) {ts.grid.bDiv.scrollTop = 0;}
			ts.p.reccount=ir;
			ts.p.treeANode = -1;
			if(ts.p.userDataOnFooter) $(ts).jqGrid("footerData","set",ts.p.userData,true);
			if (!more) updatepager(false,true);
		},
		addJSONData = function(data,t, rcnt, more, adjust) {
			var startReq = new Date();
			ts.p.reccount = 0;
			if(data) {
				if(ts.p.treeANode === -1 && !ts.p.scroll) {
					emptyRows(t);
					rcnt=0;
				} else { rcnt = rcnt > 0 ? rcnt :0; }
			} else { return; }
			var ir=0,v,i,j,row,f=[],F,cur,gi=0,si=0,ni=0,len,drows,idn,rd={}, fpos,rl = ts.rows.length,idr,rowData=[],ari=0,cn=(ts.p.altRows === true) ? " "+ts.p.altclass:"",cn1;
			ts.p.page = data[ts.p.jsonReader.page] || 1;
			ts.p.lastpage= data[ts.p.jsonReader.total] || 1;
			ts.p.records= data[ts.p.jsonReader.records] || 0;
			ts.p.userData = data[ts.p.jsonReader.userdata] || {};
			if(!ts.p.jsonReader.repeatitems) {
				F = f = reader("json");
			}

			if( ts.p.keyIndex===false ) {
				idn = ts.p.jsonReader.id;
			} else {
				idn = ts.p.keyIndex;
			}
			if(f.length>0 && !isNaN(idn)) {
				if (ts.p.remapColumns && ts.p.remapColumns.length) {
					idn = $.inArray(idn, ts.p.remapColumns);
				}
				idn=f[idn];
			}
			drows = data[ts.p.jsonReader.root];
			if( ts.p.treeGrid === true && ts.p.treeGridModel=='adjacency') {//当为树形列表时，对树形列表进行排序
				drows = $(ts).jqGrid("initTreeData",drows,idn);
				}
			if (drows) {
			len = drows.length, i=0;
			var rn = parseInt(ts.p.rowNum),br=ts.p.scroll?(parseInt(ts.p.page)-1)*rn+1:1;
			if (adjust) rn *= adjust+1;
			var afterInsRow = $.isFunction(ts.p.afterInsertRow);
			while (i<len) {
				cur = drows[i];
				idr = cur[idn];
				if(idr === undefined) {
					idr = br+i;
					if(f.length===0){
						if(ts.p.jsonReader.cell){
							var ccur = cur[ts.p.jsonReader.cell];
							idr = ccur[idn] || idr;
							ccur=null;
						}
					}
				}
				cn1 = i%2 == 1 ? cn : '';
				rowData[ari++] = "<tr id=\""+ idr +"\" role=\"row\" saveType = 'edit' class= \"ui-widget-content jqgrow ui-row-"+ts.p.direction+""+cn1+"\">";
				if(ts.p.rownumbers===true) {
				if(ts.p.rowNum !=null)
				{
					rowData[ari++] = addRowNum(0,i,ts.p.page,ts.p.rowNum);
					}else
					{
					rowData[ari++] = addRowNum(0,i,ts.p.page,ir);
					}
					ni=1;
				}
				if(ts.p.multiselect){
					rowData[ari++] = addMulti(idr,ni,i);
					gi = 1;
				}
				   if(ts.p.radioselect === true)//添加单选框
						{
						    rowData[ari++] = addRadio(idr,ni,i); 
						    gi = 1;
						}
				if (ts.p.subGrid) {
					rowData[ari++]= $(ts).jqGrid("addSubGridCell",gi+ni,i+rcnt);
					si= 1;
				}
				if (ts.p.jsonReader.repeatitems) {
					if(ts.p.jsonReader.cell) {cur = cur[ts.p.jsonReader.cell];}
					if (!F) F=orderedCols(gi+si+ni);
				}
				for (j=0;j<F.length;j++) {
					v=cur[F[j]];
					if(v===undefined) {
						try { v = eval("cur."+F[j]);}
						catch (e) {}
					}
					rowData[ari++] = addCell(idr,v,j+gi+si+ni,i+rcnt,cur);
					rd[ts.p.colModel[j+gi+si+ni].name] = v;
				}

				rowData[ari++] = "</tr>";
				if(ts.p.gridview === false ) {
					if( ts.p.treeGrid === true) {
						fpos = ts.p.treeANode >= -1 ? ts.p.treeANode: 0;
						row = $(rowData.join(''))[0];
						try {$(ts).jqGrid("setTreeNode",rd,row);} catch (e) {}
						rl ===  0 ? $("tbody:first",t).append(row) : $(ts.rows[i+fpos+rcnt]).after(row);
					} else {
						$("tbody:first",t).append(rowData.join(''));
					}
					if(ts.p.subGrid === true ) {
						try { $(ts).jqGrid("addSubGrid",ts.rows[ts.rows.length-1],gi+ni);} catch (e){}
					}
					if(afterInsRow) {ts.p.afterInsertRow(idr,rd,cur);}
					rowData=[];ari=0;
				}
				rd={};
				ir++;
				i++;
				if(ir==rn) break;
			}
			if(ts.p.gridview === true ) {
				$("tbody:first",t).append(rowData.join(''));
			}
			ts.p.totaltime = new Date() - startReq;
			if(ir>0) {ts.grid.cols = ts.rows[0].cells;if(ts.p.records===0)ts.p.records=len;}
			}
			if(!ts.p.treeGrid && !ts.p.scroll) {ts.grid.bDiv.scrollTop = 0;}
			ts.p.reccount=ir;
			ts.p.treeANode = -1;
			if(ts.p.userDataOnFooter) $(ts).jqGrid("footerData","set",ts.p.userData,true);
			if (!more) updatepager(false,true);
		},
		updatepager = function (rn, dnd) {
				var cp, last, base, bs, from, to, tot, fmt;
				base = (parseInt(ts.p.page) - 1) * parseInt(ts.p.rowNum);
				to = base + ts.p.reccount;
				if (ts.p.scroll) {
					var rows = $("tbody:first > tr", ts.grid.bDiv);
					base = to - rows.length;
					var rh = rows.outerHeight();
					if (rh) {
						var top = base * rh;
						var height = parseInt(ts.p.records, 10) * rh;
						$(">div:first", ts.grid.bDiv).css({height:height}).children("div:first").css({height:top, display:top ? "" : "none"});
					}
				}
				if (ts.p.pager) {
					fmt = $.jgrid.formatter.integer || {};
					if (ts.p.loadonce) {
						cp = last = 1;
						ts.p.lastpage = ts.page = 1;
						$(".selbox", ts.p.pager).attr("disabled", true);
					} else {
						cp = IntNum(ts.p.page);
						last = IntNum(ts.p.lastpage);
						$(".selbox", ts.p.pager).attr("disabled", false);
					}
					if (ts.p.pginput === true) {
						$(".ui-pg-input", ts.p.pager).val(ts.p.page);
						$(".ui-pg-selbox", ts.p.pager).val(ts.p.rowNum);
						$("#cp_1", ts.p.pager).html($.fmatter ? $.fmatter.util.NumberFormat(ts.p.page, fmt) : ts.p.page).width();
						$("#sp_1", ts.p.pager).html($.fmatter ? $.fmatter.util.NumberFormat(ts.p.lastpage, fmt) : ts.p.lastpage).width();
					    if (ts.p.reccount === 0) {
							$(".ui-paging-info", ts.p.pager).html("数据总量：&nbsp;&nbsp;"+"0");
						} else {
							from = base + 1;
							tot = ts.p.records;
							if ($.fmatter) {
								tot = $.fmatter.util.NumberFormat(tot, fmt);
							}
							$(".ui-paging-info", ts.p.pager).html("数据总量：&nbsp;&nbsp;"+tot);
						}
					
					
					
					}
					if (ts.p.viewrecords) {
						if (ts.p.reccount === 0) {
							$(".ui-paging-info", ts.p.pager).html(ts.p.emptyrecords);
						} else {
							from = base + 1;
							tot = ts.p.records;
							if ($.fmatter) {
								from = $.fmatter.util.NumberFormat(from, fmt);
								to = $.fmatter.util.NumberFormat(to, fmt);
								tot = $.fmatter.util.NumberFormat(tot, fmt);
							}
							$(".ui-paging-info", ts.p.pager).html($.jgrid.format(ts.p.recordtext, from, to, tot));
						}
					}
					if (ts.p.pgbuttons === true) {
						if (cp <= 0) {
							cp = last = 1;
						}
						if (cp == 1) {
							$("#first, #prev", ts.p.pager).addClass("ui-state-disabled").removeClass("ui-state-hover");
						} else {
							$("#first, #prev", ts.p.pager).removeClass("ui-state-disabled");
						}
						if (cp == last) {
							$("#next, #last", ts.p.pager).addClass("ui-state-disabled").removeClass("ui-state-hover");
						} else {
							$("#next, #last", ts.p.pager).removeClass("ui-state-disabled");
						}
					}
				}
				if (rn === true && ts.p.rownumbers === true) {
					$("td.jqgrid-rownum", ts.rows).each(function (i) {
						$(this).html(base + 1 + i);
					});
				}
				if (dnd && ts.p.jqgdnd) {
					$(ts).jqGrid("gridDnD", "updateDnD");
				}
				if ($.isFunction(ts.p.gridComplete)) {
					ts.p.gridComplete();
				}
			},
		populate = function (npage) {
				if (!ts.grid.hDiv.loading) {
					var pvis = ts.p.scroll && npage == false;
					var prm = {}, dt, dstr, pN = ts.p.prmNames;
					if (pN.search !== null) {
						prm[pN.search] = ts.p.search;
					}
					if (pN.nd != null) {
						prm[pN.nd] = new Date().getTime();
					}
					if (pN.rows !== null ) {//修改传入的unitpage让它不要转入空值
					    if(ts.p.rowNum!==null)
					      {
						     prm[pN.rows] = ts.p.rowNum;
						   }else
						   {
					         prm[pN.rows] = "";
						   }
					}
					if (pN.page !== null) {
						prm[pN.page] = ts.p.page;
					}
					if (pN.sort !== null) {
						prm[pN.sort] = ts.p.sortname;
					}
					if (pN.order !== null) {
						prm[pN.order] = ts.p.sortorder;
					}
					var lc = ts.p.loadComplete;
					var lcf = $.isFunction(lc);
					if (!lcf) {
						lc = null;
					}
					var adjust = 0;
					npage = npage || 1;
					if (npage > 1) {
						if (pN.npage != null) {
							prm[pN.npage] = npage;
							adjust = npage - 1;
							npage = 1;
						} else {
							lc = function (req) {
								if (lcf) {//设置在任何情况下都调用loadComplete
									ts.p.loadComplete.call(ts, req);
								}
								ts.grid.hDiv.loading = false;
								ts.p.page++;
								populate(npage - 1);
							};
						}
					} else {
						if (pN.npage != null) {
							delete ts.p.postData[pN.npage];
						}
					}
					$.extend(ts.p.postData, prm,ts.p.postData);
					var rcnt = !ts.p.scroll ? 0 : ts.rows.length - 1;
					if ($.isFunction(ts.p.datatype)) {
						ts.p.datatype(ts.p.postData, "load_" + ts.p.id);
						return;
					} else {
						if ($.isFunction(ts.p.beforeRequest)) {
							ts.p.beforeRequest.call(ts);
						}
					}
					dt = ts.p.datatype.toLowerCase();
					switch (dt) {
					  case "json":
					  case "jsonp":
					  case "xml":
					  case "script":
						$.ajax($.extend({url:ts.p.url, type:ts.p.mtype, dataType:dt, data:$.isFunction(ts.p.serializeGridData) ? ts.p.serializeGridData(ts.p.postData) : ts.p.postData, complete:function (req, st) {
							if (st == "success" || (req.statusText == "OK" && req.status == "200")) {
								if (dt === "xml") {
									addXmlData(req.responseXML, ts.grid.bDiv, rcnt, npage > 1, adjust);
								} else {
								var jsonData = $.jgrid.parse(req.responseText);//添加合计列
								var userdata = {};
								if(ts.p.totleCol.length>0)
								{
								   var totlecol = ts.p.totleCol;
								   for(var totlecolIndex = 0;totlecolIndex<totlecol.length;totlecolIndex++)
								   {
								        var cm = totlecol[totlecolIndex].name;
								        if(typeof totlecol[totlecolIndex].value == "undefined")
								        {
								           
								           var totle = 0;
								           var rows = jsonData['rows'];
								           for(var rowIndex = 0 ; rowIndex < rows.length;rowIndex++)
								           {
								               var rowvalue = rows[rowIndex];
								               var salary = rowvalue[cm];
								               var num = Number(salary);
								               if(num != "NaN")
								               {
								                   totle = totle + num;
								               }
								           }
								           userdata[cm] = totle;
								           
								        }else
								        {
								           userdata[cm] = totlecol[totlecolIndex].value;
								        }
								   }
								   jsonData['userdata']=userdata;
								}
								
								    jsonData = addCountCol(jsonData);//添加计算列
									addJSONData(jsonData, ts.grid.bDiv, rcnt, npage > 1, adjust);
									if(ts.p.isTips)//添加tips功能
									{
									    var tipClass = ts.p.tipsClass;
									   	var tds = $("td",ts)
	                                    tds.Tooltip({
	                                       showURL: false,
                                           extraClass: tipClass,
	                                       opacity: 0.95		                                    
	                                    });
									}
							
								}
								
								if (lc) {
									lc.call(ts,jsonData, req);//加载完成之后添加了一个参数jsonData
								}
								
								if (pvis) {
									ts.grid.populateVisible();
								}
							}
							req = null;
							if(ts.p.selectInfo !==null && ts.p.toolbar[0])
							{
							$("#t_"+$(ts).attr("id")).css("text-align", "left").html("已选择：");
							}
							if(ts.p.selectInfo !==null && ts.p.toolbar[0])
							{
							$('.rbox',ts).bind("click",function(){
							var sr = $(this).parent().parent().attr("id");
							$(ts).jqGrid("setSelection",sr,false);
							var udata = $("#"+$.jgrid.jqID(ts.p.id)).jqGrid('getRowNums');
		                 if(udata == null)
		                     {
		                     udata = "";
		                     }
			               $("#t_"+$.jgrid.jqID(ts.p.id)).css("text-align", "left").html("已选择：" + udata);
							});
							
							}
						
							endReq();
						}, error:function (xhr, st, err) {
							if ($.isFunction(ts.p.loadError)) {
								ts.p.loadError.call(ts, xhr, st, err);
							}
							endReq();
							xhr = null;
						}, beforeSend:function (xhr) {
							beginReq();
							if ($.isFunction(ts.p.loadBeforeSend)) {
								ts.p.loadBeforeSend.call(this, xhr);
							}
						}}, $.jgrid.ajaxOptions, ts.p.ajaxGridOptions));
						if (ts.p.loadonce || ts.p.treeGrid) {
							ts.p.datatype = "local";
						}
						break;
					  case "xmlstring":
						beginReq();
						addXmlData(dstr = $.jgrid.stringToDoc(ts.p.datastr), ts.grid.bDiv);
						ts.p.datatype = "local";
						if (lcf) {
							ts.p.loadComplete.call(ts, dstr);
						}
						ts.p.datastr = null;
						endReq();
						break;
					  case "jsonstring":
						beginReq();
						if (typeof ts.p.datastr == "string") {
							dstr = $.jgrid.parse(ts.p.datastr);
						} else {
							dstr = ts.p.datastr;
						}
						addJSONData(dstr, ts.grid.bDiv);
						ts.p.datatype = "local";
						if (lcf) {
							ts.p.loadComplete.call(ts, dstr);
						}
						ts.p.datastr = null;
						endReq();
						break;
					  case "local":
					  case "clientside":
						beginReq();
						ts.p.datatype = "local";
						sortArrayData();
						endReq();
						break;
					}
				}
				if(ts.p.cache){
					ts.p.cache = null;//数据重载后清空当前页排序缓存
				}
			},
		beginReq = function() {
			ts.grid.hDiv.loading = true;
			if(ts.p.hiddengrid) { return;}
			switch(ts.p.loadui) {
				case "disable":
					break;
				case "enable":
					$("#load_"+ts.p.id).show();
					break;
				case "block":
					$("#lui_"+ts.p.id).show();
					$("#load_"+ts.p.id).show();
					break;
			}
		},
		endReq = function() {
			ts.grid.hDiv.loading = false;
			switch(ts.p.loadui) {
				case "disable":
					break;
				case "enable":
					$("#load_"+ts.p.id).hide();
					break;
				case "block":
					$("#lui_"+ts.p.id).hide();
					$("#load_"+ts.p.id).hide();
					break;
			}
		},addCountCol = function(jsonData){//添加计算列
			  var colMs = ts.p.colModel;
			  var countCol = [],rows = jsonData['rows'];
			  for(var colMsIndex = 0 ;colMsIndex<colMs.length;colMsIndex++)
			  {
			      if(typeof colMs[colMsIndex].formula !== "undefined")
			      {
			          countCol.push(colMs[colMsIndex]);
			      }
			  }
			  for(var countIndex = 0 ; countIndex < countCol.length ; countIndex++)
			  {
			        var patt1=new RegExp("#\\w+#","ig");
			        
                    var result = [];
                    var reg;
                    for(var jsonIndex = 0;jsonIndex<rows.length;jsonIndex++)
                    {
                    var str = countCol[countIndex].formula;//formula，列属性设置，判断这列是否为计算列，同时根据其传入的公式得出计算列的值
			        var str2 = countCol[countIndex].formula;
			        result = [];
                    while (result != null) 
                   {
                          result = patt1.exec(str);
                             if(result != null)
                                 {
                                     var s = result[0].split("#");
                                     var val = s[1];
                                     var rowc = rows[jsonIndex];
                                     var value = rowc[val];
                                     reg = new RegExp(result[0],"g");
                                  }
                          str2 = str2.replace(reg,value) ;


                   }
                    var t = eval(str2);
                    
                    var countName = countCol[countIndex].name;
                    
                    var row  = rows[jsonIndex];
                    
                    row[countName] = t;
                    
                    rows[jsonIndex] = row;
                  }
			  }
			  jsonData['rows'] = rows;
			  return jsonData;
			
			},
		sortArrayData = function() {
			var stripNum = /[\$,%]/g;
			var rows=[], col=0, st, sv, findSortKey,newDir = (ts.p.sortorder == "asc") ? 1 :-1;
			$.each(ts.p.colModel,function(i,v){
				if(this.index == ts.p.sortname || this.name == ts.p.sortname){
					col = ts.p.lastsort= i;
					st = this.sorttype;
					return false;
				}
			});
			if (st == 'float' || st== 'number' || st== 'currency') {
				findSortKey = function($cell) {
					var key = parseFloat($cell.replace(stripNum, ''));
					return isNaN(key) ? 0 : key;
				};
			} else if (st=='int' || st=='integer') {
				findSortKey = function($cell) {
					return IntNum($cell.replace(stripNum, ''));
				};
			} else if(st == 'date') {
				findSortKey = function($cell) {
					var fd = ts.p.colModel[col].datefmt || "Y-m-d";
					return parseDate(fd,$cell).getTime();
				};
			} else {
				findSortKey = function($cell) {
					return $.trim($cell.toUpperCase());
				};
			}
			$.each(ts.rows, function(index, row) {
				try { sv = $.unformat($(row).children('td').eq(col),{colModel:ts.p.colModel[col]},col,true);}
				catch (_) { sv = $(row).children('td').eq(col).text(); }
				row.sortKey = findSortKey(sv);
				rows[index] = this;
			});
			if(ts.p.treeGrid) {
				$(ts).jqGrid("SortTree",newDir);
			} else {
				rows.sort(function(a, b) {
					if (a.sortKey < b.sortKey) {return -newDir;}
					if (a.sortKey > b.sortKey) {return newDir;}
					return 0;
				});
				if(rows[0]){
					$("td",rows[0]).each( function( k ) {
						$(this).css("width",grid.headers[k].width+"px");
					});
					ts.grid.cols = rows[0].cells;
				}
				var cn = "";
				if(ts.p.altRows) cn = ts.p.altclass;
				$.each(rows, function(i, row) {
					if(cn) {
						if(i%2 ==1) $(row).addClass(cn);
						else $(row).removeClass(cn);
					}
					$('tbody',ts.grid.bDiv).append(row);
					row.sortKey = null;
				});
			}
			ts.grid.bDiv.scrollTop = 0;
		},
		parseDate = function(format, date) {
			var tsp = {m : 1, d : 1, y : 1970, h : 0, i : 0, s : 0},k,hl,dM;
			date = date.split(/[\\\/:_;.\t\T\s-]/);
			format = format.split(/[\\\/:_;.\t\T\s-]/);
			var dfmt  = $.jgrid.formatter.date.monthNames;
			for(k=0,hl=format.length;k<hl;k++){
				if(format[k] == 'M') {
					dM = $.inArray(date[k],dfmt);
					if(dM !== -1 && dM < 12){date[k] = dM+1;}
				}
				if(format[k] == 'F') {
					dM = $.inArray(date[k],dfmt);
					if(dM !== -1 && dM > 11){date[k] = dM+1-12;}
				}
				tsp[format[k].toLowerCase()] = parseInt(date[k],10);
			}
			tsp.m = parseInt(tsp.m,10)-1;
			var ty = tsp.y;
			if (ty >= 70 && ty <= 99) {tsp.y = 1900+tsp.y;}
			else if (ty >=0 && ty <=69) {tsp.y= 2000+tsp.y;}
			return new Date(tsp.y, tsp.m, tsp.d, tsp.h, tsp.i, tsp.s,0);
		},
		setPager = function () {
				var sep = "<td class='ui-pg-button ui-state-disabled' style='width:4px;'><span class='ui-separator'></span></td>", pgid = $(ts.p.pager).attr("id") || "pager", pginp = "", pgl = "<table cellspacing='0' cellpadding='0' border='0' style='table-layout:auto;' class='ui-pg-table'><tbody><tr>",pgc="<table cellspacing='0' cellpadding='0' border='0' style='table-layout:auto;' class='ui-pg-table'><tbody><tr>", str = "", pgcnt, lft, cent, rgt, twd, tdw, i, clearVals = function (onpaging) {
					if ($.isFunction(ts.p.onPaging)) {
						ts.p.onPaging(onpaging);
					}
					ts.p.selrow = null;
					if (ts.p.multiselect) {
						ts.p.selarrrow = [];
						$("#cb_" + $.jgrid.jqID(ts.p.id), ts.grid.hDiv).attr("checked", false);
					}
					ts.p.savedRow = [];
				};
				pgcnt = "pg_" + pgid;
				lft = pgid + "_left";
				cent = pgid + "_center";
				rgt = pgid + "_right";
				$(ts.p.pager).append("<div id='" + pgcnt + "' class='ui-pager-control' role='group'><table cellspacing='0' cellpadding='0' border='0' class='ui-pg-table' style='width:100%;' role='row'><tbody><tr><td id='" + cent + "' align='left' style='white-space:pre;' width = '40%'></td><td id='" + rgt + "' align='right' width = '55%'></td></tr></tbody></table></div>").attr("dir", "ltr");
				if (ts.p.rowList.length > 0) {//设置每页显示多少条
				    str ="<td dir='rlt'>&nbsp;&nbsp;&nbsp;&nbsp;每页显示&nbsp;&nbsp;</td>";
					str += "<td dir='rlt'>";
					str += "<input class='ui-pg-selbox' type='text' size='2' maxlength='7' value='0' role='textbox'/></td>";
					str +="<td dir='rlt'>&nbsp;&nbsp;条&nbsp;&nbsp;&nbsp;&nbsp;</td>";//select
					str += "<td>&nbsp;<input id=\"unitpage_set\" name=\"Submit\" type=\"button\" class=\"input_bg\" value=\"设置\"/>&nbsp;&nbsp;</td>";
				}
				if (dir == "rtl") {
					pgc += str;
				}
				if (ts.p.pginput === true) {//跳转到哪页
					pginp = "<td dir='" + dir + "'>" +"转到&nbsp;&nbsp;<input class='ui-pg-input' type='text' size='2' maxlength='7' value='0' role='textbox'/>&nbsp;&nbsp;页" + "</td>";
					pginp += "<td>&nbsp;<input id=\"curpage_set\" name=\"Submit\" type=\"button\" class=\"input_bg\" value=\"跳转\"/>&nbsp;&nbsp;</td>";
				}
				if (ts.p.pgbuttons === true) {
					var po = ["first", "prev", "next", "last"];
					if (dir == "rtl") {
						po.reverse();
					}
					pgl += "<td id='" + po[0] + "' class='ui-pg-button ui-corner-all'><span class='ui-icon ui-icon-seek-first'></span></td>";
					pgl += "<td id='" + po[1] + "' class='ui-pg-button ui-corner-all'><span class='ui-icon ui-icon-seek-prev'></span></td>";
					
					pgl += "<td id='" + po[2] + "' class='ui-pg-button ui-corner-all'><span class='ui-icon ui-icon-seek-next'></span></td>";
					pgl += "<td id='" + po[3] + "' class='ui-pg-button ui-corner-all'><span class='ui-icon ui-icon-seek-end'></span></td>";
				
				    pgl += pginp != "" ? sep + pginp + sep : "";
				} else {
					if (pginp != "") {
						pgl += pginp;
					}
				}
				
				pgl += "</tr></tbody></table>";
				pgc +="<td dir='" + dir + "'>当前&nbsp;&nbsp;&nbsp;&nbsp;<span id='cp_1'></span>/<span id='sp_1'></span>&nbsp;&nbsp;&nbsp;&nbsp;页</td>" ;
				if (dir == "ltr") {
				
					pgc += str;
				}
				pgc +="<td dir='" + dir + "'><div dir='" + dir + "' style='text-align:" + ts.p.recordpos + "' class='ui-paging-info'></div></td>";			
				pgc += "</tr></tbody></table>";
				//if (ts.p.viewrecords === true) {
					//$("td#" + pgid + "_" + ts.p.recordpos, "#" + pgcnt).append("<div dir='" + dir + "' style='text-align:" + ts.p.recordpos + "' class='ui-paging-info'></div>");
					$("td#" + pgid + "_" + ts.p.recordpos, "#" + pgcnt).append(pgc);
				//}
				$("td#" + pgid + "_" + ts.p.pagerpos, "#" + pgcnt).append(pgl);
				tdw = $(".ui-jqgrid").css("font-size") || "11px";
				$("body").append("<div id='testpg' class='ui-jqgrid ui-widget ui-widget-content' style='font-size:" + tdw + ";visibility:hidden;' ></div>");
				twd = $(pgl).clone().appendTo("#testpg").width();
				$("#testpg").remove();
				if (twd > 0) {
					if (pginp != "") {
						twd += 50;
					}
				//	$("td#" + pgid + "_" + ts.p.pagerpos, "#" + pgcnt).width(twd);
				}
				ts.p._nvtd = [];
				ts.p._nvtd[0] = twd ? Math.floor((ts.p.width - twd) / 2) : Math.floor(ts.p.width / 3);
				ts.p._nvtd[1] = 0;
				pgl = null;
				$(".ui-pg-selbox", "#" + pgcnt).bind("keydown", function (e) {
				    var key = e.charCode ? e.charCode : e.keyCode ? e.keyCode : 0;
				    if(key ==13)
				    {
					ts.p.page = Math.round(ts.p.rowNum * (ts.p.page - 1) / $(this).val() - 0.5) + 1;
				    if(!(typeof ts.p.page === "number" && isFinite(ts.p.page)))//控制输入的数不为负数或字母
				    {
				    ts.p.page = 1;
				    }
					ts.p.rowNum = ($(this).val()>0)?$(this).val():ts.p.rowNum;
					 
					clearVals("records");
					populate();
					return false;
					}
				});
				$("#unitpage_set","#" + pgcnt).click(function(){
				ts.p.page = Math.round(ts.p.rowNum * (ts.p.page - 1) / $(".ui-pg-selbox", "#" + pgcnt).val() - 0.5) + 1;
					  if(!(typeof ts.p.page === "number" && isFinite(ts.p.page)))
				    {
				    ts.p.page = 1;
				    }
					ts.p.rowNum = ($(".ui-pg-selbox", "#" + pgcnt).val()>0)?$(".ui-pg-selbox", "#" + pgcnt).val():ts.p.rowNum;
					
					clearVals("records");
					populate();
					return false;
				
				});
				if (ts.p.pgbuttons === true) {
					$(".ui-pg-button", "#" + pgcnt).hover(function (e) {
						if ($(this).hasClass("ui-state-disabled")) {
							this.style.cursor = "default";
						} else {
							$(this).addClass("ui-state-hover");
							this.style.cursor = "pointer";
						}
					}, function (e) {
						if ($(this).hasClass("ui-state-disabled")) {
						} else {
							$(this).removeClass("ui-state-hover");
							this.style.cursor = "default";
						}
					});
					$("#first, #prev, #next, #last", ts.p.pager).click(function (e) {
						var cp = IntNum(ts.p.page), last = IntNum(ts.p.lastpage), selclick = false, fp = true, pp = true, np = true, lp = true;
						if (last === 0 || last === 1) {
							fp = false;
							pp = false;
							np = false;
							lp = false;
						} else {
							if (last > 1 && cp >= 1) {
								if (cp === 1) {
									fp = false;
									pp = false;
								} else {
									if (cp > 1 && cp < last) {
									} else {
										if (cp === last) {
											np = false;
											lp = false;
										}
									}
								}
							} else {
								if (last > 1 && cp === 0) {
									np = false;
									lp = false;
									cp = last - 1;
								}
							}
						}
						if (this.id === "first" && fp) {
							ts.p.page = 1;
							selclick = true;
						}
						if (this.id === "prev" && pp) {
							ts.p.page = (cp - 1);
							selclick = true;
						}
						if (this.id === "next" && np) {
							ts.p.page = (cp + 1);
							selclick = true;
						}
						if (this.id === "last" && lp) {
							ts.p.page = last;
							selclick = true;
						}
						if (selclick) {
							clearVals(this.id);
							populate();
						}
						return false;
					});
				}
					
				if (ts.p.pginput === true) {
						$("input.ui-pg-input", "#" + pgcnt).keypress(function (e) {
						var key = e.charCode ? e.charCode : e.keyCode ? e.keyCode : 0;
						if (key == 13) {
							ts.p.page = ($(this).val() > 0) ? $(this).val() : ts.p.page;
								  if(!(typeof ts.p.page === "number" && isFinite(ts.p.page)))//控制输入的数不为负数或字母
				    {
				    ts.p.page = 1;
				    }
							var totalpage = Number(ts.p.lastpage);//控制当输入的数大于总页数的时候，显示最后一页的数据
							if(ts.p.page>totalpage)
							{
							ts.p.page=totalpage;
							}
							clearVals("user");
							populate();
							return false;
						}
						return this;
					});
					$("#curpage_set").click(function(){//添加跳转按钮单击事件
					var value = $("input.ui-pg-input", "#" + pgcnt).val();
						ts.p.page = (value > 0) ? value : ts.p.page;
						var totalpage = Number(ts.p.lastpage);
							if(ts.p.page>totalpage)
							{
							ts.p.page=totalpage;
							}
							clearVals("user");
							populate();
							return false;
					
					});
				}
			},
			//页内排序功能
			getParserById = function (name) {
				var l = parsers.length;
				for(var i=0; i < l; i++) {
					if(parsers[i].id.toLowerCase() == name.toLowerCase()) {	
						return parsers[i];
					}
				}
				return false;
			}, multisort = function (sortList) {
				
				var dynamicExp = "var sortWrapper = function(a,b) {", l = sortList.length;
					
				for(var i=0; i < l; i++) {
					var c = sortList[i][0];
					var order = sortList[i][1];
					var s = (getCachedSortType(ts.p.parsers, c) == "text") ? ((order == "asc") ? "sortText" : "sortTextDesc") : ((order == "asc") ? "sortNumeric" : "sortNumericDesc");
					
					var e = "e" + i;
					
					dynamicExp += "var " + e + " = " + s + "(a[" + c + "],b[" + c + "]);";
					dynamicExp += "if(" + e + ") { return " + e + "; } ";
					dynamicExp += "else { ";
				}
					
				for(var i=0; i < l; i++) {
					dynamicExp += "}; ";
				}
				
				dynamicExp += "return 0; ";	
				dynamicExp += "}; ";	
				
				eval(dynamicExp);
				ts.p.cache.normalized.sort(sortWrapper);
			}, sortText = function (a, b) {
				return ((a < b) ? -1 : ((a > b) ? 1 : 0));
			}, sortTextDesc = function (a, b) {
				return ((b < a) ? -1 : ((b > a) ? 1 : 0));
			}, sortNumeric = function (a, b) {
				return a-b;
			}, sortNumericDesc = function (a, b) {
				return b-a;
			}, getCachedSortType = function (parsers, i) {
				return parsers[i].type;
			}, buildParserCache = function () {
				if(ts.tBodies[0].rows && ts.tBodies[0].rows.length){//判断当前表格是否有数据行
					var cells = ts.tBodies[0].rows[0].cells, l = cells.length;
					for (var i=0;i < l; i++) {
						var p = false;
						if((ts.p.colModel[i] && ts.p.colModel[i].sorter)) {
							p = getParserById(ts.p.colModel[i].sorter);
						}
						ts.p.parsers.push(p);
					}
				}
			}, buildCache = function () {
				var totalRows = (ts.tBodies[0] && ts.tBodies[0].rows.length) || 0,
					//数据为空时的异常处理：totalCells = ts.tBodies[0].rows[0].cells.length
					totalCells = (ts.tBodies[0] && ts.tBodies[0].rows.length && ts.tBodies[0].rows[0].cells.length) || 0,
					parsers = ts.p.parsers,
					cache = {row: [], normalized: []};
				
				for (var i=0;i < totalRows; i++) {
				
					/** Add the table data to main data array */
					var c = ts.tBodies[0].rows[i], cols = [];
				
					cache.row.push($(c));
					for(var j=0; j < totalCells; j++) {
						if(parsers[j]){
							cols.push(parsers[j].format(c.cells[j].innerHTML, ts, c.cells[j]));
						}else{
							cols.push(false);
						}
					}

					cols.push(i); // add position for rowCache
					cache.normalized.push(cols);
					cols = null;
				};
				ts.p.cache = cache;
			}, appendToTable = function () {
				
				var c = ts.p.cache, 
					r = c.row, 
					n= c.normalized, 
					totalRows = n.length, 
					checkCell = (n[0].length-1), 
					tableBody = $("tbody:first", ts).empty();
					rows = [];
				var firstRow = r[0];//缓存第一行数据
				for (var i=0;i < totalRows; i++) {
				 	rows.push(r[n[i][checkCell]]);
					tableBody.append(r[n[i][checkCell]]);
				}
				var totalCells = firstRow[0].cells.length;
				ts.grid.cols = rows[0][0].cells;//排序后替换ts.grid.cols为排序后第一行的值，解决排序后的列拖拽异常
				for(var i=0; i<totalCells; i++){//将原来第一行的数据列宽赋予排序后的第一行数据
					ts.grid.cols[i].style.width = firstRow[0].cells[i].style.width;
				}
				rows = null;
				
				$("tbody:first > tr", ts).each(function(j){//渲染排序后的斑马线效果
					if(ts.p.altRows){//启用了斑马线设置
				        if(j%2==0){
				            if($(this).hasClass(ts.p.altclass)){
				               $(this).removeClass(ts.p.altclass);
				            }
			            }else{
				            if(!$(this).hasClass(ts.p.altclass)){
				               $(this).addClass(ts.p.altclass);
				            }
			            }
					}
				});
				if(ts.p.isTips)//添加tips功能
									{
									    var tipClass = ts.p.tipsClass;
									   	var tds = $("td",ts)
	                                    tds.Tooltip({
	                                       showURL: false,
                                           extraClass: tipClass,
	                                       opacity: 0.95		                                    
	                                    });
									}
			},			
			
		sortData = function (index, idxcol,reload,sor){
			if(!ts.p.colModel[idxcol].sortable) return;
			var imgs, so;
			if(ts.p.savedRow.length > 0) {return;}
			if(!reload) {
				if( ts.p.lastsort == idxcol ) {
					if( ts.p.sortorder == 'asc') {
						ts.p.sortorder = 'desc';
					} else if(ts.p.sortorder == 'desc') { ts.p.sortorder = 'asc';}
				} else { ts.p.sortorder = 'asc';}
				ts.p.page = 1;
			}
			if(sor) {
				if(ts.p.lastsort == idxcol && ts.p.sortorder == sor) return;
				else ts.p.sortorder = sor;
			}
			var thd= $("thead:first",ts.grid.hDiv).get(0);
			$("tr th:eq("+ts.p.lastsort+") span.ui-grid-ico-sort",thd).addClass('ui-state-disabled');
			$("tr th:eq("+ts.p.lastsort+")",thd).attr("aria-selected","false");
			$("tr th:eq("+idxcol+") span.ui-icon-"+ts.p.sortorder,thd).removeClass('ui-state-disabled');
			$("tr th:eq("+idxcol+")",thd).attr("aria-selected","true");
			if(!ts.p.viewsortcols[0]) {
				if(ts.p.lastsort != idxcol) {
					$("tr th:eq("+ts.p.lastsort+") span.s-ico",thd).hide();
					$("tr th:eq("+idxcol+") span.s-ico",thd).show();
				}
			}
			ts.p.lastsort = idxcol;
			index = index.substring(5);
			ts.p.sortname = ts.p.colModel[idxcol].index || index;
			so = ts.p.sortorder;
			if($.isFunction(ts.p.onSortCol)) {ts.p.onSortCol.call(ts,index,idxcol,so);}
			
			if(ts.p.orderType && ts.p.orderType == "server"){//全局排序
				if(ts.p.datatype == "local") {
					if(ts.p.deselectAfterSort) {$(ts).jqGrid("resetSelection");}
				} else {
					ts.p.selrow = null;
					if(ts.p.multiselect){$("#cb_"+$.jgrid.jqID(ts.p.id),ts.grid.hDiv).attr("checked",false);}
					ts.p.selarrrow =[];
					ts.p.savedRow =[];
					if(ts.p.scroll) {emptyRows(ts.grid.bDiv,true);}
				}
				if(ts.p.subGrid && ts.p.datatype=='local') {
					$("td.sgexpanded","#"+ts.p.id).each(function(){
						$(this).trigger("click");
					});
				}
				populate();
			}else{//本页排序，ts.p.orderType = "page"
				if(ts.tBodies[0] && ts.tBodies[0].rows.length){//处理数据行为空时的异常
					if(!ts.p.parsers || ts.p.parsers.length <= 1){
						buildParserCache();
					}
					if(!ts.p.cache || ts.p.cache.length <= 1){
						buildCache();
					}
					var sortList = [[idxcol, ts.p.sortorder]];
					multisort(sortList);
					appendToTable();
				}
			}

			if(ts.p.sortname != index && idxcol) {ts.p.lastsort = idxcol;}
		},
		setColWidth = function () {
			var initwidth = 0, brd=ts.p.cellLayout, vc=0, lvc, scw=ts.p.scrollOffset,cw,hs=false,aw,tw=0,gw=0,
			cl = 0, cr;
			if (isSafari) { brd=0; }
			$.each(ts.p.colModel, function(i) {
				if(typeof this.hidden === 'undefined') {this.hidden=false;}
				if(this.hidden===false){
					initwidth += IntNum(this.width);
					if(this.fixed) {
						tw += this.width;
						gw += this.width+brd;
					} else {
						vc++;
					}
					cl++;
				}
			});
			if(isNaN(ts.p.width)) {ts.p.width = grid.width = initwidth;}
			else { grid.width = ts.p.width}
			ts.p.tblwidth = initwidth;
			if(ts.p.shrinkToFit ===false && ts.p.forceFit === true) {ts.p.forceFit=false;}
			if(ts.p.shrinkToFit===true && vc > 0) {
				aw = grid.width-brd*vc-gw;
				if(isNaN(ts.p.height)) {
				} else {
					aw -= scw;
					hs = true;
				}
				initwidth =0;
				$.each(ts.p.colModel, function(i) {
					if(this.hidden === false && !this.fixed){
						cw = Math.floor(aw/(ts.p.tblwidth-tw)*this.width);
						this.width =cw;
						initwidth += cw;
						lvc = i;
					}
				});
				cr =0;
				if (hs) {
					if(grid.width-gw-(initwidth+brd*vc) !== scw)
						cr = grid.width-gw-(initwidth+brd*vc)-scw;
				} else if(!hs && Math.abs(grid.width-gw-(initwidth+brd*vc)) !== 1) {
					cr = grid.width-gw-(initwidth+brd*vc);
				}
				ts.p.colModel[lvc].width += cr;
				ts.p.tblwidth = initwidth+cr+tw+cl*brd;
			}
		},
		nextVisible= function(iCol) {
			var ret = iCol, j=iCol, i;
			for (i = iCol+1;i<ts.p.colModel.length;i++){
				if(ts.p.colModel[i].hidden !== true ) {
					j=i; break;
				}
			}
			return j-ret;
		},
		getOffset = function (iCol) {
			var i, ret = {}, brd1 = isSafari ? 0 : ts.p.cellLayout;
			ret[0] =  ret[1] = ret[2] = 0;
			for(i=0;i<=iCol;i++){
				if(ts.p.colModel[i].hidden === false ) {
					ret[0] += ts.p.colModel[i].width+brd1;
				}
			}
			if(ts.p.direction=="rtl") ret[0] = ts.p.width - ret[0];
			ret[0] = ret[0] - ts.grid.bDiv.scrollLeft;
			if($(ts.grid.cDiv).is(":visible")) {ret[1] += $(ts.grid.cDiv).height() +parseInt($(ts.grid.cDiv).css("padding-top"))+parseInt($(ts.grid.cDiv).css("padding-bottom"));}
			if(ts.p.toolbar[0]==true && (ts.p.toolbar[1]=='top' || ts.p.toolbar[1]=='both')) {ret[1] += $(ts.grid.uDiv).height()+parseInt($(ts.grid.uDiv).css("border-top-width"))+parseInt($(ts.grid.uDiv).css("border-bottom-width"));}
			ret[2] += $(ts.grid.bDiv).height() + $(ts.grid.hDiv).height();
			return ret;
		};
		this.p.id = this.id;
		if ($.inArray(ts.p.multikey,sortkeys) == -1 ) {ts.p.multikey = false;}
		ts.p.keyIndex=false;
		for (i=0; i<ts.p.colModel.length;i++) {
			if (ts.p.colModel[i].key===true) {
				ts.p.keyIndex = i;
				break;
			}
		}
		ts.p.sortorder = ts.p.sortorder.toLowerCase();
		if(this.p.treeGrid === true) {
			try { $(this).jqGrid("setTreeGrid");} catch (_) {}
		}
		if(this.p.subGrid) {
			try { $(ts).jqGrid("setSubGrid");} catch (_){}
		}
		if(this.p.multiselect) {
			this.p.colNames.unshift("<input id='cb_"+this.p.id+"' class='cbox' type='checkbox'/>");
			this.p.colModel.unshift({name:'cb',width:isSafari ? ts.p.multiselectWidth+ts.p.cellLayout : ts.p.multiselectWidth,sortable:false,resizable:false,hidedlg:true,search:false,align:'center',fixed:true});
		}
		if(this.p.radioselect){
			this.p.colNames.unshift("");
				this.p.colModel.unshift({name:"rb", width:ts.p.rownumWidth, sortable:false, resizable:false, hidedlg:true, search:false, align:"center", fixed:true});
			}
		if(this.p.rownumbers) {
			this.p.colNames.unshift("");
			this.p.colModel.unshift({name:'rn',width:ts.p.rownumWidth,sortable:false,resizable:false,hidedlg:true,search:false,align:'center',fixed:true});
		}
		ts.p.xmlReader = $.extend({
			root: "rows",
			row: "row",
			page: "rows>page",
			total: "rows>total",
			records : "rows>records",
			repeatitems: true,
			cell: "cell",
			id: "[id]",
			userdata: "userdata",
			subgrid: {root:"rows", row: "row", repeatitems: true, cell:"cell"}
		}, ts.p.xmlReader);
		ts.p.jsonReader = $.extend({
			root: "rows",
			page: "page",
			total: "total",
			records: "records",
			repeatitems: true,
			cell: "cell",
			id: "id",
			userdata: "userdata",
			subgrid: {root:"rows", repeatitems: true, cell:"cell"}
		},ts.p.jsonReader);
		if(ts.p.scroll){
			ts.p.pgbuttons = false; ts.p.pginput=false; ts.p.rowList=[];
		}
		var thead = "<thead><tr class='ui-jqgrid-labels' role='rowheader'>",
		tdc, idn, w, res, sort,
		td, ptr, tbody, imgs,iac="",idc="";
		if(ts.p.shrinkToFit===true && ts.p.forceFit===true) {
			for (i=ts.p.colModel.length-1;i>=0;i--){
				if(!ts.p.colModel[i].hidden) {
					ts.p.colModel[i].resizable=false;
					break;
				}
			}
		}
		if(ts.p.viewsortcols[1] == 'horizontal') {iac=" ui-i-asc";idc=" ui-i-desc";}
		tdc = isMSIE ?  "class='ui-th-div-ie'" :"";
		imgs = "<span class='s-ico' style='display:none'><span sort='asc' class='ui-grid-ico-sort ui-icon-asc"+iac+" ui-state-disabled ui-icon ui-icon-triangle-1-n ui-sort-"+dir+"'></span>";
		imgs += "<span sort='desc' class='ui-grid-ico-sort ui-icon-desc"+idc+" ui-state-disabled ui-icon ui-icon-triangle-1-s ui-sort-"+dir+"'></span></span>";
		for(i=0;i<this.p.colNames.length;i++){
			thead += "<th role='columnheader' class='ui-state-default ui-th-column ui-th-"+dir+"'>";
			idn = ts.p.colModel[i].index || ts.p.colModel[i].name;
			thead += "<div id='jqgh_"+ts.p.colModel[i].name+"' "+tdc+">"+ts.p.colNames[i];
			if (idn == ts.p.sortname) {
				ts.p.lastsort = i;
			} 
			thead += imgs+"</div></th>";
		}
		thead += "</tr></thead>";
		$(this).append(thead);
		$("thead tr:first th",this).hover(function(){$(this).addClass('ui-state-hover');},function(){$(this).removeClass('ui-state-hover');});
		if(this.p.multiselect) {
			var onSA = true, emp=[], chk;
			if(typeof ts.p.onSelectAll !== 'function') {onSA=false;}
			$('#cb_'+$.jgrid.jqID(ts.p.id),this).bind('click',function(){
				if (this.checked) {
					$("[id^=jqg_]",ts.rows).attr("checked",true);
					$(ts.rows).each(function(i) {
						if(!$(this).hasClass("subgrid")){
						$(this).addClass("ui-state-highlight").attr("aria-selected","true");
						ts.p.selarrrow[i]= ts.p.selrow = this.id; 
						}
					});
					chk=true;
					emp=[];
				}
				else {
					$("[id^=jqg_]",ts.rows).attr("checked",false);
					$(ts.rows).each(function(i) {
						if(!$(this).hasClass("subgrid")){
							$(this).removeClass("ui-state-highlight").attr("aria-selected","false");
							emp[i] = this.id;
						}
					});
					ts.p.selarrrow = []; ts.p.selrow = null;
					chk=false;
				}
				if(onSA) {ts.p.onSelectAll(chk ? ts.p.selarrrow : emp,chk);}
				if(ts.p.selectInfo !==null && ts.p.toolbar[0])
				{
				var udata = $("#"+$.jgrid.jqID(ts.p.id)).jqGrid('getRowNums');
		                 if(udata == null)
		                     {
		                     udata = "";
		                     }
			               $("#t_"+$.jgrid.jqID(ts.p.id)).css("text-align", "left").html("已选择：" + udata);
			               }
			});
		}
		$.each(ts.p.colModel, function(i){if(!this.width) {this.width=150;} this.width = parseInt(this.width);});
		if(ts.p.autowidth===true) {
			var pw = $(eg).innerWidth();
			ts.p.width = pw > 0?  pw: 'nw';
		}
		setColWidth();
		$(eg).css("width",grid.width+"px").append("<div class='ui-jqgrid-resize-mark' id='rs_m"+ts.p.id+"'>&#160;</div>");
		$(gv).css("width",grid.width+"px");
		thead = $("thead:first",ts).get(0);
		var	tfoot = "<table role='grid' style='width:"+ts.p.tblwidth+"px' class='ui-jqgrid-ftable' cellspacing='0' cellpadding='0' border='0'><tbody><tr role='row' class='ui-widget-content footrow footrow-"+dir+"'>";
		var thr = $("tr:first",thead);
		ts.p.disableClick=false;
		$("th",thr).each(function ( j ) {
			var ht = $('div',this)[0];
			w = ts.p.colModel[j].width;
			if(typeof ts.p.colModel[j].resizable === 'undefined') {ts.p.colModel[j].resizable = true;}
			if(ts.p.colModel[j].resizable){
				res = document.createElement("span");
				$(res).html("&#160;").addClass('ui-jqgrid-resize ui-jqgrid-resize-'+dir);
				!$.browser.opera ? $(res).css("cursor","col-resize") : "";
				$(this).addClass(ts.p.resizeclass);
			} else {
				res = "";
			}
			$(this).css("width",w+"px").prepend(res);
			if( ts.p.colModel[j].hidden ) $(this).css("display","none");
			grid.headers[j] = { width: w, el: this };
			sort = ts.p.colModel[j].sortable;
			if( typeof sort !== 'boolean') {ts.p.colModel[j].sortable =  true; sort=true;}
			var nm = ts.p.colModel[j].name;
			if( !(nm == 'cb' || nm=='subgrid' || nm=='rn' || nm == 'rb') ) {//添加单选框判断
				if(ts.p.viewsortcols[2])
					$("div",this).addClass('ui-jqgrid-sortable');
			}
			if(sort) {
				if(ts.p.viewsortcols[0]) {$("div span.s-ico",this).show(); if(j==ts.p.lastsort){ $("div span.ui-icon-"+ts.p.sortorder,this).removeClass("ui-state-disabled");}}
				else if( j == ts.p.lastsort) {$("div span.s-ico",this).show();$("div span.ui-icon-"+ts.p.sortorder,this).removeClass("ui-state-disabled");}
			}
			tfoot += "<td role='gridcell' "+formatCol(j,0)+">&#160;</td>";
		}).mousedown(function(e) {
			if ($(e.target).closest("th>span.ui-jqgrid-resize").length != 1) return;
			var ci = $.jgrid.getCellIndex(this);
			if(ts.p.forceFit===true) {ts.p.nv= nextVisible(ci);}
			grid.dragStart(ci, e, getOffset(ci));
			return false;
		}).click(function(e) {
			if (ts.p.disableClick) {
				ts.p.disableClick = false;
				return false;
			}
			var s = "th>div.ui-jqgrid-sortable",r,d;
			if (!ts.p.viewsortcols[2]) { s = "th>div>span>span.ui-grid-ico-sort" }
			var t = $(e.target).closest(s);
			if (t.length != 1) return;
			var ci = $.jgrid.getCellIndex(this);
			if (!ts.p.viewsortcols[2]) { r=true,d=t.attr("sort") }
			sortData($('div',this)[0].id,ci,r,d);
			return false;
		});
		if (ts.p.sortable && $.fn.sortable) {
			try {
				$(ts).jqGrid("sortableColumns", thr);
			} catch (e){}
		}
		tfoot += "</tr></tbody></table>";
		
		tbody = document.createElement("tbody");
		this.appendChild(tbody);
		$(this).addClass('ui-jqgrid-btable');
		var hTable = $("<table class='ui-jqgrid-htable' style='width:"+ts.p.tblwidth+"px' role='grid' aria-labelledby='gbox_"+this.id+"' cellspacing='0' cellpadding='0' border='0'></table>").append(thead),
		hg = (ts.p.caption && ts.p.hiddengrid===true) ? true : false,
		hb = $("<div class='ui-jqgrid-hbox" + (dir=="rtl" ? "-rtl" : "" )+"'></div>");
		grid.hDiv = document.createElement("div");
		$(grid.hDiv)
			.css({ width: grid.width+"px"})
			.addClass("ui-state-default ui-jqgrid-hdiv")
			.append(hb);
		$(hb).append(hTable);
		if(hg) $(grid.hDiv).hide();
		ts.p._height =0;
		if(ts.p.pager){
			if(typeof ts.p.pager == "string") {if(ts.p.pager.substr(0,1) !="#") ts.p.pager = "#"+ts.p.pager;}
			$(ts.p.pager).css({width: grid.width+"px"}).appendTo(eg).addClass('ui-state-default ui-jqgrid-pager');
			ts.p._height += parseInt($(ts.p.pager).height(),10);
			if(hg) {$(ts.p.pager).hide();}
			setPager();
		}
		if( ts.p.cellEdit === false && ts.p.hoverrows === true) {
		$(ts).bind('mouseover',function(e) {
			ptr = $(e.target).closest("tr.jqgrow");
			if($(ptr).attr("class") !== "subgrid") {
				$(ptr).addClass("ui-state-hover");
			}
			return false;
		}).bind('mouseout',function(e) {
			ptr = $(e.target).closest("tr.jqgrow");
			$(ptr).removeClass("ui-state-hover");
			return false;
		});
		}
		var ri,ci;
		$(ts).before(grid.hDiv).click(function(e) {
			td = e.target;
			var scb = $(td).hasClass("cbox");
			ptr = $(td,ts.rows).closest("tr.jqgrow");
			if($(ptr).length === 0 ) {
				return this;
			}
			var cSel = true;
			if($.isFunction(ts.p.beforeSelectRow)) cSel = ts.p.beforeSelectRow.call(ts,ptr[0].id, e);
			if (td.tagName == 'A' || ((td.tagName == 'INPUT' || td.tagName == 'TEXTAREA' || td.tagName == 'OPTION' || td.tagName == 'SELECT' ) && !scb) ) { return true; }
			if(cSel === true) {
				if(ts.p.cellEdit === true) {
					if(ts.p.multiselect && scb){
						$(ts).jqGrid("setSelection",ptr[0].id,true);
					} else {
						ri = ptr[0].rowIndex;
						ci = $.jgrid.getCellIndex(td);
						try {$(ts).jqGrid("editCell",ri,ci,true);} catch (_) {}
					}
				} else if ( !ts.p.multikey ) {
					if(ts.p.multiselect && ts.p.multiboxonly) {
						if(scb){$(ts).jqGrid("setSelection",ptr[0].id,true);}
						else {
							$(ts.p.selarrrow).each(function(i,n){
								var ind = ts.rows.namedItem(n);
								$(ind).removeClass("ui-state-highlight");
								$("#jqg_"+$.jgrid.jqID(n),ind).attr("checked",false);
							});
							ts.p.selarrrow = [];
							$("#cb_"+$.jgrid.jqID(ts.p.id),ts.grid.hDiv).attr("checked",false);
							$(ts).jqGrid("setSelection",ptr[0].id,true);
						}
					} else {
						$(ts).jqGrid("setSelection",ptr[0].id,true);
					}
				} else {
					if(e[ts.p.multikey]) {
						$(ts).jqGrid("setSelection",ptr[0].id,true);
					} else if(ts.p.multiselect && scb) {
						scb = $("[id^=jqg_]",ptr).attr("checked");
						$("[id^=jqg_]",ptr).attr("checked",!scb);
					}
				}
				if($.isFunction(ts.p.onCellSelect)) {
					ri = ptr[0].id;
					ci = $.jgrid.getCellIndex(td);
					ts.p.onCellSelect.call(ts,ri,ci,$(td).html(),e);
				}
			}
			e.stopPropagation();
		}).bind('reloadGrid', function(e,opts) {
			if(ts.p.treeGrid ===true) {	ts.p.datatype = ts.p.treedatatype;}
			if (opts && opts.current) {
				ts.grid.selectionPreserver(ts);
			}
			if(ts.p.datatype=="local"){ $(ts).jqGrid("resetSelection");}
			else if(!ts.p.treeGrid) {
				ts.p.selrow=null;
				if(ts.p.multiselect) {ts.p.selarrrow =[];$('#cb_'+$.jgrid.jqID(ts.p.id),ts.grid.hDiv).attr("checked",false);}
				ts.p.savedRow = [];
				if(ts.p.scroll) {emptyRows(ts.grid.bDiv);}
			}
			if (opts && opts.page) {
				var page = opts.page;
				if (page > ts.p.lastpage) page = ts.p.lastpage;
				if (page < 1) page = 1;
				ts.p.page = page;
				if (ts.grid.prevRowHeight) {
					ts.grid.bDiv.scrollTop = (page - 1) * ts.grid.prevRowHeight * ts.p.rowNum;
				} else {
					ts.grid.bDiv.scrollTop = 0;
				}
			}
			if (ts.grid.prevRowHeight && ts.p.scroll) {
                delete ts.p.lastpage;
				ts.grid.populateVisible();
			} else
				ts.grid.populate();
			return false;
		});
		if( $.isFunction(this.p.ondblClickRow) ) {
			$(this).dblclick(function(e) {
				td = e.target;
				ptr = $(td,ts.rows).closest("tr.jqgrow");
				if($(ptr).length === 0 ){return false;}
				ri = ptr[0].rowIndex;
				ci = $.jgrid.getCellIndex(td);
				ts.p.ondblClickRow.call(ts,$(ptr).attr("id"),ri,ci, e);
				return false;
			});
		}
		if ($.isFunction(this.p.onRightClickRow)) {
			$(this).bind('contextmenu', function(e) {
				td = e.target;
				ptr = $(td,ts.rows).closest("tr.jqgrow");
				if($(ptr).length === 0 ){return false;}
				if(!ts.p.multiselect) {	$(ts).jqGrid("setSelection",ptr[0].id,true);	}
				if(!$('input',ptr).attr('checked'))//当用户右击行的时候选中该行
					{
						$(ts).jqGrid("setSelection", ptr[0].id, true);
						}
				ri = ptr[0].rowIndex;
				ci = $.jgrid.getCellIndex(td);
				//var res = $(ts).jqGrid("getRowValues",$(ptr).attr("id"));//得到某一行的数据	
					ts.p.onRightClickRow.call(ts, $(ptr).attr("id"), null, ri, ci, e);
				return false;
			});
		}
		grid.bDiv = document.createElement("div");
		$(grid.bDiv)
			.append($('<div style="position:relative"></div>').append('<div></div>').append(this))
			.addClass("ui-jqgrid-bdiv")
			.css({ height: ts.p.height+(isNaN(ts.p.height)?"":"px"), width: (grid.width)+"px"})
			.scroll(grid.scrollGrid);
		$("table:first",grid.bDiv).css({width:ts.p.tblwidth+"px"});
		if( isMSIE ) {
			if( $("tbody",this).size() == 2 ) { $("tbody:first",this).remove();}
			if( ts.p.multikey) {$(grid.bDiv).bind("selectstart",function(){return false;});}
		} else {
			if( ts.p.multikey) {$(grid.bDiv).bind("mousedown",function(){return false;});}
		}
		if(hg) {$(grid.bDiv).hide();}
		grid.cDiv = document.createElement("div");
		var arf = ts.p.hidegrid===true ? $("<a role='link' href='javascript:void(0)'/>").addClass('ui-jqgrid-titlebar-close HeaderButton').hover(
			function(){ arf.addClass('ui-state-hover');},
			function() {arf.removeClass('ui-state-hover');})
		.append("<span class='ui-icon ui-icon-circle-triangle-n'></span>").css((dir=="rtl"?"left":"right"),"0px") : "";
		$(grid.cDiv).append(arf).append("<span class='ui-jqgrid-title"+(dir=="rtl" ? "-rtl" :"" )+"'>"+ts.p.caption+"</span>")
		.addClass("ui-jqgrid-titlebar ui-widget-header ui-corner-tl ui-corner-tr ui-helper-clearfix");
		$(grid.cDiv).insertBefore(grid.hDiv);
		if( ts.p.toolbar[0] ) {
			grid.uDiv = document.createElement("div");
			if(ts.p.toolbar[1] == "top") {$(grid.uDiv).insertBefore(grid.hDiv);}
			else if (ts.p.toolbar[1]=="bottom" ) {$(grid.uDiv).insertAfter(grid.hDiv);}
			if(ts.p.toolbar[1]=="both") {
				grid.ubDiv = document.createElement("div");
				$(grid.uDiv).insertBefore(grid.hDiv).addClass("ui-userdata ui-state-default").attr("id","t_"+this.id);
				$(grid.ubDiv).insertAfter(grid.hDiv).addClass("ui-userdata ui-state-default").attr("id","tb_"+this.id);
				ts.p._height += IntNum($(grid.ubDiv).height());
				if(hg)  {$(grid.ubDiv).hide();}
			} else {
				$(grid.uDiv).width(grid.width).addClass("ui-userdata ui-state-default").attr("id","t_"+this.id);
			}
			ts.p._height += IntNum($(grid.uDiv).height());
			if(hg) {$(grid.uDiv).hide();}
		}
		if(ts.p.footerrow) {
			grid.sDiv = $("<div class='ui-jqgrid-sdiv'></div>")[0];
			hb = $("<div class='ui-jqgrid-hbox"+(dir=="rtl"?"-rtl":"")+"'></div>");
			$(grid.sDiv).append(hb).insertAfter(grid.hDiv).width(grid.width);
			$(hb).append(tfoot);
			grid.footers = $(".ui-jqgrid-ftable",grid.sDiv)[0].rows[0].cells;
			if(ts.p.rownumbers) grid.footers[0].className = 'ui-state-default jqgrid-rownum';
			if(hg) {$(grid.sDiv).hide();}
		}
		if(ts.p.caption) {
			ts.p._height += parseInt($(grid.cDiv,ts).height(),10);
			var tdt = ts.p.datatype;
			if(ts.p.hidegrid===true) {
				$(".ui-jqgrid-titlebar-close",grid.cDiv).click( function(e){
					var onHdCl = $.isFunction(ts.p.onHeaderClick);
					if(ts.p.gridstate == 'visible') {
						$(".ui-jqgrid-bdiv, .ui-jqgrid-hdiv","#gview_"+ts.p.id).slideUp("fast");
						if(ts.p.pager) {$(ts.p.pager).slideUp("fast");}
						if(ts.p.toolbar[0]===true) {
							if( ts.p.toolbar[1]=='both') {
								$(grid.ubDiv).slideUp("fast");
							}
							$(grid.uDiv).slideUp("fast");
						}
						if(ts.p.footerrow) $(".ui-jqgrid-sdiv","#gbox_"+ts.p.id).slideUp("fast");
						$("span",this).removeClass("ui-icon-circle-triangle-n").addClass("ui-icon-circle-triangle-s");
						ts.p.gridstate = 'hidden';
						if(onHdCl) {if(!hg) {ts.p.onHeaderClick.call(ts,ts.p.gridstate,e);}}
					} else if(ts.p.gridstate == 'hidden'){
						$(".ui-jqgrid-hdiv, .ui-jqgrid-bdiv","#gview_"+ts.p.id).slideDown("fast");
						if(ts.p.pager) {$(ts.p.pager).slideDown("fast");}
						if(ts.p.toolbar[0]===true) {
							if( ts.p.toolbar[1]=='both') {
								$(grid.ubDiv).slideDown("fast");
							}
							$(grid.uDiv).slideDown("fast");
						}
						if(ts.p.footerrow) $(".ui-jqgrid-sdiv","#gbox_"+ts.p.id).slideDown("fast");
						$("span",this).removeClass("ui-icon-circle-triangle-s").addClass("ui-icon-circle-triangle-n");
						if(hg) {ts.p.datatype = tdt;populate();hg=false;}
						ts.p.gridstate = 'visible';
						if(onHdCl) {ts.p.onHeaderClick.call(ts,ts.p.gridstate,e)}
					}
					return false;
				});
				if(hg) {ts.p.datatype="local"; $(".ui-jqgrid-titlebar-close",grid.cDiv).trigger("click");}
			}
		} else {$(grid.cDiv).hide();}
		$(grid.hDiv).after(grid.bDiv)
		.mousemove(function (e) {
			if(grid.resizing){grid.dragMove(e);return false;}
		});
		$(".ui-jqgrid-labels",grid.hDiv).bind("selectstart", function () { return false; });
		ts.p._height += parseInt($(grid.hDiv).height(),10);
		$(document).mouseup(function (e) {
			if(grid.resizing) {	grid.dragEnd(); return false;}
			return true;
		});
		this.updateColumns = function () {
			var r = this.rows[0], self =this;
			if(r) {
				$("td",r).each( function( k ) {
					$(this).css("width",self.grid.headers[k].width+"px");
				});
				this.grid.cols = r.cells;
			}
			return this;
		}
		ts.formatCol = formatCol;
		ts.sortData = sortData;
		ts.updatepager = updatepager;
		ts.formatter = function ( rowId, cellval , colpos, rwdat, act){return formatter(rowId, cellval , colpos, rwdat, act);};
		$.extend(grid,{populate : populate, emptyRows: emptyRows});
		this.grid = grid;
		ts.addXmlData = function(d) {addXmlData(d,ts.grid.bDiv);};
		ts.addJSONData = function(d) {addJSONData(d,ts.grid.bDiv);};
		populate();ts.p.hiddengrid=false;
		$(window).unload(function () {
			$(this).empty();
			this.grid = null;
			this.p = null;
		});
	});
};
$.jgrid.extend({
	getGridParam : function(pName) {
		var $t = this[0];
		if (!$t.grid) {return;}
		if (!pName) { return $t.p; }
		else {return typeof($t.p[pName]) != "undefined" ? $t.p[pName] : null;}
	},
	setGridParam : function (newParams){
		return this.each(function(){
			if (this.grid && typeof(newParams) === 'object') {$.extend(true,this.p,newParams);}
		});
	},
	getDataIDs : function () {
		var ids=[], i=0, len;
		this.each(function(){
			len = this.rows.length;
			if(len && len>0){
				while(i<len) {
					ids[i] = this.rows[i].id;
					i++;
				}
			}
		});
		return ids;
	},
	setSelection : function(selection,onsr) {
		return this.each(function(){
			var $t = this, stat,pt, olr, ner, ia, tpsr;
			if(selection === undefined) return;
			onsr = onsr === false ? false : true;
			pt=$t.rows.namedItem(selection);
			if(pt==null) return;
			if($t.p.selrow && $t.p.scrollrows===true) {
				olr = $t.rows.namedItem($t.p.selrow).rowIndex;
				ner = $t.rows.namedItem(selection).rowIndex;
				if(ner >=0 ){
					if(ner > olr ) {
						scrGrid(ner,'d');
					} else {
						scrGrid(ner,'u');
					}
				}
			}
			if(!$t.p.multiselect) {
				if($(pt).attr("class") !== "subgrid") {
				if( $t.p.selrow ) {$("tr#"+$.jgrid.jqID($t.p.selrow),$t.grid.bDiv).removeClass("ui-state-highlight").attr("aria-selected","false") ;}
				$t.p.selrow = pt.id;
				$("#jqg_"+$.jgrid.jqID($t.p.selrow),$t.rows).attr("checked",true);
				$(pt).addClass("ui-state-highlight").attr("aria-selected","true");
				if( $t.p.onSelectRow && onsr) { $t.p.onSelectRow($t.p.selrow, true); }
				}
			} else {
				$t.p.selrow = pt.id;
				ia = $.inArray($t.p.selrow,$t.p.selarrrow);
				if (  ia === -1 ){ 
					if($(pt).attr("class") !== "subgrid") { $(pt).addClass("ui-state-highlight").attr("aria-selected","true");}
					stat = true;
					$("#jqg_"+$.jgrid.jqID($t.p.selrow),$t.rows).attr("checked",stat);
					$t.p.selarrrow.push($t.p.selrow);
					if( $t.p.onSelectRow && onsr) { $t.p.onSelectRow($t.p.selrow, stat); }
				} else {
					if($(pt).attr("class") !== "subgrid") { $(pt).removeClass("ui-state-highlight").attr("aria-selected","false");}
					stat = false;
					$("#jqg_"+$.jgrid.jqID($t.p.selrow),$t.rows).attr("checked",stat);
					$t.p.selarrrow.splice(ia,1);
					if( $t.p.onSelectRow && onsr) { $t.p.onSelectRow($t.p.selrow, stat); }
					tpsr = $t.p.selarrrow[0];
					$t.p.selrow = (tpsr === undefined) ? null : tpsr;
				}
			}
			function scrGrid(iR,tp){
				var ch = $($t.grid.bDiv)[0].clientHeight,
				st = $($t.grid.bDiv)[0].scrollTop,
				nROT = $t.rows[iR].offsetTop+$t.rows[iR].clientHeight,
				pROT = $t.rows[iR].offsetTop;
				if(tp == 'd') {
					if(nROT >= ch) { $($t.grid.bDiv)[0].scrollTop = st + nROT-pROT; }
				}
				if(tp == 'u'){
					if (pROT < st) { $($t.grid.bDiv)[0].scrollTop = st - nROT+pROT; }
				}
			}
		});
	},
	resetSelection : function(){
		return this.each(function(){
			var t = this, ind;
			if(!t.p.multiselect) {
				if(t.p.selrow) {
					$("tr#"+$.jgrid.jqID(t.p.selrow),t.grid.bDiv).removeClass("ui-state-highlight").attr("aria-selected","false");
					t.p.selrow = null;
				}
			} else {
				$(t.p.selarrrow).each(function(i,n){
					ind = t.rows.namedItem(n);
					$(ind).removeClass("ui-state-highlight").attr("aria-selected","false");
					$("#jqg_"+$.jgrid.jqID(n),ind).attr("checked",false);
				});
				$("#cb_"+$.jgrid.jqID(t.p.id),t.grid.hDiv).attr("checked",false);
				t.p.selarrrow = [];
			}
			t.p.savedRow = [];
		});
	},
	getRowData : function( rowid ) {
		var res = {}, resall, getall=false, len, j=0;
		this.each(function(){
			var $t = this,nm,ind;
			if(typeof(rowid) == 'undefined') {
				getall = true;
				resall = [];
				len = $t.rows.length;
			} else {
				ind = $t.rows.namedItem(rowid);
				if(!ind) return res;
				len = 1;
			}
			while(j<len){
				if(getall) ind = $t.rows[j];
				$('td',ind).each( function(i) {
					nm = $t.p.colModel[i].name; 
					if ( nm !== 'cb' && nm !== 'subgrid' && nm!=="rb") {//添加单选框判断
						if($t.p.treeGrid===true && nm == $t.p.ExpandColumn) {
							res[nm] = $.jgrid.htmlDecode($("span:first",this).html());
						} else {
							try {
								res[nm] = $.unformat(this,{colModel:$t.p.colModel[i]},i);
							} catch (e){
								res[nm] = $.jgrid.htmlDecode($(this).html());
							}
						}
					}
				});
				j++;
				if(getall) { resall.push(res); res={}; }
			}
		});
		return resall ? resall: res;
	},
	delRowData : function(rowid) {
		var success = false, rowInd, ia, ri;
		this.each(function() {
			var $t = this;
			rowInd = $t.rows.namedItem(rowid);
			if(!rowInd) {return false;}
			else {
				ri = rowInd.rowIndex;
				$(rowInd).remove();
				$t.p.records--;
				$t.p.reccount--;
				$t.updatepager(true,false);
				success=true;
				if(rowid == $t.p.selrow) {$t.p.selrow=null;}
				ia = $.inArray(rowid,$t.p.selarrrow);
				if(ia != -1) {$t.p.selarrrow.splice(ia,1);}
			}
			if( ri == 0 && success ) {
				$t.updateColumns();
			}
			if( $t.p.altRows === true && success ) {
				var cn = $t.p.altclass;
				$($t.rows).each(function(i){
					if(i % 2 ==1) $(this).addClass(cn);
					else $(this).removeClass(cn);
				});
			}
		});
		return success;
	},
setRowData:function (rowid, data, cssp) {
	
		var nm, success = false;
		this.each(function () {
			var t = this, vl, ind, cp = typeof cssp;
			if (!t.grid) {
				return false;
			}
			ind = t.rows.namedItem(rowid);
			if (!ind) {
				return false;
			}
			if (data) {
			
				$(this.p.colModel).each(function (i) {
					nm = this.name;
					
					if (data[nm] != undefined) {							
						vl = t.formatter(rowid, data[nm], i, data, "edit");
						if (t.p.treeGrid === true && nm == t.p.ExpandColumn) {
							$("td:eq(" + i + ") > span:first", ind).html(vl).attr("tips", $.jgrid.stripHtml(vl));
						} else {
							$("td:eq(" + i + ")", ind).html(vl).attr("tips", $.jgrid.stripHtml(vl));
							$("td:eq(" + i + ")", ind).html(vl).attr("value",data[nm] );//修改value对应的值
						}
						success = true;
					}
				});
			}
			if (cp === "string") {
				$(ind).addClass(cssp);
			} else {
				if (cp === "object") {
					$(ind).css(cssp);
				}
			}
		});
		
		return success;
	},
	addRowData:function (rowid, data, pos, src) {
		if (!pos) {
			pos = "last";
		}
		var success = false, nm, row = "", gi = 0, si = 0, ni = 0, sind, i, v, prp = "";
		if (data) {
			this.each(function () {
				var t = this;
				var editRows = t.p.savedRow;//当有一行处于编辑状态时，不允许添加一行
				for(var v = 0 ;v< editRows.length;v++)
				{
				   var editRow = editRows[v].id;
				   i = $(t).jqGrid("getInd", editRow, true);
				   if($(i).attr("saveType")=="edit")
				   {
				     
				       return success;
				   }
				   
				}
				if (t.p.rownumbers === true) {
					prp = t.formatCol(ni, 1);
					row += "<td  role=\"gridcell\" class=\"ui-state-default jqgrid-rownum\" " + prp + ">0</td>";
					ni = 1;
				}
				if (t.p.multiselect) {
					v = "<input type=\"checkbox\" id=\"jqg_" + rowid + "\" class=\"cbox\"/>";
					prp = t.formatCol(ni, 1);
					row += "<td role=\"gridcell\" " + prp + ">" + v + "</td>";
					gi = 1;
				}
                 if(t.p.radioselect === true)
				{
				    
				 v = "<input type=\"radio\" id=\"jqg_" + rowid + "\" class=\"rbox\" name='jqg_' />";
				 prp = t.formatCol(ni, 1);
				 row +="<td role='gridcell' " + prp + ">" + v + "</td>";
				 gi = 1;
				}
				
				if (t.p.subGrid === true) {
					row += $(t).jqGrid("addSubGridCell", gi + ni, 1);
					si = 1;
				}
				if (typeof (rowid) != "undefined") {
					rowid = rowid + "";
				} else {
					rowid = (t.p.records + 1) + "";
					if (t.p.keyIndex !== false) {
						var cmn = t.p.colModel[t.p.keyIndex + gi + si + ni].name;
						if (typeof data[cmn] != "undefined") {
							rowid = data[cmn];
						}
					}
				}
				for (i = gi + si + ni; i < this.p.colModel.length; i++) {
					nm = this.p.colModel[i].name;
					v = t.formatter(rowid, data[nm], i, data, "add");
					var hhhhhhh = data[nm];
					prp = t.formatCol(i, 1);
					row += "<td value = '"+hhhhhhh+"' role=\"gridcell\" " + prp + " tips=\"" + $.jgrid.stripHtml(v) + "\">" + v + "</td>";
				}
				row = "<tr id=\"" + rowid + "\" saveType = 'add' role=\"row\" class=\"ui-widget-content jqgrow ui-row-" + t.p.direction + "\">" + row + "</tr>";
				if (t.p.subGrid === true) {
					row = $(row)[0];
					$(t).jqGrid("addSubGrid", row, gi + ni);
				}
				if (t.rows.length === 0) {
					$("table:first", t.grid.bDiv).append(row);
				} else {
					switch (pos) {
					  case "last":
						$(t.rows[t.rows.length - 1]).after(row);
						break;
					  case "first":
						$(t.rows[0]).before(row);
						break;
					  case "after":
						sind = t.rows.namedItem(src);
						if (sind) {
							$(t.rows[sind.rowIndex + 1]).hasClass("ui-subgrid") ? $(t.rows[sind.rowIndex + 1]).after(row) : $(sind).after(row);
						}
						break;
					  case "before":
						sind = t.rows.namedItem(src);
						if (sind) {
							$(sind).before(row);
							sind = sind.rowIndex;
						}
						break;
					}
				}
				t.p.records++;
				t.p.reccount++;
				if (!t.grid.cols || !t.grid.cols.length) {
					t.grid.cols = t.rows[0].cells;
				}
				if (pos === "first" || (pos === "before" && sind <= 1) || t.rows.length === 1) {
					t.updateColumns();
				}
				if (t.p.altRows === true) {
					var cn = t.p.altclass;
					if (pos == "last") {
						if ((t.rows.length - 1) % 2 == 1) {
							$(t.rows[t.rows.length - 1]).addClass(cn);
						}
					} else {
						$(t.rows).each(function (i) {
							if (i % 2 == 1) {
								$(this).addClass(cn);
							} else {
								$(this).removeClass(cn);
							}
						});
					}
				}
				var tr = $("#"+rowid,t);
				
					if(t.p.isTips)//添加tips功能
									{
									    var tipClass = t.p.tipsClass;
									   	var tds = $("td",tr)
	                                    tds.Tooltip({
	                                       showURL: false,
                                           extraClass: tipClass,
	                                       opacity: 0.95		                                    
	                                    });
									}
			  var colMs = t.p.colModel;//为固定列增加样式
			   
			  for(var colMsIndex = 0 ;colMsIndex<colMs.length;colMsIndex++)
			  {
			      if(typeof colMs[colMsIndex].fixecol == "undefined")
			      {
			           colMs[colMsIndex].fixecol = false;			           
			      }
			      var h = colMs[colMsIndex].fixecol;
			      if(colMs[colMsIndex].fixecol)
			      {
			        
			         
			         $("td:eq("+colMsIndex+")",tr).addClass("FixedCol");
			      
			      }
			  }
			  if(t.p.isFixRum)//判断是否固定编号
			    {		        
			         $(".jqgrid-rownum",tr).addClass("FixedCol");

			    }
			    if(t.p.isFixMilt)//判断是否固定复选框
			    {
			         $(".cbox",tr).parent().addClass("FixedCol");

			    }
			    if(t.p.isFixRadio)//判断是否固定单选框
			    {

			         $(".rbox",tr).parent().addClass("FixedCol");

			    }
				try {
					t.p.afterInsertRow(rowid, data);
				}
				catch (e) {
				}
				t.updatepager(true, true);
				success = true;
			});
		}
		return success;
	},
	footerData : function(action,data, format) {
		var nm, success=false, res={};
		function isEmpty(obj) { for(var i in obj) { return false; } return true; }
		if(typeof(action) == "undefined") action = "get";
		if(typeof(format) != "boolean") format  = true;
		action = action.toLowerCase();
		this.each(function(){
			var t = this, vl, ind;
			if(!t.grid || !t.p.footerrow) {return false;}
			if(action == "set") { if(isEmpty(data)) return false; }
			success=true;
			$(this.p.colModel).each(function(i){
				nm = this.name;
				if(action == "set") {
					if( data[nm] != undefined) {
						vl = format ? t.formatter( "", data[nm], i, data, 'edit') : data[nm];
						$("tr.footrow td:eq("+i+")",t.grid.sDiv).html(vl).attr("tips",$.jgrid.stripHtml(vl)); 
						success = true;
					}
				} else if(action == "get") {
					res[nm] = $("tr.footrow td:eq("+i+")",t.grid.sDiv).html();
				}
			});
		});
		return action == "get" ? res : success;
	},
	ShowHideCol : function(colname,show) {
		return this.each(function() {
			var $t = this, fndh=false;
			if (!$t.grid ) {return;}
			if( typeof colname === 'string') {colname=[colname];}
			show = show !="none" ? "" : "none";
			var sw = show == "" ? true :false;
			$(this.p.colModel).each(function(i) {
				if ($.inArray(this.name,colname) !== -1 && this.hidden === sw) {
					$("tr",$t.grid.hDiv).each(function(){
						$("th:eq("+i+")",this).css("display",show);
					});
					$($t.rows).each(function(j){
						$("td:eq("+i+")",$t.rows[j]).css("display",show);
					});
					if($t.p.footerrow) $("td:eq("+i+")",$t.grid.sDiv).css("display", show);
					if(show == "none") $t.p.tblwidth -= this.width; else $t.p.tblwidth += this.width;
					this.hidden = !sw;
					fndh=true;
				}
			});
			if(fndh===true) {
				$("table:first",$t.grid.hDiv).width($t.p.tblwidth);
				$("table:first",$t.grid.bDiv).width($t.p.tblwidth);
				$t.grid.hDiv.scrollLeft = $t.grid.bDiv.scrollLeft;
				if($t.p.footerrow) {
					$("table:first",$t.grid.sDiv).width($t.p.tblwidth);
					$t.grid.sDiv.scrollLeft = $t.grid.bDiv.scrollLeft;
				}
			}
		});
	},
	hideCol : function (colname) {
		return this.each(function(){$(this).jqGrid("ShowHideCol",colname,"none");});
	},
	showCol : function(colname) {
		return this.each(function(){$(this).jqGrid("ShowHideCol",colname,"");});
	},
	remapColumns : function(permutation, updateCells, keepHeader)
	{
		function resortArray(a) {
			var ac;
			if (a.length) {
				ac = $.makeArray(a);
			} else {
				ac = $.extend({}, a);
			}
			$.each(permutation, function(i) {
				a[i] = ac[this];
			});
		}
		var ts = this.get(0);
		function resortRows(parent, clobj) {
			$(">tr"+(clobj||""), parent).each(function() {
				var row = this;
				var elems = $.makeArray(row.cells);
				$.each(permutation, function() {
					var e = elems[this];
					if (e) {
						row.appendChild(e);
					}
				});
			});
		}
		resortArray(ts.p.colModel);
		resortArray(ts.p.colNames);
		resortArray(ts.grid.headers);
		resortRows($("thead:first", ts.grid.hDiv), keepHeader && ":not(.ui-jqgrid-labels)");
		if (updateCells) {
			resortRows($("tbody:first", ts.grid.bDiv), ".jqgrow");
		}
		if (ts.p.footerrow) {
			resortRows($("tbody:first", ts.grid.sDiv));
		}
		if (ts.p.remapColumns) {
			if (!ts.p.remapColumns.length)
				ts.p.remapColumns = $.makeArray(permutation);
			else
				resortArray(ts.p.remapColumns);
		}
		ts.p.lastsort = $.inArray(ts.p.lastsort, permutation);
		if(ts.p.treeGrid) ts.p.expColInd = $.inArray(ts.p.expColInd, permutation);
	},
	setGridWidth : function(nwidth, shrink) {
		return this.each(function(){
			var $t = this, cw,
			initwidth = 0, brd=$t.p.cellLayout, lvc, vc=0, hs=false, scw=$t.p.scrollOffset, aw, gw=0, tw=0,
			cl = 0,cr;
			if (!$t.grid ) {return;}
			if(typeof shrink != 'boolean') {
				shrink=$t.p.shrinkToFit;
			}
			if(isNaN(nwidth)) {return;}
			else { nwidth = parseInt(nwidth); $t.grid.width = $t.p.width = nwidth;}
			$("#gbox_"+$t.p.id).css("width",nwidth+"px");
			$("#gview_"+$t.p.id).css("width",nwidth+"px");
			$($t.grid.bDiv).css("width",nwidth+"px");
			$($t.grid.hDiv).css("width",nwidth+"px");
			if($t.p.pager ) {$($t.p.pager).css("width",nwidth+"px");}
			if($t.p.toolbar[0] === true){
				$($t.grid.uDiv).css("width",nwidth+"px");
				if($t.p.toolbar[1]=="both") {$($t.grid.ubDiv).css("width",nwidth+"px");}
			}
			if($t.p.footerrow) $($t.grid.sDiv).css("width",nwidth+"px");
			if(shrink ===false && $t.p.forceFit == true) {$t.p.forceFit=false;}			
			if(shrink===true) {
				if ($.browser.safari) { brd=0;}
				$.each($t.p.colModel, function(i) {
					if(this.hidden===false){
						initwidth += parseInt(this.width,10);
						if(this.fixed) {
							tw += this.width;
							gw += this.width+brd;
						} else {
							vc++;
						}
						cl++;
					}
				});
				if(vc  == 0) return; 
				$t.p.tblwidth = initwidth;
				aw = nwidth-brd*vc-gw;
				if(!isNaN($t.p.height)) {
					if($($t.grid.bDiv)[0].clientHeight < $($t.grid.bDiv)[0].scrollHeight){
						hs = true;
						aw -= scw;
					}
				}
				initwidth =0;
				var cle = $t.grid.cols.length >0;
				$.each($t.p.colModel, function(i) {
					var tn = this.name;
					if(this.hidden === false && !this.fixed){
						cw = Math.floor((aw)/($t.p.tblwidth-tw)*this.width);
						this.width =cw;
						initwidth += cw;
						$t.grid.headers[i].width=cw;
						$t.grid.headers[i].el.style.width=cw+"px";
						if($t.p.footerrow) $t.grid.footers[i].style.width = cw+"px";
						if(cle) $t.grid.cols[i].style.width = cw+"px";
						lvc = i;
					}
				});
				cr =0;
				if (hs) {
					if(nwidth-gw-(initwidth+brd*vc) !== scw)
						cr = nwidth-gw-(initwidth+brd*vc)-scw;
				} else if( Math.abs(nwidth-gw-(initwidth+brd*vc)) !== 1) {
					cr = nwidth-gw-(initwidth+brd*vc);
				}
				$t.p.colModel[lvc].width += cr;
				cw= $t.p.colModel[lvc].width;
				$t.grid.headers[lvc].width = cw;
				$t.grid.headers[lvc].el.style.width=cw+"px";
				if(cle) $t.grid.cols[lvc].style.width = cw+"px";
				$t.p.tblwidth = initwidth+cr+tw+brd*cl;
				$('table:first',$t.grid.bDiv).css("width",$t.p.tblwidth+"px");
				$('table:first',$t.grid.hDiv).css("width",$t.p.tblwidth+"px");
				$t.grid.hDiv.scrollLeft = $t.grid.bDiv.scrollLeft;
				if($t.p.footerrow) {
					$t.grid.footers[lvc].style.width = cw+"px";
					$('table:first',$t.grid.sDiv).css("width",$t.p.tblwidth+"px");
				}
			}
		});
	},
	setGridHeight : function (nh) {
		return this.each(function (){
			var $t = this;
			if(!$t.grid) {return;}
			$($t.grid.bDiv).css({height: nh+(isNaN(nh)?"":"px")});
			$t.p.height = nh;
			if ($t.p.scroll) $t.grid.populateVisible();
		});
	},
	setCaption : function (newcap){
		return this.each(function(){
			this.p.caption=newcap;
			$("span.ui-jqgrid-title",this.grid.cDiv).html(newcap);
			$(this.grid.cDiv).show();
		});
	},
	setLabel : function(colname, nData, prop, attrp ){
		return this.each(function(){
			var $t = this, pos=-1;
			if(!$t.grid) {return;}
			if(isNaN(colname)) {
				$($t.p.colModel).each(function(i){
					if (this.name == colname) {
						pos = i;return false;
					}
				});
			} else {pos = parseInt(colname,10);}
			if(pos>=0) {
				var thecol = $("tr.ui-jqgrid-labels th:eq("+pos+")",$t.grid.hDiv);
				if (nData){
					var ico = $(".s-ico",thecol);
					$("[id^=jqgh_]",thecol).empty().html(nData).append(ico);
					$t.p.colNames[pos] = nData;
				}
				if (prop) {
					if(typeof prop === 'string') {$(thecol).addClass(prop);} else {$(thecol).css(prop);}
				}
				if(typeof attrp === 'object') {$(thecol).attr(attrp);}
			}
		});
	},
	setCell : function(rowid,colname,nData,cssp,attrp) {
		return this.each(function(){
			var $t = this, pos =-1,v;
			if(!$t.grid) {return;}
			if(isNaN(colname)) {
				$($t.p.colModel).each(function(i){
					if (this.name == colname) {
						pos = i;return false;
					}
				});
			} else {pos = parseInt(colname,10);}
			if(pos>=0) {
				var ind = $t.rows.namedItem(rowid);
				if (ind){
					var tcell = $("td:eq("+pos+")",ind);
					if(nData !== "") {
						v = $t.formatter(rowid, nData, pos,ind,'edit');
						$(tcell).html(v).attr("title",$.jgrid.stripHtml(v));
					}
					if (cssp){
						if(typeof cssp === 'string') {$(tcell).addClass(cssp);} else {$(tcell).css(cssp);}
					}
					if(typeof attrp === 'object') {$(tcell).attr(attrp);}
				}
			}
		});
	},
	getCell : function(rowid,col) {
		var ret = false;
		this.each(function(){
			var $t=this, pos=-1;
			if(!$t.grid) {return;}
			if(isNaN(col)) {
				$($t.p.colModel).each(function(i){
					if (this.name === col) {
						pos = i;return false;
					}
				});
			} else {pos = parseInt(col,10);}
			if(pos>=0) {
				var ind = $t.rows.namedItem(rowid);
				if(ind) {
					try {
						ret = $.unformat($("td:eq("+pos+")",ind),{colModel:$t.p.colModel[pos]},pos);
					} catch (e){
						ret = $.jgrid.htmlDecode($("td:eq("+pos+")",ind).html());
					}
				}
			}
		});
		return ret;
	},
	getCol : function (col, obj, mathopr) {
		var ret = [], val, sum=0;
		obj = typeof (obj) != 'boolean' ? false : obj;
		if(typeof mathopr == 'undefined') mathopr = false;
		this.each(function(){
			var $t=this, pos=-1;
			if(!$t.grid) {return;}
			if(isNaN(col)) {
				$($t.p.colModel).each(function(i){
					if (this.name === col) {
						pos = i;return false;
					}
				});
			} else {pos = parseInt(col,10);}
			if(pos>=0) {
				var ln = $t.rows.length, i =0;
				if (ln && ln>0){
					while(i<ln){
						try {
							val = $.unformat($($t.rows[i].cells[pos]),{colModel:$t.p.colModel[pos]},pos);
						} catch (e) {
							val = $.jgrid.htmlDecode($t.rows[i].cells[pos].innerHTML);
						}
						mathopr ? sum += parseFloat(val,10) :
							obj ? ret.push({id:$t.rows[i].id,value:val}) : ret[i]=val;
						i++;
					}
					if(mathopr) {
						switch(mathopr.toLowerCase()){
							case 'sum': ret =sum; break;
							case 'avg': ret = sum/ln; break;
							case 'count': ret = ln; break;
						}
					}
				}
			}
		});
		return ret;
	},
	clearGridData : function(clearfooter) {
		return this.each(function(){
			var $t = this;
			if(!$t.grid) {return;}
			if(typeof clearfooter != 'boolean') clearfooter = false;
			$("tbody:first tr", $t.grid.bDiv).remove();
			if($t.p.footerrow && clearfooter) $(".ui-jqgrid-ftable td",$t.grid.sDiv).html("&#160;");
			$t.p.selrow = null; $t.p.selarrrow= []; $t.p.savedRow = [];
			$t.p.records = 0;$t.p.page='0';$t.p.lastpage='0';$t.p.reccount=0;
			$t.updatepager(true,false);
		});
	},
	getInd : function(rowid,rc){
		var ret =false,rw;
		this.each(function(){
			rw = this.rows.namedItem(rowid);
			if(rw) {
				ret = rc===true ? rw: rw.rowIndex;
			}
		});
		return ret;
	}
});
})(jQuery);
/*
**
 * formatter for values but most of the values if for jqGrid
 * Some of this was inspired and based on how YUI does the table datagrid but in jQuery fashion
 * we are trying to keep it as light as possible
 * Joshua Burnett josh@9ci.com	
 * http://www.greenbill.com
 *
 * Changes from Tony Tomov tony@trirand.com
 * Dual licensed under the MIT and GPL licenses:
 * http://www.opensource.org/licenses/mit-license.php
 * http://www.gnu.org/licenses/gpl.html
 * 
**/

;(function($) {
	$.fmatter = {};
	//opts can be id:row id for the row, rowdata:the data for the row, colmodel:the column model for this column
	//example {id:1234,}
	$.fn.fmatter = function(formatType, cellval, opts, rwd, act) {
		//debug(this);
		//debug(cellval);
		// build main options before element iteration
		opts = $.extend({}, $.jgrid.formatter, opts);
		return fireFormatter(formatType,cellval, opts, rwd, act); 
	};
	$.fmatter.util = {
		// Taken from YAHOO utils
		NumberFormat : function(nData,opts) {
			if(!isNumber(nData)) {
				nData *= 1;
			}
			if(isNumber(nData)) {
		        var bNegative = (nData < 0);
				var sOutput = nData + "";
				var sDecimalSeparator = (opts.decimalSeparator) ? opts.decimalSeparator : ".";
				var nDotIndex;
				if(isNumber(opts.decimalPlaces)) {
					// Round to the correct decimal place
					var nDecimalPlaces = opts.decimalPlaces;
					var nDecimal = Math.pow(10, nDecimalPlaces);
					sOutput = Math.round(nData*nDecimal)/nDecimal + "";
					nDotIndex = sOutput.lastIndexOf(".");
					if(nDecimalPlaces > 0) {
                    // Add the decimal separator
						if(nDotIndex < 0) {
							sOutput += sDecimalSeparator;
							nDotIndex = sOutput.length-1;
						}
						// Replace the "."
						else if(sDecimalSeparator !== "."){
							sOutput = sOutput.replace(".",sDecimalSeparator);
						}
                    // Add missing zeros
						while((sOutput.length - 1 - nDotIndex) < nDecimalPlaces) {
						    sOutput += "0";
						}
	                }
	            }
	            if(opts.thousandsSeparator) {
	                var sThousandsSeparator = opts.thousandsSeparator;
	                nDotIndex = sOutput.lastIndexOf(sDecimalSeparator);
	                nDotIndex = (nDotIndex > -1) ? nDotIndex : sOutput.length;
	                var sNewOutput = sOutput.substring(nDotIndex);
	                var nCount = -1;
	                for (var i=nDotIndex; i>0; i--) {
	                    nCount++;
	                    if ((nCount%3 === 0) && (i !== nDotIndex) && (!bNegative || (i > 1))) {
	                        sNewOutput = sThousandsSeparator + sNewOutput;
	                    }
	                    sNewOutput = sOutput.charAt(i-1) + sNewOutput;
	                }
	                sOutput = sNewOutput;
	            }
	            // Prepend prefix
	            sOutput = (opts.prefix) ? opts.prefix + sOutput : sOutput;
	            // Append suffix
	            sOutput = (opts.suffix) ? sOutput + opts.suffix : sOutput;
	            return sOutput;
				
			} else {
				return nData;
			}
		},
		// Tony Tomov
		// PHP implementation. Sorry not all options are supported.
		// Feel free to add them if you want
		DateFormat : function (format, date, newformat, opts)  {
			var	token = /\\.|[dDjlNSwzWFmMntLoYyaABgGhHisueIOPTZcrU]/g,
			timezone = /\b(?:[PMCEA][SDP]T|(?:Pacific|Mountain|Central|Eastern|Atlantic) (?:Standard|Daylight|Prevailing) Time|(?:GMT|UTC)(?:[-+]\d{4})?)\b/g,
			timezoneClip = /[^-+\dA-Z]/g,
			pad = function (value, length) {
				value = String(value);
				length = parseInt(length) || 2;
				while (value.length < length) value = '0' + value;
				return value;
			},
		    ts = {m : 1, d : 1, y : 1970, h : 0, i : 0, s : 0},
		    timestamp=0, dM, k,hl,
		    dateFormat=["i18n"];
			// Internationalization strings
		    dateFormat["i18n"] = {
				dayNames:   opts.dayNames,
		    	monthNames: opts.monthNames
			};
			if( format in opts.masks ) format = opts.masks[format];
			date = date.split(/[\\\/:_;.\t\T\s-]/);
			format = format.split(/[\\\/:_;.\t\T\s-]/);
			// parsing for month names
		    for(k=0,hl=format.length;k<hl;k++){
				if(format[k] == 'M') {
					dM = $.inArray(date[k],dateFormat.i18n.monthNames);
					if(dM !== -1 && dM < 12){date[k] = dM+1;}
				}
				if(format[k] == 'F') {
					dM = $.inArray(date[k],dateFormat.i18n.monthNames);
					if(dM !== -1 && dM > 11){date[k] = dM+1-12;}
				}
		        ts[format[k].toLowerCase()] = parseInt(date[k],10);
		    }
		    ts.m = parseInt(ts.m)-1;
		    var ty = ts.y;
		    if (ty >= 70 && ty <= 99) ts.y = 1900+ts.y;
		    else if (ty >=0 && ty <=69) ts.y= 2000+ts.y;
		    timestamp = new Date(ts.y, ts.m, ts.d, ts.h, ts.i, ts.s,0);
			if( newformat in opts.masks )  {
				newformat = opts.masks[newformat];
			} else if ( !newformat ) {
				newformat = 'Y-m-d';
			}
		    var 
		        G = timestamp.getHours(),
		        i = timestamp.getMinutes(),
		        j = timestamp.getDate(),
				n = timestamp.getMonth() + 1,
				o = timestamp.getTimezoneOffset(),
				s = timestamp.getSeconds(),
				u = timestamp.getMilliseconds(),
				w = timestamp.getDay(),
				Y = timestamp.getFullYear(),
				N = (w + 6) % 7 + 1,
				z = (new Date(Y, n - 1, j) - new Date(Y, 0, 1)) / 86400000,
				flags = {
					// Day
					d: pad(j),
					D: dateFormat.i18n.dayNames[w],
					j: j,
					l: dateFormat.i18n.dayNames[w + 7],
					N: N,
					S: opts.S(j),
					//j < 11 || j > 13 ? ['st', 'nd', 'rd', 'th'][Math.min((j - 1) % 10, 3)] : 'th',
					w: w,
					z: z,
					// Week
					W: N < 5 ? Math.floor((z + N - 1) / 7) + 1 : Math.floor((z + N - 1) / 7) || ((new Date(Y - 1, 0, 1).getDay() + 6) % 7 < 4 ? 53 : 52),
					// Month
					F: dateFormat.i18n.monthNames[n - 1 + 12],
					m: pad(n),
					M: dateFormat.i18n.monthNames[n - 1],
					n: n,
					t: '?',
					// Year
					L: '?',
					o: '?',
					Y: Y,
					y: String(Y).substring(2),
					// Time
					a: G < 12 ? opts.AmPm[0] : opts.AmPm[1],
					A: G < 12 ? opts.AmPm[2] : opts.AmPm[3],
					B: '?',
					g: G % 12 || 12,
					G: G,
					h: pad(G % 12 || 12),
					H: pad(G),
					i: pad(i),
					s: pad(s),
					u: u,
					// Timezone
					e: '?',
					I: '?',
					O: (o > 0 ? "-" : "+") + pad(Math.floor(Math.abs(o) / 60) * 100 + Math.abs(o) % 60, 4),
					P: '?',
					T: (String(timestamp).match(timezone) || [""]).pop().replace(timezoneClip, ""),
					Z: '?',
					// Full Date/Time
					c: '?',
					r: '?',
					U: Math.floor(timestamp / 1000)
				};	
			return newformat.replace(token, function ($0) {
				return $0 in flags ? flags[$0] : $0.substring(1);
			});			
		}
	};
	$.fn.fmatter.defaultFormat = function(cellval, opts) {
		return (isValue(cellval) && cellval!=="" ) ?  cellval : opts.defaultValue ? opts.defaultValue : "&#160;";
	};
	$.fn.fmatter.email = function(cellval, opts) {
		if(!isEmpty(cellval)) {
			return "<a href=\"mailto:" + cellval + "\">" + cellval + "</a>";
        }else {
			return $.fn.fmatter.defaultFormat(cellval,opts );
        }
	};
	$.fn.fmatter.checkbox =function(cval, opts) {
		var op = $.extend({},opts.checkbox), ds;
		if(!isUndefined(opts.colModel.formatoptions)) {
			op = $.extend({},op,opts.colModel.formatoptions);
		}
		if(op.disabled===true) {ds = "disabled";} else {ds="";}
		if(isEmpty(cval) || isUndefined(cval) ) cval = $.fn.fmatter.defaultFormat(cval,op);
		cval=cval+""; cval=cval.toLowerCase();
		var bchk = cval.search(/(false|0|no|off)/i)<0 ? " checked='checked' " : "";
        return "<input type=\"checkbox\" " + bchk  + " value=\""+ cval+"\" offval=\"no\" "+ds+ "/>";
    },
	$.fn.fmatter.link = function(cellval, opts) {
		var op = {target:opts.target };
		var target = "";
		if(!isUndefined(opts.colModel.formatoptions)) {
            op = $.extend({},op,opts.colModel.formatoptions);
        }
		if(op.target) {target = 'target=' + op.target;}
        if(!isEmpty(cellval)) {
        if(op.content)//添加一个参数，满足现实相同的字样但是转到不同的链接
		{
		return "<a "+target+" href=\"" + cellval + "\">" + op.content + "</a>";
		}else
		{
			return "<a "+target+" href=\"" + cellval + "\">" + cellval + "</a>";
			}
			
        }else {
            return $.fn.fmatter.defaultFormat(cellval,opts);
        }
    };
	$.fn.fmatter.showlink = function(cellval, opts) {
		var op = {baseLinkUrl: opts.baseLinkUrl,showAction:opts.showAction, addParam: opts.addParam || "", target: opts.target, idName: opts.idName },
		target = "";
		if(!isUndefined(opts.colModel.formatoptions)) {
			op = $.extend({},op,opts.colModel.formatoptions);
		}
		if(op.target) {target = 'target=' + op.target;}
		idUrl = op.baseLinkUrl+op.showAction + '?'+ op.idName+'='+opts.rowId+op.addParam;
        if(isString(cellval)) {	//add this one even if its blank string
			return "<a "+target+" href=\"" + idUrl + "\">" + cellval + "</a>";
        }else {
			return $.fn.fmatter.defaultFormat(cellval,opts);
	    }
    };
	$.fn.fmatter.integer = function(cellval, opts) {
		var op = $.extend({},opts.integer);
		if(!isUndefined(opts.colModel.formatoptions)) {
			op = $.extend({},op,opts.colModel.formatoptions);
		}
		if(isEmpty(cellval)) {
			return op.defaultValue;
		}
		return $.fmatter.util.NumberFormat(cellval,op);
	};
	$.fn.fmatter.number = function (cellval, opts) {
		var op = $.extend({},opts.number);
		if(!isUndefined(opts.colModel.formatoptions)) {
			op = $.extend({},op,opts.colModel.formatoptions);
		}
		if(isEmpty(cellval)) {
			return op.defaultValue;
		}
		return $.fmatter.util.NumberFormat(cellval,op);
	};
	$.fn.fmatter.currency = function (cellval, opts) {
		var op = $.extend({},opts.currency);
		if(!isUndefined(opts.colModel.formatoptions)) {
			op = $.extend({},op,opts.colModel.formatoptions);
		}
		if(isEmpty(cellval)) {
			return op.defaultValue;
		}
		return $.fmatter.util.NumberFormat(cellval,op);
	};
	$.fn.fmatter.date = function (cellval, opts, rwd, act) {
		var op = $.extend({},opts.date);
		if(!isUndefined(opts.colModel.formatoptions)) {
			op = $.extend({},op,opts.colModel.formatoptions);
		}
		if(!op.reformatAfterEdit && act=='edit'){
			return $.fn.fmatter.defaultFormat(cellval, opts);
		} else if(!isEmpty(cellval)) {
			return  $.fmatter.util.DateFormat(op.srcformat,cellval,op.newformat,op);
		} else {
			return $.fn.fmatter.defaultFormat(cellval, opts);
		}
	};
	$.fn.fmatter.select = function (cellval,opts, rwd, act) {
		// jqGrid specific
		cellval = cellval + "";
		var oSelect = false, ret=[];
		if(!isUndefined(opts.colModel.editoptions)){
			oSelect= opts.colModel.editoptions.value;
		}
		if (oSelect) {
			var	msl =  opts.colModel.editoptions.multiple === true ? true : false,
			scell = [], sv;
			if(msl) { scell = cellval.split(","); scell = $.map(scell,function(n){return $.trim(n);})}
			if (isString(oSelect)) {
				// mybe here we can use some caching with care ????
				var so = oSelect.split(";"), j=0;
				for(var i=0; i<so.length;i++){
					sv = so[i].split(":");
					if(msl) {
						if(jQuery.inArray(sv[0],scell)>-1) {
							ret[j] = sv[1];
							j++;
						}
					} else if($.trim(sv[0])==$.trim(cellval)) {
						ret[0] = sv[1];
						break;
					}
				}
			} else if(isObject(oSelect)) {
				// this is quicker
				if(msl) {
					ret = jQuery.map(scell, function(n, i){
						return oSelect[n];
					});
				} else {
					ret[0] = oSelect[cellval] || "";
				}
			}
		}
		cellval = ret.join(", ");
		return  cellval == "" ? $.fn.fmatter.defaultFormat(cellval,opts) : cellval;
	};
	$.unformat = function (cellval,options,pos,cnt) {
		// specific for jqGrid only
		var ret, formatType = options.colModel.formatter,
		op =options.colModel.formatoptions || {}, sep,
		re = /([\.\*\_\'\(\)\{\}\+\?\\])/g;
		unformatFunc = options.colModel.unformat||($.fn.fmatter[formatType] && $.fn.fmatter[formatType].unformat);
		if(typeof unformatFunc !== 'undefined' && isFunction(unformatFunc) ) {
			ret = unformatFunc($(cellval).text(), options, cellval);
		} else if(typeof formatType !== 'undefined' && isString(formatType) ) {
			var opts = $.jgrid.formatter || {}, stripTag;
			switch(formatType) {
				case 'integer' :
					op = $.extend({},opts.integer,op);
					sep = op.thousandsSeparator.replace(re,"\\$1");
					stripTag = new RegExp(sep, "g");
					ret = $(cellval).text().replace(stripTag,'');
					break;
				case 'number' :
					op = $.extend({},opts.number,op);
					sep = op.thousandsSeparator.replace(re,"\\$1");
					stripTag = new RegExp(sep, "g");
					ret = $(cellval).text().replace(stripTag,"").replace(op.decimalSeparator,'.');
					break;
				case 'currency':
					op = $.extend({},opts.currency,op);
					sep = op.thousandsSeparator.replace(re,"\\$1");
					stripTag = new RegExp(sep, "g");
					ret = $(cellval).text().replace(stripTag,'').replace(op.decimalSeparator,'.').replace(op.prefix,'').replace(op.suffix,'');
					break;
				case 'checkbox' :
					var cbv = (options.colModel.editoptions) ? options.colModel.editoptions.value.split(":") : ["Yes","No"];
					ret = $('input',cellval).attr("checked") ? cbv[0] : cbv[1];
					break;
				case 'select' :
					ret = $.unformat.select(cellval,options,pos,cnt);
					break;
                default:
                    ret= $(cellval).text();
                    break;
			}
		}
		return ret ? ret : cnt===true ? $(cellval).text() : $.jgrid.htmlDecode($(cellval).html());
	};
	$.unformat.select = function (cellval,options,pos,cnt) {
		// Spacial case when we have local data and perform a sort
		// cnt is set to true only in sortDataArray
		var ret = [];
		var cell = $(cellval).text();
		if(cnt==true) return cell;
		var op = $.extend({},options.colModel.editoptions);
		if(op.value){
			var oSelect = op.value,
			msl =  op.multiple === true ? true : false,
			scell = [], sv;
			if(msl) { scell = cell.split(","); scell = $.map(scell,function(n){return $.trim(n);})}
			if (isString(oSelect)) {
				var so = oSelect.split(";"), j=0;
				for(var i=0; i<so.length;i++){
					sv = so[i].split(":");
					if(msl) {
						if(jQuery.inArray(sv[1],scell)>-1) {
							ret[j] = sv[0];
							j++;
						}
					} else if($.trim(sv[1])==$.trim(cell)) {
						ret[0] = sv[0];
						break;
					}
				}
			} else if(isObject(oSelect)) {
				if(!msl) scell[0] =  cell;
				ret = jQuery.map(scell, function(n){
					var rv;
					$.each(oSelect, function(i,val){
						if (val == n) {
							rv = i;
							return false;
						}
					});
					if( rv) return rv;
				});
			}
			return ret.join(", ");
		} else {
			return cell || "";
		}
	};
	function fireFormatter(formatType,cellval, opts, rwd, act) {
		var v=cellval;

        if ($.fn.fmatter[formatType]){
            v = $.fn.fmatter[formatType](cellval, opts, rwd, act);
        }

        return v;
	};
	//private methods and data
	function debug($obj) {
		if (window.console && window.console.log) window.console.log($obj);
	};
	/**
     * A convenience method for detecting a legitimate non-null value.
     * Returns false for null/undefined/NaN, true for other values, 
     * including 0/false/''
	 *  --taken from the yui.lang
     */
    isValue= function(o) {
		return (isObject(o) || isString(o) || isNumber(o) || isBoolean(o));
    };
	isBoolean= function(o) {
        return typeof o === 'boolean';
    };
    isNull= function(o) {
        return o === null;
    };
    isNumber= function(o) {
        return typeof o === 'number' && isFinite(o);
    };
    isString= function(o) {
        return typeof o === 'string';
    };
	/**
	* check if its empty trim it and replace \&nbsp and \&#160 with '' and check if its empty ===""
	* if its is not a string but has a value then it returns false, Returns true for null/undefined/NaN
	essentailly this provdes a way to see if it has any value to format for things like links
	*/
 	isEmpty= function(o) {
		if(!isString(o) && isValue(o)) {
			return false;
		}else if (!isValue(o)){
			return true;
		}
		o = $.trim(o).replace(/\&nbsp\;/ig,'').replace(/\&#160\;/ig,'');
        return o==="";
		
    };
    isUndefined= function(o) {
        return typeof o === 'undefined';
    };
	isObject= function(o) {
		return (o && (typeof o === 'object' || isFunction(o))) || false;
    };
	isFunction= function(o) {
        return typeof o === 'function';
    };

})(jQuery);

;(function($){
/**
 * jqGrid extension for custom methods
 * Tony Tomov tony@trirand.com
 * http://trirand.com/blog/ 
 * Dual licensed under the MIT and GPL licenses:
 * http://www.opensource.org/licenses/mit-license.php
 * http://www.gnu.org/licenses/gpl.html
**/ 
$.jgrid.extend({
	getColProp : function(colname){
		var ret ={}, $t = this[0];
		if ( !$t.grid ) { return; }
		var cM = $t.p.colModel;
		for ( var i =0;i<cM.length;i++ ) {
			if ( cM[i].name == colname ) {
				ret = cM[i];
				break;
			}
		};
		return ret;
	},
	setColProp : function(colname, obj){
		//do not set width will not work
		return this.each(function(){
			if ( this.grid ) {
				if ( obj ) {
					var cM = this.p.colModel;
					for ( var i =0;i<cM.length;i++ ) {
						if ( cM[i].name == colname ) {
							$.extend(this.p.colModel[i],obj);
							break;
						}
					}
				}
			}
		});
	},
	sortGrid : function(colname,reload){
		return this.each(function(){
			var $t=this,idx=-1;
			if ( !$t.grid ) { return;}
			if ( !colname ) { colname = $t.p.sortname; }
			for ( var i=0;i<$t.p.colModel.length;i++ ) {
				if ( $t.p.colModel[i].index == colname || $t.p.colModel[i].name==colname ) {
					idx = i;
					break;
				}
			}
			if ( idx!=-1 ){
				var sort = $t.p.colModel[idx].sortable;
				if ( typeof sort !== 'boolean' ) { sort =  true; }
				if ( typeof reload !=='boolean' ) { reload = false; }
				if ( sort ) { $t.sortData("jqgh_"+colname, idx, reload); }
			}
		});
	},
	GridDestroy : function () {
		return this.each(function(){
			if ( this.grid ) { 
				if ( this.p.pager ) { // if not part of grid
					$(this.p.pager).remove();
				}
				var gid = this.id;
				try {
					$("#gbox_"+gid).remove();
				} catch (_) {}
			}
		});
	},
	GridUnload : function(){
		return this.each(function(){
			if ( !this.grid ) {return;}
			var defgrid = {id: $(this).attr('id'),cl: $(this).attr('class')};
			if (this.p.pager) {
				$(this.p.pager).empty().removeClass("ui-state-default ui-jqgrid-pager corner-bottom");
			}
			var newtable = document.createElement('table');
			$(newtable).attr({id:defgrid['id']});
			newtable.className = defgrid['cl'];
			var gid = this.id;
			$(newtable).removeClass("ui-jqgrid-btable");
			if( $(this.p.pager).parents("#gbox_"+gid).length === 1 ) {
				$(newtable).insertBefore("#gbox_"+gid).show();
				$(this.p.pager).insertBefore("#gbox_"+gid);
			} else {
				$(newtable).insertBefore("#gbox_"+gid).show();
			}
			$("#gbox_"+gid).remove();
		});
	},
    setGridState : function(state) {
		return this.each(function(){
			if ( !this.grid ) {return;}
            $t = this;
            if(state == 'hidden'){
				$(".ui-jqgrid-bdiv, .ui-jqgrid-hdiv","#gview_"+$t.p.id).slideUp("fast");
				if($t.p.pager) {$($t.p.pager).slideUp("fast");}
				if($t.p.toolbar[0]===true) {
					if( $t.p.toolbar[1]=='both') {
						$($t.grid.ubDiv).slideUp("fast");
					}
					$($t.grid.uDiv).slideUp("fast");
				}
				if($t.p.footerrow) $(".ui-jqgrid-sdiv","#gbox_"+$s.p.id).slideUp("fast");
				$(".ui-jqgrid-titlebar-close span",$t.grid.cDiv).removeClass("ui-icon-circle-triangle-n").addClass("ui-icon-circle-triangle-s");
				$t.p.gridstate = 'hidden';
            } else if(state=='visible') {
				$(".ui-jqgrid-hdiv, .ui-jqgrid-bdiv","#gview_"+$t.p.id).slideDown("fast");
				if($t.p.pager) {$($t.p.pager).slideDown("fast");}
				if($t.p.toolbar[0]===true) {
					if( $t.p.toolbar[1]=='both') {
						$($t.grid.ubDiv).slideDown("fast");
					}
					$($t.grid.uDiv).slideDown("fast");
				}
				if($t.p.footerrow) $(".ui-jqgrid-sdiv","#gbox_"+$t.p.id).slideDown("fast");
				$(".ui-jqgrid-titlebar-close span",$t.grid.cDiv).removeClass("ui-icon-circle-triangle-s").addClass("ui-icon-circle-triangle-n");
				$t.p.gridstate = 'visible';
            }
            
        });
    },
	updateGridRows : function (data, rowidname, jsonreader) {
		var nm, success=false;
		this.each(function(){
			var t = this, vl, ind, srow, sid;
			if(!t.grid) {return false;}
			if(!rowidname) rowidname = "id";
			if( data  && data.length >0 ) {
				$(data).each(function(j){
					srow = this;
					ind = t.rows.namedItem(srow[rowidname]);
					if(ind) {
						sid = srow[rowidname];
						if(jsonreader === true){
							if(t.p.jsonReader.repeatitems === true) {
								if(t.p.jsonReader.cell) {srow = srow[t.p.jsonReader.cell];}
								for (var k=0;k<srow.length;k++) {
									vl = t.formatter( sid, srow[k], k, srow, 'edit');
									if(t.p.treeGrid===true && nm == t.p.ExpandColumn) {
										$("td:eq("+k+") > span:first",ind).html(vl).attr("title",$.jgrid.stripHtml(vl));
									} else {
										$("td:eq("+k+")",ind).html(vl).attr("title",$.jgrid.stripHtml(vl)); 
									}
								}
								success = true;
								return true;
							}
						} 
						$(t.p.colModel).each(function(i){
							nm = jsonreader===true ? this.jsonmap || this.name :this.name;
							if( srow[nm] != undefined) {
								vl = t.formatter( sid, srow[nm], i, srow, 'edit');
								if(t.p.treeGrid===true && nm == t.p.ExpandColumn) {
									$("td:eq("+i+") > span:first",ind).html(vl).attr("title",$.jgrid.stripHtml(vl));
								} else {
									$("td:eq("+i+")",ind).html(vl).attr("title",$.jgrid.stripHtml(vl)); 
								}
								success = true;
							}
						});
					}
				});
			}
		});
		return success;
	},
	filterGrid : function(gridid,p){
		p = $.extend({
			gridModel : false,
			gridNames : false,
			gridToolbar : false,
			filterModel: [], // label/name/stype/defval/surl/sopt
			formtype : "horizontal", // horizontal/vertical
			autosearch: true, // if set to false a serch button should be enabled.
			formclass: "filterform",
			tableclass: "filtertable",
			buttonclass: "filterbutton",
			searchButton: "Search",
			clearButton: "Clear",
			enableSearch : false,
			enableClear: false,
			beforeSearch: null,
			afterSearch: null,
			beforeClear: null,
			afterClear: null,
			url : '',
			marksearched: true
		},p  || {});
		return this.each(function(){
			var self = this;
			this.p = p;
			if(this.p.filterModel.length == 0 && this.p.gridModel===false) { alert("No filter is set"); return;}
			if( !gridid) {alert("No target grid is set!"); return;}
			this.p.gridid = gridid.indexOf("#") != -1 ? gridid : "#"+gridid;
			var gcolMod = $(this.p.gridid).jqGrid("getGridParam",'colModel');
			if(gcolMod) {
				if( this.p.gridModel === true) {
					var thegrid = $(this.p.gridid)[0];
					var sh;
					// we should use the options search, edittype, editoptions
					// additionally surl and defval can be added in grid colModel
					$.each(gcolMod, function (i,n) {
						var tmpFil = [];
						this.search = this.search === false ? false : true;
						if(this.editrules && this.editrules.searchhidden === true) {
							sh = true;
						} else {
							if(this.hidden === true ) {
								sh = false;
							} else {
								sh = true;
							}
						}
						if( this.search === true && sh === true) {
							if(self.p.gridNames===true) {
								tmpFil.label = thegrid.p.colNames[i];
							} else {
								tmpFil.label = '';
							}
							tmpFil.name = this.name;
							tmpFil.index = this.index || this.name;
							// we support only text and selects, so all other to text
							tmpFil.stype = this.edittype || 'text';
							if(tmpFil.stype != 'select' ) {
								tmpFil.stype = 'text';
							}
							tmpFil.defval = this.defval || '';
							tmpFil.surl = this.surl || '';
							tmpFil.sopt = this.editoptions || {};
							tmpFil.width = this.width;
							self.p.filterModel.push(tmpFil);
						}
					});
				} else {
					$.each(self.p.filterModel,function(i,n) {
						for(var j=0;j<gcolMod.length;j++) {
							if(this.name == gcolMod[j].name) {
								this.index = gcolMod[j].index || this.name;
								break;
							}
						}
						if(!this.index) {
							this.index = this.name;
						}
					});
				}
			} else {
				alert("Could not get grid colModel"); return;
			}
			var triggerSearch = function() {
				var sdata={}, j=0, v;
				var gr = $(self.p.gridid)[0], nm;
                gr.p.searchdata = {};
				if($.isFunction(self.p.beforeSearch)){self.p.beforeSearch();}
				$.each(self.p.filterModel,function(i,n){
                    nm = this.index;
					switch (this.stype) {
						case 'select' :
							v = $("select[name="+nm+"]",self).val();
							if(v) {
								sdata[nm] = v;
								if(self.p.marksearched){
									$("#jqgh_"+this.name,gr.grid.hDiv).addClass("dirty-cell");
								}
								j++;
							} else {
								if(self.p.marksearched){
									$("#jqgh_"+this.name,gr.grid.hDiv).removeClass("dirty-cell");
								}
                                try {
                                    delete gr.p.postData[this.index];
                                } catch (e) {}
							}
							break;
						default:
							v = $("input[name="+nm+"]",self).val();
							if(v) {
								sdata[nm] = v;
								if(self.p.marksearched){
									$("#jqgh_"+this.name,gr.grid.hDiv).addClass("dirty-cell");
								}
								j++;
							} else {
								if(self.p.marksearched){
									$("#jqgh_"+this.name,gr.grid.hDiv).removeClass("dirty-cell");
								}
                                try {
                                    delete gr.p.postData[this.index];
                                } catch(e) {}
							}
					}
				});
				var sd =  j>0 ? true : false;
                $.extend(gr.p.postData,sdata);
				var saveurl;
				if(self.p.url) {
					saveurl = $(gr).jqGrid("getGridParam",'url');
					$(gr).jqGrid("setGridParam",{url:self.p.url});
				}
			    $(gr).jqGrid("setGridParam",{search:sd}).trigger("reloadGrid",[{page:1}]);
				if(saveurl) {$(gr).jqGrid("setGridParam",{url:saveurl});}
				if($.isFunction(self.p.afterSearch)){self.p.afterSearch();}
			};
			var clearSearch = function(){
				var sdata={}, v, j=0;
				var gr = $(self.p.gridid)[0], nm;
				if($.isFunction(self.p.beforeClear)){self.p.beforeClear();}
				$.each(self.p.filterModel,function(i,n){
                    nm = this.index;
					v = (this.defval) ? this.defval : "";
					if(!this.stype){this.stype=='text';}
					switch (this.stype) {
						case 'select' :
							var v1;
							$("select[name="+nm+"] option",self).each(function (i){
                                if(i==0) this.selected = true;
								if ($(this).text() == v) {
									this.selected = true;
									v1 = $(this).val();
									return false;
								}
							});
							if(v1) {
								// post the key and not the text
								sdata[nm] = v1;
								if(self.p.marksearched){
									$("#jqgh_"+this.name,gr.grid.hDiv).addClass("dirty-cell");
								}
								j++;
							} else {
								if(self.p.marksearched){
									$("#jqgh_"+this.name,gr.grid.hDiv).removeClass("dirty-cell");
								}
                                try {
                                    delete gr.p.postData[this.index];
                                } catch (e) {}
							}
							break;
						case 'text':
							$("input[name="+nm+"]",self).val(v);
							if(v) {
								sdata[nm] = v;
								if(self.p.marksearched){
									$("#jqgh_"+this.name,gr.grid.hDiv).addClass("dirty-cell");
								}
								j++;
							} else {
								if(self.p.marksearched){
									$("#jqgh_"+this.name,gr.grid.hDiv).removeClass("dirty-cell");
								}
                                try {
                                    delete gr.p.postData[this.index];
                                } catch (e) {}
							}
                            break;
					}
				});
				var sd =  j>0 ? true : false;
                $.extend(gr.p.postData,sdata);
				var saveurl;
				if(self.p.url) {
					saveurl = $(gr).jqGrid("getGridParam",'url');
					$(gr).jqGrid("setGridParam",{url:self.p.url});
				}
				$(gr).jqGrid("setGridParam",{search:sd}).trigger("reloadGrid",[{page:1}]);
				if(saveurl) {$(gr).jqGrid("setGridParam",{url:saveurl});}
				if($.isFunction(self.p.afterClear)){self.p.afterClear();}
			};
			var formFill = function(){
				var tr = document.createElement("tr");
				var tr1, sb, cb,tl,td, td1;
				if(self.p.formtype=='horizontal'){
					$(tbl).append(tr);
				}
				$.each(self.p.filterModel,function(i,n){
					tl = document.createElement("td");
					$(tl).append("<label for='"+this.name+"'>"+this.label+"</label>");
					td = document.createElement("td");
					var $t=this;
					if(!this.stype) { this.stype='text';}
					switch (this.stype)
					{
					case "select":
						if(this.surl) {
							// data returned should have already constructed html select
							$(td).load(this.surl,function(){
								if($t.defval) $("select",this).val($t.defval);
								$("select",this).attr({name:$t.index || $t.name, id: "sg_"+$t.name});
								if($t.sopt) $("select",this).attr($t.sopt);
								if(self.p.gridToolbar===true && $t.width) {
									$("select",this).width($t.width);
								}
								if(self.p.autosearch===true){
									$("select",this).change(function(e){
										triggerSearch();
										return false;
									});
								}
							});
						} else {
							// sopt to construct the values
							if($t.sopt.value) {
								var oSv = $t.sopt.value;
								var elem = document.createElement("select");
								$(elem).attr({name:$t.index || $t.name, id: "sg_"+$t.name}).attr($t.sopt);
								if(typeof oSv === "string") {
									var so = oSv.split(";"), sv, ov;
									for(var k=0; k<so.length;k++){
										sv = so[k].split(":");
										ov = document.createElement("option");
										ov.value = sv[0]; ov.innerHTML = sv[1];
										if (sv[1]==$t.defval) ov.selected ="selected";
										elem.appendChild(ov);
									}
								} else if(typeof oSv === "object" ) {
									for ( var key in oSv) {
										i++;
										ov = document.createElement("option");
										ov.value = key; ov.innerHTML = oSv[key];
										if (oSv[key]==$t.defval) ov.selected ="selected";
										elem.appendChild(ov);
									}
								}
								if(self.p.gridToolbar===true && $t.width) {
									$(elem).width($t.width);
								}
								$(td).append(elem);
								if(self.p.autosearch===true){
									$(elem).change(function(e){
										triggerSearch();
										return false;
									});
								}
							}
						}
						break;
					case 'text':
						var df = this.defval ? this.defval: "";
						$(td).append("<input type='text' name='"+(this.index || this.name)+"' id='sg_"+this.name+"' value='"+df+"'/>");
						if($t.sopt) $("input",td).attr($t.sopt);
						if(self.p.gridToolbar===true && $t.width) {
							if($.browser.msie) {
								$("input",td).width($t.width-4);
							} else {
								$("input",td).width($t.width-2);
							}
						}
						if(self.p.autosearch===true){
							$("input",td).keypress(function(e){
								var key = e.charCode ? e.charCode : e.keyCode ? e.keyCode : 0;
								if(key == 13){
									triggerSearch();
									return false;
								}
								return this;
							});
						}
						break;
					}
					if(self.p.formtype=='horizontal'){
						if(self.p.gridToolbar===true && self.p.gridNames===false) {
							$(tr).append(td);
						} else {
							$(tr).append(tl).append(td);
						}
						$(tr).append(td);
					} else {
						tr1 = document.createElement("tr");
						$(tr1).append(tl).append(td);
						$(tbl).append(tr1);
					}
				});
				td = document.createElement("td");
				if(self.p.enableSearch === true){
					sb = "<input type='button' id='sButton' class='"+self.p.buttonclass+"' value='"+self.p.searchButton+"'/>";
					$(td).append(sb);
					$("input#sButton",td).click(function(){
						triggerSearch();
						return false;
					});
				}
				if(self.p.enableClear === true) {
					cb = "<input type='button' id='cButton' class='"+self.p.buttonclass+"' value='"+self.p.clearButton+"'/>";
					$(td).append(cb);
					$("input#cButton",td).click(function(){
						clearSearch();
						return false;
					});
				}
				if(self.p.enableClear === true || self.p.enableSearch === true) {
					if(self.p.formtype=='horizontal') {
						$(tr).append(td);
					} else {
						tr1 = document.createElement("tr");
						$(tr1).append("<td>&#160;</td>").append(td);
						$(tbl).append(tr1);
					}
				}
			};
			var frm = $("<form name='SearchForm' style=display:inline;' class='"+this.p.formclass+"'></form>");
			var tbl =$("<table class='"+this.p.tableclass+"' cellspacing='0' cellpading='0' border='0'><tbody></tbody></table>");
			$(frm).append(tbl);
			formFill();
			$(this).append(frm);
			this.triggerSearch = triggerSearch;
			this.clearSearch = clearSearch;
		});
	},
	filterToolbar : function(p){
		p = $.extend({
			autosearch: true, 
			beforeSearch: null,
			afterSearch: null,
			beforeClear: null,
			afterClear: null,
			searchurl : ''
		},p  || {});
		return this.each(function(){
			var $t = this;
			var triggerToolbar = function() {
				var sdata={}, j=0, v, nm;
                $t.p.searchdata = {};
				$.each($t.p.colModel,function(i,n){
                    nm = this.index || this.name;
					switch (this.stype) {
						case 'select' :
							v = $("select[name="+nm+"]",$t.grid.hDiv).val();
							if(v) {
								sdata[nm] = v;
								j++;
							} else {
                                try {
                                    delete $t.p.postData[nm];
                                } catch (e) {}
                            }
							break;
						case 'text':
							v = $("input[name="+nm+"]",$t.grid.hDiv).val();
							if(v) {
								sdata[nm] = v;
								j++;
							} else {
                                try {
                                    delete $t.p.postData[nm];
                                } catch (e) {}
                            }
                            break;
					}
				});
				var sd =  j>0 ? true : false;
                $.extend($t.p.postData,sdata);
				var saveurl;
				if($t.p.searchurl) {
					saveurl = $t.p.url;
					$($t).jqGrid("setGridParam",{url:$t.p.searchurl});
				}
				var bsr = false;
				if($.isFunction(p.beforeSearch)){bsr = p.beforeSearch.call($t);}
				if(!bsr) $($t).jqGrid("setGridParam",{search:sd}).trigger("reloadGrid",[{page:1}]);
				if(saveurl) {$($t).jqGrid("setGridParam",{url:saveurl});}
				if($.isFunction(p.afterSearch)){p.afterSearch();}
			};
			var clearToolbar = function(){
				var sdata={}, v, j=0, nm;
				$.each($t.p.colModel,function(i,n){
					v = (this.searchoptions && this.searchoptions.defaultValue) ? this.searchoptions.defaultValue : "";
                    nm = this.index || this.name;
					switch (this.stype) {
						case 'select' :
							var v1;
							$("select[name="+nm+"] option",$t.grid.hDiv).each(function (i){
                                if(i==0) this.selected = true;
								if ($(this).text() == v) {
									this.selected = true;
									v1 = $(this).val();
									return false;
								}
							});
                            if (v1) {
                                // post the key and not the text
                                sdata[nm] = v1;
                                j++;
                            } else {
                                try {
                                    delete $t.p.postData[nm];
                                } catch(e) {}
                            }
							break;
						case 'text':
							$("input[name="+nm+"]",$t.grid.hDiv).val(v);
							if(v) {
								sdata[nm] = v;
								j++;
							} else {
                                try {
                                    delete $t.p.postData[nm];
                                } catch (e){}
                            }
                            break;
					}
				});
				var sd =  j>0 ? true : false;
                $.extend($t.p.postData,sdata);
				var saveurl;
				if($t.p.searchurl) {
					saveurl = $t.p.url;
					$($t).jqGrid("setGridParam",{url:$t.p.searchurl});
				}
				var bcv = false;
				if($.isFunction(p.beforeClear)){bcv = p.beforeClear.call($t);}
				if(!bcv) $($t).jqGrid("setGridParam",{search:sd}).trigger("reloadGrid",[{page:1}]);
				if(saveurl) {$($t).jqGrid("setGridParam",{url:saveurl});}
				if($.isFunction(p.afterClear)){p.afterClear();}
			};
			var toggleToolbar = function(){
                var trow = $("tr.ui-search-toolbar",$t.grid.hDiv);
                if(trow.css("display")=='none') trow.show();
                else trow.hide();
			};
			// create the row
			function bindEvents(selector, events) {
				var jElem = $(selector);
				if (jElem[0] != null) {
				    jQuery.each(events, function() {
				        if (this.data != null)
				            jElem.bind(this.type, this.data, this.fn);
				        else
				            jElem.bind(this.type, this.fn);
				    });
				}
			}
			var tr = $("<tr class='ui-search-toolbar' role='rowheader'></tr>"), th,thd, soptions;
			$.each($t.p.colModel,function(i,n){
				var cm=this;
				th = $("<th role='columnheader' class='ui-state-default ui-th-column ui-th-"+$t.p.direction+"'></th>");
				thd = $("<div style='width:100%;position:relative;height:100%;padding-right:0.3em;'></div>");
				if(this.hidden===true) { $(th).css("display","none");}
				this.search = this.search === false ? false : true;
				if(typeof this.stype == 'undefined' ) {this.stype='text';}
				soptions = $.extend({},this.searchoptions || {});
				if(this.search){
					switch (this.stype)
					{
					case "select":
						var surl = this.surl || soptions.dataUrl;
						if(surl) {
							// data returned should have already constructed html select
							// primitive jQuery load
							var self = thd;
							$.ajax($.extend({
								url: surl,
								dataType: "html",
								complete: function(res,status) {
									if(soptions.buildSelect != null) {
										var d = soptions.buildSelect(res);
										if (d)
											$(self).append(d);
									} else 
										$(self).append(res.responseText);
									if(soptions.defaultValue) $("select",self).val(soptions.defaultValue);
									$("select",self).attr({name:cm.index || cm.name, id: "gs_"+cm.name});
									if(soptions.attr) {$("select",self).attr(soptions.attr);}
									$("select",self).css({width: "100%"});
									// preserve autoserch
									if(soptions.dataInit != null) soptions.dataInit($("select",self)[0]);
									if(soptions.dataEvents != null) bindEvents($("select",self)[0],soptions.dataEvents);
									if(p.autosearch===true){
										$("select",self).change(function(e){
											triggerToolbar();
											return false;
										});
									}
								}
							}, $.jgrid.ajaxOptions, $t.p.ajaxSelectOptions || {} ));
						} else {
							var oSv;
							if(cm.searchoptions && cm.searchoptions.value)
								oSv = cm.searchoptions.value;
							else if(cm.editoptions && cm.editoptions.value) {
								oSv = cm.editoptions.value;
							}
							if (oSv) {	
								var elem = document.createElement("select");
								elem.style.width = "100%";
								$(elem).attr({name:cm.index || cm.name, id: "gs_"+cm.name});
								if(typeof oSv === "string") {
									var so = oSv.split(";"), sv, ov;
									for(var k=0; k<so.length;k++){
										sv = so[k].split(":");
										ov = document.createElement("option");
										ov.value = sv[0]; ov.innerHTML = sv[1];
										elem.appendChild(ov);
									}
								} else if(typeof oSv === "object" ) {
									for ( var key in oSv) {
										ov = document.createElement("option");
										ov.value = key; ov.innerHTML = oSv[key];
										elem.appendChild(ov);
									}
								}
								if(soptions.defaultValue) $(elem).val(soptions.defaultValue);
								if(soptions.attr) {$(elem).attr(soptions.attr);}
								if(soptions.dataInit != null) soptions.dataInit(elem);
								if(soptions.dataEvents != null) bindEvents(elem, soptions.dataEvents);
								$(thd).append(elem);
								if(p.autosearch===true){
									$(elem).change(function(e){
										triggerToolbar();
										return false;
									});
								}
							}
						}
						break;
					case 'text':
						var df = soptions.defaultValue ? soptions.defaultValue: "";
						$(thd).append("<input type='text' style='width:95%;padding:0px;' name='"+(cm.index || cm.name)+"' id='gs_"+cm.name+"' value='"+df+"'/>");
						if(soptions.attr) {$("input",thd).attr(soptions.attr);}
						if(soptions.dataInit != null) soptions.dataInit($("input",thd)[0]);
						if(soptions.dataEvents != null) bindEvents($("input",thd)[0], soptions.dataEvents);
						if(p.autosearch===true){
							$("input",thd).keypress(function(e){
								var key = e.charCode ? e.charCode : e.keyCode ? e.keyCode : 0;
								if(key == 13){
									triggerToolbar();
									return false;
								}
								return this;
							});
						}
						break;
					}
				}
				$(th).append(thd);
				$(tr).append(th);
			});
			$("table thead",$t.grid.hDiv).append(tr);
			this.triggerToolbar = triggerToolbar;
			this.clearToolbar = clearToolbar;
			this.toggleToolbar = toggleToolbar;
		});
	}
});
})(jQuery);

/*
 * jqGrid common function
 * Tony Tomov tony@trirand.com
 * http://trirand.com/blog/ 
 * Dual licensed under the MIT and GPL licenses:
 * http://www.opensource.org/licenses/mit-license.php
 * http://www.gnu.org/licenses/gpl.html
*/ 
// Modal functions
var showModal = function(h) {
	h.w.show();
};
var closeModal = function(h) {
	h.w.hide().attr("aria-hidden","true");
	if(h.o) { h.o.remove(); }
};
var createModal = function(aIDs, content, p, insertSelector, posSelector, appendsel) {
	var mw  = document.createElement('div'), rtlsup;
	rtlsup = jQuery(p.gbox).attr("dir") == "rtl" ? true : false;
	mw.className= "ui-widget ui-widget-content ui-corner-all ui-jqdialog";
	mw.id = aIDs.themodal; 
	var mh = document.createElement('div');
	mh.className = "ui-jqdialog-titlebar ui-widget-header ui-corner-all ui-helper-clearfix";
	mh.id = aIDs.modalhead;
	jQuery(mh).append("<span class='ui-jqdialog-title'>"+p.caption+"</span>");
	var ahr= jQuery("<a href='javascript:void(0)' class='ui-jqdialog-titlebar-close ui-corner-all'></a>")
	.hover(function(){ahr.addClass('ui-state-hover');},
		   function(){ahr.removeClass('ui-state-hover');})
	.append("<span class='ui-icon ui-icon-closethick'></span>");
	jQuery(mh).append(ahr);
	if(rtlsup) {
		mw.dir = "rtl";
		jQuery(".ui-jqdialog-title",mh).css("float","right");
		jQuery(".ui-jqdialog-titlebar-close",mh).css("left",0.3+"em");
	} else {
		mw.dir = "ltr";
		jQuery(".ui-jqdialog-title",mh).css("float","left");
		jQuery(".ui-jqdialog-titlebar-close",mh).css("right",0.3+"em");
	}
	var mc = document.createElement('div');
	jQuery(mc).addClass("ui-jqdialog-content ui-widget-content").attr("id",aIDs.modalcontent);
	jQuery(mc).append(content);
	mw.appendChild(mc);
	jQuery(mw).prepend(mh);
	if(appendsel===true) { jQuery('body').append(mw); } //append as first child in body -for alert dialog
	else {jQuery(mw).insertBefore(insertSelector);}
	if(typeof p.jqModal === 'undefined') {p.jqModal = true;} // internal use
	var coord = {};
	if ( jQuery.fn.jqm && p.jqModal === true) {
		if(p.left ==0 && p.top==0) {
			var pos = [];
			pos = findPos(posSelector);
			p.left = pos[0] + 4;
			p.top = pos[1] + 4;
		}
		coord.top = p.top+"px";
		coord.left = p.left;
	} else if(p.left !=0 || p.top!=0) {
		coord.left = p.left;
		coord.top = p.top+"px";
	}
	jQuery("a.ui-jqdialog-titlebar-close",mh).click(function(e){
		var oncm = jQuery("#"+aIDs.themodal).data("onClose") || p.onClose;
		var gboxclose = jQuery("#"+aIDs.themodal).data("gbox") || p.gbox;
		hideModal("#"+aIDs.themodal,{gb:gboxclose,jqm:p.jqModal,onClose:oncm});
		return false;
	});
	if (p.width == 0 || !p.width) {p.width = 300;}
	if(p.height==0 || !p.height) {p.height =200;}
	if(!p.zIndex) {p.zIndex = 950;}
	var rtlt = 0;
	if( rtlsup && coord.left && !appendsel) {
		rtlt = jQuery(p.gbox).width()- (!isNaN(p.width) ? parseInt(p.width) :0) - 8; // to do
		// just in case
		coord.left = parseInt(coord.left) + parseInt(rtlt);
	}
	if(coord.left) coord.left += "px";
	jQuery(mw).css(jQuery.extend({
		width: isNaN(p.width) ? "auto": p.width+"px",
		height:isNaN(p.height) ? "auto" : p.height + "px",
		zIndex:p.zIndex,
		overflow: 'hidden'
	},coord))
	.attr({tabIndex: "-1","role":"dialog","aria-labelledby":aIDs.modalhead,"aria-hidden":"true"});
	if(typeof p.drag == 'undefined') { p.drag=true;}
	if(typeof p.resize == 'undefined') {p.resize=true;}
	if (p.drag) {
		jQuery(mh).css('cursor','move');
		if(jQuery.fn.jqDrag) {
			jQuery(mw).jqDrag(mh);
		} else {
			try {
				jQuery(mw).draggable({handle: jQuery("#"+mh.id)});
			} catch (e) {}
		}
	}
	if(p.resize) {
		if(jQuery.fn.jqResize) {
			jQuery(mw).append("<div class='jqResize ui-resizable-handle ui-resizable-se ui-icon ui-icon-gripsmall-diagonal-se ui-icon-grip-diagonal-se'></div>");
			jQuery("#"+aIDs.themodal).jqResize(".jqResize",aIDs.scrollelm ? "#"+aIDs.scrollelm : false);
		} else {
			try {
				jQuery(mw).resizable({handles: 'se, sw',alsoResize: aIDs.scrollelm ? "#"+aIDs.scrollelm : false});
			} catch (e) {}
		}
	}
	if(p.closeOnEscape === true){
		jQuery(mw).keydown( function( e ) {
			if( e.which == 27 ) {
				var cone = jQuery("#"+aIDs.themodal).data("onClose") || p.onClose;
				hideModal(this,{gb:p.gbox,jqm:p.jqModal,onClose: cone});
			}
		});
	}
};
var viewModal = function (selector,o){
	o = jQuery.extend({
		toTop: true,
		overlay: 10,
		modal: false,
		onShow: showModal,
		onHide: closeModal,
		gbox: '',
		jqm : true,
		jqM : true
	}, o || {});
	if (jQuery.fn.jqm && o.jqm == true) {
		if(o.jqM) jQuery(selector).attr("aria-hidden","false").jqm(o).jqmShow();
		else jQuery(selector).attr("aria-hidden","false").jqmShow();
	} else {
		if(o.gbox != '') {
			jQuery(".jqgrid-overlay:first",o.gbox).show();
			jQuery(selector).data("gbox",o.gbox);
		}
		jQuery(selector).show().attr("aria-hidden","false");
		try{jQuery(':input:visible',selector)[0].focus();}catch(_){}
	}
};
var hideModal = function (selector,o) {
	o = jQuery.extend({jqm : true, gb :''}, o || {});
    if(o.onClose) {
		var oncret =  o.onClose(selector);
		if (typeof oncret == 'boolean'  && !oncret ) return;
    }	
	if (jQuery.fn.jqm && o.jqm === true) {
		jQuery(selector).attr("aria-hidden","true").jqmHide();
	} else {
		if(o.gb != '') {
			try {jQuery(".jqgrid-overlay:first",o.gb).hide();} catch (e){}
		}
		jQuery(selector).hide().attr("aria-hidden","true");
	}
};

function info_dialog(caption, content,c_b, modalopt) {
	var mopt = {
		width:290,
		height:'auto',
		dataheight: 'auto',
		drag: true,
		resize: false,
		caption:"<b>"+caption+"</b>",
		left:250,
		top:170,
		zIndex : 1000,
		jqModal : true,
		closeOnEscape : true,
		align: 'center',
		buttonalign : 'center',
		buttons : []
		// {text:'textbutt', id:"buttid", onClick : function(){...}}
		// if the id is not provided we set it like info_button_+ the index in the array - i.e info_button_0,info_button_1...
	};
	jQuery.extend(mopt,modalopt || {});
	var jm = mopt.jqModal;
	if(jQuery.fn.jqm && !jm) jm = false;
	// in case there is no jqModal
	var buttstr ="";
	if(mopt.buttons.length > 0) {
		for(var i=0;i<mopt.buttons.length;i++) {
			if(typeof mopt.buttons[i].id == "undefined") mopt.buttons[i].id = "info_button_"+i;
			buttstr += "<a href='javascript:void(0)' id='"+mopt.buttons[i].id+"' class='fm-button ui-state-default ui-corner-all'>"+mopt.buttons[i].text+"</a>";
		}
	}
	var dh = isNaN(mopt.dataheight) ? mopt.dataheight : mopt.dataheight+"px",
	cn = "text-align:"+mopt.align+";";
	var cnt = "<div id='info_id'>";
	cnt += "<div id='infocnt' style='margin:0px;padding-bottom:1em;width:100%;overflow:auto;position:relative;height:"+dh+";"+cn+"'>"+content+"</div>";
	cnt += c_b ? "<div class='ui-widget-content ui-helper-clearfix' style='text-align:"+mopt.buttonalign+";padding-bottom:0.8em;padding-top:0.5em;background-image: none;border-width: 1px 0 0 0;'><a href='javascript:void(0)' id='closedialog' class='fm-button ui-state-default ui-corner-all'>"+c_b+"</a>"+buttstr+"</div>" : "";
	cnt += "</div>";

	try {
		if(jQuery("#info_dialog").attr("aria-hidden") == "false")
			hideModal("#info_dialog",{jqm:jm});
		jQuery("#info_dialog").remove();
	} catch (e){}
	createModal({
		themodal:'info_dialog',
		modalhead:'info_head',
		modalcontent:'info_content',
		scrollelm: 'infocnt'},
		cnt,
		mopt,
		'','',true
	);
	// attach onclick after inserting into the dom
	if(buttstr) {
		jQuery.each(mopt.buttons,function(i){
			jQuery("#"+this.id,"#info_id").bind('click',function(){mopt.buttons[i].onClick.call(jQuery("#info_dialog")); return false;});
		});
	}
	jQuery("#closedialog", "#info_id").click(function(e){
		hideModal("#info_dialog",{jqm:jm});
		return false;
	});
	jQuery(".fm-button","#info_dialog").hover(
		function(){jQuery(this).addClass('ui-state-hover');}, 
		function(){jQuery(this).removeClass('ui-state-hover');}
	);
	viewModal("#info_dialog",{
		onHide: function(h) {
			h.w.hide().remove();
			if(h.o) { h.o.remove(); }
		},
		modal :true,
		jqm:jm
	});
}
//Helper functions
function findPos(obj) {
	var curleft = curtop = 0;
	if (obj.offsetParent) {
		do {
			curleft += obj.offsetLeft;
			curtop += obj.offsetTop; 
		} while (obj = obj.offsetParent);
		//do not change obj == obj.offsetParent 
	}
	return [curleft,curtop];
}
function isArray(obj) {
	if (obj.constructor.toString().indexOf("Array") == -1) {
		return false;
	} else {
		return true;
	}
}
// Form Functions
function createEl(eltype,options,vl,autowidth, ajaxso) {
	var elem = "";
	if(options.defaultValue) delete options['defaultValue'];
	function bindEv (el, opt) {
		if(jQuery.isFunction(opt.dataInit)) {
			// datepicker fix 
			el.id = opt.id;
			opt.dataInit(el);
			delete opt['id'];
			delete opt['dataInit'];
		}
		if(opt.dataEvents) {
		    jQuery.each(opt.dataEvents, function() {
		        if (this.data != null)
			        jQuery(el).bind(this.type, this.data, this.fn);
		        else
		            jQuery(el).bind(this.type, this.fn);
		    });
			delete opt['dataEvents'];
		}
		return opt;
	}
	switch (eltype)
	{
		case "textarea" :
				elem = document.createElement("textarea");
				if(autowidth) {
					if(!options.cols) jQuery(elem).css({width:"98%"});
				} else if (!options.cols) options.cols = 20;
				if(!options.rows) options.rows = 2;
				if(vl=='&nbsp;' || vl=='&#160;' || (vl.length==1 && vl.charCodeAt(0)==160)) {vl="";}
				elem.value = vl;
				options = bindEv(elem,options);
				jQuery(elem).attr(options);
				break;
		case "checkbox" : //what code for simple checkbox
			elem = document.createElement("input");
			elem.type = "checkbox";
			if( !options.value ) {
				var vl1 = vl.toLowerCase();
				if(vl1.search(/(false|0|no|off|undefined)/i)<0 && vl1!=="") {
					elem.checked=true;
					elem.defaultChecked=true;
					elem.value = vl;
				} else {
					elem.value = "on";
				}
				jQuery(elem).attr("offval","off");
			} else {
				var cbval = options.value.split(":");
				if(vl === cbval[0]) {
					elem.checked=true;
					elem.defaultChecked=true;
				}
				elem.value = cbval[0];
				jQuery(elem).attr("offval",cbval[1]);
				try {delete options['value'];} catch (e){}
			}
			options = bindEv(elem,options);
			jQuery(elem).attr(options);
			break;
		case "select" :
			elem = document.createElement("select");
			var msl, ovm = [];
			if(options.multiple===true) {
				msl = true;
				elem.multiple="multiple";
			} else msl = false;
			if(options.dataUrl != null) {
				jQuery.ajax(jQuery.extend({
					url: options.dataUrl,
					type : "GET",
					complete: function(data,status){
						try {delete options['dataUrl'];delete options['value'];} catch (e){}
						var a;
						if(options.buildSelect != null) {
							var b = options.buildSelect(data);
							a = jQuery(b).html();
							delete options['buildSelect'];
						} else 
							a = jQuery(data.responseText).html();
						if(a) {
							jQuery(elem).append(a);
							options = bindEv(elem,options);
							if(typeof options.size === 'undefined') { options.size =  msl ? 3 : 1;}
							if(msl) {
								ovm = vl.split(",");
								ovm = jQuery.map(ovm,function(n){return jQuery.trim(n)});
							} else {
								ovm[0] = vl;
							}
							jQuery(elem).attr(options);
							setTimeout(function(){
								jQuery("option",elem).each(function(i){
									if(i==0) this.selected = "";
									if(jQuery.inArray(jQuery(this).text(),ovm) > -1 || jQuery.inArray(jQuery(this).val(),ovm)>-1) {
										this.selected= "selected";
										if(!msl) return false;
									}
								});
							},0);
						}
					}
				},ajaxso || {}));
			} else if(options.value) {
				var i;
				if(msl) {
					ovm = vl.split(",");
					ovm = jQuery.map(ovm,function(n){return jQuery.trim(n)});
					if(typeof options.size === 'undefined') {options.size = 3;}
				} else {
					options.size = 1;
				}
				if(typeof options.value === 'function') options.value = options.value();
				if(typeof options.value === 'string') {
					var so = options.value.split(";"),sv, ov;
					for(i=0; i<so.length;i++){
						sv = so[i].split(":");
						ov = document.createElement("option");
						ov.value = sv[0]; ov.innerHTML = sv[1];
						if (!msl &&  (sv[0] == vl || sv[1]==vl)) ov.selected ="selected";
						if (msl && (jQuery.inArray(sv[1], ovm)>-1 || jQuery.inArray(sv[0], ovm)>-1)) {ov.selected ="selected";}
						elem.appendChild(ov);
					}
				} else if (typeof options.value === 'object') {
					var oSv = options.value;
					for ( var key in oSv) {
						ov = document.createElement("option");
						ov.value = key; ov.innerHTML = oSv[key];
						if (!msl &&  (key == vl ||oSv[key]==vl) ) ov.selected ="selected";
						if (msl && (jQuery.inArray(oSv[key],ovm)>-1 || jQuery.inArray(key,ovm)>-1)) ov.selected ="selected";
						elem.appendChild(ov);
					}
				}
				options = bindEv(elem,options);
				try {delete options['value'];} catch (e){}
				jQuery(elem).attr(options);
			}
			break;
		case "text" :
		case "password" :
		case "button" :
			elem = document.createElement("input");
			elem.type = eltype;
			elem.value = jQuery.jgrid.htmlDecode(vl);
			options = bindEv(elem,options);
			if(eltype != "button"){
				if(autowidth) {
					if(!options.size) jQuery(elem).css({width:"98%"});
				} else if (!options.size) options.size = 20;
			}
			jQuery(elem).attr(options);
			break;
		case "image" :
		case "file" :
			elem = document.createElement("input");
			elem.type = eltype;
			options = bindEv(elem,options);
			jQuery(elem).attr(options);
			break;
		case "custom" :
			elem = document.createElement("span");
			try {
				if(jQuery.isFunction(options.custom_element)) {
					var celm = options.custom_element.call(this,vl,options);
					if(celm) {
						celm = jQuery(celm).addClass("customelement").attr({id:options.id,name:options.name});
						jQuery(elem).empty().append(celm);
					}
					else throw "e2";
				} else 	throw "e1";
			} catch (e) {
				if (e=="e1") info_dialog(jQuery.jgrid.errors.errcap,"function 'custom_element' "+jQuery.jgrid.edit.msg.nodefined, jQuery.jgrid.edit.bClose);
				if (e=="e2") info_dialog(jQuery.jgrid.errors.errcap,"function 'custom_element' "+jQuery.jgrid.edit.msg.novalue,jQuery.jgrid.edit.bClose);
				else info_dialog(jQuery.jgrid.errors.errcap,e.message,jQuery.jgrid.edit.bClose);
			}
			break;
	}
	return elem;
}
function checkValues(val, valref,g) {
	var edtrul,i, nm;
	if(typeof(valref)=='string'){
		for( i =0, len=g.p.colModel.length;i<len; i++){
			if(g.p.colModel[i].name==valref) {
				edtrul = g.p.colModel[i].editrules;
				valref = i;
				try { nm = g.p.colModel[i].formoptions.label; } catch (e) {}
				break;
			}
		}
	} else if(valref >=0) {
		edtrul = g.p.colModel[valref].editrules;
	}
	if(edtrul) {
		if(!nm) nm = g.p.colNames[valref];
		if(edtrul.required === true) {
			if( val.match(/^s+$/) || val == "" )  return [false,nm+": "+jQuery.jgrid.edit.msg.required,""];
		}
		// force required
		var rqfield = edtrul.required === false ? false : true;
		if(edtrul.number === true) {
			if( !(rqfield === false && isEmpty(val)) ) {
				if(isNaN(val)) return [false,nm+": "+jQuery.jgrid.edit.msg.number,""];
			}
		}
		if(typeof edtrul.minValue != 'undefined' && !isNaN(edtrul.minValue)) {
			if (parseFloat(val) < parseFloat(edtrul.minValue) ) return [false,nm+": "+jQuery.jgrid.edit.msg.minValue+" "+edtrul.minValue,""];
		}
		if(typeof edtrul.maxValue != 'undefined' && !isNaN(edtrul.maxValue)) {
			if (parseFloat(val) > parseFloat(edtrul.maxValue) ) return [false,nm+": "+jQuery.jgrid.edit.msg.maxValue+" "+edtrul.maxValue,""];
		}
		var filter;
		if(edtrul.email === true) {
			if( !(rqfield === false && isEmpty(val)) ) {
			// taken from jquery Validate plugin
				filter = /^((([a-z]|\d|[!#\$%&'\*\+\-\/=\?\^_`{\|}~]|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])+(\.([a-z]|\d|[!#\$%&'\*\+\-\/=\?\^_`{\|}~]|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])+)*)|((\x22)((((\x20|\x09)*(\x0d\x0a))?(\x20|\x09)+)?(([\x01-\x08\x0b\x0c\x0e-\x1f\x7f]|\x21|[\x23-\x5b]|[\x5d-\x7e]|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])|(\\([\x01-\x09\x0b\x0c\x0d-\x7f]|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF]))))*(((\x20|\x09)*(\x0d\x0a))?(\x20|\x09)+)?(\x22)))@((([a-z]|\d|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])|(([a-z]|\d|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])([a-z]|\d|-|\.|_|~|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])*([a-z]|\d|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])))\.)+(([a-z]|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])|(([a-z]|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])([a-z]|\d|-|\.|_|~|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])*([a-z]|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])))\.?$/i;
				if(!filter.test(val)) {return [false,nm+": "+jQuery.jgrid.edit.msg.email,""];}
			}
		}
		if(edtrul.integer === true) {
			if( !(rqfield === false && isEmpty(val)) ) {
				if(isNaN(val)) return [false,nm+": "+jQuery.jgrid.edit.msg.integer,""];
				if ((val % 1 != 0) || (val.indexOf('.') != -1)) return [false,nm+": "+jQuery.jgrid.edit.msg.integer,""];
			}
		}
		if(edtrul.date === true) {
			if( !(rqfield === false && isEmpty(val)) ) {
				var dft = g.p.colModel[valref].datefmt || "Y-m-d";
				if(!checkDate (dft, val)) return [false,nm+": "+jQuery.jgrid.edit.msg.date+" - "+dft,""];
			}
		}
		if(edtrul.time === true) {
			if( !(rqfield === false && isEmpty(val)) ) {
				if(!checkTime (val)) return [false,nm+": "+jQuery.jgrid.edit.msg.date+" - hh:mm (am/pm)",""];
			}
		}
        if(edtrul.url === true) {
            if( !(rqfield === false && isEmpty(val)) ) {
                filter = /^(((https?)|(ftp)):\/\/([\-\w]+\.)+\w{2,3}(\/[%\-\w]+(\.\w{2,})?)*(([\w\-\.\?\\\/+@&#;`~=%!]*)(\.\w{2,})?)*\/?)/i;
                if(!filter.test(val)) {return [false,nm+": "+jQuery.jgrid.edit.msg.url,""];}
            }
        }
		if(edtrul.custom === true) {
            if( !(rqfield === false && isEmpty(val)) ) {
				if(jQuery.isFunction(edtrul.custom_func)) {
					var ret = edtrul.custom_func.call(g,val,nm);
					if(jQuery.isArray(ret)) {
						return ret;
					} else {
						return [false,jQuery.jgrid.edit.msg.customarray,""];
					}
				} else {
					return [false,jQuery.jgrid.edit.msg.customfcheck,""];
				}
			}
		}
	}
	return [true,"",""];
}
// Date Validation Javascript
function checkDate (format, date) {
	var tsp = {}, sep;
	format = format.toLowerCase();
	//we search for /,-,. for the date separator
	if(format.indexOf("/") != -1) {
		sep = "/";
	} else if(format.indexOf("-") != -1) {
		sep = "-";
	} else if(format.indexOf(".") != -1) {
		sep = ".";
	} else {
		sep = "/";
	}
	format = format.split(sep);
	date = date.split(sep);
	if (date.length != 3) return false;
	var j=-1,yln, dln=-1, mln=-1;
	for(var i=0;i<format.length;i++){
		var dv =  isNaN(date[i]) ? 0 : parseInt(date[i],10); 
		tsp[format[i]] = dv;
		yln = format[i];
		if(yln.indexOf("y") != -1) { j=i; }
		if(yln.indexOf("m") != -1) {mln=i}
		if(yln.indexOf("d") != -1) {dln=i}
	}
	if (format[j] == "y" || format[j] == "yyyy") {
		yln=4;
	} else if(format[j] =="yy"){
		yln = 2;
	} else {
		yln = -1;
	}
	var daysInMonth = DaysArray(12);
	var strDate;
	if (j === -1) {
		return false;
	} else {
		strDate = tsp[format[j]].toString();
		if(yln == 2 && strDate.length == 1) {yln = 1;}
		if (strDate.length != yln || tsp[format[j]]==0 ){
			return false;
		}
	}
	if(mln === -1) {
		return false;
	} else {
		strDate = tsp[format[mln]].toString();
		if (strDate.length<1 || tsp[format[mln]]<1 || tsp[format[mln]]>12){
			return false;
		}
	}
	if(dln === -1) {
		return false;
	} else {
		strDate = tsp[format[dln]].toString();
		if (strDate.length<1 || tsp[format[dln]]<1 || tsp[format[dln]]>31 || (tsp[format[mln]]==2 && tsp[format[dln]]>daysInFebruary(tsp[format[j]])) || tsp[format[dln]] > daysInMonth[tsp[format[mln]]]){
			return false;
		}
	}
	return true;
}
function daysInFebruary (year){
	// February has 29 days in any year evenly divisible by four,
    // EXCEPT for centurial years which are not also divisible by 400.
    return (((year % 4 == 0) && ( (!(year % 100 == 0)) || (year % 400 == 0))) ? 29 : 28 );
}
function DaysArray(n) {
	for (var i = 1; i <= n; i++) {
		this[i] = 31;
		if (i==4 || i==6 || i==9 || i==11) {this[i] = 30;}
		if (i==2) {this[i] = 29;}
	} 
	return this;
}

function isEmpty(val)
{
	if (val.match(/^s+$/) || val == "")	{
		return true;
	} else {
		return false;
	} 
}
function checkTime(time){
	// checks only hh:ss (and optional am/pm)
	var re = /^(\d{1,2}):(\d{2})([ap]m)?$/,regs;
	if(!isEmpty(time))
	{
		regs = time.match(re);
		if(regs) {
			if(regs[3]) {
				if(regs[1] < 1 || regs[1] > 12) 
					return false;
			} else {
				if(regs[1] > 23) 
					return false;
			}
			if(regs[2] > 59) {
				return false;
			}
		} else {
			return false;
		}
	}
	return true;
}
/*
 * jqModal - Minimalist Modaling with jQuery
 *   (http://dev.iceburg.net/jquery/jqmodal/)
 *
 * Copyright (c) 2007,2008 Brice Burgess <bhb@iceburg.net>
 * Dual licensed under the MIT and GPL licenses:
 *   http://www.opensource.org/licenses/mit-license.php
 *   http://www.gnu.org/licenses/gpl.html
 * 
 * $Version: 07/06/2008 +r13
 */
(function($) {
$.fn.jqm=function(o){
var p={
overlay: 50,
closeoverlay : true,
overlayClass: 'jqmOverlay',
closeClass: 'jqmClose',
trigger: '.jqModal',
ajax: F,
ajaxText: '',
target: F,
modal: F,
toTop: F,
onShow: F,
onHide: F,
onLoad: F
};
return this.each(function(){if(this._jqm)return H[this._jqm].c=$.extend({},H[this._jqm].c,o);s++;this._jqm=s;
H[s]={c:$.extend(p,$.jqm.params,o),a:F,w:$(this).addClass('jqmID'+s),s:s};
if(p.trigger)$(this).jqmAddTrigger(p.trigger);
});};

$.fn.jqmAddClose=function(e){return hs(this,e,'jqmHide');};
$.fn.jqmAddTrigger=function(e){return hs(this,e,'jqmShow');};
$.fn.jqmShow=function(t){return this.each(function(){$.jqm.open(this._jqm,t);});};
$.fn.jqmHide=function(t){return this.each(function(){$.jqm.close(this._jqm,t)});};

$.jqm = {
hash:{},
open:function(s,t){var h=H[s],c=h.c,cc='.'+c.closeClass,z=(parseInt(h.w.css('z-index')));z=(z>0)?z:3000;var o=$('<div></div>').css({height:'100%',width:'100%',position:'fixed',left:0,top:0,'z-index':z-1,opacity:c.overlay/100});if(h.a)return F;h.t=t;h.a=true;h.w.css('z-index',z);
 if(c.modal) {if(!A[0])setTimeout(function(){L('bind');},1);A.push(s);}
 else if(c.overlay > 0) {if(c.closeoverlay) h.w.jqmAddClose(o);}
 else o=F;

 h.o=(o)?o.addClass(c.overlayClass).prependTo('body'):F;
 if(ie6){$('html,body').css({height:'100%',width:'100%'});if(o){o=o.css({position:'absolute'})[0];for(var y in {Top:1,Left:1})o.style.setExpression(y.toLowerCase(),"(_=(document.documentElement.scroll"+y+" || document.body.scroll"+y+"))+'px'");}}

 if(c.ajax) {var r=c.target||h.w,u=c.ajax;r=(typeof r == 'string')?$(r,h.w):$(r);u=(u.substr(0,1) == '@')?$(t).attr(u.substring(1)):u;
  r.html(c.ajaxText).load(u,function(){if(c.onLoad)c.onLoad.call(this,h);if(cc)h.w.jqmAddClose($(cc,h.w));e(h);});}
 else if(cc)h.w.jqmAddClose($(cc,h.w));

 if(c.toTop&&h.o)h.w.before('<span id="jqmP'+h.w[0]._jqm+'"></span>').insertAfter(h.o);	
 (c.onShow)?c.onShow(h):h.w.show();e(h);return F;
},
close:function(s){var h=H[s];if(!h.a)return F;h.a=F;
 if(A[0]){A.pop();if(!A[0])L('unbind');}
 if(h.c.toTop&&h.o)$('#jqmP'+h.w[0]._jqm).after(h.w).remove();
 if(h.c.onHide)h.c.onHide(h);else{h.w.hide();if(h.o)h.o.remove();} return F;
},
params:{}};
var s=0,H=$.jqm.hash,A=[],ie6=$.browser.msie&&($.browser.version == "6.0"),F=false,
e=function(h){var i=$('<iframe src="javascript:false;document.write(\'\');" class="jqm"></iframe>').css({opacity:0});if(ie6)if(h.o)h.o.html('<p style="width:100%;height:100%"/>').prepend(i);else if(!$('iframe.jqm',h.w)[0])h.w.prepend(i); f(h);},
f=function(h){try{$(':input:visible',h.w)[0].focus();}catch(_){}},
L=function(t){$()[t]("keypress",m)[t]("keydown",m)[t]("mousedown",m);},
m=function(e){var h=H[A[A.length-1]],r=(!$(e.target).parents('.jqmID'+h.s)[0]);if(r)f(h);return !r;},
hs=function(w,t,c){return w.each(function(){var s=this._jqm;$(t).each(function() {
 if(!this[c]){this[c]=[];$(this).click(function(){for(var i in {jqmShow:1,jqmHide:1})for(var s in this[i])if(H[this[i][s]])H[this[i][s]].w[i](this);return F;});}this[c].push(s);});});};
})(jQuery);
;(function($){
/*
**
 * jqGrid addons using jQuery UI 
 * Author: Mark Williams
 * Dual licensed under the MIT and GPL licenses:
 * http://www.opensource.org/licenses/mit-license.php
 * http://www.gnu.org/licenses/gpl.html
 * depends on jQuery UI sortable
**/
if ($.browser.msie && $.browser.version==8) {
	$.expr[":"].hidden = function(elem) {
		return elem.offsetWidth === 0 || elem.offsetHeight === 0 ||
			elem.style.display == "none";
	}
}

if ($.ui && $.ui.multiselect && $.ui.multiselect.prototype._setSelected) {
    var setSelected = $.ui.multiselect.prototype._setSelected;
    $.ui.multiselect.prototype._setSelected = function(item,selected) {
        var ret = setSelected.call(this,item,selected);
        if (selected && this.selectedList) {
            var elt = this.element;
		    this.selectedList.find('li').each(function() {
			    if ($(this).data('optionLink'))
				    $(this).data('optionLink').remove().appendTo(elt);
		    });
        }
        return ret;
    }
}
        
$.jgrid.extend({
	sortableColumns : function (tblrow)
	{
		return this.each(function (){
			var ts = this;
			function start() {ts.p.disableClick = true;};
			var sortable_opts = {
				"tolerance" : "pointer",
				"axis" : "x",
				"items": '>th:not(:has(#jqgh_cb,#jqgh_rn,#jqgh_subgrid),:hidden)',
				"placeholder": {
					element: function(item) {
						var el = $(document.createElement(item[0].nodeName))
						.addClass(item[0].className+" ui-sortable-placeholder ui-state-highlight")
						.removeClass("ui-sortable-helper")[0];
						return el;
					},
					update: function(self, p) {
						p.height(self.currentItem.innerHeight() - parseInt(self.currentItem.css('paddingTop')||0, 10) - parseInt(self.currentItem.css('paddingBottom')||0, 10));
						p.width(self.currentItem.innerWidth() - parseInt(self.currentItem.css('paddingLeft')||0, 10) - parseInt(self.currentItem.css('paddingRight')||0, 10));
					}
				},
				"update": function(event, ui) {
					var p = $(ui.item).parent();
					var th = $(">th", p);
					var colModel = ts.p.colModel;
					var cmMap = {};
					$.each(colModel, function(i) { cmMap[this.name]=i });
					var permutation = [];
					th.each(function(i) {
						var id = $(">div", this).get(0).id.replace(/^jqgh_/, "");
							if (id in cmMap) {
								permutation.push(cmMap[id]);
							}
					});
	
					$(ts).jqGrid("remapColumns",permutation, true, true);
					if ($.isFunction(ts.p.sortable.update)) {
						ts.p.sortable.update(permutation);
					}
					setTimeout(function(){ts.p.disableClick=false}, 50);
				}
			};
			if (ts.p.sortable.options) {
				$.extend(sortable_opts, ts.p.sortable.options);
			} else if ($.isFunction(ts.p.sortable)) {
				ts.p.sortable = { "update" : ts.p.sortable };
			}
			if (sortable_opts.start) {
				var s = sortable_opts.start;
				sortable_opts.start = function(e,ui) {
					start();
					s.call(this,e,ui);
				}
			} else {
				sortable_opts.start = start;
			}
			if (ts.p.sortable.exclude) {
				sortable_opts.items += ":not("+ts.p.sortable.exclude+")";
			}
			tblrow.sortable(sortable_opts).data("sortable").floating = true;
		});
	},
    columnChooser : function(opts) {
        var self = this;
        var selector = $('<div style="position:relative;overflow:hidden"><div><select multiple="multiple"></select></div></div>');
        var select = $('select', selector);

        opts = $.extend({
            "width" : 420,
            "height" : 240,
            "classname" : null,
            "done" : function(perm) { if (perm) self.jqGrid("remapColumns", perm, true) },
            /* msel is either the name of a ui widget class that
               extends a multiselect, or a function that supports
               creating a multiselect object (with no argument,
               or when passed an object), and destroying it (when
               passed the string "destroy"). */
            "msel" : "multiselect",
            /* "msel_opts" : {}, */

            /* dlog is either the name of a ui widget class that 
               behaves in a dialog-like way, or a function, that
               supports creating a dialog (when passed dlog_opts)
               or destroying a dialog (when passed the string
               "destroy")
               */
            "dlog" : "dialog",

            /* dlog_opts is either an option object to be passed 
               to "dlog", or (more likely) a function that creates
               the options object.
               The default produces a suitable options object for
               ui.dialog */
            "dlog_opts" : function(opts) {
                var buttons = {};
                buttons[opts.bSubmit] = function() {
                    opts.apply_perm();
                    opts.cleanup(false);
                };
                buttons[opts.bCancel] = function() {
                    opts.cleanup(true);
                };
                return {
                    "buttons": buttons,
                    "close": function() {
                        opts.cleanup(true);
                    },
					"modal" : false,
                    "resizable": false,
                    "width": opts.width+20
                };
            },
            /* Function to get the permutation array, and pass it to the
               "done" function */
            "apply_perm" : function() {
                $('option',select).each(function(i) {
                    if (this.selected) {
                        self.jqGrid("showCol", colModel[this.value].name);
                    } else {
                        self.jqGrid("hideCol", colModel[this.value].name);
                    }
                });
                
                var perm = fixedCols.slice(0);
                $('option[selected]',select).each(function() { perm.push(parseInt(this.value)) });
                $.each(perm, function() { delete colMap[colModel[this].name] });
                $.each(colMap, function() { perm.push(parseInt(this)) });
                if (opts.done) {
                    opts.done.call(self, perm);
                }
            },
            /* Function to cleanup the dialog, and select. Also calls the
               done function with no permutation (to indicate that the
               columnChooser was aborted */
            "cleanup" : function(calldone) {
                call(opts.dlog, selector, 'destroy');
                call(opts.msel, select, 'destroy');
                selector.remove();
                if (calldone && opts.done) {
                    opts.done.call(self);
                }
            }
        }, $.jgrid.col, opts || {});

        if (opts.caption) {
            selector.attr("title", opts.caption);
        }
        if (opts.classname) {
            selector.addClass(classname);
            select.addClass(classname);
        }
        if (opts.width) {
            $(">div",selector).css({"width": opts.width,"margin":"0 auto"});
            select.css("width", opts.width);
        }
        if (opts.height) {
            $(">div",selector).css("height", opts.height);
            select.css("height", opts.height - 10);
        }
        var colModel = self.jqGrid("getGridParam", "colModel");
        var colNames = self.jqGrid("getGridParam", "colNames");
        var colMap = {}, fixedCols = [];

        select.empty();
        $.each(colModel, function(i) {
            colMap[this.name] = i;
            if (this.hidedlg) {
                if (!this.hidden) {
                    fixedCols.push(i);
                }
                return;
            }

            select.append("<option value='"+i+"' "+
                          (this.hidden?"":"selected='selected'")+">"+colNames[i]+"</option>");
        });
        function call(fn, obj) {
            if (!fn) return;
            if (typeof fn == 'string') {
                if ($.fn[fn]) {
                    $.fn[fn].apply(obj, $.makeArray(arguments).slice(2));
                }
            } else if ($.isFunction(fn)) {
                fn.apply(obj, $.makeArray(arguments).slice(2));
            }
        }

        var dopts = $.isFunction(opts.dlog_opts) ? opts.dlog_opts.call(self, opts) : opts.dlog_opts;
        call(opts.dlog, selector, dopts);
        var mopts = $.isFunction(opts.msel_opts) ? opts.msel_opts.call(self, opts) : opts.msel_opts;
        call(opts.msel, select, opts.msel_opts);
    },
	sortableRows : function (opts) {
		// Can accept all sortable options and events
		return this.each(function(){
			var $t = this;
			if(!$t.grid) return;
			// Currently we disable a treeGrid sortable
			if($t.p.treeGrid) return;
			if($.fn['sortable']) {
				opts = $.extend({
					"cursor":"move",
					"axis" : "y",
					"items": ".jqgrow"
					},
				opts || {});
				if(opts.start && $.isFunction(opts.start)) {
					opts._start_ = opts.start;
					delete opts.start;
				} else {opts._start_=false;}
				if(opts.update && $.isFunction(opts.update)) {
					opts._update_ = opts.update;
					delete opts.update;
				} else {opts._update_ = false;}
				opts.start = function(ev,ui) {
					$(ui.item).css("border-width","0px");
					$("td",ui.item).each(function(i){
						this.style.width = $t.grid.cols[i].style.width;
					});
					if($t.p.subGrid) {
						var subgid = $(ui.item).attr("id");
						try {
							$($t).jqGrid('collapseSubGridRow',subgid);
						} catch (e) {}
					}
					if(opts._start_) {
						opts._start_.apply(this,[ev,ui]);
					}
				}
				opts.update = function (ev,ui) {
					$(ui.item).css("border-width","");
					$t.updateColumns();
					if(opts._update_) {
						opts._update_.apply(this,[ev,ui]);
					}
				}
				$("tbody:first",$t).sortable(opts);
			}
		});
	},
	gridDnD : function(opts) {
		return this.each(function(){
		var $t = this;
		if(!$t.grid) return;
		// Currently we disable a treeGrid drag and drop
		if($t.p.treeGrid) return;
		if(!$.fn['draggable'] || !$.fn['droppable']) return;
		function updateDnD ()
		{
			var datadnd = $.data($t,"dnd");
		    $("tr.jqgrow:not(.ui-draggable)",$t).draggable($.isFunction(datadnd.drag) ? datadnd.drag.call($($t),datadnd) : datadnd.drag);
		}
		var appender = "<table id='jqgrid_dnd' class='ui-jqgrid-dnd'></table>";
		if($("#jqgrid_dnd").html() == null) {
			$('body').append(appender);
		}

		if(typeof opts == 'string' && opts == 'updateDnD' && $t.p.jqgdnd==true) {
			updateDnD();
			return;
		}
		opts = $.extend({
			"drag" : function (opts) {
				return $.extend({
					start : function (ev, ui) {
						// if we are in subgrid mode try to collapse the node
						if($t.p.subGrid) {
							var subgid = $(ui.helper).attr("id");
							try {
								$($t).jqGrid('collapseSubGridRow',subgid);
							} catch (e) {}
						}
						// hack
						// drag and drop does not insert tr in table, when the table has no rows
						// we try to insert new empty row on the target(s)
						for (var i=0;i<opts.connectWith.length;i++){
							if($(opts.connectWith[i]).jqGrid('getGridParam','reccount') == "0" ){
								$(opts.connectWith[i]).jqGrid('addRowData','jqg_empty_row',{});
							}
						}
						ui.helper.addClass("ui-state-highlight");
						$("td",ui.helper).each(function(i) {
							this.style.width = $t.grid.headers[i].width+"px";
						});
						if(opts.onstart && $.isFunction(opts.onstart) ) opts.onstart.call($($t),ev,ui);
					},
					stop :function(ev,ui) {
						if(ui.helper.dropped) {
							var ids = $(ui.helper).attr("id");
							$($t).jqGrid('delRowData',ids );
						}
						// if we have a empty row inserted from start event try to delete it 
						for (var i=0;i<opts.connectWith.length;i++){
							$(opts.connectWith[i]).jqGrid('delRowData','jqg_empty_row');
						}
						if(opts.onstop && $.isFunction(opts.onstop) ) opts.onstop.call($($t),ev,ui);
					}
				},opts.drag_opts || {});
			},
			"drop" : function (opts) {
				return $.extend({
					accept: function(d) {
						var tid = $(d).closest("table.ui-jqgrid-btable");
						var cn = $.data(tid[0],"dnd").connectWith;
						return $.inArray('#'+this.id,cn) != -1 ? true : false;
					},
					drop: function(ev, ui) {
						var accept = $(ui.draggable).attr("id");
						var getdata = $('#'+$t.id).jqGrid('getRowData',accept);
						if(!opts.dropbyname) {
							var j =0, tmpdata = {}, dropname;
							var dropmodel = $("#"+this.id).jqGrid('getGridParam','colModel');
							try {
								for (key in getdata) {
									if(dropmodel[j]) {
										dropname = dropmodel[j].name;
										tmpdata[dropname] = getdata[key];
									}
									j++;
								}
								getdata = tmpdata;
							} catch (e) {}
 						}
						ui.helper.dropped = true;
						if(opts.beforedrop && $.isFunction(opts.beforedrop) ) {
							//parameters to this callback - event, element, data to be inserted, sender, reciever
							// should return object which will be inserted into the reciever
							var datatoinsert = opts.beforedrop.call(this,ev,ui,getdata,$('#'+$t.id),$(this));
							if (typeof datatoinsert != "undefined" && datatoinsert !== null && typeof datatoinsert == "object") getdata = datatoinsert;
						}
						if(ui.helper.dropped) {
							var grid;
							if(opts.autoid) {
								if($.isFunction(opts.autoid)) {
									grid = opts.autoid.call(this,getdata);
								} else {
									grid = Math.ceil(Math.random()*1000);
									grid = opts.autoidprefix+grid;
								}
							}
							// NULL is interpreted as undefined while null as object
							$("#"+this.id).jqGrid('addRowData',grid,getdata,opts.droppos);
						}
						if(opts.ondrop && $.isFunction(opts.ondrop) ) opts.ondrop.call(this,ev,ui, getdata);
					}}, opts.drop_opts || {});
			},
			"onstart" : null,
			"onstop" : null,
			"beforedrop": null,
			"ondrop" : null,
			"drop_opts" : {
				"activeClass": "ui-state-active",
				"hoverClass": "ui-state-hover"
			},
			"drag_opts" : {
				"revert": "invalid",
				"helper": "clone",
				"cursor": "move",
				"appendTo" : "#jqgrid_dnd",
				"zIndex": 5000
			},
			"dropbyname" : false,
			"droppos" : "first",
			"autoid" : true,
			"autoidprefix" : "dnd_"
		}, opts || {});
		
		if(!opts.connectWith) return;
		opts.connectWith = opts.connectWith.split(",");
		opts.connectWith = $.map(opts.connectWith,function(n){return $.trim(n);});
		$.data($t,"dnd",opts);
		
		if($t.p.reccount != "0" && !$t.p.jqgdnd) {
			updateDnD();
		}
		$t.p.jqgdnd = true;
		for (var i=0;i<opts.connectWith.length;i++){
			var cn =opts.connectWith[i]
			$(cn).droppable($.isFunction(opts.drop) ? opts.drop.call($($t),opts) : opts.drop);
		};
		});
	},
	gridResize : function(opts) {
		return this.each(function(){
			var $t = this;
			if(!$t.grid || !$.fn['resizable']) return;
			opts = $.extend(opts || {});
			if(opts.alsoResize ) {
				opts._alsoResize_ = opts.alsoResize;
				delete opts.alsoResize;
			} else {
				opts._alsoResize_ = false;
			}
			if(opts.stop && $.isFunction(opts.stop)) {
				opts._stop_ = opts.stop;
				delete opts.stop;
			} else {
				opts._stop_ = false;
			}
			opts.stop = function (ev, ui) {
				$($t).jqGrid('setGridParam',{height:$("#gview_"+$t.p.id+" .ui-jqgrid-bdiv").height()});
				$($t).jqGrid('setGridWidth',ui.size.width,opts.shrinkToFit);
				if(opts._stop_) opts._stop_.call($t,ev,ui);
			};
			if(opts._alsoResize_) {
				var optstest = "{'\#gview_"+$t.p.id+" .ui-jqgrid-bdiv\':true,'" +opts._alsoResize_+"':true}";
				opts.alsoResize = eval('('+optstest+')'); // the only way that I found to do this
			} else {
				opts.alsoResize = $(".ui-jqgrid-bdiv","#gview_"+$t.p.id);
			}
			delete opts._alsoResize_;
			$("#gbox_"+$t.p.id).resizable(opts);
		});
	}
});
})(jQuery);
;(function($){
/**
 * jqGrid extension for manipulating Grid Data
 * Tony Tomov tony@trirand.com
 * http://trirand.com/blog/ 
 * Dual licensed under the MIT and GPL licenses:
 * http://www.opensource.org/licenses/mit-license.php
 * http://www.gnu.org/licenses/gpl.html
**/ 
$.jgrid.extend({
//Editing
	editRow : function(rowid,keys,oneditfunc,succesfunc, url, extraparam, aftersavefunc,errorfunc, afterrestorefunc,type,beforeSave) {
		return this.each(function(){
			var $t = this, nm, tmp, editable, cnt=0, focus=null, svr={}, ind,cm,flag = true;
			if (!$t.grid ) { return; }
				var editRows = $t.p.savedRow;//当有行处于添加状态时，不允许编辑一行
			for(var v = 0 ;v< editRows.length;v++)
				{
				   var editRow = editRows[v].id;
				   z = $($t).jqGrid("getInd", editRow, true);
				   if($(z).attr("saveType")=="add" && type == "edit")
				   {
				       alert("正在添加行，请先处理！");
				       return;
				   }
				   
				}
			if(type == "edit" && editRows.length>0 )
			{
			   value =  confirm("是否保存正在编辑的行");//当有一行在编辑时，返回，并提示保存处于编辑的那行
			   
			   var lastid = editRows[0].id;
			  if(value)
			  {
			   if ($.isFunction(beforeSave)) {
					  flag = beforeSave.call();
					 					}
				if(flag)
				{
			      $($t).jqGrid("saveRow",lastid,succesfunc, url, extraparam, aftersavefunc,errorfunc, afterrestorefunc,type,beforeSave);//确定，保存
			     }else{
			     if(lastid != rowid)
			     {
			     return;
			     }
			     } 
			     
			  }else{
			  //a(n).jqGrid("restoreRow", lastid, g,type);//取消，还原
			  return;
			  }
			}
			ind = $($t).jqGrid("getInd",rowid,true);
			if( ind == false ) {return;}
			editable = $(ind).attr("editable") || "0";
			if (editable == "0" && !$(ind).hasClass("not-editable-row")) {
				cm = $t.p.colModel;
				$('td',ind).each( function(i) {
					nm = cm[i].name;
					var treeg = $t.p.treeGrid===true && nm == $t.p.ExpandColumn;
					if(treeg) tmp = $("span:first",this).html();
					else {
					tmp = $(this).attr("value");//将取html值改成去value属性对应的值
					/*
						try {
							tmp =  $.unformat(this,{colModel:cm[i]},i);
						} catch (_) {
							tmp = $(this).html();
						}
						*/
					}
					if ( nm != 'cb' && nm != 'subgrid' && nm != 'rn' && nm != "rb") {//添加单选框判断
						svr[nm]=tmp;
						if(cm[i].editable===true) {
							if(focus===null) { focus = i; }
							if (treeg) $("span:first",this).html("");
							else $(this).html("");
							var opt = $.extend({},cm[i].editoptions || {},{id:rowid+"_"+nm,name:nm});
							if(!cm[i].edittype) { cm[i].edittype = "text"; }
							var elc = createEl(cm[i].edittype,opt,tmp,true,$.extend({},$.jgrid.ajaxOptions,$t.p.ajaxSelectOptions || {}));
							$(elc).addClass("editable");
							if(treeg) $("span:first",this).append(elc);
							else $(this).append(elc);
							//Again IE
							if(cm[i].edittype == "select" && cm[i].editoptions.multiple===true && $.browser.msie) {
								$(elc).width($(elc).width());
							}
							cnt++;
						}
					}
				});
				if(cnt > 0) {
					svr['id'] = rowid; $t.p.savedRow.push(svr);
					$(ind).attr("editable","1");
					$("td:eq("+focus+") input",ind).focus();
					if(keys===true) {
						$(ind).parent().bind("keydown",function(e) {//将按enter键保存一行，改成保存多行
							//if (e.keyCode === 27) {$($t).jqGrid("restoreRow",rowid, afterrestorefunc);}
							if (e.keyCode === 13) {
							
								var rowids = [];
								var k =$t.p.savedRow;
							  	for(var z = 0;z<k.length;z++)
							  	{
							     	rowids.push(k[z].id);
							     	
							  	}
							  	if ($.isFunction(beforeSave)) {
					                     flag = beforeSave.call();
					 					}
				                       if(flag)
				                         {
			                                     $($t).jqGrid("saveRows",rowids,succesfunc, url, extraparam, aftersavefunc,errorfunc, afterrestorefunc,type);//确定，保存
			                             }else
			                             {
			                                return;
			                             } 
								
								return false;
							
								//$($t).jqGrid("saveRow",rowid,succesfunc, url, extraparam, aftersavefunc,errorfunc, afterrestorefunc );
								//return false;
							}
							e.stopPropagation();
						});
						$(ind).bind("keydown", function(t){
							if (t.keyCode === 27) {
							
								$(n).jqGrid("restoreRow", rowid, afterrestorefunc,type);
							}
						});
					}
					if( $.isFunction(oneditfunc)) { oneditfunc(rowid); }
				}
			}
		});
	},
	saveRow : function(rowid, succesfunc, url, extraparam, aftersavefunc,errorfunc, afterrestorefunc,type,beforeSave) {
		return this.each(function(){
		var $t = this, nm, tmp={},tmp1, tmp2={}, editable, fr, cv, ind,flag = true;
		if (!$t.grid ) { return; }
		
		    if ($.isFunction(beforeSave)) {//添加表单验证
					                     flag = beforeSave.call();
					               
					 					}
				                       if(!flag)
				                         {			                            
			                                return;
			                             }
	
		url = url ? url : $t.p.editurl;
		if (url) {
			
			tmp1 = $($t).jqGrid("getSaveData",$t,extraparam,rowid);
				if(tmp1)
				{
				tmp = tmp1[0];
				tmp2 = tmp1[1];
				}
			if(!$t.grid.hDiv.loading) {
				$t.grid.hDiv.loading = true;
				$("div.loading",$t.grid.hDiv).fadeIn("fast");
				if (url == 'clientArray') {
					tmp = $.extend({},tmp, tmp2);
					var resp = $($t).jqGrid("setRowData",rowid,tmp);
					$(ind).attr("editable","0");
					for( var k=0;k<$t.p.savedRow.length;k++) {
						if( $t.p.savedRow[k].id == rowid) {fr = k; break;}
					}
					if(fr >= 0) { $t.p.savedRow.splice(fr,1); }
					if( $.isFunction(aftersavefunc) ) { aftersavefunc(rowid,resp); }
				} else {
					$.ajax($.extend({
						url:url,
						data: $.isFunction($t.p.serializeRowData) ? $t.p.serializeRowData(tmp) : tmp,
						type: "POST",
						complete: function(res,stat){
							if (stat === "success"){
								var ret;
								if( $.isFunction(succesfunc)) { ret = succesfunc(res);}
								else ret = true;
								if (ret===true) {
									tmp = $.extend({},tmp, tmp2);
									$($t).jqGrid("setRowData",rowid,tmp);
									$(ind).attr("editable","0");
									$(ind).attr("saveType","edit");
									for( var k=0;k<$t.p.savedRow.length;k++) {
										if( $t.p.savedRow[k].id == rowid) {fr = k; break;}
									};
									if(fr >= 0) { $t.p.savedRow.splice(fr,1); }
									if( $.isFunction(aftersavefunc) ) { aftersavefunc(rowid,res); }
								} else { $($t).jqGrid("restoreRow",rowid, afterrestorefunc,type); }
									var alertId = [];
								alertId.push(h);
								$($t).jqGrid("alertData",alertId);//修改计算列与合计列的值
							}
						},
						error:function(res,stat){
							if($.isFunction(errorfunc) ) {
								errorfunc(rowid, res, stat);
							} else {
								alert("Error Row: "+rowid+" Result: " +res.status+":"+res.statusText+" Status: "+stat);
							}
						}
					}, $.jgrid.ajaxOptions, $t.p.ajaxRowOptions || {}));
				}
				$t.grid.hDiv.loading = false;
				$("div.loading",$t.grid.hDiv).fadeOut("fast");
				$(ind).unbind("keydown");
			}
		}
		});
	},saveRows:function(h, g, e, f, d, c, b,type,beforeSave){//保存多行数据
	
	return this.each(function () {
		
			var o = this,r,p = {},l = {},pl,pAarry = [],u;
			if (!o.grid) {
				return;
			}		
			e = e ? e : o.p.editurl;
			if (e) {
			for(var v in h )
			{
				pl = $(o).jqGrid("getSaveData",o,f,h[v]);
				
				if(pl)
				{
				p = pl[0];
				pAarry.push(p);
				}
				}
				p={};
				for(var z = 0 ;z<o.p.colModel.length;z++)
				{
				   t = o.p.colModel[z];
				   u = t.name;
				   if (u != "cb" && u != "subgrid" && t.editable === true && u != "rn" && u != "rb"){
				   
				   for(var y=0;y<pAarry.length;y++)
				   {
				   var pggg = pAarry[y];
				     var pfff = pggg[u];
				     if(y == pAarry.length-1)
				     {	
				     if(y == 0)
				     	{p[u] = pfff}
				     	else{	
				     p[u]=p[u]+pfff;
				      }			     		      
				      				      
				     }else{
				     if(y == 0)
				     	{p[u] = pfff+","}
				     	else{	
				      p[u]=p[u]+pfff+",";
				      }
                     }				   
				   }
				   }
				}
				if (!o.grid.hDiv.loading) {
					o.grid.hDiv.loading = true;
					$("div.loading", o.grid.hDiv).fadeIn("fast");
					if (e == "clientArray") {
						p = $.extend({}, p, l);
						var n = $(o).jqGrid("setRowData", h, p);
						$(i).attr("editable", "0");
						for (var m = 0; m < o.p.savedRow.length; m++) {
							if (o.p.savedRow[m].id == h) {
								r = m;
								break;
							}
						}
						if (r >= 0) {
							o.p.savedRow.splice(r, 1);
						}
						if ($.isFunction(d)) {
							d(h, n);
						}
					} else {
						$.ajax($.extend({url:e, data:$.isFunction(o.p.serializeRowData) ? o.p.serializeRowData(p) : p, type:"POST", complete:function (x, y) {
							if (y === "success") {
							
								var w;
								if ($.isFunction(g)) {
									w = g(x);
								} else {
									w = true;
								}
									if ($.isFunction(d)) {
										d(h, x);
									
									}
								for(var z = 0;z<h.length;z++){
								if (w === true) {
									p = $.extend({}, p, l);
									i = $(o).jqGrid("getInd", h[z], true);
			                        if (i == false) {
				                            continue;
			                            }
									$(o).jqGrid("setRowData",h[z], pAarry[z]);
									$(i).attr("editable", "0");
									$(i).attr("saveType","edit");
									for (var v = 0; v < o.p.savedRow.length; v++) {
										if (o.p.savedRow[v].id == h[z]) {
											r = v;
											break;
										}
									}
									if (r >= 0) {
										o.p.savedRow.splice(r, 1);
									}
								
								} else {
									$(o).jqGrid("restoreRow", h[z], b,type);
								}
							}
							$(o).jqGrid("alertData", h);//修改计算列与合计列的值
							}
						}, error:function (k, v) {
						for(var z = 0;z<h.length;z++){
							if ($.isFunction(c)) {
								c(h, k, v);
							} else {
								alert("Error Row: " + h + " Result: " + k.status + ":" + k.statusText + " Status: " + v);
													
								$(o).jqGrid("restoreRow", h[z], b,type);//编辑或保存出错时数据还原								
							}
							}
						}}, $.jgrid.ajaxOptions, o.p.ajaxRowOptions || {}));
					}
					o.grid.hDiv.loading = false;
					$("div.loading", o.grid.hDiv).fadeOut("fast");
					for(var z in h)
					{
					i = $(o).jqGrid("getInd", h[z], true);
					$(i).parent().unbind("keydown");
					break;
					}
	//				a(i).unbind("keydown");
				}
			}
		});
	
	},getSaveData : function($t,extraparam,rowid)
	{
	var nm, tmp={},tmp1 =[], tmp2={}, fr, cv,editable, ind,flag = true;
	   
	   ind = $($t).jqGrid("getInd",rowid,true);
	   	editable = $(ind).attr("editable");
	   	
		if(ind == false) {return;}
		if(editable==='1')
		{
		var cm;
			$("td",ind).each(function(i) {
				cm = $t.p.colModel[i];
				nm = cm.name;
				if ( nm != 'cb' && nm != 'subgrid' && cm.editable===true && nm != 'rn' && nm!=='rb') {//添加单选框判断
					switch (cm.edittype) {
						case "checkbox":
							var cbv = ["Yes","No"];
							if(cm.editoptions ) {
								cbv = cm.editoptions.value.split(":");
							}
							tmp[nm]=  $("input",this).attr("checked") ? cbv[0] : cbv[1]; 
							break;
						case 'text':
						case 'password':
						case 'textarea':
						case "button" :
							tmp[nm]= !$t.p.autoencode ? $("input, textarea",this).val() : $.jgrid.htmlEncode($("input, textarea",this).val());
							break;
						case 'select':
							if(!cm.editoptions.multiple) {
								tmp[nm] = $("select>option:selected",this).val();
								tmp2[nm] = $("select>option:selected", this).text();
							} else {
								var sel = $("select",this), selectedText = [];
								tmp[nm] = $(sel).val();
								if(tmp[nm]) tmp[nm]= tmp[nm].join(","); else tmp[nm] ="";
								$("select > option:selected",this).each(
									function(i,selected){
										selectedText[i] = $(selected).text();
									}
								);
								tmp2[nm] = selectedText.join(",");
							}
							if(cm.formatter && cm.formatter == 'select') tmp2={};
							break;
						case 'custom' :
							try {
								if(cm.editoptions && $.isFunction(cm.editoptions.custom_value)) {
									tmp[nm] = cm.editoptions.custom_value($(".customelement",this),'get');
									if (tmp[nm] === undefined) throw "e2";
								} else throw "e1";
							} catch (e) {
								if (e=="e1") info_dialog(jQuery.jgrid.errors.errcap,"function 'custom_value' "+$.jgrid.edit.msg.nodefined,jQuery.jgrid.edit.bClose);
								if (e=="e2") info_dialog(jQuery.jgrid.errors.errcap,"function 'custom_value' "+$.jgrid.edit.msg.novalue,jQuery.jgrid.edit.bClose);
								else info_dialog(jQuery.jgrid.errors.errcap,e.message,jQuery.jgrid.edit.bClose);
							}
							break;
					}
					cv = checkValues(tmp[nm],i,$t);
					if(cv[0] === false) {
						cv[1] = tmp[nm] + " " + cv[1];
						return false;
					}
				}
			});
			if (cv[0] === false){
				try {
					info_dialog($.jgrid.errors.errcap,cv[1],$.jgrid.edit.bClose);
				} catch (e) {
					alert(cv[1]);
				}
				return;
			}
			if(tmp) { tmp["id"] = rowid; if(extraparam) { tmp = $.extend({},tmp,extraparam);} }
			tmp1.push(tmp);
			tmp1.push(tmp2);
			return tmp1;
			}
	},
	restoreRow : function(rowid, afterrestorefunc,type) {
		return this.each(function(){
			var $t= this, fr, ind;
			if (!$t.grid ) { return; }
			ind = $($t).jqGrid("getInd",rowid,true);
			if(ind == false) {return;}
			for( var k=0;k<$t.p.savedRow.length;k++) {
				if( $t.p.savedRow[k].id == rowid) {fr = k; break;}
			}
			if(fr >= 0) {
				if($.isFunction($.fn['datepicker'])) {
					try {
						$("input.hasDatepicker","#"+ind.id).datepicker('hide');
					} catch (e) {}
				}
				$($t).jqGrid("setRowData",rowid,$t.p.savedRow[fr]);
				$(ind).attr("editable","0").unbind("keydown");
				type = $(ind).attr("saveType");
			
				if(type == "add")//当type为add时将新增的行删除
				{
				   $("#"+rowid).remove();
				}
				$t.p.savedRow.splice(fr,1);
			}
			if ($.isFunction(afterrestorefunc))
			{
				afterrestorefunc(rowid);
			}
		});
	},
	 addRow:function(rows,keys,oneditfunc,succesfunc, url, extraparam, aftersavefunc,errorfunc, afterrestorefunc,beforeSave)
	{
	     return this.each(function () {
          var n = this; var addData = {};
          
	      if (!n.grid) {
				 return;
			  }
		 for(var colIndex = 0 ;colIndex < n.p.colModel.length;colIndex++)
		 {
		 var comdel = n.p.colModel[colIndex];
		  var cm = comdel.name;
		 if(cm != "cb" && cm != "subgrid" && cm != "rn" && cm !== "rb")
		 {
		    addData[cm] = "";
		    }
		 }
		 
		 for(var idIndex = 0;idIndex < rows ;idIndex++)
		 {
            var gid = $(n).attr("id")+"_"+$("#"+$(n).attr("id")+" > tbody:first > tr").length + 1;
            
            var flag = $(n).jqGrid("addRowData",gid,addData);
            
            if (flag) {
			$(n).jqGrid("editRow",gid, keys,oneditfunc,succesfunc,null,extraparam,aftersavefunc,errorfunc,afterrestorefunc,'add',beforeSave);
	
		} else {
			alert("请先处理正在编辑的行");
			return;
		}
        }
         
      });
	    
	},alertData:function(h){//修改计算列、合计列的值
	    return this.each(function () {
          var n = this,result,nm;
          var patt1=new RegExp("#\\w+#","ig");
	      if (!n.grid) {
				 return;
			  }
        
           for(var hIndex = 0;hIndex< h.length;hIndex++)
              {
   
                       var k = $(n).jqGrid("getInd", h[hIndex], true);
	                    if (k == false) {
				           return;
			             }
   
                       $(n.p.colModel).each(function (i) {
					         nm = this.name;	
							if(typeof this.formula !== "undefined")
							{
							   var str = this.formula;
			                   var str2 = this.formula;
			                   result = [];
                               while (result != null) 
                               {
                                    result = patt1.exec(str);
                                    if(result != null)
                                     {
                                              var s = result[0].split("#");
                                              var val = s[1];
                                              var g;
                                              for(var colIndex = 0;colIndex < n.p.colModel.length;colIndex++)
                                              {
                                                  if(n.p.colModel[colIndex].name == val)
                                                  {
                                                       g =  colIndex;
                                                       break;
                                                  }
                                              }
                                              var tds = $(k).children();
                                              var value = tds.eq(g).attr("value");
                                              reg = new RegExp(result[0],"g");
                                     }
                                       str2 = str2.replace(reg,value) ;


                                }
                                var t = eval(str2);
                                var data = {};
                                data[nm] = t;
								$(n).jqGrid('setRowData',h[hIndex],data);
                         
							}
							if(typeof n.p.userData[nm] != "undefined")
							{
							    var trs = $("tr",n);
							    var udata = 0;
							    var usernm = Number(n.p.userData[nm]);
							    if(!isNaN(usernm))
							    {
							    for(var trIndex = 0;trIndex<trs.length;trIndex++)
							    {
							    var value = Number($("td:eq(" + i + ")", trs[trIndex]).attr("value"));							    
							    if(!isNaN(value))
							    {
							        udata = udata + value;
							        
							    }
							    }							 
							    n.p.userData[nm] = udata;
							    }
							        if (n.p.userDataOnFooter) {
					                   $(n).jqGrid("footerData", "set", n.p.userData, true);
				                       }
							}
							
							
				});
           }
      });
}
//end inline edit
});
})(jQuery);
;(function($){
/**
 * jqGrid extension for form editing Grid Data
 * Tony Tomov tony@trirand.com
 * http://trirand.com/blog/ 
 * Dual licensed under the MIT and GPL licenses:
 * http://www.opensource.org/licenses/mit-license.php
 * http://www.gnu.org/licenses/gpl.html
**/ 
var rp_ge = null;
$.jgrid.extend({
	searchGrid : function (p) {
		p = $.extend({
			recreateFilter: false,
			drag: true,
			sField:'searchField',
			sValue:'searchString',
			sOper: 'searchOper',
			sFilter: 'filters',
			beforeShowSearch: null,
			afterShowSearch : null,
			onInitializeSearch: null,
			closeAfterSearch : false,
			closeOnEscape : false,
			multipleSearch : false,
			// translation
			// if you want to change or remove the order change it in sopt
			// ['bw','eq','ne','lt','le','gt','ge','ew','cn'] 
			sopt: null,
			onClose : null
			// these are common options
		}, $.jgrid.search, p || {});
		return this.each(function() {
			var $t = this;
			if(!$t.grid) {return;}
			if($.fn.searchFilter) {
				var fid = "fbox_"+$t.p.id;
				if(p.recreateFilter===true) {$("#"+fid).remove();}
				if( $("#"+fid).html() != null ) {
					if ( $.isFunction(p.beforeShowSearch) ) { p.beforeShowSearch($("#"+fid)); };
					showFilter();
					if( $.isFunction(p.afterShowSearch) ) { p.afterShowSearch($("#"+fid)); }
				} else {
					var fields = [],
					colNames = $("#"+$t.p.id).jqGrid("getGridParam","colNames"),
					colModel = $("#"+$t.p.id).jqGrid("getGridParam","colModel"),
					stempl = ['eq','ne','lt','le','gt','ge','bw','bn','in','ni','ew','en','cn','nc'],
					j,pos,k,oprtr;
					oprtr = jQuery.fn.searchFilter.defaults.operators;
					if (p.sopt !=null) {
						oprtr = [];
						k=0;
						for(j=0;j<p.sopt.length;j++) {
							if( (pos= $.inArray(p.sopt[j],stempl)) != -1 ){
								oprtr[k] = {op:p.sopt[j],text: p.odata[pos]};
								k++;
							}
						}
					}
					var searchable;
				    $.each(colModel, function(i, v) {
				        searchable = (typeof v.search === 'undefined') ?  true: v.search ,
				        hidden = (v.hidden === true),
						soptions = $.extend({}, {text: colNames[i], itemval: v.index || v.name}, this.searchoptions),
						ignoreHiding = (soptions.searchhidden === true);
						if(typeof soptions.sopt == 'undefined') soptions.sopt = p.sopt ||  stempl;
						k=0;
						soptions.ops =[];
						if(soptions.sopt.length>0) {
							for(j=0;j<soptions.sopt.length;j++) {
								if( (pos= $.inArray(soptions.sopt[j],stempl)) != -1 ){
									soptions.ops[k] = {op:soptions.sopt[j],text: p.odata[pos]};
									k++;
								}
							}
						}
						if(typeof(this.stype) === 'undefined') this.stype='text';
						if(this.stype == 'select') {
							if ( soptions.dataUrl != null) {}
							else {
								var eov;
								if(soptions.value)
									eov = soptions.value;
								else if(this.editoptions)
									eov = this.editoptions.value;
								if(eov) {
									soptions.dataValues =[];
									if(typeof(eov) === 'string') {
										var so = eov.split(";"),sv;
										for(j=0;j<so.length;j++) {
											sv = so[j].split(":");
											soptions.dataValues[j] ={value:sv[0],text:sv[1]};
										}
									} else if (typeof(eov) === 'object') {
										j=0;
										for (var key in eov) {
											soptions.dataValues[j] ={value:key,text:eov[key]};
											j++;
										}
									}
								}
							}
						}
				        if ((ignoreHiding && searchable) || (searchable && !hidden)) {
							fields.push(soptions);
						}
					});
					if(fields.length>0){
						$("<div id='"+fid+"' role='dialog' tabindex='-1'></div>").insertBefore("#gview_"+$t.p.id);
						$("#"+fid).searchFilter(fields, { groupOps: p.groupOps, operators: oprtr, onClose:hideFilter, resetText: p.Reset, searchText: p.Find, windowTitle: p.caption,  rulesText:p.rulesText, matchText:p.matchText, onSearch: searchFilters, onReset: resetFilters,stringResult:p.multipleSearch, ajaxSelectOptions: $.extend({},$.jgrid.ajaxOptions,$t.p.ajaxSelectOptions ||{}) });
						$(".ui-widget-overlay","#"+fid).remove();
						if($t.p.direction=="rtl") $(".ui-closer","#"+fid).css("float","left");
						if (p.drag===true) {
							$("#"+fid+" table thead tr:first td:first").css('cursor','move');
							if(jQuery.fn.jqDrag) {
								$("#"+fid).jqDrag($("#"+fid+" table thead tr:first td:first"));
							} else {
								try {
									$("#"+fid).draggable({handle: $("#"+fid+" table thead tr:first td:first")});
								} catch (e) {}
							}
						}
						if(p.multipleSearch === false) {
							$(".ui-del, .ui-add, .ui-del, .ui-add-last, .matchText, .rulesText", "#"+fid).hide();
							$("select[name='groupOp']","#"+fid).hide();
						}
						if ( $.isFunction(p.onInitializeSearch) ) { p.onInitializeSearch( $("#"+fid) ); };
						if ( $.isFunction(p.beforeShowSearch) ) { p.beforeShowSearch($("#"+fid)); };
						showFilter();
						if( $.isFunction(p.afterShowSearch) ) { p.afterShowSearch($("#"+fid)); }
						if(p.closeOnEscape===true){
							$("#"+fid).keydown( function( e ) {
								if( e.which == 27 ) {
									hideFilter($("#"+fid));
								}
							});
						}
					}
				}
			}
			function searchFilters(filters) {
				var hasFilters = (filters !== undefined),
				grid = $("#"+$t.p.id), sdata={};
				if(p.multipleSearch===false) {
					sdata[p.sField] = filters.rules[0].field;
					sdata[p.sValue] = filters.rules[0].data;
					sdata[p.sOper] = filters.rules[0].op;
				} else {
					sdata[p.sFilter] = filters;
				}
				grid[0].p.search = hasFilters;
				$.extend(grid[0].p.postData,sdata);
				grid.trigger("reloadGrid",[{page:1}]);
				if(p.closeAfterSearch) hideFilter($("#"+fid));
			}
			function resetFilters(filters) {
				var hasFilters = (filters !== undefined),
				grid = $("#"+$t.p.id), sdata=[];
				grid[0].p.search = hasFilters;
				if(p.multipleSearch===false) {
					sdata[p.sField] = sdata[p.sValue] = sdata[p.sOper] = "";
				} else {
					sdata[p.sFilter] = "";
				}
				$.extend(grid[0].p.postData,sdata);
				grid.trigger("reloadGrid",[{page:1}]);
			}
			function hideFilter(selector) {
				if(p.onClose){
					var fclm = p.onClose(selector);
					if(typeof fclm == 'boolean' && !fclm) return;
				}
				selector.hide();
				$(".jqgrid-overlay:first","#gbox_"+$t.p.id).hide();
			}
			function showFilter(){
				var fl = $(".ui-searchFilter").length;
				if(fl > 1) {
					var zI = $("#"+fid).css("zIndex");
					$("#"+fid).css({zIndex:parseInt(zI)+fl});
				}
				$("#"+fid).show();
				$(".jqgrid-overlay:first","#gbox_"+$t.p.id).show();
				try{$(':input:visible',"#"+fid)[0].focus();}catch(_){}
			}
		});
	},
	editGridRow : function(rowid, p){
		p = $.extend({
			top : 0,
			left: 0,
			width: 300,
			height: 'auto',
			dataheight: 'auto',
			modal: false,
			drag: true,
			resize: true,
			url: null,
			mtype : "POST",
			clearAfterAdd :true,
			closeAfterEdit : false,
			reloadAfterSubmit : true,
			onInitializeForm: null,
			beforeInitData: null,
			beforeShowForm: null,
			afterShowForm: null,
			beforeSubmit: null,
			afterSubmit: null,
			onclickSubmit: null,
			afterComplete: null,
			onclickPgButtons : null,
			afterclickPgButtons: null,
			editData : {},
			recreateForm : false,
			jqModal : true,
			closeOnEscape : false,
			addedrow : "first",
			topinfo : '',
			bottominfo: '',
			saveicon : [],
			closeicon : [],
			savekey: [false,13],
			navkeys: [false,38,40],
			checkOnSubmit : false,
			checkOnUpdate : false,
			_savedData : {},
			onClose : null,
			ajaxEditOptions : {},
			serializeEditData : null
		}, $.jgrid.edit, p || {});
		rp_ge = p;
		return this.each(function(){
			var $t = this;
			if (!$t.grid || !rowid) { return; }
			var gID = $t.p.id,
			frmgr = "FrmGrid_"+gID,frmtb = "TblGrid_"+gID,
			IDs = {themodal:'editmod'+gID,modalhead:'edithd'+gID,modalcontent:'editcnt'+gID, scrollelm : frmgr},
			onBeforeShow = $.isFunction(rp_ge.beforeShowForm) ? rp_ge.beforeShowForm : false,
			onAfterShow = $.isFunction(rp_ge.afterShowForm) ? rp_ge.afterShowForm : false,
			onBeforeInit = $.isFunction(rp_ge.beforeInitData) ? rp_ge.beforeInitData : false,
			onInitializeForm = $.isFunction(rp_ge.onInitializeForm) ? rp_ge.onInitializeForm : false,
			copydata = null,
			maxCols = 1, maxRows=0,	gurl, postdata, ret, extpost, newData, diff;
			if (rowid=="new") {
				rowid = "_empty";
				p.caption=p.addCaption;
			} else {
				p.caption=p.editCaption;
			};
			if(p.recreateForm===true && $("#"+IDs.themodal).html() != null) {
				$("#"+IDs.themodal).remove();
			}
			var closeovrl = true;
			if(p.checkOnUpdate && p.jqModal && !p.modal) {
				closeovrl = false;
			}
			if ( $("#"+IDs.themodal).html() != null ) {
				$(".ui-jqdialog-title","#"+IDs.modalhead).html(p.caption);
				$("#FormError","#"+frmtb).hide();
				if(rp_ge.topinfo) {
					$(".topinfo","#"+frmtb+"_2").html(rp_ge.topinfo);
					$(".tinfo","#"+frmtb+"_2").show();
				}
					else $(".tinfo","#"+frmtb+"_2").hide();
				if(rp_ge.bottominfo) {
					$(".bottominfo","#"+frmtb+"_2").html(rp_ge.bottominfo);
					$(".binfo","#"+frmtb+"_2").show();
				}
				else $(".binfo","#"+frmtb+"_2").hide();

				if(onBeforeInit) { onBeforeInit($("#"+frmgr)); }
				// filldata
				fillData(rowid,$t,frmgr);
				///
				if(rowid=="_empty") { $("#pData, #nData","#"+frmtb+"_2").hide(); } else { $("#pData, #nData","#"+frmtb+"_2").show(); }
				if(p.processing===true) {
					p.processing=false;
					$("#sData", "#"+frmtb+"_2").removeClass('ui-state-active');
				}
				if($("#"+frmgr).data("disabled")===true) {
					$(".confirm","#"+IDs.themodal).hide();
					$("#"+frmgr).data("disabled",false);
				}
				if(onBeforeShow) { onBeforeShow($("#"+frmgr)); }
				$("#"+IDs.themodal).data("onClose",rp_ge.onClose);
				viewModal("#"+IDs.themodal,{gbox:"#gbox_"+gID,jqm:p.jqModal, jqM: false, closeoverlay: closeovrl, modal:p.modal});
				if(!closeovrl) {
					$(".jqmOverlay").click(function(){
						if(!checkUpdates()) return false;
						hideModal("#"+IDs.themodal,{gb:"#gbox_"+gID,jqm:p.jqModal, onClose: rp_ge.onClose});
						return false;
					});
				}
				if(onAfterShow) { onAfterShow($("#"+frmgr)); }
			} else {
				$($t.p.colModel).each( function(i) {
					var fmto = this.formoptions;
					maxCols = Math.max(maxCols, fmto ? fmto.colpos || 0 : 0 );
					maxRows = Math.max(maxRows, fmto ? fmto.rowpos || 0 : 0 );
				});
				var dh = isNaN(p.dataheight) ? p.dataheight : p.dataheight+"px";
				var flr, frm = $("<form name='FormPost' id='"+frmgr+"' class='FormGrid' style='width:100%;overflow:auto;position:relative;height:"+dh+";'></form>").data("disabled",false),
				tbl =$("<table id='"+frmtb+"' class='EditTable' cellspacing='0' cellpading='0' border='0'><tbody></tbody></table>");
				$(frm).append(tbl);
				flr = $("<tr id='FormError' style='display:none'><td class='ui-state-error' colspan='"+(maxCols*2)+"'></td></tr>");
				flr[0].rp = 0;
				$(tbl).append(flr);
				//topinfo
				flr = $("<tr style='display:none' clas=='tinfo'><td class='topinfo' colspan='"+(maxCols*2)+"'>"+rp_ge.topinfo+"</td></tr>");
				flr[0].rp = 0;
				$(tbl).append(flr);
				// set the id.
				// use carefull only to change here colproperties.
				if(onBeforeInit) { onBeforeInit($("#"+frmgr)); }
				// create data
				var rtlb = $t.p.direction == "rtl" ? true :false,
				bp = rtlb ? "nData" : "pData",
				bn = rtlb ? "pData" : "nData",
				valref = createData(rowid,$t,tbl,maxCols),
				// buttons at footer
				bP = "<a href='javascript:void(0)' id='"+bp+"' class='fm-button ui-state-default ui-corner-left'><span class='ui-icon ui-icon-triangle-1-w'></span></div>",
				bN = "<a href='javascript:void(0)' id='"+bn+"' class='fm-button ui-state-default ui-corner-right'><span class='ui-icon ui-icon-triangle-1-e'></span></div>",
				bS  ="<a href='javascript:void(0)' id='sData' class='fm-button ui-state-default ui-corner-all'>"+p.bSubmit+"</a>",
				bC  ="<a href='javascript:void(0)' id='cData' class='fm-button ui-state-default ui-corner-all'>"+p.bCancel+"</a>";
				var bt = "<table border='0' class='EditTable' id='"+frmtb+"_2'><tbody><tr id='Act_Buttons'><td class='navButton ui-widget-content'>"+(rtlb ? bN+bP : bP+bN)+"</td><td class='EditButton ui-widget-content'>"+bS+bC+"</td></tr>";
				bt += "<tr style='display:none' class='binfo'><td class='bottominfo' colspan='2'>"+rp_ge.bottominfo+"</td></tr>";
				bt += "</tbody></table>";
				if(maxRows >  0) {
					var sd=[];
					$.each($(tbl)[0].rows,function(i,r){
						sd[i] = r;
					});
					sd.sort(function(a,b){
						if(a.rp > b.rp) {return 1;}
						if(a.rp < b.rp) {return -1;}
						return 0;
					});
					$.each(sd, function(index, row) {
						$('tbody',tbl).append(row);
					});
				}
				p.gbox = "#gbox_"+gID;
				var cle = false;
				if(p.closeOnEscape===true){
					p.closeOnEscape = false;
					cle = true;
				}
				var tms = $("<span></span>").append(frm).append(bt);
				createModal(IDs,tms,p,"#gview_"+$t.p.id,$("#gview_"+$t.p.id)[0]);
				if(rtlb) {
					$("#pData, #nData","#"+frmtb+"_2").css("float","right");
					$(".EditButton","#"+frmtb+"_2").css("text-align","left");
				}
				if(rp_ge.topinfo) $(".tinfo","#"+frmtb+"_2").show();
				if(rp_ge.bottominfo) $(".binfo","#"+frmtb+"_2").show();
				tms = null; bt=null;
				$("#"+IDs.themodal).keydown( function( e ) {
					var wkey = e.target;
					if ($("#"+frmgr).data("disabled")===true ) return false; //??
					if(rp_ge.savekey[0] === true && e.which == rp_ge.savekey[1]) { // save
						if(wkey.tagName != "TEXTAREA") {
							$("#sData", "#"+frmtb+"_2").trigger("click");
							return false;
						}
					}
					if(e.which === 27) {
						if(!checkUpdates()) return false;
						if(cle)	hideModal(this,{gb:p.gbox,jqm:p.jqModal, onClose: rp_ge.onClose});
						return false;
					}
					if(rp_ge.navkeys[0]===true) {
						if($("#id_g","#"+frmtb).val() == "_empty") return true;
						if(e.which == rp_ge.navkeys[1]){ //up
							$("#pData", "#"+frmtb+"_2").trigger("click");
							return false;
						}
						if(e.which == rp_ge.navkeys[2]){ //down
							$("#nData", "#"+frmtb+"_2").trigger("click");
							return false;
						}
					}
				});
				if(p.checkOnUpdate) {
					$("a.ui-jqdialog-titlebar-close span","#"+IDs.themodal).removeClass("jqmClose");
					$("a.ui-jqdialog-titlebar-close","#"+IDs.themodal).unbind("click")
					.click(function(){
						if(!checkUpdates()) return false;
						hideModal("#"+IDs.themodal,{gb:"#gbox_"+gID,jqm:p.jqModal,onClose: rp_ge.onClose});
						return false;
					});
				}
				p.saveicon = $.extend([true,"left","ui-icon-disk"],p.saveicon);
				p.closeicon = $.extend([true,"left","ui-icon-close"],p.closeicon);
				// beforeinitdata after creation of the form
				if(p.saveicon[0]==true) {
					$("#sData","#"+frmtb+"_2").addClass(p.saveicon[1] == "right" ? 'fm-button-icon-right' : 'fm-button-icon-left')
					.append("<span class='ui-icon "+p.saveicon[2]+"'></span>");
				}
				if(p.closeicon[0]==true) {
					$("#cData","#"+frmtb+"_2").addClass(p.closeicon[1] == "right" ? 'fm-button-icon-right' : 'fm-button-icon-left')
					.append("<span class='ui-icon "+p.closeicon[2]+"'></span>");
				}
				if(rp_ge.checkOnSubmit || rp_ge.checkOnUpdate) {
					bS  ="<a href='javascript:void(0)' id='sNew' class='fm-button ui-state-default ui-corner-all' style='z-index:1002'>"+p.bYes+"</a>";
					bN  ="<a href='javascript:void(0)' id='nNew' class='fm-button ui-state-default ui-corner-all' style='z-index:1002'>"+p.bNo+"</a>";
					bC  ="<a href='javascript:void(0)' id='cNew' class='fm-button ui-state-default ui-corner-all' style='z-index:1002'>"+p.bExit+"</a>";
					var ii, zI = p.zIndex  || 999; zI ++;
					if ($.browser.msie && $.browser.version ==6) {
						ii = '<iframe style="display:block;position:absolute;z-index:-1;filter:Alpha(Opacity=\'0\');" src="javascript:false;"></iframe>';
					} else { ii="";}
					$("<div class='ui-widget-overlay jqgrid-overlay confirm' style='z-index:"+zI+";display:none;'>&#160;"+ii+"</div><div class='confirm ui-widget-content ui-jqconfirm' style='z-index:"+(zI+1)+"'>"+p.saveData+"<br/><br/>"+bS+bN+bC+"</div>").insertAfter("#"+frmgr);
					$("#sNew","#"+IDs.themodal).click(function(){
						postIt();
						$("#"+frmgr).data("disabled",false);
						$(".confirm","#"+IDs.themodal).hide();
						return false;
					});
					$("#nNew","#"+IDs.themodal).click(function(){
						$(".confirm","#"+IDs.themodal).hide();
						$("#"+frmgr).data("disabled",false);
						setTimeout(function(){$(":input","#"+frmgr)[0].focus();},0);
						return false;
					});
					$("#cNew","#"+IDs.themodal).click(function(){
						$(".confirm","#"+IDs.themodal).hide();
						$("#"+frmgr).data("disabled",false);
						hideModal("#"+IDs.themodal,{gb:"#gbox_"+gID,jqm:p.jqModal,onClose: rp_ge.onClose});
						return false;
					});
				}
				// here initform - only once
				if(onInitializeForm) { onInitializeForm($("#"+frmgr)); }
				if(rowid=="_empty") { $("#pData,#nData","#"+frmtb+"_2").hide(); } else { $("#pData,#nData","#"+frmtb+"_2").show(); }
				if(onBeforeShow) { onBeforeShow($("#"+frmgr)); }
				$("#"+IDs.themodal).data("onClose",rp_ge.onClose);
				viewModal("#"+IDs.themodal,{gbox:"#gbox_"+gID,jqm:p.jqModal,closeoverlay:closeovrl,modal:p.modal});
				if(!closeovrl) {
					$(".jqmOverlay").click(function(){
						if(!checkUpdates()) return false;
						hideModal("#"+IDs.themodal,{gb:"#gbox_"+gID,jqm:p.jqModal, onClose: rp_ge.onClose});
						return false;
					});
				}
				if(onAfterShow) { onAfterShow($("#"+frmgr)); }
				$(".fm-button","#"+IDs.themodal).hover(
				   function(){$(this).addClass('ui-state-hover');}, 
				   function(){$(this).removeClass('ui-state-hover');}
				);
				$("#sData", "#"+frmtb+"_2").click(function(e){
					postdata = {}; extpost={};
					$("#FormError","#"+frmtb).hide();
					// all depend on ret array
					//ret[0] - succes
					//ret[1] - msg if not succes
					//ret[2] - the id  that will be set if reload after submit false
					getFormData();
					if(postdata.id == "_empty")	postIt();
					else if(p.checkOnSubmit===true ) {
						newData = $.extend({},postdata,extpost);
						diff = compareData(newData,rp_ge._savedData);
						if(diff) {
							$("#"+frmgr).data("disabled",true);
							$(".confirm","#"+IDs.themodal).show();
						} else {
							postIt();
						}
					} else {
						postIt();
					}
					return false;
				});
				$("#cData", "#"+frmtb+"_2").click(function(e){
					if(!checkUpdates()) return false;
					hideModal("#"+IDs.themodal,{gb:"#gbox_"+gID,jqm:p.jqModal,onClose: rp_ge.onClose});
					return false;
				});
				$("#nData", "#"+frmtb+"_2").click(function(e){
					if(!checkUpdates()) return false;
					$("#FormError","#"+frmtb).hide();
					var npos = getCurrPos();
					npos[0] = parseInt(npos[0]);
					if(npos[0] != -1 && npos[1][npos[0]+1]) {
						if($.isFunction(p.onclickPgButtons)) {
							p.onclickPgButtons('next',$("#"+frmgr),npos[1][npos[0]]);
						}
						fillData(npos[1][npos[0]+1],$t,frmgr);
						$($t).jqGrid("setSelection",npos[1][npos[0]+1]);
						if($.isFunction(p.afterclickPgButtons)) {
							p.afterclickPgButtons('next',$("#"+frmgr),npos[1][npos[0]+1]);
						}
						updateNav(npos[0]+1,npos[1].length-1);
					};
					return false;
				});
				$("#pData", "#"+frmtb+"_2").click(function(e){
					if(!checkUpdates()) return false;
					$("#FormError","#"+frmtb).hide();
					var ppos = getCurrPos();
					if(ppos[0] != -1 && ppos[1][ppos[0]-1]) {
						if($.isFunction(p.onclickPgButtons)) {
							p.onclickPgButtons('prev',$("#"+frmgr),ppos[1][ppos[0]]);
						}
						fillData(ppos[1][ppos[0]-1],$t,frmgr);
						$($t).jqGrid("setSelection",ppos[1][ppos[0]-1]);
						if($.isFunction(p.afterclickPgButtons)) {
							p.afterclickPgButtons('prev',$("#"+frmgr),ppos[1][ppos[0]-1]);
						}
						updateNav(ppos[0]-1,ppos[1].length-1);
					};
					return false;
				});
			}
			var posInit =getCurrPos();
			updateNav(posInit[0],posInit[1].length-1);
			function updateNav(cr,totr,rid){
				if (cr==0) { $("#pData","#"+frmtb+"_2").addClass('ui-state-disabled'); } else { $("#pData","#"+frmtb+"_2").removeClass('ui-state-disabled'); }
				if (cr==totr) { $("#nData","#"+frmtb+"_2").addClass('ui-state-disabled'); } else { $("#nData","#"+frmtb+"_2").removeClass('ui-state-disabled'); }
			}
			function getCurrPos() {
				var rowsInGrid = $($t).jqGrid("getDataIDs"),
				selrow = $("#id_g","#"+frmtb).val(),
				pos = $.inArray(selrow,rowsInGrid);
				return [pos,rowsInGrid];
			}
			function checkUpdates () {
				var stat = true;
				$("#FormError","#"+frmtb).hide();
				if(rp_ge.checkOnUpdate) {
					postdata = {}; extpost={};
					getFormData();
					newData = $.extend({},postdata,extpost);
					diff = compareData(newData,rp_ge._savedData);
					if(diff) {
						$("#"+frmgr).data("disabled",true);
						$(".confirm","#"+IDs.themodal).show();
						stat = false;
					}
				}
				return stat;
			}
			function getFormData(){
				$(".FormElement", "#"+frmtb).each(function(i) {
					var celm = $(".customelement", this);
					if (celm.length) {
						var  elem = celm[0], nm = elem.name;
						$.each($t.p.colModel, function(i,n){
							if(this.name == nm && this.editoptions && $.isFunction(this.editoptions.custom_value)) {
								try {
									postdata[nm] = this.editoptions.custom_value($("#"+nm,"#"+frmtb),'get');
									if (postdata[nm] === undefined) throw "e1";
								} catch (e) {
									if (e=="e1") info_dialog(jQuery.jgrid.errors.errcap,"function 'custom_value' "+$.jgrid.edit.msg.novalue,jQuery.jgrid.edit.bClose);
									else info_dialog(jQuery.jgrid.errors.errcap,e.message,jQuery.jgrid.edit.bClose);
								}
								return true;
							}
						});
					} else {
					switch ($(this).get(0).type) {
						case "checkbox":
							if($(this).attr("checked")) {
								postdata[this.name]= $(this).val();
							}else {
								var ofv = $(this).attr("offval");
								postdata[this.name]= ofv;
							}
						break;
						case "select-one":
							postdata[this.name]= $("option:selected",this).val();
							extpost[this.name]= $("option:selected",this).text();
						break;
						case "select-multiple":
							postdata[this.name]= $(this).val();
							if(postdata[this.name]) postdata[this.name] = postdata[this.name].join(",");
							else postdata[this.name] ="";
							var selectedText = [];
							$("option:selected",this).each(
								function(i,selected){
									selectedText[i] = $(selected).text();
								}
							);
							extpost[this.name]= selectedText.join(",");
						break;								
						case "password":
						case "text":
						case "textarea":
						case "button":
							postdata[this.name] = $(this).val();
							postdata[this.name] = !$t.p.autoencode ? postdata[this.name] : $.jgrid.htmlEncode(postdata[this.name]);
						break;
					}
					}
				});
				return true;
			}
			function createData(rowid,obj,tb,maxcols){
				var nm, hc,trdata, cnt=0,tmp, dc,elc, retpos=[], ind=false, rp,cp,
				tdtmpl = "<td class='CaptionTD ui-widget-content'>&#160;</td><td class='DataTD ui-widget-content' style='white-space:pre'>&#160;</td>", tmpl=""; //*2
				for (var i =1;i<=maxcols;i++) {
					tmpl += tdtmpl;
				}
				if(rowid != '_empty') {
					ind = $(obj).jqGrid("getInd",rowid);
				}
				$(obj.p.colModel).each( function(i) {
					nm = this.name;
					// hidden fields are included in the form
					if(this.editrules && this.editrules.edithidden == true) {
						hc = false;
					} else {
						hc = this.hidden === true ? true : false;
					}
					dc = hc ? "style='display:none'" : "";
					if ( nm !== 'cb' && nm !== 'subgrid' && this.editable===true && nm !== 'rn' && nm!=='rb') {//添加单选框判断
						if(ind === false) {
							tmp = "";
						} else {
							if(nm == obj.p.ExpandColumn && obj.p.treeGrid === true) {
								tmp = $("td:eq("+i+")",obj.rows[ind]).text();
							} else {
								try {
									tmp =  $.unformat($("td:eq("+i+")",obj.rows[ind]),{colModel:this},i);
								} catch (_) {
									tmp = $("td:eq("+i+")",obj.rows[ind]).html();
								}
							}
						}
						var opt = $.extend({}, this.editoptions || {} ,{id:nm,name:nm});
						frmopt = $.extend({}, {elmprefix:'',elmsuffix:'',rowabove:false,rowcontent:''}, this.formoptions || {}),
						rp = parseInt(frmopt.rowpos) || cnt+1,
						cp = parseInt((parseInt(frmopt.colpos) || 1)*2);
						if(rowid == "_empty" && opt.defaultValue ) {
							tmp = $.isFunction(opt.defaultValue) ? opt.defaultValue() : opt.defaultValue; 
						}
						if(!this.edittype) this.edittype = "text";
						elc = createEl(this.edittype,opt,tmp,false,$.extend({},$.jgrid.ajaxOptions,obj.p.ajaxSelectOptions || {}));
						if(tmp == "" && this.edittype == "checkbox") {tmp = $(elc).attr("offval");}
						if(rp_ge.checkOnSubmit || rp_ge.checkOnUpdate) rp_ge._savedData[nm] = tmp;
						$(elc).addClass("FormElement");
						trdata = $(tb).find("tr[rowpos="+rp+"]");
						if(frmopt.rowabove) {
							var newdata = $("<tr><td class='contentinfo' colspan='"+(maxcols*2)+"'>"+frmopt.rowcontent+"</td></tr>");
							$(tb).append(newdata);
							newdata[0].rp = rp;
						}
						if ( trdata.length==0 ) {
							trdata = $("<tr "+dc+" rowpos='"+rp+"'></tr>").addClass("FormData").attr("id","tr_"+nm);
							$(trdata).append(tmpl);
							$(tb).append(trdata);
							trdata[0].rp = rp;
						}
						$("td:eq("+(cp-2)+")",trdata[0]).html( typeof frmopt.label === 'undefined' ? obj.p.colNames[i]: frmopt.label);
						$("td:eq("+(cp-1)+")",trdata[0]).append(frmopt.elmprefix).append(elc).append(frmopt.elmsuffix);
						retpos[cnt] = i;
						cnt++;
					};
				});
				if( cnt > 0) {
					var idrow = $("<tr class='FormData' style='display:none'><td class='CaptionTD'></td><td colspan='"+ (maxcols*2-1)+"' class='DataTD'><input class='FormElement' id='id_g' type='text' name='id' value='"+rowid+"'/></td></tr>");
					idrow[0].rp = cnt+999;
					$(tb).append(idrow);
					if(rp_ge.checkOnSubmit || rp_ge.checkOnUpdate) rp_ge._savedData.id = rowid;
				}
				return retpos;
			}
			function fillData(rowid,obj,fmid){
				var nm, hc,cnt=0,tmp, fld,opt,vl,vlc;
				if(rp_ge.checkOnSubmit || rp_ge.checkOnUpdate) {rp_ge._savedData = {};rp_ge._savedData.id=rowid;}
				var cm = obj.p.colModel;
				if(rowid == '_empty') {
					$(cm).each(function(i){
						nm = this.name;
						opt = $.extend({}, this.editoptions || {} );
						fld = $("#"+$.jgrid.jqID(nm),"#"+fmid);
						if(fld[0] != null) {
							vl = "";
							if(opt.defaultValue ) {
								vl = $.isFunction(opt.defaultValue) ? opt.defaultValue() : opt.defaultValue;
								if(fld[0].type=='checkbox') {
									vlc = vl.toLowerCase();
									if(vlc.search(/(false|0|no|off|undefined)/i)<0 && vlc!=="") {
										fld[0].checked = true;
										fld[0].defaultChecked = true;
										fld[0].value = vl;
									} else {
										fld.attr({checked:"",defaultChecked:""});
									}
								} else {fld.val(vl); }
							} else {
								if( fld[0].type=='checkbox' ) {
									fld[0].checked = false;
									fld[0].defaultChecked = false;
									vl = $(fld).attr("offval");
								} else if (fld[0].type.substr(0,6)=='select') {
									fld[0].selectedIndex = 0; 
								} else {
									fld.val(vl);
								}
							}
							if(rp_ge.checkOnSubmit===true || rp_ge.checkOnUpdate) rp_ge._savedData[nm] = vl;
						}
					});
					$("#id_g","#"+fmid).val("_empty");
					return;
				}
				var tre = $(obj).jqGrid("getInd",rowid,true);
				if(!tre) return;
				$('td',tre).each( function(i) {
					nm = cm[i].name;
					// hidden fields are included in the form
					if ( nm !== 'cb' && nm !== 'subgrid' && nm !== 'rn' && cm[i].editable===true && nm !='rb') {//添加单选框判断
						if(nm == obj.p.ExpandColumn && obj.p.treeGrid === true) {
							tmp = $(this).text();
						} else {
							try {
								tmp =  $.unformat(this,{colModel:cm[i]},i);
							} catch (_) {
								tmp = $(this).html();
							}
						}
						if(rp_ge.checkOnSubmit===true || rp_ge.checkOnUpdate) rp_ge._savedData[nm] = tmp;
						nm = $.jgrid.jqID(nm);
						switch (cm[i].edittype) {
							case "password":
							case "text":
							case "button" :
							case "image":
								tmp = $.jgrid.htmlDecode(tmp);
								$("#"+nm,"#"+fmid).val(tmp);
								break;
							case "textarea":
								if(tmp == "&nbsp;" || tmp == "&#160;" || (tmp.length==1 && tmp.charCodeAt(0)==160) ) {tmp='';}
								$("#"+nm,"#"+fmid).val(tmp);
								break;
							case "select":
								var opv = tmp.split(",");
								opv = $.map(opv,function(n){return $.trim(n)});
								$("#"+nm+" option","#"+fmid).each(function(j){
									if (!cm[i].editoptions.multiple && (opv[0] == $(this).text() || opv[0] == $(this).val()) ){
										this.selected= true;
									} else if (cm[i].editoptions.multiple){
										if(  $.inArray($(this).text(), opv ) > -1 || $.inArray($(this).val(), opv ) > -1  ){
											this.selected = true;
										}else{
											this.selected = false;
										}
									} else {
										this.selected = false;
									}
								});
								break;
							case "checkbox":
								tmp = tmp+"";
								tmp = tmp.toLowerCase();
								if(tmp.search(/(false|0|no|off|undefined)/i)<0 && tmp!=="") {
									$("#"+nm,"#"+fmid).attr("checked",true);
									$("#"+nm,"#"+fmid).attr("defaultChecked",true); //ie
								} else {
									$("#"+nm,"#"+fmid).attr("checked",false);
									$("#"+nm,"#"+fmid).attr("defaultChecked",""); //ie
								}
								break;
							case 'custom' :
								try {
									if(cm[i].editoptions && $.isFunction(cm[i].editoptions.custom_value)) {
										var dummy = cm[i].editoptions.custom_value($("#"+nm,"#"+fmid),'set',tmp);
									} else throw "e1";
								} catch (e) {
									if (e=="e1") info_dialog(jQuery.jgrid.errors.errcap,"function 'custom_value' "+$.jgrid.edit.msg.nodefined,jQuery.jgrid.edit.bClose);
									else info_dialog(jQuery.jgrid.errors.errcap,e.message,jQuery.jgrid.edit.bClose);
								}
								break;
						}
						cnt++;
					}
				});
				if(cnt>0) { $("#id_g","#"+frmtb).val(rowid); }
			}
			function postIt() {
				var copydata, ret=[true,"",""], onCS = {};
				if($.isFunction(rp_ge.beforeCheckValues)) {
					var retvals = rp_ge.beforeCheckValues(postdata,$("#"+frmgr),postdata.id == "_empty" ? "add" : "edit");
					if(retvals && typeof(retvals) === 'object') postdata = retvals;
				}
				for( var key in postdata ){
					ret = checkValues(postdata[key],key,$t);
					if(ret[0] == false) break;
				}
				if(ret[0]) {
					if( $.isFunction( rp_ge.onclickSubmit)) { onCS = rp_ge.onclickSubmit(rp_ge,postdata) || {}; }
					if( $.isFunction(rp_ge.beforeSubmit))  { ret = rp_ge.beforeSubmit(postdata,$("#"+frmgr)); }
				}
				gurl = rp_ge.url ? rp_ge.url : $($t).jqGrid('getGridParam','editurl');
				if(ret[0]) {
					if(!gurl) { ret[0]=false; ret[1] += " "+$.jgrid.errors.nourl; }
				}
				if(ret[0] === false) {
					$("#FormError>td","#"+frmtb).html(ret[1]);
					$("#FormError","#"+frmtb).show();
					return;
				}
				if(!p.processing) {
					p.processing = true;
					$("#sData", "#"+frmtb+"_2").addClass('ui-state-active');
					// we add to pos data array the action - the name is oper
					postdata.oper = postdata.id == "_empty" ? "add" : "edit";
					postdata = $.extend(postdata,rp_ge.editData,onCS);
					$.ajax( $.extend({
						url:gurl,
						type: rp_ge.mtype,
						data: $.isFunction(rp_ge.serializeEditData) ? rp_ge.serializeEditData(postdata) :  postdata,
						complete:function(data,Status){
							if(Status != "success") {
							    ret[0] = false;
							    if ($.isFunction(rp_ge.errorTextFormat)) {
							        ret[1] = rp_ge.errorTextFormat(data);
							    } else {
							        ret[1] = Status + " Status: '" + data.statusText + "'. Error code: " + data.status;
								}
							} else {
								// data is posted successful
								// execute aftersubmit with the returned data from server
								if( $.isFunction(rp_ge.afterSubmit) ) {
									ret = rp_ge.afterSubmit(data,postdata);
								}
							}
							if(ret[0] === false) {
								$("#FormError>td","#"+frmtb).html(ret[1]);
								$("#FormError","#"+frmtb).show();
							} else {
								// remove some values if formattaer select or checkbox
								$.each($t.p.colModel, function(i,n){
									if(extpost[this.name] && this.formatter && this.formatter=='select') {
										try {delete extpost[this.name];} catch (e) {}
									}
								});
								postdata = $.extend(postdata,extpost);
								// the action is add
								if(postdata.id=="_empty" ) {
									//id processing
									// user not set the id ret[2]
									if(!ret[2]) { ret[2] = parseInt($t.p.records)+1; }
									postdata.id = ret[2];
									if(rp_ge.closeAfterAdd) {
										if(rp_ge.reloadAfterSubmit) { $($t).trigger("reloadGrid"); }
										else {
											$($t).jqGrid("addRowData",ret[2],postdata,p.addedrow);
											$($t).jqGrid("setSelection",ret[2]);
										}
										hideModal("#"+IDs.themodal,{gb:"#gbox_"+gID,jqm:p.jqModal,onClose: rp_ge.onClose});
									} else if (rp_ge.clearAfterAdd) {
										if(rp_ge.reloadAfterSubmit) { $($t).trigger("reloadGrid"); }
										else { $($t).jqGrid("addRowData",ret[2],postdata,p.addedrow); }
										fillData("_empty",$t,frmgr);
									} else {
										if(rp_ge.reloadAfterSubmit) { $($t).trigger("reloadGrid"); }
										else { $($t).jqGrid("addRowData",ret[2],postdata,p.addedrow); }
									}
								} else {
									// the action is update
									if(rp_ge.reloadAfterSubmit) {
										$($t).trigger("reloadGrid");
										if( !rp_ge.closeAfterEdit ) { setTimeout(function(){$($t).jqGrid("setSelection",postdata.id);},1000); }
									} else {
										if($t.p.treeGrid === true) {
											$($t).jqGrid("setTreeRow",postdata.id,postdata);
										} else {
											$($t).jqGrid("setRowData",postdata.id,postdata);
										}
									}
									if(rp_ge.closeAfterEdit) { hideModal("#"+IDs.themodal,{gb:"#gbox_"+gID,jqm:p.jqModal,onClose: rp_ge.onClose}); }
								}
								if($.isFunction(rp_ge.afterComplete)) {
									copydata = data;
									setTimeout(function(){rp_ge.afterComplete(copydata,postdata,$("#"+frmgr));copydata=null;},500);
								}
							}
							p.processing=false;
							if(rp_ge.checkOnSubmit || rp_ge.checkOnUpdate) {
								$("#"+frmgr).data("disabled",false);
								if(rp_ge._savedData.id !="_empty") rp_ge._savedData = postdata;
							}
							$("#sData", "#"+frmtb+"_2").removeClass('ui-state-active');
							try{$(':input:visible',"#"+frmgr)[0].focus();} catch (e){}
						},
						error:function(xhr,st,err){
							$("#FormError>td","#"+frmtb).html(st+ " : "+err);
							$("#FormError","#"+frmtb).show();
							p.processing=false;
							$("#"+frmgr).data("disabled",false);
							$("#sData", "#"+frmtb+"_2").removeClass('ui-state-active');
						}
					}, $.jgrid.ajaxOptions, rp_ge.ajaxEditOptions ));
				}
				
			}
			function compareData(nObj, oObj ) {
				var ret = false,key;
				for (key in nObj) {
					if(nObj[key] != oObj[key]) {
						ret = true;
						break;
					}
				}
				return ret;
			}
		});
	},
	viewGridRow : function(rowid, p){
		p = $.extend({
			top : 0,
			left: 0,
			width: 0,
			height: 'auto',
			dataheight: 'auto',
			modal: false,
			drag: true,
			resize: true,
			jqModal: true,
			closeOnEscape : false,
			labelswidth: '30%',
			closeicon: [],
			navkeys: [false,38,40],
			onClose: null
		}, $.jgrid.view, p || {});
		return this.each(function(){
			var $t = this;
			if (!$t.grid || !rowid) { return; }
			if(!p.imgpath) { p.imgpath= $t.p.imgpath; }
			// I hate to rewrite code, but ...
			var gID = $t.p.id,
			frmgr = "ViewGrid_"+gID , frmtb = "ViewTbl_"+gID,
			IDs = {themodal:'viewmod'+gID,modalhead:'viewhd'+gID,modalcontent:'viewcnt'+gID, scrollelm : frmgr},
			maxCols = 1, maxRows=0;
			if ( $("#"+IDs.themodal).html() != null ) {
				$(".ui-jqdialog-title","#"+IDs.modalhead).html(p.caption);
				$("#FormError","#"+frmtb).hide();
				fillData(rowid,$t);
				viewModal("#"+IDs.themodal,{gbox:"#gbox_"+gID,jqm:p.jqModal, jqM: false, modal:p.modal});
				focusaref();
			} else {
				$($t.p.colModel).each( function(i) {
					var fmto = this.formoptions;
					maxCols = Math.max(maxCols, fmto ? fmto.colpos || 0 : 0 );
					maxRows = Math.max(maxRows, fmto ? fmto.rowpos || 0 : 0 );
				});
				var dh = isNaN(p.dataheight) ? p.dataheight : p.dataheight+"px";
				var flr, frm = $("<form name='FormPost' id='"+frmgr+"' class='FormGrid' style='width:100%;overflow:auto;position:relative;height:"+dh+";'></form>"),
				tbl =$("<table id='"+frmtb+"' class='EditTable' cellspacing='1' cellpading='2' border='0' style='table-layout:fixed'><tbody></tbody></table>");
				// set the id.
				$(frm).append(tbl);
				var valref = createData(rowid, $t, tbl, maxCols),
				rtlb = $t.p.direction == "rtl" ? true :false,
				bp = rtlb ? "nData" : "pData",
				bn = rtlb ? "pData" : "nData",

				// buttons at footer
				bP = "<a href='javascript:void(0)' id='"+bp+"' class='fm-button ui-state-default ui-corner-left'><span class='ui-icon ui-icon-triangle-1-w'></span></div>",
				bN = "<a href='javascript:void(0)' id='"+bn+"' class='fm-button ui-state-default ui-corner-right'><span class='ui-icon ui-icon-triangle-1-e'></span></div>",
				bC  ="<a href='javascript:void(0)' id='cData' class='fm-button ui-state-default ui-corner-all'>"+p.bClose+"</a>";
				if(maxRows >  0) {
					var sd=[];
					$.each($(tbl)[0].rows,function(i,r){
						sd[i] = r;
					});
					sd.sort(function(a,b){
						if(a.rp > b.rp) {return 1;}
						if(a.rp < b.rp) {return -1;}
						return 0;
					});
					$.each(sd, function(index, row) {
						$('tbody',tbl).append(row);
					});
				}
				p.gbox = "#gbox_"+gID;
				var cle = false;
				if(p.closeOnEscape===true){
					p.closeOnEscape = false;
					cle = true;
				}				
				var bt = $("<span></span>").append(frm).append("<table border='0' class='EditTable' id='"+frmtb+"_2'><tbody><tr id='Act_Buttons'><td class='navButton ui-widget-content' width='"+p.labelswidth+"'>"+(rtlb ? bN+bP : bP+bN)+"</td><td class='EditButton ui-widget-content'>"+bC+"</td></tr></tbody></table>");
				createModal(IDs,bt,p,"#gview_"+$t.p.id,$("#gview_"+$t.p.id)[0]);
				if(rtlb) {
					$("#pData, #nData","#"+frmtb+"_2").css("float","right");
					$(".EditButton","#"+frmtb+"_2").css("text-align","left");
				}
				bt = null;
				$("#"+IDs.themodal).keydown( function( e ) {
					if(e.which === 27) {
						if(cle)	hideModal(this,{gb:p.gbox,jqm:p.jqModal, onClose: p.onClose});
						return false;
					}
					if(p.navkeys[0]===true) {
						if(e.which === p.navkeys[1]){ //up
							$("#pData", "#"+frmtb+"_2").trigger("click");
							return false;
						}
						if(e.which === p.navkeys[2]){ //down
							$("#nData", "#"+frmtb+"_2").trigger("click");
							return false;
						}
					}
				});
				p.closeicon = $.extend([true,"left","ui-icon-close"],p.closeicon);
				if(p.closeicon[0]==true) {
					$("#cData","#"+frmtb+"_2").addClass(p.closeicon[1] == "right" ? 'fm-button-icon-right' : 'fm-button-icon-left')
					.append("<span class='ui-icon "+p.closeicon[2]+"'></span>");
				}
				viewModal("#"+IDs.themodal,{gbox:"#gbox_"+gID,jqm:p.jqModal, modal:p.modal});
				$(".fm-button:not(.ui-state-disabled)","#"+frmtb+"_2").hover(
				   function(){$(this).addClass('ui-state-hover');}, 
				   function(){$(this).removeClass('ui-state-hover');}
				);
				focusaref();
				$("#cData", "#"+frmtb+"_2").click(function(e){
					hideModal("#"+IDs.themodal,{gb:"#gbox_"+gID,jqm:p.jqModal, onClose: p.onClose});
					return false;
				});
				$("#nData", "#"+frmtb+"_2").click(function(e){
					$("#FormError","#"+frmtb).hide();
					var npos = getCurrPos();
					npos[0] = parseInt(npos[0]);
					if(npos[0] != -1 && npos[1][npos[0]+1]) {
						if($.isFunction(p.onclickPgButtons)) {
							p.onclickPgButtons('next',$("#"+frmgr),npos[1][npos[0]]);
						}
						fillData(npos[1][npos[0]+1],$t);
						$($t).jqGrid("setSelection",npos[1][npos[0]+1]);
						if($.isFunction(p.afterclickPgButtons)) {
							p.afterclickPgButtons('next',$("#"+frmgr),npos[1][npos[0]+1]);
						}
						updateNav(npos[0]+1,npos[1].length-1);
					};
					focusaref();
					return false;
				});
				$("#pData", "#"+frmtb+"_2").click(function(e){
					$("#FormError","#"+frmtb).hide();
					var ppos = getCurrPos();
					if(ppos[0] != -1 && ppos[1][ppos[0]-1]) {
						if($.isFunction(p.onclickPgButtons)) {
							p.onclickPgButtons('prev',$("#"+frmgr),ppos[1][ppos[0]]);
						}
						fillData(ppos[1][ppos[0]-1],$t);
						$($t).jqGrid("setSelection",ppos[1][ppos[0]-1]);
						if($.isFunction(p.afterclickPgButtons)) {
							p.afterclickPgButtons('prev',$("#"+frmgr),ppos[1][ppos[0]-1]);
						}
						updateNav(ppos[0]-1,ppos[1].length-1);
					};
					focusaref();
					return false;
				});
			};
			function focusaref(){ //Sfari 3 issues
				if(p.closeOnEscape===true || p.navkeys[0]===true) {
					setTimeout(function(){$(".ui-jqdialog-titlebar-close","#"+IDs.modalhead).focus()},0);
				}
			}
			var posInit =getCurrPos();
			updateNav(posInit[0],posInit[1].length-1);
			function updateNav(cr,totr,rid){
				if (cr==0) { $("#pData","#"+frmtb+"_2").addClass('ui-state-disabled'); } else { $("#pData","#"+frmtb+"_2").removeClass('ui-state-disabled'); }
				if (cr==totr) { $("#nData","#"+frmtb+"_2").addClass('ui-state-disabled'); } else { $("#nData","#"+frmtb+"_2").removeClass('ui-state-disabled'); }
			}
			function getCurrPos() {
				var rowsInGrid = $($t).jqGrid("getDataIDs"),
				selrow = $("#id_g","#"+frmtb).val(),
				pos = $.inArray(selrow,rowsInGrid);
				return [pos,rowsInGrid];
			}
			function createData(rowid,obj,tb,maxcols){
				var nm, hc,trdata, tdl, tde, cnt=0,tmp, dc, retpos=[], ind=false,
				tdtmpl = "<td class='CaptionTD ui-widget-content' width='"+p.labelswidth+"'>&#160;</td><td class='DataTD ui-helper-reset ui-widget-content' style='white-space:pre;'>&#160;</td>", tmpl="",
				tdtmpl2 = "<td class='CaptionTD ui-widget-content'>&#160;</td><td class='DataTD ui-widget-content' style='white-space:pre;'>&#160;</td>",
				fmtnum = ['integer','number','currency'],max1 =0, max2=0 ,maxw,setme;
				for (var i =1;i<=maxcols;i++) {
					tmpl += i == 1 ? tdtmpl : tdtmpl2;
				}
				// find max number align rigth with property formatter
				$(obj.p.colModel).each( function(i) {
					if(this.editrules && this.editrules.edithidden === true) {
						hc = false;
					} else {
						hc = this.hidden === true ? true : false;
					}
					if(!hc && this.align==='right') {
						if(this.formatter && $.inArray(this.formatter,fmtnum) !== -1 ) {
							max1 = Math.max(max1,parseInt(this.width,10));
						} else {
							max2 = Math.max(max2,parseInt(this.width,10));
						}
					}
				});
				maxw  = max1 !==0 ? max1 : max2 !==0 ? max2 : 0;
				ind = $(obj).jqGrid("getInd",rowid);
				$(obj.p.colModel).each( function(i) {
					nm = this.name;
					setme = false;
					// hidden fields are included in the form
					if(this.editrules && this.editrules.edithidden === true) {
						hc = false;
					} else {
						hc = this.hidden === true ? true : false;
					}
					dc = hc ? "style='display:none'" : "";
					if ( nm !== 'cb' && nm !== 'subgrid' && nm !== 'rn' && nm!='rb') {//添加单选框判断
						if(ind === false) {
							tmp = "";
						} else {
							if(nm == obj.p.ExpandColumn && obj.p.treeGrid === true) {
								tmp = $("td:eq("+i+")",obj.rows[ind]).text();
							} else {
								tmp = $("td:eq("+i+")",obj.rows[ind]).html();
							}
						}
						setme = this.align === 'right' && maxw !==0 ? true : false;
						var opt = $.extend({}, this.editoptions || {} ,{id:nm,name:nm}),
						frmopt = $.extend({},{rowabove:false,rowcontent:''}, this.formoptions || {}),
						rp = parseInt(frmopt.rowpos) || cnt+1,
						cp = parseInt((parseInt(frmopt.colpos) || 1)*2);
						if(frmopt.rowabove) {
							var newdata = $("<tr><td class='contentinfo' colspan='"+(maxcols*2)+"'>"+frmopt.rowcontent+"</td></tr>");
							$(tb).append(newdata);
							newdata[0].rp = rp;
						}
						trdata = $(tb).find("tr[rowpos="+rp+"]");
						if ( trdata.length==0 ) {
							trdata = $("<tr "+dc+" rowpos='"+rp+"'></tr>").addClass("FormData").attr("id","trv_"+nm);
							$(trdata).append(tmpl);
							$(tb).append(trdata);
							trdata[0].rp = rp;
						}
						$("td:eq("+(cp-2)+")",trdata[0]).html('<b>'+ (typeof frmopt.label === 'undefined' ? obj.p.colNames[i]: frmopt.label)+'</b>');
						$("td:eq("+(cp-1)+")",trdata[0]).append("<span>"+tmp+"</span>").attr("id","v_"+nm);
						if(setme){
							$("td:eq("+(cp-1)+") span",trdata[0]).css({'text-align':'right',width:maxw+"px"});
						}
						retpos[cnt] = i;
						cnt++;
					};
				});
				if( cnt > 0) {
					var idrow = $("<tr class='FormData' style='display:none'><td class='CaptionTD'></td><td colspan='"+ (maxcols*2-1)+"' class='DataTD'><input class='FormElement' id='id_g' type='text' name='id' value='"+rowid+"'/></td></tr>");
					idrow[0].rp = cnt+99;
					$(tb).append(idrow);
				}
				return retpos;
			};
			function fillData(rowid,obj){
				var nm, hc,cnt=0,tmp, opt,trv;
				trv = $(obj).jqGrid("getInd",rowid,true);
				if(!trv) return;
				$('td',trv).each( function(i) {
					nm = obj.p.colModel[i].name;
					// hidden fields are included in the form
					if(obj.p.colModel[i].editrules && obj.p.colModel[i].editrules.edithidden === true) {
						hc = false;
					} else {
						hc = obj.p.colModel[i].hidden === true ? true : false;
					}
					if ( nm !== 'cb' && nm !== 'subgrid' && nm !== 'rn' && nm!=='rb') {//添加单选框判断
						if(nm == obj.p.ExpandColumn && obj.p.treeGrid === true) {
							tmp = $(this).text();
						} else {
							tmp = $(this).html();
						}
						opt = $.extend({},obj.p.colModel[i].editoptions || {});
						nm = $.jgrid.jqID("v_"+nm);
						$("#"+nm+" span","#"+frmtb).html(tmp);
						if (hc) { $("#"+nm,"#"+frmtb).parents("tr:first").hide(); }
						cnt++;
					}
				});
				if(cnt>0) { $("#id_g","#"+frmtb).val(rowid); }
			};
		});
	},
	delGridRow : function(rowids,p) {
		p = $.extend({
			top : 0,
			left: 0,
			width: 240,
			height: 'auto',
			dataheight : 'auto',
			modal: false,
			drag: true,
			resize: true,
			url : '',
			mtype : "POST",
			reloadAfterSubmit: true,
			beforeShowForm: null,
			afterShowForm: null,
			beforeSubmit: null,
			onclickSubmit: null,
			afterSubmit: null,
			jqModal : true,
			closeOnEscape : false,
			delData: {},
			delicon : [],
			cancelicon : [],
			onClose : null,
			ajaxDelOptions : {},
			serializeDelData : null
		}, $.jgrid.del, p ||{});
		rp_ge = p;
		return this.each(function(){
			var $t = this;
			if (!$t.grid ) { return; }
			if(!rowids) { return; }
			var onBeforeShow = typeof p.beforeShowForm === 'function' ? true: false,
			onAfterShow = typeof p.afterShowForm === 'function' ? true: false,
			gID = $t.p.id, onCS = {},
			dtbl = "DelTbl_"+gID,
			IDs = {themodal:'delmod'+gID,modalhead:'delhd'+gID,modalcontent:'delcnt'+gID, scrollelm: dtbl};
			if (isArray(rowids)) { rowids = rowids.join(); }
			if ( $("#"+IDs.themodal).html() != null ) {
				$("#DelData>td","#"+dtbl).text(rowids);
				$("#DelError","#"+dtbl).hide();
				if( p.processing === true) {
					p.processing=false;
					$("#dData", "#"+dtbl).removeClass('ui-state-active');
				}
				if(onBeforeShow) { p.beforeShowForm($("#"+dtbl)); }
				viewModal("#"+IDs.themodal,{gbox:"#gbox_"+gID,jqm:p.jqModal,jqM: false, modal:p.modal});
				if(onAfterShow) { p.afterShowForm($("#"+dtbl)); }
			} else {
				var dh = isNaN(p.dataheight) ? p.dataheight : p.dataheight+"px";
				var tbl = "<div id='"+dtbl+"' class='formdata' style='width:100%;overflow:auto;position:relative;height:"+dh+";'>";
				tbl += "<table class='DelTable'><tbody>";
				// error data 
				tbl += "<tr id='DelError' style='display:none'><td class='ui-state-error'></td></tr>";
				tbl += "<tr id='DelData' style='display:none'><td >"+rowids+"</td></tr>";
				tbl += "<tr><td class=\"delmsg\" style=\"white-space:pre;\">"+p.msg+"</td></tr><tr><td >&#160;</td></tr>";
				// buttons at footer
				tbl += "</tbody></table></div>"
				var bS  = "<a href='javascript:void(0)' id='dData' class='fm-button ui-state-default ui-corner-all'>"+p.bSubmit+"</a>",
				bC  = "<a href='javascript:void(0)' id='eData' class='fm-button ui-state-default ui-corner-all'>"+p.bCancel+"</a>";
				tbl += "<table cellspacing='0' cellpadding='0' border='0' class='EditTable' id='"+dtbl+"_2'><tbody><tr><td class='DataTD ui-widget-content'></td></tr><tr style='display:block;height:3px;'><td></td></tr><tr><td class='DelButton EditButton'>"+bS+"&#160;"+bC+"</td></tr></tbody></table>";
				p.gbox = "#gbox_"+gID;
				createModal(IDs,tbl,p,"#gview_"+$t.p.id,$("#gview_"+$t.p.id)[0]);
				$(".fm-button","#"+dtbl+"_2").hover(
				   function(){$(this).addClass('ui-state-hover');}, 
				   function(){$(this).removeClass('ui-state-hover');}
				);
				p.delicon = $.extend([true,"left","ui-icon-scissors"],p.delicon);
				p.cancelicon = $.extend([true,"left","ui-icon-cancel"],p.cancelicon);
				if(p.delicon[0]==true) {
					$("#dData","#"+dtbl+"_2").addClass(p.delicon[1] == "right" ? 'fm-button-icon-right' : 'fm-button-icon-left')
					.append("<span class='ui-icon "+p.delicon[2]+"'></span>");
				}
				if(p.cancelicon[0]==true) {
					$("#eData","#"+dtbl+"_2").addClass(p.cancelicon[1] == "right" ? 'fm-button-icon-right' : 'fm-button-icon-left')
					.append("<span class='ui-icon "+p.cancelicon[2]+"'></span>");
				}				
				$("#dData","#"+dtbl+"_2").click(function(e){
					var ret=[true,""]; onCS = {};
					var postdata = $("#DelData>td","#"+dtbl).text(); //the pair is name=val1,val2,...
					if( typeof p.onclickSubmit === 'function' ) { onCS = p.onclickSubmit(rp_ge) || {}; }
					if( typeof p.beforeSubmit === 'function' ) { ret = p.beforeSubmit(postdata); }
					if(ret[0]){
						var gurl = rp_ge.url ? rp_ge.url : $($t).jqGrid('getGridParam','editurl');
						if(!gurl) { ret[0]=false;ret[1] += " "+$.jgrid.errors.nourl;}
					}
					if(ret[0] === false) {
						$("#DelError>td","#"+dtbl).html(ret[1]);
						$("#DelError","#"+dtbl).show();
					} else {
						if(!p.processing) {
							p.processing = true;
							$(this).addClass('ui-state-active');
							var postd = $.extend({oper:"del", id:postdata},rp_ge.delData, onCS);
							$.ajax( $.extend({
								url:gurl,
								type: p.mtype,
								data: $.isFunction(p.serializeDelData) ? p.serializeDelData(postd) : postd,
								complete:function(data,Status){
									if(Status != "success") {
										ret[0] = false;
									    if ($.isFunction(rp_ge.errorTextFormat)) {
									        ret[1] = rp_ge.errorTextFormat(data);
									    } else {
									        ret[1] = Status + " Status: '" + data.statusText + "'. Error code: " + data.status;
										}
									} else {
										// data is posted successful
										// execute aftersubmit with the returned data from server
										if( typeof rp_ge.afterSubmit === 'function' ) {
											ret = rp_ge.afterSubmit(data,postdata);
										}
									}
									if(ret[0] === false) {
										$("#DelError>td","#"+dtbl).html(ret[1]);
										$("#DelError","#"+dtbl).show();
									} else {
										if(rp_ge.reloadAfterSubmit) {
											if($t.p.treeGrid) {
												$($t).jqGrid("setGridParam",{treeANode:0,datatype:$t.p.treedatatype});
											}
											$($t).trigger("reloadGrid");
										} else {
											var toarr = [];
											toarr = postdata.split(",");
											if($t.p.treeGrid===true){
												try {$($t).jqGrid("delTreeNode",toarr[0])} catch(e){}
											} else {
												for(var i=0;i<toarr.length;i++) {
													$($t).jqGrid("delRowData",toarr[i]);
												}
											}
											$t.p.selrow = null;
											$t.p.selarrrow = [];
										}
										if($.isFunction(rp_ge.afterComplete)) {
											setTimeout(function(){rp_ge.afterComplete(data,postdata);},500);
										}
									}
									p.processing=false;
									$("#dData", "#"+dtbl+"_2").removeClass('ui-state-active');
									if(ret[0]) { hideModal("#"+IDs.themodal,{gb:"#gbox_"+gID,jqm:p.jqModal, onClose: rp_ge.onClose}); }
								},
								error:function(xhr,st,err){
									$("#DelError>td","#"+dtbl).html(st+ " : "+err);
									$("#DelError","#"+dtbl).show();
									p.processing=false;
									$("#dData", "#"+dtbl+"_2").removeClass('ui-state-active');;
								}
							}, $.jgrid.ajaxOptions, p.ajaxDelOptions));
						}
					}
					return false;
				});
				$("#eData", "#"+dtbl+"_2").click(function(e){
					hideModal("#"+IDs.themodal,{gb:"#gbox_"+gID,jqm:p.jqModal, onClose: rp_ge.onClose});
					return false;
				});
				if(onBeforeShow) { p.beforeShowForm($("#"+dtbl)); }
				viewModal("#"+IDs.themodal,{gbox:"#gbox_"+gID,jqm:p.jqModal,modal:p.modal});
				if(onAfterShow) { p.afterShowForm($("#"+dtbl)); }
			}
			if(p.closeOnEscape===true) {
				setTimeout(function(){$(".ui-jqdialog-titlebar-close","#"+IDs.modalhead).focus()},0);
			}
		});
	},
	navGrid : function (elem, o, pEdit,pAdd,pDel,pSearch, pView) {
		o = $.extend({
			edit: true,
			editicon: "ui-icon-pencil",
			add: true,
			addicon:"ui-icon-plus",
			del: true,
			delicon:"ui-icon-trash",
			search: true,
			searchicon:"ui-icon-search",
			refresh: true,
			refreshicon:"ui-icon-refresh",
			refreshstate: 'firstpage',
			view: false,
			viewicon : "ui-icon-document",
			position : "left",
			closeOnEscape : true,
			afterRefresh : null
		}, $.jgrid.nav, o ||{});
		return this.each(function() {       
			var alertIDs = {themodal:'alertmod',modalhead:'alerthd',modalcontent:'alertcnt'},
			$t = this, vwidth, vheight, twd, tdw;
			if(!$t.grid) { return; }
			if ($("#"+alertIDs.themodal).html() == null) {
				if (typeof window.innerWidth != 'undefined') {
					vwidth = window.innerWidth,
					vheight = window.innerHeight
				} else if (typeof document.documentElement != 'undefined' && typeof document.documentElement.clientWidth != 'undefined' && document.documentElement.clientWidth != 0) {
					vwidth = document.documentElement.clientWidth,
					vheight = document.documentElement.clientHeight
				} else {
					vwidth=1024;
					vheight=768;
				}
				createModal(alertIDs,"<div>"+o.alerttext+"</div><span tabindex='0'><span tabindex='-1' id='jqg_alrt'></span></span>",{gbox:"#gbox_"+$t.p.id,jqModal:true,drag:true,resize:true,caption:o.alertcap,top:vheight/2-25,left:vwidth/2-100,width:200,height:'auto',closeOnEscape:o.closeOnEscape},"","",true);
			}
			var tbd,
			navtbl = $("<table cellspacing='0' cellpadding='0' border='0' class='ui-pg-table navtable' style='float:left;table-layout:auto;'><tbody><tr></tr></tbody></table>"),
			sep = "<td class='ui-pg-button ui-state-disabled' style='width:4px;'><span class='ui-separator'></span></td>",
			pgid = $($t.p.pager).attr("id") || 'pager';
			if($t.p.direction == "rtl") $(navtbl).attr("dir","rtl").css("float","right");
			if (o.add) {
				pAdd = pAdd || {};
				tbd = $("<td class='ui-pg-button ui-corner-all'></td>");
				$(tbd).append("<div class='ui-pg-div'><span class='ui-icon "+o.addicon+"'></span>"+o.addtext+"</div>");
				$("tr",navtbl).append(tbd);
				$(tbd,navtbl)
				.attr({"title":o.addtitle || "",id : pAdd.id || "add_"+$t.p.id})
				.click(function(){
					if (typeof o.addfunc == 'function') {
						o.addfunc();
					} else {
						$($t).jqGrid("editGridRow","new",pAdd);
					}
					return false;
				}).hover(function () {$(this).addClass("ui-state-hover");},
					function () {$(this).removeClass("ui-state-hover");}
				);
				tbd = null;
			}
			if (o.edit) {
				tbd = $("<td class='ui-pg-button ui-corner-all'></td>");
				pEdit = pEdit || {};
				$(tbd).append("<div class='ui-pg-div'><span class='ui-icon "+o.editicon+"'></span>"+o.edittext+"</div>");
				$("tr",navtbl).append(tbd);
				$(tbd,navtbl)
				.attr({"title":o.edittitle || "",id: pEdit.id || "edit_"+$t.p.id})
				.click(function(){
					var sr = $t.p.selrow;
					if (sr) {
						if(typeof o.editfunc == 'function') {
							o.editfunc(sr);
						} else {
							$($t).jqGrid("editGridRow",sr,pEdit);
						}
					} else {
						viewModal("#"+alertIDs.themodal,{gbox:"#gbox_"+$t.p.id,jqm:true});
						$("#jqg_alrt").focus();
					}
					return false;
				}).hover( function () {$(this).addClass("ui-state-hover");},
					function () {$(this).removeClass("ui-state-hover");}
				);
				tbd = null;
			}
			if (o.view) {
				tbd = $("<td class='ui-pg-button ui-corner-all'></td>");
				pView = pView || {};
				$(tbd).append("<div class='ui-pg-div'><span class='ui-icon "+o.viewicon+"'></span>"+o.viewtext+"</div>");
				$("tr",navtbl).append(tbd);
				$(tbd,navtbl)
				.attr({"title":o.viewtitle || "",id: pView.id || "view_"+$t.p.id})
				.click(function(){
					var sr = $t.p.selrow;
					if (sr) {
						$($t).jqGrid("viewGridRow",sr,pView);
					} else {
						viewModal("#"+alertIDs.themodal,{gbox:"#gbox_"+$t.p.id,jqm:true});
						$("#jqg_alrt").focus();
					}
					return false;
				}).hover( function () {$(this).addClass("ui-state-hover");},
					function () {$(this).removeClass("ui-state-hover");}
				);
				tbd = null;
			}
			if (o.del) {
				tbd = $("<td class='ui-pg-button ui-corner-all'></td>");
				pDel = pDel || {};
				$(tbd).append("<div class='ui-pg-div'><span class='ui-icon "+o.delicon+"'></span>"+o.deltext+"</div>");
				$("tr",navtbl).append(tbd);
				$(tbd,navtbl)
				.attr({"title":o.deltitle || "",id: pDel.id || "del_"+$t.p.id})
				.click(function(){
					var dr;
					if($t.p.multiselect) {
						dr = $t.p.selarrrow;
						if(dr.length==0) { dr = null; }
					} else {
						dr = $t.p.selrow;
					}
					if (dr) { $($t).jqGrid("delGridRow",dr,pDel); }
					else  {viewModal("#"+alertIDs.themodal,{gbox:"#gbox_"+$t.p.id,jqm:true}); $("#jqg_alrt").focus(); }
					return false;
				}).hover(function () {$(this).addClass("ui-state-hover");},
					function () {$(this).removeClass("ui-state-hover");}
				);
				tbd = null;
			}
			if(o.add || o.edit || o.del || o.view) { $("tr",navtbl).append(sep); }
			if (o.search) {
				tbd = $("<td class='ui-pg-button ui-corner-all'></td>");
				pSearch = pSearch || {};
				$(tbd).append("<div class='ui-pg-div'><span class='ui-icon "+o.searchicon+"'></span>"+o.searchtext+"</div>");
				$("tr",navtbl).append(tbd);
				$(tbd,navtbl)
				.attr({"title":o.searchtitle  || "",id:pSearch.id || "search_"+$t.p.id})
				.click(function(){
					$($t).jqGrid("searchGrid",pSearch);
					return false;
				}).hover(function () {$(this).addClass("ui-state-hover");},
					function () {$(this).removeClass("ui-state-hover");}
				);
				tbd = null;
			}
			if (o.refresh) {
				tbd = $("<td class='ui-pg-button ui-corner-all'></td>");
				$(tbd).append("<div class='ui-pg-div'><span class='ui-icon "+o.refreshicon+"'></span>"+o.refreshtext+"</div>");
				$("tr",navtbl).append(tbd);
				$(tbd,navtbl)
				.attr({"title":o.refreshtitle  || "",id: "refresh_"+$t.p.id})
				.click(function(){
					$t.p.search = false;
					try {
						var gID = $t.p.id;
						$("#fbox_"+gID).searchFilter().reset();
					} catch (e) {}
					switch (o.refreshstate) {
						case 'firstpage':
						    $($t).trigger("reloadGrid", [{page:1}]);
							break;
						case 'current':
						    $($t).trigger("reloadGrid", [{current:true}]);
							break;
					}
					if($.isFunction(o.afterRefresh)) o.afterRefresh();
					return false;
				}).hover(function () {$(this).addClass("ui-state-hover");},
					function () {$(this).removeClass("ui-state-hover");}
				);
				tbd = null;
			}
			tdw = $(".ui-jqgrid").css("font-size") || "11px";
			$('body').append("<div id='testpg2' class='ui-jqgrid ui-widget ui-widget-content' style='font-size:"+tdw+";visibility:hidden;' ></div>");
			twd = $(navtbl).clone().appendTo("#testpg2").width();
			$("#testpg2").remove();
			$("#"+pgid+"_"+o.position,"#"+pgid).append(navtbl);
			if($t.p._nvtd) {
				if(twd > $t.p._nvtd[0] ) {
					$("#"+pgid+"_"+o.position,"#"+pgid).width(twd);
					$t.p._nvtd[0] = twd;
				}
				$t.p._nvtd[1] = twd;
			}
		});
	},
	navButtonAdd : function (elem, p) {
		p = $.extend({
			caption : "newButton",
			title: '',
			buttonicon : 'ui-icon-newwin',
			onClickButton: null,
			position : "last",
			cursor : 'pointer'
		}, p ||{});
		return this.each(function() {
			if( !this.grid)  { return; }
			if( elem.indexOf("#") != 0) { elem = "#"+elem; }
			var findnav = $(".navtable",elem)[0], $t = this;
			if (findnav) {
				var tbd = $("<td></td>");
				$(tbd).addClass('ui-pg-button ui-corner-all').append("<div class='ui-pg-div'><span class='ui-icon "+p.buttonicon+"'></span>"+p.caption+"</div>");
				if(p.id) {$(tbd).attr("id",p.id);}
				if(p.position=='first'){
					if(findnav.rows[0].cells.length ===0 ) {
						$("tr",findnav).append(tbd);
					} else {
						$("tr td:eq(0)",findnav).before(tbd);
					}
				} else {
					$("tr",findnav).append(tbd);
				}
				$(tbd,findnav)
				.attr("title",p.title  || "")
				.click(function(e){
					if ($.isFunction(p.onClickButton) ) { p.onClickButton.call($t,e); }
					return false;
				})
				.hover(
					function () {$(this).addClass("ui-state-hover");},
					function () {$(this).removeClass("ui-state-hover");}
				)
				.css("cursor",p.cursor ? p.cursor : "normal");
			}
		});
	},
	navSeparatorAdd:function (elem,p) {
		p = $.extend({
			sepclass : "ui-separator",
			sepcontent: ''
		}, p ||{});		
		return this.each(function() {
			if( !this.grid)  { return; }
			if( elem.indexOf("#") != 0) { elem = "#"+elem; }
			var findnav = $(".navtable",elem)[0];
			if(findnav) {
				var sep = "<td class='ui-pg-button ui-state-disabled' style='width:4px;'><span class='"+p.sepclass+"'></span>"+p.sepcontent+"</td>";
				$("tr",findnav).append(sep);
			}
		});
	},
	GridToForm : function( rowid, formid ) {
		return this.each(function(){
			var $t = this;
			if (!$t.grid) { return; } 
			var rowdata = $($t).jqGrid("getRowData",rowid);
			if (rowdata) {
				for(var i in rowdata) {
					if ( $("[name="+i+"]",formid).is("input:radio") || $("[name="+i+"]",formid).is("input:checkbox"))  {
						$("[name="+i+"]",formid).each( function() {
							if( $(this).val() == rowdata[i] ) {
								$(this).attr("checked","checked");
							} else {
								$(this).attr("checked","");
							}
						});
					} else {
					// this is very slow on big table and form.
						$("[name="+i+"]",formid).val(rowdata[i]);
					}
				}
			}
		});
	},
	FormToGrid : function(rowid, formid, mode, position){
		return this.each(function() {
			var $t = this;
			if(!$t.grid) { return; }
			if(!mode) mode = 'set';
			if(!position) position = 'first';
			var fields = $(formid).serializeArray();
			var griddata = {};
			$.each(fields, function(i, field){
				griddata[field.name] = field.value;
			});
			if(mode=='add') $($t).jqGrid("addRowData",rowid,griddata, position);
			else if(mode=='set') $($t).jqGrid("setRowData",rowid,griddata);
		});
	}
});
})(jQuery);
;(function($){
/**
 * jqGrid extension
 * Paul Tiseo ptiseo@wasteconsultants.com
 * 
 * Dual licensed under the MIT and GPL licenses:
 * http://www.opensource.org/licenses/mit-license.php
 * http://www.gnu.org/licenses/gpl.html
**/ 
$.jgrid.extend({
	getPostData : function(){
		var $t = this[0];
		if(!$t.grid) { return; }
		return $t.p.postData;
	},
	setPostData : function( newdata ) {
		var $t = this[0];
		if(!$t.grid) { return; }
		// check if newdata is correct type
		if ( typeof(newdata) === 'object' ) {
			$t.p.postData = newdata;
		}
		else {
			alert("Error: cannot add a non-object postData value. postData unchanged.");
		}
	},
	appendPostData : function( newdata ) { 
		var $t = this[0];
		if(!$t.grid) { return; }
		// check if newdata is correct type
		if ( typeof(newdata) === 'object' ) {
			$.extend($t.p.postData, newdata);
		}
		else {
			alert("Error: cannot append a non-object postData value. postData unchanged.");
		}
	},
	setPostDataItem : function( key, val ) {
		var $t = this[0];
		if(!$t.grid) { return; }
		$t.p.postData[key] = val;
	},
	getPostDataItem : function( key ) {
		var $t = this[0];
		if(!$t.grid) { return; }
		return $t.p.postData[key];
	},
	getSelectRows:function(){//新增一个方法，得到选中的所有行的id
		var t;
		n = this[0];
		if (!n.grid) {
				return;
			}
					if (n.p.multiselect) {
						t = n.p.selarrrow;
						if (t.length == 0) {
							t = null;
						}
					} else {
						t = n.p.selrow;
					}
					
					return t;
	
	},getRowNums:function(){//新增一个方法得到所有选中行的行号
	
	   var t = [];
		n = this[0];
		if (!n.grid) {
				return;
			}
			     if(n.p.selectInfo != null)
			    
					{
					  var colMs = n.p.colModel;
			           var nameIndex;
			          for(var colMsIndex = 0 ;colMsIndex<colMs.length;colMsIndex++)
			          {
			               if(colMs[colMsIndex].name == n.p.selectInfo)
			              {
			                 nameIndex = colMsIndex;
			                 break;
			                 }
			           }
					
				
					    if (n.p.multiselect) {
						for(var i =0;i< n.p.selarrrow.length;i++)
						{
						    var tr = $("#"+n.p.selarrrow[i],n);
						    var num = $("td:eq("+nameIndex+")",tr).attr("value");
						    t.push(num);
						}
						if (t.length == 0) {
							t = null;
						}
					} else {
					 var tr = $("#"+n.p.selrow,n);
						    var num =  $("td:eq("+nameIndex+")",tr).attr("value");
						    t=num;
					
					}	
					}		
					return t;
	}, 
	removePostDataItem : function( key ) {
		var $t = this[0];
		if(!$t.grid) { return; }
		delete $t.p.postData[key];
	},
	getUserData : function(){
		var $t = this[0];
		if(!$t.grid) { return; }
		return $t.p.userData;
	},
	getUserDataItem : function( key ) {
		var $t = this[0];
		if(!$t.grid) { return; }
		return $t.p.userData[key];
	}
});
})(jQuery);




;(function($) {
/*
**
 * jqGrid extension - Tree Grid
 * Tony Tomov tony@trirand.com
 * http://trirand.com/blog/ 
 * Dual licensed under the MIT and GPL licenses:
 * http://www.opensource.org/licenses/mit-license.php
 * http://www.gnu.org/licenses/gpl.html
**/ 
$.jgrid.extend({
	setTreeNode : function(rd, row){
		return this.each(function(){
			var $t = this;
			if( !$t.grid || !$t.p.treeGrid ) { return; }
			var expCol = $t.p.expColInd;
			var expanded = $t.p.treeReader.expanded_field;
			var isLeaf = $t.p.treeReader.leaf_field;
			var level = $t.p.treeReader.level_field;
			row.level = rd[level];
			
			if($t.p.treeGridModel == 'nested') {
				row.lft = rd[$t.p.treeReader.left_field];
				row.rgt = rd[$t.p.treeReader.right_field];
				if(!rd[isLeaf]) {
				// NS Model
					rd[isLeaf] = (parseInt(row.rgt,10) === parseInt(row.lft,10)+1) ? 'true' : 'false';
				}
			} else {
				row.parent_id = rd[$t.p.treeReader.parent_id_field];
			}
			var curLevel = parseInt(row.level,10), ident,lftpos;
			if($t.p.tree_root_level === 0) {
				ident = curLevel+1;
				lftpos = curLevel;
			} else {
				ident = curLevel;
				lftpos = curLevel -1;
			}
			var twrap = "<div class='tree-wrap' style='width:"+(ident*18)+"px;'>";
			twrap += "<div style='left:"+(lftpos*18)+"px;' class='ui-icon ";

			if(rd[isLeaf] == "true" || rd[isLeaf] ==true) {
				twrap += $t.p.treeIcons.leaf+" tree-leaf'";
				row.isLeaf = true;
			} else {
				if(rd[expanded] == "true" || rd[expanded] == true) {
					twrap += $t.p.treeIcons.minus+" tree-minus treeclick'";
					row.expanded = true;
				} else {
					twrap += $t.p.treeIcons.plus+" tree-plus treeclick'";
					row.expanded = false;
				}
				row.isLeaf = false;
			}
			twrap += "</div></div>";
			if(parseInt(rd[level],10) !== parseInt($t.p.tree_root_level,10)) {                
				if(!$($t).isVisibleNode(row)){ 
					$(row).css("display","none");
				}
			}
			$("td:eq("+expCol+")",row).wrapInner("<span></span>").prepend(twrap);
			$(".treeclick",row).bind("click",function(e){
				var target = e.target || e.srcElement;
				var ind =$(target,$t.rows).parents("tr.jqgrow")[0].rowIndex;
				if(!$t.rows[ind].isLeaf){
					if($t.rows[ind].expanded){
						$($t).collapseRow($t.rows[ind]);
						$($t).collapseNode($t.rows[ind]);
					} else {
						$($t).expandRow($t.rows[ind]);
						$($t).expandNode($t.rows[ind]);
					}
				}
				return false;
			});
			if($t.p.ExpandColClick === true) {
			$("span", row).css("cursor","pointer").bind("click",function(e){
				var target = e.target || e.srcElement;
				var ind =$(target,$t.rows).parents("tr.jqgrow")[0].rowIndex;
				if(!$t.rows[ind].isLeaf){
					if($t.rows[ind].expanded){
						$($t).collapseRow($t.rows[ind]);
						$($t).collapseNode($t.rows[ind]);
					} else {
						$($t).expandRow($t.rows[ind]);
						$($t).expandNode($t.rows[ind]);
					}
				}
				$($t).setSelection($t.rows[ind].id);
				return false;
			});
			}
		});
	},
	setTreeGrid : function() {
		return this.each(function (){
			var $t = this, i=0;
			if(!$t.p.treeGrid) { return; }
			if(!$t.p.treedatatype ) $.extend($t.p,{treedatatype: $t.p.datatype});
			$t.p.subGrid = false; $t.p.altRows =false;
			$t.p.pgbuttons = false; $t.p.pginput = false;
			$t.p.multiselect = false; $t.p.rowList = [];
			$t.p.treeIcons = $.extend({plus:'ui-icon-triangle-1-e',minus:'ui-icon-triangle-1-s',leaf:'ui-icon-radio-off'},$t.p.treeIcons || {});
			if($t.p.treeGridModel == 'nested') {
				$t.p.treeReader = $.extend({
					level_field: "level",
					left_field:"lft",
					right_field: "rgt",
					leaf_field: "isLeaf",
					expanded_field: "expanded"
				},$t.p.treeReader);
			} else
				if($t.p.treeGridModel == 'adjacency') {
				$t.p.treeReader = $.extend({
						level_field: "level",
						parent_id_field: "parent",
						leaf_field: "isLeaf",
						expanded_field: "expanded"
				},$t.p.treeReader );
			}
			for (var key in $t.p.colModel){
				if($t.p.colModel[key].name == $t.p.ExpandColumn) {
					$t.p.expColInd = i;
					break;
				}
				i++;
			}
			if(!$t.p.expColInd) $t.p.expColInd =0;
			$.each($t.p.treeReader,function(i,n){
				if(n){
					$t.p.colNames.push(n);
					$t.p.colModel.push({name:n,width:1,hidden:true,sortable:false,resizable:false,hidedlg:true,editable:true,search:false});
				}
			});			
		});
	},
	expandRow: function (record){
		this.each(function(){
			var $t = this;
			if(!$t.grid || !$t.p.treeGrid) { return; }
			var childern = $($t).getNodeChildren(record);
			//if ($($t).isVisibleNode(record)) {
			$(childern).each(function(i){
				$(this).css("display","");
				if(this.expanded) {
					$($t).expandRow(this);
				}
			});
			//}
		});
	},
	collapseRow : function (record) {
		this.each(function(){
			var $t = this;
			if(!$t.grid || !$t.p.treeGrid) { return; }
			var childern = $($t).getNodeChildren(record);
			$(childern).each(function(i){
				$(this).css("display","none");
				$($t).collapseRow(this);
			});
		});
	},
	// NS ,adjacency models
	getRootNodes : function() {
		var result = [];
		this.each(function(){
			var $t = this;
			if(!$t.grid || !$t.p.treeGrid) { return; }
			switch ($t.p.treeGridModel) {
				case 'nested' :
					var level = $t.p.treeReader.level_field;
					$($t.rows).each(function(i){
						if(parseInt(this[level],10) === parseInt($t.p.tree_root_level,10)) {
							result.push(this);
						}
					});
					break;
				case 'adjacency' :
					$($t.rows).each(function(i){
						if(this.parent_id.toLowerCase() == "null") {
							result.push(this);
						}
					});
					break;
			}
		});
		return result;
	},
	getNodeDepth : function(rc) {
		var ret = null;
		this.each(function(){
			var $t = this;
			if(!this.grid || !this.p.treeGrid) { return; }
			switch ($t.p.treeGridModel) {
				case 'nested' :
					ret = parseInt(rc.level,10) - parseInt(this.p.tree_root_level,10);
					break;
				case 'adjacency' :
					ret = $($t).getNodeAncestors(rc).length;
					break;
			}
		});
		return ret;
	},
	getNodeParent : function(rc) {
		var result = null;
		this.each(function(){
			var $t = this;
			if(!$t.grid || !$t.p.treeGrid) { return; }
			switch ($t.p.treeGridModel) {
				case 'nested' :
					var lft = parseInt(rc.lft,10), rgt = parseInt(rc.rgt,10), level = parseInt(rc.level,10);
					$(this.rows).each(function(){
						if(parseInt(this.level,10) === level-1 && parseInt(this.lft) < lft && parseInt(this.rgt) > rgt) {
							result = this;
							return false;
						}
					});
					break;
				case 'adjacency' :
					$(this.rows).each(function(){
						if(this.id == rc.parent_id ) {
							result = this;
							return false;
						}
					});
					break;
			}
		});
		return result;
	},
	getNodeChildren : function(rc) {
		var result = [];
		this.each(function(){
			var $t = this;
			if(!$t.grid || !$t.p.treeGrid) { return; }
			switch ($t.p.treeGridModel) {
				case 'nested' :
					var lft = parseInt(rc.lft,10), rgt = parseInt(rc.rgt,10), level = parseInt(rc.level,10);
					$(this.rows).slice(1).each(function(i){
						if(parseInt(this.level,10) === level+1 && parseInt(this.lft,10) > lft && parseInt(this.rgt,10) < rgt) {
							result.push(this);
						}
					});
					break;
				case 'adjacency' :
					$(this.rows).slice(1).each(function(i){
						if(this.parent_id == rc.id) {
							result.push(this);
						}
					});
					break;
			}
		});
		return result;
	},
	getFullTreeNode : function(rc) {
		var result = [];
		this.each(function(){
			var $t = this;
			if(!$t.grid || !$t.p.treeGrid) { return; }
			switch ($t.p.treeGridModel) {
				case 'nested' :
					var lft = parseInt(rc.lft,10), rgt = parseInt(rc.rgt,10), level = parseInt(rc.level,10);
					$(this.rows).each(function(i){
						if(parseInt(this.level,10) >= level && parseInt(this.lft,10) >= lft && parseInt(this.lft,10) <= rgt) {
							result.push(this);
						}
					});
					break;
				case 'adjacency' :
					break;
			}
		});
		return result;
	},	
	// End NS, adjacency Model
	getNodeAncestors : function(rc) {
		var ancestors = [];
		this.each(function(){
			if(!this.grid || !this.p.treeGrid) { return; }
			var parent = $(this).getNodeParent(rc);
			while (parent) {
				ancestors.push(parent);
				parent = $(this).getNodeParent(parent);	
			}
		});
		return ancestors;
	},
	isVisibleNode : function(rc) {
		var result = true;
		this.each(function(){
			var $t = this;
			if(!$t.grid || !$t.p.treeGrid) { return; }
			var ancestors = $($t).getNodeAncestors(rc);
			$(ancestors).each(function(){
				result = result && this.expanded;
				if(!result) {return false;}
			});
		});
		return result;
	},
	isNodeLoaded : function(rc) {
		var result;
		this.each(function(){
			var $t = this;
			if(!$t.grid || !$t.p.treeGrid) { return; }
			if(rc.loaded !== undefined) {
				result = rc.loaded;
			} else if( rc.isLeaf || $($t).getNodeChildren(rc).length > 0){
				result = true;
			} else {
				result = false;
			}
		});
		return result;
	},
	expandNode : function(rc) {
		return this.each(function(){
			if(!this.grid || !this.p.treeGrid) { return; }
			if(!rc.expanded) {
				if( $(this).isNodeLoaded(rc) ) {
					rc.expanded = true;
					$("div.treeclick",rc).removeClass(this.p.treeIcons.plus+" tree-plus").addClass(this.p.treeIcons.minus+" tree-minus");
				} else {
					rc.expanded = true;
					$("div.treeclick",rc).removeClass(this.p.treeIcons.plus+" tree-plus").addClass(this.p.treeIcons.minus+" tree-minus");
					this.p.treeANode = rc.rowIndex;
					this.p.datatype = this.p.treedatatype;
					if(this.p.treeGridModel == 'nested') {
						$(this).setGridParam({postData:{nodeid:rc.id,n_left:rc.lft,n_right:rc.rgt,n_level:rc.level}});
					} else {
						$(this).setGridParam({postData:{nodeid:rc.id,parentid:rc.parent_id,n_level:rc.level}});
					}
					$(this).trigger("reloadGrid");
					if(this.p.treeGridModel == 'nested') {
						$(this).setGridParam({postData:{nodeid:'',n_left:'',n_right:'',n_level:''}});
					} else {
						$(this).setGridParam({postData:{nodeid:'',parentid:'',n_level:''}}); 
					}
				}
			}
		});
	},
	collapseNode : function(rc) {
		return this.each(function(){
			if(!this.grid || !this.p.treeGrid) { return; }
			if(rc.expanded) {
				rc.expanded = false;
				$("div.treeclick",rc).removeClass(this.p.treeIcons.minus+" tree-minus").addClass(this.p.treeIcons.plus+" tree-plus");
			}
		});
	},
	SortTree : function( newDir) {
		return this.each(function(){
			if(!this.grid || !this.p.treeGrid) { return; }
			var i, len,
			rec, records = [], $t = this,
			roots = $(this).getRootNodes();
			// Sorting roots
			roots.sort(function(a, b) {
				if (a.sortKey < b.sortKey) {return -newDir;}
				if (a.sortKey > b.sortKey) {return newDir;}
				return 0;
			});
			if(roots[0]){
				$("td",roots[0]).each( function( k ) {
					$(this).css("width",$t.grid.headers[k].width+"px");
				});
				$t.grid.cols = roots[0].cells;
			}
			// Sorting children
			for (i = 0, len = roots.length; i < len; i++) {
				rec = roots[i];
				records.push(rec);
				$(this).collectChildrenSortTree(records, rec, newDir);
			}
			$.each(records, function(index, row) {
				$('tbody',$t.grid.bDiv).append(row);
				row.sortKey = null;
			});
		});
	},
	collectChildrenSortTree : function(records, rec, newDir) {
		return this.each(function(){
			if(!this.grid || !this.p.treeGrid) { return; }
			var i, len,
			child, 
			children = $(this).getNodeChildren(rec);
			children.sort(function(a, b) {
				if (a.sortKey < b.sortKey) {return -newDir;}
				if (a.sortKey > b.sortKey) {return newDir;}
				return 0;
			});
			for (i = 0, len = children.length; i < len; i++) {
				child = children[i];
				records.push(child);
				$(this).collectChildrenSortTree(records, child,newDir); 
			}
		});
	},
	// experimental 
	setTreeRow : function(rowid, data) {
		var nm, success=false;
		this.each(function(){
			var t = this;
			if(!t.grid || !t.p.treeGrid) { return; }
			success = $(t).setRowData(rowid,data);
		});
		return success;
	},
	delTreeNode : function (rowid) {
		return this.each(function () {
			var $t = this;
			if(!$t.grid || !$t.p.treeGrid) { return; }
			var rc = $($t).getInd(rowid,true);
			if (rc) {
				var dr = $($t).getNodeChildren(rc);
				if(dr.length>0){
					for (var i=0;i<dr.length;i++){
						$($t).delRowData(dr[i].id);
					}
				}
				$($t).delRowData(rc.id);
			}
		});
	},
	/**
         * 根据传入的数据进行父子节点的匹配（顶级节点的父节点默认为“-1”）
         * @param data 树型数据
         * @return data 父子节点匹配好的树型数据
         */
        initTreeData:function(data,name) {
         var orderedData = [];
             var reData = [];
        this.each(function(){
            
        	if(data && data.length > 0){
        		var l = data.length;
        		
        		for(var i=0; i<l; i++){
        			orderedData['' + data[i][name]] = i;
        			data[i].ChildNodes = [];
        		}
        		var pid;
        		for(var i=0; i<l; i++){
        			pid = data[i].parent;
        			if(pid != ''){
        			//	data[i].parent = data[orderedData['' + pid]];//设置父节点
        				
        			    data[orderedData['' + pid]].ChildNodes.push(data[i]);
        			    data[orderedData['' + pid]].hasChildren = true;
        			}
        		}
        		orderedData.length = 0;
        		for(var i=0; i<l; i++){
        			pid = data[i].parent;
        			if(pid == ''){
        				orderedData.push(data[i]);
        			}
        		}
        		
        		reData = $(this).buildIdPathMap('',reData,orderedData,name);//构造节点Id和Path的Map数组，用与快速节点查询
        	}
        
        	
        
        });
        	return reData;
        },
        
        /**
         * 构造节点Id和Path的Map数组，用与快速节点查询
         * @param orderedData -构造好的树节点数据
         * @param path -树节点路径
         * @return
         */
       buildIdPathMap:function(leval,reData,orderedData,name){
       this.each(function(){
        	var l = orderedData.length;
        	var data;
        	//var tempPath;
        	if(leval === '')
        	{
        	  leval = 0; 
        	}else
        	{
        	  leval = leval + 1;
        	}
        	for(var i=0; i<l; i++){
        		data = orderedData[i];
        		//tempPath = path === "" ? i : path + "." + i;
        		//data.path = tempPath;
        		//idPathMap['' + data[name]] = data.path;
        	//	reData.push(orderedData[i]);
        		if(data.ChildNodes.length == 0)
        		{
        		   data.isLeaf = true;
        		}else
        		{
        		   data.isLeaf = false;
        		}
        		data.level = leval;
        		if(data.expanded === undefined)
        		{
        		   data.expanded = true;
        		}
        		reData.push(data);
        		if(data.ChildNodes && data.ChildNodes.length > 0){
        			$(this).buildIdPathMap(leval,reData,data.ChildNodes,name);
        		}
        	}
        
        	});
        		return reData;
        }
	
});
})(jQuery);