package com.mahjong.pk;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.webkit.GeolocationPermissions;
import android.webkit.PermissionRequest;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    // ── 版本號：每次 CI 建置時由 build.gradle 自動帶入 ──
    private static final String CURRENT_VERSION = BuildConfig.VERSION_NAME;
    private static final String GITHUB_RELEASE_API =
        "https://api.github.com/repos/ChenChihChun/Mahjong/releases/latest";

    private static final int PERMISSION_REQUEST = 1;
    private WebView webView;
    private long downloadId = -1;

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestAppPermissions();

        webView = new WebView(this);
        setContentView(webView);

        WebSettings settings = webView.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setDomStorageEnabled(true);
        settings.setMediaPlaybackRequiresUserGesture(false);
        settings.setAllowFileAccessFromFileURLs(true);
        settings.setAllowUniversalAccessFromFileURLs(true);
        settings.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);

        webView.setWebViewClient(new WebViewClient());
        webView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onPermissionRequest(PermissionRequest request) {
                request.grant(request.getResources());
            }
            @Override
            public void onGeolocationPermissionsShowPrompt(String origin,
                    GeolocationPermissions.Callback callback) {
                callback.invoke(origin, true, false);
            }
        });

        webView.loadUrl("file:///android_asset/index.html");

        // 啟動後背景檢查更新
        checkForUpdate();
    }

    // ─────────────────────────────────────────
    // 更新檢查
    // ─────────────────────────────────────────
    private void checkForUpdate() {
        new Thread(() -> {
            try {
                URL url = new URL(GITHUB_RELEASE_API);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestProperty("Accept", "application/vnd.github+json");
                conn.setConnectTimeout(5000);
                conn.setReadTimeout(5000);

                BufferedReader reader = new BufferedReader(
                    new InputStreamReader(conn.getInputStream()));
                StringBuilder sb = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) sb.append(line);
                reader.close();

                JSONObject json       = new JSONObject(sb.toString());
                String latestTag      = json.getString("tag_name");   // e.g. "build-7"
                String apkUrl         = "";

                // 找到 APK 下載網址
                var assets = json.getJSONArray("assets");
                for (int i = 0; i < assets.length(); i++) {
                    JSONObject asset = assets.getJSONObject(i);
                    if (asset.getString("name").endsWith(".apk")) {
                        apkUrl = asset.getString("browser_download_url");
                        break;
                    }
                }

                // 比對版本（tag格式: build-N，versionName 也設同樣格式）
                if (!latestTag.equals("build-" + CURRENT_VERSION) && !apkUrl.isEmpty()) {
                    final String finalApkUrl = apkUrl;
                    final String finalTag    = latestTag;
                    new Handler(Looper.getMainLooper()).post(() ->
                        showUpdateDialog(finalTag, finalApkUrl));
                }
            } catch (Exception e) {
                // 網路失敗或 API 限速，靜默忽略
            }
        }).start();
    }

    private void showUpdateDialog(String newVersion, String apkUrl) {
        new AlertDialog.Builder(this)
            .setTitle("🀄 發現新版本")
            .setMessage("最新版本：" + newVersion + "\n目前版本：build-" + CURRENT_VERSION
                + "\n\n是否立即下載更新？")
            .setPositiveButton("立即更新", (d, w) -> downloadAndInstall(apkUrl))
            .setNegativeButton("稍後再說", null)
            .show();
    }

    // ─────────────────────────────────────────
    // 下載 & 安裝
    // ─────────────────────────────────────────
    private void downloadAndInstall(String apkUrl) {
        File dest = new File(getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS),
                             "mahjong-update.apk");
        if (dest.exists()) dest.delete();

        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(apkUrl))
            .setTitle("麻將PK 更新下載中…")
            .setNotificationVisibility(
                DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
            .setDestinationUri(Uri.fromFile(dest));

        DownloadManager dm = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
        downloadId = dm.enqueue(request);

        // 監聽下載完成
        BroadcastReceiver receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context ctx, Intent intent) {
                long id = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1);
                if (id != downloadId) return;
                unregisterReceiver(this);

                // 確認下載成功
                DownloadManager.Query q = new DownloadManager.Query().setFilterById(id);
                Cursor cursor = dm.query(q);
                if (cursor.moveToFirst()) {
                    int statusIdx = cursor.getColumnIndex(DownloadManager.COLUMN_STATUS);
                    if (statusIdx >= 0 &&
                        cursor.getInt(statusIdx) == DownloadManager.STATUS_SUCCESSFUL) {
                        installApk(dest);
                    }
                }
                cursor.close();
            }
        };
        registerReceiver(receiver,
            new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));
    }

    private void installApk(File apkFile) {
        Uri apkUri = FileProvider.getUriForFile(this,
            getPackageName() + ".fileprovider", apkFile);
        Intent install = new Intent(Intent.ACTION_VIEW)
            .setDataAndType(apkUri, "application/vnd.android.package-archive")
            .addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(install);
    }

    // ─────────────────────────────────────────
    // 權限
    // ─────────────────────────────────────────
    private void requestAppPermissions() {
        String[] perms = {
            Manifest.permission.CAMERA,
            Manifest.permission.RECORD_AUDIO,
        };
        boolean needed = false;
        for (String p : perms) {
            if (ContextCompat.checkSelfPermission(this, p)
                    != PackageManager.PERMISSION_GRANTED) {
                needed = true; break;
            }
        }
        if (needed) ActivityCompat.requestPermissions(this, perms, PERMISSION_REQUEST);
    }

    @Override
    public void onBackPressed() {
        if (webView.canGoBack()) webView.goBack();
        else super.onBackPressed();
    }
}
