package com.jrabatin.prototype.activities.impl;

import com.jrabatin.prototype.activities.FirstActivities;

/**
 * @author John Rabatin
 */
public class FirstActivitiesImpl implements FirstActivities {

  @Override
  public void doFirst(int n) throws InterruptedException{
    for (int i = 0; i < n; i++) {
      //emulate some processing time
      Thread.sleep(1000);
      System.out.println("[FirstAct] Step " + i);
    }
  }
}
