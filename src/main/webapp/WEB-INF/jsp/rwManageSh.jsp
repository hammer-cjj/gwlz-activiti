<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>个人任务审核管理</title>
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
	
	//标题格式化显示
	function formatTitle(val,row){
		if (val.length > 20) {
			return "<a href='javascript:openDdDiglog("+row.id+",&quot;"+row.rwTitle+"&quot;)'>"+val.substring(0,21)+"...</a>";
		} else {
			return "<a href='javascript:openDdDiglog("+row.id+",&quot;"+row.rwTitle+"&quot;)'>"+val+"</a>";
		}
	}
	
	//搜索
	function searchRw(){
		$("#dg").datagrid('load',{
			"rwTitle":$("#s_title").val(),
			"rwZrId":$("#s_zr").combobox('getValue')
		});
	}
	
	
	//删除任务
	function deleteRw(){
		var selectedRows=$("#dg").datagrid("getSelections");
		if(selectedRows.length==0){
			 $.messager.alert("系统提示","请选择要删除的数据！");
			 return;
		 }
		 var id = selectedRows[0].id
		 $.messager.confirm("系统提示","您确定要删除这<font color=red>"+selectedRows.length+"</font>条数据吗？",function(r){
				if(r){
					$.post("${pageContext.request.contextPath}/rw/delete",{id:id},function(result){
						if(result.success){
							 $.messager.alert("系统提示","数据已成功删除！");
							 $("#dg").datagrid("reload");
						}else{
							$.messager.alert("系统提示","数据删除失败！");
						}
					},"json");
				} 
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
	function formatStartEndTime(val) {
		var d = new Date(val);
		return d.format("yyyy-MM-dd");
	}
	
	//任务发布日期的格式化
	function formatPubtime(val) {
		var d = new Date(val);
		return d.format("yyyy-MM-dd hh:mm:ss");
	}
	
	//操作栏
	function formateOperator(val, row) {
		return '<a href="#" onclick="completeSh('+row.id+')" class="rwDbBtn">完成审核 </a>';

	}
	
	//完成审核
	function completeSh(id) {
		$.ajax({
			type:"post",
			url: "${pageContext.request.contextPath}/rw/completeSh",
			data:{'id':id},
			dataType:"json",
			success:function(result) {
				if (result.success) {
					$.messager.alert("系统提示","完成审核成功！");
					$("#dg").datagrid("reload");
				} else {
					$.messager.alert("系统提示","您不是该任务的审核人，完成审核失败！");
				}
			}
		});
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
		$("#dd").dialog({
			title:val,
			modal:true,
			content:"<iframe scrolling='auto' frameborder='0' src='${pageContext.request.contextPath}/rw/findRw?flag=0&id="+id+"' style='width:100%; height:100%;'></iframe>"
		});
		$("#dd").dialog("open");
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
						$.messager.alert('Error',result.errMsg, 'error'); 
						$('#dlg').dialog('close');
						//parent.parent.window.location.href="login.jsp";
					}
				}	
			});
			
		}
	} 

	//打开修改任务对话框
	function openRwModifyTab(){
		 var selectedRows=$("#dg").datagrid("getSelections");
		 if(selectedRows.length!=1){
			 $.messager.alert("系统提示","请选择一个要修改的任务！");
			 return;
		 }
		 var row=selectedRows[0];
		 if (row.state == 0) { //未签收可以修改
			 $.ajax({
				 type:"POST",
				 url:"${pageContext.request.contextPath}/rw/findRwById?id="+row.id,
				 contentType: 'application/x-www-form-urlencoded;charset=utf-8',
				 dataType: "json",
	             success: function(result){
	                         if (result.success) {
	                        	 if (result.isEdit) { //可以修改任务
		                        	 $('#dlg').dialog('open').dialog('setTitle','修改任务');
		                        	 $('#fm').form('clear');
		                        	 url = '${pageContext.request.contextPath}/rw/saveGeRen';
		                        	 //任务Id
		                        	 $('#rwId').val(result.rw.id);
		                        	 //任务标题
		                        	 $('#rwTitle').val(row.rwTitle);
		                        	 //是否重要任务
		                        	 if (result.rw.zyFlag == 1) {
		                        		 $("#zyFlagStr").prop("checked", true); 
		                        	 }
		                        	 //任务日期
		                        	 $('#startTime').datebox('setValue', formatStartEndTime(result.rw.startTime));
		                        	 $('#endTime').datebox('setValue', formatStartEndTime(result.rw.endTime));
		                        	 //任务内容
		                        	 KindEditor.html("#editor_id", result.rw.rwContent);
		                        	 editor.focus();
		                        	 //状态，超期
		                        	 $('#cq').val(result.rw.cq);
		                        	 $('#state').val(result.rw.state);
		                     		 //加载责任人
		                    		 $("#zr").combobox({
		                    			url:'${pageContext.request.contextPath}/user/findUser',
		                    			valueField:'id',
		                    			textField:'realName',
		                    			required: true,
		                    			onLoadSuccess:function() { //默认选中
		                    				$('#zr').combobox('select',result.rw.rwZrId);
		                    			}
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
		                    			multiline:true,
		                    			onLoadSuccess:function() { //默认选中
		                    				if (result.rw.rwCyId.length > 0) {
		                    					var strArray = result.rw.rwCyId.split(",");
		                    					for (x in strArray) {
		                    						$('#cy').combobox('select', strArray[x]);
		                    					}
		                    				}
		                    			}
		                    		});
		                    		//加载任务等级
		                    		$("#rwDengJi").combobox({
		                    			panelHeight : 'auto',
		                    			valueField: 'label',
		                    			textField: 'value',
		                    			data:[{
		                    				label: '1',
		                    				value: '一般'
		                    			},{
		                    				label: '2',
		                    				value: '紧急'
		                    			},{
		                    				label: '3',
		                    				value: '特别紧急'
		                    			}],
		                    			onLoadSuccess:function() { //默认选中
		                    				$(this).combobox("select",result.rw.dengji);
		                    			}
		                    		});
		                    		//加载任务难度
		                    		$("#rwNandu").combobox({
		                    			panelHeight : 'auto',
		                    			valueField: 'label',
		                    			textField: 'value',
		                    			data:[{
		                    				label: '1',
		                    				value: '一般'
		                    			},{
		                    				label: '3',
		                    				value: '困难'
		                    			},{
		                    				label: '5',
		                    				value: '特别困难'
		                    			}],
		                    			onLoadSuccess:function() { //默认选中
		                    				$(this).combobox("select",result.rw.nandu);
		                    			}
		                    		});
		                    		//加载任务重要性
		                    		$("#rwZhongYao").combobox({
		                    			panelHeight : 'auto',
		                    			valueField: 'label',
		                    			textField: 'value',
		                    			data:[{
		                    				label: '1',
		                    				value: '一般'
		                    			},{
		                    				label: '2',
		                    				value: '重要'
		                    			},{
		                    				label: '3',
		                    				value: '特别重要'
		                    			}],
		                    			onLoadSuccess:function() { //默认选中
		                    				$(this).combobox("select",result.rw.zhongyao);
		                    			}
		                    		});
		                    		//任务附件
	                        	 } else {
	                        		 alert("该任务不是由你分配，不能修改");
	                        	 }
	                         } else {
	                        	 alert("查询出错!");
	                         }
	                      }
			 });
		 } else {
			 alert("任务已经签收,不能修改!");
		 }
		 ;
		 //$('#fm').form('load',row);
	}

	
	
