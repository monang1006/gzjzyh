<%@ page contentType="text/html; charset=utf-8" %>
<%@include file="/common/include/rootPath.jsp"%>
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
		<title>江西省财政信息系统</title>
		<SCRIPT type=text/JavaScript>
			<!--
			function show_tab(obj_name)
			{
				tab_1.style.display="none";
				tab_2.style.display="none";
				eval(obj_name+".style.display=\"block\"");
			}
			
			//-->
			function OpenNewWindow(url){
				window.open (url, 'FTP', 'top=0, left=0, toolbar=yes, menubar=yes, scrollbars=yes, resizable=yes,location=yes, status=yes');
			}
		</SCRIPT>
		<link href="<%=root %>/common/css/css.css" rel="stylesheet" type="text/css" />
	</head>

	<body>
		<div class="main_box">
			<div class="main_box_top"></div>
			<div class="main_box_md">
				<div id="tab_1" class="main_box_md_vgn">
					<div class="main_box_md_vgn_title">
						<ul>
							<li onMouseOver="javascript:show_tab('tab_1')" class="vgn_on"></li>
							<li onMouseOver="javascript:show_tab('tab_2')" class="vgn_off"></li>
						</ul>
					</div>
					<div class="main_box_md_content">
						<ul>
							<li class="main_box_md_content_title">
								<span><a href="#">[内网]江西省财政核心业务系统</a> </span>
							</li>
							<li class="main_box_md_content_title">
								<span><a href="index.jsp">[内网]江西省财政行政办公系统 </a>
								</span>
							</li>
						</ul>
						<ul>
							<li class="main_box_md_content_title">
								<span><a href="#">[内网]×××系统</a>
								</span>
							</li>
							<li class="main_box_md_content_title">
								<span><a href="#">[内网]×××下载</a>
								</span>
							</li>
						</ul>
					</div>
				</div>
				<div style="display:none;" id="tab_2" class="main_box_md_vgn">
					<div class="main_box_md_vgn_title">
						<ul>
							<li onMouseOver="javascript:show_tab('tab_1')" class="vgn_on2"></li>
							<li onMouseOver="javascript:show_tab('tab_2')" class="vgn_off2"></li>
						</ul>
					</div>
					<div class="main_box_md_content2">
						<ul>
							<li class="main_box_md_content_title2">
								<span><a href="#">江西省财政厅门户网站</a>
								</span>
							</li>
							<li class="main_box_md_content_title2">
								<span><a href="#">×××服务系统 </a>
								</span>
							</li>
						</ul>
						<ul>
							<li class="main_box_md_content_title2">
								<span><a href="#">×××查询 </a> </span>
							</li>
							<li class="main_box_md_content_title2">
								<span><a href="#">×××系统 </a>
								</span>
							</li>
						</ul>
					</div>
				</div>
			</div>
			<div class="main_box_bottom"></div>
		</div>
	</body>
</html>
