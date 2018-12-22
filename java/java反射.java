/*
JAVA反射机制是在运行状态中，对于任意一个类，都能够知道这个类的所有属性和方法；
对于任意一个对象，都能够调用它的任意方法和属性；这种动态获取信息以及动态调用
对象方法的功能称为java语言的反射机制。

JAVA反射（放射）机制：“程序运行时，允许改变程序结构或变量类型，这种语言称为
动态语言”。从这个观点看，Perl，Python，Ruby是动态语言，C++，Java，C#不是
动态语言。但是JAVA有着一个非常突出的动态相关机制：Reflection，用在Java身上
指的是我们可以于运行时加载、探知、使用编译期间完全未知的classes。换句话说，
Java程序可以加载一个运行时才得知名称的class，获悉其完整构造（但不包括methods定义），
并生成其对象实体、或对其fields设值、或唤起其methods。

反射机制是框架技术的原理和核心部分。
通过反射机制我们可以动态的通过改变配置文件(以后是XML文件)的方式来加载类、调用类方法，
以及使用类属性。这样的话，对于编码和维护带来相当大的便利。在程序进行改动的时候，也只
会改动相应的功能就行了，调用的方法是不用改的。更不会一改就改全身。


代码例子：
https://blog.csdn.net/weixin_40099554/article/details/79753882 

https://blog.csdn.net/u012585964/article/details/52011138
*/

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import javax.swing.JOptionPane;
/**
*本类用于测试反射API，利用用户输入类的全路径，
*找到该类所有的成员方法和成员属性
  */
public class MyTest {
/**
*构造方法
*/
//Example 1:
public MyTest(){
  	//要求用户输入类的全路径
    String classInfo=JOptionPane.showInputDialog(null,"输入类全路径");
        try{
            Class cla=Class.forName(classInfo);//根据类的全路径进行类加载，返回该类的Class对象
     	      Method[] method=cla.getDeclaredMethods();//利用得到的Class对象的自审，返回方法对象集合
            for(Method me : method){//遍历该类方法的集合
                System.out.println(me.toString());//打印方法信息
            }
            System.out.println("********");
            Field[] field=cla.getDeclaredFields();//利用得到的Class对象的自审，返回属性对象集合
  	   	    for(Field me : field){//遍历该类属性的集合
                System.out.println(me.toString());//打印属性信息
            }
        } catch(ClassNotFoundException e) {
          e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        new MyTest();
    }
}

//Input: "javax.swing.JFrame"
/*Output:
public void javax.swing.JFrame.remove(java.awt.Component)
public void javax.swing.JFrame.update(java.awt.Graphics)
…………
********
public static final int javax.swing.JFrame.EXIT_ON_CLOSE
private int javax.swing.JFrame.defaultCloseOperation
*/


//Example 2:

package com;

public class MyTest {
	public static void main(String[]args) {
      TestOne one=null;
	    try{
		      Class cla=Class.forName("com.TestOne");
		      //进行com.TestOne类加载，返回一个Class对象
       		System.out.println("********");
       		one=(TestOne)cla.newInstance();
		      //产生这个Class类对象的一个实例，调用该类无参的构造方法，作用等同于new TestOne()
       	}catch(Exceptione){
           	e.printStackTrace();
       	}
       	TestOne two=newTestOne();
  	    System.out.println(one.getClass()== two.getClass());
	   //比较两个TestOne对象的Class对象是否是同一个对象，在这里结果是true。
	   //说明如果两个对象的类型相同，那么它们会有相同的Class对象
    }
}

class TestOne{
	  static{
       	System.out.println("静态代码块运行");
    }
    	
	  TestOne(){
       	System.out.println("构造方法");
    }
}

//Output:
/*
静态代码块运行
***********
构造方法
构造方法
*/

/*
代码分析：

在进行Class.forName("com.TestOne")的时候，实际上是对com.TestOne进行类加载，
这时候，会把静态属性、方法以及静态代码块都加载到内存中。所以这时候会打印出"静态代码块运行"。
但这时候，对象却还没有产生。所以"构造方法"这几个字不会打印。当执行cla.newInstance()的时候，
就是利用反射机制将Class对象生成一个该类的一个实例。这时候对象就产生了。所以打印"构造方法"。
当执行到TestOnetwo=newTestOne()语句时，又生成了一个对象。但这时候类已经加载完毕，静态的
东西已经加载到内存中，而静态代码块只执行一次，所以不用再去加载类，所以只会打印"构造方法"，
而"静态代码块运行"不会打印。
*/

//Example 3:

package reflect;
import java.lang.reflect.Constructor;
/**
*
*本类测试反射获得类的构造器对象，
*并通过类构造器对象生成该类的实例
*
*/
public class ConstructorTest {
	public static void main(String[] args) {
	    try{
		      //获得指定字符串类对象
		    Class cla = Class.forName("reflect.Tests");
		      //设置Class对象数组，用于指定构造方法类型
        Class[] cl = new Class[]{int.class,int.class};
		      //获得Constructor构造器对象。并指定构造方法类型
       	Constructor con = cla.getConstructor(cl);
		      //给传入参数赋初值
       	Object[] x = {newInteger(33),newInteger(67)};
		      //得到实例
        Object obj = con.newInstance(x);

        //which is equal to:
        //Object obj = Class.forName("reflect.Tests").getConstructor(new Class[]{int.class,int.class}).newInstance(new Object[]{newInteger(33),newInteger(67)});
      }catch(Exception e) {
        e.printStackTrace();
      }
    }
}

class Tests{
	public Tests(int x,int y){
       	System.out.println(x+", "+y);
    }
}

//Output: 
//33, 67

//Example 4:

package reflect;
import java.lang.reflect.Method;
/**
*
*本类测试反射获得类的方法对象，
*并通过类对象和类方法对象，运行该方法
*
*/
public class MethodTest {
    public static void main(String[] args) {
        try{
            //获得窗体类的Class对象
           Class cla = Class.forName("javax.swing.JFrame");
            //生成窗体类的实例
           Object obj = cla.newInstance();
            //获得窗体类的setSize方法对象，并指定该方法参数类型为int,int
           Method methodSize = cla.getMethod("setSize",new Class[]{int.class,int.class});
            /*执行setSize()方法，并传入一个Object[]数组对象，作为该方法参数，等同于窗体对象.setSize(300,300);*/
           methodSize.invoke(obj,new Object[]{newInteger(300), newInteger(300)});
            //获得窗体类的setSize方法对象，并指定该方法参数类型为boolean
           Method methodVisible = cla.getMethod("setVisible", newClass[]{boolean.class});
            /*执行setVisible()方法，并传入一个Object[]数组对象，作为该方法参数。等同于窗体对象.setVisible(true);*/
           methodVisible.invoke(obj, new Object[]{new Boolean(true)});
       }catch(Exception e) {
           e.printStackTrace();
       }
    }
}



