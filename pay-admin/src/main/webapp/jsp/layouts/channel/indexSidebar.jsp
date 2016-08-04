<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<c:set var="activate" value="class='active open'"/>
<div class="sidebar" id="sidebar">
	<ul class="nav nav-list">
	<li  class="active open" >
		<a href="#" class="dropdown-toggle"> <i class="icon-desktop"></i> <span class="menu-text">数据 </span> <b class="arrow icon-angle-down"></b> </a>
		<ul class="submenu">
			<li ${param.packageId==null||param.packageId==0?activate:""}> <a href="/channel/report/?from=${from}&to=${param.to}"> <i class="icon-dashboard"></i>所有产品数据 </a> </li>
			<c:forEach var="pkg" items="${packageList}">
				<li ${param.packageId==pkg.id?activate:""}> <a href="/channel/report/?packageId=${pkg.id}&from=${from}&to=${param.to}"> <i class="icon-double-angle-right"></i>${pkg.name}</a> </li>	
			</c:forEach>
		</ul>
	</li>			
	</ul>
	<div class="sidebar-collapse" id="sidebar-collapse"> <i class="icon-double-angle-left"></i></div>
</div>

	