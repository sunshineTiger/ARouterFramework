package com.example.annotations_compiler;

import com.example.annotations.BindPath;
import com.google.auto.service.AutoService;


import java.io.IOException;
import java.io.PrintWriter;
import java.io.Writer;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedOptions;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.PackageElement;
import javax.lang.model.element.TypeElement;
import javax.tools.Diagnostic;
import javax.tools.JavaFileObject;

@AutoService(Processor.class)
public class AnnotationCompiler extends AbstractProcessor {

    public static final String SUFFIX = "AutoGenerate";
    public static final String PREFIX = "My_";

    Filer filer;
    private Messager messager;

    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv);
        filer = processingEnv.getFiler();
        messager = processingEnv.getMessager();
    }

    @Override
    public Set<String> getSupportedAnnotationTypes() {
        Set<String> types = new HashSet<String>();
        types.add(BindPath.class.getCanonicalName());
        return types;
    }

    @Override
    public SourceVersion getSupportedSourceVersion() {

        return processingEnv.getSourceVersion();
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        Set<? extends Element> elementsAnnotatedWith = roundEnv.getElementsAnnotatedWith(BindPath.class);
        Map<String, String> map = new HashMap<String, String>();
        for (Element s : elementsAnnotatedWith) {
            TypeElement typeElement = (TypeElement) s;
            BindPath an = typeElement.getAnnotation(BindPath.class);
            String key = an.value();
            String activityName = typeElement.getQualifiedName().toString();
            map.put(key, activityName + ".class");
        }
        if (map.size() > 0) {
            Writer writer = null;
            String activityName = "ActivityUtil" + System.currentTimeMillis();
            try {
                JavaFileObject classFile = processingEnv.getFiler().createSourceFile("com.zh.util." + activityName);
                writer = classFile.openWriter();
                PrintWriter bf = new PrintWriter(writer);
                bf.println("package com.zh.util;\n");
                bf.println("import com.example.arouter.ARouter;\n" +
                        "import com.example.arouter.IRouter;\n" +
                        "\n" +
                        "public class " + activityName + " implements IRouter {\n" +
                        "    @Override\n" +
                        "    public void putActivity() {");
                Iterator<String> iterator = map.keySet().iterator();
                while (iterator.hasNext()) {
                    String key = iterator.next();
                    String className = map.get(key);
                    bf.println("ARouter.getInstance().putActivity(\"" + key + "\"," + className + ");\n");
                }
                bf.println("\n}\n}");
//                writer.write(bf.toString());
                bf.flush();
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (writer != null) {
                    try {
                        writer.close();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

            }
            return true;

        }
        return false;
    }
}
