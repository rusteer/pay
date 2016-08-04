<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<div class="breadcrumbs" id="breadcrumbs">
	<ul class="breadcrumb">
		<li><i class="icon-home home-icon"></i>游戏包列表 <span class="divider"> <i class="icon-angle-right arrow-icon"></i>
		</span></li>
		<li></li>
	</ul>
</div>
 

<div class="page-content">
	<div class="row-fluid">
		<div class="span12">
			<div class="table-toolbar">
				<form class="form-inline">
					<a href="/admin/${cmpName}/0" class="btn btn-small btn-success">新增游戏包</a>
				 	<select class="input-large" id="product" >
					 	  <option value="0">选择产品</option>
							 <c:forEach var="product" items="${productList}">
							 	<option value="${product.id}" ${param.productId==product.id?"selected":""}>${product.name}</option>
							 </c:forEach>
			          </select>
			          <select class="input-large" id="channel" >
			          	<option value="0">选择渠道</option>
						 <c:forEach var="channel" items="${channelList}">
						 	<option value="${channel.id}" ${param.channelId==channel.id?"selected":""}>${channel.displayName}</option>
						 </c:forEach>
			          </select>	
			          <select class="input-large" id="sdk" >
			          	<option value="">选择SDK</option>
						 <c:forEach var="element" items="${sdkList}">
						 	<option value="${element.name}" ${param.sdk==element.name?"selected":""}>${element.displayName}</option>
						 </c:forEach>
			          </select>				          				
				</form>
			</div>
			<div class="hr"></div>
			<table cellpadding="0" cellspacing="0" border="0" class="table table-striped table-bordered table-condensed table-hover" id="example2">
				<thead>
					<tr>
			            <th>ID</th>
			            <th>数据</th>
			            <th>打包状态</th>
			            <th>复制</th>
			            <th>别名</th>
			            <th>产品名称</th>
			            <th>渠道</th>
			            <th>包名</th>
			            <th>版本</th>
			            <th>版本号</th>
			            <th>名称</th>
			            <th>可用SDK</th>
			            <th>当前SDK</th>
			            <th>登录名</th>
			            <th>登陆密码</th>
			            <th>激活个数</th>
			            <th>计费次数</th>
			            <th>信息费</th>
			            <th>推广方式</th>
			            <th>推广价格</th>
			            <th>自动同步</th>
			             <th>扣量起始数</th>
			            <th>扣量百分比</th>					            
			            <th>其他信息</th>
					</tr>
				</thead>
				<tbody>
					<c:forEach var="entity" items="${list}">
						<tr class="odd gradeX">
				            <td><a href="/admin/${cmpName}/${entity.id}?&isCopy=0&productId=${entity.productId}&channelId=${entity.channelId}">${entity.id}</a></td>
				            <td><a href="/admin/stat/?channelId=${entity.channelId}&productId=${entity.productId}&pkgId=${entity.id}"  >查看</a></td>
				            <td>
				            	<c:choose>
				            		<c:when test='${entity.buildStatus==1}'>等待打包</c:when>
				            		<c:when test='${entity.buildStatus==2}'>正在打包</c:when>	
					            	<c:when test='${entity.buildStatus==3}'><a href='${entity.apkPath}'>打包完成</a></c:when>
					            	<c:when test='${entity.buildStatus==4}'>打包失败</c:when>				            	
				            	</c:choose>
				            </td>
				            <td><a href="/admin/${cmpName}/${entity.id}?&isCopy=1&productId=${entity.productId}&channelId=${entity.channelId}">复制</a></td>
				            <td>${entity.name}</td>
				            <td><a href="/admin/product/${productMap[entity.productId].id}">${productMap[entity.productId].name}</a></td>
				            <td><a href="/admin/channel/${channelMap[entity.channelId].id}">${channelMap[entity.channelId].displayName}</a></td>
				            <td>${entity.packageName}</td>
				            <td>${entity.versionName}</td>
				            <td>${entity.versionCode}</td>
				            <td>${entity.appName}</td>
				            <td>${entity.paySdks}</td>
				            <td>${entity.paySdk}</td>
				            <td>${entity.loginName}</td>
				            <td>${entity.loginPassword}</td>
				            <td>${entity.showActivateCount?"显示":"隐藏"}</td>
				            <td>${entity.showPayCount?"显示":"隐藏"}</td>
				            <td>${entity.showPayEarning?"显示":"隐藏"}</td>
				            <td>${entity.promotionType}</td>
				            <td>${entity.marketPrice>0?entity.marketPrice:""}</td>
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
            var refreshList=function(){
                var channelId=$("#channel").val();
                var productId=$("#product").val();
                var sdk=$("#sdk").val();
                window.location.href="/admin/${cmpName}/?channelId="+channelId+"&productId="+productId+"&sdk="+sdk;
            }
            $(function() {
                $("#product").change(function(){ refreshList(); });
                $("#channel").change(function(){ refreshList(); });
                $("#sdk").change(function(){ refreshList(); });
                $("[data-toggle='popover']").popover();
            });
        </script>