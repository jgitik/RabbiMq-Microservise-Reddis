package com.example.demo.domain;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.io.Serializable;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Task implements Serializable {
    //Все классы через реббит - должны быть серилизуемые

    String id;
    Integer port;


}
