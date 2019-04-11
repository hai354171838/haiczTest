package com.tensquare.thread;

import com.tensquare.Student;

import java.io.*;

/**
 * @author:Haicz
 * @date:2019/03/17
 */
public class MyUtil {
    private MyUtil() {
        throw new AssertionError();

    }
    public static <T extends Serializable>T clone(Student obj) throws  Exception{

        ByteArrayOutputStream bout = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(bout);

        oos.writeObject(obj);

        ByteArrayInputStream bis = new ByteArrayInputStream(bout.toByteArray());

        ObjectInputStream ois = new ObjectInputStream(bis);
        return (T) ois.readObject();

    }

}
