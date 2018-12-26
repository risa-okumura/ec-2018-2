$(function(){
	
	$(document).ready(function(){
		$("#card_display").hide();
	});

	$(".pay").change(function(){
		card();
	});
	
	function card(){
		var radioVal = $('.pay:checked').val();
		if(radioVal==2){
			$("#card_display").show();
		}else{
			$("#card_display").hide();
		}
		
	};
	
});
