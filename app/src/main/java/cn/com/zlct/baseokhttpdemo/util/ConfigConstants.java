package cn.com.zlct.baseokhttpdemo.util;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Environment;

import com.facebook.cache.disk.DiskCacheConfig;
import com.facebook.common.internal.Supplier;
import com.facebook.common.util.ByteConstants;
import com.facebook.drawee.drawable.ProgressBarDrawable;
import com.facebook.drawee.generic.GenericDraweeHierarchy;
import com.facebook.drawee.generic.GenericDraweeHierarchyBuilder;
import com.facebook.drawee.generic.RoundingParams;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.interfaces.SimpleDraweeControllerBuilder;
import com.facebook.imagepipeline.cache.MemoryCacheParams;
import com.facebook.imagepipeline.common.ImageDecodeOptions;
import com.facebook.imagepipeline.core.ImagePipelineConfig;

import java.io.File;

/**
 * Fresco缓存配置
 */
public class ConfigConstants {

    private static final int MAX_HEAP_SIZE = (int) Runtime.getRuntime().maxMemory();//分配的可用内存
    public static final int MAX_MEMORY_CACHE_SIZE = MAX_HEAP_SIZE / 4;//使用的缓存数量

    public static final int MAX_SMALL_DISK_VERY_LOW_CACHE_SIZE = 5 * ByteConstants.MB;//小图极低磁盘空间缓存的最大值（特性：可将大量的小图放到额外放在另一个磁盘空间防止大图占用磁盘空间而删除了大量的小图）
    public static final int MAX_SMALL_DISK_LOW_CACHE_SIZE = 10 * ByteConstants.MB;//小图低磁盘空间缓存的最大值（特性：可将大量的小图放到额外放在另一个磁盘空间防止大图占用磁盘空间而删除了大量的小图）
    public static final int MAX_SMALL_DISK_CACHE_SIZE = 20 * ByteConstants.MB;//小图磁盘缓存的最大值（特性：可将大量的小图放到额外放在另一个磁盘空间防止大图占用磁盘空间而删除了大量的小图）

    public static final int MAX_DISK_CACHE_VERY_LOW_SIZE = 10 * ByteConstants.MB;//默认图极低磁盘空间缓存的最大值
    public static final int MAX_DISK_CACHE_LOW_SIZE = 30 * ByteConstants.MB;//默认图低磁盘空间缓存的最大值
    public static final int MAX_DISK_CACHE_SIZE = 50 * ByteConstants.MB;//默认图磁盘缓存的最大值

    private static String IMAGE_CACHE_DIR;//默认图所放路径的文件夹名
    private static String IMAGE_SMALL_CACHE_DIR;//小图所放路径的文件夹名

    private static ImagePipelineConfig sImagePipelineConfig;

    private ConfigConstants(){
    }

    /**
     * 初始化配置，单例
     */
    public static ImagePipelineConfig getImagePipelineConfig(Context context) {
        if (sImagePipelineConfig == null) {
            IMAGE_CACHE_DIR = "Android/data/" + context.getPackageName() + "/files";
            IMAGE_SMALL_CACHE_DIR = "Android/data/" + context.getPackageName() + "/sFiles";
            sImagePipelineConfig = configureCaches(context);
        }
        return sImagePipelineConfig;
    }

