<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>用户管理</title>
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/static/jquery-easyui-1.3.3/themes/default/easyui.css">
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/static/jquery-easyui-1.3.3/themes/icon.css">
<script type="text/javascript" src="${pageContext.request.contextPath}/static/jquery-easyui-1.3.3/jquery.min.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/static/jquery-easyui-1.3.3/jquery.easyui.min.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/static/jquery-easyui-1.3.3/locale/easyui-lang-zh_CN.js"></script>
<script type="text/javascript">
	
	function deleteUser(){
		var selectRows=$("#dg").datagrid("getSelections");
		if(selectRows.length==0){
			$.messager.alert("系统提示","请选择要删除的数据！");
			return;
		}
		var strIds=[];
		for(var i=0;i<selectRows.length;i++){
			strIds.push(selectRows[i].id);
		}
		var ids=strIds.join(",");
		$.messager.confirm("系统提示","您确定要删除这<font color=red>"+selectRows.length+"</font>条数据吗?",function(r){
			if(r){
				$.post("${pageContext.request.contextPath}/user/userDelete",{ids:ids},function(result){
					if(result.success){
						$.messager.alert("系统提示","数据已经成功删除！");
						$("#dg").datagrid("reload");
					}else{
						$.messager.alert("系统提示","数据删除失败，请联系管理员！");
					}
				},"json");
			}
		});
	}
	
	//打开新增用户对话框
	function openUserAddDiglog(){
		$("#dlg").dialog("open").dialog("setTitle","添加用户信息");
		//设置flag的值为1，表示新增
		$("#flag").val(1);
		$("#userName").attr("readonly",false);
		//加载部门
		$("#dept").combobox({
			url:'${pageContext.request.contextPath}/dept/findDept',
			valueField:'id',
			textField:'deptName',
			required: true
		});
		//加载角色
		$("#role").combobox({
			url:'${pageContext.request.contextPath}/role/findRole',
			panelHeight:'auto',
			valueField:'id',
			textField:'roleName',
			required: true
		});
	}
	
	//打开修改对话框
	function openUserModifyDiglog(){
		var selectRows=$("#dg").datagrid("getSelections");
		if(selectRows.length!=1){
			$.messager.alert("系统提示","请选择一条要编辑的数据！");
			return;
		}
		var row=selectRows[0];
		$("#dlg").dialog("open").dialog("setTitle","编辑用户信息");
		$("#fm").form("load",row);
		//设置flag为2,2表示修改
		$("#flag").val(2);
		//用户名不能修改
		$("#userName").attr("readonly",true);
		//加载部门
		$("#dept").combobox({
			url:'${pageContext.request.contextPath}/dept/findDept',
			valueField:'id',
			textField:'deptName',
			required: true
		});
		//选中默认的选项
		$("#dept").combobox("select", row.deptId);
		//加载角色
		$("#role").combobox({
			url:'${pageContext.request.contextPath}/role/findRole',
			panelHeight:'auto',
			valueField:'id',
			textField:'roleName',
			required: true
		});
		//选中默认的选项
		$("#role").combobox("select", row.roleId);
	}
	
	//提交数据之前进行验证
	function checkData(){
		if($("#userName").val()==''){
			$.messager.alert("系统系统","请输入用户名！");
			$("#userName").focus();
			return;
		} else if ($("#dept").combobox("getValue") == "请选择部门") {
			$.messager.alert("系统系统","请选择部门！");
			$("#dept").focus();
			return;
		} else if ($("#role").combobox("getValue") == "请选择角色") {
			$.messager.alert("系统系统","请选择角色！");
			$("#role").focus();
			return;
		}
		var flag=$("#flag").val();
		if(flag==1) {  //新增
			//判断用户名是否已经存在
			$.post("${pageContext.request.contextPath}/user/existUserName",{userName:$("#userName").val()},function(result){
				if(result.exist){
					$.messager.alert("系统系统","该用户名已存在，请更换下！");
					$("#userName").focus();
				} else{
					var urlStr = '${pageContext.request.contextPath}/user/userSave';
					saveUser(urlStr);
				}
			},"json");
		} else { //修改
			var urlStr = '${pageContext.request.contextPath}/user/userEdit';
			saveUser(urlStr);
		}
	}
	
	//保存用户
	function saveUser(urlStr){
		$("#fm").form("submit",{
			url:urlStr,
			onSubmit:function(){
				return $(this).form("validate");
			},
			success:function(result){
				var result=eval('('+result+')');
				if(result.success){
					$.messager.alert("系统系统","保存成功！");
					resetValue();
					$("#dlg").dialog("close");
					$("#dg").datagrid("reload");
				}else{
					$.messager.alert("系统系统","保存失败！");
					return;
				}
			}
		});
	}
	
	function resetValue(){
		$("#userName").val("");
		$("#password").val("");
		$("#realName").val("");
		$("#dept").val("");
		$("#role").val("");
	}
	
	//关闭对话框
	function closeUserDialog(){
		$("#dlg").dialog("close");
		resetValue();
	}
