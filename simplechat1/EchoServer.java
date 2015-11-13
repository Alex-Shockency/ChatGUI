// This file contains material supporting section 3.7 of the textbook:
// "Object Oriented Software Engineering" and is issued under the open-source
// license found at www.lloseng.com 

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.Scanner;

import ocsf.server.ConnectionToClient;
import ocsf.server.ObservableServer;

/**
 * This class overrides some of the methods in the abstract superclass in order
 * to give more functionality to the server.
 *
 * @author Dr Timothy C. Lethbridge
 * @author Dr Robert Lagani&egrave;re
 * @author Fran&ccedil;ois B&eacute;langer
 * @author Paul Holden
 * @version July 2000
 */
public class EchoServer extends ObservableServer {
	// Class variables *************************************************

	/**
	 * The default port to listen on.
	 */
	final public static int DEFAULT_PORT = 5555;
	private HashMap<String, String> userPasswords = new HashMap<String, String>();
	private HashMap<String, ArrayList<String>> blockLists = new HashMap<String, ArrayList<String>>();
	private HashMap<String, ArrayList<String>> serverChannels = new HashMap<String, ArrayList<String>>();
	ArrayList<String> validUsers = new ArrayList<String>();

	// Constructors ****************************************************

	/**
	 * Constructs an instance of the echo server.
	 *
	 * @param port
	 *            The port number to connect on.
	 */
	public EchoServer(int port) {
		super(port);
		ArrayList<String> tempUsers = new ArrayList<String>();
		serverChannels.put("public", tempUsers);
	}

	// Instance methods ************************************************

	/**
	 * This method handles any messages received from the client.
	 *
	 * @param msg
	 *            The message received from the client.
	 * @param client
	 *            The connection from which the message originated.
	 */
	public void handleMessageFromClient(Object msg, ConnectionToClient client) {
		client.setInfo("latestActive", System.currentTimeMillis());
		if (!client.getInfo("status").equals("Unavailable")) {
			client.setInfo("status", "Online");
		}
			if (msg.equals(true)) {
				// if the login is sucessful
				serverChannels.get("public").add(
						(String) client.getInfo("loginId"));
				client.setInfo("status", "Online");
				client.setInfo("currentChannel", "public");
				System.out.println(client.getInfo("loginId")
						+ " has logged on.");
				sendToAllClients(client, client.getInfo("loginId")
						+ " has logged on.");
				return;
			}
			String tempMsg = msg.toString();
			int msgCount = (int) client.getInfo("Message Count");

			if (tempMsg.startsWith("#login")) {
				loginId(msgCount, tempMsg, client);
			} else if (tempMsg.charAt(0) == '#') {
				handleClientCommand(client, tempMsg);
			} else {
				System.out.println("Message received: " + msg.toString() + " from " + client.getInfo("loginId") + " " + client);
				sendToAllClients(client, client.getInfo("loginId") + "> " + tempMsg);
			}// end else
			client.setInfo("Message Count", msgCount + 1);
		}
//	}

	/**
	 * This method overrides the one in the superclass. Called when the server
	 * starts listening for connections.
	 */
	protected void serverStarted() {
		System.out.println("Server listening for connections on port "
				+ getPort());
	}

	/**
	 * This method overrides the one in the superclass. Called when the server
	 * stops listening for connections.
	 */
	protected void serverStopped() {
		System.out.println("Server has stopped listening for connections.");
	}

	@Override
	protected void clientConnected(ConnectionToClient client) {
		client.setInfo("Message Count", 0);
		client.setInfo("status", "Unavailable");
		System.out.println("A new client is attempting to connect to the server.");
	}

	@Override
	synchronized protected void clientDisconnected(ConnectionToClient client) {
		System.out.println(client.getInfo("loginId") + " has disconnected.");
	}

	// Class methods ***************************************************
	private void sendToAllClients(ConnectionToClient client, String message) {
		for (Thread currentUserThread : getClientConnections()) {
			//client info for current thread
			ConnectionToClient currentClient=((ConnectionToClient) currentUserThread);
			if (!currentClient.getInfo("status").equals("Unavailable")
					&&!blockLists.get(currentClient.getInfo("loginId")).contains(client.getInfo("loginId"))
					&&client.getInfo("currentChannel").equals(currentClient.getInfo("currentChannel"))) {
					sendToClient((ConnectionToClient) currentUserThread,message);
			}
		}
	}

