<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>

<%@ taglib prefix="s" uri="/struts-tags"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<html>

<head>

<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">

<title>Hi</title>
<script type="text/javascript" src="script/jquery.min.js"></script>

<script type="text/javascript" src="script/jquery.watermark.min.js"></script>
<script type="text/javascript" src="script/jquery.ibutton.js"></script>
<script type="text/javascript" src="script/jquery.easing.1.3.js"></script>
<script type="text/javascript" src="script/jquery.metadata.js"></script>
<script type="text/javascript">
	$(document).ready(function() {
		// 直接把onclick事件写在了JS中
		$("#analysis").click(function() {
			// 序列化表单的值
			var param_name1 = $("#coveringArray").val();
			var param_value1 = $("#outCome").val();

			var params = {
				coveringArray : param_name1,
				outCome : param_value1,
			};
			$.ajax({
				// 后台处理程序
				url : "analysis.action",
				// 数据发送方式
				type : "post",
				// 接受数据格式
				dataType : "json",
				// 要传递的数据
				data : params,
				// 回传函数
				success : update,
				error : function(result) {
					alert(result.responseText);
				}
			});
		});

	});
	function update(result) {
		var json = eval("(" + result + ")");
		$("#result").empty();
		$("#result").html(json.result);
	}
</script>
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
	font-size: 36px;
	text-shadow: 1px 1px 1px #fff;
	padding: 20px;
}
</style>
</head>

<body>
	<h1>identification for whole array</h1>
		<div style="">
			<div style="float: left;">
				<h1>TestSuite</h1>

				<textarea class="jq_watermark" id="coveringArray"
					name="coveringArray" rows="10" cols="20"></textarea>
			</div>
			<div style="float: left;">
				<h1>execute outcome</h1>

				<textarea class="jq_watermark" id="outCome" name="outCome" rows="10"
					cols="2"></textarea>
			</div>
			<div style="clear: both"></div>
		</div>
		<div style="float: left;">
			<p class="submit">
				<button id="analysis">analysis</button>
			</p>
			<h1>result</h1>
			<textarea class="jq_watermark" id="result" name="result" rows="10"
				cols="20"></textarea>
		</div>
	
</body>

</html>