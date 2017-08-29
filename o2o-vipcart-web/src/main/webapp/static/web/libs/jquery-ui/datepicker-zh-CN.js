
( function( factory ) {
	if ( typeof define === "function" && define.amd ) {
		define( [ "../widgets/datepicker" ], factory );
	} else {

		// Browser globals
		factory( jQuery.datepicker );
	}
}( function( datepicker ) {

function getWeek(date) {
    var year = date.getFullYear(),
        month = date.getMonth(),
        days = date.getDate();
    for (var i = 0; i < month; i++) {
        days += getMonthDays(year, i);
    }
    var yearFirstDay = new Date(year, 0, 1).getDay() || 7;
    var week = Math.ceil((days - (7-yearFirstDay)) / 7);
    return week;
}

function isLeapYear(year) {
    return (year % 400 == 0) || (year % 4 == 0 && year % 100 != 0);
}

function getMonthDays(year, month) {
    return [31, null, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31][month] || (isLeapYear(year) ? 29 : 28);
}

datepicker.regional[ "zh-CN" ] = {
	closeText: "关闭",
	prevText: "&#x3C;上月",
	nextText: "下月&#x3E;",
	currentText: "今天",
	monthNames: [ "一月","二月","三月","四月","五月","六月",
	"七月","八月","九月","十月","十一月","十二月" ],
	monthNamesShort: [ "一月","二月","三月","四月","五月","六月",
	"七月","八月","九月","十月","十一月","十二月" ],
	dayNames: [ "星期日","星期一","星期二","星期三","星期四","星期五","星期六" ],
	dayNamesShort: [ "周日","周一","周二","周三","周四","周五","周六" ],
	dayNamesMin: [ "日","一","二","三","四","五","六" ],
	weekHeader: "周",
	dateFormat: "yy/mm/dd",
	firstDay: 0,
	isRTL: false,
	showMonthAfterYear: true,
	yearSuffix: "年",
	calculateWeek: function(date) {
		return getWeek(date);
	}
};
datepicker.setDefaults( datepicker.regional[ "zh-CN" ] );

return datepicker.regional[ "zh-CN" ];

} ) );
