<%@ page contentType="text/html;charset=UTF-8" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>

<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">

    <title>我的草稿</title>
    <%@include file="/newPages/efinance/common/common-head.jsp" %>
    <%@include file="/newPages/efinance/common/common-head-querylist.jsp" %>
    <style type="text/css">
        .image{
            width: 26px;
            height: 26px;
            background: #FFFFFF;
            border: 1px solid #48A0FF;
            border-radius: 4px;
            display: inline-block;
            padding-top: 2px;
            padding-left: 4px;
        }
        .warning{
            margin-left: 10px;
        }
        .trRed{
			background-color:#ff8686 !important;
		}
    </style>
</head>

<body>
<form id="queryForm" name="queryForm" method="post" action="commonbizajax2report.do?requestId=AllClaimSavedList">
    <input type="hidden" id="IsAdvancedSerach" name="IsAdvancedSerach" value="1">
    <div id="wrapper" class="gray-bg">
        <%--<div id="page-wrapper" class="gray-bg">--%>
        <div class="">
            <div class="p-w-md m-t-sm">
                <div class="row">
                    <div class="row" style="margin: 10px 0px"><!--面包屑开始-->
                        <ol class="breadcrumb">
                            <li class="active"><a style="color: #232323;">我的草稿</a></li>
                        </ol>
                    </div><!--面包屑结束-->
                    <div class="ibox">
                        <div class="ibox-content">
                            <div id="tt" style="display: block;">
                                <div class="row">
                                    <div class="col-sm-3">

                                        <div class="form-group">
                                            <label class="font-noraml">报账单编号</label>
                                            <input type="text" id="claimNo" name="claimNo" value=""
                                                   placeholder="请填写报账单编号" class="form-control" c-model="myTodo.claimNo"
                                                   style="width: 100%;height: auto">
                                        </div>

                                        <div class="display-none">

                                        <div class="form-group">
                                            <label class="font-noraml">报账金额范围</label>
                                            <div class="input-group">
                                                <input type="text" id="moneyFrom" name="moneyFrom"
                                                       class="input-sm form-control" placeholder="最低金额" c-model="myTodo.currencyMin"
                                                       style=";height: auto"
                                                       onkeyup="value=value.replace(/[^\d{1,}\.\d{1,}|\d{1,}]/g,'')"
                                                />
                                                <span class="input-group-addon"
                                                      style="padding: 4px 5px;background-color:#eeeeee">-</span>
                                                <input type="text" id="moneyTo" name="moneyTo"
                                                       class="input-sm form-control" placeholder="最高金额" c-model="myTodo.currencyMax"
                                                       style=";height: auto"
                                                       onkeyup="value=value.replace(/[^\d{1,}\.\d{1,}|\d{1,}]/g,'')"
                                                />
                                            </div>
                                        </div>


                                            <div class="form-group" id="data_5">
                                                <label class="font-noraml">起草时间</label>
                                                <div class="input-daterange input-group">
                                                    <input type="text" class="form-control Wdate" id="applyFromDate" c-model="myTodo.startDate"
                                                           name="applyFromDate" value="" style="height: auto"
                                                           onFocus="WdatePicker({maxDate:'#F{$dp.$D(\'applyEndDate\')}'});"/>
                                                    <span class="input-group-addon">-</span>
                                                    <input type="text" class="form-control Wdate" id="applyEndDate" c-model="myTodo.endDate"
                                                           name="applyEndDate" value="" style="height: auto"
                                                           onFocus="WdatePicker({minDate:'#F{$dp.$D(\'applyFromDate\')}'});"/>
                                                </div>
                                            </div>
                                        </div>

                                    </div>
                                    <div class="col-sm-3">
                                        <div class="form-group">
                                            <label class="font-noraml">报账单模板</label>
                                            <select name="itemId1" id="itemId1" class="form-control" c-model="myTodo.itemId1"
                                                    onchange="initItem2List()"></select>
                                        </div>



                                        <div class="display-none">


                                        <div class="form-group">
                                            <label class="font-noraml">付款金额范围</label>
                                            <div class="input-group">
                                                <input type="text" id="payAmountMin" name="payAmountMin" c-model="myTodo.payAmountMin"
                                                       class="input-sm form-control" placeholder="最低金额"
                                                       style="height: auto"
                                                       onkeyup="value=value.replace(/[^\d{1,}\.\d{1,}|\d{1,}]/g,'')"
                                                />
                                                <span class="input-group-addon"
                                                      style="padding: 4px 5px;background-color:#eeeeee">-</span>
                                                <input type="text" id="payAmountMax" name="payAmountMax" c-model="myTodo.payAmountMax"
                                                       class="input-sm form-control" placeholder="最高金额"
                                                       style="height: auto"
                                                       onkeyup="value=value.replace(/[^\d{1,}\.\d{1,}|\d{1,}]/g,'')"
                                                />
                                            </div>
                                        </div>


                                            <%--<div class="form-group">
                                                <label class="font-noraml">起草部门</label>
                                                <input type="text" id="applyUserDepname" name="applyUserDepname" c-model="myTodo.applyUserDepname"
                                                       placeholder="请填写起草部门" class="form-control"
                                                       style="width: 100%;height: auto">
                                            </div>--%>
                                        </div>
                                    </div>
                                    <div class="col-sm-3">
                                        <div class="form-group">
                                            <label class="font-noraml">业务大类</label>
                                            <select class="form-control" id="item2Id1" name="item2Id1" c-model="myTodo.item2Id1"
                                                    style="width: 100%;height: auto"></select>
                                        </div>


                                        <div class="display-none">
                                        <div class="form-group">
                                            <label class="font-noraml">合同名称</label>
                                            <input type="text" id="contractName" name="contractName"
                                                   placeholder="请填写合同名称" class="form-control" c-model="myTodo.contractName"
                                                   style="width: 100%;height: auto">
                                        </div>
                                        </div>

                                    </div>
                                    <div class="col-sm-3">
                                        <div class="form-group">
                                            <label class="font-noraml">供应商名称</label>
                                            <input type="hidden" id="vendorNo" name="vendorNo">
                                            <input id="vendorName" name="vendorName" class="form-control" c-model="myTodo.vendorName"
                                                   placeholder="请填写供应商名称" style="width: 100%;height: auto" readonly="readonly"/>
                                            <i class="glyphicon glyphicon-remove input-search ivendordel" onclick="onResetVendor();"  controlKey="unit"></i>
                                            <i class="glyphicon glyphicon-search input-search ivendorName" onclick="openVendor();"  controlKey="unit"></i>
                                        </div>


                                        <div class="display-none">

                                        <div class="form-group">
                                            <label class="font-noraml">合同编号</label>
                                            <input id="contractNo" name="contractNo" class="form-control pd-right20" c-model="myTodo.contractNo"
                                                   placeholder="请填写合同编号" style="width: 100%;height: auto"/>
                                        </div>


                                            <%--<div class="form-group">--%>
                                                <%--&lt;%&ndash;<label class="font-noraml">是否超出预算</label>--%>
                                                <%--<div style="letter-spacing: 20px">--%>
                                                    <%--<input type="radio" name="hasExceedBudget" c-model="myTodo.hasExceedBudget"--%>
                                                           <%--style="border-radius: 2px;width: 15px;height: 15px;color: #B4D8FF; margin-right: 10px"--%>
                                                           <%--value="1"/>是--%>
                                                    <%--<input type="radio" name="hasExceedBudget" c-model="myTodo.hasExceedBudget"--%>
                                                           <%--style="border-radius: 2px;width: 15px;height: 15px;color: #B4D8FF; margin-right: 10px"--%>
                                                           <%--value="0"/>否--%>
                                                <%--</div>&ndash;%&gt;--%>
                                            <%--</div>--%>
                                        </div>
                                    </div>
                                </div>
                                <div class="row">
                                    <div class="col-sm-12">
                                        <div class="text-center"
                                             style="border-bottom: 1px solid rgba(183,212,246,0.50); padding-bottom: 20px">
                                            <button id="showAndHide" type="button"
                                                    style="background-color: #48A0FF;height: 28px;width: 28px;border: 0px;  background: url(${pageContext.request.contextPath}/newstatic/img/icon/group_12.png) 50% 50%;  border-radius: 2px;color: #FFFFFF;font-size: 14px;">
                                                &nbsp;</button>
                                            <button style="background-color: #48A0FF;height: 28px;width: 100px;border: 0px;border-radius: 2px;color: #FFFFFF;font-size: 14px;"
                                                    type="button"
                                                    onclick="doSearch()">查询
                                            </button>
                                            <button type="button" onclick="onReset()"
                                                    style="background-color: #B7B9BB;height: 28px;width: 100px;border: 0px;border-radius: 2px;color: #FFFFFF;font-size: 14px;">
                                                重置
                                            </button>
                                            <button style="background-color: #48A0FF;height: 28px;width: 100px;border: 0px;border-radius: 2px;color: #FFFFFF;font-size: 14px;"
                                                    type="button"
                                                    onclick="doExport()">导出
                                            </button>
                                        </div>
                                    </div>
                                </div>

                            </div>
                            <div style="margin: 10px 0px 20px 0px; padding-top: 10px">
                                <span style="border: 1px solid #48A0FF;margin-left: -20px;"></span>
                                <span style="font-family: MicrosoftYaHei;font-size: 15px;color: #0B112A;margin-left: 13px;">查询结果</span>
                            </div>
                            <div class="checkListDiv q_mpsdc_body" style="margin-top: -20px;">
                                <table id="dtClaimTodo"
                                       class="table table-striped table-hover q_table table_td_left"
                                       data-classes="table-no-bordered"
                                       data-id-field="id" data-click-to-select="true" data-striped="true"
                                       data-ajax="claimTodoListSearchAjax"
                                       data-side-pagination="server" data-pagination="true" data-page-size="50"
                                       data-height="360" data-show-jumpto="true"
                                       data-row-style="render4RowStyle">
                                    <thead>
                                    <tr>
                                        <%--<th data-checkbox="true"></th>--%>
                                        <th data-width="120px" data-field="claimNo" data-formatter="render4TdLink">
                                            报账单编号
                                        </th>
                                        <th data-width="90px" data-field="itemIdName" data-formatter="render4TdLink">
                                            报账单模板
                                        </th>
                                        <th data-width="90px" data-field="item2IdName" data-formatter="render4Item2Id">
                                            业务大类
                                        </th>
                                        <th data-width="110px" data-field="applyAmount" data-formatter="processAmount" data-class="amountThCss">
                                            报账金额
                                        </th>
                                        <th data-width="80px" data-field="applyDate" data-formatter="render4TimeStampFormatter">
                                            报账日期
                                        </th>
                                        <%--<th data-width="80px" data-field="claimNo" data-formatter="render4open">
                                        </th>--%>
                                        <th data-width="100px" data-field="contractNo" data-formatter="render4TdLink">
                                            合同编号
                                        </th>
                                        <th data-width="120px" data-field="contractName" data-formatter="render4TdLink">
                                            合同名称
                                        </th>

                                        <th data-width="50px" data-field="hasExceedBudget" data-formatter="render4Budget">
                                            操作
                                        </th>
                                    </tr>
                                    </thead>
                                </table>
                            </div>
                        </div>
                    </div>
                </div>

            </div>


        </div>
        <br/><br/><br/>
        <%--<div class="navbar-fixed-bottom"--%>
        <%--style="height: 80px;width:100%;background: #FFFFFF;box-shadow: 0 -2px 10px 0 #C0DFFF;padding: 20px 60px;z-index: 10000;">--%>
        <%--<button type="button"--%>
        <%--style="background: #48A0FF;border-radius: 2px; border-width: 0px;width: 150px;height: 40px;font-family: MicrosoftYaHei;font-size: 24px;color: #FFFFFF;float: right;" onclick="batchApprove()">--%>
        <%--批量审批--%>
        <%--</button>--%>
        <%--</div>--%>
        <%-- </div>--%>
    </div>
