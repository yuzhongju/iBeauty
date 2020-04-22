package com.jueze.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Bitmap.Config;
import android.graphics.PorterDuff.Mode;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileNotFoundException;
import android.os.FileUtils;
import java.io.File;

public class ImageUtil {

	//get image tyep
	public static Object[] getImageInfo(String pathname) {
		BitmapFactory.Options options = new BitmapFactory.Options();
		//不加载到内存
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeFile(pathname, options);
		Object[] infos={options.outWidth, options.outHeight, options.outMimeType};
		return infos;
	}

	//get bitmap by resId
    public static Bitmap getBitmapById(Context context, int resId) {
        BitmapFactory.Options opt = new BitmapFactory.Options();
        opt.inPreferredConfig = Bitmap.Config.RGB_565;
        opt.inPurgeable = true;
        opt.inInputShareable = true;
        InputStream is = context.getResources().openRawResource(resId);
        return BitmapFactory.decodeStream(is, null, opt);
    }

	public static void saveBitmap(Bitmap bitmap, String pathName){
		try {
			FileUtil.createDir(new File(pathName).getParent());
			FileOutputStream fos = new FileOutputStream(pathName);
			bitmap.compress(Bitmap.CompressFormat.PNG,100,fos);
			fos.flush();
			fos.close();
		} catch (Exception e) {}
	}
	//bitmap to bytes
	public static byte[] bmp2bytes(Bitmap bitmap) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
		return baos.toByteArray();
	}

	//bytes to bitmap
	public static Bitmap bytes2bmp(byte[] bytes) {
		if (bytes != null && bytes.length > 0) {
			return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
		}
		return null;
	}

	public static Bitmap toRound(Bitmap bitmap, float index) {
        Bitmap newBitmap = null;
		try {
			newBitmap = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Config.ARGB_8888);

			Canvas canvas = new Canvas(newBitmap);
			Paint paint = new Paint();
			paint.setAntiAlias(true);

			//设置矩形大小
			Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
			RectF rectf = new RectF(rect);

			// 相当于清屏
			canvas.drawARGB(0, 0, 0, 0);
			//画圆角
			canvas.drawRoundRect(rectf, index, index, paint);

			// 取两层绘制，显示上层
			paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));

			// 把原生的图片放到这个画布上，使之带有画布的效果
			canvas.drawBitmap(bitmap, rect, rect, paint);
			
		} catch (Exception e) {
			newBitmap = bitmap;
		}
        return newBitmap;

    }

	//水平翻转 flip horizontal
	public static Bitmap flipHorizontal(Bitmap bitmap) {
		Bitmap newBitmap=null;
		try {
			Matrix matrix = new Matrix();
			matrix.postScale(-1, 1);
			newBitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
			
		} catch (Exception e) {
			newBitmap = bitmap;
		}
		return newBitmap;
	}

	//垂直翻转
	public static Bitmap flipVertical(Bitmap bitmap) {
		Bitmap newBitmap=null;
		try {
			Matrix matrix = new Matrix();
			matrix.postScale(1, -1);
			newBitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
		} catch (Exception e) {
			newBitmap = bitmap;
		}
		return newBitmap;
	}

	//旋转图片
	public static Bitmap rotate(int degrees, Bitmap bitmap) {
		Bitmap newBitmap = null;
		try {Matrix matrix = new Matrix();
			matrix.postRotate(degrees, (float)bitmap.getWidth() / 2, (float)bitmap.getHeight() / 2);
			// 创建新的图片
			newBitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
			
		} catch (Exception e) {
			newBitmap = bitmap;
		}
        return newBitmap;
	}

	//reset single color on icon.
	public static Bitmap resetColor(Bitmap bitmap, String color) {
		Bitmap newBitmap = null;
		try {
			int mColor = Color.parseColor(color);
			//按照图标大小创建数组
            int mBitmapWitdh = bitmap.getWidth();
            int mBitmapHeight = bitmap.getHeight();
            int[] mPixels = new int[mBitmapWitdh * mBitmapHeight];
            //循环 bitmap 每个像素点 查看alpha值
            int count = 0;
            for (int i = 0; i < mBitmapHeight; i++) {
                for (int j = 0; j < mBitmapWitdh; j++) {
                    //获得bitmap每个像素点的color
                    int perColor = bitmap.getPixel(j, i);
                    int a = Color.alpha(perColor);
                    if (a != 0) {
                        mPixels[count] = mColor;
                    } else {
                        mPixels[count] = Color.parseColor("#00000000");
                    }
                    count++;
                }
            }
            newBitmap = Bitmap.createBitmap(mBitmapWitdh, mBitmapHeight, Bitmap.Config.ARGB_8888);
            newBitmap.setPixels(mPixels, 0, mBitmapWitdh, 0, 0, mBitmapWitdh, mBitmapHeight);
			
		} catch (Exception e) {
			newBitmap = bitmap;
		}
		return newBitmap;
	}


	public static Bitmap scaleImage(Bitmap bitmap, float scale) {
		Bitmap newBitmap = null;
		try {
			int width = bitmap.getWidth();
			int height = bitmap.getHeight();
			Matrix matrix = new Matrix();
			matrix.postScale(scale, scale);
			newBitmap = Bitmap.createBitmap(bitmap, 0, 0, width, height, matrix, true);
		
		} catch (Exception e) {
			newBitmap = bitmap;
		}                  
		return newBitmap;
	}

	/**
	 * 缩放图片
     * @param bitmap 要缩放图片
     * @param needWidth 宽度
     * @param needHeight 高度
     * @return 成功返回new bitmap，失败返回原图
     */
    public static  Bitmap  scaleImage(Bitmap bitmap, int needWidth, int needHeight) {
		Bitmap newBitmap = null;
		try {
			int width = bitmap.getWidth();
			int height = bitmap.getHeight();
			float scaleWidth = ((float) needWidth) / width;
			float scaleHeight = ((float) needHeight) / height;
			Matrix matrix = new Matrix();
			matrix.postScale(scaleWidth, scaleHeight);
			newBitmap = Bitmap.createBitmap(bitmap, 0, 0, width, height, matrix, true);
			
		} catch (Exception e) {
			newBitmap = bitmap;
		}
		return newBitmap;
	}
}
