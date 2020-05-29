/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cc.xpcas.nettysocks;

import Enity.ProxyEntity;
import PackageChongHack.StringConstant;
import PackageChongHack.StringURL;
import Service.ServiceIndexListServer;
import View.View;
import cc.xpcas.nettysocks.utils.CloneObject;
import cc.xpcas.nettysocks.utils.CloneObjectImpl;
import cc.xpcas.nettysocks.utils.Ghi_file_txt;
import cc.xpcas.nettysocks.utils.Utils;
import docghifile_xulychuoi.Doc_file_kieu_txt;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.URL;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import me.alexpanov.net.FreePortFinder;

/**
 *
 * @author Alex
 */
public class MainController {

    // ko cho hack
    public static boolean accept = false;
    //ktra checkconnect xong chu
    public static int timeRecheck = 1;
    public static Object lock = new Object();
    public static List<ProxyEntity> listProxyInput = Collections.synchronizedList(new ArrayList<ProxyEntity>());
    public static List<ProxyEntity> listProxyRemoved = Collections.synchronizedList(new ArrayList<ProxyEntity>());
    public static List<Server> listserver = Collections.synchronizedList(new ArrayList<Server>());
    public static List<Integer> listPortAvaiable = Collections.synchronizedList(new ArrayList<>());
    public static List<String> whiteList = Collections.synchronizedList(new ArrayList<>());
//    public static List<Integer> listPortToDelete = Collections.synchronizedList(new ArrayList<>());
    public static String SocksType = StringConstant.NUMBER5;
    public static Server s = new Server();
    public static CloneObject cloneObject = new CloneObjectImpl();
//    public static String fileNameFail = "";

    public static void init() {
        whiteList = Doc_file_kieu_txt.readFile(StringConstant.WHITE_LIST_PATH);
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss");
//        fileNameFail = StringConstant.FILE_NAME_FAIL + sdf.format(timestamp) + ".txt";
        Properties pr = Utils.loadProperties(StringConstant.FILE_CONFIG_PATH);
        if (!Utils.isBlank((String) pr.get("LOCAL_HOST"))) {
            StringConstant.LOCAL_HOST = (String) pr.get("LOCAL_HOST");
        }
        if (!Utils.isBlank((String) pr.get("TIME_OUT_CHECK_CONNECT_ALL"))) {
            StringConstant.TIME_OUT_CHECK_CONNECT_ALL = Integer.parseInt((String) pr.get("TIME_OUT_CHECK_CONNECT_ALL"));
        }
        if (!Utils.isBlank((String) pr.get("NUMBER_THREAD_CHECKCONNECT"))) {
            StringConstant.NUMBER_THREAD_CHECKCONNECT = Integer.parseInt((String) pr.get("NUMBER_THREAD_CHECKCONNECT"));
        }
    }

    public static void createProxy(ProxyEntity proxy, int port, int rowid) throws InterruptedException {
        WorkerServer worker1 = new WorkerServer(proxy, port, rowid);
        worker1.start();
        View.jframe.setStatus(ServiceIndexListServer.getRowTableId(rowid), StringConstant.CREATING_PROXY);
    }

    public static String checkTypeSocks(String ip, int port) {
        try {
            SocketAddress addr = new InetSocketAddress(ip, port);
            Proxy proxy = new Proxy(Proxy.Type.HTTP, addr);
            URL url = new URL(StringURL.URL_GOOLE);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection(proxy);
            conn.setRequestMethod(StringConstant.HEAD);
            conn.setConnectTimeout(StringConstant.TIME_OUT_CHECK_CONNECT_ALL);
            conn.setReadTimeout(StringConstant.TIME_OUT_CHECK_CONNECT_ALL);
            int responseCode = conn.getResponseCode();
            if (200 <= responseCode && responseCode <= 399) {
                return StringConstant.HTTP;
            } else if (responseCode == 407) {
                return StringConstant.HTTP;
            } else {
                return StringConstant.SOCKS;
            }
        } catch (Exception e) {
            return StringConstant.SOCKS;
        }
    }

//    public boolean stopProxy(int port) {
//        for (Server server : listserver) {
//            if (port == server.port) {
//                listserver.remove(server);
//                listPortToDelete.remove((Object) port);
//                s.stop(server.acceptors, server.workers, server.future);
//                return true;
//            }
//        }
//        return false;
//    }
    public static void CheckConnect() {
        try {
            ScheduledExecutorService executor = Executors.newScheduledThreadPool(MainController.listserver.size() > StringConstant.NUMBER_THREAD_CHECKCONNECT ? StringConstant.NUMBER_THREAD_CHECKCONNECT : MainController.listserver.size());
            for (Server server : listserver) {
                Thread worker = new WorkerCheckConnect(server);
                executor.schedule(worker, 300, TimeUnit.MILLISECONDS);
            }
            executor.shutdown();
            while (!executor.isTerminated()) {
                if (!View.jframe.btn_check.getText().equals(StringConstant.CHECKING)) {
                    View.jframe.lb_rs_check.setText(StringConstant.CHECKING);
                    View.jframe.jtb_proxy.setEnabled(false);
                    View.jframe.btn_update_whitelist.setEnabled(false);
                    View.jframe.btn_check.setText(StringConstant.CHECKING);
                    View.jframe.txt_time_recheck.setEnabled(false);
                    View.jframe.btn_ExportProxy.setEnabled(false);
                    View.jframe.btn_add_proxy.setEnabled(false);
                }
            }
            View.jframe.txt_time_recheck.setEnabled(true);
            View.jframe.btn_check.setText(StringConstant.BTN_LABEL_CHECK);
            View.jframe.jtb_proxy.setEnabled(true);
            View.jframe.btn_update_whitelist.setEnabled(true);
            View.jframe.btn_ExportProxy.setEnabled(true);
            View.jframe.btn_add_proxy.setEnabled(true);

            if (MainController.listProxyInput.isEmpty()) {
                View.jframe.lb_rs_check.setText(StringConstant.EMPTY_PROXY);
                DeleteProxyInListInputFromListServer();

//                Ghi_file_txt.clear(fileNameFail);
            } else {
                View.jframe.lb_rs_check.setText(StringConstant.NUMBER_PROXY_INPUT + Integer.toString(MainController.listProxyInput.size()));
            }

        } catch (Exception e) {
            e.getMessage();
        }

    }

