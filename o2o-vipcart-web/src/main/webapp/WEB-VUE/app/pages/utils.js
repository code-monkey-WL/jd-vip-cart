import * as calendar from '../utils/calendar';
import store from '../store';

export function fillCommonQuery(data) {
		 data.cityId = store.state.cityId;
}

export function initPagination(onPage) {
	this.$on('pagination.click', (action, value) => {
		if(action === 'first') {
			this.nextPageNo = 1;
		} else if(action === 'last') {
			this.nextPageNo = this.page.totalPage;
		} else if(action === 'pre') {
			this.nextPageNo = this.page.pageNo - 1;
		} else if(action === 'next') {
			this.nextPageNo = this.page.pageNo + 1;
		} else if(value) {
			this.nextPageNo = value;
		}
		onPage();
	});
}

let blur = {
	onBlur: false
}
export function form($form, config) {
	var settings = {
		regExp: {
	    bracket : /\[(.*)\]/i,
	    decimal : /^\d*(\.)\d+/,
	    email   : /^[a-z0-9!#$%&'*+\/=?^_`{|}~.-]+@[a-z0-9]([a-z0-9-]*[a-z0-9])?(\.[a-z0-9]([a-z0-9-]*[a-z0-9])?)*$/i,
	    escape  : /[\-\[\]\/\{\}\(\)\*\+\?\.\\\^\$\|]/g,
	    flags   : /^\/(.*)\/(.*)?/,
	    integer : /^\-?\d+$/,
	    number  : /^\-?\d*(\.\d+)?$/,
	    url     : /(https?:\/\/(?:www\.|(?!www))[^\s\.]+\.[^\s]{2,}|www\.[^\s]+\.[^\s]{2,})/i
	  },
		is: {
			bracketedRule: function(rule) {
				return (rule.type && rule.type.match(settings.regExp.bracket));
			}
		},
		get: {
			ancillaryValue: function(rule) {
				if(!rule.type || (!rule.value && !settings.is.bracketedRule(rule))) {
					return false;
				}
				return (rule.value !== undefined)
					? rule.value
					: rule.type.match(settings.regExp.bracket)[1] + ''
				;
			},
			ruleName: function(rule) {
				if( settings.is.bracketedRule(rule) ) {
					return rule.type.replace(rule.type.match(settings.regExp.bracket)[0], '');
				}
				return rule.type;
			}
		},
		rules: {

	    // is not empty or blank string
	    empty: function(value) {
	      return !(value === undefined || '' === value || $.isArray(value) && value.length === 0);
	    },

	    // checkbox checked
	    checked: function() {
				// let $els = $form.find("[name='" + $(this).attr("name") + "']:checked");
	      return ($form.find("[name='" + $(this).attr("name") + "']:checked").length > 0);
	    },

	    // is most likely an email
	    email: function(value){
	      return settings.regExp.email.test(value);
	    },

	    // value is most likely url
	    url: function(value) {
	      return settings.regExp.url.test(value);
	    },

	    // matches specified regExp
	    regExp: function(value, regExp) {
	      if(regExp instanceof RegExp) {
	        return value.match(regExp);
	      }
	      var
	        regExpParts = regExp.match(settings.regExp.flags),
	        flags
	      ;
	      // regular expression specified as /baz/gi (flags)
	      if(regExpParts) {
	        regExp = (regExpParts.length >= 2)
	          ? regExpParts[1]
	          : regExp
	        ;
	        flags = (regExpParts.length >= 3)
	          ? regExpParts[2]
	          : ''
	        ;
	      }
	      return value.match( new RegExp(regExp, flags) );
	    },

	    // is valid integer or matches range
	    integer: function(value, range) {
	      var
	        intRegExp = settings.regExp.integer,
	        min,
	        max,
	        parts
	      ;
	      if( !range || ['', '..'].indexOf(range) !== -1) {
	        // do nothing
	      }
	      else if(range.indexOf('..') == -1) {
	        if(intRegExp.test(range)) {
	          min = max = range - 0;
	        }
	      }
	      else {
	        parts = range.split('..', 2);
	        if(intRegExp.test(parts[0])) {
	          min = parts[0] - 0;
	        }
	        if(intRegExp.test(parts[1])) {
	          max = parts[1] - 0;
	        }
	      }
	      return (
	        intRegExp.test(value) &&
	        (min === undefined || value >= min) &&
	        (max === undefined || value <= max)
	      );
	    },

	    // is valid number (with decimal)
	    decimal: function(value) {
	      return settings.regExp.decimal.test(value);
	    },

	    // is valid number
	    number: function(value, range) {
	      var
	        numberRegExp = settings.regExp.number,
	        min,
	        max,
	        parts
	      ;
	      if( !range || ['', '..'].indexOf(range) !== -1) {
	        // do nothing
	      }
	      else if(range.indexOf('..') == -1) {
	        if(numberRegExp.test(range)) {
	          min = max = range - 0;
	        }
	      }
	      else {
	        parts = range.split('..', 2);
	        if(numberRegExp.test(parts[0])) {
	          min = parts[0] - 0;
	        }
	        if(numberRegExp.test(parts[1])) {
	          max = parts[1] - 0;
	        }
	      }
	      return (
	        numberRegExp.test(value) &&
	        (min === undefined || value >= min) &&
	        (max === undefined || value <= max)
	      );
	    },

	    // is value (case insensitive)
	    is: function(value, text) {
	      text = (typeof text == 'string')
	        ? text.toLowerCase()
	        : text
	      ;
	      value = (typeof value == 'string')
	        ? value.toLowerCase()
	        : value
	      ;
	      return (value == text);
	    },

	    // is value
	    isExactly: function(value, text) {
	      return (value == text);
	    },

	    // value is not another value (case insensitive)
	    not: function(value, notValue) {
	      value = (typeof value == 'string')
	        ? value.toLowerCase()
	        : value
	      ;
	      notValue = (typeof notValue == 'string')
	        ? notValue.toLowerCase()
	        : notValue
	      ;
	      return (value != notValue);
	    },

	    // value is not another value (case sensitive)
	    notExactly: function(value, notValue) {
	      return (value != notValue);
	    },

	    // value contains text (insensitive)
	    contains: function(value, text) {
	      // escape regex characters
	      text = text.replace(settings.regExp.escape, "\\$&");
	      return (value.search( new RegExp(text, 'i') ) !== -1);
	    },

	    // value contains text (case sensitive)
	    containsExactly: function(value, text) {
	      // escape regex characters
	      text = text.replace(settings.regExp.escape, "\\$&");
	      return (value.search( new RegExp(text) ) !== -1);
	    },

	    // value contains text (insensitive)
	    doesntContain: function(value, text) {
	      // escape regex characters
	      text = text.replace(settings.regExp.escape, "\\$&");
	      return (value.search( new RegExp(text, 'i') ) === -1);
	    },

	    // value contains text (case sensitive)
	    doesntContainExactly: function(value, text) {
	      // escape regex characters
	      text = text.replace(settings.regExp.escape, "\\$&");
	      return (value.search( new RegExp(text) ) === -1);
	    },

	    // is at least string length
	    minLength: function(value, requiredLength) {
	      return (value !== undefined)
	        ? (value.length >= requiredLength)
	        : false
	      ;
	    },

	    // see rls notes for 2.0.6 (this is a duplicate of minLength)
	    length: function(value, requiredLength) {
	      return (value !== undefined)
	        ? (value.length >= requiredLength)
	        : false
	      ;
	    },

	    // is exactly length
	    exactLength: function(value, requiredLength) {
	      return (value !== undefined)
	        ? (value.length == requiredLength)
	        : false
	      ;
	    },

	    // is less than length
	    maxLength: function(value, maxLength) {
	      return (value !== undefined)
	        ? (value.length <= maxLength)
	        : false
	      ;
	    },

	    // matches another field
	    match: function(value, identifier) {
	      var matchingValue;
	      if( $('[data-validate="'+ identifier +'"]').length > 0 ) {
	        matchingValue = $('[data-validate="'+ identifier +'"]').val();
	      }
	      else if($('#' + identifier).length > 0) {
	        matchingValue = $('#' + identifier).val();
	      }
	      else if($('[name="' + identifier +'"]').length > 0) {
	        matchingValue = $('[name="' + identifier + '"]').val();
	      }
	      else if( $('[name="' + identifier +'[]"]').length > 0 ) {
	        matchingValue = $('[name="' + identifier +'[]"]');
	      }
	      return (matchingValue !== undefined)
	        ? ( value.toString() == matchingValue.toString() )
	        : false
	      ;
	    },

	    // different than another field
	    different: function(value, identifier) {
	      // use either id or name of field
	      var matchingValue;
	      if( $('[data-validate="'+ identifier +'"]').length > 0 ) {
	        matchingValue = $('[data-validate="'+ identifier +'"]').val();
	      }
	      else if($('#' + identifier).length > 0) {
	        matchingValue = $('#' + identifier).val();
	      }
	      else if($('[name="' + identifier +'"]').length > 0) {
	        matchingValue = $('[name="' + identifier + '"]').val();
	      }
	      else if( $('[name="' + identifier +'[]"]').length > 0 ) {
	        matchingValue = $('[name="' + identifier +'[]"]');
	      }
	      return (matchingValue !== undefined)
	        ? ( value.toString() !== matchingValue.toString() )
	        : false
	      ;
	    },

	    creditCard: function(cardNumber, cardTypes) {
	      var
	        cards = {
	          visa: {
	            pattern : /^4/,
	            length  : [16]
	          },
	          amex: {
	            pattern : /^3[47]/,
	            length  : [15]
	          },
	          mastercard: {
	            pattern : /^5[1-5]/,
	            length  : [16]
	          },
	          discover: {
	            pattern : /^(6011|622(12[6-9]|1[3-9][0-9]|[2-8][0-9]{2}|9[0-1][0-9]|92[0-5]|64[4-9])|65)/,
	            length  : [16]
	          },
	          unionPay: {
	            pattern : /^(62|88)/,
	            length  : [16, 17, 18, 19]
	          },
	          jcb: {
	            pattern : /^35(2[89]|[3-8][0-9])/,
	            length  : [16]
	          },
	          maestro: {
	            pattern : /^(5018|5020|5038|6304|6759|676[1-3])/,
	            length  : [12, 13, 14, 15, 16, 17, 18, 19]
	          },
	          dinersClub: {
	            pattern : /^(30[0-5]|^36)/,
	            length  : [14]
	          },
	          laser: {
	            pattern : /^(6304|670[69]|6771)/,
	            length  : [16, 17, 18, 19]
	          },
	          visaElectron: {
	            pattern : /^(4026|417500|4508|4844|491(3|7))/,
	            length  : [16]
	          }
	        },
	        valid         = {},
	        validCard     = false,
	        requiredTypes = (typeof cardTypes == 'string')
	          ? cardTypes.split(',')
	          : false,
	        unionPay,
	        validation
	      ;

	      if(typeof cardNumber !== 'string' || cardNumber.length === 0) {
	        return;
	      }

	      // verify card types
	      if(requiredTypes) {
	        $.each(requiredTypes, function(index, type){
	          // verify each card type
	          validation = cards[type];
	          if(validation) {
	            valid = {
	              length  : ($.inArray(cardNumber.length, validation.length) !== -1),
	              pattern : (cardNumber.search(validation.pattern) !== -1)
	            };
	            if(valid.length && valid.pattern) {
	              validCard = true;
	            }
	          }
	        });

	        if(!validCard) {
	          return false;
	        }
	      }

	      // skip luhn for UnionPay
	      unionPay = {
	        number  : ($.inArray(cardNumber.length, cards.unionPay.length) !== -1),
	        pattern : (cardNumber.search(cards.unionPay.pattern) !== -1)
	      };
	      if(unionPay.number && unionPay.pattern) {
	        return true;
	      }

	      // verify luhn, adapted from  <https://gist.github.com/2134376>
	      var
	        length        = cardNumber.length,
	        multiple      = 0,
	        producedValue = [
	          [0, 1, 2, 3, 4, 5, 6, 7, 8, 9],
	          [0, 2, 4, 6, 8, 1, 3, 5, 7, 9]
	        ],
	        sum           = 0
	      ;
	      while (length--) {
	        sum += producedValue[multiple][parseInt(cardNumber.charAt(length), 10)];
	        multiple ^= 1;
	      }
	      return (sum % 10 === 0 && sum > 0);
	    },

	    minCount: function(value, minCount) {
	      if(minCount == 0) {
	        return true;
	      }
	      if(minCount == 1) {
	        return (value !== '');
	      }
	      return (value.split(',').length >= minCount);
	    },

	    exactCount: function(value, exactCount) {
	      if(exactCount == 0) {
	        return (value === '');
	      }
	      if(exactCount == 1) {
	        return (value !== '' && value.search(',') === -1);
	      }
	      return (value.split(',').length == exactCount);
	    },

	    maxCount: function(value, maxCount) {
	      if(maxCount == 0) {
	        return false;
	      }
	      if(maxCount == 1) {
	        return (value.search(',') === -1);
	      }
	      return (value.split(',').length <= maxCount);
	    }
	  }
	};
	let onChecks = () => {
		prompt.removeAll();
		let checks = true;
		Object.keys(config.fields).forEach((key) => {
			let field = config.fields[key];
			let identifier = field.identifier;
			var $els = $form.find("[name='test']");
			if(Object.prototype.toString.call(identifier) == "[object Array]") {
				identifier.forEach((name) => {
					$els = $els.add($form.find("[name='" + name + "']"));
				});
			} else {
				$els = $els.add($form.find("[name='" + key + "']"));
			}
			if(!onCheck($els, field)) {
				checks = false;
			}
		});
		return checks;
	};
	blur.on = function() {
		if(this.onBlur) {
			return;
		}
		Object.keys(config.fields).forEach((key) => {
			let field = config.fields[key];
			let identifier = field.identifier;
			var $els = $form.find("[name='test']");
			if(Object.prototype.toString.call(identifier) == "[object Array]") {
				identifier.forEach((name) => {
					$els = $els.add($form.find("[name='" + name + "']"));
				});
			} else {
				$els = $els.add($form.find("[name='" + key + "']"));
			}
			var validateBlur = () => {
				onCheck($els, field);
			};
			$els.unbind('blur', validateBlur).bind('blur', validateBlur);
		});
		this.onBlur = true;
	};
	if(config.on == 'blur') {
		blur.on();
	}
	let onCheck = ($els, field) => {
		let check = true;
		prompt.remove($els.eq(0), field.rules[0]);
		$els.each(function(index) {
			let $el = $(this);
			for(var i = 0; i < field.rules.length; i ++) {
				let rule = field.rules[i];
				var
					value        = $el.val(),
					ancillary    = settings.get.ancillaryValue(rule),
					ruleName     = settings.get.ruleName(rule),
					ruleFunction = settings.rules[ruleName]
				;
				if(!$.isFunction(ruleFunction)) {
					check = false;
					prompt.add($el, rule);
					break;
				} else {
					if(!ruleFunction.call($el, value, ancillary)) {
						check = false;
						prompt.add($el, rule);
						break;
					}
				}

				// if(rule.type == 'empty') {
				// 	if(!$el.val()) {
				// 		check = false;
				// 		prompt.add($el, rule);
				// 		break;
				// 	}
				// } else if(rule.type == 'integer') {
				// 	if($el.val() % 1 !== 0) {
				// 		check = false;
				// 		prompt.add($el, rule);
				// 		break;
				// 	}
				// } else if(rule.type == 'checked') {
				// 	let $els = $form.find("[name='" + field.identifier + "']:checked");
				// 	if($els.length == 0) {
				// 		check = false;
				// 		prompt.add($el, rule);
				// 		break;
				// 	}
				// }
			}
		});
		return check;
	};
	let prompt = {
		add: ($el, rule) => {
			let template = `<div class="ui basic red pointing prompt label transition visible"></div>`;
			let $panel = $el.parents('.field').eq(0);
			//指定错误信息显示位置
			if(rule.parents) {
				$panel = $el.parents(rule.parents).eq(0);
			}
			//当前不存在其他校验失败 并且 错误信息不为空
			if($panel.find('.prompt').length == 0 && rule.prompt){
				$panel.append($(template).text(rule.prompt));
			}
			$el.parents('.field').eq(0).addClass("error");
		},
		remove: ($el, rule) => {
			let $panel = $el.parents('.field').eq(0);
			if(rule.parents) {
				$panel = $el.parents(rule.parents).eq(0);
			}
			$panel.find('.prompt').remove();
			$panel.find('.error').removeClass("error");
			$el.parents('.field').eq(0).removeClass("error");
			if(rule.type == 'checked') {
				let $els = $form.find("[name='" + $el.attr("name") + "']");
				$els.parents('.field').eq(0).removeClass("error");
			}
		},
		removeAll: () => {
			$form.find('.prompt').remove();
			$form.find('.error').removeClass("error");
		}
	};
	return onChecks();
}
