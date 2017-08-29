/**
 * 内容管理维护
 * create by liuhuiqing
 */
(function ($ns) {

    $ns.initList = function (pageId) {
        system.visitToAjax(pageId);
    };

    $ns.visitQueryFormSubmit = function () {
        var formData = $('#visitQueryForm').serialize();
        system.ajaxPostAsync("/task/visit/flow/list", formData, function (result) {
            $("#visitListDiv").html(result);
        });
    };

    $ns.visitManage= function () {
        $(".detail").off().on("click",function(){
            var id="detailLoading";
            system.loading(id);
            $.ajax({
                type: "GET",
                url: $(this).attr("v"),
                success: function (response) {
                    bootbox.dialog({
                            title: "任务详情",
                            message: response,
                            buttons: {
                                close: {
                                    label: "关 闭",
                                    className: "btn-success",
                                    callback: function () {

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

    $ns.visitSearchManage = function () {
        $("#visitQueryButton").off().on("click",function(){
            $ns.visitQueryFormSubmit();
        });
    };

    /**
     * 格式化JSON源码(对象转换为JSON文本)
     * @param txt
     * @param compress 是否为压缩模式
     * @returns {string}
     */
    $ns.formatJson = function(txt,compress){
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
})(using("flow"));