</form>

<script>

    var lang_type = "";
    var curPortalUserGroupId = "";
    var user;
    var item2List;
    var item2List1;
    var commonUiDataBindService4QueryList = new CommonUiDataBindService4QueryList("myTodo");
    
    $(function () {
        $("#showAndHide").click(function () {
            $(".display-none").slideToggle(function () {
            });
            $(this).toggleClass('showAndHide-active');

        });

        $('#hh').click(function () {
            $('#tt').slideToggle();
        });

        //初始化查询条件中的报账单模板
        initClaimTemplate();
        //业务类型下拉框
        initBussinessList();

        $("#dtClaimTodo").bootstrapTable();//初始化表格
    });

    //初始化报账单模板下拉框
    function initClaimTemplate() {
        var url = contextPath + "/dictListAjax.do?&dictId=" + "claimTemplate";
        baseService.generateSelectElement({
            selectId: "itemId1"
        }, url, "code", "value");
    }
    //初始化业务类型下拉框
    function initBussinessList() {
        var url = contextPath + "/dictListAjax.do?&dictId=" + "businessType";
        baseService.generateSelectElement({
            selectId: "businessType"
        }, url, "code", "value");
    }
    //初始化业务大类下拉框
    function initItem2List() {
        var parentId = $("#itemId1").val();
        if (parentId == null || parentId == '') {
            $("#item2Id1").val("");
            $("#item2Id1").trigger("chosen:updated");
            return false;
        }
        var url = contextPath + "/org/businessunit/cateLevel2.do?" + "&orgId=0&parentId=" + parentId;
        baseService.generateSelectElement({
            selectId: "item2Id1"
        }, url, "code", "value");
    }
    //报账单号跳转
    function openClaim(url) {
        skipUrl(url);
    }
    function doNewUrl(url){
        var itemId = getParam(url,"itemId");
        var LowerItemId = itemId.toLowerCase();
        var newUrl = contextPath + "/newPages/efinance/claim/" + itemId + "/" + LowerItemId + "_draft.jsp?" + url+generateSearchUrl($("#queryForm").serializeObject(),"dtClaimTodo");
        newUrl = newUrl + "&claimNowStatus=qicao"; //追加本环节为起草
        window.location.href = newUrl;
    }
    function doOldUrl(url){
        var urlData = url;
        // urlData = urlData.replace(/&/g,"|");
        // url = url + "&url=" + urlData;
        // url = urlData;
        url = url + "&fromMenu=1"+generateSearchUrl($("#queryForm").serializeObject(),"dtClaimTodo");
        url = url + "&claimNowStatus=qicao"; //追加本环节为起草
        UiBlockService.blockUI4Load();
        // window.location.href = contextPath+doProcessUrl(url);
        window.location.href = doProcessUrl(url);
    }
    //重置
    function onReset() {
        //input类型的标签
        $("#queryForm input").val("");
        $("#queryForm input[name= 'IsAdvancedSerach']").val("1");

        $("#queryForm input[type=radio][name='hasExceedBudget']:checked").attr("checked", false);
        $("#queryForm input[type=radio][name='hasExceedBudget']:eq(0)").val("1");
        $("#queryForm input[type=radio][name='hasExceedBudget']:eq(1)").val("0");
        //select类型的标签
        $("#queryForm select").val("");
        $("#queryForm select").trigger("chosen:updated");
    }


    function  doSearch() {

        var payAmountMin = parseFloat($("#payAmountMin").val());
        var payAmountMax = parseFloat($("#payAmountMax").val());

        if(payAmountMin>payAmountMax){
            alert("起始金额应小于目标金额！！ ");
            return null;
        }

        $('#dtClaimTodo').bootstrapTable('refresh');
    }

    function claimTodoListSearchAjax(params) {//数据查询ajax



        var paramUrl = contextPath + "/commonbizajax2report.do?requestId=AllClaimSavedList";
        var inputMapModel="item2List,item2List1";
        var data={inputMapModel : inputMapModel};

        data = generateSearchConditionAndUpd2Ui(commonUiDataBindService4QueryList,this,"queryForm",data,function (urlParam) {//设置联动方法，如果没有联动无需写
            if(typeof urlParam.itemId1 !='undefined'&& urlParam.itemId1 !=''){//设置报账单模板和报账单大类的联动
                updateUiById("itemId1",urlParam.itemId1);
                $('#itemId1').trigger("change");
            }
        });

        loadBootstrapTableData(this, paramUrl, params, $('#queryForm'),data,setParam);//ajax加载表格
    }

    function setParam(data) {
        curPortalUserGroupId = data.user.curGroupId;
        user = data.user;
        var inputMap = data.inputMap;
        item2List = inputMap.item2List;
        item2List1 = inputMap.item2List1;
    }

    function render4Item2Id(value, row, index, field) {
        var itemId = "T" + row.item2Id.substr(0, 3);
        var item2Name = getCatelevel2Value(value, itemId);
        var newUrl = getNewUrl(value, row);
        var oldUrl = getOldUrl(value, row);
        var createTime = row.createTime;

        var returnValue = "";

        //return returnValue;
        if(needDraftGreen(row)){
            returnValue = createTdLinkCommonGreen("openClaim( '" + oldUrl + "')", item2Name);
        }else{
            returnValue = createTdLinkCommon("openClaim( '" + oldUrl + "')", item2Name);
        }
        return returnValue;
    }
    
