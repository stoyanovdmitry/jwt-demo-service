package std.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import std.entitty.User;

@Data
@AllArgsConstructor
public class AuthRequest {
    private Long userId;
    private String username;

    public static AuthRequest of(User user) {
        return new AuthRequest(user.getId(), user.getUsername());
    }
}
