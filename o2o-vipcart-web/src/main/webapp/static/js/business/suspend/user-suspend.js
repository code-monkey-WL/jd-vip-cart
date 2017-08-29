/**
 * 用户挂起维护
 * create by liuhuiqing
 */
(function ($ns) {

    $ns.initList = function (pageId) {
        system.visitToAjax(pageId);
    };

    $ns.userSuspendQueryFormSubmit = function () {
        var formData = $('#userSuspendQueryForm').serialize();
        system.ajaxPostAsync("/suspend/user/list", formData, function (result) {
            $("#userSuspendListDiv").html(result);
        });
    };

    $ns.suspendSearchManage = function () {
        $("#userSuspendQueryButton").off().on("click",function(){
            $ns.userSuspendQueryFormSubmit();
        });

        $("#userSuspendAddButton").off().on("click",function(){
            var userId = $("#userId").val();
            var addUserItem = {
                userId: userId?userId:""
            };
            bootbox.dialog({
                    title: "新增挂起用户",
                    message: $.tmpl($("#addUserTepl").html(), addUserItem),
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
                                var $addUserForm = $("#addUserForm");
                                var userId = $addUserForm.find("#userId").val();
                                if (!userId) {
                                    bootbox.alert("用户编号不能为空！");
                                    return;
                                }
                                var suspendReason = $addUserForm.find("#suspendReason").val();
                                if(!suspendReason){
                                    bootbox.alert("挂起原因不能为空！");
                                    return;
                                }
                                var reasonValue = $addUserForm.find("#reasonValue").val();
                                if(!reasonValue){
                                    bootbox.alert("挂起原因指标不能为空！");
                                    return;
                                }
                                var startTime = $addUserForm.find("#startTime").val();;
                                if (!startTime) {
                                    bootbox.alert("开始时间不能为空！");
                                    return;
                                }
                                var endTime = $addUserForm.find("#endTime").val();;
                                if (!endTime) {
                                    bootbox.alert("结束时间不能为空！");
                                    return;
                                }
                                var id = "taskAddLoading";
                                system.loading(id);
                                $.ajax({
                                    data: $addUserForm.serialize(),
                                    type: "POST",
                                    url: "/suspend/user/add",
                                    success: function (response) {
                                        if (response.code == 0) {
                                            bootbox.alert(response.msg,function(){
                                                $("#userSuspendQueryButton").click();
                                            });
                                        } else {
                                            bootbox.alert("用户挂起出现异常：【" + response.msg + "】请检查后，再次重试！");
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
                    bootbox.confirm("确定要更改用户挂起状态？", function (ok) {
                        if (ok) {
                            system.ajaxGetAsync(url, function (result) {
                                bootbox.alert(result.msg, function () {
                                    if (result.code == 0) {
                                        $ns.userSuspendQueryFormSubmit();
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