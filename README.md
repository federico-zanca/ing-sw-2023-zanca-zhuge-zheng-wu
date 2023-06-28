# Progetto di Ingegneria del Software 2023

# Index
- Group Components
- Introduction to "My Shelfie"
- External Libraries
- Implemented Features
- User Instructions


# Group Components

| Cognome | Nome | e-mail 
| ------ | ------ |----- 
| Zanca | Federico| margherita.musumeci@mail.polimi.it
| Zhenghao Zhuge|  Michele | matteo.nunziante@mail.polimi.it
| Wu |Jia Hui| matteo.oldani@mail.polimi.it
| Zheng |Fabio| fabio1.zheng@mail.polimi.it


# Introduction to "My Shelfie"

My Shelfie is a board game that can be enjoyed by 2 to 4 players, set in a living room where every player is given a bookshelf to place item tiles picked from the living room board.
The participants compete with each other to fulfill goals that award points. These goals are visible to everyone and are randomly drawn during the game setup. Every player is also assigned a personal goal card that is not visible to others. Points are also awarded for the number of adjacent tiles you manage to place in your bookshelf.
The game ends when a player fills all the squares of their books and continues until it reaches the player sitting to the right of the first player.
The player with the most points wins.

This game is perfect for a tranquil evening with your family or friends but also promises to be competitive enough to add some zest.

# External Libraries


# Implemented Features
Number of Advanced Features implemented: 3/4
|Feature |Status|
|--------|------------|
| Simplified Rules |  ![success](https://via.placeholder.com/15/00ff00/000000?text=+)|
|Complete Rules|  ![success](https://via.placeholder.com/15/00ff00/000000?text=+)|
|TUI|  ![success](https://via.placeholder.com/15/00ff00/000000?text=+)|
|GUI |  ![success](https://via.placeholder.com/15/00ff00/000000?text=+)|
|Socket Connection|  ![success](https://via.placeholder.com/15/00ff00/000000?text=+)|
|JavaRMI Connection|  ![success](https://via.placeholder.com/15/00ff00/000000?text=+)|
|Multiple Games|   ![success](https://via.placeholder.com/15/00ff00/000000?text=+)|
|Resilience to Disconnections|   ![success](https://via.placeholder.com/15/00ff00/000000?text=+)|
|Chat|   ![success](https://via.placeholder.com/15/00ff00/000000?text=+)|
|Server Persistence|   ![failure](https://via.placeholder.com/15/ff0000/000000?text=+)|


# User Instructions

Into the deliverables folder of this poject can be found two jar files.

#### The "server.jar" is the executable dedicated to the server.

Execute with command:
```sh
java -jar server.jar
```
No interactions are possible with the server.

#### The "client.jar" is the executable dedicated to the clients.

Execute with command:
```sh
java -jar client.jar
```

#### Executing the client will display a message asking to choose between GUI and CLI.

Type:
- 0 to access CLI interface
- Any other character or word to access GUI interface


#### Insert the IP adress of the server you wish to connect to.

#### Choose the connection mode 

Write:
- 1 for socket connection
- 2 for RMI connection


## License

This project is developed in collaboration with Politecnico di Milano and Cranio Creations.

All rights of graphic resources are reserved to Cranio Creation.
