<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>任务考核</title>
<%
	// 权限验证
	if(session.getAttribute("currentUser")==null){
		response.sendRedirect(request.getContextPath()+"/sessionError");
		return;
	}
%>
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/static/jquery-easyui-1.3.3/themes/default/easyui.css">
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/static/jquery-easyui-1.3.3/themes/icon.css">
<script type="text/javascript" src="${pageContext.request.contextPath}/static/jquery-easyui-1.3.3/jquery.min.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/static/jquery-easyui-1.3.3/jquery.easyui.min.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/static/jquery-easyui-1.3.3/locale/easyui-lang-zh_CN.js"></script>
<link rel="stylesheet" href="${pageContext.request.contextPath}/static/kindeditor/themes/default/default.css" />
<link rel="stylesheet" href="${pageContext.request.contextPath}/static/kindeditor/themes/simple/simple.css" />
<script type="text/javascript" charset="utf-8" src="${pageContext.request.contextPath}/static/kindeditor/kindeditor-all-min.js"></script>
<script type="text/javascript" charset="utf-8" src="${pageContext.request.contextPath}/static/kindeditor/lang/zh-CN.js"></script>
<script src="${pageContext.request.contextPath}/static/echarts/echarts.js"></script>
<script type="text/javascript">

	//标题格式化显示
	function formatTitle(val,row){
		if (val.length > 20) {
			return "<a href='javascript:openDdDiglog("+row.id+",&quot;"+row.rwTitle+"&quot;)'>"+val.substring(0,21)+"...</a>";
		} else {
			return "<a href='javascript:openDdDiglog("+row.id+",&quot;"+row.rwTitle+"&quot;)'>"+val+"</a>";
		}
	}
	
	//对 Date.prototype 的扩展来实现的
	Date.prototype.format = function(format) {
	    var o = {
	        "M+": this.getMonth() + 1, //month 
	        "d+": this.getDate(), //day 
	        "h+": this.getHours(), //hour 
	        "m+": this.getMinutes(), //minute 
	        "s+": this.getSeconds(), //second 
	        "q+": Math.floor((this.getMonth() + 3) / 3), //quarter 
	        "S": this.getMilliseconds() //millisecond 
	    };

	    if (/(y+)/i.test(format)) {
	        format = format.replace(RegExp.$1, (this.getFullYear() + "").substr(4 - RegExp.$1.length));
	    }

	    for (var k in o) {
	        if (new RegExp("(" + k + ")").test(format)) {
	            format = format.replace(RegExp.$1, RegExp.$1.length == 1 ? o[k] : ("00" + o[k]).substr(("" + o[k]).length));
	        }
	    }
	    return format;
	};
	
	//开始日期和截至日期的格式化
	function formatStartEndTime(val) {
		var d = new Date(val);
		return d.format("yyyy-MM-dd");
	}
	
	//任务发布日期的格式化
	function formatPubtime(val) {
		if (null != val) {
			var d = new Date(val);
			return d.format("yyyy-MM-dd hh:mm:ss");
		} else {
			return null;
		}
	}
	
	//任务状态
	function formateRwstate(val, row) {
		if (val == 0) {
			return "<font color='red'>待签收</font>";
		} else if (val == 1) {
			return "进行中";
		} else if (val == 2) {
			return "<font color='green'>已完成</font>";
		} 
	}
	
	//任务完成时效
	function formateRwRx(val,row) {
		if (val == 0) {
			return "<font color='red'>未完成(0)</font>";
		} else if (val == 3) {
			return "提前完成(3)";
		} else if (val == 2) {
			return "按时完成(2)";
		} else if (val == 1) {
			return "超期完成(1)";
		}
	}
	
	//任务超期
	function formateRwCq(val, row) {
		if (val == 1) {
			return "<font color='red'>是</font>";
		} else if (val == 0) {
			return "否";
		}
	}
	
	//任务总分
	function formateRwScore(val,row) {
		var flag = row.completeQK
		var xishu = 0
		if (flag == 0) {  //未完成
			xishu = 0;
		} else if (flag == 1) { //部分完成
			xishu = 0.5;
		} else if (flag == 2) { //一般
			xishu = 1;
		} else if (flag == 3) { //较好
			xishu = 1.5;
		}
		return xishu * (row.dengji + row.zhongyao + row.completeSX + row.nandu);
	}
	
	//等级
	function formateRwDengJi(val,row) {
		if (val == 1) {
			return "一般(1)";
		} else if (val == 2) {
			return "紧急(2)";
		} else if (val == 3) {
			return "特别紧急(3)";
		}
	}
	
	//难度
	function formateRwNanDu(val,row) {
		if (val == 1) {
			return "一般(1)";
		} else if (val == 3) {
			return "困难(3)";
		} else if (val == 5) {
			return "特别困难(5)";
		}
	}
	
	//重要性
	function formateRwZhongYao(val,row) {
		if (val == 1) {
			return "一般(1)";
		} else if (val == 2) {
			return "重要(2)";
		} else if (val == 3) {
			return "特别重要(3)";
		}
	}
	
	//完成情况
	function formateRwQk(val, row) {
		if (val == 0) {
			return "未完成(0)";
		} else if (val == 1) {
			return "部分完成(0.5)"
		} else if (val == 2) {
			return "一般(1)";
		} else if (val == 3) {
			return "较好(1.5)";
		}
	}
	
	//附件下载
	function formatFjUrl(val,row) {
		if (val != '' && val != null) {
			var fjName = row.fkFjName;
			if (fjName.length > 15) {
				fjName = fjName.substr(0,12)+"..."
			}
			return "<a target='_blank' href='${pageContext.request.contextPath}/"+val +"'>"+fjName+"</a>";
		}
	}
	

	//打开任务详细对话框
	function openDdDiglog(id, val) {
		//$('#dd').dialog('open').dialog('setTitle',val);
		//$('#dd').dialog('refresh', '${pageContext.request.contextPath}/rw/findRw?id='+id);
		$("#dd").dialog({
			title:val,
			modal:true,
			content:"<iframe scrolling='auto' frameborder='0' src='${pageContext.request.contextPath}/rw/findRw?flag=0&id="+id+"' style='width:100%; height:100%;'></iframe>"
		});
		$("#dd").dialog("open");
	}
	
	//操作栏
	function formateOperator(val, row) {
		return '<a href="#" onclick="openKhList('+row.userId+')" class="viewBtn">点击查看详情</a>';

	}
	
	//打开反馈日志列表
	function openKhList(userId) {
		$("#kh-dg").datagrid({
			url:"${pageContext.request.contextPath}/rw/findMyRw?userId="+userId
		});
		$("#kh-dlg").dialog("open");
	}

