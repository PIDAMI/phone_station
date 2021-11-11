package com.company;

import java.util.concurrent.Callable;

public class Main {

    public static String f(Callable<String> s) throws Exception {
        return s.call();
    }


    public static void main(String[] args) throws Exception {


    }
}
