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
import org.apache.http.entity.ContentType;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.io.InputStream;

import static java.nio.charset.StandardCharsets.UTF_8;

final class BasicResponseContext implements ResponseContext {
    private final HttpEntity httpEntity;

    BasicResponseContext(HttpEntity httpEntity) {
        this.httpEntity = ArgsCheck.notNull(httpEntity, "httpEntity");
    }

    @Override
    public InputStream getContent() throws IOException {
        return httpEntity.getContent();
    }

    @Override
    public String getContentAsString() throws IOException {
        return EntityUtils.toString(httpEntity, UTF_8);
    }

    @Override
    public ContentType getContentType() {
        return ContentType.get(httpEntity);
    }

    @Override
    public long getContentLength() {
        return httpEntity.getContentLength();
    }
}
