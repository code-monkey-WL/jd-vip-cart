package com.jd.o2o.vipcart.web.controller;

import com.jd.o2o.vipcart.common.domain.PageBean;
import com.jd.o2o.vipcart.common.domain.enums.OperEnum;
import com.jd.o2o.vipcart.common.domain.response.ServiceResponse;
import com.jd.o2o.vipcart.common.domain.valid.SaveValid;
import com.jd.o2o.vipcart.common.domain.valid.UpdateValid;
import com.jd.o2o.vipcart.common.utils.BeanHelper;
import com.jd.o2o.vipcart.domain.entity.CategoryInfoEntity;
import com.jd.o2o.vipcart.domain.request.CategoryInfoRequest;
import com.jd.o2o.vipcart.service.base.CategoryInfoService;
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
@RequestMapping("/categoryInfo")
public class CategoryInfoController extends BaseController {
    private static final Logger LOGGER = LoggerFactory.getLogger(CategoryInfoController.class);
    @Resource
    private CategoryInfoService categoryInfoServiceImpl;


    @RequestMapping(value = "/list")
    public ModelAndView list(HttpServletRequest request, CategoryInfoRequest categoryInfoRequest) {
        ModelAndView view = new ModelAndView("/categoryInfo/listCategoryInfo");
        view.addObject("result", new ServiceResponse<>(categoryInfoServiceImpl.findList(BeanHelper.copyTo(categoryInfoRequest, CategoryInfoEntity.class))));
        return view;
    }

    @RequestMapping(value = "/plist")
    public ModelAndView pageList(HttpServletRequest request, CategoryInfoRequest categoryInfoRequest, PageBean pageBean) {
        ModelAndView view = new ModelAndView("/categoryInfo/listCategoryInfo");
        PageBean result = categoryInfoServiceImpl.pageQuery(BeanHelper.copyTo(categoryInfoRequest, CategoryInfoEntity.class), pageBean);
        view.addObject("result", new ServiceResponse<>(result));
        view.addObject("page", result);
        view.addObject("params", BeanHelper.modelToMap(categoryInfoRequest));
        return view;
    }

    @RequestMapping(value = "/lists")
    public ModelAndView listSearch(HttpServletRequest request, CategoryInfoRequest categoryInfoRequest, PageBean pageBean) {
        ModelAndView view = new ModelAndView("/categoryInfo/listSearchCategoryInfo");
        PageBean result = categoryInfoServiceImpl.pageQuery(BeanHelper.copyTo(categoryInfoRequest, CategoryInfoEntity.class), pageBean);
        view.addObject("result", new ServiceResponse<>(result));
        view.addObject("page", result);
        view.addObject("params", BeanHelper.modelToMap(categoryInfoRequest));
        return view;
    }

    @RequestMapping(value = "/add")
    public ModelAndView add(HttpServletRequest request) {
        ModelAndView view = new ModelAndView("/categoryInfo/editCategoryInfo");
        view.addObject("result", new ServiceResponse<String>());
        return view;
    }

    @RequestMapping(value = "/save")
    public ModelAndView save(HttpServletRequest request, @Validated({SaveValid.class}) CategoryInfoRequest categoryInfoRequest, BindingResult errors) {
        ModelAndView view = new ModelAndView("/categoryInfo/plist");
        if (errors.hasErrors()) {
            return view.addObject("errors", errors.getAllErrors());
        }
        CategoryInfoEntity categoryInfoEntity = BeanHelper.copyTo(categoryInfoRequest, CategoryInfoEntity.class);
        buildBaseEntityBean(categoryInfoEntity, categoryInfoRequest.getId() == null ? OperEnum.ADD : OperEnum.EDIT);
        int r = categoryInfoServiceImpl.saveOrUpdate(categoryInfoEntity);
        view.addObject("result", new ServiceResponse<>(r));
        return view;
    }

    @RequestMapping(value = "/edit/{id}")
    public ModelAndView edit(HttpServletRequest request, @PathVariable("id") Long id) {
        ModelAndView view = new ModelAndView("/categoryInfo/editCategoryInfo");
        CategoryInfoEntity categoryInfoEntity = categoryInfoServiceImpl.get(id);
        view.addObject("result", new ServiceResponse<>(categoryInfoEntity));
        return view;
    }

    @RequestMapping(value = "/update")
    public ModelAndView update(HttpServletRequest request, @Validated({UpdateValid.class}) CategoryInfoRequest categoryInfoRequest, BindingResult errors) {
        ModelAndView view = new ModelAndView("/categoryInfo/plist");
        if (errors.hasErrors()) {
            return view.addObject("errors", errors.getAllErrors());
        }
        CategoryInfoEntity categoryInfoEntity = BeanHelper.copyTo(categoryInfoRequest, CategoryInfoEntity.class);
        buildBaseEntityBean(categoryInfoEntity, OperEnum.EDIT);
        int r = categoryInfoServiceImpl.update(categoryInfoEntity);
        view.addObject("result", new ServiceResponse<>(r));
        return view;
    }

    @RequestMapping(value = "/delete/{id}")
    public ModelAndView deletes(HttpServletRequest request, @PathVariable("id") Long[] ids) {
        ModelAndView view = new ModelAndView("/categoryInfo/plist");
        int r = 0;
        for (Long id : ids) {
            r = r + categoryInfoServiceImpl.delete(id);
        }
        view.addObject("result", new ServiceResponse<>(r));
        return view;
    }
}