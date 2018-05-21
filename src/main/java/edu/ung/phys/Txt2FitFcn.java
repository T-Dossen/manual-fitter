package edu.ung.phys;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;

public class Txt2FitFcn {

  public String rawFcn, formattedFcn;
  public ArrayList<Double> min, max;
  public ArrayList<String> varNames;

  public Txt2FitFcn(BufferedReader reader) {
    min = new ArrayList<>();
    max = new ArrayList<>();
    varNames = new ArrayList<>();
    varNames.add("x");
    try {
      String thisLine = reader.readLine();
      rawFcn = thisLine;
      thisLine = reader.readLine();
      while(thisLine != null) {
        String[] thisLineSplit = thisLine.split("\\s+");
        min.add(Double.parseDouble(thisLineSplit[0]));
        max.add(Double.parseDouble(thisLineSplit[1]));
        thisLine = reader.readLine();
      }
      reader.close();
    } catch (IOException e) {
      e.printStackTrace();
    }
    formatFcn();
  }

  private void formatFcn() {
    formattedFcn = rawFcn;

    int nPars = min.size();
    for(int k = 0; k < nPars; k++) {
      int i1 = formattedFcn.indexOf("[");
      int i2 = formattedFcn.indexOf("]");
      String parID = formattedFcn.substring(i1+1, i2);
      String tmpString = formattedFcn.substring(0, i1);
      tmpString += "P" + parID;
      tmpString += formattedFcn.substring(i2+1, formattedFcn.length());
      formattedFcn = tmpString;
      varNames.add("P" + parID);
    }
  }

}
