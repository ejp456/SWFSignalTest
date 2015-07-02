package com.jrabatin.prototype.workflow;

import com.amazonaws.services.simpleworkflow.flow.annotations.Execute;
import com.amazonaws.services.simpleworkflow.flow.annotations.Signal;
import com.amazonaws.services.simpleworkflow.flow.annotations.Workflow;
import com.amazonaws.services.simpleworkflow.flow.annotations.WorkflowRegistrationOptions;
import com.jrabatin.prototype.Config;

/**
 * @author John Rabatin
 */
@Workflow
@WorkflowRegistrationOptions(defaultExecutionStartToCloseTimeoutSeconds = 3600, defaultTaskStartToCloseTimeoutSeconds = 3600)
public interface SignalWorkflow {
  @Execute(name="Process", version=Config.WORKFLOW_VERSION)
  void process(int numRecords);
  @Signal
  void toggleFlow();
}
