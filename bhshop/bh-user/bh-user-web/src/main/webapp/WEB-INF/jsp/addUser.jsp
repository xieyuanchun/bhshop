<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
</head>
<body>
	<div>
		<table
			style="border-width: 1px; border-color: #5A5E61; padding: 0px; margin: 0px; font-size: 13px; color: #5A5E61;">
			<tr>
				<td colspan="2" style="text-align: center">注册</td>
			</tr>
			<tr>
				<td style="text-align: right;">Id</td>
				<td><input id="TxtId" type="text" /></td>
			</tr>
			<tr>
				<td style="text-align: right;">Seq</td>
				<td><input id="TxtSeq" type="text" /></td>
			</tr>
			<tr>
				<td style="text-align: right;">Name</td>
				<td><input id="TxtUserName" type="text" /></td>
			</tr>
			<tr>
				<td style="text-align: right;">Mobile</td>
				<td><input id="TxtMobile" type="text" /></td>
			</tr>
			<tr>
				<td></td>
				<td style="text-align: center"><input id="BtnSure"
					type="submit" value="注册" onclick="adduser()" /> <span id="SPmsg"
					style="color:Red;"></span></td>
			</tr>
		</table>
	</div>
	<script type="text/javascript" src="js/jquery-1.11.0.min.js"></script>
	<script type="text/javascript" src="js/AddUser.js"></script>
</body>
</html>
