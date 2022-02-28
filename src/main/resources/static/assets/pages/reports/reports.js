$(document).ready(function(){
	var startDate = $('input[con-start="true"]');
	var endDate = $('input[con-start="false"]');
	console.log(startDate.val());
	console.log(endDate.val());
	var stat = moment(endDate.val(), "DD-MMM-YYYY") >= moment(startDate.val(), "DD-MMM-YYYY");
	console.log(stat);

	$('.confirm-rpt-birt-btn').off('click').on('click', function(e){
		e.preventDefault();

		var targettedForm = $('form#reportform');
		if(!targettedForm.smkValidate()) return;

		var submitUrl = targettedForm.attr('action');
		var submitType = targettedForm.attr('method');
		var formData = $(targettedForm).serializeArray();
		var reportType = $('#reportType').val();
		var reportName = $('#reportName').val() != '' ? $('#reportName').val() : 'report';

		$.ajax({
			url : submitUrl,
			type : submitType,
			data : formData,
			beforeSend : loadingMask2.show(),
			success : function(data) {
				loadingMask2.hide();
				var reporturl = getBirtUrl() + data;
				console.log(reporturl);
				document.getElementById('reportIframe').src = "";
				document.getElementById('reportIframe').src = reporturl;
			},
			error : function(jqXHR, status, errorThrown){
				loadingMask2.hide();
				showMessage(status, "Something went wrong .... ");
			}
		})
	});
	
	
	
	
	
	// Custom fields action
	// PROJECT Based Store Dropdown
	$('.project').off('change').on('change', function(e){
		
		setTimeout(() => {
			var projectId = $(this).siblings('#search-val').val();
			
			loadingMask2.show();
			$.ajax({
				url : getBasepath() + "/" + $(this).attr('dependent-search') + "/" + projectId,
				type : 'GET',
				success : function(data) {
					loadingMask2.hide();
					console.log({data});
					
					
					$(".store").find('option').remove().end().append('<option value="">-- Select --</option>');
					$.each(data, function(i, list){
						$(".store").append($('<option>').val(list.xcode).text(list.xcode + ' - ' + list.xcodename));
					});
					
				},
				error : function(jqXHR, status, errorThrown){
					loadingMask2.hide();
					showMessage(status, "Something went wrong .... ");
				}
			})
			
		}, 1000);
		
	})
	
	
});