/**
 * 用户挂起维护
 * create by liuhuiqing
 */
(function ($ns) {

    $ns.initList = function (pageId) {
        system.visitToAjax(pageId);
    };

    $ns.taskSuspendQueryFormSubmit = function () {
        var formData = $('#taskSuspendQueryForm').serialize();
        system.ajaxPostAsync("/suspend/task/list", formData, function (result) {
            $("#taskSuspendListDiv").html(result);
        });
    };

    $ns.suspendSearchManage = function () {
        $("#taskSuspendQueryButton").off().on("click",function(){
            $ns.taskSuspendQueryFormSubmit();
        });

        $("#taskSuspendAddButton").off().on("click",function(){
            var activityId = $("#activityId").val();
            var addTaskItem = {
                activityId: activityId?activityId:""
            };
            bootbox.dialog({
                    title: "新增挂起任务",
                    message: $.tmpl($("#addTaskTepl").html(), addTaskItem),
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
                                var $addTaskForm = $("#addTaskForm");
                                var activityId = $addTaskForm.find("#activityId").val();
                                if (!activityId) {
                                    bootbox.alert("活动编号不能为空！");
                                    return;
                                }
                                var taskNo = $addTaskForm.find("#taskNo").val();;
                                if (!taskNo) {
                                    bootbox.alert("任务编号不能为空！");
                                    return;
                                }
                                var suspendReason = $addTaskForm.find("#suspendReason").val();
                                if(!suspendReason){
                                    bootbox.alert("挂起原因不能为空！");
                                    return;
                                }
                                var reasonValue = $addTaskForm.find("#reasonValue").val();
                                if(!reasonValue){
                                    bootbox.alert("挂起原因指标不能为空！");
                                    return;
                                }
                                var id = "taskAddLoading";
                                system.loading(id);
                                $.ajax({
                                    data: $addTaskForm.serialize(),
                                    type: "POST",
                                    url: "/suspend/task/add",
                                    success: function (response) {
                                        if (response.code == 0) {
                                            bootbox.alert(response.msg,function(){
                                                $("#taskSuspendQueryButton").click();
                                            });
                                        } else {
                                            bootbox.alert("任务挂起出现异常：【" + response.msg + "】请检查后，再次重试！");
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

    $ns.suspendManage = function () {
        $(".manage").each(function () {
            var url = $(this).attr("v");
            if (url) {
                $(this).off().on("click", function () {
                    bootbox.confirm("确定要更改任务挂起状态？", function (ok) {
                        if (ok) {
                            system.ajaxGetAsync(url, function (result) {
                                bootbox.alert(result.msg, function () {
                                    if (result.code == 0) {
                                        $ns.taskSuspendQueryFormSubmit();
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
})(using("suspend"));