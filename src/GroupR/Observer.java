package GroupR;

import java.util.Collection;
import java.util.Observable;

/**
 * Observer interface that observes subjects for any data changes
 * @author goreckinj, hueblergw, jinq
 * @version 1.0
 * @created 05-Oct-2017 09:39:31
 */
public interface Observer {

	/**
	 * Updates the observers display based on the data it observer
	 */
	void update();
	
}