<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<div class="breadcrumbs" id="breadcrumbs">
	<ul class="breadcrumb">
		<li><i class="icon-home home-icon"></i><a href="/admin/${cmpName}/">渠道列表</a> <span class="divider"> <i class="icon-angle-right arrow-icon"></i>
		</span></li>
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
	<input type="hidden" name="packageId"  value="${entity.packageId}" />
	<input type="hidden" name="statDate"  value="${entity.statDate}" />
	<input type="hidden" name="redirect" value="${param.redirect}"/>
	<div class="control-group">
		<label class="control-label" for="activateCount">激活个数</label>
		<div class="controls">
			<input type="text" id="activateCount"  name="activateCount"  value='${entity.activateCount>0?entity.activateCount:""}' />
		</div>
	</div>
	
		
	<div class="control-group">
		<label class="control-label" for="payCount">计费次数</label>   
		<div class="controls">
			<input type="text" id="payCount" name="payCount" value='${entity.payCount>0?entity.payCount:""}' />
		</div>
	</div>
	<div class="control-group">
		<label class="control-label" for="payEarning">信息费</label>
		<div class="controls">
			<input type="text" id="payEarning" name="payEarning" value='${entity.payEarning>0?entity.payEarning:""}' />
		</div>
	</div>
	<div class="control-group">
		<label class="control-label" for="syncEarning">同步信息费</label>
		<div class="controls">
			<input type="text" id="syncEarning" name="syncEarning" value='${entity.syncEarning>0?entity.syncEarning:""}' />
		</div>
	</div>
	 
	<div class="control-group">
		<label class="control-label" for="discountActivateCount">渠道激活个数</label>
		<div class="controls">
			<input type="text" id="discountActivateCount" name="discountActivateCount" value='${entity.discountActivateCount>0?entity.discountActivateCount:""}' />
		</div>
	</div>		
	<div class="control-group">
		<label class="control-label" for="discountPayCount">渠道计费次数</label>
		<div class="controls">
			<input type="text" id="discountPayCount" name="discountPayCount" value='${entity.discountPayCount>0?entity.discountPayCount:""}' />
		</div>
	</div>		
	<div class="control-group">
		<label class="control-label" for="discountPayEarning">渠道信息费</label>
		<div class="controls">
			<input type="text" id="discountPayEarning" name="discountPayEarning" value='${entity.discountPayEarning>0?entity.discountPayEarning:""}' />
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