# FunnyBottomNavigation
![](https://img.shields.io/github/issues/FunnySaltyFish/FunnyBottomNavigation?style=flat)
![](https://img.shields.io/github/forks/FunnySaltyFish/FunnyBottomNavigation)
![](https://img.shields.io/github/stars/FunnySaltyFish/FunnyBottomNavigation)
![](https://img.shields.io/github/license/FunnySaltyFish/FunnyBottomNavigation)

#### 介绍
一个很漂亮的底部导航栏，继承自View，带有流畅且有趣的过度动画。支持Java和xml配置

#### 效果
<div>
<img src="https://github.com/FunnySaltyFish/FunnyBottomNavigation/raw/master/demo_show.gif" width="200" style="float:left;margin:10px 20px;"/>
<img src="https://github.com/funnysaltyfish/FunnyBottomNavigation/raw/master/custom_by_xml.png" width="200" style="float:right;margin:10px 20px;"/>
<div/>

因Gif画质压缩问题，图片可能无法反映真实效果。您可以点击[此处](https://github.com/funnysaltyfish/FunnyBottomNavigation/raw/master/demo-1.0.1.apk)下载demo自行体验

#### 快速开始
##### 导入
1. 在应用级别的build.gradle添加jitpack仓库

```bash
maven { url "https://jitpack.io" }
```

2. 在模块级别的build.gradle引入依赖

```bash
implementation 'com.gitee.funnysaltyfish:FunnyBottomNavigation:v1.0.1'
```
3. 同步

##### XML使用
最简单的使用如下：

```xml
<com.funny.bottomnavigation.FunnyBottomNavigation
            android:id="@+id/funny_buttom_navigation"
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            >
```
##### Java代码配置

```java
int[] ids = new int[]{//底部图标资源id
    R.drawable.ic_favorites,
    R.drawable.ic_bin,
    R.drawable.ic_run,
    R.drawable.ic_favorites
};
funnyButtomNavigation.initIconButtons(ids);
funnyButtomNavigation.setOnItemClickListener(position -> {
    //设置点击事件，回调的参数即为当前点击的底部按钮位置
    Log.i("TAG","number "+position+" is clicked");
});
```

#### 高级配置

&emsp;&emsp;除了上述简单的使用外，此库还支持额外的配置，以帮助您实现更为复杂和自定义的效果。
##### 外观
&emsp;&emsp;支持配置的属性如下：

```xml
	<!--        按钮被点击时渲染的颜色-->
        <attr name="highlightColor" format="color|reference" />
        <!--        按钮图片的宽度-->
        <attr name="imageWidth" format="dimension" />
        <!--        按钮图片的高度-->
        <attr name="imageHeight" format="dimension" />
        <!--        默认的起始页，值应当介于[0,页数-1]    -->
        <attr name="startPage" format="integer" />
        <!--        导航栏的背景颜色-->
        <attr name="backgroundColor" format="color|reference" />
        <!--        转移动画的持续时长-->
        <attr name="animationDuration" format="integer" />
```
&emsp;&emsp;使用的例子如下：

```xml
<com.funny.bottomnavigation.FunnyBottomNavigation
            android:id="@+id/funny_buttom_navigation"
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            app:animationDuration="500"
            app:backgroundColor="#FFFFFF"
            app:imageWidth="24dp"
            app:imageHeight="24dp"
            app:highlightColor="@color/design_default_color_primary"
            app:startPage="1"
            >
```

上述属性同时有对应set和get方法。

##### 回调
&emsp;&emsp;本View目前可以设置两种监听，它们均位于FunnyBottomNavigation类下。

```java
    /*
     当点击到底部按钮时会回调此接口
     参数 position 为当前点击的按钮位置，取值为[0,总数-1]
     注意，当动画仍在进行时点击无效，此时不会触发此回调
    */
    public interface OnItemClickListener{
        void onClick(int position);
    }

    //当动画进行时会回调此接口
    //参数 progress 值为[0,100]整数，代表当前动画进行的百分比
    public interface OnAnimationUpdateListener{
        void onUpdate(int progress);
    }

```
请注意，**点击事件会在抬起手指后立刻回调！**

#### 更多
&emsp;&emsp;本库的动画效果参考自[这个视频](https://www.bilibili.com/video/BV1Jp4y1q71U?t=66)

&emsp;&emsp;如果在使用过程中遇到问题，或对本库有任何功能性建议的，欢迎提出对应issue。您的支持就是我们持续进步的最大动力。

