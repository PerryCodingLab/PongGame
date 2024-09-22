// 322877754 Omer Perry

import java.util.Random;
import java.awt.Color;

import biuoop.DrawSurface;
import biuoop.GUI;

/**
 * The AbstractArtDrawing class creates random lines and performs various
 * operations on them,
 * such as drawing intersections, triangles, and middle points.
 * It also demonstrates the use of the Line class in generating and manipulating
 * lines.
 *
 * @author 322877754 Omer Perry
 */
public class AbstractArtDrawing {
   private Line[] lines;
   private int index;

   /**
    * Constructs an AbstractArtDrawing object with a specified number of random
    * lines.
    *
    * @param n The number of random lines to generate.
    * @param d The DrawSurface on which to draw the lines.
    */
   public AbstractArtDrawing(int n, DrawSurface d) {
      Random rand = new Random();
      this.lines = new Line[n];
      this.index = 0;
      for (int i = 0; i < n; i++) {
         if (i == 1) {
            //this.lines[i] = new Line(new Point(d.getWidth() / 2, d.getHeight()), new Point(d.getWidth()/ 2, 0));
            this.lines[i] = new Line(d.getWidth() / 2, d.getHeight(),d.getWidth()/ 2, 0);
         } else if (i == 2) {
            this.lines[i] = new Line(0, d.getHeight() / 2 + 50,d.getWidth(), d.getHeight() / 2 + 50);
         } else {
            this.lines[i] = Line.generateRandomLine(d.getWidth(), d.getHeight());
         }
         Line.drawLine(lines[i], d);
      }
   }

   /**
    * The main method creates a GUI and an AbstractArtDrawing object.
    * Then uses all of its methods.
    *
    * @param args Command line arguments (not used).
    */
   public static void main(String[] args) {
      GUI gui = new GUI("Random Lines", 600, 600);
      DrawSurface d = gui.getDrawSurface();
      AbstractArtDrawing example = new AbstractArtDrawing(10, d);
      example.drawTriangles(d);
      example.drawIntersections(d);
      example.drawMiddle(d);
      gui.show(d);
   }

   /**
    * Draws circles at the intersections of lines and fills them with the color
    * red.
    *
    * @param d The DrawSurface on which to draw the circles.
    */
   public void drawIntersections(DrawSurface d) {
      d.setColor(Color.RED);
      for (int i = 0; i < this.lines.length - 1; i++) {
         for (int j = i + 1; j < this.lines.length; j++) {
            if (this.lines[i].isIntersecting(this.lines[j])) {
               d.fillCircle((int) this.lines[i].intersectionWith(this.lines[j]).getX(),
                     (int) this.lines[i].intersectionWith(this.lines[j]).getY(), 3);
            }
         }
      }
   }

   /**
    * Draws triangles formed by intersecting lines and fills them with the color
    * green.
    *
    * @param d The DrawSurface on which to draw the triangles.
    */
   public void drawTriangles(DrawSurface d) {
      Point intersectOne;
      Point intersectTwo;
      Point intersectThree;
      d.setColor(Color.GREEN);
      for (int i = 0; i < this.lines.length - 2; i++) {
         for (int j = i + 1; j < this.lines.length - 1; j++) {
            if (this.lines[i].isIntersecting(this.lines[j])) {
               for (int k = j + 1; k < this.lines.length; k++) {
                  if (this.lines[i].isIntersecting(this.lines[j], this.lines[k])) {
                     intersectOne = this.lines[i].intersectionWith(this.lines[j]);
                     intersectTwo = this.lines[i].intersectionWith(this.lines[k]);
                     intersectThree = this.lines[j].intersectionWith(this.lines[k]);
                     d.drawLine((int) intersectOne.getX(), (int) intersectOne.getY(),
                           (int) intersectTwo.getX(), (int) intersectTwo.getY());
                     d.drawLine((int) intersectOne.getX(), (int) intersectOne.getY(),
                           (int) intersectThree.getX(), (int) intersectThree.getY());
                     d.drawLine((int) intersectTwo.getX(), (int) intersectTwo.getY(),
                           (int) intersectThree.getX(), (int) intersectThree.getY());
                  }
               }
            }
         }
      }
   }

   /**
    * Draws circles at the middle points of lines and fills them with the color
    * blue.
    *
    * @param d The DrawSurface on which to draw the circles.
    */
   public void drawMiddle(DrawSurface d) {
      d.setColor(Color.BLUE);
      for (int i = 0; i < this.lines.length; i++) {
         d.fillCircle((int) this.lines[i].middle().getX(), (int) this.lines[i].middle().getY(), 3);
      }
   }
}
