package com.daya.android.iap.onestore.installer;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.util.Log;

public class AppInstaller {
    private static final String TAG = "AppInstaller";
    private static final String ONE_STORE_SERVICE_PACKAGE = "com.skt.skaf.OA00018282";
    private static final String ONE_STORE_DOWNLOAD_URL = "http://m.onestore.co.kr/mobilepoc/etc/downloadGuide.omp";
    private static final String ONE_STORE_DOWNLOAD_ACTION_NAME = "android.intent.action.VIEW";

    private AppInstaller() {
    }

    public static void updateOrInstall(@NonNull Activity activity) {
        String installMessage = "원스토어 서비스 설치 후 구매가 가능합니다.\n원스토어 서비스를 설치하시겠습니까?";
        String updateMessage = "원스토어 서비스 업데이트 후 구매가 가능합니다.\n원스토어 서비스를 업데이트하시겠습니까?";
        String installButtonText = "확인";
        String updateButtonText = "업데이트";
        String cancelButtonText = "취소";
        if (isServiceInstalled(activity)) {
            showDialog(activity, updateMessage, updateButtonText, cancelButtonText);
        } else {
            showDialog(activity, installMessage, installButtonText, cancelButtonText);
        }
    }

    private static boolean isServiceInstalled(@NonNull Context context) {
        PackageManager packageManager = context.getApplicationContext().getPackageManager();
        if (packageManager == null) {
            return false;
        }

        try {
            PackageInfo packageInfo = packageManager.getPackageInfo(ONE_STORE_SERVICE_PACKAGE, 0);
            return packageInfo != null;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return false;
    }

    private static void showDialog(@NonNull final Activity activity,
                                   @NonNull String message,
                                   @NonNull String positiveButtonText,
                                   @NonNull String negativeButtonText) {
        new AlertDialog.Builder(activity)
                .setMessage(message)
                .setPositiveButton(positiveButtonText, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        try {
                            Intent intent = new Intent(ONE_STORE_DOWNLOAD_ACTION_NAME);
                            intent.setData(Uri.parse(ONE_STORE_DOWNLOAD_URL));
                            activity.startActivity(intent);
                        } catch (Exception e) {
                            Log.d(TAG, "원스토어 서비스 설치/업데이트 페이지 이동 중 알 수 없는 오류가 발생하였습니다.");
                        }
                    }
                })
                .setNegativeButton(negativeButtonText, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Log.d(TAG, "설치/업데이트를 취소하였습니다.");
                    }
                })
                .setCancelable(false)
                .show();
    }
}
