package com.tensquare;

/**
 * @author:Haicz
 * @date:2019/03/13
 */
 class A {
    static {
        System.out.println("A1");
    }
    public A(){
        System.out.println("A2");
    }
}

class B extends A{
     static {
         System.out.println("B1");
     }
     public B(){
         System.out.println("B2");
     }

}

class Hello{
    public static void main(String[] args) {
        A ab= new B();
        System.out.println("````");
        ab=new B();
        System.out.println("`````");
        B b = new B();
    }
}
