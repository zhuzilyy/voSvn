package com.zl.vo_.main.main_utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by Administrator on 2017/12/16.
 */

public class TimeUtil {

      /**
           * 返回时间戳
          *
           * @param time
           * @return
          */
    public static long dataOne(String time) {
                 SimpleDateFormat sdr = new SimpleDateFormat("yyyy-MM-dd HH:mm",
                                 Locale.CHINA);
                 Date date;
                long l = 0;
                 try {
                        date = sdr.parse(time);
                        l = date.getTime();
                    } catch (ParseException e) {
                        e.printStackTrace();
                     }
        return l;
             }





}
