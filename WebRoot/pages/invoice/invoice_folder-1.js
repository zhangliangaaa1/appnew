//+++++++++++++++++++++++++++++

$(function($){
    $.page.init();//-- 页面初始化

    blacklist();

    function blacklist(){

        // var parentId = $("#parentId").val()||"0"
        //
        // if("0" !== parentId  ){
        //     return
        // }

        var params = {}
        params.requestId = $.page.limit
        Ajax.post().url( $.ajaxUrl ).async( false ).data(params).success(success).do();
        function success( res ,isSuccess ){
            res = res || {}
            if( isSuccess ){
                return
            }
            $(".no-blacklist").remove()
            if( $("#parentId").val() && "0" !== $("#parentId").val() ){
                return
            }

            //-- 隐藏

            MessageBox.error( res.message );
            $(".modal-error-imge").height( "auto" );
        }
    }

    $.page.mark = "invoice";
    $.page.onFocus(); 
});


(function ($) {


    var action = "invoice_folder_";

    $.page.idField = "id"
    $.page.opt.del.requestId =  action +  "del";//--删除地址
    $.page.opt.del.message = "确定删除选定的发票吗?"
    $.page.opt.del.verify = function ( rows , verify ){

        if( verify ){
            return  true
        }

        var res = true;
        var keys = {}

        $.each( rows , function (){
            if( keys[ this.imageId ] ){
                res = false
                return false
            }
            keys[ this.imageId ] = this.imageId
        })

        res || confirm()

        return res;

        function confirm(){

            return MessageBox.confirm({
                message: "您选择的发票包含一图多票类型,操作会导致关联发票被删除,是否删除?" ,
                action4Yes: action4Yes,
                action4No: function action4No() {}
            });

            function action4Yes(){
                $.page.del( this,null,true, {needPrompt:false} )
            }
        }
    }


    $.page.table.requestId =  "invoice_folder";//-- 查询 ACTION
    $.page.opt.add.requestId =  action +  "add_page";


    //-- 新增 : 弹出新页面方式
    $.page.add = function( tag, id ) {

        //-- 验证
        if( false === $.page.opt.add.verify && $.page.opt.add.verify() ){
            return;
        }

        var url = "/service.do?requestId=" +  $.page.opt.add.requestId;

        var title = id ? "修改" : "新增";

        if( $.page.opt.add.title  ){
            title = $.page.opt.add.title + "-" + title;
        }

        if ( id ){
            url += "&" + $.page.idField + "=" + id;
        }

        var width = $.page.opt.add.width ||fit().width*0.9
        var height = $.page.opt.add.height ||fit().height*0.9

        //-- 打开弹窗
        dialogService.openDialog(callbackFunction, null, url,  title,width , height);

        function callbackFunction( success ){ //-- 关闭窗口触发
            success && $.page.query( 1 );
        }

    };


    //-- 智能报账 图片校验
    $.page.getCheckSaveOrSendClaim = "minimalist_getCheckSaveOrSendClaim";
    $.page.limit = "minimalist_limit";

    $.page.opt.add.width =  600;
    $.page.opt.add.height =  400;

    // $.page.opt.view.width = 900
    // $.page.opt.view.height = 600


    $.page.opt.view.requestId = action + "view"; //-- 详情地址
    $.page.opt.view.fn = function (){
        $.page.query()
    }


    $.page.fileUpload.pmatch =  /\.(JPG|jpg|PNG|png|HEIC|heic|JPGE|jpeg|PDF|pdf|OFD|ofd|SFD|sfd)$/i;


    $.page["xls-import"] = action + "upload&parentId=" + $.page.parentId; //-- 上传文件
    // $.page.fileUpload.params = {parentId:$("#parentId").val()}

    $.page.fileUpload.beforeUpload = function (){

        var limit = true;

        //-- 校验图片
        var params = {}
        params.requestId = $.page.limit
        Ajax.post().url( $.ajaxUrl ).async( false ).data(params).success(success).do();
        function success( res ,isSuccess ){
            res = res || {}
            if( !isSuccess ){
                MessageBox.error( res.message );
                $(".modal-error-imge").height( "auto" );
                limit = false
            }
        }
        return limit
    }

    //-- 批量上传
    $.page.bulkUpload = function (e ,parentId,parentName){

        if( true ===  $.page.bulkUploading ){
            MessageBox.error( "还有正在上传的文件在后台执行,请稍后再试!" );
            $(".modal-error-imge").height( "auto" );
            return
        }

        if( !$.page.fileUpload.beforeUpload() ){
            return
        }

        //-- 可以是选着的子文件夹 : 当只选则了一个文件夹时
        var select = getSelect();
        if( select ){
            parentId = parentId||select.id
            parentName = parentName|| select.folderName
        }

        parentId = parentId|| $("#parentId").val();
        parentName = parentName|| $("#parentName").val();

        if( tag() !== window ){
            tag().$.page.bulkUpload( e, parentId, parentName )
            CommonReportService.closeDialog();
            return;
        }

        parentId = parentId||"0"
        parentName = parentName||"根目录"

        var url = "/newPages/app/invoice/folder/invoice_folder_upload.jsp"
        url += "?parentId=" + parentId

        var width = $( getWindow() ).width() * 0.95;
        var height =$( getWindow() ).height() * 0.8;


        var  warnMessage = $("#warnMessage");
        warnMessage.text( "影像还在后台上传中,请不要关闭当前窗口,否则部分影像可能不能正常上传!" )
        $.page.bulkUploading = true;

        dialogService.openDialog(callbackFunction, null, url, "文件上传[parentName]".replace( "parentName",parentName )  , width , height);
        function callbackFunction( res ){
            $.page.bulkUploading = false;
            warnMessage.text( "" )
            if( !res ){
                return
            }
            "list" === res.tag && $.page.imageStatus(e, res.parentId )

            if(  "view" !== res.tag ){
                return
            }

            $.page.opt.view.title = res.folderName
            $.page.view( {id:res.id} )
        }


        function getSelect(){
            var selected = $.page.selected() || [];
            if( 1 !== selected.length  ){
                return;
            }
            selected = selected[0];
            if( "F" !== selected.folderType ){
                return
            }
            return selected;
        }

    }

    function tag(){

        if( !parent || !parent.$.page || "invoice"!==  parent.$.page.mark ){
            return window;
        }

        return parent
    }

    //-- 智能报账
    $.page.intelligent = function ( e, ids , parentId, parentName ){

        ids = ids || getSelectedGId()
        parentId = parentId || $("#parentId").val() || "0";// 调用页面ID 如果非 跟节点 关闭窗口后 打开父亲页面

        if(!ids || 0 ===ids.length  ){
            MessageBox.error('请选择影像');
            return;
        }

        //-- 判断当前窗口 是否是根窗口 不是则 调用根窗口打开方法
        if( tag() !== window ){
            tag().$.page.intelligent( e,ids, parentId, parentName )
            CommonReportService.closeDialog();
            return;
        }

        //-- 校验图片
        var params = {id: ids }
        params.requestId = $.page.getCheckSaveOrSendClaim
        Ajax.post().url( $.ajaxUrl ).data(params).success(success).do();
        function success( res, isSuccess ){
            res = res || {}
            isSuccess ||  MessageBox.error( res.message );
            isSuccess && res.data === "WARNING"  &&  confirm( res.message );
            isSuccess && res.data !== "WARNING"  && openIntelligentWindow()

            isSuccess || ( "0" !== parentId && $.page.view( {id:parentId} ) )

            $(".modal-error-imge").height( "auto" );
        }

        function confirm(message){

            return MessageBox.confirm({
                message: message ,
                action4Yes: action4Yes,
                action4No: function action4No() {
                    "0" !== parentId && $.page.view( {id:parentId} )
                }
            });

            function action4Yes(){
                openIntelligentWindow();
            }
        }

        //-- 校验通过 打开 极简页面
        function openIntelligentWindow(){
            var url =  "/service.do?requestId=minimalist_page";
            url += getInvoiceIdsUrl();
            url += "&parentId=" + parentId ;


            var width =  $( getWindow() ).width() * 0.95;
            var height = $( getWindow() ).height() * 0.95;
            dialogService.openDialog(callbackFunction, null, url, "智能报账", width , height);

            function callbackFunction( res ){
                res && $.page.query()
                res && success()

                debugger

                  "0" !== parentId &&  $.page.view( {id:parentId} )

                function success(){
                    // var mesage =  "单据[claimNo]发送成功!".replace( "claimNo",res.claimNo  )
                    // MessageBox.notice( mesage, "提交成功",function (){} )
                }

            }

            function getInvoiceIdsUrl(){
                var key = "&" + escape("invoiceId[]") + "="

                var invoiceIds = "";
                $.each( ids, function ( i,id ){
                    invoiceIds += key + id
                } )
                return invoiceIds
            }

        }

        //-- 获取ID
        function getSelectedGId() {

            var selected = $.page.selected() || [];
            if( 0 === selected.length  ){
                return [];
            }
            // var ids = getSelectedGId();

            var id = [];
            $.each( selected, val );
            function val() {
                id[id.length] = this[ $.page.idField ];
            }
            return id;
        }
    }


    //-- 影像状态列表
    $.page.imageStatus = function ( e, parentId ){
        parentId = parentId || $("#parentId").val()||0
        var url =  "/service.do?requestId=" + action + "image_status_page";
        url += "&id=" + parentId
        var width = $( getWindow() ).width() * 0.95;
        var height =$( getWindow() ).height() * 0.8;
        dialogService.openDialog(callbackFunction, null, url, "影像上传列表", width , height);

        function callbackFunction( res ){
            $.page.query()
        }

    }

    function getWindow( obj ){

        obj = obj||this
        if( obj.window ===  top.window){
            return obj.window
        }

        if( obj.parent.window ===  top.window){
            return obj.window
        }
        return getWindow( obj.parent)
    }

    //-- 移动影像
    $.page.move = function() {

        var selected = $.page.selected() || [];
        if( 0 === selected.length  ){
            MessageBox.error('请选择要移动的影像');
            $(".modal-error-imge").height( "auto" );
            return;
        }

        var url =   "/newPages/app/invoice/folder/invoice_folder_move.jsp";

        //-- 打开弹窗
        dialogService.openDialog(callbackFunction, null, url,  "移动影像",500 , 600);
        function callbackFunction( row){ //-- 关闭窗口触发

            if( !row ){
                return
            }

            //-- 不能移动至同级目录
            if( selected[0].parentId == row.id ){
                MessageBox.error('选择目录相同无法移动');
                $(".modal-error-imge").height( "auto" );
                return;
            }

            var params = {};
            params.ids = getSelectedGId().join(",");
            params.moveParentId = row.id;
            params.requestId =  action +  "move";

            //-- 移动文件
            $.runAjax(  $.ajaxUrl , params ,callBack);//ajax

            //-- 返回结果的 回调
            function callBack(res ,isOk) {
                isOk && $.page.query();
                if( isOk && parent && parent.$ && parent.$.page && parent.$.page.query ){
                    parent.$.page.query()
                }
            }

            //-- 获取ID
            function getSelectedGId() {
                var id = [];
                $.each( selected, val );
                function val() {
                    id[id.length] = this[ $.page.idField ];
                }
                return id;
            }
        }

    };

    var table = $.page.table.options = {};
    // table.onDblClickCell = function ( field, value, row ){
    //     // if( "showInvoiceType" === field && "F" === row.folderType ){
    //     //     $.page.add( this, row.id )
    //     //     return
    //     // }
    //
    //     $.page.opt.view.title = row.folderName
    //
    //     $.page.view( row )
    // }


    table.onDblClickRow = function (row, $element){
        $.page.opt.view.title = row.folderName

        $.page.view( row )
    }
    table.onClickRow = table.onDblClickRow
    table.striped = true

    table.clickToSelect = true//-- 点击选中

    //-- 复选框 选着/取消触发 按钮权限
    table.onCheck =onCheck
    table.onUncheck = onUncheck
    table.onCheckAll = checkLimit
    table.onUncheckAll = checkLimit
    table.toolbar = '#toolbar';
    table.toolbarAlign = '#right';
    table.showRefresh = false;

    $.page.process = false

    //-- 选择一行时触发
    function onCheck( row ){
        cascadeCheck( row,"checkBy" )
        // cascadeCheck( row,"check" )
    }
    //-- 取消一行时触发
    function onUncheck( row ){
        // cascadeCheck( row,"uncheck" )
        cascadeCheck( row,"uncheckBy" )
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
            $('#dtItemList').bootstrapTable(check, {field:"imageId", values:[ imageId] })
        } )

        checkLimit()

        $.page.process = false
    }



    function checkLimit(){

        disable(".limit");//-- 全部按钮禁用

        var selected = $.page.selected() || [];

        var selectSome = selectSomeLimit();//-- 是否选着了多个
        var selectOne = selectOneLimit();//-- 是否选着了一个
        renameBtLimit()||moveBtLimit() || intelligentBtLimit()

        //-- 至少选着一个控制
        function selectSomeLimit() {
            var disabled = 0 === selected.length
            disabled || enable(".select-some")
            disabled && disable(".select-some");
            return !disabled;
        }

        //-- 只选着了一个控制
        function selectOneLimit() {
            var disabled = !selectSome;
            if (!disabled) {
                disabled = selected.length > 1;
            }
            disabled || enable(".select-one") ;
            disabled && disable(".select-one");
            return !disabled;
        }

        //-- 重命名按钮权限
        function renameBtLimit() {
            if (!selectOne) {
                return;
            }
            var disabled =  selected[0].folderType !== 'F'
            //-- 判断是否是 文件夹
            disabled || enable(".rename")
            disabled && disable(".rename")
        }


        //-- 移动按钮权限
        function moveBtLimit() {
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
            disabled || enable(".move")
            disabled && disable(".move")
        }

        //-- 智能报账权限
        function intelligentBtLimit() {
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
            disabled || enable(".intelligent")
            disabled && disable(".intelligent")
        }

    }

    function enable(select) {
        $(select).attr("disabled", false).addClass("btn-normal-white").removeClass("btn-normal-grey")
    }

    function disable(select) {
        $(select).attr("disabled", true).addClass("btn-normal-grey").removeClass("btn-normal-white")
    }

    var columns = $.page.table.columns = [];

    //-- 过期的数据不允许勾选:  不允许修改和删除
    columns[ columns.length ] = { checkbox:"true" , formatter:function (value, row) {
            return { disabled:row.isEnd }
    }};

    columns[columns.length] = {
        field: "rn", title: "", width: "50px"
    };

    // columns[columns.length] = {field:"showInvoiceType", title: "",formatter:type,width:"40px"};
    columns[columns.length] = {field:"showInvoiceType", title: "文件名称",formatter:type,width:"300px"};
    columns[columns.length] = {field:"createDate", title: "创建时间",formatter:render4TdLink,width:"100px"};

    columns[columns.length] = {field:"folderDataCount", title: "影像总数",formatter:render4TdLink,width:"100px"};
    columns[columns.length] = {field:"invoiceAmount", title: "合计金额",formatter:render4TdLink,width:"100px"};

    columns[columns.length] = {field:"fileName", title: "影像原名称",formatter:render4TdLink,width:"400px"};
    columns[columns.length] = {field:"imageId2", title: "imageId",visible : false, formatter : imageId};
    columns[columns.length] = {field:"data", title: "查看详情",formatter:option,width:"100px"};


    //-- 发票类型  默认未知
    function imageId(value,row){
        value  = value || row.id
        return value;
    }
    function type(value,row){
        value = "F" === row.folderType&& "0" || value || 5;//-- 默认未知

        var em = "<span class=\"invoice-icon-type\" title='folderName' >&nbsp;&nbsp;" ;

        if(  "F" === row.folderType  ){
            em +=   "<a href=\"javascript:void(0);\" >folderName</a>"
        }else{
            em += "folderName";
        }
        em += "</span>"

        return em.replace("type",value)
            .replace("$id$",row.id)
            .replace("folderName",row.folderName)
            .replace("folderName",row.folderName );
    }

    function option(value,row){

        var bt = "<button type=\"button\" class=\"btn btn-default btn-sm\" title='详情' onclick='$.page.opt.view.title =\"$folderName$\";$.page.view({id:$id$})' >"
        bt += "<span class=\"glyphicon glyphicon-zoom-in\" />"
        bt += "</button>"
        bt = bt.replace("$folderName$", row.folderName).replace("$id$", row.id);

        bt += "&nbsp;"
        bt += "<button type=\"button\" class=\"btn btn-default btn-sm\" title='删除'  onclick='$.page.del(this,{id:$id$})'>" .replace("$id$",row.id);
        bt += "<span class=\"glyphicon glyphicon-trash \" />";
        bt += "</button>";


        return bt;
    }


    //-- 查询
    $.page.query = function( e , page  ) {

        page = page || (jQuery.isNumeric(e) && e);

        disable(".limit");//-- 全部按钮禁用

        var table = $('#dtItemList');
        var pageNumber = table.bootstrapTable('getOptions').pageNumber;
        page = page|| pageNumber;

        //-- 当前页 与 预期页不一样则 跳转 否则 即刷新
        page === pageNumber &&  table.bootstrapTable('refresh')
        page === pageNumber ||  table.bootstrapTable('selectPage',page)


    };


    function render4TdLink ( value  ) {
        return createTdLinkCommon("",value);
    }
    
  //-- 事件绑定 : 获取焦点时input全选
    $.page.onFocus = function (){
    	$("#queryRow input").focus(function(){
    		this.select();
      	});
    }


})(jQuery);