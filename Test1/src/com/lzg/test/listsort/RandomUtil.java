package com.lzg.test.listsort;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 
 * @author lzg
 * @date 2018年1月19日 下午9:23:00
 *
 */
public class RandomUtil {
	
	public static void main(String[] args) {
		System.out.println(System.currentTimeMillis());
		System.out.println(getTime());
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		System.out.println(sdf.format(new Date(getTime())));
		System.out.println(getPayDate());
		System.out.println(getPayMonth());
	}
	
	public static long getTime(){
		return (long)(Math.random() * 10000000000000L);
	}
	
	public static String getPayDate(){
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		return sdf.format(new Date(getTime()));
	}
	
	public static String getPayMonth(){
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMM");
		return sdf.format(new Date(getTime()));
	}

}
