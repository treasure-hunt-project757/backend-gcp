package sheba.backend.app.entities;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import lombok.ToString;
import sheba.backend.app.DTO.AdminDTO;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "Task")
@Data
@ToString(exclude = {"questionTask", "mediaList", "admin"})
//@Inheritance(strategy = InheritanceType.JOINED)
public class Task {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long taskID;
    private String name;
    private String description;
    private boolean withMsg;
    private long adminIDAPI;

    @ElementCollection
    @CollectionTable(name = "task_text", joinColumns = @JoinColumn(name = "task_id"))
    @Column(name = "text")
    private List<String> taskFreeTexts;

    @OneToOne(mappedBy = "task", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private QuestionTask questionTask;

    @OneToMany(mappedBy = "task", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<MediaTask> mediaList = new ArrayList<>();

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fk_admin_id")
    private Admin admin;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Task task = (Task) o;
        return taskID == task.taskID;
    }

    @Override
    public int hashCode() {
        return Objects.hash(taskID);
    }
}
