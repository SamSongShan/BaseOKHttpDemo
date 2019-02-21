package cn.com.zlct.baseokhttpdemo.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Handler;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.Headers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okio.Buffer;
import okio.BufferedSink;
import okio.Okio;
import okio.Source;

/**
 * OkHttp的工具类
 */
public class OkHttpUtil {

    private static OkHttpClient okHttpClient;
    private static Handler handler = new Handler();

    public static void initOkHttp() {
        okHttpClient = new OkHttpClient.Builder()
                .connectTimeout(60, TimeUnit.MINUTES)
                .readTimeout(60, TimeUnit.MINUTES)
                .writeTimeout(60, TimeUnit.MINUTES)
                .retryOnConnectionFailure(false)
                .build();
    }

    /**
     * 下载json
     */
    public static void getJSON(String url, OnDataListener dataListener) {
        Request request = new Request.Builder()
                .url(url)
                .build();
        Call call = okHttpClient.newCall(request);
        call.enqueue(new OkHttpCallback(url, dataListener));
    }

    /**
     * 同步get请求 -- 让子类调用
     */
    public static String getJSON(String url) throws IOException {
        Request request = new Request.Builder()
                .url(url)
                .build();
        Response Response = okHttpClient.newCall(request).execute();
        return Response.body().string();
    }

    /**
     * 下载小图
     */
    public static void getStream(final String url, final OnBytesListener bytesListener) {
        Request request = new Request.Builder()
                .url(url)
                .build();
        Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                if (bytesListener != null) {
                    Log.e("loge", "onFailure: " + url + "\n" + e.getMessage());
                    bytesListener.onFailure(url, e.getMessage());
                }
            }

