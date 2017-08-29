/**
 * 审核维护
 * create by liuhuiqing
 */
(function ($ns) {

    $ns.initList = function (pageId) {
        system.visitToAjax(pageId);
        system.tableListStyle()
    };

    $ns.auditQueryFormSubmit = function () {
        var formData = $('#auditQueryForm').serialize();
        system.ajaxPostAsync("/audit/list",formData,function(result){
            $("#auditListDiv").html(result);
        });
    };

    $ns.auditQueryFormToolsSubmit = function () {
        var formData = $('#auditQueryForm').serialize();
        system.ajaxPostAsync("/audit/toolsList",formData,function(result){
            $("#auditListDiv").html(result);
        });
    };

    $ns.cacheQueryFormSubmit = function () {
        var formData = $('#cacheQueryForm').serialize();
        system.ajaxPostAsync("/util/cacheSearch",formData,function(result){
            if(result.result != null){
                $("#findCacheValue").html("CacheResult:"+result.result);
            }else{
                $("#findCacheValue").html("NoThisCacheKey");
            }
        });
    };

    $ns.editCacheFormSubmit = function () {
        var formData = $('#editCacheForm').serialize();
        system.ajaxPostAsync("/util/editCache",formData,function(result){
            bootbox.alert(result.msg);
        });
    };

    $ns.removeCacheFormSubmit = function () {
        var formData = $('#removeCacheForm').serialize();
        system.ajaxPostAsync("/util/removeCache",formData,function(result){
            //alert(JSON.stringify(result));
            bootbox.alert(result.msg);
        });
    };

    $ns.auditManage = function () {
        $(".manage").each(function(){
            var url = $(this).attr("v");
            if(url){
                $(this).click(function(){
                    $(this).attr("href", url);
                    $(this).attr("target", "_blank");
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
    $ns.addSet = function (element,arr) {
        if(!arr){
            arr = [];
            arr.push(element);
            return arr;
        }
        var isHave = false;
        for(t in arr){
            if(arr[t]==element){
                isHave = true;
                break
            }
        }
        if(!isHave){
            arr.push(element);
        }
        return arr;
    };

    /**
     * 组织冒泡事件
     * @param parent
     * @param child
     */
    $ns.stopPropagation = function(parent,child){
        var p = $(parent);
        var c = $(child);
        p.click(function(){
            c.attr("checked", (c.attr("checked")==true) ? false : true);
        });
        c.click(function(e){
            stopBubble(e);
        });
        function stopBubble(e){
            if(e){
                e.stopPropagation();
            }else{
                window.event.cancelBubble = true;
            }
        }
    };
})(using("audit"));