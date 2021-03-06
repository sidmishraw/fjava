/*
 * Copyright (c) 2017. You are free to use the code for non-commercial and
 * non-academic
 * purposes. To be frank just use it for fun.
 */

package io.github.sidmishraw.fjava.core;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Created by sidmishraw on 6/1/17.
 * <p>
 * v 1.0.0
 * <p>
 * <p>
 * FJava core Variable.
 * <p>
 * The Variable instance is the variable for FJava.
 * <p>
 * It has its own thread of execution.
 * <p>
 * FJava doesn't allow iteration, assignment(allows initialization just once)
 * and
 * mutable datastructures. It does allow messageQueues.
 * <p>
 * In this toy implementation of Variable for FJava, we have 4 operations on the
 * variable.
 * <p>
 * <b>GET<b/> - will return the value of the state Variable currently is in.
 * <p>
 * <b>SET<b/> - will set the value/ update the state of the Variable, like
 * what assignment with(=) operator did in Java.
 * <p>
 * <b>PRINT<b/> - will print the current state of the Variable to the Standard
 * Output (Console).
 * <p>
 * <b>DELETE<b/> - will stop Variable's thread of execution /delete the
 * Variable.
 * <p>
 */
public class Variable<T> implements Runnable {
	
	/**
	 * Opcode is the operations permitted on the Variable
	 * <p>
	 * I think it would be better to have the core logic of the operation as an
	 * lambda
	 * that can be executed inside the control loop instead of the switching over
	 * the
	 * opcode
	 * but this is just a toy method so not going to go that deep just yet.
	 */
	private enum Opcode {
		
		GET, SET, PRINT, DELETE;
	}
	
	/**
	 * A wrapper used for preventing blocking of the get message queue.
	 *
	 * @param <T>
	 */
	private class WrapperObject {
		
		private T value;
		
		/**
		 * Initialization is allowed and is not viewed as assignment
		 *
		 * @param value
		 */
		public WrapperObject(T value) {
			
			this.value = value;
		}
		
		public T getValue() {
			
			return value;
		}
	}
	
	/**
	 * The Message is the message that is used by the variable to execute the
	 * operations.
	 */
	private static class Message<T> {
		
		private Opcode	opcode;
		private T				value;
		
		public Message(Opcode opcode, T value) {
			
			// assuming initialization is allowed just once
			// initialization is allowed just once
			// no setters allowed, value class
			this.opcode = opcode;
			this.value = value;
		}
		
		// gets the value of the Operand object
		// this is null in case of GET and PRINT opcodes
		public T value() {
			
			return this.value;
		}
		
		// gets the opcode of the Operand object
		public Opcode opcode() {
			
			return this.opcode;
		}
	}
	
	/**
	 * The message queue that holds the incoming operations on the variable
	 */
	private BlockingQueue<Message<T>>			msgQueue		= new LinkedBlockingQueue<>();
	
	/**
	 * The queue used to get the result out of the variable's controlLoop
	 */
	private BlockingQueue<WrapperObject>	getterQueue	= new LinkedBlockingQueue<>();
	
	/**
	 * Every variable needs to have an initial value
	 *
	 * @param initialValue
	 */
	public Variable(T initialValue) {
		
		Thread myThread = new Thread(this);
		myThread.start();
		
		// System.out.println("Thread: " + myThread.getId());
		
		this.set(initialValue);
	}
	
	@Override
	public void run() {
		
		// starts the control loop
		// initially it starts out with a null state, but this is populated by the
		// constructor when it calls the set() after starting the thread.
		controlLoop(null);
		
		// System.out.println("Variable is dead T_T");
	}
	
	/**
	 * The `set` message to set the provided value into the Variable instance
	 *
	 * @param newValue
	 */
	public void set(T newValue) {
		
		try {
			
			// sets the msg for putting value into the control loop
			this.msgQueue.put(new Message<>(Opcode.SET, newValue));
		} catch (InterruptedException e) {
			
			e.printStackTrace();
		}
	}
	
	/**
	 * Gets the value of the variable instance from the controlLoop
	 *
	 * @return the value of the variable
	 */
	public T get() {
		
		try {
			
			// using a message queue to fetch the value from the control loop
			// since can't return out of the control loop for a get
			// sets the msg for getting the value from the control loop
			this.msgQueue.put(new Message<>(Opcode.GET, null));
			
			// going to block till it gets the value from the controlLoop
			// it will get the wrapped object because message queue will not take null
			// as input
			return getterQueue.take().getValue();
		} catch (Exception e) {
			
			e.printStackTrace();
		}
		
		return null;
	}
	
	/**
	 * Prints the value of the variable to the stdout.
	 */
	public void print() {
		
		try {
			
			this.msgQueue.put(new Message<>(Opcode.PRINT, null));
		} catch (Exception e) {
			
			e.printStackTrace();
		}
	}
	
	/**
	 * Deletes the variable instance. i.e. stops the instance's thread of
	 * execution
	 */
	public void delete() {
		
		try {
			
			this.msgQueue.put(new Message<>(Opcode.DELETE, null));
		} catch (InterruptedException e) {
			
			e.printStackTrace();
		}
	}
	
	/**
	 * The controlLoop is a tail recursive function that holds the state of the
	 * variable
	 * <p>
	 * It doesn't keep executing all the time, it blocks till there is a message
	 * in the
	 * message queue from which it can know what operation needs to be carried out
	 * on the
	 * Variable instance.
	 *
	 * @param value
	 *          The value or state of the variable
	 */
	private void controlLoop(T value) {
		
		try {
			
			// this is not an assignment, it is initialization
			// since tmp was not defined prior to this
			Message<T> tmp = this.msgQueue.take();
			
			switch (tmp.opcode()) {
				
				case PRINT: {
					
					// print out the value
					System.out.println(value);
					controlLoop(value);
					break;
				}
				
				case GET: {
					
					// using another message queue for fetching the value
					// from the controlLoop. This is because, the controlLoop
					// cannot break(return for a get())
					if (null != value) {
						
						this.getterQueue.put(new WrapperObject(value));
					} else {
						
						this.getterQueue.put(new WrapperObject(null));
					}
					
					controlLoop(value);
					
					break;
				}
				
				case SET: {
					
					// update the state of the controlLoop
					controlLoop((T) tmp.value());
					
					break;
				}
				
				case DELETE: {
					
					// kills the thread of the Variable instance
					Thread.yield();
					return;
				}
			}
			
		} catch (Exception e) {
			
			e.printStackTrace();
		}
	}
}
