package org.geektimes.rest.demo;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.HashMap;

public class RestClientDemo {

    public static void main(String[] args) {
        // https://api.apiopen.top/getJoke?page=1&count=2&type=video
        Client client = ClientBuilder.newClient();
        HashMap<Object, Object> params = new HashMap<>();
        params.put("page", 1);
        params.put("count", 2);
        params.put("type", "video");
        Entity<Object> entity = Entity.entity(params, MediaType.APPLICATION_FORM_URLENCODED_TYPE);
        Response response = client
                .target("https://api.apiopen.top/getJoke")
                .request()
                .post(entity);
        System.out.println("getJoke测试结果：" + response.readEntity(String.class));

        // http://echo.apipost.cn/get.php
        entity = Entity.entity(new User(1L, "张三", "123456"), MediaType.APPLICATION_JSON_TYPE);
        response = client
                .target("http://echo.apipost.cn/get.php")
                .request()
                .post(entity);
        System.out.println("apipost测试结果：" + response.readEntity(String.class));
    }

    static class User {
        private Long id;

        private String name;

        private String password;

        public User(Long id, String name, String password) {
            this.id = id;
            this.name = name;
            this.password = password;
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
    }

}
