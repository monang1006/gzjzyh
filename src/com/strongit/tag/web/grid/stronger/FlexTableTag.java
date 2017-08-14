package com.strongit.tag.web.grid.stronger;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.TagSupport;

import org.apache.commons.beanutils.NestedNullException;
import org.apache.commons.beanutils.PropertyUtils;

import com.strongmvc.orm.hibernate.Page;

public class FlexTableTag extends TagSupport {
	private String name = null; // 表格名称
	private String headCss = null; // 表头CSS样式
	private String wholeCss = null; // 整体CSS样式
	private String detailCss = null; // 内容CSS样式
	private String sumCss = null; // 表尾CSS样式
	private String leftCss = null; // 左边框CSS样式
	private String height = null; // 表格高度
	private String width = null; // 表格宽度
	private String property = null; // 主键属性
	private boolean isCanDrag = true; // 是否能够拖拉
	private boolean isCanFixUpCol = true; // 是否能够固定列
	private String clickColor = null; // 选中行颜色设置
	private String pageSize = null; // 每页显示列表行数,接收数据
	public int pageRows = 0; // 每页显示列表行数
	private String footShow = null; // 表尾展现内容
	private List collection = null; // 内容列表
	private String getValueType = null; // 获取内容方式
	private boolean isCanSplit = true; // 是否可分页
	private boolean isShowMenu = true; // 是否可分页
	private int nameColNum = 1;// 右键菜单要获取值的列
	private boolean showSearch = true;// 是否显示搜索栏
	private boolean showZebraLine = true;// 是否显示搜索栏
	private String onclick = null;
	/** 行点击事件 */
	private String ondblclick = null;
	/** 行双击事件 */
	private Page page = null;
	/** 分页辅助对象 */
	private String pagename = "page";
	/** 分页辅助对象参数名称 */

	private List HeadList = null;
	/** 存储表头HTML字符串 */
	private List FootList = null;
	/** 存储表尾HTML字符串 */
	private List CellList = null;
	/** 存储内容辅助信息类 */
	private List dataList = null;
	/** 存储展现数据 */
	private List searchList = null;
	/** 存储搜索栏数据 */

	private List<TableColTag> tableColTagList = null;

	private List<TableColTag> percentTableColTagList = null;// 使用百分比的列
	private List<TableColTag> otherTableColTagList = null;// 使用百分比的列

	public static int MAX_ROWS = 10;
	public static int MIDDLE_ROWS = 10;

	public FlexTableTag() {
		name = "myTable";
		headCss = "biao_bg3";
		wholeCss = "table1";
		detailCss = "td1";
		sumCss = "td1";
		leftCss = "";
		height = "250";
		width = "100%";
		property = "";
		// isCanDrag = true;
		isCanDrag = false;// yanjian 2011-12-13 默认不可拖拉
		isCanFixUpCol = true;
		isShowMenu = true;
		clickColor = "#A9B2CA";
		setPageSize("10");
		getValueType = "getValueByProperty";// getValueByProperty：通过属性获取值，getValueByArray:通过数组获取值
		footShow = "showCheck";// showCheck：展现选中框，showSum:展现合计，null:不展现表尾
		nameColNum = 1;
		pagename = "page";

		dataList = new ArrayList();
		HeadList = new ArrayList();
		FootList = new ArrayList();
		CellList = new ArrayList();
		tableColTagList = new ArrayList<TableColTag>(8);
		percentTableColTagList = new ArrayList<TableColTag>(8);
		otherTableColTagList = new ArrayList<TableColTag>(8);
	}

