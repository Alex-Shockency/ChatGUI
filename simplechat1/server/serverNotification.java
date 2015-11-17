package server;

import java.io.Serializable;

public class serverNotification implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -9117710161361379673L;
	
	String message;
	String info;
	
	public serverNotification(String message,String info){
		this.message=message;
		this.info=info;
	}

	public String getMessage(){
		return message;
	}
	public String getInfo(){
		return info;
	}
}
