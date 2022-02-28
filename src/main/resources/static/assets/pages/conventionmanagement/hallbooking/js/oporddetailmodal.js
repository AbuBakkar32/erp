$(document).ready(function(){
	// Get item purchase unit
	var items = [];
	var functionsUK = [];

	function prepareItem(){
		//console.log($('a.selected').length)
		items = [];

		$.each($('a.item-box.btn-success'), function(bi, b){
			var xitem = $(b).data('xitem');
			var xcatitem = $(b).data('xcatitem');

			if(items.includes(xitem)){
				if(xcatitem == 'Function'){
					if(functionsUK.includes(xitem)){
						functionsUK.pop(xitem);
					}
				}

				const index = items.indexOf(xitem);
				if (index > -1) {
					items.splice(index, 1);
				}
				$(this).toggleClass("btn-success btn-default");
				calculateRate();
				return;
			}


			if(xcatitem == 'Function'){
				if(functionsUK.length > 0){
					var rxitem = functionsUK[0];
					const index = items.indexOf(rxitem);
					if (index > -1) {
						items.splice(index, 1);
					}
					functionsUK.pop();
					$('#' + rxitem).toggleClass("btn-success btn-default");
				}
				functionsUK.push(xitem);
			}
			items.push(xitem);
		});

		calculateRate();
	}
	prepareItem();

	$('a.item-box').off('click').on('click', function(e){
		if($(this))
		var xitem = $(this).data('xitem');
		var xcatitem = $(this).data('xcatitem');

		if(items.includes(xitem)){
			if(xcatitem == 'Function'){
				if(functionsUK.includes(xitem)){
					functionsUK.pop(xitem);
				}
			}

			const index = items.indexOf(xitem);
			if (index > -1) {
				items.splice(index, 1);
			}
			$(this).toggleClass("btn-success btn-default");
			calculateRate();
			return;
		}


		if(xcatitem == 'Function'){
			if(functionsUK.length > 0){
				var rxitem = functionsUK[0];
				const index = items.indexOf(rxitem);
				if (index > -1) {
					items.splice(index, 1);
				}
				functionsUK.pop();
				$('#' + rxitem).toggleClass("btn-success btn-default");
			}
			functionsUK.push(xitem);
		}
		items.push(xitem);


		$(this).toggleClass("btn-success btn-default");

		console.log({items});
		calculateRate();
	})

	function calculateRate(){
		console.log("%cCalculating",'color:green');
		var total = 0;
		items.forEach(function(item, index){
			if(item != undefined){
				total += $('#' + item).data('xrate');
			}
			
		});
		$('.rate-btn').html("Total : " + total + "/-");
	}

	$('.confirm-btn-modal').off('click').on('click', function(e){
		e.preventDefault();

		console.log({items});

		loadingMask2.show();
		$.ajax({
			url : getBasepath() + '/conventionmanagement/hallbooking/oporddetails/save',
			type : 'POST',
			data: {
				xitems: items,
				xordernum : $('#xordernum').val()
			},
			success : function(data) {
				loadingMask2.hide();
				if(data.status == 'SUCCESS'){
					$('div.modal').modal('hide');
					showMessage(data.status.toLowerCase(), data.message);
					if(data.triggermodalurl){
						modalLoader(getBasepath() + data.triggermodalurl, data.modalid);
					} else {
						if(data.redirecturl){
							setTimeout(() => {
								window.location.replace(getBasepath() + data.redirecturl);
							}, 1000);
						} else if(data.reloadurl){
							doSectionReloadWithNewData(data);
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
		});
		
	})
});