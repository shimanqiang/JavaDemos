package com.huifenqi.study.anno;

import javax.annotation.processing.*;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.*;
import javax.tools.Diagnostic;
import javax.tools.JavaFileObject;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

//生成接口的处理类
//@SupportedAnnotationTypes("com.huifenqi.study.anno.GenerateSQLMapper")
//@SupportedSourceVersion(SourceVersion.RELEASE_8)
public class GenerateSQLMapperProcessor extends AbstractProcessor {
    private Filer filer;
    private Messager messager;

    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv);
        this.filer = processingEnv.getFiler();
        this.messager = processingEnv.getMessager();
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        messager.printMessage(Diagnostic.Kind.NOTE, "开始处理" + GenerateSQLMapper.class.getName() + "注解");

        messager.printMessage(Diagnostic.Kind.NOTE, "开始遍历annotations");

        for (TypeElement te : annotations) {
            messager.printMessage(Diagnostic.Kind.NOTE, te.getSimpleName());
            System.out.println("1111111111");
        }
        //取出每一个用MyAnnotation注解的元素
        for(Element element : roundEnv.getElementsAnnotatedWith(GenerateSQLMapper.class)){
            //如果这个元素是一个方法
            if(element.getKind() == ElementKind.METHOD){
                //强转成方法对应的element，同理，如果你的注解是一个类，那你可以强转成TypeElement
                ExecutableElement executableElement = (ExecutableElement)element;

                //打印方法名
                System.out.println(executableElement.getSimpleName());

                //打印方法的返回类型
                System.out.println(executableElement.getReturnType().toString());

                //获取方法所有的参数
                List<? extends VariableElement> params = executableElement.getParameters();
                //逐个打印参数名
                for(VariableElement variableElement : params){
                    System.out.println(variableElement.getSimpleName());
                }

                //打印注解的值
                System.out.println(executableElement.getAnnotation(GenerateSQLMapper.class).prefix());
            }
            System.out.println("------------------------------");
        }


//        /**roundEnv.getRootElements()会返回工程中所有的Class
//         在实际应用中需要对各个Class先做过滤以提高效率，避免对每个Class的内容都进行扫描*/
//        for (Element e : roundEnv.getRootElements()) {
//            List<String> statements = new ArrayList<>();
//            /*遍历Class内所有元素*/
//            for (Element el : e.getEnclosedElements()) {
//                /*只处理包含了注解并被修饰为public的Field*/
//                if (el.getKind().isField() && el.getAnnotation(GenerateSQLMapper.class) != null && el.getModifiers().contains(Modifier.PUBLIC)) {
//                    /*获取注解信息，生成代码片段*/
//                    GenerateSQLMapper meta = el.getAnnotation(GenerateSQLMapper.class);
////                    int repeat = meta.repeat();
////                    String seed = meta.id();
////                    String result = "";
////                    for (int i = 0; i < repeat; i++) {
////                        result += seed;
////                    }
////                    statements.add("\t\ttarget." + el.getSimpleName() + " = \"" + result + "\";");
//                }
//            }
//            if (statements.size() == 0) {
//                return true;
//            }
//            String enclosingName;
//            if (e instanceof PackageElement) {
//                enclosingName = ((PackageElement) e).getQualifiedName().toString();
//            } else {
//                enclosingName = ((TypeElement) e).getQualifiedName().toString();
//            }
//            /*获取生成类的类名和package*/
//            String pkgName = enclosingName.substring(0, enclosingName.lastIndexOf('.'));
//            String clsName = e.getSimpleName() + "Gen";
//            //log(pkgName + "," + clsName);
//            /*创建文件，写入代码内容*/
//            try {
//                JavaFileObject f = processingEnv.getFiler().createSourceFile(clsName);
//                //log(f.toUri().toString());
//                Writer writer = f.openWriter();
//                PrintWriter printWriter = new PrintWriter(writer);
//                printWriter.println("//Auto generated code, do not modify it!");
//                printWriter.println("package " + pkgName + ";");
//                printWriter.println("\nimport com.moxun.Actor;\n");
//                printWriter.println("public class " + clsName + "<T extends " + e.getSimpleName() + "> implements Actor{");
//                printWriter.println("\tprotected T target;");
//                printWriter.println("\n\tpublic " + clsName + "(T obj) {");
//                printWriter.println("\t\tthis.target = obj;");
//                printWriter.println("\t}\n");
//                printWriter.println("\t@Override");
//                printWriter.println("\tpublic void action() {");
//                for (String statement : statements) {
//                    printWriter.println(statement);
//                }
//                printWriter.println("\t}");
//                printWriter.println("}");
//                printWriter.flush();
//                printWriter.close();
//                writer.close();
//            } catch (IOException e1) {
//                e1.printStackTrace();
//            }
//        }

        return false;
    }


    @Override
    public Set<String> getSupportedAnnotationTypes() {
        Set<String> set = new HashSet<>();
        set.add("com.huifenqi.study.anno.GenerateSQLMapper");
        //return super.getSupportedAnnotationTypes();
        return set;
    }

    @Override
    public SourceVersion getSupportedSourceVersion() {
        //return super.getSupportedSourceVersion();
        return SourceVersion.latestSupported();
    }
}
