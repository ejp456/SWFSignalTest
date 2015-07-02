package com.jrabatin.prototype;

import com.amazonaws.auth.AWSCredentials;

import java.util.Date;
import java.util.List;

/**
 * Configuration object used for signing AWS requests.
 * @author John Rabatin
 */
public class SigningConfiguration {
  private Date date;
  private String host;
  private String region;
  private String service;
  private String algorithm;
  private AWSCredentials credentials;
  private List<String> signedHeaders;

  public SigningConfiguration() {
    date = null;
    region = null;
    service = null;
    credentials = null;
    algorithm = null;
    signedHeaders = null;
  }

  public AWSCredentials getCredentials() {
    return credentials;
  }

  public void setCredentials(AWSCredentials credentials) {
    this.credentials = credentials;
  }

  public String getService() {
    return service;
  }

  public void setService(String service) {
    this.service = service;
  }

  public String getRegion() {
    return region;
  }

  public void setRegion(String region) {
    this.region = region;
  }

  public Date getDate() {
    return date;
  }

  public void setDate(Date date) {
    this.date = date;
  }

  public String getHost() {
    return host;
  }

  public void setHost(String host) {
    this.host = host;
  }

  public String getAlgorithm() {
    return algorithm;
  }

  public void setAlgorithm(String algorithm) {
    this.algorithm = algorithm;
  }

  public List<String> getSignedHeaders() {
    return signedHeaders;
  }

  public void setSignedHeaders(List<String> signedHeaders) {
    this.signedHeaders = signedHeaders;
  }
}
