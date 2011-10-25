package perseus.util;

import java.io.OutputStream;
import java.io.PrintStream;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.apache.commons.lang.time.StopWatch;

/**
 * The intent of this class is to provide a convenient way to record timing
 * information for a series of events, each of which has a description
 * associated with it. To use it, obtain a new Timekeeper via eiither one of
 * the constructors or through the static getTimekeeper() method; call
 * the start() method to begin timing, then call the record() method with an
 * appropriate descriptive string each time you want too record the time.
 * When you're finished, call stop() to stop timing and then output() to
 * print out the results.
 */
public class Timekeeper {

	private static final String SEPARATOR =
		"---------------" + System.getProperty("line.separator");

	private TimekeeperTask allActions;
	private TimekeeperTask defaultTask;

	private TimekeeperTask selectedTask;

	private Map<String,TimekeeperTask> tasks =
		new TreeMap<String,TimekeeperTask>();

	private StopWatch watch;
	private String name;
	private OutputStream out;

	private boolean running = false;

	private long previousTime;

	private static Map<Object,Timekeeper> keepers =
		new HashMap<Object,Timekeeper>();
	private static NumberFormat format = NumberFormat.getInstance();

	public static Timekeeper getTimekeeper(Object identifier) {
		Timekeeper keeper;

		if (!keepers.containsKey(identifier)) {
			keeper = new Timekeeper(identifier.toString(), System.err);
			keepers.put(identifier, keeper);
		} else {
			keeper = keepers.get(identifier);
		}

		return keeper;
	}

	public Timekeeper() {
		this(null);
	}

	/**
	 * Constructor class that allows one to associate a name with
	 * an instance of Timekeeper
	 *
	 * @param name The name of the timekeeper
	 */
	public Timekeeper(String name) {
		this(name, System.err);
	}

	/** 
	 * Constructor class that allows one to associate a name with
	 * an instance of Timekeeper as well as a PrintStream to 
	 * print all timing information to.
	 *
	 * @param name the name of the timekeeper
	 * @param out the printstream to display timing information to
	 */
	public Timekeeper(String name, PrintStream out) {
		this.name = name;
		this.out = out;

		allActions = new TimekeeperTask("all");
		defaultTask = new TimekeeperTask("default");

		selectedTask = defaultTask;

		watch = new StopWatch();
	}

	/**
	 * Starts the timer
	 */
	public void start() {

		if (running) {
			throw new IllegalStateException(
					"Cannot start an already-running timekeeper");
		}
		watch.start();

		running = true;
		previousTime = watch.getTime();
	}

	public void selectTask(String groupName) {
		if (tasks.containsKey(groupName)) {
			selectedTask = (TimekeeperTask) tasks.get(groupName);
		} else {
			selectedTask = new TimekeeperTask(groupName);
			tasks.put(groupName, selectedTask);
		}
	}

	/**
	 * Use the default Task as the TimekeeperTask to execute
	 */
	public void selectDefaultTask() {
		selectedTask = defaultTask;
	}

	public void recordInTask(String groupName, String message) {

		TimekeeperTask actions;

		TimedAction timedAction = createAction(message);

		if (!tasks.containsKey(groupName)) {
			actions = new TimekeeperTask(groupName);
			tasks.put(groupName, actions);
		} else {
			actions = (TimekeeperTask) tasks.get(groupName);
		}

		actions.add(timedAction);
	}

	/**
	 * If the timer is not running start it up.  Then create TimedAction 
	 * with the given message associated with it
	 *
	 * @param message
	 */
	public void record(String message) {
		if (!running) {
			start();
		}

		TimedAction action = createAction(message);

		selectedTask.add(action);
	}

	/**
	 * Create a TimedAction and associate a message with it
	 *
	 * @param message the message to associate with the action
	 * @return TimedAction
	 */
	public TimedAction createAction(String message) {

		/*
	 if (!running) {
	 throw new IllegalStateException(
	 "Cannot record from a stopped Timekeeper");
	 }
		 */

		long time = watch.getTime();

		long duration = time - previousTime;
		previousTime = time;

		TimedAction timedAction = new TimedAction(message, duration);
		allActions.add(timedAction);

		return timedAction;
	}

	/**
	 * Stop running the Timekeeper
	 */
	public void stop() {

		if (!running) {
			throw new IllegalStateException(
					"Cannot stop a stopped Timekepeer");
		}

		running = false;
		watch.stop();
	}

	/**
	 * Display results from running the Timekeeper serialized as 
	 * a String.  A time breakdown for each action is provided.
	 *
	 * @return A report describing the results of running the timekeeper
	 */
	public String getResults() {
		StringBuilder buffer = new StringBuilder();

		buffer.append(SEPARATOR);        
		buffer.append(getHeader());
		buffer.append(SEPARATOR);

		buffer.append(allActions);

		buffer.append(SEPARATOR);

		return buffer.toString();
	}

	public String getResults(String actionType) {
		return getResults(actionType, true);
	}

