package com.mdata.thirdparty.dianli.lora.client.codec;


import com.google.common.io.BaseEncoding;

import java.util.Base64;

/**
 * Created by administrator on 17/7/23.
 */
public class Test {
   String a= "receive from lora -->qqo1IBG18LYFAAAADgAzAAcXFQoqcROx\n"+
            "receive from lora -->qqo1ICG18A4FAAAADiAzAAcXFQoJcfAOGgAAAABqMgAHFxUKK3GgFA\n"+
            "receive from lora -->qqo1ICG18IEaAAAAAHAzAAcXFQo7cfDCBQAAAA5AMwAHFxUKI3EPZA\n"+
            "receive from lora -->qqo1ICG18LYaAAAAAGkzAAcXFQorcfAOEQAAAAAAMgAHFxUKLHFqrQ\n"+
            "receive from lora -->qqo1ICG18MIRAAAAAAQyAAcXFQsKcfDCGgAAAABvMwAHFxUKJHH2Zg\n"+
            "receive from lora -->qqo1ICG18LIRAAAAAAAzAAcXFQoccfAcBQAAAA4gMQAHFxULFnFcjw";

    public static void main(String[] args) throws  Exception {
       byte[] bb= Base64.getDecoder().decode("qqo1ICG1MaIFAAAADUBVAAPoAAAAUVGABQAAAA5AVQAD6AAAAFFjsQ");
        System.out.println(new String(bb,"utf-8"));
       byte[] b= BaseEncoding.base64().decode("qqo1ICG1MaIFAAAADUBVAAPoAAAAUVGABQAAAA5AVQAD6AAAAFFjsQ");
        StringBuilder sb=new StringBuilder();
        for (int i = 0; i < b.length; i++) {
            String hex = Integer.toHexString(b[i] & 0xFF);
            if (hex.length() == 1) {
                hex = '0' + hex;
            }
            sb.append(hex.toUpperCase()) ;
        }
        System.out.println(sb.toString());
       // byte [] b= "qqo1IBG18LYFAAAADgAzAAcXFQoqcROx".getBytes("UTF-8");
    }
}
