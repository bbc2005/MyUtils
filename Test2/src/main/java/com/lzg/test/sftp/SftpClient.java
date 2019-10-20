package com.lzg.test.sftp;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.SftpException;

/**
 * 
 * @author lzg
 * @date 2018年4月15日 下午5:06:55
 *
 */
public class SftpClient {

	private Logger logger = LogManager.getLogger(SftpClient.class);

	private Session session = null;
	private ChannelSftp channel = null;

	private SftpClient() {
	}

	public static SftpClient connect() {
		return new SftpClient().init();
	}

	public SftpClient init() {
		try {
			// String host = PropertiesUtil.getString("sftp.host");
			// int port = PropertiesUtil.getInt("sftp.port");
			// String userName = PropertiesUtil.getString("sftp.user.name");
			// String password = PropertiesUtil.getString("sftp.user.password");
			// Integer timeout = PropertiesUtil.getInt("sftp.timeout");
			// Integer aliveMax = PropertiesUtil.getInt("sftp.aliveMax");
			String host = "192.168.216.129";
			int port = 22;
			String userName = "sftp";
			String password = "admin";
			Integer timeout = 30000;
			Integer aliveMax = 10;
			JSch jsch = new JSch(); // 创建JSch对象
			session = jsch.getSession(userName, host, port); // 根据用户名，主机ip，端口获取一个Session对象
			if (password != null) {
				session.setPassword(password); // 设置密码
			}
			session.setConfig("StrictHostKeyChecking", "no"); // 为Session对象设置properties
			if (timeout != null)
				session.setTimeout(timeout); // 设置timeout时间
			if (aliveMax != null)
				session.setServerAliveCountMax(aliveMax);
			session.connect(); // 通过Session建立链接
			channel = (ChannelSftp) session.openChannel("sftp"); // 打开SFTP通道
			channel.connect(); // 建立SFTP通道的连接
			logger.info("SSH Channel connected.");
		} catch (JSchException e) {
			e.printStackTrace();
		}
		return this;
	}

	public void disconnect() {
		if (channel != null) {
			channel.disconnect();
		}
		if (session != null) {
			session.disconnect();
			logger.info("SSH Channel disconnected.");
		}
	}

	/** 发送文件 */
	public void put(String src, String dst) {
		try {
			channel.put(src, dst, new FileTransferProgressMonitor());
		} catch (SftpException e) {
			e.printStackTrace();
		}
	}

	/** 获取文件 */
	public void get(String src, String dst) {
		try {
			channel.get(src, dst, new FileTransferProgressMonitor());
		} catch (SftpException e) {
			e.printStackTrace();
		}
	}
}