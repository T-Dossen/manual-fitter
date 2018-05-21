package edu.ung.phys;

import processing.core.PApplet;

import java.io.BufferedReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashSet;

import net.objecthunter.exp4j.Expression;
import net.objecthunter.exp4j.ExpressionBuilder;

/**
 * @author naharrison
 */
public class ManualFitter extends PApplet {

  public static void main(String[] args) {
    PApplet.main("edu.ung.phys.ManualFitter");
  }


  public Expression expr;
  ArrayList<String> varNames;
  ArrayList<Double> parMin, parMax, parVal, sliderX, xdata, ydata;
  double xmin, xmax, ymin, ymax;
  int slideXstart, slideXend, slideDeltaY;


  public void settings() {
    size(1200, 640);
  }


  public void setup() {
    frameRate(20);
    slideXstart = 20;
    slideXend = 295;
    slideDeltaY = 60;
    BufferedReader fcnReader = createReader("fitFunction.txt");
    Txt2FitFcn txt2ff = new Txt2FitFcn(fcnReader);
    parMin = txt2ff.min;
    parMax = txt2ff.max;
    parVal = new ArrayList<>(parMin);
    varNames = txt2ff.varNames;
    sliderX = new ArrayList<>(Collections.nCopies(parMin.size(), (double) slideXstart));
    expr = new ExpressionBuilder(txt2ff.formattedFcn)
        .variables(new LinkedHashSet<String>(varNames))
        .build();
    BufferedReader dataReader = createReader("XYdata.txt");
    Txt2Data txt2d = new Txt2Data(dataReader);
    xdata = txt2d.x;
    ydata = txt2d.y;
    xmin = Collections.min(xdata);
    xmax = Collections.max(xdata);
    ymin = Collections.min(ydata);
    ymax = Collections.max(ydata);
  }


  public void draw() {
    background(200);
    strokeWeight(1);

    pushMatrix();
    translate(width/4 + 45, 20);
    scale((float) 0.7, (float) 0.92);
    fill(240);
    rect(0, 0, width, height);
    plotData();
    plotFcn();
    labelAxes();
    popMatrix();

    makeSliders();

    update();
  }


  private void plotData() {
    fill(0, 0, 255);
    for(int k = 0; k < xdata.size(); k++) {
      double x = ((xdata.get(k) - xmin)/(xmax - xmin))*width;
      double y = height - ((ydata.get(k) - ymin)/(ymax - ymin))*height;
      ellipse((float) x, (float) y, 10, 8);
    }
  }


  private void plotFcn() {
    fill(255, 0, 0);
    for(int k = 0; k < 250; k++) {
      double x = xmin + k*((xmax - xmin)/250.0);
      expr.setVariable("x", x);
      for(int j = 1; j < varNames.size(); j++) {
        expr.setVariable(varNames.get(j), parVal.get(j-1));
      }
      double y = expr.evaluate();
      ellipse((float) ((x-xmin)*width/(xmax-xmin)), (float) (height - (y-ymin)*height/(ymax-ymin)), 8, 6);
    }
  }


  private void labelAxes() {
    fill(0);
    textSize(16);
    text(String.format("%3.2f", xmin), -8, height+18);
    text(String.format("%3.2f", xmax), width-25, height+18);
    text(String.format("%3.2f", ymin), -45, height+2);
    text(String.format("%3.2f", ymax), -45, 8);
  }


  private void makeSliders() {
    strokeWeight(3);
    for(int k = 0; k < parMin.size(); k++) {
      stroke(0);
      line(slideXstart, (k+1)*slideDeltaY, slideXend, (k+1)*slideDeltaY);
      noStroke();
      fill(75);
      ellipse(sliderX.get(k).floatValue(), (k+1)*slideDeltaY, 15, 15);
      fill(0);
      textSize(14);
      text(String.format("%3.2f", parVal.get(k)), slideXstart, (k+1)*slideDeltaY + 20);
    }
  }


  private void update() {
    if(mousePressed && mouseX > slideXstart && mouseX < slideXend) {
      if((mouseY%slideDeltaY < 8 || mouseY%slideDeltaY > slideDeltaY-8) && mouseY < slideDeltaY*parVal.size()+8) {
        int sliderIndex = Math.round(((float) mouseY)/slideDeltaY) - 1;
        sliderX.set(sliderIndex, (double) mouseX);
        double newVal = parMin.get(sliderIndex) + (((double) mouseX - slideXstart)/(slideXend - slideXstart))*(parMax.get(sliderIndex) - parMin.get(sliderIndex));
        parVal.set(sliderIndex, newVal);
      }
    }
  }


}
