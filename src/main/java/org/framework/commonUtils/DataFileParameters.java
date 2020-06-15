package org.framework.commonUtils;

/**
 * // TODO Comment
 */
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface DataFileParameters {
  String name();
  String path() default "";
  boolean debug() default false;
  boolean loadEmptyColumns() default false;
  String encoding() default "UTF-8";
  String sheetName() default "test";

}
