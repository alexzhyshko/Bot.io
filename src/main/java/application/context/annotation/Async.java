package application.context.annotation;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;


/**
 * 
 * @author Oleksandr Zhyshko
 *
 */
@Retention(RUNTIME)
@Target({TYPE, METHOD})
public @interface Async {

}
