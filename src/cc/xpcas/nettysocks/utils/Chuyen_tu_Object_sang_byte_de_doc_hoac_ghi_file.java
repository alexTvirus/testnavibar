/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cc.xpcas.nettysocks.utils;


import java.io.ByteArrayOutputStream;
import java.io.ObjectOutputStream;
import java.util.List;
import java.util.Set;

/**
 *
 * @author Alex
 */
public class Chuyen_tu_Object_sang_byte_de_doc_hoac_ghi_file {

    public static byte[] ObjectToByte(String s) {

        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ObjectOutputStream oos1 = new ObjectOutputStream(baos);
            oos1.writeObject(s);
            oos1.flush();
            return baos.toByteArray();
        } catch (Exception e) {
            e.getMessage();
        }
        return null;
    }
}
