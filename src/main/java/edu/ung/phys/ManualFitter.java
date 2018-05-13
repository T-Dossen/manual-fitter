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
	ArrayList<Double> parMin, parMax, xdata, ydata;
	double xmin, xmax, ymin, ymax;

	
	public void settings() {
		size(1200, 640);
	}


	public void setup() {
		frameRate(10);
		BufferedReader fcnReader = createReader("fitFunction.txt");
		Txt2FitFcn txt2ff = new Txt2FitFcn(fcnReader);
		parMin = txt2ff.min;
		parMax = txt2ff.max;
		varNames = txt2ff.varNames;
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
		pushMatrix();
		translate(50, 50);
		scale((float) 0.6, (float) 0.6);
		fill(240);
		rect(0, 0, width, height);
		plotData();
		plotFcn();
		popMatrix();
	}


	private void plotData() {
		fill(0, 0, 255);
		for(int k = 0; k < xdata.size(); k++) {
			double x = ((xdata.get(k) - xmin)/(xmax - xmin))*width;
			double y = height - ((ydata.get(k) - ymin)/(ymax - ymin))*height;
			ellipse((float) x, (float) y, 8, 8);
		}
	}
	
	
	private void plotFcn() {
		fill(255, 0, 0);
		for(int k = 0; k < 200; k++) {
			double x = xmin + k*((xmax - xmin)/200.0);
			expr.setVariable("x", x);
			for(int j = 1; j < varNames.size(); j++) {
				expr.setVariable(varNames.get(j), parMin.get(j-1));
			}
			double y = expr.evaluate();
			ellipse((float) ((x-xmin)*width/(xmax-xmin)), (float) (height - (y-ymin)*height/(ymax-ymin)), 6, 6);
		}
	}

	
}
