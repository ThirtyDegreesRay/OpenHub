[English](/README.md) | 中文
# OpenHub 
[![Releases](https://img.shields.io/github/release/ThirtyDegreesRay/OpenHub.svg)](https://github.com/ThirtyDegreesRay/OpenHub/releases/latest)

一个**开源**的GitHub Android客户端应用程序,更快更简洁。
<!-- ![OpenHub](https://github.com/ThirtyDegreesRay/OpenHub/raw/master/art/openhub.png) -->

[Download Apk From Releases](https://github.com/ThirtyDegreesRay/OpenHub/releases/latest)

## 特点
* 项目使用MVP+dagger2+retrofit架构，在onSaveInstanceState时保存了Activity、Fragment、Presenter中的数据；
* 对Retrofit2+RxJava进行了封装，实现了先加载缓存数据，然后再加载网络数据显示，用户体验更佳；
* 支持主题切换、16种强调色切换、语言切换；
* 支持趋势版本库列表查看，每隔1小时，服务器端刷新一次数据；
* 启动时立马显示闪屏，无白屏时间；
* 更快更酷的代码高亮；
* And more...

## 截屏

| 动态 | 侧滑页 | 个人主页 |
|:-:|:-:|:-:|
| ![news](/art/news.png?raw=true) | ![drawer](/art/drawer.png?raw=true) | ![profile](/art/profile.png?raw=true) |

| 版本库主页 | 趋势 | 代码 |
|:-:|:-:|:-:|
| ![repo](/art/repo.png?raw=true) | ![trending](/art/trending.png?raw=true) | ![code](/art/code.png?raw=true) |

## 开源库

* [ButterKnife](https://github.com/JakeWharton/butterknife) Bind Android views and callbacks to fields and methods.
* [GreenDao](https://github.com/greenrobot/greenDAO) A light & fast ORM solution for Android that maps objects to SQLite databases.
* [EventBus](https://github.com/greenrobot/EventBus) Android optimized event bus that simplifies communication between Activities, Fragments, Threads, Services, etc.
* [RxJava](https://github.com/ReactiveX/RxJava) A library for composing asynchronous and event-based programs using observable sequences for the Java VM.
* [RxAndroid](https://github.com/ReactiveX/RxAndroid) RxJava bindings for Android.
* [Retrofit](https://github.com/square/retrofit) Type-safe HTTP client for Android and Java by Square, Inc.
* [Dagger](https://github.com/google/dagger) A fast dependency injector for Android and Java. 
* [Glide](https://github.com/bumptech/glide) An image loading and caching library for Android focused on smooth scrolling.
* [DataAutoAccess](https://github.com/ThirtyDegreesRay/DataAutoAccess) A simple way to access android bundle data.
* [RoundedImageView](https://github.com/vinc3m1/RoundedImageView) A fast ImageView that supports rounded corners, ovals, and circles.
* [Toasty](https://github.com/GrenderG/Toasty) The usual Toast, but with steroids 💪.
* [material-about-library](https://github.com/daniel-stoneuk/material-about-library) Makes it easy to create beautiful about screens for your apps.
* [material-dialogs](https://github.com/afollestad/material-dialogs) A beautiful, fluid, and customizable dialogs API.
* [GitHub-Trending](https://github.com/thedillonb/GitHub-Trending) Scrapes GitHub's Trending Pages.

## License
    Copyright 2017 ThirtyDegreesRay
    
    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at
    
       http://www.apache.org/licenses/LICENSE-2.0
    
    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.



