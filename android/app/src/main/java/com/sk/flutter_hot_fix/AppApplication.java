package com.sk.flutter_hot_fix;

import static com.tencent.bugly.beta.tinker.TinkerManager.getApplication;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.widget.Toast;


//import com.idlefish.flutterboost.FlutterBoost;
//import com.idlefish.flutterboost.Platform;
//import com.idlefish.flutterboost.Utils;
//import com.idlefish.flutterboost.interfaces.INativeRouter;
import androidx.multidex.MultiDexApplication;

//import com.sk.flutter_hot_fix.services.RestartService;
import com.tencent.bugly.Bugly;
import com.tencent.bugly.beta.Beta;
import com.tencent.bugly.beta.interfaces.BetaPatchListener;

import java.util.Locale;
import java.util.Map;

import io.flutter.FlutterInjector;
import io.flutter.app.FlutterApplication;
import io.flutter.embedding.android.FlutterView;
import io.flutter.plugin.common.MethodChannel;
import io.flutter.view.FlutterMain;

/**
 * |                   quu..__
 * |                    $$  `---.__
 * |                     "$        `--.                          ___.---uuudP
 * |                      `$           `.__.------.__     __.---'      $$$$"              .
 * |                        "          -'            `-.-'            $$$"              .'|
 * |                          ".                                       d$"             _.'  |
 * |                            `.   /                              ..."             .'     |
 * |                              `./                           ..::-'            _.'       |
 * |                               /                         .:::-'            .-'         .'
 * |                              :                          ::''\          _.'            |
 * |                             .' .-.             .-.           `.      .'               |
 * |                             : /'$$|           .@"$\           `.   .'              _.-'
 * |                            .'|$$|          |$$,$$|           |  <            _.-'
 * |                            | `:$$:'          :$$$$$:           `.  `.       .-'
 * |                            :                  `"--'             |    `-.     \
 * |                           :$$.       ==             .$$$.       `.      `.    `\
 * |                           |$$:                      :$$$:        |        >     >
 * |                           |$'     `..'`..'          `$$$'        x:      /     /
 * |                            \                                   xXX|     /    ./
 * |                             \                                xXXX'|    /   ./
 * |                             /`-.                                  `.  /   /
 * |                            :    `-  ...........,                   | /  .'
 * |                            |         ``:::::::'       .            |<    `.
 * |                            |             ```          |           x| \ `.:``.
 * |                            |                         .'    /'   xXX|  `:`M`M':.
 * |                            |    |                    ;    /:' xXXX'|  -'MMMMM:'
 * |                            `.  .'                   :    /:'       |-'MMMM.-'
 * |                             |  |                   .'   /'        .'MMM.-'
 * |                             `'`'                   :  ,'          |MMM<
 * |                               |                     `'            |tbap\
 * |                                \                                  :MM.-'
 * |                                 \                 |              .''
 * |                                  \.               `.            /
 * |                                   /     .:::::::.. :           /
 * |                                  |     .:::::::::::`.         /
 * |                                  |   .:::------------\       /
 * |                                 /   .''               >::'  /
 * |                                 `',:                 :    .'
 * |
 * |                                                      `:.:'
 * |
 * |
 * |
 *
 * @author SK on 2020/6/18
 */


public class AppApplication extends FlutterApplication {


    // 测试sophix时，请注掉attachBaseContext
    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        Beta.installTinker();
        //  https://bugly.qq.com/docs/user-guide/api-hotfix/?v=1.0.0
        // allow to notify user to restart
        Beta.canNotifyUserRestart = true;

        Beta.betaPatchListener = new BetaPatchListener() {
            @Override
            public void onPatchReceived(String patchFile) {
                Toast.makeText(getApplication(), "补丁下载地址" + patchFile, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onDownloadReceived(long savedLength, long totalLength) {
                Toast.makeText(getApplication(),
                        String.format(Locale.getDefault(), "%s %d%%",
                                Beta.strNotificationDownloading,
                                (int) (totalLength == 0 ? 0 : savedLength * 100 / totalLength)),
                        Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onDownloadSuccess(String msg) {
                Toast.makeText(getApplication(), "补丁下载成功", Toast.LENGTH_SHORT).show();

                // 自动重启
//                Intent intent = new Intent(base, RestartService.class);
//                intent.putExtra("packageName", base.getPackageName());
//
//                base.startService(intent);

                final Intent intent = getPackageManager().getLaunchIntentForPackage(getPackageName());
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);

                //杀死整个进程
                android.os.Process.killProcess(android.os.Process.myPid());
            }

            @Override
            public void onDownloadFailure(String msg) {
                Toast.makeText(getApplication(), "补丁下载失败", Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onApplySuccess(String msg) {
                Toast.makeText(getApplication(), "补丁应用成功", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onApplyFailure(String msg) {
                Toast.makeText(getApplication(), "补丁应用失败", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onPatchRollback() {
                Toast.makeText(getApplication(), "补丁回滚", Toast.LENGTH_SHORT).show();
            }
        };
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Bugly.init(this, "9b1052211a", true);
    }

    //配置flutterboost
//    private void initFlutterBoost() {
//        INativeRouter router =new INativeRouter() {
//            @Override
//            public void openContainer(Context context, String url, Map<String, Object> urlParams, int requestCode, Map<String, Object> exts) {
//                String  assembleUrl= Utils.assembleUrl(url,urlParams);
//            }
//
//        };
//
//        FlutterBoost.BoostLifecycleListener boostLifecycleListener= new FlutterBoost.BoostLifecycleListener(){
//
//            @Override
//            public void beforeCreateEngine() {
//
//            }
//
//            @Override
//            public void onEngineCreated() {
//
//                // 注册MethodChannel，监听flutter侧的getPlatformVersion调用
//                MethodChannel methodChannel = new MethodChannel(FlutterBoost.instance().engineProvider().getDartExecutor(), "flutter_native_channel");
//                methodChannel.setMethodCallHandler((call, result) -> {
//
//                    if (call.method.equals("getPlatformVersion")) {
//                        result.success(Build.VERSION.RELEASE);
//                    } else {
//                        result.notImplemented();
//                    }
//
//                });
//
//                // 注册PlatformView viewTypeId要和flutter中的viewType对应
////                FlutterBoost
////                        .instance()
////                        .engineProvider()
////                        .getPlatformViewsController()
////                        .getRegistry()
////                        .registerViewFactory("plugins.test/view", new TextPlatformViewFactory(StandardMessageCodec.INSTANCE));
//
//            }
//
//            @Override
//            public void onPluginsRegistered() {
//
//            }
//
//            @Override
//            public void onEngineDestroy() {
//
//            }
//
//        };
//
//        //
//        // AndroidManifest.xml 中必须要添加 flutterEmbedding 版本设置
//        //
//        //   <meta-data android:name="flutterEmbedding"
//        //               android:value="2">
//        //    </meta-data>
//        // GeneratedPluginRegistrant 会自动生成 新的插件方式　
//        //
//        // 插件注册方式请使用
//        // FlutterBoost.instance().engineProvider().getPlugins().add(new FlutterPlugin());
//        // GeneratedPluginRegistrant.registerWith()，是在engine 创建后马上执行，放射形式调用
//        //
//
//        Platform platform= new FlutterBoost
//                .ConfigBuilder(this,router)
//                .isDebug(true)
//                .whenEngineStart(FlutterBoost.ConfigBuilder.ANY_ACTIVITY_CREATED)
//                .renderMode(FlutterView.RenderMode.texture)
//                .lifecycleListener(boostLifecycleListener)
//                .build();
//        FlutterBoost.instance().init(platform);
//    }
}
