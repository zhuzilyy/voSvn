package com.zl.vo_.ui;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Rect;
import android.hardware.Camera;
import android.hardware.Camera.AutoFocusCallback;
import android.hardware.Camera.PreviewCallback;
import android.hardware.Camera.Size;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.zl.vo_.R;
import com.zl.vo_.main.Entity.Result;
import com.zl.vo_.main.activities.UserDetailsActivityVo;
import com.zl.vo_.main.activities.VoBaseActivity;
import com.zl.vo_.main.https.MyCommonCallback;
import com.zl.vo_.main.main_utils.myUtils;
import com.zl.vo_.utils.Url;
import com.zl.vo_.widget.scan.CameraManager;
import com.zl.vo_.widget.scan.CameraPreview;

import net.sourceforge.zbar.Config;
import net.sourceforge.zbar.Image;
import net.sourceforge.zbar.ImageScanner;
import net.sourceforge.zbar.Symbol;
import net.sourceforge.zbar.SymbolSet;

import org.xutils.http.RequestParams;
import org.xutils.x;

import java.io.IOException;
import java.lang.reflect.Field;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ScanCaptureActivity extends VoBaseActivity implements OnClickListener{
	private Camera mCamera;
	private CameraPreview mPreview;
	private Handler autoFocusHandler;
	private CameraManager mCameraManager;

	private TextView scanResult;
	private FrameLayout scanPreview;
	private Button scanRestart;
	private RelativeLayout scanContainer;
	private RelativeLayout scanCropView;
	private ImageView scanLine;

	private Rect mCropRect = null;
	private boolean barcodeScanned = false;
	private boolean previewing = true;
	private ImageScanner mImageScanner = null;
	private String cursor;
	@BindView(R.id.iv_back)
	public ImageView iv_back;
	static {
		System.loadLibrary("iconv");
	}

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.widget_zbar_scan_capture);
		VoBaseActivity.addActivity(this);
        ButterKnife.bind(this);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		setTitle("二维码/条码 扫描");
		findViewById();
		addEvents();
		initViews();
	}

	private void findViewById() {
		scanPreview = (FrameLayout) findViewById(R.id.capture_preview);
		scanResult = (TextView) findViewById(R.id.capture_scan_result);
		scanRestart = (Button) findViewById(R.id.capture_restart_scan);
		scanContainer = (RelativeLayout) findViewById(R.id.capture_container);
		scanCropView = (RelativeLayout) findViewById(R.id.capture_crop_view);
		scanLine = (ImageView) findViewById(R.id.capture_scan_line);
	}

	private void addEvents() {
		scanRestart.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				if (barcodeScanned) {
					barcodeScanned = false;
					scanResult.setText("Scanning...");
					mCamera.setPreviewCallback(previewCb);
					mCamera.startPreview();
					previewing = true;
					mCamera.autoFocus(autoFocusCB);
				}
			}
		});
	}

	private void initViews() {
		mImageScanner = new ImageScanner();
		mImageScanner.setConfig(0, Config.X_DENSITY, 3);
		mImageScanner.setConfig(0, Config.Y_DENSITY, 3);

		autoFocusHandler = new Handler();
		mCameraManager = new CameraManager(this);
		try {
			mCameraManager.openDriver();
		} catch (IOException e) {
			e.printStackTrace();
		}

		// 调整扫描框大小,自适应屏幕
		Display display = this.getWindowManager().getDefaultDisplay();
		int width = display.getWidth();
		int height = display.getHeight();

		RelativeLayout.LayoutParams linearParams = (RelativeLayout.LayoutParams) scanCropView
				.getLayoutParams();
		linearParams.height = (int) (width * 0.8);
		linearParams.width = (int) (width * 0.8);
		scanCropView.setLayoutParams(linearParams);
		// **

		mCamera = mCameraManager.getCamera();
		mPreview = new CameraPreview(this, mCamera, previewCb, autoFocusCB);
		scanPreview.addView(mPreview);

		TranslateAnimation animation = new TranslateAnimation(
				Animation.RELATIVE_TO_PARENT, 0.0f,
				Animation.RELATIVE_TO_PARENT, 0.0f,
				Animation.RELATIVE_TO_PARENT, 0.0f,
				Animation.RELATIVE_TO_PARENT, 0.85f);
		animation.setDuration(5000);
		animation.setRepeatCount(-1);
		animation.setRepeatMode(Animation.REVERSE);
		scanLine.startAnimation(animation);
	}

	public void onPause() {
		super.onPause();
		releaseCamera();
	}

	private void releaseCamera() {
		if (mCamera != null) {
			previewing = false;
			mCamera.setPreviewCallback(null);
			mCamera.release();
			mCamera = null;
		}
	}

	private Runnable doAutoFocus = new Runnable() {
		public void run() {
			if (previewing)
				mCamera.autoFocus(autoFocusCB);
		}
	};

	PreviewCallback previewCb = new PreviewCallback() {
		public void onPreviewFrame(byte[] data, Camera camera) {
			Size size = camera.getParameters().getPreviewSize();

			// 这里需要将获取的data翻转一下，因为相机默认拿的的横屏的数据
			byte[] rotatedData = new byte[data.length];
			for (int y = 0; y < size.height; y++) {
				for (int x = 0; x < size.width; x++)
					rotatedData[x * size.height + size.height - y - 1] = data[x
							+ y * size.width];
			}

			// 宽高也要调整
			int tmp = size.width;
			size.width = size.height;
			size.height = tmp;

			initCrop();

			Image barcode = new Image(size.width, size.height, "Y800");
			barcode.setData(rotatedData);
			barcode.setCrop(mCropRect.left, mCropRect.top, mCropRect.width(),
					mCropRect.height());

			int result = mImageScanner.scanImage(barcode);
			String resultStr = null;

			if (result != 0) {
				SymbolSet syms = mImageScanner.getResults();
				for (Symbol sym : syms) {
					resultStr = sym.getData();
				}
			}
			Log.i("qrcode","resultStr********"+resultStr);
			if (!TextUtils.isEmpty(resultStr)) {
				previewing = false;
				mCamera.setPreviewCallback(null);
				mCamera.stopPreview();

				releaseCamera();
				barcodeScanned = true;

				/***
				 * 扫二维码得到环信id
				 */
				RequestParams params=new RequestParams(Url.FriendInfoURL);
				params.addParameter("huanxin_account", myUtils.readUser(ScanCaptureActivity.this).getHuanxin_account());
				Log.i("err/*","======()()"+myUtils.readUser(ScanCaptureActivity.this).getHuanxin_account());

				params.addParameter("friend_huanxin_account",resultStr);
				Log.i("err/*","======()()"+resultStr);
				final String ResultStr = resultStr;
				x.http().post(params, new MyCommonCallback<Result>() {
					@Override
					public void success(Result data) {
						if("0".equals(data.code)){
							Intent intent=new Intent(ScanCaptureActivity.this,UserDetailsActivityVo.class);
							intent.putExtra("HXid",ResultStr);
							intent.putExtra("way","apply_erweima");

							startActivity(intent);
						}else {
							Log.i("err/*","======()()");
							Toast.makeText(getApplicationContext(), "无效二维码",
									Toast.LENGTH_SHORT).show();
						}

					}

					@Override
					public void error(Throwable ex, boolean isOnCallback) {
						Log.i("err/*",ex.getMessage());
						Toast.makeText(getApplicationContext(), "无效二维码",
								Toast.LENGTH_SHORT).show();
					}
				});






		/*		if (resultStr.contains(":")) {
					String type = resultStr
							.substring(0, resultStr.indexOf(":"));
					String value = resultStr
							.substring(resultStr.indexOf(":") + 1);
					System.out.println("type----------->>>>" + type);
					System.out.println("value----------->>>>" + value);
					if (type.equals("userInfo")) {


						startActivity(new Intent(ScanCaptureActivity.this,
								UserDetailsActivityVo.class).putExtra(
								Constant.KEY_USER_INFO, value));

					}else {

						Toast.makeText(getApplicationContext(), "无效二维码",
								Toast.LENGTH_SHORT).show();
					}

				} else {

					Toast.makeText(getApplicationContext(), "无效二维码",
							Toast.LENGTH_SHORT).show();
				}*/

				//
				// Intent rIntent = new Intent();
				// rIntent.putExtra("SCAN_RESULT",resultStr);
				// setResult(RESULT_OK,rIntent);

				finish();

				// scanResult.setText("barcode result " + resultStr);
				// barcodeScanned = true;
			}
		}
	};

	// Mimic continuous auto-focusing
	AutoFocusCallback autoFocusCB = new AutoFocusCallback() {
		public void onAutoFocus(boolean success, Camera camera) {
			autoFocusHandler.postDelayed(doAutoFocus, 1000);
		}
	};

	/**
	 * 初始化截取的矩形区域
	 */
	private void initCrop() {
		int cameraWidth = mCameraManager.getCameraResolution().y;
		int cameraHeight = mCameraManager.getCameraResolution().x;

		/** 获取布局中扫描框的位置信息 */
		int[] location = new int[2];
		scanCropView.getLocationInWindow(location);

		int cropLeft = location[0];
		int cropTop = location[1] - getStatusBarHeight();

		int cropWidth = scanCropView.getWidth();
		int cropHeight = scanCropView.getHeight();

		/** 获取布局容器的宽高 */
		int containerWidth = scanContainer.getWidth();
		int containerHeight = scanContainer.getHeight();

		/** 计算最终截取的矩形的左上角顶点x坐标 */
		int x = cropLeft * cameraWidth / containerWidth;
		/** 计算最终截取的矩形的左上角顶点y坐标 */
		int y = cropTop * cameraHeight / containerHeight;

		/** 计算最终截取的矩形的宽度 */
		int width = cropWidth * cameraWidth / containerWidth;
		/** 计算最终截取的矩形的高度 */
		int height = cropHeight * cameraHeight / containerHeight;

		/** 生成最终的截取的矩形 */
		mCropRect = new Rect(x, y, width + x, height + y);
	}

	private int getStatusBarHeight() {
		try {
			Class<?> c = Class.forName("com.android.internal.R$dimen");
			Object obj = c.newInstance();
			Field field = c.getField("status_bar_height");
			int x = Integer.parseInt(field.get(obj).toString());
			return getResources().getDimensionPixelSize(x);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return 0;
	}

    @OnClick({R.id.iv_back})
    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.iv_back:
                finish();
                break;
            default:
                break;
        }

    }
}
