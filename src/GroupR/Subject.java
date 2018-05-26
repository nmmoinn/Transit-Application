package GroupR;

import java.util.Collection;

/**
 * Subject interface that notifies observers of any updates
 * @author goreckinj, hueblergw, jinq
 * @version 1.0
 * @created 05-Oct-2017 09:39:32
 */
public interface Subject {

	/**
	 * Adds an observer. Return true if successfully added
	 * 
	 * @param o
	 */
	boolean addObserver(Observer o);

	/**
	 * Deletes an observer. Returns the deleted observer
	 * 
	 * @param o
	 */
	Observer deleteObservers(Observer o);

	/**
	 * Notifies observers. Returns true if notified successfully
	 */
	void notifyObservers();

}