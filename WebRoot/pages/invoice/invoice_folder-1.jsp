<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="contextPath" value="${pageContext.request.contextPath}"/>
<c:set var="version" value="1.73"/>
<html>
<head>
    <meta charset="utf-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0"/>
    <title>
        影像夹
    </title>
    <%@include file="/newPages/efinance/common/common-head.jsp" %>
    <%@include file="/newPages/efinance/common/common-head-querylist.jsp" %>
    <link rel="stylesheet" type="text/css"
          href="${contextPath}/newPages/efinance/common/template/page.css?v=${version}"/>
    <link rel="stylesheet" type="text/css" href="${contextPath}/cssStyle/invoice/type.css?v=${version}"/>

    <script>
        $.page = $.page||{}
        $.page.parentId = "${id}"
    </script>

    <script type="text/javascript" src="${contextPath}/scripts/plupload/js/plupload.full.min.js?v=${version}"></script>
    <script type="text/javascript" src="${contextPath}/resources/biz/biz-service.js?v=${version}"></script>
    <script type="text/javascript" src="${contextPath}/resources/framework/util/other-ajax.js?v=${version}"></script>
    <script type="text/javascript"
            src="${contextPath}/newPages/efinance/common/template/list-page.js?v=${version}"></script>
    <script type="text/javascript"
            src="${contextPath}/newPages/app/invoice/folder/invoice_folder.js?v=${version}"></script>
    <style>
        .fileName {
            width: 200px;
            display: inline;
            height: 28px;
            line-height: normal;
            font-size: 12px;
            font-weight: lighter;
            border-color: #66afe9;
            margin-top: -20px;
        }

    </style>
</head>
<body>
<div id="wrapper" class="gray-bg">
    <div class="">
        <div class="p-w-md m-t-sm">
            <div class="row">
                <c:if test="${empty child}">
                    <div class="row" style="margin: 10px 0px"><!--面包屑开始-->
                        <ol class="breadcrumb">
                            <li class="active">
                                <a style="color: #232323;">影像夹</a>
                                <i style="color: red;" id="warnMessage"></i>
                            </li>
                        </ol>
                    </div><!--面包屑结束-->
                </c:if>
                <div class="ibox">
                    <div class="ibox-content">
                        <div id="tt" style="display: block;">
                            <form id="queryForm" method="post">
                                <input type="hidden" id="parentId" name="parentId" value="${id}">
                                <input type="hidden" id="parentName"  value="${parentName}">
                                <div id="queryForm_dynamic">
                                    <div class="row">
                                        <div class="form-group col-sm-3 ">
                                            <label class="font-noraml">
                                                销方名称
                                            </label>
                                            <input type="text" name="keyWord" class="form-control"/>
                                        </div>
                                        <div class="form-group col-sm-6 ">
                                            <label class="font-noraml">金额</label>
                                            <div class="row">
                                                <div class="col-sm-5 ">
                                                    <input type="text" number="true" class="form-control" name="minAmount" min="0"/>
                                                </div>
                                                <div class="col-sm-1">
                                                    _
                                                </div>
                                                <div class="col-sm-5 ">
                                                    <input type="text" number="true" class="form-control" name="maxAmount" min="0"/>
                                                </div>
                                            </div>

                                        </div>
                                    </div>
                                    <div class="row display-none">
                                    </div>
                                    <div class="row">
                                        <div class="col-sm-12">
                                            <div class="text-center bt-div">

                                                <button data-fn="bulkUpload" class="btn-normal-blue no-blacklist" style="line-height: 28px"  type="button">上传影像</button>

                                                <button class="btn-normal-white" type="button" data-fn="imageStatus" style="width: 130px">
                                                    影像上传列表
                                                </button>

                                                <c:if test="${empty child}">
                                                    <button class="btn-normal-white" type="button"
                                                            data-fn="add">
                                                        新建文件夹
                                                    </button>
                                                </c:if>

                                                <button class="btn-normal-blue" type="button" onclick="$.page.query(1)">查询
                                                </button>
                                                <button type="button" class="btn-normal-grey" data-fn="reset">重置
                                                </button>
                                               
                                                <button class="btn-normal-grey limit select-some" type="button"
                                                        data-fn="del" disabled>删除
                                                </button>

                                                <c:if test="${empty child}">
                                                    <button class="btn-normal-grey limit select-one rename"
                                                            type="button"
                                                            data-fn="update"
                                                            style="width: 130px"
                                                            disabled>
                                                        重命名
                                                    </button>
                                                </c:if>

                                                <button class="btn-normal-grey limit select-some move" type="button"
                                                        data-fn="move" disabled>移动影像
                                                </button>
                                                <button class="btn-normal-grey limit select-some intelligent no-blacklist"
                                                        type="button" data-fn="intelligent" disabled>智能报账
                                                </button>
                                                

                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </form>
                        </div>
                        <div>
                            <div id="toolbar">
                                <span style="border: 1px solid #48A0FF;margin-left: -20px;"></span>
                                <span style="font-family: MicrosoftYaHei,serif;font-size: 15px;color: #0B112A;margin-left: 13px;">查询结果</span>
