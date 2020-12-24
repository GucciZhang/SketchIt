Jeff Zhang
20772204 j927zhan
openjdk version "11.0.8" 2020-07-14
Microsoft Windows 10 Home 10.0.18363 N/A Build 18363


IView interface, Model class based on example code

Max window size: 1900 x 1400
Min window size: 600 x 500

****************
DRAWING
****************
When drawing shapes, dragging the mouse outside the canvas will not continue drawing the shape.
When drawing line, initial click is start point, dragging sets end point.
When drawing circle, initial click is center, dragging sets radius.
When drawing rectangle, initial click is one corner, dragging sets opposite corner.

****************
TOOLS
****************
For erase and fill, the full mouse click (press and release) must occur in the same shape for operation
 to happen. If mouse is dragged outside initial shape, nothing will happen.

For select, user must click (press & release) on a shape for it to be selected. Then, they interact
 with it by starting in the selection box and dragging.

When a shape is selected, a translucent bounding box appears around it.

***************
UI DISABLING
***************
All 4 colour/style options are enabled when using: circle, rect, selected a circle, selected a rect.
Only Line colour, line thickness, line style are enabled when using: line, selected a line.
Only Fill colour enabled when using: fill.
Otherwise all disabled.

****************
MENU BAR
****************
SketchIt canvas is saved as .txt file.
SketchIt can only load valid SketchIt .txt file.
If file save/load fails will display error dialog.

When load with unsaved changes, save dialog will popup. Cancelling in save window will cancel load as well.
When open new canvas, the selected tools & colours & styles don't change.
When open new canvas, it is unsaved.

Quitting application from Window X button will not prompt to save.

Bonus: copy-cut-paste


