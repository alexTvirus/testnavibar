/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Enity;

import CustomException.WrongProxyException;
import PackageChongHack.StringConstant;
import cc.xpcas.nettysocks.Server;
import cc.xpcas.nettysocks.utils.Utils;
import docghifile_xulychuoi.Doc_file_kieu_txt;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Alex
 */
public class EntityService {

    public static ProxyEntity getProxyEntity(String proxy) throws Exception {
        ProxyEntity entity = new ProxyEntity();
        try {
            String[] temp = (proxy.trim().replaceAll("\\s+", "")).split(":", 4);
            if (temp.length == 2) {
                entity.setIp(temp[0]);
                entity.setPort(temp[1]);
            } else if (temp.length == 4) {
                entity.setIp(temp[0]);
                entity.setPort(temp[1]);
                entity.setUsername(temp[2]);
                entity.setPassword(temp[3]);
            } else {
                throw new WrongProxyException(StringConstant.EXCEPTION_INPUT_PROXY);
            }
            return entity;
        } catch (Exception e) {
            throw e;
        }
    }

    public static List<ProxyEntity> getListProxy(String pathproxy) throws Exception {
        List<String> S_lists = Doc_file_kieu_txt.readFile(pathproxy);
        List<ProxyEntity> px_lists = new ArrayList<>();

        for (String item : S_lists) {
            if (!Utils.isBlank(item.trim())) {
                px_lists.add(getProxyEntity(item));
            }
        }
        // loai proxy trung
        for (int i = px_lists.size() - 1; i > 0; i--) {
            for (int j = i - 1; j >= 0; j--) {
                if (px_lists.get(i).getIp().equals(px_lists.get(j).getIp())
                        && px_lists.get(i).getPort().equals(px_lists.get(j).getPort())) {
                    px_lists.remove(j);
                    i--;
                }
            }
        }
        return px_lists;
    }

    public static List<ProxyEntity> getListProxy(List<Server> server) throws Exception {
        List<ProxyEntity> px_lists = new ArrayList<>();
        ProxyEntity entity = null;
        for (Server item : server) {
            if (item.StatusConnect.equals(StringConstant.CONNECTED)) {
                entity = new ProxyEntity();
                entity.setIp(InetAddress.getLocalHost().getHostAddress());
                entity.setPort(Integer.toString(item.port));
                px_lists.add(entity);
            }
        }

        return px_lists;
    }
}
