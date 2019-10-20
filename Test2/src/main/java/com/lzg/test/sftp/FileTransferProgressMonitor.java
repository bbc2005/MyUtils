package com.lzg.test.sftp;

import java.text.DecimalFormat;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.jcraft.jsch.SftpProgressMonitor;

/**
 * 传输进度监控
 * @author lzg
 * @date 2018年4月15日 下午5:02:37
 *
 */
public class FileTransferProgressMonitor implements SftpProgressMonitor {

	private final Logger logger = LogManager.getLogger();

	private long transfered; // 记录已传输的数据总大小
	private long fileSize; // 记录文件总大小
	private int minInterval = 100; // 打印日志时间间隔
	private long startTime; // 开始时间
	private DecimalFormat df = new DecimalFormat("#.##");
	private long preTime;

	/**
	 * 开始传输 当文件开始传输时，调用init方法
	 * 
	 * @param op
	 * @param src
	 * @param dest
	 * @param max
	 */
	@Override
	public void init(int op, String src, String dest, long max) {
		this.fileSize = max;
		logger.info("Transferring start.");
		logger.info(
				"start transferring {} to remote server {} ,file size: {} Byte",
				src, dest, max);
		startTime = System.currentTimeMillis();
	}

	/**
	 * 正在传输 当每次传输了一个数据块后，调用count方法，count方法的参数为这一次传输的数据块大小
	 * 
	 * @param count
	 * @return
	 */
	@Override
	public boolean count(long count) {
		if (fileSize != 0 && transfered == 0) {
			logger.info("Transferring progress : transfered {} Byte,  {}%", transfered, df.format(0));
			preTime = System.currentTimeMillis();
		}
		transfered += count;
		if (fileSize != 0) {
			long interval = System.currentTimeMillis() - preTime;
			if (transfered == fileSize || interval > minInterval) {
				preTime = System.currentTimeMillis();
				double d = ((double) transfered * 100) / (double) fileSize;
				logger.info("Transferring progress : transfered {} Byte,  {}%", transfered, df.format(d));
			}
		} else {
			logger.info("Transferring progress : transfered {} Byte ", transfered);
		}
		return true;
	}

	/**
	 * 当传输结束时，调用end方法
	 */
	@Override
	public void end() {
		logger.info("Transferring end. used time: {}ms",
				String.valueOf(System.currentTimeMillis() - startTime));
	}
}
