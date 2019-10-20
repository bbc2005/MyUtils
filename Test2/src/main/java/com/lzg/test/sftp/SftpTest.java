package com.lzg.test.sftp;
/**
 * 
 * @author lzg
 * @date 2018年4月15日 下午5:20:46
 *
 */
public class SftpTest {

	public static void main(String[] args) {
		SftpClient client = SftpClient.connect();
		String src = "D:/剑指offer_扫描版_6.22M_高清重制.pdf";
		String dst = "/shared/";
		client.put(src , dst );
		client.disconnect();
	}

}
