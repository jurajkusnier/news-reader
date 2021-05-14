
<p align="center">
    <a href="https://github.com/jurajkusnier/news-reader/issues" title="Open Issues">
        <img src="https://img.shields.io/github/issues/jurajkusnier/news-reader/issues">
    </a>
    <a href="./LICENSE" title="License">
        <img src="https://img.shields.io/badge/License-Apache%202.0-green.svg">
    </a>
</p>

# News Reader

News Reader combines the most used libraries and best practices that you can use as the template for your next android project.

## About
The app loads the latest articles from [News API](https://newsapi.org/) and saves them for offline access. It contains two screens, a list of the latest articles, and an article preview.
Feel free to use this project as a template for your own app.

**Disclaimer:** Work in progress. This is not the final state of the template

## Architecture Overview
- Android [MVVM Design Pattern](https://developer.android.com/jetpack/guide) with ViewModel, Repository and LiveData 
- [Navigation component](https://developer.android.com/guide/navigation/navigation-getting-started) using Safe Args to pass data with type safety.
- Kotlin [Coroutines](https://kotlinlang.org/docs/coroutines-overview.html) + Asynchronous Flow
- [Retrofit](https://square.github.io/retrofit/) + Moshi to make API calls
- [Room](https://developer.android.com/jetpack/androidx/releases/room) as offline cache
- (Hilt)[https://dagger.dev/hilt/] as a DI
- (Coil)[https://github.com/coil-kt/coil] image loading library

## Building
To build the project you have to copy `client.default.properties` to `client.properties` and replace `API_KEY` with your api key.

## Licensing
```
Copyright (c) 2021 Juraj Kusnier.

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

   http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
```
