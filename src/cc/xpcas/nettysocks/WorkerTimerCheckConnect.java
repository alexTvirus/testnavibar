/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cc.xpcas.nettysocks;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Alex
 */
public class WorkerTimerCheckConnect extends Thread {


    public WorkerTimerCheckConnect() {
    }

    @Override
    public void run() {
        while (true) {
            try {
                MainController.CheckConnect();
                Thread.sleep(MainController.timeRecheck * 60000);
            } catch (InterruptedException ex) {
                System.out.println(ex.getMessage());
            }
        }
    }
}
