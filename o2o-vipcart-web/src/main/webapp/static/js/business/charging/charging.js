/**
 * 活动维护
 * create by liuxian
 */
(function ($charging) {

    $charging.initList = function (pageId) {
        system.visitToAjax(pageId);
    };

    $charging.chargingQueryFormSubmit = function () {
        var formData = $('#chargingQueryForm').serialize();
        system.ajaxPostAsync("/charging/list", formData, function (result) {
            $("#chargingListDiv").html(result);
        });
    };

    $charging.chargingDelete = function () {
        $(".charging").each(function () {
            var url = $(this).attr("v");
            if (url) {
                $(this).off().on("click", function () {
                    bootbox.confirm("确定废弃这条规则？", function (ok) {
                        if (ok) {
                            system.ajaxGetAsync(url, function (result) {
                                bootbox.alert(result.msg, function () {
                                    if (result.code == 0) {
                                        $charging.chargingQueryFormSubmit();
                                    }
                                });
                            });
                        }
                    });
                });
            }
        });
    };

    /**
     * 内容配置提交校验
     * @returns {boolean}
     */
    $charging.checkContent = function(){
        var $addChargingForm = $("#addChargingForm");
        var reg = new RegExp("^[0-9]*$");

        var userId = $addChargingForm.find("#userId").val();
        if (!userId) {
            bootbox.alert("错误：用户编号不能为空！");
            return false;
        }
        if(!reg.test(userId)){
            alert("错误：用户编号必须是数字!");
            return false;
        }

        var userName = $addChargingForm.find("#userName").val();
        if (!userName) {
            bootbox.alert("错误：用户姓名不能为空！");
            return false;
        }
        var telephoneNo = $addChargingForm.find("#telephoneNo").val();
        if(!telephoneNo){
            bootbox.alert("错误：用户手机号不能为空！");
            return false;
        }
        reg = new RegExp("^(((13[0-9]{1})|(15[0-9]{1})|(17[0-9]{1})|(18[0-9]{1}))+[0-9]{8})$");
        if(!reg.test(telephoneNo)){
            alert("错误：手机号格式不合法!");
            return false;
        }

        var areaId = $addChargingForm.find("#areaId").val();
        if(!areaId){
            bootbox.alert("错误：用户所在城市编号不能为空！");
            return false;
        }
        reg = new RegExp("^[0-9]{6}$");
        if(!reg.test(areaId)){
            alert("错误：城市编号必须是6位数字!");
            return false;
        }

        var areaName = $addChargingForm.find("#areaName").val();
        if(!areaName){
            bootbox.alert("错误：用户所在城市名称不能为空！");
            return false;
        }
        // var startTime = $addChargingForm.find("#startTime").val();
        // if (!startTime) {
        //     bootbox.alert("错误：开始时间不能为空！");
        //     return false;
        // }
        // var endTime = $addChargingForm.find("#endTime").val();
        // if (!endTime) {
        //     bootbox.alert("错误：结束时间不能为空！");
        //     return false;
        // }
        var benchmark = $addChargingForm.find("#benchmark").val();
        if(!benchmark){
            bootbox.alert("错误：提成基线不能为空！");
            return false;
        }
        reg = new RegExp("[0-9]*$");
        if(!reg.test(benchmark)){
            alert("错误：提成基线必须是数字!");
            return false;
        }

        var bsBenchmark = $addChargingForm.find("#bsBenchmark").val();
        if(!bsBenchmark){
            bootbox.alert("错误：底薪基线不能为空！");
            return false;
        }
        reg = new RegExp("[0-9]*$");
        if(!reg.test(bsBenchmark)){
            alert("错误：底薪基线必须是数字!");
            return false;
        }

        var fined = $addChargingForm.find("#viewFined").val();
        if(!fined){
            bootbox.alert("错误：底薪扣款不能为空！");
            return false;
        }
        reg = new RegExp("[0-9]*$");
        if(!reg.test(fined)){
            alert("错误：底薪扣款必须是数字!");
            return false;
        }

        var basicSalary = $addChargingForm.find("#viewBasicSalary").val();
        if(!basicSalary){
            bootbox.alert("错误：单日底薪不能为空！");
            return false;
        }

        var commission = $addChargingForm.find("#viewCommission").val();
        if(!commission){
            bootbox.alert("错误：提成不能为空！");
            return false;
        }

        var fbsb = parseFloat(bsBenchmark);
        var fb = parseFloat(benchmark);
        var ff = parseFloat(fined);
        var fbs = parseFloat(basicSalary);
        if(fbsb > fb){
            bootbox.alert("错误：底薪基线 > 提成基线");
            return false;
        }

        if(ff * (fb - fbsb) > fbs){
            bootbox.alert("错误：扣款 x ( 提成基线 - 底薪基线 ) > 底薪");
            return false;
        }

        return true;
    }

    $charging.contentSearchManage = function () {
        $("#chargingQueryButton").off().on("click",function(){
            $charging.chargingQueryFormSubmit();
        });

        $("#chargingAddButton").off().on("click",function(){
            $.ajax({
                type: "GET",
                url: "/charging/findChargingTemplateList",
                success: function (response) {
                    bootbox.dialog({
                            title: "新增计费规则",
                            message: response,
                            buttons: {
                                close: {
                                    label: "关 闭",
                                    className: "btn-success",
                                    callback: function () {

                                    }
                                },
                                success: {
                                    label: "确 定",
                                    className: "btn-success",
                                    callback: function () {
                                        var $addChargingForm = $("#addChargingForm");
                                        if(!$charging.checkContent()){
                                            return;
                                        }
                                        var id = "messageAddLoading";
                                        system.loading(id);
                                        $.ajax({
                                            data: $addChargingForm.serialize(),
                                            type: "POST",
                                            url: "/charging/add",
                                            success: function (response) {
                                                if (response.code == 0) {
                                                    bootbox.alert(response.msg,function(){
                                                        $("#chargingQueryButton").click();
                                                    });
                                                } else {
                                                    bootbox.alert("添加计费规则失败：【" + response.msg + "】请检查后，再次重试！");
                                                }
                                            },
                                            complete: function (XMLHttpRequest, textStatus) {
                                                system.unLoading(id);
                                            }
                                        });
                                    }
                                }
                            }
                        }
                    );
                }
        });
    });
};

})(using("charging"));