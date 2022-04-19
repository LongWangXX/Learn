package com.lw.data.importer;

public class Util {
    /**
     * 小驼峰转蛇形
     *
     * @param string
     * @return
     */
    public static String toHumpString(String string) {

        StringBuilder sb = new StringBuilder(string);

        int temp = 0;
        for (int i = 0; i < string.length(); i++) {
            if (Character.isUpperCase(string.charAt(i))) {
                sb.insert(i + temp, "_");
                temp += 1;
            }
        }
        return sb.toString().toLowerCase();
    }
}
