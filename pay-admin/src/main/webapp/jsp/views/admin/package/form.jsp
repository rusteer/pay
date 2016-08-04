<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<div class="breadcrumbs" id="breadcrumbs">
	<ul class="breadcrumb">
		<li><i class="icon-home home-icon"></i><a
			href="/admin/package/?channelId=${entity.channelId}&productId=${entity.productId}">产品列表</a>
			<span class="divider"> <i class="icon-angle-right arrow-icon"></i>
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
<hr />
<form class="form-horizontal" id="bizForm" method="post"
	action="/admin/${cmpName}/" AUTOCOMPLETE="OFF">
	<c:set var="entityId" value="${entity.id}" />
	<c:if test="${param.isCopy=='1'}">
		<c:set var="entityId" value="" />
	</c:if>
	<input type="hidden" name="id" value="${entityId}"> <input
		type="hidden" name="redirectUrl" id="redirectUrl"
		value="/admin/package/?channelId=${entity.channelId}&productId=${entity.productId}">
	<div class="page-content">
		<ul class="nav nav-tabs" id="bizFormTab">
			<li><a href="#tab1" data-toggle="tab">基本属性</a></li>
			<li><a href="#tab2" data-toggle="tab">计费点信息</a></li>
			<li><a href="#tab3" data-toggle="tab">打包命令</a></li>
		</ul>
		<div id="myTabContent" class="tab-content">
			<div class="tab-pane active span12" id="tab1">
				<table cellpadding="0" cellspacing="0" border="0"
					class="table table-striped table-bordered table-condensed table-hover"
					id="example2">
					<tr>
						<td>别名</td>
						<td><input class="span5" type="text" id="name"
							placeholder="别名" name="name" value="${entity.name}" /></td>
						<td>重新打包</td>
						<td><select class="span5" id="needBuild" name="needBuild">
								<option value="0">否</option>
								<option value="1">是</option>
						</select></td>
					</tr>
					<tr>
						<td>可用SDK</td>
						<td><select multiple="" class="span5 chzn-select" id="paySdks" name="paySdks" data-placeholder="可用SDK">
								<c:forEach var="sdk" items="${sdkList}">
									<option value="${sdk.name}"
										${fn:contains(entity.paySdks,sdk.name)?"selected":"" }>${sdk.displayName}</option>
								</c:forEach>
						</select></td>
						<td>当前SDK</td>
						<td><select class="span5" id="paySdk" name="paySdk">
								<c:forEach var="sdk" items="${sdkList}">
									<option value="${sdk.name}"
										${fn:contains(entity.paySdk,sdk.name)?"selected":"" }>${sdk.displayName}</option>
								</c:forEach>
						</select></td>
					</tr>
					<tr>
						<td>产品</td>
						<td><select class="span5" id="productId" name="productId">
								<option value="0"></option>
								<c:forEach var="product" items="${productList}">
									<option value="${product.id}"
										${entity.productId==product.id?"selected":""}>${product.name}</option>
								</c:forEach>
						</select></td>
						<td>渠道</td>
						<td><select class="span5" id="channelId" name="channelId">
								<option value="0"></option>
								<c:forEach var="channel" items="${channelList}">
									<option value="${channel.id}"
										${entity.channelId==channel.id?"selected":""}>${channel.displayName}</option>
								</c:forEach>
						</select></td>
					</tr>

					<tr>
						<td>包名</td>
						<td><input class="span5" type="text" id="packageName"
							placeholder="包名" name="packageName" value="${entity.packageName}" /></td>
						<td>名称</td>
						<td><input class="span5" id="appName" type="text"
							name="appName" value="${entity.appName}"></td>
					</tr>
					<tr>
						<td>版本</td>
						<td><input class="span5" type="text" id="versionName"
							placeholder="版本" name="versionName" value="${entity.versionName}" /></td>
						<td>版本号</td>
						<td><input class="span5" id="versionCode" type="text"
							name="versionCode" value="${entity.versionCode}"></td>
					</tr>

					<tr>
						<td>登录名</td>
						<td><input class="span5" id="loginName" type="text"
							name="loginName" value="${entity.loginName}"></td>
						<td>登录密码</td>
						<td><input class="span5" id="loginPassword" type="text"
							name="loginPassword" value="${entity.loginPassword}"></td>
					</tr>
					<tr>
						<td>激活个数</td>
						<td><select class="span5" id="showActivateCount"
							name="showActivateCount">
								<option value="0">隐藏</option>
								<option value="1" ${entity.showActivateCount?"selected":"" }>显示</option>
						</select></td>
						<td>计费次数</td>
						<td><select class="span5" id="showPayCount"
							name="showPayCount">
								<option value="0">隐藏</option>
								<option value="1" ${entity.showPayCount?"selected":"" }>显示</option>
						</select></td>
					</tr>
					<tr>
						<td>信息费</td>
						<td><select class="span5" id="showPayEarning"
							name="showPayEarning">
								<option value="0">隐藏</option>
								<option value="1" ${entity.showPayEarning?"selected":"" }>显示</option>
						</select></td>
						<td>推广方式</td>
						<td><select class="span5" id="promotionType"
							name="promotionType">
								<option value="CPA">CPA</option>
								<option value="CPS"
									${entity.promotionType=="CPS"?"selected":"" }>CPS</option>
						</select></td>
					</tr>
					<tr>
						<td>自动同步</td>
						<td><select id="autoSync" name="autoSync" class="span5">
								<option value="0">关闭</option>
								<option value="1" ${entity.autoSync?"selected":""}>开启</option>
						</select></td>
						<td>推广价格</td>
						<td ><input class="span5" type="text" id="marketPrice" name="marketPrice" value="${entity.marketPrice}" /></td>						
					</tr>
					<tr>
						<td>扣量起始数</td>
						<td><input class="span5" type="text" id="discountStartCount" name="discountStartCount" value="${entity.discountStartCount}" /></td>
						<td>扣量百分比</td>
						<td><input class="span5" type="text" id="discountRate" name="discountRate" value="${entity.discountRate}" /></td>
					</tr>
					<tr>
						<td>其他信息</td>
						<td colspan="3"><textarea class="span10" id="info" placeholder="其他信息" name="info">${entity.info}</textarea></td>
					</tr>
				</table>
			</div>
			<div class="tab-pane span12" id="tab2">
				<table cellpadding="0" cellspacing="0" border="0"
					class="table table-striped table-bordered table-condensed table-hover"
					id="example2">
					<tr>
						<th>计费点ID</th>
						<th>计费点名称</th>
						<th>计费点价格</th>
						<th class="th2">SDK1</th>
						<th class="th2">SDK2</th>
						<th class="th2">SDK3</th>
					</tr>
					<c:forEach var="entity" items="${packageItemList}">
						<tr>
							<td>${entity.payIndex}</td>
							<td>${entity.payName}</td>
							<td>${entity.payPrice}</td>
							<td><select class="span2"
								name="payIndex-${entity.payIndex}-sdk1">
									<option value="0"></option>
									<c:forEach var="sdk" items="${sdkMap}">
										<option value="${sdk.value.id}"
											${entity.paySdk1==sdk.value.id?"selected":"" }>${sdk.value.displayName}</option>
									</c:forEach>
							</select></td>
							<td><select class="span2"
								name="payIndex-${entity.payIndex}-sdk2">
									<option value="0"></option>
									<c:forEach var="sdk" items="${sdkMap}">
										<option value="${sdk.value.id}"
											${entity.paySdk2==sdk.value.id?"selected":"" }>${sdk.value.displayName}</option>
									</c:forEach>
							</select></td>
							<td><select class="span2"
								name="payIndex-${entity.payIndex}-sdk3">
									<option value="0"></option>
									<c:forEach var="sdk" items="${sdkMap}">
										<option value="${sdk.value.id}"
											${entity.paySdk3==sdk.value.id?"selected":"" }>${sdk.value.displayName}</option>
									</c:forEach>
							</select></td>
						</tr>
					</c:forEach>
				</table>
			</div>
			<div class="tab-pane span10" id="tab3">
				<textarea class="span12" placeholder="计费点信息" rows="20">${buildFunc}</textarea>
			</div>
		</div>
	</div>
	<div class="form-actions">
		<button class="btn btn-info" type="button" onclick="submitForm(this)">
			<i class="icon-ok bigger-110"></i> 提交
		</button>
		&nbsp; &nbsp; &nbsp;
		<button class="btn" type="button" onclick="window.history.back();">
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
	var updatePaySdk = function() {
		var selectdValues = $("#paySdks").val();
		$("#paySdk option").each(function() {
			$(this).remove();
		});
		$("#paySdks option").each(function() {
			var option = $(this);
			if (_.contains(selectdValues, option.val())) {
				var str = "<option value='" + option.val() + "'";
				if (option.val() == window.paySdk) {
					str = str + " selected";
				}
				str = str + ">" + option.text() + "</option>"
				$("#paySdk").append(str);
			}
		});
	}
	$(function() {
		window.paySdk = "${entity.paySdk}";
		$('#bizFormTab a:first').tab('show')
		$(".chzn-select").chosen();
		updatePaySdk();
		$("#paySdks").change(function() {
			updatePaySdk();
		});
		$("#paySdk").change(function(btn) {
			window.paySdk = $("#paySdk").val();
		});
	});
</script>