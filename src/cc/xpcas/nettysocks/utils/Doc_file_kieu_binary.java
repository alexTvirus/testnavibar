/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cc.xpcas.nettysocks.utils;

import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.util.List;
import java.util.Set;

/**
 *
 * @author Alex
 */
public class Doc_file_kieu_binary {

    public static String readFileBinary(String path) {
        try {
            FileInputStream fis = new FileInputStream(path);
            ObjectInputStream ois = new ObjectInputStream(fis);
            String lists = (String) ois.readObject();
            ois.close();
            return lists;
        } catch (Exception e) {
            e.getMessage();
        }
        return null;
    }
}
