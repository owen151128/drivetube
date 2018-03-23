package kr.owens.drivetube.ui;

import android.content.DialogInterface;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import kr.owens.drivetube.R;
import kr.owens.drivetube.databinding.ActivityMainBinding;
import kr.owens.drivetube.service.FloatingWidgetService;

public class MainActivity extends AppCompatActivity implements MainContract.View {

    private static final int APP_PERMISSION = 1234;
    private ActivityMainBinding binding;
    private AlertDialog.Builder alertDialogBuilder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        alertDialogBuilder = new AlertDialog.Builder(MainActivity.this);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && !Settings.canDrawOverlays(this)) {
            startActivityForResult(new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                    Uri.parse("package:" + getPackageName())), APP_PERMISSION);
        } else {
            initView();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == APP_PERMISSION) {
            if (resultCode == RESULT_OK) {
                initView();
            } else {
                makeDialog("오류", "권한이 허용되어 있지 않습니다. 권한을 허용하고 앱을 실행해 주세요.\n" +
                                "확인 버튼을 누르시면 앱이 종료됩니다.", true,
                        false, null, (d, w) -> finish(), null);
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    private void initView() {
        startService(new Intent(MainActivity.this, FloatingWidgetService.class));
        finish();
    }

    private void makeDialog(String title, String message,
                            boolean isAlert, boolean cancelable, View view,
                            DialogInterface.OnClickListener onYesListener,
                            DialogInterface.OnClickListener onNoListener) {
        alertDialogBuilder.setTitle(title).setMessage(message)
                .setPositiveButton("OK", onYesListener);
        if (isAlert) {
            alertDialogBuilder.setNegativeButton(null, null);
        } else {
            alertDialogBuilder.setNegativeButton("NO", onNoListener);
        }
        if (cancelable)
            alertDialogBuilder.setCancelable(true);
        else
            alertDialogBuilder.setCancelable(false);

        alertDialogBuilder.setView(view);

        alertDialogBuilder.show();
    }
}
