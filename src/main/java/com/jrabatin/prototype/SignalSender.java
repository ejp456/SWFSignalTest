package com.jrabatin.prototype;
import com.amazonaws.ClientConfiguration;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.simpleworkflow.AmazonSimpleWorkflow;
import com.amazonaws.services.simpleworkflow.AmazonSimpleWorkflowClient;
import com.amazonaws.services.simpleworkflow.model.SignalWorkflowExecutionRequest;
import com.amazonaws.services.simpleworkflow.model.WorkflowExecution;
import com.jrabatin.prototype.workflow.SignalWorkflowClientExternal;
import com.jrabatin.prototype.workflow.SignalWorkflowClientExternalFactory;
import com.jrabatin.prototype.workflow.SignalWorkflowClientExternalFactoryImpl;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author John Rabatin
 */
public class SignalSender {
  private static final String DOMAIN = "prototype";
  private static final String WORKFLOW_ID = "workflow";
  private static final String SIGNAL_NAME = "PauseWorkflow";
  private static final String ENDPOINT = "http://swf.us-east-1.amazonaws.com";
  private static AWSCredentials awsCredentials;

  public static void main(String[] args) {
    String swfAccessId = "";
    String swfSecretKey = "";
    awsCredentials = new BasicAWSCredentials(swfAccessId, swfSecretKey);

    //sendSignal();
    ClientConfiguration config = new ClientConfiguration().withSocketTimeout(1000*1000);
    AmazonSimpleWorkflow service = new AmazonSimpleWorkflowClient(awsCredentials, config);
    service.setEndpoint("https://swf.us-east-1.amazonaws.com");

    String domain = "prototype";
    SignalWorkflowClientExternalFactory factory = new SignalWorkflowClientExternalFactoryImpl(service, domain);
    SignalWorkflowClientExternal flow = factory.getClient("workflow");
    WorkflowExecution execution = flow.getWorkflowExecution();
    flow.toggleFlow();

    /*
    ClientConfiguration config = new ClientConfiguration().withSocketTimeout(1000*1000);
    AmazonSimpleWorkflow service = new AmazonSimpleWorkflowClient(awsCredentials, config);
    SignalWorkflowExecutionRequest request = new SignalWorkflowExecutionRequest();
    request.setDomain(DOMAIN);
    request.setWorkflowId(WORKFLOW_ID);
    request.setSignalName(SIGNAL_NAME);
    request.setRunId("22rg/E1qVJwoCaJHLo94CzEULoL3/WbUcZqr940si9iVs=");
    request.setInput("ok");
    service.signalWorkflowExecution(request);

    System.out.println("Sent request: " + request.toString());
        */
  }

  private static void sendSignal() {
    String body = "{" + "\"domain\": " + "\"" + DOMAIN + "\"" + ",\n" +
        "\"signalName\": " + "\"" + SIGNAL_NAME + "\"" + ",\n" +
        "\"workflowId\": " + "\"" + WORKFLOW_ID + "\""  + "}";
    Map<String, String> headers = new TreeMap<String, String>();
    Date date = new Date();

    SimpleDateFormat dateFormat = new SimpleDateFormat(
            "EEE, dd MMM yyyy HH:mm:ss z", Locale.US);
    dateFormat.setTimeZone(TimeZone.getTimeZone("GMT"));

    String formattedDate = dateFormat.format(date);

    headers.put("content-type", "application/x-amz-json-1.0");
    headers.put("host", "swf.us-east-1.amazonaws.com");
    headers.put("x-amz-date", formattedDate);
    headers.put("x-amz-target", "SimpleWorkflowService.SignalWorkflowExecution");

    try {
      System.out.println("Sending signal request with data: ");
      System.out.println(body);
      System.out.println();
      String signature = new AwsSignature()
          .withCredentials(awsCredentials)
          .withHeaders(headers)
          .withDigestSize(256)
          .build();
      String authorizationString = "AWS3 AWSAccessKeyId=" + awsCredentials.getAWSAccessKeyId() +
              ",Algorithm=HmacSHA256,SignedHeaders=content-type;host;x-amz-date;x-amz-target,Signature=" + signature;
      System.out.println("\nAuth String: " + authorizationString);
      HttpResponse<String> response = Unirest.post(ENDPOINT)
        .header("host", "swf.us-east-1.amazonaws.com")
        .header("x-amz-date", formattedDate)
        .header("x-amz-target", "SimpleWorkflowService.SignalWorkflowExecution")
        .header("x-amzn-authorization", authorizationString)
        .header("Content-Type", "application/x-amz-json-1.0")
        .body(body)
        .asString();
      System.out.println("Response Code: " + response.getStatus() + ". Response Body:");
      System.out.println(response.getBody());
    } catch (UnirestException UE) {
      System.out.println("Exception during signal request ==> " + UE);
    } catch (java.security.SignatureException SE) {
      System.out.println("Exception during signal request ==> " + SE);
    }
  }
}
