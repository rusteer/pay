<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>  
<div class="page-content">
	<div class="row-fluid">
		<div class="span12">
			<div class="table-toolbar">
				<form class="form-inline">
				 	<input type="text" class="input-large date-picker" id="from" value="${from}" placeholder="起始日期">
				    <input type="text" class="input-large" id="to" value="${param.to}"  placeholder="结束日期">					
				</form>
			</div>
			<div class="hr"></div>
			<table cellpadding="0" cellspacing="0" border="0" class="table table-striped table-bordered table-condensed table-hover" id="example2">
				<thead>
					<tr>
		             <th>统计日期</th>
		             <c:if test="${pkg.showActivateCount}"><th>激活个数</th></c:if>
		             <c:if test="${pkg.showPayCount}"><th>计费次数</th></c:if>
		             <c:if test="${pkg.showPayEarning}"><th>成功计费(元)</th></c:if>
					</tr>
				</thead>
				<tbody>
					<c:set var="discountActivateCount" value="0"/>
					<c:set var="discountPayCount" value="0"/>
					<c:set var="discountPayEarning" value="0"/>
					<c:forEach var="stat" items="${statList}">
					<c:set var="discountActivateCount" value="${discountActivateCount+stat.discountActivateCount}"/>
					<c:set var="discountPayCount" value="${discountPayCount+stat.discountPayCount}"/>
					<c:set var="discountPayEarning" value="${discountPayEarning+stat.discountPayEarning}"/>
						<tr class="odd gradeX">
				             <td>${stat.statDate}</td>
				             <c:if test="${pkg.showActivateCount}"><td>${stat.discountActivateCount}</td></c:if>
				             <c:if test="${pkg.showPayCount}"><td>${stat.discountPayCount}</td></c:if>
				             <c:if test="${pkg.showPayEarning}"><td><fmt:formatNumber value="${stat.discountPayEarning/100}" pattern="##.##" minFractionDigits="0" /></td></c:if>
						</tr>
					</c:forEach>
						<tr class="odd gradeX">
				             <td>总计</td>
				             <c:if test="${pkg.showActivateCount}"><td>${discountActivateCount}</td></c:if>
				             <c:if test="${pkg.showPayCount}"><td>${discountPayCount}</td></c:if>
				             <c:if test="${pkg.showPayEarning}"><td><fmt:formatNumber value="${discountPayEarning/100}" pattern="##.##" minFractionDigits="0" /></td></c:if>
						</tr>					
				</tbody>
			</table>
		</div>
	</div>
</div>
        <script>
        $(function() {
       	 var refreshList=function(){
                var from=$("#from").val();
                var to=$("#to").val();
                window.location.href="?from="+from+"&to="+to;
            };        	
           $("#from").datepicker({format:"yyyy-mm-dd"}).on('changeDate', function(ev){refreshList();});
           $("#to").datepicker({format:"yyyy-mm-dd"}).on('changeDate', function(ev){refreshList();});
       });
        </script>