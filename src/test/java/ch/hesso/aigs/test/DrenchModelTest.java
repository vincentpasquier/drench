package ch.hesso.aigs.test;

import ch.hesso.aigs.game.DrenchModel;
import junit.framework.TestCase;
import org.junit.Test;

import java.io.File;

/**
 *
 */
public final class DrenchModelTest extends TestCase {

  private DrenchModel _model;
  private static final int WIDTH = 15;
  private static final int HEIGHT = 15;
  private static final int SIZE = WIDTH * HEIGHT;

  @Override
  public void setUp () {
    _model = DrenchModel.Generator.createRandomModel ( 15, 15, 30 );
  }

  @Test
  public void testFileLoaderFromFile () throws Exception {
    File file = new File ( DrenchModelTest.class.getResource ( "/testGridWork.txt" ).getFile () );
    DrenchModel model = DrenchModel.FileLoader.loadFromFile ( file );
    assertEquals ( 5, model.width () );
    assertEquals ( 5, model.height () );
    assertEquals ( 10, model.turns () );
  }
}