    public static void CheckConnectOneIP(int port, int rowid) {
        new Thread() {
            public void run() {
                try {
                    String rs = checkConnectForChangeIp(port);
                    JOptionPane.showConfirmDialog(null, rs);
                    View.jframe.setStatus(rowid, rs);
                    if (StringConstant.CONNECTED.equals(rs)) {
                        for (Server server : MainController.listserver) {
                            if (server.port == port) {
                                server.StatusConnect = rs;
                            }
                        }
                    }
                } catch (IOException ex) {
                    System.out.println(ex.getMessage());
                }
            }
        }.start();
    }

    public static String checkConnectForChangeIp(int port) throws IOException {
        Socket socket = null;
        BufferedWriter wr = null;
        try {
            SocketAddress addr = new InetSocketAddress(StringConstant.LOCAL_HOST, port);
            Proxy proxy = new Proxy(Proxy.Type.SOCKS, addr);
            socket = new Socket(proxy);
            socket.connect(new InetSocketAddress(InetAddress.getByName("sql1-177218.appspot.com"), 80), 4000);
            wr = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream(), "UTF8"));
            wr.write("HEAD / HTTP/1.1\r\n");
            wr.write("Host: " + StringURL.URL_GOOLE + "\r\n\r\n");
            wr.flush();
//            BufferedReader in5 = new BufferedReader(new InputStreamReader(socket.getInputStream(), "UTF-8"));
//            StringBuffer response = new StringBuffer();
//            String inputLine;
//            while ((inputLine = in5.readLine()) != null) {
//                response.append(inputLine);
//            }
//            in5.close();
            if (socket.getInputStream().read() == -1) {
                return StringConstant.DISCONNECTED;
            } else {
                return StringConstant.CONNECTED;
            }
//            SocketAddress addr = new InetSocketAddress(StringConstant.LOCAL_HOST, port);
            //            Proxy proxy = new Proxy(Proxy.Type.SOCKS, addr);
            //            URL url = new URL(StringURL.URL_GOOLE);
            //            HttpURLConnection conn = (HttpURLConnection) url.openConnection(proxy);
            //            conn.setRequestMethod(StringConstant.HEAD);
            //            conn.setConnectTimeout(StringConstant.TIME_OUT_CHECK_CONNECT_ALL);
            //            conn.setReadTimeout(StringConstant.TIME_OUT_CHECK_CONNECT_ALL);
            //            int responseCode = conn.getResponseCode();
            //            if (200 <= responseCode && responseCode <= 399) {
            //                return StringConstant.CONNECTED;
            //            } else if (responseCode == -1) {
            //                return StringConstant.DISCONNECTED;
            //            }

        } catch (Exception e) {
            e.getMessage();
        } finally {
            if (wr != null) {
                wr.close();
            }
            if (socket != null) {
                socket.close();
            }

        }
        return StringConstant.DISCONNECTED;
    }

    public static void changeip(ProxyEntity proxy, int port) throws InterruptedException, UnsupportedEncodingException, IOException {
        boolean isConnect = false;
        while ((MainController.listProxyInput.size() != 0) && !isConnect) {
            // xoa proxy trong list
            synchronized (MainController.lock) {
                if (MainController.listProxyInput.size() != 0) {
                    proxy = MainController.listProxyInput.get(0);
                    MainController.listProxyRemoved.add(MainController.listProxyInput.get(0));
                    MainController.listProxyInput.remove(MainController.listProxyInput.get(0));
                }
            }
            if ((MainController.listProxyInput.size() != 0)) {
                // change ip cho port 
                String type = MainController.checkTypeSocks(proxy.getIp(), Integer.parseInt(proxy.getPort()));
                for (Server s : listserver) {
                    if (port == s.port) {
                        View.jframe.setPort(ServiceIndexListServer.getRowTableId(s.rowid), Integer.toString(s.port));
                        View.jframe.setIp(ServiceIndexListServer.getRowTableId(s.rowid), proxy.getIp());
                        View.jframe.setStatus(ServiceIndexListServer.getRowTableId(s.rowid), StringConstant.CREATING_PROXY);
                        if (type.equals(StringConstant.HTTP)) {
                            s.socksProperties.upstream.getAddress().setHost(proxy.getIp());
                            s.socksProperties.upstream.getAddress().setPort(Integer.parseInt(proxy.getPort()));
                            s.socksProperties.upstream.user = proxy.getUsername();
                            s.socksProperties.upstream.pass = proxy.getPassword();
                            s.socksProperties.upstream.TypeProtocol = StringConstant.HTTP;
                        } else {
                            s.socksProperties.upstream.getAddress().setHost(proxy.getIp());
                            s.socksProperties.upstream.getAddress().setPort(Integer.parseInt(proxy.getPort()));
                            s.socksProperties.upstream.user = proxy.getUsername();
                            s.socksProperties.upstream.pass = proxy.getPassword();
                            s.socksProperties.upstream.TypeProtocol = StringConstant.SOCKS;
                        }
                        // check connect lai neu connect xong , disconnect lay proxy khac
                        if (MainController.checkConnectForChangeIp(port).equals(StringConstant.CONNECTED)) {
                            MainController.listserver.get(MainController.listserver.indexOf(s)).proxy = proxy;
                            MainController.listserver.get(MainController.listserver.indexOf(s)).StatusConnect = StringConstant.CONNECTED;
                            View.jframe.setPort(ServiceIndexListServer.getRowTableId(s.rowid), Integer.toString(s.port));
                            View.jframe.setIp(ServiceIndexListServer.getRowTableId(s.rowid), proxy.getIp());
                            View.jframe.setStatus(ServiceIndexListServer.getRowTableId(s.rowid), StringConstant.CONNECTED);
                            isConnect = true;
                            break;
                        } else {
//                            Ghi_file_txt.writeFile(getProxyString(proxy), fileNameFail);
                            View.jframe.setPort(ServiceIndexListServer.getRowTableId(s.rowid), Integer.toString(s.port));
                            View.jframe.setIp(ServiceIndexListServer.getRowTableId(s.rowid), proxy.getIp());
                            MainController.listserver.get(MainController.listserver.indexOf(s)).proxy = proxy;
                            View.jframe.setStatus(ServiceIndexListServer.getRowTableId(s.rowid), StringConstant.DISCONNECTED);
                            isConnect = false;
                            break;
                        }
                    }
                }
            }
        }
        //xoa proxy trong list proxy
        //ghi cac proxy bi xoa vao file
        // doi proxy moi cho port , neu list proxy empty thi ko doi , ko xoa , ko ghi va hien thong bao het proxy
    }

    public static void testchangeip(ProxyEntity proxy, int port) {
        new Thread() {
            public void run() {
                try {
                    for (Server s : MainController.listserver) {
                        s.socksProperties.upstream.getAddress().setHost(proxy.getIp());
                        s.socksProperties.upstream.getAddress().setPort(Integer.parseInt(proxy.getPort()));
                        s.socksProperties.upstream.user = proxy.getUsername();
                        s.socksProperties.upstream.pass = proxy.getPassword();
                        s.socksProperties.upstream.TypeProtocol = StringConstant.SOCKS;
                    }
                    MainController.checkConnectForChangeIp(port);
                } catch (IOException ex) {
                    System.out.println(ex.getMessage());
                }
            }
        }.start();

    }

    public static String getProxyString(ProxyEntity proxy) {
        StringBuffer sb = null;
        sb = new StringBuffer();
        sb.append(proxy.getIp());
        sb.append(":");
        sb.append(proxy.getPort());
        if (!Utils.isBlank(proxy.getUsername()) && Utils.isBlank(proxy.getPassword())) {
            sb.append(":");
            sb.append(proxy.getUsername());
            sb.append(":");
            sb.append(proxy.getPassword());
        }
        return sb.toString();
    }

    public static int getPort(int port) {
        return FreePortFinder.findFreeLocalPort(port);
    }

    // xoa cac proxy da connect trong list server tu list input
    public static void DeleteProxyInListInputFromListServer() {
        // input chua proxy server ko co , remove chua proxy server co
        for (Server server : MainController.listserver) {
            MainController.listProxyRemoved.remove(server.proxy);
        }
        MainController.listProxyInput.addAll(MainController.listProxyRemoved);
        MainController.listProxyRemoved.clear();
        for (Server server : MainController.listserver) {
            MainController.listProxyRemoved.add(server.proxy);
        }

    }
}
