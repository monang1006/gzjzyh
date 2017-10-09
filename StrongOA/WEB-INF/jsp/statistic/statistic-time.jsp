<meta http-equiv="X-UA-Compatible" content="IE=edge">
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="/common/include/rootPath3.jsp"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<html>
<head>
<title>按时间统计</title>
<%@include file="/common/include/meta.jsp"%>
<LINK type=text/css rel=stylesheet
	href="<%=frameroot%>/css/properties_windows_add.css">
<script type="text/javascript" language="javascript"
	src="<%=path%>/common/js/jquery/jquery-1.8.3.min.js"></script>
<LINK href="<%=path%>/css/properties_windows_list.css" type=text/css
	rel=stylesheet>
<script language="javascript" src="<%=path%>/common/js/echarts/echarts.min.js"></script>
</head>
<base target="_self" />
<body class=contentbodymargin oncontextmenu="return false;">
	<!-- 为ECharts准备一个具备大小（宽高）的Dom -->
    <div id="main" style="width: 100%;height:100%;"></div>
    <script type="text/javascript">
        // 基于准备好的dom，初始化echarts实例
        var myChart = echarts.init(document.getElementById('main'));
        
        var months = ['一月','二月','三月','四月','五月','六月','七月','八月','九月','十月','十一月','十二月'];
        var years = ${titlesJson};
        var datas = ${datasJson};
        var detailsOptions = [];
        // 组装明细数据
        if(years.length > 0){
			for(var i=0;i<years.length;i++){
				var yearName = years[i];
				detailsOptions.push({
					title : {
						text: yearName + '年申请情况',
						textStyle: {
				        	fontSize: 20
				        }
					},
		            series : [
		                {
				            name: '查询申请',
				            type: 'bar',
				            stack: '总量',
				            label: {
				                normal: {
				                    show: false,
				                    position: 'insideRight'
				                }
				            },
				            data: datas[yearName]["0"]
				        },
				        {
				            name: '冻结申请',
				            type: 'bar',
				            stack: '总量',
				            label: {
				                normal: {
				                    show: false,
				                    position: 'insideRight'
				                }
				            },
				            data: datas[yearName]["1"]
				        },
				        {
				            name: '续冻申请',
				            type: 'bar',
				            stack: '总量',
				            label: {
				                normal: {
				                    show: false,
				                    position: 'insideRight'
				                }
				            },
				            data: datas[yearName]["2"]
				        },
				        {
				            name: '解冻申请',
				            type: 'bar',
				            stack: '总量',
				            label: {
				                normal: {
				                    show: false,
				                    position: 'insideRight'
				                }
				            },
				            data: datas[yearName]["3"]
				        }
		            ]
				})
			}        	
        }
        
     	// 指定图表的配置项和数据
		var option = {
		    baseOption: {
		        timeline: {
		            axisType: 'category',
		            autoPlay: false,
		            playInterval: 1000,
		            data: years,
		            label: {
		                formatter : function(s) {
		                    return s;
		                }
		            }
		        },
		        tooltip: {
		        },
		        legend: {
		            data: ['查询申请', '冻结申请', '续冻申请', '解冻申请']
		        },
		        calculable : true,
		        grid: {
		            top: 80,
		            bottom: 100,
		            tooltip: {
		                trigger: 'axis',
		                axisPointer: {
		                    type: 'shadow',
		                    label: {
		                        show: true,
		                        formatter: function (params) {
		                            return params.value;
		                        }
		                    }
		                }
		            }
		        },
		        xAxis: [
		            {
		                'type':'category',
		                'axisLabel':{'interval':0},
		                'data':months,
		                splitLine: {show: false}
		            }
		        ],
		        yAxis: [
		            {
		                type: 'value',
		                name: '申请数量',
		                minInterval: 1
		            }
		        ]
		    },
		    options: detailsOptions
		};
        
        // 使用刚指定的配置项和数据显示图表。
        myChart.setOption(option);
    </script>
</body>
</html>