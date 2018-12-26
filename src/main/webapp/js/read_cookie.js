$(function(){
	
	$(document).ready(function () {
		
//		var show_id_history = $.cookie("item").split(',');
//		console.log(show_id_history);
		
		
		var show_id_history = $.cookie("recently_viewed_id").split(',');
		var show_name_history = $.cookie("recently_viewed_name").split(',');
		console.log($.cookie("recently_viewed_id"));
		console.log($.cookie("recently_viewed_name"));
		
		if(show_id_history == null){
			$("#sample").text("閲覧履歴がありません");
		}else{
			
		
		var table = $("<table class='table table-striped table table-bordered'>")
		var tbody = $("<tbody>")

		tbody.append("<tr>")
		
		for(var i = 0 ; i < show_id_history.length ; i++){
			
				
			var id = show_id_history[i];
			var name = show_name_history[i]
			
			tbody.append("<td>");
			tbody.append('<div class="carousel-grid col-lg-3 col-md-4 col-sm-12">');
			tbody.append('<a href="/ShowItemDetail/detail/' + id + '"><div class="img-block"><img src="http://172.16.0.16/media_2017/image/first_ec/' + id + '.jpg" class="img-responsive img-rounded" width="200" height="200"></div></a>');
			tbody.append("<br>");
			tbody.append(name);
			tbody.append("</td>");
			tbody.append("</div>");
			
			
		}
		tbody.append("</tr>")
		table.append(tbody);

		$("#sample").html(table);

		}

		
	});
	
});