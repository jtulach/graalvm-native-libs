package org.apidesign.demo;

public final class Print {
    private Print() {
    }

    public static void main(String... args) {
        var url = Print.class.getProtectionDomain().getCodeSource().getLocation();
        var name = System.getProperty("java.vm.name");
        System.out.println("Print by " + name + " loaded by: " + url);
    }
}
