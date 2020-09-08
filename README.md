# `Bot.io`
[![MIT License](http://img.shields.io/badge/license-MIT-blue.svg?style=flat)](LICENSE)
<img src="https://img.shields.io/badge/version-v1.1.0-brightgreen.svg?style=flat-square" alt="Version">
<img src="https://img.shields.io/travis/schollz/croc.svg?style=flat-square" alt="Build
Status">

### The project is available as a Maven dependency:
```
<dependency>
	<groupId>io.github.alexzhyshko</groupId>
	<artifactId>BotIO</artifactId>
	<version>1.1.0</version>
</dependency>
```

##### It's a simple Telegram Bot framework for Java to make your life easier

  - *Route an update to a specific class and method*
  - *Use built-in Dependency Injection for comfortable component usage*
  - *Easily scale your app only by adding one class and couple annotations*
  - *Use a built-in boilerplate class for Bot that has all you need by default*

###### For startup use `Application.start()` in your main method
###### Configure `bot.token`, `bot.username` and `rootScanDirectory` in `application.properties` file
###### For appropriate work you have to define your own `UserService` class with a `getUserState()` method inside and annotate this class with `@UserServiceMarker`, as well as `@Component` annotation for the framework to use this class for routing

## Tech

* **Java Core** - widespread programming language mainly used for enterprise projects
* **TelegramBots lib** - library for interaction with Telegram API ([link](https://github.com/rubenlagus/TelegramBots/wiki/Getting-Started), huge thanks to [**rubenlagus**](https://github.com/rubenlagus))

## Usage

##### Annonations
* `@Configuration` - to mark your custom config class
* `@Component` - to include a class to an Application Context
* `@Inject` - to inject an object to a field
* `@Async` - use this annotation on the type to mark that a type contains async code. Use on method to specify which method should be ran asynchronously
* `@Case` - use this annotation on the type to mark that a type contains case mapping. Use on method to specify which method should be used for mapping. In parentheses specify `caseNumber` to map(default = 0)
##### Router
* Use `@Configuration` annotation and extend `RouterConfiguratorAdapter` to write your custom router config. Use `add(caseNumber, methodName, class)` to add a route to a class.
* Create a `class` class and define a method with name `methodName` inside , also you need to specify an Update argument for this method.
* After this, any update will be routed to your method depending on case in UserService([examples](src/main/cases))

  ##### Or
  
* Create any class in your project
* Annotate it with `@Component` and `@Case`
* Add any method and annotate it with `@Case` and add `caseNumber` parameter in parantheses. 
All mapping will be performed automatically
