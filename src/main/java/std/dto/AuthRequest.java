package std.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import std.entitty.Role;
import std.entitty.User;

import java.util.List;
import java.util.stream.Collectors;

@Data
@AllArgsConstructor
public class AuthRequest {
    private Long userId;
    private String username;
    private List<String> roles;

    public static AuthRequest of(User user) {
        return new AuthRequest(user.getId(), user.getUsername(), defineRoles(user));
    }

    private static List<String> defineRoles(User user) {
        return user.getRoles().stream().map(Role::getRolename).collect(Collectors.toList());
    }
}
