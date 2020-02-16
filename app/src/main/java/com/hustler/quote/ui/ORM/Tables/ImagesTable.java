package com.hustler.quote.ui.ORM.Tables;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.hustler.quote.ui.database.Contract;

@Entity(tableName = "mstr_images")
public class ImagesTable {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    public int id;


    @ColumnInfo(name = Contract.Images.IMAGE_ID)
    public String IMAGE_ID;
    @ColumnInfo(name = Contract.Images.IMAGE_USERNAME)
    public String IMAGE_USERNAME;
    @ColumnInfo(name = Contract.Images.IMAGE_PROFILEPIC)
    public String IMAGE_PROFILEPIC;
    @ColumnInfo(name = Contract.Images.IMAGE_REGULAR)
    public String IMAGE_REGULAR;
    @ColumnInfo(name = Contract.Images.IMAGE_HD)
    public String IMAGE_HD;
    @ColumnInfo(name = Contract.Images.IMAGE_UHD)
    public String IMAGE_UHD;
    @ColumnInfo(name = Contract.Images.IMAGE_DOWNLOAD_LOCATION)
    public String IMAGE_DOWNLOAD_LOCATION;
    @ColumnInfo(name = Contract.Images.IMAGE_IS_LIKED)
    public String IMAGE_IS_LIKED;

    public ImagesTable(int id, String IMAGE_ID, String IMAGE_USERNAME, String IMAGE_PROFILEPIC, String IMAGE_REGULAR, String IMAGE_HD, String IMAGE_UHD, String IMAGE_DOWNLOAD_LOCATION, String IMAGE_IS_LIKED) {
        this.id = id;
        this.IMAGE_ID = IMAGE_ID;
        this.IMAGE_USERNAME = IMAGE_USERNAME;
        this.IMAGE_PROFILEPIC = IMAGE_PROFILEPIC;
        this.IMAGE_REGULAR = IMAGE_REGULAR;
        this.IMAGE_HD = IMAGE_HD;
        this.IMAGE_UHD = IMAGE_UHD;
        this.IMAGE_DOWNLOAD_LOCATION = IMAGE_DOWNLOAD_LOCATION;
        this.IMAGE_IS_LIKED = IMAGE_IS_LIKED;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getIMAGE_ID() {
        return IMAGE_ID;
    }

    public void setIMAGE_ID(String IMAGE_ID) {
        this.IMAGE_ID = IMAGE_ID;
    }

    public String getIMAGE_USERNAME() {
        return IMAGE_USERNAME;
    }

    public void setIMAGE_USERNAME(String IMAGE_USERNAME) {
        this.IMAGE_USERNAME = IMAGE_USERNAME;
    }

    public String getIMAGE_PROFILEPIC() {
        return IMAGE_PROFILEPIC;
    }

    public void setIMAGE_PROFILEPIC(String IMAGE_PROFILEPIC) {
        this.IMAGE_PROFILEPIC = IMAGE_PROFILEPIC;
    }

    public String getIMAGE_REGULAR() {
        return IMAGE_REGULAR;
    }

    public void setIMAGE_REGULAR(String IMAGE_REGULAR) {
        this.IMAGE_REGULAR = IMAGE_REGULAR;
    }

    public String getIMAGE_HD() {
        return IMAGE_HD;
    }

    public void setIMAGE_HD(String IMAGE_HD) {
        this.IMAGE_HD = IMAGE_HD;
    }

    public String getIMAGE_UHD() {
        return IMAGE_UHD;
    }

    public void setIMAGE_UHD(String IMAGE_UHD) {
        this.IMAGE_UHD = IMAGE_UHD;
    }

    public String getIMAGE_DOWNLOAD_LOCATION() {
        return IMAGE_DOWNLOAD_LOCATION;
    }

    public void setIMAGE_DOWNLOAD_LOCATION(String IMAGE_DOWNLOAD_LOCATION) {
        this.IMAGE_DOWNLOAD_LOCATION = IMAGE_DOWNLOAD_LOCATION;
    }

    public String getIMAGE_IS_LIKED() {
        return IMAGE_IS_LIKED;
    }

    public void setIMAGE_IS_LIKED(String IMAGE_IS_LIKED) {
        this.IMAGE_IS_LIKED = IMAGE_IS_LIKED;
    }
}
