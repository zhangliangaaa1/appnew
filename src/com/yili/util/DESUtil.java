package com.yili.util;
import java.security.*;
import javax.crypto.*;

public class DESUtil {
    private static String strDefaultKey = "mykey";
    private Cipher encryptCipher = null;
    private Cipher decryptCipher = null;
    
    public static final String KEY = "&7U(*Il"; //&7U(*Il这个是密钥
    
    public static String byteArr2HexStr(byte[] arrB) throws Exception {
        int iLen = arrB.length;
        StringBuffer sb = new StringBuffer(iLen * 2);
        for (int i = 0; i < iLen; i++) {
            int intTmp = arrB[i];
            while (intTmp < 0) {
                intTmp = intTmp + 256;
            }
            if (intTmp < 16) {
                sb.append("0");
            }
            sb.append(Integer.toString(intTmp, 16));
        }
        return sb.toString();
    }

    public static byte[] hexStr2ByteArr(String strIn) throws Exception {
        byte[] arrB = strIn.getBytes();
        int iLen = arrB.length;

        byte[] arrOut = new byte[iLen / 2];
        for (int i = 0; i < iLen; i = i + 2) {
            String strTmp = new String(arrB, i, 2);
            arrOut[i / 2] = (byte) Integer.parseInt(strTmp, 16);
        }
        return arrOut;
    }

    public DESUtil() throws Exception {
        this(strDefaultKey);
    }

    public DESUtil(String strKey) throws Exception {
        if (strKey == null)
            return;
        java.security.Security.addProvider(new com.sun.crypto.provider.SunJCE());
        Key key = getKey(strKey.getBytes());
        encryptCipher = Cipher.getInstance("DES");
        encryptCipher.init(Cipher.ENCRYPT_MODE, key);

        decryptCipher = Cipher.getInstance("DES");
        decryptCipher.init(Cipher.DECRYPT_MODE, key);
    }

    public byte[] encrypt(byte[] arrB) throws Exception {
        return encryptCipher.doFinal(arrB);
    }

    public String encrypt(String strIn) throws Exception {
        return byteArr2HexStr(encrypt(strIn.getBytes()));
    }


    public byte[] decrypt(byte[] arrB) throws Exception {
        return decryptCipher.doFinal(arrB);
    }

    public String decrypt(String strIn) throws Exception {
        return new String(decrypt(hexStr2ByteArr(strIn)));
    }

    private Key getKey(byte[] arrBTmp) throws Exception {
        byte[] arrB = new byte[8];

        for (int i = 0; i < arrBTmp.length && i < arrB.length; i++) {
            arrB[i] = arrBTmp[i];
        }

        Key key = new javax.crypto.spec.SecretKeySpec(arrB, "DES");

        return key;
    }

   
    /** 加密
     * @param key 密钥
     * @param str 需要加密的字符串
     * @return
     */
    public static String encode(String key, String str) {
        DESUtil des = null;
        try {
            des = new DESUtil(key);
            return des.encrypt(str);
        } catch (Exception ex) {
        }
        return "";
    }

    public static String decode(String key, String str) {
        DESUtil des = null;
        try {
            des = new DESUtil(key);
            return des.decrypt(str);
        } catch (Exception ex) {
        }
        return "";
    }

    public static void main(String[] args) {
    	
//    	String token  =  CodeUtil.hexSHA1("yili8888!.003305041506481177828");
//    	System.out.println("加密前的字符：" + token);
    	
        try {
            String test = "00124424123456";
            if(args!=null&&args.length>0){
                test= args[0];
            }
            String key = "&7U(*Il"; //&7U(*Il这个是密钥
            if (args!=null&&args.length > 1) {
                key = args[1];
            }
            DESUtil des = new DESUtil(key);
            System.out.println("加密前的字符：" + test);
            System.out.println("加密后的字符：" + DESUtil.encode(key, test+key));
            System.out.println("解密后的字符：" + des.decrypt("de26aaf7f8f6bc6162da2aecf77d239724e6c381191f21b1"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}