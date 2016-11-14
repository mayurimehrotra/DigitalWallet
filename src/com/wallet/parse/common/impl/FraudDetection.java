package com.wallet.parse.common.impl;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

public interface FraudDetection {
	Charset ENCODING = StandardCharsets.UTF_8;
	String NEW_LINE_DELIMITER = "\n";
	
	String EXPECTED_HEADER = "time, id1, id2, amount, message";
	
	String INPUT_FILE_DIR = "paymo_input";
	String BATCH_FILE_NAME = "batch_payment.txt";
	String STREAM_FILE_NAME = "stream_payment.txt";
	
	String OUT_FILE_DIR="paymo_output";
	String OUT_FILE_FEATURE1="output1.txt";
	String OUT_FILE_FEATURE2="output2.txt";
	String OUT_FILE_FEATURE3="output3.txt";
	
	String TRUSTED_PAYMENT="trusted";
	String UNVERIFIED_PAYMENT="unverified";
	
	String CACHE_ID_DELIMIMTER = "-";
	int BFS_MAX_DEPT = 4;
	
}
