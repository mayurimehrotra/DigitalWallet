package com.wallet;

import java.io.IOException;

import com.wallet.parse.batch.BatchParser;
import com.wallet.parse.stream.StreamParser;
import com.wallet.process.ProcessPayment;

public class Main {

	public static void main(String[] args) throws IOException {
		
		ProcessPayment processPayment = new ProcessPayment();
		BatchParser batchParser = new BatchParser();
		StreamParser streamParser = new StreamParser();
		
		batchParser.parse(processPayment);
		streamParser.parse(processPayment);
		
		processPayment.closeResources();
	}

}
