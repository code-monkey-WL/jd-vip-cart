package com.jd.o2o.vipcart.web.controller;

import com.jd.o2o.vipcart.common.domain.PageBean;
import com.jd.o2o.vipcart.common.domain.enums.OperEnum;
import com.jd.o2o.vipcart.common.domain.response.ServiceResponse;
import com.jd.o2o.vipcart.common.domain.valid.SaveValid;
import com.jd.o2o.vipcart.common.domain.valid.UpdateValid;
import com.jd.o2o.vipcart.common.utils.BeanHelper;
import com.jd.o2o.vipcart.domain.entity.BrandInfoEntity;
import com.jd.o2o.vipcart.domain.request.BrandInfoRequest;
import com.jd.o2o.vipcart.service.base.BrandInfoService;
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
@RequestMapping("/brandInfo")
public class BrandInfoController extends BaseController {
    private static final Logger LOGGER = LoggerFactory.getLogger(BrandInfoController.class);
    @Resource
    private BrandInfoService brandInfoServiceImpl;


    @RequestMapping(value = "/list")
    public ModelAndView list(HttpServletRequest request, BrandInfoRequest brandInfoRequest) {
        ModelAndView view = new ModelAndView("/brandInfo/listBrandInfo");
        view.addObject("result", new ServiceResponse<>(brandInfoServiceImpl.findList(BeanHelper.copyTo(brandInfoRequest, BrandInfoEntity.class))));
        return view;
    }

    @RequestMapping(value = "/plist")
    public ModelAndView pageList(HttpServletRequest request, BrandInfoRequest brandInfoRequest, PageBean pageBean) {
        ModelAndView view = new ModelAndView("/brandInfo/listBrandInfo");
        PageBean result = brandInfoServiceImpl.pageQuery(BeanHelper.copyTo(brandInfoRequest, BrandInfoEntity.class), pageBean);
        view.addObject("result", new ServiceResponse<>(result));
        view.addObject("page", result);
        view.addObject("params", BeanHelper.modelToMap(brandInfoRequest));
        return view;
    }

    @RequestMapping(value = "/lists")
    public ModelAndView listSearch(HttpServletRequest request, BrandInfoRequest brandInfoRequest, PageBean pageBean) {
        ModelAndView view = new ModelAndView("/brandInfo/listSearchBrandInfo");
        PageBean result = brandInfoServiceImpl.pageQuery(BeanHelper.copyTo(brandInfoRequest, BrandInfoEntity.class), pageBean);
        view.addObject("result", new ServiceResponse<>(result));
        view.addObject("page", result);
        view.addObject("params", BeanHelper.modelToMap(brandInfoRequest));
        return view;
    }

    @RequestMapping(value = "/add")
    public ModelAndView add(HttpServletRequest request) {
        ModelAndView view = new ModelAndView("/brandInfo/editBrandInfo");
        view.addObject("result", new ServiceResponse<String>());
        return view;
    }

    @RequestMapping(value = "/save")
    public ModelAndView save(HttpServletRequest request, @Validated({SaveValid.class}) BrandInfoRequest brandInfoRequest, BindingResult errors) {
        ModelAndView view = new ModelAndView("/brandInfo/plist");
        if (errors.hasErrors()) {
            return view.addObject("errors", errors.getAllErrors());
        }
        BrandInfoEntity brandInfoEntity = BeanHelper.copyTo(brandInfoRequest, BrandInfoEntity.class);
        buildBaseEntityBean(brandInfoEntity, brandInfoRequest.getId() == null ? OperEnum.ADD : OperEnum.EDIT);
        int r = brandInfoServiceImpl.saveOrUpdate(brandInfoEntity);
        view.addObject("result", new ServiceResponse<>(r));
        return view;
    }

    @RequestMapping(value = "/edit/{id}")
    public ModelAndView edit(HttpServletRequest request, @PathVariable("id") Long id) {
        ModelAndView view = new ModelAndView("/brandInfo/editBrandInfo");
        BrandInfoEntity brandInfoEntity = brandInfoServiceImpl.get(id);
        view.addObject("result", new ServiceResponse<>(brandInfoEntity));
        return view;
    }

    @RequestMapping(value = "/update")
    public ModelAndView update(HttpServletRequest request, @Validated({UpdateValid.class}) BrandInfoRequest brandInfoRequest, BindingResult errors) {
        ModelAndView view = new ModelAndView("/brandInfo/plist");
        if (errors.hasErrors()) {
            return view.addObject("errors", errors.getAllErrors());
        }
        BrandInfoEntity brandInfoEntity = BeanHelper.copyTo(brandInfoRequest, BrandInfoEntity.class);
        buildBaseEntityBean(brandInfoEntity, OperEnum.EDIT);
        int r = brandInfoServiceImpl.update(brandInfoEntity);
        view.addObject("result", new ServiceResponse<>(r));
        return view;
    }

    @RequestMapping(value = "/delete/{id}")
    public ModelAndView deletes(HttpServletRequest request, @PathVariable("id") Long[] ids) {
        ModelAndView view = new ModelAndView("/brandInfo/plist");
        int r = 0;
        for (Long id : ids) {
            r = r + brandInfoServiceImpl.delete(id);
        }
        view.addObject("result", new ServiceResponse<>(r));
        return view;
    }
}