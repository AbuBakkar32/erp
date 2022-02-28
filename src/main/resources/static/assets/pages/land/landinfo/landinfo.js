function convertoToDecimal(amount){
	return (amount * 1.65).toFixed(2);
}

function convertToKatha(amount){
	return (amount / 1.65).toFixed(2);
}

$(document).ready(function(){
	
	$("input[name='xlandqty'], input[name='xlanddedroad'],  input[name='xlanddedother'],input[name='xtotded'],input[name='xtotrem'], input[name='xlandnetqty'], input[name='xroadred'], input[name='xotherred'], input[name='xlandqtyd'], input[name='xlandqtyk'], input[name='xlandunit'], input[name='xamtar'], input[name='xamtrv']").off('blur').on('blur', function(e){
		if($(this).val() == '' || isNaN($(this).val())){
			$(this).val(0);
		}
		
		calculate();
		calculateLandAmount();
		calbalance()
	});

	$('#xlandunit').off('change').on('change', function(e){
		e.preventDefault();
		calculateLandAmount();
		calculate()
	})
	function calculateLandAmount(){
		var amount =parseFloat($("input[name='xlandqty']").val());
		var unit = $('#xlandunit').val();
		if(unit === 'Decimal'){
			$('input[name="xlandqtyd"]').val(amount);
			$('input[name="xlandqtyk"]').val(convertToKatha(amount));
		} else {
			$('input[name="xlandqtyd"]').val(convertoToDecimal(amount));
			$('input[name="xlandqtyk"]').val(amount);
		}
	}

	function calbalance(){
		if ($('input[name="xamtar"]').val() == ''){
			$('input[name="xamtar"]').val('0.00');
		}
		if ($('input[name="xamtrv"]').val() == ''){
			$('input[name="xamtrv"]').val('0.00');
		}
		var xamtar =parseFloat($("input[name='xamtar']").val());
		var xamtrv = parseFloat($("input[name='xamtrv']").val());
		
		if(xamtrv < 0){
			$("input.xamtrv-display").val('(' + Math.abs(xamtrv) +')')
		}
		
		var balance = (xamtar + xamtrv).toFixed(2);
		$('.balance').val(balance);

		if ($('.balance').val() == '' || isNaN($('.balance').val())){
			$('.balance').val('0.00');
		}
		
		if($('.balance').val()< 0){
			$("input.balance").val('(' + Math.abs(balance) +')')
		}

	}

	function calculate(){
		var xlandqty = parseFloat($("input[name='xlandqty']").val());
		var xlanddedroad =parseFloat($("input[name='xlanddedroad']").val());
		var xlanddedother =parseFloat($("input[name='xlanddedother']").val());
		var xtotded =parseFloat($("input[name='xtotded']").val());
		var xtotrem =parseFloat($("input[name='xtotrem']").val());
		var xlandqtyd = parseFloat($("input[name='xlandqtyd']").val());
		var net1 = (xlandqtyd * xlanddedroad)/100;
		var net2 = (xlandqtyd * xlanddedother)/100;
		var tot = (net1 + net2).toFixed(2);
		var rem = (xlandqtyd - tot).toFixed(2);
		$('input[name="xtotded"]').val(tot);
		$('input[name="xtotrem"]').val(rem);
	}

	calculate();
	calculateLandAmount();
	calbalance()
})