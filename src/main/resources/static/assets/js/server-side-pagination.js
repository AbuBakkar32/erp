$(document).ready(function(){

	var dataLimit = $('.ss-data-limit').val();
	var dataHint = $('.ss-data-search').val();
	var dataPage = 1;

	var baseTable = $('.ss-data-table').data('base-table');
	var baseTableAlias = $('.ss-data-table').data('base-table-alias');
	var whereCondition = $('.ss-data-table').data('where');
	var joinCondition = $('.ss-data-table').data('join-condition');


	var queryColumns = [];
	$.each($('th.query-column'), function(i, col){
		queryColumns.push({
			"column" : $(col).data('column'),
			"prefix" : $(col).data('prefix'),
			"index" : $(col).data('index'),
			"sortType" : $(col).data('sort'),
			"link" : $(col).data('link'),
			"linkUrl" : $(col).data('link-url'),
			"dataType" : $(col).data('type'),
		});
	})

	var orderByColumns = [];
	$.each($('th.query-column'), function(i, col){
		orderByColumns.push($(col).data('column') + ' ' + $(col).data('sort'));
	})
	
	console.log(queryColumns);
	console.log(baseTable);
	console.log(baseTableAlias);
	console.log(joinCondition);
	console.log(whereCondition);
	console.log(orderByColumns);
	console.log(dataLimit);
	console.log(dataHint);

	var queryString = {};
	queryString.queryColumns = queryColumns;
	queryString.baseTable = baseTable;
	queryString.baseTableAlias = baseTableAlias;
	queryString.joinCondition = joinCondition;
	queryString.whereCondition = whereCondition;
	queryString.orderByColumns = orderByColumns;
	queryString.dataLimit = dataLimit;
	queryString.dataHint = dataHint;
	queryString.dataPage = dataPage;
	
	
	$.ajax({
		url : getBasepath() + '/pagination/fetchdata',
		type : 'POST',
		dataType : 'json',
		data: JSON.stringify(queryString),
		beforeSend: function(xhr) {
			xhr.setRequestHeader("Accept", "application/json");
			xhr.setRequestHeader("Content-Type", "application/json");
		},
		success : function(data) {
			loadingMask2.hide();
			if(data.status == 'SUCCESS'){
				if(data.displayMessage == true) showMessage(data.status.toLowerCase(), data.message);

				if(data.triggermodalurl){
					modalLoader(getBasepath() + data.triggermodalurl, data.modalid);
				} else {
					if(data.reloadurl){
						loadTableBody(data);
						generatePaginationFromData(data.totalData, dataLimit, dataPage);
						//console.log("Total data : " + data.totalData)
						
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
	})
	
	
	
	
	
	
	
})




function generatePaginationFromData(numberOfData, dataLimit, selectedPage){

	var totalPage = Math.ceil(numberOfData / dataLimit);

	var nextBtn = '<li class="paging-btn" data-page="NEXT"><a href="#">Next</a></li>';
	var prevBtn = '<li class="paging-btn" data-page="PREV"><a href="#">Prev</a></li>';
	var dotBtn = '<li class="paging-btn disabled" data-page="DOT"><a href="#">...</a></li>';
	if(selectedPage == 1) {
		prevBtn = '<li class="paging-btn disabled" data-page="PREV"><a href="#">Prev</a></li>';
	}
	if(selectedPage == totalPage){
		nextBtn = '<li class="paging-btn disabled" data-page="NEXT"><a href="#">Next</a></li>';
	}

	var prevDotPrint = true;
	var nextDotPrint = true;

	var pagingUl = $('ul.pagination');
	$(pagingUl).find('li').remove();

	for(var i = 1; i <= totalPage; i++) {

		if(i == 1) $(pagingUl).append(prevBtn);

		if(totalPage <= 5) {
			if(selectedPage == i) {
				$(pagingUl).append('<li class="active paging-btn" data-page="'+ i +'"><a href="#">'+ i +'</a></li>');
			} else {
				$(pagingUl).append('<li class="paging-btn" data-page="'+ i +'"><a href="#">'+ i +'</a></li>');
			}
		} else {
			if(i == 1 && selectedPage != i) $(pagingUl).append('<li class="paging-btn" data-page="'+ i +'"><a href="#">'+ i +'</a></li>');

			if((selectedPage - 1 ) - 1 > 1 && prevDotPrint) {
				$(pagingUl).append(dotBtn);
				prevDotPrint = false;
			}

			if(selectedPage - 1 == i && (selectedPage - 1) != 1) {
				$(pagingUl).append('<li class="paging-btn" data-page="'+ i +'"><a href="#">'+ i +'</a></li>');
			}
			if(selectedPage == i) {
				$(pagingUl).append('<li class="active paging-btn" data-page="'+ i +'"><a href="#">'+ i +'</a></li>');
			}
			if(selectedPage + 1 == i && (selectedPage + 1) != totalPage) {
				$(pagingUl).append('<li class="paging-btn" data-page="'+ i +'"><a href="#">'+ i +'</a></li>');
			}

			if(i > (selectedPage + 1) && (selectedPage + 1 ) + 1 < totalPage && nextDotPrint) {
				$(pagingUl).append(dotBtn);
				nextDotPrint = false;
			}

			if(i == totalPage && selectedPage != i) $(pagingUl).append('<li class="paging-btn" data-page="'+ i +'"><a href="#">'+ i +'</a></li>');
		}

		if(i == totalPage) $(pagingUl).append(nextBtn);
	}
}



function loadTableBody(rdata){
	
	var tableBody = {};
	tableBody.lst = rdata.tableBodyData;
	
	
	//console.log({rdata})
	
	loadingMask2.show();
	$.ajax({
		url : getBasepath() + rdata.reloadurl,
		type : 'POST',
		data: tableBody,
		success : function(data) {
			loadingMask2.hide();
			console.log(data);
			
			
			var target = $('#' + rdata.reloadelementid);
			var parentElement = target.parent();
			$(target).remove();
			parentElement.append(data);
			bindTableButtonsEvent($('#' + rdata.reloadelementid));
			
		}, 
		error : function(jqXHR, status, errorThrown){
			showMessage(status, "Something went wrong .... ");
			loadingMask2.hide();
		}
	});

}
