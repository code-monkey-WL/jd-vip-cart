package com.jd.o2o.vipcart.common.plugins.httpcliend;

import com.jd.o2o.vipcart.common.domain.response.BaseResponseCode;
import com.jd.o2o.vipcart.common.domain.response.ServiceResponse;
import com.jd.o2o.vipcart.common.plugins.httpcliend.manage.HttpClientFutureCallback;
import com.jd.o2o.vipcart.common.plugins.httpcliend.manage.HttpClientManagerBuilder;
import com.jd.o2o.vipcart.common.plugins.httpcliend.manage.HttpClientProxy;
import com.jd.o2o.vipcart.common.plugins.httpcliend.manage.HttpClientResponseHandler;
import com.jd.o2o.vipcart.common.utils.json.JsonUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.http.Consts;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.fluent.Async;
import org.apache.http.client.fluent.Executor;
import org.apache.http.client.fluent.Form;
import org.apache.http.client.fluent.Request;
import org.apache.http.entity.ContentType;
import org.apache.http.util.Args;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.*;
import java.util.concurrent.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

//@Component
public class HttpClientExecutor {
    private static final Logger logger = LoggerFactory.getLogger(HttpClientExecutor.class);
    private static int maxPoolNum = 2 * Runtime.getRuntime()
            .availableProcessors();// 最大线程数
    private Executor executor;
    private int defaultTimeOut = 5000;
    private Charset charSet = Consts.UTF_8;
    private ContentType defaultContentType = ContentType.create("application/x-www-form-urlencoded", charSet);

    private ResponseHandler<String> responseHandler = null;

    private HttpClientExecutor() {
        super();
        HttpClientManagerBuilder httpClientManagerBuilder = HttpClientManagerBuilder.create();
        this.executor = Executor.newInstance(httpClientManagerBuilder.builder());
        HttpClientProxy.getInstance().addHttpClientManagerBuilder(httpClientManagerBuilder);
        responseHandler = getInstanceResponseHandler(charSet);
    }

    private HttpClientExecutor(HttpClient httpclient) {
        super();
        this.executor = Executor.newInstance(httpclient);
        responseHandler = getInstanceResponseHandler(charSet);
    }

    public static HttpClientExecutor newInstance() {
        return new HttpClientExecutor();
    }

    public static HttpClientExecutor newInstance(HttpClient httpclient) {
        return new HttpClientExecutor(httpclient);
    }

    public HttpClientResponseHandler getInstanceResponseHandler(Charset charSet) {
        return new HttpClientResponseHandler(charSet);
    }

    public void setResponseHandler(ResponseHandler<String> responseHandler) {
        this.responseHandler = responseHandler;
    }

    /**
     * 设置请求的默认过期时间
     *
     * @param url
     * @return
     * @author liuhuiqing DateTime 2014-6-24 上午9:39:00
     */
    public Request getRequest(String url) {
        Args.notEmpty(url, "url");
        return Request.Get(url).connectTimeout(defaultTimeOut)
                .socketTimeout(defaultTimeOut);
    }

    /**
     * 设置请求的默认过期时间
     *
     * @param url
     * @return
     * @author liuhuiqing DateTime 2014-6-24 上午9:39:00
     */
    public Request getRequest(String url, Map<String, String> headerMap) {
        Args.notEmpty(url, "url");
        Request request = Request.Get(url).connectTimeout(defaultTimeOut)
                .socketTimeout(defaultTimeOut);
        if(headerMap == null || headerMap.isEmpty()){
            return request;
        }
        for(Map.Entry<String,String> entry:headerMap.entrySet()){
            request.addHeader(entry.getKey(), entry.getValue());
        }
        return request;
    }

    /**
     * 将指定map的数据组装成form表单数据
     *
     * @param url
     * @param formMap
     * @param contentType
     * @return
     */
    public Request getPostRequest(String url, Map<String, String> formMap, ContentType contentType) {
       return getPostRequest(url,formMap,null,contentType);
    }

