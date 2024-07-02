import com.mirror.annotation.ProcessAnnotation;
import com.mirror.annotation.Report;
import org.junit.Test;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Objects;

public class AnnotationTest {
    /**
     * 这个检测方法输出所有的信息
     * 也为了下方的程序逻辑重设打好基础
     */
    @Test
    public void testAnnotation(){
        Report[] classReports = ProcessAnnotation.class.getAnnotationsByType(Report.class);
        System.out.println("class level classReports");
        for(Report report : classReports){
            System.out.println("Type: "+ report.type()+","+"level "+report.level()+",value:"+report.value());
        }
        try {
            Method method = ProcessAnnotation.class.getMethod("annotateMethod");
            Report reportMethod = method.getAnnotation(Report.class);
            System.out.println("Type: "+ reportMethod.type()+","+"level "+reportMethod.level()+",value:"+reportMethod.value());
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }

        try {
            Field field = ProcessAnnotation.class.getDeclaredField("n");
            Report[] reportFields = field.getAnnotationsByType(Report.class);
            for (Report report:
                 reportFields) {
                System.out.println("Type: "+ report.type()+","+"level "+report.level()+",value:"+report.value());
            }
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
        try {
            Constructor<ProcessAnnotation> constructor = ProcessAnnotation.class.getConstructor();
            Report[] reportConstructor = constructor.getAnnotationsByType(Report.class);
            for(Report report : reportConstructor){
                System.out.println("Type: "+ report.type()+","+"level "+report.level()+",value:"+report.value());
            }
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testCheckAnnotation(){
        for (Method method:
             ProcessAnnotation.class.getMethods()) {
            Report report = method.getAnnotation(Report.class);
            if(report != null && !Objects.equals(report.level(), "error")){
                try {

                    method.invoke(new ProcessAnnotation());

                } catch (IllegalAccessException | InvocationTargetException e) {
                    e.printStackTrace();
                }
            }
        }
    }

}
