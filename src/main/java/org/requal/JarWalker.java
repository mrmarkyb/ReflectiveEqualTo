package org.requal;

import java.util.List;
import java.util.ArrayList;
import java.util.jar.JarInputStream;
import java.util.jar.JarEntry;
import java.io.InputStream;
import java.io.IOException;

/**
 * Created by IntelliJ IDEA.
 * User: mburnett
 * Date: Apr 30, 2010
 * Time: 7:11:13 PM
 * To change this template use File | Settings | File Templates.
 */
public class JarWalker {
    private String jarName;

    public JarWalker(String jarName) {
        this.jarName = jarName;
    }

    public List<String> listClassNames() {
        return getClasseNamesInPackage(jarName);
    }

    public List getClasseNamesInPackage
            (String jarName) {

        InputStream jarStream = null;
        try {
            jarStream = ClassLoader.getSystemResourceAsStream(jarName);
            return getClassNamesFromInputStream(jarStream);
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }
        finally {
            if(jarStream != null) {
                try {
                    jarStream.close();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }

        }
    }

    private List getClassNamesFromInputStream(InputStream jarStream) throws IOException {
        List classNames = new ArrayList();
        JarInputStream jarFile = new JarInputStream(jarStream);
        JarEntry jarEntry;
        while (0 < jarFile.available()) {
            jarEntry = jarFile.getNextJarEntry();
            if (isClassEntry(jarEntry)) {
                classNames.add(classNameFor(jarEntry));
            }
        }
        return classNames;
    }

    private String classNameFor(JarEntry jarEntry) {
        return jarEntry.getName().replaceAll("\\.class", "").replaceAll("/", "\\.");
    }

    private boolean isClassEntry(JarEntry jarEntry) {
        return jarEntry != null && jarEntry.getName().endsWith(".class");
    }
}
