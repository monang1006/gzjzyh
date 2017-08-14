<%@ page contentType="text/html; charset=utf-8"%>
<jsp:directive.page import="com.strongit.uums.optprivilmanage.BaseOptPrivilManager"/>
<jsp:directive.page import="com.strongmvc.service.ServiceLocator"/>
<jsp:directive.page import="com.strongit.oa.common.workflow.IWorkflowService"/>
<jsp:directive.page import="com.strongit.workflow.workflowInterface.ITaskService"/>
<jsp:directive.page import="com.strongit.workflow.bo.TwfBaseNodesetting"/>
<%@page import="com.strongit.workflow.bo.TwfBaseNodesettingPlugin"%>
<%@ taglib uri="/tags/strongitJbpm" prefix="strongit"%>
<%@ taglib uri="/tags/c.tld" prefix="c"%>
<%@include file="/common/include/rootPath.jsp"%>
<%
	List<List<Object[]>> nodes = (List<List<Object[]>>) request
			.getAttribute("nodes");
	//List<Object[]> ns = (List) request.getAttribute("nodes");
	String curdesp = (String) request.getAttribute("curdesp");//当前正运行的节点的描述信息
	String curid = (String) request.getAttribute("curid");//当前正运行的节点排序号
	//String instanceid = (String) request.getAttribute("instanceid");//流程实例id
	String flag = "";//显示在页面的内容

	//第一行
	int linesum = 5;//每行排列多少个节点
	String leftline = root + "/images/leftline.jpg";//箭头向左迁移线图片
	String rightline = root + "/images/rightline.jpg";//箭头向右迁移线图片
	String bgnode = root + "/images/bgnode.jpg";//节点背景图片
	String selnode = root + "/images/selnode.jpg";//节点背景图片
	String turnline = root + "/images/turnline.jpg";//换行时合并右转弯图片
	String rightturn = root + "/images/rightturn.jpg";//左转弯图片
	String dealed = root + "/images/dealed.jpg";//已处理的节点图片
	int imgPer = 1;//拐弯图片的拉长的倍数
	if (nodes.size() >= 3 * linesum) {
		flag = "流程简化失败！";
	} else if (nodes.size() > 0) {
		if (linesum > nodes.size()) {
			linesum = nodes.size();
		}
		flag = flag + "<tr align='center' valign='center'><td></td>";

		for (int i = 0; i < linesum; i++) {
			if(curdesp==null){
				continue;
			}
			List<Object[]> list = nodes.get(i);
			String colhtml = "";//当排序号相同时，建立一个html，遍历建立size()行，替换obj[3]
			if (list.size() > 1) {
				Object[] obj = null;
				String showmsg = "";

				colhtml = "<table>";
				int tmpi = 1;
				int imgHeight = 1;
				for (int n = 0; n < list.size(); n++) {
					obj = list.get(n);
					showmsg="";
					if (!"".equals(obj[3].toString())) {
						if (obj[3].toString().length() > 9) {
							showmsg = obj[3].toString().substring(0, 8)
									+ "...";
						} else {
							showmsg = obj[3].toString();
						}
					}
					if (curdesp.equals(obj[0]) && curid.equals(obj[1])) {
						colhtml = colhtml
								+ "<tr><td  width='100' height='74px' style='background:url("
								+ selnode
								+ ");background-repeat:no-repeat;background-position: center;'><table width='100' height='74px' border='0' cellspacing='0' cellpadding='0'><tr height='28px'   align='center'><td colspan='2' id='wrap'><span class='blueline'>"
								+ obj[0]
								+ "</span></td></tr><tr ><td width='18' ></td><td id='wrap' align='left' valign='center'><span class='redline'><a id='"
								+ i
								+ "' onmouseover=showlayer(this,'showtips') onmouseout=hidelayer('showtips') title='"
								+ "处理人:"+obj[3]
								+ "'>"
								+ showmsg
								+ "</a></span></td></tr></table></td></tr>";
					} else {
						String bg = "";
						String strword = "";
						if ("1".equals(obj[4].toString())) {
							bg = dealed;
							strword = "处理人:" + obj[3];
						} else {
							bg = bgnode;
							strword = "待处理人:" + obj[3];
						}
						colhtml = colhtml
								+ "<tr><td  width='100' height='74px' style='background:url("
								+ bg
								+ ");background-repeat:no-repeat;background-position: center;'><table width='100' height='74px' border='0' cellspacing='0' cellpadding='0'><tr height='28px'   align='center'><td colspan='2' id='wrap'><span class='blueline'>"
								+ obj[0]
								+ "</span></td></tr><tr  ><td width='18' ></td><td id='wrap' align='left' valign='center'><span class='redline'><a id='"
								+ i
								+ "' onmouseover=showlayer(this,'showtips') onmouseout=hidelayer('showtips') title='"
								+ strword
								+ "'>"
								+ showmsg
								+ "</a></span></td></tr></table></td></tr>";

					}
					tmpi = tmpi + 1;
				}
				if (tmpi > imgHeight) {
					imgHeight = tmpi;
					imgPer = tmpi;
				}
				colhtml = colhtml + "</table>";
				if (i == linesum - 1) {
					flag = flag + "<td>" + colhtml + "</td>";
				} else {
					flag = flag + "<td>" + colhtml
							+ "</td><td width='55'><img src='"
							+ rightline
							+ "' width='100%' height='10'></td>";
				}
			} else {//list.size()为1时
				Object[] obj = list.get(0);
				String showmsg = "";
				if (!"".equals(obj[3].toString())) {
					if (obj[3].toString().length() > 9) {
						showmsg = obj[3].toString().substring(0, 8)
								+ "...";
					} else {
						showmsg = obj[3].toString();
					}
				}
				if (curdesp.equals(obj[0]) && curid.equals(obj[1])) {
					if (i == linesum - 1) {
						flag = flag
								+ "<td   width='100' height='74px' style='background:url("
								+ selnode
								+ ");background-repeat:no-repeat;background-position: center;'><table width='100' height='74px' border='0' cellspacing='0' cellpadding='0'><tr height='28px'   align='center'><td colspan='2' id='wrap'><span class='blueline'>"
								+ obj[0]
								+ "</span></td></tr><tr ><td width='18' ></td><td id='wrap' align='left' valign='center'><span class='redline'><a id='"
								+ i
								+ "' onmouseover=showlayer(this,'showtips') onmouseout=hidelayer('showtips') title='"
								+ "处理人:"+obj[3] + "'>" + showmsg
								+ "</a></span></td></tr></table></td>";
					} else {
						flag = flag
								+ "<td   width='100' height='74px' style='background:url("
								+ selnode
								+ ");background-repeat:no-repeat;background-position: center;'><table width='100' height='74px' border='0' cellspacing='0' cellpadding='0'><tr height='28px'  align='center'><td colspan='2' id='wrap'><span class='blueline'>"
								+ obj[0]
								+ "</span></td></tr><tr  ><td width='18'></td><td id='wrap' align='left' valign='center'><span class='redline'><a id='"
								+ i
								+ "' onmouseover=showlayer(this,'showtips') onmouseout=hidelayer('showtips') title='"
								+ "处理人:"+obj[3]
								+ "'>"
								+ showmsg
								+ "</a></span></td></tr></table></td><td width='55'><img src='"
								+ rightline
								+ "' width='100%' height='10'></td>";
					}
				} else {
					String bg="";
					String strword="";
					if("1".equals(obj[4].toString())){
						bg=dealed;
						strword="处理人:"+obj[3];
					}else{
						bg=bgnode;
						strword="待处理人:"+obj[3];
					}	
					if (i == linesum - 1) {
						flag = flag
								+ "<td  width='100' height='74px' style='background:url("
								+ bg
								+ ");background-repeat:no-repeat;background-position: center;'><table width='100' height='74px' border='0' cellspacing='0' cellpadding='0'><tr height='28px'   align='center'><td colspan='2' id='wrap'><span class='blueline'>"
								+ obj[0]
								+ "</span></td></tr><tr  ><td width='18' ></td><td id='wrap' align='left' valign='center'><span class='redline'><a id='"
								+ i
								+ "' onmouseover=showlayer(this,'showtips') onmouseout=hidelayer('showtips') title='"
								+ strword + "'>" + showmsg
								+ "</a></span></td></tr></table></td>";//箭头向右
					} else {
						flag = flag
								+ "<td   width='100' height='74px' style='background:url("
								+ bg
								+ ");background-repeat:no-repeat;background-position: center;'><table width='100' height='74px' border='0' cellspacing='0' cellpadding='0'><tr height='28px'  align='center'><td colspan='2' id='wrap'><span class='blueline'>"
								+ obj[0]
								+ "</span></td></tr><tr  ><td width='18'></td><td id='wrap' align='left' valign='center'><span class='redline'><a id='"
								+ i
								+ "' onmouseover=showlayer(this,'showtips') onmouseout=hidelayer('showtips') title='"
								+ strword
								+ "'>"
								+ showmsg
								+ "</a></span></td></tr></table></td><td width='55'><img src='"
								+ rightline
								+ "' width='100%' height='10'></td>";//箭头向左
					}
				}
			}

		}
		//大于1行，最多2行
		if (linesum < nodes.size() && (2 * linesum) >= nodes.size()) {
			//获取本行内重复序列号相同最多的节点

			int imgHeight = 1;
			for (int n = nodes.size() - 1; n >= linesum; n--) {
				List<Object[]> list = nodes.get(n);
				int tmpi = 1;
				if (list.size() > 1) {
					for (int m = 0; m < list.size(); m++) {
						tmpi = tmpi + 1;
					}
					if (tmpi > imgHeight) {
						imgHeight = tmpi;
					}
				}
			}
			if (imgHeight > 1) {
				imgHeight = imgHeight - 1;
			}
			if (imgPer > 1) {
				imgPer = imgPer - 1;
			}

			int top = (imgPer * 85) / 2;//距离顶端的距离

			imgPer = imgPer + imgHeight;

			int srcHeight = (85 * imgPer) / 2;
			flag = flag
					+ "<td rowspan='2' valign='top' style='padding-top:"
					+ top + "px'><img src='" + turnline + "' height='"
					+ srcHeight + "px' width='32' ></td></tr>";//显示换行的图片

			//第2行
			flag = flag + "<tr align='center' height='100'>";
			//多余的先填充空表格
			for (int m = 0; m < 2 * (2 * linesum - nodes.size()) + 1; m++) {
				flag = flag + "<td></td>";
			}
			//倒序填充

			imgPer = 1;//将倍数重新初始化

			for (int i = nodes.size() - 1; i >= linesum; i--) {
				List<Object[]> list = nodes.get(i);
				String colhtml = "";//当排序号相同时，建立一个html，遍历建立size()行，替换obj[3]
				if (list.size() > 1) {
					Object[] obj = null;
					String showmsg = "";

					colhtml = "<table>";
					for (int n = 0; n < list.size(); n++) {
						obj = list.get(n);
						showmsg="";
						if (!"".equals(obj[3].toString())) {
							if (obj[3].toString().length() > 9) {
								showmsg = obj[3].toString().substring(
										0, 8)
										+ "...";
							} else {
								showmsg = obj[3].toString();
							}
						}

						if (curdesp.equals(obj[0])
								&& curid.equals(obj[1])) {
							colhtml = colhtml
									+ "<tr><td  width='100' height='74px' style='background:url("
									+ selnode
									+ ");background-repeat:no-repeat;background-position: center;'><table width='100' height='74px' border='0' cellspacing='0' cellpadding='0'><tr height='28px'   align='center'><td colspan='2' id='wrap'><span class='blueline'>"
									+ obj[0]
									+ "</span></td></tr><tr ><td width='18' ></td><td id='wrap' align='left' valign='center'><span class='redline'><a id='"
									+ i
									+ "' onmouseover=showlayer(this,'showtips') onmouseout=hidelayer('showtips') title='"
									+ "处理人:"+obj[3]
									+ "'>"
									+ showmsg
									+ "</a></span></td></tr></table></td></tr>";
						} else {
							String bg = "";
							String strword = "";
							if ("1".equals(obj[4].toString())) {
								bg = dealed;
								strword = "处理人:" + obj[3];
							} else {
								bg = bgnode;
								strword = "待处理人:" + obj[3];
							}
							colhtml = colhtml
									+ "<tr><td  width='100' height='74px' style='background:url("
									+ bg
									+ ");background-repeat:no-repeat;background-position: center;'><table width='100' height='74px' border='0' cellspacing='0' cellpadding='0'><tr height='28px'   align='center'><td colspan='2' id='wrap'><span class='blueline'>"
									+ obj[0]
									+ "</span></td></tr><tr  ><td width='18' ></td><td id='wrap' align='left' valign='center'><span class='redline'><a id='"
									+ i
									+ "' onmouseover=showlayer(this,'showtips') onmouseout=hidelayer('showtips') title='"
									+ strword
									+ "'>"
									+ showmsg
									+ "</a></span></td></tr></table></td></tr>";
						}
					}
					colhtml = colhtml + "</table>";
					if (i == linesum - 1) {
						flag = flag + "<td>" + colhtml + "</td>";
					} else if (i == linesum) {
						flag = flag + "<td>" + colhtml + "</td>";
					} else {
						flag = flag + "<td>" + colhtml
								+ "</td><td width='55'><img src='"
								+ leftline
								+ "' width='100%' height='10'></td>";
					}
				} else {
					Object[] obj = list.get(0);
					String showTwoMsg = "";
					if (!"".equals(obj[3].toString())) {
						if (obj[3].toString().length() > 9) {
							showTwoMsg = obj[3].toString().substring(0,
									8)
									+ "...";
						} else {
							showTwoMsg = obj[3].toString();
						}
					}
					if (curdesp.equals(obj[0]) && curid.equals(obj[1])) {
						if (i == linesum) {
							flag = flag
									+ "<td  width='100' height='74px' style='background:url("
									+ selnode
									+ ");background-repeat:no-repeat;background-position: center;'><table width='100' height='74px' border='0' cellspacing='0' cellpadding='0'><tr height='28px'  align='center'><td colspan='2' id='wrap'><span class='blueline'>"
									+ obj[0]
									+ "</span></td></tr><tr  ><td width='18'> </td><td id='wrap' align='left' valign='center'><span class='redline'><a id='"
									+ i
									+ "' onmouseover=showlayer(this,'showtips') onmouseout=hidelayer('showtips') title='"
									+ "处理人:"+obj[3]
									+ "'>"
									+ showTwoMsg
									+ "</a></span></td></tr></table></td>";
						} else {
							flag = flag
									+ "<td  width='100' height='74px' style='background:url("
									+ selnode
									+ ");background-repeat:no-repeat;background-position: center;'><table width='100' height='74px' border='0' cellspacing='0' cellpadding='0'><tr height='28px'  align='center'><td colspan='2' id='wrap'><span class='blueline'>"
									+ obj[0]
									+ "</span></td></tr><tr  ><td width='18'> </td><td id='wrap' align='left' valign='center'><span class='redline'><a id='"
									+ i
									+ "' onmouseover=showlayer(this,'showtips') onmouseout=hidelayer('showtips') title='"
									+ "处理人:"+obj[3]
									+ "'>"
									+ showTwoMsg
									+ "</a></span><td></tr></table></td><td width='55'><img src='"
									+ leftline
									+ "' width='100%' height='10'></td>";
						}
					} else {
						String bg="";
						String strword="";
						if("1".equals(obj[4].toString())){
							bg=dealed;
							strword="处理人:"+obj[3];
						}else{
							bg=bgnode;
							strword="待处理人:"+obj[3];
						}	
						if (i == linesum) {
							flag = flag
									+ "<td  width='100' height='74px' style='background:url("
									+ bg
									+ ");background-repeat:no-repeat;background-position: center;'><table width='100' height='74px' border='0' cellspacing='0' cellpadding='0'><tr height='28px'  align='center'><td colspan='2' id='wrap'><span class='blueline'>"
									+ obj[0]
									+ "</span></td></tr><tr ><td width='18'> </td><td id='wrap' align='left' valign='center'><span class='redline'><a id='"
									+ i
									+ "' onmouseover=showlayer(this,'showtips') onmouseout=hidelayer('showtips') title='"
									+ strword
									+ "'>"
									+ showTwoMsg
									+ "</a></span></td></tr></table></td>";//箭头向右
						} else {
							flag = flag
									+ "<td width='100' height='74px' style='background:url("
									+ bg
									+ ");background-repeat:no-repeat;background-position: center;'><table width='100' height='74px' border='0' cellspacing='0' cellpadding='0'><tr height='28px'  align='center'><td colspan='2' id='wrap'><span class='blueline'>"
									+ obj[0]
									+ "</span></td></tr><tr><td width='18'> </td><td id='wrap' align='left' valign='center'><span class='redline'><a id='"
									+ i
									+ "' onmouseover=showlayer(this,'showtips') onmouseout=hidelayer('showtips') title='"
									+ strword
									+ "'>"
									+ showTwoMsg
									+ "</a></span></td></tr></table></td><td width='55'><img src='"
									+ leftline
									+ "' width='100%' height='10'></td>";//箭头向左
						}
					}
				}

			}
			flag = flag + "</tr>";
		} else if ((2 * linesum) < nodes.size()
				&& (3 * linesum) > nodes.size()) {

			int imgHeight = 1;
			for (int i = 2 * linesum - 1; i >= linesum; i--) {
				List<Object[]> list = nodes.get(i);
				int tmpi = 1;
				if (list.size() > 1) {
					for (int n = 0; n < list.size(); n++) {
						tmpi = tmpi + 1;
					}
					if (tmpi > imgHeight) {
						imgHeight = tmpi;
					}
				}
			}

			if (imgPer > 1) {
				imgPer = imgPer - 1;
			}
			int firstLine = imgPer;//第一行相同排序号的节点数

			int top = (imgPer * 85) / 2;//距离顶端的距离

			if (imgHeight > 1) {
				imgHeight = imgHeight - 1;
			}

			int twoLine = imgHeight;//第2行的

			imgPer = imgPer + imgHeight;//第一行加第2行

			int srcHeight = (85 * imgPer) / 2;

			flag = flag
					+ "<td rowspan='2' valign='top' style='padding-top:"
					+ top + "px'><img src='" + turnline + "' height='"
					+ srcHeight + "px' width='32'></td></tr>";//显示换行的图片

			//已经处理完第1以及第2行的，则将imgHeight初始化为1
			imgHeight = 1;
			imgPer = 1;

			for (int i = 2 * linesum; i < nodes.size(); i++) {
				List<Object[]> list = nodes.get(i);
				int tmpi = 1;
				if (list.size() > 1) {
					for (int n = 0; n < list.size(); n++) {
						tmpi = tmpi + 1;
					}
					if (tmpi > imgHeight) {
						imgHeight = tmpi;
					}
				}
			}
			if (imgHeight > 1) {
				imgHeight = imgHeight - 1;
			}
			int threeLine = imgHeight;//第3行的

			imgPer = twoLine + threeLine;

			int itop = (twoLine * 85) / 2;//距离顶端的距离
			int isrcHeight = (85 * imgPer) / 2;

			//第3行
			flag = flag + "<tr align='center' height='100'>";
			flag = flag
					+ "<td rowspan='2' valign='top' style='padding-top:"
					+ itop + "px'><img src='" + rightturn
					+ "' height='" + isrcHeight
					+ "px' width='32'></td>";

			//倒序填充

			for (int i = 2 * linesum - 1; i >= linesum; i--) {
				List<Object[]> list = nodes.get(i);
				String colhtml = "";
				if (list.size() > 1) {
					Object[] obj = null;
					String showmsg = "";

					colhtml = "<table>";
					for (int n = 0; n < list.size(); n++) {
						showmsg="";
						obj = list.get(n);
						if (!"".equals(obj[3].toString())) {
							if (obj[3].toString().length() > 9) {
								showmsg = obj[3].toString().substring(
										0, 8)
										+ "...";
							} else {
								showmsg = obj[3].toString();
							}
						}

						if (curdesp.equals(obj[0])
								&& curid.equals(obj[1])) {
							colhtml = colhtml
									+ "<tr><td  width='100' height='74px' style='background:url("
									+ selnode
									+ ");background-repeat:no-repeat;background-position: center;'><table width='100' height='74px' border='0' cellspacing='0' cellpadding='0'><tr height='28px'   align='center'><td colspan='2' id='wrap'><span class='blueline'>"
									+ obj[0]
									+ "</span></td></tr><tr ><td width='18' ></td><td id='wrap' align='left' valign='center'><span class='redline'><a id='"
									+ i
									+ "' onmouseover=showlayer(this,'showtips') onmouseout=hidelayer('showtips') title='"
									+ "处理人:"+obj[3]
									+ "'>"
									+ showmsg
									+ "</a></span></td></tr></table></td></tr>";
						} else {
							String bg="";
							String strword="";
							if("1".equals(obj[4].toString())){
								bg=dealed;
								strword="处理人:"+obj[3];
							}else{
								bg=bgnode;
								strword="待处理人:"+obj[3];
							}	
							colhtml = colhtml
									+ "<tr><td  width='100' height='74px' style='background:url("
									+ bg
									+ ");background-repeat:no-repeat;background-position: center;'><table width='100' height='74px' border='0' cellspacing='0' cellpadding='0'><tr height='28px'   align='center'><td colspan='2' id='wrap'><span class='blueline'>"
									+ obj[0]
									+ "</span></td></tr><tr  ><td width='18' ></td><td id='wrap' align='left' valign='center'><span class='redline'><a id='"
									+ i
									+ "' onmouseover=showlayer(this,'showtips') onmouseout=hidelayer('showtips') title='"
									+ strword
									+ "'>"
									+ showmsg
									+ "</a></span></td></tr></table></td></tr>";
						}
					}
					colhtml = colhtml + "</table>";
					if (i == linesum - 1) {
						flag = flag + "<td>" + colhtml + "</td>";
					} else if (i == linesum) {
						flag = flag + "<td>" + colhtml + "</td>";
					} else {
						flag = flag + "<td>" + colhtml
								+ "</td><td width='55'><img src='"
								+ leftline
								+ "' width='100%' height='10'></td>";
					}
				} else {

					Object[] obj = list.get(0);
					String showTwoMsg = "";
					if (!"".equals(obj[3].toString())) {
						if (obj[3].toString().length() > 9) {
							showTwoMsg = obj[3].toString().substring(0,
									8)
									+ "...";
						} else {
							showTwoMsg = obj[3].toString();
						}
					}
					if (curdesp.equals(obj[0]) && curid.equals(obj[1])) {
						if (i == linesum) {
							flag = flag
									+ "<td  width='100' height='74px' style='background:url("
									+ selnode
									+ ");background-repeat:no-repeat;background-position: center;'><table width='100' height='74px' border='0' cellspacing='0' cellpadding='0'><tr height='28px'  align='center'><td colspan='2' id='wrap'><span class='blueline'>"
									+ obj[0]
									+ "</span></td></tr><tr  ><td width='18'> </td><td id='wrap' align='left' valign='center'><span class='redline'><a id='"
									+ i
									+ "' onmouseover=showlayer(this,'showtips') onmouseout=hidelayer('showtips') title='"
									+ "处理人:"+obj[3]
									+ "'>"
									+ showTwoMsg
									+ "</a></span></td></tr></table></td>";
						} else {
							flag = flag
									+ "<td  width='100' height='74px' style='background:url("
									+ selnode
									+ ");background-repeat:no-repeat;background-position: center;'><table width='100' height='74px' border='0' cellspacing='0' cellpadding='0'><tr height='28px'  align='center'><td colspan='2' id='wrap'><span class='blueline'>"
									+ obj[0]
									+ "</span></td></tr><tr  ><td width='18'> </td><td id='wrap' align='left' valign='center'><span class='redline'><a id='"
									+ i
									+ "' onmouseover=showlayer(this,'showtips') onmouseout=hidelayer('showtips') title='"
									+ "处理人:"+obj[3]
									+ "'>"
									+ showTwoMsg
									+ "</a></span><td></tr></table></td><td width='55'><img src='"
									+ leftline
									+ "' width='100%' height='10'></td>";
						}
					} else {
						String bg="";
						String strword="";
						if("1".equals(obj[4].toString())){
							bg=dealed;
							strword="处理人:"+obj[3];
						}else{
							bg=bgnode;
							strword="待处理人:"+obj[3];
						}	
						if (i == linesum) {
							flag = flag
									+ "<td  width='100' height='74px' style='background:url("
									+ bg
									+ ");background-repeat:no-repeat;background-position: center;'><table width='100' height='74px' border='0' cellspacing='0' cellpadding='0'><tr height='28px'  align='center'><td colspan='2' id='wrap'><span class='blueline'>"
									+ obj[0]
									+ "</span></td></tr><tr ><td width='18'> </td><td id='wrap' align='left' valign='center'><span class='redline'><a id='"
									+ i
									+ "' onmouseover=showlayer(this,'showtips') onmouseout=hidelayer('showtips') title='"
									+ strword
									+ "'>"
									+ showTwoMsg
									+ "</a></span></td></tr></table></td>";//箭头向右
						} else {
							flag = flag
									+ "<td width='100' height='74px' style='background:url("
									+ bg
									+ ");background-repeat:no-repeat;background-position: center;'><table width='100' height='74px' border='0' cellspacing='0' cellpadding='0'><tr height='28px'  align='center'><td colspan='2' id='wrap'><span class='blueline'>"
									+ obj[0]
									+ "</span></td></tr><tr ><td width='18'> </td><td id='wrap' align='left' valign='center'><span class='redline'><a id='"
									+ i
									+ "' onmouseover=showlayer(this,'showtips') onmouseout=hidelayer('showtips') title='"
									+ strword
									+ "'>"
									+ showTwoMsg
									+ "</a></span></td></tr></table></td><td width='55'><img src='"
									+ leftline
									+ "' width='100%' height='10'></td>";//箭头向左
						}
					}
				}
			}
			flag = flag + "</tr>";

			//第3行
			flag = flag + "<tr align='center' >";

			//顺序填充

			for (int i = 2 * linesum; i < nodes.size(); i++) {
				List<Object[]> list = nodes.get(i);
				String colhtml = "";
				if (list.size() > 1) {
					Object[] obj = null;
					String showmsg = "";

					colhtml = "<table>";
					for (int n = 0; n < list.size(); n++) {
						showmsg="";
						obj = list.get(n);
						if (!"".equals(obj[3].toString())) {
							if (obj[3].toString().length() > 9) {
								showmsg = obj[3].toString().substring(
										0, 8)
										+ "...";
							} else {
								showmsg = obj[3].toString();
							}
						}

						if (curdesp.equals(obj[0])
								&& curid.equals(obj[1])) {
							colhtml = colhtml
									+ "<tr><td  width='100' height='74px' style='background:url("
									+ selnode
									+ ");background-repeat:no-repeat;background-position: center;'><table width='100' height='74px' border='0' cellspacing='0' cellpadding='0'><tr height='28px'   align='center'><td colspan='2' id='wrap'><span class='blueline'>"
									+ obj[0]
									+ "</span></td></tr><tr ><td width='18' ></td><td id='wrap' align='left' valign='center'><span class='redline'><a id='"
									+ i
									+ "' onmouseover=showlayer(this,'showtips') onmouseout=hidelayer('showtips') title='"
									+ "处理人:"+obj[3]
									+ "'>"
									+ showmsg
									+ "</a></span></td></tr></table></td></tr>";
						} else {
							String bg="";
							String strword="";
							if("1".equals(obj[4].toString())){
								bg=dealed;
								strword="处理人:"+obj[3];
							}else{
								bg=bgnode;
								strword="待处理人:"+obj[3];
							}	
							colhtml = colhtml
									+ "<tr><td  width='100' height='74px' style='background:url("
									+ bg
									+ ");background-repeat:no-repeat;background-position: center;'><table width='100' height='74px' border='0' cellspacing='0' cellpadding='0'><tr height='28px'   align='center'><td colspan='2' id='wrap'><span class='blueline'>"
									+ obj[0]
									+ "</span></td></tr><tr  ><td width='18' ></td><td id='wrap' align='left' valign='center'><span class='redline'><a id='"
									+ i
									+ "' onmouseover=showlayer(this,'showtips') onmouseout=hidelayer('showtips') title='"
									+ strword
									+ "'>"
									+ showmsg
									+ "</a></span></td></tr></table></td></tr>";
						}
					}
					colhtml = colhtml + "</table>";
					if (i == linesum - 1) {
						flag = flag + "<td>" + colhtml + "</td>";
					} else if (i == linesum) {
						flag = flag + "<td>" + colhtml + "</td>";
					} else if (i == nodes.size() - 1) {
						flag = flag + "<td>" + colhtml + "</td>";
					} else {
						flag = flag + "<td>" + colhtml
								+ "</td><td width='55'><img src='"
								+ rightline
								+ "' width='100%' height='10'></td>";
					}
				} else {
					Object[] obj = list.get(0);
					String showThrMsg = "";
					if (!"".equals(obj[3].toString())) {
						if (obj[3].toString().length() > 9) {
							showThrMsg = obj[3].toString().substring(0,
									8)
									+ "...";
						} else {
							showThrMsg = obj[3].toString();
						}
					}
					if (curdesp.equals(obj[0]) && curid.equals(obj[1])) {
						if (i == nodes.size() - 1) {
							flag = flag
									+ "<td  width='100' height='74px' style='background:url("
									+ selnode
									+ ");background-repeat:no-repeat;background-position: center;'><table width='100' height='74px' border='0' cellspacing='0' cellpadding='0'><tr height='28px'  align='center'><td colspan='2' id='wrap'><span class='blueline'>"
									+ obj[0]
									+ "</span></td></tr><tr  ><td width='18'>  </td><td id='wrap' align='left' valign='center'><span class='redline'><a id='"
									+ i
									+ "' onmouseover=showlayer(this,'showtips') onmouseout=hidelayer('showtips') title='"
									+ "处理人:"+obj[3]
									+ "'>"
									+ showThrMsg
									+ "</a></span></td></tr></table></td>";
						} else {
							flag = flag
									+ "<td  width='100' height='74px' style='background:url("
									+ selnode
									+ ");background-repeat:no-repeat;background-position: center;'><table width='100' height='74px' border='0' cellspacing='0' cellpadding='0'><tr height='28px'  align='center'><td colspan='2' id='wrap'><span class='blueline'>"
									+ obj[0]
									+ "</span></td></tr><tr ><td width='18'> </td><td id='wrap' align='left' valign='center'><span class='redline'><a id='"
									+ i
									+ "' onmouseover=showlayer(this,'showtips') onmouseout=hidelayer('showtips') title='"
									+ "处理人:"+obj[3]
									+ "'>"
									+ showThrMsg
									+ "</a></span><td></tr></table></td><td width='55'><img src='"
									+ rightline
									+ "' width='100%' height='10'></td>";
						}
					} else {
						String bg="";
						String strword="";
						if("1".equals(obj[4].toString())){
							bg=dealed;
							strword="处理人:"+obj[3];
						}else{
							bg=bgnode;
							strword="待处理人:"+obj[3];
						}	
						if (i == nodes.size() - 1) {
							flag = flag
									+ "<td  width='100' height='74px' style='background:url("
									+ bg
									+ ");background-repeat:no-repeat;background-position: center;'><table width='100' height='74px' border='0' cellspacing='0' cellpadding='0'><tr height='28px'  align='center'><td colspan='2' id='wrap'><span class='blueline'>"
									+ obj[0]
									+ "</span></td></tr><tr ><td width='18'> </td><td id='wrap' align='left' valign='center'><span class='redline'><a id='"
									+ i
									+ "' onmouseover=showlayer(this,'showtips') onmouseout=hidelayer('showtips') title='"
									+ strword
									+ "'>"
									+ showThrMsg
									+ "</a></span></td></tr></table></td>";//箭头向右
						} else {
							flag = flag
									+ "<td width='100' height='74px' style='background:url("
									+ bg
									+ ");background-repeat:no-repeat;background-position: center;'><table width='100' height='74px' border='0' cellspacing='0' cellpadding='0'><tr height='28px'  align='center'><td colspan='2' id='wrap'><span class='blueline'>"
									+ obj[0]
									+ "</span></td></tr><tr><td width='18'> </td><td id='wrap' align='left' valign='center'><span class='redline'><a id='"
									+ i
									+ "' onmouseover=showlayer(this,'showtips') onmouseout=hidelayer('showtips') title='"
									+ strword
									+ "'>"
									+ showThrMsg
									+ "</a></span></td></tr></table></td><td width='55'><img src='"
									+ rightline
									+ "' width='100%' height='10'></td>";//箭头向左
						}
					}
				}
			}
			//多余的先填充空表格
			for (int m = 0; m < 2 * (3 * linesum - nodes.size()); m++) {
				flag = flag + "<td></td>";
			}

			flag = flag + "<td></td>";
			flag = flag + "</tr>";
		} else {
			flag = flag + "</tr>";
		}
	}

	System.out.println("flag=" + flag);
