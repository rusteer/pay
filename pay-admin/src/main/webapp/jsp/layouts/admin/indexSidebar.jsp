<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<c:set var="activate" value="class='active open'"/>
<div class="sidebar" id="sidebar">
	<ul class="nav nav-list">
		<li ${cmpName=="channel"?activate:""}><a href="/admin/channel/"> <i class="icon-list-alt"></i> <span class="menu-text">渠道管理</span> </a></li>
		<li ${cmpName=="product"?activate:""}><a href="/admin/product/"> <i class="icon-list-alt"></i> <span class="menu-text">产品管理</span> </a></li>
		<li ${cmpName=="package"?activate:""}><a href="/admin/package/"> <i class="icon-list-alt"></i> <span class="menu-text">软件包管理</span> </a></li>
		<li ${ activate }><a href="#" class="dropdown-toggle"> <i class="icon-list-alt"></i> <span class="menu-text">数据报表</span> <b class="arrow icon-angle-down"></b> </a>
			<ul class="submenu">
				<li ${cmpName=="stat"?activate:""}><a href="/admin/stat/"> <i class="icon-list"></i> <span class="menu-text">推广报表</span></a></li>
				<li ${cmpName=="sync"?activate:""}><a href="/admin/sync/"> <i class="icon-list"></i> <span class="menu-text">数据同步</span></a></li>
				<li ${cmpName=="paypoint"?activate:""}><a href="/admin/paypoint/"> <i class="icon-list"></i> <span class="menu-text">计费点分析</span></a></li>
			</ul>
		</li>				
				 				
	</ul>
	<div class="sidebar-collapse" id="sidebar-collapse"> <i class="icon-double-angle-left"></i></div>
</div>

	