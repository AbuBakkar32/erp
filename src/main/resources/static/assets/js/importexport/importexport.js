/**
 * Show Progress bar
 * @returns
 */
function showProgress(){
	$('#progressmask').show();
}

/**
 * Hide progress bar
 * @returns
 */
function hideProgress(){
	$('#progressmask').hide();
	$('#progressmask .progress-bar').css('width', 0 + "%");
	$('#progressmask .progress-bar').html(0 + "% complete");
}

/**
 * Progress bar percentage update
 * @param percentage
 * @returns
 */
function updateProgress(percentage){
	showProgress();
	$('#progressmask .progress-bar').css('width', percentage + "%");
	$('#progressmask .progress-bar').html(percentage + "% complete");
}

/**
 * Update import table status
 * @param data
 * @returns
 */
function updateStatusTable(data){
	$('.upload-result-satus').removeClass('nodisplay');
	$('.status-table-filename').html(data.fileName);
	$('.status-table .status-table-created').html(data.numberOfCreateRecord);
	$('.status-table .status-table-updated').html(data.numberOfUpdateRecord);
	$('.status-table .status-table-deleted').html(data.numberOfDeleteRecord);
	$('.status-table .status-table-total').html(data.totalNumberOfRecords);
	$('.import-btn').attr('data-filename', data.fileLocationToImportData);
}

/**
 * Reset import table status
 * @param forimport
 * @returns
 */
function resetStatusTable(forimport){
	$('.upload-result-satus').addClass('nodisplay');
	$('.import-btn, .import-cancel-btn').removeClass('nodisplay');
	$('.status-table-filename').html("");
	$('.status-table .status-table-created').html(0);
	$('.status-table .status-table-updated').html(0);
	$('.status-table .status-table-deleted').html(0);
	$('.status-table .status-table-total').html(0);
	if(!forimport) $('.import-btn').removeAttr('data-filename');
	if(forimport) $('.import-btn, .import-cancel-btn').addClass('nodisplay');
}

/**
 * Update progress bar percentage value
 * @param percentage
 * @returns
 */
function updateProgressBar(percentage){
	if(percentage > 100) percentage = 100;
	$('.upload-progress-satus').removeClass('nodisplay');
	var progressbar = $('div.progress-bar');
	progressbar.attr('aria-valuenow', percentage);
	progressbar.css('width', percentage + '%');
	progressbar.html(percentage + '% Complete');
}

/**
 * Reset progressbar percentage
 * @returns
 */
function resetProgressBar(){
	$('.upload-progress-satus').addClass('nodisplay');
	var progressbar = $('div.progress-bar');
	progressbar.attr('aria-valuenow', 0);
	progressbar.css('width', '0%');
	progressbar.html('0% Complete');
}

/**
 * Reset import table status
 * @param errorTable
 * @returns
 */
function resetTable(errorTable){
	$('.upload-error-satus').addClass('nodisplay');
	errorTable.clear().draw();
}

