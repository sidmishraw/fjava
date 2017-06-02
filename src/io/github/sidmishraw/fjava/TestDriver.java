package io.github.sidmishraw.fjava;

import io.github.sidmishraw.fjava.core.Variable;

/**
 * Created by sidmishraw on 6/1/17.
 * <p>
 * This is the test driver for FJava, just to test out the language features
 */
public class TestDriver {

	public static void main(String[] args) {

		Variable<String> variable = new Variable<>("Hello");

		variable.print();

		System.out.println(String.format("The value of the variable: %s", variable.get()));

		variable.set("bye");

		variable.print();

		System.out.println(String.format("The value of the variable: %s", variable.get()));

		variable.delete();

		System.out.println("Done testing");
	}
}
