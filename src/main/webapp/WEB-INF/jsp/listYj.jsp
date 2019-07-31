<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>意见建议列表</title>
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
<script type="text/javascript">
	
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
	
	//意见发布日期的格式化
	function formatPubtime(val, row) {
		var d = new Date(val);
		return d.format("yyyy-MM-dd hh:mm:ss");
	}
	
	//操作栏
	function formateOperator(val, row) {
		return '<a href="#" onclick="openDdDiglog('+row.id+',&quot;'+row.yjTitle+'&quot;)" class="viewBtn">点击查看 </a>';

	}
	
	//打开意见内容对话框
	function openDdDiglog(id,yjTitle) {
		$('#dd').dialog('open').dialog('setTitle',yjTitle);
		$('#dd').dialog('refresh', '${pageContext.request.contextPath}/yj/findYj?id='+id);
	
	}
	
</script>
</head>
<body style="margin: 1px">
<table id="dg" title="意见建议列表" 
   fitColumns="true" pagination="true" rownumbers="true" singleSelect="true" 
   url="${pageContext.request.contextPath}/yj/list" fit="true">
   <thead>
   	<tr>
   		<th field="id" id="yjId" hidden="true"></th>
   		<th field="yjTitle" width="50" align="center">标题</th>
   		<th field="realName" width="50" align="center" >建议人</th>
   		<th field="pubTime" width="50" align="center" formatter="formatPubtime">提交日期</th>
   		<th field="operator" width="50" align="center" formatter="formateOperator">操作</th>
   	</tr>
   </thead>
 </table>
 
<!-- 查看意见开始 -->
<div id="dd" class="easyui-dialog" style="width:400px;height:250px;padding:20px 20px;" modal="true" closed="true">   
	
</div>  
<!-- 查看意见结束 -->
 


<script type="text/javascript">
/*
$("#dg").datagrid({
	onLoadSuccess:function(data){
		$(".viewBtn").linkbutton({text:"任务分配", plain:true});
	}
});
*/
//session失效跳转至登陆界面
$('#dg').datagrid({
	onLoadSuccess:function(result) {
		$(".viewBtn").linkbutton({text:"点击查看", plain:true})
		if (!result.success) {
			alert(result.errMsg);
			parent.window.location.href="login.jsp";
		}
	}
});

</script>
</body>
</html>