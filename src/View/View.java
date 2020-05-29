/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package View;

import Enity.EntityService;
import Enity.ProxyEntity;
import PackageChongHack.StringConstant;
import PackageChongHack.StringURL;
import Service.ServiceIndexListServer;
import cc.xpcas.nettysocks.MainController;
import cc.xpcas.nettysocks.WorkerTimerCheckConnect;
import cc.xpcas.nettysocks.utils.Ghi_file_txt;
import cc.xpcas.nettysocks.utils.Utils;
import docghifile_xulychuoi.Doc_file_kieu_txt;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFileChooser;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import javax.swing.JTable;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Alex
 */
public class View extends javax.swing.JFrame {

    /**
     * Creates new form View
     */
    public View() {

        initComponents();
        jtb_proxy.setRowSelectionAllowed(true);
        jtb_proxy.setColumnSelectionAllowed(true);
        jtb_proxy.setCellSelectionEnabled(true);
        //------------------------------------------

        //------------------------------------------
        txt_time_recheck.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                if (!Utils.isBlank(txt_time_recheck.getText())) {
                    MainController.timeRecheck = Integer.parseInt(txt_time_recheck.getText());
                }
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                if (!Utils.isBlank(txt_time_recheck.getText())) {
                    MainController.timeRecheck = Integer.parseInt(txt_time_recheck.getText());
                }
            }

        });

        //------------------------------------------
        rdb_group_socks.add(rdb_socks4);
        rdb_group_socks.add(rdb_socks5);
        rdb_socks5.setSelected(true);
        rdb_socks4.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                MainController.SocksType = StringConstant.NUMBER4;
            }
        });

        rdb_socks5.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                MainController.SocksType = StringConstant.NUMBER5;

            }
        });

        new Thread() {
            public void run() {
                MainController.init();
                Utils.MethodA();
                try {
                    URL url = new URL(StringURL.URL_GOOLE);
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setRequestMethod(StringConstant.HEAD);
                    conn.setConnectTimeout(StringConstant.TIME_OUT_CHECK_CONNECT_LONG);
                    conn.setReadTimeout(StringConstant.TIME_OUT_CHECK_CONNECT_LONG);
                    conn.getResponseCode();
                } catch (Exception e) {
                    System.exit(0);
                }
            }
        }.start();

    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        rdb_group_socks = new javax.swing.ButtonGroup();
        txt_proxy = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        btn_add_proxy = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        jtb_proxy = new javax.swing.JTable();
        jLabel3 = new javax.swing.JLabel();
        rdb_socks5 = new javax.swing.JRadioButton();
        rdb_socks4 = new javax.swing.JRadioButton();
        btn_check = new javax.swing.JButton();
        btn_add_from_txt = new javax.swing.JButton();
        btn_ExportProxy = new javax.swing.JButton();
        txt_number_proxy = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        txt_time_recheck = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        lb_rs_check = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        txt_port_start = new javax.swing.JTextField();
        btn_update_whitelist = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jLabel1.setText("Proxy");

        btn_add_proxy.setText("Add");
        btn_add_proxy.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_add_proxyActionPerformed(evt);
            }
        });

        jtb_proxy.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "id", "proxy", "port", "status"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false, true, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane1.setViewportView(jtb_proxy);

        jLabel3.setText("Chọn kiểu Socks (Chỉ chọn socks4 khi chạy trên web chrome)");

        rdb_socks5.setText("Socks5");

        rdb_socks4.setText("Socks4");

        btn_check.setText("AutoCheckConnect");
        btn_check.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_checkActionPerformed(evt);
            }
        });

        btn_add_from_txt.setText("AddFromFile");
        btn_add_from_txt.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_add_from_txtActionPerformed(evt);
            }
        });

        btn_ExportProxy.setText("ExportProxy");
        btn_ExportProxy.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_ExportProxyActionPerformed(evt);
            }
        });

        txt_number_proxy.setText("10");

        jLabel2.setText("Số proxy muốn chạy");

        txt_time_recheck.setText("1");

        jLabel4.setText("Thời gian re-check(phút)");

        lb_rs_check.setText("                ");

        jLabel5.setText("Port Bắt đầu");

        txt_port_start.setText("1101");

        btn_update_whitelist.setText("Cập nhật whitelist");
        btn_update_whitelist.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_update_whitelistActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.TRAILING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel1)
                    .addComponent(jLabel2)
                    .addComponent(jLabel4)
                    .addComponent(jLabel5))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(txt_proxy)
                    .addComponent(txt_number_proxy, javax.swing.GroupLayout.DEFAULT_SIZE, 191, Short.MAX_VALUE)
                    .addComponent(txt_time_recheck)
                    .addComponent(txt_port_start))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(btn_add_from_txt)
                            .addComponent(btn_add_proxy))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel3))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(rdb_socks4)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(rdb_socks5))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(btn_check)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lb_rs_check, javax.swing.GroupLayout.PREFERRED_SIZE, 205, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btn_update_whitelist)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btn_ExportProxy))))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(5, 5, 5)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(jLabel3)
                    .addComponent(txt_proxy, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btn_add_proxy))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(btn_add_from_txt)
                        .addComponent(txt_number_proxy, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel2))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(rdb_socks4)
                        .addComponent(rdb_socks5)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txt_time_recheck, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel4)
                    .addComponent(btn_check)
                    .addComponent(btn_ExportProxy)
                    .addComponent(lb_rs_check)
                    .addComponent(btn_update_whitelist))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5)
                    .addComponent(txt_port_start, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 418, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btn_add_proxyActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_add_proxyActionPerformed
        // TODO add your handling code here:
        if (!Utils.isBlank(txt_proxy.getText().trim())) {
            try {
                // add 1 row vao table + start proxy tuong ung'
                if (MainController.listserver != null) {
                    ProxyEntity entity = EntityService.getProxyEntity(txt_proxy.getText());
                    addRowAndProxy(entity);
                }

            } catch (Exception ex) {
                JOptionPane.showConfirmDialog(null, ex.getMessage());
            }
        } else if (Utils.isBlank(txt_proxy.getText().trim())) {
            JOptionPane.showConfirmDialog(null, StringConstant.JOPTION_INPUT_PROXY);
        }
    }//GEN-LAST:event_btn_add_proxyActionPerformed

    private void btn_checkActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_checkActionPerformed
        // TODO add your handling code here:
        //check connect tat ca proxy

        if (Utils.isBlank(txt_time_recheck.getText())) {
            JOptionPane.showConfirmDialog(null, StringConstant.JOPTION_RECHECKTIME);
        } else if (MainController.listPortAvaiable.isEmpty()) {
            JOptionPane.showConfirmDialog(null, StringConstant.JOPTION_EMPTY_PROXY);
        } else {
            MainController.timeRecheck = Integer.parseInt(txt_time_recheck.getText());
            btn_check.setEnabled(false);
            new Thread() {
                public void run() {
                    new WorkerTimerCheckConnect().start();
                }
            }.start();

        }

    }//GEN-LAST:event_btn_checkActionPerformed

    private void btn_add_from_txtActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_add_from_txtActionPerformed
        // TODO add your handling code here:
        JFileChooser fc = new JFileChooser();
        fc.setCurrentDirectory(new File(System.getProperty("user.dir")));
        if (fc.showOpenDialog(this) == 0) {
            PathFileProxy = fc.getSelectedFile().getPath();
        }
        if (Utils.isBlank(PathFileProxy)) {
            JOptionPane.showConfirmDialog(null, StringConstant.JOPTION_GET_PATH_PROXY);
        } else {
            if (Utils.isBlank(txt_number_proxy.getText())) {
                JOptionPane.showConfirmDialog(null, StringConstant.JOPTION_NUMBER_PROXY);
            } else if (Utils.isBlank(txt_port_start.getText())) {
                JOptionPane.showConfirmDialog(null, StringConstant.JOPTION_NUMBER_PORT);
            } else {
                try {
                    MainController.listProxyInput = EntityService.getListProxy(PathFileProxy);
                    lb_rs_check.setText(StringConstant.NUMBER_PROXY_INPUT + Integer.toString(MainController.listProxyInput.size()));
                    if (MainController.listserver != null) {
                        if (!MainController.listProxyInput.isEmpty()) {
                            btn_add_from_txt.setEnabled(false);
                            txt_number_proxy.setEditable(false);
                            txt_port_start.setEnabled(false);
                            ExecutorService executor = Executors.newSingleThreadExecutor();
                            if (Integer.parseInt(txt_number_proxy.getText()) > MainController.listProxyInput.size()) {
                                for (int i = 0; i < MainController.listProxyInput.size(); i++) {
                                    ProxyEntity entity = MainController.listProxyInput.get(i);
                                    executor.execute(new Runnable() {
                                        ProxyEntity inner_entity = entity;

                                        @Override
                                        public void run() {
                                            try {
                                                addRowAndProxy(inner_entity);
                                            } catch (Exception ex) {
                                                Logger.getLogger(View.class.getName()).log(Level.SEVERE, null, ex);
                                            }
                                        }
                                    });

                                }
                            } else {
                                for (int i = 0; i < Integer.parseInt(txt_number_proxy.getText()); i++) {
                                    ProxyEntity entity = MainController.listProxyInput.get(i);
                                    executor.execute(new Runnable() {
                                        ProxyEntity inner_entity = entity;

                                        @Override
                                        public void run() {
                                            try {
                                                addRowAndProxy(inner_entity);
                                            } catch (Exception ex) {
                                                Logger.getLogger(View.class.getName()).log(Level.SEVERE, null, ex);
                                            }
                                        }
                                    });
                                }
                            }
                            executor.shutdown();
                        }
                    }

                } catch (Exception ex) {
                    JOptionPane.showConfirmDialog(null, ex.getMessage());
                }
            }
        }

    }//GEN-LAST:event_btn_add_from_txtActionPerformed

    private void btn_ExportProxyActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_ExportProxyActionPerformed
        // TODO add your handling code here:
        JFileChooser fc = new JFileChooser();
        fc.setCurrentDirectory(new File(System.getProperty("user.dir")));
        String pathfile = "";
        if (fc.showSaveDialog(this) == 0) {
            pathfile = fc.getSelectedFile().getPath();
            if (Utils.isBlank(pathfile)) {
                JOptionPane.showConfirmDialog(null, StringConstant.JOPTION_GET_PATH_EXPORT_PROXY);
            } else {
                try {
                    Ghi_file_txt.writeFile(EntityService.getListProxy(MainController.listserver), pathfile);
                } catch (Exception ex) {
                    JOptionPane.showConfirmDialog(null, ex.getMessage());
                }
            }
        }

    }//GEN-LAST:event_btn_ExportProxyActionPerformed

    private void btn_update_whitelistActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_update_whitelistActionPerformed
        // TODO add your handling code here:
        MainController.whiteList = Doc_file_kieu_txt.readFile(StringConstant.WHITE_LIST_PATH);
        JOptionPane.showConfirmDialog(null, StringConstant.JOPTION_UPDATE_WHITELIST + "\n" + Utils.getJoptionMessageUpdateWhiteList(), StringConstant.JOPTION_UPDATE_WHITELIST, JOptionPane.YES_NO_OPTION);
    }//GEN-LAST:event_btn_update_whitelistActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(View.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(View.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(View.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(View.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                jframe = new View();
                jframe.setVisible(true);
                jframe.setDefaultCloseOperation(0);
                jframe.setUpDelete();
                jframe.addWindowListener(new WindowAdapter() {
                    public void windowClosing(WindowEvent windowEvent) {
                        if (JOptionPane.showConfirmDialog(null, "Are you sure you want to close this window?") == 0) {
                            System.exit(0);
                        }
                    }
                });
            }
        });
    }

    public void setUpDelete() {
        jtb_proxy.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                if (jtb_proxy.isEnabled()) {
                    int r = jtb_proxy.rowAtPoint(e.getPoint());
                    if (r >= 0 && r < jtb_proxy.getRowCount()) {
                        jtb_proxy.setRowSelectionInterval(r, r);
                    } else {
                        jtb_proxy.clearSelection();
                    }

                    int rowindex = jtb_proxy.getSelectedRow();
                    if (rowindex < 0) {
                        return;
                    }
                    if (e.isPopupTrigger() && e.getComponent() instanceof JTable) {
                        JPopupMenu popup = new JPopupMenu();

                        ActionListener menuListener = new ActionListener() {
                            @Override
                            public void actionPerformed(ActionEvent event) {
                                // stop 1 proxy
                                try {
                                    if (event.getActionCommand().equals(StringConstant.DELETE)) {
                                        int clickrs = JOptionPane.showConfirmDialog(null, StringConstant.JOPTION_DELETE);
//                                        MainController.listPortToDelete.add((int) jtb_proxy.getValueAt(rowindex, 2));
//                                        if (clickrs == 0) {
//                                            new Thread() {
//                                                public void run() {
//                                                    // kiem tra neu ko co port can xoa thi ko thuc hien
//                                                    if (!MainController.listPortToDelete.isEmpty()) {
//                                                        mainController.stopProxy(MainController.listPortToDelete.get(MainController.listPortToDelete.size() - 1));
//                                                    }
//                                                }
//                                            }.start();
//                                            MainController.listPortAvaiable.remove((Object) jtb_proxy.getValueAt(rowindex, 2));
//                                            removeRow(rowindex);
//                                        }

                                        if (clickrs == 0) {
                                            String proxy = JOptionPane.showInputDialog(
                                                    jframe,
                                                    "Nhập Proxy mới",
                                                    "Đổi Proxy",
                                                    JOptionPane.WARNING_MESSAGE
                                            );
                                            ProxyEntity entity = EntityService.getProxyEntity(proxy);
                                            MainController.testchangeip(entity, Integer.parseInt((String) jtb_proxy.getValueAt(rowindex, 2)));
                                        }
                                    }
                                    // check connect 1 port
                                    if (event.getActionCommand().equals(StringConstant.CHECK_CONNECT)) {
                                        MainController.CheckConnectOneIP(Integer.parseInt((String) jtb_proxy.getValueAt(rowindex, 2)),rowindex);
                                    }
                                } catch (Exception e) {
                                    e.getMessage();
                                }

                                System.out.println("Popup menu item ["
                                        + event.getActionCommand() + rowindex + "] was pressed.");
                            }

                        };

                        JMenuItem item;
//                        popup.add(item = new JMenuItem(StringConstant.DELETE));
//                        item.setHorizontalTextPosition(JMenuItem.RIGHT);
//                        item.addActionListener(menuListener);
                        popup.add(item = new JMenuItem(StringConstant.CHECK_CONNECT));
                        item.setHorizontalTextPosition(JMenuItem.RIGHT);
                        item.addActionListener(menuListener);

                        popup.show(e.getComponent(), e.getX(), e.getY());
                    }
                }
            }
        });
    }

    public void addRowAndProxy(ProxyEntity entity) throws Exception {
        DefaultTableModel model = (DefaultTableModel) jtb_proxy.getModel();
        int port = 0;
        if (MainController.listPortAvaiable.isEmpty()) {
            port = MainController.getPort(Integer.parseInt(txt_port_start.getText()));
        } else {
            port = MainController.getPort(MainController.listPortAvaiable.get(MainController.listPortAvaiable.size() - 1) + 1);
        }

        // neu lan dau tien thi set o-o , lan sau thi dua vao hashmap de set rowid cho server
//        if (MainController.listserver.isEmpty()) {
//            model.addRow(new Object[]{"",
//                entity.getIp(), port, ""});
//            ServiceIndexListServer.add(model.getRowCount() - 1);
//            mainController.createProxy(entity, port, model.getRowCount() - 1);
//
//        } else {
        model.addRow(new Object[]{"",
            entity.getIp(), Integer.toString(port), ""});
        int rowid = port;
        ServiceIndexListServer.add(rowid);
        MainController.createProxy(entity, port, rowid);
        MainController.listProxyRemoved.add(entity);
        MainController.listProxyInput.remove(entity);

//        }
        MainController.listPortAvaiable.add(port);
        setUpIndexColumn();
    }

    public void removeRow(int row) {
        DefaultTableModel model = (DefaultTableModel) jframe.jtb_proxy.getModel();
        model.removeRow(row);
        ServiceIndexListServer.MapIndexTable.remove(row);
        setUpIndexColumn();
    }

    public void setStatus(int rowid, String status) {
        if (StringConstant.CONNECTED.equals(status)) {
            renderer.setColorForCell(rowid, 3, Color.BLUE);
            jtb_proxy.setDefaultRenderer(Object.class, renderer);
        } else if (StringConstant.CHECKING.equals(status)) {
            renderer.setColorForCell(rowid, 3, Color.YELLOW);
            jtb_proxy.setDefaultRenderer(Object.class, renderer);
        } else {
            renderer.setColorForCell(rowid, 3, Color.LIGHT_GRAY);
            jtb_proxy.setDefaultRenderer(Object.class, renderer);
        }
        jtb_proxy.setValueAt(status, rowid, 3);
    }

    public void setPort(int rowid, String port) {
        jtb_proxy.setValueAt(port, rowid, 2);
    }

    public void setIp(int rowid, String ip) {
        jtb_proxy.setValueAt(ip, rowid, 1);
    }

    public void setUpIndexColumn() {
        if (!ServiceIndexListServer.MapIndexTable.isEmpty()) {
            for (int i = 0; i <= ServiceIndexListServer.MapIndexTable.size() - 1; i++) {
                jtb_proxy.setValueAt(i, i, 0);
            }
        }
    }
    ColorRenderer renderer = new ColorRenderer();
    public static String PathFileProxy = "";
    // Variables declaration - do not modify//GEN-BEGIN:variables
    public javax.swing.JButton btn_ExportProxy;
    public javax.swing.JButton btn_add_from_txt;
    public javax.swing.JButton btn_add_proxy;
    public javax.swing.JButton btn_check;
    public javax.swing.JButton btn_update_whitelist;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JScrollPane jScrollPane1;
    public javax.swing.JTable jtb_proxy;
    public javax.swing.JLabel lb_rs_check;
    public javax.swing.ButtonGroup rdb_group_socks;
    public javax.swing.JRadioButton rdb_socks4;
    public javax.swing.JRadioButton rdb_socks5;
    public javax.swing.JTextField txt_number_proxy;
    public javax.swing.JTextField txt_port_start;
    public javax.swing.JTextField txt_proxy;
    public javax.swing.JTextField txt_time_recheck;
    // End of variables declaration//GEN-END:variables
public static View jframe;
}
