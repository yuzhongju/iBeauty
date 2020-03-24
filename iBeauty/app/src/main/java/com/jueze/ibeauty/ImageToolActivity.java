package com.jueze.ibeauty;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.BottomSheetDialog;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.GlideBitmapDrawable;
import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;
import com.jueze.ibeauty.util.FileUriUtils;
import com.jueze.ibeauty.util.FileUtil;
import com.jueze.ibeauty.util.ImageUtil;
import com.jueze.ibeauty.util.ScreenUtil;
import com.jueze.ibeauty.util.ShapeUtil;
import com.jueze.ibeauty.util.ToastUtil;
import java.io.File;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ImageToolActivity extends BaseActivity implements View.OnClickListener {

	private Context mContext;
	private Toolbar toolbar;
	private ImageView imageView;
	//是否已选择图片
	private boolean hasLoaded = false;
	//图片路径
	private String realPath = null;
	//屏幕宽高
	private int screenWidth, screenHeight;

	//bitmap
	private Bitmap rotateLeftBitmap, rotateRightBitmap, rotate180Bitmap, flipHorizontalBitmap, flipVerticalBitmap;

	private static final int REQUEST_CHOOSE_IMAGE = 0;
	private static final int WHAT_ROTATE_LEFT = 101;
	private static final int WHAT_ROTATE_RIGHT = 102;
	private static final int WHAT_ROTATE_180 = 103;
	private static final int WHAT_FLIP_HORIZONTAL = 104;
	private static final int WHAT_FLIP_VERTICAL = 105;

	private FloatingActionMenu fam;
	private FloatingActionButton fabChooseImage, fabChangeColor, fabChangeSize, fabSaveImage;

	private View changeColorView, changeSizeView;
	private BottomSheetDialog changeColorBsd, changeSizeBsd;

	//修改颜色checkbox
	private CheckBox whiteCb, blackCb, greyCb;

	private Handler mHandler = new Handler(){

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
				case WHAT_ROTATE_LEFT:
					imageView.setImageBitmap(rotateLeftBitmap);
					break;
				case WHAT_ROTATE_RIGHT:
					imageView.setImageBitmap(rotateRightBitmap);
					break;
				case WHAT_ROTATE_180:
					imageView.setImageBitmap(rotate180Bitmap);
					break;
				case WHAT_FLIP_HORIZONTAL:
					imageView.setImageBitmap(flipHorizontalBitmap);
					break;
				case WHAT_FLIP_VERTICAL:
					imageView.setImageBitmap(flipVerticalBitmap);
					break;
				default:
			}
		}

	};
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (Build.VERSION.SDK_INT >= 21) {
			View decorView = getWindow().getDecorView();
			decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
			getWindow().setStatusBarColor(Color.TRANSPARENT);
		}
		setContentView(R.layout.activity_image_tool);
		setSupportActionBar(toolbar);
		setBack("图像工具");

	}

	@Override
	public void bindViews() {
		toolbar = findViewById(R.id.toolbar);
		//图片
		imageView = findViewById(R.id.image_preview);
		//fam fab
		fam = findViewById(R.id.fab_menu);
		fabChooseImage = findViewById(R.id.fab_choose_image);
		fabChangeColor = findViewById(R.id.fab_change_color);
		fabChangeSize = findViewById(R.id.fab_change_size);
		fabSaveImage = findViewById(R.id.fab_save_image);
	}

	@Override
	public void initData() {
		this.mContext = ImageToolActivity.this;
		screenWidth = ScreenUtil.getWidth();
		screenHeight = ScreenUtil.getHeight();
	}

	@Override
	public void initEvent() {
		fabChooseImage.setOnClickListener(this);
		fabChangeColor.setOnClickListener(this);
		fabChangeSize.setOnClickListener(this);
		fabSaveImage.setOnClickListener(this);
		fam.setOnClickListener(this);
	}

	private Bitmap getCurrentBitmap() {
		Bitmap bitmap= null;
		try {
			bitmap = ((GlideBitmapDrawable)imageView.getDrawable()).getBitmap();
		} catch (Exception e) {
			bitmap = ((BitmapDrawable)imageView.getDrawable()).getBitmap();
		}
		return bitmap;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.fab_menu:
				fam.close(true);
				break;
			case R.id.fab_choose_image:
				fam.close(true);
				Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
				intent.setType("image/*");
				startActivityForResult(intent, REQUEST_CHOOSE_IMAGE);
				break;
			case R.id.fab_change_color:
				fam.close(true);
				if (hasLoaded) {
					int w = getCurrentBitmap().getWidth();
					int h=getCurrentBitmap().getHeight();
					if (w <= 1000 && h <= 1000) {
						changeColorView = View.inflate(this, R.layout.dialog_change_color, null);
						ShapeUtil.set(changeColorView, 15, 15, 0, 0, "#ffffff");
						final Button btn = changeColorView.findViewById(R.id.change_color_btn);
						final EditText et = changeColorView.findViewById(R.id.color_hex_value);
						whiteCb = changeColorView.findViewById(R.id.white_color_cb);
						blackCb = changeColorView.findViewById(R.id.black_color_cb);
						greyCb = changeColorView.findViewById(R.id.grey_color_cb);

						changeColorBsd = new BottomSheetDialog(this, R.style.BottomSheetStyle);
						changeColorBsd.setContentView(changeColorView);
						changeColorBsd.show();

						whiteCb.setOnCheckedChangeListener(checkBoxListener);
						blackCb.setOnCheckedChangeListener(checkBoxListener);
						greyCb.setOnCheckedChangeListener(checkBoxListener);
						btn.setOnClickListener(new View.OnClickListener(){
								@Override
								public void onClick(View p1) {
									Bitmap changedBitmap = getCurrentBitmap();
									if (whiteCb.isChecked()) {
										changeColorBsd.dismiss();
										changedBitmap = ImageUtil.resetColor(getCurrentBitmap(), "#ffffff");
									} else if (blackCb.isChecked()) {
										changeColorBsd.dismiss();
										changedBitmap = ImageUtil.resetColor(getCurrentBitmap(), "#000000");
									} else if (greyCb.isChecked()) {
										changeColorBsd.dismiss();
										changedBitmap = ImageUtil.resetColor(getCurrentBitmap(), "#888888");
									} else {
										String hexColor = et.getText().toString();
										Matcher m = Pattern.compile("#([a-fA-F0-9]{8}|[a-fA-F0-9]{6})").matcher(hexColor);
										if (TextUtils.isEmpty(hexColor)) {
											ToastUtil.show("输入不能为空");
										} else if (m.matches()) {
											changeColorBsd.dismiss();
											changedBitmap = ImageUtil.resetColor(getCurrentBitmap(), hexColor);
										} else {
											ToastUtil.show("输入格式错误");
										}
									}
									imageView.setImageBitmap(changedBitmap);
								}
							});
					} else {
						ToastUtil.show("图片太大了");
					}
				} else {
					ToastUtil.show("请选择一张图片");
				}
				break;
			case R.id.fab_change_size:
				fam.close(true);
				if (!hasLoaded) {
					ToastUtil.show("请选择一张图片");
				} else {
					changeSizeView = View.inflate(this, R.layout.dialog_change_size, null);
					ShapeUtil.set(changeSizeView, 15, 15, 0, 0, "#ffffff");

					final EditText widthET = changeSizeView.findViewById(R.id.width_size);
					final EditText heightET = changeSizeView.findViewById(R.id.height_size);
					Button btn=changeSizeView.findViewById(R.id.comfirm);

					changeSizeBsd = new BottomSheetDialog(this, R.style.BottomSheetStyle);
					changeSizeBsd.setContentView(changeSizeView);
					changeSizeBsd.show();

					btn.setOnClickListener(new View.OnClickListener(){
							@Override
							public void onClick(View p1) {
								String wstr = widthET.getText().toString();
								String hstr = heightET.getText().toString();
								if (TextUtils.isEmpty(wstr) || TextUtils.isEmpty(hstr)) {
									ToastUtil.show("输入不能为空");
								} else {
									int nw=Integer.valueOf(wstr);
									int nh=Integer.valueOf(hstr);
									if (nw <= 3000 && nh <= 3000) {
										imageView.setImageBitmap(ImageUtil.scaleImage(getCurrentBitmap(), nw, nh));
										changeSizeBsd.dismiss();
									} else {
										ToastUtil.show("这么大，想累死我吗");
									}
								}
							}
						});
				}
				break;
			case R.id.fab_save_image:
				fam.close(true);
				if (!hasLoaded) {
					ToastUtil.show("请选择一张图片");
				} else {
					String path=Attributes.appImagePath;
					String name = System.currentTimeMillis() + realPath.substring(realPath.lastIndexOf("."));
					String pathName = path + name;
					FileUtil.createDir(path);
					ImageUtil.saveBitmap(getCurrentBitmap(), pathName);
					ToastUtil.show("已保存");
				}
				break;
			default:
		}
	}

	CheckBox.OnCheckedChangeListener checkBoxListener = new CheckBox.OnCheckedChangeListener(){

		@Override
		public void onCheckedChanged(CompoundButton cb, boolean checked) {
			switch (cb.getId()) {
				case R.id.white_color_cb:
					if (checked) {
						blackCb.setChecked(false);
						greyCb.setChecked(false);
					}
					break;
				case R.id.black_color_cb:
					if (checked) {
						whiteCb.setChecked(false);
						greyCb.setChecked(false);
					}
					break;
				case R.id.grey_color_cb:
					if (checked) {
						blackCb.setChecked(false);
						whiteCb.setChecked(false);
					}
					break;
				default:
			}
		}
	};


	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.menu_image_tool, menu);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		super.onOptionsItemSelected(item);
		switch (item.getItemId()) {
			case R.id.image_info:
				if (hasLoaded) {
					String path = realPath;
					Object[] infos = ImageUtil.getImageInfo(path);

					String type = (String)infos[2];
					String size = FileUtil.decimal2(new File(path).length());
					int width = infos[0];
					int height = infos[1];
					width = getCurrentBitmap().getWidth();
					height = getCurrentBitmap().getHeight();
					String pixels = width + " x " + height;
					View dialogView = View.inflate(this, R.layout.dialog_image_info, null);
					TextView pathView = dialogView.findViewById(R.id.path);
					TextView typeView = dialogView.findViewById(R.id.type);
					TextView sizeView = dialogView.findViewById(R.id.size);
					TextView pixelsView = dialogView.findViewById(R.id.pixels);

					pathView.setText(path);
					typeView.setText(type);
					sizeView.setText(size);
					pixelsView.setText(pixels);
					AlertDialog dialog = new AlertDialog.Builder(this)
						.setView(dialogView)
						.show();

				} else {
					ToastUtil.show("请选择一张图片");
				}

				break;
			case R.id.rotate_left_90:
				if (hasLoaded) {
					new Thread(new Runnable(){
							@Override
							public void run() {
								rotateLeftBitmap = ImageUtil.rotate(-90, getCurrentBitmap());
								mHandler.sendEmptyMessage(WHAT_ROTATE_LEFT);
							}
						}).start();
				} else {
					ToastUtil.show("请选择一张图片");
				}
				break;
			case R.id.rotate_right_90:
				if (hasLoaded) {
					new Thread(new Runnable(){
							@Override
							public void run() {
								rotateRightBitmap = ImageUtil.rotate(90, getCurrentBitmap());
								mHandler.sendEmptyMessage(WHAT_ROTATE_RIGHT);
							}
						}).start();
				} else {
					ToastUtil.show("请选择一张图片");
				}
				break;
			case R.id.rotate_180:
				if (hasLoaded) {
					new Thread(new Runnable(){
							@Override
							public void run() {
								rotate180Bitmap = ImageUtil.rotate(180, getCurrentBitmap());
								mHandler.sendEmptyMessage(WHAT_ROTATE_180);
							}
						}).start();
				} else {
					ToastUtil.show("请选择一张图片");
				}
				break;
			case R.id.flip_horizontal:
				if (hasLoaded) {
					new Thread(new Runnable(){
							@Override
							public void run() {
								flipHorizontalBitmap = ImageUtil.flipHorizontal(getCurrentBitmap());
								mHandler.sendEmptyMessage(WHAT_FLIP_HORIZONTAL);
							}
						}).start();
				} else {
					ToastUtil.show("请选择一张图片");
				}
				break;
			case R.id.flip_vertical:
				if (hasLoaded) {
					new Thread(new Runnable(){
							@Override
							public void run() {
								flipVerticalBitmap = ImageUtil.flipVertical(getCurrentBitmap());
								mHandler.sendEmptyMessage(WHAT_FLIP_VERTICAL);
							}
						}).start();
				} else {
					ToastUtil.show("请选择一张图片");
				}
				break;
			default:
		}
		return true;
	}

	@Override
	public void setStatusBar() {
		//super.setStatusBar();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		switch (requestCode) {
			case REQUEST_CHOOSE_IMAGE:
				if (resultCode == RESULT_OK) {
					try {
						realPath = FileUriUtils.getByIntent(this, data);
					} catch (Exception e) {}
					if (TextUtils.isEmpty(realPath)) {
						hasLoaded = false;
						ToastUtil.show("请重新选择一个管理器");
					} else {
						hasLoaded = true;
						Glide.with(this).load(new File(realPath)).into(imageView);
					}
				}
				break;
			default:
		}
	}





}
