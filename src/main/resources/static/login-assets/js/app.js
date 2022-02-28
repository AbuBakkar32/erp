$(document).ready(function(){
	console.log("hi");
	$('.fake-login-btn').off('click').on('click', function(e){
		e.preventDefault();
		submitMainForm();
	})

	/**
	 * Submit main form
	 */
	function submitMainForm(customurl){
		if($('form#fakeloginform').length < 1) return;

		var targettedForm = $('form#fakeloginform');

		var submitUrl = (customurl != undefined) ? customurl : targettedForm.attr('action');
		var submitType = targettedForm.attr('method');
		var formData = $(targettedForm).serializeArray();

		$.ajax({
			url : submitUrl,
			type :submitType,
			data : formData,
			success : function(data) {
				if(data.status == 'SUCCESS'){
					if(data.redirecturl){
						setTimeout(() => {
							window.location.replace(getBasepath() + data.redirecturl);
						}, 1500);
					}
				} else {
					$('.error-message-container').html(data.message);
					$('form#fakeloginform').trigger("reset");
					$('.error-message-container').css("display","block");
					setTimeout(() => {
						$('.error-message-container').css("display","none");
					}, 2000);
				}
			}, 
			error : function(jqXHR, status, errorThrown){
				alert("Something went wrong. Please contact with system administrator");
			}
		});
	}

	/**
	 * Generate project context path
	 */
	function getBasepath(){
		var basePath = $('a.basePath').attr('href');
		basePath = basePath.split('/')[1];
		var href = location.href.split('/');
		if(basePath != ''){
			return href[0] + '//' + href[2] + '/' + basePath;
		}
		return href[0] + '//' + href[2];
	}
})