	public int doStartTag() throws JspException {
		tableColTagList.clear();
		percentTableColTagList.clear();
		otherTableColTagList.clear();
		JspWriter out = pageContext.getOut();
		HeadList = new ArrayList();
		CellList = new ArrayList();
		FootList = new ArrayList();
		try {
			/**
			 * 创建表格
			 */
			// out.println("<div id=\""+name+"_div\" style=\"overflow:
			// auto;width: 100%;height: 300px;\" onmousedown=\"downBody(this)\"
			// onmouseover=\"moveBody(this)\" onmouseup=\"upBody(this)\"
			// onselectstart=\"selectBody(this)\">");
			// out.println("<div id=\"myDiv\" style=\"display:none;
			// height:201px; left:12px; position:absolute; top:50px; width:28px;
			// z-index:1\">");
			// out.println(" <hr id=\"myLine\" width=\"1\" size=\"200\" noshade
			// color=\"#F4F4F4\">");
			// out.println("</div>");
			// out.println("<table width=\"100%\" style=\"vertical-align: top;\"
			// border=\"0\" cellpadding=\"0\" cellspacing=\"0\"
			// height=\"80%\">");
			out
					.println("<table id=\""
							+ name
							+ "_div\" width=\"100%\"   cellpadding=\"0\" cellspacing=\"0\" onmousedown=\"downBody(this)\" onmouseover=\"moveBody(this)\" onmouseup=\"upBody(this)\" onselectstart=\"selectBody(this)\">");
			out
					.println("<div id=\"myDiv\" style=\"display:none; height:201px; left:12px; position:absolute; top:50px; width:28px; z-index:1\">");
			out
					.println(" <hr id=\"myLine\" width=\"1\" size=\"200\" noshade color=\"#F4F4F4\">");
			out.println("</div>");
			if (showSearch)
				out.print("<tr><td valign=top>");
			pageContext.setAttribute("com.strongit.tag.web.grid.FLEXTABLETAG",
					((Object) (this)));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return EVAL_BODY_INCLUDE;
	}

	public int doEndTag() throws JspTagException {
		String trHeight = "height:30px;";
		int pageResultSize = page == null ? 0 : page.getResult() == null ? 0
				: page.getResult().size();
		int showSzie = pageResultSize == 0 ? 0 : (pageResultSize > page
				.getPageSize()) ? page.getPageSize() : pageResultSize;// 列表当前显示记录数
		if (pageSize != null && !"".equals(pageSize)) {
			pageRows = Integer.valueOf(pageSize);
		}

		if (tableColTagList != null && tableColTagList.size() != 0) {
			algorithm();
			for (TableColTag tableColTag : tableColTagList) {
				tableColTag.doStartHtmml();
			}
		}
		JspWriter out = pageContext.getOut();
		try {
			if (showSearch)
				out.println("</td></tr>");
			out.println("<tr><td valign=top align=\"left\">");
			// out.println("<div id=\""+name+"_div\" style=\"overflow:
			// auto;width: "+width+";height: "+height+";vertical-align: top;\"
			// onmousedown=\"downBody(this)\" onmouseover=\"moveBody(this)\"
			// onmouseup=\"upBody(this)\" onselectstart=\"selectBody(this)\">");
			// out.println("<div id=\"myDiv\" style=\"display:none;
			// height:201px; left:12px; position:absolute; top:50px; width:28px;
			// z-index:1\">");
			// out.println(" <hr id=\"myLine\" width=\"1\" size=\"200\" noshade
			// color=\"#F4F4F4\">");
			// out.println("</div>");
			out.println("<style type=\"text/css\">");
			out
					.println("#"
							+ name
							+ " td,th {overflow: hidden;text-overflow: ellipsis;white-space: nowrap; line-height:19px;}");
			out.println("</style>");
			if (showSzie > pageRows) {
				out.println("<div style=\"height:338;overflow-y: auto;\">");
			}
			out
					.println("<table id=\""
							+ name
							+ "\" style=\"vertical-align: top;width: 100%;table-layout: fixed;\" align=\"left\" class=\""
							+ wholeCss
							+ "\"  cellpadding=0 cellspacing=1 width=\""
							+ width + "\" height=\"100%\"  >");
			/**
			 * 建立表头
			 */
			int collines = HeadList.size();
			out.println("<thead>");
			StringBuilder fixedHeadStyle = new StringBuilder();
			if (showSzie > pageRows) {
				fixedHeadStyle
						.append("position: relative;top: expression(this.parentElement.parentElement.parentElement.scrollTop-1);z-index:2;");
			}
			out.println("<tr style=\"" + trHeight + fixedHeadStyle + "\">");// style=\"position:relative;top:expression(this.parentElement.offsetParent.parentElement.scrollTop);z-index:1;\">");
			out.println("<td style=\"display:none\" >" + property + "</td>");

			for (int i = 0; i < collines; i++) {
				out.println(HeadList.get(i));
			}
			// out.println("<th class=\""+headCss+"\" style=\"text-indent:
			// 0px;\"></th>");
			out.println("</tr>");
			out.println("</thead>");
			/**
			 * 建立表内容
			 */

			/**
			 * 通过BO属性值查找显示列表值
			 */
			out.println("<tbody ");
			if (isShowMenu) {
				out.print(" oncontextmenu=\"chickRightMouse(event)\"");
			}
			out.print(" onmousedown=\"TableMouseDown(\'" + clickColor
					+ "\')\">");
			int size = 0;
			// out.println("<div style=\"overflow-x:
			// auto;overflow-y:scroll;width: 100%;height:250px;\" >");
			boolean hasCheck = false;
			if ("getValueByProperty".equals(getValueType)) {
				// try {
				dataList = collection;// (List)RequestUtils.lookup(pageContext,
										// collection, ((String) (null)));
				size = dataList == null ? 0 : dataList.size();
				// } catch (JspException e1) {
				// TODO Auto-generated catch block
				// e1.printStackTrace();
				// }
				for (int i = 0; i < size; i++) {
					if (isShowZebraLine()) {
						if (i % 2 == 0) {
							this.setDetailCss("td1");
						} else {
							this.setDetailCss("td2");
						}
					} else {
						this.setDetailCss("td1");
					}
					Object obj = dataList.get(i);
					out.println("<tr style=\"" + trHeight
							+ "line-height:2.0\" ");
					if (getOnclick() != null && !"".equals(getOnclick())
							&& !"null".equals(getOnclick())) {
						out.print(" onclick=\"clickChecked(this);"
								+ getOnclick() + "\"");
					} else {
						out.print(" onclick=\"clickChecked(this);\"");
					}
					if (getOndblclick() != null && !"".equals(getOndblclick())
							&& !"null".equals(getOndblclick()))
						out.print(" ondblclick=\"" + getOndblclick() + "\"");
					if (property.startsWith("javascript:")) {
						StringBuffer function = getFunctionValue(obj, property,
								null);
						out.print(" value=\"" + function.toString() + "\">");
						out.println("<td style=\"display:none\" >"
								+ function.toString() + "</td>");
					} else {
						try {
							out.print(" value=\""
									+ (Object) PropertyUtils.getProperty(obj,
											property) + "\">");
							out.println("<td style=\"display:none\" >"
									+ (Object) PropertyUtils.getProperty(obj,
											property) + "</td>");
						} catch (Exception e) {
							// TODO Auto-generated catch block
							out.print(" value=\"" + property + "\">");
							out.println("<td style=\"display:none\" >"
									+ property + "</td>");
							// e.printStackTrace();
						}
					}

					for (int j = 0; j < CellList.size(); j++) {
						// String tagname = (String)CellList.get(j);
						TableColHelp coltag = (TableColHelp) CellList.get(j);
						Object valueid;
						if (coltag.getProperty().startsWith("javascript:")) {
							valueid = getFunctionValue(obj,
									coltag.getProperty(), coltag.getMap())
									.toString();
						} else {
							try {
								valueid = (Object) PropertyUtils.getProperty(
										obj, coltag.getProperty());
							} catch (Exception e) {
								// TODO Auto-generated catch block
								valueid = coltag.getProperty();
								// e.printStackTrace();
							}
						}
						Object showvalue;
						if (coltag.getShowValue().startsWith("javascript:")) {
							showvalue = getFunctionValue(obj,
									coltag.getShowValue(), coltag.getMap())
									.toString();
						} else {
							try {
								showvalue = (Object) PropertyUtils.getProperty(
										obj, coltag.getShowValue());
							} catch (NestedNullException nne) {
								// 没有找到 对应字段 的值
								showvalue = "";
							} catch (Exception e) {
								// TODO Auto-generated catch block
								showvalue = coltag.getShowValue();
								// e.printStackTrace();
							}
						}
						if (showvalue == null) {
							showvalue = "";
						} else if (showvalue.toString().indexOf("\n") > -1) {
							showvalue = showvalue.toString().replaceAll("\\n",
									" ");
						}
						out.println(coltag.getColtag().innerHTML(valueid,
								showvalue, coltag.getOnclick(),
								coltag.getOndblclick(), coltag.getMap()));
						if (coltag.getColtag().getClass().getName().equals(
								CheckBoxTag.class.getName()))
							hasCheck = true;
					}
					// out.println("<td class=\""+detailCss+"\"
					// style=\"text-indent: 0px;\"></td>");
					out.println("</tr>");
				}
			}
			/**
			 * 通过数组位置对应查找显示列表值
			 */
			else if ("getValueByArray".equals(getValueType)) {
				dataList = collection;
				size = dataList == null ? 0 : dataList.size();

				for (int i = 0; i < size; i++) {
					if (isShowZebraLine()) {
						if (i % 2 == 0) {
							this.setDetailCss("td1");
						} else {
							this.setDetailCss("td2");
						}
					} else {
						this.setDetailCss("td1");
					}
					Object[] obj = (Object[]) dataList.get(i);
					out
							.println("<tr style=\"" + trHeight
									+ "line-height:2.0\"");
					if (getOnclick() != null && !"".equals(getOnclick())
							&& !"null".equals(getOnclick())) {
						out.print(" onclick=\"clickChecked(this);"
								+ getOnclick() + "\"");
					} else {
						out.print(" onclick=\"clickChecked(this);\"");
					}
					if (getOndblclick() != null && !"".equals(getOndblclick())
							&& !"null".equals(getOndblclick()))
						out.print(" ondblclick=\"" + getOndblclick() + "\"");
					int pos = 0;
					if (property.startsWith("javascript:")) {
						StringBuffer function = getFunctionArrayValue(obj,
								property, null);
						out.print(" value=\"" + function.toString() + "\">");
						out.println("<td style=\"display:none\" >"
								+ function.toString() + "</td>");
					} else {
						try {
							if (property != null)
								pos = Integer.parseInt(property);
							out.print(" value=\"" + obj[pos] + "\">");
							out.println("<td style=\"display:none\" >"
									+ obj[pos] + "</td>");
						} catch (Exception e) {
							// TODO Auto-generated catch block
							out.print(" value=\"" + property + "\">");
							out.println("<td style=\"display:none\" >"
									+ property + "</td>");
							// e.printStackTrace();
						}
					}
					for (int j = 0; j < CellList.size(); j++) {
						TableColHelp coltag = (TableColHelp) CellList.get(j);
						Object valueid = null;
						if (coltag.getValuepos().startsWith("javascript:")) {
							valueid = getFunctionArrayValue(obj, coltag
									.getValuepos(), coltag.getMap());
						} else {
							try {
								valueid = obj[Integer.parseInt(coltag
										.getValuepos())];
							} catch (Exception e) {
								valueid = coltag.getValuepos();
								// e.printStackTrace();
							}
						}
						Object showvalue = null;
						if (coltag.getValueshowpos().startsWith("javascript:")) {
							showvalue = getFunctionArrayValue(obj, coltag
									.getValueshowpos(), coltag.getMap());
						} else {
							try {
								showvalue = obj[Integer.parseInt(coltag
										.getValueshowpos())];
							} catch (Exception e) {
								showvalue = coltag.getValueshowpos();
								// e.printStackTrace();
							}
						}
						out.println(coltag.getColtag().innerHTML(valueid,
								showvalue, coltag.getOnclick(),
								coltag.getOndblclick(), coltag.getMap()));
						if (coltag.getColtag().getClass().getName().equals(
								CheckBoxTag.class.getName()))
							hasCheck = true;
					}
					// out.println("<td class=\""+detailCss+"\"
					// style=\"text-indent: 0px;\"></td>");
					out.println("</tr>");
				}
			}
			/***/
			if (showSzie < pageRows) {
				int blankRows = pageRows - showSzie;
				int tdSize = HeadList.size();
				for (int bi = 0; bi < blankRows; bi++) {
					String tdclass = ((showSzie + bi) % 2 == 0) ? "td1" : "td2";
					out.println("<tr>");
					out.println("<td style=\"" + trHeight
							+ "display:none\" ></td>");
					for (int tdi = 0; tdi < tdSize; tdi++) {
						out.println("<td  style=\"" + trHeight
								+ "line-height:2.0\"  class=\"" + tdclass
								+ "\"  />");
					}
					out.println("</tr>");
				}
			}
			// out.println("</div>");
			out.println("</tbody>");

			/**
			 * 建立表尾
			 */
			out.println("<tfoot>");
			if ("showfoot".equals(footShow)) {
				out.println("<tr>");
				out.println("		<td align=\"left\" class=\"" + sumCss
						+ "\" colspan=\"" + (collines + 1) + "\" id=\"" + name
						+ "_td\">已选择：</td>");
				out.println("</tr>");
				out.println("<script> setTableStatus(" + name
						+ "_td); setFootNum(1); setClickColor('"
						+ clickColor.toLowerCase() + "');</script>");
				// }else if("showSum".equals(footShow)){
			} else {
				out.println("<tr style=\"display: none\" >");
				out.println("		<td align=\"left\" class=\"" + sumCss
						+ "\" colspan=\"" + (collines + 1) + "\" id=\"" + name
						+ "_td\">已选择：</td>");
				out.println("</tr>");
				out.println("<script> setTableStatus(" + name
						+ "_td); setFootNum(1); setClickColor('"
						+ clickColor.toLowerCase() + "');</script>");
			}
			out.println("</tfoot>");
			out.println("</table>");
			if (showSzie > pageRows) {
				out.println("</div>");
			}
			// out.println("</div>");
			out.println("<script>");
			out.println("setTableBorder(" + name + "); ");
			out.println("setHasCheck('" + hasCheck + "'); ");
			out.println("setOrColor('#ffffff'); ");
			/*
			 * if(height.indexOf('%')!=-1){ out.println("var
			 * "+name+"_div=document.getElementById(\""+name+"_div\"); ");
			 * out.println("if(window.parent!=undefined)"); out.println(
			 * name+"_div.height=window.parent.document.body.scrollHeight/100*"+height.substring(0,height.indexOf('%'))+";
			 * "); //out.println("alert("+name+"_div.height); "); }
			 */
			out.println("</script>");
			out.println("</td>");
			out.println("</tr>");
			/**
			 * 添加分页
			 */
			if (isCanSplit && page != null) {

				if (page.getPageNo() <= 0) {
					page.setPageNo(1);
				}
				int totalPages = page.getTotalPages();
				if (totalPages <= 0) {
					totalPages = 1;
				}

				out.println("<tr>");
				out.println("<td>");
				out
						.println("<table width=\"100%\" border=\"0\" cellspacing=\"0\" cellpadding=\"0\">");
				out.println("        <tr>");
				out
						.println("          <td class=\"biao_bg3\"><table width=\"100%\" border=\"0\" cellspacing=\"0\" cellpadding=\"0\">");
				out.println("            <tr>");
				out
						.print("              <td width=\"1%\">&nbsp;<input id=\"orderby\" name=\""
								+ pagename
								+ ".orderBy\" type=\"hidden\" size=\"2\"");
				if (page.getOrderBy() != null
						&& !page.getOrderBy().equals("null")
						&& !page.getOrderBy().equals(""))
					out.print(" value=\"" + page.getOrderBy() + "\"");
				out.print("><input id=\"order\" name=\"" + pagename
						+ ".order\" type=\"hidden\" size=\"2\"");
				if (page.getOrder() != null && !page.getOrder().equals("null")
						&& !page.getOrder().equals(""))
					out.print(" value=\"" + page.getOrder() + "\"");
				out.println("></td>");
				out.println("<td width=\"13%\">当前&nbsp;" + page.getPageNo()
						+ "/" + totalPages + "&nbsp;页</td>");
				// if(page.isHasPre() && totalPages!=1){
				// out.println(" <td width=\"7%\"><input name=\"Submit2\"
				// type=\"button\" class=\"input_bg\" value=\"首页\"
				// onclick=\"gotoPage(1)\"></td>");
				// out.println(" <td width=\"7%\"><input name=\"Submit22\"
				// type=\"button\" class=\"input_bg\" value=\"上一页\"
				// onclick=\"gotoPage("+page.getPrePage()+")\"></td>");
				// }
				// if(page.isHasNext() && totalPages!=1){
				// out.println(" <td width=\"7%\"><input name=\"Submit23\"
				// type=\"button\" class=\"input_bg\" value=\"下一页\"
				// onclick=\"gotoPage("+page.getNextPage()+")\"></td>");
				// out.println(" <td width=\"5%\"><input name=\"Submit24\"
				// type=\"button\" class=\"input_bg\" value=\"尾页\"
				// onclick=\"gotoPage("+totalPages+")\"></td>");
				// }
				HttpServletRequest httpServletRequest = (HttpServletRequest) pageContext
						.getRequest();
				String rooturl = httpServletRequest.getContextPath();

				if (page.isHasPre() && totalPages != 1) {
					out
							.println("              <td width=\"50px\"><img class=\"input_bg_page\" src=\""
									+ rooturl
									+ "/common/images/first_1.png\" style=\"cursor: pointer;\" onclick=\"gotoPage(1)\" title=\"首页\"/></td>");
					out
							.println("              <td width=\"50px\"><img class=\"input_bg_page\" src=\""
									+ rooturl
									+ "/common/images/prev_1.png\" style=\"cursor: pointer;\" onclick=\"gotoPage("
									+ page.getPrePage()
									+ ")\" title=\"上一页\"/></td>");
				}
				if (page.isHasNext() && totalPages != 1) {
					out
							.println("              <td width=\"50px\"><img class=\"input_bg_page\" src=\""
									+ rooturl
									+ "/common/images/next_1.png\" style=\"cursor: pointer;\" onclick=\"gotoPage("
									+ page.getNextPage()
									+ ")\" title=\"下一页\"/></td>");
					out
							.println("              <td width=\"50px\"><img class=\"input_bg_page\" src=\""
									+ rooturl
									+ "/common/images/end_1.png\" style=\"cursor: pointer;\" onclick=\"gotoPage("
									+ totalPages + ")\" title=\"尾页\"/></td>");
				}

				out.println("<td width=\"60\">&nbsp;转到</td>");
				out
						.println("<td width=\"60\" nowrap=\"nowrap\" align=\"center\">"
								+ "<input id=\"pageNo\" name=\""
								+ pagename
								+ ".pageNo\"type=\"text\" size=\"4\" value=\""
								+ page.getPageNo()
								+ "\" onkeypress=\"if(event.keyCode==13) gotoPage();\" style=\"text-align:right;padding-right: 3px;\">"
								+ "<input id=\"pageNoBak\" name=\""
								+ pagename
								+ ".pageNoBak\"type=\"hidden\" size=\"4\" value=\""
								+ page.getPageNo() + "\">" + "</td>");

				out.println("<td width=\"40\">&nbsp;页&nbsp;</td>");
				out
						.println("<td width=\"80\"><input name=\"Submit242\" type=\"button\" class=\"button\" value=\"跳 转\" onclick=\"gotoPage()\">");

				out.println("<td width=\"16%\">&nbsp;&nbsp;共"
						+ page.getTotalCount() + "条数据</td>");

				out.println("<td width=\"*\">&nbsp;</td>");
				out.println("<td width=\"50\">&nbsp;每页&nbsp;</td>");
				out
						.println("<td width=\"60\" align=\"center\">"
								+ "<input id=\"pagesize\" name=\""
								+ pagename
								+ ".pageSize\" type=\"text\" size=\"2\" value=\""
								+ page.getPageSize()
								+ "\" onkeypress=\"if(event.keyCode==13) gotoPage('1');\" style=\"text-align:right;padding-right: 3px;\">"
								+ "<input id=\"pageSizeBak\" name=\""
								+ pagename
								+ ".pageSizeBak\" type=\"hidden\" size=\"2\" value=\""
								+ page.getPageSize() + "\">" + "</td>");
				out
						.println("<td width=\"80\">&nbsp;<input name=\"Submit\" type=\"button\" class=\"button\" value=\"设 置\" onclick=\"gotoPage('1')\"></td>");
				out.println("<script>");
				out.println(" function gotoPage(no){");
				out.println("	var psize = " + totalPages);
				out.println("	var lastpagesize = " + page.getPageSize());
				out
						.println("	var pagesize = document.getElementById('pagesize').value;");
				out
						.println("	if(isNaN(pagesize)||pagesize<=0||(!isNumber(pagesize))||(pagesize.indexOf('+')>-1)){ alert('设置的每页数必须为正整数，请重新设置！');document.getElementById('pagesize').value =lastpagesize;  return;}");
				out
						.println("	if(pagesize.length>10){ alert('设置的每页数超过限制，请重新设置！'); return;}");
				out
						.println("	if(pagesize>500){ alert('每页显示不能超过500，请重新设置！');document.getElementById('pagesize').value =lastpagesize; return;}");
				out.println("	if(no!=null&&no!=\"\"){");
				out.println("		document.getElementById('pageNo').value=no;");
				out.println("	}");
				out.println("	no = document.getElementById('pageNo').value; ");
				out.println("	var lastno = " + page.getPageNo());
				out
						.println("	if(isNaN(no)||no<=0||(!isNumber(no))||(no.indexOf('+')>-1)){ alert('设置的页数必须为正整数，请重新设置！'); document.getElementById('pageNo').value = lastno; return;}");
				out
						.println("	if(no>psize){ alert('设置的页数超出总页数范围，请重新设置！'); document.getElementById('pageNo').value = lastno; return;}");
				// out.println(" else");
				// out.println("
				// document.getElementById('pageNo').value=\"1\";");
				out.println("	doFromSubmit('" + name + "Form');");// document.getElementById('"+name+"Form').submit();");
				out.println(" }");
				out.println("</script>");
				out.println("</td>");
				// out.println(" <td width=\"1%\">&nbsp;</td>");
				out.println("            </tr>");
				out.println("          </table></td>");
				out.println("        </tr>");
				out.println("      </table>");
				out.println("</td>");
				out.println("</tr>");
			}
			out.println("</table>");
			// out.println("</div>");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return EVAL_PAGE;
	}

	private StringBuffer getFunctionValue(Object obj, String property,
			HashMap map) {
		StringBuffer function = new StringBuffer("<script> document.write(")
				.append(property.substring(property.indexOf(":") + 1, property
						.indexOf("(") + 1));

		String args = property.substring(property.indexOf("(") + 1, property
				.indexOf(")"));
		if (args.length() > 0) {
			String argment[] = args.split(",");
			for (int k = 0; k < argment.length; k++) {
				Object pro = null;
				try {
					pro = (Object) PropertyUtils.getProperty(obj, argment[k]);
					if (map != null && map.get(pro) != null)
						pro = map.get(pro);
				} catch (Exception e) {
					pro = argment[k];
					if (map != null && map.get(pro) != null)
						pro = map.get(pro);
				}
				if (k == 0)
					function.append("'").append(pro).append("'");
				else
					function.append(",'").append(pro).append("'");
			}
		}
		function.append(")) </script>");
		return function;
	}

	private StringBuffer getFunctionArrayValue(Object[] obj, String property,
			HashMap map) {
		StringBuffer function = new StringBuffer("<script> document.write(")
				.append(property.substring(property.indexOf(":") + 1, property
						.indexOf("(") + 1));

		String args = property.substring(property.indexOf("(") + 1, property
				.indexOf(")"));
		if (args.length() > 0) {
			String argment[] = args.split(",");
			for (int k = 0; k < argment.length; k++) {
				Object pro = null;
				try {
					pro = (Object) obj[Integer.parseInt(argment[k])];
					if (map != null && map.get(pro) != null)
						pro = map.get(pro);
				} catch (Exception e) {
					pro = argment[k];
					if (map != null && map.get(pro) != null)
						pro = map.get(pro);
				}
				if (k == 0)
					function.append("'").append(pro).append("'");
				else
					function.append(",'").append(pro).append("'");
			}
		}
		function.append(")) </script>");
		return function;
	}

	public String getClickColor() {
		return clickColor;
	}

	public void setClickColor(String clickColor) {
		this.clickColor = clickColor.trim();
	}

	public String getDetailCss() {
		return detailCss;
	}

	public void setDetailCss(String detailCss) {
		this.detailCss = detailCss;
	}

	public String getHeadCss() {
		return headCss;
	}

	public void setHeadCss(String headCss) {
		this.headCss = headCss;
	}

	public String getHeight() {
		return height;
	}

	public void setHeight(String height) {
		this.height = height;
	}

	public boolean getIsCanDrag() {
		return false;
	}

	public void setIsCanDrag(boolean isCanDrag) {
		this.isCanDrag = isCanDrag;
	}

	public boolean getIsCanFixUpCol() {
		return isCanFixUpCol;
	}

	public void setIsCanFixUpCol(boolean isCanFixUpCol) {
		this.isCanFixUpCol = isCanFixUpCol;
	}

	public String getLeftCss() {
		return leftCss;
	}

	public void setLeftCss(String leftCss) {
		this.leftCss = leftCss;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getProperty() {
		return property;
	}

	public void setProperty(String property) {
		this.property = property;
	}

	public String getSumCss() {
		return sumCss;
	}

	public void setSumCss(String sumCss) {
		this.sumCss = sumCss;
	}

	public String getWholeCss() {
		return wholeCss;
	}

	public void setWholeCss(String wholeCss) {
		this.wholeCss = wholeCss;
	}

	public String getWidth() {
		return width;
	}

	public void setWidth(String width) {
		this.width = width;
	}

	public List getCellList() {
		return CellList;
	}

	public void setCellList(List cellList) {
		CellList = cellList;
	}

	public List getHeadList() {
		return HeadList;
	}

	public void setHeadList(List headList) {
		HeadList = headList;
	}

	public List getFootList() {
		return FootList;
	}

	public void setFootList(List footList) {
		FootList = footList;
	}

	public String getFootShow() {
		return footShow;
	}

	public void setFootShow(String footShow) {
		this.footShow = footShow;
	}

	public List getCollection() {
		return collection;
	}

	public void setCollection(List collection) {
		this.collection = collection;
	}

	public String getGetValueType() {
		return getValueType;
	}

	public void setGetValueType(String getValueType) {
		this.getValueType = getValueType;
	}

	public boolean getIsCanSplit() {
		return isCanSplit;
	}

	public void setIsCanSplit(boolean isCanSplit) {
		this.isCanSplit = isCanSplit;
	}

	public int getNameColNum() {
		return nameColNum;
	}

	public void setNameColNum(int nameColNum) {
		this.nameColNum = nameColNum;
	}

	public List getSearchList() {
		return searchList;
	}

	public void setSearchList(List searchList) {
		this.searchList = searchList;
	}

	public boolean isShowSearch() {
		return showSearch;
	}

	public void setShowSearch(boolean showSearch) {
		this.showSearch = showSearch;
	}

	public String getOnclick() {
		return onclick;
	}

	public void setOnclick(String onclick) {
		this.onclick = onclick;
	}

	public String getOndblclick() {
		return ondblclick;
	}

	public void setOndblclick(String ondblclick) {
		this.ondblclick = ondblclick;
	}

	public Page getPage() {
		return page;
	}

	public void setPage(Page page) {
		this.page = page;
	}

	public List getDataList() {
		return dataList;
	}

	public void setDataList(List dataList) {
		this.dataList = dataList;
	}

	public boolean getIsShowMenu() {
		return isShowMenu;
	}

	public void setIsShowMenu(boolean isShowMenu) {
		this.isShowMenu = isShowMenu;
	}

	public void setCanDrag(boolean isCanDrag) {
		this.isCanDrag = isCanDrag;
	}

	public void setCanFixUpCol(boolean isCanFixUpCol) {
		this.isCanFixUpCol = isCanFixUpCol;
	}

	public void setCanSplit(boolean isCanSplit) {
		this.isCanSplit = isCanSplit;
	}

	public String getPagename() {
		return pagename;
	}

	public void setPagename(String pagename) {
		this.pagename = pagename;
	}

	public boolean isShowZebraLine() {
		return showZebraLine;
	}

	public void setShowZebraLine(boolean showZebraLine) {
		this.showZebraLine = showZebraLine;
	}

	public void addTableColTag(TableColTag tableColTag) {
		tableColTagList.add(tableColTag);
		String width = tableColTag.getWidth();
		if (width != null && width.endsWith("%")) {
			percentTableColTagList.add(tableColTag);
		} else {
			otherTableColTagList.add(tableColTag);
		}
	}

	private void algorithm() {
		int total = 100;
		int intWidth = 0;
		String stringWidth = "";
		int percentSize = (percentTableColTagList == null) ? 0
				: percentTableColTagList.size();
		if (percentSize > 0) {
			for (TableColTag tableColTag : percentTableColTagList) {
				stringWidth = tableColTag.getWidth().substring(0,
						tableColTag.getWidth().length() - 1);
				intWidth = Integer.parseInt(stringWidth);
				total -= intWidth;
			}
		}
		int otherSize = (otherTableColTagList == null) ? 0
				: otherTableColTagList.size();
		if (otherSize > 0) {// 存在其他列
			int averageWidth = total / otherSize;
			int remainderWidth = total % otherSize;
			int lastWidth = averageWidth + remainderWidth;
			int index = 0;
			for (TableColTag tableColTag : otherTableColTagList) {
				if (index == otherSize) {// 设置最后一列宽度
					tableColTag.setWidth(lastWidth + "%");
				} else {
					tableColTag.setWidth(averageWidth + "%");
				}
				index += 1;
			}
		} else {
			int averageWidth = total / percentSize;
			int remainderWidth = total % percentSize;
			int lastWidth = averageWidth + remainderWidth;
			int index = 0;
			for (TableColTag tableColTag : percentTableColTagList) {
				stringWidth = tableColTag.getWidth().substring(0,
						tableColTag.getWidth().length() - 1);
				intWidth = Integer.parseInt(stringWidth);
				if (index == percentSize - 1) {// 设置最后一列宽度
					tableColTag.setWidth((intWidth + lastWidth) + "%");
				} else {
					tableColTag.setWidth((intWidth + averageWidth) + "%");
				}
				index += 1;
			}
		}
	}

	public String getPageSize() {
		return pageSize;
	}

	public void setPageSize(String pageSize) {
		this.pageSize = pageSize;
	}
}