</script>
</head>
<body style="margin: 1px">
<table id="dg" title="个人任务审核管理" 
   fitColumns="true" pagination="true" rownumbers="true" singleSelect="true" scrollbarSize="0"
   url="${pageContext.request.contextPath}/rw/listSh" fit="true" toolbar="#tb">
   <thead>
   	<tr>
   		<th field="rwTitle" width="50" align="center" formatter="formatTitle">标题</th>
   		<th field="realName" width="50" align="center" >责任人</th>
   		<th field="startTime" width="50" align="center" formatter="formatStartEndTime">开始日期</th>
   		<th field="endTime" width="50" align="center" formatter="formatStartEndTime">截至日期</th>
   		<th field="operator" width="50" align="center" formatter="formateOperator">操作</th>
   		<th field="id" hidden="true"></th>
   	</tr>
   </thead>
 </table>
 <div id="tb">
 	<div>
 		<a href="javascript:openRwModifyTab()" class="easyui-linkbutton" iconCls="icon-edit" plain="true">修改</a>
	 	<a href="javascript:deleteRw()" class="easyui-linkbutton" iconCls="icon-remove" plain="true">删除</a>
 	</div>                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                   
 	<div>
 		&nbsp;标题：&nbsp;<input type="text" id="s_title" size="20" onkeydown="if(event.keyCode==13) searchRw()"/>
 		&nbsp;责任人：&nbsp;<input editable="false" style="width:80px;" id="s_zr" name="rwZrId"  onkeydown="if(event.keyCode==13) searchRw()" />
 		<a href="javascript:searchRw()" class="easyui-linkbutton" iconCls="icon-search" plain="true">搜索</a>
 	</div>
 </div>
 
