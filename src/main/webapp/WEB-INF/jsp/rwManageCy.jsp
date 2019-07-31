<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>任务管理 - 我参与的任务</title>
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
	//任务类型格式化显示
	function formatRwCategory(val,row){
		var returnVal = "";
		if (val == 1) {
			returnVal = "常规任务";
		} else if (val == 2) {
			returnVal = "重要工作";
		} else if (val == 3) {
			returnVal = "其他";
		}
		return returnVal;
	}
	
	function formatTitle(val,row){
		if (row.zyFlag == 1) {
			return "<a href='javascript:openDdDiglog("+row.id+",&quot;"+row.rwTitle+"&quot;)'><img style='vertical-align:middle;' width='13px' height='13px' src='static/images/zy.gif'/>"+val+"</a>";
		} else {
			return "<a href='javascript:openDdDiglog("+row.id+",&quot;"+row.rwTitle+"&quot;)'>"+val+"</a>";
		}
	}
	
	function searchRw(){
		$("#dg").datagrid('load',{
			"rwTitle":$("#s_title").val(),
			"state":$("#s_state").combobox('getValue'),
			"rwZrId":$("#s_zr").combobox('getValue'),
			"cq":$("#s_cq").combobox('getValue')
		});
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
	function formatStartEndTime(val,row) {
		var d = new Date(val);
		return d.format("yyyy-MM-dd");
	}
	
	//任务发布日期的格式化
	function formatPubtime(val, row) {
		var d = new Date(val);
		return d.format("yyyy-MM-dd hh:mm:ss");
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
			return "<font color='red'>未完成</font>";
		} else if (val == 1) {
			return "提前完成";
		} else if (val == 2) {
			return "按时完成";
		} else if (val == 3) {
			return "超期完成";
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
	
	//操作栏
	/* function formateOperator(val, row) {
		return '<a href="#" class="rwfpBtn">任务分配 </a>&nbsp;&nbsp;<a class="tjpsBtn">添加批示</a>';

	} */
	
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
			content:"<iframe scrolling='auto' frameborder='0' src='${pageContext.request.contextPath}/rw/findRw?flag=3&id="+id+"' style='width:100%; height:100%;'></iframe>"
		});
		$("#dd").dialog("open");
	}
	
	//新增任务
	function newRw() {
		$('#dlg').dialog('open').dialog('setTitle','添加任务');
		$('#fm').form('clear');
		KindEditor.instances[0].html("");
		url = '${pageContext.request.contextPath}/rw/save';
		//加载责任人
		$("#zr").combobox({
			url:'${pageContext.request.contextPath}/user/findUser',
			valueField:'id',
			textField:'realName',
			required: true
		});
		//任务分配人
		$('#rwFpId').val('${currentUser.realName }');
		$('#rwFpIdHidden').val('${currentUser.id }');
		//加载参与人
		$("#cy").combobox({
			url:'${pageContext.request.contextPath}/user/findCy',
			valueField:'id',
			textField:'realName',
			multiple:true,
			multiline:true
		});
		//加载任务类别
		$("#rwCategory").combobox({
			panelHeight : 'auto',
			valueField: 'label',
			textField: 'value',
			data:[{
				label: '1',
				value: '常规任务'
			},{
				label: '2',
				value: '重要工作'
			},{
				label: '3',
				value: '其他'
			}],
			onLoadSuccess:function() {
				var val = $(this).combobox("getData");
				if (val != null) {
					$(this).combobox("select",val[0].label);
				}
			}
		});
	}
	
	//保存任务
	function saveRw() {
		//获取表单数据
		var rwTitle = $("#rwTitle").val();
		//var rwCategory = $("#rwCategory").val();
		var rwContent = $("#editor_id").val();
		//获取接受任务的用户Id
		var rwCyId = $("#cy").combobox('getValues');
		var startTime = $("#startTime").datebox("getValue");
		var endTime = $("#endTime").datebox("getValue");
		//对表单数据进行判断验证
		if (rwTitle == null || rwTitle == '') {
			alert("请输入任务标题!");
		} else if (rwContent == null || rwContent == '') {
			alert("请输入任务内容!")
		} else if (rwCyId == null || rwCyId == '') {
			alert("请分配任务参与人!")
		} else if (startTime == null || startTime == '' ||
				endTime == null || endTime == '') {
				alert("请输入任务开始日期和截止日期");
		} else {
			$("#fm").form("submit",{ 
				url:url,
				success:function(result) {
					var result = eval('('+result+')');
					if (result.success){
						$.messager.alert("系统提示","任务发布成功！");
						$('#dlg').dialog('close');		// close the dialog
						$('#dg').datagrid('reload');	// reload the user data
					} else {
						parent.parent.window.location.href="login.jsp";
					}
				}	
			});
			
		}
	} 
	
