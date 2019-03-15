package edu.northeastern.ccs.im.factories;

import org.junit.Test;

import edu.northeastern.ccs.im.clientmenu.clientutils.CurrentLevel;
import edu.northeastern.ccs.im.clientmenu.factories.ModuleFactory;

import static org.junit.Assert.assertTrue;

public class ModuleFactoryTest {

  @Test
  public void test() {
    ModuleFactory moduleFactory = new ModuleFactory();

    boolean isCoreOperationType;
    isCoreOperationType = moduleFactory.getModelFromFactory(CurrentLevel.LOGIN_LEVEL) != null;
    assertTrue(isCoreOperationType);

    isCoreOperationType = moduleFactory.getModelFromFactory(CurrentLevel.REGISTRATION) != null;
    assertTrue(isCoreOperationType);

    isCoreOperationType = moduleFactory.getModelFromFactory(CurrentLevel.LEVEL1) != null;
    assertTrue(isCoreOperationType);

    isCoreOperationType = moduleFactory.getModelFromFactory(CurrentLevel.LEVEL2) != null;
    assertTrue(isCoreOperationType);
  }
}
