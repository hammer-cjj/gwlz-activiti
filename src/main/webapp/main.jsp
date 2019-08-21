<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%response.setHeader("Cache-Control","no-store");%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>任务督办-主页</title>
<%
	// 权限验证
	if(session.getAttribute("currentUser")==null){
		response.sendRedirect("login.jsp");
		return;
	}
%>
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/static/jquery-easyui-1.3.3/themes/default/easyui.css">
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/static/jquery-easyui-1.3.3/themes/icon.css">
<script type="text/javascript" src="${pageContext.request.contextPath}/static/jquery-easyui-1.3.3/jquery.min.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/static/jquery-easyui-1.3.3/jquery.easyui.min.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/static/jquery-easyui-1.3.3/locale/easyui-lang-zh_CN.js"></script>
<script type="text/javascript">
	var url;
	
	function openTab(text,url,iconCls){
		if($("#tabs").tabs("exists",text)){
			$("#tabs").tabs("select",text);
		}else{
			var content="<iframe frameborder=0 scrolling='auto' style='width:100%;height:100%' src='${pageContext.request.contextPath}/"+url+"'></iframe>";
			$("#tabs").tabs("add",{
				title:text,
				iconCls:iconCls,
				closable:true,
				content:content
			});
		}
	}
	/* 
	function openTabCq(){
		if($("#tabs").tabs("exists","即将超期任务")){
			$("#tabs").tabs("select","即将超期任务");
		}else{
			var content="<iframe frameborder=0 scrolling='auto' style='width:100%;height:100%' src='${pageContext.request.contextPath}/rwManageCq'></iframe>";
			$("#tabs").tabs("add",{
				title:"即将超期任务",
				iconCls:'icon-archive',
				closable:true,
				content:content
			});
		}
	}
	 */
	//打开修改密码对话框
	function openPasswordModifyDialog(){
		$("#dlg").dialog("open").dialog("setTitle","修改密码");
		url="${pageContext.request.contextPath}/user/modifyPassword?id=${currentUser.id}";
	}
	
	//修改密码
	function modifyPassword(){
		$("#fm").form("submit",{
			url:url,
			onSubmit:function(){
				var newPassword=$("#newPassword").val();
				var newPassword2=$("#newPassword2").val();
				if(!$(this).form("validate")){
					return false;
				}
				if(newPassword!=newPassword2){
					$.messager.alert("系统提示","确认密码输入错误！");
					return false;
				}
				return true;
			},
			success:function(result){
				var result=eval('('+result+')');
				if(result.success){
					$.messager.alert("系统提示","密码修改成功，下一次登录生效！");
					resetValue();
					$("#dlg").dialog("close");
				}else{
					$.messager.alert("系统提示","密码修改失败！");
					return;
				}
			}
		 });
	}
	
	//关闭修改密码对话框
	function closePasswordModifyDialog(){
		resetValue();
		$("#dlg").dialog("close");
	}
	
	function resetValue(){
		$("#oldPassword").val("");
		$("#newPassword").val("");
		$("#newPassword2").val("");
	}
	
	//退出登陆
	function logout(){
		$.messager.confirm("系统提示","您确定要退出系统吗？",function(r){
			if(r){
				window.location.href='${pageContext.request.contextPath}/user/logout';
			} 
		 });
	}
	
	//刷新任务考核
	function refreshKaohe(){
		$.post("${pageContext.request.contextPath}/rwKh/refreshKaohe",{},function(result){
			if(result.success){
				$.messager.alert("系统提示","已成功刷新！");
			}else{
				$.messager.alert("系统提示","刷新失败！");
			}
		},"json");
	}
	
