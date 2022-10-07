package com.edatasolutions.utils;

import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidParameterSpecException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;

public class MD5 {

    public static String encrypt(String token) {

        int RESULT_LENGTH = 16;

        MessageDigest cript;

        try {
            cript = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }

        cript.reset();

        try {
            cript.update(token.getBytes("utf8"));
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }

        return new BigInteger(1, cript.digest()).toString(RESULT_LENGTH);

    }


}
