package com.wallet.process;


import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.wallet.parse.common.impl.FraudDetection;
import com.wallet.transaction.Transaction;
import com.wallet.user.User;


public class Graph implements FraudDetection{
	
	protected Map<String, Integer> cache = new HashMap<>();
	protected Map<String, List<Transaction>> graph = new HashMap<>();
	private List<Transaction> transactions = null;
	
	public void addEntryinGraph(Transaction transaction){
		
		if(graph.get(transaction.getFrom()) == null || graph.get(transaction.getFrom()).isEmpty()){
			transactions = new LinkedList<>();	
		}else{
			transactions = graph.get(transaction.getFrom());
		}
		transactions.add(transaction);
		graph.put(transaction.getFrom().getUserId(), transactions);
		
		//add <id1-id2,0> to cacheMap where id1 <= id2
		cache.put(builCacheID(transaction.getFrom(), transaction.getTo()), 0);
	}
	protected String builCacheID(User from, User to){
		StringBuilder builCacheID = new StringBuilder();
		if(isToIDLessThanEqualToFromID(from, to)){
			return builCacheID
					.append(to.getUserId())
					.append(CACHE_ID_DELIMIMTER)
					.append(from.getUserId()).toString();
		}else{
			return builCacheID
					.append(from.getUserId())
					.append(CACHE_ID_DELIMIMTER)
					.append(to.getUserId()).toString();
		}
	}
	private boolean isToIDLessThanEqualToFromID(User from, User to){
		Long idFrom = Long.parseLong(from.getUserId());
		Long idTo = Long.parseLong(to.getUserId());
		return idTo <= idFrom ? true : false;
	}
	
	
	
}
