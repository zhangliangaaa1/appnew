//+++++++++++++++++++++++++++++

$(function ($) {
    //-- 字段
    $.page.template = $("#template").children()
    //-- 类型模板
    $.page.templateChosen = $("#templateChosen").children()

    $.page.loadView();
});


(function ($) {

    $.page = $.page || {};//-- 页面配置

    var action = "invoice_folder_image";

    $.page.add = action + "_update"; //-- 保存的 ACTION
    $.page.veriOcrReq = action + "_verification"; //-- 验真地址
    $.page.view = action; //-- 图片详情信息
    $.page.model = action + "_mode"; //-- 切换类型

    //-- 加载详情
    $.page.loadView = function (  ) {

        var params = {invoiceId: $("#id").val() }
        params.requestId = $.page.view
        Ajax.post().url( $.ajaxUrl ).data(params).success(success).do();

        //-- 成功返回事件
        function success(res) {
            res = res || {};
            $.page.setFields(res.data)
            $.page.fields = res.data.data //-- 缓存当前信息
            $.page.initData = $("#editForm").serializeObject()//-- 设置完成后 初始化 当前数据
            // $.page.onChange();
            $.page.limit(res.data)//-- 权限控制
            $.page.onFocus();
        }
    }

    //-- 权限控制
    $.page.limit = function ( res ){
        res = res||{}

        btControl("saveBtn", "Y" === res.saveBtnShow);
        btControl("verificationBt", "Y" === res.verfiBtnShow);

        // -- 按钮控制
        function btControl( id, usable ){
            var disabled = !usable;
            var background = usable &&"#2591FF" ||"#eee"
            $("#" + id ).attr( "disabled", disabled ).css( "background",background  )
        }

    }

    $.page.onChange = function (){
        //-- 事件绑定 : 值改变 变色
        $("#fields input").blur( function (){
            $.page.changeFields( $(this) )
        } )

        $("#fields select").change( function (){
            $.page.changeFields( $(this) )
        } )

    }
    
  //-- 事件绑定 : 获取焦点时input全选
    $.page.onFocus = function (){
    	$("#imageDetlDiv input[type='text']").focus(function(){
    		this.select();
      	});
    }
    
    
    
    

    //-- 字段变化  修改颜色
    $.page.changeFields = function (obj){
        var id = obj.attr("id");
        var isChange = $.page.initData[id] !== obj.val()
        obj.css( "color" ,isChange&&"red"||"#555")
    }

    $.page.setFields = function ( fields ){

        var head  = fields;

        

        // -- 字段存放 父节点
        var container = $("#fields")
        container.html("");//-- 清空

        if (!fields.data || 0 === fields.data.length) {
            return container.html("无数据");
        }

        $.each(fields.data, function (index, row) {

            //-- 发票名称 如果没有 取 文件夹名称
            if( row.keyTitle === "folderName" &&  !row.value){
                row.value = head.folderName;

                row.value = row.value||"影像夹"
            }

            container.append(setField(index, row,head))
        });

        setImg();//-- 设置图片
        $.page.selectEve();//-- 下拉框 组件初始化


        //-- 类型修改了
        if( $.page.isChangeType ){
            $("#invoiceTypeModify").parent().find( ".chosen-single span" ).css("color", "red")
        }

        function setImg(){
            var src = head.image
            $("#div_image img").attr( "src", src ).attr("data-original",src)
            // new Viewer(document.getElementById('div_image'), {
            //     toolbar: true,//显示工具条
            //     viewed() {
            //         viewer.zoomTo(0.75);// 图片显示比例 75%
            //     },
            //     show: function () {// 动态加载图片后，更新实例
            //         viewer.update();
            //     }
            // });
        }

        function setField(index, data,head) {

            var alternately = 0 === index % 2;// -- 单行 加背景

            var row = $.page.template.clone(true);
            alternately && row.addClass("row-alternately")

            //影像要素
            var key = row.find(".left");
            "true" === data.required && key.append("<span class=\"required2\">*</span>"); // -- 必填提示 *
            key.append(data.key)//--标题

            //影像中的信息
            var val = row.find(".val");

            // if( "ocrVerificationStatusName" === data.keyTitle  ){
            //     if(!verificationBtLimit( data.value ) ){
            //         $("#verificationBt").hide()
            //     }else{
            //         $("#verificationBt").show()
            //     }
            // }

            // -- 不可编辑模式
            if ( "false" ===  data.editable) {
                setDefault()
                return row;
            }
            
            if ("Y" != head.saveBtnShow) {
                setDefault()
                return row;
            }

            // 编辑模式 先设置样式
            val.addClass("right").css("marginBottom", "3px");

            "input_date" === data.inputType && setDate( "yyyy-MM-dd", true );
            "input_time" === data.inputType && setDate( "HH:mm" );
            "input" === data.inputType && setInput( );
            "chosen" === data.inputType && setChosen( );

            "true"  == data.difference && val.find("input").css("color", "red") //-- 修改过的字段变红

            if( "invoiceTypeModify" === data.keyTitle  ){
                $.page.isChangeType = "true" == data.difference;
            }

            return row;

            //-- 验真 按钮权限
            function verificationBtLimit( ocrVerificationStatusName  ){

                if( ocrVerificationStatusName === "无需验真" ){
                    return false
                }

                if( ocrVerificationStatusName === "系统确认真票" ){
                    return false
                }

                if( ocrVerificationStatusName === "系统确认作废" ){
                    return false
                }

                if( ocrVerificationStatusName === "红冲发票" ){
                    return false
                }

                return ocrVerificationStatusName !== "失控发票";
            }

            function setChosen(){
                var chosen = $.page.templateChosen.clone(true);
                "true" ===  data.required && chosen.addClass("required")
                chosen.attr( "id", data.keyTitle )
                chosen.attr( "name", data.keyTitle )
                chosen.val(  data.value )
                val.append(chosen)
            }

            function setInput(){
                var input = $( "<input class=\"form-control\"  type=\"text\">" );
                "true" === data.required && input.addClass("required")
                input.attr( "id", data.keyTitle )
                input.attr( "name", data.keyTitle )
                input.val(  data.value )
                val.append(input)
            }

            function setDate( dateFmt , isDate){

                var input = $( "<input class=\"form-control text Wdate\" readonly type=\"text\">" );
                input.focus( function (){
                    WdatePicker({dateFmt:dateFmt,alwaysUseStartDate:true});
                })

                "true" === data.required && input.addClass("required")

                input.attr( "id", data.keyTitle )
                input.attr( "name", data.keyTitle )

                // -- 值
                var value = data.value;
                if( isDate &&value.indexOf('-')==-1){// --日期格式化时间  加是否存在“-”的判断是因为，页面第一次保存的时候，格式为“yyyyMMdd”,保存数据后的格式变为“yyyy-MM-dd”
                    value = value.substr( 0, 4) + "-" + value.substr( 4, 2) + "-" + value.substr(6,2)
                }
                input.val( value )

                val.append(input)
            }


            //-- 默认显示值
            function setDefault() {
                if(data.key == "影像类型"){
                    var text= $("#templateChosen select").find("option[value="+ data.value +"]").text();
                    var label = $("<label class=\"font-noraml\" />");
                    label.html(text)
                    val.append(label)
                }else{
                    var label = $("<label class=\"font-noraml\" />");
                    label.html(data.value)
                    val.append(label)
                }
            }

        }
    }



    //-- 发票验真
    $.page.verification = function () {
        var params = {id: $("#id").val()}
        params.requestId = $.page.veriOcrReq
        Ajax.post().url($.ajaxUrl).data(params).success(success).do();

        //-- 成功返回事件
        function success(res,success) {
            res = res || {};
            // success && $("#verificationBt").attr( "disabled", "disabled" )
            MessageBox.notice(res.message, "", function () {
                $.page.loadView();//成功 刷新数据
            });
        }
    }


    //-- 数据保存
    $.page.save = function () {

        if ($.page.validate && false === $.page.validate()) {
            return false;
        }

        if (!validateForm("editForm")) {
            return false;
        }

        var form = $("#editForm").serializeObject();

        var data  =  $.page.fields.slice()

        //-- 复制表单的值
        $.each( data ,function ( idnex ,row ){
            if ( "false" ===  row.editable) {
                return;
            }
            //-- 可编辑的选项重新赋值
            row.value = $("#" +  row.keyTitle).val()
            if( "input_date" === data.inputType ){
                row.value =  row.value.replace( "-","" ).replace( "-","" ).replace( "-","" )
            }
        } )

        var params = {}
        params.data = JSON.stringify(data)
        params.requestId = $.page.add
        params.invoiceId = $("#id").val()

        Ajax.post().url( $.ajaxUrl).data(params ).success(success).do();

        //-- 成功返回事件
        function success(res, success) {
            res = res || {};

            MessageBox.notice(res.message, "", function () {
                debugger
                success && $.page.loadView();//功 刷新修改 历史
            });

        }
    };


    //-- 变更发票类型
    $.page.changeType = function () {
        var params = {invoiceId: $("#id").val() ,invoiceType : $("#invoiceTypeModify").val()}
        params.requestId = $.page.model
        Ajax.post().url( $.ajaxUrl ).data(params).success(success).do();
        function success(res) {
            res = res || {};
            $.page.setFields(res.data)
            $.page.fields = res.data.data //-- 缓存当前信息
            // $.page.onChange();//-- 绑定事件
            // $("#invoiceTypeModify").change();
            // $.page.changeFields( $("#invoiceTypeModify") )
            $.page.limit(res.data)
            // $.page.initData = $("#editForm").serializeObject();//-- 设置完成后 初始化 当前数据
        }
    }

})(jQuery);