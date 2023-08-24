package searchengine.model;

import com.sun.istack.NotNull;
import lombok.Data;

import javax.persistence.*;

@Entity
@Table(name = "page", indexes = @Index(name = "path_index", columnList = "path, site_id", unique = true))
@Data
public class Page {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    @JoinColumn(name = "site_id", referencedColumnName = "id", nullable = false)
    private Website website;

    @Column(columnDefinition = "VARCHAR(255)", nullable = false)
    private String path;

    @NotNull
    private int code;

    @Column(columnDefinition = "MEDIUMTEXT", nullable = false)
    private String content;
}
