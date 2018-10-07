package com.example.bang.toeichelper.secret;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Formatter;

/**
 * Created by BANG on 2015-05-19.
 */
public class SHA_1 {

    public SHA_1(){

    }

    public String encryptPassWord(String strPW){

        String strSHA1 = "";

        try {
            MessageDigest crypt = MessageDigest.getInstance("SHA-1");
            crypt.reset();
            crypt.update(strPW.getBytes("UTF-8"));
            strSHA1 = byteToHex(crypt.digest());

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        return strSHA1;
    }

    public String byteToHex(final byte[] hash)
    {
        Formatter formatter = new Formatter();

        for (byte b : hash)
        {
            formatter.format("%02x", b);
        }

        String result = formatter.toString();
        formatter.close();

        return result;
    }
}
