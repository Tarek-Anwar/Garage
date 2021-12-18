public class GarageInfo {
    private String garageName;
    private String address;
    private String location;

    public GarageInfo(String garageName, String address, String location) {
        this.garageName = garageName;
        this.address = address;
        this.location = location;
    }

    public String getGarageName() {
        return garageName;
    }

    public void setGarageName(String garageName) {
        this.garageName = garageName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }
}
