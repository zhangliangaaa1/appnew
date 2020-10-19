<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="contextPath" value="${pageContext.request.contextPath}"/>
<c:set var="version" value="1.31"/>
<html>
<head>
    <meta charset="utf-8"/>
    <meta name="viewport" content="width=device-width, initial-scale=1.0"/>
    <title>影像图片</title>

    <c:if test="${empty param.include}">
        <%@include file="/newPages/efinance/common/common-head.jsp" %>
        <%@include file="/newPages/efinance/common/common-head-querylist.jsp" %>
        <link rel="stylesheet" href="${contextPath}/pages/cc/assets/css/bootstrap.min.css"/>
        <script src="${contextPath}/pages/cc/assets/js/jquery.min.js"></script>
        <script src="${contextPath}/pages/cc/assets/js/bootstrap.min.js"></script>
    </c:if>
    <link rel="stylesheet" type="text/css" href="${contextPath}/pages/cc/css/normalize.css"/>
    <link rel="stylesheet" href="${contextPath}/pages/cc/assets/css/bootstrap.min.css"/>
    <link rel="stylesheet" href="${contextPath}/pages/cc/dist/viewer.css"/>
    <link rel="stylesheet" href="${contextPath}/pages/cc/css/main.css"/>
    <script src="${contextPath}/pages/cc/dist/viewer.js"></script>
    <script src="${contextPath}/pages/cc/assets/js/main.js"></script>

</head>
<body>
<div id="wrapper" class="gray-bg">
    <div class="">
        <div class="p-w-md m-t-sm" style="margin: 0;padding: 0">
            <div class="row" style="text-align: center;margin: 0;">
                <div id="div_image" class="docs-pictures clearfix">
                    <c:if test="${empty param.maxHeight}">
                        <c:set var="maxHeight" value="100%" />
                    </c:if>
                    <c:if test="${!empty param.maxHeight}">
                        <c:set var="maxHeight" value="${param.maxHeight}" />
                    </c:if>
                    <c:if test="${empty param.maxWidth}">
                        <c:set var="maxWidth" value="100%" />
                    </c:if>
                    <c:if test="${!empty param.maxWidth}">
                        <c:set var="maxWidth" value="${param.maxWidth}" />
                    </c:if>

                    <img src="${param.url}" class="img-rounded viewer-toggle" height="auto" width="auto" data-original="${param.original}" style="max-width:${maxWidth};max-height: ${maxHeight};" >
                </div>
            </div>
        </div>
    </div>
</div>
</body>
</html>