$('#load1').on('click', function() {
    var $this = $(this);
    $this.button('loading');
    setTimeout(function() {
        $this.button('reset');
    }, 10000);
});