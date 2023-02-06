# webapp9

# TripScanner

## Team members and info

Here you can find information about the members that form the developer team for TripScanner

| Name        | Surname        | URJC Mail                        | Github                                               |
| -           | -              | -                                | -                                                    |
| Marcos      | Ferrer Zalve   | m.ferrer.2020@alumnos.urjc.es    | [@LovetheFrogs](https://github.com/LovetheFrogs)     |
| Anna        | Trofimova      | a.trofimova.2020@alumnos.urjc.es | [@anna-trofimova](https://github.com/anna-trofimova) |
| Iván        | Penedo Ventosa | i.penedo.2020@alumnos.urjc.es    | [@xIvqn](https://github.com/xIvqn)                   |
| Adaya María | Ruíz Mayoral   | a.ruiz.2020@alumnos.urjc.es      | [@AdayaUwU](https://github.com/AdayaUwU)             |

## App features

Here you can see the features of our web application TripScanner.

### Entities

We'll have the followin entities **users**, **places**, **destinations** and **itineraries**.

![DAW_Relationships_Diagram](https://user-images.githubusercontent.com/102818341/215725341-230eb5bf-a057-4bba-bb4d-dfab198adbce.svg)

### User privileges

Our web app will have the following types of users:

- **Unregistered users:** Not registered users can search for itineraries, destinations and places, but can not save them anywhere.

- **Registered users:** Registerd users can do the same as unregistered ones, but they can also create new itineraries (either public or private), save public ones and edit them privately. Registered users also have a personal account in the website where they can create a biography and change their profile picture and how they appear to other users.

- **Administrator:** Administrators can do all the previous tasks. They can also edit the public itineraries and create new ones, add new places and destinations.

### Images

Out of our four entities, three of them will have images asociated to them. Users will have a profile picture of their liking, places will have a photo of them and destinations will have a picture of an iconic landmark they have.

### Graphs

Inside each destination, users will be able to see a bar graph containing the most popular places inside them. There will also be a graph in the main page where you can see the most popular destinations.

### Aditional technologies

The app will be able to send confirmation emails to users when they register, and anytime they save or edit an itinerary.

It will also be able to generate pdf documents with a selected itinerary that a user has saved.

Our app will use the OpenStreetMap API which is an Open Source map API. We will also be using SkyScanner's and TripAdvisor API to get information about hotels, destinations and flights.

### Advanced algorithms

TripScanner will have a selection of algorithms. These are:

- An algorithm to search for the shortest path between places of an itinerary.
- A search algorithm with different search criteria, for both places and destinations.
