package com.jrabatin.prototype.workflow.impl;

import com.amazonaws.services.simpleworkflow.flow.*;
import com.amazonaws.services.simpleworkflow.flow.annotations.Asynchronous;
import com.amazonaws.services.simpleworkflow.flow.core.OrPromise;
import com.amazonaws.services.simpleworkflow.flow.core.Promise;
import com.amazonaws.services.simpleworkflow.flow.core.Settable;
import com.amazonaws.services.simpleworkflow.flow.core.TryCatchFinally;
import com.jrabatin.prototype.activities.*;
import com.jrabatin.prototype.workflow.SignalWorkflow;

/**
 * @author John Rabatin
 */
public class SignalWorkflowImpl implements SignalWorkflow {

  private final FirstActivitiesClient firstActClient = new FirstActivitiesClientImpl();
  private final SecondActivitiesClient secondActClient = new SecondActivitiesClientImpl();
  private final ThirdActivitiesClient thirdActClient = new ThirdActivitiesClientImpl();
  private Settable<Void> ready = new Settable<Void>();
  private boolean paused = false;
  private WorkflowClock clock;
  private int numRecords = 0;
  private int testInt = 0;
  private final Void GO = null;

  @Override
  public void process(final int numRecords) {
    this.numRecords = numRecords;
    //ready.set(GO);
    System.out.println("Starting workflow.");
    new TryCatchFinally() {
      @Override
      protected void doTry() throws Throwable {
        System.out.println("Ready: " + ready.isReady());
        Promise<Void> firstActFinished = firstActClient.doFirst(numRecords, ready);
        Promise<Void> secondActFinished = secondActClient.doSecond(numRecords, ready, firstActFinished);
        Promise<Void> thirdActFinished = thirdActClient.doThird(numRecords, ready, secondActFinished);
      }

      @Override
      protected void doCatch(Throwable e) throws Throwable {
        System.out.println("Caught exception: " + e.toString());
        e.printStackTrace();
      }

      @Override
      protected void doFinally() throws Throwable {
        System.out.println("Finished workflow.");
      }
    };
  }

  @Override
  public void toggleFlow() {
    testInt++;
    System.out.println("Test Increment: " + testInt);
    if (ready.isReady()) {
      System.out.println("Pausing flow.");
      ready = new Settable<Void>();
    } else {
      System.out.println("Resuming flow.");
      ready.set(GO);
    }
  }
}