</script>
</head>
<body class="easyui-layout">
    <div region="north" style="height:110px;background-color: #E0ECFF">
    	<table style="padding: 10px" width="100%">
			<tr>
				<td width="50%">
					<img alt="logo" src="static/images/logo.png">
				</td>
				<td valign="bottom" align="right" width="50%">
					<font size="3">&nbsp;&nbsp;<strong>欢迎：</strong>${currentUser.realName }</font>
				</td>
			</tr>
		</table>
    </div>
    
    <div region="west" style="width: 250px" title="导航菜单" split="true">
		<div class="easyui-accordion" data-options="fit:true,border:false">
			<div title="工作管理"  data-options="iconCls:'icon-computer'" style="padding:10px;">
				<c:choose>
					<c:when test="${currentUser.roleId == 1 or  currentUser.roleId == 0}">
						<a href="javascript:openTab('任务督办管理','rwManage','icon-archive')" class="easyui-linkbutton" data-options="plain:true,iconCls:'icon-archive'" style="width: 150px;">任务督办管理</a>
						<a href="javascript:openTab('备忘任务管理','rwManageBw','icon-archive')" class="easyui-linkbutton" data-options="plain:true,iconCls:'icon-archive'" style="width: 150px;">备忘任务管理</a>
						<a href="javascript:openTab('任务审核管理','rwManageSh','icon-archive')" class="easyui-linkbutton" data-options="plain:true,iconCls:'icon-archive'" style="width: 150px;">任务审核管理</a>
					</c:when>
					<c:otherwise>
						<c:if test="${currentUser.roleId == 2 }">
							<a href="javascript:openTab('任务审核管理','rwManageSh','icon-archive')" class="easyui-linkbutton" data-options="plain:true,iconCls:'icon-archive'" style="width: 150px;">任务审核管理</a>
							<a href="javascript:openTab('我分配的任务','rwManageFp','icon-archive')" class="easyui-linkbutton" data-options="plain:true,iconCls:'icon-archive'" style="width: 150px;">我分配的任务</a>
						</c:if>
						<a href="javascript:openTab('我负责的任务','rwManageZr','icon-archive')" class="easyui-linkbutton" data-options="plain:true,iconCls:'icon-archive'" style="width: 150px;">我负责的任务</a>
						<a href="javascript:openTab('我参与的任务','rwManageCy','icon-archive')" class="easyui-linkbutton" data-options="plain:true,iconCls:'icon-archive'" style="width: 150px;">我参与的任务</a>
						<a href="javascript:openTab('个人提交任务','rwManageTj','icon-archive')" class="easyui-linkbutton" data-options="plain:true,iconCls:'icon-archive'" style="width: 150px;">个人提交任务</a>
					</c:otherwise>
				</c:choose>
			</div>
			<c:if test="${currentUser.roleId == 1 or  currentUser.roleId == 0}">
				<div title="任务考核管理"  data-options="iconCls:'icon-apply'" style="padding:10px">
					<a href="javascript:openTab('任务考核','rwKaohe','icon-doc')" class="easyui-linkbutton" data-options="plain:true,iconCls:'icon-doc'" style="width: 150px;">任务考核</a>
					<a href="javascript:refreshKaohe()" class="easyui-linkbutton" data-options="plain:true,iconCls:'icon-refresh'" style="width: 150px;">刷新考核分数</a>
				</div>
			</c:if>
			<div title="个人信息管理"  data-options="iconCls:'icon-grxx'" style="padding:10px">
				<a href="javascript:openPasswordModifyDialog()" class="easyui-linkbutton" data-options="plain:true,iconCls:'icon-modifyPassword'" style="width: 150px;">修改密码</a>
				<a href="javascript:logout()" class="easyui-linkbutton" data-options="plain:true,iconCls:'icon-exit'" style="width: 150px;">安全退出</a>
			</div>
			<c:if test="${currentUser.roleId == '0' }">
				<div title="基础数据管理" data-options="selected:true,iconCls:'icon-item'" style="padding: 10px">
					<a href="javascript:openTab('用户管理','userManage','icon-user')" class="easyui-linkbutton" data-options="plain:true,iconCls:'icon-user'" style="width: 150px">用户管理</a>
					<!-- <a href="javascript:openTab('角色管理','groupManage.jsp','icon-role')" class="easyui-linkbutton" data-options="plain:true,iconCls:'icon-role'" style="width: 150px">角色管理</a>
					<a href="javascript:openTab('用户权限管理','authManage.jsp','icon-power')" class="easyui-linkbutton" data-options="plain:true,iconCls:'icon-power'" style="width: 150px">用户权限管理</a> -->
				</div>
			</c:if>
			<div title="意见建议管理"  data-options="iconCls:'icon-bkgl'" style="padding:10px">
				<a href="javascript:openTab('提交意见建议','writeYj','icon-writeblog')" class="easyui-linkbutton" data-options="plain:true,iconCls:'icon-writeblog'" style="width: 150px">提交意见建议</a>
				<a href="javascript:openTab('查看意见建议','listYj','icon-plgl')" class="easyui-linkbutton" data-options="plain:true,iconCls:'icon-plgl'" style="width: 150px">查看意见建议</a>
			</div>
		</div>
	</div>
    
    <div data-options="region:'center'" >
    	<div class="easyui-tabs" fit="true" border="false" id="tabs">
		<div title="首页" data-options="iconCls:'icon-home'">
			<div align="center" style="padding-top: 100px"><font color="red" size="10">欢迎使用</font></div>
		</div>
	</div>
    </div>
    
    <div region="south" style="height: 50px;padding: 8px" align="center">
		Copyright © 2019 舟山市公安局科技信息化处 版权所有<br/>
		技术支持：陈枫  682327 cf@zss.zj
	</div>
	
	
	<div id="dlg" class="easyui-dialog" style="width:400px;height:200px;padding: 10px 20px"
	   closed="true" buttons="#dlg-buttons">
	   
	   <form id="fm" method="post">
	   	<table cellspacing="8px">
	   		<tr>
	   			<td>用户名：</td>
	   			<td><input type="text" id="userName" name="userName" readonly="readonly" value="${currentUser.userName }" style="width: 200px"/></td>
	   		</tr>
	   		<tr>
	   			<td>新密码：</td>
	   			<td><input type="password" id="newPassword" name="newPassword" class="easyui-validatebox" required="true" style="width: 200px"/></td>
	   		</tr>
	   		<tr>
	   			<td>确认新密码：</td>
	   			<td><input type="password" id="newPassword2" name="newPassword2" class="easyui-validatebox" required="true" style="width: 200px"/></td>
	   		</tr>
	   	</table>
	   </form>
	 </div>
	 
	 <div id="dlg-buttons">
	 	<a href="javascript:modifyPassword()" class="easyui-linkbutton" iconCls="icon-ok">保存</a>
	 	<a href="javascript:closePasswordModifyDialog()" class="easyui-linkbutton" iconCls="icon-cancel">关闭</a>
	 </div>
	 
