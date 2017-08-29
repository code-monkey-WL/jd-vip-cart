/**
 * 内容管理维护
 * create by liuhuiqing
 */
(function ($ns) {

    $ns.initList = function (pageId) {
        system.visitToAjax(pageId);
    };

    $ns.contentQueryFormSubmit = function () {
        var formData = $('#contentQueryForm').serialize();
        system.ajaxPostAsync("/cm/list", formData, function (result) {
            $("#contentListDiv").html(result);
        });
    };

    /**
     * 内容配置提交校验
     * @returns {boolean}
     */
    $ns.checkContent = function(){
        var $addContentForm = $("#addContentForm");
        var cmType = $addContentForm.find("#cmType").val();
        if (!cmType) {
            bootbox.alert("类型不能为空！");
            return false;
        }
        var areaId = $addContentForm.find("#areaId").val();
        if(!areaId){
            bootbox.alert("区域不能为空！");
            return false;
        }
        var startTime = $addContentForm.find("#startTime").val();;
        if (!startTime) {
            bootbox.alert("开始时间不能为空！");
            return false;
        }
        var endTime = $addContentForm.find("#endTime").val();;
        if (!endTime) {
            bootbox.alert("结束时间不能为空！");
            return false;
        }
        var cmContent = $addContentForm.find("#cmContent").val();
        if(!cmContent){
            bootbox.alert("内容取值不能为空！");
            return false;
        }
        var cmDesc = $addContentForm.find("#cmDesc").val();
        if(!cmDesc){
            bootbox.alert("内容描述不能为空！");
            return false;
        }
        var sort = $addContentForm.find("#sort").val();
        if(!sort){
            bootbox.alert("排序号不能为空！");
            return false;
        }
        return true;
    }

    $ns.contentSearchManage = function () {
        $("#contentQueryButton").off().on("click",function(){
            $ns.contentQueryFormSubmit();
        });

        $("#contentAddButton").off().on("click",function(){
            bootbox.dialog({
                    title: "新增内容",
                    message: $.tmpl($("#addContentTepl").html()),
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
                                var $addContentForm = $("#addContentForm");
                                if(!$ns.checkContent()){
                                    return;
                                }
                                var id = "contentAddLoading";
                                system.loading(id);
                                $.ajax({
                                    data: $addContentForm.serialize(),
                                    type: "POST",
                                    url: "/cm/add",
                                    success: function (response) {
                                        if (response.code == 0) {
                                            bootbox.alert(response.msg,function(){
                                                $("#contentQueryButton").click();
                                            });
                                        } else {
                                            bootbox.alert("添加内容出现异常：【" + response.msg + "】请检查后，再次重试！");
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

    $ns.contentManage = function () {
        $(".manage").each(function () {
            var url = $(this).attr("v");
            if (url) {
                $(this).off().on("click", function () {
                    bootbox.confirm("确定要删除该记录？", function (ok) {
                        if (ok) {
                            system.ajaxGetAsync(url, function (result) {
                                bootbox.alert(result.msg, function () {
                                    if (result.code == 0) {
                                        $ns.contentQueryFormSubmit();
                                    }
                                });
                            });
                        }
                    });
                });
            }
        });

        $(".detail").off().on("click",function(){
            var id="detailLoading";
            system.loading(id);
            $.ajax({
                type: "GET",
                url: $(this).attr("v"),
                success: function (response) {
                    bootbox.dialog({
                            title: "查看内容",
                            message: response,
                            buttons: {
                                close: {
                                    label: "关 闭",
                                    className: "btn-success",
                                    callback: function () {

                                    }
                                },
                                success: {
                                    label: "复 制",
                                    className: "btn-success",
                                    callback: function () {
                                        var $addContentForm = $("#addContentForm");
                                        if(!$ns.checkContent()){
                                            return;
                                        }
                                        var id = "contentAddLoading";
                                        system.loading(id);
                                        $.ajax({
                                            data: $addContentForm.serialize(),
                                            type: "POST",
                                            url: "/cm/add",
                                            success: function (response) {
                                                if (response.code == 0) {
                                                    bootbox.alert(response.msg,function(){
                                                        $("#contentQueryButton").click();
                                                    });
                                                } else {
                                                    bootbox.alert("添加内容出现异常：【" + response.msg + "】请检查后，再次重试！");
                                                }
                                            },
                                            complete: function (XMLHttpRequest, textStatus) {
                                                system.unLoading(id);
                                            }
                                        });
//                                        bootbox.confirm("您确定要复制当前记录另存为新的记录吗？", function (ok) {
//                                            if (ok) {
//
//                                            }
//                                        });
                                    }
                                }
                            }
                        }
                    );
                },
                complete: function (XMLHttpRequest, textStatus) {
                    system.unLoading(id);
                }
            });
        });
    };

    /**
     * 带有去重功能的数组集合
     * @param element
     * @param arr
     * @returns {*}
     */
    $ns.addSet = function (element, arr) {
        if (!arr) {
            arr = [];
            arr.push(element);
            return arr;
        }
        var isHave = false;
        for (t in arr) {
            if (arr[t] == element) {
                isHave = true;
                break
            }
        }
        if (!isHave) {
            arr.push(element);
        }
        return arr;
    };

    /**
     * 组织冒泡事件
     * @param parent
     * @param child
     */
    $ns.stopPropagation = function (parent, child) {
        var p = $(parent);
        var c = $(child);
        p.click(function () {
            c.attr("checked", (c.attr("checked") == true) ? false : true);
        });
        c.click(function (e) {
            stopBubble(e);
        });
        function stopBubble(e) {
            if (e) {
                e.stopPropagation();
            } else {
                window.event.cancelBubble = true;
            }
        }
    };
})(using("cm"));