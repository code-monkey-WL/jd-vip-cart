/**
 * 活动维护
 * create by liuhuiqing
 */
(function ($ns) {

    $ns.initList = function (pageId) {
        system.visitToAjax(pageId);
    };

    $ns.activityQueryFormSubmit = function () {
        var formData = $('#activityQueryForm').serialize();
        system.ajaxPostAsync("/act/list", formData, function (result) {
            $("#activityListDiv").html(result);
        });
    };

    $ns.activityManage = function () {
        $(".audit-manage").each(function () {
            var url = $(this).attr("v");
            if (url) {
                $(this).off().on("click", function () {
                    $(this).attr("href", url);
                    $(this).attr("target", "_blank");
                });
            }
        });
        $(".activity-manage").each(function () {
            var url = $(this).attr("v");
            if (url) {
                $(this).off().on("click", function () {
                    bootbox.confirm("确定要更改活动状态？", function (ok) {
                        if (ok) {
                            system.ajaxGetAsync(url, function (result) {
                                bootbox.alert(result.msg, function () {
                                    if (result.code == 0) {
                                        $ns.activityQueryFormSubmit();
                                    }
                                });
                            });
                        }
                    });
                });
            }
        });
        $(".task-import").each(function () {
            var url = $(this).attr("v");
            var importUrl = $(this).attr("url");
            var activityId = $(this).attr("activityId");
            var activityTypeName = $(this).attr("activityTypeName");
            var activityName = $(this).attr("activityName");
            var title = $(this).text();
            if (url) {
                $(this).off().on("click", function () {
                    var taskImportItem = {
                        activityId: activityId,
                        activityTypeName: activityTypeName,
                        activityName: activityName
                    };
                    bootbox.dialog({
                            title: title,
                            message: $.tmpl($("#taskImportTepl").html(), taskImportItem),
                            buttons: {
                                close: {
                                    label: "关 闭",
                                    className: "btn-success",
                                    callback: function () {

                                    }
                                },
                                success: {
                                    label: "导 入",
                                    className: "btn-success",
                                    callback: function () {
                                        var $taskImportForm = $("#taskImportForm");
                                        var path = $("#taskList").val();
                                        if (!path) {
                                            bootbox.alert("请选择要导入的任务文件");
                                            return;
                                        }
                                        if (!path.endWith(".xls") && !path.endWith(".XLS")
                                            && !path.endWith(".xlsx") && !path.endWith(".XLSX")) {
                                            bootbox.alert("导入的任务文件的类型必须是xls或xlsx格式！");
                                            return;
                                        }
                                        var data = new FormData($taskImportForm[0]);
                                        var id = "taskImportLoading";
                                        system.loading(id);
                                        $.ajax({
                                            data: data,
                                            type: "POST",
                                            url: importUrl,
                                            cache: false,
                                            contentType: false,
                                            processData: false,
                                            success: function (response) {
                                                if (response.code == 0) {
                                                    bootbox.alert(response.msg);
                                                } else {
                                                    bootbox.alert("文件导入可能出现异常：【" + response.msg + "】请检查后，再确认是否需要重新上传！");
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
            }
        });
    };

    $ns.areaPrice = function (elementId) {

        $("input[name='priceRadios']").off().on("click", function () {
            var value = $("input[name='priceRadios']:checked").val();
            if (value == 0) {
                $("#allAreaDiv").find(":text").removeAttr("disabled");
                $("#partAreaDiv").find(":text,select").attr("disabled", "disabled").val("");
                $(".partAreaResultDiv").remove();
            } else {
                $("#allAreaDiv").find(":text").attr("disabled", "disabled").val("");
                $("#partAreaDiv").find(":text,select").removeAttr("disabled");
            }
        });
        $("#" + elementId).off().on("click", function () {
            var areaId = $("#areaId").val();
            var price = $("#price").val();
            if (!areaId || !price) {
                bootbox.alert("城市和价格不能为空！");
                return;
            }
            var $modelAreaIds = $(".partAreaResultDiv").find("input[name='areaIds']");
            for (var i = 0; i < $modelAreaIds.length; i++) {
                if ($modelAreaIds.eq(i).val() == areaId) {
                    bootbox.alert("目标城市已经设置过赏金配置，请确认！");
                    return;
                }
            }
            var $newSetting = $('<div class="form-group partAreaResultDiv">' +
                '<div class="col-sm-2  text-right"><i class="glyphicon glyphicon-circle-arrow-right"></i></div>' +
                '<div class="col-sm-4">' +
                '<input name="areaIds" type="hidden"/>' +
                '</div>' +
                '<div class="col-sm-4">' +
                '<input name="prices" type="hidden"/>' +
                '</div>' +
                '<div class="col-sm-1">' +
                '<i class="btn btn-primary glyphicon glyphicon-remove-circle">删 除</i>' +
                '</div></div>');
            var $currAreaIds = $newSetting.find("input[name='areaIds']");
            var $currPrices = $newSetting.find("input[name='prices']");
            var $d = $newSetting.find(".glyphicon-remove-circle");
            $currAreaIds.val(areaId);
            $currAreaIds.parent().append($("#areaId").find("option:selected").text());
            $currPrices.val(price);
            $currPrices.parent().append(price + "元");
            $d.click(function () {
                $(this).parent().parent().remove();
            });
            $("#partAreaDiv").after($newSetting);
        });
    };


    $ns.addExtend = function (elementId) {

        $(":radio").click(function(){
            if($(this).val() == "0"){
                $("#areaId").attr("disabled","disabled");//再改成disabled
            }
            if($(this).val() == "1"){
                $("#areaId").removeAttr("disabled");
            }
        });
        $("#" + elementId).off().on("click", function () {
            var areaId = $("#areaId").val();
            var text = $(".activityContentEdit").summernote('code');
            if($("input:radio:checked").val() == "0"){
                areaId = $("input:radio:checked").val();
            }
            if (!areaId || !text) {
                bootbox.alert("城市和扩展内容不能为空！");
                return;
            }
            var $newSetting = $('<div class="form-group partAreaResultDiv">' +
                '<div class="col-sm-2  text-right"><i class="glyphicon glyphicon-circle-arrow-right"></i></div>' +
                '<div class="col-sm-4">' +
                '<input name="areaId" type="hidden"/>' +
                '</div>' +
                '<div class="col-sm-4">' +
                '<input name="extContent" type="hidden"/>' +
                '</div>' +
                '<div class="col-sm-1">' +
                '<i class="btn btn-primary glyphicon glyphicon-remove-circle">删 除</i>' +
                '</div></div>');
            var $currAreaIds = $newSetting.find("input[name='areaId']");
            var $extContent = $newSetting.find("input[name='extContent']");
            var $d = $newSetting.find(".glyphicon-remove-circle");
            $currAreaIds.val(areaId);
            $currAreaIds.parent().append($("#areaId").find("option:selected").text());
            $extContent.val(text);
            $extContent.parent().append(text);
            $d.click(function () {
                $(this).parent().parent().remove();
            });
            $("#partAreaDiv").after($newSetting);
        });
    };

    $ns.initTaskSource = function () {
        $("input[name='activityEntity.taskSource']").off().on("click", function () {
            var taskSource = $("input[name='activityEntity.taskSource']:checked").val();
            if(taskSource == 3){
                $("#taskSourceUrlDiv").show(300);
            }else{
                $("#taskSourceUrlDiv").hide(300).find(":input").val("");
            }
        });
    };

    $ns.initTaskAttr = function () {
        var $taskAttrDiv = $("#taskAttrDiv");
        var activityTypeOptionDefault = $taskAttrDiv.find('.attr-type').html();
        function createTaskLine(attr) {
            return [
                "<tr>",
                    "<td>" + attr.typeDesc + "</td>",
                    "<td>" + attr.showTypeDesc + "</td>",
                    "<td class='td-sort'>" + attr.sort + "</td>",
                    "<td>" + attr.name + "</td>",
                "<td><a href='javascript:void(0);' class='del-attr'>删除</a>",
                    "<input type='hidden' name='activityAttr.attrType' value='" + attr.type + "'>",
                    "<input type='hidden' name='activityAttr.displayType' value='" + attr.showType + "'>",
                    "<input type='hidden' name='activityAttr.sort' value='" + attr.sort + "'>",
                    "<input type='hidden' name='activityAttr.attrName' value='" + attr.name + "'>",
                "</td>",
                "</tr>"
            ].join("");
        }

        $("[id='activityEntity.activityType']").off().on("change", function () {
            var activityType = $(this).val();
            $("#taskAttrDescDiv").removeClass().empty();
            if(activityType == 2){
                //任务名称和任务地址
                $taskAttrDiv.find(".attr-type").empty()
                    .append("<option value='2'>任务地址</option>")
                    .append("<option value='3'>任务名称</option>");
                $("#taskAttrDescDiv").addClass("alert alert-warning").html("<strong>注意：</strong>此处只需要配置<strong>任务名称和任务地址</strong>属性即可");
            }else{
                $taskAttrDiv.find(".attr-type").empty().append(activityTypeOptionDefault);
            }
        });

        $taskAttrDiv.on('click', '.del-attr', function (e) {
            $(e.target).parents('tr').remove();
        });

        $taskAttrDiv.find('.add-attr-btn').click(function (e) {
            e.stopPropagation();
            var attrType = $taskAttrDiv.find('.attr-type').val();
            var attrTypeDesc = $taskAttrDiv.find('.attr-type option:selected').text();
            var attrShowType = $taskAttrDiv.find('.attr-show-type').val();
            var attrShowTypeDesc = $taskAttrDiv.find('.attr-show-type option:selected').text();
            var attrSort = $taskAttrDiv.find('.attr-sort').val();
            var attrName = $taskAttrDiv.find('.attr-name').val();
            if (!attrType || !attrShowType || !attrSort || !attrName){
                bootbox.alert("活动属性入参不能为空！");
                return;
            }

            var line = createTaskLine({
                type: attrType,
                typeDesc: attrTypeDesc,
                name: attrName,
                showType: attrShowType,
                showTypeDesc: attrShowTypeDesc,
                sort: attrSort
            });
            var $tbody = $taskAttrDiv.find('.task-attr-tbody');
            $tbody.append(line);
            var $trs = $tbody.find('tr');
            $trs.sort(function (a, b) {
                return parseInt($(a).find('.td-sort').text().trim()) - parseInt($(b).find('.td-sort').text().trim());
            });
            $trs.detach().appendTo($tbody);
            $taskAttrDiv.find('.attr-type').val(0);
            $taskAttrDiv.find('.attr-name').val("")
            $taskAttrDiv.find('.attr-sort').val(++attrSort);
        });
    };

    $ns.initCallbackConfig = function () {
        $("#saveCallbackBtn").off().on("click", function () {
            var activityId = $("#activityId").val();
            var callbackUrl = $("#callbackUrl").val();
            var callbackStates = $("#callbackStates").val();
            if(!activityId){
                bootbox.alert("没有找到指定活动！");
                return;
            }
            var RegUrl = new RegExp();
            RegUrl.compile("[a-zA-z]+://[^s]*");
            if (!RegUrl.test(callbackUrl)) {
                bootbox.alert("状态回传服务URL格式不正确！");
                return;
            }
            if(callbackStates){
                var cs = callbackStates.split(",");
                for(var i=0;i<cs.length;i++){
                    if(cs[i].split("=").length!=2){
                        bootbox.alert("业务状态类型格式不正确！");
                        return;
                    }
                }
            }
            bootbox.confirm("确定要提交保存配置信息？", function (ok) {
                if (ok) {
                    var id = "callbackLoading";
                    system.loading(id);
                    $.ajax({
                        url: "/act/saveCallback",
                        type: "post",
                        data: $("#callBackForm").serialize(),
                        success: function (result) {
                            if (result.code == "0") {
                                bootbox.alert(result.msg, function () {
                                    window.location.href="/act/goActivityConfig?activityId="+activityId;
                                });
                            } else {
                                bootbox.alert(result.msg);
                            }
                        },
                        complete: function (XMLHttpRequest, textStatus) {
                            system.unLoading(id);
                        }
                    });
                }
            });
        });
    };
    $ns.initChargingConfig = function () {
        $(".city-online").off().on("click", function () {
            var activityId = $(this).attr("activityId");
            if(!activityId){
                bootbox.alert("没有找到指定活动！");
                return;
            }
            var areaId = $(this).attr("areaId");
            if(!areaId){
                bootbox.alert("没有找到指定城市！");
                return;
            }
            bootbox.confirm("确定要提交保存配置信息？", function (ok) {
                if (ok) {
                    var id = "cityOnlineLoading";
                    system.loading(id);
                    $.ajax({
                        url: "/act/cityOnline?activityId="+activityId+"&areaIds="+areaId,
                        type: "get",
                        success: function (result) {
                            if (result.code == "0") {
                                bootbox.alert(result.msg, function () {
                                    window.location.href="/act/goActivityConfig?activityId="+activityId;
                                });
                            } else {
                                bootbox.alert(result.msg);
                            }
                        },
                        complete: function (XMLHttpRequest, textStatus) {
                            system.unLoading(id);
                        }
                    });
                }
            });
        });
        $(".city-offline").off().on("click", function () {
            var activityId = $(this).attr("activityId");
            if(!activityId){
                bootbox.alert("没有找到指定活动！");
                return;
            }
            var areaId = $(this).attr("areaId");
            if(!areaId){
                bootbox.alert("没有找到指定城市！");
                return;
            }
            bootbox.confirm("确定要提交保存配置信息？", function (ok) {
                if (ok) {
                    var id = "cityOfflineLoading";
                    system.loading(id);
                    $.ajax({
                        url: "/act/cityOffline?activityId="+activityId+"&areaIds="+areaId,
                        type: "get",
                        success: function (result) {
                            if (result.code == "0") {
                                bootbox.alert(result.msg, function () {
                                    window.location.href="/act/goActivityConfig?activityId="+activityId;
                                });
                            } else {
                                bootbox.alert(result.msg);
                            }
                        },
                        complete: function (XMLHttpRequest, textStatus) {
                            system.unLoading(id);
                        }
                    });
                }
            });
        });

        $("#saveChargingConfigBtn").off().on("click", function () {
            var activityId = $("#activityId").val();
            if(!activityId){
                bootbox.alert("没有找到指定活动！");
                return;
            }
            bootbox.confirm("确定要提交保存配置信息？", function (ok) {
                if (ok) {
                    var id = "chargingConfigLoading";
                    system.loading(id);
                    $.ajax({
                        url: "/act/saveChargingConfig",
                        type: "post",
                        data: $("#chargingConfigForm").serialize(),
                        success: function (result) {
                            if (result.code == "0") {
                                bootbox.alert(result.msg, function () {
                                    window.location.href="/act/goActivityConfig?activityId="+activityId;
                                });
                            } else {
                                bootbox.alert(result.msg);
                            }
                        },
                        complete: function (XMLHttpRequest, textStatus) {
                            system.unLoading(id);
                        }
                    });
                }
            });
        });
    };
    $ns.initAuditGroupConfig = function () {
        var tempIdGen = 1;
        // 未保存的分组列表
        var unsaveGroupList = [];

        $("#addGroupButton").off().on("click", function () {
            var $groupName = $("#auditGroupName");
            var $sort = $("#auditGroupSort");
            var $placeHolder = $("#auditGroupPlaceHolder");
            var $endMark = $("#endMark");
            var groupName = $groupName.val();
            var sort = $sort.val();
            var placeHolder = $placeHolder.val();
            var endMark = $endMark.val();
            if (!groupName || !sort) {
                bootbox.alert("分组名称和排序号不能为空！");
                return;
            }
            var groupItem = {
                tempId: tempIdGen++,
                groupName: groupName,
                placeHolder: placeHolder,
                endMark:endMark,
                endMarkName:$endMark.find("option:selected").text(),
                sort: sort
            };
            unsaveGroupList.push(groupItem);
            var $tr = $.tmpl($("#auditGroupTemplate").html(), groupItem);
            $tr.on("click", ".del-group-btn", function (e) {
                var tempId = $(e.target).attr("data-tempId");
                for (var len = unsaveGroupList.length, i = len - 1; i >= 0; i--) {
                    var item = unsaveGroupList[i];
                    if (item.tempId == tempId) {
                        unsaveGroupList.splice(i, 1);
                        $tr.remove();
                        break;
                    }
                }
            });
            $tr.appendTo("#auditGroupList");
            $groupName.val("");
            $sort.val(parseInt(sort) + 1);
            $placeHolder.val("");
        });
        $ns.deleteAuditGroup();
        $("#saveAuditGroupBtn").off().on("click", function () {
            var size = unsaveGroupList.length;
            if (size < 1) {
                bootbox.alert("请先进行分组添加，在进行保存操作！");
                return;
            }
            var activityId = $(this).attr("activityId");
            var auditConfigGroupVOList = [];
            for (var i = 0; i < size; i++) {
                var auditConfigGroupEntity = {
                    activityId: activityId,
                    groupName: unsaveGroupList[i].groupName,
                    placeHolder: unsaveGroupList[i].placeHolder,
                    endMark:unsaveGroupList[i].endMark,
                    sort: unsaveGroupList[i].sort
                };
                auditConfigGroupVOList.push({"auditConfigGroupEntity": auditConfigGroupEntity});
            }
            bootbox.confirm("确定要提交保存分组配置信息？", function (ok) {
                if (ok) {
                    var id = "auditGroupLoading";
                    system.loading(id);
                    $.ajax({
                        url: "/act/saveAuditConfigGroup",
                        type: "post",
                        dataType: "json",
                        contentType: 'application/json',
                        data: JSON.stringify(auditConfigGroupVOList),
                        success: function (result) {
                            if (result.code == "0") {
                                unsaveGroupList.length = 0;
                                bootbox.alert(result.msg, function () {
                                    $("#auditGroupList").empty();
                                    system.ajaxGetAsync("/act/listAuditConfigGroup?activityId=" + activityId, function (configResult) {
                                        $("#auditGroupList").html(configResult);
                                    });
                                });
                            } else {
                                bootbox.alert(result.msg);
                            }
                        },
                        complete: function (XMLHttpRequest, textStatus) {
                            system.unLoading(id);
                        }
                    });
                }
            });
        });

        $("#auditGroupList").off().on("click", ".group", function () {
            var $radioGroup = $(this).find(":radio");
            var groupId = $radioGroup.val();
            var groupName = $radioGroup.attr('data-group-name');
            if (groupId) {
                $radioGroup.attr("checked", "checked");
            } else {
                groupId = "";
                groupName = "";
            }
            $("#configGroupIdInput").val(groupId);
            $("#configGroupNameInput").val(groupName);

        });
    };

    $ns.deleteAuditGroup = function () {
        $("#auditGroupList").find(".del-group-btn").off().on("click", function (e) {
            var $group = $(e.target);
            var groupId = $group.attr("data-id");
            if (!groupId) {
                bootbox.alert("找不到要删除的分组编号！");
            }
            bootbox.confirm("确认要删除分组[" + $group.attr("data-group-name") + "]信息", function (ok) {
                if (ok) {
                    system.ajaxPostAsync("/act/deleteAuditConfigGroup?groupIds=" + groupId, null, function (result) {
                        if (result.code == "0") {
                            bootbox.alert(result.msg, function () {
                                $("#auditGroupList").empty();
                                var url = "/act/listAuditConfigGroup?activityId=" + $group.attr("activityId");
                                system.ajaxGetAsync(url, function (configResult) {
                                    $("#auditGroupList").html(configResult);
                                });
                            });
                        } else {
                            bootbox.alert(result.msg);
                        }
                    });
                }
            });
        });
    };

    $ns.submitActivity = function (formId) {
        var form = $("#" + formId);
        var data = form.data('bootstrapValidator');
        data.validate();
        var checkPassed = activity.editorToForm("activityContentEdit", form, false);
        if (data && checkPassed) {
            if (data.isValid()) {
                var $tr = $(".task-attr-tbody").find("tr");
                if ($tr.length < 1) {
                    bootbox.alert("请设置活动任务属性！");
                    return;
                }
                bootbox.confirm("确定要提交数据吗？", function (ok) {
                    if(ok){
                        var id = "auditGroupLoading";
                        system.loading(id);
                        form.ajaxSubmit({
                            complete: function() {
                                system.unloading(id);
                            },
                            success: function(data) {
                                if(data.code == '0') {
                                    bootbox.alert("提交成功", function() {
                                        window.location.href = "/act/search";
                                    });
                                } else {
                                    bootbox.alert('提交失败! ' + data.msg);
                                }
                            },
                            error: function() {
                                bootbox.alert("提交失败");
                            }
                        });
                        $(this).attr("disabled", true);
                    }
                });
                return;
            }
        }
        bootbox.alert("表单验证不通过，请检查！");
    };

    $ns.submitActivityExtend = function (formId) {
        var form = $("#" + formId);
        var data = form.serialize();
        if(!data){
            return;
        }
        bootbox.confirm("确定要提交数据吗？", function (result) {
            if(result){
                $.ajax({
                    url: "/act/saveActivityExtend",
                    type: "post",
                    async : false,
                    data: data,
                    success: function (result) {
                        var actId = $("#activityId").val();
                        bootbox.alert(result.msg, function() {
                            window.location.href = "/act/goActEx?activityId="+actId;
                        });
                    }
                });
            }
        });
        return;
    };

    $ns.bindSubmitAuditConfig = function () {
        $("#submitAuditConfig").off().on("click", function () {
            var tr = $("#elementListBody").find("tr");
            var activityId = $("#activityId").val();
            if (tr.length < 1) {
                bootbox.alert("请添加审核校验页元素配置！");
                return;
            }
            if (!activityId) {
                bootbox.alert("无法获取活动信息！");
                return;
            }
            var auditConfigVOList = [];
            tr.each(function () {
                var currTr = $(this);
                var auditConfigVO = {};
                var auditConfigEntity = {};
                var auditConfigContentEntityList = [];
                var activityAttrId = "";
                currTr.find(":hidden").each(function () {
                    var name = $(this).attr("name");
                    var value = $(this).val();
                    var auditConfigContentEntity = {};
                    if (name == "tag" && value) {
                        var keyValues = value.split(",");
                        auditConfigContentEntity.tagName = keyValues[0];
                        auditConfigContentEntity.tagValue = keyValues[1];
                        auditConfigContentEntity.nextGroupId = keyValues[2];
                        auditConfigContentEntity.auditSuggestion=keyValues[3];
                        auditConfigContentEntity.sort = auditConfigContentEntityList.length;
                        auditConfigContentEntityList.push(auditConfigContentEntity);
                        if(auditConfigContentEntity.nextGroupId){
                            auditConfigEntity.affectFlow = 1;
                        }
                        auditConfigEntity.moreContent = 1;
                    } else {
                        if (name == "activityAttrId") {
                            activityAttrId = value;
                        } else {
                            auditConfigEntity[name] = value;
                            auditConfigEntity.moreContent = 0;
                        }
                    }
                });
                auditConfigEntity.activityId = activityId;
                auditConfigVO.activityAttrId = activityAttrId;
                auditConfigVO.auditConfigEntity = auditConfigEntity;
                auditConfigVO.auditConfigContentEntityList = auditConfigContentEntityList;
                auditConfigVOList.push(auditConfigVO);
            });
            bootbox.confirm("确定要提交保存审核配置信息？", function (ok) {
                if (ok) {
                    var id = "auditConfigLoading";
                    system.loading(id);
                    $.ajax({
                        url: "/act/saveAuditConfig",
                        type: "post",
                        dataType: "json",
                        contentType: 'application/json',
                        data: JSON.stringify(auditConfigVOList),
                        success: function (result) {
                            if (result.code == "0") {
                                bootbox.alert(result.msg, function () {
                                    $("#elementListBody").remove();
                                    system.ajaxGetAsync("/act/listAuditConfig?activityId=" + activityId, function (configResult) {
                                        $("#activityConfigDiv").html(configResult);
                                    });
                                });
                            } else {
                                bootbox.alert(result.msg);
                            }
                        },
                        complete: function (XMLHttpRequest, textStatus) {
                            system.unLoading(id);
                        }
                    });
                }
            });
        });
    };

    $ns.wizard = function (id, formId) {
        $('#' + id).bootstrapWizard({
            onNext: function (tab, navigation, index) {
//                var $form = $("#"+form);
//                var data = $form.data('bootstrapValidator');
//                data.validate();
//                if (!data || !data.isValid()) {
//                    return false;
//                }
//                return true;
            },
            onTabClick: function () {
//                return false;
            },
            onTabShow: function (tab, navigation, index) {
                var $total = navigation.find('li').length;
                var $current = index + 1;
                var $percent = ($current / $total) * 100;
                $('#rootwizard .progress-bar').css({width: $percent + '%'});
            }
        });
    };

    $ns.wizardActivityContent = function (id) {
        $('#' + id).bootstrapWizard({
            onNext: function (tab, navigation, index) {

            },
            onTabShow: function (tab, navigation, index) {
                var $total =
                    navigation.find('li').length;
            }
        });
    };

    $ns.bindUploadLogoButton = function (uploadLogoButtonId) {
        $("#" + uploadLogoButtonId).off().on("click", function () {
            var logoUrlUpload = $("#logoUrlUpload");
            var path = logoUrlUpload.val();
            if (!path) {
                bootbox.alert("请选择要上传的logo图片");
                return;
            }
            if (!path.endWith(".jpg") && !path.endWith(".JPG")
                && !path.endWith(".png") && !path.endWith(".PNG")
                && !path.endWith(".gif") && !path.endWith(".GIF")) {
                bootbox.alert("上传的logo图片的类型必须是jpg，png或gif格式！");
                return;
            }
            var uploadForm = $("<form id='activityForm' method='post' enctype='multipart/form-data'></form>");
            var cloneFile = logoUrlUpload.clone(true);
            uploadForm.append(logoUrlUpload);
            $("#logoUrlUploadLabel").html(cloneFile);
            var data = new FormData(uploadForm[0]);
            data.append("file", path);
            var id = "logoUrlLoading";
            system.loading(id);
            $.ajax({
                data: data,
                type: "POST",
                url: "/img/upload",
                cache: false,
                contentType: false,
                processData: false,
                success: function (response) {
                    if (response.code == 0) {
                        var imageUrl = response.result.imgUrl;
                        if (imageUrl) {
                            $("[id='activityEntity.logoUrl']").val(imageUrl);
                            $("#logoUrlImgView").attr("src", imageUrl).show();
                            bootbox.alert("图片上传成功！");
                            return;
                        }
                    }
                    bootbox.alert(response.result);
                },
                complete: function (XMLHttpRequest, textStatus) {
                    system.unLoading(id);
                }
            });
        });
    };

    // 参照实现：http://blog.csdn.net/qing_gee/article/details/51027040
    $ns.editorInit = function (editorClass) {
        $('.' + editorClass).each(function () {
            var editor = $(this);
            var placeholder = editor.attr("placeholder") || '';
            var url = editor.attr("action") || '';
            $(this).summernote({
                lang: 'zh-CN',
                placeholder: placeholder,
                minHeight: 300,
                dialogsFade: true,// Add fade effect on dialogs
                dialogsInBody: true,// Dialogs can be placed in body, not in
                disableDragAndDrop: false,// default false You can disable drag
                callbacks: {
                    onImageUpload: function (files) {
                        var $files = $(files);
                        $files.each(function () {
                            var file = this;
                            var data = new FormData();
                            data.append("file", file);
                            var id = "insertImageLoading";
                            system.loading(id);
                            $.ajax({
                                data: data,
                                type: "POST",
                                url: url,
                                cache: false,
                                contentType: false,
                                processData: false,
                                success: function (response) {
                                    if (response.code == 0) {
                                        var imageUrl = response.result.imgUrl;
                                        editor.summernote('insertImage', imageUrl, function ($image) {
                                        });
                                    } else {
                                        bootbox.alert(response.result);
                                    }
                                },
                                complete: function (XMLHttpRequest, textStatus) {
                                    system.unLoading(id);
                                }
                            });
                        });
                    }
                }
            });
        });
    };
    $ns.datePickerValid = function (formId, element) {
        var name = $(element).attr("name");
        var status = 'NOT_VALIDATED';
        if ($(element).val()) {
            status = 'VALIDATED';
        }
        $("#" + formId).bootstrapValidator('updateStatus', name, status).bootstrapValidator('validateField', name);
    };

    $ns.editorToForm = function (editorClass, form) {
        var $form = $(form);
        var checked = true;
        // 富文本编辑器
        $("." + editorClass, $form).each(function () {
            var $this = $(this);
            var name = $this.attr("name");
//            var $input = $form.find("input[name='"+name+"']");
            var text = $this.summernote('code');
//            if($input.length > 0){
            // 暂时不放开数据回填功能
//                if(!text){
//                    text = $input.val();
//                }
//                $this.summernote('editor.insertText', text);
//            }else{
            if (!$this.summernote('isEmpty')) {
                var editor = "<input type='hidden' name='" + name + "' value='" + text + "' />";
                $form.append(editor);
            } else {
                if ($this.hasClass("required")) {
                    checked = false;
                    return false;
                }
            }
//            }
        });
        return checked;
    };

    $ns.iframeCallback = function (editorClass, form, callback) {
        var $form = $(form);
        var $iframe = $("#callbackframe");

        var data = $form.data('bootstrapValidator');
        if (data) {
            if (!data.isValid()) {
                return false;
            }
        }
        $ns.editorToForm(editorClass, form);
        if ($iframe.size() == 0) {
            $iframe = $("<iframe id='callbackframe' name='callbackframe' src='about:blank' style='display:none'></iframe>").appendTo("body");
        }
        if (!form.ajax) {
            $form.append('<input type="hidden" name="ajax" value="1" />');
        }
        form.target = "callbackframe";

        $ns._iframeResponse($iframe[0], callback);
    };

    $ns._iframeResponse = function (iframe, callback) {
        var $iframe = $(iframe);
        var $document = $(document);

        $document.trigger("ajaxStart");

        $iframe.bind("load", function (event) {
            $iframe.unbind("load");
            $document.trigger("ajaxStop");

            if (iframe.src == "javascript:'%3Chtml%3E%3C/html%3E';" || // For
                // Safari
                iframe.src == "javascript:'<html></html>';") { // For FF, IE
                return;
            }

            var doc = iframe.contentDocument || iframe.document;

            // fixing Opera 9.26,10.00
            if (doc.readyState && doc.readyState != 'complete')
                return;
            // fixing Opera 9.64
            if (doc.body && doc.body.innerHTML == "false")
                return;

            var response;

            if (doc.XMLDocument) {
                // response is a xml document Internet Explorer property
                response = doc.XMLDocument;
            } else if (doc.body) {
                try {
                    response = $iframe.contents().find("body").text();
                    response = jQuery.parseJSON(response);
                } catch (e) { // response is html document or plain text
                    response = doc.body.innerHTML;
                }
            } else {
                // response is a xml document
                response = doc;
            }
            callback(response);
        });
    };

    $ns.validateActivityForm = function () {
        $('#activityForm').bootstrapValidator({
            excluded: [':disabled'],
            message: '输入值不符合要求',
            feedbackIcons: {
                valid: 'glyphicon glyphicon-ok',
                invalid: 'glyphicon glyphicon-remove',
                validating: 'glyphicon glyphicon-refresh'
            },
            fields: {
                'activityEntity.activityType': {
                    validators: {
                        notEmpty: {
                            message: '活动类型不能为空'
                        },
                        integer: {
                            message: '必须是整数'
                        }
                    }
                },
                'activityEntity.taskSource': {
                    validators: {
                        notEmpty: {
                            message: '任务来源不能为空'
                        }
                    }
                },
                'activityEntity.orgCode': {
                    validators: {
                        notEmpty: {
                            message: '活动商家不能为空'
                        }
                    }
                },
                'activityEntity.activityName': {
                    validators: {
                        notEmpty: {
                            message: '活动名称不能为空'
                        }
                    }
                },
                'activityEntity.startTime': {
                    validators: {
                        notEmpty: {
                            message: '活动开始时间不能为空'
                        },
                        date: {
                            format: 'YYYY-MM-DD HH:MM:SS',
                            message: '活动开始时间格式不正确'
                        }
                    }
                },
                'activityEntity.endTime': {
                    validators: {
                        notEmpty: {
                            message: '活动结束时间不能为空'
                        },
                        date: {
                            format: 'YYYY-MM-DD HH:MM:SS',
                            message: '活动结束时间格式不正确'
                        }
                    }
                },
                'activityEntity.summary': {
                    validators: {
                        notEmpty: {
                            message: '活动公告不能为空'
                        }
                    }
                },
//                'activityEntity.description': {
//                    validators: {
//                        notEmpty: {
//                            message: '活动内容不能为空'
//                        }
//                    }
//                },
//                'activityEntity.requirement': {
//                    validators: {
//                        notEmpty: {
//                            message: '操作要求不能为空'
//                        }
//                    }
//                },
                'activityEntity.auditType': {
                    validators: {
                        notEmpty: {
                            message: '审核方式不能为空'
                        },
                        integer: {
                            message: '必须是整数'
                        }
                    }
                },
                'activityEntity.submitSecond': {
                    validators: {
                        notEmpty: {
                            message: '提交周期不能为空'
                        },
                        integer: {
                            message: '必须是整数'
                        }
                    }
                },
                'activityEntity.auditSecond': {
                    validators: {
                        notEmpty: {
                            message: '审核周期不能为空'
                        },
                        integer: {
                            message: '必须是整数'
                        }
                    }
                },
                'activityEntity.price': {
                    validators: {
                        notEmpty: {
                            message: '任务赏金不能为空'
                        },
                        greaterThan: {
                            value: 0,
                            message: '任务赏金必须是正实数'
                        }
                    }
                }
            }
        });
//            .on('status.field.bv', function(e, data) {
//            var $form     = $(e.target),
//                validator = data.bv,
//                $tabPane  = data.element.parents('.tab-pane'),
//                tabId     = $tabPane.attr('id');
//
//            if (tabId) {
//                var $icon = $('a[href="#' + tabId + '"][data-toggle="tab"]').parent().find('i');
//                // Add custom class to tab containing the field
//                if (data.status == validator.STATUS_INVALID) {
//                    $icon.removeClass('fa-check').addClass('fa-times');
//                } else if (data.status == validator.STATUS_VALID) {
//                    var isValidTab = validator.isValidContainer($tabPane);
//                    $icon.removeClass('fa-check fa-times')
//                        .addClass(isValidTab ? 'fa-check' : 'fa-times');
//                }
//            }
//        });
    };

    $ns.initMessageBox = function (activityId) {
        $(document).on('click', '.addkv-btn', function() {
            var msgBoxForm=  $(".audit-config-message-box-form");
            var key = msgBoxForm.find('input.key').val();
            var val = msgBoxForm.find('input.value').val();
            var groupId = msgBoxForm.find('select.group').val();
            var auditSuggestion = msgBoxForm.find('select.auditSuggestion').val();
            if (!key || !val) {
                bootbox.alert("[键:值]不能为空！");
                return;
            }
            var kv = {
                key: key,
                value: val,
                groupName: msgBoxForm.find(".group option:selected").text(),
                groupId: groupId || '',
                auditSuggestion:auditSuggestion,
                auditSuggestionName:msgBoxForm.find('.auditSuggestion option:selected').text()
            };
            $.tmpl($("#kvTemplate").html(), kv).appendTo(msgBoxForm.find('.kv-tbody'));
        });
        $(document).on('click', '.rmkv-btn', function (e) {
            $(e.target).parents('tr').remove();
        });
    };

    $ns.popAuditConfig = function (configType, typeName,groupId) {
        if(!groupId){
            bootbox.alert("请先选择校验项所属分组！");
            return false;
        }
        var messageInput = $.tmpl($("#messageInputTemplate").html(), {groupId:groupId}).html();
        var messageText = $.tmpl($("#messageTextTemplate").html(), {groupId:groupId}).html();
        var messageBox = $.tmpl($("#messageBoxTemplate").html(), {groupId:groupId}).html();
        var promotion = $.tmpl($("#promotionTemplate").html(), {groupId:groupId}).html();
        var message = messageInput;
        var isMustTag = false;
        if (configType == 4 || configType == 5 || configType == 6 || configType == 7 || configType == 11) {
            message = messageBox;
            isMustTag = true;

        } else if (configType == 9 || configType == 10) {
            message = messageText;
        } else if(configType == 2001){
            message = promotion;
        }
        bootbox.dialog({
                title: "添加审核校验元素",
                message: message,
                buttons: {
                    close: {
                        label: "关 闭",
                        className: "btn-success",
                        callback: function () {

                        }
                    },
                    success: {
                        label: "暂 存",
                        className: "btn-success",
                        callback: function () {
                            var activityAttrId = $("#activityAttr").val();
                            var activityAttrName = $("#activityAttr").find("option:selected").text();
                            var configName = $('#configName').val();
                            var maxLength = $('#maxLength').val();
                            var tags = [];
                            $(this).find("input[name='tag']").each(function(){
                                tags.push($(this).val());
                            });
                            var defaultValue = $('#defaultValue').val();
                            var placeHolder = $('#placeHolder').val();
                            var required = $('input[name="required"]:checked').val();
                            var sort = $('#sort').val();
                            var oldSortArr = [];
                            $("#elementListBody").find("input[name='sort']").each(function () {
                                oldSortArr.push($(this).val());
                            });
                            if (isMustTag && tags.length<1) {
                                bootbox.alert("请填写[键:值]属性！");
                                return false;
                            }
                            if (!sort) {
                                bootbox.alert("请填写排序属性！");
                                return false;
                            }
                            for (var i = 0; i < oldSortArr.length; i++) {
                                if (oldSortArr[i] == sort) {
                                    bootbox.alert("排序属性值不能重复！");
                                    return false;
                                }
                            }
                            var html = "<tr>" +
                                "<td>" + $("#configGroupNameInput").val()+buildAuditConfigHidden("groupId", groupId,true) + "</td>" +
                                "<td>" + typeName + buildAuditConfigHidden("configType", configType, true) + "</td>" +
                                "<td>" + activityAttrName + buildAuditConfigHidden("activityAttrId", (activityAttrId ? activityAttrId : ''), true) + "</td>" +
                                "<td>" + buildAuditConfigHidden("configName", (configName ? configName : '')) + "</td>" +
                                "<td>" + buildAuditConfigHidden("maxLength", (maxLength ? maxLength : '1024')) + "</td>" +
                                "<td>" + buildKVAuditConfigHidden(tags) + "</td>" +
                                "<td>" + buildAuditConfigHidden("defaultValue", (defaultValue ? defaultValue : '')) + "</td>" +
                                "<td>" + buildAuditConfigHidden("required", (required ? required : '1')) + "</td>" +
                                "<td>" + buildAuditConfigHidden("placeHolder", (placeHolder ? placeHolder : '')) + "</td>" +
                                "<td>" + buildAuditConfigHidden("sort", (sort ? sort : '0')) + "</td>" +
                                "<td><button type='button' class='btn bg-primary glyphicon glyphicon-remove' onclick='javascript:$(this).parent().parent().remove();'>删 除</button></td>" +
                                "</tr>";
                            $("#elementListBody").append(html);
                        }
                    }
                }
            }
        );
        function buildKVAuditConfigHidden(tags){
            var html = "";
            for(var i=0;i<tags.length;i++){
                var ts = tags[i].split(",");
                html = html + ts[0]+":"+ts[1];
                if(ts[2]){
                    html = html +" --> "+ ts[2];
                }
                html = html +"<br/>";
                html = html + "<input type='hidden' name='tag' value='"+tags[i]+"'>";
            }
            return html;
        }

        var $select = $('.audit-config-message-box-form select.group');
        if($select.length > 0) {
            var url = '/act/listAuditConfigGroup.json?activityId=' + $("#activityId").val();
            system.ajaxGetAsync(url, function (data) {
                if (data.result.code == '0') {
                    var groups = data.result.result;
                    $select.empty().append("<option value=''>无指向分组</option>");
                    groups.forEach(function (group) {
                        $select.append("<option value='" + group.id + "'>" + group.groupName + "</option>");
                    });
                    $select.append("<option value='0'>结束</option>");
                } else {
                    bootbox.alert('加载分组失败');
                }
            });
        }
    };

    function buildAuditConfigHidden(name, text, isTextHidden) {
        var value = (text ? " value='" + text + "'" : "");
        var html = "";
        if (!isTextHidden) {
            html = html + text;
        }
        return html + "<input type='hidden' name='" + name + "' " + value + ">";
    }

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
})(using("activity"));