    /**
     * 将指定map的数据组装成form表单数据
     *
     * @param url
     * @param formMap
     * @param headerMap
     * @param contentType
     * @return
     */
    public Request getPostRequest(String url, Map<String, String> formMap, Map<String, String> headerMap,ContentType contentType) {
        Args.notEmpty(url, "url");
        if (contentType == null) {
            contentType = defaultContentType;
        }
        if(headerMap == null){
            headerMap = new HashMap<String, String>();
        }
        if(headerMap.get("X-Custom-header") == null){
            headerMap.put("X-Custom-header", "stuff");
        }
        Request request = Request.Post(url);
        for(Map.Entry<String,String> entry:headerMap.entrySet()){
            request.addHeader(entry.getKey(), entry.getValue());
        }
        if (ContentType.APPLICATION_FORM_URLENCODED.getMimeType().equals(contentType.getMimeType())) {
            return request.bodyForm(mapToPair(formMap), charSet);
        }
        return request.bodyString(mapToJsonString(formMap), contentType);
    }

    public Executor getExecutor() {
        return executor;
    }

    /**
     * 处理httpget请求
     *
     * @param url
     * @return
     * @author liuhuiqing DateTime 2014-6-24 上午9:37:25
     */
    public ServiceResponse<String> executeGet(String url) {
        return executeGet(url, responseHandler);
    }

    /**
     * 处理httpget请求,带自定义回调处理逻辑
     *
     * @param url
     * @param responseHandler
     * @return
     * @author liuhuiqing DateTime 2014-6-24 上午11:06:33
     */
    public <T> ServiceResponse<T> executeGet(String url,
                                             ResponseHandler<T> responseHandler) {
        return executeGet(url,null,responseHandler);
    }

    /**
     * 处理httpget请求,带自定义回调处理逻辑
     *
     * @param url
     * @param responseHandler
     * @return
     * @author liuhuiqing DateTime 2014-6-24 上午11:06:33
     */
    public <T> ServiceResponse<T> executeGet(String url, Map<String, String> headerMap,
                                             ResponseHandler<T> responseHandler) {
        Args.notNull(responseHandler, "responseHandler");
        Request request = getRequest(url,headerMap);
        return executeSingle(request, 0, responseHandler);
    }

    /**
     * 处理httppost请求
     *
     * @param url
     * @param formMap
     * @return
     * @author liuhuiqing DateTime 2014-6-24 上午9:37:02
     */
    public ServiceResponse<String> executePost(String url, Map<String, String> formMap) {
        return executePost(url, formMap, responseHandler);
    }

    /**
     * 处理httppost请求,带自定义回调处理逻辑
     *
     * @param url
     * @param formMap
     * @param <T>
     * @return
     */
    public <T> ServiceResponse<T> executePost(String url, Map<String, String> formMap,
                                              ResponseHandler<T> responseHandler) {
        return executePost(url,formMap,null,null,responseHandler);
    }

    /**
     * 处理httppost请求,带自定义回调处理逻辑
     *
     * @param url
     * @param formMap
     * @param headerMap
     * @return
     */
    public ServiceResponse<String> executePost(String url, Map<String, String> formMap,Map<String, String> headerMap) {
        return executePost(url,formMap,headerMap,null,responseHandler);
    }

    /**
     * 处理httppost请求,带自定义回调处理逻辑
     *
     * @param url
     * @param formMap
     * @param headerMap
     * @param <T>
     * @return
     */
    public <T> ServiceResponse<T> executePost(String url, Map<String, String> formMap,Map<String, String> headerMap,
                                              ContentType contentType,  ResponseHandler<T> responseHandler) {
        Args.notNull(responseHandler, "responseHandler");
        Request request = getPostRequest(url, formMap, headerMap, contentType);
        return executeSingle(request, 0, responseHandler);
    }

    /**
     * 处理httppost请求
     *
     * @param url
     * @param formMap
     * @param contentType 返回值内容类型
     * @return
     */
    public ServiceResponse<String> executePost(String url, Map<String, String> formMap, ContentType contentType) {
        return executePost(url, formMap, contentType, responseHandler);
    }


    /**
     * 处理httppost请求,带自定义回调处理逻辑
     *
     * @param url
     * @param formMap
     * @param contentType 返回值内容类型
     * @param <T>
     * @return
     */
    public <T> ServiceResponse<T> executePost(String url, Map<String, String> formMap, ContentType contentType,
                                              ResponseHandler<T> responseHandler) {
        return executePost(url,formMap,null,contentType,responseHandler);
    }

