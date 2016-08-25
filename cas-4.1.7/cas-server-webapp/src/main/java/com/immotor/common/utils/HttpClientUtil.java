package com.immotor.common.utils;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.apache.http.HttpEntity;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.StatusLine;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.entity.ContentProducer;
import org.apache.http.entity.EntityTemplate;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * HTTP客户端,实现最基本的GET,POST请求,返回值类型均为JSON字符串
 * 
 */
public abstract class HttpClientUtil {

    private static final Logger   logger         = LoggerFactory.getLogger(HttpClientUtil.class);
    protected static final String defaultCharset = "UTF-8";                                      // 默认编码

    /**
     * 发起HTTP GET请求
     * 
     * @param url URL
     * @return 响应结果
     */
    public static final String get(String url) {
        return execute(new HttpGet(url));
    }

    /**
     * 发起HTTP GET请求
     * 
     * @param charset 编码
     * @param uri URI
     * @param params 请求参数
     * @return 响应结果
     */
    public static final String get(String charset, String uri, List<NameValuePair> params) {
        String queryString = URLEncodedUtils.format(params, charset);
        return get(uri + "?" + queryString);
    }

    /**
     * 发起HTTP GET请求
     * 
     * @param charset 编码
     * @param uri URI
     * @param params 请求参数
     * @return 响应结果
     */
    public static final String get(String charset, String uri, NameValuePair... params) {
        if (params.length == 0) {
            return get(uri);
        } else {
            return get(charset, uri, Arrays.asList(params));
        }
    }

    /**
     * 发起HTTP GET请求
     * 
     * @param charset 编码
     * @param uri URI
     * @param params 请求参数
     * @return 响应结果
     */
    public static final String get(String charset, String uri, Map<String, Object> params) {
        if (Utils.isEmpty(params)) {
            return get(uri);
        } else {
            return get(charset, uri, mapToNameValuePairs(params));
        }
    }

    /**
     * 发起HTTP GET请求(UTF-8)
     * 
     * @param uri URI
     * @param params 请求参数
     * @return 响应结果
     */
    public static final String get(String uri, List<NameValuePair> params) {
        return get(defaultCharset, uri, params);
    }

    /**
     * 发起HTTP GET请求(UTF-8)
     * 
     * @param uri URI
     * @param params 请求参数
     * @return 响应结果
     */
    public static final String get(String uri, NameValuePair... params) {
        return get(defaultCharset, uri, params);
    }

    /**
     * 发起HTTP GET请求(UTF-8)
     * 
     * @param uri URI
     * @param params 请求参数
     * @return 响应结果
     */
    public static final String get(String uri, Map<String, Object> params) {
        return get(defaultCharset, uri, params);
    }

    /**
     * 发起HTTP POST请求
     * 
     * @param url URL
     * @return 响应结果
     */
    public static final String post(String url) {
        return execute(new HttpPost(url));
    }

    /**
     * 发起HTTP POST请求
     * 
     * @param charset 编码
     * @param uri URI
     * @param params 请求参数
     * @return 响应结果
     */
    public static final String post(String charset, String uri, List<NameValuePair> params) {
        try {
            UrlEncodedFormEntity entity = new UrlEncodedFormEntity(params, charset);
            HttpPost httppost = new HttpPost(uri);
            httppost.setEntity(entity);
            return execute(httppost);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException("不支持的编码类型: " + charset, e);
        }
    }

    /**
     * 发起HTTP POST请求
     * 
     * @param charset 编码
     * @param uri URI
     * @param params 请求参数
     * @return 响应结果
     */
    public static final String post(String charset, String uri, NameValuePair... params) {
        if (params.length == 0) {
            return post(uri);
        } else {
            return post(charset, uri, Arrays.asList(params));
        }
    }

