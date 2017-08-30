package com.jd.o2o.vipcart.web.controller;

import com.jd.o2o.vipcart.common.domain.PageBean;
import com.jd.o2o.vipcart.common.domain.enums.OperEnum;
import com.jd.o2o.vipcart.common.domain.response.ServiceResponse;
import com.jd.o2o.vipcart.common.domain.valid.SaveValid;
import com.jd.o2o.vipcart.common.domain.valid.UpdateValid;
import com.jd.o2o.vipcart.common.utils.BeanHelper;
import com.jd.o2o.vipcart.domain.entity.DictTypeEntity;
import com.jd.o2o.vipcart.domain.request.DictTypeRequest;
import com.jd.o2o.vipcart.service.base.DictTypeService;
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
@RequestMapping("/dictType")
public class DictTypeController extends BaseController {
    private static final Logger LOGGER = LoggerFactory.getLogger(DictTypeController.class);
    @Resource
    private DictTypeService dictTypeServiceImpl;


    @RequestMapping(value = "/list")
    public ModelAndView list(HttpServletRequest request, DictTypeRequest dictTypeRequest) {
        ModelAndView view = new ModelAndView("/dictType/listDictType");
        view.addObject("result", new ServiceResponse<>(dictTypeServiceImpl.findList(BeanHelper.copyTo(dictTypeRequest, DictTypeEntity.class))));
        return view;
    }

    @RequestMapping(value = "/plist")
    public ModelAndView pageList(HttpServletRequest request, DictTypeRequest dictTypeRequest, PageBean pageBean) {
        ModelAndView view = new ModelAndView("/dictType/listDictType");
        PageBean result = dictTypeServiceImpl.pageQuery(BeanHelper.copyTo(dictTypeRequest, DictTypeEntity.class), pageBean);
        view.addObject("result", new ServiceResponse<>(result));
        view.addObject("page", result);
        view.addObject("params", BeanHelper.modelToMap(dictTypeRequest));
        return view;
    }

    @RequestMapping(value = "/lists")
    public ModelAndView listSearch(HttpServletRequest request, DictTypeRequest dictTypeRequest, PageBean pageBean) {
        ModelAndView view = new ModelAndView("/dictType/listSearchDictType");
        PageBean result = dictTypeServiceImpl.pageQuery(BeanHelper.copyTo(dictTypeRequest, DictTypeEntity.class), pageBean);
        view.addObject("result", new ServiceResponse<>(result));
        view.addObject("page", result);
        view.addObject("params", BeanHelper.modelToMap(dictTypeRequest));
        return view;
    }

    @RequestMapping(value = "/add")
    public ModelAndView add(HttpServletRequest request) {
        ModelAndView view = new ModelAndView("/dictType/editDictType");
        view.addObject("result", new ServiceResponse<String>());
        return view;
    }

    @RequestMapping(value = "/save")
    public ModelAndView save(HttpServletRequest request, @Validated({SaveValid.class}) DictTypeRequest dictTypeRequest, BindingResult errors) {
        ModelAndView view = new ModelAndView("/dictType/plist");
        if (errors.hasErrors()) {
            return view.addObject("errors", errors.getAllErrors());
        }
        DictTypeEntity dictTypeEntity = BeanHelper.copyTo(dictTypeRequest, DictTypeEntity.class);
        buildBaseEntityBean(dictTypeEntity, dictTypeRequest.getId() == null ? OperEnum.ADD : OperEnum.EDIT);
        int r = dictTypeServiceImpl.saveOrUpdate(dictTypeEntity);
        view.addObject("result", new ServiceResponse<>(r));
        return view;
    }

    @RequestMapping(value = "/edit/{id}")
    public ModelAndView edit(HttpServletRequest request, @PathVariable("id") Long id) {
        ModelAndView view = new ModelAndView("/dictType/editDictType");
        DictTypeEntity dictTypeEntity = dictTypeServiceImpl.get(id);
        view.addObject("result", new ServiceResponse<>(dictTypeEntity));
        return view;
    }

    @RequestMapping(value = "/update")
    public ModelAndView update(HttpServletRequest request, @Validated({UpdateValid.class}) DictTypeRequest dictTypeRequest, BindingResult errors) {
        ModelAndView view = new ModelAndView("/dictType/plist");
        if (errors.hasErrors()) {
            return view.addObject("errors", errors.getAllErrors());
        }
        DictTypeEntity dictTypeEntity = BeanHelper.copyTo(dictTypeRequest, DictTypeEntity.class);
        buildBaseEntityBean(dictTypeEntity, OperEnum.EDIT);
        int r = dictTypeServiceImpl.update(dictTypeEntity);
        view.addObject("result", new ServiceResponse<>(r));
        return view;
    }

    @RequestMapping(value = "/delete/{id}")
    public ModelAndView deletes(HttpServletRequest request, @PathVariable("id") Long[] ids) {
        ModelAndView view = new ModelAndView("/dictType/plist");
        int r = 0;
        for (Long id : ids) {
            r = r + dictTypeServiceImpl.delete(id);
        }
        view.addObject("result", new ServiceResponse<>(r));
        return view;
    }
}