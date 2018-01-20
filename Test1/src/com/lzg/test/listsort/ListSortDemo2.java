package com.lzg.test.listsort;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 实体实现Comparable接口并重写compareTo方法，
 * 只能对一个属性升序或降序排序
 * 通过Collections.sort(list)排序
 * 
 * @author lzg
 * @date 2018年1月19日 下午9:21:33
 *
 */
public class ListSortDemo2 {
	
	public static void main(String[] args) {
		List<Entity> list = new ArrayList<Entity>();
		
		for (int i = 0; i < 30; i++) {
			Entity pojo = new Entity();
			pojo.setName("name" + i);
			pojo.setPayDate(RandomUtil.getPayDate());
			pojo.setPayMonth(RandomUtil.getPayMonth());
			list.add(pojo);
		}
		
		Entity p1 = new Entity();
		p1.setName("test");
		p1.setPayDate(null);
		p1.setPayMonth("");
		list.add(p1);
		
		Entity p2 = new Entity();
		p2.setName("test");
		p2.setPayDate("");
		p2.setPayMonth(null);
		list.add(p2);
		
		Entity p3 = new Entity();
		p3.setName("test");
		p3.setPayDate("的所发生的");
		p3.setPayMonth("6641sd");
		list.add(p3);
		
		System.out.println("排序前：");
		System.out.println("-------------------------------------------");
		for (int i = 0; i < list.size(); i++) {
			Entity pojo = list.get(i);
			System.out.println("name=" + pojo.getName() + ",payDate=" + pojo.getPayDate() + ",payMonth=" + pojo.getPayMonth());
		}
		System.out.println("-------------------------------------------");
		
//		Collections.sort(list);//按实体类定义的排方式排序--》降序
		Collections.sort(list,Collections.reverseOrder());//反转实体类定义的排方式排序--》升序
		
		System.out.println("按payDate降序排序后：");
		System.out.println("-------------------------------------------");
		for (int i = 0; i < list.size(); i++) {
			Entity pojo = list.get(i);
			System.out.println("name=" + pojo.getName() + ",payDate=" + pojo.getPayDate() + ",payMonth=" + pojo.getPayMonth());
		}
		System.out.println("-------------------------------------------");
		
	}
	

}
