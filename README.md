# `Bot.io`
[![MIT License](http://img.shields.io/badge/license-MIT-blue.svg?style=flat)](LICENSE)
<img src="https://img.shields.io/travis/schollz/croc.svg?style=flat-square" alt="Build
Status">

### Find the lates version for your favourite build tool [here](https://search.maven.org/artifact/io.github.alexzhyshko/BotIO)

##### It's a simple Telegram Bot framework for Java to make your life easier

  - *Route an update to a specific class and method*
  - *Use built-in Dependency Injection for comfortable component usage*
  - *Easily scale your app only by adding one class and couple annotations*
  - *Use a built-in boilerplate class for Bot that has all you need by default*


## Tech

* **Java Core** - widespread programming language mainly used for enterprise projects
* **TelegramBots lib** - library for interaction with Telegram API ([link](https://github.com/rubenlagus/TelegramBots/wiki/Getting-Started), huge thanks to [**rubenlagus**](https://github.com/rubenlagus))

## Usage

### Annonations
* `@Configuration` - to mark your custom config class
* `@Component` - to include a class to an Application Context
* `@Inject` - to inject an object to a field
* `@Async` - use this annotation on the type to mark that a type contains async code. Use on method to specify which method should be ran asynchronously
* `@Case` - use this annotation on the type to mark that a type contains case mapping. Use on method to specify which method should be used for mapping. In parentheses specify `caseNumber` to map(default = 0)

### Startup
For startup use this line in your main method:
```
    Application.start();
```

### UserService
* Define a class with any name(but logically - UserService)
* Add `@Component` and `@UserServiceMarker` so the context can find your custom User Service
* Create a method `int getUserState(int)` to let router use this method for routing
```
@Component
@UserServiceMarker
public class UserService {
	
	UserRepository repository;
	
	public int getUserState(int userid) {
		repository.getStateByUserId(userid);
	}
	
}
```

### Configuring
* Create an `application.properties` in `src/main/java`
* Add three properties:
    * rootScanDirectory - directory in which to scan classes(for maven projects mostly is `/src/main/java/`)
    * bot.token
    * bot.username

### Router

* Use `@Configuration` annotation and extend `RouterConfiguratorAdapter` to write your custom router config. Use `add(caseNumber, methodName, class)` to add a route to a class
* Create a `class` class and define a method with name `methodName` inside , also you need to specify an Update argument for this method
* After this, any update will be routed to your method depending on case in UserService

##### Or
  
* Create any class in your project
* Annotate it with `@Component` and `@Case`
* Add any method and annotate it with `@Case` and add `caseNumber` parameter in parantheses. 
All mapping will be performed automatically

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

## [Example](https://github.com/alexzhyshko/Bot.io.examples/tree/master)
