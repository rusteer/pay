<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<div class="breadcrumbs" id="breadcrumbs">
	<ul class="breadcrumb">
		<li><i class="icon-home home-icon"></i><a href="/admin/${cmpName}/">产品列表</a> <span class="divider"> <i class="icon-angle-right arrow-icon"></i>
		</span></li>
		<li>${entity.id>0?entity.name:"新增产品"}</li>
	</ul>
</div>
<c:if test="${param.saveSuccess==true}">
	<div class="alert alert-block alert-success">
		<button type="button" class="close" data-dismiss="alert">
			<i class="icon-remove"></i>
		</button>
		<i class="icon-ok green"></i> 保存成功!
	</div>
</c:if>
<c:if test="${saveSuccess==false}">
	<div class="alert alert-block alert-error">
		<button type="button" class="close" data-dismiss="alert">
			<i class="icon-remove"></i>
		</button>
		<i class="icon-remove"></i> 保存失败! ${errorMessage}
	</div>
</c:if>
<hr/>
<form class="form-horizontal" id="bizForm" method="post" action="/admin/${cmpName}/" AUTOCOMPLETE="OFF" >
	<input type="hidden" name="id" value="${entity.id}"/>
	<div class="control-group">
		<label class="control-label" for="name">名称</label>
		<div class="controls">
			<input class="span12" type="text" id="name" placeholder="昵称" name="name" value="${entity.name}"/>
		</div>
	</div>
	<div class="control-group">
		<label class="control-label" for="projectName">项目名称</label>
		<div class="controls">
			<input class="span12" type="text" id="projectName" placeholder="项目名称" name="projectName" value="${entity.projectName}"/>
		</div>
	</div>	
	<div class="control-group">
		<label class="control-label" for="packageName">主包名</label>
		<div class="controls">
			<input class="span12" type="text" id="packageName" placeholder="简称" name="packageName" value="${entity.packageName}"/>
		</div>
	</div>
	<div class="control-group">
		<label class="control-label" for="codePath">路径</label>
		<div class="controls">
			<input class="span12" type="text" id="codePath" placeholder="路径" name="codePath" value="${entity.codePath}"/>
		</div>
	</div>	
	<div class="control-group">
		<label class="control-label" for="description">备注</label>
		<div class="controls">
			<input class="span12" type="text" id="description" placeholder="备注" name="description" value="${entity.description}"/>
		</div>
	</div>
	<div class="control-group">
		<label class="control-label" for="hot">热度</label>
		<div class="controls">
			<select class="span12" id="hot" name="hot">
				<c:forEach var="x" begin="0" end="10" step="1">
					<option value="${x}"  ${entity.hot==x?"selected":""} >${x}</option>
				</c:forEach>
			</select>
		</div>
	</div>
	<div class="control-group">
		<label class="control-label" for="payItems">计费点信息</label>
		<div class="controls">
			<textarea class="span12" id="payItems" placeholder="计费点信息"  name="payItems" rows="15">${payItems}</textarea>
		</div>
	</div>									 	
	<div class="form-actions">
		<button class="btn btn-info" type="button" onclick="submitForm(this)">
			<i class="icon-ok bigger-110"></i> 保存
		</button>
		&nbsp; &nbsp; &nbsp;
		<button class="btn" type="button" onclick="goBack()">
			<i class="icon-backward bigger-110"></i> 返回
		</button>
	</div>
</form>
<script type="text/javascript">
	var goBack = function() {
		window.history.back();
	}
	var submitForm = function(btn) {
		btn.form.submit();
	}
</script>