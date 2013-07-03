package ch.hesso.aigs.ai.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 *
 */
@Retention ( RetentionPolicy.RUNTIME )
@Target ( ElementType.FIELD )
public @interface AIDigitParameters {

  /**
   * @return
   */
  double min () default 0.0;

  /**
   * @return
   */
  double max () default 0.0;

  /**
   * @return
   */
  double value () default 0.0;
}
