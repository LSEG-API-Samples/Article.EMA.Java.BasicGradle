///*|----------------------------------------------------------------------------------------------------
// *|            This source code is provided under the Apache 2.0 license      	--
// *|  and is provided AS IS with no warranty or guarantee of fit for purpose.  --
// *|                See the project's LICENSE.md for details.                  					--
// *|           Copyright (C) 2019 Refinitiv. All rights reserved.            		--
///*|----------------------------------------------------------------------------------------------------

//This code is based on RTSDK Java com.refinitiv.ema.examples.training.consumer.series100.ex100_MP_Streaming example

package com.refinitiv.ema.examples.localconsumer;

import com.refinitiv.ema.access.Msg;
import com.refinitiv.ema.access.AckMsg;
import com.refinitiv.ema.access.GenericMsg;
import com.refinitiv.ema.access.RefreshMsg;
import com.refinitiv.ema.access.ReqMsg;
import com.refinitiv.ema.access.StatusMsg;
import com.refinitiv.ema.access.UpdateMsg;
import com.refinitiv.ema.access.EmaFactory;
import com.refinitiv.ema.access.OmmConsumer;
import com.refinitiv.ema.access.OmmConsumerClient;
import com.refinitiv.ema.access.OmmConsumerConfig;
import com.refinitiv.ema.access.OmmConsumerEvent;
import com.refinitiv.ema.access.OmmException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
// import ch.qos.logback.classic.LoggerContext;
// import ch.qos.logback.core.util.StatusPrinter;


class AppClient implements OmmConsumerClient
{
	Logger logger = LoggerFactory.getLogger(AppClient.class);
	

	AppClient(){
		logger.info("Starting {}", AppClient.class.getSimpleName());
	}

	public void onRefreshMsg(RefreshMsg refreshMsg, OmmConsumerEvent event)
	{
		if (refreshMsg.hasName())
			logger.info("Getting Refresh Response Message, Item Name: {}", refreshMsg.name());
		
		if (refreshMsg.hasServiceName())
			logger.info("Service Name: {}", refreshMsg.serviceName());
		
		logger.info("Item State:  {}", refreshMsg.state());
		logger.info("Refresh Response Message \n{}",refreshMsg);
		//System.out.println(refreshMsg);
	}
	
	public void onUpdateMsg(UpdateMsg updateMsg, OmmConsumerEvent event) 
	{
		if (updateMsg.hasName())
		logger.info("Getting Update Response Message, Item Name: {}", updateMsg.name());
	
		if (updateMsg.hasServiceName())
			logger.info("Service Name: {}", updateMsg.serviceName());
		
		logger.info("Update Response Message \n{}", updateMsg);
		//System.out.println(updateMsg);
	}

	public void onStatusMsg(StatusMsg statusMsg, OmmConsumerEvent event) 
	{
		logger.info("Status Response Message \n{}", statusMsg);
		//System.out.println(statusMsg);
	}

	public void onGenericMsg(GenericMsg genericMsg, OmmConsumerEvent consumerEvent){}
	public void onAckMsg(AckMsg ackMsg, OmmConsumerEvent consumerEvent){}
	public void onAllMsg(Msg msg, OmmConsumerEvent consumerEvent){}
}

public class Consumer 
{

	static String serviceName = "ELEKTRON_DD";
	static String host = "localhost";
	static String port = "14002";
	static String userName = "GRADLE_USER";
	static String itemName = "/THB=";
	static Logger logger = LoggerFactory.getLogger(Consumer.class);

	static boolean readCommandlineArgs(String[] args)
	{
		try
		{
			int argsCount = 0;

			while (argsCount < args.length)
			{
				
				if ("-username".equals(args[argsCount]))
				{
					userName = argsCount < (args.length-1) ? args[++argsCount] : null;
					++argsCount;
				}
				else if ("-service".equals(args[argsCount]))
				{
					serviceName = argsCount < (args.length-1) ? args[++argsCount] : null;
					++argsCount;
				}
				else if ("-host".equals(args[argsCount]))
				{
					host = argsCount < (args.length-1) ? args[++argsCount] : null;
					++argsCount;
				}
				else if ("-port".equals(args[argsCount]))
				{
					port = argsCount < (args.length-1) ? args[++argsCount] : null;
					++argsCount;
				}
				
				else if ("-itemName".equals(args[argsCount]))
				{
					itemName = argsCount < (args.length-1) ? args[++argsCount] : null;
					++argsCount;
				}
				
				else // unrecognized command line argument
				{
					// for (String s : args){
					// 	System.out.println(s);
					// }
					// printHelp();
					// return false;
				}
			}

		}
		catch (Exception e)
		{
			printHelp();
			return false;
		}

		return true;
	}

	static void printHelp()
	{
		System.out.println("\nOptions:\n" + "  -?\tShows this usage\n"
						   + "  -username DACS username(optional)\n"
						   + "  -service RTDS or IProvider service name(optional)\n"
						   + "  -host RTDS or IProvider hostname name or IP Address(optional)\n"
						   + "  -port RTDS or IProvider RSSL Port(optional)\n"
						   + "  -itemName Request item name (optional).\n"
						   + "\n");
	}

	public static void main(String[] args) {
		OmmConsumer consumer = null;
		
        logger.info("Starting {}", Consumer.class.getSimpleName());
		try{
			AppClient appClient = new AppClient();
			
			OmmConsumerConfig config = EmaFactory.createOmmConsumerConfig();

			if (!readCommandlineArgs(args))
				return;
			
			consumer  = EmaFactory.createOmmConsumer(config.host(String.join(":",host,port)).username(userName));
			
			ReqMsg reqMsg = EmaFactory.createReqMsg();
			
			consumer.registerClient(reqMsg.serviceName(serviceName).name(itemName), appClient);
			
			Thread.sleep(60000);			// API calls onRefreshMsg(), onUpdateMsg() and onStatusMsg()
		} 
		catch (InterruptedException | OmmException excp) {
			logger.error(excp.getMessage());
		}
		finally {
			if (consumer != null) consumer.uninitialize();
		}
	}
}


