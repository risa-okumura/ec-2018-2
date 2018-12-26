$(function() {
	$(document).ready(function() {
		var id = $(location).attr('pathname').split('/')[3];
		var item_name = $('#item_name').text();
		console.log(item_name);
		var cookie_id = 'recently_viewed_id';
		var cookie_name = 'recently_viewed_name';
		var viewed_ids = [];
		var viewed_names = [];
		var delete_item = false;
		$.cookie.defaults.path = "/";
		
		if($.cookie(cookie_id) || $.cookie(cookie_name)){
			 viewed_ids = $.cookie(cookie_id).split(",");
			 viewed_names = $.cookie(cookie_name).split(",");
		  }
		
		 // 重複していなければ、itemを配列に追加
//		 if($.inArray(id, viewed_ids)<0){
//			 viewed_ids.push(id);
//		  }
		 
		 if($.inArray(id, viewed_ids) > 0 || $.inArray(item_name, viewed_names) > 0){
			 var id_num = $.inArray(id,viewed_ids);
			 var name_num = $.inArray(item_name,viewed_names);
			 viewed_ids.splice(id_num,1);
			 viewed_names.splice(name_num,1);
			 viewed_ids.push(id);
			 viewed_names.push(item_name);
		  }else{
			  viewed_ids.push(id);
			  viewed_names.push(item_name);
		  }
		 
		// 5個以上ならば1つ削除
		  if (viewed_ids.length > 5){
			  viewed_ids.shift();
			  viewed_names.shift();
		  }

		 // 配列をクッキ―に保存
		 $.cookie(cookie_id,viewed_ids);
		 $.cookie(cookie_name,viewed_names);
		console.log(viewed_ids);
		console.log(viewed_names);
		
//		var show_item_history = [];
//		var URL = $(location).attr('pathname')
//			
//			if( !IsArrayExists(show_item_history, URL)){
//				show_item_history.push(URL);
//			}
//			$.cookie("show_item_history[i]", URL, {
//				expires : 14,
//				path : '/'
//			});
//
//		// var valueName = $.cookie("Name");
//		// var valueAge = $.cookie("Age");
//		// console.log(valueName);
//
//		console.log("書き込みしました");
	});
});
