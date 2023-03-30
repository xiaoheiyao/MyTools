package com.lqz.commonres.utils;

import android.content.Context;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Environment;

import com.lqz.commonres.R;
import com.lqz.commonsdk.application.AppContext;
import com.lqz.commonsdk.utils.AreaUtil;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

//todo 这个类叫这个名字不合适，应该拆分一下
public class CommonUtil {

    /**
     * 是否连接上网络
     *
     * @param context
     * @return
     */
    public static boolean isNetworkConnected(Context context) {
        if (context != null) {
            ConnectivityManager mConnectivityManager = (ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo mNetworkInfo = mConnectivityManager.getActiveNetworkInfo();
            if (mNetworkInfo != null) {
                return mNetworkInfo.isAvailable();
            }
        }
        return false;
    }

    private static Map<String, Object> dataMap;

    public static void isDataChange(String name, Object value, Runnable changeRun) {
        if (name == null || value == null || changeRun == null) {
            return;
        }
        if (dataMap == null) {
            dataMap = new HashMap();
        }
        Object valueForMap = dataMap.get(name);
        if (valueForMap != null) {
            if (!valueForMap.equals(value)) {
                dataMap.put(name, value);
                changeRun.run();
            }
        } else {
            dataMap.put(name, value);
        }
    }

    public static double[] parseGpsToDms(double gps) {
        double[] dmsGps = new double[3];
        int d = (int) gps;
        int m = (int) ((gps - d) * 60);
        double s = ((gps - d) * 60 - m) * 60;
        dmsGps[0] = d;
        dmsGps[1] = m;
        dmsGps[2] = s;
        return dmsGps;
    }

    public static double parseGpsToDegree(double[] dmsGps) {
        double d = (int) dmsGps[0];
        double m = (int) dmsGps[1];
        double s = dmsGps[2];
        double degree = d + m / 60d + s / 3600d;
        return degree;
    }

    public static String getKeepFormatTime(int time) {
        int hour = time / 3600;
        int minute = (time - 3600 * hour) / 60;
        int s = time % 60;
        DecimalFormat df = new DecimalFormat("00");
        String allTime = df.format(hour) + ":" + df.format(minute) + ":" + df.format(s);
        return allTime;
    }

    public static String getAreaUnitName(AreaUtil.AreaUnit areaUnit) {
        String name = "";
        if (areaUnit == AreaUtil.AreaUnit.MU) {
            name = AppContext.getResString(R.string.public_mu);
        } else if (areaUnit == AreaUtil.AreaUnit.ARE) {
            name = AppContext.getResString(R.string.public_are);
        } else if (areaUnit == AreaUtil.AreaUnit.ACRE) {
            name = AppContext.getResString(R.string.public_acre);
        } else if (areaUnit == AreaUtil.AreaUnit.HA) {
            name = AppContext.getResString(R.string.public_ha);
        }else if (areaUnit == AreaUtil.AreaUnit.RAI) {
            name = AppContext.getResString(R.string.public_rai);
        }
        return name;
    }

    /**
     * 根据国际化获取对应的面积
     *
     * @param area（㎡）
     * @return
     */
    public static String getAreaFormat(Context context, float area) {
        AreaUtil.AreaUnit areaUnit = AreaUtil.getAreaUnit();
        DecimalFormat decimalFormat = new DecimalFormat("0.00");
        float unitArea = CommonUtil.keep2Decimal((float) (area / areaUnit.factorToM2));
        if (unitArea > 0.01) {
            return decimalFormat.format(unitArea) + getAreaUnitName(areaUnit);
        } else {
            return decimalFormat.format(area) + "㎡";
        }

    }

    /**
     * 根据国际化获取对应的面积 不带单位
     *
     * @param area（㎡）
     * @return 保留两位小数
     */
    public static String getAreaFormat_2(Context context, float area) {
        AreaUtil.AreaUnit areaUnit = AreaUtil.getAreaUnit();
        DecimalFormat decimalFormat = new DecimalFormat("0.00");
        float unitArea = CommonUtil.keep2Decimal((float) (area / areaUnit.factorToM2));
//        if (unitArea > 0.01) {
            return decimalFormat.format(unitArea);
//        } else {
//            return decimalFormat.format(area);
//        }

    }

    /**
     * 获得对应的面积 不带单位
     * @return
     */
    public static String getAreaUnit(){
        AreaUtil.AreaUnit areaUnit = AreaUtil.getAreaUnit();
        return getAreaUnitName(areaUnit);
    }


    /**
     * 根据国际化获取对应的面积 不带单位
     *
     * @param area（㎡）
     * @return 保留一位小数
     */
    public static String getAreaFormat2(Context context, float area) {
        AreaUtil.AreaUnit areaUnit = AreaUtil.getAreaUnit();
        DecimalFormat decimalFormat = new DecimalFormat("0.0");
        float unitArea = CommonUtil.keep2Decimal((float) (area / areaUnit.factorToM2));
        if (unitArea > 0.01) {
            return decimalFormat.format(unitArea);
        } else {
            return decimalFormat.format(area) ;
        }

    }

    @SuppressWarnings("unchecked")
    public static <T> T cloneTo(T src) throws RuntimeException {
        ByteArrayOutputStream memoryBuffer = new ByteArrayOutputStream();
        ObjectOutputStream out = null;
        ObjectInputStream in = null;
        T dist = null;
        try {
            out = new ObjectOutputStream(memoryBuffer);
            out.writeObject(src);
            out.flush();
            in = new ObjectInputStream(new ByteArrayInputStream(memoryBuffer.toByteArray()));
            dist = (T) in.readObject();
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            if (out != null) {
                try {
                    out.close();
                    out = null;
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
            if (in != null) {
                try {
                    in.close();
                    in = null;
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }
        return dist;
    }

    /**
     * 数组深度拷贝
     *
     * @param src
     * @param <T>
     * @return
     * @throws IOException
     * @throws ClassNotFoundException
     */
    public static <T> List<T> deepCopy(List<T> src) {
        ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
        ObjectOutputStream out = null;
        List<T> dest = null;
        try {
            out = new ObjectOutputStream(byteOut);
            out.writeObject(src);
            ByteArrayInputStream byteIn = new ByteArrayInputStream(byteOut.toByteArray());
            ObjectInputStream in = new ObjectInputStream(byteIn);
            dest = (List<T>) in.readObject();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }


        return dest;
    }

    public static float keep2Decimal(float number) {
        BigDecimal b = new BigDecimal(number);
        float f1 = b.setScale(2, BigDecimal.ROUND_HALF_UP).floatValue();
        return f1;
    }

    public static float keep1Decimal(float number) {
        BigDecimal b = new BigDecimal(number);
        float f1 = b.setScale(1, BigDecimal.ROUND_HALF_UP).floatValue();
        return f1;
    }

    /**
     * 是否打开GPS
     *
     * @return true：打开 false：未打开
     */
    public static boolean hasOpenGPS(Context context) {
        LocationManager locationManager =
                ((LocationManager) context.getSystemService(Context.LOCATION_SERVICE));
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
    }

    public static String formatAccount(String account) {
        if (account.contains("@")) {
            String emails = account.replaceAll("(\\w?)(\\w+)(\\w)(@\\w+\\.[a-z]+(\\.[a-z]+)?)", "$1****$3$4");
            return emails;
        } else {
            String phone_s = account.replaceAll("(\\d{3})\\d{4}(\\d{4})", "$1****$2");
            return phone_s;
        }
    }

    public static String copyImageFromRawToSDCard(String fileName) throws IOException {
        String sdcardPath = Environment.getExternalStorageDirectory().toString();
        String dirPath = sdcardPath + "/TxgAgric";

        File dir = new File(dirPath);
        if (!dir.exists() || !dir.isDirectory()) {
            dir.mkdir();
        }

        dirPath = dirPath + "/share";
        dir = new File(dirPath);
        if (!dir.exists() || !dir.isDirectory()) {
            dir.mkdir();
        }

        File file = new File(dir, fileName);
        if (file.exists() && file.isFile()) {
            return file.getPath();
        }

        InputStream inputStream = null;
        OutputStream outputStream = null;

        //通过IO流的方式，将assets目录下的文件，写入到SD卡中。
        if (!file.exists()) {
            try {
                file.createNewFile();

                inputStream = AppContext.getInstance().getClass().getClassLoader().getResourceAsStream("assets/" + fileName);
                outputStream = new FileOutputStream(file);

                byte[] buffer = new byte[1024];
                int len;

                while ((len = inputStream.read(buffer)) != -1) {
                    outputStream.write(buffer, 0, len);
                }

            } catch (IOException e) {
                e.printStackTrace();
            } finally {

                if (outputStream != null) {
                    outputStream.flush();
                    outputStream.close();
                }

                if (inputStream != null) {
                    inputStream.close();
                }
            }
        }
        return file.getPath();
    }
}