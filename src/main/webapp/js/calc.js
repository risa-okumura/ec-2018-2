$(function(){
	
	$('.size').change(function(){
		calc();
	});
	
	$('.toppingList').change(function() {
		calc();
	});
	
    $('.form-control').change(function() {
    	calc();
    });
 
    function calc() {
    	var str = $('.size:checked').val();
    	var cnt = $('.toppingList:checkbox:checked').length;
    	var quantity = $('.form-control option:selected').val();
    	var priceM = $('#priceM').val();
    	var priceL = $('#priceL').val();
    	var toppingM = $('#toppingM').val();
    	var toppingL = $('#toppingL').val();
    	var total = 0;
    	
    	if( str == 'M' ){
    		total = ( Number(priceM) + Number(cnt * toppingM) ) * quantity;
    	}else{
    		total = ( Number(priceL) + Number(cnt * toppingL) ) * quantity;
    	}
    	$('#total').text(total.toLocaleString());
    	};
    	
});