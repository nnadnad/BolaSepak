package android.example.pbd_bolasepak;

public class MatchDetails {
    String homeName;
    String homeSkor;
    String homeBadge;

    String awayName;
    String awaySkor;
    String awayBadge;

    String date;

    public MatchDetails(String homeName, String homeSkor, String homeBadge, String awayName, String awaySkor, String awayBadge, String date) {
        this.homeName = homeName;
        this.homeSkor = homeSkor;
        this.homeBadge = homeBadge;

        this.awayName = awayName;
        this.awaySkor = awaySkor;
        this.awayBadge = awayBadge;

        this.date = date;
    }

    public String getHomeName() {
        return this.homeName;
    }

    public String getHomeBadge() {
        return  this.homeBadge;
    }

    public String getHomeSkor() {
        return  this.homeSkor;
    }

    public String getAwayName() {
        return this.awayName;
    }

    public String getAwaySkor() {
        return this.awaySkor;
    }

    public String getAwayBadge() {
        return  this.awayBadge;
    }

    public String getDate() {
        return  this.date;
    }
}
