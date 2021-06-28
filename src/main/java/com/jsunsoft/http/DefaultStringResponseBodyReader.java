package com.jsunsoft.http;

import org.apache.http.entity.ContentType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;

import static java.nio.charset.StandardCharsets.UTF_8;

class DefaultStringResponseBodyReader implements ResponseBodyReader<String> {
    private static final Logger LOGGER = LoggerFactory.getLogger(DefaultStringResponseBodyReader.class);

    static final ResponseBodyReader<String> INSTANCE = new DefaultStringResponseBodyReader();

    @Override
    public boolean isReadable(ResponseBodyReadableContext bodyReadableContext) {
        return bodyReadableContext.getType() == String.class;
    }

    /**
     * @param bodyReaderContext the response context.
     *
     * @return content as {@link String}
     *
     * @throws IOException                   if the stream could not be created or
     *                                       if the first byte cannot be read for any reason other than the end of the file,
     *                                       if the input stream has been closed, or if some other I/O
     * @throws InvalidContentLengthException If content length exceeds {@link Integer#MAX_VALUE Integer.MAX_VALUE}
     * @throws UnsupportedEncodingException  If the named charset is not supported
     */
    @Override
    public String read(ResponseBodyReaderContext bodyReaderContext) throws IOException {

        long startTime = System.currentTimeMillis();

        String result = null;
        InputStream inputStream = bodyReaderContext.getContent();

        if (inputStream != null) {
            int bufferInitialSize = resolveBufferInitialSize(bodyReaderContext);
            byte[] buffer = new byte[bufferInitialSize];

            ByteArrayOutputStream outputStream = new ByteArrayOutputStream(buffer.length);

            int length;

            while ((length = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, length);
            }

            ContentType contentType = bodyReaderContext.getContentType();
            Charset charset = contentType == null || contentType.getCharset() == null ? UTF_8 : contentType.getCharset();

            result = outputStream.toString(charset.name());
        }

        LOGGER.trace("Content type is: {}", result);

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("Executed response body as string. Time: {}, length of response body: {}", HttpRequestUtils.humanTime(startTime), (result == null ? 0 : result.length()));
        }

        return result;
    }

    private int resolveBufferInitialSize(ResponseBodyReaderContext bodyReaderContext) throws IOException {
        int result;
        long contentLength = bodyReaderContext.getContentLength();
        if (contentLength > Integer.MAX_VALUE) {
            throw new InvalidContentLengthException(contentLength, "Content length is large. Content length greater than Integer.MAX_VALUE");
        }
        int integerContentLength = (int) contentLength;

        if (integerContentLength >= 0) {
            result = integerContentLength;
        } else {
            result = 1024;
        }

        return result;
    }
}
