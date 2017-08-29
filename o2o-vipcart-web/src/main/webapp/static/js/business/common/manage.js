/**
 * 页面操作通用处理方法的封装
 * create by liuhuiqing
 */
(function ($ns) {
    $ns.initList = function (params,extEvent) {
        params = $ns.initParams(params);
        lsp.visitToAjax(params.pageId);
        lsp.tableListStyle()
        $ns.bindListEvent(params);
        if(typeof(eval(extEvent)) == "function"){
            extEvent(params);
        }
    };
    $ns.initSearch = function (params,extEvent) {
        params = $ns.initParams(params);
        $ns.bindSearchEvent(params);
        if(typeof(eval(extEvent)) == "function"){
            extEvent(params);
        }
    };

    $ns.initParams = function (params) {
        var pageId = params;
        if(typeof(params) == 'object'){
            pageId = params.pageId;
        }
        return {
            pageId: pageId,
            dbclick:params.dbclick?true:false,
            width: params.width?params.width:1024,
            height: params.height?params.height:400
        };
    };
    $ns.bindSearchEvent = function (params) {
        var pageId = params.pageId;
        var $listPage = $("#" + pageId);
        var key = $listPage.attr("v");
        //绑定新增处理事件
        $listPage.find(".find").unbind("click").bind("click", function () {
            var $searchList = $listPage.find("#searchList");
            if ($searchList.length < 1) {
                return lsp.alert("页面" + pageId + "中没有找到列表id=searchList的元素");
            }
            var searchFormData = $listPage.find("#" + key + "SearchForm").serialize();
            lsp.ajaxPostAsync("/" + key + "/plist", searchFormData, function (data) {
                $searchList.html(data);
            });
        });
    };
    $ns.bindListEvent = function (params) {
        var pageId = params.pageId;
        var width = params.width;
        var height = params.height;
        var $listPage = $("#" + pageId);
        var key = $listPage.attr("v");//页面操作标记
        var $allCheck = $listPage.find("input[name='" + key + "AllCheckbox']");//全选按钮元素
        var checkBoxKey = "input[name='" + key + "Checkbox']";//单选按钮元素定位条件
        var $quickSearchValue = $listPage.find(".quickSearchValue");//快速查询输入元素
        var $quickSearchButton = $listPage.find(".quickSearchButton");
        //绑定全选按钮事件
        $allCheck.unbind("click").bind("click", function () {
            var $checkBox = $listPage.find(checkBoxKey);
            if ($(this).attr("checked") == true) {
                $checkBox.attr("checked", true);
            } else {
                $checkBox.attr("checked", false);
            }
        });

        //绑定按钮快速查询事件
        $quickSearchButton.unbind("click").bind("click", function(){quickSearch()});

        //绑定键盘快速查询事件
        $quickSearchValue.keydown(function (e) {
            var keyCode = e.keyCode;
            //回车事件触发快速查询
            if (keyCode == 13) {
                if ($quickSearchButton.length > 0) {
                    $quickSearchButton.click();
                } else {
                    quickSearch();
                }
            } else if (keyCode == 27) {//ESC事件触发表单查询
                var $find = $(".find");
                if($find.length > 0){
                    $(".find").click();
                }else{
                    lsp.ajaxPostAsync("/" + key + "/plist", null, function (data) {
                        $listPage.parent().html(data);
                    });
                }
            }
        });

        //绑定批量删除事件
        $listPage.find(".deleteAll").unbind("click").bind("click", function () {
            var ids = "";
            var count = 0;
            var query = $(this).attr("q");
            query = query?"?"+query:"";
            $listPage.find(checkBoxKey + ":checked").each(function () {
                if (count == 0) {
                    ids = ids + $(this).val();
                } else {
                    ids = ids + "," + $(this).val();
                }
                count = count + 1;
            });
            if (!ids) {
                return lsp.alert("请选择要操作的记录！");
            }
            lsp.confirm("确定要进行批量删除操作?", function () {
                lsp.ajaxPostAsync("/" + key + "/delete/" + ids + "?format=json", null, function (data) {
                    $ns.parseResult("/" + key + "/plist"+query, null, data);
                });
            });
        });

        //绑定新增处理事件
        $listPage.find(".add").unbind("click").bind("click", function () {
            var param = $(this).attr("v");
            if(!param){
                param = "";
            }
            lsp.openUrlByObject({id: key, title: "新 增", content: "/" + key + "/add"+param, callback: function (data) {
                $ns.saveOrUpdate(key, data, "/save.json");
            }, width: width, height: height});
        });

        //绑定修改处理事件
        $listPage.find(".edit").unbind("click").bind("click", function () {
            var editId = $(this).attr("v");
            if (!editId) {
                return lsp.alert("请选择要操作的记录！");
            }
            lsp.openUrlByObject({id: key, title: "修 改", content: "/" + key + "/edit/" + editId, callback: function (data) {
                $ns.saveOrUpdate(key, data, "/update.json");
            }, width: width, height: height});
        });

        //绑定记录双击事件触发修改操作
        $listPage.find(".list tr:gt(0)").find("td:gt(0)").unbind("dblclick").bind("dblclick", function () {
            if(!params.dbclick){
                return false;
            }
            var editId = $(this).parent().find(checkBoxKey).val();
            if (editId) {
                lsp.openUrlByObject({id: key, title: "修 改", content: "/" + key + "/edit/" + editId, callback: function (data) {
                    $ns.saveOrUpdate(key, data, "/update.json");
                }, width: width, height: height});
            }
        }).bind("click", function () {//由于绑定了隔行变色事件，此处不再调用解绑方法
            var $checkBox = $(this).parent().find(checkBoxKey)
            if ($checkBox.length > 0) {
                $checkBox.attr("checked", !$checkBox.attr("checked"));
            }
        });

        //绑定删除处理事件
        $listPage.find(".delete").unbind("click").bind("click", function () {
            var deleteId = $(this).attr("v");
            var query = $(this).attr("q");
            query = query?"?"+query:"";
            if (!deleteId) {
                return lsp.alert("请选择要操作的记录！");
            }
            lsp.confirm("确定要进行删除操作?", function () {
                lsp.ajaxPostAsync("/" + key + "/delete/" + deleteId + "?format=json", null, function (data) {
                    $ns.parseResult("/" + key + "/plist"+query, null, data);
                });
            });
        });

        //绑定启用禁用处理事件
        $listPage.find(".start_stop").unbind("click").bind("click", function () {
            var startStop = $(this);
            var startStopId = startStop.attr("v");
            if (!startStopId) {
                return lsp.alert("请选择要操作的记录！");
            }
            var yn = startStop.hasClass("stop")?0:1;
            lsp.confirm("确定要进行该操作?", function () {
                lsp.ajaxPostAsync("/" + key + "/update.json?id=" + startStopId + "&yn="+yn, null, function (data) {
                    if(data.result.success){
                        if(yn==0){
                            startStop.removeClass("stop").addClass("start").val("已启用");
                        }else{
                            startStop.removeClass("start").addClass("stop").val("已禁用");
                        }
                    }
                    lsp.alert(data.result.msg);
                });
            });
        });

        //绑定下载处理事件
        $listPage.find(".download").unbind("click").bind("click", function () {
            var downloadId = $(this).attr("v");
            if (!downloadId) {
                return lsp.alert("请选择要操作的记录！");
            }
            lsp.confirm("确定要进行下载操作?", function () {
                window.location.href = "/" + key + "/download/"+downloadId;
            });
        });

        //快速查询
        function quickSearch() {
            //查询属性优先选用列表项
            var quickSearchKey = $listPage.find(".quickSearchKey").find("option:selected").val();
            if (!quickSearchKey) {
                //其次选用输入框扩展属性配置，支持指定多个属性（属性之间用空格或逗号隔开即可）
                quickSearchKey = $quickSearchValue.attr("quickSearchKey");
                if (!quickSearchKey) {
                    return lsp.alert("没有绑定要查询的字段");
                }
            }
            var reg = /([^\s])\s+([^\s\b])/g;
            var params = quickSearchKey.replace(reg, "$1,$2").split(",");
            var formData = "";
            var value = $quickSearchValue.val().replace(/[ ]/g, "");
            for (var i = 0; i < params.length; i++) {
                if (i > 0) {
                    formData = formData + "&";
                }
                formData = formData + params[i] + "=" + value;
            }
            lsp.ajaxPostAsync("/" + key + "/plist", formData, function (data) {
                var $searchList = $("#searchList");
                if($searchList.length > 0){
                    $searchList.html(data);
                }else{
                    $searchList = $(data);
                    $listPage.parent().html($searchList);
                }
                // 重新赋值快速查询条件
                var $quickSearchValue = $searchList.find(".quickSearchValue");
                $quickSearchValue.val(value);
                var $quickSearchKey =$searchList.find(".quickSearchKey");
                if($quickSearchKey.length > 0){
                    $quickSearchKey.find("option[value='"+quickSearchKey+"']").attr("selected",true);
                }else{
                    $quickSearchValue.attr("quickSearchKey",quickSearchKey);
                }
            });
        }
    };

    /**
     * 保存或更新表单信息
     * @param key 当前页面操作表关键字
     * @param popDoc 弹出页面元素对象
     * @param actionUrl 相对路径（对应requestMapping取值）
     */
    $ns.saveOrUpdate = function (key, popDoc, actionUrl) {
        var $form = $(popDoc).find("form");
        if ($form.length < 1) {
            return lsp.unLoading(popDoc);
        }
        if (checksubmit($form[0])) {
            lsp.ajaxPostAsync("/" + key + actionUrl, $form.serialize(), function (result) {
                $ns.parseResult("/" + key + "/plist", popDoc, result);
            });
        }
    };
    /**
     * 服务返回结果解析方法 当operation_table的上相邻元素的id=search时，为异步刷新页面
     * @param callBackUrl服务url（对应conrtroller服务的根url+requestMapping的Url）
     * @param popDoc 弹出页面元素对象
     * @param serviceResponse 返回的ServiceResponse结果对象
     */
    $ns.parseResult = function (callBackUrl, popDoc, serviceResponse) {
        //检查错误信息
        var errMsg = $ns.errorMsg(serviceResponse,popDoc);
        if(errMsg){
            return lsp.alert(errMsg);
        }
        var response = serviceResponse.result;
        if (response.code == "0") {
            var msg = response.result > 0 ? response.msg : "操作失败";
            if(popDoc){
                lsp.unLoading(popDoc);
            }
            lsp.alert(msg, function () {
                if(!callBackUrl){
                    return;
                }
                var $searchList = $("#searchList");
                if ($searchList.length > 0) {
                    var $search = $searchList.parent();
                    var $form = $search.find("form");
                    var formData = null;
                    if ($form.length > 0) {
                        formData = $form.serialize();
                    }
                    lsp.ajaxPostAsync(callBackUrl, formData, function (data) {
                        $searchList.html(data);
                    });
                } else {
                    window.document.location.href = callBackUrl;
                }
            });
        } else {
            lsp.alert(response.msg);
        }
    };

    /**
     * 错误信息处理
     * @param serviceResponse
     * @param pageId
     * @returns {string}
     */
    $ns.errorMsg = function (serviceResponse,pageId) {
        //检查错误信息
        var errors = (serviceResponse?serviceResponse.errors:"");
        var errMsg = "";
        if(errors){
            var $pageId;
            if(!pageId){
                $pageId = $(document);
            }else{
                $pageId = lsp.paramJquery(pageId);
                if($pageId.length < 1){
                    $pageId = $(document);
                }
            }
            var len = errors.length;
            for(var i=0;i<len;i++){
                var name = $pageId.find("#"+errors[i].field).parent().prev().text();
                if(name){
                    errMsg = errMsg + name+ ":";
                }
                errMsg = errMsg + errors[i].defaultMessage+"\n\r";
            }
        }
        return errMsg;
    };


    $ns.formatJson = function(txt,compress/*是否为压缩模式*/){/* 格式化JSON源码(对象转换为JSON文本) */
        var indentChar = '    ';
        if(/^\s*$/.test(txt)){
            alert('数据为空,无法格式化! ');
            return;
        }
        try{var data=eval('('+txt+')');}
        catch(e){
            alert('数据源语法错误,格式化失败! 错误信息: '+e.description,'err');
            return;
        };
        var draw=[],last=false,This=this,line=compress?'':'\n',nodeCount=0,maxDepth=0;

        var notify=function(name,value,isLast,indent/*缩进*/,formObj){
            nodeCount++;/*节点计数*/
            for (var i=0,tab='';i<indent;i++ )tab+=indentChar;/* 缩进HTML */
            tab=compress?'':tab;/*压缩模式忽略缩进*/
            maxDepth=++indent;/*缩进递增并记录*/
            if(value&&value.constructor==Array){/*处理数组*/
                draw.push(tab+(formObj?('"'+name+'":'):'')+'['+line);/*缩进'[' 然后换行*/
                for (var i=0;i<value.length;i++)
                    notify(i,value[i],i==value.length-1,indent,false);
                draw.push(tab+']'+(isLast?line:(','+line)));/*缩进']'换行,若非尾元素则添加逗号*/
            }else   if(value&&typeof value=='object'){/*处理对象*/
                draw.push(tab+(formObj?('"'+name+'":'):'')+'{'+line);/*缩进'{' 然后换行*/
                var len=0,i=0;
                for(var key in value)len++;
                for(var key in value)notify(key,value[key],++i==len,indent,true);
                draw.push(tab+'}'+(isLast?line:(','+line)));/*缩进'}'换行,若非尾元素则添加逗号*/
            }else{
                if(typeof value=='string')value='"'+value+'"';
                draw.push(tab+(formObj?('"'+name+'":'):'')+value+(isLast?'':',')+line);
            };
        };
        var isLast=true,indent=0;
        notify('',data,isLast,indent,false);
        return draw.join('');
    }

})(using("handler"));