package com.jrabatin.prototype.activities.impl;

import com.jrabatin.prototype.activities.ThirdActivities;

/**
 * @author John Rabatin
 */
public class ThirdActivitiesImpl implements ThirdActivities {

  @Override
  public void doThird(int n) throws InterruptedException {
    for (int i = 0; i < n; i++) {
      //emulate some processing time
      Thread.sleep(1000);
      System.out.println("[ThirdAct] Step " + i);
    }
  }
}
