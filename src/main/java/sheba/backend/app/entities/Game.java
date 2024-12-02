package sheba.backend.app.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;

import java.util.Comparator;
import java.util.List;

@Entity
@Table(name = "Game")
@Data
public class Game {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long gameID;
    private long adminID;
    private String gameName;
    private String description;
    private String QRCodePath;
    private String QRCodeURL;



    @OneToOne(mappedBy = "game", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private GameImage gameImage;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fk_admin_id")
    private Admin admin;

    @OneToMany(mappedBy = "game", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Unit> units;

    public void sortUnits() {
        units.sort(Comparator.comparingInt(Unit::getUnitOrder));
    }

    @Override
    public String toString() {
        return "Game{" +
                "gameID=" + gameID +
                ", gameName='" + gameName + '\'' +
                ", description='" + description + '\'' +
                ", gameImage=" + gameImage +
                ", admin=" + admin.getAdminID() + " "+ admin.getUsername()+
                '}';
    }
}
