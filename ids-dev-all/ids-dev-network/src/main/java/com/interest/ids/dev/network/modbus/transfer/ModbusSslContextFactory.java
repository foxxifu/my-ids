package com.interest.ids.dev.network.modbus.transfer;

import java.io.IOException;
import java.io.InputStream;
import java.security.GeneralSecurityException;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.NoSuchAlgorithmException;

import javax.net.ssl.KeyManager;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;

import com.interest.ids.common.project.utils.ResourceReadUtil;
import com.interest.ids.common.project.utils.ShareConf;


public final class ModbusSslContextFactory {
    private static Logger log = LoggerFactory.getLogger(ModbusSslContextFactory.class);
    private static final SSLContext SERVER_CONTEXT;
    private static final String key_file = "adapter/modbus/server_keystore.jks";
    private static final String CERT_ALGORITHM_TYPE = "SunX509";
    private static final String KEYSTORE_TYPE = "JKS";
    private static final String KEYSTORE_PASS = "NetEco123";

    static{

        SSLContext sslContext = null;
        try{
            sslContext = SSLContext.getInstance("SSLv3");
        }
        catch(NoSuchAlgorithmException e1){
            log.error("have Exception:",e1);
        }

        try{
            if(null != getKeyManagers() && null != getTrustManagers()){
                sslContext.init(getKeyManagers(), getTrustManagers(), null);
            }
        }
        catch(KeyManagementException e){
            log.error("have Exception:",e);
        }
        catch(IOException e){
            log.error("have Exception:",e);
        }
        catch(GeneralSecurityException e){
            log.error("have Exception:",e);
        }
        sslContext.createSSLEngine().getSupportedCipherSuites();

        SERVER_CONTEXT = sslContext;

    }

    public static SSLContext getServerContext() {
        return SERVER_CONTEXT;
    }

    private ModbusSslContextFactory() {
    }

    private static InputStream getKeyFileIs() throws Exception {
        try{
        	Resource rs = ResourceReadUtil.getResourceFromClassPath(ShareConf.empfDataConf + key_file);
        	return rs.getInputStream();
        }
        catch(Exception e){
            log.error("have Exception:",e);
            throw e;
        }
    }

    private static KeyManager[] getKeyManagers() throws IOException,
        GeneralSecurityException {
        InputStream is = null;
        KeyStore ks = null;
        KeyManagerFactory kmFact = null;

        KeyManager[] kms = null;
        try{
            // 获得KeyManagerFactory对象.
            kmFact = KeyManagerFactory.getInstance(CERT_ALGORITHM_TYPE);

            is = getKeyFileIs();
            ks = KeyStore.getInstance(KEYSTORE_TYPE);
            ks.load(is, KEYSTORE_PASS.toCharArray());

            // 使用获得的KeyStore初始化KeyManagerFactory对象
            kmFact.init(ks, KEYSTORE_PASS.toCharArray());

            // 获得KeyManagers对象
            kms = kmFact.getKeyManagers();
        }
        catch(Exception e){
            log.error("have Exception:",e);
        }
        finally{
            if(is != null){
                is.close();
            }
        }
        return kms;
    }

    private static TrustManager[] getTrustManagers() throws IOException,
        GeneralSecurityException {
        InputStream is = null;
        TrustManagerFactory kmFact = null;
        KeyStore ks = null;
        TrustManager[] kms = null;

        // 获得KeyManagerFactory对象.
        try{
            kmFact = TrustManagerFactory.getInstance(CERT_ALGORITHM_TYPE);

            // 配置KeyManagerFactory对象使用的KeyStoree.我们通过一个文件加载
            // is = new FileInputStream(cert_path);
            is = getKeyFileIs();
            ks = KeyStore.getInstance(KEYSTORE_TYPE);
            ks.load(is, KEYSTORE_PASS.toCharArray());
            // 使用获得的KeyStore初始化KeyManagerFactory对象
            kmFact.init(ks);

            // 获得KeyManagers对象
            kms = kmFact.getTrustManagers();
        }
        catch(Exception e){

            log.error("have Exception:",e);
        }
        finally{
            if(is != null){
                is.close();
            }
        }
        return kms;
    }
}
