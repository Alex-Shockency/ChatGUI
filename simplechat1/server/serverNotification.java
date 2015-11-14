package server;

import java.io.Serializable;

public class serverNotification implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -9117710161361379673L;
	
	String message;
	
	public serverNotification(String message){
		this.message=message;
	}

	public String getMessage(){
		return message;
	}
}
