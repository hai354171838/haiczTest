package com.tensquare;

import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;

/**
 * @author:Haicz
 * @date:2019/03/13
 */
public class AppTest {
    public static void main(String args[]) throws IOException {
        Thread thread = new Thread() {
            @Override
            public void run() {
                for (int i = 0; i < 15; i++) {
                    System.out.println("小明:" + i);
                }
            }
        };
        thread.start();

        new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < 15; i++) {
                    System.out.println("小红:" + i);
                }
            }
        }).start();

        for (int i = 0; i < 15; i++) {
            System.out.println("小强:"+i);
        }
    }
    @Test
    public void test() {
        String str1="abc";
        String str="abc";
        String str2=new String("abc");
        System.out.println(str1==str2);//fl
        System.out.println(str.equals(str2));//ture
        System.out.println(str==str1);//

    }

    @Test
    public void test02() throws Exception {

        //test(2222);
        //System.out.println(judgePalindrome(2222));
        Student student = new Student("小米",11);
        Student stu = (Student) student.clone();
        System.out.println(student);
        System.out.println(stu);



    }
    public void test(int a ) {
        String str= new String().valueOf(a);
        for(int i=0;i<str.length();i++){
            if(str.charAt(i)!=str.charAt(str.length()-1-i)){
                System.out.println(str+"不是回文数");
                return;
            }
        }
        System.out.println(str+"是回文数");
    }

    public static boolean judgePalindrome(int n){
        StringBuilder builder=new StringBuilder(String.valueOf(n));
        return builder.toString().equals(builder.reverse().toString());
    }

    @Test
    public void test03() {
       ArrayList<Object> al = new ArrayList();
;    }

}
