//+++++++++++++++++++++++++++++

$(function($){
    $.page.init();
    // $("#folderName")[0].focus();

    var $folderName = $("#folderName");
    var val = $folderName.val();
    $folderName.val( "" ).focus().val(val);

    //-- 回车 保存
    $( window ).keydown(function(e){
        if(e.keyCode==13){
            $.page.save();
            e.stopPropagation();
            return false
        }
    });

});

(function ($) {
    $.page.add = "invoice_folder_add"; //-- 保存的 ACTION

    $.page.validate = function (){
        return true
    }

})(jQuery);