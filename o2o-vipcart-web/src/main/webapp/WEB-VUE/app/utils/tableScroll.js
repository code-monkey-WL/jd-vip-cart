export default function tableScroll(selector) {
  var $table = $(selector);
  var $copy = $table.clone();
  $copy.removeAttr("id");
  $copy.removeAttr("name");
  var $div = $("<div></div>");
  var height = $table.find("thead").height() + 1;
  $div.css({
    'width': $table.parent().width(),
    'height': height,
    'overflow': 'hidden',
    'position': "fixed",
    'margin-top': -height
  });
  $table.css({
    'position': "absolute",
    'top': -height
  });
  $table.parent().css("margin-top", height);
  $($table.parent().parent()).css({
    'transform': 'translateZ(0)',
    '-webkit-transform': 'translateZ(0)'/*重点样式*/
  });
  $copy.addClass("table_scroll");
  $table.parent().find(".table_scroll").parent().remove();
  $div.append($copy);
  $table.before($div);
}