    /**
     * 初始化配置
     */
    private static ImagePipelineConfig configureCaches(Context context) {
        //内存配置
        final MemoryCacheParams bitmapCacheParams = new MemoryCacheParams(
                ConfigConstants.MAX_MEMORY_CACHE_SIZE, // 内存缓存中总图片的最大大小,以字节为单位。
                Integer.MAX_VALUE,                     // 内存缓存中图片的最大数量。
                ConfigConstants.MAX_MEMORY_CACHE_SIZE, // 内存缓存中准备清除但尚未被删除的总图片的最大大小,以字节为单位。
                Integer.MAX_VALUE,                     // 内存缓存中准备清除的总图片的最大数量。
                Integer.MAX_VALUE);                    // 内存缓存中单个图片的最大大小。

        //修改内存图片缓存数量，空间策略（这个方式有点恶心）
        Supplier<MemoryCacheParams> mSupplierMemoryCacheParams= new Supplier<MemoryCacheParams>() {
            @Override
            public MemoryCacheParams get() {
                return bitmapCacheParams;
            }
        };

        //小图片的磁盘配置
        DiskCacheConfig diskSmallCacheConfig =  DiskCacheConfig.newBuilder(context)
                .setBaseDirectoryPath(Environment.getExternalStorageDirectory().getAbsoluteFile())//缓存图片基路径
                .setBaseDirectoryName(IMAGE_SMALL_CACHE_DIR)//文件夹名
                .setMaxCacheSize(ConfigConstants.MAX_DISK_CACHE_SIZE)//默认缓存的最大大小。
                .setMaxCacheSizeOnLowDiskSpace(MAX_SMALL_DISK_LOW_CACHE_SIZE)//缓存的最大大小,使用设备时低磁盘空间。
                .setMaxCacheSizeOnVeryLowDiskSpace(MAX_SMALL_DISK_VERY_LOW_CACHE_SIZE)//缓存的最大大小,当设备极低磁盘空间
                .build();

        //默认图片的磁盘配置
        DiskCacheConfig diskCacheConfig =  DiskCacheConfig.newBuilder(context)
                .setBaseDirectoryPath(Environment.getExternalStorageDirectory().getAbsoluteFile())//缓存图片基路径
                .setBaseDirectoryName(IMAGE_CACHE_DIR)//文件夹名
                .setMaxCacheSize(ConfigConstants.MAX_DISK_CACHE_SIZE)//默认缓存的最大大小。
                .setMaxCacheSizeOnLowDiskSpace(MAX_DISK_CACHE_LOW_SIZE)//缓存的最大大小,使用设备时低磁盘空间。
                .setMaxCacheSizeOnVeryLowDiskSpace(MAX_DISK_CACHE_VERY_LOW_SIZE)//缓存的最大大小,当设备极低磁盘空间
                .build();

        //缓存图片配置
        ImagePipelineConfig.Builder configBuilder = ImagePipelineConfig.newBuilder(context)
//            .setAnimatedImageFactory(AnimatedImageFactory animatedImageFactory)//图片加载动画
                .setBitmapMemoryCacheParamsSupplier(mSupplierMemoryCacheParams)//内存缓存配置（一级缓存，已解码的图片）
//            .setCacheKeyFactory(cacheKeyFactory)//缓存Key工厂
//            .setEncodedMemoryCacheParamsSupplier(encodedCacheParamsSupplier)//内存缓存和未解码的内存缓存的配置（二级缓存）
                .setMainDiskCacheConfig(diskCacheConfig)//磁盘缓存配置（总，三级缓存）
//            .setMemoryTrimmableRegistry(memoryTrimmableRegistry) //内存用量的缩减,有时我们可能会想缩小内存用量。比如应用中有其他数据需要占用内存，不得不把图片缓存清除或者减小 或者我们想检查看看手机是否已经内存不够了。
//            .setNetworkFetchProducer(networkFetchProducer)//自定的网络层配置：如OkHttp，Volley
//            .setPoolFactory(poolFactory)//线程池工厂配置
//            .setProgressiveJpegConfig(progressiveJpegConfig)//渐进式JPEG图
//            .setRequestListeners(requestListeners)//图片请求监听
//            .setResizeAndRotateEnabledForNetwork(boolean resizeAndRotateEnabledForNetwork)//调整和旋转是否支持网络图片
                .setSmallImageDiskCacheConfig(diskSmallCacheConfig)//磁盘缓存配置（小图片，可选～三级缓存的小图优化缓存）
                ;
        return configBuilder.build();
    }
    //圆形，圆角切图，对动图无效
    public static RoundingParams getRoundingParams(){
        RoundingParams roundingParams = RoundingParams.fromCornersRadius(7f);
        return roundingParams;
    }

    //Drawees   DraweeHierarchy  组织
    public static GenericDraweeHierarchy getGenericDraweeHierarchy(Context context){
        GenericDraweeHierarchy gdh = new GenericDraweeHierarchyBuilder(context.getResources())
                .setFailureImage(ConfigConstants.sErrorDrawable)//fresco:failureImage="@drawable/error"失败图
//            .setFailureImage(failureDrawable, failureImageScaleType)//fresco:failureImageScaleType="centerInside"失败图缩放类型
//            .setOverlay(overlay)//fresco:overlayImage="@drawable/watermark"叠加图
//            .setOverlays(overlays)
                .setPlaceholderImage(ConfigConstants.sPlaceholderDrawable)//fresco:placeholderImage="@color/wait_color"占位图
//            .setPlaceholderImage(placeholderDrawable, placeholderImageScaleType)//fresco:placeholderImageScaleType="fitCenter"占位图缩放类型
//            .setPressedStateOverlay(drawable)//fresco:pressedStateOverlayImage="@color/red"按压状态下的叠加图
                .setProgressBarImage(new ProgressBarDrawable())//进度条fresco:progressBarImage="@drawable/progress_bar"进度条
//            .setProgressBarImage(progressBarImage, progressBarImageScaleType)//fresco:progressBarImageScaleType="centerInside"进度条类型
//            .setRetryImage(retryDrawable)//fresco:retryImage="@drawable/retrying"点击重新加载
//            .setRetryImage(retryDrawable, retryImageScaleType)//fresco:retryImageScaleType="centerCrop"点击重新加载缩放类型
                .setRoundingParams(RoundingParams.asCircle())//圆形/圆角fresco:roundAsCircle="true"圆形
                .build();
        return gdh;
    }


//DraweeView～～～SimpleDraweeView——UI组件
//  public static SimpleDraweeView getSimpleDraweeView(Context context,Uri uri){
//    SimpleDraweeView simpleDraweeView=new SimpleDraweeView(context);
//    simpleDraweeView.setImageURI(uri);
//    simpleDraweeView.setAspectRatio(1.33f);//宽高缩放比
//    return simpleDraweeView;
//  }

