// This file contains material supporting section 3.7 of the textbook:
// "Object Oriented Software Engineering" and is issued under the open-source
// license found at www.lloseng.com 

package client;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

import common.ChatIF;
import ocsf.client.AbstractClient;

/**
 * This class overrides some of the methods defined in the abstract superclass
 * in order to give more functionality to the client.
 *
 * @author Dr Timothy C. Lethbridge
 * @author Dr Robert Lagani&egrave;
 * @author Fran&ccedil;ois B&eacute;langer
 * @version July 2000
 */
public class ChatClient extends AbstractClient {
	// Instance variables **********************************************

	/**
	 * The interface type variable. It allows the implementation of the display
	 * method in the client.
	 */
	ChatIF clientUI;
	private String id;
	private String Password=null;
	private ArrayList<String> blockList = new ArrayList<String>();
	private String[] currentUsers;
	private boolean ispasswordSet = false;

	// Constructors ****************************************************

	/**
	 * Constructs an instance of the chat client.
	 *
	 * @param host
	 *            The server to connect to.
	 * @param port
	 *            The port number to connect on.
	 * @param clientUI
	 *            The interface type variable.
	 */

	public ChatClient(String login, String host, int port, ChatIF clientUI) throws IOException {
		super(host, port); // Call the superclass constructor
		this.clientUI = clientUI;
		id = login;
		try {
			openConnection();
			sendToServer("#login " + login);
			handleCommand("#enterpassword");
		} catch (IOException e) {
			System.out.println("Cannot open connection.  Awaiting command.");
		}
	}

	// Instance methods ************************************************

	/**
	 * This method handles all data that comes in from the server.
	 *
	 * @param msg
	 *            The message from the server.
	 */
	public void handleMessageFromServer(Object msg) {
		if (msg instanceof String[]) {
			currentUsers = (String[]) msg;
		} else {
			String message = msg.toString();
			if (message.contains(">")) {
				if (!blockList.contains(message.substring(0, message.indexOf('>')))) {
					if (blockList.contains("server")) {
						if (!message.substring(0, message.indexOf('>')).equals("SERVER MSG")) {
							clientUI.display(message);
						}
					} else
						clientUI.display(message);
				}
			} else if (message.startsWith("#password")) {
				Password = message.substring(message.indexOf(" ") + 1, message.length());
			}
			 else
				clientUI.display(message);
		}
	}

	/**
	 * This method handles all data coming from the UI
	 *
	 * @param message
	 *            The message from the UI.
	 */
	public void handleMessageFromClientUI(String message) {
		try {
			if (message.length() != 0) {
				if (message.charAt(0) == '#') {
					handleCommand(message);
					String[] blockArray = blockList.toArray(new String[blockList.size()]);
					if (!message.equals("#logoff")) {
						sendToServer(blockArray);
					}
				} else {
					sendToServer(message);
				}
			}
		} catch (IOException e) {
			clientUI.display("Could not send message to server.  Terminating client.");
			quit();
		}
	}

	/**
	 * This method terminates the client.
	 */
	public void quit() {
		try {
			closeConnection();
		} catch (IOException e) {
		}
		System.exit(0);
	}

	@Override
	protected void connectionClosed() {
		System.out.println("Client's connection has been closed.");
	}

	@Override
	protected void connectionException(Exception exception) {
		System.out.println("Abnormal termination of connection.");
	}

	/**
	 * performs action based on command returns -1 for invalid command
	 * 
	 * @param command
	 * @return
	 */
	private void handleCommand(String command) throws IOException {
		// parse string
		// check #
		// check command
		if (command.charAt(0) == '#') {
			// parse command
			int endOfCommand = command.length();
			String argument = "";
			if (command.contains(" ")) {
				endOfCommand = command.indexOf(' ');
				argument = command.substring(command.indexOf(' ') + 1);
			}
			String commandType = command.substring(1, endOfCommand);
			switch (commandType) {
			case "quit":
				System.exit(1);
				break;
			case "logoff":
				sendToServer("#logoff");
				closeConnection();
				break;
			case "sethost":
				if (isConnected()) {
					clientUI.display("ERROR - client is already connected to a server");
					break;
				}
				setHost(argument);
				if (argument.length() == 0) {
					clientUI.display("ERROR - no argument provided");
				} else
					clientUI.display("Host set to: " + argument);
				break;
			case "setport":
				if (isConnected()) {
					clientUI.display("ERROR - client is already connected to a server");
					break;
				}
				try {
					setPort(Integer.parseInt(argument));
					clientUI.display("Port set to: " + argument);
				} catch (Exception e) {
					clientUI.display("ERROR - invalid port # format");
				}
				break;
			case "login":
				if (isConnected()) {
					clientUI.display("ERROR - client is already connected to a server");
				} else {
					openConnection();
					sendToServer("#login " + id);
					handleCommand("#enterpassword");
				}
				break;
			case "gethost":
				clientUI.display("HOST NAME: " + getHost());
				break;
			case "getport":
				clientUI.display("PORT: " + Integer.toString(getPort()));
				break;
			case "block":
				if (!argument.isEmpty()) {
					String name = argument;
					if (!blockList.contains(name)) {
						if (name.equals(id)) {
							clientUI.display("You cannot block the sending of messages to yourself.");
						} else if (!Arrays.asList(currentUsers).contains(name)) {
							clientUI.display("User " + name + " does not exist.");
						} else
							blockList.add(name);
					} else {
						clientUI.display("Messages from " + name + " are already blocked");
					}
				}
				break;
			case "unblock":
				if (!argument.isEmpty()) {
					String name = argument;
					if (blockList.contains(name)) {
						blockList.remove(name);
					}
				} else {
					blockList.clear();
				}
				break;
			case "whoiblock":
				if (blockList.size() == 0) {
					clientUI.display("No blocking is in effect.");
				} else {
					for (int i = 0; i < blockList.size(); i++) {
						clientUI.display("Messages from " + blockList.get(i) + " are blocked");
					}
				}
				break;
			case "whoblocksme":
				sendToServer("#whoblocksme");
				break;
			case "enterpassword":
				Scanner sc = new Scanner(System.in);
				if(ispasswordSet)
				{
					clientUI.display("ERROR - Password has already been set.");
					break;
				}
				if (Password.isEmpty()) {
					handleCommand("#setpassword");
					sendToServer("#passwordSuccess "+Password);
					ispasswordSet = true;
					break;
				} 
				System.out.println("Login: " + id);
				System.out.print("Password: ");
				String temp = sc.nextLine();
				if (!temp.equals(Password)) {
					clientUI.display("ERROR - invalid logn information");
					handleCommand("#enterpassword");
				} else {
					sendToServer("#passwordSuccess "+Password);
					ispasswordSet = true;
				}
				break;
			case "setpassword":
				if(ispasswordSet)
				{
					clientUI.display("ERROR - Password has already been set.");
					break;
				}
				Scanner sc2 = new Scanner(System.in);
				System.out.print("Set Password: ");
				Password = sc2.nextLine();
				clientUI.display("Password set to: " + Password);
				ispasswordSet = true;
				break;
			default:
				clientUI.display("ERROR - invalid command");
			}
		}
	}
}// End of ChatClient class
