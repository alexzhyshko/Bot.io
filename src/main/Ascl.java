package main;

import application.context.annotation.Async;
import application.context.annotation.AsyncMethod;
import application.context.annotation.Component;

@Async
@Component
public class Ascl {

	@AsyncMethod
	public void async1() {

		while(true) {
			System.out.println("async1 running");
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		
	}

	@AsyncMethod
	public void async2() {
		while(true) {
			System.out.println("async2 running");
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

}
