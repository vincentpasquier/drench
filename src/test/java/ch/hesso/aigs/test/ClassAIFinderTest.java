package ch.hesso.aigs.test;

import ch.hesso.aigs.ai.AI;
import ch.hesso.aigs.util.ClassAIFinder;
import com.google.common.collect.ImmutableSet;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;

/**
 *
 */
public final class ClassAIFinderTest {

  public static void main ( final String[] args ) throws IllegalAccessException, InstantiationException {
    ClassAIFinder finder = new ClassAIFinder ();
    ImmutableSet<Class<? extends AI>> classes = finder.getAIClasses ( AI.class, "ch.hesso" );
    for ( Class<? extends AI> ais : classes ) {
      System.out.println ( ais.getName () );
      for ( Field field : ais.getDeclaredFields () ) {
        for ( Annotation a : field.getDeclaredAnnotations () ) {
          System.out.println ( a.toString () );
        }
      }
      for ( Annotation a : ais.getAnnotations () ) {
        System.out.println ( a.toString () );
      }
    }
  }
}
