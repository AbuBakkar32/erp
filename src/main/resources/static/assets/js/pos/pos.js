$(document).ready(function(){

    // Resize Container
    containerResizer();
    $(window).on('resize', function(){
        containerResizer();
    });
    

    // Customized number input field
    customizedNumberField();


    // Time Counter
    timeCounter();

});




/**
 * Container Resizer
 */
function containerResizer(){
    var windowWidth = window.innerWidth;
    var windowHeight = window.innerHeight;
    
    // Flex Container
    var headerBarHeight = $('.header-bar').height();
    $('.flex-container').css({
        'height': windowHeight - headerBarHeight - 1
    });

    // Flex item 2
    var flexItemOneWidth = $('.flex-item1').width();
    $('.flex-item2').css({
        'width' : windowWidth - flexItemOneWidth - 35,
    });

    // Sub category
    $(".subcategory-menu").mousewheel(function(event, delta) {
        this.scrollLeft -= (delta * 150);
        this.scrollRight -= (delta * 150);
        event.preventDefault();
    });

    // Product Container, Cart Container, Bill Container
    var fh = $('.flex-item2').height();
    var fw = $('.flex-item2').width();
    var sch = $('.subcategory-menu').height();
    $('.product-container').css({
        'height' : fh - sch - 17,
        'width' : fw - $('.cart-container').width() - $('.bill-container').width() - 19
    });
    $('.cart-container').css({
        'height' : fh - sch - 17
    });
    $('.bill-container').css({
        'height' : fh - sch - 17
    });


    // Cart
    var ch = $('.cart-container').height();
    var cartHeaderHeight = $('.cart-header').height();
    var invoiceHeight = $('.invoice').height();
    $('.cart').css({
        'height' : (ch - cartHeaderHeight) - invoiceHeight - 20
    });


    // KOT Note
    var bch = $('.bill-container').height();
    var cdh = $('.calculator-display').height();
    var pth = $('.payment-type').height();
    var cbh = $('.calculator-body').height();
    $('.kot-note').css({
        'height' : bch - cdh - pth - cbh - 13
    })
}


/**
 * Customized Cart Number Field
 */
function customizedNumberField(){
    $('<div class="quantity-nav"><button class="quantity-button quantity-up"><i class="fas fa-chevron-up"></i></button><button class="quantity-button quantity-down"><i class="fas fa-chevron-down"></i></button></div>').insertAfter('.quantity input');
    $('.quantity').each(function () {
        var spinner = jQuery(this),
            input = spinner.find('input[type="number"]'),
            btnUp = spinner.find('.quantity-up'),
            btnDown = spinner.find('.quantity-down'),
            min = input.attr('min'),
            max = input.attr('max');

        btnUp.click(function () {
        var oldValue = parseFloat(input.val());
        if (oldValue >= max) {
            var newVal = oldValue;
        } else {
            var newVal = oldValue + 1;
        }
        spinner.find("input").val(newVal);
        spinner.find("input").trigger("change");
        });

        btnDown.click(function () {
        var oldValue = parseFloat(input.val());
        if (oldValue <= min) {
            var newVal = oldValue;
        } else {
            var newVal = oldValue - 1;
        }
        spinner.find("input").val(newVal);
        spinner.find("input").trigger("change");
        });
    });
}


/**
 * Time Counter
 */
function timeCounter(){
    $('.header-timer').html(moment().format("MMMM Do YYYY, h:mm:ss a"));
    setInterval(function () {
        $('.header-timer').html(moment().format("MMMM Do YYYY, h:mm:ss a"));
    }, 1000);
}