package impl;

public class Class1 {

    String string;
    Class2 class2Object;

    public Class1() {}

    public Class1(String string, Class2 class2Object) {
        this.string = string;
        this.class2Object = class2Object;
    }

    public void setString(String string) {
        this.string = string;
    }

    public void setClass2Object(Class2 class2Object) {
        this.class2Object = class2Object;
    }

    public String getString() {
        return string;
    }

    public Class2 getClass2Object() {
        return class2Object;
    }
}
