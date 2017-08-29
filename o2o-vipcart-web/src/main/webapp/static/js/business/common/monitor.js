/**
 * 监控报表
 * create by liuhuiqing
 */
(function ($ns) {

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

    $ns.drawCharts = function (title,subtitle,xAxis_categories,yAxis_title,plotLines_value,tooltip,series){
        var id = title+subtitle;
        var container = $("#"+id);
        if(container.length==0){
            container =  $("<div id=\""+id+"\" class=\"charts\" style=\"min-width:700px;height:400px\"></div>");
            $(".body").append(container);
        }
        container.highcharts({
            title: {
                text: title,
                x: -20 //center
            },
            subtitle: {
                text: subtitle,
                x: -20
            },
            xAxis: {
                categories: xAxis_categories
            },
            yAxis: {
                title: {
                    text: yAxis_title
                },
                plotLines: [
                    {
                        value: plotLines_value,
                        width: 1,
                        color: '#808080'
                    }
                ]
            },
            tooltip: {
                valueSuffix: tooltip
            },
            legend: {
                layout: 'vertical',
                align: 'right',
                verticalAlign: 'middle',
                borderWidth: 0
            },
            series: series
        });
    };

    $ns.removeCharts = function (title,subtitle){
        var id = title+subtitle;
        $("#"+id).remove();
    };

    $ns.charts = function (ids){
        var head = $("#head");
        lsp.ajaxPostAsync("/rpcMethod/monitor.json",$("#monitorForm").serialize(),function(result){
            var data = result.result.result;
            if(!data){
                head.append("未获得到监控数据");
                return;
            }
            var msg = data.split("///");
            var isError = true;
            var menus = [];
            for(var i=0;i<msg.length;i++){
                // 切面名称和切面对应属性对象以://隔开的
                var props = msg[i].replace(new RegExp(/\\"/g),'"').split(":::");
                if(!props || props.length!=2){
                    continue;
                }
                isError = false;
                var title = props[0];
                var prop = eval('('+props[1]+')');
                if(!props || prop.length==0){
                    continue;
                }
                var xAxis_categories=[];
                var myObjs = [];
                var apps = [];
                var label = $("<label for=\""+title+"\"> "+title+"&nbsp;&nbsp;&nbsp;&nbsp;</label>");
                var menu = $("<input type=\"checkbox\" id="+title+" value="+title+" />");
                menus.push(menu);
                head.append(menu).append(label);
                // 分析切面属性对象数组
                for(var j=0;j<prop.length;j++){
                    // 全量提取X时间轴
                    xAxis_categories = $ns.addSet(prop[j].dateTime,xAxis_categories);
                    apps = $ns.addSet(prop[j].appCode,apps);
                    var methodName = prop[j].methodName;
                    var isNew = true;
                    // 组织X轴时间，Y轴value值，调用来源等多维度的实体对象
                    for(t in myObjs){
                        if(myObjs[t].name == methodName){
                            myObjs[t].data.push({dateTime:prop[j].dateTime,value:prop[j].value,appCode:prop[j].appCode});
                            isNew = false;
                            break;
                        }
                    }
                    if(isNew){
                        var data = [];
                        data.push({dateTime:prop[j].dateTime,value:prop[j].value,appCode:prop[j].appCode});
                        myObjs.push({name:methodName,data:data});
                    }
                }
                // 将X轴升序排列
                xAxis_categories.sort(function(a,b){return a>b?1:-1});
                var appSeries = [];
                // 多应用
                for(var p=0;p<apps.length;p++){
                    var series = [];
                    // 多方法
                    for(t in myObjs){
                        var newDateValue = [];
                        // X轴单位
                        for(var k=0;k<xAxis_categories.length;k++){
                            var isHave = false;
                            for(temp in myObjs[t].data){
                                var data = myObjs[t].data[temp];
                                if(xAxis_categories[k]==data.dateTime && apps[p]==data.appCode){
                                    newDateValue.push(data.value);
                                    isHave = true;
                                    break;
                                }
                            }
                            // 补全Y轴数据
                            if(!isHave){
                                newDateValue.push(0);
                            }
                        }
                        series.push({name:myObjs[t].name,data:newDateValue});
                    }
                    appSeries.push(series);
                }
                menu.data("title",title);
                menu.data("apps",apps);
                menu.data("xAxis_categories",xAxis_categories);
                menu.data("appSeries",appSeries);
                // 绑定交互事件
                menu.click(function(){
                    var self = $(this);
                    var isChecked = (self.attr("checked")==true);
                    // 按照调用来源生成多组报表
                    draw(self,isChecked);
                });
            }
            if(isError){
                head.append("监控数据格式不正确！");
            }
            if(menus.length < 1){
                head.append("暂无监控数据！");
                return;
            }
            var isInit = false;
            if(ids && ids.length > 0){
                for(var id in ids){
                    for(var menu in menus){
                        if(ids[id] == menus[menu].attr("id")){
                            var isChecked = true;
                            var self = menus[menu].attr("checked",isChecked);
                            draw(self,isChecked);
                            isInit = true;
                            break;
                        }
                    }
                }
            }
            if(!isInit){
                var isChecked = true;
                var self = menus[0].attr("checked",isChecked);
                draw(self,isChecked);
            }
        });
    };

    /**
     * 刷新报表
     */
    $ns.refresh = function (){
        var ids = [];
        $("#head").find(":checked").each(function(){
            ids.push($(this).attr("id"));
        });
        $("#head").empty();
        $(".charts").remove();
        $ns.charts(ids);
    }

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
        };
    };

    /**
     * 根据应用显示报表信息
     * @param self
     * @param isChecked
     */
    function draw(self,isChecked){
        for(var q=0;q<self.data("apps").length;q++){
            if (isChecked) {
                $ns.drawCharts(self.data("title"),self.data("apps")[q],self.data("xAxis_categories"),"调用次数","1","次",self.data("appSeries")[q]);
            }else{
                $ns.removeCharts(self.data("title"),self.data("apps")[q]);
            }
        }
    }

})(using("monitor"));