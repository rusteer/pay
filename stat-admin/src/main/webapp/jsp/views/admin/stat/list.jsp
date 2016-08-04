<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>  
<div class="breadcrumbs" id="breadcrumbs">
	<ul class="breadcrumb">
		<li><i class="icon-home home-icon"></i>推广列表 <span class="divider"> <i class="icon-angle-right arrow-icon"></i>
		</span></li>
		<li></li>
	</ul>
</div>
<div class="page-content">
	<div class="row-fluid">
		<div class="span12">
			<div class="table-toolbar">
				<form class="form-inline">
				 	<input type="text" class="input-large date-picker" id="from" value="${from}" placeholder="起始日期">
				    <input type="text" class="input-large" id="to" value="${param.to}"  placeholder="结束日期">					
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
			          <select class="input-large" id="package" >
			          	<option value="0">选择软件包</option>
						 <c:forEach var="package" items="${packageList}">
						 	<option value="${package.id}" ${param.packageId==package.id?"selected":""}>${package.name}</option>
						 </c:forEach>
			          </select>	
				</form>
			</div>
			<div class="hr"></div>
			<table cellpadding="0" cellspacing="0" border="0" class="table table-striped table-bordered table-condensed table-hover" id="example2">
				<thead>
					<tr>
		             <th>统计日期</th>
		             <th>激活个数</th>
		             <th>计费次数</th>
		             <th>信息费</th>
		             <th>同步信息费</th>
		             <th>ARPU</th>
		             <th>同步ARPU</th>
		             <th>付费率</th>
		             <th>转化</th>
		             <th>渠道激活个数</th>
		             <th>渠道计费次数</th>
		             <th>渠道信息费</th>
					</tr>
				</thead>
				<tbody>
					<c:set var="payEarning" value="0"/>
					<c:set var="payCount" value="0"/>
					<c:set var="syncEarning" value="0"/>
					<c:set var="activateCount" value="0"/>
					<c:set var="discountActivateCount" value="0"/>
					<c:set var="discountPayEarning" value="0"/>
					<c:forEach var="stat" items="${list}">
						<c:set var="payEarning" value="${payEarning+stat.payEarning}"/>
						<c:set var="payCount" value="${payCount+stat.payCount}"/>
						<c:set var="syncEarning" value="${syncEarning+stat.syncEarning}"/>
						<c:set var="activateCount" value="${activateCount+stat.activateCount}"/>
						<c:set var="discountActivateCount" value="${discountActivateCount+stat.discountActivateCount}"/>
						<c:set var="discountPayEarning" value="${discountPayEarning+stat.discountPayEarning}"/>
						<tr class="odd gradeX">
			             <td>	
							<c:choose>
				                 <c:when test="${stat.packageId>0}">
			                     	<a href="javascript:show(${stat.packageId},'${stat.statDate}')"> ${stat.statDate}</a>
			                 	 </c:when>
			                 	 <c:otherwise>
			                 	   ${stat.statDate}
			                 	 </c:otherwise>							
							</c:choose>
			             </td>
			             <td>${stat.activateCount>0?stat.activateCount:""}</td>
			             <td>${stat.payCount>0?stat.payCount:""}</td>
			             <td><c:if test="${stat.payEarning>0}"><fmt:formatNumber value="${stat.payEarning}" pattern="##.##" minFractionDigits="0" /></c:if></td>
			             <td><c:if test="${stat.syncEarning>0}"><fmt:formatNumber value="${stat.syncEarning}" pattern="##.##" minFractionDigits="0" /></c:if></td>
			             <td><c:if test="${stat.activateCount>0 && stat.payEarning>0}"><fmt:formatNumber value="${stat.payEarning/(stat.activateCount)}" pattern="##.##" minFractionDigits="2" /></c:if></td>
			             <td><c:if test="${stat.activateCount>0 && stat.syncEarning>0}"><fmt:formatNumber value="${stat.syncEarning/(stat.activateCount)}" pattern="##.##" minFractionDigits="2" /></c:if></td>
			             <td><c:if test="${stat.activateCount>0 && stat.payCount>0}"><fmt:formatNumber value="${stat.payCount*100/(stat.activateCount)}" pattern="##.##" minFractionDigits="2" />%</c:if></td>
			             <td><c:if test="${stat.syncEarning>0 && stat.syncEarning>0}"><fmt:formatNumber value="${stat.syncEarning*100/(stat.payEarning)}" pattern="##.##" minFractionDigits="2" />%</c:if></td>
			             <td><c:if test="${stat.discountActivateCount>0}"><fmt:formatNumber value="${stat.discountActivateCount}" pattern="##.##" minFractionDigits="0" /></c:if></td>
			             <td><c:if test="${stat.discountPayCount>0}"><fmt:formatNumber value="${stat.discountPayCount}" pattern="##.##" minFractionDigits="0" /></c:if></td>
			             <td><c:if test="${stat.discountPayEarning>0}"><fmt:formatNumber value="${stat.discountPayEarning}" pattern="##.##" minFractionDigits="0" /></c:if></td>
						</tr>
					</c:forEach>
						<tr class="odd gradeX">
			             <th>总计</th>
			             <th>${activateCount>0?activateCount:""}</th>
			             <th>${payCount>0?payCount:""}</th>
			             <th><c:if test="${payEarning>0}"><fmt:formatNumber value="${payEarning}" pattern="##.##" minFractionDigits="0" /></c:if></th>
			             <th><c:if test="${syncEarning>0}"><fmt:formatNumber value="${syncEarning}" pattern="##.##" minFractionDigits="0" /></c:if></th>
			             <th><c:if test="${activateCount>0 && payEarning>0}"><fmt:formatNumber value="${payEarning/(activateCount)}" pattern="##.##" minFractionDigits="2" /></c:if></th>
			             <th><c:if test="${activateCount>0 && syncEarning>0}"><fmt:formatNumber value="${syncEarning/(activateCount)}" pattern="##.##" minFractionDigits="2" /></c:if></th>
			             <th><c:if test="${activateCount>0 && payCount>0}"><fmt:formatNumber value="${payCount*100/(activateCount)}" pattern="##.##" minFractionDigits="2" />%</c:if></th>
			             <th><c:if test="${syncEarning>0 && syncEarning>0}"><fmt:formatNumber value="${syncEarning*100/(payEarning)}" pattern="##.##" minFractionDigits="2" />%</c:if></th>
			             <th><c:if test="${discountActivateCount>0}"><fmt:formatNumber value="${discountActivateCount}" pattern="##.##" minFractionDigits="0" /></c:if></th>
			             <th><c:if test="${discountPayCount>0}"><fmt:formatNumber value="${discountPayCount}" pattern="##.##" minFractionDigits="0" /></c:if></th>
			             <th><c:if test="${discountPayEarning>0}"><fmt:formatNumber value="${discountPayEarning}" pattern="##.##" minFractionDigits="0" /></c:if></th>
						</tr>					
				</tbody>
			</table>
		</div>
	</div>
</div>
<script>
var show=function(packageId,statDate){
	 window.location.href="/admin/stat/form?packageId="+packageId+"&statDate="+statDate+"&redirect="+encodeURIComponent(window.location.href) ;
}
 $(function() {
	 var refreshList=function(){
         var productId=$("#product").val();
         var channelId=$("#channel").val();
         var pkgId=$("#package").val();
         var from=$("#from").val();
         var to=$("#to").val();
         window.location.href="?productId="+productId+"&channelId="+channelId+"&packageId="+pkgId+"&from="+from+"&to="+to ;
     };
     
    $("#channel").change(function(){ refreshList(); });
    $("#package").change(function(){ refreshList(); });
    $("#product").change(function(){ refreshList(); });
    $("#from").datepicker({format:"yyyy-mm-dd"}).on('changeDate', function(ev){refreshList();});
    $("#to").datepicker({format:"yyyy-mm-dd"}).on('changeDate', function(ev){refreshList();});
});
 
</script>