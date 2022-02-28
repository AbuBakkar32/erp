function convertoToDecimal(amount){
	return (amount * 1.65).toFixed(2);
}

function convertToKatha(amount){
	return (amount / 1.65).toFixed(2);
}

$(document).ready(function(){
	
	$("input[name='xlandqtyg'], input[name='xlandqtydsg'],  input[name='xlandqtyksg'],input[name='xlandqtyn'], input[name='xlandqtydsn'], input[name='xlandqtyksn']").off('blur').on('blur', function(e){
		if($(this).val() == '' || isNaN($(this).val())){
			$(this).val(0);
		}
		
		calculateGrossLandAmount();
		calculateNetLandAmount();
	});

	$('#xlandunitg').off('change').on('change', function(e){
		e.preventDefault();
		calculateGrossLandAmount();
	})
	function calculateGrossLandAmount(){
		var amount =parseFloat($("input[name='xlandqtyg']").val());
		var unit = $('#xlandunitg').val();
		if(unit === 'Decimal'){
			$('input[name="xlandqtydsg"]').val(amount);
			$('input[name="xlandqtyksg"]').val(convertToKatha(amount));
		} else {
			$('input[name="xlandqtydsg"]').val(convertoToDecimal(amount));
			$('input[name="xlandqtyksg"]').val(amount);
		}
		if ($('.xlandqtydsg').val() == '' || isNaN($('.xlandqtydsg').val())){
			$('.xlandqtydsg').val('0.00');
		}
		if ($('.xlandqtyksg').val() == '' || isNaN($('.xlandqtyksg').val())){
			$('.xlandqtyksg').val('0.00');
		}
	}
	$('#xlandunitn').off('change').on('change', function(e){
		e.preventDefault();
		calculateNetLandAmount();
	})
	function calculateNetLandAmount(){
		var amount =parseFloat($("input[name='xlandqtyn']").val());
		var unit = $('#xlandunitn').val();
		if(unit === 'Decimal'){
			$('input[name="xlandqtydsn"]').val(amount);
			$('input[name="xlandqtyksn"]').val(convertToKatha(amount));
		} else {
			$('input[name="xlandqtydsn"]').val(convertoToDecimal(amount));
			$('input[name="xlandqtyksn"]').val(amount);
		}
		if ($('.xlandqtydsn').val() == '' || isNaN($('.xlandqtydsn').val())){
			$('.xlandqtydsn').val('0.00');
		}
		if ($('.xlandqtyksn').val() == '' || isNaN($('.xlandqtyksn').val())){
			$('.xlandqtyksn').val('0.00');
		}
	}

	calculateNetLandAmount();
	calculateGrossLandAmount();
})