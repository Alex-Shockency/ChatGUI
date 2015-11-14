// This file contains material supporting section 3.7 of the textbook:
// "Object Oriented Software Engineering" and is issued under the open-source
// license found at www.lloseng.com 

package client;

import java.io.IOException;
import java.util.Scanner;

import ocsf.client.AbstractClient;
import ocsf.client.ObservableClient;
import server.serverNotification;
import common.ChatIF;

/**
 * This class overrides some of the methods defined in the abstract superclass
 * in order to give more functionality to the client.
 *
 * @author Dr Timothy C. Lethbridge
 * @author Dr Robert Lagani&egrave;
 * @author Fran&ccedil;ois B&eacute;langer
 * @version July 2000
 */
public class ChatClient extends ObservableClient {
	// Instance variables **********************************************

	/**
	 * The interface type variable. It allows the implementation of the display
	 * method in the client.
	 */
	ChatIF clientUI;
	private String id = "";
//	private String password = "";

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

	public ChatClient(String login,String password, String host, int port, ChatIF clientUI)
			throws IOException {
		super(host, port); // Call the superclass constructor
		this.clientUI = clientUI;
		id = login;
		try {
			openConnection();
			sendToServer("#login " + login + " " + password);
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
		if (msg instanceof serverNotification) {
			serverNotification sn = (serverNotification) msg;
			setChanged();
			notifyObservers(sn);
		} else {
			String message = msg.toString();
			clientUI.display(message);
		}
	}

	/**
	 * This method handles all data coming from the UI
	 *
	 * @param message
	 *            The message from the UI.
	 */
	public void handleMessageFromClientUI(Object message) {
		try {
			if (((String)message).length() != 0) {
				String messageString = (String) message;
				if (messageString.charAt(0) == '#') {
					handleCommand(messageString);
				} else {
					sendToServer(message);
				}
			}
		} catch (IOException e) {
			clientUI.display("Could not send message to server.  Terminating client.");
			quit();
		}
	}

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
				quit();
				break;
			case "logoff":
				sendToServer(command);
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
					//passwordCheck();
				}
				break;
			case "gethost":
				clientUI.display("HOST NAME: " + getHost());
				break;
			case "getport":
				clientUI.display("PORT: " + Integer.toString(getPort()));
				break;
			case "block":
			case "unblock":
			case "whoiblock":
			case "whoblocksme":
			case "pm":
			case "status":
			case "notavailable":
			case "available":
				sendToServer(command);
				break;
			case "createChannel":
				if (argument.length() == 0) {
					clientUI.display("ERROR - no argument provided");
				} else {
					sendToServer(command);
				}
				break;
			case "joinChannel":
				if (argument.length() == 0) {
					clientUI.display("ERROR - no argument provided");
				} else
					sendToServer(command);
				break;
			case "leaveChannel":
				sendToServer(command);
				break;
			case "channelStatus":
				sendToServer(command);
				break;
			case "monitor":
				if (argument.length() == 0) {
					clientUI.display("ERROR - no argument provided");
				} else if (argument.equals(this.id)) {
					System.out.println("ERROR - you can not monitor yourself.");
				} else {
					sendToServer(command);
				}
				break;
			default:
				clientUI.display("ERROR - invalid command");
			}
		}
	}

//	@SuppressWarnings("resource")
//	private boolean passwordCheck() {
//		Scanner sc = new Scanner(System.in);
//		if (password.isEmpty()) {
//			setPassword();
//		}
//		System.out.println("Login: " + id);
//		System.out.print("Password: ");
//		String temp = sc.nextLine();
//		if (temp.equals(password)) {
//			try {
//				sendToServer(true);
//			} catch (IOException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//			return true;
//		} else {
//			System.out.println("ERROR - Invalid login information try again.");
//			passwordCheck();
//			return false;
//		}
//	}
//
//	@SuppressWarnings("resource")
//	private void setPassword() {
//		Scanner sc = new Scanner(System.in);
//		System.out.println("Login: " + id);
//		System.out.print("Set Password: ");
//		password = sc.nextLine();
//		clientUI.display("Password set to: " + password);
//		try {
//			sendToServer("#setPassword " + password);
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//	}

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
}
// End of ChatClient class
