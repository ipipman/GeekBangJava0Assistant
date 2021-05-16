/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.geektimes.rest.client;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.geektimes.rest.core.DefaultResponse;

import javax.ws.rs.HttpMethod;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.InvocationCallback;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;
import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Method;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Collection;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.Future;

/**
 * HTTP POST Method {@link Invocation}
 *
 * @author liuwei
 * @since 2021/3/31
 */
class HttpPostInvocation implements Invocation {

    private final URI uri;

    private final URL url;

    private final MultivaluedMap<String, Object> headers;

    private final Entity<?> entity;

    HttpPostInvocation(URI uri, MultivaluedMap<String, Object> headers, Entity<?> entity) {
        this.uri = uri;
        this.headers = headers;
        this.entity = entity;
        try {
            this.url = uri.toURL();
        } catch (MalformedURLException e) {
            throw new IllegalArgumentException();
        }
    }

    @Override
    public Invocation property(String name, Object value) {
        return this;
    }

    @Override
    public Response invoke() {
        HttpURLConnection connection = null;
        try {
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod(HttpMethod.POST);
            setRequestHeaders(connection);
            setRequestBody(connection);
            // TODO Set the cookies
            int statusCode = connection.getResponseCode();
            DefaultResponse response = new DefaultResponse();
            response.setConnection(connection);
            response.setStatus(statusCode);
            return response;
        } catch (IOException e) {
            // TODO Error handler
        }
        return null;
    }

    private void setRequestHeaders(HttpURLConnection connection) {

        for (Map.Entry<String, List<Object>> entry : headers.entrySet()) {
            String headerName = entry.getKey();
            for (Object headerValue : entry.getValue()) {
                connection.setRequestProperty(headerName, headerValue.toString());
            }
        }

        if (entity != null) {
            Locale language = entity.getLanguage();
            if (language != null) {
                connection.setRequestProperty(HttpHeaders.CONTENT_LANGUAGE, language.getLanguage());
            }

            String encoding = Optional.ofNullable(entity.getEncoding()).orElse("UTF-8");
            connection.setRequestProperty(HttpHeaders.CONTENT_ENCODING, encoding);

            MediaType mediaType = entity.getMediaType();
            if (mediaType != null) {
                connection.setRequestProperty(HttpHeaders.CONTENT_TYPE,
                        mediaType.getType() + "/" + mediaType.getSubtype());
            }
        }

    }

    private void setRequestBody(HttpURLConnection connection) throws IOException {
        if (entity == null || entity.getEntity() == null) {
            return;
        }

        String encoding = Optional.ofNullable(entity.getEncoding()).orElse("UTF-8");
        connection.setDoOutput(true);
        OutputStream outputStream = connection.getOutputStream();

        Object data = entity.getEntity();
        Class<?> dataClass = data.getClass();

        // 基础类型
        boolean primitive = dataClass.isPrimitive();
        if (primitive) {
            byte[] bytes = URLEncoder.encode(data.toString(), encoding).getBytes();
            outputStream.write(bytes);
            return;
        }

        // json类型
        MediaType mediaType = entity.getMediaType();
        if (Objects.equals(mediaType, MediaType.APPLICATION_JSON_TYPE)) {
            ObjectMapper objectMapper = new ObjectMapper();
            byte[] bytes = objectMapper.writeValueAsString(data).getBytes(encoding);
            outputStream.write(bytes);
            return;
        }

        if (data instanceof Collection) {
            // TODO Not support
            return;
        }

        StringBuilder postData = new StringBuilder(256);
        if (data instanceof Map) {
            Map<Object, Object> map = (Map) data;
            for (Map.Entry<Object, Object> entry : map.entrySet()) {
                appendParam(postData, entry.getKey(), entry.getValue());
            }
            byte[] bytes = postData.toString().getBytes(encoding);
            outputStream.write(bytes);
            return;
        }

        try {
            BeanInfo beanInfo = Introspector.getBeanInfo(dataClass, Object.class);
            PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
            for (PropertyDescriptor propertyDescriptor : propertyDescriptors) {
                Method readMethod = propertyDescriptor.getReadMethod();
                String name = propertyDescriptor.getName();
                Object value = readMethod.invoke(data);
                appendParam(postData, name, value);
            }
        } catch (Throwable e) {
            throw new IllegalArgumentException(e);
        }
        byte[] bytes = postData.toString().getBytes(encoding);
        outputStream.write(bytes);

    }

    private StringBuilder appendParam(StringBuilder postData, Object key, Object value) {
        try {
            postData.append(URLEncoder.encode(String.valueOf(key), "UTF-8"));
            postData.append('=');
            postData.append(value == null ? "" : URLEncoder.encode(String.valueOf(value), "UTF-8"));
        } catch (UnsupportedEncodingException ignored) {

        }
        return postData.append("&");
    }

    @Override
    public <T> T invoke(Class<T> responseType) {
        Response response = invoke();
        return response.readEntity(responseType);
    }

    @Override
    public <T> T invoke(GenericType<T> responseType) {
        Response response = invoke();
        return response.readEntity(responseType);
    }

    @Override
    public Future<Response> submit() {
        return null;
    }

    @Override
    public <T> Future<T> submit(Class<T> responseType) {
        return null;
    }

    @Override
    public <T> Future<T> submit(GenericType<T> responseType) {
        return null;
    }

    @Override
    public <T> Future<T> submit(InvocationCallback<T> callback) {
        return null;
    }
}
