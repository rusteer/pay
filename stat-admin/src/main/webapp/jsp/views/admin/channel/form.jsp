<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<div class="breadcrumbs" id="breadcrumbs">
	<ul class="breadcrumb">
		<li><i class="icon-home home-icon"></i><a href="/admin/${cmpName}/">渠道列表</a> <span class="divider"> <i class="icon-angle-right arrow-icon"></i>
		</span></li>
		<li>${entity.id>0?entity.displayName:"新增渠道"}</li>
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
		<label class="control-label" for="displayName">昵称</label>
		<div class="controls">
			<input type="text" id="displayName"  name="displayName"  value="${entity.displayName}" />
		</div>
	</div>	
	<div class="control-group">
		<label class="control-label" for="loginName">登录名</label>
		<div class="controls">
			<input type="text" id="loginName" name="loginName" value="${entity.loginName}" />
		</div>
	</div>
	<div class="control-group">
		<label class="control-label" for="password">密码</label>
		<div class="controls">
			<input type="text" id="password" name="password" value="${entity.password}" />
		</div>
	</div>
	<div class="control-group">
		<label class="control-label" for="showActivateCount">激活个数</label>
		<div class="controls">
			<select id="showActivateCount" name="showActivateCount">
				<option value="0">隐藏</option>
				<option value="1" ${entity.showActivateCount?"selected":""}>显示</option>
			</select>
		</div>
	</div>
	<div class="control-group">
		<label class="control-label" for="showPayCount">计费次数</label>
		<div class="controls">
			<select id="showPayCount" name="showPayCount">
				<option value="0">隐藏</option>
				<option value="1" ${entity.showPayCount?"selected":""}>显示</option>
			</select>
		</div>
	</div>
	<div class="control-group">
		<label class="control-label" for="showPayEarning">信息费</label>
		<div class="controls">
			<select id="showPayEarning" name="showPayEarning">
				<option value="0">隐藏</option>
				<option value="1" ${entity.showPayEarning?"selected":""}>显示</option>
			</select>
		</div>
	</div>	
	<div class="control-group">
		<label class="control-label" for="hot">热度</label>
		<div class="controls">
			<select id="hot" name="hot">
				<c:forEach var="x" begin="0" end="10" step="1">
					<option value="${x}"  ${entity.hot==x?"selected":""} >${x}</option>
				</c:forEach>
			</select>
		</div>
	</div>	
	<div class="control-group">
		<label class="control-label" for="autoSync">自动同步</label>
		<div class="controls">
			<select id="autoSync" name="autoSync">
				<option value="0">关闭</option>
				<option value="1" ${entity.autoSync?"selected":""}>开启</option>
			</select>
		</div>
	</div>	
	<div class="control-group">
		<label class="control-label" for="discountRate">扣量百分比</label>
		<div class="controls">
			<input type="text" id="discountRate" name="discountRate" value="${entity.discountRate}" />
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