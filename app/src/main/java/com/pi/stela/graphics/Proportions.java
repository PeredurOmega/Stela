package com.pi.stela.graphics;

/**
 * Created by Paul on 08/10/2016.
 */
import android.graphics.Point;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.WindowManager;

public class Proportions{
    public double Height = 0;
    public double Width = 0;
    public double HProportion(WindowManager WM){
        Display display = WM.getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        double HProportion = 0;
        double height = size.y;
        Height = height;
        HProportion = height/1920;
        return HProportion;
    }
    public double WProportion(WindowManager WM){
        Display display = WM.getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        double WProportion = 0;
        double width = size.x;
        Width = width;
        WProportion = width/1080;
        return WProportion;
    }
    public double SProportion(WindowManager WM){
        Display display = WM.getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        double SProportion = 0;
        DisplayMetrics dm = new DisplayMetrics();
        WM.getDefaultDisplay().getMetrics(dm);
        int width=dm.widthPixels;
        int height=dm.heightPixels;
        int dens=dm.densityDpi;
        double wi=(double)width/(double)dens;
        double hi=(double)height/(double)dens;
        double x = Math.pow(wi,2);
        double y = Math.pow(hi,2);
        double screenInches = Math.sqrt(x+y);
        SProportion = screenInches/12;
        return SProportion;
    }
}
