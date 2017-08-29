/**
 * 消息管理维护
 * create by zhaoyun
 */
(function ($ns) {

    $ns.initList = function (pageId) {
        system.visitToAjax(pageId);
    };

    $ns.contentQueryFormSubmit = function () {
        var formData = $('#contentQueryForm').serialize();
        system.ajaxPostAsync("/nm/list", formData, function (result) {
            $("#contentListDiv").html(result);
        });
    };

    /**
     * 内容配置提交校验
     * @returns {boolean}
     */
    $ns.checkContent = function(){
        var $addMessageForm = $("#addMessageForm");
        var userId = $addMessageForm.find("#userId").val();
        if (!userId) {
            bootbox.alert("用户编号不能为空！");
            return false;
        }
        var cmType = $addMessageForm.find("#nmType").val();
        if (!cmType) {
            bootbox.alert("内容类型不能为空！");
            return false;
        }
        var nmMode = $addMessageForm.find("#nmMode").val();
        if(!nmMode){
            bootbox.alert("通知模式类型不能为空！");
            return false;
        }
        var startTime = $addMessageForm.find("#startTime").val();
        if (!startTime) {
            bootbox.alert("开始时间不能为空！");
            return false;
        }
        var endTime = $addMessageForm.find("#endTime").val();
        if (!endTime) {
            bootbox.alert("结束时间不能为空！");
            return false;
        }
        var nmTitle = $addMessageForm.find("#nmTitle").val();
        if(!nmTitle){
            bootbox.alert("标题不能为空！");
            return false;
        }
        var nmContent = $addMessageForm.find("#nmContent").val();
        if(!nmContent){
            bootbox.alert("内容不能为空！");
            return false;
        }

        var nmDesc = $addMessageForm.find("#nmDesc").val();
        if(!nmDesc){
            bootbox.alert("内容描述不能为空！");
            return false;
        }
        var sort = $addMessageForm.find("#sort").val();
        if(!sort){
            bootbox.alert("排序号不能为空！");
            return false;
        }
        return true;
    }

    $ns.contentSearchManage = function () {
        $("#messageQueryButton").off().on("click",function(){
            $ns.contentQueryFormSubmit();
        });

        $("#messageAddButton").off().on("click",function(){
            bootbox.dialog({
                    title: "新增内容",
                    message: $.tmpl($("#addMessageTepl").html()),
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
                                var $addMessageForm = $("#addMessageForm");
                                if(!$ns.checkContent()){
                                    return;
                                }
                                var id = "messageAddLoading";
                                system.loading(id);
                                $.ajax({
                                    data: $addMessageForm.serialize(),
                                    type: "POST",
                                    url: "/nm/add",
                                    success: function (response) {
                                        if (response.code == 0) {
                                            bootbox.alert(response.msg,function(){
                                                $("#messageQueryButton").click();
                                            });
                                        } else {
                                            bootbox.alert("添加消息出现异常：【" + response.msg + "】请检查后，再次重试！");
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

    $ns.messageManage = function () {
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
                            title: "查看消息",
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
                                        var $addContentForm = $("#addMessageForm");
                                        if(!$ns.checkContent()){
                                            return;
                                        }
                                        var id = "messageAddLoading";
                                        system.loading(id);
                                        $.ajax({
                                            data: $addContentForm.serialize(),
                                            type: "POST",
                                            url: "/nm/add",
                                            success: function (response) {
                                                if (response.code == 0) {
                                                    bootbox.alert(response.msg,function(){
                                                        $("#messageQueryButton").click();
                                                    });
                                                } else {
                                                    bootbox.alert("添加消息出现异常：【" + response.msg + "】请检查后，再次重试！");
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
})(using("nm"));