package com.jd.o2o.vipcart.web.controller;

import com.jd.o2o.vipcart.common.domain.PageBean;
import com.jd.o2o.vipcart.common.domain.enums.OperEnum;
import com.jd.o2o.vipcart.common.domain.response.ServiceResponse;
import com.jd.o2o.vipcart.common.domain.valid.SaveValid;
import com.jd.o2o.vipcart.common.domain.valid.UpdateValid;
import com.jd.o2o.vipcart.common.utils.BeanHelper;
import com.jd.o2o.vipcart.domain.entity.PromotionInfoEntity;
import com.jd.o2o.vipcart.domain.request.PromotionInfoRequest;
import com.jd.o2o.vipcart.service.base.PromotionInfoService;
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
@RequestMapping("/promotionInfo")
public class PromotionInfoController extends BaseController {
    private static final Logger LOGGER = LoggerFactory.getLogger(PromotionInfoController.class);
    @Resource
    private PromotionInfoService promotionInfoServiceImpl;


    @RequestMapping(value = "/list")
    public ModelAndView list(HttpServletRequest request, PromotionInfoRequest promotionInfoRequest) {
        ModelAndView view = new ModelAndView("/promotionInfo/listPromotionInfo");
        view.addObject("result", new ServiceResponse<>(promotionInfoServiceImpl.findList(BeanHelper.copyTo(promotionInfoRequest, PromotionInfoEntity.class))));
        return view;
    }

    @RequestMapping(value = "/plist")
    public ModelAndView pageList(HttpServletRequest request, PromotionInfoRequest promotionInfoRequest, PageBean pageBean) {
        ModelAndView view = new ModelAndView("/promotionInfo/listPromotionInfo");
        PageBean result = promotionInfoServiceImpl.pageQuery(BeanHelper.copyTo(promotionInfoRequest, PromotionInfoEntity.class), pageBean);
        view.addObject("result", new ServiceResponse<>(result));
        view.addObject("page", result);
        view.addObject("params", BeanHelper.modelToMap(promotionInfoRequest));
        return view;
    }

    @RequestMapping(value = "/lists")
    public ModelAndView listSearch(HttpServletRequest request, PromotionInfoRequest promotionInfoRequest, PageBean pageBean) {
        ModelAndView view = new ModelAndView("/promotionInfo/listSearchPromotionInfo");
        PageBean result = promotionInfoServiceImpl.pageQuery(BeanHelper.copyTo(promotionInfoRequest, PromotionInfoEntity.class), pageBean);
        view.addObject("result", new ServiceResponse<>(result));
        view.addObject("page", result);
        view.addObject("params", BeanHelper.modelToMap(promotionInfoRequest));
        return view;
    }

    @RequestMapping(value = "/add")
    public ModelAndView add(HttpServletRequest request) {
        ModelAndView view = new ModelAndView("/promotionInfo/editPromotionInfo");
        view.addObject("result", new ServiceResponse<String>());
        return view;
    }

    @RequestMapping(value = "/save")
    public ModelAndView save(HttpServletRequest request, @Validated({SaveValid.class}) PromotionInfoRequest promotionInfoRequest, BindingResult errors) {
        ModelAndView view = new ModelAndView("/promotionInfo/plist");
        if (errors.hasErrors()) {
            return view.addObject("errors", errors.getAllErrors());
        }
        PromotionInfoEntity promotionInfoEntity = BeanHelper.copyTo(promotionInfoRequest, PromotionInfoEntity.class);
        buildBaseEntityBean(promotionInfoEntity, promotionInfoRequest.getId() == null ? OperEnum.ADD : OperEnum.EDIT);
        int r = promotionInfoServiceImpl.saveOrUpdate(promotionInfoEntity);
        view.addObject("result", new ServiceResponse<>(r));
        return view;
    }

    @RequestMapping(value = "/edit/{id}")
    public ModelAndView edit(HttpServletRequest request, @PathVariable("id") Long id) {
        ModelAndView view = new ModelAndView("/promotionInfo/editPromotionInfo");
        PromotionInfoEntity promotionInfoEntity = promotionInfoServiceImpl.get(id);
        view.addObject("result", new ServiceResponse<>(promotionInfoEntity));
        return view;
    }

    @RequestMapping(value = "/update")
    public ModelAndView update(HttpServletRequest request, @Validated({UpdateValid.class}) PromotionInfoRequest promotionInfoRequest, BindingResult errors) {
        ModelAndView view = new ModelAndView("/promotionInfo/plist");
        if (errors.hasErrors()) {
            return view.addObject("errors", errors.getAllErrors());
        }
        PromotionInfoEntity promotionInfoEntity = BeanHelper.copyTo(promotionInfoRequest, PromotionInfoEntity.class);
        buildBaseEntityBean(promotionInfoEntity, OperEnum.EDIT);
        int r = promotionInfoServiceImpl.update(promotionInfoEntity);
        view.addObject("result", new ServiceResponse<>(r));
        return view;
    }

    @RequestMapping(value = "/delete/{id}")
    public ModelAndView deletes(HttpServletRequest request, @PathVariable("id") Long[] ids) {
        ModelAndView view = new ModelAndView("/promotionInfo/plist");
        int r = 0;
        for (Long id : ids) {
            r = r + promotionInfoServiceImpl.delete(id);
        }
        view.addObject("result", new ServiceResponse<>(r));
        return view;
    }
}