<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>  
<div class="breadcrumbs" id="breadcrumbs">
	<ul class="breadcrumb">
		<li><i class="icon-home home-icon"></i>数据同步<span class="divider"> <i class="icon-angle-right arrow-icon"></i>
		</span></li>
		<li></li>
	</ul>
</div>
<div class="page-content">
	<div class="row-fluid">
		<div class="span12">
			<div class="table-toolbar">
				<form class="form-inline">
				 	<input type="text" class="input-large date-picker" id="statDate" value="${statDate}" placeholder="日期">
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
			          <select class="input-large" id="showAllPkgs" >
			          	<option value="0">只显示有效数据</option>
			          	<option value="1" ${param.showAllPkgs==1?"selected":""}>显示所有数据</option>
			          </select>
				</form>
			</div>
			<div class="hr"></div>
			<table cellpadding="0" cellspacing="0" border="0" class="table table-striped table-bordered table-condensed table-hover" id="example2">
				<thead>
					<tr>
			     	 <th>游戏包ID</th>
			     	 <th>渠道</th>
			     	 <th>游戏包</th>
			         <th>激活个数</th>
			         <th>计费次数</th>
			         <th>信息费</th>
			         <th>同步信息费</th>
					 <th>ARPU</th>
					 <th>同步ARPU</th>
			         <th>付费率</th>
			         <th>转化</th>
			         <th>渠道激活个数</th>
			         <th>渠道信息费</th>
			         <th>推广方式</th>
			         <th> </th>
					</tr>
				</thead>
				<tbody>
					<c:set var="payEarning" value="0"/>
					<c:set var="payCount" value="0"/>
					<c:set var="syncEarning" value="0"/>
					<c:set var="activateCount" value="0"/>
					<c:set var="discountActivateCount" value="0"/>
					<c:set var="discountPayEarning" value="0"/>				
					<c:forEach var="stat" items="${statList}">
						<c:set var="payEarning" value="${payEarning+stat.payEarning}"/>
						<c:set var="payCount" value="${payCount+stat.payCount}"/>
						<c:set var="syncEarning" value="${syncEarning+stat.syncEarning}"/>
						<c:set var="activateCount" value="${activateCount+stat.activateCount}"/>
						<c:set var="discountActivateCount" value="${discountActivateCount+stat.discountActivateCount}"/>
						<c:set var="discountPayEarning" value="${discountPayEarning+stat.discountPayEarning}"/>					
						<tr class="odd gradeX">
			             <!-- 游戏包ID --><td> ${stat.packageId}</td>
			             <!-- 渠道 --><td><a href="/admin/channel/${stat.channel.id}">${stat.channel.displayName}</a></td>
			             <!-- 游戏包 --><td><a href="/admin/package/${stat.pkg.id}">${stat.pkg.appName}[${stat.pkg.name}]</a></td>
			             <!-- 激活个数 --><td>${stat.activateCount>0?stat.activateCount:""}</td>
			             <!-- 计费次数 --><td>${stat.payCount>0?stat.payCount:""}</td>
			             <!-- 信息费 --><td><c:if test="${stat.payEarning>0}"><fmt:formatNumber value="${stat.payEarning/100}" pattern="##.##" minFractionDigits="0" /></c:if></td>
			             <!-- 同步信息费 --><td><c:if test="${stat.syncEarning>0}"><fmt:formatNumber value="${stat.syncEarning/100}" pattern="##.##" minFractionDigits="0" /></c:if></td>
			             <!-- ARPU --><td><c:if test="${stat.activateCount>0 && stat.payEarning>0}"><fmt:formatNumber value="${stat.payEarning/100/(stat.activateCount)}" pattern="##.##" minFractionDigits="2" /></c:if></td>
			             <!-- 同步ARPU --><td><c:if test="${stat.activateCount>0 && stat.syncEarning>0}"><fmt:formatNumber value="${stat.syncEarning/100/(stat.activateCount)}" pattern="##.##" minFractionDigits="2" /></c:if></td>
			             <!-- 付费率--><td><c:if test="${stat.activateCount>0 && stat.payCount>0}"><fmt:formatNumber value="${stat.payCount*100/(stat.activateCount)}" pattern="##.##" minFractionDigits="2" />%</c:if></td>
			             <!-- 转化 --><td><c:if test="${stat.syncEarning>0 && stat.payEarning>0}"><fmt:formatNumber value="${stat.syncEarning*100/(stat.payEarning)}" pattern="##.##" minFractionDigits="2" />%</c:if></td>
			             <td><c:if test="${stat.discountActivateCount>0}"><fmt:formatNumber value="${stat.discountActivateCount}" pattern="##.##" minFractionDigits="0" /></c:if></td>
			             <td><c:if test="${stat.discountPayEarning>0}"><fmt:formatNumber value="${stat.discountPayEarning/100}" pattern="##.##" minFractionDigits="0" /></c:if></td>
			             <td>${stat.pkg.promotionType}</td> 
			             <td><a href="javascript:updateData(${stat.packageId},'${stat.statDate}')" class="btn-link">更新数据</a></td>            
						</tr>
					</c:forEach>
						<tr class="odd gradeX">
				             <td colspan="3">统计</td>
				             <!-- 激活个数 --><td>${activateCount>0?activateCount:""}</td>
				             <!-- 计费次数 --><td>${payCount>0?payCount:""}</td>
				             <!-- 信息费 --><td><c:if test="${payEarning>0}"><fmt:formatNumber value="${payEarning/100}" pattern="##.##" minFractionDigits="0" /></c:if></td>
				             <!-- 同步信息费 --><td><c:if test="${syncEarning>0}"><fmt:formatNumber value="${syncEarning/100}" pattern="##.##" minFractionDigits="0" /></c:if></td>
				             <!-- ARPU --><td><c:if test="${activateCount>0 && payEarning>0}"><fmt:formatNumber value="${payEarning/100/(activateCount)}" pattern="##.##" minFractionDigits="2" /></c:if></td>
				             <!-- 同步ARPU --><td><c:if test="${activateCount>0 && syncEarning>0}"><fmt:formatNumber value="${syncEarning/100/(activateCount)}" pattern="##.##" minFractionDigits="2" /></c:if></td>
				             <!-- 付费率--><td><c:if test="${activateCount>0 && payCount>0}"><fmt:formatNumber value="${payCount*100/(activateCount)}" pattern="##.##" minFractionDigits="2" />%</c:if></td>
				             <!-- 转化 --><td><c:if test="${syncEarning>0 && payEarning>0}"><fmt:formatNumber value="${syncEarning*100/(payEarning)}" pattern="##.##" minFractionDigits="2" />%</c:if></td>
				             <td><c:if test="${discountActivateCount>0}"><fmt:formatNumber value="${discountActivateCount}" pattern="##.##" minFractionDigits="0" /></c:if></td>
				             <td><c:if test="${discountPayEarning>0}"><fmt:formatNumber value="${discountPayEarning/100}" pattern="##.##" minFractionDigits="0" /></c:if></td>
				             <td> </td> 
				             <td> </td>      
						</tr>					
				</tbody>
			</table>
		</div>
	</div>
</div>
<script>
var updateData=function(pkgId,statDate){
	var url="/admin/sync/"+pkgId+"/"+statDate+"/?redirectUrl="+encodeURIComponent(window.location.href);
	window.location.href=url;
}
 $(function() {
	 var refreshList=function(){
         var productId=$("#product").val();
         var channelId=$("#channel").val();
         var statDate=$("#statDate").val();
         var showAllPkgs=$("#showAllPkgs").val();
         window.location.href="?productId="+productId+"&channelId="+channelId+"&statDate="+statDate+"&showAllPkgs="+showAllPkgs;
     }; 
     
     
    $("#showAllPkgs").change(function(){ refreshList(); });
    $("#channel").change(function(){ refreshList(); });
    $("#product").change(function(){ refreshList(); });
    $("#statDate").datepicker({format:"yyyy-mm-dd"}).on('changeDate', function(ev){refreshList();});
});
 </script>