<!-- 查看任务详细开始 -->
<div id="dd" class="easyui-dialog" style="width:600px;height:430px;" closed="true">   

</div>  
<!-- 查看任务详细结束 -->
 
<!-- 新增任务对话框开始 --> 
<div id="dlg" class="easyui-dialog" style="width:600px;height:550px;padding: 10px 20px"
		closed="true" buttons="#dlg-buttons">
	<form id="fm" method="post" enctype="multipart/form-data">
		<table>
			<tr>
				<td style="width: 110px;">任务标题</td>
				<td colspan="3"><input style="width: 280px;" id="rwTitle" name="rwTitle" ></td>
			</tr>
			<tr><td colspan="4" height="10px"><input type="hidden" id="rwId" name="id" /></td></tr>
			<tr>
				<td style="width: 110px;">任务日期</td>
				<td style="width: 190px;">
					<input name="startTime" id="startTime" style="width:85px;" class="easyui-datebox" id="startTime" />
					-
					<input name="endTime" id="endTime" style="width:85px;" class="easyui-datebox" id="endTime" />
				</td>
				<td style="width: 110px;" align="center">任务等级</td>
				<td style="width: 190px;">
					<!-- <input style="width: 100px;" id="rwCategory" name="rwCategoryId" class="easyui-combobox" /> -->
					<input style="width: 100px;" id="rwDengJi" name="dengji" class="easyui-combobox" />
				</td>
			</tr>
			<tr><td colspan="4" height="10px"></td></tr>
			<tr>
				<td style="width: 110px;">任务难度</td>
				<td style="width: 190px;">
					<input style="width: 183px;" id="rwNandu" name="nandu" class="easyui-combobox" />
				</td>
				<td style="width: 110px;" align="center">任务重要性</td>
				<td style="width: 190px;">
					<input style="width: 100px;" id="rwZhongYao" name="zhongyao" class="easyui-combobox" />
				</td>
			</tr>
			<tr><td colspan="4" height="10px"></td></tr>
			<tr>
				<td style="width: 110px;">任务责任人</td>
				<td style="width: 190px;">
					<input editable="false" style="width: 183px;"  type="text" id="zr" name="rwZrId" class="easyui-combobox" value="请选择责任人" />
				</td>
				<td style="width: 110px;" align="center">任务分配人</td>
				<td style="width: 150px;">
					<input id="rwFpId" readonly="readonly" type="text" style="width:96px;"  />
					<input id="rwFpIdHidden" type="hidden" name="rwFpId" /> 
				</td>
			</tr>
			<tr><td colspan="4" height="10px"></td></tr>
			<tr>
				<td style="width: 110px;">任务参与人</td>
				<td colspan="3">
					<input style="width: 400px; height: 50px;" id="cy" name="rwCyId" class="easyui-combobox"  editable="false" />
				</td>
			</tr>
			<tr><td height="10px"></td></tr>
			<tr>
				<td style="width: 110px;">任务描述</td>
				<td colspan="3">
					<textarea id="editor_id" name="rwContent">
					</textarea>
				</td>
			</tr>
			<tr>
				<td colspan="4" height="10px">
					<input type="hidden" id="cq" name="cq" />
					<input type="hidden" id="state" name="state" />
				</td>
			</tr>
			<tr>
				<td style="width: 110px;">附件</td>
				<td colspan="3" >
					<input type="file" id="fj"  name="fj"  />
				</td>
			</tr>	
		</table>
	</form>
</div>
<div id="dlg-buttons">
	<a href="#" class="easyui-linkbutton" iconCls="icon-ok" onclick="saveRw()">保存</a>
	<a href="#" class="easyui-linkbutton" iconCls="icon-cancel" onclick="javascript:$('#dlg').dialog('close')">取消</a>
</div>
<!-- 新增任务对话框结束 --> 

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

$("#dg").datagrid({
	onLoadSuccess:function(result){
		$(".rwDbBtn").linkbutton({text:"完成审核", plain:true});
		if (result.errFlag) { //session失效跳转至登陆界面
			parent.window.location.href="login.jsp";
		}
	}
});


//搜索栏加载责任人
$("#s_zr").combobox({
	url:'${pageContext.request.contextPath}/user/findUser',
	valueField:'id',
	textField:'realName',
	select:'',
	loadFilter:function(data){
		return [{'realName':'全部','id':-1,'selected':true}].concat(data);
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