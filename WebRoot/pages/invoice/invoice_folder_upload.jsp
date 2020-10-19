<%@ page language="java" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@include file="/newPages/efinance/common/v.jsp" %>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8">
    <meta http-equiv="Pragma" content="no-cache">
    <meta http-equiv="Cache-Control" content="no-cache">
    <meta http-equiv="Expires" content="Sat, 01 Dec 2001 00:00:00 GMT">

    <%@include file="/newPages/efinance/common/common-head.jsp" %>

    <link rel="stylesheet" type="text/css"
          href="${pageContext.request.contextPath}/scripts/plupload/js/jquery.plupload.queue/css/jquery.plupload.queue.css"/>
    <script type="text/javascript" src="${pageContext.request.contextPath}/scripts/plupload/js/plupload.full.min.js"></script>

    <script type="text/javascript"
            src="${pageContext.request.contextPath}/scripts/plupload/js/jquery.plupload.queue/jquery.plupload.queue.js"></script>
    <script type="text/javascript" src="${pageContext.request.contextPath}/scripts/plupload/js/i18n/zh_CN.js"></script>
    <script type="text/javascript"
            src="${pageContext.request.contextPath}/newPages/app/invoice/folder/invoice_folder_upload.js?v=${version}"></script>
    <script type="text/javascript" src="${pageContext.request.contextPath}/resources/framework/util/other-ajax.js?v=${version}"></script>
    <style type="text/css">
        .selector {

            width: 150px;
            height: 33px;
            border: 1px solid #36c;
            position: relative
        }

        .selector input {
            width: 122px;
            height: 16px;
            border: 0px solid blue;
            font-size: 12px;
            position: absolute;
            left: 2px;
            top: 1px;
            z-index: 99
        }

        .selector ul {
            width: 124px;
            max-height: 150px;
            margin: 0;
            padding: 0;
            position: absolute;
            left: 1px;
            top: 20px;
            border: 1px solid #b7cadd;
            list-style: none;
            display: none;
            z-index: 100;
            text-align: left;
            overflow: auto;
        }

        .selector li a {
            width: 124px;
            line-height: 24px;
            display: block;
            color: #000;
            background: #fff;
            text-decoration: none;
            text-indent: 10px
        }

        .selector li a:hover {
            background: #ccc
        }

        .selector .arr {
            position: absolute;
            left: 0px;
            top: 0px;
            width: 150px;
            height: 20px;
            cursor: pointer;
        }
    </style>
    <script language="javascript">
        $.page  = $.page ||{}
        $.page.parentId = "${param.parentId}" || "0";
    </script>

<body style="background-color: #EEF6FD;">
<div class="divAccachment" id="divAccachmentId">
    <form id="formId" action="Submit.action" method="post">
        <div style="width: 100%;">
            <div id="uploader" style="width:100%; text-align:center;margin: 0px;padding: 0px;">
                <p></p>
            </div>
        </div>
    </form>
</div>
<br>
</body>
</html>