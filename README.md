# `Bot.io`
[![MIT License](https://img.shields.io/badge/License-MIT-Blue.svg)](https://shields.io/)
[![Generic badge](https://img.shields.io/badge/Build-Passing-Green.svg)](https://shields.io/)

## [Usage example](https://github.com/alexzhyshko/Bot.io.examples/tree/master)

### Find the latest version [here](https://search.maven.org/artifact/io.github.alexzhyshko/BotIO)



##### It's a simple Telegram Bot framework for Java to make your life easier

  - *Route an update to a specific class and method*
  - *Use built-in Dependency Injection for comfortable component usage*
  - *Easily scale your app only by adding one class and couple annotations*
  - *Use a built-in boilerplate class for Bot that has all you need by default*


## Tech

* **Java** - widespread programming language mainly used for enterprise projects
* **TelegramBots lib** - library for interaction with Telegram API ([link](https://github.com/rubenlagus/TelegramBots/wiki/Getting-Started), huge thanks to [**rubenlagus**](https://github.com/rubenlagus))

## Usage

### Annonations
* `@Configuration` - to mark your custom config class
* `@Component` - to include a class to an Application Context
* `@Inject` - to inject an object to a field
* `@Async` - use this annotation on the type to mark that it contains async code. Use on method to specify which method should be ran asynchronously
* `@Case` - use this annotation on the type to mark that it contains case or callback mapping. Use on method to specify which method should be used for mapping. In parentheses specify `caseNumber` to map(default = 0)
* `@Callback` - use this annotation on the method to mark that it contains callback mapping. In parentheses specify `caseNumber` to map(default = 0)
* `@UserServiceMarker` - marker annotation to mark user-created User Service class, which is to be used by core

### Startup
For startup use this line in your main method:
```
    Application.start();
```

### UserService
* Define a class with any name(but logically - UserService)
* Add `@Component` and `@UserServiceMarker` so the context can find your custom User Service
* Create a method `int getUserState(int)` to let router use this method for routing
* * Create a method `int setUserState(int, int)` to let router use this method for routing
```
@Component
@UserServiceMarker
public class UserService {
	
	UserRepository repository;
	
	public int getUserState(int userid) {
		repository.getStateByUserId(userid);
	}
	
	public int getUserState(int userid, int state) {
		repository.setStateByUserId(userid, state);
	}
	
}
```

### Configuring
* Create an `application.properties` in `src/main/java`
* Add three properties:
    * rootPackage - root package, where to start file scan
    * bot.token
    * bot.username

### Router

* Use `@Configuration` annotation and extend `RouterConfiguratorAdapter` to write your custom router config. Use `add(caseNumber, methodName, class)` to add a route to a class
* Create a `class` class and define a method with name `methodName` inside , also you need to specify an Update argument for this method
* After this, any update will be routed to your method depending on case in UserService

##### Or
  
* Create any class in your project
* Annotate it with `@Component` and `@Case`
* Add any method and annotate it with `@Case`(for routing regular mesasge) or `@Callback`(for routing callback query) and add `caseNumber` parameter in parantheses. 
All mapping will be performed automatically

### Routing to classes

* Inject a `RouterManager` class as a field and use its methods to change user's case according to given class name.
Can be completely replaced just by using UserService's `setUserState` method

### Session
* Using `@Inject` annotation inject a SessionManager field to your case class
* To set a property, use `setProperty(key, value)`
* To get property, use `getProperty(key, targetClass)`

### Sending messages
* Use `@Inject` over a `SendMessage` field in your case class
* Using built-in methods fill needed info for message: `chatId` and `text`.
* (Optional) Add buttons using built-in methods
* Invoke `sendMessage` on your MessageSender instance to send message.
* After this, all attributes are restored to default and object can be reused.
Example:
```
    int userid = update.getMessage().getFrom().getId();
    sender.setChatId(userid);
    sender.setText("case 0 works");
    sender.sendMessage();
```

### Editing messages
* Use `@Inject` over an `MessageEditor` field in your case class
* Using built-in methods fill needed info for message: `chatId`, `text` and `messageId`.
* (Optional) Add inline buttons using built-in methods
* Invoke `sendMessage` on your MessageEditor instance to send message.
* After this, all attributes are restored to default and object can be reused.
```
    String data = update.getCallbackQuery().getData();
	editor.setChatId(update.getCallbackQuery().getFrom().getId());
	editor.setMessageId(update.getCallbackQuery().getMessage().getMessageId());
	editor.setText("New text");
	editor.setInlineButtons(Arrays.asList("New inline"), Arrays.asList("new_inline"));
	editor.sendMessage();
```
### Sending documents
* Use `@Inject` over a `DocumentSender` field in your case class
* Using built-in methods fill needed info for message: `chatId` and `file`.
* Invoke `sendDocument` on your DocumentSender instance to send document.
* After this, all attributes are restored to default and object can be reused.
```
    File file = new File("path to file");
	InputFile inputFile = new InputFile(file, file.getName());
	docSender.setChatId(userid);
	docSender.setFile(inputFile);
	docSender.sendDocument();
```

### Loading documents
* Use `@Inject` over a `DocumentLoader` field in your case class
* Using `loadDocument` and pass a documentId as parameter to get a `java.io.File` result
```
    int docId = someId;
	File result = docLoader.loadDocument(docId);
```

### Framework is can be built to jar using shading
