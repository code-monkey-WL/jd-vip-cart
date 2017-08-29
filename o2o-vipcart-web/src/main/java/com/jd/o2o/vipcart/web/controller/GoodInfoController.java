package com.jd.o2o.vipcart.web.controller;

import com.jd.o2o.vipcart.common.domain.PageBean;
import com.jd.o2o.vipcart.common.domain.enums.OperEnum;
import com.jd.o2o.vipcart.common.domain.response.ServiceResponse;
import com.jd.o2o.vipcart.common.domain.valid.SaveValid;
import com.jd.o2o.vipcart.common.domain.valid.UpdateValid;
import com.jd.o2o.vipcart.common.utils.BeanHelper;
import com.jd.o2o.vipcart.domain.entity.GoodInfoEntity;
import com.jd.o2o.vipcart.domain.request.GoodInfoRequest;
import com.jd.o2o.vipcart.service.base.GoodInfoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("/goodInfo")
public class GoodInfoController extends BaseController {
    private static final Logger LOGGER = LoggerFactory.getLogger(GoodInfoController.class);
    @Resource
    private GoodInfoService goodInfoServiceImpl;


    @RequestMapping(value = "/list")
    public ModelAndView list(HttpServletRequest request, GoodInfoRequest goodInfoRequest) {
        ModelAndView view = new ModelAndView("/goodInfo/listGoodInfo");
        view.addObject("result", new ServiceResponse<>(goodInfoServiceImpl.findList(BeanHelper.copyTo(goodInfoRequest, GoodInfoEntity.class))));
        return view;
    }

    @RequestMapping(value = "/plist")
    public ModelAndView pageList(HttpServletRequest request, GoodInfoRequest goodInfoRequest, PageBean pageBean) {
        ModelAndView view = new ModelAndView("/goodInfo/listGoodInfo");
        PageBean result = goodInfoServiceImpl.pageQuery(BeanHelper.copyTo(goodInfoRequest, GoodInfoEntity.class), pageBean);
        view.addObject("result", new ServiceResponse<>(result));
        view.addObject("page", result);
        view.addObject("params", BeanHelper.modelToMap(goodInfoRequest));
        return view;
    }

    @RequestMapping(value = "/lists")
    public ModelAndView listSearch(HttpServletRequest request, GoodInfoRequest goodInfoRequest, PageBean pageBean) {
        ModelAndView view = new ModelAndView("/goodInfo/listSearchGoodInfo");
        PageBean result = goodInfoServiceImpl.pageQuery(BeanHelper.copyTo(goodInfoRequest, GoodInfoEntity.class), pageBean);
        view.addObject("result", new ServiceResponse<>(result));
        view.addObject("page", result);
        view.addObject("params", BeanHelper.modelToMap(goodInfoRequest));
        return view;
    }

    @RequestMapping(value = "/add")
    public ModelAndView add(HttpServletRequest request) {
        ModelAndView view = new ModelAndView("/goodInfo/editGoodInfo");
        view.addObject("result", new ServiceResponse<String>());
        return view;
    }

    @RequestMapping(value = "/save")
    public ModelAndView save(HttpServletRequest request, @Validated({SaveValid.class}) GoodInfoRequest goodInfoRequest, BindingResult errors) {
        ModelAndView view = new ModelAndView("/goodInfo/plist");
        if (errors.hasErrors()) {
            return view.addObject("errors", errors.getAllErrors());
        }
        GoodInfoEntity goodInfoEntity = BeanHelper.copyTo(goodInfoRequest, GoodInfoEntity.class);
        buildBaseEntityBean(goodInfoEntity, goodInfoRequest.getId() == null ? OperEnum.ADD : OperEnum.EDIT);
        int r = goodInfoServiceImpl.save(goodInfoEntity);
        view.addObject("result", new ServiceResponse<>(r));
        return view;
    }

    @RequestMapping(value = "/edit/{id}")
    public ModelAndView edit(HttpServletRequest request, @PathVariable("id") Long id) {
        ModelAndView view = new ModelAndView("/goodInfo/editGoodInfo");
        GoodInfoEntity goodInfoEntity = goodInfoServiceImpl.get(id);
        view.addObject("result", new ServiceResponse<>(goodInfoEntity));
        return view;
    }

    @RequestMapping(value = "/update")
    public ModelAndView update(HttpServletRequest request, @Validated({UpdateValid.class}) GoodInfoRequest goodInfoRequest, BindingResult errors) {
        ModelAndView view = new ModelAndView("/goodInfo/plist");
        if (errors.hasErrors()) {
            return view.addObject("errors", errors.getAllErrors());
        }
        GoodInfoEntity goodInfoEntity = BeanHelper.copyTo(goodInfoRequest, GoodInfoEntity.class);
        buildBaseEntityBean(goodInfoEntity, OperEnum.EDIT);
        int r = goodInfoServiceImpl.update(goodInfoEntity);
        view.addObject("result", new ServiceResponse<>(r));
        return view;
    }

    @RequestMapping(value = "/delete/{id}")
    public ModelAndView deletes(HttpServletRequest request, @PathVariable("id") Long id) {
        ModelAndView view = new ModelAndView("/goodInfo/plist");
        int r = goodInfoServiceImpl.delete(id);
        view.addObject("result", new ServiceResponse<>(r));
        return view;
    }
}