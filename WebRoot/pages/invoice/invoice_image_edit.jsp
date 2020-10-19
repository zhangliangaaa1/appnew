<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
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
    <title>发票详情</title>
    <%@include file="/newPages/efinance/common/common-head.jsp" %>
    <%@include file="/newPages/efinance/common/common-head-querylist.jsp" %>
    <link href="${contextPath}/newPages/app/invoice/folder/invoice_image_edit.css?v=${version}" rel="stylesheet">
    <script type="text/javascript" src="${contextPath}/resources/biz/biz-service.js"></script>
    <script type="text/javascript" src="${contextPath}/newPages/efinance/common/template/list-page.js"></script>
    <script type="text/javascript" src="${contextPath}/resources/framework/util/other-ajax.js"></script>
    <script type="text/javascript"
            src="${contextPath}/newPages/app/invoice/folder/invoice_image_add.js?v=${version}"></script>
    <script type="text/javascript"
            src="${contextPath}/newPages/app/invoice/folder/invoice_image_edit.js?v=${version}"></script>
</head>
<body>
<div class="ibox-content">
    <div class="row">
        <div class="col-sm-6 ">
            <div class="panel panel-default" id="imageDetlDiv">
                <div class="panel-heading" style="background-color: #dddddd;">
                    <h3 class="panel-title">
                        影像信息详情
                    </h3>
                </div>
                <div class="panel-body" style="min-height: 560px;">
                    <form id="editForm" class="formControl">
                        <div class="row" style="font-weight: bold;border-bottom: #0d8ddb 1px solid;">
                            <div class="form-group left">
                                影像要素
                            </div>
                            <div class="form-group">
                                影像中的信息
                            </div>
                        </div>
                        <div id="fields">

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
                                <c:set var="requiredB" value="required"/>
                            </c:if>
                            <div class="row" style="${style}">
                                <div class="form-group left">
                                        ${required}${row.key}
                                </div>
                                <c:if test="${row.editable}">
                                    <div class="form-group right" style="margin-bottom: 3px">
                                        <c:if test="${row.inputType == 'chosen'}">
                                            <select name="${row.keyTitle}" id="${row.keyTitle}"
                                                    class="form-control chosen ${requiredB} "
                                                    onchange="$.page.changeType()">
                                                <c:forEach var="type" items="${invoiceType}">
                                                    <c:if test="${row.value == type.key}">
                                                        <c:set var="selected" value="selected"/>
                                                    </c:if>
                                                    <c:if test="${row.value != type.key}">
                                                        <c:set var="selected" value=""/>
                                                    </c:if>
                                                    <option value="${type.key}"  ${selected}>${type.value}</option>
                                                </c:forEach>
                                            </select>
                                        </c:if>
                                        <c:if test="${row.inputType == 'input'}">
                                            <input type="text" name="${row.keyTitle}" id="${row.keyTitle}"
                                                   value="${row.value}" class="form-control ${requiredB}"/>
                                        </c:if>
                                        <c:if test="${row.inputType == 'input_date'}">
                                            <input class="form-control text Wdate ${requiredB}"
                                                   value="${fn:substring(row.value , 0, 4)}-${fn:substring(row.value , 4, 6)}-${fn:substring(row.value , 6, 8)}"
                                                   name="${row.keyTitle}"
                                                   id="${row.keyTitle}"
                                                   readonly
                                                   onFocus="WdatePicker({dateFmt:'yyyy-MM-dd',alwaysUseStartDate:true});"
                                                   type="text">
                                        </c:if>
                                        <c:if test="${row.inputType == 'input_time'}">
                                            <input class="form-control text Wdate ${requiredB}"
                                                   value="${row.value}"
                                                   name="${row.keyTitle}"
                                                   id="${row.keyTitle}"
                                                   readonly
                                                   onFocus="WdatePicker({dateFmt:'HH:mm',alwaysUseStartDate:true});"
                                                   type="text">
                                        </c:if>
                                    </div>
                                </c:if>
                                <c:if test="${!row.editable}">
                                    <div class="form-group">
                                        <label class="font-noraml">${row.value}</label>
                                    </div>
                                </c:if>
                            </div>
                        </c:forEach>
                    </form>
                    <div class="row">
                        <div class="col-sm-12" style="text-align: center;">
                            <div class=" q_modal_newInvoice" style="border: 0px solid red">
                                <div class="popdiv_query  q_mps_dialog q_mn">
                                    <div class=" q_mpsd_content q_mn_content">
                                        <div class="q_masdc_footer">
                                            <button class="q_mpsdcsr_active" id="saveBtn" data-fn="save">保存</button>
                                            <button class="q_mpsdcsr_active" id="verificationBt"  data-fn="verification">发票验真</button>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
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
                        <jsp:param name="maxHeight" value="530px"/>
                        <jsp:param name="include" value="true"/>
                    </jsp:include>
                </div>
            </div>
        </div>
    </div>
    <form id="queryForm" method="post">
        <input type="hidden" id="id" name="id" value="${id}">
    </form>
    <div class="row" style="display: none">
        <div class="panel panel-default">
            <div class="panel-heading" style="background-color: #dddddd;">
                <h3 class="panel-title">
                    修改记录
                </h3>
            </div>
            <div class="panel-body">
                <div class="checkListDiv q_mpsdc_body" style="margin-top: -20px;">
                    <table id="dtItemList" class="table  table-hover q_table table_td_left"></table>
                </div>
            </div>
        </div>
    </div>
</div>
<div id="template" style="display: none">
    <div class="row">
        <div class="form-group left">
        </div>
        <div class="form-group val">
        </div>
    </div>
</div>
<div id="templateChosen" style="display: none">
    <select class="form-control chosen" option-fn="changeType" >
        <c:forEach var="type" items="${invoiceType}">
            <option value="${type.key}" >${type.value}</option>
        </c:forEach>
    </select>
</div>

</body>
</html>