    /**
     * 多线程批量异步处理httpget请求
     *
     * @param urlList     请求url集合
     * @param maxWaitTime 每个请求等待处理的最长时间：<=0时，表示没有过期要求,使用系统默认最长过期时间
     * @return
     * @author liuhuiqing DateTime 2014-6-24 上午9:34:43
     */
    public List<ServiceResponse<String>> executeAsyHttpGet(List<String> urlList,
                                                           int maxWaitTime) {
        return executeAsyHttpGet(urlList, maxWaitTime, responseHandler);
    }

    /**
     * 多线程批量异步处理httpget请求,带自定义回调处理逻辑
     *
     * @param urlList
     * @param maxWaitTime
     * @param responseHandler
     * @return
     * @author liuhuiqing DateTime 2014-6-24 上午11:24:01
     */
    public <T> List<ServiceResponse<T>> executeAsyHttpGet(List<String> urlList,
                                                          int maxWaitTime, ResponseHandler<T> responseHandler) {
        return executeAsyHttpGet(urlList,null,maxWaitTime,responseHandler);
    }

    /**
     * 多线程批量异步处理httpget请求,带自定义回调处理逻辑
     *
     * @param urlList
     * @param maxWaitTime
     * @param responseHandler
     * @return
     * @author liuhuiqing DateTime 2014-6-24 上午11:24:01
     */
    public <T> List<ServiceResponse<T>> executeAsyHttpGet(List<String> urlList,Map<String, String> headerMap,
                                                          int maxWaitTime, ResponseHandler<T> responseHandler) {
        Args.notNull(responseHandler, "responseHandler");
        // 初始化request请求
        Request[] requests = initAysHttpGet(urlList,headerMap);
        return executeBatch(maxWaitTime, responseHandler, requests);
    }

    /**
     * 执行一个http请求
     *
     * @param request
     * @param maxWaitTime     最长等待时间
     * @param responseHandler 回调处理结果
     * @return
     * @author liuhuiqing DateTime 2014-6-24 下午1:38:18
     */
    private <T> ServiceResponse<T> executeSingle(Request request, int maxWaitTime,
                                                 ResponseHandler<T> responseHandler) {
        ServiceResponse<T> result = null;
        Future<T> future = null;
        ExecutorService threadpool = Executors.newSingleThreadExecutor();
        int waitTime = maxWaitTime;
        // 校正过期时间设置,为防止连接长期占有,在此设置默认过期时间
        if (waitTime <= 0) {
            waitTime = defaultTimeOut;
        }
        try {
            // 异步调用工具类
            Async async = Async.newInstance().use(executor).use(threadpool);
            future = async.execute(
                    request,
                    responseHandler,
                    new HttpClientFutureCallback<T>(request.toString(), System
                            .currentTimeMillis()));
            T t = future.get(waitTime, TimeUnit.MILLISECONDS);
            result = new ServiceResponse<T>(t);
        } catch (TimeoutException e) {
            logger.error("http请求超时， 执行时间超过指定的最大等待时间[{}ms]！", waitTime);
            result = new ServiceResponse<T>(BaseResponseCode.FAILURE.getCode(), "http请求超时", e.getMessage());
            if (future != null) {
                future.cancel(true);
            }
            request.abort();// 防止线程阻塞
        } catch (InterruptedException e) {
            logger.error("http请求运行线程已中断！！", e);
            result = new ServiceResponse<T>(BaseResponseCode.FAILURE.getCode(), "http请求运行线程已中断", e.getMessage());
            if (future != null) {
                future.cancel(true);
            }
            request.abort();
        } catch (Exception e) {
            logger.error("http请求执行出现异常！", e);
            result = new ServiceResponse<T>(BaseResponseCode.FAILURE.getCode(), "http请求执行出现异常", e.getMessage());
            if (future != null) {
                future.cancel(true);
            }
            request.abort();
        } finally {
            if (threadpool != null) {
                threadpool.shutdown();
            }
        }
        return result;
    }

