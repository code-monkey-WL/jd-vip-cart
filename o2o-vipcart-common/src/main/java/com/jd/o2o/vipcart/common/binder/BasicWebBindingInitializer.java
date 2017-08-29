package com.jd.o2o.vipcart.common.binder;

import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.support.WebBindingInitializer;
import org.springframework.web.context.request.WebRequest;

import java.util.Date;

/**
 * 全局的类型转换器
 * 当Controller处理器方法参数使用@RequestParam、@PathVariable、@RequestHeader、@CookieValue和@ModelAttribute标记的时候
 * 都会触发initBinder方法的执行，这包括使用WebBindingInitializer定义的全局方法和在Controller中使用@InitBinder标记的局部方法。
 * 而且每个使用了这几个注解标记的参数都会触发一次initBinder方法的执行，
 * 这也意味着有几个参数使用了上述注解就会触发几次initBinder方法的执行
 * 让Spring能够识别解析器需要添加配置如下：
 * < bean class = "org.springframework.web.servlet.mvc.annotation.AnnotationMethodHandlerAdapter" >
 * < property name = "webBindingInitializer" >
 * < bean class = "com.jd.o2o.product.common.web.binder.BasicWebBindingInitializer" />
 * </ property >
 * </ bean >
 * <p/>
 * Created by liuhuiqing on 2015/3/11.
 */
public class BasicWebBindingInitializer implements WebBindingInitializer {
    @Override
    public void initBinder(WebDataBinder binder, WebRequest request) {
        binder.registerCustomEditor(Date.class, new DateEditorSupport());
        binder.addValidators(new AppContextValidator());
    }
}
