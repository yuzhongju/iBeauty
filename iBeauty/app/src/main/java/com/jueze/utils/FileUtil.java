package com.jueze.utils;
import android.content.Context;
import android.os.Environment;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class FileUtil {
	public static boolean isExists(String filePathName){
		return new File(filePathName).exists();
	}
	public static boolean isFile(String filePathName){
		return new File(filePathName).isFile();
	}
	public static boolean copyFromAssets(Context context, String from, String to){
		InputStream is=null;
		BufferedOutputStream bos=null;
		try{
			if(!createDir(new File(to).getParent())) return false;
			is=context.getAssets().open(from);
			bos=new BufferedOutputStream(new FileOutputStream(to));
			byte[] buffer=new byte[1024];
			int len;
			while((len=is.read(buffer,0,buffer.length))!=-1){
				if(len>0) bos.write(buffer,0,len);
			}
			bos.flush();
			is.close();
			bos.close();
			return true;
		}catch(Exception e){}
		return false;
	}
	
    /**复制单个文件
     *@param from 原文件完整路径
     *@param to 目标文件完整路径
     *@return 
     */
    public static boolean copyFile(String from, String to) {
        File fromFile = new File(from);
        File toFile = new File(to);

        BufferedInputStream bis = null;
        BufferedOutputStream bos = null;
        try {
            if(!createDir(toFile.getParent())) return false;
            bis = new BufferedInputStream(new FileInputStream(fromFile));
            bos = new BufferedOutputStream(new FileOutputStream(toFile));
            byte[] buffer = new byte[1024];
            int len;  //存储已读取字节数
            while ((len = bis.read(buffer, 0, buffer.length)) != -1) {
                if (len > 0) bos.write(buffer, 0, len);
            }
            bos.flush();
            bis.close();
            bos.close();
			return true;
        } catch (Exception e) {}
		return false;
    }


    /**复制文件夹以及子目录
     *@param from 原文件夹完整路径
     *@param to 目标文件夹完整路径
     *@return boolean
     */
    public static boolean copyDir(String from, String to) {
        File fromFile = new File(from);
        File toFile = new File(to);
        try {
            if (!(createDir(toFile.toString()))) return false;
            File[] files = fromFile.listFiles();
            for (File file : files) {
                if (file.isFile()) {
                    copyFile(file.toString(), toFile + File.separator + file.getName());
                } else {
                    copyDir(file.toString(), toFile + File.separator + file.getName());
                }
            }
            return true;
        } catch (Exception e) {}
        return false;
    }


    /**删除单个文件
     *@param filename 目标文件完整路径
     *@return boolean
     */

    public static boolean deleteFile(String filename) {
        File file = new File(filename);
        try {
            if (file.exists() && file.isFile()) return file.delete();
        } catch (Exception e) {}
        return false;
    }

    /**删除文件夹以及子目录
     *@param dir 目标文件夹完整路径
     *@param boolean
     */

    public static boolean deleteDir(String dir) {
        
        File dirFile = new File(dir);
        try {
            if (dirFile.exists() && dirFile.isDirectory()) {
                for (File file : dirFile.listFiles()) {
                    if (!deleteDir(file.getPath())) return false;
                }
            }
            return dirFile.delete();
        } catch (Exception e) {}
        return false;
    }

    /**重命名文件及文件夹
     *@param oldPathName 原文件完整路径
     *@param newFilename 目标文件名称
     */
    public static boolean renameTo(String oldPathName, String newFilename) {
        File oldFile = new File(oldPathName);
        File newFile = new File(oldFile.getParent() + File.separator + newFilename);
        try {
            return oldFile.renameTo(newFile);
        } catch (Exception e) {}
        return false;
    }

    /**移动文件及文件夹
     *@param oldPathName 原文件完整路径
     *@param newPathName 目标文件完整路径
     */
    public static boolean moveFile(String oldPathName, String newPathName) {
        File oldFile = new File(oldPathName);
        File newFile = new File(newPathName);
        try {
            if (oldFile.isFile() && newFile.isFile()) {
                if (!(createDir(newFile.getParent()))) return false;
            }
            return oldFile.renameTo(newFile);
        } catch (Exception e) {}
        return false;
    }


    /**读assets文本文件
     *@param fileName 目标文件相对路径
     @return str
     */
    public static String readFromAssets(Context context,String fileName) {
        String str = null;
        try {
            InputStream input = context.getAssets().open(fileName);
            byte[] buffer = new byte[input.available()];
            input.read(buffer);
            str = new String(buffer, "utf-8");
            input.close();
        } catch (IOException e) {}
        return str;
    }


    /**读raw文本文件
     *@param resId 目标文件资源id
     *@retuen str
     */
    public static String readFromRaw(Context context,int resId) {
        String str = null;
        try {
            InputStream input = context.getResources().openRawResource(resId);
            byte[] buffer = new byte[input.available()];
            input.read(buffer);
            str = new String(buffer, "utf-8");
            input.close();
        } catch (IOException e) {}
        return str;
    }


    /**读私有文本文件
     *@param filePathName 目标文件完整路径
     *@return str
     */
    public static String readFromApp(String filePathName) {
        String str = null;
        try {
            File file = new File(filePathName);
            FileInputStream input = new FileInputStream(file);
            byte[] buffer = new byte[input.available()];
            input.read(buffer);
            str = new String(buffer, "utf-8");
            input.close();
        } catch (Exception e) {}
        return str;
    }

    /**写私有文本文件
     *@param filePathName 目标文件完整路径
     *@param fileContent 写入内容
     */
    public static void writeToApp(String filePathName, String fileContent) {
        try {
            File file = new File(filePathName);
            createDir(file.getParent());
            FileOutputStream output = new FileOutputStream(file);
            output.write(fileContent.getBytes());
            output.close();
        } catch (Exception e) {}
    }


    /**读SD文本文件
     *@param filePathName 目标文件完整路径
     *@return str
     */
    public static String readFromSD(String filePathName) {
        String str = null;
        try {
            File file = new File(filePathName);
            FileInputStream input = new FileInputStream(file);
            byte[] buffer = new byte[input.available()];
            input.read(buffer);
            str = new String(buffer, "utf-8");
            input.close();
        } catch (Exception e) {}
        return str;
    }

    /**写SD文本文件
     *@param filePathName 目标文件完整路径
     *@param fileContent 写入内容
     */
    public static void writeToSD(String filePathName, String fileContent) {
        try {
            File file = new File(filePathName);
            createDir(file.getParent());
            FileOutputStream output = new FileOutputStream(file);
            output.write(fileContent.getBytes());
            output.close();
        } catch (Exception e) {}
    }

    /**创建文件夹
     *@param dir 目标文件夹完整路径
     *@return
     */

    public static boolean createDir(String dir) {
        File dirFile = new File(dir);
        try {
            if (!dirFile.exists()) {
                return dirFile.mkdirs();
            } else {
                return true;
            }
        } catch (Exception e) {}
        return false;
    }

    //sd是否可读
    public static boolean sdReadable() {
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            return true;
        }
        return false;
    }

    public static String decimal2(long size){
        String a = null;
        double b = size;
        double kb = size/1024d;
        double mb = size/1024/1024d;
        double gb = size/1024/1024/1024d;
        if(b<1024){
            a = String.format("%.2f",b) + " B";
        }else if(kb<1024){
            a = String.format("%.2f",kb) + " K";
        }else if(mb<1024){
            a = String.format("%.2f",mb) + " M";
        }else if(gb<1024){
            a = String.format("%.2f",gb) + " G";
        }
        return a;
    }

}
