<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>

<%@ taglib prefix="s" uri="/struts-tags"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01//EN" "http://www.w3.org/TR/html4/strict.dtd">
<html>
<head>
<s:head />
<title>Identify failure-inducing combinations</title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<meta name="description"
	content="Identify failure-inducing combinations" />
<meta name="keywords"
	content="jquery, form, sliding, usability, css3, validation, javascript" />
<link rel="stylesheet" href="css/style.css" type="text/css"
	media="screen" />
<link type="text/css" href="css/docs.css" rel="stylesheet" media="all" />
<link type="text/css" href="css/jquery.ibutton.css" rel="stylesheet"
	media="all" />
<link type="text/css" href="css/identify.css" rel="stylesheet"
	media="all" />

<style type="text/css">
label.label {
	display: block;
	width: 120px;
	float: left;
	padding-top: 5px;
	font-weight: bold;
	height: 30px;
}

input.button {
	margin-top: 5px;
}

/* shows overwriting styles via CSS */
#css .ibutton-container {
	width: 175px;
}

#css .ibutton-container .ibutton-handle {
	width: 5px;
}
</style>
<script type="text/javascript" src="script/jquery.min.js"></script>

<script type="text/javascript" src="script/jquery.watermark.min.js"></script>
<script type="text/javascript" src="script/jquery.ibutton.js"></script>
<script type="text/javascript" src="script/jquery.easing.1.3.js"></script>
<script type="text/javascript" src="script/jquery.metadata.js"></script>
<script type="text/javascript" src="script/slidingReport.js"></script>
<script type="text/javascript" src="script/sliding.form.js"></script>

<script type="text/javascript">
<!--//
	function debug() {
		if (window.console && window.console.log)
			console.log(arguments);
	}
	// on DOM ready
	$(document).ready(function() {
		$(".group").iButton({
			init : function() {
				debug("init", arguments);
			},
			change : function() {
				debug("change", arguments);
			},
			click : function() {
				debug("click", arguments);
			},
			disable : function() {
				debug("disable", arguments);
			},
			destroy : function() {
				debug("destroy", arguments);
			}
		});
	});
	//
</script>
<script type="text/javascript">
	$(document).ready(function() {
		// 直接把onclick事件写在了JS中
		$("#startButton").click(function() {
			// 序列化表单的值
			var param_name1 = $("#param_name").val();
			var param_value1 = $("#param_value").val();
			var fail_test1 = $("#fail_test").val();
			var approach1 = $("#approach").val();

			var params = {
				param_name : param_name1,
				param_value : param_value1,
				fail_test : fail_test1,
				approach : approach1
			};
			$.ajax({
				// 后台处理程序
				url : "start.action",
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
				cas[i] = "<p class= 'pass'>" + json.testCases[i] + "<br /></p>";
			else if (ispass == "-1") /* failed */
				cas[i] = "<p class= 'fail'>" + json.testCases[i] + "<br /></p>";
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
			$("#candi").empty();
			$("#candi").html("failure-inducing");

			var height1 = $('#testUnderRun').height() + $('#lay').height();
			var height2 = $('#report').height();
			$('#lay').stop().animate({
				marginTop : '-' + height1 + 'px'
			}, 500, function() {
			});
			$('#wrapper').stop().animate({
				height : height2 + 'px'
			}, 500, function() {
			});
		}

	}
</script>
</head>
<style>
span.reference {
	position: fixed;
	left: 5px;
	top: 5px;
	font-size: 10px;
	text-shadow: 1px 1px 1px #fff;
}

span.reference a {
	color: #555;
	text-decoration: none;
	text-transform: uppercase;
}

span.reference a:hover {
	color: #000;
}

h1 {
	color: #ccc;
	font-size: 36px;
	text-shadow: 1px 1px 1px #fff;
	padding: 20px;
}
</style>
<body>
<li><a href="classificationTree.jsp">CTA</a></li>
	<div id="content">
		<h1>Identify failure-inducing combinations</h1>
		<div id="wrapper">
			<div id="lay">
				<div id="steps">
					<form id="formElem" name="formElem" action="" method="post">
						<fieldset class="step">
							<legend>Configurations</legend>
							<p>
								<label for="param_name">Parameter names</label> <input
									id="param_name" class="jq_watermark" title="p1 p2 p3 p4"
									name="param_name" AUTOCOMPLETE=OFF />
							</p>
							<p>
								<label for="param_value">Parameter values</label>
								<textarea class="jq_watermark" id="param_value"
									name="param_value" rows="4" cols="20" wrap="off"
									title="1 2 3 4<br />pass fail<br />1000 2000 3000<br />char string integer bit"
									AUTOCOMPLETE=OFF></textarea>
							</p>
						</fieldset>
						<fieldset class="step">
							<legend>Failed Test Configuration</legend>
							<p>
								<label for="fail_test">Failed Test Configuration</label> <input
									id="fail_test" class="jq_watermark" title="v1 v2 v3 v4"
									name="fail_test" AUTOCOMPLETE=OFF />
							</p>

						</fieldset>
						<fieldset class="step">
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
								<button id="startButton">Start</button>
							</p>
						</fieldset>
					</form>
				</div>
				<div id="navigation" style="display: none;">
					<ul>
						<li class="selected"><a href="#">Configuration</a></li>
						<li><a href="#">Failed Test Configuration</a></li>
						<li><a href="#">Approach choose</a></li>
					</ul>
				</div>
			</div>

			<div id="run">
				<div id="testUnderRun">
					<fieldset class="step2">
						<legend>Test Configuration Under Test</legend>
						<div>
							<div class="testConfiguration">
								<div id="simple" class="group" style="float: right;">
									<div class="row">
										<input type="checkbox" id="checkRun" name="run"
											class="{labelOn: 'PASS', labelOff: 'FAIL'}" />
									</div>
								</div>
								<div id="show"></div>
							</div>

						</div>
						<div id="bnex">
							<button id="next">Next</button>
						</div>
					</fieldset>
				</div>
				<div id="report">
					<div id="flsteps">
						<fieldset class="flstep">
							<legend>failure-inducing Combinations</legend>
							<div class="plike">
								<label for="candidate-inducing" id="candi">candidate-inducing</label>
								<div class="showlist" id="tuples"></div>
							</div>
						</fieldset>
						<fieldset class="flstep">
							<legend>Generated Test Configurations</legend>
							<div class="plike">
								<label for="all" id="total"></label>
								<div class="showlist" id="cases"></div>
							</div>
						</fieldset>
					</div>
					<div id="flnavigation" style="display: none;">
						<ul>
							<li class="selected"><a href="#">failure-inducing
									Combinations</a></li>
							<li><a href="#">Generated Test Configurations</a></li>
						</ul>
					</div>
				</div>
			</div>

		</div>
	</div>
</body>
</html>