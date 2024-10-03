# Nasapi

This app allows you to discover an astronomical event every day from NASA. You can also add the events you like the most as favorites and add your own photos for a more unique and personalized experience.

<br>

## Table of Contents
- [Features](#features)
- [Architecture](#rchitecture)
- [Data sources](#data-sources)
  - [Remote](#remote)
  - [Local](#local)
  - [SSOT](#ssot)

<br>   

## Features

| Feature      | Screenshot | Description |
|--------------|------------|-------------|
| Home         | ![Home](https://github.com/jdccMobile/Nasapi/blob/master/screenshots/home.png) | Discover daily astronomical events and easily access your favorites list from the top bar|
| Details      | ![Details](https://github.com/jdccMobile/Nasapi/blob/master/screenshots/details.png) | Explore the event image in detail and read its description. You can also add your own photos and save the event as a favorite |
| Favorites    | ![Favorites](https://github.com/jdccMobile/Nasapi/blob/master/screenshots/favorites.png) | Check all your favorite events and access them quickly |

<br>   

## Architecture

The app is modularized into three modules following the Clean Architecture principles:

- Presentation (App): The app layer contains the UI and the ViewModels. Its responsibilities are to display the data received from the domain layer and react to user events. The presentation layer has access to the data and domain layers to interact with the data and business logic, and then update the UI accordingly.

- Data: The data layer contains the local and remote data sources, their models, and the repository implementations. Its responsibilities are to fetch data from different sources and transform this external data into models that the domain layer understands. The data layer has access to the domain layer to get the necessary data. It implements the repository interfaces defined in the domain layer (inversion of dependencies).

- Domain: The domain layer contains the business logic models, repository interfaces, and use cases. Its responsibility is to define the business logic and models. The domain layer must not depend on the other layers, so changes in the presentation or data layers should not affect the business logic. Additionally, it should not depend on the framework where it is implemented, making it reusable in other contexts.
<div align="center">
  <img src="https://github.com/jdccMobile/Nasapi/blob/master/screenshots/clean-architecture.png" alt="CleanArchitecture" width="300">
</div>
<br> 

The app follows the MVVM pattern:

- View: Represents the UI (UI components from the presentation layer) and captures events that the user may trigger. The view is subscribed to the ViewModel, and every time it emits data, the UI will update accordingly. It also communicates user-generated events to the ViewModel so the corresponding action can be taken.

- ViewModel: Represents the state of the view (ViewModel in the presentation layer) and acts as an intermediary between the view and the model. When it receives an event from the view, it communicates with the model through a use case, and when data is updated, it notifies the view so that it can update the user interface.

- Model: Represents the data sources (data sources from the data layer), the models (models and repositories from the domain layer), and the business logic (use cases). When it receives a request from the ViewModel, it performs the corresponding read/write operations and returns the information through the use case.

<div align="center">
  <img src="https://github.com/jdccMobile/Nasapi/blob/master/screenshots/mvvm.png" alt="MVVM" width="500">
</div>

<br>

Benefits of Implementing Clean Architecture and MVVM in Android: 
- **Separation of Concerns (Modularity)**: by dividing the app into distinct layers (Presentation, Data, and Domain), each layer has its own responsibilities. This separation makes it easier to manage and maintain the codebase. 
- **Testability**: each layer can be tested independently, allowing for easier writing of unit tests. The MVVM pattern facilitates testing by allowing developers to test the ViewModel without requiring the UI to be present. 
- **Scalability**: as the application grows, new features can be added with minimal impact on existing code. Changes in one layer do not necessarily affect other layers. 
- **Reusability**: the Domain layer can be reused in different applications or contexts without being dependent on the Presentation or Data layers. This improves the maintainability of the code. 
- **Enhanced Maintainability**: the clear structure of Clean Architecture makes it easier for developers to navigate the codebase, reducing the time required for onboarding new team members or making changes. 
- **Improved UI Responsiveness**: the MVVM pattern allows the UI to react to changes in the underlying data automatically, improving the user experience by providing real-time updates. 
- **Independence from Frameworks**: the Domain layer is independent of any frameworks, which allows for flexibility in changing or upgrading the underlying technologies used in the presentation or data layers. 

<br>

## Data sources

### Remote
For the remote data source, the [NASA's APOD (Astronomy Picture of the Day) API](https://api.nasa.gov/) is used. This API returns images along with associated metadata such as the date, title, description, images, and other details.

| **Parameter** | **Type**           | **Default** | **Description** |
|----------------------|--------------------|-----------------------|-----------------|
| **api_key**           | String             | `DEMO_KEY`            | Your API key for authentication. Sign up at [NASA API](https://api.nasa.gov/#signUp). |
| **date**              | String (YYYY-MM-DD)| Today's date          | The date of the APOD image (e.g., 2014-11-03). Must be after 1995-06-16. No images are available for tomorrow. |
| **concept_tags**      | Boolean            | `False`               | Whether concept tags should be returned. Concept tags are derived from common search tags in the description. |
| **hd**                | Boolean            | Ignored               | Indicates whether high-resolution images should be returned. Always ignored, high-res images are returned regardless. |
| **count**             | Integer (1-100)    | N/A                   | The number of randomly chosen images to return. Cannot be used with `date`, `start_date`, or `end_date`. |
| **start_date**        | String (YYYY-MM-DD)| N/A                   | The start of a date range. All images between `start_date` and `end_date` will be returned. Cannot be used with `date`. |
| **end_date**          | String (YYYY-MM-DD)| Current date          | The end of a date range. If only `start_date` is specified, `end_date` defaults to today. |
| **thumbs**            | Boolean            | `False`               | Whether a thumbnail URL for video files should be returned. Ignored if the APOD is not a video. |

| **Returned Fields**        | **Type**      | **Description**                                                                                      |
|------------------|---------------|------------------------------------------------------------------------------------------------------|
| **resource**      | Dictionary    | Describes the `image_set` or planet that the response illustrates, determined by the structured endpoint. |
| **concept_tags**  | Boolean       | Reflection of the supplied option. Included in the response because of default values.                |
| **title**         | String        | The title of the image.                                                                               |
| **date**          | String        | The date of the APOD image. Included in response due to default values.                               |
| **url**           | String        | The URL of the APOD image or video of the day.                                                        |
| **hdurl**         | String        | The URL for any high-resolution image for that day. Returned regardless of the 'hd' param setting, omitted if unavailable. |
| **media_type**    | String        | The type of media returned. May either be 'image' or 'video' depending on the content.                |
| **explanation**   | String        | The supplied text explanation of the image.                                                          |
| **concepts**      | Array         | The most relevant concepts within the text explanation. Only supplied if `concept_tags` is set to True. |
| **thumbnail_url** | String        | The URL of the thumbnail of the video.                                                               |
| **copyright**     | String        | The name of the copyright holder.                                                                    |
| **service_version**| String        | The version of the API service used.                                                                 |

<br>

### Local
For the local data source, the Room library is used to easily work with SQL databases. There are two tables:

**AstronomicEventDb** stores information about astronomical events.
| **Field**       | **Type**    | **Description**                                                                            |
|-----------------|-------------|--------------------------------------------------------------------------------------------|
| **id**          | String      | The unique identifier for the astronomical event                                          |
| **title**       | String      | The title of the astronomical event                                                        |
| **description** | String      | A detailed description of the astronomical event                                          |
| **date**        | String      | The date in YYYY-MM-DD format when the astronomical event occurred                        |
| **imageUrl**    | String?     | The URL of the event's image (can be `null` if no image is available)                   |
| **is_favorite** | Boolean     | Indicates whether the astronomical event has been marked as a favorite by the user |
| **has_image**   | Boolean     | Indicates whether the event has an image available                         |

<br>

**AstronomicEventPhotoDb** stores photos taken by the user and associates them with each event.
| **Field**       | **Type**    | **Description**                                                                            |
|-----------------|-------------|--------------------------------------------------------------------------------------------|
| **photoId**     | String      | The unique identifier for the photo                                                       |
| **eventId**     | String      | The identifier of the astronomical event related to the photo (relation to AstronomicEventDb) |
| **filePath**    | String      | The local path where the photo is stored on the device                                   |

<br>

### SSOT
To maintain data consistency and integrity, our application adheres to the Single Source of Truth (SSOT) principle in managing data sources. To implement this, we follow these steps:

- **Fetching Remote Data**: The application requests data from the NASA APIs, specifically the Astronomy Picture of the Day (APOD) API, which provides information about astronomical events.

- **Storing Locally**: The fetched data is stored in a local database using the Room library, comprising two tables:
  - **AstronomicEventDb**: Contains details about astronomical events.
  - **AstronomicEventPhotoDb**: Stores user-uploaded photos associated with events.

- **Accessing Local Data**: The application retrieves all data exclusively from the local database, making it the single source of truth for the user interface.

<br>

The benefits of implementing SSOT are:
- Consistency: Ensures accurate and reliable information presented to users.
- Performance: Local data access is faster than frequent remote API calls, improving app responsiveness.
- Offline Capability: Users can access previously fetched data without an internet connection.
- Simplified Management: Focuses on maintaining the integrity of the local database instead of synchronizing multiple data sources.
