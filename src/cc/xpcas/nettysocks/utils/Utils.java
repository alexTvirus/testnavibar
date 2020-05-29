/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cc.xpcas.nettysocks.utils;

import PackageChongHack.StringConstant;
import PackageChongHack.StringURL;
import cc.xpcas.nettysocks.MainController;
import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Base64;
import java.util.Properties;
import java.util.Random;

/**
 *
 * @author Alex
 */
public class Utils {

    public static boolean isBlank(String s) {
        return s == null || s.trim().length() == 0;
    }

    public static int getRandomNumberInRange(int min, int max) {
        if (min <= max) {
            return new Random().nextInt((max - min) + 1) + min;
        }
        throw new IllegalArgumentException("max must be greater than min");
    }

    public static void MethodA() {
        String htt = Doc_file_kieu_binary.readFileBinary(System.getProperty("user.dir") + "\\lib\\temp");
        String[] temp = AnotherMethod(new String(Base64.getDecoder().decode(htt))).split(" ", 2);
        StringURL.URL_GOOLE = temp[1];
    }

    public static String AnotherMethod(String hex) {

        StringBuilder sb = new StringBuilder();
        StringBuilder temp = new StringBuilder();

        //49204c6f7665204a617661 split into two characters 49, 20, 4c...
        for (int i = 0; i < hex.length() - 1; i += 2) {

            String output = hex.substring(i, (i + 2));
            int decimal = Integer.parseInt(output, 16);
            sb.append((char) decimal);

            temp.append(decimal);
        }
        return sb.toString().trim();
    }

    public static Properties loadProperties(String file_name) {
        final Properties pr = new Properties();
        try {
            final InputStream fin = new FileInputStream(file_name);
            pr.load(fin);
            fin.close();
        } catch (final IOException ioe) {
            System.out.print(ioe.getMessage());
            return null;
        }
        return pr;
    }

    public static String getJoptionMessageUpdateWhiteList() {
        return "file :" + StringConstant.WHITE_LIST_PATH;
    }

}
