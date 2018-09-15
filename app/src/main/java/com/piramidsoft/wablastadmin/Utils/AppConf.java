package com.piramidsoft.wablastadmin.Utils;

/**
 * Created by Ayo Maju on 09/07/2018.
 * Updated by Muhammad Iqbal on 09/07/2018.
 */

public class AppConf {

    public static final String SIGNALLING_URL = "http://118.98.64.44:9850";
    public static final String BASE_URL = "http://118.98.64.43/wablast";
    public static final String URL_SERV = BASE_URL + "/index.php/";

    public static final String URL_SENDBLAST = URL_SERV + "blast/sendblast";
    public static final String URL_PENGIRIM = URL_SERV + "blast/pengirim";
    public static final String URL_LOGS = URL_SERV + "blast/logs";
    public static final String URL_LOGS_NUMBER = URL_SERV + "blast/logs_number";
    public static final String URL_LOGS_NUMBER_DONE = URL_SERV + "blast/logs_number_done";
    public static final String URL_SET_PROSES_DONE = URL_SERV + "blast/set_done";
    public static final String URL_TERKIRIM = URL_SERV + "blast/terkirim";
    public static final String URL_LIST_SPY = URL_SERV + "Deep/list_spy";
    public static final String URL_DETAIL_SPY = URL_SERV + "Deep/detail_data";
}
