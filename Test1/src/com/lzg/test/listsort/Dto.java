package com.lzg.test.listsort;

import java.text.SimpleDateFormat;
import java.util.Comparator;

/**
 * 
 * @author lzg
 * @date 2018年1月19日 下午10:38:37
 *
 */
public class Dto {
	
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
	
	public final class CompareByPayDate implements Comparator<Dto> {
		boolean isAsc;//是否升序
		
		public CompareByPayDate(boolean isAsc) {
			super();
			this.isAsc = isAsc;
		}

		@Override
		public int compare(Dto o1, Dto o2) {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			long payDateMs1 = 0;
			long payDateMs2 = 0;
			try {
				payDateMs1 = sdf.parse(o1.getPayDate()).getTime();
			} catch (Exception e) {
				e.printStackTrace();
			}
			try {
				payDateMs2 = sdf.parse(o2.getPayDate()).getTime();
			} catch (Exception e) {
				e.printStackTrace();
			}
			long diff = (payDateMs1 - payDateMs2)/(24*60*60*1000) ;
//			System.out.println("payDateMs1=" + payDateMs1 + ",payDateMs2=" + payDateMs2 +",diff=" + diff);	
			
			if(isAsc){//升序排序
				if(diff > 0 ){
					return 1;
				}else if(diff < 0 ){
					return -1;
				}else{
					return 0;
				}
			}else{//降序排序
				if(diff > 0 ){
					return -1;
				}else if(diff < 0 ){
					return 1;
				}else{
					return 0;
				}
			}
			
		}
		
	}

    public final class CompareByPayMonth implements Comparator<Dto> {
    	boolean isAsc;//是否升序

        public CompareByPayMonth(boolean isAsc) {
        	this.isAsc = isAsc;
        }

        @Override
        public int compare(Dto o1, Dto o2) {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMM");
			long payMonthMs1 = 0;
			long payMonthMs2 = 0;
			try {
				payMonthMs1 = sdf.parse(o1.getPayMonth()).getTime();
			} catch (Exception e) {
				e.printStackTrace();
			}
			try {
				payMonthMs2 = sdf.parse(o2.getPayMonth()).getTime();
			} catch (Exception e) {
				e.printStackTrace();
			}
			long diff = (payMonthMs1 - payMonthMs2)/(24*60*60*1000) ;
//			System.out.println("payMonthMs1=" + payMonthMs1 + ",payMonthMs2=" + payMonthMs2 +",diff=" + diff);	
			
			if(isAsc){//升序排序
				if(diff > 0 ){
					return 1;
				}else if(diff < 0 ){
					return -1;
				}else{
					return 0;
				}
			}else{//降序排序
				if(diff > 0 ){
					return -1;
				}else if(diff < 0 ){
					return 1;
				}else{
					return 0;
				}
			}
        }
    }

}
