# MvpLibrary
mvp依赖库

1.添加方法

   allprojects{
  
      repositroies{
      
         ....
         
        maven { url 'https://jitpack.io' }
        
      }

  }

  dependencies {
  
    implementation  'com.github.AgnoiY:MvpLibrary:1.0.17'

    implementation 'com.android.support:design:28.0.0'
    
    implementation 'com.android.support:multidex:1.0.3'
    
    /*事件线Eventbus*/
    
    implementation 'org.greenrobot:eventbus:3.1.1'
    
    /*recyclerView*/
    implementation 'com.android.support:recyclerview-v7:28.0.0'
    
    implementation 'com.scwang.smartrefresh:SmartRefreshLayout:1.0.3'
    
    implementation 'com.github.CymChad:BaseRecyclerViewAdapterHelper:2.9.34'
   
    /*屏幕适配*/
    
    implementation 'me.jessyan:autosize:1.1.2'
   
    /*网络请求框架*/
    
    implementation 'com.github.AgnoiY:RrtrofitFrame:1.0.19'
    
    implementation 'com.squareup.okhttp3:okhttp:3.10.0'
    
    implementation 'com.squareup.retrofit2:retrofit:2.3.0'
    
    implementation 'com.squareup.retrofit2:adapter-rxjava2:2.3.0'
    
    implementation 'com.squareup.retrofit2:converter-gson:2.3.0'
    
    /*RxJava&RxAndroid*/
    
    implementation 'io.reactivex.rxjava2:rxandroid:2.0.1'
    
    implementation 'io.reactivex.rxjava2:rxjava:2.1.3'
    
    /*RxLifecycle基础库*/
    
    implementation 'com.trello.rxlifecycle2:rxlifecycle:2.1.0'
    
    implementation 'com.trello.rxlifecycle2:rxlifecycle-components:2.1.0'
  
  }
  
2.使用方法

　　在Application中
 
    　 onCreate()方法里进行注册：
 
 　　　　　　RetrofitLibrary.getHttpConfigure().setNotTipDialog(true);　// 网络请求基础配置类,　可选
       
 　　　　　　MvpLibrary.init(this, BaseUrl);
  
     　onTerminate()方法里进行注销：
     
　　　　　　MvpLibrary.onDestory();
