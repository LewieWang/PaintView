# PaintView 

[![Jitpack 1.0.0](https://jitpack.io/v/LewieWang/PaintView.svg)](https://jitpack.io/#LewieWang/PaintView)

### A simple drawing board
### 简单画板实现

- 支持橡皮檫功能
- 支持撤销功能
- 支持反撤销功能
- 支持清屏功能
- ...

#### 在根目录build.gradle中添加

```
allprojects {
		repositories {
			...
			maven { url 'https://jitpack.io' }
		}
	}
```
#### 项目build.gradle中添加

```
dependencies {
	        implementation 'com.github.LewieWang:PaintView:Tag'
	}
```

#### 如何使用
```
paintView.setEraserWidth(20f)
                .setPaintColor(Color.RED)
                .setPaintWidth(20f)
                .setBackgroundResource(R.mipmap.timg);
```

```
    //设置画笔模式
    paintView.setMode(PaintView.MODE_PEN);
   
    //设置橡皮檫模式
    paintView.setMode(PaintView.MODE_ERASER);
    
    //撤销
    paintView.undo();
    
    //反撤销
    paintView.redo();

    //清屏
    paintView.clear();
    
    //保存图片
    public void save(View view){
        Bitmap bitmap = ImageUtils.view2Bitmap(paintView);

        ImageUtils.save(bitmap,new File(imgPath + imgName ),Bitmap.CompressFormat.JPEG);
    }
```

