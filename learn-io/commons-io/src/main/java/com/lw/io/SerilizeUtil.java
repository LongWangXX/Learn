package com.lw.io;

import java.io.*;
import java.util.HashMap;

public class SerilizeUtil {
    public static void serilize(Object object, String path) {
        File f = new File(path);
        OutputStream os = null;
        ObjectOutputStream oos = null;
        try {
            os = new FileOutputStream(f);
            oos = new ObjectOutputStream(os);
            oos.writeObject(object);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (os != null) {
                try {
                    os.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (oos != null) {
                try {
                    oos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static Object deSerilize(String path) {
        InputStream is = null;
        ObjectInputStream ois = null;
        try {
            File f = new File(path);
            is = new FileInputStream(f);
            ois = new ObjectInputStream(is);
            return ois.readObject();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (ois != null) {
                try {
                    ois.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }


        }

        return null;
    }

    public static void main(String[] args) {
        HashMap<String, Integer> map = new HashMap<>();
        map.put("abc", 12);
        serilize(map, "D:\\abc\\position.file");
        Object o = deSerilize("D:\\abc\\position.file");
        System.out.println(o);
        HashMap<String, Integer> map2 = (HashMap<String, Integer>) o;
        map2.put("a", 2);
        System.out.println(map2);
        System.out.println(map);
        Object o1 = deSerilize("D:\\abc\\position.file");
        System.out.println(o1);
    }
}