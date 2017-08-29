package com.jd.o2o.vipcart.web.controller;

import com.jd.o2o.vipcart.common.plugins.log.track.LoggerTrackFactory;
import org.slf4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

/**
 * User: zhaoyun
 * Date: 16-9-12
 * Time: 上午10:01
 */
@Controller
@RequestMapping(value = "/")
public class IndexController extends BaseController {
    private static final Logger LOGGER = LoggerTrackFactory.getLogger(IndexController.class);

    @RequestMapping(value = "/index")
    public ModelAndView pageQueryAttachMq(HttpServletRequest request) throws Exception {
        ModelAndView mv = new ModelAndView("/monitor/index");
        return mv;

    }

}
