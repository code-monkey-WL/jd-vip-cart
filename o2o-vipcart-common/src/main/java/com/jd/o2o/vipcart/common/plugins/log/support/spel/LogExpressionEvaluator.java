package com.jd.o2o.vipcart.common.plugins.log.support.spel;

import com.jd.o2o.vipcart.common.plugins.cache.aspect.support.spel.LazyParamAwareEvaluationContext;
import org.springframework.core.LocalVariableTableParameterNameDiscoverer;
import org.springframework.core.ParameterNameDiscoverer;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.Expression;
import org.springframework.expression.spel.standard.SpelExpressionParser;

import java.lang.reflect.Method;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 表达式解析实现
 * Created by liuhuiqing on 2015/10/30.
 */
public class LogExpressionEvaluator {

    private SpelExpressionParser parser = new SpelExpressionParser();

    // shared param discoverer since it caches data internally
    private ParameterNameDiscoverer paramNameDiscoverer = new LocalVariableTableParameterNameDiscoverer();

    private Map<String, Expression> keyCache = new ConcurrentHashMap<String, Expression>();

    private Map<String, Method> targetMethodCache = new ConcurrentHashMap<String, Method>();


    public EvaluationContext createEvaluationContext(
           Method method, Object[] args, Object target, Class<?> targetClass) {

        LogExpressionRootObject rootObject =
                new LogExpressionRootObject(method, args, target, targetClass);
        return new LazyParamAwareEvaluationContext(rootObject,
                this.paramNameDiscoverer, method, args, targetClass, this.targetMethodCache);
    }

    public Object key(String keyExpression, Method method, EvaluationContext evalContext) {
        String key = toString(method, keyExpression);
        Expression keyExp = this.keyCache.get(key);
        if (keyExp == null) {
            keyExp = this.parser.parseExpression(keyExpression);
            this.keyCache.put(key, keyExp);
        }
        return keyExp.getValue(evalContext);
    }

    private String toString(Method method, String expression) {
        StringBuilder sb = new StringBuilder();
        sb.append(method.getDeclaringClass().getName());
        sb.append("#");
        sb.append(method.toString());
        sb.append("#");
        sb.append(expression);
        return sb.toString();
    }
}
