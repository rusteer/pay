<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>  
<div class="hr"></div>
<form class="form-horizontal"  method="post" action="/admin/sync/" id="form1" AUTOCOMPLETE="OFF" >
	<input type="hidden" name="packageId" value="${entity.packageId}"/>
    <input type="hidden" name="statDate" value="${entity.statDate}"/>
    <input type="hidden" name="redirectUrl" value="${param.redirectUrl}"/>
   <c:forEach var="sync" items="${syncList}">
	    <div class="control-group">
	       <label class="control-label" for="sdk-${sync.sdk.id}">[${sync.sdk.displayName}SDK]同步信息费</label>
	       <div class="controls">
	           <input class="input-xlarge focused" id="sdk-${sync.sdk.id}" type="text" name="sdk-${sync.sdk.id}" value="<fmt:formatNumber value="${sync.syncEarning/100}" pattern="##.##" minFractionDigits="0" />">
	       </div>
	   </div>     
   </c:forEach>
   <div class="control-group">
       <label class="control-label" for="discountActivateCount">渠道激活个数</label>
       <div class="controls">
           <input class="input-xlarge focused" id="discountActivateCount" type="text" name="discountActivateCount" value="${entity.discountActivateCount}">
       </div>
   </div>
   <div class="control-group">
       <label class="control-label" for="discountPayEarning">渠道信息费</label>
       <div class="controls">
           <input  class="input-xlarge focused" id="discountPayEarning" type="text" name="discountPayEarning" value="<fmt:formatNumber value="${entity.discountPayEarning/100}" pattern="##.##" minFractionDigits="0" />">
       </div>
   </div>

	<div class="form-actions">
		<button class="btn btn-info" type="submit" onclick="this.form.submit()">
			<i class="icon-ok bigger-110"></i>
			提交
		</button>
	&nbsp; &nbsp; &nbsp;
		<button class="btn" type="button" onclick="window.history.back();">
			<i class="icon-backward bigger-110"></i>
			返回
		</button>
	</div>
</form>
<div class="hr"></div> 
