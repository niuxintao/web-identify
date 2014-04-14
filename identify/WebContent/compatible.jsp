<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@taglib uri="/struts-tags" prefix="s"%>
<!DOCTYPE html>

<html>
<head>
<!-- Generation main -->
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<meta name="author" content="jason">


<title>Identify</title>
<link rel="stylesheet" type="text/css" href="css/bootstrap.css">

<style type="text/css">
body {
	padding-top: 20px;
	padding-bottom: 40px;
}

/* Custom table color */
.table-striped tbody>tr:nth-child(odd)>td,.table-striped tbody>tr:nth-child(odd)>th
	{
	background-color: #b8e7ff;
}
/* Custon link color */
.nav-tabs>li>a {
	color: #666666;
}

.nav-tabs>.active>a {
	color: #000000;
}
</style>
<link href="css/bootstrap-responsive.min.css" rel="stylesheet">


<script type="text/javascript" src="script/jquery.min.js"></script>
<script type="text/javascript">
//<![CDATA[ 
$(window).load(function(){
var placeholder = '1 2 3 4\n pass fail\n 1000 2000 3000\n char string integer bit';
$('textarea').attr('value', placeholder);

$('textarea').focus(function(){
    if($(this).val() === placeholder){
        $(this).attr('value', '');
    }
});

$('textarea').blur(function(){
    if($(this).val() ===''){
        $(this).attr('value', placeholder);
    }    
});
});//]]> 

</script>


</head>
<body>

	<!-- nav -->
	<div class="navbar navbar-fixed-top navbar-static-top navbar-inverse">
		<div class="navbar-inner">
			<div class="container">
				<button type="button" class="btn btn-navbar" data-toggle="collapse"
					data-target=".nav-collapse">
					<span class="icon-bar"></span> <span class="icon-bar"></span> <span
						class="icon-bar"></span>
				</button>

				<!-- <a class="brand" href="#">Sig CT</a> -->
				<div class="nav-collapse collapse">
					<ul class="nav">
						<li class="dropdown active"><a href="#"
							class="dropdown-toggle" data-toggle="dropdown"><strong>Identify</strong><b
								class="caret"></b></a>
							<ul class="dropdown-menu">
								<li class="active"><a href="#">Generation</a></li>
								<li><a href="#">Location</a></li>
								<li><a href="#">Application</a></li>
								<li><a href="/CombPublication">Publication</a></li>
								<!--  <li class="divider"></li>
        <li class="nav-header">Nav header</li> -->
							</ul></li>
					</ul>

					<ul class="nav pull-right">
						<li><a href="/index.html"><strong>Home</strong></a></li>
						<li><a href="/group.html"><strong>Group</strong></a></li>
						<li><a href="/contact.html"><strong>Contact</strong></a></li>
						<li><a href="#"><strong>About</strong></a></li>
					</ul>
				</div>
			</div>
		</div>
	</div>


	<div class="container">
		<br /> <br /> <br /> <br />

		<!--  <ul class="nav nav-tabs">
       <li class="active"><a href="#">Generation</a></li>
       <li><a href="#">Location</a></li>
       <li><a href="#">Application</a></li>
       <li><a href="/CombPublication">Publication</a></li>
    </ul> -->

		<div class="row-fluid">
			<!-- step description -->
			<div class="span2">
				<ul class="nav nav-list">
					<li class="nav-header">Step</li>
					<li class="active"><a href="#">Setting</a></li>
					<li>Executing</li>
				</ul>
			</div>

			<div class="span10">
				<!-- input form -->
				<form action="start" method="post">
					<div class="well weill-large">
						<fieldset>
							<legend>Configurations</legend>
							<p>
								<label for="param_name">Parameter names</label> <input
									id="param_name" type="text" class="span5"
									placeholder="e.g 'p1 p2 p3 p4'" name="param_name" />
							</p>
							<p>
								<label for="param_value">Parameter values</label>
								<textarea class="span5" id="param_value" name="param_value"
									rows="4" cols="300" wrap="off" AUTOCOMPLETE=OFF></textarea>
							</p>
					</div>
					</fieldset>
					<div class="well weill-large">
						<fieldset>
							<legend>Failed Test Configuration</legend>
							<p>
								<input id="fail_test" type="text" class="span5"
									placeholder="v1 v2 v3 v4" name="fail_test" AUTOCOMPLETE=OFF />
							</p>
					</div>
					</fieldset>
					<div class="well weill-large">
						<fieldset>
							<legend>Choose Approach</legend>
							<p>
								<label for="approach">approach</label> <select id="approach"
									name="approach">
									<option>Aug_TRT</option>
									<option>TRT</option>
									<option>FIC_BS</option>
									<option>OFOT</option>
								</select>
							</p>
							<p class="submit">
								<button id="startButton" class="btn btn btn-primary">Start</button>
							</p>
						</fieldset>
					</div>
				</form>
			</div>
		</div>

		<hr>
		<div class="footer">
			<p>&copy; Company 2012</p>
		</div>

	</div>
<script src="js/jquery-1.8.3.js"></script>
	<script src="script/bootstrap.js"></script>
	<script src="script/bootstrap-modal.js"></script>
</body>
</html>