    /**
     * 发起HTTP POST请求
     * 
     * @param charset 编码
     * @param uri URI
     * @param params 请求参数
     * @return 响应结果
     */
    public static final String post(String charset, String uri, Map<String, Object> params) {
        if (Utils.isEmpty(params)) {
            return post(uri);
        } else {
            return post(charset, uri, mapToNameValuePairs(params));
        }
    }

    /**
     * 发起HTTP POST请求(UTF-8)
     * 
     * @param uri URI
     * @param params 请求参数
     * @return 响应结果
     */
    public static final String post(String uri, List<NameValuePair> params) {
        return post(defaultCharset, uri, params);
    }

    /**
     * 发起HTTP POST请求(UTF-8)
     * 
     * @param uri URI
     * @param params 请求参数
     * @return 响应结果
     */
    public static final String post(String uri, NameValuePair... params) {
        return post(defaultCharset, uri, params);
    }

    /**
     * 发起HTTP POST请求(UTF-8)
     * 
     * @param uri URI
     * @param params 请求参数
     * @return 响应结果
     */
    public static final String post(String uri, Map<String, Object> params) {
        return post(defaultCharset, uri, params);
    }

    /**
     * 发起HTTP POST请求，
     * @param uri URI
     * @param streamStr 输出流请求参数
     * @return 响应结果
     */
    public static final String postStream(String uri, final String streamStr) {
        ContentProducer cp = new ContentProducer() {
            public void writeTo(OutputStream outstream) throws IOException {
                Writer writer = new OutputStreamWriter(outstream, "UTF-8");
                writer.write(streamStr);
                writer.flush();
            }
        };
        HttpEntity entity = new EntityTemplate(cp);
        HttpPost httppost = new HttpPost(uri);
        httppost.setEntity(entity);
        return execute(httppost);
    }
    
    
    /**
     * 发起HTTP POST请求.
     * @param uri URI.
     * @param xmlParam XML请求参数.
     * @return 响应结果.
     */
    public static final String postXml(String uri, String xmlParam) {
        HttpPost httppost = new HttpPost(uri);
        httppost.addHeader("Content-Type","text/xml; charset=UTF-8");
        httppost.setEntity(new StringEntity(xmlParam, "UTF-8"));
        return execute(httppost);
    }
    
    /**
     * 发起HTTP POST请求，
     * @param uri URI
     * @param jsonParam json请求参数
     * @return 响应结果
     */
    public static final String postJson(String uri, String jsonParam) {
        HttpPost httppost = new HttpPost(uri);
        StringEntity entity = new StringEntity(jsonParam,"utf-8");//解决中文乱码问题    
        entity.setContentEncoding("UTF-8");    
        entity.setContentType("application/json");    
        httppost.setEntity(entity);
        return execute(httppost);
    }
    
    /**
     * 将Map转为NameValuePair集合,将过滤空键或空值
     * 
     * @param map Map对象
     * @return NameValuePair集合
     */
    private static final List<NameValuePair> mapToNameValuePairs(Map<?, ?> map) {
        List<NameValuePair> nameValuePairs = Utils.newList(map.size());
        Object key = null, value = null;
        for (Entry<?, ?> entry : map.entrySet()) {
            key = entry.getKey();
            value = entry.getValue();
            if (!Utils.isEmpty(key) && !Utils.isEmpty(value)) {
                BasicNameValuePair nameValuePair = new BasicNameValuePair(Utils.toString(key), Utils.toString(value));
                nameValuePairs.add(nameValuePair);
            }
        }
        return nameValuePairs;
    }
    
    
    /**
     * 发起HTTP请求
     * 
     * @param request HTTP请求
     * @return 响应结果
     */
    private static final String execute(HttpUriRequest request) {
        String body = "";
        CloseableHttpClient client = null;
        CloseableHttpResponse response = null;
        try {
          
            client = createHttpCilent();
//            client = HttpClientBuilder.create().build();
            response = client.execute(request);
            if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                HttpEntity entity = response.getEntity();
                body = EntityUtils.toString(entity, "UTF-8");
                EntityUtils.consumeQuietly(entity);
            } else {
                String formatter = "HTTP请求失败: %s, status: %s %s";
                StatusLine statusLine = response.getStatusLine();
                String msg = String.format(formatter, request.getRequestLine(), statusLine.getStatusCode(), statusLine.getReasonPhrase());
                throw new RuntimeException(msg);
            }
        } catch (IOException e) {
            throw new RuntimeException("HTTP请求异常: " + request.getRequestLine(), e);
        } catch (KeyManagementException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally {
            try {
                if (response != null) {
                    response.close();
                }
                if (client != null) {
                    client.close();
                }
            } catch (IOException e) {
                logger.warn("Release resources failed!", e);
            }
        }
        return body;
    }
    
