/*
 * Copyright 2017 Benik Arakelyan
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.jsunsoft.http;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.entity.ContentType;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;

final class BasicResponseBodyReaderContext<T> implements ResponseBodyReaderContext<T> {
    private final HttpResponse httpResponse;
    private final Class<T> type;
    private final Type genericType;

    BasicResponseBodyReaderContext(HttpResponse httpResponse, Class<T> type, Type genericType) {
        this.httpResponse = ArgsCheck.notNull(httpResponse, "httpResponse");
        this.type = ArgsCheck.notNull(type, "type");
        this.genericType = ArgsCheck.notNull(genericType, "genericType");
    }

    @Override
    public InputStream getContent() throws IOException {
        return httpResponse.getEntity().getContent();
    }

    @Override
    public String getContentAsString() throws IOException {
        return ResponseBodyReader.stringReader().read(new BasicResponseBodyReaderContext<>(httpResponse, String.class, String.class));
    }

    @Override
    public ContentType getContentType() {
        return ContentType.get(getHttpEntity());
    }

    @Override
    public long getContentLength() {
        return getHttpEntity().getContentLength();
    }

    @Override
    public HttpEntity getHttpEntity() {
        return httpResponse.getEntity();
    }

    @Override
    public Class<T> getType() {
        return type;
    }

    @Override
    public Type getGenericType() {
        return genericType;
    }

    @Override
    public StatusLine getStatusLine() {
        return httpResponse.getStatusLine();
    }

    @Override
    public boolean hasEntity() {
        return getHttpEntity() != null;
    }
}
