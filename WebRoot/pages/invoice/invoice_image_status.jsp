<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="contextPath" value="${pageContext.request.contextPath}"/>
<c:set var="version" value="1.3"/>
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
            src="${contextPath}/newPages/app/invoice/folder/invoice_image_status.js?v=${version}"></script>

    <%--    <link rel="stylesheet" type="text/css" href="${contextPath}/pages/cc/css/normalize.css" />--%>
    <%--    <link rel="stylesheet" type="text/css" href="${contextPath}/pages/cc/css/default.css" />--%>
    <%--    <link rel="stylesheet" href="${contextPath}/pages/cc/assets/css/bootstrap.min.css" />--%>
    <link rel="stylesheet" href="${contextPath}/pages/cc/dist/viewer.css"/>
    <%--    <link rel="stylesheet" href="${contextPath}/pages/cc/css/main.css" />--%>
    <%--    <script src="${contextPath}/pages/cc/assets/js/jquery.min.js"></script>--%>
    <%--    <script src="${contextPath}/pages/cc/assets/js/bootstrap.min.js"></script>--%>
    <script src="${contextPath}/pages/cc/dist/viewer.js"></script>
    <script src="${contextPath}/pages/cc/assets/js/main.js"></script>
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
                                <input type="hidden" id="parentId" name="parentId" value="${id}">
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
<div>
    <div id="div_image" class="docs-pictures clearfix" style="display: none">
    </div>
</div>
<div>
    <div id="div_image1" class="docs-pictures clearfix" style="display: none">
    </div>
</div>
</body>
</html>