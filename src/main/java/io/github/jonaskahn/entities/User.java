package io.github.jonaskahn.entities;

import io.github.jonaskahn.entities.converter.StringCollectionConverter;
import io.github.jonaskahn.entities.enums.Status;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.proxy.HibernateProxy;

import java.util.List;
import java.util.Objects;

@Getter
@Setter
@Entity
@Table(name = "users")
@ToString
@RequiredArgsConstructor
@Builder
@AllArgsConstructor
public class User extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Size(max = 128)
    @NotNull
    @Column(name = "username", nullable = false, length = 128)
    private String username;

    @NotNull
    @Column(name = "preferred_username", nullable = false)
    private Long preferredUsername;

    @Size(max = 128)
    @NotNull
    @Column(name = "email", nullable = false, length = 128)
    private String email;

    @Size(max = 255)
    @Column(name = "full_name")
    private String fullName;

    @Size(max = 80)
    @NotNull
    @Column(name = "password", nullable = false, length = 80)
    private String password;

    @NotNull
    @ColumnDefault("1")
    @Column(name = "status", nullable = false)
    private Status status;

    @Lob
    @Column(name = "roles")
    @ColumnDefault("[\"app:user\"]")
    @Convert(converter = StringCollectionConverter.class)
    private List<String> roles;

    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;
        Class<?> oEffectiveClass = o instanceof HibernateProxy ? ((HibernateProxy) o).getHibernateLazyInitializer().getPersistentClass() : o.getClass();
        Class<?> thisEffectiveClass = this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass() : this.getClass();
        if (thisEffectiveClass != oEffectiveClass) return false;
        User user = (User) o;
        return getId() != null && Objects.equals(getId(), user.getId());
    }

    @Override
    public final int hashCode() {
        return this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass().hashCode() : getClass().hashCode();
    }
}