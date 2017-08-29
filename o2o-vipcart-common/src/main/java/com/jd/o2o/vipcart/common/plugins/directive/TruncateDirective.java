package com.jd.o2o.vipcart.common.plugins.directive;

import org.apache.velocity.runtime.parser.node.Node;
import org.apache.velocity.context.InternalContextAdapter;
import org.apache.velocity.exception.MethodInvocationException;
import org.apache.velocity.exception.ParseErrorException;
import org.apache.velocity.exception.ResourceNotFoundException;
import org.apache.velocity.runtime.directive.Directive;

import java.io.IOException;
import java.io.Writer;

/**
 * 字符串截取
 * 例子：#truncate(Object truncateMe, int maxLength, String suffix, boolean truncateAtWord)
 * Created by liuhuiqing on 2015/6/1.
 */
public class TruncateDirective extends Directive {
    public String getName() {
        return "truncate";
    }

    public int getType() {
        return LINE;
    }

    public boolean render(InternalContextAdapter context, Writer writer, Node node)
            throws IOException, ResourceNotFoundException, ParseErrorException, MethodInvocationException {

        // 待截取字符串
        String truncateMe = null;
        // 保留最大字符串长度
        int maxLength = 10;
        // 为截取后的字符串添加后缀
        String suffix = null;
        // 截取字符串是否保留到单词级别
        Boolean truncateAtWord = false;

        //reading params
        Node truncateMeNode = node.jjtGetChild(0);
        if (truncateMeNode != null) {
            truncateMe = String.valueOf(truncateMeNode.value(context));
        }
        Node maxLengthNode = node.jjtGetChild(1);
        if (maxLengthNode != null) {
            maxLength = (Integer) maxLengthNode.value(context);
        }
        Node suffixNode = node.jjtGetChild(2);
        if (suffixNode != null) {
            suffix = String.valueOf(suffixNode.value(context));
        }
        Node truncateAtWordNode = node.jjtGetChild(3);
        if (truncateAtWordNode != null) {
            truncateAtWord = (Boolean) truncateAtWordNode.value(context);
        }

        //truncate and write result to writer
        writer.write(truncate(truncateMe, maxLength, suffix, truncateAtWord));

        return true;

    }

    //does actual truncating (taken directly from DisplayTools)
    public String truncate(String truncateMe, int maxLength, String suffix, boolean truncateAtWord) {
        if (truncateMe == null || maxLength <= 0) {
            return null;
        }

        if (truncateMe.length() <= maxLength) {
            return truncateMe;
        }
        if (suffix == null || maxLength - suffix.length() <= 0) {
            // either no need or no room for suffix
            return truncateMe.substring(0, maxLength);
        }
        if (truncateAtWord) {
            // find the latest space within maxLength
            int lastSpace = truncateMe.substring(0, maxLength - suffix.length() + 1).lastIndexOf(" ");
            if (lastSpace > suffix.length()) {
                return truncateMe.substring(0, lastSpace) + suffix;
            }
        }
        // truncate to exact character and append suffix
        return truncateMe.substring(0, maxLength - suffix.length()) + suffix;

    }
}
