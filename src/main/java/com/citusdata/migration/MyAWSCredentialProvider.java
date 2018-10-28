package com.citusdata.migration;

import com.amazonaws.auth.*;
import com.amazonaws.services.securitytoken.AWSSecurityTokenService;
import com.amazonaws.services.securitytoken.AWSSecurityTokenServiceClientBuilder;
import com.amazonaws.services.securitytoken.model.AssumeRoleRequest;
import com.amazonaws.services.securitytoken.model.AssumeRoleResult;

import java.util.Scanner;

public class MyAWSCredentialProvider {
    public static AWSStaticCredentialsProvider getMFAAssumeRoleCredentialProvider(String mfaARN, String assumeRoleARN, String region, String mfaCode) {
        AWSCredentials awsCredentials = new DefaultAWSCredentialsProviderChain().getCredentials();
        final String accessKey = awsCredentials.getAWSAccessKeyId();
        final String secretKey = awsCredentials.getAWSSecretKey();
        final Integer duration = 3600;
        final String SESSION_NAME = "test";

        BasicAWSCredentials basicCredentials =
                new BasicAWSCredentials(accessKey, secretKey);

        AWSSecurityTokenServiceClientBuilder stsBuilder =
                AWSSecurityTokenServiceClientBuilder
                        .standard()
                        .withCredentials(
                                new AWSStaticCredentialsProvider(
                                        basicCredentials
                                )
                        )
                        .withRegion(region);

        AssumeRoleRequest assumeRequest = new AssumeRoleRequest()
                .withRoleArn(assumeRoleARN)
                .withDurationSeconds(duration)
                .withRoleSessionName(SESSION_NAME)
                .withSerialNumber(mfaARN)
                .withTokenCode(mfaCode);
        AWSSecurityTokenService sts =
                stsBuilder.build();
        AssumeRoleResult t = sts.assumeRole(assumeRequest);

        BasicSessionCredentials basicSessionCredentials =
                new BasicSessionCredentials(
                        t.getCredentials().getAccessKeyId(),
                        t.getCredentials().getSecretAccessKey(),
                        t.getCredentials().getSessionToken()
                );

        return new AWSStaticCredentialsProvider(
                basicSessionCredentials
        );
    }
}
