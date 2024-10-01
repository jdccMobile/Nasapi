# Nasapi

This app allows you to discover an astronomical event every day from NASA. You can also add the events you like the most as favorites and add your own photos for a more unique and personalized experience.

## Table of Contents
- [Features](#features)
- [Architecture](#rchitecture)
- [Data sources](#data-sources)
  - [API](#api)
  - [Room](#room)
  - [SSOT](#ssot)

## Features

| Feature      | Screenshot | Description |
|--------------|------------|-------------|
| Home         | ![Home](https://github.com/jdccMobile/Nasapi/blob/master/screenshots/home.png) | Discover daily astronomical events and easily access your favorites list from the top bar|
| Details      | ![Details](https://github.com/jdccMobile/Nasapi/blob/master/screenshots/details.png) | Explore the event image in detail and read its description. You can also add your own photos and save the event as a favorite. |
| Favorites    | ![Favorites](https://github.com/jdccMobile/Nasapi/blob/master/screenshots/favorites.png) | Check all your favorite events and access them quickly |

## Architecture

The app is modularized into three layers following the Clean Architecture principles:

- Presentation (App): The app layer contains the UI and the ViewModels. Its responsibilities are to display the data received from the domain layer and react to user events. The presentation layer has access to the data and domain layers to interact with the data and business logic, and then update the UI accordingly.

- Data: The data layer contains the local and remote data sources, their models, and the repository implementations. Its responsibilities are to fetch data from different sources and transform this external data into models that the domain layer understands. The data layer has access to the domain layer to get the necessary data. It implements the repository interfaces defined in the domain layer (inversion of dependencies).

- Domain: The domain layer contains the business logic models, repository interfaces, and use cases. Its responsibility is to define the business logic and models. The domain layer must not depend on the other layers, so changes in the presentation or data layers should not affect the business logic. Additionally, it should not depend on the framework where it is implemented, making it reusable in other contexts.

 ![CleanArchitecture](https://github.com/jdccMobile/Nasapi/blob/master/screenshots/clean-architecture.png)

The app follows the MVVM pattern:

- View: Represents the UI (UI components from the presentation layer) and captures events that the user may trigger. The view is subscribed to the ViewModel, and every time it emits data, the UI will update accordingly. It also communicates user-generated events to the ViewModel so the corresponding action can be taken.

- ViewModel: Represents the state of the view (ViewModel in the presentation layer) and acts as the intermediary between the view and the model. When it receives an event from the view, it communicates with the model via a use case, and when the data is updated, it notifies the view so it can update the UI.

- Model: Represents the data sources (data sources from the data layer), the models (models and repositories from the domain layer), and the business logic (use cases). When it receives a request from the ViewModel, it performs the corresponding read/write operations and returns the information through the use case.

![MVVM](https://github.com/jdccMobile/Nasapi/blob/master/screenshots/mvvm.png)


## Data sources

### API

### Room

### SSOT
