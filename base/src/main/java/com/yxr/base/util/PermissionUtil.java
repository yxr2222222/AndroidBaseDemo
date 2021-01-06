package com.yxr.base.util;


import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.text.TextUtils;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;

import com.tbruyelle.rxpermissions2.RxPermissions;
import com.yxr.base.widget.dialog.CancelConfirmDialog;

import java.lang.ref.WeakReference;

import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;


public class PermissionUtil {
    public static void requestPermission(@NonNull FragmentActivity activity, String... permissions) {
        checkPermission(new SimpleConsumer(activity, permissions) {
            @Override
            protected void onHasPermission() {

            }
        });
    }

    @SuppressLint("CheckResult")
    public static Disposable checkPermission(@NonNull SimpleConsumer consumer) {
        return new RxPermissions(consumer.getActivity()).request(consumer.getPermissions()).subscribe(consumer);
    }

    public static String getPermissionName(String permission) {
        if (TextUtils.isEmpty(permission)) {
            return "";
        }
        if (Manifest.permission.READ_CALENDAR.contains(permission)) {
            return "读取日历";
        }
        if (Manifest.permission.WRITE_CALENDAR.contains(permission)) {
            return "写入日历";
        }
        if (Manifest.permission.CAMERA.contains(permission)) {
            return "照相机";
        }
        if (Manifest.permission.READ_CONTACTS.contains(permission)) {
            return "读取联系人";
        }
        if (Manifest.permission.WRITE_CONTACTS.contains(permission)) {
            return "写入联系人";
        }
        if (Manifest.permission.GET_ACCOUNTS.contains(permission)) {
            return "获取账户";
        }
        if (Manifest.permission.ACCESS_FINE_LOCATION.contains(permission)) {
            return "访问详细位置";
        }
        if (Manifest.permission.ACCESS_COARSE_LOCATION.contains(permission)) {
            return "访问粗略位置";
        }
        if (Manifest.permission.RECORD_AUDIO.contains(permission)) {
            return "记录音频";
        }
        if (Manifest.permission.READ_PHONE_STATE.contains(permission)) {
            return "读取手机状态";
        }
        if (Manifest.permission.CALL_PHONE.contains(permission)) {
            return "拨打电话";
        }
        if (Manifest.permission.READ_CALL_LOG.contains(permission)) {
            return "读取通话记录";
        }
        if (Manifest.permission.WRITE_CALL_LOG.contains(permission)) {
            return "写入通话记录";
        }
        if (Manifest.permission.ADD_VOICEMAIL.contains(permission)) {
            return "添加语音信箱";
        }
        if (Manifest.permission.BODY_SENSORS.contains(permission)) {
            return "传感器";
        }
        if (Manifest.permission.SEND_SMS.contains(permission)) {
            return "发送短信";
        }
        if (Manifest.permission.RECEIVE_SMS.contains(permission)) {
            return "接收短信";
        }
        if (Manifest.permission.READ_SMS.contains(permission)) {
            return "查看短信";
        }
        if (Manifest.permission.RECEIVE_WAP_PUSH.contains(permission)) {
            return "接收WAP推送";
        }
        if (Manifest.permission.RECEIVE_MMS.contains(permission)) {
            return "接收彩信";
        }
        if (Manifest.permission.READ_EXTERNAL_STORAGE.contains(permission)) {
            return "读取外部存储";
        }
        if (Manifest.permission.WRITE_EXTERNAL_STORAGE.contains(permission)) {
            return "写入外部存储";
        }
        return "";
    }

    public static abstract class SimpleConsumer implements Consumer<Boolean> {
        private final String[] permissions;
        private final WeakReference<FragmentActivity> activityWeakReference;

        public SimpleConsumer(FragmentActivity activity, String... permissions) {
            this.activityWeakReference = new WeakReference<>(activity);
            this.permissions = permissions;
        }

        public String[] getPermissions() {
            return permissions;
        }

        @Override
        public void accept(Boolean aBoolean) throws Exception {
            if (aBoolean != null && aBoolean) {
                onHasPermission();
            } else {
                onNoPermission();
            }
        }

        protected void onNoPermission() {
            if (activityWeakReference.get() != null) {
                StringBuilder builder = new StringBuilder("以下 【");
                for (int i = 0; i < permissions.length; i++) {
                    String permission = permissions[i];
                    builder.append(getPermissionName(permission));
                    if (i < permissions.length - 1) {
                        builder.append(", ");
                    }
                }
                builder.append("】权限可能没有授权，没有这些权限将影响部分功能使用，是否前往手动设置？");
                FragmentActivity activity = activityWeakReference.get();
                CancelConfirmDialog cancelConfirmDialog = new CancelConfirmDialog(activity);
                cancelConfirmDialog.setContent(builder.toString());
                cancelConfirmDialog.setCancelConfirmListener(new CancelConfirmDialog.SimpleCancelConfirmListener() {
                    @Override
                    public void onConfirm() {
                        jump2Setting();
                    }
                });
                cancelConfirmDialog.show();
            }
        }

        /**
         * 跳转到权限设置界面
         */
        private void jump2Setting() {
            if (activityWeakReference.get() != null) {
                try {
                    FragmentActivity activity = activityWeakReference.get();
                    Intent intent = new Intent();
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.setAction("android.settings.APPLICATION_DETAILS_SETTINGS");
                    intent.setData(Uri.fromParts("package", activity.getPackageName(), null));
                    activity.startActivity(intent);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        protected abstract void onHasPermission();

        public FragmentActivity getActivity() {
            return activityWeakReference.get();
        }
    }
}
