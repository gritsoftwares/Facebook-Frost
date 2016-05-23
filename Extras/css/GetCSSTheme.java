package com.pitchedapps.facebook.frost.webHelpers;

import android.util.Log;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;

public class GetCSSTheme {
    static String dir = "C:\\Users\\user1\\PA\\FB\\Extras\\css";
    static String newFileName = "css_new.txt";
    static String carryOver = "";
    static String[] keys = {"background:", "background-color", "box-shader"};
    static StringBuilder result = new StringBuilder();

    public static void main(String[] args) {
//		dir = new File(GetCSSTheme.class.getProtectionDomain().getCodeSource().getLocation().getPath()).getParent();
//		dir = dir.replace("%20", " ");
//        getThemeComponents(dir + "\\css.txt");

//        keys = new String[] {"background:", "background-color", "box-shader"};
		test();
    }

    public static void test() {
        StringBuilder result = new StringBuilder();
        result.append("TEST");
        try (Writer writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(dir + "\\new_css.txt"), "utf-8"))) {
            writer.append(result);
            writer.close();
        } catch (Exception e) {
            // This should never happen
            e.printStackTrace();
        }
    }

    public static void getThemeComponents(String file) {
        String line = "";
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            while ((line = br.readLine()) != null) {
               smallThemeComponent(carryOver + line);
            }
        } catch (IOException e) {
            System.out.println("ERROR :" + e + "\n" + line);
        }

        try {
            try (Writer writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(dir + "\\new_css.txt"), "utf-8"))) {
                writer.append(result);
                writer.close();
            } catch (Exception e) {
                // This should never happen
                e.printStackTrace();
            }
//			writer.append(result);
//			if (writer != null) {
//				writer.close();
//			}
        } catch (Exception e) {
            // This should never happen
            e.printStackTrace();
        }
    }

    private static void smallThemeComponent(String s) {
        while (s.contains("}")) {
            int left = s.indexOf("\\{");
            int right = s.indexOf("}");
            if (left == -1 || right == -1) {
                carryOver = s;
                return;
            }
            String inner = s.substring(left, right);
            Log.d("SSS", s);
            for (String key : keys) {
                if (inner.contains(key)) {
                    result.append(s.substring(0, right+1));
                    Log.d("result:", s.substring(0, right+1));
                    break;
                }
            }
            s = s.substring(right+1);
        }
        if (s.length() != 0) {
            carryOver = s;
        }
    }
}
