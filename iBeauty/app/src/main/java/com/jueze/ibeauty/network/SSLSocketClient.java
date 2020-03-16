package com.jueze.ibeauty.network;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.SSLContext;
import java.security.NoSuchAlgorithmException;
import javax.net.ssl.TrustManager;
import java.security.SecureRandom;
import javax.net.ssl.X509TrustManager;
import java.security.cert.X509Certificate;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLSession;

public class SSLSocketClient {
    //获取sslsocketfactory
	public static SSLSocketFactory getSSLSocketFactory(){
		try {
			SSLContext sslContext = SSLContext.getInstance("SSL");
			sslContext.init(null, getTrustManager(), new SecureRandom());
			return sslContext.getSocketFactory();
		} catch (Exception e) {}
		return null;
	}
	
	//获取trustmanager
	public static TrustManager[] getTrustManager(){
		TrustManager[] trustAllCerts = new TrustManager[]{
			new X509TrustManager(){

				@Override
				public void checkClientTrusted(X509Certificate[] p1, String p2) {
				}

				@Override
				public void checkServerTrusted(X509Certificate[] p1, String p2){
				}

				@Override
				public X509Certificate[] getAcceptedIssuers() {
					return new X509Certificate[]{};
				}
			}
		};
		return trustAllCerts;
	}
	
	//获取hostnameverifier
	public static HostnameVerifier getHostnameVerifier(){
		HostnameVerifier hostnameVerifier = new HostnameVerifier(){

			@Override
			public boolean verify(String p1, SSLSession p2) {
				return true;
			}
		};
		return hostnameVerifier;
	}
}
