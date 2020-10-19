//+++++++++++++++++++++++++++++

$(function($){
    $.page.init();//-- 页面初始化
    $.page.query();
});


(function ($) {

    var action = "invoice_folder_";

    $.page.idField = "imageId"
    $.page.opt.del.requestId =  action +  "image_status_del";//--删除地址
    $.page.table.requestId =  action + "image_status";//-- 查询 ACTION
    $.page.opt.view.requestId = action + "view"; //-- 详情地址

    $.extend($.fn.bootstrapTable.locales['zh-CN'], {
        formatNoMatches: function () {
            var eye = $('#eye') ? $('#eye').is(':checked') : true;
            return eye&&"暂无待处理的影像"||"暂无影像"
        }
    });

    $.extend($.fn.bootstrapTable.defaults, $.fn.bootstrapTable.locales['zh-CN']);

    //-- 查询
    $.page.query = function() {

        var url = contextPath + "/commonbizajax2report.do";
        var params = {}
        params.requestId = $.page.table.requestId;
        params.parentId = $("#parentId").val()||'' ;

        Ajax.post().url(url).data(params).success(success).do();
        function success(res, isOk) {
            res = res || {};
            $.page.datas = res.data||[]
            $.page.inductionData()//--数据分类
            $.page.setImage()//--加载 viewer
            $.page.reloadTable();//-- 加载table
        }
    };

    // -- 归纳 数据
    $.page.inductionData = function( ){
        $.page.data  = {}
        $.page.data.unknownData = []
        $.page.data.all = $.page.datas

        //-- 将
        $.each( $.page.datas,function (){
            var statusCode = this.statusCode
            if( "4" === statusCode  || "3" === statusCode ){
                return;
            }
            $.page.data.unknownData.push( this )
        } )

    }

    //-- 变更 是否隐藏 已识别 筛选
    $.page.changeEye = function (){
        $.page.eye = $('#eye') ? $('#eye').is(':checked') : true;//-- 记录状态
        $.page.reloadTable();//-- 重载数据
    }

    //-- 加载表格数据
    $.page.reloadTable = function (){

        $('#dtItemList').bootstrapTable("load", getData() )

        function getData(){
            var eye = $('#eye') ? $('#eye').is(':checked') : true;
            if( !eye ){
                return $.page.data.all;
            }
            return $.page.data.unknownData
        }
    }

    $.page.setImage = function (){

        var div_image =  $("#div_image");
        var div_image1 =  $("#div_image1");

        div_image.html("")
        div_image1.html("")

        addImg( div_image , $.page.data.all  )
        addImg( div_image1 , $.page.data.unknownData ,"_u"   )

        function addImg( tag ,rows ,prefix  ){
            prefix = prefix || ""
            var temp = "<img id=\"img$prefix$_$id$\" src=\"$url$\" class=\"img-rounded viewer-toggle\">"
            temp = temp.replace( "$prefix$",prefix  )
            $.each( rows , function (){
                var viewUrl =  this.image;
                var html = temp.replace( "$id$",  this.imageId ).replace( "$url$",viewUrl)
                tag.append( html )
            } )
        }

        $.page.viewer = new Viewer(document.getElementById('div_image'), {
            toolbar: true,//显示工具条
            viewed: function() {
                $.page.viewer.zoomTo(0.75);// 图片显示比例 75%
            },
            show: function () {// 动态加载图片后，更新实例
                $.page.viewer.update();
            }
        });
        $.page.viewer1 = new Viewer(document.getElementById('div_image1'), {
            toolbar: true,//显示工具条
            viewed: function() {
                $.page.viewer.zoomTo(0.75);// 图片显示比例 75%
            },
            show: function () {// 动态加载图片后，更新实例
                $.page.viewer.update();
            }
        });
    }

    var table = $.page.table.options = {};
    table.onClickCell = function ( field, value, row ){
        if ("showInvoiceType" !== field) {
            return
        }
    }

    table.onClickRow = function (){}

    table.data = []
    table.showRefresh = false
    table.sidePagination = "client"
    table.onPostHeader = function (){

        if( !$.page.eye && false !== $.page.eye ) {
            $.page.eye = false
        }
        var eye = $.page.eye ;
        $('#eye').prop('checked',eye);
    }

    table.responseHandler=function(result) {//请求数据成功后，渲染表格前的数据处理
        return { total: result.data.length ,rows : result.data }
    }

    var columns = $.page.table.columns = [];

    // columns[columns.length] = {
    //     field: "rn", title: "", width: "20px", formatter: function (value, row, index) {
    //         return index + 1;
    //     }
    // };


    columns[columns.length] = {field:"image_small", title: "影像查看",formatter:small,width:"100px"};
    columns[columns.length] = {
        field: "status",
        title:  "影像状态 （ <span class=\"glyphicon glyphicon-eye-open\" />） <input id='eye' type=\"checkbox\" name='eye'  onclick='$.page.changeEye()' checked /> ",
        formatter: status,
        titleTooltip: "选中隐藏已识别/已验真数据",
        width: "150px"
    };
    columns[columns.length] = {field:"createDate", title: "上传时间",formatter:$.page.table.render4TdLink,width:"250px"};
    columns[columns.length] = {field:"statusCode", title: "操作",formatter:option};


    //-- 影像缩略图
    function small(value,row){
        var img = "<img src=\"url\" class=\"img-rounded viewer-toggle\" height=\"30px\" width=\"30px\" onclick=\"$.page.view('viewUrl')\">"
        // var img = "<img src=\"url\" class=\"img-rounded viewer-toggle\" height=\"30px\" width=\"30px\" >"
        var url = row.image_small;
        var viewUrl = row.image;
        viewUrl = encodeURI(viewUrl);

        // return img.replace( "url",url ).replace( "viewUrl",viewUrl )
        return img.replace( "url",url ).replace( "viewUrl",row.imageId )
    }
    //-- 影像缩略图
    $.page.view = function( id, viewUrl){

        var eye =  $('#eye').is(':checked')
        eye = eye ? "_u" : "";

        $("#img" + eye +"_"  + id  ).click()
    }

    function status(value,row){
        if( effectiveImage(row) ){
            return $.page.table.render4TdLink(value,row);
        }
        //-- 错误数据标红
        return "<span style='color: red'>val</span>".replace( "val" ,value   )
    }

    function option(value,row){
        if( effectiveImage(row) ){
            return;
        }

        return "<a class=\"label\" onclick='$.page.del(this,{imageId:$id$})'><span class=\"glyphicon glyphicon-trash\" /></a>"
            .replace("$id$",row.imageId);
    }

    function effectiveImage( row ){
        var statusCode = row.statusCode;
        return "4" === statusCode || "3" === statusCode || "2" === statusCode;
    }

})(jQuery);