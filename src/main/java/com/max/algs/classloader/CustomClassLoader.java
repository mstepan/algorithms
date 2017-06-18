package com.max.algs.classloader;

import org.apache.log4j.Logger;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class CustomClassLoader extends ClassLoader {

    private static final Logger LOG = Logger.getLogger(CustomClassLoader.class);
    private final Path BASE_FOLDER = Paths.get("D:\\temp");

    @Override
    public Class<?> loadClass(String name) throws ClassNotFoundException {

        try {
            return super.loadClass(name);
        }
        catch (Exception ex) {
            LOG.info("'" + name + "' can't be loaded by any parent");
        }

        try {

            if (name.contains("AAA")) {

                String fullPath = name;

                fullPath = fullPath.replace(".", "\\") + ".class";

                Path classPath = BASE_FOLDER.resolve(fullPath);

                if (!Files.exists(classPath)) {
                    throw new ClassNotFoundException();
                }

                byte[] classBytecode = Files.readAllBytes(classPath);
                Class<?> clazz = defineClass(name, classBytecode, 0,
                        classBytecode.length);
                resolveClass(clazz);
                return clazz;
            }
        }
        catch (Exception ex) {
            LOG.error(ex);
        }

        return super.loadClass(name);
    }

}
