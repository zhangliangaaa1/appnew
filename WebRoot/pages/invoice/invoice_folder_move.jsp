<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="contextPath" value="${pageContext.request.contextPath}"/>
<c:set var="version" value="1.31"/>
<html>
<head>
    <meta charset="utf-8"/>
    <meta name="viewport" content="width=device-width, initial-scale=1.0"/>
    <title>影像夹</title>
    <%@include file="/newPages/efinance/common/common-head.jsp" %>
    <%@include file="/newPages/efinance/common/common-head-querylist.jsp" %>
    <link rel="stylesheet" type="text/css"
          href="${contextPath}/newPages/efinance/common/template/page.css?v=${version}"/>
    <link rel="stylesheet" type="text/css" href="${contextPath}/cssStyle/invoice/type.css?v=${version}"/>
    <script type="text/javascript" src="${contextPath}/scripts/plupload/js/plupload.full.min.js?v=${version}"></script>
    <script type="text/javascript" src="${contextPath}/resources/biz/biz-service.js?v=${version}"></script>
    <script type="text/javascript" src="${contextPath}/resources/framework/util/other-ajax.js?v=${version}"></script>
    <script type="text/javascript"
            src="${contextPath}/newPages/efinance/common/template/list-page.js?v=${version}"></script>
    <script type="text/javascript"
            src="${contextPath}/newPages/app/invoice/folder/invoice_folder_move.js?v=${version}"></script>
</head>
<body>
<div id="wrapper" class="gray-bg">
    <div class="">
        <div class="p-w-md m-t-sm">
            <div class="row">
                <div class="ibox">
                    <div class="ibox-content">
                        <div id="tt" style="display: block;">
                            <form id="queryForm" method="post">
                                <input name="folderType" type="hidden" value="F" />
                            </form>
                            <div id="queryForm_dynamic" style="margin-top: -30px">
                                <div class="row">
                                    <div class="col-sm-12" >
                                        <div class="text-center bt-div">
                                            选择移动的文件夹
                                            &nbsp;&nbsp;
                                            <button class="btn-normal-white" type="button" data-fn="add" style="width:130px ">
                                                <img src="${pageContext.request.contextPath}/newstatic/img/icon/q/plus.jpg"/>
                                                <font class="q_zx">新建文件夹</font>
                                            </button>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                        <div class="checkListDiv q_mpsdc_body" style="margin-top: -10px">
                            <table id="dtItemList" class="table  table-hover q_table table_td_left"></table>
                        </div>
                    </div>
                </div>
            </div>
            <div class="row">
                <div class="col-sm-12" style="text-align: center;margin-bottom: 10px">
                    <div class=" q_modal_newInvoice" style="border: 0px solid red">
                        <div class="popdiv_query  q_mps_dialog q_mn">
                            <div class=" q_mpsd_content q_mn_content">
                                <div class="q_masdc_footer">
                                    <input type="button" class="q_mpsdcsr_gray" data-fn="close" value="关闭"/>
                                    <input type="button" class="q_mpsdcsr_active" value="确认" data-fn="select"/>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>
</body>
</html>