<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title></title>

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
	//任务签收
	function rwSign(id) {
		$.messager.confirm('系统提示','您确认想要签收此任务吗？',function(r){    
		    if (r){    
		    	$.post("${pageContext.request.contextPath}/rw/sign",{id:id },function(result){
					if(result.success){
						$.messager.alert("系统提示","签收成功！");
						
						parent.$('#dd').dialog('refresh');
					}else{
						$.messager.alert("系统提示","签收失败！");
						//window.top.location.href="${pageContext.request.contextPath}/login.jsp";
					}
				},"json");
		    }    
		});  
	}
	
	//打开任务完成对话框
	function rwComplete(id) {
		$("#fk-add").dialog("open").dialog("setTitle","任务完成");
		$('#fk-fm').form('clear');
		var rwId = ${rwDetail.id};
		$('#rwId').val(rwId);
		//任务完成置为1
		$('#fkWcFlag').val("1");
		$('#fk-ps').html("反馈内容");
	}
	
	//打开任务反馈对话框
	function rwFk() {
		$("#fk-add").dialog("open").dialog("setTitle","任务反馈");
		$('#fk-fm').form('clear');
		var rwId = ${rwDetail.id};
		$('#rwId').val(rwId);
		//任务反馈置为2
		$('#fkWcFlag').val("2");
		$('#fk-ps').html("反馈内容");
	}
	
	//打开任务批示对话框
	function rwPs() {
		$("#fk-add").dialog("open").dialog("setTitle","任务批示");
		$('#fk-fm').form('clear');
		var rwId = ${rwDetail.id};
		$('#rwId').val(rwId);
		//任务批示置为2
		$('#fkWcFlag').val("2");
		$('#fk-ps').html("批示内容");
		//fix bug。添加批示关闭后，再添加批示，完成情况增加一行。
		$('#fk-tr1').remove();
		$('#fk-tr2').remove();
		var qkTr = "<tr id='fk-tr1'><td width='80px' height='10px'>完成情况</td><td><input type='radio' name='completeQK' value='3' />较好";
		qkTr += " <input type='radio' name='completeQK' value='2' />一般  <input type='radio' name='completeQK' value='1' />部分完成";
		qkTr += " <input type='radio' name='completeQK' value='0' />未完成";
		qkTr += "</td></tr><tr id='fk-tr2'><td colspan='2' height='10px'></td></tr>";	
		$('#qk').after(qkTr);
	}
	
	//保存任务，批示反馈
	function saveRwFk() {
		var fkWcFlag = $('#fkWcFlag').val();
		if (fkWcFlag == 1) { //任务完成
			urlStr = '${pageContext.request.contextPath}/rw/complete';
		} else if (fkWcFlag == 2) { //任务反馈，批示
			urlStr = '${pageContext.request.contextPath}/rwFk/saveRwFk';
		}
		var content = $("#fk-content").val();
		if (content == null || content == "") {
			alert("请输入反馈内容!");
		} else {
			$("#fk-fm").form("submit",{
				url:urlStr,
				success:function(result){
					var result = eval('('+result+')');
					if (result.success) {
						$.messager.alert("系统提示","保存成功！");
						$('#fk-add').dialog('close');
						parent.$('#dd').dialog('refresh');
					} else {
						$.messager.alert("系统提示","保存失败！ "+result.errMsg, "error", function(){
							$('#fk-add').dialog('close');
							parent.$('#dd').dialog('close');
						});
					}
				}
			});
		}
	}
	
	//打开反馈日志列表
	function openFkList(rwId) {
		parent.$('#dd').dialog('close');
		parent.$("#fk-dg").datagrid({
			url:"${pageContext.request.contextPath}/rwFk/listRwFk?rwId="+rwId
		});
		parent.$("#fk-dlg").dialog("open");
	}
	
	//关闭反馈日志触发事件
	parent.$("#fk-dlg").dialog({
		onClose:function(){
			parent.$('#dd').dialog('open');
		}
	});
	
	//任务参与人分配
	function rwCyFp() {
		$("#cy").dialog("open").dialog("setTitle","任务参与人分配");
		//加载参与人
		$("#cyUser").combobox({
			url:'${pageContext.request.contextPath}/user/findCy',
			valueField:'id',
			textField:'realName',
			multiple:true,
			multiline:true,
			required:true
		});
	}
	
	//保存任务参与人
	function saveRwCy() {
		$("#cy-fm").form("submit",{
			url:"${pageContext.request.contextPath}/rw/addRwCy",
			success:function(result){
				var result = eval('('+result+')');
				if (result.success) {
					$.messager.alert("系统提示","保存成功！");
					$('#cy').dialog('close');
					//windows.location.href="${pageContext.request.contextPath}/rw/findRw?flag=1&id="+result.id;
					//parent.$('#dd').dialog('close')
					parent.$('#dd').dialog('refresh');
				} else {
					$.messager.alert("系统提示","保存失败！");
				}
			}
		});
		
		//$('#cy').dialog('close');
	}
	
	$(function(){
	    $(":checkbox").click(function(){
			//设置当前选中checkbox的状态为checked
	    	$(this).attr("checked",true);
	    	$(this).siblings().attr("checked",false); //设置当前选中的checkbox同级(兄弟级)其他checkbox状态为未选中
		});
	});
	
	//文件预览
	function openYulan(url) {
		if (url != null && url != '') {
			url = "http://127.0.0.1:8080"+url;
			window.open('http://127.0.0.1:8012/onlinePreview?url='+encodeURIComponent(url),'_blank');
		}
	}
