package impl;

import com.cedarsoftware.util.DeepEquals;

import java.util.HashMap;

/**
 */
public class Application {
    public static void main(String[] args) {

        //obj1 creation
        Class3 class3Obj1 = new Class3("Obj1");
        Class3 class3Obj2 = new Class3("Obj2");

        Class2 class2 = new Class2();
        HashMap<String, String> hashMap1 = new HashMap<String, String>();
        hashMap1.put("1", "value1");
        hashMap1.put("2", "value2");
        class2.setString1("string1");
        class2.setClass3Object1(class3Obj1);
        class2.setclass3Object2(class3Obj2);
        class2.setHashMap(hashMap1);

        Class1 obj1 = new Class1();
        obj1.setString("class1obj1");
        obj1.setClass2Object(class2);

        //obj2 creation
        Class3 class3Obj3 = new Class3("Obj1");
        Class3 class3Obj4 = new Class3("Obj2");

        Class2 class22 = new Class2();
        HashMap<String, String> hashMap2 = new HashMap<String, String>();
        hashMap2.put("1", "value1");
        hashMap2.put("2", "value2");
        class22.setString1("string1");
        class22.setClass3Object1(class3Obj3);
        class22.setclass3Object2(class3Obj4);
        class22.setHashMap(hashMap2);

        Class1 obj2 = new Class1();
        obj2.setString("class1obj11");
        obj2.setClass2Object(class22);


        System.out.println(DeepEquals.deepEquals(obj1, obj2));

//        h(obj.animal,true);





    }
}
