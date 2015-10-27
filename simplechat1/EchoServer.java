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
	private HashMap<String, String> userPasswords = new HashMap<String, String>();
	private HashMap<String, String[]> blockLists = new HashMap<String, String[]>();
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
			if(msg.equals(true))
			{
				System.out.println(client.getInfo("Login Id")
						+ " has logged on.");
				this.sendToAllClients(client.getInfo("Login Id")
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
				System.out.println("Message received: " + msg.toString()
						+ " from " + client.getInfo("Login Id") + " " + client);
				this.sendToAllClients(client.getInfo("Login Id") + "> "
						+ tempMsg);
			}
			client.setInfo("Message Count", msgCount + 1);
		}
	}

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
		System.out
				.println("A new client is attempting to connect to the server.");
	}

	@Override
	synchronized protected void clientDisconnected(ConnectionToClient client) {
		System.out.println(client.getInfo("Login Id") + " has disconnected.");
	}

	// Class methods ***************************************************
	private void loginId(int msgCount, String message, ConnectionToClient client) {
		if (message.startsWith("#login")) {
			if (msgCount == 0) {
				client.setInfo("Login Id",
						message.substring(7, message.length()));
				passwordCheck(client);
				if (!validUsers.contains(client.getInfo("Login Id"))) {
					validUsers.add((String) client.getInfo("Login Id"));
				}
				String[] tempArray = new String[validUsers.size()];
				tempArray = validUsers.toArray(tempArray);
				this.sendToAllClients((tempArray));
			}

		} else if (msgCount == 0 && !message.startsWith("#login")) {
			try {
				client.sendToClient("ERROR - No login ID specified.  Connection aborted.");
				client.close();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
	}
	private void passwordCheck(ConnectionToClient client) {
		if(validUsers.contains(client.getInfo("Login Id")))
		{
		for (Entry<String, String> entry : userPasswords.entrySet()) {
			String key = entry.getKey();
			Object value = entry.getValue();
			if (key.equals(client.getInfo("Login Id"))) {
				try {
					client.sendToClient("#password " + value);
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		}
		}
		else {
			try {
				client.sendToClient("Error - User is not valid please set password.");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
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
		case "whoblocksme":
			ArrayList<String> whoblocksMe = new ArrayList<String>();
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
			if (whoblocksMe.isEmpty()) {
				try {
					client.sendToClient("No one is currently blocking you");
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} else {
				for (int i = 0; i < whoblocksMe.size(); i++) {
					try {
						client.sendToClient("Messages to " + whoblocksMe.get(i)
								+ " are being blocked");
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
			break;
		case "logoff":
			try {
				client.close();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			break;
		case "setPassword":
			userPasswords.put((String) client.getInfo("Login Id"), argument);
			try {
				client.sendToClient("#password " + argument);
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			break;
		case "pm":
			if (argument.length() == 0||!argument.contains(" ")) {
				try {
					client.sendToClient("ERROR - usage:#pm <User> <message>");
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			else{
			String clientToPm=argument.substring(0,argument.indexOf(" "));
			String privateMessage=argument.substring(argument.indexOf(' ')+1);
			privateMessage(client,clientToPm,privateMessage);
			}
			break;
		default:
			try {
				client.sendToClient("ERROR - invalid command");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	private void privateMessage(ConnectionToClient client,String clientToPM,String message)
	{
		for(Thread currentUserThread:getClientConnections())
		{
			Object tempLogin=((ConnectionToClient) currentUserThread).getInfo("Login Id");
			if(tempLogin.equals(clientToPM))
			{
				try {
					((ConnectionToClient) currentUserThread).sendToClient("(PRIVATE)"+client.getInfo("Login Id")+"> "+message);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}
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
