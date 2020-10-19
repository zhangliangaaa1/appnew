<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="contextPath" value="${pageContext.request.contextPath}"/>
<c:set var="version" value="1.33"/>
<html>
<head>
    <meta charset="utf-8"/>
    <meta name="viewport" content="width=device-width, initial-scale=1.0"/>
    <title>智能填单-发票选择界面</title>
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
            src="${contextPath}/newPages/app/invoice/folder/invoice_image_select.js?v=${version}"></script>
    <style>
        .fileName {
            width: 200px;
            display: inline;
            height: 28px;
            line-height: normal;
            font-size: 12px;
            font-weight: lighter;
            border-color: #66afe9;
        }
    </style>
</head>
<body>
<div id="wrapper" class="gray-bg">
    <div class="">
        <div class="p-w-md m-t-sm">
            <div class="row">
                <div class="ibox">
                    <div class="ibox-content">
                        <div id="tt" style="display: block;">
                            <form id="queryForm" method="post" action="${contextPath}/service.do">
                                <input type="hidden" id="requestId" name="requestId" value="invoice_folder_image_select_page">
                                <input type="hidden" id="id" name="id">
                                <input type="hidden" id="parentId" name="parentId" value="${id}">
                                <input type="hidden" id="limit" name="limit" value="99999">
                                <input type="hidden" id="offset" name="offset" value="0">
                                <input type="hidden" id="divId4OpenQueryDialog" name="divId4OpenQueryDialog" value="${divId4OpenQueryDialog}">
                                <c:forEach items="${idNotIn}" var="id">
                                    <input type="hidden" name="idNotIn[]" value="${id}">
                                </c:forEach>
                            </form>
                        </div>
                        <div class="checkListDiv q_mpsdc_body" style="margin-top: -20px;">
                            <table id="dtItemList" class="table  table-hover q_table table_td_left"></table>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>
<div class="navbar-fixed-bottom bottom-area" style="height: 40px;padding-left: 0">
    <div class="col-sm-12" >
        <div class=" q_modal_newInvoice" style="border: 0px solid red">
            <div class="popdiv_query  q_mps_dialog q_mn">
                <div class=" q_mpsd_content q_mn_content">
                    <div class="q_masdc_footer">
                        <input type="button" class="q_mpsdcsr_gray btnBack" data-fn="close" value="关闭"/>
                        <input type="button" class="btn-normal-grey limit select-some select" value="确定" data-fn="select"/>
                        <c:if test="${!empty id && id != 0}">
                        <input type="button" class="q_mpsdcsr_gray btnBack" data-fn="backtrack" value="返回"/>
                        </c:if>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>
</body>
</html>