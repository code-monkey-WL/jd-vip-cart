package com.jd.o2o.vipcart.web.controller;

import com.jd.o2o.vipcart.common.domain.BaseEntityBean;
import com.jd.o2o.vipcart.common.domain.PageBean;
import com.jd.o2o.vipcart.common.domain.context.AppContext;
import com.jd.o2o.vipcart.common.domain.enums.OperEnum;
import com.jd.o2o.vipcart.common.domain.enums.YNEnum;
import com.jd.o2o.vipcart.common.domain.exception.BaseMsgException;
import com.jd.o2o.vipcart.common.domain.response.ServiceResponse;
import com.jd.o2o.vipcart.common.plugins.log.track.LoggerTrackFactory;
import com.jd.o2o.vipcart.common.utils.BeanHelper;
import com.jd.o2o.vipcart.common.utils.json.JsonUtils;
import com.jd.o2o.vipcart.web.common.vo.UserVO;
import com.jd.o2o.vipcart.web.common.web.JsonResponse;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.util.*;

/**
 * 公共服务提取
 * Created by liuhuiqing on 2016/7/6.
 */
public class BaseController {
    private static final Logger LOGGER = LoggerTrackFactory.getLogger(BaseController.class);
    private static final String ITEM_PREFIX = "item_";
    @Resource
    protected AppContext appContext;

    /**
     * 获得登录人信息
     *
     * @param request
     * @return
     */
    protected UserVO getUserVO(HttpServletRequest request) {
        UserVO userVO = (UserVO) request.getAttribute("userVO");
        if (userVO == null || StringUtils.isEmpty(userVO.getUserId())) {
            throw new BaseMsgException("不能获得当前操作人登录信息");
        }
        return userVO;
    }


    /**
     * 获得经过网关处理后的表单信息
     *
     * @param request
     * @param target
     * @param <T>
     * @return
     */
    protected <T> T getPostDataFromGateway(HttpServletRequest request, Class<T> target) {
        Map<String, String[]> params = request.getParameterMap();
        String[] bodies = params.get("body");
        if (bodies != null && bodies.length > 0) {
            try {
                return JsonUtils.fromJson(bodies[0], target);
            } catch (Exception e) {
                LOGGER.error(String.format("字符串[%s]转对象[%s]出现异常", bodies[0], target.getName()), e);
            }
        }
        return null;
    }

    /**
     * 从网关获得用户登录态信息
     *
     * @param request
     * @return
     */
    protected UserVO getUserPinFromGateway(HttpServletRequest request) {
        Map<String, String[]> params = request.getParameterMap();
        String[] deliveryManNos = params.get("user");
        if (deliveryManNos != null && deliveryManNos.length > 0) {
            try {
                return JsonUtils.fromJson(deliveryManNos[0], UserVO.class);
            } catch (Exception e) {
                LOGGER.error(String.format("字符串[%s]转对象userVO出现异常", deliveryManNos[0]), e);
            }
        }
        return null;
    }

    /**
     * 获取HttpServletRequest请求的表单数据并转换成指定对象类型
     *
     * @param request
     * @return
     */
    protected <T> T getPostData(HttpServletRequest request, Class<T> target) {
        try {
            T result = target.newInstance();
            Field[] fields = target.getDeclaredFields();
            Map<String, String[]> params = request.getParameterMap();
            for (Field field : fields) {
                String name = field.getName();
                String[] values = params.get(name);
                if (values == null || values.length < 1) {
                    continue;
                }
                boolean isAccessible = field.isAccessible();
                if (!isAccessible) {
                    field.setAccessible(true);
                }
                Class<?> filedType = field.getType();
                if (filedType.isArray()) {
                    int length = values.length;
                    Class<?> clazz = filedType.getComponentType();
                    Object array = Array.newInstance(clazz, length);
                    for (int i = 0; i < length; i++) {
                        Object obj;
                        try {
                            obj = BeanHelper.primitiveToObject(values[i], clazz);
                        } catch (Exception e) {
                            obj = JsonUtils.fromJson(values[i], clazz);
                        }
                        Array.set(array, i, obj);
                    }
                    field.set(result, array);
                } else {
                    try {
                        field.set(result, BeanHelper.primitiveToObject(values[0], filedType));
                    } catch (Exception e) {
                        field.set(result, JsonUtils.fromJson(values[0], filedType));
                    }
                }
            }
            return result;
        } catch (Exception e) {
            LOGGER.error(String.format("http请求入参转换成对象[%s]出现异常", target.getName()), e);
            return null;
        }
    }


