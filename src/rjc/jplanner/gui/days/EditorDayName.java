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

package rjc.jplanner.gui.days;

import rjc.jplanner.JPlanner;
import rjc.jplanner.gui.XTextField;
import rjc.jplanner.gui.table.EditorText;

/*************************************************************************************************/
/****************************** Table cell editor for day-type name ******************************/
/*************************************************************************************************/

public class EditorDayName extends EditorText
{

  /**************************************** constructor ******************************************/
  public EditorDayName( int columnIndex, int rowIndex )
  {
    // create editor
    super( columnIndex, rowIndex );

    // add listener to set error status
    ( (XTextField) getControl() ).textProperty().addListener( ( observable, oldText, newText ) ->
    {
      // length must be between 1 and 40 characters long
      String error = null;
      String tidy = JPlanner.clean( newText ).trim();
      int len = tidy.length();
      if ( len < 1 || len > 40 )
        error = "Name length not between 1 and 40 characters";

      // name should be unique
      if ( JPlanner.plan.daytypes.isDuplicateDayName( tidy, rowIndex ) )
        error = "Name not unique";

      // display error message and set editor error status
      JPlanner.gui.setError( getControl(), error );
    } );

  }

  /******************************************* getValue ******************************************/
  @Override
  public Object getValue()
  {
    // return editor text cleaned and trimmed
    return JPlanner.clean( (String) super.getValue() ).trim();
  }

}
