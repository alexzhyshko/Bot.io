# `Bot.io`

##### It's a simple Telegram Bot framework for Java to make your life easier

  - *Route an update to a specific class and method*
  - *Use built-in Dependency Injection for comfortable component usage*
  - *Easily scale your app only by adding one config line and one class*


## Tech

* **Java Core** - widespread programming language mainly used for enterprise projects
* **TelegramBots lib** - library for interaction with Telegram API ([link](https://github.com/rubenlagus/TelegramBots/wiki/Getting-Started), huge thanks to [**rubenlagus**](https://github.com/rubenlagus))

## Usage

##### Annonations
* `@Configuration` - to mark your custom config class
* `@Component` - to include a class to an Application Context
* `@Inject` - to inject an object to a field
* `@Async` - to mark, that this class contains async functionality
* `@AsyncMethod` - to mark a method inside an Async class, that should be executed asynchronously
##### Router
* Use `@Configuration` annotation and extend `RouterConfiguratorAdapter` to write your custom router config. Use `add(caseNumber, methodName, className)` to add a route to a class.
* Create a class with name `className` and create inside a method with name `methodName`, also you need to specify an Update argument for this method.
* After this, any update will be routed to your method depending on case in UserService([examples](src/main/cases))