    /**
     * 将上传文件解析成数据对象
     *
     * @param request
     * @param clazz
     * @param <T>
     * @return
     * @throws Exception
     */
    protected <T> List<T> parseUploadFile(HttpServletRequest request, Class<T> clazz, boolean includeHead) throws Exception {
        if (!(request instanceof MultipartHttpServletRequest)) {
            throw new BaseMsgException("提交表单的数据类型不是multipart/form-data！");
        }
        Map<String, MultipartFile> fileMap = ((MultipartHttpServletRequest) request).getFileMap();
        if (fileMap == null || fileMap.isEmpty()) {
            throw new BaseMsgException("未找到文件类型数据！");
        }
        String[] columns = null;
        if (Map.class.isAssignableFrom(clazz)) {
            Object cols = request.getAttribute("cols");
            if (cols == null) {
                cols = request.getParameter("cols");
            }
            if (cols == null) {
                throw new BaseMsgException("入参文件属性个数不能为空！");
            }
            int num = Integer.valueOf(cols.toString());
            columns = new String[num];
            for (int i = 0; i < num; i++) {
                columns[i] = ITEM_PREFIX + i;
            }
        } else {
            Field[] fs = clazz.getDeclaredFields();
            int num = fs.length;
            columns = new String[num];
            for (int i = 0; i < num; i++) {
                columns[i] = fs[i].getName();
            }
        }
        List<T> dataList = new ArrayList<T>();
        int i = 0;
        for (Map.Entry<String, MultipartFile> entry : fileMap.entrySet()) {
            MultipartFile file = entry.getValue();
            if (file.isEmpty()) {
                continue;
            }
            int beginRow = 1;
            if (i > 0) {
                beginRow = 2;
            }
            List<T> list = null;
//            ExcelParser.getInstance().parse(file.getInputStream(), clazz, columns, beginRow, 0, 0);
            i++;
            if (CollectionUtils.isEmpty(list)) {
                continue;
            }
            if (includeHead) {
                dataList.addAll(list);
            } else {
                dataList.addAll(list.subList(1, list.size()));
            }
        }
//        System.err.println(JsonUtils.toJson(dataList));
        return dataList;
    }

    /**
     * 统一输出流对象
     *
     * @param response
     * @param obj
     * @throws java.io.IOException
     */
    protected void printOutStream(HttpServletResponse response, Object obj) throws IOException {
        response.setContentType("text/html;UTF-8");
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("result", obj);
        response.getWriter().write(JsonUtils.toJson(map));
    }

    /**
     * 统一视图处理
     *
     * @param view
     * @return
     */
    protected ModelAndView buildModelAndView(String view) {
        return buildModelAndView(view, null);
    }

    /**
     * 统一视图处理
     *
     * @param view
     * @param serviceResponse
     * @return
     */
    protected ModelAndView buildModelAndView(String view, ServiceResponse serviceResponse) {
        ModelAndView modelAndView = new ModelAndView(view);
        modelAndView.setViewName(view);
        if (serviceResponse != null) {
            modelAndView.addObject("result", serviceResponse);
        }
        return modelAndView;
    }

    /**
     * 统一视图处理,带分页
     *
     * @param view
     * @param serviceResponse
     * @return
     */
    protected <T> ModelAndView buildModelAndView(String view, ServiceResponse<PageBean<T>> serviceResponse, Map<String, String> queryMap) {
        ModelAndView modelAndView = new ModelAndView(view);
        modelAndView.setViewName(view);
        if (serviceResponse != null) {
            modelAndView.addObject("result", serviceResponse);
            modelAndView.addObject("page", serviceResponse.getResult());
            modelAndView.addObject("params", queryMap);
        }
        return modelAndView;
    }

    /**
     * 填充基本数据-添加数据
     *
     * @param entity
     */
    protected void paddingData4Add(HttpServletRequest request, BaseEntityBean entity) {
        entity.setCreatePin(getUserVO(request).getUserId());
        if (entity.getYn() == null) {
            entity.setYn(YNEnum.Y.getCode());
        }
        entity.setCreateTime(new Date());
    }

    /**
     * 填充基本数据-修改数据
     *
     * @param entity
     */
    protected void paddingData4Update(HttpServletRequest request, BaseEntityBean entity) {
        entity.setUpdatePin(getUserVO(request).getUserId());
        entity.setUpdateTime(new Date());
    }

    /**
     * 类型转换
     *
     * @param serviceResponse
     * @return
     */
    protected JsonResponse toJsonResponse(ServiceResponse serviceResponse) {
        JsonResponse jsonResponse = new JsonResponse(serviceResponse.getCode(), serviceResponse.getMsg());
        jsonResponse.setResult(serviceResponse.getResult());
        return jsonResponse;
    }

    /**
     * 填充操作人信息
     * @param baseEntityBean
     * @return
     */
    protected BaseEntityBean buildBaseEntityBean(BaseEntityBean baseEntityBean,OperEnum operEnum){
        String pin = getUserPinFromGateway(null).getUserId();
        if(operEnum == OperEnum.ADD){
            baseEntityBean.setCreatePin(pin);
        }else{
            baseEntityBean.setUpdatePin(pin);
        }
        baseEntityBean.setYn(YNEnum.Y.getCode());
        return baseEntityBean;
    }
}