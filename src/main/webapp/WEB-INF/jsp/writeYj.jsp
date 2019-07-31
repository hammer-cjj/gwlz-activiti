<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>意见建议管理</title>
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
	function submitData () {
		var yjTitle = $('#yjTitle').val();
		var yjContent = $('#editor_id').val();
		if (yjTitle == null || yjTitle == '') {
			alert("请输入标题!");
		} else if (yjContent == null || yjContent == '') {
			alert("请输入内容!");
		} else {
			$.post(
				"${pageContext.request.contextPath}/yj/saveYj",
				{'yjTitle':yjTitle,'yjContent':yjContent},
				function(result){
					if(result.success){
						alert("发布成功！");
						resetValue();
					}else{
						alert("发布失败！");
					}
				}
				,"json");
		}
	}
	
	// 重置数据
	function resetValue(){
		$('#yjTitle').val("");
		KindEditor.html("#editor_id","");
	}
</script>
</head>
<body style="margin: 1px">
<div border="false"  class="easyui-panel" title="填写意见管理" style="padding: 10px">
 	<table cellspacing="20px">
   		<tr>
   			<td width="80px">标题：</td>
   			<td><input type="text" id="yjTitle" name="yjTitle" style="width: 400px;"/></td>
   		</tr>
   		<tr>
   			<td valign="top">内容：</td>
   			<td>
				<textarea id="editor_id" name="yjContent"></textarea>
   			</td>
   		</tr>
   		<tr>
   			<td></td>
   			<td>
   				<a href="javascript:submitData()" class="easyui-linkbutton" data-options="iconCls:'icon-submit'">提交</a>
   			</td>
   		</tr>
   	</table>
</div>

<script type="text/javascript">
var editor;
KindEditor.ready(function(K) {
        editor = K.create('#editor_id', {
                width: '400px',
                height:'250px',
                minWidth:'400px',
                minHeight:'200px',
                themeType:'simple',
                resizeType: 0,
                uploadJson : '${pageContext.request.contextPath}/rw/upLoad',
                //fileManagerJson : '../jsp/file_manager_json.jsp',
                allowFileUpload:true,
                filePostName:'file',
                allowImageRemote:false,
                items:[
                       'fontname', 'fontsize', '|', 'forecolor','hilitecolor','bold','italic','underline','removeformat', '|',
                       'justifyleft','justifycenter','justifyright','|','link','unlink'
                      ],
                afterBlur:function() {
                	this.sync();
                }
        });
});
</script>
</body>
</html>