</body>
<script type="text/javascript">
	$.ajax({
		url:"${pageContext.request.contextPath}/rw/tj",
		contentType: "application/json;charset=UTF-8",
		type:"GET",
		success:function(result) {
			if (result.success) {
				var message = ""
				if (result.flag == 1) {//处长权限
					if (result.weiCqTotal > 0) {
						message = message.concat("<div style='text-align:center;font-size:13px;line-height:13px;'>有<font color='red'>"+result.weiCqTotal+"</font>条任务即将超期</div>");
						$.messager.show({
							title:'系统消息',
							msg:message,
							timeout:0
							//showType:'slide'
						});
					}
				} else if (result.flag == 2) {//副处长
					if (result.weiCqZr > 0 || result.weiCqCy > 0) {
						if (result.weiCqZr > 0) {
							message = message.concat("<div style='text-align:center;font-size:13px;line-height:13px;'>您负责的任务有<font color='red'>"+result.weiCqZr+"</font>条即将超期</div>");
						}	 
						if (result.weiCqCy > 0) {
							message = message.concat("<div style='text-align:center;font-size:13px;line-height:13px;'>您参与的任务有<font color='red'>"+result.weiCqCy+"</font>条即将超期</div>");
						} 
						$.messager.show({
							title:'系统消息',
							msg:message,
							timeout:0
							//showType:'slide'
						});
					}
				} else if (result.flag == 3) {//其他
					if (result.weiCqZr > 0 || result.weiCqCy > 0) {
						if (result.weiCqZr > 0) {
							message = message.concat("<div style='text-align:center;font-size:13px;line-height:13px;'>您负责的任务有<font color='red'>"+result.weiCqZr+"</font>条即将超期</div>");
						}
						if (result.weiCqCy > 0) {
							message = message.concat("<div style='text-align:center;font-size:13px;line-height:13px;'>您参与的任务有<font color='red'>"+result.weiCqCy+"</font>条即将超期</div>");
						} 
						$.messager.show({
							title:'系统消息',
							msg:message,
							timeout:0
							//showType:'slide'
						});
					}
				}
			}
		}		
	});
</script>
</html>