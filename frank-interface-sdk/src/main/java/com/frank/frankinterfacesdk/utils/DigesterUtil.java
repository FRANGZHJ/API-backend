package com.frank.frankinterfacesdk.utils;

import cn.hutool.crypto.digest.DigestAlgorithm;
import cn.hutool.crypto.digest.Digester;

public class DigesterUtil {

    public static Digester getDigest(){
        Digester digester = new Digester(DigestAlgorithm.SHA512);
        return digester;
    }
}
