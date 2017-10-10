<meta http-equiv="X-UA-Compatible" content="IE=edge">
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="/common/include/rootPath3.jsp"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<html>
<head>
<title>按银行统计</title>
<%@include file="/common/include/meta.jsp"%>
<LINK type=text/css rel=stylesheet
	href="<%=frameroot%>/css/properties_windows_add.css">
<script type="text/javascript" language="javascript"
	src="<%=path%>/common/js/jquery/jquery-1.8.3.min.js"></script>
<LINK href="<%=path%>/css/properties_windows_list.css" type=text/css
	rel=stylesheet>
<script language="javascript" src="<%=path%>/common/js/echarts/echarts.min.new.js"></script>
</head>
<base target="_self" />
<body class=contentbodymargin oncontextmenu="return false;">
	<!-- 为ECharts准备一个具备大小（宽高）的Dom -->
    <div id="main" style="width: 100%;height:100%;"></div>
    <script type="text/javascript">
        // 基于准备好的dom，初始化echarts实例
        var myChart = echarts.init(document.getElementById('main'));
        
        var orgs = ${titlesJson};
        var datas = ${datasJson};
        
     	// 指定图表的配置项和数据
		var option = {
	    	title : {
				text: '银行申请统计',
				textStyle: {
		        	fontSize: 20
		        }
			},
			tooltip : {
		        trigger: 'axis',
		        axisPointer : {            // 坐标轴指示器，坐标轴触发有效
		            type : 'shadow'        // 默认为直线，可选为：'line' | 'shadow'
		        }
		    },
	        legend: {
	            data: ['待签收申请', '处理中申请', '已处理申请']
	        },
	        calculable : true,
	        xAxis: [
				{
				    type: 'value',
				    name: '申请数量',
				    minInterval: 1
				}
	        ],
	        yAxis: [
	            {
	                'type':'category',
	                'data':orgs
	            }
	        ],
	        series: [
                {
		            name: '待签收申请',
		            type: 'bar',
		            stack: '总量',
		            label: {
		                normal: {
		                    show: false,
		                    position: 'insideRight'
		                }
		            },
		            data: datas["2"]
		        },
		        {
		            name: '处理中申请',
		            type: 'bar',
		            stack: '总量',
		            label: {
		                normal: {
		                    show: false,
		                    position: 'insideRight'
		                }
		            },
		            data: datas["4"]
		        },
		        {
		            name: '已处理申请',
		            type: 'bar',
		            stack: '总量',
		            label: {
		                normal: {
		                    show: false,
		                    position: 'insideRight'
		                }
		            },
		            data: datas["5"]
		        }
            ]
		};
        
        // 使用刚指定的配置项和数据显示图表。
        myChart.setOption(option);
    </script>
</body>
</html>