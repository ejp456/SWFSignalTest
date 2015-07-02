package com.jrabatin.prototype.workflow;

import com.amazonaws.ClientConfiguration;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.simpleworkflow.AmazonSimpleWorkflow;
import com.amazonaws.services.simpleworkflow.AmazonSimpleWorkflowClient;
import com.amazonaws.services.simpleworkflow.model.WorkflowExecution;
import com.jrabatin.prototype.Config;

/**
 * @author John Rabatin
 */
public class Main {
  private static final int NUM_RECORDS = Config.WORKFLOW_NUMRECORDS;
  public static void main(String[] args) {
    ClientConfiguration config = new ClientConfiguration().withSocketTimeout(1000*1000);

    String swfAccessId = "";
    String swfSecretKey = "";
    AWSCredentials awsCredentials = new BasicAWSCredentials(swfAccessId, swfSecretKey);

    AmazonSimpleWorkflow service = new AmazonSimpleWorkflowClient(awsCredentials, config);
    service.setEndpoint("https://swf.us-east-1.amazonaws.com");

    String domain = "prototype";

    SignalWorkflowClientExternalFactory factory = new SignalWorkflowClientExternalFactoryImpl(service, domain);
    SignalWorkflowClientExternal flow = factory.getClient("workflow");
    WorkflowExecution execution = flow.getWorkflowExecution();
    flow.process(NUM_RECORDS);
    System.out.println("RunID: " + execution.getRunId());
    System.out.println("WorkflowID: " + execution.getWorkflowId());
    try {
      Thread.sleep(2000); //just wait a couple seconds
    } catch (InterruptedException ex) {
    }
    System.out.println("Pausing.");
    flow.toggleFlow();
  }
}
