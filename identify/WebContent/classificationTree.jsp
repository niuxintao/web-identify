<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>

<%@ taglib prefix="s" uri="/struts-tags"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<html>

<head>

<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">

<title>Hi</title>

</head>

<body>
	<form id="formElem" name="formElem" action="" method="post">
		<h1>TestSuite</h1>
		<textarea class="jq_watermark" id="coveringArray" name="coveringArray"
			rows="10" cols="20"></textarea>

		<h1>execute outcome</h1>
		<textarea class="jq_watermark" id="outCome" name="outCome" rows="10"
			cols="2"></textarea>

		<h1>result</h1>
		<textarea class="jq_watermark" id="result" name="result" rows="10"
			cols="20"></textarea>
		<p class="submit">
			<button id="startButton">analysis</button>
		</p>
	</form>
</body>

</html>