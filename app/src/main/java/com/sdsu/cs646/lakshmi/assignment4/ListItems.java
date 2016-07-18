package com.sdsu.cs646.lakshmi.assignment4;

/**
 * Created by lakshmimohandev on 3/19/16.
 */
public class ListItems
{
    private String id;
    private String latitude;
    private String longitude;
    private String type;
    private String description;
    private String created;
    private String imagetype;
    private String thumbnail;


    public String getId()
    {
        return this.id;
    }
    public void setId(String id)
    {
        this.id = id;
    }
    public String getLatitude()
    {
        return latitude;
    }
    public void setLatitude(String latitude)
    {
        this.latitude = latitude;
    }
    public String getLongitude()
    {
        return longitude;
    }
    public void setLongitude(String longitude)
    {
        this.longitude = longitude;
    }
    public String getType()
    {
        return type;
    }
    public void setType(String type)
    {
        this.type = type;
    }
    public String getDescription()
    {
        return description;
    }
    public void setDescription(String description)
    {
        this.description = description;
    }
    public String getCreated()
    {
        return created;
    }
    public void setCreated_date(String created)
    {
        this.created = created;
    }
    public String getThumbnail()
    {
        return thumbnail;
    }
    public void setThumbnail(String thumbnail)
    {
        this.thumbnail = thumbnail;
    }
    public String getImagetype() {
        return imagetype;
    }
    public void setImagetype(String imagetype) {
        this.imagetype = imagetype;
    }

    @Override
    public String toString()
    {
        return "PothholeData{" +
                "id=" + id +
                ", latitude='" + latitude + '\'' +
                ", longitude='" + longitude + '\'' +
                ", type='" + type + '\'' +
                ", description='" + description + '\'' +
                ", created_date='" + created + '\'' +
                ", imagetype='" + imagetype + '\'' +
                '}';
    }
}