	public String getResults(String actionType, boolean displayTotal) {

		StringBuilder buffer = new StringBuilder();

		if (tasks.containsKey(actionType)) {
			TimekeeperTask group = (TimekeeperTask) tasks.get(actionType);

			buffer.append(SEPARATOR);

			return group.toString(displayTotal);
		} else {
			return actionType + ": no actions recorded";
		}
	}

	/**
	 * Serialize the Timekeeper class as a String
	 *
	 * @return a string describing the timekeeper class
	 */
	public String getSummary() {

		StringBuilder buffer = new StringBuilder();

		String nl = System.getProperty("line.separator");

		buffer.append(SEPARATOR)
		.append(getHeader())
		.append(nl)
		.append(SEPARATOR)
		.append(defaultTask.getSummary());


		buffer.append(defaultTask.getSummary()).append(nl);

		for (TimekeeperTask group : tasks.values()) {
			buffer.append(group.getSummary()).append(nl);
		}

		buffer.append(SEPARATOR)
		.append(allActions.getSummary())
		.append(nl)
		.append(SEPARATOR);

		return buffer.toString();
	}


	private String getHeader() {
		return String.format("Timekeeper %s %s", name, new Date());
	}

	/**
	 * Reset the Timekeeper
	 */
	public void reset() {
		if (running) {
			running = false;
		}

		watch.reset();

		allActions = new TimekeeperTask();
		defaultTask = new TimekeeperTask();

		selectedTask = defaultTask;

		tasks.clear();
	}

	/**
	 * Retrieve the OutputStream where timing information
	 * will be displayed
	 *
	 * @return the OutputStream
	 */
	public OutputStream getOutputStream() {
		return out;
	}

	/**
	 * Set the OutputStream where timing information will
	 * be displayed
	 *
	 * @param out the outputstream to display
	 */
	public void setOutputStream(OutputStream out) {
		this.out = out;
	}

	/**
	 * Provides a means to group a series of Actions and see
	 * how long they take to execute.
	 */
	private class TimekeeperTask implements Comparable<TimekeeperTask> {

		private String name;
		private List<TimedAction> actions = new ArrayList<TimedAction>();

		private long totalDuration;

		/**
		 * Class Constructor
		 */
		public TimekeeperTask() {
			this("__default__");
		}

		/**
		 * Class Constructor.  Allows a name to be associated
		 * with the TimekeeperTask created
		 *
		 * @param name the name of the task
		 */
		public TimekeeperTask(String name) {
			this.name = name;

			totalDuration = 0;
		}

		/**
		 * Add an action to the TimekeeperTask instance
		 *
		 * @param action is the TimedAction to add to the task
		 */
		public void add(TimedAction action) {
			actions.add(action);

			totalDuration += action.getDuration();
		}

		/**
		 * Get the name associated with this Timekeeper Task
		 */
		public String getName() {
			return name;
		}

		/**
		 * Set the name of this Timekeeper Task.
		 */
		public void setName(String name) {
			this.name = name;
		}

		/**
		 * Return how many actions there are
		 *
		 * @return the number of actions
		 */
		public int size() {
			return actions.size();
		}

		/**
		 * Return a summary of the TimekeeperTask as a String.
		 * The summary includes the Task name, the number of actions associated with
		 * the task, and the total ms it takes to exectute the task.
		 *
		 * @return the summary of the timekeeper task.
		 */
		public String getSummary() {

			return String.format("Task: %s %d items, %dms total",
					name, actions.size(), totalDuration);
		}

		/**
		 * Serialize the TimekeeperTask as a String.  This includes everything in GetSummary, but
		 * also includes a time breakdown for individual actions within the task
		 *
		 * @return a string 
		 */
		public String toString() {
			return toString(true);
		}

		/**
		 * Serialize the Timekeeper task as a String.  If displayTotal is true, include
		 * a time breakdown for individual actions within the task.  Otherwise, just
		 * display the time it takes for the task as a whole to execute
		 *
		 * @param displayTotal true or false
		 * @return the TimekeeperTask serialized as a string
		 */
		public String toString(boolean displayTotal) {

			StringBuilder buffer = new StringBuilder();

			buffer.append(getSummary());
			buffer.append('\n');

			int count = 0;
			for (TimedAction action : actions) {
				buffer.append(count++ + "\t" + action);
				buffer.append('\n');
			}

			return buffer.toString();
		}

		public int compareTo(TimekeeperTask task) {
			return getName().compareTo(task.getName());
		}
	}

	/**
	 * Defines an action to be timed
	 */
	private class TimedAction {

		long duration;
		String message;

		/**
		 * Class Constructor.
		 *
		 * @param msg 
		 * @param dur The duration of the action
		 */
		public TimedAction(String msg, long dur) {
			message = msg;
			duration = dur;
		}

		/**
		 * Method to retrieve the duration of the action
		 *
		 * @return The duration of the timed event
		 */
		public long getDuration() {
			return duration;
		}

		/**
		 * Method to retrieve the message associated with the action
		 *
		 * @return The message for this timed event
		 */
		public String getMessage() {
			return message;
		}

		/**
		 * Serialize this timed event as a String
		 *
		 * @return this timed event serialized as a string.
		 */
		public String toString() {
			return format.format(duration) + "ms\t" + message;
		}
	}
}
