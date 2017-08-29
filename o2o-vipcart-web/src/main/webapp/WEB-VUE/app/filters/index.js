import numeral from 'numeral';
import Vue from 'vue';

Vue.filter('numeral', function (value, format) {
    return numeral(value).format(format);
});

Vue.filter('wrap', function (value, replace) {
    let list = value.split(replace);
    let str = list.join(replace + "<br>");
    return str;
});

Vue.filter('enum', function (value, type) {
    if(type == "vipcartType") {
        switch (value) {
            case 1:
                return '满减券';
            case 2:
                return '立减券';
            case 3:
                return '免运费券';
            case 4:
                return '免N元运费券';
            case 5:
                return '满折券';
        }
    } else if(type == 'showName') {
        switch (value.vipcartType) {
            case 1:
                return '满 '+value.minOrderAmount/100+' 减 '+value.quota/100+' 元';
            case 2:
                return '立减 '+value.quota/100+' 元';
            case 3:
                return '满 '+value.minOrderAmount/100+' 全免';
            case 4:
                return '满 '+value.minOrderAmount/100+' 免 '+value.quota/100+' 元';
            case 5:
                return '满 '+value.minOrderAmount/100+' 元享 '+value.quota/100+' 折,最多抵扣 '+value.maxOrderAmount/100+' 元';
        }
    } else if(type == 'vipcartState') {
        switch (value) {
            case 1:
                return '新建';
            case 2:
                return '待确认';
            case 3:
                return '已生效';
            case 4:
                return '已锁定';
            case 5:
                return '已领完';
            case 6:
                return '已驳回';
            case 7:
                return '已结束';
            case 8:
                return '已取消';
            case 9:
                return '未开始';
            case 10:
                return '进行中';
        }
    }
    else if(type == 'activityState') {
        switch (value) {
            case 1:
                return '新建';
            case 2:
                return '待确认';
            case 3:
                return '已生效';
            case 4:
                return '已锁定';
            case 5:
                return '已取消';
            case 7:
                return '已结束';
            case 8:
                return '进行中';
            case 9:
                return '未开始';
        }
    }
    else if(type == 'state') {
        switch (value) {
            case 1:
                return '未使用';
            case 2:
                return '已使用';
            case 3:
                return '已作废';
        }
    }
    else if(type == 'pushState') {
        switch (value) {
            case 0:
                return '未推送';
            case 1:
                return '已推送';
        }
    }
    else if(type == 'riskState') {
        switch (value) {
            case 0:
                return '未校验';
            case 1:
                return '校验中';
            case 2:
                return '校验结束';
        }
    }
    else if(type == 'limitTypeShow') {
        switch (value) {
            case "200":
                return '限商家/门店';
            case "220":
                return '限行业';
            case "270":
                return '限品牌';
            case "240":
                return '限品类';
            case "250":
                return '限单品';
        }
    }
    else if(type == 'activityType') {
        switch (value) {
            case 1:
                return '普通活动';
            case 2:
                return '红包裂变';
            case 3:
                return '兑换活动';
            case 5:
                return '地推活动';
            case 6:
                return '下单返券';
            case 8:
                return '定向推送';
        }
    }
    return value;
});


Vue.filter('showLocation', function (value) {
    if(!value) {
        return '';
    }
    let list = value.split(',');
    return list.map((item) => {
            switch(item) {
    case '101':
        return '门店页';
    case '201':
        return '领券中心';
    case '401':
        return '活动页';
    default:
        return item;
    }
}).join(',');
});