%>
<html xmlns:v>
	<head>
		<%@include file="/common/include/meta.jsp"%>
		<title>查看流程图</title>
		<link href="<%=frameroot%>/css/windows.css" type="text/css" rel="stylesheet">
		<script src="<%=path%>/common/js/jquery/jquery-1.2.6.js" type="text/javascript"></script>
		<script src="<%=path%>/common/js/common/common.js" type="text/javascript"></script>
		<style type="text/css">
.redline {
	font-size: 12px;
	font-weight: bold;
	color: blue
}

.blueline {
	font-size: 11px;
	font-weight: bold;
	color: white;
	align
}

#wrap {
	word-break: break-all;
}

 v\:*{behavior: url(#default#VML);} 
</style>
		
		<div style="display:none;
			position:absolute; 
			width:240px; 
			height:200px; 
			filter:alpha(opacity=80); 
			top:expression((document.body.clientHeight-this.offsetHeight)/2);
			left:expression((document.body.clientWidth-this.offsetWidth)/2);  
			z-index:2; 
			background-color: #F2F9FC; 
			overflow:hidden; 
			border:#2870B7 solid 1px;"><p/>   
		    
	  </div> 
	
	 
	 <script type="text/javascript">
		
		function CPos(x, y)   
		{   
    		this.x = x;   
    		this.y = y;   
		}
		
		function GetObjPos(ATarget)   
		{   
    		var target = ATarget;   
    		var pos = new CPos(target.offsetLeft, target.offsetTop);   
       
    		var target = target.offsetParent;   
    		while (target)   
    		{   
        		pos.x += target.offsetLeft;   
        		pos.y += target.offsetTop;   
           		target = target.offsetParent   
    		}   
    		return pos;   
		}   
		function showlayer(obj , divid) {   
 			pos = GetObjPos(obj);   
 			l = document.getElementById(divid); 
 			
 			swidth=document.body.offsetWidth;//网页宽度
 			sheight=document.body.offsetHeight;//网页高度
 			if (pos.x>(swidth/2)){
 				l.style.left = pos.x - 240;
 			}else{
 				l.style.left = pos.x + 60;
 			}
 			
 			//l.style.left = pos.x - 240;   
 			l.style.top = pos.y + 10;   
 			l.style.display="block";
 			var str=obj.title;
 			var head=str.substring(0,str.indexOf(":"))//处理人
 			var namer=str.substring(str.indexOf(":")+1)//内容
 			
 			var arr;
 			var laststr="<table><tr><td width='60'  align='center'><span class='redline'>"+head+"</span ></td><td width='178'></td></tr>";
 			if(namer.indexOf(",")==-1){
 				laststr=laststr+"<tr><td width='60'  align='center'></td><td width='178'>"+namer+"</td></tr>";
 			}else{
 				arr=namer.split(",");
 				for(var i=0;i<arr.length;i++){
 					laststr=laststr+"<tr><td width='60'  align='center'></td><td width='178'>"+arr[i]+"</td></tr>";
 				}
 			}
 			laststr=laststr+"</table>"
 			
 			l.innerHTML=laststr;
		}   
		function hidelayer(divid){   
    		document.getElementById(divid).style.display = "none";   
		}   
		
		</script>
	</head>
	<body class="contentbodymargin">
		<!--  oncontextmenu="return false;"> -->
		<div id="contentborder" align="center">

			<table width="98%">
				<tr>
					<td height="5"></td>
				</tr>
			</table>
			     <table width="98%">
        <tr>
          <td align="center" valign="center">
          	<table>
          		
            	<%=flag%>

            </table>
 

          </td>
        </tr>
		<div>
      </table>
		<v:roundRect  id="showtips" style="position:absolute;left:20px;top:50px;width:240px; height: 200px;display:none;filter:alpha(opacity=80)" FillColor="#F2F9FC" Filled="T" />	 
		</div> 
	</body>
</html>