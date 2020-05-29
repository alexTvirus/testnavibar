/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cc.xpcas.nettysocks.handler;

import cc.xpcas.nettysocks.MainController;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.ipfilter.AbstractRemoteAddressFilter;
import java.net.Inet4Address;
import java.net.Inet6Address;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.SocketAddress;

/**
 *
 * @author Alex
 */
public class FilterHandler extends AbstractRemoteAddressFilter {

    @Override
    protected boolean accept(ChannelHandlerContext chc, SocketAddress t) throws Exception {
        InetAddress inetAddress = ((InetSocketAddress) t).getAddress();
//        System.out.println("Your current Hostname : " + ip.getHostAddress());
//        if (inetAddress instanceof Inet4Address) {
//            System.out.println("IPv4: -------------------------------------------------" + inetAddress);
//        } else if (inetAddress instanceof Inet6Address) {
//            System.out.println("IPv6: --------------------------------------------------" + inetAddress);
//        } else {
//            System.err.println("Not an IP address.");
//        }
        if (inetAddress.getHostAddress().equals(InetAddress.getLocalHost().getHostAddress())) {
            return true;
        }
        if (MainController.whiteList.get(0).equals("all")) {
            return true;
        }
        for (String item : MainController.whiteList) {
            if (item.trim().equals(inetAddress.getHostAddress())) {
                //  cho ip nay truy cap
                return true;
            }
        }
        // ko cho ip nay truy cap
        return false;

    }

}
