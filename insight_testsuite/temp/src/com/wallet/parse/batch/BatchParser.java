package com.wallet.parse.batch;

import java.io.IOException;

import com.wallet.parse.common.impl.AbstractParser;
import com.wallet.process.ProcessPayment;

public class BatchParser extends AbstractParser {

	public void parse(ProcessPayment processPayment) throws IOException {
		readFile(BATCH_FILE_NAME, this, processPayment);
	}

}