    /**
     * 批量执行http请求
     *
     * @param maxWaitTime     最长等待时间
     * @param responseHandler
     * @param requests
     * @return
     * @author liuhuiqing DateTime 2014-6-24 下午1:45:35
     */
    private <T> List<ServiceResponse<T>> executeBatch(int maxWaitTime,
                                                      ResponseHandler<T> responseHandler, Request... requests) {
        // 线程过量保护
        int len = requests.length;
        int nThreads = len > maxPoolNum ? maxPoolNum : len;
        ExecutorService threadpool = Executors.newFixedThreadPool(nThreads);
        // 异步调用工具类
        Async async = Async.newInstance().use(executor).use(threadpool);
        List<ServiceResponse<T>> resultList = null;
        try {
            Queue<Future<T>> queue = new LinkedList<Future<T>>();
            final long startTime = System.currentTimeMillis();
            for (final Request request : requests) {
                Future<T> future = async.execute(request, responseHandler,
                        new HttpClientFutureCallback<T>(request.toString(),
                                startTime));
                queue.add(future);
            }
            int count = 0;
            int waitTime = maxWaitTime;
            // 校正过期时间设置,为防止连接长期占有,在此设置默认过期时间
            if (waitTime <= 0) {
                waitTime = defaultTimeOut;
            }
            resultList = new ArrayList<ServiceResponse<T>>();
            while (!queue.isEmpty()) {
                Future<T> future = queue.remove();
                ServiceResponse<T> result = null;
                try {
                    T t = future.get(waitTime, TimeUnit.MILLISECONDS);
                    result = new ServiceResponse<T>(t);
                } catch (TimeoutException e) {
                    logger.error("http请求超时， 执行时间超过指定的最大等待时间[{}ms]", waitTime);
                    result = new ServiceResponse<T>(BaseResponseCode.FAILURE.getCode(), "http请求超时", e.getMessage());
                    future.cancel(true);
                    requests[count].abort();// 防止线程阻塞
                } catch (InterruptedException e) {
                    logger.error("http请求运行线程已中断！", e);
                    result = new ServiceResponse<T>(BaseResponseCode.FAILURE.getCode(), "http请求运行线程已中断", e.getMessage());
                    future.cancel(true);
                    requests[count].abort();
                } catch (Exception e) {
                    logger.error("http请求执行出现异常！", e);
                    result = new ServiceResponse<T>(BaseResponseCode.FAILURE.getCode(), "http请求执行出现异常", e.getMessage());
                    future.cancel(true);
                    requests[count].abort();
                }
                resultList.add(result);
                count++;
            }
        } finally {
            if (threadpool != null) {
                threadpool.shutdown();
            }
        }
        return resultList;
    }

    /**
     * 初始化参数
     *
     * @param urlList 异步请求url集合
     @param headerMap
     * @return
     * @author liuhuiqing DateTime 2014-6-19 下午5:01:24
     */
    private Request[] initAysHttpGet(List<String> urlList,Map<String, String> headerMap) {
        Args.notEmpty(urlList, "urlList");
        // 根据请求链接数调整线程池大小
        int size = urlList.size();
        Request[] requests = new Request[size];
        for (int i = 0; i < size; i++) {
            requests[i] = getRequest(urlList.get(i),headerMap);
        }
        return requests;
    }

    /**
     * map转换成NameValuePair
     *
     * @param map
     * @return
     * @author liuhuiqing DateTime 2014-6-19 下午3:59:03
     */
    public static List<NameValuePair> mapToPair(Map<String, String> map) {
        List<NameValuePair> nameValuePairList = null;
        if (map != null && !map.isEmpty()) {
            Form form = Form.form();
            for (Map.Entry<String, String> entry : map.entrySet()) {
                form.add(entry.getKey(), entry.getValue());
            }
            nameValuePairList = form.build();
        }
        return nameValuePairList;
    }

    /**
     * map转换成json格式字符串
     *
     * @param map
     * @return
     */
    public static String mapToJsonString(Map<String, String> map) {
        StringBuilder sb = new StringBuilder("{");
        if (map != null && !map.isEmpty()) {
            for (Map.Entry<String, String> entry : map.entrySet()) {
                sb.append(entry.getKey()).append(":").append(entry.getValue()).append(",");
            }
            sb.deleteCharAt(sb.length() - 1);
        }
        sb.append("}");
        return sb.toString();
    }

