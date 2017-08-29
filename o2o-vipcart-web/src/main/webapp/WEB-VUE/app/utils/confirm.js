const template = `
<div class="ui modal small confirm">
  <div class="header">提示</div>
  <div class="content">

  </div>
  <div class="actions">
    <div class="ui cancel red inverted button"><i class="remove icon"></i> 取消</div>
    <div class="ui ok green inverted button"><i class="checkmark icon"></i> 确定</div>
  </div>
</div>`;

export default function confirm(content, callback) {
    var $template = $(template);
    $template.find(".content").text(content);
    $(document.body).append($template);
    $(".confirm").modal('setting', 'closable', false).modal('show');
    $(".confirm .cancel").unbind().click(function(){
        var $confirm = $(this).parent().parent();
        $confirm.modal('hide');
        setTimeout(() => {$confirm.remove();}, 300);
    });
    $(".confirm .ok").unbind().click(function(){
        var $confirm = $(this).parent().parent();
        $confirm.modal('hide');
        setTimeout(() => {$confirm.remove();callback && callback();}, 300);
    });
}
