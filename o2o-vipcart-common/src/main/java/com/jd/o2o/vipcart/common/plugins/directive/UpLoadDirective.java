package com.jd.o2o.vipcart.common.plugins.directive;

import com.jd.o2o.vipcart.common.plugins.directive.model.UpDownloadModel;
import com.jd.o2o.vipcart.common.plugins.directive.utils.UpDownloadUtil;
import org.apache.velocity.context.InternalContextAdapter;
import org.apache.velocity.exception.MethodInvocationException;
import org.apache.velocity.exception.ParseErrorException;
import org.apache.velocity.exception.ResourceNotFoundException;
import org.apache.velocity.runtime.parser.node.Node;

import java.io.IOException;
import java.io.Writer;

/**
 * 上传文件标签
 * Created by liuhuiqing on 2015/6/1.
 */
public class UpLoadDirective extends org.apache.velocity.runtime.directive.Directive {

    @Override
    public String getName() {
        return "fileUpLoad";
    }

    @Override
    public int getType() {
        return LINE;
    }

    @Override
    public boolean render(InternalContextAdapter internalContextAdapter, Writer writer, Node node) throws IOException, ResourceNotFoundException, ParseErrorException, MethodInvocationException {
        String resCode = null;
        String extString = null;
        String actionString = "/fileUpload/upload";

        Node resCodeNode = node.jjtGetChild(0);
        if (resCodeNode != null) {
            resCode = String.valueOf(resCodeNode.value(internalContextAdapter));
        }
        Node extStringNode = node.jjtGetChild(1);
        if (extStringNode != null) {
            extString = String.valueOf(extStringNode.value(internalContextAdapter));
        }
        Node actionStringNode = node.jjtGetChild(2);
        if (actionStringNode != null) {
            actionString = String.valueOf(actionStringNode.value(internalContextAdapter));
        }
        writer.write(UpDownloadUtil.buildUploadHtml(buildUpDownloadModel(resCode, extString, actionString)));
        return true;
    }

    private UpDownloadModel buildUpDownloadModel(String resCode, String ext, String action) {
        UpDownloadModel upDownloadModel = new UpDownloadModel();
        upDownloadModel.setAction(action);
        upDownloadModel.setExt(ext);
        upDownloadModel.setResCode(resCode);
        return upDownloadModel;
    }
}
