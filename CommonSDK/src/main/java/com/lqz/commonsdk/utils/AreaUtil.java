package com.lqz.commonsdk.utils;

import com.lqz.commonsdk.application.AppContext;

import java.util.Locale;

public class AreaUtil {
    public static AreaUnit getAreaUnit() {
        Locale locale = Locale.getDefault();
        if (locale.getLanguage().endsWith("zh")) { //中文
            return AreaUnit.MU;
        } else if(locale.getLanguage().equals(Locale.JAPANESE.getLanguage())){ //日语
            return AreaUnit.HA;
        }else if(locale.getLanguage().equalsIgnoreCase("th")){ //泰语
            return AreaUnit.RAI;
        }else{ //其他语言
            return AreaUnit.ACRE;
        }
    }
    //该方法被弃用了，如果需要修改，参考系统助手上设置语言
    public static void setAreaUnit(AreaUnit areaUnit) {
//        Locale locale = Locale.getDefault();
//        if (locale.getLanguage().endsWith("zh")) {
//            return;
//        } else {
//            ACache.get(AppContext.getInstance()).put(ACacheApi.Param.areaUnit, areaUnit);
//        }
    }

    public static double convertAreaUnit(float value, AreaUnit sourceUnit, AreaUnit targetUnit){
        return value*sourceUnit.factorToM2/targetUnit.factorToM2;
    }
    // 0平方 1 是亩 2是公亩 3英亩 4公顷 5rai是泰国单位
    public enum AreaUnit {
        M2(0, "m²", 1),
        MU(1, "mu", 666.666),
        ARE(2, "are", 100),
        ACRE(3, "acre", 4046.86),
        HA(4, "ha", 10000),
        RAI(5, "Rai", 1600);

        public int type;
        public String name;
        public double factorToM2;

        AreaUnit(int type, String name, double factorToM2) {
            this.type = type;
            this.name = name;
            this.factorToM2 = factorToM2;
        }
    }
}
