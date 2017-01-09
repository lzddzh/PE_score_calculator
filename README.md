## 体育分数计算器 PE-score calculator
   这是一个带有简单界面的体育分数计算小程序，给体育老师计算分数使用。使用场景是
   在将学生测试成绩（如米数，秒数）换算成学生成绩分数时，评分表上仅有100分，90分，85分
   等标准，却需要给出学生的精确分数值。如：

   给定“男生跳远评分表”如下：
   ```
   分数    米数
   100 2.29
   96 2.26 
   90 2.2
   87 2.17
   81 2.10
   75 1.95
   72 1.92
   66 1.81
   60 1.67
   ```

   要求计算下列学生的成绩：
   ```
   1
   1.67
   1.81
   2.27
   2.29
   3.5
   ```

   只要将上述两个信息复制进入本程序，点击计算按钮，即可得到学生对应的分数。

## 运行截图
   ![screen shot](https://github.com/lzddzh/PE_score_calculator/blob/master/img/ScreenShot.png)

## 运行环境Pre-requests
   Java jre 1.8

## 怎样编译How to Compile
   如需编译代码，需要安装Java jdk 1.8
   If you want to compile the code yourself, then Java JDK 1.8 is needed.
   ```
   $ cd /path/to/this/folder
   $ javac -encoding=UTF-8 Main.java
   ```

## 怎样运行How to Run

   Linux/Mac:
   ```
   $ cd /path/to/this/folder
   $ java -classpath . Main
   ```
   
   Windows:
   安装Java jre 1.8后，双击run.bat即可
   Double click the 'run.bat' to run the program.

## 许可License
   MIT
