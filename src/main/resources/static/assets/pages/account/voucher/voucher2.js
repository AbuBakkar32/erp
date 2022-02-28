$(document).ready(function(){

	// init select2 plugin
	$('.select2').select2();

	// initial focus
	$('#xtrn').focus();

	// global variables
	var trigger = false;
	var accountData = [];
	var storeData = [];
	var selectedStores = [];
	var subAccountsData = [];
	var rowIndexCounter = 0;
	function initRowCounter(){
		rowIndexCounter = $("#voucherdetailtable tbody").find('tr').length;
	}
	initRowCounter();

	// initialize selec2 dropdown
	function initSelect2Dropdown(){
		$('#voucherdetailbody').find('.select2').select2();
	}
	initSelect2Dropdown();

	// init project stores
	$('.project-xwh').on('change', function(e){
		e.preventDefault();
		initSelectedProjectStore();
		updateAllRowStores();
	})
	function initSelectedProjectStore(){
		selectedStores = [];
		var selectedProject = $('.project-xwh').val();
		$.each(storeData, function(i, data){
			if(data.xproject == selectedProject){
				selectedStores.push(data);
			}
		});
	}
	initSelectedProjectStore();

	function updateAllRowStores(){
		$("#voucherdetailbody").find('.details-store').find('option').remove().end().append('<option value="">-- Select --</option>');
		$.each(selectedStores, function(i, list){
			$("#voucherdetailbody").find('.details-store').append($('<option>').val(list.xcode).text(list.xcode + ' - ' + list.xcodename));
		});

		// loop throught row with previous saved data
		var rows = $('#voucherdetailbody').find('tr');
		$.each(rows, function(i, row){
			var xwh = $(row).data('xwh');
			var options = $(row).find('.details-store option');
			$.each(options, function(j, op){
				if($(op).attr('value') == xwh){
					$(op).prop('selected', true);
				}
			})
		})
	}

	// initialize accountData 
	function initAccountData(){
		loadingMask2.show();
		$.ajax({
			url : getBasepath() + '/account/voucher/preloadeddata',
			type : 'GET',
			success : function(data) {
				loadingMask2.hide();
				accountData = data.acmstList;
				storeData = data.stores;
				subAccountsData = data.subAccounts;
				initSelectedProjectStore();
				console.log({data});
			},
			error : function(jqXHR, status, errorThrown){
				showMessage(status, "Something went wrong .... ");
				loadingMask2.hide();
			}
		})
	}
	initAccountData();

	// Xcredit input field blur event
	function initCreditBlurEvent(){
		$('#voucherdetailbody').find('.xcredit').keydown(function(e) {
			var code = e.keyCode || e.which;
			//console.log(code);
			if (code === 9) {  
				if(rowIndexCounter - $(this).parents('tr').data('rowindex') > 0) return;
				generateRow();
			}
		});
	}
	initCreditBlurEvent();

	// Generate new Row in detail table
	function generateRow(){
		rowIndexCounter++;

		var accountsdropdown = 			'<div class="form-group">' +
											'<div class="row">' +
												'<div class="col-md-12">' +
													'<select class="form-control select2 xacc" name="xacc">';

		$.each(accountData, function(i, el){
			accountsdropdown += 						'<option value='+ el.xacc +'>'+ el.xacc + ' - ' + el.xdesc +'</option>';
		})

			accountsdropdown += 					'</select>'+
												'</div>'+
											'</div>'+
										'</div>';

		var storeDropdown = '<div class="form-group">' +
								'<div class="row">' +
									'<div class="col-md-12">' +
										'<select class="form-control select2 details-store" name="xwh">' + 
											'<option value=""> -- Select -- </option>';

		$.each(selectedStores, function(i, el){
			storeDropdown += 				'<option value='+ el.xcode +'>'+ el.xcode + ' - ' + el.xcodename +'</option>';
		})

			storeDropdown += 			'</select>'+
									'</div>'+
								'</div>'+
							'</div>';

		var trow = 	'<tr data-rowindex="'+ rowIndexCounter +'">'+ 
						'<td style="text-align: center;">' + rowIndexCounter + '</td>'+
						'<td>' + accountsdropdown +'</td>' +
						'<td>' + 
							'<div class="form-group">' +
								'<select class="form-control select2 xsub">'+
									'<option value="">-- Select --</option>'+
								'</select>'+
							'</div>'+
						'</td>' +
						'<td>' + storeDropdown +'</td>' +
						'<td>' + 
							'<div class="form-group">'+
								'<textarea rows="1" cols="" class="form-control xlong"></textarea>'+
							'</div>'+
						'</td>' +
						'<td>' + 
							'<div class="form-group">'+
								'<input type="number" class="form-control xdebit" value="0.00" min="0"/>'+
							'</div>'+
						'</td>' +
						'<td>' + 
							'<div class="form-group">' +
								'<input type="number" class="form-control xcredit" value="0.00" min="0"/>' + 
							'</div>' +
						'</td>' +
						'<td style="text-align: center;">' + 
							'<button type="button" class="btn btn-danger row-remover"><i class="fas fa-trash"></i></button>'
						'</td>'+
					'</tr>';
		
		$('#voucherdetailbody').append(trow);

		initSelect2Dropdown();
		subAccountAjaxCallInit();
		initCreditBlurEvent();
		rowRemove();
		reserialTableRow();
		$('tr[data-rowindex="'+ rowIndexCounter +'"]').find('.xacc').trigger("change");
	}

	// Remove row
	function rowRemove(){
		$('.row-remover').off('click').on('click', function(e){
			e.preventDefault();
			$(this).parents('tr').remove();
			rowIndexCounter--;

			reserialTableRow();
		})
	}
	rowRemove();

	// sub account search
	function subAccountAjaxCallInit(){
		$('.xacc').on('change', function(e){
			e.preventDefault();
			var xacc = $(this).val();
			var parentEl = $(this).parents('tr');
			$(parentEl).find('.xsub').find('option').remove().end().append('<option value="">-- Select --</option>');
			$.each(subAccountsData, function(i, list){
				if(list.xacc == xacc){
					$(parentEl).find('.xsub').append($('<option>').val(list.xsub).text(list.xsub + ' - ' + list.xorg));
				}
			})
		})
	}
	subAccountAjaxCallInit();

	// Event from header to generate new row
	$('textarea[name=xlong]').keydown(function(e) {
		var code = e.keyCode || e.which;
		if (code === 9) {  
			if(rowIndexCounter > 0) return;
			$('#voucherdetailtable').removeClass('nodisplay');
			generateRow();
		}
	});

	// Row creator button event
	$('.row-creator').off('click').on('click', function(e){
		e.preventDefault();
		generateRow();
	});

	$('.voucher-confirm-btn').off('click').on('click', function(e){
		e.preventDefault();
		submitForm();
	});

	// Form submit
	function submitForm(){
		var formValues = $('.voucher-header-form').serializeArray();

		var acheader = {};

		$.each(formValues, function(i, data){
			acheader[data.name] = data.value;
		});

		var acdetails = [];

		var rows = $("#voucherdetailtable tbody").find('tr');
		$.each(rows, function(i, row){
			var acdetail = {};
			acdetail.xacc = $(row).find('.xacc').val();
			acdetail.xsub = $(row).find('.xsub').val();
			acdetail.xlong = $(row).find('.xlong').val();
			acdetail.xdebit = $(row).find('.xdebit').val();
			acdetail.xcredit = $(row).find('.xcredit').val();
			acdetail.xwh = $(row).find('.details-store').val();
			acdetails.push(acdetail);
		})

		var voucherMaster = {};
		voucherMaster.acheader = acheader;
		voucherMaster.acdetails = acdetails;

		loadingMask2.show();
		$.ajax({
			url : getBasepath() + '/account/voucher/savevoucher',
			type : 'POST',
			dataType : 'json', 
			data: JSON.stringify(voucherMaster),
			beforeSend: function(xhr) {
				xhr.setRequestHeader("Accept", "application/json");
				xhr.setRequestHeader("Content-Type", "application/json");
			},
			success : function(data) {
				loadingMask2.hide();
				if(data.status == 'SUCCESS'){
					showMessage(data.status.toLowerCase(), data.message);
					if(data.triggermodalurl){
						modalLoader(getBasepath() + data.triggermodalurl, data.modalid);
					} else {
						if(data.reloadurl){
							doSectionReloadWithNewData(data);
						} else if(data.redirecturl){
							setTimeout(() => {
								window.location.replace(getBasepath() + data.redirecturl);
							}, 1000);
						}
					}
				} else {
					showMessage(data.status.toLowerCase(), data.message);
				}
			},
			error : function(jqXHR, status, errorThrown){
				showMessage(status, "Something went wrong .... ");
				loadingMask2.hide()
			}
		})
	}

	function reserialTableRow(){
		var rows = $("#voucherdetailtable tbody").find('tr');
		$.each(rows, function(i, elem){
			$(elem).find('td:first').text(i + 1);
		});
	}
	reserialTableRow();

	// Submit using key ctrl + Entr
	$(document).on('keydown', function(e) {
		if(e.keyCode == 17) {
			trigger = true;
		} 
		if(!(e.keyCode == 17 || e.keyCode == 13)) {
			trigger = false;
		}
		if (trigger && e.keyCode == 13){
			submitForm();
		}
	});

	// right panel toggler
	$('.right-panel-toggler').off('click').on('click', function(e){
		e.preventDefault();

		if($('.right-panel').hasClass('menu-close')){
			$(this).html();
			$(this).html('<i class="fas fa-caret-right"></i>');
			$(this).animate({
				'right' : '75%',
				'opacity' : '1'
			},200);
			$('.right-panel').animate({
				'width' : '75%',
				'opacity' : '1'
			},200);
			$('.right-panel').removeClass('menu-close');
		} else {
			$(this).html();
			$(this).html('<i class="fas fa-caret-left"></i>');
			$(this).animate({
				'right' : '0%',
				'opacity' : '.5'
			},200);
			$('.right-panel').animate({
				'width' : '0%',
				'opacity' : '0'
			},200);
			$('.right-panel').addClass('menu-close');
		}
	});

})