package com.omarahmed42.ecommerce.util;

import java.math.BigInteger;
import java.util.Arrays;

public class BigIntegerHandler {
    private BigIntegerHandler(){}

    public static byte[] toByteArray(BigInteger bigInteger){
        byte[] array = bigInteger.toByteArray();
        if (array[0] == 0) {
            array = Arrays.copyOfRange(array, 1, array.length);
        }
        return array;
    }}
