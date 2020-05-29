/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cc.xpcas.nettysocks.utils;

import Enity.ProxyEntity;
import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.util.List;
import javax.swing.JOptionPane;

/**
 *
 * @author Alex
 */
public class Ghi_file_txt {

    public static synchronized void writeFile(List<ProxyEntity> lists, java.lang.String filename) throws java.io.FileNotFoundException, java.io.UnsupportedEncodingException, java.io.IOException {
        FileOutputStream out = null;
        OutputStreamWriter outputStreamWriter = null;
        BufferedWriter bufferedWriter = null;
        try {
            out = new FileOutputStream(filename);
            outputStreamWriter = new OutputStreamWriter(out, "UTF-8");
            bufferedWriter = new BufferedWriter(outputStreamWriter);
            StringBuffer sb = null;
            for (ProxyEntity item : lists) {
                sb = new StringBuffer();
                sb.append(item.getIp());
                sb.append(":");
                sb.append(item.getPort());
                if (!Utils.isBlank(item.getUsername()) && !Utils.isBlank(item.getPassword())) {
                    sb.append(":");
                    sb.append(item.getUsername());
                    sb.append(":");
                    sb.append(item.getPassword());
                }
                bufferedWriter.write(sb.toString());
                bufferedWriter.newLine();
            }

        } catch (IOException e) {
            JOptionPane.showConfirmDialog(null, e.getMessage());
        } finally {
            if (out != null) {
                bufferedWriter.close();
            }
        }
    }

    public static synchronized void writeFile(java.lang.String content, java.lang.String filename) throws java.io.FileNotFoundException, java.io.UnsupportedEncodingException, java.io.IOException {
        PrintWriter out = null;
        try {
            out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(new FileOutputStream(filename, true), StandardCharsets.UTF_8)));
            out.println(content);
        } catch (IOException e) {
            System.out.println(e.getMessage());
            JOptionPane.showConfirmDialog(null, e.getMessage());
        } finally {
            if (out != null) {
                out.close();
            }
        }
    }

    public static synchronized void clear(java.lang.String filename) throws java.io.FileNotFoundException, java.io.UnsupportedEncodingException, java.io.IOException {
        FileOutputStream out = null;
        OutputStreamWriter outputStreamWriter = null;
        BufferedWriter bufferedWriter = null;
        try {
            out = new FileOutputStream(filename);
            outputStreamWriter = new OutputStreamWriter(out, "UTF-8");
            bufferedWriter = new BufferedWriter(outputStreamWriter);
            bufferedWriter.write("");
        } catch (IOException e) {
            System.out.println(e.getMessage());
            JOptionPane.showConfirmDialog(null, e.getMessage());
        } finally {
            if (out != null) {
                bufferedWriter.close();
            }
        }
    }
}
