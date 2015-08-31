package com.simonbrobert.web2text.util;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Simon on 2015-08-24.
 */
public class Epoch {

    public static long getCurrentEpoch(){
        Date now = new Date();
        long epoch = now.getTime();
        return epoch;
    }

}
