package com.example.psm.Controller;

public class Row {
   //isHeader guna utk tanda dalam data sama ad dammy data or x
    //isheader nk tau dlu bru dia display
    //bydefault false bukan dammy (data betoi)
    //bydefault true dammy(xdata)

    public boolean isHeader(){
      return row;

    }
    private boolean row = false;

    public void isHeader(boolean list){
        row = list;
    }
}
