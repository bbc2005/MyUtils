package com.lzg.test.listsort;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * 实体有多个实现Comparator接口的内部类，
 * 可以分别按各个属性升序或降序排序
 * 通过Collections.sort(list, c1)排序
 * @author lzg
 * @date 2018年1月19日 下午9:21:33
 *
 */
public class ListSortDemo3 {
	
	public static void main(String[] args) {
		List<Dto> list = new ArrayList<Dto>();
		
		for (int i = 0; i < 30; i++) {
			Dto pojo = new Dto();
			pojo.setName("name" + i);
			pojo.setPayDate(RandomUtil.getPayDate());
			pojo.setPayMonth(RandomUtil.getPayMonth());
			list.add(pojo);
		}
		
		Dto p1 = new Dto();
		p1.setName("test");
		p1.setPayDate(null);
		p1.setPayMonth("");
		list.add(p1);
		
		Dto p2 = new Dto();
		p2.setName("test");
		p2.setPayDate("");
		p2.setPayMonth(null);
		list.add(p2);
		
		Dto p3 = new Dto();
		p3.setName("test");
		p3.setPayDate("的所发生的");
		p3.setPayMonth("6641sd");
		list.add(p3);
		
		System.out.println("排序前：");
		System.out.println("-------------------------------------------");
		for (int i = 0; i < list.size(); i++) {
			Dto pojo = list.get(i);
			System.out.println("name=" + pojo.getName() + ",payDate=" + pojo.getPayDate() + ",payMonth=" + pojo.getPayMonth());
		}
		System.out.println("-------------------------------------------");
		
		Comparator<Dto> c1 = new Dto().new CompareByPayDate(false);
		Collections.sort(list, c1);
		
		System.out.println("按payDate降序排序后：");
		System.out.println("-------------------------------------------");
		for (int i = 0; i < list.size(); i++) {
			Dto pojo = list.get(i);
			System.out.println("name=" + pojo.getName() + ",payDate=" + pojo.getPayDate() + ",payMonth=" + pojo.getPayMonth());
		}
		System.out.println("-------------------------------------------");
		
		Comparator<Dto> c2 = new Dto().new CompareByPayMonth(false);
		Collections.sort(list, c2);
		
		System.out.println("按payMonth降序排序后：");
		System.out.println("*******************************************");
		for (int i = 0; i < list.size(); i++) {
			Dto pojo = list.get(i);
			System.out.println("name=" + pojo.getName() + ",payDate=" + pojo.getPayDate() + ",payMonth=" + pojo.getPayMonth());
		}
		System.out.println("*******************************************");
		
	}
	

}
