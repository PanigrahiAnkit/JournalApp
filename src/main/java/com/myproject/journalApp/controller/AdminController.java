package com.myproject.journalApp.controller;

import com.myproject.journalApp.cache.AppCache;
import com.myproject.journalApp.dto.AdminUserDto;
import com.myproject.journalApp.entity.User;
import com.myproject.journalApp.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
@Tag(
        name = "Admin APIs",
        description = "Endpoints for administrative operations such as user management and cache control."
)
public class AdminController {

    private final UserService userService; // This works same as @Autowired

    private final AppCache appCache;

    @GetMapping("/all-users")
    @Operation(
            summary = "Get all users",
            description = "Retrieves a list of all users in the system."
    )
    @ApiResponse(responseCode = "200", description = "Users found")
    @ApiResponse(responseCode = "404", description = "No users found")
    public ResponseEntity<?> getAllUsers() {
        List<User> all = userService.getAll();
        if (all != null && !all.isEmpty()) {
            return new ResponseEntity<>(all, HttpStatus.OK);
        }
        return new ResponseEntity<>(all, HttpStatus.NOT_FOUND);
    }


    // first user has been manually added as an admin, since without it no one can with "USER" role can even access
    // the "admin" endpoint and hence cannot access the admin data, so one user was manually given "ADMIN".
    @PostMapping("/create-admin-user")
    @Operation(
            summary = "Create admin user",
            description = "Creates a new user with admin privileges."
    )
    @ApiResponse(responseCode = "200", description = "Admin user created")
    @ApiResponse(responseCode = "400", description = "Invalid input")
    public void createUser(@RequestBody AdminUserDto adminUserDto) {
        User user = new User();
        user.setUserName(adminUserDto.getUserName());
        user.setEmail(adminUserDto.getEmail());
        user.setPassword(adminUserDto.getPassword());
        userService.saveAdmin(user);
    }

    @GetMapping("/clear-app-cache")
    @Operation(
            summary = "Clear application cache",
            description = "Clears and reinitializes the application cache."
    )

    @ApiResponse(responseCode = "200", description = "Cache cleared")
    public void clearAppCache() {
        appCache.init();
    }
}