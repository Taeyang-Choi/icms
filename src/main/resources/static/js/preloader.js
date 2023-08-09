(function () {
    var ieprefix = "";
    if(/MSIE \d|Trident.*rv:/.test(navigator.userAgent)) {
        //ieprefix = "/ie";
    }
    document.write('<script src='+ieprefix+'"/js/bootstrap.min.js"><\/script>');
})();