/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Service;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Alex
 */
public class ServiceIndexListServer {

    public static List<Integer> MapIndexTable = new ArrayList<>();

    public static void test() {

    }

    public static int getRowTableId(Integer rowServer) {
        return MapIndexTable.indexOf(rowServer);
    }

    public static void add(Integer rowServer) {
        MapIndexTable.add(rowServer);
    }

    public static void delete(Integer rowServer) {
        MapIndexTable.remove(rowServer);
    }

}