$(document).ready(function() {
	var errorTable = $('#upload-error-satus-table').DataTable();

	// File selection done for upload
	$("input#file").change(function(e){
		// reset everything first
		hideProgress();
		resetStatusTable(false);
		resetTable(errorTable);

		var files = $(this).get(0).files;
		if (files.length == 0) return;

		var data = new FormData();
		data.append("file", files[0]);
		data.append("ignoreHeading", $('#ignoreFirstRow').is(':checked'));
		data.append("updateExisting", $('#updateExisting').is(':checked'));
		data.append("delimeterType", $('#fileDelimiter').val());

		$.ajax({
			type : "POST",
			url : getBasepath() + "/importexport/upload/csv/" + $('.modulename').val(),
			contentType : false,
			processData : false, 
			data : data,
			success : function(data) {
				$("input#file").val("");
				updateProgress(data.progress);
				if(data.isWorkInProgress == false) {
					setTimeout(() => {
						hideProgress();
						if(data.allOk == true) {
							updateStatusTable(data);
						} else {
							updateErrorTable(data);
						}
					}, 1000);
					return;
				}
				getUploadProgressAndUpdateProgressbar(data.token);
			},
			error : function() {
				alert("There was error uploading files!");
			}
		});

	});

	
	function getUploadProgressAndUpdateProgressbar(token){
		if(token == undefined || token == '') return;

		var statusUrl = getBasepath() + '/importexport/progress/status/' + token;
		$.ajax({
			type : 'GET',
			url : statusUrl,
			success : function(data){
				updateProgress(data.progress);
				if(data.isWorkInProgress == false){
					setTimeout(() => {
						hideProgress();
						if(data.allOk == true) {
							updateStatusTable(data);
						} else {
							updateErrorTable(data);
						}
					}, 1000);
					return;
				}
				getUploadProgressAndUpdateProgressbar(data.token);
			},
			error : function(){
				alert("Something wrong");
			}
		})
	}

	function updateErrorTable(data){
		if(data.csvErrors.length < 1) return;
		$('.upload-error-satus').removeClass('nodisplay');
		$.each(data.csvErrors, function(index, item){
			errorTable.row.add([item.lineNo, item.column, item.reason]).draw(false);
		})
		errorTable.order([0, 'asc']).draw();
	}

	$('.import-btn').off('click').on('click', function(e){
		e.preventDefault();
		hideProgress();
		resetStatusTable(true);
		resetTable(errorTable);

		$.ajax({
			type : "POST",
			url : getBasepath() + "/importexport/import/csv/" + $('.modulename').val(),
			data : {
				'fileName' : $(this).data('filename')
			},
			success : function(data) {
				$("input#file").val("");
				updateProgress(data.progress);
				if(data.isWorkInProgress == false) {
					setTimeout(() => {
						hideProgress();
						updateStatusTable(data);
					}, 1000);
					return;
				}
				getImportProgressAndUpdateProgressbar(data.token);
			}, 
			error : function(){
				alert("Something wrong");
			}
		});
	});

	function getImportProgressAndUpdateProgressbar(token){
		if(token == undefined || token == '') return;

		var statusUrl = getBasepath() + '/importexport/progress/status/' + token;
		$.ajax({
			type : 'GET',
			url : statusUrl,
			success : function(data){
				updateProgress(data.progress);
				if(data.isWorkInProgress == false){
					setTimeout(() => {
						hideProgress();
						updateStatusTable(data);
					}, 1000);
					return;
				}
				getImportProgressAndUpdateProgressbar(data.token);
			},
			error : function(){
				alert("Something wrong");
			}
		})
	}

	$('.import-cancel-btn').off('click').on('click', function(){
		resetStatusTable(false);
	});

	$('.export-btn').off('click').on('click', function(e){
		e.preventDefault();

		$.ajax({
			type : "POST",
			url : getBasepath() + "/importexport/download/" + $('.modulename').val(),
			data : $('form#exportform').serializeArray(),
			success : function(data) {
				updateProgress(data.progress);
				if(data.isWorkInProgress == false) {
					setTimeout(() => {
						hideProgress();
						completeExport(data);
					}, 1000);
					return;
				}
				beginExportStatus(data.token);
			}, 
			error : function(){
				alert("Something wrong");
			}
		});

	})

	function beginExportStatus(token){
		if(token == undefined || token == '') return;

		var statusUrl = getBasepath() + '/importexport/exportprogress/status/' + token;
		$.ajax({
			type : 'GET',
			url : statusUrl,
			success : function(data){
				updateProgress(data.progress);
				if(data.isWorkInProgress == false){
					setTimeout(() => {
						hideProgress();
						completeExport(data);
					}, 1000);
					return;
				}
				beginExportStatus(data.token);
			},
			error : function(){
				alert("Something wrong");
			}
		})
	}

	function completeExport(pdata){
		var formRef = $('#exportform');
		var submiturl = getBasepath() + '/importexport/' + pdata.moduleName + '/exportfinal/' + pdata.token;
		$(formRef).attr("action", submiturl);
		$(formRef).submit();
	}

	// Checkbox event
	$('.checkboxcontrolled').off('change').on('change', function(e){
		e.preventDefault();

		var dependentitems = $(this).attr('id');

		if($(this).prop("checked") == true){
			$(".form-group[data-toggledby='"+ dependentitems +"']").addClass('nodisplay');
		} else {
			$(".form-group[data-toggledby='"+ dependentitems +"']").removeClass('nodisplay');
		}
	})

})