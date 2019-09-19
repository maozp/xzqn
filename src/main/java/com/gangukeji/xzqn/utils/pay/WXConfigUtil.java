package com.gangukeji.xzqn.utils.pay;

import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.InputStream;
import com.github.wxpay.sdk.WXPayConfig;
import java.io.File;


public class WXConfigUtil implements WXPayConfig {
    private byte[] certData;
    public static final String APP_ID = "wx447195be5c084757"; //app Id
    public static final String KEY = "wanbo123456789987654321123456789";//秘钥
    public static final String MCH_ID = "1505384961"; //商户号

    public WXConfigUtil() throws Exception {
//        String certPath = "证书地址";//从微信商户平台下载的安全证书存放的路径
//        File file = new File(certPath);
//        InputStream certStream = new FileInputStream(file);
//        this.certData = new byte[(int) file.length()];
//        certStream.read(this.certData);
//        certStream.close();
    }

    @Override
    public String getAppID() {
        return APP_ID;
    }

    //parnerid，商户号
    @Override
    public String getMchID() {
        return MCH_ID;
    }

    @Override
    public String getKey() {
        return KEY;
    }

    @Override
    public InputStream getCertStream() {
        ByteArrayInputStream certBis = new ByteArrayInputStream(this.certData);
        return certBis;
    }

    @Override
    public int getHttpConnectTimeoutMs() {
        return 8000;
    }

    @Override
    public int getHttpReadTimeoutMs() {
        return 10000;
    }
}
