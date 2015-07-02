package com.jrabatin.prototype.activities.impl;

import com.jrabatin.prototype.activities.SecondActivities;

/**
 * @author John Rabatin
 */
public class SecondActivitiesImpl implements SecondActivities {

  @Override
  public void doSecond(int n) throws InterruptedException {
    for (int i = 0; i < n; i++) {
      //emulate some processing time
      Thread.sleep(1000);
      System.out.println("[SecondAct] Step " + i);
    }
  }
}
