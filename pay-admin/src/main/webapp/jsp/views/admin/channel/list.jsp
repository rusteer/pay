<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<div class="breadcrumbs" id="breadcrumbs">
	<ul class="breadcrumb">
		<li><i class="icon-home home-icon"></i>渠道列表 <span class="divider"> <i class="icon-angle-right arrow-icon"></i>
		</span></li>
		<li></li>
	</ul>
</div>
<div class="page-content">
	<div class="row-fluid">
		<div class="span12">
			<div class="table-toolbar">
				<form class="form-inline">
					<a href="/admin/${cmpName}/0" class="btn btn-small btn-success">新增渠道</a>
				</form>
			</div>
			<div class="hr"></div>
			<table cellpadding="0" cellspacing="0" border="0" class="table table-striped table-bordered table-condensed table-hover" id="example2">
				<thead>
					<tr>
			            <th>ID</th>
			            <th>数据</th>
			            <th>昵称</th>
			            <th>登录名</th>
			            <th>登录密码</th>
			            <th>激活个数</th>
			            <th>计费次数</th>
			            <th>信息费</th>
			            <th>热度</th>
			            <th>自动同步</th>
			            <th>扣量起始数</th>
			            <th>扣量百分比</th>			            
			            <th>其他信息</th>
					</tr>
				</thead>
				<tbody>
					<c:forEach var="entity" items="${list}">
						<tr class="odd gradeX">
							<td><a href="/admin/${cmpName}/${entity.id}">${entity.id}</a></td>
							<td><a href="/admin/stat/?channelId=${entity.id}">查看</a></td>
							<td>${entity.displayName}</td>
							<td>${entity.loginName}</td>
							<td>${entity.password}</td>
							<td>${entity.showActivateCount?"显示":"隐藏"}</td>
							<td>${entity.showPayCount?"显示":"隐藏"}</td>
							<td>${entity.showPayEarning?"显示":"隐藏"}</td>
							<td>${entity.hot}</td>
							<td>${entity.autoSync?"开启":"关闭"}</td>
							<td><c:if test="${entity.autoSync&&entity.discountStartCount>0}">${entity.discountStartCount}</c:if></td>
							<td><c:if test="${entity.autoSync&&entity.discountRate>0}">${entity.discountRate}%</c:if></td>
							<td>${entity.info}</td>
						</tr>
					</c:forEach>
				</tbody>
			</table>
		</div>
	</div>
</div>
<script>
	var refreshList = function() {
		var cpId = $("#cp").val();
		window.location.href = "/app/" + cpId;
	}
	$(function() {
		$("#cp").change(function() {
			refreshList();
		});
	});
</script>