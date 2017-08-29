/**
 * 计费模板
 * create by liuxian
 */
(function ($template) {

    $template.initList = function (pageId) {
        system.visitToAjax(pageId);
    };

    $template.templateQueryFormSubmit = function () {
        var formData = $('#templateQueryForm').serialize();
        system.ajaxPostAsync("/chargingTemplate/list", formData, function (result) {
            $("#chargingTemplateListDiv").html(result);
        });
    };

    $template.templateDelete = function () {
        $(".template").each(function () {
            var url = $(this).attr("v");
            if (url) {
                $(this).off().on("click", function () {
                    bootbox.confirm("确定废弃这条规则？", function (ok) {
                        if (ok) {
                            system.ajaxGetAsync(url, function (result) {
                                bootbox.alert(result.msg, function () {
                                    if (result.code == 0) {
                                        $template.templateQueryFormSubmit();
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
    $template.checkContent = function(){
        var $addTemplateForm = $("#addTemplateForm");
        var reg = new RegExp("^[0-9]*$");

        var name = $addTemplateForm.find("#name").val();
        if (!name) {
            bootbox.alert("错误：模板名称不能为空！");
            return false;
        }

        var areaId = $addTemplateForm.find("#areaId").val();
        if(!areaId){
            bootbox.alert("错误：用户所在城市编号不能为空！");
            return false;
        }
        reg = new RegExp("^[0-9]{6}$");
        if(!reg.test(areaId)){
            alert("错误：城市编号必须是6位数字!");
            return false;
        }

        var areaName = $addTemplateForm.find("#areaName").val();
        if(!areaName){
            bootbox.alert("错误：用户所在城市名称不能为空！");
            return false;
        }

        var benchmark = $addTemplateForm.find("#benchmark").val();
        if(!benchmark){
            bootbox.alert("错误：提成基线不能为空！");
            return false;
        }
        reg = new RegExp("[0-9]*$");
        if(!reg.test(benchmark)){
            alert("错误：提成基线必须是数字!");
            return false;
        }

        var bsBenchmark = $addTemplateForm.find("#bsBenchmark").val();
        if(!bsBenchmark){
            bootbox.alert("错误：底薪基线不能为空！");
            return false;
        }
        reg = new RegExp("[0-9]*$");
        if(!reg.test(bsBenchmark)){
            alert("错误：底薪基线必须是数字!");
            return false;
        }

        var fined = $addTemplateForm.find("#viewFined").val();
        if(!fined){
            bootbox.alert("错误：底薪扣款不能为空！");
            return false;
        }
        reg = new RegExp("[0-9]*$");
        if(!reg.test(fined)){
            alert("错误：底薪扣款必须是数字!");
            return false;
        }

        var basicSalary = $addTemplateForm.find("#viewBasicSalary").val();
        if(!basicSalary){
            bootbox.alert("错误：单日底薪不能为空！");
            return false;
        }

        var commission = $addTemplateForm.find("#viewCommission").val();
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

    $template.contentSearchManage = function () {
        $("#templateQueryButton").off().on("click",function(){
            $template.templateQueryFormSubmit();
        });

        $("#templateAddButton").off().on("click",function(){
            bootbox.dialog({
                    title: "新增计费模板",
                    message: $.tmpl($("#addTemplateTepl").html()),
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
                                var $addTemplateForm = $("#addTemplateForm");
                                if(!$template.checkContent()){
                                    return;
                                }
                                var id = "messageAddLoading";
                                system.loading(id);
                                $.ajax({
                                    data: $addTemplateForm.serialize(),
                                    type: "POST",
                                    url: "/chargingTemplate/add",
                                    success: function (response) {
                                        if (response.code == 0) {
                                            bootbox.alert(response.msg,function(){
                                                $("#templateQueryButton").click();
                                            });
                                        } else {
                                            bootbox.alert("添加模板失败：【" + response.msg + "】请检查后，再次重试！");
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
        });
    };

})(using("template"));