</script>
</head>
<body style="margin: 1px">
<table id="dg" title="任务考核" fit="false"
   fitColumns="true" pagination="true" rownumbers="true" singleSelect="true" scrollbarSize="0"
   url="${pageContext.request.contextPath}/rwKh/list" fit="true" >
   <thead>
   	<tr>
   		<th field="realName" width="100" align="center" >姓名</th>
   		<th field="count" width="100" align="center">任务个数</th>
   		<th field="totalScore" width="100" align="center">任务总分</th>
   		<th field="operator" width="100" align="center" formatter="formateOperator">操作</th>
   		<th field="userId" hidden="true"></th>
   	</tr>
   </thead>
</table>
<br/>
<!-- 图表 -->
<div id="kaohe-pic-zhu" style="width:550px;height:400px;float: left;"></div>
<div id="kaohe-pic-bing" style="width:550px;height:400px;float: right;"></div>
<script type="text/javascript">
        // 基于准备好的dom，初始化echarts实例
        var myChart = echarts.init(document.getElementById('kaohe-pic-zhu'));
        var myChart2 = echarts.init(document.getElementById('kaohe-pic-bing'));

        $.ajax({
        	url:"${pageContext.request.contextPath}/rwKh/score-name",
        	async:true,
        	cache:false,
        	type:"POST",
        	dataType:"json",
        	success:function(result){
        		//柱状图
                myChart.setOption({
                	title: {
                        text: '任务 - 人员 - 柱状图'
                    },
                    tooltip: {},
                    legend: {
                        data:['任务总分','任务个数']
                    },
                    xAxis: {
                     type:"category",
                     data:result.names,
                     axisLabel: {  
                    	   interval:0,  
                    	   rotate:40  
                    	}  
                    },
                    yAxis: {
                    	type:"value"
                    },
                    series: [
                    	{
	                        name: '任务总分',
	                        type: 'bar',
	                        data: result.scores
                    	},
                    	{
                    		name: '任务个数',
 	                        type: 'bar',
 	                        data: result.counts
                    	}
                    ]
                });
        		
        		//饼状图
                myChart2.setOption({
                	title : {
                        text: '任务 - 人员 - 饼状图',
                        x:'left'
                    },
                    tooltip : {
                        trigger: 'item',
                        formatter: "{a} <br/>{b} : {c} ({d}%)"
                    },
                    legend: {
                        type: 'scroll',
                        orient: 'vertical',
                        right: 'right',
                        data: result.names
                    },
                    series : [
                        {
                            name: '姓名',
                            type: 'pie',
                            radius : '55%',
                            center: ['40%', '50%'],
                            data: result.data,
                            itemStyle: {
                                emphasis: {
                                    shadowBlur: 10,
                                    shadowOffsetX: 0,
                                    shadowColor: 'rgba(0, 0, 0, 0.5)'
                                }
                            }
                        }
                    ]
                });
        	}
        });
        
       
        
