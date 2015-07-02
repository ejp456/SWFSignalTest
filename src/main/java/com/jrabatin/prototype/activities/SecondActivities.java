package com.jrabatin.prototype.activities;

import com.amazonaws.services.simpleworkflow.flow.annotations.Activities;
import com.amazonaws.services.simpleworkflow.flow.annotations.ActivityRegistrationOptions;
import com.jrabatin.prototype.Config;

/**
 * @author John Rabatin
 */
@Activities(version= Config.WORKFLOW_VERSION)
@ActivityRegistrationOptions(defaultTaskScheduleToStartTimeoutSeconds = 3600,
        defaultTaskStartToCloseTimeoutSeconds = 3600)
public interface SecondActivities {
  void doSecond(int n) throws InterruptedException;
}