    /**
     * 获得url的主域名
     *
     * @param url
     * @return
     * @author liuhuiqing DateTime 2014-6-23 下午3:35:49
     */
    public static String getHostName(String url) {
        String result = null;
        if (StringUtils.isEmpty(url)) {
            return result;
        }
        Pattern p = Pattern.compile(
                "(?<=http://|\\.)[^.]*?\\.(com|cn|net|org|biz|info|cc|tv)",
                Pattern.CASE_INSENSITIVE);
        Matcher matcher = p.matcher(url);
        if (matcher.find()) {
            result = matcher.group();
        }
        System.out.println(result);
        return result;
    }

    public static void main(String[] args) throws IOException {
//        long startTime = System.currentTimeMillis();
//        List<String> list = new ArrayList<String>();
//        for (int i = 0; i < 100; i++) {
//            list.add("http://ip.jd.com/lookup.action");
//            list.add("http://iframe.ip138.com/ic.asp");
////			Result<String> r = HttpClientExecutor.newInstance().executeGet(
////					"http://iframe.ip138.com/ic.asp");
////			System.out.println(r.isRet() + (r.isRet() ? "" : r.getRetCode()));
//            startTime = System.currentTimeMillis();
//            ServiceResponse<String> result =
//                    HttpClientExecutor.newInstance().executeGet("http://apis.map.qq.com/ws/direction/v1/tricycle?from=39.982908,116.308994&to=39.978172,116.335602&key=TJ3BZ-2VN3D-6LW4P-P2V2R-J3SLK-OOFSS");
//
//            System.out.println("cost1:" + (System.currentTimeMillis() - startTime)
//                    + "ms"+result.isSuccess() + (result.isSuccess() ? "" : result.getMsg()));
//        }
//        List<ServiceResponse<String>> resultList =
//                HttpClientExecutor.newInstance().executeAsyHttpGet(list, 0);
//        for (ServiceResponse<String> r : resultList) {
//            System.out.println(r.isSuccess() + (r.isSuccess() ? "" : r.getMsg()));
//        }
//        System.out.println("cost1:" + (System.currentTimeMillis() - startTime)
//                + "ms");
//        startTime = System.currentTimeMillis();
//        ServiceResponse<String> result =
//                HttpClientExecutor.newInstance().executeGet("http://apis.map.qq.com/ws/direction/v1/tricycle?from=39.982908,116.308994&to=39.978172,116.335602&key=TJ3BZ-2VN3D-6LW4P-P2V2R-J3SLK-OOFSS");
//            System.out.println("cost1:" + (System.currentTimeMillis() - startTime)
//                    + "ms"+result.isSuccess() + (result.isSuccess() ? "" : result.getMsg()));

        Map<String,String> map = new HashMap<String, String>();
        map.put("timestamp", "1");
        map.put("verificationHash", "imdada");
        map.put("token", "0b1f37958677e32f22f2bb04cc70e8f5");

        try {
            System.err.println(Request.Post("http://www.ems.com.cn/ems/tool/altlnew/queryHazy")
                    .addHeader("X-Custom-header", "stuff")
//                    .useExpectContinue()
//                    .version(HttpVersion.HTTP_1_1)
//                    .viaProxy(new HttpHost("myproxy", 80))
                    .bodyForm(Form.form().add("sendCityCode", "北京").add("rcvCityCode", "北京").add("sendDate", "2016-07-07").build(), Consts.UTF_8)
                    .execute().returnContent().asString());
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.err.println(JsonUtils.toJson(HttpClientExecutor.newInstance().executePost("http://dadacst.dev.imdada.cn/v1_0/dada/token/verify/", map)));
        map.put("sendCityCode", "北京");
        map.put("rcvCityCode", "北京");
        map.put("sendDate", "2016-07-07");
        System.err.println(JsonUtils.toJson(HttpClientExecutor.newInstance().executePost("http://www.ems.com.cn/ems/tool/altlnew/queryHazy", map)));
    }

}
