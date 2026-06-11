package org.apidesign.demo;

import java.io.File;
import java.net.URL;
import java.net.URLClassLoader;

public final class App {
    private App() {
    }

    public static void main(String[] args) throws Exception {
        if (args.length == 2) {
            var cp = new File(args[0]).getAbsoluteFile();
            if (!cp.isDirectory()) {
                throw new IllegalArgumentException("This should be a directory: " + cp);
            }
            var l = new URLClassLoader(new URL[] { cp.toURI().toURL() });
            var c = l.loadClass(args[1]);
            System.out.println("found class: " + c);
            c.getMethod("main", String[].class).invoke(null, (Object) args);
            System.out.println("invoked it's main!");
        }
        var name = System.getProperty("java.vm.name");
        var url = App.class.getProtectionDomain().getCodeSource().getLocation();
        System.out.println("Hello by " + name + " loaded by: " + url);
    }
}