/**
huachao	每个报账单取值报账金额不一样， 在此处统一处理 
*/
    function processAmount(value, row, index, field) {
        var itemId = row.itemId;
        var validateStatus = row.validateStatus//报账单整体校验可以提交
        if(itemId == "T030"){
            value = row.payAmount;
        }
		
		//处理金额格式
		value =formatMoney(value,2,"");

        var newUrl = getNewUrl(value, row);
        var oldUrl = getOldUrl(value, row);
		
        var returnValue = "";

        //return returnValue;
        if(needDraftGreen(row)){
            returnValue = createTdLinkCommonGreen("openClaim( '" + oldUrl + "')", value);
        }else{
            returnValue = createTdLinkCommon("openClaim( '" + oldUrl + "')", value);
        }
        return returnValue;
    }    

    //获取新的跳转url
    function getNewUrl(value, row) {
        row.url = "<%=request.getContextPath()%>/service.do?itemId="+row.itemId+"&requestId="+row.itemId+"LoadAll"+"&claimId="+row.claimId+"&wfActDefId=updateDraftState&srequestId=AllClaimSavedList";
        var url = row.url + '&nextIsSingle=' + row.isSingle + '&businessSubType=' + row.businessSubType;
        url = url.substr(url.indexOf("item2Id"));
        return url;
    }
    //获取旧的跳转url
    function getOldUrl(value, row) {
        row.url = "<%=request.getContextPath()%>/service.do?requestId="+row.itemId+"LoadAll"+"&claimId="+row.claimId+"&wfActDefId=updateDraftState&srequestId=AllClaimSavedList&itemId="+row.itemId;
        return doProcessUrl(row.url);
    }

    function render4TdLink(value, row, index, field) {
        var oldUrl = getOldUrl(value, row);

        var returnValue = "";

        //return returnValue;
        if(needDraftGreen(row)){
            returnValue = createTdLinkCommonGreen("openClaim( '" + oldUrl + "')", value);
        }else{
            returnValue = createTdLinkCommon("openClaim( '" + oldUrl + "')", value);
        }
        return returnValue;
    }


    function NewDate(str){
        if(!str){
            return 0;
        }
        arr=str.split(" ");
        d=arr[0].split("-");
        t=arr[1].split(":");
        var date = new Date();
        date.setUTCFullYear(d[0], d[1] - 1, d[2]);
        date.setUTCHours(t[0], t[1], t[2], 0);
        return date;
    }


    function render4TimeStampFormatter(value, row, index) {//日期格式化



        var thisDateLong =NewDate(value);


        var date = new Date(thisDateLong);
        var y = date.getFullYear();
        var m = date.getMonth() + 1;
        m = m < 10 ? ('0' + m) : m;
        var d = date.getDate();
        d = d < 10 ? ('0' + d) : d;
        var h = date.getHours();
        h = h < 10 ? ('0' + h) : h;
        var minute = date.getMinutes();
        var second = date.getSeconds();
        minute = minute < 10 ? ('0' + minute) : minute;
        second = second < 10 ? ('0' + second) : second;

        value  =  y + '-' + m + '-' + d ;


        var oldUrl = getOldUrl(value, row);

        var returnValue = "";

        //return returnValue;
        if(needDraftGreen(row)){
            returnValue = createTdLinkCommonGreen("openClaim( '" + oldUrl + "')", value);
        }else{
            returnValue = createTdLinkCommon("openClaim( '" + oldUrl + "')", value);
        }
        return returnValue;

    }


    //检查数据校验是否通过
    function needDraftGreen(row){
        var itemId = row.itemId;
        var validateStatus = row.validateStatus//报账单整体校验可以提交
        if(itemId == "T039" && validateStatus == "1"){
            return true;
        }else{
            return false;
        }
    }

    function render4IsIma(value, row, index, field) {
        var result = "";
        if(value == "1"){
            result ="是";
        }else{
            result ="否";
        }
        return result;
    }
    function render4Budget(value, row, index, field) {
        // if(value == "是"){
        //     //var imgUrl =contextPath+"/newstatic/img/icon/shape.png";
        //     return '<img class="warning" src= "'+contextPath+'/newstatic/img/icon/shape.png"/><div><font size="1" color="red">预算超支</font></div>';
        // }else{
        //     return "删除";
        // }
        //材料调拨销售方不可以自己删除 供应商协同报账单不可以自己删除
        if("T042" != row.itemId&&"T039" != row.itemId
        		&&"T053" != row.itemId
        		&&"T054" != row.itemId
        		&&"T055" != row.itemId
        		&&"T056" != row.itemId){
        	return '<i class="glyphicon glyphicon-trash" title="删除" onclick=deleteLine('+ row.claimId + ',"' + row.itemId + '","' + row.replaceDraftUserYn + '",queryForm)></i>';        
        }
        if("T039" == row.itemId && row.replaceDraftUserYn == "Y"){
        	return '<i class="glyphicon glyphicon-trash" title="删除" onclick=deleteLine('+ row.claimId + ',"' + row.itemId + '","' + row.replaceDraftUserYn + '",queryForm)></i>';        
        }

    }


    function deleteLine(claimId,itemId,replaceDraftUserYn,formid){



        MessageBox.confirm({
            message: "确定要删除该报账单吗?",
            action4Yes: function action4Yes() {
                var confirm = true;
                if("T039"==itemId && replaceDraftUserYn == "N"){
                    //confirm = window.confirm("请再次确认是否删除报账单，若确认删除报账单请通知供应商作废或红冲发票并将发票邮寄回供应商处?");
                    confirm = false;
                    MessageBox.error("此报账单不能删除");
                    return;
                }
                if(!confirm)
                    return;
                form = document.getElementById(formid);
                var compId="${gbsmo.user.curCompId}";
                window.location.href =doProcessUrl("<%=request.getContextPath()%>/service.do?requestId="+itemId+"ClaimDel&deleteIds="+claimId+"&compId="+compId+"&claimId="+claimId+"&processStateEng=rootDrafterActivity&deleteFrom=form");
            },
            action4No: function action4No() {
            }
        });

        // 旧的删除以及提示
        <%--if(window.confirm("确定要删除该报账单吗？")){--%>
            <%--var confirm = true;--%>
            <%--if("T039"==itemId)--%>
                <%--confirm = window.confirm("请再次确认是否删除报账单，若确认删除报账单请通知供应商作废或红冲发票并将发票邮寄回供应商处?");--%>
            <%--if(!confirm)--%>
                <%--return;--%>
            <%--form = document.getElementById(formid);--%>
            <%--var compId="${gbsmo.user.curCompId}";--%>
            <%--window.location.href =doProcessUrl("<%=request.getContextPath()%>/service.do?requestId="+itemId+"ClaimDel&deleteIds="+claimId+"&compId="+compId+"&claimId="+claimId+"&processStateEng=rootDrafterActivity");--%>
        <%--}--%>
    }



    function render4open(value, row, index, field){
        var activityDefID = row.activityDefID;
        var batchApproveOpt = row.batchApproveOpt;
        var itemId = "T" + row.item2Id.substr(0, 3);
        var newUrl = getNewUrl(value, row);
        var oldUrl = getOldUrl(value, row);
        var createTime = row.createTime;
        var result = '<a href="javascript:void(0)" onclick="openClaim( \''+ oldUrl +'\')"><span class="image"><img src="'+contextPath+'/newstatic/img/icon/q/fdjss.png"/></span></a>';
        if(activityDefID != "drafterActivity"){
            if(activityDefID.indexOf("drafterOperateActivity") == -1){
                if(batchApproveOpt != "PackBA"){
                    var claimId = row.claimId;
                    var processInstID = row.processInstID;
                    var activityInstID = row.activityInstID;
                    var taskInstID = row.taskInstID;
                    var activityDefID = row.activityDefID;
                    var item2Id = row.item2Id;
                    var isSingle = row.isSingle;
                    var id = row.id;
                    var businessObjectID = row.businessObjectID;
                    var businessType = row.businessType;
                    var businessSubType = row.businessSubType;

                    var activityInstID = row.activityInstID;
                    var processDefName = row.processDefName;
                    result += '<a href="javascript:void(0)" onclick="wfsubmit(\''+claimId+'\',\''+processInstID+'\',\''+activityInstID+'\',\''+taskInstID+'\',\''+activityDefID+'\',\''+item2Id+'\',\''+isSingle+'\',\''+id+'\',\''+businessObjectID+'\',\''+businessType+'\',\''+businessSubType+'\')"><span class="image"><img src="'+contextPath+'/newstatic/img/icon/q/ok.png"/></span></a>';
                    result += '<a href="javascript:void(0)" onclick="tuihui(\''+claimId+'\',\''+item2Id+'\',\''+processInstID+'\',\''+activityInstID+'\',\''+taskInstID+'\',\''+processDefName+'\',\''+activityDefID+'\',\''+isSingle+'\',\''+id+'\',\''+businessObjectID+'\',\''+businessType+'\',\''+businessSubType+'\')"><span class="image"><img src="'+contextPath+'/newstatic/img/icon/q/fdjss2.png"/></span></a>';
                }
            }
        }
        return result;
    }

    //打开供应商弹框
    function openVendor() {
        var userJson = JSON.parse(user);
        var compId = userJson.curCompId;
        var orgId = userJson.orgId;
        var pageUrl = "/newPages/efinance/myReimbursement/searchVendorList.jsp";
        dialogService.openDialog(function (dataReturn) {//回调方法
            if (typeof(dataReturn) != "undefined") {
                updateVendor(dataReturn);
            }
        }, {//参数
            compId : compId,
            orgId : orgId,
            hasSite : 'yes'
        }, pageUrl, "选择供应商");
    }

    function updateVendor(dataReturn) {
        if (typeof(dataReturn) != "undefined") {
            updateUiById("vendorNo", dataReturn.vendorNumber);
            updateUiById("vendorName", dataReturn.vendorName);
        }
    }

    function onResetVendor(){
        updateUiById("vendorNo", "");
        updateUiById("vendorName", "");
    }

    function wfsubmit(claimId, processInstID, activityInstID, taskInstID, activityDefID, item2Id, isSingle, pendingID, businessObjectID, businessType, businessSubType) {

        var jsonUser = $.parseJSON(user);
        var url = "";
        if (businessType == "claim") {
            url = contextPath +"/service.do?requestId=taskInstQueryNextActivityDefFacadeService&claimId=" + claimId + "&userid=" + jsonUser.userid + "&username=" + jsonUser.username + "&activityInstID=" + activityInstID + "&processInstID=" + processInstID + "&taskInstID=" + taskInstID + "&activityDefID=" + activityDefID + "&item2Id=" + item2Id + "&pendingID=" + pendingID + "&cType=auto" + "&taskInstID=" + taskInstID;

        } else if (businessType == "prvMessage") {
            if (businessSubType == "head") {
                url = "审批", contextPath+"/service.do?requestId=oTHTaskInstQueryNextActivityDefFacadeService&businessObjectID=" + businessObjectID + "&userid=" + jsonUser.userid + "&username=" + jsonUser.username + "&activityInstID=" + activityInstID + "&processInstID=" + processInstID + "&taskInstID=" + taskInstID + "&activityDefID=" + activityDefID + "&item2Id=" + item2Id + "&pendingID=" + pendingID + "&cType=auto&businessType=" + businessType + "&businessSubType=" + businessSubType;
            } else if (businessSubType == "line") {
                url = contextPath+"/service.do?requestId=oTHTaskInstQueryNextActivityDefFacadeService&businessObjectID=" + businessObjectID + "&userid=" + jsonUser.userid + "&username=" + jsonUser.username + "&activityInstID=" + activityInstID + "&processInstID=" + processInstID + "&taskInstID=" + taskInstID + "&activityDefID=" + activityDefID + "&item2Id=505001&pendingID=" + pendingID + "&cType=auto&businessType=" + businessType + "&businessSubType=" + businessSubType;
            }
        }
        //增加列表查询条件
        url = combination(url, "claimNo");
        url = combination(url, "itemId");
        url = combination(url, "targetSelect");
        url = combination(url, "vendorName");
        url = combination(url, "contractName");
        url = combination(url, "contractNo");
        url = combination(url, "applyUsername");
        url = combination(url, "moneyFrom");
        url = combination(url, "moneyTo");
        url = combination(url, "processState");
        url = combination(url, "startDate");
        url = combination(url, "endDate");
        url = combination(url, "orgId");
        url = combination(url, "segment1");
        url = combination(url, "itemId1");
        url = combination(url, "item2Id1");
        url = combination(url, "targetSelect");
        url = combination(url, "currencyMin");
        url = combination(url, "currencyMax");
        url = combination(url, "vendorNo");
        url = combination(url, "payAmountMin");
        url = combination(url, "applyUserDepname");
        url = combination(url, "payAmountMax");
        url = combination(url, "businessType");
        url = combination(url, "IsAdvancedSerach");
        url = url + "&queryItem2Id1=" + document.getElementById("item2Id1").value;


        if (businessType == "claim") {

            XMLHttp.sendReq("post", contextPath+"/ajax.do?serviceBean=taskInstQueryNextActivityDefFacadeService&methodName=queryNextActivity&activityInstID=" + activityInstID + "&processInstID=" + processInstID + "&taskInstID=" + taskInstID + "&claimId=" + claimId + "&activityDefID=" + activityDefID, "", function (http_request) {
                var rspText = http_request.responseText;
                var isTrue = false;
                if (rspText != "") {
                    var results = http_request.responseXML;
                    var rsflag = results.getElementsByTagName("rsflag")[0].childNodes;
                    var flag = getNodeValue(rsflag[0]);

                    var rsnextid = results.getElementsByTagName("rsnextid")[0].childNodes;
                    var nextid = getNodeValue(rsnextid[0]);

                    var rsnextname = results.getElementsByTagName("rsnextname")[0].childNodes;
                    var nextname = getNodeValue(rsnextname[0]);

                    var rsviewuser = results.getElementsByTagName("rsviewuser")[0].childNodes;
                    var viewuser = getNodeValue(rsviewuser[0]);

                    var rsnextuser = results.getElementsByTagName("rsnextuser")[0].childNodes;
                    var nextuser = getNodeValue(rsnextuser[0]);

                    var currid = activityDefID;

                    if (flag == "single") {
                        //显示确认信息页面
                        isTrue = true;
                        confirmSubmitUrl = url;
                        var url_confirm = contextPath+"/pages/workflow/confirmSubmit.jsp?claimId=" + claimId + "&item2Id=" + item2Id + "&userid=" + jsonUser.userid + "&username=" + jsonUser.username + "&activityInstID=" + activityInstID + "&processInstID=" + processInstID + "&taskInstID=" + taskInstID + "&activityDefID=" + activityDefID + "&pendingID=" + pendingID + "&nextname=" + encodeURI(nextname) + "&nextuser=" + encodeURI(nextuser) + "&viewuser=" + encodeURI(viewuser);
                        showSimpleDivBox41(500, 390, "审批", url_confirm, 5, "doConfirmWfSubmit", 0, 0);

                    } else if (flag == "singleNotConfirm") {
                        //增加提交时差不能小于30秒限制
                        if (!checkAndSetCookie(claimId, jsonUser.userid, jsonUser.curGroupId, activityDefID, pendingID)) {
                            MessageBox.notice("此操作已经提交，请稍后查看。");
                            return;
                        }
                        isTrue = false;
                        wfRealsubmit(claimId, processInstID, activityInstID, taskInstID, activityDefID, item2Id, isSingle, pendingID, businessObjectID, businessType, businessSubType);

                    } else if (flag == "notsingle") {
                        isTrue = false;
                        wfRealsubmit(claimId, processInstID, activityInstID, taskInstID, activityDefID, item2Id, isSingle, pendingID, businessObjectID, businessType, businessSubType);

                    } else if (flag == "error") {
                        isTrue = false;
                        MessageBox.error(nextuser);

                    } else {
                        isTrue = false;
                        MessageBox.error("\u51FA\u73B0\u5F02\u5E38\uFF0C\u67E5\u8BE2\u4E0B\u4E00\u73AF\u8282\u5931\u8D25\uFF0C\u672A\u5F97\u5230\u73AF\u8282\u540D\uFF0C\u8BF7\u7A0D\u540E\u91CD\u8BD5\uFF01");

                    }
                } else {
                    isTrue = false;
                    //查询后续环节错误
                    MessageBox.error("\u51FA\u73B0\u5F02\u5E38\uFF0C\u67E5\u8BE2\u4E0B\u4E00\u73AF\u8282\u5931\u8D25\uFF0C\u672A\u5F97\u5230\u73AF\u8282\u540D\uFF0C\u8BF7\u7A0D\u540E\u91CD\u8BD5\uFF01");

                }

            });

        } else {

            showSimpleDivBox3(550, 390, "审批", url, 5, "");
        }

    }

    function wfRealsubmit(claimId, processInstID, activityInstID, taskInstID, activityDefID, item2Id, isSingle, pendingID, businessObjectID, businessType, businessSubType) {
        var url = "";
        var jsonUser = $.parseJSON(user);
        if (businessType == "claim") {
            url = contextPath+"/service.do?requestId=taskInstQueryNextActivityDefFacadeService&claimId=" + claimId + "&userid=" + jsonUser.userid + "&username=" + jsonUser.username + "&activityInstID=" + activityInstID + "&processInstID=" + processInstID + "&taskInstID=" + taskInstID + "&activityDefID=" + activityDefID + "&item2Id=" + item2Id + "&pendingID=" + pendingID + "&cType=auto";
        } else if (businessType == "prvMessage") {
            if (businessSubType == "head") {
                url = "审批", contextPath+"/service.do?requestId=oTHTaskInstQueryNextActivityDefFacadeService&businessObjectID=" + businessObjectID + "&userid=" + jsonUser.userid + "&username=" + jsonUser.username + "&activityInstID=" + activityInstID + "&processInstID=" + processInstID + "&taskInstID=" + taskInstID + "&activityDefID=" + activityDefID + "&item2Id=" + item2Id + "&pendingID=" + pendingID + "&cType=auto&businessType=" + businessType + "&businessSubType=" + businessSubType;
            } else if (businessSubType == "line") {
                url = contextPath+"/service.do?requestId=oTHTaskInstQueryNextActivityDefFacadeService&businessObjectID=" + businessObjectID + "&userid=" + jsonUser.userid + "&username=" + jsonUser.username + "&activityInstID=" + activityInstID + "&processInstID=" + processInstID + "&taskInstID=" + taskInstID + "&activityDefID=" + activityDefID + "&item2Id=505001&pendingID=" + pendingID + "&cType=auto&businessType=" + businessType + "&businessSubType=" + businessSubType;
            }
        }
        //增加列表查询条件
        url = combination(url, "claimNo");
        url = combination(url, "itemId");
        url = combination(url, "targetSelect");
        url = combination(url, "vendorName");
        url = combination(url, "contractName");
        url = combination(url, "contractNo");
        url = combination(url, "applyUsername");
        url = combination(url, "moneyFrom");
        url = combination(url, "moneyTo");
        url = combination(url, "processState");
        url = combination(url, "startDate");
        url = combination(url, "endDate");
        url = combination(url, "orgId");
        url = combination(url, "segment1");
        url = combination(url, "itemId1");
        url = combination(url, "item2Id1");
        url = combination(url, "targetSelect");
        url = combination(url, "currencyMin");
        url = combination(url, "currencyMax");
        url = combination(url, "vendorNo");
        url = combination(url, "payAmountMin");
        url = combination(url, "applyUserDepname");
        url = combination(url, "payAmountMax");
        url = combination(url, "businessType");
        url = combination(url, "IsAdvancedSerach");
        url = url + "&queryItem2Id1=" + document.getElementById("item2Id1").value;
        showSimpleDivBox3(550, 390, "审批", url, 5, "");
    }

    function tuihui(claimId, item2Id, processInstID, activityInstID, taskInstID, processDefName, activityDefID, isSingle, pendingID, businessObjectID, businessType, businessSubType) {
        var url = "";
        if (businessType == "claim") {
            url = contextPath+"/service.do?requestId=processBackQueryFacadeService&taskInstID=" + taskInstID + "&activityInstID=" + activityInstID + "&processInstID=" + processInstID + "&processDefName=" + processDefName + "&activityDefID=" + activityDefID + "&pendingID=" + pendingID + "&claimId=" + claimId + "&cType=auto&item2Id=" + item2Id;
        } else if (businessType == "prvMessage") {
            if (businessSubType == "head") {
                url = contextPath+"/service.do?requestId=oTHProcessBackQueryFacadeService&taskInstID=" + taskInstID + "&activityInstID=" + activityInstID + "&processInstID=" + processInstID + "&processDefName=" + processDefName + "&activityDefID=" + activityDefID + "&pendingID=" + pendingID + "&businessObjectID=" + businessObjectID + "&cType=auto&businessType=" + businessType + "&businessSubType=" + businessSubType + "&item2Id=" + item2Id;
            } else if (businessSubType == "line") {
                url = contextPath+"/service.do?requestId=oTHProcessBackQueryFacadeService&taskInstID=" + taskInstID + "&activityInstID=" + activityInstID + "&processInstID=" + processInstID + "&processDefName=" + processDefName + "&activityDefID=" + activityDefID + "&pendingID=" + pendingID + "&businessObjectID=" + businessObjectID + "&cType=auto&businessType=" + businessType + "&businessSubType=" + businessSubType + "&item2Id=" + item2Id;
            }
        }
        //增加列表查询条件
        url = combination(url, "claimNo");
        url = combination(url, "itemId");
        url = combination(url, "targetSelect");
        url = combination(url, "vendorName");
        url = combination(url, "contractName");
        url = combination(url, "contractNo");
        url = combination(url, "applyUsername");
        url = combination(url, "moneyFrom");
        url = combination(url, "moneyTo");
        url = combination(url, "processState");
        url = combination(url, "startDate");
        url = combination(url, "endDate");
        url = combination(url, "orgId");
        url = combination(url, "segment1");
        url = combination(url, "itemId1");
        url = combination(url, "item2Id1");
        url = combination(url, "targetSelect");
        url = combination(url, "currencyMin");
        url = combination(url, "currencyMax");
        url = combination(url, "vendorNo");
        url = combination(url, "payAmountMin");
        url = combination(url, "payAmountMax");
        url = combination(url, "businessType");
        url = combination(url, "IsAdvancedSerach");
        url = url + "&queryItem2Id1=" + document.getElementById("item2Id1").value;
        showSimpleDivBox3(500, 390, "退回", url, 5, "getVendorData");
    }
    //批量审批
    function batchApprove(){
        var claimList = $.map($('#dtClaimTodo').bootstrapTable('getSelections'), function (row) {
            return row;
        });
        var claimIds="";
        var count=0;
        var selectCount=0;
        for(i=0;i<claimList.length;i++){
            selectCount=selectCount+1;
            var claim =claimList[i];
            var item2Id=claim.item2Id;
            var claimId=claim.claimId;
            var processStateEng=claim.activityDefID;

            if(item2List.indexOf(item2Id)>=0 &&processStateEng=="departmentManagerActivity"){
                count=count+1;
                if(claimIds==""){
                    claimIds= claimId;
                }else{
                    claimIds=claimIds+","+claimId;
                }
            }
            if(item2List1.indexOf(item2Id)>=0&&processStateEng=="directorActivity"){
                count=count+1;
                if(claimIds==""){
                    claimIds= claimId;
                }else{
                    claimIds=claimIds+","+claimId;
                }
            }

        }
        if(count==0){
            MessageBox.notice("只有业务大类编码为"+item2List+"并且处于‘部门业务负责人审批’,‘分管领导’状态的报账单才能进行批量审批！");
            return false;
        }
        $("#batch").prop("disabled", "disabled");
        UiBlockService.blockUI({message:"<h4>请求处理中.请稍候...</h4>"});
        var url=contextPath+"/service.do?requestId=batchApproval&claimIds="+claimIds+"&selectCount="+selectCount+"&count="+count;
        window.location.href =doProcessUrl(url);
        $("#batch").removeProp("disabled");

    }

    function doExport() {
        var form = document.getElementById("queryForm");
        form.action = contextPath + "/claimVoucherReportExportAction.do?requestId=doExportMyDraft";
        form.submit();
    }
    
   function render4RowStyle(row,index) {
		if(typeof row.costBenefitDesc!="undefined"&&row.itemId=='T020'&&row.costBenefitDesc!=''){
		    return {classes:'trRed'} ;
		}
		return "";
    }
</script>
</body>
</html>