    //SimpleDraweeControllerBuilder
    public static SimpleDraweeControllerBuilder getSimpleDraweeControllerBuilder(SimpleDraweeControllerBuilder sdcb, Uri uri, Object callerContext, DraweeController draweeController){
        SimpleDraweeControllerBuilder controllerBuilder = sdcb
                .setUri(uri)
                .setCallerContext(callerContext)
//              .setAspectRatio(1.33f);//宽高缩放比
                .setOldController(draweeController);
        return controllerBuilder;
    }

    //图片解码
    public static ImageDecodeOptions getImageDecodeOptions(){
        ImageDecodeOptions decodeOptions = ImageDecodeOptions.newBuilder()
//            .setBackgroundColor(Color.TRANSPARENT)//图片的背景颜色
//            .setDecodeAllFrames(decodeAllFrames)//解码所有帧
//            .setDecodePreviewFrame(decodePreviewFrame)//解码预览框
//            .setForceOldAnimationCode(forceOldAnimationCode)//使用以前动画
//            .setFrom(options)//使用已经存在的图像解码
//            .setMinDecodeIntervalMs(intervalMs)//最小解码间隔（分位单位）
                .setUseLastFrameForPreview(true)//使用最后一帧进行预览
                .build();
        return decodeOptions;
    }

    //图片显示
    /*public static ImageRequest getImageRequest(InstrumentedDraweeView view,String uri){
        ImageRequest imageRequest = ImageRequestBuilder.newBuilderWithSource(Uri.parse(uri))
//            .setAutoRotateEnabled(true)//自动旋转图片方向
//            .setImageDecodeOptions(getImageDecodeOptions())//  图片解码库
//            .setImageType(ImageType.SMALL)//图片类型，设置后可调整图片放入小图磁盘空间还是默认图片磁盘空间
//            .setLocalThumbnailPreviewsEnabled(true)//缩略图预览，影响图片显示速度（轻微）
                .setLowestPermittedRequestLevel(ImageRequest.RequestLevel.FULL_FETCH)//请求经过缓存级别  BITMAP_MEMORY_CACHE，ENCODED_MEMORY_CACHE，DISK_CACHE，FULL_FETCH
//            .setPostprocessor(postprocessor)//修改图片
//            .setProgressiveRenderingEnabled(true)//渐进加载，主要用于渐进式的JPEG图，影响图片显示速度（普通）
                .setResizeOptions(new ResizeOptions(view.getLayoutParams().width, view.getLayoutParams().height))//调整大小
//            .setSource(Uri uri)//设置图片地址
                .build();
        return imageRequest;
    }*/

    //DraweeController 控制 DraweeControllerBuilder
    /*public static DraweeController getDraweeController(ImageRequest imageRequest,InstrumentedDraweeView view){
        DraweeController draweeController = Fresco.newDraweeControllerBuilder()
//            .reset()//重置
                .setAutoPlayAnimations(true)//自动播放图片动画
//            .setCallerContext(callerContext)//回调
                .setControllerListener(view.getListener())//监听图片下载完毕等
//            .setDataSourceSupplier(dataSourceSupplier)//数据源
//            .setFirstAvailableImageRequests(firstAvailableImageRequests)//本地图片复用，可加入ImageRequest数组
                .setImageRequest(imageRequest)//设置单个图片请求～～～不可与setFirstAvailableImageRequests共用，配合setLowResImageRequest为高分辨率的图
//            .setLowResImageRequest(ImageRequest.fromUri(lowResUri))//先下载显示低分辨率的图
                .setOldController(view.getController())//DraweeController复用
                .setTapToRetryEnabled(true)//点击重新加载图
                .build();
        return draweeController;
    }*/

    //默认加载图片和失败图片
    public static Drawable sPlaceholderDrawable;
    public static Drawable sErrorDrawable;

    /**
     * 清空缓存
     */
    public static void clearCache(){
        FileUtil.delFile(new File(Environment.getExternalStorageDirectory().getAbsoluteFile() + "/" +
                IMAGE_CACHE_DIR));
        FileUtil.delFile(new File(Environment.getExternalStorageDirectory().getAbsoluteFile() + "/" +
                IMAGE_SMALL_CACHE_DIR));
    }

    /**
     * 获取缓存大小
     */
    public static long cacheSize(){
        return FileUtil.getFileSize(new File(Environment.getExternalStorageDirectory().getAbsoluteFile() + "/" +
                IMAGE_CACHE_DIR)) +
               FileUtil.getFileSize(new File(Environment.getExternalStorageDirectory().getAbsoluteFile() + "/" +
                       IMAGE_SMALL_CACHE_DIR));
    }
}