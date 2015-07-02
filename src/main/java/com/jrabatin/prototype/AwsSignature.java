package com.jrabatin.prototype;

import com.amazonaws.auth.AWSCredentials;
import org.apache.commons.codec.binary.Base64;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.security.MessageDigest;
import java.security.SignatureException;
import java.util.Map;

/**
 * Used to calculate AWS request signatures using the version 3 signing process.
 * @author John Rabatin
 */
public class AwsSignature {
  private String canonicalHeaderString = null;
  private String httpMethod = "POST";
  private AWSCredentials credentials = null;
  private int digestSize = 1;

  /**
   * Minimum request headers expected:
   * - host
   * - x-amz-date
   * Should contain any headers of the form x-amz-*.
   * @param signingHeaders
   *      Headers to use in the signature calculation.
   * @return Unbuilt AwsSignature with valid canonical header string.
   */
  public AwsSignature withHeaders(Map<String, String> signingHeaders) {
    if (signingHeaders == null || signingHeaders.size() < 1) {
      throw new IllegalArgumentException("Header map was null or empty.");
    }
    StringBuilder sb = new StringBuilder();
    sb = new StringBuilder();
    for (Map.Entry<String, String> header : signingHeaders.entrySet()) {
      if (header != null) {
        sb.append(header.getKey().trim());
        sb.append(":");
        sb.append(header.getValue().trim());
        sb.append("\n");
      }
    }
    this.canonicalHeaderString = sb.toString();
    return this;
  }

  /**
   * @return Unbuilt AwsSignature with valid credentials.
   */
  public AwsSignature withCredentials(AWSCredentials credentials) {
    if (credentials != null) {
      this.credentials = credentials;
    }
    return this;
  }

  /**
   * Optional. SHA algorithm defaults to a digest size of 1.
   * @return Unbuilt AwsSignature with custom digest size (ex. 256)
   */
  public AwsSignature withDigestSize(int digestSize) {
    if (digestSize > 0) {
      this.digestSize = digestSize;
    }
    return this;
  }

  /**
   * Optional. Specify the http method for the request.
   * Defaults to POST and only POST, GET, or PUT are valid.
   *
   * @return Unbuilt AwsSignature with valid http method. (ex. 256)
   */
  public AwsSignature withHttpMethod(String httpMethod) {
    if (httpMethod.equalsIgnoreCase("GET") || httpMethod.equalsIgnoreCase("PUT")) {
      this.httpMethod = httpMethod;
    }
    return this;
  }

  /**
   * Build the signature.
   * Step 1.) Create a string-to-sign using the canonical header string.
   * Step 2.) Compute a SHA digest of the string-to-sign.
   * Step 3.) Compute an HMAC-SHA digest using the digest from Step 3 and the AWS Secret Key.
   * Step 4.) Base64 encode the hash from step 4.
   * @return String representation of the Base64 encoded signature.
   */
  public String build() throws java.security.SignatureException  {
    if (canonicalHeaderString == null || credentials == null ||
        canonicalHeaderString.length() == 0 || credentials.getAWSSecretKey() == null) {
      return null;
    }

    StringBuilder sTSBuilder = new StringBuilder();
    sTSBuilder.append(httpMethod);            //Line 1: "POST"
    sTSBuilder.append("\n/\n\n");             //Line 2: "/"  Line 3: empty
    sTSBuilder.append(canonicalHeaderString); //Line 4 - n
    sTSBuilder.append("\n");

    String finalSignature, stringToSign = sTSBuilder.toString();
    byte[] hashedSTS, rawSignature, stringToSignAsBytes = stringToSign.getBytes();
    try {
      hashedSTS = computeSHA(stringToSignAsBytes);
      rawSignature = computeHMAC(hashedSTS, credentials.getAWSSecretKey().getBytes());
      finalSignature = new String(Base64.encodeBase64(rawSignature));

      System.out.println("CHS: ");
      System.out.println(canonicalHeaderString);
      System.out.println("STS: ");
      System.out.println(stringToSign);
      System.out.println("Secret Key: ");
      System.out.println(credentials.getAWSSecretKey());
      System.out.println("Digest Size: ");
      System.out.println(digestSize);
    }
    catch (Exception e) {
      throw new SignatureException("Failed to generate signature. \n" + e.getMessage());
    }

    return finalSignature;
  }

  private byte[] computeSHA(byte[] data) throws java.security.NoSuchAlgorithmException {
    String algorithm = "SHA-" + digestSize;
    MessageDigest md = MessageDigest.getInstance(algorithm);
    return md.digest(data);
  }

  private byte[] computeHMAC(byte[] data, byte[] key) throws Exception  {
    String algorithm = "HmacSHA" + digestSize;
    Mac mac = Mac.getInstance(algorithm);
    mac.init(new SecretKeySpec(key, algorithm));
    return mac.doFinal(data);
  }
}