	private void sendToClient(ConnectionToClient client, String message) {
		try {
			for (Thread currentUserThread : getClientConnections()) {
				//client info for current thread
				ConnectionToClient currentClient=((ConnectionToClient) currentUserThread);
				if (currentClient.getInfo("whoimonitor") != null) {
					//if someone is monitoring somebody forward the message from that user.
					if (currentClient.getInfo("whoimonitor").equals(client.getInfo("loginId"))) {
						//if they are not blocked then forward.
						if (!blockLists.get(currentClient.getInfo("loginId")).contains(client.getInfo("loginId"))) {
								currentClient.sendToClient("(monitored from "+ client.getInfo("loginId")+ ") "+ message );
							}
					}
				}
			}// end for
			client.sendToClient(message);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void loginId(int msgCount, String message, ConnectionToClient client) {
		if (message.startsWith("#login")) {
			if (msgCount == 0) {
				client.setInfo("loginId",message.substring(7, message.length()));
				//Initialize blockList of client to empty list.
				ArrayList<String> tempList=new ArrayList<String>();
				blockLists.put((String) client.getInfo("loginId"),tempList);
				passwordCheck(client);
				if (!validUsers.contains(client.getInfo("loginId"))) {
					validUsers.add((String) client.getInfo("loginId"));
				}
			}

		} else if (msgCount == 0 && !message.startsWith("#login")) {
			try {
				client.sendToClient("ERROR - No loginId specified.  Connection aborted.");
				client.close();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
	}

	private void passwordCheck(ConnectionToClient client) {
		if (validUsers.contains(client.getInfo("loginId"))) {
			for (Entry<String, String> entry : userPasswords.entrySet()) {
				String key = entry.getKey();
				Object value = entry.getValue();
				if (key.equals(client.getInfo("loginId"))) {
					sendToClient(client, "#password " + value);
				}
			}
		} else {
			sendToClient(client, "Error - User is not valid please set password.");
		}
	}

	private void privateMessage(ConnectionToClient client, String clientToPM, String message) {
		for (Thread currentUserThread : getClientConnections()) {
			//client info for current thread
			ConnectionToClient currentClient = ((ConnectionToClient) currentUserThread);
			if (currentClient.getInfo("loginId").equals(clientToPM)) {
				if(!currentClient.getInfo("status").equals("Unavailable")&&!blockLists.get(currentClient.getInfo("loginId")).contains(client.getInfo("loginId"))){
					if (clientToPM.equals(client.getInfo("loginId"))) {
					sendToClient(client,"ERROR - You cannot private message yourself.");
					} else if (!((ConnectionToClient) currentUserThread).getInfo("status").equals("Unavailable")) {
					sendToClient(currentClient, "(PRIVATE MSG) "+client.getInfo("loginId") + "> " + message);
					} else
					sendToClient(client, "ERROR - " + currentClient.getInfo("loginId") + " is not available.");
				}
			}
		}
			
	}

	private void status(String user, ConnectionToClient client) {

		if (validUsers.indexOf(user) == -1) {
			sendToClient(client, "User " + user + " does not exist.");
		} else {
			// get actual status on user in argument
			// (1)Online - active (2) Idle - active but no message/commands in
			// the last 5 minutes
			// (3)unavailable - logged in but not willing to chat (4) offline -
			// not logged in

			for (Thread currentUserThread : getClientConnections()) {
				Object tempLogin = ((ConnectionToClient) currentUserThread).getInfo("loginId");
				if (tempLogin.equals(user)) {
					try {

						if (((ConnectionToClient) currentUserThread).getInfo(
								"status").equals("Unavailable")) {
							client.sendToClient("User "
									+ ((ConnectionToClient) currentUserThread)
											.getInfo("loginId")
									+ " is "
									+ ((ConnectionToClient) currentUserThread)
											.getInfo("status"));
							return;
						}

						long lastActiveMoment = (long) ((ConnectionToClient) currentUserThread)
								.getInfo("latestActive");
						long timeSinceLastActivity = System.currentTimeMillis()
								- lastActiveMoment;

						if (timeSinceLastActivity >= 300000) {
							// 300k ms is 5 minutes

							((ConnectionToClient) currentUserThread).setInfo(
									"status", "Idle");

							client.sendToClient("User "
									+ ((ConnectionToClient) currentUserThread)
											.getInfo("loginId")
									+ " is "
									+ ((ConnectionToClient) currentUserThread)
											.getInfo("status"));
						} else {

							((ConnectionToClient) currentUserThread).setInfo(
									"status", "Online");
							client.sendToClient("User "
									+ ((ConnectionToClient) currentUserThread)
											.getInfo("loginId")
									+ " is "
									+ ((ConnectionToClient) currentUserThread)
											.getInfo("status"));
						}
						return;
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}// end for

			sendToClient(client, "User " + user + " is Offline");
			// not offline or unavailable
			// calculate if they've been inactive for 5 minutes+

			// if (current time - their last time they did something) > 300k (5
			// minutes)
			// then theyre idle

		}// end else
	}// end status()

	private void channelStatus(String channel, ConnectionToClient client) {
		if (serverChannels.containsKey(channel)) {
			if (client.getInfo("currentChannel").equals(channel)|| channel.equals("public")) {
				for (Thread currentUserThread : getClientConnections()) {
					ConnectionToClient currentClient = ((ConnectionToClient) currentUserThread);
					if (channel.equals(currentClient.getInfo("currentChannel"))) {
						sendToClient(client,"User " + currentClient.getInfo("loginId")+ " is "+ currentClient.getInfo("status"));
					}
				}
			} else {
				sendToClient(client,"You are not authorized to get information about channel "+ channel + ".");
			}
		} else
			sendToClient(client, "ERROR - Channel does not exist.");
	}

	private void handleClientCommand(ConnectionToClient client, String command) {
		// parse command
		int endOfCommand = command.length();
		String argument = "";
		if (command.contains(" ")) {
			endOfCommand = command.indexOf(' ');
			argument = command.substring(command.indexOf(' ') + 1);
		}
		String commandType = command.substring(1, endOfCommand);
		switch (commandType) {
		case "block":
			if(validUsers.contains(argument)){
				if(!argument.equals(client.getInfo("loginId"))){
				blockLists.get(client.getInfo("loginId")).add(argument);
				sendToClient(client,"Messages from "+argument+" are now being blocked.");
				}
				else
					sendToClient(client,"ERROR - You cannot block yourself.");
			}
			else
				sendToClient(client,"ERROR - User does not exist.");
			break;
		case "unblock":
			if(argument.isEmpty())
			{
				blockLists.get(client.getInfo("loginId")).clear();
				sendToClient(client,"All users have been unblocked.");
			}
			else
				blockLists.get(client.getInfo("loginId")).remove(argument);
				sendToClient(client,argument+" has been unblocked.");
			break;
		case "whoiblock":
			ArrayList <String> clientBlockList=blockLists.get(client.getInfo("loginId"));
			for(int i=0;i<clientBlockList.size();i++)
			{
				sendToClient(client,clientBlockList.get(i));
			}
			break;
		case "whoblocksme":
			ArrayList<String> whoblocksMe = new ArrayList<String>();
			for (Entry<String, ArrayList<String>> entry : blockLists.entrySet()) {
				String key = entry.getKey();
				ArrayList<String> blockList = entry.getValue();
				if (!key.equals(client.getInfo("loginId"))) {
					if(blockList.contains(client.getInfo("loginId"))){
						whoblocksMe.add(key);
					}
				}
			}
			if (whoblocksMe.isEmpty()) {
				sendToClient(client, "No one is currently blocking you");
			} else {
				for (int i = 0; i < whoblocksMe.size(); i++) {
					sendToClient(client, "Messages to " + whoblocksMe.get(i)
							+ " are being blocked.");
				}
			}
			break;
		case "logoff":
			try {
				client.setInfo("status", "Offline");
				client.close();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			break;
		case "setPassword":
			userPasswords.put((String) client.getInfo("loginId"), argument);
			sendToClient(client, "#password " + argument);
			break;
		case "pm":
			if (argument.length() == 0 || !argument.contains(" ")) {
				sendToClient(client, "ERROR - Usage:#pm <User> <message>");
			} else {
				String clientToPm = argument.substring(0, argument.indexOf(" "));
				String privateMessage = argument.substring(argument.indexOf(' ') + 1);
				privateMessage(client, clientToPm, privateMessage);
			}
			break;
		case "status":
			status(argument, client);
			break;
		case "channelStatus":
			channelStatus(argument, client);
			break;
		case "notavailable":

			client.setInfo("status", "Unavailable");

			sendToClient(client, "Your status has been set to Unavailable.");

			break;
		case "available":
			client.setInfo("status", "Online");
			sendToClient(client, "Your status has been set to Online.");
			break;
		case "createChannel":
			ArrayList<String> tempUsers = new ArrayList<String>();
			if (serverChannels.containsKey(argument)) {
				sendToClient(client, "ERROR - Channel is already created.");
			} else {
				sendToAllClients(client,client.getInfo("loginId") + " has disconnected from "+ client.getInfo("currentChannel")+ " channel");
				//Intialize to empty list
				serverChannels.put(argument, tempUsers);
				serverChannels.get(argument).add((String) client.getInfo("loginId"));
				serverChannels.get("public").remove(client.getInfo("loginId"));
				client.setInfo("currentChannel", argument);
				sendToClient(client, "Channel " + argument + " has been created.");
				sendToAllClients(client, client.getInfo("loginId")+ " has logged in to " + argument + " channel");
			}
			break;
		case "joinChannel":
			System.out.println();
			if (client.getInfo("status").equals("Unavailable")) {
				sendToClient(client,"ERROR - Cannot join channel, "+ client.getInfo("loginId") + " is Unavailable");
				break;
			} else {
				for (Entry<String, ArrayList<String>> entry : serverChannels
						.entrySet()) {
					String key = entry.getKey();
					if (serverChannels.containsKey(argument)) {
						if (key.equals(argument)) {
							if (!serverChannels.get(key).contains(client.getInfo("loginId"))) {
								serverChannels.get(key).add((String) client.getInfo("loginId"));
								serverChannels.get("public").remove(client.getInfo("loginId"));
								client.setInfo("currentChannel", argument);
								sendToAllClients(client,client.getInfo("loginId")+ " has logged into "+ argument + " channel");
							} else
								sendToClient(client,"ERROR - already connected to channel.");
						}
					} else {
						sendToClient(client, "ERROR - Channel does not exist.");
						break;
					}
				}
			}
			break;
		case "leaveChannel":
			if (client.getInfo("currentChannel").equals("public")) {
				sendToClient(client, "Cannot disconnect from public channel");
			} else {
				sendToAllClients(
						client,
						client.getInfo("loginId")
								+ " has disconnected from channel "
								+ client.getInfo("currentChannel"));
				client.setInfo("currentChannel", "public");
			}
			break;
		case "monitor":
			client.setInfo("whoimonitor", argument);
			sendToClient(client, "You are now monitoring " + argument + ".");
			break;
		default:

			sendToClient(client, "ERROR - invalid command");

		}
	}// end handleClientCommand()

	public void readvalidUsers(File file) {
		try {
			Scanner reader = new Scanner(file);
			String line;
			while (reader.hasNextLine()) {
				line = reader.nextLine();
				String[] arrayLine = line.split(",");
				userPasswords.put(arrayLine[0], arrayLine[1]);
				validUsers.add(arrayLine[0]);
			}
			reader.close();
		} catch (FileNotFoundException e) {
			System.out.println("ERROR - File not found");
		}
	}

	/**
	 * This method is responsible for the creation of the server instance (there
	 * is no UI in this phase).
	 *
	 * @param args
	 *            [0] The port number to listen on. Defaults to 5555 if no
	 *            argument is entered.
	 */
	public static void main(String[] args) {
		int port = 0; // Port to listen on

		try {
			port = Integer.parseInt(args[0]); // Get port from command line
		} catch (Throwable t) {
			port = DEFAULT_PORT; // Set port to 5555
		}

		EchoServer sv = new EchoServer(port);

		try {
			sv.listen(); // Start listening for connections
		} catch (Exception ex) {
			System.out.println("ERROR - Could not listen for clients!");
		}
	}
}
// End of EchoServer class