<%--                                <div class="no-blacklist"--%>
<%--                                     style="text-align: right;padding-right: 30px;float: right ;width: 600px">--%>
<%--                                    <div id="xls-import" class="file-upload">--%>
<%--                                        <a id="pickfiles" href="javascript:" style="height: 28px;line-height: 28px">--%>
<%--                                            <button class="btn-normal-blue" type="button" style="height: 28px;line-height: 28px">选择文件</button>--%>
<%--                                        </a>--%>
<%--                                        <input type="text" id="filelist" class="form-control fileName" style="line-height: 28px;margin-top: -15px"--%>
<%--                                               alwaysReadonly="true" readonly="readonly"/>--%>
<%--                                        <button id="uploadfiles" class="btn-normal-blue file-upload-bt" style="line-height: 28px" type="button">--%>
<%--                                            上传--%>
<%--                                        </button>--%>
<%--                                        <button data-fn="bulkUpload" class="btn-normal-blue" style="line-height: 28px"  type="button">批量上传</button>--%>
<%--                                    </div>--%>
<%--                                </div>--%>
                            </div>
                            <%--                        <div class="checkListDiv q_mpsdc_body" style="margin-top: -20px;">--%>
                            <table id="dtItemList" class="table  table-hover q_table table_td_left"></table>
                            <%--                        </div>--%>
                        </div>
                    </div>
                </div>
            </div>
<%--            <div class="row" >--%>
<%--                <div style="text-align: left;padding-left: 30px;width: 130px;float: left">--%>
<%--                    <button class="btn-normal-white" type="button" data-fn="imageStatus" style="width: 130px">--%>
<%--                        影像上传列表--%>
<%--                    </button>--%>
<%--                </div>--%>
<%--                <div class="no-blacklist" style="text-align: right;padding-right: 30px;float: right ;width: 600px">--%>
<%--                    <div id="xls-import" class="file-upload">--%>
<%--                        <a id="pickfiles" href="javascript:" style="height: 28px;" >--%>
<%--                            <button  class="btn-normal-blue file-upload-bt" type="button">选择文件</button>--%>
<%--                        </a>--%>
<%--                        <input type="text" id="filelist" class="form-control fileName" alwaysReadonly="true" readonly="readonly" />--%>
<%--                        <button id="uploadfiles" class="btn-normal-blue file-upload-bt" type="button">上传</button>--%>
<%--                        <button  data-fn="bulkUpload" class="btn-normal-blue file-upload-bt" type="button">批量上传</button>--%>
<%--                    </div>--%>
<%--                </div>--%>
<%--            </div>--%>
        </div>
    </div>
</div>
<iframe id="picturePreview" width="100%" height="100%" style="display: none" src="/FSSC/"></iframe>

</body>
</html>