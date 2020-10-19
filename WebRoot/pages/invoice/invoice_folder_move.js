//+++++++++++++++++++++++++++++

$(function($){
    $.page.init();//-- 页面初始化
});


(function ($) {

    var action = "invoice_folder_";

    $.page.idField = "id"
    $.page.opt.add.requestId =  action +  "add_page";
    $.page.table.requestId =  "invoice_folder";//-- 查询 ACTION
    $.page.opt.add.width =  600;
    $.page.opt.add.height =  300;


    //-- 关闭按钮
    $.page.close = function() {
        CommonReportService.closeDialog();
    };

    //-- 选择数据
    $.page.select = function() {
        var selected = $.page.selected() || [];
        if( 0 === selected.length  ){
            // return MessageBox.error('请选择一条数据');
            selected = [ {id:0} ] //-- 不选择则移动至 根目录
        }
        onDblClickRow(selected[0]);
    };

    //-- 双击行选择数据
    function onDblClickRow(row){
        CommonReportService.closeDialog(row);
    }


    var table = $.page.table.options = {};
    table.onClickRow = function (){}
    table.clickToSelect = true
    table.showRefresh = false
    table.height = 350;

    //-- 双击行选择
    table.onDblClickRow = onDblClickRow;

    var columns = $.page.table.columns = [];

    //-- 过期的数据不允许勾选:  不允许修改和删除
    columns[ columns.length ] = { radio:"true",width:10};
    columns[columns.length] = {field:"showInvoiceType", title: "",formatter:type,width:100};


    //-- 发票类型  默认未知
    function type(value,row){
        value = "F" === row.folderType&& "0" || value || 5;//-- 默认未知
        return "<span class=\"invoice-icon-type\">&nbsp;&nbsp;folderName</span>".replace("type",value).replace("folderName",row.folderName );
    }

})(jQuery);