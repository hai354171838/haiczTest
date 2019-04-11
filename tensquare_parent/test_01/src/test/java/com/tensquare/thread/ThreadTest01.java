package com.tensquare.thread;


import com.tensquare.Student;
import org.junit.Test;

import java.util.*;

/**
 * @author:Haicz
 * @date:2019/03/17
 */
public class ThreadTest01 {
    public static void main(String[] args) {
//        BlockingQueue<Integer> queue=new ArrayBlockingQueue<Integer>(100);
//        Producer producer = new Producer(queue);
//        Consumer c1 = new Consumer(queue);
//        Consumer c2 = new Consumer(queue);
//
//        new Thread(producer).start();
//
//        new Thread(c1).start();
//        new Thread(c2).start();

        try {
            Student student = new Student("小王",13);
            Student stu = MyUtil.clone(student);
            System.out.println(student==stu);
            System.out.println("name="+student.getName()+"age="+student.getAge());
            System.out.println("克隆name="+stu.getName()+"age="+stu.getAge());
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
    @Test
    public void test() {
        Vector<Object> objects = new Vector<>();
        Hashtable<Object, Object> objectObjectHashtable = new Hashtable<>();
        Map<Integer, Student> map = new HashMap();


        map.put(6,new Student("大王",19) );
        map.put(1,new Student("小王",18));
        map.put(13, new Student("小王",12));
        map.put(233, new Student("阿花",22));
        System.out.println(map);
        Map<Integer, Student> sortHashMap = sortHashMap(map);
        System.out.println(sortHashMap);

    }
    public Map<Integer, Student>  sortHashMap(Map<Integer, Student> map) {
        Set<Map.Entry<Integer, Student>> set = map.entrySet();

        ArrayList<Map.Entry<Integer, Student>> al = new ArrayList(set);

        Collections.sort(al, new Comparator<Map.Entry<Integer, Student>>() {
            @Override
            public int compare(Map.Entry<Integer, Student> o1, Map.Entry<Integer, Student> o2) {
                return o2.getValue().getAge()-o1.getValue().getAge();
            }
        });

        LinkedHashMap<Integer, Student> lhm = new LinkedHashMap<>();

        for (Map.Entry<Integer, Student> entry : al) {
            lhm.put(entry.getKey(),entry.getValue() );
        }

        return lhm;
    }
}
