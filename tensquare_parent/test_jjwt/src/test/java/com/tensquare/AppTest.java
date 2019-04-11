package com.tensquare;

import org.junit.Test;

/**
 * Unit test for simple App.
 */
public class AppTest 
{
    /**
     * Rigorous Test :-)
     */
    @Test
    public void shouldAnswerWithTrue()
    {
        //System.out.println(reversal(1023));
        System.out.println(test02());

    }


    public int reversal(int x) {
        if (x>-10&&x<10){
            return x;
        }else if (x>=10){
            String str=""+x;
            char[] arr = str.toString().toCharArray();
            int length = str.length();
            if (length%2==0){
                for (int i = 0; i < length / 2; i++) {
                    char temp=arr[i];
                    arr[i]=arr[length-1-i];
                    arr[length-1-i]=temp;
                }
                String str1="";
                for (char c : arr) {
                    str1+=c;
                }
                return Integer.parseInt(str1);
            }

        }else {

        }



       return 0;

    }

    public int test02() {
            int i=0;

        try {
            ++i;
        } finally {
            ++i;
        }
        return ++i;
    }
}