</script>
</head>
<body style="margin: 1px">
<table id="dg" title="用户管理" class="easyui-datagrid"
  fitColumns="true" pagination="true" rownumbers="true"
  url="${pageContext.request.contextPath}/user/userList" fit="true" toolbar="#tb">
 <thead>
 	<tr>
 		<th field="cb" checkbox="true" align="center"></th>
 		<th field="userName" width="80" align="center">用户名</th>
 		<th field="password" width="80" align="center">密码</th>
 		<th field="realName" width="50" align="center">姓名</th>
 		<th field="deptName" width="80" align="center">部门</th>
 		<th field="roleName" width="80" align="center">角色</th>
 		<th field="tel" width="80" align="center">电话</th>
 	</tr>
 </thead>
</table>
<div id="tb">
 <div>
	<a href="javascript:openUserAddDiglog()" class="easyui-linkbutton" iconCls="icon-add" plain="true">添加</a>
	<a href="javascript:openUserModifyDiglog()" class="easyui-linkbutton" iconCls="icon-edit" plain="true">修改</a>
	<a href="javascript:deleteUser()" class="easyui-linkbutton" iconCls="icon-remove" plain="true">删除</a>
 </div>
</div>

<div id="dlg" class="easyui-dialog" style="width: 620px;height: 250px;padding: 10px 20px" closed="true" buttons="#dlg-buttons">
 
 	<form id="fm" method="post">
 		<table cellpadding="8px">
 			<tr>
 				<td>用户名：</td>
 				<td>
 					<input type="text" id="userName" name="userName" class="easyui-validatebox" required="true"/>
 				</td>
 				<td>&nbsp;&nbsp;&nbsp;&nbsp;</td>
 				<td>密码：</td>
 				<td>
 					<input type="text" id="password" name="password" class="easyui-validatebox" required="true"/>
 				</td>
 			</tr>
 			<tr>
 				<td>姓名：</td>
 				<td>
 					<input type="text" id="realName" name="realName" class="easyui-validatebox" required="true"/>
 				</td>
 				<td>&nbsp;&nbsp;&nbsp;&nbsp;</td>
 				<td>部门：</td>
 				<td>
 					<input type="text" id="dept" name="deptId" class="easyui-combobox" value="请选择部门" />
 				</td>
 			</tr>
 			<tr>
 				<td>角色：</td>
 				<td>
 					<input type="text" id="role" name="roleId" class="easyui-combobox" value="请选择角色" />
 					<!-- flag值为1表示新增，值为2表示修改 -->
 					<input type="hidden" id="flag" name="flag"/>
 					<input type="hidden" name="id">
 				</td>
 				<td>&nbsp;&nbsp;&nbsp;&nbsp;</td>
 				<td>电话：</td>
 				<td>
 					<input type="text" id="tel" name="tel" class="easyui-validatebox" required="true"/>
 				</td>
 			</tr>
 		</table>
 	</form>
 
</div>

<div id="dlg-buttons">
	<a href="javascript:checkData()" class="easyui-linkbutton" iconCls="icon-ok">保存</a>
	<a href="javascript:closeUserDialog()" class="easyui-linkbutton" iconCls="icon-cancel">关闭</a>
</div>
</body>
</html>