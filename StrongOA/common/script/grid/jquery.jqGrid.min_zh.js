
/* 
* jqGrid  3.6.1 - jQuery Grid 
* Copyright (c) 2008, Tony Tomov, tony@trirand.com 
* Dual licensed under the MIT and GPL licenses 
* http://www.opensource.org/licenses/mit-license.php 
* http://www.gnu.org/licenses/gpl.html 
* Date:2009-11-23 
* Modules: grid.base.js; jquery.fmatter.js; grid.custom.js; grid.common.js; grid.formedit.js; jquery.searchFilter.js; grid.inlinedit.js; grid.celledit.js; jqModal.js; jqDnR.js; grid.subgrid.js; grid.treegrid.js; grid.import.js; JsonXml.js; grid.setcolumns.js; grid.postext.js; grid.tbltogrid.js; grid.jqueryui.js; 
*/
(function ($) {
	$.jgrid = $.jgrid || {};
	$.extend($.jgrid, {htmlDecode:function (value) {
		if (value == "&nbsp;" || value == "&#160;" || (value.length == 1 && value.charCodeAt(0) == 160)) {
			return "";
		}
		return !value ? value : String(value).replace(/&amp;/g, "&").replace(/&gt;/g, ">").replace(/&lt;/g, "<").replace(/&quot;/g, "\"");
	}, htmlEncode:function (value) {
		return !value ? value : String(value).replace(/&/g, "&amp;").replace(/>/g, "&gt;").replace(/</g, "&lt;").replace(/\"/g, "&quot;");
	}, format:function (format) {
		var args = $.makeArray(arguments).slice(1);
		if (format === undefined) {
			format = "";
		}
		return format.replace(/\{(\d+)\}/g, function (m, i) {
			return args[i];
		});
	}, getCellIndex:function (cell) {
		cell = $(cell);
		cell = (!cell.is("td") && !cell.is("th") ? cell.closest("td,th") : cell)[0];
		if ($.browser.msie) {
			return $.inArray(cell, cell.parentNode.cells);
		}
		return cell.cellIndex;
	}, stripHtml:function (v) {
		v = v + "";
		var regexp = /<("[^"]*"|'[^']*'|[^'">])*>/gi;
		if (v) {
			return v.replace(regexp, "");
		} else {
			return v;
		}
	}, stringToDoc:function (xmlString) {
		var xmlDoc;
		if (typeof xmlString !== "string") {
			return xmlString;
		}
		try {
			var parser = new DOMParser();
			xmlDoc = parser.parseFromString(xmlString, "text/xml");
		}
		catch (e) {
			xmlDoc = new ActiveXObject("Microsoft.XMLDOM");
			xmlDoc.async = false;
			xmlDoc.loadXML(xmlString);
		}
		return (xmlDoc && xmlDoc.documentElement && xmlDoc.documentElement.tagName != "parsererror") ? xmlDoc : null;
	}, parse:function (jsonString) {
		var js = jsonString, msg;
		if (js.substr(0, 9) == "while(1);") {
			js = js.substr(9);
		}
		if (js.substr(0, 2) == "/*") {
			js = js.substr(2, js.length - 4);
		}
		if (!js) {
			js = "{}";
		}
		($.jgrid.useJSON === true && typeof (JSON) === "object" && typeof (JSON.parse) === "function") ? msg = JSON.parse(js) : msg = eval("(" + js + ")");
		return msg.hasOwnProperty("d") ? msg.d : msg;
	}, empty:function () {
		while (this.firstChild) {
			this.removeChild(this.firstChild);
		}
	}, jqID:function (sid) {
		sid = sid + "";
		return sid.replace(/([\.\:\[\]])/g, "\\$1");
	}, ajaxOptions:{}, extend:function (methods) {
		$.extend($.fn.jqGrid, methods);
		if (!this.no_legacy_api) {
			$.fn.extend(methods);
		}
	}});
	$.fn.jqGrid = function (pin) {
		if (typeof pin == "string") {
			var fn = $.fn.jqGrid[pin];
			if (!fn) {
				throw ("jqGrid - No such method: " + pin);
			}
			var args = $.makeArray(arguments).slice(1);
			return fn.apply(this, args);
		}
		return this.each(function () {
			if (this.grid) {
				return;
			}
			var p = $.extend(true, {url:"",
			 height:150,
			  page:1, 
			  rowNum:20, 
			  records:0, 
			  pager:"",
			  pgbuttons:true, 
			  pginput:true, 
			  colModel:[], 
			  rowList:[], 
			  colNames:[],
			  sortorder:"asc", 
			  sortname:"",
			  datatype:"xml", 
			  mtype:"GET",
			  totleCol:[],//添加合计列属性
			  radioselect:false, //添加单选框属性
			  selectInfo:null,//添加已选择属性 
			  orderType:"server",//添加页内排序属性
			  isTips:true, //添加是否显示提示框属性
			  isFixRum:false,//添加拖动时是否固定行号属性
			  isFixMilt:false,//添加拖动时是否固定多选框
			  isFixRadio:false,//添加拖动时是否固定单选框
			  tipsClass:"pretty", //提示框的样式
			  altRows:false, 
			  selarrrow:[], 
			  savedRow:[], 
			  shrinkToFit:true, 
			  xmlReader:{}, 
			  jsonReader:{}, 
			  subGrid:false, 
			  subGridModel:[], 
			  reccount:0, 
			  lastpage:0, 
			  lastsort:0, 
			  selrow:null, 
			  beforeSelectRow:null, 
			  onSelectRow:null, 
			  onSortCol:null, 
			  ondblClickRow:null, 
			  onRightClickRow:null, 
			  onPaging:null, 
			  onSelectAll:null, 
			  loadComplete:null, 
			  gridComplete:null, 
			  loadError:null, 
			  loadBeforeSend:null, 
			  afterInsertRow:null, 
			  beforeRequest:null, 
			  onHeaderClick:null, 
			  viewrecords:false, 
			  loadonce:false, 
			  multiselect:false,
			   multikey:false, 
			   editurl:null, 
			   search:false, 
			   caption:"", 
			   hidegrid:true, 
			   hiddengrid:false, 
			   postData:{}, 
			   userData:{}, 
			   treeGrid:false, 
			   treeGridModel:"nested", 
			   treeReader:{}, 
			   treeANode:-1, 
			   ExpandColumn:null, 
			   tree_root_level:0, 
			   prmNames:{page:"page", rows:"rows", sort:"sidx", order:"sord", search:"_search", nd:"nd"}, 
			   forceFit:false, 
			   gridstate:"visible", 
			   cellEdit:false, 
			   cellsubmit:"remote", 
			   nv:0, 
			   loadui:"enable", 
			   toolbar:[false, ""], 
			   scroll:false, 
			   multiboxonly:false, 
			   deselectAfterSort:true, 
			   scrollrows:false, 
			   autowidth:false, 
			   scrollOffset:18, 
			   cellLayout:5, 
			   subGridWidth:20, 
			   multiselectWidth:20, 
			   gridview:false, 
			   rownumWidth:25, 
			   rownumbers:false, 
			   pagerpos:"right", 
			   recordpos:"center", 
			   footerrow:false, 
			   userDataOnFooter:false, 
			   hoverrows:true, 
			   altclass:"ui-priority-secondary", 
			   viewsortcols:[false, "vertical", true], 
			   resizeclass:"", 
			   autoencode:false, 
			   remapColumns:[], 
			   ajaxGridOptions:{}, 
			   direction:"ltr"}, $.jgrid.defaults, pin || {});
			var grid = {headers:[], cols:[], footers:[], dragStart:function (i, x, y) {
				this.resizing = {idx:i, startX:x.clientX, sOL:y[0]};
				this.hDiv.style.cursor = "col-resize";
				this.curGbox = $("#rs_m" + p.id, "#gbox_" + p.id);
				this.curGbox.css({display:"block", left:y[0], top:y[1], height:y[2]});
				if ($.isFunction(p.resizeStart)) {
					p.resizeStart.call(this, x, i);
				}
				document.onselectstart = new Function("return false");
			}, dragMove:function (x) {
				if (this.resizing) {
					var diff = x.clientX - this.resizing.startX, h = this.headers[this.resizing.idx], newWidth = p.direction === "ltr" ? h.width + diff : h.width - diff, hn, nWn;
					if (newWidth > 33) {
						this.curGbox.css({left:this.resizing.sOL + diff});
						if (p.forceFit === true) {
							hn = this.headers[this.resizing.idx + p.nv];
							nWn = p.direction === "ltr" ? hn.width - diff : hn.width + diff;
							if (nWn > 33) {
								h.newWidth = newWidth;
								hn.newWidth = nWn;
							}
						} else {
							this.newWidth = p.direction === "ltr" ? p.tblwidth + diff : p.tblwidth - diff;
							h.newWidth = newWidth;
						}
					}
				}
			}, dragEnd:function () {
				this.hDiv.style.cursor = "default";
				if (this.resizing) {
					var idx = this.resizing.idx, nw = this.headers[idx].newWidth || this.headers[idx].width;
					nw = parseInt(nw);
					this.resizing = false;
					$("#rs_m" + p.id).css("display", "none");
					p.colModel[idx].width = nw;
					this.headers[idx].width = nw;
					this.headers[idx].el.style.width = nw + "px";
					if (this.cols.length > 0) {
						this.cols[idx].style.width = nw + "px";
					}
					if (this.footers.length > 0) {
						this.footers[idx].style.width = nw + "px";
					}
					if (p.forceFit === true) {
						nw = this.headers[idx + p.nv].newWidth || this.headers[idx + p.nv].width;
						this.headers[idx + p.nv].width = nw;
						this.headers[idx + p.nv].el.style.width = nw + "px";
						if (this.cols.length > 0) {
							this.cols[idx + p.nv].style.width = nw + "px";
						}
						if (this.footers.length > 0) {
							this.footers[idx + p.nv].style.width = nw + "px";
						}
						p.colModel[idx + p.nv].width = nw;
					} else {
						p.tblwidth = this.newWidth || p.tblwidth;
						$("table:first", this.bDiv).css("width", p.tblwidth + "px");
						$("table:first", this.hDiv).css("width", p.tblwidth + "px");
						this.hDiv.scrollLeft = this.bDiv.scrollLeft;
						if (p.footerrow) {
							$("table:first", this.sDiv).css("width", p.tblwidth + "px");
							this.sDiv.scrollLeft = this.bDiv.scrollLeft;
						}
					}
					if ($.isFunction(p.resizeStop)) {
						p.resizeStop.call(this, nw, idx);
					}
				}
				this.curGbox = null;
				document.onselectstart = new Function("return true");
			}, populateVisible:function () {
				if (grid.timer) {
					clearTimeout(grid.timer);
				}
				grid.timer = null;
				var dh = $(grid.bDiv).height();
				if (!dh) {
					return;
				}
				var table = $("table:first", grid.bDiv);
				var rows = $("> tbody > tr:visible:first", table);
				var rh = rows.outerHeight() || grid.prevRowHeight;
				if (!rh) {
					return;
				}
				grid.prevRowHeight = rh;
				var rn = p.rowNum;
				if (rn < 10) {
					rn = parseInt(dh / rh) + 1 << 1;
					if (rn < 10) {
						rn = 10;
					}
					p.rowNum = rn;
				}
				var scrollTop = grid.scrollTop = grid.bDiv.scrollTop;
				var ttop = Math.round(table.position().top) - scrollTop;
				var tbot = ttop + table.height();
				var div = rh * rn;
				var page, npage, empty;
				if (ttop <= 0 && tbot < dh && (p.lastpage == null || parseInt((tbot + scrollTop + div - 1) / div) < p.lastpage)) {
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
			}, scrollGrid:function () {
				if (p.scroll) {
					var scrollTop = grid.bDiv.scrollTop;
					if (scrollTop != grid.scrollTop) {
						grid.scrollTop = scrollTop;
						if (grid.timer) {
							clearTimeout(grid.timer);
						}
						grid.timer = setTimeout(grid.populateVisible, 200);
					}
				}
				grid.hDiv.scrollLeft = grid.bDiv.scrollLeft;
				if (p.footerrow) {
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
			
			}, selectionPreserver:function (ts) {
				var p = ts.p;
				var sr = p.selrow, sra = p.selarrrow ? $.makeArray(p.selarrrow) : null;
				var left = ts.grid.bDiv.scrollLeft;
				var complete = p.gridComplete;
				p.gridComplete = function () {
					p.selrow = null;
					p.selarrrow = [];
					if (p.multiselect && sra && sra.length > 0) {
						for (var i = 0; i < sra.length; i++) {
							if (sra[i] != sr) {
								$(ts).jqGrid("setSelection", sra[i], false);
							}
						}
					}
					if (sr) {
						$(ts).jqGrid("setSelection", sr, false);
					}
					ts.grid.bDiv.scrollLeft = left;
					if (p.gridComplete = complete) {
						complete();
					}
				};
			}};
			this.p = p;
			var i, dir, ts;
			if (this.p.colNames.length === 0) {
				for (i = 0; i < this.p.colModel.length; i++) {
					this.p.colNames[i] = this.p.colModel[i].label || this.p.colModel[i].name;
				}
			}
			if (this.p.colNames.length !== this.p.colModel.length) {
				alert($.jgrid.errors.model);
				return;
			}
			var gv = $("<div class='ui-jqgrid-view'></div>"), ii, isMSIE = $.browser.msie ? true : false, isSafari = $.browser.safari ? true : false;
			ts = this;
			ts.p.direction = $.trim(ts.p.direction.toLowerCase());
			if ($.inArray(ts.p.direction, ["ltr", "rtl"]) == -1) {
				ts.p.direction = "ltr";
			}
			dir = ts.p.direction;
			$(gv).insertBefore(this);
			$(this).appendTo(gv).removeClass("scroll");
			var eg = $("<div class='ui-jqgrid ui-widget ui-widget-content ui-corner-all'></div>");
			$(eg).insertBefore(gv).attr({id:"gbox_" + this.id, dir:dir});
			$(gv).appendTo(eg).attr("id", "gview_" + this.id);
			if (isMSIE && $.browser.version <= 6) {
				ii = "<iframe style=\"display:block;position:absolute;z-index:-1;filter:Alpha(Opacity='0');\" src=\"javascript:false;\"></iframe>";
			} else {
				ii = "";
			}
			$("<div class='ui-widget-overlay jqgrid-overlay' id='lui_" + this.id + "'></div>").append(ii).insertBefore(gv);
			$("<div class='loading' id='load_" + this.id + "'>" + this.p.loadtext + "</div>").insertBefore(gv);
			$(this).attr({cellSpacing:"0", cellPadding:"0", border:"0", role:"grid", "aria-multiselectable":!!this.p.multiselect, "aria-labelledby":"gbox_" + this.id});
			var sortkeys = ["shiftKey", "altKey", "ctrlKey"], IntNum = function (val, defval) {
				val = parseInt(val, 10);
				if (isNaN(val)) {
					return defval ? defval : 0;
				} else {
					return val;
				}
			}, formatCol = function (pos, rowInd) {
				var ral = ts.p.colModel[pos].align, result = "style=\"", clas = ts.p.colModel[pos].classes;
				if (ral) {
					result += "text-align:" + ral + ";";
				}
				if (ts.p.colModel[pos].hidden === true) {
					result += "display:none;";
				}
				if (rowInd === 0) {
					result += "width: " + grid.headers[pos].width + "px;";
				}
				return result + "\"" + (clas !== undefined ? (" class=\"" + clas + "\"") : "");
			}, addCell = function (rowId, cell, pos, irow, srvr) {
				var v, prp;
				var cm = ts.p.colModel[pos];//修改value
				v = formatter(rowId, cell, pos, srvr, "add");
				prp = formatCol(pos, irow);
				if(cm.formatter == "date")
				{
				    cell = v;
				}
				return "<td  value = '"+cell+"'role=\"gridcell\" " + prp + " tips=\"" + $.jgrid.stripHtml(v) + "\">" + v + "</td>";//添加一个value属性
			}, formatter = function (rowId, cellval, colpos, rwdat, _act) {
				var cm = ts.p.colModel[colpos], v;
				if (typeof cm.formatter !== "undefined") {
					var opts = {rowId:rowId, colModel:cm};
					if ($.isFunction(cm.formatter)) {
						v = cm.formatter(cellval, opts, rwdat, _act);
					} else {
						if ($.fmatter) {
							v = $.fn.fmatter(cm.formatter, cellval, opts, rwdat, _act);
						} else {
							v = cellVal(cellval);
						}
					}
				} else {
					v = cellVal(cellval);
				}
				return v;
			}, cellVal = function (val) {
				return val === undefined || val === null || val === "" ? "&#160;" : ts.p.autoencode ? $.jgrid.htmlEncode(val + "") : val + "";
			}, addMulti = function (rowid, pos, irow) {
				var v = "<input type=\"checkbox\" id=\"jqg_" + rowid + "\" class=\"cbox\" name=\"jqg_" + rowid + "\"/>", prp = formatCol(pos, irow);
				return "<td  role='gridcell' " + prp + ">" + v + "</td>";
			}, 
			
			 addRadio = function (rowid, pos, irow) {//添加单选框
				var v = "<input type=\"radio\" id=\"jqg_" + rowid + "\" class=\"rbox\" name='jqg_' />", prp = formatCol(pos, irow);
				return "<td role='gridcell' " + prp + ">" + v + "</td>";
			},addRowNum = function (pos, irow, pG, rN) {
				var v = (parseInt(pG) - 1) * parseInt(rN) + 1 + irow, prp = formatCol(pos, irow);
				return "<td role=\"gridcell\" class=\"ui-state-default jqgrid-rownum\" " + prp + ">" + v + "</td>";
			}, reader = function (datatype) {
				var field, f = [], j = 0, i;
				for (i = 0; i < ts.p.colModel.length; i++) {
					field = ts.p.colModel[i];
					if (field.name !== "cb" && field.name !== "subgrid" && field.name !== "rn" && field.name!=='rb') {
						f[j] = (datatype == "xml") ? field.xmlmap || field.name : field.jsonmap || field.name;
						j++;
					}
				}
				return f;
			}, orderedCols = function (offset) {
				var order = ts.p.remapColumns;
				if (!order || !order.length) {
					order = $.map(ts.p.colModel, function (v, i) {
						return i;
					});
				}
				if (offset) {
					order = $.map(order, function (v) {
						return v < offset ? null : v - offset;
					});
				}
				return order;
			}, emptyRows = function (parent, scroll) {
				var tBody = $("tbody:first", parent);
				if (!ts.p.gridview || ts.p.jqgdnd) {
					$("*", tBody).children().unbind();
				}
				if (isMSIE) {
					$.jgrid.empty.apply(tBody[0]);
				} else {
					tBody[0].innerHTML = "";
				}
				if (scroll && ts.p.scroll) {
					$(">div:first", parent).css({height:"auto"}).children("div:first").css({height:0, display:"none"});
					parent.scrollTop = 0;
				}
				tBody = null;
			}, addXmlData = function (xml, t, rcnt, more, adjust) {
				var startReq = new Date();
				ts.p.reccount = 0;
				if ($.isXMLDoc(xml)) {
					if (ts.p.treeANode === -1 && !ts.p.scroll) {
						emptyRows(t);
						rcnt = 0;
					} else {
						rcnt = rcnt > 0 ? rcnt : 0;
					}
				} else {
					return;
				}
				var i, fpos, ir = 0, v, row, gi = 0, si = 0, ni = 0, idn, getId, f = [], F, rd = {}, rl = ts.rows.length, xmlr, rid, rowData = [], ari = 0, cn = (ts.p.altRows === true) ? " " + ts.p.altclass : "", cn1;
				if (!ts.p.xmlReader.repeatitems) {
					f = reader("xml");
				}
				if (ts.p.keyIndex === false) {
					idn = ts.p.xmlReader.id;
				} else {
					idn = ts.p.keyIndex;
				}
				if (f.length > 0 && !isNaN(idn)) {
					if (ts.p.remapColumns && ts.p.remapColumns.length) {
						idn = $.inArray(idn, ts.p.remapColumns);
					}
					idn = f[idn];
				}
				if ((idn + "").indexOf("[") === -1) {
					if (f.length) {
						getId = function (trow, k) {
							return $(idn, trow).text() || k;
						};
					} else {
						getId = function (trow, k) {
							return $(ts.p.xmlReader.cell, trow).eq(idn).text() || k;
						};
					}
				} else {
					getId = function (trow, k) {
						return trow.getAttribute(idn.replace(/[\[\]]/g, "")) || k;
					};
				}
				$(ts.p.xmlReader.page, xml).each(function () {
					ts.p.page = this.textContent || this.text || 1;
				});
				$(ts.p.xmlReader.total, xml).each(function () {
					ts.p.lastpage = this.textContent || this.text || 1;
				});
				$(ts.p.xmlReader.records, xml).each(function () {
					ts.p.records = this.textContent || this.text || 0;
				});
				$(ts.p.xmlReader.userdata, xml).each(function () {
					ts.p.userData[this.getAttribute("name")] = this.textContent || this.text;
				});
				var gxml = $(ts.p.xmlReader.root + " " + ts.p.xmlReader.row, xml), gl = gxml.length, j = 0;
				if (gxml && gl) {
					var rn = parseInt(ts.p.rowNum), br = ts.p.scroll ? (parseInt(ts.p.page) - 1) * rn + 1 : 1;
					if (adjust) {
						rn *= adjust + 1;
					}
					var afterInsRow = $.isFunction(ts.p.afterInsertRow);
					while (j < gl) {
						xmlr = gxml[j];
						rid = getId(xmlr, br + j);
						cn1 = j % 2 == 1 ? cn : "";
						rowData[ari++] = "<tr id=\"" + rid + "\" saveType = 'edit' role=\"row\" class =\"ui-widget-content jqgrow ui-row-" + ts.p.direction + "" + cn1 + "\">";
						if (ts.p.rownumbers === true) {
							rowData[ari++] = addRowNum(0, j, ts.p.page, ts.p.rowNum);
							ni = 1;
						}
						if (ts.p.multiselect === true) {
							rowData[ari++] = addMulti(rid, ni, j);
							gi = 1;
						}
						if(ts.p.radioselect === true)
						{
						    rowData[ari++] = addRadio(rid,ni,j); 
						    gi = 1;
						}
						if (ts.p.subGrid === true) {
							rowData[ari++] = $(ts).jqGrid("addSubGridCell", gi + ni, j + rcnt);
							si = 1;
						}
						if (ts.p.xmlReader.repeatitems) {
							if (!F) {
								F = orderedCols(gi + si + ni);
							}
							var cells = $(ts.p.xmlReader.cell, xmlr);
							$.each(F, function (k) {
								var cell = cells[this];
								if (!cell) {
									return false;
								}
								v = cell.textContent || cell.text;
								rd[ts.p.colModel[k + gi + si + ni].name] = v;
								rowData[ari++] = addCell(rid, v, k + gi + si + ni, j + rcnt, xmlr);
							});
						} else {
							for (i = 0; i < f.length; i++) {
								v = $(f[i], xmlr).text();
								rd[ts.p.colModel[i + gi + si + ni].name] = v;
								rowData[ari++] = addCell(rid, v, i + gi + si + ni, j + rcnt, xmlr);
							}
						}
						rowData[ari++] = "</tr>";
						if (ts.p.gridview === false) {
							if (ts.p.treeGrid === true) {
								fpos = ts.p.treeANode >= -1 ? ts.p.treeANode : 0;
								row = $(rowData.join(""))[0];
								try {
									$(ts).jqGrid("setTreeNode", rd, row);
								}
								catch (e) {
								}
								rl === 0 ? $("tbody:first", t).append(row) : $(ts.rows[j + fpos + rcnt]).after(row);
							} else {
								$("tbody:first", t).append(rowData.join(""));
							}
							if (ts.p.subGrid === true) {
								try {
									$(ts).jqGrid("addSubGrid", ts.rows[ts.rows.length - 1], gi + ni);
								}
								catch (e) {
								}
							}
							if (afterInsRow) {
								ts.p.afterInsertRow.call(ts, rid, rd, xmlr);
							}
							rowData = [];
							ari = 0;
						}
						rd = {};
						ir++;
						j++;
						if (ir == rn) {
							break;
						}
					}
				}
				if (ts.p.gridview === true) {
					$("tbody:first", t).append(rowData.join(""));
				}
				ts.p.totaltime = new Date() - startReq;
				if (ir > 0) {
					ts.grid.cols = ts.rows[0].cells;
					if (ts.p.records === 0) {
						ts.p.records = gl;
					}
				}
				rowData = null;
				if (!ts.p.treeGrid && !ts.p.scroll) {
					ts.grid.bDiv.scrollTop = 0;
				}
				ts.p.reccount = ir;
				ts.p.treeANode = -1;
				if (ts.p.userDataOnFooter) {
					$(ts).jqGrid("footerData", "set", ts.p.userData, true);
				}
				if (!more) {
					updatepager(false, true);
				}
			}, addJSONData = function (data, t, rcnt, more, adjust) {
				var startReq = new Date();
				ts.p.reccount = 0;
				if (data) {
					if (ts.p.treeANode === -1 && !ts.p.scroll) {
						emptyRows(t);
						rcnt = 0;
					} else {
						rcnt = rcnt > 0 ? rcnt : 0;
					}
				} else {
					return;
				}
				var ir = 0, v, i, j, row, f = [], F, cur, gi = 0, si = 0, ni = 0, len, drows, idn, rd = {}, fpos, rl = ts.rows.length, idr, rowData = [], ari = 0, cn = (ts.p.altRows === true) ? " " + ts.p.altclass : "", cn1;
				ts.p.page = data[ts.p.jsonReader.page] || 1;
				ts.p.lastpage = data[ts.p.jsonReader.total] || 1;
				ts.p.records = data[ts.p.jsonReader.records] || 0;
				ts.p.userData = data[ts.p.jsonReader.userdata] || {};
				if (!ts.p.jsonReader.repeatitems) {
					F = f = reader("json");
				}
				if (ts.p.keyIndex === false) {
					idn = ts.p.jsonReader.id;
				} else {
					idn = ts.p.keyIndex;
				}
				if (f.length > 0 && !isNaN(idn)) {
					if (ts.p.remapColumns && ts.p.remapColumns.length) {
						idn = $.inArray(idn, ts.p.remapColumns);
					}
					idn = f[idn];
				}
				drows = data[ts.p.jsonReader.root];
				if (drows) {
					len = drows.length, i = 0;
					var rn = parseInt(ts.p.rowNum), br = ts.p.scroll ? (parseInt(ts.p.page) - 1) * rn + 1 : 1;
					if (adjust) {
						rn *= adjust + 1;
					}
					var afterInsRow = $.isFunction(ts.p.afterInsertRow);
					while (i < len) {
						cur = drows[i];
						idr = cur[idn];
						if (idr === undefined) {
							idr = br + i;
							if (f.length === 0) {
								if (ts.p.jsonReader.cell) {
									var ccur = cur[ts.p.jsonReader.cell];
									idr = ccur[idn] || idr;
									ccur = null;
								}
							}
						}
						cn1 = i % 2 == 1 ? cn : "";
						rowData[ari++] = "<tr id=\"" + idr + "\" saveType = 'edit'  role=\"row\" class= \"ui-widget-content jqgrow ui-row-" + ts.p.direction + "" + cn1 + "\">";
						if (ts.p.rownumbers === true) {
							rowData[ari++] = addRowNum(0, i, ts.p.page, ts.p.rowNum);
							ni = 1;
						}
						if (ts.p.multiselect) {
							rowData[ari++] = addMulti(idr, ni, i);
							gi = 1;
						}
                         if(ts.p.radioselect === true)
						{
						    rowData[ari++] = addRadio(idr,ni,i); 
						    gi = 1;
						}
						if (ts.p.subGrid) {
							rowData[ari++] = $(ts).jqGrid("addSubGridCell", gi + ni, i + rcnt);
							si = 1;
						}
						if (ts.p.jsonReader.repeatitems) {
							if (ts.p.jsonReader.cell) {
								cur = cur[ts.p.jsonReader.cell];
							}
							if (!F) {
								F = orderedCols(gi + si + ni);
							}
						}
						for (j = 0; j < F.length; j++) {
							v = cur[F[j]];
							if (v === undefined) {
								try {
									v = eval("cur." + F[j]);
								}
								catch (e) {
								}
							}
							rowData[ari++] = addCell(idr, v, j + gi + si + ni, i + rcnt, cur);
							rd[ts.p.colModel[j + gi + si + ni].name] = v;
						}
						rowData[ari++] = "</tr>";
						if (ts.p.gridview === false) {
							if (ts.p.treeGrid === true) {
								fpos = ts.p.treeANode >= -1 ? ts.p.treeANode : 0;
								row = $(rowData.join(""))[0];
								try {
									$(ts).jqGrid("setTreeNode", rd, row);
								}
								catch (e) {
								}
								rl === 0 ? $("tbody:first", t).append(row) : $(ts.rows[i + fpos + rcnt]).after(row);
							} else {
								$("tbody:first", t).append(rowData.join(""));
							}
							if (ts.p.subGrid === true) {
								try {
									$(ts).jqGrid("addSubGrid", ts.rows[ts.rows.length - 1], gi + ni);
								}
								catch (e) {
								}
							}
							if (afterInsRow) {
								ts.p.afterInsertRow(idr, rd, cur);
							}
							rowData = [];
							ari = 0;
						}
						rd = {};
						ir++;
						i++;
						if (ir == rn) {
							break;
						}
					}
					if (ts.p.gridview === true) {
						$("tbody:first", t).append(rowData.join(""));
					}
					ts.p.totaltime = new Date() - startReq;
					if (ir > 0) {
						ts.grid.cols = ts.rows[0].cells;
						if (ts.p.records === 0) {
							ts.p.records = len;
						}
					}
				}
				if (!ts.p.treeGrid && !ts.p.scroll) {
					ts.grid.bDiv.scrollTop = 0;
				}
				ts.p.reccount = ir;
				ts.p.treeANode = -1;
				if (ts.p.userDataOnFooter) {
					$(ts).jqGrid("footerData", "set", ts.p.userData, true);
				}
				if (!more) {
					updatepager(false, true);
				}
			}, updatepager = function (rn, dnd) {
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
							$(".ui-paging-info", ts.p.pager).html(ts.p.emptyrecords);
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
			}, populate = function (npage) {
				if (!ts.grid.hDiv.loading) {
					var pvis = ts.p.scroll && npage == false;
					var prm = {}, dt, dstr, pN = ts.p.prmNames;
					if (pN.search !== null) {
						prm[pN.search] = ts.p.search;
					}
					if (pN.nd != null) {
						prm[pN.nd] = new Date().getTime();
					}
					if (pN.rows !== null) {
						prm[pN.rows] = ts.p.rowNum;
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
								if (lcf) {
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
					$.extend(ts.p.postData, prm);
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
									lc.call(ts, req);
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
			}, beginReq = function () {
				ts.grid.hDiv.loading = true;
				if (ts.p.hiddengrid) {
					return;
				}
				switch (ts.p.loadui) {
				  case "disable":
					break;
				  case "enable":
					$("#load_" + ts.p.id).show();
					break;
				  case "block":
					$("#lui_" + ts.p.id).show();
					$("#load_" + ts.p.id).show();
					break;
				}
			}, endReq = function () {
				ts.grid.hDiv.loading = false;
				switch (ts.p.loadui) {
				  case "disable":
					break;
				  case "enable":
					$("#load_" + ts.p.id).hide();
					break;
				  case "block":
					$("#lui_" + ts.p.id).hide();
					$("#load_" + ts.p.id).hide();
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
			
			 sortArrayData = function () {
				var stripNum = /[\$,%]/g;
				var rows = [], col = 0, st, sv, findSortKey, newDir = (ts.p.sortorder == "asc") ? 1 : -1;
				$.each(ts.p.colModel, function (i, v) {
					if (this.index == ts.p.sortname || this.name == ts.p.sortname) {
						col = ts.p.lastsort = i;
						st = this.sorttype;
						return false;
					}
				});
				if (st == "float" || st == "number" || st == "currency") {
					findSortKey = function ($cell) {
						var key = parseFloat($cell.replace(stripNum, ""));
						return isNaN(key) ? 0 : key;
					};
				} else {
					if (st == "int" || st == "integer") {
						findSortKey = function ($cell) {
							return IntNum($cell.replace(stripNum, ""));
						};
					} else {
						if (st == "date") {
							findSortKey = function ($cell) {
								var fd = ts.p.colModel[col].datefmt || "Y-m-d";
								return parseDate(fd, $cell).getTime();
							};
						} else {
							findSortKey = function ($cell) {
								return $.trim($cell.toUpperCase());
							};
						}
					}
				}
				$.each(ts.rows, function (index, row) {
					try {
						sv = $.unformat($(row).children("td").eq(col), {colModel:ts.p.colModel[col]}, col, true);
					}
					catch (_) {
						sv = $(row).children("td").eq(col).text();
					}
					row.sortKey = findSortKey(sv);
					rows[index] = this;
				});
				if (ts.p.treeGrid) {
					$(ts).jqGrid("SortTree", newDir);
				} else {
					rows.sort(function (a, b) {
						if (a.sortKey < b.sortKey) {
							return -newDir;
						}
						if (a.sortKey > b.sortKey) {
							return newDir;
						}
						return 0;
					});
					if (rows[0]) {
						$("td", rows[0]).each(function (k) {
							$(this).css("width", grid.headers[k].width + "px");
						});
						ts.grid.cols = rows[0].cells;
					}
					var cn = "";
					if (ts.p.altRows) {
						cn = ts.p.altclass;
					}
					$.each(rows, function (i, row) {
						if (cn) {
							if (i % 2 == 1) {
								$(row).addClass(cn);
							} else {
								$(row).removeClass(cn);
							}
						}
						$("tbody", ts.grid.bDiv).append(row);
						row.sortKey = null;
					});
				}
				ts.grid.bDiv.scrollTop = 0;
			}, parseDate = function (format, date) {
				var tsp = {m:1, d:1, y:1970, h:0, i:0, s:0}, k, hl, dM;
				date = date.split(/[\\\/:_;.\t\T\s-]/);
				format = format.split(/[\\\/:_;.\t\T\s-]/);
				var dfmt = $.jgrid.formatter.date.monthNames;
				for (k = 0, hl = format.length; k < hl; k++) {
					if (format[k] == "M") {
						dM = $.inArray(date[k], dfmt);
						if (dM !== -1 && dM < 12) {
							date[k] = dM + 1;
						}
					}
					if (format[k] == "F") {
						dM = $.inArray(date[k], dfmt);
						if (dM !== -1 && dM > 11) {
							date[k] = dM + 1 - 12;
						}
					}
					tsp[format[k].toLowerCase()] = parseInt(date[k], 10);
				}
				tsp.m = parseInt(tsp.m, 10) - 1;
				var ty = tsp.y;
				if (ty >= 70 && ty <= 99) {
					tsp.y = 1900 + tsp.y;
				} else {
					if (ty >= 0 && ty <= 69) {
						tsp.y = 2000 + tsp.y;
					}
				}
				return new Date(tsp.y, tsp.m, tsp.d, tsp.h, tsp.i, tsp.s, 0);
			}, setPager = function () {
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
				$(ts.p.pager).append("<div id='" + pgcnt + "' class='ui-pager-control' role='group'><table cellspacing='0' cellpadding='0' border='0' class='ui-pg-table' style='width:100%;' role='row'><tbody><tr><td id='" + lft + "' align='left' width = '1%'></td><td id='" + cent + "' align='left' style='white-space:pre;' width = '40%'></td><td id='" + rgt + "' align='right' width = '55%'></td></tr></tbody></table></div>").attr("dir", "ltr");
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
					ts.p.rowNum = $(this).val();
					clearVals("records");
					populate();
					return false;
					}
				});
				$("#unitpage_set","#" + pgcnt).click(function(){
				ts.p.page = Math.round(ts.p.rowNum * (ts.p.page - 1) / $(".ui-pg-selbox", "#" + pgcnt).val() - 0.5) + 1;
					ts.p.rowNum = $(".ui-pg-selbox", "#" + pgcnt).val();
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
							clearVals("user");
							populate();
							return false;
						}
						return this;
					});
				}
			}, getParserById = function (name) {
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
				var cells = ts.tBodies[0].rows[0].cells, l = cells.length;
				for (var i=0;i < l; i++) {
					var p = false;
					if((ts.p.colModel[i] && ts.p.colModel[i].sorter)) {
						p = getParserById(ts.p.colModel[i].sorter);
					}
					ts.p.parsers.push(p);
				}
			}, buildCache = function () {
				var totalRows = (ts.tBodies[0] && ts.tBodies[0].rows.length) || 0,
					totalCells = ts.tBodies[0].rows[0].cells.length,
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
			
			}, sortData = function (index, idxcol, reload, sor) {
				if (!ts.p.colModel[idxcol].sortable) {
					return;
				}
				var imgs, so;
				if (ts.p.savedRow.length > 0) {
					return;
				}
				if (!reload) {
					if (ts.p.lastsort == idxcol) {
						if (ts.p.sortorder == "asc") {
							ts.p.sortorder = "desc";
						} else {
							if (ts.p.sortorder == "desc") {
								ts.p.sortorder = "asc";
							}
						}
					} else {
						ts.p.sortorder = "asc";
					}
					ts.p.page = 1;
				}
				if (sor) {
					if (ts.p.lastsort == idxcol && ts.p.sortorder == sor) {
						return;
					} else {
						ts.p.sortorder = sor;
					}
				}
				var thd = $("thead:first", ts.grid.hDiv).get(0);
				$("tr th:eq(" + ts.p.lastsort + ") span.ui-grid-ico-sort", thd).addClass("ui-state-disabled");
				$("tr th:eq(" + ts.p.lastsort + ")", thd).attr("aria-selected", "false");
				$("tr th:eq(" + idxcol + ") span.ui-icon-" + ts.p.sortorder, thd).removeClass("ui-state-disabled");
				$("tr th:eq(" + idxcol + ")", thd).attr("aria-selected", "true");
				if (!ts.p.viewsortcols[0]) {
					if (ts.p.lastsort != idxcol) {
						$("tr th:eq(" + ts.p.lastsort + ") span.s-ico", thd).hide();
						$("tr th:eq(" + idxcol + ") span.s-ico", thd).show();
					}
				}
				ts.p.lastsort = idxcol;
				index = index.substring(5);
				ts.p.sortname = ts.p.colModel[idxcol].index || index;
				so = ts.p.sortorder;
				if ($.isFunction(ts.p.onSortCol)) {
					ts.p.onSortCol.call(ts, index, idxcol, so);
				}
				if(ts.p.orderType && ts.p.orderType == "server"){//全局排序
					if (ts.p.datatype == "local") {
						if (ts.p.deselectAfterSort) {
							$(ts).jqGrid("resetSelection");
						}
					} else {
						ts.p.selrow = null;
						if (ts.p.multiselect) {
							$("#cb_" + $.jgrid.jqID(ts.p.id), ts.grid.hDiv).attr("checked", false);
						}
						ts.p.selarrrow = [];
						ts.p.savedRow = [];
						if (ts.p.scroll) {
							emptyRows(ts.grid.bDiv, true);
						}
					}
					if (ts.p.subGrid && ts.p.datatype == "local") {
						$("td.sgexpanded", "#" + ts.p.id).each(function () {
							$(this).trigger("click");
						});
					}
					populate();
				}else{//本页排序，ts.p.orderType = "page"
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
				if (ts.p.sortname != index && idxcol) {
					ts.p.lastsort = idxcol;
				}
			}, setColWidth = function () {
				var initwidth = 0, brd = ts.p.cellLayout, vc = 0, lvc, scw = ts.p.scrollOffset, cw, hs = false, aw, tw = 0, gw = 0, cl = 0, cr;
				if (isSafari) {
					brd = 0;
				}
				$.each(ts.p.colModel, function (i) {
					if (typeof this.hidden === "undefined") {
						this.hidden = false;
					}
					if (this.hidden === false) {
						initwidth += IntNum(this.width);
						if (this.fixed) {
							tw += this.width;
							gw += this.width + brd;
						} else {
							vc++;
						}
						cl++;
					}
				});
				if (isNaN(ts.p.width)) {
					ts.p.width = grid.width = initwidth;
				} else {
					grid.width = ts.p.width;
				}
				ts.p.tblwidth = initwidth;
				if (ts.p.shrinkToFit === false && ts.p.forceFit === true) {
					ts.p.forceFit = false;
				}
				if (ts.p.shrinkToFit === true && vc > 0) {
					aw = grid.width - brd * vc - gw;
					if (isNaN(ts.p.height)) {
					} else {
						aw -= scw;
						hs = true;
					}
					initwidth = 0;
					$.each(ts.p.colModel, function (i) {
						if (this.hidden === false && !this.fixed) {
							cw = Math.floor(aw / (ts.p.tblwidth - tw) * this.width);
							this.width = cw;
							initwidth += cw;
							lvc = i;
						}
					});
					cr = 0;
					if (hs) {
						if (grid.width - gw - (initwidth + brd * vc) !== scw) {
							cr = grid.width - gw - (initwidth + brd * vc) - scw;
						}
					} else {
						if (!hs && Math.abs(grid.width - gw - (initwidth + brd * vc)) !== 1) {
							cr = grid.width - gw - (initwidth + brd * vc);
						}
					}
					ts.p.colModel[lvc].width += cr;
					ts.p.tblwidth = initwidth + cr + tw + cl * brd;
				}
			}, nextVisible = function (iCol) {
				var ret = iCol, j = iCol, i;
				for (i = iCol + 1; i < ts.p.colModel.length; i++) {
					if (ts.p.colModel[i].hidden !== true) {
						j = i;
						break;
					}
				}
				return j - ret;
			}, getOffset = function (iCol) {
				var i, ret = {}, brd1 = isSafari ? 0 : ts.p.cellLayout;
				ret[0] = ret[1] = ret[2] = 0;
				for (i = 0; i <= iCol; i++) {
					if (ts.p.colModel[i].hidden === false) {
						ret[0] += ts.p.colModel[i].width + brd1;
					}
				}
				if (ts.p.direction == "rtl") {
					ret[0] = ts.p.width - ret[0];
				}
				ret[0] = ret[0] - ts.grid.bDiv.scrollLeft;
				if ($(ts.grid.cDiv).is(":visible")) {
					ret[1] += $(ts.grid.cDiv).height() + parseInt($(ts.grid.cDiv).css("padding-top")) + parseInt($(ts.grid.cDiv).css("padding-bottom"));
				}
				if (ts.p.toolbar[0] == true && (ts.p.toolbar[1] == "top" || ts.p.toolbar[1] == "both")) {
					ret[1] += $(ts.grid.uDiv).height() + parseInt($(ts.grid.uDiv).css("border-top-width")) + parseInt($(ts.grid.uDiv).css("border-bottom-width"));
				}
				ret[2] += $(ts.grid.bDiv).height() + $(ts.grid.hDiv).height();
				return ret;
			};
			this.p.id = this.id;
			if ($.inArray(ts.p.multikey, sortkeys) == -1) {
				ts.p.multikey = false;
			}
			ts.p.keyIndex = false;
			for (i = 0; i < ts.p.colModel.length; i++) {
				if (ts.p.colModel[i].key === true) {
					ts.p.keyIndex = i;
					break;
				}
			}
			ts.p.sortorder = ts.p.sortorder.toLowerCase();
			if (this.p.treeGrid === true) {
				try {
					$(this).jqGrid("setTreeGrid");
				}
				catch (_) {
				}
			}
			if (this.p.subGrid) {
				try {
					$(ts).jqGrid("setSubGrid");
				}
				catch (_) {
				}
			}
			if (this.p.multiselect) {
				this.p.colNames.unshift("<input id='cb_" + this.p.id + "' name='cb_" + this.p.id + "' class='cbox' type='checkbox'/>");//增加全选框的name属性，兼容validator.js
				this.p.colModel.unshift({name:"cb", width:isSafari ? ts.p.multiselectWidth + ts.p.cellLayout : ts.p.multiselectWidth, sortable:false, resizable:false, hidedlg:true, search:false, align:"center", fixed:true});
			}
			if(this.p.radioselect){
			this.p.colNames.unshift("");
				this.p.colModel.unshift({name:"rb", width:ts.p.rownumWidth, sortable:false, resizable:false, hidedlg:true, search:false, align:"center", fixed:true});
			}
			if (this.p.rownumbers) {
				this.p.colNames.unshift("");
				this.p.colModel.unshift({name:"rn", width:ts.p.rownumWidth, sortable:false, resizable:false, hidedlg:true, search:false, align:"center", fixed:true});
			}
			ts.p.xmlReader = $.extend({root:"rows", row:"row", page:"rows>page", total:"rows>total", records:"rows>records", repeatitems:true, cell:"cell", id:"[id]", userdata:"userdata", subgrid:{root:"rows", row:"row", repeatitems:true, cell:"cell"}}, ts.p.xmlReader);
			ts.p.jsonReader = $.extend({root:"rows", page:"page", total:"total", records:"records", repeatitems:true, cell:"cell", id:"id", userdata:"userdata", subgrid:{root:"rows", repeatitems:true, cell:"cell"}}, ts.p.jsonReader);
			if (ts.p.scroll) {
				ts.p.pgbuttons = false;
				ts.p.pginput = false;
				ts.p.rowList = [];
			}
			var thead = "<thead><tr class='ui-jqgrid-labels' role='rowheader'>", tdc, idn, w, res, sort, td, ptr, tbody, imgs, iac = "", idc = "";
			if (ts.p.shrinkToFit === true && ts.p.forceFit === true) {
				for (i = ts.p.colModel.length - 1; i >= 0; i--) {
					if (!ts.p.colModel[i].hidden) {
						ts.p.colModel[i].resizable = false;
						break;
					}
				}
			}
			if (ts.p.viewsortcols[1] == "horizontal") {
				iac = " ui-i-asc";
				idc = " ui-i-desc";
			}
			tdc = isMSIE ? "class='ui-th-div-ie'" : "";
			imgs = "<span class='s-ico' style='display:none'><span sort='asc' class='ui-grid-ico-sort ui-icon-asc" + iac + " ui-state-disabled ui-icon ui-icon-triangle-1-n ui-sort-" + dir + "'></span>";
			imgs += "<span sort='desc' class='ui-grid-ico-sort ui-icon-desc" + idc + " ui-state-disabled ui-icon ui-icon-triangle-1-s ui-sort-" + dir + "'></span></span>";
			for (i = 0; i < this.p.colNames.length; i++) {
				thead += "<th role='columnheader' class='ui-state-default ui-th-column ui-th-" + dir + "'>";
				idn = ts.p.colModel[i].index || ts.p.colModel[i].name;
				thead += "<div id='jqgh_" + ts.p.colModel[i].name + "' " + tdc + ">" + ts.p.colNames[i];
				if (idn == ts.p.sortname) {
					ts.p.lastsort = i;
				}
				thead += imgs + "</div></th>";
			}
			thead += "</tr></thead>";
			$(this).append(thead);
			$("thead tr:first th", this).hover(function () {
				$(this).addClass("ui-state-hover");
			}, function () {
				$(this).removeClass("ui-state-hover");
			});
			if (this.p.multiselect) {
				var onSA = true, emp = [], chk;
				if (typeof ts.p.onSelectAll !== "function") {
					onSA = false;
				}
				$("#cb_" + $.jgrid.jqID(ts.p.id), this).bind("click", function () {
					if (this.checked) {
						$("[id^=jqg_]", ts.rows).attr("checked", true);
						$(ts.rows).each(function (i) {
							if (!$(this).hasClass("subgrid")) {
								$(this).addClass("ui-state-highlight").attr("aria-selected", "true");
								ts.p.selarrrow[i] = ts.p.selrow = this.id;
							}
						});
						chk = true;
						emp = [];
					} else {
						$("[id^=jqg_]", ts.rows).attr("checked", false);
						$(ts.rows).each(function (i) {
							if (!$(this).hasClass("subgrid")) {
								$(this).removeClass("ui-state-highlight").attr("aria-selected", "false");
								emp[i] = this.id;
							}
						});
						ts.p.selarrrow = [];
						ts.p.selrow = null;
						chk = false;
					}
					if (onSA) {
						ts.p.onSelectAll(chk ? ts.p.selarrrow : emp, chk);
					}
					var udata = $("#"+$.jgrid.jqID(ts.p.id)).jqGrid('getRowNums');
		                 if(udata == null)
		                     {
		                     udata = "";
		                     }
			               $("#t_"+$.jgrid.jqID(ts.p.id)).css("text-align", "left").html("已选择：" + udata);
				});
			}
			$.each(ts.p.colModel, function (i) {
				if (!this.width) {
					this.width = 150;
				}
				this.width = parseInt(this.width);
			});
			if (ts.p.autowidth === true) {
				var pw = $(eg).innerWidth();
				ts.p.width = pw > 0 ? pw : "nw";
			}
			setColWidth();
			$(eg).css("width", grid.width + "px").append("<div class='ui-jqgrid-resize-mark' id='rs_m" + ts.p.id + "'>&#160;</div>");
			$(gv).css("width", grid.width + "px");
			thead = $("thead:first", ts).get(0);
			var tfoot = "<table role='grid' style='width:" + ts.p.tblwidth + "px' class='ui-jqgrid-ftable' cellspacing='0' cellpadding='0' border='0'><tbody><tr role='row' class='ui-widget-content footrow footrow-" + dir + "'>";
			var thr = $("tr:first", thead);
			ts.p.disableClick = false;
			$("th", thr).each(function (j) {
				var ht = $("div", this)[0];
				w = ts.p.colModel[j].width;
				if (typeof ts.p.colModel[j].resizable === "undefined") {
					ts.p.colModel[j].resizable = true;
				}
				if (ts.p.colModel[j].resizable) {
					res = document.createElement("span");
					$(res).html("&#160;").addClass("ui-jqgrid-resize ui-jqgrid-resize-" + dir);
					!$.browser.opera ? $(res).css("cursor", "col-resize") : "";
					$(this).addClass(ts.p.resizeclass);
				} else {
					res = "";
				}
				$(this).css("width", w + "px").prepend(res);
				if (ts.p.colModel[j].hidden) {
					$(this).css("display", "none");
				}
				grid.headers[j] = {width:w, el:this};
				sort = ts.p.colModel[j].sortable;
				if (typeof sort !== "boolean") {
					ts.p.colModel[j].sortable = true;
					sort = true;
				}
				var nm = ts.p.colModel[j].name;
				if (!(nm == "cb" || nm == "subgrid" || nm == "rn" || nm == "rb")) {
					if (ts.p.viewsortcols[2]) {
						$("div", this).addClass("ui-jqgrid-sortable");
					}
				}
				if (sort) {
					if (ts.p.viewsortcols[0]) {
						$("div span.s-ico", this).show();
						if (j == ts.p.lastsort) {
							$("div span.ui-icon-" + ts.p.sortorder, this).removeClass("ui-state-disabled");
						}
					} else {
						if (j == ts.p.lastsort) {
							$("div span.s-ico", this).show();
							$("div span.ui-icon-" + ts.p.sortorder, this).removeClass("ui-state-disabled");
						}
					}
				}
				tfoot += "<td role='gridcell' " + formatCol(j, 0) + ">&#160;</td>";
			}).mousedown(function (e) {
				if ($(e.target).closest("th>span.ui-jqgrid-resize").length != 1) {
					return;
				}
				var ci = $.jgrid.getCellIndex(this);
				if (ts.p.forceFit === true) {
					ts.p.nv = nextVisible(ci);
				}
				grid.dragStart(ci, e, getOffset(ci));
				return false;
			}).click(function (e) {
				if (ts.p.disableClick) {
					ts.p.disableClick = false;
					return false;
				}
				var s = "th>div.ui-jqgrid-sortable", r, d;
				if (!ts.p.viewsortcols[2]) {
					s = "th>div>span>span.ui-grid-ico-sort";
				}
				var t = $(e.target).closest(s);
				if (t.length != 1) {
					return;
				}
				var ci = $.jgrid.getCellIndex(this);
				if (!ts.p.viewsortcols[2]) {
					r = true, d = t.attr("sort");
				}
				sortData($("div", this)[0].id, ci, r, d);
				return false;
			});
			if (ts.p.sortable && $.fn.sortable) {
				try {
					$(ts).jqGrid("sortableColumns", thr);
				}
				catch (e) {
				}
			}
			tfoot += "</tr></tbody></table>";
			tbody = document.createElement("tbody");
			this.appendChild(tbody);
			$(this).addClass("ui-jqgrid-btable");
			var hTable = $("<table class='ui-jqgrid-htable' style='width:" + ts.p.tblwidth + "px' role='grid' aria-labelledby='gbox_" + this.id + "' cellspacing='0' cellpadding='0' border='0'></table>").append(thead), hg = (ts.p.caption && ts.p.hiddengrid === true) ? true : false, hb = $("<div class='ui-jqgrid-hbox" + (dir == "rtl" ? "-rtl" : "") + "'></div>");
			grid.hDiv = document.createElement("div");
			$(grid.hDiv).css({width:grid.width + "px"}).addClass("ui-state-default ui-jqgrid-hdiv").append(hb);
			$(hb).append(hTable);
			if (hg) {
				$(grid.hDiv).hide();
			}
			ts.p._height = 0;
			if (ts.p.pager) {
				if (typeof ts.p.pager == "string") {
					if (ts.p.pager.substr(0, 1) != "#") {
						ts.p.pager = "#" + ts.p.pager;
					}
				}
				$(ts.p.pager).css({width:grid.width + "px"}).appendTo(eg).addClass("ui-state-default ui-jqgrid-pager");
				ts.p._height += parseInt($(ts.p.pager).height(), 10);
				if (hg) {
					$(ts.p.pager).hide();
				}
				setPager();
			}
			if (ts.p.cellEdit === false && ts.p.hoverrows === true) {
				$(ts).bind("mouseover", function (e) {
					ptr = $(e.target).closest("tr.jqgrow");
					if ($(ptr).attr("class") !== "subgrid") {
						$(ptr).addClass("ui-state-hover");
					}
					return false;
				}).bind("mouseout", function (e) {
					ptr = $(e.target).closest("tr.jqgrow");
					$(ptr).removeClass("ui-state-hover");
					return false;
				});
			}
			var ri, ci;
			$(ts).before(grid.hDiv).click(function (e) {
				td = e.target;
				var scb = $(td).hasClass("cbox");
				ptr = $(td, ts.rows).closest("tr.jqgrow");
				if ($(ptr).length === 0) {
					return this;
				}
				var cSel = true;
				if ($.isFunction(ts.p.beforeSelectRow)) {
					cSel = ts.p.beforeSelectRow.call(ts, ptr[0].id, e);
				}
				if (td.tagName == "A" || ((td.tagName == "INPUT" || td.tagName == "TEXTAREA" || td.tagName == "OPTION" || td.tagName == "SELECT") && !scb)) {
					return true;
				}
				if (cSel === true) {
					if (ts.p.cellEdit === true) {
						if (ts.p.multiselect && scb) {
							$(ts).jqGrid("setSelection", ptr[0].id, true);
						} else {
							ri = ptr[0].rowIndex;
							ci = $.jgrid.getCellIndex(td);
							try {
								$(ts).jqGrid("editCell", ri, ci, true);
							}
							catch (_) {
							}
						}
					} else {
						if (!ts.p.multikey) {
							if (ts.p.multiselect && ts.p.multiboxonly) {
								if (scb) {
									$(ts).jqGrid("setSelection", ptr[0].id, true);
								} else {
									$(ts.p.selarrrow).each(function (i, n) {
										var ind = ts.rows.namedItem(n);
										$(ind).removeClass("ui-state-highlight");
										$("#jqg_" + $.jgrid.jqID(n), ind).attr("checked", false);
									});
									ts.p.selarrrow = [];
									$("#cb_" + $.jgrid.jqID(ts.p.id), ts.grid.hDiv).attr("checked", false);
									$(ts).jqGrid("setSelection", ptr[0].id, true);
								}
							} else {
								$(ts).jqGrid("setSelection", ptr[0].id, true);
							}
						} else {
							if (e[ts.p.multikey]) {
								$(ts).jqGrid("setSelection", ptr[0].id, true);
							} else {
								if (ts.p.multiselect && scb) {
									scb = $("[id^=jqg_]", ptr).attr("checked");
									$("[id^=jqg_]", ptr).attr("checked", !scb);
								}
							}
						}
					}
					if ($.isFunction(ts.p.onCellSelect)) {
						ri = ptr[0].id;
						ci = $.jgrid.getCellIndex(td);
						ts.p.onCellSelect.call(ts, ri, ci, $(td).html(), e);
					}
				}
				e.stopPropagation();
			}).bind("reloadGrid", function (e, opts) {
				if (ts.p.treeGrid === true) {
					ts.p.datatype = ts.p.treedatatype;
				}
				if (opts && opts.current) {
					ts.grid.selectionPreserver(ts);
				}
				if (ts.p.datatype == "local") {
					$(ts).jqGrid("resetSelection");
				} else {
					if (!ts.p.treeGrid) {
						ts.p.selrow = null;
						if (ts.p.multiselect) {
							ts.p.selarrrow = [];
							$("#cb_" + $.jgrid.jqID(ts.p.id), ts.grid.hDiv).attr("checked", false);
						}
						ts.p.savedRow = [];
						if (ts.p.scroll) {
							emptyRows(ts.grid.bDiv);
						}
					}
				}
				if (opts && opts.page) {
					var page = opts.page;
					if (page > ts.p.lastpage) {
						page = ts.p.lastpage;
					}
					if (page < 1) {
						page = 1;
					}
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
				} else {
					ts.grid.populate();
				}
				return false;
			});
			if ($.isFunction(this.p.ondblClickRow)) {
				$(this).dblclick(function (e) {
					td = e.target;
					ptr = $(td, ts.rows).closest("tr.jqgrow");
					if ($(ptr).length === 0) {
						return false;
					}
					ri = ptr[0].rowIndex;
					ci = $.jgrid.getCellIndex(td);
					ts.p.ondblClickRow.call(ts, $(ptr).attr("id"), ri, ci, e);
					return false;
				});
			}
			if ($.isFunction(this.p.onRightClickRow)) {
				$(this).bind("contextmenu", function (e) {
					td = e.target;
					ptr = $(td, ts.rows).closest("tr.jqgrow");
					if ($(ptr).length === 0) {
						return false;
					}
					if (!ts.p.multiselect) {
					     $(ts).jqGrid("setSelection", ptr[0].id, true);
					}
					
					if(!$('input',ptr).attr('checked'))
					{
						$(ts).jqGrid("setSelection", ptr[0].id, true);
						}
					ri = ptr[0].rowIndex;
					ci = $.jgrid.getCellIndex(td);
					//alert($(ptr).attr("id"));
					var res = $(ts).jqGrid("getRowValues",$(ptr).attr("id"));//得到某一行的数据
					
					ts.p.onRightClickRow.call(ts, $(ptr).attr("id"),res, ri, ci, e);
					return false;
				});
			}
			grid.bDiv = document.createElement("div");
			$(grid.bDiv).append($("<div style=\"position:relative\"></div>").append("<div></div>").append(this)).addClass("ui-jqgrid-bdiv").css({height:ts.p.height + (isNaN(ts.p.height) ? "" : "px"), width:(grid.width) + "px"}).scroll(grid.scrollGrid);
			$("table:first", grid.bDiv).css({width:ts.p.tblwidth + "px"});
			if (isMSIE) {
				if ($("tbody", this).size() == 2) {
					$("tbody:first", this).remove();
				}
				if (ts.p.multikey) {
					$(grid.bDiv).bind("selectstart", function () {
						return false;
					});
				}
			} else {
				if (ts.p.multikey) {
					$(grid.bDiv).bind("mousedown", function () {
						return false;
					});
				}
			}
			if (hg) {
				$(grid.bDiv).hide();
			}
			grid.cDiv = document.createElement("div");
			var arf = ts.p.hidegrid === true ? $("<a role='link' href='javascript:void(0)'/>").addClass("ui-jqgrid-titlebar-close HeaderButton").hover(function () {
				arf.addClass("ui-state-hover");
			}, function () {
				arf.removeClass("ui-state-hover");
			}).append("<span class='ui-icon ui-icon-circle-triangle-n'></span>").css((dir == "rtl" ? "left" : "right"), "0px") : "";
			$(grid.cDiv).append(arf).append("<span class='ui-jqgrid-title" + (dir == "rtl" ? "-rtl" : "") + "'>" + ts.p.caption + "</span>").addClass("ui-jqgrid-titlebar ui-widget-header ui-corner-tl ui-corner-tr ui-helper-clearfix");
			$(grid.cDiv).insertBefore(grid.hDiv);
			if (ts.p.toolbar[0]) {
				grid.uDiv = document.createElement("div");
				if (ts.p.toolbar[1] == "top") {
					$(grid.uDiv).insertBefore(grid.hDiv);
				} else {
					if (ts.p.toolbar[1] == "bottom") {
						$(grid.uDiv).insertAfter(grid.hDiv);
					}
				}
				if (ts.p.toolbar[1] == "both") {
					grid.ubDiv = document.createElement("div");
					$(grid.uDiv).insertBefore(grid.hDiv).addClass("ui-userdata ui-state-default").attr("id", "t_" + this.id);
					$(grid.ubDiv).insertAfter(grid.hDiv).addClass("ui-userdata ui-state-default").attr("id", "tb_" + this.id);
					ts.p._height += IntNum($(grid.ubDiv).height());
					if (hg) {
						$(grid.ubDiv).hide();
					}
				} else {
					$(grid.uDiv).width(grid.width).addClass("ui-userdata ui-state-default").attr("id", "t_" + this.id);
				}
				ts.p._height += IntNum($(grid.uDiv).height());
				if (hg) {
					$(grid.uDiv).hide();
				}
			}
			if (ts.p.footerrow) {
				grid.sDiv = $("<div class='ui-jqgrid-sdiv'></div>")[0];
				hb = $("<div class='ui-jqgrid-hbox" + (dir == "rtl" ? "-rtl" : "") + "'></div>");
				$(grid.sDiv).append(hb).insertAfter(grid.hDiv).width(grid.width);
				$(hb).append(tfoot);
				grid.footers = $(".ui-jqgrid-ftable", grid.sDiv)[0].rows[0].cells;
				if (ts.p.rownumbers) {
					grid.footers[0].className = "ui-state-default jqgrid-rownum";
				}
				if (hg) {
					$(grid.sDiv).hide();
				}
			}
			if (ts.p.caption) {
				ts.p._height += parseInt($(grid.cDiv, ts).height(), 10);
				var tdt = ts.p.datatype;
				if (ts.p.hidegrid === true) {
					$(".ui-jqgrid-titlebar-close", grid.cDiv).click(function (e) {
						var onHdCl = $.isFunction(ts.p.onHeaderClick);
						if (ts.p.gridstate == "visible") {
							$(".ui-jqgrid-bdiv, .ui-jqgrid-hdiv", "#gview_" + ts.p.id).slideUp("fast");
							if (ts.p.pager) {
								$(ts.p.pager).slideUp("fast");
							}
							if (ts.p.toolbar[0] === true) {
								if (ts.p.toolbar[1] == "both") {
									$(grid.ubDiv).slideUp("fast");
								}
								$(grid.uDiv).slideUp("fast");
							}
							if (ts.p.footerrow) {
								$(".ui-jqgrid-sdiv", "#gbox_" + ts.p.id).slideUp("fast");
							}
							$("span", this).removeClass("ui-icon-circle-triangle-n").addClass("ui-icon-circle-triangle-s");
							ts.p.gridstate = "hidden";
							if (onHdCl) {
								if (!hg) {
									ts.p.onHeaderClick.call(ts, ts.p.gridstate, e);
								}
							}
						} else {
							if (ts.p.gridstate == "hidden") {
								$(".ui-jqgrid-hdiv, .ui-jqgrid-bdiv", "#gview_" + ts.p.id).slideDown("fast");
								if (ts.p.pager) {
									$(ts.p.pager).slideDown("fast");
								}
								if (ts.p.toolbar[0] === true) {
									if (ts.p.toolbar[1] == "both") {
										$(grid.ubDiv).slideDown("fast");
									}
									$(grid.uDiv).slideDown("fast");
								}
								if (ts.p.footerrow) {
									$(".ui-jqgrid-sdiv", "#gbox_" + ts.p.id).slideDown("fast");
								}
								$("span", this).removeClass("ui-icon-circle-triangle-s").addClass("ui-icon-circle-triangle-n");
								if (hg) {
									ts.p.datatype = tdt;
									populate();
									hg = false;
								}
								ts.p.gridstate = "visible";
								if (onHdCl) {
									ts.p.onHeaderClick.call(ts, ts.p.gridstate, e);
								}
							}
						}
						return false;
					});
					if (hg) {
						ts.p.datatype = "local";
						$(".ui-jqgrid-titlebar-close", grid.cDiv).trigger("click");
					}
				}
			} else {
				$(grid.cDiv).hide();
			}
			$(grid.hDiv).after(grid.bDiv).mousemove(function (e) {
				if (grid.resizing) {
					grid.dragMove(e);
					return false;
				}
			});
			$(".ui-jqgrid-labels", grid.hDiv).bind("selectstart", function () {
				return false;
			});
			ts.p._height += parseInt($(grid.hDiv).height(), 10);
			$(document).mouseup(function (e) {
				if (grid.resizing) {
					grid.dragEnd();
					return false;
				}
				return true;
			});
			this.updateColumns = function () {
				var r = this.rows[0], self = this;
				if (r) {
					$("td", r).each(function (k) {
						$(this).css("width", self.grid.headers[k].width + "px");
					});
					this.grid.cols = r.cells;
				}
				return this;
			};
			ts.formatCol = formatCol;
			ts.sortData = sortData;
			ts.updatepager = updatepager;
			ts.formatter = function (rowId, cellval, colpos, rwdat, act) {
				return formatter(rowId, cellval, colpos, rwdat, act);
			};
			$.extend(grid, {populate:populate, emptyRows:emptyRows});
			this.grid = grid;
			ts.addXmlData = function (d) {
				addXmlData(d, ts.grid.bDiv);
			};
			ts.addJSONData = function (d) {
				addJSONData(d, ts.grid.bDiv);
			};
			populate();
			ts.p.hiddengrid = false;
			$(window).unload(function () {
				$(this).empty();
				this.grid = null;
				this.p = null;
			});
		});
	};
	$.jgrid.extend({getGridParam:function (pName) {
		var $t = this[0];
		if (!$t.grid) {
			return;
		}
		if (!pName) {
			return $t.p;
		} else {
			return typeof ($t.p[pName]) != "undefined" ? $t.p[pName] : null;
		}
	}, setGridParam:function (newParams) {
		return this.each(function () {
			if (this.grid && typeof (newParams) === "object") {
				$.extend(true, this.p, newParams);
			}
		});
	}, getDataIDs:function () {
		var ids = [], i = 0, len;
		this.each(function () {
			len = this.rows.length;
			if (len && len > 0) {
				while (i < len) {
					ids[i] = this.rows[i].id;
					i++;
				}
			}
		});
		return ids;
	}, setSelection:function (selection, onsr) {
		return this.each(function () {
			var $t = this, stat, pt, olr, ner, ia, tpsr;
			if (selection === undefined) {
				return;
			}
			onsr = onsr === false ? false : true;
			pt = $t.rows.namedItem(selection);
			if (pt == null) {
				return;
			}
			if ($t.p.selrow && $t.p.scrollrows === true) {
				olr = $t.rows.namedItem($t.p.selrow).rowIndex;
				ner = $t.rows.namedItem(selection).rowIndex;
				if (ner >= 0) {
					if (ner > olr) {
						scrGrid(ner, "d");
					} else {
						scrGrid(ner, "u");
					}
				}
			}
			if (!$t.p.multiselect) {
				if ($(pt).attr("class") !== "subgrid") {
					if ($t.p.selrow) {
						$("tr#" + $.jgrid.jqID($t.p.selrow), $t.grid.bDiv).removeClass("ui-state-highlight").attr("aria-selected", "false");
					}
					$t.p.selrow = pt.id;
					$(pt).addClass("ui-state-highlight").attr("aria-selected", "true");
					$("#jqg_" + $.jgrid.jqID($t.p.selrow), $t.rows).attr("checked", true);
					if ($t.p.onSelectRow && onsr) {
						$t.p.onSelectRow($t.p.selrow, true);
					}
				}
			} else {
				$t.p.selrow = pt.id;
				ia = $.inArray($t.p.selrow, $t.p.selarrrow);
				if (ia === -1) {
					if ($(pt).attr("class") !== "subgrid") {
						$(pt).addClass("ui-state-highlight").attr("aria-selected", "true");
					}
					stat = true;
					$("#jqg_" + $.jgrid.jqID($t.p.selrow), $t.rows).attr("checked", stat);
					$t.p.selarrrow.push($t.p.selrow);
					if ($t.p.onSelectRow && onsr) {
						$t.p.onSelectRow($t.p.selrow, stat);
					}
				} else {
					if ($(pt).attr("class") !== "subgrid") {
						$(pt).removeClass("ui-state-highlight").attr("aria-selected", "false");
					}
					stat = false;
					$("#jqg_" + $.jgrid.jqID($t.p.selrow), $t.rows).attr("checked", stat);
					$t.p.selarrrow.splice(ia, 1);
					if ($t.p.onSelectRow && onsr) {
						$t.p.onSelectRow($t.p.selrow, stat);
					}
					tpsr = $t.p.selarrrow[0];
					$t.p.selrow = (tpsr === undefined) ? null : tpsr;
				}
			}
			function scrGrid(iR, tp) {
				var ch = $($t.grid.bDiv)[0].clientHeight, st = $($t.grid.bDiv)[0].scrollTop, nROT = $t.rows[iR].offsetTop + $t.rows[iR].clientHeight, pROT = $t.rows[iR].offsetTop;
				if (tp == "d") {
					if (nROT >= ch) {
						$($t.grid.bDiv)[0].scrollTop = st + nROT - pROT;
					}
				}
				if (tp == "u") {
					if (pROT < st) {
						$($t.grid.bDiv)[0].scrollTop = st - nROT + pROT;
					}
				}
			}
		});
	}, resetSelection:function () {
		return this.each(function () {
			var t = this, ind;
			if (!t.p.multiselect) {
				if (t.p.selrow) {
					$("tr#" + $.jgrid.jqID(t.p.selrow), t.grid.bDiv).removeClass("ui-state-highlight").attr("aria-selected", "false");
					t.p.selrow = null;
				}
			} else {
				$(t.p.selarrrow).each(function (i, n) {
					ind = t.rows.namedItem(n);
					$(ind).removeClass("ui-state-highlight").attr("aria-selected", "false");
					$("#jqg_" + $.jgrid.jqID(n), ind).attr("checked", false);
				});
				$("#cb_" + $.jgrid.jqID(t.p.id), t.grid.hDiv).attr("checked", false);
				t.p.selarrrow = [];
			}
			t.p.savedRow = [];
		});
	}, getRowData:function (rowid) {
		var res = {}, resall, getall = false, len, j = 0;
		this.each(function () {
			var $t = this, nm, ind;
			if (typeof (rowid) == "undefined") {
				getall = true;
				resall = [];
				len = $t.rows.length;
			} else {
				ind = $t.rows.namedItem(rowid);
				if (!ind) {
					return res;
				}
				len = 1;
			}
			while (j < len) {
				if (getall) {
					ind = $t.rows[j];
				}
				$("td", ind).each(function (i) {
					nm = $t.p.colModel[i].name;
					if (nm !== "cb" && nm !== "subgrid" && nm!=="rb") {
						if ($t.p.treeGrid === true && nm == $t.p.ExpandColumn) {
							res[nm] = $.jgrid.htmlDecode($("span:first", this).html());
						} else {
							try {
								res[nm] = $.unformat(this, {colModel:$t.p.colModel[i]}, i);
							}
							catch (e) {
								res[nm] = $.jgrid.htmlDecode($(this).html());
							}
						}
					}
				});
				j++;
				if (getall) {
					resall.push(res);
					res = {};
				}
			}
		});
		return resall ? resall : res;
	}, 
	getRowValues:function(rowid)
	{
	
	var res = {}, resall, getall = false, len, j = 0;
		this.each(function () {
			var $t = this, nm, ind;
			if (typeof (rowid) == "undefined") {
				getall = true;
				resall = [];
				len = $t.rows.length;
			} else {
				ind = $t.rows.namedItem(rowid);
				if (!ind) {
					return res;
				}
				len = 1;
			}
			while (j < len) {
				if (getall) {
					ind = $t.rows[j];
				}
				$("td", ind).each(function (i) {
					nm = $t.p.colModel[i].name;
					if (nm !== "cb" && nm !== "subgrid" && nm !== "rb") {
						if ($t.p.treeGrid === true && nm == $t.p.ExpandColumn) {
							res[nm] = $.jgrid.htmlDecode($("span:first", this).html());
						} else {							
								res[nm] = $(this).attr("value");
						}
					}
				});
				j++;
				if (getall) {
					resall.push(res);
					res = {};
				}
			}
		});
		return resall ? resall : res;
	
	},
	
	delRowData:function (rowid) {
		var success = false, rowInd, ia, ri;
		this.each(function () {
			var $t = this;
			rowInd = $t.rows.namedItem(rowid);
			if (!rowInd) {
				return false;
			} else {
				ri = rowInd.rowIndex;
				$(rowInd).remove();
				$t.p.records--;
				$t.p.reccount--;
				$t.updatepager(true, false);
				success = true;
				if (rowid == $t.p.selrow) {
					$t.p.selrow = null;
				}
				ia = $.inArray(rowid, $t.p.selarrrow);
				if (ia != -1) {
					$t.p.selarrrow.splice(ia, 1);
				}
			}
			if (ri == 0 && success) {
				$t.updateColumns();
			}
			if ($t.p.altRows === true && success) {
				var cn = $t.p.altclass;
				$($t.rows).each(function (i) {
					if (i % 2 == 1) {
						$(this).addClass(cn);
					} else {
						$(this).removeClass(cn);
					}
				});
			}
		});
		return success;
	}, setRowData:function (rowid, data, cssp) {
	
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
	}, addRowData:function (rowid, data, pos, src) {
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
			  var colMs = t.p.colModel;//为固定列增加样式
			   var tr = $("#"+rowid,t);
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
	}, footerData:function (action, data, format) {
		var nm, success = false, res = {};
		function isEmpty(obj) {
			for (var i in obj) {
				return false;
			}
			return true;
		}
		if (typeof (action) == "undefined") {
			action = "get";
		}
		if (typeof (format) != "boolean") {
			format = true;
		}
		action = action.toLowerCase();
		this.each(function () {
			var t = this, vl, ind;
			if (!t.grid || !t.p.footerrow) {
				return false;
			}
			if (action == "set") {
				if (isEmpty(data)) {
					return false;
				}
			}
			success = true;
			$(this.p.colModel).each(function (i) {
				nm = this.name;
				if (action == "set") {
					if (data[nm] != undefined) {
						vl = format ? t.formatter("", data[nm], i, data, "edit") : data[nm];
						$("tr.footrow td:eq(" + i + ")", t.grid.sDiv).html(vl).attr("tips", $.jgrid.stripHtml(vl));
						success = true;
					}
				} else {
					if (action == "get") {
						res[nm] = $("tr.footrow td:eq(" + i + ")", t.grid.sDiv).html();
					}
				}
			});
		});
		return action == "get" ? res : success;
	}, ShowHideCol:function (colname, show) {
		return this.each(function () {
			var $t = this, fndh = false;
			if (!$t.grid) {
				return;
			}
			if (typeof colname === "string") {
				colname = [colname];
			}
			show = show != "none" ? "" : "none";
			var sw = show == "" ? true : false;
			$(this.p.colModel).each(function (i) {
				if ($.inArray(this.name, colname) !== -1 && this.hidden === sw) {
					$("tr", $t.grid.hDiv).each(function () {
						$("th:eq(" + i + ")", this).css("display", show);
					});
					$($t.rows).each(function (j) {
						$("td:eq(" + i + ")", $t.rows[j]).css("display", show);
					});
					if ($t.p.footerrow) {
						$("td:eq(" + i + ")", $t.grid.sDiv).css("display", show);
					}
					if (show == "none") {
						$t.p.tblwidth -= this.width;
					} else {
						$t.p.tblwidth += this.width;
					}
					this.hidden = !sw;
					fndh = true;
				}
			});
			if (fndh === true) {
				$("table:first", $t.grid.hDiv).width($t.p.tblwidth);
				$("table:first", $t.grid.bDiv).width($t.p.tblwidth);
				$t.grid.hDiv.scrollLeft = $t.grid.bDiv.scrollLeft;
				if ($t.p.footerrow) {
					$("table:first", $t.grid.sDiv).width($t.p.tblwidth);
					$t.grid.sDiv.scrollLeft = $t.grid.bDiv.scrollLeft;
				}
			}
		});
	}, hideCol:function (colname) {
		return this.each(function () {
			$(this).jqGrid("ShowHideCol", colname, "none");
		});
	}, showCol:function (colname) {
		return this.each(function () {
			$(this).jqGrid("ShowHideCol", colname, "");
		});
	}, remapColumns:function (permutation, updateCells, keepHeader) {
		function resortArray(a) {
			var ac;
			if (a.length) {
				ac = $.makeArray(a);
			} else {
				ac = $.extend({}, a);
			}
			$.each(permutation, function (i) {
				a[i] = ac[this];
			});
		}
		var ts = this.get(0);
		function resortRows(parent, clobj) {
			$(">tr" + (clobj || ""), parent).each(function () {
				var row = this;
				var elems = $.makeArray(row.cells);
				$.each(permutation, function () {
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
			if (!ts.p.remapColumns.length) {
				ts.p.remapColumns = $.makeArray(permutation);
			} else {
				resortArray(ts.p.remapColumns);
			}
		}
		ts.p.lastsort = $.inArray(ts.p.lastsort, permutation);
		if (ts.p.treeGrid) {
			ts.p.expColInd = $.inArray(ts.p.expColInd, permutation);
		}
	}, setGridWidth:function (nwidth, shrink) {
		return this.each(function () {
			var $t = this, cw, initwidth = 0, brd = $t.p.cellLayout, lvc, vc = 0, hs = false, scw = $t.p.scrollOffset, aw, gw = 0, tw = 0, cl = 0, cr;
			if (!$t.grid) {
				return;
			}
			if (typeof shrink != "boolean") {
				shrink = $t.p.shrinkToFit;
			}
			if (isNaN(nwidth)) {
				return;
			} else {
				nwidth = parseInt(nwidth);
				$t.grid.width = $t.p.width = nwidth;
			}
			$("#gbox_" + $t.p.id).css("width", nwidth + "px");
			$("#gview_" + $t.p.id).css("width", nwidth + "px");
			$($t.grid.bDiv).css("width", nwidth + "px");
			$($t.grid.hDiv).css("width", nwidth + "px");
			if ($t.p.pager) {
				$($t.p.pager).css("width", nwidth + "px");
			}
			if ($t.p.toolbar[0] === true) {
				$($t.grid.uDiv).css("width", nwidth + "px");
				if ($t.p.toolbar[1] == "both") {
					$($t.grid.ubDiv).css("width", nwidth + "px");
				}
			}
			if ($t.p.footerrow) {
				$($t.grid.sDiv).css("width", nwidth + "px");
			}
			if (shrink === false && $t.p.forceFit == true) {
				$t.p.forceFit = false;
			}
			if (shrink === true) {
				if ($.browser.safari) {
					brd = 0;
				}
				$.each($t.p.colModel, function (i) {
					if (this.hidden === false) {
						initwidth += parseInt(this.width, 10);
						if (this.fixed) {
							tw += this.width;
							gw += this.width + brd;
						} else {
							vc++;
						}
						cl++;
					}
				});
				if (vc == 0) {
					return;
				}
				$t.p.tblwidth = initwidth;
				aw = nwidth - brd * vc - gw;
				if (!isNaN($t.p.height)) {
					if ($($t.grid.bDiv)[0].clientHeight < $($t.grid.bDiv)[0].scrollHeight) {
						hs = true;
						aw -= scw;
					}
				}
				initwidth = 0;
				var cle = $t.grid.cols.length > 0;
				$.each($t.p.colModel, function (i) {
					var tn = this.name;
					if (this.hidden === false && !this.fixed) {
						cw = Math.floor((aw) / ($t.p.tblwidth - tw) * this.width);
						this.width = cw;
						initwidth += cw;
						$t.grid.headers[i].width = cw;
						$t.grid.headers[i].el.style.width = cw + "px";
						if ($t.p.footerrow) {
							$t.grid.footers[i].style.width = cw + "px";
						}
						if (cle) {
							$t.grid.cols[i].style.width = cw + "px";
						}
						lvc = i;
					}
				});
				cr = 0;
				if (hs) {
					if (nwidth - gw - (initwidth + brd * vc) !== scw) {
						cr = nwidth - gw - (initwidth + brd * vc) - scw;
					}
				} else {
					if (Math.abs(nwidth - gw - (initwidth + brd * vc)) !== 1) {
						cr = nwidth - gw - (initwidth + brd * vc);
					}
				}
				$t.p.colModel[lvc].width += cr;
				cw = $t.p.colModel[lvc].width;
				$t.grid.headers[lvc].width = cw;
				$t.grid.headers[lvc].el.style.width = cw + "px";
				if (cle) {
					$t.grid.cols[lvc].style.width = cw + "px";
				}
				$t.p.tblwidth = initwidth + cr + tw + brd * cl;
				$("table:first", $t.grid.bDiv).css("width", $t.p.tblwidth + "px");
				$("table:first", $t.grid.hDiv).css("width", $t.p.tblwidth + "px");
				$t.grid.hDiv.scrollLeft = $t.grid.bDiv.scrollLeft;
				if ($t.p.footerrow) {
					$t.grid.footers[lvc].style.width = cw + "px";
					$("table:first", $t.grid.sDiv).css("width", $t.p.tblwidth + "px");
				}
			}
		});
	}, setGridHeight:function (nh) {
		return this.each(function () {
			var $t = this;
			if (!$t.grid) {
				return;
			}
			$($t.grid.bDiv).css({height:nh + (isNaN(nh) ? "" : "px")});
			$t.p.height = nh;
			if ($t.p.scroll) {
				$t.grid.populateVisible();
			}
		});
	}, setCaption:function (newcap) {
		return this.each(function () {
			this.p.caption = newcap;
			$("span.ui-jqgrid-title", this.grid.cDiv).html(newcap);
			$(this.grid.cDiv).show();
		});
	}, setLabel:function (colname, nData, prop, attrp) {
		return this.each(function () {
			var $t = this, pos = -1;
			if (!$t.grid) {
				return;
			}
			if (isNaN(colname)) {
				$($t.p.colModel).each(function (i) {
					if (this.name == colname) {
						pos = i;
						return false;
					}
				});
			} else {
				pos = parseInt(colname, 10);
			}
			if (pos >= 0) {
				var thecol = $("tr.ui-jqgrid-labels th:eq(" + pos + ")", $t.grid.hDiv);
				if (nData) {
					var ico = $(".s-ico", thecol);
					$("[id^=jqgh_]", thecol).empty().html(nData).append(ico);
					$t.p.colNames[pos] = nData;
				}
				if (prop) {
					if (typeof prop === "string") {
						$(thecol).addClass(prop);
					} else {
						$(thecol).css(prop);
					}
				}
				if (typeof attrp === "object") {
					$(thecol).attr(attrp);
				}
			}
		});
	}, setCell:function (rowid, colname, nData, cssp, attrp) {
		return this.each(function () {
			var $t = this, pos = -1, v;
			if (!$t.grid) {
				return;
			}
			if (isNaN(colname)) {
				$($t.p.colModel).each(function (i) {
					if (this.name == colname) {
						pos = i;
						return false;
					}
				});
			} else {
				pos = parseInt(colname, 10);
			}
			if (pos >= 0) {
				var ind = $t.rows.namedItem(rowid);
				if (ind) {
					var tcell = $("td:eq(" + pos + ")", ind);
					if (nData !== "") {
						v = $t.formatter(rowid, nData, pos, ind, "edit");
						$(tcell).html(v).attr("tips", $.jgrid.stripHtml(v));
					}
					if (cssp) {
						if (typeof cssp === "string") {
							$(tcell).addClass(cssp);
						} else {
							$(tcell).css(cssp);
						}
					}
					if (typeof attrp === "object") {
						$(tcell).attr(attrp);
					}
				}
			}
		});
	}, getCell:function (rowid, col) {
		var ret = false;
		this.each(function () {
			var $t = this, pos = -1;
			if (!$t.grid) {
				return;
			}
			if (isNaN(col)) {
				$($t.p.colModel).each(function (i) {
					if (this.name === col) {
						pos = i;
						return false;
					}
				});
			} else {
				pos = parseInt(col, 10);
			}
			if (pos >= 0) {
				var ind = $t.rows.namedItem(rowid);
				if (ind) {
					try {
						ret = $.unformat($("td:eq(" + pos + ")", ind), {colModel:$t.p.colModel[pos]}, pos);
					}
					catch (e) {
						ret = $.jgrid.htmlDecode($("td:eq(" + pos + ")", ind).html());
					}
				}
			}
		});
		return ret;
	}, getCol:function (col, obj, mathopr) {
		var ret = [], val, sum = 0;
		obj = typeof (obj) != "boolean" ? false : obj;
		if (typeof mathopr == "undefined") {
			mathopr = false;
		}
		this.each(function () {
			var $t = this, pos = -1;
			if (!$t.grid) {
				return;
			}
			if (isNaN(col)) {
				$($t.p.colModel).each(function (i) {
					if (this.name === col) {
						pos = i;
						return false;
					}
				});
			} else {
				pos = parseInt(col, 10);
			}
			if (pos >= 0) {
				var ln = $t.rows.length, i = 0;
				if (ln && ln > 0) {
					while (i < ln) {
						try {
							val = $.unformat($($t.rows[i].cells[pos]), {colModel:$t.p.colModel[pos]}, pos);
						}
						catch (e) {
							val = $.jgrid.htmlDecode($t.rows[i].cells[pos].innerHTML);
						}
						mathopr ? sum += parseFloat(val, 10) : obj ? ret.push({id:$t.rows[i].id, value:val}) : ret[i] = val;
						i++;
					}
					if (mathopr) {
						switch (mathopr.toLowerCase()) {
						  case "sum":
							ret = sum;
							break;
						  case "avg":
							ret = sum / ln;
							break;
						  case "count":
							ret = ln;
							break;
						}
					}
				}
			}
		});
		return ret;
	}, clearGridData:function (clearfooter) {
		return this.each(function () {
			var $t = this;
			if (!$t.grid) {
				return;
			}
			if (typeof clearfooter != "boolean") {
				clearfooter = false;
			}
			$("tbody:first tr", $t.grid.bDiv).remove();
			if ($t.p.footerrow && clearfooter) {
				$(".ui-jqgrid-ftable td", $t.grid.sDiv).html("&#160;");
			}
			$t.p.selrow = null;
			$t.p.selarrrow = [];
			$t.p.savedRow = [];
			$t.p.records = 0;
			$t.p.page = "0";
			$t.p.lastpage = "0";
			$t.p.reccount = 0;
			$t.updatepager(true, false);
		});
	}, getInd:function (rowid, rc) {
		var ret = false, rw;
		this.each(function () {
			rw = this.rows.namedItem(rowid);
			if (rw) {
				ret = rc === true ? rw : rw.rowIndex;
			}
		});
		return ret;
	}});
})(jQuery);
(function (c) {
	c.fmatter = {};
	c.fn.fmatter = function (g, h, f, d, e) {
		f = c.extend({}, c.jgrid.formatter, f);
		return a(g, h, f, d, e);
	};
	c.fmatter.util = {NumberFormat:function (f, d) {
		if (!isNumber(f)) {
			f *= 1;
		}
		if (isNumber(f)) {
			var h = (f < 0);
			var n = f + "";
			var k = (d.decimalSeparator) ? d.decimalSeparator : ".";
			var l;
			if (isNumber(d.decimalPlaces)) {
				var m = d.decimalPlaces;
				var g = Math.pow(10, m);
				n = Math.round(f * g) / g + "";
				l = n.lastIndexOf(".");
				if (m > 0) {
					if (l < 0) {
						n += k;
						l = n.length - 1;
					} else {
						if (k !== ".") {
							n = n.replace(".", k);
						}
					}
					while ((n.length - 1 - l) < m) {
						n += "0";
					}
				}
			}
			if (d.thousandsSeparator) {
				var p = d.thousandsSeparator;
				l = n.lastIndexOf(k);
				l = (l > -1) ? l : n.length;
				var o = n.substring(l);
				var e = -1;
				for (var j = l; j > 0; j--) {
					e++;
					if ((e % 3 === 0) && (j !== l) && (!h || (j > 1))) {
						o = p + o;
					}
					o = n.charAt(j - 1) + o;
				}
				n = o;
			}
			n = (d.prefix) ? d.prefix + n : n;
			n = (d.suffix) ? n + d.suffix : n;
			return n;
		} else {
			return f;
		}
	}, DateFormat:function (I, L, O, x) {
		var m = /\\.|[dDjlNSwzWFmMntLoYyaABgGhHisueIOPTZcrU]/g, C = /\b(?:[PMCEA][SDP]T|(?:Pacific|Mountain|Central|Eastern|Atlantic) (?:Standard|Daylight|Prevailing) Time|(?:GMT|UTC)(?:[-+]\d{4})?)\b/g, K = /[^-+\dA-Z]/g, J = function (j, i) {
			j = String(j);
			i = parseInt(i) || 2;
			while (j.length < i) {
				j = "0" + j;
			}
			return j;
		}, d = {m:1, d:1, y:1970, h:0, i:0, s:0}, g = 0, q, E, f, D = ["i18n"];
		D.i18n = {dayNames:x.dayNames, monthNames:x.monthNames};
		if (I in x.masks) {
			I = x.masks[I];
		}
		L = L.split(/[\\\/:_;.\t\T\s-]/);
		I = I.split(/[\\\/:_;.\t\T\s-]/);
		for (E = 0, f = I.length; E < f; E++) {
			if (I[E] == "M") {
				q = c.inArray(L[E], D.i18n.monthNames);
				if (q !== -1 && q < 12) {
					L[E] = q + 1;
				}
			}
			if (I[E] == "F") {
				q = c.inArray(L[E], D.i18n.monthNames);
				if (q !== -1 && q > 11) {
					L[E] = q + 1 - 12;
				}
			}
			d[I[E].toLowerCase()] = parseInt(L[E], 10);
		}
		d.m = parseInt(d.m) - 1;
		var M = d.y;
		if (M >= 70 && M <= 99) {
			d.y = 1900 + d.y;
		} else {
			if (M >= 0 && M <= 69) {
				d.y = 2000 + d.y;
			}
		}
		g = new Date(d.y, d.m, d.d, d.h, d.i, d.s, 0);
		if (O in x.masks) {
			O = x.masks[O];
		} else {
			if (!O) {
				O = "Y-m-d";
			}
		}
		var t = g.getHours(), H = g.getMinutes(), F = g.getDate(), B = g.getMonth() + 1, A = g.getTimezoneOffset(), y = g.getSeconds(), v = g.getMilliseconds(), r = g.getDay(), e = g.getFullYear(), l = (r + 6) % 7 + 1, p = (new Date(e, B - 1, F) - new Date(e, 0, 1)) / 86400000, h = {d:J(F), D:D.i18n.dayNames[r], j:F, l:D.i18n.dayNames[r + 7], N:l, S:x.S(F), w:r, z:p, W:l < 5 ? Math.floor((p + l - 1) / 7) + 1 : Math.floor((p + l - 1) / 7) || ((new Date(e - 1, 0, 1).getDay() + 6) % 7 < 4 ? 53 : 52), F:D.i18n.monthNames[B - 1 + 12], m:J(B), M:D.i18n.monthNames[B - 1], n:B, t:"?", L:"?", o:"?", Y:e, y:String(e).substring(2), a:t < 12 ? x.AmPm[0] : x.AmPm[1], A:t < 12 ? x.AmPm[2] : x.AmPm[3], B:"?", g:t % 12 || 12, G:t, h:J(t % 12 || 12), H:J(t), i:J(H), s:J(y), u:v, e:"?", I:"?", O:(A > 0 ? "-" : "+") + J(Math.floor(Math.abs(A) / 60) * 100 + Math.abs(A) % 60, 4), P:"?", T:(String(g).match(C) || [""]).pop().replace(K, ""), Z:"?", c:"?", r:"?", U:Math.floor(g / 1000)};
		return O.replace(m, function (i) {
			return i in h ? h[i] : i.substring(1);
		});
	}};
	c.fn.fmatter.defaultFormat = function (e, d) {
		return (isValue(e) && e !== "") ? e : d.defaultValue ? d.defaultValue : "&#160;";
	};
	c.fn.fmatter.email = function (e, d) {
		if (!isEmpty(e)) {
			return "<a href=\"mailto:" + e + "\">" + e + "</a>";
		} else {
			return c.fn.fmatter.defaultFormat(e, d);
		}
	};
	c.fn.fmatter.checkbox = function (g, e) {
		var h = c.extend({}, e.checkbox), f;
		if (!isUndefined(e.colModel.formatoptions)) {
			h = c.extend({}, h, e.colModel.formatoptions);
		}
		if (h.disabled === true) {
			f = "disabled";
		} else {
			f = "";
		}
		if (isEmpty(g) || isUndefined(g)) {
			g = c.fn.fmatter.defaultFormat(g, h);
		}
		g = g + "";
		g = g.toLowerCase();
		var d = g.search(/(false|0|no|off)/i) < 0 ? " checked='checked' " : "";
		return "<input type=\"checkbox\" " + d + " value=\"" + g + "\" offval=\"no\" " + f + "/>";
	}, c.fn.fmatter.link = function (f, d) {
		var g = {target:d.target};
		var e = "";
		if (!isUndefined(d.colModel.formatoptions)) {
			g = c.extend({}, g, d.colModel.formatoptions);
		}
		if (g.target) {
			e = "target=" + g.target;
		}
		if (!isEmpty(f)) {
			return "<a " + e + " href=\"" + f + "\">" + f + "</a>";
		} else {
			return c.fn.fmatter.defaultFormat(f, d);
		}
	};
	c.fn.fmatter.showlink = function (f, d) {
		var g = {baseLinkUrl:d.baseLinkUrl, showAction:d.showAction, addParam:d.addParam || "", target:d.target, idName:d.idName}, e = "";
		if (!isUndefined(d.colModel.formatoptions)) {
			g = c.extend({}, g, d.colModel.formatoptions);
		}
		if (g.target) {
			e = "target=" + g.target;
		}
		idUrl = g.baseLinkUrl + g.showAction + "?" + g.idName + "=" + d.rowId + g.addParam;
		if (isString(f)) {
			return "<a " + e + " href=\"" + idUrl + "\">" + f + "</a>";
		} else {
			return c.fn.fmatter.defaultFormat(f, d);
		}
	};
	c.fn.fmatter.integer = function (e, d) {
		var f = c.extend({}, d.integer);
		if (!isUndefined(d.colModel.formatoptions)) {
			f = c.extend({}, f, d.colModel.formatoptions);
		}
		if (isEmpty(e)) {
			return f.defaultValue;
		}
		return c.fmatter.util.NumberFormat(e, f);
	};
	c.fn.fmatter.number = function (e, d) {
		var f = c.extend({}, d.number);
		if (!isUndefined(d.colModel.formatoptions)) {
			f = c.extend({}, f, d.colModel.formatoptions);
		}
		if (isEmpty(e)) {
			return f.defaultValue;
		}
		return c.fmatter.util.NumberFormat(e, f);
	};
	c.fn.fmatter.currency = function (e, d) {
		var f = c.extend({}, d.currency);
		if (!isUndefined(d.colModel.formatoptions)) {
			f = c.extend({}, f, d.colModel.formatoptions);
		}
		if (isEmpty(e)) {
			return f.defaultValue;
		}
		return c.fmatter.util.NumberFormat(e, f);
	};
	c.fn.fmatter.date = function (g, f, d, e) {
		var h = c.extend({}, f.date);
		if (!isUndefined(f.colModel.formatoptions)) {
			h = c.extend({}, h, f.colModel.formatoptions);
		}
		if (!h.reformatAfterEdit && e == "edit") {
			return c.fn.fmatter.defaultFormat(g, f);
		} else {
			if (!isEmpty(g)) {
				return c.fmatter.util.DateFormat(h.srcformat, g, h.newformat, h);
			} else {
				return c.fn.fmatter.defaultFormat(g, f);
			}
		}
	};
	c.fn.fmatter.select = function (k, d, e, n) {
		k = k + "";
		var g = false, m = [];
		if (!isUndefined(d.colModel.editoptions)) {
			g = d.colModel.editoptions.value;
		}
		if (g) {
			var q = d.colModel.editoptions.multiple === true ? true : false, p = [], o;
			if (q) {
				p = k.split(",");
				p = c.map(p, function (i) {
					return c.trim(i);
				});
			}
			if (isString(g)) {
				var f = g.split(";"), h = 0;
				for (var l = 0; l < f.length; l++) {
					o = f[l].split(":");
					if (q) {
						if (jQuery.inArray(o[0], p) > -1) {
							m[h] = o[1];
							h++;
						}
					} else {
						if (c.trim(o[0]) == c.trim(k)) {
							m[0] = o[1];
							break;
						}
					}
				}
			} else {
				if (isObject(g)) {
					if (q) {
						m = jQuery.map(p, function (r, j) {
							return g[r];
						});
					} else {
						m[0] = g[k] || "";
					}
				}
			}
		}
		k = m.join(", ");
		return k == "" ? c.fn.fmatter.defaultFormat(k, d) : k;
	};
	c.unformat = function (g, n, k, e) {
		var j, h = n.colModel.formatter, i = n.colModel.formatoptions || {}, o, m = /([\.\*\_\'\(\)\{\}\+\?\\])/g;
		unformatFunc = n.colModel.unformat || (c.fn.fmatter[h] && c.fn.fmatter[h].unformat);
		if (typeof unformatFunc !== "undefined" && isFunction(unformatFunc)) {
			j = unformatFunc(c(g).text(), n, g);
		} else {
			if (typeof h !== "undefined" && isString(h)) {
				var d = c.jgrid.formatter || {}, l;
				switch (h) {
				  case "integer":
					i = c.extend({}, d.integer, i);
					o = i.thousandsSeparator.replace(m, "\\$1");
					l = new RegExp(o, "g");
					j = c(g).text().replace(l, "");
					break;
				  case "number":
					i = c.extend({}, d.number, i);
					o = i.thousandsSeparator.replace(m, "\\$1");
					l = new RegExp(o, "g");
					j = c(g).text().replace(l, "").replace(i.decimalSeparator, ".");
					break;
				  case "currency":
					i = c.extend({}, d.currency, i);
					o = i.thousandsSeparator.replace(m, "\\$1");
					l = new RegExp(o, "g");
					j = c(g).text().replace(l, "").replace(i.decimalSeparator, ".").replace(i.prefix, "").replace(i.suffix, "");
					break;
				  case "checkbox":
					var f = (n.colModel.editoptions) ? n.colModel.editoptions.value.split(":") : ["Yes", "No"];
					j = c("input", g).attr("checked") ? f[0] : f[1];
					break;
				  case "select":
					j = c.unformat.select(g, n, k, e);
					break;
				  default:
					j = c(g).text();
					break;
				}
			}
		}
		return j ? j : e === true ? c(g).text() : c.jgrid.htmlDecode(c(g).html());
	};
	c.unformat.select = function (h, s, n, e) {
		var m = [];
		var q = c(h).text();
		if (e == true) {
			return q;
		}
		var l = c.extend({}, s.colModel.editoptions);
		if (l.value) {
			var f = l.value, r = l.multiple === true ? true : false, p = [], o;
			if (r) {
				p = q.split(",");
				p = c.map(p, function (i) {
					return c.trim(i);
				});
			}
			if (isString(f)) {
				var d = f.split(";"), g = 0;
				for (var k = 0; k < d.length; k++) {
					o = d[k].split(":");
					if (r) {
						if (jQuery.inArray(o[1], p) > -1) {
							m[g] = o[0];
							g++;
						}
					} else {
						if (c.trim(o[1]) == c.trim(q)) {
							m[0] = o[0];
							break;
						}
					}
				}
			} else {
				if (isObject(f)) {
					if (!r) {
						p[0] = q;
					}
					m = jQuery.map(p, function (j) {
						var i;
						c.each(f, function (t, u) {
							if (u == j) {
								i = t;
								return false;
							}
						});
						if (i) {
							return i;
						}
					});
				}
			}
			return m.join(", ");
		} else {
			return q || "";
		}
	};
	function a(h, i, g, d, e) {
		var f = i;
		if (c.fn.fmatter[h]) {
			f = c.fn.fmatter[h](i, g, d, e);
		}
		return f;
	}
	function b(d) {
		if (window.console && window.console.log) {
			window.console.log(d);
		}
	}
	isValue = function (d) {
		return (isObject(d) || isString(d) || isNumber(d) || isBoolean(d));
	};
	isBoolean = function (d) {
		return typeof d === "boolean";
	};
	isNull = function (d) {
		return d === null;
	};
	isNumber = function (d) {
		return typeof d === "number" && isFinite(d);
	};
	isString = function (d) {
		return typeof d === "string";
	};
	isEmpty = function (d) {
		if (!isString(d) && isValue(d)) {
			return false;
		} else {
			if (!isValue(d)) {
				return true;
			}
		}
		d = c.trim(d).replace(/\&nbsp\;/ig, "").replace(/\&#160\;/ig, "");
		return d === "";
	};
	isUndefined = function (d) {
		return typeof d === "undefined";
	};
	isObject = function (d) {
		return (d && (typeof d === "object" || isFunction(d))) || false;
	};
	isFunction = function (d) {
		return typeof d === "function";
	};
})(jQuery);
(function (a) {
	a.jgrid.extend({getColProp:function (d) {
		var b = {}, f = this[0];
		if (!f.grid) {
			return;
		}
		var e = f.p.colModel;
		for (var c = 0; c < e.length; c++) {
			if (e[c].name == d) {
				b = e[c];
				break;
			}
		}
		return b;
	}, setColProp:function (c, b) {
		return this.each(function () {
			if (this.grid) {
				if (b) {
					var e = this.p.colModel;
					for (var d = 0; d < e.length; d++) {
						if (e[d].name == c) {
							a.extend(this.p.colModel[d], b);
							break;
						}
					}
				}
			}
		});
	}, sortGrid:function (c, b) {
		return this.each(function () {
			var g = this, d = -1;
			if (!g.grid) {
				return;
			}
			if (!c) {
				c = g.p.sortname;
			}
			for (var f = 0; f < g.p.colModel.length; f++) {
				if (g.p.colModel[f].index == c || g.p.colModel[f].name == c) {
					d = f;
					break;
				}
			}
			if (d != -1) {
				var e = g.p.colModel[d].sortable;
				if (typeof e !== "boolean") {
					e = true;
				}
				if (typeof b !== "boolean") {
					b = false;
				}
				if (e) {
					g.sortData("jqgh_" + c, d, b);
				}
			}
		});
	}, GridDestroy:function () {
		return this.each(function () {
			if (this.grid) {
				if (this.p.pager) {
					a(this.p.pager).remove();
				}
				var c = this.id;
				try {
					a("#gbox_" + c).remove();
				}
				catch (b) {
				}
			}
		});
	}, GridUnload:function () {
		return this.each(function () {
			if (!this.grid) {
				return;
			}
			var d = {id:a(this).attr("id"), cl:a(this).attr("class")};
			if (this.p.pager) {
				a(this.p.pager).empty().removeClass("ui-state-default ui-jqgrid-pager corner-bottom");
			}
			var b = document.createElement("table");
			a(b).attr({id:d.id});
			b.className = d.cl;
			var c = this.id;
			a(b).removeClass("ui-jqgrid-btable");
			if (a(this.p.pager).parents("#gbox_" + c).length === 1) {
				a(b).insertBefore("#gbox_" + c).show();
				a(this.p.pager).insertBefore("#gbox_" + c);
			} else {
				a(b).insertBefore("#gbox_" + c).show();
			}
			a("#gbox_" + c).remove();
		});
	}, setGridState:function (b) {
		return this.each(function () {
			if (!this.grid) {
				return;
			}
			$t = this;
			if (b == "hidden") {
				a(".ui-jqgrid-bdiv, .ui-jqgrid-hdiv", "#gview_" + $t.p.id).slideUp("fast");
				if ($t.p.pager) {
					a($t.p.pager).slideUp("fast");
				}
				if ($t.p.toolbar[0] === true) {
					if ($t.p.toolbar[1] == "both") {
						a($t.grid.ubDiv).slideUp("fast");
					}
					a($t.grid.uDiv).slideUp("fast");
				}
				if ($t.p.footerrow) {
					a(".ui-jqgrid-sdiv", "#gbox_" + $s.p.id).slideUp("fast");
				}
				a(".ui-jqgrid-titlebar-close span", $t.grid.cDiv).removeClass("ui-icon-circle-triangle-n").addClass("ui-icon-circle-triangle-s");
				$t.p.gridstate = "hidden";
			} else {
				if (b == "visible") {
					a(".ui-jqgrid-hdiv, .ui-jqgrid-bdiv", "#gview_" + $t.p.id).slideDown("fast");
					if ($t.p.pager) {
						a($t.p.pager).slideDown("fast");
					}
					if ($t.p.toolbar[0] === true) {
						if ($t.p.toolbar[1] == "both") {
							a($t.grid.ubDiv).slideDown("fast");
						}
						a($t.grid.uDiv).slideDown("fast");
					}
					if ($t.p.footerrow) {
						a(".ui-jqgrid-sdiv", "#gbox_" + $t.p.id).slideDown("fast");
					}
					a(".ui-jqgrid-titlebar-close span", $t.grid.cDiv).removeClass("ui-icon-circle-triangle-s").addClass("ui-icon-circle-triangle-n");
					$t.p.gridstate = "visible";
				}
			}
		});
	}, updateGridRows:function (e, c, d) {
		var b, f = false;
		this.each(function () {
			var h = this, j, k, i, g;
			if (!h.grid) {
				return false;
			}
			if (!c) {
				c = "id";
			}
			if (e && e.length > 0) {
				a(e).each(function (m) {
					i = this;
					k = h.rows.namedItem(i[c]);
					if (k) {
						g = i[c];
						if (d === true) {
							if (h.p.jsonReader.repeatitems === true) {
								if (h.p.jsonReader.cell) {
									i = i[h.p.jsonReader.cell];
								}
								for (var l = 0; l < i.length; l++) {
									j = h.formatter(g, i[l], l, i, "edit");
									if (h.p.treeGrid === true && b == h.p.ExpandColumn) {
										a("td:eq(" + l + ") > span:first", k).html(j).attr("tips", a.jgrid.stripHtml(j));
									} else {
										a("td:eq(" + l + ")", k).html(j).attr("tips", a.jgrid.stripHtml(j));
									}
								}
								f = true;
								return true;
							}
						}
						a(h.p.colModel).each(function (n) {
							b = d === true ? this.jsonmap || this.name : this.name;
							if (i[b] != undefined) {
								j = h.formatter(g, i[b], n, i, "edit");
								if (h.p.treeGrid === true && b == h.p.ExpandColumn) {
									a("td:eq(" + n + ") > span:first", k).html(j).attr("tips", a.jgrid.stripHtml(j));
								} else {
									a("td:eq(" + n + ")", k).html(j).attr("tips", a.jgrid.stripHtml(j));
								}
								f = true;
							}
						});
					}
				});
			}
		});
		return f;
	}, filterGrid:function (c, b) {
		b = a.extend({gridModel:false, gridNames:false, gridToolbar:false, filterModel:[], formtype:"horizontal", autosearch:true, formclass:"filterform", tableclass:"filtertable", buttonclass:"filterbutton", searchButton:"Search", clearButton:"Clear", enableSearch:false, enableClear:false, beforeSearch:null, afterSearch:null, beforeClear:null, afterClear:null, url:"", marksearched:true}, b || {});
		return this.each(function () {
			var l = this;
			this.p = b;
			if (this.p.filterModel.length == 0 && this.p.gridModel === false) {
				alert("No filter is set");
				return;
			}
			if (!c) {
				alert("No target grid is set!");
				return;
			}
			this.p.gridid = c.indexOf("#") != -1 ? c : "#" + c;
			var d = a(this.p.gridid).jqGrid("getGridParam", "colModel");
			if (d) {
				if (this.p.gridModel === true) {
					var e = a(this.p.gridid)[0];
					var g;
					a.each(d, function (o, p) {
						var m = [];
						this.search = this.search === false ? false : true;
						if (this.editrules && this.editrules.searchhidden === true) {
							g = true;
						} else {
							if (this.hidden === true) {
								g = false;
							} else {
								g = true;
							}
						}
						if (this.search === true && g === true) {
							if (l.p.gridNames === true) {
								m.label = e.p.colNames[o];
							} else {
								m.label = "";
							}
							m.name = this.name;
							m.index = this.index || this.name;
							m.stype = this.edittype || "text";
							if (m.stype != "select") {
								m.stype = "text";
							}
							m.defval = this.defval || "";
							m.surl = this.surl || "";
							m.sopt = this.editoptions || {};
							m.width = this.width;
							l.p.filterModel.push(m);
						}
					});
				} else {
					a.each(l.p.filterModel, function (o, p) {
						for (var m = 0; m < d.length; m++) {
							if (this.name == d[m].name) {
								this.index = d[m].index || this.name;
								break;
							}
						}
						if (!this.index) {
							this.index = this.name;
						}
					});
				}
			} else {
				alert("Could not get grid colModel");
				return;
			}
			var h = function () {
				var q = {}, p = 0, n;
				var o = a(l.p.gridid)[0], m;
				o.p.searchdata = {};
				if (a.isFunction(l.p.beforeSearch)) {
					l.p.beforeSearch();
				}
				a.each(l.p.filterModel, function (t, v) {
					m = this.index;
					switch (this.stype) {
					  case "select":
						n = a("select[name=" + m + "]", l).val();
						if (n) {
							q[m] = n;
							if (l.p.marksearched) {
								a("#jqgh_" + this.name, o.grid.hDiv).addClass("dirty-cell");
							}
							p++;
						} else {
							if (l.p.marksearched) {
								a("#jqgh_" + this.name, o.grid.hDiv).removeClass("dirty-cell");
							}
							try {
								delete o.p.postData[this.index];
							}
							catch (u) {
							}
						}
						break;
					  default:
						n = a("input[name=" + m + "]", l).val();
						if (n) {
							q[m] = n;
							if (l.p.marksearched) {
								a("#jqgh_" + this.name, o.grid.hDiv).addClass("dirty-cell");
							}
							p++;
						} else {
							if (l.p.marksearched) {
								a("#jqgh_" + this.name, o.grid.hDiv).removeClass("dirty-cell");
							}
							try {
								delete o.p.postData[this.index];
							}
							catch (u) {
							}
						}
					}
				});
				var r = p > 0 ? true : false;
				a.extend(o.p.postData, q);
				var s;
				if (l.p.url) {
					s = a(o).jqGrid("getGridParam", "url");
					a(o).jqGrid("setGridParam", {url:l.p.url});
				}
				a(o).jqGrid("setGridParam", {search:r}).trigger("reloadGrid", [{page:1}]);
				if (s) {
					a(o).jqGrid("setGridParam", {url:s});
				}
				if (a.isFunction(l.p.afterSearch)) {
					l.p.afterSearch();
				}
			};
			var k = function () {
				var q = {}, n, p = 0;
				var o = a(l.p.gridid)[0], m;
				if (a.isFunction(l.p.beforeClear)) {
					l.p.beforeClear();
				}
				a.each(l.p.filterModel, function (t, w) {
					m = this.index;
					n = (this.defval) ? this.defval : "";
					if (!this.stype) {
						this.stype == "text";
					}
					switch (this.stype) {
					  case "select":
						var v;
						a("select[name=" + m + "] option", l).each(function (x) {
							if (x == 0) {
								this.selected = true;
							}
							if (a(this).text() == n) {
								this.selected = true;
								v = a(this).val();
								return false;
							}
						});
						if (v) {
							q[m] = v;
							if (l.p.marksearched) {
								a("#jqgh_" + this.name, o.grid.hDiv).addClass("dirty-cell");
							}
							p++;
						} else {
							if (l.p.marksearched) {
								a("#jqgh_" + this.name, o.grid.hDiv).removeClass("dirty-cell");
							}
							try {
								delete o.p.postData[this.index];
							}
							catch (u) {
							}
						}
						break;
					  case "text":
						a("input[name=" + m + "]", l).val(n);
						if (n) {
							q[m] = n;
							if (l.p.marksearched) {
								a("#jqgh_" + this.name, o.grid.hDiv).addClass("dirty-cell");
							}
							p++;
						} else {
							if (l.p.marksearched) {
								a("#jqgh_" + this.name, o.grid.hDiv).removeClass("dirty-cell");
							}
							try {
								delete o.p.postData[this.index];
							}
							catch (u) {
							}
						}
						break;
					}
				});
				var r = p > 0 ? true : false;
				a.extend(o.p.postData, q);
				var s;
				if (l.p.url) {
					s = a(o).jqGrid("getGridParam", "url");
					a(o).jqGrid("setGridParam", {url:l.p.url});
				}
				a(o).jqGrid("setGridParam", {search:r}).trigger("reloadGrid", [{page:1}]);
				if (s) {
					a(o).jqGrid("setGridParam", {url:s});
				}
				if (a.isFunction(l.p.afterClear)) {
					l.p.afterClear();
				}
			};
			var i = function () {
				var q = document.createElement("tr");
				var n, s, m, o, r, p;
				if (l.p.formtype == "horizontal") {
					a(f).append(q);
				}
				a.each(l.p.filterModel, function (A, v) {
					o = document.createElement("td");
					a(o).append("<label for='" + this.name + "'>" + this.label + "</label>");
					r = document.createElement("td");
					var z = this;
					if (!this.stype) {
						this.stype = "text";
					}
					switch (this.stype) {
					  case "select":
						if (this.surl) {
							a(r).load(this.surl, function () {
								if (z.defval) {
									a("select", this).val(z.defval);
								}
								a("select", this).attr({name:z.index || z.name, id:"sg_" + z.name});
								if (z.sopt) {
									a("select", this).attr(z.sopt);
								}
								if (l.p.gridToolbar === true && z.width) {
									a("select", this).width(z.width);
								}
								if (l.p.autosearch === true) {
									a("select", this).change(function (E) {
										h();
										return false;
									});
								}
							});
						} else {
							if (z.sopt.value) {
								var t = z.sopt.value;
								var w = document.createElement("select");
								a(w).attr({name:z.index || z.name, id:"sg_" + z.name}).attr(z.sopt);
								if (typeof t === "string") {
									var u = t.split(";"), D, x;
									for (var y = 0; y < u.length; y++) {
										D = u[y].split(":");
										x = document.createElement("option");
										x.value = D[0];
										x.innerHTML = D[1];
										if (D[1] == z.defval) {
											x.selected = "selected";
										}
										w.appendChild(x);
									}
								} else {
									if (typeof t === "object") {
										for (var C in t) {
											A++;
											x = document.createElement("option");
											x.value = C;
											x.innerHTML = t[C];
											if (t[C] == z.defval) {
												x.selected = "selected";
											}
											w.appendChild(x);
										}
									}
								}
								if (l.p.gridToolbar === true && z.width) {
									a(w).width(z.width);
								}
								a(r).append(w);
								if (l.p.autosearch === true) {
									a(w).change(function (E) {
										h();
										return false;
									});
								}
							}
						}
						break;
					  case "text":
						var B = this.defval ? this.defval : "";
						a(r).append("<input type='text' name='" + (this.index || this.name) + "' id='sg_" + this.name + "' value='" + B + "'/>");
						if (z.sopt) {
							a("input", r).attr(z.sopt);
						}
						if (l.p.gridToolbar === true && z.width) {
							if (a.browser.msie) {
								a("input", r).width(z.width - 4);
							} else {
								a("input", r).width(z.width - 2);
							}
						}
						if (l.p.autosearch === true) {
							a("input", r).keypress(function (F) {
								var E = F.charCode ? F.charCode : F.keyCode ? F.keyCode : 0;
								if (E == 13) {
									h();
									return false;
								}
								return this;
							});
						}
						break;
					}
					if (l.p.formtype == "horizontal") {
						if (l.p.gridToolbar === true && l.p.gridNames === false) {
							a(q).append(r);
						} else {
							a(q).append(o).append(r);
						}
						a(q).append(r);
					} else {
						n = document.createElement("tr");
						a(n).append(o).append(r);
						a(f).append(n);
					}
				});
				r = document.createElement("td");
				if (l.p.enableSearch === true) {
					s = "<input type='button' id='sButton' class='" + l.p.buttonclass + "' value='" + l.p.searchButton + "'/>";
					a(r).append(s);
					a("input#sButton", r).click(function () {
						h();
						return false;
					});
				}
				if (l.p.enableClear === true) {
					m = "<input type='button' id='cButton' class='" + l.p.buttonclass + "' value='" + l.p.clearButton + "'/>";
					a(r).append(m);
					a("input#cButton", r).click(function () {
						k();
						return false;
					});
				}
				if (l.p.enableClear === true || l.p.enableSearch === true) {
					if (l.p.formtype == "horizontal") {
						a(q).append(r);
					} else {
						n = document.createElement("tr");
						a(n).append("<td>&#160;</td>").append(r);
						a(f).append(n);
					}
				}
			};
			var j = a("<form name='SearchForm' style=display:inline;' class='" + this.p.formclass + "'></form>");
			var f = a("<table class='" + this.p.tableclass + "' cellspacing='0' cellpading='0' border='0'><tbody></tbody></table>");
			a(j).append(f);
			i();
			a(this).append(j);
			this.triggerSearch = h;
			this.clearSearch = k;
		});
	}, filterToolbar:function (b) {
		b = a.extend({autosearch:true, beforeSearch:null, afterSearch:null, beforeClear:null, afterClear:null, searchurl:""}, b || {});
		return this.each(function () {
			var g = this;
			var c = function () {
				var o = {}, n = 0, m, l;
				g.p.searchdata = {};
				a.each(g.p.colModel, function (s, u) {
					l = this.index || this.name;
					switch (this.stype) {
					  case "select":
						m = a("select[name=" + l + "]", g.grid.hDiv).val();
						if (m) {
							o[l] = m;
							n++;
						} else {
							try {
								delete g.p.postData[l];
							}
							catch (t) {
							}
						}
						break;
					  case "text":
						m = a("input[name=" + l + "]", g.grid.hDiv).val();
						if (m) {
							o[l] = m;
							n++;
						} else {
							try {
								delete g.p.postData[l];
							}
							catch (t) {
							}
						}
						break;
					}
				});
				var p = n > 0 ? true : false;
				a.extend(g.p.postData, o);
				var r;
				if (g.p.searchurl) {
					r = g.p.url;
					a(g).jqGrid("setGridParam", {url:g.p.searchurl});
				}
				var q = false;
				if (a.isFunction(b.beforeSearch)) {
					q = b.beforeSearch.call(g);
				}
				if (!q) {
					a(g).jqGrid("setGridParam", {search:p}).trigger("reloadGrid", [{page:1}]);
				}
				if (r) {
					a(g).jqGrid("setGridParam", {url:r});
				}
				if (a.isFunction(b.afterSearch)) {
					b.afterSearch();
				}
			};
			var j = function () {
				var o = {}, m, n = 0, l;
				a.each(g.p.colModel, function (s, v) {
					m = (this.searchoptions && this.searchoptions.defaultValue) ? this.searchoptions.defaultValue : "";
					l = this.index || this.name;
					switch (this.stype) {
					  case "select":
						var u;
						a("select[name=" + l + "] option", g.grid.hDiv).each(function (w) {
							if (w == 0) {
								this.selected = true;
							}
							if (a(this).text() == m) {
								this.selected = true;
								u = a(this).val();
								return false;
							}
						});
						if (u) {
							o[l] = u;
							n++;
						} else {
							try {
								delete g.p.postData[l];
							}
							catch (t) {
							}
						}
						break;
					  case "text":
						a("input[name=" + l + "]", g.grid.hDiv).val(m);
						if (m) {
							o[l] = m;
							n++;
						} else {
							try {
								delete g.p.postData[l];
							}
							catch (t) {
							}
						}
						break;
					}
				});
				var q = n > 0 ? true : false;
				a.extend(g.p.postData, o);
				var r;
				if (g.p.searchurl) {
					r = g.p.url;
					a(g).jqGrid("setGridParam", {url:g.p.searchurl});
				}
				var p = false;
				if (a.isFunction(b.beforeClear)) {
					p = b.beforeClear.call(g);
				}
				if (!p) {
					a(g).jqGrid("setGridParam", {search:q}).trigger("reloadGrid", [{page:1}]);
				}
				if (r) {
					a(g).jqGrid("setGridParam", {url:r});
				}
				if (a.isFunction(b.afterClear)) {
					b.afterClear();
				}
			};
			var k = function () {
				var l = a("tr.ui-search-toolbar", g.grid.hDiv);
				if (l.css("display") == "none") {
					l.show();
				} else {
					l.hide();
				}
			};
			function f(l, n) {
				var m = a(l);
				if (m[0] != null) {
					jQuery.each(n, function () {
						if (this.data != null) {
							m.bind(this.type, this.data, this.fn);
						} else {
							m.bind(this.type, this.fn);
						}
					});
				}
			}
			var h = a("<tr class='ui-search-toolbar' role='rowheader'></tr>"), d, i, e;
			a.each(g.p.colModel, function (t, p) {
				var v = this;
				d = a("<th role='columnheader' class='ui-state-default ui-th-column ui-th-" + g.p.direction + "'></th>");
				i = a("<div style='width:100%;position:relative;height:100%;padding-right:0.3em;'></div>");
				if (this.hidden === true) {
					a(d).css("display", "none");
				}
				this.search = this.search === false ? false : true;
				if (typeof this.stype == "undefined") {
					this.stype = "text";
				}
				e = a.extend({}, this.searchoptions || {});
				if (this.search) {
					switch (this.stype) {
					  case "select":
						var l = this.surl || e.dataUrl;
						if (l) {
							var y = i;
							a.ajax(a.extend({url:l, dataType:"html", complete:function (z, n) {
								if (e.buildSelect != null) {
									var A = e.buildSelect(z);
									if (A) {
										a(y).append(A);
									}
								} else {
									a(y).append(z.responseText);
								}
								if (e.defaultValue) {
									a("select", y).val(e.defaultValue);
								}
								a("select", y).attr({name:v.index || v.name, id:"gs_" + v.name});
								if (e.attr) {
									a("select", y).attr(e.attr);
								}
								a("select", y).css({width:"100%"});
								if (e.dataInit != null) {
									e.dataInit(a("select", y)[0]);
								}
								if (e.dataEvents != null) {
									f(a("select", y)[0], e.dataEvents);
								}
								if (b.autosearch === true) {
									a("select", y).change(function (B) {
										c();
										return false;
									});
								}
							}}, a.jgrid.ajaxOptions, g.p.ajaxSelectOptions || {}));
						} else {
							var m;
							if (v.searchoptions && v.searchoptions.value) {
								m = v.searchoptions.value;
							} else {
								if (v.editoptions && v.editoptions.value) {
									m = v.editoptions.value;
								}
							}
							if (m) {
								var r = document.createElement("select");
								r.style.width = "100%";
								a(r).attr({name:v.index || v.name, id:"gs_" + v.name});
								if (typeof m === "string") {
									var o = m.split(";"), x, q;
									for (var s = 0; s < o.length; s++) {
										x = o[s].split(":");
										q = document.createElement("option");
										q.value = x[0];
										q.innerHTML = x[1];
										r.appendChild(q);
									}
								} else {
									if (typeof m === "object") {
										for (var w in m) {
											q = document.createElement("option");
											q.value = w;
											q.innerHTML = m[w];
											r.appendChild(q);
										}
									}
								}
								if (e.defaultValue) {
									a(r).val(e.defaultValue);
								}
								if (e.attr) {
									a(r).attr(e.attr);
								}
								if (e.dataInit != null) {
									e.dataInit(r);
								}
								if (e.dataEvents != null) {
									f(r, e.dataEvents);
								}
								a(i).append(r);
								if (b.autosearch === true) {
									a(r).change(function (n) {
										c();
										return false;
									});
								}
							}
						}
						break;
					  case "text":
						var u = e.defaultValue ? e.defaultValue : "";
						a(i).append("<input type='text' style='width:95%;padding:0px;' name='" + (v.index || v.name) + "' id='gs_" + v.name + "' value='" + u + "'/>");
						if (e.attr) {
							a("input", i).attr(e.attr);
						}
						if (e.dataInit != null) {
							e.dataInit(a("input", i)[0]);
						}
						if (e.dataEvents != null) {
							f(a("input", i)[0], e.dataEvents);
						}
						if (b.autosearch === true) {
							a("input", i).keypress(function (z) {
								var n = z.charCode ? z.charCode : z.keyCode ? z.keyCode : 0;
								if (n == 13) {
									c();
									return false;
								}
								return this;
							});
						}
						break;
					}
				}
				a(d).append(i);
				a(h).append(d);
			});
			a("table thead", g.grid.hDiv).append(h);
			this.triggerToolbar = c;
			this.clearToolbar = j;
			this.toggleToolbar = k;
		});
	}});
})(jQuery);
var showModal = function (a) {
	a.w.show();
};
var closeModal = function (a) {
	a.w.hide().attr("aria-hidden", "true");
	if (a.o) {
		a.o.remove();
	}
};
var createModal = function (l, g, b, n, q, o) {
	var k = document.createElement("div"), a;
	a = jQuery(b.gbox).attr("dir") == "rtl" ? true : false;
	k.className = "ui-widget ui-widget-content ui-corner-all ui-jqdialog";
	k.id = l.themodal;
	var d = document.createElement("div");
	d.className = "ui-jqdialog-titlebar ui-widget-header ui-corner-all ui-helper-clearfix";
	d.id = l.modalhead;
	jQuery(d).append("<span class='ui-jqdialog-title'>" + b.caption + "</span>");
	var m = jQuery("<a href='javascript:void(0)' class='ui-jqdialog-titlebar-close ui-corner-all'></a>").hover(function () {
		m.addClass("ui-state-hover");
	}, function () {
		m.removeClass("ui-state-hover");
	}).append("<span class='ui-icon ui-icon-closethick'></span>");
	jQuery(d).append(m);
	if (a) {
		k.dir = "rtl";
		jQuery(".ui-jqdialog-title", d).css("float", "right");
		jQuery(".ui-jqdialog-titlebar-close", d).css("left", 0.3 + "em");
	} else {
		k.dir = "ltr";
		jQuery(".ui-jqdialog-title", d).css("float", "left");
		jQuery(".ui-jqdialog-titlebar-close", d).css("right", 0.3 + "em");
	}
	var i = document.createElement("div");
	jQuery(i).addClass("ui-jqdialog-content ui-widget-content").attr("id", l.modalcontent);
	jQuery(i).append(g);
	k.appendChild(i);
	jQuery(k).prepend(d);
	if (o === true) {
		jQuery("body").append(k);
	} else {
		jQuery(k).insertBefore(n);
	}
	if (typeof b.jqModal === "undefined") {
		b.jqModal = true;
	}
	var c = {};
	if (jQuery.fn.jqm && b.jqModal === true) {
		if (b.left == 0 && b.top == 0) {
			var h = [];
			h = findPos(q);
			b.left = h[0] + 4;
			b.top = h[1] + 4;
		}
		c.top = b.top + "px";
		c.left = b.left;
	} else {
		if (b.left != 0 || b.top != 0) {
			c.left = b.left;
			c.top = b.top + "px";
		}
	}
	jQuery("a.ui-jqdialog-titlebar-close", d).click(function (r) {
		var p = jQuery("#" + l.themodal).data("onClose") || b.onClose;
		var s = jQuery("#" + l.themodal).data("gbox") || b.gbox;
		hideModal("#" + l.themodal, {gb:s, jqm:b.jqModal, onClose:p});
		return false;
	});
	if (b.width == 0 || !b.width) {
		b.width = 300;
	}
	if (b.height == 0 || !b.height) {
		b.height = 200;
	}
	if (!b.zIndex) {
		b.zIndex = 950;
	}
	var j = 0;
	if (a && c.left && !o) {
		j = jQuery(b.gbox).width() - (!isNaN(b.width) ? parseInt(b.width) : 0) - 8;
		c.left = parseInt(c.left) + parseInt(j);
	}
	if (c.left) {
		c.left += "px";
	}
	jQuery(k).css(jQuery.extend({width:isNaN(b.width) ? "auto" : b.width + "px", height:isNaN(b.height) ? "auto" : b.height + "px", zIndex:b.zIndex, overflow:"hidden"}, c)).attr({tabIndex:"-1", role:"dialog", "aria-labelledby":l.modalhead, "aria-hidden":"true"});
	if (typeof b.drag == "undefined") {
		b.drag = true;
	}
	if (typeof b.resize == "undefined") {
		b.resize = true;
	}
	if (b.drag) {
		jQuery(d).css("cursor", "move");
		if (jQuery.fn.jqDrag) {
			jQuery(k).jqDrag(d);
		} else {
			try {
				jQuery(k).draggable({handle:jQuery("#" + d.id)});
			}
			catch (f) {
			}
		}
	}
	if (b.resize) {
		if (jQuery.fn.jqResize) {
			jQuery(k).append("<div class='jqResize ui-resizable-handle ui-resizable-se ui-icon ui-icon-gripsmall-diagonal-se ui-icon-grip-diagonal-se'></div>");
			jQuery("#" + l.themodal).jqResize(".jqResize", l.scrollelm ? "#" + l.scrollelm : false);
		} else {
			try {
				jQuery(k).resizable({handles:"se, sw", alsoResize:l.scrollelm ? "#" + l.scrollelm : false});
			}
			catch (f) {
			}
		}
	}
	if (b.closeOnEscape === true) {
		jQuery(k).keydown(function (r) {
			if (r.which == 27) {
				var p = jQuery("#" + l.themodal).data("onClose") || b.onClose;
				hideModal(this, {gb:b.gbox, jqm:b.jqModal, onClose:p});
			}
		});
	}
};
var viewModal = function (a, c) {
	c = jQuery.extend({toTop:true, overlay:10, modal:false, onShow:showModal, onHide:closeModal, gbox:"", jqm:true, jqM:true}, c || {});
	if (jQuery.fn.jqm && c.jqm == true) {
		if (c.jqM) {
			jQuery(a).attr("aria-hidden", "false").jqm(c).jqmShow();
		} else {
			jQuery(a).attr("aria-hidden", "false").jqmShow();
		}
	} else {
		if (c.gbox != "") {
			jQuery(".jqgrid-overlay:first", c.gbox).show();
			jQuery(a).data("gbox", c.gbox);
		}
		jQuery(a).show().attr("aria-hidden", "false");
		try {
			jQuery(":input:visible", a)[0].focus();
		}
		catch (b) {
		}
	}
};
var hideModal = function (a, d) {
	d = jQuery.extend({jqm:true, gb:""}, d || {});
	if (d.onClose) {
		var b = d.onClose(a);
		if (typeof b == "boolean" && !b) {
			return;
		}
	}
	if (jQuery.fn.jqm && d.jqm === true) {
		jQuery(a).attr("aria-hidden", "true").jqmHide();
	} else {
		if (d.gb != "") {
			try {
				jQuery(".jqgrid-overlay:first", d.gb).hide();
			}
			catch (c) {
			}
		}
		jQuery(a).hide().attr("aria-hidden", "true");
	}
};
function info_dialog(n, h, b, m) {
	var k = {width:290, height:"auto", dataheight:"auto", drag:true, resize:false, caption:"<b>" + n + "</b>", left:250, top:170, zIndex:1000, jqModal:true, closeOnEscape:true, align:"center", buttonalign:"center", buttons:[]};
	jQuery.extend(k, m || {});
	var c = k.jqModal;
	if (jQuery.fn.jqm && !c) {
		c = false;
	}
	var f = "";
	if (k.buttons.length > 0) {
		for (var d = 0; d < k.buttons.length; d++) {
			if (typeof k.buttons[d].id == "undefined") {
				k.buttons[d].id = "info_button_" + d;
			}
			f += "<a href='javascript:void(0)' id='" + k.buttons[d].id + "' class='fm-button ui-state-default ui-corner-all'>" + k.buttons[d].text + "</a>";
		}
	}
	var j = isNaN(k.dataheight) ? k.dataheight : k.dataheight + "px", l = "text-align:" + k.align + ";";
	var a = "<div id='info_id'>";
	a += "<div id='infocnt' style='margin:0px;padding-bottom:1em;width:100%;overflow:auto;position:relative;height:" + j + ";" + l + "'>" + h + "</div>";
	a += b ? "<div class='ui-widget-content ui-helper-clearfix' style='text-align:" + k.buttonalign + ";padding-bottom:0.8em;padding-top:0.5em;background-image: none;border-width: 1px 0 0 0;'><a href='javascript:void(0)' id='closedialog' class='fm-button ui-state-default ui-corner-all'>" + b + "</a>" + f + "</div>" : "";
	a += "</div>";
	try {
		if (jQuery("#info_dialog").attr("aria-hidden") == "false") {
			hideModal("#info_dialog", {jqm:c});
		}
		jQuery("#info_dialog").remove();
	}
	catch (g) {
	}
	createModal({themodal:"info_dialog", modalhead:"info_head", modalcontent:"info_content", scrollelm:"infocnt"}, a, k, "", "", true);
	if (f) {
		jQuery.each(k.buttons, function (e) {
			jQuery("#" + this.id, "#info_id").bind("click", function () {
				k.buttons[e].onClick.call(jQuery("#info_dialog"));
				return false;
			});
		});
	}
	jQuery("#closedialog", "#info_id").click(function (i) {
		hideModal("#info_dialog", {jqm:c});
		return false;
	});
	jQuery(".fm-button", "#info_dialog").hover(function () {
		jQuery(this).addClass("ui-state-hover");
	}, function () {
		jQuery(this).removeClass("ui-state-hover");
	});
	viewModal("#info_dialog", {onHide:function (e) {
		e.w.hide().remove();
		if (e.o) {
			e.o.remove();
		}
	}, modal:true, jqm:c});
}
function findPos(a) {
	var b = curtop = 0;
	if (a.offsetParent) {
		do {
			b += a.offsetLeft;
			curtop += a.offsetTop;
		} while (a = a.offsetParent);
	}
	return [b, curtop];
}
function isArray(a) {
	if (a.constructor.toString().indexOf("Array") == -1) {
		return false;
	} else {
		return true;
	}
}
function createEl(d, f, s, j, q) {
	var r = "";
	if (f.defaultValue) {
		delete f.defaultValue;
	}
	function k(i, e) {
		if (jQuery.isFunction(e.dataInit)) {
			i.id = e.id;
			e.dataInit(i);
			delete e.id;
			delete e.dataInit;
		}
		if (e.dataEvents) {
			jQuery.each(e.dataEvents, function () {
				if (this.data != null) {
					jQuery(i).bind(this.type, this.data, this.fn);
				} else {
					jQuery(i).bind(this.type, this.fn);
				}
			});
			delete e.dataEvents;
		}
		return e;
	}
	switch (d) {
	  case "textarea":
		r = document.createElement("textarea");
		if (j) {
			if (!f.cols) {
				jQuery(r).css({width:"98%"});
			}
		} else {
			if (!f.cols) {
				f.cols = 20;
			}
		}
		if (!f.rows) {
			f.rows = 2;
		}
		if (s == "&nbsp;" || s == "&#160;" || (s.length == 1 && s.charCodeAt(0) == 160)) {
			s = "";
		}
		r.value = s;
		f = k(r, f);
		jQuery(r).attr(f);
		break;
	  case "checkbox":
		r = document.createElement("input");
		r.type = "checkbox";
		if (!f.value) {
			var u = s.toLowerCase();
			if (u.search(/(false|0|no|off|undefined)/i) < 0 && u !== "") {
				r.checked = true;
				r.defaultChecked = true;
				r.value = s;
			} else {
				r.value = "on";
			}
			jQuery(r).attr("offval", "off");
		} else {
			var m = f.value.split(":");
			if (s === m[0]) {
				r.checked = true;
				r.defaultChecked = true;
			}
			r.value = m[0];
			jQuery(r).attr("offval", m[1]);
			try {
				delete f.value;
			}
			catch (p) {
			}
		}
		f = k(r, f);
		jQuery(r).attr(f);
		break;
	  case "select":
		r = document.createElement("select");
		var b, g = [];
		if (f.multiple === true) {
			b = true;
			r.multiple = "multiple";
		} else {
			b = false;
		}
		if (f.dataUrl != null) {
			jQuery.ajax(jQuery.extend({url:f.dataUrl, type:"GET", complete:function (x, w) {
				try {
					delete f.dataUrl;
					delete f.value;
				}
				catch (y) {
				}
				var v;
				if (f.buildSelect != null) {
					var i = f.buildSelect(x);
					v = jQuery(i).html();
					delete f.buildSelect;
				} else {
					v = jQuery(x.responseText).html();
				}
				if (v) {
					jQuery(r).append(v);
					f = k(r, f);
					if (typeof f.size === "undefined") {
						f.size = b ? 3 : 1;
					}
					if (b) {
						g = s.split(",");
						g = jQuery.map(g, function (e) {
							return jQuery.trim(e);
						});
					} else {
						g[0] = s;
					}
					jQuery(r).attr(f);
					setTimeout(function () {
						jQuery("option", r).each(function (e) {
							if (e == 0) {
								this.selected = "";
							}
							if (jQuery.inArray(jQuery(this).text(), g) > -1 || jQuery.inArray(jQuery(this).val(), g) > -1) {
								this.selected = "selected";
								if (!b) {
									return false;
								}
							}
						});
					}, 0);
				}
			}}, q || {}));
		} else {
			if (f.value) {
				var n;
				if (b) {
					g = s.split(",");
					g = jQuery.map(g, function (e) {
						return jQuery.trim(e);
					});
					if (typeof f.size === "undefined") {
						f.size = 3;
					}
				} else {
					f.size = 1;
				}
				if (typeof f.value === "function") {
					f.value = f.value();
				}
				if (typeof f.value === "string") {
					var o = f.value.split(";"), l, h;
					for (n = 0; n < o.length; n++) {
						l = o[n].split(":");
						h = document.createElement("option");
						h.value = l[0];
						h.innerHTML = l[1];
						if (!b && (l[0] == s || l[1] == s)) {
							h.selected = "selected";
						}
						if (b && (jQuery.inArray(l[1], g) > -1 || jQuery.inArray(l[0], g) > -1)) {
							h.selected = "selected";
						}
						r.appendChild(h);
					}
				} else {
					if (typeof f.value === "object") {
						var c = f.value;
						for (var t in c) {
							h = document.createElement("option");
							h.value = t;
							h.innerHTML = c[t];
							if (!b && (t == s || c[t] == s)) {
								h.selected = "selected";
							}
							if (b && (jQuery.inArray(c[t], g) > -1 || jQuery.inArray(t, g) > -1)) {
								h.selected = "selected";
							}
							r.appendChild(h);
						}
					}
				}
				f = k(r, f);
				try {
					delete f.value;
				}
				catch (p) {
				}
				jQuery(r).attr(f);
			}
		}
		break;
	  case "text":
	  case "password":
	  case "button":
		r = document.createElement("input");
		r.type = d;
		r.value = jQuery.jgrid.htmlDecode(s);
		f = k(r, f);
		if (d != "button") {
			if (j) {
				if (!f.size) {
					jQuery(r).css({width:"98%"});
				}
			} else {
				if (!f.size) {
					f.size = 20;
				}
			}
		}
		jQuery(r).attr(f);
		break;
	  case "image":
	  case "file":
		r = document.createElement("input");
		r.type = d;
		f = k(r, f);
		jQuery(r).attr(f);
		break;
	  case "custom":
		r = document.createElement("span");
		try {
			if (jQuery.isFunction(f.custom_element)) {
				var a = f.custom_element.call(this, s, f);
				if (a) {
					a = jQuery(a).addClass("customelement").attr({id:f.id, name:f.name});
					jQuery(r).empty().append(a);
				} else {
					throw "e2";
				}
			} else {
				throw "e1";
			}
		}
		catch (p) {
			if (p == "e1") {
				info_dialog(jQuery.jgrid.errors.errcap, "function 'custom_element' " + jQuery.jgrid.edit.msg.nodefined, jQuery.jgrid.edit.bClose);
			}
			if (p == "e2") {
				info_dialog(jQuery.jgrid.errors.errcap, "function 'custom_element' " + jQuery.jgrid.edit.msg.novalue, jQuery.jgrid.edit.bClose);
			} else {
				info_dialog(jQuery.jgrid.errors.errcap, p.message, jQuery.jgrid.edit.bClose);
			}
		}
		break;
	}
	return r;
}
function checkValues(c, m, j) {
	var f, h, n;
	if (typeof (m) == "string") {
		for (h = 0, len = j.p.colModel.length; h < len; h++) {
			if (j.p.colModel[h].name == m) {
				f = j.p.colModel[h].editrules;
				m = h;
				try {
					n = j.p.colModel[h].formoptions.label;
				}
				catch (l) {
				}
				break;
			}
		}
	} else {
		if (m >= 0) {
			f = j.p.colModel[m].editrules;
		}
	}
	if (f) {
		if (!n) {
			n = j.p.colNames[m];
		}
		if (f.required === true) {
			if (c.match(/^s+$/) || c == "") {
				return [false, n + ": " + jQuery.jgrid.edit.msg.required, ""];
			}
		}
		var d = f.required === false ? false : true;
		if (f.number === true) {
			if (!(d === false && isEmpty(c))) {
				if (isNaN(c)) {
					return [false, n + ": " + jQuery.jgrid.edit.msg.number, ""];
				}
			}
		}
		if (typeof f.minValue != "undefined" && !isNaN(f.minValue)) {
			if (parseFloat(c) < parseFloat(f.minValue)) {
				return [false, n + ": " + jQuery.jgrid.edit.msg.minValue + " " + f.minValue, ""];
			}
		}
		if (typeof f.maxValue != "undefined" && !isNaN(f.maxValue)) {
			if (parseFloat(c) > parseFloat(f.maxValue)) {
				return [false, n + ": " + jQuery.jgrid.edit.msg.maxValue + " " + f.maxValue, ""];
			}
		}
		var a;
		if (f.email === true) {
			if (!(d === false && isEmpty(c))) {
				a = /^((([a-z]|\d|[!#\$%&'\*\+\-\/=\?\^_`{\|}~]|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])+(\.([a-z]|\d|[!#\$%&'\*\+\-\/=\?\^_`{\|}~]|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])+)*)|((\x22)((((\x20|\x09)*(\x0d\x0a))?(\x20|\x09)+)?(([\x01-\x08\x0b\x0c\x0e-\x1f\x7f]|\x21|[\x23-\x5b]|[\x5d-\x7e]|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])|(\\([\x01-\x09\x0b\x0c\x0d-\x7f]|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF]))))*(((\x20|\x09)*(\x0d\x0a))?(\x20|\x09)+)?(\x22)))@((([a-z]|\d|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])|(([a-z]|\d|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])([a-z]|\d|-|\.|_|~|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])*([a-z]|\d|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])))\.)+(([a-z]|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])|(([a-z]|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])([a-z]|\d|-|\.|_|~|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])*([a-z]|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])))\.?$/i;
				if (!a.test(c)) {
					return [false, n + ": " + jQuery.jgrid.edit.msg.email, ""];
				}
			}
		}
		if (f.integer === true) {
			if (!(d === false && isEmpty(c))) {
				if (isNaN(c)) {
					return [false, n + ": " + jQuery.jgrid.edit.msg.integer, ""];
				}
				if ((c % 1 != 0) || (c.indexOf(".") != -1)) {
					return [false, n + ": " + jQuery.jgrid.edit.msg.integer, ""];
				}
			}
		}
		if (f.date === true) {
			if (!(d === false && isEmpty(c))) {
				var b = j.p.colModel[m].datefmt || "Y-m-d";
				if (!checkDate(b, c)) {
					return [false, n + ": " + jQuery.jgrid.edit.msg.date + " - " + b, ""];
				}
			}
		}
		if (f.time === true) {
			if (!(d === false && isEmpty(c))) {
				if (!checkTime(c)) {
					return [false, n + ": " + jQuery.jgrid.edit.msg.date + " - hh:mm (am/pm)", ""];
				}
			}
		}
		if (f.url === true) {
			if (!(d === false && isEmpty(c))) {
				a = /^(((https?)|(ftp)):\/\/([\-\w]+\.)+\w{2,3}(\/[%\-\w]+(\.\w{2,})?)*(([\w\-\.\?\\\/+@&#;`~=%!]*)(\.\w{2,})?)*\/?)/i;
				if (!a.test(c)) {
					return [false, n + ": " + jQuery.jgrid.edit.msg.url, ""];
				}
			}
		}
		if (f.custom === true) {
			if (!(d === false && isEmpty(c))) {
				if (jQuery.isFunction(f.custom_func)) {
					var k = f.custom_func.call(j, c, n);
					if (jQuery.isArray(k)) {
						return k;
					} else {
						return [false, jQuery.jgrid.edit.msg.customarray, ""];
					}
				} else {
					return [false, jQuery.jgrid.edit.msg.customfcheck, ""];
				}
			}
		}
	}
	return [true, "", ""];
}
function checkDate(l, c) {
	var e = {}, n;
	l = l.toLowerCase();
	if (l.indexOf("/") != -1) {
		n = "/";
	} else {
		if (l.indexOf("-") != -1) {
			n = "-";
		} else {
			if (l.indexOf(".") != -1) {
				n = ".";
			} else {
				n = "/";
			}
		}
	}
	l = l.split(n);
	c = c.split(n);
	if (c.length != 3) {
		return false;
	}
	var f = -1, m, g = -1, d = -1;
	for (var h = 0; h < l.length; h++) {
		var b = isNaN(c[h]) ? 0 : parseInt(c[h], 10);
		e[l[h]] = b;
		m = l[h];
		if (m.indexOf("y") != -1) {
			f = h;
		}
		if (m.indexOf("m") != -1) {
			d = h;
		}
		if (m.indexOf("d") != -1) {
			g = h;
		}
	}
	if (l[f] == "y" || l[f] == "yyyy") {
		m = 4;
	} else {
		if (l[f] == "yy") {
			m = 2;
		} else {
			m = -1;
		}
	}
	var a = DaysArray(12);
	var k;
	if (f === -1) {
		return false;
	} else {
		k = e[l[f]].toString();
		if (m == 2 && k.length == 1) {
			m = 1;
		}
		if (k.length != m || e[l[f]] == 0) {
			return false;
		}
	}
	if (d === -1) {
		return false;
	} else {
		k = e[l[d]].toString();
		if (k.length < 1 || e[l[d]] < 1 || e[l[d]] > 12) {
			return false;
		}
	}
	if (g === -1) {
		return false;
	} else {
		k = e[l[g]].toString();
		if (k.length < 1 || e[l[g]] < 1 || e[l[g]] > 31 || (e[l[d]] == 2 && e[l[g]] > daysInFebruary(e[l[f]])) || e[l[g]] > a[e[l[d]]]) {
			return false;
		}
	}
	return true;
}
function daysInFebruary(a) {
	return (((a % 4 == 0) && ((!(a % 100 == 0)) || (a % 400 == 0))) ? 29 : 28);
}
function DaysArray(b) {
	for (var a = 1; a <= b; a++) {
		this[a] = 31;
		if (a == 4 || a == 6 || a == 9 || a == 11) {
			this[a] = 30;
		}
		if (a == 2) {
			this[a] = 29;
		}
	}
	return this;
}
function isEmpty(a) {
	if (a.match(/^s+$/) || a == "") {
		return true;
	} else {
		return false;
	}
}
function checkTime(c) {
	var b = /^(\d{1,2}):(\d{2})([ap]m)?$/, a;
	if (!isEmpty(c)) {
		a = c.match(b);
		if (a) {
			if (a[3]) {
				if (a[1] < 1 || a[1] > 12) {
					return false;
				}
			} else {
				if (a[1] > 23) {
					return false;
				}
			}
			if (a[2] > 59) {
				return false;
			}
		} else {
			return false;
		}
	}
	return true;
}
(function (b) {
	var a = null;
	b.jgrid.extend({searchGrid:function (c) {
		c = b.extend({recreateFilter:false, drag:true, sField:"searchField", sValue:"searchString", sOper:"searchOper", sFilter:"filters", beforeShowSearch:null, afterShowSearch:null, onInitializeSearch:null, closeAfterSearch:false, closeOnEscape:false, multipleSearch:false, sopt:null, onClose:null}, b.jgrid.search, c || {});
		return this.each(function () {
			var l = this;
			if (!l.grid) {
				return;
			}
			if (b.fn.searchFilter) {
				var g = "fbox_" + l.p.id;
				if (c.recreateFilter === true) {
					b("#" + g).remove();
				}
				if (b("#" + g).html() != null) {
					if (b.isFunction(c.beforeShowSearch)) {
						c.beforeShowSearch(b("#" + g));
					}
					f();
					if (b.isFunction(c.afterShowSearch)) {
						c.afterShowSearch(b("#" + g));
					}
				} else {
					var n = [], v = b("#" + l.p.id).jqGrid("getGridParam", "colNames"), s = b("#" + l.p.id).jqGrid("getGridParam", "colModel"), u = ["eq", "ne", "lt", "le", "gt", "ge", "bw", "bn", "in", "ni", "ew", "en", "cn", "nc"], i, r, h, p;
					p = jQuery.fn.searchFilter.defaults.operators;
					if (c.sopt != null) {
						p = [];
						h = 0;
						for (i = 0; i < c.sopt.length; i++) {
							if ((r = b.inArray(c.sopt[i], u)) != -1) {
								p[h] = {op:c.sopt[i], text:c.odata[r]};
								h++;
							}
						}
					}
					var q;
					b.each(s, function (w, j) {
						q = (typeof j.search === "undefined") ? true : j.search, hidden = (j.hidden === true), soptions = b.extend({}, {text:v[w], itemval:j.index || j.name}, this.searchoptions), ignoreHiding = (soptions.searchhidden === true);
						if (typeof soptions.sopt == "undefined") {
							soptions.sopt = c.sopt || u;
						}
						h = 0;
						soptions.ops = [];
						if (soptions.sopt.length > 0) {
							for (i = 0; i < soptions.sopt.length; i++) {
								if ((r = b.inArray(soptions.sopt[i], u)) != -1) {
									soptions.ops[h] = {op:soptions.sopt[i], text:c.odata[r]};
									h++;
								}
							}
						}
						if (typeof (this.stype) === "undefined") {
							this.stype = "text";
						}
						if (this.stype == "select") {
							if (soptions.dataUrl != null) {
							} else {
								var x;
								if (soptions.value) {
									x = soptions.value;
								} else {
									if (this.editoptions) {
										x = this.editoptions.value;
									}
								}
								if (x) {
									soptions.dataValues = [];
									if (typeof (x) === "string") {
										var y = x.split(";"), e;
										for (i = 0; i < y.length; i++) {
											e = y[i].split(":");
											soptions.dataValues[i] = {value:e[0], text:e[1]};
										}
									} else {
										if (typeof (x) === "object") {
											i = 0;
											for (var k in x) {
												soptions.dataValues[i] = {value:k, text:x[k]};
												i++;
											}
										}
									}
								}
							}
						}
						if ((ignoreHiding && q) || (q && !hidden)) {
							n.push(soptions);
						}
					});
					if (n.length > 0) {
						b("<div id='" + g + "' role='dialog' tabindex='-1'></div>").insertBefore("#gview_" + l.p.id);
						b("#" + g).searchFilter(n, {groupOps:c.groupOps, operators:p, onClose:d, resetText:c.Reset, searchText:c.Find, windowTitle:c.caption, rulesText:c.rulesText, matchText:c.matchText, onSearch:t, onReset:m, stringResult:c.multipleSearch, ajaxSelectOptions:b.extend({}, b.jgrid.ajaxOptions, l.p.ajaxSelectOptions || {})});
						b(".ui-widget-overlay", "#" + g).remove();
						if (l.p.direction == "rtl") {
							b(".ui-closer", "#" + g).css("float", "left");
						}
						if (c.drag === true) {
							b("#" + g + " table thead tr:first td:first").css("cursor", "move");
							if (jQuery.fn.jqDrag) {
								b("#" + g).jqDrag(b("#" + g + " table thead tr:first td:first"));
							} else {
								try {
									b("#" + g).draggable({handle:b("#" + g + " table thead tr:first td:first")});
								}
								catch (o) {
								}
							}
						}
						if (c.multipleSearch === false) {
							b(".ui-del, .ui-add, .ui-del, .ui-add-last, .matchText, .rulesText", "#" + g).hide();
							b("select[name='groupOp']", "#" + g).hide();
						}
						if (b.isFunction(c.onInitializeSearch)) {
							c.onInitializeSearch(b("#" + g));
						}
						if (b.isFunction(c.beforeShowSearch)) {
							c.beforeShowSearch(b("#" + g));
						}
						f();
						if (b.isFunction(c.afterShowSearch)) {
							c.afterShowSearch(b("#" + g));
						}
						if (c.closeOnEscape === true) {
							b("#" + g).keydown(function (j) {
								if (j.which == 27) {
									d(b("#" + g));
								}
							});
						}
					}
				}
			}
			function t(w) {
				var e = (w !== undefined), k = b("#" + l.p.id), j = {};
				if (c.multipleSearch === false) {
					j[c.sField] = w.rules[0].field;
					j[c.sValue] = w.rules[0].data;
					j[c.sOper] = w.rules[0].op;
				} else {
					j[c.sFilter] = w;
				}
				k[0].p.search = e;
				b.extend(k[0].p.postData, j);
				k.trigger("reloadGrid", [{page:1}]);
				if (c.closeAfterSearch) {
					d(b("#" + g));
				}
			}
			function m(w) {
				var e = (w !== undefined), k = b("#" + l.p.id), j = [];
				k[0].p.search = e;
				if (c.multipleSearch === false) {
					j[c.sField] = j[c.sValue] = j[c.sOper] = "";
				} else {
					j[c.sFilter] = "";
				}
				b.extend(k[0].p.postData, j);
				k.trigger("reloadGrid", [{page:1}]);
			}
			function d(e) {
				if (c.onClose) {
					var j = c.onClose(e);
					if (typeof j == "boolean" && !j) {
						return;
					}
				}
				e.hide();
				b(".jqgrid-overlay:first", "#gbox_" + l.p.id).hide();
			}
			function f() {
				var k = b(".ui-searchFilter").length;
				if (k > 1) {
					var j = b("#" + g).css("zIndex");
					b("#" + g).css({zIndex:parseInt(j) + k});
				}
				b("#" + g).show();
				b(".jqgrid-overlay:first", "#gbox_" + l.p.id).show();
				try {
					b(":input:visible", "#" + g)[0].focus();
				}
				catch (e) {
				}
			}
		});
	}, editGridRow:function (c, d) {
		d = b.extend({top:0, left:0, width:300, height:"auto", dataheight:"auto", modal:false, drag:true, resize:true, url:null, mtype:"POST", clearAfterAdd:true, closeAfterEdit:false, reloadAfterSubmit:true, onInitializeForm:null, beforeInitData:null, beforeShowForm:null, afterShowForm:null, beforeSubmit:null, afterSubmit:null, onclickSubmit:null, afterComplete:null, onclickPgButtons:null, afterclickPgButtons:null, editData:{}, recreateForm:false, jqModal:true, closeOnEscape:false, addedrow:"first", topinfo:"", bottominfo:"", saveicon:[], closeicon:[], savekey:[false, 13], navkeys:[false, 38, 40], checkOnSubmit:false, checkOnUpdate:false, _savedData:{}, onClose:null, ajaxEditOptions:{}, serializeEditData:null}, b.jgrid.edit, d || {});
		a = d;
		return this.each(function () {
			var e = this;
			if (!e.grid || !c) {
				return;
			}
			var C = e.p.id, y = "FrmGrid_" + C, u = "TblGrid_" + C, i = {themodal:"editmod" + C, modalhead:"edithd" + C, modalcontent:"editcnt" + C, scrollelm:y}, D = b.isFunction(a.beforeShowForm) ? a.beforeShowForm : false, O = b.isFunction(a.afterShowForm) ? a.afterShowForm : false, N = b.isFunction(a.beforeInitData) ? a.beforeInitData : false, o = b.isFunction(a.onInitializeForm) ? a.onInitializeForm : false, I = null, J = 1, q = 0, v, E, F, R, H, B;
			if (c == "new") {
				c = "_empty";
				d.caption = d.addCaption;
			} else {
				d.caption = d.editCaption;
			}
			if (d.recreateForm === true && b("#" + i.themodal).html() != null) {
				b("#" + i.themodal).remove();
			}
			var k = true;
			if (d.checkOnUpdate && d.jqModal && !d.modal) {
				k = false;
			}
			if (b("#" + i.themodal).html() != null) {
				b(".ui-jqdialog-title", "#" + i.modalhead).html(d.caption);
				b("#FormError", "#" + u).hide();
				if (a.topinfo) {
					b(".topinfo", "#" + u + "_2").html(a.topinfo);
					b(".tinfo", "#" + u + "_2").show();
				} else {
					b(".tinfo", "#" + u + "_2").hide();
				}
				if (a.bottominfo) {
					b(".bottominfo", "#" + u + "_2").html(a.bottominfo);
					b(".binfo", "#" + u + "_2").show();
				} else {
					b(".binfo", "#" + u + "_2").hide();
				}
				if (N) {
					N(b("#" + y));
				}
				n(c, e, y);
				if (c == "_empty") {
					b("#pData, #nData", "#" + u + "_2").hide();
				} else {
					b("#pData, #nData", "#" + u + "_2").show();
				}
				if (d.processing === true) {
					d.processing = false;
					b("#sData", "#" + u + "_2").removeClass("ui-state-active");
				}
				if (b("#" + y).data("disabled") === true) {
					b(".confirm", "#" + i.themodal).hide();
					b("#" + y).data("disabled", false);
				}
				if (D) {
					D(b("#" + y));
				}
				b("#" + i.themodal).data("onClose", a.onClose);
				viewModal("#" + i.themodal, {gbox:"#gbox_" + C, jqm:d.jqModal, jqM:false, closeoverlay:k, modal:d.modal});
				if (!k) {
					b(".jqmOverlay").click(function () {
						if (!g()) {
							return false;
						}
						hideModal("#" + i.themodal, {gb:"#gbox_" + C, jqm:d.jqModal, onClose:a.onClose});
						return false;
					});
				}
				if (O) {
					O(b("#" + y));
				}
			} else {
				b(e.p.colModel).each(function (Y) {
					var Z = this.formoptions;
					J = Math.max(J, Z ? Z.colpos || 0 : 0);
					q = Math.max(q, Z ? Z.rowpos || 0 : 0);
				});
				var r = isNaN(d.dataheight) ? d.dataheight : d.dataheight + "px";
				var M, U = b("<form name='FormPost' id='" + y + "' class='FormGrid' style='width:100%;overflow:auto;position:relative;height:" + r + ";'></form>").data("disabled", false), A = b("<table id='" + u + "' class='EditTable' cellspacing='0' cellpading='0' border='0'><tbody></tbody></table>");
				b(U).append(A);
				M = b("<tr id='FormError' style='display:none'><td class='ui-state-error' colspan='" + (J * 2) + "'></td></tr>");
				M[0].rp = 0;
				b(A).append(M);
				M = b("<tr style='display:none' clas=='tinfo'><td class='topinfo' colspan='" + (J * 2) + "'>" + a.topinfo + "</td></tr>");
				M[0].rp = 0;
				b(A).append(M);
				if (N) {
					N(b("#" + y));
				}
				var f = e.p.direction == "rtl" ? true : false, T = f ? "nData" : "pData", V = f ? "pData" : "nData", z = s(c, e, A, J), l = "<a href='javascript:void(0)' id='" + T + "' class='fm-button ui-state-default ui-corner-left'><span class='ui-icon ui-icon-triangle-1-w'></span></div>", m = "<a href='javascript:void(0)' id='" + V + "' class='fm-button ui-state-default ui-corner-right'><span class='ui-icon ui-icon-triangle-1-e'></span></div>", h = "<a href='javascript:void(0)' id='sData' class='fm-button ui-state-default ui-corner-all'>" + d.bSubmit + "</a>", t = "<a href='javascript:void(0)' id='cData' class='fm-button ui-state-default ui-corner-all'>" + d.bCancel + "</a>";
				var Q = "<table border='0' class='EditTable' id='" + u + "_2'><tbody><tr id='Act_Buttons'><td class='navButton ui-widget-content'>" + (f ? m + l : l + m) + "</td><td class='EditButton ui-widget-content'>" + h + t + "</td></tr>";
				Q += "<tr style='display:none' class='binfo'><td class='bottominfo' colspan='2'>" + a.bottominfo + "</td></tr>";
				Q += "</tbody></table>";
				if (q > 0) {
					var x = [];
					b.each(b(A)[0].rows, function (Y, Z) {
						x[Y] = Z;
					});
					x.sort(function (Z, Y) {
						if (Z.rp > Y.rp) {
							return 1;
						}
						if (Z.rp < Y.rp) {
							return -1;
						}
						return 0;
					});
					b.each(x, function (Y, Z) {
						b("tbody", A).append(Z);
					});
				}
				d.gbox = "#gbox_" + C;
				var p = false;
				if (d.closeOnEscape === true) {
					d.closeOnEscape = false;
					p = true;
				}
				var P = b("<span></span>").append(U).append(Q);
				createModal(i, P, d, "#gview_" + e.p.id, b("#gview_" + e.p.id)[0]);
				if (f) {
					b("#pData, #nData", "#" + u + "_2").css("float", "right");
					b(".EditButton", "#" + u + "_2").css("text-align", "left");
				}
				if (a.topinfo) {
					b(".tinfo", "#" + u + "_2").show();
				}
				if (a.bottominfo) {
					b(".binfo", "#" + u + "_2").show();
				}
				P = null;
				Q = null;
				b("#" + i.themodal).keydown(function (Y) {
					var Z = Y.target;
					if (b("#" + y).data("disabled") === true) {
						return false;
					}
					if (a.savekey[0] === true && Y.which == a.savekey[1]) {
						if (Z.tagName != "TEXTAREA") {
							b("#sData", "#" + u + "_2").trigger("click");
							return false;
						}
					}
					if (Y.which === 27) {
						if (!g()) {
							return false;
						}
						if (p) {
							hideModal(this, {gb:d.gbox, jqm:d.jqModal, onClose:a.onClose});
						}
						return false;
					}
					if (a.navkeys[0] === true) {
						if (b("#id_g", "#" + u).val() == "_empty") {
							return true;
						}
						if (Y.which == a.navkeys[1]) {
							b("#pData", "#" + u + "_2").trigger("click");
							return false;
						}
						if (Y.which == a.navkeys[2]) {
							b("#nData", "#" + u + "_2").trigger("click");
							return false;
						}
					}
				});
				if (d.checkOnUpdate) {
					b("a.ui-jqdialog-titlebar-close span", "#" + i.themodal).removeClass("jqmClose");
					b("a.ui-jqdialog-titlebar-close", "#" + i.themodal).unbind("click").click(function () {
						if (!g()) {
							return false;
						}
						hideModal("#" + i.themodal, {gb:"#gbox_" + C, jqm:d.jqModal, onClose:a.onClose});
						return false;
					});
				}
				d.saveicon = b.extend([true, "left", "ui-icon-disk"], d.saveicon);
				d.closeicon = b.extend([true, "left", "ui-icon-close"], d.closeicon);
				if (d.saveicon[0] == true) {
					b("#sData", "#" + u + "_2").addClass(d.saveicon[1] == "right" ? "fm-button-icon-right" : "fm-button-icon-left").append("<span class='ui-icon " + d.saveicon[2] + "'></span>");
				}
				if (d.closeicon[0] == true) {
					b("#cData", "#" + u + "_2").addClass(d.closeicon[1] == "right" ? "fm-button-icon-right" : "fm-button-icon-left").append("<span class='ui-icon " + d.closeicon[2] + "'></span>");
				}
				if (a.checkOnSubmit || a.checkOnUpdate) {
					h = "<a href='javascript:void(0)' id='sNew' class='fm-button ui-state-default ui-corner-all' style='z-index:1002'>" + d.bYes + "</a>";
					m = "<a href='javascript:void(0)' id='nNew' class='fm-button ui-state-default ui-corner-all' style='z-index:1002'>" + d.bNo + "</a>";
					t = "<a href='javascript:void(0)' id='cNew' class='fm-button ui-state-default ui-corner-all' style='z-index:1002'>" + d.bExit + "</a>";
					var G, w = d.zIndex || 999;
					w++;
					if (b.browser.msie && b.browser.version == 6) {
						G = "<iframe style=\"display:block;position:absolute;z-index:-1;filter:Alpha(Opacity='0');\" src=\"javascript:false;\"></iframe>";
					} else {
						G = "";
					}
					b("<div class='ui-widget-overlay jqgrid-overlay confirm' style='z-index:" + w + ";display:none;'>&#160;" + G + "</div><div class='confirm ui-widget-content ui-jqconfirm' style='z-index:" + (w + 1) + "'>" + d.saveData + "<br/><br/>" + h + m + t + "</div>").insertAfter("#" + y);
					b("#sNew", "#" + i.themodal).click(function () {
						j();
						b("#" + y).data("disabled", false);
						b(".confirm", "#" + i.themodal).hide();
						return false;
					});
					b("#nNew", "#" + i.themodal).click(function () {
						b(".confirm", "#" + i.themodal).hide();
						b("#" + y).data("disabled", false);
						setTimeout(function () {
							b(":input", "#" + y)[0].focus();
						}, 0);
						return false;
					});
					b("#cNew", "#" + i.themodal).click(function () {
						b(".confirm", "#" + i.themodal).hide();
						b("#" + y).data("disabled", false);
						hideModal("#" + i.themodal, {gb:"#gbox_" + C, jqm:d.jqModal, onClose:a.onClose});
						return false;
					});
				}
				if (o) {
					o(b("#" + y));
				}
				if (c == "_empty") {
					b("#pData,#nData", "#" + u + "_2").hide();
				} else {
					b("#pData,#nData", "#" + u + "_2").show();
				}
				if (D) {
					D(b("#" + y));
				}
				b("#" + i.themodal).data("onClose", a.onClose);
				viewModal("#" + i.themodal, {gbox:"#gbox_" + C, jqm:d.jqModal, closeoverlay:k, modal:d.modal});
				if (!k) {
					b(".jqmOverlay").click(function () {
						if (!g()) {
							return false;
						}
						hideModal("#" + i.themodal, {gb:"#gbox_" + C, jqm:d.jqModal, onClose:a.onClose});
						return false;
					});
				}
				if (O) {
					O(b("#" + y));
				}
				b(".fm-button", "#" + i.themodal).hover(function () {
					b(this).addClass("ui-state-hover");
				}, function () {
					b(this).removeClass("ui-state-hover");
				});
				b("#sData", "#" + u + "_2").click(function (Y) {
					E = {};
					R = {};
					b("#FormError", "#" + u).hide();
					W();
					if (E.id == "_empty") {
						j();
					} else {
						if (d.checkOnSubmit === true) {
							H = b.extend({}, E, R);
							B = K(H, a._savedData);
							if (B) {
								b("#" + y).data("disabled", true);
								b(".confirm", "#" + i.themodal).show();
							} else {
								j();
							}
						} else {
							j();
						}
					}
					return false;
				});
				b("#cData", "#" + u + "_2").click(function (Y) {
					if (!g()) {
						return false;
					}
					hideModal("#" + i.themodal, {gb:"#gbox_" + C, jqm:d.jqModal, onClose:a.onClose});
					return false;
				});
				b("#nData", "#" + u + "_2").click(function (Y) {
					if (!g()) {
						return false;
					}
					b("#FormError", "#" + u).hide();
					var Z = X();
					Z[0] = parseInt(Z[0]);
					if (Z[0] != -1 && Z[1][Z[0] + 1]) {
						if (b.isFunction(d.onclickPgButtons)) {
							d.onclickPgButtons("next", b("#" + y), Z[1][Z[0]]);
						}
						n(Z[1][Z[0] + 1], e, y);
						b(e).jqGrid("setSelection", Z[1][Z[0] + 1]);
						if (b.isFunction(d.afterclickPgButtons)) {
							d.afterclickPgButtons("next", b("#" + y), Z[1][Z[0] + 1]);
						}
						L(Z[0] + 1, Z[1].length - 1);
					}
					return false;
				});
				b("#pData", "#" + u + "_2").click(function (Z) {
					if (!g()) {
						return false;
					}
					b("#FormError", "#" + u).hide();
					var Y = X();
					if (Y[0] != -1 && Y[1][Y[0] - 1]) {
						if (b.isFunction(d.onclickPgButtons)) {
							d.onclickPgButtons("prev", b("#" + y), Y[1][Y[0]]);
						}
						n(Y[1][Y[0] - 1], e, y);
						b(e).jqGrid("setSelection", Y[1][Y[0] - 1]);
						if (b.isFunction(d.afterclickPgButtons)) {
							d.afterclickPgButtons("prev", b("#" + y), Y[1][Y[0] - 1]);
						}
						L(Y[0] - 1, Y[1].length - 1);
					}
					return false;
				});
			}
			var S = X();
			L(S[0], S[1].length - 1);
			function L(Z, aa, Y) {
				if (Z == 0) {
					b("#pData", "#" + u + "_2").addClass("ui-state-disabled");
				} else {
					b("#pData", "#" + u + "_2").removeClass("ui-state-disabled");
				}
				if (Z == aa) {
					b("#nData", "#" + u + "_2").addClass("ui-state-disabled");
				} else {
					b("#nData", "#" + u + "_2").removeClass("ui-state-disabled");
				}
			}
			function X() {
				var Z = b(e).jqGrid("getDataIDs"), Y = b("#id_g", "#" + u).val(), aa = b.inArray(Y, Z);
				return [aa, Z];
			}
			function g() {
				var Y = true;
				b("#FormError", "#" + u).hide();
				if (a.checkOnUpdate) {
					E = {};
					R = {};
					W();
					H = b.extend({}, E, R);
					B = K(H, a._savedData);
					if (B) {
						b("#" + y).data("disabled", true);
						b(".confirm", "#" + i.themodal).show();
						Y = false;
					}
				}
				return Y;
			}
			function W() {
				b(".FormElement", "#" + u).each(function (aa) {
					var ad = b(".customelement", this);
					if (ad.length) {
						var ab = ad[0], Y = ab.name;
						b.each(e.p.colModel, function (ae, ag) {
							if (this.name == Y && this.editoptions && b.isFunction(this.editoptions.custom_value)) {
								try {
									E[Y] = this.editoptions.custom_value(b("#" + Y, "#" + u), "get");
									if (E[Y] === undefined) {
										throw "e1";
									}
								}
								catch (af) {
									if (af == "e1") {
										info_dialog(jQuery.jgrid.errors.errcap, "function 'custom_value' " + b.jgrid.edit.msg.novalue, jQuery.jgrid.edit.bClose);
									} else {
										info_dialog(jQuery.jgrid.errors.errcap, af.message, jQuery.jgrid.edit.bClose);
									}
								}
								return true;
							}
						});
					} else {
						switch (b(this).get(0).type) {
						  case "checkbox":
							if (b(this).attr("checked")) {
								E[this.name] = b(this).val();
							} else {
								var Z = b(this).attr("offval");
								E[this.name] = Z;
							}
							break;
						  case "select-one":
							E[this.name] = b("option:selected", this).val();
							R[this.name] = b("option:selected", this).text();
							break;
						  case "select-multiple":
							E[this.name] = b(this).val();
							if (E[this.name]) {
								E[this.name] = E[this.name].join(",");
							} else {
								E[this.name] = "";
							}
							var ac = [];
							b("option:selected", this).each(function (ae, af) {
								ac[ae] = b(af).text();
							});
							R[this.name] = ac.join(",");
							break;
						  case "password":
						  case "text":
						  case "textarea":
						  case "button":
							E[this.name] = b(this).val();
							E[this.name] = !e.p.autoencode ? E[this.name] : b.jgrid.htmlEncode(E[this.name]);
							break;
						}
					}
				});
				return true;
			}
			function s(ab, ah, ae, am) {
				var Y, Z, aj, ak = 0, ao, ap, ai, an = [], af = false, ag, aa, ac = "<td class='CaptionTD ui-widget-content'>&#160;</td><td class='DataTD ui-widget-content' style='white-space:pre'>&#160;</td>", ad = "";
				for (var al = 1; al <= am; al++) {
					ad += ac;
				}
				if (ab != "_empty") {
					af = b(ah).jqGrid("getInd", ab);
				}
				b(ah.p.colModel).each(function (au) {
					Y = this.name;
					if (this.editrules && this.editrules.edithidden == true) {
						Z = false;
					} else {
						Z = this.hidden === true ? true : false;
					}
					ap = Z ? "style='display:none'" : "";
					if (Y !== "cb" && Y !== "subgrid" && this.editable === true && Y !== "rn" && Y !== "rb") {
						if (af === false) {
							ao = "";
						} else {
							if (Y == ah.p.ExpandColumn && ah.p.treeGrid === true) {
								ao = b("td:eq(" + au + ")", ah.rows[af]).text();
							} else {
								try {
									ao = b.unformat(b("td:eq(" + au + ")", ah.rows[af]), {colModel:this}, au);
								}
								catch (ar) {
									ao = b("td:eq(" + au + ")", ah.rows[af]).html();
								}
							}
						}
						var at = b.extend({}, this.editoptions || {}, {id:Y, name:Y});
						frmopt = b.extend({}, {elmprefix:"", elmsuffix:"", rowabove:false, rowcontent:""}, this.formoptions || {}), ag = parseInt(frmopt.rowpos) || ak + 1, aa = parseInt((parseInt(frmopt.colpos) || 1) * 2);
						if (ab == "_empty" && at.defaultValue) {
							ao = b.isFunction(at.defaultValue) ? at.defaultValue() : at.defaultValue;
						}
						if (!this.edittype) {
							this.edittype = "text";
						}
						ai = createEl(this.edittype, at, ao, false, b.extend({}, b.jgrid.ajaxOptions, ah.p.ajaxSelectOptions || {}));
						if (ao == "" && this.edittype == "checkbox") {
							ao = b(ai).attr("offval");
						}
						if (a.checkOnSubmit || a.checkOnUpdate) {
							a._savedData[Y] = ao;
						}
						b(ai).addClass("FormElement");
						aj = b(ae).find("tr[rowpos=" + ag + "]");
						if (frmopt.rowabove) {
							var av = b("<tr><td class='contentinfo' colspan='" + (am * 2) + "'>" + frmopt.rowcontent + "</td></tr>");
							b(ae).append(av);
							av[0].rp = ag;
						}
						if (aj.length == 0) {
							aj = b("<tr " + ap + " rowpos='" + ag + "'></tr>").addClass("FormData").attr("id", "tr_" + Y);
							b(aj).append(ad);
							b(ae).append(aj);
							aj[0].rp = ag;
						}
						b("td:eq(" + (aa - 2) + ")", aj[0]).html(typeof frmopt.label === "undefined" ? ah.p.colNames[au] : frmopt.label);
						b("td:eq(" + (aa - 1) + ")", aj[0]).append(frmopt.elmprefix).append(ai).append(frmopt.elmsuffix);
						an[ak] = au;
						ak++;
					}
				});
				if (ak > 0) {
					var aq = b("<tr class='FormData' style='display:none'><td class='CaptionTD'></td><td colspan='" + (am * 2 - 1) + "' class='DataTD'><input class='FormElement' id='id_g' type='text' name='id' value='" + ab + "'/></td></tr>");
					aq[0].rp = ak + 999;
					b(ae).append(aq);
					if (a.checkOnSubmit || a.checkOnUpdate) {
						a._savedData.id = ab;
					}
				}
				return an;
			}
			function n(Z, af, ab) {
				var ak, ah, ac = 0, ag, ae, Y, ad, ai;
				if (a.checkOnSubmit || a.checkOnUpdate) {
					a._savedData = {};
					a._savedData.id = Z;
				}
				var aj = af.p.colModel;
				if (Z == "_empty") {
					b(aj).each(function (al) {
						ak = this.name;
						Y = b.extend({}, this.editoptions || {});
						ae = b("#" + b.jgrid.jqID(ak), "#" + ab);
						if (ae[0] != null) {
							ad = "";
							if (Y.defaultValue) {
								ad = b.isFunction(Y.defaultValue) ? Y.defaultValue() : Y.defaultValue;
								if (ae[0].type == "checkbox") {
									ai = ad.toLowerCase();
									if (ai.search(/(false|0|no|off|undefined)/i) < 0 && ai !== "") {
										ae[0].checked = true;
										ae[0].defaultChecked = true;
										ae[0].value = ad;
									} else {
										ae.attr({checked:"", defaultChecked:""});
									}
								} else {
									ae.val(ad);
								}
							} else {
								if (ae[0].type == "checkbox") {
									ae[0].checked = false;
									ae[0].defaultChecked = false;
									ad = b(ae).attr("offval");
								} else {
									if (ae[0].type.substr(0, 6) == "select") {
										ae[0].selectedIndex = 0;
									} else {
										ae.val(ad);
									}
								}
							}
							if (a.checkOnSubmit === true || a.checkOnUpdate) {
								a._savedData[ak] = ad;
							}
						}
					});
					b("#id_g", "#" + ab).val("_empty");
					return;
				}
				var aa = b(af).jqGrid("getInd", Z, true);
				if (!aa) {
					return;
				}
				b("td", aa).each(function (an) {
					ak = aj[an].name;
					if (ak !== "cb" && ak !== "subgrid" && ak !== "rn" && aj[an].editable === true && ak !== "rb") {
						if (ak == af.p.ExpandColumn && af.p.treeGrid === true) {
							ag = b(this).text();
						} else {
							try {
								ag = b.unformat(this, {colModel:aj[an]}, an);
							}
							catch (am) {
								ag = b(this).html();
							}
						}
						if (a.checkOnSubmit === true || a.checkOnUpdate) {
							a._savedData[ak] = ag;
						}
						ak = b.jgrid.jqID(ak);
						switch (aj[an].edittype) {
						  case "password":
						  case "text":
						  case "button":
						  case "image":
							ag = b.jgrid.htmlDecode(ag);
							b("#" + ak, "#" + ab).val(ag);
							break;
						  case "textarea":
							if (ag == "&nbsp;" || ag == "&#160;" || (ag.length == 1 && ag.charCodeAt(0) == 160)) {
								ag = "";
							}
							b("#" + ak, "#" + ab).val(ag);
							break;
						  case "select":
							var al = ag.split(",");
							al = b.map(al, function (aq) {
								return b.trim(aq);
							});
							b("#" + ak + " option", "#" + ab).each(function (aq) {
								if (!aj[an].editoptions.multiple && (al[0] == b(this).text() || al[0] == b(this).val())) {
									this.selected = true;
								} else {
									if (aj[an].editoptions.multiple) {
										if (b.inArray(b(this).text(), al) > -1 || b.inArray(b(this).val(), al) > -1) {
											this.selected = true;
										} else {
											this.selected = false;
										}
									} else {
										this.selected = false;
									}
								}
							});
							break;
						  case "checkbox":
							ag = ag + "";
							ag = ag.toLowerCase();
							if (ag.search(/(false|0|no|off|undefined)/i) < 0 && ag !== "") {
								b("#" + ak, "#" + ab).attr("checked", true);
								b("#" + ak, "#" + ab).attr("defaultChecked", true);
							} else {
								b("#" + ak, "#" + ab).attr("checked", false);
								b("#" + ak, "#" + ab).attr("defaultChecked", "");
							}
							break;
						  case "custom":
							try {
								if (aj[an].editoptions && b.isFunction(aj[an].editoptions.custom_value)) {
									var ap = aj[an].editoptions.custom_value(b("#" + ak, "#" + ab), "set", ag);
								} else {
									throw "e1";
								}
							}
							catch (ao) {
							
								if (ao == "e1") {
									info_dialog(jQuery.jgrid.errors.errcap, "function 'custom_value' " + b.jgrid.edit.msg.nodefined, jQuery.jgrid.edit.bClose);
								} else {
									info_dialog(jQuery.jgrid.errors.errcap, ao.message, jQuery.jgrid.edit.bClose);
								}
							}
							break;
						}
						ac++;
					}
				});
				if (ac > 0) {
					b("#id_g", "#" + u).val(Z);
				}
			}
			function j() {
				var ac, aa = [true, "", ""], Z = {};
				if (b.isFunction(a.beforeCheckValues)) {
					var Y = a.beforeCheckValues(E, b("#" + y), E.id == "_empty" ? "add" : "edit");
					if (Y && typeof (Y) === "object") {
						E = Y;
					}
				}
				for (var ab in E) {
					aa = checkValues(E[ab], ab, e);
					if (aa[0] == false) {
						break;
					}
				}
				if (aa[0]) {
					if (b.isFunction(a.onclickSubmit)) {
						Z = a.onclickSubmit(a, E) || {};
					}
					if (b.isFunction(a.beforeSubmit)) {
						aa = a.beforeSubmit(E, b("#" + y));
					}
				}
				v = a.url ? a.url : b(e).jqGrid("getGridParam", "editurl");
				if (aa[0]) {
					if (!v) {
						aa[0] = false;
						aa[1] += " " + b.jgrid.errors.nourl;
					}
				}
				if (aa[0] === false) {
					b("#FormError>td", "#" + u).html(aa[1]);
					b("#FormError", "#" + u).show();
					return;
				}
				if (!d.processing) {
					d.processing = true;
					b("#sData", "#" + u + "_2").addClass("ui-state-active");
					E.oper = E.id == "_empty" ? "add" : "edit";
					E = b.extend(E, a.editData, Z);
					b.ajax(b.extend({url:v, type:a.mtype, data:b.isFunction(a.serializeEditData) ? a.serializeEditData(E) : E, complete:function (ae, ad) {
						if (ad != "success") {
							aa[0] = false;
							if (b.isFunction(a.errorTextFormat)) {
								aa[1] = a.errorTextFormat(ae);
							} else {
								aa[1] = ad + " Status: '" + ae.statusText + "'. Error code: " + ae.status;
							}
						} else {
							if (b.isFunction(a.afterSubmit)) {
								aa = a.afterSubmit(ae, E);
							}
						}
						if (aa[0] === false) {
							b("#FormError>td", "#" + u).html(aa[1]);
							b("#FormError", "#" + u).show();
						} else {
							b.each(e.p.colModel, function (ag, ai) {
								if (R[this.name] && this.formatter && this.formatter == "select") {
									try {
										delete R[this.name];
									}
									catch (ah) {
									}
								}
							});
							E = b.extend(E, R);
							if (E.id == "_empty") {
								if (!aa[2]) {
									aa[2] = parseInt(e.p.records) + 1;
								}
								E.id = aa[2];
								if (a.closeAfterAdd) {
									if (a.reloadAfterSubmit) {
										b(e).trigger("reloadGrid");
									} else {
										b(e).jqGrid("addRowData", aa[2], E, d.addedrow);
										b(e).jqGrid("setSelection", aa[2]);
									}
									hideModal("#" + i.themodal, {gb:"#gbox_" + C, jqm:d.jqModal, onClose:a.onClose});
								} else {
									if (a.clearAfterAdd) {
										if (a.reloadAfterSubmit) {
											b(e).trigger("reloadGrid");
										} else {
											b(e).jqGrid("addRowData", aa[2], E, d.addedrow);
										}
										n("_empty", e, y);
									} else {
										if (a.reloadAfterSubmit) {
											b(e).trigger("reloadGrid");
										} else {
											b(e).jqGrid("addRowData", aa[2], E, d.addedrow);
										}
									}
								}
							} else {
								if (a.reloadAfterSubmit) {
									b(e).trigger("reloadGrid");
									if (!a.closeAfterEdit) {
										setTimeout(function () {
											b(e).jqGrid("setSelection", E.id);
										}, 1000);
									}
								} else {
									if (e.p.treeGrid === true) {
										b(e).jqGrid("setTreeRow", E.id, E);
									} else {
										b(e).jqGrid("setRowData", E.id, E);
									}
								}
								if (a.closeAfterEdit) {
									hideModal("#" + i.themodal, {gb:"#gbox_" + C, jqm:d.jqModal, onClose:a.onClose});
								}
							}
							if (b.isFunction(a.afterComplete)) {
								ac = ae;
								setTimeout(function () {
									a.afterComplete(ac, E, b("#" + y));
									ac = null;
								}, 500);
							}
						}
						d.processing = false;
						if (a.checkOnSubmit || a.checkOnUpdate) {
							b("#" + y).data("disabled", false);
							if (a._savedData.id != "_empty") {
								a._savedData = E;
							}
						}
						b("#sData", "#" + u + "_2").removeClass("ui-state-active");
						try {
							b(":input:visible", "#" + y)[0].focus();
						}
						catch (af) {
						}
					}, error:function (af, ad, ae) {
						b("#FormError>td", "#" + u).html(ad + " : " + ae);
						b("#FormError", "#" + u).show();
						d.processing = false;
						b("#" + y).data("disabled", false);
						b("#sData", "#" + u + "_2").removeClass("ui-state-active");
					}}, b.jgrid.ajaxOptions, a.ajaxEditOptions));
				}
			}
			function K(ab, Y) {
				var Z = false, aa;
				for (aa in ab) {
					if (ab[aa] != Y[aa]) {
						Z = true;
						break;
					}
				}
				return Z;
			}
		});
	}, viewGridRow:function (c, d) {
		d = b.extend({top:0, left:0, width:0, height:"auto", dataheight:"auto", modal:false, drag:true, resize:true, jqModal:true, closeOnEscape:false, labelswidth:"30%", closeicon:[], navkeys:[false, 38, 40], onClose:null}, b.jgrid.view, d || {});
		return this.each(function () {
			var x = this;
			if (!x.grid || !c) {
				return;
			}
			if (!d.imgpath) {
				d.imgpath = x.p.imgpath;
			}
			var r = x.p.id, A = "ViewGrid_" + r, s = "ViewTbl_" + r, j = {themodal:"viewmod" + r, modalhead:"viewhd" + r, modalcontent:"viewcnt" + r, scrollelm:A}, h = 1, f = 0;
			if (b("#" + j.themodal).html() != null) {
				b(".ui-jqdialog-title", "#" + j.modalhead).html(d.caption);
				b("#FormError", "#" + s).hide();
				m(c, x);
				viewModal("#" + j.themodal, {gbox:"#gbox_" + r, jqm:d.jqModal, jqM:false, modal:d.modal});
				k();
			} else {
				b(x.p.colModel).each(function (F) {
					var G = this.formoptions;
					h = Math.max(h, G ? G.colpos || 0 : 0);
					f = Math.max(f, G ? G.rowpos || 0 : 0);
				});
				var y = isNaN(d.dataheight) ? d.dataheight : d.dataheight + "px";
				var w, C = b("<form name='FormPost' id='" + A + "' class='FormGrid' style='width:100%;overflow:auto;position:relative;height:" + y + ";'></form>"), l = b("<table id='" + s + "' class='EditTable' cellspacing='1' cellpading='2' border='0' style='table-layout:fixed'><tbody></tbody></table>");
				b(C).append(l);
				var v = n(c, x, l, h), z = x.p.direction == "rtl" ? true : false, E = z ? "nData" : "pData", g = z ? "pData" : "nData", t = "<a href='javascript:void(0)' id='" + E + "' class='fm-button ui-state-default ui-corner-left'><span class='ui-icon ui-icon-triangle-1-w'></span></div>", u = "<a href='javascript:void(0)' id='" + g + "' class='fm-button ui-state-default ui-corner-right'><span class='ui-icon ui-icon-triangle-1-e'></span></div>", D = "<a href='javascript:void(0)' id='cData' class='fm-button ui-state-default ui-corner-all'>" + d.bClose + "</a>";
				if (f > 0) {
					var e = [];
					b.each(b(l)[0].rows, function (F, G) {
						e[F] = G;
					});
					e.sort(function (G, F) {
						if (G.rp > F.rp) {
							return 1;
						}
						if (G.rp < F.rp) {
							return -1;
						}
						return 0;
					});
					b.each(e, function (F, G) {
						b("tbody", l).append(G);
					});
				}
				d.gbox = "#gbox_" + r;
				var q = false;
				if (d.closeOnEscape === true) {
					d.closeOnEscape = false;
					q = true;
				}
				var B = b("<span></span>").append(C).append("<table border='0' class='EditTable' id='" + s + "_2'><tbody><tr id='Act_Buttons'><td class='navButton ui-widget-content' width='" + d.labelswidth + "'>" + (z ? u + t : t + u) + "</td><td class='EditButton ui-widget-content'>" + D + "</td></tr></tbody></table>");
				createModal(j, B, d, "#gview_" + x.p.id, b("#gview_" + x.p.id)[0]);
				if (z) {
					b("#pData, #nData", "#" + s + "_2").css("float", "right");
					b(".EditButton", "#" + s + "_2").css("text-align", "left");
				}
				B = null;
				b("#" + j.themodal).keydown(function (F) {
					if (F.which === 27) {
						if (q) {
							hideModal(this, {gb:d.gbox, jqm:d.jqModal, onClose:d.onClose});
						}
						return false;
					}
					if (d.navkeys[0] === true) {
						if (F.which === d.navkeys[1]) {
							b("#pData", "#" + s + "_2").trigger("click");
							return false;
						}
						if (F.which === d.navkeys[2]) {
							b("#nData", "#" + s + "_2").trigger("click");
							return false;
						}
					}
				});
				d.closeicon = b.extend([true, "left", "ui-icon-close"], d.closeicon);
				if (d.closeicon[0] == true) {
					b("#cData", "#" + s + "_2").addClass(d.closeicon[1] == "right" ? "fm-button-icon-right" : "fm-button-icon-left").append("<span class='ui-icon " + d.closeicon[2] + "'></span>");
				}
				viewModal("#" + j.themodal, {gbox:"#gbox_" + r, jqm:d.jqModal, modal:d.modal});
				b(".fm-button:not(.ui-state-disabled)", "#" + s + "_2").hover(function () {
					b(this).addClass("ui-state-hover");
				}, function () {
					b(this).removeClass("ui-state-hover");
				});
				k();
				b("#cData", "#" + s + "_2").click(function (F) {
					hideModal("#" + j.themodal, {gb:"#gbox_" + r, jqm:d.jqModal, onClose:d.onClose});
					return false;
				});
				b("#nData", "#" + s + "_2").click(function (F) {
					b("#FormError", "#" + s).hide();
					var G = i();
					G[0] = parseInt(G[0]);
					if (G[0] != -1 && G[1][G[0] + 1]) {
						if (b.isFunction(d.onclickPgButtons)) {
							d.onclickPgButtons("next", b("#" + A), G[1][G[0]]);
						}
						m(G[1][G[0] + 1], x);
						b(x).jqGrid("setSelection", G[1][G[0] + 1]);
						if (b.isFunction(d.afterclickPgButtons)) {
							d.afterclickPgButtons("next", b("#" + A), G[1][G[0] + 1]);
						}
						o(G[0] + 1, G[1].length - 1);
					}
					k();
					return false;
				});
				b("#pData", "#" + s + "_2").click(function (G) {
					b("#FormError", "#" + s).hide();
					var F = i();
					if (F[0] != -1 && F[1][F[0] - 1]) {
						if (b.isFunction(d.onclickPgButtons)) {
							d.onclickPgButtons("prev", b("#" + A), F[1][F[0]]);
						}
						m(F[1][F[0] - 1], x);
						b(x).jqGrid("setSelection", F[1][F[0] - 1]);
						if (b.isFunction(d.afterclickPgButtons)) {
							d.afterclickPgButtons("prev", b("#" + A), F[1][F[0] - 1]);
						}
						o(F[0] - 1, F[1].length - 1);
					}
					k();
					return false;
				});
			}
			function k() {
				if (d.closeOnEscape === true || d.navkeys[0] === true) {
					setTimeout(function () {
						b(".ui-jqdialog-titlebar-close", "#" + j.modalhead).focus();
					}, 0);
				}
			}
			var p = i();
			o(p[0], p[1].length - 1);
			function o(G, H, F) {
				if (G == 0) {
					b("#pData", "#" + s + "_2").addClass("ui-state-disabled");
				} else {
					b("#pData", "#" + s + "_2").removeClass("ui-state-disabled");
				}
				if (G == H) {
					b("#nData", "#" + s + "_2").addClass("ui-state-disabled");
				} else {
					b("#nData", "#" + s + "_2").removeClass("ui-state-disabled");
				}
			}
			function i() {
				var G = b(x).jqGrid("getDataIDs"), F = b("#id_g", "#" + s).val(), H = b.inArray(F, G);
				return [H, G];
			}
			function n(L, R, P, X) {
				var H, K, S, aa, F, V = 0, Z, ab, Y = [], Q = false, N = "<td class='CaptionTD ui-widget-content' width='" + d.labelswidth + "'>&#160;</td><td class='DataTD ui-helper-reset ui-widget-content' style='white-space:pre;'>&#160;</td>", O = "", I = "<td class='CaptionTD ui-widget-content'>&#160;</td><td class='DataTD ui-widget-content' style='white-space:pre;'>&#160;</td>", M = ["integer", "number", "currency"], U = 0, T = 0, J, G;
				for (var W = 1; W <= X; W++) {
					O += W == 1 ? N : I;
				}
				b(R.p.colModel).each(function (ad) {
					if (this.editrules && this.editrules.edithidden === true) {
						K = false;
					} else {
						K = this.hidden === true ? true : false;
					}
					if (!K && this.align === "right") {
						if (this.formatter && b.inArray(this.formatter, M) !== -1) {
							U = Math.max(U, parseInt(this.width, 10));
						} else {
							T = Math.max(T, parseInt(this.width, 10));
						}
					}
				});
				J = U !== 0 ? U : T !== 0 ? T : 0;
				Q = b(R).jqGrid("getInd", L);
				b(R.p.colModel).each(function (ae) {
					H = this.name;
					G = false;
					if (this.editrules && this.editrules.edithidden === true) {
						K = false;
					} else {
						K = this.hidden === true ? true : false;
					}
					ab = K ? "style='display:none'" : "";
					if (H !== "cb" && H !== "subgrid" && H !== "rn" && H !== "rb") {
						if (Q === false) {
							Z = "";
						} else {
							if (H == R.p.ExpandColumn && R.p.treeGrid === true) {
								Z = b("td:eq(" + ae + ")", R.rows[Q]).text();
							} else {
								Z = b("td:eq(" + ae + ")", R.rows[Q]).html();
							}
						}
						G = this.align === "right" && J !== 0 ? true : false;
						var ad = b.extend({}, this.editoptions || {}, {id:H, name:H}), ai = b.extend({}, {rowabove:false, rowcontent:""}, this.formoptions || {}), af = parseInt(ai.rowpos) || V + 1, ah = parseInt((parseInt(ai.colpos) || 1) * 2);
						if (ai.rowabove) {
							var ag = b("<tr><td class='contentinfo' colspan='" + (X * 2) + "'>" + ai.rowcontent + "</td></tr>");
							b(P).append(ag);
							ag[0].rp = af;
						}
						S = b(P).find("tr[rowpos=" + af + "]");
						if (S.length == 0) {
							S = b("<tr " + ab + " rowpos='" + af + "'></tr>").addClass("FormData").attr("id", "trv_" + H);
							b(S).append(O);
							b(P).append(S);
							S[0].rp = af;
						}
						b("td:eq(" + (ah - 2) + ")", S[0]).html("<b>" + (typeof ai.label === "undefined" ? R.p.colNames[ae] : ai.label) + "</b>");
						b("td:eq(" + (ah - 1) + ")", S[0]).append("<span>" + Z + "</span>").attr("id", "v_" + H);
						if (G) {
							b("td:eq(" + (ah - 1) + ") span", S[0]).css({"text-align":"right", width:J + "px"});
						}
						Y[V] = ae;
						V++;
					}
				});
				if (V > 0) {
					var ac = b("<tr class='FormData' style='display:none'><td class='CaptionTD'></td><td colspan='" + (X * 2 - 1) + "' class='DataTD'><input class='FormElement' id='id_g' type='text' name='id' value='" + L + "'/></td></tr>");
					ac[0].rp = V + 99;
					b(P).append(ac);
				}
				return Y;
			}
			function m(J, L) {
				var F, M, I = 0, H, G, K;
				K = b(L).jqGrid("getInd", J, true);
				if (!K) {
					return;
				}
				b("td", K).each(function (N) {
					F = L.p.colModel[N].name;
					if (L.p.colModel[N].editrules && L.p.colModel[N].editrules.edithidden === true) {
						M = false;
					} else {
						M = L.p.colModel[N].hidden === true ? true : false;
					}
					if (F !== "cb" && F !== "subgrid" && F !== "rn" && F != "rb") {
						if (F == L.p.ExpandColumn && L.p.treeGrid === true) {
							H = b(this).text();
						} else {
							H = b(this).html();
						}
						G = b.extend({}, L.p.colModel[N].editoptions || {});
						F = b.jgrid.jqID("v_" + F);
						b("#" + F + " span", "#" + s).html(H);
						if (M) {
							b("#" + F, "#" + s).parents("tr:first").hide();
						}
						I++;
					}
				});
				if (I > 0) {
					b("#id_g", "#" + s).val(J);
				}
			}
		});
	}, delGridRow:function (c, d) {
		d = b.extend({top:0, left:0, width:240, height:"auto", dataheight:"auto", modal:false, drag:true, resize:true, url:"", mtype:"POST", reloadAfterSubmit:true, beforeShowForm:null, afterShowForm:null, beforeSubmit:null, onclickSubmit:null, afterSubmit:null, jqModal:true, closeOnEscape:false, delData:{}, delicon:[], cancelicon:[], onClose:null, ajaxDelOptions:{}, serializeDelData:null}, b.jgrid.del, d || {});
		a = d;
		return this.each(function () {
			var l = this;
			if (!l.grid) {
				return;
			}
			if (!c) {
				return;
			}
			var m = typeof d.beforeShowForm === "function" ? true : false, g = typeof d.afterShowForm === "function" ? true : false, e = l.p.id, f = {}, j = "DelTbl_" + e, h = {themodal:"delmod" + e, modalhead:"delhd" + e, modalcontent:"delcnt" + e, scrollelm:j};
			if (isArray(c)) {
				c = c.join();
			}
			if (b("#" + h.themodal).html() != null) {
				b("#DelData>td", "#" + j).text(c);
				b("#DelError", "#" + j).hide();
				if (d.processing === true) {
					d.processing = false;
					b("#dData", "#" + j).removeClass("ui-state-active");
				}
				if (m) {
					d.beforeShowForm(b("#" + j));
				}
				viewModal("#" + h.themodal, {gbox:"#gbox_" + e, jqm:d.jqModal, jqM:false, modal:d.modal});
				if (g) {
					d.afterShowForm(b("#" + j));
				}
			} else {
				var n = isNaN(d.dataheight) ? d.dataheight : d.dataheight + "px";
				var k = "<div id='" + j + "' class='formdata' style='width:100%;overflow:auto;position:relative;height:" + n + ";'>";
				k += "<table class='DelTable'><tbody>";
				k += "<tr id='DelError' style='display:none'><td class='ui-state-error'></td></tr>";
				k += "<tr id='DelData' style='display:none'><td >" + c + "</td></tr>";
				k += "<tr><td class=\"delmsg\" style=\"white-space:pre;\">" + d.msg + "</td></tr><tr><td >&#160;</td></tr>";
				k += "</tbody></table></div>";
				var i = "<a href='javascript:void(0)' id='dData' class='fm-button ui-state-default ui-corner-all'>" + d.bSubmit + "</a>", o = "<a href='javascript:void(0)' id='eData' class='fm-button ui-state-default ui-corner-all'>" + d.bCancel + "</a>";
				k += "<table cellspacing='0' cellpadding='0' border='0' class='EditTable' id='" + j + "_2'><tbody><tr><td class='DataTD ui-widget-content'></td></tr><tr style='display:block;height:3px;'><td></td></tr><tr><td class='DelButton EditButton'>" + i + "&#160;" + o + "</td></tr></tbody></table>";
				d.gbox = "#gbox_" + e;
				createModal(h, k, d, "#gview_" + l.p.id, b("#gview_" + l.p.id)[0]);
				b(".fm-button", "#" + j + "_2").hover(function () {
					b(this).addClass("ui-state-hover");
				}, function () {
					b(this).removeClass("ui-state-hover");
				});
				d.delicon = b.extend([true, "left", "ui-icon-scissors"], d.delicon);
				d.cancelicon = b.extend([true, "left", "ui-icon-cancel"], d.cancelicon);
				if (d.delicon[0] == true) {
					b("#dData", "#" + j + "_2").addClass(d.delicon[1] == "right" ? "fm-button-icon-right" : "fm-button-icon-left").append("<span class='ui-icon " + d.delicon[2] + "'></span>");
				}
				if (d.cancelicon[0] == true) {
					b("#eData", "#" + j + "_2").addClass(d.cancelicon[1] == "right" ? "fm-button-icon-right" : "fm-button-icon-left").append("<span class='ui-icon " + d.cancelicon[2] + "'></span>");
				}
				b("#dData", "#" + j + "_2").click(function (s) {
					var q = [true, ""];
					f = {};
					var r = b("#DelData>td", "#" + j).text();
					if (typeof d.onclickSubmit === "function") {
						f = d.onclickSubmit(a) || {};
					}
					if (typeof d.beforeSubmit === "function") {
						q = d.beforeSubmit(r);
					}
					if (q[0]) {
						var p = a.url ? a.url : b(l).jqGrid("getGridParam", "editurl");
						if (!p) {
							q[0] = false;
							q[1] += " " + b.jgrid.errors.nourl;
						}
					}
					if (q[0] === false) {
						b("#DelError>td", "#" + j).html(q[1]);
						b("#DelError", "#" + j).show();
					} else {
						if (!d.processing) {
							d.processing = true;
							b(this).addClass("ui-state-active");
							var t = b.extend({oper:"del", id:r}, a.delData, f);
							b.ajax(b.extend({url:p, type:d.mtype, data:b.isFunction(d.serializeDelData) ? d.serializeDelData(t) : t, complete:function (x, v) {
								if (v != "success") {
									q[0] = false;
									if (b.isFunction(a.errorTextFormat)) {
										q[1] = a.errorTextFormat(x);
									} else {
										q[1] = v + " Status: '" + x.statusText + "'. Error code: " + x.status;
									}
								} else {
									if (typeof a.afterSubmit === "function") {
										q = a.afterSubmit(x, r);
									}
								}
								if (q[0] === false) {
									b("#DelError>td", "#" + j).html(q[1]);
									b("#DelError", "#" + j).show();
								} else {
									if (a.reloadAfterSubmit) {
										if (l.p.treeGrid) {
											b(l).jqGrid("setGridParam", {treeANode:0, datatype:l.p.treedatatype});
										}
										b(l).trigger("reloadGrid");
									} else {
										var u = [];
										u = r.split(",");
										if (l.p.treeGrid === true) {
											try {
												b(l).jqGrid("delTreeNode", u[0]);
											}
											catch (y) {
											}
										} else {
											for (var w = 0; w < u.length; w++) {
												b(l).jqGrid("delRowData", u[w]);
											}
										}
										l.p.selrow = null;
										l.p.selarrrow = [];
									}
									if (b.isFunction(a.afterComplete)) {
										setTimeout(function () {
											a.afterComplete(x, r);
										}, 500);
									}
								}
								d.processing = false;
								b("#dData", "#" + j + "_2").removeClass("ui-state-active");
								if (q[0]) {
									hideModal("#" + h.themodal, {gb:"#gbox_" + e, jqm:d.jqModal, onClose:a.onClose});
								}
							}, error:function (w, u, v) {
								b("#DelError>td", "#" + j).html(u + " : " + v);
								b("#DelError", "#" + j).show();
								d.processing = false;
								b("#dData", "#" + j + "_2").removeClass("ui-state-active");
							}}, b.jgrid.ajaxOptions, d.ajaxDelOptions));
						}
					}
					return false;
				});
				b("#eData", "#" + j + "_2").click(function (p) {
					hideModal("#" + h.themodal, {gb:"#gbox_" + e, jqm:d.jqModal, onClose:a.onClose});
					return false;
				});
				if (m) {
					d.beforeShowForm(b("#" + j));
				}
				viewModal("#" + h.themodal, {gbox:"#gbox_" + e, jqm:d.jqModal, modal:d.modal});
				if (g) {
					d.afterShowForm(b("#" + j));
				}
			}
			if (d.closeOnEscape === true) {
				setTimeout(function () {
					b(".ui-jqdialog-titlebar-close", "#" + h.modalhead).focus();
				}, 0);
			}
		});
	}, navGrid:function (f, h, e, g, d, c, i) {
		h = b.extend({edit:true, editicon:"ui-icon-pencil", add:false, addicon:"ui-icon-plus", del:false, delicon:"ui-icon-trash", search:false, searchicon:"ui-icon-search", refresh:false, refreshicon:"ui-icon-refresh", refreshstate:"firstpage", view:false, viewicon:"ui-icon-document", position:"left", closeOnEscape:true, afterRefresh:null}, b.jgrid.nav, h || {});
		return this.each(function () {
			var j = {themodal:"alertmod", modalhead:"alerthd", modalcontent:"alertcnt"}, n = this, m, s, o, k;
			if (!n.grid) {
				return;
			}
			if (b("#" + j.themodal).html() == null) {
				if (typeof window.innerWidth != "undefined") {
					m = window.innerWidth, s = window.innerHeight;
				} else {
					if (typeof document.documentElement != "undefined" && typeof document.documentElement.clientWidth != "undefined" && document.documentElement.clientWidth != 0) {
						m = document.documentElement.clientWidth, s = document.documentElement.clientHeight;
					} else {
						m = 1024;
						s = 768;
					}
				}
				createModal(j, "<div>" + h.alerttext + "</div><span tabindex='0'><span tabindex='-1' id='jqg_alrt'></span></span>", {gbox:"#gbox_" + n.p.id, jqModal:true, drag:true, resize:true, caption:h.alertcap, top:s / 2 - 25, left:m / 2 - 100, width:200, height:"auto", closeOnEscape:h.closeOnEscape}, "", "", true);
			}
			var p, q = b("<table cellspacing='0' cellpadding='0' border='0' class='ui-pg-table navtable' style='float:left;table-layout:auto;'><tbody><tr></tr></tbody></table>"), r = "<td class='ui-pg-button ui-state-disabled' style='width:4px;'><span class='ui-separator'></span></td>", l = b(n.p.pager).attr("id") || "pager";
			if (n.p.direction == "rtl") {
				b(q).attr("dir", "rtl").css("float", "right");
			}
			if (h.add) {
				g = g || {};
				p = b("<td class='ui-pg-button ui-corner-all'></td>");
				b(p).append("<div class='ui-pg-div'><span class='ui-icon " + h.addicon + "'></span>" + h.addtext + "</div>");
				b("tr", q).append(p);
				b(p, q).attr({title:h.addtitle || "", id:g.id || "add_" + n.p.id}).click(function () {
					if (typeof h.addfunc == "function") {
						h.addfunc();
					} else {
						b(n).jqGrid("editGridRow", "new", g);
					}
					return false;
				}).hover(function () {
					b(this).addClass("ui-state-hover");
				}, function () {
					b(this).removeClass("ui-state-hover");
				});
				p = null;
			}
			if (h.edit) {
				p = b("<td class='ui-pg-button ui-corner-all'></td>");
				e = e || {};
				b(p).append("<div class='ui-pg-div'><span class='ui-icon " + h.editicon + "'></span>" + h.edittext + "</div>");
				b("tr", q).append(p);
				b(p, q).attr({title:h.edittitle || "", id:e.id || "edit_" + n.p.id}).click(function () {
					var t = n.p.selrow;
					if (t) {
						if (typeof h.editfunc == "function") {
							h.editfunc(t);
						} else {
							b(n).jqGrid("editGridRow", t, e);
						}
					} else {
						viewModal("#" + j.themodal, {gbox:"#gbox_" + n.p.id, jqm:true});
						b("#jqg_alrt").focus();
					}
					return false;
				}).hover(function () {
					b(this).addClass("ui-state-hover");
				}, function () {
					b(this).removeClass("ui-state-hover");
				});
				p = null;
			}
			if (h.view) {
				p = b("<td class='ui-pg-button ui-corner-all'></td>");
				i = i || {};
				b(p).append("<div class='ui-pg-div'><span class='ui-icon " + h.viewicon + "'></span>" + h.viewtext + "</div>");
				b("tr", q).append(p);
				b(p, q).attr({title:h.viewtitle || "", id:i.id || "view_" + n.p.id}).click(function () {
					var t = n.p.selrow;
					if (t) {
						b(n).jqGrid("viewGridRow", t, i);
					} else {
						viewModal("#" + j.themodal, {gbox:"#gbox_" + n.p.id, jqm:true});
						b("#jqg_alrt").focus();
					}
					return false;
				}).hover(function () {
					b(this).addClass("ui-state-hover");
				}, function () {
					b(this).removeClass("ui-state-hover");
				});
				p = null;
			}
			if (h.del) {
				p = b("<td class='ui-pg-button ui-corner-all'></td>");
				d = d || {};
				b(p).append("<div class='ui-pg-div'><span class='ui-icon " + h.delicon + "'></span>" + h.deltext + "</div>");
				b("tr", q).append(p);
				b(p, q).attr({title:h.deltitle || "", id:d.id || "del_" + n.p.id}).click(function () {
					var t;
					if (n.p.multiselect) {
						t = n.p.selarrrow;
						if (t.length == 0) {
							t = null;
						}
					} else {
						t = n.p.selrow;
					}
					if (t) {
						b(n).jqGrid("delGridRow", t, d);
					} else {
						viewModal("#" + j.themodal, {gbox:"#gbox_" + n.p.id, jqm:true});
						b("#jqg_alrt").focus();
					}
					return false;
				}).hover(function () {
					b(this).addClass("ui-state-hover");
				}, function () {
					b(this).removeClass("ui-state-hover");
				});
				p = null;
			}
			if (h.add || h.edit || h.del || h.view) {
				b("tr", q).append(r);
			}
			if (h.search) {
				p = b("<td class='ui-pg-button ui-corner-all'></td>");
				c = c || {};
				b(p).append("<div class='ui-pg-div'><span class='ui-icon " + h.searchicon + "'></span>" + h.searchtext + "</div>");
				b("tr", q).append(p);
				b(p, q).attr({title:h.searchtitle || "", id:c.id || "search_" + n.p.id}).click(function () {
					b(n).jqGrid("searchGrid", c);
					return false;
				}).hover(function () {
					b(this).addClass("ui-state-hover");
				}, function () {
					b(this).removeClass("ui-state-hover");
				});
				p = null;
			}
			if (h.refresh) {
				p = b("<td class='ui-pg-button ui-corner-all'></td>");
				b(p).append("<div class='ui-pg-div'><span class='ui-icon " + h.refreshicon + "'></span>" + h.refreshtext + "</div>");
				b("tr", q).append(p);
				b(p, q).attr({title:h.refreshtitle || "", id:"refresh_" + n.p.id}).click(function () {
					n.p.search = false;
					try {
						var t = n.p.id;
						b("#fbox_" + t).searchFilter().reset();
					}
					catch (u) {
					}
					switch (h.refreshstate) {
					  case "firstpage":
						b(n).trigger("reloadGrid", [{page:1}]);
						break;
					  case "current":
						b(n).trigger("reloadGrid", [{current:true}]);
						break;
					}
					if (b.isFunction(h.afterRefresh)) {
						h.afterRefresh();
					}
					return false;
				}).hover(function () {
					b(this).addClass("ui-state-hover");
				}, function () {
					b(this).removeClass("ui-state-hover");
				});
				p = null;
			}
			k = b(".ui-jqgrid").css("font-size") || "11px";
			b("body").append("<div id='testpg2' class='ui-jqgrid ui-widget ui-widget-content' style='font-size:" + k + ";visibility:hidden;' ></div>");
			o = b(q).clone().appendTo("#testpg2").width();
			b("#testpg2").remove();
			b("#" + l + "_" + h.position, "#" + l).append(q);
			if (n.p._nvtd) {
				if (o > n.p._nvtd[0]) {
					b("#" + l + "_" + h.position, "#" + l).width(o);
					n.p._nvtd[0] = o;
				}
				n.p._nvtd[1] = o;
			}
		});
	},navButtonAdd:function (c, d) {
		d = b.extend({caption:"newButton", title:"", buttonicon:"ui-icon-newwin", onClickButton:null, position:"last", cursor:"pointer"}, d || {});
		return this.each(function () {
			if (!this.grid) {
				return;
			}
			if (c.indexOf("#") != 0) {
				c = "#" + c;
			}
			var e = b(".navtable", c)[0], g = this;
			if (e) {
				var f = b("<td></td>");
				b(f).addClass("ui-pg-button ui-corner-all").append("<div class='ui-pg-div'><span class='ui-icon " + d.buttonicon + "'></span>" + d.caption + "</div>");
				if (d.id) {
					b(f).attr("id", d.id);
				}
				if (d.position == "first") {
					if (e.rows[0].cells.length === 0) {
						b("tr", e).append(f);
					} else {
						b("tr td:eq(0)", e).before(f);
					}
				} else {
					b("tr", e).append(f);
				}
				b(f, e).attr("title", d.title || "").click(function (h) {
					if (b.isFunction(d.onClickButton)) {
						d.onClickButton.call(g, h);
					}
					return false;
				}).hover(function () {
					b(this).addClass("ui-state-hover");
				}, function () {
					b(this).removeClass("ui-state-hover");
				}).css("cursor", d.cursor ? d.cursor : "normal");
			}
		});
	}, navSeparatorAdd:function (c, d) {
		d = b.extend({sepclass:"ui-separator", sepcontent:""}, d || {});
		return this.each(function () {
			if (!this.grid) {
				return;
			}
			if (c.indexOf("#") != 0) {
				c = "#" + c;
			}
			var f = b(".navtable", c)[0];
			if (f) {
				var e = "<td class='ui-pg-button ui-state-disabled' style='width:4px;'><span class='" + d.sepclass + "'></span>" + d.sepcontent + "</td>";
				b("tr", f).append(e);
			}
		});
	}, GridToForm:function (c, d) {
		return this.each(function () {
			var g = this;
			if (!g.grid) {
				return;
			}
			var f = b(g).jqGrid("getRowData", c);
			if (f) {
				for (var e in f) {
					if (b("[name=" + e + "]", d).is("input:radio") || b("[name=" + e + "]", d).is("input:checkbox")) {
						b("[name=" + e + "]", d).each(function () {
							if (b(this).val() == f[e]) {
								b(this).attr("checked", "checked");
							} else {
								b(this).attr("checked", "");
							}
						});
					} else {
						b("[name=" + e + "]", d).val(f[e]);
					}
				}
			}
		});
	}, FormToGrid:function (d, e, f, c) {
		return this.each(function () {
			var i = this;
			if (!i.grid) {
				return;
			}
			if (!f) {
				f = "set";
			}
			if (!c) {
				c = "first";
			}
			var g = b(e).serializeArray();
			var h = {};
			b.each(g, function (j, k) {
				h[k.name] = k.value;
			});
			if (f == "add") {
				b(i).jqGrid("addRowData", d, h, c);
			} else {
				if (f == "set") {
					b(i).jqGrid("setRowData", d, h);
				}
			}
		});
	}});
})(jQuery);
jQuery.fn.searchFilter = function (a, c) {
	function b(j, p, g) {
		this.$ = j;
		this.add = function (z) {
			if (z == null) {
				j.find(".ui-add-last").click();
			} else {
				j.find(".sf:eq(" + z + ") .ui-add").click();
			}
			return this;
		};
		this.del = function (z) {
			if (z == null) {
				j.find(".sf:last .ui-del").click();
			} else {
				j.find(".sf:eq(" + z + ") .ui-del").click();
			}
			return this;
		};
		this.search = function (z) {
			j.find(".ui-search").click();
			return this;
		};
		this.reset = function (z) {
			j.find(".ui-reset").click();
			return this;
		};
		this.close = function () {
			j.find(".ui-closer").click();
			return this;
		};
		if (p != null) {
			function v() {
				jQuery(this).toggleClass("ui-state-hover");
				return false;
			}
			function i(z) {
				jQuery(this).toggleClass("ui-state-active", (z.type == "mousedown"));
				return false;
			}
			function e(z, A) {
				return "<option value='" + z + "'>" + A + "</option>";
			}
			function s(B, z, A) {
				return "<select class='" + B + "'" + (A ? " style='display:none;'" : "") + ">" + z + "</select>";
			}
			function w(z, B) {
				var A = j.find("tr.sf td.data " + z);
				if (A[0] != null) {
					B(A);
				}
			}
			function q(z, B) {
				var A = j.find("tr.sf td.data " + z);
				if (A[0] != null) {
					jQuery.each(B, function () {
						if (this.data != null) {
							A.bind(this.type, this.data, this.fn);
						} else {
							A.bind(this.type, this.fn);
						}
					});
				}
			}
			var n = jQuery.extend({}, jQuery.fn.searchFilter.defaults, g);
			var y = -1;
			var x = "";
			jQuery.each(n.groupOps, function () {
				x += e(this.op, this.text);
			});
			x = "<select name='groupOp'>" + x + "</select>";
			j.html("").addClass("ui-searchFilter").append("<div class='ui-widget-overlay' style='z-index: -1'>&#160;</div><table class='ui-widget-content ui-corner-all'><thead><tr><td colspan='5' class='ui-widget-header ui-corner-all' style='line-height: 18px;'><div class='ui-closer ui-state-default ui-corner-all ui-helper-clearfix' style='float: right;'><span class='ui-icon ui-icon-close'></span></div>" + n.windowTitle + "</td></tr></thead><tbody><tr class='sf'><td class='fields'></td><td class='ops'></td><td class='data'></td><td><div class='ui-del ui-state-default ui-corner-all'><span class='ui-icon ui-icon-minus'></span></div></td><td><div class='ui-add ui-state-default ui-corner-all'><span class='ui-icon ui-icon-plus'></span></div></td></tr><tr><td colspan='5' class='divider'><div>&#160;</div></td></tr></tbody><tfoot><tr><td colspan='3'><span class='ui-reset ui-state-default ui-corner-all' style='display: inline-block; float: left;'><span class='ui-icon ui-icon-arrowreturnthick-1-w' style='float: left;'></span><span style='line-height: 18px; padding: 0 7px 0 3px;'>" + n.resetText + "</span></span><span class='ui-search ui-state-default ui-corner-all' style='display: inline-block; float: right;'><span class='ui-icon ui-icon-search' style='float: left;'></span><span style='line-height: 18px; padding: 0 7px 0 3px;'>" + n.searchText + "</span></span><span class='matchText'>" + n.matchText + "</span> " + x + " <span class='rulesText'>" + n.rulesText + "</span></td><td>&#160;</td><td><div class='ui-add-last ui-state-default ui-corner-all'><span class='ui-icon ui-icon-plusthick'></span></div></td></tr></tfoot></table>");
			var k = j.find("tr.sf");
			var h = k.find("td.fields");
			var f = k.find("td.ops");
			var o = k.find("td.data");
			var r = "";
			jQuery.each(n.operators, function () {
				r += e(this.op, this.text);
			});
			r = s("default", r, true);
			f.append(r);
			var l = "<input type='text' class='default' style='display:none;' />";
			o.append(l);
			var u = "";
			var t = false;
			var d = false;
			jQuery.each(p, function (C) {
				var B = C;
				u += e(this.itemval, this.text);
				if (this.ops != null) {
					t = true;
					var z = "";
					jQuery.each(this.ops, function () {
						z += e(this.op, this.text);
					});
					z = s("field" + B, z, true);
					f.append(z);
				}
				if (this.dataUrl != null) {
					if (C > y) {
						y = C;
					}
					d = true;
					var F = this.dataEvents;
					var D = this.dataInit;
					var A = this.buildSelect;
					jQuery.ajax(jQuery.extend({url:this.dataUrl, complete:function (H) {
						var G;
						if (A != null) {
							G = jQuery("<div />").append(A(H));
						} else {
							G = jQuery("<div />").append(H.responseText);
						}
						G.find("select").addClass("field" + B).hide();
						o.append(G.html());
						if (D) {
							w(".field" + C, D);
						}
						if (F) {
							q(".field" + C, F);
						}
						if (C == y) {
							j.find("tr.sf td.fields select[name='field']").change();
						}
					}}, n.ajaxSelectOptions));
				} else {
					if (this.dataValues != null) {
						d = true;
						var E = "";
						jQuery.each(this.dataValues, function () {
							E += e(this.value, this.text);
						});
						E = s("field" + B, E, true);
						o.append(E);
					} else {
						if (this.dataEvents != null || this.dataInit != null) {
							d = true;
							var E = "<input type='text' class='field" + B + "' />";
							o.append(E);
						}
					}
				}
				if (this.dataInit != null && C != y) {
					w(".field" + C, this.dataInit);
				}
				if (this.dataEvents != null && C != y) {
					q(".field" + C, this.dataEvents);
				}
			});
			u = "<select name='field'>" + u + "</select>";
			h.append(u);
			var m = h.find("select[name='field']");
			if (t) {
				m.change(function (B) {
					var A = B.target.selectedIndex;
					var C = jQuery(B.target).parents("tr.sf").find("td.ops");
					C.find("select").removeAttr("name").hide();
					var z = C.find(".field" + A);
					if (z[0] == null) {
						z = C.find(".default");
					}
					z.attr("name", "op").show();
				});
			} else {
				f.find(".default").attr("name", "op").show();
			}
			if (d) {
				m.change(function (B) {
					var A = B.target.selectedIndex;
					var C = jQuery(B.target).parents("tr.sf").find("td.data");
					C.find("select,input").removeClass("vdata").hide();
					var z = C.find(".field" + A);
					if (z[0] == null) {
						z = C.find(".default");
					}
					z.show().addClass("vdata");
				});
			} else {
				o.find(".default").show().addClass("vdata");
			}
			if (t || d) {
				m.change();
			}
			j.find(".ui-state-default").hover(v, v).mousedown(i).mouseup(i);
			j.find(".ui-closer").click(function (z) {
				n.onClose(jQuery(j.selector));
				return false;
			});
			j.find(".ui-del").click(function (z) {
				var A = jQuery(z.target).parents(".sf");
				if (A.siblings(".sf").length > 0) {
					if (n.datepickerFix === true && jQuery.fn.datepicker !== undefined) {
						A.find(".hasDatepicker").datepicker("destroy");
					}
					A.remove();
				} else {
					A.find("select[name='field']")[0].selectedIndex = 0;
					A.find("select[name='op']")[0].selectedIndex = 0;
					A.find(".data input").val("");
					A.find(".data select").each(function () {
						this.selectedIndex = 0;
					});
					A.find("select[name='field']").change();
				}
				return false;
			});
			j.find(".ui-add").click(function (C) {
				var D = jQuery(C.target).parents(".sf");
				var B = D.clone(true).insertAfter(D);
				B.find(".ui-state-default").removeClass("ui-state-hover ui-state-active");
				if (n.clone) {
					B.find("select[name='field']")[0].selectedIndex = D.find("select[name='field']")[0].selectedIndex;
					var A = (B.find("select[name='op']")[0] == null);
					if (!A) {
						B.find("select[name='op']").focus()[0].selectedIndex = D.find("select[name='op']")[0].selectedIndex;
					}
					var z = B.find("select.vdata");
					if (z[0] != null) {
						z[0].selectedIndex = D.find("select.vdata")[0].selectedIndex;
					}
				} else {
					B.find(".data input").val("");
					B.find("select[name='field']").focus();
				}
				if (n.datepickerFix === true && jQuery.fn.datepicker !== undefined) {
					D.find(".hasDatepicker").each(function () {
						var E = jQuery.data(this, "datepicker").settings;
						B.find("#" + this.id).unbind().removeAttr("id").removeClass("hasDatepicker").datepicker(E);
					});
				}
				B.find("select[name='field']").change();
				return false;
			});
			j.find(".ui-search").click(function (C) {
				var B = jQuery(j.selector);
				var z;
				var A = B.find("select[name='groupOp'] :selected").val();
				if (!n.stringResult) {
					z = {groupOp:A, rules:[]};
				} else {
					z = "{\"groupOp\":\"" + A + "\",\"rules\":[";
				}
				B.find(".sf").each(function (D) {
					var G = jQuery(this).find("select[name='field'] :selected").val();
					var F = jQuery(this).find("select[name='op'] :selected").val();
					var E = jQuery(this).find("input.vdata,select.vdata :selected").val();
					if (!n.stringResult) {
						z.rules.push({field:G, op:F, data:E});
					} else {
						if (D > 0) {
							z += ",";
						}
						z += "{\"field\":\"" + G + "\",";
						z += "\"op\":\"" + F + "\",";
						z += "\"data\":\"" + E + "\"}";
					}
				});
				if (n.stringResult) {
					z += "]}";
				}
				n.onSearch(z);
				return false;
			});
			j.find(".ui-reset").click(function (A) {
				var z = jQuery(j.selector);
				z.find(".ui-del").click();
				z.find("select[name='groupOp']")[0].selectedIndex = 0;
				n.onReset();
				return false;
			});
			j.find(".ui-add-last").click(function () {
				var A = jQuery(j.selector + " .sf:last");
				var z = A.clone(true).insertAfter(A);
				z.find(".ui-state-default").removeClass("ui-state-hover ui-state-active");
				z.find(".data input").val("");
				z.find("select[name='field']").focus();
				if (n.datepickerFix === true && jQuery.fn.datepicker !== undefined) {
					A.find(".hasDatepicker").each(function () {
						var B = jQuery.data(this, "datepicker").settings;
						z.find("#" + this.id).unbind().removeAttr("id").removeClass("hasDatepicker").datepicker(B);
					});
				}
				z.find("select[name='field']").change();
				return false;
			});
		}
	}
	return new b(this, a, c);
};
jQuery.fn.searchFilter.version = "1.2.9";
jQuery.fn.searchFilter.defaults = {clone:true, datepickerFix:true, onReset:function (a) {
	alert("Reset Clicked. Data Returned: " + a);
}, onSearch:function (a) {
	alert("Search Clicked. Data Returned: " + a);
}, onClose:function (a) {
	a.hide();
}, groupOps:[{op:"AND", text:"all"}, {op:"OR", text:"any"}], operators:[{op:"eq", text:"is equal to"}, {op:"ne", text:"is not equal to"}, {op:"lt", text:"is less than"}, {op:"le", text:"is less or equal to"}, {op:"gt", text:"is greater than"}, {op:"ge", text:"is greater or equal to"}, {op:"in", text:"is in"}, {op:"ni", text:"is not in"}, {op:"bw", text:"begins with"}, {op:"bn", text:"does not begin with"}, {op:"ew", text:"ends with"}, {op:"en", text:"does not end with"}, {op:"cn", text:"contains"}, {op:"nc", text:"does not contain"}], matchText:"match", rulesText:"rules", resetText:"Reset", searchText:"Search", stringResult:true, windowTitle:"Search Rules", ajaxSelectOptions:{}};
(function (a) {
	a.jgrid.extend({editRow:function (c, i, h, j, b, e, d, f, g,type,beforeSave) {//增加一个type参数，表述是再原有行的基础上编辑，还是新增一样编辑，值为add,edit，增加一个参数beforeSave，表单验证失败时不做处理
		return this.each(function () {
			var n = this, s, o, l, m = 0, r = null, q = {}, k, p,value,z,flag = true;
			if (!n.grid) {
				return;
			}
			
			var editRows = n.p.savedRow;//当有行处于添加状态时，不允许编辑一行
			for(var v = 0 ;v< editRows.length;v++)
				{
				   var editRow = editRows[v].id;
				   z = a(n).jqGrid("getInd", editRow, true);
				   if(a(z).attr("saveType")=="add" && type == "edit")
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
			      a(n).jqGrid("saveRow",lastid , j, b, e, d, f, g,type,beforeSave);//确定，保存
			     }else{
			     if(lastid != c)
			     {
			     return;
			     }
			     } 
			     
			  }else{
			  //a(n).jqGrid("restoreRow", lastid, g,type);//取消，还原
			  return;
			  }
			}
			
			k = a(n).jqGrid("getInd", c, true);
			if (k == false) {
				return;
			}
			l = a(k).attr("editable") || "0";
			if (l == "0" && !a(k).hasClass("not-editable-row")) {
				p = n.p.colModel;
				a("td", k).each(function (w) {
					s = p[w].name;
					var v = n.p.treeGrid === true && s == n.p.ExpandColumn;
					if (v) {
						o = a("span:first", this).html();
					} else {	
							o = a(this).attr("value");//将取html值改成去value属性对应的值
							//alert(o);						
					}
					if (s != "cb" && s != "subgrid" && s != "rn" && s != "rb") {
						q[s] = o;
						if (p[w].editable === true) {
							if (r === null) {
								r = w;
							}
							if (v) {
								a("span:first", this).html("");
							} else {
								a(this).html("");
							}
							var u = a.extend({}, p[w].editoptions || {}, {id:c + "_" + s, name:s});
							if (!p[w].edittype) {
								p[w].edittype = "text";
							}
							var x = createEl(p[w].edittype, u, o, true, a.extend({}, a.jgrid.ajaxOptions, n.p.ajaxSelectOptions || {}));
							a(x).addClass("editable");
							if (v) {
								a("span:first", this).append(x);
							} else {
								a(this).append(x);
							}
							if (p[w].edittype == "select" && p[w].editoptions.multiple === true && a.browser.msie) {
								a(x).width(a(x).width());
							}
							m++;
						}
					}
				});
				if (m > 0) {
					q.id = c;
					n.p.savedRow.push(q);
			//		var kkkkkkkk = n.p.savedRow.length;
					a(k).attr("editable", "1");
					a("td:eq(" + r + ") input", k).focus();
					if (i === true) {
						a(k).parent().bind("keydown", function (t) {
							if (t.keyCode === 13) {
								var h = [];
								var k =n.p.savedRow;
							  	for(var z = 0;z<k.length;z++)
							  	{
							     	h.push(k[z].id);
							     	
							  	}
							  	if ($.isFunction(beforeSave)) {
					                     flag = beforeSave.call();
					 					}
				                       if(flag)
				                         {
			                                     a(n).jqGrid("saveRows",h, j, b, e, d, f, g,type);//确定，保存
			                             }else
			                             {
			                                return;
			                             } 
								
								return false;
							}
							t.stopPropagation();
						
						});
						a(k).bind("keydown", function(t){
							if (t.keyCode === 27) {
							
								a(n).jqGrid("restoreRow", c, g,type);
							}
						});
						
					}
					if (a.isFunction(h)) {
						h(c);
					}
				}
			}
		});
	}, saveRow:function (h, g, e, f, d, c, b,type,beforeSave) {//同editRow，保存一行数据
		return this.each(function () {
		
			var o = this,r,p = {},l = {},pl,flag = true;
			if (!o.grid) {
				return;
			}
			i = a(o).jqGrid("getInd", h, true);
			if (i == false) {
				return;
			}
                   if ($.isFunction(beforeSave)) {//添加表单验证
					                     flag = beforeSave.call();
					               
					 					}
				                       if(!flag)
				                         {			                            
			                                return;
			                             }
			e = e ? e : o.p.editurl;
			if (e) {
				pl = a(o).jqGrid("getSaveData",o,f,h);
				if(pl)
				{
				p = pl[0];
				l = pl[1];
				}
				if (!o.grid.hDiv.loading) {
					o.grid.hDiv.loading = true;
					a("div.loading", o.grid.hDiv).fadeIn("fast");
					if (e == "clientArray") {
						p = a.extend({}, p, l);
						var n = a(o).jqGrid("setRowData", h, p);
						a(i).attr("editable", "0");
						for (var m = 0; m < o.p.savedRow.length; m++) {
							if (o.p.savedRow[m].id == h) {
								r = m;
								break;
							}
						}
						if (r >= 0) {
							o.p.savedRow.splice(r, 1);
						}
						if (a.isFunction(d)) {
							d(h, n);
						}
					} else {
					   
						a.ajax(a.extend({url:e, data:a.isFunction(o.p.serializeRowData) ? o.p.serializeRowData(p) : p, type:"POST", complete:function (x, y) {

							if (y === "success") {
							
								var w;
								if (a.isFunction(g)) {
									w = g(x);
								} else {
									w = true;
								}
								if (w === true) {
									p = a.extend({}, p, l);
									a(o).jqGrid("setRowData",h, p);
									a(i).attr("editable", "0");
									a(i).attr("saveType","edit");
									for (var v = 0; v < o.p.savedRow.length; v++) {
										if (o.p.savedRow[v].id == h) {
											r = v;
											break;
										}
									}
									if (r >= 0) {
										o.p.savedRow.splice(r, 1);
									}
									if (a.isFunction(d)) {
										d(h, x);
									}
								} else {
									a(o).jqGrid("restoreRow", h, b,type);
								}
								var alertId = [];
								alertId.push(h);
								a(o).jqGrid("alertData",alertId);//修改计算列与合计列的值
							}
						}, error:function (k, v) {
							if (a.isFunction(c)) {
								c(h, k, v);
							} else {
								alert("Error Row: " + h + " Result: " + k.status + ":" + k.statusText + " Status: " + v);
								a(o).jqGrid("restoreRow", h, b,type);//编辑或保存出错时数据还原
							}
						}}, a.jgrid.ajaxOptions, o.p.ajaxRowOptions || {}));
					}
					o.grid.hDiv.loading = false;
					a("div.loading", o.grid.hDiv).fadeOut("fast");
					a(i).parent().unbind("keydown");
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
   
                       var k = a(n).jqGrid("getInd", h[hIndex], true);
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
                                              var tds = a(k).children();
                                              var value = tds.eq(g).attr("value");
                                              reg = new RegExp(result[0],"g");
                                     }
                                       str2 = str2.replace(reg,value) ;


                                }
                                var t = eval(str2);
                                var data = {};
                                data[nm] = t;
								a(n).jqGrid('setRowData',h[hIndex],data);
                         
							}
							if(typeof n.p.userData[nm] != "undefined")
							{
							    var trs = a("tr",n);
							    var udata = 0;
							    var usernm = Number(n.p.userData[nm]);
							    if(!isNaN(usernm))
							    {
							    for(var trIndex = 0;trIndex<trs.length;trIndex++)
							    {
							    var value = Number(a("td:eq(" + i + ")", trs[trIndex]).attr("value"));							    
							    if(!isNaN(value))
							    {
							        udata = udata + value;
							        
							    }
							    }							 
							    n.p.userData[nm] = udata;
							    }
							        if (n.p.userDataOnFooter) {
					                   a(n).jqGrid("footerData", "set", n.p.userData, true);
				                       }
							}
							
							
				});
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
				pl = a(o).jqGrid("getSaveData",o,f,h[v]);
				
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
					a("div.loading", o.grid.hDiv).fadeIn("fast");
					if (e == "clientArray") {
						p = a.extend({}, p, l);
						var n = a(o).jqGrid("setRowData", h, p);
						a(i).attr("editable", "0");
						for (var m = 0; m < o.p.savedRow.length; m++) {
							if (o.p.savedRow[m].id == h) {
								r = m;
								break;
							}
						}
						if (r >= 0) {
							o.p.savedRow.splice(r, 1);
						}
						if (a.isFunction(d)) {
							d(h, n);
						}
					} else {
						a.ajax(a.extend({url:e, data:a.isFunction(o.p.serializeRowData) ? o.p.serializeRowData(p) : p, type:"POST", complete:function (x, y) {
							if (y === "success") {
							
								var w;
								if (a.isFunction(g)) {
									w = g(x);
								} else {
									w = true;
								}
									if (a.isFunction(d)) {
										d(h, x);
									
									}
								for(var z = 0;z<h.length;z++){
								if (w === true) {
									p = a.extend({}, p, l);
									i = a(o).jqGrid("getInd", h[z], true);
			                        if (i == false) {
				                            continue;
			                            }
									a(o).jqGrid("setRowData",h[z], pAarry[z]);
									a(i).attr("editable", "0");
									a(i).attr("saveType","edit");
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
									a(o).jqGrid("restoreRow", h[z], b,type);
								}
							}
							a(o).jqGrid("alertData", h);//修改计算列与合计列的值
							}
						}, error:function (k, v) {
						for(var z = 0;z<h.length;z++){
							if (a.isFunction(c)) {
								c(h, k, v);
							} else {
								alert("Error Row: " + h + " Result: " + k.status + ":" + k.statusText + " Status: " + v);
													
								a(o).jqGrid("restoreRow", h[z], b,type);//编辑或保存出错时数据还原								
							}
							}
						}}, a.jgrid.ajaxOptions, o.p.ajaxRowOptions || {}));
					}
					o.grid.hDiv.loading = false;
					a("div.loading", o.grid.hDiv).fadeOut("fast");
					for(var z in h)
					{
					i = a(o).jqGrid("getInd", h[z], true);
					a(i).parent().unbind("keydown");
					break;
					}
	//				a(i).unbind("keydown");
				}
			}
		});
	
	},
	getSaveData:function(o,f,h){//得到要保存的某行的数据
	var u,i, p = {}, l = {}, q,pl;
	pl = [];
	if (!o.grid) {
				return;
			}
			i = a(o).jqGrid("getInd", h, true);
			if (i == false) {
				return;
			}
			j = a(i).attr("editable");
	if(j=='1')
	{
	     var t;
				a("td", i).each(function (v) {
					t = o.p.colModel[v];
					u = t.name;
					if (u != "cb" && u != "subgrid" && t.editable === true && u != "rn" && u !== "rb") {
					
						switch (t.edittype) {
						  case "checkbox":
							var k = ["Yes", "No"];
							if (t.editoptions) {
								k = t.editoptions.value.split(":");
							}
							p[u] = a("input", this).attr("checked") ? k[0] : k[1];
							break;
						  case "text":
						  case "password":
						  case "textarea":
						  case "button":
							p[u] = !o.p.autoencode ? a("input, textarea", this).val() : a.jgrid.htmlEncode(a("input, textarea", this).val());
							break;
						  case "select":
							if (!t.editoptions.multiple) {
								p[u] = a("select>option:selected", this).val();
								//l[u] = a("select>option:selected", this).text();
								l[u] = a("select>option:selected", this).attr("value");
							} else {
								var w = a("select", this), y = [];
								p[u] = a(w).val();
								if (p[u]) {
									p[u] = p[u].join(",");
								} else {
									p[u] = "";
								}
								a("select > option:selected", this).each(function (z, A) {
									//y[z] = a(A).text();
									y[z] = a(A).attr("value");//将取html值改成取原有的value值
								});
								l[u] = y.join(",");
							}
							if (t.formatter && t.formatter == "select") {
								l = {};
							}
							break;
						  case "custom":
							try {
								if (t.editoptions && a.isFunction(t.editoptions.custom_value)) {
									p[u] = t.editoptions.custom_value(a(".customelement", this), "get");
									if (p[u] === undefined) {
										throw "e2";
									}
								} else {
									throw "e1";
								}
							}
							catch (x) {
								if (x == "e1") {
									info_dialog(jQuery.jgrid.errors.errcap, "function 'custom_value' " + a.jgrid.edit.msg.nodefined, jQuery.jgrid.edit.bClose);
								}
								if (x == "e2") {
									info_dialog(jQuery.jgrid.errors.errcap, "function 'custom_value' " + a.jgrid.edit.msg.novalue, jQuery.jgrid.edit.bClose);
								} else {
									info_dialog(jQuery.jgrid.errors.errcap, x.message, jQuery.jgrid.edit.bClose);
								}
							}
							break;
						}
						q = checkValues(p[u], v, o);
						if (q[0] === false) {
							q[1] = p[u] + " " + q[1];
							return false;
						}
					}
				});
				if (q[0] === false) {
					try {
						info_dialog(a.jgrid.errors.errcap, q[1], a.jgrid.edit.bClose);
					}
					catch (s) {
						alert(q[1]);
					}
					return;
				}
				if (p) {
					p.id = h;
					if (f) {
						p = a.extend({}, p, f);
					}
				}
				
				 pl.push(p);
				 pl.push(l);
				return pl;
				}
	},  restoreRow:function (c, b,type) {//同editRow
		return this.each(function () {
			var i = this, d, g;
			if (!i.grid) {
				return;
			}
			g = a(i).jqGrid("getInd", c, true);
			if (g == false) {
				return;
			}
			for (var f = 0; f < i.p.savedRow.length; f++) {
				if (i.p.savedRow[f].id == c) {
					d = f;
					break;
				}
			}
			if (d >= 0) {
				if (a.isFunction(a.fn.datepicker)) {
					try {
						a("input.hasDatepicker", "#" + g.id).datepicker("hide");
					}
					catch (h) {
					}
				}
				a(i).jqGrid("setRowData", c, i.p.savedRow[d]);
				if(type == "add")//当type为add时将新增的行删除
				{
				   $("#"+c).remove();
				}
				a(g).attr("editable", "0").unbind("keydown");
				i.p.savedRow.splice(d, 1);
			}
			if (a.isFunction(b)) {
				b(c);
			}
		});
	}});
})(jQuery);
(function (a) {
	a.jgrid.extend({editCell:function (d, c, b) {
		return this.each(function () {
			var j = this, m, k, g;
			if (!j.grid || j.p.cellEdit !== true) {
				return;
			}
			c = parseInt(c, 10);
			j.p.selrow = j.rows[d].id;
			if (!j.p.knv) {
				a(j).jqGrid("GridNav");
			}
			if (j.p.savedRow.length > 0) {
				if (b === true) {
					if (d == j.p.iRow && c == j.p.iCol) {
						return;
					}
				}
				var h = a("td:eq(" + j.p.savedRow[0].ic + ")>#" + j.p.savedRow[0].id + "_" + a.jgrid.jqID(j.p.savedRow[0].name), j.rows[j.p.savedRow[0].id]).val();
				if (j.p.savedRow[0].v != h) {
					a(j).jqGrid("saveCell", j.p.savedRow[0].id, j.p.savedRow[0].ic);
				} else {
					a(j).jqGrid("restoreCell", j.p.savedRow[0].id, j.p.savedRow[0].ic);
				}
			} else {
				window.setTimeout(function () {
					a("#" + j.p.knv).attr("tabindex", "-1").focus();
				}, 0);
			}
			m = j.p.colModel[c].name;
			if (m == "subgrid" || m == "cb" || nm == "rn" || nm == "rb") {
				return;
			}
			g = a("td:eq(" + c + ")", j.rows[d]);
			if (j.p.colModel[c].editable === true && b === true && !g.hasClass("not-editable-cell")) {
				if (parseInt(j.p.iCol) >= 0 && parseInt(j.p.iRow) >= 0) {
					a("td:eq(" + j.p.iCol + ")", j.rows[j.p.iRow]).removeClass("edit-cell ui-state-highlight");
					a(j.rows[j.p.iRow]).removeClass("selected-row ui-state-hover");
				}
				a(g).addClass("edit-cell ui-state-highlight");
				a(j.rows[d]).addClass("selected-row ui-state-hover");
				try {
					k = a.unformat(g, {colModel:j.p.colModel[c]}, c);
				}
				catch (l) {
					k = a(g).html();
				}
				if (!j.p.colModel[c].edittype) {
					j.p.colModel[c].edittype = "text";
				}
				j.p.savedRow.push({id:d, ic:c, name:m, v:k});
				if (a.isFunction(j.p.formatCell)) {
					var i = j.p.formatCell(j.rows[d].id, m, k, d, c);
					if (i != undefined) {
						k = i;
					}
				}
				var f = a.extend({}, j.p.colModel[c].editoptions || {}, {id:d + "_" + m, name:m});
				var e = createEl(j.p.colModel[c].edittype, f, k, true, a.extend({}, a.jgrid.ajaxOptions, j.p.ajaxSelectOptions || {}));
				if (a.isFunction(j.p.beforeEditCell)) {
					j.p.beforeEditCell(j.rows[d].id, m, k, d, c);
				}
				a(g).html("").append(e).attr("tabindex", "0");
				window.setTimeout(function () {
					a(e).focus();
				}, 0);
				a("input, select, textarea", g).bind("keydown", function (n) {
					if (n.keyCode === 27) {
						if (a("input.hasDatepicker", g).length > 0) {
							if (a(".ui-datepicker").is(":hidden")) {
								a(j).jqGrid("restoreCell", d, c);
							} else {
								a("input.hasDatepicker", g).datepicker("hide");
							}
						} else {
							a(j).jqGrid("restoreCell", d, c);
						}
					}
					if (n.keyCode === 13) {
						a(j).jqGrid("saveCell", d, c);
					}
					if (n.keyCode == 9) {
						if (n.shiftKey) {
							a(j).jqGrid("prevCell", d, c);
						} else {
							a(j).jqGrid("nextCell", d, c);
						}
					}
					n.stopPropagation();
				});
				if (a.isFunction(j.p.afterEditCell)) {
					j.p.afterEditCell(j.rows[d].id, m, k, d, c);
				}
			} else {
				if (parseInt(j.p.iCol) >= 0 && parseInt(j.p.iRow) >= 0) {
					a("td:eq(" + j.p.iCol + ")", j.rows[j.p.iRow]).removeClass("edit-cell ui-state-highlight");
					a(j.rows[j.p.iRow]).removeClass("selected-row ui-state-hover");
				}
				g.addClass("edit-cell ui-state-highlight");
				a(j.rows[d]).addClass("selected-row ui-state-hover");
				if (a.isFunction(j.p.onSelectCell)) {
					k = g.html().replace(/\&#160\;/ig, "");
					j.p.onSelectCell(j.rows[d].id, m, k, d, c);
				}
			}
			j.p.iCol = c;
			j.p.iRow = d;
		});
	}, saveCell:function (c, b) {
		return this.each(function () {
			var k = this, m;
			if (!k.grid || k.p.cellEdit !== true) {
				return;
			}
			if (k.p.savedRow.length >= 1) {
				m = 0;
			} else {
				m = null;
			}
			if (m != null) {
				var h = a("td:eq(" + b + ")", k.rows[c]), s, p, r = k.p.colModel[b], t = r.name, g = a.jgrid.jqID(t);
				switch (r.edittype) {
				  case "select":
					if (!r.editoptions.multiple) {
						s = a("#" + c + "_" + g + ">option:selected", k.rows[c]).val();
						p = a("#" + c + "_" + g + ">option:selected", k.rows[c]).text();
					} else {
						var d = a("#" + c + "_" + g, k.rows[c]), f = [];
						s = a(d).val();
						if (s) {
							s.join(",");
						} else {
							s = "";
						}
						a("option:selected", d).each(function (e, u) {
							f[e] = a(u).text();
						});
						p = f.join(",");
					}
					if (r.formatter) {
						p = s;
					}
					break;
				  case "checkbox":
					var i = ["Yes", "No"];
					if (r.editoptions) {
						i = r.editoptions.value.split(":");
					}
					s = a("#" + c + "_" + g, k.rows[c]).attr("checked") ? i[0] : i[1];
					p = s;
					break;
				  case "password":
				  case "text":
				  case "textarea":
				  case "button":
					s = !k.p.autoencode ? a("#" + c + "_" + g, k.rows[c]).val() : a.jgrid.htmlEncode(a("#" + c + "_" + g, k.rows[c]).val());
					p = s;
					break;
				  case "custom":
					try {
						if (r.editoptions && a.isFunction(r.editoptions.custom_value)) {
							s = r.editoptions.custom_value(a(".customelement", h), "get");
							if (s === undefined) {
								throw "e2";
							} else {
								p = s;
							}
						} else {
							throw "e1";
						}
					}
					catch (n) {
						if (n == "e1") {
							info_dialog(jQuery.jgrid.errors.errcap, "function 'custom_value' " + a.jgrid.edit.msg.nodefined, jQuery.jgrid.edit.bClose);
						}
						if (n == "e2") {
							info_dialog(jQuery.jgrid.errors.errcap, "function 'custom_value' " + a.jgrid.edit.msg.novalue, jQuery.jgrid.edit.bClose);
						} else {
							info_dialog(jQuery.jgrid.errors.errcap, n.message, jQuery.jgrid.edit.bClose);
						}
					}
					break;
				}
				if (p != k.p.savedRow[m].v) {
					if (a.isFunction(k.p.beforeSaveCell)) {
						var q = k.p.beforeSaveCell(k.rows[c].id, t, s, c, b);
						if (q) {
							s = q;
						}
					}
					var j = checkValues(s, b, k);
					if (j[0] === true) {
						var l = {};
						if (a.isFunction(k.p.beforeSubmitCell)) {
							l = k.p.beforeSubmitCell(k.rows[c].id, t, s, c, b);
							if (!l) {
								l = {};
							}
						}
						if (p == "") {
							p = " ";
						}
						if (a("input.hasDatepicker", h).length > 0) {
							a("input.hasDatepicker", h).datepicker("hide");
						}
						if (k.p.cellsubmit == "remote") {
							if (k.p.cellurl) {
								var o = {};
								o[t] = s;
								o.id = k.rows[c].id;
								o = a.extend(l, o);
								a.ajax(a.extend({url:k.p.cellurl, data:a.isFunction(k.p.serializeCellData) ? k.p.serializeCellData(o) : o, type:"POST", complete:function (e, v) {
									if (v == "success") {
										if (a.isFunction(k.p.afterSubmitCell)) {
											var u = k.p.afterSubmitCell(e, o.id, t, s, c, b);
											if (u[0] === true) {
												a(h).empty();
												a(k).jqGrid("setCell", k.rows[c].id, b, p);
												a(h).addClass("dirty-cell");
												a(k.rows[c]).addClass("edited");
												if (a.isFunction(k.p.afterSaveCell)) {
													k.p.afterSaveCell(k.rows[c].id, t, s, c, b);
												}
												k.p.savedRow.splice(0, 1);
											} else {
												info_dialog(a.jgrid.errors.errcap, u[1], a.jgrid.edit.bClose);
												a(k).jqGrid("restoreCell", c, b);
											}
										} else {
											a(h).empty();
											a(k).jqGrid("setCell", k.rows[c].id, b, p);
											a(h).addClass("dirty-cell");
											a(k.rows[c]).addClass("edited");
											if (a.isFunction(k.p.afterSaveCell)) {
												k.p.afterSaveCell(k.rows[c].id, t, s, c, b);
											}
											k.p.savedRow.splice(0, 1);
										}
									}
								}, error:function (e, u) {
									if (a.isFunction(k.p.errorCell)) {
										k.p.errorCell(e, u);
										a(k).jqGrid("restoreCell", c, b);
									} else {
										info_dialog(a.jgrid.errors.errcap, e.status + " : " + e.statusText + "<br/>" + u, a.jgrid.edit.bClose);
										a(k).jqGrid("restoreCell", c, b);
									}
								}}, a.jgrid.ajaxOptions, k.p.ajaxCellOptions || {}));
							} else {
								try {
									info_dialog(a.jgrid.errors.errcap, a.jgrid.errors.nourl, a.jgrid.edit.bClose);
									a(k).jqGrid("restoreCell", c, b);
								}
								catch (n) {
								}
							}
						}
						if (k.p.cellsubmit == "clientArray") {
							a(h).empty();
							a(k).jqGrid("setCell", k.rows[c].id, b, p);
							a(h).addClass("dirty-cell");
							a(k.rows[c]).addClass("edited");
							if (a.isFunction(k.p.afterSaveCell)) {
								k.p.afterSaveCell(k.rows[c].id, t, s, c, b);
							}
							k.p.savedRow.splice(0, 1);
						}
					} else {
						try {
							window.setTimeout(function () {
								info_dialog(a.jgrid.errors.errcap, s + " " + j[1], a.jgrid.edit.bClose);
							}, 100);
							a(k).jqGrid("restoreCell", c, b);
						}
						catch (n) {
						}
					}
				} else {
					a(k).jqGrid("restoreCell", c, b);
				}
			}
			if (a.browser.opera) {
				a("#" + k.p.knv).attr("tabindex", "-1").focus();
			} else {
				window.setTimeout(function () {
					a("#" + k.p.knv).attr("tabindex", "-1").focus();
				}, 0);
			}
		});
	}, restoreCell:function (c, b) {
		return this.each(function () {
			var h = this, d;
			if (!h.grid || h.p.cellEdit !== true) {
				return;
			}
			if (h.p.savedRow.length >= 1) {
				d = 0;
			} else {
				d = null;
			}
			if (d != null) {
				var g = a("td:eq(" + b + ")", h.rows[c]);
				if (a.isFunction(a.fn.datepicker)) {
					try {
						a("input.hasDatepicker", g).datepicker("hide");
					}
					catch (f) {
					}
				}
				a(g).empty().attr("tabindex", "-1");
				a(h).jqGrid("setCell", h.rows[c].id, b, h.p.savedRow[d].v);
				h.p.savedRow.splice(0, 1);
			}
			window.setTimeout(function () {
				a("#" + h.p.knv).attr("tabindex", "-1").focus();
			}, 0);
		});
	}, nextCell:function (c, b) {
		return this.each(function () {
			var f = this, e = false;
			if (!f.grid || f.p.cellEdit !== true) {
				return;
			}
			for (var d = b + 1; d < f.p.colModel.length; d++) {
				if (f.p.colModel[d].editable === true) {
					e = d;
					break;
				}
			}
			if (e !== false) {
				a(f).jqGrid("editCell", c, e, true);
			} else {
				if (f.p.savedRow.length > 0) {
					a(f).jqGrid("saveCell", c, b);
				}
			}
		});
	}, prevCell:function (c, b) {
		return this.each(function () {
			var f = this, e = false;
			if (!f.grid || f.p.cellEdit !== true) {
				return;
			}
			for (var d = b - 1; d >= 0; d--) {
				if (f.p.colModel[d].editable === true) {
					e = d;
					break;
				}
			}
			if (e !== false) {
				a(f).jqGrid("editCell", c, e, true);
			} else {
				if (f.p.savedRow.length > 0) {
					a(f).jqGrid("saveCell", c, b);
				}
			}
		});
	}, GridNav:function () {
		return this.each(function () {
			var g = this;
			if (!g.grid || g.p.cellEdit !== true) {
				return;
			}
			g.p.knv = g.p.id + "_kn";
			var f = a("<span style='width:0px;height:0px;background-color:black;' tabindex='0'><span tabindex='-1' style='width:0px;height:0px;background-color:grey' id='" + g.p.knv + "'></span></span>"), d, c;
			a(f).insertBefore(g.grid.cDiv);
			a("#" + g.p.knv).focus().keydown(function (h) {
				c = h.keyCode;
				if (g.p.direction == "rtl") {
					if (c == 37) {
						c = 39;
					} else {
						if (c == 39) {
							c = 37;
						}
					}
				}
				switch (c) {
				  case 38:
					if (g.p.iRow - 1 >= 0) {
						e(g.p.iRow - 1, g.p.iCol, "vu");
						a(g).jqGrid("editCell", g.p.iRow - 1, g.p.iCol, false);
					}
					break;
				  case 40:
					if (g.p.iRow + 1 <= g.rows.length - 1) {
						e(g.p.iRow + 1, g.p.iCol, "vd");
						a(g).jqGrid("editCell", g.p.iRow + 1, g.p.iCol, false);
					}
					break;
				  case 37:
					if (g.p.iCol - 1 >= 0) {
						d = b(g.p.iCol - 1, "lft");
						e(g.p.iRow, d, "h");
						a(g).jqGrid("editCell", g.p.iRow, d, false);
					}
					break;
				  case 39:
					if (g.p.iCol + 1 <= g.p.colModel.length - 1) {
						d = b(g.p.iCol + 1, "rgt");
						e(g.p.iRow, d, "h");
						a(g).jqGrid("editCell", g.p.iRow, d, false);
					}
					break;
				  case 13:
					if (parseInt(g.p.iCol, 10) >= 0 && parseInt(g.p.iRow, 10) >= 0) {
						a(g).jqGrid("editCell", g.p.iRow, g.p.iCol, true);
					}
					break;
				}
				return false;
			});
			function e(p, n, o) {
				if (o.substr(0, 1) == "v") {
					var h = a(g.grid.bDiv)[0].clientHeight, q = a(g.grid.bDiv)[0].scrollTop, r = g.rows[p].offsetTop + g.rows[p].clientHeight, l = g.rows[p].offsetTop;
					if (o == "vd") {
						if (r >= h) {
							a(g.grid.bDiv)[0].scrollTop = a(g.grid.bDiv)[0].scrollTop + g.rows[p].clientHeight;
						}
					}
					if (o == "vu") {
						if (l < q) {
							a(g.grid.bDiv)[0].scrollTop = a(g.grid.bDiv)[0].scrollTop - g.rows[p].clientHeight;
						}
					}
				}
				if (o == "h") {
					var k = a(g.grid.bDiv)[0].clientWidth, j = a(g.grid.bDiv)[0].scrollLeft, i = g.rows[p].cells[n].offsetLeft + g.rows[p].cells[n].clientWidth, m = g.rows[p].cells[n].offsetLeft;
					if (i >= k + parseInt(j)) {
						a(g.grid.bDiv)[0].scrollLeft = a(g.grid.bDiv)[0].scrollLeft + g.rows[p].cells[n].clientWidth;
					} else {
						if (m < j) {
							a(g.grid.bDiv)[0].scrollLeft = a(g.grid.bDiv)[0].scrollLeft - g.rows[p].cells[n].clientWidth;
						}
					}
				}
			}
			function b(l, h) {
				var k, j;
				if (h == "lft") {
					k = l + 1;
					for (j = l; j >= 0; j--) {
						if (g.p.colModel[j].hidden !== true) {
							k = j;
							break;
						}
					}
				}
				if (h == "rgt") {
					k = l - 1;
					for (j = l; j < g.p.colModel.length; j++) {
						if (g.p.colModel[j].hidden !== true) {
							k = j;
							break;
						}
					}
				}
				return k;
			}
		});
	}, getChangedCells:function (c) {
		var b = [];
		if (!c) {
			c = "all";
		}
		this.each(function () {
			var e = this, d;
			if (!e.grid || e.p.cellEdit !== true) {
				return;
			}
			a(e.rows).each(function (f) {
				var g = {};
				if (a(this).hasClass("edited")) {
					a("td", this).each(function (h) {
						d = e.p.colModel[h].name;
						if (d !== "cb" && d !== "subgrid" && d!== "rb") {
							if (c == "dirty") {
								if (a(this).hasClass("dirty-cell")) {
									try {
										g[d] = a.unformat(this, {colModel:e.p.colModel[h]}, h);
									}
									catch (j) {
										g[d] = a.jgrid.htmlDecode(a(this).html());
									}
								}
							} else {
								try {
									g[d] = a.unformat(this, {colModel:e.p.colModel[h]}, h);
								}
								catch (j) {
									g[d] = a.jgrid.htmlDecode(a(this).html());
								}
							}
						}
					});
					g.id = this.id;
					b.push(g);
				}
			});
		});
		return b;
	}});
})(jQuery);
(function (d) {
	d.fn.jqm = function (f) {
		var e = {overlay:50, closeoverlay:true, overlayClass:"jqmOverlay", closeClass:"jqmClose", trigger:".jqModal", ajax:l, ajaxText:"", target:l, modal:l, toTop:l, onShow:l, onHide:l, onLoad:l};
		return this.each(function () {
			if (this._jqm) {
				return k[this._jqm].c = d.extend({}, k[this._jqm].c, f);
			}
			n++;
			this._jqm = n;
			k[n] = {c:d.extend(e, d.jqm.params, f), a:l, w:d(this).addClass("jqmID" + n), s:n};
			if (e.trigger) {
				d(this).jqmAddTrigger(e.trigger);
			}
		});
	};
	d.fn.jqmAddClose = function (f) {
		return j(this, f, "jqmHide");
	};
	d.fn.jqmAddTrigger = function (f) {
		return j(this, f, "jqmShow");
	};
	d.fn.jqmShow = function (e) {
		return this.each(function () {
			d.jqm.open(this._jqm, e);
		});
	};
	d.fn.jqmHide = function (e) {
		return this.each(function () {
			d.jqm.close(this._jqm, e);
		});
	};
	d.jqm = {hash:{}, open:function (B, A) {
		var p = k[B], q = p.c, m = "." + q.closeClass, v = (parseInt(p.w.css("z-index")));
		v = (v > 0) ? v : 3000;
		var f = d("<div></div>").css({height:"100%", width:"100%", position:"fixed", left:0, top:0, "z-index":v - 1, opacity:q.overlay / 100});
		if (p.a) {
			return l;
		}
		p.t = A;
		p.a = true;
		p.w.css("z-index", v);
		if (q.modal) {
			if (!a[0]) {
				setTimeout(function () {
					i("bind");
				}, 1);
			}
			a.push(B);
		} else {
			if (q.overlay > 0) {
				if (q.closeoverlay) {
					p.w.jqmAddClose(f);
				}
			} else {
				f = l;
			}
		}
		p.o = (f) ? f.addClass(q.overlayClass).prependTo("body") : l;
		if (c) {
			d("html,body").css({height:"100%", width:"100%"});
			if (f) {
				f = f.css({position:"absolute"})[0];
				for (var w in {Top:1, Left:1}) {
					f.style.setExpression(w.toLowerCase(), "(_=(document.documentElement.scroll" + w + " || document.body.scroll" + w + "))+'px'");
				}
			}
		}
		if (q.ajax) {
			var e = q.target || p.w, x = q.ajax;
			e = (typeof e == "string") ? d(e, p.w) : d(e);
			x = (x.substr(0, 1) == "@") ? d(A).attr(x.substring(1)) : x;
			e.html(q.ajaxText).load(x, function () {
				if (q.onLoad) {
					q.onLoad.call(this, p);
				}
				if (m) {
					p.w.jqmAddClose(d(m, p.w));
				}
				h(p);
			});
		} else {
			if (m) {
				p.w.jqmAddClose(d(m, p.w));
			}
		}
		if (q.toTop && p.o) {
			p.w.before("<span id=\"jqmP" + p.w[0]._jqm + "\"></span>").insertAfter(p.o);
		}
		(q.onShow) ? q.onShow(p) : p.w.show();
		h(p);
		return l;
	}, close:function (f) {
		var e = k[f];
		if (!e.a) {
			return l;
		}
		e.a = l;
		if (a[0]) {
			a.pop();
			if (!a[0]) {
				i("unbind");
			}
		}
		if (e.c.toTop && e.o) {
			d("#jqmP" + e.w[0]._jqm).after(e.w).remove();
		}
		if (e.c.onHide) {
			e.c.onHide(e);
		} else {
			e.w.hide();
			if (e.o) {
				e.o.remove();
			}
		}
		return l;
	}, params:{}};
	var n = 0, k = d.jqm.hash, a = [], c = d.browser.msie && (d.browser.version == "6.0"), l = false, h = function (f) {
		var e = d("<iframe src=\"javascript:false;document.write('');\" class=\"jqm\"></iframe>").css({opacity:0});
		if (c) {
			if (f.o) {
				f.o.html("<p style=\"width:100%;height:100%\"/>").prepend(e);
			} else {
				if (!d("iframe.jqm", f.w)[0]) {
					f.w.prepend(e);
				}
			}
		}
		g(f);
	}, g = function (f) {
		try {
			d(":input:visible", f.w)[0].focus();
		}
		catch (e) {
		}
	}, i = function (e) {
		d()[e]("keypress", b)[e]("keydown", b)[e]("mousedown", b);
	}, b = function (o) {
		var f = k[a[a.length - 1]], m = (!d(o.target).parents(".jqmID" + f.s)[0]);
		if (m) {
			g(f);
		}
		return !m;
	}, j = function (e, f, m) {
		return e.each(function () {
			var o = this._jqm;
			d(f).each(function () {
				if (!this[m]) {
					this[m] = [];
					d(this).click(function () {
						for (var p in {jqmShow:1, jqmHide:1}) {
							for (var q in this[p]) {
								if (k[this[p][q]]) {
									k[this[p][q]].w[p](this);
								}
							}
						}
						return l;
					});
				}
				this[m].push(o);
			});
		});
	};
})(jQuery);
(function (g) {
	g.fn.jqDrag = function (f) {
		return c(this, f, "d");
	};
	g.fn.jqResize = function (i, f) {
		return c(this, i, "r", f);
	};
	g.jqDnR = {dnr:{}, e:0, drag:function (f) {
		if (h.k == "d") {
			e.css({left:h.X + f.pageX - h.pX, top:h.Y + f.pageY - h.pY});
		} else {
			e.css({width:Math.max(f.pageX - h.pX + h.W, 0), height:Math.max(f.pageY - h.pY + h.H, 0)});
			if (M1) {
				a.css({width:Math.max(f.pageX - M1.pX + M1.W, 0), height:Math.max(f.pageY - M1.pY + M1.H, 0)});
			}
		}
		return false;
	}, stop:function () {
		g().unbind("mousemove", b.drag).unbind("mouseup", b.stop);
	}};
	var b = g.jqDnR, h = b.dnr, e = b.e, a, c = function (l, j, i, f) {
		return l.each(function () {
			j = (j) ? g(j, l) : l;
			j.bind("mousedown", {e:l, k:i}, function (k) {
				var o = k.data, n = {};
				e = o.e;
				a = f ? g(f) : false;
				if (e.css("position") != "relative") {
					try {
						e.position(n);
					}
					catch (m) {
					}
				}
				h = {X:n.left || d("left") || 0, Y:n.top || d("top") || 0, W:d("width") || e[0].scrollWidth || 0, H:d("height") || e[0].scrollHeight || 0, pX:k.pageX, pY:k.pageY, k:o.k};
				if (a && o.k != "d") {
					M1 = {X:n.left || f1("left") || 0, Y:n.top || f1("top") || 0, W:a[0].offsetWidth || f1("width") || 0, H:a[0].offsetHeight || f1("height") || 0, pX:k.pageX, pY:k.pageY, k:o.k};
				} else {
					M1 = false;
				}
				g().mousemove(g.jqDnR.drag).mouseup(g.jqDnR.stop);
				return false;
			});
		});
	}, d = function (f) {
		return parseInt(e.css(f)) || false;
	};
	f1 = function (f) {
		return parseInt(a.css(f)) || false;
	};
})(jQuery);
(function (a) {
	a.jgrid.extend({setSubGrid:function () {
		return this.each(function () {
			var c = this, b;
			c.p.colNames.unshift("");
			c.p.colModel.unshift({name:"subgrid", width:a.browser.safari ? c.p.subGridWidth + c.p.cellLayout : c.p.subGridWidth, sortable:false, resizable:false, hidedlg:true, search:false, fixed:true});
			b = c.p.subGridModel;
			if (b[0]) {
				b[0].align = a.extend([], b[0].align || []);
				for (i = 0; i < b[0].name.length; i++) {
					b[0].align[i] = b[0].align[i] || "left";
				}
			}
		});
	}, addSubGridCell:function (e, c) {
		var b = "", d;
		this.each(function () {
			b = this.formatCol(e, c);
			d = this.p.gridview;
		});
		if (d === false) {
			return "<td role='grid' class='ui-sgcollapsed sgcollapsed' " + b + "><a href='javascript:void(0);'><span class='ui-icon ui-icon-plus'></span></a></td>";
		} else {
			return "<td role='grid' " + b + "></td>";
		}
	}, addSubGrid:function (b, c) {
		return this.each(function () {
			var m = this;
			if (!m.grid) {
				return;
			}
			var n, o, p, j, k, g, h;
			a("td:eq(" + c + ")", b).click(function (q) {
				if (a(this).hasClass("sgcollapsed")) {
					p = m.p.id;
					n = a(this).parent();
					j = c >= 1 ? "<td colspan='" + c + "'>&#160;</td>" : "";
					o = a(n).attr("id");
					h = true;
					if (a.isFunction(m.p.subGridBeforeExpand)) {
						h = m.p.subGridBeforeExpand(p + "_" + o, o);
					}
					if (h === false) {
						return false;
					}
					k = 0;
					a.each(m.p.colModel, function (s, r) {
						if (this.hidden === true || this.name == "rn" || this.name == "cb" || this.name == "rb" ) {
							k++;
						}
					});
					g = "<tr role='row' class='ui-subgrid'>" + j + "<td class='ui-widget-content subgrid-cell'><span class='ui-icon ui-icon-carat-1-sw'/></td><td colspan='" + parseInt(m.p.colNames.length - 1 - k) + "' class='ui-widget-content subgrid-data'><div id=" + p + "_" + o + " class='tablediv'>";
					a(this).parent().after(g + "</div></td></tr>");
					if (a.isFunction(m.p.subGridRowExpanded)) {
						m.p.subGridRowExpanded(p + "_" + o, o);
					} else {
						l(n);
					}
					a(this).html("<a href='javascript:void(0);'><span class='ui-icon ui-icon-minus'></span></a>").removeClass("sgcollapsed").addClass("sgexpanded");
				} else {
					if (a(this).hasClass("sgexpanded")) {
						h = true;
						if (a.isFunction(m.p.subGridRowColapsed)) {
							n = a(this).parent();
							o = a(n).attr("id");
							h = m.p.subGridRowColapsed(p + "_" + o, o);
						}
						if (h === false) {
							return false;
						}
						a(this).parent().next().remove(".ui-subgrid");
						a(this).html("<a href='javascript:void(0);'><span class='ui-icon ui-icon-plus'></span></a>").removeClass("sgexpanded").addClass("sgcollapsed");
					}
				}
				return false;
			});
			var l = function (u) {
				var t, q, v, s, r;
				q = a(u).attr("id");
				v = {id:q, nd_:(new Date().getTime())};
				if (!m.p.subGridModel[0]) {
					return false;
				}
				if (m.p.subGridModel[0].params) {
					for (r = 0; r < m.p.subGridModel[0].params.length; r++) {
						for (s = 0; s < m.p.colModel.length; s++) {
							if (m.p.colModel[s].name == m.p.subGridModel[0].params[r]) {
								v[m.p.colModel[s].name] = a("td:eq(" + s + ")", u).text().replace(/\&#160\;/ig, "");
							}
						}
					}
				}
				if (!m.grid.hDiv.loading) {
					m.grid.hDiv.loading = true;
					a("#load_" + m.p.id).show();
					if (!m.p.subgridtype) {
						m.p.subgridtype = m.p.datatype;
					}
					m.p.subgridtype = m.p.subgridtype.toLowerCase();
					if (a.isFunction(m.p.subgridtype)) {
						m.p.subgridtype(v);
					}
					switch (m.p.subgridtype) {
					  case "xml":
					  case "json":
						a.ajax(a.extend({type:m.p.mtype, url:m.p.subGridUrl, dataType:m.p.subgridtype, data:a.isFunction(m.p.serializeSubGridData) ? m.p.serializeSubGridData(v) : v, complete:function (w) {
							if (m.p.subgridtype == "xml") {
								d(w.responseXML, q);
							} else {
								f(a.jgrid.parse(w.responseText), q);
							}
							w = null;
						}}, a.jgrid.ajaxOptions, m.p.ajaxSubgridOptions || {}));
						break;
					}
				}
				return false;
			};
			var e = function (r, q, t) {
				var s = a("<td align='" + m.p.subGridModel[0].align[t] + "'></td>").html(q);
				a(r).append(s);
			};
			var d = function (v, t) {
				var x, u, w, q, s = a("<table cellspacing='0' cellpadding='0' border='0'><tbody></tbody></table>"), r = a("<tr></tr>");
				for (u = 0; u < m.p.subGridModel[0].name.length; u++) {
					x = a("<th class='ui-state-default ui-th-column ui-th-" + m.p.direction + "'></th>");
					a(x).html(m.p.subGridModel[0].name[u]);
					a(x).width(m.p.subGridModel[0].width[u]);
					a(r).append(x);
				}
				a(s).append(r);
				if (v) {
					q = m.p.xmlReader.subgrid;
					a(q.root + " " + q.row, v).each(function () {
						r = a("<tr class='ui-widget-content ui-subtblcell'></tr>");
						if (q.repeatitems === true) {
							a(q.cell, this).each(function (A) {
								e(r, a(this).text() || "&#160;", A);
							});
						} else {
							var z = m.p.subGridModel[0].mapping || m.p.subGridModel[0].name;
							if (z) {
								for (u = 0; u < z.length; u++) {
									e(r, a(z[u], this).text() || "&#160;", u);
								}
							}
						}
						a(s).append(r);
					});
				}
				var y = a("table:first", m.grid.bDiv).attr("id") + "_";
				a("#" + y + t).append(s);
				m.grid.hDiv.loading = false;
				a("#load_" + m.p.id).hide();
				return false;
			};
			var f = function (x, u) {
				var z, B, v, y, q, s = a("<table cellspacing='0' cellpadding='0' border='0'><tbody></tbody></table>"), r = a("<tr></tr>");
				for (v = 0; v < m.p.subGridModel[0].name.length; v++) {
					z = a("<th class='ui-state-default ui-th-column ui-th-" + m.p.direction + "'></th>");
					a(z).html(m.p.subGridModel[0].name[v]);
					a(z).width(m.p.subGridModel[0].width[v]);
					a(r).append(z);
				}
				a(s).append(r);
				if (x) {
					q = m.p.jsonReader.subgrid;
					B = x[q.root];
					if (typeof B !== "undefined") {
						for (v = 0; v < B.length; v++) {
							y = B[v];
							r = a("<tr class='ui-widget-content ui-subtblcell'></tr>");
							if (q.repeatitems === true) {
								if (q.cell) {
									y = y[q.cell];
								}
								for (var t = 0; t < y.length; t++) {
									e(r, y[t] || "&#160;", t);
								}
							} else {
								var w = m.p.subGridModel[0].mapping || m.p.subGridModel[0].name;
								if (w.length) {
									for (var t = 0; t < w.length; t++) {
										e(r, y[w[t]] || "&#160;", t);
									}
								}
							}
							a(s).append(r);
						}
					}
				}
				var A = a("table:first", m.grid.bDiv).attr("id") + "_";
				a("#" + A + u).append(s);
				m.grid.hDiv.loading = false;
				a("#load_" + m.p.id).hide();
				return false;
			};
			m.subGridXml = function (r, q) {
				d(r, q);
			};
			m.subGridJson = function (r, q) {
				f(r, q);
			};
		});
	}, expandSubGridRow:function (b) {
		return this.each(function () {
			var e = this;
			if (!e.grid && !b) {
				return;
			}
			if (e.p.subGrid === true) {
				var c = a(this).jqGrid("getInd", b, true);
				if (c) {
					var d = a("td.sgcollapsed", c)[0];
					if (d) {
						a(d).trigger("click");
					}
				}
			}
		});
	}, collapseSubGridRow:function (b) {
		return this.each(function () {
			var e = this;
			if (!e.grid && !b) {
				return;
			}
			if (e.p.subGrid === true) {
				var c = a(this).jqGrid("getInd", b, true);
				if (c) {
					var d = a("td.sgexpanded", c)[0];
					if (d) {
						a(d).trigger("click");
					}
				}
			}
		});
	}, toggleSubGridRow:function (b) {
		return this.each(function () {
			var e = this;
			if (!e.grid && !b) {
				return;
			}
			if (e.p.subGrid === true) {
				var c = a(this).jqGrid("getInd", b, true);
				if (c) {
					var d = a("td.sgcollapsed", c)[0];
					if (d) {
						a(d).trigger("click");
					} else {
						d = a("td.sgexpanded", c)[0];
						if (d) {
							a(d).trigger("click");
						}
					}
				}
			}
		});
	}});
})(jQuery);
(function (a) {
	a.jgrid.extend({setTreeNode:function (b, c) {
		return this.each(function () {
			var g = this;
			if (!g.grid || !g.p.treeGrid) {
				return;
			}
			var j = g.p.expColInd;
			var i = g.p.treeReader.expanded_field;
			var f = g.p.treeReader.leaf_field;
			var e = g.p.treeReader.level_field;
			c.level = b[e];
			if (g.p.treeGridModel == "nested") {
				c.lft = b[g.p.treeReader.left_field];
				c.rgt = b[g.p.treeReader.right_field];
				if (!b[f]) {
					b[f] = (parseInt(c.rgt, 10) === parseInt(c.lft, 10) + 1) ? "true" : "false";
				}
			} else {
				c.parent_id = b[g.p.treeReader.parent_id_field];
			}
			var k = parseInt(c.level, 10), h, l;
			if (g.p.tree_root_level === 0) {
				h = k + 1;
				l = k;
			} else {
				h = k;
				l = k - 1;
			}
			var d = "<div class='tree-wrap tree-wrap-" + g.p.direction + "' style='width:" + (h * 18) + "px;'>";
			d += "<div style='" + (g.p.direction == "rtl" ? "right:" : "left:") + (l * 18) + "px;' class='ui-icon ";
			if (b[f] == "true" || b[f] == true) {
				d += g.p.treeIcons.leaf + " tree-leaf'";
				c.isLeaf = true;
			} else {
				if (b[i] == "true" || b[i] == true) {
					d += g.p.treeIcons.minus + " tree-minus treeclick'";
					c.expanded = true;
				} else {
					d += g.p.treeIcons.plus + " tree-plus treeclick'";
					c.expanded = false;
				}
				c.isLeaf = false;
			}
			d += "</div></div>";
			if (parseInt(b[e], 10) !== parseInt(g.p.tree_root_level, 10)) {
				if (!a(g).jqGrid("isVisibleNode", c)) {
					a(c).css("display", "none");
				}
			}
			a("td:eq(" + j + ")", c).wrapInner("<span></span>").prepend(d);
			a(".treeclick", c).bind("click", function (o) {
				var n = o.target || o.srcElement;
				var m = a(n, g.rows).parents("tr.jqgrow")[0].rowIndex;
				if (!g.rows[m].isLeaf) {
					if (g.rows[m].expanded) {
						a(g).jqGrid("collapseRow", g.rows[m]);
						a(g).jqGrid("collapseNode", g.rows[m]);
					} else {
						a(g).jqGrid("expandRow", g.rows[m]);
						a(g).jqGrid("expandNode", g.rows[m]);
					}
				}
				return false;
			});
			if (g.p.ExpandColClick === true) {
				a("span", c).css("cursor", "pointer").bind("click", function (o) {
					var n = o.target || o.srcElement;
					var m = a(n, g.rows).parents("tr.jqgrow")[0].rowIndex;
					if (!g.rows[m].isLeaf) {
						if (g.rows[m].expanded) {
							a(g).jqGrid("collapseRow", g.rows[m]);
							a(g).jqGrid("collapseNode", g.rows[m]);
						} else {
							a(g).jqGrid("expandRow", g.rows[m]);
							a(g).jqGrid("expandNode", g.rows[m]);
						}
					}
					a(g).jqGrid("setSelection", g.rows[m].id);
					return false;
				});
			}
		});
	}, setTreeGrid:function () {
		return this.each(function () {
			var e = this, d = 0, b;
			if (!e.p.treeGrid) {
				return;
			}
			if (!e.p.treedatatype) {
				a.extend(e.p, {treedatatype:e.p.datatype});
			}
			e.p.subGrid = false;
			e.p.altRows = false;
			e.p.pgbuttons = false;
			e.p.pginput = false;
			e.p.multiselect = false;
			e.p.rowList = [];
			b = "ui-icon-triangle-1-" + (e.p.direction == "rtl" ? "w" : "e");
			e.p.treeIcons = a.extend({plus:b, minus:"ui-icon-triangle-1-s", leaf:"ui-icon-radio-off"}, e.p.treeIcons || {});
			if (e.p.treeGridModel == "nested") {
				e.p.treeReader = a.extend({level_field:"level", left_field:"lft", right_field:"rgt", leaf_field:"isLeaf", expanded_field:"expanded"}, e.p.treeReader);
			} else {
				if (e.p.treeGridModel == "adjacency") {
					e.p.treeReader = a.extend({level_field:"level", parent_id_field:"parent", leaf_field:"isLeaf", expanded_field:"expanded"}, e.p.treeReader);
				}
			}
			for (var c in e.p.colModel) {
				if (e.p.colModel[c].name == e.p.ExpandColumn) {
					e.p.expColInd = d;
					break;
				}
				d++;
			}
			if (!e.p.expColInd) {
				e.p.expColInd = 0;
			}
			a.each(e.p.treeReader, function (f, g) {
				if (g) {
					e.p.colNames.push(g);
					e.p.colModel.push({name:g, width:1, hidden:true, sortable:false, resizable:false, hidedlg:true, editable:true, search:false});
				}
			});
		});
	}, expandRow:function (b) {
		this.each(function () {
			var d = this;
			if (!d.grid || !d.p.treeGrid) {
				return;
			}
			var c = a(d).jqGrid("getNodeChildren", b);
			a(c).each(function (e) {
				a(this).css("display", "");
				if (this.expanded) {
					a(d).jqGrid("expandRow", this);
				}
			});
		});
	}, collapseRow:function (b) {
		this.each(function () {
			var d = this;
			if (!d.grid || !d.p.treeGrid) {
				return;
			}
			var c = a(d).jqGrid("getNodeChildren", b);
			a(c).each(function (e) {
				a(this).css("display", "none");
				if (this.expanded) {
					a(d).jqGrid("collapseRow", this);
				}
			});
		});
	}, getRootNodes:function () {
		var b = [];
		this.each(function () {
			var d = this;
			if (!d.grid || !d.p.treeGrid) {
				return;
			}
			switch (d.p.treeGridModel) {
			  case "nested":
				var c = d.p.treeReader.level_field;
				a(d.rows).each(function (e) {
					if (parseInt(this[c], 10) === parseInt(d.p.tree_root_level, 10)) {
						b.push(this);
					}
				});
				break;
			  case "adjacency":
				a(d.rows).each(function (e) {
					if (this.parent_id == null || this.parent_id.toLowerCase() == "null") {
						b.push(this);
					}
				});
				break;
			}
		});
		return b;
	}, getNodeDepth:function (c) {
		var b = null;
		this.each(function () {
			var d = this;
			if (!this.grid || !this.p.treeGrid) {
				return;
			}
			switch (d.p.treeGridModel) {
			  case "nested":
				b = parseInt(c.level, 10) - parseInt(this.p.tree_root_level, 10);
				break;
			  case "adjacency":
				b = a(d).jqGrid("getNodeAncestors", c).length;
				break;
			}
		});
		return b;
	}, getNodeParent:function (c) {
		var b = null;
		this.each(function () {
			var g = this;
			if (!g.grid || !g.p.treeGrid) {
				return;
			}
			switch (g.p.treeGridModel) {
			  case "nested":
				var e = parseInt(c.lft, 10), d = parseInt(c.rgt, 10), f = parseInt(c.level, 10);
				a(this.rows).each(function () {
					if (parseInt(this.level, 10) === f - 1 && parseInt(this.lft) < e && parseInt(this.rgt) > d) {
						b = this;
						return false;
					}
				});
				break;
			  case "adjacency":
				a(this.rows).each(function () {
					if (this.id == c.parent_id) {
						b = this;
						return false;
					}
				});
				break;
			}
		});
		return b;
	}, getNodeChildren:function (c) {
		var b = [];
		this.each(function () {
			var g = this;
			if (!g.grid || !g.p.treeGrid) {
				return;
			}
			switch (g.p.treeGridModel) {
			  case "nested":
				var e = parseInt(c.lft, 10), d = parseInt(c.rgt, 10), f = parseInt(c.level, 10);
				a(this.rows).each(function (h) {
					if (parseInt(this.level, 10) === f + 1 && parseInt(this.lft, 10) > e && parseInt(this.rgt, 10) < d) {
						b.push(this);
					}
				});
				break;
			  case "adjacency":
				a(this.rows).each(function (h) {
					if (this.parent_id == c.id) {
						b.push(this);
					}
				});
				break;
			}
		});
		return b;
	}, getFullTreeNode:function (c) {
		var b = [];
		this.each(function () {
			var g = this;
			if (!g.grid || !g.p.treeGrid) {
				return;
			}
			switch (g.p.treeGridModel) {
			  case "nested":
				var e = parseInt(c.lft, 10), d = parseInt(c.rgt, 10), f = parseInt(c.level, 10);
				a(this.rows).each(function (h) {
					if (parseInt(this.level, 10) >= f && parseInt(this.lft, 10) >= e && parseInt(this.lft, 10) <= d) {
						b.push(this);
					}
				});
				break;
			  case "adjacency":
				b.push(c);
				a(this.rows).each(function (h) {
					len = b.length;
					for (h = 0; h < len; h++) {
						if (b[h].id == this.parent_id) {
							b.push(this);
							break;
						}
					}
				});
				break;
			}
		});
		return b;
	}, getNodeAncestors:function (c) {
		var b = [];
		this.each(function () {
			if (!this.grid || !this.p.treeGrid) {
				return;
			}
			var d = a(this).jqGrid("getNodeParent", c);
			while (d) {
				b.push(d);
				d = a(this).jqGrid("getNodeParent", d);
			}
		});
		return b;
	}, isVisibleNode:function (c) {
		var b = true;
		this.each(function () {
			var e = this;
			if (!e.grid || !e.p.treeGrid) {
				return;
			}
			var d = a(e).jqGrid("getNodeAncestors", c);
			a(d).each(function () {
				b = b && this.expanded;
				if (!b) {
					return false;
				}
			});
		});
		return b;
	}, isNodeLoaded:function (c) {
		var b;
		this.each(function () {
			var d = this;
			if (!d.grid || !d.p.treeGrid) {
				return;
			}
			if (c.loaded !== undefined) {
				b = c.loaded;
			} else {
				if (c.isLeaf || a(d).jqGrid("getNodeChildren", c).length > 0) {
					b = true;
				} else {
					b = false;
				}
			}
		});
		return b;
	}, expandNode:function (b) {
		return this.each(function () {
			if (!this.grid || !this.p.treeGrid) {
				return;
			}
			if (!b.expanded) {
				if (a(this).jqGrid("isNodeLoaded", b)) {
					b.expanded = true;
					a("div.treeclick", b).removeClass(this.p.treeIcons.plus + " tree-plus").addClass(this.p.treeIcons.minus + " tree-minus");
				} else {
					b.expanded = true;
					a("div.treeclick", b).removeClass(this.p.treeIcons.plus + " tree-plus").addClass(this.p.treeIcons.minus + " tree-minus");
					this.p.treeANode = b.rowIndex;
					this.p.datatype = this.p.treedatatype;
					if (this.p.treeGridModel == "nested") {
						a(this).jqGrid("setGridParam", {postData:{nodeid:b.id, n_left:b.lft, n_right:b.rgt, n_level:b.level}});
					} else {
						a(this).jqGrid("setGridParam", {postData:{nodeid:b.id, parentid:b.parent_id, n_level:b.level}});
					}
					a(this).trigger("reloadGrid");
					if (this.p.treeGridModel == "nested") {
						a(this).jqGrid("setGridParam", {postData:{nodeid:"", n_left:"", n_right:"", n_level:""}});
					} else {
						a(this).jqGrid("setGridParam", {postData:{nodeid:"", parentid:"", n_level:""}});
					}
				}
			}
		});
	}, collapseNode:function (b) {
		return this.each(function () {
			if (!this.grid || !this.p.treeGrid) {
				return;
			}
			if (b.expanded) {
				b.expanded = false;
				a("div.treeclick", b).removeClass(this.p.treeIcons.minus + " tree-minus").addClass(this.p.treeIcons.plus + " tree-plus");
			}
		});
	}, SortTree:function (b) {
		return this.each(function () {
			if (!this.grid || !this.p.treeGrid) {
				return;
			}
			var f, c, g, e = [], h = this, d = a(this).jqGrid("getRootNodes");
			d.sort(function (j, i) {
				if (j.sortKey < i.sortKey) {
					return -b;
				}
				if (j.sortKey > i.sortKey) {
					return b;
				}
				return 0;
			});
			if (d[0]) {
				a("td", d[0]).each(function (i) {
					a(this).css("width", h.grid.headers[i].width + "px");
				});
				h.grid.cols = d[0].cells;
			}
			for (f = 0, c = d.length; f < c; f++) {
				g = d[f];
				e.push(g);
				a(this).jqGrid("collectChildrenSortTree", e, g, b);
			}
			a.each(e, function (i, j) {
				a("tbody", h.grid.bDiv).append(j);
				j.sortKey = null;
			});
		});
	}, collectChildrenSortTree:function (b, d, c) {
		return this.each(function () {
			if (!this.grid || !this.p.treeGrid) {
				return;
			}
			var g, e, h, f = a(this).jqGrid("getNodeChildren", d);
			f.sort(function (j, i) {
				if (j.sortKey < i.sortKey) {
					return -c;
				}
				if (j.sortKey > i.sortKey) {
					return c;
				}
				return 0;
			});
			for (g = 0, e = f.length; g < e; g++) {
				h = f[g];
				b.push(h);
				a(this).jqGrid("collectChildrenSortTree", b, h, c);
			}
		});
	}, setTreeRow:function (c, d) {
		var b, e = false;
		this.each(function () {
			var f = this;
			if (!f.grid || !f.p.treeGrid) {
				return;
			}
			e = a(f).jqGrid("setRowData", c, d);
		});
		return e;
	}, delTreeNode:function (b) {
		return this.each(function () {
			var f = this;
			if (!f.grid || !f.p.treeGrid) {
				return;
			}
			var d = a(f).jqGrid("getInd", b, true);
			if (d) {
				var e = a(f).jqGrid("getNodeChildren", d);
				if (e.length > 0) {
					for (var c = 0; c < e.length; c++) {
						a(f).jqGrid("delRowData", e[c].id);
					}
				}
				a(f).jqGrid("delRowData", d.id);
			}
		});
	}});
})(jQuery);
(function (a) {
	a.jgrid.extend({jqGridImport:function (b) {
		b = a.extend({imptype:"xml", impstring:"", impurl:"", mtype:"GET", impData:{}, xmlGrid:{config:"roots>grid", data:"roots>rows"}, jsonGrid:{config:"grid", data:"data"}}, b || {});
		return this.each(function () {
			var f = this;
			var d = function (h, m) {
				var g = a(m.xmlGrid.config, h)[0];
				var l = a(m.xmlGrid.data, h)[0];
				if (xmlJsonClass.xml2json && a.jgrid.parse) {
					var n = xmlJsonClass.xml2json(g, " ");
					var n = a.jgrid.parse(n);
					for (var i in n) {
						var j = n[i];
					}
					if (l) {
						var k = n.grid.datatype;
						n.grid.datatype = "xmlstring";
						n.grid.datastr = h;
						a(f).jqGrid(j).jqGrid("setGridParam", {datatype:k});
					} else {
						a(f).jqGrid(j);
					}
					n = null;
					j = null;
				} else {
					alert("xml2json or parse are not present");
				}
			};
			var e = function (h, k) {
				if (h && typeof h == "string") {
					var g = a.jgrid.parse(h);
					var l = g[k.jsonGrid.config];
					var i = g[k.jsonGrid.data];
					if (i) {
						var j = l.datatype;
						l.datatype = "jsonstring";
						l.datastr = i;
						a(f).jqGrid(l).jqGrid("setGridParam", {datatype:j});
					} else {
						a(f).jqGrid(l);
					}
				}
			};
			switch (b.imptype) {
			  case "xml":
				a.ajax({url:b.impurl, type:b.mtype, data:b.impData, dataType:"xml", complete:function (g, h) {
					if (h == "success") {
						d(g.responseXML, b);
						if (a.isFunction(b.importComplete)) {
							b.importComplete(g);
						}
					}
					g = null;
				}});
				break;
			  case "xmlstring":
				if (b.impstring && typeof b.impstring == "string") {
					var c = a.jgrid.stringToDoc(b.impstring);
					if (c) {
						d(c, b);
						if (a.isFunction(b.importComplete)) {
							b.importComplete(c);
						}
						b.impstring = null;
					}
					c = null;
				}
				break;
			  case "json":
				a.ajax({url:b.impurl, type:b.mtype, data:b.impData, dataType:"json", complete:function (g, h) {
					if (h == "success") {
						e(g.responseText, b);
						if (a.isFunction(b.importComplete)) {
							b.importComplete(g);
						}
					}
					g = null;
				}});
				break;
			  case "jsonstring":
				if (b.impstring && typeof b.impstring == "string") {
					e(b.impstring, b);
					if (a.isFunction(b.importComplete)) {
						b.importComplete(b.impstring);
					}
					b.impstring = null;
				}
				break;
			}
		});
	}, jqGridExport:function (c) {
		c = a.extend({exptype:"xmlstring", root:"grid", ident:"\t"}, c || {});
		var b = null;
		this.each(function () {
			if (!this.grid) {
				return;
			}
			var e = a(this).jqGrid("getGridParam");
			if (e.rownumbers) {
				e.colNames.splice(0);
				e.colModel.splice(0);
			}
			if (e.multiselect) {
				e.colNames.splice(0);
				e.colModel.splice(0);
			}
			if (e.subgrid) {
				e.colNames.splice(0);
				e.colModel.splice(0);
			}
			if (e.treeGrid) {
				for (var d in e.treeReader) {
					e.colNames.splice(e.colNames.length - 1);
					e.colModel.splice(e.colModel.length - 1);
				}
			}
			switch (c.exptype) {
			  case "xmlstring":
				b = "<" + c.root + ">" + xmlJsonClass.json2xml(e, c.ident) + "</" + c.root + ">";
				break;
			  case "jsonstring":
				b = "{" + xmlJsonClass.toJson(e, c.root, c.ident) + "}";
				break;
			}
		});
		return b;
	}});
})(jQuery);
var xmlJsonClass = {xml2json:function (b, d) {
	if (b.nodeType === 9) {
		b = b.documentElement;
	}
	var a = this.removeWhite(b);
	var e = this.toObj(a);
	var c = this.toJson(e, b.nodeName, "\t");
	return "{\n" + d + (d ? c.replace(/\t/g, d) : c.replace(/\t|\n/g, "")) + "\n}";
}, json2xml:function (d, c) {
	var e = function (q, f, h) {
		var o = "";
		var l, g;
		if (q instanceof Array) {
			if (q.length === 0) {
				o += h + "<" + f + ">__EMPTY_ARRAY_</" + f + ">\n";
			} else {
				for (l = 0, g = q.length; l < g; l += 1) {
					var p = h + e(q[l], f, h + "\t") + "\n";
					o += p;
				}
			}
		} else {
			if (typeof (q) === "object") {
				var k = false;
				o += h + "<" + f;
				var j;
				for (j in q) {
					if (q.hasOwnProperty(j)) {
						if (j.charAt(0) === "@") {
							o += " " + j.substr(1) + "=\"" + q[j].toString() + "\"";
						} else {
							k = true;
						}
					}
				}
				o += k ? ">" : "/>";
				if (k) {
					for (j in q) {
						if (q.hasOwnProperty(j)) {
							if (j === "#text") {
								o += q[j];
							} else {
								if (j === "#cdata") {
									o += "<![CDATA[" + q[j] + "]]>";
								} else {
									if (j.charAt(0) !== "@") {
										o += e(q[j], j, h + "\t");
									}
								}
							}
						}
					}
					o += (o.charAt(o.length - 1) === "\n" ? h : "") + "</" + f + ">";
				}
			} else {
				if (typeof (q) === "function") {
					o += h + "<" + f + "><![CDATA[" + q + "]]></" + f + ">";
				} else {
					if (q.toString() === "\"\"" || q.toString().length === 0) {
						o += h + "<" + f + ">__EMPTY_STRING_</" + f + ">";
					} else {
						o += h + "<" + f + ">" + q.toString() + "</" + f + ">";
					}
				}
			}
		}
		return o;
	};
	var b = "";
	var a;
	for (a in d) {
		if (d.hasOwnProperty(a)) {
			b += e(d[a], a, "");
		}
	}
	return c ? b.replace(/\t/g, c) : b.replace(/\t|\n/g, "");
}, toObj:function (b) {
	var g = {};
	var f = /function/i;
	if (b.nodeType === 1) {
		if (b.attributes.length) {
			var e;
			for (e = 0; e < b.attributes.length; e += 1) {
				g["@" + b.attributes[e].nodeName] = (b.attributes[e].nodeValue || "").toString();
			}
		}
		if (b.firstChild) {
			var a = 0, d = 0, c = false;
			var h;
			for (h = b.firstChild; h; h = h.nextSibling) {
				if (h.nodeType === 1) {
					c = true;
				} else {
					if (h.nodeType === 3 && h.nodeValue.match(/[^ \f\n\r\t\v]/)) {
						a += 1;
					} else {
						if (h.nodeType === 4) {
							d += 1;
						}
					}
				}
			}
			if (c) {
				if (a < 2 && d < 2) {
					this.removeWhite(b);
					for (h = b.firstChild; h; h = h.nextSibling) {
						if (h.nodeType === 3) {
							g["#text"] = this.escape(h.nodeValue);
						} else {
							if (h.nodeType === 4) {
								if (f.test(h.nodeValue)) {
									g[h.nodeName] = [g[h.nodeName], h.nodeValue];
								} else {
									g["#cdata"] = this.escape(h.nodeValue);
								}
							} else {
								if (g[h.nodeName]) {
									if (g[h.nodeName] instanceof Array) {
										g[h.nodeName][g[h.nodeName].length] = this.toObj(h);
									} else {
										g[h.nodeName] = [g[h.nodeName], this.toObj(h)];
									}
								} else {
									g[h.nodeName] = this.toObj(h);
								}
							}
						}
					}
				} else {
					if (!b.attributes.length) {
						g = this.escape(this.innerXml(b));
					} else {
						g["#text"] = this.escape(this.innerXml(b));
					}
				}
			} else {
				if (a) {
					if (!b.attributes.length) {
						g = this.escape(this.innerXml(b));
						if (g === "__EMPTY_ARRAY_") {
							g = "[]";
						} else {
							if (g === "__EMPTY_STRING_") {
								g = "";
							}
						}
					} else {
						g["#text"] = this.escape(this.innerXml(b));
					}
				} else {
					if (d) {
						if (d > 1) {
							g = this.escape(this.innerXml(b));
						} else {
							for (h = b.firstChild; h; h = h.nextSibling) {
								if (f.test(b.firstChild.nodeValue)) {
									g = b.firstChild.nodeValue;
									break;
								} else {
									g["#cdata"] = this.escape(h.nodeValue);
								}
							}
						}
					}
				}
			}
		}
		if (!b.attributes.length && !b.firstChild) {
			g = null;
		}
	} else {
		if (b.nodeType === 9) {
			g = this.toObj(b.documentElement);
		} else {
			alert("unhandled node type: " + b.nodeType);
		}
	}
	return g;
}, toJson:function (b, a, d) {
	var l = a ? ("\"" + a + "\"") : "";
	if (b === "[]") {
		l += (a ? ":[]" : "[]");
	} else {
		if (b instanceof Array) {
			var c, h, f = [];
			for (h = 0, c = b.length; h < c; h += 1) {
				f[h] = this.toJson(b[h], "", d + "\t");
			}
			l += (a ? ":[" : "[") + (f.length > 1 ? ("\n" + d + "\t" + f.join(",\n" + d + "\t") + "\n" + d) : f.join("")) + "]";
		} else {
			if (b === null) {
				l += (a && ":") + "null";
			} else {
				if (typeof (b) === "object") {
					var j = [];
					var e;
					for (e in b) {
						if (b.hasOwnProperty(e)) {
							j[j.length] = this.toJson(b[e], e, d + "\t");
						}
					}
					l += (a ? ":{" : "{") + (j.length > 1 ? ("\n" + d + "\t" + j.join(",\n" + d + "\t") + "\n" + d) : j.join("")) + "}";
				} else {
					if (typeof (b) === "string") {
						var k = /(^-?\d+\.?\d*$)/;
						var p = /function/i;
						var g = b.toString();
						if (k.test(g) || p.test(g) || g === "false" || g === "true") {
							l += (a && ":") + g;
						} else {
							l += (a && ":") + "\"" + b + "\"";
						}
					} else {
						l += (a && ":") + b.toString();
					}
				}
			}
		}
	}
	return l;
}, innerXml:function (d) {
	var b = "";
	if ("innerHTML" in d) {
		b = d.innerHTML;
	} else {
		var a = function (j) {
			var g = "", f;
			if (j.nodeType === 1) {
				g += "<" + j.nodeName;
				for (f = 0; f < j.attributes.length; f += 1) {
					g += " " + j.attributes[f].nodeName + "=\"" + (j.attributes[f].nodeValue || "").toString() + "\"";
				}
				if (j.firstChild) {
					g += ">";
					for (var h = j.firstChild; h; h = h.nextSibling) {
						g += a(h);
					}
					g += "</" + j.nodeName + ">";
				} else {
					g += "/>";
				}
			} else {
				if (j.nodeType === 3) {
					g += j.nodeValue;
				} else {
					if (j.nodeType === 4) {
						g += "<![CDATA[" + j.nodeValue + "]]>";
					}
				}
			}
			return g;
		};
		for (var e = d.firstChild; e; e = e.nextSibling) {
			b += a(e);
		}
	}
	return b;
}, escape:function (a) {
	return a.replace(/[\\]/g, "\\\\").replace(/[\"]/g, "\\\"").replace(/[\n]/g, "\\n").replace(/[\r]/g, "\\r");
}, removeWhite:function (b) {
	b.normalize();
	var c;
	for (c = b.firstChild; c; ) {
		if (c.nodeType === 3) {
			if (!c.nodeValue.match(/[^ \f\n\r\t\v]/)) {
				var a = c.nextSibling;
				b.removeChild(c);
				c = a;
			} else {
				c = c.nextSibling;
			}
		} else {
			if (c.nodeType === 1) {
				this.removeWhite(c);
				c = c.nextSibling;
			} else {
				c = c.nextSibling;
			}
		}
	}
	return b;
}};
(function (a) {
	a.jgrid.extend({setColumns:function (b) {
		b = a.extend({top:0, left:0, width:200, height:"auto", dataheight:"auto", modal:false, drag:true, beforeShowForm:null, afterShowForm:null, afterSubmitForm:null, closeOnEscape:true, ShrinkToFit:false, jqModal:false, saveicon:[true, "left", "ui-icon-disk"], closeicon:[true, "left", "ui-icon-close"], onClose:null, colnameview:true, closeAfterSubmit:true, updateAfterCheck:false}, a.jgrid.col, b || {});
		return this.each(function () {
			var j = this;
			if (!j.grid) {
				return;
			}
			var k = typeof b.beforeShowForm === "function" ? true : false;
			var d = typeof b.afterShowForm === "function" ? true : false;
			var e = typeof b.afterSubmitForm === "function" ? true : false;
			var c = j.p.id, h = "ColTbl_" + c, f = {themodal:"colmod" + c, modalhead:"colhd" + c, modalcontent:"colcnt" + c, scrollelm:h};
			if (a("#" + f.themodal).html() != null) {
				if (k) {
					b.beforeShowForm(a("#" + h));
				}
				viewModal("#" + f.themodal, {gbox:"#gbox_" + c, jqm:b.jqModal, jqM:false, modal:b.modal});
				if (d) {
					b.afterShowForm(a("#" + h));
				}
			} else {
				var l = isNaN(b.dataheight) ? b.dataheight : b.dataheight + "px";
				var m = "<div id='" + h + "' class='formdata' style='width:100%;overflow:auto;position:relative;height:" + l + ";'>";
				m += "<table class='ColTable' cellspacing='1' cellpading='2' border='0'><tbody>";
				for (i = 0; i < this.p.colNames.length; i++) {
					if (!j.p.colModel[i].hidedlg) {
						m += "<tr><td style='white-space: pre;'><input type='checkbox' style='margin-right:5px;' id='col_" + this.p.colModel[i].name + "' class='cbox' value='T' " + ((this.p.colModel[i].hidden === false) ? "checked" : "") + "/><label for='col_" + this.p.colModel[i].name + "'>" + this.p.colNames[i] + ((b.colnameview) ? " (" + this.p.colModel[i].name + ")" : "") + "</label></td></tr>";
					}
				}
				m += "</tbody></table></div>";
				var g = !b.updateAfterCheck ? "<a href='javascript:void(0)' id='dData' class='fm-button ui-state-default ui-corner-all'>" + b.bSubmit + "</a>" : "", n = "<a href='javascript:void(0)' id='eData' class='fm-button ui-state-default ui-corner-all'>" + b.bCancel + "</a>";
				m += "<table border='0' class='EditTable' id='" + h + "_2'><tbody><tr style='display:block;height:3px;'><td></td></tr><tr><td class='DataTD ui-widget-content'></td></tr><tr><td class='ColButton EditButton'>" + g + "&#160;" + n + "</td></tr></tbody></table>";
				b.gbox = "#gbox_" + c;
				createModal(f, m, b, "#gview_" + j.p.id, a("#gview_" + j.p.id)[0]);
				if (b.saveicon[0] == true) {
					a("#dData", "#" + h + "_2").addClass(b.saveicon[1] == "right" ? "fm-button-icon-right" : "fm-button-icon-left").append("<span class='ui-icon " + b.saveicon[2] + "'></span>");
				}
				if (b.closeicon[0] == true) {
					a("#eData", "#" + h + "_2").addClass(b.closeicon[1] == "right" ? "fm-button-icon-right" : "fm-button-icon-left").append("<span class='ui-icon " + b.closeicon[2] + "'></span>");
				}
				if (!b.updateAfterCheck) {
					a("#dData", "#" + h + "_2").click(function (p) {
						for (i = 0; i < j.p.colModel.length; i++) {
							if (!j.p.colModel[i].hidedlg) {
								var o = j.p.colModel[i].name.replace(".", "\\.");
								if (a("#col_" + o, "#" + h).attr("checked")) {
									a(j).jqGrid("showCol", j.p.colModel[i].name);
									a("#col_" + o, "#" + h).attr("defaultChecked", true);
								} else {
									a(j).jqGrid("hideCol", j.p.colModel[i].name);
									a("#col_" + o, "#" + h).attr("defaultChecked", "");
								}
							}
						}
						if (b.ShrinkToFit === true) {
							a(j).jqGrid("setGridWidth", j.grid.width - 0.001, true);
						}
						if (b.closeAfterSubmit) {
							hideModal("#" + f.themodal, {gb:"#gbox_" + c, jqm:b.jqModal, onClose:b.onClose});
						}
						if (e) {
							b.afterSubmitForm(a("#" + h));
						}
						return false;
					});
				} else {
					a(":input", "#" + h).click(function (o) {
						var p = this.id.substr(4);
						if (p) {
							if (this.checked) {
								a(j).jqGrid("showCol", p);
							} else {
								a(j).jqGrid("hideCol", p);
							}
							if (b.ShrinkToFit === true) {
								a(j).jqGrid("setGridWidth", j.grid.width - 0.001, true);
							}
						}
						return this;
					});
				}
				a("#eData", "#" + h + "_2").click(function (o) {
					hideModal("#" + f.themodal, {gb:"#gbox_" + c, jqm:b.jqModal, onClose:b.onClose});
					return false;
				});
				a("#dData, #eData", "#" + h + "_2").hover(function () {
					a(this).addClass("ui-state-hover");
				}, function () {
					a(this).removeClass("ui-state-hover");
				});
				if (k) {
					b.beforeShowForm(a("#" + h));
				}
				viewModal("#" + f.themodal, {gbox:"#gbox_" + c, jqm:b.jqModal, jqM:true, modal:b.modal});
				if (d) {
					b.afterShowForm(a("#" + h));
				}
			}
		});
	}});
})(jQuery);
(function (a) {
	a.jgrid.extend({getPostData:function () {
		var b = this[0];
		if (!b.grid) {
			return;
		}
		return b.p.postData;
	}, setPostData:function (b) {
		var c = this[0];
		if (!c.grid) {
			return;
		}
		if (typeof (b) === "object") {
			c.p.postData = b;
		} else {
			alert("Error: cannot add a non-object postData value. postData unchanged.");
		}
	}, appendPostData:function (b) {
		var c = this[0];
		if (!c.grid) {
			return;
		}
		if (typeof (b) === "object") {
			a.extend(c.p.postData, b);
		} else {
			alert("Error: cannot append a non-object postData value. postData unchanged.");
		}
	}, setPostDataItem:function (b, c) {
		var d = this[0];
		if (!d.grid) {
			return;
		}
		d.p.postData[b] = c;
	}, getPostDataItem:function (b) {
		var c = this[0];
		if (!c.grid) {
			return;
		}
		return c.p.postData[b];
	}, removePostDataItem:function (b) {
		var c = this[0];
		if (!c.grid) {
			return;
		}
		delete c.p.postData[b];
	},getSelectRows:function(){//新增一个方法，得到选中的所有行的id
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
	getUserData:function () {
		var b = this[0];
		if (!b.grid) {
			return;
		}
		return b.p.userData;
	}, getUserDataItem:function (b) {
		var c = this[0];
		if (!c.grid) {
			return;
		}
		return c.p.userData[b];
	}});
})(jQuery);
function tableToGrid(a, b) {
	$(a).each(function () {
		if (this.grid) {
			return;
		}
		$(this).width("99%");
		var n = $(this).width();
		var p = $("input[type=checkbox]:first", $(this));
		var h = $("input[type=radio]:first", $(this));
		var d = p.length > 0;
		var g = !d && h.length > 0;
		var j = d || g;
		var i = p.attr("name") || h.attr("name");
		var l = [];
		var o = [];
		$("th", $(this)).each(function () {
			if (l.length == 0 && j) {
				l.push({name:"__selection__", index:"__selection__", width:0, hidden:true});
				o.push("__selection__");
			} else {
				l.push({name:$(this).attr("id") || $(this).html(), index:$(this).attr("id") || $(this).html(), width:$(this).width() || 150});
				o.push($(this).html());
			}
		});
		var f = [];
		var e = [];
		var m = [];
		$("tbody > tr", $(this)).each(function () {
			var r = {};
			var q = 0;
			$("td", $(this)).each(function () {
				if (q == 0 && j) {
					var s = $("input", $(this));
					var t = s.attr("value");
					e.push(t || f.length);
					if (s.attr("checked")) {
						m.push(t);
					}
					r[l[q].name] = s.attr("value");
				} else {
					r[l[q].name] = $(this).html();
				}
				q++;
			});
			if (q > 0) {
				f.push(r);
			}
		});
		$(this).empty();
		$(this).addClass("scroll");
		$(this).jqGrid($.extend({datatype:"local", width:n, colNames:o, colModel:l, multiselect:d}, b || {}));
		for (var k = 0; k < f.length; k++) {
			var c = null;
			if (e.length > 0) {
				c = e[k];
				if (c && c.replace) {
					c = encodeURIComponent(c).replace(/[.\-%]/g, "_");
				}
			}
			if (c == null) {
				c = k + 1;
			}
			$(this).jqGrid("addRowData", c, f[k]);
		}
		for (var k = 0; k < m.length; k++) {
			$(this).jqGrid("setSelection", m[k]);
		}
	});
}
(function ($) {
	if ($.browser.msie && $.browser.version == 8) {
		$.expr[":"].hidden = function (elem) {
			return elem.offsetWidth === 0 || elem.offsetHeight === 0 || elem.style.display == "none";
		};
	}
	if ($.ui && $.ui.multiselect && $.ui.multiselect.prototype._setSelected) {
		var setSelected = $.ui.multiselect.prototype._setSelected;
		$.ui.multiselect.prototype._setSelected = function (item, selected) {
			var ret = setSelected.call(this, item, selected);
			if (selected && this.selectedList) {
				var elt = this.element;
				this.selectedList.find("li").each(function () {
					if ($(this).data("optionLink")) {
						$(this).data("optionLink").remove().appendTo(elt);
					}
				});
			}
			return ret;
		};
	}
	$.jgrid.extend({sortableColumns:function (tblrow) {
		return this.each(function () {
			var ts = this;
			function start() {
				ts.p.disableClick = true;
			}
			var sortable_opts = {tolerance:"pointer", axis:"x", items:">th:not(:has(#jqgh_cb,#jqgh_rn,#jqgh_subgrid),:hidden)", placeholder:{element:function (item) {
				var el = $(document.createElement(item[0].nodeName)).addClass(item[0].className + " ui-sortable-placeholder ui-state-highlight").removeClass("ui-sortable-helper")[0];
				return el;
			}, update:function (self, p) {
				p.height(self.currentItem.innerHeight() - parseInt(self.currentItem.css("paddingTop") || 0, 10) - parseInt(self.currentItem.css("paddingBottom") || 0, 10));
				p.width(self.currentItem.innerWidth() - parseInt(self.currentItem.css("paddingLeft") || 0, 10) - parseInt(self.currentItem.css("paddingRight") || 0, 10));
			}}, update:function (event, ui) {
				var p = $(ui.item).parent();
				var th = $(">th", p);
				var colModel = ts.p.colModel;
				var cmMap = {};
				$.each(colModel, function (i) {
					cmMap[this.name] = i;
				});
				var permutation = [];
				th.each(function (i) {
					var id = $(">div", this).get(0).id.replace(/^jqgh_/, "");
					if (id in cmMap) {
						permutation.push(cmMap[id]);
					}
				});
				$(ts).jqGrid("remapColumns", permutation, true, true);
				if ($.isFunction(ts.p.sortable.update)) {
					ts.p.sortable.update(permutation);
				}
				setTimeout(function () {
					ts.p.disableClick = false;
				}, 50);
			}};
			if (ts.p.sortable.options) {
				$.extend(sortable_opts, ts.p.sortable.options);
			} else {
				if ($.isFunction(ts.p.sortable)) {
					ts.p.sortable = {update:ts.p.sortable};
				}
			}
			if (sortable_opts.start) {
				var s = sortable_opts.start;
				sortable_opts.start = function (e, ui) {
					start();
					s.call(this, e, ui);
				};
			} else {
				sortable_opts.start = start;
			}
			if (ts.p.sortable.exclude) {
				sortable_opts.items += ":not(" + ts.p.sortable.exclude + ")";
			}
			tblrow.sortable(sortable_opts).data("sortable").floating = true;
		});
	}, columnChooser:function (opts) {
		var self = this;
		var selector = $("<div style=\"position:relative;overflow:hidden\"><div><select multiple=\"multiple\"></select></div></div>");
		var select = $("select", selector);
		opts = $.extend({width:420, height:240, classname:null, done:function (perm) {
			if (perm) {
				self.jqGrid("remapColumns", perm, true);
			}
		}, msel:"multiselect", dlog:"dialog", dlog_opts:function (opts) {
			var buttons = {};
			buttons[opts.bSubmit] = function () {
				opts.apply_perm();
				opts.cleanup(false);
			};
			buttons[opts.bCancel] = function () {
				opts.cleanup(true);
			};
			return {buttons:buttons, close:function () {
				opts.cleanup(true);
			}, modal:false, resizable:false, width:opts.width + 20};
		}, apply_perm:function () {
			$("option", select).each(function (i) {
				if (this.selected) {
					self.jqGrid("showCol", colModel[this.value].name);
				} else {
					self.jqGrid("hideCol", colModel[this.value].name);
				}
			});
			var perm = fixedCols.slice(0);
			$("option[selected]", select).each(function () {
				perm.push(parseInt(this.value));
			});
			$.each(perm, function () {
				delete colMap[colModel[this].name];
			});
			$.each(colMap, function () {
				perm.push(parseInt(this));
			});
			if (opts.done) {
				opts.done.call(self, perm);
			}
		}, cleanup:function (calldone) {
			call(opts.dlog, selector, "destroy");
			call(opts.msel, select, "destroy");
			selector.remove();
			if (calldone && opts.done) {
				opts.done.call(self);
			}
		}}, $.jgrid.col, opts || {});
		if (opts.caption) {
			selector.attr("title", opts.caption);
		}
		if (opts.classname) {
			selector.addClass(classname);
			select.addClass(classname);
		}
		if (opts.width) {
			$(">div", selector).css({width:opts.width, margin:"0 auto"});
			select.css("width", opts.width);
		}
		if (opts.height) {
			$(">div", selector).css("height", opts.height);
			select.css("height", opts.height - 10);
		}
		var colModel = self.jqGrid("getGridParam", "colModel");
		var colNames = self.jqGrid("getGridParam", "colNames");
		var colMap = {}, fixedCols = [];
		select.empty();
		$.each(colModel, function (i) {
			colMap[this.name] = i;
			if (this.hidedlg) {
				if (!this.hidden) {
					fixedCols.push(i);
				}
				return;
			}
			select.append("<option value='" + i + "' " + (this.hidden ? "" : "selected='selected'") + ">" + colNames[i] + "</option>");
		});
		function call(fn, obj) {
			if (!fn) {
				return;
			}
			if (typeof fn == "string") {
				if ($.fn[fn]) {
					$.fn[fn].apply(obj, $.makeArray(arguments).slice(2));
				}
			} else {
				if ($.isFunction(fn)) {
					fn.apply(obj, $.makeArray(arguments).slice(2));
				}
			}
		}
		var dopts = $.isFunction(opts.dlog_opts) ? opts.dlog_opts.call(self, opts) : opts.dlog_opts;
		call(opts.dlog, selector, dopts);
		var mopts = $.isFunction(opts.msel_opts) ? opts.msel_opts.call(self, opts) : opts.msel_opts;
		call(opts.msel, select, opts.msel_opts);
	}, sortableRows:function (opts) {
		return this.each(function () {
			var $t = this;
			if (!$t.grid) {
				return;
			}
			if ($t.p.treeGrid) {
				return;
			}
			if ($.fn.sortable) {
				opts = $.extend({cursor:"move", axis:"y", items:".jqgrow"}, opts || {});
				if (opts.start && $.isFunction(opts.start)) {
					opts._start_ = opts.start;
					delete opts.start;
				} else {
					opts._start_ = false;
				}
				if (opts.update && $.isFunction(opts.update)) {
					opts._update_ = opts.update;
					delete opts.update;
				} else {
					opts._update_ = false;
				}
				opts.start = function (ev, ui) {
					$(ui.item).css("border-width", "0px");
					$("td", ui.item).each(function (i) {
						this.style.width = $t.grid.cols[i].style.width;
					});
					if ($t.p.subGrid) {
						var subgid = $(ui.item).attr("id");
						try {
							$($t).jqGrid("collapseSubGridRow", subgid);
						}
						catch (e) {
						}
					}
					if (opts._start_) {
						opts._start_.apply(this, [ev, ui]);
					}
				};
				opts.update = function (ev, ui) {
					$(ui.item).css("border-width", "");
					$t.updateColumns();
					if (opts._update_) {
						opts._update_.apply(this, [ev, ui]);
					}
				};
				$("tbody:first", $t).sortable(opts);
			}
		});
	}, gridDnD:function (opts) {
		return this.each(function () {
			var $t = this;
			if (!$t.grid) {
				return;
			}
			if ($t.p.treeGrid) {
				return;
			}
			if (!$.fn.draggable || !$.fn.droppable) {
				return;
			}
			function updateDnD() {
				var datadnd = $.data($t, "dnd");
				$("tr.jqgrow:not(.ui-draggable)", $t).draggable($.isFunction(datadnd.drag) ? datadnd.drag.call($($t), datadnd) : datadnd.drag);
			}
			var appender = "<table id='jqgrid_dnd' class='ui-jqgrid-dnd'></table>";
			if ($("#jqgrid_dnd").html() == null) {
				$("body").append(appender);
			}
			if (typeof opts == "string" && opts == "updateDnD" && $t.p.jqgdnd == true) {
				updateDnD();
				return;
			}
			opts = $.extend({drag:function (opts) {
				return $.extend({start:function (ev, ui) {
					if ($t.p.subGrid) {
						var subgid = $(ui.helper).attr("id");
						try {
							$($t).jqGrid("collapseSubGridRow", subgid);
						}
						catch (e) {
						}
					}
					for (var i = 0; i < opts.connectWith.length; i++) {
						if ($(opts.connectWith[i]).jqGrid("getGridParam", "reccount") == "0") {
							$(opts.connectWith[i]).jqGrid("addRowData", "jqg_empty_row", {});
						}
					}
					ui.helper.addClass("ui-state-highlight");
					$("td", ui.helper).each(function (i) {
						this.style.width = $t.grid.headers[i].width + "px";
					});
					if (opts.onstart && $.isFunction(opts.onstart)) {
						opts.onstart.call($($t), ev, ui);
					}
				}, stop:function (ev, ui) {
					if (ui.helper.dropped) {
						var ids = $(ui.helper).attr("id");
						$($t).jqGrid("delRowData", ids);
					}
					for (var i = 0; i < opts.connectWith.length; i++) {
						$(opts.connectWith[i]).jqGrid("delRowData", "jqg_empty_row");
					}
					if (opts.onstop && $.isFunction(opts.onstop)) {
						opts.onstop.call($($t), ev, ui);
					}
				}}, opts.drag_opts || {});
			}, drop:function (opts) {
				return $.extend({accept:function (d) {
					var tid = $(d).closest("table.ui-jqgrid-btable");
					var cn = $.data(tid[0], "dnd").connectWith;
					return $.inArray("#" + this.id, cn) != -1 ? true : false;
				}, drop:function (ev, ui) {
					var accept = $(ui.draggable).attr("id");
					var getdata = $("#" + $t.id).jqGrid("getRowData", accept);
					if (!opts.dropbyname) {
						var j = 0, tmpdata = {}, dropname;
						var dropmodel = $("#" + this.id).jqGrid("getGridParam", "colModel");
						try {
							for (key in getdata) {
								if (dropmodel[j]) {
									dropname = dropmodel[j].name;
									tmpdata[dropname] = getdata[key];
								}
								j++;
							}
							getdata = tmpdata;
						}
						catch (e) {
						}
					}
					ui.helper.dropped = true;
					if (opts.beforedrop && $.isFunction(opts.beforedrop)) {
						var datatoinsert = opts.beforedrop.call(this, ev, ui, getdata, $("#" + $t.id), $(this));
						if (typeof datatoinsert != "undefined" && datatoinsert !== null && typeof datatoinsert == "object") {
							getdata = datatoinsert;
						}
					}
					if (ui.helper.dropped) {
						var grid;
						if (opts.autoid) {
							if ($.isFunction(opts.autoid)) {
								grid = opts.autoid.call(this, getdata);
							} else {
								grid = Math.ceil(Math.random() * 1000);
								grid = opts.autoidprefix + grid;
							}
						}
						$("#" + this.id).jqGrid("addRowData", grid, getdata, opts.droppos);
					}
					if (opts.ondrop && $.isFunction(opts.ondrop)) {
						opts.ondrop.call(this, ev, ui, getdata);
					}
				}}, opts.drop_opts || {});
			}, onstart:null, onstop:null, beforedrop:null, ondrop:null, drop_opts:{activeClass:"ui-state-active", hoverClass:"ui-state-hover"}, drag_opts:{revert:"invalid", helper:"clone", cursor:"move", appendTo:"#jqgrid_dnd", zIndex:5000}, dropbyname:false, droppos:"first", autoid:true, autoidprefix:"dnd_"}, opts || {});
			if (!opts.connectWith) {
				return;
			}
			opts.connectWith = opts.connectWith.split(",");
			opts.connectWith = $.map(opts.connectWith, function (n) {
				return $.trim(n);
			});
			$.data($t, "dnd", opts);
			if ($t.p.reccount != "0" && !$t.p.jqgdnd) {
				updateDnD();
			}
			$t.p.jqgdnd = true;
			for (var i = 0; i < opts.connectWith.length; i++) {
				var cn = opts.connectWith[i];
				$(cn).droppable($.isFunction(opts.drop) ? opts.drop.call($($t), opts) : opts.drop);
			}
		});
	}, gridResize:function (opts) {
		return this.each(function () {
			var $t = this;
			if (!$t.grid || !$.fn.resizable) {
				return;
			}
			opts = $.extend(opts || {});
			if (opts.alsoResize) {
				opts._alsoResize_ = opts.alsoResize;
				delete opts.alsoResize;
			} else {
				opts._alsoResize_ = false;
			}
			if (opts.stop && $.isFunction(opts.stop)) {
				opts._stop_ = opts.stop;
				delete opts.stop;
			} else {
				opts._stop_ = false;
			}
			opts.stop = function (ev, ui) {
				$($t).jqGrid("setGridParam", {height:$("#gview_" + $t.p.id + " .ui-jqgrid-bdiv").height()});
				$($t).jqGrid("setGridWidth", ui.size.width, opts.shrinkToFit);
				if (opts._stop_) {
					opts._stop_.call($t, ev, ui);
				}
			};
			if (opts._alsoResize_) {
				var optstest = "{'#gview_" + $t.p.id + " .ui-jqgrid-bdiv':true,'" + opts._alsoResize_ + "':true}";
				opts.alsoResize = eval("(" + optstest + ")");
			} else {
				opts.alsoResize = $(".ui-jqgrid-bdiv", "#gview_" + $t.p.id);
			}
			delete opts._alsoResize_;
			$("#gbox_" + $t.p.id).resizable(opts);
		});
	}});
})(jQuery);

