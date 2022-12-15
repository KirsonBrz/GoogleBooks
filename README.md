<h1 align="left">Приложение для поиска книг</h1>

![progress](https://img.shields.io/badge/Progress-In%20work-yellow)

Тестовое задание для компании Subtotal


<h3 align="left">Полное превью</h3>




https://user-images.githubusercontent.com/52128742/207201427-f774df72-5a4b-4a51-bb0a-2f11d9153074.mp4



<h1 align="left">Стэк </h1>

- MVVM Clean Architecture project
- Kotlin Coroutines
- Jetpack Compose
- Kotlin Flow
- Hilt
- Retrofit
- UnitTesting using MockWebServer and Truth
- Pagination with compose pager

<h2 align="left">Принцип: </h2>

I

Пользователь заходит в приложение, видит строку поиска и список категорий


II

Пользователь может произвести поиск по строке текста или категории, перед ним откроется список найденных книг

III

Пользователь открывает страницу книги, может посмотреть её название, автора, дату выхода и описание.



<h2 align="left">Функционал: </h2>

1) Многомодульное приложение с функциональными модулями:
- Основные компоненты, некоторые сущности и тема приложения лежат в модуле [core](https://github.com/KirsonBrz/GoogleBooks/tree/master/core) 
- Реализован функциональный модуль [home](https://github.com/KirsonBrz/EcommerceConcept/tree/master/main), где происходит поиск и просмотр книг
- Каждый из слоев представляет собой отдельный модуль, для инкапсуляции добавлены мапперы из интернет моделей в доменные.

2) Приложение доступно в светлой


![lightTheme](https://user-images.githubusercontent.com/52128742/207201553-46c5eca7-474b-4dd0-b493-e226a855c9f5.jpg)


и тёмной темах

![darkTheme](https://user-images.githubusercontent.com/52128742/207201576-62639dce-bc21-46c3-a43d-ae453af37a49.jpg)


2) Отдельное внимание уделяется анимациям, занимающим важную роль в восприятии интерфейса пользователем, например:
- [Переключение между табами](https://github.com/KirsonBrz/GoogleBooks/blob/master/home/ui/src/main/java/com/kirson/googlebooks/components/SuggestionScreen.kt)

![gifSugges](https://media.giphy.com/media/hEB42GjDDvBXJmYusd/giphy.gif)

- [Анимация TopBar при выборе категории](https://github.com/KirsonBrz/GoogleBooks/blob/master/home/ui/src/main/java/com/kirson/googlebooks/components/ScrollableAppBar.kt)

![gifCategory](https://media.giphy.com/media/vcyDESVNrRcqfq4iNZ/giphy.gif)

- [состояние сети](https://github.com/KirsonBrz/GoogleBooks/blob/master/core/ui/uikit/src/main/java/com/kirson/googlebooks/components/ConnectivityStatus.kt)

![gifNet](https://media.giphy.com/media/cnE8fgwdMqAbHg10bL/giphy.gif)

3) Добавлены Unit тесты в data слое для тестирования запросов к API, пригодились при отладке бага с двойным поиском.

4) Навигация в приложении реализована благодаря модулю [navigation](https://github.com/KirsonBrz/EcommerceConcept/tree/master/navigation), под капотом: NavigationComponent и NavGraph от Compose

5) Для иньекции зависимостей используется Hilt, но для правильной реализации модулей и чистой архитектуры необходимо использовать Dagger2 (например, интерфейс репозитория должен находится в domain слое, где мы пользуемся его методами, но не знаем их реализации)

6) На разработку проекта затрачено чуть более 50 часов

![time](https://user-images.githubusercontent.com/52128742/207202588-316f7c34-407e-4378-ade2-ab15f0e3e74f.png)




