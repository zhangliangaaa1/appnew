<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page language="java" pageEncoding="UTF-8" %>
<c:set var="contextPath" value="${pageContext.request.contextPath}"/>
<c:set var="version" value="1.21"/>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8">
    <meta http-equiv="Pragma" content="no-cache">
    <meta http-equiv="Cache-Control" content="no-cache">
    <meta http-equiv="Expires" content="Sat, 01 Dec 2001 00:00:00 GMT">
    <%@include file="/newPages/efinance/common/common-head.jsp" %>
    <%@include file="/newPages/efinance/common/common-head-querylist.jsp" %>
    <script type="text/javascript" src="${contextPath}/resources/biz/biz-service.js?v=${version}"></script>
    <script type="text/javascript" src="${contextPath}/newPages/efinance/common/template/add-page.js?v=${version}"></script>
    <script type="text/javascript" src="${contextPath}/resources/framework/util/other-ajax.js?v=${version}"></script>
    <script type="text/javascript"
            src="${contextPath}/newPages/app/invoice/folder/invoice_folder_add.js?v=${version}"></script>
    <style type="text/css">
        select {
            width: 100%;
            height: auto
        }

        .input-search {
            height: 20px
        }
    </style>
</head>
<body>
<div class="ibox-content">
    <form id="editForm" style="margin-top:-50px" class="formControl">
        <input type="hidden" name="id" value="${bean.id}"/>
        <div class="row base-area">
            <div class="form-inline">
                <div class="form-group col-sm-4 ">
                    <input type="text" id="folderName"  maxlength="30" name="folderName" value="${bean.folderName}"  class="form-control required "/>
                </div>
            </div>
        </div>
    </form>
</div>
<div class="row">
    <div class="col-sm-12" style="text-align: center;">
        <div class=" q_modal_newInvoice" style="border: 0px solid red">
            <div class="popdiv_query  q_mps_dialog q_mn">
                <div class=" q_mpsd_content q_mn_content">
                    <div class="q_masdc_footer">
                        <input type="button" class="q_mpsdcsr_active btnSaveLine" value="保存" data-fn="save"/>
                        <input type="button" class="q_mpsdcsr_gray btnBack" data-fn="close" value="关闭"/>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>
</body>
</html>