</script>
<style type="text/css">
	body {
		font-size: 12px;
		color:#444;
	}
	.td1 {
		border-top: 1px solid #ddd;
		border-bottom: 1px solid #ddd;
	}
	
</style>
</head>
<body>
<table style="width: 100%">
	<tr>
		<td colspan="4" class="td1">
			<c:if test="${currentUser == null }">
				<% response.sendRedirect(request.getContextPath()+"/sessionError"); %>
			</c:if>
			<c:choose>
				<c:when test="${currentUser.roleId == 1 or currentUser.roleId == 0 }">
					<a href="#" onclick="rwPs()" class="easyui-linkbutton" >添加批示</a>
				</c:when>
				<%-- 副处长权限，具有分配、责任、参与页面 --%>
				<c:when test="${currentUser.roleId == 2}">
					<%-- 任务分配页面 --%>
					<c:if test="${flag == 1 }">
						<c:if test="${rwDetail.cyUserList == null }">
							<a href="#" onclick="rwCyFp()" class="easyui-linkbutton" >人员分配</a><!-- <span>&gt;</span> -->
						</c:if>
						<a href="#" onclick="rwPs()" class="easyui-linkbutton" >添加批示</a>
					</c:if>
					<%-- 任务责任页面 --%>
					<c:if test="${flag == 2 }">
						<c:if test="${rwDetail.state == 0 }">
							<a href="#" onclick="rwSign(${rwDetail.id})" class="easyui-linkbutton" >任务签收</a><!-- <span>&gt;</span> -->
						</c:if>
						<c:if test="${rwDetail.cyUserList == null }">
							<a href="#" onclick="rwCyFp()" class="easyui-linkbutton" >人员分配</a><!-- <span>&gt;</span> -->
						</c:if>
						<c:choose>
							<c:when test="${rwDetail.state == 1 }">
								<a href="#" onclick="rwFk()" class="easyui-linkbutton" >任务反馈</a><!-- <span>&gt;</span> -->
								<a href="#" onclick="rwComplete(${rwDetail.id})" class="easyui-linkbutton">任务完成</a>
							</c:when>
							<c:otherwise>
								<a href="#" onclick="rwFk()" class="easyui-linkbutton" >任务反馈</a>
							</c:otherwise>
						</c:choose>
					</c:if>
					<%-- 任务参与页面 --%>
					<c:if test="${flag == 3 }">
						<c:choose>
							<c:when test="${rwDetail.state == 1 }">
								<a href="#" onclick="rwFk()" class="easyui-linkbutton" >任务反馈</a><!-- <span>&gt;</span> -->
								<a href="#" onclick="rwComplete(${rwDetail.id})" class="easyui-linkbutton" >任务完成</a>
							</c:when>
							<c:otherwise>
								<a href="#" onclick="rwFk()" class="easyui-linkbutton">任务反馈</a>
							</c:otherwise>
						</c:choose>
					</c:if>
				</c:when>
				<%-- 其他人权限，具有责任、参与页面 --%>
				<c:otherwise>
					<%-- 任务责任页面 --%>
					<c:if test="${flag == 2 }">
						<c:if test="${ rwDetail.state == 0 }">
							<a href="#" onclick="rwSign(${rwDetail.id})" class="easyui-linkbutton" >任务签收</a>
							<a href="#" onclick="rwFk()" class="easyui-linkbutton" >任务反馈</a><!-- <span>&gt;</span> -->
							<a href="#" onclick="rwComplete(${rwDetail.id})" class="easyui-linkbutton" >任务完成</a>
						</c:if>
						<c:if test="${ rwDetail.state == 1 }">
							<a href="#" onclick="rwFk()" class="easyui-linkbutton" >任务反馈</a><!-- <span>&gt;</span> -->
							<a href="#" onclick="rwComplete(${rwDetail.id})" class="easyui-linkbutton" >任务完成</a>
						</c:if>
						<c:if test="${ rwDetail.state == 2 }">
							<a href="#" onclick="rwFk()" class="easyui-linkbutton" >任务反馈</a>
						</c:if>
					</c:if>
					<%-- 任务参与页面 --%>
					<c:if test="${flag == 3 }">
						<c:choose>
							<c:when test="${ rwDetail.state == 1 }">
								<a href="#" onclick="rwFk()" class="easyui-linkbutton" >任务反馈</a><!-- <span>&gt;</span> -->
								<a href="#" onclick="rwComplete(${rwDetail.id})" class="easyui-linkbutton" >任务完成</a>
							</c:when>
							<c:otherwise>
								<a href="#" onclick="rwFk()" class="easyui-linkbutton" >任务反馈</a>
							</c:otherwise>
						</c:choose>
					</c:if>
				</c:otherwise>
			</c:choose>
		</td>
	</tr>
	<tr>
		<td colspan="4" style="background-color:#fffaf4;height:30px;valign:middle;">
			<span>${rwDetail.rwTitle }</span>
		</td>
	</tr>
	<tr>
		<td width="15%">分配人</td>
		<td width="35%" align="left">${rwDetail.fpName }</td>
		<td width="15%">任务等级</td>
		<td width="35%" align="left">
			<c:choose>
				<c:when test="${rwDetail.dengji == 1 }">
					一般
				</c:when>
				<c:when test="${rwDetail.dengji == 2 }">
					紧急
				</c:when>
				<c:when test="${rwDetail.dengji == 3 }">
					特别紧急
				</c:when>
			</c:choose>
		</td>
	</tr>
	<tr><td colspan="4" height="3px"></td></tr>
	<tr>
		<td width="15%">责任人</td>
		<td width="35%" align="left">${rwDetail.zrName }</td>
		<td width="15%">参与人</td>
		<td width="35%" align="left">
			<c:forEach items="${rwDetail.cyUserList }" var="cyUser">
				${cyUser.realName }&nbsp;
			</c:forEach>
		</td>
	</tr>
	<tr><td colspan="4" height="3px"></td></tr>
	<tr>
		<td width="15%">任务日期</td>
		<td>
			${rwDetail.startTimeStr } - ${rwDetail.endTimeStr }
		</td>
		<td>完成日期</td>
		<td>${rwDetail.completeDate }</td>
	</tr>
	<tr><td colspan="4" height="3px"></td></tr>
	<tr>
		<td width="15%">完成时效</td>
		<td width="35%">
			<c:if test="${rwDetail.completeSX == 0 }">未完成</c:if>
			<c:if test="${rwDetail.completeSX == 3 }">提前完成</c:if>
			<c:if test="${rwDetail.completeSX == 2 }">按时完成</c:if>
			<c:if test="${rwDetail.completeSX == 1 }">超期完成</c:if>
		</td>
		<td width="15%">完成情况</td>
		<td width="35%">
			<!-- 3：较好；2：一般 ；1:部分完成； 0： 未完成-->
			<c:if test="${rwDetail.completeQK == 0 }">未完成</c:if>
			<c:if test="${rwDetail.completeQK == 1 }">部分完成</c:if>
			<c:if test="${rwDetail.completeQK == 2 }">一般</c:if>
			<c:if test="${rwDetail.completeQK == 3 }">较好</c:if>
		</td>
	</tr>
	<tr><td colspan="4" height="3px"></td></tr>
	<tr>
		<td width="15%">任务难度</td>
		<td width="35%">
			<c:if test="${rwDetail.nandu == 1 }">一般</c:if>
			<c:if test="${rwDetail.nandu == 3 }">困难</c:if>
			<c:if test="${rwDetail.nandu == 5 }">特别困难</c:if>
		</td>
		<td width="15%">任务重要性</td>
		<td width="35%">
			<c:if test="${rwDetail.zhongyao == 1 }">一般</c:if>
			<c:if test="${rwDetail.zhongyao == 2 }">重要</c:if>
			<c:if test="${rwDetail.zhongyao == 3 }">特别重要</c:if>
		</td>
	</tr>
	<tr><td colspan="4" height="3px"></td></tr>
	<tr height="140px;">
		<td width="15%" valign="top">任务内容</td>
		<td colspan="3" valign="top">
			${rwDetail.rwContent }
		</td>
	</tr>
	<tr><td colspan="4" height="3px"></td></tr>
	<tr>
		<td width="15%">任务附件</td>
		<td colspan="3">
			<a target="_blank" href="${pageContext.request.contextPath}/${rwDetail.rwFj }">${rwDetail.rwFjName }</a>
			<c:if test="${rwDetail.rwFjName != null }">
				&nbsp;&nbsp;&nbsp;&nbsp;<a href="#" onclick="openYulan('${pageContext.request.contextPath}/${rwDetail.rwFj }')" style="text-decoration: none;color:#0080ff;">文件预览</a>
			</c:if>
		</td>
	</tr>
	<tr><td colspan="4" height="3px"></td></tr>
	<tr>
		<td colspan="4" style="background-color:#fffaf4;height:30px;valign:middle;">
			<!-- <div style="float: left;">反馈</div> -->
			<div style="float: right;"><a href="#" onclick="openFkList(${rwDetail.id})" style="text-decoration: none;color:#0080ff;">任务日志</a></div>
		</td>
	</tr>
