import Vue from 'vue';
import czcpush from '../utils/czcpush';

Vue.directive('search',{
    bind: function(el, binding) {
        $(this.el).dropdown({
					message: {
						noResults: '未搜索到结果'
					},
					onChange: (value, text, selected) => {
            var item = {
              value: value,
              lon: $(selected).attr("data-lon"),
              lat: $(selected).attr("data-lat")
            };
						if(this.arg === 'bind' && this.expression) {
							this.vm.$set(this.expression, value);
							this.vm.$emit('dropdown.change', item);
						}
					}
				});
    },
  	update: function(value) {
  		$(this.el).dropdown('set selected', value);
  	}
});
