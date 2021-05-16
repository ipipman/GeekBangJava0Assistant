package org.geektimes.projects.user.domain;

import org.geektimes.projects.user.validator.bean.validation.Phone;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.Min;
import java.io.Serializable;
import java.util.Objects;

import static javax.persistence.GenerationType.AUTO;

/**
 * 用户领域对象
 *
 * @since 1.0
 */
@Entity
@Table(name = "users")
public class User implements Serializable {

    @Id
    // 不做处理
    @Min(value = 1, message = "id需大于0")
    @GeneratedValue(strategy = AUTO)
    private Long id;

    @Column
    @NotBlank(message = "用户名不能为空", groups = {Register.class, Login.class})
    @Length(max = 16, message = "用户名不能超过16个字符", groups = {Register.class, Login.class})
    private String name;

    @Column
    @NotBlank(message = "密码不能为空", groups = {Register.class, Login.class})
    @Length(min = 6, max = 32, message = "密码只支持6~32个字符", groups = {Register.class, Login.class})
    private String password;

    @Column
    @NotBlank(message = "邮箱不能为空", groups = {Register.class})
    @Email(message = "邮箱格式错误", groups = {Register.class})
    private String email;

    @Column
    @NotBlank(message = "手机号不能为空", groups = {Register.class})
    @Phone(message = "手机号格式错误", groups = {Register.class})
    private String phoneNumber;

    public interface Register {}

    public interface Login {}

    public User() {

    }

    public User(String name, String password, String email, String phoneNumber) {
        this.name = name;
        this.password = password;
        this.email = email;
        this.phoneNumber = phoneNumber;
    }

    public User(Long id, String name, String password, String email, String phoneNumber) {
        this.id = id;
        this.name = name;
        this.password = password;
        this.email = email;
        this.phoneNumber = phoneNumber;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(id, user.id) && Objects.equals(name, user.name) && Objects.equals(password, user.password) && Objects.equals(email, user.email) && Objects.equals(phoneNumber, user.phoneNumber);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, password, email, phoneNumber);
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", password='" + password + '\'' +
                ", email='" + email + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                '}';
    }
}
