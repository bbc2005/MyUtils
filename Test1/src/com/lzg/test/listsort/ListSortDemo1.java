package com.lzg.test.listsort;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * 通过创建Comparator匿名内部类并实现其compare方法
 * 可以分别按各个属性升序或降序排序
 * 通过list.sort(c)排序
 * @author lzg
 * @date 2018年1月19日 下午9:21:33
 *
 */
public class ListSortDemo1 {
	
	public static void main(String[] args) {
		List<Pojo> list = new ArrayList<Pojo>();
		
		for (int i = 0; i < 30; i++) {
			Pojo pojo = new Pojo();
			pojo.setName("name" + i);
			pojo.setPayDate(RandomUtil.getPayDate());
			pojo.setPayMonth(RandomUtil.getPayMonth());
			list.add(pojo);
		}
		
		Pojo p1 = new Pojo();
		p1.setName("test");
		p1.setPayDate(null);
		p1.setPayMonth("");
		list.add(p1);
		
		Pojo p2 = new Pojo();
		p2.setName("test");
		p2.setPayDate("");
		p2.setPayMonth(null);
		list.add(p2);
		
		Pojo p3 = new Pojo();
		p3.setName("test");
		p3.setPayDate("的所发生的");
		p3.setPayMonth("6641sd");
		list.add(p3);
		
		System.out.println("排序前：");
		System.out.println("-------------------------------------------");
		for (int i = 0; i < list.size(); i++) {
			Pojo pojo = list.get(i);
			System.out.println("name=" + pojo.getName() + ",payDate=" + pojo.getPayDate() + ",payMonth=" + pojo.getPayMonth());
		}
		System.out.println("-------------------------------------------");
		
		sortByPayDate(list,false);
		
		System.out.println("按payDate降序排序后：");
		System.out.println("-------------------------------------------");
		for (int i = 0; i < list.size(); i++) {
			Pojo pojo = list.get(i);
			System.out.println("name=" + pojo.getName() + ",payDate=" + pojo.getPayDate() + ",payMonth=" + pojo.getPayMonth());
		}
		System.out.println("-------------------------------------------");
		
		sortByPayMonth(list,false);
		
		System.out.println("按payMonth降序排序后：");
		System.out.println("*******************************************");
		for (int i = 0; i < list.size(); i++) {
			Pojo pojo = list.get(i);
			System.out.println("name=" + pojo.getName() + ",payDate=" + pojo.getPayDate() + ",payMonth=" + pojo.getPayMonth());
		}
		System.out.println("*******************************************");
	}
	
	public static void sortByPayDate(List<Pojo> list,final boolean isAsc){
		
		Comparator<? super Pojo> c = new Comparator<Pojo>() {

			@Override
			public int compare(Pojo o1, Pojo o2) {
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
//				System.out.println("payDateMs1=" + payDateMs1 + ",payDateMs2=" + payDateMs2 +",diff=" + diff);	
				
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
		};
		list.sort(c);
	}
	
	public static void sortByPayMonth(List<Pojo> list,final boolean isAsc){

		Comparator<? super Pojo> c = new Comparator<Pojo>() {

			@Override
			public int compare(Pojo o1, Pojo o2) {
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
//				System.out.println("payMonthMs1=" + payMonthMs1 + ",payMonthMs2=" + payMonthMs2 +",diff=" + diff);	
				
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
		};
		list.sort(c);
	}

}
