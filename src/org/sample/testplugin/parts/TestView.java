package org.sample.testplugin.parts;
//package org.sample.testplugin.view;

import org.eclipse.swt.*;
import org.eclipse.swt.events.*;
import org.eclipse.swt.widgets.*;
import org.eclipse.ui.*;
import org.eclipse.ui.views.*;
import org.eclipse.ui.part.ViewPart;





public class TestView extends ViewPart {
   public void createPartControl(Composite parent) {
   final Canvas clock = new Canvas(parent,SWT.NONE);
   clock.addPaintListener(new PaintListener() {
      public void paintControl(PaintEvent e) {
       e.gc.drawArc(e.x,e.y,e.width-1,e.height-1,0,360);
      }
   });
   
   
   }
   
   
public void setFocus() {
}

}