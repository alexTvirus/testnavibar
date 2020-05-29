/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cc.xpcas.nettysocks;

import Enity.ProxyEntity;
import PackageChongHack.StringConstant;
import Service.ServiceIndexListServer;
import View.View;

import cc.xpcas.nettysocks.config.Address;
import cc.xpcas.nettysocks.utils.Utils;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Alex
 */
public class WorkerServer extends Thread {

    private ProxyEntity proxy;
    private int port;
    private int rowid;

    public WorkerServer(ProxyEntity proxy, int port, int rowid) {
        this.proxy = proxy;
        this.port = port;
        this.rowid = rowid;
    }

    @Override
    public void run() {
        try {
            Server s = new Server();
            String type = MainController.checkTypeSocks(proxy.getIp(), Integer.parseInt(proxy.getPort()));
            if (type.equals(StringConstant.HTTP)) {
                s = s.sockHTTP(new Address(proxy.getIp(), Integer.parseInt(proxy.getPort())), proxy, port, rowid);
            } else {
                s = s.socks5Main(new Address(proxy.getIp(), Integer.parseInt(proxy.getPort())), proxy, port, rowid);
            }
            MainController.listserver.add(s);
            s.proxy = proxy;
            View.jframe.setStatus(ServiceIndexListServer.getRowTableId(rowid), StringConstant.CONNECTED);
            s.start();

        } catch (InterruptedException ex) {
            Logger.getLogger(WorkerServer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
