package com.rahul.learn.web;

class Bar {
	public Bar(String arg1, String arg2) {
	}
}

public class Foo extends Bar {
	private int someInt;

	public Foo() {
		super(someMethod(), someStaticMethod());
		someInt = 1;
	}

	protected static String someMethod() {
		return "abc";
	}

	public static String someStaticMethod() {
		return "xyz";
	}
}