
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

import ocsf.server.AbstractServer;
import ocsf.server.ConnectionToClient;

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
public class EchoServer extends AbstractServer {
	// Class variables *************************************************

	/**
	 * The default port to listen on.
	 */
	final public static int DEFAULT_PORT = 5555;
	HashMap<String, String[]> blockLists = new HashMap<String, String[]>();
	ArrayList<String> whoblocksMe = new ArrayList<String>();
	ArrayList<String> validUsers = new ArrayList<String>();

	// Constructors ****************************************************

	/**
	 * Constructs an instance of the echo server.
	 *
	 * @param port
	 *            The port number to connect on.
	 */
	public EchoServer(File file,int port) {
		super(port);
		readValidList(file);
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
		if (msg instanceof String[]) {
			String[] tempArray = ((String[]) msg);
			blockLists.put((String) client.getInfo("Login Id"), tempArray);
		} else {
			String tempMsg = msg.toString();
			int msgCount = (int) client.getInfo("Message Count");
			loginId(msgCount, tempMsg, client);
			System.out.println(
					"Message received: " + msg.toString() + " from " + client.getInfo("Login Id") + " " + client);
			if (msgCount == 0) {
				this.sendToAllClients(client.getInfo("Login Id") + " has logged on.");
			}
			if (msgCount != 0) {
				if (tempMsg.trim().equals("#logoff")) {
					try {
						client.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				} else if (tempMsg.trim().equals("#whoblocksme")) {
					whoblocksMe(client);
				} else {
					if (client.getInfo("Login Id").equals("server")) {
						this.sendToAllClients("SERVER MSG" + "> " + tempMsg);
					} else {
						this.sendToAllClients(client.getInfo("Login Id") + "> " + tempMsg);
					}
				}
			}
			client.setInfo("Message Count", msgCount + 1);
		}
	}

	/**
	 * This method overrides the one in the superclass. Called when the server
	 * starts listening for connections.
	 */
	protected void serverStarted() {
		System.out.println("Server listening for connections on port " + getPort());
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
		System.out.println("A new client is attempting to connect to the server.");
	}

	@Override
	synchronized protected void clientDisconnected(ConnectionToClient client) {
		System.out.println(client.getInfo("Login Id") + " has disconnected.");
	}

	// Class methods ***************************************************

	/**
	 * This method receives the login message and checks if it is valid and is
	 * the first message sent. Called when the client attempts to login.
	 * 
	 * @param msgCount
	 *            -Keeps track of the number of messages sent by user used to
	 *            check if login command is issued later.
	 * @param message
	 *            -message received by server from client.
	 * @param client
	 *            - used to set client info
	 */
	private void loginId(int msgCount, String message, ConnectionToClient client) {
		if (message.startsWith("#login")) {
			if (msgCount == 0) {
				client.setInfo("Login Id", message.substring(7, message.length()));
				// adds users that have logged in to a list of valid users
				// converts arraylist to string array and sends to client
				// ------------------------------------------------------
				validUsers.add((String) client.getInfo("Login Id"));
				String[] tempArray = new String[validUsers.size()];
				tempArray = validUsers.toArray(tempArray);
				try {
					client.sendToClient(tempArray);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				// -------------------------------------------------------
			}

		} else if (msgCount == 0 && !message.startsWith("#login")) {
			try {
				client.sendToClient("ERROR - No login ID specified.  Connection aborted.");
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			try {
				client.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * This method iterates through the blockLists Hashmap and adds the users
	 * that are currently blocking the client to the whoblockme list.
	 * 
	 * @param client
	 *            - used to send messages to the client.
	 */
	private void whoblocksMe(ConnectionToClient client) {
		for (Entry<String, String[]> entry : blockLists.entrySet()) {
			String key = entry.getKey();
			Object value = entry.getValue();
			String[] tempValues = (String[]) value;
			if (!key.equals(client.getInfo("Login Id"))) {
				for (int i = 0; i < tempValues.length; i++) {
					if (tempValues[i].equals(client.getInfo("Login Id"))) {
						if (!whoblocksMe.contains(key))
							whoblocksMe.add(key);
					}
				}
			}
		}
		for (int i = 0; i < whoblocksMe.size(); i++) {
			try {
				client.sendToClient("Messages to " + whoblocksMe.get(i) + " are being blocked");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	private void readValidList(File file) {
		try {
			Scanner reader = new Scanner(file);
			String line;
			while (reader.hasNextLine()) {
				line = reader.nextLine().trim();
				validUsers.add(line);
			}
			reader.close();
		} catch (FileNotFoundException e) {
			System.out.println("Error - File not found");
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
		File file = null;
		try {
			try{
			file = new File(args[0]);
			}
			catch(Exception e){
				System.out.println("Error - No validUsers file specified.");
			}
			port = Integer.parseInt(args[1]); // Get port from command line
		} catch (Throwable t) {
			port = DEFAULT_PORT; // Set port to 5555
		}

		EchoServer sv = new EchoServer(file,port);
		try {
			sv.listen(); // Start listening for connections
		} catch (Exception ex) {
			System.out.println("ERROR - Could not listen for clients!");
		}
	}
}
// End of EchoServer class
