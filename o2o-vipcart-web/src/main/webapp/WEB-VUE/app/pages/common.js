export function showModel(model) {
  var $model = $(model.el);
  $model.find(".remove").unbind().click(function(){
    close();
  });
  $model.modal({
    blurring: true,
    transition: 'horizontal flip',
    closable  : false,
    onDeny    : function() {
      //ok
      model.cancelCallback();
      close();
      return false;
    },
    onApprove : function() {
      //cancel
      if(model.okCallback()) {
        close();
      }
      return false;
    }
  }).modal('show');
  var close = function() {
    $model.modal("hide");
  };
}
//显示长度信息
export function contentLength(selector) {
  var $text = $(selector);
  var maxlength = $text.attr("maxlength") || 300;
  var html = '<div class="floating ui label length hide"></div>';
  var $length = $(html);
  $length.html(getLength());
  $text.focus(function(){
    $length.removeClass("hide");
  });
  $text.blur(function(){
    $length.addClass("hide");

  });
  $text.bind('input propertychange', function() {
    $length.html(getLength());
      var pattern = new RegExp("[`~!@%#$^&*()=|{}':;',　\\[\\]<>/? ／＼］\\.；：%……+ ￥（）【】‘”“'．。，、？１２３４５６７８９０－＝＿＋～？！＠＃＄％＾＆＊）]");
      if(pattern.test($text.val())){
          $('#error_info').css('padding-left','22%').show();
      }else {
          $('#error_info').hide();
      }
  });
  $text.after($length);
  function getLength() {
    var len = maxlength - $text.val().length;
    return len >= 0 ? len : 0;
  }

}
export function number(event) {
  var e = event || window.event || arguments.callee.caller.arguments[0];
  if((e.keyCode < 48 || e.keyCode > 57) && (e.keyCode < 96 || e.keyCode > 105) && e.keyCode != 8) {
    e.returnValue = false;
  }
}
