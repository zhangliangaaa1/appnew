
$(function($){
    $.page.init();
});

(function ($) {

    $.page = $.page || {};
    $.page.documentNum = [];
    $.page.result = {};
    $.page.result.documentNum = []
    $.page.result.successNum = 0;
    $.page.result.failNum = 0;

    //-- 状态控制
    $.page.status = {}

    $.page.overdue = {}
    $.page.overdue.time = 15 * 1000;//逾期时间

    $.ajaxUrl = contextPath + "/commonbizajax2report.do";//-- Ajax


    $.page.init = function () {
        plupload.addI18n({
            "Error: Invalid file extension:": "您上传的文件格式不正确！"
        });

        initDialog() || initPluploadQueue()
    }

    function initDialog(){
        if ($(window.parent.document).find(".ui-dialog-titlebar-close").length > 0) {
            $(window.parent.document).find(".ui-dialog-titlebar-close").attr("alwaysVisbled", "true");
        }
    }


    function initPluploadQueue() {

        initElement() || bindEvent()

        function initElement() {
            var options = {};

            options.url = $.ajaxUrl;
            var urlParams = {};
            urlParams.requestId = "invoice_folder_upload";
            urlParams.parentId = $.page.parentId;
            options.multipart_params = urlParams

            options.runtimes = "html5,flash,silverlight,html4";
            options.chunk_size = "100mb";
            options.max_file_size = "25mb";
            options.unique_names = true;
            options.multiple_queues = true;

            options.flash_swf_url = contextPath + '/scripts/plupload/js/Moxie.swf'
            options.silverlight_xap_url = contextPath + '/scripts/plupload/js/Moxie.xap'

            var mime_types = [];
            mime_types[0] = {title: "Image Files", extensions: "jpg,jpeg,png"}
            mime_types[1] = {title: "PDF Files", extensions: "pdf"}
            mime_types[2] = {title: "Other Files", extensions: "heic,sfd"}
            options.filters = {}
            options.filters.mime_types = mime_types;
            options.filters.prevent_duplicates = true;

            $.page.uploader = $("#uploader").pluploadQueue(options);
        }

        function bindEvent() {
            var uploader =  $.page.uploader =  $("#uploader").pluploadQueue();
            if (!uploader) {
                return;
            }
            uploader.bind('FilesAdded', bindFilesAdded);//添加文件
            uploader.bind('FilesRemoved', bindFilesRemoved);//--移除文件
            uploader.bind('BeforeUpload', bindBeforeUpload);//-- 准备上传
            uploader.bind('FileUploaded', bindFileUploaded);//-- 单个文件上传成功
            uploader.bind('UploadComplete', bindUploadComplete);//-- 上传完成
            uploader.bind('Error', bindError);
        }

        function bindBeforeUpload(uploader, files) {
            // var claim = window.parent.claimService.claim;
            // var input = window.parent.claimService.input;

            var docName = $("#docNameInput").val();
            // var urlStr = contextPath +  '&docName=' + docName;
            //
            // uploader.setOption("url", encodeURI(urlStr));

            var EASFileDocName = "";
            var items = $("#SelectorOptions a");
            for (var i = 0; i < items.length; i++) {
                if (items[i].innerText) {
                    EASFileDocName += "&eas&" + items[i].innerText;
                }
            }
            if (EASFileDocName.indexOf("&eas&" + docName) < 0) {
                EASFileDocName += "&eas&" + docName;
            }
            EASFileDocName = EASFileDocName.substring(5);
            setCookie("EASFileDocName", EASFileDocName, 365);
            UiBlockService.blockUI({message: "<h4>正在上传文件，请稍等...</h4>"});

            //-- 队列开始的时候 开启超时任务 : 超时隐藏
            $.page.result.startTime || setOverdueTask();

            $.page.result.startTime =  $.page.result.startTime || new Date();//-- 记录开始时间

            //-- 队列上传 以及发票认证结果 15 秒
            function setOverdueTask() {
                //-- 标记 上传状态  , 当 返回识别结果后 会设置成  false
                $.page.status.uploading = true;
                $.page.status.ocrResult = false;//-
                $.page.overdueTask = setTimeout(function () {

                    //-- 如果 上传程序没执行完成 则隐藏 窗口
                    if( $.page.status.uploading ){
                        $.page.overdue.hide( $.page.result.startTime  )
                        return
                    }
                    //-- 如果 已经上传完成 但是还没返回识别结果 则 关闭窗口 弹出list 页面
                    false === $.page.status.ocrResult && $.page.overdue.close()

                }, $.page.overdue.time )
            }

        }

        //-- 单一文件上传完成后 记录对应时间
        function bindFileUploaded(uploader, files,res ) {

            res = res ||{}
            res = res.response || "{}"
            res = JSON.parse(res)

            var isSuccess  = "ok" === res.res || "ok" === res.status
            isSuccess && success()
            isSuccess || failure()

            function success(){
                $.page.result.documentNum.push(res.data.documentNum)
                $.page.result.successNum++;
            }

            function failure(){
                $.page.result.failNum++;
            }
        }

        //-- 文件上传完成 开始校验 识别结果
        function bindUploadComplete(uploader, files,a,b,c) {

            debugger

            $.page.status.uploading = false

            //-- 本次上传结果
            var result = $.page.result;

            UiBlockService.unblockUI();

            //-- 获取剩余时间 : 如果剩余时间 不足3秒  跳到list 页面
            debugger
            var time = remainingTimeOverdue( result.startTime )
            time = Math.floor( time );
            if( 3 > time ){
                closeWindow( "list" )
                return
            }

            result.time = time;
            getRecognitionResult( result )

            // $.page.initCache()
            $("#docNameInput").val("");

            // $.page.documentNum = [];
            // $.page.result = {};
            // $.page.result.documentNum = []
            // $.page.result.successNum = 0;
            // $.page.result.failNum = 0;


        }

        function bindError(uploader, errObject) {
            debugger
            var errorMessage = "";
            if (!isIE()) {
                var parser = new DOMParser();
                var xmlDoc = parser.parseFromString(errObject.response, "text/xml");
                errorMessage = xmlDoc.getElementById("error1").outerText;
            } else {// Internet Explorer
                var resp = new ActiveXObject("microsoft.xmldom");
                resp.loadXML(XMLHttpReq.responseText);
                errorMessage = resp.getElementById("error1").outerText;
            }
            MessageBox.error(errorMessage);
            UiBlockService.unblockUI();
        }

        function bindFilesAdded(uploader, files) {
            var _h = 37;
            if (navigator.userAgent.indexOf("MSIE 6.0") > 0) {
                _h = 35;
            }
            if (uploader.files.length > 0) {
                $(".moxie-shim").css({top: $(".plupload_add").position().top - _h + (_h)});
            } else {
                $(".moxie-shim").css({top: $(".plupload_add").position().top - _h});
            }
            resizeIframeHeight();
        }

        function bindFilesRemoved(uploader, files) {
            var _h = 37;
            if (navigator.userAgent.indexOf("MSIE 6.0") > 0) {
                _h = 35;
            }
            if (uploader.files.length > 0) {
                $(".moxie-shim").css({top: $(".plupload_add").position().top - _h + (_h)});
            } else {
                $(".moxie-shim").css({top: $(".plupload_add").position().top});
            }
            resizeIframeHeight();
        }
    }

    //- 是否超时 : 15秒
    function isOverdue( startTime   ){
        return  remainingTimeOverdue( startTime ) <= 0;
    }

    //-- 逾期剩余时间 : 开始时间 + 15秒 - 当前时间
    //-- @return 剩余时间(秒)
    function remainingTimeOverdue( startTime ){
        return (startTime.getTime() + $.page.overdue.time - Date.now() ) / 1000;
    }

    //-- 获取识别结果
    //-- 时间为15秒 , 15秒 未返回结果则关闭窗口
    function getRecognitionResult( result ){

        0 === result.documentNum.length || ajaxGetRecognitionResult()

        //-- 请求识别结果
        function ajaxGetRecognitionResult(  ){

            var params = {};
            params.requestId = "invoice_folder_getUploadImgOcrStatus"
            params.time = result.time;
            params.documentNum = result.documentNum ;

            // $.postAjax(  $.ajaxUrl ,params ,success )
            Ajax.post().url( $.ajaxUrl ).data(params).success(success).do();

            function success(res,isOk){
                res = res ||{}
                res.message = res.message || []
                var data = res.data;

                if( isOk && "original" === data.tag  ){
                    MessageBox.error(  res.message );
                    $.page.documentNum = [];
                    $.page.result = {};
                    $.page.result.documentNum = []
                    $.page.result.successNum = 0;
                    $.page.result.failNum = 0;
                    return
                }

                if( !isOk ){
                    MessageBox.error(  res.message );
                    return
                }

                closeWindow(  data.tag ,data );
            }
        }
    }

    function closeWindow( tag ,data ){

        UiBlockService.unblockUI();

        if( "list" !== tag && "view" !== tag  ){
            return
        }

        if( $.page.status.hide ){
           return CommonReportService.closeDialog( );
        }

        data = data ||{}

        var res = {};
        res.tag = tag
        res.parentId = $.page.parentId
        res.id = data.id
        res.folderName = data.folderName
        CommonReportService.closeDialog(res);
    }

    function resizeIframeHeight() {
        if ($(window.parent.document).find("#attachIframeId").length > 0) {
            $(window.parent.document).find("#attachIframeId").height($("#divAccachmentId").height() + 8);
        }
    }


    //-- 逾期操作
    $.page.overdue = $.page.overdue || {}

    //- 逾期后隐藏
    $.page.overdue.hide = function(startTime){
        return !isOverdue(startTime) || hide()
        function hide(){
            $.page.status.hide = true;
            return true
        }
    }

    //-- 逾期后 关闭 并弹出 list 页面
    $.page.overdue.close = function(startTime){
        return !isOverdue(startTime) || close()
        function close(){
            closeWindow( "list")
            return true
        }
    }


    //-- 页面缓存初始化
    $.page.setCache = function ( key,value ){

        $.page.result = $.page.result || {}
        $.page.result.documentNum = [] //-- 上传成功后的 IMG ID
        $.page.result.successNum = 0; //-- 成功数量
        $.page.result.failNum = 0;//-- 失败数量

        //-- 控制页面撞他
        $.page.status = $.page.status || {}
        $.page.status.hide =  $.page.status.hide || false //-- 当前窗口是否隐藏(后台上传)
    }

})(jQuery);
