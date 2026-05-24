# Cat Adoption Board

An Android native mobile application that lets users browse adoptable cats from [TheCatAPI](https://thecatapi.com/), adopt them with custom names, and manage their adopted cat collection with sorting, searching, and editing capabilities.

---

## Setup Instructions

Add the `CATS_API_KEY` line from `example.gradle.properties` to your `gradle.properties` file, and replace `YOUR_API_KEY` with your actual API key from [TheCatAPI](https://thecatapi.com/).

---

## Requirements Traceability

| Requirement | Implementation | Relevant Files |
|---|---|---|
| **List of items with swipe functions for editing and deleting** | A `LazyColumn` renders adopted cats; each item uses `SwipeToDismissBox` — swipe left (end-to-start) triggers deletion, swipe right (start-to-end) navigates to the edit screen | `AdoptedCatsView.kt`, `AdoptedCatsViewModel.kt` |
| **Second view for creating and editing items** | The **Adopt** screen (reached via FAB or empty-state prompt) lets users browse cats and adopt one via a `ModalBottomSheet` with a name field. The **Edit** screen (reached via swipe-right) lets users rename a cat and toggle its ear-tipped status | `AdoptACatView.kt`, `AdoptACatViewModel.kt`, `EditAdoptedCatView.kt`, `EditAdoptedCatViewModel.kt` |
| **Search function for the list (bonus)** | A `TextField` at the top of the list filters cats in real time. The search field changes between name-based and breed-based filtering depending on the active sort field. The Adopt screen also has a searchable breed dropdown | `AdoptedCatsView.kt` |
| **Items respond to taps and show more details** | Tapping a cat card expands it inline to reveal breed descriptions, temperament list, and ear-tipped status. On the Adopt screen, tapping a cat opens a `ModalBottomSheet` with full details and the adopt action | `AdoptedCatsView.kt`, `AdoptACatView.kt` |
| **External API** | Uses [TheCatAPI](https://thecatapi.com/) via the Ktor HTTP client. Two endpoints: `GET /v1/images/search` for cat images and `GET /v1/breeds` for breed reference data. API key is configured via `gradle.properties` and `BuildConfig` | `Cat.kt`, `CatRepository.kt`, `Breed.kt`, `BreedRepository.kt`, `AppModule.kt` |
| **API response caching (bonus)** | Breed data is cached in a Room database on every app launch via a `WorkManager` worker. Cat images are cached on disk by the Coil image-loading library. Cat search results (the image list) are fetched fresh each visit | `BreedRepository.kt`, `LoadBreedInformationWorker.kt`, `AppDatabase.kt`, `BreedDao.kt` |
| **Persisted settings** | Sort field (name/breed), sort order (ascending/descending), and preferred breed filter are persisted across sessions using Jetpack DataStore | `UserPreferences.kt`, `AdoptedCatsViewModel.kt`, `AdoptACatViewModel.kt` |
| **Modern UI framework (Android-specific)** | Entire UI is built with Jetpack Compose + Material3. No XML layouts are used. Screens use Compose Navigation with typed `@Serializable` routes and slide animations | All files in `screens/`, `CatAdoptionBoard.kt`, `Navigation.kt` |
| **RecyclerView/TableView equivalent** | The list is rendered with Compose's `LazyColumn`, which is the declarative equivalent of RecyclerView/TableView | `AdoptedCatsView.kt` |

---

## Technology Stack

| Library / Framework | Purpose |
|---|---|
| **Kotlin 2.3** | Language |
| **Jetpack Compose + Material3** | Declarative UI |
| **Compose Navigation** | Screen routing with typed routes |
| **Hilt (Dagger)** | Dependency injection |
| **Room** | Local database for adopted cats and cached breeds |
| **Ktor Client** | HTTP client for TheCatAPI |
| **kotlinx.serialization** | JSON parsing (API + Room TypeConverters) |
| **Coil (Coil3)** | Async image loading with disk caching |
| **Jetpack DataStore** | Persistent user preferences |
| **WorkManager** | Background breed data sync |
| **Gradle Version Catalog** | Dependency management |

---

## Personal Notes

### Development Time

Approximately 20 hours. I received the assessment on a Friday afternoon, started from an example project, worked primarily Sunday and Monday, added finishing touches on Tuesday before submission.

### Development Process

I thought about how to accomplish each of the requirements, and the Cats API was the easiest for me to visualize. I planned out each of the pages and features I would include to cover the requirements, and what dependencies and components I would need to accomplish them. From there you can see a lot of the progression through my commit history, but I started with getting a query ran, processed, and displayed to the screen using Ktor. From there I made a few small visual components, and started working through the Navigation Graph, Repository and Dao layer, and Dependency Injection via hilt. Once the initial capacity for all of those was created, DataStore was implemented, and each layer was fleshed out more thoroughly.

### Design Decisions

I decided to use Ktor because of its native coroutine support which played nicely with the DataStore preference-based filtering that I wanted to implement.
I used Hilt Work Manager to load in Breed data as part of the caching requirement, and for preparing the breed filter on the Adoption page. 
Additionally, I created the Dao in a way to reuse these breeds for the adopted cats, saving me from duplicating breed data or making a complex type-converter for RoomDB.
I stuck with Compose only MVVM architecture, since that was my background at Seerist, and I feel between that and kotlinx serialization, that everything works together really nicely between navigation and Ktor parsing. 

### What I'd Improve

I wish I could have finished the Breed page in time, but with the amount of time it took configuring dependencies and getting the underlying systems set up, I didn't have adequate time at the end. I should have started my design implementation with a more re-usable card system in mind to save time. Additionally, if there was a way to combine the Adopted and Saved cat objects, that too could save time by combining repo's. I also would have spent more time on the navigation system, because as it stands it's a poor representation of my knowledge of backstack control and NavGraph usage, but again, I was limited on time. 

### Challenges Faced

The initial startup was time-consuming. Finding the correct dependencies and versions, moving them to the Toml files, and constantly rebuilding and re-running to make sure everything was working correctly.
The other part that was tricky was the Hilt WorkManager. In my previous experience, I used a pre-existing factory pattern with lazy initializations of the necessary repositories. Since I was using Hilt in other ways, I thought it made sense to also use it for the Work Manager. This lead to a few different issues with cached build information, modifying the AndroidManifest, and creating the lazy initialization in the Main Activity. 
Lastly, I don't have a great eye for design personally. I'm good at identifying problems with accessibility or usability, but actually designing layouts has been something Designers have dictated for me. So beyond color scheming for accessibility (such as using softer whites and blacks), there wasn't much for me to express on that end. Animations and reusable visual components were a core part of what I did at Seerist, and I didn't get to express that very well. 