</script>
</head>
<body style="margin: 1px">
<table id="dg" title="任务管理" 
   fitColumns="true" pagination="true" rownumbers="true" singleSelect="true" scrollbarSize="0"
   url="${pageContext.request.contextPath}/rw/listCy" fit="true" toolbar="#tb">
   <thead>
   	<tr>
   		<!-- <th field="cb" checkbox="true" align="center"></th> -->
   		<th field="rwCategoryId" width="50" align="center" formatter="formatRwCategory">任务类型</th>
   		<th field="rwTitle" width="50" align="center" formatter="formatTitle">标题</th>
   		<th field="ztFlag" hidden="true" id="zyrw"></th>
   		<th field="realName" width="50" align="center" >责任人</th>
   		<!-- <th field="rwContent" width="100" align="center" >内容</th>
   		<th field="rwFj" width="100" align="center" formatter="formatFjUrl">附件</th> -->
   		<th field="startTime" width="50" align="center" formatter="formatStartEndTime">开始日期</th>
   		<th field="endTime" width="50" align="center" formatter="formatStartEndTime">截至日期</th>
   		<th field="state" width="50" align="center" formatter="formateRwstate">状态</th>
   		<th field="cq" align="center" formatter="formateRwCq">是否超期</th>
   		<th field="completeSX" width="50" align="center" formatter="formateRwRx">完成时效</th>
   		<th field="pubTime" width="50" align="center" formatter="formatPubtime">发布日期</th>
   		<!-- <th field="operator" width="50" align="center" formatter="formateOperator">操作</th> -->
   	</tr>
   </thead>
 </table>
 <div id="tb">
 	<div>
 		<!-- <a href="javascript:newRw()" class="easyui-linkbutton" iconCls="icon-add" plain="true">新增</a> -->
 		<!-- <a href="javascript:openBlogModifyTab()" class="easyui-linkbutton" iconCls="icon-edit" plain="true">修改</a> -->
 		<!-- <a href="javascript:deleteRw()" class="easyui-linkbutton" iconCls="icon-remove" plain="true">删除</a> -->
 	</div>                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                   
 	<div>
 		&nbsp;标题：&nbsp;<input type="text" id="s_title" size="20" onkeydown="if(event.keyCode==13) searchRw()"/>
 		&nbsp;责任人：&nbsp;<input editable="false" style="width:80px;" id="s_zr" name="rwZrId"  onkeydown="if(event.keyCode==13) searchRw()" />
 		&nbsp;状态：&nbsp;<select style="width:80px;" panelHeight="auto" editable="false" class="easyui-combobox" id="s_state" onkeydown="if(event.keyCode==13) searchRw()">
 							<option value="-1">全部</option>
 							<option value="0">未签收</option>
 							<option value="1">进行中</option>
 							<option value="2">已完成</option>
 						</select>
 		&nbsp;超期：&nbsp;<select style="width:70px;" panelHeight="auto" editable="false" class="easyui-combobox" id="s_cq" onkeydown="if(event.keyCode==13) searchRw()">
 							<option value="-1">全部</option>
 							<option value="0">未超期</option>
 							<option value="1">已超期</option>
 						</select>
 		<a href="javascript:searchRw()" class="easyui-linkbutton" iconCls="icon-search" plain="true">搜索</a>
 	</div>
 </div>
 
<!-- 查看任务详细开始 -->
<div id="dd" class="easyui-dialog" style="width:600px;height:430px;" 
	data-options="onBeforeClose:function(){$('#dg').datagrid('reload')}" closed="true">   

</div>  
<!-- 查看任务详细结束 -->

<!-- 查看反馈日志开始 -->

<div id="fk-dlg" class="easyui-dialog" title="查看反馈日志" style="width:800px;height:500px;"
    data-options="closed:true">
	<table id="fk-dg" class="easyui-datagrid" fit="true" scrollbarSize="0" style="width: 100%;height: 100%;"
	   fitColumns="true" pagination="true" rownumbers="true" singleSelect="true" nowrap="false"
	    fit="true" >
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
/* 
$("#dg").datagrid({
	onLoadSuccess:function(data){
		$(".rwfpBtn").linkbutton({text:"任务分配", plain:true});
		$(".tjpsBtn").linkbutton({text:"添加批示", plain:true});
	}
});
 */
 
//搜索栏加载责任人
 $("#s_zr").combobox({
 	url:'${pageContext.request.contextPath}/user/findSearchZrUser',
 	valueField:'id',
 	textField:'realName',
 	select:'',
 	loadFilter:function(data){
 		return [{'realName':'全部','id':-1,'selected':true}].concat(data);
 	}
 }); 
 
//session失效跳转至登陆界面
 $('#dg').datagrid({
 	onLoadSuccess:function(result) {
 		if (result.errFlag) {
 			parent.window.location.href="login.jsp";
 		}
 	}
 });  
 
var editor;
KindEditor.ready(function(K) {
        editor = K.create('#editor_id', {
                width: '400px',
                height:'200px',
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