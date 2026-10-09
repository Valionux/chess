# ♕ BYU CS 240 Chess

This project demonstrates mastery of proper software design, client/server architecture, networking using HTTP and WebSocket, database persistence, unit testing, serialization, and security.

## 10k Architecture Overview

The application implements a multiplayer chess server and a command line chess client.

[![Sequence Diagram](10k-architecture.png)](https://sequencediagram.org/index.html#initialData=C4S2BsFMAIGEAtIGckCh0AcCGAnUBjEbAO2DnBElIEZVs8RCSzYKrgAmO3AorU6AGVIOAG4jUAEyzAsAIyxIYAERnzFkdKgrFIuaKlaUa0ALQA+ISPE4AXNABWAexDFoAcywBbTcLEizS1VZBSVbbVc9HGgnADNYiN19QzZSDkCrfztHFzdPH1Q-Gwzg9TDEqJj4iuSjdmoMopF7LywAaxgvJ3FC6wCLaFLQyHCdSriEseSm6NMBurT7AFcMaWAYOSdcSRTjTka+7NaO6C6emZK1YdHI-Qma6N6ss3nU4Gpl1ZkNrZwdhfeByy9hwyBA7mIT2KAyGGhuSWi9wuc0sAI49nyMG6ElQQA)

## Modules

The application has three modules.

- **Client**: The command line program used to play a game of chess over the network.
- **Server**: The command line program that listens for network requests from the client and manages users and games.
- **Shared**: Code that is used by both the client and the server. This includes the rules of chess and tracking the state of a game.

## Starter Code

As you create your chess application you will move through specific phases of development. This starts with implementing the moves of chess and finishes with sending game moves over the network between your client and server. You will start each phase by copying course provided [starter-code](starter-code/) for that phase into the source code of the project. Do not copy a phases' starter code before you are ready to begin work on that phase.

## IntelliJ Support

Open the project directory in IntelliJ in order to develop, run, and debug your code using an IDE.

## Running the program using Java

Once you have compiled your project into an uber jar, you can execute it with the following command.

```sh
java -jar client/target/client-jar-with-dependencies.jar

♕ 240 Chess Client: chess.ChessPiece@7852e922
```

## Sequence Diagram of the chess Web API

The sequence diagram for the Web API used for the chess app can be found [here](https://sequencediagram.org/index.html?presentationMode=readOnly&shrinkToFit=true#initialData=IYYwLg9gTgBAwgGwJYFMB2YBQAHYUxIhK4YwDKKUAbpTngUSWDABLBoAmCtu+hx7ZhWqEUdPo0EwAIsDDAAgiBAoAzqswc5wAEbBVKGBx2ZM6MFACeq3ETQBzGAAYAdAE5M9qBACu2AMQALADMABwATG4gMP7I9gAWYDoIPoYASij2SKoWckgQaJiIqKQAtAB85JQ0UABcMADaAAoA8mQAKgC6MAD0PgZQADpoAN4ARP2UaMAAtihjtWMwYwA0y7jqAO7QHAtLq8soM8BICHvLAL6YwjUwFazsXJT145NQ03PnB2MbqttQu0WyzWYyOJzOQLGVzYnG4sHuN1E9SgmWyYEoAAoMlkcpQMgBHVI5ACU12qojulVk8iUKnU9XsKDAAFUBhi3h8UKTqYplGpVJSjDpagAxJCcGCsyg8mA6SwwDmzMQ6FHAADWkoGME2SDA8QVA05MGACFVHHlKAAHmiNDzafy7gjySp6lKoDyySIVI7KjdnjAFKaUMBze11egAKKWlTYAgFT23Ur3YrmeqBJzBYbjObqYCMhbLCNQbx1A1TJXGoMh+XyNXoKFmTiYO189Q+qpelD1NA+BAIBMU+4tumqWogVXot3sgY87nae1t+7GWoKDgcTXS7QD71D+et0fj4PohQ+PUY4Cn+Kz5t7keC5er9cnvUe4cOpO+51dgOXj2Inf3DCTyltiaJ4movZYEBcLtn6pavIaSpLPU+wgheertBAdZoMhyyXAmlDtimGD1OEThOFmEyIZ8MAocCyzofEmHYbh+xXOgHCmF4vgBNA7CMjEIpwBG0hwAoMAADIQFkhTEcwTrUP6zRtF0vQGOo+RoFmyyKp8izfL8-yAmMSxXHBlLQf6Iw6dR8z6SChk7HspmYJZ8LlP+34INJ4oYlJMkEkSYCkp5lJvvSMCMiybK6VyN40vulLLjAYoSm6MpymW7xKvFvJ3h+nmujOW6hRU4WjjAh5yCgz7xOev7aHOCX5UuwoBmuP4vlu5WwV+9S1X+X4+m59T+eKGSqJBrmPDBBXVFZNnlnpJloZezH1vpMBmfN8IVHJpHkdpVFLXZK0MWtWEbSZW2NpxnjeH4-heCg6AxHEiTPa9-m+FgcmCnB9QNNIEYSRG7QRt0PTqaomnDIx62FOZFQjcaF3YdNsKERUhUwN59g-X50k-YFajBduhhlbe-LIkcEA0LV9UYZdaBNXl76VMlqXro8MAojMdOGAN2iyvK8PM7lC4Ch+HZUEi5PDTN-rfaeE1TW5PoAzAIzbYpu3lPtMBkRRozsU2908f4KLrv42DihqElojAADiSoaH9Ck1IDTtg5D9hKnDaPoAR7ko2L6Pq9jfW42iLtzH5MdKiTJLy5TzXU5FTKx2ojNMczrOS0lbVc87SoyhAOgAFYoOAAqZWHQc9XNnb1FnMoKMWwCWCnyOK6WDs5FnqsIFBvcaztLzLH7OYFg04xTygACS0gFgAjOEwSBCCmzxLqKBupyzkgskoBqvvSH2ZPSoAHJKvpFwrC4j+dDriZ7cgqaG4doyX9PgNz0qS9V7r03ssbeu8z7LW+MfEAp9bKHx-igG+cw74PyfqbO63FHocAAOxuCcCgJwMQIzBDgMJAAbPACchgs4wCKO-Mon5daA1aB0X2-tpiBy0qMeeSCUAvyxuUUOnCsw8KVNCUekdm6VSoVnDEcAZGJxQISUmIUhqpzZhFKq6JZHz2vI3DmbVHwlzmK+Kmi5JGyxdMYlAg1OwK0xqWeRR4UCDwgsPDGwEx5MK1ggwB9Q14bxukjN+JQwAHWNtZMY88-EwACYEG6HEuIPQCJYFAfYICbDekgBIYBUnpMyQAKQgOKaxMRoFqjoaE-649GhNGZKpHo88A5M2wlmbACBO6UDgBAbyUA1jROkPwkOvd6j1y4SMdpnSoDdN6f0gBgyPGzQ8lHCuxS0CyKKeKVxyjk6lXKOVUZa43SYVkW8HkcyTHaDWCAHp0B86JVavUYurdhaZSXsaCUBhmDolmBLB5HskTWNsZYimgERkwE2esxRk13ERwsVZIZPoDZGyzOgpJFsvCdyyTkzF8pEDBlgMAbA7TCB5AKLQ92jDPaNGBqDcGkNjDBwsuCxZAicYcDSUyFAGJpCcvREnMmeyDlGD5dy+5+UDH1HxXgGAepDBaHkHoAwtDG4WMBd3QR4LeXcH5W4keDivHUu1kyj8yKv7GsSUAA)
