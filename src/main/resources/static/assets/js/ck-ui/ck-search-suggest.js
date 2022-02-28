var debounce = null;

$(document).ready(function(){
	console.log("%cSearch Sugget Init...", "color:green");

	$(".searchsuggest").off('keyup').on('keyup', function(e){
		if(e.which == 38 || e.which == 40 || e.which == 13){
			var swithc = "UP";
			if(e.which == 40) swithc = "DOWN";
			if(e.which == 13) swithc = "ENTR"
			adjustSelectedItem(swithc);
			return;
		}

		var hint = $(this).val();
		var targetElement = $(this);
		var serachUrl = $(this).attr('search-url');

		if(hint == '') return;

		if(debounce != null) clearTimeout(debounce);
		debounce = setTimeout(function(){
			$.ajax({
				url : getBasepath() + '/' + serachUrl + '/' + hint,
				type : 'GET',
				success : function(data) {
					generateSearchResult(targetElement, data);
				},
				error : function(jqXHR, status, errorThrown){
					//showMessage(status, "Something went wrong .... ");
				}
			});
		}, 700);

	});

	function adjustSelectedItem(swithc){
		if(swithc == 'ENTR'){
			selectItemAndSetValue($('tr.search-item.selected-search-item'), $('.searchsuggest'), $('.search-suggest-table-container'));
			return;
		}

		var selectedRowIndex = $('tr.search-item.selected-search-item').data('rowindex')

		$('tr.search-item').removeClass('selected-search-item');

		if(swithc == 'UP' && selectedRowIndex != 1){
			selectedRowIndex--;
		} else if(swithc == 'DOWN' && selectedRowIndex != $('tr.search-item').length) {
			selectedRowIndex++;
		}

		$('tr.search-item[data-rowindex="'+ selectedRowIndex +'"]').addClass('selected-search-item');

		var rowpos = $('tr.search-item[data-rowindex="'+ selectedRowIndex +'"]').position();
		var rowHeight = $('tr.search-item[data-rowindex="'+ selectedRowIndex +'"]').height();
		var tableHHeight = $('.search-suggest-table-container').height();
		//console.log("===> Table height : "+ tableHHeight +"  ===> position : " + rowpos.top + "  ===> row height : " + rowHeight);

		if((rowpos.top - 3) > tableHHeight){
			if(swithc == 'DOWN'){
				$('.search-suggest-table-container').animate({
					scrollTop: $('.search-suggest-table-container').scrollTop() + Number(rowHeight + 2)
				}, 0);
			}
		}
		if (swithc == 'UP'){
			$('.search-suggest-table-container').animate({
				scrollTop: selectedRowIndex == 1 ?  $('.search-suggest-table-container').scrollTop() - 99999 : $('.search-suggest-table-container').scrollTop() - Number(rowHeight - 2)
			}, 0);
		}
	}

	$(".searchsuggest").off('blur').on('blur', function(){
		var targetElement = $(this);
		var parent = $(targetElement).parent();

		// Remove previous search suggest div if exist
		setTimeout(() => {
			// list mood
			if($(parent).find('.search-suggest-results').length > 0){
				$(parent).find('.search-suggest-results').remove();
			}
			// table mood
			if($(parent).find('.search-suggest-table-container').length > 0){
				$(parent).find('.search-suggest-table-container').remove();
			}
			// clear data
			if($(targetElement).val() == ''){
				$(parent).find('#search-val').val("");
				$(parent).find('#search-des').val("");
			} else {
				$(targetElement).val($(parent).find('#search-des').val());
			}
		}, 500);

	})

	function tableMoodRowGenerator(uielement, data){
		var parent = $(uielement).parent();

		// Remove previous search suggest div if exist
		if($(parent).find('.search-suggest-table-container').length > 0){
			$(parent).find('.search-suggest-table-container').remove();
		}

		// create new search suggest resul div
		var headerColumn = 	'<tr>';
		$.each(data.columns, function(i, col){
			headerColumn += '<th style="text-align: center; white-space: nowrap;">'+ col +'</th>';
		})
			headerColumn += '</tr>';

		var bodyData = '';
		$.each(data.data, function(i, row){
			var selectedClass = "";
			if(i == 0) selectedClass = "selected-search-item";

			bodyData += '<tr class="search-item '+ selectedClass +'" data-rowindex="'+ (i+1) +'">';

			$.each(row, function(j, coldata){
				var forvalue = false;
				var forprompt = false;
				if(j == data.valueindex) forvalue = true;
				if(data.promptindex.includes(j)) forprompt = true;
				bodyData += '<td style="text-align: center; white-space: nowrap;" data-forvalue="'+ forvalue +'" data-forprompt="'+ forprompt +'">'+ coldata +'</td>';
			})

			bodyData += '</tr>';
		})
		if(data.data.length == 0){
			bodyData += '<tr><td style="text-align: center;" colspan="'+ data.columns.length +'">No result found</td></tr>';
		}

		var tableContainer = 	'<div class="search-suggest-table-container" style="display: block;">'+
									'<table class="table table-striped search-suggest-table">'+
										'<thead>' +
											headerColumn +
										'</thead>' +
										'<tbody>' +
											bodyData +
										'<tbody>' +
									'</table>'+
								'</div>';
		$(parent).append(tableContainer);

		$('tr.search-item').off('click').on('click', function(e){
			selectItemAndSetValue($(this), uielement, tableContainer);
		})
	}

	function selectItemAndSetValue(selectedItem, uielement, tableContainer){
		var itemval = $(selectedItem).find('td[data-forvalue="true"]').html();
		var itemprompt = "";
		$.each($(selectedItem).find('td[data-forprompt="true"]'), function(i, item){
			if(i == 0) {
				itemprompt += $(item).html();
			} else {
				itemprompt += ' - ' + $(item).html();
			}
		})
		$(uielement).val(itemprompt);
		var parent = $(uielement).parent();
		$(parent).find('#search-val').val(itemval);
		$(parent).find('#search-des').val(itemprompt);
		$(tableContainer).remove();
	}

	function generateSearchResult(uielement, data){
		if(data.tableMood == true){
			tableMoodRowGenerator(uielement, data);
			return;
		}

		var parent = $(uielement).parent();

		// Remove previous search suggest div if exist
		if($(parent).find('.search-suggest-results').length > 0){
			$(parent).find('.search-suggest-results').remove();
		}

		// create new search suggest resul div
		$(parent).append('<div class="search-suggest-results col-sm-6"></div>');

		var searchContainer = $(parent).find('.search-suggest-results');
		$(searchContainer).css({
			'display':'block',
			'min-width' : $(uielement).width() + 25,
			'top' : $(uielement).height("px")
		});

		// clreate list items
		$(searchContainer).html('<ul class="search-container-ul"></ul>');
		var totalItem = 0;
		$.each(data, function(index, item){
			totalItem++;
			var listitem = '<li class="search-item" value="'+ item.value +'" prompt="'+ item.prompt +'">'+ item.prompt +'</li>';
			$(searchContainer).find('.search-container-ul').append(listitem);
		})
		if(totalItem == 0){
			var noresultitem = '<li class="search-item" value="" prompt="">No result found</li>';
			$(searchContainer).find('.search-container-ul').append(noresultitem);
		}

		$(searchContainer).find('.search-item').off('click').on('click', function(){
			var itemprompt = $(this).attr('prompt');
			var itemval = $(this).attr('value');
			$(uielement).val(itemprompt);
			$(parent).find('#search-val').val(itemval);
			$(parent).find('#search-des').val(itemprompt);
			$(searchContainer).remove();
		})

	}

});