/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package PackageChongHack;

/**
 *
 * @author Alex
 */
public class StringConstant {

    public static String LOCAL_HOST = "0.0.0.0";
    public static String CONNECTED = "Connected";
    public static String DISCONNECTED = "Disconnected";
    public static String CHECKING = "Checking...";
    public static String HTTP = "HTTP";
    public static String SOCKS = "Socks";
    public static String HEAD = "HEAD";
    public static String CREATING_PROXY = "Creating proxy...";
    public static String NUMBER5 = "5";
    public static String NUMBER4 = "4";
    public static String EXCEPTION_INPUT_PROXY = "định dạng proxy sai hãy nhập đúng mẫu proxy";
    public static String JOPTION_INPUT_PROXY = "Hãy nhập proxy có dạng (ip:port) hoặc (ip:port:user:pass)";
    public static String DELETE = "ChangeProxy";
    public static String JOPTION_DELETE = "Bạn muốn đổi proxy này?";
    public static String CHECK_CONNECT = "CheckConnect";
    public static int NUMBER_THREAD_CHECKCONNECT = 100;
    public static int NUMBER_THREAD_ADD_PROXY = 20;
    public static String WHITE_LIST_PATH = System.getProperty("user.dir") + "\\config\\white-list.txt";
    public static String EMPTY_PROXY = "Đã dùng hết proxy";
    public static String NUMBER_PROXY_INPUT = "Số proxy còn lại:";
    public static String JOPTION_RECHECKTIME = "Hãy set thời gian check";
    public static String JOPTION_GET_PATH_PROXY = "Hãy chọn đường dẫn đến file chứa proxy";
    public static String JOPTION_EMPTY_PROXY = "Hãy nhập proxy vào trước khi chạy auto";
    public static String JOPTION_NUMBER_PROXY = "Hãy nhập số proxy muốn chạy";
    public static String JOPTION_NUMBER_PORT = "Hãy nhập số port muốn bắt đầu";
    public static String JOPTION_GET_PATH_EXPORT_PROXY = "Hãy chọn đường muốn lưu file";
    public static int TIME_OUT_CHECK_CONNECT_ALL = 10000;
    public static int TIME_OUT_CHECK_CONNECT_LONG = 25000;
    public static String FILE_NAME_FAIL = "proxy-list-fails-";
    public static String BTN_LABEL_CHECK = "AutoCheckConnect";
    public static String JOPTION_UPDATE_WHITELIST = "Cập nhật whitelist thành công";
    public static String FILE_CONFIG_PATH = System.getProperty("user.dir") + "\\config\\config.properties";
}
