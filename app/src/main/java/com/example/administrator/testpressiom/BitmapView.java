package com.example.administrator.testpressiom;

import java.io.File;
import java.io.FileOutputStream;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.view.View;

public class BitmapView extends View
{
	private Matrix matrix = null;
	public BitmapView(Context context)
	{
		super(context);
	}

	public void onDraw(Canvas canvas)
	{
		
		// 获取资源文件的引用res
		Resources res = getResources(); 
		// 获取图形资源文件
		Bitmap bmp = BitmapFactory.decodeResource(res, R.mipmap.ic_launcher);
		// 设置canvas画布背景为白色
		canvas.drawColor(Color.BLACK);
		canvas.drawBitmap(bmp, 0, 0, null);
		// 定义矩阵对象
		matrix = new Matrix();
		//旋转30度
		matrix.postRotate(30);
		Bitmap bitmap = Bitmap.createBitmap(bmp, 0, 50, bmp.getWidth(), bmp.getHeight()/2,
				matrix, true);
		canvas.drawBitmap(bitmap, 0, 250, null);
		SaveBitmap(bitmap);
	}
	//保存到本地
	public void SaveBitmap(Bitmap bmp)
	{
		Bitmap bitmap = Bitmap.createBitmap(800, 600, Config.ARGB_8888);  
		Canvas canvas = new Canvas(bitmap);
		//加载背景图片
		Bitmap bmps = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher);
		canvas.drawBitmap(bmps, 0, 0, null);
		//加载要保存的画面
		canvas.drawBitmap(bmp, 10, 100, null);
		//保存全部图层
		canvas.save(Canvas.ALL_SAVE_FLAG);
		canvas.restore();
		//存储路径
		File file = new File("/sdcard/song/");
		if(!file.exists())
			file.mkdirs();
			try {
				FileOutputStream fileOutputStream = new FileOutputStream(file.getPath() + "/xuanzhuan.jpg");
				bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fileOutputStream);
				fileOutputStream.close();
				System.out.println("saveBmp is here");
			} catch (Exception e) {
						e.printStackTrace();
		}
	}
}
