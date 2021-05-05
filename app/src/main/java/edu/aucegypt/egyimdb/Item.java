package edu.aucegypt.egyimdb;

public class Item {
    String buttonListName;
    int buttonListImage;

    public Item(String name, int image)
    {
        this.buttonListImage=image;
        this.buttonListName=name;
    }
    public String getbuttonName()
    {
        return buttonListName;
    }
    public int getbuttonImage()
    {
        return buttonListImage;
    }
}
