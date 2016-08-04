<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>  
<div class="hr"></div>
<form class="form-inline" />
 	<input type="text" class="input-large date-picker" id="searchFrom" value="${param.from}" placeholder="起始日期">
    <input type="text" class="input-large" id="searchTo" value="${param.to}"  placeholder="结束日期">
     <select class="input-large" id="product">
        <option value="0">选择产品</option>
        <c:forEach var="entity" items="${productList}">
        	<option value="${entity.id}" ${entity.id==param.productId?"selected":""} >${entity.name}</option>
        </c:forEach>
     </select> 
     <select class="input-large" id="channel" >
          <option value="0">选择渠道</option>
	 		<c:forEach var="entity" items="${channelList}">
	        	<option value="${entity.id}" ${entity.id==param.channelId?"selected":""} >${entity.displayName}</option>
	        </c:forEach>          
      </select>  
      <select class="input-large" id="pkg">
          <option value="0">选择软件包</option>
	 		<c:forEach var="entity" items="${packageList}">
	        	<option value="${entity.id}" ${entity.id==param.packageId?"selected":""} >${entity.appName}[${entity.id}][${entity.name}]</option>
	        </c:forEach>           
      </select>  
        <select class="input-large" id="sdk" >
        	<option value="0">选择SDK</option>
			 <c:forEach var="sdk" items="${sdkList}">
	 			<option value="${sdk.id}" ${param.sdkId==sdk.id?"selected":""}>${sdk.displayName}</option>
	 		</c:forEach>
        </select>	                
</form>
<div class="hr"></div>	   
 
<table cellpadding="0" cellspacing="0" border="0" class="table table-striped table-bordered table-condensed table-hover" id="example2">
     <thead>
         <tr>
             <th rowspan="2" >名称</th>
             <th rowspan="2" >价格</th>
             <th colspan="2" style="text-align:center"><div class="text-warning">取消数据</div></th>
             <th colspan="6" style="text-align:center">付费数据</th>
         </tr>
         <tr  class="success">
         	<th style="text-align:center"><div class="text-warning">取消总数</div></th>
         	<th style="text-align:center"><div class="text-warning">取消率</div></th>
         	<th style="text-align:center">付费总数</th>
         	<th style="text-align:center">付费率</th>
         	<th style="text-align:center"><div class="text-success">成功数</div></th>
         	<th style="text-align:center"><div class="text-success">成功率</div></th>         	
         	<th style="text-align:center"><div class="text-error">失败数</div></th>
         	<th style="text-align:center"><div class="text-error">失败率</div></th>           	         	
         </tr>
     </thead>
     <tbody>
     	<c:forEach var="bean" items="${statList}">
	 		<tr class="odd gradeX">
	             <td >${bean.payName}</td>
	             <td >${bean.price}</td>
	             <td style="text-align:center"><div class="text-warning">${bean.cancleCount}</div></td>
	             <td style="text-align:center"><div class="text-warning"><fmt:formatNumber value="${bean.cancleCount*100/bean.allCount}" pattern="##.##" minFractionDigits="0" />%</div></td>
	             <td style="text-align:center"  class="success">${bean.successCount+bean.failureCount}</td>
	             <td style="text-align:center"><fmt:formatNumber value="${((bean.successCount+bean.failureCount)*100/(bean.allCount))}" pattern="##.##" minFractionDigits="0" />%</td>
	             <td style="text-align:center"><div class="text-success">${bean.successCount}</div></td>
	             <td style="text-align:center"><div class="text-success"><fmt:formatNumber value="${(bean.successCount)*100/(bean.successCount+bean.failureCount)}" pattern="##.##" minFractionDigits="0" />% </div></td>
	             <td style="text-align:center"><div class="text-error">${bean.failureCount}</div></td>
	             <td style="text-align:center"><div class="text-error"><fmt:formatNumber value="${(bean.failureCount)*100/(bean.successCount+bean.failureCount)}" pattern="##.##" minFractionDigits="0" />%   </div></td>
	         </tr>     	
     	</c:forEach>
     </tbody>
 </table>
<script>
$(function() {
	 var refreshList=function(){
        var productId=$("#product").val();
        var channelId=$("#channel").val();
        var pkgId=$("#pkg").val();
        var sdkId=$("#sdk").val();
        var from=$("#searchFrom").val();
        var to=$("#searchTo").val();
        window.location.href="/admin/paypoint/?sdkId="+sdkId+"&productId="+productId+"&channelId="+channelId+"&packageId="+pkgId+"&from="+from+"&to="+to;
    };        	
   $("#channel").change(function(){ refreshList(); });
   $("#pkg").change(function(){ refreshList(); });
   $("#sdk").change(function(){ refreshList(); });
   $("#product").change(function(){ refreshList(); });
   $("#searchFrom").datepicker({format:"yyyy-mm-dd"}).on('changeDate', function(ev){refreshList();});
   $("#searchTo").datepicker({format:"yyyy-mm-dd"}).on('changeDate', function(ev){refreshList();});
});
</script>
    
