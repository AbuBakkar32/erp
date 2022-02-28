$(document).ready(function(){

	// init select2 plugin
	$('.select2').select2();

	// initial focus
	$('#xtrn').focus();

	// global variables
	var trigger = false;
	var itemData = [];
	var customers = [];
	var rowIndexCounter = 0;
	function initRowCounter(){
		rowIndexCounter = $("#mobomdetailtable tbody").find('tr').length;
	}
	initRowCounter();

	// initialize selec2 dropdown
	function initSelect2Dropdown(){
		$('#mobomdetailbody').find('.select2').select2();
	}
	initSelect2Dropdown();

	// initialize itemData 
	function initItemData(){
		loadingMask2.show();
		$.ajax({
			url : getBasepath() + '/production/mobomheader/preloadeddata',
			type : 'GET',
			success : function(data) {
				loadingMask2.hide();
				itemData = data.caitemList;
				customers = data.customers;
				initselectedXitem();

				$(".bom-xcus").find('option').remove().end().append('<option value="">-- Select --</option>');
				$.each(customers, function(i, list){
					$(".bom-xcus").append($('<option>').val(list.xcus).text(list.xcus + ' - ' + list.xorg));
				});

				$(".bom-xitem").find('option').remove().end().append('<option value="">-- Select --</option>');
				$.each(itemData, function(i, list){
					$(".bom-xitem").append($('<option>').val(list.xitem).text(list.xitem + ' - ' + list.xdesc));
				});
				
				/*$(".item-xitem").find('option').remove().end().append('<option value="">-- Select --</option>');
				$.each(itemData, function(i, list){
					$(".item-xitem").append($('<option>').val(list.xitem).text(list.xitem + ' - ' + list.xdesc));
				});*/

				console.log({data});
			},
			error : function(jqXHR, status, errorThrown){
				showMessage(status, "Something went wrong .... ");
				loadingMask2.hide();
			}
		})
	}
	initItemData();


	// Generate new Row in detail table
	function generateRow(){
		rowIndexCounter++;

		var itemsdropdown = 			'<div class="form-group">' +
											'<div class="row">' +
												'<div class="col-md-12">' +
													'<select class="form-control select2 item-xitem" name="xitem">';

		$.each(itemData, function(i, el){
			itemsdropdown += 						'<option data-item-unit="'+ el.xunit +'" value='+ el.xitem +'>'+ el.xitem + ' - ' + el.xdesc +'</option>';
		})

			itemsdropdown += 					'</select>'+
												'</div>'+
											'</div>'+
										'</div>';

		var trow = 	'<tr data-rowindex="'+ rowIndexCounter +'" data-xsign="-1"'+'" data-xtype="BOM-">'+ 
						'<td style="text-align: center;">' + rowIndexCounter + '</td>'+
						'<td>' + itemsdropdown +'</td>' +
						'<td>' + 
							'<div class="form-group">'+
								'<input type="number" class="form-control item-qty" min="1" value="1"/>'+
							'</div>'+
						'</td>' +
						'<td>' + 
							'<div class="form-group">'+
								'<input type="text" class="form-control item-unit" disabled="disabled"/>'+
							'</div>'+
						'</td>' +
						'<td>' + 
							'<div class="form-group">'+
								'<input type="text" class="form-control item-xref"/>'+
							'</div>'+
						'</td>' +
						'<td style="text-align: center;">' + 
							'<button type="button" class="btn btn-danger row-remover"><i class="fas fa-trash"></i></button>'
						'</td>'+
					'</tr>';
		
		$('#mobomdetailbody').append(trow);

		initSelect2Dropdown();
		rowRemove();
		reserialTableRow();
		$('tr[data-rowindex="'+ rowIndexCounter +'"]').find('.xitem').trigger("change");

//Getting Unit
		$.each($('#mobomdetailtable tbody tr'), function(i, row){

			$(row).find('.item-unit').val($(row).find('.item-xitem').find(":selected").data('item-unit'));

			$($(row).find('.item-xitem')).on('change', function(e){
				e.preventDefault();
				$(row).find('.item-unit').val($(this).find(":selected").data('item-unit'));
			})
		})

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

	// Event from header to generate new row
	$('textarea[name=xref]').keydown(function(e) {
		var code = e.keyCode || e.which;
		if (code === 9) {  
			if(rowIndexCounter > 0) return;
			$('#mobomdetailtable').removeClass('nodisplay');
			generateRow();
		}
	});

	// Row creator button event
	$('.row-creator').off('click').on('click', function(e){
		e.preventDefault();
		generateRow();
	});

	$('.bom-confirm-btn').off('click').on('click', function(e){
		e.preventDefault();
		submitForm();
	});

	// Form submit
	function submitForm(){
		var formValues = $('.bom-header-form').serializeArray();

		var mobomheader = {};

		$.each(formValues, function(i, data){
			mobomheader[data.name] = data.value;
		});

		var mobomdetails = [];

		var rows = $("#mobomdetailtable tbody").find('tr');
		$.each(rows, function(i, row){
			var mobomdetail = {};
			mobomdetail.xitem = $(row).find('.item-xitem').val();
			mobomdetail.xqty = $(row).find('.item-qty').val();
			mobomdetail.xunit = $(row).find('.item-unit').val();
			mobomdetail.xref = $(row).find('.item-xref').val();
			mobomdetail.xsign = $(row).data('xsign');
			mobomdetail.xtype = $(row).data('xtype');
			mobomdetails.push(mobomdetail);
		})

		var BOMMaster = {};
		BOMMaster.mobomheader = mobomheader;
		BOMMaster.mobomdetails = mobomdetails;

		loadingMask2.show();
		$.ajax({
			url : getBasepath() + '/production/mobomheader/savebom',
			type : 'POST',
			dataType : 'json', 
			data: JSON.stringify(BOMMaster),
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
		var rows = $("#mobomdetailtable tbody").find('tr');
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
	

	// on change effect
	function bomUnitSeleection(){
		$('.bom-xitem').on('change', function(e){
			e.preventDefault();
			var itemCode = $(this).val();
			$.each(itemData, function(i, item){
				if(item.xitem == itemCode){
					$('.bom-item-unit').val(item.xunit);
				}
			})
		})
	}
	bomUnitSeleection();
})