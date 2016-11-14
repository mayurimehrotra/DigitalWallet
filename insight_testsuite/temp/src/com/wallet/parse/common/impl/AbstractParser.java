package com.wallet.parse.common.impl;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Timestamp;
import java.util.Scanner;

import com.wallet.parse.batch.BatchParser;
import com.wallet.parse.stream.StreamParser;
import com.wallet.process.ProcessPayment;
import com.wallet.transaction.Transaction;
import com.wallet.user.User;

public class AbstractParser implements FraudDetection{

	public void readFile(String fileName, Object object, ProcessPayment processPayment) throws IOException {
		boolean isExpectedHeader = false;
		Path path = Paths.get(String.format("%s/%s", INPUT_FILE_DIR, fileName));
		try (Scanner scanner =  new Scanner(path, ENCODING.name())){
			scanner.useDelimiter(NEW_LINE_DELIMITER);
			
			//parse header
			if(scanner.hasNextLine()){
				String header = scanner.next();
				if(header.equals(EXPECTED_HEADER)){
					isExpectedHeader = true;
				}else{
					//log incorrect header
				}
			}
			else{
				//File is empty
			}
			
			//parse rest of the file
			if(isExpectedHeader){
				while (scanner.hasNextLine()){
					String line = scanner.nextLine();
					if(! line.isEmpty()){
						
						if(! (line.matches(".*, [0-9]+, [0-9]+, [0-9]*.[0-9]*, .*"))){
							System.out.println("Line not matched expected format: "+ line);
						}else{
							if(object instanceof BatchParser){
								processPayment.addEntryinGraph(parseLine(line));
							}
							else if(object instanceof StreamParser){
								processPayment.procesPayment(parseLine(line));
							}
						}
					}else{
						//empty line
					}
				}
			}     
		}
	}
	public Transaction parseLine(String line ){
		
		String [] attributes = line.trim().split(",");
		if(attributes[0] == null || attributes[0].isEmpty()){
			System.out.println("error during parsing timestamp");
		}else if(attributes[1] == null || attributes[1].isEmpty()){
			System.out.println("error during parsing fromID");
		}else if(attributes[2] == null || attributes[2].isEmpty()){
			System.out.println("error during parsing toID");
		}else if(attributes[3] == null || attributes[3].isEmpty()){
			System.out.println("error during parsing amount");
		}else if (attributes[4] == null || attributes[4].isEmpty()){
			System.out.println("error during parsing message");
		}	
		
		return new Transaction(Timestamp.valueOf(attributes[0].trim()), 
				new User(attributes[1].trim()), 
				new User(attributes[2].trim()),
				Float.parseFloat(attributes[3].trim()),
				attributes[4].trim());
	}

}