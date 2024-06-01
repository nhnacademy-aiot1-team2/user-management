package domainTest;

import com.user.management.entity.Provider;
import com.user.management.entity.Role;
import com.user.management.entity.Status;
import com.user.management.entity.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

class EntityTest {

    Role role = new Role(1L, "admin");
    Status status = new Status(1L, "active");
    Provider provider = new Provider("Google", "google");

    @Test
    @DisplayName("userEntity 생성 테스트")
    void userEntityTest() {
        User user = User.builder()
                .id("user")
                .name("name")
                .password("password")
                .email("user@contxt.co.kr")
                .role(role)
                .status(status)
                .provider(provider)
                .createdAt(LocalDateTime.of(2023,4,1,15,30,0))
                .latestLoginAt(LocalDateTime.of(2023,5,5,5,24,0))
                .build();

        assertThat(user.getId()).isEqualTo("user");
        assertThat(user.getName()).isEqualTo("name");
        assertThat(user.getPassword()).isEqualTo("password");
        assertThat(user.getEmail()).isEqualTo("user@contxt.co.kr");
        assertThat(user.getRole()).isEqualTo(role);
        assertThat(user.getStatus()).isEqualTo(status);
        assertThat(user.getProvider()).isEqualTo(provider);
        assertThat(user.getCreatedAt()).isEqualTo(LocalDateTime.of(2023,4,1,15,30,0));
        assertThat(user.getLatestLoginAt()).isEqualTo(LocalDateTime.of(2023,5,5,5,24,0));

        //join column test
        assertThat(user.getRole().getId()).isEqualTo(1L);
        assertThat(user.getRole().getName()).isEqualTo("admin");

        assertThat(user.getStatus().getId()).isEqualTo(1L);
        assertThat(user.getStatus().getName()).isEqualTo("active");

        assertThat(user.getProvider().getId()).isEqualTo("Google");
        assertThat(user.getProvider().getName()).isEqualTo("google");
    }

    @Test
    @DisplayName("roleEntity 생성 테스트")
    void roleTest() {
        Role role = Role.builder()
                .id(1L)
                .name("user")
                .build();

        assertThat(role.getId()).isEqualTo(1L);
        assertThat(role.getName()).isEqualTo("user");
    }

    @Test
    @DisplayName("StatusEntity 생성 테스트")
    void statusTest() {
        Status status = Status.builder()
                .id(1L)
                .name("ACTIVE")
                .build();

        assertThat(status.getId()).isEqualTo(1L);
        assertThat(status.getName()).isEqualTo("ACTIVE");
    }

    @Test
    @DisplayName("providerEntity 생성 테스트")
    void providerTest() {
        Provider provider = Provider.builder()
                .id("GOOGLE")
                .name("google")
                .build();

        assertThat(provider.getId()).isEqualTo("GOOGLE");
        assertThat(provider.getName()).isEqualTo("google");
    }
}
