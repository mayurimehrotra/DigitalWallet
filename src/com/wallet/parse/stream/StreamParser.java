package com.wallet.parse.stream;

import java.io.IOException;

import com.wallet.parse.common.impl.AbstractParser;
import com.wallet.process.ProcessPayment;

public class StreamParser extends AbstractParser{
	
	public void parse(ProcessPayment processPayment) throws IOException {
		readFile(STREAM_FILE_NAME, this, processPayment);
	}
}
