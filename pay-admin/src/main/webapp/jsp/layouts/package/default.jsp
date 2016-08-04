<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="sitemesh" uri="http://www.opensymphony.com/sitemesh/decorator" %>  
<!DOCTYPE html>
<html lang="en">
<head>
<meta charset="utf-8" />
<title>渠道报表</title>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<meta name="viewport" content="width=device-width, initial-scale=1.0" />
<link rel="stylesheet" href="/static/assets/css/bootstrap.min.css" />
<link rel="stylesheet" href="/static/assets/css/bootstrap-responsive.min.css" />
<link rel="stylesheet" href="/static/assets/css/font-awesome.min.css" />
<link rel="stylesheet" href="/static/assets/css/select2.css" />
<link rel="stylesheet" href="/static/assets/css/jquery-ui-1.10.3.custom.min.css" />
<link rel="stylesheet" href="/static/assets/css/chosen.css" />
<link rel="stylesheet" href="/static/assets/css/datepicker.css" />
<link rel="stylesheet" href="/static/assets/css/ace.min.css" />
<link rel="stylesheet" href="/static/assets/css/ace-responsive.min.css" />
<link rel="stylesheet" href="/static/assets/css/ace-skins.min.css" />
<script src="/static/assets/ajax.googleapis.com/ajax/libs/jquery/2.0.3/jquery.min.js"></script>
<script src="/static/assets/js/bootstrap.min.js"></script>
<script src="/static/assets/js/ace-elements.min.js"></script>
<script src="/static/assets/js/ace.min.js"></script>
<script src="/static/assets/js/jquery.validate.min.js"></script>
<script src="/static/assets/js/additional-methods.min.js"></script>
<script src="/static/assets/js/bootbox.min.js"></script>
<script src="/static/assets/js/select2.min.js"></script>
<script src="/static/assets/js/date-time/bootstrap-datepicker.min.js"></script>
<script src="/static/assets/js/chosen.jquery.min.js"></script>
<script src="/static/assets/js/underscore/underscore-min.js"></script>
<style type="text/css">
	 
</style>
</head>
<body>
		<div class="navbar">
			<div class="navbar-inner">
				<div class="container-fluid">
					<a href="#" class="brand"> <small> <i class="icon-leaf"></i>${pkg.name}</small> </a><!--/.brand-->
					<ul class="nav ace-nav pull-right">
						<li> <a href="/package/logout"> <i class="icon-off"></i> 退出 </a></li>
					</ul><!--/.ace-nav-->
				</div><!--/.container-fluid-->
			</div><!--/.navbar-inner-->
		</div>
				<div class="page-content">
					<div class="row-fluid">
						<div class="span12">
							<!--PAGE CONTENT BEGINS-->
							<sitemesh:body/>
							<!--PAGE CONTENT ENDS-->
						</div><!--/.span-->
					</div><!--/.row-fluid-->
				</div><!--/.page-content-->
		<a href="#" id="btn-scroll-up" class="btn-scroll-up btn btn-small btn-inverse">
			<i class="icon-double-angle-up icon-only bigger-110"></i>
		</a>
		<!--basic scripts-->
		<!--[if !IE]>-->
		
		<!--<![endif]-->
		<!--[if IE]>
<script src="/assets/ajax.googleapis.com/ajax/libs/jquery/1.10.2/jquery.min.js"></script>
<![endif]-->
		<!--[if !IE]>-->
		<script type="text/javascript">
			window.jQuery || document.write("<script src='/static/assets/js/jquery-2.0.3.min.js'>"+"<"+"/script>");
		</script>
		<!--<![endif]-->
		<!--[if IE]>
<script type="text/javascript">
 window.jQuery || document.write("<script src='/static/assets/js/jquery-1.10.2.min.js'>"+"<"+"/script>");
</script>
<![endif]-->

		<script type="text/javascript">
			if("ontouchend" in document) document.write("<script src='/static/assets/js/jquery.mobile.custom.min.js'>"+"<"+"/script>");
		</script>
		
		<!--page specific plugin scripts-->
		<!--ace scripts-->

		<!--inline scripts related to this page-->
	</body>
</html>
