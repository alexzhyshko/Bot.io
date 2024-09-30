# `BotIO`
[![Apache-2.0 License](https://img.shields.io/badge/License-Apache_2.0-Blue.svg)](LICENSE)
[![Generic badge](https://img.shields.io/badge/Build-Passing-Green.svg)](https://central.sonatype.com/artifact/io.github.alexzhyshko/BotIO/2.0.1)

## Maven Central
https://central.sonatype.com/artifact/io.github.alexzhyshko/BotIO/2.0.1

## Dependencies

* [Spring Boot, Spring Boot Autoconfigure](https://github.com/spring-projects/spring-boot) Apache-2.0 License
* [Project Lombok](https://projectlombok.org/) MIT License
* [Telegrambots-longpolling, telegrambots-client, telegrambots-springboot-longpolling-starter](https://github.com/rubenlagus/TelegramBots) MIT License
#### (All corresponding rights belong to their respective owners)

## Documentation

First of all, since this library relies on [TelegramBots](https://github.com/rubenlagus/TelegramBots) project, I recommend to check basics of it before moving on.


### Bot Token configuration

There are 3 ways to configure a bot token:
* Add an environment variable `BOT_TOKEN`;
* Add a configuration property `botio.botToken`;
* Create custom token provider class. For it just create a new bean which implements `BotTokenProvider`.

### Create routes and transitions

Overall, flow structure in this library consists of so called "routes". A route is basically a state, to which a user will be directed upon each action.
Each route has its own "view" - a message, set of buttons etc; and set of methods - actions which are to be executed on a certain user action.

As a first step, it is required to set up an Entrypoint route - the route which is equivalent to index.html in web apps - it is the landing route for our bot application.
To do so, we need to:
* create a class with a `@Component`;
* add `@Entrypoint` and `@Message` - to declare an entrypoint and that current route will be used to handle message type of updates;
* implement `Route` interface (empty by design).

Example below:

```
@Component
@Entrypoint
@Message
public class LandingRoute implements Route {
```

### View Initialization

Every route, except the Entrypoint, can be configured to have a certain message to be displayed whenever the user is redirected to this route.
View initializer can be created by declaring a method with `@ViewInitializer` within the route class. This method receives `SendMessage.SendMessageBuilder builder` as a parameter.
This is a message builder ready to be customized with your own text, buttons, actions etc. Just modify it using builder methods and that's it (not recommended to call .build()).

```
@ViewInitializer
public void initView(SendMessage.SendMessageBuilder builder) {
   builder
    .text("Main Menu")
}
```

### Message/Callback handlers

Currently, bot supports handling two basic types of updates - messages and callbacks, and to declare that the class is handling a `Message` or a `Callback`, respective annotations should be put over the class.
Moreover, to create the logic itself, it is needed to specify inside the route class the handler methods with corresponding `MessageMapping` and `CallbackMapping`. Each method gets an `UpdateWrapper` instance as an input and should return a ResponseEntity:

```
@MessageMapping
public ResponseEntity helloWorld(UpdateWrapper wrapper, I18NLabelsWrapper i18NLabelsWrapper) {
    SendMessage sendMessage = SendMessage.builder()
            .chatId(wrapper.getChatId())
            .text("Hello World!")
            .build();
						
    return ResponseEntity.builder()
            .response(sendMessage)
            .build();
}
```
(similarly Callback handlers can be created)

### Transitions to other routes

Routing logic is implemented out-of-the-box and it is enough to add a `.nextRoute(<NextRouteClass>.class)` into the `ResponseEntity` builder.
Library will automatically invoke the view initializer and transition current state to the specified route.


```
    return ResponseEntity.builder()
            .response(sendMessage)
            .nextRoute(MenuRoute.class)
            .build();
}
```

In OOTB state management is using in-memory strategy. Future plans include to add Redis and DB support, until then, feel free to code your own implementation and override OOTB beans.

### Filtering

Out-of-the-box there is an available functinality for using filters.
To create a filter it is needed:
* Create a filter class and put `@Component` over it;
* Implement FilterAdapter interface and its method;
* Add config property `botio.filters` with filter bean names to specify in which order filters have to be executed;
* Use filterChain.doFilter() to trigger further flow, you can add any logic before anf after - it will be executed in the order of stack;
* Use UpdateWrapper to get, check or modify any request-related information;
* Use TelegramClient to execute any actions from within the filter;
* To abort the filter flow - just throw a FilterException;

```
@Component
public class BlacklistCheckFilter implements FilterAdapter {
    @Override
    public void filter(FilterChain filterChain, UpdateWrapper wrapper, TelegramClient telegramClient) throws FilterException {
        //do something before request

        filterChain.doFilter();

        throw new FilterException("some error");
    }
}
```

For better understanding of the place of filters in the general flow, at the end I will include a flowchart.

### Session management

For now, OOTB uses in-memory session management. Plans include to add Redis support.

Session management is done as silently as possible, it is persisted in between requests, unless the server is restarted.
To get and modify the session, in the handler method just use `updateWrapper.getSession()` method.

It is possible to get, set, delete and invalidate user session.

### Internationalization

This library packs I18N functionality out of the box. By default it is disabled, however it can be enabled by adding a `botio.i18nEnabled=true` to the confifuration properties.

I18N is initialized under the hood and provides an easy access to labels without significant effort.
Overall, to use the I18N you need:
* Enable `botio.i18nEnabled`;
* In view initializer method or handler method add second input parameter `I18NLabelsWrapper i18NLabelsWrapper`. This will be automatically constructed and injected for you;
* Create message properties files under resources folder, default file is `messages_en.properties`, but you can add your own files for each locale you need;
* In code, to get the label, call `i18NLabelsWrapper.getLabel(key)` (or `i18NLabelsWrapper.getLabel(key, args)` in case you have any custom parameters).

### Flowchart of processing steps:
![image](https://github.com/user-attachments/assets/2a251dae-ddbd-471e-be46-82aa7e397368)


