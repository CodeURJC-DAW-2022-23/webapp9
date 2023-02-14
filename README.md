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

## Team Organization

The team will be organized using the intregration with the native GitHub Projects. That way, team members can use GitHub Issues and other GitHub options to automate the workflow of the web application development. GitHub Project can be accessed through the following [link](https://github.com/orgs/CodeURJC-DAW-2022-23/projects/3/views/1?layout=board).

## App features

Here you can see the features of our web application TripScanner.

### Entities

We'll have the followin entities **users**, **places**, **destinations** and **itineraries**.


<picture>
  <source media="(prefers-color-scheme: dark)" srcset="https://user-images.githubusercontent.com/72264031/217202549-74b12ef3-274f-477a-935e-f8d553fb5e04.svg">
  <source media="(prefers-color-scheme: light)" srcset="https://user-images.githubusercontent.com/72264031/216968255-32aef1b5-eaf1-45b7-acb8-776c2c00c8fc.svg">
  <img alt="Shows an illustrated sun in light mode and a moon with stars in dark mode." src="https://user-images.githubusercontent.com/25423296/163456779-a8556205-d0a5-45e2-ac17-42d089e3c3f8.png">
</picture>

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

## Phase 1 aditions

## Webpage screenshots

Here you can see screenshots of the webpage developed during phase 1.

![image](https://user-images.githubusercontent.com/102818341/218514617-db631ba1-2f02-4f34-8a24-52230c3a11cf.png)

The main page will show popular itineraries, destinations and places. Those are the ones most seen by the users. It will also allow for manual search using the search bar, and login into your account.

![image](https://user-images.githubusercontent.com/102818341/218514955-bb821902-daf1-4766-a1d1-510619b4bf83.png)

The login popup allows users to log into their account or create an account if they don't have one.

![image](https://user-images.githubusercontent.com/102818341/218515154-eb5aa2c4-0f4b-4ba8-a12d-54fe4da58ce8.png)

The registration page allows for unregistered users to create a new account to get all the features of TripScanner.

![image](https://user-images.githubusercontent.com/102818341/218515601-bed69071-20ea-440f-a1e4-00f3b87c5f17.png)

Results are shown after using the search bar and can be filtered by public results or private ones you have created as a registered user. You also have a number of filters to narrow down the search results.

![image](https://user-images.githubusercontent.com/102818341/218515651-1c54e6c2-a9b2-4128-9c1a-ce9a791aafe1.png)

The details of an item are shown when you click on it and show some more information about it.

![image](https://user-images.githubusercontent.com/72264031/218572147-eb1ee025-adb0-480e-bf65-70b62e403fb0.png)

Registered users can see their profile information here, as well as editing it, and their itineraries.

![image](https://user-images.githubusercontent.com/72264031/218571911-a75a0a10-7461-4631-a2e8-dff7bf44f2e4.png)

At last, administrators can see a panel with the options they have. This panel (as well as the user information one) is not accesible yet by navigating through the webpage.

### Navigation diagram

![Diagrama DAW-3-1](https://user-images.githubusercontent.com/102818341/218516781-fdaf6302-f9dd-4d04-948e-cc64230ff477.png)

