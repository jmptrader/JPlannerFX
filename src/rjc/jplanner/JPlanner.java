/**************************************************************************
 *  Copyright (C) 2016 by Richard Crook                                   *
 *  https://github.com/dazzle50/JPlannerFX                                *
 *                                                                        *
 *  This program is free software: you can redistribute it and/or modify  *
 *  it under the terms of the GNU General Public License as published by  *
 *  the Free Software Foundation, either version 3 of the License, or     *
 *  (at your option) any later version.                                   *
 *                                                                        *
 *  This program is distributed in the hope that it will be useful,       *
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of        *
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the         *
 *  GNU General Public License for more details.                          *
 *                                                                        *
 *  You should have received a copy of the GNU General Public License     *
 *  along with this program.  If not, see http://www.gnu.org/licenses/    *
 **************************************************************************/

package rjc.jplanner;

import java.io.File;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;

import javafx.application.Application;
import javafx.scene.control.Tooltip;
import javafx.stage.Stage;
import javafx.util.Duration;
import rjc.jplanner.gui.MainWindow;
import rjc.jplanner.model.DateTime;
import rjc.jplanner.model.Plan;

/*************************************************************************************************/
// JPlanner by Richard Crook
// Aims to be a project planner similar to M$Project with table entry of tasks & Gantt chart
// Also aims to have automatic resource levelling and scheduling based on task priority
// Also aims to have resource levels variable within single task
// Also aims to have Gantt chart task bar thickness showing this variable resource usage
// Based on work I started as early as 2005
/*************************************************************************************************/

public class JPlanner extends Application
{
  public static Plan         plan;           // globally accessible plan
  public static MainWindow   gui;            // globally accessible main-window

  public static final String ERROR = "error";

  /******************************************** main *********************************************/
  public static void main( String[] args )
  {
    // main entry point for application startup
    trace( "~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ JPlanner started ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~" );
    trace( plan );
    plan = new Plan();
    trace( plan );
    plan.initialise();
    trace( plan );

    // launch main application display
    launch( args );

    trace( "~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ JPlanner ended ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~" );
  }

  /******************************************** start ********************************************/
  @Override
  public void start( Stage stage ) throws Exception
  {
    // create main window
    gui = new MainWindow( stage );
    gui.load( new File( "C:\\Users\\Richard\\Desktop\\tests\\ttt.xml" ) );
  }

  /******************************************* trace *********************************************/
  public static void trace( Object... objects )
  {
    // prints space separated objects in string representation prefixed by date-time and suffixed by file+line-number & method
    StringBuilder str = new StringBuilder();
    for ( Object obj : objects )
    {
      if ( obj == null )
        str.append( "null " );
      else
        str.append( obj.toString() + " " );
    }

    StackTraceElement[] stack = new Throwable().getStackTrace();
    String method = stack[1].getMethodName() + "()";
    String file = "(" + stack[1].getFileName() + ":" + stack[1].getLineNumber() + ") ";
    System.out.println( DateTime.now() + " " + str.toString() + file + method );
  }

  /******************************************* stack *********************************************/
  public static void stack()
  {
    // prints stack
    StackTraceElement[] stack = new Throwable().getStackTrace();
    System.out.println( DateTime.now() + "======================= STACK =======================" );
    for ( int i = 1; i < stack.length; i++ )
      System.out.println( "\t" + stack[i] );
    System.out.println( DateTime.now() + "===================== STACK END =====================" );
  }

  /******************************************* clean *********************************************/
  public static String clean( String txt )
  {
    // returns a clean string
    return txt.trim().replaceAll( "\\s", " " ).replaceAll( "(\\s{2,})", " " );
  }

  /*************************************** tool-tip hack *****************************************/
  static
  {
    // hack tool-tip durations behaviour (temporary until Java 9)
    try
    {
      Tooltip obj = new Tooltip();
      Class<?> clazz = obj.getClass().getDeclaredClasses()[1];
      Constructor<?> constructor = clazz.getDeclaredConstructor( Duration.class, Duration.class, Duration.class,
          boolean.class );
      constructor.setAccessible( true );
      Object tooltipBehavior = constructor.newInstance( new Duration( 100 ), //open
          new Duration( 60000 ), //visible
          new Duration( 200 ), //close
          false );
      Field fieldBehavior = obj.getClass().getDeclaredField( "BEHAVIOR" );
      fieldBehavior.setAccessible( true );
      fieldBehavior.set( obj, tooltipBehavior );
    }
    catch ( Exception exception )
    {
      trace( exception );
    }
  }
}
