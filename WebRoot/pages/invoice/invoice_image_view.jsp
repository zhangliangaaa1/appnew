<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page language="java" pageEncoding="UTF-8" %>
<c:set var="contextPath" value="${pageContext.request.contextPath}"/>
<c:set var="version" value="1.11"/>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8">
    <meta http-equiv="Pragma" content="no-cache">
    <meta http-equiv="Cache-Control" content="no-cache">
    <meta http-equiv="Expires" content="Sat, 01 Dec 2001 00:00:00 GMT">
    <title>发票详情</title>
    <%@include file="/newPages/efinance/common/common-head.jsp" %>
    <%@include file="/newPages/efinance/common/common-head-querylist.jsp" %>
    <script type="text/javascript" src="${contextPath}/resources/biz/biz-service.js"></script>
    <script type="text/javascript" src="${contextPath}/newPages/efinance/common/template/add-page.js"></script>
    <script type="text/javascript" src="${contextPath}/resources/framework/util/other-ajax.js"></script>
    <script type="text/javascript"
            src="${contextPath}/newPages/app/invoice/folder/invoice_image_add.js?v=${version}"></script>
    <style type="text/css">
        select {
            width: 100%;
            height: auto
        }

        .input-search {
            height: 20px
        }

        .form-group {
            margin-bottom: 0;
            margin-top: 18px;
            /*padding-top: 8px;*/
            line-height: 0;
        }

        .row {
            /*height: 30px;*/
        }

        .panel {
            margin-left: -13px;
            margin-right: -13px;
        }
    </style>
</head>
<body>
<div class="ibox-content">
    <div class="col-sm-6 ">
        <div class="panel panel-default">
            <div class="panel-heading" style="background-color: #dddddd;">
                <h3 class="panel-title">
                    影像信息详情
                </h3>
            </div>
            <div class="panel-body" style="min-height: 560px;">
                <form id="editForm" class="formControl">
                    <div class="row" style="font-weight: bold;border-bottom: #0d8ddb 1px solid;">
                        <div class="form-group col-sm-6 ">
                            影像要素
                        </div>
                        <div class="form-group col-sm-6 ">
                            影像中的信息
                        </div>
                    </div>
                    <c:forEach items="${bean.data}" var="row" varStatus="xh">
                        <c:if test="${xh.index%2 == 0}">
                            <c:set var="style" value="background-color: #f5f5f5;;"/>
                        </c:if>
                        <c:if test="${xh.index%2 == 1}">
                            <c:set var="style" value=""/>
                        </c:if>
                        <c:if test="${row.required}">
                            <c:set var="required" value="<span class=\"required2\">*</span>"/>
                        </c:if>
                        <div class="row" style="${style}">
                            <div class="form-group col-sm-6 ">
                                    ${row.key} ${required}
                            </div>
                            <div class="form-group col-sm-6 ">
                                <label class="font-noraml">${row.value}</label>
                            </div>
                        </div>
                    </c:forEach>
                </form>
            </div>
        </div>
    </div>
    <div class="col-sm-6 ">
        <div class="panel panel-default">
            <div class="panel-heading" style="background-color: #dddddd;">
                <h3 class="panel-title">
                    影像预览
                </h3>
            </div>
            <div class="panel-body" style="height: 560px;padding: 15px;">
                <jsp:include page="invoice_image.jsp">
                    <jsp:param name="url" value="${contextPath}/${bean.image}"/>
                    <jsp:param name="original" value="${contextPath}/${bean.image}"/>
                    <jsp:param name="maxHeight" value="530px"/>
                </jsp:include>
            </div>
        </div>
    </div>
</div>
</div>
</body>
</html>
