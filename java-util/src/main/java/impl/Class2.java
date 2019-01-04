package impl;

import java.util.HashMap;

public class Class2 {

    String string1;
    Class3 class3Object1;
    Class3 class3Object2;
    HashMap<String, String> hashMap;

    public String getString1() {
        return string1;
    }

    public Class3 getClass3Object1() {
        return class3Object1;
    }

    public Class3 getClass3Object2() {
        return class3Object2;
    }

    public HashMap<String, String> getHashMap() {
        return hashMap;
    }

    public void setString1(String string1) {
        this.string1 = string1;
    }

    public void setClass3Object1(Class3 class3Object1) {
        this.class3Object1 = class3Object1;
    }

    public void setclass3Object2(Class3 class3Object2) {
        this.class3Object2 = class3Object2;
    }

    public void setHashMap(HashMap<String, String> hashMap) {
        this.hashMap = hashMap;
    }

    public Class2() {}

    public Class2(String string1, Class3 class3Object1, Class3 class3Object2, HashMap<String, String> hashMap) {
        this.string1 = string1;
        this.class3Object1 = class3Object1;
        this.class3Object2 = class3Object2;
        this.hashMap = hashMap;
    }
}
