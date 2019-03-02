package edu.northeastern.ccs.im.client.SecondLevel;

import edu.northeastern.ccs.im.client.CoreOperation;
import edu.northeastern.ccs.im.client.ParentModel;

public interface SecondLevelModule extends CoreOperation {

  void doSecondLevelOperations(ParentModel secondLevelModel);
}