            @Override
            public void onResponse(Call call, final Response response) throws IOException {
                final byte[] bytes = response.body().bytes();
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        if (bytesListener != null) {
                            bytesListener.onResponse(url, bytes);
                        }
                    }
                });
            }
        });
    }

    /**
     * 下载文件
     */
    public static void fileDownload(final String url, final String filePath, final OnProgressListener progressListener,
                                    final OnDataListener dataListener) {
        Request request = new Request.Builder()
                .url(url)
                .build();
        Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                onFail(dataListener, url, e);
            }

            @Override
            public void onResponse(Call call, final Response response) throws IOException {
                InputStream is = response.body().byteStream();
                long length = response.body().contentLength();
                OutputStream os = new FileOutputStream(new File(filePath));
                byte[] bs = new byte[1024];
                int len;
                long count = 0l;
                while ((len = is.read(bs)) != -1) {
                    count += len;
                    os.write(bs, 0, len);
                    if (count * 100 == length) {
                        progressListener.onProgress(100);

                    } else {
                        progressListener.onProgress((int) (((double) (count * 100 / length))));

                    }
                }
                is.close();
                os.close();
                onResp(dataListener, url, "suc");
            }
        });
    }

    /**
     * post提交JSON表单
     */
    public static void postJson(String url, OnDataListener dataListener) {
        final Request request = new Request.Builder()
                .url(url)
                .build();
        okHttpClient.newCall(request).enqueue(new OkHttpCallback(url, dataListener));
    }
    public static void postJson(String url, Map<String,String> inJson, OnDataListener dataListener) {
        FormBody.Builder builder = new FormBody.Builder();
        for (String in : inJson.keySet()) {
            builder.add(in, inJson.get(in));
        }
        FormBody formBody = builder.build();

        final Request request = new Request.Builder()
                .url(url)
                .post(formBody)
                .build();
        okHttpClient.newCall(request).enqueue(new OkHttpCallback(url, dataListener));
    }
    public static void postJson(String url, String inJson, OnDataListener dataListener) {
        FormBody.Builder builder = new FormBody.Builder();
        builder.add("inJson", inJson);
        FormBody formBody = builder.build();

        final Request request = new Request.Builder()
                .url(url)
                .post(formBody)
                .build();
        okHttpClient.newCall(request).enqueue(new OkHttpCallback(url, dataListener));
    }

    /**
     * post提交JSON表单 -- 检查网络状态
     */
    public static void postJson(Context context, final View view, final String url, String inJson,
                                final OnDataListener dataListener) {
        if (!NetworkUtil.isNetWorkEnable(context)) {//网络不可用
            final Snackbar snackbar = Snackbar.make(view, "当前网络不可用，请稍后再试", Snackbar.LENGTH_INDEFINITE);
            snackbar.setAction("确定", new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    snackbar.dismiss();
                }
            });
            Snackbar.SnackbarLayout snackBarLayout = (Snackbar.SnackbarLayout) snackbar.getView();
            snackBarLayout.setAlpha(0.8f);
            snackbar.show();
            if (view instanceof SwipeRefreshLayout) {
                view.post(new Runnable() {
                    @Override
                    public void run() {
                        ((SwipeRefreshLayout) view).setRefreshing(false);
                    }
                });
            }
        }
        postJson(url, inJson, dataListener);
    }

    /**
     * post提交Bitmap -- 不带进度条
     */
    public static void postStream(String url, String encode, Bitmap bitmap, OnDataListener dataListener) {
        postStream(url, encode, BitMapUtil.Bitmap2Bytes(bitmap), dataListener);
    }

    /**
     * post提交bytes -- 不带进度条
     */
    public static void postStream(String url, String encode, final byte[] bytes, OnDataListener dataListener) {
        RequestBody requestBody = new RequestBody() {
            @Override
            public MediaType contentType() {
                return MultipartBody.FORM;
            }

            @Override
            public long contentLength() throws IOException {
                return bytes.length;
            }

            @Override
            public void writeTo(BufferedSink sink) throws IOException {
                sink.write(bytes);
            }
        };
        createCall(encode, url, requestBody).enqueue(new OkHttpCallback(url, dataListener));
    }

    /**
     * post上传File -- 不带进度条
     */
    /*public static void postStream(String url, String encode, final File file, OnDataListener dataListener) {
        if (file == null) {
            return;
        }
        RequestBody requestBody = new RequestBody() {
            @Override
            public MediaType contentType() {
                return MultipartBody.FORM;
            }

            @Override
            public long contentLength() throws IOException {
                return file.length();
            }

            @Override
            public void writeTo(BufferedSink sink) throws IOException {
                Source source = Okio.source(file);
                sink.writeAll(source);
            }
        };
        createCall(encode, url, requestBody).enqueue(new OkHttpCallback(url, dataListener));
    }*/

    /**
     * post上传Bitmap -- 带进度条
     */
    public static void postStream(String url, String encode, final int index, Bitmap bitmap,
                                  final OnProgressMultiListener progressListener, OnDataListener dataListener) {
        final byte[] bytes = BitMapUtil.Bitmap2BAOS(bitmap).toByteArray();
        RequestBody requestBody = new RequestBody() {
            @Override
            public MediaType contentType() {
                return MultipartBody.FORM;
            }

            @Override
            public long contentLength() throws IOException {
                return bytes.length;
            }

            @Override
            public void writeTo(BufferedSink sink) throws IOException {
                Source source = Okio.source(new ByteArrayInputStream(bytes));
                long readCount = 0L;//一共上传了多少字节
                long readSize;//当前上传了多少字节
                Buffer buffer = new Buffer();
                while (true) {
                    readSize = (int) source.read(buffer, 1024);
                    if (readSize < 0) {
                        break;
                    } else {
                        sink.write(buffer, readSize);
                        readCount += readSize;
                        if (progressListener != null) {
                            int rate = (int) (readCount * 100.0 / contentLength());
                            progressListener.onProgressMulti(index, rate);
                        }
                    }
                }
            }
        };
        createCall(encode, url, requestBody).enqueue(new OkHttpCallback(url, dataListener));
    }

    /**
     * post上传File -- 带进度条
     */
    /*public static void postStream(String url, String encode, final int index, final File file,
                                  final OnProgressMultiListener progressListener, OnDataListener dataListener) {
        if (file == null) {
            return;
        }
        RequestBody requestBody = new RequestBody() {
            @Override
            public MediaType contentType() {
                return MultipartBody.FORM;
            }

            @Override
            public long contentLength() throws IOException {
                return file.length();
            }

            @Override
            public void writeTo(BufferedSink sink) throws IOException {
                Source source = Okio.source(file);
                int readCount = 0;//一共上传了多少字节
                int readSize;//当前上传了多少字节
                Buffer buffer = new Buffer();
                while(true){
                    readSize = (int) source.read(buffer, 1024);
                    if (readSize < 0) {
                        break;
                    } else {
                        sink.write(buffer, readSize);
                        readCount += readSize;
                        if (progressListener != null) {
                            int rate = (int) (readCount * 100 /contentLength());
                            progressListener.onProgressMulti(index, rate);
                        }
                    }
                }
            }
        };
        createCall(encode, url, requestBody).enqueue(new OkHttpCallback(url, dataListener));
    }*/

    /**
     * 创建Call对象
     */
    private static Call createCall(String encode, String url, RequestBody requestBody) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss", Locale.getDefault());
        String fileName = encode + "_" + sdf.format(new Date()) + ".jpg";
        MultipartBody build = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("EnCode", encode)
                .addPart(Headers.of("Content-Disposition", "form-data; name=\"" + encode +
                        "\"; fileName=\"" + fileName + "\""), requestBody)
                .build();
        Request request = new Request.Builder()
                .url(url)
                .post(build)
                .build();
        return okHttpClient.newCall(request);
    }

    /**
     * 结果回调
     */
    static class OkHttpCallback implements Callback {

        private String url;
        private OnDataListener dataListener;

        public OkHttpCallback(String url, OnDataListener dataListener) {
            this.url = url;
            this.dataListener = dataListener;
        }

        @Override
        public void onFailure(Call call, IOException e) {
            onFail(dataListener, url, e);
        }

        @Override
        public void onResponse(Call call, Response response) throws IOException {
            onResp(dataListener, url, response.body().string());
        }
    }

    private static void onFail(OnDataListener dataListener, String url, IOException e) {
        if (dataListener != null) {
            Log.e("loge", "onFailure: " + url + "\n" + e.getMessage());
            dataListener.onFailure(url, e.getMessage());
        }
    }

    private static void onResp(final OnDataListener dataListener, final String url, final String json) {
        handler.post(new Runnable() {
            @Override
            public void run() {
                try {
                    if (dataListener != null && !TextUtils.isEmpty(json)) {
                        dataListener.onResponse(url, json);
                    }
                } catch (Exception e) {
                    Log.e("loge", json.substring(json.length() - Math.min(40, json.length())) + "\n" + "E: "
                            + e.getMessage());
                }
            }
        });
    }

    public interface OnDataListener {
        void onResponse(String url, String json);
        void onFailure(String url, String error);
    }

    public interface OnBytesListener {
        void onResponse(String url, byte[] bytes);
        void onFailure(String url, String error);
    }

    public interface OnProgressListener {
        void onProgress(int rate);
    }

    public interface OnProgressMultiListener {
        void onProgressMulti(int index, int rate);
    }
}
