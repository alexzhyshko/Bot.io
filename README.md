# `Bot.io`
[![MIT License](https://img.shields.io/badge/License-MIT-Blue.svg)](LICENSE)
[![Generic badge](https://img.shields.io/badge/Build-Passing-Green.svg)](https://mvnrepository.com/artifact/io.github.alexzhyshko/BotIO)

# Documentation for the [latest](https://search.maven.org/artifact/io.github.alexzhyshko/BotIO) version

#### [Usage example](https://github.com/alexzhyshko/Bot.io.examples/tree/master)

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
* `@Component` - to include a class to an Application Context
* `@Filter` - to mark that a class is a filter
* `@Inject` - to inject an object to a field
* `@Async` - use this annotation on the type to mark that it contains async code. Use on method to specify which method should be ran asynchronously
* `@State` - use this annotation over a type to mark that it contains State logic, in parantheses specify stateNumber (default = 0)
* `@Case` - use this annotation over the method to specify which method should be used for mapping, in parentheses specify `message` to map(default = *)
* `@Callback` - use this annotation on the method to mark that it contains callback mapping. In parentheses specify `command` to map(default = *)
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
	
	public int setUserState(int userid, int state) {
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
  
* Create any class in your project
* Annotate it with `@Component` and `@Case`, add `caseNumber` argument to `@Case`
* Add any method and annotate it with `@Case`(for routing regular mesasge) or `@Callback`(for routing callback query) and add `message` or `command` argument respectively ro route specific message/command to metod. By default `*`, means that, if no other routes found, this route will be used. 
All mapping will be performed automatically

### Routing to classes

* Inject a `RouterManager` class as a field and use its methods to change user's case according to given class name.
Can be completely replaced just by using UserService's `setUserState` method

### Session
* Using `@Inject` annotation inject a `SessionManager` field to your case class
* There is no need to choose proper session for user, it is auto mapped by itself
* To set a property, use `setProperty(key, value)`
* To get property, use `getProperty(key, targetClass)`

### Filters
* To declare that a class is a fiter, use `@Filter` and `@Component`
* Add `order` argument(zero-based) to specify ordder of filter, if you have several
* Add `enabled` argument(default true) to disable/enable filter
* Implement core interface `FilterAdapter` and its method
* If you want to terminate query(filter didn't match), throw a `FilterException`

### Sending messages
* Use `@Inject` over a `MessageSender` field in your case class
* Using built-in methods fill needed info for message: `chatId` and `text`.
* (Optional) Add buttons using built-in methods and button objects, you can set location or contact request to button
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
* (Optional) Add inline buttons using built-in methods and inline button objects, you can set URL link to the button
* Invoke `editMessage` on your MessageEditor instance to edit message.
* After this, all attributes are restored to default and object can be reused.
```
    String data = update.getCallbackQuery().getData();
	editor.setChatId(update.getCallbackQuery().getFrom().getId());
	editor.setMessageId(update.getCallbackQuery().getMessage().getMessageId());
	editor.setText("New text");
	editor.setInlineButtons(Arrays.asList("New inline"), Arrays.asList("new_inline"));
	editor.sendMessage();
```


### Deleting messages
* Use `@Inject` over an `MessageDeleter` field in your case class
* Using built-in methods fill needed info for message: `chatId` and `messageId`.
* Invoke `deleteMessage` on your `MessageDeleter` instance to delete message.
* After this, all attributes are restored to default and object can be reused.
```
    int userid = update.getCallbackQuery().getFrom().getId();
	deleter.setChatId(userid);
	deleter.setMessageId(update.getCallbackQuery().getMessage().getMessageId());
	deleter.deleteMessage();
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

### Framework can be built to jar using shading
