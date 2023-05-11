package com.example.shop.dto;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;

@Getter
@Setter
public class MemberFormDto {

    @NotBlank(message = "이름은 필수 입력 값입니다.")
    private String name;

    @NotBlank(message = "이메일은 필수 입력 값입니다.")
    @Email(message = "이메일 형식으로 입력해주세요.")
    private String email;

    @NotBlank(message = "주소는 필수 입력 값입니다.")
    private String address;

    @NotBlank(message = "비밀번호 필수 입력 값입니다.")
    @Length(min = 8, max = 16, message = "비밀번호는 8 이상, 16 이하 입니다.")
    private String password;

}
