# FunnyBottomNavigation

#### 介绍
一个很漂亮的底部导航栏，继承自View，带有流畅且有趣的过度动画。支持Java/Kotlin和xml配置

#### 效果

<p>
<div>
<img src="https://gitee.com/funnysaltyfish/FunnyBottomNavigation/raw/master/demo_show.gif" width="200" style="float:left;margin:10px 20px;"/>
<img src="https://gitee.com/funnysaltyfish/FunnyBottomNavigation/raw/master/custom_by_xml.png" width="200" style="float:right;margin:10px 20px;"/>
<div/>
</p>
详细动画效果：  

![输入图片说明](https://gitee.com/funnysaltyfish/FunnyBottomNavigation/raw/master/detail_gif.gif "在这里输入图片标题")   


因Gif画质压缩问题，图片可能无法反映真实效果。您可以点击[此处](https://gitee.com/funnysaltyfish/FunnyBottomNavigation/raw/master/demo-1.1.0.apk)下载demo自行体验

#### 快速开始
##### 导入
1. 在应用级别的build.gradle添加jitpack仓库

```bash
maven { url "https://jitpack.io" }
```

2. 在模块级别的build.gradle引入依赖
* 请注意，从v1.1.0开始，此库的实现语言由原先的Java迁移至Kotlin，此变迁一般不会影响Java使用，但仍有一定概率出现问题。如果遇到这类情况，请在issue中提出。

```bash
implementation 'com.gitee.funnysaltyfish:FunnyBottomNavigation:v1.1.0'
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

##### Kotlin配置

```kotlin
val ids  = intArrayOf(
            R.drawable.ic_favorites,
            R.drawable.ic_bin,
            R.drawable.ic_run,
            R.drawable.ic_favorites)
//...
funnyButtomNavigation.initIconButtons(ids);
funnyButtomNavigation.onItemClickListener = object : FunnyBottomNavigation.OnItemClickListener{
                override fun onClick(position: Int) {
                    Log.i(TAG, "" + position + "isClicked")
                    funnyViewModel.setText("第" + position + "页")
                }
            }
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

```kotlin
    /*
     当点击到底部按钮时会回调此接口
     参数 position 为当前点击的按钮位置，取值为[0,总数-1]
     注意，当动画仍在进行时点击无效，此时不会触发此回调
    */
    interface OnItemClickListener {
        fun onClick(position: Int)
    }

    //当动画进行时会回调此接口
    //参数 progress 值为[0,100]整数，代表当前动画进行的百分比
    interface OnAnimationUpdateListener {
        fun onUpdate(progress: Int)
    }

```
请注意，**点击事件会在抬起手指后立刻回调！**



##### 初始化

&emsp;&emsp;本应用支持以下两种初始化方式

```kotlin
	/**
     * 初始化底部按钮
     * @param iconIds 图片id的集合（ArrayList形式)
     */
    fun initIconButtons(iconIds: ArrayList<Int?>) 
        
    /**
     * 初始化底部按钮
     * @param iconIds 图片id的集合（数组形式)
     */
    fun initIconButtons(iconIds: IntArray)
```

上述两种初始化效果完全相同。

请注意，初始化会等待至View宽高测量完成再进行，此期间如果获取buttonList会产生错误！



##### 手动跳转

1.0.2版本新增了手动跳转的方法

```kotlin
	/**
     * 跳转到对应页面
     * @param page 需要跳转的页面，取值 [0,页面总数-1]
     * @param hasAnimation 是否有动画效果
     * @param performClick 是否同时执行点击事件【请确保点击事件不会造成方法死循环】
     */
    @JvmOverloads
    fun moveTo(page: Int, hasAnimation: Boolean = true, performClick: Boolean = false)
```



#### 更多
&emsp;&emsp;本库的动画效果参考自[这个视频](https://www.bilibili.com/video/BV1Jp4y1q71U?t=66)

&emsp;&emsp;如果在使用过程中遇到问题，或对本库有任何功能性建议的，欢迎提出对应issue。您的支持就是我们持续进步的最大动力。

