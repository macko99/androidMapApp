package pl.mzlnk.emergencyspot.model.user;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class NewUserDto {

    private String username;
    private String firstName;
    private String lastName;
    private String pesel;
    private String password;

}
