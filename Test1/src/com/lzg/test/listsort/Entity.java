package com.lzg.test.listsort;

import java.text.SimpleDateFormat;

/**
 * 
 * @author lzg
 * @date 2018年1月19日 下午10:38:37
 *
 */
public class Entity implements Comparable<Entity>{
	
	private String name;
	private String payDate;
	private String payMonth;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPayDate() {
		return payDate;
	}
	public void setPayDate(String payDate) {
		this.payDate = payDate;
	}
	public String getPayMonth() {
		return payMonth;
	}
	public void setPayMonth(String payMonth) {
		this.payMonth = payMonth;
	}
	@Override
	public int compareTo(Entity o) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		long payDateMs1 = 0;
		long payDateMs2 = 0;
		try {
			payDateMs1 = sdf.parse(this.getPayDate()).getTime();
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			payDateMs2 = sdf.parse(o.getPayDate()).getTime();
		} catch (Exception e) {
			e.printStackTrace();
		}
		long diff = (payDateMs1 - payDateMs2)/(24*60*60*1000) ;
//		System.out.println("payDateMs1=" + payDateMs1 + ",payDateMs2=" + payDateMs2 +",diff=" + diff);	
		
		//升序排序
//		if(diff > 0 ){
//			return 1;
//		}else if(diff < 0 ){
//			return -1;
//		}else{
//			return 0;
//		}
		
		//降序排序
		if(diff > 0 ){
			return -1;
		}else if(diff < 0 ){
			return 1;
		}else{
			return 0;
		}
	}

}
