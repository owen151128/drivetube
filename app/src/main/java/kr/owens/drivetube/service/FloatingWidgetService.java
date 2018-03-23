package kr.owens.drivetube.service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.PixelFormat;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.webkit.WebSettings;
import android.webkit.WebView;

import kr.owens.drivetube.R;
import kr.owens.drivetube.databinding.WidgetFloatingBinding;
import kr.owens.drivetube.ui.MainActivity;
import kr.owens.drivetube.wrapper.WebViewClientWrapper;

public class FloatingWidgetService extends Service {

    private WidgetFloatingBinding binding;
    private WindowManager mWindowManager;
    private WindowManager.LayoutParams params;
    private View mView;

    private int initialX = 0;
    private int initialY = 0;
    private float initialTouchX = 0;
    private float initialTouchY = 0;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        binding = DataBindingUtil.inflate(LayoutInflater.from(this), R.layout.widget_floating, null, false);
        binding.setService(this);
        mView = binding.getRoot();
        setWebView(binding.webView, binding.webView.getSettings());
        mWindowManager = (WindowManager) getSystemService(WINDOW_SERVICE);
        params = new WindowManager.LayoutParams(
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.TYPE_PHONE,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                PixelFormat.TRANSLUCENT);
        params.gravity = Gravity.TOP | Gravity.START;
        params.x = 0;
        params.y = 100;

        mWindowManager.addView(mView, params);

        binding.widgetIcon.setOnTouchListener((View v, MotionEvent e) -> {
            switch (e.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    initialX = params.x;
                    initialY = params.y;

                    initialTouchX = e.getRawX();
                    initialTouchY = e.getRawY();
                    return true;

                case MotionEvent.ACTION_UP:
                    int xDiff = (int) (e.getRawX() - initialTouchX);
                    int yDiff = (int) (e.getRawY() - initialTouchY);

                    if (xDiff < 10 && yDiff < 10) {
                        if (isViewWidget()) {
                            binding.widgetLayout.setVisibility(View.GONE);
                            binding.webViewLayout.setVisibility(View.VISIBLE);
                        }
                    }
                    v.performClick();
                    return true;

                case MotionEvent.ACTION_MOVE:
                    params.x = initialX + (int) (e.getRawX() - initialTouchX);
                    params.y = initialY + (int) (e.getRawY() - initialTouchY);
                    mWindowManager.updateViewLayout(mView, params);
                    return true;
            }
            return false;
        });

        binding.widgetIcon.setOnLongClickListener(v -> {
            Intent intent = new Intent(FloatingWidgetService.this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            stopSelf();

            return false;
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mView != null)
            mWindowManager.removeView(mView);
    }

    private boolean isViewWidget() {
        return mView == null || binding.widgetLayout.getVisibility() == View.VISIBLE;
    }

    private void setWebView(WebView webView, WebSettings webSettings) {
        webSettings.setCacheMode(WebSettings.LOAD_DEFAULT);
        webSettings.setJavaScriptEnabled(true);

        webView.setFocusable(true);
        webView.setWebViewClient(new WebViewClientWrapper());
        webView.loadUrl("https://m.youtube.com");
    }

    public void onStopServiceButtonClicked(View v) {
        stopSelf();
    }

    public void onCloseWebViewButtonClicked(View v) {

        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.toggleSoftInput(InputMethodManager.RESULT_UNCHANGED_SHOWN, InputMethodManager.RESULT_UNCHANGED_SHOWN);
        binding.widgetLayout.setVisibility(View.VISIBLE);
        binding.webViewLayout.setVisibility(View.GONE);
    }
}
