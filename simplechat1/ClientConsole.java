// This file contains material supporting section 3.7 of the textbook:
// "Object Oriented Software Engineering" and is issued under the open-source
// license found at www.lloseng.com 

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import client.ChatClient;
import common.ChatIF;

/**
 * This class constructs the UI for a chat client.  It implements the
 * chat interface in order to activate the display() method.
 * Warning: Some of the code here is cloned in ServerConsole 
 *
 * @author Fran&ccedil;ois B&eacute;langer
 * @author Dr Timothy C. Lethbridge  
 * @author Dr Robert Lagani&egrave;re
 * @version July 2000
 */
public class ClientConsole implements ChatIF 
{
  //Class variables *************************************************
  
  /**
   * The default port to connect on.
   */
  final public static int DEFAULT_PORT = 5555;
  
  //Instance variables **********************************************
  
  /**
   * The instance of the client that created this ConsoleChat.
   */
  ChatClient client;
  String loginID;
  
  //Constructors ****************************************************

  /**
   * Constructs an instance of the ClientConsole UI.
   *
   * @param host The host to connect to.
   * @param port The port to connect on.
   */
  public ClientConsole(String login, String host, int port) 
  {
    try 
    {
      client= new ChatClient(login, host, port, this);
    } 
    catch(IOException exception) 
    {
      System.out.println("Error: Can't setup connection!"
                + " Terminating client.");
      System.exit(1);
    }
  }

  
  //Instance methods ************************************************
  
  /**
   * This method waits for input from the console.  Once it is 
   * received, it sends it to the client's message handler.
   */
  public void accept() 
  {
    try
    {
      BufferedReader fromConsole = 
        new BufferedReader(new InputStreamReader(System.in));
      String message;
      while (true) 
      {
        message = fromConsole.readLine();
        client.handleMessageFromClientUI(message);
      }
    } 
    catch (Exception ex) 
    {
      System.out.println
        ("Unexpected error while reading from console!");
    }
  }

  /**
   * This method overrides the method in the ChatIF interface.  It
   * displays a message onto the screen.
   *
   * @param message The string to be displayed.
   */
  public void display(String message) 
  {
    System.out.println(message);
  }


  
  //Class methods ***************************************************
  
  /**
   * This method is responsible for the creation of the Client UI.
   *
   * @param args[0] The host to connect to.
   */
  public static void main(String[] args) 
  {
    String host = "";
    String id = "";
    int port = 0;  //The port number
	if (args.length==2){
	    try
	    {
	      id = args[0];
	      host="localhost";
	      port = Integer.parseInt(args[1]); //Get port from command line
	    }
	    catch(Throwable t)
	    {
	      host = args[1];
	      port = DEFAULT_PORT; //Set port to 5555
	    }
	    
    }else if(args.length > 2){
        try
        {
          id = args[0].toString();
          host = args[1].toString();
          port = Integer.parseInt(args[2]);
        }
        catch(Exception e)
        {
          port = DEFAULT_PORT;
          host = "localhost";
        }
    }else if(args.length == 1){
	    id = args[0];
	    host = "localhost";
	    port = DEFAULT_PORT;
    }else{
    	System.out.println("ERROR - No login ID specified.  Connection aborted.");
    	System.exit(1);
    }	
	
    ClientConsole chat= new ClientConsole(id, host, port);
    chat.accept();  //Wait for console data
  }
}
//End of ConsoleChat class