</script>

<!-- 查看任务详细开始 -->
<div id="dd" class="easyui-dialog" style="width:600px;height:430px;" closed="true">   

</div>  
<!-- 查看任务详细结束 -->

<!-- 查看某个用户的考核详情开始 --> 
<div id="kh-dlg" class="easyui-dialog" title="查看考核详情" style="width:800px;height:400px;"
    data-options="closed:true">
	<table id="kh-dg" class="easyui-datagrid" fit="true" scrollbarSize="0" style="width: 100%;height: 100%;"
	   fitColumns="true" pagination="true" rownumbers="true" singleSelect="true" nowrap="false">
	   <thead>
	   	<tr>
	   		<th field="rwTitle" width="100" align="center" formatter="formatTitle" >标题</th>
	   		<th field="score" width="50" align="center" formatter="formateRwScore">分数</th>
	   		<th field="dengji" width="50" align="center" formatter="formateRwDengJi" >等级(分数)</th>
	   		<th field="nandu" width="50" align="center" formatter="formateRwNanDu" >难度(分数)</th>
	   		<th field="zhongyao" width="50" align="center" formatter="formateRwZhongYao" >重要性(分数)</th>
	   		<th field="completeSX" width="50" align="center" formatter="formateRwRx" >完成时效(分数)</th>
	   		<th field="completeQK" width="50" align="center" formatter="formateRwQk" >完成情况(系数)</th>
	   		<th field="completeDate" width="50" align="center" formatter="formatPubtime" >完成日期</th>
	   		<th field="id" hidden="true"></th>
	   	</tr>
	   </thead>
	 </table>
</div>
<!-- 查看某个用户的考核详情结束 -->

<!-- 查看反馈日志开始 -->

<div id="fk-dlg" class="easyui-dialog" title="查看反馈日志" style="width:800px;height:500px;"
    data-options="closed:true">
	<table id="fk-dg" class="easyui-datagrid" fit="true" scrollbarSize="0" style="width: 100%;height: 100%;"
	   fitColumns="true" pagination="true" rownumbers="true" singleSelect="true" nowrap="false">
	   <thead>
	   	<tr>
	   		<th field="fkName" width="50" align="center" >反馈人</th>
	   		<th field="fkTime" width="50" align="center" formatter="formatPubtime">反馈日期</th>
	   		<th field="content" width="100" align="center" >反馈内容</th>
	   		<th field="fkFj" width="100" align="center" formatter="formatFjUrl">附件</th>
	   		<th field="fkFjName" hidden="true"></th>
	   	</tr>
	   </thead>
	 </table>
</div>

<!-- 查看反馈日志结束 -->

<script type="text/javascript">
$('#dg').datagrid({
	onLoadSuccess:function(result) {
		$(".viewBtn").linkbutton({text:"点击查看", plain:true})
	}
});
</script>
</body>
</html>