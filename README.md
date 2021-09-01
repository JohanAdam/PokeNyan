# PokeNyan
PokeAPi + Kotlin + Clean Architecture + Jetpack Navigation.

My small demonstration for android application based on Clean Architecture and Model-View-ViewModel (MVVM) for me to understand more and hope can help other android developers get head start into Android clean architecture structure.

For now it only fetch all pokemon list from [PokeAPI](https://pokeapi.co/docs/v2) and display the list using [Android Jetpack Paging 3](https://developer.android.com/topic/libraries/architecture/paging/v3-overview), and for the detail page just showing basic info based on the PokeAPI pokemon detail API.

### Project Structure
![image](https://user-images.githubusercontent.com/15909044/131653639-e74d787a-65ac-4d0c-ae70-2988b381aea1.png)

For the project structure, I tried to separated each layer by module. 
#### App module / Presentation layer.
An Android module that contains view (Activities,Fragments,Dialogs,etc..). ViewModels also will be here. This module will be responsible for UI presentation. Every data returned from Data module through entity will be handle in ViewModel and observe by View.
#### Data module / Data layer.
An Android module that contains mappers, Domain Module repository & usecases implementation and data source (Local Data Source/Network Data source) for basic operations. 
#### Domain module 
A pure java module. Its should be the individual and the innermost module. We should not have any Android package related in this module. Domain module consist of entity models, use cases interface, repository interfaces. 

<br>
<p align="center">
  <img src="https://user-images.githubusercontent.com/15909044/131683124-501bb704-8426-458d-b25f-a10e7fa7cdd2.png">
</p>
<br>

### Tech Stack & Library: 
- [Koin](https://insert-koin.io/).
- [Jetpack Library](https://developer.android.com/jetpack) (Navigation , Paging 3, Data Binding, View Binding, View Model, LiveData & Lifecycle)
- [Coroutines.](https://developer.android.com/kotlin/coroutines)
- [Retrofit.](https://square.github.io/retrofit/)
- [Glide.](https://github.com/bumptech/glide)
- [Timber.](https://github.com/JakeWharton/timber)

## Some screenshot!
### List page with pagination.
<img src="/screenshot/Detail.png?raw=true" width="240">

### Details screen.
<img src="/screenshot/List.png?raw=true" width="240">

Reference :
- https://medium.com/android-dev-hacks/detailed-guide-on-android-clean-architecture-9eab262a9011

*This project is just a demo. If you have any feedback or question please reach me!

**If you have any question please feel free to email me at johanadam95@gmail.com or contact me through Twitter!**

[![Twitter URL](https://img.shields.io/twitter/url/https/twitter.com/Johan_Nyan.svg?style=social&label=Follow%20%40Johan_Nyan)](https://twitter.com/Johan_Nyan)

