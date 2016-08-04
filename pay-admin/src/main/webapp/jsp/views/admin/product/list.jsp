<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<div class="breadcrumbs" id="breadcrumbs">
	<ul class="breadcrumb">
		<li><i class="icon-home home-icon"></i>产品列表 <span class="divider"> <i class="icon-angle-right arrow-icon"></i>
		</span></li>
		<li></li>
	</ul>
</div>
<div class="page-content">
	<div class="row-fluid">
		<div class="span12">
			<div class="table-toolbar">
				<form class="form-inline">
					<a href="/admin/${cmpName}/0" class="btn btn-small btn-success">新增产品</a>
				</form>
			</div>
			<div class="hr"></div>
			<table cellpadding="0" cellspacing="0" border="0" class="table table-striped table-bordered table-condensed table-hover" id="example2">
				<thead>
					<tr>
			            <th>ID</th>
			            <th>数据</th>
			            <th>名称</th>
			            <th>项目名称</th>
			            <th>主包名</th>
			            <th>热度</th>
			            <th>路径</th>
			            <th>备注</th>
					</tr>
				</thead>
				<tbody>
					<c:forEach var="entity" items="${list}">
						<tr class="odd gradeX">
							<td><a href="/admin/${cmpName}/${entity.id}">${entity.id}</a></td>
							<td><a href="/admin/stat/?productId=${entity.id}">查看</a></td>
							<td>${entity.name}</td>
							<td>${entity.projectName}</td>
							<td>${entity.packageName}</td>
							<td>${entity.hot}</td>
							<td>${entity.codePath}</td>
							<td>${entity.description}</td>
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