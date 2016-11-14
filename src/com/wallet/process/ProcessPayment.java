package com.wallet.process;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayDeque;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Queue;

import com.wallet.parse.common.impl.FraudDetection;
import com.wallet.transaction.Transaction;
import com.wallet.user.User;

public class ProcessPayment extends Graph implements FraudDetection{
	private PrintWriter printWriter1 = null;
	private PrintWriter printWriter2 = null;
	private PrintWriter printWriter3 = null;
	
	public ProcessPayment() throws IOException {
		createOutFiles();
	}

	public void procesPayment(Transaction transaction){
		String cache_key = builCacheID(transaction.getFrom(), transaction.getTo());
		boolean foundInCache = false;
		if( cache.containsKey(cache_key) && 
				cache.get(cache_key) == 0){
			sendOutputToFile(Feature.FEATURE1, TRUSTED_PAYMENT);
			sendOutputToFile(Feature.FEATURE2, TRUSTED_PAYMENT);
			sendOutputToFile(Feature.FEATURE3, TRUSTED_PAYMENT);
			foundInCache = true;
		}else {
			updateCacheUsingBFS(transaction);
		}
		if(! foundInCache){	
			if(cache.containsKey(cache_key) && 
					cache.get(cache_key) == 1){
				sendOutputToFile(Feature.FEATURE1, UNVERIFIED_PAYMENT);
				sendOutputToFile(Feature.FEATURE2, TRUSTED_PAYMENT);
				sendOutputToFile(Feature.FEATURE3, TRUSTED_PAYMENT);
			}else if(cache.containsKey(cache_key) && 
					(cache.get(cache_key) >=2 && cache.get(cache_key) <=4) ){
				sendOutputToFile(Feature.FEATURE1, UNVERIFIED_PAYMENT);
				sendOutputToFile(Feature.FEATURE2, UNVERIFIED_PAYMENT);
				sendOutputToFile(Feature.FEATURE3, TRUSTED_PAYMENT);
			}else{
				sendOutputToFile(Feature.FEATURE1, UNVERIFIED_PAYMENT);
				sendOutputToFile(Feature.FEATURE2, UNVERIFIED_PAYMENT);
				sendOutputToFile(Feature.FEATURE3, UNVERIFIED_PAYMENT);
			}
		}
		
		
	}

	private void sendOutputToFile(Feature feature, String result){
		if(feature.name().equals(Feature.FEATURE1.name())){
			writeToFile(printWriter1, result);
		}else if(feature.name().equals(Feature.FEATURE2.name())){
			writeToFile(printWriter2, result);
		}else if(feature.name().equals(Feature.FEATURE3.name())){
			writeToFile(printWriter3, result);
		}
	}
	
	private void writeToFile(PrintWriter printWriter, String result){
		printWriter.println(result);
		printWriter.flush();
	}
	
	private void createOutFiles() throws IOException {
		
		if(! new File(OUT_FILE_DIR).exists()){
			new File(OUT_FILE_DIR).mkdirs();
		}
		printWriter1 = new PrintWriter(String.format("%s/%s", OUT_FILE_DIR, OUT_FILE_FEATURE1));
		printWriter2 = new PrintWriter(String.format("%s/%s", OUT_FILE_DIR, OUT_FILE_FEATURE2));
		printWriter3 = new PrintWriter(String.format("%s/%s", OUT_FILE_DIR, OUT_FILE_FEATURE3));
	}
	public void closeResources(){
		printWriter1.close();
		printWriter2.close();
		printWriter3.close();
	}
	
	/*
	 * Description: Runs BFS algorithm on the graph and updates cache while exploring the graph
	 * input: Transaction from stream
	 * */
	private void updateCacheUsingBFS(Transaction transaction) {
		
		//consider src as fromUser ID
		Queue<String> openQueue = new ArrayDeque<>();
		Map<String, Boolean> visitedMap = new HashMap<>();
		User from = transaction.getFrom();
		
		openQueue.add(from.getUserId());
		
		int currentDepth = -1,
		elementsToDepthIncrease = 1, 
			      nextElementsToDepthIncrease = 0;
		
		while(! openQueue.isEmpty()){
			String currentFromID = openQueue.poll();
			
			//process currentTransaction
			visitedMap.put(currentFromID, true);
			
			
			List<Transaction> childTransactions = getDirectTransactions(currentFromID);
			if(childTransactions == null || childTransactions.isEmpty()){
				return;
			}else{
				nextElementsToDepthIncrease += childTransactions.size();
				if (--elementsToDepthIncrease == 0) {
					if (++currentDepth > BFS_MAX_DEPT){ 
						return;
					}
					elementsToDepthIncrease = nextElementsToDepthIncrease;
					nextElementsToDepthIncrease = 0;
				}
				for(Transaction t: childTransactions){
					if(! visitedMap.containsKey(t.getTo().getUserId())){
						
						if(! openQueue.contains(t.getTo().getUserId())){
							//add or update cache_id if cache_depth is greater than current depth
							//currently updating cache_id only from source
							String cache_id = builCacheID(from, t.getTo());
							if( !cache.containsKey(cache_id)){
								cache.put(cache_id, currentDepth);
							} else if(cache.containsKey(cache_id) && cache.get(cache_id) > currentDepth){
								cache.put(cache_id, currentDepth);
							}
							openQueue.add(t.getTo().getUserId());
						}
						
					}
				}
			}
		}
	}


	private List<Transaction> getDirectTransactions(String fromID) {
		return graph.containsKey(fromID) ? graph.get(fromID) : null;
	}
}
