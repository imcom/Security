#! /bin/bash

java -classpath ./lib/bcprov-jdk15on-147.jar:SampleAzn.jar:SampleLM.jar:SampleAction.jar:SampleServices.jar -Djava.security.manager -Djava.security.policy==sampleazn.policy -Djava.security.auth.login.config==sample_jaas.config sample.SampleAzn
