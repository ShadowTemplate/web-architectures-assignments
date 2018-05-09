import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.*;
import javax.tools.JavaCompiler;
import javax.tools.JavaFileObject;
import javax.tools.StandardJavaFileManager;
import javax.tools.ToolProvider;

public class BeanAnnotator {

    public static void main(String[] args) {
        if (args.length != 1) {
            System.err.println("Please specify the .java file of the class to annotate.");
            return;
        }

        String classFile = args[0],
                className = getClassName(classFile),
                tempFolder = System.getProperty("java.io.tmpdir");

        try {
            System.out.println("Trying to compile class " + className + "...");
            compileClass(classFile, tempFolder);
        } catch (IOException ex) {
            System.err.println("POJO compilation failed. Please, make sure the class is properly written.");
            System.err.println(ex.getMessage());
            ex.printStackTrace();
            return;
        }

        try {
            System.out.println("Trying to load class " + className + " to check annotations...");
            Class<?> compiledClass = loadClass(className, tempFolder);
            if (isClassAnnotatedWith(compiledClass, Bean.class)) {
                System.out.println("Class will be annotated to become Entity since it is annotated with @" + Bean.class.getCanonicalName());
                generateBeanAnnotations(classFile, className, hasEmptyConstructor(compiledClass));
            } else {
                System.out.println("Class won't be further processed since it isn't annotated with @" + Bean.class.getCanonicalName());
            }
        } catch (Exception ex) {
            System.err.println(className + " class was successfully compiled, but couldn't be loaded by the class loader");
            System.err.println(ex.getMessage());
            ex.printStackTrace();
        }
    }

    private static String getClassName(String classFile) {
        if (classFile.contains(File.separator)) {
            return classFile.substring(classFile.lastIndexOf(File.separator) + 1, classFile.lastIndexOf('.'));
        } else {
            return classFile.substring(0, classFile.lastIndexOf('.'));
        }
    }

    private static String getClassFolder(String classFile) {
        if (classFile.contains(File.separator)) {
            return classFile.substring(0, classFile.lastIndexOf(File.separator) + 1);
        } else {
            return ".";
        }
    }

    private static void compileClass(String sourceFile, String compilationFolder) throws IOException {
        JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
        StandardJavaFileManager fileManager = compiler.getStandardFileManager(null, null, null);
        List<File> sourceFileList = Collections.singletonList(new File(sourceFile));
        String[] options = new String[]{"-d", compilationFolder};
        Iterable<? extends JavaFileObject> compilationUnits = fileManager.getJavaFileObjectsFromFiles(sourceFileList);
        JavaCompiler.CompilationTask task = compiler.getTask(null, fileManager, null,
                Arrays.asList(options), null, compilationUnits);
        task.call();
        fileManager.close();
    }

    private static Class<?> loadClass(String className, String folder) throws MalformedURLException, ClassNotFoundException {
        URL[] classPath = {new File(folder).toURI().toURL()};
        return new URLClassLoader(classPath).loadClass(className);
    }

    private static boolean isClassAnnotatedWith(Class<?> compiledClass, Class annotation) {
        return Arrays.stream(compiledClass.getAnnotations())
                .anyMatch(a -> a.annotationType().getCanonicalName().equals(annotation.getCanonicalName()));
    }

    private static boolean hasEmptyConstructor(Class<?> compiledClass) {
        return Arrays.stream(compiledClass.getConstructors()).anyMatch(c -> c.getParameterCount() == 0);
    }

    private static void generateBeanAnnotations(String classFile, String className, boolean hasEmptyConstructor) {
        try {
            String newSourceCode = BeanAnnotatorToolkit.annotateUnit(classFile, hasEmptyConstructor);
            String classFolder = getClassFolder(classFile);
            String outputFolder = classFolder + File.separator + "generated" + File.separator,
                    outputFile = outputFolder + className + ".java";
            new File(outputFolder).mkdirs();
            try(PrintWriter out = new PrintWriter(outputFile)) {
                out.println(newSourceCode);
                System.out.println("\nGenerated class:\n");
                System.out.println(newSourceCode);
                System.out.println("\n\nNew class written into " + outputFile);
            }
        } catch (Exception ex) {
            System.err.println(ex.getMessage());
            ex.printStackTrace();
        }
    }
}
