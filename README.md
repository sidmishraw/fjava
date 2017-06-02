# FJava

-----------------------------------------------------
*****************************************************

Author - Sidharth Mishra

Advisor - Dr. Jon Pearce

*****************************************************
-----------------------------------------------------

**FJava** is a functional take on Java.


To summarize it is:
 ```
    JAVA - {assignment, iteration, mutable datastructures}
 ```

An example program using **FJava** would look like:
```java

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
```

### Variable
The `variable(s)` in FJava are **active**. 

They have their own thread of control. The operations on them are simple messages with opcode and a value. 
These messages are executed inside the `controlLoop`. 

The `controlLoop` is an **infinite tail-recursive 
function** that maintains the state of the variable and also executes the operations 
asked of the variable.

For the toy implementation, we have 4 basic operations on the Variable:

* **GET** - It gets the current state value of the Variable.
* **SET** - It updates the state of the Variable. This is used instead of normal 
assignment operator to update the state.
* **DELETE** - It stops the Variable's thread of control stopping the variable
* **PRINT** - It prints the current state of the Variable to the standard output 
(console).

The operations are messages that are dumped into the message queue of the Variable when
 the corressponding operations are invoked. 
 
 For eg: When the `variable.get()` method is
  invoked on the Variable, a message containing `GET` opcode is pushed into the message
   queue. The controlLoop finds this message and executes it to give the value desired.

#### Assignment:

When I say `assignment`, I mean situations where we are modifying the state of 
the variable.

For eg:
```
String variable = "Hello";          // This is not assignment but initialization

variable = "Bye";                   // Now this is assignment since I'm trying to
                                    // modify the value of the variable that is already
                                    // initialized.
variable = variable + "and bye!";   // is also considered as assignment 
```

#### Iteration:

By `iteration` I refer to all the loop constructs in Java(`for`, `while`, `do-while` etc.)
There is no room for these loop constructs in **FJava**.

**Iteration** is acheived by `tail-recursion`.

##### What is tail-recursion?
[The accepted answer of this stackexchange question is pretty good](https://cs.stackexchange.com/questions/6230/what-is-tail-recursion)


Tail recursion is a special case of recursion where the calling function does no more computation after making a recursive call.
When we make a normal recursive call, we have to push the return address onto the call stack then jump to the called function.
When we have tail recursion we know that as soon as we return from the recursive call we're going to immediately return as well, so we can skip the entire chain of recursive functions returning and return straight to the original caller. 
That means we don't need a call stack at all for all of the recursive calls, and can implement the final call as a simple jump, which saves us space.

For eg:

```
// decrementByOne() is tail-recursive
int decrementByOne(int nbr, int times) {
	
	if (times == 0) {
		
		return nbr;
	}
	
	return decrementByOne(nbr - 1, --times); 
}

// g is not tail recursive
// It requires extra stack space to hold values for the computations and the recursion 
// chain.
int g(int x) {
	
  if (x == 1) {
  	
    return 1;
  }

  int y = g(x-1);

  return x*y;
}
```

#### Mutable Datastructures:

`Mutable data-structures` are not allowed in FJava. 
But, message queues are allowed since these are used for holding the messages that 
allow the variables to execute the operations asked of it.


#### Toy project/examples using FJava:
* [Bank account manager application]() -- Coming Soon!
