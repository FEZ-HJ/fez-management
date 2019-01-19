package com.fez.utils;

import java.util.Random;

/**
 * Created by H.J
 * 2019/1/19
 */
public class KeyUtil {

    public static synchronized String getUniqueKey(){
        Random random = new Random();
        Integer integer = random.nextInt(900000)+100000;

        return System.currentTimeMillis()+String.valueOf(integer);
    }
}
