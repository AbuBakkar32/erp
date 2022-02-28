$(document).ready(function(e){
	bindTableButtonsEvent($('table.profilelinetable'));

	$('.pl-display').off('change').on('change', function(e){
		e.preventDefault();
		console.log($(this).attr('id') + ' - ' + $(this).prop("checked"));

		var profilecode = $(this).attr('profilecode');
		var profilelinecode = $(this).attr('id');
		var profiletype = $(this).attr('profiletype');
		var profilelineid = $(this).attr('profilelineid');
		var screenprompt = $(this).attr('screenprompt');
		var enabled = $(this).attr('enabled');
		var display = $(this).prop("checked");
		var seqn = $(this).attr("seqn");
		var pgroup = $(this).attr('pgroup');
		var submitUrl = $(this).data("url");
		console.log(submitUrl);

		var data = {};
		data.profilecode = profilecode;
		data.profilelinecode = profilelinecode;
		data.profiletype = profiletype;
		data.profilelineid = profilelineid;
		data.screenprompt = screenprompt;
		data.display = display;
		data.seqn = seqn;
		data.pgroup = pgroup;
		data.enabled = enabled;

		console.log({data});

		$.ajax({
			type : "POST",
			url : submitUrl,
			data : data,
			beforeSend : loadingMask2.show(),
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
				loadingMask2.hide();
			}
		});

	});

	
	
	
	$('.pl-enabled').off('change').on('change', function(e){
		e.preventDefault();
		console.log($(this).attr('id') + ' - ' + $(this).prop("checked"));

		var profilecode = $(this).attr('profilecode');
		var profilelinecode = $(this).attr('id');
		var profiletype = $(this).attr('profiletype');
		var profilelineid = $(this).attr('profilelineid');
		var screenprompt = $(this).attr('screenprompt');
		var display = $(this).attr('display');
		var enabled = $(this).prop("checked");
		var seqn = $(this).attr("seqn");
		var pgroup = $(this).attr('pgroup');
		var submitUrl = $(this).data("url");
		console.log(submitUrl);

		var data = {};
		data.profilecode = profilecode;
		data.profilelinecode = profilelinecode;
		data.profiletype = profiletype;
		data.profilelineid = profilelineid;
		data.screenprompt = screenprompt;
		data.display = display;
		data.seqn = seqn;
		data.pgroup = pgroup;
		data.enabled = enabled;

		console.log({data});

		$.ajax({
			type : "POST",
			url : submitUrl,
			data : data,
			beforeSend : loadingMask2.show(),
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
				loadingMask2.hide();
			}
		});

	});

	
})