    /** 
     * 绕过验证 
     *   
     * @return 
     * @throws NoSuchAlgorithmException  
     * @throws KeyManagementException  
     */  
    public static SSLContext createIgnoreVerifySSL() throws NoSuchAlgorithmException, KeyManagementException {  
        SSLContext sc = SSLContext.getInstance("SSLv3");  
      
        // 实现一个X509TrustManager接口，用于绕过验证，不用修改里面的方法  
        X509TrustManager trustManager = new X509TrustManager() {  
            @Override  
            public void checkClientTrusted(  
                    java.security.cert.X509Certificate[] paramArrayOfX509Certificate,  
                    String paramString) throws CertificateException {  
            }  
      
            @Override  
            public void checkServerTrusted(  
                    java.security.cert.X509Certificate[] paramArrayOfX509Certificate,  
                    String paramString) throws CertificateException {  
            }  
      
            @Override  
            public java.security.cert.X509Certificate[] getAcceptedIssuers() {  
                return null;  
            }  
        };  
      
        sc.init(null, new TrustManager[] { trustManager }, null);  
        return sc;  
    }
    public static CloseableHttpClient createHttpCilent() throws KeyManagementException, NoSuchAlgorithmException{
        CloseableHttpClient client  = null;
        //采用绕过验证的方式处理https请求  
        SSLContext sslcontext = createIgnoreVerifySSL();  
        // 设置协议http和https对应的处理socket链接工厂的对象  
        Registry<ConnectionSocketFactory> socketFactoryRegistry = RegistryBuilder.<ConnectionSocketFactory>create()
                .register("http", PlainConnectionSocketFactory.INSTANCE)
                .register("https", new SSLConnectionSocketFactory(sslcontext)).build();
        PoolingHttpClientConnectionManager connManager = new PoolingHttpClientConnectionManager(socketFactoryRegistry);  
        HttpClients.custom().setConnectionManager(connManager);  
        client = HttpClients.custom().setConnectionManager(connManager).build();
        return client;
    }
    
    
    /**
     * app 流量查询专用
     */
    public static final String getJsonAuth(String uri, String jsonParam, String token) {
        HttpPost httppost = new HttpPost(uri);
        StringEntity entity = new StringEntity(jsonParam,"utf-8");//解决中文乱码问题    
        entity.setContentEncoding("UTF-8");    
        entity.setContentType("application/json"); 
        httppost.setHeader("Authorization",token);
        httppost.setEntity(entity);
        return execute(httppost);
    }
    
    /**
     * 发起HTTP GET请求
     */
    public static final String authGet(String url, String token) {
        HttpGet get =  new HttpGet(url);
        get.setHeader("Authorization", token);
        return execute(get);
    }
    
    /**
     * 发起HTTP GET请求
     */
    public static final String authGet(String uri, Map<String, Object> params, String token) {
        if (Utils.isEmpty(params)) {
            return authGet(uri, token);
        } else {
            String queryString = URLEncodedUtils.format(mapToNameValuePairs(params), defaultCharset);
            return authGet(uri + "?" + queryString, token);
        }
    }
}
