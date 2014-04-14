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


<title>Execute</title>
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
<!--//
	function debug() {
		if (window.console && window.console.log)
			console.log(arguments);
	}
	//
	$(document).ready(function() {
		// 直接把onclick事件写在了JS中
		$("#next").click(function() {
			// 序列化表单的值
			if ($("#checkRun").attr("checked") == true)
				run1 = "on";
			else
				run1 = "down";
			var params = {
				run : run1
			};
			$.ajax({
				// 后台处理程序
				url : "generate.action",
				// 数据发送方式
				type : "post",
				// 接受数据格式
				dataType : "json",
				// 要传递的数据
				data : params,
				// 回传函数
				success : updateStart,
				error : function(result) {
					alert(result.responseText);
				}
			});
		});
	});
	function updateStart(result) {
		var json = eval("(" + result + ")");
		var str = json.testCase;

		$("#show").fadeOut("normal", function() {
			var div = $("<div id='show'>" + str + "</div>").hide();
			$(this).replaceWith(div);
			$('#show').fadeIn("slow");
		});

		$("#tuples").empty();
		var tups = new Array();
		for ( var i in json.bugtuples) {
			tups[i] = "" + json.bugtuples[i] + "<br />";
		}
		$.each(tups, function(idx, val) {
			$('#tuples').append(val);
		});

		$("#cases").empty();
		var cas = new Array();
		for ( var i in json.testCases) {
			var ispass = json.passorfail[i];
			if (ispass == "1") /* passed  */
				cas[i] = "<p>"+"<font color='#008000'>√&nbsp;&nbsp;</font>" + json.testCases[i] +"</p>";
			else if (ispass == "-1") /* failed */
				cas[i] = "<p>"+ "<font color='#CC3333'>×&nbsp;&nbsp;</font>"+json.testCases[i] + "</p>";
			else
				cas[i] = "" + json.testCases[i] + "<br />";

		}
		$.each(cas, function(idx, val) {
			$('#cases').append(val);
		});

		$("#total").empty();
		$("#total").html("total:" + json.testCases.length);

		if (!str) { //the identify is over
			alert("the identify process is completed!");
			$("#testExecute").hide("slow");
		}

	}
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
		<br />
		<br /> <br />
		<br />

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
					<li>Setting</li>
					<li class="active"><a href="#">Executing</a></li>
				</ul>
			</div>

			<div class="span10">
				<!-- input form -->
				<div id="testExecute" class="well weill-large">
					<fieldset>
						<legend> Test Configuration Under Test</legend>
						<div>
							<div class="testConfiguration">
								<div id="simple" class="group" style="float: right;">
									<div class="row">
									 <input id = "checkRun" type="radio" name="run" value = 1 checked="checked"> pass
									 <input id = "checkRunFail" type="radio" name="run" value = 2> fail
									</div>
								</div>
								<div id="show">
									<s:property value="nextTestCase" />
								</div>
							</div>
						</div>
						<div id="bnex" style="float: left;">
							<button id="next" class="btn btn btn-primary">Next</button>
						</div>
						<div style="clear: both;"></div>
					</fieldset>
				</div>

				<div class="well weill-large">
					<div id="report">
						<div id="flsteps">
							<fieldset>
								<legend>failure-inducing Combinations</legend>
								<div class="plike">
									<label for="candidate-inducing" id="candi">candidate-inducing</label>
									<div class="showlist" id="tuples"></div>
								</div>
							</fieldset>
							<fieldset>
								<legend>Generated Test Configurations</legend>
								<div class="plike">
									<label for="all" id="total"></label>
									<div class="showlist" id="cases"></div>
								</div>
							</fieldset>
						</div>
					</div>
				</div>

	
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