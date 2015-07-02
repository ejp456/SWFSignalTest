package com.jrabatin.prototype.workflow;

import com.amazonaws.ClientConfiguration;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.simpleworkflow.AmazonSimpleWorkflow;
import com.amazonaws.services.simpleworkflow.AmazonSimpleWorkflowClient;
import com.amazonaws.services.simpleworkflow.flow.ActivityWorker;
import com.amazonaws.services.simpleworkflow.flow.WorkflowWorker;
import com.jrabatin.prototype.activities.impl.FirstActivitiesImpl;
import com.jrabatin.prototype.activities.impl.SecondActivitiesImpl;
import com.jrabatin.prototype.activities.impl.ThirdActivitiesImpl;
import com.jrabatin.prototype.workflow.impl.SignalWorkflowImpl;

/**
 * @author John Rabatin
 */
public final class SignalWorkflowWorker {
  public static void main(String[] args) throws Exception {
    ClientConfiguration config = new ClientConfiguration().withSocketTimeout(70*1000);

    String swfAccessId = "AKIAJHQDSYUO53VABF5Q";
    String swfSecretKey = "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";
    AWSCredentials awsCredentials = new BasicAWSCredentials(swfAccessId, swfSecretKey);

    AmazonSimpleWorkflow service = new AmazonSimpleWorkflowClient(awsCredentials, config);
    service.setEndpoint("https://swf.us-east-1.amazonaws.com");

    String domain = "prototype";
    String taskListToPoll = "tasklist";

    //register activities
    ActivityWorker aw = new ActivityWorker(service, domain, taskListToPoll);
    aw.addActivitiesImplementation(new FirstActivitiesImpl());
    aw.addActivitiesImplementation(new SecondActivitiesImpl());
    aw.addActivitiesImplementation(new ThirdActivitiesImpl());
    aw.start();

    //register workflow
    WorkflowWorker wfw = new WorkflowWorker(service, domain, taskListToPoll);
    wfw.addWorkflowImplementationType(SignalWorkflowImpl.class);
    wfw.start();
  }
}
