package com.jd.o2o.vipcart.web.controller;

import com.jd.o2o.vipcart.common.domain.PageBean;
import com.jd.o2o.vipcart.common.domain.enums.OperEnum;
import com.jd.o2o.vipcart.common.domain.response.ServiceResponse;
import com.jd.o2o.vipcart.common.domain.valid.SaveValid;
import com.jd.o2o.vipcart.common.domain.valid.UpdateValid;
import com.jd.o2o.vipcart.common.utils.BeanHelper;
import com.jd.o2o.vipcart.domain.entity.SpiderConfigEntity;
import com.jd.o2o.vipcart.domain.request.SpiderConfigRequest;
import com.jd.o2o.vipcart.service.base.SpiderConfigService;
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
@RequestMapping("/spiderConfig")
public class SpiderConfigController extends BaseController {
	private static final Logger LOGGER = LoggerFactory.getLogger(SpiderConfigController.class);
    @Resource
    private SpiderConfigService spiderConfigServiceImpl;


    @RequestMapping(value = "/list")
    public ModelAndView list(HttpServletRequest request,SpiderConfigRequest spiderConfigRequest) {
        ModelAndView view = new ModelAndView("/spiderConfig/listSpiderConfig");
        view.addObject("result",new ServiceResponse<>(spiderConfigServiceImpl.findList(BeanHelper.copyTo(spiderConfigRequest, SpiderConfigEntity.class))));
        return view;
    }
	
	@RequestMapping(value = "/plist")
    public ModelAndView pageList(HttpServletRequest request,SpiderConfigRequest spiderConfigRequest,PageBean pageBean) {
        ModelAndView view = new ModelAndView("/spiderConfig/listSpiderConfig");
		PageBean result = spiderConfigServiceImpl.pageQuery(BeanHelper.copyTo(spiderConfigRequest,SpiderConfigEntity.class), pageBean);
        view.addObject("result", new ServiceResponse<>(result));
        view.addObject("page", result);
        view.addObject("params",BeanHelper.modelToMap(spiderConfigRequest));
        return view;
    }
	
	@RequestMapping(value = "/lists")
	public ModelAndView listSearch(HttpServletRequest request, SpiderConfigRequest spiderConfigRequest, PageBean pageBean) {
        ModelAndView view = new ModelAndView("/spiderConfig/listSearchSpiderConfig");
        PageBean result = spiderConfigServiceImpl.pageQuery(BeanHelper.copyTo(spiderConfigRequest, SpiderConfigEntity.class), pageBean);
        view.addObject("result", new ServiceResponse<>(result));
        view.addObject("page", result);
        view.addObject("params",BeanHelper.modelToMap(spiderConfigRequest));
        return view;
    }
	
	 @RequestMapping(value = "/add")
    public ModelAndView add(HttpServletRequest request) {
        ModelAndView view = new ModelAndView("/spiderConfig/editSpiderConfig");
		view.addObject("result", new ServiceResponse<String>());
        return view;
    }
	
	@RequestMapping(value = "/save")
    public ModelAndView save(HttpServletRequest request,@Validated({SaveValid.class}) SpiderConfigRequest spiderConfigRequest, BindingResult errors) {
        ModelAndView view = new ModelAndView("/spiderConfig/plist");
		if(errors.hasErrors()){
            return view.addObject("errors", errors.getAllErrors());
        }
        SpiderConfigEntity spiderConfigEntity = BeanHelper.copyTo(spiderConfigRequest,SpiderConfigEntity.class);
		buildBaseEntityBean(spiderConfigEntity, spiderConfigRequest.getId()==null? OperEnum.ADD:OperEnum.EDIT);
        int r = spiderConfigServiceImpl.saveOrUpdate(spiderConfigEntity);
        view.addObject("result",new ServiceResponse<>(r));
        return view;
    }
	
	@RequestMapping(value = "/edit/{id}")
    public ModelAndView edit(HttpServletRequest request,@PathVariable("id") Long id) {
        ModelAndView view = new ModelAndView("/spiderConfig/editSpiderConfig");
        SpiderConfigEntity spiderConfigEntity = spiderConfigServiceImpl.get(id);
        view.addObject("result",new ServiceResponse<>(spiderConfigEntity));
        return view;
    }
	
	@RequestMapping(value = "/update")
    public ModelAndView update(HttpServletRequest request,@Validated({ UpdateValid.class}) SpiderConfigRequest spiderConfigRequest, BindingResult errors) {
        ModelAndView view = new ModelAndView("/spiderConfig/plist");
		if(errors.hasErrors()){
            return view.addObject("errors", errors.getAllErrors());
        }
        SpiderConfigEntity spiderConfigEntity = BeanHelper.copyTo(spiderConfigRequest,SpiderConfigEntity.class);
		buildBaseEntityBean(spiderConfigEntity, OperEnum.EDIT);
        int r = spiderConfigServiceImpl.update(spiderConfigEntity);
        view.addObject("result",new ServiceResponse<>(r));
        return view;
    }
	
	@RequestMapping(value = "/delete/{id}")
    public ModelAndView deletes(HttpServletRequest request,@PathVariable("id") Long[] ids) {
        ModelAndView view = new ModelAndView("/spiderConfig/plist");
        int r = 0;
        for (Long id : ids) {
            r = r + spiderConfigServiceImpl.delete(id);
        }
        view.addObject("result", new ServiceResponse<>(r));
        return view;
    }
}