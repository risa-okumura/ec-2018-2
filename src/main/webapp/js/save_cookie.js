
$(function(){
	$(document).ready(function() {
		
		var id = $(location).attr('pathname').split('/')[3];
		const xsrf = $.cookie('XSRF-TOKEN');
		console.log(id);
	
//		$("#output_data").text("");
		$.ajax({
			type : "POST",
			url  : "/itemHistory",
			data : {"id":id},
			headers: { 'X-XSRF-TOKEN': xsrf },
//			dataType: "json",
			success : function(data){
					success(data);
					},
			error	: function(XMLHttpRequest, textStatus, errorThrown){
					error(XMLHttpRequest,textStatus,errorThrown);
					}
		});
	});
});

function success(data){
	console.log(data);
	var item = data;
	var cookie_name = "item";
	var viewed_items = [];
//	var viewed_items = Array();
	
	if($.cookie(cookie_name)){
		 viewed_items = $.cookie(cookie_name).split(",");
	  }
	
	 // 重複してたら以前のデータを削除し、配列の最後に最新のデータを追加.
	 if($.inArray(item, viewed_items) > 0){
		 var num = $.inArray(item,viewed_items);
		 viewed_items.splice(num,1);
		 viewed_items.push(item);
	  }else{
		  viewed_items.push(item);
	  }
	 
	// 5個以上ならば1つ削除.
	  if (viewed_items.length > 5){
		  viewed_items.shift();
		  console.log("一件削除");
	  }

	 // 配列をクッキ―に保存.
	 $.cookie(cookie_name,viewed_items);
	console.log(viewed_items);
}

function error(XMLHttpRequest, textStatus, errorThrown) {
    alert("error:" + XMLHttpRequest);
    alert("status:" + textStatus);
    alert("errorThrown:" + errorThrown);
}



