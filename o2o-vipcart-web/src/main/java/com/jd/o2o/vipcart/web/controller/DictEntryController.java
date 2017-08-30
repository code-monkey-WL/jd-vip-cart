package com.jd.o2o.vipcart.web.controller;

import com.jd.o2o.vipcart.common.domain.PageBean;
import com.jd.o2o.vipcart.common.domain.enums.OperEnum;
import com.jd.o2o.vipcart.common.domain.response.ServiceResponse;
import com.jd.o2o.vipcart.common.domain.valid.SaveValid;
import com.jd.o2o.vipcart.common.domain.valid.UpdateValid;
import com.jd.o2o.vipcart.common.utils.BeanHelper;
import com.jd.o2o.vipcart.domain.entity.DictEntryEntity;
import com.jd.o2o.vipcart.domain.request.DictEntryRequest;
import com.jd.o2o.vipcart.service.base.DictEntryService;
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
@RequestMapping("/dictEntry")
public class DictEntryController extends BaseController {
    private static final Logger LOGGER = LoggerFactory.getLogger(DictEntryController.class);
    @Resource
    private DictEntryService dictEntryServiceImpl;


    @RequestMapping(value = "/list")
    public ModelAndView list(HttpServletRequest request, DictEntryRequest dictEntryRequest) {
        ModelAndView view = new ModelAndView("/dictEntry/listDictEntry");
        view.addObject("result", new ServiceResponse<>(dictEntryServiceImpl.findList(BeanHelper.copyTo(dictEntryRequest, DictEntryEntity.class))));
        return view;
    }

    @RequestMapping(value = "/plist")
    public ModelAndView pageList(HttpServletRequest request, DictEntryRequest dictEntryRequest, PageBean pageBean) {
        ModelAndView view = new ModelAndView("/dictEntry/listDictEntry");
        PageBean result = dictEntryServiceImpl.pageQuery(BeanHelper.copyTo(dictEntryRequest, DictEntryEntity.class), pageBean);
        view.addObject("result", new ServiceResponse<>(result));
        view.addObject("page", result);
        view.addObject("params", BeanHelper.modelToMap(dictEntryRequest));
        return view;
    }

    @RequestMapping(value = "/lists")
    public ModelAndView listSearch(HttpServletRequest request, DictEntryRequest dictEntryRequest, PageBean pageBean) {
        ModelAndView view = new ModelAndView("/dictEntry/listSearchDictEntry");
        PageBean result = dictEntryServiceImpl.pageQuery(BeanHelper.copyTo(dictEntryRequest, DictEntryEntity.class), pageBean);
        view.addObject("result", new ServiceResponse<>(result));
        view.addObject("page", result);
        view.addObject("params", BeanHelper.modelToMap(dictEntryRequest));
        return view;
    }

    @RequestMapping(value = "/add")
    public ModelAndView add(HttpServletRequest request) {
        ModelAndView view = new ModelAndView("/dictEntry/editDictEntry");
        view.addObject("result", new ServiceResponse<String>());
        return view;
    }

    @RequestMapping(value = "/save")
    public ModelAndView save(HttpServletRequest request, @Validated({SaveValid.class}) DictEntryRequest dictEntryRequest, BindingResult errors) {
        ModelAndView view = new ModelAndView("/dictEntry/plist");
        if (errors.hasErrors()) {
            return view.addObject("errors", errors.getAllErrors());
        }
        DictEntryEntity dictEntryEntity = BeanHelper.copyTo(dictEntryRequest, DictEntryEntity.class);
        buildBaseEntityBean(dictEntryEntity, dictEntryRequest.getId() == null ? OperEnum.ADD : OperEnum.EDIT);
        int r = dictEntryServiceImpl.saveOrUpdate(dictEntryEntity);
        view.addObject("result", new ServiceResponse<>(r));
        return view;
    }

    @RequestMapping(value = "/edit/{id}")
    public ModelAndView edit(HttpServletRequest request, @PathVariable("id") Long id) {
        ModelAndView view = new ModelAndView("/dictEntry/editDictEntry");
        DictEntryEntity dictEntryEntity = dictEntryServiceImpl.get(id);
        view.addObject("result", new ServiceResponse<>(dictEntryEntity));
        return view;
    }

    @RequestMapping(value = "/update")
    public ModelAndView update(HttpServletRequest request, @Validated({UpdateValid.class}) DictEntryRequest dictEntryRequest, BindingResult errors) {
        ModelAndView view = new ModelAndView("/dictEntry/plist");
        if (errors.hasErrors()) {
            return view.addObject("errors", errors.getAllErrors());
        }
        DictEntryEntity dictEntryEntity = BeanHelper.copyTo(dictEntryRequest, DictEntryEntity.class);
        buildBaseEntityBean(dictEntryEntity, OperEnum.EDIT);
        int r = dictEntryServiceImpl.update(dictEntryEntity);
        view.addObject("result", new ServiceResponse<>(r));
        return view;
    }

    @RequestMapping(value = "/delete/{id}")
    public ModelAndView deletes(HttpServletRequest request, @PathVariable("id") Long[] ids) {
        ModelAndView view = new ModelAndView("/dictEntry/plist");
        int r = 0;
        for (Long id : ids) {
            r = r + dictEntryServiceImpl.delete(id);
        }
        view.addObject("result", new ServiceResponse<>(r));
        return view;
    }
}