</table>


<!-- 添加反馈，批示，完成开始 -->
<div id="fk-add" class="easyui-dialog" title="添加任务反馈" style="width:400px;height:280px;padding:20px 20px;"
    data-options="closed:true,modal:true">
    <form id="fk-fm" method="post" enctype="multipart/form-data">
    <table>
    	<tr>
    		<td width="80px" id="fk-ps"></td>
    		<td>
    			<textarea style="resize:none;" id="fk-content" name="content" rows="5" cols="35"></textarea>
    			<input type="hidden" id="rwId" name="rwId" value="" />
    		</td>
    	</tr>
    	<tr id="qk"><td colspan="2" height="10px" ></td></tr>
    	<tr>
    		<td width="80px">附件</td>
    		<td>
    			<input type="file"  name="fj"  />
    		</td>
    	</tr>
    	<tr><td width="80px"></td><td>附件最大50M</td></tr>
    	<!-- 任务反馈还是完成的标志位 -->
    	<tr><td colspan="2" height="10px"><input type="hidden" id="fkWcFlag"  value="" /></td></tr>
    	<tr>
    		<td colspan="2">
    			<div id="cy-buttons" align="center">
					<a href="#" class="easyui-linkbutton" iconCls="icon-ok" onclick="saveRwFk()">保存</a>
					&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
					<a href="#" class="easyui-linkbutton" iconCls="icon-cancel" onclick="javascript:$('#fk-add').dialog('close')">取消</a>
				</div>
    		</td>
    	</tr>
    </table>
    </form>
</div>
<!-- 添加反馈，批示，完成结束 -->

<!-- 添加任务参与人开始 -->
<div id="cy" class="easyui-dialog" title="任务参与人分配" style="width:400px;height:200px;padding:20px 20px;"
    data-options="closed:true,modal:true">
    <form id="cy-fm" method="post">
    <table>
    	<tr>
    		<td width="100px">任务参与人</td>
    		<td width="300px">
    			<input style="width: 220px; height: 50px;" id="cyUser" name="rwCyId" class="easyui-combobox" editable="false"/>
    			<input type="hidden" name="id" value="${rwDetail.id}" />
    		</td>
    	</tr>
    	<tr><td colspan="2" height="10px"></td></tr>
    	<tr>
    		<td colspan="2">
    			<div id="cy-buttons" align="center">
					<a href="#" class="easyui-linkbutton" iconCls="icon-ok" onclick="saveRwCy()">保存</a>
					&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
					<a href="#" class="easyui-linkbutton" iconCls="icon-cancel" onclick="javascript:$('#cy').dialog('close')">取消</a>
				</div>
    		</td>
    	</tr>
    </table>
    </form>
</div>
<!-- 添加任务参与人结束 -->
</body>
</html>