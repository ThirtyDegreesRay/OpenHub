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

* [ButterKnife](https://github.com/JakeWharton/butterknife) 视图绑定
* [GreenDao](https://github.com/greenrobot/greenDAO) 一个轻量级而且更快的Android ORM解决方案
* [EventBus](https://github.com/greenrobot/EventBus) 简化Android Activities, Fragments, Threads, Services,等组件之间的通信
* [RxJava](https://github.com/ReactiveX/RxJava) 一个专注于异步编程与控制可观察数据（或者事件）流的API
* [RxAndroid](https://github.com/ReactiveX/RxAndroid) 为了在Android中使用RxJava
* [Retrofit](https://github.com/square/retrofit) 安全的HTTP请求工具库
* [Dagger](https://github.com/google/dagger) 一个快速的依赖注入
* [Glide](https://github.com/bumptech/glide) 一个图像加载和缓存库为了Android中更平滑的滚动。
* [DataAutoAccess](https://github.com/ThirtyDegreesRay/DataAutoAccess) 一个简单的方法去自动存取Android bundle中的数据
* [RoundedImageView](https://github.com/vinc3m1/RoundedImageView) 一个快速ImageView支持圆角,椭圆形,圆形
* [Toasty](https://github.com/GrenderG/Toasty) 美化的Toast
* [material-about-library](https://github.com/daniel-stoneuk/material-about-library) 让你更容易的创建美丽的关于页面
* [material-dialogs](https://github.com/afollestad/material-dialogs) 一个美丽、流畅的和可自定义的对话框的API
* [GitHub-Trending](https://github.com/thedillonb/GitHub-Trending) 对GitHub趋势页面进行数据抓取

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



