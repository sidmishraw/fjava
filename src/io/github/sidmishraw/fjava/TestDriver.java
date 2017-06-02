package io.github.sidmishraw.fjava;

import io.github.sidmishraw.fjava.core.Variable;

/**
 * Created by sidmishraw on 6/1/17.
 * <p>
 * This is the test driver for FJava, just to test out the language features
 */
public class TestDriver {

	/**
	 * @param args
	 */
	public static void main(String[] args) {

		Variable<String> variable = new Variable<>("Hello");

		variable.print();

		System.out.println(String.format("The value of the variable: %s", variable.get()));

		variable.set("bye");

		System.out.print("variable = ");

		variable.print();

		System.out.println(String.format("The value of the variable: %s", variable.get()));

		// ending the life of the variable
		variable.delete();

		// A null initialized variable
		Variable<String> nullVariable = new Variable<>(null);

		System.out.println("Fetching val");

		// the get will block the invoking thread till some other thread updates the
		System.out.println(String.format("The value of the variable: %s", nullVariable.get
				()));

		// ending the life of the variable
		nullVariable.delete();

		System.out.println("Done testing");
	}
}
