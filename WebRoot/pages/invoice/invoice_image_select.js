//+++++++++++++++++++++++++++++

$(function($){
    $.page.init();//-- 页面初始化
});


(function ($) {

    var action = "invoice_folder_";
    $.page.idField = "id"
    $.page.table.requestId =  "invoice_folder";//-- 查询 ACTION
    $.page.opt.view.requestId = action + "view"  ; //-- 详情地址

    //-- 选择
    $.page.select = function (){
        var selected = $.page.selected() || [];
        if( 0 === selected.length  ){
            return MessageBox.error('请选择影像');
        }
        CommonReportService.closeDialog(selected);
    }
    //-- 返回
    $.page.backtrack = function (){
        $.page.view({id:0} )
    }

    var table = $.page.table.options = {};
    table.onClickRow = function (  row ){
        if(  "F" !== row.folderType ){
            return
        }
        $.page.opt.view.title = row.folderName
        $.page.view( row )
    }

    //--  详情
    $.page.view = function (row) {
        $("#id").val(row.id)
        $("#queryForm").submit()
    };

    //-- 关闭按钮
    $.page.close = function() {
        CommonReportService.closeDialog();
    };

    table.pagination = false
    table.showRefresh = false
    table.clickToSelect = true

    table.responseHandler=function(result) {//请求数据成功后，渲染表格前的数据处理
        return result.rows
    }
    //-- 复选框 选着/取消触发 按钮权限
    table.onCheck =onCheck
    table.onUncheck = onUncheck
    table.onCheckAll = checkLimit
    table.onUncheckAll = checkLimit

    $.page.process = false

    //-- 选择一行时触发
    function onCheck( row ){
        cascadeCheck( row,"check" )
    }
    //-- 取消一行时触发
    function onUncheck( row ){
        cascadeCheck( row,"uncheck" )
    }

    //-- 级联勾选/不勾选
    function cascadeCheck(row,check){

        if( $.page.process ){
            return
        }

        $.page.process = true

        if( "F" === row.folderType ){
            $.page.process = false
            return checkLimit()
        }

        var imageId = row.imageId;
        if( !imageId ){
            $.page.process = false
            return  checkLimit()
        }

        //-- 一图多票级联取消勾选
        var data = $('#dtItemList').bootstrapTable("getData")||[];
        $.each( data , function (i,r){
            r.imageId = r.imageId|| ("id" + r.id )
            if( imageId !== r.imageId  ){
                return
            }
            if( r.rn === row.rn ){
                return
            }
            $('#dtItemList').bootstrapTable(check, r.rn -1)
        } )

        checkLimit()

        $.page.process = false
    }

    function checkLimit(){

        disable(".limit");//-- 全部按钮禁用

        var selected = $.page.selected() || [];
        var selectSome = selectSomeLimit();//-- 是否选着了多个
        selectBtLimit()

        //-- 至少选着一个控制
        function selectSomeLimit() {
            var disabled = 0 === selected.length
            disabled || enable(".select-some")
            disabled && disable(".select-some");
            return !disabled;
        }


        //-- 确定选着按钮
        function selectBtLimit() {
            if (!selectSome) {
                return;
            }

            //-- 判断多选的数据是否包含了 文件夹 如果包含了 则不允许移动
            var disabled =  false
            $.each( selected,function ( index,row ){
                if( row.folderType === 'I' ){
                    return
                }
                disabled = true
                return false;
            })

            //-- 判断是否是 文件夹
            disabled || enable(".select")
            disabled && disable(".select")
        }

    }

    function enable(select) {
        $(select).attr("disabled", false).addClass("btn-normal-white").removeClass("btn-normal-grey")
    }

    function disable(select) {
        $(select).attr("disabled", true).addClass("btn-normal-grey").removeClass("btn-normal-white")
    }

    var columns = $.page.table.columns = [];

    //-- 只能选择 图片
    columns[ columns.length ] = {checkbox:"true" , formatter:function (value, row) {
            return { disabled:"F" === row.folderType }
        }};

    columns[columns.length] = {field:"showInvoiceType", title: "文件名称",formatter:type,width:"100px"};
    columns[columns.length] = {field:"createDate", title: "创建时间",formatter:render4TdLink,width:"60px"};
    columns[columns.length] = {field:"folderDataCount", title: "影像总数",formatter:render4TdLink,width:"50px"};
    columns[columns.length] = {field:"invoiceAmount", title: "合计金额",formatter:render4TdLink,width:"60px"};

    columns[columns.length] = {field:"fileName", title: "影像原名称",formatter:render4TdLink,width:"400px"};



    //-- 发票类型  默认未知
    function type(value,row){
        value = "F" === row.folderType&& "0" || value || 5;//-- 默认未知
        return "<span class=\"invoice-icon-type\" title='folderName' >&nbsp;&nbsp;folderName</span>"
            .replace("type",value)
            .replace("folderName",row.folderName)
            .replace("folderName",row.folderName );
    }



    //-- 查询
    $.page.query = function() {
        disable(".limit");//-- 全部按钮禁用
        $('#dtItemList').bootstrapTable('refresh')
    };

    function render4TdLink ( value  ) {
        return createTdLinkCommon("",value);
    }

    LocationUtil.getParams = function (){
        return { "divId4OpenQueryDialog" :  $("#divId4OpenQueryDialog").val() }
    }

})(jQuery);