const template = `
<div class="ui modal small alert">
  <div class="header">提示</div>
  <div class="content">

  </div>
  <div class="actions">
    <div class="ui cancel button">确定</div>
  </div>
</div>`;

export default function alert(content, callback) {
    var $template = $(template);
    $template.find(".content").text(content);
    $(document.body).append($template);
    $(".alert").modal('setting', 'closable', false).modal('show');
    $(".alert .button").unbind().click(function(){
        var $alert = $(this).parent().parent();
        $alert.modal('hide');

        setTimeout(() => {$alert.remove();callback && callback();}, 300);
    });
}
