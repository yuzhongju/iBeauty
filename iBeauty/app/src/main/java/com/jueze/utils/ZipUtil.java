package com.jueze.utils;

import android.os.Build;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.util.Enumeration;
import java.util.zip.Adler32;
import java.util.zip.CheckedOutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;

public class ZipUtil {


    /**压缩文件及文件夹
	 *@param srcFileName
	 *@param zipFileName
	 *@return
	 */

    public static boolean toZip(String srcFileName, String zipFileName) {
        return toZip(new File(srcFileName), new File(zipFileName));
    }


    /**压缩文件及文件夹
     *@param srcFile
     *@param zipFile
     *@return
     */
    private static boolean toZip(File srcFile, File zipFile) {

        if (!zipFile.getParentFile().exists()) {
            zipFile.getParentFile().mkdirs();
        }
        ZipOutputStream zos = null;
        try {
            //创建zip输出流
            zos = new ZipOutputStream(new CheckedOutputStream(new FileOutputStream(zipFile), new Adler32()));
            String basePath = srcFile.getName();
            String rootPath = basePath + File.separator;
            return compress(srcFile, zos, basePath, rootPath);

        } catch (Exception e) {
        } finally {
            if (zos != null) {
                try {
                    zos.close();
                } catch (IOException e) {}
            }
        }
        return false;
    }


    /**递归压缩 压缩文件及文件夹
     *@param srcFile
     *@param zos
     *@param basePath
     *@param rootPath
     *@return
     */
    private static boolean compress(File srcFile, ZipOutputStream zos, String basePath, String rootPath) {
        byte[] buffer = new byte[1024];
        basePath = basePath.replace(rootPath, "");

        try {
            if (srcFile.isDirectory()) {
                File[] fileList = srcFile.listFiles();
                if (fileList.length == 0) {
                    zos.putNextEntry(new ZipEntry(basePath + "/"));
                } else {
                    for (File file : fileList) {
                        compress(file, zos, basePath + File.separator + file.getName(), rootPath);
                    }
                }
            } else {
                zos.putNextEntry(new ZipEntry(basePath));
                FileInputStream fis = new FileInputStream(srcFile);
                int len;
                while ((len = fis.read(buffer)) != -1) {
                    zos.write(buffer, 0, len);
                    zos.flush();
                }
                zos.flush();
                zos.closeEntry();
                fis.close();

            }
            return true;
        } catch (Exception e) {}
        return false;
    }


    /**
     * 解压到指定目录
     */
    public static boolean unZipFiles(String zipPath, String descDir) {
        return unZipFiles(new File(zipPath), descDir);
    }
    /**
     * 解压文件到指定目录
     */
    private static boolean unZipFiles(File zipFile, String descDir) {
        if (!descDir.endsWith("/")) descDir = descDir + "/";
        //File pathFile = new File(descDir);
        try {

            //解决zip文件中有中文目录或者中文文件
            ZipFile zip = (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) ?new ZipFile(zipFile, Charset.forName("GBK")): new ZipFile(zipFile);
            for (Enumeration entries = zip.entries(); entries.hasMoreElements();) {
                ZipEntry entry = (ZipEntry)entries.nextElement();
                String zipEntryName = entry.getName();
                InputStream in = zip.getInputStream(entry);
                String outPath = (descDir + zipEntryName).replaceAll("\\*", "/");
                //判断路径是否存在,不存在则创建文件路径
                File file = new File(outPath.substring(0, outPath.lastIndexOf('/')));
                if (!file.exists()) {
                    file.mkdirs();
                }
                //判断文件全路径是否为文件夹,如果是上面已经上传,不需要解压
                if (new File(outPath).isDirectory()) {
                    continue;
                }
                //输出文件路径信息
                //System.out.println(outPath);
                OutputStream out = new FileOutputStream(outPath);
                byte[] buffer = new byte[1024];
                int len;
                while ((len = in.read(buffer)) > 0) {
                    out.write(buffer, 0, len);
                }
                in.close();
                out.flush();
                out.close();
            }
            return true;
        } catch (Exception e) {}
        return false;
    }


}
