package com.nm.utils;

public class Singleton {
    private static Singleton ourInstance;

    public static void getOurInstance() {
        if (ourInstance == null) {
            ourInstance = new Singleton();
            try {
                Class.forName("com.mysql.jdbc.Driver");
            } catch (ClassNotFoundException ex) {

            }
        }
    }
}
