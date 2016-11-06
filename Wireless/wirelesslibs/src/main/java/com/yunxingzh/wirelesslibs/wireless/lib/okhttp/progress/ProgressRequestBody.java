package com.yunxingzh.wirelesslibs.wireless.lib.okhttp.progress;

import com.yunxingzh.wirelesslibs.wireless.lib.okhttp.OkRequestParams;

import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import okio.Buffer;
import okio.BufferedSink;
import okio.ForwardingSink;
import okio.Okio;
import okio.Sink;

/**
 * Created by lidongyang on 2016/5/11.
 */
public class ProgressRequestBody extends RequestBody {

    protected RequestBody requestBody;
    protected OkRequestParams requestBodys;
    protected final ProgressListener progressListener;
    protected BufferedSink bufferedSink;

    public ProgressRequestBody(RequestBody requestBody, ProgressListener progressListener) {
        this.requestBody = requestBody;
        this.progressListener = progressListener;
    }

    public ProgressRequestBody(OkRequestParams requestBodys, ProgressListener progressListener) {
        this.requestBodys = requestBodys;
        this.progressListener = progressListener;
    }

    @Override
    public MediaType contentType() {
        return requestBody.contentType();
    }

    @Override
    public long contentLength() throws IOException  {
        return requestBody.contentLength();
    }

    @Override
    public void writeTo(BufferedSink sink) throws IOException {
        if (bufferedSink == null) {
            bufferedSink = Okio.buffer(sink(sink));
        }
        requestBody.writeTo(bufferedSink);
        bufferedSink.flush();
    }

    private Sink sink(Sink sink) {
        return new ForwardingSink(sink) {
            private long bytesWritten = 0;
            private long contentLength = 0;
            @Override
            public void write(Buffer source, long byteCount) throws IOException {
                super.write(source, byteCount);
                if (contentLength <= 0) {
                    contentLength = contentLength();
                }
                bytesWritten += byteCount;
                if (progressListener != null) {
                    progressListener.update(bytesWritten, contentLength, bytesWritten == contentLength);
                }
            }
        };
    }
}