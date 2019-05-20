// 注册
function adduser() {
	var Id = $("#TxtId").val();
	var Seq = $("#TxtSeq").val();
	var Name = $("#TxtUserName").val();
	var Mobile = $("#TxtMobile").val();
	// 提示
	var SPmsg = document.getElementById("SPmsg");
	var localhost = location.protocol + '//' + location.host + "/addUser1.html";
	$.ajax({
		type : "POST", // 提交方式
		contentType : "application/json; charset=utf-8", // 内容类型
		dataType : "json", // 类型
		url : localhost,
		data : JSON.stringify({
			"id" : Id,
			"seq" : Seq,
			"name" : Name,
			"mobile" : Mobile
		}),
		success : function(result) {
			// console.info(result);
			if (result.status == 200) {
				SPmsg.innerText = "操作成功!";
			} else {
				SPmsg.innerText = "操作失败!";
				return;
			}
		},
		error : function(json) {
			SPmsg.innerText = "操作失败，请刷新后重试";
			return;
		}
	});
}
