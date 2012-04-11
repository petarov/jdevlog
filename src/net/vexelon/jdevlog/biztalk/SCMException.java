package net.vexelon.jdevlog.biztalk;

public class SCMException extends Exception {

	String guess;
	
	public SCMException(String message, Throwable t) {
		super(t);
		this.guess = guess;
	}
	
	public String getGuess() {
		return guess;
	}
}
