package ch.hesso.aigs.util;

import com.google.common.collect.ImmutableSet;
import com.google.common.reflect.ClassPath;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 */
public final class ClassAIFinder<C> {

  // Logger java
  private static final Logger LOG = Logger.getLogger ( ClassAIFinder.class.getName () );
  private ClassPath _classPath;

  public ClassAIFinder () {
    try {
      _classPath = ClassPath.from ( ClassLoader.getSystemClassLoader () );
    } catch ( IOException e ) {
      e.printStackTrace ();
      LOG.log ( Level.SEVERE, "[ClassAIFinder.ClassAIFinder]: " + e.getMessage () );
    }
  }

  /**
   * @param clazz
   * @param topDirectory
   * @return
   */
  public ImmutableSet<Class<? extends C>> getAIClasses ( final Class<C> clazz, final String topDirectory ) {
    ImmutableSet<ClassPath.ClassInfo> classes = _classPath.getTopLevelClassesRecursive ( topDirectory );
    Set<Class<? extends C>> toReturn = new HashSet<> ();
    for ( ClassPath.ClassInfo info : classes ) {
      Class<?> current = info.load ();
      if ( clazz.isAssignableFrom ( current ) && !current.getName ().equals ( clazz.getName () ) ) {
        toReturn.add ( ( Class<? extends C> ) current ); // If it is assignable from class, it extends or implements it
      }
    }
    return ImmutableSet.copyOf ( toReturn );
  }
}
