package com.jd.o2o.vipcart.common.plugins.httpcliend.manage;

import org.apache.http.Consts;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpResponseException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;

/**
 * Class Name: HttpClientResponseHandler.java
 *
 * @author liuhuiqing DateTime 2014-6-19 下午2:29:18
 * @version 1.0
 */
public class HttpClientResponseHandler implements ResponseHandler<String> {
    private Charset charset;

    public HttpClientResponseHandler() {
        super();
    }

    public HttpClientResponseHandler(Charset charset) {
        super();
        this.charset = charset;
    }


    @Override
    public String handleResponse(HttpResponse response)
            throws ClientProtocolException, IOException {
        final StatusLine statusLine = response.getStatusLine();
        final HttpEntity entity = response.getEntity();
        String result = null;
        if (statusLine.getStatusCode() >= 300) {
            throw new HttpResponseException(statusLine.getStatusCode(),
                    statusLine.getReasonPhrase());
        }
        if (entity != null) {
            byte[] b = EntityUtils.toByteArray(entity);
            if (charset == null) {
                charset = Consts.UTF_8;
            }
            try {
                result = new String(b, charset.name());
            } catch (final UnsupportedEncodingException ex) {
                result = new String(b);
            }
        }
        return result;
    }

}
